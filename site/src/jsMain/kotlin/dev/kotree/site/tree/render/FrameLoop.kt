package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.PerfProbe
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.camera.Fly
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.camera.ViewTracker
import kotlinx.browser.window
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.min

/**
 * The one requestAnimationFrame loop of the tree (ADR 006). Input handlers only record intent here (summed wheel delta, pan
 * delta, pointer position) and call [requestFrame], which schedules at most one frame. A frame applies the flight, the pan and
 * the smoothed wheel zoom, finds the hover node, updates the view state and draws. It keeps running only while a flight or the
 * wheel smoothing is unsettled, so an idle tree costs no frames (docs/07 §7). Nothing is allocated per frame: the camera keeps
 * plain doubles and the intents are fields.
 */
class FrameLoop(
    private val camera: Camera,
    private val fly: Fly,
    private val tracker: ViewTracker,
    private val renderer: VectorRenderer,
    private val onView: (ViewState) -> Unit,
) {
    private var scheduled = false
    private var inFrame = false
    private var disposed = false
    private var handle = 0
    private var lastNow = Double.NaN
    private var panDx = 0.0
    private var panDy = 0.0
    private var wheelRemaining = 0.0
    private var wheelX = 0.0
    private var wheelY = 0.0
    private var pointerX = -1.0
    private var pointerY = -1.0
    private var crisp = false
    private var crispTimer = -1
    private var lastView: ViewState? = null
    private val frameCallback: (Double) -> Unit = { now -> frame(now) }

    init {
        camera.onChange = { requestFrame() }
        fly.kick = { requestFrame() }
    }

    /** Schedules one frame unless one is pending or running (a running frame draws after its own changes). */
    fun requestFrame() {
        if (inFrame || scheduled || disposed) return
        scheduled = true
        handle = window.requestAnimationFrame(frameCallback)
    }

    /** Sum of wheel e-folds still to apply; the frame moves toward it with time constant camera.wheel.tau. */
    fun addWheel(eFolds: Double, x: Double, y: Double) {
        wheelRemaining += eFolds
        wheelX = x
        wheelY = y
        requestFrame()
    }

    fun addPan(dx: Double, dy: Double) {
        panDx += dx
        panDy += dy
        requestFrame()
    }

    /** Pointer in CSS px relative to the canvas, negative when it left. Asks for a frame only if the hover node changes. */
    fun setPointer(x: Double, y: Double) {
        pointerX = x
        pointerY = y
        val next = if (x < 0.0 || y < 0.0) null else renderer.hitNode(x, y)
        if (next !== renderer.hover) requestFrame()
    }

    /** Stops the loop and its timers. */
    fun dispose() {
        disposed = true
        window.cancelAnimationFrame(handle)
        if (crispTimer >= 0) window.clearTimeout(crispTimer)
        camera.onChange = null
        fly.kick = null
    }

    private fun frame(now: Double) {
        scheduled = false
        if (disposed) return
        inFrame = true
        PerfProbe.frame(now)
        val dt = if (lastNow.isNaN()) FIRST_FRAME_MS else min(now - lastNow, MAX_FRAME_MS)
        lastNow = now
        var more = fly.advance(now)
        if (panDx != 0.0 || panDy != 0.0) {
            camera.panByScreen(panDx, panDy)
            panDx = 0.0
            panDy = 0.0
        }
        if (wheelRemaining != 0.0) {
            val step = if (abs(wheelRemaining) < WHEEL_EPS) wheelRemaining else wheelRemaining * (1.0 - exp(-dt / Tokens.Camera.wheelTauMs))
            wheelRemaining -= step
            val before = camera.z
            camera.zoomAt(wheelX, wheelY, exp(step))
            // At a zoom limit the camera does not move: drop what is left.
            if (camera.z == before) wheelRemaining = 0.0
            if (wheelRemaining != 0.0) more = true
        }
        val view = tracker.update(camera.z, camera.cx, camera.cy)
        if (view !== lastView) {
            lastView = view
            onView(view)
        }
        val wasCrisp = crisp
        crisp = false
        renderer.draw(camera, view, wasCrisp, pointerX, pointerY)
        inFrame = false
        if (more) {
            requestFrame()
        } else {
            lastNow = Double.NaN
            PerfProbe.idle()
            if (!wasCrisp) armCrisp()
        }
    }

    /** tree.idle.crisp ms after motion stops, draw one frame that makes the cached layer exact. */
    private fun armCrisp() {
        if (crispTimer >= 0) window.clearTimeout(crispTimer)
        crispTimer = window.setTimeout({
            crispTimer = -1
            crisp = true
            requestFrame()
        }, Tokens.Tree.idleCrispMs)
    }

    private companion object {
        /** A frame after a pause assumes a typical 60 Hz step. */
        const val FIRST_FRAME_MS = 16.0
        /** Longest step the smoothing sees, so a stalled tab does not jump the zoom. */
        const val MAX_FRAME_MS = 50.0
        /** Wheel e-folds below which the remainder is applied at once. */
        const val WHEEL_EPS = 1e-4
    }
}
