# 13 — Phase 1 plan: design tokens and core components

> **Purpose:** the plan for Phase 1 in at most 3 sessions: check order, `Tokens.kt` migration, component split with file ownership, `/kitchen-sink`, acceptance.
> **Read this when:** starting Phase 1, or touching `Tokens.kt`, fonts, or any component in `docs/02` §6.
> **Depends on:** `00` (wins), `02` (all), `07` §3, §8.1, `10` Phase 1, `11` rows P0-5, P0-6, P0-8, P0-11, P0-12, `12` §3, §4, §7.
> **Version:** v1 · 20 Oct 2026 · owner: `architect`. Branch `phase-1-tokens`. Not yet listed in `docs/INDEX.md` (add: "Phase 1 tokens and components -> `13`").

K = `site/src/jsMain/kotlin/dev/kotree/site/`. Baseline to protect: geometry hash `cecb906380417dd8`; `kotree.js` 240,690 B gzip (`07` §8.1).

---

## 1. Order of work (checks first)

Run in this order, all in S1, by the tokens `ui-engineer`. Commands run from `/site` (Git Bash). Record `DONE <date>` in `11` and remove the VERIFY in `02`.

| Step | Row | Command / test | Pass | Fallback |
|---|---|---|---|---|
| 0 | baseline | Before any edit: `kobweb run`, headless Chrome (method of `docs/reviews/S3-acceptance.md`) screenshots at z = 1, 2.4, 4.0 into `docs/reviews/phase1/before-*.png` | 3 files exist | none |
| 1 | P0-5 + P0-6, one probe | In `K/pages/dev/TokenProbe.kt` (throwaway) declare top-level `val colorBranchKotlinVar by StyleVariable<CSSColorValue>()`, `colorBranchAndroidCoreTintVar`, `space2Var`, set them on the root, and add `val probe: String = "--" + colorOnSurfaceVar.name`. `kobweb run`; DevTools console on an element inside the root: `getComputedStyle($0).getPropertyValue("--space-2")` and the other two | `8px`, `#3b5bdb` (case may differ), the tint; `probe` compiles | P0-5: write the observed name in `02` §3.1 table (token name stays the contract). P0-6: keep the CSS name as a string beside each declaration; only the Phase 5 sim colour reader needs it. Check by console, not a unit test (Kotlin/JS test setup is unverified, `12` §10) |
| 2 | P0-8 fonts | Needs Python with `fonttools` and `brotli`. Sources: Google Fonts repo `ofl/robotoflex` (variable TTF) and a JetBrains Mono release (variable TTF); list the files before naming them. Roboto Flex: `fonttools varLib.instancer RobotoFlex*.ttf wght=400:800 opsz=14:32 wdth=drop slnt=drop GRAD=drop XOPQ=drop XTRA=drop YOPQ=drop YTAS=drop YTDE=drop YTFI=drop YTLC=drop YTUC=drop -o rf-2axis.ttf`, then `pyftsubset rf-2axis.ttf --unicodes="U+0000-00FF,U+0131,U+0152-0153,U+2000-206F,U+20AC,U+2122" --flavor=woff2 --output-file=roboto-flex-v1.woff2`. JetBrains Mono: same subset, `wght=400:700`, `jetbrains-mono-v1.woff2`. `wc -c *.woff2` | Roboto Flex < 150 KB, JetBrains Mono < 100 KB, total <= 250 KB (`07` §8). `wght` upper bound 800 is deliberate: emphasized type is +100 (`02` §3), so display 700 -> 800. `⚠️ VERIFY` `axis=drop` and range syntax with `fonttools varLib.instancer --help` before running (range syntax `wght=400:700` is in the fontTools docs) | Pin `opsz`, then static weights 400/600/700 and drop emphasized variants; or drop `wght` to 400:700 and drop emphasized display |
| 3 | licences | Copy each font's `OFL.txt` next to its file | `public/fonts/OFL-roboto-flex.txt`, `OFL-jetbrains-mono.txt` exist | none (OFL requires it) |
| 4 | font loading | `⚠️ VERIFY` the Kobweb-native way: read the Kobweb docs and search the `varabyte/kobweb` repo for `font-face`; do not guess an API. Default that needs no new API: `site/src/jsMain/resources/public/fonts/fonts.css` with `@font-face` (`font-display: swap`, `font-variant-ligatures` is set on `font.code` in Base.kt), linked from `<head>` in `site/build.gradle.kts` (`07` §4: head links live there), plus `<link rel="preload" as="font" type="font/woff2" crossorigin>` for Roboto Flex only | DevTools Network: both woff2 served from `/fonts/`, no third-party request; `document.fonts.check("16px 'Roboto Flex'")` true | Static weights (step 2 fallback) |
| 5 | P0-11 | Dev page with one ~300-word paragraph at `layout.lesson.text` 624 px, `type.body`. Console: take 5 full lines (use `Range.getClientRects()` per line) and count characters | average 62 to 72 | Change the `layout.lesson.text` value, not the rule |
| 6 | P0-12 | `K/motion/Spring.kt`: damping 0.8, stiffness 380, mass 1, integrated on `requestAnimationFrame`; log `performance.now()` until `abs(x - target) < 0.5` px for a 100 px move | 500 to 700 ms; nothing blocks input over 900 ms | Tune by eye, or a precomputed CSS `linear()` easing |
| 7 | contrast | `python scripts/check-tokens.py` (written in S1, stdlib only, reads hex from `Tokens.kt`). WCAG ratios in **sRGB mixing, the same formula as the Kotlin `tint()`**. Checks: every `color.branch.*` ink on white >= 4.5 and within 0.1 of the `02` §2.2 table; **every ink on its own tint >= 4.5** (`02` §2.2 measured ink on white only); `on-branch` on ink >= 4.5; `on-surface-muted`, `leaf.shadow.label`, each `feedback.*` on white and on its tint >= 4.5; focus ring vs white >= 3 | exit code 0 | Known problem: `architecture-data` `#2E7D4F` on its 12% tint is about 4.3 (hand arithmetic; the script decides, expect `networking`, `tools`, `testing` to be borderline). Fix order: (1) tint 12% -> 8% (architect edits `02` first), (2) darken that ink within its hue (`02` first). Never relax 4.5 |

Stop and report if step 1 or 2 fails twice.

## 2. Migration of `Tokens.kt` (the tree must not break)

Principle: **add, never rename.** The tree has 119 references to `Tokens.*`. Every existing name and value stays; S1 changes no file under `K/tree/`. Each value is typed once; CSS variables are set from the same Kotlin constants.

| Stays a Kotlin constant (in `Tokens.kt`) | Becomes a Silk `StyleVariable` / CSS variable (declared in new `K/TokenVars.kt`, set at the root in `AppEntry`) |
|---|---|
| `World`, `Zoom`, `Gen`, `Camera`, `LabelNudge`, `Motion` (all numbers, incl. spring numbers), `Space.grid/hitAreaPx/labelOutlinePx/hairlinePx` | `color.surface/on-surface/on-surface-muted/outline/focus-ring` |
| **Canvas / world colours as `Int`:** `Color.bark`, `outline`, `surface`, `inkBark`, `inkOutline`, `inkRgb`, plus new `leaf.shadow/upnext/upnext.glow/easy/medium/hard`, `fruit`, `done-mark` (`02` §2.3) | `color.branch.<slug>` ink, `.tint`, `color.on-branch`, `color.bough.*`, `color.feedback.*` (+ `.tint`) |
| `Color.onSurface`, `onSurfaceMuted`, `leafShadowLabel` stay as `Int` (Labels/TreeView read them) and **also** feed the matching variables | `font.ui`, `font.code`; `type.<name>.size/.line/.weight`; `shape.*`; `space.1..16`; `elevation.*`; `layout.*` |
| Numbers the tree needs in math: `Type.labelSizePx/LineHeightPx/Weight` stay `Int`; `typeLabel*` variables are set from them | Not variables: leaf-state colours (DOM chips read the `Int` via `Color.rgb(...)`), spring numbers |

Added to `Tokens.kt` (literals live only here): `object Branch` (17 ink `Int`s from `02` §2.2, plus `fun tint(ink, pct)` = sRGB mix into white, never typed), `object Chrome` (feedback, on-branch), `object Leaf`, `object Shape`, `object Spacing` (1..16), `object TypeScale` (6 rows), `object LayoutW` (viewport.min 1024, lesson.text 544 (was 624, P0-11), lesson.wide 720, gutter), `Type.fontUi` becomes `arrayOf("Roboto Flex", "system-ui", "sans-serif")`, new `fontCode`.

`K/TokenVars.kt` holds top-level `*Var` declarations named by the `02` §3.1 rule (examples: `colorSurfaceVar` -> `--color-surface`, `colorBranchAndroidCoreTintVar`, `space2Var`, `typeBodySizeVar`, `layoutLessonWideVar`) and one `fun Modifier.tokenVariables(): Modifier` that calls `setVariable` for each, values taken from `Tokens`. It contains no literal. `AppEntry.kt` applies it to the root `Box`; `colorMode` stays LIGHT (L9). `fun branchInk(slug)` / `branchTint(slug)` map a branch slug to its variables for components.

`K/ui/Base.kt` (global, run 1): body font and size, `font-variant-ligatures: none` on code, focus ring (`3 px`, `2 px` offset, `color.focus-ring`), and `@media (prefers-reduced-motion: reduce)` that zeroes transitions and animations. `⚠️ VERIFY` how Silk registers global styles; use the Kobweb docs, not a guessed name.

**Tree regression gate (end of S1, blocks S2):**
- [ ] Console geometry hash is `cecb906380417dd8`.
- [ ] `git diff --stat -- site/src/jsMain/kotlin/dev/kotree/site/tree` is empty.
- [ ] After-screenshots at z = 1, 2.4, 4.0 match the before set except label text (font changed).
- [ ] The font swap is the real risk: `LabelNudge.charWidthFactor` (0.62) is an estimate. Check z = 1 shows exactly 5 labels and z >= 2.4 shows 17 with no overlap. If they overlap, change that one constant in `Tokens.kt` and record the new value.
- [ ] `kobweb export --notty --layout static` exits 0.

## 3. Components and file ownership

Three owners. Run 1 goes first because runs 2 and 3 import its names. Token declarations are **frozen** at the end of S1; a missing token goes to the main agent, who edits between runs (the `12` §6 rule). Runs 2 and 3 treat `Tokens.kt`, `TokenVars.kt`, `AppEntry.kt`, `Base.kt`, fonts and `ui/core/*` as read-only. Components take plain parameters (strings, lists, callbacks, a branch slug); none reads tree data or `/content`. Each run ships a throwaway preview page that S3 deletes.

| Run | Session | Owns (files) | Delivers |
|---|---|---|---|
| **1 tokens + fonts** | S1 | `K/Tokens.kt`, `K/TokenVars.kt`, `K/AppEntry.kt`, `K/ui/Base.kt`, `K/motion/Spring.kt`, `K/ui/core/Button.kt`, `K/ui/core/Chip.kt`, `site/build.gradle.kts` (head link only), `site/src/jsMain/resources/public/fonts/*`, `scripts/check-tokens.py`, `scripts/check-hardcoded.sh`, `K/pages/dev/TokenProbe.kt` | Section 1 steps, migration, `Button` (filled, tonal, text; 44 px min; shape morph full -> md on press via `motion.spatial.fast`), `Chip` (difficulty and status as text plus swatch) |
| **2 layout** | S2 | `K/ui/layout/{TopBar,Breadcrumb,ZoomControls,BranchPanel,LeafPopup,ProgressCounter,ComingSoonBadge}.kt`, `K/pages/dev/LayoutPreview.kt` | `TopBar` ("Kotree", tagline D14, counter), `Breadcrumb` (links), `ZoomControls` (+, -, reset, Start here; keyboard), `BranchPanel` (ink, tint, `shape.xl`, 4 px top bar), `LeafPopup` (`shape.blob`, `elevation.2`; Watch-the-video button only when `videoUrl` is non-empty, L26), `ProgressCounter` ("N of M leaves grown", `aria-live="polite"`), `ComingSoonBadge` |
| **3 lesson + challenge** | S2 | `K/ui/lesson/{LessonHeader,LessonBlock,CodeBlock,EditorShell,PredictOutput,FixBug,SimFrame}.kt`, `K/pages/dev/LessonPreview.kt` | `LessonHeader` (no Video/Capstone section, L26), `LessonBlock`, `CodeBlock` (copy button, ligatures off), `EditorShell` (frame, Run button, offline message slot; **no Playground**, that is Phase 6), `PredictOutput` / `FixBug` (static shells: options, check button, feedback with icon plus text; no scoring logic), `SimFrame` (720 x 405 CSS px, play/pause/reset buttons that call callbacks; no engine) |

Not components in Phase 1: `TreeCanvas`, `LeafNode`, `FruitNode`, `StemLabel` are the tree world (Phase 2 remainder). The kitchen sink shows their colours as swatches read from the `Int` constants, nothing more.

### `/kitchen-sink` (S3, one owner: `K/pages/KitchenSink.kt`)

- `@Page("/kitchen-sink")`. "Private" means **unlisted**, not secret (public repo): no link from any page, `noindex`, not in the sitemap. `⚠️ VERIFY` per-page `<meta name="robots" content="noindex">` in Kobweb (docs search, not a guess); fallback `public/robots.txt` with `Disallow: /kitchen-sink`. After export: `grep -c kitchen site/.kobweb/site/sitemap.xml` must print 0 if the file exists (`⚠️ VERIFY` whether Kobweb emits a sitemap at all; `07` §6.4 builds ours from lessons only).
- Sections: colour swatches (neutrals, 17 branch ink + tint pairs with ratio text, leaf states, fruit, feedback), type scale (all 6 rows, emphasized), shape, spacing, elevation, motion (spring demo, reduced-motion note), layout widths, then every component of section 3 in each state (default, hover, focus, pressed, disabled). Placeholder copy says "Sample text" and never names a real leaf, so nothing looks published (hard rule 1).
- Each section carries an `id`, so screenshots are per section.

## 4. Sessions

| Session | Runs | Output | Exit |
|---|---|---|---|
| **S1** | run 1 alone | **DONE 20 Oct 2026** (results: `docs/reviews/P1-probes.md`; token changes: `lesson.text` 544, three inks darkened, digit names `--space2`). Steps 0 to 7, migration, `Button`, `Chip` | tree regression gate green; `scripts/check-tokens.py` and `scripts/check-hardcoded.sh` exit 0; commit `feat: design tokens and fonts` |
| **S2** | runs 2 and 3 in parallel | their components and preview pages | each builds alone; commit per run |
| **S3** | `ui-engineer` (kitchen sink, deletes `K/pages/dev/*`), then `reviewer`, then `fixer` | `/kitchen-sink`, bundle numbers, screenshots in `docs/reviews/phase1/`, `docs/reviews/P1-acceptance.md` | acceptance below; `housekeeper` updates status files; `docs/07` §8 gets the new numbers |

## 5. Acceptance

- [ ] **No hardcoded values.** `bash scripts/check-hardcoded.sh` exits 0 (**patterns narrowed in S1, 20 Oct 2026, see `docs/reviews/P1-probes.md`**: `font-family` and `border-radius` match only when followed by a quoted literal; the unit pattern needs a non-identifier character before the digits; the hex pattern needs a word boundary). It greps `K/` and `site/src/jsMain/resources` excluding `Tokens.kt` and `public/fonts/fonts.css` for: `#[0-9A-Fa-f]{3,8}\b`, `0x[0-9A-Fa-f]{6}`, `rgb\(`, `[0-9]+(\.[0-9]+)?\.(px|em|rem|vh|vw|ms)\b` (allowed: `0.px`, `100.percent`), `"(Roboto|JetBrains)`, `font-family`, `border-radius`. Existing tree code already passes (`12` §10).
- [ ] **Contrast** (section 1 step 7) passes, run again in S3 after any token change.
- [ ] **White only.** DevTools Rendering: emulate `prefers-color-scheme: dark`; `/` and `/kitchen-sink` background is still `rgb(255, 255, 255)`; `grep -r "prefers-color-scheme" K/` is empty.
- [ ] **Reduced motion.** DevTools Rendering: emulate `prefers-reduced-motion: reduce`: camera fly is an instant cut (`12` §2), buttons do not morph, `ProgressCounter`/pulse static; screenshot both modes.
- [ ] **Keyboard.** Every interactive component shows the focus ring and is reachable with Tab; min target 44 x 44 px for chrome (tree label buttons stay 24 px until Phase 2 remainder, see Q3).
- [ ] **Bundle.** Record in `docs/07` §8.1: `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` for (a) main before Phase 1 = 240,690 B, (b) after S1, (c) after S3, (d) after S3 with `KitchenSink.kt` temporarily removed. Report delta against 240,690. Cap: total delta <= +25 KB gzip is fine; above +25 KB, `architect` looks at it; above +40 KB fails (headroom to 400 KB is about 159 KB and lessons need it). Fonts are separate: <= 250 KB total, not in the bundle.
- [ ] **Tree unchanged** (section 2 gate, repeated in S3) and `kobweb export --notty --layout static` exits 0 with no warnings.
- [ ] **Kitchen-sink screenshots reviewed** by `reviewer` (every section at 1280 x 800, plus the two emulation modes) with PASS in `docs/reviews/P1-acceptance.md`.
- [ ] The kitchen sink is not linked, not in the sitemap, `noindex` or `robots.txt` applied.

## 6. Risks

| Risk | Mitigation |
|---|---|
| Font swap changes label widths and breaks nudging | gate in section 2; one constant to tune |
| Ink on tint fails 4.5 for several branches | step 7 fix order; `02` edited first |
| Silk variable API or digit names differ | step 1 probe before any other code |
| All lesson/component code lands in one bundle | caps in section 5; no new libraries |
| Runs 2 and 3 need a token that is missing | frozen list; main agent adds between runs |

## 7. Rejected

- Converting all `Tokens` to `StyleVariable`s: the canvas and the nudge math need numbers (`12` §11).
- Typing hex values in both Kotlin and CSS: two sources would drift.
- Three fully parallel runs from the start: components import token names that do not exist until S1 ends.
- Google Fonts CDN, a second display font (`02` §3).
- Playground, sim engine, tree data in Phase 1 components: Phases 3 to 6.

## 8. Open questions for the author

1. Does the kitchen sink ship in the production export (unlisted, `noindex`; default) or is it removed from the export in Phase 8?
2. If an ink fails on its 12% tint: may tints drop to 8% (default, keeps your accepted hues), or should inks be darkened instead?
3. Tree label buttons are 24 px high, below the 44 px chrome minimum. Accept 24 px until Phase 2 remainder (default), or raise to 44 px now?

### Author answers (20 Oct 2026)

1. `/kitchen-sink` ships in production, **unlisted and noindex** (no link, not in the sitemap). Nothing to strip in Phase 8.
2. If an ink fails contrast on its 12% tint, **darken the ink**; keep the 12% tint.
3. Tree labels **stay 24 px**; pad the click target invisibly to 44 px if needed.
