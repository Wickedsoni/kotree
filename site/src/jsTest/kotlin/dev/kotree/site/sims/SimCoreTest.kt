package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimCore
import dev.kotree.site.sims.core.TextDescription
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimCoreTest {
    private val spec = traySpec()

    @Test
    fun scriptGivesOneSnapshotThenARefuse() {
        val afterSnap = SimCore.fold(spec, listOf("edit-a", "a>tray", "snap"))
        assertEquals(1.0, afterSnap.vars["snaps"])
        assertEquals("shelf", afterSnap.item("s1")?.at)
        assertEquals("Snapshot 1: new A, B carried over", afterSnap.item("s1")?.label)
        assertEquals("folder", afterSnap.item("a")?.at)
        assertEquals("plain", afterSnap.markOf("a"))
        assertEquals("Snapshot 1 holds the new A. B is carried over unchanged.", afterSnap.said)
        assertFalse(afterSnap.refused)

        val refused = SimCore.fold(spec, spec.script)
        assertEquals(1.0, refused.vars["snaps"], "the second snapshot takes nothing")
        assertTrue(refused.refused)
        assertEquals("Nothing is in the tray, so no snapshot is taken. Changed files stay in the folder.", refused.said)
        assertEquals("changed", refused.markOf("b"))
    }

    @Test
    fun aDropOnlyHappensWhenItsConditionHolds() {
        val s = SimCore.fold(spec, listOf("a>tray"))
        assertEquals("folder", s.item("a")?.at)
        assertEquals("File A has no change to stage.", s.said)
        assertTrue(s.refused)
        assertEquals("tray", SimCore.fold(spec, listOf("edit-a", "a>tray")).item("a")?.at)
    }

    @Test
    fun backEqualsFoldOfHistoryMinusOne() {
        val history = spec.script
        var state = SimCore.fold(spec, history)
        for (n in history.size downTo 1) {
            val back = SimCore.fold(spec, history.take(n - 1))
            // Stepping forward from the Back state with the removed event gives the state we came from.
            assertEquals(state, SimCore.step(spec, back, history[n - 1]))
            state = back
        }
    }

    @Test
    fun resetEqualsTheIntroState() {
        val intro = SimCore.fold(spec, emptyList())
        assertEquals(SimCore.initial(spec), intro)
        assertEquals(spec.intro, intro.said)
        assertEquals(0.0, intro.vars["snaps"])
    }

    @Test
    fun unknownEventRefusesWithTheDefault() {
        val s = SimCore.fold(spec, listOf("nope"))
        assertEquals("Nothing changes.", s.said)
        assertTrue(s.refused)
    }

    @Test
    fun aToggleReplaysEarlierActionsWithTheNewValue() {
        val t = specOf(
            "zones",
            """{"title":"t","intro":"i","vars":{"n":1},
              "toggles":[{"id":"double","label":"Double it","var":"mode"}],
              "sliders":[{"id":"step","label":"Step","var":"by","min":1,"max":9,"step":1}],
              "actions":[{"id":"go","label":"Go","cases":[
                {"when":{"var":{"mode":"on"}},"do":[{"add":{"n":10}},{"say":"Ten. n is {n}."}]},
                {"do":[{"add":{"n":1}},{"say":"One. n is {n}."}]}]}]}""",
        )
        assertEquals(2.0, SimCore.fold(t, listOf("go")).vars["n"])
        assertEquals(11.0, SimCore.fold(t, listOf("go", "double=on")).vars["n"], "the setting reaches back over the action")
        assertEquals(11.0, SimCore.fold(t, listOf("double=on", "go")).vars["n"])
        assertEquals(2.0, SimCore.fold(t, listOf("go", "double=on", "double=off")).vars["n"])
        assertEquals(4.0, SimCore.fold(t, listOf("step=4")).vars["by"])
        assertEquals("Double it: on.", SimCore.fold(t, listOf("double=on")).said)
    }

    @Test
    fun yamlOnBecomesTheTextOn() {
        val t = specOf("zones", """{"title":"t","intro":"i","vars":{"flag":true}}""")
        assertEquals("on", t.vars["flag"])
    }

    @Test
    fun textDescriptionRunsTheScriptThroughTheCore() {
        val lines = TextDescription.lines(spec)
        assertEquals(spec.intro, lines.first())
        assertEquals(1 + spec.script.size + 3, lines.size)
        assertEquals("File A is in the tray.", lines[2])
        assertTrue(lines.last().startsWith("Try this, step 3: "))
        assertTrue(lines.none { it.contains("<") })
    }

    @Test
    fun lanesQueueAndStackPop() {
        val t = specOf(
            "lanes",
            """{"title":"t","intro":"i",
              "lanes":[{"id":"q","label":"Queue","kind":"queue","slots":2},{"id":"st","label":"Stack","kind":"stack","slots":2}],
              "items":[{"id":"x","label":"X","at":"q.0"},{"id":"y","label":"Y","at":"q.1"},{"id":"z","label":"Z","at":"st.0"},{"id":"w","label":"W","at":"st.1"}],
              "actions":[
               {"id":"deq","label":"Dequeue","cases":[{"do":[{"pop":"q"},{"say":"Front left."}]}]},
               {"id":"pop-st","label":"Pop","cases":[{"do":[{"pop":"st"},{"say":"Top left."}]}]},
               {"id":"push-z","label":"Push Z","cases":[{"do":[{"push":{"q":"z"}},{"say":"Z joined."}]}]}]}""",
        )
        val q = SimCore.fold(t, listOf("deq"))
        assertEquals(null, q.item("x"))
        assertEquals("q.0", q.item("y")?.at)
        val st = SimCore.fold(t, listOf("pop-st"))
        assertEquals(null, st.item("w"))
        assertEquals("st.0", st.item("z")?.at)
        assertTrue(SimCore.fold(t, listOf("push-z")).refused, "the queue is full")
        assertEquals("q.1", SimCore.fold(t, listOf("deq", "push-z")).item("z")?.at)
        assertTrue(SimCore.fold(t, listOf("pop-st", "pop-st", "pop-st")).refused, "the stack is empty")
    }
}
