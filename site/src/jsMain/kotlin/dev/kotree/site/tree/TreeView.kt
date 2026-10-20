package dev.kotree.site.tree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.space6Var
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.camera.Fly
import dev.kotree.site.tree.camera.ViewTracker
import dev.kotree.site.tree.camera.attachCameraInput
import dev.kotree.site.tree.gen.generate
import dev.kotree.site.tree.gen.geometryHash
import dev.kotree.site.tree.gen.layoutReport
import dev.kotree.site.tree.model.Difficulty
import dev.kotree.site.tree.model.Leaf
import dev.kotree.site.tree.model.TreeData
import dev.kotree.site.tree.overlay.LeafNodes
import dev.kotree.site.tree.overlay.StemLabels
import dev.kotree.site.tree.overlay.TreeLabels
import dev.kotree.site.tree.overlay.cssRgb
import dev.kotree.site.tree.overlay.splitProgress
import dev.kotree.site.tree.render.PixelCanvas
import dev.kotree.site.tree.render.TileCache
import dev.kotree.site.ui.core.Difficulty as UiDifficulty
import dev.kotree.site.ui.layout.LeafPopup
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.math.roundToInt

private const val REPORT_WIDTH_PX = 1280
private const val REPORT_HEIGHT_PX = 800
private const val HOME_TEXT = "Home"
private const val UP_NEXT_SUMMARY = "Coming next. The author is learning this now."
private const val VIEW_LABEL = "Kotree. Drag to pan, scroll or plus and minus to zoom, Home to fit the tree."

/**
 * The whole tree, full size of its parent: generator, renderer, camera input, fly and labels (docs/12 §5).
 * Frames are drawn only when the camera changes (docs/12 §8).
 */
@Composable
fun TreeView(data: TreeData) {
    val geometry = remember(data) {
        val t0 = window.performance.now()
        generate(data, data.seed).also {
            console.log("generate + treeData init ms", (window.performance.now() - t0).roundToInt(), "(plan <= 50)")
            console.log("geometry hash", geometryHash(it))
            // Check at the zoom each branch label really flies to (docs/14 §5), on a 1280 x 800 viewport.
            val probe = Camera(it.bounds).also { c -> c.setViewport(Viewport(REPORT_WIDTH_PX, REPORT_HEIGHT_PX)) }
            layoutReport(it, data, Viewport(REPORT_WIDTH_PX, REPORT_HEIGHT_PX)) { b -> probe.branchTarget(b, upNextIds(data)[b.id]).z }
                .forEach { line -> console.log(line) }
        }
    }
    val camera = remember(geometry) { Camera(geometry.bounds) }
    val fly = remember(camera) { Fly(camera) }
    val tracker = remember(geometry) { ViewTracker(geometry) }
    // Not read in this scope: reading it here would recompose the container and re-apply its attributes,
    // which drops keyboard focus. Only [Overlay] reads it.
    val state = remember(camera) { mutableStateOf(camera.state) }
    var container: Element? = null
    var popupLeaf by remember(data) { mutableStateOf<Leaf?>(null) }

    Div(
        Modifier.styleModifier {
            property("position", "relative")
            property("width", "100%")
            property("height", "${Tokens.Layout.fullViewportHeightVh}vh")
            property("overflow", "hidden")
            property("touch-action", "none")
            property("user-select", "none")
            property("background", cssRgb(Tokens.Color.surface))
            property("outline", "none")
        }.toAttrs {
            attr("tabindex", "0")
            attr("role", "group")
            attr("aria-label", VIEW_LABEL)
            ref { el ->
                val canvas = document.createElement("canvas") as HTMLCanvasElement
                canvas.setAttribute("aria-hidden", "true")
                canvas.style.position = "absolute"
                canvas.style.setProperty("inset", "0")
                canvas.style.width = "100%"
                canvas.style.height = "100%"
                PixelCanvas.applyPixelatedStyle(canvas)
                el.insertBefore(canvas, el.firstChild)
                container = el
                // Focus ring is an overlay element toggled imperatively (a state read here would recompose the
                // container and drop focus; a plain outline would be painted under the canvas).
                val ringPx = Tokens.Space.grid / 2
                val ring = document.createElement("div") as HTMLElement
                ring.style.setProperty("position", "absolute")
                ring.style.setProperty("inset", "0")
                ring.style.setProperty("box-sizing", "border-box")
                ring.style.setProperty("border", "${ringPx}px solid ${cssRgb(Tokens.Color.onSurface)}")
                ring.style.setProperty("pointer-events", "none")
                ring.style.setProperty("display", "none")
                ring.setAttribute("aria-hidden", "true")
                el.appendChild(ring)
                val onFocusIn: (org.w3c.dom.events.Event) -> Unit = {
                    if (el.matches(":focus-visible")) ring.style.setProperty("display", "block")
                }
                val onFocusOut: (org.w3c.dom.events.Event) -> Unit = { ring.style.setProperty("display", "none") }
                el.addEventListener("focus", onFocusIn)
                el.addEventListener("blur", onFocusOut)

                val painter = PixelCanvas(canvas, TileCache(geometry, data))
                val ids = geometry.boughs.map { it.id }
                var pending = false
                fun paint() {
                    pending = false
                    val cam = camera.state
                    val step = (splitProgress(cam.z) * Tokens.Zoom.splitSteps).roundToInt()
                    painter.draw(cam, geometry.bounds, { step }, ids)
                }
                camera.onChange = {
                    state.value = camera.state
                    el.setAttribute("data-z", (camera.z.asDynamic().toFixed(2) as String))
                    if (!pending) {
                        pending = true
                        window.requestAnimationFrame { paint() }
                    }
                }
                val remove = attachCameraInput(el, camera, fly)
                camera.homeTarget().let { camera.set(it.z, it.centre) } // start on a whole-pixel fit
                camera.onChange?.invoke()
                onDispose {
                    remove()
                    el.removeEventListener("focus", onFocusIn)
                    el.removeEventListener("blur", onFocusOut)
                    container = null
                    camera.onChange = null
                    canvas.remove()
                }
            }
        }
    ) {
        // Visually hidden page heading (screen readers and document outline).
        H1(Modifier.styleModifier {
            property("position", "absolute")
            property("width", "${Tokens.Space.hairlinePx}px")
            property("height", "${Tokens.Space.hairlinePx}px")
            property("margin", "-${Tokens.Space.hairlinePx}px")
            property("padding", "0")
            property("overflow", "hidden")
            property("clip", "rect(0, 0, 0, 0)")
            property("white-space", "nowrap")
            property("border", "0")
        }.toAttrs()) { Text("Kotree") }
        // Home first so it is first in tab order after the container.
        HomeButton { tracker.unpin(); fly.to(camera.homeTarget()) }
        Overlay(data, geometry, camera, fly, tracker, state, { popupLeaf = it }) {
            // The focused label was removed on a level switch: keep focus inside the view.
            val active = document.activeElement
            if (active == null || active == document.body) (container as? HTMLElement)?.focus()
        }
        popupLeaf?.let { UpNextPopup(it) { popupLeaf = null } }
    }
}

@Composable
private fun Overlay(
    data: TreeData, geometry: TreeGeometry, camera: Camera, fly: Fly, tracker: ViewTracker, state: State<CameraState>,
    onLeaf: (Leaf) -> Unit, onLevelSwitch: () -> Unit,
) {
    val view = tracker.update(state.value)
    val upNextId = remember(data) { upNextIds(data) }
    TreeLabels(
        data = data,
        geometry = geometry,
        camera = state.value,
        onBough = { tracker.unpin(); fly.to(camera.boughTarget(it)) },
        onBranch = { _, branch -> tracker.pin(branch.id); fly.to(camera.branchTarget(branch, upNextId[branch.id])) },
        onLevelSwitch = onLevelSwitch,
    )
    StemLabels(data, geometry, state.value, view) { tracker.pin(it.id.substringBefore('/')); fly.to(camera.stemTarget(it)) }
    LeafNodes(data, geometry, state.value, view, onLeaf)
}

/** Up-next popup (docs/03 §6.2): the shared [LeafPopup] without "Open lesson", centred over the view. */
@Composable
private fun UpNextPopup(leaf: Leaf, onClose: () -> Unit) {
    LeafPopup(
        branch = leaf.id.substringBefore('/'),
        title = leaf.title,
        summary = UP_NEXT_SUMMARY,
        difficulty = leaf.difficulty.toUi(),
        onOpenLesson = null,
        onClose = onClose,
        modifier = Modifier.styleModifier {
            property("position", "absolute")
            property("left", "50%")
            property("top", "50%")
            property("transform", "translate(-50%, -50%)")
            property("max-width", "calc(100% - ${space6Var.value()} * 2)")
        },
    )
}

/** Branch id to the full id of its up-next leaf (absent when every leaf is live). */
private fun upNextIds(data: TreeData): Map<String, String> =
    data.boughs.flatMap { it.branches }.mapNotNull { b -> b.upNext?.let { b.id to it } }.toMap()

private fun Difficulty.toUi(): UiDifficulty = when (this) {
    Difficulty.EASY -> UiDifficulty.Easy
    Difficulty.MEDIUM -> UiDifficulty.Medium
    Difficulty.HARD -> UiDifficulty.Hard
}

@Composable
private fun HomeButton(onClick: () -> Unit) {
    var focusRing by remember { mutableStateOf(false) }
    val ring = Tokens.Space.grid / 2
    Button(
        Modifier.styleModifier {
            property("position", "absolute")
            property("left", "${Tokens.Space.grid * 4}px")
            property("top", "${Tokens.Space.grid * 4}px")
            property("min-width", "${Tokens.Space.hitAreaPx * 2}px")
            property("min-height", "${Tokens.Space.hitAreaPx * 2}px")
            property("padding", "0 ${Tokens.Space.grid * 4}px")
            property("border", "${Tokens.Space.hairlinePx}px solid ${cssRgb(Tokens.Color.outline)}")
            property("border-radius", "${Tokens.Space.hitAreaPx * 2}px")
            property("background", cssRgb(Tokens.Color.surface))
            property("color", cssRgb(Tokens.Color.onSurface))
            property("cursor", "pointer")
            property("font-family", Tokens.Type.fontUi.joinToString(", "))
            property("font-size", "${Tokens.Type.labelSizePx}px")
            property("line-height", "${Tokens.Type.labelLineHeightPx}px")
            property("font-weight", "${Tokens.Type.labelWeight}")
            property("outline", if (focusRing) "${ring}px solid ${cssRgb(Tokens.Color.onSurface)}" else "none")
            property("outline-offset", "${ring}px")
        }.toAttrs {
            attr("type", "button")
            attr("aria-label", "Fit the whole tree")
            onClick { onClick() }
            onFocus { focusRing = (it.nativeEvent.target as? Element)?.matches(":focus-visible") == true }
            onBlur { focusRing = false }
        }
    ) {
        Text(HOME_TEXT)
    }
}
