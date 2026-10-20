package dev.kotree.site

import kotlin.math.round

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
        /** color.leaf.shadow.label */ const val leafShadowLabel = 0x6B7280

        /** Token names used as `ink` strings in the contracts. */
        const val inkBark = "color.bark"
        /** Grey limb ink for branches without a live leaf (docs/02 §5 rule 8). */
        const val inkOutline = "color.outline"

        /** Resolves an ink token name from the contracts to its rgb value. */
        // inkRgb resolves only bark and outline: the two inks the prototype draws.
        fun inkRgb(name: String): Int = when (name) {
            inkBark -> bark
            inkOutline -> outline
            else -> error("Unknown ink token: $name")
        }
    }

    object Type {
        /** font.ui: self-hosted Roboto Flex (public/fonts), then system fallbacks. */
        val fontUi: Array<String> = arrayOf("Roboto Flex", "system-ui", "sans-serif")
        /** font.code: self-hosted JetBrains Mono. Ligatures are switched off in Base.kt. */
        val fontCode: Array<String> = arrayOf("JetBrains Mono", "ui-monospace", "monospace")
        /** type.label: 14 / 20, weight 600 */
        const val labelSizePx = 14
        const val labelLineHeightPx = 20
        const val labelWeight = 600
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
        const val zMax = 24.0
        const val splitStart = 1.4
        const val splitEnd = 2.4
        const val boughEnter = 1.8
        const val boughLeave = 1.62
        /** Lowest zoom a bough fit may use: near the split end, so branch labels are readable. */
        const val boughFitMin = 2.2
        /** Quantised split steps: 0..8 means 9 bitmaps per bough (docs/03 §2.4). */
        const val splitSteps = 8
        /** Z3 Stem level (docs/14 §4.1): enter and leave thresholds (hysteresis). */
        const val stemEnter = 7.0
        const val stemLeave = 6.3
        /** Z4 Leaf level. */
        const val leafEnter = 14.0
        const val leafLeave = 12.6
        /** Stem twigs are drawn only from this split step (fully split). */
        const val twigsOnStep = 8
        /** Z2 Branch level enter (docs/14 §4.1); leave uses the same 0.9 ratio as the bough thresholds. */
        const val branchEnter = 3.5
        /** Stem fly (docs/14 §4.1): fit margin and lowest zoom. */
        const val stemFitMargin = 0.12
        const val stemFitMinZ = 8.0
        /** Branches whose stem labels need more than the default landing z of 4 to clear each other (docs/14 §5). */
        val branchLandingZ: Map<String, Double> = mapOf("ui" to 6.0)
    }

    /** layoutReport estimates (docs/14 §5, docs/03 §2.6). */
    object LayoutCheck {
        /** Estimated label glyph width in CSS px per character. */
        const val LABEL_CHAR_PX = 8.0
        /** Horizontal label padding in CSS px, each side. */
        const val LABEL_PAD_PX = 6.0
        /** Minimum CSS gap between leaf hit areas at the stem level. */
        const val LEAF_GAP_MIN_PX = 8.0
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
        /** Stem twigs attach along the limb at parameters from STEM_T_MIN to STEM_T_MAX, alternating sides. */
        const val STEM_T_MIN = 0.25
        const val STEM_T_MAX = 0.95
        /** Twig length in world px, plus per-stem jitter (Rng "stem:<id>"). */
        const val STEM_LEN = 36.0
        const val STEM_LEN_JITTER = 6.0
        /** Extra twig length for every second same-side stem, so neighbouring labels stack. */
        const val STEM_LEN_STAGGER = 20.0
        /** Twig angle off the limb tangent, degrees, plus jitter. */
        const val STEM_ANGLE_DEG = 55.0
        const val STEM_ANGLE_JITTER_DEG = 8.0
        const val STEM_WIDTH = 2
        const val STEM_TIP_WIDTH = 1
        /** Gap between the leaf grid's edge and the label anchor (sideways), and the twig tip's vertical label offset. */
        const val STEM_LABEL_GAP = 12.0
        /** Leaf slots: fixed pitch between slots in a ceil(sqrt(n))-column grid at the twig tip; leaf edge, world px. */
        const val LEAF_PITCH = 12.0
        const val LEAF_SIZE = 8
        /** Twig bend as a share of twig length, and leaf slot jitter as a share of the pitch (Rng jitter, +-). */
        const val STEM_BEND = 0.15
        const val LEAF_JITTER = 0.05
    }

    /** Camera and input tuning. */
    object Camera {
        /** A different bough becomes current only when its centre is this fraction as far as the held one (no flicker). */
        const val boughSwitchRatio = 0.8
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
        /** Fit margins from docs/03 §2.5. */
        const val fitMarginTree = 0.05
        const val fitMarginBough = 0.08
        const val fitMarginBranch = 0.08
        /** A stem or leaf fly keeps its branch focused until the view is farther than this (world px) from it. */
        const val pinReleaseWorld = 120.0
    }

    /** Label collision nudging (screen px; deterministic, vertical only). */
    object LabelNudge {
        /** Estimated text width per character as a fraction of the label font size. */
        const val charWidthFactor = 0.62
        /** Largest distance a label may move from its limb tip. */
        const val maxPx = 60.0
        const val iterations = 16
        const val gapPx = Space.grid
    }

    object Motion {
        const val flyDurationMs = 500
        /** Half a glow pulse of an up-next leaf (grow, then shrink); static under reduced motion. */
        const val glowPulseMs = 1000
    }

    // ---- Phase 1 additions (docs/13 section 2). Additions only: nothing above this line was renamed. ----
    // Hex values are typed here and nowhere else. scripts/check-tokens.py reads the `const val ... = 0x......` lines below.

    /** color.branch.<slug> inks (docs/02 §2.2). Tints are computed by [tint], never typed. */
    object Branch {
        const val foundation = 0x6D4C41
        const val kotlin = 0x3B5BDB
        const val androidCore = 0x1B2F7A
        const val ui = 0xC2185B
        const val concurrency = 0x7A5A00
        /** Darkened from 2E7D4F on 20 Oct 2026: ink on its 12% tint was 4.30 (author answer 2, docs/13). */
        const val architectureData = 0x2C774B
        const val appNetworking = 0x01579B
        const val kmp = 0x4A7000
        const val dsa = 0xA84A00
        const val daa = 0x7B3FC4
        const val os = 0xB3261E
        /** Darkened from 00796B on 20 Oct 2026: ink on its 12% tint was 4.49. */
        const val networking = 0x007669
        const val testing = 0xC0392B
        const val profiling = 0x5E4AE3
        const val nativeNdk = 0x8A4B2A
        const val security = 0x37474F
        const val tools = 0x546E7A

        /** Share of the ink mixed into white for a container fill (docs/02 §2.2). */
        const val tintPercent = 12

        /** Branch slug (docs/00 §5) to ink, in tree order. */
        val inks: List<Pair<String, Int>> = listOf(
            "foundation" to foundation, "kotlin" to kotlin, "android-core" to androidCore, "ui" to ui,
            "concurrency" to concurrency, "architecture-data" to architectureData, "app-networking" to appNetworking,
            "kmp" to kmp, "dsa" to dsa, "daa" to daa, "os" to os, "networking" to networking, "testing" to testing,
            "profiling" to profiling, "native-ndk" to nativeNdk, "security" to security, "tools" to tools,
        )

        /** sRGB mix of [ink] into white: [pct] percent ink, the rest white. The same formula scripts/check-tokens.py uses. */
        fun tint(ink: Int, pct: Int = tintPercent): Int {
            fun mix(shift: Int): Int {
                val c = (ink shr shift) and 0xFF
                val white = 0xFF
                return ((c * pct + white * (100 - pct)) / 100.0).let { round(it).toInt() }
            }
            return (mix(16) shl 16) or (mix(8) shl 8) or mix(0)
        }
    }

    /** color.bough.* reuse a branch ink (docs/02 §2.2). */
    object Bough {
        const val roots = Branch.foundation
        const val kotlinAndroid = Branch.kotlin
        const val architectureKmp = Branch.architectureData
        const val csTheory = Branch.dsa
        const val craft = Branch.security
    }

    /** Chrome colours: text on an ink fill and the feedback set (docs/02 §2.4). */
    object Chrome {
        /** color.on-branch */ const val onBranch = 0xFFFFFF
        /** color.feedback.correct, darkened from 2E7D32 on 20 Oct 2026 (ink on tint was 4.36) */ const val feedbackCorrect = 0x2C7730
        /** color.feedback.wrong */ const val feedbackWrong = 0xB3261E
        /** color.feedback.hint */ const val feedbackHint = 0x7A5A00
        /** color.focus-ring default when no branch is active. */
        const val focusRingDefault = Branch.kotlin
        const val focusRingWidthPx = 3
        const val focusRingOffsetPx = 2
        /** State layers on buttons: share of the content colour laid over the fill (M3). */
        const val hoverLayerPercent = 6
        const val pressedLayerPercent = 8
        /** Tonal buttons darken their label ink by this share of black while hovered or pressed, to keep 4.5:1 (P1 review item 3). */
        const val tonalStateInkDarkenPercent = 10
        /** Chip swatch edge, px. */
        const val chipSwatchPx = 12
    }

    /** World colours as Int (docs/02 §2.3). The canvas and DOM chips read these directly. */
    object Leaf {
        /** color.leaf.shadow */ const val shadow = 0xD8DADF
        /** color.leaf.upnext */ const val upnext = 0xC9CDD4
        /** color.leaf.upnext.glow, drawn at [upnextGlowAlphaPercent] */ const val upnextGlow = 0xFFD54F
        const val upnextGlowAlphaPercent = 60
        /** Up-next pip drawn on the grey twigs: color.on-surface-muted, 3.5:1 against color.outline (the swatch above is too close to it). */
        const val upnextPip = Color.onSurfaceMuted
        /** color.leaf.easy */ const val easy = 0xA5D6A7
        /** color.leaf.medium */ const val medium = 0x43A047
        /** color.leaf.hard */ const val hard = 0x1B5E20
        /** color.leaf.done-mark */ const val doneMark = 0x000000
        /** color.fruit */ const val fruit = 0xE53935
    }

    /** shape.* (docs/02 §4.1), px. */
    object Shape {
        const val xs = 8
        const val md = 16
        const val xl = 28
        const val full = 999
        /** shape.blob: asymmetric radii 28 / 8 / 28 / 8. */
        const val blobLarge = 28
        const val blobSmall = 8
    }

    /** space.1 .. space.16 (docs/02 §4.2), px. */
    object Spacing {
        const val s1 = 4
        const val s2 = 8
        const val s3 = 12
        const val s4 = 16
        const val s6 = 24
        const val s8 = 32
        const val s12 = 48
        const val s16 = 64
        /** Minimum click target for chrome. */
        const val minTargetPx = 44
    }

    /** elevation.* (docs/02 §4.3). */
    object Elevation {
        const val borderPx = 1
        const val shadowOffsetYPx = 2
        const val shadowBlurPx = 8
        const val shadowAlphaPercent = 12
    }

    /** type.<name> rows (docs/02 §3): size px, line height px, weight. */
    object TypeScale {
        class Row(val sizePx: Int, val lineHeightPx: Int, val weight: Int)
        /** Emphasized variants add this to the weight. */
        const val emphasisWeightStep = 100
        val display = Row(48, 56, 700)
        val headline = Row(32, 40, 650)
        val title = Row(22, 30, 600)
        val body = Row(18, 30, 400)
        val label = Row(Type.labelSizePx, Type.labelLineHeightPx, Type.labelWeight)
        val code = Row(16, 26, 400)
    }

    /** layout.* (docs/02 §4.5), px. */
    object LayoutW {
        const val viewportMin = 1024
        const val lessonText = 544
        const val lessonWide = 720
        const val gutter = Spacing.s8
        /** SimFrame is 16:9: height = width * 9 / 16. */
        const val simAspectW = 16
        const val simAspectH = 9
    }

    /** Spring parameters (Compose Material 3 ExpressiveMotionTokens, docs/02 §4.4). Mass is 1. */
    object Spring {
        class Params(val dampingRatio: Double, val stiffness: Double)
        val spatial = Params(0.8, 380.0)
        val spatialFast = Params(0.6, 800.0)
        val spatialSlow = Params(0.8, 200.0)
        val effects = Params(1.0, 1600.0)
        val effectsFast = Params(1.0, 3800.0)
        val effectsSlow = Params(1.0, 800.0)
    }
}
