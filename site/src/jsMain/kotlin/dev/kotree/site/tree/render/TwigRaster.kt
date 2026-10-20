package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.model.Difficulty
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus

/**
 * One tile per branch: the gray stem twigs plus the drawn leaf pixels (docs/14 §4.1).
 * Only live and up-next leaves are drawn (D3); shadow leaves never are. The glow is a DOM ring, not drawn here.
 */
object TwigRaster {
    /** [leaves] maps a full leaf id to its data; without it only twigs are drawn. Null when there is nothing to draw. */
    fun rasterise(branch: BranchGeo, leaves: Map<String, Leaf>): Tile? {
        if (branch.stems.isEmpty()) return null
        val pips = ArrayList<LimbRaster.Pip>()
        for (stem in branch.stems) for (slot in stem.leaves) {
            val leaf = leaves[slot.id] ?: continue
            val rgb = when (leaf.status) {
                LeafStatus.SHADOW -> continue
                LeafStatus.UPNEXT -> Tokens.Leaf.upnextPip
                LeafStatus.LIVE -> when (leaf.difficulty) {
                    Difficulty.EASY -> Tokens.Leaf.easy
                    Difficulty.MEDIUM -> Tokens.Leaf.medium
                    Difficulty.HARD -> Tokens.Leaf.hard
                }
            }
            pips += LimbRaster.Pip(slot.at.x, slot.at.y, Tokens.Gen.LEAF_SIZE.toDouble(), rgb)
        }
        return LimbRaster.rasterise(branch.stems.map { it.twig }, pips)
    }
}
