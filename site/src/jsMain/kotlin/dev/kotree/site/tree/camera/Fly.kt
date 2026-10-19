package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Vec
import kotlinx.browser.window
import kotlin.math.pow

/**
 * Eased camera flight (docs/03 §2.5). With `prefers-reduced-motion: reduce` it is an instant cut
 * (docs/12 §2). Frames run only while a flight is active.
 */
class Fly(private val camera: Camera) {
    private var handle: Int? = null
    private var startMs = Double.NaN
    private var fromZ = 1.0
    private var fromCentre = Vec(0.0, 0.0)
    private var target = FlyTarget(1.0, Vec(0.0, 0.0))

    val active: Boolean get() = handle != null

    fun to(t: FlyTarget) {
        cancel()
        if (prefersReducedMotion() || Tokens.Motion.flyDurationMs <= 0) {
            camera.set(t.z, t.centre)
            return
        }
        fromZ = camera.z
        fromCentre = camera.centre
        target = t
        startMs = Double.NaN
        handle = window.requestAnimationFrame { tick(it) }
    }

    fun cancel() {
        handle?.let { window.cancelAnimationFrame(it) }
        handle = null
    }

    private fun tick(now: Double) {
        if (startMs.isNaN()) startMs = now
        val raw = ((now - startMs) / Tokens.Motion.flyDurationMs).coerceIn(0.0, 1.0)
        val e = ease(raw)
        // Zoom in log space so the apparent speed is even; centre moves linearly.
        val z = fromZ * (target.z / fromZ).pow(e)
        camera.set(
            z,
            Vec(
                fromCentre.x + (target.centre.x - fromCentre.x) * e,
                fromCentre.y + (target.centre.y - fromCentre.y) * e,
            ),
        )
        handle = if (raw < 1.0) window.requestAnimationFrame { tick(it) } else null
    }

    private fun ease(t: Double): Double =
        if (t < HALF) 4.0 * t * t * t else 1.0 - (-2.0 * t + 2.0).pow(3) / 2.0

    private companion object {
        const val HALF = 0.5
    }
}

fun prefersReducedMotion(): Boolean = window.matchMedia("(prefers-reduced-motion: reduce)").matches
