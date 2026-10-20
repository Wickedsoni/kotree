package dev.kotree.site.ui.lesson

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import dev.kotree.site.layoutHeaderHeightVar
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.layoutNavWidthVar
import dev.kotree.site.layoutRailWidthVar
import dev.kotree.site.space2Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue

/** CSS classes of the lesson page (ADR 007). The rules are below, because a media query needs the stylesheet. */
internal const val LESSON_GRID = "kt-lesson-grid"
internal const val LESSON_NAV = "kt-lesson-nav"
internal const val LESSON_RAIL = "kt-lesson-rail"
internal const val LESSON_MAIN = "kt-lesson-main"
/** Shown from layout.bp.large up. */
internal const val LESSON_LARGE_ONLY = "kt-lesson-large-only"
/** Shown below layout.bp.large only. */
internal const val LESSON_BELOW_LARGE = "kt-lesson-below-large"
/** Shown below layout.bp.xlarge only (the stem panel moves to the left column from there). */
internal const val LESSON_BELOW_XLARGE = "kt-lesson-below-xlarge"
/** Previous / next grid at the bottom, and the place of the next card in it. */
internal const val LESSON_PN = "kt-lesson-pn"
internal const val LESSON_PN_NEXT = "kt-lesson-pn-next"

/**
 * Three tracks by CSS media query, no script (ADR 007): below bp.large one track, from bp.large main + right rail,
 * from bp.xlarge stem navigation + main + right rail. Mobile first: the base rule is the narrow layout.
 */
@InitSilk
fun initLessonStyles(ctx: InitSilkContext) {
    val expanded = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpExpanded}px"))
    val large = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpLarge}px"))
    val xlarge = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${Tokens.LayoutW.bpXlarge}px"))

    // The sticky rails sit below the sticky header and scroll on their own when taller than the viewport.
    // Padding and an equal negative margin keep the focus ring from being clipped by overflow.
    val stickyTop = "calc(${layoutHeaderHeightVar.value()} + ${space6Var.value()})"
    val stickyMax = "calc(100vh - ${layoutHeaderHeightVar.value()} - ${space6Var.value()} - ${space6Var.value()})"

    ctx.stylesheet.registerStyle(".$LESSON_GRID") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("justify-content", "center")
                property("column-gap", space8Var.value())
                property("row-gap", space8Var.value())
                property("grid-template-columns", "minmax(0, ${layoutLessonWideVar.value()})")
            }
        }
        cssRule(large) {
            Modifier.styleModifier {
                property("grid-template-columns", "minmax(0, ${layoutLessonWideVar.value()}) ${layoutRailWidthVar.value()}")
            }
        }
        cssRule(xlarge) {
            Modifier.styleModifier {
                property(
                    "grid-template-columns",
                    "${layoutNavWidthVar.value()} minmax(0, ${layoutLessonWideVar.value()}) ${layoutRailWidthVar.value()}"
                )
            }
        }
    }

    ctx.stylesheet.registerStyle(".$LESSON_MAIN") {
        base { Modifier.styleModifier { property("min-width", "0") } }
    }

    ctx.stylesheet.registerStyle(".$LESSON_RAIL") {
        base {
            Modifier.styleModifier {
                property("box-sizing", "border-box")
                property("display", "flex")
                property("flex-direction", "column")
                property("gap", space4Var.value())
                property("min-width", "0")
            }
        }
        cssRule(large) {
            Modifier.styleModifier {
                property("position", "sticky")
                property("top", stickyTop)
                property("align-self", "start")
                property("max-height", stickyMax)
                property("overflow-y", "auto")
                property("padding", space2Var.value())
                property("margin", "calc(-1 * ${space2Var.value()})")
            }
        }
    }

    ctx.stylesheet.registerStyle(".$LESSON_NAV") {
        base { Modifier.styleModifier { property("display", "none") } }
        cssRule(xlarge) {
            Modifier.styleModifier {
                property("box-sizing", "border-box")
                property("display", "flex")
                property("flex-direction", "column")
                property("gap", space4Var.value())
                property("min-width", "0")
                property("position", "sticky")
                property("top", stickyTop)
                property("align-self", "start")
                property("max-height", stickyMax)
                property("overflow-y", "auto")
                property("padding", space2Var.value())
                property("margin", "calc(-1 * ${space2Var.value()})")
            }
        }
    }

    ctx.stylesheet.registerStyle(".$LESSON_LARGE_ONLY") {
        base { Modifier.styleModifier { property("display", "none") } }
        cssRule(large) { Modifier.styleModifier { property("display", "block") } }
    }

    ctx.stylesheet.registerStyle(".$LESSON_BELOW_LARGE") {
        base { Modifier.styleModifier { property("display", "block") } }
        cssRule(large) { Modifier.styleModifier { property("display", "none") } }
    }

    ctx.stylesheet.registerStyle(".$LESSON_BELOW_XLARGE") {
        base { Modifier.styleModifier { property("display", "block") } }
        cssRule(xlarge) { Modifier.styleModifier { property("display", "none") } }
    }

    ctx.stylesheet.registerStyle(".$LESSON_PN") {
        base {
            Modifier.styleModifier {
                property("display", "grid")
                property("grid-template-columns", "minmax(0, 1fr)")
                property("gap", space4Var.value())
            }
        }
        cssRule(expanded) {
            Modifier.styleModifier { property("grid-template-columns", "repeat(2, minmax(0, 1fr))") }
        }
    }

    ctx.stylesheet.registerStyle(".$LESSON_PN_NEXT") {
        base { Modifier }
        cssRule(expanded) { Modifier.styleModifier { property("grid-column", "2") } }
    }
}
