package dev.kotree.site.sims

import dev.kotree.site.sims.core.ChartMath
import dev.kotree.site.sims.core.SimCore
import dev.kotree.site.sims.layouts.barValue
import dev.kotree.site.sims.layouts.markValues
import dev.kotree.site.sims.layouts.marksOf
import dev.kotree.site.sims.layouts.xRangeOf
import dev.kotree.site.sims.layouts.xSamples
import dev.kotree.site.sims.layouts.yBoundsOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChartScaleTest {
    private val json = """{"title":"t","intro":"i","axes":{"x":"n","y":"minutes"},
        "bars":[{"id":"big","label":"Big","value":500},{"id":"live","label":"Live","value":{"var":"v"}}],
        "curves":[{"id":"lin","label":"3n + 2","f":{"poly":[2,3,0]}},{"id":"exp","label":"2^n","f":"pow2"}],
        "bands":[{"id":"cap","label":"Cap","kind":"ceiling","f":{"poly":[0,1,0]},"k":9000}],
        "marks":{"start":0,"stop":20,"step":5},"scale":{"x":[0,20],"y":"auto"},"vars":{"v":700}}"""

    @Test
    fun autoYScaleCoversEverySeries() {
        val s = specOf("chart", json)
        val st = SimCore.initial(s)
        val (lo, hi) = xRangeOf(s)
        assertEquals(0.0, lo)
        assertEquals(20.0, hi)
        val b = yBoundsOf(s, st, lo, hi)
        s.bars.forEach { assertTrue(barValue(it, st) <= b.yMax) }
        xSamples(lo, hi).forEach { x ->
            s.curves.forEach { c -> assertTrue(ChartMath.eval(c.fn, x) <= b.yMax && ChartMath.eval(c.fn, x) >= b.yMin) }
            s.bands.forEach { c -> assertTrue(ChartMath.bandAt(c, x) <= b.yMax) }
        }
        // 2^20 = 1,048,576 is the tallest series.
        assertTrue(b.yMax >= 1_048_576.0)
        assertEquals(0.0, b.yMin)
    }

    @Test
    fun barValueFollowsItsVariable() {
        val s = specOf("chart", json)
        assertEquals(700.0, barValue(s.bars[1], SimCore.initial(s)))
    }

    @Test
    fun marksCountUpAndDown() {
        assertEquals(listOf(0.0, 5.0, 10.0, 15.0), marksOf(specOf("chart", json)))
        assertEquals(listOf(0.0, 5.0, 10.0, 15.0, 20.0), markValues(0.0, 20.0, 5.0, inclusive = true, down = false))
        assertEquals(listOf(10.0, 7.0, 4.0, 1.0), markValues(10.0, 0.0, 3.0, inclusive = false, down = true))
        assertEquals(listOf(9.0, 6.0, 3.0, 0.0), markValues(9.0, 0.0, 3.0, inclusive = true, down = true))
        assertEquals(listOf(9.0, 6.0, 3.0), markValues(9.0, 0.0, 3.0, inclusive = false, down = true))
        assertTrue(markValues(0.0, 1.0, 0.0, inclusive = false, down = false).isEmpty())
    }
}
