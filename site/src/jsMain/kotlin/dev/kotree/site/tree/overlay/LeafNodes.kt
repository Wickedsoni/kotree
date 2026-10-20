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
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.camera.ZoomLevel
import dev.kotree.site.tree.camera.prefersReducedMotion
import dev.kotree.site.tree.camera.snappedScale
import dev.kotree.site.tree.camera.toScreen
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.TreeData
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import kotlin.js.json

/** `rgba()` from a token's rgb int and a percent alpha. */
private fun cssRgba(rgb: Int, alphaPercent: Int): String =
    "rgba(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF}, ${alphaPercent / PERCENT})"

private const val PERCENT = 100.0

/**
 * Clickable nodes for the focused branch's live and up-next leaves, from the Branch level, culled to the viewport
 * (docs/14 §4.1). Shadow leaves get nothing (D3). An up-next leaf carries a DOM glow ring in `leaf.upnext.glow`,
 * static under `prefers-reduced-motion`. At the Leaf level the title sits next to the leaf.
 * Place inside the same `position: relative` camera-sized element as [TreeLabels].
 */
@Composable
fun LeafNodes(
    data: TreeData,
    geometry: TreeGeometry,
    camera: CameraState,
    view: ViewState,
    onLeaf: (Leaf) -> Unit,
) {
    val branchId = view.branchId
    if (view.level < ZoomLevel.Z2 || branchId == null) return
    val branch = geometry.boughs.flatMap { it.branches }.firstOrNull { it.id == branchId } ?: return
    val leafData = remember(data, branchId) {
        data.boughs.flatMap { it.branches }.firstOrNull { it.id == branchId }
            ?.stems.orEmpty().flatMap { it.leaves }.associateBy { it.id }
    }
    val scale = snappedScale(geometry.bounds, camera.viewportPx, camera.z)
    val leafPx = Tokens.Gen.LEAF_SIZE * scale
    val reach = maxOf(leafPx, Tokens.Space.hitAreaPx.toDouble())
    val vw = camera.viewportPx.widthPx
    val vh = camera.viewportPx.heightPx

    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("inset", "0")
            property("pointer-events", "none")
            property("overflow", "clip")
        }.toAttrs()
    ) {
        for (stem in branch.stems) for (slot in stem.leaves) {
            val leaf = leafData[slot.id] ?: continue
            if (leaf.status == LeafStatus.SHADOW) continue
            val at = camera.toScreen(geometry.bounds, slot.at)
            if (at.x < -reach || at.y < -reach || at.x > vw + reach || at.y > vh + reach) continue
            LeafNode(leaf, at.x, at.y, leafPx, reach, showTitle = view.level >= ZoomLevel.Z4) { onLeaf(leaf) }
        }
    }
}

@Composable
private fun LeafNode(leaf: Leaf, x: Double, y: Double, leafPx: Double, reach: Double, showTitle: Boolean, onClick: () -> Unit) {
    var focusRing by remember { mutableStateOf(false) }
    val upNext = leaf.status == LeafStatus.UPNEXT
    val ring = Tokens.Space.grid / 2
    if (upNext) GlowRing(x, y, leafPx)
    Button(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${x}px")
            property("top", "${y}px")
            property("transform", "translate(-50%, -50%)")
            property("width", "${reach}px")
            property("height", "${reach}px")
            property("padding", "0")
            property("border", "0")
            property("background", "transparent")
            property("cursor", "pointer")
            property("pointer-events", "auto")
            property("border-radius", "${Tokens.Shape.full}px")
            property("outline", if (focusRing) "${ring}px solid ${cssRgb(Tokens.Color.onSurface)}" else "none")
            property("outline-offset", "${ring}px")
        }.toAttrs {
            attr("type", "button")
            attr("aria-label", if (upNext) "${leaf.title}, coming next" else leaf.title)
            onClick { onClick() }
            onFocus { focusRing = (it.nativeEvent.target as? Element)?.matches(":focus-visible") == true }
            onBlur { focusRing = false }
        }
    ) {}
    if (showTitle) {
        Span(
            Modifier.styleModifier {
                property("position", "absolute")
                property("left", "${x + reach / 2 + Tokens.Space.grid}px")
                property("top", "${y}px")
                property("transform", "translate(0, -50%)")
                property("white-space", "nowrap")
                property("pointer-events", "none")
                property("color", cssRgb(Tokens.Color.onSurface))
                property("font-family", Tokens.Type.fontUi.joinToString(", "))
                property("font-size", "${Tokens.Type.labelSizePx}px")
                property("line-height", "${Tokens.Type.labelLineHeightPx}px")
                property("font-weight", "${Tokens.Type.labelWeight}")
                property("-webkit-text-stroke", "${Tokens.Space.labelOutlinePx * 2}px ${cssRgb(Tokens.Color.surface)}")
                property("paint-order", "stroke fill")
            }.toAttrs { attr("aria-hidden", "true") }
        ) { Text(leaf.title) }
    }
}

/** Decorative glow around an up-next leaf: a ring in the glow token, pulsing unless reduced motion is on. */
@Composable
private fun GlowRing(x: Double, y: Double, leafPx: Double) {
    val size = leafPx + Tokens.Space.grid * 2
    val glow = cssRgba(Tokens.Leaf.upnextGlow, Tokens.Leaf.upnextGlowAlphaPercent)
    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${x}px")
            property("top", "${y}px")
            property("width", "${size}px")
            property("height", "${size}px")
            property("transform", "translate(-50%, -50%)")
            property("box-sizing", "border-box")
            property("border-radius", "${Tokens.Shape.full}px")
            property("border", "${Tokens.Space.grid / 2}px solid $glow")
            property("box-shadow", "0 0 ${Tokens.Space.grid * 2}px ${Tokens.Space.grid / 2}px $glow")
            property("pointer-events", "none")
        }.toAttrs {
            attr("aria-hidden", "true")
            if (!prefersReducedMotion()) {
                ref { el ->
                    val from = "0 0 ${Tokens.Space.grid}px 0 $glow"
                    val to = "0 0 ${Tokens.Space.grid * 3}px ${Tokens.Space.grid}px $glow"
                    val anim = el.asDynamic().animate(
                        arrayOf(json("boxShadow" to from), json("boxShadow" to to)),
                        json(
                            "duration" to Tokens.Motion.glowPulseMs,
                            "iterations" to Double.POSITIVE_INFINITY,
                            "direction" to "alternate",
                            "easing" to "ease-in-out",
                        ),
                    )
                    onDispose { anim.cancel() }
                }
            }
        }
    ) {}
}
