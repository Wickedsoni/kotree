package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space3Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text

/**
 * LessonBlock: one section of the lesson body (docs/04 section 4), a titled card on the reading column
 * (`layout.lesson.text`) with `elevation.1` and `space.8` below. The heading is an h2 so the page outline works.
 *
 * @param id anchor id for the heading, for example "the-question".
 */
@Composable
fun LessonBlock(
    title: String,
    modifier: Modifier = Modifier,
    id: String? = null,
    content: @Composable () -> Unit,
) {
    Section(
        modifier
            .attrsModifier { if (id != null) attr("aria-labelledby", "$id-heading") }
            .styleModifier {
                property("box-sizing", "border-box")
                property("max-width", layoutLessonTextVar.value())
                property("border", elevation1Var.value())
                property("border-radius", shapeMdVar.value())
                property("padding", space6Var.value())
                property("margin-bottom", space8Var.value())
                property("color", colorOnSurfaceVar.value())
                typo(Typo.Body)
            }.toAttrs()
    ) {
        H2(
            Modifier.attrsModifier { if (id != null) id("$id-heading") }
                .styleModifier {
                    property("margin", "0 0 ${space3Var.value()}")
                    typo(Typo.Title)
                }.toAttrs()
        ) { Text(title) }
        Div { content() }
    }
}
