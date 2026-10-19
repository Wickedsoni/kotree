package dev.kotree.site.tree.model

/** Tree data, same field names as docs/03 §4.2 (schema 1). Phase 3 swaps only the source. */
data class TreeData(
    val schema: Int,
    val seed: Long,
    val boughs: List<Bough>,
    val counts: Counts,
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
    val order: Int,
    /** Colour token name, e.g. "color.branch.kotlin". */
    val accent: String,
    /** Empty in the prototype: no leaves, nothing can be mistaken for published content. */
    val stems: List<Stem>,
)

/** Placeholder shape; real stem fields arrive with Phase 3. */
data class Stem(val id: String, val title: String, val order: Int)
