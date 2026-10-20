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
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorStatusUpnextContainerVar
import dev.kotree.site.colorStatusUpnextVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation3Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeXlIncreasedVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.shapeXxlVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

/** Branches with at least this many stems get a two-column card (ADR 006). */
const val WIDE_STEM_COUNT = 10

private const val TREE_HREF = "/"
private const val PERCENT_FULL = 100

private val morphSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatialFast.dampingRatio, Tokens.Spring.spatialFast.stiffness).cssLinear()
}

enum class CardKind { Hero, Wide, Regular }

/**
 * One branch card of the bento grid (ADR 006). The id is the branch slug (anchor `/topics#<branch>`). Hero =
 * Foundation: fully tinted, shape.xxl, a "Start here" link. Others: white surface, outline border, ink top band;
 * hover morphs shape.xl to shape.xl.increased and lifts to elevation.3 (instant under reduced motion, Base.kt).
 *
 * @param openStemId stem id from the URL hash, opened and scrolled to by the page.
 */
@Composable
fun BranchCard(
    branch: Branch,
    kind: CardKind,
    titles: Map<String, String>,
    openStemId: String?,
    startHref: String,
    modifier: Modifier = Modifier,
) {
    var hovered by remember { mutableStateOf(false) }
    val hero = kind == CardKind.Hero
    val ink = branchInk(branch.id).value().toString()
    val tint = branchTint(branch.id).value().toString()
    val (seconds, easing) = morphSpring
    val upNextTitle = branch.upNext?.let { titles[it] }
    val upNextStemId = branch.upNext?.substringBeforeLast("/")
    val sizeClass = when (kind) {
        CardKind.Hero -> BENTO_HERO_CLASS
        CardKind.Wide -> BENTO_WIDE_CLASS
        CardKind.Regular -> ""
    }
    Section(
        modifier
            .inBranch(branch.id)
            .styleModifier {
                property("display", "flex")
                property("flex-direction", "column")
                property("box-sizing", "border-box")
                property("min-width", "0")
                property("padding", space6Var.value())
                property("border", if (hero) "0" else elevation1Var.value().toString())
                property("border-top", "${space1Var.value()} solid $ink")
                property("border-radius", if (hero) shapeXxlVar.value().toString() else if (hovered) shapeXlIncreasedVar.value().toString() else shapeXlVar.value().toString())
                property("background-color", if (hero) tint else colorSurfaceVar.value().toString())
                property("box-shadow", if (hovered) elevation3Var.value() else "none")
                property("font-family", fontUiVar.value())
                property("color", colorOnSurfaceVar.value())
                property("scroll-margin-top", space4Var.value())
                property("transition", "border-radius ${seconds}s $easing, box-shadow ${seconds}s $easing")
            }
            .toAttrs {
                attr("id", branch.id)
                attr("aria-labelledby", "${branch.id}-title")
                if (sizeClass.isNotEmpty()) classes(sizeClass)
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false }
            }
    ) {
        H3(
            Modifier.styleModifier {
                property("margin", "0")
                property("color", ink)
                property("font-size", typeTitleSizeVar.value())
                property("line-height", typeTitleLineVar.value())
                property("font-weight", typeTitleWeightVar.value())
            }.toAttrs { attr("id", "${branch.id}-title") }
        ) { Text(branch.title) }
        Progress(branch, ink, if (hero) colorSurfaceVar.value().toString() else tint)
        if (upNextTitle != null) {
            Div(
                Modifier.styleModifier {
                    property("margin", "${space3Var.value()} 0 0")
                    property("padding", "${space2Var.value()} ${space3Var.value()}")
                    property("border-radius", shapeFullVar.value())
                    property("background-color", colorStatusUpnextContainerVar.value())
                    property("color", colorStatusUpnextVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text("Up next: $upNextTitle") }
        }
        if (hero) {
            A(
                startHref,
                Modifier.styleModifier {
                    property("display", "inline-flex")
                    property("align-items", "center")
                    property("align-self", "flex-start")
                    property("justify-content", "center")
                    property("box-sizing", "border-box")
                    property("margin", "${space4Var.value()} 0 0")
                    property("min-height", Tokens.Spacing.minTargetPx.px)
                    property("min-width", Tokens.Spacing.minTargetPx.px)
                    property("padding", "0 ${space4Var.value()}")
                    property("border-radius", shapeFullVar.value())
                    property("background-color", ink)
                    property("color", colorOnBranchVar.value())
                    property("text-decoration", "none")
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text("Start here") }
        }
        Ul(
            Modifier.styleModifier {
                property("margin", "${space4Var.value()} 0 0")
                property("padding", "0")
                property("list-style", "none")
            }.toAttrs()
        ) {
            branch.stems.sortedBy { it.order }.forEach { stem ->
                StemRow(branch, stem, titles, stem.id == openStemId || stem.id == upNextStemId)
            }
        }
        A(
            "$TREE_HREF#${branch.id}",
            Modifier.styleModifier {
                property("display", "inline-flex")
                property("align-items", "center")
                property("align-self", "flex-start")
                property("margin", "${space2Var.value()} 0 0")
                property("min-height", Tokens.Spacing.minTargetPx.px)
                property("color", ink)
                property("text-decoration", "underline")
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
            }.toAttrs { attr("aria-label", "See ${branch.title} on the tree") }
        ) { Text("See on the tree") }
    }
}

/** "x of y live" text and a linear bar (ink on tint) with progressbar semantics. */
@Composable
private fun Progress(branch: Branch, ink: String, track: String) {
    val total = branch.counts.total
    val percent = if (total == 0) 0 else branch.counts.live * PERCENT_FULL / total
    Div(
        Modifier.styleModifier {
            property("margin", "${space2Var.value()} 0 0")
            property("color", colorOnSurfaceMutedVar.value())
            property("font-size", typeBodySizeVar.value())
            property("line-height", typeBodyLineVar.value())
        }.toAttrs()
    ) { Text("${branch.counts.live} of ${branch.counts.total} live") }
    Div(
        Modifier.attrsModifier {
            attr("role", "progressbar")
            attr("aria-label", "${branch.title} live topics")
            attr("aria-valuemin", "0")
            attr("aria-valuemax", total.toString())
            attr("aria-valuenow", branch.counts.live.toString())
            attr("aria-valuetext", "${branch.counts.live} of $total live")
        }.styleModifier {
            property("overflow", "hidden")
            property("height", space2Var.value())
            property("margin", "${space2Var.value()} 0 0")
            property("border-radius", shapeFullVar.value())
            property("background-color", track)
        }.toAttrs()
    ) {
        Div(
            Modifier.styleModifier {
                property("width", "$percent%")
                property("height", "100%")
                property("background-color", ink)
            }.toAttrs()
        )
    }
}
