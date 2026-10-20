# 02 — Design System

> **Purpose:** the single list of design tokens (colour, type, shape, motion, spacing), the pixel-tree rules and the UI component list.
> **Read this when:** you write any CSS/Kobweb style, draw the tree, or add a component.
> **Depends on:** `docs/00-decisions.md` (L7, L9-L12, D5, D6, D13, §6, §7). Zoom levels and routes: `docs/03`.
> **Rule:** no hardcoded colour, font or spacing anywhere else. Code refers to token names only. Token names are dotted here (`color.branch.kotlin`) and become CSS custom properties with dashes (`--color-branch-kotlin`) by the fixed rule in §3.1 (Silk `StyleVariable`, name rule read from the Kobweb source and probed 20 Oct 2026: digit names get no dash, see §3.1; `StyleVariable.name` is public).
> **Verified:** 18 Oct 2026 (sources inline in §3 and §4). **Changes from verification:** none; every decision in 00 and every value in this doc held. **Added 18 Oct 2026:** token-to-CSS-variable name rule (§3.1) and layout widths incl. the narrowest `SimFrame` (§4.5).

---

## 1. Principles

1. **One theme, white only.** Page background `color.surface` = `#FFFFFF`. No dark mode, no `prefers-color-scheme` branch (L9).
2. **Two visual languages that never mix (D6).** The world (tree, leaves, fruit) is pixelated. Panels, buttons, popups, lesson pages and the editor are smooth Material 3 Expressive (M3E).
3. **Colour is for understanding.** Section colour tells the learner "which subject am I in". It appears only in chrome once the camera enters a branch (D5). The tree itself stays black, gray and green.
4. **Never colour alone.** Every branch also has a text label and a glyph, so colour-blind users and similar hues are still told apart.
5. **No generic AI look (L11).** Rejected: purple-to-blue gradients, glassmorphism, neon glow, stock emoji icons, identical rounded cards in a grid. Use flat tints, strong shape contrast, and the tree as the hero.

## 2. Colour tokens

### 2.1 Neutrals and base

| Token | Value | Use |
|---|---|---|
| `color.surface` | `#FFFFFF` | page, panels, lesson body |
| `color.on-surface` | `#1A1C1E` | body text (contrast ≈ 17:1) |
| `color.on-surface-muted` | `#5F6368` | secondary text (≥ 4.5:1) |
| `color.outline` | `#C4C7CC` | dividers, input borders |
| `color.bark` | `#000000` | tree trunk and limbs (L7: black bark) |
| `color.focus-ring` | resolves to the active branch ink, else `color.branch.kotlin` | 3 px keyboard focus ring, 2 px offset |

### 2.2 Section (branch) colours — colour psychology

Each branch has **three** tokens: `color.branch.<slug>` (the **ink**: text, buttons, borders; every one measured ≥ 4.5:1 on white), `color.branch.<slug>.tint` (container fill = ink mixed 12% into white, computed, not typed) and `color.on-branch` (`#FFFFFF`, text on an ink-filled button). Names below use the branch slug from `docs/00` §5 unchanged (dashes stay dashes; mapping rule in §3.1).

Contrast ratios were computed against `#FFFFFF` (WCAG relative luminance). Since 20 Oct 2026 `scripts/check-tokens.py` also checks every ink on its own 12% tint (sRGB mix) at >= 4.5; the tint stays 12% (author answer 2, `docs/13` §8) and inks that failed were darkened within their hue (`architecture-data`, `networking`; see §2.4 for `feedback.correct`). Every other pair passes (`docs/reviews/P1-probes.md`).

| Token (`color.branch.…`) | Ink | Contrast | Psychology and why this hue |
|---|---|---|---|
| `foundation` | `#6D4C41` | 7.6 | **Earth brown.** Roots, ground, stability. The only brown on the page, so "Start here" is unmistakable. |
| `kotlin` | `#3B5BDB` | 5.7 | **Blue (accepted).** Trust and focus: the language you read all day. |
| `android-core` | `#1B2F7A` | 12.1 | **Navy.** Same blue family as Kotlin (same bough) but much darker: depth, the platform underneath your code. |
| `ui` | `#C2185B` | 5.9 | **Magenta-pink.** Creativity, expression, things you can see and touch. The only pink. |
| `concurrency` | `#7A5A00` | 6.4 | **Dark gold.** Time, motion, "things happening at once"; yellow is the classic signal for caution, which fits races and deadlocks. Bright gold fill `#E0A100` is not allowed for text (fails contrast); use ink or tint. |
| `architecture-data` | `#2C774B` | 5.46 (4.65 on tint) | **Green (accepted).** Growth and structure: layers that hold up. Darkened 20 Oct 2026 (was `#2E7D4F`, 4.30 on its tint). |
| `app-networking` | `#01579B` | 7.4 | **Deep sky blue.** Reach outward to servers; sky, requests travelling. Kept darker and bluer than networking teal. |
| `kmp` | `#4A7000` | 5.8 | **Lime-olive green (accepted green family).** Sharing code across platforms: living, spreading growth. Yellower than architecture green. |
| `dsa` | `#A84A00` | 5.8 | **Orange (accepted).** Energy and problem solving. Burnt orange so text passes contrast. |
| `daa` | `#7B3FC4` | 6.3 | **Purple (accepted).** Abstraction and depth of thought. |
| `os` | `#B3261E` | 6.5 | **Crimson.** Authority, the kernel, resources that must not run out; red = system-level, handle with care. |
| `networking` | `#007669` | 5.53 (4.67 on tint) | **Teal (accepted).** Connection and flow. Darkened 20 Oct 2026 (was `#00796B`, 4.49 on its tint). |
| `testing` | `#C0392B` | 5.4 | **Coral red.** Bugs and failing checks demand attention; the colour a red test turns. Lighter and warmer than `os`. |
| `profiling` | `#5E4AE3` | 5.9 | **Indigo-violet.** Measuring, insight, looking inside. Bluer-violet than `daa`. |
| `native-ndk` | `#8A4B2A` | 6.7 | **Rust / sienna.** Metal, close to the hardware, C/C++ heritage. Redder and more saturated than `foundation`. |
| `security` | `#37474F` | 9.7 | **Charcoal.** Seriousness, protection, a locked door. Dark and cold, so never mistaken for shadow-gray. |
| `tools` | `#546E7A` | 5.4 | **Pewter blue-gray.** Neutral workshop instruments; deliberately quiet. Always paired with a wrench glyph. |

Bough tokens reuse a family colour for headers when branches are still grouped (D4): `color.bough.roots` = `foundation`, `color.bough.kotlin-android` = `kotlin`, `color.bough.architecture-kmp` = `architecture-data`, `color.bough.cs-theory` = `dsa`, `color.bough.craft` = `security`.

**Known near-pairs and the mitigation** (hue within ~25° or similar lightness):

| Pair | Mitigation |
|---|---|
| `kotlin` / `profiling` | different bough; profiling is more violet; glyph differs |
| `networking` / `app-networking` | teal vs deep blue; lightness differs; sit in different boughs |
| `os` / `testing` | ink lightness differs; glyph and label always shown |
| `foundation` / `native-ndk` | foundation is desaturated, native is saturated rust; different boughs |
| `architecture-data` / `kmp` | adjacent branches; the hue shifts toward yellow in `kmp`; label always shown |
| `tools` / `security` | same bough; charcoal is far darker |

Rejected: giving all 17 a fully distinct hue (the wheel has room for roughly 12 clearly different hues; the glyph rule covers the rest); gradients between branches (muddy, looks generic).

### 2.3 Leaf state colours (inside the pixel world)

These are world colours, not UI colours. Leaves never take the branch colour (D5).

| Token | Value | Meaning (`docs/00` §7) |
|---|---|---|
| `color.leaf.shadow` | `#D8DADF` | `shadow`: planned, not published. Stem names beside it use `color.leaf.shadow.label` `#6B7280` (≈ 4.8:1) so they stay readable. |
| `color.leaf.upnext` | `#C9CDD4` fill + glow | `upnext`: the one glowing leaf per branch. Glow = 2-step pixel halo in `color.leaf.upnext.glow` `#FFD54F` at 60% alpha, pulsing 1.6 s (see §6). |
| `color.leaf.easy` | `#A5D6A7` | `live`, difficulty `easy`: light green |
| `color.leaf.medium` | `#43A047` | `live`, `medium`: mid green |
| `color.leaf.hard` | `#1B5E20` | `live`, `hard`: dark green |
| `color.leaf.done-mark` | `#000000` pixel check | visitor checkmark (D10), drawn on top of the leaf |
| `color.fruit` | `#E53935` | capstone fruit (D12); its outline is bark black |

Acceptance: a leaf's state is identifiable without colour via shape cue as well: `shadow` = outline only, `upnext` = outline plus halo, `live` = filled, `hard` = filled with a 1-px darker vein pixel line.

### 2.4 Feedback colours (chrome)

`color.feedback.correct` `#2C7730` (5.54 on white, 4.71 on its tint; darkened 20 Oct 2026 from `#2E7D32`, which gave 4.36), `color.feedback.wrong` `#B3261E`, `color.feedback.hint` `#7A5A00`; each with a `.tint`. Used in Predict-the-output and Fix-the-bug (D9). Always accompanied by an icon and text.

---

## 3. Typography

| Token | Value |
|---|---|
| `font.ui` | `"Roboto Flex"`, then `system-ui`, `sans-serif` |
| `font.code` | `"JetBrains Mono"`, then `ui-monospace`, `monospace` |

Verified facts (18 Oct 2026):

- **Roboto Flex** is a variable font in the Google Fonts repository under `ofl/robotoflex`, licence SIL OFL 1.1 (`OFL.txt`: "Copyright 2017 The Roboto Flex Project Authors", no Reserved Font Name, so subsetting and renaming rules do not block us). Axes from `METADATA.pb` (13): `wght` 100-1000, `wdth` 25-151, `opsz` 8-144, `slnt` -10-0, `GRAD` -200-150, and parametric axes `XOPQ`, `XTRA`, `YOPQ`, `YTAS`, `YTDE`, `YTFI`, `YTLC`, `YTUC`. We use only `wght`, `wdth` (default), `opsz`. Sources: https://github.com/google/fonts/blob/main/ofl/robotoflex/METADATA.pb , https://github.com/google/fonts/blob/main/ofl/robotoflex/OFL.txt
- **Roboto Flex is the Material 3 Expressive default typeface.** Android's M3E type-scale page: "The default type scale for Material 3 Expressive is Roboto Flex". Caveat: that page is the Wear OS guide; m3.material.io is rendered by script and could not be read by our tools, so the phone/web page is not confirmed directly. The claim is used only as rationale (L10), not as a dependency. Source: https://developer.android.com/design/ui/wear/guides/styles/typography/type-scale-tokens
- **JetBrains Mono** is OFL 1.1 ("Copyright 2020 The JetBrains Mono Project Authors"), free for commercial and non-commercial use, no Reserved Font Name seen. The repo builds `.ttf`, `.otf`, `woff2` and variable files; the default build has ligatures, and a "NL" build has none. Sources: https://github.com/JetBrains/JetBrainsMono , https://raw.githubusercontent.com/JetBrains/JetBrainsMono/master/OFL.txt
- Decision: ligatures stay off for learners via `font-variant-ligatures: none` so `->` and `!=` show as typed (standard CSS). Fallback if a browser ignores it: ship the NL build.
- Bundling rule: ship each font's `OFL.txt` next to its files in `/fonts/` (the OFL requires the licence with every copy).
- Verified 20 Oct 2026 (P0-8, `docs/reviews/P1-probes.md`): subset to Latin, axes `wght` 400-800 and `opsz` 14-32 for Roboto Flex: `roboto-flex-v1.woff2` 43,640 B; `jetbrains-mono-v1.woff2` (`wght` 400-700) 34,492 B; total 78,132 B against the 250 KB budget. Latin only, so glyphs such as arrows fall back to system fonts.

Hosting: self-host the font files from `/fonts/` (no third-party request, `font-display: swap`). Rejected: Google Fonts CDN (extra request, privacy); a second display font (L10 allows one UI font).

### 3.1 How tokens reach CSS (Kobweb / Silk)

Verified (https://kobweb.varabyte.com/docs/concepts/presentation/silk): Silk offers `StyleVariable<T>()` (a typed CSS custom property), set with `Modifier.setVariable(...)` and read with `.value()` (optional fallback); a default can be given at declaration. Silk's palette defaults are minimal, are changed in an `@InitSilk` method through `ctx.theme.palettes.light`, and the initial `ColorMode` defaults to `LIGHT`.

Decisions for Kotree: declare tokens as `StyleVariable`s in one `Tokens.kt`, set once on the root `@App` element; set only the light palette and never switch colour mode (L9); no `ColorMode.systemPreference`.

**Name rule (settled 18 Oct 2026, Q4).** Declare each token variable with the property delegate, `val colorBranchKotlinVar by StyleVariable<CSSColorValue>()`, at top level of `Tokens.kt` (no enclosing object, so no group prefix is added) and no `prefix` argument. Kobweb turns the Kotlin property name into kebab case and drops a trailing `-var`. Evidence: the Silk docs show `val MyFontSizeScaleFactorVar by StyleVariable<Number>()` emitting `var(--my-font-size-scale-factor)`; the `StyleVariable.kt` source (`compose-html-ext`, read through a fetch tool that summarised it, not verbatim) builds the name this way and writes the property as `--` + name. Sources: https://kobweb.varabyte.com/docs/concepts/presentation/silk , https://github.com/varabyte/kobweb/blob/main/frontend/compose-html-ext/src/jsMain/kotlin/com/varabyte/kobweb/compose/css/StyleVariable.kt

| Step | Rule | Example |
|---|---|---|
| 1. CSS name | token name: replace every `.` with `-`, add `--` in front. **Digit exception:** no dash before a digit | `color.branch.android-core.tint` becomes `--color-branch-android-core-tint`; `space.2` becomes `--space2` (not `--space-2`) |
| 2. Kotlin property | split the token on `.` and `-`, camelCase the parts, add `Var` | `colorBranchAndroidCoreTintVar` |
| 3. Check | Kobweb's kebab-casing of step 2 must give back step 1 | `color-branch-android-core-tint` |

- Only colour, font, shape, spacing, type-size and `layout.*` tokens become variables. Spring and timing numbers (`motion.*`) stay plain Kotlin constants.
- Code that must read a variable by string (the sim colour reader, `docs/05` §3.3) builds the name from the Kotlin object, `"--" + colorOnSurfaceVar.name`, and never types the string. Verified 20 Oct 2026 (P0-6): `name` is public and `"--" + colorOnSurfaceVar.name` gives `--color-on-surface`.
- **Digit exception (probed 20 Oct 2026, P0-5, Chrome 155).** Kobweb adds no dash before a digit: `space2Var` becomes `--space2`; `--space-2` is empty. `--color-branch-kotlin` and `--color-branch-android-core-tint` match the rule. So step 1 holds for letters only: for a token part that is a digit, the CSS name joins it to the previous part without a dash (`space.2` becomes `--space2`, `space.12` becomes `--space12`). The token name stays the contract; code uses the Kotlin `*Var` (`space2Var`) and never types the CSS name.
- Any `calc` on variables must use Silk's `calc { }` (arithmetic on variables fails silently).

| Token | Size / line height (px) | Weight | Use |
|---|---|---|---|
| `type.display` | 48 / 56 | 700 | home headline, branch title in panel |
| `type.headline` | 32 / 40 | 650 | lesson title, section headers |
| `type.title` | 22 / 30 | 600 | popup title, card title |
| `type.body` | 18 / 30 | 400 | lesson text (large, loose, fresher-friendly) |
| `type.label` | 14 / 20 | 600 | buttons, chips, stem names |
| `type.code` | 16 / 26 | 400 (JetBrains Mono) | code blocks, editor |

Emphasized variants add 100 weight, used for the one key line per view. Lesson line length: 62-72 characters. Minimum text size anywhere: 14 px.

## 4. Shape, spacing, elevation, motion

### 4.1 Shape (M3E: large radii, mixed shapes, shape morph)

| Token | Value | Use |
|---|---|---|
| `shape.xs` | 8 px | chips, tooltips |
| `shape.md` | 16 px | inputs, code blocks |
| `shape.xl` | 28 px | panels, popups, cards |
| `shape.full` | 999 px | buttons, progress counter |
| `shape.blob` | asymmetric radii 28/8/28/8 px | leaf popup and "Start here" button: one distinctive non-card shape |

Buttons morph from `shape.full` to `shape.md` on press. Verified in part (18 Oct 2026): Compose Material 3 `ButtonShapes` holds a `shape` and a `pressedShape`, and "Button will morph between these shapes depending on the interaction" (API is experimental; https://composables.com/docs/androidx.compose.material3/material3/classes/ButtonShapes). That confirms the pattern, not our radii. Our radii (`full` to `md`) are our own design choice; the m3.material.io shape pages could not be read by our tools. Kotree implements the morph itself in CSS `border-radius` with `motion.spatial`.

### 4.2 Spacing (4 px grid)

`space.1`=4, `space.2`=8, `space.3`=12, `space.4`=16, `space.6`=24, `space.8`=32, `space.12`=48, `space.16`=64. Panel padding `space.6`; gap between lesson blocks `space.8`; minimum click target 44 x 44 px; leaves in the world keep at least `space.4` clear gap (L7).

### 4.3 Elevation

White-only, so separation is by outline and tint, not shadow: `elevation.0` none (default); `elevation.1` = 1 px `color.outline` border; `elevation.2` = soft shadow `0 2px 8px` at 12% black, only for popups over the tree.

### 4.4 Motion (M3E: springs)

| Token | Value | Use |
|---|---|---|
| `motion.spatial` | spring, damping ratio 0.8, stiffness 380 (M3E default spatial) | panels, popup, anything that moves or resizes |
| `motion.spatial.fast` | spring, damping ratio 0.6, stiffness 800 | small elements: buttons, chips |
| `motion.spatial.slow` | spring, damping ratio 0.8, stiffness 200 | large panels |
| `motion.effects` | spring, damping ratio 1.0, stiffness 1600 (M3E default effects) | colour, opacity |
| `motion.effects.fast` / `.slow` | damping ratio 1.0, stiffness 3800 / 800 | quick or gentle fades |
| `motion.camera` | 600-900 ms ease-in-out | Canopy, Bough, Branch, Stem, Leaf transitions |
| `motion.pulse` | 1.6 s sine, opacity 0.35-0.8 | `upnext` glow |

Spring numbers verified 18 Oct 2026 against the Compose Material 3 `ExpressiveMotionTokens` source (`SpringDefaultSpatialDamping` 0.8f / `Stiffness` 380f; `SpringDefaultEffects` 1.0f / 1600f; fast spatial 0.6f / 800f; slow spatial 0.8f / 200f; fast effects 1.0f / 3800f; slow effects 1.0f / 800f): https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/ExpressiveMotionTokens.kt . The older "standard" scheme is spatial 0.9 / 700 (https://github.com/material-components/material-components-android/blob/master/docs/theming/Motion.md); we use the expressive one. Note these are Compose spring parameters (damping ratio, stiffness, mass 1), not CSS values: CSS has no spring. Measured 20 Oct 2026 (P0-12): 0.8 / 380 settles (within 0.5 px on a 100 px move) in **0.37 s**, not the earlier 0.5-0.7 s estimate (omega = sqrt(380) = 19.5 rad/s, decay 15.6 per s); the spring values are not tuned. Expect about 0.3-0.5 s for this spring. `Spring.cssLinear()` builds a CSS `linear()` easing and its duration from the spring; `Button` uses it for the shape morph. Camera uses `motion.camera`, not a spring. Honour `prefers-reduced-motion`: instant cut for camera, static halo for pulse. Acceptance: no animation over 900 ms blocks input.

### 4.5 Layout widths (lesson page, desktop-first L8; settled 18 Oct 2026, Q5)

| Token | Value | Use |
|---|---|---|
| `layout.viewport.min` | 1024 px | narrowest **supported** browser width. Below it the page still scrolls and works, but is not designed, tested or budgeted (L8, `docs/00` §9). |
| `layout.lesson.text` | 544 px, centred (`Tokens.LayoutW.lessonText`; 544 = 68 x 8) | prose column. Measured 20 Oct 2026 (P0-11, headless Chrome 155, 18 px Roboto Flex, 5 full lines): 624 px gave an average of 77.8 characters per line (76-81), outside the 62-72 rule of §3; 544 px gives 68.2 (64-73). The token changed from 624 to 544, not the rule. |
| `layout.lesson.wide` | 720 px, centred | blocks that break out of the prose column: `SimFrame`, `CodeBlock`, `Editor`, `PredictOutput` / `FixBug`. Unchanged because `SimFrame` needs 720. It no longer equals `layout.lesson.text` plus `space.12` each side (544 + 96 = 640): the break-out is 88 px each side (720 - 544 = 176), set by the two widths, not by a spacing token. |
| `layout.page.gutter` | `space.8` (32) each side | page padding |

**Narrowest supported `SimFrame` width = 720 CSS px** (`layout.lesson.wide`). Why: 720 + 2 x 32 gutter = 784, which is below the 1024 px viewport floor, so at every supported viewport (1024 to any larger size) the frame is exactly 720 x 405 CSS px, because `layout.lesson.wide` is a maximum **and** the column always has room for it. Sim scale is therefore always 1 (world unit = CSS px, `docs/05` §2.2), and a 44 x 44 world-unit body is 44 x 44 CSS px. Rejected: a fluid frame (scale varies, every spec would have to re-check hit sizes); a 560 px frame (needs bodies of at least 57 world units, breaks the existing `null-lunch-box` sizes). Outside the supported range (viewport under 1024) the frame shrinks with `width: 100%`, keeping 16:9; nothing is promised there.

## 5. Pixel-tree rules (the world)

1. **Pixelated only inside the tree canvas** (`image-rendering: pixelated`). Draw at low internal resolution (`world.px` = 4 screen px at Canopy zoom), scale by integer factors only. Never smooth or blur.
2. **Bark is pure black** (`color.bark`), 1 world-px outline, no gradients; thicker toward roots, thinner toward tips. References: a bare black oak silhouette; a lone white-blossom tree in mist (L7).
3. **World palette is limited** to bark, leaf states (2.3), `color.fruit`, and a faint ground line in `color.outline`. No branch colour in the world.
4. **Backdrop is procedural and seeded** (D1); stems and leaves are crisp overlays at world coordinates (D2). Leaf hit target is at least 44 px on screen at Leaf zoom.
5. **Text over the world is smooth** (`font.ui`, `type.label`) with a 3 px white outline; never rasterised.
6. **Zoom level names** (detail in `docs/03`): Canopy (bough groups), Bough (branches split out), Branch (stems), Stem (leaves), Leaf (popup, then lesson). World pixel size stays constant; the tree grows apart rather than blurring.
8. **Limb state (author, 20 Oct 2026):** a branch's limb is drawn grey (`color.outline`) while it has no leaf with `status: live`, and turns `color.bark` black as soon as it has one. The trunk and boughs stay black. This is the only exception to rule 2 and uses an existing palette colour, so rule 3 still holds.
7. The section accent enters the screen as a 4 px top bar and panel tint once the camera is at Branch level or deeper (D5).

## 6. Components

| Component | Tokens | Notes and acceptance |
|---|---|---|
| `TreeCanvas` | `color.bark`, `color.leaf.*`, `color.fruit`, `world.px` | pixelated; keyboard-pannable; text alternative lists visible branches |
| `ZoomControls` (+, -, reset: `shape.full`; Start here: `shape.blob`, per §4.1, settled 20 Oct 2026) | `color.branch.foundation` | keyboard operable; focus ring visible |
| `Breadcrumb` | `type.label`, branch ink | each crumb is a link that zooms out |
| `BranchPanel` | `color.branch.<slug>`, `.tint`, `shape.xl`, `type.display` | opens at Branch zoom; stems, glyph, progress |
| `StemLabel` | `color.leaf.shadow.label`, `type.label` | gray from day one (D3) |
| `LeafNode` | `color.leaf.*`, `world.px` | shadow, upnext, live easy/medium/hard, done-mark |
| `LeafPopup` | `shape.blob`, `elevation.2`, branch ink, `type.title` | topic name, summary, Open lesson, Watch the video (L26) |
| `FruitNode` | `color.fruit` | capstone (D12) |
| `ProgressCounter` | `shape.full`, `type.label` | "N of M leaves grown" (D11); screen-reader announced |
| `Button` (filled, tonal, text) | branch ink / tint, `color.on-branch`, `shape.full` | 44 px min height; hover, focus, pressed |
| `Chip` (difficulty, status) | `shape.xs`, `color.leaf.*` swatch + text | difficulty written as text, not colour only |
| `LessonHeader` | `type.headline`, branch tint | Watch-the-video button; no Video or Capstone section (L26) |
| `LessonBlock` | `type.body`, `space.8`, `elevation.1` | structure per `docs/04` |
| `CodeBlock` | `font.code`, `type.code`, `shape.md`, `color.outline` | copy button; ligatures off |
| `Editor` (Playground) | `font.code`, `shape.md` | D8; Run; offline message |
| `PredictOutput` / `FixBug` | `color.feedback.*` | D9; icon plus text |
| `SimFrame` | `shape.xl`, `elevation.1`, `layout.lesson.wide` | wrapper for sims (`docs/05`); play, pause, reset; 720 x 405 CSS px (16:9) at every supported viewport (§4.5) |
| `ComingSoonBadge` | `color.leaf.shadow.label`, `shape.xs` | shadow branches (L4) |
| `TopBar` | `color.surface`, `elevation.1` | "Kotree", tagline (D14), counter |

## 7. Global acceptance criteria

- [ ] Background is `#FFFFFF` on every route; no dark-mode media query.
- [ ] No hex colour, raw px font size or font name outside the token file.
- [ ] Every interactive element shows `color.focus-ring` on keyboard focus and has at least 4.5:1 text contrast.
- [ ] The tree canvas is the only pixelated surface.
- [ ] Only reference videos and animations exist; no Android app or emulator, so nothing here depends on one.

## 8. Open verification items

| # | Item |
|---|---|
| V1 | **Settled** 18 Oct 2026: Roboto Flex is OFL 1.1 with 13 axes and is the M3E default type scale (Wear OS guide; m3.material.io unreadable by tools). **Closed** 20 Oct 2026: woff2 sizes 43,640 B and 34,492 B (§3). |
| V2 | **Settled**: JetBrains Mono OFL 1.1; ship `OFL.txt` with the files. |
| V3 | **Settled** for springs (ExpressiveMotionTokens). Shape-morph: pattern confirmed, radii are ours. **Closed** 20 Oct 2026: settle time 0.37 s (§4.4). |
| V4 | **Settled** 18 Oct 2026: name rule in §3.1. **Closed** 20 Oct 2026: digit exception recorded and `StyleVariable.name` is public (§3.1). |
| V5 | **Settled** 18 Oct 2026: narrowest supported `SimFrame` width 720 px (§4.5). **Closed** 20 Oct 2026: 624 px gave 77.8 characters per line, so `layout.lesson.text` is 544 px (68.2); `SimFrame` stays 720 (§4.5). |
