package dev.kotree.site.tree.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.tree.BoughGeo
import dev.kotree.site.tree.BranchGeo
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.camera.toScreen
import dev.kotree.site.tree.model.TreeData
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

/** CSS `rgb()` string from a token's rgb int. */
internal fun cssRgb(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

private fun smoothstep(x: Double): Double {
    val t = x.coerceIn(0.0, 1.0)
    return t * t * (3.0 - 2.0 * t)
}

/** Split progress p (docs/03 §2.4). */
internal fun splitProgress(z: Double): Double =
    smoothstep((z - Tokens.Zoom.splitStart) / (Tokens.Zoom.splitEnd - Tokens.Zoom.splitStart))

/** Bough level switch with hysteresis: enter at 1.8, leave below 1.62 (docs/03 §2.2). */
private class LevelMode {
    var inBough = false
    fun update(z: Double): Boolean {
        if (z >= Tokens.Zoom.boughEnter) inBough = true else if (z < Tokens.Zoom.boughLeave) inBough = false
        return inBough
    }
}

/**
 * Smooth DOM labels over the canvas, at world coordinates (docs/12 §2, docs/07 §7).
 * Canopy: the 5 bough titles (Foundation is the `roots` bough). From the Bough level the bough titles
 * hand over to branch titles, which fade in with opacity 2p - 1. Foundation stays labelled at every level.
 * Place inside a `position: relative` element that has the camera's size.
 */
@Composable
fun TreeLabels(
    data: TreeData,
    geometry: TreeGeometry,
    camera: CameraState,
    onBough: (BoughGeo) -> Unit,
    onBranch: (BoughGeo, BranchGeo) -> Unit,
) {
    val mode = remember { LevelMode() }
    val inBough = mode.update(camera.z)
    val branchOpacity = (2.0 * splitProgress(camera.z) - 1.0).coerceIn(0.0, 1.0)
    val boughTitles = data.boughs.associate { it.id to it.title }
    val branchTitles = data.boughs.flatMap { it.branches }.associate { it.id to it.title }

    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("inset", "0")
            property("pointer-events", "none")
            property("overflow", "hidden")
        }.toAttrs()
    ) {
        for (bough in geometry.boughs) {
            val title = boughTitles[bough.id] ?: bough.id
            val isRoots = bough.id == ROOTS_ID
            if (!inBough || isRoots) {
                LabelButton(
                    text = title,
                    at = camera.toScreen(geometry.bounds, bough.labelAnchor),
                    opacity = 1.0,
                    colour = Tokens.Color.onSurface,
                    onClick = { onBough(bough) },
                )
            }
            if (inBough && !isRoots) {
                for (branch in bough.branches) {
                    LabelButton(
                        text = branchTitles[branch.id] ?: branch.id,
                        at = camera.toScreen(geometry.bounds, branch.labelAnchor),
                        opacity = branchOpacity,
                        colour = Tokens.Color.leafShadowLabel,
                        onClick = { onBranch(bough, branch) },
                    )
                }
            }
        }
    }
}

private const val ROOTS_ID = "roots"

@Composable
private fun LabelButton(text: String, at: Vec, opacity: Double, colour: Int, onClick: () -> Unit) {
    var focusRing by remember { mutableStateOf(false) }
    val ring = Tokens.Space.grid / 2
    val outline = Tokens.Space.labelOutlinePx
    Button(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${at.x}px")
            property("top", "${at.y}px")
            property("transform", "translate(-50%, -50%)")
            property("min-width", "${Tokens.Space.hitAreaPx}px")
            property("min-height", "${Tokens.Space.hitAreaPx}px")
            property("padding", "0 ${Tokens.Space.grid}px")
            property("border", "0")
            property("background", "transparent")
            property("cursor", "pointer")
            property("pointer-events", "auto")
            property("white-space", "nowrap")
            property("opacity", "$opacity")
            property("color", cssRgb(colour))
            property("font-family", Tokens.Type.fontUi.joinToString(", "))
            property("font-size", "${Tokens.Type.labelSizePx}px")
            property("line-height", "${Tokens.Type.labelLineHeightPx}px")
            property("font-weight", "${Tokens.Type.labelWeight}")
            // Smooth text with a white outline (docs/02 §5 rule 5). The stroke is centred on the glyph edge,
            // so twice the outline width gives `labelOutlinePx` visible outside; paint-order keeps the fill on top.
            property("-webkit-text-stroke", "${outline * 2}px ${cssRgb(Tokens.Color.surface)}")
            property("paint-order", "stroke fill")
            property("outline", if (focusRing) "${ring}px solid ${cssRgb(Tokens.Color.onSurface)}" else "none")
            property("outline-offset", "${ring}px")
            property("border-radius", "${Tokens.Space.grid}px")
        }.toAttrs {
            attr("type", "button")
            attr("aria-label", text)
            onClick { onClick() }
            // Ring only for keyboard focus, not for a mouse click.
            onFocus { focusRing = (it.nativeEvent.target as? Element)?.matches(":focus-visible") == true }
            onBlur { focusRing = false }
        }
    ) {
        Text(text)
    }
}
