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
import dev.kotree.site.colorStatusUpnextContainerVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.CardVariant
import dev.kotree.site.ui.core.LeafStatus
import dev.kotree.site.ui.core.ListRow
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.StatusIcon
import dev.kotree.site.ui.core.SurfaceCard
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/** Stems shown before the "Show all" disclosure (ADR 007). */
const val STEMS_COLLAPSED = 6

private const val TREE_HREF = "/"
private const val TITLE_LINES = 2

/**
 * One branch card of the grid (ADR 007), in a fixed order: eyebrow (bough) · title · meter · up-next row · divider ·
 * the first [STEMS_COLLAPSED] stems with a disclosure row · footer link. The id is the branch slug (anchor
 * `/topics#<branch>`). The card is a static surface (no hover effect); only its rows are interactive, and each stem
 * row opens the stem sheet through [onOpenStem]. The footer sits at the bottom edge so footers line up across a row.
 *
 * @param visible false while the bough filter hides the card (`display: none`, so the grid closes up).
 * @param forceAll opens the disclosure (a hash points at a stem past the first six).
 */
@Composable
fun BranchCard(
    branch: Branch,
    boughTitle: String,
    visible: Boolean,
    forceAll: Boolean,
    onOpenStem: (Stem) -> Unit,
) {
    var showAll by remember(forceAll) { mutableStateOf(forceAll) }
    val ink = branchInk(branch.id).value().toString()
    val titleId = "${branch.id}-title"
    val stems = branch.stems.sortedBy { it.order }
    val upNextLeaf = branch.upNext?.let { id -> stems.flatMap { it.leaves }.firstOrNull { it.id == id } }
    val upNextStem = branch.upNext?.let { id -> stems.firstOrNull { stem -> stem.leaves.any { it.id == id } } }
    val shown = if (showAll) stems else stems.take(STEMS_COLLAPSED)
    // The grid item is a wrapper: SurfaceCard sets its own display, so hiding is done here.
    Div(
        Modifier.styleModifier {
            property("display", if (visible) "grid" else "none")
            property("min-width", "0")
        }.toAttrs()
    ) {
        SurfaceCard(
            CardVariant.Outlined,
            branch.id,
            Modifier.attrsModifier {
                attr("id", branch.id)
                attr("role", "group")
                attr("aria-labelledby", titleId)
            },
            inkBand = true,
        ) {
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("flex-direction", "column")
                    property("gap", space4Var.value())
                    property("height", "100%")
                }.toAttrs()
            ) {
                Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()) }.toAttrs()) {
                    Span(
                        Modifier.styleModifier {
                            property("color", colorOnSurfaceMutedVar.value())
                            property("font-size", typeLabelSizeVar.value())
                            property("line-height", typeLabelLineVar.value())
                            property("font-weight", typeLabelWeightVar.value())
                        }.toAttrs()
                    ) { Text(boughTitle) }
                    H2(
                        Modifier.styleModifier {
                            property("margin", "0")
                            property("min-height", "calc(${TITLE_LINES} * ${typeTitleLineVar.value()})")
                            property("overflow", "hidden")
                            property("display", "-webkit-box")
                            property("-webkit-box-orient", "vertical")
                            property("-webkit-line-clamp", TITLE_LINES.toString())
                            property("color", ink)
                            property("font-size", typeTitleSizeVar.value())
                            property("line-height", typeTitleLineVar.value())
                            property("font-weight", typeTitleWeightVar.value())
                        }.toAttrs {
                            attr("id", titleId)
                            attr("title", branch.title)
                        }
                    ) { Text(branch.title) }
                }
                Meter(branch.counts.live, branch.counts.total, "${branch.title} live topics", branch.id)
                if (upNextLeaf != null) {
                    Div(
                        Modifier.styleModifier {
                            property("background-color", colorStatusUpnextContainerVar.value())
                            property("border-radius", shapeMdVar.value())
                        }.toAttrs()
                    ) {
                        ListRow(
                            title = upNextLeaf.title,
                            branch = branch.id,
                            onClick = upNextStem?.let { target -> fun() { onOpenStem(target) } },
                            supporting = "Up next",
                            clampLines = 2,
                            leading = { StatusIcon(LeafStatus.UpNext) },
                        )
                    }
                }
                Ul(
                    Modifier.styleModifier {
                        property("margin", "0")
                        property("padding", "${space2Var.value()} 0 0")
                        property("list-style", "none")
                        property("border-top", elevation1Var.value())
                    }.toAttrs()
                ) {
                    shown.forEach { stem -> StemRow(branch.id, stem, onOpenStem) }
                    if (stems.size > STEMS_COLLAPSED) {
                        Li {
                            ListRow(
                                title = if (showAll) "Show fewer" else "Show all ${stems.size} stems",
                                modifier = Modifier.attrsModifier { attr("aria-expanded", showAll.toString()) },
                                branch = branch.id,
                                onClick = { showAll = !showAll },
                            )
                        }
                    }
                }
                A(
                    "$TREE_HREF#${branch.id}",
                    Modifier.styleModifier {
                        property("display", "inline-flex")
                        property("align-items", "center")
                        property("align-self", "flex-start")
                        property("margin-top", "auto")
                        property("min-height", Tokens.Spacing.minTargetPx.px)
                        property("padding", "0 ${space3Var.value()}")
                        property("color", ink)
                        property("text-decoration", "underline")
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                    }.toAttrs { attr("aria-label", "See ${branch.title} on the tree") }
                ) { Text("See on the tree") }
            }
        }
    }
}
