package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.model.Bough
import dev.kotree.site.tree.model.TreeData
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin

internal fun quantise(v: Double): Double = round(v * Tokens.Gen.QUANT) / Tokens.Gen.QUANT

private fun q(v: Vec) = Vec(quantise(v.x), quantise(v.y))

private fun bezier(a: Vec, b: Vec, c: Vec, d: Vec, t: Double): Vec {
    val u = 1.0 - t
    val w0 = u * u * u
    val w1 = 3 * u * u * t
    val w2 = 3 * u * t * t
    val w3 = t * t * t
    return Vec(
        w0 * a.x + w1 * b.x + w2 * c.x + w3 * d.x,
        w0 * a.y + w1 * b.y + w2 * c.y + w3 * d.y,
    )
}

private fun sampled(a: Vec, b: Vec, c: Vec, d: Vec, n: Int): List<Vec> =
    List(n) { i -> q(bezier(a, b, c, d, i.toDouble() / (n - 1))) }

private fun at(v: Vec, dir: Vec, dist: Double) = Vec(v.x + dir.x * dist, v.y + dir.y * dist)

/** Unit direction for an angle in degrees measured from straight up, positive to the right. */
private fun up(deg: Double) = Vec(sin(deg * Tokens.Gen.DEG), -cos(deg * Tokens.Gen.DEG))

/** Unit direction for an angle in degrees measured from straight down, positive to the right. */
private fun down(deg: Double) = Vec(sin(deg * Tokens.Gen.DEG), cos(deg * Tokens.Gen.DEG))

/**
 * Pure, deterministic: the same [data] and [seed] always give the same geometry (D1).
 * Trunk, roots, the bough limbs and their branch limbs only; no stems or leaves yet.
 */
fun generate(data: TreeData, seed: Long): TreeGeometry {
    val w = Tokens.World.widthPx.toDouble()
    val h = Tokens.World.heightPx.toDouble()
    val cx = w / 2
    val base = Vec(cx, h * Tokens.Gen.TRUNK_BASE_Y)
    val top = Vec(cx, h * Tokens.Gen.TRUNK_TOP_Y)

    val tr = Rng(seed, "trunk")
    val t1 = Vec(cx + tr.jitter(Tokens.Gen.TRUNK_WOBBLE), base.y + (top.y - base.y) * 0.33)
    val t2 = Vec(cx + tr.jitter(Tokens.Gen.TRUNK_WOBBLE), base.y + (top.y - base.y) * 0.66)
    val trunk = Polyline(sampled(base, t1, t2, top, Tokens.Gen.TRUNK_POINTS), Tokens.Gen.TRUNK_WIDTH, Tokens.Color.inkBark, Tokens.Gen.TRUNK_TOP_WIDTH)
    fun trunkAt(t: Double) = q(bezier(base, t1, t2, top, t))

    val roots = buildRoots(seed, base)

    val leafBoughs = data.boughs.filter { it.id != Tokens.Gen.FOUNDATION_ID }.sortedBy { it.order }
    val boughs = ArrayList<BoughGeo>(data.boughs.size)
    data.boughs.sortedBy { it.order }.forEach { b ->
        if (b.id == Tokens.Gen.FOUNDATION_ID) {
            boughs.add(foundation(seed, b, base))
        } else {
            val idx = leafBoughs.indexOf(b)
            val norm = if (leafBoughs.size > 1) 2.0 * idx / (leafBoughs.size - 1) - 1.0 else 0.0
            boughs.add(bough(seed, b, norm, trunkAt(1.0 - Tokens.Gen.FORK_LOW * abs(norm))))
        }
    }

    return GeneratedTree(boundsOf(trunk, roots, boughs), trunk, roots, boughs)
}

private fun buildRoots(seed: Long, base: Vec): List<Polyline> = List(Tokens.Gen.ROOT_COUNT) { i ->
    val r = Rng(seed, "root:$i")
    val side = if (i % 2 == 0) -1.0 else 1.0
    val frac = (i / 2).toDouble() / (Tokens.Gen.ROOT_COUNT / 2 - 1)
    val deg = side * (Tokens.Gen.ROOT_MIN_DEG + (Tokens.Gen.ROOT_MAX_DEG - Tokens.Gen.ROOT_MIN_DEG) * frac + r.jitter(6.0))
    val len = r.range(Tokens.Gen.ROOT_LEN_MIN, Tokens.Gen.ROOT_LEN_MAX)
    val dir = down(deg)
    val end = at(base, dir, len)
    val bend = Vec(-dir.y, dir.x) // perpendicular
    val c1 = at(at(base, dir, len * 0.3), bend, r.jitter(Tokens.Gen.BEND * 0.5))
    val c2 = at(at(base, dir, len * 0.7), bend, r.jitter(Tokens.Gen.BEND))
    val width = Tokens.Gen.ROOT_WIDTH_MAX - ((Tokens.Gen.ROOT_WIDTH_MAX - Tokens.Gen.ROOT_WIDTH_MIN) * abs(frac)).toInt()
    Polyline(sampled(base, c1, c2, end, Tokens.Gen.POINTS), width, Tokens.Color.inkBark, Tokens.Gen.ROOT_TIP_WIDTH)
}

private fun foundation(seed: Long, b: Bough, base: Vec): BoughGeo {
    val r = Rng(seed, "bough:${b.id}")
    val dir = down(r.jitter(4.0))
    val end = at(base, dir, Tokens.Gen.FOUNDATION_LEN)
    val c1 = at(base, dir, Tokens.Gen.FOUNDATION_LEN * 0.33)
    val c2 = at(base, dir, Tokens.Gen.FOUNDATION_LEN * 0.66)
    val centre = Polyline(sampled(base, c1, c2, end, Tokens.Gen.POINTS), Tokens.Gen.BOUGH_WIDTH, Tokens.Color.inkBark, Tokens.Gen.BOUGH_TIP_WIDTH)
    // One branch, "foundation": it never splits, so its full limb equals the centre path.
    val branches = b.branches.map {
        val tip = centre.points.last()
        BranchGeo(it.id, Polyline(centre.points, Tokens.Gen.BRANCH_WIDTH, Tokens.Color.inkOutline, Tokens.Gen.BRANCH_TIP_WIDTH), tip, q(at(tip, dir, Tokens.Gen.LABEL_GAP)))
    }
    return BoughGeo(b.id, base, centre, branches, q(at(end, dir, Tokens.Gen.LABEL_GAP)))
}

private fun bough(seed: Long, b: Bough, norm: Double, fork: Vec): BoughGeo {
    val r = Rng(seed, "bough:${b.id}")
    val deg = norm * Tokens.Gen.BOUGH_SPREAD_DEG + r.jitter(4.0)
    val dir = up(deg)
    val perp = Vec(-dir.y, dir.x) // to the right of dir
    val len = Tokens.Gen.BOUGH_LEN + r.jitter(Tokens.Gen.BOUGH_LEN_JITTER)
    val end = at(fork, dir, len)
    val bendSign = if (norm == 0.0) 1.0 else -sign(norm) // bend back toward the trunk then out
    val c1 = at(at(fork, dir, len * 0.35), perp, bendSign * r.range(0.2, 1.0) * Tokens.Gen.BEND)
    val c2 = at(at(fork, dir, len * 0.7), perp, bendSign * r.range(-0.3, 0.6) * Tokens.Gen.BEND)
    val centre = Polyline(sampled(fork, c1, c2, end, Tokens.Gen.POINTS), Tokens.Gen.BOUGH_WIDTH, Tokens.Color.inkBark, Tokens.Gen.BOUGH_TIP_WIDTH)

    val m = b.branches.size
    val branches = b.branches.sortedBy { it.order }.mapIndexed { j, br ->
        val br0 = Rng(seed, "branch:${br.id}")
        val n = if (m > 1) 2.0 * j / (m - 1) - 1.0 else 0.0
        val off = n * Tokens.Gen.BRANCH_SPREAD + br0.jitter(Tokens.Gen.BRANCH_SPREAD * 0.08)
        val extra = br0.range(Tokens.Gen.BRANCH_EXTRA_MIN, Tokens.Gen.BRANCH_EXTRA_MAX)
        val curl = br0.jitter(Tokens.Gen.BRANCH_CURL)
        val pts = centre.points.mapIndexed { k, c ->
            val t = k.toDouble() / (Tokens.Gen.POINTS - 1)
            val sideways = off * t.pow(1.6) + curl * sin(t * PI)
            q(Vec(c.x + dir.x * extra * t + perp.x * sideways, c.y + dir.y * extra * t + perp.y * sideways))
        }
        val tip = pts.last()
        val prev = pts[pts.size - 2]
        val dx = tip.x - prev.x
        val dy = tip.y - prev.y
        val norm2 = kotlin.math.sqrt(dx * dx + dy * dy)
        val tangent = if (norm2 == 0.0) dir else Vec(dx / norm2, dy / norm2)
        BranchGeo(br.id, Polyline(pts, Tokens.Gen.BRANCH_WIDTH, Tokens.Color.inkOutline, Tokens.Gen.BRANCH_TIP_WIDTH), tip, q(at(tip, tangent, Tokens.Gen.LABEL_GAP)))
    }
    return BoughGeo(b.id, fork, centre, branches, q(at(end, dir, Tokens.Gen.LABEL_GAP)))
}

private fun sign(v: Double) = if (v < 0) -1.0 else 1.0

private fun boundsOf(trunk: Polyline, roots: List<Polyline>, boughs: List<BoughGeo>): Bounds {
    var x0 = Double.MAX_VALUE
    var y0 = Double.MAX_VALUE
    var x1 = -Double.MAX_VALUE
    var y1 = -Double.MAX_VALUE
    fun add(p: Polyline) {
        val m = p.maxWidthPx / 2.0
        for (v in p.points) {
            x0 = min(x0, v.x - m); y0 = min(y0, v.y - m)
            x1 = max(x1, v.x + m); y1 = max(y1, v.y + m)
        }
    }
    add(trunk)
    roots.forEach(::add)
    for (b in boughs) {
        add(b.centre)
        b.branches.forEach { add(it.full) }
    }
    val pad = Tokens.Gen.LABEL_PAD
    fun label(v: Vec) {
        x0 = min(x0, v.x - pad); y0 = min(y0, v.y - pad)
        x1 = max(x1, v.x + pad); y1 = max(y1, v.y + pad)
    }
    for (b in boughs) {
        label(b.labelAnchor)
        b.branches.forEach { label(it.labelAnchor) }
    }
    return Bounds(x0, y0, x1, y1)
}
