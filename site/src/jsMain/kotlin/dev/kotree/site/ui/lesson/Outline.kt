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
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space1Var
import dev.kotree.site.space3Var
import dev.kotree.site.typeBodySmallLineVar
import dev.kotree.site.typeBodySmallSizeVar
import dev.kotree.site.typeBodySmallWeightVar
import dev.kotree.site.ui.core.Skeleton
import dev.kotree.site.ui.core.SkeletonLine
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

private class OutlineItem(val id: String, val text: String)

private const val SECTION_ID_PREFIX = "sec-"

/** Read the `h2` elements of the rendered lesson body. Gives each one an id `sec-<n>` when it has none. */
private fun scanHeadings(): List<OutlineItem> {
    val body = document.getElementById(LESSON_BODY_ID) ?: return emptyList()
    val nodes = body.querySelectorAll("h2")
    val out = ArrayList<OutlineItem>()
    for (i in 0 until nodes.length) {
        val el = nodes.item(i) as? HTMLElement ?: continue
        if (el.id.isEmpty()) el.id = "$SECTION_ID_PREFIX${i + 1}"
        out.add(OutlineItem(el.id, el.textContent.orEmpty()))
    }
    return out
}

/** The id of the last heading whose top has passed the upper part of the viewport. */
private fun currentHeading(items: List<OutlineItem>): String {
    val line = window.innerHeight / 3.0
    var current = items.firstOrNull()?.id.orEmpty()
    for (item in items) {
        val el = document.getElementById(item.id) ?: continue
        if (el.getBoundingClientRect().top <= line) current = item.id
    }
    return current
}

private fun reducedMotion(): Boolean = window.matchMedia("(prefers-reduced-motion: reduce)").matches

/** Smooth scroll to the heading (a jump under reduced motion), then move keyboard focus to it (ADR 007). */
private fun scrollToHeading(id: String) {
    val el = document.getElementById(id) ?: return
    val options: dynamic = js("({})")
    options.behavior = if (reducedMotion()) "auto" else "smooth"
    options.block = "start"
    el.asDynamic().scrollIntoView(options)
    el.setAttribute("tabindex", "-1")
    val focusOptions: dynamic = js("({})")
    focusOptions.preventScroll = true
    el.asDynamic().focus(focusOptions)
}

/**
 * "On this page" list (ADR 006, 007): the `h2` headings of the lesson body as links, the current one marked with
 * `aria-current`. It scans after the body has rendered ([ready] true) and follows the scroll position. Until then it
 * shows three skeleton rows, so the rail does not jump (the static export and the first client frame match). A click scrolls, moves focus to the heading and calls [onNavigate].
 */
@Composable
internal fun Outline(
    lessonId: String,
    ready: Boolean,
    branch: String,
    onNavigate: () -> Unit = {},
) {
    var items by remember(lessonId) { mutableStateOf(emptyList<OutlineItem>()) }
    var current by remember(lessonId) { mutableStateOf("") }
    var scanned by remember(lessonId) { mutableStateOf(false) }

    DisposableEffect(lessonId, ready) {
        if (!ready) return@DisposableEffect onDispose { }
        var frame = 0
        var scheduled = false
        var found = emptyList<OutlineItem>()
        val onScroll: (Event) -> Unit = {
            if (!scheduled) {
                scheduled = true
                frame = window.requestAnimationFrame {
                    scheduled = false
                    current = currentHeading(found)
                }
            }
        }
        // The prose sets its innerHTML when it enters the composition, so scan once the browser is back in its loop.
        val timer = window.setTimeout({
            found = scanHeadings()
            items = found
            current = currentHeading(found)
            scanned = true
        }, 0)
        window.addEventListener("scroll", onScroll)
        onDispose {
            window.clearTimeout(timer)
            window.removeEventListener("scroll", onScroll)
            if (scheduled) window.cancelAnimationFrame(frame)
        }
    }

    if (items.isEmpty()) {
        if (!scanned) Skeleton(3, SkeletonLine.Row)
        return
    }
    Nav(Modifier.attrsModifier { attr("aria-label", "On this page") }.toAttrs()) {
        Ul(
            Modifier.styleModifier {
                property("margin", "0")
                property("padding", "0")
                property("list-style", "none")
            }.toAttrs()
        ) {
            items.forEach { item ->
                Li {
                    OutlineLink(item, item.id == current, branch) {
                        scrollToHeading(item.id)
                        current = item.id
                        onNavigate()
                    }
                }
            }
        }
    }
}

@Composable
private fun OutlineLink(item: OutlineItem, isCurrent: Boolean, branch: String, onSelect: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }
    val color = if (isCurrent) branchInk(branch).value().toString() else colorOnSurfaceVar.value().toString()
    val percent = if (hovered) Tokens.StateLayer.hoverPercent else 0
    val layer = "color-mix(in srgb, $color $percent%, transparent)"
    A(
        "#${item.id}",
        Modifier.styleModifier {
            property("display", "flex")
            property("align-items", "center")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("padding", "${space1Var.value()} ${space3Var.value()}")
            property("border-radius", shapeMdVar.value())
            property("background-color", if (isCurrent) branchTint(branch).value().toString() else "transparent")
            property("background-image", "linear-gradient($layer, $layer)")
            property("color", color)
            property("text-decoration", "none")
            property("font-family", fontUiVar.value())
            property("font-size", typeBodySmallSizeVar.value())
            property("line-height", typeBodySmallLineVar.value())
            property("font-weight", typeBodySmallWeightVar.value())
        }.toAttrs {
            attr("title", item.text)
            if (isCurrent) attr("aria-current", "location")
            onMouseEnter { hovered = true }
            onMouseLeave { hovered = false }
            onClick {
                it.preventDefault()
                onSelect()
            }
        }
    ) { Text(item.text) }
}
