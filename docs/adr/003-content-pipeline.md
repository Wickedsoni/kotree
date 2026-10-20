# ADR 003 — Content pipeline: roadmaps read at build time, validators, generated tree data

Status: accepted · Date: 20 Oct 2026 (written at Phase 3 S2 exit, recorded after S3) · Decided by: `architect` per `docs/07` §6.4 and `docs/14` §3, built in Phase 3 S2
Related: ADR 005 (lesson delivery: lesson text never enters the bundle; this ADR covers only the tree data and the validators).

## Context

- Content lives outside `site/`: 17 `content/<branch>/_roadmap.md` files (canonical tree structure, hard rule 5) and, later, leaf files `content/<branch>/<stem>/<leaf>.md`. Live state = a leaf file with `status: live` (D16); up-next is computed with an optional `upnext: true` override (D15).
- The app must draw the whole tree (482 leaves, 180 stems, 17 branches, 20 fruits on 20 Oct 2026) with no runtime fetch and no `kotlinx.serialization` in the one Kotlin/JS bundle (budget 400,000 B gzip, `docs/07` §8).
- Tools (`housekeeper`'s `PROGRESS.md`, scripts, agents) need the same data without parsing Kotlin.
- Bad content must fail the build with a message that names the file, the line and the ID.

## Decision

1. **Build code in `buildSrc`** (plain JVM, package `dev.kotree.build`, SnakeYAML `org.yaml:snakeyaml:2.7` written literally because `buildSrc` does not see the root version catalog). Files: `Model.kt` (contract: `Violation`, `loadContent`, `validate`, `buildTree`), `Registry.kt` (fixed boughs and branches, the only copy), `RoadmapParser.kt`, `LeafParser.kt`, `Validate.kt`, `Baseline.kt` (V8 git lookup), `TreeBuilder.kt`, `EmitKotlin.kt`, `EmitJson.kt`, `Tasks.kt`.
2. **Parser** (`docs/14` §3.1): each roadmap read as UTF-8 (`\r\n` accepted). The first fenced block must be YAML; it is parsed with SnakeYAML `compose` (node tree) and every scalar is kept as a string, so dates and booleans never become typed values; node start lines feed the messages. Stem headings and leaf rows are read only inside `## Stems` with fixed regexes, only the first two cells; `## Fruits` rows are full IDs; `## Later` and `## Roadmap changes` are never read for leaves.
3. **Validators V1–V12**, task `validateContent`. Every message is `V<n> <file>:<line> [<id>] <what>`; all violations are printed (max 50), then the task throws `GradleException`.

   | Rule | Fails when |
   |---|---|
   | V1 | Bough/branch not in the registry, or under the wrong bough |
   | V2 | Bad slug, or duplicate stem/leaf slug in a branch |
   | V3 | `requires` names an unknown, cross-branch or later leaf |
   | V4 | Leaf file without a roadmap row, or frontmatter `id` not equal to its path |
   | V5 | Leaf file `title`, `difficulty`, `requires`, `fruit` differ from the roadmap |
   | V6 | YAML block and `## Stems` / `## Fruits` tables disagree, or the first block is not YAML |
   | V7 | Reserved or `_`-prefixed top-level folder (except `_templates`) |
   | V8 | A leaf live at `-PbaselineRef` (default `origin/main`) is no longer live, removed or renamed; read from git history. Missing ref or `git` (shallow clone): **warning only** |
   | V9 | More than one `upnext: true` in a branch, or `upnext: true` on a live leaf |
   | V10 | Frontmatter shape (required keys, `status` values, live leaf without `summary`, bad `video` URL, bad dates) or an unknown roadmap YAML key |
   | V11 | Stem or leaf slug is `index` or starts with a digit (Kobweb route and file-name limits, P0-3a) |
   | V12 | Raw HTML in a live leaf body other than the exact SIM marker line (from ADR 005; fenced and inline code ignored) |

   Counts are a test, not a rule: `RealContentTest` asserts 482 / 180 / 20 / 17 and the `docs/08` §9.0 per-branch table, so a roadmap edit that changes them changes that test and `docs/08` §9.0 in the same commit. SIM, `SimRegistry` and CH1–CH10 rules arrive with Phases 4 to 6.
4. **Generator**, task `generateTreeData` (`dependsOn(validateContent)`), one in-memory model, two outputs, byte-deterministic (UTF-8 without BOM, `\n`, schema key order, two-space JSON indent, no timestamps, non-ASCII escaped in Kotlin):
   - `site/build/generated/kotree/kotlin/dev/kotree/site/tree/model/GeneratedTreeData.kt`: only `val treeData: TreeData = TreeData(...)` and one private function per branch (17), added to `jsMain` with `kotlin.srcDir(generateTreeData.flatMap { it.kotlinOut })`. The data classes and enums (`TreeData`, `Branch`, `Stem`, `Leaf`, `LeafStatus`, `Difficulty`; field names = `docs/03` §4.2) stay hand-written in `site/src/jsMain/kotlin/dev/kotree/site/tree/model/TreeData.kt`.
   - `site/build/tree.json` (`docs/03` §4.2 schema), hand-written writer; `copyTreeJson` copies it into `site/.kobweb/site/` after the export. The app never fetches it.
   - State rules as `docs/14` §3.3: shadow fields `path`, `summary`, `video`, `publishedOn` are `null`; branch `order` is the 0-based `docs/00` §5 position.
5. **Tests:** JVM tests in `buildSrc/src/test/kotlin` (`ValidateTest`, `BaselineTest` with a temp git repo, `RealContentTest`), run with `./gradlew -p buildSrc test`; 50 tests, 0 failures at S2 exit.

## Deviations from the plan and from `docs/07`

| Planned | Built | Why |
|---|---|---|
| `docs/07` §6.4: file `tree/TreeData.kt` with data classes and literals | `tree/model/GeneratedTreeData.kt`, literals only | Name clash with the hand-written model; the hand-written classes make generator drift a compile error |
| `docs/07` §6.4: `tree.json` via a JSON library | Hand-written deterministic writer | No extra build dependency; key order and bytes fully controlled |
| `docs/07` §6.4: renamed-slug check through the live sitemap | V8 on git history; sitemap check stays a Phase 7 `deploy.yml` step | Works before the first deploy and offline |
| `docs/07` §6.6: fixtures in `site/src/test/fixtures/` | `buildSrc` JVM tests | Kotlin/JS tests unverified (`docs/13`); JVM tests run (path already updated in `docs/07` §6.6) |
| `docs/14` §3.2: V1–V11 | V1–V12 | V12 added by ADR 005 |
| `docs/07` §6.2: `syncLessons`, `verifyLessonExport` in Phase 3 | Moved to Phase 4 (as `renderLessons`, ADR 005) | No live lessons yet (orchestrator, `docs/14` §8 answer 3) |
| `docs/14` §3.4/§3.5: one fixture folder per failing case (`fixtures/<case>/`) | One good fixture (`buildSrc/src/test/resources/fixtures/good/`), copied to a temp dir and mutated per test by `Fx.kt` | Fewer files; each failing case is still its own test |

## Rejected options

| Option | Why rejected |
|---|---|
| App fetches `tree.json` at runtime | Extra request and a JSON parser or `kotlinx.serialization` in the bundle |
| Hand-written tree data | Drifts from the roadmaps |
| Fully generated data classes | Drift is silent; the class file shows up in no review |
| Two generator tasks (Kotlin and JSON) | The outputs could drift |
| Extra Kobweb resource directory so `tree.json` lands in the export | Unverified export behaviour; the copy step needs none |
| One huge literal function | Risk of slow or failed Kotlin/JS compiles; one function per branch instead |
| Hard-coding 482 in `validateContent` | `docs/08` R5 lets the count grow; counts live in a test |

## Consequences

- Bundle: the generated tree data cost T = +35,131 B gzip (cap 60,000; ADR 005 assumed ≤ 40,000). S3 drawing code added +10,318 B; `kotree.js` is 323,366 B. Numbers and the re-based ADR 005 projection (about 396 KB) are in `docs/07` §8.1. If the tree data must shrink, the shadow-leaf record is slimmed by schema change and ADR, never by silently dropping fields.
- Determinism: `limbHash` `cecb906380417dd8` proved registry IDs and order match the old sample data; `fullHash` is recorded in `docs/07` §8.1 (final value in `docs/reviews/P3-acceptance.md`).
- Any roadmap edit runs through these validators on the next build; a new rule is a new `V<n>` row here and in `docs/14` §3.2, with a failing test.
- Phase 4 reuses `LeafParser` for `renderLessons` and `LiveLessonRoutes` (ADR 005), so there is one parser for leaf files.
- Not yet verified end to end: a seeded bad roadmap failing `:site:validateContent` and the export log (`P3-acceptance.md`, "Not verified"). Do it on a scratch copy, never on a canonical roadmap.
