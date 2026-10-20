# ADR 005 — Lesson content delivery: one lesson page, per-leaf JSON, per-leaf exported HTML

Status: accepted · Date: 20 Oct 2026 · Decided by: the author (option A approved 20 Oct 2026, `docs/14` §8 answer 2), specified by `architect`
Supersedes: the "one Kobweb Markdown page per live leaf" design of `docs/07` §6.2, §6.2.1 (steps 3-5), §6.3 and §6.7 (v2.4), and the planned ADR 004 (challenge widgets generated as Kotlin).

## Context

- P0-7 (`docs/reviews/P3-probes.md` Step 2): a lesson compiled by the Kobweb markdown plugin costs **4,190 B gzip** of the one main bundle `kotree.js` that every page loads. 482 leaves project to **2,337,690 B** against the 400,000 B budget (`docs/07` §8); only **19** leaves fit. Dropping `/kitchen-sink` buys about 7.
- P0-3a: the plugin does not sanitise `-` in file names (`fun What-suspend-meansPage` does not compile); P0-3b: the export writes `<route>.html`; U5: `{{{ Widget("arg") }}}` works.
- Kobweb facts used here (verified 20 Oct 2026, source at tag `v0.25.1` and the Kobweb docs):
  - F1. The export skips dynamic routes (`.filter { !it.contains('{') }`) and adds `exportBlock.extraRoutes`; `addExtraRoute(route: String, exportPath: String? = null)` exports one instance (`KobwebExportTask.kt`, `AppBlock.kt`, exporting docs).
  - F2. The export snapshots each page with Playwright `navigate(url?_kobwebIsExporting=true...)` with **no extra wait**, then saves `content()` of the whole document. So content fetched after the load event is **not** in the snapshot (`KobwebExportTask.kt`).
  - F3. On start the generated `main` **removes every child of `#_kobweb-root`** ("poor man's hydration"), then renders, routing by `window.location` (`.html` suffix removed) (`templates/MainTemplate.kt`). Nodes outside `#_kobweb-root` survive.
  - F4. `AppGlobals.isExporting` tells code it runs inside the export (exporting docs).
  - F5. Compose HTML `attrs { ref { el -> ...; onDispose { } } }` gives the DOM element once when it enters the composition (Compose HTML "Using Effects" tutorial).
- Constraints: no login, static site on Vercel (`cleanUrls: true`, `trailingSlash: false`), a real URL per live lesson, author Markdown in `/content`, existing `ui/lesson/*` components (`docs/02` §6), `SimFrame`/`SimHost`, Playground editor and challenge cards (`docs/06`), shadow leaves never get a page (D3, D16).

## Decision

**Lesson text and challenge data leave the Kotlin bundle.** The build renders each live leaf to a JSON block list on the JVM (no Markdown or YAML parser in the browser). One Kotlin lesson page renders any leaf from that JSON with the existing components. The export writes one HTML file per live leaf from that page, and a post-export step bakes the leaf's head tags, a static copy of its prose and its JSON into that file.

### 1. What the build emits per live leaf (`<id>` = `<branch>/<stem>/<leaf>`)

| Output | Path | Made by | Content |
|---|---|---|---|
| Lesson JSON | `site/build/generated/kotree/resources/public/lessons/<id>.json` → exported to `/lessons/<id>.json` | `renderLessons` (JVM, `buildSrc`) | Schema below. Served by the dev server and the export as a public resource. |
| Static body | `site/build/kotree/lessons-static/<id>.html` | `renderLessons` | The `html` blocks joined, plus each `code`/`predict`/`fix` code as an escaped `<pre><code>`. No answers, hints or `why` text (no spoilers before JS). |
| Lesson page | `site/.kobweb/site/<id>.html` | `kobweb export` via `addExtraRoute(route, exportPath = "<id>.html")`, then `emitLessonPages` | Kobweb's snapshot of the lesson page (real header, breadcrumb, layout), with: `<title>`, `<meta name="description">` (= `summary`), `<link rel="canonical">` and `og:` tags from `SITE_ORIGIN`; the static body inside `#kt-lesson-body`; `<script type="application/json" id="kt-lesson-data">` (the same JSON, `<` written as `<`) appended to `<head>`. |

Once per build: `LessonConfig.kt` (generated Kotlin: `const val LESSON_KOTLIN_VERSION`), `sitemap.xml` (`/` plus every live lesson), `<meta name="robots" content="noindex">` added to `404.html`.

**Lesson JSON, schema 1** (deterministic writer as in `docs/14` §3.4: fixed key order, `\n`, no timestamps):

```json
{
  "schema": 1,
  "id": "kotlin/null-safety/what-is-null",
  "playground": true,
  "sims": ["null-lunch-box"],
  "blocks": [
    { "t": "html", "html": "<h2>The question</h2><p>...</p>" },
    { "t": "sim", "id": "null-lunch-box" },
    { "t": "code", "lang": "kotlin", "code": "fun main() { ... }", "runnable": true },
    { "t": "predict", "id": "nullable-length", "code": "...", "run": true, "verified": "kotlin-2.4.21", "generated": false,
      "options": [{ "id": "a", "text": "...", "why": "...", "correct": true }], "output": "..." },
    { "t": "fix", "id": "...", "code": "...", "verified": "kotlin-2.4.21", "generated": false,
      "hints": ["...", "..."], "expected": "...", "fix": "...", "explanation": "..." }
  ]
}
```

- Challenge field names are exactly `docs/06` §3; `runnable` follows the `docs/06` §1 rule. Title, difficulty, summary, video, breadcrumb, previous/next come from the compiled `TreeData` (already in the bundle), never from the JSON.
- Splitting (replaces R1/R2): `renderLessons` cuts the body at an exact SIM marker line (`docs/07` §6.2 R1 regex) → `sim`; a ```` ```challenge ```` block (CH1-CH10 already passed) → `predict`/`fix`; a ```` ```kotlin ```` fence → `code`; everything between → one `html` block, rendered by commonmark-java with the GFM tables extension and `escapeHtml(true)` (raw HTML is escaped, so `innerHTML` holds only Markdown output). ⚠️ VERIFY at implementation: current versions of `org.commonmark:commonmark`, `org.commonmark:commonmark-ext-gfm-tables`, `org.jsoup:jsoup` on Maven Central; written literally in `buildSrc/build.gradle.kts`.

### 2. How the page renders and hydrates

- `K/pages/Lesson.kt`: `@Page("/{branch}/{stem}/{leaf}")`, params read with `ctx.route.params.getValue(...)` (`docs/03` §7.2). Static routes (`/`, `/404`, `/kitchen-sink`) have other segment counts.
- Id not `LIVE` in `TreeData` → the shared shadow stub / "Not found" content of `docs/03` §7.4 (same composable as `NotFound`).
- Live id → layout from `TreeData` at once; body source, in order: (1) `#kt-lesson-data` in the document whose `id` matches (direct load: synchronous, so the F3 clear-and-render happens in one task with the same text: no flash); (2) else `fetch("/lessons/<id>.json")` (in-app navigation from the tree, and the dev server); while loading, a body placeholder with reserved height; on failure, "This lesson could not load." plus a Retry button. Parsing is `JSON.parse` into `external interface` types (no `kotlinx.serialization`).
- During export (`AppGlobals.isExporting`, F4) the page renders `#kt-lesson-body` empty and does **not** fetch, so the snapshot is deterministic (F2); `emitLessonPages` fills it.
- Blocks, in one composition (Silk theme and tokens intact): `html` → `Div` with class `LessonProseStyle` (Silk `CssStyle`, tokens only) whose `ref` sets `innerHTML` once (F5), keyed by leaf id and block index; `code` → `EditorShell` when `runnable` and `playground`, else `CodeBlock`; `predict` → `PredictOutput`; `fix` → `FixBug`; `sim` → `SimFrame` hosting `SimHost(id)` (placeholder frame until Phase 5). The Playground script loads on demand as `docs/06` §2.1 says (JSON `playground`); `data-version` = `LESSON_KOTLIN_VERSION`.
- Links inside `html` blocks are plain `<a href="/...">`: a full page load of an exported page (no router interception; acceptable).

### 3. Export, Vercel, SEO

- `site/build.gradle.kts`: `kobweb { app { export { liveRoutes.forEach { addExtraRoute(it, exportPath = it.removePrefix("/") + ".html") } } } }`. `liveRoutes` = `providers.of(LiveLessonRoutes::class) { ... }.get()`, a Gradle `ValueSource` in `buildSrc` that reuses `LeafParser` (one parser, no drift). Gradle tracks file **contents** read at configuration time but documents nothing about directory listings, and `gradle.properties` has `org.gradle.configuration-cache=true`, so a `ValueSource` (re-checked each build) is used. ⚠️ VERIFY (`docs/11` OD-3): export, add a new live leaf file, export again: the second export has its HTML.
- `cleanUrls` + `trailingSlash: false` are unchanged; the file shape `<route>.html` is P0-3b. `vercel.json` gains a header rule `/lessons/(.*)` → `X-Robots-Tag: noindex` (the JSON is data, the page is canonical).
- Shadow and up-next leaves: no JSON, no HTML, no sitemap entry, no route string. A direct request gets Vercel's `404.html` with status 404; the client router then matches the dynamic lesson route by `window.location` (F3) and shows the stub. ⚠️ VERIFY (`docs/11` P4-1, P7-2).
- `404.html` and `/lessons/*` are `noindex`; lesson pages carry a canonical URL; `sitemap.xml` lists only live lessons and `/`.

### 4. How a leaf goes live (data flow)

1. `housekeeper` sets `status: live` after `reviewer` PASS (hard rule 4).
2. Configuration: `LiveLessonRoutes` returns the route → one `addExtraRoute`.
3. `validateContent` → `generateTreeData` (`TreeData` has `LIVE`, `summary`, `video`; the tree lights the leaf, the popup's "Open lesson" works) and `renderLessons` (JSON, static body, `LessonConfig.kt`).
4. `kobweb export` snapshots `/<id>` with an empty body.
5. CI: export success check, `copyTreeJson`, `emitLessonPages`, `verifyLessonExport`, deploy (`docs/07` §5.4).

## Cost per leaf (budget: main JS ≤ 400,000 B gzip)

| Item | Per live leaf | At 482 live |
|---|---|---|
| Lesson text + challenge data in `kotree.js` | **0 B** | 0 B |
| Live-only `TreeData` fields (`summary`, `video`, `publishedOn`, `path`) | ≤ 120 B gzip (estimate; measure at the pilot) | ≤ 57,840 B |
| Fixed lesson runtime (page, block renderer, prose style, JSON source), once | — | ≤ 15,000 B (fail line 20,000) |
| Projection: 277,917 (M0) + 40,000 (T reserve) + 15,000 + 57,840 | | **390,757 B** (under 400,000; thin) |
| Off-bundle: `/lessons/<id>.json` | ≈ 4.2 KB gzip, one request only on in-app navigation | — |
| Off-bundle: `<id>.html` (snapshot + static body + inline JSON) | target ≤ 30 KB gzip, ≤ 130 KB raw (measure at the pilot) | ≤ 63 MB of the 100 MB export budget |
| Build: one Playwright snapshot | about 1 s, parallel threads | minutes; CI ≤ 10 min watched |

Levers if the measured projection exceeds 380,000 B, in order: drop `/kitchen-sink` from production (−30 KB); derive `path` from `id` instead of storing it (`docs/03` §4.2 change); move `summary` out of `TreeData` into the lesson JSON (popup reads it after a fetch; needs a `docs/03` §6.1 change and an ADR note).

## Rejected options

| Option | Why rejected |
|---|---|
| Keep one Kobweb Markdown page per leaf | 4,190 B per leaf in the shared bundle; 19 leaves fit. |
| Same, publish about 20 leaves, re-decide later | The author chose A; the tree must grow to 482. |
| (B) Second Kotlin/JS bundle loaded on demand | No documented Kobweb support for code splitting. |
| (C) Raise the budget | c = 4.19 KB, far above the 1 KB threshold of `docs/14` §2.2; 2.3 MB of JS on the home page. |
| HTML fragment fetched and set as `innerHTML`, widgets mounted into placeholders with separate `renderComposable` calls | Several compositions lose Silk theme and locals; widget data would still need a format. The JSON block list keeps one composition. |
| Lesson pages written entirely by a JVM template (no Kobweb snapshot) | Header, breadcrumb and layout would exist twice (Kotlin and template) and drift. |
| One shell for all lessons via a Vercel rewrite | Soft 200 for shadow leaves, no per-page title or canonical, rewrite list churn. |
| Inline JSON only, no `/lessons/*.json` | In-app navigation and `kobweb run` load no new HTML, so they need a fetchable file. |
| Fetch only, no inline JSON | Direct loads would clear the static body (F3) and wait for a fetch: flash and CLS. |
| Markdown or YAML parsed in the browser | Parser in the bundle. |

## Consequences

- `syncLessons`, R1/R2 Markdown rewrites, `ChallengeWidgets.kt`, the Kobweb markdown configuration (`addSource`, `imports`, `defaultLayout`, `process`) and the P0-3a rename + `routeOverride` fallback are **not used for lessons**. If no other page uses Markdown, Phase 4 removes the `kobwebx.markdown` plugin and library and records the bundle delta.
- `addExtraRoute` is now expected in `site/build.gradle.kts`, but only inside the `liveRoutes` loop.
- CH1-CH10 and the SIM rules stay in `validateContent` (they check the source). New rules: V11 (slug is `index` or starts with a digit) and V12 (raw HTML in a live leaf body other than the exact SIM marker line).
- `kobweb export` can exit 0 on a failed build (P3 probes): CI deletes `site/.kobweb/site` first, then fails if `index.html` is missing or the log contains `Failed to export`.
- Phase 4 owns: `renderLessons`, `emitLessonPages`, `verifyLessonExport` (extended), `LiveLessonRoutes`, the lesson page, `NotFound`, the pilot leaf. Phase 6 adds Playground wiring and challenge state to the same blocks.
- A layout change rewrites every lesson HTML file (Vercel uploads per day, `docs/07` §5.6, P7-3).
- `docs/04` and `docs/06` text that names `syncLessons`, `{{{ }}}` or the `code` handler is out of date and is aligned by their owners.

## Tests (Phase 4 acceptance; checked by `verifyLessonExport` and `reviewer`)

- [ ] Pilot live: `site/.kobweb/site/<id>.html` exists, has exactly one `script#kt-lesson-data` whose `id` is the leaf, a non-empty `#kt-lesson-body`, the canonical URL and the summary as description; `/lessons/<id>.json` exists with `"schema": 1`; no JSON or HTML for any non-live leaf; `sitemap.xml` = `/` + live set.
- [ ] `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` grows by ≤ 20,000 B with the lesson runtime and does not grow (± 200 B beyond the `TreeData` fields) when a second synthetic live leaf is added on a scratch copy.
- [ ] Direct load: no request to `/lessons/*.json`; in-app "Open lesson": exactly one; with the JSON request blocked, the Retry message shows.
- [ ] `grep -c "kt-lesson" site/.kobweb/site/index.html` is 0 (nothing lesson-specific leaks into other pages).
- [ ] Shadow leaf URL: HTTP 404, stub visible, no console error (P4-1, P7-2).
