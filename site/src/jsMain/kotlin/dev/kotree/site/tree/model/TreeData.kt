package dev.kotree.site.tree.model

/**
 * Tree data, same field names as docs/03 §4.2 (schema 1). The values come from the generated
 * `GeneratedTreeData.kt` (`treeData`), written by the `generateTreeData` Gradle task.
 */
data class TreeData(
    val schema: Int,
    val seed: Long,
    /** Full leaf ID of the "Start here" leaf (docs/03 §5). */
    val startHere: String,
    val counts: Counts,
    val boughs: List<Bough>,
)

data class Counts(val live: Int, val total: Int)

data class Bough(
    val id: String,
    val title: String,
    val order: Int,
    val branches: List<Branch>,
)

data class Branch(
    val id: String,
    val title: String,
    /** 0-based position across all 17 branches (docs/00 §5). */
    val order: Int,
    /** Colour token name, e.g. "color.branch.kotlin". */
    val accent: String,
    val counts: Counts,
    /** Full leaf ID of the up-next leaf; null when every leaf of the branch is live. */
    val upNext: String?,
    val stems: List<Stem>,
)

data class Stem(
    val id: String,
    val slug: String,
    val title: String,
    val why: String,
    val order: Int,
    val counts: Counts,
    val leaves: List<Leaf>,
)

enum class LeafStatus { SHADOW, UPNEXT, LIVE }

enum class Difficulty { EASY, MEDIUM, HARD }

data class Leaf(
    val id: String,
    val slug: String,
    val title: String,
    val status: LeafStatus,
    val difficulty: Difficulty,
    /** 1-based position in the branch, YAML order. */
    val order: Int,
    val requires: List<String>,
    val fruit: Boolean,
    /** Site-root path; null unless live. */
    val path: String?,
    val summary: String?,
    val video: String?,
    val publishedOn: String?,
)
