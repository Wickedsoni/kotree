package dev.kotree.site.ui

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.outlineOffset
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import dev.kotree.site.colorFocusRingVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontCodeVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeBodyWeightVar
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

/** Body text of the app: font, size and colour from the token variables. Apply on the root element (variables are set there). */
fun Modifier.baseTypography(): Modifier = this
    .fontFamily(fontUiVar.value())
    .fontSize(typeBodySizeVar.value())
    .lineHeight(typeBodyLineVar.value())
    .styleModifier { property("font-weight", typeBodyWeightVar.value()) }
    .color(colorOnSurfaceVar.value())

/**
 * Global rules (docs/13 section 2): code font with ligatures off, the keyboard focus ring, and reduced motion.
 * Registered through Silk's stylesheet in an @InitSilk method, the documented route for global CSS.
 */
@InitSilk
fun initBaseStyles(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyle("code, pre, kbd, samp") {
        base {
            Modifier
                .fontFamily(fontCodeVar.value())
                .styleModifier { property("font-variant-ligatures", "none") }
        }
    }
    ctx.stylesheet.registerStyle("*:focus-visible") {
        base {
            Modifier
                .outline(Tokens.Chrome.focusRingWidthPx.px, LineStyle.Solid, colorFocusRingVar.value())
                .outlineOffset(Tokens.Chrome.focusRingOffsetPx.px)
        }
    }
    registerScrollbarStyles(ctx)
    registerPageRules(ctx)
    ctx.stylesheet.registerStyle("*") {
        base { Modifier }
        cssRule(CSSMediaQuery.MediaFeature("prefers-reduced-motion", StylePropertyValue("reduce"))) {
            Modifier.styleModifier {
                property("transition-duration", "0s !important")
                property("animation-duration", "0s !important")
                property("animation-iteration-count", "1 !important")
                property("scroll-behavior", "auto !important")
            }
        }
    }
}

/** Class for counts: tabular figures so numbers do not jitter (ADR 007). */
const val NUM_CLASS = "kt-num"

private fun rgbCss(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

/**
 * True when the engine understands `::-webkit-scrollbar` (Chrome, Edge, Safari). Chrome 121+ drops the pill shape when
 * the standard `scrollbar-width` / `scrollbar-color` are also set, so those are only written where this is false (Firefox).
 */
private fun supportsWebkitScrollbar(): Boolean {
    val supported: dynamic = js("typeof CSS !== 'undefined' && typeof CSS.supports === 'function' && CSS.supports('selector(::-webkit-scrollbar)')")
    return supported == true
}

/**
 * Slim pill scrollbars for the page and every scroller (ADR 007). Colours come from Tokens.kt as plain rgb values, not
 * from CSS variables: the viewport scrollbar belongs to `html`, where the token variables (set on the app root) do not exist.
 */
private fun registerScrollbarStyles(ctx: InitSilkContext) {
    val size = "${Tokens.Scrollbar.sizePx}px"
    ctx.stylesheet.registerStyle("::-webkit-scrollbar") {
        base { Modifier.styleModifier { property("width", size); property("height", size) } }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-track") {
        base { Modifier.styleModifier { property("background-color", "transparent") } }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-corner") {
        base { Modifier.styleModifier { property("background-color", "transparent") } }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-button") {
        base { Modifier.styleModifier { property("display", "none") } }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-thumb") {
        base {
            Modifier.styleModifier {
                property("background-color", rgbCss(Tokens.Scrollbar.thumb))
                property("border", "${Tokens.Scrollbar.insetPx}px solid transparent")
                property("background-clip", "padding-box")
                property("border-radius", "${Tokens.Shape.full}px")
                property("min-height", "${Tokens.Scrollbar.thumbMinPx}px")
                property("min-width", "${Tokens.Scrollbar.thumbMinPx}px")
            }
        }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-thumb:hover") {
        base {
            Modifier.styleModifier {
                property("background-color", rgbCss(Tokens.Scrollbar.thumbHover))
                property("border-width", "${Tokens.Scrollbar.insetHoverPx}px")
            }
        }
    }
    ctx.stylesheet.registerStyle("::-webkit-scrollbar-thumb:active") {
        base { Modifier.styleModifier { property("background-color", rgbCss(Tokens.Scrollbar.thumbActive)) } }
    }
    if (!supportsWebkitScrollbar()) {
        // Firefox: the standard properties. scrollbar-color inherits, scrollbar-width does not, so both go on every element.
        ctx.stylesheet.registerStyle(":root, :root *") {
            base {
                Modifier.styleModifier {
                    property("scrollbar-width", "thin")
                    property("scrollbar-color", "${rgbCss(Tokens.Scrollbar.thumb)} transparent")
                }
            }
        }
    }
}

/** Page-level rules: header clearance for hash targets, balanced headings, tabular counts, pointer cursors (ADR 007). */
private fun registerPageRules(ctx: InitSilkContext) {
    // html has no token variables, so the clearance is computed from Tokens (header height + space.4).
    ctx.stylesheet.registerStyle("html") {
        base {
            Modifier.styleModifier {
                property("scroll-padding-top", "${Tokens.LayoutW.headerHeight + Tokens.Spacing.s4}px")
            }
        }
    }
    ctx.stylesheet.registerStyle("h1, h2, h3") {
        base { Modifier.styleModifier { property("text-wrap", "balance") } }
    }
    ctx.stylesheet.registerStyle(".$NUM_CLASS") {
        base { Modifier.styleModifier { property("font-variant-numeric", "tabular-nums") } }
    }
    ctx.stylesheet.registerStyle("a[href], button:not([disabled]), [role=button], [role=radio]") {
        base { Modifier.styleModifier { property("cursor", "pointer") } }
    }
    ctx.stylesheet.registerStyle("button:disabled, [aria-disabled=true]") {
        base { Modifier.styleModifier { property("cursor", "not-allowed") } }
    }
}
