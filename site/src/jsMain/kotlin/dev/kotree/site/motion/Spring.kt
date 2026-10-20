package dev.kotree.site.motion

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.sqrt

/**
 * Damped spring, closed form (docs/02 §4.4). Compose spring parameters: damping ratio and stiffness, mass 1.
 * Position runs from 0 to 1 as time goes on; a spring move is `from + (to - from) * position(t)`.
 * Closed form means no integration error and no dependence on the frame rate.
 */
class Spring(private val dampingRatio: Double, private val stiffness: Double) {
    private val omega0 = sqrt(stiffness)

    /** Fraction of the move done after [seconds]. */
    fun position(seconds: Double): Double {
        val t = seconds
        val z = dampingRatio
        return if (z < 1.0) {
            val wd = omega0 * sqrt(1.0 - z * z)
            1.0 - exp(-z * omega0 * t) * (cos(wd * t) + (z * omega0 / wd) * sin(wd * t))
        } else {
            1.0 - exp(-omega0 * t) * (1.0 + omega0 * t)
        }
    }

    /** Seconds until the remaining distance is below [thresholdPx] for a move of [distancePx], searched at 1 ms steps. */
    fun settleSeconds(distancePx: Double, thresholdPx: Double, stepSeconds: Double = STEP_SECONDS, limitSeconds: Double = LIMIT_SECONDS): Double {
        var last = 0.0
        var t = 0.0
        while (t <= limitSeconds) {
            if (abs(1.0 - position(t)) * distancePx >= thresholdPx) last = t
            t += stepSeconds
        }
        return last + stepSeconds
    }

    /**
     * A CSS `linear()` easing of this spring and the duration it needs (docs/11 P0-12 fallback: CSS has no spring).
     * The duration is the settle time to within 0.5 percent of the move; the last point is exactly 1.
     */
    fun cssLinear(): Pair<Double, String> {
        val duration = settleSeconds(1.0, CSS_SETTLE_FRACTION)
        val points = (0..CSS_POINTS).joinToString(", ") { i ->
            val value = if (i == CSS_POINTS) 1.0 else position(duration * i / CSS_POINTS)
            (kotlin.math.round(value * CSS_ROUND) / CSS_ROUND).toString()
        }
        return duration to "linear($points)"
    }

    private companion object {
        const val CSS_SETTLE_FRACTION = 0.005
        const val CSS_POINTS = 24
        const val CSS_ROUND = 1000.0
        const val STEP_SECONDS = 0.001
        const val LIMIT_SECONDS = 5.0
    }
}
