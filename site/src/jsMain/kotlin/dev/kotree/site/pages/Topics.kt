package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.space12Var
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeHeadlineLineVar
import dev.kotree.site.typeHeadlineSizeVar
import dev.kotree.site.typeHeadlineWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Chip
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

private const val PAGE_TITLE = "Topics - Kotree"
private const val TREE_HREF = "/"

/** All topics per branch with their status, so the author can see what is next (plain list, no tree). */
@Page("/topics")
@Composable
fun TopicsPage() {
    document.title = PAGE_TITLE
    val titles = treeData.boughs.flatMap { it.branches }.flatMap { it.stems }.flatMap { it.leaves }
        .associate { it.id to it.title }
    Main(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("max-width", layoutLessonWideVar.value())
            property("margin", "0 auto")
            property("padding", "${space8Var.value()} ${layoutPageGutterVar.value()} ${space12Var.value()}")
            property("font-family", fontUiVar.value())
            property("font-size", typeBodySizeVar.value())
            property("line-height", typeBodyLineVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        Nav(Modifier.attrsModifier { attr("aria-label", "Page") }.toAttrs()) {
            A(TREE_HREF, linkStyle(color = branchInk("foundation").value().toString()).toAttrs()) { Text("Back to the tree") }
        }
        H1(
            Modifier.styleModifier {
                property("margin", "${space4Var.value()} 0 ${space2Var.value()}")
                property("font-size", typeHeadlineSizeVar.value())
                property("line-height", typeHeadlineLineVar.value())
                property("font-weight", typeHeadlineWeightVar.value())
            }.toAttrs()
        ) { Text("Topics") }
        P(Modifier.styleModifier {
            property("margin", "0 0 ${space8Var.value()}")
            property("color", colorOnSurfaceMutedVar.value())
        }.toAttrs()) {
            Text("Every branch of the tree, topic by topic. Live topics are open, the up-next topic is what is being learned now.")
        }
        treeData.boughs.sortedBy { it.order }.forEach { bough ->
            Section(Modifier.attrsModifier { attr("aria-label", bough.title) }.toAttrs()) {
                Div(Modifier.styleModifier {
                    property("margin", "${space8Var.value()} 0 ${space4Var.value()}")
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                    property("text-transform", "uppercase")
                }.toAttrs()) { Text(bough.title) }
                bough.branches.sortedBy { it.order }.forEach { BranchSection(it, titles) }
            }
        }
    }
}

@Composable
private fun BranchSection(branch: Branch, titles: Map<String, String>) {
    Section(
        Modifier.inBranch(branch.id).styleModifier {
            property("margin", "0 0 ${space6Var.value()}")
            property("padding", space6Var.value())
            property("border", elevation1Var.value())
            property("border-top", "${Tokens.Chrome.focusRingWidthPx}px solid ${branchInk(branch.id).value()}")
            property("border-radius", shapeXlVar.value())
        }.toAttrs { attr("id", branch.id) }
    ) {
        H2(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("align-items", "baseline")
                property("gap", space3Var.value())
                property("margin", "0 0 ${space4Var.value()}")
                property("color", branchInk(branch.id).value().toString())
                property("font-size", typeTitleSizeVar.value())
                property("line-height", typeTitleLineVar.value())
                property("font-weight", typeTitleWeightVar.value())
            }.toAttrs()
        ) {
            Text(branch.title)
            Span(Modifier.styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("font-weight", typeLabelWeightVar.value())
            }.toAttrs()) { Text("${branch.counts.live} of ${branch.counts.total} live") }
            A("$TREE_HREF#${branch.id}", linkStyle(color = branchInk(branch.id).value().toString()).styleModifier {
                property("font-size", typeLabelSizeVar.value())
            }.toAttrs { attr("aria-label", "Show ${branch.title} on the tree") }) { Text("See on the tree") }
        }
        branch.stems.sortedBy { it.order }.forEach { StemBlock(branch, it, titles) }
    }
}

@Composable
private fun StemBlock(branch: Branch, stem: Stem, titles: Map<String, String>) {
    H3(
        Modifier.styleModifier {
            property("margin", "${space6Var.value()} 0 ${space1Var.value()}")
            property("font-size", typeBodySizeVar.value())
            property("line-height", typeBodyLineVar.value())
            property("font-weight", typeTitleWeightVar.value())
        }.toAttrs()
    ) { Text("${stem.title} (${stem.counts.live} of ${stem.counts.total})") }
    if (stem.why.isNotBlank()) {
        P(Modifier.styleModifier {
            property("margin", "0 0 ${space2Var.value()}")
            property("color", colorOnSurfaceMutedVar.value())
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
        }.toAttrs()) { Text(stem.why) }
    }
    Ul(Modifier.styleModifier {
        property("margin", "0")
        property("padding", "0")
        property("list-style", "none")
    }.toAttrs()) {
        stem.leaves.sortedBy { it.order }.forEach { LeafRow(branch, it, titles) }
    }
}

@Composable
private fun LeafRow(branch: Branch, leaf: Leaf, titles: Map<String, String>) {
    val upNext = leaf.status == LeafStatus.UPNEXT
    val path = leaf.path.takeIf { leaf.status == LeafStatus.LIVE }
    val live = path != null
    Li(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("align-items", "center")
            property("gap", "${space1Var.value()} ${space3Var.value()}")
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("padding", "${space1Var.value()} ${space3Var.value()}")
            property("border-bottom", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            if (upNext) {
                property("background-color", branchTint(branch.id).value().toString())
                property("border-radius", "${Tokens.Chrome.focusRingWidthPx}px")
            }
        }.toAttrs()
    ) {
        val textColor = if (leaf.status == LeafStatus.SHADOW) colorOnSurfaceMutedVar.value() else colorOnSurfaceVar.value()
        if (path != null) {
            A(path, linkStyle(color = branchInk(branch.id).value().toString()).toAttrs()) { Text(leaf.title) }
        } else {
            Span(Modifier.styleModifier {
                property("color", textColor.toString())
                if (upNext) property("font-weight", typeTitleWeightVar.value())
            }.toAttrs()) { Text(leaf.title) }
        }
        when (leaf.status) {
            LeafStatus.LIVE -> Chip("Live", Tokens.Leaf.medium)
            LeafStatus.UPNEXT -> Chip("Up next", Tokens.Leaf.upnext)
            LeafStatus.SHADOW -> Chip("Coming soon", Tokens.Leaf.shadow)
        }
        if (!live && leaf.requires.isNotEmpty()) {
            Span(Modifier.styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
            }.toAttrs()) {
                Text("Needs: " + leaf.requires.joinToString(", ") { titles[it] ?: it })
            }
        }
    }
}

private fun linkStyle(color: String): Modifier = Modifier.styleModifier {
    property("display", "inline-flex")
    property("align-items", "center")
    property("min-height", "${Tokens.Spacing.minTargetPx}px")
    property("color", color)
    property("text-decoration", "underline")
}
