package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.TreeData

/**
 * Lazy cache of rasterised tiles for one [TreeGeometry]: roots, trunk, and each bough at split steps 0..8
 * (docs/12 §8). Rasterising happens once per key; the frame loop only blits.
 */
class TileCache(private val geo: TreeGeometry, data: TreeData? = null) {
    private val tiles = HashMap<String, Tile?>()

    /** Full leaf id to leaf, for the drawn leaf pixels. Empty without data (twigs only). */
    private val leaves: Map<String, Leaf> = data?.boughs.orEmpty()
        .flatMap { it.branches }.flatMap { it.stems }.flatMap { it.leaves }.associateBy { it.id }

    /** Number of rasterisations performed so far (for the dev page). */
    var rasterCount: Int = 0
        private set

    fun roots(): Tile? = get(KEY_ROOTS) { LimbRaster.rasterise(geo.roots) }

    fun trunk(): Tile? = get(KEY_TRUNK) { LimbRaster.rasterise(listOf(geo.trunk)) }

    /** The branch limbs plus the bough centre at [step] (clamped to 0..splitSteps). */
    fun bough(boughId: String, step: Int): Tile? {
        val s = step.coerceIn(0, Tokens.Zoom.splitSteps)
        val bough = geo.boughs.firstOrNull { it.id == boughId } ?: return null
        return get("bough:$boughId:$s") {
            // Limbs first, the bough on top: at step 0 the limbs hide inside the black bough.
            LimbRaster.rasterise(geo.limbsAt(boughId, s) + bough.centre)
        }
    }

    fun branchIds(boughId: String): List<String> =
        geo.boughs.firstOrNull { it.id == boughId }?.branches?.map { it.id }.orEmpty()

    /** Twigs and leaf pixels of one branch, built on first use. Null if the branch has no stems. */
    fun twigs(boughId: String, branchId: String): Tile? {
        val branch = geo.boughs.firstOrNull { it.id == boughId }?.branches?.firstOrNull { it.id == branchId }
            ?: return null
        return get("twigs:$branchId") { TwigRaster.rasterise(branch, leaves) }
    }

    fun clear() {
        tiles.clear()
    }

    private inline fun get(key: String, build: () -> Tile?): Tile? {
        if (tiles.containsKey(key)) return tiles[key]
        val tile = build()
        rasterCount++
        tiles[key] = tile
        return tile
    }

    private companion object {
        const val KEY_ROOTS = "roots"
        const val KEY_TRUNK = "trunk"
    }
}
