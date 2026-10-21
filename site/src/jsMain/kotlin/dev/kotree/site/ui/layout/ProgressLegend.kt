package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.tree.render.cssColor
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Legend and counter of the visitor's progress, floating on the tree (ADR 008 §Progress): a "Not done" swatch, a yellow
 * to orange "Done" swatch and "N of M done" as a polite live region. The swatches are decoration (`aria-hidden`); the
 * words carry the meaning. Not shown when there is no live leaf to mark.
 */
@Composable
fun ProgressLegend(done: Int, live: Int, modifier: Modifier = Modifier) {
    if (live <= 0) return
    Span(
        modifier
            .styleModifier {
                property("display", "inline-flex")
                property("align-items", "center")
                property("flex-wrap", "wrap")
                property("gap", space3Var.value())
                property("padding", "${space2Var.value()} ${space4Var.value()}")
                property("background-color", colorSurfaceVar.value())
                property("border-radius", shapeFullVar.value())
                property("box-shadow", elevation2Var.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("white-space", "nowrap")
            }
            .toAttrs()
    ) {
        Swatch("Not done", cssColor(Tokens.Leaf.medium))
        Swatch("Done", "linear-gradient(90deg, ${cssColor(Tokens.Autumn.yellow)}, ${cssColor(Tokens.Autumn.orange)})")
        Span(
            Modifier
                .attrsModifier {
                    attr("role", "status")
                    attr("aria-live", "polite")
                }
                .styleModifier { property("font-variant-numeric", "tabular-nums") }
                .toAttrs()
        ) { Text("$done of $live done") }
    }
}

@Composable
private fun Swatch(label: String, background: String) {
    Span(
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("gap", space2Var.value())
        }.toAttrs()
    ) {
        Span(
            Modifier
                .attrsModifier { attr("aria-hidden", "true") }
                .styleModifier {
                    property("display", "inline-block")
                    property("width", Tokens.Chrome.chipSwatchPx.px)
                    property("height", Tokens.Chrome.chipSwatchPx.px)
                    property("border-radius", shapeFullVar.value())
                    property("background", background)
                    property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                }
                .toAttrs()
        )
        Text(label)
    }
}
