package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorScrimVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.layoutDialogWidthVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeLgIncreasedVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.camera.prefersReducedMotion
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.ButtonKind
import kotlinx.browser.document
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.json

private const val SHEET_TITLE_ID = "side-sheet-title"
private const val MS_PER_SECOND = 1000.0
private const val FOCUSABLE = "a[href], button:not([disabled]), input, select, textarea, [tabindex]:not([tabindex='-1'])"

private val moveSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}
private val fadeSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.effects.dampingRatio, Tokens.Spring.effects.stiffness).cssLinear()
}

/** Holds the panel element so the key handler can find the focusable controls inside it. */
private class PanelHolder {
    var element: HTMLElement? = null
}

/**
 * Keeps Tab inside the panel: from the last control it wraps to the first, and Shift+Tab from the first (or from the
 * title, which comes before every control) wraps to the last. In between the browser moves focus by itself.
 */
private fun trapTab(panel: HTMLElement?, e: KeyboardEvent) {
    if (panel == null) return
    val nodes = panel.querySelectorAll(FOCUSABLE)
    val count = nodes.length
    if (count == 0) {
        e.preventDefault()
        return
    }
    val active = document.activeElement
    var index = -1
    for (i in 0 until count) {
        if (nodes.item(i) == active) index = i
    }
    if (e.shiftKey) {
        if (index <= 0) {
            e.preventDefault()
            (nodes.item(count - 1) as? HTMLElement)?.focus()
        }
    } else if (index == count - 1) {
        e.preventDefault()
        (nodes.item(0) as? HTMLElement)?.focus()
    }
}

/**
 * Modal side sheet (ADR 007): a panel at the right, layout.dialog.width wide and full height, over a scrim
 * (color.scrim) at z.sheet. Enter: slide with motion.spatial and fade with motion.effects; a plain cut under
 * prefers-reduced-motion. Focus moves to the title and stays inside (Tab wraps); Escape or a click on the scrim calls
 * [onClose]; when the sheet leaves the page, focus goes back to the element that had it when the sheet opened.
 * The body scrolls inside the global pill scrollbar. [branch] is a branch slug for the tint, ink and focus ring.
 */
@Composable
fun SideSheet(
    title: String,
    onClose: () -> Unit,
    eyebrow: String? = null,
    branch: String = "kotlin",
    footer: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    // The element that had focus when the sheet opened; read before the sheet takes focus.
    val opener = remember { document.activeElement as? HTMLElement }
    val holder = remember { PanelHolder() }
    DisposableEffect(Unit) {
        onDispose {
            if (opener != null && opener.isConnected && opener != document.body) opener.focus()
        }
    }
    val ink = branchInk(branch).value().toString()
    Div(
        Modifier
            .inBranch(branch)
            .styleModifier {
                property("position", "fixed")
                property("inset", "0")
                property("z-index", Tokens.Z.sheet.toString())
                property("display", "flex")
                property("justify-content", "flex-end")
                property("background-color", colorScrimVar.value())
            }
            .toAttrs {
                onClick { event ->
                    if (event.nativeEvent.target == event.nativeEvent.currentTarget) onClose()
                }
            }
    ) {
        Div(
            Modifier
                .attrsModifier {
                    attr("role", "dialog")
                    attr("aria-modal", "true")
                    attr("aria-labelledby", SHEET_TITLE_ID)
                }
                .styleModifier {
                    property("display", "flex")
                    property("flex-direction", "column")
                    property("box-sizing", "border-box")
                    property("width", layoutDialogWidthVar.value())
                    property("max-width", "100%")
                    property("height", "100%")
                    property("overflow", "hidden")
                    property("background-color", colorSurfaceVar.value())
                    property("border-radius", "${shapeLgIncreasedVar.value()} 0 0 ${shapeLgIncreasedVar.value()}")
                    property("box-shadow", elevation2Var.value())
                    property("color", colorOnSurfaceVar.value())
                    property("font-family", fontUiVar.value())
                    property("font-size", typeBodySizeVar.value())
                    property("line-height", typeBodyLineVar.value())
                }
                .toAttrs {
                    ref { element ->
                        holder.element = element
                        if (!prefersReducedMotion()) {
                            val (moveSeconds, moveEasing) = moveSpring
                            element.asDynamic().animate(
                                arrayOf(
                                    json("transform" to "translateX(${Tokens.Spacing.s6}px)"),
                                    json("transform" to "translateX(0)"),
                                ),
                                json("duration" to moveSeconds * MS_PER_SECOND, "easing" to moveEasing),
                            )
                            val (fadeSeconds, fadeEasing) = fadeSpring
                            element.asDynamic().animate(
                                arrayOf(json("opacity" to 0), json("opacity" to 1)),
                                json("duration" to fadeSeconds * MS_PER_SECOND, "easing" to fadeEasing),
                            )
                        }
                        onDispose { holder.element = null }
                    }
                    onKeyDown { event ->
                        if (event.key == "Escape") {
                            event.nativeEvent.stopPropagation()
                            event.preventDefault()
                            onClose()
                        } else if (event.key == "Tab") {
                            trapTab(holder.element, event.nativeEvent.unsafeCast<KeyboardEvent>())
                        }
                    }
                }
        ) {
            Div(
                Modifier.styleModifier {
                    property("display", "grid")
                    property("gap", space2Var.value())
                    property("padding", "${space4Var.value()} ${space6Var.value()}")
                    property("background-color", branchTint(branch).value().toString())
                }.toAttrs()
            ) {
                if (eyebrow != null) {
                    Span(
                        Modifier.styleModifier {
                            property("color", ink)
                            property("font-size", typeLabelSizeVar.value())
                            property("line-height", typeLabelLineVar.value())
                            property("font-weight", typeLabelWeightVar.value())
                        }.toAttrs()
                    ) { Text(eyebrow) }
                }
                H2(
                    Modifier.styleModifier {
                        property("margin", "0")
                        property("color", ink)
                        property("font-size", typeTitleSizeVar.value())
                        property("line-height", typeTitleLineVar.value())
                        property("font-weight", typeTitleWeightVar.value())
                    }.toAttrs {
                        attr("id", SHEET_TITLE_ID)
                        attr("tabindex", "-1")
                        ref { element ->
                            element.focus()
                            onDispose { }
                        }
                    }
                ) { Text(title) }
            }
            Div(
                Modifier.styleModifier {
                    property("flex", "1 1 auto")
                    property("min-height", "0")
                    property("overflow-y", "auto")
                    property("padding", "${space4Var.value()} ${space6Var.value()}")
                }.toAttrs()
            ) { content() }
            Div(
                Modifier.styleModifier {
                    property("display", "flex")
                    property("flex-wrap", "wrap")
                    property("justify-content", "flex-end")
                    property("gap", space2Var.value())
                    property("padding", "${space3Var.value()} ${space6Var.value()}")
                    property("border-top", elevation1Var.value())
                }.toAttrs()
            ) { footer() }
        }
    }
}

private const val LINK_BUTTON_BRANCH = "kotlin"

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
    branch: String = LINK_BUTTON_BRANCH,
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
