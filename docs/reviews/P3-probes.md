# Phase 3 probes (S1, 20 Oct 2026)

Method: scratch copy of the repo outside it (`.../scratchpad/work`, deleted afterwards), Kobweb CLI 0.9.23 from the `varabyte/kobweb-cli` release zip (not on PATH in this environment), Windows 11, Git Bash. No fixture, spike file or script is in the repo. Nothing is staged or committed. `docs/14` plan names `probe/p3`; per the author's answers the work stayed on `main` and the probe lived in the scratch copy instead. `scripts/spike-bundle.py` does not exist in the repo (the plan says it is merged to `main`); a throwaway version was used from the scratch folder and is **not** added (see Open items).

## Step 1: P0-3, P0-4, U4, U5 (one export run set)

| Row | Date | Result | Observed | Action |
|---|---|---|---|---|
| Slug audit (`docs/14` 2.0) | 20 Oct 2026 | not re-run | Done earlier by the architect; roadmaps unchanged. | none |
| P0-3b route file shape | 20 Oct 2026 | **PASS** | `kotlin/null-safety/what-is-null.html`, `concurrency/suspend/what-suspend-means.html`, `kotlin/extensions-scope-and-delegates/lateinit.html` all exist; shadow leaf `is-null-the-same-as-zero.html` absent. Shape is `<route>.html`. | none |
| P0-3a generated names | 20 Oct 2026 | **FAIL as planned, fallback PASS** | Folder names are fine: hyphens become underscores (`pages.kotlin.null_safety`, `pages.kotlin.extensions_scope_and_delegates`), and the soft keyword `suspend` compiles as a package segment. **File names are not sanitised**: `what-suspend-means.md` generated `fun initWhat-suspend-meansPage(...)` and `fun What-suspend-meansPage()`, a Kotlin syntax error, and the build failed (`compileKotlinJs`). Fallback used: `syncLessons` renames each file `-` to `_` (`what_suspend_means.md`, generated `What_suspend_means.kt`) and injects `routeOverride: /<id>` after the `id:` line. Export then succeeded with the exact hyphenated routes above. | `07` 6.2: the route-override fallback is required, not optional. Add validator rule V11 (docs/14 S2-A): slug not on the unsafe list; note the lesson file name rule is "no `-` in the synced file name". Generated names recorded: `.../pages/concurrency/suspend/What_suspend_means.kt`, `.../pages/kotlin/extensions_scope_and_delegates/Lateinit.kt`, `.../pages/kotlin/null_safety/What_is_null.kt`. Architect to update `07` and `11`. |
| P0-4 `@Page("404")` | 20 Oct 2026 | **PASS** | `.kobweb/site/404.html` exists. | none |
| U4 `addSource(syncLessons)` | 20 Oct 2026 | **PASS** | `kobweb { markdown { addSource(syncLessons) } }` with `syncLessons` a `Sync` task registered via `tasks.register<Sync>`: pages generated from `build/kotree/markdown`. No `dependsOn` needed. | none |
| U5 `{{{ ProbeSim("null-lunch-box") }}}` | 20 Oct 2026 | **PASS** | Generated `ProbeSim("null-lunch-box")` call; with `imports.add("dev.kotree.site.probe.*")` it compiled, and `grep -c null-lunch-box` on the page HTML = 1 (stub text rendered). | none |

Side findings (both matter for Phase 4):

1. **`kobweb export --notty --layout static` returned exit 0 on a failed build** (CLI 0.9.23 via `kobweb.bat`; four failing runs printed `BUILD FAILED` and `Failed to export a Kobweb site.` yet `$?` was 0). `docs/07` 5.4 and `docs/14` section 5 ("exit non-zero") assume the opposite. CI and `scripts/verify-phase3.sh` must also check that `.kobweb/site/index.html` exists after export and that the log has no `Failed to export`. Could be a Windows wrapper effect; re-test on the Linux CI runner.
2. A `Sync` task using `eachFile`, `rename` or `filter` lambdas reports `UP-TO-DATE` after the lambda is edited (lambdas are not task inputs). The real `syncLessons` should be a custom task class (or declare the rules as inputs), as `07` 6.2 already says.

## Step 2: P0-7 bundle spike

50 synthetic leaves, about 8,100 B of Markdown each (about 1,225 words, 6 `##` headings, 2 lists, blockquote, 15-line Kotlin block, two challenge widgets calling the real `PredictOutput` and `FixBug` with literal data). Prose is real, non-repeating sentences from `docs/`, `content/` roadmaps and the local skills folder (docs alone were too small for 50 leaves: 288k of 480k characters needed). Pages 10/10 and 50/50 exported, tokens in bundle 10/10 and 50/50, no Kotlin errors or warnings.

| N leaves | `kotree.js` raw B | gzip -9 B |
|---|---|---|
| 0 (scratch baseline M0) | 975,783 | 278,899 |
| 10 | 1,145,130 | 320,962 |
| 50 | 1,827,990 | 488,421 |

The repo's own export is 277,917 B gzip (raw 972,954); the scratch copy is 982 B larger (path-dependent strings), so the slope uses the scratch M0 for a like-for-like difference.

| Quantity | Value |
|---|---|
| c50 = (M50 - M0) / 50 | **4,190 B gzip per leaf** |
| c10 = (M10 - M0) / 10 | 4,206 B (0.4 % from c50, within 15 %) |
| T (tree data reserve, until S2 measures it) | 40,000 B |
| projection = M0 + 482 x c + T (with M0 277,917) | 277,917 + 2,019,773 + 40,000 = **2,337,690 B** (about 2.34 MB) |
| budget | 400,000 B |
| leaves until budget = (400,000 - 277,917 - 40,000) / c | **19.6, so 19 leaves** |

Dropping `/kitchen-sink` (about 30 KB) buys about 7 more leaves; it cannot close a gap of about 1.94 MB.

**P0-7: FAIL.** The projection is 5.8 times the budget (worse than the plan's "about an order of magnitude" of room, but the same conclusion). Per the author's answer 2, ADR 005 option A (build-time prerender to static HTML fragments fetched by one lesson page) is the chosen direction; **the architect writes ADR 005 and updates `07` and `04`**. This thread stops here. Phase 3 S2 and S3 do not wait for it.

## Open items for the orchestrator

- `scripts/spike-bundle.py` is absent from the repo. The throwaway used here lives in the scratch folder. Phase 8 will want it repeated: say if a cleaned copy should be added to `scripts/`. (It writes `leaf_NN.md` with underscores because of the P0-3a file-name finding.)
- `11` DONE marks and `03`/`07` VERIFY removals belong to the architect, with the P0-3a deviation above.
