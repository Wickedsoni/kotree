# Review L1-c: first-leaf shadow leaves (android-core, app-networking, architecture-data, concurrency, kmp, native-ndk)

Reviewer: independent, 21 Oct 2026. Checklist: `docs/lesson-checklist.md`; structure: `docs/04` sections 2 to 6; frontmatter: `docs/00` 8.2.

Leaves:
- `content/android-core/what-is-an-app/what-is-inside-an-app.md`
- `content/app-networking/requests-from-an-app/what-an-app-sends-and-gets.md`
- `content/architecture-data/why-architecture/why-code-needs-structure.md`
- `content/concurrency/why-the-main-thread/what-the-main-thread-does.md`
- `content/kmp/what-is-kmp/what-kmp-shares.md`
- `content/native-ndk/what-is-native/what-is-native-code.md`

## What passed

- `./gradlew :site:validateContent --offline` reports `validateContent: ok` (exit 0).
- Frontmatter: all 14 keys, in order, on all six. id, title, difficulty easy, order 1, requires [], fruit false equal the roadmap rows. Summaries are under 25 words. `sims` match the SIM markers: `[request-parcel-pipe]`, `[main-thread-queue]`, `[shared-or-platform-bins]`, the other three `[]`. All three sim ids are in `docs/sims/needed-ids.md`. `playground` is correct everywhere: true for networking, architecture, concurrency (Try it and Fix the bug present); false for android-core, kmp, native-ndk (all Predicts `run: false`).
- Section order and counts: 12 or 13 headings in the right order, interview section before Recap with 4 questions each, Recaps 3 one-sentence lines, SOURCES comment on line 18, Question blocks of 5 sentences or fewer, 2 to 3 Predicts with 3 options, exactly 1 Fix with 2 hints and the five reviewer questions. No stray VERIFY, no "next lesson", no banned words.
- Prose budget (outside fences and the interview section): 1037, 1305, 1155, 1129, 1214, 1082 words. All inside 900 to 1600.
- Kotlin: all blocks compiled and run with `scripts/kt-run.sh` (pinned Kotlin 2.4.21), no compiler warnings. Every Predict output, Try it step, Break step and Fix pair matches the leaf:
  - networking: `200 / 3 books`; `404 / nothing here`; `200 / results for kotlin`; POST gives `201 / saved`; the buggy `search` prints `404 / nothing here`, the fix prints `200 / results for kotlin`.
  - architecture: `Total: 70`; with 80 `Total: 75`; the buggy profile function throws NumberFormatException on "Ada" (so hint 1 is true); the fix prints `Name: Ada / Status: minor`.
  - concurrency: queue order as stated; `1, 6, 7`; with 50 it is `1, 51, 52` (so the leaf's "52" is right); the buggy list prints 40, 41, 42; the fix prints 1, 41, 42.
- Predict keys are true, and the correct letter varies in every leaf (b/c/a, a/c, c/a, c/a/b, c/a/b, c/a).
- Interview section of native-ndk (the writer's own, labelled): all four answers are sound. The ABI, `/lib/<abi>/lib<name>.so`, NDK-reasons and missing-`.so` claims match the official pages. See FIX 4 and 5 for two wording risks.

## Facts verified (read 21 Oct 2026)

Page bodies read:
- `developer.android.com/guide/components/fundamentals`: the four definitions; "Android apps don't have a single entry point: there's no `main()` function"; undeclared activities, services and content providers "can never run"; broadcast receivers "can either be declared in the manifest or created dynamically in code"; the service-plays-music example; "if the content provider permits it". The page also lists "the battery is low" as a system broadcast, so the low-battery example is officially backed.
- `.../guide/components/processes-and-threads`: main thread wording; "in charge of dispatching events ... including drawing events"; "does not create a separate thread for each instance of a component"; blocked thread and ANR "more than a few seconds"; the two rules. The page also says "the Android UI toolkit is not thread-safe" (see FIX 1).
- `.../topic/architecture`: "most important principle is separation of concerns" with the exact list; "common mistake to write all your code in an Activity"; the OS "frequently destroying and recreating them" on rotation; primary role of an activity is to host the UI; mixing network and database logic makes testing "much more difficult, if not impossible"; "Don't store data in app components".
- `kotlinlang.org/docs/multiplatform/kmp-overview.html` and `.../multiplatform-expect-actual.html`: all quoted KMP sentences are verbatim. The page also recommends interfaces over expect/actual, so "try the interface first" is supported.
- `developer.android.com/ndk/guides` and `.../ndk/guides/abis`: all NDK and ABI quotes verbatim. The "builds and installs, but crashes at runtime" sentence sits at the end of the install-time library scan section. The page does not say when the crash happens (see FIX 5).
- `developer.android.com/develop/connectivity/network-ops/connecting`: "Most network-connected apps use HTTP to send and receive data."

Not fully verified (page body unreachable; the proxy returned 403 and DNS failed for developer.mozilla.org):
- MDN HTTP Messages and 404 pages: only web-search excerpts were readable. They agree with the leaf: start line, optional headers, optional body; a request body mainly for POST, PUT and PATCH; status line with a numeric status code; 404 means the server cannot find the requested resource. The leaf labels all of it "general knowledge", which is correct.
- Looper reference: the body could not be read (the part fetched held no class text). Search excerpts confirm: Looper runs a message loop for a thread, MessageQueue holds the messages, the system creates the main looper. The leaf makes no Looper claim, so nothing depends on it.
- `source.android.com/docs/core/runtime`: only search excerpts. They confirm ART runs DEX bytecode and compiles it on the device with dex2oat at install time (AOT, plus JIT and interpretation since Android 7). The leaf makes no DEX or ART claim. This fact matters for FIX 4.

## FIX list

1. Mechanical. `concurrency/what-the-main-thread-does.md`, interview Q4 ("Why does Android keep screen work on one thread?", about line 250). It says "The documentation gives the rule but not the reason." That is false: the processes-and-threads page says "the Android UI toolkit is not thread-safe. So, don't manipulate your UI from a worker thread." Rewrite: quote that sentence as the official reason, keep the "conflicting changes" explanation labelled "my own reasoning (general knowledge)", and keep the trade-off. The "Where the analogy stops" sentence can stay.
2. Mechanical. `concurrency/what-the-main-thread-does.md`, Name it (lines 73 to 75), Common mistakes (line 229), Where this shows up (line 237), interview Q2 (line 245), checklist 3. "component", "widgets", "events", "UI toolkit" and "process" are used before they are defined. Add one short gloss at first use, for example "components, the building blocks of an app such as its screens", "widgets, things on the screen such as buttons", "the UI toolkit, the set of tools that draw those widgets".
3. Mechanical. `architecture-data/why-code-needs-structure.md`, Mental model (lines 26 to 36), Build step 3 (line 64) and Tricks (line 240). The analogy is never mapped to the three code jobs, which docs/04 4.2 requires. The order is also inconsistent: the code runs get, decide, show, but the text says "cook, waiter, cashier", and the Predict `why` lines already treat the cashier as the discount rule. Add an explicit mapping (cook = getting the prices, cashier = working out the total, waiter = showing it) and write the plate order in that same order in Build and Tricks.
4. Mechanical. `native-ndk/what-is-native-code.md`, Name it definition (line 69), interview Q1 (line 165) and Guess/Build wording. "Compiled ahead of time into instructions for one kind of CPU" also describes ART's install-time dex2oat output (source.android.com, search excerpt), which a later leaf teaches, and a sharp interviewer would object. Say "compiled on the developer's computer, before the app is installed, into instructions for one kind of CPU". Label it "(general knowledge)".
5. Mechanical. `native-ndk/what-is-native-code.md`, Predict `remove-the-bottle` option a (line 128) and its `why`/`output` (lines 130, 138), Tricks (line 146), Where this shows up and interview Q4 (line 174). The official page says only "crashes at runtime". "Crashes at that moment" and "when the feature runs, not at install time" are inference (the library is normally loaded when code first asks for it). Use "crashes at runtime" in option text and output, and add "(my own reasoning)" to the "when the feature runs" explanation.
6. Mechanical. Set `verified: kotlin-2.4.21` (the code was compiled and run with it, and the outputs match) on these blocks. Networking: `address-that-does-not-exist`, `where-the-search-word-goes`, `fix-search-word-in-address`. Architecture: `mixed-cart-total`, `change-the-rule`, `fix-all-in-one-profile`. Concurrency: `order-in-the-line`, `when-does-the-second-draw-finish`, `fix-slow-job-in-front`. Keep `verified: not-run` on the `run: false` blocks in android-core, kmp and native-ndk. In the three SOURCES comments, replace "ran on Kotlin 2.4.20 only, outputs traced by hand" with the 2.4.21 run.
7. Mechanical. Labels (checklist 4). `app-networking` interview Q2 ("a token ... goes in a header", "new record goes in the body of a POST") and Q3 (404 is a normal reply): add "(general knowledge)". The four-question advice in Where this shows up: add "(my own reasoning)". `android-core` Where this shows up, the sentence "look first at a service or a broadcast receiver, not an activity": add "(my own reasoning)". `architecture-data` Where this shows up, last paragraph (rotation and rule change both "come from one place doing several jobs"): add "(my own reasoning)". The Android page does back "(my own example)" for low battery, so that label may be dropped or turned into a citation.
8. Mechanical. `kmp/what-kmp-shares.md`, Name it (lines 83 to 87), checklist 3. "UI", "API(s)", "interface" and "Compose Multiplatform" are used without a gloss. Add short ones: UI = the screens the user sees and touches; API = a set of ready-made calls a phone offers; interface = a list of what something must do, with no code behind it; Compose Multiplatform = JetBrains' way to share screen code across platforms (from the verified kmp-overview sentence).
9. Needs the owner (or a writer pass). Author-supplied data not used (checklist 13). (a) Networking: the `common-wrong-beliefs` seed ("HTTPS hides URL query parameters from server logs") is missing from Common mistakes. (b) Concurrency: the second belief (Compose composables run on background threads) is missing, and the junior and senior interview seeds (Looper/MessageQueue/Handler; main queue flooding) were dropped because the Looper page was unreadable; search excerpts now support a short Looper answer, but the page body should be read before it is cited. (c) None of the six uses its `build-exercise` in Try it or Tricks, and the official references from the practice files (Network Inspector, Looper, APK Analyzer) are not in the SOURCES comments. L1-a deferred this class of item, so either add one optional Tricks line each or record the deferral in `docs/NEEDS-ME.md`. (d) `docs/practice/interview-android-core.md` for `what-is-inside-an-app` is about APK contents, classes.dex and AAB, which does not match the roadmap row (four components). The writer rightly followed the roadmap. The practice entry should be fixed or moved to the APK leaf. (e) Architecture roadmap build task ("add one feature and count edited places") is not realised; Try it changes one number only.
10. Mechanical (housekeeper or curriculum-designer on go-live). Strip the SOURCES comments. Roadmap markers: the app-networking row's "VERIFY: which request parts the current client sets by default" (the leaf makes no such claim); the kmp row's "VERIFY at publish: copy each status with its date" (the leaf states no platform statuses); the native-ndk row's "VERIFY again before live" (now verified against the two NDK pages above).

Not run: the full `wave-check.sh` export and the browser view of sims (the three sims are not built; SIM markers are allowed). No images exist in these leaves, so there is no alt-text or contrast issue.

VERDICT: FIX

## Fixes applied

Applied 21 Oct 2026 by the writer. Items 1 to 8 and 10 were requested; item 9 is deferred.

- FIX 1 done: concurrency interview Q4 now quotes "the Android UI toolkit is not thread-safe. So, don't manipulate your UI from a worker thread.", glosses "thread-safe" and "worker thread", and keeps the conflicting-changes idea as "my own reasoning (general knowledge)".
- FIX 2 done: concurrency Name it glosses components, events, widgets; interview Q2 glosses process; Where this shows up glosses UI toolkit.
- FIX 3 done: architecture Mental model maps cook = getting prices, cashier = total and discount, waiter = showing. Build step 3, Tricks and You can now use the order cook, cashier, waiter.
- FIX 4 done: native-ndk Name it, Build step 2, interview Q1, Recap and Common mistakes now say "compiled on the developer's computer, before the app is installed"; the definition is labelled (general knowledge).
- FIX 5 done: native-ndk option a, its why, output, Tricks, Where this shows up and interview Q4 use "crashes at runtime"; the "when the feature runs" idea is labelled "my own reasoning".
- FIX 6 partly done: `verified: kotlin-2.4.21` set on the nine named blocks (networking 3, architecture 3, concurrency 3). The `run: false` blocks keep `not-run`. No Kotlin code was changed, so the reviewer's 2.4.21 compile and output check stands. I had no shell in this session, so I did not re-run `scripts/kt-run.sh`.
- FIX 7 done: labels added in networking (Q2, Q3, four-question advice), android-core (service or receiver advice; the "(my own example)" label was replaced by the documentation's own low-battery mention) and architecture (last paragraph).
- FIX 8 done: kmp Name it glosses UI, API, interface and Compose Multiplatform.
- FIX 10 partly done: the SOURCES comments are stripped from all six leaves. The three roadmap markers (networking, kmp, native-ndk rows) are NOT cleared, because this task forbids roadmap edits; they stay for `housekeeper` or `curriculum-designer` at go-live.
- FIX 9 deferred by instruction.
- `./gradlew :site:validateContent` was NOT run (no shell available in this session), so the final verdict line is withheld. Re-run the validator, then add the re-check verdict.

## Re-check (21 Oct 2026)

`validateContent: ok` re-run. FIX 1 to 8 verified in the files: Q4 quotes the thread-safe sentence with glosses; concurrency glosses components, events, widgets, process, UI toolkit; architecture maps cook, cashier, waiter and uses that order in Build, Tricks and You can now use; native-ndk says "compiled on the developer's computer, before the app is installed" and "crashes at runtime", with "my own reasoning" on the load-time idea; the nine `verified: kotlin-2.4.21` tags are present and the `run: false` blocks keep `not-run`; labels added; kmp glosses added; SOURCES comments gone. FIX 9 is deferred to the owner and the three roadmap markers of FIX 10 are left for housekeeper at go-live (both accepted).

New violation:

1. Mechanical. `content/native-ndk/what-is-native/what-is-native-code.md` line 134 (Predict `remove-the-bottle`, option c `why`): "The documentation says it installs. The documentation says it installs, then crashes at runtime." The first sentence is a duplicate. Delete it.

VERDICT (re-check): FIX
VERDICT (re-check, final): PASS
