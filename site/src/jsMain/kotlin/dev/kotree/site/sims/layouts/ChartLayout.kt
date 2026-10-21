package dev.kotree.site.sims.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeMdVar
import dev.kotree.site.sims.SimStage
import dev.kotree.site.sims.core.BandKind
import dev.kotree.site.sims.core.ChartMath
import dev.kotree.site.sims.core.SimBarSpec
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.sims.core.SimState
import dev.kotree.site.sims.core.shown
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.math.max
import kotlin.math.min

/*
 * ADR 009 section 2, template `chart`: growth, sizes, measurements, number lines. Bars are DOM columns that scale on
 * `transform` (a spring move; none under reduced motion). Curves and bands are one inline SVG built from numbers only
 * (no author text goes into the markup), stroked with token variables; each series also has a dash pattern and a legend
 * entry, so colour is never the only cue. The y-scale is `auto`: it covers every visible series. The layout keeps no
 * state and runs no timer, so it costs nothing while idle.
 */

private const val SAMPLES = 32
private const val VIEW = 100.0
private const val MAX_MARKS = 40
private const val DEFAULT_X_MAX = 10.0
private const val PAD_SHARE = 0.05
private const val ROUND = 100.0
private val DASHES = listOf("", "8 4", "2 4")

/** Lower and upper end of the y axis. */
class ChartBounds(val yMin: Double, val yMax: Double)

private fun items(d: dynamic): List<dynamic> =
    if (d == null || d == undefined) emptyList() else d.unsafeCast<Array<dynamic>>().toList()

private fun num(d: dynamic, fallback: Double): Double = if (d == null || d == undefined) fallback else (d as Number).toDouble()

/** x range: `scale.x`, else the span of `marks`, else 0 to 10. */
fun xRangeOf(spec: SimSpec): Pair<Double, Double> {
    val sx = spec.raw.scale?.x
    if (sx != null && sx != undefined) {
        val a = items(sx)
        if (a.size == 2) return Pair(num(a[0], 0.0), num(a[1], DEFAULT_X_MAX))
    }
    val m = spec.raw.marks
    if (m != null && m != undefined) {
        val a = num(m.start, 0.0)
        val b = num(m.stop, DEFAULT_X_MAX)
        if (a != b) return Pair(min(a, b), max(a, b))
    }
    return Pair(0.0, DEFAULT_X_MAX)
}

/** [SAMPLES] + 1 evenly spaced x values from [lo] to [hi], where curves and bands are drawn. */
fun xSamples(lo: Double, hi: Double): List<Double> = (0..SAMPLES).map { lo + (hi - lo) * it / SAMPLES }

/** Value of a bar: its fixed value, else the number in its variable, else 0. */
fun barValue(bar: SimBarSpec, state: SimState): Double =
    bar.value ?: (state.vars[bar.valueVar ?: ""] as? Double) ?: 0.0

/** Auto y-scale: always includes 0, covers every bar, curve sample and band sample, then adds a little headroom. */
fun yBoundsOf(spec: SimSpec, state: SimState, lo: Double, hi: Double): ChartBounds {
    val values = ArrayList<Double>()
    values.add(0.0)
    spec.bars.forEach { values.add(barValue(it, state)) }
    val xs = xSamples(lo, hi)
    spec.curves.forEach { c -> xs.forEach { values.add(ChartMath.eval(c.fn, it)) } }
    spec.bands.forEach { b -> xs.forEach { values.add(ChartMath.bandAt(b, it)) } }
    val top = values.filter { !it.isNaN() && !it.isInfinite() }.maxOrNull() ?: 1.0
    val bottom = values.filter { !it.isNaN() && !it.isInfinite() }.minOrNull() ?: 0.0
    val span = if (top > bottom) top - bottom else 1.0
    return ChartBounds(if (bottom < 0.0) bottom - span * PAD_SHARE else bottom, top + span * PAD_SHARE)
}

/**
 * The x values of `marks`. Counting up: start, start + step, ... below stop (or up to stop when [inclusive]). Counting
 * [down]: start, start - step, ... above stop (or down to stop when [inclusive]). At most [MAX_MARKS] values.
 */
fun markValues(start: Double, stop: Double, step: Double, inclusive: Boolean, down: Boolean): List<Double> {
    if (step <= 0.0) return emptyList()
    val out = ArrayList<Double>()
    var i = 0
    while (out.size < MAX_MARKS) {
        val v = if (down) start - i * step else start + i * step
        val past = if (down) (if (inclusive) v < stop else v <= stop) else (if (inclusive) v > stop else v >= stop)
        if (past) break
        out.add(v)
        i++
    }
    return out
}

/** The marks of a spec, empty when it declares none. */
fun marksOf(spec: SimSpec): List<Double> {
    val m = spec.raw.marks
    if (m == null || m == undefined) return emptyList()
    return markValues(num(m.start, 0.0), num(m.stop, 0.0), num(m.step, 1.0), m.inclusive == true, m.down == true)
}

private fun r(v: Double): String = (kotlin.math.round(v * ROUND) / ROUND).toString()

private fun points(fn: (Double) -> Double, lo: Double, hi: Double, b: ChartBounds): String {
    val span = b.yMax - b.yMin
    return xSamples(lo, hi).joinToString(" ") { x ->
        val v = fn(x).let { if (it.isNaN() || it.isInfinite()) b.yMin else it }
        "${r((x - lo) / (hi - lo) * VIEW)},${r(VIEW - (v - b.yMin) / span * VIEW)}"
    }
}

private fun svgOf(stage: SimStage, lo: Double, hi: Double, b: ChartBounds): String {
    val ink = branchInk(stage.branch).value().toString()
    val quiet = colorOutlineVar.value().toString()
    val w = 2 * Tokens.Elevation.borderPx
    val spec = stage.spec
    val sb = StringBuilder()
    sb.append("<svg viewBox=\"0 0 ${r(VIEW)} ${r(VIEW)}\" preserveAspectRatio=\"none\" width=\"100%\" height=\"100%\" aria-hidden=\"true\" focusable=\"false\" style=\"display:block;overflow:visible\">")
    fun line(pts: String, color: String, width: Int, dash: String) {
        sb.append("<polyline fill=\"none\" points=\"$pts\" style=\"stroke:$color;stroke-width:$width;vector-effect:non-scaling-stroke\"")
        if (dash.isNotEmpty()) sb.append(" stroke-dasharray=\"$dash\"")
        sb.append("/>")
    }
    spec.bands.forEachIndexed { i, band -> line(points({ ChartMath.bandAt(band, it) }, lo, hi, b), quiet, w, DASHES[(i + 1) % DASHES.size]) }
    spec.curves.forEachIndexed { i, c -> line(points({ ChartMath.eval(c.fn, it) }, lo, hi, b), ink, w, DASHES[i % DASHES.size]) }
    val x = stage.state.vars["x"] as? Double
    if (x != null && x >= lo && x <= hi) {
        val px = r((x - lo) / (hi - lo) * VIEW)
        line("$px,0 $px,${r(VIEW)}", colorOnSurfaceVar.value().toString(), Tokens.Elevation.borderPx, DASHES[2])
    }
    sb.append("</svg>")
    return sb.toString()
}

private val barSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}

@Composable
fun ChartLayout(stage: SimStage) {
    val spec = stage.spec
    val (lo, hi) = remember(spec) { xRangeOf(spec) }
    val bounds = yBoundsOf(spec, stage.state, lo, hi)
    val marks = remember(spec) { marksOf(spec) }
    val ink = branchInk(stage.branch).value().toString()
    val xLabel = spec.raw.axes?.x?.toString() ?: ""
    val yLabel = spec.raw.axes?.y?.toString() ?: ""
    val hasPlot = spec.curves.isNotEmpty() || spec.bands.isNotEmpty()

    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
            property("width", "100%")
            property("height", "100%")
            property("padding", space3Var.value())
            property("box-sizing", "border-box")
            typo(Typo.Body)
        }.toAttrs { attr("role", "group"); attr("aria-label", "Chart: $yLabel against $xLabel") }
    ) {
        Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()); typo(Typo.Label) }.toAttrs()) {
            Text("↑ $yLabel (top: ${bounds.yMax.shown()})")
        }
        if (spec.bars.isNotEmpty()) Bars(stage, bounds)
        if (hasPlot) {
            val svg = svgOf(stage, lo, hi, bounds)
            key(svg) {
                Div(
                    Modifier.styleModifier {
                        property("flex", "1")
                        property("min-height", "0")
                        property("border-left", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                        property("border-bottom", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                    }.toAttrs { ref { el -> el.innerHTML = svg; onDispose { } } }
                ) { }
            }
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("justify-content", "space-between")
                    property("color", colorOnSurfaceMutedVar.value())
                    typo(Typo.Label)
                }.toAttrs()
            ) {
                Span { Text(lo.shown()) }
                Span { Text("$xLabel →") }
                Span { Text(hi.shown()) }
            }
        }
        if (marks.isNotEmpty()) Marks(marks, lo, hi, ink)
        if (hasPlot) Legend(stage, ink)
    }
}

@Composable
private fun Bars(stage: SimStage, b: ChartBounds) {
    val reduced = stage.reducedMotion
    val (seconds, easing) = barSpring
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("align-items", "stretch")
            property("gap", space3Var.value())
            property("flex", "1")
            property("min-height", "0")
        }.toAttrs()
    ) {
        stage.spec.bars.forEach { bar ->
            key(bar.id) {
                val v = barValue(bar, stage.state)
                val share = if (b.yMax > 0.0) (max(v, 0.0) / b.yMax).coerceIn(0.0, 1.0) else 0.0
                Div(
                    Modifier.styleModifier {
                        property("display", "flex")
                        property("flex-direction", "column")
                        property("flex", "1")
                        property("min-width", "0")
                        property("gap", space1Var.value())
                    }.toAttrs()
                ) {
                    Div(
                        Modifier.styleModifier {
                            property("flex", "1")
                            property("min-height", "0")
                            property("display", "flex")
                            property("align-items", "flex-end")
                            property("border-bottom", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                        }.toAttrs()
                    ) {
                        Div(
                            Modifier.styleModifier {
                                property("width", "100%")
                                property("height", "100%")
                                property("background-color", branchTint(stage.branch).value().toString())
                                property("border", "${Tokens.Elevation.borderPx}px solid $ink")
                                property("border-radius", "${shapeMdVar.value()} ${shapeMdVar.value()} 0 0")
                                property("box-sizing", "border-box")
                                property("transform-origin", "bottom")
                                property("transform", "scaleY($share)")
                                if (!reduced) {
                                    property("transition-property", "transform")
                                    property("transition-duration", "${seconds}s")
                                    property("transition-timing-function", easing)
                                }
                            }.toAttrs { attr("aria-hidden", "true") }
                        ) { }
                    }
                    Span(Modifier.styleModifier { property("color", colorOnSurfaceVar.value()); property("text-align", "center"); typo(Typo.Label) }.toAttrs()) {
                        Text("${bar.label}: ${v.shown()}")
                    }
                }
            }
        }
    }
}

@Composable
private fun Marks(marks: List<Double>, lo: Double, hi: Double, ink: String) {
    Div(
        Modifier.styleModifier {
            property("position", "relative")
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("border-top", "${Tokens.Elevation.borderPx}px solid $ink")
        }.toAttrs { attr("role", "list"); attr("aria-label", "Marks: ${marks.joinToString(", ") { it.shown() }}") }
    ) {
        marks.forEach { m ->
            val at = if (hi > lo) ((m - lo) / (hi - lo)).coerceIn(0.0, 1.0) * VIEW else 0.0
            Span(
                Modifier.styleModifier {
                    property("position", "absolute")
                    property("left", "${r(at)}%")
                    property("transform", "translateX(-50%)")
                    property("padding-top", space1Var.value())
                    property("color", colorOnSurfaceVar.value())
                    typo(Typo.Label)
                }.toAttrs { attr("role", "listitem") }
            ) { Text(m.shown()) }
        }
    }
}

@Composable
private fun Legend(stage: SimStage, ink: String) {
    val spec = stage.spec
    val x = stage.state.vars["x"] as? Double
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("gap", "${space1Var.value()} ${space3Var.value()}")
            typo(Typo.Label)
        }.toAttrs()
    ) {
        @Composable fun entry(label: String, dash: Int, color: String, extra: String) {
            Span(Modifier.styleModifier { property("color", colorOnSurfaceVar.value()) }.toAttrs()) {
                Span(
                    Modifier.styleModifier {
                        property("display", "inline-block")
                        property("width", space3Var.value())
                        property("margin-right", space1Var.value())
                        property("border-top", "${2 * Tokens.Elevation.borderPx}px ${listOf("solid", "dashed", "dotted")[dash % 3]} $color")
                    }.toAttrs { attr("aria-hidden", "true") }
                ) { }
                Text(label + extra)
            }
        }
        spec.curves.forEachIndexed { i, c ->
            val at = if (x != null) " (at ${x.shown()}: ${(kotlin.math.round(ChartMath.eval(c.fn, x) * 1000.0) / 1000.0).shown()})" else ""
            entry(c.label, i, ink, at)
        }
        spec.bands.forEachIndexed { i, band ->
            val kind = if (band.kind == BandKind.Floor) "floor" else "ceiling"
            val exit = ChartMath.published(spec, stage.state.vars, "${band.id}.exit")?.let { " (exit: $it)" } ?: ""
            entry("${band.label}, $kind", i + 1, colorOutlineVar.value().toString(), exit)
        }
    }
}
