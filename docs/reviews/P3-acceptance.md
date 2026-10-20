# P3 acceptance: Phase 3 S3 review (20 Oct 2026)

Reviewer: independent. Tree: uncommitted working tree on `main` (git status at start). Export built with Kobweb CLI 0.9.23, served from a local static server, headless Chrome 1280x800 DPR 1 (CDP script, not committed). Source files were not edited. All stray processes (Chrome, static server) were killed afterwards.

VERDICT: FIX (no blocker; 5 small fixes, 2 need the owner's decision)

## Checklist (docs/14 section 5)

| Bullet | Result | Evidence |
|---|---|---|
| Validators fail on seeded bad input | PASS (unit), not run end to end | `./gradlew -p buildSrc test` exit 0 (task up to date, 50 tests per NEXT-STEPS). The end-to-end roadmap edit was not done: it would edit a canonical roadmap, which this review must not do. S2 scope. |
| Counts match | PASS | `scripts/verify-phase3.sh`: 482 total, 0 live, 17 branches with the 08 section 9.0 totals, 180 stems, 20 fruits, 17 upNext, 465 shadow. |
| Deterministic | PASS | `generateTreeData` run twice (second with `--rerun-tasks`): identical sha256 for `tree.json` and `GeneratedTreeData.kt`. `fullHash=c9399b38f0f393cf` printed on three separate page loads. |
| No leaf published | PASS | No `content/<branch>/<stem>/<leaf>.md` in `git diff main`; `live: 0`; no `addExtraRoute` in `site/build.gradle.kts`. |
| Limbs unmoved | PASS | Console on every load: `limbHash=cecb906380417dd8 fullHash=c9399b38f0f393cf`. Z1 screenshot not re-diffed against the Phase 1 set, but the limb hash is unchanged and `TreeGeometry.bounds` is untouched. |
| layoutReport | PASS | Console at 1280x800: `layoutReport: PASS`, every branch OK at landing z and z=7 (ui lands at z=6 via `Tokens.Zoom.branchLandingZ`). Note it is an estimate (label width = 8 px per character, `Tokens.LayoutCheck`), not measured DOM boxes. |
| Performance | PASS with caveats | 10 s wheel zoom from home (z 0.8 to 24.0): rAF p50 16.7, p95 16.8, max 17.0 ms (vsync bound, 60 Hz headless, so p95 is 0.1 ms over 16.7 by rounding only). Main-thread task time over the 10 s: 0.32 s (script 0.14 s), about 3 % busy, so frames are not at risk. Max DOM nodes 45 (55 with the stem view and popup). Click to popup 26 to 32 ms (MutationObserver to next rAF). Init <= 50 ms: not measured directly (the code has no `performance.now()` log, as the plan asks). Evidence: `geometry hash` logged at 220 ms after navigation including bundle load, and the whole `layoutReport` (17 branches) runs in the next 5 ms. Add the log (FIX 5) to close this bullet. |
| Bundle recorded in 07 section 8.1 | FIX (owner `architect`) | Measured now: 323,366 B gzip, S3 adds 10,318 B (cap 15,000, PASS). Not yet in docs/07 section 8.1 (it holds only 313,048). Verified by re-export: 323,366 B. |
| Screenshots reviewed | PASS with findings | 15 files in `docs/reviews/phase3/` (kotlin, dsa, foundation at z 2.4, 4, 8, 14, 24) viewed. Reduced-motion set added by this review: `reduced-motion-kotlin-stem.png`, `reduced-motion-kotlin-popup.png`, plus `motion-kotlin-popup.png`. Findings below (F1 to F3). |
| Accessibility | PASS | Names verified in the live DOM: `"<stem title>, coming soon"` for all 15 Kotlin stems and `"Where does a Kotlin program start running?, coming next"` for the up-next leaf. Stem buttons are real `<button>`s in data order. Focus ring uses `:focus-visible` (`LabelButton`, `LeafNode`), outline 2 px in `onSurface`. Glow: 1 running animation (1000 ms) normally, `document.getAnimations()` is empty under `prefers-reduced-motion: reduce`, and the ring is still drawn (see screenshot). `check-tokens.py`: 0 failures. `check-hardcoded.sh`: clean. No `prefers-color-scheme` rule in source (AppEntry fixes the mode to LIGHT; headless Chrome reports dark and the page stays white). Popup: `role=dialog`, `aria-labelledby`, focus moves into it, Escape and Close restore focus to the opener, only a Close button. |
| Export and no warnings | PASS | Old export deleted, export run: `index.html` present, 0 x `Failed to export`, `BUILD SUCCESSFUL`. `compileKotlinJs --rerun-tasks`: no `w:` lines. Webpack prints per-module "N warnings" lines (pre-existing library source-map notices, not Kotlin warnings). |

## FIX list

1. `docs/07-technical-architecture.md` section 8.1: add S3 numbers (323,366 B gzip, +10,318 B over S2, +45,449 B over M0 277,917, 76,634 B headroom), and the `fullHash` (`c9399b38f0f393cf`) as the new determinism value (plan section 4.2 says this goes in 07 section 8.1). Needs `architect` (owner).
2. Up-next leaf is hard to see (design). `Tokens.Leaf.upnext` is `#C9CDD4` and the twig is `color.outline` `#C4C7CC`: the leaf pixel (and its 1 px outline, also `color.outline`) is nearly the same grey as the twigs it sits on, so only the glow ring marks it (`kr-stem.png`, `foundation-z14.png`). The tokens follow docs/02, so this is a design call: either keep and accept, or give the pip a darker outline from an existing token. Needs owner decision, not mechanical.
3. First stems and the up-next leaf can be off-screen at the landing z=4 on long limbs (known open item). Confirmed: at the Kotlin landing no `, coming next` button exists until the visitor clicks a stem; the first up-next leaf shows only after the stem fly (z 12.4). Options: per-branch `branchLandingZ`, or a landing centre on the up-next stem. Needs owner decision.
4. Known open, confirmed: twigs are chunky at z 8 to 24 (about 40 to 80 px blocks, `kn2-stem.png`, `kotlin-z24.png`), and neighbouring branches' up-next pips are drawn as unclickable, glowless squares near crossing limbs. Tied to the Phase 2 remainder (re-raster per zoom). Report only, as the plan says.
5. Mechanical, `fixer` (K = `site/src/jsMain/kotlin/dev/kotree/site/`):
   - `K/tree/TreeView.kt`: add the `performance.now()` log for `generate()` + `treeData` init (plan section 5, performance bullet).
   - `K/tree/camera/Camera.kt` near line 25: orphan KDoc `/** Stem fly: 12% margin, at least z = 8 ... */` attached to nothing (the values live in `Tokens.Zoom.stemFitMargin` and `stemFitMinZ`); delete it.
   - `K/tree/gen/Stems.kt`: bare literals `len * 0.15` (twig bend) and `pitch * 0.05` (slot jitter) should be `Tokens.Gen.STEM_BEND` and `LEAF_JITTER` (the hardcoded-values script does not catch unitless ratios). Changing the values would change `fullHash`, so keep them equal and re-record.
   - `K/tree/TreeView.kt`: `UpNextPopup` (about 60 lines) copies the look of `LeafPopup` (`K/ui/layout/LeafPopup.kt`) because that always shows "Open lesson". Better to give `LeafPopup` an optional `onOpen: (() -> Unit)?` (null = Close only) and delete the copy, so the popup is not maintained twice. Also tidy the unsorted imports at the top of `TreeView.kt`. Both are small refactors for `fixer`; the popup refactor touches a Phase 1 component, so owner sign-off is advised.

## Not verified

- End-to-end validator failure through `:site:validateContent` and the export log message (S2 bullet).
- Performance on a real 60 Hz display or a throttled CPU (headless numbers only), and Z1 pixel diff against the Phase 1 "after" screenshot.
- `layoutReport` measures estimated widths; real DOM label overlap was only checked by eye in the screenshots (no overlaps seen in `kotlin-z4.png` and `dsa-z8.png`; Kotlin z=4 labels "Special Classes" and "Classes and Objects" sit close but do not overlap).
