package dev.kotree.site.tree.gen

import dev.kotree.site.Tokens
import dev.kotree.site.tree.Bounds
import dev.kotree.site.tree.Polyline
import dev.kotree.site.tree.TreeGeometry
import dev.kotree.site.tree.Vec

/** FNV-1a 64 feeder shared by [limbHash] and [fullHash]. */
private class Fnv {
    private var h = 0xCBF29CE484222325UL.toLong() // FNV offset basis

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
    fun feed(v: Vec) { feed(v.x); feed(v.y) }
    fun feed(b: Bounds) { feed(b.minX); feed(b.minY); feed(b.maxX); feed(b.maxY) }

    fun feed(p: Polyline) {
        feed(p.widthPx.toLong())
        feed(p.tipWidthPx.toLong())
        feed(p.ink)
        feed(p.points.size.toLong())
        for (pt in p.points) feed(pt)
    }

    fun hex(): String = h.toULong().toString(HEX_RADIX)
}

/**
 * FNV-1a 64 over every number and id of the trunk, roots, limbs and labels, including all split steps.
 * Stems and leaves are NOT included, so this must stay cecb906380417dd8 (proof that no limb moved, docs/14 §4.2).
 */
fun limbHash(g: TreeGeometry): String {
    val f = Fnv()
    f.feed(g.bounds)
    f.feed(g.trunk)
    g.roots.forEach { f.feed(it) }
    for (b in g.boughs) {
        f.feed(b.id)
        f.feed(b.forkPoint)
        f.feed(b.centre)
        f.feed(b.labelAnchor)
        for (br in b.branches) {
            f.feed(br.id)
            f.feed(br.full)
            f.feed(br.tip)
            f.feed(br.labelAnchor)
        }
        for (step in 0..Tokens.Zoom.splitSteps) {
            g.limbsAt(b.id, step).forEach { f.feed(it) }
        }
    }
    return f.hex()
}

/**
 * FNV-1a 64 over every stem twig, tip, label anchor, leaf slot and bounds, plus each branch's bounds.
 * This is the determinism value from Phase 3 on (docs/14 §4.2).
 */
fun fullHash(g: TreeGeometry): String {
    val f = Fnv()
    for (b in g.boughs) {
        f.feed(b.id)
        for (br in b.branches) {
            f.feed(br.id)
            f.feed(br.bounds)
            f.feed(br.stems.size.toLong())
            for (s in br.stems) {
                f.feed(s.id)
                f.feed(s.twig)
                f.feed(s.tip)
                f.feed(s.labelAnchor)
                f.feed(s.bounds)
                f.feed(s.leaves.size.toLong())
                for (l in s.leaves) {
                    f.feed(l.id)
                    f.feed(l.at)
                }
            }
        }
    }
    return f.hex()
}

/** Both hashes for the console log: "limbHash=<hex> fullHash=<hex>". Printed twice by the dev page. */
fun geometryHash(g: TreeGeometry): String = "limbHash=${limbHash(g)} fullHash=${fullHash(g)}"

private const val HEX_RADIX = 16
