package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.Vec
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A rasterised group of limbs: one canvas pixel per art pixel (Tokens.World.px generator units),
 * origin at ([x0], [y0]) in art pixels.
 * It is scaled only by integer factors when blitted (docs/07 §7).
 */
class Tile(val x0: Int, val y0: Int, val w: Int, val h: Int, val canvas: HTMLCanvasElement)

/** Turns polylines into pixel tiles: filled stroke of `widthPx` (generator units) plus a 1 world-px outline (docs/02 §5 rules 1-2, 8). */
object LimbRaster {
    private const val OUTLINE_PX = 1
    /** Art pixels per generator unit. */
    private const val K = 1.0 / Tokens.World.px
    private const val SAMPLES_PER_PX = 2.0
    private const val ALPHA_OPAQUE = 0xFF

    /** A filled square centred on ([cx], [cy]) in generator units, edge [sizeUnits], with a 1 px outline (leaf pixels). */
    class Pip(val cx: Double, val cy: Double, val sizeUnits: Double, val rgb: Int)

    /**
     * Rasterises [lines] in order (later lines paint over earlier fills), then [pips] on top.
     * Returns null when there is nothing to draw.
     */
    fun rasterise(lines: List<Polyline>, pips: List<Pip> = emptyList()): Tile? {
        val drawable = lines.filter { it.points.isNotEmpty() }
        if (drawable.isEmpty() && pips.isEmpty()) return null

        var minX = Double.MAX_VALUE
        var minY = Double.MAX_VALUE
        var maxX = -Double.MAX_VALUE
        var maxY = -Double.MAX_VALUE
        var pad = 0
        for (pip in pips) {
            val half = pip.sizeUnits * K / 2
            pad = max(pad, ceil(half).toInt() + OUTLINE_PX + 1)
            minX = min(minX, pip.cx * K); maxX = max(maxX, pip.cx * K)
            minY = min(minY, pip.cy * K); maxY = max(maxY, pip.cy * K)
        }
        for (line in drawable) {
            pad = max(pad, ceil(line.maxWidthPx * K / 2).toInt() + OUTLINE_PX + 1)
            for (p in line.points) {
                minX = min(minX, p.x * K); maxX = max(maxX, p.x * K)
                minY = min(minY, p.y * K); maxY = max(maxY, p.y * K)
            }
        }
        val x0 = floor(minX).toInt() - pad
        val y0 = floor(minY).toInt() - pad
        val w = ceil(maxX).toInt() + pad - x0 + 1
        val h = ceil(maxY).toInt() + pad - y0 + 1

        val rgb = IntArray(w * h)       // painted colour, valid where painted[i]
        val painted = BooleanArray(w * h)

        for (line in drawable) {
            val fill = BooleanArray(w * h)
            stamp(line, fill, x0, y0, w, h)
            val fillRgb = Tokens.Color.inkRgb(line.ink)
            val outlineRgb = if (line.ink == Tokens.Color.inkBark) Tokens.Color.bark else Tokens.Color.outline
            for (i in fill.indices) {
                if (fill[i]) {
                    rgb[i] = fillRgb
                    painted[i] = true
                }
            }
            if (outlineRgb == fillRgb) continue // same colour: an outline would only fatten the limb
            // Outline only on pixels that are still empty, so a grey limb never outlines over black bark.
            for (y in 0 until h) for (x in 0 until w) {
                val i = y * w + x
                if (fill[i] || painted[i]) continue
                val touches = (x > 0 && fill[i - 1]) || (x < w - 1 && fill[i + 1]) ||
                    (y > 0 && fill[i - w]) || (y < h - 1 && fill[i + w])
                if (touches) {
                    rgb[i] = outlineRgb
                    painted[i] = true
                }
            }
        }
        if (pips.isNotEmpty()) paintPips(pips, rgb, painted, x0, y0, w, h)
        return toTile(rgb, painted, x0, y0, w, h)
    }

    private fun paintPips(pips: List<Pip>, rgb: IntArray, painted: BooleanArray, x0: Int, y0: Int, w: Int, h: Int) {
        val fill = BooleanArray(w * h)
        for (pip in pips) {
            val edge = max(1, (pip.sizeUnits * K).roundToInt())
            val left = (pip.cx * K - edge / 2.0).roundToInt() - x0
            val top = (pip.cy * K - edge / 2.0).roundToInt() - y0
            for (y in max(0, top) until min(h, top + edge)) for (x in max(0, left) until min(w, left + edge)) {
                val i = y * w + x
                fill[i] = true
                rgb[i] = pip.rgb
                painted[i] = true
            }
        }
        // Outline on pixels no pip filled; it may overwrite a twig pixel, never a leaf.
        for (y in 0 until h) for (x in 0 until w) {
            val i = y * w + x
            if (fill[i]) continue
            val touches = (x > 0 && fill[i - 1]) || (x < w - 1 && fill[i + 1]) ||
                (y > 0 && fill[i - w]) || (y < h - 1 && fill[i + w])
            if (touches) {
                rgb[i] = Tokens.Color.outline
                painted[i] = true
            }
        }
    }

    private fun stamp(line: Polyline, fill: BooleanArray, x0: Int, y0: Int, w: Int, h: Int) {
        val pts = line.points.map { Vec(it.x * K, it.y * K) }
        val r0 = max(line.widthPx * K / 2.0, 0.5)
        val r1 = max(line.tipWidthPx * K / 2.0, 0.5)
        if (pts.size == 1) {
            disc(fill, pts[0].x - x0, pts[0].y - y0, r0, w, h)
            return
        }
        // Radius tapers with arc length from r0 at the first point to r1 at the last.
        val total = (0 until pts.size - 1).sumOf { hypot(pts[it + 1].x - pts[it].x, pts[it + 1].y - pts[it].y) }
        var done = 0.0
        for (s in 0 until pts.size - 1) {
            val a = pts[s]
            val b = pts[s + 1]
            val len = hypot(b.x - a.x, b.y - a.y)
            val n = ceil(len * SAMPLES_PER_PX).toInt().coerceAtLeast(1)
            for (k in 0..n) {
                val t = k.toDouble() / n
                val f = if (total > 0.0) (done + len * t) / total else 0.0
                disc(fill, a.x + (b.x - a.x) * t - x0, a.y + (b.y - a.y) * t - y0, r0 + (r1 - r0) * f, w, h)
            }
            done += len
        }
    }

    /** Fills every pixel whose centre lies inside the disc. (cx, cy) are in tile pixels. */
    private fun disc(fill: BooleanArray, cx: Double, cy: Double, r: Double, w: Int, h: Int) {
        val xa = max(0, floor(cx - r).toInt())
        val xb = min(w - 1, ceil(cx + r).toInt())
        val ya = max(0, floor(cy - r).toInt())
        val yb = min(h - 1, ceil(cy + r).toInt())
        val r2 = r * r
        for (y in ya..yb) for (x in xa..xb) {
            val dx = x + 0.5 - cx
            val dy = y + 0.5 - cy
            if (dx * dx + dy * dy < r2) fill[y * w + x] = true
        }
    }

    private fun toTile(rgb: IntArray, painted: BooleanArray, x0: Int, y0: Int, w: Int, h: Int): Tile {
        val canvas = document.createElement("canvas") as HTMLCanvasElement
        canvas.width = w
        canvas.height = h
        val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
        val image = ctx.createImageData(w.toDouble(), h.toDouble())
        // Uint8ClampedArray clamps, so write plain ints (a Byte of 255 would arrive as -1 and clamp to 0).
        val data = image.data.asDynamic()
        for (i in painted.indices) {
            if (!painted[i]) continue
            val c = rgb[i]
            val o = i * 4
            data[o] = (c shr 16) and 0xFF
            data[o + 1] = (c shr 8) and 0xFF
            data[o + 2] = c and 0xFF
            data[o + 3] = ALPHA_OPAQUE
        }
        ctx.putImageData(image, 0.0, 0.0)
        return Tile(x0, y0, w, h, canvas)
    }
}
