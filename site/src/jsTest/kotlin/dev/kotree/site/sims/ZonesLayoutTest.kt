package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimCore
import dev.kotree.site.sims.layouts.Box
import dev.kotree.site.sims.layouts.boxAt
import dev.kotree.site.sims.layouts.draggableItems
import dev.kotree.site.sims.layouts.dropEvent
import dev.kotree.site.sims.layouts.markCue
import dev.kotree.site.sims.layouts.zonesOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ZonesLayoutTest {
    @Test
    fun zonesAndDraggablesComeFromTheSpec() {
        val spec = traySpec()
        assertEquals(listOf("folder", "tray", "shelf"), zonesOf(spec).map { it.id })
        assertEquals(setOf("a", "b"), draggableItems(spec))
        assertEquals("a>tray", dropEvent("a", "tray"))
    }

    @Test
    fun hitTestPicksTheZoneUnderThePointer() {
        val boxes = listOf(Box("folder", 0.0, 0.0, 100.0, 100.0), Box("tray", 110.0, 0.0, 210.0, 100.0))
        assertEquals("tray", boxAt(boxes, 150.0, 50.0))
        assertNull(boxAt(boxes, 105.0, 50.0))
        assertNull(boxAt(boxes, 50.0, 150.0))
    }

    @Test
    fun mouseAndKeyboardFireTheSameEventAndItRefusesWithoutAChange() {
        val spec = traySpec()
        val event = dropEvent("a", "tray")
        val refused = SimCore.fold(spec, listOf(event))
        assertTrue(refused.refused)
        assertEquals("folder", refused.item("a")?.at)
        val ok = SimCore.fold(spec, listOf("edit-a", event))
        assertEquals("tray", ok.item("a")?.at)
    }

    @Test
    fun everyMarkExceptPlainHasATextCue() {
        assertNull(markCue("plain"))
        listOf("changed", "good", "bad", "ghost", "picked").forEach { assertTrue(markCue(it) != null) }
    }
}
