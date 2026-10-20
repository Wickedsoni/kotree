package dev.kotree.build

import java.io.File

/** One rule failure. Printed as `V<n> <file>:<line> [<id>] <message>` (docs/14 section 3.2). */
data class Violation(val rule: String, val file: String, val line: Int, val id: String?, val message: String) {
    fun format(): String = "$rule $file:$line [${id ?: "-"}] $message"
}

/** One leaf of a roadmap YAML block. [id] is the slug; [fullId] is `branch/stem/leaf`. */
data class RoadmapLeaf(
    val id: String,
    val fullId: String,
    val title: String,
    val difficulty: String,
    val requires: List<String>,
    val fruit: Boolean,
    val line: Int,
)

data class RoadmapStem(
    val id: String,
    val fullId: String,
    val title: String,
    val why: String,
    val leaves: List<RoadmapLeaf>,
    val line: Int,
)

/** A leaf row of a `## Stems` table: only the first two cells are read. */
data class TableLeafRow(val slug: String, val difficulty: String, val line: Int)

/** A `### slug · Title` section of `## Stems`. */
data class TableStem(val id: String, val title: String, val line: Int, val rows: List<TableLeafRow>)

/** A row of `## Fruits`: a full ID. */
data class FruitRow(val fullId: String, val line: Int)

/** Everything read from one `_roadmap.md`. [dir] is the content folder name; [branch] is the YAML value. */
data class Roadmap(
    val file: String,
    val dir: String,
    val branch: String,
    val bough: String,
    val stems: List<RoadmapStem>,
    val tableStems: List<TableStem>,
    val fruitRows: List<FruitRow>,
    val branchLine: Int,
    val boughLine: Int,
)

/** A leaf file `content/<branch>/<stem>/<leaf>.md`. [fm] holds every frontmatter scalar as a string. */
data class LeafFile(
    val file: String,
    val branch: String,
    val stem: String,
    val leaf: String,
    val fm: Map<String, String>,
    val requires: List<String>?,
    val sims: List<String>?,
    val fmLines: Map<String, Int>,
    val body: String,
    val bodyStartLine: Int,
) {
    val pathId: String get() = "$branch/$stem/$leaf"
    fun line(key: String): Int = fmLines[key] ?: 1
}

/** Result of [loadContent]. [parseViolations] are returned again by [validate]. */
data class Content(
    val roadmaps: List<Roadmap>,
    val leafFiles: List<LeafFile>,
    val folders: List<String>,
    val parseViolations: List<Violation>,
)

/** Reads every roadmap and leaf file under [contentDir]. Parse problems become [Violation]s, never exceptions. */
fun loadContent(contentDir: File): Content {
    val violations = mutableListOf<Violation>()
    val prefix = contentDir.name
    val folders = (contentDir.listFiles { f -> f.isDirectory }?.map { it.name } ?: emptyList()).sorted()
    val roadmaps = mutableListOf<Roadmap>()
    val leaves = mutableListOf<LeafFile>()
    for (dir in folders) {
        val branchDir = File(contentDir, dir)
        if (dir.startsWith("_") || dir in RESERVED_FIRST_SEGMENTS) continue // V7 reports these
        val roadmapFile = File(branchDir, "_roadmap.md")
        if (roadmapFile.isFile) {
            parseRoadmap(roadmapFile, "$prefix/$dir/_roadmap.md", dir, violations)?.let { roadmaps += it }
        }
        branchDir.listFiles { f -> f.isDirectory }?.sortedBy { it.name }?.forEach { stemDir ->
            stemDir.listFiles { f -> f.isFile && f.name.endsWith(".md") && !f.name.startsWith("_") }
                ?.sortedBy { it.name }?.forEach { f ->
                    parseLeafFile(f, "$prefix/$dir/${stemDir.name}/${f.name}", dir, stemDir.name, violations)
                        ?.let { leaves += it }
                }
        }
    }
    return Content(roadmaps, leaves, folders, violations)
}
