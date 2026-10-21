package dev.kotree.site.tree.render

import dev.kotree.site.Tokens
import dev.kotree.site.tree.PerfProbe
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.hit.HitIndex
import dev.kotree.site.tree.hit.Kind
import dev.kotree.site.tree.hit.Node
import dev.kotree.site.tree.hit.TreeNodes
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private const val PERCENT = 100.0
private const val STATUS_SHADOW = 0
private const val STATUS_UPNEXT = 1
private const val DIFFICULTY_HARD = 2
private const val TWO_PI = 2.0 * PI
private const val TANGENT_EPS = 1e-9
/** Control point of the leaf lens (unit length): half its value is the half-width of the leaf. */
private const val LENS_CTRL = 0.56
/** The vein of a hard leaf spans this share of the leaf length. */
private const val VEIN_HALF = 0.32

/** A filled shape and its colour. */
private class Piece(val path: dynamic, val color: String)

private fun newPath(): dynamic = js("new Path2D()")

private fun rgbaCss(rgb: Int, percent: Int): String =
    "rgba(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF}, ${percent / PERCENT})"

/**
 * Appends one tapered limb to [path]: left and right edges offset by half the width along the normals, round caps at both
 * ends. Width runs linearly (by arc length) from widthPx to tipWidthPx. Filled with the non-zero rule.
 */
private fun addLimb(path: dynamic, line: Polyline) {
    val pts = line.points
    val n = pts.size
    if (n == 0) return
    val w0 = line.widthPx.toDouble()
    val w1 = line.tipWidthPx.toDouble()
    if (n == 1) {
        path.moveTo(pts[0].x + w0 / 2.0, pts[0].y)
        path.arc(pts[0].x, pts[0].y, w0 / 2.0, 0.0, TWO_PI)
        path.closePath()
        return
    }
    val cum = DoubleArray(n)
    for (i in 1 until n) cum[i] = cum[i - 1] + hypot(pts[i].x - pts[i - 1].x, pts[i].y - pts[i - 1].y)
    val total = cum[n - 1]
    val nx = DoubleArray(n)
    val ny = DoubleArray(n)
    val hw = DoubleArray(n)
    for (i in 0 until n) {
        val a = pts[max(i - 1, 0)]
        val b = pts[min(i + 1, n - 1)]
        var dx = b.x - a.x
        var dy = b.y - a.y
        var len = hypot(dx, dy)
        if (len < TANGENT_EPS) { dx = 1.0; dy = 0.0; len = 1.0 }
        nx[i] = -dy / len
        ny[i] = dx / len
        hw[i] = (w0 + (w1 - w0) * (if (total > 0.0) cum[i] / total else 0.0)) / 2.0
    }
    path.moveTo(pts[0].x + nx[0] * hw[0], pts[0].y + ny[0] * hw[0])
    for (i in 1 until n) path.lineTo(pts[i].x + nx[i] * hw[i], pts[i].y + ny[i] * hw[i])
    val aEnd = atan2(ny[n - 1], nx[n - 1])
    path.arc(pts[n - 1].x, pts[n - 1].y, hw[n - 1], aEnd, aEnd - PI, true)
    for (i in n - 2 downTo 0) path.lineTo(pts[i].x - nx[i] * hw[i], pts[i].y - ny[i] * hw[i])
    val aStart = atan2(ny[0], nx[0])
    path.arc(pts[0].x, pts[0].y, hw[0], aStart + PI, aStart, true)
    path.closePath()
}

private fun buildLens(): dynamic {
    val p = newPath()
    p.moveTo(-0.5, 0.0)
    p.quadraticCurveTo(0.0, -LENS_CTRL, 0.5, 0.0)
    p.quadraticCurveTo(0.0, LENS_CTRL, -0.5, 0.0)
    p.closePath()
    return p
}

private fun buildVein(): dynamic {
    val p = newPath()
    p.moveTo(-VEIN_HALF, 0.0)
    p.lineTo(VEIN_HALF, 0.0)
    return p
}

private fun smoothstep(x: Double): Double {
    val t = x.coerceIn(0.0, 1.0)
    return t * t * (3.0 - 2.0 * t)
}

/** Split progress p (docs/03 §2.4). */
internal fun splitProgress(z: Double): Double =
    smoothstep((z - Tokens.Zoom.splitStart) / (Tokens.Zoom.splitEnd - Tokens.Zoom.splitStart))

/**
 * Smooth vector renderer on one Canvas 2D (ADR 006). Geometry is turned into cached Path2D objects once; a frame fills
 * cached paths (through [WoodLayer]), then draws the hover and focus states and the labels on top. Backing size = CSS size
 * x devicePixelRatio, smoothing on, no pixelated rendering. [paintWood] allocates nothing per call except the transform
 * numbers. Path2D is created through `js("new Path2D()")` and drawn through `dynamic`, so no Kotlin binding is needed.
 */
class VectorRenderer(
    private val canvas: HTMLCanvasElement,
    private val geometry: TreeGeometry,
    val nodes: TreeNodes,
) : WoodPainter {
    /** ADR 006 Q1: shadow leaves are drawn with the locked look. One boolean reverts it. */
    var drawShadowLeaves: Boolean = true

    /** The node under the pointer and the node with keyboard focus; set by the frame loop and by A11yNodes. */
    var hover: Node? = null
        private set
    var focus: Node? = null

    private val g: dynamic = canvas.getContext("2d")
    private val hits = HitIndex(nodes, geometry)
    private val labels = LabelLayout(nodes, g)
    private val wood = WoodLayer(this)
    private val backdrop = Backdrop(geometry)

    private val surfaceCss = cssColor(Tokens.Color.surface)
    private val stemCss = cssColor(Tokens.Status.treeStem)
    private val liveCss = cssColor(Tokens.Status.live)
    private val upNextCss = cssColor(Tokens.Status.upnext)
    private val glowCss = cssColor(Tokens.Status.upnextGlow)
    private val haloCss = rgbaCss(Tokens.Status.upnextGlow, Tokens.Leaf.upnextGlowAlphaPercent)
    private val shadowFillCss = cssColor(Tokens.Leaf.shadow)
    private val shadowStrokeCss = cssColor(Tokens.Status.leafShadowStroke)
    private val leafFillCss = arrayOf(cssColor(Tokens.Leaf.easy), cssColor(Tokens.Leaf.medium), cssColor(Tokens.Leaf.hard))
    private val hoverLayerCss = rgbaCss(Tokens.Color.onSurface, Tokens.StateLayer.hoverPercent)
    private val tipInkCss = cssColor(Tokens.Color.onSurface)
    private val tipEdgeCss = cssColor(Tokens.Color.outline)
    private val defaultRingCss = cssColor(Tokens.Chrome.focusRingDefault)
    private val ringByBranch = HashMap<String, String>().also { m ->
        for ((slug, rgb) in Tokens.Branch.inks) m[slug] = cssColor(rgb)
    }

    private val leafMin = Tokens.Tree.leafMinPx.toDouble()
    private val leafMax = Tokens.Tree.leafMaxPx.toDouble()
    private val hitRadius = Tokens.Space.hitAreaPx / 2.0

    private val lens: dynamic = buildLens()
    private val vein: dynamic = buildVein()

    private val rootPieces: Array<Piece> = Array(geometry.roots.size) { pieceOf(geometry.roots[it]) }
    private val trunkPiece: Piece = pieceOf(geometry.trunk)
    private val boughPieces: Array<Array<Array<Piece>?>> =
        Array(geometry.boughs.size) { arrayOfNulls<Array<Piece>>(Tokens.Zoom.splitSteps + 1) }
    private val twigPaths: Array<Any?> = Array<Any?>(nodes.branchNodes.size) { i ->
        val stems = nodes.branchNodes[i].branchGeo?.stems.orEmpty()
        if (stems.isEmpty()) {
            null
        } else {
            val p = newPath()
            for (st in stems) {
                val pts = st.twig.points
                if (pts.isEmpty()) continue
                p.moveTo(pts[0].x, pts[0].y)
                for (j in 1 until pts.size) p.lineTo(pts[j].x, pts[j].y)
            }
            p
        }
    }

    // Camera facts of the last draw, for hit tests between frames.
    private var curS = 1.0
    private var curCx = 0.0
    private var curCy = 0.0
    private var curHalfW = 0.0
    private var curHalfH = 0.0
    private var curLevel = 0
    private var curView: ViewState? = null
    private var curSet: LabelSet? = null
    private var viewRef: ViewState? = null
    private var branchIdx = -1
    private var stemIdx = -1
    private var lastBase = Double.NaN
    private var tipX = 0.0
    private var tipY = 0.0
    private var tipW = 0.0
    private var tipH = 0.0

    private fun pieceOf(line: Polyline): Piece {
        val p = newPath()
        addLimb(p, line)
        return Piece(p, cssColor(Tokens.Color.inkRgb(line.ink)))
    }

    private fun piecesOf(bi: Int, step: Int): Array<Piece> {
        val cached = boughPieces[bi][step]
        if (cached != null) return cached
        val bough = geometry.boughs[bi]
        // Limbs first, the bough on top: at step 0 the limbs hide inside the bough.
        val lines = geometry.limbsAt(bough.id, step) + bough.centre
        val built = Array(lines.size) { pieceOf(lines[it]) }
        boughPieces[bi][step] = built
        return built
    }

    /** Called when the font arrives: text widths and label layouts are measured again. */
    fun fontsChanged() {
        labels.invalidate()
    }

    fun invalidateWood() {
        wood.invalidate()
    }

    override fun paintWood(g: dynamic, s: Double, k: Double, ox: Double, oy: Double, ww: Double, wh: Double, step: Int, level: Int, branch: Int) {
        val sk = s * k
        g.setTransform(sk, 0.0, 0.0, sk, -ox * sk, -oy * sk)
        for (p in rootPieces) { g.fillStyle = p.color; g.fill(p.path) }
        g.fillStyle = trunkPiece.color
        g.fill(trunkPiece.path)
        for (bi in boughPieces.indices) {
            for (p in piecesOf(bi, step)) { g.fillStyle = p.color; g.fill(p.path) }
        }
        if (step >= Tokens.Zoom.twigsOnStep) {
            g.strokeStyle = stemCss
            g.lineCap = "round"
            g.lineJoin = "round"
            g.lineWidth = (Tokens.Gen.STEM_WIDTH * s).coerceIn(Tokens.Tree.stemLineMinPx, Tokens.Tree.stemLineMaxPx) / s
            for (t in twigPaths) if (t != null) g.stroke(t)
        }
        if (level >= LEVEL_BRANCH) paintLeaves(g, s, k, ox, oy, ww, wh, level, branch)
    }

    private fun paintLeaves(g: dynamic, s: Double, k: Double, ox: Double, oy: Double, ww: Double, wh: Double, level: Int, branch: Int) {
        val strokePx = Tokens.Tree.stemLineMinPx
        val haloPx = Tokens.Space.grid.toDouble()
        val ls = (Tokens.Gen.LEAF_SIZE * s).coerceIn(leafMin, leafMax)
        val reach = ls / s
        val lineLocal = strokePx / ls
        for (i in 0 until nodes.leafCount) {
            if (level < LEVEL_STEM && nodes.leafBranchIdx[i] != branch) continue
            val status = nodes.leafStatus[i]
            if (status == STATUS_SHADOW && !drawShadowLeaves) continue
            val x = nodes.leafX[i]
            val y = nodes.leafY[i]
            if (x < ox - reach || x > ox + ww + reach || y < oy - reach || y > oy + wh + reach) continue
            val c = nodes.leafCos[i] * ls * k
            val sn = nodes.leafSin[i] * ls * k
            g.setTransform(c, sn, -sn, c, (x - ox) * s * k, (y - oy) * s * k)
            if (status == STATUS_SHADOW) {
                g.fillStyle = shadowFillCss
                g.fill(lens)
                g.strokeStyle = shadowStrokeCss
                g.lineWidth = lineLocal
                g.stroke(lens)
            } else if (status == STATUS_UPNEXT) {
                g.strokeStyle = haloCss
                g.lineWidth = (strokePx + 2.0 * haloPx) / ls
                g.stroke(lens)
                g.fillStyle = glowCss
                g.fill(lens)
                g.strokeStyle = upNextCss
                g.lineWidth = lineLocal
                g.stroke(lens)
            } else {
                val diff = nodes.leafDifficulty[i]
                g.fillStyle = leafFillCss[diff]
                g.fill(lens)
                g.strokeStyle = liveCss
                g.lineWidth = lineLocal
                g.stroke(lens)
                if (diff == DIFFICULTY_HARD) {
                    g.strokeStyle = surfaceCss
                    g.stroke(vein)
                }
            }
        }
    }

    /**
     * One frame. [pointerX], [pointerY] = pointer in CSS px relative to the canvas, negative when outside; the hover node
     * is found here, once per frame. [crisp] = motion has stopped, make the wood layer exact.
     */
    fun draw(cam: Camera, view: ViewState, crisp: Boolean, pointerX: Double, pointerY: Double) {
        val t0 = PerfProbe.beginDraw()
        val vp = cam.viewport
        val dpr = window.devicePixelRatio
        val bw = (vp.widthPx * dpr).roundToInt()
        val bh = (vp.heightPx * dpr).roundToInt()
        if (bw <= 0 || bh <= 0) return // not laid out yet: nothing to draw
        if (canvas.width != bw || canvas.height != bh) {
            canvas.width = bw
            canvas.height = bh
            wood.invalidate()
        }
        if (cam.base != lastBase) {
            lastBase = cam.base
            labels.invalidate()
        }
        if (view !== viewRef) {
            viewRef = view
            branchIdx = view.branchId?.let { nodes.branchIndex[it] } ?: -1
            stemIdx = view.stemId?.let { nodes.stemIndex[it] } ?: -1
        }
        val s = cam.scale
        val level = view.level.ordinal
        val set = labels.get(level, branchIdx, stemIdx, cam.z, cam.base)
        curS = s
        curCx = cam.cx
        curCy = cam.cy
        curHalfW = vp.widthPx / 2.0
        curHalfH = vp.heightPx / 2.0
        curLevel = level
        curView = view
        curSet = set
        updateHover(pointerX, pointerY)

        backdrop.draw(g, dpr, s, cam.cx, cam.cy, vp.widthPx, vp.heightPx)
        val step = (splitProgress(cam.z) * Tokens.Zoom.splitSteps).roundToInt()
        wood.draw(g, cam, s, dpr, step, level, branchIdx, crisp)
        g.setTransform(dpr, 0.0, 0.0, dpr, 0.0, 0.0)

        val hovered = hover
        val focused = focus
        if (hovered != null) drawHoverLayer(hovered, set, view)
        if (focused != null && focused !== hovered) drawHoverLayer(focused, set, view)
        drawLabels(set, view, vp.widthPx.toDouble(), vp.heightPx.toDouble(), hovered, focused)
        if (hovered != null) drawTooltipIfHidden(hovered, set, view)
        if (focused != null) {
            val tipShown = focused !== hovered && drawTooltipIfHidden(focused, set, view)
            drawFocusRing(focused, set, view, tipShown || (focused === hovered && labelIndex(focused, set, view) < 0))
        }
        PerfProbe.endDraw(t0)
    }

    // ---- hit testing and hover ----

    private fun updateHover(x: Double, y: Double) {
        val next = if (x < 0.0 || y < 0.0) null else hitNode(x, y)
        if (next !== hover) {
            hover = next
            canvas.style.setProperty("cursor", if (next != null) "pointer" else "")
        }
    }

    /** The node at a CSS-px point of the canvas: label rectangles first, then limbs, twigs and leaves. */
    fun hitNode(x: Double, y: Double): Node? {
        val set = curSet
        val view = curView
        if (set != null && view != null) {
            for (i in 0 until set.count) {
                if (!labelShown(set, i, view)) continue
                val rx = (set.ax[i] - curCx) * curS + curHalfW + set.ox[i]
                val ry = (set.ay[i] - curCy) * curS + curHalfH + set.oy[i]
                if (x >= rx && x <= rx + set.w[i] && y >= ry && y <= ry + set.h[i]) return set.node[i]
            }
        }
        return hits.query(curCx + (x - curHalfW) / curS, curCy + (y - curHalfH) / curS, curS, curLevel)
    }

    private fun labelShown(set: LabelSet, i: Int, view: ViewState): Boolean {
        if (!set.visible[i]) return false
        val filter = set.boughFilter[i]
        return filter == null || filter == view.boughId
    }

    /** Index of [n]'s label when it is placed and shown now, else -1. */
    private fun labelIndex(n: Node, set: LabelSet, view: ViewState): Int {
        val i = set.indexOf[n.id] ?: return -1
        return if (labelShown(set, i, view)) i else -1
    }

    // ---- states and labels ----

    private fun drawHoverLayer(n: Node, set: LabelSet, view: ViewState) {
        g.fillStyle = hoverLayerCss
        if (n.kind == Kind.LEAF) {
            val sx = (n.ax - curCx) * curS + curHalfW
            val sy = (n.ay - curCy) * curS + curHalfH
            g.beginPath()
            g.arc(sx, sy, hitRadius, 0.0, TWO_PI)
            g.fill()
        }
        val i = labelIndex(n, set, view)
        if (i >= 0) {
            val rx = (set.ax[i] - curCx) * curS + curHalfW + set.ox[i]
            val ry = (set.ay[i] - curCy) * curS + curHalfH + set.oy[i]
            roundRect(rx, ry, set.w[i], set.h[i], Tokens.Shape.xs.toDouble())
            g.fill()
        }
    }

    private fun drawLabels(set: LabelSet, view: ViewState, w: Double, h: Double, hovered: Node?, focused: Node?) {
        g.textBaseline = "middle"
        g.textAlign = "left"
        g.lineJoin = "round"
        g.lineWidth = Tokens.Space.labelOutlinePx * 2.0
        g.strokeStyle = surfaceCss
        for (i in 0 until set.count) {
            if (!labelShown(set, i, view)) continue
            val x = (set.ax[i] - curCx) * curS + curHalfW + set.ox[i]
            val y = (set.ay[i] - curCy) * curS + curHalfH + set.oy[i]
            if (x > w || y > h || x + set.w[i] < 0.0 || y + set.h[i] < 0.0) continue
            val node = set.node[i]
            val bold = node != null && (node === hovered || node === focused)
            drawText(set.text[i], x, y, set.h[i], set.style[i], bold, set.color[i])
        }
    }

    private fun drawText(text: String, x: Double, y: Double, h: Double, style: Int, bold: Boolean, color: String) {
        g.font = LabelFonts.of(style, bold)
        val tx = x + Tokens.Space.labelOutlinePx
        val ty = y + h / 2.0
        g.strokeText(text, tx, ty)
        g.fillStyle = color
        g.fillText(text, tx, ty)
    }

    /** A hovered or focused node whose label found no free slot gets its title as a tooltip. Returns true if drawn. */
    private fun drawTooltipIfHidden(n: Node, set: LabelSet, view: ViewState): Boolean {
        if (labelIndex(n, set, view) >= 0) return false
        val sx = (n.ax - curCx) * curS + curHalfW
        val sy = (n.ay - curCy) * curS + curHalfH
        val style = if (n.kind == Kind.BOUGH) LabelSet.STYLE_TITLE else LabelSet.STYLE_LABEL
        val pad = Tokens.Spacing.s2.toDouble()
        val w = labels.textWidth(n.title, style) + 2.0 * pad
        val h = LabelFonts.lineHeight(style) + 2.0 * Tokens.Spacing.s1
        val x = sx + hitRadius + Tokens.Spacing.s1
        val y = sy - h / 2.0
        tipX = x
        tipY = y
        tipW = w
        tipH = h
        roundRect(x, y, w, h, Tokens.Shape.xs.toDouble())
        g.fillStyle = surfaceCss
        g.fill()
        g.strokeStyle = tipEdgeCss
        g.lineWidth = Tokens.Space.hairlinePx.toDouble()
        g.stroke()
        g.font = LabelFonts.of(style, true)
        g.textBaseline = "middle"
        g.textAlign = "left"
        g.fillStyle = tipInkCss
        g.fillText(n.title, x + pad, y + h / 2.0)
        return true
    }

    private fun drawFocusRing(n: Node, set: LabelSet, view: ViewState, onTooltip: Boolean) {
        val off = Tokens.Chrome.focusRingOffsetPx.toDouble()
        val width = Tokens.Chrome.focusRingWidthPx.toDouble()
        g.strokeStyle = ringByBranch[n.branchId] ?: defaultRingCss
        g.lineWidth = width
        if (n.kind == Kind.LEAF) {
            val sx = (n.ax - curCx) * curS + curHalfW
            val sy = (n.ay - curCy) * curS + curHalfH
            g.beginPath()
            g.arc(sx, sy, hitRadius + off + width / 2.0, 0.0, TWO_PI)
            g.stroke()
            return
        }
        val grow = off + width / 2.0
        if (onTooltip) {
            roundRect(tipX - grow, tipY - grow, tipW + 2.0 * grow, tipH + 2.0 * grow, Tokens.Shape.xs.toDouble() + grow)
            g.stroke()
            return
        }
        val i = labelIndex(n, set, view)
        if (i >= 0) {
            val rx = (set.ax[i] - curCx) * curS + curHalfW + set.ox[i]
            val ry = (set.ay[i] - curCy) * curS + curHalfH + set.oy[i]
            roundRect(rx - grow, ry - grow, set.w[i] + 2.0 * grow, set.h[i] + 2.0 * grow, Tokens.Shape.xs.toDouble() + grow)
            g.stroke()
        }
    }

    private fun roundRect(x: Double, y: Double, w: Double, h: Double, radius: Double) {
        val r = min(radius, min(w, h) / 2.0)
        g.beginPath()
        g.moveTo(x + r, y)
        g.arcTo(x + w, y, x + w, y + h, r)
        g.arcTo(x + w, y + h, x, y + h, r)
        g.arcTo(x, y + h, x, y, r)
        g.arcTo(x, y, x + w, y, r)
        g.closePath()
    }

    /** Placed label rectangles of the last frame in CSS px, for `window.__kotreeLabels()` (perf probe). */
    fun labelRects(): dynamic {
        val out: dynamic = js("[]")
        val set = curSet
        val view = curView
        if (set == null || view == null) return out
        for (i in 0 until set.count) {
            if (!labelShown(set, i, view)) continue
            val r: dynamic = js("({})")
            r.x = (set.ax[i] - curCx) * curS + curHalfW + set.ox[i]
            r.y = (set.ay[i] - curCy) * curS + curHalfH + set.oy[i]
            r.w = set.w[i]
            r.h = set.h[i]
            r.text = set.text[i]
            out.push(r)
        }
        return out
    }

    private companion object {
        const val LEVEL_BRANCH = 2
        const val LEVEL_STEM = 3
    }
}
