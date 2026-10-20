package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.fontCodeVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeBodyWeightVar
import dev.kotree.site.typeCodeLineVar
import dev.kotree.site.typeCodeSizeVar
import dev.kotree.site.typeCodeWeightVar
import dev.kotree.site.typeHeadlineLineVar
import dev.kotree.site.typeHeadlineSizeVar
import dev.kotree.site.typeHeadlineWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import kotlinx.browser.window
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.dom.Code
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Type rows of docs/02 section 3 that lesson components use. */
internal enum class Typo { Headline, Title, Body, Label, Code }

/** Font, size, line height and weight of a type row, all from token variables. */
internal fun StyleScope.typo(kind: Typo) {
    property("font-family", if (kind == Typo.Code) fontCodeVar.value() else fontUiVar.value())
    when (kind) {
        Typo.Headline -> {
            property("font-size", typeHeadlineSizeVar.value()); property("line-height", typeHeadlineLineVar.value())
            property("font-weight", typeHeadlineWeightVar.value())
        }
        Typo.Title -> {
            property("font-size", typeTitleSizeVar.value()); property("line-height", typeTitleLineVar.value())
            property("font-weight", typeTitleWeightVar.value())
        }
        Typo.Body -> {
            property("font-size", typeBodySizeVar.value()); property("line-height", typeBodyLineVar.value())
            property("font-weight", typeBodyWeightVar.value())
        }
        Typo.Label -> {
            property("font-size", typeLabelSizeVar.value()); property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
        }
        Typo.Code -> {
            property("font-size", typeCodeSizeVar.value()); property("line-height", typeCodeLineVar.value())
            property("font-weight", typeCodeWeightVar.value())
        }
    }
}

/** Outlined, rounded frame used by code, editor and challenge cards. */
internal fun StyleScope.frame() {
    property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
    property("border-radius", shapeMdVar.value())
    property("background-color", colorSurfaceVar.value())
    property("box-sizing", "border-box")
    property("max-width", "100%")
}

/** Calls [onDone] with true when the text reached the clipboard. The clipboard API may be missing or refused. */
internal fun copyToClipboard(text: String, onDone: (Boolean) -> Unit) {
    val clipboard = window.navigator.asDynamic().clipboard
    if (clipboard == null || clipboard.writeText == null) {
        onDone(false)
        return
    }
    try {
        clipboard.writeText(text).then({ onDone(true) }, { onDone(false) })
    } catch (e: Throwable) {
        onDone(false)
    }
}

/**
 * CodeBlock: read-only code with a copy button (docs/02 section 6). Ligatures are off through the global rule for
 * `code, pre` in Base.kt. The code area is focusable so a keyboard user can scroll long lines. The copy result is
 * announced in a polite live region. The frame does not clip (overflow stays visible) so the focus rings of the Copy
 * button and the code area are fully shown; only the code area scrolls.
 *
 * @param label accessible name of the code region, for example "Kotlin example".
 */
@Composable
fun CodeBlock(code: String, modifier: Modifier = Modifier, label: String = "Kotlin code", branch: String = "kotlin") {
    var status by remember { mutableStateOf("") }
    Div(modifier.styleModifier { frame() }.toAttrs()) {
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("justify-content", "space-between")
                property("gap", space2Var.value())
                property("padding", "0 ${space2Var.value()} 0 ${space4Var.value()}")
                property("border-bottom", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                property("color", colorOnSurfaceMutedVar.value())
                typo(Typo.Label)
            }.toAttrs()
        ) {
            Span { Text(label) }
            Button(
                text = "Copy",
                onClick = { copyToClipboard(code) { ok -> status = if (ok) "Copied" else "Select the code and copy it" } },
                kind = ButtonKind.Text,
                branch = branch,
            )
        }
        Pre(
            Modifier
                .attrsModifier { attr("tabindex", "0"); attr("role", "region"); attr("aria-label", label) }
                .styleModifier {
                    property("margin", "0")
                    property("padding", "${space3Var.value()} ${space4Var.value()}")
                    property("overflow", "auto")
                    property("color", colorOnSurfaceVar.value())
                    typo(Typo.Code)
                }.toAttrs()
        ) {
            Code { Text(code) }
        }
        Div(
            Modifier.attrsModifier { attr("role", "status"); attr("aria-live", "polite") }
                .styleModifier {
                    property("padding", "0 ${space4Var.value()}")
                    property("color", colorOnSurfaceMutedVar.value())
                    typo(Typo.Label)
                    if (status.isEmpty()) property("display", "none")
                }.toAttrs()
        ) { Text(status) }
    }
}
