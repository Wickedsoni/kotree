package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import kotlinx.browser.window
import kotlin.math.pow

/**
 * Eased camera flight (docs/03 §2.5). With `prefers-reduced-motion: reduce` it is an instant cut
 * (docs/12 §2). The flight owns no timer: the frame loop calls [advance] once per frame and [kick] asks it for a frame,
 * so a flight and the pan or wheel input share one requestAnimationFrame (ADR 006).
 */
class Fly(private val camera: Camera) {
    /** Set by the frame loop; called when a flight starts so a frame is scheduled. */
    var kick: (() -> Unit)? = null

    var active: Boolean = false
        private set
    private var startMs = Double.NaN
    private var fromZ = 1.0
    private var fromX = 0.0
    private var fromY = 0.0
    private var toZ = 1.0
    private var toX = 0.0
    private var toY = 0.0

    fun to(t: FlyTarget) {
        cancel()
        if (prefersReducedMotion() || Tokens.Motion.flyDurationMs <= 0) {
            camera.set(t.z, t.centre.x, t.centre.y)
            return
        }
        fromZ = camera.z
        fromX = camera.cx
        fromY = camera.cy
        toZ = t.z
        toX = t.centre.x
        toY = t.centre.y
        startMs = Double.NaN
        active = true
        kick?.invoke()
    }

    fun cancel() {
        active = false
    }

    /** Moves the camera for the frame at [now] (rAF timestamp, ms). Returns true while the flight continues. */
    fun advance(now: Double): Boolean {
        if (!active) return false
        if (startMs.isNaN()) startMs = now
        val raw = ((now - startMs) / Tokens.Motion.flyDurationMs).coerceIn(0.0, 1.0)
        val e = ease(raw)
        // Zoom in log space so the apparent speed is even; centre moves linearly.
        val z = fromZ * (toZ / fromZ).pow(e)
        camera.set(z, fromX + (toX - fromX) * e, fromY + (toY - fromY) * e)
        if (raw >= 1.0) active = false
        return active
    }

    private fun ease(t: Double): Double =
        if (t < HALF) 4.0 * t * t * t else 1.0 - (-2.0 * t + 2.0).pow(3) / 2.0

    private companion object {
        const val HALF = 0.5
    }
}

fun prefersReducedMotion(): Boolean = window.matchMedia("(prefers-reduced-motion: reduce)").matches
