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

/** Axis-aligned box in world pixels. */
data class Bounds(val minX: Double, val minY: Double, val maxX: Double, val maxY: Double)

/** One leaf slot at a stem's twig tip. A slot exists for EVERY leaf; it is drawn only if live or up-next (D1, D3). */
data class LeafGeo(val id: String, val at: Vec)

/** A stem twig on a branch limb (Phase 3, docs/14 §4.2). */
data class StemGeo(
    val id: String,
    val twig: Polyline,
    val tip: Vec,
    val labelAnchor: Vec,
    val leaves: List<LeafGeo>,
    val bounds: Bounds,
)

data class BranchGeo(
    val id: String,
    /** The limb at p = 1. */
    val full: Polyline,
    val tip: Vec,
    val labelAnchor: Vec,
    /** Stem twigs along the limb, filled by layoutStems; empty until then. */
    val stems: List<StemGeo> = emptyList(),
    /** Limb plus stems plus leaf slots. Defaults to the limb's own box (stems add to it later). */
    val bounds: Bounds = Bounds(
        full.points.minOf { it.x }, full.points.minOf { it.y },
        full.points.maxOf { it.x }, full.points.maxOf { it.y },
    ),
)

data class BoughGeo(
    val id: String,
    val forkPoint: Vec,
    val centre: Polyline,
    val branches: List<BranchGeo>,
    val labelAnchor: Vec,
)

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
