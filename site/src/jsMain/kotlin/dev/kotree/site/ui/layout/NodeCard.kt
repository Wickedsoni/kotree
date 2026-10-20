package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorStatusUpnextContainerVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.layoutDialogWidthVar
import dev.kotree.site.layoutHeaderHeightVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeMdVar
import dev.kotree.site.shapeXlIncreasedVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.camera.CARD_ATTRIBUTE
import dev.kotree.site.tree.camera.prefersReducedMotion
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Difficulty as ModelDifficulty
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus as ModelStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.Chip
import dev.kotree.site.ui.core.Difficulty
import dev.kotree.site.ui.core.LeafStatus
import dev.kotree.site.ui.core.ListRow
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.StatusChip
import dev.kotree.site.ui.core.StatusIcon
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.json

/** Shown on every card of a leaf that has no lesson yet (L31 wording rule). */
const val NOT_PUBLISHED_TEXT = "Not published yet. Being written in the order of the tree."

/** Enter animation start (ADR 006: scale 0.92 to 1). */
private const val CARD_FROM_SCALE = 0.92
private const val MS_PER_SECOND = 1000.0

/** Rows listed in a card before the "more" link (ADR 007): leaves of a stem, stems of a branch. */
private const val MAX_LEAF_ROWS = 6
private const val MAX_STEM_ROWS = 8
private const val WHY_LINES = 3

private val moveSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}
private val fadeSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.effects.dampingRatio, Tokens.Spring.effects.stiffness).cssLinear()
}

/**
 * Node card (ADR 006, redesigned in ADR 007): one compact Material 3 Expressive card for a leaf, stem or branch of the
 * tree. shape.xl.increased, elevation.2, white surface, layout.dialog.width wide. A tint band holds the [eyebrow]
 * (where the node sits), the title and optional [chips]; the body scrolls inside the global pill scrollbar when it is
 * taller than the space below the header; the footer is end-aligned: Close, then the [actions] (the primary last).
 * It is a dialog: focus moves into it on open; Escape or Close call [onClose] (the caller gives focus back to the
 * node). Enter: scale 0.92 to 1 with motion.spatial and a fade with motion.effects; a plain cut under
 * prefers-reduced-motion.
 *
 * @param branch branch slug (docs/00 §5) for the ink and tint.
 * @param clearHeader true on the tree: the card never reaches up under the floating header. False for a card shown
 * in normal page flow (the kitchen sink).
 */
@Composable
fun NodeCard(
    branch: String,
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    clearHeader: Boolean = true,
    eyebrow: (@Composable () -> Unit)? = null,
    chips: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    body: @Composable () -> Unit,
) {
    val titleId = "node-card-title"
    val ink = branchInk(branch).value().toString()
    // Room kept free above the card: the floating header pill plus a gap on both sides.
    val topClearance = "calc(${layoutHeaderHeightVar.value()} + 2 * ${space4Var.value()})"
    Div(
        modifier
            .inBranch(branch)
            .attrsModifier {
                attr("role", "dialog")
                attr("aria-labelledby", titleId)
                attr("tabindex", "-1")
                attr(CARD_ATTRIBUTE, "")
            }
            .styleModifier {
                property("display", "flex")
                property("flex-direction", "column")
                property("box-sizing", "border-box")
                property("width", layoutDialogWidthVar.value())
                property("max-width", "100%")
                if (clearHeader) {
                    // The host centres the card in the viewport; this margin keeps its top below the header
                    // for any height, and the max-height keeps its bottom inside the viewport.
                    property("margin-top", "calc($topClearance - ${space6Var.value()})")
                    property("max-height", "calc(${Tokens.Layout.fullViewportHeightVh}vh - $topClearance - ${space6Var.value()})")
                } else {
                    property("max-height", "100%")
                }
                property("overflow", "hidden")
                property("background-color", colorSurfaceVar.value())
                property("border-radius", shapeXlIncreasedVar.value())
                property("box-shadow", elevation2Var.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeBodySizeVar.value())
                property("line-height", typeBodyLineVar.value())
            }
            .toAttrs {
                ref { element ->
                    element.focus()
                    if (!prefersReducedMotion()) {
                        val (moveSeconds, moveEasing) = moveSpring
                        element.asDynamic().animate(
                            arrayOf(json("transform" to "scale($CARD_FROM_SCALE)"), json("transform" to "scale(1)")),
                            json("duration" to moveSeconds * MS_PER_SECOND, "easing" to moveEasing),
                        )
                        val (fadeSeconds, fadeEasing) = fadeSpring
                        element.asDynamic().animate(
                            arrayOf(json("opacity" to 0), json("opacity" to 1)),
                            json("duration" to fadeSeconds * MS_PER_SECOND, "easing" to fadeEasing),
                        )
                    }
                    onDispose { }
                }
                onKeyDown {
                    if (it.key == "Escape") {
                        it.nativeEvent.stopPropagation()
                        it.preventDefault()
                        onClose()
                    }
                }
            }
    ) {
        Div(
            Modifier.styleModifier {
                property("display", "grid")
                property("gap", space2Var.value())
                property("padding", "${space4Var.value()} ${space6Var.value()}")
                property("background-color", branchTint(branch).value().toString())
            }.toAttrs()
        ) {
            if (eyebrow != null) {
                Div(
                    Modifier.styleModifier {
                        property("display", "flex")
                        property("flex-wrap", "wrap")
                        property("align-items", "center")
                        property("gap", space1Var.value())
                        property("color", ink)
                        property("font-size", typeLabelSizeVar.value())
                        property("line-height", typeLabelLineVar.value())
                        property("font-weight", typeLabelWeightVar.value())
                    }.toAttrs()
                ) { eyebrow() }
            }
            H3(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("color", ink)
                    property("font-size", typeTitleSizeVar.value())
                    property("line-height", typeTitleLineVar.value())
                    property("font-weight", typeTitleWeightVar.value())
                }.toAttrs { attr("id", titleId) }
            ) { Text(title) }
            if (chips != null) {
                Div(
                    Modifier.styleModifier {
                        property("display", "flex")
                        property("flex-wrap", "wrap")
                        property("gap", space2Var.value())
                    }.toAttrs()
                ) { chips() }
            }
        }
        Div(
            Modifier.styleModifier {
                property("flex", "1 1 auto")
                property("min-height", "0")
                property("overflow-y", "auto")
                property("padding", "${space4Var.value()} ${space6Var.value()}")
            }.toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("display", "grid")
                    property("gap", space4Var.value())
                }.toAttrs()
            ) { body() }
        }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("justify-content", "flex-end")
                property("gap", space2Var.value())
                property("padding", "${space3Var.value()} ${space6Var.value()}")
                property("border-top", elevation1Var.value())
            }.toAttrs()
        ) {
            Button("Close", onClose, kind = ButtonKind.Text, branch = branch)
            actions()
        }
    }
}

/** A link in the eyebrow line: branch ink, underlined. Text-sized, so it is only as tall as its line. */
@Composable
private fun EyebrowLink(text: String, href: String) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("min-height", Tokens.Space.hitAreaPx.px)
            property("color", "inherit")
            property("text-decoration", "underline")
        }.toAttrs()
    ) { Text(text) }
}

@Composable
private fun EyebrowSeparator() {
    Span(Modifier.attrsModifier { attr("aria-hidden", "true") }.toAttrs()) { Text("›") }
}

/** Running text of a card, muted, cut after [lines] lines; the full text stays in `title`. */
@Composable
private fun ClampedText(text: String, lines: Int) {
    P(
        Modifier.styleModifier {
            property("margin", "0")
            property("color", colorOnSurfaceMutedVar.value())
            property("overflow", "hidden")
            property("display", "-webkit-box")
            property("-webkit-box-orient", "vertical")
            property("-webkit-line-clamp", lines.toString())
        }.toAttrs { attr("title", text) }
    ) { Text(text) }
}

/**
 * Wrapper for a list of rows. The rows carry their own inline padding, so the wrapper reaches out by the same amount:
 * the row text lines up with the title above it and only the hover fill uses the extra width.
 */
@Composable
private fun RowList(content: @Composable () -> Unit) {
    Div(
        Modifier.styleModifier {
            property("margin", "0 calc(-1 * ${space3Var.value()})")
        }.toAttrs()
    ) { content() }
}

/** "n/m" at the end of a stem row. The visible text is short; the label says it in words. */
@Composable
private fun CountLabel(live: Int, total: Int) {
    Span(
        Modifier
            .attrsModifier {
                attr("role", "img")
                attr("aria-label", "$live of $total live")
            }
            .styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("font-variant-numeric", "tabular-nums")
            }
            .toAttrs()
    ) { Text("$live/$total") }
}

private fun ModelStatus.toUi(): LeafStatus = when (this) {
    ModelStatus.LIVE -> LeafStatus.Live
    ModelStatus.UPNEXT -> LeafStatus.UpNext
    ModelStatus.SHADOW -> LeafStatus.Shadow
}

private fun ModelDifficulty.toUi(): Difficulty = when (this) {
    ModelDifficulty.EASY -> Difficulty.Easy
    ModelDifficulty.MEDIUM -> Difficulty.Medium
    ModelDifficulty.HARD -> Difficulty.Hard
}

/** One leaf of a stem card: status icon, title (a link when live), a chip for live and up-next only. */
@Composable
private fun LeafRow(leaf: Leaf, branch: String) {
    val status = leaf.status.toUi()
    val path = leaf.path.takeIf { leaf.status == ModelStatus.LIVE }
    if (leaf.status == ModelStatus.SHADOW) {
        ListRow(
            title = leaf.title,
            branch = branch,
            href = path,
            muted = true,
            clampLines = 2,
            leading = { StatusIcon(status) },
        )
    } else {
        ListRow(
            title = leaf.title,
            branch = branch,
            href = path,
            muted = path == null,
            clampLines = 2,
            leading = { StatusIcon(status) },
            trailing = { StatusChip(status, compact = true) },
        )
    }
}

/** Card of a leaf. Live: summary and **Open lesson**. Up next or coming soon: the not-published text, why, live neighbours, topics link. */
@Composable
fun LeafNodeCard(leaf: Leaf, branch: Branch, stem: Stem, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val live = leaf.status == ModelStatus.LIVE
    val path = leaf.path
    NodeCard(
        branch = branch.id,
        title = leaf.title,
        onClose = onClose,
        modifier = modifier,
        eyebrow = {
            EyebrowLink(branch.title, "/#${branch.id}")
            EyebrowSeparator()
            EyebrowLink(stem.title, "/#${stem.id}")
        },
        chips = {
            StatusChip(leaf.status.toUi(), compact = true)
            val difficulty = leaf.difficulty.toUi()
            Chip(difficulty.label, difficulty.swatch, compact = true, fill = colorSurfaceVar.value().toString())
        },
        actions = {
            if (live && path != null) {
                LinkButton("Open lesson", path, kind = ButtonKind.Filled, branch = branch.id)
            } else {
                LinkButton("See all topics", "/topics#${stem.id}", kind = ButtonKind.Tonal, branch = branch.id)
            }
        },
    ) {
        if (live) {
            P(Modifier.styleModifier { property("margin", "0") }.toAttrs()) { Text(leaf.summary.orEmpty()) }
        } else {
            P(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text(NOT_PUBLISHED_TEXT) }
            ClampedText(stem.why, WHY_LINES)
            val index = stem.leaves.indexOfFirst { it.id == leaf.id }
            val previous = stem.leaves.take(maxOf(index, 0)).lastOrNull { it.status == ModelStatus.LIVE && it.path != null }
            val next = stem.leaves.drop(index + 1).firstOrNull { it.status == ModelStatus.LIVE && it.path != null }
            val previousPath = previous?.path
            val nextPath = next?.path
            if (previous != null && previousPath != null || next != null && nextPath != null) {
                RowList {
                    if (previous != null && previousPath != null) {
                        ListRow(
                            title = previous.title,
                            branch = branch.id,
                            href = previousPath,
                            supporting = "Live, before this topic",
                            leading = { StatusIcon(LeafStatus.Live) },
                        )
                    }
                    if (next != null && nextPath != null) {
                        ListRow(
                            title = next.title,
                            branch = branch.id,
                            href = nextPath,
                            supporting = "Live, after this topic",
                            leading = { StatusIcon(LeafStatus.Live) },
                        )
                    }
                }
            }
        }
    }
}

/** Card of a stem: why, a meter, up to six leaves with their status (live ones link to the lesson), and the topics link. */
@Composable
fun StemNodeCard(stem: Stem, branch: Branch, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val leaves = stem.leaves.sortedBy { it.order }
    NodeCard(
        branch = branch.id,
        title = stem.title,
        onClose = onClose,
        modifier = modifier,
        eyebrow = { EyebrowLink(branch.title, "/#${branch.id}") },
        actions = { LinkButton("Open in Topics", "/topics#${stem.id}", kind = ButtonKind.Tonal, branch = branch.id) },
    ) {
        ClampedText(stem.why, WHY_LINES)
        Meter(stem.counts.live, stem.counts.total, "${stem.title} live topics", branch.id)
        RowList {
            leaves.take(MAX_LEAF_ROWS).forEach { LeafRow(it, branch.id) }
            if (leaves.size > MAX_LEAF_ROWS) {
                ListRow(
                    title = "+${leaves.size - MAX_LEAF_ROWS} more in Topics",
                    branch = branch.id,
                    href = "/topics#${stem.id}",
                )
            }
        }
    }
}

/** Card of a branch: a meter, the up-next topic, up to eight stems (each flies to the stem via [onStem]), and the topics link. */
@Composable
fun BranchNodeCard(branch: Branch, onStem: (Stem) -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val stems = branch.stems.sortedBy { it.order }
    val upNext = branch.upNext?.let { id -> stems.flatMap { it.leaves }.firstOrNull { it.id == id } }
    val upNextStem = branch.upNext?.let { id -> stems.firstOrNull { stem -> stem.leaves.any { it.id == id } } }
    val goUpNext = upNextStem?.let { target -> fun() { onStem(target) } }
    val boughTitle = treeData.boughs.firstOrNull { bough -> bough.branches.any { it.id == branch.id } }?.title
    NodeCard(
        branch = branch.id,
        title = branch.title,
        onClose = onClose,
        modifier = modifier,
        eyebrow = { if (boughTitle != null) Text(boughTitle) },
        actions = { LinkButton("Open in Topics", "/topics#${branch.id}", kind = ButtonKind.Tonal, branch = branch.id) },
    ) {
        Meter(branch.counts.live, branch.counts.total, "${branch.title} live topics", branch.id)
        if (upNext != null) {
            Div(
                Modifier.styleModifier {
                    property("background-color", colorStatusUpnextContainerVar.value())
                    property("border-radius", shapeMdVar.value())
                }.toAttrs()
            ) {
                ListRow(
                    title = upNext.title,
                    branch = branch.id,
                    onClick = goUpNext,
                    supporting = "Up next",
                    clampLines = 2,
                    leading = { StatusIcon(LeafStatus.UpNext) },
                )
            }
        }
        RowList {
            stems.take(MAX_STEM_ROWS).forEach { stem ->
                ListRow(
                    title = stem.title,
                    branch = branch.id,
                    onClick = { onStem(stem) },
                    trailing = { CountLabel(stem.counts.live, stem.counts.total) },
                )
            }
            if (stems.size > MAX_STEM_ROWS) {
                ListRow(
                    title = "All ${stems.size} stems in Topics",
                    branch = branch.id,
                    href = "/topics#${branch.id}",
                )
            }
        }
    }
}
