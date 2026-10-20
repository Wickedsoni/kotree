package dev.kotree.build

private fun jsonString(s: String): String {
    val sb = StringBuilder(s.length + 2)
    sb.append('"')
    for (ch in s) {
        when {
            ch == '\\' -> sb.append("\\\\")
            ch == '"' -> sb.append("\\\"")
            ch == '\n' -> sb.append("\\n")
            ch == '\r' -> sb.append("\\r")
            ch == '\t' -> sb.append("\\t")
            ch.code < 0x20 -> sb.append("\\u").append(ch.code.toString(16).padStart(4, '0'))
            else -> sb.append(ch)
        }
    }
    sb.append('"')
    return sb.toString()
}

private fun scalar(v: Any?): String = when (v) {
    null -> "null"
    is String -> jsonString(v)
    is Boolean, is Int, is Long -> v.toString()
    else -> error("not a JSON scalar: ${v::class}")
}

/** Maps keep insertion order (schema order); lists of scalars print on one line. */
private fun write(sb: StringBuilder, v: Any?, indent: Int) {
    when (v) {
        is Map<*, *> -> {
            if (v.isEmpty()) { sb.append("{}"); return }
            sb.append("{\n")
            val pad = "  ".repeat(indent + 1)
            var first = true
            for ((k, x) in v) {
                if (!first) sb.append(",\n")
                first = false
                sb.append(pad).append(jsonString(k as String)).append(": ")
                write(sb, x, indent + 1)
            }
            sb.append("\n").append("  ".repeat(indent)).append("}")
        }
        is List<*> -> {
            if (v.isEmpty()) { sb.append("[]"); return }
            if (v.all { it == null || it is String || it is Number || it is Boolean }) {
                sb.append(v.joinToString(", ", "[", "]") { scalar(it) })
                return
            }
            sb.append("[\n")
            val pad = "  ".repeat(indent + 1)
            v.forEachIndexed { i, x ->
                if (i > 0) sb.append(",\n")
                sb.append(pad)
                write(sb, x, indent + 1)
            }
            sb.append("\n").append("  ".repeat(indent)).append("]")
        }
        else -> sb.append(scalar(v))
    }
}

private fun counts(c: TreeCounts) = linkedMapOf<String, Any?>("live" to c.live, "total" to c.total)

private fun leafMap(l: TreeLeaf) = linkedMapOf<String, Any?>(
    "id" to l.id, "slug" to l.slug, "title" to l.title, "status" to l.status, "difficulty" to l.difficulty,
    "order" to l.order, "requires" to l.requires, "fruit" to l.fruit, "path" to l.path,
    "summary" to l.summary, "video" to l.video, "publishedOn" to l.publishedOn,
)

private fun stemMap(s: TreeStem) = linkedMapOf<String, Any?>(
    "id" to s.id, "slug" to s.slug, "title" to s.title, "why" to s.why, "order" to s.order,
    "counts" to counts(s.counts), "leaves" to s.leaves.map(::leafMap),
)

private fun branchMap(b: TreeBranch) = linkedMapOf<String, Any?>(
    "id" to b.id, "title" to b.title, "order" to b.order, "accent" to b.accent,
    "counts" to counts(b.counts), "upNext" to b.upNext, "stems" to b.stems.map(::stemMap),
)

/** The docs/03 section 4.2 schema, two-space indent, `\n`, trailing newline, no timestamps. */
fun emitJson(t: TreeModel): String {
    val root = linkedMapOf<String, Any?>(
        "schema" to t.schema,
        "seed" to t.seed,
        "counts" to counts(t.counts),
        "startHere" to t.startHere,
        "boughs" to t.boughs.map { b ->
            linkedMapOf<String, Any?>(
                "id" to b.id, "title" to b.title, "order" to b.order, "branches" to b.branches.map(::branchMap),
            )
        },
    )
    val sb = StringBuilder(512 * 1024)
    write(sb, root, 0)
    sb.append("\n")
    return sb.toString()
}
