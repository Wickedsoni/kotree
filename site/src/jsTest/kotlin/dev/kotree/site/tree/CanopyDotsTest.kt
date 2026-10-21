package dev.kotree.site.tree

import dev.kotree.site.Tokens
import dev.kotree.site.tree.gen.canopyDots
import dev.kotree.site.tree.gen.generate
import dev.kotree.site.tree.model.treeData
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** ADR 008 WP4: the canopy buffer is deterministic, bounded and never below the ground. Pure Kotlin, no DOM. */
class CanopyDotsTest {
    private val seed = Tokens.World.seed
    private val geometry = generate(treeData, seed)

    @Test
    fun sameSeedGivesSameDots() {
        val a = canopyDots(geometry, seed)
        val b = canopyDots(geometry, seed)
        assertEquals(a.n, b.n)
        assertContentEquals(a.x, b.x)
        assertContentEquals(a.y, b.y)
        assertContentEquals(a.r, b.r)
        assertContentEquals(a.branch, b.branch)
        assertContentEquals(a.shade, b.shade)
        assertContentEquals(a.turn, b.turn)
        assertContentEquals(a.warm, b.warm)
    }

    @Test
    fun countIsPositiveAndWithinMax() {
        val d = canopyDots(geometry, seed)
        assertTrue(d.n > 0, "no dots")
        assertTrue(d.n <= Tokens.Canopy.maxDots, "n=${d.n} exceeds maxDots")
        assertEquals(d.n, d.x.size)
    }

    @Test
    fun noDotBelowHorizon() {
        val d = canopyDots(geometry, seed)
        val horizon = geometry.trunk.points.first().y
        for (i in 0 until d.n) assertTrue(d.y[i] <= horizon, "dot $i at y=${d.y[i]} below horizon $horizon")
    }

    @Test
    fun fieldsAreInRange() {
        val d = canopyDots(geometry, seed)
        for (i in 0 until d.n) {
            assertTrue(d.branch[i] in d.branchIds.indices)
            assertTrue(d.shade[i] in 0 until Tokens.Canopy.shades)
            assertTrue(d.turn[i] in 0.0..1.0 && d.warm[i] in 0.0..1.0)
        }
    }
}
