package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeXsVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private fun rgbCss(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

/** Badge for a shadow branch or stem (L4). Text in the gray-label colour, so it is readable on white. */
@Composable
fun ComingSoonBadge(modifier: Modifier = Modifier, text: String = "Coming soon") {
    Span(
        modifier
            .styleModifier {
                property("display", "inline-block")
                property("padding", "${space1Var.value()} ${space2Var.value()}")
                property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                property("border-radius", shapeXsVar.value())
                property("color", rgbCss(Tokens.Color.leafShadowLabel))
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("white-space", "nowrap")
            }
            .toAttrs()
    ) {
        Text(text)
    }
}
