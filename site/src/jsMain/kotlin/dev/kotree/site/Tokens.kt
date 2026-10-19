package dev.kotree.site

/**
 * Every colour, size, threshold and duration of the tree-first prototype (docs/12 §3, §7).
 * Names follow docs/02 (dotted token name in the KDoc, camelCase here). No literal of this kind
 * may appear outside this file (hard rule 2). Phase 1 swaps values or points them at StyleVariables.
 */
object Tokens {

    object Color {
        /** color.surface */ const val surface = 0xFFFFFF
        /** color.bark */ const val bark = 0x000000
        /** color.on-surface */ const val onSurface = 0x1A1C1E
        /** color.on-surface-muted */ const val onSurfaceMuted = 0x5F6368
        /** color.outline */ const val outline = 0xC4C7CC
        /** color.leaf.shadow (prototype branch limb colour, docs/12 §2) */ const val leafShadow = 0xD8DADF
        /** color.leaf.shadow.label */ const val leafShadowLabel = 0x6B7280

        /** Token names used as `ink` strings in the contracts. */
        const val inkBark = "color.bark"
        /** Grey limb ink for branches without a live leaf (docs/02 �5 rule 8). */
        const val inkOutline = "color.outline"

        /** Resolves an ink token name from the contracts to its rgb value. */
        fun inkRgb(name: String): Int = when (name) {
            inkBark -> bark
            inkOutline -> outline
            else -> error("Unknown ink token: $name")
        }
    }

    object Type {
        /** font.ui fallback only; Roboto Flex arrives in Phase 1. */
        val fontUi: Array<String> = arrayOf("system-ui", "sans-serif")
        /** type.label: 14 / 20, weight 600 */
        const val labelSizePx = 14
        const val labelLineHeightPx = 20
        const val labelWeight = 600
        const val placeholderTitleSizePx = 32
    }

    object Space {
        /** Spacing grid (docs/02 §4: space.1 = 4). */
        const val grid = 4
        const val hitAreaPx = 24
        const val labelOutlinePx = 3
        const val hairlinePx = 1
    }

    object Layout {
        const val fullViewportHeightVh = 100
    }

    object World {
        /** world.px: screen pixels per world pixel at Canopy zoom. */
        const val px = 4
        const val seed = 20261017L
        /** World bounds in world pixels (prototype choice). */
        const val widthPx = 1600
        const val heightPx = 1200
    }

    object Zoom {
        const val zMin = 0.8
        const val zMax = 4.0
        const val splitStart = 1.4
        const val splitEnd = 2.4
        const val boughEnter = 1.8
        const val boughLeave = 1.62
        /** Quantised split steps: 0..8 means 9 bitmaps per bough (docs/03 §2.4). */
        const val splitSteps = 8
    }

    /** Generator shape tuning (layout, not design). World size comes from [World]. */
    object Gen {
        const val POINTS = 16
        const val TRUNK_POINTS = 12
        const val TRUNK_BASE_Y = 0.78
        const val TRUNK_TOP_Y = 0.50
        const val TRUNK_WOBBLE = 14.0
        const val TRUNK_WIDTH = 40
        const val TRUNK_TOP_WIDTH = 24
        const val BOUGH_WIDTH = 20
        const val BOUGH_TIP_WIDTH = 8
        const val BRANCH_WIDTH = 8
        const val BRANCH_TIP_WIDTH = 4
        const val BOUGH_LEN = 340.0
        const val BOUGH_LEN_JITTER = 30.0
        const val BOUGH_SPREAD_DEG = 50.0
        const val FORK_LOW = 0.18
        const val BEND = 50.0
        const val BRANCH_SPREAD = 125.0
        const val BRANCH_EXTRA_MIN = 40.0
        const val BRANCH_EXTRA_MAX = 110.0
        const val BRANCH_CURL = 14.0
        const val ROOT_COUNT = 6
        const val ROOT_MIN_DEG = 22.0
        const val ROOT_MAX_DEG = 78.0
        const val ROOT_LEN_MIN = 150.0
        const val ROOT_LEN_MAX = 210.0
        const val ROOT_WIDTH_MAX = 22
        const val ROOT_WIDTH_MIN = 14
        const val ROOT_TIP_WIDTH = 4
        const val FOUNDATION_LEN = 150.0
        const val LABEL_GAP = 24.0
        /** Extra world units kept around label anchors inside the bounds so labels fit on screen at z = 1. */
        const val LABEL_PAD = 24.0
        const val FOUNDATION_ID = "roots"
        const val DEG = kotlin.math.PI / 180.0
        /** Coordinates are rounded to 1/QUANT world px so the geometry hash is stable. */
        const val QUANT = 16.0
    }

    /** Camera and input tuning. */
    object Camera {
        /** Zoom per wheel pixel, in e-folds (z *= exp(-deltaY * this)). */
        const val wheelSensitivity = 0.0015
        /** Wheel `deltaMode` LINE to pixels. */
        const val wheelLineHeightPx = 16.0
        /** One press of + or -. */
        const val keyZoomFactor = 1.25
        /** Arrow-key pan step as a fraction of the shorter viewport side. */
        const val panStepFraction = 0.12
        /** Pointer travel before a press becomes a drag. */
        const val dragThresholdPx = Space.grid
        /** Fit margins from docs/03 �2.5. */
        const val fitMarginTree = 0.05
        const val fitMarginBough = 0.08
        const val fitMarginBranch = 0.08
    }

    object Motion {
        const val flyDurationMs = 500
    }
}
