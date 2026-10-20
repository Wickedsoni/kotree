package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.lessons.LeafInfo
import dev.kotree.site.space3Var
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.ui.core.ListRow
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.StatusIcon
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Ul
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

/** "n of m live" for the panel meta of the stem panel. */
internal fun stemMeta(info: LeafInfo): String =
    "${info.stem.leaves.count { it.status == LeafStatus.LIVE }} of ${info.stem.leaves.size} live"

/**
 * "In this stem" (ADR 007): the meter, then one [ListRow] per leaf in order with its status glyph. The current leaf is
 * tinted and marked `aria-current="page"`; live leaves link; the others are muted plain rows.
 */
@Composable
internal fun StemNav(info: LeafInfo) {
    val leaves = info.stem.leaves
    val branch = info.branch.id
    Nav(Modifier.attrsModifier { attr("aria-label", "In this stem") }.toAttrs()) {
        Meter(
            value = leaves.count { it.status == LeafStatus.LIVE },
            max = leaves.size,
            label = "Lessons live in ${info.stem.title}",
            branch = branch,
            showText = false,
            modifier = Modifier.styleModifier { property("margin-bottom", space3Var.value()) },
        )
        Ul(
            Modifier.styleModifier {
                property("margin", "0")
                property("padding", "0")
                property("list-style", "none")
            }.toAttrs()
        ) {
            leaves.forEach { leaf ->
                val isCurrent = leaf.id == info.leaf.id
                val live = leaf.status == LeafStatus.LIVE
                Li {
                    ListRow(
                        title = leaf.title,
                        branch = branch,
                        href = if (live) "/${leaf.id}" else null,
                        current = isCurrent,
                        muted = !live,
                        clampLines = 2,
                        leading = { StatusIcon(leaf.status.chip()) },
                    )
                }
            }
        }
    }
}

private fun LeafStatus.chip(): ChipStatus = when (this) {
    LeafStatus.SHADOW -> ChipStatus.Shadow
    LeafStatus.UPNEXT -> ChipStatus.UpNext
    LeafStatus.LIVE -> ChipStatus.Live
}
