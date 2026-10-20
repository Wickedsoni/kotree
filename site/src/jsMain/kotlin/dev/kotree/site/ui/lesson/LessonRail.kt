package dev.kotree.site.ui.lesson

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
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.lessons.LeafInfo
import dev.kotree.site.lessons.findLeaf
import dev.kotree.site.shapeLgIncreasedVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space8Var
import dev.kotree.site.spaceInsetPanelVar
import dev.kotree.site.spaceStackSectionVar
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.typeBodySmallLineVar
import dev.kotree.site.typeBodySmallSizeVar
import dev.kotree.site.typeBodySmallWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleSmallLineVar
import dev.kotree.site.typeTitleSmallSizeVar
import dev.kotree.site.typeTitleSmallWeightVar
import dev.kotree.site.ui.core.CardVariant
import dev.kotree.site.ui.core.EmptyState
import dev.kotree.site.ui.core.SurfaceCard
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Button as DomButton

private const val OUTLINE_REGION_ID = "kt-lesson-outline"
private const val NO_NEIGHBOURS = "No other lessons are live in this branch yet."

/** Text link in the branch ink, 44 px high. */
@Composable
internal fun TextLink(text: String, href: String, branch: String) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("color", branchInk(branch).value().toString())
            property("text-decoration", "underline")
        }.toAttrs()
    ) { Text(text) }
}

/**
 * The right rail (ADR 007), a column of [RailPanel]s, sticky below the header from bp.large. Order: On this page and
 * Previous and next from bp.large; In this stem below bp.xlarge (from there it moves to [LessonStemColumn]). Below
 * bp.large the rail is a plain block under the lesson and holds only In this stem.
 */
@Composable
internal fun LessonRail(info: LeafInfo, bodyReady: Boolean) {
    Aside(
        Modifier.attrsModifier { attr("aria-label", "Lesson side panel") }.toAttrs { classes(LESSON_RAIL) }
    ) {
        Div(Modifier.toAttrs { classes(LESSON_LARGE_ONLY) }) {
            RailPanel("On this page") { Outline(info.leaf.id, bodyReady, info.branch.id) }
        }
        Div(Modifier.toAttrs { classes(LESSON_BELOW_XLARGE) }) {
            RailPanel("In this stem", stemMeta(info)) { StemNav(info) }
        }
        Div(Modifier.toAttrs { classes(LESSON_LARGE_ONLY) }) {
            RailPanel("Previous and next") { RailNeighbours(info) }
        }
    }
}

/** The left column from bp.xlarge: In this stem. Hidden by CSS below that width. */
@Composable
internal fun LessonStemColumn(info: LeafInfo) {
    Aside(
        Modifier.attrsModifier { attr("aria-label", "Stem navigation") }.toAttrs { classes(LESSON_NAV) }
    ) {
        RailPanel("In this stem", stemMeta(info)) { StemNav(info) }
    }
}

/**
 * Below bp.large the outline is not a rail: a collapsed "On this page" disclosure sits above the lesson body
 * (`aria-expanded`). A choice closes it again so the section is visible.
 */
@Composable
internal fun OutlineDisclosure(info: LeafInfo, bodyReady: Boolean) {
    var open by remember(info.leaf.id) { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }
    val ink = colorOnSurfaceVar.value().toString()
    val percent = if (hovered) Tokens.StateLayer.hoverPercent else 0
    val layer = "color-mix(in srgb, $ink $percent%, transparent)"
    Div(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("margin-top", space4Var.value())
            property("padding", space1Var.value())
            property("background-color", colorSurfaceVar.value())
            property("border", elevation1Var.value())
            property("border-radius", shapeLgIncreasedVar.value())
        }.toAttrs { classes(LESSON_BELOW_LARGE) }
    ) {
        DomButton(
            Modifier.styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("justify-content", "space-between")
                property("box-sizing", "border-box")
                property("width", "100%")
                property("min-height", Tokens.Spacing.minTargetPx.px)
                property("padding", "${space1Var.value()} ${spaceInsetPanelVar.value()}")
                property("border", "0")
                property("border-radius", shapeMdVar.value())
                property("background-color", "transparent")
                property("background-image", "linear-gradient($layer, $layer)")
                property("color", colorOnSurfaceVar.value())
                property("text-align", "start")
                property("font-family", fontUiVar.value())
                property("font-size", typeTitleSmallSizeVar.value())
                property("line-height", typeTitleSmallLineVar.value())
                property("font-weight", typeTitleSmallWeightVar.value())
            }.toAttrs {
                attr("type", "button")
                attr("aria-expanded", open.toString())
                attr("aria-controls", OUTLINE_REGION_ID)
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false }
                onClick { open = !open }
            }
        ) {
            Span { Text("On this page") }
            Span(
                Modifier.styleModifier {
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text(if (open) "Hide" else "Show") }
        }
        Div(
            Modifier.styleModifier {
                if (open) property("padding", "${space2Var.value()} ${space1Var.value()} ${space1Var.value()}")
                else property("display", "none")
            }.toAttrs { id(OUTLINE_REGION_ID) }
        ) {
            if (open) {
                Outline(info.leaf.id, bodyReady, info.branch.id, onNavigate = { open = false })
            }
        }
    }
}

/** Previous / next as compact tonal cards for the rail (label, leaf title, stem title). */
@Composable
private fun RailNeighbours(info: LeafInfo) {
    val previous = info.previousLive
    val next = info.nextLive
    if (previous == null && next == null) {
        EmptyState(NO_NEIGHBOURS, action = { TextLink("See all topics", "/topics", info.branch.id) })
        return
    }
    Nav(
        Modifier.attrsModifier { attr("aria-label", "Previous and next lessons") }.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space3Var.value())
        }.toAttrs()
    ) {
        if (previous != null) CompactNeighbour("Previous", previous, info.branch.id)
        if (next != null) CompactNeighbour("Next", next, info.branch.id)
    }
}

@Composable
private fun CompactNeighbour(label: String, leaf: Leaf, branch: String) {
    var hovered by remember { mutableStateOf(false) }
    val ink = colorOnSurfaceVar.value().toString()
    val percent = if (hovered) Tokens.StateLayer.hoverPercent else 0
    val layer = "color-mix(in srgb, $ink $percent%, transparent)"
    val stemTitle = findLeaf(leaf.id)?.stem?.title.orEmpty()
    A(
        "/${leaf.id}",
        Modifier.styleModifier {
            property("display", "block")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("padding", "${space2Var.value()} ${space3Var.value()}")
            property("background-color", branchTint(branch).value().toString())
            property("background-image", "linear-gradient($layer, $layer)")
            property("border-radius", shapeMdVar.value())
            property("color", colorOnSurfaceVar.value())
            property("text-decoration", "none")
            property("font-family", fontUiVar.value())
        }.toAttrs {
            onMouseEnter { hovered = true }
            onMouseLeave { hovered = false }
            onBlur { hovered = false }
        }
    ) {
        Div(
            Modifier.styleModifier {
                property("color", branchInk(branch).value().toString())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
            }.toAttrs()
        ) { Text(label) }
        Div(
            Modifier.styleModifier {
                property("font-size", typeTitleSmallSizeVar.value())
                property("line-height", typeTitleSmallLineVar.value())
                property("font-weight", typeTitleSmallWeightVar.value())
            }.toAttrs()
        ) { Text(leaf.title) }
        if (stemTitle.isNotEmpty()) {
            Div(
                Modifier.styleModifier {
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeBodySmallSizeVar.value())
                    property("line-height", typeBodySmallLineVar.value())
                    property("font-weight", typeBodySmallWeightVar.value())
                }.toAttrs()
            ) { Text(stemTitle) }
        }
    }
}

/**
 * The end of the lesson (ADR 007): previous at the start and a clear Next card at the end of a two-column grid, then the
 * ways back. With no live neighbour an [EmptyState] with a link replaces the cards.
 */
@Composable
internal fun NextSteps(info: LeafInfo) {
    val branch = info.branch.id
    val previous = info.previousLive
    val next = info.nextLive
    Div(Modifier.styleModifier { property("margin-top", spaceStackSectionVar.value()) }.toAttrs()) {
        if (previous == null && next == null) {
            EmptyState(NO_NEIGHBOURS, action = { TextLink("See all topics", "/topics", branch) })
        } else {
            Nav(Modifier.attrsModifier { attr("aria-label", "Lesson navigation") }.toAttrs { classes(LESSON_PN) }) {
                if (previous != null) {
                    Div { NeighbourCard("Previous lesson", previous, branch, alignEnd = false) }
                }
                if (next != null) {
                    Div(Modifier.toAttrs { classes(LESSON_PN_NEXT) }) { NeighbourCard("Next lesson", next, branch, alignEnd = true) }
                }
            }
        }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("column-gap", space8Var.value())
                property("margin-top", space4Var.value())
            }.toAttrs()
        ) {
            TextLink("Back to the tree", "/#${info.leaf.id}", branch)
            TextLink("See all topics", "/topics", branch)
        }
    }
}

@Composable
private fun NeighbourCard(label: String, leaf: Leaf, branch: String, alignEnd: Boolean) {
    val stemTitle = findLeaf(leaf.id)?.stem?.title.orEmpty()
    SurfaceCard(
        variant = CardVariant.Tonal,
        branch = branch,
        modifier = Modifier.styleModifier { property("height", "100%") },
        href = "/${leaf.id}",
    ) {
        Div(
            Modifier.styleModifier {
                property("text-align", if (alignEnd) "end" else "start")
                property("font-family", fontUiVar.value())
            }.toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("color", branchInk(branch).value().toString())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text(label) }
            Div(
                Modifier.styleModifier {
                    property("margin", "${space1Var.value()} 0")
                    typo(Typo.Title)
                }.toAttrs()
            ) { Text(leaf.title) }
            if (stemTitle.isNotEmpty()) {
                Div(
                    Modifier.styleModifier {
                        property("color", colorOnSurfaceMutedVar.value())
                        property("font-size", typeBodySmallSizeVar.value())
                        property("line-height", typeBodySmallLineVar.value())
                        property("font-weight", typeBodySmallWeightVar.value())
                    }.toAttrs()
                ) { Text(stemTitle) }
            }
        }
    }
}
