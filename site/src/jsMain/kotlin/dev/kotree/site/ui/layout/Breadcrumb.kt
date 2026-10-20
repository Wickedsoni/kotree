package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.space2Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Ol
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** One crumb. [href] is the real address (works without the callback); the last crumb is the current place. */
data class Crumb(val label: String, val href: String)

/**
 * Breadcrumb trail. Each crumb except the last is a link to its [Crumb.href]. When [onSelect] is given it gets the
 * index and the default navigation is cancelled (the tree can fly the camera instead); when it is null (the default)
 * the link navigates normally. The last crumb is plain text with aria-current.
 */
@Composable
fun Breadcrumb(
    crumbs: List<Crumb>,
    onSelect: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    branch: String = "foundation",
) {
    Nav(modifier.attrsModifier { attr("aria-label", "Breadcrumb") }.toAttrs()) {
        Ol(Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("align-items", "center")
            property("gap", space2Var.value())
            property("margin", "0")
            property("padding", "0")
            property("list-style", "none")
            property("font-family", fontUiVar.value())
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
        }.toAttrs()) {
            crumbs.forEachIndexed { index, crumb ->
                val last = index == crumbs.lastIndex
                Li(Modifier.styleModifier {
                    property("display", "flex")
                    property("align-items", "center")
                    property("gap", space2Var.value())
                }.toAttrs()) {
                    if (last) {
                        Span(Modifier.attrsModifier { attr("aria-current", "page") }
                            .styleModifier { property("color", colorOnSurfaceVar.value()) }
                            .toAttrs()) { Text(crumb.label) }
                    } else {
                        A(
                            crumb.href,
                            Modifier.styleModifier {
                                property("display", "inline-flex")
                                property("align-items", "center")
                                property("min-height", Tokens.Spacing.minTargetPx.px)
                                property("color", branchInk(branch).value().toString())
                                property("text-decoration", "underline")
                            }.toAttrs {
                                if (onSelect != null) {
                                    onClick {
                                        it.preventDefault()
                                        onSelect(index)
                                    }
                                }
                            }
                        ) { Text(crumb.label) }
                        Span(Modifier.attrsModifier { attr("aria-hidden", "true") }
                            .styleModifier { property("color", colorOnSurfaceMutedVar.value()) }
                            .toAttrs()) { Text("/") }
                    }
                }
            }
        }
    }
}

private val Int.px: String get() = "${this}px"
