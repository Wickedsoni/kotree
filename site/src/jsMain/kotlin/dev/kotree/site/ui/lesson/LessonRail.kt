package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import dev.kotree.site.layoutRailWidthVar
import dev.kotree.site.lessons.LeafInfo
import dev.kotree.site.lessons.findLeaf
import dev.kotree.site.shapeXlVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space8Var
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.ui.core.StatusChip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Ol
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

/**
 * True from the viewport width `layout.rail.min-viewport` up. CSS media queries cannot read custom properties, so the
 * constant is [Tokens.LayoutW.railMinViewport] and the query is a `matchMedia` listener (ADR 006).
 */
@Composable
internal fun rememberWideLayout(): Boolean {
    val query = remember { window.matchMedia("(min-width: ${Tokens.LayoutW.railMinViewport}px)") }
    var wide by remember { mutableStateOf(query.matches) }
    DisposableEffect(query) {
        val onChange: (Event) -> Unit = { wide = query.matches }
        query.addEventListener("change", onChange)
        onDispose { query.removeEventListener("change", onChange) }
    }
    return wide
}

/**
 * The side rail (ADR 006): outline (wide only), stem progress, previous / next. [wide] = two-column layout: the rail
 * is sticky and scrolls on its own. Narrow: a plain block under the lesson body.
 */
@Composable
internal fun LessonRail(info: LeafInfo, wide: Boolean, bodyReady: Boolean) {
    val branch = info.branch.id
    Aside(
        Modifier.attrsModifier { attr("aria-label", "Lesson side panel") }.styleModifier {
            property("box-sizing", "border-box")
            if (wide) {
                property("width", layoutRailWidthVar.value())
                property("position", "sticky")
                property("top", space8Var.value())
                property("align-self", "start")
                property("max-height", "calc(100vh - ${space8Var.value()} - ${space8Var.value()})")
                property("overflow-y", "auto")
            } else {
                property("margin-top", space8Var.value())
            }
        }.toAttrs()
    ) {
        if (wide) {
            RailCard("On this page") { Outline(info.leaf.id, bodyReady, branch) }
        }
        RailCard("Progress in ${info.stem.title}") { StemProgress(info) }
        RailCard("Previous and next") { PrevNextCards(info, "Previous and next lessons") }
    }
}

@Composable
private fun RailCard(title: String, content: @Composable () -> Unit) {
    Section(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("margin-bottom", space4Var.value())
            property("padding", space4Var.value())
            property("background-color", colorSurfaceVar.value())
            property("border", elevation1Var.value())
            property("border-radius", shapeXlVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        H2(
            Modifier.styleModifier {
                property("margin", "0 0 ${space3Var.value()}")
                typo(Typo.Title)
            }.toAttrs()
        ) { Text(title) }
        content()
    }
}

/** "N of M grown in <stem>" and every leaf of the stem in order with its status. Live leaves link. */
@Composable
private fun StemProgress(info: LeafInfo) {
    val leaves = info.stem.leaves
    val grown = leaves.count { it.status == LeafStatus.LIVE }
    val branch = info.branch.id
    Div(
        Modifier.styleModifier {
            property("margin-bottom", space3Var.value())
            property("color", colorOnSurfaceMutedVar.value())
            typo(Typo.Label)
        }.toAttrs()
    ) { Text("$grown of ${leaves.size} grown in ${info.stem.title}") }
    Ol(
        Modifier.styleModifier {
            property("margin", "0")
            property("padding", "0")
            property("list-style", "none")
        }.toAttrs()
    ) {
        leaves.forEach { leaf ->
            val isCurrent = leaf.id == info.leaf.id
            Li(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("flex-wrap", "wrap")
                    property("align-items", "center")
                    property("justify-content", "space-between")
                    property("gap", space2Var.value())
                    property("box-sizing", "border-box")
                    property("min-height", Tokens.Spacing.minTargetPx.px)
                    property("padding", "${space1Var.value()} ${space2Var.value()}")
                    property("border-radius", shapeXlVar.value())
                    property("background-color", if (isCurrent) branchTint(branch).value().toString() else "transparent")
                    typo(Typo.Label)
                }.toAttrs()
            ) {
                if (isCurrent) {
                    Span(Modifier.attrsModifier { attr("aria-current", "page") }.toAttrs()) { Text(leaf.title) }
                } else if (leaf.status == LeafStatus.LIVE) {
                    A(
                        "/${leaf.id}",
                        Modifier.styleModifier {
                            property("color", branchInk(branch).value().toString())
                            property("text-decoration", "underline")
                        }.toAttrs()
                    ) { Text(leaf.title) }
                } else {
                    Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) { Text(leaf.title) }
                }
                StatusChip(leaf.status.chip())
            }
        }
    }
}

private fun LeafStatus.chip(): ChipStatus = when (this) {
    LeafStatus.SHADOW -> ChipStatus.Shadow
    LeafStatus.UPNEXT -> ChipStatus.UpNext
    LeafStatus.LIVE -> ChipStatus.Live
}

/**
 * Previous / next as tonal cards: label, leaf title and stem title (`previousLive`, `nextLive`). The up-next leaf
 * shows as text without a link. Used in the rail and at the bottom of the page.
 */
@Composable
internal fun PrevNextCards(info: LeafInfo, label: String = "Lesson navigation") {
    Nav(
        Modifier.attrsModifier { attr("aria-label", label) }.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space3Var.value())
        }.toAttrs()
    ) {
        info.previousLive?.let { NeighbourCard("Previous", it, info.branch.id) }
        info.nextLive?.let { NeighbourCard("Next", it, info.branch.id) }
        info.comingNext?.let {
            Div(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()) {
                Text("Coming next: ${it.title}")
            }
        }
        if (info.previousLive == null && info.nextLive == null && info.comingNext == null) {
            Div(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()) {
                Text("No other lessons are live in this branch yet.")
            }
        }
    }
}

@Composable
private fun NeighbourCard(label: String, leaf: Leaf, branch: String) {
    val stemTitle = findLeaf(leaf.id)?.stem?.title.orEmpty()
    A(
        "/${leaf.id}",
        Modifier.styleModifier {
            property("display", "block")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("padding", space3Var.value())
            property("background-color", branchTint(branch).value().toString())
            property("border-radius", shapeXlVar.value())
            property("color", colorOnSurfaceVar.value())
            property("text-decoration", "none")
        }.toAttrs()
    ) {
        Div(Modifier.styleModifier { property("color", branchInk(branch).value().toString()); typo(Typo.Label) }.toAttrs()) { Text(label) }
        Div(Modifier.styleModifier { typo(Typo.Title) }.toAttrs()) { Text(leaf.title) }
        if (stemTitle.isNotEmpty()) {
            Div(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()) { Text(stemTitle) }
        }
    }
}
