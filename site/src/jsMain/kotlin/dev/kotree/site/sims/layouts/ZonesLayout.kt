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
import dev.kotree.site.space4Var
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
 * ADR 009 section 2, template `zones`: things placed in containers. Draws the zones as columns and the items as chips in
 * the zone they are at. An item named in `drops` is draggable (Pointer Events with setPointerCapture); the keyboard path is
 * the same chip as a button: Enter picks it up, then a "Drop in <place>" button per place, Esc cancels. Both fire the same
 * event id "<item>><place>". The layout keeps no sim state; `picked` is only the keyboard's transient selection.
 */

/** A zone as the layout draws it. */
class ZoneDef(val id: String, val label: String)

private const val ATTR_ZONE = "data-zone"
private const val DRAG_SLOP_PX = 4

private val moveSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}

/** The zones of a `zones` spec, read from its raw JSON (2 to 4). */
fun zonesOf(spec: SimSpec): List<ZoneDef> {
    val raw = spec.raw.zones ?: return emptyList()
    return raw.unsafeCast<Array<dynamic>>().map { ZoneDef(it.id.toString(), it.label.toString()) }
}

/** Ids of the items the learner may drag: those named in `drops`. */
fun draggableItems(spec: SimSpec): Set<String> = spec.drops.map { it.item }.toSet()

/** The event a drop fires, identical for mouse and keyboard. */
fun dropEvent(item: String, zone: String): String = "$item>$zone"

/** Axis-aligned box in client pixels, for the hit test. */
class Box(val id: String, val left: Double, val top: Double, val right: Double, val bottom: Double)

/** The id of the box that holds the point, or null. The first box in order wins on a shared edge. */
fun boxAt(boxes: List<Box>, x: Double, y: Double): String? =
    boxes.firstOrNull { x >= it.left && x <= it.right && y >= it.top && y <= it.bottom }?.id

/** Short text cue of a mark; colour is never the only cue. Null for `plain`. */
fun markCue(mark: String): String? = when (mark) {
    "changed" -> "changed"
    "good" -> "✓ good"
    "bad" -> "✗ wrong"
    "ghost" -> "not there yet"
    "picked" -> "picked"
    else -> null
}

@Composable
fun ZonesLayout(stage: SimStage) {
    val zones = remember(stage.spec) { zonesOf(stage.spec) }
    val draggable = remember(stage.spec) { draggableItems(stage.spec) }
    var picked by remember(stage.spec) { mutableStateOf<String?>(null) }
    val dragMoved = remember(stage.spec) { booleanArrayOf(false) }

    fun drop(item: String, zone: String) {
        picked = null
        stage.fire(dropEvent(item, zone))
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
            typo(Typo.Body)
        }.toAttrs {
            onKeyDown { e -> if ((e.nativeEvent as KeyboardEvent).key == "Escape" && picked != null) { picked = null; e.preventDefault() } }
        }
    ) {
        Div(
            Modifier.styleModifier {
                property("display", "grid")
                property("grid-template-columns", "repeat(${zones.size.coerceAtLeast(1)}, minmax(0, 1fr))")
                property("gap", space3Var.value())
                property("flex", "1")
                property("min-height", "0")
            }.toAttrs()
        ) {
            zones.forEach { zone ->
                ZoneBox(stage, zone, draggable, picked, dragMoved,
                    onPick = { id -> picked = if (picked == id) null else id },
                    onDrop = ::drop)
            }
        }
        val current = picked
        if (current != null) PickBar(stage, current, zones, ::drop) { picked = null }
    }
}

@Composable
private fun ZoneBox(
    stage: SimStage, zone: ZoneDef, draggable: Set<String>, picked: String?, dragMoved: BooleanArray,
    onPick: (String) -> Unit, onDrop: (String, String) -> Unit,
) {
    val items = stage.state.items.filter { it.at == zone.id }
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-direction", "column")
            property("gap", space2Var.value())
            property("padding", space3Var.value())
            property("min-width", "0")
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeXlVar.value())
            property("background-color", colorSurfaceVar.value())
        }.toAttrs {
            attr(ATTR_ZONE, zone.id)
            attr("role", "group")
            attr("aria-label", zone.label)
        }
    ) {
        Span(Modifier.styleModifier { property("color", ink); typo(Typo.Title) }.toAttrs()) { Text(zone.label) }
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
            items.forEach { item ->
                key(item.id) { ItemChip(stage, item, item.id in draggable, picked == item.id, dragMoved, onPick, onDrop) }
            }
        }
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
    val ghost = mark == "ghost"
    val borderStyle = if (mark == "changed" || ghost) "dashed" else "solid"
    val borderColor = when {
        mark == "good" -> colorFeedbackCorrectVar.value().toString()
        mark == "bad" -> colorFeedbackWrongVar.value().toString()
        stage.hintEvent != null && canDrag && stage.hintEvent.startsWith("${item.id}>") -> colorFeedbackHintVar.value().toString()
        else -> ink
    }
    val raised = isPicked || mark == "picked"
    val styles = Modifier.styleModifier {
        property("display", "inline-flex")
        property("align-items", "center")
        property("gap", space2Var.value())
        property("min-height", "${Tokens.Spacing.minTargetPx}px")
        property("min-width", "${Tokens.Spacing.minTargetPx}px")
        property("padding", "0 ${space3Var.value()}")
        property("border", "${(if (raised) 2 else 1) * Tokens.Elevation.borderPx}px $borderStyle $borderColor")
        property("border-radius", shapeMdVar.value())
        property("background-color", if (raised) branchTint(stage.branch).value().toString() else colorSurfaceVar.value().toString())
        property("color", if (ghost) colorOnSurfaceMutedVar.value().toString() else colorOnSurfaceVar.value().toString())
        property("touch-action", if (canDrag) "none" else "auto")
        property("user-select", "none")
        if (canDrag) property("cursor", "grab")
        typo(Typo.Label)
    }
    val content: @Composable () -> Unit = {
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
    val enter = enterEffect(stage.reducedMotion)
    if (!canDrag) {
        Span(styles.toAttrs { ref { el -> enter(el); onDispose { } } }) { content() }
        return
    }
    Button(
        attrs = styles.toAttrs {
            attr("type", "button")
            attr("aria-pressed", isPicked.toString())
            attr("aria-label", "$label${if (cue != null) ", $cue" else ""}. Press Enter to pick up, then choose a place.")
            ref { el -> enter(el); wireDrag(el, item.id, dragMoved, onDrop); onDispose { } }
            onClick { if (dragMoved[0]) dragMoved[0] = false else onPick(item.id) }
        }
    ) { content() }
}

/** The keyboard path: a "Drop in <place>" button per place for the picked item, plus Cancel. */
@Composable
private fun PickBar(stage: SimStage, itemId: String, zones: List<ZoneDef>, onDrop: (String, String) -> Unit, onCancel: () -> Unit) {
    val item = stage.state.item(itemId) ?: return
    val ink = branchInk(stage.branch).value().toString()
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("align-items", "center")
            property("gap", space2Var.value())
        }.toAttrs { attr("role", "group"); attr("aria-label", "Drop ${stage.state.labels[itemId] ?: item.label}") }
    ) {
        zones.filter { it.id != item.at }.forEach { zone ->
            PickButton("Drop in ${zone.label}", ink, stage.hintEvent == dropEvent(itemId, zone.id), autoFocus = false) { onDrop(itemId, zone.id) }
        }
        PickButton("Cancel", ink, false, autoFocus = false, onClick = onCancel)
    }
}

@Composable
private fun PickButton(text: String, ink: String, hinted: Boolean, autoFocus: Boolean, onClick: () -> Unit) {
    Button(
        attrs = Modifier.styleModifier {
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("padding", "0 ${space4Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px ${if (hinted) "dashed" else "solid"} ${if (hinted) colorFeedbackHintVar.value().toString() else ink}")
            property("border-radius", shapeFullVar.value())
            property("background-color", colorSurfaceVar.value())
            property("color", ink)
            property("cursor", "pointer")
            typo(Typo.Label)
        }.toAttrs {
            attr("type", "button")
            if (autoFocus) attr("autofocus", "")
            onClick { onClick() }
        }
    ) { Text(text) }
}

// ---- browser plumbing (no timers, no animation frames: work happens only inside pointer events) ----

/**
 * A chip's entry motion: a spring move on `transform` and a fade on `opacity` when it appears in a new place. Under reduced
 * motion the host fades the whole stage instead, so the chip does nothing here. Runs once per mount, so it costs nothing idle.
 */
private fun enterEffect(reduced: Boolean): (Element) -> Unit = { el ->
    if (!reduced) {
        val (seconds, easing) = moveSpring
        val keyframes = js("([{ transform: 'scale(0.9)', opacity: 0 }, { transform: 'none', opacity: 1 }])")
        val options = js("({})")
        options.duration = seconds * 1000.0
        options.easing = easing
        el.asDynamic().animate(keyframes, options)
    }
}

private fun zoneBoxes(from: Element): List<Box> {
    val grid = from.closest("[$ATTR_ZONE]")?.parentElement ?: return emptyList()
    val nodes = grid.asDynamic().querySelectorAll("[$ATTR_ZONE]")
    val out = ArrayList<Box>()
    val count = (nodes.length as Int)
    for (i in 0 until count) {
        val z = nodes[i].unsafeCast<HTMLElement>()
        val r = z.getBoundingClientRect()
        out.add(Box(z.getAttribute(ATTR_ZONE).orEmpty(), r.left, r.top, r.right, r.bottom))
    }
    return out
}

/** Pointer drag with capture: the chip follows the pointer via transform; on release the zone under the pointer gets the drop. */
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
                val target = boxAt(zoneBoxes(chip), p.clientX.unsafeCast<Double>(), p.clientY.unsafeCast<Double>())
                if (target != null) onDrop(itemId, target)
            }
        }
    }
    chip.addEventListener("pointerup", { e -> finish(e, true) })
    chip.addEventListener("pointercancel", { e -> finish(e, false); moved[0] = false })
}
