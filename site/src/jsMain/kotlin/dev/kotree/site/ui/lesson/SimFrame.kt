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
 * SimFrame: the wrapper for a simulation (docs/05, docs/02 section 6): `layout.lesson.wide` wide (720 px), 16:9 stage
 * (405 px), `shape.xl`, `elevation.1`, and Play or Pause plus Reset buttons that only call the callbacks. There is no
 * engine here. The stage is a [content] slot; its default is a plain label. Controls are real buttons, so they take
 * Tab and show the focus ring.
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
    content: @Composable () -> Unit = { Text("Simulation area") },
) {
    var playing by remember { mutableStateOf(false) }
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
                typo(Typo.Body)
            }.toAttrs()
        ) { content() }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("gap", space2Var.value())
                property("padding", space3Var.value())
                property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            }.toAttrs()
        ) {
            // One call site for both states keeps the same DOM node, so keyboard focus stays on the button (WCAG 2.4.3).
            Button(
                text = if (playing) "Pause" else "Play",
                onClick = { if (playing) { playing = false; onPause() } else { playing = true; onPlay() } },
                branch = branch,
            )
            Button("Reset", { playing = false; onReset() }, kind = ButtonKind.Tonal, branch = branch)
        }
    }
}
