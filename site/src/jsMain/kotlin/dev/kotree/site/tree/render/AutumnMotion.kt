package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.gen.Rng
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

/**
 * Pure motion state of the autumn leaves (ADR 008 §Motion). No DOM: [reduced] is injected, so tests run it on Node.
 *
 * - [activity] A in [0, 1] rises while the camera moves and settles after it stops; it is 0 under reduced motion.
 * - Per leaf: a resting autumn value [rest], a sway/shimmer [phase], and an ease between 0 (green) and [rest] that runs over
 *   `Motion.autumnMs` when the leaf is marked or unmarked (0 ms under reduced motion).
 * - A leaf is in the live pass while it is done or easing. Leaves that are not done are never in it.
 * Everything is preallocated; a frame allocates nothing.
 */
internal class AutumnMotion(
    private val count: Int,
    seed: Long,
    ids: (Int) -> String,
    private val reduced: () -> Boolean,
) {
    val rest = DoubleArray(count) { AutumnColor.rest(seed, ids(it)) }
    val phase = DoubleArray(count) { TWO_PI * Rng(seed, "autumn-phase:${ids(it)}").nextDouble() }

    /** Activity envelope A. */
    var activity = 0.0
        private set

    /** Leaves the live pass drew in the last frame; set by the renderer. */
    var drawn = 0

    private val done = BooleanArray(count)
    private val easing = BooleanArray(count)
    private val easeFrom = DoubleArray(count)
    private val easeTo = DoubleArray(count)
    private val easeStart = DoubleArray(count)
    private var easingCount = 0

    fun isDone(i: Int): Boolean = done[i]

    /** In the live pass: done, or still easing back to green. */
    fun inLive(i: Int): Boolean = done[i] || easing[i]

    /** Eased colour value of leaf [i] at [now], before shimmer: 0 green .. [rest]. */
    fun base(i: Int, now: Double): Double {
        if (!easing[i]) return if (done[i]) rest[i] else 0.0
        val e = smooth((now - easeStart[i]) / Tokens.Motion.autumnMs)
        return easeFrom[i] + (easeTo[i] - easeFrom[i]) * e
    }

    /** Colour value with the zoom-linked shimmer: [wave] in [-1, 1] is sin(omega*now + phase). */
    fun shimmered(i: Int, now: Double, wave: Double): Double =
        (base(i, now) - activity * Tokens.Autumn.shimmer * (HALF + HALF * wave)).coerceIn(0.0, 1.0)

    /** Marks or unmarks leaf [i]; starts the ease from wherever the colour is now. No-op when nothing changes. */
    fun setDone(i: Int, value: Boolean, now: Double) {
        if (done[i] == value) return
        val from = base(i, now)
        done[i] = value
        val to = if (value) rest[i] else 0.0
        if (easing[i]) easingCount--
        if (reduced()) {
            easing[i] = false
            return
        }
        easing[i] = true
        easingCount++
        easeFrom[i] = from
        easeTo[i] = to
        easeStart[i] = now
    }

    /**
     * Advances the envelope and the eases to [now]. [moved] = the camera differs from the last frame. Returns true when a
     * leaf left the live pass (an undo ease reached green), so the caller must render the wood layer again.
     */
    fun step(now: Double, dt: Double, moved: Boolean): Boolean {
        val rm = reduced()
        activity = when {
            rm -> 0.0
            moved -> min(1.0, activity + dt / Tokens.Sway.riseMs)
            else -> max(0.0, activity - dt / Tokens.Sway.settleMs)
        }
        var left = false
        if (easingCount > 0) {
            for (i in 0 until count) {
                if (!easing[i]) continue
                if (rm || now - easeStart[i] >= Tokens.Motion.autumnMs) {
                    easing[i] = false
                    easingCount--
                    if (!done[i]) left = true
                }
            }
        }
        return left
    }

    /** A frame is still needed: the sway is visibly alive, or an ease is running. Idle means false. */
    fun active(): Boolean = easingCount > 0 || (activity > 0.0 && drawn > 0)

    private fun smooth(x: Double): Double {
        val t = x.coerceIn(0.0, 1.0)
        return t * t * (3.0 - 2.0 * t)
    }

    private companion object {
        const val TWO_PI = 2.0 * PI
        const val HALF = 0.5
    }
}
