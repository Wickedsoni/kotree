# ADR 006 — Design reset: vector tree, bento topics, wide lesson page

Status: proposed · Date: 21 Oct 2026 · Decided by: the author (L32, live-site screenshots), specified by `architect`
Supersedes: `docs/02` §5 (pixel-tree rules) and the "pixelated" rows of §1.2, §6 (`TreeCanvas`, `LeafNode`) and §7; `docs/07` §7 rows "Layers", "Pixel crispness", "Offscreen cache"; `docs/03` §2.4 "quantised so the pixel limbs move in visible pixel steps" (the 9 split steps stay, only the reason changes); the wording of `docs/03` §6.2 (pre-L31).
⚠️ CONFLICT WITH DEFAULT D3: shadow leaves are now **drawn** (as a muted, locked look) from Stem level and are clickable, because L32 says "every stem and leaf clickable". Their URLs still return 404 (no page). See Open question Q1.

## Context

Author feedback from the live site (21 Oct 2026):

| # | Problem | Root cause found in the code |
|---|---|---|
| 1 | Tree unreadable; leaves look like twigs; labels overlap | 1-art-px raster (`render/LimbRaster.kt`, `TwigRaster.kt`) at `world.px` 4; leaves are 8-world-px squares the same black or gray as twigs; `nudgeApart` (`overlay/Labels.kt`) only moves labels vertically, at most 60 px, so dense stems still overlap |
| 2 | Nothing on the tree is clickable | Only `live`/`upnext` leaves get a button (`overlay/LeafNodes.kt`) and 0 leaves are live, so each branch has one target; stems only from Z2 in the focused branch; `/#<branch>` links from `/topics` and the lesson page are never read (no hash handling in `tree/`) |
| 3 | Zoom and pan lag | Every camera change writes Compose state (`TreeView.kt` `state.value = camera.state`), so every label and leaf button recomposes with ~20 inline style strings per frame; `nudgeApart` is O(n² × 16) per frame; `snappedScale` rounds the scale to whole device pixels, so zoom moves in visible jumps; wheel events are applied one by one, not merged per frame |
| 4 | `/topics` is a long list | `pages/Topics.kt` is one column of sections |
| 5 | Breadcrumb `Kotree / Foundation / Foundation / How Computers Work` | `LessonView.kt` puts bough + branch + stem; bough `roots` and branch `foundation` are both titled "Foundation"; the leaf itself is missing |
| 6 | Empty side margins on wide screens | Lesson `Main` is capped at `layout.lesson.wide` 720 px, nothing beside it |
| 7 | Tree popup is a plain white card: "Coming next. The author is learning this now." | `UpNextPopup` in `TreeView.kt`; wording predates L31 (autonomous publishing) |

Facts checked for this ADR (21 Oct 2026):
- M3 shape scale (Compose `ShapeTokens.kt`, androidx-main): ExtraSmall 4, Small 8, Medium 12, Large 16, **LargeIncreased 20**, ExtraLarge 28, **ExtraLargeIncreased 32**, **ExtraExtraLarge 48** dp, Full = circle.
- M3 elevation (`ElevationTokens.kt`): Level0 0, Level1 1, Level2 3, Level3 6, Level4 8, Level5 12 dp.
- M3 state layers (`StateTokens.kt`): hover 0.08, focus 0.10, pressed 0.10, dragged 0.16.
- A trackpad pinch arrives as a `wheel` event with `ctrlKey = true` (MDN `MouseEvent.ctrlKey`; Chrome since M35, Firefox 55; Safari 18 reported to do the same). `preventDefault()` stops page zoom.
- CSS media queries cannot read custom properties, so breakpoints are Kotlin constants, not CSS variables.

## Decision (summary)

1. **Tree = smooth vector Canvas 2D.** Limbs, stems, leaves, labels, hover and focus rings are all drawn on one canvas. Geometry (`tree/gen/*`) does not change, so `limbHash` `cecb906380417dd8` stays. No DOM node moves per frame.
2. **Accessible buttons stay**, as a visually hidden, focusable list of the current level's nodes, rebuilt only when the level, the focused branch or the focused stem changes. Focus draws the ring on the canvas and pans the node into view.
3. **One node card** (M3E) for leaf, stem and branch replaces `UpNextPopup`. Every click leads somewhere (live leaf → lesson; everything else → card with links).
4. **`/topics` = bento grid** of branch cards with their stems and leaves, read from the compiled tree data (`treeData`, the twin of `tree.json`; no fetch).
5. **Lesson page = content column + sticky side rail** (outline, stem progress, previous/next). Breadcrumb `Kotree / <Branch> / <Stem> / <Leaf>`.
6. **New tokens land first, in one commit by WP-B** (step B0); WP-A and WP-C only read them.

## Work packages and file ownership (disjoint)

Order: **B0** (WP-B, small, blocking) → then **WP-A, WP-B, WP-C in parallel**. A and C may start coding against the names in §Tokens before B0 merges, but must rebase on B0 before compiling.

| File (under `site/src/jsMain/kotlin/dev/kotree/site/` unless a path starts with `scripts/`) | Owner | Action |
|---|---|---|
| `Tokens.kt`, `TokenVars.kt`, `scripts/check-tokens.py` | **WP-B** (B0) | add §Tokens; nothing renamed |
| `ui/layout/SiteHeader.kt` | **WP-B** (B0 stub with the final signature, then full) | new |
| `ui/core/Chip.kt`, `ui/core/Button.kt`, `ui/Base.kt` | WP-B | status chip uses status tokens; signatures unchanged |
| `pages/Topics.kt`, new `ui/topics/BentoGrid.kt`, `ui/topics/BranchCard.kt`, `ui/topics/StemRow.kt` | WP-B | rewrite / new |
| `tree/TreeView.kt`, `tree/camera/*`, `tree/model/*` (read only), `tree/gen/LayoutCheck.kt` | **WP-A** | change |
| `tree/render/PixelCanvas.kt`, `LimbRaster.kt`, `TwigRaster.kt`, `Cache.kt` | WP-A | **delete** |
| new `tree/render/VectorRenderer.kt`, `tree/render/WoodLayer.kt`, `tree/render/LabelLayout.kt`, `tree/render/FrameLoop.kt`, `tree/hit/HitIndex.kt`, `tree/overlay/A11yNodes.kt`, `tree/DeepLink.kt`, `tree/PerfProbe.kt` | WP-A | new |
| `tree/overlay/Labels.kt`, `StemLabels.kt`, `LeafNodes.kt` | WP-A | delete (replaced by canvas labels + `A11yNodes.kt`) |
| `ui/layout/LeafPopup.kt` (keep signature for `KitchenSink.kt`), new `ui/layout/NodeCard.kt`, `ui/layout/ZoomControls.kt` | WP-A | change / new |
| `ui/lesson/LessonView.kt`, `ui/layout/Breadcrumb.kt`, `lessons/LessonLookup.kt`, new `ui/lesson/LessonRail.kt`, new `ui/lesson/Outline.kt`, `ui/lesson/LessonHeader.kt` | **WP-C** | change / new |
| `pages/KitchenSink.kt`, `pages/Lesson.kt`, `pages/Index.kt`, `AppEntry.kt`, `tree/gen/*` (except LayoutCheck), buildSrc | nobody | must still compile unchanged |

Shared-file rules:
- During the wave only WP-B edits `Tokens.kt` / `TokenVars.kt`. A token A or C finds missing goes in their reply. The orchestrator adds it in one follow-up commit; until then use the nearest existing token. No literals (`scripts/check-hardcoded.sh` must stay clean).
- Public signatures of `Breadcrumb`, `LeafPopup`, `Chip`, `Button`, `TopBar` stay source-compatible (new parameters only with defaults), so `KitchenSink.kt` compiles untouched.
- Cross-package contracts are fixed here (SiteHeader signature, hash grammar, anchor ids). Nobody changes them inside the wave.

### Contracts between packages

```kotlin
// ui/layout/SiteHeader.kt (WP-B; B0 ships this signature with a minimal body)
enum class NavItem { Tree, Topics, Lesson }
@Composable fun SiteHeader(current: NavItem, floating: Boolean = false, modifier: Modifier = Modifier)
// floating = true: over the tree canvas (surface fill, elevation.2, shape.full); false: in page flow.
// Content: "Kotree" wordmark (link "/"), nav pills Tree ("/") and Topics ("/topics") with aria-current,
// ProgressCounter(treeData.counts.live, treeData.counts.total).
```

Deep-link hash on `/` (WP-A reads; WP-B and WP-C write links):

| Hash | Tree action on load and on `hashchange` |
|---|---|
| `#<bough>` | fly to bough |
| `#<branch>` | fly to branch, open the branch card |
| `#<branch>/<stem-slug>` (= stem id) | fly to stem, open the stem card |
| `#<branch>/<stem-slug>/<leaf-slug>` (= leaf id) | fly to leaf, open the leaf card |
| unknown | ignore, fit the whole tree |

`/topics` anchors (WP-B writes ids; WP-A and WP-C link to them): branch card `id="<branch>"`, stem row `id="<branch>/<stem-slug>"` (an HTML id may contain `/`; the browser's own fragment scroll needs no selector escaping). Opening `/topics#<branch>/<stem>` expands that stem.

## Tokens (B0, WP-B creates them first)

Name rule: `docs/02` §3.1 (dots → dashes; no dash before a digit). Values that are colours are typed only in `Tokens.kt`. "CSS var" = also a `StyleVariable` in `TokenVars.kt`; "Kotlin" = a constant only (canvas, camera or media queries).

### Colour (status and tree)

| Token | Value | Kind | Use / check |
|---|---|---|---|
| `color.status.live` | `0x2C7730` (= `feedback.correct`; 5.54 on white, 4.71 on tint, measured) | CSS var + Kotlin | live chip text, live leaf stroke, card status |
| `color.status.live.container` | 12 % tint of the ink, computed | CSS var | live chip and card fills |
| `color.status.upnext` | `0x7A5A00` (= `concurrency` ink, passes on its tint) | CSS var + Kotlin | up-next chip text, up-next leaf stroke |
| `color.status.upnext.container` | 12 % tint, computed | CSS var | up-next chip fill, highlighted card row |
| `color.status.upnext.glow` | `0xFFD54F` (alias of `color.leaf.upnext.glow`) | Kotlin | up-next leaf fill and halo |
| `color.status.shadow` | `0x5F6368` (= `on-surface-muted`) | CSS var + Kotlin | "Coming soon" chip text, locked labels |
| `color.status.shadow.container` | 12 % tint, computed (`#ECECED`, about 5.1:1 by hand; `check-tokens.py` must confirm) | CSS var | shadow chip fill |
| `color.tree.stem` | `0x80868B` (about 3.7:1 on white, non-text ≥ 3:1) | Kotlin | stem lines; thinner than limbs, never black |
| `color.leaf.shadow.stroke` | `0x80868B` (same value) | Kotlin | outline of a locked (shadow) leaf |
| `color.leaf.easy/medium/hard`, `color.leaf.shadow`, `color.bark`, `color.outline` | unchanged | — | leaf fills, limbs (rule 8 grey/black limbs stays) |

"Status" words follow `docs/00` §7 (`live`, `upnext`, `shadow`). "Locked" is only the name of the look. `check-tokens.py` gains: each status ink on white and on its own container ≥ 4.5; `color.tree.stem` and `color.leaf.shadow.stroke` on white ≥ 3.0.

### State, shape, elevation, layout, tree tuning

| Token | Value | Kind | Source |
|---|---|---|---|
| `state.hover` / `state.focus` / `state.pressed` | 8 / 10 / 10 % of `on-surface` | Kotlin | M3 `StateTokens` (verified). The `Button` values (6/8) are not changed in this wave. |
| `shape.lg.increased` | 20 px | CSS var | M3 `CornerLargeIncreased` |
| `shape.xl.increased` | 32 px | CSS var | M3 `CornerExtraLargeIncreased` (card hover morph target, node card) |
| `shape.xxl` | 48 px | CSS var | M3 `CornerExtraExtraLarge` (Foundation hero card) |
| `elevation.3` | `0 6px 16px` black at 14 % | CSS var (`--elevation3`) | level dp from M3 `ElevationTokens` Level3 = 6; the CSS shadow is ours |
| `layout.page.max` | 1440 px | CSS var | topics grid and lesson page max width |
| `layout.rail.width` | 320 px | CSS var | lesson side rail |
| `layout.rail.min-viewport` | 1200 px | Kotlin (`Tokens.LayoutW.railMinViewport`) | rail shown from here (media query) |
| `layout.bento.col.min` | 280 px | CSS var | `repeat(auto-fill, minmax(280px, 1fr))`: 3 columns at 1024, 4 at 1440 and wider |
| `tree.leaf.min` / `tree.leaf.max` | 12 / 28 CSS px | Kotlin | drawn leaf size clamp on screen |
| `tree.stem.line.min` / `.max` | 1.5 / 3 CSS px | Kotlin | stem stroke width clamp |
| `tree.hit.cell` | 32 world px | Kotlin | hit-grid cell |
| `tree.label.buckets` | 8 per e-fold of `z` | Kotlin | label layout cache key |
| `tree.layer.margin` / `tree.layer.rescale` | 0.25 / 0.15 | Kotlin | wood layer margin and re-render tolerance |
| `tree.idle.crisp` | 120 ms | Kotlin | re-render crisp after motion stops |
| `camera.wheel.tau` | 90 ms | Kotlin | wheel smoothing time constant |
| `camera.pinch.sensitivity` | 0.01 per `deltaY` unit | Kotlin | ctrl+wheel (trackpad pinch) |
| `motion.card` | `motion.spatial` (0.8 / 380) for the card, `motion.effects` for the fade | Kotlin (existing `Spring`) | node card enter, shape morph |

Existing names are not renamed. Our `shape.xs` (8) and `shape.md` (16) equal M3 Small and Large; that mismatch is recorded here and left as is (renaming would touch every component).

## WP-A — Tree (`ui-engineer`, Sonnet)

**Rendering (`VectorRenderer.kt`).** Each `Polyline` becomes one filled tapered `Path2D` (offset left and right by half the width along the normals, round cap). It is built once per geometry and split step (4 boughs × 9 steps + trunk + roots + 1 per branch for stems), so a frame only fills cached paths. ⚠️ VERIFY: `org.w3c.dom.Path2D` and `CanvasRenderingContext2D.fill(path)` exist in the Kotlin/JS DOM bindings; else use `js("new Path2D()")` with a typed `external` wrapper. Smoothing stays on, no `image-rendering: pixelated`, backing size = CSS size × `devicePixelRatio`, `ctx.setTransform` per frame. `snappedScale` becomes a continuous `scaleAt(bounds, vp, z)`, so zoom is smooth.

Visual language (readability first):

| Node | Look | State cue without colour |
|---|---|---|
| Limb | tapered, `color.bark`; grey `color.outline` while the branch has no live leaf (docs/02 rule 8 kept) | width |
| Stem | thin line `color.tree.stem`, 1.5-3 px, ends in its leaf cluster | thin, never black |
| Leaf `live` | lens (vesica) shape along the twig, fill `leaf.easy/medium/hard`, 1.5 px stroke `status.live`; `hard` adds a centre vein | filled |
| Leaf `upnext` | lens, fill `status.upnext.glow`, stroke `status.upnext`, static 4 px halo (no idle frames; reduced motion is the same) | halo |
| Leaf `shadow` | lens, fill `leaf.shadow`, stroke `leaf.shadow.stroke` | pale outline |
| Hover | state layer `on-surface` at `state.hover` behind node and label; label weight +100 | — |
| Keyboard focus | 3 px ring `color.focus-ring` (branch ink), offset 2 px, around the hit area | ring |

Leaves are drawn at `tree.leaf.min`-`max` screen size, so they are always visible and clickable from Stem level.

**Labels (`LabelLayout.kt`).** Labels are drawn on the canvas with `font.ui` at `type.label` size (bough titles at `type.title`), with a white 3 px halo (`labelOutlinePx`), at a fixed screen size. Layout is greedy, in priority order: hovered/focused, then live, up-next, shadow, then data order. Each label tries four slots (E, W, N, S of its anchor, gap `space.1`) against placed labels and leaf hit rectangles. If no slot is free, the label is hidden; it is shown on hover or focus as a tooltip on top. The layout is computed for **all** labels of the level (not only visible ones), cached by (level, branch, stem, `floor(ln z × tree.label.buckets)`). Panning is a translation and never changes it, so labels do not jitter. Text widths come from `measureText` once per string, cleared after `document.fonts.ready` resolves (⚠️ VERIFY binding; else `asDynamic()`).

Semantic zoom (thresholds of `docs/03` §2.2 unchanged):

| Level | Labels drawn | Clickable |
|---|---|---|
| Canopy | 4 bough titles + Foundation | boughs, roots |
| Bough | branch titles of the current bough | branches |
| Branch | stem titles of the focused branch | stems, branch limb |
| Stem | stem title + leaf titles of the focused stem | every leaf of the stem (all states), stems |
| Leaf | every leaf title in view (after collision) | every leaf in view |

**Hit-testing (`HitIndex.kt`).** Built once per geometry. Parallel arrays (`kind`, node index, world x, y, radius, min level) plus a uniform grid (`tree.hit.cell`) of item indices; limbs are stored as segments. A query turns the pointer into world coordinates, scans the 3 × 3 cells, keeps items of the current level, and picks the nearest whose screen hit radius ≥ 12 px (24 px target) contains the point; label rectangles from the current layout count too. Hover runs at most once per frame.

**Input and frame loop (`FrameLoop.kt`, `camera/Input.kt`).** Handlers only record intent (summed wheel delta, pan delta, pointer position) and call `requestFrame()`, which schedules at most one `requestAnimationFrame`. Each frame: apply pan; move `ln z` toward the wheel target with `1 - exp(-dt / camera.wheel.tau)`; ctrl+wheel (pinch) applies at once with `camera.pinch.sensitivity`; then draw. The loop stops when there is no input, no fly and the smoothing has settled (idle = no frames, `docs/07` §7). `setPointerCapture` on `pointerdown`. A click is a `pointerup` within `dragThresholdPx`, then hit-test (the capture-phase click-swallow hack goes). Resizing uses a `ResizeObserver`. No allocation in the frame path: camera state lives in mutable fields and scratch `DoubleArray`s, with no `Vec`/`CameraState` per frame. Compose state is written only when `ViewState` (level, branch, stem, hovered id, focused id) changes.

**Wood layer (`WoodLayer.kt`).** Limbs, stems and leaves of the view plus `tree.layer.margin` are drawn into one offscreen `<canvas>` at scale s0. While `|s/s0 - 1| ≤ tree.layer.rescale` and the view stays inside the margin, the frame is a single scaled `drawImage`, then labels and states on top. Otherwise, and `tree.idle.crisp` after motion stops, it re-renders. **Simplest-first rule:** if direct drawing of the cached paths measures ≤ 4 ms p95 at DPR 2, WP-A may skip `WoodLayer.kt` and record the numbers in its reply.

**Accessible nodes (`A11yNodes.kt`).** Real `<button>`s, visually hidden (clip pattern, not `display: none`), in data order for the current level (table above), with `aria-label` "<title>, <live | coming next | coming soon>". Focus sets the canvas focus ring and pans the node into view if it is off screen; Enter/Space = click. Keys of `docs/03` §2.6 are unchanged. The list recomposes only on level, branch or stem change. `ZoomControls` (existing component) is placed bottom-right; `SiteHeader(NavItem.Tree, floating = true)` top-left. The old `HomeButton` and `TopicsLink` are removed.

**Node card (`NodeCard.kt`, replaces `UpNextPopup`).** One M3E card, `shape.xl.increased`, `elevation.2`, white surface with a branch-tint header band. Enter: scale 0.92 → 1 and fade with `motion.card` (cut under reduced motion). It is anchored next to the node (flips to stay on screen). `Esc` and the close button return focus to the node's button.

| Clicked | Card content | Primary action |
|---|---|---|
| Live leaf | context chips (Branch, Stem, both links to `/#…`), difficulty chip, status chip "Live", title (`type.title`), `summary` | filled button **Open lesson** → navigate to the leaf route `/<branch>/<stem>/<leaf>` |
| Up-next or shadow leaf | same chips, status chip "Up next" or "Coming soon", text: **"Not published yet. Being written in the order of the tree."** plus `stem.why` | tonal button **See all topics** → `/topics#<branch>/<stem>`; plus links to the nearest live leaves of the same stem (previous/next live), when they exist |
| Stem (label or twig) | stem title, `why`, "N of M grown", list of all its leaves with status chips; live ones are links to their lessons | camera flies to the stem; button **Open in topics** → `/topics#<branch>/<stem>` |
| Branch (label or limb) | branch title in ink, progress, stems list (each flies to the stem), up-next leaf line | camera flies to the branch; button **Open in topics** → `/topics#<branch>` |
| Bough / roots | no card | camera flies to the bough |

Wording rule (L31): no text says "the author is learning this"; nothing on the tree is a dead end.

**Deep links (`DeepLink.kt`)** implement the hash grammar above. **Perf probe (`PerfProbe.kt`)** is active only with `?perf=1`. It records draw ms and rAF deltas in preallocated `Float64Array`s (1,200 entries) and exposes `window.__kotreePerf` (p50, p95, max) and `window.__kotreeLabels()` (placed label rectangles). Off: one boolean check per frame.

## WP-B — Tokens, `/topics` bento grid, header (`ui-engineer`, Sonnet)

**B0 (first commit, blocking):** §Tokens in `Tokens.kt` + `TokenVars.kt`, `check-tokens.py` extended and passing, `SiteHeader.kt` stub with the final signature. Commit `feat: design-reset tokens (ADR 006)`.

**Bento grid (`pages/Topics.kt`, `ui/topics/*`).** Data: compiled `treeData` (no fetch). Page: `SiteHeader(NavItem.Topics)`, headline, one-line intro, legend of the three status chips. The grid is `display: grid`, `grid-template-columns: repeat(auto-fill, minmax(layout.bento.col.min, 1fr))`, gap `space.6`, `grid-auto-flow: dense`, max width `layout.page.max`. Each bough has a full-row header (`grid-column: 1 / -1`, `type.label`, bough ink).

| Card | Span | Shape | Colour roles |
|---|---|---|---|
| Foundation ("Start here") | 2 columns × 2 rows | `shape.xxl` | fully branch-tinted container, ink title, filled "Start here" button → first leaf or `/#foundation` |
| Branch with ≥ 10 stems | 2 columns | `shape.xl` | white surface, `elevation.1`, 4 px ink top band, tint header |
| Other branches | 1 column | `shape.xl` | same |

Card content: branch title (`type.title`, ink); "N of M grown" with a linear bar (ink on tint, `role="progressbar"` with values); an "Up next: <leaf>" row in `status.upnext.container`; then the stems as `StemRow`s. A row is a disclosure button (`aria-expanded`) with title, count and a strip of leaf dots (status swatches, decorative). The leaf list is rendered **only when open** (keeps DOM small): each leaf shows a status chip (text + swatch: Live / Up next / Coming soon) and difficulty text; live leaves link to their route. The stem holding the up-next leaf, or the one named in the URL hash, starts open. Footer link "See on the tree" → `/#<branch>`. Hover: shape morph `shape.xl` → `shape.xl.increased` and `elevation.3`, with `Spring.cssLinear` and `motion.spatial.fast` (none under reduced motion). Card ids follow the anchor contract. Avoid the "identical rounded cards" look of `docs/02` §1.5 by varying spans, shapes and the hero card.

**Header polish.** `SiteHeader` full body: wordmark + tagline (D14) at ≥ 1200 px, nav pills (tonal fill on the current item, 44 px targets), progress counter. Used by WP-A (tree), WP-B (topics), WP-C (lesson).

## WP-C — Lesson page (`ui-engineer`, Sonnet)

**Breadcrumb data (`LessonLookup.kt`).** `LeafInfo.crumbs` = `Kotree "/"` / `<branch.title> "/#<branch>"` / `<stem.title> "/#<branch>/<stem-slug>"` / `<leaf.title>` (current, `aria-current="page"`). No bough crumb. A guard drops a crumb whose label equals the previous one. `Breadcrumb.kt`: `onSelect` becomes optional (`null` = plain link navigation, the new default); the existing call shape still compiles. The same crumbs are used by `NotFoundContent` for a known shadow leaf.

**Layout (`LessonView.kt`, `LessonRail.kt`).** Page = `SiteHeader(NavItem.Lesson)`, breadcrumb, then a grid centred at `layout.page.max`. At viewport ≥ `layout.rail.min-viewport` it has two columns: content `minmax(0, layout.lesson.wide)` and rail `layout.rail.width`, gap `space.12`. Prose stays `layout.lesson.text` 544 px (the 62-72 characters rule, measured, `docs/02` §4.5); wide blocks stay 720. Below 1200 px the rail content moves under the lesson body in one column (no outline there). The rail is `position: sticky`, `top: space.8`, `max-height: calc(100vh - 2 × space.8)` with Silk `calc { }`, scrolls on its own. ⚠️ VERIFY the exact Silk syntax for a raw `min-width` media query in a `CssStyle` (else `Breakpoint` values set from `Tokens.LayoutW.railMinViewport`). `#kt-lesson-body` keeps its id and element, because `emitLessonPages` and `verifyLessonExport` match it.

Rail sections (each an M3E card, `shape.xl`, `elevation.1`):
1. **On this page** (`Outline.kt`): after the prose renders, read the `h2` elements inside `#kt-lesson-body`, give each an id `sec-<n>` if it has none, list them as links, and mark the current one with an `IntersectionObserver`. Smooth scroll, instant under reduced motion. Empty in the export snapshot, filled after hydration (no CLS: it is in the rail column).
2. **Stem progress:** "N of M grown in <stem>", every leaf of the stem in order with a status chip; live leaves are links, the current leaf has `aria-current`.
3. **Previous / Next:** tonal cards with label, leaf title and stem title (`previousLive`, `nextLive`; `comingNext` as text without a link).

The bottom-of-page previous/next stays (as the same cards) for readers who reach the end. "Back to the tree" → `/#<leaf id>`.

## Performance and bundle budget

| Item | Budget | Measured how |
|---|---|---|
| Canvas draw per frame (pan, zoom, fly) | ≤ 8 ms p95, target ≤ 4 ms, at 1440×900 DPR 1 and 2 | `__kotreePerf.draw` |
| Frame interval while moving | ≤ 16.7 ms p95 (`docs/07` §8) | `__kotreePerf.frame`; headless rAF timing is unreliable, so draw ms gates CI and frame interval is confirmed by eye on the author's laptop (Performance panel, 10 s script) |
| Idle | 0 frames after `tree.idle.crisp` + 1 frame | probe counter stays flat for 2 s |
| Click → card visible | ≤ 100 ms | Performance panel |
| Long tasks while zooming | none > 50 ms | `PerformanceObserver` long tasks in the probe |
| `/topics` DOM nodes, all stems closed | ≤ 1,500 | `document.querySelectorAll('*').length` |
| Lesson CLS | ≤ 0.05 | Lighthouse |

`kotree.js` is 333,619 B gzip (`docs/07` §8.1) against 600,000 B (L30). These are estimates, not measured:

| Package | Removes | Adds | Net estimate | Fail line |
|---|---|---|---|---|
| WP-A | raster + cache + 3 Compose overlays (about −8 to −12 KB) | renderer, layer, labels, hit index, frame loop, a11y list, node card, deep link, probe | +5 to +15 KB | +25 KB |
| WP-B | old topics list | ~40 new `StyleVariable`s + `setVariable`s, bento components, header | +6 to +12 KB | +15 KB |
| WP-C | — | crumbs, rail, outline, observer | +4 to +10 KB | +15 KB |
| **Total** | | | **about 349-371 KB** | **> 390,000 B fails the wave** (keeps room for 482 × 120 B live fields and sims) |

Each WP records `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` after a clean export (CI command of `docs/07` §3) in its reply.

## Acceptance checks

Run on the static export served locally (`kobweb run --env prod --layout static`). Screenshots come from headless Chrome through the reviewer's CDP approach (`docs/reviews/S3-acceptance.md`, `P3-acceptance.md`), saved to `docs/reviews/design-reset/`. Always: `scripts/check-hardcoded.sh` clean, `scripts/check-tokens.py` 0 failures, compile with no `w:` warnings, `KitchenSink.kt` compiles unchanged.

**WP-A**
- [ ] Screenshots at 1440×900 DPR 1 and 2: `/` z1, bough, kotlin z4, kotlin stem z8, z14, hover on a leaf, keyboard focus on a stem, each of the 4 card types. Edges are smooth (no pixel stair-steps); leaves, stems and limbs can be told apart in a grayscale copy of the z8 shot.
- [ ] `limbHash` printed on load is still `cecb906380417dd8`.
- [ ] No overlap: for every level shot, `__kotreeLabels()` rectangles are pairwise disjoint and disjoint from leaf hit rectangles (script asserts).
- [ ] Clickable: at Stem level every leaf of the focused stem (all 3 states) opens a card when its screen point is clicked (script clicks each); every stem label and twig opens the stem card and flies; every branch label and limb flies and opens the branch card; boughs fly.
- [ ] Never a dead end: a live leaf's **Open lesson** loads its route (synthetic live leaf on a scratch copy); an up-next/shadow card shows "Not published yet. Being written in the order of the tree.", a `/topics#<branch>/<stem>` link that expands that stem, and live-neighbour links when they exist; the text "author is learning" appears nowhere (`grep -r`).
- [ ] Card: M3E shape `shape.xl.increased`, status and difficulty chips with text, branch and stem chips that link, focus moves into the card and returns to the node on `Esc`; reduced motion = no scale animation.
- [ ] Keyboard: Tab reaches every node of the level; focus ring visible on canvas; the node pans into view; `Esc`, `+`, `-`, arrows and `Home` work as in `docs/03` §2.6.
- [ ] Deep links: `/#kotlin`, `/#kotlin/null-safety`, a leaf id each land on the right node with its card; an unknown hash fits the tree.
- [ ] Perf (`?perf=1`): 120 CDP wheel events plus a 2 s drag plus 20 ctrl+wheel (pinch) events: draw p95 ≤ 8 ms at DPR 1 and 2, no long task > 50 ms, idle frames 0 after settle. Zoom has no visible steps (scale continuous).

**WP-B**
- [ ] B0 merged first; `check-tokens.py` covers every new ink and stroke.
- [ ] `/topics` at 1024, 1440 and 1920 wide: 3, 4 and 4 columns; Foundation hero 2×2; bough headers full width; no horizontal scroll.
- [ ] Each card shows stems; opening a stem lists its leaves with a text status chip; live leaves link; `/topics#kotlin/null-safety` opens with that stem expanded and in view.
- [ ] DOM ≤ 1,500 nodes with all stems closed; every interactive element ≥ 44 px, visible focus ring, contrast ≥ 4.5:1.
- [ ] `SiteHeader` on `/` (floating), `/topics` and a lesson page; current item has `aria-current`.

**WP-C**
- [ ] Breadcrumb for a Foundation leaf reads `Kotree / Foundation / How Computers Work / <leaf title>`; no word repeats; the Branch and Stem crumbs open `/#foundation` and `/#foundation/<stem>` and the tree lands there (needs WP-A).
- [ ] At 1440 and 1920: rail visible, sticky while scrolling, outline lists the `h2`s and highlights the current one; at 1024: one column, stem progress and previous/next under the body.
- [ ] Stem progress lists every leaf of the stem with status; previous/next cards link to the right routes; up-next shows as text only.
- [ ] Export: `verifyLessonExport` passes on a scratch live leaf; the snapshot has `#kt-lesson-body`; CLS ≤ 0.05.
- [ ] Screenshots: preview route `/preview/<id>` of any draft leaf (dev server) at 1024, 1440, 1920.

## Risks

| Risk | Mitigation |
|---|---|
| Canvas text before fonts load measures wrong widths | re-measure and invalidate the label cache after `document.fonts.ready` |
| Hidden buttons and a canvas ring confuse screen-magnifier users | pan the focused node into view; ring ≥ 3 px in branch ink |
| Drawing shadow leaves contradicts D3 | flagged above, Q1; one boolean in `VectorRenderer` (`drawShadowLeaves`) reverts it |
| Kotlin/JS binding gaps (`Path2D`, `fonts.ready`, `ResizeObserver`) | ⚠️ VERIFY at compile; typed `external` wrappers in `tree/render` |
| Layout change rewrites every exported lesson HTML | 0 live leaves today; no cost now |
| Three agents and one tokens file | B0 first; only WP-B edits it in the wave; requests go via replies |
| Old screenshots and `docs/02` §5, `docs/07` §7, `docs/03` §2.3/§6.2 text go stale | `architect` aligns those docs after the wave (not the WPs); `housekeeper` updates status files |

## Rejected options

| Option | Why rejected |
|---|---|
| Keep the pixel raster, just bigger | Still unreadable at Canopy; integer snapping keeps the zoom steps |
| SVG tree (one DOM node per limb and leaf) | 700+ nodes moved per frame; the recompose lag in a new form |
| WebGL / a canvas library | Bundle and complexity for about 700 simple shapes |
| Keep positioned DOM label buttons | They are the measured lag source; collision layout then needs DOM reads |
| Labels nudged per frame | Jitter while panning; layout per zoom bucket is stable and cheaper |
| Wider prose column (624+ px) | Measured 77.8 characters per line, fails the 62-72 rule |
| Left and right rails | More layout for little gain; one rail fits from 1200 px |
| Native `<details>` with every leaf in the DOM on `/topics` | About 3,000 nodes |
| Tokens split per package | Three token files would drift; hard rule 2 wants one source |

## Consequences

- `tree/render/*` raster code and the three Compose overlays are deleted; `Tokens.World.px` remains only if `gen/` still reads it (geometry unchanged).
- The tree now uses `color.status.*` and `color.tree.stem`; `docs/02` §2.3 gets the stroke rows, and §5 is rewritten as "vector tree rules" by `architect`.
- `docs/03` §2.3 (shadow leaves drawn at Stem and Leaf level) and §6.2 (card wording, L31) are updated after Q1 is answered.

## Open questions

| # | Question | Default if no answer |
|---|---|---|
| Q1 | Draw shadow leaves (locked look) on the tree, replacing D3's "hidden until published"? | Yes, drawn (L32 "every leaf clickable") |
| Q2 | Up-next glow: static halo (no idle frames) or pulsing (frames while visible)? | Static |
| Q3 | Keep black bark, or a softer M3E neutral for limbs? | Black (L7), unchanged |
