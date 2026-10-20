package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.shapeLgIncreasedVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.spaceInsetPanelVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleSmallLineVar
import dev.kotree.site.typeTitleSmallSizeVar
import dev.kotree.site.typeTitleSmallWeightVar
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * One block of the lesson rails (ADR 007): outlined surface, `shape.lg.increased`, padding `space.inset.panel`, a
 * `type.title.small` heading and an optional [meta] text at the end. Every rail block uses it, so all look alike.
 */
@Composable
internal fun RailPanel(title: String, meta: String? = null, content: @Composable () -> Unit) {
    Section(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("padding", spaceInsetPanelVar.value())
            property("background-color", colorSurfaceVar.value())
            property("border", elevation1Var.value())
            property("border-radius", shapeLgIncreasedVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("align-items", "baseline")
                property("justify-content", "space-between")
                property("gap", space2Var.value())
                property("margin-bottom", space3Var.value())
            }.toAttrs()
        ) {
            H2(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("font-size", typeTitleSmallSizeVar.value())
                    property("line-height", typeTitleSmallLineVar.value())
                    property("font-weight", typeTitleSmallWeightVar.value())
                }.toAttrs()
            ) { Text(title) }
            if (meta != null) {
                Span(
                    Modifier.styleModifier {
                        property("color", colorOnSurfaceMutedVar.value())
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                        property("font-variant-numeric", "tabular-nums")
                        property("white-space", "nowrap")
                    }.toAttrs()
                ) { Text(meta) }
            }
        }
        content()
    }
}
