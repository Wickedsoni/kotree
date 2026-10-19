package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry

/**
 * FNV-1a 64 over every number and id in the geometry, including all split steps. Returned as hex.
 * The dev page logs it twice; equal strings mean the generator is deterministic (docs/12 §10).
 */
fun geometryHash(g: TreeGeometry): String {
    var h = 0xCBF29CE484222325UL.toLong() // FNV offset basis
    fun feed(v: Long) {
        var x = v
        repeat(8) {
            h = (h xor (x and 0xFF)) * 1099511628211L
            x = x ushr 8
        }
    }
    fun feed(s: String) {
        feed(s.length.toLong())
        for (c in s) feed(c.code.toLong())
    }
    fun feed(d: Double) = feed(d.toRawBits())
    fun feed(p: Polyline) {
        feed(p.widthPx.toLong())
        feed(p.tipWidthPx.toLong())
        feed(p.ink)
        feed(p.points.size.toLong())
        for (pt in p.points) {
            feed(pt.x)
            feed(pt.y)
        }
    }

    feed(g.bounds.minX); feed(g.bounds.minY); feed(g.bounds.maxX); feed(g.bounds.maxY)
    feed(g.trunk)
    g.roots.forEach { feed(it) }
    for (b in g.boughs) {
        feed(b.id)
        feed(b.forkPoint.x); feed(b.forkPoint.y)
        feed(b.centre)
        feed(b.labelAnchor.x); feed(b.labelAnchor.y)
        for (br in b.branches) {
            feed(br.id)
            feed(br.full)
            feed(br.tip.x); feed(br.tip.y)
            feed(br.labelAnchor.x); feed(br.labelAnchor.y)
        }
        for (step in 0..Tokens.Zoom.splitSteps) {
            g.limbsAt(b.id, step).forEach { feed(it) }
        }
    }
    return h.toULong().toString(HEX_RADIX)
}

private const val HEX_RADIX = 16
