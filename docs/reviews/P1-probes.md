# Phase 1 probes (S1, 20 Oct 2026)

Headless Chrome 155, 1440x900, DPR 1, dev server (`./gradlew :site:kobwebStart`, stopped after). Probe page `K/pages/dev/TokenProbe.kt` (throwaway, S3 deletes).

| Row | Result | Observed | Action |
|---|---|---|---|
| P0-5 digit names | **FAIL** | `space2Var` becomes `--space2`; `--space-2` is empty. `--color-branch-kotlin` and `--color-branch-android-core-tint` are correct. | Fallback: `docs/02` section 3.1 table must record "digit names get no dash: `space2Var` -> `--space2`". Token name stays the contract. Not edited here (doc not owned): architect. |
| P0-6 `StyleVariable.name` public | **PASS** | `"--" + colorOnSurfaceVar.name` compiles and gives `--color-on-surface`. | none |
| P0-8 font sizes | **PASS** | `roboto-flex-v1.woff2` 43,640 B (axes wght 400-800, opsz 14-32); `jetbrains-mono-v1.woff2` 34,492 B (wght 400-700). Total 78,132 B, budget 250 KB. fontTools 4.66.1, `varLib.instancer` range syntax verified with `--help`. | none |
| Licences | **PASS** | `public/fonts/OFL-roboto-flex.txt`, `OFL-jetbrains-mono.txt` (copied from google/fonts `ofl/robotoflex` and JetBrains/JetBrainsMono `master`). | none |
| Font loading | **PASS** | Network: only `/fonts/roboto-flex-v1.woff2` and `/fonts/fonts.css`, no third party. `document.fonts`: Roboto Flex loaded; JetBrains Mono loads on demand (file served 200). Head links in `site/build.gradle.kts`. Subset is Latin only: glyphs such as arrows fall back to system fonts. | none |
| P0-11 chars per line | **FAIL at 624, PASS at 544** | 5-line average 77.8 at 624 px (lines 76-81). At 544 px: average 68.2, lines 64-73. | Fallback: `layout.lesson.text` changed 624 -> **544** (`Tokens.LayoutW.lessonText`; 544 = 68 x 8). `layout.lesson.wide` stays 720 (SimFrame needs it), so `docs/02` section 4.5's "text + space.12 each side" no longer holds (544 + 96 = 640). Architect to update. |
| P0-12 spring settle | **FAIL on the number, mechanics PASS** | 0.8 / 380 closed form: last time more than 0.5 px from target on a 100 px move = **0.371 s** (expected 0.5-0.7). rAF run in Chrome agrees (first entry into 0.5 px at 209 ms). No animation exceeds 900 ms. | Spring values are verified against `ExpressiveMotionTokens`, so they are not tuned; the doc's 0.5-0.7 s estimate is what is off (omega = sqrt(380) = 19.5 rad/s, decay 15.6 /s). Fallback used: `Spring.cssLinear()` builds a CSS `linear()` easing and its duration from the spring (used by `Button` shape morph). Architect: change the expectation to about 0.3-0.5 s, or the author tunes by eye. |
| Contrast (`scripts/check-tokens.py`) | **FAIL, then PASS after fix** | On 12% tint: `architecture-data` 4.30, `networking` 4.49, `feedback.correct` 4.36. Author answer 2: darken ink, keep tint. New: `architecture-data` `#2C774B` (5.46 white, 4.65 tint), `networking` `#007669` (5.53, 4.67), `feedback.correct` `#2C7730` (5.54, 4.71). Same hue and saturation, lightness -0.01 to -0.02 in HSL. Every other pair passes. | `docs/02` section 2.2 and 2.4 must take the new hex and ratios (architect). Script exit 0. |
| Hardcoded values (`scripts/check-hardcoded.sh`) | **PASS** | Exit 0. Two deviations from `docs/13` section 5: `font-family` and `border-radius` are flagged only when followed by a quoted literal (the tree's `property("font-family", ...)` calls would otherwise fail); the unit pattern requires a non-identifier char before the digits (`Tokens.Spacing.s1.px` is not a literal); the hex pattern needs a word boundary so the tree's 64-bit FNV constants do not match. | none |

## Tree regression gate

- Geometry hash `cecb906380417dd8`: PASS (console, every load).
- `git diff --stat -- site/src/jsMain/kotlin/dev/kotree/site/tree`: empty, PASS.
- Screenshots `docs/reviews/phase1/before-*.png` and `after-*.png` (home, z1, bough, branch). Font swap changes label text only. Label widths shrink by 0 to 3 px (for example "Kotlin and Android" 128 -> 127 px, "Android core and lifecycles" 179 -> 182 px), positions move by at most 3 px. Home and z1: exactly 5 bough labels. Bough view: 4 branch labels plus Foundation, no overlap (smallest vertical gap between label boxes 47 px). `LabelNudge.charWidthFactor` unchanged (0.62). Note: since 20 Oct the tree shows only the current bough's branch labels, so "17 labels at z >= 2.4" no longer applies.
- Bundle: `kotree.js` 252,441 B gzip -9 after S1 (baseline 240,690, +11,751; includes the throwaway probe page).
- `kobweb export --notty --layout static` (CLI 0.9.23): exit 0. No Kotlin `w:`/`e:`; webpack prints source-map warnings for library modules only (as in `S1-gates.md`).
- Keyboard: Tab focuses a Button with `outline: 3px solid rgb(59, 91, 219)`, offset 2 px; Button height 44 px; hover state layer applied.
