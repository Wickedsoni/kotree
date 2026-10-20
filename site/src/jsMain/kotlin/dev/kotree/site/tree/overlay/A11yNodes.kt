package dev.kotree.site.tree.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.camera.ZoomLevel
import dev.kotree.site.tree.hit.Kind
import dev.kotree.site.tree.hit.Node
import dev.kotree.site.tree.hit.TreeNodes
import dev.kotree.site.tree.model.LeafStatus
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/** Attribute that marks the hidden button of a node, so the card can give focus back to it. */
const val NODE_ATTRIBUTE = "data-kt-node"

/**
 * Visually hidden, focusable buttons for the nodes of the current level, in data order (ADR 006): the keyboard and
 * screen-reader twin of the canvas. Built from [ViewState] only, so it recomposes when the level, bough, branch or stem
 * changes and never on a pan or zoom. Focus draws the ring on the canvas ([onFocusNode]); Enter and Space activate like a
 * click. [onListChanged] runs after the list changed so the view can keep focus inside itself.
 */
@Composable
fun A11yNodes(
    nodes: TreeNodes,
    view: State<ViewState>,
    onActivate: (Node) -> Unit,
    onFocusNode: (Node?) -> Unit,
    onListChanged: () -> Unit,
) {
    val v = view.value
    val list = remember(v) { nodesFor(nodes, v) }
    LaunchedEffect(list) { onListChanged() }
    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("width", "${Tokens.Space.hairlinePx}px")
            property("height", "${Tokens.Space.hairlinePx}px")
            property("margin", "-${Tokens.Space.hairlinePx}px")
            property("padding", "0")
            property("overflow", "clip") // clip, not hidden: focus can never scroll this box
            property("clip", "rect(0, 0, 0, 0)")
            property("white-space", "nowrap")
            property("border", "0")
        }.toAttrs { attr("role", "group"); attr("aria-label", "Tree nodes") }
    ) {
        for (n in list) key(n.id) {
            Button(
                Modifier.toAttrs {
                    attr("type", "button")
                    attr("aria-label", labelOf(n))
                    attr(NODE_ATTRIBUTE, n.id)
                    onClick { onActivate(n) }
                    onFocus { onFocusNode(n) }
                    onBlur { onFocusNode(null) }
                }
            ) { Text(n.title) }
        }
    }
}

private fun labelOf(n: Node): String {
    val leaf = n.leaf
    if (leaf != null) {
        return when (leaf.status) {
            LeafStatus.LIVE -> "${n.title}, live"
            LeafStatus.UPNEXT -> "${n.title}, coming next"
            LeafStatus.SHADOW -> "${n.title}, coming soon"
        }
    }
    val counts = n.stem?.counts ?: n.branch?.counts
    if (counts != null && n.kind != Kind.BOUGH) {
        return if (counts.live == 0) "${n.title}, coming soon" else "${n.title}, ${counts.live} of ${counts.total} live"
    }
    return n.title
}

/** The nodes that are clickable at this level (docs/03 §2.3 and ADR 006), in data order. */
private fun nodesFor(nodes: TreeNodes, v: ViewState): List<Node> {
    val out = ArrayList<Node>()
    if (v.level == ZoomLevel.Z0) {
        out += nodes.boughNodes
        return out
    }
    val foundation = nodes.foundation
    if (foundation != null) out += foundation
    if (v.level == ZoomLevel.Z1) {
        val bough = v.boughId
        if (bough != null) for (b in nodes.branchesOfBough[bough].orEmpty()) if (b !== foundation) out += b
        return out
    }
    val branch = v.branchId?.let { nodes.byId[it] } ?: return out
    if (branch !== foundation) out += branch
    for (st in nodes.stemsOfBranch[branch.id].orEmpty()) {
        out += st
        val withLeaves = v.level == ZoomLevel.Z4 || (v.level == ZoomLevel.Z3 && st.id == v.stemId)
        if (withLeaves) out += nodes.leavesOfStem[st.id].orEmpty()
    }
    return out
}
