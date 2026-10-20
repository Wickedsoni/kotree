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
import dev.kotree.site.tree.StemGeo
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.camera.ZoomLevel
import dev.kotree.site.tree.camera.toScreen
import dev.kotree.site.tree.model.TreeData
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

private const val HINT_ID = "kotree-stem-hint"

/**
 * Stem titles of the focused branch only, from the Branch level (docs/14 §4.1). Every stem is a real button in
 * data order, so Tab reaches all of them. Hover or focus shows `Coming soon: <why>` below the label.
 * Place inside the same `position: relative` camera-sized element as [TreeLabels].
 */
@Composable
fun StemLabels(
    data: TreeData,
    geometry: TreeGeometry,
    camera: CameraState,
    view: ViewState,
    onStem: (StemGeo) -> Unit,
) {
    val branchId = view.branchId
    if (view.level < ZoomLevel.Z2 || branchId == null) return
    val branch = geometry.boughs.flatMap { it.branches }.firstOrNull { it.id == branchId } ?: return
    val stemData = remember(data, branchId) {
        data.boughs.flatMap { it.branches }.firstOrNull { it.id == branchId }?.stems.orEmpty().associateBy { it.id }
    }
    val stems = branch.stems.filter { it.id in stemData }
    if (stems.isEmpty()) return
    var hinted by remember(branchId) { mutableStateOf<String?>(null) }

    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("inset", "0")
            property("pointer-events", "none")
            property("overflow", "clip")
        }.toAttrs()
    ) {
        val specs = stems.map { geo ->
            LabelSpec(
                text = stemData.getValue(geo.id).title,
                at = camera.toScreen(geometry.bounds, geo.labelAnchor),
                opacity = 1.0,
                colour = Tokens.Color.leafShadowLabel,
                onClick = { onStem(geo) },
            )
        }
        val placed = nudgeApart(specs, camera.viewportPx.widthPx, camera.viewportPx.heightPx)
        for ((i, geo) in stems.withIndex()) {
            val stem = stemData.getValue(geo.id)
            val comingSoon = stem.counts.live < stem.counts.total
            val spec = specs[i]
            LabelButton(
                text = spec.text,
                at = placed[i],
                opacity = spec.opacity,
                colour = spec.colour,
                onClick = spec.onClick,
                ariaLabel = if (comingSoon) "${stem.title}, coming soon" else stem.title,
                describedBy = if (hinted == geo.id) HINT_ID else null,
                onHint = { on -> if (on) hinted = geo.id else if (hinted == geo.id) hinted = null },
            )
        }
        val hintIndex = stems.indexOfFirst { it.id == hinted }
        if (hintIndex >= 0) {
            val stem = stemData.getValue(stems[hintIndex].id)
            val text = if (stem.counts.live < stem.counts.total) "Coming soon: ${stem.why}" else stem.why
            Hint(text, placed[hintIndex])
        }
    }
}

@Composable
private fun Hint(text: String, at: Vec) {
    Div(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${at.x}px")
            property("top", "${at.y + Tokens.Space.hitAreaPx / 2 + Tokens.Space.grid}px")
            property("transform", "translate(-50%, 0)")
            property("max-width", "${Tokens.Spacing.s16 * 5}px")
            property("padding", "${Tokens.Spacing.s1}px ${Tokens.Spacing.s2}px")
            property("background", cssRgb(Tokens.Color.surface))
            property("color", cssRgb(Tokens.Color.onSurface))
            property("border", "${Tokens.Space.hairlinePx}px solid ${cssRgb(Tokens.Color.outline)}")
            property("border-radius", "${Tokens.Shape.xs}px")
            property("font-family", Tokens.Type.fontUi.joinToString(", "))
            property("font-size", "${Tokens.Type.labelSizePx}px")
            property("line-height", "${Tokens.Type.labelLineHeightPx}px")
            property("pointer-events", "none")
        }.toAttrs {
            id(HINT_ID)
            attr("role", "tooltip")
        }
    ) {
        Text(text)
    }
}
