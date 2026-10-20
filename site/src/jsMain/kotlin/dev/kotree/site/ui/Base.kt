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
import dev.kotree.site.ui.topics.BENTO_HERO_CLASS
import dev.kotree.site.ui.topics.BENTO_WIDE_CLASS
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
    // Bento spans (ADR 006): wide and hero cards span columns only where at least two columns fit.
    val twoColumnsPx = 2 * Tokens.LayoutW.bentoColMin + Tokens.Spacing.s6
    val twoColumns = CSSMediaQuery.MediaFeature("min-width", StylePropertyValue("${twoColumnsPx}px"))
    ctx.stylesheet.registerStyle(".$BENTO_WIDE_CLASS") {
        base { Modifier }
        cssRule(twoColumns) { Modifier.styleModifier { property("grid-column", "span 2") } }
    }
    ctx.stylesheet.registerStyle(".$BENTO_HERO_CLASS") {
        base { Modifier }
        cssRule(twoColumns) {
            Modifier.styleModifier {
                property("grid-column", "span 2")
                property("grid-row", "span 2")
            }
        }
    }
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
