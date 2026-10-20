package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.space3Var
import dev.kotree.site.space6Var
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.Difficulty
import dev.kotree.site.ui.core.DifficultyChip
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * LessonHeader: title, difficulty chip, progress line on the branch tint (docs/04 section 1.1, parts 2 and 3).
 * The Watch the video button exists only when [videoUrl] is not blank (L26): no placeholder, no disabled button.
 * The breadcrumb is a separate layout component.
 *
 * @param progressLine for example "3 of 8 leaves in this stem"; shown as plain text.
 */
@Composable
fun LessonHeader(
    title: String,
    difficulty: Difficulty,
    branch: String,
    modifier: Modifier = Modifier,
    progressLine: String = "",
    videoUrl: String = "",
) {
    Header(
        modifier.styleModifier {
            property("box-sizing", "border-box")
            property("max-width", layoutLessonTextVar.value())
            property("background-color", branchTint(branch).value().toString())
            property("border-radius", shapeXlVar.value())
            property("padding", space6Var.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        H1(Modifier.styleModifier { property("margin", "0 0 ${space3Var.value()}"); typo(Typo.Headline) }.toAttrs()) { Text(title) }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("align-items", "center")
                property("gap", space3Var.value())
            }.toAttrs()
        ) {
            DifficultyChip(difficulty)
            if (progressLine.isNotBlank()) {
                P(Modifier.styleModifier { property("margin", "0"); typo(Typo.Label) }.toAttrs()) { Text(progressLine) }
            }
        }
        if (videoUrl.isNotBlank()) {
            Div(Modifier.styleModifier { property("margin-top", space3Var.value()) }.toAttrs()) {
                Button(
                    text = "Watch the video",
                    onClick = { window.open(videoUrl, "_blank", "noopener,noreferrer") },
                    kind = ButtonKind.Filled,
                    branch = branch,
                )
            }
        }
    }
}
