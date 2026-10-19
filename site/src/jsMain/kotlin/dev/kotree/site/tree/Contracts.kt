package dev.kotree.site.tree

/**
 * Contracts between generator, renderer, camera and labels (docs/12 §7). Our own types, no framework API.
 * The generator's `generate(data, seed): TreeGeometry` is pure and deterministic (D1).
 */

/** A point in world pixels. */
data class Vec(val x: Double, val y: Double)

/**
 * A drawn line. [ink] is a colour token name resolved through Tokens.Color.inkRgb.
 * Width tapers linearly along the path from [widthPx] at the first point to [tipWidthPx] at the last.
 */
data class Polyline(val points: List<Vec>, val widthPx: Int, val ink: String, val tipWidthPx: Int = widthPx) {
    val maxWidthPx: Int get() = maxOf(widthPx, tipWidthPx)
}

data class BranchGeo(
    val id: String,
    /** The limb at p = 1. */
    val full: Polyline,
    val tip: Vec,
    val labelAnchor: Vec,
)

data class BoughGeo(
    val id: String,
    val forkPoint: Vec,
    val centre: Polyline,
    val branches: List<BranchGeo>,
    val labelAnchor: Vec,
)

/** Axis-aligned box in world pixels. */
data class Bounds(val minX: Double, val minY: Double, val maxX: Double, val maxY: Double)

interface TreeGeometry {
    val bounds: Bounds
    val trunk: Polyline
    val roots: List<Polyline>
    val boughs: List<BoughGeo>

    /** Branch limbs of [boughId] at quantised split [step] in 0..8 (0 = fused on the bough centre). */
    fun limbsAt(boughId: String, step: Int): List<Polyline>
}

/** Camera size in CSS pixels. */
data class Viewport(val widthPx: Int, val heightPx: Int)

data class CameraState(val z: Double, val centre: Vec, val viewportPx: Viewport)
