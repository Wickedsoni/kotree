package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.gen.CanopyDots
import dev.kotree.site.tree.gen.canopyDots
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val PERCENT = 100.0
private const val TWO_PI = 2.0 * PI
private const val CHANNEL_MAX = 255.0

private fun newPath(): dynamic = js("new Path2D()")

private fun rgbaCss(rgb: Int, percent: Int): String =
    "rgba(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF}, ${percent / PERCENT})"

private fun mixChannel(a: Int, b: Int, t: Double): Int = (a + (b - a) * t).roundToInt().coerceIn(0, CHANNEL_MAX.toInt())

private fun mixRgb(a: Int, b: Int, t: Double): Int =
    (mixChannel((a shr 16) and 0xFF, (b shr 16) and 0xFF, t) shl 16) or
        (mixChannel((a shr 8) and 0xFF, (b shr 8) and 0xFF, t) shl 8) or
        mixChannel(a and 0xFF, b and 0xFF, t)

/** Smoothstep of the clamped [x]. */
internal fun smooth01(x: Double): Double {
    val t = x.coerceIn(0.0, 1.0)
    return t * t * (3.0 - 2.0 * t)
}

/** Opacity of the dot canopy at zoom [z]: 1 when zoomed out, 0 from fadeEndZ. A function of z only, so no timer. */
internal fun canopyAlpha(z: Double): Double =
    1.0 - smooth01((z - Tokens.Canopy.fadeStartZ) / (Tokens.Canopy.fadeEndZ - Tokens.Canopy.fadeStartZ))

/** Leaf opacity bucket 0..leafAlphaBuckets at zoom [z] (leaves fade in as the dots fade out). */
internal fun leafAlphaBucket(z: Double): Int {
    val a = smooth01((z - Tokens.Canopy.leafFadeStartZ) / (Tokens.Canopy.leafFadeEndZ - Tokens.Canopy.leafFadeStartZ))
    return (a * Tokens.Canopy.leafAlphaBuckets).roundToInt()
}

/**
 * The soft dot canopy (ADR 008 §Canopy). The deterministic buffer is rasterised once into an offscreen canvas covering
 * its bounds; a frame is one drawImage at the zoom-driven alpha. Rebuilt lazily after [invalidate].
 */
class CanopyLayer(geometry: TreeGeometry, seed: Long = Tokens.World.seed) {
    private val dots: CanopyDots = canopyDots(geometry, seed)
    private val canvas = document.createElement("canvas") as HTMLCanvasElement
    private var valid = false

    /** Fraction done per branch (index = [CanopyDots.branchIds]). */
    private val fraction = DoubleArray(dots.branchIds.size)

    private val greenCss = arrayOf(Tokens.Canopy.light, Tokens.Canopy.mid, Tokens.Canopy.deep)
        .map { rgbaCss(it, Tokens.Canopy.alphaPercent) }
    private val autumnCss = (0 until Tokens.Canopy.autumnTones).map { i ->
        val t = i.toDouble() / (Tokens.Canopy.autumnTones - 1)
        rgbaCss(mixRgb(Tokens.Autumn.yellow, Tokens.Autumn.orange, t), Tokens.Canopy.alphaPercent)
    }

    fun invalidate() {
        valid = false
    }

    /** Sets the fraction done of each branch (by id; missing = 0) and rebuilds the texture on the next frame that needs it. */
    fun setFractions(byBranchId: Map<String, Double>) {
        for (j in dots.branchIds.indices) fraction[j] = byBranchId[dots.branchIds[j]] ?: 0.0
        valid = false
    }

    private fun rebuild() {
        val b = dots.bounds
        val w = b.maxX - b.minX
        val h = b.maxY - b.minY
        if (dots.n == 0 || w <= 0.0 || h <= 0.0) {
            canvas.width = 1
            canvas.height = 1
            valid = true
            return
        }
        val k = min(Tokens.Canopy.texPxPerWorld, sqrt(Tokens.Canopy.texMaxArea / (w * h)))
        canvas.width = max(1, ceil(w * k).toInt())
        canvas.height = max(1, ceil(h * k).toInt())
        val buckets: Array<Any?> = Array(greenCss.size + autumnCss.size) { newPath() }
        for (i in 0 until dots.n) {
            val f = fraction[dots.branch[i]]
            val bucket = if (dots.turn[i] >= f) dots.shade[i]
            else greenCss.size + min(autumnCss.size - 1, (dots.warm[i] * autumnCss.size).toInt())
            val p: dynamic = buckets[bucket]
            p.moveTo(dots.x[i] + dots.r[i], dots.y[i])
            p.arc(dots.x[i], dots.y[i], dots.r[i], 0.0, TWO_PI)
        }
        val c: dynamic = canvas.getContext("2d")
        c.setTransform(k, 0.0, 0.0, k, -b.minX * k, -b.minY * k)
        for (j in buckets.indices) {
            c.fillStyle = if (j < greenCss.size) greenCss[j] else autumnCss[j - greenCss.size]
            c.fill(buckets[j])
        }
        valid = true
    }

    /**
     * Draws the canopy at zoom [z] into [g] (device-pixel-ratio transform on entry; left in the same state on exit).
     * ([cx], [cy]) is the camera centre, [s] CSS px per world unit, viewport [vw] x [vh] CSS px.
     */
    fun draw(g: dynamic, z: Double, dpr: Double, s: Double, cx: Double, cy: Double, vw: Int, vh: Int) {
        val alpha = canopyAlpha(z)
        if (alpha < Tokens.Canopy.minDrawAlpha || dots.n == 0) return
        if (!valid) rebuild()
        val b = dots.bounds
        val left = cx - vw / s / 2.0
        val top = cy - vh / s / 2.0
        if (b.maxX < left || b.minX > left + vw / s || b.maxY < top || b.minY > top + vh / s) return
        val sk = dpr * s
        g.setTransform(sk, 0.0, 0.0, sk, -left * sk, -top * sk)
        g.globalAlpha = alpha
        g.drawImage(canvas, b.minX, b.minY, b.maxX - b.minX, b.maxY - b.minY)
        g.globalAlpha = 1.0
        g.setTransform(dpr, 0.0, 0.0, dpr, 0.0, 0.0)
    }
}
