package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec

/**
 * Split step to branch limbs (docs/03 §2.4). Every branch polyline has the same number of points as
 * its bough centre; at step 0 each point sits on the centre, at the last step on the branch's own path,
 * and in between the points are linearly interpolated. The branch ink and width come from the full limb.
 */
internal fun limbAtStep(centre: Polyline, full: Polyline, step: Int): Polyline {
    val s = step.coerceIn(0, Tokens.Zoom.splitSteps)
    val p = s.toDouble() / Tokens.Zoom.splitSteps
    val pts = full.points.mapIndexed { i, f ->
        val c = centre.points[i]
        Vec(quantise(c.x + (f.x - c.x) * p), quantise(c.y + (f.y - c.y) * p))
    }
    return Polyline(pts, full.widthPx, full.ink, full.tipWidthPx)
}

internal class GeneratedTree(
    override val bounds: Bounds,
    override val trunk: Polyline,
    override val roots: List<Polyline>,
    override val boughs: List<BoughGeo>,
) : TreeGeometry {

    private val byId: Map<String, BoughGeo> = boughs.associateBy { it.id }

    override fun limbsAt(boughId: String, step: Int): List<Polyline> {
        val b = byId[boughId] ?: return emptyList()
        // Foundation is its own bough: its single limb is the bough itself, nothing to separate.
        if (boughId == Tokens.Gen.FOUNDATION_ID) return emptyList()
        return b.branches.map { limbAtStep(b.centre, it.full, step) }
    }
}
