package dev.kotree.site.sims.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorFeedbackCorrectVar
import dev.kotree.site.colorFeedbackHintVar
import dev.kotree.site.colorFeedbackWrongVar
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.sims.SimStage
import dev.kotree.site.sims.core.SimItem
import dev.kotree.site.sims.core.SimLaneSpec
import dev.kotree.site.sims.core.SimState
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent

/*
 * ADR 009 section 2, template `lanes`: indexed slots in up to 3 lanes (row, queue, stack). The layout keeps no sim state.
 * Items sit at "<lane>.<i>"; `push` and `pop` are core effects that follow the lane kind. An item named in `drops` is
 * draggable (Pointer Events with capture) and also pickable by keyboard: Enter picks it up, every slot becomes a "Drop"
 * button, Esc cancels. Both fire the same event id "<item>><lane>.<i>". No timers or animation frames: idle cost is zero,
 * and nothing moves by itself, so reduced motion needs no special case.
 */

private const val ATTR_SLOT = "data-slot"
private const val DRAG_SLOP_PX = 4

/** The place id of slot [index] of [lane]. */
fun slotPlace(lane: String, index: Int): String = "$lane.$index"

/** What sits in each slot of [lane], in slot order; null for an empty slot. */
fun slotContents(state: SimState, lane: SimLaneSpec): List<SimItem?> {
    val byPlace = state.items.associateBy { it.at }
    return List(lane.slots) { byPlace[slotPlace(lane.id, it)] }
}

/** The index shown over a slot: from 0 when `index0`, otherwise from 1. */
fun slotNumber(lane: SimLaneSpec, index: Int): Int = if (lane.index0) index else index + 1

/** Text cue that names which end a queue or stack works at, so a reader need not rely on position. Null for a row. */
fun endCue(lane: SimLaneSpec, index: Int, contents: List<SimItem?>): String? {
    val filled = contents.indices.filter { contents[it] != null }
    if (contents[index] == null || filled.isEmpty()) return null
    return when (lane.kind) {
        "queue" -> if (index == filled.first()) "front" else if (index == filled.last()) "back" else null
        "stack" -> if (index == filled.last()) "top" else null
        else -> null
    }
}

@Composable
fun LanesLayout(stage: SimStage) {
    val lanes = remember(stage.spec) { stage.spec.lanes.values.toList() }
    val draggable = remember(stage.spec) { draggableItems(stage.spec) }
    var picked by remember(stage.spec) { mutableStateOf<String?>(null) }
    val dragMoved = remember(stage.spec) { booleanArrayOf(false) }

    fun drop(item: String, place: String) {
        picked = null
        stage.fire(dropEvent(item, place))
    }

    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space3Var.value())
            property("width", "100%")
            property("height", "100%")
            property("padding", space3Var.value())
            property("box-sizing", "border-box")
            property("overflow", "auto")
            typo(Typo.Body)
        }.toAttrs {
            onKeyDown { e -> if ((e.nativeEvent as KeyboardEvent).key == "Escape" && picked != null) { picked = null; e.preventDefault() } }
        }
    ) {
        lanes.forEach { lane ->
            key(lane.id) {
                LaneRow(stage, lane, draggable, picked, dragMoved,
                    onPick = { id -> picked = if (picked == id) null else id },
                    onDrop = ::drop)
            }
        }
        if (picked != null) {
            Div(Modifier.styleModifier { typo(Typo.Label); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs { attr("role", "status") }) {
                Text("Choose a slot to drop in, or press Esc to cancel.")
            }
        }
    }
}

@Composable
private fun LaneRow(
    stage: SimStage, lane: SimLaneSpec, draggable: Set<String>, picked: String?, dragMoved: BooleanArray,
    onPick: (String) -> Unit, onDrop: (String, String) -> Unit,
) {
    val contents = slotContents(stage.state, lane)
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
            property("padding", space3Var.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeXlVar.value())
            property("background-color", colorSurfaceVar.value())
        }.toAttrs { attr("role", "group"); attr("aria-label", "${lane.label}, a ${lane.kind}") }
    ) {
        Span(Modifier.styleModifier { property("color", ink); typo(Typo.Title) }.toAttrs()) { Text("${lane.label} (${lane.kind})") }
        Div(
            Modifier.styleModifier {
                property("display", "grid")
                property("grid-template-columns", "repeat(${lane.slots.coerceAtLeast(1)}, minmax(0, 1fr))")
                property("gap", space1Var.value())
            }.toAttrs()
        ) {
            contents.forEachIndexed { i, item ->
                key(i) { Slot(stage, lane, i, item, endCue(lane, i, contents), draggable, picked, dragMoved, onPick, onDrop) }
            }
        }
    }
}

@Composable
private fun Slot(
    stage: SimStage, lane: SimLaneSpec, index: Int, item: SimItem?, cue: String?, draggable: Set<String>, picked: String?,
    dragMoved: BooleanArray, onPick: (String) -> Unit, onDrop: (String, String) -> Unit,
) {
    val place = slotPlace(lane.id, index)
    val ink = branchInk(stage.branch).value().toString()
    val hinted = stage.hintEvent != null && picked != null && stage.hintEvent == dropEvent(picked, place)
    val canTarget = picked != null && item == null
    val box = Modifier.styleModifier {
        property("display", "flex")
        property("flex-direction", "column")
        property("align-items", "center")
        property("justify-content", "center")
        property("gap", space1Var.value())
        property("min-width", "0")
        property("min-height", "${Tokens.Spacing.minTargetPx * 2}px")
        property("padding", space1Var.value())
        property("border", "${Tokens.Elevation.borderPx}px ${if (item == null) "dashed" else "solid"} ${if (hinted) colorFeedbackHintVar.value().toString() else ink}")
        property("border-radius", shapeMdVar.value())
        property("background-color", if (item == null) colorSurfaceVar.value().toString() else branchTint(stage.branch).value().toString())
        property("color", colorOnSurfaceVar.value())
        typo(Typo.Label)
    }
    val inner: @Composable () -> Unit = {
        Span(Modifier.styleModifier { property("color", colorOnSurfaceMutedVar.value()) }.toAttrs { attr("aria-hidden", "true") }) {
            Text(slotNumber(lane, index).toString())
        }
        if (item != null) ItemChip(stage, item, item.id in draggable, picked == item.id, dragMoved, onPick, onDrop)
        if (cue != null) Span(
            Modifier.styleModifier {
                property("padding", "0 ${space1Var.value()}")
                property("border-radius", shapeFullVar.value())
                property("background-color", ink)
                property("color", colorOnBranchVar.value())
            }.toAttrs()
        ) { Text(cue) }
    }
    if (canTarget) {
        Button(
            attrs = box.styleModifier { property("cursor", "pointer") }.toAttrs {
                attr("type", "button")
                attr(ATTR_SLOT, place)
                attr("aria-label", "Drop in ${lane.label} slot ${slotNumber(lane, index)}")
                onClick { onDrop(picked.orEmpty(), place) }
            }
        ) { inner() }
    } else {
        Div(box.toAttrs { attr(ATTR_SLOT, place); attr("role", "group"); attr("aria-label", "${lane.label} slot ${slotNumber(lane, index)}${if (item == null) ", empty" else ""}") }) { inner() }
    }
}

@Composable
private fun ItemChip(
    stage: SimStage, item: SimItem, canDrag: Boolean, isPicked: Boolean, dragMoved: BooleanArray,
    onPick: (String) -> Unit, onDrop: (String, String) -> Unit,
) {
    val mark = stage.state.markOf(item.id)
    val label = stage.state.labels[item.id] ?: item.label
    val cue = markCue(mark)
    val ink = branchInk(stage.branch).value().toString()
    val borderColor = when (mark) {
        "good" -> colorFeedbackCorrectVar.value().toString()
        "bad" -> colorFeedbackWrongVar.value().toString()
        else -> ink
    }
    val raised = isPicked || mark == "picked"
    val styles = Modifier.styleModifier {
        property("display", "inline-flex")
        property("align-items", "center")
        property("gap", space1Var.value())
        property("min-height", "${Tokens.Spacing.minTargetPx}px")
        property("padding", "0 ${space2Var.value()}")
        property("border", "${(if (raised) 2 else 1) * Tokens.Elevation.borderPx}px ${if (mark == "changed" || mark == "ghost") "dashed" else "solid"} $borderColor")
        property("border-radius", shapeMdVar.value())
        property("background-color", colorSurfaceVar.value())
        property("color", if (mark == "ghost") colorOnSurfaceMutedVar.value().toString() else colorOnSurfaceVar.value().toString())
        if (mark == "ghost") property("opacity", Tokens.Opacity.ghost)
        property("touch-action", if (canDrag) "none" else "auto")
        property("user-select", "none")
        if (canDrag) property("cursor", "grab")
        typo(Typo.Label)
    }
    val content: @Composable () -> Unit = { Text(if (cue != null) "$label ($cue)" else label) }
    if (!canDrag) {
        Span(styles.toAttrs()) { content() }
        return
    }
    Button(
        attrs = styles.toAttrs {
            attr("type", "button")
            attr("aria-pressed", isPicked.toString())
            attr("aria-label", "$label${if (cue != null) ", $cue" else ""}. Press Enter to pick up, then choose a slot.")
            ref { el -> wireDrag(el, item.id, dragMoved, onDrop); onDispose { } }
            onClick { if (dragMoved[0]) dragMoved[0] = false else onPick(item.id) }
        }
    ) { content() }
}

// ---- browser plumbing (work happens only inside pointer events) ----

private fun slotBoxes(from: Element): List<Box> {
    val root = from.closest("[role=group][aria-label*=', a ']")?.parentElement ?: return emptyList()
    val nodes = root.asDynamic().querySelectorAll("[$ATTR_SLOT]")
    val out = ArrayList<Box>()
    for (i in 0 until (nodes.length as Int)) {
        val z = nodes[i].unsafeCast<HTMLElement>()
        val r = z.getBoundingClientRect()
        out.add(Box(z.getAttribute(ATTR_SLOT).orEmpty(), r.left, r.top, r.right, r.bottom))
    }
    return out
}

private fun wireDrag(el: Element, itemId: String, moved: BooleanArray, onDrop: (String, String) -> Unit) {
    val chip = el.unsafeCast<HTMLElement>()
    var startX = 0.0
    var startY = 0.0
    var active = false
    chip.addEventListener("pointerdown", { e ->
        val p = e.asDynamic()
        if (p.button != 0 && p.pointerType == "mouse") return@addEventListener
        startX = p.clientX.unsafeCast<Double>(); startY = p.clientY.unsafeCast<Double>()
        active = true
        moved[0] = false
        chip.asDynamic().setPointerCapture(p.pointerId)
    })
    chip.addEventListener("pointermove", { e ->
        if (!active) return@addEventListener
        val p = e.asDynamic()
        val dx = p.clientX.unsafeCast<Double>() - startX
        val dy = p.clientY.unsafeCast<Double>() - startY
        if (kotlin.math.abs(dx) > DRAG_SLOP_PX || kotlin.math.abs(dy) > DRAG_SLOP_PX) moved[0] = true
        if (moved[0]) chip.style.transform = "translate(${dx}px, ${dy}px)"
    })
    val finish: (org.w3c.dom.events.Event, Boolean) -> Unit = { e, commit ->
        if (active) {
            active = false
            val p = e.asDynamic()
            chip.style.transform = ""
            if (commit && moved[0]) {
                val target = boxAt(slotBoxes(chip), p.clientX.unsafeCast<Double>(), p.clientY.unsafeCast<Double>())
                if (target != null) onDrop(itemId, target)
            }
        }
    }
    chip.addEventListener("pointerup", { e -> finish(e, true) })
    chip.addEventListener("pointercancel", { e -> finish(e, false); moved[0] = false })
}
