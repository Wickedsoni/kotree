# S3 review: tree-first prototype (20 Oct 2026)

Scope: docs/12 §1-3, §8, §10; docs/02 §3, §5 (rule 8); docs/00 hard rules. Code under `site/src/jsMain/kotlin/dev/kotree/site/`. Screenshots viewed: 01-home, wheel-157, wheel-246, wheel-400, 02-bough-craft, 03-branch, wheel-NaN. No server started, no code edited.

VERDICT: FIX (no blocker; 2 medium accessibility/visual items, rest low)

## Checks that passed

1. Hard-coded values: grep for hex, `rgb(`, px/vh/rem literals and font names outside `Tokens.kt` finds only the hex PRNG/FNV constants in `gen/Rng.kt` and `gen/GeometryHash.kt` (not colours) and the `cssRgb` helpers that format token values. Fonts and sizes come from `Tokens.Type`/`Tokens.Space`. Literal strings such as `"100%"`, `"0"`, `"none"` are not design values. PASS.
2. White only: `AppEntry.kt` forces `ColorMode.LIGHT`, background is `Tokens.Color.surface`; screenshots are white. PASS.
3. Rule 8: branch limbs use `color.outline` (grey), trunk, roots and boughs use `color.bark`. PASS (all branches grey is right with zero live leaves).
4. Determinism (docs/12 §8, D1): seeded SplitMix64, no `Math.random`, no clock, no unordered iteration (`sortedBy order` used; `HashMap` only in the tile cache). Geometry hash is logged and S3 records it identical. PASS. The unit test is owed to the Phase 2 remainder (docs/12 §3).
5. Performance: one canvas, `imageSmoothingEnabled = false`, `image-rendering: pixelated`, lazy tile cache, frames only via rAF on camera change, 24-29 DOM nodes. PASS. Deviation from docs/12 §8 (non-integer blit scale) is declared in S3-acceptance.
6. Kotlin: `./gradlew :site:compileKotlinJs --rerun --no-build-cache` ran the task, BUILD SUCCESSFUL, zero `w:` and zero `e:` lines. Caveat: the compile was incremental (no source change), so an old warning in an unchanged file would not be re-printed. Recommend one clean build before the commit.
7. Keyboard: arrows pan, `+`/`=`/`-`/`_` zoom, `Home` flies to fit, labels and Home are real `<button>`s with `:focus-visible` rings. Reduced motion: `Fly.to` cuts instantly when `prefers-reduced-motion: reduce`. Labels are real text with `aria-label`; canvas is `aria-hidden`. Contrast: `onSurface` and `leafShadowLabel` (#6B7280) on white both clear 4.5:1. PASS.

## FIX list

1. **Medium, accessibility.** `tree/TreeView.kt:60` (`property("outline", "none")`) plus `tree/TreeView.kt:62`. The container is the keyboard target (`tabindex=0`, receives arrows, `+`, `-`, `Home`) but has no visible focus indicator, so a keyboard user tabbing in sees nothing (WCAG 2.4.7). Add the same `:focus-visible` ring the buttons use, from tokens. Mechanical.
2. **Medium, visual.** `docs/reviews/screenshots/03-branch.png` (z = 4.0 after a branch click): no label is visible anywhere, and the view is only grey limbs, so the user cannot tell which branch they flew to. Likely the branch label anchor (tip) lies outside the viewport because `fit` clamps to `zMax` (`camera/Camera.kt:121`, `branchTarget`). Check that the clicked branch's label is on screen at the landing view; fix by centring on the label anchor or by clamping the fit so the tip stays inside. Needs owner (behaviour choice).
3. **Low-medium, accessibility.** `tree/overlay/Labels.kt:76,85` and `tree/TreeView.kt:101-103`. When `inBough` flips, the focused bough button is removed from the DOM and focus drops to `<body>`. Also, tab order is labels first and Home last although Home sits top-left visually. Move focus to the container on the switch (or keep the clicked label's equivalent), and place `HomeButton` before `Overlay`. Mechanical for the order, owner for the focus policy.
4. **Low, accessibility.** `tree/TreeView.kt:63` `role="application"` makes screen readers pass arrow keys through and hides normal reading. The page also has no `<h1>`, no landmark and no per-page title (grep finds none). Replace with `role="group"` plus a visually-hidden `<h1>Kotree</h1>`, or confirm in Phase 2 remainder. Needs owner.
5. **Low, visual.** Screenshots 01-home, wheel-157 and wheel-246: because the blit scale is not an integer, art pixels are 3 or 4 device px, so trunk and bough edges show 1-px steps (e.g. 01-home trunk at y 535 and y 620, wheel-157 trunk at y 650 and y 780). In 03-branch there are small fragments where limbs overlap the bough (a white notch near x 253,y 547 and a black block near x 267,y 680). This is the known deviation; record it for Phase 2 remainder (re-rasterise per level). Needs owner.
6. **Low, visual.** `02-bough-craft.png`: "Computer networking" and "Profiling and optimisation" labels sit 31 px apart and their tips converge, so they nearly collide (S3-acceptance already notes overlap between neighbouring boughs). Not clipped. Owner decision (label nudge or hide on collision).
7. **Low, hygiene.** `Tokens.kt:21` and `Tokens.kt:127` contain a Windows-1252 byte (the section sign) so the file is not valid UTF-8; Edit/Write tools refuse it. Convert to UTF-8 (replace with "§" or "section"). Mechanical.
8. **Low, hygiene.** Unused tokens: `Tokens.Color.leafShadow` (and `inkRgb` reaches only bark and outline), `Tokens.Type.placeholderTitleSizePx`. Remove or leave a comment. Mechanical.
9. **Low, hygiene.** `docs/reviews/screenshots/wheel-NaN.png` is a stale shot of the old "Kotree" placeholder with a text selection (a test run with a NaN zoom value). Delete it. Mechanical.
10. **Info.** `tree/render/PixelCanvas.kt:13` and `tree/overlay/Labels.kt:25` define two different `cssRgb` helpers (hex and `rgb()`). Merge into one in a shared file when Phase 1 wires StyleVariables.

## Unverified

Real-browser keyboard behaviour (focus ring on the container, behaviour of focusing an off-screen label inside an `overflow: hidden` overlay, which may scroll the overlay) was not run; no server was started, per instruction. Screen-reader output was not tested.

## Fixes applied (20 Oct 2026)

- 1 DONE: focus ring on the keyboard container, an aria-hidden overlay border from Tokens (an outline was painted under the canvas), shown on `:focus-visible` only. Verified with Tab in headless Chrome.
- 2 DONE: `branchTarget` centres between tip and label when the limb does not fit at zMax (the earlier condition was inverted). Security at z=4.00 now shows its label on screen.
- 3 DONE: Home is before the labels in tab order; focus returns to the container if a level switch removes the focused label.
- 4 DONE: `role=group` plus visually hidden `<h1>Kotree</h1>`.
- 5 DONE via decision B.
- 6 DONE via decision C.
- 7 DONE: Tokens.kt is valid UTF-8.
- 8 DONE: removed `leafShadow` and `placeholderTitleSizePx`; `inkRgb` commented.
- 9 SKIPPED here: wheel-NaN.png is already deleted in the working tree.
- 10 SKIPPED (Phase 1).
- Also: label overlay uses `overflow: clip` so focus can never scroll it.

Decision B: whole-number pixel scaling only. Art pixel size is rounded to whole device px (`snappedScale`), fits floor to it, start view and Home fit at z about 0.82. Zoom steps visibly between whole scales; camera z stays smooth.

Decision C: colliding labels are nudged vertically (`nudgeApart`, 16 passes, max 60 px from the tip, gap 4 px, constants in `Tokens.LabelNudge`). Checked at 1440x900 for z 2.4 to 4.0 on all four boughs: zero overlaps. Craft at z=2.4: "Computer networking" and "Profiling and optimisation" now sit 32 px apart.
