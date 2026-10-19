package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.TreeGeometry

/**
 * Lazy cache of rasterised tiles for one [TreeGeometry]: roots, trunk, and each bough at split steps 0..8
 * (docs/12 §8). Rasterising happens once per key; the frame loop only blits.
 */
class TileCache(private val geo: TreeGeometry) {
    private val tiles = HashMap<String, Tile?>()

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
