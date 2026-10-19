# Next steps (handoff, written 18 Oct 2026)

> Read this first in a new session, then `docs/INDEX.md`. `docs/00-decisions.md` wins every conflict.

## State right now

- Spec set is written: `docs/00`-`10`, `docs/INDEX.md`, `content/_templates/leaf.md`, 17 `content/*/_roadmap.md` (482 leaves, 20 fruits, all `shadow`), 10 agents in `.claude/agents/`, review reports in `docs/reviews/` (A2e-* files).
- Nothing is published. No site code exists yet (`/site` not created). Git repo exists (created in the IDE) but **nothing is committed or pushed**. The author will push later.
- Open `⚠️ VERIFY` marks in docs 00-10: about 53 (each has a test). Roadmap marks (~287) are checked per leaf before it goes live.

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
