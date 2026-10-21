package dev.kotree.site.sims

import androidx.compose.runtime.Composable
import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.sims.core.SimState
import dev.kotree.site.sims.core.TryProgress

/**
 * What a template layout receives (ADR 009 section 2): the spec, the current state, and `fire` to send an event (a drag
 * drop, a chip press). A layout draws; it never keeps its own state, so Back, Reset and Try this stay correct.
 *
 * @property reducedMotion true under `prefers-reduced-motion`: a layout then uses no transitions (the host adds a fade).
 * @property hintEvent the event that moves the current Try this step forward; a layout may mark its control (dashed outline).
 */
class SimStage(
    val spec: SimSpec,
    val state: SimState,
    val branch: String,
    val reducedMotion: Boolean,
    val progress: TryProgress,
    val hintEvent: String?,
    val fire: (String) -> Unit,
)

typealias SimLayout = @Composable (SimStage) -> Unit

/**
 * Template layouts by template name. WP3 to WP6 of ADR 009 add `zones`, `stepper`, `lanes`, `flow`, `states`, `chart`
 * and `compare` here, one file each under `sims/layouts/`. Empty in WP2: a template without a layout is shown read-only
 * on a live page and as a labelled frame in a preview.
 */
val templateLayouts: Map<String, SimLayout> = mapOf(
    "zones" to { s -> dev.kotree.site.sims.layouts.ZonesLayout(s) },
    "stepper" to { s -> dev.kotree.site.sims.layouts.StepperLayout(s) },
    "lanes" to { s -> dev.kotree.site.sims.layouts.LanesLayout(s) },
    "flow" to { s -> dev.kotree.site.sims.layouts.FlowLayout(s) },
    "states" to { s -> dev.kotree.site.sims.layouts.StatesLayout(s) },
)
