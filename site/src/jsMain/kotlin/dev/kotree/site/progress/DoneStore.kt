package dev.kotree.site.progress

import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import kotlinx.browser.localStorage

/** The one localStorage key of the visitor's done marks (ADR 008 §Progress). Value: leaf ids, sorted, joined by a newline. */
const val DONE_KEY = "kotree.done.v1"

private const val SEPARATOR = '\n'

/** Key of a leaf in the done store: its path without the leading slash. Only live leaves have one. */
fun leafDoneId(leaf: Leaf?): String? {
    if (leaf == null || leaf.status != LeafStatus.LIVE) return null
    return leaf.path?.removePrefix("/")
}

/** A tiny string store. A null value removes the key. */
interface KeyValue {
    fun read(key: String): String?
    fun write(key: String, value: String?)
}

/** In-memory store: the fallback when localStorage is blocked, and the store of tests. */
class MemoryKv : KeyValue {
    private val map = HashMap<String, String>()

    override fun read(key: String): String? = map[key]

    override fun write(key: String, value: String?) {
        if (value == null) map.remove(key) else map[key] = value
    }
}

/**
 * localStorage behind try/catch (private mode, blocked storage and a full quota all throw). After the first failure it
 * uses an in-memory map for the rest of the session, so marking still works without a console error.
 */
class LocalStorageKv : KeyValue {
    private val memory = MemoryKv()
    private var failed = false

    override fun read(key: String): String? {
        if (!failed) {
            try {
                return localStorage.getItem(key)
            } catch (e: Throwable) {
                failed = true
            }
        }
        return memory.read(key)
    }

    override fun write(key: String, value: String?) {
        if (!failed) {
            try {
                if (value == null) localStorage.removeItem(key) else localStorage.setItem(key, value)
                return
            } catch (e: Throwable) {
                failed = true
            }
        }
        memory.write(key, value)
    }
}

/**
 * The set of leaves the visitor marked done. Only ids in [liveIds] are accepted; garbage, duplicates and unknown ids in
 * the stored value are dropped (and the value is rewritten on the next change).
 */
class DoneStore(private val kv: KeyValue, private val liveIds: Set<String>) {
    private val done = HashSet<String>()

    val count: Int get() = done.size

    init {
        reload()
    }

    fun isDone(id: String): Boolean = id in done

    fun doneIds(): Set<String> = done

    /** Marks or unmarks [id]. Returns false, and writes nothing, when [id] is not a live leaf. */
    fun setDone(id: String, done: Boolean): Boolean {
        if (id !in liveIds) return false
        val changed = if (done) this.done.add(id) else this.done.remove(id)
        if (changed) persist()
        return true
    }

    /** Reads the store again (a `storage` event from another tab). */
    fun reload() {
        done.clear()
        val raw = kv.read(DONE_KEY) ?: return
        for (part in raw.split(SEPARATOR)) {
            val id = part.trim()
            if (id in liveIds) done.add(id)
        }
    }

    private fun persist() {
        kv.write(DONE_KEY, if (done.isEmpty()) null else done.sorted().joinToString(SEPARATOR.toString()))
    }
}
