package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * Controls of a sim whose state lives outside the frame (ADR 009 section 4): the template host owns Play, history and
 * speed, and the frame only draws the buttons. A null callback hides its button, so Back and Step are opt-in.
 */
class SimControls(
    val playing: Boolean,
    val slow: Boolean,
    val describing: Boolean,
    val canBack: Boolean,
    val canStep: Boolean,
    val onPlay: () -> Unit,
    val onPause: () -> Unit,
    val onBack: () -> Unit,
    val onStep: () -> Unit,
    val onReset: () -> Unit,
    val onSpeed: () -> Unit,
    val onDescribe: () -> Unit,
)

/**
 * SimFrame: the wrapper for a simulation (docs/05, docs/02 section 6): `layout.lesson.wide` wide (720 px), 16:9 stage
 * (405 px), `shape.xl`, `elevation.1`, and Play or Pause plus Reset buttons that only call the callbacks. There is no
 * engine here. The stage is a [content] slot; its default is a plain label. Controls are real buttons, so they take
 * Tab and show the focus ring.
 *
 * With [controls] the frame also draws Back, Step, Speed and Text description (ADR 009 section 4) and the host owns the
 * state; [actions] is the row of template actions, toggles and sliders, and [below] holds the Try this list, readouts and
 * the text description. Without [controls] it behaves as before.
 *
 * @param title accessible name of the whole simulation, for example "Sample simulation".
 */
@Composable
fun SimFrame(
    title: String,
    modifier: Modifier = Modifier,
    branch: String = "kotlin",
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onReset: () -> Unit = {},
    controls: SimControls? = null,
    actions: (@Composable () -> Unit)? = null,
    below: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit = { Text("Simulation area") },
) {
    var playing by remember { mutableStateOf(false) }
    val isPlaying = controls?.playing ?: playing
    Div(
        modifier
            .attrsModifier { attr("role", "group"); attr("aria-label", title) }
            .styleModifier {
                property("box-sizing", "border-box")
                // The 1 px border sits outside the 720 px stage: width = stage + both borders, so the stage is 720 x 405.
                property("width", "calc(${layoutLessonWideVar.value()} + ${2 * Tokens.Elevation.borderPx}px)")
                property("max-width", "100%")
                property("border", elevation1Var.value())
                property("border-radius", shapeXlVar.value())
                property("overflow", "hidden")
                property("color", colorOnSurfaceVar.value())
            }.toAttrs()
    ) {
        Div(
            Modifier.styleModifier {
                property("aspect-ratio", "${Tokens.LayoutW.simAspectW} / ${Tokens.LayoutW.simAspectH}")
                property("display", "flex")
                property("align-items", "center")
                property("justify-content", "center")
                property("background-color", branchTint(branch).value().toString())
                property("position", "relative")
                property("overflow", "hidden")
                typo(Typo.Body)
            }.toAttrs()
        ) { content() }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("gap", space2Var.value())
                property("padding", space3Var.value())
                property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            }.toAttrs()
        ) {
            // One call site for both states keeps the same DOM node, so keyboard focus stays on the button (WCAG 2.4.3).
            Button(
                text = if (isPlaying) "Pause" else "Play",
                onClick = {
                    if (controls != null) { if (controls.playing) controls.onPause() else controls.onPlay() }
                    else if (playing) { playing = false; onPause() } else { playing = true; onPlay() }
                },
                branch = branch,
            )
            if (controls != null) {
                Button("Back", controls.onBack, kind = ButtonKind.Tonal, branch = branch, enabled = controls.canBack)
                Button("Step", controls.onStep, kind = ButtonKind.Tonal, branch = branch, enabled = controls.canStep)
                Button("Reset", controls.onReset, kind = ButtonKind.Tonal, branch = branch)
                Button(if (controls.slow) "Speed: Slow" else "Speed: Normal", controls.onSpeed, kind = ButtonKind.Text, branch = branch)
                Button(
                    if (controls.describing) "Hide text description" else "Show text description",
                    controls.onDescribe, kind = ButtonKind.Text, branch = branch,
                )
            } else {
                Button("Reset", { playing = false; onReset() }, kind = ButtonKind.Tonal, branch = branch)
            }
        }
        actions?.let {
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("flex-wrap", "wrap")
                    property("align-items", "center")
                    property("gap", space2Var.value())
                    property("padding", space3Var.value())
                    property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                }.toAttrs()
            ) { it() }
        }
        below?.let {
            Div(
                Modifier.styleModifier {
                    property("padding", space3Var.value())
                    property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                    typo(Typo.Body)
                }.toAttrs()
            ) { it() }
        }
    }
}
