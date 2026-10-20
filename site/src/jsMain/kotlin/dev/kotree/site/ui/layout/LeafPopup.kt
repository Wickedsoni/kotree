package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.shapeBlobVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.Difficulty
import dev.kotree.site.ui.core.DifficultyChip
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Popup for a live leaf: shape.blob, elevation.2, title in the branch ink, summary, Open lesson, and a Watch the
 * video button only when [videoUrl] is non-empty (L26). It is a dialog: focus moves into it on open and Escape
 * or Close return focus to the opener, then call [onClose]. The caller decides where it is placed.
 */
@Composable
fun LeafPopup(
    branch: String,
    title: String,
    summary: String,
    difficulty: Difficulty,
    onOpenLesson: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    videoUrl: String = "",
) {
    val titleId = "leaf-popup-title"
    // The element that had focus when the popup opened; read before the popup takes focus.
    val opener = remember { document.activeElement as? HTMLElement }
    val close = {
        if (opener != null && opener.isConnected) opener.focus()
        onClose()
    }
    Div(
        modifier
            .inBranch(branch)
            .attrsModifier {
                attr("role", "dialog")
                attr("aria-labelledby", titleId)
                attr("tabindex", "-1")
            }
            .styleModifier {
                property("background-color", colorSurfaceVar.value())
                property("border-radius", shapeBlobVar.value())
                property("box-shadow", elevation2Var.value())
                property("padding", space6Var.value())
                property("color", colorOnSurfaceVar.value())
                property("font-family", fontUiVar.value())
                property("font-size", typeBodySizeVar.value())
                property("line-height", typeBodyLineVar.value())
            }
            .toAttrs {
                ref { element ->
                    element.focus()
                    onDispose { }
                }
                onKeyDown {
                    if (it.key == "Escape") close()
                }
            }
    ) {
        H3(
            Modifier.attrsModifier { id(titleId) }.styleModifier {
                property("margin", "0 0 ${space2Var.value()}")
                property("color", branchInk(branch).value().toString())
                property("font-size", typeTitleSizeVar.value())
                property("line-height", typeTitleLineVar.value())
                property("font-weight", typeTitleWeightVar.value())
            }.toAttrs()
        ) { Text(title) }
        Div(Modifier.styleModifier { property("margin", "0 0 ${space3Var.value()}") }.toAttrs()) { DifficultyChip(difficulty) }
        P(Modifier.styleModifier { property("margin", "0 0 ${space4Var.value()}") }.toAttrs()) { Text(summary) }
        Div(Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("gap", space2Var.value())
        }.toAttrs()) {
            Button("Open lesson", onOpenLesson, kind = ButtonKind.Filled, branch = branch)
            if (videoUrl.isNotEmpty()) {
                Button(
                    "Watch the video",
                    { window.open(videoUrl, "_blank", "noopener") },
                    kind = ButtonKind.Tonal,
                    branch = branch,
                )
            }
            Button("Close", close, kind = ButtonKind.Text, branch = branch)
        }
    }
}
