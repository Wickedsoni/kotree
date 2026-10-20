package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorSkeletonVar
import dev.kotree.site.shapeXsVar
import dev.kotree.site.space2Var
import dev.kotree.site.typeBodyLineVar
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/** Line kinds of a [Skeleton]: [Body] bars at the body line height, [Row] bars at the 44 px row height. */
enum class SkeletonLine { Body, Row }

/** Hides an element visually but keeps it for screen readers. */
internal fun StyleScope.visuallyHidden() {
    property("position", "absolute")
    property("width", "${Tokens.Space.hairlinePx}px")
    property("height", "${Tokens.Space.hairlinePx}px")
    property("margin", "-${Tokens.Space.hairlinePx}px")
    property("padding", "0")
    property("overflow", "hidden")
    property("clip", "rect(0, 0, 0, 0)")
    property("white-space", "nowrap")
    property("border", "0")
}

/**
 * Loading placeholder (ADR 007): [lines] bars of `color.skeleton`, the last one 60 % wide. Static, with no shimmer,
 * so reduced motion looks the same. The bars are `aria-hidden`; one visually hidden "Loading" is announced instead.
 */
@Composable
fun Skeleton(lines: Int, line: SkeletonLine = SkeletonLine.Body, modifier: Modifier = Modifier) {
    val count = if (lines < 1) 1 else lines
    Div(
        modifier.styleModifier {
            property("position", "relative")
            property("display", "grid")
            property("gap", space2Var.value())
        }.toAttrs { attr("role", "status") }
    ) {
        Div(Modifier.styleModifier { visuallyHidden() }.toAttrs()) { Text("Loading") }
        for (i in 0 until count) {
            val last = i == count - 1
            Div(
                Modifier
                    .attrsModifier { attr("aria-hidden", "true") }
                    .styleModifier {
                        property(
                            "height",
                            if (line == SkeletonLine.Row) "${Tokens.Spacing.minTargetPx}px" else typeBodyLineVar.value().toString(),
                        )
                        property("width", if (last && count > 1) "${Tokens.Size.skeletonLastPercent}%" else "100%")
                        property("border-radius", shapeXsVar.value())
                        property("background-color", colorSkeletonVar.value())
                    }
                    .toAttrs()
            )
        }
    }
}
