package dev.kotree.site.sims

import dev.kotree.site.sims.core.ChartMath
import dev.kotree.site.sims.core.SimCore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChartMathTest {
    // The curve is 3n + 2; each band is k times n. "floor" in an id makes a floor, anything else a ceiling.
    private fun chart(floorK: Double, ceilingK: Double, n0: Int) = specOf(
        "chart",
        """{"title":"t","intro":"i","axes":{"x":"n","y":"minutes"},
          "curves":[{"id":"cost","label":"3n + 2","f":{"poly":[2,3,0]}}],
          "bands":[{"id":"floor","label":"Floor","f":{"poly":[0,1,0]},"k":$floorK},{"id":"ceiling","label":"Ceiling","f":{"poly":[0,1,0]},"k":$ceilingK}],
          "vars":{"x":10,"n0":$n0}}""",
    )

    @Test
    fun polyAndFriends() {
        val s = chart(3.0, 4.0, 2)
        assertEquals(32.0, ChartMath.eval(s.curves[0].fn, 10.0))
        val t = specOf(
            "chart",
            """{"title":"t","intro":"i","axes":{"x":"n","y":"y"},"curves":[
              {"id":"a","label":"a","f":"log2"},{"id":"b","label":"b","f":"nlog2"},{"id":"c","label":"c","f":"pow2"}]}""",
        )
        assertEquals(3.0, ChartMath.eval(t.curves[0].fn, 8.0), 1e-9)
        assertEquals(24.0, ChartMath.eval(t.curves[1].fn, 8.0), 1e-9)
        assertEquals(256.0, ChartMath.eval(t.curves[2].fn, 8.0), 1e-9)
    }

    @Test
    fun threeNPlusTwoIsInsideFloor3AndCeiling4FromN0Is2() {
        val s = chart(3.0, 4.0, 2)
        assertNull(ChartMath.bandExit(s, s.bands[0], 2.0), "floor 3")
        assertNull(ChartMath.bandExit(s, s.bands[1], 2.0), "ceiling 4")
        // Before n0 = 2 the ceiling does not hold: at n = 1, 3n + 2 = 5 is above 4n = 4.
        assertEquals(1, ChartMath.bandExit(s, s.bands[1], 1.0))
    }

    @Test
    fun aCeilingOf2Point9IsCrossedAtOnce() {
        val s = chart(3.0, 2.9, 2)
        assertEquals(2, ChartMath.bandExit(s, s.bands[1], 2.0))
    }

    @Test
    fun publishedVariablesFeedInterpolation() {
        val s = chart(3.0, 2.9, 2)
        assertEquals("32", ChartMath.published(s, s.vars, "cost.at"))
        assertEquals("2", ChartMath.published(s, s.vars, "ceiling.exit"))
        assertEquals("none", ChartMath.published(s, s.vars, "floor.exit"))
        assertNull(ChartMath.published(s, s.vars, "nothing.at"))
        assertEquals("At 10 the cost is 32.", SimCore.interpolate(s, SimCore.initial(s), "At {x} the cost is {cost.at}."))
        assertEquals("{unknown}", SimCore.interpolate(s, SimCore.initial(s), "{unknown}"))
    }

    @Test
    fun floorDirectionComesFromTheName() {
        val s = chart(4.0, 4.0, 2)
        assertTrue(ChartMath.isFloor(s.bands[0]))
        assertTrue(!ChartMath.isFloor(s.bands[1]))
        // Floor 4: 3n + 2 >= 4n only up to n = 2, so the floor holds at n0 = 2 and breaks at 3.
        assertEquals(3, ChartMath.bandExit(s, s.bands[0], 2.0))
    }
}
