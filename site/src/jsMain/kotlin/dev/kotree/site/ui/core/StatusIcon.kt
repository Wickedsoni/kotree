package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorStatusLiveVar
import dev.kotree.site.colorStatusUpnextVar
import dev.kotree.site.shapeFullVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

/** rgb text of a colour typed in Tokens.kt, for the world colours that have no CSS variable. */
internal fun rgbString(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

/**
 * Status glyph (ADR 007): a 12 px mark in a 24 px box with the same shape cue as the tree. Live = filled
 * `status.live`; up next = `status.upnext.glow` fill with a `status.upnext` ring; coming soon = `leaf.shadow.stroke`
 * outline. `aria-hidden`: the row text or the chip carries the meaning.
 */
@Composable
fun StatusIcon(status: LeafStatus, modifier: Modifier = Modifier) {
    val ring = "${Tokens.Size.statusRingPx}px solid"
    Div(
        modifier
            .attrsModifier { attr("aria-hidden", "true") }
            .styleModifier {
                property("display", "inline-flex")
                property("align-items", "center")
                property("justify-content", "center")
                property("flex-shrink", "0")
                property("width", Tokens.Size.statusIconBoxPx.px)
                property("height", Tokens.Size.statusIconBoxPx.px)
            }
            .toAttrs()
    ) {
        Div(
            Modifier.styleModifier {
                property("box-sizing", "border-box")
                property("width", Tokens.Size.statusIconPx.px)
                property("height", Tokens.Size.statusIconPx.px)
                property("border-radius", shapeFullVar.value())
                when (status) {
                    LeafStatus.Live -> property("background-color", colorStatusLiveVar.value())
                    LeafStatus.UpNext -> {
                        property("background-color", rgbString(Tokens.Status.upnextGlow))
                        property("border", "$ring ${colorStatusUpnextVar.value()}")
                    }
                    LeafStatus.Shadow -> {
                        property("background-color", "transparent")
                        property("border", "$ring ${rgbString(Tokens.Status.leafShadowStroke)}")
                    }
                }
            }.toAttrs()
        )
    }
}
