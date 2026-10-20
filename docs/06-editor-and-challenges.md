# 06 — Editor and Challenges

> **Purpose:** how runnable Kotlin, Predict-the-output and Fix-the-bug work on lesson pages, how their data is written in Markdown (the fenced `challenge` block), and how every expected output is verified.
> **Read this when:** writing or reviewing challenges, building the Playground block or challenge cards, or deciding what a lesson may promise about Android behaviour.
> **Depends on:** `00` (D8, D9, L15, §10), `01` §3.4-3.5, `04` (section names, written content of challenges), `05` (Sims), `07` §6.2 and §6.2.1 (`renderLessons`, challenge validation CH1-CH10) and `docs/adr/005-lesson-content-delivery.md` (how the lesson page renders the blocks).
> **Version:** v1 · aligned with ADR 005 on 20 Oct 2026 (§3 delivery, E3, E6) · amended 18 Oct 2026 (challenge format is the only one; L15 conflict banner removed because `docs/00` is amended; Kotlin 2.4.21 pinned).

**Android scope (L15 amended, D8).** There is no Android emulator and no Android simulator in the browser. Android behaviour is shown through reference videos, animations and Sims (`docs/05`) that show how behaviour changes when the code changes. This document promises **real Kotlin execution only**.

---

## 1. What the editor is, and is not

| It is | It is not |
|---|---|
| A box where the learner edits and runs plain Kotlin (JVM, `fun main()`) | An Android emulator or simulator |
| The place for Predict-the-output "Run it to check" and Fix-the-bug | A place to run Compose, `Activity`, `Context` or any Android class |
| Online-only (D8) | Available offline |

**Rule for Android topics.** A leaf about lifecycle, Compose recomposition, permissions etc. shows behaviour with a reference video or a Sim. Any runnable Kotlin on that leaf is a *plain-Kotlin model* of the idea (for example, a `fun main()` that prints "onCreate, onStart, onResume" from a hand-written class). Label such blocks "A model in plain Kotlin, not real Android". Acceptance: no challenge imports `android.*` or `androidx.*`.

**What "runnable" means (used by the `playground` frontmatter key, `docs/04` §2).** A *runnable block* is Kotlin that the page loads into the Playground so the learner can press Run. Exactly these are runnable:

| Block | Runnable? |
|---|---|
| `code` of a `challenge` block with `run` true or absent (§3.1) | Yes |
| The code the learner types in the Try it section | Yes |
| Name it blocks, `// does not compile` blocks, and other illustrations | No. They are read-only highlighted code. |

`playground: true` exactly when the leaf has at least one runnable block. A `playground: true` leaf always has Try it and Fix the bug (`docs/04` §4). A `playground: false` leaf (a theory leaf) has neither, and its Predict items use `run: false` (paper-and-pencil). Because a Predict item with "Run it to check" runs code, any leaf with such an item must have `playground: true`.

Rejected alternatives (one line each): own Kotlin compile server (cost, abuse risk); Kotlin compiled to WebAssembly inside the page (the Playground's own `wasm` target still compiles on the API server, `compiler=wasm` in `src/config.js`, and a client-side Kotlin compiler for the browser was found in neither the README nor the source; read 18 Oct 2026, https://github.com/JetBrains/kotlin-playground); Android emulator streamed from a server (cost, out of scope per `00` §9).

---

## 2. Kotlin Playground embed (D8)

Source: the JetBrains `kotlin-playground` library. Checked on 18 Oct 2026 against https://github.com/JetBrains/kotlin-playground (README, `package.json`, `webpack.config.js`, `src/index.js`, `src/config.js`, `src/webdemo-api.js`, `src/executable-code/*`, `src/view/output-view.js`, `src/styles.scss`). Nothing was run in a browser for this review, so each fact below is tagged **Documented** (stated in the README), **Read in source** (seen in the code, never executed) or **Unknown** (⚠️ VERIFY with a test). Pages were read through a page summariser, not line by line, so re-read the cited file before relying on a "Read in source" row for a build decision.

Endpoints (Read in source; the host default is in `webpack.config.js`: `https://api.kotlinlang.org/`; paths are built in `src/config.js` as `<server>/api/<version>/...`): `GET <server>/versions` (version list), `POST .../compiler/run` (java target), `.../compiler/translate` (canvas), `.../compiler/test` (junit), `.../compiler/highlight`, `.../compiler/complete`. Requests are JSON (`Content-Type: application/json; charset=utf-8`). Documented in the README: `data-server` overrides the server, and `https://api.kotlinlang.org/versions` lists valid `data-version` values. Published terms of use or rate limits for `api.kotlinlang.org`: none found; the root URL answers 404. Treat the server as a free third-party service that can change (L27).

### 2.1 Loading

| Item | Decision |
|---|---|
| Script | `https://unpkg.com/kotlin-playground@1/dist/playground.min.js`, loaded from a plain absolute `https://` URL (matches `07` §3 "External"). The README uses `https://unpkg.com/kotlin-playground@1`, and `package.json` `main` is `dist/playground.min.js`, so the full path resolves. Current library version at check time: 1.34.0. |
| When loaded | Only on pages whose lesson JSON has `"playground": true` (from frontmatter, `00` §8.2; ADR 005), injected when the first code block nears the viewport (matches `07` perf budget "0 bytes on pages with `playground: false`"). |
| Never | Bundled into the Kobweb JS (size budget), or loaded on the tree/home screens. |
| Init | From the CDN script the function is `KotlinPlayground(selector, options)`; from NPM it is imported as `playground` and called as `playground(selector, options)`. Per the JSDoc in `src/index.js` it returns a Promise of an array of instances. The README also documents a `getInstance` callback exposing `execute()` and `getCode()`. Auto-attach with `<code class="kotlin-code">` is also documented; we call the function ourselves so the offline fallback (§2.3) can decide first. |

### 2.2 Per-block options

| Option | Value we use | Why |
|---|---|---|
| target platform | `data-target-platform="java"` (the default) | Runs `fun main()` with `println` |
| Kotlin version | `data-version="2.4.21"` (§5) | Output must match the version the author verified with. `data-min-compiler-version` is not enough: it sets only a minimum. |
| theme | Set no theme attribute. Never `darcula` (dark, L9). | Documented: the README lists the themes `idea`, `darcula`, `default`. Read in source: `THEMES.DEFAULT` is the fallback, and the page CSS draws a white editor (`.CodeMirror { background: white }` in CodeMirror 5's `lib/codemirror.css`; `idea` adds only a pale yellow active-line tint `#FFFAE3`). The sources disagreed on whether the attribute is `theme` or `data-theme`, so not naming one avoids the question. ⚠️ VERIFY (visual, Phase 6): render one block with no theme attribute on a white page and check the editor and output area are light and readable. |
| editable | Whole code editable for Try it and Fix the bug. Predict code is shown read-only. | Documented: `data-highlight-only` makes a block read-only (value `nocursor` also stops focus). Read in source: with highlight-only the Run button is **not rendered** (template condition `!highlightOnly`), and `execute()` itself has no highlight-only guard. Unknown: whether the output panel renders and whether the `getInstance` callback fires for a highlight-only block. ⚠️ VERIFY: make one `data-highlight-only` block, call `instance.execute()` from our own button inside `getInstance`, and see whether output appears. If not, use a normal editor and reset it on each run. |
| sample region | Not used in v1 | Documented: `//sampleStart` and `//sampleEnd` markers limit editing to one part of the code (the README summary was ambiguous about which side is the read-only one; not needed in v1); `none-markers` turns this off. It does not say the rest is hidden. We show the whole program. |
| autocomplete | `data-autocomplete="false"` (the default) | Keeps beginners from copy-pasting suggestions without reading |
| output height | Not an option for `java`. We size the card around the output area with our tokens. | Documented: `data-output-height` is for the `canvas` target only. Read in source: the java path never uses it, and the library CSS gives the output area only `min-height: 60px` and `overflow: auto` (no max-height). So there is no option; a CSS cap from our side is expected to work. ⚠️ VERIFY (Phase 6): add a `max-height` rule for the output element and check it scrolls. |

Chrome (Run button label, frame, spacing, fonts) follows `02`: `font.code` = JetBrains Mono (D13), `shape.md`. The Playground iframe/DOM is styled as far as the library allows; where it cannot be styled, wrap it in a card with our tokens. No hardcoded colours, fonts or spacing.

### 2.3 Offline and failure behaviour

| Case | What the learner sees | Status |
|---|---|---|
| Script fails to load (offline, blocked) | The code stays visible as a plain read-only code block, plus the message in §6. Run button is hidden. | Our code, testable |
| Script loads but `GET <server>/versions` fails (offline after load, server down, non-JSON reply) | Read in source (`webdemo-api.js` `getCompilerVersions`, `executable-code/index.js` `create`): the library swallows the error, writes `Cann't get kotlin version from server` to the **browser console only**, and turns every block into a **highlight-only** block: code shown, **no Run button**, nothing in the output area. | Read in source, never run. ⚠️ VERIFY by loading a page, then setting dev tools to Offline before init and again with `api.kotlinlang.org` blocked. |
| Versions load, then the run request fails | Read in source (`executable-fragment.js` `execute`): the rejection handler writes `error.message` into the output area as a single error entry and opens the console. There is no custom "server unreachable" text. | Read in source, never run. ⚠️ VERIFY with Offline switched on after the page is ready, then press Run. |
| Slow response | Read in source: `execute()` sets a waiting flag and ignores a second Run press until a result arrives. No timeout is defined anywhere in the library, and no retry. | Read in source. A timeout, if we want one, is ours to add; no number is chosen yet (Phase 6). |
| Server limits on code (threads, network, files, run time, size) | Documented: nothing. Read in source: `output-view.js` has a special notice for Java access-control exceptions, so user code runs in a restricted sandbox, and `webdemo-api.js` sets no size or rate limit on the client. | Unknown. ⚠️ VERIFY only if a leaf ever needs it. Rule: §3.3 already bans threads, time, randomness and input in Fix tasks, and §8 now extends the ban to every challenge plus network and files, so we do not depend on any limit. |

Detection (our side): the library's own failure at init is silent (second row), so waiting for the script `load` event is not enough. Before calling `KotlinPlayground(...)`, run `fetch("https://api.kotlinlang.org/versions")` ourselves with a ~5 s timeout and check that the answer is a list containing our pinned version (§5). Also check `navigator.onLine`. If anything fails, do not call the library: render the fallback. (Design, not yet built or run; the browser can call that URL because the library does the same call from the page.) Acceptance: with the browser set to Offline in dev tools, every lesson page still shows all code as readable text and every Predict card still works (§4).

Playground server dependency: code runs on JetBrains' servers, so a public lesson depends on a third party. Mitigation: all expected outputs are stored in the page (not fetched), so the page never depends on the server to be *correct*, only to be *runnable*.

---

## 3. Challenge data format in lesson Markdown

**This is the only challenge format.** A challenge is a fenced block whose info string is `challenge`. The body is YAML. Authors write nothing else to place a challenge: no HTML comment marker, no extra Markdown labels (`docs/04` §5). `validateContent` checks the fields below (rule text CH1-CH10 is owned by `07` §6.2.1; the field rules are owned here). Authors only ever write the YAML below.

**How a block reaches the learner (ADR 005).** `renderLessons` (`07` §6.2) turns each `challenge` block into one entry in the leaf's lesson JSON, served at `/lessons/<id>.json`: `type: predict` becomes `{ "t": "predict", ... }` and `type: fix` becomes `{ "t": "fix", ... }`. The field names are exactly the ones in §3.1 to §3.3 (`id`, `code`, `run`, `generated`, `verified`, and `options` / `output` or `hints` / `expected` / `fix` / `explanation`); only the YAML key `type` is renamed `t` in JSON. A plain `kotlin` fence becomes `{ "t": "code", "lang": "kotlin", "code": ..., "runnable": ... }`, with `runnable` following the §1 rule. The lesson JSON also holds `playground` (the frontmatter flag). The one lesson page (`K/pages/Lesson.kt`) reads the JSON (inline `script#kt-lesson-data` on a direct load, otherwise one `fetch`) and renders the blocks in one Compose composition with the existing `ui/lesson` components:

| JSON block | Component |
|---|---|
| `code`, `runnable` true and `playground` true | `EditorShell` (the Playground embed of §2) |
| `code`, otherwise | `CodeBlock` (read-only highlighted code) |
| `predict` | `PredictOutput` |
| `fix` | `FixBug` |

There is no Kobweb Markdown widget call, no `code` handler override and no generated challenge Kotlin. Challenge text, options, hints and answers are therefore not in `kotree.js`; they arrive with the lesson JSON. The static HTML of an exported leaf shows only the code (escaped `<pre><code>`) with no options, hints, answers or `why` text, so nothing spoils before the page's JavaScript runs.

On GitHub the block reads as plain YAML in the source file, so the answers and reasons are still readable there. If the lesson JSON cannot load, the page shows "This lesson could not load." with a Retry button (not the offline message of §6).

### 3.1 Common fields

| Field | Required | Rule |
|---|---|---|
| `type` | yes | `predict` or `fix` |
| `id` | yes | Unique inside the page, kebab-case, never changed after publishing (local-storage key, D10) |
| `generated` | no | `true` if the code is written in the style of AI output (§7) |
| `run` | no | Default `true`. `false` means nothing is sent to the Playground: the item is paper-and-pencil (trace a table, count steps). Allowed only on `predict`, and only on a `playground: false` leaf. `code` may then be any text the learner traces (a table, pseudo-steps) and need not compile. |
| `code` | yes | The code shown. When `run` is true it must be a complete Kotlin program that compiles and runs (§5). |
| `verified` | yes | The Kotlin version it was run with (`kotlin-2.4.21`, §5), `UNVERIFIED: <reason>` (§5), or `not-run` when `run: false` and the answer was checked by hand-tracing |

### 3.2 Predict-the-output (1 to 3 per leaf)

~~~
```challenge
type: predict
id: nullable-length
verified: kotlin-2.4.21
code: |
  fun main() {
      val name: String? = null
      println(name?.length)
  }
options:
  - id: a
    text: "0"
    why: "A missing value is not the same as an empty one. 0 would need an empty String."
  - id: b
    text: "null"
    correct: true
    why: "The safe call ?. returns null when name is null, and println prints that as null."
  - id: c
    text: "The program stops with an error"
    why: "That would happen with !!, not with ?."
output: "null"
```
~~~

(The example above is an illustration of the format. A real challenge's `verified` value is only written after the run in §5.)

Rules:
- `code` is 3 to 12 lines.
- **Exactly 3 options**, ids `a`, `b`, `c`, exactly one `correct: true`. The page shows them as A, B, C.
- Every option has `why` (one line, plain English, L19). The correct one explains the rule, in the words of the lesson's analogy where it fits. Each wrong one names the misconception it comes from and treats the guess as a clue ("You may have expected…"). The card shows the `why` of the chosen option.
- `output` is the exact stdout, multi-line allowed with `|`. Trailing newline is trimmed on both sides when compared.
- Flow: learner picks an option, presses **Run it to check**, the Playground runs `code`, and the card compares the real output with `output`. Offline: the card reveals `output` and the explanation straight from the page, and says it was not run (§6). With `run: false` there is no Run button; the card reveals `output` and the `why` of the chosen option when the learner presses "Show the answer".

### 3.3 Fix-the-bug (exactly 1 per leaf)

~~~
```challenge
type: fix
id: fix-unneeded-bang
verified: kotlin-2.4.21
code: |
  fun main() {
      val email: String? = null
      println(email!!.length)
  }
hints:
  - "Run it first. Read the last line of the message before touching the code."
  - "The value can be null. What does !! promise the compiler?"
expected: "0"
fix: |
  fun main() {
      val email: String? = null
      println(email?.length ?: 0)
  }
explanation: "!! says 'trust me, never null'. Here it is null, so it crashes. ?: gives a safe default."
```
~~~

(The example is short to show the shape. A real Fix task is 10 to 30 lines, `docs/04` §5.2.)

Rules:
- `code` is 10 to 30 lines, with exactly one realistic bug. It must show itself (wrong output, or a crash message), and the broken code is what the learner sees first.
- `hints`: **exactly 2**, vague to specific. Hint 1 points at a line, not at the fix. Hint 2 names the idea. Hints appear one at a time on request.
- `expected`: exact stdout of the **fixed** program. The card shows it from the start as "It should print", and accepts the learner's run when its stdout equals `expected` (trimmed). Different code with the same output is accepted.
- `fix` is a complete program, shown only after the learner asks ("Show me one fix"), and only after both hints. It uses only what this leaf (or an earlier required leaf) taught.
- `explanation` is optional. When present, the card shows it after a correct run or after "Show me one fix".
- A crash is a normal result: shown as plain text in neutral style.
- Fix tasks never depend on randomness, time, threads, or input.
- The five reviewer questions (`docs/04` §3.6) are plain Markdown written under the block by the author. They are the same text in every leaf and are not part of the YAML.

### 3.4 Progress

Per-card state (chosen option, hints used, solved) lives in local storage under `kotree.v1.challenge.<leafId>.<challengeId>` (D10), following the `kotree.v1.*` namespace of `docs/03` §9. It is never sent anywhere.

Acceptance (format): a challenge with a missing required field, a predict item without exactly 3 options or without exactly one `correct: true`, a fix item without exactly 2 hints, an empty `verified`, `run: false` on a `fix` item or on a `playground: true` leaf, or a duplicate `id` on a page fails the content check in CI (owned by the `07` §6 tasks).

---

## 4. Behaviour of the cards

| Moment | Predict card | Fix card |
|---|---|---|
| First view | Code, options, "Run it to check" disabled until an option is chosen | Editable code, "It should print" with `expected`, Run, "Want a nudge?" |
| Learner is right | Gentle confirm plus the `why` of the chosen option | "That prints what we wanted." plus `explanation` |
| Learner is wrong | Neutral card, shows the real output and the `why` of their option, then "Try another" | Their output beside `expected`, a line on what differs, next hint on request |
| Offline | Works from page data (§6) | Code readable; Run replaced by the offline message; `expected` and `fix` behind "Show me" |
| Re-do | "Try again" resets the card | "Reset code" restores the broken version |

Acceptance:
- No card uses a red cross, a red fill, buzzer wording, or a score that can go down.
- A wrong guess is the most useful moment (`01` §3.4): the wrong option's `why` is always displayed.
- Keyboard-operable; state is shown with words, not only colour.

---

## 5. How expected output is verified

**Rule: no expected output is written from memory or reasoning. It is copied from a real run.**

**Verification version: Kotlin 2.4.21** (pinned by the author, 18 Oct 2026). `verified` is written as `kotlin-2.4.21`. `docs/07` mirrors this version. The Playground's `data-version` (§2.2) is set to the same version, so the learner's run matches the author's run.

Checked 18 Oct 2026 (documented list, https://api.kotlinlang.org/versions, the URL the Playground README gives for `data-version`): it listed 2.5.0-Beta1, **2.4.21 (flagged `latestStable`)**, 2.4.10, 2.4.0, 2.3.21, 2.2.21 and older. 2.4.20 is not listed, so 2.4.21 is the only valid 2.4.2x pin. Settled for today. The list changes, so the pre-flight check in §2.3 compares against it at run time; if 2.4.21 ever disappears, ask the author whether to re-pin, and never run the Playground on a different version than `verified` says.

| Step | Detail |
|---|---|
| 1 | Save `code` as `Main.kt` in a scratch folder outside the repo |
| 2 | Run it: `kotlinc Main.kt -include-runtime -d main.jar` then `java -jar main.jar` (both commands are in https://kotlinlang.org/docs/command-line.html, read 18 Oct 2026; that page is not versioned, and it does not document a `kotlin Main.kt` command, so we do not use one). ⚠️ VERIFY on the author's machine: `kotlinc -version` prints 2.4.21. No Kotlin CLI? Use an IntelliJ scratch file or the Kotlin Playground itself (set to 2.4.21). |
| 3 | Copy stdout exactly into `output` / `expected` |
| 4 | Fix tasks: run the **broken** code (record the failure) and the **fixed** code (its output equals `expected`) |
| 5 | Put the Kotlin version in `verified` |
| 6 | Each wrong option must differ from the real output; where practical run a variant that really prints it, so the misconception is real |

For `run: false` items there is nothing to run. The author or `reviewer` traces the answer by hand a second time, and `verified: not-run` records that.

If a snippet cannot be run, write `verified: "UNVERIFIED: <reason>"`. The build treats this as a warning in dev and a failure for `status: live`; `reviewer` must not PASS a leaf containing one. In chat or docs the item is marked `⚠️ UNVERIFIED`.

Acceptance: for every challenge in a live leaf, a reviewer can paste `code` into the Playground and see exactly `output` / `expected`.

---

## 6. Words on the page (no shaming)

| Situation | Copy |
|---|---|
| Right | "Yes, that is what it prints." |
| Wrong (predict) | "Not this time. Here is what really happens." plus the option's `why` |
| Wrong (fix) | "Not quite yet. Here is what your code printed." |
| Hint offered | "Want a nudge?" |
| All hints used | "Here is one way to fix it." |
| Offline | "Running code needs internet. You can still read the code and the answers." |
| Runner did not answer | "The code runner did not answer. Check your connection and try again. The code and answer above are still right." |
| Compile error from the learner's edit | Compiler message as plain text under the code |

Banned: red crosses, "Wrong!", "Incorrect", "Failed", "Oops", scolding punctuation, scores that go down, streak loss, timers on a challenge. Acceptance: searching widget strings for these words finds nothing.

---

## 7. Marking AI-style generated code

Supports `01` §3.5: tidy, confident, slightly wrong code.

| Where | Marking |
|---|---|
| Data | `generated: true`. It means "written to look like AI output", not "a tool wrote it unchecked". |
| UI | A neutral chip above the code: "In the style of AI-written code". No warning colour. |
| Prompt | "Would you accept this in review? Find the one problem." |
| Honesty | Still run and verified (§5); a human reads the final text. The label never claims a specific tool wrote it. |
| Roadmap | The curriculum designer marks stems that use these tasks; `challenge-builder` sets `generated: true` only there. A Fix task in an unmarked leaf may be plain code, but still carries the five reviewer questions (`docs/04` §3.6). |
| Tone | Never "AI is bad": the person who can check it stays in the job. |

---

## 8. Authoring checklist for `challenge-builder`

- [ ] 1 to 3 Predict items per leaf. Exactly 1 Fix task, only on a leaf with `playground: true`.
- [ ] A theory leaf (`playground: false`) has Predict items with `run: false`, no Fix task and no Try it.
- [ ] Predict: exactly 3 options, one `correct: true`, each with a one-line `why` that maps to a real misconception. Code 3 to 12 lines.
- [ ] Fix: exactly 2 hints, `expected`, a complete `fix`. Code 10 to 30 lines.
- [ ] Every snippet was run; `verified` has `kotlin-2.4.21`, or UNVERIFIED with a reason.
- [ ] Output is deterministic (no time, random, threads, hash order). No network or file access in any challenge (Playground limits are undocumented, §2.3).
- [ ] Uses only features taught up to this leaf (`requires`).
- [ ] No `android.*` / `androidx.*`; Android ideas go through video or Sim, with an optional plain-Kotlin model.
- [ ] Plain English (L19); no trick syntax; copy follows §6.
- [ ] `playground: true` in the leaf frontmatter when any challenge has `run` true or absent.

## 9. Open items

| # | Item | Owner |
|---|---|---|
| E1 | ~~Amend L15 and D8 in `docs/00` for the no-emulator ruling~~ **Done 18 Oct 2026.** | closed |
| E2 | Run-time tests still open (§2.2, §2.3): the offline and server-down behaviour (read in source, never run), `execute()` on a highlight-only block, a CSS cap on the output area, the no-theme-attribute look on white, and the pre-flight `versions` check. URL, init function, `data-version`, option names, endpoints and the 2.4.21 offer are settled. | `architect`, Phase 6 |
| E3 | ~~Confirm that a Kobweb `code` handler override can emit the widget call.~~ **Obsolete 20 Oct 2026 (ADR 005):** no Markdown plugin is used for lessons; the lesson JSON and the block renderer replace it (§3). | closed |
| E4 | ~~Confirm the Playground offers 2.4.21~~ **Done 18 Oct 2026** (§5: listed, `latestStable`). Remaining: mirror 2.4.21 in `docs/07` (it already does, `LESSON_KOTLIN_VERSION`) and check `kotlinc -version` on the author's machine. | `architect` |
| E5 | `docs/10` Phase 6 prompt (line 91) says "first Android-behaviour animation named in `docs/06`"; no animation is named here. Change to "named in `docs/05`". | Orchestrator |
| E6 | ~~`07` §6 must sync and validate `challenge` blocks.~~ **Done:** `renderLessons` writes them to the lesson JSON and CH1-CH10 validate them (`07` §6.2, §6.2.1; fields in §3, including optional `run` and `generated`). | closed |
| E7 | Not answered by ADR 005, for `architect`: (1) how `renderLessons` decides `runnable` for a plain `kotlin` fence in Try it (by section heading, or an explicit marker); (2) how `EditorShell` hands a Compose-made element to `KotlinPlayground(...)` and disposes it; (3) where card state (`kotree.v1.challenge.*`) is read and written inside the block renderer (ADR 005 leaves it to Phase 6). | `architect`, Phase 6 |
