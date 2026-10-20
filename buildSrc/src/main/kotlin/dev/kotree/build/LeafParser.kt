package dev.kotree.build

import org.yaml.snakeyaml.error.YAMLException
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.ScalarNode
import org.yaml.snakeyaml.nodes.SequenceNode
import java.io.File

/**
 * Reads the frontmatter of one leaf file (docs/00 section 8.2). Scalars stay strings; `requires` and `sims`
 * are lists. Shape errors are V10. Returns null only when no frontmatter can be read.
 */
fun parseLeafFile(f: File, rel: String, branch: String, stem: String, out: MutableList<Violation>): LeafFile? {
    val leaf = f.name.removeSuffix(".md")
    val pathId = "$branch/$stem/$leaf"
    val lines = splitLines(f.readText(Charsets.UTF_8))
    if (lines.firstOrNull()?.trimEnd() != "---") {
        out += Violation("V10", rel, 1, pathId, "the file must start with a `---` frontmatter line")
        return null
    }
    val closeIdx = (1 until lines.size).firstOrNull { lines[it].trimEnd() == "---" }
    if (closeIdx == null) {
        out += Violation("V10", rel, 1, pathId, "the frontmatter is never closed with `---`")
        return null
    }
    val yamlText = lines.subList(1, closeIdx).joinToString("\n")
    val baseLine = 2
    val root = try {
        composeYaml(yamlText)
    } catch (e: YAMLException) {
        out += Violation("V10", rel, baseLine + yamlErrorLine(e), pathId, "the frontmatter is not valid YAML: ${yamlErrorText(e)}")
        return null
    }
    val lineOf: (Node) -> Int = { baseLine + it.startMark.line }
    if (root == null) {
        out += Violation("V10", rel, 1, pathId, "the frontmatter is empty")
        return null
    }
    val required = setOf("id", "title", "status", "difficulty", "order", "requires", "summary", "video", "publishedOn", "updatedOn", "sims", "playground", "fruit")
    val map = readMapping(root, required + "upnext", required, rel, pathId, "the frontmatter", lineOf, out) ?: return null

    val fm = LinkedHashMap<String, String>()
    val keyLines = LinkedHashMap<String, Int>()
    var requires: List<String>? = null
    var sims: List<String>? = null
    for ((k, v) in map) {
        keyLines[k] = lineOf(v)
        when (v) {
            is ScalarNode -> {
                fm[k] = v.value
                if (k == "requires" || k == "sims") out += Violation("V10", rel, lineOf(v), pathId, "`$k` must be a list")
            }
            is SequenceNode -> {
                val items = v.value.map { scalarOf(it) }
                if (items.any { it == null }) out += Violation("V10", rel, lineOf(v), pathId, "`$k` entries must be plain strings")
                val strings = items.filterNotNull()
                when (k) {
                    "requires" -> requires = strings
                    "sims" -> sims = strings
                    else -> out += Violation("V10", rel, lineOf(v), pathId, "`$k` must be a single value, not a list")
                }
            }
            else -> out += Violation("V10", rel, lineOf(v), pathId, "`$k` must be a value or a list")
        }
    }
    val body = lines.subList(closeIdx + 1, lines.size).joinToString("\n")
    return LeafFile(rel, branch, stem, leaf, fm, requires, sims, keyLines, body, closeIdx + 2)
}
