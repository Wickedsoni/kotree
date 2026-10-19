# S3 acceptance (20 Oct 2026)

Measured with headless Chrome 155, 1440x900, DPR 1, exported static site served from localhost. Screenshots in `docs/reviews/screenshots/` (01-home, wheel-157, wheel-246, 02-bough-craft, 03-branch).

## P0-10 numbers (record only, not gates)

| Item | Value |
|---|---|
| First paint (HTML shell) | 56 ms (static), 590-700 ms (dev server) |
| Tree visible | before the 3 s sample on both servers |
| Frame time while zooming (120 wheel events) | p50 16.7 ms, p95 16.7 ms, max 16.9 ms (vsync bound) |
| Bundle `kotree.js` | 827,482 B raw, 240,690 B gzip -9 (`gzip -9 -c site/.kobweb/site/kotree.js \| wc -c`) |
| DOM nodes at home | 24 to 29 |
| Geometry hash | `cecb906380417dd8` on every load |

## Acceptance bullets (docs/12 §10)

| Bullet | Result |
|---|---|
| G1-G5 recorded | PASS (`S1-gates.md`) |
| White page, one black tree, no dark mode, no console error | PASS (no errors logged; tree black, limbs grey) |
| z=1: exactly 5 bough labels; z>=2.4: 16 branch labels + Foundation | PASS (5 at z=1; 17 at z=2.46) |
| Limbs separate in 8 steps; no flicker at z=1.80 | PASS by construction (step = round(p*8)); label set is stable at 1.74, 1.80, 1.86. Note: between z 1.8 and ~1.9 only Foundation is labelled (bough titles hand over at 1.8, branch titles reach opacity above 0 at p>0.5). Spec behaviour, author may want to tune |
| Bough click lands z>=2.4; branch click z=4.0; Home z=1 | PASS (2.40, 4.00); Home now lands at the whole-pixel fit z=0.82 at 1440x900 (see Scale) |
| Same pixels each load | PASS (hash identical across runs) |
| No hex colour, font or px size outside Tokens.kt | PASS (only FNV/RNG constants in gen/, not colours) |
| No leaf, stem, lesson or /content read; export succeeds | PASS (`kobweb export --notty --layout static` exit 0, kotree.js 827 KB, index.html 72 KB; no OOM text in log) |
| P0-10 and bundle size into docs/07 §8 | OWED to main agent (numbers above) |

## Changes and deviations

- Keyboard bug: the container re-applied its attributes on every recomposition, which removed and re-added `tabindex` and dropped focus, so only the first key press worked. Camera state is now read only in a child composable. `+`, `=`, `-`, Home, drag and wheel verified.
- Renderer bug: tile alpha was written as a Byte (255 became -1, clamped to 0), so the canvas stayed blank. Fixed.
- Scale (updated 20 Oct 2026, docs/12 §8): the non-integer blit deviation is removed. `snappedScale` rounds the art-pixel size to a whole number of device px; tiles, whole-pixel origin and labels all use it, so edges are crisp at every zoom. Camera z stays continuous, the picture steps. Home and fits floor to a whole scale, so Home shows z about 0.82 at 1440x900 (was 1.00) and the tree is slightly smaller.
- Tapered width: `Polyline.tipWidthPx` added (contract change).
- Branch limbs use `color.outline` (rule 8). Foundation has no separate limb. Limbs are drawn under the bough so they hide inside it at step 0.
- Not done: determinism unit test (no Kotlin/JS test setup, owed to Phase 2 remainder). At bough zoom, neighbouring boughs' branch labels can overlap.
- 20 Oct 2026: at bough zoom only the current bough's branch labels render (nearest bough to view centre, sticky via Tokens.Camera.boughSwitchRatio); other boughs' branch labels and zero-opacity ones are removed from DOM, so tab order and accessibility tree drop them; Foundation stays labelled; zooming out restores all bough labels. Screenshots 02, 05, 06, 07 retaken at 1440x900.
