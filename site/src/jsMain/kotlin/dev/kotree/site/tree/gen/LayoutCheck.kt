package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Viewport
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.TreeData
import kotlin.math.max
import kotlin.math.min

/** Float tolerance when comparing sizes in CSS px. */
private const val SIZE_EPS = 1e-6

/** Landing fit margin around a branch (docs/14 §4.1). */
private const val LANDING_MARGIN = 0.12

private class Box(val x0: Double, val y0: Double, val x1: Double, val y1: Double) {
    val w get() = x1 - x0
    val h get() = y1 - y0

    /** Smallest distance between the two boxes; negative when they overlap. */
    fun gapTo(o: Box): Double {
        val dx = max(o.x0 - x1, x0 - o.x1)
        val dy = max(o.y0 - y1, y0 - o.y1)
        return if (dx < 0 && dy < 0) max(dx, dy) else max(0.0, max(dx, dy))
    }
}

/**
 * Pure self-check of docs/14 §5 "layoutReport". For every branch, at its landing zoom and at z = [Tokens.Zoom.stemEnter]
 * (1280 x 800 by default), every stem label and leaf hit area must be at least [Tokens.Space.hitAreaPx]
 * square or wider, none may overlap another, and leaf hit areas keep a gap of
 * [Tokens.LayoutCheck.LEAF_GAP_MIN_PX] at the stem level. Returns the report lines; lines starting with "FAIL" are failures and the
 * last line is a summary. The caller logs them. Only live and up-next leaves are checked: shadow leaves are never drawn (D3).
 *
 * Label boxes are estimated from the title length; leaf hit boxes are max(hit area, leaf size at that zoom).
 */
fun layoutReport(
    g: TreeGeometry,
    data: TreeData,
    viewport: Viewport = Viewport(1280, 800),
    landingZ: ((BranchGeo) -> Double)? = null,
): List<String> {
    val lines = ArrayList<String>()
    var fails = 0
    val hit = Tokens.Space.hitAreaPx.toDouble()
    val base = min(
        viewport.widthPx / max(g.bounds.maxX - g.bounds.minX, 1.0),
        viewport.heightPx / max(g.bounds.maxY - g.bounds.minY, 1.0),
    )
    val titles = HashMap<String, String>()
    // Only live and up-next leaves are drawn and get a hit area (D3): shadow slots are not checked.
    val drawn = HashSet<String>()
    data.boughs.forEach { b ->
        b.branches.forEach { br ->
            br.stems.forEach { st ->
                titles[st.id] = st.title
                st.leaves.forEach { if (it.status != LeafStatus.SHADOW) drawn.add(it.id) }
            }
        }
    }

    fun fail(msg: String) { fails++; lines.add("FAIL $msg") }

    for (bough in g.boughs) for (br in bough.branches) {
        if (br.stems.isEmpty()) continue
        val fit = min(
            viewport.widthPx / max((br.bounds.maxX - br.bounds.minX) * (1 + 2 * LANDING_MARGIN), 1.0),
            viewport.heightPx / max((br.bounds.maxY - br.bounds.minY) * (1 + 2 * LANDING_MARGIN), 1.0),
        ) / base
        val land = landingZ?.invoke(br) ?: fit.coerceIn(Tokens.Zoom.boughFitMin, Tokens.Zoom.zMax)
        for ((tag, z) in listOf("landing" to land, "stem" to Tokens.Zoom.stemEnter)) {
            val s = base * z
            val labels = ArrayList<Pair<String, Box>>()
            val leaves = ArrayList<Pair<String, Box>>()
            for (st in br.stems) {
                val chars = titles[st.id]?.length ?: st.id.length
                val w = chars * Tokens.LayoutCheck.LABEL_CHAR_PX + 2 * Tokens.LayoutCheck.LABEL_PAD_PX
                val ax = st.labelAnchor.x * s
                val ay = st.labelAnchor.y * s
                val right = st.labelAnchor.x >= st.tip.x
                labels.add(st.id to if (right) Box(ax, ay - hit / 2, ax + w, ay + hit / 2) else Box(ax - w, ay - hit / 2, ax, ay + hit / 2))
                for (l in st.leaves) {
                    if (l.id !in drawn) continue
                    val half = max(hit, Tokens.Gen.LEAF_SIZE * s) / 2
                    leaves.add(l.id to Box(l.at.x * s - half, l.at.y * s - half, l.at.x * s + half, l.at.y * s + half))
                }
            }
            val before = fails
            labels.forEach { (id, b) -> if (b.w < hit - SIZE_EPS || b.h < hit - SIZE_EPS) fail("${br.id} z=$z $tag label $id ${b.w}x${b.h} < $hit") }
            for (i in labels.indices) for (j in i + 1 until labels.size) {
                if (labels[i].second.gapTo(labels[j].second) < 0) fail("${br.id} z=$z $tag labels overlap ${labels[i].first} / ${labels[j].first}")
            }
            for (i in leaves.indices) for (j in i + 1 until leaves.size) {
                val gap = leaves[i].second.gapTo(leaves[j].second)
                val need = if (tag == "stem") Tokens.LayoutCheck.LEAF_GAP_MIN_PX else 0.0
                if (gap < need) fail("${br.id} z=$z $tag leaf hit areas gap $gap < $need: ${leaves[i].first} / ${leaves[j].first}")
            }
            for ((lid, lb) in labels) for ((id, b) in leaves) {
                if (lb.gapTo(b) < 0) fail("${br.id} z=$z $tag label $lid overlaps leaf $id")
            }
            if (fails == before) lines.add("ok ${br.id} z=$z $tag (${br.stems.size} stems)")
        }
    }
    lines.add(if (fails == 0) "layoutReport: PASS" else "layoutReport: $fails failure(s)")
    return lines
}
