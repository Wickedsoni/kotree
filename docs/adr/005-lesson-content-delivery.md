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

## Phase 4 addendum (21 Oct 2026): what was built, gaps closed

Built: `renderLessons`, `emitLessonPages`, `verifyLessonExport`, `LiveLessonRoutes` (buildSrc `LessonRenderer.kt`, `LessonTasks.kt`, with tests), the lesson page (`ui/lesson/LessonView.kt`, `lessons/LessonData.kt`, `lessons/LessonLookup.kt`, `ui/lesson/Prose.kt`), `pages/Lesson.kt`, `pages/LessonPreview.kt`, `pages/NotFound.kt`. No leaf is live. Facts changed or found while building:

- **Budget.** L30 (21 Oct 2026) raised the `kotree.js` budget to 600 KB gzip. The 400 KB figure, the 380 KB lever point and the "drop `/kitchen-sink`" lever of this ADR are superseded. Measured numbers are in `docs/07` §8.1.
- **jsoup not used.** The Kobweb snapshot is pretty-printed with attributes in alphabetical order (`<meta content=".." name="description">`, seen in a real export), so `emitLessonPages` edits it with attribute-order-free regexes on tags it controls (`#kt-lesson-body`, `<title>`, description, robots). `verifyLessonExport` checks the result, so a changed snapshot shape fails the build. Only commonmark 0.30.1 and its GFM tables extension were added (Maven Central latest, 21 Oct 2026).
- **`AppGlobals.isExporting` does not exist in 0.25.1** (not found in `kobweb-core`). The page reads the `_kobwebIsExporting` query parameter the export adds (ADR fact F2 names it). Verified by a real export.
- **JDK.** The export and `kobweb` CLI 0.9.23 worked with the JetBrains Runtime 25 on the author's machine, not only JDK 17.
- **Dev server answers 200 with `index.html` for a missing JSON.** The lesson page treats any reply that is not a schema 1 lesson with the right `id` as a failure, so Retry shows (tested with the request blocked, and with a missing file).
- **Preview.** `/preview/<branch>/<stem>/<leaf>` reads `/lessons-preview/<id>.json`. That folder is written only when `renderLessons` runs with preview on: automatic for `kobwebStart` and `kobweb run`, `-Pkotree.preview=true|false` overrides, and any task whose name contains `export` forces it off. `emitLessonPages` deletes `lessons-preview` from the export and `verifyLessonExport` fails if it is there. The page adds `<meta name="robots" content="noindex">` at run time, shows a banner and the author comments of the file ("Author notes"), and nothing links to it. `preview` and `lessons-preview` are reserved first segments (V7, `docs/03` §7.3).
- **Drafts.** Production renders only `status: live` leaves; a broken draft cannot fail a production build (preview off). With preview on, a broken draft fails with `file:line [id] rule message`.
- **Challenge rules.** CH1-CH5, CH8, CH10 style checks live in `renderLesson` (rule codes in the message); CH7, CH9 and the version-newer check are not implemented yet. YAML note: `verified: "UNVERIFIED: reason"` must be quoted (a bare `: ` is a YAML error, CH2).

### Gaps closed (docs/NEXT-STEPS step 1)

| Gap | Decision | Why | Phase 6 must |
|---|---|---|---|
| 1. How `runnable` is set for a plain `kotlin` fence | A fence is read-only by default. Only ```` ```kotlin run ```` is runnable, and only on a `playground: true` leaf (renderer error otherwise). `challenge` blocks follow `docs/06` §1 (`run` true or absent = runnable). | `docs/04` §4.4 says Name-it code is read-only and Try-it code is runnable; a flag on the fence is explicit, visible in the Markdown and needs no guessing. | Add `kotlin run` to the author notes in `docs/04` §4.6 (Try it) and the lesson template. |
| 2. EditorShell to Playground hand-off | Phase 4 renders runnable code in `EditorShell` with a disabled Run button and the text "Running code is not switched on yet." (`runEnabled`, `runHint` parameters), Predict "Run it to check" also reveals stored answers. | A Run button that does nothing would lie. The frame, the `playground` flag and `LESSON_KOTLIN_VERSION` (`LessonConfig.kt`) are already in place. | Load the Playground script on demand, run `fetch` of the versions list with the 5 s timeout of `docs/06` §2.3, mount it in the `content` slot, set `runEnabled = true`. No leaf with a runnable block goes live before this. |
| 3. Where challenge state lives | In the card composables (`remember`), keyed by the stable `id`; nothing is stored in Phase 4. `PredictOutput` and `FixBug` already get `id` and are re-created per leaf and block index. | State must not leak between leaves, and the storage key of `docs/06` §3.4 (`kotree.v1.challenge.<leafId>.<challengeId>`) needs the leaf id, which the lesson page has. | Wrap the cards with a small store that reads and writes that key (try/catch around local storage). The leaf id is `LessonData.id`. |
| 4. SimFrame versus SimHost | `SimFrame` stays the chrome (frame, Play, Reset). `SimHost(id)` in `LessonView.kt` looks the id up in a registry (empty until Phase 5) and puts the sim in the frame's content slot. An unregistered sim draws nothing on a live page (the "Try this" text still teaches, `docs/04` §4.3) and a labelled frame in a preview. | A live page must never show "coming soon" (`docs/04` §4.3), and a preview must show where the sim goes. | Register each sim in `simRegistry`; wire Play and Reset to the engine. |
| 5. The `VERIFY` comment versus V12 | The comment stays blocking: V12 rejects any raw HTML, including `<!-- ⚠️ VERIFY ... -->`, in a live leaf, so a leaf cannot go live with an open VERIFY. `housekeeper` deletes the comment after `reviewer` PASS (add this to the publish checklist). Drafts keep their comment: the renderer drops whole-line comments from the prose and the preview shows them as "Author notes". | One rule, no extra validator, and the open question is visible to the author while reading the draft. | Nothing. |

### Handoffs
- **Phase 7 owns `vercel.json`** (not created here): `cleanUrls`, `trailingSlash: false`, the `X-Robots-Tag: noindex` rule for `/lessons/(.*)` and `/lessons-preview/(.*)`. The HTTP 404 status for a shadow URL (`docs/11` P4-1, P7-2) can only be checked on Vercel.
- Not built: "I finished this" toggle, video section (a live leaf with a `video` shows the header button only), per-leaf `X-Robots-Tag`, challenge persistence, Playground.
- CI order after the export: `copyTreeJson`, `emitLessonPages`, `verifyLessonExport` (`docs/07` §5.4).
