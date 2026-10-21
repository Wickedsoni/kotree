package dev.kotree.site.sims

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.events.Event

/*
 * Browser plumbing of the template host (ADR 009 section 4). Every listener here is passive and removed by the caller's
 * dispose, and nothing runs unless a sim is playing: the only timer in the sim system is the one setTimeout that
 * TemplateSimHost schedules while Play is on.
 */

/** Only one sim plays at a time on a page: starting one stops the previous one. */
object PagePlayback {
    private var stopActive: (() -> Unit)? = null
    private var owner: Any? = null

    /** [who] starts playing; the previous player is told to stop. */
    fun claim(who: Any, stop: () -> Unit) {
        if (owner !== who) stopActive?.invoke()
        owner = who
        stopActive = stop
    }

    fun release(who: Any) {
        if (owner === who) { owner = null; stopActive = null }
    }
}

private const val REDUCED_QUERY = "(prefers-reduced-motion: reduce)"

/** Watches `prefers-reduced-motion`; returns the function that removes the listener. */
fun watchReducedMotion(onChange: (Boolean) -> Unit): () -> Unit {
    val query = window.matchMedia(REDUCED_QUERY)
    onChange(query.matches)
    val listener: (Event) -> Unit = { onChange(query.matches) }
    query.addEventListener("change", listener)
    return { query.removeEventListener("change", listener) }
}

/** Calls [onHidden] when the tab goes to the background; returns the function that removes the listener. */
fun watchTabHidden(onHidden: () -> Unit): () -> Unit {
    val listener: (Event) -> Unit = { if (document.asDynamic().visibilityState == "hidden") onHidden() }
    document.addEventListener("visibilitychange", listener)
    return { document.removeEventListener("visibilitychange", listener) }
}

@JsName("IntersectionObserver")
private external class DomIntersectionObserver(callback: (Array<dynamic>, dynamic) -> Unit, options: dynamic) {
    fun observe(target: Element)
    fun disconnect()
}

/** Reports whether at least [share] of [element] is on screen; returns the function that disconnects. */
fun watchVisible(element: Element, share: Double, onChange: (Boolean) -> Unit): () -> Unit {
    val options = js("({})")
    options.threshold = share
    val observer = DomIntersectionObserver({ entries, _ ->
        if (entries.isNotEmpty()) onChange(entries[entries.size - 1].isIntersecting == true)
    }, options)
    observer.observe(element)
    return { observer.disconnect() }
}

/** Fades [element] in over [durationMs]. Uses the Web Animations API because the global reduced-motion rule zeroes CSS transitions. */
fun fadeIn(element: Element, durationMs: Int) {
    val keyframes = js("([{ opacity: 0 }, { opacity: 1 }])")
    val options = js("({})")
    options.duration = durationMs
    element.asDynamic().animate(keyframes, options)
}
