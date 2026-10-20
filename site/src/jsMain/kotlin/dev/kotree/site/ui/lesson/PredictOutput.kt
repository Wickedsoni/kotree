package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorFeedbackCorrectTintVar
import dev.kotree.site.colorFeedbackCorrectVar
import dev.kotree.site.colorFeedbackHintTintVar
import dev.kotree.site.colorFeedbackHintVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Fieldset
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Legend
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

/** One answer option of a Predict card (docs/06 section 3.2). [why] is hidden until the learner checks. */
class PredictOption(val id: String, val text: String, val why: String, val correct: Boolean = false)

/** Tone of a feedback box (docs/06 section 4): never red, never a cross. */
internal enum class FeedbackKind(val iconPath: String) {
    /** Gentle confirm: a tick. */
    Correct("M5 12.5l4.5 4.5L19 7.5"),
    /** Neutral: a wrong guess, shown calmly: an arrow pointing on. */
    Neutral("M5 12h14M13 6l6 6-6 6"),
    /** A nudge: a question mark. */
    Hint("M9.5 9.5a2.5 2.5 0 1 1 3.5 2.3c-.7.4-1 .9-1 1.7M12 17.5v.01"),
}

/** Inline SVG so the icons never fall back to a system font. Stroke is currentColor, size is the body type token. */
private fun iconSvg(path: String): String =
    "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"100%\" height=\"100%\" fill=\"none\" " +
        "stroke=\"currentColor\" stroke-width=\"2.25\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"$path\"/></svg>"

/**
 * Feedback box: an icon (decorative) plus words, so meaning never rests on colour (docs/02 section 6).
 * [focusOnShow] moves keyboard focus to the box when it appears, for the case where the control that was focused is gone.
 */
@Composable
internal fun FeedbackBox(
    kind: FeedbackKind,
    heading: String,
    modifier: Modifier = Modifier,
    focusOnShow: Boolean = false,
    body: @Composable () -> Unit = {},
) {
    Div(
        modifier
            .attrsModifier { attr("role", "status") }
            .styleModifier {
                property("box-sizing", "border-box")
                property("display", "flex")
                property("gap", space3Var.value())
                property("padding", space4Var.value())
                property("border-radius", shapeMdVar.value())
                property("color", colorOnSurfaceVar.value())
                when (kind) {
                    FeedbackKind.Correct -> {
                        property("background-color", colorFeedbackCorrectTintVar.value())
                        property("border", "${Tokens.Elevation.borderPx}px solid ${colorFeedbackCorrectVar.value()}")
                    }
                    FeedbackKind.Hint -> {
                        property("background-color", colorFeedbackHintTintVar.value())
                        property("border", "${Tokens.Elevation.borderPx}px solid ${colorFeedbackHintVar.value()}")
                    }
                    FeedbackKind.Neutral -> property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                }
                typo(Typo.Body)
            }.toAttrs {
                if (focusOnShow) {
                    attr("tabindex", "-1")
                    ref { element ->
                        element.focus()
                        onDispose { }
                    }
                }
            }
    ) {
        Span(
            Modifier.attrsModifier { attr("aria-hidden", "true") }
                .styleModifier {
                    property("flex", "none")
                    property("display", "inline-block")
                    property("width", typeBodyLineVar.value())
                    property("height", typeBodyLineVar.value())
                    when (kind) {
                        FeedbackKind.Correct -> property("color", colorFeedbackCorrectVar.value())
                        FeedbackKind.Hint -> property("color", colorFeedbackHintVar.value())
                        FeedbackKind.Neutral -> property("color", colorOnSurfaceVar.value())
                    }
                }.toAttrs {
                    ref { element ->
                        element.innerHTML = iconSvg(kind.iconPath)
                        onDispose { }
                    }
                }
        )
        Div {
            P(Modifier.styleModifier { property("margin", "0"); property("font-weight", typeTitleWeightVar.value()) }.toAttrs()) { Text(heading) }
            body()
        }
    }
}

/**
 * PredictOutput: a static Predict-the-output card (docs/06 sections 3.2, 4, 6). Code is shown, the learner picks A, B or
 * C, presses "Run it to check" (disabled until an option is chosen). No answer, no `why` and no output is shown before
 * that press (they are in the data but not in the page). There is no score and no timer; "Try again" resets.
 * The real Playground run is Phase 6: [onCheck] is called with the chosen option id, and the card reveals the stored
 * [output] and the `why` of the chosen option, which is the offline path of docs/06 section 4.
 */
@Composable
fun PredictOutput(
    id: String,
    code: String,
    options: List<PredictOption>,
    output: String,
    modifier: Modifier = Modifier,
    branch: String = "kotlin",
    onCheck: (String) -> Unit = {},
) {
    var chosen by remember { mutableStateOf<String?>(null) }
    var checked by remember { mutableStateOf(false) }
    var retries by remember { mutableStateOf(0) }
    val picked = options.firstOrNull { it.id == chosen }
    // After "Try again" the button turns disabled (nothing chosen), which drops focus: put it on the first option instead.
    LaunchedEffect(retries) {
        if (retries > 0) (document.getElementById("$id-${options.first().id}") as? HTMLElement)?.focus()
    }
    Div(
        modifier.attrsModifier { attr("role", "group"); attr("aria-label", "Predict the output") }
            .styleModifier { frame(); property("padding", space4Var.value()); property("color", colorOnSurfaceVar.value()) }.toAttrs()
    ) {
        CodeBlock(code, branch = branch, label = "Code to predict")
        Fieldset(
            Modifier.attrsModifier { if (checked) attr("disabled", "") }
                .styleModifier { property("border", "0"); property("margin", "${space4Var.value()} 0 0"); property("padding", "0") }.toAttrs()
        ) {
            Legend(Modifier.styleModifier { property("padding", "0"); typo(Typo.Title) }.toAttrs()) { Text("What does it print?") }
            options.forEachIndexed { index, option ->
                val inputId = "$id-${option.id}"
                Label(
                    forId = inputId,
                    attrs = Modifier.styleModifier {
                        property("display", "flex")
                        property("align-items", "center")
                        property("gap", space3Var.value())
                        property("min-height", Tokens.Spacing.minTargetPx.px)
                        property("cursor", "pointer")
                        typo(Typo.Body)
                    }.toAttrs()
                ) {
                    Input(InputType.Radio) {
                        id(inputId)
                        attr("name", id)
                        checked(chosen == option.id)
                        value(option.id)
                        onClick { chosen = option.id }
                    }
                    Span { Text("${'A' + index}. ") }
                    Span(Modifier.styleModifier { typo(Typo.Code) }.toAttrs()) { Text(option.text) }
                }
            }
        }
        Div(Modifier.styleModifier { property("margin-top", space3Var.value()); property("display", "flex"); property("gap", space2Var.value()) }.toAttrs()) {
            // One call site keeps the same DOM node when the label changes, so focus stays on the button (WCAG 2.4.3).
            Button(
                text = if (checked) "Try again" else "Run it to check",
                onClick = {
                    if (checked) { checked = false; chosen = null; retries += 1 }
                    else picked?.let { checked = true; onCheck(it.id) }
                },
                kind = if (checked) ButtonKind.Tonal else ButtonKind.Filled,
                branch = branch,
                enabled = checked || picked != null,
            )
        }
        if (checked && picked != null) {
            val right = picked.correct
            FeedbackBox(
                kind = if (right) FeedbackKind.Correct else FeedbackKind.Neutral,
                heading = if (right) "Yes, that is what it prints." else "Not this time. Here is what really happens.",
                modifier = Modifier.styleModifier { property("margin-top", space3Var.value()) },
            ) {
                P(Modifier.styleModifier { property("margin", "${space2Var.value()} 0 0") }.toAttrs()) { Text(picked.why) }
                P(Modifier.styleModifier { property("margin", "${space2Var.value()} 0 0") }.toAttrs()) { Text("The output is:") }
                Pre(Modifier.styleModifier { property("margin", "0"); typo(Typo.Code) }.toAttrs()) { Text(output) }
            }
        }
    }
}
