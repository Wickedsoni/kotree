package dev.kotree.site.sims.core

/**
 * Try this progress (ADR 009 section 3): a pure function of the history, nothing stored.
 *
 * Step i is done when its `done` events occur, in order (as a subsequence), strictly after the events that finished the
 * previous step. Events that happen before a step is reachable do not count for it. An optional step that is not done does
 * not block the steps after it; a required step that is not done blocks every later step.
 *
 * @property done one flag per step.
 * @property current index of the first step that is not done, or null when every step is done.
 * @property requiredDone true when every non-optional step is done.
 * @property nextEvent the event that would move [current] forward, for the dashed hint outline; null when none.
 */
class TryProgress(val done: List<Boolean>, val current: Int?, val requiredDone: Boolean, val nextEvent: String?)

object TryProgressCalc {
    fun of(steps: List<SimTryStep>, history: List<String>): TryProgress {
        val done = BooleanArray(steps.size)
        var pos = 0 // index in history where the next step may start matching
        var blocked = false
        var nextEvent: String? = null
        var firstOpen: Int? = null
        for ((i, step) in steps.withIndex()) {
            if (blocked) continue
            var at = pos
            var matched = 0
            while (matched < step.done.size) {
                val found = (at until history.size).firstOrNull { history[it] == step.done[matched] } ?: break
                at = found + 1
                matched++
            }
            if (matched == step.done.size) {
                done[i] = true
                pos = at
            } else {
                if (firstOpen == null) { firstOpen = i; nextEvent = step.done[matched] }
                if (!step.optional) blocked = true
            }
        }
        val current = done.indexOfFirst { !it }.takeIf { it >= 0 }
        val required = steps.indices.all { done[it] || steps[it].optional }
        return TryProgress(done.toList(), current, required, if (current == firstOpen) nextEvent else null)
    }
}
