package dev.kotree.site.ui.layout

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
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space4Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

private const val BRANCH_DEFAULT = "kotlin"

/**
 * A link that looks like a [dev.kotree.site.ui.core.Button] (same kinds, 44 px high, shape.full, hover layer). It is a
 * real `<a>`, so it can be opened in a new tab. Used for primary actions that navigate (ADR 007).
 */
@Composable
fun LinkButton(
    text: String,
    href: String,
    modifier: Modifier = Modifier,
    kind: ButtonKind = ButtonKind.Filled,
    branch: String = BRANCH_DEFAULT,
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val ink = branchInk(branch).value().toString()
    val tint = branchTint(branch).value().toString()
    val onInk = colorOnBranchVar.value().toString()
    val fill: String
    var content: String
    when (kind) {
        ButtonKind.Filled -> { fill = ink; content = onInk }
        ButtonKind.Tonal -> { fill = tint; content = ink }
        else -> { fill = "transparent"; content = ink }
    }
    if (kind == ButtonKind.Tonal && (pressed || hovered)) {
        content = "color-mix(in srgb, $ink ${100 - Tokens.Chrome.tonalStateInkDarkenPercent}%, black)"
    }
    val layerPercent = when {
        pressed -> Tokens.Chrome.pressedLayerPercent
        hovered -> Tokens.Chrome.hoverLayerPercent
        else -> 0
    }
    val layer = "color-mix(in srgb, $content $layerPercent%, transparent)"
    A(
        href,
        modifier
            .styleModifier {
                property("display", "inline-flex")
                property("align-items", "center")
                property("justify-content", "center")
                property("box-sizing", "border-box")
                property("min-height", Tokens.Spacing.minTargetPx.px)
                property("min-width", Tokens.Spacing.minTargetPx.px)
                property("padding", "0 ${space4Var.value()}")
                property("border-radius", shapeFullVar.value())
                property("background-color", fill)
                property("background-image", "linear-gradient($layer, $layer)")
                property("color", content)
                property("text-decoration", "none")
                property("white-space", "nowrap")
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
            }
            .toAttrs {
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false; pressed = false }
                onMouseDown { pressed = true }
                onMouseUp { pressed = false }
                onBlur { pressed = false }
            }
    ) { Text(text) }
}
