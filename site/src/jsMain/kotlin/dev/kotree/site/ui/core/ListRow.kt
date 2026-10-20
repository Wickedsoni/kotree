package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space1Var
import dev.kotree.site.space3Var
import dev.kotree.site.typeBodySmallLineVar
import dev.kotree.site.typeBodySmallSizeVar
import dev.kotree.site.typeBodySmallWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Button as DomButton

/**
 * List row (ADR 007): a grid of leading (24 px) / title / trailing, 44 px high, with `space.3` gaps and inline padding.
 * The element follows the job: `<a>` with [href], `<button type=button>` with [onClick], else a plain `<div>`.
 * Interactive rows get the hover and pressed state layers. [current] = tint fill, ink text and `aria-current`;
 * [muted] = muted ink for rows that lead nowhere. The title is clamped to [clampLines] lines with an ellipsis, and the
 * full text goes into `title`. [supporting] is a second line in type.label.
 */
@Composable
fun ListRow(
    title: String,
    modifier: Modifier = Modifier,
    branch: String = "kotlin",
    href: String? = null,
    onClick: (() -> Unit)? = null,
    supporting: String? = null,
    current: Boolean = false,
    muted: Boolean = false,
    clampLines: Int = 1,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val activate = onClick
    val interactive = href != null || activate != null
    val ink = branchInk(branch).value().toString()
    val contentColor = when {
        current -> ink
        muted -> colorOnSurfaceMutedVar.value().toString()
        else -> colorOnSurfaceVar.value().toString()
    }
    val layerPercent = when {
        !interactive -> 0
        pressed -> Tokens.StateLayer.pressedPercent
        hovered -> Tokens.StateLayer.hoverPercent
        else -> 0
    }
    val layer = "color-mix(in srgb, $contentColor $layerPercent%, transparent)"
    val columns = buildList {
        if (leading != null) add("${Tokens.Size.statusIconBoxPx}px")
        add("minmax(0, 1fr)")
        if (trailing != null) add("auto")
    }.joinToString(" ")
    val style: StyleScope.() -> Unit = {
        property("display", "grid")
        property("grid-template-columns", columns)
        property("column-gap", space3Var.value())
        property("align-items", "center")
        property("box-sizing", "border-box")
        property("width", "100%")
        property("min-height", Tokens.Spacing.minTargetPx.px)
        property("padding", "${space1Var.value()} ${space3Var.value()}")
        property("margin", "0")
        property("border", "0")
        property("border-radius", shapeMdVar.value())
        property("background-color", if (current) branchTint(branch).value().toString() else "transparent")
        property("background-image", "linear-gradient($layer, $layer)")
        property("color", contentColor)
        property("text-align", "start")
        property("text-decoration", "none")
        property("font-family", fontUiVar.value())
        property("font-size", typeBodySmallSizeVar.value())
        property("line-height", typeBodySmallLineVar.value())
        property("font-weight", typeBodySmallWeightVar.value())
    }
    val scoped = modifier.inBranch(branch).styleModifier(style)
    if (href != null) {
        A(
            href,
            scoped.toAttrs {
                if (current) attr("aria-current", "page")
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false; pressed = false }
                onMouseDown { pressed = true }
                onMouseUp { pressed = false }
                onBlur { pressed = false }
            }
        ) { RowContent(title, supporting, clampLines, leading, trailing) }
    } else if (activate != null) {
        DomButton(
            scoped.toAttrs {
                attr("type", "button")
                if (current) attr("aria-current", "true")
                this.onClick { activate() }
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false; pressed = false }
                onMouseDown { pressed = true }
                onMouseUp { pressed = false }
                onBlur { pressed = false }
            }
        ) { RowContent(title, supporting, clampLines, leading, trailing) }
    } else {
        Div(
            scoped.toAttrs {
                if (current) attr("aria-current", "true")
            }
        ) { RowContent(title, supporting, clampLines, leading, trailing) }
    }
}

@Composable
private fun RowContent(
    title: String,
    supporting: String?,
    clampLines: Int,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
) {
    if (leading != null) {
        Div(Modifier.styleModifier { property("display", "flex"); property("justify-content", "center") }.toAttrs()) { leading() }
    }
    Div(Modifier.styleModifier { property("min-width", "0") }.toAttrs()) {
        Span(
            Modifier.styleModifier {
                property("display", "block")
                property("overflow", "hidden")
                if (clampLines <= 1) {
                    property("white-space", "nowrap")
                    property("text-overflow", "ellipsis")
                } else {
                    property("display", "-webkit-box")
                    property("-webkit-box-orient", "vertical")
                    property("-webkit-line-clamp", clampLines.toString())
                }
            }.toAttrs { attr("title", title) }
        ) { Text(title) }
        if (supporting != null) {
            Span(
                Modifier.styleModifier {
                    property("display", "block")
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) { Text(supporting) }
        }
    }
    if (trailing != null) {
        Div(Modifier.styleModifier { property("display", "flex"); property("align-items", "center") }.toAttrs()) { trailing() }
    }
}
