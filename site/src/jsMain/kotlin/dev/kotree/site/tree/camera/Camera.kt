package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.LeafGeo
import dev.kotree.site.tree.StemGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.Viewport
import kotlin.math.max
import kotlin.math.min
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlinx.browser.window

private const val FIT_EPS = 1e-9

/** Z2 Branch level enter (docs/14 §4.1); leave is [BRANCH_LEAVE_RATIO] of it, like the bough 1.8 / 1.62. */
private const val BRANCH_LEAVE_RATIO = 0.9
/** Where a branch fly lands (the old zMax of 4.0, before zMax rose to 24 for the Leaf level). */
private const val BRANCH_LANDING_Z = 4.0

/** Zoom levels of docs/03 §2.2 plus the Phase 3 Stem and Leaf levels. */
enum class ZoomLevel { Z0, Z1, Z2, Z3, Z4 }

/** What the overlay layers need from the camera: the level and the focused branch (null below Z2). */
data class ViewState(val level: ZoomLevel, val branchId: String?)

/**
 * Level with hysteresis on every switch (no flicker at a threshold) and the focused branch: the one whose
 * bounds are nearest the view centre, sticky like the bough switch. Plain state, call [update] on each camera change.
 */
class ViewTracker(private val geometry: dev.kotree.site.tree.TreeGeometry) {
    private var inBough = false
    private var inBranch = false
    private var inStem = false
    private var inLeaf = false
    private var branchId: String? = null
    private var pinned: String? = null
    private val branches = geometry.boughs.flatMap { it.branches }

    fun update(cam: CameraState): ViewState {
        val z = cam.z
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
        if (level < ZoomLevel.Z2) {
            branchId = null
            return ViewState(level, null)
        }
        // Distance to the limb itself, not its bounding box: boxes of crossing limbs overlap, so many would tie at 0
        // and the clicked branch would not be the focused one.
        fun dist(b: BranchGeo): Double {
            val pts = b.full.points
            var best = Double.MAX_VALUE
            for (i in 1 until pts.size) best = min(best, distToSegment(cam.centre, pts[i - 1], pts[i]))
            // A stem fly centres on the stem (off the limb): its twig and leaf slots count too, so focus stays on its
            // branch. Distances to thin shapes, not boxes: the boxes of neighbouring branches overlap and tie at 0.
            for (st in b.stems) {
                val tw = st.twig.points
                for (i in 1 until tw.size) best = min(best, distToSegment(cam.centre, tw[i - 1], tw[i]))
                for (l in st.leaves) best = min(best, distToSegment(cam.centre, l.at, l.at))
            }
            return best
        }
        // A stem or leaf fly pins its branch: a fly passes near other limbs and would flip the focus mid-way.
        pinned?.let { id ->
            val pb = branches.firstOrNull { it.id == id }
            if (pb != null && dist(pb) <= Tokens.Camera.pinReleaseWorld) {
                branchId = id
                return ViewState(level, id)
            }
            pinned = null
        }
        val nearest = branches.minByOrNull { dist(it) }
        val held = branches.firstOrNull { it.id == branchId }
        if (nearest != null && (held == null || dist(nearest) < dist(held) * Tokens.Camera.boughSwitchRatio)) branchId = nearest.id
        return ViewState(level, branchId)
    }

    /** Keep [id] focused while the view stays near it (set by stem and leaf flies). */
    fun pin(id: String) { pinned = id }

    /** Drop the pin (flies to a bough, a branch or home). */
    fun unpin() { pinned = null }

    private fun hysteresis(on: Boolean, z: Double, enter: Double, leave: Double): Boolean =
        if (z >= enter) true else if (z < leave) false else on
}

private fun distToSegment(p: Vec, a: Vec, b: Vec): Double {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val len2 = dx * dx + dy * dy
    val f = if (len2 == 0.0) 0.0 else (((p.x - a.x) * dx + (p.y - a.y) * dy) / len2).coerceIn(0.0, 1.0)
    return kotlin.math.hypot(p.x - (a.x + dx * f), p.y - (a.y + dy * f))
}

/** Where a fly ends. */
data class FlyTarget(val z: Double, val centre: Vec)

/**
 * Screen pixels per world pixel at z = 1: the scale at which [bounds] exactly fits [vp] (docs/03 §2.1).
 */
fun baseScale(bounds: Bounds, vp: Viewport): Double {
    val w = max(bounds.maxX - bounds.minX, 1.0)
    val h = max(bounds.maxY - bounds.minY, 1.0)
    return min(max(vp.widthPx, 1) / w, max(vp.heightPx, 1) / h)
}

/**
 * CSS px per world unit at zoom [z], snapped so one art pixel is a whole number of device pixels (docs/12 §8).
 * The camera zoom stays continuous; what is drawn (tiles and labels) follows this stepped scale.
 */
fun snappedScale(bounds: Bounds, vp: Viewport, z: Double): Double {
    val dpr = window.devicePixelRatio
    val deviceUnitsPerArtPx = baseScale(bounds, vp) * z * dpr * Tokens.World.px
    val whole = max(1, deviceUnitsPerArtPx.roundToInt())
    return whole / (dpr * Tokens.World.px)
}

/** World to screen (CSS px relative to the camera element's top-left). */
fun CameraState.toScreen(bounds: Bounds, v: Vec): Vec {
    val s = snappedScale(bounds, viewportPx, z)
    return Vec((v.x - centre.x) * s + viewportPx.widthPx / 2.0, (v.y - centre.y) * s + viewportPx.heightPx / 2.0)
}

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
 * Camera over the world [bounds]. Plain Kotlin state; [onChange] fires after every change so a
 * caller can schedule a redraw (docs/12 §8: frames only while the camera moves).
 */
class Camera(val bounds: Bounds) {
    var viewport: Viewport = Viewport(1, 1)
        private set
    var z: Double = 1.0
        private set
    var centre: Vec = boundsCentre()
        private set
    var onChange: (() -> Unit)? = null

    val state: CameraState get() = CameraState(z, centre, viewport)

    /** Screen pixels per world pixel now. */
    val scale: Double get() = snappedScale(bounds, viewport, z)

    fun setViewport(vp: Viewport) {
        if (vp == viewport) return
        viewport = vp
        onChange?.invoke()
    }

    fun set(newZ: Double, newCentre: Vec) {
        z = newZ.coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax)
        centre = Vec(
            newCentre.x.coerceIn(bounds.minX, bounds.maxX),
            newCentre.y.coerceIn(bounds.minY, bounds.maxY),
        )
        onChange?.invoke()
    }

    /** Drag: the world follows the pointer. */
    fun panByScreen(dxPx: Double, dyPx: Double) {
        val s = scale
        set(z, Vec(centre.x - dxPx / s, centre.y - dyPx / s))
    }

    /** Zoom by [factor] keeping the world point under screen point ([sx], [sy]) fixed. */
    fun zoomAt(sx: Double, sy: Double, factor: Double) {
        val before = scale
        val wx = centre.x + (sx - viewport.widthPx / 2.0) / before
        val wy = centre.y + (sy - viewport.heightPx / 2.0) / before
        val newZ = (z * factor).coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax)
        val after = snappedScale(bounds, viewport, newZ)
        set(newZ, Vec(wx - (sx - viewport.widthPx / 2.0) / after, wy - (sy - viewport.heightPx / 2.0) / after))
    }

    fun zoomAtCentre(factor: Double) = zoomAt(viewport.widthPx / 2.0, viewport.heightPx / 2.0, factor)

    /** Whole tree: fit with a 5% margin, then at least z = 1 (docs/03 §2.5). */
    fun homeTarget(): FlyTarget = fit(bounds, Tokens.Camera.fitMarginTree, wholeZAtMost(1.0))

    /** Largest zoom <= [z] whose art scale is a whole number of device pixels (never rounds up past the fit). */
    private fun wholeZAtMost(z: Double): Double {
        val perArt = window.devicePixelRatio * Tokens.World.px
        val whole = max(1.0, floor(baseScale(bounds, viewport) * z * perArt + FIT_EPS))
        return whole / perArt / baseScale(bounds, viewport)
    }

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
        // fit() caps the zoom at the largest whole-pixel scale; never land above the Leaf level on a stem.
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
        return min(max(viewport.widthPx, 1) / w, max(viewport.heightPx, 1) / h) / baseScale(bounds, viewport)
    }

    /** Fit [box] with [margin] on each side, then clamp z to at least [minZ] and at most zMax. */
    fun fit(box: Bounds, margin: Double, minZ: Double): FlyTarget {
        val k = 1.0 + 2.0 * margin
        val w = max(box.maxX - box.minX, 1.0) * k
        val h = max(box.maxY - box.minY, 1.0) * k
        val needed = min(max(viewport.widthPx, 1) / w, max(viewport.heightPx, 1) / h)
        // Largest whole device-pixel art scale that still fits, so the snapped view never overflows the margin.
        val dpr = window.devicePixelRatio
        val perArt = dpr * Tokens.World.px
        val whole = max(1.0, floor(needed * perArt + FIT_EPS))
        val fitZ = whole / perArt / baseScale(bounds, viewport)
        return FlyTarget(
            max(fitZ, minZ).coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax),
            Vec((box.minX + box.maxX) / 2.0, (box.minY + box.maxY) / 2.0),
        )
    }

    private fun boundsCentre() = Vec((bounds.minX + bounds.maxX) / 2.0, (bounds.minY + bounds.maxY) / 2.0)
}
