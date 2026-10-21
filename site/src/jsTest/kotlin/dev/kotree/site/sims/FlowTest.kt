package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimItem
import dev.kotree.site.sims.layouts.StateNode
import dev.kotree.site.sims.layouts.StationDef
import dev.kotree.site.sims.layouts.Pt
import dev.kotree.site.sims.layouts.arrowsOf
import dev.kotree.site.sims.layouts.currentNodes
import dev.kotree.site.sims.layouts.edgeGeom
import dev.kotree.site.sims.layouts.hopList
import dev.kotree.site.sims.layouts.nodePositions
import dev.kotree.site.sims.layouts.pathHolding
import dev.kotree.site.sims.layouts.pathsOf
import dev.kotree.site.sims.layouts.stationCells
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FlowTest {
    private val line = listOf(listOf("in", "mix", "bake", "box"))
    private val fork = listOf(listOf("in", "gate", "yes", "out"), listOf("in", "gate", "no", "out2"))

    @Test
    fun aMoveToANonAdjacentStationHopsAlongThePath() {
        assertEquals(listOf("mix", "bake", "box"), hopList(line, "in", "box"))
        assertEquals(listOf("bake"), hopList(line, "mix", "bake"))
        assertEquals(listOf("bake", "mix"), hopList(line, "box", "mix"))
        assertEquals(emptyList(), hopList(line, "mix", "mix"))
    }

    @Test
    fun aForkPicksThePathThatHoldsTheTarget() {
        assertEquals(fork[1], pathHolding(fork, "no"))
        assertEquals(fork[0], pathHolding(fork, "yes"))
        assertNull(pathHolding(fork, "nowhere"))
        assertEquals(listOf("gate", "no", "out2"), hopList(fork, "in", "out2"))
        assertEquals(listOf("gate", "yes"), hopList(fork, "in", "yes"))
    }

    @Test
    fun crossingBetweenBranchesWalksBackToTheSharedStation() {
        assertEquals(listOf("gate", "yes"), hopList(fork, "no", "yes"))
        assertEquals(listOf("no", "gate", "yes"), hopList(fork, "out2", "yes").takeLast(3))
    }

    @Test
    fun unknownStationsJumpStraightToTheTarget() {
        assertEquals(listOf("box"), hopList(line, "ghost", "box"))
    }

    @Test
    fun gridPlacesPathsInRowsAndSharedStationsAcrossThem() {
        val stations = listOf("in", "gate", "yes", "out", "no", "out2").map { StationDef(it, it) }
        val cells = stationCells(stations, fork)
        assertEquals(2, cells.getValue("in").rowSpan)
        assertEquals(0, cells.getValue("yes").row)
        assertEquals(1, cells.getValue("no").row)
        assertEquals(2, cells.getValue("yes").col)
        assertEquals(listOf("in" to "gate", "gate" to "yes", "yes" to "out", "gate" to "no", "no" to "out2"), arrowsOf(fork))
    }

    @Test
    fun withoutPathsAllStationsFormOneLine() {
        val spec = specOf("flow", """{"title":"t","intro":"i","stations":[{"id":"a","label":"A"},{"id":"b","label":"B"}]}""")
        assertEquals(listOf(listOf("a", "b")), pathsOf(spec))
    }

    @Test
    fun statesRingAndRowPositions() {
        val ring = nodePositions(4, "ring")
        assertEquals(4, ring.size)
        assertEquals(50.0, ring[0].x, 0.001)
        assertTrue(ring[0].y < 50.0)
        val row = nodePositions(3, "row")
        assertTrue(row[0].x < row[1].x && row[1].x < row[2].x)
        assertEquals(50.0, row[1].y, 0.001)
    }

    @Test
    fun edgesStopShortOfTheNodesAndPairedEdgesDoNotOverlap() {
        val a = Pt(10.0, 50.0)
        val b = Pt(90.0, 50.0)
        val one = assertNotNull(edgeGeom(a, b, false))
        assertTrue(one.x1 > a.x && one.x2 < b.x)
        val ab = assertNotNull(edgeGeom(a, b, true))
        val ba = assertNotNull(edgeGeom(b, a, true))
        assertTrue(ab.y1 != ba.y1)
        assertNull(edgeGeom(a, a, false))
    }

    @Test
    fun currentNodeIsTheOneWithTheTokenElseTheLitOnes() {
        val nodes = listOf(StateNode("red", "Red"), StateNode("green", "Green"))
        assertEquals(setOf("green"), currentNodes(nodes, listOf(SimItem("t", "token", "green")), listOf("red")))
        assertEquals(setOf("red"), currentNodes(nodes, emptyList(), listOf("red", "red>green")))
    }
}
