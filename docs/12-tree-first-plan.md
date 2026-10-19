# 12 — Tree-first milestone (Phase 0 + Phase 2 merged)

> **Purpose:** the plan for the first visible milestone: a Kobweb site that shows the pixel-art tree (trunk, roots, 4 boughs, 16 branches) with hard-coded sample data, in at most 3 sessions.
> **Read this when:** starting the scaffold, or any task that touches `tree/`, `Tokens.kt` or the sample data before Phase 3.
> **Depends on:** `00` (L28 authorises this plan; wins conflicts), `02` §2.1, §2.3, §5, `03` §2, §4.2, `07` §2, §3, §7, §8, `10`, `11`.
> **Version:** v1 · 20 Oct 2026 · owner: `architect`.

Build order after this doc: **this milestone (Phase 0 + 2) → Phase 1 → Phase 3 → Phase 4 …** (`docs/10`). Phase numbers are unchanged because `03` and `11` cite them.

---

## 1. Goal

Open `http://localhost:8080/` and see one black pixel-art tree on a white page. Wheel-zoom, drag, and watch the four boughs split into branch limbs, with labels. Data is hard-coded; nothing is read from `/content`.

## 2. What is on screen

| Item | Shown | Source rule |
|---|---|---|
| Page | White (`color.surface`), no dark mode | L9 |
| Trunk, roots, 4 bough limbs | Black pixel bark, thicker toward the roots | `02` §5 rules 1-2 |
| 16 branch limbs + Foundation | Hidden inside the fused bough limb at `z ≤ 1.4`, fan out as `z` rises to 2.4, drawn in 9 quantised steps | `03` §2.4 (`p`, `p_drawn`) |
| Branch limb colour | Light gray `color.leaf.shadow` (zero live leaves: L4, L7). Trunk, roots and bough limbs stay black | Author's choice in this plan, see §10 Q1 |
| Labels (real `<button>` DOM nodes over the canvas) | At `z = 1`: exactly 5 labels (Kotlin and Android, Architecture and KMP, CS theory, Craft, Foundation). Branch titles fade in from `z = 1.8` (opacity `2p - 1`) | `03` §2.3, §2.4, §2.7 |
| Camera | Wheel zoom around the pointer, drag pan, `+` `-`, `Home` = fit tree. Click a bough or branch label = eased fly there. `z` range **0.8 to 4.0** in the prototype (Z0 to Z2 entry) | `03` §2.1, §2.5 |
| Reduced motion | Fly becomes an instant cut | `07` §7 |

Counts: the tree has Foundation plus 16 branches (`00` §5; `03` §1 calls this "17 branches"). After the split, 17 branch labels are visible (16 + Foundation, which is its own bough).

## 3. Deliberately skipped (each is owed by the phase shown)

| Skipped | Owed by |
|---|---|
| Stems, leaves, fruits, up-next glow, popups, shadow-stem hints | Phase 3 (data) and Phase 2 remainder, see `docs/10` |
| Lessons, sims, editor, Playground, Markdown pipeline, `/content` reading | Phases 3 to 6 |
| Final tokens: StyleVariable wiring, branch ink colours, Roboto Flex / JetBrains Mono, M3E components, `/kitchen-sink` | Phase 1 |
| Breadcrumb, minimap, "Start here" trail, full keyboard map (`Tab` order, arrows, `Esc`), double-click zoom, z above 4.0, Z3 and Z4 | Phase 2 remainder (listed in `docs/10`) |
| CI workflow, README, LICENSE | Phase 0b (`docs/10`) |
| Spring motion (P0-12), determinism unit test | Phase 1 / Phase 2 remainder |

Rule that keeps the swap cheap: **every value lives in `Tokens.kt`** (colours, sizes, seed, thresholds, durations, font fallback). No literal outside it (hard rule 2). Phase 1 then changes the values, or points them at `StyleVariable`s, in one place.

## 4. Checks that run first (session 1, in this order)

| Gate | Check | Command | Pass |
|---|---|---|---|
| G1 | JDK (`11` U3) | `java -version` | 17 to 27 |
| G2 | Kobweb pin (`11` U1) | open the Kobweb releases page | `0.25.1` still latest stable, else the newer pin goes into ADR 001 |
| G3 | Lesson Kotlin pin (`11` U2) | `curl -s https://api.kotlinlang.org/versions \| grep -c '"2.4.21"'` | prints 1 or more; else ask the author to re-pin |
| G4 | Build runs, white page | `kobweb run` in `/site` (`07` §3) | `http://localhost:8080/` is white and says "Kotree" |
| G5 | Export works | `kobweb export --notty --layout static` in `/site` (`07` §3) | exit code 0, `site/.kobweb/site` is non-empty |

Do not start session 2 until G1 to G5 are recorded (date + result) in the session-1 commit message.

Recorded during session 3, not gates: `11` P0-10 (first screen, frame times, DOM node count) and the baseline bundle size `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` (`07` §8). Per `11` P0-10, the targets do not become acceptance criteria until met.

**Deferred `11` rows (Phase 0b)** and the point where each must be done:

| Rows | Before |
|---|---|
| P0-5, P0-6 (token names), P0-8 (fonts), P0-11, P0-12 | Phase 1 |
| P0-3, P0-4, P0-7 (bundle spike), U4, U5 | Phase 3 (P0-7 at the latest before the pilot leaf) |
| P0-1, P0-2, P0-9 | Phase 4 / Phase 6 (any quiet moment) |

In `11` the "Phase" column still says 0 for these rows; read it as 0b.

## 5. Sessions (3)

| Session | Runs | Output |
|---|---|---|
| **S1 Foundation** | 1 `ui-engineer` (sequential), `housekeeper` in parallel | Gates G1-G5, scaffold per `07` §2.2, `Tokens.kt`, sample data, contract stubs (§7), `.gitignore`, ADR 001 file |
| **S2 Build** | 3 `ui-engineer` in parallel (A, B, C of §6) | Generator, renderer, camera + labels, each with a throwaway dev page |
| **S3 Join** | 1 `ui-engineer`, then `reviewer` | `TreeView.kt`, home page mount, dev pages deleted, acceptance (§8), P0-10 numbers |

Git: branch `phase-0-2-tree-first`; one conventional commit per run (`feat:`, `docs:`, `chore:`). The repo has no commits and `.idea/` and `.claude/` are untracked, so `.gitignore` is committed **before** the first `git add`.

## 6. File ownership

Root for Kotlin: `site/src/jsMain/kotlin/dev/kotree/site/` (written `K/` below; package `dev.kotree.site`, `07` §2.2).

| Owner | Files |
|---|---|
| **S1 ui-engineer** | repo-root Gradle files from the template (`07` §2.2 steps 1-4), `site/build.gradle.kts`, `site/.kobweb/conf.yaml`, `K/AppEntry.kt`, `K/pages/Index.kt` (white placeholder), `K/Tokens.kt`, `K/tree/model/TreeData.kt`, `K/tree/model/SampleTree.kt`, `K/tree/Contracts.kt` |
| **S1 housekeeper** | `.gitignore`, `docs/adr/001-kobweb-framework.md` (text from §9, verbatim) |
| **S2-A generator** | `K/tree/gen/*` (`Rng.kt`, `Layout.kt`, `Split.kt`, `GeometryHash.kt`), `K/pages/dev/GeneratorPreview.kt` |
| **S2-B renderer** | `K/tree/render/*` (`PixelCanvas.kt`, `LimbRaster.kt`, `Cache.kt`), `K/pages/dev/RenderPreview.kt` |
| **S2-C camera and labels** | `K/tree/camera/*` (`Camera.kt`, `Input.kt`, `Fly.kt`), `K/tree/overlay/*` (`Labels.kt`), `K/pages/dev/CameraPreview.kt` |
| **S3 ui-engineer** | `K/tree/TreeView.kt`, `K/pages/Index.kt` (mount), deletes `K/pages/dev/*`, may fix any file after a reviewer FIX |

Rules: in S2, `Tokens.kt`, `TreeData.kt`, `SampleTree.kt` and `Contracts.kt` are **read-only**. A run that needs a new token or contract field reports it to the main agent, who edits the file between runs. No run edits another run's folder. File names above are a plan; the owner may rename inside its own folder.

## 7. Data shapes

**Sample data (`SampleTree.kt`).** Same field names as `03` §4.2 (schema 1), so Phase 3 swaps only the source: `schema = 1`, `seed = 20261017`, `boughs[]` with `id`, `title`, `order`, `branches[]` with `id`, `title`, `order`, `accent` (token name string). `stems` is an empty list on every branch; no leaves, so no sample leaf can be mistaken for published content (hard rule 1). `counts` is `0 / 0` and is not displayed. Boughs and titles are exactly `03` §1 and `00` §5.

**Contracts (`Contracts.kt`, our own names, no framework API).**

| Type | Fields |
|---|---|
| `Vec` | `x`, `y` in world pixels |
| `Polyline` | `points: List<Vec>`, `widthPx: Int`, `ink: String` (a token name) |
| `BoughGeo` | `id`, `forkPoint: Vec`, `centre: Polyline`, `branches: List<BranchGeo>`, `labelAnchor: Vec` |
| `BranchGeo` | `id`, `full: Polyline` (at p = 1), `tip: Vec`, `labelAnchor: Vec` |
| `TreeGeometry` | `bounds`, `trunk`, `roots`, `boughs`, `fun limbsAt(boughId, step: Int /*0..8*/): List<Polyline>` |
| `CameraState` | `z`, `centre: Vec`, `viewportPx` |

`generate(data, seed): TreeGeometry` is pure and deterministic (D1). The renderer draws `TreeGeometry`; the camera and labels read anchors; nobody else reads the generator's internals.

**Token file (`Tokens.kt`)**, plain Kotlin constants named after `docs/02`: `color.surface`, `color.bark`, `color.on-surface`, `color.on-surface-muted`, `color.outline`, `color.leaf.shadow`, `color.leaf.shadow.label`; `type.label` (14 / 20, 600) and a system-font fallback (fonts come in Phase 1); `seed`; `world.px = 4`; world bounds; `zMin 0.8`, `zMax 4.0` (prototype), split start 1.4, split end 2.4, Bough enter 1.8 / leave 1.62; fly duration 500 ms (`03` §2.5 allows 600); hit area 24 px; label outline 3 px; spacing grid 4 px. Names for CSS use the `02` §3.1 rule so Phase 1 can turn them into variables.

## 8. Rendering and performance rules (from `07` §7, §8)

- One `<canvas>`, backing size = CSS size x `devicePixelRatio`, `imageSmoothingEnabled = false`, CSS `image-rendering: pixelated`. Labels are DOM `<button>`s on top (D2).
- Pre-render bitmaps: trunk and roots once; each bough at 9 split steps (36 bitmaps). The frame loop only blits, scaled by an integer factor. No re-rasterising while zooming. `requestAnimationFrame` runs only while the camera moves; idle = no frames.
- Draw only what is visible; DOM nodes are 5 bough + 17 branch labels, so no culling is needed yet.
- Targets to record (not gates yet): frame time p95 <= 16.7 ms, canvas draw <= 8 ms, first screen < 3 s, tree sprites 0 KB (procedural), home first load <= 1 MB.

## 9. ADR 001 text (copy verbatim into `docs/adr/001-kobweb-framework.md`)

```markdown
# ADR 001 — Kobweb as the framework

Status: accepted · Date: 20 Oct 2026 · Decided by: the author (L18)

## Context
Kotree is a public, no-login learning site shaped like one pixel-art tree. The author works in Kotlin and wants one language for the site, the tree renderer and later the sims. The site is static, desktop-first, served at https://kotree.garvitmaheshwari.in with no base path (L25). Content is Markdown in /content (L17).

## Decision
- Framework: Kobweb (Kotlin/JS, Compose HTML, Silk), pinned `kobweb = "0.25.1"` (latest stable on 18 Oct 2026; re-checked on <date of gate G2>). It compiles with the Kotlin version that release names (2.4.10 at that pin).
- Site project in /site, package `dev.kotree.site`, JDK Temurin 17 locally and in CI.
- `LESSON_KOTLIN_VERSION = "2.4.21"` for lesson code and the Playground `data-version`, re-checked on <date of gate G3>. It may differ from the Kotlin version that compiles the site.
- Static export: `kobweb export --layout static`.
- Build order (L28): the scaffold and the pixel tree are one milestone (docs/12) with all values in one Tokens.kt; design tokens are applied in Phase 1, content pipeline in Phase 3.

## Rejected options
None are recorded in docs/00 to docs/11; the stack is an author lock (L18). Add rows here only with their reason if the author names alternatives.

## Consequences
- One main JS bundle for every page; lesson text will land in it (docs/07 §8). The Phase 0b bundle spike (P0-7) decides whether ADR 005 is needed.
- Kobweb version bumps are deliberate and go through a new ADR or an edit here with the date.
- Verified-command table lives in docs/07 §3; no other flags are used.
```

## 10. Acceptance, risks, open questions

**Acceptance (session 3, `reviewer` checks):**

- [ ] G1 to G5 have recorded results.
- [ ] `kobweb run` shows a white page with one black tree; no dark mode; no console error.
- [ ] At `z = 1` exactly 5 tree labels are visible, all bough names (`03` §2.7). After `z >= 2.4` the 16 branch labels plus Foundation are visible.
- [ ] Wheel from `z = 1` to `z = 2.4` over any bough shows the limbs separating in 8 visible steps; stopping at `z = 1.80 ± 0.02` does not flicker labels.
- [ ] Clicking a bough lands at `z >= 2.4`, fully split; clicking a branch lands at `z = 4.0`; `Home` returns to `z = 1`.
- [ ] Same pixels on every load (the geometry hash logged to the console is identical twice).
- [ ] `grep` finds no hex colour, font name or pixel size outside `Tokens.kt`.
- [ ] No leaf, stem, lesson or `/content` read exists; the export (G5) still succeeds.
- [ ] P0-10 numbers and the baseline bundle size are written into `docs/07` §8 by the main agent (reported by the reviewer).

**Risks.**

| Risk | Mitigation |
|---|---|
| Kobweb scaffold has Windows or version friction | Gates G1-G5 come first; if G4 or G5 fail, stop and report |
| Pixel scale (`02` §5 rule 6: constant world pixel) clashes with integer-scale blit (`07` §7) | Prototype uses the integer blit and caps `z` at 4.0; limbs look blocky near 4.0. Phase 2 remainder re-rasterises per level |
| Canvas or Compose HTML API names unknown to the engineers | Use only names found in the Kobweb / Compose HTML docs; mark anything else `⚠️ VERIFY` in code review |
| Kotlin/JS test setup in the template unverified | Determinism is checked by console hash in this milestone; the unit test is owed to Phase 2 remainder |
| Frame time over budget with 36 bitmaps | Fewer steps (4 instead of 8) is a `Tokens.kt` change; record the numbers either way |

**Open questions for the author.**

- Q1: With no live leaves, branch limbs are gray (L4 "shadow branches") and trunk, roots and bough limbs are black (L7, `02` §2.1). When a branch gets its first live leaf, does its limb turn black? This plan assumes yes; `02` §5 should say so.
- Q2: Is the "17 branches" wording in `03` §1 meant to include Foundation? This plan treats it as 16 + Foundation.

## 11. Rejected options (this plan)

- Keep Phase 0, then Phase 1, then Phase 2 in order: the author wants to see the tree first (L28).
- Wire `StyleVariable` tokens now: needs P0-5 and P0-6 first and gives the canvas nothing it can read; constants in one file are swapped in Phase 1.
- Sample leaves in the data: a fake "live" leaf on a public site could be mistaken for published content (hard rule 1).
- Z3 and Z4 in this milestone: need stems, leaves and lesson data that do not exist yet.
