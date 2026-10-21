package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimTryStep
import dev.kotree.site.sims.core.TryProgressCalc
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TryProgressTest {
    private val steps = trayTry()

    @Test
    fun stepsFinishInOrder() {
        assertEquals(listOf(false, false, false), TryProgressCalc.of(steps, emptyList()).done)
        assertEquals(0, TryProgressCalc.of(steps, emptyList()).current)
        val p = TryProgressCalc.of(steps, listOf("edit-a", "a>tray", "snap"))
        assertEquals(listOf(true, true, false), p.done)
        assertEquals(2, p.current)
        assertEquals("edit-b", p.nextEvent)
        val all = TryProgressCalc.of(steps, listOf("edit-a", "a>tray", "snap", "edit-b", "snap"))
        assertEquals(listOf(true, true, true), all.done)
        assertNull(all.current)
        assertTrue(all.requiredDone)
    }

    @Test
    fun aRefusedEventStillCounts() {
        // The final "snap" is refused by the core, but Try this only reads the history.
        assertTrue(TryProgressCalc.of(steps, listOf("edit-a", "a>tray", "snap", "edit-b", "snap")).requiredDone)
    }

    @Test
    fun eventsBeforeAStepIsReachableDoNotCountForIt() {
        // edit-b and snap came first; step 3 needs them after step 2 is done.
        val p = TryProgressCalc.of(steps, listOf("edit-b", "snap", "edit-a", "a>tray", "snap"))
        assertEquals(listOf(true, true, false), p.done)
        // Step 2 cannot finish before step 1: the drop came first.
        val q = TryProgressCalc.of(steps, listOf("a>tray", "snap", "edit-a"))
        assertEquals(listOf(true, false, false), q.done)
        assertEquals("a>tray", q.nextEvent)
    }

    @Test
    fun aStepNeedsItsEventsInOrder() {
        val p = TryProgressCalc.of(steps, listOf("edit-a", "snap", "a>tray"))
        assertEquals(listOf(true, false, false), p.done)
        assertEquals(1, p.current)
    }

    @Test
    fun anOptionalStepDoesNotBlockOrCount() {
        val withOptional = steps.take(2) + SimTryStep("Extra.", listOf("edit-b"), true)
        val p = TryProgressCalc.of(withOptional, listOf("edit-a", "a>tray", "snap"))
        assertEquals(listOf(true, true, false), p.done)
        assertTrue(p.requiredDone, "the optional step is not needed")
        assertEquals(2, p.current)

        val middle = listOf(steps[0], SimTryStep("Extra.", listOf("edit-b"), true), steps[1])
        val m = TryProgressCalc.of(middle, listOf("edit-a", "a>tray", "snap"))
        assertEquals(listOf(true, false, true), m.done)
        assertTrue(m.requiredDone)
    }
}
