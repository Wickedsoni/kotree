package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Offline copy from docs/06 section 6. */
const val OFFLINE_MESSAGE = "Running code needs internet. You can still read the code and the answers."

/**
 * EditorShell: the frame around a runnable code area (docs/06 section 2). It holds a [content] slot (Phase 6 puts the
 * Kotlin Playground there; until then a CodeBlock), a Run button and an output area. There is no Playground here.
 * When [online] is false the Run button is replaced by [OFFLINE_MESSAGE]. The output area is a polite live region.
 *
 * @param output text shown under the code after a run, empty for none.
 */
@Composable
fun EditorShell(
    title: String,
    modifier: Modifier = Modifier,
    branch: String = "kotlin",
    online: Boolean = true,
    runLabel: String = "Run",
    output: String = "",
    onRun: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Div(
        modifier
            .attrsModifier { attr("role", "group"); attr("aria-label", title) }
            .styleModifier {
                frame()
                property("padding", space4Var.value())
                property("color", colorOnSurfaceVar.value())
            }.toAttrs()
    ) {
        Div { content() }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("align-items", "center")
                property("gap", space3Var.value())
                property("margin-top", space3Var.value())
            }.toAttrs()
        ) {
            if (online) {
                Button(text = runLabel, onClick = onRun, branch = branch)
            } else {
                Span(
                    Modifier.attrsModifier { attr("role", "status") }
                        .styleModifier { property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()
                ) { Text(OFFLINE_MESSAGE) }
            }
        }
        Div(
            Modifier.attrsModifier { attr("role", "status"); attr("aria-live", "polite") }
                .styleModifier { if (output.isEmpty()) property("display", "none") }.toAttrs()
        ) {
            Div(Modifier.styleModifier { property("margin-top", space3Var.value()); property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()) {
                Text("Output")
            }
            Pre(
                Modifier.styleModifier {
                    property("margin", "${space2Var.value()} 0 0")
                    property("padding", space3Var.value())
                    property("overflow", "auto")
                    property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                    typo(Typo.Code)
                }.toAttrs()
            ) { Text(output) }
        }
    }
}
