package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Viewport
import kotlinx.browser.window
import org.w3c.dom.AddEventListenerOptions
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.pointerevents.PointerEvent
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min

/**
 * Wires drag, wheel and keyboard to [camera] on [el]. Returns a function that removes every listener.
 * Keys: arrows pan, + and - zoom around the viewport centre, Home flies to the whole tree.
 * The element should have tabindex so it can take keyboard focus.
 */
fun attachCameraInput(el: HTMLElement, camera: Camera, fly: Fly): () -> Unit {
    val removers = mutableListOf<() -> Unit>()

    fun <E : Event> listen(type: String, options: AddEventListenerOptions? = null, handler: (E) -> Unit) {
        val cb: (Event) -> Unit = {
            @Suppress("UNCHECKED_CAST")
            handler(it as E)
        }
        if (options == null) el.addEventListener(type, cb) else el.addEventListener(type, cb, options)
        removers += { el.removeEventListener(type, cb) }
    }

    fun measure() {
        val r = el.getBoundingClientRect()
        camera.setViewport(Viewport(r.width.toInt(), r.height.toInt()))
    }
    measure()
    val onResize: (Event) -> Unit = { measure() }
    window.addEventListener("resize", onResize)
    removers += { window.removeEventListener("resize", onResize) }

    // Wheel zoom around the pointer. Not passive so the page does not scroll.
    listen<WheelEvent>("wheel", AddEventListenerOptions(passive = false)) { e ->
        e.preventDefault()
        fly.cancel()
        val unit = if (e.deltaMode == WheelEvent.DOM_DELTA_LINE) Tokens.Camera.wheelLineHeightPx else 1.0
        val r = el.getBoundingClientRect()
        camera.zoomAt(e.clientX - r.left, e.clientY - r.top, exp(-e.deltaY * unit * Tokens.Camera.wheelSensitivity))
    }

    // Drag pan with a small threshold so label clicks stay clicks.
    var pointerId: Int? = null
    var lastX = 0.0
    var lastY = 0.0
    var startX = 0.0
    var startY = 0.0
    var dragging = false

    listen<PointerEvent>("pointerdown") { e ->
        if (pointerId != null || e.button.toInt() != 0) return@listen
        pointerId = e.pointerId
        lastX = e.clientX.toDouble(); lastY = e.clientY.toDouble()
        startX = lastX; startY = lastY
        dragging = false
    }
    listen<PointerEvent>("pointermove") { e ->
        if (e.pointerId != pointerId) return@listen
        val x = e.clientX.toDouble()
        val y = e.clientY.toDouble()
        if (!dragging) {
            if (abs(x - startX) < Tokens.Camera.dragThresholdPx && abs(y - startY) < Tokens.Camera.dragThresholdPx) return@listen
            dragging = true
            fly.cancel()
            el.setPointerCapture(e.pointerId)
        }
        camera.panByScreen(x - lastX, y - lastY)
        lastX = x; lastY = y
    }
    fun endDrag(e: PointerEvent) {
        if (e.pointerId != pointerId) return
        if (dragging) {
            // The click that follows a drag must not activate a label.
            val swallow: (Event) -> Unit = { it.stopPropagation(); it.preventDefault() }
            el.addEventListener("click", swallow, true)
            window.setTimeout({ el.removeEventListener("click", swallow, true) }, 0)
        }
        pointerId = null
        dragging = false
    }
    listen<PointerEvent>("pointerup") { endDrag(it) }
    listen<PointerEvent>("pointercancel") { endDrag(it) }

    listen<KeyboardEvent>("keydown") { e ->
        if (e.ctrlKey || e.metaKey || e.altKey) return@listen
        val step = min(camera.viewport.widthPx, camera.viewport.heightPx) * Tokens.Camera.panStepFraction
        val handled = when (e.key) {
            "ArrowLeft" -> { fly.cancel(); camera.panByScreen(step, 0.0); true }
            "ArrowRight" -> { fly.cancel(); camera.panByScreen(-step, 0.0); true }
            "ArrowUp" -> { fly.cancel(); camera.panByScreen(0.0, step); true }
            "ArrowDown" -> { fly.cancel(); camera.panByScreen(0.0, -step); true }
            "+", "=" -> { fly.cancel(); camera.zoomAtCentre(Tokens.Camera.keyZoomFactor); true }
            "-", "_" -> { fly.cancel(); camera.zoomAtCentre(1.0 / Tokens.Camera.keyZoomFactor); true }
            "Home" -> { fly.to(camera.homeTarget()); true }
            else -> false
        }
        if (handled) e.preventDefault()
    }

    return { removers.forEach { it() }; removers.clear(); fly.cancel() }
}
