package dev.kotree.site.sims.core

/*
 * ADR 009 section 2: state = fold(initial, history). Pure functions of a [SimSpec] and a list of event ids; no clock,
 * no DOM. Back is `fold(history.dropLast(1))`, Reset is `fold(emptyList())`.
 *
 * Event ids: an action id, "<item>><place>" for a drop, "<toggle>=on" or "<toggle>=off", "<slider>=<number>".
 *
 * Decisions the ADR leaves open (reported in the WP2 hand-off):
 *  - A refused event stays in the history, so Try this can see it (ADR 009 worked example, step 3).
 *  - Settings (toggles, sliders) are hoisted: the LAST setting event of the history sets its variable from the very start,
 *    so changing a setting replays every earlier action with the new value ("change the code, see the change").
 *  - An effect that cannot run (move of an unknown item, push into a full lane, pop of an empty lane) refuses the whole
 *    case: nothing changes except the `said` text, which is the action's `refuse` or [DEFAULT_REFUSE].
 *  - `say` is interpolated after the other effects of its case, so it sees the final variables.
 *  - A drop that runs moves its item into the place it was dropped on before its own effects.
 *  - `pop` removes the item from the sim. A queue shifts the rest toward slot 0; a stack and a row pop the highest slot.
 */

data class SimItem(val id: String, val label: String, val at: String)

/** One frame of a sim. [said] is what the live region reads; [refused] is true when it is a refuse text. */
data class SimState(
    val vars: Map<String, SimValue>,
    val items: List<SimItem>,
    val marks: Map<String, String>,
    val labels: Map<String, String>,
    val lit: List<String>,
    val rows: List<List<String>>,
    val said: String,
    val refused: Boolean,
) {
    fun item(id: String): SimItem? = items.firstOrNull { it.id == id }
    fun markOf(id: String): String = marks[id] ?: MARK_PLAIN
}

object SimCore {
    private val INTERPOLATION = Regex("\\{([a-zA-Z][A-Za-z0-9_.]*)\\}")

    /** Value of each setting after the whole [history], keyed by variable name (last event wins). */
    fun settings(spec: SimSpec, history: List<String>): Map<String, SimValue> {
        val out = LinkedHashMap<String, SimValue>()
        for (e in history) settingOf(spec, e)?.let { (name, value) -> out[name] = value }
        return out
    }

    /** The variable and value an event sets, when it is a toggle or slider event of [spec]. */
    private fun settingOf(spec: SimSpec, event: String): Pair<String, SimValue>? {
        val eq = event.indexOf('=')
        if (eq <= 0) return null
        val id = event.substring(0, eq)
        val raw = event.substring(eq + 1)
        spec.toggles.firstOrNull { it.id == id }?.let { return it.variable to (if (raw == "on") "on" else "off") }
        spec.sliders.firstOrNull { it.id == id }?.let { s -> raw.toDoubleOrNull()?.let { return s.variable to it } }
        return null
    }

    fun initial(spec: SimSpec, history: List<String> = emptyList()): SimState = SimState(
        vars = spec.vars + settings(spec, history),
        items = spec.items.map { SimItem(it.id, it.label, it.at) },
        marks = emptyMap(),
        labels = emptyMap(),
        lit = emptyList(),
        rows = emptyList(),
        said = spec.intro,
        refused = false,
    )

    fun fold(spec: SimSpec, history: List<String>): SimState =
        history.fold(initial(spec, history)) { state, event -> step(spec, state, event) }

    /** One event applied to [state]. Unknown events refuse with [DEFAULT_REFUSE]. */
    fun step(spec: SimSpec, state: SimState, event: String): SimState {
        describeSetting(spec, state, event)?.let { return state.copy(said = it, refused = false) }
        val action = spec.actions.firstOrNull { it.id == event }
        val drop = if (action == null) spec.drops.firstOrNull { it.event == event } else null
        val cases = action?.cases ?: drop?.let { listOf(it.case) } ?: emptyList()
        val refuse = action?.refuse ?: drop?.refuse ?: DEFAULT_REFUSE
        val chosen = cases.firstOrNull { holds(spec, state, it) }
        // A successful drop first moves the dragged item into the place it was dropped on (the worked example relies on it).
        val lead = if (drop != null) listOf<SimEffect>(SimEffect.Move(listOf(drop.item to drop.to))) else emptyList()
        val next = chosen?.let { run(spec, state, lead + it.effects) }
        return next ?: state.copy(said = interpolate(spec, state, refuse), refused = true)
    }

    private fun describeSetting(spec: SimSpec, state: SimState, event: String): String? {
        val (name, value) = settingOf(spec, event) ?: return null
        val id = event.substringBefore('=')
        val label = spec.toggles.firstOrNull { it.id == id }?.label ?: spec.sliders.firstOrNull { it.id == id }?.label ?: name
        return "$label: ${(state.vars[name] ?: value).shown()}."
    }

    // ---- conditions ----

    private fun same(a: SimValue?, b: SimValue): Boolean = a != null && a.shown() == b.shown()

    private fun holds(spec: SimSpec, state: SimState, case: SimCase): Boolean =
        (case.whenHolds == null || all(state, case.whenHolds)) && (case.unless == null || !all(state, case.unless))

    private fun all(state: SimState, c: SimCondition): Boolean =
        c.at.all { (item, place) -> state.item(item)?.at == place } &&
            c.mark.all { (id, mark) -> state.markOf(id) == mark } &&
            c.vars.all { (name, value) -> same(state.vars[name], value) }

    // ---- effects ----

    private class Work(var s: SimState) {
        var fail = false
        var say: String? = null
    }

    private fun run(spec: SimSpec, start: SimState, effects: List<SimEffect>): SimState? {
        val w = Work(start)
        for (e in effects) {
            apply(spec, w, e)
            if (w.fail) return null
        }
        return w.s.copy(said = w.say?.let { interpolate(spec, w.s, it) } ?: "", refused = false)
    }

    private fun apply(spec: SimSpec, w: Work, e: SimEffect) {
        val s = w.s
        when (e) {
            is SimEffect.Say -> w.say = e.text
            is SimEffect.Move -> {
                var items = s.items
                for ((id, place) in e.moves) {
                    if (items.none { it.id == id }) { w.fail = true; return }
                    items = items.map { if (it.id == id) it.copy(at = place) else it }
                }
                w.s = s.copy(items = items)
            }
            is SimEffect.Spawn -> {
                val id = interpolate(spec, s, e.id)
                val item = SimItem(id, interpolate(spec, s, e.label), e.at)
                w.s = s.copy(items = s.items.filter { it.id != id } + item, marks = s.marks - id, labels = s.labels - id)
            }
            is SimEffect.Remove ->
                if (s.item(e.item) == null) w.fail = true else w.s = s.copy(items = s.items.filter { it.id != e.item }, marks = s.marks - e.item)
            is SimEffect.Mark -> w.s = s.copy(marks = e.marks.fold(s.marks) { m, (id, mark) -> if (mark == MARK_PLAIN) m - id else m + (id to mark) })
            is SimEffect.Label -> w.s = s.copy(labels = s.labels + e.labels.map { (id, t) -> id to interpolate(spec, s, t) })
            is SimEffect.Set -> w.s = s.copy(vars = s.vars + e.values)
            is SimEffect.Add -> w.s = s.copy(vars = e.deltas.fold(s.vars) { v, (name, d) -> v + (name to (((v[name] as? Double) ?: 0.0) + d)) })
            is SimEffect.Light -> w.s = s.copy(lit = e.ids)
            is SimEffect.Row -> w.s = s.copy(rows = s.rows + listOf(e.cells.map { interpolate(spec, s, it) }))
            is SimEffect.Push -> for ((lane, item) in e.pushes) { push(spec, w, lane, item); if (w.fail) return }
            is SimEffect.Pop -> pop(spec, w, e.lane)
        }
    }

    private fun slotsOf(s: SimState, lane: String): List<Pair<Int, SimItem>> =
        s.items.mapNotNull { it.slotIn(lane)?.let { n -> n to it } }.sortedBy { it.first }

    private fun SimItem.slotIn(lane: String): Int? =
        if (at.startsWith("$lane.")) at.substring(lane.length + 1).toIntOrNull() else null

    private fun push(spec: SimSpec, w: Work, lane: String, itemId: String) {
        val l = spec.lanes[lane]
        val used = slotsOf(w.s, lane).map { it.first }
        if (l == null || w.s.item(itemId) == null || used.size >= l.slots) { w.fail = true; return }
        val slot = if (l.kind == "row") (0 until l.slots).first { it !in used } else (used.maxOrNull() ?: -1) + 1
        if (slot >= l.slots) { w.fail = true; return }
        w.s = w.s.copy(items = w.s.items.map { if (it.id == itemId) it.copy(at = "$lane.$slot") else it })
    }

    private fun pop(spec: SimSpec, w: Work, lane: String) {
        val l = spec.lanes[lane]
        val used = slotsOf(w.s, lane)
        if (l == null || used.isEmpty()) { w.fail = true; return }
        val gone = if (l.kind == "queue") used.first().second else used.last().second
        var items = w.s.items.filter { it.id != gone.id }
        if (l.kind == "queue") items = items.map { it.slotIn(lane)?.let { n -> it.copy(at = "$lane.${n - 1}") } ?: it }
        w.s = w.s.copy(items = items, marks = w.s.marks - gone.id)
    }

    // ---- text ----

    /** Replaces `{name}` with a variable, a published chart variable, or leaves it as written when unknown. */
    fun interpolate(spec: SimSpec, state: SimState, text: String): String =
        INTERPOLATION.replace(text) { m ->
            val name = m.groupValues[1]
            state.vars[name]?.shown() ?: ChartMath.published(spec, state.vars, name) ?: m.value
        }
}
