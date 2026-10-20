package dev.kotree.site.lessons

import dev.kotree.site.tree.model.Bough
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.tree.model.treeData

/** A leaf with the nodes above it, read from the compiled tree data (no fetch). */
class LeafInfo(val bough: Bough, val branch: Branch, val stem: Stem, val leaf: Leaf) {
    /** Leaves of the branch in order, across stems. */
    private val inBranch: List<Leaf> get() = branch.stems.flatMap { it.leaves }.sortedBy { it.order }

    /** The nearest live leaf before this one, shadow leaves skipped (docs/03 section 6.3). */
    val previousLive: Leaf? get() = inBranch.lastOrNull { it.order < leaf.order && it.status == LeafStatus.LIVE }

    /** The nearest live leaf after this one. */
    val nextLive: Leaf? get() = inBranch.firstOrNull { it.order > leaf.order && it.status == LeafStatus.LIVE }

    /** The up-next leaf of the branch when it comes after this one: shown as "Coming next" without a link. */
    val comingNext: Leaf? get() = inBranch.firstOrNull { it.order > leaf.order && it.status == LeafStatus.UPNEXT }

    /** "Topic 2 of 5 in <stem>" for the header. */
    val progressLine: String get() = "Topic ${stem.leaves.indexOfFirst { it.id == leaf.id } + 1} of ${stem.leaves.size} in ${stem.title}"
}

private val index: Map<String, LeafInfo> by lazy {
    buildMap {
        for (bough in treeData.boughs) for (branch in bough.branches) for (stem in branch.stems) for (leaf in stem.leaves) {
            put(leaf.id, LeafInfo(bough, branch, stem, leaf))
        }
    }
}

/** The leaf with this full id (`branch/stem/leaf`), or null. */
fun findLeaf(id: String): LeafInfo? = index[id]
