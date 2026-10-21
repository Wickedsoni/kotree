package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimCore
import dev.kotree.site.sims.layouts.endCue
import dev.kotree.site.sims.layouts.slotContents
import dev.kotree.site.sims.layouts.slotNumber
import dev.kotree.site.sims.layouts.visibleLogRows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val LANES_JSON = """{
"title":"Queue and stack","intro":"Both lanes hold p, q, r.",
"lanes":[{"id":"q","label":"Queue","kind":"queue","slots":3,"index0":true},{"id":"s","label":"Stack","kind":"stack","slots":3}],
"items":[{"id":"p","label":"P","at":"q.0"},{"id":"q1","label":"Q","at":"q.1"},{"id":"r","label":"R","at":"q.2"},
 {"id":"x","label":"X","at":"s.0"},{"id":"y","label":"Y","at":"s.1"},{"id":"z","label":"Z","at":"loose"}],
"actions":[
 {"id":"dq","label":"Take from queue","cases":[{"do":[{"pop":"q"},{"say":"Front left."}]}]},
 {"id":"pop-s","label":"Pop stack","cases":[{"do":[{"pop":"s"},{"say":"Top left."}]}]},
 {"id":"push-q","label":"Join queue","cases":[{"do":[{"push":{"q":"z"}},{"say":"Joined."}]}],"refuse":"Queue is full."},
 {"id":"push-s","label":"Push stack","cases":[{"do":[{"push":{"s":"z"}},{"say":"Pushed."}]}],"refuse":"Stack is full."}
]}"""

class LanesTest {
    private val spec = specOf("lanes", LANES_JSON)

    @Test
    fun queuePopTakesTheFrontAndShifts() {
        val s = SimCore.fold(spec, listOf("dq"))
        assertNull(s.item("p"))
        assertEquals("q.0", s.item("q1")?.at)
        assertEquals("q.1", s.item("r")?.at)
    }

    @Test
    fun stackPopTakesTheTop() {
        val s = SimCore.fold(spec, listOf("pop-s"))
        assertNull(s.item("y"))
        assertEquals("s.0", s.item("x")?.at)
    }

    @Test
    fun pushIntoAFullLaneRefusesAndChangesNothing() {
        val s = SimCore.fold(spec, listOf("push-q"))
        assertTrue(s.refused)
        assertEquals("Queue is full.", s.said)
        assertEquals("loose", s.item("z")?.at)
        val ok = SimCore.fold(spec, listOf("push-s"))
        assertEquals("s.2", ok.item("z")?.at)
        val again = SimCore.fold(spec, listOf("push-s", "push-s"))
        assertTrue(again.refused)
        assertEquals("s.2", again.item("z")?.at)
    }

    @Test
    fun slotsAndCuesComeFromTheState() {
        val lane = spec.lanes.getValue("q")
        val contents = slotContents(SimCore.initial(spec), lane)
        assertEquals(listOf("p", "q1", "r"), contents.map { it?.id })
        assertEquals("front", endCue(lane, 0, contents))
        assertEquals("back", endCue(lane, 2, contents))
        assertNull(endCue(lane, 1, contents))
        assertEquals(0, slotNumber(lane, 0))
        assertEquals(1, slotNumber(spec.lanes.getValue("s"), 0))
        val stack = spec.lanes.getValue("s")
        assertEquals("top", endCue(stack, 1, slotContents(SimCore.initial(spec), stack)))
    }

    @Test
    fun logRowsKeepTheNewestAndFitTheColumns() {
        val rows = (1..10).map { listOf("$it") }
        val shown = visibleLogRows(rows, 2)
        assertEquals(8, shown.size)
        assertEquals(listOf("3", ""), shown.first())
    }
}
