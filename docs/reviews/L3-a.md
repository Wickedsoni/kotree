# Review L3-a: five layer-3 shadow leaves (commit a108985)

Reviewer: independent. Date: 21 Oct 2026. Checklist: `docs/lesson-checklist.md` (items 1 to 14). Structure: `docs/04-lesson-system.md` and `content/_templates/leaf.md`.

Build: `../gradlew :site:validateContent` printed `validateContent: ok` (all five sim blocks pass SM0 to SM11).

Summary: what-is-an-activity FIX, first-get-call FIX, what-is-a-viewmodel FIX, running-work-on-another-thread FIX, loop-invariants FIX. No wrong fact and no wrong key was found. Every FIX is small. Items marked `fixer` are mechanical (exact text given); items marked `owner` need the lesson writer or author.

## Snippets (all compiled and run with `bash scripts/kt-run.sh`, kotlinc 2.4.21)

Every non-"does not compile" Kotlin block, every runnable or traceable challenge `code` and `fix`, and the Try-it break variants were compiled and run. Outputs match the text, `output` and `expected` fields:
- activity: model prints `Frame: standard setup` / `Picture in the frame: Forecast`.
- first-get-call: model prints `Table: card handed over` / `Waiter: walking to the kitchen` / `Table: got Sunny`.
- viewmodel: model `Screen's own count: 0` / `Keeper's count: 2`; Predict 1 `Screen 1, keeper 2`; Predict 2 `keeper created`, `screen created`, `screen created`; buggy Fix prints `Taps: 0`, the `fix` prints `Taps: 2`; Try-it Break (delete `keeper.count++`) prints `0` and `0` as stated.
- thread: Name it program prints the three lines in order; `which-thread-prints` prints `Job runs on: helper` / `Main runs on: main`; `run-is-not-start` prints `Job runs on: main` / `Main runs on: main`; Fix `fix` prints `Screen: drawing`, `Screen: still drawing`, `Job: report ready` (buggy version prints job line second, as the bug says). Thread name `main` is confirmed on a local JVM (Javadoc does not state it; see "Could not verify").
- daa: `Till: 22`; `After 4: 4`, `After 6: 10`, `After 2: 12`; `Largest: 0`; fix prints `Warmest: 18` / `Warmest: -3` (buggy prints `18` / `0`); Try-it Break (`receipts += 1`) throws `IllegalStateException: Check failed` at the first turn, so `Till` is never printed, as stated.
- Warnings: the `kt-run.sh` harness shows none; as in L2-a it is not known to surface warnings.

## Sims (hand-traced against SimCore fold/step, SimModel and TryProgress)

All five: template fits ADR 009 "Use for" (stepper trace, flow route, zones, lanes queues, stepper trace); no colour, size or coordinate in any block; no `phone` or `timeline`; every Try step uses only buttons or toggles; every `done` event is a declared action or toggle event and every step is reachable in order; 1 to 3 steps, one optional (first-get-call); `say` text matches the vars (interpolation runs after the other effects of a case, rows run at their position, both checked).
- picture-frame-hook: with `skip` off, runs 1 to 4 give pc 1 to 5, picture "Forecast" after run 3 (line l4 lit), final `end of onCreate`; with `skip=on` replayed from the start (settings are hoisted) picture stays "Empty frame" and run 3 uses the "no setContent line" case (pc 3 and skip on listed before pc 3). Rows have 3 cells for 3 columns. Prose ("frame first, picture at setContent", "frame stays empty") matches.
- waiter-and-kitchen: order moves card to waiter; walk with door off sets pictures 3 (say says "3 pictures", matches); serve removes card and spawns the plate. Optional `door=on` replays: pictures 0, card marked bad, then plate still arrives. The optional step text ("drops to 0") matches. Refuse texts are reachable and true.
- stage-and-backstage: add x2 gives count 2 and label "Counter prop: 2"; rotate with the prop on stage falls to the second case (no `when`) and resets count to 0, costume 2; Try 3 (add, stash, rotate) gives count 1 kept, costume 3, say "still at 1". Matches prose.
- second-cashier: bring with helper off puts the order in `main-line.0`, missed 50 (800 / 16 = 50); `open-helper=on` replays it into `helper-line.0` with missed 0; report then serve gives the note and "Display: new price", shown 1. Lane ids `main-line`, `helper-line`, `window` do not collide in `slotIn`. Matches prose.
- till-check-scale: serves give till 15, 18, 22 and receipts 5, 8, 12 (holds); with `skip=on` customer 2 gives till 18 and receipts 5 (broken, l4 not lit), customer 3 gives till 22 and receipts 9 (still broken). Every number in every `say` matches. Try 2 "first row where the check reads broken" is row 2.
- Not done: opening `/preview/<id>` in a browser (mouse, keyboard only, reduced motion, live region). The mouse and keyboard paths are the same controls for all five (buttons and one toggle, no drag, no text input), so the risk is low, but the browser pass is still owed by the housekeeper or the layer build.

## Docs checked by fetch on 21 Oct 2026 (all matched unless listed in a FIX)

- Android intro-activities: entry point sentence; "Unlike programming paradigms in which apps are launched with a `main` method"; "fires when the system creates your activity"; "next callback is always onStart"; "In a Compose app, use this callback to set up your host composable using `setContent`"; "In modern Compose apps, this is the only activity necessary"; main activity is the first screen; manifest `<activity>` element; the doc sample extends `ComponentActivity` and calls `setContent` inside `onCreate`.
- Android runtime-changes: "The recreation also clears out any state kept as fields in the Activity"; new instance created with `onCreate`.
- Android viewmodel page: business logic or screen level state holder; caches state and persists it through configuration changes; remains in memory until the owner disappears (activity finishes); two key benefits; no `Context` or `Resources`; `viewModel()` from lifecycle-viewmodel-compose.
- Android viewmodel-savedstate: system-initiated process death and `SavedStateHandle` as backup.
- Android network-ops/connecting: Retrofit sentence (type-safe HTTP client for the JVM, built on OkHttp, declarative interface); `UserService` with `@GET` and `suspend fun`; `NetworkOnMainThreadException`.
- Android processes-and-threads: the two rules, UI thread, "not instantaneous" worker threads, not thread-safe, `runOnUiThread`, `View.post`, `View.postDelayed`, coroutines and executor examples.
- Kotlin coroutines-basics: "When a coroutine suspends, the thread isn't blocked and remains free to run other tasks"; "You can only call a suspending function from another suspending function"; `launch`.
- Kotlin `thread`: "Creates a thread that runs the specified block of code"; `start`: "if true, the thread is immediately started".
- Java 21 Thread: start schedules and "will execute independently of the current thread"; "started at most once"; run "not intended to be invoked directly"; join "Waits for this thread to terminate".
- Retrofit CHANGELOG 2.6.0 (2019-06-05): "Support suspend modifier on functions for Kotlin". The site `https://lysine.dev/retrofit/` is the documentation site linked from the Retrofit README, so the SOURCES line is fine.
- OkHttp (re-check asked): the current `OkHttpClient.kt` KDoc on `master` (https://raw.githubusercontent.com/square/okhttp/master/okhttp/src/commonJvmAndroid/kotlin/okhttp3/OkHttpClient.kt) says: "OkHttp performs best when you create a single `OkHttpClient` instance and reuse it for all of your HTTP calls. This is because each client holds its own connection pool and thread pools. Reusing connections and threads reduces latency and saves memory. Conversely, creating a client for each request wastes resources on idle pools." The leaf's sentences match. The 3.9.1 Javadoc cited in the leaf is old; see first-get-call FIX 1.
- Kotlin `check` (loop-invariants) was matched in L2-a: throws `IllegalStateException` when false.
- LeetCode numbers and titles in loop-invariants: 704 Binary Search, 26 Remove Duplicates from Sorted Array, 283 Move Zeroes, 53 Maximum Subarray, 75 Sort Colors, 42 Trapping Rain Water. All correct; order easy, easy, easy, medium, medium, hard; `practice-daa.md` problems 26, 283, 75, 53, 42 are all used (704 is an extra, correct one).

## Could not verify

- The Kotlin Playground (play.kotlinlang.org) thread name: `Thread.currentThread().name` prints `main` on a local JVM with kotlinc 2.4.21, and the Java docs do not state it. Whether the hosted Playground prints `main` was not run. `which-thread-prints` and `run-is-not-start` depend on it. Check once on the live Playground (author or the layer build) before go-live.
- Browser behaviour of `/preview/<id>` (see Sims).
- That Android Studio's current Empty Activity template uses `ComponentActivity` and `enableEdgeToEdge`: the leaf rightly makes no claim.

---

## 1. android-core/activities/what-is-an-activity: FIX (minor)

Facts: all match the docs above. The three "my own reasoning" labels (the "single door" trade-off, the empty-frame cause, the sentence about the template) are in the right places. Question block has 4 sentences. Recap lines 16, 10, 13 words. Predict keys b, a (varied); `verified: not-run` and `run: false` are right (no Kotlin runs). Frontmatter (difficulty easy, requires, sims, fruit) matches the roadmap row. needed-ids row present.

1. Checklist 3. Line 150 uses the Android term "entry point" without defining it. Old: `The Android documentation says it is the entry point for the app's interaction with the user.` New: `The Android documentation says it is the **entry point** for the app's interaction with the user. An entry point is a door the system can use to come into your app.` Mechanical (fixer).
2. Checklist 13 (author `build-exercise`) is not seeded. Tricks has 4 bullets (the limit), so merge two and add the task. Old lines 270 to 273:
   `- Frame first, picture second.`
   `- You never call \`onCreate\`. The system calls it for you.`
   `- No \`setContent\`, no picture.`
   `- One frame, many pictures: a modern app needs one activity.`
   New:
   `- Frame first, picture second. You never call \`onCreate\`: the system calls it for you.`
   `- No \`setContent\`, no picture. One frame, many pictures: a modern app needs one activity.`
   `- Task, in Android Studio: open the \`MainActivity\` of a new Compose project and find the \`setContent\` call inside \`onCreate\`.`
   Mechanical (fixer). (The Compose template does contain these; this is a find-it task, not a claim about the template.)
3. Housekeeper: strip the three VERIFY lines 22 to 24 (the plain-Kotlin model compiled and prints as stated on line 207; the template question is not claimed; the empty-frame sentence is already labelled "my own reasoning" in "Where this shows up"). Both Predict blocks keep `verified: not-run`.

## 2. app-networking/http-client/first-get-call: FIX

Facts: all verified, including the OkHttp advice (re-checked on the current KDoc). `weatherApi.today()` is line 9 in the Predict listing and line 3 is the `suspend fun`, line 6 the builder (key c is true). Question block 3 sentences. Recap 10, 16, 10 words. Frontmatter matches the roadmap row.

1. Sources, checklist 9 and 4. Line 24 cites the 3.9.1 Javadoc and line 25 is a VERIFY about it. The current text is verified. Old line 24: `- https://javadoc.io/static/com.squareup.okhttp3/okhttp/3.9.1/okhttp3/OkHttpClient.html (create one OkHttpClient and reuse it; each client has its own connection pool and thread pools)`. New: `- https://raw.githubusercontent.com/square/okhttp/master/okhttp/src/commonJvmAndroid/kotlin/okhttp3/OkHttpClient.kt (current OkHttpClient KDoc, read 21 Oct 2026: create a single OkHttpClient and reuse it for all calls; each client holds its own connection pool and thread pools; reuse reduces latency and saves memory; a client for each request wastes resources on idle pools)`. Delete the VERIFY on line 25. Also delete the VERIFY on line 28 (the model compiled and prints the three lines on line 205). Keep nothing else changed. Mechanical (fixer/housekeeper).
2. Checklist 4. Line 146: `**\`@GET\`** marks one entry as a request of the GET kind, which asks for data without changing anything.` Add the label: `... which asks for data without changing anything (general knowledge).` Mechanical.
3. Checklist 11 (answer quality). The interview answer on lines 293 to 294 says "I do not know how Retrofit does this inside". The Retrofit CHANGELOG for 2.6.0 (already in SOURCES, line 22) says a `suspend` function acts like a function that returns a `Call` which is run with `enqueue`. Old last two sentences: `The call pauses at that line and carries on when the answer arrives. I do not know how Retrofit does this inside, so I would say that if asked.` New: `The call pauses at that line and carries on when the answer arrives. The Retrofit changelog for 2.6.0 says a \`suspend\` function behaves like a function that returns a \`Call\` run with \`enqueue\`. My own reasoning: that is why the calling thread is not held while the request is in flight.` Mechanical (text supplied). Also add `(2.6.0 entry also says suspend works like Call.enqueue)` to SOURCES line 22.
4. Checklist 13 (`build-exercise`) not seeded. Tricks has 4 bullets, so replace bullet 4 (`- The call can wait, so it is marked \`suspend\`.`, already said in Build step 6 and Recap) with: `- Task, in an Android project with Retrofit: call one \`@GET\` function five times with one shared client, then five times with a new client for each call, and compare the times.` Mechanical.
5. Owner, optional. The author's `common-wrong-beliefs` ("a suspend call on a Retrofit interface needs `withContext(Dispatchers.IO)`") is not used. It is consistent with the changelog (`enqueue`) but "main-safe" is not stated on any page read. If the writer wants it as a fourth Common mistake, first read the Retrofit documentation for the statement that suspend calls are main-safe, then label it. Do not add it unverified.
6. Housekeeper: VERIFY on lines 26 and 27 stay as they are labelled ("my own reasoning"; no trailing-slash claim) and may be stripped at go-live; no UNVERIFIED stamp exists (`run: false`, `verified: not-run` is correct on both Predicts).

## 3. architecture-data/viewmodel/what-is-a-viewmodel: FIX

Facts: all match the docs. Keys a, b (varied); the Predict code is 10 and 6 lines; Fix code 22 lines (10 to 30), no `generated` (roadmap row says `F:`, correct); Fix has exactly 2 hints; five reviewer questions present. Recap 16, 12, 19 words. Frontmatter matches the roadmap row. Prose is 1,604 words (budget 900 to 1,600): four words over, no action.

1. Checklist 3. `Context` and `Resources` are used on line 134 and in the Common mistakes and interview without any gloss. Old line 134: `It also says a ViewModel should not hold references to lifecycle-related things such as a \`Context\` or \`Resources\`, because it can live longer than the screen and a held reference would leak memory.` New: `It also says a ViewModel should not hold references to lifecycle-related things such as a \`Context\` (the Android class that gives access to information about the app's environment) or \`Resources\` (the class that gives access to the app's resources, such as strings and images), because it can live longer than the screen and a held reference would leak memory. A memory leak is memory that stays in use after nothing needs it (general knowledge).` Mechanical (fixer).
2. Fact, line 365 (interview, last answer): `The trade-off is that saved state must be small and light, so large data belongs in local storage.` The saved-state page says: "State must be simple and lightweight. For complex or large data, you should use local persistence." New: `The trade-off is that saved state must be simple and lightweight, so complex or large data belongs in local persistence, as the saved-state page says.` Mechanical.
3. docs/04 4.8 (Tricks 2 to 4 bullets): Tricks has 5 (lines 335 to 339). Delete bullet 2 (`- A ViewModel is a stage manager: the audience never sees it.`, already the Mental model) so the Task bullet stays. Mechanical.
4. Housekeeper (compiled and run on kotlinc 2.4.21, outputs match): set `verified: kotlin-2.4.21` on lines 193 (`screen-and-keeper-after-rebuild`), 225 (`keeper-made-once`) and 263 (`fix-counter-kept-in-screen`). Delete VERIFY line 23 (compile now done). The VERIFY on line 24 is about re-checking ViewModel behaviour: done today against the three SOURCES pages (all matched), so strip it. Line 25 is a note, strip.
5. Owner, optional: the author's `common-wrong-beliefs` for this leaf (every composable needs its own ViewModel) is not used; acceptable for a leaf whose roadmap wrong belief ("a second Activity") is covered.

## 4. concurrency/threads/running-work-on-another-thread: FIX

Facts: all verified (Android, Kotlin and Java quotes above). The `Thread-0` option in `run-is-not-start` is plausible and the stated `main` is true on a local JVM. Keys c, a (varied). Fix code 12 lines (the `fix` is 16); Fix has 2 hints and the five questions. Recap 13, 15, 16. Frontmatter matches the roadmap row. Playground true and Try it and Fix the bug exist: correct.

1. docs/04 4.8 (Tricks 2 to 4 bullets): Tricks has 5 (lines 297 to 301). Merge bullets 1 and 4. Old: `- A helper counter does not make the order faster. It frees the main line.` and `- Only the main counter tidies the window display.` New (one bullet, delete the other): `- A helper counter does not make the order faster. It frees the main line, and only the main counter tidies the window display.` Mechanical.
2. Checklist 3. Two unglossed terms. Line 314: `The documentation says the Android UI toolkit is not thread-safe, so do all changes ...`. New: `The documentation says the Android UI toolkit is not thread-safe (two threads changing it at the same time can clash, general knowledge), so do all changes ...`. Line 139 to 141 (Name it): `creates a thread that runs the block you give it.` New: `creates a thread that runs the block of code you give it, the lines between the curly braces.` Mechanical.
3. Checklist 4, optional wording. Line 145: `Calling \`run\` yourself runs the job on the thread that called it.` The Javadoc only says invoking `run` invokes the task. Add after it: `(My own reasoning from the Javadoc: an ordinary method call runs on the caller's thread.)` Mechanical.
4. Housekeeper (compiled and run, outputs match): set `verified: kotlin-2.4.21` on lines 177 (`which-thread-prints`), 207 (`run-is-not-start`) and 242 (`fix-slow-job-on-main-line`). Delete VERIFY lines 24 and 25 (resolved). Keep VERIFY line 23 only for the Playground thread name until the author confirms `main` on the live Playground, then delete it (see "Could not verify").
5. Owner: prose is 1,857 words against the 900 to 1,600 budget. Trim about 260 words (longest: the Name it section, the interview answer on thread pools, Where this shows up) or relax the budget for leaves that carry the interview section, as raised in L2-a.

## 5. daa/analysis/loop-invariants: FIX (minor)

Facts: invariant definitions, the three CLRS steps (labelled general knowledge) and the binary-search invariant are correct. `check` matches Kotlin docs. Keys c, a (varied). Fix has `generated: true` (roadmap `F(ai)`), correct, and 14 lines. "Practise on these": 6 problems, easy to hard, numbers and titles verified, one training line each. Recap 18, 19, 16 words. Frontmatter matches the roadmap row; the roadmap sim cell says "a balance scale" and the leaf uses a till-check stepper, which `docs/sims/needed-ids.md` line 76 already records.

1. Housekeeper (compiled and run, outputs match): set `verified: kotlin-2.4.21` on lines 180 (`running-total-last-line`), 211 (`largest-starting-at-zero`) and 251 (`fix-warmest-starts-at-zero`). Delete VERIFY line 23 (the Break step was run: `check` fails in the first turn). Lines 24 and 25 are explanations of choices, strip at go-live.
2. Owner: prose is 1,914 words against the 900 to 1,600 budget. Trim about 310 words (candidates: the last paragraph of Name it, the Where this shows up paragraph, the interview answers) or relax the budget for leaves that carry the interview and practice sections (L2-a raised the same point).
3. Checklist 14 tidy, optional: the Predict `running-total-last-line` code has a comment `// What is the last line printed?`; fine.

---

## Cross-leaf housekeeping

- `docs/sims/needed-ids.md` lines 72 to 76 hold the five new sim ids but are an uncommitted change (`git status`: ` M docs/sims/needed-ids.md`; commit a108985 touched only the five leaves). Commit it with the fixes.
- Every `say` is interpolated and true, but several run two or three short sentences, not one. This matches the already-reviewed leaves, so it is not a FIX.
- Alt text: no images in any of the five. Colour: none in any sim block. No claim in any leaf is unlabelled "official" that the pages read do not state.

VERDICT: FIX

## Fixes applied

- what-is-an-activity: entry point gloss, Tricks merged to 3 bullets plus Task, VERIFY lines stripped.
- first-get-call: OkHttp source replaced with current KDoc, changelog note, `(general knowledge)` label, `Call.enqueue` answer, Tricks task, VERIFY lines stripped.
- what-is-a-viewmodel: Context and Resources glossed, saved-state wording, Tricks bullet removed, `verified: kotlin-2.4.21`, VERIFY lines stripped.
- running-work-on-another-thread: thread and UI-toolkit glosses, run label, Tricks merged, `verified` stamps, VERIFY lines stripped except the Playground thread-name line; prose trimmed to about 1600 words.
- loop-invariants: `verified` stamps, VERIFY lines stripped; prose trimmed to about 1600 words.
- Owner items (common-wrong-beliefs, Playground check) left open.
- validateContent: ok.
