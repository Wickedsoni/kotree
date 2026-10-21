package dev.kotree.build

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LessonRendererTest {
    private fun leaf(body: String, playground: Boolean = true, status: String = "live", sims: List<String> = emptyList()) = LeafFile(
        file = "content/foundation/alpha/first-leaf.md", branch = "foundation", stem = "alpha", leaf = "first-leaf",
        fm = mapOf("status" to status, "playground" to playground.toString(), "title" to "First leaf?", "summary" to "One sentence."),
        requires = emptyList(), sims = sims, fmLines = emptyMap(), body = body, bodyStartLine = 10,
    )

    private val predict = """
        ```challenge
        type: predict
        id: nullable-length
        verified: kotlin-2.4.21
        code: |
          fun main() {
              println(1)
          }
        options:
          - id: a
            text: "0"
            why: "Wrong."
          - id: b
            text: "1"
            correct: true
            why: "Right."
          - id: c
            text: "Crash"
            why: "No."
        output: "1"
        ```
    """.trimIndent()

    private val fix = """
        ```challenge
        type: fix
        id: fix-it
        verified: "UNVERIFIED: not run yet"
        code: |
          fun main() { println(2) }
        hints:
          - "one"
          - "two"
        expected: "1"
        fix: |
          fun main() { println(1) }
        explanation: "Because."
        ```
    """.trimIndent()

    private fun renderWith(body: String, playground: Boolean = true, keepNotes: Boolean = false) =
        renderLesson(leaf(body, playground), keepNotes)

    @Test
    fun cutsTheBodyIntoBlocksInOrder() {
        val body = "## Q\n\nText with **bold**.\n\n<!-- SIM: input-process-output -->\n\n**Try this:**\n\n```kotlin\nfun main() {}\n```\n\n```kotlin run\nfun main() { println(1) }\n```\n\n$predict\n\n$fix\n\nAfter.\n"
        val l = renderWith(body)
        assertEquals(
            listOf("HtmlLessonBlock", "SimLessonBlock", "HtmlLessonBlock", "CodeLessonBlock", "CodeLessonBlock", "PredictLessonBlock", "FixLessonBlock", "HtmlLessonBlock"),
            l.blocks.map { it::class.simpleName },
        )
        assertEquals(false, (l.blocks[3] as CodeLessonBlock).runnable)
        assertEquals(true, (l.blocks[4] as CodeLessonBlock).runnable)
        assertTrue((l.blocks[0] as HtmlLessonBlock).html.contains("<strong>bold</strong>"))
        assertEquals("input-process-output", (l.blocks[1] as SimLessonBlock).id)
        val p = l.blocks[5] as PredictLessonBlock
        assertEquals(listOf("a", "b", "c"), p.options.map { it.id })
        assertEquals("fun main() {\n    println(1)\n}", p.code)
        assertEquals("1", p.output)
        assertEquals(listOf("one", "two"), (l.blocks[6] as FixLessonBlock).hints)
    }

    @Test
    fun rawHtmlIsEscapedAndCommentsAreNotShown() {
        val l = renderWith("<!-- VERIFY: check this -->\n## Q\n\nA <b>tag</b> here.\n", keepNotes = true)
        val html = (l.blocks.single() as HtmlLessonBlock).html
        assertFalse(html.contains("<b>"), html)
        assertFalse(html.contains("VERIFY"), html)
        assertEquals(listOf("VERIFY: check this"), l.notes)
        assertEquals(emptyList(), renderWith("<!-- note -->\n## Q\n", keepNotes = false).notes)
    }

    @Test
    fun javascriptLinksAreDropped() {
        val html = (renderWith("[x](javascript:alert(1))\n").blocks.single() as HtmlLessonBlock).html
        assertFalse(html.contains("javascript:"), html)
    }

    @Test
    fun gfmTablesRender() {
        val html = (renderWith("| A | B |\n|---|---|\n| 1 | 2 |\n").blocks.single() as HtmlLessonBlock).html
        assertTrue(html.contains("<table>") && html.contains("<th>A</th>"), html)
    }

    @Test
    fun otherFencesStayInProseAndDoNotLeakMarkers() {
        val l = renderWith("```text\n<!-- SIM: x -->\nplain\n```\n")
        val html = (l.blocks.single() as HtmlLessonBlock).html
        assertTrue(html.contains("<pre><code class=\"language-text\">"), html)
        assertEquals(1, l.blocks.size)
    }

    @Test
    fun crlfAndLfGiveTheSameBytes() {
        val body = "## Q\n\nText.\n\n$predict\n\n```kotlin\nfun main() {}\n```\n"
        val lf = lessonJson(renderWith(body))
        val crlf = lessonJson(renderWith(body.replace("\n", "\r\n")))
        assertEquals(lf, crlf)
        assertEquals(lf, lessonJson(renderWith(body)))
        assertFalse(lf.contains('\r'))
    }

    @Test
    fun jsonShapeAndEscaping() {
        val l = renderWith("A < B & \"quote\"\n\n```kotlin\nval s = \"\\n\"\n```\n")
        val json = lessonJson(l)
        assertTrue(json.startsWith("{\"schema\":1,\"id\":\"foundation/alpha/first-leaf\",\"playground\":true,\"sims\":[],\"blocks\":["), json)
        assertFalse(json.contains('<'), "every < is written as \\u003c: $json")
        assertTrue(json.contains("\\u003c"))
        assertTrue(json.endsWith("}\n"))
    }

    @Test
    fun staticBodyHasNoAnswers() {
        val l = renderWith("## Q\n\nText.\n\n$predict\n\n$fix\n")
        val body = lessonStaticBody(l)
        assertTrue(body.contains("println(1)") && body.contains("println(2)"))
        for (secret in listOf("Right.", "Wrong.", "Because.", "one", "two", "correct", "println(1) }")) {
            assertFalse(body.contains(secret), "static body leaks `$secret`: $body")
        }
    }

    @Test
    fun challengeProblemsFailWithFileLineAndRule() {
        fun msg(body: String, playground: Boolean = true) =
            assertFailsWith<LessonException> { renderWith(body, playground) }.message!!
        assertTrue(msg("```challenge extra\n```\n").startsWith("content/foundation/alpha/first-leaf.md:10 [foundation/alpha/first-leaf] CH1"))
        assertTrue("CH1" in msg("```challenge\ntype: predict\n"))
        assertTrue("CH2" in msg("```challenge\n: : :\n  - [\n```\n"))
        assertTrue("CH3" in msg("```challenge\ntype: nope\n```\n"))
        assertTrue("CH3" in msg(predict.replace("output:", "outputt:")))
        assertTrue("CH4" in msg("$predict\n\n$predict\n"))
        assertTrue("CH5" in msg(predict.replace("correct: true", "correct: false")))
        assertTrue("CH5" in msg(fix.replace("  - \"two\"\n", "")))
        assertTrue("CH8" in msg(predict.replace("kotlin-2.4.21", "2.4.21")))
        assertTrue("CH10" in msg(predict, playground = false))
        assertTrue("runnable" in msg("```kotlin run\nfun main() {}\n```\n", playground = false))
        assertTrue("unknown fence option" in msg("```kotlin fast\n```\n"))
    }

    @Test
    fun paperPredictIsAllowedOnATheoryLeaf() {
        val paper = predict.replace("verified: kotlin-2.4.21", "verified: not-run\nrun: false")
        val p = renderWith(paper, playground = false).blocks.single() as PredictLessonBlock
        assertEquals(false, p.run)
    }

    @Test
    fun sitemapAndRoutes() {
        val live = leaf("x")
        val shadow = LeafFile(live.file, "foundation", "alpha", "second-leaf", live.fm + ("status" to "shadow"), emptyList(), emptyList(), emptyMap(), "", 1)
        assertEquals(listOf("/foundation/alpha/first-leaf"), liveRoutes(listOf(shadow, live)))
        val xml = sitemapXml(liveRoutes(listOf(shadow, live)))
        assertTrue(xml.contains("<loc>$SITE_ORIGIN/</loc>") && xml.contains("<loc>$SITE_ORIGIN/foundation/alpha/first-leaf</loc>"))
        assertFalse(xml.contains("second-leaf"))
    }

    @Test
    fun bakeWritesHeadTagsBodyAndData() {
        val page = "<html><head><title>Kotree</title><meta name=\"description\" content=\"old\"></head><body><div id=\"_kobweb-root\"><div id=\"kt-lesson-body\" class=\"x\"></div></div></body></html>"
        val baked = bakeLessonPage(page, "a/b/c", "What is \"x\"?", "Summary & more.", "<p>Body</p>\n", "{\"schema\":1,\"id\":\"a/b/c\"}\n")
        assertTrue(baked.contains("<title>What is &quot;x&quot;? \u00B7 Kotree</title>"), baked)
        assertEquals(1, Regex("name=\"description\"").findAll(baked).count())
        assertTrue(baked.contains("<link rel=\"canonical\" href=\"$SITE_ORIGIN/a/b/c\">"))
        assertTrue(baked.contains("<div id=\"kt-lesson-body\" class=\"x\">\n<p>Body</p>\n</div>"))
        assertTrue(baked.indexOf("kt-lesson-data") < baked.indexOf("</head>"))
        assertFailsWith<org.gradle.api.GradleException> { bakeLessonPage(baked, "a/b/c", "t", "s", "b", "{}") } // body no longer empty
    }

    @Test
    fun verifyCatchesLeaksMissingPagesAndPreview() {
        val site = Files.createTempDirectory("kotree-site").toFile()
        File(site, "index.html").writeText("<html></html>")
        val branches = setOf("foundation", "kotlin")
        assertEquals(emptyList(), verifyLessonExport(site, emptyList(), branches).filter { "sitemap" !in it })
        File(site, "kotlin/stem").mkdirs()
        File(site, "kotlin/stem/draft.html").writeText("<html></html>")
        File(site, "lessons-preview").mkdirs()
        val p = verifyLessonExport(site, listOf("kotlin/stem/live"), branches)
        assertTrue(p.any { "live leaf `kotlin/stem/live` was not exported" in it }, p.toString())
        assertTrue(p.any { "`kotlin/stem/draft.html` was exported but its leaf is not live" in it }, p.toString())
        assertTrue(p.any { "lessons-preview" in it }, p.toString())
    }

    @Test
    fun realDraftsRenderForPreview() {
        var d: File? = File("").absoluteFile
        while (d != null && !File(d, "content/foundation/_roadmap.md").isFile) d = d.parentFile
        val content = loadContent(File(d!!, "content"))
        assertEquals(emptyList(), content.parseViolations.map { it.format() })
        val drafts = content.leafFiles.filter { it.fm["status"] != "live" }
        for (f in drafts) {
            val l = renderLesson(f, keepNotes = true)
            assertTrue(l.blocks.any { it is HtmlLessonBlock }, f.pathId)
            val again = lessonJson(renderLesson(f, keepNotes = true))
            assertEquals(again, lessonJson(l), "deterministic: ${f.pathId}")
            assertFalse(lessonJson(l).contains("<!--"), f.pathId)
        }
        val foundation = content.leafFiles.first { it.pathId == "foundation/how-computers-work/what-does-a-computer-do" }
        val l = renderLesson(foundation, keepNotes = true)
        assertEquals(1, l.blocks.count { it is SimLessonBlock })
        assertEquals(3, l.blocks.count { it is PredictLessonBlock })
        assertTrue(l.blocks.filterIsInstance<HtmlLessonBlock>().any { it.html.contains("<table>") })
    }
}
