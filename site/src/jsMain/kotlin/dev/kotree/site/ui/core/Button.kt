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
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeBlobVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.space4Var
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Button as DomButton

/** Button kinds (docs/02 section 6): ink fill, tint fill, no fill. */
enum class ButtonKind { Filled, Tonal, Text }

private const val BRANCH_DEFAULT = "kotlin"

private val pressSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatialFast.dampingRatio, Tokens.Spring.spatialFast.stiffness).cssLinear()
}

/**
 * Button: at least 44 px high, shape.full that morphs to shape.md while pressed (motion.spatial.fast, as a CSS
 * `linear()` spring; instant under prefers-reduced-motion by the global rule in Base.kt). The focus ring comes
 * from Base.kt. A disabled button keeps readable text: muted ink on white with an outline, not a faded fill.
 *
 * @param blob rest shape is shape.blob instead of shape.full (used by "Start here").
 * @param branch branch slug (docs/00 section 5) that supplies the ink and tint.
 */
@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    kind: ButtonKind = ButtonKind.Filled,
    branch: String = BRANCH_DEFAULT,
    enabled: Boolean = true,
    blob: Boolean = false,
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val ink = branchInk(branch).value().toString()
    val tint = branchTint(branch).value().toString()
    val surface = colorSurfaceVar.value().toString()
    val muted = colorOnSurfaceMutedVar.value().toString()
    val onInk = colorOnBranchVar.value().toString()
    val fill: String
    var content: String
    when {
        !enabled -> { fill = surface; content = muted }
        kind == ButtonKind.Filled -> { fill = ink; content = onInk }
        kind == ButtonKind.Tonal -> { fill = tint; content = ink }
        else -> { fill = "transparent"; content = ink }
    }
    if (enabled && kind == ButtonKind.Tonal && (pressed || hovered)) {
        content = "color-mix(in srgb, $ink ${100 - Tokens.Chrome.tonalStateInkDarkenPercent}%, black)"
    }
    val layerPercent = when {
        !enabled -> 0
        pressed -> Tokens.Chrome.pressedLayerPercent
        hovered -> Tokens.Chrome.hoverLayerPercent
        else -> 0
    }
    val layer = "color-mix(in srgb, $content $layerPercent%, transparent)"
    val (seconds, easing) = pressSpring
    DomButton(
        modifier
            .styleModifier {
                property("min-height", Tokens.Spacing.minTargetPx.px)
                property("min-width", Tokens.Spacing.minTargetPx.px)
                property("padding", "0 ${space4Var.value()}")
                property("border", if (enabled) "0" else "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
                property("border-radius", "${if (pressed && enabled) shapeMdVar.value() else if (blob) shapeBlobVar.value() else shapeFullVar.value()}")
                property("background-color", fill)
                property("background-image", "linear-gradient($layer, $layer)")
                property("color", content)
                property("font-family", fontUiVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("cursor", if (enabled) "pointer" else "not-allowed")
                property("transition", "border-radius ${seconds}s $easing")
            }
            .toAttrs {
                attr("type", "button")
                if (!enabled) attr("disabled", "")
                onClick { if (enabled) onClick() }
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false; pressed = false }
                onMouseDown { pressed = true }
                onMouseUp { pressed = false }
                onKeyDown { if (it.key == " " || it.key == "Enter") pressed = true }
                onKeyUp { pressed = false }
                onBlur { pressed = false }
            }
    ) {
        Text(text)
    }
}
