package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space1Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Progress meter (ADR 007): "N of M live" (type.label, tabular) over an 8 px pill bar, `role=progressbar` with values.
 * [label] names the bar for assistive tech. [onTint] draws the track on white instead of the branch tint, for a meter
 * that sits on a tinted card. [showText] hides the visible count only; the bar keeps its accessible values.
 */
@Composable
fun Meter(
    value: Int,
    max: Int,
    label: String,
    branch: String,
    showText: Boolean = true,
    onTint: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val total = if (max < 1) 1 else max
    val shown = value.coerceIn(0, total)
    val percent = shown * 100 / total
    val track = if (onTint) colorSurfaceVar.value().toString() else branchTint(branch).value().toString()
    Div(
        modifier.styleModifier {
            property("display", "grid")
            property("gap", space1Var.value())
        }.toAttrs()
    ) {
        if (showText) {
            Span(
                Modifier.styleModifier {
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                    property("font-variant-numeric", "tabular-nums")
                }.toAttrs()
            ) { Text("$shown of $max live") }
        }
        Div(
            Modifier
                .attrsModifier {
                    attr("role", "progressbar")
                    attr("aria-label", label)
                    attr("aria-valuemin", "0")
                    attr("aria-valuemax", total.toString())
                    attr("aria-valuenow", shown.toString())
                }
                .styleModifier {
                    property("box-sizing", "border-box")
                    property("height", Tokens.Size.meterPx.px)
                    property("border-radius", shapeFullVar.value())
                    property("background-color", track)
                    property("overflow", "hidden")
                }
                .toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("height", "100%")
                    property("width", "$percent%")
                    property("border-radius", shapeFullVar.value())
                    property("background-color", branchInk(branch).value().toString())
                }.toAttrs()
            )
        }
    }
}
