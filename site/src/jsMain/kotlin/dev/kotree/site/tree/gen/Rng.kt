package dev.kotree.site.tree.gen

/**
 * Small deterministic PRNG (SplitMix64). Pure Kotlin, no platform random, so the same seed gives the
 * same sequence on every engine (D1). Child streams are derived from a string key so the result for one
 * node never depends on how many other nodes were generated before it.
 */
class Rng private constructor(private var state: Long) {

    constructor(seed: Long, key: String) : this(mix(seed xor fnv(key)))

    fun nextLong(): Long {
        state += GOLDEN
        var z = state
        z = (z xor (z ushr 30)) * MUL_1
        z = (z xor (z ushr 27)) * MUL_2
        return z xor (z ushr 31)
    }

    /** Uniform in [0, 1). */
    fun nextDouble(): Double = (nextLong() ushr 11).toDouble() / TWO_POW_53

    /** Uniform in [from, to). */
    fun range(from: Double, to: Double): Double = from + (to - from) * nextDouble()

    /** Uniform in [-amount, amount). */
    fun jitter(amount: Double): Double = range(-amount, amount)

    private companion object {
        val GOLDEN = 0x9E3779B97F4A7C15UL.toLong()
        val MUL_1 = 0xBF58476D1CE4E5B9UL.toLong()
        val MUL_2 = 0x94D049BB133111EBUL.toLong()
        const val TWO_POW_53 = 9007199254740992.0
        val FNV_OFFSET = 0xCBF29CE484222325UL.toLong()
        const val FNV_PRIME = 1099511628211L

        fun mix(v: Long): Long {
            var z = v + GOLDEN
            z = (z xor (z ushr 30)) * MUL_1
            z = (z xor (z ushr 27)) * MUL_2
            return z xor (z ushr 31)
        }

        fun fnv(key: String): Long {
            var h = FNV_OFFSET
            for (c in key) {
                h = (h xor c.code.toLong()) * FNV_PRIME
            }
            return h
        }
    }
}
