package dev.kotree.site.progress

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** ADR 008 WP5: the done store. Pure Kotlin over [MemoryKv], no DOM. */
class DoneStoreTest {
    private val live = setOf("a/one", "a/two", "b/three")

    @Test
    fun marksPersistSortedAndReload() {
        val kv = MemoryKv()
        val store = DoneStore(kv, live)
        assertTrue(store.setDone("b/three", true))
        assertTrue(store.setDone("a/one", true))
        assertEquals("a/one\nb/three", kv.read(DONE_KEY))
        assertEquals(2, store.count)
        val again = DoneStore(kv, live)
        assertTrue(again.isDone("a/one"))
        assertFalse(again.isDone("a/two"))
        assertEquals(setOf("a/one", "b/three"), again.doneIds())
    }

    @Test
    fun undoRemovesAndEmptyClearsKey() {
        val kv = MemoryKv()
        val store = DoneStore(kv, live)
        store.setDone("a/one", true)
        assertTrue(store.setDone("a/one", false))
        assertFalse(store.isDone("a/one"))
        assertNull(kv.read(DONE_KEY))
        assertEquals(0, store.count)
    }

    @Test
    fun unknownIdIsRejectedWithoutWriting() {
        val kv = MemoryKv()
        val store = DoneStore(kv, live)
        assertFalse(store.setDone("a/missing", true))
        assertNull(kv.read(DONE_KEY))
        assertEquals(0, store.count)
    }

    @Test
    fun garbageDuplicatesAndUnknownIdsAreDropped() {
        val kv = MemoryKv()
        kv.write(DONE_KEY, "a/one\n\n  \nnope\na/one\n{\"x\":1}\nb/three")
        val store = DoneStore(kv, live)
        assertEquals(setOf("a/one", "b/three"), store.doneIds())
        store.setDone("a/two", true)
        assertEquals("a/one\na/two\nb/three", kv.read(DONE_KEY))
    }

    @Test
    fun reloadSeesAnotherTab() {
        val kv = MemoryKv()
        val store = DoneStore(kv, live)
        kv.write(DONE_KEY, "a/two")
        store.reload()
        assertTrue(store.isDone("a/two"))
        kv.write(DONE_KEY, null)
        store.reload()
        assertEquals(0, store.count)
    }

    @Test
    fun failingStorageNeverThrows() {
        val kv = object : KeyValue {
            override fun read(key: String): String? = null
            override fun write(key: String, value: String?) {}
        }
        val store = DoneStore(kv, live)
        assertTrue(store.setDone("a/one", true))
        assertTrue(store.isDone("a/one"))
    }

    @Test
    fun localStorageWithoutBrowserFallsBackToMemory() {
        // On Node there is no localStorage: every access throws, and the wrapper must switch to memory silently.
        val kv = LocalStorageKv()
        assertNull(kv.read(DONE_KEY))
        kv.write(DONE_KEY, "a/one")
        assertEquals("a/one", kv.read(DONE_KEY))
        kv.write(DONE_KEY, null)
        assertNull(kv.read(DONE_KEY))
    }
}
