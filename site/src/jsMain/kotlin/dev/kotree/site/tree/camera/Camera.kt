package dev.kotree.site.tree.camera

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.Viewport
import kotlin.math.max
import kotlin.math.min

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

/** World to screen (CSS px relative to the camera element's top-left). */
fun CameraState.toScreen(bounds: Bounds, v: Vec): Vec {
    val s = baseScale(bounds, viewportPx) * z
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

/** All branches at p = 1 plus the bough centre (docs/03 §2.5). */
fun boughBounds(bough: BoughGeo): Bounds = boundsOf(listOf(bough.centre) + bough.branches.map { it.full })

fun branchBounds(branch: BranchGeo): Bounds = boundsOf(listOf(branch.full))

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
    val scale: Double get() = baseScale(bounds, viewport) * z

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
        val after = baseScale(bounds, viewport) * newZ
        set(newZ, Vec(wx - (sx - viewport.widthPx / 2.0) / after, wy - (sy - viewport.heightPx / 2.0) / after))
    }

    fun zoomAtCentre(factor: Double) = zoomAt(viewport.widthPx / 2.0, viewport.heightPx / 2.0, factor)

    /** Whole tree: fit with a 5% margin, then at least z = 1 (docs/03 §2.5). */
    fun homeTarget(): FlyTarget = fit(bounds, Tokens.Camera.fitMarginTree, 1.0)

    fun boughTarget(b: BoughGeo): FlyTarget = fit(boughBounds(b), Tokens.Camera.fitMarginBough, Tokens.Zoom.splitEnd)

    fun branchTarget(b: BranchGeo): FlyTarget = fit(branchBounds(b), Tokens.Camera.fitMarginBranch, Tokens.Zoom.zMax)

    /** Fit [box] with [margin] on each side, then clamp z to at least [minZ] and at most zMax. */
    fun fit(box: Bounds, margin: Double, minZ: Double): FlyTarget {
        val k = 1.0 + 2.0 * margin
        val w = max(box.maxX - box.minX, 1.0) * k
        val h = max(box.maxY - box.minY, 1.0) * k
        val needed = min(max(viewport.widthPx, 1) / w, max(viewport.heightPx, 1) / h)
        val fitZ = needed / baseScale(bounds, viewport)
        return FlyTarget(
            max(fitZ, minZ).coerceIn(Tokens.Zoom.zMin, Tokens.Zoom.zMax),
            Vec((box.minX + box.maxX) / 2.0, (box.minY + box.maxY) / 2.0),
        )
    }

    private fun boundsCentre() = Vec((bounds.minX + bounds.maxX) / 2.0, (bounds.minY + bounds.maxY) / 2.0)
}
