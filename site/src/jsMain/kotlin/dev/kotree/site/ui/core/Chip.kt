package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeXsVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Difficulty of a topic (docs/00 section 7). Always written as text, the swatch only repeats it. */
enum class Difficulty(val label: String, val swatch: Int) {
    Easy("Easy", Tokens.Leaf.easy),
    Medium("Medium", Tokens.Leaf.medium),
    Hard("Hard", Tokens.Leaf.hard),
}

/** Leaf status (docs/00 section 7). */
enum class LeafStatus(val label: String, val swatch: Int) {
    Shadow("Planned", Tokens.Leaf.shadow),
    UpNext("Up next", Tokens.Leaf.upnext),
    Live("Live", Tokens.Leaf.medium),
}

private fun swatchCss(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

/**
 * Chip: [text] with a swatch square beside it (docs/02 section 6). The text carries the meaning, so the chip is
 * readable without colour. [swatchRgb] is a world colour as Int (docs/13 section 2: DOM chips read the Int).
 * Not interactive, so it takes no focus.
 */
@Composable
fun Chip(text: String, swatchRgb: Int, modifier: Modifier = Modifier) {
    Div(
        modifier
            .styleModifier {
                property("display", "inline-flex")
                property("align-items", "center")
                property("gap", space2Var.value())
                property("padding", "${space1Var.value()} ${space2Var.value()}")
                property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                property("border-radius", shapeXsVar.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("white-space", "nowrap")
            }
            .toAttrs()
    ) {
        Span(
            Modifier
                .attrsModifier { attr("aria-hidden", "true") }
                .styleModifier {
                    property("display", "inline-block")
                    property("width", Tokens.Chrome.chipSwatchPx.px)
                    property("height", Tokens.Chrome.chipSwatchPx.px)
                    property("border-radius", "${Tokens.Elevation.borderPx}px")
                    property("background-color", swatchCss(swatchRgb))
                    property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                }
                .toAttrs()
        )
        Text(text)
    }
}

@Composable
fun DifficultyChip(difficulty: Difficulty, modifier: Modifier = Modifier) = Chip(difficulty.label, difficulty.swatch, modifier)

@Composable
fun StatusChip(status: LeafStatus, modifier: Modifier = Modifier) = Chip(status.label, status.swatch, modifier)
