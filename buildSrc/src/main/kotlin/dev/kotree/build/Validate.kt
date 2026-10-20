package dev.kotree.build

import java.time.LocalDate
import java.time.format.DateTimeParseException

private val DIFFICULTIES = setOf("easy", "medium", "hard")
private val STATUSES = setOf("shadow", "upnext", "live")
private val FULL_ID = Regex("^[a-z0-9]+(-[a-z0-9]+)*/[a-z0-9]+(-[a-z0-9]+)*/[a-z0-9]+(-[a-z0-9]+)*$")
private val SIM_MARKER = Regex("^<!-- SIM: [a-z0-9]+(-[a-z0-9]+)* -->$")
private val DATE = Regex("^\\d{4}-\\d{2}-\\d{2}$")
private val RAW_HTML = Regex("<!--|</?[A-Za-z][A-Za-z0-9-]*[\\s/>]")
private val INLINE_CODE = Regex("`[^`]*`")

/** Prints at most [max] violations (docs/14 section 3.2), then a count of the rest. */
fun renderViolations(list: List<Violation>, max: Int = 50): String {
    val lines = list.take(max).map { it.format() }.toMutableList()
    if (list.size > max) lines += "... and ${list.size - max} more"
    return lines.joinToString("\n")
}

/** Runs V1-V12 (docs/14 section 3.2). [baselineLive] is null when V8 is skipped. Order is deterministic. */
fun validate(c: Content, baselineLive: Set<String>?): List<Violation> {
    val v = ArrayList<Violation>(c.parseViolations)
    v7(c, v)
    for (r in c.roadmaps) {
        v1(r, v)
        v2(r, v)
        v3(r, v)
        v6(r, v)
        v11(r, v)
    }
    val roadmapByBranch = c.roadmaps.associateBy { it.branch }
    val leafIndex = HashMap<String, RoadmapLeaf>()
    for (r in c.roadmaps) for (s in r.stems) for (l in s.leaves) leafIndex.putIfAbsent(l.fullId, l)
    for (f in c.leafFiles) {
        v4v5(f, roadmapByBranch[f.branch], leafIndex, v)
        v10(f, v)
        if (f.fm["status"] == "live") v12(f, v)
    }
    v9(c, v)
    if (baselineLive != null) v8(c, baselineLive, v)
    return v
}

private fun v7(c: Content, v: MutableList<Violation>) {
    for (name in c.folders) {
        val file = "content/$name"
        if (name == "_templates") continue
        if (name.startsWith("_")) {
            v += Violation("V7", file, 1, name, "content folder `$name` starts with `_`, which is reserved")
        } else if (name in RESERVED_FIRST_SEGMENTS) {
            v += Violation("V7", file, 1, name, "content folder `$name` is a reserved first path segment")
        } else if (branchDef(name) == null) {
            v += Violation("V1", file, 1, name, "content folder `$name` is not a branch in the registry")
        }
    }
}

private fun v1(r: Roadmap, v: MutableList<Violation>) {
    val def = branchDef(r.branch)
    if (def == null) {
        v += Violation("V1", r.file, r.branchLine, r.branch, "branch `${r.branch}` is not in the registry")
        return
    }
    if (r.dir != r.branch) v += Violation("V1", r.file, r.branchLine, r.branch, "branch `${r.branch}` sits in folder `${r.dir}`")
    if (BOUGHS.none { it.slug == r.bough }) {
        v += Violation("V1", r.file, r.boughLine, r.branch, "bough `${r.bough}` is not in the registry")
    } else if (def.bough != r.bough) {
        v += Violation("V1", r.file, r.boughLine, r.branch, "branch `${r.branch}` belongs to bough `${def.bough}`, not `${r.bough}`")
    }
}

private fun v2(r: Roadmap, v: MutableList<Violation>) {
    val stemSeen = HashSet<String>()
    val leafSeen = HashSet<String>()
    for (s in r.stems) {
        if (!SLUG_REGEX.matches(s.id)) v += Violation("V2", r.file, s.line, s.fullId, "stem slug `${s.id}` is not lowercase kebab-case")
        if (!stemSeen.add(s.id)) v += Violation("V2", r.file, s.line, s.fullId, "duplicate stem slug `${s.id}` in branch `${r.branch}`")
        for (l in s.leaves) {
            if (!SLUG_REGEX.matches(l.id)) v += Violation("V2", r.file, l.line, l.fullId, "leaf slug `${l.id}` is not lowercase kebab-case")
            if (!leafSeen.add(l.id)) v += Violation("V2", r.file, l.line, l.fullId, "duplicate leaf slug `${l.id}` in branch `${r.branch}`")
        }
    }
}

private fun v3(r: Roadmap, v: MutableList<Violation>) {
    val order = HashMap<String, Int>()
    r.stems.flatMap { it.leaves }.forEachIndexed { i, l -> order.putIfAbsent(l.fullId, i) }
    r.stems.flatMap { it.leaves }.forEachIndexed { i, l ->
        for (req in l.requires) {
            val msg = when {
                !FULL_ID.matches(req) -> "requires `$req` is not a full ID (<branch>/<stem>/<leaf>)"
                !req.startsWith(r.branch + "/") -> "requires `$req`, a leaf of another branch"
                req !in order -> "requires `$req`, which is not a leaf of this roadmap"
                order.getValue(req) >= i -> "requires `$req`, which is listed later (or is the leaf itself)"
                else -> null
            }
            if (msg != null) v += Violation("V3", r.file, l.line, l.fullId, msg)
        }
    }
}

private fun v6(r: Roadmap, v: MutableList<Violation>) {
    val yamlIds = r.stems.map { it.id }
    val tableIds = r.tableStems.map { it.id }
    if (yamlIds != tableIds) {
        v += Violation("V6", r.file, r.tableStems.firstOrNull()?.line ?: r.stems.firstOrNull()?.line ?: 1, r.branch,
            "stem order or ids differ: YAML $yamlIds, headings $tableIds")
    }
    val tableById = r.tableStems.associateBy { it.id }
    for (s in r.stems) {
        val t = tableById[s.id] ?: continue
        if (t.title != s.title) {
            v += Violation("V6", r.file, t.line, s.fullId, "heading title `${t.title}` differs from YAML title `${s.title}`")
        }
        val yamlLeaves = s.leaves.map { it.id }
        val rowLeaves = t.rows.map { it.slug }
        if (yamlLeaves != rowLeaves) {
            val missing = yamlLeaves.filter { it !in rowLeaves }
            val extra = rowLeaves.filter { it !in yamlLeaves }
            val why = when {
                missing.isNotEmpty() -> "table is missing leaf rows $missing"
                extra.isNotEmpty() -> "table has extra leaf rows $extra"
                else -> "table leaf rows are in a different order than YAML"
            }
            v += Violation("V6", r.file, t.line, s.fullId, why)
        } else {
            for ((l, row) in s.leaves.zip(t.rows)) {
                if (l.difficulty != row.difficulty) {
                    v += Violation("V6", r.file, row.line, l.fullId, "Diff `${row.difficulty}` differs from YAML difficulty `${l.difficulty}`")
                }
            }
        }
    }
    val yamlFruit = r.stems.flatMap { it.leaves }.filter { it.fruit }.map { it.fullId }.toSet()
    val rowFruit = r.fruitRows.map { it.fullId }.toSet()
    if (yamlFruit != rowFruit) {
        val line = r.fruitRows.firstOrNull()?.line ?: 1
        v += Violation("V6", r.file, line, r.branch,
            "fruit set differs: YAML only ${(yamlFruit - rowFruit).sorted()}, ## Fruits only ${(rowFruit - yamlFruit).sorted()}")
    }
}

private fun v11(r: Roadmap, v: MutableList<Violation>) {
    fun bad(slug: String) = slug == "index" || slug.firstOrNull()?.isDigit() == true
    for (s in r.stems) {
        if (bad(s.id)) v += Violation("V11", r.file, s.line, s.fullId, "stem slug `${s.id}` is `index` or starts with a digit")
        for (l in s.leaves) {
            if (bad(l.id)) v += Violation("V11", r.file, l.line, l.fullId, "leaf slug `${l.id}` is `index` or starts with a digit")
        }
    }
}

private fun v4v5(f: LeafFile, r: Roadmap?, index: Map<String, RoadmapLeaf>, v: MutableList<Violation>) {
    val pathId = f.pathId
    f.fm["id"]?.let { id ->
        if (id != pathId) v += Violation("V4", f.file, f.line("id"), pathId, "frontmatter id `$id` does not equal the path `$pathId`")
    }
    val row = index[pathId]
    if (r == null || row == null) {
        v += Violation("V4", f.file, 1, pathId, "leaf file has no row in the roadmap of branch `${f.branch}`")
        return
    }
    f.fm["title"]?.let { if (it != row.title) v += Violation("V5", f.file, f.line("title"), pathId, "title differs from the roadmap: `${row.title}`") }
    f.fm["difficulty"]?.let {
        if (it != row.difficulty) v += Violation("V5", f.file, f.line("difficulty"), pathId, "difficulty `$it` differs from the roadmap `${row.difficulty}`")
    }
    f.requires?.let {
        if (it != row.requires) v += Violation("V5", f.file, f.line("requires"), pathId, "requires $it differs from the roadmap ${row.requires}")
    }
    f.fm["fruit"]?.let {
        if (it != row.fruit.toString()) v += Violation("V5", f.file, f.line("fruit"), pathId, "fruit `$it` differs from the roadmap `${row.fruit}`")
    }
}

private fun v10(f: LeafFile, v: MutableList<Violation>) {
    val id = f.pathId
    val status = f.fm["status"]
    if (status != null && status !in STATUSES) {
        v += Violation("V10", f.file, f.line("status"), id, "status `$status` must be shadow, upnext or live")
    }
    f.fm["difficulty"]?.let {
        if (it !in DIFFICULTIES) v += Violation("V10", f.file, f.line("difficulty"), id, "difficulty `$it` must be easy, medium or hard")
    }
    f.fm["order"]?.let {
        if (it.toIntOrNull() == null) v += Violation("V10", f.file, f.line("order"), id, "order `$it` must be a whole number")
    }
    for (k in listOf("playground", "fruit", "upnext")) {
        f.fm[k]?.let {
            if (it != "true" && it != "false") v += Violation("V10", f.file, f.line(k), id, "$k `$it` must be true or false")
        }
    }
    if (status == "live" && f.fm["summary"].isNullOrBlank()) {
        v += Violation("V10", f.file, f.line("summary"), id, "a live leaf needs a non-empty summary")
    }
    f.fm["video"]?.let {
        if (it.isNotEmpty() && !it.startsWith("https://www.youtube.com/watch?v=") && !it.startsWith("https://youtu.be/")) {
            v += Violation("V10", f.file, f.line("video"), id, "video must be empty or an https://www.youtube.com/watch?v= or https://youtu.be/ URL")
        }
    }
    for (k in listOf("publishedOn", "updatedOn")) {
        f.fm[k]?.let {
            if (it.isNotEmpty() && !validDate(it)) v += Violation("V10", f.file, f.line(k), id, "$k `$it` must be empty or YYYY-MM-DD")
        }
    }
}

private fun validDate(s: String): Boolean {
    if (!DATE.matches(s)) return false
    return try {
        LocalDate.parse(s)
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

private fun v9(c: Content, v: MutableList<Violation>) {
    for ((branch, files) in c.leafFiles.groupBy { it.branch }) {
        val flagged = files.filter { it.fm["upnext"] == "true" }
        if (flagged.size > 1) {
            for (f in flagged.drop(1)) {
                v += Violation("V9", f.file, f.line("upnext"), f.pathId, "more than one `upnext: true` in branch `$branch`")
            }
        }
        for (f in flagged) {
            if (f.fm["status"] == "live") v += Violation("V9", f.file, f.line("upnext"), f.pathId, "`upnext: true` on a live leaf")
        }
    }
}

private fun v8(c: Content, baseline: Set<String>, v: MutableList<Violation>) {
    val byId = c.leafFiles.associateBy { it.pathId }
    for (id in baseline.sorted()) {
        val f = byId[id]
        if (f == null) {
            v += Violation("V8", "content/$id.md", 1, id, "was live at the baseline ref and was removed or renamed")
        } else if (f.fm["status"] != "live") {
            v += Violation("V8", f.file, f.line("status"), id, "was live at the baseline ref and is no longer live")
        }
    }
}

/** V12: raw HTML in a live body, other than an exact SIM marker line. Fenced code and inline code are ignored. */
private fun v12(f: LeafFile, v: MutableList<Violation>) {
    var fence: String? = null
    f.body.split("\n").forEachIndexed { i, raw ->
        val line = raw.trimEnd('\r')
        val t = line.trimStart()
        val marker = if (t.startsWith("```")) "```" else if (t.startsWith("~~~")) "~~~" else null
        if (fence != null) {
            if (marker == fence) fence = null
            return@forEachIndexed
        }
        if (marker != null) {
            fence = marker
            return@forEachIndexed
        }
        if (SIM_MARKER.matches(line.trim())) return@forEachIndexed
        val m = RAW_HTML.find(line.replace(INLINE_CODE, ""))
        if (m != null) {
            v += Violation("V12", f.file, f.bodyStartLine + i, f.pathId, "raw HTML `${m.value.trim()}` in a live body; use Markdown")
        }
    }
}
