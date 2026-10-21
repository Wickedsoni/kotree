package dev.kotree.site.sims

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorFeedbackHintVar
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.sims.core.SimCore
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.sims.core.SimTryStep
import dev.kotree.site.sims.core.TextDescription
import dev.kotree.site.sims.core.TryProgress
import dev.kotree.site.sims.core.TryProgressCalc
import dev.kotree.site.sims.core.shown
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.ui.core.visuallyHidden
import dev.kotree.site.ui.lesson.SimControls
import dev.kotree.site.ui.lesson.SimFrame
import dev.kotree.site.ui.lesson.Typo
import dev.kotree.site.ui.lesson.typo
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.max
import org.jetbrains.compose.web.attributes.min
import org.jetbrains.compose.web.attributes.step
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ol
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent

/*
 * ADR 009 sections 3 and 4: the shared widget shell for every template sim. It owns the history (the only state),
 * playback, the Try this list, the live region, the text description and reduced motion; a template layout only draws the
 * stage. Zero idle CPU: the one setTimeout exists only while Play is on, and the observers are passive.
 */

private const val NOW_BADGE = "Now"
private const val DONE_SR = "done"
private const val OPTIONAL_TAG = "optional"
private const val CHECK_MARK = "✓"
private const val TRY_TITLE = "Try this"
private const val SLOW_FACTOR = 2

/** The template sim of one ```sim block, with its stage from [layout] (a fallback list when there is none). */
@Composable
fun TemplateSimHost(spec: SimSpec, branch: String, layout: SimLayout?) {
    var history by remember(spec) { mutableStateOf(emptyList<String>()) }
    var scriptIdx by remember(spec) { mutableStateOf(0) }
    var playing by remember(spec) { mutableStateOf(false) }
    var slow by remember(spec) { mutableStateOf(false) }
    var describing by remember(spec) { mutableStateOf(false) }
    var reduced by remember(spec) { mutableStateOf(false) }
    var visible by remember(spec) { mutableStateOf(true) }
    var frameEl by remember(spec) { mutableStateOf<Element?>(null) }
    var stageEl by remember(spec) { mutableStateOf<Element?>(null) }
    val owner = remember(spec) { Any() }
    val fresh = remember(spec) { booleanArrayOf(false) }

    val state = remember(spec, history) { SimCore.fold(spec, history) }
    val progress = remember(spec, history) { TryProgressCalc.of(spec.tryList, history) }
    val description = remember(spec) { TextDescription.lines(spec) }

    fun fire(event: String) {
        val h = history
        val isSetting = event.contains('=')
        history = if (isSetting && h.lastOrNull()?.substringBefore('=') == event.substringBefore('=')) h.dropLast(1) + event else h + event
    }

    fun stepScript() {
        if (scriptIdx >= spec.script.size) return
        fire(spec.script[scriptIdx])
        scriptIdx += 1
    }

    fun pause() { playing = false; PagePlayback.release(owner) }

    fun reset() { pause(); history = emptyList(); scriptIdx = 0 }

    fun back() {
        val h = history
        if (h.isEmpty()) return
        if (scriptIdx > 0 && h.last() == spec.script[scriptIdx - 1]) scriptIdx -= 1
        history = h.dropLast(1)
    }

    fun play() {
        if (scriptIdx >= spec.script.size) { history = emptyList(); scriptIdx = 0 }
        PagePlayback.claim(owner) { playing = false }
        fresh[0] = true
        playing = true
    }

    // Reduced motion, hidden tab and on-screen share: listeners that exist for the life of the sim and cost nothing idle.
    DisposableEffect(spec) {
        val stopMotion = watchReducedMotion { reduced = it }
        val stopTab = watchTabHidden { pause() }
        onDispose { stopMotion(); stopTab(); PagePlayback.release(owner) }
    }
    DisposableEffect(frameEl) {
        val el = frameEl
        val stop = if (el != null) watchVisible(el, Tokens.Motion.simVisibleShare) { visible = it; if (!it) pause() } else ({})
        onDispose { stop() }
    }

    // The single timer: one setTimeout while playing, cleared on every change of its keys and on dispose.
    DisposableEffect(playing, scriptIdx, slow, visible) {
        var handle: Int? = null
        if (playing && visible && scriptIdx < spec.script.size) {
            val delay = if (fresh[0]) 0 else Tokens.Motion.simStepMs * (if (slow) SLOW_FACTOR else 1)
            handle = window.setTimeout({
                fresh[0] = false
                stepScript()
                if (scriptIdx + 1 >= spec.script.size) pause()
            }, delay)
        }
        onDispose { handle?.let { window.clearTimeout(it) } }
    }

    // Reduced motion: each new state fades in over simFadeMs. Web Animations, because Base.kt zeroes CSS transitions.
    LaunchedEffect(history, reduced) {
        val el = stageEl
        if (reduced && el != null && history.isNotEmpty()) fadeIn(el, Tokens.Motion.simFadeMs)
    }

    val controls = SimControls(
        playing = playing, slow = slow, describing = describing,
        canBack = history.isNotEmpty(), canStep = scriptIdx < spec.script.size,
        onPlay = ::play, onPause = ::pause, onBack = ::back, onStep = { pause(); stepScript() },
        onReset = ::reset, onSpeed = { slow = !slow }, onDescribe = { describing = !describing },
    )
    val stage = SimStage(spec, state, branch, reduced, progress, progress.nextEvent, ::fire)

    SimFrame(
        title = spec.title,
        branch = branch,
        controls = controls,
        modifier = Modifier.attrsModifier {
            ref { el -> frameEl = el; onDispose { } }
            onKeyDown { e ->
                if (handleKey(e.nativeEvent as KeyboardEvent, ::play, ::pause, playing, ::stepScript, ::back, ::reset) { slow = !slow }) e.preventDefault()
            }
        },
        actions = if (spec.actions.isEmpty() && spec.toggles.isEmpty() && spec.sliders.isEmpty()) null else ({
            SimActions(spec, state.vars, branch, progress.nextEvent, ::fire)
        }),
        below = {
            SimReadouts(spec, state.vars)
            spec.caption?.let { Caption(it) }
            TryList(spec.tryList, progress, branch)
            if (describing) Description(description)
            // Live region (ADR 009 section 4): always mounted, empty at first, text updated in place.
            Div(
                Modifier.attrsModifier { attr("aria-live", "polite"); attr("aria-atomic", "true"); attr("role", "status") }
                    .styleModifier { visuallyHidden() }.toAttrs()
            ) { Text(if (history.isEmpty()) "" else state.said) }
        },
    ) {
        Div(
            Modifier.styleModifier {
                property("width", "100%")
                property("height", "100%")
                property("display", "flex")
                property("align-items", "center")
                property("justify-content", "center")
            }.toAttrs { ref { el -> stageEl = el; onDispose { } } }
        ) {
            if (layout != null) layout(stage) else FallbackStage(stage)
        }
    }
}

/** Keys inside the frame only (ADR 009 section 4). Returns true when the key was used. */
private fun handleKey(
    e: KeyboardEvent, play: () -> Unit, pause: () -> Unit, playing: Boolean,
    step: () -> Unit, back: () -> Unit, reset: () -> Unit, speed: () -> Unit,
): Boolean {
    if (e.ctrlKey || e.metaKey || e.altKey) return false
    val tag = (e.target as? HTMLElement)?.tagName.orEmpty()
    return when (e.key) {
        " " -> if (tag == "BUTTON" || tag == "INPUT" || tag == "SUMMARY") false else { if (playing) pause() else play(); true }
        "." -> { pause(); step(); true }
        "," -> { back(); true }
        "r", "R" -> if (tag == "INPUT") false else { reset(); true }
        "s", "S" -> if (tag == "INPUT") false else { speed(); true }
        else -> false
    }
}

// ---- stage fallback ----

/** Preview only, for a template whose layout is not built yet: the state as plain text, so every control still works. */
@Composable
private fun FallbackStage(stage: SimStage) {
    Div(
        Modifier.styleModifier {
            property("padding", space4Var.value())
            property("max-width", "100%")
            typo(Typo.Body)
        }.toAttrs()
    ) {
        P(Modifier.styleModifier { property("margin", "0 0 ${space2Var.value()}"); typo(Typo.Title) }.toAttrs()) {
            Text("The ${stage.spec.template} layout is not built yet. Preview only.")
        }
        stage.state.items.forEach { item ->
            P(Modifier.styleModifier { property("margin", "0") }.toAttrs()) {
                Text("${stage.state.labels[item.id] ?: item.label} at ${item.at} (${stage.state.markOf(item.id)})")
            }
        }
    }
}

// ---- controls under the stage ----

@Composable
private fun SimActions(spec: SimSpec, vars: Map<String, Any>, branch: String, hint: String?, fire: (String) -> Unit) {
    spec.actions.forEach { a ->
        HintWrap(hint == a.id) { ChipButton(a.label, branch, filled = false, role = null, checked = null) { fire(a.id) } }
    }
    spec.toggles.forEach { t ->
        val on = vars[t.variable]?.toString() == "on"
        val next = if (on) "off" else "on"
        HintWrap(hint == "${t.id}=$next") {
            ChipButton("${t.label}: ${if (on) "On" else "Off"}", branch, filled = on, role = "switch", checked = on) { fire("${t.id}=$next") }
        }
    }
    spec.sliders.forEach { s ->
        val value = (vars[s.variable] as? Double) ?: s.min
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("gap", space2Var.value())
                typo(Typo.Label)
            }.toAttrs()
        ) {
            org.jetbrains.compose.web.dom.Label(forId = "sim-${spec.id}-${s.id}") { Text("${s.label}: ${value.shown()}") }
            Input(InputType.Range) {
                id("sim-${spec.id}-${s.id}")
                min(s.min.toString()); max(s.max.toString()); step(s.step)
                value(value)
                style {
                    property("accent-color", branchInk(branch).value().toString())
                    property("min-height", "${Tokens.Spacing.minTargetPx}px")
                }
                onInput { fire("${s.id}=${it.value.toString().toDoubleOrNull()?.shown() ?: s.min.shown()}") }
            }
        }
    }
}

/** The control that fires the next expected Try this event gets a dashed outline: a shape cue, not colour alone. */
@Composable
private fun HintWrap(hinted: Boolean, content: @Composable () -> Unit) {
    Div(
        Modifier.styleModifier {
            property("padding", "${Tokens.Elevation.borderPx}px")
            property("border-radius", shapeFullVar.value())
            property("border", "${2 * Tokens.Elevation.borderPx}px ${if (hinted) "dashed" else "solid"} ${if (hinted) colorFeedbackHintVar.value().toString() else "transparent"}")
        }.toAttrs()
    ) { content() }
}

/** A real button (Tab, Enter, Space, focus ring from Base.kt) of at least 44 px, for actions and switches. */
@Composable
private fun ChipButton(label: String, branch: String, filled: Boolean, role: String?, checked: Boolean?, onClick: () -> Unit) {
    Button(
        attrs = Modifier.styleModifier {
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("min-width", "${Tokens.Spacing.minTargetPx}px")
            property("padding", "0 ${space4Var.value()}")
            property("border", "${Tokens.Elevation.borderPx}px solid ${branchInk(branch).value()}")
            property("border-radius", shapeFullVar.value())
            property("background-color", if (filled) branchInk(branch).value().toString() else branchTint(branch).value().toString())
            property("color", if (filled) colorOnBranchVar.value().toString() else branchInk(branch).value().toString())
            property("cursor", "pointer")
            typo(Typo.Label)
        }.toAttrs {
            attr("type", "button")
            if (role != null) attr("role", role)
            if (checked != null) attr("aria-checked", checked.toString())
            onClick { onClick() }
        }
    ) { Text(label) }
}

@Composable
private fun SimReadouts(spec: SimSpec, vars: Map<String, Any>) {
    if (spec.show.isEmpty()) return
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("gap", space4Var.value())
            property("margin-bottom", space3Var.value())
            typo(Typo.Label)
        }.toAttrs()
    ) {
        spec.show.forEach { Span { Text("${it.label}: ${vars[it.variable]?.shown().orEmpty()}") } }
    }
}

@Composable
private fun Caption(text: String) {
    P(Modifier.styleModifier {
        property("margin", "0 0 ${space3Var.value()}")
        property("color", colorOnSurfaceMutedVar.value())
        typo(Typo.Label)
    }.toAttrs()) { Text(text) }
}

@Composable
private fun Description(lines: List<String>) {
    Div(
        Modifier.styleModifier {
            property("margin-top", space3Var.value())
            property("padding", space3Var.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", shapeMdVar.value())
        }.toAttrs()
    ) {
        lines.forEach { P(Modifier.styleModifier { property("margin", "0 0 ${space1Var.value()}") }.toAttrs()) { Text(it) } }
    }
}

// ---- Try this ----

/**
 * The Try this list (ADR 009 section 3). The current step is `aria-current="step"` with a left bar, bold text and a "Now"
 * badge; a done step has a check mark and visually hidden "done". Step text is trusted build output, set once with innerHTML.
 */
@Composable
fun TryList(steps: List<SimTryStep>, progress: TryProgress?, branch: String) {
    P(Modifier.styleModifier { property("margin", "0 0 ${space2Var.value()}"); typo(Typo.Title) }.toAttrs()) { Text(TRY_TITLE) }
    Ol(
        attrs = Modifier.styleModifier {
            property("margin", "0")
            property("padding", "0")
            property("list-style", "none")
            property("display", "grid")
            property("gap", space2Var.value())
        }.toAttrs()
    ) {
        steps.forEachIndexed { i, step ->
            key(i, step.html) {
                val isDone = progress?.done?.getOrNull(i) == true
                val isCurrent = progress != null && progress.current == i
                Li(
                    attrs = Modifier.styleModifier {
                        property("padding", "${space1Var.value()} ${space3Var.value()}")
                        property("border-left", "${Tokens.Motion.simStepBarPx}px solid ${if (isCurrent) branchInk(branch).value().toString() else "transparent"}")
                        property("color", colorOnSurfaceVar.value())
                        typo(Typo.Body)
                        if (isCurrent) property("font-weight", typeTitleWeightVar.value())
                    }.toAttrs { if (isCurrent) attr("aria-current", "step") }
                ) {
                    Span(Modifier.styleModifier { property("margin-right", space2Var.value()) }.toAttrs { attr("aria-hidden", "true") }) {
                        Text(if (isDone) CHECK_MARK else "${i + 1}.")
                    }
                    if (isDone) Span(Modifier.styleModifier { visuallyHidden() }.toAttrs()) { Text(DONE_SR) }
                    Span(attrs = {
                        ref { el -> el.innerHTML = step.html; onDispose { } }
                    })
                    if (step.optional) Span(Modifier.styleModifier { property("margin-left", space2Var.value()); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                        Text("($OPTIONAL_TAG)")
                    }
                    if (isCurrent) Span(
                        Modifier.styleModifier {
                            property("margin-left", space2Var.value())
                            property("padding", "0 ${space2Var.value()}")
                            property("border-radius", shapeFullVar.value())
                            property("background-color", branchTint(branch).value().toString())
                            property("color", branchInk(branch).value().toString())
                            typo(Typo.Label)
                        }.toAttrs()
                    ) { Text(NOW_BADGE) }
                }
            }
        }
    }
}

/** A template sim whose layout is not built yet, on a live page: the steps, read-only, so the lesson still teaches. */
@Composable
fun ReadOnlyTry(spec: SimSpec, branch: String) {
    Div(Modifier.styleModifier { property("margin-top", space2Var.value()) }.toAttrs()) {
        TryList(spec.tryList, null, branch)
    }
}
