package dev.kotree.build

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.error.YAMLException

// ADR 009 sections 1, 2 and 6: the ```sim fence of a leaf. Parse (SnakeYAML SafeConstructor, like `challenge` blocks),
// validate SM2-SM8 and SM10, normalise, write JSON. SM0, SM1 and SM9 need the whole leaf and live in LessonRenderer/Validate.

/** The exact caption of a `phone` add-on (ADR 009 section 2, docs/05 section 3.4). */
const val SIM_CAPTION = "A picture of what Android does. Not a real phone."

/** Largest JSON of one sim block in bytes (rule SM10). */
const val SIM_MAX_JSON_BYTES = 8 * 1024

/** One Try this step: [html] is Markdown rendered with escapeHtml on and the outer paragraph removed. */
data class SimTryStep(val html: String, val done: List<String>, val optional: Boolean)

val SIM_TEMPLATES = listOf("stepper", "zones", "lanes", "flow", "chart", "states", "compare")
val SIM_MARKS = setOf("plain", "changed", "good", "bad", "ghost", "picked")

private val COMMON_KEYS = setOf(
    "id", "template", "title", "intro", "caption", "phone", "timeline", "vars", "show", "actions", "toggles", "sliders", "script", "try",
)
private val TEMPLATE_KEYS = mapOf(
    "stepper" to setOf("lines", "code", "groups", "board", "log"),
    "zones" to setOf("zones", "items", "drops"),
    "lanes" to setOf("lanes", "items", "drops"),
    "flow" to setOf("stations", "paths", "items"),
    "chart" to setOf("axes", "bars", "curves", "bands", "marks", "scale"),
    "states" to setOf("nodes", "edges", "start", "layout", "items"),
    "compare" to setOf("sides"),
)
private val TEMPLATE_REQUIRED = mapOf(
    "stepper" to setOf("lines"), "zones" to setOf("zones", "items"), "lanes" to setOf("lanes"), "flow" to setOf("stations"),
    "chart" to setOf("axes"), "states" to setOf("nodes", "edges", "start"), "compare" to setOf("sides"),
)
private val ENVELOPE_REQUIRED = setOf("id", "template", "title", "intro", "try")

// Output order of the normalised spec: top-level keys by this list, nested record keys by RECORD_ORDER (rest keeps source order).
private val KEY_ORDER = listOf(
    "title", "intro", "caption", "lines", "code", "groups", "board", "log", "zones", "items", "lanes", "stations", "paths",
    "axes", "bars", "curves", "bands", "marks", "scale", "nodes", "edges", "start", "layout", "sides", "phone", "timeline",
    "vars", "show", "actions", "drops", "toggles", "sliders", "script",
)
private val RECORD_ORDER = listOf(
    "id", "label", "text", "at", "kind", "slots", "index0", "from", "to", "var", "item", "value", "f", "k", "min", "max", "step",
    "start", "stop", "inclusive", "down", "rows", "columns", "boxes", "lines", "when", "unless", "do", "cases", "refuse", "done", "optional",
)

private val ID = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")
private val VAR = Regex("^[a-z][A-Za-z0-9_]*$")
private val INTERPOLATION = Regex("\\{([a-zA-Z][A-Za-z0-9_.]*)}")
private val SLOT = Regex("^([a-z0-9]+(?:-[a-z0-9]+)*)\\.(\\d+)$")
private val CURVES = setOf("log2", "nlog2", "pow2")
private val EFFECTS = setOf("say", "move", "spawn", "remove", "mark", "label", "set", "add", "light", "row", "push", "pop")
private val CONDITIONS = setOf("at", "mark", "var")

/**
 * Parses, checks and normalises one ```sim block. [fail] gets the 0-based line offset inside the block and a message that
 * starts with the rule (`SM2: ...`). [branch] is the leaf's branch (profiling leaves take no `phone`, SM8).
 */
fun parseSimBlock(yamlText: String, branch: String, fail: (Int, String) -> Nothing): SimLessonBlock {
    val root: Any? = try {
        Yaml(SafeConstructor(LoaderOptions().apply { isAllowDuplicateKeys = false })).load<Any?>(yamlText)
    } catch (e: YAMLException) {
        fail(yamlErrorLine(e), "SM2: the sim block is not valid YAML: ${yamlErrorText(e)}")
    }
    return SimChecker(branch) { fail(0, it) }.check(root)
}

private class SimChecker(val branch: String, val failFn: (String) -> Nothing) {
    fun bad(rule: String, msg: String): Nothing = failFn("$rule: $msg")

    // ---- typed access ----
    fun isScalar(v: Any?) = v is String || v is Number || v is Boolean

    fun text(v: Any?, path: String, rule: String = "SM2"): String {
        if (!isScalar(v)) bad(rule, "`$path` must be text")
        val s = v.toString()
        if (s.isBlank()) bad(rule, "`$path` must not be empty")
        return s
    }

    fun num(v: Any?, path: String): Double = (v as? Number)?.toDouble() ?: bad("SM2", "`$path` must be a number")

    fun int(v: Any?, path: String): Int {
        val d = num(v, path)
        if (d != Math.floor(d)) bad("SM2", "`$path` must be a whole number")
        return d.toInt()
    }

    fun flag(v: Any?, path: String): Boolean = v as? Boolean ?: bad("SM2", "`$path` must be true or false")

    @Suppress("UNCHECKED_CAST")
    fun map(v: Any?, path: String, rule: String = "SM2"): MutableMap<String, Any?> {
        if (v !is Map<*, *>) bad(rule, "`$path` must be a mapping")
        for (k in v.keys) if (k !is String) bad(rule, "`$path` has a key that is not text")
        return v as MutableMap<String, Any?>
    }

    @Suppress("UNCHECKED_CAST")
    fun list(v: Any?, path: String, max: Int, min: Int = 0, rule: String = "SM4"): MutableList<Any?> {
        if (v !is List<*>) bad("SM2", "`$path` must be a list")
        if (v.size > max) bad(rule, "`$path` holds ${v.size} entries, the limit is $max")
        if (v.size < min) bad(rule, "`$path` needs at least $min entries")
        return v as MutableList<Any?>
    }

    fun keys(m: Map<String, Any?>, allowed: Set<String>, required: Set<String>, path: String, rule: String = "SM2") {
        for (k in m.keys) if (k !in allowed) bad(rule, "unknown key `$k` in $path")
        for (k in required) if (m[k] == null) bad(rule, "`$k` is missing in $path")
    }

    fun id(v: Any?, path: String): String {
        val s = text(v, path)
        if (!ID.matches(s)) bad("SM2", "`$path` value `$s` must be lowercase kebab-case")
        return s
    }

    // ---- collected names ----
    val vars = LinkedHashMap<String, Any?>()
    val places = HashSet<String>()           // places effects can target
    val lanes = HashMap<String, Pair<String, Int>>() // lane id -> (kind, slots)
    val ids = HashSet<String>()              // every declared entity id (for mark, label, light)
    val itemIds = HashSet<String>()
    val dynamicSpawns = ArrayList<Regex>()
    val staticSpawns = HashSet<String>()
    val events = HashSet<String>()
    val sliders = HashMap<String, Triple<Double, Double, Double>>()
    val toggles = HashSet<String>()
    var logColumns = 0
    var hasPhone = false
    var tpl = ""

    fun declare(set: MutableSet<String>, id: String, path: String) {
        if (!set.add(id)) bad("SM2", "duplicate id `$id` in $path")
    }

    fun placeOk(p: String): Boolean {
        if (p in places) return true
        val m = SLOT.matchEntire(p)
        if (m != null) {
            val lane = lanes[m.groupValues[1]]
            if (lane != null && m.groupValues[2].toInt() < lane.second) return true
        }
        return false
    }

    fun place(v: Any?, path: String): String {
        val p = text(v, path, "SM5")
        if (!placeOk(p)) bad("SM5", "`$p` in $path is not a place of this $tpl sim")
        return p
    }

    fun item(v: Any?, path: String): String {
        val s = text(v, path, "SM5")
        val known = s in itemIds || s in staticSpawns || dynamicSpawns.any { it.matches(s) }
        if (!known) bad("SM5", "`$s` in $path is not an item of this sim")
        return s
    }

    fun anyId(v: Any?, path: String): String {
        val s = text(v, path, "SM5")
        val known = s in ids || s in itemIds || s in staticSpawns || placeOk(s) || dynamicSpawns.any { it.matches(s) }
        if (!known) bad("SM5", "`$s` in $path is not an id of this sim")
        return s
    }

    fun varName(v: Any?, path: String): String {
        val s = text(v, path, "SM5")
        if (s !in vars && !published(s)) bad("SM5", "variable `$s` in $path is not declared in `vars`")
        return s
    }

    fun published(s: String): Boolean {
        if (tpl != "chart") return false
        val base = s.substringBefore('.')
        val suffix = s.substringAfter('.', "")
        return (suffix == "at" && base in ids) || (suffix == "exit" && base in ids)
    }

    fun interpolations(s: String, path: String) {
        for (m in INTERPOLATION.findAll(s)) {
            val name = m.groupValues[1]
            if (name !in vars && !published(name)) bad("SM5", "`{$name}` in $path is not a declared variable")
        }
    }

    fun spawnPattern(id: String): Regex? {
        if (!id.contains('{')) return null
        val hole = Regex("\\{[a-zA-Z][A-Za-z0-9_.]*}")
        val sb = StringBuilder()
        var last = 0
        for (h in hole.findAll(id)) {
            sb.append(Regex.escape(id.substring(last, h.range.first))).append("\\d+")
            last = h.range.last + 1
        }
        sb.append(Regex.escape(id.substring(last)))
        return Regex(sb.toString())
    }

    // ---- main ----
    fun check(root: Any?): SimLessonBlock {
        val m = map(root, "the sim block")
        keys(m, COMMON_KEYS + TEMPLATE_KEYS.values.flatten(), ENVELOPE_REQUIRED, "the sim block")
        val id = id(m["id"], "id")
        tpl = text(m["template"], "template", "SM3")
        if (tpl !in SIM_TEMPLATES) bad("SM3", "template `$tpl` must be one of ${SIM_TEMPLATES.joinToString(", ")}")
        val allowed = COMMON_KEYS + TEMPLATE_KEYS.getValue(tpl)
        for (k in m.keys) if (k !in allowed) bad("SM2", "key `$k` is not allowed in a $tpl sim")
        keys(m, allowed, ENVELOPE_REQUIRED + TEMPLATE_REQUIRED.getValue(tpl), "a $tpl sim")

        val title = text(m["title"], "title")
        if (title.length > 60) bad("SM4", "`title` has ${title.length} characters, the limit is 60")
        text(m["intro"], "intro")
        if (m["caption"] != null) text(m["caption"], "caption")

        readVars(m["vars"])
        readTemplate(m)
        readPhoneAndTimeline(m)
        interpolations(m["intro"].toString(), "intro")
        readShow(m["show"])
        readEvents(m)
        readScript(m["script"])
        val tryList = readTry(m["try"])
        if (m["phone"] != null) {
            if (branch == "profiling") bad("SM8", "a profiling leaf must not use `phone` (docs/05 S11)")
            if (m["caption"] != SIM_CAPTION) bad("SM8", "`phone` needs `caption: $SIM_CAPTION` exactly")
        }

        val spec = LinkedHashMap<String, Any?>()
        for (k in KEY_ORDER) if (m.containsKey(k)) spec[k] = norm(m[k])
        val block = SimLessonBlock(id, tpl, spec, tryList)
        val bytes = simBlockJson(block).toByteArray(Charsets.UTF_8).size
        if (bytes > SIM_MAX_JSON_BYTES) bad("SM10", "the sim block is $bytes bytes as JSON, the limit is $SIM_MAX_JSON_BYTES")
        return block
    }

    fun readVars(v: Any?) {
        if (v == null) return
        for ((k, value) in map(v, "vars")) {
            if (!VAR.matches(k)) bad("SM2", "variable name `$k` must start with a lowercase letter and use letters, digits or _")
            if (!isScalar(value)) bad("SM2", "variable `$k` must be text or a number")
            vars[k] = value
        }
    }

    fun labelled(v: Any?, path: String, max: Int, min: Int = 0, set: MutableSet<String>? = null, extra: Set<String> = emptySet(), extraRequired: Set<String> = emptySet()): List<Map<String, Any?>> =
        list(v, path, max, min).mapIndexed { n, e ->
            val r = map(e, "$path[${n + 1}]")
            keys(r, setOf("id", "label") + extra, setOf("id", "label") + extraRequired, "$path[${n + 1}]")
            val rid = id(r["id"], "$path[${n + 1}].id")
            text(r["label"], "$path[${n + 1}].label")
            if (set != null) declare(set, rid, path)
            r
        }

    fun readTemplate(m: Map<String, Any?>) {
        when (tpl) {
            "stepper" -> {
                val lineIds = HashSet<String>()
                labelledText(m["lines"], "lines", 12, lineIds)
                ids += lineIds; places += lineIds
                if (m["code"] != null) flag(m["code"], "code")
                m["groups"]?.let { g ->
                    val gids = HashSet<String>()
                    for (r in labelled(g, "groups", 3, extra = setOf("lines"), extraRequired = setOf("lines"), set = gids)) {
                        for (l in list(r["lines"], "groups.lines", 12)) {
                            if (text(l, "groups.lines") !in lineIds) bad("SM5", "group line `$l` is not in `lines`")
                        }
                    }
                    ids += gids; places += gids
                }
                m["board"]?.let { b ->
                    for ((n, e) in list(b, "board", 8).withIndex()) {
                        val r = map(e, "board[${n + 1}]")
                        keys(r, setOf("var", "label"), setOf("var", "label"), "board[${n + 1}]")
                        val name = varName(r["var"], "board[${n + 1}].var")
                        text(r["label"], "board[${n + 1}].label")
                        places += name; ids += name
                    }
                }
                m["log"]?.let { l ->
                    val r = map(l, "log")
                    keys(r, setOf("columns"), setOf("columns"), "log")
                    logColumns = list(r["columns"], "log.columns", 5, 1).onEach { text(it, "log.columns") }.size
                }
            }
            "zones" -> {
                val zoneIds = HashSet<String>()
                labelled(m["zones"], "zones", 4, 2, zoneIds)
                places += zoneIds; ids += zoneIds
                items(m["items"], 12) { at -> if (at !in zoneIds) bad("SM5", "item place `$at` is not a zone") }
            }
            "lanes" -> {
                val laneIds = HashSet<String>()
                for (r in labelled(m["lanes"], "lanes", 3, 1, laneIds, setOf("kind", "slots", "index0"), setOf("kind", "slots"))) {
                    val kind = text(r["kind"], "lanes.kind")
                    if (kind !in setOf("row", "queue", "stack")) bad("SM3", "lane kind `$kind` must be row, queue or stack")
                    val slots = int(r["slots"], "lanes.slots")
                    if (slots < 1 || slots > 16) bad("SM4", "a lane has $slots slots, the limit is 1 to 16")
                    if (r["index0"] != null) flag(r["index0"], "lanes.index0")
                    lanes[r["id"].toString()] = kind to slots
                }
                ids += laneIds
                items(m["items"], 24) { at ->
                    if (!SLOT.matches(at) || !placeOk(at)) bad("SM5", "item place `$at` must be <lane>.<slot> inside the lane")
                }
            }
            "flow" -> {
                val st = HashSet<String>()
                labelled(m["stations"], "stations", 10, 2, st)
                places += st; ids += st
                m["paths"]?.let { p ->
                    var first: String? = null
                    for (path in list(p, "paths", 3)) {
                        val seq = list(path, "paths[]", 10, 2).map { text(it, "paths[]", "SM5") }
                        for (s in seq) if (s !in st) bad("SM5", "path station `$s` is not in `stations`")
                        if (first != null && first != seq[0]) bad("SM4", "all paths must share the first station `$first`")
                        first = seq[0]
                    }
                }
                items(m["items"], 8) { at -> if (at !in st) bad("SM5", "item place `$at` is not a station") }
            }
            "chart" -> {
                val axes = map(m["axes"], "axes")
                keys(axes, setOf("x", "y"), setOf("x", "y"), "axes")
                text(axes["x"], "axes.x"); text(axes["y"], "axes.y")
                if (m["bars"] == null && m["curves"] == null) bad("SM2", "a chart sim needs `bars` or `curves`")
                val bars = HashSet<String>()
                m["bars"]?.let {
                    for (r in labelled(it, "bars", 6, set = bars, extra = setOf("value"), extraRequired = setOf("value"))) {
                        val value = r["value"]
                        if (value is Map<*, *>) {
                            val vm = map(value, "bars.value"); keys(vm, setOf("var"), setOf("var"), "bars.value")
                            text(vm["var"], "bars.value.var", "SM5").let { n -> if (n !in vars) bad("SM5", "variable `$n` in bars.value is not declared in `vars`") }
                        } else num(value, "bars.value")
                    }
                }
                ids += bars
                val curves = HashSet<String>()
                m["curves"]?.let { for (r in labelled(it, "curves", 3, set = curves, extra = setOf("f"), extraRequired = setOf("f"))) curveFn(r["f"], "curves.f") }
                val bands = HashSet<String>()
                m["bands"]?.let {
                    for (r in labelled(it, "bands", 2, set = bands, extra = setOf("kind", "f", "k"), extraRequired = setOf("f", "k"))) {
                        val kind = r["kind"]
                        if (kind != "floor" && kind != "ceiling") bad("SM11", "band `${r["id"]}` needs `kind: floor` or `kind: ceiling`")
                        curveFn(r["f"], "bands.f"); num(r["k"], "bands.k")
                    }
                }
                ids += curves; ids += bands; places += bars; places += curves
                m["marks"]?.let { mk ->
                    val r = map(mk, "marks")
                    keys(r, setOf("start", "stop", "step", "inclusive", "down"), setOf("start", "stop", "step"), "marks")
                    num(r["start"], "marks.start"); num(r["stop"], "marks.stop")
                    if (num(r["step"], "marks.step") <= 0.0) bad("SM2", "`marks.step` must be above 0")
                    r["inclusive"]?.let { flag(it, "marks.inclusive") }; r["down"]?.let { flag(it, "marks.down") }
                }
                m["scale"]?.let { sc ->
                    val r = map(sc, "scale")
                    keys(r, setOf("x", "y"), emptySet(), "scale")
                    r["x"]?.let { x -> val xs = list(x, "scale.x", 2, 2); if (num(xs[0], "scale.x") >= num(xs[1], "scale.x")) bad("SM2", "`scale.x` must be [min, max] with min below max") }
                    r["y"]?.let { y -> if (y != "auto") bad("SM2", "`scale.y` must be auto") }
                }
            }
            "states" -> {
                val nodes = HashSet<String>()
                labelled(m["nodes"], "nodes", 6, 1, nodes)
                places += nodes; ids += nodes
                val edgeIds = HashSet<String>()
                for ((n, e) in list(m["edges"], "edges", 10).withIndex()) {
                    val r = map(e, "edges[${n + 1}]")
                    keys(r, setOf("from", "to", "label"), setOf("from", "to"), "edges[${n + 1}]")
                    val from = text(r["from"], "edges.from", "SM5"); val to = text(r["to"], "edges.to", "SM5")
                    if (from !in nodes || to !in nodes) bad("SM5", "edge `$from>$to` names a node that does not exist")
                    if (r["label"] != null) text(r["label"], "edges.label")
                    edgeIds += "$from>$to"
                }
                places += edgeIds
                if (text(m["start"], "start", "SM5") !in nodes) bad("SM5", "`start` is not a node")
                m["layout"]?.let { if (it != "ring" && it != "row") bad("SM3", "`layout` must be ring or row") }
                items(m["items"], 8) { at -> if (at !in nodes) bad("SM5", "item place `$at` is not a node") }
            }
            "compare" -> {
                val sideIds = HashSet<String>()
                val sides = list(m["sides"], "sides", 2, 2)
                for ((n, e) in sides.withIndex()) {
                    val r = map(e, "sides[${n + 1}]")
                    keys(r, setOf("id", "label", "boxes"), setOf("id", "label", "boxes"), "sides[${n + 1}]")
                    val sid = id(r["id"], "sides.id"); text(r["label"], "sides.label"); declare(sideIds, sid, "sides")
                    val boxIds = HashSet<String>()
                    for (b in labelled(r["boxes"], "sides.boxes", 6, 1, boxIds)) places += "$sid.${b["id"]}"
                }
                ids += sideIds; places += sideIds
            }
        }
        // initial items may refer to nothing else; spawn ids are collected later in readEvents
    }

    fun labelledText(v: Any?, path: String, max: Int, set: MutableSet<String>) {
        for ((n, e) in list(v, path, max, 1).withIndex()) {
            val r = map(e, "$path[${n + 1}]")
            keys(r, setOf("id", "text"), setOf("id", "text"), "$path[${n + 1}]")
            declare(set, id(r["id"], "$path.id"), path); text(r["text"], "$path.text")
        }
    }

    fun items(v: Any?, max: Int, atCheck: (String) -> Unit) {
        if (v == null) return
        for ((n, e) in list(v, "items", max).withIndex()) {
            val r = map(e, "items[${n + 1}]")
            keys(r, setOf("id", "label", "at"), setOf("id", "label", "at"), "items[${n + 1}]")
            declare(itemIds, id(r["id"], "items.id"), "items"); text(r["label"], "items.label")
            atCheck(text(r["at"], "items.at", "SM5"))
        }
    }

    fun curveFn(v: Any?, path: String) {
        if (v is Map<*, *>) {
            val r = map(v, path); keys(r, setOf("poly"), setOf("poly"), path)
            list(r["poly"], "$path.poly", 3, 3).forEach { num(it, "$path.poly") }
        } else if (text(v, path) !in CURVES) bad("SM3", "`$path` must be poly, log2, nlog2 or pow2")
    }

    fun readPhoneAndTimeline(m: Map<String, Any?>) {
        m["phone"]?.let { p ->
            hasPhone = true
            val r = map(p, "phone"); keys(r, setOf("rows"), setOf("rows"), "phone")
            val rows = HashSet<String>()
            labelled(r["rows"], "phone.rows", 6, 1, rows)
            for (row in rows) { places += "phone.$row"; ids += "phone.$row" }
        }
        m["timeline"]?.let { t ->
            val chips = HashSet<String>()
            labelled(t, "timeline", 10, 1, chips)
            ids += chips
        }
    }

    fun readShow(v: Any?) {
        if (v == null) return
        for ((n, e) in list(v, "show", 4).withIndex()) {
            val r = map(e, "show[${n + 1}]")
            keys(r, setOf("var", "label"), setOf("var", "label"), "show[${n + 1}]")
            varName(r["var"], "show.var"); text(r["label"], "show.label")
        }
    }

    // ---- actions, drops, toggles, sliders ----
    fun readEvents(m: MutableMap<String, Any?>) {
        // Pass 1: collect spawn ids and event ids, so effects can refer to items created by other cases.
        val actionList = m["actions"]?.let { list(it, "actions", 6) } ?: mutableListOf()
        val dropList = m["drops"]?.let { list(it, "drops", 24) } ?: mutableListOf()
        collectSpawns(actionList); collectSpawns(dropList)

        val actionIds = HashSet<String>()
        for ((n, a) in actionList.withIndex()) {
            val r = map(a, "actions[${n + 1}]")
            keys(r, setOf("id", "label", "cases", "do", "refuse"), setOf("id", "label"), "actions[${n + 1}]")
            val aid = id(r["id"], "actions.id"); declare(actionIds, aid, "actions"); events += aid
            text(r["label"], "actions[$aid].label")
            if ((r["cases"] == null) == (r["do"] == null)) bad("SM2", "action `$aid` needs exactly one of `cases` and `do`")
            if (r["do"] != null) { // `do:` is one case without a condition
                r["cases"] = mutableListOf<Any?>(linkedMapOf("do" to r.remove("do")))
            }
            for ((k, c) in list(r["cases"], "actions[$aid].cases", 12, 1).withIndex()) {
                val cm = map(c, "actions[$aid].cases[${k + 1}]")
                keys(cm, setOf("when", "unless", "do"), setOf("do"), "actions[$aid].cases[${k + 1}]")
                condition(cm["when"], "actions[$aid].when"); condition(cm["unless"], "actions[$aid].unless")
                effects(cm["do"], "actions[$aid].do")
            }
            if (r["refuse"] != null) interpolated(r["refuse"], "actions[$aid].refuse")
        }
        for ((n, d) in dropList.withIndex()) {
            val r = map(d, "drops[${n + 1}]")
            keys(r, setOf("item", "to", "when", "unless", "do", "refuse"), setOf("item", "to", "do"), "drops[${n + 1}]")
            val it = item(r["item"], "drops.item"); val to = place(r["to"], "drops.to")
            events += "$it>$to"
            condition(r["when"], "drops[$it>$to].when"); condition(r["unless"], "drops[$it>$to].unless")
            effects(r["do"], "drops[$it>$to].do")
            if (r["refuse"] != null) interpolated(r["refuse"], "drops[$it>$to].refuse")
        }
        m["toggles"]?.let { t ->
            for (r in labelled(t, "toggles", 2, extra = setOf("var"), extraRequired = setOf("var"))) {
                val tid = r["id"].toString(); varName(r["var"], "toggles.var")
                toggles += tid; events += "$tid=on"; events += "$tid=off"
            }
        }
        m["sliders"]?.let { s ->
            for (r in labelled(s, "sliders", 3, extra = setOf("var", "min", "max", "step"), extraRequired = setOf("var", "min", "max", "step"))) {
                val sid = r["id"].toString(); varName(r["var"], "sliders.var")
                val lo = num(r["min"], "sliders.min"); val hi = num(r["max"], "sliders.max"); val st = num(r["step"], "sliders.step")
                if (lo >= hi || st <= 0.0) bad("SM2", "slider `$sid` needs min below max and step above 0")
                sliders[sid] = Triple(lo, hi, st)
            }
        }
    }

    fun eventOk(e: String): Boolean {
        if (e in events) return true
        val eq = e.indexOf('=')
        if (eq > 0) {
            val sl = sliders[e.substring(0, eq)] ?: return false
            val v = e.substring(eq + 1).toDoubleOrNull() ?: return false
            return v >= sl.first && v <= sl.second
        }
        return false
    }

    fun readScript(v: Any?) {
        if (v == null) return
        for (e in list(v, "script", 20)) {
            val s = text(e, "script", "SM5")
            if (!eventOk(s)) bad("SM5", "script event `$s` is not an action, drop, toggle or slider event of this sim")
        }
    }

    fun readTry(v: Any?): List<SimTryStep> {
        val steps = list(v, "try", 4, 1)
        val out = ArrayList<SimTryStep>()
        for ((n, e) in steps.withIndex()) {
            val r = map(e, "try[${n + 1}]", "SM7")
            keys(r, setOf("text", "done", "optional"), setOf("text", "done"), "try[${n + 1}]", "SM7")
            val optional = r["optional"]?.let { o -> o as? Boolean ?: bad("SM7", "`try[${n + 1}].optional` must be true or false") } ?: false
            val done = list(r["done"], "try[${n + 1}].done", 20, 1, "SM7").map { d ->
                text(d, "try.done", "SM7").also { ev -> if (!eventOk(ev)) bad("SM7", "try step ${n + 1}: done event `$ev` does not exist in this sim") }
            }
            val html = renderMarkdown(text(r["text"], "try[${n + 1}].text", "SM7")).let { h ->
                if (h.startsWith("<p>") && h.endsWith("</p>") && h.indexOf("<p>", 1) < 0) h.removePrefix("<p>").removeSuffix("</p>") else h
            }
            out += SimTryStep(html, done, optional)
        }
        if (out.count { !it.optional } !in 1..3) bad("SM7", "`try` needs 1 to 3 steps, plus at most 1 with `optional: true`")
        if (out.count { it.optional } > 1) bad("SM7", "`try` allows at most 1 step with `optional: true`")
        return out
    }

    fun collectSpawns(entries: List<Any?>) {
        fun walk(v: Any?) {
            when (v) {
                is Map<*, *> -> {
                    val sp = v["spawn"]
                    if (sp is Map<*, *>) (sp["id"] as? String)?.let { sid ->
                        val pattern = spawnPattern(sid)
                        if (pattern != null) dynamicSpawns += pattern else staticSpawns += sid
                    }
                    v.values.forEach { walk(it) }
                }
                is List<*> -> v.forEach { walk(it) }
            }
        }
        entries.forEach { walk(it) }
    }

    fun interpolated(v: Any?, path: String): String {
        val s = text(v, path, "SM6")
        interpolations(s, path)
        return s
    }

    fun condition(v: Any?, path: String) {
        if (v == null) return
        val c = map(v, path, "SM6")
        if (c.isEmpty()) bad("SM6", "`$path` must not be empty")
        for ((k, value) in c) {
            if (k !in CONDITIONS) bad("SM6", "condition `$k` in $path must be at, mark or var")
            val inner = map(value, "$path.$k", "SM6")
            for ((a, b) in inner) when (k) {
                "at" -> { item(a, "$path.at"); place(b, "$path.at") }
                "mark" -> { anyId(a, "$path.mark"); if (text(b, "$path.mark", "SM6") !in SIM_MARKS) bad("SM6", "mark `$b` in $path must be one of ${SIM_MARKS.joinToString(", ")}") }
                else -> { varName(a, "$path.var"); if (!isScalar(b)) bad("SM6", "`$path.var` value must be text or a number") }
            }
        }
    }

    fun effects(v: Any?, path: String) {
        var says = 0
        for (e in list(v, path, 24, 1, "SM6")) {
            val r = map(e, "$path effect", "SM6")
            if (r.size != 1) bad("SM6", "every effect in $path is one key; found ${r.keys}")
            val (name, arg) = r.entries.first()
            if (name !in EFFECTS) bad("SM6", "effect `$name` in $path must be one of ${EFFECTS.joinToString(", ")}")
            when (name) {
                "say" -> { says++; interpolated(arg, "$path.say") }
                "move" -> for ((k, p) in map(arg, "$path.move", "SM6")) { item(k, "$path.move"); place(p, "$path.move") }
                "spawn" -> {
                    val s = map(arg, "$path.spawn", "SM6"); keys(s, setOf("id", "label", "at"), setOf("id", "label", "at"), "$path.spawn", "SM6")
                    text(s["id"], "$path.spawn.id", "SM6"); interpolated(s["label"], "$path.spawn.label"); place(s["at"], "$path.spawn.at")
                }
                "remove" -> item(arg, "$path.remove")
                "mark" -> for ((k, mk) in map(arg, "$path.mark", "SM6")) {
                    anyId(k, "$path.mark")
                    if (text(mk, "$path.mark", "SM6") !in SIM_MARKS) bad("SM6", "mark `$mk` in $path must be one of ${SIM_MARKS.joinToString(", ")}")
                }
                "label" -> for ((k, t) in map(arg, "$path.label", "SM6")) { anyId(k, "$path.label"); interpolated(t, "$path.label") }
                "set" -> for ((k, value) in map(arg, "$path.set", "SM6")) { varName(k, "$path.set"); if (!isScalar(value)) bad("SM6", "`$path.set` value must be text or a number") }
                "add" -> for ((k, value) in map(arg, "$path.add", "SM6")) { varName(k, "$path.add"); num(value, "$path.add") }
                "light" -> list(arg, "$path.light", 24).forEach { anyId(it, "$path.light") }
                "row" -> {
                    if (tpl != "stepper" || logColumns == 0) bad("SM5", "`row` in $path needs a stepper sim with `log`")
                    val cells = list(arg, "$path.row", logColumns)
                    cells.forEach { c -> interpolated(c, "$path.row") }
                }
                "push" -> for ((lane, it) in map(arg, "$path.push", "SM6")) { laneId(lane, "$path.push"); item(it, "$path.push") }
                "pop" -> laneId(arg, "$path.pop")
            }
        }
        if (says != 1) bad("SM6", "every case in $path needs exactly one `say`, found $says")
    }

    fun laneId(v: Any?, path: String) {
        val s = text(v, path, "SM5")
        if (s !in lanes) bad("SM5", "`$s` in $path is not a lane of this sim")
    }

    // ---- normalisation ----
    fun rank(k: String, order: List<String>): Int = order.indexOf(k).let { if (it < 0) Int.MAX_VALUE else it }

    fun norm(v: Any?, top: Boolean = false): Any? = when (v) {
        is Map<*, *> -> {
            val order = if (top) KEY_ORDER else RECORD_ORDER
            val out = LinkedHashMap<String, Any?>()
            v.entries.map { it.key.toString() to it.value }.sortedBy { rank(it.first, order) }.forEach { (k, value) -> out[k] = norm(value) }
            out
        }
        is List<*> -> v.map { norm(it) }
        is String, is Boolean, is Int, is Long, is Double -> v
        is Number -> v.toDouble()
        null -> null
        else -> v.toString()
    }
}

// ---- JSON ----

internal fun StringBuilder.jsonValue(v: Any?) {
    when (v) {
        null -> append("null")
        is String -> jsonString(v)
        is Boolean, is Int, is Long -> append(v.toString())
        is Double -> append(if (v == Math.floor(v) && Math.abs(v) < 1e15) v.toLong().toString() else v.toString())
        is Map<*, *> -> {
            append('{')
            var first = true
            for ((k, value) in v) {
                if (!first) append(',')
                first = false
                jsonString(k.toString()); append(':'); jsonValue(value)
            }
            append('}')
        }
        is List<*> -> {
            append('[')
            v.forEachIndexed { n, e -> if (n > 0) append(','); jsonValue(e) }
            append(']')
        }
        else -> jsonString(v.toString())
    }
}

/** JSON of one sim block (ADR 009 section 1). A legacy bare marker has no `tpl` and gives only `{t, id}`. */
fun simBlockJson(b: SimLessonBlock): String {
    val sb = StringBuilder()
    sb.append("{\"t\":\"sim\",\"id\":").jsonString(b.id)
    if (b.tpl != null) {
        sb.append(",\"tpl\":").jsonString(b.tpl)
        sb.append(",\"spec\":").jsonValue(b.spec ?: emptyMap<String, Any?>())
        sb.append(",\"try\":[")
        b.tryList.forEachIndexed { n, t ->
            if (n > 0) sb.append(',')
            sb.append("{\"html\":").jsonString(t.html)
            sb.append(",\"done\":["); t.done.forEachIndexed { k, d -> if (k > 0) sb.append(','); sb.jsonString(d) }; sb.append(']')
            sb.append(",\"optional\":").append(t.optional).append('}')
        }
        sb.append(']')
    }
    sb.append('}')
    return sb.toString()
}

/** Static markup of the Try this steps for the no-JS body (ADR 009 section 1). Empty for a legacy marker. */
fun simStaticHtml(b: SimLessonBlock): String {
    if (b.tryList.isEmpty()) return ""
    val sb = StringBuilder("<p><strong>Try this:</strong></p>\n<ol>\n")
    for (t in b.tryList) sb.append("<li>").append(t.html).append("</li>\n")
    return sb.append("</ol>\n").toString()
}
