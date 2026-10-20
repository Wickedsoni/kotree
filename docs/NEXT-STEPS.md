# Next steps (handoff, written 18 Oct 2026, updated 20 Oct 2026 after Phase 3)

> Read this first in a new session, then `docs/INDEX.md`. `docs/00-decisions.md` wins every conflict.

## State on 18 Oct 2026 (historical; current state is "Phase 3 done" below)

- Spec set is written: `docs/00`-`10`, `docs/INDEX.md`, `content/_templates/leaf.md`, 17 `content/*/_roadmap.md` (482 leaves, 20 fruits, all `shadow`), 10 agents in `.claude/agents/`, review reports in `docs/reviews/` (A2e-* files).
- Nothing is published. No site code exists yet (`/site` not created). Git repo exists (created in the IDE) but **nothing is committed or pushed**. The author will push later.
- Open `⚠️ VERIFY` marks in docs 00-10: about 53 (each has a test). Roadmap marks (~287) are checked per leaf before it goes live.

## Phase 3 done, 20 Oct 2026 (read this first, then docs/INDEX.md)

**Repo:** https://github.com/Wickedsoni/kotree (public, `main`). Everything below is committed and pushed. Work on `main`; the orchestrator makes small conventional commits; agents never commit. Author is on Windows; git identity for this repo is `Wickedsoni` (noreply).

**Phase 3 summary**
- S1: probes (export shape OK, hyphenated Markdown file names break Kobweb, V11), bundle spike FAILED (4,190 B gzip per compiled lesson), so ADR 005: lessons become JSON plus pre-rendered pages.
- S2: `buildSrc` parser, validators V1-V12, generator (`GeneratedTreeData.kt` + `tree.json`, deterministic), recorded in ADR 003 (`docs/adr/003-content-pipeline.md`).
- S3: the tree draws real stems (labels), twigs and the 17 up-next leaves at deeper zoom (shadow leaves never drawn, D3), stem fly, up-next popup, accessible stem and leaf buttons. Review: `docs/reviews/P3-acceptance.md` (screenshots in `docs/reviews/phase3/`).
- Numbers (`docs/07` §8.1): `kotree.js` gzip 277,917 (M0) → 313,048 (S2, T = +35,131) → 323,366 (S3, +10,318). `limbHash` `cecb906380417dd8`; `fullHash` `c9399b38f0f393cf` is provisional until the FIX-5 pass lands (final value in `P3-acceptance.md`).
- Watch: the ADR 005 projection re-based on S3 is about 396 KB of the 400 KB budget; Phase 4 must measure the lesson runtime and per-leaf cost at the pilot and likely drop `/kitchen-sink` from production (about -30 KB).
- Open from the review: up-next pip nearly the same grey as twigs (design call); first stems / up-next leaf can be off-screen at landing z=4 on long limbs (per-branch landing z or centre on the up-next stem); chunky twigs at z 8-24 (Phase 2 remainder, re-raster per zoom).

**Next, in order**
1. Phase 4 (lesson page and pilot leaf `kotlin/null-safety/what-is-null`, lunch-box analogy): **gated** on the author saying he understood what-is-null. Before building, close the open design gaps from ADR 005: how `runnable` is set for a plain kotlin fence; EditorShell to Playground handoff; where challenge state lives; SimFrame vs SimHost hosting; the VERIFY comment in the worked example breaks V12 once live.
2. Ask the author: "add /topics?" (a plain list page of live and up-next topics; not decided).
3. Add `scripts/spike-bundle.py` (cleaned) for Phase 8 bundle checks.
4. Phase 0b leftovers in docs/11, then Phase 5 (sims), 6 (editor), 7 (deploy; Vercel Hobby is fine while the channel is not monetised), 8 (polish).

## Moving to cloud sessions

- Everything is pushed to GitHub; a cloud session must `git clone https://github.com/Wickedsoni/kotree` (full clone, not shallow: V8 reads `origin/main` and only warns when it is missing).
- No Windows-only paths in commands or docs. The Kobweb CLI path in "How to run" below is the author's local Windows install; a cloud machine installs its own CLI (Linux: `docs/07` §2.1 options, or the CI way in `docs/07` §3: download the pinned 0.9.23 zip from `varabyte/kobweb-cli` releases) plus JDK 17 (ADR 001).
- `./gradlew` may need the executable bit (`docs/07` §2.2 step 4). The first export downloads a Playwright browser.
- Results may differ from the local runs: Gradle and export behaviour (the export exit code on Linux is P4-4 in `docs/11`), and headless Chrome checks (fonts, DPR, frame timings, screenshots). Re-measure before comparing numbers.

## Earlier detail (kept as the record)

**Done (all reviewed unless noted)**
- Spec set docs/00-14, ADRs 001 and 005, roadmaps for 17 branches (482 leaves, 20 fruits, all `shadow`), 10 agents in `.claude/agents/`.
- Tree-first prototype (docs/12): pixel tree, pan/zoom/fly, labels, whole-number pixel scale, nudged labels, only the current bough's branch labels (docs/reviews/S3-*).
- Phase 1 (docs/13): tokens, self-hosted fonts, Button/Chip, layout and lesson/challenge components, unlisted noindex `/kitchen-sink`, contrast and hard-code check scripts (docs/reviews/P1-*).
- Phase 3 S1 (docs/14, docs/reviews/P3-probes.md): lesson export shape OK; hyphenated file names break Kobweb (sync renames `-` to `_` plus absolute routeOverride, check V11); bundle spike FAILED (about 4,190 B gzip per compiled lesson), so ADR 005: lessons are rendered to JSON (`/lessons/<id>.json`) plus pre-rendered pages, not compiled into the app.
- Phase 3 S2: `buildSrc` parser and validators V1-V12 (50 tests, 0 failures), tree data generator (`generateTreeData` writes `tree.json` and the Kotlin data, deterministic), home page now uses generated data (`SampleTree.kt` deleted). Real numbers: 482 leaves, 180 stems, 17 branches, 20 fruits; day one 0 live, 17 upnext, 465 shadow. Limb hash still `cecb906380417dd8`. `kotree.js` gzip 313,048 B (T = +35,131 over 277,917; ADR 005 needs T <= 40,000; budget 400,000).

**How to run:** `./gradlew :site:kobwebStart` then http://localhost:8080/ ; stop with `./gradlew :site:kobwebStop`. NEVER use interactive `kobweb run` (opens looping console windows). On the author's Windows machine only, the Kobweb CLI is at `C:\Users\Avik\.cache\portfolio-tools\kobweb-cli\kobweb-0.9.23\bin\kobweb.bat` (cloud: see "Moving to cloud sessions"). `kobweb export` can exit 0 even when the build fails: check that output files exist. Agents must not leave `find /` searches or headless Chrome running (a stuck shell and a hung headless Chrome blocked agents; kill stale processes if an agent's token count stops moving).

**Rules to keep:** docs/00 wins; L28 (tree first), L29 (lesson voice: industry accurate, easy to say aloud, one analogy with where it stops); no leaf goes live until the author says he understood it.

## Resumed 20 Oct 2026

The two stopped agents finished. `docs/07` §6.7 now holds the lesson-export plan (fixed `@Page` routes, `addExtraRoute` kept as backup), `docs/11-phase0-checklist.md` lists 30 VERIFY rows plus 5 Phase 0 checks, and the CSS-variable rule and 720 px SimFrame width are settled (`docs/02` §3.1, §4.5). Roadmap check re-run: 482 leaves, 20 fruits, 0 bad `requires`. `docs/my-timeline.md` is the author's study plan.

## To do, in order

1. ~~Finish the two agents.~~ Done 20 Oct 2026.
2. ~~Re-run the roadmap check script~~ Done 20 Oct 2026 (482 / 20 / 0). Original note: re-run the roadmap check script (YAML leaves == table rows, `requires` exist and are earlier, no dangling IDs). Last good result: 482 leaves, 20 fruits, 0 bad requires. Edited since then: `content/kotlin/_roadmap.md` (`what-is-null` requires).
3. One final consistency read (reviewer, Sonnet) after the edits since `docs/reviews/A2e-final.md`. Look for new contradictions only.
4. Author review of: docs 00, 01, 02 (colours), 03 (zoom), 04 (lesson page), 08 (counts, 3-year timeline, KMP-without-a-Mac section 8.1).
5. Author confirms two proposals in `docs/08` §8.1: (a) the three evidence labels as plain sentences in the lesson body; (b) a Mac-needed leaf goes live only with a linked green CI run (or a borrowed-Mac note).
6. Author decision still open: Vercel Hobby is non-commercial. Fine for a personal journal with no ads. If a monetised YouTube channel counts as commercial, ask Vercel Support (L27 says free tiers; upgrade later only if needed).
7. Before the first commit: scan for secrets and personal data (hard rule 7). The repo is public. Then `git add`, a first commit with a conventional message (`docs: ...`), then the author pushes.
8. Phase 0 (`docs/10`, `docs/11`): create `/site` (Kobweb 0.25.1), GitHub Actions build, Vercel CLI `--prebuilt` deploy, subdomain `kotree.garvitmaheshwari.in` (CNAME in Hostinger DNS), ADR 001. Run the `docs/11` tests (cheap blockers first). Pin Kotlin 2.4.21 (`LESSON_KOTLIN_VERSION`) and re-check it is in the Playground list.
9. Phase 1+ per `docs/10`. Publish a leaf only after the author says "I understood X" (L5). Video is optional and added later (L26).

## Decisions made on 18 Oct 2026 (all recorded in docs/00 unless noted)

- Subdomain `kotree.garvitmaheshwari.in`, site root `/`, no base path (L25).
- Free tiers only; Kotree is the author's personal tree of growth, shared publicly (L27).
- Hosting: GitHub Actions builds, Vercel CLI `--prebuilt` deploys (D17).
- Video optional, added later; button/section appear only when `video` is set (L26).
- No Android emulator or simulator; Android behaviour shown by `code-to-screen` sims and animations (L15, D8). Profiling uses `animation`.
- Up-next computed, optional `upnext: true` override (D15); live list = leaf `status: live` (D16).
- Challenge format: fenced `challenge` YAML block (`docs/06`). Kotlin 2.4.21.
- Tree data: generated `TreeData.kt` for the app, plus `/tree.json` for tools.
- Trim: about 450 leaves target, real count 482 (band 360-540). About 3 years at 3 leaves a week.
- Max 3 subagents at once (Claude Pro). Roadmap runs R3-R8 used Sonnet.
- `physics-designer` effort set to high.
- Room and release-builds leaves stay single leaves.
- KMP on Windows: no iOS builds; use a public repo with free GitHub Actions `macos-latest` as iOS evidence (`docs/08` §8.1).

## Known risks to remember

- Kobweb static export skips dynamic routes: Kobweb skips dynamic routes on export; lessons are static pages, so no `addExtraRoute` (`docs/07` §6.7).
- Lesson text compiles into the main bundle; at about 480 leaves it may exceed the 400 KB budget. Phase 0 spike with 50 synthetic leaves decides.
- Kobweb drops HTML comments, so `syncLessons` converts `<!-- SIM: id -->` before Kobweb sees it.
- Nothing was compiled or run so far: every `verified:` field is `UNVERIFIED`.
- `m3.material.io` could not be read by the tools; the Roboto Flex M3E claim rests on a Wear OS guide.
