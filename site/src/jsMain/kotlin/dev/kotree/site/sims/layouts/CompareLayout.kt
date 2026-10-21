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
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.sims.SimStage
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/*
 * ADR 009 section 2, template `compare`: two ways of doing the same thing. Two columns, each a titled stack of boxes.
 * A box is the place `<side>.<box>`; items spawned or moved there show as chips inside it; a lit box (`light`) gets a
 * thick outline, a tint and the words "(now)" with aria-current="step". Nothing here is interactive (the host owns the
 * controls), nothing runs while idle, and motion is left to the host's fade.
 */

/** A box of a side; [place] is the id effects use. */
class CompareBox(val place: String, val label: String)

/** A side of a `compare` spec. */
class CompareSide(val id: String, val label: String, val boxes: List<CompareBox>)

private fun items(d: dynamic): List<dynamic> =
    if (d == null || d == undefined) emptyList() else d.unsafeCast<Array<dynamic>>().toList()

/** The two sides of a `compare` spec, read from its raw JSON. */
fun compareSidesOf(spec: SimSpec): List<CompareSide> = items(spec.raw.sides).map { s ->
    val id = s.id.toString()
    CompareSide(id, s.label.toString(), items(s.boxes).map { CompareBox("$id.${it.id}", it.label.toString()) })
}

@Composable
fun CompareLayout(stage: SimStage) {
    val sides = remember(stage.spec) { compareSidesOf(stage.spec) }
    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("grid-template-columns", "repeat(${sides.size.coerceAtLeast(1)}, minmax(0, 1fr))")
            property("gap", space3Var.value())
            property("width", "100%")
            property("height", "100%")
            property("padding", space3Var.value())
            property("box-sizing", "border-box")
            typo(Typo.Body)
        }.toAttrs()
    ) {
        sides.forEach { side -> key(side.id) { SideColumn(stage, side) } }
    }
}

@Composable
private fun SideColumn(stage: SimStage, side: CompareSide) {
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
            property("min-width", "0")
            property("min-height", "0")
            property("padding", space3Var.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeXlVar.value())
            property("background-color", colorSurfaceVar.value())
        }.toAttrs { attr("role", "group"); attr("aria-label", side.label) }
    ) {
        Span(Modifier.styleModifier { property("color", ink); typo(Typo.Title) }.toAttrs()) { Text(side.label) }
        side.boxes.forEach { box -> key(box.place) { BoxCard(stage, box) } }
    }
}

@Composable
private fun BoxCard(stage: SimStage, box: CompareBox) {
    val state = stage.state
    val ink = branchInk(stage.branch).value().toString()
    val lit = box.place in state.lit
    val mark = state.markOf(box.place)
    val cue = markCue(mark)
    val label = state.labels[box.place] ?: box.label
    val width = (if (lit) 2 else 1) * Tokens.Elevation.borderPx
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space1Var.value())
            property("padding", space2Var.value())
            property("border", "${width}px ${if (mark == "changed" || mark == "ghost") "dashed" else "solid"} ${if (lit) ink else colorOutlineVar.value().toString()}")
            property("border-radius", shapeMdVar.value())
            property("background-color", if (lit) branchTint(stage.branch).value().toString() else colorSurfaceVar.value().toString())
            if (mark == "ghost") property("opacity", Tokens.Opacity.ghost)
            property("color", colorOnSurfaceVar.value())
            typo(Typo.Label)
        }.toAttrs {
            attr("role", "group")
            attr("aria-label", label)
            if (lit) attr("aria-current", "step")
        }
    ) {
        Span {
            Text(if (lit) "$label (now)" else label)
            if (cue != null) Span(
                Modifier.styleModifier {
                    property("margin-left", space2Var.value())
                    property("padding", "0 ${space1Var.value()}")
                    property("border-radius", shapeFullVar.value())
                    property("background-color", ink)
                    property("color", colorOnBranchVar.value())
                }.toAttrs()
            ) { Text(cue) }
        }
        state.items.filter { it.at == box.place }.forEach { item ->
            key(item.id) {
                Span(
                    Modifier.styleModifier {
                        property("align-self", "flex-start")
                        property("padding", "0 ${space2Var.value()}")
                        property("border", "${Tokens.Elevation.borderPx}px solid $ink")
                        property("border-radius", shapeMdVar.value())
                        property("color", colorOnSurfaceMutedVar.value())
                    }.toAttrs()
                ) { Text(state.labels[item.id] ?: item.label) }
            }
        }
    }
}
