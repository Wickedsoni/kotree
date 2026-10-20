package dev.kotree.site.tree

import dev.kotree.site.tree.hit.Node
import dev.kotree.site.tree.hit.TreeNodes
import kotlinx.browser.window
import org.w3c.dom.events.Event

/**
 * Deep links on `/` (ADR 006). The hash is a node id: `#<bough>`, `#<branch>`, `#<branch>/<stem-slug>` (= stem id) or
 * `#<branch>/<stem-slug>/<leaf-slug>` (= leaf id). An unknown or empty hash gives null: the caller fits the whole tree.
 */
fun nodeFromHash(nodes: TreeNodes): Node? = nodes.byId[window.location.hash.removePrefix("#")]

/** Calls [onTarget] on every `hashchange`. Returns a function that removes the listener. */
fun attachDeepLink(nodes: TreeNodes, onTarget: (Node?) -> Unit): () -> Unit {
    val listener: (Event) -> Unit = { onTarget(nodeFromHash(nodes)) }
    window.addEventListener("hashchange", listener)
    return { window.removeEventListener("hashchange", listener) }
}
