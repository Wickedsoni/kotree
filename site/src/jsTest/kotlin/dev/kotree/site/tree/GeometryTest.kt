package dev.kotree.site.tree

import dev.kotree.site.Tokens
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.gen.fullHash
import dev.kotree.site.tree.gen.generate
import dev.kotree.site.tree.gen.layoutReport
import dev.kotree.site.tree.gen.limbHash
import dev.kotree.site.tree.model.treeData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** ADR 008 WP0: pins today's geometry so the intended WP2 rebaseline is the only change. Pure Kotlin, no DOM. */
class GeometryTest {
    private val seed = Tokens.World.seed

    @Test
    fun generateIsDeterministic() {
        val a = generate(treeData, seed)
        val b = generate(treeData, seed)
        assertEquals(limbHash(a), limbHash(b))
        assertEquals(fullHash(a), fullHash(b))
    }

    @Test
    fun limbHashIsPinned() {
        assertEquals("cecb906380417dd8", limbHash(generate(treeData, seed)))
    }

    @Test
    fun fullHashIsPinned() {
        assertEquals("c9399b38f0f393cf", fullHash(generate(treeData, seed)))
    }

    // Baseline measured on 21 Oct 2026 (JVM run of the same pure code): the report does NOT pass today.
    // WP0 pins it as a ratchet (failures must not grow; slack covers float noise on JS). WP2 must reach strict PASS.
    @Test
    fun layoutReportDoesNotRegressAt1280x800() = assertNoWorse(1280, 800, baseline = 31)

    @Test
    fun layoutReportDoesNotRegressAt1440x900() = assertNoWorse(1440, 900, baseline = 28)

    private fun assertNoWorse(w: Int, h: Int, baseline: Int) {
        val g = generate(treeData, seed)
        val vp = Viewport(w, h)
        val cam = Camera(g.bounds).also { it.setViewport(vp) }
        val up = treeData.boughs.flatMap { it.branches }.mapNotNull { b -> b.upNext?.let { b.id to it } }.toMap()
        val lines = layoutReport(g, treeData, vp) { b -> cam.branchTarget(b, up[b.id]).z }
        val last = lines.last()
        val fails = if (last == "layoutReport: PASS") 0 else last.removePrefix("layoutReport: ").substringBefore(' ').toInt()
        assertTrue(fails <= baseline + SLACK, "$last (baseline $baseline)\n" + lines.filter { it.startsWith("FAIL") }.joinToString("\n"))
    }

    private companion object {
        const val SLACK = 3
    }
}
