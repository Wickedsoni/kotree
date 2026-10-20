package dev.kotree.site.tree.hit

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.LeafGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.StemGeo
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.tree.model.TreeData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/** Node kinds, ordered by hit priority: a larger kind wins when several are under the pointer. */
object Kind {
    const val BOUGH = 0
    const val BRANCH = 1
    const val STEM = 2
    const val LEAF = 3
}

/** One thing on the tree that can be clicked, focused and labelled. Plain data, built once. */
class Node(
    val kind: Int,
    val id: String,
    val title: String,
    /** World position of the label anchor (leaf: the slot). */
    val ax: Double,
    val ay: Double,
    val boughId: String,
    /** Branch slug (docs/00 §5); empty for a bough. */
    val branchId: String,
    /** Index in the kind's own list ([TreeNodes.boughNodes] and so on). */
    val index: Int,
    val boughGeo: BoughGeo? = null,
    val branchGeo: BranchGeo? = null,
    val stemGeo: StemGeo? = null,
    val leafGeo: LeafGeo? = null,
    val branch: Branch? = null,
    val stem: Stem? = null,
    val leaf: Leaf? = null,
)

/** Every node of the tree in data order, plus flat arrays of the leaves for the renderer. */
class TreeNodes(geometry: TreeGeometry, data: TreeData) {
    val boughNodes = ArrayList<Node>()
    val branchNodes = ArrayList<Node>()
    val stemNodes = ArrayList<Node>()
    val leafNodes = ArrayList<Node>()
    val byId = HashMap<String, Node>()
    val branchesOfBough = HashMap<String, List<Node>>()
    val stemsOfBranch = HashMap<String, List<Node>>()
    val leavesOfStem = HashMap<String, List<Node>>()
    val branchIndex = HashMap<String, Int>()
    val stemIndex = HashMap<String, Int>()

    val leafCount: Int
    val leafX: DoubleArray
    val leafY: DoubleArray
    val leafCos: DoubleArray
    val leafSin: DoubleArray
    /** [dev.kotree.site.tree.model.LeafStatus] ordinal: SHADOW 0, UPNEXT 1, LIVE 2. */
    val leafStatus: IntArray
    /** [dev.kotree.site.tree.model.Difficulty] ordinal: EASY 0, MEDIUM 1, HARD 2. */
    val leafDifficulty: IntArray
    val leafBranchIdx: IntArray

    /** The Foundation branch: the roots bough label acts as its node from the Bough level up. */
    val foundation: Node?
        get() = byId[FOUNDATION_BRANCH]

    init {
        val boughGeos = geometry.boughs.associateBy { it.id }
        val branchGeos = geometry.boughs.flatMap { it.branches }.associateBy { it.id }
        val stemGeos = branchGeos.values.flatMap { it.stems }.associateBy { it.id }
        val leafGeos = stemGeos.values.flatMap { it.leaves }.associateBy { it.id }
        val lx = ArrayList<Double>()
        val ly = ArrayList<Double>()
        val lc = ArrayList<Double>()
        val ls = ArrayList<Double>()
        val lstatus = ArrayList<Int>()
        val ldiff = ArrayList<Int>()
        val lbranch = ArrayList<Int>()

        for (bough in data.boughs) {
            val bg = boughGeos[bough.id] ?: continue
            val boughNode = Node(Kind.BOUGH, bough.id, bough.title, bg.labelAnchor.x, bg.labelAnchor.y, bough.id, "", boughNodes.size, boughGeo = bg)
            boughNodes += boughNode
            byId[bough.id] = boughNode
            val branchList = ArrayList<Node>()
            for (branch in bough.branches) {
                val brg = branchGeos[branch.id] ?: continue
                val bIdx = branchNodes.size
                val branchNode = Node(
                    Kind.BRANCH, branch.id, branch.title, brg.labelAnchor.x, brg.labelAnchor.y, bough.id, branch.id, bIdx,
                    boughGeo = bg, branchGeo = brg, branch = branch,
                )
                branchNodes += branchNode
                branchList += branchNode
                byId[branch.id] = branchNode
                branchIndex[branch.id] = bIdx
                val stemList = ArrayList<Node>()
                for (stem in branch.stems) {
                    val sg = stemGeos[stem.id] ?: continue
                    val stemNode = Node(
                        Kind.STEM, stem.id, stem.title, sg.labelAnchor.x, sg.labelAnchor.y, bough.id, branch.id, stemNodes.size,
                        boughGeo = bg, branchGeo = brg, stemGeo = sg, branch = branch, stem = stem,
                    )
                    stemNodes += stemNode
                    stemList += stemNode
                    byId[stem.id] = stemNode
                    stemIndex[stem.id] = stemNode.index
                    val pts = sg.twig.points
                    val twigAngle = if (pts.size >= 2) {
                        atan2(pts[pts.size - 1].y - pts[pts.size - 2].y, pts[pts.size - 1].x - pts[pts.size - 2].x)
                    } else {
                        0.0
                    }
                    val leafList = ArrayList<Node>()
                    for ((i, leaf) in stem.leaves.withIndex()) {
                        val lg = leafGeos[leaf.id] ?: continue
                        val leafNode = Node(
                            Kind.LEAF, leaf.id, leaf.title, lg.at.x, lg.at.y, bough.id, branch.id, leafNodes.size,
                            boughGeo = bg, branchGeo = brg, stemGeo = sg, leafGeo = lg, branch = branch, stem = stem, leaf = leaf,
                        )
                        leafNodes += leafNode
                        leafList += leafNode
                        byId[leaf.id] = leafNode
                        val angle = twigAngle + ((i % LEAF_FAN_STEPS) - 1) * LEAF_FAN_RAD
                        lx += lg.at.x
                        ly += lg.at.y
                        lc += cos(angle)
                        ls += sin(angle)
                        lstatus += leaf.status.ordinal
                        ldiff += leaf.difficulty.ordinal
                        lbranch += bIdx
                    }
                    leavesOfStem[stem.id] = leafList
                }
                stemsOfBranch[branch.id] = stemList
            }
            branchesOfBough[bough.id] = branchList
        }
        leafCount = leafNodes.size
        leafX = lx.toDoubleArray()
        leafY = ly.toDoubleArray()
        leafCos = lc.toDoubleArray()
        leafSin = ls.toDoubleArray()
        leafStatus = lstatus.toIntArray()
        leafDifficulty = ldiff.toIntArray()
        leafBranchIdx = lbranch.toIntArray()
    }

    private companion object {
        const val FOUNDATION_BRANCH = "foundation"
        /** Leaves in a cluster fan out around the twig direction in three steps of this angle (radians). */
        const val LEAF_FAN_STEPS = 3
        const val LEAF_FAN_RAD = 0.45
    }
}

/**
 * Uniform grid over the world (cell = tree.hit.cell) of hit items: limb and twig segments and leaf points, kept in parallel
 * arrays (ADR 006). Built once per geometry. A query scans the 3 x 3 cells around the pointer, keeps the items of the
 * current level and returns the best one within the screen hit radius: larger [Kind] first, then the nearest. No allocation.
 */
class HitIndex(nodes: TreeNodes, geometry: TreeGeometry) {
    private val nodeIdx: Array<Node>
    private val kind: IntArray
    private val minLevel: IntArray
    private val maxLevel: IntArray
    private val x0: DoubleArray
    private val y0: DoubleArray
    private val x1: DoubleArray
    private val y1: DoubleArray
    private val count: Int

    private val originX = geometry.bounds.minX
    private val originY = geometry.bounds.minY
    private val cols = floor((geometry.bounds.maxX - geometry.bounds.minX) / Tokens.Tree.hitCell).toInt() + 1
    private val rows = floor((geometry.bounds.maxY - geometry.bounds.minY) / Tokens.Tree.hitCell).toInt() + 1
    private val cellStart: IntArray
    private val cellItems: IntArray

    init {
        val owners = ArrayList<Node>()
        val ks = ArrayList<Int>()
        val mins = ArrayList<Int>()
        val maxs = ArrayList<Int>()
        val ax = ArrayList<Double>()
        val ay = ArrayList<Double>()
        val bx = ArrayList<Double>()
        val by = ArrayList<Double>()

        fun addLine(line: Polyline, owner: Node, k: Int, lo: Int, hi: Int) {
            val pts: List<Vec> = line.points
            if (pts.size == 1) {
                owners += owner; ks += k; mins += lo; maxs += hi
                ax += pts[0].x; ay += pts[0].y; bx += pts[0].x; by += pts[0].y
            }
            for (i in 1 until pts.size) {
                owners += owner; ks += k; mins += lo; maxs += hi
                ax += pts[i - 1].x; ay += pts[i - 1].y; bx += pts[i].x; by += pts[i].y
            }
        }

        for (bough in nodes.boughNodes) {
            val bg = bough.boughGeo ?: continue
            addLine(bg.centre, bough, Kind.BOUGH, LEVEL_CANOPY, LEVEL_BOUGH)
            if (bough.id == ROOTS) for (root in geometry.roots) addLine(root, bough, Kind.BOUGH, LEVEL_CANOPY, LEVEL_BOUGH)
        }
        for (branch in nodes.branchNodes) {
            val brg = branch.branchGeo ?: continue
            // The roots bough is hit as the bough; from the Bough level its Foundation branch takes over.
            addLine(brg.full, branch, Kind.BRANCH, LEVEL_BOUGH, LEVEL_LEAF)
        }
        for (stem in nodes.stemNodes) {
            val sg = stem.stemGeo ?: continue
            addLine(sg.twig, stem, Kind.STEM, LEVEL_BRANCH, LEVEL_LEAF)
        }
        for (leaf in nodes.leafNodes) {
            val lg = leaf.leafGeo ?: continue
            owners += leaf; ks += Kind.LEAF; mins += LEVEL_STEM; maxs += LEVEL_LEAF
            ax += lg.at.x; ay += lg.at.y; bx += lg.at.x; by += lg.at.y
        }

        count = owners.size
        nodeIdx = owners.toTypedArray()
        kind = ks.toIntArray()
        minLevel = mins.toIntArray()
        maxLevel = maxs.toIntArray()
        x0 = ax.toDoubleArray()
        y0 = ay.toDoubleArray()
        x1 = bx.toDoubleArray()
        y1 = by.toDoubleArray()

        // CSR grid: count items per cell, prefix sum, fill. Each item goes into every cell its box touches.
        val cells = cols * rows
        val starts = IntArray(cells + 1)
        for (i in 0 until count) {
            for (cy in cellY(min(y0[i], y1[i]))..cellY(max(y0[i], y1[i]))) {
                for (cx in cellX(min(x0[i], x1[i]))..cellX(max(x0[i], x1[i]))) starts[cy * cols + cx + 1]++
            }
        }
        for (c in 0 until cells) starts[c + 1] += starts[c]
        val items = IntArray(starts[cells])
        val fill = IntArray(cells)
        for (i in 0 until count) {
            for (cy in cellY(min(y0[i], y1[i]))..cellY(max(y0[i], y1[i]))) {
                for (cx in cellX(min(x0[i], x1[i]))..cellX(max(x0[i], x1[i]))) {
                    val c = cy * cols + cx
                    items[starts[c] + fill[c]] = i
                    fill[c]++
                }
            }
        }
        cellStart = starts
        cellItems = items
    }

    private fun cellX(wx: Double): Int = floor((wx - originX) / Tokens.Tree.hitCell).toInt().coerceIn(0, cols - 1)
    private fun cellY(wy: Double): Int = floor((wy - originY) / Tokens.Tree.hitCell).toInt().coerceIn(0, rows - 1)

    /** Node under world point ([wx], [wy]) at zoom [level] (ordinal 0..4), or null. [scale] = CSS px per world unit. */
    fun query(wx: Double, wy: Double, scale: Double, level: Int): Node? {
        val radius = Tokens.Space.hitAreaPx / 2.0 / scale
        val cx = cellX(wx)
        val cy = cellY(wy)
        var best = -1
        var bestKind = -1
        var bestDist = Double.MAX_VALUE
        for (gy in max(cy - 1, 0)..min(cy + 1, rows - 1)) for (gx in max(cx - 1, 0)..min(cx + 1, cols - 1)) {
            val c = gy * cols + gx
            for (n in cellStart[c] until cellStart[c + 1]) {
                val i = cellItems[n]
                if (level < minLevel[i] || level > maxLevel[i]) continue
                val d = distToSegment(wx, wy, x0[i], y0[i], x1[i], y1[i])
                if (d > radius) continue
                if (kind[i] > bestKind || (kind[i] == bestKind && d < bestDist)) {
                    best = i
                    bestKind = kind[i]
                    bestDist = d
                }
            }
        }
        return if (best < 0) null else nodeIdx[best]
    }

    private fun distToSegment(px: Double, py: Double, ax: Double, ay: Double, bx: Double, by: Double): Double {
        val dx = bx - ax
        val dy = by - ay
        val len2 = dx * dx + dy * dy
        val f = if (len2 == 0.0) 0.0 else (((px - ax) * dx + (py - ay) * dy) / len2).coerceIn(0.0, 1.0)
        return hypot(px - (ax + dx * f), py - (ay + dy * f))
    }

    companion object {
        const val LEVEL_CANOPY = 0
        const val LEVEL_BOUGH = 1
        const val LEVEL_BRANCH = 2
        const val LEVEL_STEM = 3
        const val LEVEL_LEAF = 4
        private const val ROOTS = "roots"
    }
}
