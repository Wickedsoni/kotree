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
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.fontCodeVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.sims.SimStage
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.sims.core.shown
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/*
 * ADR 009 section 2, template `stepper`: order of execution. Draws `lines` (the lit line is the pointer, marked with an
 * arrow and aria-current so colour is never the only cue), optional `groups` of lines, a `board` of variable cells and a
 * `log` table filled by `row` effects. Read-only: the sim buttons live in the host. No timers, so nothing runs while idle,
 * and nothing animates, so reduced motion needs no special case.
 */

/** A line of code or text. */
class StepLine(val id: String, val text: String)

/** A group (a room, a card): a label and the lines it holds. */
class StepGroup(val id: String, val label: String, val lines: List<StepLine>)

/** A board cell: a variable and the label shown over its value. */
class BoardCell(val variable: String, val label: String)

private fun items(d: dynamic): List<dynamic> =
    if (d == null || d == undefined) emptyList() else d.unsafeCast<Array<dynamic>>().toList()

private fun text(d: dynamic): String = if (d == null || d == undefined) "" else d.toString()

/** The top-level `lines` of a `stepper` spec. */
fun stepLinesOf(spec: SimSpec): List<StepLine> = items(spec.raw.lines).map { StepLine(text(it.id), text(it.text)) }

/** The `groups`; a group line is either an object `{id, text}` or the id of a top-level line. */
fun stepGroupsOf(spec: SimSpec): List<StepGroup> {
    val top = stepLinesOf(spec).associateBy { it.id }
    return items(spec.raw.groups).map { g ->
        StepGroup(text(g.id), text(g.label), items(g.lines).map { l ->
            if (jsTypeOf(l) == "string") StepLine(l.toString(), top[l.toString()]?.text ?: l.toString())
            else StepLine(text(l.id), text(l.text))
        })
    }
}

fun boardOf(spec: SimSpec): List<BoardCell> = items(spec.raw.board).map { BoardCell(text(it.`var`), text(it.label)) }

/** Log column titles; accepts `log: {columns: [..]}`. */
fun logColumnsOf(spec: SimSpec): List<String> {
    val log = spec.raw.log
    return if (log == null || log == undefined) emptyList() else items(log.columns).map { text(it) }
}

/** The newest [max] rows, padded or cut to [columns] cells so a ragged row cannot break the grid. */
fun visibleLogRows(rows: List<List<String>>, columns: Int, max: Int = 8): List<List<String>> =
    rows.takeLast(max).map { r -> List(columns) { r.getOrElse(it) { "" } } }

@Composable
fun StepperLayout(stage: SimStage) {
    val spec = stage.spec
    val lines = remember(spec) { stepLinesOf(spec) }
    val groups = remember(spec) { stepGroupsOf(spec) }
    val board = remember(spec) { boardOf(spec) }
    val columns = remember(spec) { logColumnsOf(spec) }
    val code = spec.raw.code == true
    val lit = stage.state.lit.toSet()
    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("grid-template-columns", "repeat(auto-fit, minmax(0, 1fr))")
            property("grid-auto-flow", "column")
            property("gap", space3Var.value())
            property("width", "100%")
            property("height", "100%")
            property("padding", space3Var.value())
            property("box-sizing", "border-box")
            property("overflow", "auto")
            typo(Typo.Body)
        }.toAttrs()
    ) {
        if (lines.isNotEmpty()) Panel(stage, spec.title) {
            lines.forEach { l -> key(l.id) { LineRow(stage, l, l.id in lit, code) } }
        }
        groups.forEach { g ->
            Panel(stage, g.label) { g.lines.forEach { l -> key(g.id + l.id) { LineRow(stage, l, l.id in lit, code) } } }
        }
        if (board.isNotEmpty()) Panel(stage, "Board") {
            board.forEach { c -> key(c.variable) { BoardRow(stage, c) } }
        }
        if (columns.isNotEmpty()) Panel(stage, "Log") {
            LogTable(columns, visibleLogRows(stage.state.rows, columns.size))
        }
    }
}

@Composable
private fun Panel(stage: SimStage, title: String, content: @Composable () -> Unit) {
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space1Var.value())
            property("padding", space3Var.value())
            property("min-width", "0")
            property("align-self", "start")
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeXlVar.value())
            property("background-color", colorSurfaceVar.value())
        }.toAttrs { attr("role", "group"); attr("aria-label", title) }
    ) {
        Span(Modifier.styleModifier { property("color", branchInk(stage.branch).value().toString()); typo(Typo.Title) }.toAttrs()) { Text(title) }
        content()
    }
}

@Composable
private fun LineRow(stage: SimStage, line: StepLine, isLit: Boolean, code: Boolean) {
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("gap", space2Var.value())
            property("align-items", "baseline")
            property("min-height", "${Tokens.Spacing.minTargetPx / 2}px")
            property("padding", "${space1Var.value()} ${space2Var.value()}")
            property("border-radius", shapeMdVar.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${if (isLit) ink else "transparent"}")
            property("background-color", if (isLit) branchTint(stage.branch).value().toString() else "transparent")
            property("color", colorOnSurfaceVar.value())
            typo(Typo.Label)
            if (code) property("font-family", fontCodeVar.value())
        }.toAttrs { if (isLit) attr("aria-current", "step") }
    ) {
        Span(Modifier.styleModifier { property("color", ink); property("width", space3Var.value()) }.toAttrs { attr("aria-hidden", "true") }) {
            Text(if (isLit) "▶" else "")
        }
        Span { Text(line.text) }
    }
}

@Composable
private fun BoardRow(stage: SimStage, cell: BoardCell) {
    val value = stage.state.vars[cell.variable]?.shown() ?: "–"
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("justify-content", "space-between")
            property("gap", space3Var.value())
            property("padding", "${space1Var.value()} ${space2Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeMdVar.value())
            typo(Typo.Label)
        }.toAttrs()
    ) {
        Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) { Text(cell.label) }
        Span(Modifier.styleModifier { property("color", colorOnSurfaceVar.value()); property("font-family", fontCodeVar.value()) }.toAttrs()) { Text(value) }
    }
}

@Composable
private fun LogTable(columns: List<String>, rows: List<List<String>>) {
    val grid = Modifier.styleModifier {
        property("display", "grid")
        property("grid-template-columns", "repeat(${columns.size}, minmax(0, 1fr))")
        property("gap", space2Var.value())
        property("padding", "${space1Var.value()} ${space2Var.value()}")
        typo(Typo.Label)
    }
    Div(grid.toAttrs { attr("role", "row") }) {
        columns.forEach { c ->
            Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs { attr("role", "columnheader") }) { Text(c) }
        }
    }
    rows.forEachIndexed { i, r ->
        key(i) {
            Div(grid.toAttrs { attr("role", "row") }) {
                r.forEach { cell -> Span(Modifier.styleModifier { property("color", colorOnSurfaceVar.value()) }.toAttrs { attr("role", "cell") }) { Text(cell) } }
            }
        }
    }
}
