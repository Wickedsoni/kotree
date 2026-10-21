package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.camera.Camera
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/** Paints the wood (limbs, stems, leaves) into any 2D context: the layer's own, or the visible canvas for direct drawing. */
interface WoodPainter {
    /**
     * [s] CSS px per world unit, [k] device px per CSS px of the target, ([ox], [oy]) the world point at the target's
     * top-left, ([ww], [wh]) the world size it covers, [step] the split step 0..8, [level] the ZoomLevel ordinal,
     * [branch] the focused branch index or -1, [leafBucket] the leaf opacity in 1/leafAlphaBuckets steps.
     */
    fun paintWood(g: dynamic, s: Double, k: Double, ox: Double, oy: Double, ww: Double, wh: Double, step: Int, level: Int, branch: Int, leafBucket: Int)
}

/**
 * Offscreen layer holding the wood of the view plus tree.layer.margin (ADR 006). While the scale stays within
 * tree.layer.rescale of the layer's, the view stays inside it and level, step and focused branch are unchanged, a frame is
 * one scaled drawImage. Otherwise, and tree.idle.crisp after motion stops, it is rendered again. [useLayer] = false
 * draws directly (the simplest-first fallback of the ADR).
 */
class WoodLayer(private val painter: WoodPainter) {
    var useLayer: Boolean = true

    private val canvas = document.createElement("canvas") as HTMLCanvasElement
    private val lg: dynamic = canvas.getContext("2d")
    private var valid = false
    private var x0 = 0.0
    private var y0 = 0.0
    private var ww = 0.0
    private var wh = 0.0
    private var s0 = 1.0
    private var rcx = 0.0
    private var rcy = 0.0
    private var keyStep = -1
    private var keyLevel = -1
    private var keyBranch = -2
    private var keyLeaf = -1

    fun invalidate() {
        valid = false
    }

    /** Draws the wood into [g], whose transform is the device pixel ratio scale (CSS px coordinates). */
    fun draw(g: dynamic, cam: Camera, s: Double, dpr: Double, step: Int, level: Int, branch: Int, leafBucket: Int, crisp: Boolean) {
        val vw = cam.viewport.widthPx.toDouble()
        val vh = cam.viewport.heightPx.toDouble()
        val worldW = vw / s
        val worldH = vh / s
        val left = cam.cx - worldW / 2.0
        val top = cam.cy - worldH / 2.0
        if (!useLayer) {
            painter.paintWood(g, s, dpr, left, top, worldW, worldH, step, level, branch, leafBucket)
            return
        }
        var reuse = valid && step == keyStep && level == keyLevel && branch == keyBranch && leafBucket == keyLeaf &&
            abs(s / s0 - 1.0) <= Tokens.Tree.layerRescale &&
            left >= x0 && top >= y0 && left + worldW <= x0 + ww && top + worldH <= y0 + wh
        // After motion stops the layer is made exact: same scale and centre as the view.
        if (reuse && crisp && !(s == s0 && cam.cx == rcx && cam.cy == rcy)) reuse = false
        if (!reuse) render(cam, s, dpr, step, level, branch, leafBucket, vw, vh)
        val dx = (x0 - cam.cx) * s + vw / 2.0
        val dy = (y0 - cam.cy) * s + vh / 2.0
        g.drawImage(canvas, 0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble(), dx, dy, ww * s, wh * s)
    }

    private fun render(cam: Camera, s: Double, dpr: Double, step: Int, level: Int, branch: Int, leafBucket: Int, vw: Double, vh: Double) {
        val margin = Tokens.Tree.layerMargin
        // Keep the layer under a pixel budget on very large screens by lowering its resolution.
        val areaCss = vw * (1.0 + 2.0 * margin) * vh * (1.0 + 2.0 * margin)
        var k = dpr
        if (areaCss * k * k > MAX_LAYER_AREA) k = sqrt(MAX_LAYER_AREA / areaCss)
        // The margin is a whole number of device pixels, so a layer drawn 1:1 lands on the pixel grid.
        val padX = floor(vw * margin * k) / k
        val padY = floor(vh * margin * k) / k
        val lwPx = ceil((vw + 2.0 * padX) * k).toInt()
        val lhPx = ceil((vh + 2.0 * padY) * k).toInt()
        canvas.width = lwPx
        canvas.height = lhPx
        ww = lwPx / (s * k)
        wh = lhPx / (s * k)
        x0 = cam.cx + (-padX - vw / 2.0) / s
        y0 = cam.cy + (-padY - vh / 2.0) / s
        s0 = s
        rcx = cam.cx
        rcy = cam.cy
        keyStep = step
        keyLevel = level
        keyBranch = branch
        keyLeaf = leafBucket
        valid = true
        painter.paintWood(lg, s, k, x0, y0, ww, wh, step, level, branch, leafBucket)
    }

    private companion object {
        /** Largest layer, in device pixels (about 64 MB of RGBA). */
        const val MAX_LAYER_AREA = 16_000_000.0
    }
}
