package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.Chip
import dev.kotree.site.ui.core.Difficulty
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

/**
 * Popup for a leaf, built on the same card as the tree node card (ADR 007): tint band with the title and a compact
 * difficulty chip, the summary, and an end-aligned footer: Close, Watch the video (only when [videoUrl] is non-empty,
 * L26), Open lesson (only when [onOpenLesson] is not null; an up-next leaf has none). It is a dialog: focus moves into it
 * on open and Escape or Close return focus to the opener, then call [onClose]. The caller decides where it is placed.
 */
@Composable
fun LeafPopup(
    branch: String,
    title: String,
    summary: String,
    difficulty: Difficulty,
    onOpenLesson: (() -> Unit)?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    videoUrl: String = "",
) {
    // The element that had focus when the popup opened; read before the popup takes focus.
    val opener = remember { document.activeElement as? HTMLElement }
    val close = {
        if (opener != null && opener.isConnected) opener.focus()
        onClose()
    }
    NodeCard(
        branch = branch,
        title = title,
        onClose = close,
        modifier = modifier,
        clearHeader = false,
        chips = { Chip(difficulty.label, difficulty.swatch, compact = true, fill = colorSurfaceVar.value().toString()) },
        actions = {
            if (videoUrl.isNotEmpty()) {
                Button(
                    "Watch the video",
                    { window.open(videoUrl, "_blank", "noopener") },
                    kind = ButtonKind.Tonal,
                    branch = branch,
                )
            }
            if (onOpenLesson != null) Button("Open lesson", onOpenLesson, kind = ButtonKind.Filled, branch = branch)
        },
    ) {
        P(Modifier.styleModifier { property("margin", "0") }.toAttrs()) { Text(summary) }
    }
}
