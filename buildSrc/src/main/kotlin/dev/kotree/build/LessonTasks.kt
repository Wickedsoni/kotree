package dev.kotree.build

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

/** Where lesson JSON of live leaves goes below `resourcesOut` (served and exported at `/lessons/<id>.json`). */
const val LESSON_JSON_DIR = "public/lessons"

/** Where preview JSON goes below `previewOut`. Never exported: `emitLessonPages` and `verifyLessonExport` enforce that. */
const val PREVIEW_JSON_DIR = "public/lessons-preview"

private fun writeUtf8(f: File, text: String) {
    f.parentFile.mkdirs()
    f.writeBytes(text.toByteArray(Charsets.UTF_8)) // no BOM, `\n` kept as written
}

/**
 * Renders the leaves of /content to lesson JSON (ADR 005 section 1).
 * - Every `status: live` leaf: `resourcesOut/public/lessons/<id>.json`, `staticOut/<id>.html`, one sitemap entry.
 * - With [preview] true, every leaf file (any status, so shadow drafts too) also goes to
 *   `previewOut/public/lessons-preview/<id>.json`, with its author comments as `notes`. Off by default and off in any export.
 * A broken live leaf fails the build; a broken draft only fails when [preview] is on.
 */
abstract class RenderLessonsTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contentDir: DirectoryProperty

    @get:Input
    abstract val preview: Property<Boolean>

    @get:OutputDirectory
    abstract val resourcesOut: DirectoryProperty

    @get:OutputDirectory
    abstract val previewOut: DirectoryProperty

    @get:OutputDirectory
    abstract val staticOut: DirectoryProperty

    /** Source root; the file goes to `dev/kotree/site/lessons/LessonConfig.kt` below it. */
    @get:OutputDirectory
    abstract val kotlinOut: DirectoryProperty

    @get:OutputFile
    abstract val sitemapOut: RegularFileProperty

    @TaskAction
    fun run() {
        val content = loadContent(contentDir.get().asFile)
        listOf(resourcesOut, previewOut, staticOut, kotlinOut).forEach { it.get().asFile.deleteRecursively() }
        val live = content.leafFiles.filter { it.fm["status"] == "live" }.sortedBy { it.pathId }
        for (leaf in live) {
            val lesson = renderOrFail(leaf, keepNotes = false)
            writeUtf8(File(resourcesOut.get().asFile, "$LESSON_JSON_DIR/${leaf.pathId}.json"), lessonJson(lesson))
            writeUtf8(File(staticOut.get().asFile, "${leaf.pathId}.html"), lessonStaticBody(lesson))
        }
        if (preview.get()) {
            for (leaf in content.leafFiles.sortedBy { it.pathId }) {
                writeUtf8(
                    File(previewOut.get().asFile, "$PREVIEW_JSON_DIR/${leaf.pathId}.json"),
                    lessonJson(renderOrFail(leaf, keepNotes = true)),
                )
            }
            logger.lifecycle("renderLessons: preview ON, ${content.leafFiles.size} drafts and lessons in lessons-preview")
        }
        writeUtf8(File(kotlinOut.get().asFile, LESSON_CONFIG_PATH), lessonConfigKotlin())
        writeUtf8(sitemapOut.get().asFile, sitemapXml(liveRoutes(content.leafFiles)))
        logger.lifecycle("renderLessons: ${live.size} live lesson(s)")
    }

    private fun renderOrFail(leaf: LeafFile, keepNotes: Boolean): Lesson = try {
        renderLesson(leaf, keepNotes)
    } catch (e: LessonException) {
        throw GradleException("renderLessons: ${e.message}")
    }
}

/**
 * The routes of the live leaves, for `addExtraRoute` (ADR 005 section 3). A ValueSource, because the configuration cache
 * tracks file contents but not directory listings: a new leaf file must change the routes.
 */
abstract class LiveLessonRoutes : ValueSource<List<String>, LiveLessonRoutes.Params> {
    interface Params : ValueSourceParameters {
        val contentDir: DirectoryProperty
    }

    override fun obtain(): List<String> {
        val root = parameters.contentDir.get().asFile
        val out = ArrayList<LeafFile>()
        val violations = ArrayList<Violation>()
        for (branch in root.listFiles { f -> f.isDirectory && !f.name.startsWith("_") }.orEmpty()) {
            for (stem in branch.listFiles { f -> f.isDirectory }.orEmpty()) {
                for (f in stem.listFiles { f -> f.isFile && f.name.endsWith(".md") && !f.name.startsWith("_") }.orEmpty()) {
                    parseLeafFile(f, "content/${branch.name}/${stem.name}/${f.name}", branch.name, stem.name, violations)?.let { out += it }
                }
            }
        }
        return liveRoutes(out)
    }
}

private val TITLE_TAG = Regex("<title>.*?</title>", RegexOption.DOT_MATCHES_ALL)
private val DESCRIPTION_TAG = Regex("<meta\\b[^>]*\\bname=\"description\"[^>]*>")
private val BODY_ELEMENT = Regex("(<div\\b[^>]*\\bid=\"kt-lesson-body\"[^>]*>)(\\s*)(</div>)")
private val ROBOTS_TAG = Regex("<meta\\b[^>]*\\bname=\"robots\"[^>]*>")

/** Escapes text for an HTML attribute or text node. */
internal fun htmlEscape(s: String): String =
    s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")

/**
 * Bakes one exported lesson page (docs/07 section 6.7): title, description, canonical and og tags, the static prose in
 * `#kt-lesson-body` and the lesson JSON in `script#kt-lesson-data` (in `<head>`, outside `#_kobweb-root`).
 * [leafTitle] and [summary] come from the frontmatter. Fails when the snapshot has no empty `#kt-lesson-body`.
 */
fun bakeLessonPage(html: String, id: String, leafTitle: String, summary: String, staticBody: String, json: String): String {
    val bodyMatches = BODY_ELEMENT.findAll(html).toList()
    if (bodyMatches.size != 1) throw GradleException("emitLessonPages: $id has ${bodyMatches.size} empty #kt-lesson-body element(s), expected exactly 1")
    if (!html.contains("</head>")) throw GradleException("emitLessonPages: $id has no </head>")
    var out = BODY_ELEMENT.replace(html) { m -> m.groupValues[1] + "\n" + staticBody + m.groupValues[3] }
    val title = htmlEscape("$leafTitle · Kotree")
    out = if (TITLE_TAG.containsMatchIn(out)) TITLE_TAG.replaceFirst(out, Regex.escapeReplacement("<title>$title</title>"))
    else out.replace("</head>", "<title>$title</title></head>")
    out = DESCRIPTION_TAG.replace(out, "")
    val url = "$SITE_ORIGIN/$id"
    val tags = buildString {
        append("<meta name=\"description\" content=\"").append(htmlEscape(summary)).append("\">")
        append("<link rel=\"canonical\" href=\"").append(htmlEscape(url)).append("\">")
        append("<meta property=\"og:title\" content=\"").append(title).append("\">")
        append("<meta property=\"og:description\" content=\"").append(htmlEscape(summary)).append("\">")
        append("<meta property=\"og:url\" content=\"").append(htmlEscape(url)).append("\">")
        append("<script type=\"application/json\" id=\"kt-lesson-data\">").append(json.trimEnd()).append("</script>")
    }
    return out.replace("</head>", tags + "</head>")
}

/** Adds `<meta name="robots" content="noindex">` to a page once. */
fun addNoindex(html: String): String =
    if (ROBOTS_TAG.containsMatchIn(html)) html else html.replace("</head>", "<meta name=\"robots\" content=\"noindex\"></head>")

/**
 * Runs after `kobweb export` (docs/07 section 6.7): finishes every live lesson page, writes `sitemap.xml`, marks
 * `404.html` noindex, and removes any `lessons-preview` folder so a draft can never ship.
 */
abstract class EmitLessonPagesTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contentDir: DirectoryProperty

    @get:Internal
    abstract val staticDir: DirectoryProperty

    @get:Internal
    abstract val lessonsJsonDir: DirectoryProperty

    @get:Internal
    abstract val sitemapFile: RegularFileProperty

    /** `.kobweb/site`. Changed in place, so the task has no declared outputs and always runs. */
    @get:Internal
    abstract val siteDir: DirectoryProperty

    @TaskAction
    fun run() {
        val site = siteDir.get().asFile
        if (!File(site, "index.html").isFile) throw GradleException("emitLessonPages: ${site.path}/index.html is missing; run the export first")
        File(site, "lessons-preview").deleteRecursively()
        val live = loadContent(contentDir.get().asFile).leafFiles.filter { it.fm["status"] == "live" }.sortedBy { it.pathId }
        for (leaf in live) {
            val page = File(site, "${leaf.pathId}.html")
            if (!page.isFile) throw GradleException("emitLessonPages: live leaf ${leaf.pathId} was not exported (${page.path} is missing)")
            val json = File(lessonsJsonDir.get().asFile, "${leaf.pathId}.json").readText(Charsets.UTF_8)
            val body = File(staticDir.get().asFile, "${leaf.pathId}.html").readText(Charsets.UTF_8)
            val baked = bakeLessonPage(page.readText(Charsets.UTF_8), leaf.pathId, leaf.fm.getValue("title"), leaf.fm.getValue("summary"), body, json)
            writeUtf8(page, baked)
            // Keep the JSON beside the export even if the resource copy missed it (docs/07 section 6.2 step 4 fallback).
            writeUtf8(File(site, "lessons/${leaf.pathId}.json"), json)
        }
        writeUtf8(File(site, "sitemap.xml"), sitemapFile.get().asFile.readText(Charsets.UTF_8))
        val notFound = File(site, "404.html")
        if (notFound.isFile) writeUtf8(notFound, addNoindex(notFound.readText(Charsets.UTF_8)))
        logger.lifecycle("emitLessonPages: ${live.size} lesson page(s) finished")
    }
}

private val DATA_SCRIPT = Regex("<script type=\"application/json\" id=\"kt-lesson-data\">(.*?)</script>", RegexOption.DOT_MATCHES_ALL)
private val CANONICAL = Regex("<link rel=\"canonical\" href=\"([^\"]*)\">")
private val ID_IN_JSON = Regex("\"id\":\"([^\"]*)\"")

/** Problems found in the export. Empty means the export is clean. Pure, so tests can feed it a fake export folder. */
fun verifyLessonExport(site: File, liveIds: List<String>, branchSlugs: Set<String>): List<String> {
    val problems = ArrayList<String>()
    if (!File(site, "index.html").isFile) problems += "index.html is missing"
    val expected = liveIds.sorted()
    val actual = site.walkTopDown().filter { it.isFile && it.extension == "html" }
        .map { it.relativeTo(site).invariantSeparatorsPath.removeSuffix(".html") }
        .filter { p -> p.split('/').let { it.size == 3 && it[0] in branchSlugs } }.sorted().toList()
    for (id in expected - actual.toSet()) problems += "live leaf `$id` was not exported"
    for (id in actual - expected.toSet()) problems += "`$id.html` was exported but its leaf is not live"
    val lessonJsons = File(site, "lessons").takeIf { it.isDirectory }?.walkTopDown()?.filter { it.isFile && it.extension == "json" }
        ?.map { it.relativeTo(File(site, "lessons")).invariantSeparatorsPath.removeSuffix(".json") }?.sorted()?.toList().orEmpty()
    for (id in expected - lessonJsons.toSet()) problems += "lessons/$id.json is missing"
    for (id in lessonJsons - expected.toSet()) problems += "lessons/$id.json exists but its leaf is not live"
    if (File(site, "lessons-preview").exists()) problems += "lessons-preview is in the export: drafts must never ship"
    for (id in expected.filter { it in actual }) {
        val html = File(site, "$id.html").readText(Charsets.UTF_8)
        val scripts = DATA_SCRIPT.findAll(html).toList()
        if (scripts.size != 1) problems += "$id.html has ${scripts.size} script#kt-lesson-data, expected 1"
        else {
            val json = scripts[0].groupValues[1]
            if (ID_IN_JSON.find(json)?.groupValues?.get(1) != id) problems += "$id.html: the inline JSON id is not $id"
            if (!json.contains("\"schema\":1")) problems += "$id.html: the inline JSON is not schema 1"
        }
        val hasBody = Regex("<div\\b[^>]*\\bid=\"kt-lesson-body\"[^>]*>\\s*[^\\s<]|<div\\b[^>]*\\bid=\"kt-lesson-body\"[^>]*>\\s*<[^/]").containsMatchIn(html)
        if (!hasBody) problems += "$id.html: #kt-lesson-body is missing or empty"
        val canonical = CANONICAL.find(html)?.groupValues?.get(1)
        if (canonical != "$SITE_ORIGIN/$id") problems += "$id.html: canonical is `$canonical`, expected `$SITE_ORIGIN/$id`"
    }
    val sitemap = File(site, "sitemap.xml")
    if (!sitemap.isFile) problems += "sitemap.xml is missing"
    else {
        val listed = Regex("<loc>([^<]*)</loc>").findAll(sitemap.readText(Charsets.UTF_8)).map { it.groupValues[1] }.toList().sorted()
        val want = (listOf("$SITE_ORIGIN/") + expected.map { "$SITE_ORIGIN/$it" }).sorted()
        if (listed != want) problems += "sitemap.xml lists $listed, expected $want"
    }
    val notFound = File(site, "404.html")
    if (notFound.isFile && !ROBOTS_TAG.containsMatchIn(notFound.readText(Charsets.UTF_8))) problems += "404.html has no robots noindex"
    for (f in site.walkTopDown().filter { it.isFile && it.extension == "html" }) {
        val rel = f.relativeTo(site).invariantSeparatorsPath.removeSuffix(".html")
        if (rel !in expected && f.readText(Charsets.UTF_8).contains("kt-lesson-data")) problems += "$rel.html is not a live lesson but contains kt-lesson-data"
    }
    return problems
}

/** Fails when the export does not match the live set. Run after the export and `emitLessonPages`. */
abstract class VerifyLessonExportTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contentDir: DirectoryProperty

    @get:Internal
    abstract val siteDir: DirectoryProperty

    @TaskAction
    fun run() {
        val live = loadContent(contentDir.get().asFile).leafFiles.filter { it.fm["status"] == "live" }.map { it.pathId }
        val problems = verifyLessonExport(siteDir.get().asFile, live, BRANCHES.map { it.slug }.toSet())
        if (problems.isNotEmpty()) throw GradleException("verifyLessonExport: ${problems.size} problem(s)\n" + problems.joinToString("\n"))
        logger.lifecycle("verifyLessonExport: ok (${live.size} live lesson(s))")
    }
}
