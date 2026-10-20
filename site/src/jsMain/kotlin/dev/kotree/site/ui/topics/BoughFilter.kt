package dev.kotree.site.ui.topics

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
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space2Var
import dev.kotree.site.space4Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import kotlinx.browser.document
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Button as DomButton

private const val ALL_LABEL = "All"
private const val GROUP_LABEL = "Filter by bough"

private fun radioId(index: Int): String = "kt-bough-filter-$index"

/**
 * Bough filter (ADR 007): a single-select segmented group, `role=radiogroup`. Index 0 is "All", the next indexes follow
 * [boughTitles]. Roving tabindex: only the selected radio is a Tab stop, so Tab leaves the group; the arrow keys move
 * the selection (and focus) with wrap-around. Selecting does not change the URL.
 */
@Composable
fun BoughFilter(boughTitles: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    val labels = listOf(ALL_LABEL) + boughTitles
    var hovered by remember { mutableStateOf(-1) }
    val onSurface = colorOnSurfaceVar.value().toString()
    val surface = colorSurfaceVar.value().toString()
    Div(
        Modifier
            .attrsModifier {
                attr("role", "radiogroup")
                attr("aria-label", GROUP_LABEL)
            }
            .styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("gap", space2Var.value())
            }
            .toAttrs()
    ) {
        labels.forEachIndexed { index, label ->
            val checked = index == selected
            val content = if (checked) surface else onSurface
            val layerPercent = if (hovered == index) Tokens.StateLayer.hoverPercent else 0
            val layer = "color-mix(in srgb, $content $layerPercent%, transparent)"
            DomButton(
                Modifier.styleModifier {
                    property("box-sizing", "border-box")
                    property("min-height", Tokens.Spacing.minTargetPx.px)
                    property("padding", "0 ${space4Var.value()}")
                    property("border", "${Tokens.Elevation.borderPx}px solid $onSurface")
                    property("border-radius", shapeFullVar.value())
                    property("background-color", if (checked) onSurface else surface)
                    property("background-image", "linear-gradient($layer, $layer)")
                    property("color", content)
                    property("font-family", fontUiVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs {
                    attr("type", "button")
                    attr("id", radioId(index))
                    attr("role", "radio")
                    attr("aria-checked", checked.toString())
                    attr("tabindex", if (checked) "0" else "-1")
                    onClick { onSelect(index) }
                    onMouseEnter { hovered = index }
                    onMouseLeave { hovered = -1 }
                    onKeyDown { event ->
                        val step = when (event.key) {
                            "ArrowRight", "ArrowDown" -> 1
                            "ArrowLeft", "ArrowUp" -> -1
                            else -> 0
                        }
                        if (step != 0) {
                            event.preventDefault()
                            val next = (index + step + labels.size) % labels.size
                            onSelect(next)
                            document.getElementById(radioId(next))?.asDynamic()?.focus()
                        }
                    }
                }
            ) { Text(label) }
        }
    }
}
