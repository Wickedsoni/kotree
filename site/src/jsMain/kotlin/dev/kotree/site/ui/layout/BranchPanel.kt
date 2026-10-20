package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.shapeXlVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeDisplayLineVar
import dev.kotree.site.typeDisplaySizeVar
import dev.kotree.site.typeDisplayWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/** One stem row of a branch: title and how many of its leaves are grown. */
data class StemSummary(val title: String, val grown: Int, val total: Int)

/**
 * Panel for a branch at Branch zoom: branch tint fill, a 4 px ink bar on top, shape.xl, display title, summary,
 * overall progress and the stems. The focus ring inside takes the branch ink. [onClose] is optional; when given, a
 * Close button is shown.
 */
@Composable
fun BranchPanel(
    slug: String,
    title: String,
    summary: String,
    stems: List<StemSummary>,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
) {
    val titleId = "branch-panel-$slug"
    Section(
        modifier
            .inBranch(slug)
            .attrsModifier { attr("aria-labelledby", titleId) }
            .styleModifier {
                property("background-color", branchTint(slug).value().toString())
                property("border-top", "${Tokens.Spacing.s1}px solid ${branchInk(slug).value().toString()}")
                property("border-radius", shapeXlVar.value())
                property("padding", space6Var.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeBodySizeVar.value())
                property("line-height", typeBodyLineVar.value())
            }
            .toAttrs()
    ) {
        H2(
            Modifier.attrsModifier { id(titleId) }.styleModifier {
                property("margin", "0 0 ${space3Var.value()}")
                property("color", branchInk(slug).value().toString())
                property("font-size", typeDisplaySizeVar.value())
                property("line-height", typeDisplayLineVar.value())
                property("font-weight", typeDisplayWeightVar.value())
            }.toAttrs()
        ) { Text(title) }
        P(Modifier.styleModifier { property("margin", "0 0 ${space4Var.value()}") }.toAttrs()) { Text(summary) }
        Div(Modifier.styleModifier { property("margin", "0 0 ${space4Var.value()}") }.toAttrs()) {
            ProgressCounter(stems.sumOf { it.grown }, stems.sumOf { it.total })
        }
        Ul(Modifier.styleModifier {
            property("margin", "0 0 ${space4Var.value()}")
            property("padding", "0")
            property("list-style", "none")
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
        }.toAttrs()) {
            for (stem in stems) Li { Text("${stem.title}: ${stem.grown} of ${stem.total}") }
        }
        if (onClose != null) Button("Close", onClose, kind = ButtonKind.Tonal, branch = slug)
    }
}
