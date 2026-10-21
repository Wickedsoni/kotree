package dev.kotree.build

import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.error.YAMLException

/** Kotlin version of the Playground and of every `verified` output (docs/06 section 5). Written to `LessonConfig.kt`. */
const val LESSON_KOTLIN_VERSION = "2.4.21"

/** Canonical origin of the site (docs/00 L25). Used for canonical URLs and the sitemap. */
const val SITE_ORIGIN = "https://kotree.garvitmaheshwari.in"

/** One block of a lesson, in page order (ADR 005 section 1). */
sealed interface LessonBlock

data class HtmlLessonBlock(val html: String) : LessonBlock
/** A sim. A legacy bare marker has only [id]; a ```sim fence adds [tpl], [spec] and [tryList] (ADR 009 section 1). */
data class SimLessonBlock(
    val id: String,
    val tpl: String? = null,
    val spec: Map<String, Any?>? = null,
    val tryList: List<SimTryStep> = emptyList(),
) : LessonBlock
data class CodeLessonBlock(val lang: String, val code: String, val runnable: Boolean) : LessonBlock

data class PredictOption(val id: String, val text: String, val why: String, val correct: Boolean)

data class PredictLessonBlock(
    val id: String,
    val code: String,
    val run: Boolean,
    val verified: String,
    val generated: Boolean,
    val options: List<PredictOption>,
    val output: String,
) : LessonBlock

data class FixLessonBlock(
    val id: String,
    val code: String,
    val verified: String,
    val generated: Boolean,
    val hints: List<String>,
    val expected: String,
    val fix: String,
    val explanation: String,
) : LessonBlock

/** A rendered leaf. [notes] holds the author comments of the body and is only filled for previews. */
data class Lesson(
    val id: String,
    val playground: Boolean,
    val sims: List<String>,
    val blocks: List<LessonBlock>,
    val notes: List<String>,
)

/** A problem in a leaf body, printed as `<file>:<line> [<id>] <message>`. */
class LessonException(file: String, line: Int, id: String, message: String) : RuntimeException("$file:$line [$id] $message")

private val SIM_MARKER_LINE = Regex("^<!-- SIM: ([a-z0-9]+(-[a-z0-9]+)*) -->$")
private val KEBAB = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")
private val FENCE_OPEN = Regex("^(`{3,}|~{3,})(.*)$")
private val VERIFIED = Regex("^(kotlin-\\d+\\.\\d+\\.\\d+|UNVERIFIED: .+|not-run)$")

private const val PREDICT_OPTION_COUNT = 3
private const val FIX_HINT_COUNT = 2
private val PREDICT_KEYS = setOf("type", "id", "run", "generated", "verified", "code", "options", "output")
private val PREDICT_REQUIRED = setOf("type", "id", "verified", "code", "options", "output")
private val FIX_KEYS = setOf("type", "id", "generated", "verified", "code", "hints", "expected", "fix", "explanation")
private val FIX_REQUIRED = setOf("type", "id", "verified", "code", "hints", "expected", "fix")
private val OPTION_KEYS = setOf("id", "text", "why", "correct")

private val markdownExtensions = listOf(TablesExtension.create())
private val markdownParser: Parser = Parser.builder().extensions(markdownExtensions).build()

// escapeHtml: raw HTML in the source is shown as text, so the browser sets innerHTML only from Markdown output.
// sanitizeUrls: a javascript: link becomes an empty href.
private val markdownRenderer: HtmlRenderer =
    HtmlRenderer.builder().extensions(markdownExtensions).escapeHtml(true).sanitizeUrls(true).build()

/** Prose Markdown to HTML. Deterministic. */
fun renderMarkdown(markdown: String): String = markdownRenderer.render(markdownParser.parse(markdown)).trim()

/**
 * Cuts the body of [f] into lesson blocks (docs/07 section 6.2, ADR 005). Rules:
 * - an exact SIM marker line (legacy) or a ```` ```sim ```` fence (ADR 009) becomes a `sim` block;
 * - a ```` ```challenge ```` fence becomes `predict` or `fix` (structure checked, docs/06 section 3);
 * - a ```` ```kotlin ```` fence becomes `code`; ```` ```kotlin run ```` makes it runnable (needs `playground: true`);
 * - any other fence stays in the prose;
 * - a whole-line `<!-- ... -->` comment is dropped from the prose (kept in [Lesson.notes] when [keepNotes]);
 * - the rest is prose, rendered by [renderMarkdown].
 * CRLF and a BOM are normalised first, so the output is the same on every platform.
 */
fun renderLesson(f: LeafFile, keepNotes: Boolean): Lesson {
    val lines = f.body.removePrefix("﻿").replace("\r\n", "\n").replace('\r', '\n').split("\n")
    val playground = f.fm["playground"] == "true"
    val blocks = ArrayList<LessonBlock>()
    val notes = ArrayList<String>()
    val challengeIds = HashSet<String>()
    val prose = StringBuilder()
    fun fail(i: Int, msg: String): Nothing = throw LessonException(f.file, f.bodyStartLine + i, f.pathId, msg)
    fun flush() {
        val html = renderMarkdown(prose.toString())
        prose.setLength(0)
        if (html.isNotEmpty()) blocks += HtmlLessonBlock(html)
    }

    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        val fence = FENCE_OPEN.matchEntire(line)
        if (fence != null) {
            val mark = fence.groupValues[1]
            val info = fence.groupValues[2].trim()
            var end = i + 1
            while (end < lines.size && lines[end].trimEnd() != mark) end++
            val closed = end < lines.size
            val inner = lines.subList(minOf(i + 1, lines.size), end).joinToString("\n")
            val firstWord = info.substringBefore(' ')
            if (mark.startsWith("`") && firstWord == "sim") {
                if (info != "sim") fail(i, "SM2: the fence must be exactly ```sim")
                if (!closed) fail(i, "SM2: the sim block is never closed")
                flush()
                blocks += parseSimBlock(inner, f.branch) { off, msg -> fail(i + 1 + off, msg) }
            } else if (firstWord == "challenge" || (mark.startsWith("`") && info.startsWith("challenge"))) {
                if (info != "challenge") fail(i, "CH1: the fence must be exactly ```challenge")
                if (!closed) fail(i, "CH1: the challenge block is never closed")
                flush()
                blocks += parseChallenge(inner, f, i, playground, challengeIds, ::fail)
            } else if (mark.startsWith("`") && firstWord == "kotlin") {
                if (!closed) fail(i, "the kotlin fence is never closed")
                val flags = info.removePrefix("kotlin").trim()
                val runnable = when (flags) {
                    "" -> false
                    "run" -> true
                    else -> fail(i, "unknown fence option `$flags`; use `kotlin` or `kotlin run`")
                }
                if (runnable && !playground) fail(i, "a runnable kotlin block needs `playground: true` in the frontmatter (docs/06 section 1)")
                flush()
                blocks += CodeLessonBlock("kotlin", inner, runnable)
            } else {
                // Any other fence belongs to the prose; copy it through unchanged so its lines are never read as markers.
                for (k in i..minOf(end, lines.size - 1)) prose.append(lines[k]).append('\n')
            }
            i = end + 1
            continue
        }
        val trimmed = line.trim()
        val sim = SIM_MARKER_LINE.matchEntire(trimmed)
        if (sim != null) {
            flush()
            blocks += SimLessonBlock(sim.groupValues[1])
            i++
            continue
        }
        if (trimmed.startsWith("<!--")) {
            var end = i
            while (end < lines.size && !lines[end].contains("-->")) end++
            if (end >= lines.size) fail(i, "an HTML comment is never closed")
            if (keepNotes) {
                val text = lines.subList(i, end + 1).joinToString(" ").trim().removePrefix("<!--").removeSuffix("-->").trim()
                if (text.isNotEmpty()) notes += text
            }
            i = end + 1
            continue
        }
        prose.append(line).append('\n')
        i++
    }
    flush()
    return Lesson(f.pathId, playground, f.sims ?: emptyList(), blocks, notes)
}

private fun parseChallenge(
    yamlText: String,
    f: LeafFile,
    fenceLine: Int,
    playground: Boolean,
    seenIds: MutableSet<String>,
    fail: (Int, String) -> Nothing,
): LessonBlock {
    val at = fenceLine + 1
    val root: Any? = try {
        Yaml(SafeConstructor(LoaderOptions())).load<Any?>(yamlText)
    } catch (e: YAMLException) {
        fail(at, "CH2: the challenge block is not valid YAML: ${yamlErrorText(e)}")
    }
    val map = (root as? Map<*, *>) ?: fail(at, "CH2: the challenge block must be a YAML mapping")
    val type = map["type"] as? String
    val (allowed, required) = when (type) {
        "predict" -> PREDICT_KEYS to PREDICT_REQUIRED
        "fix" -> FIX_KEYS to FIX_REQUIRED
        else -> fail(at, "CH3: `type` must be predict or fix")
    }
    for (k in map.keys) if (k !in allowed) fail(at, "CH3: unknown key `$k` in a $type challenge")
    for (k in required) if (map[k] == null) fail(at, "CH3: `$k` is missing in a $type challenge")

    fun text(key: String, container: Map<*, *> = map): String {
        val v = container[key] ?: fail(at, "CH3: `$key` is missing")
        if (v is Map<*, *> || v is List<*>) fail(at, "CH3: `$key` must be text")
        return v.toString().trimEnd('\n')
    }
    fun flag(key: String, default: Boolean): Boolean = when (val v = map[key]) {
        null -> default
        is Boolean -> v
        else -> fail(at, "CH6: `$key` must be true or false")
    }

    val id = text("id")
    if (!KEBAB.matches(id)) fail(at, "CH4: id `$id` must be kebab-case")
    if (!seenIds.add(id)) fail(at, "CH4: duplicate challenge id `$id`")
    val verified = text("verified")
    if (!VERIFIED.matches(verified)) fail(at, "CH8: verified `$verified` must be kotlin-<major>.<minor>.<patch>, UNVERIFIED: <reason> or not-run")
    val code = text("code")
    val generated = flag("generated", false)

    return if (type == "predict") {
        val run = flag("run", true)
        if (!run && playground) fail(at, "CH10: `run: false` is only allowed on a `playground: false` leaf")
        if (run && !playground) fail(at, "CH10: a runnable predict item needs `playground: true` in the frontmatter")
        val raw = map["options"] as? List<*> ?: fail(at, "CH5: `options` must be a list")
        if (raw.size != PREDICT_OPTION_COUNT) fail(at, "CH5: a predict item needs exactly $PREDICT_OPTION_COUNT options, found ${raw.size}")
        val options = raw.mapIndexed { n, o ->
            val om = o as? Map<*, *> ?: fail(at, "CH5: option ${n + 1} must be a mapping")
            for (k in om.keys) if (k !in OPTION_KEYS) fail(at, "CH5: unknown key `$k` in option ${n + 1}")
            val correct = when (val c = om["correct"]) {
                null -> false
                is Boolean -> c
                else -> fail(at, "CH5: `correct` in option ${n + 1} must be true or false")
            }
            PredictOption(text("id", om), text("text", om), text("why", om), correct)
        }
        if (options.count { it.correct } != 1) fail(at, "CH5: exactly one option needs `correct: true`")
        if (options.map { it.id } != listOf("a", "b", "c")) fail(at, "CH5: option ids must be a, b, c in that order")
        PredictLessonBlock(id, code, run, verified, generated, options, text("output"))
    } else {
        val raw = map["hints"] as? List<*> ?: fail(at, "CH5: `hints` must be a list")
        if (raw.size != FIX_HINT_COUNT) fail(at, "CH5: a fix item needs exactly $FIX_HINT_COUNT hints, found ${raw.size}")
        if (!playground) fail(at, "CH10: a fix item needs `playground: true` in the frontmatter")
        val hints = raw.map { h ->
            if (h == null || h is Map<*, *> || h is List<*>) fail(at, "CH5: every hint must be text")
            h.toString()
        }
        val explanation = if (map["explanation"] == null) "" else text("explanation")
        FixLessonBlock(id, code, verified, generated, hints, text("expected"), text("fix"), explanation)
    }
}

// ---- Output: lesson JSON, static body, config, sitemap ----

internal fun StringBuilder.jsonString(s: String) {
    append('"')
    for (c in s) when {
        c == '"' -> append("\\\"")
        c == '\\' -> append("\\\\")
        c == '\n' -> append("\\n")
        c == '\r' -> append("\\r")
        c == '\t' -> append("\\t")
        c == '<' -> append("\\u003c") // safe inside an inline script tag, and the same bytes everywhere
        c == ' ' || c == ' ' || c < ' ' -> append("\\u").append(c.code.toString(16).padStart(4, '0'))
        else -> append(c)
    }
    append('"')
}

private fun StringBuilder.jsonStrings(list: List<String>) {
    append('[')
    list.forEachIndexed { n, s -> if (n > 0) append(','); jsonString(s) }
    append(']')
}

private fun StringBuilder.kv(key: String, first: Boolean = false, value: StringBuilder.() -> Unit) {
    if (!first) append(',')
    jsonString(key); append(':'); value()
}

/** Lesson JSON, schema 1 (ADR 005 section 1): fixed key order, `\n` only, no timestamps, one trailing newline. */
fun lessonJson(lesson: Lesson): String {
    val sb = StringBuilder()
    sb.append('{')
    sb.kv("schema", first = true) { append(1) }
    sb.kv("id") { jsonString(lesson.id) }
    sb.kv("playground") { append(lesson.playground) }
    sb.kv("sims") { jsonStrings(lesson.sims) }
    sb.kv("blocks") {
        append('[')
        lesson.blocks.forEachIndexed { n, b ->
            if (n > 0) append(',')
            append('{')
            when (b) {
                is HtmlLessonBlock -> { kv("t", true) { jsonString("html") }; kv("html") { jsonString(b.html) } }
                is SimLessonBlock -> append(simBlockJson(b).removePrefix("{").removeSuffix("}"))
                is CodeLessonBlock -> {
                    kv("t", true) { jsonString("code") }; kv("lang") { jsonString(b.lang) }
                    kv("code") { jsonString(b.code) }; kv("runnable") { append(b.runnable) }
                }
                is PredictLessonBlock -> {
                    kv("t", true) { jsonString("predict") }; kv("id") { jsonString(b.id) }; kv("code") { jsonString(b.code) }
                    kv("run") { append(b.run) }; kv("verified") { jsonString(b.verified) }; kv("generated") { append(b.generated) }
                    kv("options") {
                        append('[')
                        b.options.forEachIndexed { k, o ->
                            if (k > 0) append(',')
                            append('{')
                            kv("id", true) { jsonString(o.id) }; kv("text") { jsonString(o.text) }
                            kv("why") { jsonString(o.why) }; kv("correct") { append(o.correct) }
                            append('}')
                        }
                        append(']')
                    }
                    kv("output") { jsonString(b.output) }
                }
                is FixLessonBlock -> {
                    kv("t", true) { jsonString("fix") }; kv("id") { jsonString(b.id) }; kv("code") { jsonString(b.code) }
                    kv("verified") { jsonString(b.verified) }; kv("generated") { append(b.generated) }
                    kv("hints") { jsonStrings(b.hints) }; kv("expected") { jsonString(b.expected) }
                    kv("fix") { jsonString(b.fix) }; kv("explanation") { jsonString(b.explanation) }
                }
            }
            append('}')
        }
        append(']')
    }
    if (lesson.notes.isNotEmpty()) sb.kv("notes") { jsonStrings(lesson.notes) }
    sb.append("}\n")
    return sb.toString()
}

private fun escapeHtml(s: String): String =
    s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")

/**
 * Static copy of the prose for the exported page (ADR 005 section 1): prose plus escaped code, and nothing that gives
 * an answer away (no options, hints, fix, explanation or output).
 */
fun lessonStaticBody(lesson: Lesson): String {
    val sb = StringBuilder()
    fun pre(code: String) = sb.append("<pre><code>").append(escapeHtml(code)).append("</code></pre>\n")
    for (b in lesson.blocks) when (b) {
        is HtmlLessonBlock -> sb.append(b.html).append('\n')
        is CodeLessonBlock -> pre(b.code)
        is PredictLessonBlock -> pre(b.code)
        is FixLessonBlock -> pre(b.code)
        is SimLessonBlock -> sb.append(simStaticHtml(b))
    }
    return sb.toString()
}

/** Generated Kotlin for the app: the pinned Kotlin version and the site origin. */
fun lessonConfigKotlin(): String = """
    // Generated by the renderLessons Gradle task. Do not edit.
    package dev.kotree.site.lessons

    const val LESSON_KOTLIN_VERSION = "$LESSON_KOTLIN_VERSION"
    const val SITE_ORIGIN = "$SITE_ORIGIN"
""".trimIndent() + "\n"

const val LESSON_CONFIG_PATH = "dev/kotree/site/lessons/LessonConfig.kt"

/** `sitemap.xml`: the home page plus every live lesson (docs/07 section 6.7). [routes] look like `/kotlin/x/y`. */
fun sitemapXml(routes: List<String>): String {
    val sb = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n")
    for (r in listOf("/") + routes.sorted()) sb.append("  <url><loc>").append(SITE_ORIGIN).append(r).append("</loc></url>\n")
    sb.append("</urlset>\n")
    return sb.toString()
}

/** Routes of the live leaves, sorted, such as `/kotlin/null-safety/what-is-null` (D16: a leaf is live by its own status). */
fun liveRoutes(leaves: List<LeafFile>): List<String> =
    leaves.filter { it.fm["status"] == "live" }.map { "/" + it.pathId }.sorted()
