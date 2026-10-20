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
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorStatusLiveVar
import dev.kotree.site.colorStatusShadowContainerVar
import dev.kotree.site.colorStatusUpnextVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Difficulty
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.core.StatusChip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Button as DomButton
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

private fun difficultyText(d: Difficulty): String = when (d) {
    Difficulty.EASY -> "Easy"
    Difficulty.MEDIUM -> "Medium"
    Difficulty.HARD -> "Hard"
}

private fun chipStatus(s: LeafStatus): ChipStatus = when (s) {
    LeafStatus.LIVE -> ChipStatus.Live
    LeafStatus.UPNEXT -> ChipStatus.UpNext
    LeafStatus.SHADOW -> ChipStatus.Shadow
}

private fun dotColor(s: LeafStatus): String = when (s) {
    LeafStatus.LIVE -> colorStatusLiveVar.value().toString()
    LeafStatus.UPNEXT -> colorStatusUpnextVar.value().toString()
    LeafStatus.SHADOW -> colorStatusShadowContainerVar.value().toString()
}

/** One decorative strip for all leaves of a stem: a single node with hard colour stops (keeps the DOM small). */
private fun stripGradient(leaves: List<Leaf>): String {
    val n = leaves.size
    if (n == 0) return "none"
    val stops = leaves.mapIndexed { i, leaf ->
        "${dotColor(leaf.status)} ${i * 100.0 / n}% ${(i + 1) * 100.0 / n}%"
    }
    return "linear-gradient(to right, ${stops.joinToString(", ")})"
}

/**
 * One stem of a branch card (ADR 006): a disclosure button (`aria-expanded`) with title, "x of y" and a strip of
 * leaf swatches. The leaf list exists in the DOM only while open. The row id is the stem id, the anchor
 * `/topics#<branch>/<stem-slug>`. [forceOpen] opens it (up-next stem or URL hash).
 */
@Composable
fun StemRow(branch: Branch, stem: Stem, titles: Map<String, String>, forceOpen: Boolean) {
    var open by remember(forceOpen) { mutableStateOf(forceOpen) }
    val panelId = "${stem.id}/leaves"
    val leaves = stem.leaves.sortedBy { it.order }
    Li(
        Modifier.styleModifier {
            property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("scroll-margin-top", space3Var.value())
        }.toAttrs { attr("id", stem.id) }
    ) {
        DomButton(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("align-items", "center")
                property("gap", "${space1Var.value()} ${space3Var.value()}")
                property("box-sizing", "border-box")
                property("width", "100%")
                property("min-height", Tokens.Spacing.minTargetPx.px)
                property("padding", "${space2Var.value()} 0")
                property("border", "0")
                property("background-color", "transparent")
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("text-align", "start")
                property("cursor", "pointer")
            }.toAttrs {
                attr("type", "button")
                attr("aria-expanded", open.toString())
                attr("aria-controls", panelId)
                onClick { open = !open }
            }
        ) {
            Span(Modifier.styleModifier {
                property("flex", "1 1 auto")
                property("min-width", "0")
            }.toAttrs()) {
                Span(Modifier.attrsModifier { attr("aria-hidden", "true") }.toAttrs()) { Text(if (open) "▾ " else "▸ ") }
                Text(stem.title)
            }
            Span(Modifier.styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("white-space", "nowrap")
            }.toAttrs()) { Text("${stem.counts.live} of ${stem.counts.total}") }
            Span(
                Modifier.attrsModifier { attr("aria-hidden", "true") }.styleModifier {
                    property("flex", "1 0 100%")
                    property("height", space2Var.value())
                    property("border-radius", shapeFullVar.value())
                    property("background-image", stripGradient(leaves))
                }.toAttrs()
            )
        }
        if (open) {
            Ul(
                Modifier.styleModifier {
                    property("margin", "0 0 ${space2Var.value()}")
                    property("padding", "0")
                    property("list-style", "none")
                }.toAttrs { attr("id", panelId) }
            ) {
                leaves.forEach { LeafItem(branch, it, titles) }
            }
        }
    }
}

@Composable
private fun LeafItem(branch: Branch, leaf: Leaf, titles: Map<String, String>) {
    val path = leaf.path.takeIf { leaf.status == LeafStatus.LIVE }
    Li(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("align-items", "center")
            property("gap", "${space1Var.value()} ${space3Var.value()}")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
        }.toAttrs()
    ) {
        if (path != null) {
            A(
                path,
                Modifier.styleModifier {
                    property("display", "inline-flex")
                    property("align-items", "center")
                    property("min-height", Tokens.Spacing.minTargetPx.px)
                    property("color", branchInk(branch.id).value().toString())
                    property("text-decoration", "underline")
                }.toAttrs()
            ) { Text(leaf.title) }
        } else {
            Span(Modifier.styleModifier {
                property("color", if (leaf.status == LeafStatus.SHADOW) colorOnSurfaceMutedVar.value().toString() else colorOnSurfaceVar.value().toString())
            }.toAttrs()) { Text(leaf.title) }
        }
        StatusChip(chipStatus(leaf.status))
        Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
            Text(difficultyText(leaf.difficulty))
        }
        if (path == null && leaf.requires.isNotEmpty()) {
            Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                Text("Needs: " + leaf.requires.joinToString(", ") { titles[it] ?: it })
            }
        }
    }
}
