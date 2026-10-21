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
import dev.kotree.site.shapeXlVar
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

/*
 * ADR 009 section 2, template `flow`: pipelines, routes, forks. Stations sit in a grid (column = step along a path, row =
 * path; a station shared by all paths spans every row). Items are chips inside the station they are at. A `move` to a
 * non-adjacent station hops along the path: the chip animates through every station on the way (Web Animations, one
 * animation per mount, so nothing runs while idle). Under reduced motion the chip simply appears; the host adds the fade.
 */

private const val ATTR_STATION = "data-station"

private val hopSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}

/** A station as the layout draws it. */
class StationDef(val id: String, val label: String)

/** The stations of a `flow` spec, read from its raw JSON (at most 10). */
fun stationsOf(spec: SimSpec): List<StationDef> {
    val raw = spec.raw.stations ?: return emptyList()
    return raw.unsafeCast<Array<dynamic>>().map { StationDef(it.id.toString(), it.label.toString()) }
}

/** The paths of a `flow` spec (at most 3, sharing the first station). With none declared, all stations form one path. */
fun pathsOf(spec: SimSpec): List<List<String>> {
    val raw = spec.raw.paths
    if (raw != null && raw != undefined) {
        val declared = raw.unsafeCast<Array<dynamic>>().map { p -> p.unsafeCast<Array<dynamic>>().map { it.toString() } }
        if (declared.isNotEmpty()) return declared
    }
    return listOf(stationsOf(spec).map { it.id })
}

/** The first path that holds [target]: how a fork picks its way. Null when no path holds it. */
fun pathHolding(paths: List<List<String>>, target: String): List<String>? = paths.firstOrNull { target in it }

/**
 * The stations an item passes when it goes from [from] to [to], excluding [from] and ending with [to]. Along one path it
 * walks forward or back. Across two paths of a fork it first walks back to the shared station, then forward.
 * When no path knows either end, the item jumps straight to [to].
 */
fun hopList(paths: List<List<String>>, from: String, to: String): List<String> {
    if (from == to) return emptyList()
    val both = paths.firstOrNull { from in it && to in it }
    if (both != null) {
        val a = both.indexOf(from)
        val b = both.indexOf(to)
        return if (b > a) both.subList(a + 1, b + 1) else (a - 1 downTo b).map { i -> both[i] }
    }
    val fromPath = pathHolding(paths, from)
    val toPath = pathHolding(paths, to)
    if (fromPath == null || toPath == null) return listOf(to)
    var j = 0
    while (j + 1 < fromPath.size && j + 1 < toPath.size && fromPath[j + 1] == toPath[j + 1]) j++
    val out = ArrayList<String>()
    for (i in fromPath.indexOf(from) - 1 downTo j) out.add(fromPath[i])
    for (i in j + 1..toPath.indexOf(to)) out.add(toPath[i])
    return out
}

/** Grid cell of a station: column and row (both 0-based) and how many rows it spans. */
class StationCell(val col: Int, val row: Int, val rowSpan: Int)

/** Cells for every station: path r fills row r; a station shared by every path spans all rows; unplaced stations follow in row 0. */
fun stationCells(stations: List<StationDef>, paths: List<List<String>>): Map<String, StationCell> {
    val out = LinkedHashMap<String, StationCell>()
    val rows = paths.size.coerceAtLeast(1)
    paths.forEachIndexed { r, path ->
        path.forEachIndexed { c, id ->
            if (id !in out) {
                val shared = paths.size > 1 && paths.all { id in it && it.indexOf(id) == c }
                out[id] = if (shared) StationCell(c, 0, rows) else StationCell(c, r, 1)
            }
        }
    }
    var next = (out.values.maxOfOrNull { it.col } ?: -1) + 1
    stations.forEach { if (it.id !in out) out[it.id] = StationCell(next++, 0, 1) }
    return out
}

/** Pairs of consecutive stations across all paths, each once: where an arrow is drawn. */
fun arrowsOf(paths: List<List<String>>): List<Pair<String, String>> =
    paths.flatMap { p -> p.zipWithNext() }.distinct()

@Composable
fun FlowLayout(stage: SimStage) {
    val stations = remember(stage.spec) { stationsOf(stage.spec) }
    val paths = remember(stage.spec) { pathsOf(stage.spec) }
    val cells = remember(stage.spec) { stationCells(stations, paths) }
    val arrows = remember(stage.spec) { arrowsOf(paths) }
    val lastAt = remember(stage.spec) { HashMap<String, String>() }
    val ink = branchInk(stage.branch).value().toString()
    val rows = cells.values.maxOfOrNull { it.row + it.rowSpan } ?: 1
    val cols = (cells.values.maxOfOrNull { it.col } ?: 0) + 1

    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("grid-template-columns", "repeat($cols, minmax(0, 1fr) auto)")
            property("grid-template-rows", "repeat($rows, minmax(0, 1fr))")
            property("align-items", "stretch")
            property("column-gap", space2Var.value())
            property("row-gap", space3Var.value())
            property("width", "100%")
            property("height", "100%")
            property("padding", space3Var.value())
            property("box-sizing", "border-box")
            typo(Typo.Body)
        }.toAttrs()
    ) {
        stations.forEach { st ->
            val cell = cells[st.id] ?: return@forEach
            key(st.id) { StationBox(stage, st, cell, lastAt, paths) }
        }
        arrows.forEach { (a, b) ->
            val ca = cells[a] ?: return@forEach
            if (cells[b] == null) return@forEach
            Span(
                Modifier.styleModifier {
                    property("grid-column", "${ca.col * 2 + 2}")
                    property("grid-row", "${ca.row + 1} / span ${ca.rowSpan}")
                    property("align-self", "center")
                    property("color", ink)
                    typo(Typo.Title)
                }.toAttrs { attr("aria-hidden", "true") }
            ) { Text("→") }
        }
    }
}

@Composable
private fun StationBox(
    stage: SimStage, st: StationDef, cell: StationCell, lastAt: HashMap<String, String>, paths: List<List<String>>,
) {
    val items = stage.state.items.filter { it.at == st.id }
    val ink = branchInk(stage.branch).value().toString()
    val lit = st.id in stage.state.lit
    val width = (if (lit) 2 else 1) * Tokens.Elevation.borderPx
    Div(
        Modifier.styleModifier {
            property("grid-column", "${cell.col * 2 + 1}")
            property("grid-row", "${cell.row + 1} / span ${cell.rowSpan}")
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
            property("padding", space3Var.value())
            property("min-width", "0")
            property("border", "${width}px solid ${if (lit) ink else colorOutlineVar.value().toString()}")
            property("border-radius", shapeXlVar.value())
            property("background-color", if (lit) branchTint(stage.branch).value().toString() else colorSurfaceVar.value().toString())
        }.toAttrs {
            attr(ATTR_STATION, st.id)
            attr("role", "group")
            attr("aria-label", st.label)
            if (lit) attr("aria-current", "step")
        }
    ) {
        Span(Modifier.styleModifier { property("color", ink); typo(Typo.Title) }.toAttrs()) {
            Text(if (lit) "${st.label} (now)" else st.label)
        }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("align-content", "flex-start")
                property("gap", space2Var.value())
                property("flex", "1")
                property("min-height", "${Tokens.Spacing.minTargetPx}px")
            }.toAttrs()
        ) {
            items.forEach { item -> key(item.id) { FlowChip(stage, item, lastAt, paths) } }
        }
    }
}

@Composable
private fun FlowChip(stage: SimStage, item: SimItem, lastAt: HashMap<String, String>, paths: List<List<String>>) {
    val mark = stage.state.markOf(item.id)
    val label = stage.state.labels[item.id] ?: item.label
    val cue = markCue(mark)
    val ink = branchInk(stage.branch).value().toString()
    val ghost = mark == "ghost"
    val borderColor = when (mark) {
        "good" -> colorFeedbackCorrectVar.value().toString()
        "bad" -> colorFeedbackWrongVar.value().toString()
        else -> ink
    }
    val reduced = stage.reducedMotion
    val at = item.at
    Span(
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("gap", space2Var.value())
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("padding", "0 ${space3Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px ${if (mark == "changed" || ghost) "dashed" else "solid"} $borderColor")
            property("border-radius", shapeMdVar.value())
            property("background-color", colorSurfaceVar.value())
            property("color", if (ghost) colorOnSurfaceMutedVar.value().toString() else colorOnSurfaceVar.value().toString())
            typo(Typo.Label)
        }.toAttrs {
            ref { el ->
                val prev = lastAt[item.id]
                lastAt[item.id] = at
                if (!reduced) animateHops(el, prev, hopList(paths, prev ?: at, at), at)
                onDispose { }
            }
        }
    ) {
        Text(label)
        if (cue != null) Span(
            Modifier.styleModifier {
                property("padding", "0 ${space1Var.value()}")
                property("border-radius", shapeFullVar.value())
                property("background-color", ink)
                property("color", colorOnBranchVar.value())
                typo(Typo.Label)
            }.toAttrs()
        ) { Text(cue) }
    }
}

// ---- browser plumbing ----

private fun stationCenter(from: Element, id: String): Pair<Double, Double>? {
    val root = from.closest("[$ATTR_STATION]")?.parentElement ?: return null
    val node = root.asDynamic().querySelector("[$ATTR_STATION=\"$id\"]") ?: return null
    if (node == undefined) return null
    val r = node.getBoundingClientRect()
    return Pair((r.left as Double) + (r.width as Double) / 2, (r.top as Double) + (r.height as Double) / 2)
}

/**
 * Runs once when a chip mounts in a new station: the chip starts at the station it came from and passes every hop on the
 * way. A new item (no previous place) fades in. Each segment eases with the M3E spatial spring.
 */
private fun animateHops(el: Element, prev: String?, hops: List<String>, now: String) {
    val (seconds, easing) = hopSpring
    val here = stationCenter(el, now)
    val steps = ArrayList<Pair<Double, Double>>()
    if (prev != null && here != null) {
        (listOf(prev) + hops.dropLast(1)).forEach { id ->
            val c = stationCenter(el, id)
            if (c != null) steps.add(Pair(c.first - here.first, c.second - here.second))
        }
    }
    val frames = js("([])")
    val options = js("({})")
    if (steps.isEmpty()) {
        frames.push(js("({ opacity: 0 })"))
        frames.push(js("({ opacity: 1 })"))
        options.duration = seconds * 1000.0
        options.easing = easing
    } else {
        steps.forEach { (dx, dy) ->
            val f = js("({})")
            f.transform = "translate(${dx}px, ${dy}px)"
            f.easing = easing
            frames.push(f)
        }
        val last = js("({})")
        last.transform = "none"
        frames.push(last)
        options.duration = seconds * 1000.0 * steps.size
    }
    el.asDynamic().animate(frames, options)
}
