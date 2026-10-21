package dev.kotree.site.sims.core

/*
 * ADR 009 section 2: the sim data model, read once from the validated JSON that the build writes (SimSpec.kt, rules SM2
 * to SM10). The build already checked every reference, so this reader is lenient about shape and strict about nothing it
 * cannot use. Pure Kotlin after [readSimSpec]: no window, no document, so it runs on Node in the unit tests.
 */

/** A variable value: a [String] or a [Double]. YAML `on` and `off` arrive as booleans and become "on" and "off". */
typealias SimValue = Any

/** Marks an item or place can carry (ADR 009 section 2). */
const val MARK_PLAIN = "plain"

/** Text said when no case of an action runs and the action has no `refuse` (ADR 009 section 2). */
const val DEFAULT_REFUSE = "Nothing changes."

/** Value text of a variable, as shown and as interpolated: whole numbers without a decimal point. */
fun SimValue.shown(): String = when (this) {
    is Double -> if (this == kotlin.math.floor(this) && kotlin.math.abs(this) < 1e15) this.toLong().toString() else this.toString()
    else -> this.toString()
}

class SimItemSpec(val id: String, val label: String, val at: String)
class SimLaneSpec(val id: String, val label: String, val kind: String, val slots: Int, val index0: Boolean)

/** A curve function, drawn from the closed set of ADR 009 section 2 (no expression evaluation). */
sealed interface CurveFn {
    class Poly(val c0: Double, val c1: Double, val c2: Double) : CurveFn
    object Log2 : CurveFn
    object NLog2 : CurveFn
    object Pow2 : CurveFn
}

class SimCurveSpec(val id: String, val label: String, val fn: CurveFn)
class SimBandSpec(val id: String, val label: String, val fn: CurveFn, val k: Double)

/** A bar of a chart: a fixed [value], or the value of the variable [valueVar]. */
class SimBarSpec(val id: String, val label: String, val value: Double?, val valueVar: String?)

/** Condition set of a case: every listed entry must hold. */
class SimCondition(
    val at: Map<String, String>,
    val mark: Map<String, String>,
    val vars: Map<String, SimValue>,
)

sealed interface SimEffect {
    class Say(val text: String) : SimEffect
    class Move(val moves: List<Pair<String, String>>) : SimEffect
    class Spawn(val id: String, val label: String, val at: String) : SimEffect
    class Remove(val item: String) : SimEffect
    class Mark(val marks: List<Pair<String, String>>) : SimEffect
    class Label(val labels: List<Pair<String, String>>) : SimEffect
    class Set(val values: List<Pair<String, SimValue>>) : SimEffect
    class Add(val deltas: List<Pair<String, Double>>) : SimEffect
    class Light(val ids: List<String>) : SimEffect
    class Row(val cells: List<String>) : SimEffect
    class Push(val pushes: List<Pair<String, String>>) : SimEffect
    class Pop(val lane: String) : SimEffect
}

class SimCase(val whenHolds: SimCondition?, val unless: SimCondition?, val effects: List<SimEffect>)
class SimAction(val id: String, val label: String, val cases: List<SimCase>, val refuse: String?)
class SimDrop(val item: String, val to: String, val case: SimCase, val refuse: String?) {
    val event: String get() = "$item>$to"
}

class SimToggle(val id: String, val label: String, val variable: String)
class SimSlider(val id: String, val label: String, val variable: String, val min: Double, val max: Double, val step: Double)
class SimShow(val variable: String, val label: String)
class SimTryStep(val html: String, val done: List<String>, val optional: Boolean)

/** One sim, ready for the core. [raw] is the original JSON object for layouts, which read their own template keys. */
class SimSpec(
    val id: String,
    val template: String,
    val title: String,
    val intro: String,
    val caption: String?,
    val vars: Map<String, SimValue>,
    val show: List<SimShow>,
    val items: List<SimItemSpec>,
    val lanes: Map<String, SimLaneSpec>,
    val curves: List<SimCurveSpec>,
    val bands: List<SimBandSpec>,
    val bars: List<SimBarSpec>,
    val actions: List<SimAction>,
    val drops: List<SimDrop>,
    val toggles: List<SimToggle>,
    val sliders: List<SimSlider>,
    val script: List<String>,
    val tryList: List<SimTryStep>,
    val raw: dynamic,
) {
    val hasPhone: Boolean get() = raw.phone != null && raw.phone != undefined
}

// ---- JSON reading ----

private fun has(d: dynamic, key: String): Boolean = d != null && d != undefined && d[key] != null && d[key] != undefined

private fun arr(d: dynamic): List<dynamic> =
    if (d == null || d == undefined) emptyList() else d.unsafeCast<Array<dynamic>>().toList()

private fun keysOf(d: dynamic): List<String> =
    if (d == null || d == undefined) emptyList() else js("Object").keys(d).unsafeCast<Array<String>>().toList()

private fun str(d: dynamic): String = if (d == null || d == undefined) "" else d.toString()

/** One YAML scalar as a [SimValue]. */
internal fun scalar(d: dynamic): SimValue = when (jsTypeOf(d)) {
    "number" -> (d as Number).toDouble()
    "boolean" -> if (d as Boolean) "on" else "off"
    else -> str(d)
}

private fun pairs(d: dynamic): List<Pair<String, String>> = keysOf(d).map { it to str(d[it]) }

private fun condition(d: dynamic): SimCondition? {
    if (d == null || d == undefined) return null
    return SimCondition(
        at = pairs(d.at).toMap(),
        mark = pairs(d.mark).toMap(),
        vars = keysOf(d.`var`).associateWith { scalar(d.`var`[it]) },
    )
}

private fun effect(d: dynamic): SimEffect? {
    val key = keysOf(d).firstOrNull() ?: return null
    val arg = d[key]
    return when (key) {
        "say" -> SimEffect.Say(str(arg))
        "move" -> SimEffect.Move(pairs(arg))
        "spawn" -> SimEffect.Spawn(str(arg.id), str(arg.label), str(arg.at))
        "remove" -> SimEffect.Remove(str(arg))
        "mark" -> SimEffect.Mark(pairs(arg))
        "label" -> SimEffect.Label(pairs(arg))
        "set" -> SimEffect.Set(keysOf(arg).map { it to scalar(arg[it]) })
        "add" -> SimEffect.Add(keysOf(arg).map { it to (arg[it] as Number).toDouble() })
        "light" -> SimEffect.Light(arr(arg).map { str(it) })
        "row" -> SimEffect.Row(arr(arg).map { str(it) })
        "push" -> SimEffect.Push(pairs(arg))
        "pop" -> SimEffect.Pop(str(arg))
        else -> null
    }
}

private fun case(d: dynamic): SimCase =
    SimCase(condition(d.`when`), condition(d.unless), arr(d.`do`).mapNotNull { effect(it) })

private fun curveFn(d: dynamic): CurveFn {
    if (jsTypeOf(d) == "object") {
        val c = arr(d.poly).map { (it as Number).toDouble() }
        return CurveFn.Poly(c.getOrElse(0) { 0.0 }, c.getOrElse(1) { 0.0 }, c.getOrElse(2) { 0.0 })
    }
    return when (str(d)) {
        "nlog2" -> CurveFn.NLog2
        "pow2" -> CurveFn.Pow2
        else -> CurveFn.Log2
    }
}

/**
 * Reads the `spec` of a sim block plus its [tryList]. Returns null when the JSON is not a sim this build understands
 * (for example a schema from a newer build), so the host falls back to the read-only Try this list.
 */
fun readSimSpec(id: String, template: String, spec: dynamic, tryList: List<SimTryStep>): SimSpec? = try {
    val lanes = LinkedHashMap<String, SimLaneSpec>()
    if (template == "lanes") arr(spec.lanes).forEach {
        lanes[str(it.id)] = SimLaneSpec(str(it.id), str(it.label), str(it.kind), (it.slots as Number).toInt(), it.index0 == true)
    }
    SimSpec(
        id = id,
        template = template,
        title = str(spec.title),
        intro = str(spec.intro),
        caption = if (has(spec, "caption")) str(spec.caption) else null,
        vars = keysOf(spec.vars).associateWith { scalar(spec.vars[it]) },
        show = arr(spec.show).map { SimShow(str(it.`var`), str(it.label)) },
        items = arr(spec.items).map { SimItemSpec(str(it.id), str(it.label), str(it.at)) },
        lanes = lanes,
        curves = arr(spec.curves).map { SimCurveSpec(str(it.id), str(it.label), curveFn(it.f)) },
        bands = arr(spec.bands).map { SimBandSpec(str(it.id), str(it.label), curveFn(it.f), (it.k as Number).toDouble()) },
        bars = arr(spec.bars).map {
            val v = it.value
            if (jsTypeOf(v) == "object") SimBarSpec(str(it.id), str(it.label), null, str(v.`var`))
            else SimBarSpec(str(it.id), str(it.label), (v as Number).toDouble(), null)
        },
        actions = arr(spec.actions).map { a ->
            val cases = if (has(a, "cases")) arr(a.cases).map { case(it) } else listOf(case(a))
            SimAction(str(a.id), str(a.label), cases, if (has(a, "refuse")) str(a.refuse) else null)
        },
        drops = arr(spec.drops).map { SimDrop(str(it.item), str(it.to), case(it), if (has(it, "refuse")) str(it.refuse) else null) },
        toggles = arr(spec.toggles).map { SimToggle(str(it.id), str(it.label), str(it.`var`)) },
        sliders = arr(spec.sliders).map {
            SimSlider(str(it.id), str(it.label), str(it.`var`), (it.min as Number).toDouble(), (it.max as Number).toDouble(), (it.step as Number).toDouble())
        },
        script = arr(spec.script).map { str(it) },
        tryList = tryList,
        raw = spec,
    )
} catch (e: Throwable) {
    null
}
