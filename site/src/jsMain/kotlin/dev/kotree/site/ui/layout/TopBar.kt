package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Top bar: name, tagline (D14) and the leaves counter. Plain text and a counter: it has no interactive part. */
@Composable
fun TopBar(
    grown: Int,
    total: Int,
    modifier: Modifier = Modifier,
    title: String = "Kotree",
    tagline: String = "Grow your Android knowledge, one leaf at a time.",
) {
    Header(
        modifier
            .styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("gap", space4Var.value())
                property("padding", "${space3Var.value()} ${space6Var.value()}")
                property("background-color", colorSurfaceVar.value())
                property("border-bottom", elevation1Var.value())
                property("font-family", fontUiVar.value())
            }
            .toAttrs()
    ) {
        Span(Modifier.styleModifier {
            property("color", branchInk("foundation").value().toString())
            property("font-size", typeTitleSizeVar.value())
            property("line-height", typeTitleLineVar.value())
            property("font-weight", typeTitleWeightVar.value())
        }.toAttrs()) { Text(title) }
        Span(Modifier.styleModifier {
            property("flex", "1")
            property("color", colorOnSurfaceMutedVar.value())
            property("font-size", typeBodySizeVar.value())
            property("line-height", typeBodyLineVar.value())
        }.toAttrs()) { Text(tagline) }
        Div { ProgressCounter(grown, total) }
    }
}
