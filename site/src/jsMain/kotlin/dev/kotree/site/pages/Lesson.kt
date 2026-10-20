package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.kotree.site.ui.lesson.LessonPage

/** One lesson page for every leaf (ADR 005 section 2). The export writes one HTML file per live leaf. */
@Page("/{branch}/{stem}/{leaf}")
@Composable
fun LessonRoute() {
    val params = rememberPageContext().route.params
    LessonPage("${params.getValue("branch")}/${params.getValue("stem")}/${params.getValue("leaf")}", preview = false)
}
