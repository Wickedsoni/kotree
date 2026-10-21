package dev.kotree.site.sims.core

import kotlin.math.ln
import kotlin.math.pow

/**
 * Chart arithmetic of ADR 009 section 2: `poly`, `log2`, `nlog2`, `pow2` and the band exit. No expression evaluation.
 *
 * Band convention (the validated keys `{id, label, f, k}` carry no direction, so this is chosen here): a band is the line
 * `k * f(x)`; the curve it bounds is the first curve of the sim. A band whose id or label contains "floor" or "lower" is
 * a floor (the curve must stay at or above the line); every other band is a ceiling (at or below). `exit` is the first
 * integer `x >= n0` where the curve is outside, searched up to [SEARCH_LIMIT] values; no exit in that range gives null.
 */
object ChartMath {
    /** How many integers past n0 `exit` looks at before it says "none". */
    const val SEARCH_LIMIT = 10_000

    private const val LN2 = 0.6931471805599453

    fun eval(fn: CurveFn, x: Double): Double = when (fn) {
        is CurveFn.Poly -> fn.c0 + fn.c1 * x + fn.c2 * x * x
        CurveFn.Log2 -> if (x <= 0.0) 0.0 else ln(x) / LN2
        CurveFn.NLog2 -> if (x <= 0.0) 0.0 else x * ln(x) / LN2
        CurveFn.Pow2 -> 2.0.pow(x)
    }

    fun isFloor(band: SimBandSpec): Boolean {
        val t = (band.id + " " + band.label).lowercase()
        return "floor" in t || "lower" in t
    }

    /** Value of the band line at [x]. */
    fun bandAt(band: SimBandSpec, x: Double): Double = band.k * eval(band.fn, x)

    /** True when [curve] is inside [band] at [x] (equal counts as inside). */
    fun inside(curve: SimCurveSpec, band: SimBandSpec, x: Double): Boolean {
        val c = eval(curve.fn, x)
        val b = bandAt(band, x)
        return if (isFloor(band)) c >= b else c <= b
    }

    /** First integer x >= [n0] where the first curve of [spec] leaves [band], or null when it never does in range. */
    fun bandExit(spec: SimSpec, band: SimBandSpec, n0: Double): Int? {
        val curve = spec.curves.firstOrNull() ?: return null
        val start = kotlin.math.ceil(n0).toInt()
        for (x in start..start + SEARCH_LIMIT) if (!inside(curve, band, x.toDouble())) return x
        return null
    }

    /**
     * Published variables (ADR 009 section 2): `<curve>.at` is the curve at the `x` variable, `<band>.exit` the band exit
     * from the `n0` variable ("none" for no exit). Null when [name] is neither.
     */
    fun published(spec: SimSpec, vars: Map<String, SimValue>, name: String): String? {
        fun num(key: String): Double? = (vars[key] as? Double)
        if (name.endsWith(".at")) {
            val curve = spec.curves.firstOrNull { it.id == name.removeSuffix(".at") } ?: return null
            return eval(curve.fn, num("x") ?: return null).roundedShown()
        }
        if (name.endsWith(".exit")) {
            val band = spec.bands.firstOrNull { it.id == name.removeSuffix(".exit") } ?: return null
            return bandExit(spec, band, num("n0") ?: return null)?.toString() ?: "none"
        }
        return null
    }

    /** Three decimals at most, no trailing zeros, so a live-region sentence stays short. */
    private fun Double.roundedShown(): String = (kotlin.math.round(this * 1000.0) / 1000.0).shown()
}
