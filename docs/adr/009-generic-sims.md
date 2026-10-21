# ADR 009 — Generic animated sim templates driven by a ```sim block in the leaf

Status: accepted · Date: 21 Oct 2026 · Decided by: the author (hybrid: generic templates first, bespoke physics later; every leaf from layer 3 on ships a working sim), specified by `architect` · Decision row: **L37** in `docs/00`.
Amends: `docs/04` §3.4 and §4.3 (marker form, Try this), `docs/05` §1 S8 and S9, §3.2 "Marker to widget", §6.1 stage 2, §7.1 (for template sims only), ADR 005 §1 (`sim` block fields), `docs/07` §8 "Sim Kotlin code" row. Owners align their docs (WP8).
Paths: K = `site/src/jsMain/kotlin/dev/kotree/site/`, T = `site/src/jsTest/kotlin/dev/kotree/site/`, B = `buildSrc/src/main/kotlin/dev/kotree/build/`, BT = `buildSrc/src/test/kotlin/dev/kotree/build/`.

## Context

- Bug (author, 21 Oct 2026): every "See it move" section shows only the Try this text. No sim was ever built: `simRegistry` in `K/ui/lesson/LessonView.kt` is `emptyMap()`, so `SimHost` draws nothing on a live page. 67 leaves carry a `<!-- SIM: id -->` marker (`docs/sims/needed-ids.md`) and the 17 roadmaps hold 284 `sim:` cells.
- One bespoke Matter.js sim per id (docs/05 S8: a spec, then a sim) cannot keep pace with layers of about 15 leaves.
- Facts used: the build already parses YAML fences with SnakeYAML (`challenge` blocks, `B/LessonRenderer.kt`), writes one lesson JSON per live leaf with `{ "t": "sim", "id" }` blocks (ADR 005), and the static body skips `sim` blocks (`LessonRenderer.kt` ~line 338). `SimFrame` has Play/Pause and Reset only. `Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()` (`K/motion/Spring.kt`, returns `Pair<Double, String>` = seconds and a CSS `linear()` easing; used by `Card.kt` and `Button.kt`) turns M3E springs into CSS (`docs/02` §4.4). `aria-current="step"` marks the current step in a process ([MDN ariaCurrent](https://developer.mozilla.org/en-US/docs/Web/API/Element/ariaCurrent)). `Element.setPointerCapture()` keeps pointer events on the dragged element until `pointerup`/`pointercancel`, Baseline since July 2020 ([MDN](https://developer.mozilla.org/docs/Web/API/Element/setPointerCapture)). `prefers-reduced-motion` via `matchMedia` plus a `change` listener (`docs/05` §7.2, web.dev).

## Decision

### 1. How a leaf names its template and data: a ```` ```sim ```` fence in the leaf

The bare marker is replaced by one fenced YAML block directly under `## See it move`. The fence **is** the sim block: it carries `id`, `template`, the scene data and the Try this steps. "**What you should notice:**" stays in prose under the fence.

Why the fence (chosen) and not `docs/sims/<id>.sim.json`:

| | ```` ```sim ```` fence (chosen) | `docs/sims/<id>.sim.json` (rejected) |
|---|---|---|
| Source of truth | One file per leaf; Try this text lives once, next to the data that drives it | Try this text in the leaf, data in a second file: two places to drift |
| Authoring | YAML, comments allowed, same rules and parser as `challenge` blocks | Hand-written JSON: quoting, no comments, a second loader in the build |
| Review | Reviewer sees prose and sim data in one diff | Easy to miss the second file |
| Errors | `file:line [id] SMn message`, like CH rules; preview shows them | New path-to-leaf error mapping |
| Reuse | Ids are per leaf in practice (all 67 rows are distinct); a later leaf copies the block | Its only advantage, not needed |
| Bundle | Data goes into the lesson JSON: **0 B in `kotree.js` per leaf** (ADR 005) | Same, only if embedded the same way |

Also rejected: per-leaf Kotlin config objects (bytes per leaf in the one bundle, the problem ADR 005 solved); keeping the marker plus a fence below it (the id written twice, a pairing rule).

Build (WP1): `renderLesson` turns a ```` ```sim ```` fence into `SimLessonBlock(id, tpl, spec, try)`. JSON (schema stays 1; fields are additive, so old readers ignore them):

```json
{ "t": "sim", "id": "snapshot-tray", "tpl": "zones",
  "spec": { "...": "the validated YAML, keys normalised, see section 2" },
  "try": [ { "html": "Edit file A. ...", "done": ["edit-a"], "optional": false } ] }
```

- `try[].html` is the step text rendered by `renderMarkdown` (escapeHtml on) with the outer `<p>` removed; the widget sets it once with `innerHTML`, like `Prose`.
- The static body (`lessons-static/<id>.html`) writes `<p><strong>Try this:</strong></p><ol>…</ol>` for each sim block, so no-JS readers and the first paint keep the steps.
- A legacy bare marker still parses to `{ "t": "sim", "id" }` (no `tpl`). Rule SM0 reports it as a warning until WP7 has converted all 67 leaves, then as an error.
- Frontmatter does not change: `sims:` lists the fence ids (new rule SM1: the two sets are equal).

Client resolution order in `SimHost(block)`: (1) a bespoke sim registered for `id` in `simRegistry` (later physics sims win); (2) the template for `tpl`; (3) the try list read-only (template not built yet) on a live page, plus a labelled frame in a preview.

### 2. One engine, seven templates

All templates share one pure-Kotlin core. **State = fold(initial, history)**: the history is a list of events; Back removes the last event and replays; Reset clears the history; a toggle or slider change replays the history with the new value (the `docs/05` §4.3 "change the code, see the change" rule). No physics, no `requestAnimationFrame`; motion is CSS transitions on `transform` and `opacity` between two states. Kind in `docs/05` terms: `animation` (or `code-to-screen` when `phone` is present).

**Common envelope** (keys in this order; limits enforced by WP1):

```yaml
id: kebab-id                 # required; = needed-ids.md row; unique in the leaf
template: zones              # stepper | zones | lanes | flow | chart | states | compare
title: Working folder, tray and snapshot shelf   # accessible name, <= 60 chars
intro: Files A and B sit in the working folder.   # first live-region sentence and text-description line 1
caption: A picture of what Android does. Not a real phone.   # required, exact text, when `phone` is present
# ...template keys (table below)...
phone: { rows: [ { id: r1, label: "Counter: 0" } ] }    # optional add-on, <= 6 rows; places phone.<row>
timeline: [ { id: draw1, label: Draw } ]                 # optional add-on, <= 10 chips; lit chip = current
vars: { snaps: 0 }           # optional; strings or numbers
show: [ { var: snaps, label: Snapshots } ]               # optional readouts, <= 4
actions:                     # <= 6 buttons
  - id: snap
    label: Take snapshot
    cases:                   # first case whose `when` holds and `unless` fails runs; `do:` = one case, no condition
      - when: { at: { a: tray } }
        do: [ ... effects ... ]
    refuse: Nothing is in the tray, so no snapshot is taken.   # said when no case runs (default "Nothing changes.")
drops:                       # zones and lanes only; makes an item draggable; event id "<item>><place>"
  - { item: a, to: tray, when: { mark: { a: changed } }, do: [ ... ], refuse: File A has no change to stage. }
toggles: [ { id: wipe, label: Wipe the display before each scan, var: wipe } ]   # <= 2; event "wipe=on|off"
sliders: [ { id: n, label: Houses, var: n, min: 10, max: 1000, step: 10 } ]      # <= 3; event "n=<v>"
script: [ edit-a, "a>tray", snap, edit-b, snap ]         # what Step and Play fire, in order, <= 20 events
try:                         # 1 to 3 steps, plus at most 1 with optional: true (docs/04 §3.4)
  - text: Edit file A. It is marked "changed" in the working folder.
    done: [ edit-a ]         # events that must happen, in order, after the previous step is done
```

**Conditions** (closed set): `at: {item: place}`, `mark: {id: mark}`, `var: {name: value}`; all listed must hold. **Effects** (closed set of 12; every case has exactly one `say`): `say: text` · `move: {item: place}` · `spawn: {id, label, at}` · `remove: item` · `mark: {id: mark}` · `label: {id: text}` · `set: {var: value}` · `add: {var: number}` · `light: [ids]` (replaces the lit set) · `row: [cells]` (stepper log) · `push: {lane: item}` · `pop: lane`. Strings may use `{var}`. **Marks** (closed): `plain`, `changed` (dashed outline + "changed" badge), `good` (check icon), `bad` (cross icon), `ghost` (dashed, 40 % opacity: a place that is not there yet), `picked` (raised outline). Colour is never the only cue (`docs/05` §3.3).

**Templates** (auto-layout inside the 720 × 405 stage; data never holds coordinates, so authors cannot break the layout):

| Template | Use for | Keys and limits | Places (ids effects can target) |
|---|---|---|---|
| `stepper` | order of execution, traces, timelines of steps, call and return | `lines: [{id, text}]` ≤ 12; `code: true` = mono font; `groups: [{id, label, lines}]` ≤ 3 (rooms, cards; nested by order); `board: [{var, label}]` ≤ 8 cells; `log: {columns}` ≤ 5 columns, ≤ 8 rows | lines (lit line = the pointer), groups, board cells |
| `zones` | things placed in containers, references, copies, staging | `zones: [{id, label}]` 2–4; `items: [{id, label, at}]` ≤ 12; an item named in `drops` is draggable | zones, items |
| `lanes` | indexed rows, arrays, queues, stacks, search, switches | `lanes: [{id, label, kind: row/queue/stack, slots: ≤ 16, index0: true}]` ≤ 3; `items: [{id, label, at: "<lane>.<i>"}]` | `<lane>.<i>` slots, items; `push`/`pop` follow `kind` |
| `flow` | pipelines, build lines, packets, routes, forks, chutes | `stations: [{id, label}]` ≤ 10; `paths: [[station ids]]` ≤ 3 sharing the first station; `items` ≤ 8 | stations, items; a `move` to a non-adjacent station hops along the path |
| `chart` | growth, sizes, measurements, number lines | `axes: {x, y}`; `bars: [{id, label, value}]` ≤ 6 (number or `{var}`); `curves: [{id, label, f}]` ≤ 3 with `f` one of `poly: [c0, c1, c2]`, `log2`, `nlog2`, `pow2` (no expression evaluation); `bands: [{id, label, kind, f, k}]` ≤ 2, the line `k · f(x)`, `kind` required: `floor` (the first curve must stay at or above it) or `ceiling` (at or below); equal counts as inside; direction never comes from the id or label; `marks: {start, stop, step, inclusive, down}`; `scale: {x: [min, max], y: auto}` | bars, curves; published vars `<curve>.at` (value at `x` var) and `<band>.exit` (first integer x ≥ `n0` var where the first curve is outside the band by its `kind`, or `none`) |
| `states` | modes and transitions, lifecycles, signals, loops with a gate | `nodes: [{id, label}]` ≤ 6; `edges: [{from, to, label}]` ≤ 10; `start`; `layout: ring/row` | nodes, edges as `from>to`, a token item |
| `compare` | two ways of doing the same thing, before and after | `sides:` exactly 2, each `{id, label, boxes: [{id, label}] ≤ 6}` | `<side>.<box>` |

Writers pick the template from the "Use for" column. Bespoke physics sims (later, `docs/05` unchanged for them) replace a template sim by registering the same id; candidates are marked ★ in §5.

**Worked example** (`snapshot-tray`, `tools/git/what-is-a-commit`; WP7 writes the full block):

```sim
id: snapshot-tray
template: zones
title: Working folder, tray and snapshot shelf
intro: Files A and B sit in the working folder. The tray and the shelf are empty.
zones:
  - { id: folder, label: Working folder }
  - { id: tray, label: "Tray (the frame)" }
  - { id: shelf, label: Snapshot shelf }
items:
  - { id: a, label: File A, at: folder }
  - { id: b, label: File B, at: folder }
vars: { snaps: 0 }
show: [ { var: snaps, label: Snapshots } ]
actions:
  - id: edit-a
    label: Edit file A
    do: [ mark: { a: changed }, say: File A is changed. It is still in the working folder. ]
  - id: edit-b
    label: Edit file B
    do: [ mark: { b: changed }, say: File B is changed. It is still in the working folder. ]
  - id: snap
    label: Take snapshot
    cases:
      - when: { at: { a: tray } }
        do:
          - add: { snaps: 1 }
          - spawn: { id: "s{snaps}", label: "Snapshot {snaps}: new A, B carried over", at: shelf }
          - move: { a: folder }
          - mark: { a: plain }
          - say: Snapshot {snaps} holds the new A. B is carried over unchanged.
    refuse: Nothing is in the tray, so no snapshot is taken. Changed files stay in the folder.
drops:
  - { item: a, to: tray, when: { mark: { a: changed } }, do: [ say: File A is in the tray. ], refuse: File A has no change to stage. }
  - { item: b, to: tray, when: { mark: { b: changed } }, do: [ say: File B is in the tray. ], refuse: File B has no change to stage. }
script: [ edit-a, "a>tray", snap, edit-b, snap ]
try:
  - { text: Edit file A. It is marked "changed" in the working folder., done: [ edit-a ] }
  - { text: Drag file A into the tray. Then press "Take snapshot"., done: [ "a>tray", snap ] }
  - { text: Edit file B and leave it out of the tray. Press "Take snapshot" again., done: [ edit-b, snap ] }
```

### 3. Try this is driven by the widget

- The widget renders the `try` list under the controls inside `SimFrame` (the frame grows; height is fixed from the data at first render, so no layout shift).
- Progress is derived from the history (pure function, `TryProgress`): step *i* is done when its `done` events occur in order after step *i − 1* is done. The current step is the first step not done.
- Current step: `aria-current="step"`, a 4 px branch-ink left bar, bold text and a "Now" text badge. Done steps: check icon plus visually hidden "done". The control that fires the next expected event gets a dashed `color.feedback.hint` outline (shape cue, not colour only).
- Back, Reset, toggles and sliders recompute progress from the history; nothing is stored.

### 4. Look, motion, CPU, accessibility, size

| Topic | Rule |
|---|---|
| Rendering | DOM, not canvas: ≤ 60 elements in the stage; text is real text; chips are buttons. Rejected: canvas (needs proxies and hit testing for every item); Matter.js (physics would add false behaviour to exact steps, `docs/05` §5.4). |
| Tokens | Colours, fonts, shapes, spacing only through the CSS variables of `Tokens.kt` (`docs/05` §3.3 allowed list). Moves use `Spring.cssLinear()` of `motion.spatial`; fades `motion.effects.fast`. One new constant: `Tokens.Motion.simStepMs = 1200` (Play interval; Slow doubles it). No literal in any sim file (`scripts/check-hardcoded.sh`, `check-tokens.py`). |
| Controls | `SimFrame` gains Back, Step, Speed (Normal/Slow) and "Text description" next to Play/Pause and Reset (`docs/05` §7.1; Show the rules does not apply). Template actions, toggles (`role="switch"`) and sliders (`<input type="range">` with a visible label and value) sit in one row under the stage. Keys inside the frame only: Space play, `.` step, `,` back, `R` reset, `S` speed. |
| Drag | Pointer Events with `setPointerCapture`; the drop place is the zone or slot whose `getBoundingClientRect()` holds the pointer. Keyboard: an item chip is a button; Enter picks it up and shows "Drop in <place>" buttons for every place; Esc cancels. Mouse and keyboard fire the same event id. Draggable chips ≥ 44 × 44 CSS px. |
| Play | One `window.setTimeout` at a time fires the next `script` event every `simStepMs`; stops at the end, on Pause, when the tab is hidden (`visibilitychange`), when the frame is under 10 % visible (`IntersectionObserver`), and on dispose. Play on one sim pauses any other on the page. **No autoplay** (amends `docs/05` §6.1 stage 2 for templates): the frame shows the `intro` state. |
| Idle CPU | Zero: no timer and no animation frame unless Play is running; transitions only on `transform`/`opacity`. Acceptance: Performance panel shows no task from sim code for 10 s while idle. |
| Reduced motion | Transitions off; each new state appears with a 150 ms opacity fade; Play still steps (instant cuts). Read with `matchMedia` plus a `change` listener. Nothing flashes more than 3 times per second. |
| Live region | One polite `aria-live` region per sim; each event announces its `say` (or `refuse`); Reset announces `intro`. |
| Text description | Generated: `intro`, then the `say` of every `script` event run through the core, then the try steps. Same text with the stage hidden. |
| Labels | Frame `role="group"` with `aria-label` = `title`; every button names its action; toggles and sliders have visible labels. |
| Bundle | `kotree.js` gzip growth: core ≤ 8 KB, host and controls ≤ 8 KB, each template layout ≤ 3 KB; **total ≤ 35 KB** (fail line 40 KB), measured per WP with `gzip -9 -c site/.kobweb/site/kotree.js | wc -c`. Replaces the "12 KB runtime + 6 KB per sim" row of `docs/07` §8 for template sims. Per leaf: 0 B in the bundle; the sim block in the lesson JSON ≤ 8 KB raw (SM rule). |
| Speed | A step's DOM update ≤ 4 ms; no long task over 50 ms; CLS ≤ 0.05 on the leaf. |

### 5. Mapping of every id in `docs/sims/needed-ids.md` (★ = bespoke physics candidate later)

| Template | Ids |
|---|---|
| `stepper` (10) | recipe-card-pointer, line-by-line-stepper, labelled-jars, recipe-card-call, trace-table-stepper, hotel-bell-startup, reserved-seat-timeline, lazy-coffee-machine, fire-alarm-rooms, net-and-sweeper |
| `zones` (14) | desk-and-cabinet ★, flat-pack-app-box, mug-and-whiteboard-tag, null-lunch-box ★ (`docs/05` §10 spec), house-and-street-names, recipe-card-handoff, garden-window-view, cookie-cutter-stamp, twins-or-one-person, two-keys-one-flat, snapshot-tray, shared-or-platform-bins, dex-packer, hotel-corridor-files (+ `phone`) |
| `lanes` (13) | binary-switches, building-manager, post-office-boxes, basket-checkout-loop, fuse-halving-search, letter-stack-gates, inn-room-clerk, app-house-doors, tests-lamps, main-thread-queue (+ `phone`), linear-search-shelf, blocked-main-thread-timeline (+ `phone`, `timeline`), branch-labels |
| `flow` (14) | input-process-output, fork-and-gate ★, bakery-build-line, parcel-to-phone, translator-box, bytecode-adapters, sorting-chutes ★, knock-and-note, factory-line-stations, mixed-tools-chute, request-parcel-pipe, launch-path-manifest (+ `phone`), postcard-routes, two-belts-one-source |
| `chart` (8) | numbered-stairs-loop, overflow-cups ★, tape-measure-range, measure-bars, step-counter-bars, drop-the-small-parts, ceiling-and-floor-band, hospital-monitor ★ |
| `states` (2) | loop-track-gate, three-lamp-signal |
| `compare` (6) | phone-kitchen-two-ways, translation-lanes, menu-board-cover, shop-window-restock, form-photocopier, declarative-vs-steps (+ `phone`) |

Total 67. The 284 roadmap `sim:` cells are not pre-mapped (rows change); the writer picks the template with the "Use for" column when writing the leaf and appends `— template: <name>` to the leaf's `needed-ids.md` row. `states` is small today but covers the lifecycle, TCP and process-state rows of later layers. Profiling leaves never use `phone` (`docs/05` S11).

### 6. Validation rules (WP1, `validateContent` and `renderLesson`)

SM0 bare marker (warning until WP7 ends, then error) · SM1 `sims` = fence ids · SM2 envelope keys known, required present, order free · SM3 `template` in the set · SM4 limits of §2 · SM5 every referenced item, place, var, action, event and station exists · SM6 effects and conditions in the closed sets; exactly one `say` per case · SM7 `try` 1–3 plus ≤ 1 optional; every `done` event exists and is in `script` or is a declared action, drop, toggle or slider event · SM8 `phone` ⇒ exact caption; no `phone` on a `profiling` leaf · SM9 one sim fence per leaf section, ≤ 2 per leaf · SM10 JSON of the block ≤ 8 KB · SM11 every chart band has `kind` and it is `floor` or `ceiling` (missing or other value: "band `<id>` needs `kind: floor` or `kind: ceiling`").

Amendment (21 Oct 2026, `architect`): band `kind` and SM11 added. Before this, the client guessed floor vs ceiling from the words "floor"/"lower" in the band id or label (rejected: a label such as "Lower is better" or a non-English label flips the maths silently). No live leaf had `bands`, so no content changes.

### 7. Checklist change (`docs/lesson-checklist.md`, applied by WP8 only after WP1 merges; before that a ```` ```sim ```` fence would render as a code block in the prose)

Item 8 becomes: "Frontmatter (sims, difficulty, order, requires, fruit) must match the roadmap row. If the leaf needs a sim, write one ```` ```sim ```` block (ADR 009) under `## See it move`, never a bare `<!-- SIM -->` marker; list its id in `sims` and append the id, leaf and template to `docs/sims/needed-ids.md`."
New item 14: "Sim data (ADR 009): the template fits the idea (§2 'Use for'); `try` holds the Try this steps (1–3, plus at most one optional) and each `done` is reachable; every `say` is one plain, true sentence in the leaf's analogy; Android pictures carry the exact caption. Reviewer opens `/preview/<id>`, does every Try step with the mouse, then with the keyboard only, then with reduced motion on; each step must light in turn and the live region must read each `say`. From layer 3 on, a leaf with a See it move section and no working sim is a FAIL."

## Work packages (`ui-engineer`, one per message, in order; content WPs go to `sim-engineer`)

Every WP: compiles with no `w:` warnings, `scripts/check-hardcoded.sh` and `scripts/check-tokens.py` clean, all tests green, `KitchenSink.kt` compiles unchanged, reports the `kotree.js` gzip delta. One file has one owner per WP.

| WP | Owner | Files | Test |
|---|---|---|---|
| WP1 Build | ui-engineer | new `B/SimSpec.kt` (parse YAML with SnakeYAML `SafeConstructor`, validate SM0–SM10, normalise, JSON writer); `B/LessonRenderer.kt` (```` ```sim ```` fence → `SimLessonBlock(id, tpl, spec, try)`, JSON fields, static try list); `B/Validate.kt` (SM0, SM1); fixtures under `buildSrc/src/test/resources/fixtures/` | `BT/SimSpecTest.kt`: the §2 example passes and its JSON is byte-stable; one failing fixture per SM rule with the expected `file:line [id] SMn`; a legacy marker gives `{t, id}` and SM0 warning; static body holds the `<ol>` |
| WP2 Core | ui-engineer | `K/lessons/LessonData.kt` (`BlockData.tpl`, `spec`, `try` as external fields); new `K/sims/core/SimModel.kt` (Kotlin data classes from the JSON), `SimCore.kt` (`fold(spec, history): SimState`, conditions, 12 effects, `{var}` interpolation, toggles and sliders replay), `TryProgress.kt`, `TextDescription.kt`, `ChartMath.kt` (`poly`, `log2`, `nlog2`, `pow2`, `<band>.exit`) | `T/sims/SimCoreTest.kt` (snapshot-tray: script gives 1 snapshot then a refuse; Back equals fold of history minus one; Reset equals intro state; toggle replays), `TryProgressTest.kt` (order, out-of-order events, optional step), `ChartMathTest.kt` (3n + 2 inside floor 3 and ceiling 4 from n0 = 2; exit for ceiling 2.9) |
| WP3 Host + `zones` | ui-engineer | `K/ui/lesson/SimFrame.kt` (Back, Step, Speed, Text description, controls slot, try slot; keys); new `K/sims/TemplateSimHost.kt` (state holder, playback timer, visibility, single runner per page, reduced motion, live region, try list, drag and keyboard pick-up, add-ons `phone`, `timeline`, `show`); new `K/sims/layouts/ZonesLayout.kt`; `K/ui/lesson/LessonView.kt` (`SimHost` resolution order §1); `K/Tokens.kt` (`Motion.simStepMs`, additions only) | `T/sims/PlaybackTest.kt` (pure `nextPlayback(state, event)`: stops at end, on hidden, on pause; no timer while idle); manual: `/preview/tools/git/what-is-a-commit` after WP7a, keyboard-only and reduced motion |
| WP4 `stepper` + `lanes` | ui-engineer | new `K/sims/layouts/StepperLayout.kt`, `LanesLayout.kt` | `T/sims/LanesTest.kt` (queue `pop` takes the front, stack the top; `push` into a full lane refuses); manual on two pilot leaves |
| WP5 `flow` + `states` | ui-engineer | new `K/sims/layouts/FlowLayout.kt`, `StatesLayout.kt` | `T/sims/FlowTest.kt` (a move to a non-adjacent station yields the hop list along its path; a fork picks the path that holds the target) |
| WP6 `chart` + `compare` | ui-engineer | new `K/sims/layouts/ChartLayout.kt`, `CompareLayout.kt` | `T/sims/ChartScaleTest.kt` (auto y-scale covers every visible series; marks progression with `inclusive` false and `down` true) |
| WP7a–g Backfill | sim-engineer (data), reviewer | the 67 leaves, about 10 per message, grouped by template; replaces each marker and its prose Try this list with the ```` ```sim ```` block (Try step wording kept unless it is wrong); `docs/sims/needed-ids.md` (template suffix). Last batch flips SM0 to error in `B/Validate.kt` (one line, ui-engineer) | `validateContent` green; reviewer does the §7 hand check per leaf |
| WP8 Docs | architect + housekeeper | `docs/04` §3.4, §4.3; `docs/05` §1, §3.2, §6.1, §7.1 (template kind); `docs/07` §8 row; `content/_templates/leaf.md` (fence instead of marker); `docs/lesson-checklist.md` (§7) | `rg "<!-- SIM:" content docs` returns nothing outside ADRs after WP7 |

Order: WP1 → WP2 → WP3 → WP7a (zones pilot, including `snapshot-tray`) → WP4 → WP5 → WP6 → rest of WP7 → WP8 checklist. Layer 3 leaves may go live only with a template whose WP has merged.

## Consequences

- Every live leaf gets a working, accessible animation with no new library and 0 B per leaf in the bundle; bespoke physics sims stay possible per id.
- Sim behaviour is data, so lesson-writer and sim-engineer own it; reviewer checks it like lesson facts. `docs/sims/<id>.md` specs are needed only for bespoke sims (amends `docs/05` S8).
- The closed effect set limits what a template can show; an idea that needs more is a bespoke sim, not a new effect, unless `architect` amends this ADR.
- Risk: authoring errors in YAML. Mitigation: SM rules with file and line, preview shows them.
- Risk: 67-leaf backfill cost. Mitigation: batches of 10 grouped by template, so each batch reuses one pattern.

## Open questions

1. `what-is-a-commit` Try step 3 says to take a second snapshot with nothing staged; Git then records no commit ("nothing added to commit"). ⚠️ VERIFY against git-scm.com in WP7a; the example above already shows the refusal. If confirmed, the leaf's step text needs a reviewer-approved fix (lesson content, not this ADR).
