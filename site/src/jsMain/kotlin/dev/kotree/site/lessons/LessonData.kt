package dev.kotree.site.lessons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import kotlinx.browser.window

/*
 * Lesson JSON, schema 1 (ADR 005 section 1). Parsed with JSON.parse into these external interfaces, so there is no
 * serialization library in the bundle. Field names are the ones the build writes (LessonRenderer.kt).
 */

external interface LessonData {
    val schema: Int
    val id: String
    val playground: Boolean
    val sims: Array<String>
    val blocks: Array<BlockData>
    val notes: Array<String>?
}

external interface BlockData {
    val t: String
    val html: String?
    val id: String?
    val lang: String?
    val code: String?
    val runnable: Boolean?
    val run: Boolean?
    val verified: String?
    val generated: Boolean?
    val options: Array<OptionData>?
    val output: String?
    val hints: Array<String>?
    val expected: String?
    val fix: String?
    val explanation: String?
}

external interface OptionData {
    val id: String
    val text: String
    val why: String
    val correct: Boolean
}

/** Where a lesson comes from, one state at a time. */
sealed interface LessonState {
    object Loading : LessonState
    object Failed : LessonState
    class Ready(val data: LessonData) : LessonState
}

private const val DATA_SCRIPT_ID = "kt-lesson-data"
private const val EXPORT_FLAG = "_kobwebIsExporting"

/** True while `kobweb export` snapshots the page: render an empty body and fetch nothing (ADR 005 section 2). */
fun isExporting(): Boolean = window.location.search.contains(EXPORT_FLAG)

private fun parseLesson(text: String, id: String): LessonData? = try {
    val data = JSON.parse<LessonData>(text)
    if (data.schema == 1 && data.id == id) data else null
} catch (e: Throwable) {
    null
}

/** The baked JSON of an exported page, if it is there and is this lesson (direct load, synchronous). */
private fun inlineLesson(id: String): LessonData? =
    document.getElementById(DATA_SCRIPT_ID)?.textContent?.let { parseLesson(it, id) }

/**
 * Loads a lesson: (1) the inline `script#kt-lesson-data` of a direct load, (2) else `fetch(url)`.
 * A reply that is not a lesson of this id (a 404, or an index.html answered with status 200) is a failure, so the page
 * shows Retry instead of staying empty. [retry] re-runs the fetch.
 */
@Composable
fun rememberLesson(id: String, url: String, useInline: Boolean, retries: Int): LessonState {
    var state by remember(id, url) {
        mutableStateOf<LessonState>(
            when {
                isExporting() -> LessonState.Loading
                else -> (if (useInline) inlineLesson(id) else null)?.let { LessonState.Ready(it) } ?: LessonState.Loading
            }
        )
    }
    LaunchedEffect(id, url, retries) {
        if (isExporting() || state is LessonState.Ready) return@LaunchedEffect
        state = LessonState.Loading
        window.fetch(url).then { response ->
            if (!response.ok) throw IllegalStateException("HTTP ${response.status}")
            response.text()
        }.then { text ->
            val data = parseLesson(text, id)
            state = if (data != null) LessonState.Ready(data) else LessonState.Failed
        }.catch {
            state = LessonState.Failed
        }
    }
    return state
}
