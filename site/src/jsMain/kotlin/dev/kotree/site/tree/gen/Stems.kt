package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.LeafGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.StemGeo
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.model.Branch
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/** Points per twig polyline. */
private const val TWIG_POINTS = 4

/**
 * Stem twigs and leaf slots for [branch] (docs/14 §4.2). Pure and deterministic:
 * Rng(seed, "stem:<id>") and Rng(seed, "leaf:<id>"). Stems attach along the limb at parameters
 * STEM_T_MIN..STEM_T_MAX in data order, alternating sides. Every leaf gets a slot (index inside the stem),
 * in a ceil(sqrt(n))-column grid at the twig tip with a fixed pitch.
 */
fun layoutStems(seed: Long, branch: Branch, geo: BranchGeo): List<StemGeo> {
    val stems = branch.stems.sortedBy { it.order }
    val n = stems.size
    if (n == 0) return emptyList()
    val limb = geo.full.points
    val cum = DoubleArray(limb.size)
    for (i in 1 until limb.size) cum[i] = cum[i - 1] + dist(limb[i - 1], limb[i])
    val total = cum.last()

    return stems.mapIndexed { i, s ->
        val r = Rng(seed, "stem:${s.id}")
        val t = if (n > 1) {
            Tokens.Gen.STEM_T_MIN + (Tokens.Gen.STEM_T_MAX - Tokens.Gen.STEM_T_MIN) * i / (n - 1)
        } else {
            (Tokens.Gen.STEM_T_MIN + Tokens.Gen.STEM_T_MAX) / 2
        }
        val (origin, tangent) = along(limb, cum, t * total)
        val side = if (i % 2 == 0) 1.0 else -1.0
        val theta = side * (Tokens.Gen.STEM_ANGLE_DEG + r.jitter(Tokens.Gen.STEM_ANGLE_JITTER_DEG)) * Tokens.Gen.DEG
        val dir = Vec(tangent.x * cos(theta) - tangent.y * sin(theta), tangent.x * sin(theta) + tangent.y * cos(theta))
        val perp = Vec(-dir.y, dir.x)
        // Same-side neighbours alternate long and short so their labels stack vertically instead of overlapping.
        val stagger = if ((i / 2) % 2 == 1) Tokens.Gen.STEM_LEN_STAGGER else 0.0
        val len = Tokens.Gen.STEM_LEN + stagger + r.jitter(Tokens.Gen.STEM_LEN_JITTER)
        val bend = r.jitter(len * Tokens.Gen.STEM_BEND)
        val pts = List(TWIG_POINTS) { k ->
            val f = k.toDouble() / (TWIG_POINTS - 1)
            val sag = bend * sin(f * PI)
            quantiseVec(Vec(origin.x + dir.x * len * f + perp.x * sag, origin.y + dir.y * len * f + perp.y * sag))
        }
        val tip = pts.last()
        val twig = Polyline(pts, Tokens.Gen.STEM_WIDTH, Tokens.Color.inkOutline, Tokens.Gen.STEM_TIP_WIDTH)
        // The label clears the leaf grid sideways: grid half width + leaf half + gap (layoutReport: label vs leaf).
        val cols = max(1, ceil(sqrt(s.leaves.size.toDouble())).toInt())
        val clear = (cols - 1) / 2.0 * Tokens.Gen.LEAF_PITCH + Tokens.Gen.LEAF_SIZE / 2.0 + Tokens.Gen.STEM_LABEL_GAP
        val anchor = quantiseVec(Vec(tip.x + (if (dir.x >= 0) clear else -clear), tip.y + dir.y * Tokens.Gen.STEM_LABEL_GAP))

        val leaves = leafSlots(seed, s.leaves.map { it.id }, tip, dir)
        StemGeo(s.id, twig, tip, anchor, leaves, stemBounds(twig, tip, leaves))
    }
}

private fun leafSlots(seed: Long, ids: List<String>, tip: Vec, dir: Vec): List<LeafGeo> {
    val cols = max(1, ceil(sqrt(ids.size.toDouble())).toInt())
    val pitch = Tokens.Gen.LEAF_PITCH
    val vSign = if (dir.y > 0) 1.0 else -1.0 // grow away from the limb's side of the screen
    return ids.mapIndexed { k, id ->
        val r = Rng(seed, "leaf:$id")
        val col = k % cols
        val row = k / cols
        val x = tip.x + (col - (cols - 1) / 2.0) * pitch + r.jitter(pitch * Tokens.Gen.LEAF_JITTER)
        val y = tip.y + vSign * (pitch / 2 + row * pitch) + r.jitter(pitch * Tokens.Gen.LEAF_JITTER)
        LeafGeo(id, quantiseVec(Vec(x, y)))
    }
}

private fun stemBounds(twig: Polyline, tip: Vec, leaves: List<LeafGeo>): Bounds {
    val m = twig.maxWidthPx / 2.0
    var x0 = tip.x - m
    var y0 = tip.y - m
    var x1 = tip.x + m
    var y1 = tip.y + m
    for (p in twig.points) {
        x0 = min(x0, p.x - m); y0 = min(y0, p.y - m)
        x1 = max(x1, p.x + m); y1 = max(y1, p.y + m)
    }
    val h = Tokens.Gen.LEAF_SIZE / 2.0
    for (l in leaves) {
        x0 = min(x0, l.at.x - h); y0 = min(y0, l.at.y - h)
        x1 = max(x1, l.at.x + h); y1 = max(y1, l.at.y + h)
    }
    return Bounds(x0, y0, x1, y1)
}

private fun quantiseVec(v: Vec) = Vec(quantise(v.x), quantise(v.y))

private fun dist(a: Vec, b: Vec): Double {
    val dx = b.x - a.x
    val dy = b.y - a.y
    return sqrt(dx * dx + dy * dy)
}

/** Point and unit tangent at arc length [s] along the polyline. */
private fun along(pts: List<Vec>, cum: DoubleArray, s: Double): Pair<Vec, Vec> {
    var i = 1
    while (i < pts.size - 1 && cum[i] < s) i++
    val a = pts[i - 1]
    val b = pts[i]
    val seg = max(cum[i] - cum[i - 1], 1e-9)
    val f = ((s - cum[i - 1]) / seg).coerceIn(0.0, 1.0)
    val d = dist(a, b)
    val tangent = if (d == 0.0) Vec(0.0, -1.0) else Vec((b.x - a.x) / d, (b.y - a.y) / d)
    return Pair(Vec(a.x + (b.x - a.x) * f, a.y + (b.y - a.y) * f), tangent)
}
