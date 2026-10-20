package dev.kotree.site.ui.topics

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.branchInk
import dev.kotree.site.layoutBentoColMinVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutPageMaxVar
import dev.kotree.site.space12Var
import dev.kotree.site.space2Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

/** CSS classes whose column spans are registered in ui/Base.kt (a media query needs the stylesheet). */
const val BENTO_WIDE_CLASS = "kt-bento-wide"
const val BENTO_HERO_CLASS = "kt-bento-hero"

/**
 * Bento grid (ADR 006): auto-fill columns of at least layout.bento.col.min, so 1, 2, 3 and 4 columns follow the
 * viewport. Dense flow lets 1-column cards fill the gaps beside the wide and hero cards.
 */
@Composable
fun BentoGrid(content: @Composable () -> Unit) {
    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("grid-template-columns", "repeat(auto-fill, minmax(${layoutBentoColMinVar.value()}, 1fr))")
            property("grid-auto-flow", "row dense")
            property("gap", space6Var.value())
            property("box-sizing", "border-box")
            property("width", "100%")
            property("max-width", layoutPageMaxVar.value())
            property("margin", "0 auto")
            property("padding", "${space8Var.value()} ${layoutPageGutterVar.value()} ${space12Var.value()}")
        }.toAttrs()
    ) { content() }
}

/** Full-row header of one bough, in the bough ink. [inkSlug] is a branch slug whose ink colours the bough. */
@Composable
fun BoughHeader(title: String, inkSlug: String) {
    H2(
        Modifier.styleModifier {
            property("grid-column", "1 / -1")
            property("margin", "${space6Var.value()} 0 ${space2Var.value()}")
            property("color", branchInk(inkSlug).value().toString())
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
            property("text-transform", "uppercase")
        }.toAttrs()
    ) { Text(title) }
}
