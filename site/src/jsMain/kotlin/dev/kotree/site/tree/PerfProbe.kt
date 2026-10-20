package dev.kotree.site.tree

import kotlinx.browser.window

/**
 * Perf probe (ADR 006), active only with `?perf=1`. Records draw milliseconds and rAF deltas in preallocated
 * Float64Arrays (Kotlin DoubleArray) and exposes `window.__kotreePerf` (p50, p95, max per series, frame counter, long tasks)
 * and `window.__kotreeLabels()` (placed label rectangles). Off: one boolean check per frame.
 */
object PerfProbe {
    val enabled: Boolean = window.location.search.contains("perf=1")

    private const val CAPACITY = 1200
    private const val REFRESH_EVERY = 30
    private const val P50 = 0.5
    private const val P95 = 0.95
    private val drawMs = DoubleArray(CAPACITY)
    private val frameMs = DoubleArray(CAPACITY)
    private var drawCount = 0
    private var frameCount = 0
    private var lastFrame = Double.NaN
    private var frames = 0
    private var longTasks = 0
    private var longMax = 0.0
    private var published: dynamic = null

    /** Returns the start time, or 0 when the probe is off. */
    fun beginDraw(): Double = if (enabled) window.performance.now() else 0.0

    fun endDraw(start: Double) {
        if (!enabled) return
        drawMs[drawCount % CAPACITY] = window.performance.now() - start
        drawCount++
        if (drawCount % REFRESH_EVERY == 0) refresh()
    }

    /** Call once per frame with the rAF timestamp. */
    fun frame(now: Double) {
        if (!enabled) return
        frames++
        if (!lastFrame.isNaN()) {
            frameMs[frameCount % CAPACITY] = now - lastFrame
            frameCount++
        }
        lastFrame = now
    }

    /** Idle gaps must not count as slow frames: the loop calls this when it stops. */
    fun idle() {
        lastFrame = Double.NaN
    }

    /** Publishes `window.__kotreePerf` and `window.__kotreeLabels`; [labels] returns the placed label rectangles. */
    fun install(labels: () -> dynamic) {
        if (!enabled) return
        val obj: dynamic = js("({})")
        val refreshFn: () -> Unit = { refresh() }
        obj.snapshot = refreshFn
        published = obj
        window.asDynamic().__kotreePerf = obj
        val labelsFn: () -> dynamic = { labels() }
        window.asDynamic().__kotreeLabels = labelsFn
        watchLongTasks()
        refresh()
    }

    private fun refresh() {
        val obj = published ?: return
        obj.draw = stats(drawMs, minOf(drawCount, CAPACITY))
        obj.frame = stats(frameMs, minOf(frameCount, CAPACITY))
        obj.frames = frames
        obj.longTasks = longTasks
        obj.longTaskMax = longMax
    }

    private fun stats(values: DoubleArray, n: Int): dynamic {
        val out: dynamic = js("({})")
        out.n = n
        if (n == 0) {
            out.p50 = 0.0
            out.p95 = 0.0
            out.max = 0.0
            return out
        }
        val sorted = values.copyOfRange(0, n)
        sorted.sort()
        out.p50 = sorted[((n - 1) * P50).toInt()]
        out.p95 = sorted[((n - 1) * P95).toInt()]
        out.max = sorted[n - 1]
        return out
    }

    private fun watchLongTasks() {
        try {
            val cb: (dynamic) -> Unit = { list ->
                val entries = list.getEntries()
                val len = entries.length as Int
                for (i in 0 until len) {
                    longTasks++
                    val d = entries[i].duration as Number
                    if (d.toDouble() > longMax) longMax = d.toDouble()
                }
            }
            val observer: dynamic = js("Reflect").construct(js("PerformanceObserver"), arrayOf<dynamic>(cb))
            val options: dynamic = js("({})")
            options.entryTypes = arrayOf("longtask")
            observer.observe(options)
        } catch (e: Throwable) {
            console.warn("long tasks are not observable in this browser", e)
        }
    }
}
