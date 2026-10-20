package dev.kotree.site.ui.topics

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space3Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.typeBodySmallLineVar
import dev.kotree.site.typeBodySmallSizeVar
import dev.kotree.site.typeBodySmallWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.CardVariant
import dev.kotree.site.ui.core.ListRow
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.StatusIcon
import dev.kotree.site.ui.core.SurfaceCard
import dev.kotree.site.ui.layout.LinkButton
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

private const val EYEBROW = "Start here"
private const val COPY = "The roots every other branch grows from. Begin here if you are new."

/**
 * Foundation banner (ADR 007): a full-width hero card in the Foundation tint. Left: eyebrow, title, one line of copy,
 * meter and the filled **Start here** link. Right: the Foundation stems as tiles; each opens the stem sheet through
 * [onOpenStem], and the tile that holds the up-next topic carries its status icon. The card id is the branch slug.
 */
@Composable
fun FoundationHero(branch: Branch, startHref: String, onOpenStem: (Stem) -> Unit) {
    val ink = branchInk(branch.id).value().toString()
    val titleId = "${branch.id}-title"
    SurfaceCard(
        CardVariant.Hero,
        branch.id,
        Modifier.attrsModifier {
            attr("id", branch.id)
            attr("role", "group")
            attr("aria-labelledby", titleId)
        },
    ) {
        Div(Modifier.toAttrs { classes(HERO_CLASS) }) {
            Div(Modifier.styleModifier { property("display", "grid"); property("gap", space3Var.value()); property("justify-items", "start") }.toAttrs()) {
                Span(
                    Modifier.styleModifier {
                        property("color", colorOnSurfaceMutedVar.value())
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                    }.toAttrs()
                ) { Text(EYEBROW) }
                H2(
                    Modifier.styleModifier {
                        property("margin", "0")
                        property("color", ink)
                        property("font-size", typeTitleSizeVar.value())
                        property("line-height", typeTitleLineVar.value())
                        property("font-weight", typeTitleWeightVar.value())
                    }.toAttrs { attr("id", titleId) }
                ) { Text(branch.title) }
                P(Modifier.styleModifier { property("margin", "0") }.toAttrs()) { Text(COPY) }
                Meter(
                    branch.counts.live,
                    branch.counts.total,
                    "${branch.title} live topics",
                    branch.id,
                    onTint = true,
                    modifier = Modifier.styleModifier { property("width", "100%") },
                )
                LinkButton("Start here", startHref, kind = ButtonKind.Filled, branch = branch.id)
            }
            Ul(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("padding", "0")
                    property("list-style", "none")
                }.toAttrs { classes(TILES_CLASS) }
            ) {
                branch.stems.sortedBy { it.order }.forEach { stem ->
                    val holdsUpNext = stem.leaves.any { it.status == LeafStatus.UPNEXT }
                    Li(
                        Modifier
                            .attrsModifier { attr("id", stem.id) }
                            .styleModifier {
                                property("min-width", "0")
                                property("background-color", colorSurfaceVar.value())
                                property("border-radius", shapeMdVar.value())
                                property("font-size", typeBodySmallSizeVar.value())
                                property("line-height", typeBodySmallLineVar.value())
                                property("font-weight", typeBodySmallWeightVar.value())
                            }
                            .toAttrs()
                    ) {
                        if (holdsUpNext) {
                            ListRow(
                                title = stem.title,
                                branch = branch.id,
                                onClick = { onOpenStem(stem) },
                                supporting = "${stem.counts.live} of ${stem.counts.total} live, up next",
                                trailing = { StatusIcon(ChipStatus.UpNext) },
                            )
                        } else {
                            ListRow(
                                title = stem.title,
                                branch = branch.id,
                                onClick = { onOpenStem(stem) },
                                supporting = "${stem.counts.live} of ${stem.counts.total} live",
                            )
                        }
                    }
                }
            }
        }
    }
}
