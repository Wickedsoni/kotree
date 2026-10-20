package dev.kotree.build

/** Build-side tree model, field for field the schema of docs/03 section 4.2. Both emitters read only this. */
const val TREE_SCHEMA: Int = 1

/** Layout seed (D1), the same constant as `Tokens.World.seed` in the site. */
const val TREE_SEED: Long = 20261017L

data class TreeCounts(val live: Int, val total: Int)

data class TreeLeaf(
    val id: String,
    val slug: String,
    val title: String,
    val status: String, // shadow | upnext | live
    val difficulty: String, // easy | medium | hard
    val order: Int,
    val requires: List<String>,
    val fruit: Boolean,
    val path: String?,
    val summary: String?,
    val video: String?,
    val publishedOn: String?,
)

data class TreeStem(
    val id: String,
    val slug: String,
    val title: String,
    val why: String,
    val order: Int,
    val counts: TreeCounts,
    val leaves: List<TreeLeaf>,
)

data class TreeBranch(
    val id: String,
    val title: String,
    val order: Int,
    val accent: String,
    val counts: TreeCounts,
    val upNext: String?,
    val stems: List<TreeStem>,
)

data class TreeBough(val id: String, val title: String, val order: Int, val branches: List<TreeBranch>)

data class TreeModel(
    val schema: Int,
    val seed: Long,
    val startHere: String,
    val counts: TreeCounts,
    val boughs: List<TreeBough>,
)

private fun String?.orNullIfBlank(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

/**
 * docs/14 section 3.3 and docs/03 section 3.1. Assumes [validate] passed, but never throws on a
 * missing roadmap: that branch just has no stems.
 */
fun buildTree(c: Content): TreeModel {
    val files = c.leafFiles.associateBy { it.pathId }
    val roadmaps = c.roadmaps.associateBy { it.branch }

    val boughs = BOUGHS.mapIndexed { boughOrder, bd ->
        val branches = BRANCHES.withIndex().filter { it.value.bough == bd.slug }.map { (branchOrder, def) ->
            buildBranch(def, branchOrder, roadmaps[def.slug], files)
        }
        TreeBough(bd.slug, bd.title, boughOrder, branches)
    }
    val all = boughs.flatMap { it.branches }
    val foundation = all.firstOrNull { it.id == "foundation" }
    val foundationLeaves = foundation?.stems?.flatMap { it.leaves }.orEmpty()
    val startHere = (foundationLeaves.firstOrNull { it.status == "live" }
        ?: foundationLeaves.firstOrNull { it.status == "upnext" }
        ?: foundationLeaves.firstOrNull())?.id ?: ""
    return TreeModel(
        TREE_SCHEMA, TREE_SEED, startHere,
        TreeCounts(all.sumOf { it.counts.live }, all.sumOf { it.counts.total }),
        boughs,
    )
}

private fun buildBranch(def: BranchDef, branchOrder: Int, roadmap: Roadmap?, files: Map<String, LeafFile>): TreeBranch {
    val stemsIn = roadmap?.stems.orEmpty()
    val flat = stemsIn.flatMap { s -> s.leaves.map { it } }

    fun isLive(l: RoadmapLeaf): Boolean {
        val f = files[l.fullId] ?: return false
        return f.fm["id"] == l.fullId && f.fm["status"]?.trim() == "live"
    }

    val nonLive = flat.filter { !isLive(it) }
    val upNextId: String? = (nonLive.firstOrNull { files[it.fullId]?.fm?.get("upnext")?.trim() == "true" }
        ?: nonLive.firstOrNull())?.fullId

    var leafOrder = 0
    val stems = stemsIn.mapIndexed { si, s ->
        val leaves = s.leaves.map { l ->
            leafOrder++
            val live = isLive(l)
            val fm = files[l.fullId]?.fm.orEmpty()
            TreeLeaf(
                id = l.fullId,
                slug = l.id,
                title = l.title,
                status = when {
                    live -> "live"
                    l.fullId == upNextId -> "upnext"
                    else -> "shadow"
                },
                difficulty = l.difficulty,
                order = leafOrder,
                requires = l.requires,
                fruit = l.fruit,
                path = if (live) "/${l.fullId}" else null,
                summary = if (live) fm["summary"].orNullIfBlank() else null,
                video = if (live) fm["video"].orNullIfBlank() else null,
                publishedOn = if (live) fm["publishedOn"].orNullIfBlank() else null,
            )
        }
        TreeStem(
            id = s.fullId, slug = s.id, title = s.title, why = s.why, order = si + 1,
            counts = TreeCounts(leaves.count { it.status == "live" }, leaves.size),
            leaves = leaves,
        )
    }
    return TreeBranch(
        id = def.slug, title = def.title, order = branchOrder, accent = "color.branch.${def.slug}",
        counts = TreeCounts(stems.sumOf { it.counts.live }, stems.sumOf { it.counts.total }),
        upNext = upNextId, stems = stems,
    )
}
