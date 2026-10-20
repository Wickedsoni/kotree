package dev.kotree.build

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.MarkedYAMLException
import org.yaml.snakeyaml.error.YAMLException
import org.yaml.snakeyaml.nodes.MappingNode
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.ScalarNode
import org.yaml.snakeyaml.nodes.SequenceNode
import java.io.File
import java.io.StringReader

private val STEM_HEADING = Regex("^### ([a-z0-9]+(-[a-z0-9]+)*) · (.+)$")
private val LEAF_ROW = Regex("^\\| `([a-z0-9]+(-[a-z0-9]+)*)` \\| (easy|medium|hard) \\|")
private val FRUIT_ROW = Regex("^\\| `([a-z0-9-]+/[a-z0-9-]+/[a-z0-9-]+)` \\|")
private val BACKTICKED_SLUG = Regex("`[a-z0-9]+(-[a-z0-9]+)*`")

internal fun splitLines(text: String): List<String> = text.removePrefix("﻿").split(Regex("\r?\n"))

/** SnakeYAML `compose`: a node tree, scalars stay strings (no implicit types). Returns null for an empty document. */
internal fun composeYaml(text: String): Node? = Yaml().compose(StringReader(text))

/** Line (0-based, inside the YAML text) of a SnakeYAML problem, or 0. */
internal fun yamlErrorLine(e: YAMLException): Int =
    (e as? MarkedYAMLException)?.let { it.problemMark?.line ?: it.contextMark?.line } ?: 0

internal fun yamlErrorText(e: YAMLException): String = (e.message ?: e.javaClass.simpleName).lineSequence().first().trim()

/** The scalar value of [n], or null when it is not a scalar. */
internal fun scalarOf(n: Node?): String? = (n as? ScalarNode)?.value

/**
 * Reads a YAML mapping into key -> value node. Reports non-scalar keys, duplicate keys, unknown keys and missing
 * required keys (all V10). [lineOf] maps a node to a 1-based file line.
 */
internal fun readMapping(
    node: Node,
    allowed: Set<String>,
    required: Set<String>,
    file: String,
    id: String?,
    what: String,
    lineOf: (Node) -> Int,
    out: MutableList<Violation>,
): Map<String, Node>? {
    if (node !is MappingNode) {
        out += Violation("V10", file, lineOf(node), id, "$what must be a YAML mapping")
        return null
    }
    val map = LinkedHashMap<String, Node>()
    for (t in node.value) {
        val key = scalarOf(t.keyNode)
        if (key == null) {
            out += Violation("V10", file, lineOf(t.keyNode), id, "$what has a key that is not a plain word")
            continue
        }
        if (key in map) out += Violation("V10", file, lineOf(t.keyNode), id, "$what repeats the key `$key`")
        if (key !in allowed) out += Violation("V10", file, lineOf(t.keyNode), id, "$what has unknown key `$key`")
        map[key] = t.valueNode
    }
    for (k in required) if (k !in map) out += Violation("V10", file, lineOf(node), id, "$what is missing the key `$k`")
    return map
}

private fun sectionRange(lines: List<String>, heading: String): IntRange? {
    val start = lines.indexOfFirst { it.trimEnd() == heading }
    if (start < 0) return null
    var end = lines.size
    for (i in start + 1 until lines.size) if (lines[i].startsWith("## ")) { end = i; break }
    return (start + 1) until end
}

/** Parses one `_roadmap.md` per docs/14 section 3.1. Returns null only when the YAML block cannot be read at all. */
fun parseRoadmap(f: File, rel: String, dir: String, out: MutableList<Violation>): Roadmap? {
    val lines = splitLines(f.readText(Charsets.UTF_8))
    val fenceIdx = lines.indexOfFirst { it.isNotBlank() }
    if (fenceIdx < 0 || lines[fenceIdx].trimEnd() != "```yaml") {
        out += Violation("V6", rel, fenceIdx + 1, dir, "the first block must be a ```yaml block with only blank lines before it")
        return null
    }
    val closeIdx = (fenceIdx + 1 until lines.size).firstOrNull { lines[it].trimEnd() == "```" }
    if (closeIdx == null) {
        out += Violation("V6", rel, fenceIdx + 1, dir, "the ```yaml block is never closed")
        return null
    }
    val yamlText = lines.subList(fenceIdx + 1, closeIdx).joinToString("\n")
    val baseLine = fenceIdx + 2 // file line of YAML line 0
    val root = try {
        composeYaml(yamlText)
    } catch (e: YAMLException) {
        out += Violation("V6", rel, baseLine + yamlErrorLine(e), dir, "the YAML block is not valid YAML: ${yamlErrorText(e)}")
        return null
    }
    if (root == null) {
        out += Violation("V6", rel, baseLine, dir, "the YAML block is empty")
        return null
    }
    val lineOf: (Node) -> Int = { baseLine + it.startMark.line }

    val top = readMapping(root, setOf("branch", "bough", "stems"), setOf("branch", "bough", "stems"), rel, dir, "the roadmap", lineOf, out)
        ?: return null
    val branch = scalarOf(top["branch"]) ?: ""
    val bough = scalarOf(top["bough"]) ?: ""
    val branchLine = top["branch"]?.let(lineOf) ?: baseLine
    val boughLine = top["bough"]?.let(lineOf) ?: baseLine

    val stems = mutableListOf<RoadmapStem>()
    val stemsNode = top["stems"]
    if (stemsNode != null && stemsNode !is SequenceNode) {
        out += Violation("V10", rel, lineOf(stemsNode), branch, "`stems` must be a list")
    }
    for (sn in (stemsNode as? SequenceNode)?.value ?: emptyList()) {
        val sm = readMapping(sn, setOf("id", "title", "why", "leaves"), setOf("id", "title", "why", "leaves"), rel, branch, "a stem", lineOf, out)
            ?: continue
        val sid = scalarOf(sm["id"]) ?: ""
        val fullStem = "$branch/$sid"
        val leaves = mutableListOf<RoadmapLeaf>()
        val leavesNode = sm["leaves"]
        if (leavesNode != null && leavesNode !is SequenceNode) {
            out += Violation("V10", rel, lineOf(leavesNode), fullStem, "`leaves` must be a list")
        }
        for (ln in (leavesNode as? SequenceNode)?.value ?: emptyList()) {
            val keys = setOf("id", "title", "difficulty", "requires", "fruit")
            val lm = readMapping(ln, keys, keys, rel, fullStem, "a leaf", lineOf, out) ?: continue
            val lid = scalarOf(lm["id"]) ?: ""
            val full = "$fullStem/$lid"
            val diff = scalarOf(lm["difficulty"]) ?: ""
            lm["difficulty"]?.let { n ->
                if (diff !in setOf("easy", "medium", "hard")) {
                    out += Violation("V10", rel, lineOf(n), full, "difficulty `$diff` must be easy, medium or hard")
                }
            }
            val reqNode = lm["requires"]
            val reqs = mutableListOf<String>()
            if (reqNode != null) {
                if (reqNode is SequenceNode) {
                    for (r in reqNode.value) {
                        val s = scalarOf(r)
                        if (s == null) out += Violation("V10", rel, lineOf(r), full, "`requires` entries must be plain IDs") else reqs += s
                    }
                } else {
                    out += Violation("V10", rel, lineOf(reqNode), full, "`requires` must be a list")
                }
            }
            val fruitText = scalarOf(lm["fruit"]) ?: "false"
            lm["fruit"]?.let { n ->
                if (fruitText != "true" && fruitText != "false") out += Violation("V10", rel, lineOf(n), full, "`fruit` must be true or false")
            }
            leaves += RoadmapLeaf(lid, full, scalarOf(lm["title"]) ?: "", diff, reqs, fruitText == "true", lineOf(ln))
        }
        stems += RoadmapStem(sid, fullStem, scalarOf(sm["title"]) ?: "", scalarOf(sm["why"]) ?: "", leaves, lineOf(sn))
    }

    // ---- Markdown sections (everything after the YAML block) ----
    val tableStems = mutableListOf<TableStem>()
    val stemsRange = sectionRange(lines, "## Stems")
    if (stemsRange == null) {
        out += Violation("V6", rel, closeIdx + 1, branch, "the file has no `## Stems` section")
    } else {
        var curId: String? = null
        var curTitle = ""
        var curLine = 0
        var rows = mutableListOf<TableLeafRow>()
        fun flush() {
            curId?.let { tableStems += TableStem(it, curTitle, curLine, rows) }
        }
        for (i in stemsRange) {
            val line = lines[i].trimEnd()
            val h = STEM_HEADING.matchEntire(line)
            if (h != null) {
                flush()
                curId = h.groupValues[1]
                curTitle = h.groupValues[3].trim()
                curLine = i + 1
                rows = mutableListOf()
            } else if (line.startsWith("### ")) {
                out += Violation("V6", rel, i + 1, branch, "stem heading must be `### <slug> · <Title>`: $line")
            } else if (curId != null) {
                LEAF_ROW.find(line)?.let { rows += TableLeafRow(it.groupValues[1], it.groupValues[3], i + 1) }
            }
        }
        flush()
    }

    val fruitRows = mutableListOf<FruitRow>()
    sectionRange(lines, "## Fruits")?.forEach { i ->
        FRUIT_ROW.find(lines[i])?.let { fruitRows += FruitRow(it.groupValues[1], i + 1) }
    }
    sectionRange(lines, "## Later")?.forEach { i ->
        val line = lines[i]
        if (line.startsWith("|")) {
            out += Violation("V6", rel, i + 1, branch, "`## Later` must not contain table rows")
        } else if (BACKTICKED_SLUG.containsMatchIn(line)) {
            out += Violation("V6", rel, i + 1, branch, "`## Later` must not name a backticked slug")
        }
    }
    sectionRange(lines, "## Roadmap changes")?.forEach { i ->
        if (lines[i].startsWith("| `")) {
            out += Violation("V6", rel, i + 1, branch, "`## Roadmap changes` rows must start with a date, not a backtick")
        }
    }

    return Roadmap(rel, dir, branch, bough, stems, tableStems, fruitRows, branchLine, boughLine)
}
