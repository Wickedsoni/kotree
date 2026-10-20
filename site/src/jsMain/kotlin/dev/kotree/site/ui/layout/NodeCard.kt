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
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeXlIncreasedVar
import dev.kotree.site.shapeXsVar
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
import dev.kotree.site.ui.core.Difficulty
import dev.kotree.site.ui.core.DifficultyChip
import dev.kotree.site.ui.core.LeafStatus
import dev.kotree.site.ui.core.StatusChip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import kotlin.js.json

/** Shown on every card of a leaf that has no lesson yet (L31 wording rule). */
const val NOT_PUBLISHED_TEXT = "Not published yet. Being written in the order of the tree."

/** Enter animation start (ADR 006: scale 0.92 to 1). */
private const val CARD_FROM_SCALE = 0.92
private const val MS_PER_SECOND = 1000.0

private val moveSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}
private val fadeSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.effects.dampingRatio, Tokens.Spring.effects.stiffness).cssLinear()
}

/**
 * Node card (ADR 006): one Material 3 Expressive card for a leaf, stem or branch of the tree. shape.xl.increased,
 * elevation.2, white surface, a header band in the branch tint. It is a dialog: focus moves into it on open; Escape or
 * Close call [onClose] (the caller gives focus back to the node). Enter: scale 0.92 to 1 with motion.spatial and a fade
 * with motion.effects; a plain cut under prefers-reduced-motion. The caller decides where it is placed.
 *
 * @param branch branch slug (docs/00 §5) for the ink and tint.
 * @param chips context chips in the header band, above the title.
 * @param actions buttons beside Close in the footer.
 */
@Composable
fun NodeCard(
    branch: String,
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    chips: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    body: @Composable () -> Unit,
) {
    val titleId = "node-card-title"
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
                property("max-height", "100%")
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
                property("padding", "${space4Var.value()} ${space6Var.value()}")
                property("background-color", branchTint(branch).value().toString())
            }.toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("flex-wrap", "wrap")
                    property("gap", space2Var.value())
                    property("margin-bottom", space2Var.value())
                }.toAttrs()
            ) { chips() }
            H3(
                Modifier.attrsModifier { id(titleId) }.styleModifier {
                    property("margin", "0")
                    property("color", branchInk(branch).value().toString())
                    property("font-size", typeTitleSizeVar.value())
                    property("line-height", typeTitleLineVar.value())
                    property("font-weight", typeTitleWeightVar.value())
                }.toAttrs()
            ) { Text(title) }
        }
        Div(
            Modifier.styleModifier {
                property("flex", "1 1 auto")
                property("min-height", "0")
                property("overflow-y", "auto")
                property("padding", "${space4Var.value()} ${space6Var.value()}")
            }.toAttrs()
        ) { body() }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("gap", space2Var.value())
                property("padding", "${space3Var.value()} ${space6Var.value()}")
                property("border-top", elevation1Var.value())
            }.toAttrs()
        ) {
            actions()
            Button("Close", onClose, kind = ButtonKind.Text, branch = branch)
        }
    }
}

/** A link in the card's running text: branch ink, underlined, 44 px tall target. */
@Composable
private fun CardLink(text: String, href: String, branch: String) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("color", branchInk(branch).value().toString())
            property("font-weight", typeLabelWeightVar.value())
        }.toAttrs()
    ) { Text(text) }
}

/** A chip that links to the tree or topics (branch and stem context). */
@Composable
private fun LinkChip(text: String, href: String, branch: String) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("padding", "0 ${space2Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeXsVar.value())
            property("background-color", colorSurfaceVar.value())
            property("color", branchInk(branch).value().toString())
            property("text-decoration", "none")
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
            property("white-space", "nowrap")
        }.toAttrs()
    ) { Text(text) }
}

@Composable
private fun Muted(text: String) {
    P(
        Modifier.styleModifier {
            property("margin", "0 0 ${space3Var.value()}")
            property("color", colorOnSurfaceMutedVar.value().toString())
        }.toAttrs()
    ) { Text(text) }
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

private fun go(href: String) {
    window.location.href = href
}

/** Card of a leaf. Live: summary and **Open lesson**. Up next or coming soon: the not-published text, why, topics link, live neighbours. */
@Composable
fun LeafNodeCard(leaf: Leaf, branch: Branch, stem: Stem, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val live = leaf.status == ModelStatus.LIVE
    val path = leaf.path
    NodeCard(
        branch = branch.id,
        title = leaf.title,
        onClose = onClose,
        modifier = modifier,
        chips = {
            LinkChip(branch.title, "/#${branch.id}", branch.id)
            LinkChip(stem.title, "/#${stem.id}", branch.id)
            DifficultyChip(leaf.difficulty.toUi())
            StatusChip(leaf.status.toUi())
        },
        actions = {
            if (live && path != null) {
                Button("Open lesson", { go(path) }, kind = ButtonKind.Filled, branch = branch.id)
            } else {
                Button("See all topics", { go("/topics#${stem.id}") }, kind = ButtonKind.Tonal, branch = branch.id)
            }
        },
    ) {
        if (live) {
            P(Modifier.styleModifier { property("margin", "0") }.toAttrs()) { Text(leaf.summary.orEmpty()) }
        } else {
            P(Modifier.styleModifier { property("margin", "0 0 ${space3Var.value()}"); property("font-weight", typeLabelWeightVar.value()) }.toAttrs()) {
                Text(NOT_PUBLISHED_TEXT)
            }
            Muted(stem.why)
            val index = stem.leaves.indexOfFirst { it.id == leaf.id }
            val previous = stem.leaves.take(maxOf(index, 0)).lastOrNull { it.status == ModelStatus.LIVE && it.path != null }
            val next = stem.leaves.drop(index + 1).firstOrNull { it.status == ModelStatus.LIVE && it.path != null }
            if (previous != null || next != null) {
                Muted("Lessons that are live in this stem:")
                val previousPath = previous?.path
                val nextPath = next?.path
                if (previous != null && previousPath != null) CardLink("Before: ${previous.title}", previousPath, branch.id)
                if (next != null && nextPath != null) CardLink("After: ${next.title}", nextPath, branch.id)
            }
        }
    }
}

/** Card of a stem: why, "N of M grown", every leaf with its status (live ones link to the lesson), and the topics link. */
@Composable
fun StemNodeCard(stem: Stem, branch: Branch, onClose: () -> Unit, modifier: Modifier = Modifier) {
    NodeCard(
        branch = branch.id,
        title = stem.title,
        onClose = onClose,
        modifier = modifier,
        chips = {
            LinkChip(branch.title, "/#${branch.id}", branch.id)
            LinkChip("${stem.counts.live} of ${stem.counts.total} grown", "/topics#${stem.id}", branch.id)
        },
        actions = { Button("Open in topics", { go("/topics#${stem.id}") }, kind = ButtonKind.Tonal, branch = branch.id) },
    ) {
        Muted(stem.why)
        Ul(
            Modifier.styleModifier {
                property("list-style", "none")
                property("margin", "0")
                property("padding", "0")
            }.toAttrs()
        ) {
            for (leaf in stem.leaves) {
                val path = leaf.path
                Li(
                    Modifier.styleModifier {
                        property("display", "flex")
                        property("align-items", "center")
                        property("flex-wrap", "wrap")
                        property("gap", space2Var.value())
                        property("padding", "${space1Var.value()} 0")
                    }.toAttrs()
                ) {
                    StatusChip(leaf.status.toUi())
                    if (leaf.status == ModelStatus.LIVE && path != null) CardLink(leaf.title, path, branch.id) else Text(leaf.title)
                }
            }
        }
    }
}

/** Card of a branch: progress, its stems (each flies to the stem via [onStem]), the up-next leaf, and the topics link. */
@Composable
fun BranchNodeCard(branch: Branch, onStem: (Stem) -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val upNext = branch.upNext?.let { id -> branch.stems.flatMap { it.leaves }.firstOrNull { it.id == id } }
    NodeCard(
        branch = branch.id,
        title = branch.title,
        onClose = onClose,
        modifier = modifier,
        chips = { LinkChip("${branch.counts.live} of ${branch.counts.total} grown", "/topics#${branch.id}", branch.id) },
        actions = { Button("Open in topics", { go("/topics#${branch.id}") }, kind = ButtonKind.Tonal, branch = branch.id) },
    ) {
        if (upNext != null) Muted("Up next: ${upNext.title}")
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-direction", "column")
                property("align-items", "flex-start")
            }.toAttrs()
        ) {
            for (stem in branch.stems) {
                Button(
                    "${stem.title} (${stem.counts.live} of ${stem.counts.total})",
                    { onStem(stem) },
                    kind = ButtonKind.Text,
                    branch = branch.id,
                )
            }
        }
    }
}
