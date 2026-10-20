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
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Ol
import org.jetbrains.compose.web.dom.Text
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

private fun scrollToHeading(id: String) {
    val el = document.getElementById(id) ?: return
    val options: dynamic = js("({})")
    options.behavior = if (reducedMotion()) "auto" else "smooth"
    options.block = "start"
    el.asDynamic().scrollIntoView(options)
}

/**
 * "On this page" list (ADR 006): the `h2` headings of the lesson body as links, the current one marked with
 * `aria-current`. It scans after the body has rendered ([ready] true) and follows the scroll position.
 * Empty (and so nothing is drawn) until the headings exist, so the export snapshot has no outline.
 */
@Composable
internal fun Outline(lessonId: String, ready: Boolean, branch: String) {
    var items by remember(lessonId) { mutableStateOf(emptyList<OutlineItem>()) }
    var current by remember(lessonId) { mutableStateOf("") }

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
        }, 0)
        window.addEventListener("scroll", onScroll)
        onDispose {
            window.clearTimeout(timer)
            window.removeEventListener("scroll", onScroll)
            if (scheduled) window.cancelAnimationFrame(frame)
        }
    }

    if (items.isEmpty()) return
    Nav(Modifier.attrsModifier { attr("aria-label", "On this page") }.toAttrs()) {
        Ol(
            Modifier.styleModifier {
                property("margin", "0")
                property("padding", "0")
                property("list-style", "none")
            }.toAttrs()
        ) {
            items.forEach { item ->
                val isCurrent = item.id == current
                Li {
                    A(
                        "#${item.id}",
                        Modifier.styleModifier {
                            property("display", "flex")
                            property("align-items", "center")
                            property("box-sizing", "border-box")
                            property("min-height", Tokens.Spacing.minTargetPx.px)
                            property("padding", "0 ${space3Var.value()}")
                            property("margin-bottom", space2Var.value())
                            property("border-radius", shapeFullVar.value())
                            property("background-color", if (isCurrent) branchTint(branch).value().toString() else "transparent")
                            property("color", if (isCurrent) branchInk(branch).value().toString() else colorOnSurfaceVar.value().toString())
                            property("text-decoration", "none")
                            typo(Typo.Label)
                        }.toAttrs {
                            if (isCurrent) attr("aria-current", "location")
                            onClick {
                                it.preventDefault()
                                scrollToHeading(item.id)
                                current = item.id
                            }
                        }
                    ) { Text(item.text) }
                }
            }
        }
    }
}
