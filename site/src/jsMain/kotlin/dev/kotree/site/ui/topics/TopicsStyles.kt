package dev.kotree.site.ui.topics

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue

/**
 * Topics page global rules. WP-A moved the two bento rules here unchanged from ui/Base.kt; WP-B owns this file
 * and replaces them with the even branch grid (ADR 007).
 */
@InitSilk
fun initTopicsStyles(ctx: InitSilkContext) {
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
}
