package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space1Var
import dev.kotree.site.space3Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * "N of M leaves grown" (D11). A polite live region, so a screen reader announces the new count when a leaf is
 * marked. The count changes without animation, so reduced motion needs nothing extra.
 */
@Composable
fun ProgressCounter(grown: Int, total: Int, modifier: Modifier = Modifier) {
    Span(
        modifier
            .attrsModifier {
                attr("role", "status")
                attr("aria-live", "polite")
            }
            .styleModifier {
                property("display", "inline-block")
                property("padding", "${space1Var.value()} ${space3Var.value()}")
                property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                property("border-radius", shapeFullVar.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("white-space", "nowrap")
                property("font-variant-numeric", "tabular-nums")
            }
            .toAttrs()
    ) {
        Text("$grown of $total leaves grown")
    }
}
