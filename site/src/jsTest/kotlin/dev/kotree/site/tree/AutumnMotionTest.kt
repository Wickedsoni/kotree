package dev.kotree.site.tree

import dev.kotree.site.Tokens
import dev.kotree.site.tree.render.AutumnMotion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** ADR 008 WP6: pure motion state. No DOM. */
class AutumnMotionTest {
    private val seed = Tokens.World.seed
    private val frame = 16.0
    private val ease = Tokens.Motion.autumnMs.toDouble()

    private fun motion(reduced: Boolean = false) = AutumnMotion(3, seed, { "leaf-$it" }, { reduced })

    @Test
    fun envelopeRisesWhileMovingAndSettlesAfter() {
        val m = motion()
        m.drawn = 1
        var now = 0.0
        repeat((Tokens.Sway.riseMs / frame).toInt() + 2) { now += frame; m.step(now, frame, true) }
        assertEquals(1.0, m.activity)
        assertTrue(m.active())
        repeat((Tokens.Sway.settleMs / frame).toInt() + 2) { now += frame; m.step(now, frame, false) }
        assertEquals(0.0, m.activity)
        assertFalse(m.active())
    }

    @Test
    fun idleIsInactiveAndMovingWithNoDoneLeafInViewAsksForNoFrames() {
        val m = motion()
        assertFalse(m.active())
        m.step(frame, frame, true)
        assertTrue(m.activity > 0.0)
        m.drawn = 0
        assertFalse(m.active())
    }

    @Test
    fun markEasesToRestThenStops() {
        val m = motion()
        m.setDone(0, true, 0.0)
        assertTrue(m.inLive(0))
        assertTrue(m.active())
        assertEquals(0.0, m.base(0, 0.0))
        val mid = m.base(0, ease / 2.0)
        assertTrue(mid > 0.0 && mid < m.rest[0])
        m.step(ease, frame, false)
        assertFalse(m.active())
        assertEquals(m.rest[0], m.base(0, 1e6))
        assertTrue(m.inLive(0))
        assertFalse(m.inLive(1))
        assertEquals(0.0, m.base(1, 1e6))
    }

    @Test
    fun undoEasesBackAndLeavesTheLivePass() {
        val m = motion()
        m.setDone(0, true, 0.0)
        m.step(ease, frame, false)
        val t0 = 2000.0
        m.setDone(0, false, t0)
        assertTrue(m.inLive(0))
        assertEquals(m.rest[0], m.base(0, t0))
        assertFalse(m.step(t0 + 10.0, frame, false))
        assertTrue(m.step(t0 + ease, frame, false))
        assertFalse(m.inLive(0))
        assertFalse(m.active())
    }

    @Test
    fun reducedMotionHasNoSwayNoShimmerAndInstantColour() {
        val m = motion(reduced = true)
        m.drawn = 1
        m.setDone(0, true, 0.0)
        assertEquals(m.rest[0], m.base(0, 0.0))
        assertFalse(m.active())
        repeat(20) { m.step(it * frame, frame, true) }
        assertEquals(0.0, m.activity)
        assertEquals(m.rest[0], m.shimmered(0, 0.0, 1.0))
        assertFalse(m.active())
        m.setDone(0, false, 0.0)
        assertFalse(m.inLive(0))
    }

    @Test
    fun shimmerStaysInRangeAndOnlyLowersTheColour() {
        val m = motion()
        m.setDone(0, true, 0.0)
        m.step(ease, frame, false)
        repeat(20) { m.step(ease + it * frame, frame, true) }
        val t = m.shimmered(0, ease, 1.0)
        assertTrue(t >= 0.0 && t < m.rest[0])
    }

    @Test
    fun restAndPhaseAreDeterministic() {
        val a = motion()
        val b = motion()
        for (i in 0 until 3) {
            assertEquals(a.rest[i], b.rest[i])
            assertEquals(a.phase[i], b.phase[i])
        }
    }
}
