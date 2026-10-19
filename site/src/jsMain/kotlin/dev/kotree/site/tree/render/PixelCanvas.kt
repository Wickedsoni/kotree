package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.CameraState
import dev.kotree.site.tree.camera.snappedScale
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.roundToInt

/** `#rrggbb` string for a token rgb value. */
fun cssRgb(rgb: Int): String = "#" + rgb.toString(16).padStart(6, '0')

/**
 * Blits cached tiles to one `<canvas>` with whole-number scaling only (docs/12 §8).
 * Backing size = CSS size x devicePixelRatio; smoothing off; the element also needs CSS `image-rendering: pixelated`
 * (see [applyPixelatedStyle]). Call [draw] only while the camera moves (no idle frames).
 */
class PixelCanvas(private val canvas: HTMLCanvasElement, private val cache: TileCache) {

    /** [stepFor] returns the split step 0..8 for a bough id. */
    fun draw(cam: CameraState, bounds: Bounds, stepFor: (String) -> Int, boughIds: List<String>) {
        val dpr = window.devicePixelRatio
        val bw = (cam.viewportPx.widthPx * dpr).roundToInt()
        val bh = (cam.viewportPx.heightPx * dpr).roundToInt()
        if (canvas.width != bw) canvas.width = bw
        if (canvas.height != bh) canvas.height = bh

        val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
        ctx.imageSmoothingEnabled = false
        ctx.fillStyle = cssRgb(Tokens.Color.surface)
        ctx.fillRect(0.0, 0.0, bw.toDouble(), bh.toDouble())

        // Whole device pixels per art pixel (docs/12 §8), and a whole-pixel origin, so every edge is crisp.
        val unit = snappedScale(bounds, cam.viewportPx, cam.z) * dpr
        val s = (unit * Tokens.World.px).roundToInt().toDouble()
        val originX = (bw / 2.0 - cam.centre.x * unit).roundToInt().toDouble()
        val originY = (bh / 2.0 - cam.centre.y * unit).roundToInt().toDouble()

        fun blit(tile: Tile?) {
            if (tile == null) return
            val dx = (originX + tile.x0 * s).roundToInt()
            val dy = (originY + tile.y0 * s).roundToInt()
            val dw = (originX + (tile.x0 + tile.w) * s).roundToInt() - dx
            val dh = (originY + (tile.y0 + tile.h) * s).roundToInt() - dy
            if (dx > bw || dy > bh || dx + dw < 0 || dy + dh < 0) return // off screen
            ctx.drawImage(tile.canvas, 0.0, 0.0, tile.w.toDouble(), tile.h.toDouble(),
                dx.toDouble(), dy.toDouble(), dw.toDouble(), dh.toDouble())
        }

        blit(cache.roots())
        blit(cache.trunk())
        for (id in boughIds) blit(cache.bough(id, stepFor(id)))
    }

    companion object {
        /** Sets the CSS that keeps the browser from smoothing the scaled canvas. */
        fun applyPixelatedStyle(canvas: HTMLCanvasElement) {
            canvas.style.setProperty("image-rendering", "pixelated")
        }
    }
}
