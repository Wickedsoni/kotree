package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.LeafGeo
import dev.kotree.site.tree.StemGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.Viewport
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

/** Z2 Branch level enter (docs/14 §4.1); leave is [BRANCH_LEAVE_RATIO] of it, like the bough 1.8 / 1.62. */
private const val BRANCH_LEAVE_RATIO = 0.9
/** Where a branch fly lands (the old zMax of 4.0, before zMax rose to 24 for the Leaf level). */
private const val BRANCH_LANDING_Z = 4.0

/** Zoom levels of docs/03 §2.2 plus the Phase 3 Stem and Leaf levels. */
enum class ZoomLevel { Z0, Z1, Z2, Z3, Z4 }

/**
 * What the canvas and the accessible list need from the camera: the level, the current bough (from Z1), the focused
 * branch (from Z2) and the focused stem (from Z3). Ids are null where the level does not define them.
 */
data class ViewState(val level: ZoomLevel, val boughId: String?, val branchId: String?, val stemId: String?)

/**
 * Level with hysteresis on every switch (no flicker at a threshold), the current bough, the focused branch and the focused
 * stem: each the one nearest the view centre, sticky like the bough switch. Plain state; call [update] on each frame
 * that moved the camera. It returns the same [ViewState] instance while nothing changed.
 */
class ViewTracker(geometry: TreeGeometry) {
    private var inBough = false
    private var inBranch = false
    private var inStem = false
    private var inLeaf = false
    private var boughId: String? = null
    private var branchId: String? = null
    private var stemId: String? = null
    private var pinned: String? = null
    private val boughs = geometry.boughs
    private val branches = geometry.boughs.flatMap { it.branches }
    private val boughCentreX = DoubleArray(boughs.size)
    private val boughCentreY = DoubleArray(boughs.size)
    private var last = ViewState(ZoomLevel.Z0, null, null, null)

    init {
        for ((i, b) in boughs.withIndex()) {
            val box = boughBounds(b)
            boughCentreX[i] = (box.minX + box.maxX) / 2.0
            boughCentreY[i] = (box.minY + box.maxY) / 2.0
        }
    }

    fun update(z: Double, cx: Double, cy: Double): ViewState {
        inBough = hysteresis(inBough, z, Tokens.Zoom.boughEnter, Tokens.Zoom.boughLeave)
        inBranch = hysteresis(inBranch, z, Tokens.Zoom.branchEnter, Tokens.Zoom.branchEnter * BRANCH_LEAVE_RATIO)
        inStem = hysteresis(inStem, z, Tokens.Zoom.stemEnter, Tokens.Zoom.stemLeave)
        inLeaf = hysteresis(inLeaf, z, Tokens.Zoom.leafEnter, Tokens.Zoom.leafLeave)
        val level = when {
            inLeaf -> ZoomLevel.Z4
            inStem -> ZoomLevel.Z3
            inBranch -> ZoomLevel.Z2
            inBough -> ZoomLevel.Z1
            else -> ZoomLevel.Z0
        }
        if (level >= ZoomLevel.Z1) updateBough(cx, cy) else boughId = null
        if (level >= ZoomLevel.Z2) updateBranch(cx, cy) else branchId = null
        if (level >= ZoomLevel.Z3) updateStem(cx, cy) else stemId = null
        val l = last
        if (l.level == level && l.boughId == boughId && l.branchId == branchId && l.stemId == stemId) return l
        last = ViewState(level, boughId, branchId, stemId)
        return last
    }

    private fun updateBough(cx: Double, cy: Double) {
        var best = -1
        var bestD = Double.MAX_VALUE
        var held = -1
        for (i in boughs.indices) {
            val d = hypot(boughCentreX[i] - cx, boughCentreY[i] - cy)
            if (d < bestD) { bestD = d; best = i }
            if (boughs[i].id == boughId) held = i
        }
        if (best < 0) return
        val heldD = if (held < 0) Double.MAX_VALUE else hypot(boughCentreX[held] - cx, boughCentreY[held] - cy)
        if (held < 0 || bestD < heldD * Tokens.Camera.boughSwitchRatio) boughId = boughs[best].id
    }

    private fun updateBranch(cx: Double, cy: Double) {
        // A stem or leaf fly pins its branch: a fly passes near other limbs and would flip the focus mid-way.
        val pin = pinned
        if (pin != null) {
            var pb: BranchGeo? = null
            for (b in branches) if (b.id == pin) pb = b
            if (pb != null && distToBranch(pb, cx, cy) <= Tokens.Camera.pinReleaseWorld) {
                branchId = pin
                return
            }
            pinned = null
        }
        // Distance to the limb itself, not its bounding box: boxes of crossing limbs overlap, so many would tie at 0.
        var nearest: BranchGeo? = null
        var nearestD = Double.MAX_VALUE
        var held: BranchGeo? = null
        for (b in branches) {
            val d = distToBranch(b, cx, cy)
            if (d < nearestD) { nearestD = d; nearest = b }
            if (b.id == branchId) held = b
        }
        if (nearest == null) return
        val heldD = if (held == null) Double.MAX_VALUE else distToBranch(held, cx, cy)
        if (held == null || nearestD < heldD * Tokens.Camera.boughSwitchRatio) branchId = nearest.id
    }

    private fun updateStem(cx: Double, cy: Double) {
        var branch: BranchGeo? = null
        for (b in branches) if (b.id == branchId) branch = b
        if (branch == null || branch.stems.isEmpty()) { stemId = null; return }
        var nearest: StemGeo? = null
        var nearestD = Double.MAX_VALUE
        var held: StemGeo? = null
        for (st in branch.stems) {
            val d = distToStem(st, cx, cy)
            if (d < nearestD) { nearestD = d; nearest = st }
            if (st.id == stemId) held = st
        }
        if (nearest == null) return
        val heldD = if (held == null) Double.MAX_VALUE else distToStem(held, cx, cy)
        if (held == null || nearestD < heldD * Tokens.Camera.boughSwitchRatio) stemId = nearest.id
    }

    private fun distToBranch(b: BranchGeo, cx: Double, cy: Double): Double {
        var best = distToLine(b.full.points, cx, cy)
        // A stem fly centres on the stem (off the limb): its twigs and leaf slots count too, so focus stays on its branch.
        for (st in b.stems) best = min(best, distToStem(st, cx, cy))
        return best
    }

    private fun distToStem(st: StemGeo, cx: Double, cy: Double): Double {
        var best = distToLine(st.twig.points, cx, cy)
        for (l in st.leaves) best = min(best, hypot(l.at.x - cx, l.at.y - cy))
        return best
    }

    private fun distToLine(pts: List<Vec>, cx: Double, cy: Double): Double {
        var best = Double.MAX_VALUE
        for (i in 1 until pts.size) best = min(best, distToSegment(cx, cy, pts[i - 1], pts[i]))
        return best
    }

    /** Keep [id] focused while the view stays near it (set by stem and leaf flies). */
    fun pin(id: String) { pinned = id }

    /** Drop the pin (flies to a bough, a branch or home). */
    fun unpin() { pinned = null }

    private fun hysteresis(on: Boolean, z: Double, enter: Double, leave: Double): Boolean =
        if (z >= enter) true else if (z < leave) false else on
}

private fun distToSegment(px: Double, py: Double, a: Vec, b: Vec): Double {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val len2 = dx * dx + dy * dy
    val f = if (len2 == 0.0) 0.0 else (((px - a.x) * dx + (py - a.y) * dy) / len2).coerceIn(0.0, 1.0)
    return hypot(px - (a.x + dx * f), py - (a.y + dy * f))
}

/** Where a fly ends. */
data class FlyTarget(val z: Double, val centre: Vec)

/**
 * Screen pixels per world unit at z = 1: the scale at which [bounds] exactly fits [vp] (docs/03 §2.1).
 */
fun baseScale(bounds: Bounds, vp: Viewport): Double {
    val w = max(bounds.maxX - bounds.minX, 1.0)
    val h = max(bounds.maxY - bounds.minY, 1.0)
    return min(max(vp.widthPx, 1) / w, max(vp.heightPx, 1) / h)
}

/** CSS px per world unit at zoom [z]. Continuous: zoom has no steps (ADR 006, replaces the whole-pixel snapping). */
fun scaleAt(bounds: Bounds, vp: Viewport, z: Double): Double = baseScale(bounds, vp) * z

/** Bounds of polylines, padded by half the line width. */
fun boundsOf(lines: List<Polyline>): Bounds {
    var minX = Double.MAX_VALUE
    var minY = Double.MAX_VALUE
    var maxX = -Double.MAX_VALUE
    var maxY = -Double.MAX_VALUE
    for (line in lines) {
        val pad = line.maxWidthPx / 2.0
        for (p in line.points) {
            minX = min(minX, p.x - pad)
            minY = min(minY, p.y - pad)
            maxX = max(maxX, p.x + pad)
            maxY = max(maxY, p.y + pad)
        }
    }
    return Bounds(minX, minY, maxX, maxY)
}

/** All branches at p = 1, the bough centre and every branch label anchor (docs/03 §2.5). */
fun boughBounds(bough: BoughGeo): Bounds {
    val limbs = boundsOf(listOf(bough.centre) + bough.branches.map { it.full })
    var minX = limbs.minX
    var minY = limbs.minY
    var maxX = limbs.maxX
    var maxY = limbs.maxY
    for (branch in bough.branches) {
        minX = min(minX, branch.labelAnchor.x)
        minY = min(minY, branch.labelAnchor.y)
        maxX = max(maxX, branch.labelAnchor.x)
        maxY = max(maxY, branch.labelAnchor.y)
    }
    return Bounds(minX, minY, maxX, maxY)
}

/** The limb plus its label anchor, so a fit always keeps the branch title on screen. */
fun branchBounds(branch: BranchGeo): Bounds {
    val limb = boundsOf(listOf(branch.full))
    val a = branch.labelAnchor
    return Bounds(min(limb.minX, a.x), min(limb.minY, a.y), max(limb.maxX, a.x), max(limb.maxY, a.y))
}

/**
 * Camera over the world [bounds]. Plain Kotlin state in mutable fields (no object per change); [onChange] fires after every
 * change so the frame loop can schedule a draw (docs/07 §7: frames only while the camera moves).
 */
class Camera(val bounds: Bounds) {
    var viewport: Viewport = Viewport(1, 1)
        private set
    var z: Double = 1.0
        private set
    var cx: Double = (bounds.minX + bounds.maxX) / 2.0
        private set
    var cy: Double = (bounds.minY + bounds.maxY) / 2.0
        private set
    /** Screen pixels per world unit at z = 1 for the current viewport. */
    var base: Double = baseScale(bounds, viewport)
        private set
    var onChange: (() -> Unit)? = null

    val state: CameraState get() = CameraState(z, Vec(cx, cy), viewport)

    /** Screen pixels per world unit now. */
    val scale: Double get() = base * z

    fun setViewport(vp: Viewport) {
        if (vp == viewport) return
        viewport = vp
        base = baseScale(bounds, vp)
        onChange?.invoke()
    }

    fun set(newZ: Double, x: Double, y: Double) {
        z = newZ.coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax)
        cx = x.coerceIn(bounds.minX, bounds.maxX)
        cy = y.coerceIn(bounds.minY, bounds.maxY)
        onChange?.invoke()
    }

    /** Drag: the world follows the pointer. */
    fun panByScreen(dxPx: Double, dyPx: Double) {
        val s = scale
        set(z, cx - dxPx / s, cy - dyPx / s)
    }

    /** Zoom by [factor] keeping the world point under screen point ([sx], [sy]) fixed. */
    fun zoomAt(sx: Double, sy: Double, factor: Double) {
        val before = scale
        val halfW = viewport.widthPx / 2.0
        val halfH = viewport.heightPx / 2.0
        val wx = cx + (sx - halfW) / before
        val wy = cy + (sy - halfH) / before
        val newZ = (z * factor).coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax)
        val after = base * newZ
        set(newZ, wx - (sx - halfW) / after, wy - (sy - halfH) / after)
    }

    fun zoomAtCentre(factor: Double) = zoomAt(viewport.widthPx / 2.0, viewport.heightPx / 2.0, factor)

    /** Whole tree: fit with a 5% margin (docs/03 §2.5). */
    fun homeTarget(): FlyTarget = fit(bounds, Tokens.Camera.fitMarginTree, Tokens.Zoom.zMin)

    fun boughTarget(b: BoughGeo): FlyTarget = fit(boughBounds(b), Tokens.Camera.fitMarginBough, Tokens.Zoom.boughFitMin)

    /**
     * Fit the branch limb plus its label. A branch runs from the trunk fork to its tip, so at zMax the whole
     * limb usually does not fit; then the view is centred between the tip and the label (the part that
     * identifies the branch) so the title is on screen, not cut off at an edge.
     */
    fun branchTarget(b: BranchGeo, upNextLeafId: String? = null): FlyTarget {
        val box = branchBounds(b)
        // Landing z, not zMax: zMax rose from 4 to 24 for the Leaf level but a branch still lands at the old 4.
        // A crowded branch lands higher (Tokens.Zoom.branchLandingZ, found by layoutReport).
        val landing = max(BRANCH_LANDING_Z, Tokens.Zoom.branchLandingZ[b.id] ?: 0.0)
        val target = fit(box, Tokens.Camera.fitMarginBranch, landing)
        if (requiredZ(box, Tokens.Camera.fitMarginBranch) >= landing) return target
        // The whole limb does not fit: show the stem holding the up-next leaf (else the first stem) together with
        // the branch label when both fit, otherwise centre on that stem so the glowing leaf is on screen.
        val stem = b.stems.firstOrNull { s -> s.leaves.any { it.id == upNextLeafId } } ?: b.stems.firstOrNull()
        val a = b.labelAnchor
        if (stem == null) return FlyTarget(target.z, Vec((b.tip.x + a.x) / 2.0, (b.tip.y + a.y) / 2.0))
        val sb = stem.bounds
        val both = Bounds(min(sb.minX, a.x), min(sb.minY, a.y), max(sb.maxX, a.x), max(sb.maxY, a.y))
        if (requiredZ(both, Tokens.Camera.fitMarginBranch) >= landing) return FlyTarget(target.z, Vec((both.minX + both.maxX) / 2.0, (both.minY + both.maxY) / 2.0))
        return FlyTarget(target.z, Vec((sb.minX + sb.maxX) / 2.0, (sb.minY + sb.maxY) / 2.0))
    }

    /** Fly to a stem twig, its labels and leaf slots: fit with a 12% margin, at least z = 8 (docs/14 §4.1). */
    fun stemTarget(s: StemGeo): FlyTarget = fit(s.bounds, Tokens.Zoom.stemFitMargin, Tokens.Zoom.stemFitMinZ).let { t ->
        // Never land above the Leaf level on a stem.
        t.copy(z = min(t.z, Tokens.Zoom.leafEnter - 1.0))
    }

    /** Fly to one leaf slot: at least the Leaf level so its title shows (docs/14 §4.1). */
    fun leafTarget(l: LeafGeo): FlyTarget {
        val pad = Tokens.Gen.LEAF_PITCH
        val box = Bounds(l.at.x - pad, l.at.y - pad, l.at.x + pad, l.at.y + pad)
        return fit(box, Tokens.Zoom.stemFitMargin, Tokens.Zoom.leafEnter)
    }

    private fun requiredZ(box: Bounds, margin: Double): Double {
        val k = 1.0 + 2.0 * margin
        val w = max(box.maxX - box.minX, 1.0) * k
        val h = max(box.maxY - box.minY, 1.0) * k
        return min(max(viewport.widthPx, 1) / w, max(viewport.heightPx, 1) / h) / base
    }

    /** Fit [box] with [margin] on each side, then clamp z to at least [minZ] and at most zMax. */
    fun fit(box: Bounds, margin: Double, minZ: Double): FlyTarget {
        val fitZ = requiredZ(box, margin)
        return FlyTarget(
            max(fitZ, minZ).coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax),
            Vec((box.minX + box.maxX) / 2.0, (box.minY + box.maxY) / 2.0),
        )
    }
}
