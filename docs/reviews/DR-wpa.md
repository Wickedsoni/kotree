# DR WP-A: vector tree, unverified items

Not compiled here (dl.google.com blocked, no Compose/Kobweb artifacts offline). CI must compile with no `w:` warnings.
What was checked locally: `scripts/check-hardcoded.sh` clean, `scripts/check-tokens.py` 0 failures, and a JVM-frontend type check of
the pure-Kotlin files (Camera, ViewTracker, HitIndex, LabelLayout, FrameLoop, VectorRenderer, WoodLayer, PerfProbe, DeepLink, gen/*)
with only the expected "unresolved DOM / dynamic" errors left. Compose files (TreeView, A11yNodes, NodeCard) and Input.kt were re-read only.

## Not measured (no browser here)
- Draw p95, frame interval, idle frame count, long tasks: need `?perf=1` and the acceptance script.
- gzip size of `kotree.js` (`gzip -9 -c site/.kobweb/site/kotree.js | wc -c`): not measured. Raster, cache and three overlays are deleted;
  renderer, layer, labels, hit index, loop, a11y list, card, deep link, probe are added.
- `limbHash` (`cecb906380417dd8`): `tree/gen/*` untouched except `LayoutCheck.kt`, so it should hold; confirm in the console.
- `WoodLayer` is on (`WoodLayer.useLayer = true`). Set false to draw directly (ADR simplest-first rule) if the layer misbehaves.

## Unverified API use
- `js("new Path2D()")`, then `fill(path)`, `stroke(path)`, `arc`, `arcTo`, `quadraticCurveTo` through `dynamic` (no Kotlin binding relied on).
- `canvas.getContext("2d")` as `dynamic`; `measureText(...).width`; `g.drawImage(canvas, 9 args)` on the offscreen layer.
- `Reflect.construct(ResizeObserver, [lambda])` and `PerformanceObserver` with a Kotlin lambda as the JS callback (relies on lambdas being plain JS functions).
- `document.fonts.load(font).then(ok, ok)` through `dynamic`. Chosen over `fonts.ready` because `ready` has already resolved before the first draw.
- `window.requestAnimationFrame(callback: (Double) -> Unit): Int`, `setTimeout({..}, Int): Int`, `clearTimeout(Int)`, `cancelAnimationFrame(Int)`.
- `canvas.setPointerCapture`, `canvas.style.setProperty`, `Element.matches(":focus-visible")`, `target.closest(selector)` through `dynamic`.
- Compose: `Modifier.toAttrs { }` on a `Button(...)` (A11yNodes), `ref { element -> ... onDispose { } }` with `element.asDynamic().animate(...)`
  (copied from the removed GlowRing), `it.nativeEvent.stopPropagation()` in `onKeyDown`, `key(id) { }`, `Ul`/`Li`.
- `ZoomControls` is unchanged and placed bottom-right with a `Modifier` that sets `position/right/bottom`.

## Deviations and decisions
- Shadow leaves are drawn and clickable from the Stem level (Q1 default). `VectorRenderer.drawShadowLeaves` reverts it.
  `LayoutCheck.kt` now checks every leaf, shadow included.
- Leaves are drawn from the Branch level for the focused branch only, from the Stem level for all branches. Hit areas: leaves from the
  Stem level (all branches), twigs from the Branch level, branch limbs from the Bough level, bough limbs and roots at Canopy and Bough.
- Labels: the layout is cached per (level, branch, stem, ln z bucket) at the bucket's centre scale; inside a bucket (about 13 % zoom) anchors move
  with the real scale, so a tiny overlap is possible at a bucket edge. Level 3 labels the focused stem's leaves; level 4 labels the focused branch's leaves
  and stems (leaves of other branches are clickable but unlabeled, hover shows a tooltip).
- Roots label and limb act as the Foundation branch from the Bough level (fly + branch card); at Canopy they only fly.
- Card position: right of the node (left if no room, centred on narrow screens), always vertically centred. After a stem or branch fly the anchor is the view centre.
- Container is `tabindex="-1"` (the hidden node buttons are the tab stops); key handlers still work once focus is inside it.
- `Camera.fit` and `homeTarget` are continuous now (no whole-pixel snapping), so landing zooms differ slightly from the old ones; `Tokens.Zoom.branchLandingZ` was tuned for the old values and is not re-measured.
- `Esc` with no card open flies one zoom level out; double-click zoom is not implemented (not in ADR WP-A).
- `__kotreeLabels()` returns label rectangles only (screen CSS px). Leaf hit squares are 24 px (`Tokens.Space.hitAreaPx`) centred on `leaf.at`.

## Literals without a token (file-private constants)
`ZOOM_OUT_FACTOR` 0.85 (TreeView), `CARD_WIDTH_PX` = 6 x `space.16` (TreeView), `CARD_FROM_SCALE` 0.92 (NodeCard, from the ADR),
`LENS_CTRL` 0.56 and `VEIN_HALF` 0.32 (leaf shape), `LEAF_FAN_RAD` 0.45 (leaf angle spread), `MAX_LAYER_AREA` 16 M device px (WoodLayer),
`MAX_FRAME_MS` 50 and `WHEEL_EPS` (FrameLoop). Proposed tokens if the orchestrator wants them: `tree.card.width`, `tree.layer.max-area`.
The up-next halo is `space.1` (4 px), the leaf stroke is `tree.stem.line.min` (1.5 px).

## Files
Deleted: `tree/overlay/{Labels,StemLabels,LeafNodes}.kt`, `tree/render/{PixelCanvas,LimbRaster,TwigRaster,Cache}.kt` (nothing else referenced them).
`Tokens.World.px`, `Tokens.LabelNudge` and `Tokens.Leaf.upnextPip` are now unused; left in `Tokens.kt` (not mine).
`LeafPopup.kt` is untouched (KitchenSink still uses it).
