package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.kotree.site.ui.lesson.NotFoundContent
import kotlinx.browser.window

/** `404.html` of the export (docs/03 section 7.4). `emitLessonPages` adds the robots noindex tag to the file. */
@Page("/404")
@Composable
fun NotFoundPage() {
    NotFoundContent(window.location.pathname)
}
