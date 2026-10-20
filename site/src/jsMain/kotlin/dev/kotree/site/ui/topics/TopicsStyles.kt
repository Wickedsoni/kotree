package dev.kotree.site.ui.topics

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import dev.kotree.site.spaceGapGridVar
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue

/** CSS classes of the topics page. Their rules are registered below, because a media query needs the stylesheet. */
const val GRID_CLASS = "kt-topics-grid"
const val INTRO_CLASS = "kt-topics-intro"
const val HERO_CLASS = "kt-topics-hero"
const val TILES_CLASS = "kt-topics-tiles"

/** Hero layout (ADR 007): left text 5 parts, right tiles 7 parts, from layout.bp.large. */
private const val HERO_COLUMNS = "5fr 7fr"

/** Foundation stem tiles per row: narrow and from layout.bp.large (ADR 007: 2 below, 3 from there). */
private const val TILE_COLS_NARROW = 2
private const val TILE_COLS_WIDE = 3

private fun columns(count: Int): String = "repeat($count, minmax(0, 1fr))"

/**
 * Topics page global rules (ADR 007). The branch grid has fixed 1 / 2 / 4 columns by media query, so with 16 cards
 * every row is full. Everything is mobile first: the base rule is the narrow layout.
 */
@InitSilk
fun initTopicsStyles(ctx: InitSilkContext) {
    val expanded = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpExpanded}px"))
    val large = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpLarge}px"))

    ctx.stylesheet.registerStyle(".$GRID_CLASS") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("grid-template-columns", columns(Tokens.LayoutW.topicsColsCompact))
                property("gap", spaceGapGridVar.value())
                property("align-items", "stretch")
            }
        }
        cssRule(expanded) {
            Modifier.styleModifier { property("grid-template-columns", columns(Tokens.LayoutW.topicsColsMedium)) }
        }
        cssRule(large) {
            Modifier.styleModifier { property("grid-template-columns", columns(Tokens.LayoutW.topicsColsWide)) }
        }
    }

    ctx.stylesheet.registerStyle(".$INTRO_CLASS") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("gap", space6Var.value())
            }
        }
        cssRule(large) {
            Modifier.styleModifier {
                property("grid-template-columns", "minmax(0, 1fr) auto")
                property("align-items", "end")
            }
        }
    }

    ctx.stylesheet.registerStyle(".$HERO_CLASS") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("gap", space6Var.value())
            }
        }
        cssRule(large) {
            Modifier.styleModifier {
                property("grid-template-columns", HERO_COLUMNS)
                property("gap", space8Var.value())
                property("align-items", "center")
            }
        }
    }

    ctx.stylesheet.registerStyle(".$TILES_CLASS") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("grid-template-columns", columns(TILE_COLS_NARROW))
                property("gap", Tokens.Spacing.s2.toString() + "px")
            }
        }
        cssRule(large) {
            Modifier.styleModifier { property("grid-template-columns", columns(TILE_COLS_WIDE)) }
        }
    }
}
