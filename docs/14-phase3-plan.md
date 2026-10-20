# 14 — Phase 3 plan: content pipeline, real tree data, stems and leaves

> **Purpose:** Phase 3 in at most 3 sessions: cheap checks first (P0-3, P0-4, P0-7), the generator that turns the 17 roadmaps into `TreeData` and `/tree.json` with validation V1-V10, and the tree changes that show 180 stems and the up-next leaves at deeper zoom.
> **Read this when:** starting Phase 3, touching `buildSrc`, `site/build.gradle.kts` content tasks, `tree/model/*`, or the tree's stem and leaf layers.
> **Depends on:** `00` (wins), `03` §2-§4, `07` §3, §6, §8, §8.1, `08` §2, §9.0, §15, `10` Phase 3, `11` rows P0-3, P0-4, P0-7, U4, U5, `12` §7, `13` §2.
> **Version:** v1.1 · 20 Oct 2026 · owner: `architect`. Branch names are advisory: work stays on `main`, commits by the orchestrator only (§8 answer 4). v1.1: S1 results (`docs/reviews/P3-probes.md`), ADR 005 written, V11 added to S2-A, export exit-code caveat (§5).

K = `site/src/jsMain/kotlin/dev/kotree/site/`. B = `buildSrc/src/main/kotlin/dev/kotree/build/`. Baselines to protect: limb geometry hash `cecb906380417dd8`; `kotree.js` 277,917 B gzip (`07` §8.1); budget 400,000 B.

Hard rule 1 holds all phase: **no leaf `.md` is added under `/content` on `phase-3-pipeline`**. Probe fixtures live only on `probe/p3` (deleted after S1). Day-one numbers under `03` §3.1 (D3, D15): **0 live, 17 up-next (first YAML leaf of each branch), 465 shadow, 482 total**. "All stay shadow" means no `status: live`; the 17 up-next leaves are computed, not published.

---

## 1. Scope changes against `docs/10` Phase 3 (flag for the orchestrator)

| Item | Decision | Why |
|---|---|---|
| `generateTreeData`, `validateContent`, `tree.json`, `PROGRESS.md` generator, tree reading real roadmaps, stems and up-next leaves | In Phase 3 (S2, S3) | Independent of lesson rendering. |
| Lesson delivery: `renderLessons`, `LiveLessonRoutes` + `addExtraRoute`, `emitLessonPages`, `verifyLessonExport`, the lesson page (replace `syncLessons`, R1/R2 rewrites, `ChallengeWidgets.kt`) | **Phase 4** (with the pilot leaf) | P0-7 failed; ADR 005 (`docs/adr/005-lesson-content-delivery.md`) set the design; no live leaf exists to test them. |
| `docs/10` acceptance "`verifyLessonExport` passes" | Moves with it to Phase 4 | Nothing to export. |
| Validator V11 (slug `index` or leading digit) | **Phase 3 S2-A** | Roadmap-only rule from the P0-3a finding (`07` §6.2). V12 (raw HTML in a live body) waits for Phase 4. |

## 2. S1 — Cheap checks (order matters; probe branch, then decide)

**Done 20 Oct 2026** (`docs/reviews/P3-probes.md`, scratch copy outside the repo instead of `probe/p3`): P0-3a FAIL with fallback PASS, P0-3b PASS, P0-4 PASS, U4 PASS, U5 PASS, **P0-7 FAIL** (4,190 B gzip per leaf, projection 2,337,690 B). ADR 005 written; `07`, `03`, `11` updated. Side finding: `kobweb export` exited 0 on failed builds (§5 adjusted). The text below is the plan as run.

One `ui-engineer` on `probe/p3` (from `main`), then `architect`. Commands are Git Bash from the repo root. Results go to `docs/reviews/P3-probes.md` on `phase-3-pipeline`; `architect` then records `DONE <date>` in `11` and removes the VERIFY marks in `03` §7.2/§11 and `07` §4/§6.2/§8.

### 2.0 Slug audit (done 20 Oct 2026 by grep over the 17 YAML blocks; repeat after any roadmap edit)

No slug starts with a digit. Kotlin keyword slugs that exist: stem `concurrency/suspend` and leaf `kotlin/extensions-scope-and-delegates/lateinit` (both soft keywords). Hard keywords (`when`, `object`, `in`, `is`) are **absent**, so the `docs/11` fixture `kotlin/when/object` tests a shape that does not exist. Re-run: `grep -rhoE "^\s+- id: .*" content/*/_roadmap.md | sed 's/.*id: //' | sort -u | grep -xE '[0-9].*|as|break|class|continue|do|else|for|fun|if|in|interface|is|null|object|package|return|super|this|throw|true|try|typealias|val|var|when|while'`.

### 2.1 Step 1 — P0-3 + P0-4 + U4 + U5 in one export run

On `probe/p3` only. Fixture leaves (frontmatter per `00` §8.2, `id` equal to the path, body one paragraph):

| File | `status` | Tests |
|---|---|---|
| `content/kotlin/null-safety/what-is-null.md` | live | `docs/11` name, plain case |
| `content/concurrency/suspend/what-suspend-means.md` | live | keyword stem (real shape); body has the line `{{{ ProbeSim("null-lunch-box") }}}` (U5; `ProbeSim(id: String)` is a stub composable in `K/probe/ProbeWidgets.kt`, imported with `imports.add("dev.kotree.site.probe.*")`) |
| `content/kotlin/extensions-scope-and-delegates/lateinit.md` | live | keyword leaf, long hyphenated stem (generated package/function names, P0-3a) |
| `content/kotlin/null-safety/is-null-the-same-as-zero.md` | shadow | must produce **no** file |

Also on the probe: `K/pages/NotFound.kt` with `@Page("404")` (stub text only; the real page is Phase 4), and a throwaway `syncLessons` in `site/build.gradle.kts`: a `Sync` task from `../content` (`include("*/*/*.md")`) into `build/kotree/markdown` that skips files not containing `status: live`, registered with `kobweb { markdown { addSource(syncLessons) } }` (U4; exact block name per `07` §6.3, ⚠️ VERIFY against the Kobweb docs before writing it).

```
cd site && kobweb export --notty --layout static ; echo exit=$?
for f in kotlin/null-safety/what-is-null concurrency/suspend/what-suspend-means kotlin/extensions-scope-and-delegates/lateinit; do test -f .kobweb/site/$f.html && echo OK $f || echo MISSING $f; done
test ! -e .kobweb/site/kotlin/null-safety/is-null-the-same-as-zero.html && echo shadow-absent
test -f .kobweb/site/404.html && echo OK 404
grep -c "null-lunch-box" .kobweb/site/concurrency/suspend/what-suspend-means.html     # >= 1 (U5 rendered; the frame text from the stub)
find build -name '*.kt' -path '*kobweb*' | grep -iE 'suspend|lateinit|null.safety' | head   # generated names (P0-3a): record them
```

| Result | Pass | Fallback (`11`) |
|---|---|---|
| Exit 0, three files, shadow absent | P0-3b `<route>.html` shape confirmed | Shape is `<route>/index.html`: update `07` §5.5, `verifyLessonExport` (Phase 4), re-run P7-2. |
| Build compiles with `suspend`, `lateinit`, hyphenated folders | P0-3a | `syncLessons` writes safe file names plus an absolute `routeOverride`; add rule V11 "slug not on the unsafe list" to the validator (S2-A). |
| `404.html` exists | P0-4 | `@Page("/404")`, re-run. |
| Pages exist (U4) / `ProbeSim` renders (U5) | U4, U5 | `addSource(layout.buildDirectory.dir(...))` + `dependsOn`; generated wrapper per sim (`07` §6.2). |

### 2.2 Step 2 — P0-7 bundle spike (50 synthetic leaves)

Tool: `scripts/spike-bundle.py` (stdlib, run as `python -I`; merged to `main` so Phase 8 can repeat it; its output folders are never committed). It writes, per leaf `NN`:
- `site/src/jsMain/resources/markdown/spike/leaf-NN.md`: about 1,500 words, frontmatter, 6 `##` headings, 3-sentence paragraphs, 2 bullet lists, 1 blockquote, 1 ```` ```kotlin ```` block of about 15 lines, a unique token `spikeleaf-NN`, and two lines `{{{ Spike_NN_predict }}}` and `{{{ Spike_NN_fix }}}`.
- `site/src/jsMain/kotlin/dev/kotree/site/spike/SpikeChallenges.kt`: 2 x N `@Composable` wrappers, each calling the existing `PredictOutput` (3 options with `why` text) or `FixBug` (2 hints, expected output, explanation) with literal data of realistic size. The file stands in for Phase 6's generated `ChallengeWidgets.kt`; the wrappers are referenced from the pages, so dead-code elimination cannot remove them.
- Text comes from **real prose**: `docs/*.md` with code fences and table rows stripped, split into non-overlapping slices, no sentence used twice (repeated text would gzip far better than real lessons). The script exits 1 if the corpus is too small for N x 9,000 characters.

```
cd site
kobweb export --notty --layout static
echo "0  $(wc -c < .kobweb/site/kotree.js)  $(gzip -9 -c .kobweb/site/kotree.js | wc -c)"       # M0, expect 277917
for N in 10 50; do
  python -I ../scripts/spike-bundle.py --leaves $N --corpus ../docs --out-md src/jsMain/resources/markdown/spike --out-kt src/jsMain/kotlin/dev/kotree/site/spike
  kobweb export --notty --layout static
  echo "$N  $(wc -c < .kobweb/site/kotree.js)  $(gzip -9 -c .kobweb/site/kotree.js | wc -c)"
  echo "tokens in bundle: $(grep -o 'spikeleaf-[0-9]*' .kobweb/site/kotree.js | sort -u | wc -l) pages: $(find .kobweb/site/spike -name '*.html' | wc -l)"   # both must equal N
done
```

Calculation (put the table in `P3-probes.md`): `c = (M50 - M0) / 50` (check `c10` within 15 % of `c50`; if not, report both). Reserve for tree data `T`: 40,000 B until S2 measures it (cap 60,000, `07` §8). `projection = M0 + 482 x c + T`. `leaves until budget = (400,000 - M0 - T) / c`.

**Pass:** `projection <= 400,000`. **Expected result: FAIL by about an order of magnitude** (a 1,500-word lesson is about 9 KB raw, 3-4 KB gzip; headroom 122 KB less T is room for roughly 20-25 live leaves, about 7-8 weeks of publishing at 3 a week). Dropping `/kitchen-sink` (about 30 KB) buys about 8 leaves and cannot close the gap, so the author's "sink ships" decision is not the issue.

**Result: FAIL; ADR 005 written as `docs/adr/005-lesson-content-delivery.md` (candidate A, refined: per-leaf lesson JSON rendered on the JVM, one dynamic lesson page, `addExtraRoute` per live leaf, `emitLessonPages` bakes head tags, static prose and inline JSON).** Original fallback text: `architect` writes the ADR before the pilot leaf, with the measured table. Candidates were: (A) build-time prerender of each live lesson to a static HTML fragment (JVM Markdown converter in `buildSrc`, so no parser in the bundle), one dynamic lesson page fetches it, export through the `addExtraRoute` path of `07` §6.7; (B) a second Kotlin/JS bundle loaded on demand (⚠️ VERIFY: no documented Kobweb support); (C) raise the budget with a reason (rejected unless c is under about 1 KB). **Phase 3 S2 and S3 do not wait for ADR 005**: they touch no lesson page.

S1 exit: `P3-probes.md` has all rows, the spike table and a PASS/FAIL line; ADR 005 drafted if FAIL; `probe/p3` deleted (`git branch -D probe/p3`) after the file is committed on `phase-3-pipeline`.

## 3. S2 — The generator (3 parallel runs, then integration)

### 3.1 V6 table format, resolved (closes `08` §15 Q1 and Q4; `curriculum-designer` copies this into `08`)

Counts that prove the rule on the real files (20 Oct 2026): rows matching `^\| \`slug\` \|` total **502** = 482 leaf rows + 20 Fruits rows; `- id:` lines total **662** (665 minus 3 in the template) = 482 leaves + 180 stems.

The parser reads each `_roadmap.md` as UTF-8 (accept `\r\n`) and:
1. **YAML:** the first fenced block must be ```` ```yaml ```` with only blank lines before it. Parse with SnakeYAML `compose` (a node tree), read every scalar as a **string** (no implicit types, so `publishedOn: 2026-10-20` never becomes a `Date`) and keep `startMark.line` for messages (⚠️ VERIFY the `compose` / `Node.startMark` API against SnakeYAML 2.7, the latest on Maven Central on 20 Oct 2026). Unknown keys fail (V10).
2. **Tables:** only inside the `## Stems` section (from that exact heading to the next line starting `## `). A stem starts at `^### ([a-z0-9]+(-[a-z0-9]+)*) · (.+)$` (middle dot U+00B7). A leaf row matches `^\| \`([a-z0-9]+(-[a-z0-9]+)*)\` \| (easy|medium|hard) \|`: first cell slug without `/`, second cell difficulty. Header and separator rows never start with a backtick. Only the first two cells are read, so `|` inside later cells cannot break it.
3. **`## Fruits`:** rows whose first cell is a full ID `^\| \`[a-z0-9-]+/[a-z0-9-]+/[a-z0-9-]+\` \|`; the set must equal the YAML `fruit: true` set.
4. **`## Later` and `## Roadmap changes`:** never read for leaves. `## Later` must have no line starting with `|` and no backticked slug (closes Q4); `## Roadmap changes` rows start with a date, not a backtick.
5. **V6 fails** when: stem order or stem `id` differs between YAML and headings; a leaf is missing, extra or reordered in a stem; the `Diff` cell differs from YAML `difficulty`; the heading title differs from the YAML stem `title`; the Fruits set differs.

### 3.2 Validation rules (task `validateContent`; every message is `V<n> <file>:<line> [<id>] <what>`; all violations are printed, max 50, then the task throws `GradleException`)

| Rule | Fails when |
|---|---|
| V1 | Bough/branch not in the fixed registry (`03` §1; titles from `00` §5), or branch under the wrong bough. The registry lives in `B/Registry.kt` only. |
| V2 | Slug fails `^[a-z0-9]+(-[a-z0-9]+)*$`, or duplicate stem or leaf slug in a branch. |
| V3 | `requires` names an unknown leaf, a leaf of another branch, or a leaf listed **later** (full IDs in YAML). |
| V4 | A leaf file without a roadmap row, or frontmatter `id` not equal to its path. |
| V5 | Leaf file `title`, `difficulty`, `requires`, `fruit` differ from the roadmap. |
| V6 | Section 3.1 step 5, plus the first block is not YAML. |
| V7 | A registry or content top-level folder is a reserved word (`03` §7.3) or starts with `_` (except `_templates`). Subsumed by V1 for unknown folders. |
| V8 | A leaf that was `status: live` at `-PbaselineRef` (default `origin/main`) is no longer live, or was removed or renamed. Git history, not a list file (D16): `git grep -lE '^status:[[:space:]]*live' <ref> -- 'content/*/*/*.md'` through `ProcessBuilder`. Ref or `git` missing (shallow clone): **warning, not failure**. The sitemap check of `07` §6.4 is a Phase 7 `deploy.yml` step and is separate. |
| V9 | More than one `upnext: true` in a branch, or `upnext: true` on a live leaf. |
| V10 | Frontmatter shape: missing required key (`00` §8.2), `status` not in `shadow|upnext|live`, live leaf without `summary`, `video` not empty and not an `https://www.youtube.com/watch?v=` or `https://youtu.be/` URL, dates not `YYYY-MM-DD`, unknown key in the roadmap YAML. |
| V11 | A stem or leaf slug is `index` or starts with a digit (`07` §6.2; P0-3a). No current slug fails (grep 20 Oct 2026). |

SIM and challenge rules (`07` §6.4, CH1-CH10) arrive with Phase 4 and Phase 6.

**Counts are a test, not a rule.** `docs/08` R5 lets M grow, so `validateContent` never hard-codes 482. `buildSrc` test `RealContentTest` asserts 482 leaves, 180 stems, 20 fruits, 17 branches and the per-branch table of `08` §9.0 on the real `/content`; a roadmap edit that changes M changes that test and `08` §9.0 in the same commit.

### 3.3 State rules (task `generateTreeData`, `B/TreeBuilder.kt`; `03` §3.1 and `07` §6.4 are the source)

- `live`: leaf file exists, `id` matches, `status: live`. Anything else in a file (`shadow`, `upnext`, draft) is **non-live and makes no page**.
- `upnext`: per branch exactly one, or none when every leaf is live. (1) the non-live leaf whose file has `upnext: true`; else (2) the first non-live leaf in YAML order. A file with `status: upnext` alone changes nothing.
- `shadow`: every other leaf. `path`, `summary`, `video`, `publishedOn` are `null` unless live; for a live leaf `video: ""` becomes `null` (L26).
- `order`: leaf 1-based across the branch (YAML order); stem 1-based in the branch; bough as `03` §4.2 (roots 0); **branch `order` = 0-based position in `00` §5 across all 17** (foundation 0, kotlin 1, tools 16), which is the `03` example. The tree layout sorts inside a bough, so geometry does not change.
- `counts` at every node (`live`, `total`); `startHere` = first live Foundation leaf, else the Foundation up-next leaf (the first Foundation leaf today).
- Day-one result (acceptance): `counts {live 0, total 482}`, 17 branches, 180 stems, 20 fruits, 17 `upNext`, 465 shadow.

### 3.4 Data shapes and wiring

**Outputs** (same in-memory model, both byte-deterministic: UTF-8 without BOM, `\n`, fixed key order = schema order, two-space JSON indent, no timestamps; non-ASCII written as `\uXXXX` in Kotlin; Kotlin strings escape `\`, `"`, `$`, newline):

| File | Path | Content |
|---|---|---|
| `GeneratedTreeData.kt` | `site/build/generated/kotree/kotlin/dev/kotree/site/tree/model/GeneratedTreeData.kt` | **Only** `val treeData: TreeData = TreeData(...)`, one private function per branch (17) so no single function is huge. Data classes stay hand-written in `K/tree/model/TreeData.kt` (reviewed, and a compile error appears if the generator drifts). This differs from `07` §6.4 ("data classes and literals", file `tree/TreeData.kt`): the name avoids a clash with the existing file. |
| `tree.json` | `site/build/tree.json` | `03` §4.2 schema, hand-written deterministic writer (no JSON library). Task `copyTreeJson` copies it to `site/.kobweb/site/tree.json` and runs **after** the export (CI step in Phase 0b/7; locally `../gradlew :site:copyTreeJson`). The app never fetches it. |

**Hand-written model (`K/tree/model/TreeData.kt`, extended, field names = `03` §4.2):** `TreeData(schema, seed, startHere, counts, boughs)`; `Branch(id, title, order, accent, counts, upNext: String?, stems)`; `Stem(id, slug, title, why, order, counts, leaves)`; `Leaf(id, slug, title, status: LeafStatus, difficulty: Difficulty, order, requires, fruit, path: String?, summary: String?, video: String?, publishedOn: String?)`. Enums `LeafStatus {SHADOW, UPNEXT, LIVE}` and `Difficulty {EASY, MEDIUM, HARD}`.

**Build-side contract (B/Model.kt, owned by S2-A, so S2-B codes against it in parallel):**
```kotlin
data class Violation(val rule: String, val file: String, val line: Int, val id: String?, val message: String)
fun loadContent(contentDir: File): Content                       // roadmaps + leaf files, parse errors become Violations
fun validate(c: Content, baselineLive: Set<String>?): List<Violation>
fun buildTree(c: Content): TreeModel                              // S2-B: states, order, counts, upNext (3.3)
```

**Gradle (`site/build.gradle.kts`, S2-B):** task classes live in `buildSrc` (plain JVM, SnakeYAML `org.yaml:snakeyaml:2.7`, versions written literally because `buildSrc` does not see the root catalog by default, ⚠️ VERIFY). `validateContent` is a dependency of `generateTreeData`; the generated folder joins the Kotlin source set with the pattern the markdown plugin itself uses (`07` §8): `kotlin.sourceSets.jsMain { kotlin.srcDir(generateTreeData.map { it.kotlinOut }) }` (⚠️ VERIFY it also wires the task dependency; fallback `dependsOn` on `compileKotlinJs` and the export task). Inputs: `@InputDirectory` `../content`. Tests: `buildSrc/src/test/kotlin` with fixtures in `buildSrc/src/test/resources/fixtures/<case>/` (differs from `07` §6.6 `site/src/test/fixtures/`: Kotlin/JS tests are unverified per `13`, JVM tests are not). Run `../gradlew -p ../buildSrc test` (⚠️ VERIFY on Gradle 9.6.1; the wrapper file says 9.6.1, `07` quotes 9.8.1).

### 3.5 Ownership (3 parallel runs)

| Run | Owner | Files |
|---|---|---|
| **A parse + validate** | `ui-engineer` | `B/Model.kt`, `B/Registry.kt`, `B/RoadmapParser.kt`, `B/LeafParser.kt`, `B/Validate.kt`, `B/Baseline.kt` (V8 git), `buildSrc/src/test/**` (one failing fixture per V1-V11, V6 sub-cases, V8 with a temp `git init` repo, `RealContentTest`) |
| **B emit + wire** | `ui-engineer` | `buildSrc/build.gradle.kts`, `B/TreeBuilder.kt`, `B/EmitKotlin.kt`, `B/EmitJson.kt`, `B/Tasks.kt`, `site/build.gradle.kts`, `K/tree/model/TreeData.kt`, `K/pages/Index.kt` (swap `sampleTree` for `treeData`, then delete `SampleTree.kt`). B commits last and runs the integration. |
| **C progress** | `housekeeper` | `scripts/progress.py` (stdlib; reads `site/build/tree.json`, writes `PROGRESS.md`: N of M, per branch, up-next list), `PROGRESS.md`, `scripts/verify-phase3.sh` (the checks of section 5) |

Rules: `Tokens.kt`, `Contracts.kt`, `tree/gen/*`, `tree/render/*`, `tree/camera/*` are read-only in S2. Commits: `feat: content parser and validators`, `feat: tree data generator`, `chore: progress script`.

**S2 exit gate:** `generate()` still ignores stems, so with the generated data the console geometry hash must equal `cecb906380417dd8` (this proves registry ids and order match the sample); `kobweb export --notty --layout static` exits 0; `gzip -9` of `kotree.js` recorded as `T = after - 277,917` (cap 60,000); `generateTreeData` run twice gives identical `sha256sum` for both outputs. If T > 60,000: slim the shadow leaf record (drop `title` and `requires` for shadow leaves) through a schema change and ADR, not by silently dropping fields.

ADR 003 (`docs/adr/003-content-pipeline.md`) is written by `architect` at S2 exit, with the deviations above.

## 4. S3 — The tree changes

### 4.1 What the visitor sees (`03` §2.3, D3)

Shadow leaves are never drawn (D3), so day one is deliberately sparse:

| Level | New in Phase 3 |
|---|---|
| Z0 Canopy, Z1 Bough | Unchanged. The z = 1 screenshot is identical to the Phase 1 "after" set. |
| Z1 at fully split (`z >= 2.4`, `p_drawn = 8`) | Gray stem twigs appear on each branch limb (no labels). Twigs are not drawn at partial split: they would detach from the moving limb. |
| Z2 Branch (`z >= 3.5`) | Stem titles for the **focused branch only** (shadow-stem text token), 180 stems in total, the largest are `dsa` 16 and `kotlin` 15. Hover or focus shows `Coming soon: <why>`. The branch's up-next leaf appears small with its glow. |
| Z3 Stem (`z >= 7.0`, leave `< 6.3`) | The focused stem's up-next leaf at full size, clickable; the stem hint stays on hover and focus. |
| Z4 Leaf (`z >= 14.0`, leave `< 12.6`) | The up-next leaf's title next to it. `zMax` rises from 4.0 to 24. |
| Click | Stem label: fly to the stem (fit, 12 % margin, `z >= 8.0`). Up-next leaf: popup "Coming next. The author is learning this now." (`03` §6.2, the existing `LeafPopup` component, Close only). The live-leaf popup is wired by data and untested until Phase 4. |

Result today: 180 gray twigs with names and 17 glowing leaves. A leaf is drawn and a DOM button created only for `live` and `upnext`; a leaf going live later changes only the build, never its position (leaf slot = index inside the stem, not the count of live leaves, D1).

### 4.2 Geometry contract (frozen in step 0 by the main agent or one `ui-engineer`, before the parallel runs)

`Contracts.kt` gains: `StemGeo(id, twig: Polyline, tip: Vec, labelAnchor: Vec, leaves: List<LeafGeo>, bounds: Bounds)`, `LeafGeo(id, at: Vec)` (a slot for **every** leaf, drawn only if live or up-next), `BranchGeo.stems: List<StemGeo>`, `BranchGeo.bounds`. Signature: `fun layoutStems(seed: Long, branch: Branch, geo: BranchGeo): List<StemGeo>`. Rules that make it deterministic: `Rng(seed, "stem:<id>")` and `Rng(seed, "leaf:<id>")` (keys are strings, so the existing limb streams are untouched); stems attach along the limb at parameters from 0.25 to 0.95 in order, alternating sides; leaf slots sit in a `ceil(sqrt(n))`-column grid at the twig tip with a fixed pitch. `TreeGeometry.bounds` keeps meaning "trunk, roots, limbs, labels" (not stems), so the whole-tree fit and the z = 1 view do not move.

`Tokens.kt` gains (names only, values chosen by the owner and then frozen): `Zoom.stemEnter 7.0 / stemLeave 6.3 / leafEnter 14.0 / leafLeave 12.6 / twigsOnStep 8`, `zMax 24.0`, `Gen.STEM_*`, `Gen.LEAF_PITCH`, `Gen.LEAF_SIZE`.

**Hash.** `GeometryHash.kt` keeps the old algorithm as `limbHash` (**must stay `cecb906380417dd8`**, proof that no limb moved) and adds `fullHash` over twigs and leaf slots. The console prints both; the new `fullHash` is recorded in `07` §8.1 and is the determinism value from now on (this is the by-design change). A two-run determinism check (generate twice, compare) is part of S3.

### 4.3 Ownership (step 0, then 3 parallel runs, then a join)

| Run | Owner | Files |
|---|---|---|
| **0 freeze** | main agent or one `ui-engineer` (about 30 min) | `K/tree/Contracts.kt`, `K/Tokens.kt`, stub `K/tree/gen/Stems.kt` |
| **A geometry** | `ui-engineer` | `K/tree/gen/Stems.kt`, `K/tree/gen/Layout.kt` (calls `layoutStems`, bounds unchanged), `K/tree/gen/GeometryHash.kt`, `K/tree/gen/LayoutCheck.kt` (pure report, section 5) |
| **B renderer** | `ui-engineer` | `K/tree/render/TwigRaster.kt` (new), `K/tree/render/Cache.kt`, `K/tree/render/PixelCanvas.kt`, `K/tree/render/LimbRaster.kt`. One lazily built bitmap per branch for twigs and leaf pixels (17 more, under the 36-bitmap budget style of `03` §10), blitted only at `p_drawn = 8`. `draw(...)` keeps its signature (new parameters optional). No re-rasterising per zoom level (still owed to the Phase 2 remainder); if twigs look chunky at z 8-24, report it, do not fix it here. |
| **C camera + overlay** | `ui-engineer` | `K/tree/camera/Camera.kt` (levels Z0-Z4 with hysteresis, `stemTarget`, `leafTarget`, focused branch, `zMax`), `K/tree/camera/Input.kt`, `K/tree/camera/Fly.kt` (only if limits need it), `K/tree/overlay/Labels.kt` (edit), `K/tree/overlay/StemLabels.kt` and `K/tree/overlay/LeafNodes.kt` (new). DOM buttons only for the focused branch's stems and for visible live/up-next leaves, culled to the viewport; stem labels reuse `LabelNudge`; the up-next glow is a DOM ring in the `leaf.upnext.glow` token, static under reduced motion. |
| **Join** | `ui-engineer`, then `reviewer`, then `fixer` | `K/tree/TreeView.kt` (wire layers, popup state, level switches), screenshots in `docs/reviews/phase3/`, `docs/reviews/P3-acceptance.md` |

Rules: no run edits another run's folder; a missing contract field goes to the main agent. **Owed to later phases, not done here:** breadcrumb, minimap, "Start here" trail, `Esc` stack, double-click zoom, deep-link hashes, checkmarks, live-leaf popup contents (Phase 2 remainder and Phase 4).

## 5. Acceptance (`reviewer` checks; commands in `scripts/verify-phase3.sh`)

- [ ] **Validators fail on seeded bad input.** One `buildSrc` test fixture per V1-V11 (V6 five sub-cases, V8 on a temp git repo) fails with `V<n> <file>:<line> [<id>]`. End to end: edit a real roadmap to forward-`requires`, run `../gradlew :site:validateContent` in `/site`: **exit non-zero**, message names the file and ID; then the export command of `07` §3: `.kobweb/site/index.html` is absent and the log contains the same message and `Failed to export` (the CLI exit code is not trusted, P3 probes). Revert; both succeed.
- [ ] **Counts match.** `tree.json`: `counts {live 0, total 482}`; 17 branches with the totals of `08` §9.0 (34, 65, 37, 49, 32, 33, 17, 22, 46, 22, 23, 21, 22, 16, 11, 15, 17); 180 stems; 20 fruits; 17 `upNext`; 465 shadow. `TreeData` holds the same values (a test compares both outputs field by field).
- [ ] **Deterministic.** Two runs of `generateTreeData`: equal `sha256sum` for both outputs; `fullHash` equal on two page loads.
- [ ] **No leaf published.** `git diff --name-only main...HEAD | grep -E '^content/[^_/]+/[^_/]+/[^_/]+\.md$'` is empty; `tree.json` has `live: 0`; `grep -rn addExtraRoute site/build.gradle.kts` is empty.
- [ ] **Limbs unmoved.** Console `limbHash` is `cecb906380417dd8`; z = 1 screenshot matches the Phase 1 "after" set.
- [ ] **Self-check `layoutReport`** (console, 1280 x 800): at each branch's landing z and at z = 7, every stem label and leaf hit area is at least 24 x 24 CSS px, none overlaps another, leaf gaps at z = 7 are at least 8 CSS px (`03` §2.6). If `dsa` or `kotlin` fails, raise that branch's landing z (allowed, `03` §11), never shrink the rule.
- [ ] **Performance.** 10 s wheel-zoom trace over z 1 to 24: p95 frame <= 16.7 ms; DOM nodes <= 200; `generate()` + `treeData` init <= 50 ms (`performance.now()` log); click to popup <= 100 ms.
- [ ] **Bundle recorded in `07` §8.1** (by `architect`): `M0` 277,917; after S2 (T); after S3; each as `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` against 400,000; plus the 50-leaf projection from S1. Fail line: T > 60,000 or S3 adds more than 15,000.
- [ ] **Screenshots reviewed** (headless Chrome as `docs/reviews/S3-acceptance.md`): `kotlin`, `dsa` (16 stems) and `foundation` at z = 2.4, 4, 8, 14, 24, plus reduced-motion; PASS in `P3-acceptance.md`.
- [ ] **Accessibility.** Tab reaches every stem of the focused branch and the up-next leaf in data order; accessible names `"<stem title>, coming soon"` and `"<leaf title>, coming next"`; visible focus ring; the glow is static under `prefers-reduced-motion`; text contrast >= 4.5 via `scripts/check-tokens.py`; `scripts/check-hardcoded.sh` exits 0; no dark-mode query.
- [ ] The export command of `07` §3 (old export deleted first) produces `.kobweb/site/index.html`, and its log has no `Failed to export` and no Kotlin warnings. `scripts/verify-phase3.sh` uses this check, never the exit code alone.

## 6. Risks

| Risk | Mitigation |
|---|---|
| P0-7 failed and the lesson design changed (ADR 005) | Phase 3 never touches lesson pages; ADR 005 gates only Phase 4. ADR 005's bundle projection (about 391 KB at 482 live) counts on T ≤ 40,000: if S2 measures T higher, report it before S3. |
| `kobweb export` exits 0 on a failed build | Delete the old export first; check `index.html` and the log (`07` §3). |
| A 482-leaf literal slows or breaks the Kotlin/JS compile | One private function per branch (17); if still slow, emit one compact string table parsed at start (schema unchanged). Measure compile time in S2. |
| `T` over 60 KB | Slim shadow record by ADR (section 3.5 exit gate). |
| 16 stems on one limb overlap | `layoutReport` gate; per-branch landing z; two-sided alternation. |
| SnakeYAML turns dates and booleans into typed values | `compose` + string scalars only (section 3.1). |
| Windows default charset corrupts `·` or non-ASCII | Every read and write names UTF-8; non-ASCII escaped in Kotlin output. |
| Dev server misses a new leaf file | Restart `kobweb run` (`07` §6.6). |

## 7. Follow-ups for other owners (not edited here)

`curriculum-designer`: `08` §15 Q1 and Q4 closed by section 3.1; V11 means no future slug may be `index` or start with a digit. `architect` after S1 (**done 20 Oct 2026**): `11` DONE marks for P0-3, P0-4, P0-7, U4, U5; `03` §7.2/§11 and `07` §6.2 VERIFY removal; `07` §8.1 spike numbers; ADR 005. Still owed after S2: `07` §6.4 deviation (generated file name; fixtures path already updated in `07` §6.6); `07` §8.1 T and S3 numbers; ADR 003. `07` §4 (1) VERIFY is closed by P0-4 PASS; its (2) half stays for P7-2. Owners of `04`, `05`, `06`: align text that names `syncLessons`, `{{{ }}}` lines, `ChallengeWidgets.kt` or the `code` handler with ADR 005. `docs/08` §9.0 stays the count source.

## 8. Open questions for the author (max 3)

1. **Sparse deep zoom.** Because D3 hides shadow leaves, Z3 and Z4 show only gray stem names and 17 glowing up-next leaves on day one. Keep D3 (default), or amend it so a stem also draws faint shadow-leaf dots (a visual hint of how much is coming, still unclickable)?
2. **If the spike fails (expected).** Accept that lessons stop being Kobweb-compiled Markdown pages and become prerendered static pages fetched by one lesson page (ADR 005 candidate A, default), or keep Markdown pages and publish only about 20 leaves before re-deciding?
(`syncLessons` and `verifyLessonExport` moving to Phase 4, section 1, is a scope change for the orchestrator, not a question for the author.)

### Author and orchestrator answers (20 Oct 2026)

1. Shadow leaves: **keep D3** (never drawn; the tree grows as the author learns). No spec change.
2. If the 50-leaf spike fails the budget: **use pre-rendered lesson pages (ADR 005 option A)**; write ADR 005 and update docs/07 and docs/04 then.
3. Scope change confirmed by the orchestrator: `syncLessons` and `verifyLessonExport` move to Phase 4 (no live lessons exist yet).
4. Git: work on `main` with small commits made by the orchestrator; agents do not branch or commit (the plan's branch names are advisory).
