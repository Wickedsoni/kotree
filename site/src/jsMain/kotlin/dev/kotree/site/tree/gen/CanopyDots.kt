package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Precomputed canopy dots (ADR 008 §Canopy). Parallel arrays, no per-dot objects. [branch] indexes [branchIds];
 * [shade] is a green tone 0..2; a dot is autumn when [turn] < fraction done of its branch; [warm] 0 = yellow .. 1 = orange.
 */
class CanopyDots(
    val n: Int,
    val x: DoubleArray,
    val y: DoubleArray,
    val r: DoubleArray,
    val branch: IntArray,
    val shade: IntArray,
    val turn: DoubleArray,
    val warm: DoubleArray,
    val branchIds: Array<String>,
    val bounds: Bounds,
)

private const val TWO_PI = 2.0 * PI

/** Growing buffers of one build. */
private class Acc(cap: Int, val horizonY: Double) {
    var n = 0
    val x = DoubleArray(cap)
    val y = DoubleArray(cap)
    val r = DoubleArray(cap)
    val branch = IntArray(cap)
    val shade = IntArray(cap)
    val turn = DoubleArray(cap)
    val warm = DoubleArray(cap)

    /** Draws one dot in the fixed order; [fixedBranch] < 0 means the dot takes a random branch among [nBranches] from [firstBranch]. */
    fun dot(rng: Rng, anchors: List<Vec>, radius: Double, fixedBranch: Int, firstBranch: Int, nBranches: Int) {
        val a = anchors[(rng.nextDouble() * anchors.size).toInt().coerceAtMost(anchors.size - 1)]
        val u1 = rng.nextDouble()
        val u2 = rng.nextDouble()
        val rr = rng.range(Tokens.Canopy.dotRMin, Tokens.Canopy.dotRMax)
        val sh = (rng.nextDouble() * Tokens.Canopy.shades).toInt().coerceAtMost(Tokens.Canopy.shades - 1)
        val tu = rng.nextDouble()
        val wa = rng.nextDouble()
        val br = if (fixedBranch >= 0) fixedBranch
        else firstBranch + (rng.nextDouble() * nBranches).toInt().coerceAtMost(nBranches - 1)
        val rho = radius * sqrt(u1)
        val th = TWO_PI * u2
        val px = a.x + rho * cos(th)
        val py = a.y + rho * sin(th)
        if (py > horizonY) return // below the ground: skipped, draws already consumed
        x[n] = px; y[n] = py; r[n] = rr; branch[n] = br; shade[n] = sh; turn[n] = tu; warm[n] = wa
        n++
    }
}

/**
 * Builds the canopy buffer from new Rng streams only ("canopy-bough:<id>", "canopy:<id>"). Pure and deterministic.
 * Foundation (roots) gets no dots. Dots below the horizon (the trunk's first point) are dropped.
 */
fun canopyDots(g: TreeGeometry, seed: Long): CanopyDots {
    val horizonY = g.trunk.points.firstOrNull()?.y ?: g.bounds.maxY
    val bs = g.boughs.filter { it.id != Tokens.Gen.FOUNDATION_ID }
    val ids = ArrayList<String>()
    for (b in bs) for (br in b.branches) ids += br.id

    // Counts first, so a scale factor can be applied before any sampling.
    var total = 0
    for (b in bs) {
        total += Tokens.Canopy.dotsPerBough
        for (br in b.branches) total += Tokens.Canopy.dotsPerBranch + Tokens.Canopy.dotsPerStem * br.stems.size
    }
    val scale = if (total > Tokens.Canopy.maxDots) Tokens.Canopy.maxDots.toDouble() / total else 1.0
    fun scaled(c: Int): Int = if (scale < 1.0) (c * scale).toInt() else c

    val acc = Acc(minOf(total, Tokens.Canopy.maxDots), horizonY)
    var first = 0
    for (b in bs) {
        val pts = b.centre.points
        if (b.branches.isNotEmpty() && pts.isNotEmpty()) {
            val from = (pts.size * Tokens.Canopy.boughAnchorFrom).toInt().coerceIn(0, pts.size - 1)
            val anchors = pts.subList(from, pts.size)
            val rng = Rng(seed, "canopy-bough:${b.id}")
            repeat(scaled(Tokens.Canopy.dotsPerBough)) {
                acc.dot(rng, anchors, Tokens.Canopy.boughRadius.toDouble(), -1, first, b.branches.size)
            }
        }
        for ((j, br) in b.branches.withIndex()) {
            val limb = br.full.points
            val anchors = ArrayList<Vec>()
            for (i in limb.indices) if (i.toDouble() / (limb.size - 1).coerceAtLeast(1) >= Tokens.Canopy.limbAnchorFrom) anchors += limb[i]
            val rng = Rng(seed, "canopy:${br.id}")
            val count = scaled(Tokens.Canopy.dotsPerBranch + Tokens.Canopy.dotsPerStem * br.stems.size)
            if (anchors.isNotEmpty()) {
                // Limb anchors take the first part of the count and stem tips the rest, so tips always get dots.
                val limbCount = scaled(Tokens.Canopy.dotsPerBranch)
                repeat(minOf(limbCount, count)) { acc.dot(rng, anchors, Tokens.Canopy.limbRadius.toDouble(), first + j, 0, 0) }
                val tips = br.stems.map { it.tip }
                if (tips.isNotEmpty()) {
                    repeat(count - minOf(limbCount, count)) { acc.dot(rng, tips, Tokens.Canopy.stemRadius.toDouble(), first + j, 0, 0) }
                }
            }
        }
        first += b.branches.size
    }

    val n = acc.n
    var minX = Double.MAX_VALUE
    var minY = Double.MAX_VALUE
    var maxX = -Double.MAX_VALUE
    var maxY = -Double.MAX_VALUE
    for (i in 0 until n) {
        val rr = acc.r[i]
        if (acc.x[i] - rr < minX) minX = acc.x[i] - rr
        if (acc.y[i] - rr < minY) minY = acc.y[i] - rr
        if (acc.x[i] + rr > maxX) maxX = acc.x[i] + rr
        if (acc.y[i] + rr > maxY) maxY = acc.y[i] + rr
    }
    val bounds = if (n == 0) Bounds(0.0, 0.0, 0.0, 0.0) else Bounds(minX, minY, maxX, maxY)
    return CanopyDots(
        n, acc.x.copyOf(n), acc.y.copyOf(n), acc.r.copyOf(n), acc.branch.copyOf(n), acc.shade.copyOf(n),
        acc.turn.copyOf(n), acc.warm.copyOf(n), ids.toTypedArray(), bounds,
    )
}
