package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.space1Var
import dev.kotree.site.space4Var
import dev.kotree.site.typeHeadlineLineVar
import dev.kotree.site.typeHeadlineSizeVar
import dev.kotree.site.typeHeadlineWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Section header (ADR 007): an optional eyebrow (type.label, muted) over the title; [meta] and [action] sit at the end,
 * baseline-aligned. [level] picks h1 (type.headline) or h2 / h3 (type.title). [id] becomes the heading's anchor id.
 */
@Composable
fun SectionHeader(
    title: String,
    level: Int = 2,
    eyebrow: String? = null,
    meta: String? = null,
    id: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val headingStyle = Modifier.styleModifier {
        property("margin", "0")
        if (level <= 1) {
            property("font-size", typeHeadlineSizeVar.value())
            property("line-height", typeHeadlineLineVar.value())
            property("font-weight", typeHeadlineWeightVar.value())
        } else {
            property("font-size", typeTitleSizeVar.value())
            property("line-height", typeTitleLineVar.value())
            property("font-weight", typeTitleWeightVar.value())
        }
    }
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("align-items", "baseline")
            property("justify-content", "space-between")
            property("gap", space4Var.value())
        }.toAttrs()
    ) {
        Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()) }.toAttrs()) {
            if (eyebrow != null) {
                Span(
                    Modifier.styleModifier {
                        property("color", colorOnSurfaceMutedVar.value())
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                    }.toAttrs()
                ) { Text(eyebrow) }
            }
            when {
                level <= 1 -> H1(headingStyle.toAttrs { if (id != null) id(id) }) { Text(title) }
                level == 2 -> H2(headingStyle.toAttrs { if (id != null) id(id) }) { Text(title) }
                else -> H3(headingStyle.toAttrs { if (id != null) id(id) }) { Text(title) }
            }
        }
        if (meta != null || action != null) {
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("align-items", "baseline")
                    property("gap", space4Var.value())
                }.toAttrs()
            ) {
                if (meta != null) {
                    Span(
                        Modifier.styleModifier {
                            property("color", colorOnSurfaceMutedVar.value())
                            property("font-size", typeLabelSizeVar.value())
                            property("line-height", typeLabelLineVar.value())
                            property("font-weight", typeLabelWeightVar.value())
                            property("font-variant-numeric", "tabular-nums")
                        }.toAttrs()
                    ) { Text(meta) }
                }
                if (action != null) action()
            }
        }
    }
}
