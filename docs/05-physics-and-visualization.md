# 05 — Physics and Visualization

> **Purpose:** how Kotree "shows it moving" (L14, L21, `docs/01` §3.3): the physics engine, the Kotlin/JS interop shape, the four sim kinds, the method that maps a physical behaviour to a CS or Android concept, the metaphor catalogue, the sim spec template for `docs/sims/<sim-id>.md`, loading and performance budgets, accessibility, and one worked spec (`null-lunch-box`).
> **Read this when:** you design a sim (`physics-designer`), build one (`sim-engineer`), place a `SIM` marker in a lesson (`lesson-writer`), propose a `sim:` cell in a roadmap (`curriculum-designer`), or review a leaf that has `sims` in its frontmatter (`reviewer`).
> **Depends on:** `docs/00-decisions.md` (wins every conflict; L14, L15 amended 18 Oct 2026, L19, L21, D6, D8, §4 "Sim", §8.2 `sims`), `docs/01` §3.3, `docs/02` §2, §3, §4.4, §6 `SimFrame`, `docs/04` §3.4 and §4.3 (the sim slot), `docs/07` §2.2 (folder layout), §8 (budgets), `docs/08` §6 (`sim:` cells).
> **Version:** v1.2 · 18 Oct 2026 (v1.1: review A2e fixes; v1.2: verification pass against official sources, 28 marks settled to 7 open, see "Changes from verification") · owner: `physics-designer` (design rules), `architect` (runtime and loading rules).

**Changes from verification (18 Oct 2026).** Facts read from the Matter.js 0.20.0 docs and source, Three.js docs, kotlinlang.org, MDN, web.dev and w3.org (§12) changed these design rules:

| # | Fact found | Design change | Where |
|---|---|---|---|
| V1 | `runner.enabled = false` skips engine updates "while the runner loop remains running": `Runner.run` requests the next frame before it checks `enabled`. `Runner.stop` cancels the frame loop. | Pause, tab-hidden and idle use `Runner.stop`; resume uses `Runner.run` again. `enabled` is never used. A long gap is safe: `Runner.tick` replaces any frame delta above `max(1000/15, maxFrameTime)` with the last accepted delta. | §6.1, §6.2 |
| V2 | `maxFrameTime` is a time budget, not an update count; `maxUpdates` (default `null`) is the count limit. | Runner is created with `maxUpdates: 2`. | §2.2, §6.2 |
| V3 | `MouseConstraint` acts in the engine's `beforeUpdate` event, so nothing can be picked up while the runner is stopped. | A stopped (idle or paused-by-hiding) sim calls `Runner.run` on pointer-down on the canvas and on keyboard pick-up. A learner-paused sim (Play/Pause) stays frozen; drag is disabled with the hint "Press Play to move things." | §6.1, §7.1 |
| V4 | Matter's `Mouse` adds a non-passive `wheel` listener that calls `event.preventDefault()`, which blocks page scrolling over the canvas. Touch handlers also call `preventDefault()`. | The host removes the `wheel` listener right after creating the `Mouse` (the handler is the public `mouse.mousewheel`). New acceptance item in §6.4. Touch scrolling over the canvas stays blocked (mobile is not a priority, L8). | §3.2, §6.4 |
| V5 | `Mouse` converts pointer position using `canvas.width / clientWidth` and the canvas attribute `data-pixel-ratio` (default 1); `Mouse.setScale` / `Mouse.setOffset` exist. | The host sets `data-pixel-ratio` to the ratio used for the backing store, and calls `Mouse.setScale` with `720 / cssWidth` (and `405 / cssHeight`) on every resize; offset stays 0. | §7.3 |
| V6 | `Mouse` reads only `changedTouches[0]`; a second finger moves the drag point. Its `mouseup` listener is on the canvas only. | The host filters touch events that are not the first touch, and ends a drag on a window-level pointer-up outside the canvas. Both carry a Phase 5 test. | §7.3, §10.6 |
| V7 | Three.js `WebGLRenderer` has required WebGL 2 since r163. | The `3d` fallback triggers when WebGL 2 is unavailable; detection is the same one-line check the Three.js `WebGL.isWebGL2Available()` addon uses, run once per page. | §4.4 |
| V8 | Three.js 0.186.1 is about 185 KB gzip as one minified bundle (bundlephobia). | Budget 200 KB kept; the margin is small, so the first `3d` spec must measure the vendored pair (Q3). | §6.3 |

No locked decision (L1–L27) or default (D1–D17) changes. Free tiers only (L27): every library here is free and self-hosted (MIT).

**The one rule behind this doc.** A sim is a moving picture of an idea the lesson text already explains (`docs/04` §3.4). The physics must make the learner *feel* a true rule of the idea. If the physics would teach something false, the sim is a plain animation instead (§5.4).

**No Android emulator, no Android simulator (L15 amended, D8, `docs/00` §9).** Nothing on the site runs Android code. Android behaviour is shown with **code-to-screen** sims (§4.3): the learner flips between pre-written code variants and watches a drawn screen and timeline change. The word "sim" in this project always means "a visualization on a lesson page" (`docs/00` §4), never "Android simulator".

---

## 1. Decisions at a glance

| # | Decision | Section |
|---|---|---|
| S1 | 2D physics engine: **Matter.js 0.20.0** (MIT, about 26 KB gzip). | §2 |
| S2 | The engine file is **self-hosted and loaded on demand** with a script tag, not bundled through `npm()`, so the home page and sim-less pages download 0 engine bytes. | §3.1 |
| S3 | Kotlin talks to Matter.js only through one file of `external` declarations, wrapped by a small Kotlin `SimWorld` API. Nothing else in the codebase names a Matter.js function. | §3.2 |
| S4 | Kotree draws sims with **its own Canvas 2D renderer** using `docs/02` tokens. `Matter.Render` is not used. | §3.3 |
| S5 | Four sim kinds: `physics`, `animation`, `code-to-screen`, `3d`. | §4 |
| S6 | 3D is allowed **only inside sims** (L21), with **Three.js**, scripted motion, no 3D physics engine in v1. | §4.4 |
| S7 | Sims are **smooth, not pixelated**. They live in chrome (`SimFrame`, `docs/02` §6); only the tree canvas is pixelated (D6, `docs/02` §7). | §3.3 |
| S8 | **Template sims first, bespoke later** (ADR 009, L37). Every sim today is a template sim: one ```` ```sim ```` block in the leaf (`docs/04` §3.4) is its whole spec, so no `docs/sims/<sim-id>.md` is written. A **bespoke** sim (physics, `3d`; ★ candidates in ADR 009 §5) is built later, needs a spec at `docs/sims/<sim-id>.md` from the §8 template **before** `sim-engineer` starts, and replaces the template sim by registering the same `id`. | §8, ADR 009 |
| S9 | **Template sims:** no autoplay; the frame shows the `intro` state; Play runs only after a click, one timer at a time, and stops at the end of the script, on Pause, when the tab is hidden or the frame is under 10 % visible; zero idle CPU (ADR 009 §4). **Bespoke sims:** load when the frame comes within 400 px of the viewport, run only while at least half visible. Both are destroyed when the page is left. | §6, ADR 009 §4 |
| S10 | Reduced motion: no autoplay; every learner action jumps to its settled result with a 150 ms fade. Full motion stays one click away. | §7.2 |
| S11 | Every sim on a `profiling` leaf is kind `animation`, never `code-to-screen` (author decision 18 Oct 2026). | §4.5 |

---

## 2. Physics engine (L14)

### 2.1 Choice: Matter.js 0.20.0

Facts checked on 18 Oct 2026 (sources in §12):

| Fact | Value | Source |
|---|---|---|
| Latest version | `0.20.0`, published 2024-06-23; it is the only release after 0.19.0 (2023-02-16) and the only npm dist-tag (`latest`) | npm registry, CHANGELOG, API docs header (checked 18 Oct 2026) |
| Licence | MIT (README; the `matter.min.js` banner says "License MIT") | GitHub README, shipped file |
| Size | The file we ship, `build/matter.min.js`, is 83.5 kB; bundled and minified 83,161 bytes, **25,859 bytes gzip**; 0 dependencies. Inside the §6.3 budget of 30 KB. CI still measures the vendored file (§6.4). | unpkg file list, bundlephobia (checked 18 Oct 2026) |
| Dimension | 2D rigid bodies only | GitHub README |
| Features we use | rigid bodies, static bodies, sensors (`isSensor`), sleeping, constraints (stiff and spring-like), composites, `MouseConstraint` (drag with mouse or touch), collision events (`collisionStart`, `collisionActive`, `collisionEnd`), `Runner` with a fixed timestep | API docs: Engine, Body, Constraint, MouseConstraint, Runner |
| Defaults we rely on | `gravity.y = 1`, `gravity.scale = 0.001`; iterations position 6, velocity 4, constraint 2; `enableSleeping = false` (we turn it on); body `restitution 0`, `friction 0.1`, `frictionAir 0.01`, `frictionStatic 0.5`, `density 0.001`, `slop 0.05`, `sleepThreshold 60`; constraint `stiffness 1`, `damping 0` | API docs |
| Timestep | `Runner` uses a **fixed** step `delta`, default `1000 / 60` ms ("added new Matter.Runner with default fixed timestep", 0.20.0 changelog), with `maxFrameTime` default `1000 / 30` ms (a time budget) and `maxUpdates` default `null` (a count limit) | Runner docs, CHANGELOG |
| Pause | `runner.enabled = false` pauses updates "while the runner loop remains running"; `Runner.stop` "ends execution of `Runner.run` ... by canceling the frame loop" (V1) | Runner docs and `Runner.js` source |
| Combining rule | Two touching bodies use `max` of their restitution and `min` of their friction | Body docs |
| Determinism | The README, API docs and changelog never use the word "deterministic" (checked 18 Oct 2026). The Runner "favours a smoother user experience over perfect time keeping" and "will call multiple engine updates (or none) as needed", so the number of updates between two learner actions depends on frame timing. Therefore: tests compare **states** (which box holds what), never coordinates. ⚠️ VERIFY (unknown: whether the same sequence of `Engine.update(engine, 1000/60)` calls gives the same states on one machine). Phase 5 test (`docs/10` physics layer): drive the world headless with 600 fixed updates and a scripted drag, run it 20 times, and require identical state chips every time. | Runner docs, README, CHANGELOG |
| Maintenance | Development has slowed; 0.20.0 is mature and feature-complete for our needs. Pin it; do not chase alphas. | npm mirror commentary |

Why Matter.js: it is small, plain JavaScript (no WebAssembly start-up step), its units are screen pixels so specs are easy to read, it has drag (`MouseConstraint`), sensors and soft constraints built in, and the API docs are complete. These are exactly the behaviours the catalogue in §5.3 needs.

Rejected, one line each:

- **Rapier 2D (`@dimforge/rapier2d` 0.21.0, Apache-2.0)**: its `rapier_wasm2d_bg.wasm` alone is 2.4 MB uncompressed (unpkg, 18 Oct 2026), about 30 times the whole Matter.js file; WebAssembly needs async start-up; the `-compat` package inlines the WASM as base64 (12.1 MB unpacked `dist/`); cross-platform determinism needs a separate build. Overkill for teaching sims.
- **Planck.js 1.5.1 (Box2D rewrite)**: 210,911 bytes minified, 46,711 bytes gzip (bundlephobia, 18 Oct 2026), about 1.8 times Matter.js; metre-based units add a conversion layer to every spec; no gain over Matter.js for our scenes.
- **Box2D compiled to WASM**: same start-up and size cost as Rapier, older API.
- **Hand-written physics**: L14 asks for a real engine; a home-made one would be our own bug source.
- **3D physics (Rapier 3D, cannon-es) in v1**: no concept in the catalogue needs 3D *physics*; 3D sims use scripted motion (§4.4).

### 2.2 World units and standard numbers

Every 2D sim uses the same world so specs can be compared:

| Item | Value |
|---|---|
| Logical world size | **720 × 405** world units (16:9). One world unit = one CSS px when the frame is 720 px wide; the canvas scales to the frame width by a single factor. |
| Origin | top-left; `y` grows downward (Matter.js convention) |
| Timestep | fixed `1000 / 60` ms per engine update (Runner `delta` default); Runner created with `maxUpdates: 2` (V2) |
| Gravity | `y = 1`, `scale = 0.001` (defaults) unless the spec says "no gravity" (queues, pipes) |
| Sleeping | on (`enableSleeping = true`) so settled scenes cost almost nothing |
| Mass | Matter.js derives mass from `density × area`. Specs give **density and size**, and may quote the resulting mass for reference. Units are Matter.js units, not kilograms. |
| Spring | a `Constraint` with `stiffness < 1`. Reference points from the docs: `1` very stiff, `0.2` soft spring; `damping 0.1` is heavy. Stiffness has no physical unit. |
| Speed cap | specs that allow throwing set a maximum speed (default 25 units per step, applied with `Body.getSpeed` / `Body.setSpeed`, which keeps direction) because Matter.js has **no continuous collision detection**: issue #5 "Prevent object passing through walls / tunneling (implement CCD)" is still open; the author wrote "Currently there is no continuous collision detection (CCD), so fast moving objects pass through other objects", and on 24 Jun 2024 that 0.20.0 Runner sub-stepping only "can help" (checked 18 Oct 2026). Rule for specs: the cap must be smaller than (thinnest wall + smallest body dimension), so the two shapes always overlap in at least one update (for `null-lunch-box`: 8-unit box wall + 44-unit sandwich = 52 > 25). |
| World edges | every physics sim has four static walls just outside the visible area, 40 units thick |

---

## 3. Kotlin/JS interop and rendering

### 3.1 Loading the engine on demand

| Rule | Value |
|---|---|
| Where the file lives | `site/src/jsMain/resources/public/vendor/matter-0.20.0.min.js`, served at `/vendor/matter-0.20.0.min.js`. Copied from `build/matter.min.js` of the `matter-js@0.20.0` npm package (the package's `build/` holds exactly `matter.js` 374 kB and `matter.min.js` 83.5 kB; unpkg, 18 Oct 2026). Version in the file name so the URL can be cached for a long time. |
| How it loads | One shared Kotlin loader (`SimEngineLoader`) adds a single `<script>` tag (standard DOM `document.createElement("script")`) the first time any sim on the page asks for it and returns the same pending result to every later caller. The file is a UMD build: with no CommonJS `exports`/`module` and no AMD `define` on the page, it runs `e.Matter=t()` with `e = this`, so it creates the global `window.Matter` (checked in the shipped file). The Kobweb docs (kobweb.varabyte.com/docs, checked 18 Oct 2026) have no page on per-page script loading, so the loader uses only the browser DOM API and no Kobweb feature. Phase 0 acceptance: after load, `window.Matter.Engine` is defined. |
| Why not `implementation(npm("matter-js", ...))` | An npm dependency is bundled into the one Kotlin/JS bundle, so every page, including home, would pay for it. That breaks the `docs/07` §8 budget "physics engine: 0 bytes on home". |
| Failure | If the script fails (offline, blocked) the frame shows the text equivalent (§7.3) and the line "The moving picture could not load. The text on this page explains the same idea." No retry loop. |
| Three.js (3D kind only) | **Two files**, because since r171 `three.module.js` imports `./three.core.js` (checked in `three@0.186.1`: the module's `import` and `export ... from` lines both point at `./three.core.js`). Copy `build/three.core.js` and `build/three.module.js` from the pinned npm package into `site/src/jsMain/resources/public/vendor/three-<version>/`, keeping both names so the relative import resolves. The package ships no `.min.js` files: `build/` holds exactly `three.cjs`, `three.core.js` (1.46 MB), `three.module.js` (663 kB), `three.tsl.js`, `three.webgpu.js`, `three.webgpu.nodes.js` (unpkg, 0.186.1, 18 Oct 2026). So both files are minified once when vendored (any minifier that keeps ES-module `import`/`export` lines, for example `esbuild --minify` without `--bundle`). Size: see §6.3. No import map is needed, because Kotree imports by absolute URL and the only inner import is the relative `./three.core.js`. Loading: `SimEngineLoader` adds one inline `<script type="module">` whose text is `import * as THREE from "/vendor/three-<version>/three.module.js"; window.THREE = THREE;` and then dispatches a ready event; Kotlin externals read the global `THREE`. Timeout 10 s, then the failure row below. Rejected: a classic `<script src>` (an ES module cannot be loaded that way); `import()` written inside Kotlin `js("...")`: Kotlin copies `js()` text "as-is" into its output (kotlinlang.org js-interop), and webpack, which Kobweb's build uses (`docs/07`: `build/kotlin-webpack/...`), parses every `import()` it finds; its `/* webpackIgnore: true */` comment "disables dynamic import parsing" (webpack.js.org module-methods). Whether Kotlin keeps that comment inside `js()` is untested and not needed while this option stays rejected. Candidate version `0.186.1` (npm latest, 18 Oct 2026); final pin at the first `3d` spec (Q3). |

Rejected: a CDN (`unpkg`, `jsDelivr`): third-party request, privacy, and a second point of failure. Rejected: Kotlin/JS code splitting so sim code also loads lazily. The official docs (kotlinlang.org `js-project-setup`, page dated 23 Sep 2026) offer output granularity `per-module` (default), `whole-program` and `per-file` (needs an `es2015`/`es2020` target), but say nothing about loading those files lazily; the only lazy-loading example is a community proof of concept (`kjs-chunks`, `-Xir-per-module`, described by its author as fragile). Kobweb then bundles the output with webpack into one script (`docs/07`). Revisit only if the Kotlin docs add lazy loading.

**Consequence for `docs/07` §8.** The engine and any 3D library are 0 bytes on home. The **Kotlin code of each sim** sits in the main Kotlin/JS bundle, because that bundle is one file. Budget for that code: shared sim runtime ≤ 12 KB gzip, each sim ≤ 6 KB gzip. These are targets, not measurements: the open check is in §6.3 (shared runtime row). `docs/07` §8 already states this ("Sim Kotlin code" row).

### 3.2 External declarations and the `SimWorld` wrapper

| Layer | File (under `site/src/jsMain/kotlin/dev/kotree/site/sims/`) | Rule |
|---|---|---|
| Externals | `engine/MatterExternals.kt` | `external` declarations **without** `@JsModule`, referring to the global `Matter` object created by the script tag. Declare only the members Kotree uses (Engine, Runner, Bodies, Body, Composite, Constraint, Mouse, MouseConstraint, Events). Never invent a member: copy each name from the 0.20.0 API docs. An `external` declaration without `@JsModule` names a global, exactly like the Kotlin docs' own examples `external fun alert(message: Any?): Unit` and `external val window: Window` (kotlinlang.org js-interop, page dated 23 Sep 2026); `@JsModule` is only for module imports (js-modules). The Kotlin docs do not discuss this per module kind, so Phase 0 acceptance includes one smoke call (`Matter.Engine.create()` after the script loads) in both the dev and the export build. |
| Option objects | same file | Matter.js takes plain JS option objects. Each is an `external interface` (for example `BodyOptions` with `isStatic`, `isSensor`, `restitution`, `friction`, `frictionStatic`, `frictionAir`, `density`, `inertia`, `chamfer`, `collisionFilter`) created by one factory that returns `js("{}")`, the pattern of the Kotlin docs' `JQueryAjaxSettings` example (js-interop). Rejected: `jso` from kotlin-wrappers (a new dependency for one helper). |
| Wrapper | `engine/SimWorld.kt` | The only API sims use: create world, add body, add constraint, add sensor, listen for "body entered/left sensor", step, pause, resume, reset, dispose. Swapping engines later changes this layer only. Pause and resume map to `Runner.stop` and `Runner.run` (V1); step maps to `Engine.update(engine, 1000/60)` called directly; after creating `Mouse`, `SimWorld` removes its `wheel` listener (`removeEventListener("wheel", mouse.mousewheel)`) so the page still scrolls over the canvas (V4). |
| Renderer | `engine/SimRenderer.kt` | Canvas 2D drawing of bodies from their vertices, plus DOM labels (§3.3). |
| Host | `SimHost.kt` | The composable placed in lessons: `SimHost(id: String)`. Draws `SimFrame` chrome, canvas, DOM overlay, live region; handles loading, visibility, disposal (§6). The canvas is Compose HTML's `Canvas(attrs, content)` (package `org.jetbrains.compose.web.dom`, typed `HTMLCanvasElement`); set-up and clean-up go in its `attrs { ref { canvas -> ...; onDispose { ... } } }`, which the Compose HTML source documents as called once when the element enters and once when it leaves the composition, built on `DisposableEffect` (checked in `compose-multiplatform` `html/core` source, 18 Oct 2026). |
| Registry | `SimRegistry.kt` | `id → factory`. One entry per spec in `docs/sims/`. |
| One sim | `<SimName>Sim.kt` + `<SimName>Config` | All tunable numbers from the spec live in **one named config object** (`sim-engineer` rule), with the same names and values as the spec's parameter table. |

**Block to widget.** Superseded for the source form by ADR 009 §1: authors write one ```` ```sim ```` block (`docs/04` §3.4, §4.3), which becomes `{ "t": "sim", "id", "tpl", "spec", "try" }`; `SimHost` tries a bespoke sim for `id`, then the template for `tpl`. The comment-marker text below is history (SM0 now rejects the marker). Old form: authors wrote a SIM comment marker. The page renders `SimHost("sim-id")` at that place. Settled in ADR 005 and `docs/07` §6.2: `renderLessons` cuts the body at the exact marker line (regex in `docs/07` §6.2 R1) and writes a `{ "t": "sim", "id": "sim-id" }` block into the leaf's lesson JSON (`/lessons/<id>.json`; the JSON also lists all IDs in `sims`). The one lesson page (`K/pages/Lesson.kt`) walks the blocks in a single composition and, for a `sim` block, renders `SimFrame` hosting `SimHost(id)`; until a sim exists (before Phase 5) it shows a placeholder frame. No Kobweb Markdown widget line is generated, and `/content` is never changed. The sim's Kotlin code stays in the main bundle (§3.1); the JSON carries only the ID. A sim in the exported static HTML is only its surrounding text (Try this, What you should notice), because the static body holds the `html` blocks and no widget; `SimHost` mounts when the page hydrates.

### 3.3 Rendering rules

| Rule | Value |
|---|---|
| Smooth, not pixelated | Canvas uses normal smoothing; backing size = CSS size × `devicePixelRatio`. The pixel-art style belongs to the tree only (D6, `docs/02` §7). |
| Colours | Read once at mount from the CSS custom properties of `docs/02` tokens with `getComputedStyle(frameElement).getPropertyValue(name)`, the documented way to read a custom property in JavaScript (MDN "Using CSS custom properties"). The CSS name of a token is its dotted name with `.` replaced by `-` and `--` in front: `color.on-surface` is `--color-on-surface`, `color.branch.kotlin.tint` is `--color-branch-kotlin-tint` (rule and source: `docs/02` §3.1). Sim code builds `name` as `"--" + colorOnSurfaceVar.name` from the Kotlin variable object and never types the string. Phase 0 test: the DevTools check in `docs/02` §3.1 (one command per name); then log `color.on-surface` from a test sim frame and compare with `docs/02` (`#1A1C1E`). No colour literal in any sim file. |
| Allowed tokens | `color.surface`, `color.on-surface`, `color.on-surface-muted`, `color.outline`, the active branch ink `color.branch.<slug>` and its `.tint`, `color.feedback.correct`, `.wrong`, `.hint` and their tints, `color.focus-ring`. World tokens (`color.bark`, `color.leaf.*`, `color.fruit`) are **not** used in sims. If a sim needs another colour, the spec asks `architect` for a token in `docs/02`; it never picks a hex value. |
| Never colour alone | Every state difference also has a shape or text cue: dashed vs solid outline, a label, an icon (`docs/02` §1.4). |
| Text | Labels, counts and messages are **DOM elements** over the canvas (`font.ui`, `type.label` or `type.body`), never drawn into the canvas, so they are crisp, translatable later and readable by screen readers. |
| Strokes | Body outline 2 units in `color.on-surface`; hidden rules (sensors, springs) drawn only when "Show the rules" is on, as dashed `color.on-surface-muted` lines. |
| Shape language | Flat fills, no gradients, no shadows inside the canvas, no glow (`docs/02` §1.5). |
| Motion outside physics | Lid flips, labels, overlays use `motion.spatial` / `motion.effects` springs (`docs/02` §4.4). Nothing scripted lasts over 900 ms or blocks input. |

Rejected: `Matter.Render` (debug look, its own colour handling, not token-driven). Rejected: SVG for physics bodies (DOM cost per frame grows with body count; canvas is cheaper at 40 bodies).

---

## 4. Sim kinds

Every spec names exactly one kind. The kind decides what is loaded and what "honest" means.

| Kind | Loads | Use when | Example IDs |
|---|---|---|---|
| `physics` | Matter.js | A true rule of the idea can be **felt** through contact, gravity, a spring or a sensor (§5) | `null-lunch-box`, `call-stack-tube`, `gc-strings` |
| `animation` | nothing extra | Physics would add false behaviour (wobble, bounce, chance) to an idea that is exact and step-by-step; **every `profiling` sim** (§4.5) | `bubble-sort-bars`, `bst-insert-steps` |
| `code-to-screen` | nothing extra | **Android behaviour**: what changes on screen when the code changes (L15 amended). Not used on `profiling` leaves (§4.5) | `remember-rotate`, `lifecycle-timeline` |
| `3d` | Three.js | Depth itself carries meaning (layers above layers) and a flat picture would hide it (L21) | `memory-hierarchy-tower`, `osi-layer-stack` |

A sim may combine `code-to-screen` with physics bodies only if the spec explains why; in that case its kind is `physics` and it carries the code-to-screen rules of §4.3 as well.

### 4.1 `physics`

Matter.js world, standard numbers from §2.2, standard controls from §7.1. The spec must pass the honesty check (§5.2).

### 4.2 `animation`

A scripted sequence of states. Each state is one row in the spec's script; the learner moves through states with Next / Back / Play. No engine. Motion between states uses `motion.spatial`. This is the right kind for sorting, tree and graph algorithms, scheduling and lifecycles, because those ideas are discrete: each step is exactly one rule, and nothing "settles".

### 4.3 `code-to-screen` (how Android behaviour is shown)

There is no emulator and no simulator (L15 amended). Instead:

| Part | Rule |
|---|---|
| Code panel (left) | A short read-only Kotlin snippet, `font.code`, with **one to three toggles** that switch between pre-written variants (for example: "with `remember`" / "without `remember`"; "collect in `lifecycleScope`" / "collect in `GlobalScope`"). The learner does not type here; free typing belongs to the editor (`docs/06`). |
| Screen (right) | A drawn phone-shaped frame in chrome style (`shape.xl`, `color.outline`), clearly a drawing. It shows only the UI the snippet produces. Caption under it, always visible: **"A picture of what Android does. Not a real phone."** |
| Timeline (bottom) | A row of event chips in time order (for example `onCreate`, `onStart`, `onResume`, "rotate", `onPause`…). The current event is highlighted with branch ink plus a bold label. |
| Actions | Buttons for the real-world events the idea is about: "Rotate the phone", "Press Home", "Tap +1", "Lose network". Each action replays the timeline from the current variant. |
| Change the code, see the change | Flipping a toggle **resets the screen and replays the same actions**, so the learner sees one difference caused by one code change. This is the core of the kind. |
| Source of truth | Every behaviour shown must be traced in the spec to an official Android Developers or Kotlin page (URL and the sentence it relies on). `reviewer` checks these like lesson facts (CLAUDE.md rule 4). |
| What it never does | Run Kotlin, import `android.*`, imitate a full device (status bar, launcher, real app icons), or claim timing it does not know. |

Rejected: an Android emulator or simulator in the browser (L15 amended: too heavy; `docs/00` §9). Rejected: screen-recorded videos as the main device: they cannot show "change the code, see the change" on one screen; reference videos stay optional in the lesson (L15).

### 4.4 `3d` (L21)

| Rule | Value |
|---|---|
| Library | Three.js, self-hosted, loaded on demand as **two files**, `three.core.js` plus the `three.module.js` entry module, through an inline `<script type="module">` (§3.1). Candidate `0.186.1` (npm `latest`, 18 Oct 2026). Size: §6.3. Licence MIT (npm registry `license: "MIT"`, 0.186.1). |
| Physics | None in v1. Motion is scripted (states and tweens), exactly like `animation`. |
| When allowed | Only if the spec answers "what does depth mean here?" with a concept (for example "each layer wraps the one below it"). Decoration is not a reason. |
| Camera | Fixed or orbit with mouse drag; buttons for "Front", "Side", "Top"; arrow keys orbit when the canvas is focused. No auto-orbit. |
| Scene budget | ≤ 2,000 triangles, ≤ 20 meshes, one directional light plus ambient, no shadows, no post-processing; render only while something moves. |
| Look | Flat shading in token colours; text labels are DOM overlays, never 3D text. |
| Reduced motion | Static camera, state changes as cuts with 150 ms fade. |
| Fallback | A flat 2D version of the same states (the text equivalent plus the `animation` kind) when **WebGL 2** is unavailable. Three.js `WebGLRenderer` has not supported WebGL 1 since r163 (r163 release notes "Remove WebGL 1 support"; Migration Guide 162 → 163). Detection: the check of the official addon `three/addons/capabilities/WebGL.js` `isWebGL2Available()` ("Returns `true` if WebGL 2 is available", threejs.org docs): `window.WebGL2RenderingContext` exists and a test canvas returns a `webgl2` context, any exception = unavailable. `SimEngineLoader` runs it itself **once per page, before** downloading Three.js (no download when it fails), so the addon file is not vendored. |

Rejected for v1: 3D physics (Rapier 3D, cannon-es): extra 100+ KB and no catalogue entry needs it.

### 4.5 `profiling` leaves use `animation` (author decision, 18 Oct 2026)

| Rule | Value |
|---|---|
| Kind | Every sim listed in the `sims` of a `profiling` leaf is kind `animation`. No `code-to-screen`, `physics` or `3d` sim is placed on a `profiling` leaf. |
| Why | Profiling teaches how to read recorded measurements (traces, flame charts, frame ticks, memory lines). A recording is a fixed sequence of states, which is exactly what `animation` steps through. A `code-to-screen` sim would suggest it knows real timing, which §4.3 forbids. |
| Reuse | A `profiling` leaf does not reuse a `code-to-screen` sim from another branch. If it needs the same picture, `physics-designer` writes an `animation` spec that steps through a recording of it. |
| Numbers | Every number shown (ms, MB, frame counts) is labelled as an example recording, with the source it imitates written in the spec. |
| Check | `reviewer` fails a `profiling` leaf whose sim spec has any other kind. |

---

## 5. Mapping physics to a concept

### 5.1 The method (seven steps, in the spec's section 2)

1. **One concept, one misconception.** Copy the misconception from the leaf's roadmap row (`docs/08` §2.1 leaf table). The sim exists to remove it.
2. **List the rules.** Write the two to four rules that are always true for the concept (for a stack: "only the top item can be taken"; "the last in is the first out"; "there is a size limit").
3. **Pick a physical behaviour per rule** from the vocabulary in §5.3. The learner must be able to *feel* the rule by pushing: a rule that is only written in a label is not shown.
4. **Name the extras.** Physics always adds things the concept does not have (bounce, wobble, friction, chance). List each extra, then remove it with numbers (restitution 0, high air friction, no gravity, sleeping) or explain in "Where the metaphor stops" why it cannot be mistaken for a rule.
5. **Map the break.** The "Break it" action must cause a real failure of the concept (crash, overflow, lost data, race, frozen screen), not a fun explosion.
6. **Check the analogy.** The sim uses the lesson's one running analogy (`docs/04` §3.2), with the same object names. No second analogy.
7. **Choose the kind.** If step 4 leaves an extra that could be learned as truth, switch to `animation` (§5.4). Android behaviour is always `code-to-screen` unless step 3 finds a true physical rule. A `profiling` leaf is always `animation` (§4.5).

### 5.2 Honesty check (must be answered "yes" in every spec)

- [ ] Every rule of step 2 is felt through a physical behaviour or a visible check, not only through text.
- [ ] Every extra of step 4 is removed or named in "Where the metaphor stops".
- [ ] Doing the same actions twice gives the same **state** (randomness appears only if the concept itself is random, for example thread timing, and then the sim offers "Replay with the same timing").
- [ ] The break shows a real failure of the concept.
- [ ] A learner who only watched the sim and read nothing would not believe a false sentence about the concept. The spec writes down the most likely false sentence and how the sim prevents it.

### 5.3 Physical vocabulary

| Physical behaviour (Matter.js feature) | Stands for | Typical use |
|---|---|---|
| Static body (`isStatic`) | a fixed place: a memory slot, a box, a lane, a rule that does not move | boxes, tubes, shelves |
| Dynamic body | a value, an object, a task, a packet | balls, sandwiches, cards |
| Gravity | "things settle to a default position"; order by arrival in a tube | stacks, buckets |
| No gravity + steady push (`applyForce` every step) | flow in one direction | queues, pipes, streams |
| Sensor (`isSensor`) | a check that notices without touching: "is something in this box?" | null checks, bucket membership, "arrived" |
| Stiff constraint (`stiffness 1`) | a reference: this label points at that object | variables, references, links |
| Spring constraint (`stiffness < 1`) | a rule that pulls back: type rules, invariants, back-pressure | non-null box, bounded buffer |
| Removing a constraint | losing a reference | garbage collection, dangling links |
| Collision | competing for the same place | races, locks, hash collisions |
| Gate (static body switched on/off) | a lock, a permission, a closed connection | mutex, semaphore, firewall |
| Friction, air friction | cost or delay | latency, slow work |
| Sleeping body | idle, waiting, costs nothing | suspended coroutine |
| Capacity of a container | a limit | stack size, buffer size, heap size |
| Freezing the whole world | the program stopped | crash, ANR, deadlock |

Things physics must **not** stand for: speed of an algorithm (bodies falling faster does not mean faster code), size in memory as weight unless the spec says so, randomness of bounce as any concept, and "settling" as sorting.

### 5.4 When physics misleads, use `animation`

| Concept | Why physics misleads | Use |
|---|---|---|
| Sorting algorithms | Settling by weight is a different process ("gravity sort"); bounce hides the comparison and swap steps | `animation`: bars, one comparison per step |
| Binary search tree, heap | A hanging mobile balances by mass, a heap by order; the learner would learn the wrong balance rule | `animation`: node moves along edges |
| Graph search (BFS, DFS) | Spreading water or rolling balls find paths by physics, not by the queue or stack rule | `animation`: frontier coloured step by step |
| Big-O growth | Speed of a falling body says nothing about step counts | `animation`: counter plus plot |
| Activity / Fragment lifecycle | Discrete states with exact order; a bouncing ball suggests chance | `code-to-screen` with timeline |
| Compose recomposition | It is a rule about which functions run again, not a force | `code-to-screen`, recomposed parts flash |
| Process scheduling (round robin) | Exact turn-taking; collisions would suggest contention that is not there | `animation`: wheel of turns |

### 5.5 Metaphor catalogue

Proposals for `physics-designer` and `curriculum-designer`. Each row is a starting point; the spec still runs §5.1. "Break" is the one failure the sim lets the learner cause.

| Concept (branch) | Kind | Metaphor and what moves | Learner pushes / breaks | Where the metaphor stops |
|---|---|---|---|---|
| Null and nullable types (`kotlin`) | physics | `null-lunch-box`: two labelled boxes; a sandwich can be dragged in and out; a spring returns the sandwich to the "always" box | Break: count slices in an empty box without looking | Kotlin refuses the empty "always" box before the program starts, not by pulling it back while running (§10) |
| Variable and assignment (`foundation`) | physics | a labelled box holds one ball; dropping a new ball pushes the old one out to a bin | drop a ball into a full box | real variables have no "push"; the old value is simply replaced |
| Value vs reference (`kotlin`) | physics | name tags tied by stiff strings to balls; two tags can tie to one ball; pushing the ball moves it for both tags | cut a string; tie two tags to one ball and change it | memory has no strings; a reference is an address |
| Garbage collection (`kotlin`, `os`) | physics | `gc-strings`: balls hang from fixed pins (roots) by strings; cut every string to a ball and press "Run GC": it drops into a bin | cut strings, run GC | GC runs at times the program does not choose; the sim lets the learner press the button |
| Memory leak (`android-core`) | physics | a long-lived pin ("lives as long as the app") keeps a string to a screen's ball; the tank of balls fills | open the screen ten times | real heaps are not a tank; the limit is real but larger |
| Stack and call stack (`dsa`, `kotlin`) | physics | `call-stack-tube`: a vertical tube, gravity on, open top; only the top ball can be lifted out | Break: keep calling, the tube overflows (stack overflow) | the real limit depends on the device; the sim shows 8 slots |
| Queue (`dsa`) | physics | a horizontal conveyor, no gravity, balls enter at the back and leave at the front | try to take from the middle (the gate blocks it) | real queues do not move by themselves |
| Array and index (`dsa`) | physics | a row of fixed numbered cups; inserting in the middle pushes every later ball one cup right | insert at index 0 in a long row and watch the work | the shifting cost is real; the speed is not |
| Linked list (`dsa`) | physics | beads on short stiff strings; insert = cut one string and tie two | drop the head bead's string: the whole chain falls away | no gravity in memory; "falls away" means "unreachable" |
| Hash map buckets (`dsa`) | physics | `hash-chute-buckets`: a chute with a deflector set by the key's hash number drops each ball into one of 8 cups | give two keys the same hash: both land in one cup | the deflector stands for a function; real hashing is arithmetic |
| Threads sharing data, race (`concurrency`, `os`) | physics | two lanes push balls toward one counter slot; whoever arrives second overwrites | Break: remove the gate (lock); "Replay with the same timing" | real scheduling is decided by the system; the sim seeds it so it can replay |
| Lock and deadlock (`concurrency`, `os`) | physics | two gates, two balls; each ball holds one gate and waits for the other; the world freezes | close both gates in the wrong order | a deadlock does not move at all; the frozen world is the honest picture |
| Suspend vs block (`concurrency`) | physics | a thread lane with a side shelf: a suspended task rests (sleeping body) on the shelf and the lane keeps moving; a blocking task sits in the lane | toggle "blocking call" | a suspended coroutine is not stored "beside" a thread; it is saved state |
| Flow and back-pressure (`concurrency`) | physics | a tap emits balls into a pipe; a collector at the end; a spring buffer squeezes when the collector is slow | turn the tap up, slow the collector | cold flows do not run before collection: the tap stays closed until the collector is attached |
| Main thread and frame drops (`android-core`; for `profiling` see the frame-ticks row below) | physics | a conveyor that must move one tray every frame; a heavy ball on the tray stalls it; stalled trays are counted as dropped frames | put heavy work on the main lane | frame timing numbers in the sim are scaled for visibility |
| Network latency and loss (`networking`, `app-networking`) | physics | packets slide along a rope with friction; a gap in the rope drops packets; an "ack" ball returns | widen the gap; add a retry | real packets do not fall; "lost" means not delivered |
| Activity lifecycle (`android-core`) | code-to-screen | `lifecycle-timeline`: phone drawing plus callback chips | Rotate, Home, Back | timing between callbacks is not to scale |
| Compose state and `remember` (`ui`) | code-to-screen | `remember-rotate`: a counter on a drawn screen; toggle `remember` / `rememberSaveable`; rotate | tap +1 three times, rotate | shows the documented outcome, not how Compose stores it |
| Recomposition scope (`ui`) | code-to-screen | parts of the drawn screen flash when they run again | move state up or down one level | flashing marks "ran again", not "redrew pixels" |
| Process scheduling (`os`) | animation | a turn wheel hands the CPU to each process for a slice | change the time slice | — |
| Sorting (`dsa`, `daa`) | animation | bars, one compare-or-swap per step, step counter | Next / Back, change input order | — |
| Reading a frame trace (`profiling`) | animation | a recorded row of frame ticks under a 16 ms budget line; stepping moves a cursor tick by tick and names the work inside each over-budget frame | step to the first over-budget frame; switch the example recording | the recording is an example; real numbers come from the learner's own profiler |
| Memory hierarchy (`os`) | 3d | a tower of shelves: registers, cache, RAM, disk; a fetch climbs down and back | ask for data that is not cached | distances are not to scale; the spec states the real orders of magnitude in text |
| Layered protocols (`networking`) | 3d | a parcel wrapped in one box per layer and unwrapped on the other side | remove one layer | real headers are bytes, not boxes |

Reuse rule (`docs/08` §6): one sim file can serve several leaves through its config (for example `call-stack-tube` with `slots = 8` for `dsa` and with recursion labels for `kotlin`). A reused sim keeps one ID and one spec; the spec lists every leaf and its config.

---

## 6. Loading, running and performance

### 6.1 Life of a sim on a lesson page

| Stage | Trigger | What happens |
|---|---|---|
| 0. Page load | HTML arrives | `SimFrame` is rendered at its final size (16:9 box, 720 CSS px wide = `layout.lesson.wide`, `docs/02` §4.5) so nothing shifts later (CLS). Inside: the sim title and the text equivalent (§7.3). **0 engine bytes**, no canvas work. |
| 1. Near | frame within 400 px of the viewport (`IntersectionObserver` with `rootMargin: "400px"`; a second observer with `threshold: [0.1, 0.5]` drives stages 2 and 3; the callback fires each time the visible ratio crosses a listed value, web.dev "IntersectionObserver's coming into view") | Load Matter.js (`physics`) or Three.js (`3d`) if not yet loaded; build the world; draw the initial state once. Stays **paused**. |
| 2. Visible | at least 50% of the frame visible | Start the runner (autoplay), unless reduced motion is on (§7.2). |
| 3. Hidden | under 10% visible, or the browser tab is hidden (`document.visibilityState`) | `Runner.stop` (cancels the frame loop; `enabled = false` would keep requesting frames, V1). State is kept. On return, `Runner.run` resumes; the long gap is replaced by the last frame delta, so nothing jumps. |
| 4. Leave | the learner navigates away | Dispose: stop the runner, clear the engine, remove every listener and the canvas. No timers survive. |

Rules:

- **One running sim per page.** If two are visible, the one the learner touched last runs; the other pauses.
- **At most two sims per leaf** (`docs/08` §3 budget table: 0–1 normal, 2 maximum).
- The page never waits for a sim: the lesson text is complete without it (`docs/04` §3.4, P10).
- Home and any page with `sims: []` load no sim library and run no sim code (`docs/07` §8).
- A live page has either a working sim or no See it move section (`docs/04` §4.3). The loader's failure message (§3.1) is for network failures only, not for unbuilt sims.

### 6.2 Per-sim performance budget (default; a spec may lower it, never raise it)

Measured on the `docs/07` §8 reference laptop (4 cores, integrated GPU, 1920×1080, DPR 1 and 2), Chrome.

| Metric | Budget |
|---|---|
| Frame rate while running | 60 fps; frame time ≤ 16.7 ms p95 |
| Physics step | ≤ 4 ms p95 per frame (all engine updates in that frame) |
| Draw | ≤ 4 ms p95 per frame |
| Dynamic bodies | ≤ 40 (`physics`) |
| Constraints | ≤ 20 |
| Sensors | ≤ 10 |
| Engine updates per frame | ≤ 2, enforced by `maxUpdates: 2` (V2); the default `maxFrameTime` `1000/30` ms stays as the time budget |
| Idle cost | 0 frames requested when every body sleeps and no tween runs: `SimWorld` calls `Runner.stop` when all dynamic bodies are sleeping, and `Runner.run` again on pointer-down on the canvas, keyboard pick-up or any sim button (V1, V3) |
| Time to first drawn state after stage 1 | ≤ 300 ms (engine already cached: ≤ 100 ms) |
| Main-thread long task caused by a sim | none over 50 ms |
| Memory after dispose | returns to within 1 MB of the pre-sim heap after 3 open/leave cycles |
| `3d` scene | ≤ 2,000 triangles, ≤ 20 meshes, 1 light + ambient |

### 6.3 Byte budget

| Item | Budget (gzip) | Loaded |
|---|---|---|
| Matter.js 0.20.0 | ≤ 30 KB | on demand, once per visit (cached) |
| Three.js: `three.core.js` + `three.module.js` together, minified | ≤ 200 KB combined. Known: 0.186.1 bundled as one minified file is 736,231 bytes, **184,898 bytes gzip** (bundlephobia, 18 Oct 2026). ⚠️ VERIFY (unknown: the size of the two files minified separately, which can be a little larger than one bundle). Test, at the first `3d` spec (Q3): minify the pinned pair, `gzip -9` each, sum; fail if over 200 KB. | on demand, only on pages with a `3d` sim |
| Shared sim runtime (Kotlin: host, loader, `SimWorld`, renderer) | ≤ 12 KB | in the main bundle (§3.1). ⚠️ VERIFY (unknown: real gzip size; this row and the next are targets, as `docs/07` §8 says). Phase 5 test: gzip the production `kotree.js` before and after the first sim lands and subtract; CI repeats the diff for every new sim. |
| One sim's Kotlin code and config | ≤ 6 KB | in the main bundle |
| One sim's images or audio | none by default; if a spec needs an image, ≤ 30 KB, in `/sims/<sim-id>/` | on demand |

Sims use no sound in v1.

### 6.4 Acceptance (loading and performance)

- [ ] Network panel on `/` and on a leaf with `sims: []`: no request to `/vendor/`.
- [ ] Network panel on a leaf with a sim: the engine request starts only after scrolling to within 400 px of the frame.
- [ ] Lighthouse CLS on that leaf ≤ 0.05 with the sim loading (`docs/07` §8).
- [ ] Performance panel, 10 s of dragging: frame time ≤ 16.7 ms p95; no long task over 50 ms from sim code.
- [ ] Switching tabs pauses the sim (no frames requested while hidden).
- [ ] With the pointer over a running sim, the mouse wheel scrolls the lesson page (Matter's `wheel` handler removed, V4).
- [ ] Network panel: the vendored `matter-0.20.0.min.js` transfers ≤ 30 KB gzip.
- [ ] Opening and leaving the page three times leaves no extra canvas, listener or growing heap.

---

## 7. Controls and accessibility

### 7.1 Standard controls (every sim, in `SimFrame`)

| Control | Key (when focus is inside the frame) | What it does | What it reveals |
|---|---|---|---|
| Play / Pause | `Space` | Runs or freezes time (`Runner.run` / `Runner.stop`). While paused nothing can be dragged, because Matter's drag works only during engine updates (V3); pointer-down shows the hint "Press Play to move things." | That the idea is about change over time; pausing lets the learner look |
| Step | `.` (full stop) | Advances 10 engine updates (1/6 s) for `physics`, one state for `animation` / `code-to-screen` | The order of events |
| Reset | `R` | Returns to the initial state from the spec | That the same actions give the same result |
| Speed: Normal / Slow | `S` toggles | Slow = one quarter speed: `engine.timing.timeScale = 0.25` ("the global scaling factor of time for all bodies", default `1`, Engine docs; README feature "Time scaling (slow-mo, speed-up)"). Paths differ slightly from Normal, so tests compare states only (§2.1) | Fast events (collisions, crashes) |
| Show the rules | `H` toggles | Draws hidden sensors and springs as dashed lines with short DOM labels | The invisible rule behind the behaviour |
| Text description | button, no key | Expands the text equivalent (§7.3) | Everything, without motion |

Sim-specific controls come from the spec (drag, buttons, toggles). Each one must name, in the spec, **what it reveals**. A control that reveals nothing is removed.

Keyboard shortcuts work only while focus is inside the frame, never globally, and never with modifier keys, so they do not clash with the browser or screen readers. Every shortcut also has a visible button.

### 7.2 Reduced motion (`prefers-reduced-motion: reduce`)

| Normal | Reduced motion |
|---|---|
| Autoplay at stage 2 | No autoplay. The frame shows the initial state and a Play button. This counts as "idle", not "paused by the learner": a pointer-down or keyboard pick-up still starts the runner for that action (V3). The preference is read with `window.matchMedia("(prefers-reduced-motion: reduce)")` and a `change` listener, because JavaScript animations must react to changes themselves (web.dev "prefers-reduced-motion"). |
| A dropped body falls and settles | The engine is stepped off-screen until every body sleeps (max 300 updates), then the settled state is drawn with a 150 ms fade (`motion.effects`) |
| Spring pull-back, lid flips, sliding | Jump to the end state with a 150 ms fade |
| Camera moves (`3d`) | Cuts |
| Flashing highlights (`code-to-screen`) | A steady outline and a text label |

The learner can still press Play to see the full motion; reduced motion removes motion that starts by itself, not the learner's choice. Nothing flashes more than 3 times per second in any mode.

### 7.3 Keyboard, screen reader, text equivalent

| Need | Rule |
|---|---|
| Draggable bodies | Each draggable body has a DOM **proxy button** placed over it (`aria-label` = the analogy name, for example "Sandwich B, on the tray"). `Enter` or `Space` picks it up; `←` / `→` move it between named **drop places** defined in the spec; `Enter` drops; `Esc` puts it back. Pointer-drag and keyboard use the same drop places, so results are identical. |
| Focus | Visible `color.focus-ring` (3 px, 2 px offset) on every control and proxy; a picked-up body also gets a dashed outline in the canvas. Tab order is written in the spec. |
| Live region | One polite `aria-live` region per sim. Every state change the spec lists produces one plain sentence (examples in §10.6). Crashes and refusals use the same polite region, not an alert. |
| Text equivalent | A short ordered list in the frame (collapsed until the canvas is ready, then behind "Text description"): the initial state, each learner action and its result, and the "What you should notice" sentence. It must teach the same thing with the canvas off. |
| Canvas | `role="img"` with an `aria-label` describing the current state in one sentence, updated on every state change. |
| Pointer | Mouse and touch both drag (Matter.js `Mouse` listens to `mousedown/move/up` and `touchstart/move/end` on the canvas). Minimum hit size for a draggable body: 44 × 44 CSS px at the narrowest supported frame width. That is WCAG 2.2 SC 2.5.5 Target Size (Enhanced, level AAA, "at least 44 by 44 CSS pixels") and `docs/02` "minimum click target 44 x 44 px"; the AA floor, SC 2.5.8, is 24 × 24 CSS px (w3.org Understanding pages, 18 Oct 2026). Rule for specs: every draggable body is at least 44 × 44 world units. The narrowest supported `SimFrame` is **720 CSS px** (`layout.lesson.wide`, viewport floor 1024 px, `docs/02` §4.5, settled 18 Oct 2026), so the scale is always 1 and 44 world units are 44 CSS px; specs need no scale arithmetic. Below a 1024 px viewport nothing is promised (L8); `SimHost` logs a dev warning when any draggable body falls below 44 CSS px there, and `reviewer` fails a sim with any draggable body below 24 world units. Phase 0 test: at 1024, 1280 and 1920 px viewports the `SimFrame` measures 720 px wide (DevTools `$0.getBoundingClientRect().width`). No invisible hit-slop is used: the drawn body is the hit area. **Mouse mapping (V5):** Matter's `Mouse` divides the pointer offset by `clientWidth / canvas.width × data-pixel-ratio` (attribute on the canvas, default 1). So the host sets `data-pixel-ratio` to the `devicePixelRatio` used for the backing store, which makes `Mouse` report CSS px, and calls `Mouse.setScale(mouse, { x: 720 / cssWidth, y: 405 / cssHeight })` on mount and on every resize (offset `0, 0`). **Touch (V6):** `Mouse` reads only `changedTouches[0]`, so a second finger moves the drag point; the host's capture-phase touch listener on the frame wrapper stops touch events that do not belong to the first active touch. |
| Colour | Never the only cue (§3.3). |
| Contrast | Labels and body outlines ≥ 4.5:1 against `color.surface`, using token pairs already measured in `docs/02`. |

---

## 8. Sim spec template (`docs/sims/<sim-id>.md`)

`physics-designer` copies this block, fills every field, and deletes nothing. Empty answers are written as "none" with a reason. Numbers are required wherever a field asks for them.

````markdown
# Sim: <sim-id>

> **Purpose:** spec for the sim `<sim-id>`, read by `sim-engineer` (build) and `reviewer` (check).
> **Read this when:** building, changing or reviewing this sim.
> **Depends on:** `docs/05` (rules), `docs/02` (tokens), leaf row(s) in `content/<branch>/_roadmap.md`.

| Field | Value |
|---|---|
| ID | `<sim-id>` (kebab-case, ≤ 32 characters, never renamed once a live leaf uses it) |
| Kind | `physics` \| `animation` \| `code-to-screen` \| `3d` |
| Used by leaves | `<branch>/<stem>/<leaf>` (config: …) |
| Branch ink | `color.branch.<slug>` |
| Status | `draft` \| `ready-to-build` \| `built` \| `reviewed` |
| Facts checked against | URLs (required for `code-to-screen`) |

## 1. Concept and misconception
- Teaches (one sentence):
- Misconception removed (copied from the roadmap row):
- Most likely false sentence a watcher could learn, and how this sim prevents it:

## 2. Metaphor and honesty (`docs/05` §5.1, §5.2)
- Analogy objects (same names as the lesson's Mental model):
- Rules of the concept → physical behaviour that makes each one felt:
- Extras the physics adds → how each is removed (with numbers) or named:
- Where the metaphor stops (one or two sentences, reused in the lesson if useful):
- Honesty check: five boxes from §5.2, each ticked with one line of reason.

## 3. World, bodies, constraints, parameters
- World: 720 × 405, gravity, timestep, iterations, sleeping.
- Body table: name · shape and size · static/dynamic/sensor · density (and resulting mass) · restitution · friction · frictionAir · collision category and mask · start position.
- Constraint table: name · bodies · stiffness · damping · length · when it exists.
- Forces applied each step (if any), speed cap.
- Tokens per body and label.
- Config object name and every tunable number (same names as `sim-engineer` will use).

## 4. Controls
- Standard controls (§7.1): any change or "none".
- Sim controls table: control · pointer · keyboard · what it reveals.
- Drop places (for keyboard and pointer).
- Tab order.

## 5. Script
Table: step · initial state or learner action · what moves (bodies, numbers) · live-region sentence · what the learner should notice.
Must cover every "Try this" item of the lesson in order.

## 6. Edge cases, performance, accessibility
- Edge cases table: case · what the sim does.
- Performance: body, constraint and sensor counts against §6.2; anything lowered.
- Reduced motion: per action, the end state shown.
- Text equivalent (full text).

## 7. Acceptance criteria
Checklist a reviewer can test by hand in under 10 minutes, including keyboard-only and reduced-motion runs.
````

---

## 9. IDs, files and workflow

| Item | Rule |
|---|---|
| Sim ID | kebab-case, unique site-wide, ≤ 32 characters, named **metaphor + idea** (`null-lunch-box`, `call-stack-tube`). It appears in leaf frontmatter `sims` (`docs/00` §8.2), so it never changes once a live leaf uses it (same rule as slugs). |
| Spec | `docs/sims/<sim-id>.md`, from the §8 template. |
| Code | `site/src/jsMain/kotlin/dev/kotree/site/sims/` (`docs/07` §2.2): `engine/` shared files, one `<SimName>Sim.kt` per sim, one entry in `SimRegistry.kt`. |
| Vendor files | `site/src/jsMain/resources/public/vendor/` |
| Roadmap cell | `sim: <idea>` or `none: <reason>` (`docs/08` §6). For Android behaviour, write `sim: code-to-screen <idea>`. On `profiling` leaves, write `sim: animation <idea>` (§4.5). |
| Order of work | roadmap `sim:` cell → `physics-designer` writes the spec (status `ready-to-build`) → `sim-engineer` builds (status `built`) → `reviewer` checks the sim with the leaf (status `reviewed`) → `housekeeper` (`docs/09`). |
| Build check (proposed for `validateContent`, owner `architect`) | Fail the build if a live leaf lists a sim ID that has no `SimRegistry` entry or no `docs/sims/<id>.md`, or if a `SIM` marker ID is missing from frontmatter `sims` (`docs/04` §2). |

---

## 10. Worked spec: `null-lunch-box`

This is the filled template for the sim used by the worked lesson in `docs/04` §10 (`kotlin/null-safety/what-is-null`). `physics-designer` copies it to `docs/sims/null-lunch-box.md` when that leaf is scheduled; the copy then becomes the source of truth. It follows the lesson's analogy (a lunch box with a label) and its **Try this** list (four items, including "Look inside first", Q1).

| Field | Value |
|---|---|
| ID | `null-lunch-box` |
| Kind | `physics` |
| Used by leaves | `kotlin/null-safety/what-is-null` (default config) |
| Branch ink | `color.branch.kotlin` |
| Status | `draft` (becomes `ready-to-build` when copied to `docs/sims/`) |
| Facts checked against | `https://kotlinlang.org/docs/null-safety.html` (the same page `docs/04` §10 used; read 18 Oct 2026, page dated 28 Aug 2025). It says "The only possible causes of an NPE in Kotlin are": an explicit `throw NullPointerException()`, the `!!` operator, data inconsistency during initialization, and Java interoperation, which matches "Where the metaphor stops" (§10.2). `reviewer` re-reads the page when the leaf is written (CLAUDE.md rule 4). |

### 10.1 Concept and misconception

- **Teaches:** a place in a program can hold *nothing*; its label decides whether nothing is allowed; using nothing as if it were something stops the whole program, and looking first avoids that.
- **Misconception removed** (roadmap row, `docs/08` §2.1): "null is the same as 0 or an empty string." In the sim, counting slices in an empty box never answers "0 slices": it stops the app.
- **Most likely false sentence a watcher could learn:** "Empty boxes crash programs." Prevented by Try-this step 1 (emptying the "might be empty" box is allowed and nothing breaks) and by the "Look inside first" path (empty box, no crash).

### 10.2 Metaphor and honesty

- **Analogy objects** (same names as the lesson's Mental model): lunch box = a place that holds a value; sandwich = the value; empty box = nothing here; label "always has a sandwich" / "might be empty" = what kind of box it is; "Count the slices" = using the value.
- **Rules → physical behaviour:**

| Rule of the concept | How the learner feels it |
|---|---|
| A "might be empty" place may hold nothing | Sandwich B can be dragged in and out of that box freely; its state chip switches between "Holds a sandwich" and "Empty"; nothing else happens |
| An "always" place can never hold nothing | Sandwich A is tied to its box by a soft spring: dragging it out feels resistance, and on release it is put back. The box's chip never shows "Empty" |
| Using nothing as something stops the program | "Count the slices" on the empty "might be empty" box freezes the whole world, not only that box |
| Looking first is safe | With "Look inside first" ticked, the same count on the empty box answers "Nothing here, so nothing to count" and the world keeps running |

- **Extras the physics adds, and how they are removed:** bounce → restitution 0 on every body (combined `max` stays 0); sliding → friction 0.6, static friction 0.8; spinning → sandwiches cannot rotate (`inertia: Infinity` in the `Bodies.rectangle` options: the Body docs say inertia "Can be manually set to `Infinity` to prevent rotation of the body", and `Body._initProperties` applies `options.inertia || body.inertia`, so the option wins over the computed value; 0.20.0 source); gravity and weight have no meaning in the concept, they only make dragging feel natural (named below).
- **Where the metaphor stops:** Kotlin reads the labels *before* the program runs, so a real "always" box is refused at that moment, not pulled back while running; the sim shows the refusal as a spring because a learner can feel it. In real Kotlin the label check also stops most "count without looking" code from being written at all; the crash gets through when code forces it (an operator met in a later leaf) or when the value comes from code Kotlin cannot check. A real app closes instead of freezing. Gravity and the weight of a sandwich mean nothing.
- **Honesty check:**
  - [x] Every rule is felt (table above).
  - [x] Every extra is removed with numbers or named in "Where the metaphor stops".
  - [x] Same actions give the same state: no randomness; outcomes depend only on which box holds which sandwich.
  - [x] The break (count on an empty box without looking) is the real failure of the concept.
  - [x] The likely false sentence is named and prevented (10.1).

### 10.3 World, bodies, constraints, parameters

World: 720 × 405 units; gravity `y = 1`, `scale = 0.001`; Runner `delta = 1000/60` ms; iterations default (position 6, velocity 4, constraint 2); sleeping on; speed cap 25 units per update. Four standard world walls (§2.2). No Kotlin syntax appears in the sim, because See it move comes before Build the logic (`docs/04` §3.3).

Bodies (centre x, y; sizes in units):

| Name | Shape, size | Type | Density (mass) | Restitution | Friction / static / air | Category → collides with | Start | Token |
|---|---|---|---|---|---|---|---|---|
| Table | rect 720 × 40 | static | — | 0 | 0.6 / — / — | `SCENE` → all | (360, 385) | fill `color.surface`, outline `color.on-surface` |
| Box A ("always has a sandwich"): left wall, right wall, floor | walls 8 × 58, floor 156 × 8; inside 140 × 50, open top | static | — | 0 | 0.6 | `SCENE` → all | outer x 60–216, y 307–365 | outline `color.branch.kotlin` (solid) |
| Box B ("might be empty"): same three parts | same | static | — | 0 | 0.6 | `SCENE` → all | outer x 504–660, y 307–365 | outline `color.branch.kotlin` (solid) |
| Tray shelf and two legs | shelf 160 × 8; legs 8 × 86 | static | — | 0 | 0.6 | `SCENE` → all | shelf (360, 280); legs (300, 322), (420, 322) | outline `color.on-surface-muted` |
| Sandwich A | rect 96 × 44 (meets the 44 × 44 px hit rule of §7.3 at a 720 px frame), corner chamfer 6 (option `chamfer: { radius: 6 }`; `Bodies.rectangle` passes `chamfer.radius` to `Vertices.chamfer`, 0.20.0 source) | dynamic, no rotation | 0.002 (≈ 8.4) | 0 | 0.6 / 0.8 / 0.03 | `SANDWICH` → `SCENE`, `SANDWICH` | (138, 335), resting on Box A floor (floor top y 357, minus half-height 22) | fill `color.branch.kotlin.tint`, outline `color.branch.kotlin`, one line across the middle = 2 slices |
| Sandwich B | same | same | same | 0 | same | same | (360, 254), resting on the shelf (shelf top y 276, minus 22) | same |
| Sensor A | rect 140 × 50 | sensor, static | — | — | — | detects sandwiches | (138, 332) | drawn only with "Show the rules" (dashed `color.on-surface-muted`) |
| Sensor B | rect 140 × 50 | sensor, static | — | — | — | detects sandwiches | (582, 332) | same |
| Sensor "on top of A" | rect 140 × 40 | sensor, static | — | — | — | detects Sandwich B | (138, 287) | same |

Constraint:

| Name | Between | Stiffness | Damping | Length | When it exists |
|---|---|---|---|---|---|
| Always rule | world point (138, 335) and Sandwich A centre | 0.02 | 0.1 | 0 | always (it is at rest while A sits in its box) |

Fit check after the height change (40 → 44): the box inside is 140 × 50 (y 307–357), so a resting sandwich spans y 313–357 and sits 6 units below the rim; `boxInnerHeight 50` is unchanged. Sandwich B resting on A has its centre at y 291, inside Sensor "on top of A" (y 267–307). Sensors A and B (y 307–357) still contain a resting sandwich's centre (y 335).

Scripted (non-physics) motion:

- **Put back:** if Sandwich A is released with its centre outside Sensor A, A is made static, moved along an arc (peak 40 units above Box A's rim) to (138, 335) over the `motion.spatial` spring (target ≤ 400 ms), then made dynamic with zero velocity. Reason: a spring alone would drag A into walls; the rule is not a force, so a firm scripted move is the honest picture. `Body.setStatic(body, true)` saves restitution, friction, mass, inertia, density and their inverses in `part._original`; `setStatic(body, false)` restores each and clears `_original` (0.20.0 `Body.js`). So A gets back its mass and its infinite inertia (no rotation). The static phase also zeroes A's speed and angular speed.
- **Look inside:** Sensor B outline turns solid `color.branch.kotlin` for 300 ms.

State rules:

- A box **holds a sandwich** when a sandwich's centre is inside that box's sensor and the sandwich has been settled (speed under 0.3 units per update) for 10 updates, or is sleeping.
- Box A always reports "Holds a sandwich" (the rule keeps it; during a drag, the chip stays and the spring is the visible reason).
- Sandwich B resting in Sensor "on top of A" means "Box A is full": message, no state change.

Config object `NullLunchBoxConfig` (names and defaults): `worldWidth 720`, `worldHeight 405`, `gravityY 1`, `gravityScale 0.001`, `deltaMs 1000/60`, `maxSpeed 25`, `sandwichWidth 96`, `sandwichHeight 44`, `sandwichChamfer 6`, `sandwichDensity 0.002`, `sandwichFriction 0.6`, `sandwichFrictionStatic 0.8`, `sandwichFrictionAir 0.03`, `restitution 0`, `boxInnerWidth 140`, `boxInnerHeight 50`, `boxWall 8`, `alwaysRuleStiffness 0.02`, `alwaysRuleDamping 0.1`, `putBackArcHeight 40`, `settleSpeed 0.3`, `settleUpdates 10`, `countWaitMaxMs 1000`, `lookHighlightMs 300`, `forcedCountNoticeMs 300`, `respawnDelayMs 500`, `showLookInsideFirst true`.

### 10.4 Controls

Standard controls (§7.1): all present, no change. "Show the rules" draws the three sensors and the Always-rule spring with DOM labels "checks: is a sandwich inside?" and "rule: this box can never be empty".

| Control | Pointer | Keyboard | What it reveals |
|---|---|---|---|
| Sandwich B | drag | proxy button "Sandwich B", then drop places | A "might be empty" box can be filled and emptied, and empty is allowed |
| Sandwich A | drag | proxy button "Sandwich A", then drop places | An "always" box refuses to stay empty |
| Count the slices (on Box A) | button under Box A | Tab, Enter | An "always" box can be used without looking: always "2 slices" |
| Count the slices (on Box B) | button under Box B | Tab, Enter | Works when full; stops the app when empty and nobody looked |
| Look inside first (checkbox, applies to Box B's count) | click | Tab, Space | Looking first turns the crash into a safe "nothing here" answer |

Drop places (shared by pointer and keyboard; pointer drops anywhere, keyboard snaps to these): "tray", "table, between the boxes" (360, 345), "box: might be empty", "box: always has a sandwich". Keyboard drop on a place = the body appears 30 units above the place's centre and falls (reduced motion: appears settled).

Tab order: Play/Pause → Step → Reset → Speed → Show the rules → Sandwich A → Sandwich B → Count (Box A) → Count (Box B) → Look inside first → Text description.

Labels (DOM, `type.label`) on each box front: Box A "always has a sandwich" in a chip with solid border and a lock glyph; Box B "might be empty" in a chip with dashed border and a "?" glyph (shape cue, not colour only). State chip above each box: "Holds a sandwich" (filled-circle glyph) or "Empty" (empty-circle glyph).

### 10.5 Script

| Step | Initial state or learner action | What moves | Live-region sentence | What the learner should notice |
|---|---|---|---|---|
| 0 | Initial: A in Box A, B on the tray, Box B empty; chips "Holds a sandwich" / "Empty"; Look inside first off | nothing (all bodies asleep) | "Two lunch boxes. The box labelled always has a sandwich holds sandwich A. The box labelled might be empty is empty. Sandwich B is on the tray." | Two boxes, two different labels |
| 1 (Try this 1) | Drag B into Box B, then drag it out to the table | B falls in and settles; chip → "Holds a sandwich"; then B is lifted out; chip → "Empty" | "Sandwich B is in the box labelled might be empty." then "The box labelled might be empty is empty again. That is allowed." | Empty is a normal state for this box. Nothing breaks |
| 2 (Try this 2) | With Box B empty and Look inside first off, press Count the slices on Box B | Notice "Label says might be empty. Counting without looking…" for 300 ms; then the runner stops, every body is redrawn with `color.outline` fill, and an overlay appears (`color.feedback.wrong.tint`, error icon, `color.feedback.wrong` text): "The app stopped. It tried to count the slices in an empty box." Only Reset and Text description stay active | "The app stopped. It tried to count the slices in an empty box. Press Reset to start again." | The answer is not "0 slices". Using nothing as something stops everything, not just one box |
| 3 | Press Reset | world returns to step 0 | "Back to the start." | Same start every time |
| 4 (Try this 3) | Drag A out of Box A and let go anywhere outside it | during the drag A lags behind the pointer (spring resistance); on release A is put back along an arc and settles; Box A's chip stays "Holds a sandwich" throughout | "The box labelled always has a sandwich will not stay empty. Sandwich A went back." | This label is a promise, and the rules keep it |
| 5 (extra, see Q1) | Tick Look inside first; with Box B empty, press Count on Box B | Sensor B outline lights for 300 ms; chip above Box B: "Nothing here, so nothing to count." (`color.feedback.hint.tint`, info icon); the world keeps running | "You looked first. The box is empty, so there is nothing to count. The app keeps running." | Looking first is the safe path |
| 6 | Put B in Box B; press Count on Box B; press Count on Box A | chip "2 slices" (`color.feedback.correct.tint`, check icon) above each box | "Sandwich B has 2 slices." / "Sandwich A has 2 slices." | A full box can be used. An "always" box needs no look |
| 7 | Drop B onto Box A | B rests on top of A, sticking out above the rim | "This box already holds a sandwich. It holds one at a time." | A box holds one sandwich |

### 10.6 Edge cases, performance, accessibility

| Case | What the sim does |
|---|---|
| B thrown or pushed off the visible area (centre outside x −40…760 or y > 445) | After 500 ms B reappears on the tray. "Sandwich B is back on the tray." |
| B rests across Box B's rim | State follows B's centre: inside the sensor = "Holds a sandwich", else "Empty". |
| Count pressed while a sandwich is still moving | Waits until every sandwich is settled (max 1 s), then evaluates; during the wait the button shows a small progress glyph. |
| Count pressed again within 300 ms, or during the look highlight | Ignored. |
| Count pressed while a body is being dragged or picked up by keyboard | Both Count buttons are disabled during any drag; tooltip "Let go of the sandwich first." |
| A dragged into Box B and released | Put back to Box A (same as step 4). |
| Two-finger drag of A and B together | One drag at a time. Matter's `Mouse` does **not** ignore the second finger by itself (it reads only `changedTouches[0]`, so the drag point would jump); the host's first-touch filter (§7.3, V6) drops it. ⚠️ VERIFY (unknown: whether the filter also keeps `touchend` of the second finger from ending the first drag). Phase 5 test on a touch screen or Chrome DevTools touch emulation: hold A with one finger, tap with a second finger, A must not jump and must stay held. |
| Pointer released outside the canvas during a drag | `Mouse` listens for `mouseup` on the canvas only, so the release is missed. The host listens for `pointerup` on `window` while a drag is active and ends the drag the same way (Matter sets `mouse.button = -1` on its own `mouseup`; source). Phase 5 test: drag B off the frame, release, B must fall. |
| Drag or keyboard pick-up during the crash overlay | Disabled. |
| Reset during the put-back arc or a fall | Cancels the motion and restores step 0. |
| Frame resized during a drag | Drag ends; the body falls from where it is. |
| Tab hidden mid-fall | Paused; resumes from the same state. |
| Engine fails to load | Text equivalent plus the §3.1 failure line. |

Performance: 2 dynamic bodies (budget 40), 1 constraint plus the drag constraint (budget 20), 3 sensors (budget 10), 14 static parts including world walls. Lowered budgets for this sim: physics step ≤ 1 ms p95; idle within 2 s of the last action (all bodies asleep, 0 frames).

Reduced motion: no autoplay; drops appear settled with a 150 ms fade; the put-back is a jump with a 150 ms fade; look-inside highlight is a steady outline for 1 s; crash overlay fades in over 150 ms with no other motion. Dragging still follows the pointer (learner-driven).

Text equivalent:

1. There are two lunch boxes. One is labelled "always has a sandwich" and holds sandwich A. The other is labelled "might be empty" and is empty. Sandwich B is on a tray.
2. You can put sandwich B into the "might be empty" box and take it out again. The box may stay empty. Nothing breaks.
3. If the "might be empty" box is empty and you count its slices without looking, the whole app stops. The answer is not zero slices.
4. If you tick "Look inside first", the same count says "Nothing here, so nothing to count" and the app keeps running.
5. If you try to take sandwich A out of the "always has a sandwich" box, it is put back. That box can never be empty.
6. What you should notice: a "might be empty" box may hold nothing; an "always" box never does; using nothing as if it were something stops the app.

### 10.7 Acceptance criteria

- [ ] Steps 0–7 of §10.5 can be done with a mouse and give exactly the chips, messages and live-region sentences listed.
- [ ] The same steps can be done with the keyboard only, in the tab order of §10.4, with a visible focus ring at every stop.
- [ ] Counting slices on the empty Box B with Look inside first off never shows "0 slices"; it shows the stopped-app overlay, and only Reset and Text description respond.
- [ ] With Look inside first on, the same action shows "Nothing here, so nothing to count." and the world keeps running.
- [ ] Box A's chip never reads "Empty", during or after any drag.
- [ ] No body bounces visibly (restitution 0) and sandwiches never rotate.
- [ ] Repeating steps 1–4 after Reset gives the same states every time.
- [ ] With `prefers-reduced-motion: reduce`, nothing moves until the learner acts, and every result appears with a fade of 150 ms or less.
- [ ] No hex colour, font name or pixel spacing literal in the sim's Kotlin files; all from `docs/02` tokens.
- [ ] No Kotlin syntax is visible anywhere in the sim.
- [ ] Network panel: Matter.js loads only after scrolling near the sim; the lesson text is complete with JavaScript sims blocked.
- [ ] Performance panel, 10 s of dragging: frame time ≤ 16.7 ms p95, physics step ≤ 1 ms p95; 0 frames requested 2 s after the last action.
- [ ] Screen reader reads each live-region sentence once per action. Default setup: NVDA (free, fits L27) with Chrome on Windows 11. ⚠️ VERIFY (unknown: whether the author tests with NVDA or another reader; this cannot be checked on the web). Test: the author confirms the reader before the first sim review; `reviewer` records it in the review report.

---

## 11. Acceptance for this doc's system, conflicts and open items

### 11.1 System acceptance

- [ ] Every live leaf with a non-empty `sims` list has, for each ID, a `docs/sims/<id>.md` with status `reviewed` and a `SimRegistry` entry.
- [ ] Every spec uses the §8 template with no section deleted, and passes the §5.2 honesty check.
- [ ] No sim imports Matter.js through `npm()`; the only Matter.js names in the codebase are in `engine/MatterExternals.kt`.
- [ ] No sim uses `Matter.Render`, pixelated drawing, world tokens, or colour literals.
- [ ] No sim, caption or label calls itself an Android emulator or simulator; every `code-to-screen` sim shows the caption "A picture of what Android does. Not a real phone." and lists its sources.
- [ ] The budgets of §6.2 and §6.3 hold on the reference laptop.

### 11.2 Conflicts with other docs (this doc does not edit them)

C1–C5 were all closed on 18 Oct 2026 by the owning docs (`docs/04`, `docs/07` §6.2 and §8, `docs/08`). No conflict is open.

Known follow-up outside this doc: the `profiling` roadmap still has `sim: code-to-screen` cells that §4.5 no longer allows (`content/profiling/_roadmap.md`); `curriculum-designer` changes them to `sim: animation`.

No contradiction with `docs/00` was found. No default (D1–D17) is changed.

### 11.3 Open questions

| # | Question | For |
|---|---|---|
| Q1 | ~~`null-lunch-box` adds "Look inside first" (step 5), not in the `docs/04` §10 Try-this list.~~ **Resolved 18 Oct 2026:** author keeps it; `docs/04` §10 now has it as Try-this item 4. | closed |
| Q2 | Matter.js determinism on one machine: confirm with the replay test in §2.1 (Phase 5). The "no CCD" statement is settled (issue #5 open, §2.2). | `sim-engineer` |
| Q3 | Pin the Three.js version (candidate `0.186.1`, about 185 KB gzip as one bundle) and measure the combined gzip size of the minified `three.core.js` + `three.module.js` before the first `3d` spec (§6.3). | `architect` |
| Q4 | ~~Kobweb's emitted CSS custom-property names.~~ **Resolved 18 Oct 2026:** token name with `.` to `-` and `--` in front; sims build the string from the Kotlin variable's `name` (used in §3.3); `docs/02` §3.1 (its V4). Only the digit-name case and whether `StyleVariable.name` is public stay ⚠️ VERIFY, with a one-command Phase 0 test in `docs/02` §3.1. | closed |
| Q5 | ~~Narrowest `SimFrame` width.~~ **Resolved 18 Oct 2026:** 720 CSS px at every supported viewport (1024 px and up); `docs/02` §4.5 (its V5), used in §6.1 and §7.3. Phase 0 only measures it. | closed |

---

## 12. Sources (checked 18 Oct 2026)

- Matter.js repository and licence (MIT, 2D rigid bodies): https://github.com/liabru/matter-js
- Matter.js 0.20.0 API, Engine (defaults, `Engine.update`, collision events, `Engine.clear`): https://brm.io/matter-js/docs/classes/Engine.html
- Matter.js Runner (fixed `delta` 1000/60, `maxFrameTime`, `enabled`): https://brm.io/matter-js/docs/classes/Runner.html
- Matter.js Body (defaults, `setStatic`, `setPosition`, `setVelocity`, `applyForce`, combining rule): https://brm.io/matter-js/docs/classes/Body.html
- Matter.js Constraint (stiffness, damping, length): https://brm.io/matter-js/docs/classes/Constraint.html
- Matter.js MouseConstraint (drag, `startdrag`, `enddrag`): https://brm.io/matter-js/docs/classes/MouseConstraint.html
- Matter.js size, first estimate (81.2 KB min, 25.3 KB gzip; superseded by the bundlephobia figure below): https://depscope.dev/pkg/npm/matter-js ; version history: https://snyk.io/advisor/npm-package/matter-js
- Rapier JS (compat build embeds WASM as base64; deterministic build): https://unpkg.com/@dimforge/rapier2d@0.17.3/README.md , https://cdn.jsdelivr.net/npm/@dimforge/rapier3d-deterministic@0.19.3/README.md
- Planck.js 1.5.0: https://cdn.jsdelivr.net/npm/planck@1.5.0/README.md
- Kotlin/JS modules, `@JsModule`, `@JsNonModule`, plain module kind: https://kotlinlang.org/docs/js-modules.html
- Kotlin Gradle `npm()` dependency: https://kotlinlang.org/api/kotlin-gradle-plugin/kotlin-gradle-plugin-api/org.jetbrains.kotlin.gradle.plugin/-kotlin-dependency-handler/npm.html
- Kotlin/JS chunk splitting proof of concept (experimental): https://github.com/cybernetics/kjs-chunks
- Three.js historic build sizes: https://minime.stephan-brumme.com/threejs/145
- Three.js 0.186.1 build folder (`three.core.js`, `three.module.js`, no `.min.js`): https://app.unpkg.com/three@0.186.1/files/build , https://cdn.jsdelivr.net/npm/three@0.186.1/build/three.module.js (imports `./three.core.js`)
- Three.js split into `three.core.js` since r171: https://discourse.threejs.org/t/three-js-now-imports-three-core-js/75187

Added in the verification pass (all read 18 Oct 2026):

- Matter.js npm registry (only `latest` = 0.20.0, published 2024-06-23, MIT): https://registry.npmjs.org/matter-js
- Matter.js CHANGELOG (0.20.0 fixed-timestep Runner; no "deterministic"): https://raw.githubusercontent.com/liabru/matter-js/0.20.0/CHANGELOG.md
- Matter.js 0.20.0 `build/` file list (matter.min.js 83.5 kB): https://app.unpkg.com/matter-js@0.20.0/files/build ; UMD banner and global: https://cdn.jsdelivr.net/npm/matter-js@0.20.0/build/matter.min.js
- Matter.js size 83,161 B min, 25,859 B gzip: https://bundlephobia.com/api/size?package=matter-js@0.20.0
- Matter.js no CCD, issue #5 (open): https://github.com/liabru/matter-js/issues/5
- Matter.js source at 0.20.0 (Runner loop and `enabled`; `Body.setStatic` `_original`; `inertia` option; `Bodies.rectangle` chamfer; `Mouse` touch, wheel, pixel ratio, scale; `MouseConstraint` on `beforeUpdate`): https://raw.githubusercontent.com/liabru/matter-js/0.20.0/src/core/Runner.js , .../src/body/Body.js , .../src/factory/Bodies.js , .../src/core/Mouse.js , .../src/constraint/MouseConstraint.js
- Matter.js Engine docs (`timing.timeScale`): https://brm.io/matter-js/docs/classes/Engine.html ; Runner docs (`maxUpdates`, `Runner.stop`): https://brm.io/matter-js/docs/classes/Runner.html
- Rapier 2D 0.21.0 (wasm 2.4 MB, Apache-2.0): https://app.unpkg.com/@dimforge/rapier2d@0.21.0/files , https://registry.npmjs.org/@dimforge/rapier2d-compat/latest
- Planck.js 1.5.1 (46,711 B gzip): https://bundlephobia.com/api/size?package=planck
- Three.js 0.186.1 npm metadata (MIT, `module`, `exports`): https://registry.npmjs.org/three/latest ; build files: https://app.unpkg.com/three@0.186.1/files/build ; size 184,898 B gzip: https://bundlephobia.com/api/size?package=three@0.186.1
- Three.js WebGL 2 check: https://threejs.org/docs/pages/WebGL.html ; WebGL 1 removed in r163: https://github.com/mrdoob/three.js/releases/tag/r163 , https://github.com/mrdoob/three.js/wiki/Migration-Guide
- Kotlin `js()`, `external` globals, `js("{}")` option objects: https://kotlinlang.org/docs/js-interop.html
- Kotlin module kinds and `@JsModule`: https://kotlinlang.org/docs/js-modules.html ; output granularity: https://kotlinlang.org/docs/js-project-setup.html
- Kotlin null safety (NPE causes): https://kotlinlang.org/docs/null-safety.html
- Compose HTML `Canvas` and `ref`/`onDispose`: https://github.com/JetBrains/compose-multiplatform/tree/master/html/core/src/jsMain/kotlin/org/jetbrains/compose/web
- Kobweb docs index (no per-page script-loading page): https://kobweb.varabyte.com/docs
- webpack `import()` and `webpackIgnore`: https://webpack.js.org/api/module-methods/
- Reading CSS custom properties from JavaScript: https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_custom_properties
- Reduced motion in JavaScript: https://web.dev/articles/prefers-reduced-motion ; IntersectionObserver `rootMargin` and `threshold`: https://web.dev/articles/intersectionobserver
- WCAG 2.2 target size: https://www.w3.org/WAI/WCAG22/Understanding/target-size-enhanced.html (44 × 44, AAA), https://www.w3.org/WAI/WCAG22/Understanding/target-size-minimum.html (24 × 24, AA)
- NVDA (free, Windows 10/11): https://www.nvaccess.org/about-nvda/
