# ADR 008 (request): Tree v3 — airy layout, canopy dots, backdrop, autumn tracker

Status: requested by the author on 21 Oct 2026; `architect` writes the plan below this brief, then `ui-engineer` builds. Not started.
Author reference images (described): (1) a huge banyan tree with a dense round green canopy, thick trunk and hanging roots, dramatic cloudy sky, red earth and grass; (2) the Kotree sketch: black trunk, roots labelled Foundation, four boughs (Kotlin and Android, Architecture and KMP, CS theory, Craft).

## Author's requests (verbatim intent)
1. **More space.** Increase the gap between stems and leaves, and between branches. Leaves and stems must have "plenty of space"; nothing crowded.
2. **Unzoomed tree looks empty.** When zoomed out, add many small dotted leaves (a soft canopy of dots) so the tree reads as a full, lush tree like the banyan photo. Dots fade out and real leaf nodes (clickable) fade in as the camera zooms in (semantic zoom; no pop).
3. **Backdrop.** The background is plain white and boring. Add a tasteful design (inspired by the banyan photo: soft sky gradient, light clouds, distant hills or ground, subtle texture). Keep the page readable; Hard rule 2 (white background, no dark mode) is relaxed by the author for the tree canvas only, via a new decision L35 recorded by the architect in docs/00-decisions.md. Lesson and topics pages stay white.
4. **Progress tracker with autumn effect.** When the author marks a topic (leaf) as done, that leaf transitions from green to autumn (yellow then orange, dried look), smoothly animated. The more topics done, the more autumn the canopy: branches and the unzoomed dot-canopy tint by the fraction done. Marking is a click on the leaf's sheet ("Mark as done" / "Undo"). State is stored in the browser (localStorage, wrapped in try/catch, works without it), keyed by leaf path; no login, no server. Include a small legend and a counter ("N of M done"). Done is separate from "live": only live leaves can be marked.

5. **Motion while zooming (added by the author).** As the camera zooms in or out, done leaves visibly shift colour (green to yellow to orange, with a gentle sway or flutter), while leaves not marked done stay green and still. The motion is tied to zoom/pan activity (and a short ease when a leaf is first marked done), not a constant animation loop, so idle CPU stays near zero. Reduced-motion: no sway, colours switch instantly.

## Constraints
- Keep smooth zoom (frame budget in ADR 006/007); dots are drawn from a precomputed buffer or an offscreen cache, not per-frame random.
- Everything token-driven (Tokens.kt); autumn palette added as tokens. Respect prefers-reduced-motion (no animated transition, instant colour).
- Layout changes must keep `LayoutCheck.kt` invariants and the GeometryHash seed stable per decision.
- kotree.js gzip budget 600,000 B (wave-check). Last measured 334,045 B.
- a11y: done state exposed in A11yNodes (aria text), not by colour alone (also a check-mark glyph in the sheet).
- Verification: GitHub Actions build (validateContent + export + wave-check). One on-demand build at the end (CLAUDE.md CI rule).

---

## Plan (architect, 21 Oct 2026)

Status: proposed · Builder: `ui-engineer` (Sonnet), **one WP per message, in order WP0 → WP6**. Every WP must: compile with no `w:` warnings, keep `scripts/check-hardcoded.sh` and `scripts/check-tokens.py` clean, pass the tests that exist so far, keep `KitchenSink.kt` compiling unchanged, and report `kotree.js` gzip. Then one on-demand CI build at the end (`gh workflow run build.yml --ref <branch>`).
Decision row: **L35** in `docs/00` (backdrop on the tree canvas only). This plan also implements **D10** (visitor checkmarks in localStorage). L3 is unchanged: each visitor's marks live only in their own browser.
Paths: K = `site/src/jsMain/kotlin/dev/kotree/site/`, T = `site/src/jsTest/kotlin/dev/kotree/site/`.

### Decisions (summary)

1. **Space** comes only from `Tokens.Gen` constants plus stricter `LayoutCheck` rules. The seed `Tokens.World.seed` (20261017) stays the same. `limbHash` and `fullHash` change **once, on purpose, in WP2**. WP2 records the new values here and in `docs/07` §8.1, and a test pins them. This replaces the "limbHash stays `cecb906380417dd8`" rule of ADR 006/007.
2. **Canopy dots**: a deterministic buffer built from **new** Rng streams (`canopy-bough:<bough>`, `canopy:<branch>`; existing streams are untouched). It is rasterised once into an offscreen texture. Each frame draws it with one `drawImage`, at an alpha set by zoom.
3. **Backdrop** is drawn on the canvas: the sky in screen space (a small cached offscreen canvas) and hills plus ground in world space (cached `Path2D`). The page `body` and every other page stay white (L35).
4. **Done state**: a `DoneStore` over a 2-method `KeyValue` interface, using one localStorage key. Only `live` leaves can be marked.
5. **Autumn colour**: each leaf takes its colour from a precomputed CSS-string lookup table (LUT). The fraction done per branch tints that branch's dots, twigs and limb.
6. **Motion**: an activity envelope driven by camera movement. Frames run only while the envelope or a mark-done ease is non-zero.

### Work packages (owner of every file: `ui-engineer`, one WP at a time, so no two edits ever overlap)

| WP | Scope | Files | Why the build stays green |
|---|---|---|---|
| **WP0** Test harness + CI | jsTest source set; first test; CI steps | `site/build.gradle.kts` (`jsTest.dependencies { implementation(kotlin("test")) }`), new T`tree/GeometryTest.kt`, `.github/workflows/build.yml` (before the gate script, add the steps `./gradlew :site:jsBrowserTest`, `python3 scripts/check-tokens.py`, `bash scripts/check-hardcoded.sh`; confirm each exits non-zero on failure) | no product code changes. Tests: running `generate` twice gives equal hashes; `limbHash == "cecb906380417dd8"`; `fullHash` == today's value (paste it from the first run); last line of `layoutReport` == `layoutReport: PASS` at 1280×800 and 1440×900 |
| **WP1** Tokens | every token in §Tokens; contrast rules | `K/Tokens.kt` (new objects `Autumn`, `Backdrop`, `Canopy`, `Sway`; additions only), `scripts/check-tokens.py` | constants only, nothing reads them yet |
| **WP2** Airy layout | §Spacing | `K/Tokens.kt` (`Gen`, `LayoutCheck`, `Zoom.branchLandingZ` only), `K/tree/gen/LayoutCheck.kt`, T`tree/GeometryTest.kt` (new pinned hashes) | `layoutReport` PASS is required; the tuning loop below ends at the first PASS |
| **WP3** Backdrop | §Backdrop | new `K/tree/render/Backdrop.kt`; `K/tree/render/VectorRenderer.kt` (the surface fill becomes the backdrop) | tree inks keep 3:1 on the backdrop (enforced by `check-tokens.py`) |
| **WP4** Canopy dots + LOD | §Canopy | new `K/tree/gen/CanopyDots.kt` (pure), new `K/tree/render/CanopyLayer.kt`, `VectorRenderer.kt`, `K/tree/render/WoodLayer.kt` (leaf alpha bucket added to the layer key and to `paintWood`), new T`tree/CanopyDotsTest.kt` | every branch fraction is 0 for now (green only) |
| **WP5** Done store + UI + static autumn | §Progress, §Autumn (no animation) | new `K/progress/DoneStore.kt`, new `K/ui/layout/ProgressLegend.kt`, `K/ui/layout/NodeCard.kt` (new params with defaults), `K/tree/overlay/A11yNodes.kt`, `K/tree/TreeView.kt`, `VectorRenderer.kt`, `CanopyLayer.kt`, new `K/tree/render/AutumnColor.kt`, new T`progress/DoneStoreTest.kt`, T`tree/AutumnColorTest.kt` | done leaves are drawn at their resting autumn colour inside the wood layer; the layer key gains `progressVersion` |
| **WP6** Motion | §Motion | new `K/tree/render/AutumnMotion.kt`, `VectorRenderer.kt` (live pass), `K/tree/render/FrameLoop.kt` (passes `now`; `more ||= renderer.motionActive()`), new T`tree/AutumnMotionTest.kt` | reduced motion and idle tests pass |
| WP7 (not ui-engineer) | docs alignment (`docs/02` §2.3 autumn and backdrop rows, `docs/03` progress, `docs/07` §8.1), then the reviewer runs the acceptance checks | `architect`, `reviewer`, `housekeeper` | — |

⚠️ VERIFY (WP0): `configAsKobwebApplication` sets up a `js { browser() }` target, so `jsBrowserTest` (Karma, headless Chrome by default) should exist, and `ubuntu-latest` should ship Chrome. Kotlin's docs mark Karma as deprecated. Fallback 1: the experimental `browser { test { chromium() } }` DSL (Playwright). Fallback 2: `js { nodejs() }` + `jsNodeTest`. Every planned test is pure Kotlin (no DOM) so that fallback 2 works.

### Tokens (WP1; colour hex typed only in `Tokens.kt`; Kotlin constants, no CSS vars except `ProgressLegend`, which uses existing vars plus the two autumn swatches through `cssColor`)

| Token | Value (start) | Use |
|---|---|---|
| `color.leaf.autumn.yellow` / `.orange` | `0xF2C94C` / `0xE8772E` | ramp stops at t = 0.5 / 1 |
| `color.leaf.autumn.stroke` | `0x8A4B12` | done-leaf stroke (t = 1) and dry veins |
| `color.limb.autumn` | `0x8A5A2B` | limb and twig tint target |
| `color.sky.top` / `.horizon` | `0xEAF3FA` / `0xFBFDFE` | sky gradient |
| `color.cloud` | = `color.surface`, alpha `Backdrop.cloudAlphaPercent` 55 | clouds |
| `color.hills` / `color.ground.grass` / `color.ground.earth` | `0xE4EEDF` / `0xE3F0D8` / `0xF3E9DC` | world-space ground |
| `color.canopy.light` / `.mid` / `.deep` | `0xC8E6C9` / `0x9CCC65` / `0x66BB6A`, alpha `Canopy.alphaPercent` 70 | green dots |
| `Backdrop.skyScale` 0.5 · `cloudCount` 5 · `grainAlphaPercent` 4 · `grainTilePx` 128 · `hillHeightWorld` 60 · `grassWorld` 10 · `hillSamples` 48 | | §Backdrop |
| `Canopy.dotsPerBough` 260 · `dotsPerBranch` 220 · `dotsPerStem` 12 · `maxDots` 7000 · `boughRadius` 70 · `limbRadius` 44 · `stemRadius` 26 · `dotRMin` 1.8 · `dotRMax` 3.6 (world px) · `texPxPerWorld` 1.5 · `texMaxArea` 4,000,000 · `fadeStartZ` 2.4 · `fadeEndZ` 5.0 · `leafFadeStartZ` 3.5 · `leafFadeEndZ` 5.0 · `leafAlphaBuckets` 8 | | §Canopy |
| `Autumn.lutSteps` 32 · `restMin` 0.75 · `shimmer` 0.3 · `limbMax` 0.5 · `dryAt` 0.5 | | §Autumn |
| `Sway.deg` 9 · `periodMs` 1600 · `riseMs` 150 · `settleMs` 700 · `Motion.autumnMs` 900 | | §Motion |

New `check-tokens.py` rules: each of `tree.stem`, `leaf.shadow.stroke`, `status.live`, `status.upnext`, `leaf.autumn.stroke`, `bark` must reach **≥ 3.0:1** on each of `sky.top`, `sky.horizon`, `hills`, `ground.grass`, `ground.earth`. The same holds for the 50 % sRGB mix of `tree.stem` with `limb.autumn`. Hand-computed values are 3.07 to 3.28 for `tree.stem`, which is tight. If one fails, move that backdrop colour toward white; never darken the inks.

### Spacing (WP2)

Start values. Only these change:

| `Tokens.Gen` | now | new |
|---|---|---|
| `BRANCH_SPREAD` | 125 | 165 |
| `BOUGH_SPREAD_DEG` | 50 | 56 |
| `BOUGH_LEN` | 340 | 360 |
| `STEM_T_MIN` | 0.25 | 0.20 |
| `STEM_LEN` / `STEM_LEN_STAGGER` | 36 / 20 | 52 / 32 |
| `STEM_LABEL_GAP` | 12 | 16 |
| `LEAF_PITCH` (`LEAF_SIZE` stays 8) | 12 | 18 |

New `LayoutCheck` invariants. All are checked at landing z and at `stemEnter`, like today:
- `LEAF_GAP_MIN_PX` goes from 8 to **16**.
- New `STEM_GAP_MIN_PX` = 24: the leaf cluster box of each stem (the union of its leaf hit boxes) keeps this gap to every other stem's cluster in the same branch.
- New cross-branch rule at `stemEnter`: leaf hit boxes of different branches in the same bough keep a gap ≥ `LEAF_GAP_MIN_PX`, and no stem label overlaps another branch's leaf box.

Tuning loop: if `layoutReport` fails, raise `STEM_LEN_STAGGER` by 4, then `BRANCH_SPREAD` by 10, then the branch's `Zoom.branchLandingZ` by 0.5. Stop at the first PASS. Record the final table and both new hashes here.

### Backdrop (WP3, `Backdrop.kt`)

- **Sky** is an offscreen canvas of size CSS viewport × `skyScale`. It is rebuilt only when the viewport size changes. It holds a vertical linear gradient from `sky.top` to `sky.horizon`. It also holds `cloudCount` clouds from `Rng(seed, "cloud:<k>")`: cx in [0, 1]·w, cy in [0.06, 0.40]·h, rx in [0.08, 0.16]·w; each cloud is 3 overlapping ellipses in `color.cloud`. A grain pass follows: a 128 px tile of random black dots at alpha ≤ `grainAlphaPercent` (`Rng(seed, "grain")`), applied once with `createPattern`. Per frame: `drawImage(sky, 0, 0, vw, vh)`.
- **Ground** is in world space. `horizonY` = the trunk's first point y. The hills are one `Path2D` built once from x = `bounds.minX − W` to `bounds.maxX + W` over `hillSamples` samples, with y = horizonY − hillH·(0.5 + 0.5 sin(f1·x + φ1))·(0.6 + 0.4 sin(f2·x + φ2)), where f and φ come from `Rng(seed, "hills")`. Per frame, under the world transform, fill the hills, then a grass band (`grassWorld` tall), then the earth down to the bottom of the view. Each rect is clamped to the visible world rect.
- **Order per frame**: sky → hills/grass/earth → canopy texture → wood layer → live pass (WP6) → hover, labels, focus. The wood layer stays transparent. Labels keep their white halo, so text contrast does not change.

### Canopy (WP4)

The buffer is built by `canopyDots(g: TreeGeometry, seed: Long): CanopyDots` (pure):

```kotlin
class CanopyDots(val n: Int, val x: DoubleArray, val y: DoubleArray, val r: DoubleArray,
                 val branch: IntArray,   // index into branchIds (geometry order, Foundation excluded)
                 val shade: IntArray,    // 0..2 green tone
                 val turn: DoubleArray,  // [0,1): dot is autumn when turn < fraction done of its branch
                 val warm: DoubleArray,  // [0,1): 0 yellow .. 1 orange
                 val branchIds: Array<String>, val bounds: Bounds)
```

- Per non-roots bough: `Rng(seed, "canopy-bough:<id>")`. The anchors are the bough's centre points with index ≥ 40 % of `POINTS`. Place `dotsPerBough` dots in a disc of radius `boughRadius`; each takes a random branch of its bough.
- Per non-Foundation branch: `Rng(seed, "canopy:<id>")`. The anchors are the limb points with t ≥ 0.33 (radius `limbRadius`) plus every stem tip (radius `stemRadius`). The count is `dotsPerBranch + dotsPerStem × stems`.
- Each dot draws, in this fixed order: anchor index, u1, u2, r ∈ [`dotRMin`, `dotRMax`], shade, turn, warm. Its position is ρ = R·√u1, θ = 2π·u2. A dot below `horizonY` is skipped, but its draws are still consumed. If the total exceeds `maxDots`, every count is scaled by `maxDots/total` (floor) before sampling. The estimate is about 6,650 dots.
- **Texture** (`CanopyLayer`): one offscreen canvas over `bounds` at k = min(`texPxPerWorld`, √(`texMaxArea`/area)). It holds 6 `Path2D` buckets: green 0..2 by shade when `turn ≥ f_b`, else autumn bucket 3 + min(2, ⌊warm·3⌋) with colours mix(Y, O, 0 / 0.5 / 1). The layer is rebuilt lazily on the next frame with α_dots > 0 after `invalidate()`, which is called on a progress change.
- **Level of detail**, from z only (no timer, so reduced motion behaves the same): α_dots = 1 − smoothstep((z − `fadeStartZ`)/(`fadeEndZ` − `fadeStartZ`)). When α_dots ≥ 0.01: set `globalAlpha = α_dots`, draw the texture once, then set alpha back to 1. α_leaf = smoothstep((z − `leafFadeStartZ`)/(`leafFadeEndZ` − `leafFadeStartZ`)), rounded to `1/leafAlphaBuckets`, applied as `globalAlpha` in `paintLeaves` and added to the `WoodLayer` key. That gives at most 8 layer re-renders across the band.
- Dots are decorative: no hit-test entries and no a11y nodes. The leaves' clickable level is unchanged (`LEVEL_STEM`).

### Progress (WP5, `progress/DoneStore.kt`)

```kotlin
const val DONE_KEY = "kotree.done.v1"            // value: leaf ids, sorted, joined by '\n' (no JSON to parse)
interface KeyValue { fun read(key: String): String?; fun write(key: String, value: String?) }  // null = remove
class LocalStorageKv : KeyValue   // every access to kotlinx.browser.localStorage (property, getItem, setItem,
                                  // removeItem) inside try/catch; after the first failure, uses an in-memory map
class MemoryKv : KeyValue
class DoneStore(private val kv: KeyValue, private val liveIds: Set<String>) {
    fun isDone(id: String): Boolean
    fun setDone(id: String, done: Boolean): Boolean   // false and no write when id is not live
    fun doneIds(): Set<String>
    val count: Int
    fun reload()                                      // re-read kv (storage event from another tab)
}
```

Loading keeps only ids in `liveIds`. Garbage, duplicates and unknown ids are dropped and rewritten on the next write. The id = the leaf id = `path` without its leading `/`, which is stable by hard rule 6.

`TreeView.kt` sets the store up:
- It builds the store once with `liveIds` from `treeData`, plus `progressVersion = mutableStateOf(0)`.
- A toggle runs `store.setDone`, then `renderer.onProgress(leafId, animate = true)`, then `progressVersion++` and `loop.requestFrame()`.
- A `storage` event with `key == DONE_KEY` runs `store.reload()` and `renderer.onProgress(null, animate = false)`. The listener is removed on dispose.

UI changes:
- **NodeCard**: `LeafNodeCard(..., done: Boolean = false, onToggleDone: (() -> Unit)? = null)`, with source-compatible defaults. Only for live leaves: a tonal toggle in the footer before the primary button, reading **Mark as done** / **Undo**, with `aria-pressed`. Focus stays on it after a toggle. When done, the chip row gains a compact chip "✓ Done" (the glyph is `aria-hidden`, the text carries the meaning).
- **ProgressLegend** `(done: Int, live: Int)`: a floating surface pill at bottom-left (inset `space.4`, `shape.full`, `elevation.2`). It shows a swatch "Not done" (`leaf.medium`), a yellow→orange swatch "Done", and "N of M done" (tabular nums, `role=status`, `aria-live=polite`). Swatches are `aria-hidden`. It is not rendered when `live == 0`.
- **A11yNodes**: gains the params `isDone: (String) -> Boolean` and `progressVersion: State<Int>`, and the list is remembered on (view, version). A done live leaf reads "<title>, live, done". A stem or branch with k > 0 done adds ", k done".

### Autumn (WP5 static, WP6 animated; `AutumnColor.kt`)

- `ramp(base, t)`: for t ≤ 0.5, `mix(base, Y, t/0.5)`; else `mix(Y, O, (t − 0.5)/0.5)`. The mix is per channel in sRGB, rounded, the same formula as `Tokens.Branch.tint`. The stroke is `mix(status.live, autumn.stroke, t)`.
- LUT, built once: `fill[difficulty][k]` and `stroke[k]` as CSS strings, k = 0..`lutSteps`−1, so nothing is allocated per frame.
- Per leaf, from `Rng(seed, "autumn:<leafId>")`: rest_i = `restMin` + (1 − `restMin`)·u. The resting colour is amber to orange, never the pure yellow of the up-next glow. The phase is φ_i = 2π·u′.
- Dry cue (not colour alone): when t ≥ `dryAt`, stroke a `dryVeins` `Path2D` (midrib ±0.36 plus two side veins) in the stroke colour. The hard-leaf white vein is skipped on done leaves.
- Branch fraction f_b = done_b / live_b, or 0 when live_b = 0. The limb colour of branch b is `mix(ink, limb.autumn, f_b·limbMax)`; `Piece.color` becomes a `var`, recolouring that branch's piece in every cached split step on change. The twig stroke per branch is `mix(tree.stem, limb.autumn, f_b·limbMax)`; `twigPaths` are already per branch. The dots follow §Canopy.

### Motion (WP6, `AutumnMotion.kt`)

- **Envelope A ∈ [0, 1]**, once per frame: `moving` = camera z, cx or cy differs exactly from the last frame, or a fly is active. Then A = moving ? min(1, A + dt/`riseMs`) : max(0, A − dt/`settleMs`). Under reduced motion A ≡ 0. One `MediaQueryList` for `(prefers-reduced-motion: reduce)` is cached and its `.matches` read per frame (no allocation).
- **Live pass**: leaves with done or easing are skipped in `paintLeaves` and drawn on the main canvas after the wood layer, with the same level and view cull as `paintLeaves`, at α_leaf.
  - angle = leafAngle + A·`Sway.deg`·DEG·sin(ω·now + φ_i), with ω = 2π/`periodMs`.
  - t = ease_i(now)·rest_i − A·`shimmer`·(0.5 + 0.5 sin(ω·now + φ_i)), clamped to [0, 1].
  - LUT index = round(t·(lutSteps − 1)).
  - Leaves that are not done are never in this pass, so they stay green and still.
- **Ease on mark / undo**: from the current t to rest_i (or 0) over `Motion.autumnMs` with smoothstep. Under reduced motion it lasts 0 ms. A leaf whose undo ease reaches 0 goes back to the layer (`progressVersion` bump).
- `motionActive()` = (A > 0 and the live pass drew ≥ 1 leaf in the last frame) or an ease is running. The frame loop stops otherwise, so idle = 0 frames.
- Arrays (`easeFrom`, `easeTo`, `easeStart`, `rest`, `phase`, `inLivePass`) are preallocated per leaf.

### Performance budget (1440×900, DPR 1 and 2, `?perf=1`)

| Item | Budget |
|---|---|
| Draw p95 while zooming or panning | ≤ 8 ms, unchanged (ADR 006). Sub-budgets: sky ≤ 0.5, ground ≤ 0.3, canopy ≤ 1.0, live pass ≤ 1.5 ms for 200 done leaves in view |
| Canopy texture rebuild (on a mark) / sky rebuild (on resize) | ≤ 30 ms / ≤ 15 ms, never during a zoom gesture |
| Idle | 0 frames after `settleMs` + `idle.crisp` + 1 frame; 0 frames ever when no done leaf is in view |
| Memory | canopy texture ≤ 16 MB (4 M px), sky ≤ 2 MB |
| `kotree.js` gzip | each WP ≤ +8 KB; the wave ≤ +25 KB (fail line 370,000 B); hard 600,000 B |
| Click "Mark as done" → colour starts | ≤ 1 frame |

### Accessibility

- Done is never shown by colour alone: the canvas draws dry veins, the card shows "✓ Done" text and the toggle has `aria-pressed`, the A11yNodes aria says ", done", and the legend counter is a live region.
- All motion respects reduced motion (no sway, no shimmer, instant colour). The LOD cross-fade is a function of zoom, not time.
- Contrast on the backdrop is enforced by `check-tokens.py`.
- Hit targets and tab order are unchanged.

### Acceptance checks (reviewer, end of wave; CDP screenshots to `docs/reviews/tree-v3/`)

- [ ] CI green: `jsBrowserTest`, `check-tokens.py`, `check-hardcoded.sh`, `validateContent`, wave-check.
- [ ] `layoutReport: PASS` with the new rules. The console hashes equal the values pinned in WP2.
- [ ] Shots at z 1, 2, 3, 4.5, 6, 8, 14: lush dot canopy at z ≤ 2.4. Dots and leaves cross-fade between 3.5 and 5 with no frame where both are absent. At ≥ 5 there are no dots.
- [ ] Sky, clouds and ground show on `/` only; `/topics` and a lesson page have a computed `body` background of `#ffffff`.
- [ ] Mark a live leaf: green → yellow → orange over ≈ 900 ms. Reload: still done. With localStorage blocked (DevTools override or a `setItem` that throws): no console error, and marking works for the session. Undo reverses it. Shadow and up-next cards have no toggle.
- [ ] While zooming, done leaves sway and shimmer and other leaves stay still. Under emulated `prefers-reduced-motion: reduce` there is no sway and the colour is instant.
- [ ] Idle: the probe frame counter stays flat for 2 s after settle.
- [ ] The legend counter equals `store.count`. A screen reader hears ", done" on a done leaf button.

### Risks

| Risk | Mitigation |
|---|---|
| Tight 3:1 margins of `tree.stem` on the backdrop | `check-tokens.py` gate; lighten the backdrop, never darken inks |
| The autumn colour could be mistaken for the up-next glow | rest t ≥ 0.75 (amber to orange), dry veins, no halo; up-next keeps its halo |
| Grey limbs (`color.outline`) fade further on the sky | sky is near-white at crown height (`sky.horizon`); reviewer checks the z 2 to 4 grayscale shot |
| `jsBrowserTest` cannot be wired in the Kobweb module | the two fallbacks above; tests are pure |
| New spacing grows the world, so the canopy-level tree looks smaller | camera fits from geometry; `boughFitMin` checked in the shots |
| The live pass with many done leaves (up to 482) | culled to view and level; budget line; worst case 482 × 2 trig + 3 canvas calls |
| Hash rebaseline hides an accidental geometry change later | the new values are pinned by a test in WP2 |

### Rejected options

| Option | Why rejected |
|---|---|
| Dots generated per frame or drawn as 7,000 arcs per frame | jitter and 3 to 6 ms per frame; the texture is one `drawImage` |
| CSS background image behind a transparent canvas | it does not pan or zoom with the world ground; another asset and request |
| A backdrop with parallax per frame | redraw cost and motion sickness for a small gain |
| A JSON value in localStorage | a parse-failure path; newline-separated ids are simpler |
| One key per leaf | 482 keys and N reads at start-up |
| A constant idle sway loop | violates the idle-CPU rule; motion is tied to camera activity |
| Keeping limbs exactly still (no hash change) | it cannot give more space between branches (request 1) |

### Open questions

| # | Question | Default if no answer |
|---|---|---|
| Q1 | Should "Mark as done" also appear on the lesson page? | No (tree card only); later wave |
| Q2 | Should Foundation (roots, under the ground) get dots? | No; its done state shows by leaf colour and limb tint |
| Q3 | Should the per-visitor done counter replace the header's "N of M leaves grown"? | No; both stay, and the legend is on the tree only |
