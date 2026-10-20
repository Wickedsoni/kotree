package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.hit.Kind
import dev.kotree.site.tree.hit.Node
import dev.kotree.site.tree.hit.TreeNodes
import dev.kotree.site.tree.model.LeafStatus
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln

/** CSS `rgb()` string for a token rgb value. */
internal fun cssColor(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

/** Canvas `font` strings: font.ui at type.label (leaf, stem, branch labels) and type.title (bough titles). */
internal object LabelFonts {
    private val family: String = Tokens.Type.fontUi.joinToString(", ") { if (it.contains(' ')) "\"$it\"" else it }
    val label: String = "${Tokens.Type.labelWeight} ${Tokens.Type.labelSizePx}px $family"
    val labelBold: String = "${Tokens.Type.labelWeight + Tokens.TypeScale.emphasisWeightStep} ${Tokens.Type.labelSizePx}px $family"
    val title: String = "${Tokens.TypeScale.title.weight} ${Tokens.TypeScale.title.sizePx}px $family"
    val titleBold: String = "${Tokens.TypeScale.title.weight + Tokens.TypeScale.emphasisWeightStep} ${Tokens.TypeScale.title.sizePx}px $family"

    fun of(style: Int, bold: Boolean): String = if (style == LabelSet.STYLE_TITLE) (if (bold) titleBold else title) else (if (bold) labelBold else label)
    fun lineHeight(style: Int): Int = if (style == LabelSet.STYLE_TITLE) Tokens.TypeScale.title.lineHeightPx else Tokens.Type.labelLineHeightPx
}

/**
 * The labels of one level, placed. Rectangles are stored as offsets (ox, oy) from the world anchor in screen px, so a
 * pan only moves the anchors and never changes the layout. Arrays are parallel; [visible] false = no free slot (the label
 * shows only as a tooltip on hover or focus).
 */
class LabelSet(val count: Int) {
    val node = arrayOfNulls<Node>(count)
    val text = Array(count) { "" }
    val color = Array(count) { "" }
    val style = IntArray(count)
    val ax = DoubleArray(count)
    val ay = DoubleArray(count)
    val w = DoubleArray(count)
    val h = DoubleArray(count)
    val ox = DoubleArray(count)
    val oy = DoubleArray(count)
    val visible = BooleanArray(count)
    /** When not null the label is drawn only while this bough is the current one. */
    val boughFilter = arrayOfNulls<String>(count)
    val indexOf = HashMap<String, Int>()

    companion object {
        const val STYLE_LABEL = 0
        const val STYLE_TITLE = 1
    }
}

private class Cand(val node: Node, val text: String, val style: Int, val color: String, val boughFilter: String?, val group: Int)

/**
 * Greedy label layout (ADR 006). Labels are placed in priority order (roots title, focused stem, live, up-next, shadow, data
 * order); each tries centre (not for leaves), east, west, north, south of its anchor against placed labels and leaf hit
 * squares. The result is computed for ALL labels of the level, not only the visible ones, and cached by
 * (level, branch, stem, floor(ln z * tree.label.buckets)), so panning never changes it and labels do not jitter.
 */
class LabelLayout(private val nodes: TreeNodes, private val g: dynamic) {
    private val sets = HashMap<Long, LabelSet>()
    private val widths = HashMap<String, Double>()
    private val titleWidths = HashMap<String, Double>()
    private var lastKey = Long.MIN_VALUE
    private var lastSet: LabelSet? = null

    private val inkLabel = cssColor(Tokens.Color.onSurface)
    private val inkShadow = cssColor(Tokens.Status.shadow)
    private val inkUpNext = cssColor(Tokens.Status.upnext)

    /** Drops every cached layout and text width (viewport changed, or the web font arrived). */
    fun invalidate() {
        sets.clear()
        widths.clear()
        titleWidths.clear()
        lastKey = Long.MIN_VALUE
        lastSet = null
    }

    /** The layout for this level at zoom [z]; [base] = camera.base. [level] is the ZoomLevel ordinal. */
    fun get(level: Int, branchIdx: Int, stemIdx: Int, z: Double, base: Double): LabelSet {
        val bucket = floor(ln(z) * Tokens.Tree.labelBuckets).toInt()
        val bi = if (level >= LEVEL_BRANCH) branchIdx else -1
        val si = if (level == LEVEL_STEM) stemIdx else -1
        val key = (((level.toLong() * KEY_BRANCH + (bi + 1)) * KEY_STEM + (si + 1)) * KEY_BUCKET) + (bucket + KEY_BUCKET_OFFSET)
        val cached = lastSet
        if (key == lastKey && cached != null) return cached
        var set = sets[key]
        if (set == null) {
            val sRef = base * exp((bucket + 0.5) / Tokens.Tree.labelBuckets)
            set = compute(level, bi, si, sRef)
            sets[key] = set
        }
        lastKey = key
        lastSet = set
        return set
    }

    fun textWidth(text: String, style: Int): Double {
        val cache = if (style == LabelSet.STYLE_TITLE) titleWidths else widths
        val known = cache[text]
        if (known != null) return known
        g.font = LabelFonts.of(style, false)
        val width = (g.measureText(text).width as Number).toDouble()
        cache[text] = width
        return width
    }

    private fun inkOf(node: Node): String {
        val leaf = node.leaf
        if (leaf != null) {
            return when (leaf.status) {
                LeafStatus.LIVE -> inkLabel
                LeafStatus.UPNEXT -> inkUpNext
                LeafStatus.SHADOW -> inkShadow
            }
        }
        val stem = node.stem
        if (node.kind == Kind.STEM && stem != null && stem.counts.live == 0) return inkShadow
        return inkLabel
    }

    private fun leafGroup(node: Node): Int = when (node.leaf?.status) {
        LeafStatus.LIVE -> GROUP_LIVE
        LeafStatus.UPNEXT -> GROUP_UPNEXT
        else -> GROUP_SHADOW
    }

    private fun compute(level: Int, branchIdx: Int, stemIdx: Int, sRef: Double): LabelSet {
        val cands = ArrayList<Cand>()
        val roots = nodes.byId[ROOTS]
        if (level == LEVEL_CANOPY) {
            for (b in nodes.boughNodes) cands += Cand(b, b.title, LabelSet.STYLE_TITLE, inkLabel, null, GROUP_LIVE)
        } else {
            if (roots != null) cands += Cand(roots, roots.title, LabelSet.STYLE_TITLE, inkLabel, null, GROUP_ROOTS)
            val branch = if (branchIdx >= 0) nodes.branchNodes[branchIdx] else null
            if (level == LEVEL_BOUGH) {
                for (b in nodes.branchNodes) {
                    if (b.id != FOUNDATION) cands += Cand(b, b.title, LabelSet.STYLE_LABEL, inkLabel, b.boughId, GROUP_LIVE)
                }
            } else if (branch != null) {
                val stems = nodes.stemsOfBranch[branch.id].orEmpty()
                val focused = if (stemIdx >= 0) nodes.stemNodes[stemIdx] else null
                if (level == LEVEL_STEM && focused != null) {
                    cands += Cand(focused, focused.title, LabelSet.STYLE_LABEL, inkOf(focused), null, GROUP_FOCUSED)
                    for (leaf in nodes.leavesOfStem[focused.id].orEmpty()) {
                        cands += Cand(leaf, leaf.title, LabelSet.STYLE_LABEL, inkOf(leaf), null, leafGroup(leaf))
                    }
                }
                for (st in stems) {
                    if (st !== focused || level != LEVEL_STEM) cands += Cand(st, st.title, LabelSet.STYLE_LABEL, inkOf(st), null, GROUP_OTHER)
                }
                if (level == LEVEL_LEAF) {
                    for (st in stems) for (leaf in nodes.leavesOfStem[st.id].orEmpty()) {
                        cands += Cand(leaf, leaf.title, LabelSet.STYLE_LABEL, inkOf(leaf), null, leafGroup(leaf))
                    }
                }
            }
        }
        val ordered = cands.sortedBy { it.group }

        // Obstacles: hit squares of the focused branch's leaves, which are drawn from the Branch level.
        val obstacles = ArrayList<Node>()
        if (level >= LEVEL_BRANCH && branchIdx >= 0) {
            for (st in nodes.stemsOfBranch[nodes.branchNodes[branchIdx].id].orEmpty()) obstacles += nodes.leavesOfStem[st.id].orEmpty()
        }
        val cap = ordered.size + obstacles.size
        val px = DoubleArray(cap)
        val py = DoubleArray(cap)
        val pw = DoubleArray(cap)
        val ph = DoubleArray(cap)
        var pc = 0
        val hit = Tokens.Space.hitAreaPx.toDouble()
        for (o in obstacles) {
            px[pc] = o.ax * sRef - hit / 2.0
            py[pc] = o.ay * sRef - hit / 2.0
            pw[pc] = hit
            ph[pc] = hit
            pc++
        }

        val pad = Tokens.Space.labelOutlinePx.toDouble()
        val gap = Tokens.Spacing.s1.toDouble()
        val set = LabelSet(ordered.size)
        for ((i, c) in ordered.withIndex()) {
            val isLeaf = c.node.kind == Kind.LEAF
            val w = textWidth(c.text, c.style) + 2.0 * pad
            val h = LabelFonts.lineHeight(c.style).toDouble()
            val axl = c.node.ax * sRef
            val ayl = c.node.ay * sRef
            val r = if (isLeaf) hit / 2.0 else 0.0
            set.node[i] = c.node
            set.text[i] = c.text
            set.color[i] = c.color
            set.style[i] = c.style
            set.ax[i] = c.node.ax
            set.ay[i] = c.node.ay
            set.w[i] = w
            set.h[i] = h
            set.boughFilter[i] = c.boughFilter
            set.indexOf[c.node.id] = i
            set.ox[i] = -w / 2.0
            set.oy[i] = -h / 2.0
            for (slot in (if (isLeaf) 1 else 0)..SLOT_SOUTH) {
                val rx = when (slot) {
                    SLOT_CENTRE, SLOT_NORTH, SLOT_SOUTH -> axl - w / 2.0
                    SLOT_EAST -> axl + r + gap
                    else -> axl - r - gap - w
                }
                val ry = when (slot) {
                    SLOT_NORTH -> ayl - r - gap - h
                    SLOT_SOUTH -> ayl + r + gap
                    else -> ayl - h / 2.0
                }
                var free = true
                for (k in 0 until pc) {
                    if (rx < px[k] + pw[k] + pad && rx + w + pad > px[k] && ry < py[k] + ph[k] + pad && ry + h + pad > py[k]) {
                        free = false
                        break
                    }
                }
                if (free) {
                    set.visible[i] = true
                    set.ox[i] = rx - axl
                    set.oy[i] = ry - ayl
                    px[pc] = rx
                    py[pc] = ry
                    pw[pc] = w
                    ph[pc] = h
                    pc++
                    break
                }
            }
        }
        return set
    }

    private companion object {
        const val ROOTS = "roots"
        const val FOUNDATION = "foundation"
        const val LEVEL_CANOPY = 0
        const val LEVEL_BOUGH = 1
        const val LEVEL_BRANCH = 2
        const val LEVEL_STEM = 3
        const val LEVEL_LEAF = 4
        const val GROUP_ROOTS = 0
        const val GROUP_FOCUSED = 1
        const val GROUP_LIVE = 2
        const val GROUP_UPNEXT = 3
        const val GROUP_SHADOW = 4
        const val GROUP_OTHER = 5
        const val SLOT_CENTRE = 0
        const val SLOT_EAST = 1
        const val SLOT_WEST = 2
        const val SLOT_NORTH = 3
        const val SLOT_SOUTH = 4
        // Cache key radices: branch index + 1 < 32, stem index + 1 < 2048, bucket + offset < 256.
        const val KEY_BRANCH = 32L
        const val KEY_STEM = 2048L
        const val KEY_BUCKET = 256L
        const val KEY_BUCKET_OFFSET = 64L
    }
}
