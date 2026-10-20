package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.layoutHeaderHeightVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutPageMaxVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Text

/** Which nav pill is current (ADR 006). [Lesson] marks no pill: a lesson is not a destination of the nav. */
enum class NavItem { Tree, Topics, Lesson }

private const val BRANCH_DEFAULT = "kotlin"
private const val HOME_HREF = "/"
private const val TOPICS_HREF = "/topics"
/** Working tagline, decision D14 in docs/00. */
private const val TAGLINE = "Grow your Android knowledge, one leaf at a time."
/** Class of the tagline: hidden below layout.bp.large, shown from there (rule registered below). */
private const val TAGLINE_CLASS = "kt-header-tagline"

/** The tagline needs room: it appears from layout.bp.large only (ADR 007). */
@InitSilk
fun initHeaderStyles(ctx: InitSilkContext) {
    val large = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpLarge}px"))
    ctx.stylesheet.registerStyle(".$TAGLINE_CLASS") {
        base { Modifier.styleModifier { property("display", "none") } }
        cssRule(large) { Modifier.styleModifier { property("display", "inline") } }
    }
}

/**
 * Site header (ADR 007). In flow (default): a sticky, full-bleed bar of layout.header.height with a bottom border and an
 * inner row on the page max-width grid, so the wordmark lines up with the page content. It stays in the document flow,
 * so nothing needs a top offset; hash targets clear it through `scroll-padding-top` (Base.kt). No ancestor may set
 * `overflow`, or sticky stops working. [floating] = true sits over the tree canvas (surface fill, elevation.2,
 * shape.full) and is unchanged. The current pill carries `aria-current="page"` and a tonal fill.
 */
@Composable
fun SiteHeader(current: NavItem, floating: Boolean = false, modifier: Modifier = Modifier) {
    if (floating) {
        Header(
            modifier
                .styleModifier {
                    property("display", "flex")
                    property("align-items", "center")
                    property("gap", space4Var.value())
                    property("box-sizing", "border-box")
                    property("padding", "${space2Var.value()} ${space6Var.value()}")
                    property("background-color", colorSurfaceVar.value())
                    property("font-family", fontUiVar.value())
                    property("box-shadow", elevation2Var.value())
                    property("border-radius", shapeFullVar.value())
                }
                .toAttrs()
        ) {
            HeaderContent(current, tagline = false, pushCounter = false)
        }
    } else {
        Header(
            modifier
                .styleModifier {
                    property("position", "sticky")
                    property("top", "0")
                    property("z-index", Tokens.Z.header.toString())
                    property("box-sizing", "border-box")
                    property("height", layoutHeaderHeightVar.value())
                    property("background-color", colorSurfaceVar.value())
                    property("border-bottom", elevation1Var.value())
                    property("font-family", fontUiVar.value())
                }
                .toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("align-items", "center")
                    property("gap", space4Var.value())
                    property("box-sizing", "border-box")
                    property("height", "100%")
                    property("max-width", layoutPageMaxVar.value())
                    property("margin", "0 auto")
                    property("padding", "0 ${layoutPageGutterVar.value()}")
                }.toAttrs()
            ) {
                HeaderContent(current, tagline = true, pushCounter = true)
            }
        }
    }
}

/** Wordmark (+ tagline), nav pills and counter, in reading order: wordmark, Tree, Topics. */
@Composable
private fun HeaderContent(current: NavItem, tagline: Boolean, pushCounter: Boolean) {
    A(
        HOME_HREF,
        Modifier.styleModifier {
            property("color", branchInk("foundation").value().toString())
            property("text-decoration", "none")
            property("font-size", typeTitleSizeVar.value())
            property("line-height", typeTitleLineVar.value())
            property("font-weight", typeTitleWeightVar.value())
        }.toAttrs()
    ) { Text("Kotree") }
    if (tagline) {
        Span(
            Modifier.styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("white-space", "nowrap")
            }.toAttrs { classes(TAGLINE_CLASS) }
        ) { Text(TAGLINE) }
    }
    Nav(
        Modifier.styleModifier {
            property("display", "flex")
            property("align-items", "center")
            property("gap", space2Var.value())
            // wordmark + gap + this margin = space.8 after the wordmark
            if (tagline) property("margin-left", space4Var.value())
        }.toAttrs { attr("aria-label", "Main") }
    ) {
        NavPill("Tree", HOME_HREF, current == NavItem.Tree)
        NavPill("Topics", TOPICS_HREF, current == NavItem.Topics)
    }
    ProgressCounter(
        treeData.counts.live,
        treeData.counts.total,
        if (pushCounter) Modifier.styleModifier { property("margin-left", "auto") } else Modifier,
    )
}

/** One nav pill: a link with a 44 px target. Current = tonal fill and ink text; the focus ring comes from Base.kt. */
@Composable
private fun NavPill(text: String, href: String, isCurrent: Boolean) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("justify-content", "center")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("min-width", Tokens.Spacing.minTargetPx.px)
            property("padding", "0 ${space3Var.value()}")
            property("border-radius", shapeFullVar.value())
            property("background-color", if (isCurrent) branchTint(BRANCH_DEFAULT).value().toString() else "transparent")
            property("color", if (isCurrent) branchInk(BRANCH_DEFAULT).value().toString() else colorOnSurfaceVar.value().toString())
            property("text-decoration", "none")
            property("font-family", fontUiVar.value())
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
            property("white-space", "nowrap")
        }.toAttrs {
            if (isCurrent) attr("aria-current", "page")
        }
    ) { Text(text) }
}
