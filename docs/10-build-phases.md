# 10 — Build Phases and Master Prompts

> **Purpose:** the order in which the site is built, what each phase delivers, and the exact prompt to paste into Claude Code (main agent: Sonnet 5.5, medium) for each phase.
> **Read this when:** starting a phase or resuming after a break.
> **Depends on:** `00`, `02`, `03`, `04`, `05`, `06`, `07`, `09`, `12`.
> **Version:** v1.3 · 20 Oct 2026 (v1.1, review A2e: Phase 3 data file, Phase 5 sim IDs and acceptance, Phase 6 sim and challenge pipeline. v1.2: lesson export per `docs/07` §6.7 in Phases 3 and 4; Phase 0 runs `docs/11-phase0-checklist.md`. v1.3: Phase 0 and Phase 2 merged into the tree-first milestone, `docs/00` L28, plan in `docs/12`).

**Build order (L28): tree-first milestone (Phase 0 + Phase 2, `docs/12`) → Phase 1 → Phase 0b (the rest of Phase 0) → Phase 2 remainder → Phase 3 → Phase 4 → …** Phase numbers are not renumbered because `docs/03` and `docs/11` cite them.

Rules for every phase: one phase = one branch in git (`phase-N-<name>`; the merged milestone uses `phase-0-2-tree-first`), small commits, `reviewer` PASS before merging to `main`, Vercel preview checked by the author. The author learns while building, so publish leaves in parallel with phases from Phase 4 onward.

---

## Phase 0 + Phase 2 — Tree-first milestone (merged, L28)

**Plan:** `docs/12-tree-first-plan.md` (scope, checks, file ownership, acceptance, ADR 001 text). At most 3 sessions; branch `phase-0-2-tree-first`.
**Delivers from Phase 0:** Kobweb scaffold in `/site`, white page, gates G1-G5 (JDK, pins, `kobweb run`, export), `.gitignore`, ADR 001.
**Delivers from Phase 2:** procedural pixel tree on a canvas from hard-coded sample data (`docs/00` §5), Z0 and Z1 only (grouped boughs splitting into branch limbs, labels), wheel zoom, drag pan, `Home`, click-to-fly to a bough or branch.
**Not delivered, and owed:**
- Phase 0b (after Phase 1): CI workflow, README, LICENSE, and every `docs/11` row not run in the milestone (order and deadlines in `docs/12` §4). Phase 0's acceptance sentence about `docs/11` still applies to Phase 0b.
- Phase 2 remainder (after Phase 0b, before or with Phase 3): breadcrumb, minimap, "Start here" trail, full keyboard map, double-click zoom, Z2 to Z4 (stems, leaves, up-next glow, popups), re-rasterising per zoom level, `z` above 4.0, determinism unit test, 60 fps measured on the author's laptop.

> **Master prompt (tree-first milestone)**
> You are the orchestrator for Kotree. Read `CLAUDE.md`, `docs/INDEX.md`, then `docs/12-tree-first-plan.md` only. Run the session the author names (S1, S2 or S3) exactly as `docs/12` §5 and §6 say: one owner per file, `Tokens.kt` read-only in S2, no more than 3 parallel runs. S1 starts with gates G1-G5; stop and report if one fails. Use hard-coded sample data, no leaves. Report in 150 words.

---

## Phase 0b — Rest of Phase 0 (after Phase 1)

Text below is the original Phase 0 definition. In the tree-first order, the scaffold and ADR 001 are already done; what remains is the CI workflow, README, LICENSE and the deferred `docs/11` rows.

**Goal:** an empty Kobweb site that builds locally, in CI, and shows a white page with the project name.
**Agents:** `architect` (plan and ADR 001), `ui-engineer` (scaffold), `housekeeper` (README, .gitignore, LICENSE).
**Deliverables:** `/site` Kobweb project, `README.md`, `LICENSE`, GitHub Actions workflow that builds the export, ADR 001 recording the Kobweb choice, the pinned Kobweb version and `LESSON_KOTLIN_VERSION` (`docs/07` §1; pinned to Kotlin 2.4.21, re-checked against the Playground version list).
**Acceptance:** `kobweb run` shows the page; the CI export succeeds; the repo contains no secrets; every Phase 0 row of `docs/11-phase0-checklist.md` has a recorded result (pass, or the fallback applied).

> **Master prompt**
> You are the orchestrator for Kotree (read `CLAUDE.md`, `docs/INDEX.md`, then `docs/07-technical-architecture.md`). Phase 0b (the scaffold from `docs/12` already exists; skip creating it): add a GitHub Actions workflow that exports the static site, plus README and LICENSE. For a fresh repo without the milestone, create the Kobweb project in `/site` with a white placeholder home page first. Run `architect` first for a one-page plan and ADR 001, and run the Phase 0 rows of `docs/11-phase0-checklist.md` in the order listed (cheap blockers first). Then launch `ui-engineer` (scaffold) and `housekeeper` (README, LICENSE, .gitignore) in parallel. Finish with `reviewer`. Use the exact commands from `docs/07`; do not guess flags. Report in 150 words.

---

## Phase 1 — Design tokens and core components

**Order:** runs after the tree-first milestone (`docs/12`). Start by running the Phase 1 rows of `docs/12` §4 (P0-5, P0-6, P0-8, P0-11, P0-12), then replace the constants in `Tokens.kt` with the final `docs/02` tokens (or `StyleVariable`s per `docs/02` §3.1), keeping the token names the tree already uses. The tree must look the same or better afterwards.

**Goal:** the visual language exists in code: colours, type, shapes, motion and the base components, shown on a private `/kitchen-sink` page.
**Agents:** `ui-engineer` (tokens, components), `reviewer` (design-system conformance), `fixer`.
**Deliverables:** token file (colours incl. branch accents, type scale, shape and motion tokens), components from `docs/02` (leaf popup, branch panel, breadcrumb, progress counter, buttons, code frame, challenge card), self-hosted fonts.
**Acceptance:** no hardcoded values outside the token file; contrast meets `docs/02`; reduced-motion respected.

> **Master prompt**
> Phase 1 of Kotree. Read `docs/02-design-system.md` and `docs/07`. Implement the design tokens and the component list from `docs/02` §components in `/site`, plus a `/kitchen-sink` page that shows every token and component. Split the work so at least 3 `ui-engineer` runs go in parallel on separate files: (1) tokens + fonts, (2) layout components, (3) lesson and challenge components. Then `reviewer` checks conformance and `fixer` applies mechanical items. No hardcoded colours, fonts or spacing outside the token file. Report in 150 words.

---

## Phase 2 — The tree world (first part merged into the tree-first milestone; remainder here)

**Status (L28):** Z0 and Z1, the generator, renderer and minimal camera are built in the tree-first milestone (`docs/12`). Everything below that `docs/12` §3 lists as skipped is the **Phase 2 remainder**, done after Phase 0b and before the tree reads real data in Phase 3. Use the master prompt below for the remainder, starting from the existing `tree/` code.

**Goal:** the home page is a standing pixel-art tree on white with a camera: drag, wheel zoom, click-to-zoom into a branch, breadcrumb, minimap, semantic zoom levels Z0 to Z2 (tree → bough → branch).
**Agents:** `architect` (camera and layout algorithm plan), `ui-engineer` × 2 (canvas renderer; camera and interaction), `sim-engineer` not needed yet, `reviewer`.
**Deliverables:** deterministic procedural tree generator from data, renderer with pixel-scaling rules, camera with eased transitions, shadow styling, "Start here" trail from the roots, keyboard navigation.
**Acceptance:** 60 fps on the author's laptop at Z0; same tree on every load; every branch reachable by keyboard; nodes remain clickable at every zoom (see `docs/03`).

> **Master prompt**
> Phase 2 of Kotree. Read `docs/03-information-architecture.md`, `docs/02` (pixel tree sections) and `docs/07` (camera and canvas sections). First run `architect` for a plan with file ownership. Then in one message launch `ui-engineer` twice: one owns the tree generator and renderer files, the other owns camera, input and breadcrumb/minimap files. Use hard-coded sample data matching `docs/00` §5 until Phase 3. End with `reviewer` (performance, determinism, keyboard access). Report in 150 words.

---

## Phase 3 — Content pipeline

**Goal:** the tree is driven by content files. Roadmap YAML + leaf frontmatter → generated Kotlin `TreeData.kt` at build time (`generateTreeData`, `docs/07` §6.4; the same generator also emits `/tree.json` for tools; the app does not fetch it at runtime); leaf Markdown → one static page per `status: live` leaf through `syncLessons` (`docs/07` §6.2), which the static export writes as `<branch>/<stem>/<leaf>.html` without `addExtraRoute` (`docs/07` §6.7); shadow and up-next leaves get no page and no file; states `shadow`, `upnext`, `live` rendered correctly; progress counter is real.
**Agents:** `architect` (schema and generator plan), `ui-engineer` (Gradle task, loaders), `housekeeper` (validation script, `PROGRESS.md`), `reviewer`.
**Deliverables:** `generateTreeData`, `syncLessons` (with the sim-marker rewrite R1), `validateContent` with the rules of `docs/07` §6.4 (unknown IDs, forward `requires`, duplicate slugs and the rest fail the build), `verifyLessonExport` (`docs/07` §6.7) as a CI step after the export, `PROGRESS.md` generator, tree reading real `content/*/_roadmap.md`.
**Acceptance:** adding a leaf file and running the build changes the tree; a bad ID fails the build with a clear message; `verifyLessonExport` passes, i.e. the export holds exactly one lesson `.html` per live leaf in `tree.json` and none for shadow or up-next leaves; `site/build.gradle.kts` contains no `addExtraRoute`.

> **Master prompt**
> Phase 3 of Kotree. Read `docs/00` §8, `docs/03` (data model) and `docs/07` (pipeline). Plan with `architect`, then run in parallel: `ui-engineer` (generator + loader code), `housekeeper` (validation script and `PROGRESS.md`), and a second `ui-engineer` (shadow/upnext/live rendering rules from `docs/03`). Validation must fail the build on unknown IDs, forward `requires`, duplicate slugs. Lessons are exported as static pages; do not add `addExtraRoute` (`docs/07` §6.7); add `verifyLessonExport` to the CI build. Finish with `reviewer`. Report in 150 words.

---

## Phase 4 — Lesson page and the pilot leaf

**Goal:** a leaf opens as a full lesson page with the structure from `docs/04`, a video section and Watch-the-video button that appear only when the leaf's `video` field is non-empty, previous/next, "back to the tree" that restores the camera, and one real pilot lesson live.
**Agents:** `ui-engineer` (renderer), `lesson-writer` (pilot), `challenge-builder`, `reviewer`, `fixer`, `housekeeper`.
**Deliverables:** Markdown renderer with the extensions defined in `docs/04`, lesson layout, the `NotFound` page with the shadow-leaf stub (`docs/03` §7.4, `docs/07` §4 and §6.7), the pilot leaf `kotlin/null-safety/what-is-null` (or the first leaf the author understood), published through pipeline P1.
**Acceptance:** pilot passes `reviewer`; the tree shows the leaf as live; the lesson works without JavaScript errors; the export contains `kotlin/null-safety/what-is-null.html` (or the pilot's own path) and `verifyLessonExport` passes; opening the URL of a shadow leaf in the same stem shows "This leaf has not grown yet." with a link back to the stem (row P4-1 of `docs/11-phase0-checklist.md`).

> **Master prompt**
> Phase 4 of Kotree. The author says he understood `<LEAF-ID>`. Run pipeline P1 from `docs/09` exactly. In wave 1 launch `lesson-writer`, `challenge-builder` and `ui-engineer` (lesson page renderer per `docs/04`, plus the `NotFound` shadow stub per `docs/07` §6.7) in parallel. Add `physics-designer` only if the roadmap row names a sim idea. Then `reviewer`, `fixer`, `housekeeper`. Do not set `live` before PASS. Report in 150 words.

---

## Phase 5 — Physics layer

**Goal:** the sim framework and the first three sims run inside lesson pages: `call-stack-tube` (`physics`), `hash-chute-buckets` (`physics`) and `lifecycle-timeline` (`code-to-screen`), all from the `docs/05` §5.5 catalogue.
**Agents:** `physics-designer` × 3 (parallel, one per sim), `sim-engineer` × 3 after their specs, `architect` (framework plan first), `reviewer`.
**Deliverables:** `SimEngineLoader`, `MatterExternals.kt`, `SimWorld`, `SimRenderer`, `SimHost` with the standard controls of `docs/05` §7.1, `SimRegistry`, fixed-timestep loop, three sims with specs under `docs/sims/`, the determinism replay test (`docs/05` Q2).
**Acceptance:** each sim meets its spec's acceptance list and the budgets of `docs/05` §6.2–6.4; with `prefers-reduced-motion: reduce` there is no autoplay and every learner action shows its settled result with a 150 ms fade (`docs/05` §7.2); the same actions after Reset or reload give the same **state** (which box or slot holds what), not the same coordinates (`docs/05` §5.2).

> **Master prompt**
> Phase 5 of Kotree. Read `docs/05-physics-and-visualization.md` and `docs/07`. First `architect` plans the sim framework (host component, engine wrapper, loop, API). Then launch `physics-designer` three times in parallel for the sims `call-stack-tube`, `hash-chute-buckets`, `lifecycle-timeline` (`docs/05` §5.5); after each spec exists launch `sim-engineer` for it, one file area per sim. Finish with `reviewer`. Report in 150 words.

---

## Phase 6 — Editor and challenges

**Goal:** runnable Kotlin in lessons, Predict-the-output, Fix-the-bug, Read-the-AI-code, and a second `code-to-screen` sim, `remember-rotate` (`docs/05` §4.3, §5.5; no emulator, no simulator).
**Agents:** `architect` (Playground integration plan), `ui-engineer` (editor chrome, challenge components, the challenge rewrite R2), `challenge-builder` (content), `physics-designer` then `sim-engineer` (`remember-rotate`), `reviewer`.
**Deliverables:** Playground integration with offline fallback message and `data-version` = `LESSON_KOTLIN_VERSION`; ```` ```challenge ```` blocks turned into `PredictCard` / `FixCard` widgets by `syncLessons` with validation rules CH1–CH10 (`docs/07` §6.2.1); challenge components with hint ladder and local-storage progress; the `remember-rotate` spec and sim.
**Acceptance:** a learner can edit and run code, get feedback on a wrong answer without a score screen, and see their checkmark persist after reload.

> **Master prompt**
> Phase 6 of Kotree. Read `docs/06-editor-and-challenges.md`, `docs/07` §6.2.1 and `docs/05` §4.3. `architect` plans the Playground integration. Then in one message launch `ui-engineer` (editor chrome, challenge cards, the `syncLessons` challenge rewrite and CH1–CH10), `challenge-builder` (challenges for the pilot leaf) and `physics-designer` (spec for the `code-to-screen` sim `remember-rotate`); after the spec exists, launch `sim-engineer` for it. Finish with `reviewer`. Report in 150 words.

---

## Phase 7 — Deploy

**Goal:** the site is live on kotree.garvitmaheshwari.in through Vercel (DNS CNAME created in Hostinger), every push to `main` redeploys, previews work on pull requests.
**Agents:** `architect` (deploy plan from `docs/07`), `housekeeper` (README deploy section), main agent (manual steps with the author).
**Deliverables:** GitHub Actions build, Vercel project, domain connected, HTTPS, `sitemap.xml`, social preview image, 404 page served with HTTP 404 (page built in Phase 4), and the Phase 7 rows of `docs/11-phase0-checklist.md`.
**Acceptance:** `https://kotree.garvitmaheshwari.in` serves the site; a content commit goes live within minutes.

> **Master prompt**
> Phase 7 of Kotree. The site must be served at `https://kotree.garvitmaheshwari.in` (a subdomain, no base path; L25, D17). Read `docs/00` L25, D17 and the deploy sections of `docs/07`. Ask `architect` to produce the exact click-by-click checklist for Vercel and the Hostinger DNS records (verify the current Vercel instructions). Run `housekeeper` for the README. Do not run deploy commands that need secrets: list them for the author to run. Report in 150 words.

---

## Phase 8 — Polish and launch

**Goal:** the first public version is fast, accessible, and ready to share with the first 10 to 20 published leaves.
**Agents:** `reviewer` × 3 in parallel (design, accessibility, performance), `fixer`, `ui-engineer` for what they find, `housekeeper`.
**Deliverables:** audit reports in `docs/reviews/`, fixes, launch checklist (OG images, README, channel links on the home page).
**Acceptance:** no critical findings; Lighthouse accessibility and performance targets from `docs/07` met; every branch has at least a visible shadow with a hint.

> **Master prompt**
> Phase 8 of Kotree. Launch three `reviewer` runs in parallel: design conformance, accessibility, performance. Collect FIX lists, give mechanical items to `fixer` and the rest to `ui-engineer`. Re-run `reviewer` once. Then `housekeeper` updates `PROGRESS.md` and the launch checklist. Report in 150 words.

---

## After launch: the weekly loop

Run pipeline P1 (`docs/09`) for each leaf you understood. Every Sunday run P4. Once a month run P5.
