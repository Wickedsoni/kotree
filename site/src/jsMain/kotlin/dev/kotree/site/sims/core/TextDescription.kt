package dev.kotree.site.sims.core

/**
 * The generated text description (ADR 009 section 4): the `intro`, then the sentence of every `script` event run through
 * the core, then the Try this steps. Same text with the stage hidden, so it works with a screen reader and without the
 * stage. Pure and deterministic.
 */
object TextDescription {
    private val TAG = Regex("<[^>]*>")

    /** Step text without markup: the build writes escaped Markdown, so tags are formatting and entities are escapes. */
    fun plain(html: String): String = TAG.replace(html, "")
        .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'").replace("&amp;", "&")

    fun lines(spec: SimSpec): List<String> {
        val out = ArrayList<String>()
        out += spec.intro
        var state = SimCore.initial(spec, spec.script)
        for (event in spec.script) {
            state = SimCore.step(spec, state, event)
            out += state.said
        }
        spec.tryList.forEachIndexed { i, t ->
            out += "Try this, step ${i + 1}${if (t.optional) " (optional)" else ""}: ${plain(t.html)}"
        }
        return out
    }
}
