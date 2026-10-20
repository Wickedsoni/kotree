package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.space2Var
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.dom.Div

private const val BRANCH = "foundation"

/**
 * Zoom controls: zoom in, zoom out, reset, Start here. Native buttons, so Tab, Enter and Space work; the icon-like
 * "+" and "-" carry an aria-label. Buttons come from core (44 px, focus ring from the global rule).
 */
@Composable
fun ZoomControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit,
    onStartHere: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Div(
        modifier
            .attrsModifier {
                attr("role", "group")
                attr("aria-label", "Zoom controls")
            }
            .styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("gap", space2Var.value())
            }
            .toAttrs()
    ) {
        Button("+", onZoomIn, Modifier.attrsModifier { attr("aria-label", "Zoom in") }, ButtonKind.Tonal, BRANCH)
        Button("-", onZoomOut, Modifier.attrsModifier { attr("aria-label", "Zoom out") }, ButtonKind.Tonal, BRANCH)
        Button("Reset", onReset, Modifier.attrsModifier { attr("aria-label", "Reset view") }, ButtonKind.Tonal, BRANCH)
        Button("Start here", onStartHere, kind = ButtonKind.Filled, branch = BRANCH, blob = true)
    }
}
