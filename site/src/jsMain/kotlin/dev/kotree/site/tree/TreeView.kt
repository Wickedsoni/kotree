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
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.camera.Fly
import dev.kotree.site.tree.camera.attachCameraInput
import dev.kotree.site.tree.gen.generate
import dev.kotree.site.tree.gen.geometryHash
import dev.kotree.site.tree.model.TreeData
import dev.kotree.site.tree.overlay.TreeLabels
import dev.kotree.site.tree.overlay.cssRgb
import dev.kotree.site.tree.overlay.splitProgress
import dev.kotree.site.tree.render.PixelCanvas
import dev.kotree.site.tree.render.TileCache
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.roundToInt

private const val HOME_TEXT = "Home"
private const val VIEW_LABEL = "Kotree. Drag to pan, scroll or plus and minus to zoom, Home to fit the tree."

/**
 * The whole tree, full size of its parent: generator, renderer, camera input, fly and labels (docs/12 §5).
 * Frames are drawn only when the camera changes (docs/12 §8).
 */
@Composable
fun TreeView(data: TreeData) {
    val geometry = remember(data) {
        generate(data, data.seed).also { console.log("geometry hash", geometryHash(it)) }
    }
    val camera = remember(geometry) { Camera(geometry.bounds) }
    val fly = remember(camera) { Fly(camera) }
    // Not read in this scope: reading it here would recompose the container and re-apply its attributes,
    // which drops keyboard focus. Only [Overlay] reads it.
    val state = remember(camera) { mutableStateOf(camera.state) }

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
            attr("role", "application")
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

                val painter = PixelCanvas(canvas, TileCache(geometry))
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
                camera.onChange?.invoke()
                onDispose {
                    remove()
                    camera.onChange = null
                    canvas.remove()
                }
            }
        }
    ) {
        Overlay(data, geometry, camera, fly, state)
        HomeButton { fly.to(camera.homeTarget()) }
    }
}

@Composable
private fun Overlay(data: TreeData, geometry: TreeGeometry, camera: Camera, fly: Fly, state: State<CameraState>) {
    TreeLabels(
        data = data,
        geometry = geometry,
        camera = state.value,
        onBough = { fly.to(camera.boughTarget(it)) },
        onBranch = { _, branch -> fly.to(camera.branchTarget(branch)) },
    )
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
