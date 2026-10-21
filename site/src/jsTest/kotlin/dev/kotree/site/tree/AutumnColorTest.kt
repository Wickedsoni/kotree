package dev.kotree.site.tree

import dev.kotree.site.Tokens
import dev.kotree.site.tree.render.AutumnColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** ADR 008 WP5: pure autumn colour maths. No DOM. */
class AutumnColorTest {
    private val seed = Tokens.World.seed

    @Test
    fun rampHitsItsStops() {
        for (base in intArrayOf(Tokens.Leaf.easy, Tokens.Leaf.medium, Tokens.Leaf.hard)) {
            assertEquals(base, AutumnColor.ramp(base, 0.0))
            assertEquals(Tokens.Autumn.yellow, AutumnColor.ramp(base, 0.5))
            assertEquals(Tokens.Autumn.orange, AutumnColor.ramp(base, 1.0))
        }
    }

    @Test
    fun strokeRunsFromLiveToAutumnStroke() {
        assertEquals(Tokens.Status.live, AutumnColor.stroke(0.0))
        assertEquals(Tokens.Autumn.stroke, AutumnColor.stroke(1.0))
    }

    @Test
    fun restIsDeterministicInRangeAndVaries() {
        val ids = (0 until 50).map { "leaf-$it" }
        val rests = ids.map { AutumnColor.rest(seed, it) }
        assertEquals(rests, ids.map { AutumnColor.rest(seed, it) })
        assertTrue(rests.all { it >= Tokens.Autumn.restMin && it < 1.0 })
        assertTrue(rests.toSet().size > 40)
    }

    @Test
    fun indexIsClampedAndMonotonic() {
        assertEquals(0, AutumnColor.index(-1.0))
        assertEquals(0, AutumnColor.index(0.0))
        assertEquals(Tokens.Autumn.lutSteps - 1, AutumnColor.index(1.0))
        assertEquals(Tokens.Autumn.lutSteps - 1, AutumnColor.index(2.0))
        var last = 0
        for (i in 0..100) {
            val k = AutumnColor.index(i / 100.0)
            assertTrue(k >= last)
            last = k
        }
    }

    @Test
    fun lutMatchesRampAtItsEnds() {
        val lut = AutumnColor.buildLut()
        val end = Tokens.Autumn.lutSteps - 1
        assertEquals(3, lut.fill.size)
        assertEquals(Tokens.Autumn.lutSteps, lut.stroke.size)
        assertEquals(AutumnColor.css(Tokens.Leaf.medium), lut.fill[1][0])
        assertEquals(AutumnColor.css(Tokens.Autumn.orange), lut.fill[1][end])
        assertEquals(AutumnColor.css(Tokens.Autumn.stroke), lut.stroke[end])
    }

    @Test
    fun fractionIsZeroWithoutLiveLeaves() {
        assertEquals(0.0, AutumnColor.fraction(0, 0))
        assertEquals(0.5, AutumnColor.fraction(2, 4))
        assertEquals(1.0, AutumnColor.fraction(3, 3))
    }
}
