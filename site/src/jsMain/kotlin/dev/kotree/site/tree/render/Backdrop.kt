package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.gen.Rng
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sin

private const val PERCENT = 100.0
private const val TWO_PI = 2.0 * PI
private const val HALF = 0.5
private const val CLOUD_PUFFS = 3
private const val PUFF_SHIFT = 0.5
private const val PUFF_LIFT = 0.45

private fun newPath(): dynamic = js("new Path2D()")

private fun rgbaCss(rgb: Int, percent: Int): String =
    "rgba(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF}, ${percent / PERCENT})"

/**
 * Sky, clouds and ground behind the tree (ADR 008 §Backdrop, decision L35). The sky is a small offscreen canvas rebuilt
 * only when the viewport size changes; hills are one cached Path2D in world space. Nothing is allocated per frame.
 */
class Backdrop(geometry: TreeGeometry, private val seed: Long = Tokens.World.seed) {
    private val sky = document.createElement("canvas") as HTMLCanvasElement
    private var skyW = -1
    private var skyH = -1

    private val skyTopCss = cssColor(Tokens.Backdrop.skyTop)
    private val skyHorizonCss = cssColor(Tokens.Backdrop.skyHorizon)
    private val cloudCss = rgbaCss(Tokens.Backdrop.cloud, Tokens.Backdrop.cloudAlphaPercent)
    private val grainCss = rgbaCss(Tokens.Color.onSurface, Tokens.Backdrop.grainAlphaPercent)
    private val hillsCss = cssColor(Tokens.Backdrop.hills)
    private val grassCss = cssColor(Tokens.Backdrop.grass)
    private val earthCss = cssColor(Tokens.Backdrop.earth)

    private val horizonY: Double = geometry.trunk.points.firstOrNull()?.y ?: geometry.bounds.maxY
    private val grassBottom: Double = horizonY + Tokens.Backdrop.grassWorld
    private val hills: dynamic = buildHills(geometry)

    private fun buildHills(geometry: TreeGeometry): dynamic {
        val b = geometry.bounds
        val w = b.maxX - b.minX
        val x0 = b.minX - w
        val x1 = b.maxX + w
        val r = Rng(seed, "hills")
        val f1 = TWO_PI / r.range(Tokens.Backdrop.hillWave1Min, Tokens.Backdrop.hillWave1Max)
        val f2 = TWO_PI / r.range(Tokens.Backdrop.hillWave2Min, Tokens.Backdrop.hillWave2Max)
        val p1 = r.range(0.0, TWO_PI)
        val p2 = r.range(0.0, TWO_PI)
        val n = Tokens.Backdrop.hillSamples
        val height = Tokens.Backdrop.hillHeightWorld.toDouble()
        val p = newPath()
        p.moveTo(x0, horizonY)
        for (i in 0..n) {
            val x = x0 + (x1 - x0) * i / n
            val a = HALF + HALF * sin(f1 * x + p1)
            val c = Tokens.Backdrop.hillBase + Tokens.Backdrop.hillMix * sin(f2 * x + p2)
            p.lineTo(x, horizonY - height * a * c)
        }
        p.lineTo(x1, horizonY)
        p.closePath()
        return p
    }

    private fun rebuildSky(vw: Int, vh: Int) {
        val w = max(1, ceil(vw * Tokens.Backdrop.skyScale).toInt())
        val h = max(1, ceil(vh * Tokens.Backdrop.skyScale).toInt())
        sky.width = w
        sky.height = h
        val c: dynamic = sky.getContext("2d")
        val wd = w.toDouble()
        val hd = h.toDouble()
        val grad = c.createLinearGradient(0.0, 0.0, 0.0, hd)
        grad.addColorStop(0.0, skyTopCss)
        grad.addColorStop(1.0, skyHorizonCss)
        c.fillStyle = grad
        c.fillRect(0.0, 0.0, wd, hd)
        c.fillStyle = cloudCss
        for (k in 0 until Tokens.Backdrop.cloudCount) {
            val r = Rng(seed, "cloud:$k")
            val cx = r.range(0.0, 1.0) * wd
            val cy = r.range(Tokens.Backdrop.cloudCyMin, Tokens.Backdrop.cloudCyMax) * hd
            val rx = r.range(Tokens.Backdrop.cloudRxMin, Tokens.Backdrop.cloudRxMax) * wd
            val ry = rx * Tokens.Backdrop.cloudFlatten
            for (j in 0 until CLOUD_PUFFS) {
                val shift = (j - 1) * PUFF_SHIFT * rx
                val lift = if (j == 1) -PUFF_LIFT * ry else 0.0
                c.beginPath()
                c.ellipse(cx + shift, cy + lift, rx, ry, 0.0, 0.0, TWO_PI)
                c.fill()
            }
        }
        c.fillStyle = grainPattern(c)
        c.fillRect(0.0, 0.0, wd, hd)
        skyW = vw
        skyH = vh
    }

    private fun grainPattern(c: dynamic): dynamic {
        val tilePx = Tokens.Backdrop.grainTilePx
        val tile = document.createElement("canvas") as HTMLCanvasElement
        tile.width = tilePx
        tile.height = tilePx
        val t: dynamic = tile.getContext("2d")
        t.fillStyle = grainCss
        val r = Rng(seed, "grain")
        for (i in 0 until Tokens.Backdrop.grainDots) {
            t.fillRect((r.nextDouble() * tilePx).toInt().toDouble(), (r.nextDouble() * tilePx).toInt().toDouble(), 1.0, 1.0)
        }
        return c.createPattern(tile, "repeat")
    }

    /**
     * Draws the sky, then hills, grass and earth, into [g] (transform = device pixel ratio scale, CSS px). The camera is
     * ([cx], [cy]) at [s] CSS px per world unit; the viewport is [vw] x [vh] CSS px.
     */
    fun draw(g: dynamic, dpr: Double, s: Double, cx: Double, cy: Double, vw: Int, vh: Int) {
        if (vw != skyW || vh != skyH) rebuildSky(vw, vh)
        g.setTransform(dpr, 0.0, 0.0, dpr, 0.0, 0.0)
        g.drawImage(sky, 0.0, 0.0, vw.toDouble(), vh.toDouble())

        val ww = vw / s
        val wh = vh / s
        val left = cx - ww / 2.0
        val top = cy - wh / 2.0
        val bottom = top + wh
        if (bottom <= horizonY - Tokens.Backdrop.hillHeightWorld) return // only sky in view
        val sk = dpr * s
        g.setTransform(sk, 0.0, 0.0, sk, -left * sk, -top * sk)
        g.fillStyle = hillsCss
        g.fill(hills)
        g.fillStyle = grassCss
        g.fillRect(left, horizonY, ww, Tokens.Backdrop.grassWorld.toDouble())
        if (bottom > grassBottom) {
            g.fillStyle = earthCss
            g.fillRect(left, grassBottom, ww, bottom - grassBottom)
        }
        g.setTransform(dpr, 0.0, 0.0, dpr, 0.0, 0.0)
    }
}
