package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.render.FrameLoop
import org.w3c.dom.AddEventListenerOptions
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.pointerevents.PointerEvent
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min

/** What the input layer reports to the view: a click without a drag, Home, and Escape. */
class InputCallbacks(
    /** A press and release without a drag, in CSS px relative to the canvas. */
    val onClick: (Double, Double) -> Unit,
    val onHome: () -> Unit,
    /** Escape pressed with no card handling it. */
    val onEscape: () -> Unit,
)

/**
 * Wires drag, wheel, pinch and keyboard (ADR 006). Handlers only record intent in [loop] (summed wheel delta, pan delta,
 * pointer position) and the loop applies it once per frame. Pointer and wheel listen on [canvas]; keys on [container],
 * which should have a tabindex so it can take keyboard focus. Returns a function that removes every listener.
 * Keys: arrows pan, + and - zoom around the viewport centre, Home flies to the whole tree, Escape closes or zooms out.
 */
fun attachCameraInput(
    container: HTMLElement,
    canvas: HTMLElement,
    camera: Camera,
    fly: Fly,
    loop: FrameLoop,
    callbacks: InputCallbacks,
): () -> Unit {
    val removers = mutableListOf<() -> Unit>()

    fun <E : Event> listen(target: HTMLElement, type: String, options: AddEventListenerOptions? = null, handler: (E) -> Unit) {
        val cb: (Event) -> Unit = {
            @Suppress("UNCHECKED_CAST")
            handler(it as E)
        }
        if (options == null) target.addEventListener(type, cb) else target.addEventListener(type, cb, options)
        removers += { target.removeEventListener(type, cb) }
    }

    // Wheel zoom around the pointer. Not passive so the page does not scroll. A trackpad pinch arrives as a wheel event
    // with ctrlKey = true and is applied at once; a plain wheel is summed and smoothed by the loop.
    listen<WheelEvent>(canvas, "wheel", AddEventListenerOptions(passive = false)) { e ->
        e.preventDefault()
        fly.cancel()
        val unit = if (e.deltaMode == WheelEvent.DOM_DELTA_LINE) Tokens.Camera.wheelLineHeightPx else 1.0
        val r = canvas.getBoundingClientRect()
        val x = e.clientX - r.left
        val y = e.clientY - r.top
        if (e.ctrlKey) {
            camera.zoomAt(x, y, exp(-e.deltaY * unit * Tokens.Camera.pinchSensitivity))
        } else {
            loop.addWheel(-e.deltaY * unit * Tokens.Camera.wheelSensitivity, x, y)
        }
    }

    // Drag pan with a small threshold so clicks stay clicks. A click is a pointerup that never became a drag.
    var pointerId: Int? = null
    var lastX = 0.0
    var lastY = 0.0
    var startX = 0.0
    var startY = 0.0
    var dragging = false

    listen<PointerEvent>(canvas, "pointerdown") { e ->
        if (pointerId != null || e.button.toInt() != 0) return@listen
        pointerId = e.pointerId
        lastX = e.clientX.toDouble()
        lastY = e.clientY.toDouble()
        startX = lastX
        startY = lastY
        dragging = false
        canvas.setPointerCapture(e.pointerId)
    }
    listen<PointerEvent>(canvas, "pointermove") { e ->
        val r = canvas.getBoundingClientRect()
        val x = e.clientX.toDouble()
        val y = e.clientY.toDouble()
        if (pointerId == null) {
            loop.setPointer(x - r.left, y - r.top)
            return@listen
        }
        if (e.pointerId != pointerId) return@listen
        if (!dragging) {
            if (abs(x - startX) < Tokens.Camera.dragThresholdPx && abs(y - startY) < Tokens.Camera.dragThresholdPx) return@listen
            dragging = true
            fly.cancel()
        }
        loop.addPan(x - lastX, y - lastY)
        lastX = x
        lastY = y
    }
    fun endPointer(e: PointerEvent, click: Boolean) {
        if (e.pointerId != pointerId) return
        val wasDrag = dragging
        pointerId = null
        dragging = false
        if (click && !wasDrag) {
            val r = canvas.getBoundingClientRect()
            callbacks.onClick(e.clientX - r.left, e.clientY - r.top)
        }
    }
    listen<PointerEvent>(canvas, "pointerup") { endPointer(it, true) }
    listen<PointerEvent>(canvas, "pointercancel") { endPointer(it, false) }
    listen<PointerEvent>(canvas, "pointerleave") { loop.setPointer(-1.0, -1.0) }

    listen<KeyboardEvent>(container, "keydown") { e ->
        if (e.ctrlKey || e.metaKey || e.altKey) return@listen
        // Keys typed inside the node card belong to the card.
        val target: dynamic = e.target
        if (target != null && target.closest != null && target.closest(CARD_SELECTOR) != null) return@listen
        val step = min(camera.viewport.widthPx, camera.viewport.heightPx) * Tokens.Camera.panStepFraction
        val handled = when (e.key) {
            "ArrowLeft" -> { fly.cancel(); camera.panByScreen(step, 0.0); true }
            "ArrowRight" -> { fly.cancel(); camera.panByScreen(-step, 0.0); true }
            "ArrowUp" -> { fly.cancel(); camera.panByScreen(0.0, step); true }
            "ArrowDown" -> { fly.cancel(); camera.panByScreen(0.0, -step); true }
            "+", "=" -> { fly.cancel(); camera.zoomAtCentre(Tokens.Camera.keyZoomFactor); true }
            "-", "_" -> { fly.cancel(); camera.zoomAtCentre(1.0 / Tokens.Camera.keyZoomFactor); true }
            "Home" -> { callbacks.onHome(); true }
            "Escape" -> { callbacks.onEscape(); true }
            else -> false
        }
        if (handled) e.preventDefault()
    }

    return { removers.forEach { it() }; removers.clear(); fly.cancel() }
}

/** The node card marks itself with this attribute (NodeCard.kt). */
const val CARD_ATTRIBUTE = "data-kt-card"
private const val CARD_SELECTOR = "[$CARD_ATTRIBUTE]"
