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
import dev.kotree.site.colorFeedbackCorrectVar
import dev.kotree.site.colorFeedbackWrongVar
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.sims.SimStage
import dev.kotree.site.sims.core.SimItem
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * ADR 009 section 2, template `states`: modes and transitions. Nodes sit on a ring or in a row (computed here, the data
 * holds no coordinates). Edges are straight lines in one SVG drawn from a string, with a text label at the midpoint. The
 * current node is the one that holds a token item, or the ones named by `light`; an edge lights when its id "from>to" is
 * in the lit set. The layout draws only; actions in the host fire the transitions through `move`/`light` effects.
 */

private val tokenSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}

private const val STAGE_UNITS = 100.0
private const val NODE_SHORTEN = 13.0
private const val PAIR_OFFSET = 3.0

/** A node as the layout draws it. */
class StateNode(val id: String, val label: String)

/** A transition: event id is "from>to". */
class StateEdge(val from: String, val to: String, val label: String) {
    val id: String get() = "$from>$to"
}

fun nodesOf(spec: SimSpec): List<StateNode> {
    val raw = spec.raw.nodes ?: return emptyList()
    return raw.unsafeCast<Array<dynamic>>().map { StateNode(it.id.toString(), it.label.toString()) }
}

fun edgesOf(spec: SimSpec): List<StateEdge> {
    val raw = spec.raw.edges ?: return emptyList()
    return raw.unsafeCast<Array<dynamic>>().map {
        StateEdge(it.from.toString(), it.to.toString(), if (it.label == null || it.label == undefined) "" else it.label.toString())
    }
}

/** A point on the 100 x 100 stage, in percent. */
class Pt(val x: Double, val y: Double)

/** Node centres: `ring` spreads them on an ellipse starting at the top; `row` puts them on one line. */
fun nodePositions(count: Int, layout: String): List<Pt> {
    if (count <= 0) return emptyList()
    if (layout == "row" || count == 1) return List(count) { Pt((it + 0.5) * STAGE_UNITS / count, 50.0) }
    return List(count) {
        val angle = -PI / 2 + it * 2 * PI / count
        Pt(50.0 + 36.0 * cos(angle), 50.0 + 34.0 * sin(angle))
    }
}

/** A drawn edge: the line from [x1],[y1] to [x2],[y2] (shortened so it stops at the node boxes) and its label point. */
class EdgeGeom(val x1: Double, val y1: Double, val x2: Double, val y2: Double, val mx: Double, val my: Double)

/**
 * Line for one edge. [paired] true when the opposite edge also exists: both are pushed to their own side so they do not
 * overlap. Null for a self-loop (drawn as a label only) or when the nodes coincide.
 */
fun edgeGeom(a: Pt, b: Pt, paired: Boolean): EdgeGeom? {
    val dx = b.x - a.x
    val dy = b.y - a.y
    val len = sqrt(dx * dx + dy * dy)
    if (len < 1.0) return null
    val ux = dx / len
    val uy = dy / len
    val off = if (paired) PAIR_OFFSET else 0.0
    val ox = -uy * off
    val oy = ux * off
    val cut = NODE_SHORTEN.coerceAtMost(len / 2.5)
    val x1 = a.x + ux * cut + ox
    val y1 = a.y + uy * cut + oy
    val x2 = b.x - ux * cut + ox
    val y2 = b.y - uy * cut + oy
    return EdgeGeom(x1, y1, x2, y2, (x1 + x2) / 2, (y1 + y2) / 2)
}

/** Ids of the nodes that are current: those with a token item on them, else the lit nodes. */
fun currentNodes(nodes: List<StateNode>, items: List<SimItem>, lit: List<String>): Set<String> {
    val ids = nodes.map { it.id }.toSet()
    val withToken = items.map { it.at }.filter { it in ids }.toSet()
    return if (withToken.isNotEmpty()) withToken else lit.filter { it in ids }.toSet()
}

private fun f(v: Double): String = (kotlin.math.round(v * 100) / 100).toString()

private const val VIEW_W = 160.0
private const val VIEW_H = 90.0
private const val HEAD_LEN = 3.2
private const val HEAD_HALF = 1.6

/** The edge lines and arrowheads as one SVG string. Stage is 16:9, so the 160 x 90 view scales evenly. Colours come in as CSS values. */
fun linesSvg(edges: List<StateEdge>, geoms: List<EdgeGeom?>, lit: Set<String>, litColor: String, offColor: String): String {
    val sb = StringBuilder()
    sb.append("<svg viewBox=\"0 0 ${VIEW_W.toInt()} ${VIEW_H.toInt()}\" width=\"100%\" height=\"100%\" aria-hidden=\"true\" focusable=\"false\">")
    edges.forEachIndexed { i, e ->
        val g = geoms[i] ?: return@forEachIndexed
        val on = e.id in lit
        val x1 = g.x1 * VIEW_W / STAGE_UNITS
        val y1 = g.y1 * VIEW_H / STAGE_UNITS
        val x2 = g.x2 * VIEW_W / STAGE_UNITS
        val y2 = g.y2 * VIEW_H / STAGE_UNITS
        val dx = x2 - x1
        val dy = y2 - y1
        val len = sqrt(dx * dx + dy * dy).coerceAtLeast(0.001)
        val ux = dx / len
        val uy = dy / len
        val bx = x2 - ux * HEAD_LEN
        val by = y2 - uy * HEAD_LEN
        val color = if (on) litColor else offColor
        sb.append("<g style=\"color:$color\">")
        sb.append("<line x1=\"${f(x1)}\" y1=\"${f(y1)}\" x2=\"${f(bx)}\" y2=\"${f(by)}\" stroke=\"currentColor\"")
        sb.append(" stroke-width=\"${if (on) 0.9 else 0.4}\"${if (on) "" else " stroke-dasharray=\"1.5 1\""}/>")
        sb.append("<polygon fill=\"currentColor\" points=\"${f(x2)},${f(y2)} ${f(bx - uy * HEAD_HALF)},${f(by + ux * HEAD_HALF)} ${f(bx + uy * HEAD_HALF)},${f(by - ux * HEAD_HALF)}\"/>")
        sb.append("</g>")
    }
    sb.append("</svg>")
    return sb.toString()
}

@Composable
fun StatesLayout(stage: SimStage) {
    val nodes = remember(stage.spec) { nodesOf(stage.spec) }
    val edges = remember(stage.spec) { edgesOf(stage.spec) }
    val layout = remember(stage.spec) { stage.spec.raw.layout?.toString() ?: "ring" }
    val start = remember(stage.spec) { stage.spec.raw.start?.toString() }
    val points = remember(stage.spec) { nodePositions(nodes.size, layout) }
    val index = remember(stage.spec) { nodes.mapIndexed { i, n -> n.id to i }.toMap() }
    val pairs = remember(stage.spec) { edges.map { e -> edges.any { it.from == e.to && it.to == e.from && it.from != it.to } } }
    val geoms = remember(stage.spec) {
        edges.mapIndexed { i, e ->
            val a = index[e.from]
            val b = index[e.to]
            if (a == null || b == null || a == b) null else edgeGeom(points[a], points[b], pairs[i])
        }
    }
    val current = currentNodes(nodes, stage.state.items, stage.state.lit)
    val litEdges = stage.state.lit.toSet()
    val ink = branchInk(stage.branch).value().toString()
    val svg = linesSvg(edges, geoms, litEdges, ink, colorOutlineVar.value().toString())

    Div(
        Modifier.styleModifier {
            property("position", "relative")
            property("width", "100%")
            property("height", "100%")
            property("box-sizing", "border-box")
            typo(Typo.Body)
        }.toAttrs()
    ) {
        key(svg) {
            Div(
                Modifier.styleModifier {
                    property("position", "absolute")
                    property("inset", "0")
                    property("pointer-events", "none")
                }.toAttrs { ref { el -> el.innerHTML = svg; onDispose { } } }
            ) {}
        }
        edges.forEachIndexed { i, e ->
            val g = geoms[i]
            val pt = g?.let { Pt(it.mx, it.my) } ?: index[e.from]?.let { Pt(points[it].x, points[it].y - 15.0) } ?: return@forEachIndexed
            if (e.label.isNotEmpty() || g == null) EdgeLabel(e, pt, e.id in litEdges, ink, g == null)
        }
        nodes.forEachIndexed { i, n ->
            key(n.id) { NodeBox(stage, n, points[i], n.id in current, n.id == start) }
        }
    }
}

@Composable
private fun EdgeLabel(e: StateEdge, at: Pt, on: Boolean, ink: String, loop: Boolean) {
    val text = if (loop) "${e.label.ifEmpty { "again" }} (back to ${e.to})" else e.label
    Span(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${at.x}%")
            property("top", "${at.y}%")
            property("transform", "translate(-50%, -50%)")
            property("padding", "0 ${space1Var.value()}")
            property("border-radius", shapeFullVar.value())
            property("background-color", colorSurfaceVar.value())
            property("color", if (on) ink else colorOnSurfaceMutedVar.value().toString())
            property("white-space", "nowrap")
            if (on) property("font-weight", "700")
            typo(Typo.Label)
        }.toAttrs { attr("role", "note"); attr("aria-label", "From ${e.from} to ${e.to}: ${e.label}${if (on) ", just used" else ""}") }
    ) { Text(text) }
}

@Composable
private fun NodeBox(stage: SimStage, n: StateNode, at: Pt, isNow: Boolean, isStart: Boolean) {
    val ink = branchInk(stage.branch).value().toString()
    val tokens = stage.state.items.filter { it.at == n.id }
    val width = (if (isNow) 3 else 1) * Tokens.Elevation.borderPx
    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${at.x}%")
            property("top", "${at.y}%")
            property("transform", "translate(-50%, -50%)")
            property("display", "flex")
            property("flex-direction", "column")
            property("align-items", "center")
            property("gap", space1Var.value())
            property("min-width", "${Tokens.Spacing.minTargetPx}px")
            property("padding", "${space2Var.value()} ${space3Var.value()}")
            property("border", "${width}px solid ${if (isNow) ink else colorOutlineVar.value().toString()}")
            property("border-radius", shapeMdVar.value())
            property("background-color", if (isNow) branchTint(stage.branch).value().toString() else colorSurfaceVar.value().toString())
            property("color", colorOnSurfaceVar.value())
            typo(Typo.Label)
        }.toAttrs {
            attr("role", "group")
            attr("aria-label", n.label)
            if (isNow) attr("aria-current", "step")
        }
    ) {
        Span(Modifier.styleModifier { typo(Typo.Title); property("color", ink) }.toAttrs()) { Text(n.label) }
        if (isNow) Span(Modifier.styleModifier { typo(Typo.Label) }.toAttrs()) { Text("now") }
        if (isStart) Span(Modifier.styleModifier { typo(Typo.Label); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) { Text("start") }
        tokens.forEach { item -> key(item.id) { TokenChip(stage, item) } }
    }
}

@Composable
private fun TokenChip(stage: SimStage, item: SimItem) {
    val mark = stage.state.markOf(item.id)
    val label = stage.state.labels[item.id] ?: item.label
    val cue = markCue(mark)
    val ink = branchInk(stage.branch).value().toString()
    val border = when (mark) {
        "good" -> colorFeedbackCorrectVar.value().toString()
        "bad" -> colorFeedbackWrongVar.value().toString()
        else -> ink
    }
    val reduced = stage.reducedMotion
    Span(
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("gap", space1Var.value())
            property("padding", "0 ${space2Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px ${if (mark == "changed" || mark == "ghost") "dashed" else "solid"} $border")
            property("border-radius", shapeMdVar.value())
            property("background-color", ink)
            property("color", colorOnBranchVar.value())
            typo(Typo.Label)
        }.toAttrs { ref { el -> if (!reduced) enter(el); onDispose { } } }
    ) {
        Text(label)
        if (cue != null) Text(" ($cue)")
    }
}

/** The token arrives with a spring scale and fade, once per mount. */
private fun enter(el: Element) {
    val (seconds, easing) = tokenSpring
    val keyframes = js("([{ transform: 'scale(0.6)', opacity: 0 }, { transform: 'none', opacity: 1 }])")
    val options = js("({})")
    options.duration = seconds * 1000.0
    options.easing = easing
    el.asDynamic().animate(keyframes, options)
}
