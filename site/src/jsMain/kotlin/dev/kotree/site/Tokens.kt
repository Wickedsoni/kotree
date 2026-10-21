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
        val branchLandingZ: Map<String, Double> = mapOf("ui" to 6.0, "concurrency" to 5.0, "dsa" to 5.0, "networking" to 5.0, "native-ndk" to 5.0)
    }

    /** layoutReport estimates (docs/14 §5, docs/03 §2.6). */
    object LayoutCheck {
        /** Estimated label glyph width in CSS px per character. */
        const val LABEL_CHAR_PX = 8.0
        /** Horizontal label padding in CSS px, each side. */
        const val LABEL_PAD_PX = 6.0
        /** Minimum CSS gap between leaf hit areas at the stem level. */
        const val LEAF_GAP_MIN_PX = 16.0
        /** Minimum CSS gap between the leaf cluster boxes of two stems of one branch, at the stem level. */
        const val STEM_GAP_MIN_PX = 24.0
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
        const val BOUGH_LEN = 420.0
        const val BOUGH_LEN_JITTER = 30.0
        const val BOUGH_SPREAD_DEG = 66.0
        const val FORK_LOW = 0.18
        const val BEND = 50.0
        const val BRANCH_SPREAD = 460.0
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
        const val STEM_T_MIN = 0.250
        const val STEM_T_MAX = 0.95
        /** Twig length in world px, plus per-stem jitter (Rng "stem:<id>"). */
        const val STEM_LEN = 104.000
        const val STEM_LEN_JITTER = 6.0
        /** Extra twig length for every second same-side stem, so neighbouring labels stack. */
        const val STEM_LEN_STAGGER = 52.0
        /** Twig angle off the limb tangent, degrees, plus jitter. */
        const val STEM_ANGLE_DEG = 55.0
        const val STEM_ANGLE_JITTER_DEG = 8.0
        const val STEM_WIDTH = 2
        const val STEM_TIP_WIDTH = 1
        /** Gap between the leaf grid's edge and the label anchor (sideways), and the twig tip's vertical label offset. */
        const val STEM_LABEL_GAP = 32.000
        /** Leaf slots: fixed pitch between slots in a ceil(sqrt(n))-column grid at the twig tip; leaf edge, world px. */
        const val LEAF_PITCH = 14.000
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
        /** camera.wheel.tau: wheel smoothing time constant, ms. */
        const val wheelTauMs = 90
        /** camera.pinch.sensitivity: ctrl+wheel (trackpad pinch), e-folds per deltaY unit. */
        const val pinchSensitivity = 0.01
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
        /** motion.tooltip.delay: delay before a custom tooltip (ADR 007). */
        const val tooltipDelayMs = 500
        /** motion.autumn: ease of a leaf turning to autumn colour when marked done (ADR 008). */
        const val autumnMs = 900
        /** Play interval of a template sim (ADR 009 section 4); Slow doubles it. */
        const val simStepMs = 1200
        /** Opacity fade of a new sim state under reduced motion (ADR 009 section 4). */
        const val simFadeMs = 150
        /** A playing sim pauses when less than this share of its frame is visible (ADR 009 section 4). */
        const val simVisibleShare = 0.1
        /** Width of the bar that marks the current Try this step (ADR 009 section 3). */
        const val simStepBarPx = 4
    }

    /** Opacity values (ADR 009 section 2). */
    object Opacity {
        /** Mark `ghost`: a place that is not there yet, drawn dashed at 40 %. */
        const val ghost = 0.4
    }

    // ---- Tree v3 additions (ADR 008 WP1). Constants only; additions only. ----

    /** Autumn colours and tuning for done leaves (ADR 008 §Tokens). */
    object Autumn {
        /** color.leaf.autumn.yellow: ramp stop at t = 0.5 */ const val yellow = 0xF2C94C
        /** color.leaf.autumn.orange: ramp stop at t = 1 */ const val orange = 0xE8772E
        /** color.leaf.autumn.stroke: done-leaf stroke and dry veins */ const val stroke = 0x8A4B12
        /** color.limb.autumn: limb and twig tint target */ const val limb = 0x8A5A2B
        const val lutSteps = 32
        const val restMin = 0.75
        const val shimmer = 0.3
        const val limbMax = 0.5
        const val dryAt = 0.5
    }

    /** Sky, clouds and ground drawn on the tree canvas only (decision L35). */
    object Backdrop {
        /** color.sky.top */ const val skyTop = 0xEAF3FA
        /** color.sky.horizon */ const val skyHorizon = 0xFBFDFE
        /** color.cloud = color.surface at [cloudAlphaPercent] */ const val cloud = Color.surface
        const val cloudAlphaPercent = 55
        /** color.hills */ const val hills = 0xE4EEDF
        /** color.ground.grass */ const val grass = 0xE3F0D8
        /** color.ground.earth */ const val earth = 0xF3E9DC
        const val skyScale = 0.5
        const val cloudCount = 5
        const val grainAlphaPercent = 4
        const val grainTilePx = 128
        const val hillHeightWorld = 60
        const val grassWorld = 10
        const val hillSamples = 48
        /** Cloud centre y and half-width as shares of the sky canvas; ry = rx * [cloudFlatten]. */
        const val cloudCyMin = 0.06
        const val cloudCyMax = 0.40
        const val cloudRxMin = 0.08
        const val cloudRxMax = 0.16
        const val cloudFlatten = 0.35
        /** Grain: dots per tile, and hill wavelengths (world px) of the two sine terms. */
        const val grainDots = 600
        const val hillWave1Min = 500.0
        const val hillWave1Max = 900.0
        const val hillWave2Min = 220.0
        const val hillWave2Max = 420.0
        const val hillBase = 0.6
        const val hillMix = 0.4
    }

    /** Green canopy dots (ADR 008 §Canopy). Radii and sizes in world px unless noted. */
    object Canopy {
        /** color.canopy.light */ const val light = 0xC8E6C9
        /** color.canopy.mid */ const val mid = 0x9CCC65
        /** color.canopy.deep */ const val deep = 0x66BB6A
        const val alphaPercent = 70
        const val dotsPerBough = 260
        const val dotsPerBranch = 220
        const val dotsPerStem = 12
        const val maxDots = 7000
        const val boughRadius = 70
        const val limbRadius = 44
        const val stemRadius = 26
        const val dotRMin = 1.8
        const val dotRMax = 3.6
        const val texPxPerWorld = 1.5
        const val texMaxArea = 4_000_000
        const val fadeStartZ = 2.4
        const val fadeEndZ = 5.0
        const val leafFadeStartZ = 3.5
        const val leafFadeEndZ = 5.0
        const val leafAlphaBuckets = 8
        /** Dots of a bough sit on its centre points from this share of the points on; dots of a branch on limb points from this t. */
        const val boughAnchorFrom = 0.4
        const val limbAnchorFrom = 0.33
        /** Green tones, and autumn tones (yellow, mid, orange) of the dot buckets. */
        const val shades = 3
        const val autumnTones = 3
        /** Dots are drawn only above this alpha. */
        const val minDrawAlpha = 0.01
    }

    /** Leaf sway when the camera moves (ADR 008 §Motion). */
    object Sway {
        const val deg = 9
        const val periodMs = 1600
        const val riseMs = 150
        const val settleMs = 700
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
        /** shape.lg.increased: M3 CornerLargeIncreased (ADR 006). */
        const val lgIncreased = 20
        /** shape.xl.increased: M3 CornerExtraLargeIncreased (card hover morph target, node card). */
        const val xlIncreased = 32
        /** shape.xxl: M3 CornerExtraExtraLarge (Foundation hero card). */
        const val xxl = 48
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
        /** space.inset.card / .panel (ADR 007): padding of cards and of rail / sheet panels. */
        const val insetCard = s6
        const val insetPanel = s4
        /** space.stack.page / .section: header to first content; between page sections. */
        const val stackPage = s8
        const val stackSection = s12
        /** space.gap.grid: gap of every grid. */
        const val gapGrid = s6
    }

    /** elevation.* (docs/02 §4.3). */
    object Elevation {
        const val borderPx = 1
        const val shadowOffsetYPx = 2
        const val shadowBlurPx = 8
        const val shadowAlphaPercent = 12
        /** elevation.3 (ADR 006): M3 Level3 = 6 dp; the CSS shadow is ours. */
        const val level3OffsetYPx = 6
        const val level3BlurPx = 16
        const val level3AlphaPercent = 14
    }

    /** color.status.* (ADR 006 §Tokens). Inks; containers are computed with [Branch.tint]. Hex typed here only. */
    object Status {
        /** color.status.live (= feedback.correct) */ const val live = 0x2C7730
        /** color.status.upnext (= concurrency ink) */ const val upnext = 0x7A5A00
        /** color.status.upnext.glow (alias of color.leaf.upnext.glow) */ const val upnextGlow = Leaf.upnextGlow
        /** color.status.shadow (= on-surface-muted) */ const val shadow = 0x5F6368
        /** color.tree.stem: stem lines, non-text 3:1 on white. */ const val treeStem = 0x80868B
        /** color.leaf.shadow.stroke: outline of a locked (shadow) leaf. */ const val leafShadowStroke = 0x80868B
    }

    /** state.* layers (M3 StateTokens): share of on-surface laid over a surface. */
    object StateLayer {
        const val hoverPercent = 8
        const val focusPercent = 10
        const val pressedPercent = 10
    }

    /** tree.* tuning for the vector canvas (ADR 006). Kotlin only. */
    object Tree {
        /** tree.leaf.min / max: drawn leaf size clamp, CSS px. */
        const val leafMinPx = 12
        const val leafMaxPx = 28
        /** tree.stem.line.min / max: stem stroke width clamp, CSS px. */
        const val stemLineMinPx = 1.5
        const val stemLineMaxPx = 3.0
        /** tree.hit.cell: hit-grid cell, world px. */
        const val hitCell = 32
        /** tree.label.buckets: label layout cache buckets per e-fold of z. */
        const val labelBuckets = 8
        /** tree.layer.margin / tree.layer.rescale: wood layer margin and re-render tolerance. */
        const val layerMargin = 0.25
        const val layerRescale = 0.15
        /** tree.idle.crisp: re-render crisp after motion stops, ms. */
        const val idleCrispMs = 120
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
        /** type.title.small / type.body.small (ADR 007). */
        val titleSmall = Row(16, 24, 600)
        val bodySmall = Row(16, 24, 400)
    }

    /** layout.* (docs/02 §4.5), px. */
    object LayoutW {
        const val viewportMin = 1024
        const val lessonText = 544
        const val lessonWide = 720
        const val gutter = Spacing.s8
        /** layout.page.max: topics grid and lesson page max width. */
        const val pageMax = 1440
        /** layout.rail.width: lesson side rail. */
        const val railWidth = 320
        /** layout.rail.min-viewport: rail shown from here (media query, so Kotlin only). */
        const val railMinViewport = 1200
        /** layout.bento.col.min: minmax() lower bound of a bento column. */
        const val bentoColMin = 280
        /** layout.header.height: sticky header, scroll padding, rail top (M3 small top app bar). */
        const val headerHeight = 64
        /** layout.bp.*: Android window size classes (ADR 007). `bpLarge` is the same value as [railMinViewport]. */
        const val bpExpanded = 840
        const val bpLarge = railMinViewport
        const val bpXlarge = 1600
        /** layout.nav.width: lesson left stem navigation. */
        const val navWidth = 256
        /** layout.dialog.width: tree node card and side sheet. */
        const val dialogWidth = 400
        /** layout.topics.cols: branch grid columns below expanded / expanded to large / from large. */
        const val topicsColsCompact = 1
        const val topicsColsMedium = 2
        const val topicsColsWide = 4
        /** layout.measure.short, in ch. */
        const val measureShortCh = 60
        /** SimFrame is 16:9: height = width * 9 / 16. */
        const val simAspectW = 16
        const val simAspectH = 9
    }

    /** scrollbar.* (ADR 007). Colours reuse existing tokens: thumb = color.tree.stem, hover = on-surface-muted, active = on-surface. */
    object Scrollbar {
        const val thumb = Status.treeStem
        const val thumbHover = Color.onSurfaceMuted
        const val thumbActive = Color.onSurface
        /** scrollbar.size: gutter. */
        const val sizePx = 12
        /** scrollbar.inset / .inset.hover: transparent border, so the thumb is 6 px (8 px on hover). */
        const val insetPx = 3
        const val insetHoverPx = 2
        /** scrollbar.thumb.min: minimum thumb length. */
        const val thumbMinPx = 48
    }

    /** color.skeleton and color.scrim (ADR 007): on-surface mixed into white, and on-surface at an alpha. */
    object Overlay {
        const val skeletonPercent = 6
        /** M3 ScrimTokens.ContainerOpacity (0.32). */
        const val scrimAlphaPercent = 32
        val skeleton: Int get() = Branch.tint(Color.onSurface, skeletonPercent)
    }

    /** size.* (ADR 007), px. */
    object Size {
        /** size.chip.compact: non-interactive status chip height. */
        const val chipCompactPx = 24
        /** size.status.icon: glyph edge, inside a [statusIconBoxPx] box. */
        const val statusIconPx = 12
        const val statusIconBoxPx = 24
        /** size.meter: height of the progress bar. */
        const val meterPx = 8
        /** Last skeleton bar width, percent. */
        const val skeletonLastPercent = 60
        /** Status icon ring width. */
        const val statusRingPx = 2
        /** Segmented bough filter and disclosure rows reuse [Spacing.minTargetPx]. */
        const val skipLinkInsetPx = Spacing.s4
    }

    /** z.* (ADR 007). */
    object Z {
        const val header = 10
        const val sheet = 20
        const val skip = 30
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
