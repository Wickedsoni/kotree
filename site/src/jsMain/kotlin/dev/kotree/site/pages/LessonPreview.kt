package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.kotree.site.ui.lesson.LessonPage

/**
 * Draft preview: `/preview/<branch>/<stem>/<leaf>`. Not linked from anywhere, noindex, never exported (it is a dynamic
 * route, and its JSON only exists in a dev run). In a production site it shows the "This lesson could not load" box.
 */
@Page("/preview/{branch}/{stem}/{leaf}")
@Composable
fun LessonPreviewRoute() {
    val params = rememberPageContext().route.params
    LessonPage("${params.getValue("branch")}/${params.getValue("stem")}/${params.getValue("leaf")}", preview = true)
}
