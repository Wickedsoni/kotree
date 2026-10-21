package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.gen.Rng
import kotlin.math.roundToInt

private const val HALF = 0.5
private const val CHANNEL_MAX = 255

/** Pure autumn colour maths (ADR 008 §Autumn). No DOM, so tests can run it on Node. */
internal object AutumnColor {
    /** CSS strings for every difficulty and LUT step, built once: nothing is allocated per frame. */
    class Lut(val fill: Array<Array<String>>, val stroke: Array<String>)

    private fun channel(a: Int, b: Int, t: Double): Int = (a + (b - a) * t).roundToInt().coerceIn(0, CHANNEL_MAX)

    /** Per-channel sRGB mix of [a] and [b] at [t], rounded (the formula of `Tokens.Branch.tint`). */
    fun mix(a: Int, b: Int, t: Double): Int =
        (channel((a shr 16) and 0xFF, (b shr 16) and 0xFF, t) shl 16) or
            (channel((a shr 8) and 0xFF, (b shr 8) and 0xFF, t) shl 8) or
            channel(a and 0xFF, b and 0xFF, t)

    /** Green [base] to yellow at t = 0.5 to orange at t = 1. */
    fun ramp(base: Int, t: Double): Int =
        if (t <= HALF) mix(base, Tokens.Autumn.yellow, t / HALF) else mix(Tokens.Autumn.yellow, Tokens.Autumn.orange, (t - HALF) / HALF)

    /** Outline of a done leaf: the live stroke towards the autumn stroke. */
    fun stroke(t: Double): Int = mix(Tokens.Status.live, Tokens.Autumn.stroke, t)

    /** Resting t of a leaf: amber to orange, never the pure yellow of the up-next glow. Deterministic per leaf id. */
    fun rest(seed: Long, leafId: String): Double =
        Tokens.Autumn.restMin + (1.0 - Tokens.Autumn.restMin) * Rng(seed, "autumn:$leafId").nextDouble()

    /** LUT index of [t]. */
    fun index(t: Double): Int = (t * (Tokens.Autumn.lutSteps - 1)).roundToInt().coerceIn(0, Tokens.Autumn.lutSteps - 1)

    /** Share of the live leaves of a branch that are done, 0 when it has none. */
    fun fraction(done: Int, live: Int): Double = if (live <= 0) 0.0 else done.toDouble() / live

    fun css(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

    fun buildLut(): Lut {
        val steps = Tokens.Autumn.lutSteps
        val bases = intArrayOf(Tokens.Leaf.easy, Tokens.Leaf.medium, Tokens.Leaf.hard)
        val fill = Array(bases.size) { d -> Array(steps) { k -> css(ramp(bases[d], k.toDouble() / (steps - 1))) } }
        val stroke = Array(steps) { k -> css(stroke(k.toDouble() / (steps - 1))) }
        return Lut(fill, stroke)
    }
}
