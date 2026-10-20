package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.base
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement

private const val PROSE = "kt-prose"

/**
 * Prose: HTML made by the build from Markdown (raw HTML in the source was escaped there), set as innerHTML once when the
 * element enters the composition (ADR 005 section 2). Styled by the `.kt-prose` rules below, tokens only.
 */
@Composable
fun Prose(html: String, modifier: Modifier = Modifier) {
    Div(
        modifier.toAttrs {
            classes(PROSE)
            ref { element ->
                element.innerHTML = html
                markBoldLeads(element)
                onDispose { }
            }
        }
    )
}

private const val LEAD_PARAGRAPH = "kt-lead-p"
private const val LEAD_BOLD = "kt-lead-b"

/**
 * Generic: a paragraph that starts with a bold run followed by a line break and more text (the "**Question?**" then
 * answer shape used by `## Say it in an interview`) gets the bold run on its own line. Other paragraphs are untouched.
 */
private fun markBoldLeads(root: HTMLElement) {
    val paragraphs = root.querySelectorAll("p")
    for (i in 0 until paragraphs.length) {
        val p = paragraphs.item(i) as? HTMLElement ?: continue
        val first = p.firstChild ?: continue
        if (first.nodeName != "STRONG") continue
        val after = first.nextSibling ?: continue
        val breaks = after.nodeName == "BR" || (after.nodeName == "#text" && after.textContent.orEmpty().startsWith("\n"))
        val hasMore = after.nodeName == "BR" || after.textContent.orEmpty().isNotBlank()
        if (!breaks || !hasMore) continue
        p.classList.add(LEAD_PARAGRAPH)
        (first as? HTMLElement)?.classList?.add(LEAD_BOLD)
    }
}

/** Rules for HTML inside `.kt-prose`. Registered through Silk's stylesheet, like the global rules in Base.kt. */
@InitSilk
fun initProseStyles(ctx: InitSilkContext) {
    fun rule(selector: String, style: com.varabyte.kobweb.compose.ui.Modifier) {
        ctx.stylesheet.registerStyle(".$PROSE$selector") { base { style } }
    }
    rule("", Modifier.styleModifier {
        property("box-sizing", "border-box")
        property("max-width", layoutLessonTextVar.value())
        property("margin-left", "auto")
        property("margin-right", "auto")
        property("color", colorOnSurfaceVar.value())
        typo(Typo.Body)
    })
    rule(" h2", Modifier.styleModifier {
        property("margin", "${space8Var.value()} 0 ${space3Var.value()}")
        property("padding-top", space4Var.value())
        property("border-top", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
        typo(Typo.Title)
    })
    rule(" h3", Modifier.styleModifier {
        property("margin", "${space6Var.value()} 0 ${space2Var.value()}")
        typo(Typo.Body)
        property("font-weight", dev.kotree.site.typeTitleWeightVar.value())
    })
    rule(" p", Modifier.styleModifier { property("margin", "0 0 ${space3Var.value()}") })
    rule(" .$LEAD_PARAGRAPH", Modifier.styleModifier { property("margin", "0 0 ${space4Var.value()}") })
    rule(" .$LEAD_BOLD", Modifier.styleModifier {
        property("display", "block")
        property("margin", "0 0 ${space1Var.value()}")
    })
    rule(" .$LEAD_BOLD + br", Modifier.styleModifier { property("display", "none") })
    rule(" ul, .$PROSE ol", Modifier.styleModifier {
        property("margin", "0 0 ${space3Var.value()}")
        property("padding-left", space6Var.value())
    })
    rule(" li", Modifier.styleModifier { property("margin-bottom", space1Var.value()) })
    rule(" strong", Modifier.styleModifier { property("font-weight", dev.kotree.site.typeTitleWeightVar.value()) })
    rule(" a", Modifier.styleModifier {
        property("color", colorOnSurfaceVar.value())
        property("text-decoration", "underline")
    })
    rule(" code", Modifier.styleModifier { typo(Typo.Code) })
    rule(" pre", Modifier.styleModifier {
        property("margin", "0 0 ${space3Var.value()}")
        property("padding", "${space3Var.value()} ${space4Var.value()}")
        property("overflow", "auto")
        property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
        property("border-radius", dev.kotree.site.shapeMdVar.value())
    })
    rule(" table", Modifier.styleModifier {
        property("border-collapse", "collapse")
        property("margin", "0 0 ${space3Var.value()}")
        property("max-width", "100%")
        property("display", "block")
        property("overflow-x", "auto")
    })
    rule(" th, .$PROSE td", Modifier.styleModifier {
        property("padding", "${space2Var.value()} ${space3Var.value()}")
        property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
        property("text-align", "left")
        property("vertical-align", "top")
    })
    rule(" th", Modifier.styleModifier { property("font-weight", dev.kotree.site.typeTitleWeightVar.value()) })
    rule(" blockquote", Modifier.styleModifier {
        property("margin", "0 0 ${space3Var.value()}")
        property("padding-left", space4Var.value())
        property("border-left", "${Tokens.Chrome.focusRingWidthPx}px solid ${colorOutlineVar.value()}")
    })
}
