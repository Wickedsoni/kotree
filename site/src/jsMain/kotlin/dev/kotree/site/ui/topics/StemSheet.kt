package dev.kotree.site.ui.topics

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Difficulty
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.ListRow
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.StatusChip
import dev.kotree.site.ui.core.StatusIcon
import dev.kotree.site.ui.layout.LinkButton
import dev.kotree.site.ui.layout.SideSheet
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

private const val SEPARATOR = " · "
private const val SHADOW_NOTE = "Topics with an empty outline are coming soon."

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

/**
 * The stem sheet (ADR 007, list-detail): eyebrow = branch, title = stem, the why text, a meter, then every leaf in order
 * as a row (status icon, title, difficulty). Live rows link to the lesson; the others are muted plain rows. A leaf that
 * is not live also lists what it builds on ([titles] maps a leaf id to its title). Footer: Close, "See on the tree".
 */
@Composable
fun StemSheet(branch: Branch, stem: Stem, titles: Map<String, String>, onClose: () -> Unit) {
    SideSheet(
        title = stem.title,
        onClose = onClose,
        eyebrow = branch.title,
        branch = branch.id,
        footer = {
            Button("Close", onClose, kind = ButtonKind.Text, branch = branch.id)
            LinkButton("See on the tree", "/#${stem.id}", kind = ButtonKind.Tonal, branch = branch.id)
        },
    ) {
        Div(Modifier.styleModifier { property("display", "grid"); property("gap", space4Var.value()) }.toAttrs()) {
            P(Modifier.styleModifier { property("margin", "0"); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                Text(stem.why)
            }
            Meter(stem.counts.live, stem.counts.total, "${stem.title} live topics", branch.id)
            if (stem.leaves.any { it.status == LeafStatus.SHADOW }) {
                P(
                    Modifier.styleModifier {
                        property("margin", "0")
                        property("color", colorOnSurfaceMutedVar.value())
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                    }.toAttrs()
                ) { Text(SHADOW_NOTE) }
            }
            Ul(
                Modifier.styleModifier {
                    property("margin", "0 calc(-1 * ${space3Var.value()})")
                    property("padding", "0")
                    property("list-style", "none")
                }.toAttrs()
            ) {
                stem.leaves.sortedBy { it.order }.forEach { leaf ->
                    Li { SheetLeafRow(branch.id, leaf, titles) }
                }
            }
        }
    }
}

@Composable
private fun SheetLeafRow(branch: String, leaf: Leaf, titles: Map<String, String>) {
    val status = chipStatus(leaf.status)
    val live = leaf.status == LeafStatus.LIVE
    val path = leaf.path.takeIf { live }
    val parts = buildList {
        add(difficultyText(leaf.difficulty))
        if (!live && leaf.requires.isNotEmpty()) add("Builds on: " + leaf.requires.joinToString(", ") { titles[it] ?: it })
    }
    if (leaf.status == LeafStatus.SHADOW) {
        ListRow(
            title = leaf.title,
            branch = branch,
            supporting = parts.joinToString(SEPARATOR),
            muted = true,
            clampLines = 2,
            leading = { StatusIcon(status) },
        )
    } else {
        ListRow(
            title = leaf.title,
            branch = branch,
            href = path,
            supporting = parts.joinToString(SEPARATOR),
            muted = !live,
            clampLines = 2,
            leading = { StatusIcon(status) },
            trailing = { StatusChip(status, compact = true) },
        )
    }
}
