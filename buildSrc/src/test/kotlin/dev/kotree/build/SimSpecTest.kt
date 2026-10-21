package dev.kotree.build

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SimSpecTest {
    private val example: String =
        SimSpecTest::class.java.getResource("/fixtures/sim/snapshot-tray.yaml")!!.readText().replace("\r\n", "\n")

    private class Fail(val off: Int, val msg: String) : RuntimeException(msg)

    private fun parse(yaml: String, branch: String = "tools") =
        parseSimBlock(yaml, branch) { off, msg -> throw Fail(off, msg) }

    private fun rule(yaml: String, branch: String = "tools"): String =
        assertFailsWith<Fail> { parse(yaml, branch) }.msg.substringBefore(':')

    private fun edit(from: String, to: String): String {
        check(example.contains(from)) { "`$from` not in example" }
        return example.replace(from, to)
    }

    private fun leaf(body: String, sims: List<String>) = LeafFile(
        file = "content/tools/git/x.md", branch = "tools", stem = "git", leaf = "x",
        fm = mapOf("status" to "live", "playground" to "false"), requires = emptyList(), sims = sims,
        fmLines = emptyMap(), body = body, bodyStartLine = 10,
    )

    @Test
    fun exampleParsesAndJsonIsByteStable() {
        val b = parse(example)
        assertEquals("snapshot-tray", b.id)
        assertEquals("zones", b.tpl)
        assertEquals(3, b.tryList.size)
        val json = simBlockJson(b)
        assertEquals(json, simBlockJson(parse(example)))
        assertTrue(json.startsWith("{\"t\":\"sim\",\"id\":\"snapshot-tray\",\"tpl\":\"zones\",\"spec\":{\"title\":"))
        assertTrue(json.contains("\"try\":[{\"html\":"))
        assertTrue(simStaticHtml(b).contains("<ol>"))
    }

    @Test
    fun oneFailingCasePerRule() {
        assertEquals("SM2", rule("id: [unclosed"))
        assertEquals("SM2", rule(edit("title:", "colour:")))
        assertEquals("SM3", rule(edit("template: zones", "template: carousel")))
        assertEquals("SM4", rule(edit("title: Working folder, tray and snapshot shelf", "title: " + "x".repeat(61))))
        assertEquals("SM5", rule(edit("script: [ edit-a,", "script: [ edit-z,")))
        assertEquals("SM6", rule(edit("- add: { snaps: 1 }", "- explode: { snaps: 1 }")))
        assertEquals("SM7", rule(edit("done: [ edit-a ]", "done: [ edit-q ]")))
        assertEquals("SM8", rule(edit("vars:", "phone: { rows: [ { id: r1, label: x } ] }\nvars:")))
        assertEquals("SM8", rule(edit("vars:", "caption: A picture of what Android does. Not a real phone.\nphone: { rows: [ { id: r1, label: x } ] }\nvars:"), branch = "profiling"))
        assertEquals("SM10", rule(edit("intro: Files A and B sit in the working folder. The tray and the shelf are empty.", "intro: " + "word ".repeat(2000))))
    }

    @Test
    fun legacyMarkerStaysAndFenceBecomesABlock() {
        val legacy = renderLesson(leaf("## See it move\n\n<!-- SIM: old-one -->\n", listOf("old-one")), false)
        val lb = legacy.blocks.filterIsInstance<SimLessonBlock>().single()
        assertEquals(null, lb.tpl)
        assertEquals("{\"t\":\"sim\",\"id\":\"old-one\"}", simBlockJson(lb))
        assertEquals("", simStaticHtml(lb))
        val l = renderLesson(leaf("## See it move\n\n```sim\n$example```\n\nAfter.\n", listOf("snapshot-tray")), false)
        assertEquals("zones", l.blocks.filterIsInstance<SimLessonBlock>().single().tpl)
        assertTrue(lessonStaticBody(l).contains("<ol>"))
        assertTrue(lessonJson(l).contains("\"tpl\":\"zones\""))
    }

    @Test
    fun fenceErrorsCarryFileAndLine() {
        val msg = assertFailsWith<LessonException> {
            renderLesson(leaf("## See it move\n\n```sim\n${edit("template: zones", "template: nope")}```\n", listOf("snapshot-tray")), false)
        }.message!!
        assertTrue(msg.startsWith("content/tools/git/x.md:13 [tools/git/x] SM3"), msg)
    }

    @Test
    fun validateChecksSm1AndSm9() {
        val fx = Fx()
        fx.edit(Fx.FIRST, "<!-- SIM: counter-sim -->", "```sim\n${example}```\n\n```sim\n${example}```")
        val rules = fx.violations().rules()
        assertTrue("SM9" in rules && "SM1" in rules, rules.toString())
    }
}
