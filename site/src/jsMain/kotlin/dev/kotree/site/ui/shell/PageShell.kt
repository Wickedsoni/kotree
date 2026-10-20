package dev.kotree.site.ui.shell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutPageMaxVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.spaceStackPageVar
import dev.kotree.site.spaceStackSectionVar
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.layout.SiteHeader
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.Text

/** Id of the main landmark: the skip link target and `scroll` anchor. */
const val MAIN_ID = "kt-main"

/**
 * Page shell for every page except the tree (ADR 007): the skip link (first Tab stop), the sticky in-flow header, then
 * `<main id="kt-main" tabindex="-1">` on the page max-width with gutters. [branch] scopes the focus ring colour.
 * No page sets a top padding to make room for the header: it is in the flow, and hash targets clear it through
 * `scroll-padding-top`.
 */
@Composable
fun PageShell(current: NavItem, branch: String? = null, content: @Composable () -> Unit) {
    SkipLink()
    SiteHeader(current)
    val scope = if (branch != null) Modifier.inBranch(branch) else Modifier
    Main(
        scope
            .styleModifier {
                property("box-sizing", "border-box")
                property("max-width", layoutPageMaxVar.value())
                property("margin", "0 auto")
                property("padding", "${spaceStackPageVar.value()} ${layoutPageGutterVar.value()} ${spaceStackSectionVar.value()}")
                property("font-family", fontUiVar.value())
                property("font-size", typeBodySizeVar.value())
                property("line-height", typeBodyLineVar.value())
                property("color", colorOnSurfaceVar.value())
            }
            .toAttrs {
                id(MAIN_ID)
                attr("tabindex", "-1")
            }
    ) { content() }
}

/**
 * "Skip to content": hidden until it has keyboard focus, then drawn at the top-left above the header (z.skip).
 * Enter moves focus to the main landmark explicitly, because the router may handle hash links itself.
 */
@Composable
private fun SkipLink() {
    var focused by remember { mutableStateOf(false) }
    A(
        "#$MAIN_ID",
        Modifier
            .styleModifier {
                property("position", if (focused) "fixed" else "absolute")
                property("z-index", Tokens.Z.skip.toString())
                if (focused) {
                    property("top", space4Var.value())
                    property("left", space4Var.value())
                    property("padding", "${space3Var.value()} ${space4Var.value()}")
                    property("background-color", colorSurfaceVar.value())
                    property("border", elevation1Var.value())
                    property("border-radius", shapeFullVar.value())
                    property("color", colorOnSurfaceVar.value())
                    property("text-decoration", "none")
                    property("font-family", fontUiVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                } else {
                    property("left", "-${Tokens.Size.skipLinkInsetPx}px")
                    property("top", "0")
                    property("width", "${Tokens.Space.hairlinePx}px")
                    property("height", "${Tokens.Space.hairlinePx}px")
                    property("overflow", "hidden")
                }
            }
            .toAttrs {
                onFocus { focused = true }
                onBlur { focused = false }
                onClick {
                    it.preventDefault()
                    document.getElementById(MAIN_ID)?.asDynamic()?.focus()
                }
            }
    ) { Text("Skip to content") }
}
