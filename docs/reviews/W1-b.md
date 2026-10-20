# Review W1-b: four Foundation shadow leaves

Reviewer: independent (did not write the leaves). Gate: L31 (reviewer PASS before `live`), L29 voice, hard rules, `docs/04` §8 checklist, `docs/06` §3 and §5.
Reachable sources used: developer.android.com (page text read with `curl` where the fetch tool truncated), kotlinlang.org. Not reachable: play.kotlinlang.org, api.kotlinlang.org, Wikipedia. No `kotlinc` on this box, so no Kotlin was run. No gradle was run.

## Summary

| Leaf | Content verdict | Live-eligible after the FIX list? |
|---|---|---|
| `foundation/how-computers-work/memory-vs-storage` | FIX (5 mechanical items) | No: sim `desk-and-cabinet` has no spec and no build (W-1) |
| `foundation/how-computers-work/what-is-an-os` | FIX (7 mechanical items) | No: sim `building-manager` has no spec and no build (W-1) |
| `foundation/first-code/what-is-code` | FIX (4 mechanical items); then PASS by reading | Yes |
| `foundation/first-code/run-your-first-program` | FIX (3 mechanical, 2 owner/environment) | No: four `UNVERIFIED` fields need a real run, and Phase 6 Playground is not built (W-2) |

Passed by hand-trace (my own second trace, which is what `not-run` records): every `run: false` Predict item in the first three leaves has exactly one correct option and the stated answer is right. Details per leaf below.

## Cross-leaf findings

W-1 (owner: `physics-designer`, then `sim-engineer`). `docs/04` §4.3 and P2: a live leaf has a working sim or no See it move section. Both sim ids are only one-line entries in `docs/sims/needed-ids.md`; no spec, no build. The roadmap rows for both leaves promise a sim, so deleting the section is not allowed (`docs/04` §4: omit only when the row says why). These two leaves stay `shadow` until the sims exist. The plain text under each SIM marker works with the sim off, so no wording change is needed for that.

W-2 (owner: environment). `docs/NEXT-STEPS.md`: no leaf with a runnable block goes live before Phase 6 (Playground). `run-your-first-program` is such a leaf.

W-3 (V12). Every `<!-- ... -->` comment other than the exact `<!-- SIM: id -->` line must be gone before `live` (`docs/04` §5.3). In each of the four leaves that is the single multi-line `<!-- ⚠️ VERIFY ... -->` comment at lines 18-19 (it starts at line 18 and ends at the `-->` on line 19). Keep the `<!-- SIM: ... -->` marker (memory-vs-storage line 39, what-is-an-os line 42). The comment holds the source trail, so the verified quotes are recorded in this file instead (see "Source trail" at the end). Stripping is a `housekeeper` step at go-live; do it after the FIX items, not before, so the trail stays in the file while fixes happen.

W-4 (owner: `curriculum-designer`). `content/foundation/_roadmap.md` line 229 carries a `⚠️ VERIFY` in the Notes cell of `run-your-first-program` ("Playground still embeds and runs without login on the publish date"). Roadmap changes go through `curriculum-designer` (hard rule 5). It cannot be tested from this box (play.kotlinlang.org and api.kotlinlang.org are blocked). Test in Phase 6 with a browser. The leaf body makes no login claim, so the leaf does not depend on it.

## Leaf 1: memory-vs-storage — FIX

Template, frontmatter order and values, roadmap equality (id, title, difficulty, requires, fruit, order 5), twelve headings minus Try it and Fix the bug (correct for `playground: false`), summary 24 words, one analogy plus "Where the analogy stops", Notice/Guess/Build/Name it order, three Predict items with 3 options each and `run: false` / `verified: not-run`, no banned words, no `!`, no emoji in the body, bold once per term, prose about 1,160 words: all OK.

Hand-trace of Predict items: `pull-the-plug` b is correct (desk empties, cabinet keeps C and X); `desk-is-full` b; `game-while-off` b. All three agree with the key.

FIX list:

1. Line 180. Misattributed source. Old: `Android's own notes on this add that, to the user, it looks as if the app crashed. The app often gets no chance to save, so progress is lost.` The LMK page says "To the end user, it looks like the app crashed" about a user-perceived kill (an app showing an activity or running a foreground service), not about an app kept out of sight. The same page does say "These terminations result in an immediate, non-graceful process exit" and "often bypassing standard lifecycle state-saving mechanisms and resulting in lost user progress". New: `Android's own notes on this say the app is ended at once, often without its normal save steps, so progress can be lost.` (mechanical)
2. Line 182. Overclaim. Old: `So a game's saved level survives a power cut, but not an uninstall.` New: `So a level the game keeps in its own storage survives a power cut, but not an uninstall.` (The data-storage page says media and documents in shared storage are not removed on uninstall.) (mechanical)
3. Line 170. Unsourced consumer-labelling claim. Old: `Both are counted in GB, and shop labels sometimes use the word "memory" for storage.` New: `Both are counted in GB, and people often say "memory" when they mean storage.` (mechanical)
4. Lines 186-188 (Recap). Template says one sentence per line; each of the three lines is two sentences. New:
   1. `Memory is the fast desk, and it forgets everything when the power goes off.`
   2. `Storage is the filing cabinet: slower, roomier, and it keeps files without power.`
   3. `Saving copies work from the desk to the cabinet, so unsaved work lives only on the desk.` (mechanical)
5. Line 194, You can now. `Sort everyday items on a phone into memory or storage.` has only one matching task (`game-while-off`). Old → new: `Sort an installed game and an unsaved message into memory or storage.` (matches the table and `game-while-off`). (mechanical)

Optional (not a FIX): `desk-is-full` states the answer in its own `code` text ("the system can end apps that are no longer on the screen"). It still teaches, but it gives the answer away. Owner choice.

Claims checked (see trail): "kills processes in the cache" when memory runs low; apps out of sight are shut down first; uninstall removes app-specific files. Cached processes and "no longer visible" activities support "keeping out of sight". OK.

Could not verify from reachable official sources (textbook level, do not block): RAM = random-access memory; RAM loses contents without power; storage keeps files without power; memory is faster and usually smaller than storage; apps are copied into memory while used. Secondary sources agree (tutorial and vendor glossaries only). Decision: accepted as general CS knowledge, not Kotlin/Android/KMP claims under hard rule 4. Strip VERIFY (1) with the comment.

VERIFY resolution: (1) accepted as above; (2) sim spec missing: see W-1, stays a blocker; (3) hand-traced by this reviewer, done.

## Leaf 2: what-is-an-os — FIX

Template, frontmatter, roadmap equality (order 6, requires both entries), headings, summary 23 words, bold once, Notice/Guess/Build without keywords, three Predict items (3 options each, `run: false`, `not-run`), no banned words: OK. Prose about 1,320 words (within 900-1,600).

Hand-trace: `who-gets-the-board` b (Chat asked at 9:02, before Camera at 9:03; Maps has finished); `no-manager` b (later write at spot 7 replaces the first); `ask-for-the-key` b (see item 3). Agrees with the key.

VERIFY (4) CAMERA: RESOLVED. `https://developer.android.com/reference/android/Manifest.permission` (read with curl; the fetch tool only returned the sidebar): `CAMERA ... Required to be able to access the camera device ... Protection level: dangerous`. The permissions overview says runtime permissions are also known as dangerous permissions, that "the system presents a runtime permission prompt", and that "the microphone and camera provide access to particularly sensitive information". So a system dialog for camera access is documented. But the docs also say a permission can be granted or denied automatically, a user who denies twice sees no dialog again, and taking photos needs no permission at all if the app hands off to another camera app. Hence items 3 and 4 below.

FIX list:

1. Line 38. Old: `Android lets apps share parts of memory that hold the same system code, but not the private working area of each app.` Source: memory-overview "Share memory" (Zygote shares framework code and resources across app processes); security-tips (other processes cannot access private data). New: `Android lets apps share the memory that holds the same system code and resources, but not each app's private data.` (mechanical; also not a sentence over 25 words)
2. Line 84. Old: `The Android documentation says that when an app starts and has nothing else running, the system starts a new process for it.` Doc: "When an application component starts and the application doesn't have any other components running, the Android system starts a new Linux process". New: `The Android documentation says that when part of an app starts and no other part of that app is running, the system starts a new process for it.` (mechanical)
3. Line 86. Overgeneralises (normal permissions are granted at install with no question). Old: `When an app needs something outside its own area, such as information about you, the OS asks you first. That is a **permission**.` New: `When an app needs something sensitive outside its own area, such as your camera or your contacts, Android asks you first. That question is a **permission** request.` (mechanical)
4. Line 161 (Predict `ask-for-the-key`, `code`). Scope the situation so the dialog is the true answer. Old: `A chat app wants to use your camera.` New: `A chat app asks to use your camera for the first time.` (mechanical)
5. Line 194. Same scope issue. Old: `...and an app that needs something outside it must ask.` New: `...and an app that wants something sensitive outside it has to ask.` Also split: put the sandbox definition in its own sentence (current sentence is 27 words). (mechanical)
6. Line 198. Unexplained jargon (L29: "framework", "library module"). Old: `The Android documentation also describes a layer between the framework and the hardware, with one library module for each kind of hardware, such as the camera. Apps reach the camera through the manager's layers, not directly.` New: `The Android documentation also describes a layer that sits between Android's app tools and the hardware, with one piece of code for each kind of hardware, such as the camera. Apps reach the camera through the manager's layers, not directly.` (mechanical)
7. Lines 20-22 (The question) are fine. Lines 188-190 Common mistakes use "doorway", a small second image. Old: `Next time, picture the manager standing in the doorway.` New: `Next time, picture the app asking the manager.` (mechanical, T analogy check)

Claims checked: Linux kernel foundation; HAL has one library module per hardware type such as camera (and Bluetooth); framework API call loads the module; every app runs in a limited-access sandbox; system permission dialog; other processes cannot access private data; process may be shut down for processes serving the user more immediately. All match the quotes in the trail. Unverified generic claims: OS manages hardware and shares it among programs; it starts before apps; Windows, macOS, Linux, Android are operating systems; kernel is the core. Secondary sources (Silberschatz et al., Tanenbaum definitions as quoted by search results) agree. Accepted as general knowledge. The Android boot-order claim ("starts first") is standard but no page was quoted.

VERIFY resolution: (1) accepted; (2) sim missing, W-1; (3) hand-traced; first two Predict items use a toy rule stated on the page, which is fine; (4) resolved above.

## Leaf 3: what-is-code — FIX, then PASS by reading

Template, frontmatter, roadmap equality (order 7, requires `what-is-a-cpu`), headings, summary 24 words, bold once, first screenful has `?`, no code, no bold term; prose about 1,010 words; no banned words: OK. Roadmap challenges (P: two lists, bread outside; B: remove a step) are present. `playground: false` with `run: false` / `not-run` Predict items and no Try it / Fix the bug: correct.

Code: the one `kotlin` block (lines 81-85) is character for character the Hello world program on `https://kotlinlang.org/docs/kotlin-tour-hello-world.html` (the page's version adds a trailing `// Hello, world!` comment). Checked by reading, not compiled (no kotlinc here). The leaf claims no run output, and the block is read-only, so reading is enough for this leaf. Statements in the text match the docs: `main()` is where the program starts; `println` prints its argument then ends the line ("Prints the given message and the line separator to the standard output stream"); string literals use double quotes; Kotlin is "a modern language" (getting-started); Kotlin is fully supported for Android (developer.android.com/kotlin).

Hand-trace: `bread-on-the-outside` A is correct (Slip B gives cheese, bread, bread, cheese outside); `one-step-missing` b; `robot-and-chese` b. Agrees with the key.

FIX list:

1. Line 79. Wrong promise. The next lesson runs `println("Hi")`, not this program. Old: `Here is a complete Kotlin program. You will run it in the next lesson. Do not worry about every word yet.` New: `Here is a complete Kotlin program. You will run one like it in the next lesson. Do not worry about every word yet.` (mechanical)
2. Line 61. Claim about how guessing is built is stronger than anything sourced. Old: `But the guessing was built in ahead of time, as exact steps written by people. The computer that runs it still follows exact steps.` New: `But the guessing is itself carried out by exact steps that were set up ahead of time. The computer that runs it still follows exact steps.` (mechanical)
3. Line 182. Same claim. Old: `But their guessing was built from exact steps too.` New: `But their guessing is carried out by exact steps too.` (mechanical)
4. Line 190. Overclaim. Old: `Android apps are written in code like the program above. The Android documentation says Kotlin is fully supported for Android development. So the exactness you met here is the same exactness behind every screen on a phone.` New: `Android apps can be written in Kotlin, the language of the program above. The Android documentation says Kotlin is fully supported for Android development. So the exactness you met here is the same exactness behind an app written in Kotlin.` (mechanical)

Minor (optional): line 26 sentence is 26 words. Split after "bread,".

VERIFY resolution: (1) hand-traced here; (2) compiled by reading against kotlinlang.org, no output claimed; Kotlin 2.4.21 run is not required because no `verified` field asserts a run; (3) generic claim that a computer has no unwritten knowledge: textbook, no official source; accepted, listed as unverified. After the four items and the strip of lines 18-19: PASS.

## Leaf 4: run-your-first-program — FIX (blocked)

Template, frontmatter, roadmap equality (order 8, requires `what-is-code`), headings (Try it and Fix the bug present, correct for `playground: true`), summary 22 words, bold once, Fix the bug has exactly 2 hints, `expected` equals `fix` output line for line, the five reviewer questions are present, the fix code is 10 lines (within 10-30), no AI-style tasks needed (stem has no `ai-review` flag), no compiler message is quoted: OK. Prose about 1,160 words.

Code reading (not run): `println("Hi")` prints `Hi`; `println("2 + 3")` prints `2 + 3`; the two-line program prints `Hello` then `Kotlin`; `printn(...)` fails to compile (unresolved name); the Fix the bug program has `"Step 1: write the code)` with no closing quote, which cannot compile (the grammar page does not exclude a newline inside a string, so even if a lexer let the string run on, the next `println("` would close it and leave stray tokens). The fixed program prints the 7 lines in `expected`. These readings are not a run.

FIX list:

1. [owner/environment] Lines 85, 108, 131, 165. All four `verified` fields are `"UNVERIFIED: compile not run"`. `docs/06` §5 and `docs/04` §8.4 forbid PASS and forbid `live`. `run: false` is not allowed here (`playground: true`, `docs/06` §3.1). Needed: someone with Kotlin 2.4.21 (kotlinc or the Playground at 2.4.21) runs these and records the result, then sets `verified: kotlin-2.4.21`:

   | Id | Run | Expected stdout |
   |---|---|---|
   | `print-hi` | as written | `Hi` |
   | `text-is-not-sum` | as written | `2 + 3` |
   | `two-lines` | as written | `Hello` / `Kotlin` |
   | `fix-missing-quote` | `code` as written | compile error (record it; do not quote the message in the lesson) |
   | `fix-missing-quote` | `fix` | the 7 lines in `expected` |
   | Try it step 3 | `printn` variant | compile error (unresolved name) |

   Then re-run this review. Not `fixer` work. (Note `docs/NEXT-STEPS.md` says only 2.4.0 and 2.4.20 were available; `verified` may say `kotlin-2.4.21` only if that version was used.)
2. [owner: `architect`, E7 in `docs/06` §9] Try it (lines 155-158) tells the learner to "Copy the program from the first Predict item into the editor", but the page has no editable editor at that point: Predict code is read-only and the only editable block is Fix the bug, which comes later. `docs/06` §1 says Try it code is the runnable block. Proposed edit (mechanical once E7 is answered): add a `kotlin` fence to Try it with the `println("Hi")` program (complete program, so the "Every kotlin block" rule holds) and change step 1. Old: `**Write.** Copy the program from the first Predict item into the editor. Change the text between the quote marks to `My first program ran`. Keep the quote marks.` New: `**Write.** In the editor below, change the text between the quote marks to `My first program ran`. Keep the quote marks.` Kotlin block count stays at 7 (limit 8).
3. Line 157. Unverified UI claim. Old: `The program does not run, and the editor shows an error message instead of your text.` New: `The program does not run, and you see an error message instead of your text.` (mechanical)
4. Line 66. Old: `The Kotlin documentation calls it an online application for writing, running and sharing Kotlin code in your browser, and says there is no need to create a new project and build it.` The "no need to create a new project and build it" sentence is the page's intro about quick ways to run Kotlin, not a Playground-only statement. New: `The Kotlin documentation calls it an online application for writing, running and sharing Kotlin code in your browser. The same page says that for quick checks there is no need to create a new project and build it.` (mechanical; also removes a 32-word sentence)
5. Lines 235-237 (Recap), lines 2 and 3 are two sentences each. New:
   2. `The reply is what the program prints, and text goes between double quotes.`
   3. `An error is a reply too, so fix the line it points to and run again.` (mechanical)

Optional (not a FIX): hint 1 (line 178) says "lines that start with Step"; the code lines start with `println`. Better: `Look at the lines whose text starts with Step. One of them looks different at the end.`

VERIFY resolution:
- (1) Not compiled or run: open, see item 1. Blocker.
- (2) Playground login and install claims: kotlinlang.org's run-code-snippets page calls Playground "an online application for writing, running, and sharing Kotlin code in your browser" and lists it among ways to run Kotlin with "no need to create a new project and build it". It says nothing about login, sign-in or installing. The leaf body claims no login and no account, and "nothing to install" follows from "in your browser". Safe as written. Do not add a sentence that says "no login" or "free" without a Phase 6 browser test (W-4).
- (3) "Run needs an internet connection" (line 37): follows from "online application" and from `docs/06` §1 ("Online-only"). Accepted. Keep.
- (4) Try it holds no code fence: see item 2.
- (5) Empty sim list: roadmap row says `none:` with the live Playground as the demo. Correct, no change.
- Android claims: Kotlin fully supported for Android; Logcat is a window in Android Studio showing "messages that you added to your app with the Log class, messages from services that run on Android, or system messages". The leaf's "messages that the developer added to the app, plus messages from the system" matches. Android Studio first appears in the last leaf of `how-an-app-is-born` per the roadmap; line 227's "the last lesson of this part of the course" is right if "this part" means Foundation. Optional clearer wording: `in the last Foundation lesson`.

## Source trail (replaces the VERIFY comments once they are stripped)

All read this session.
- https://developer.android.com/topic/performance/memory-overview: "As the system runs low on resources like memory, it kills processes in the cache."; "Each app process is forked from an existing process called Zygote ... most of the RAM pages allocated for framework code and resources to be shared across all app processes."
- https://developer.android.com/topic/performance/vitals/lmk: LMK "reacts to high memory pressure by terminating the least essential processes"; about user-perceived kills: "To the end user, it looks like the app crashed, often bypassing standard lifecycle state-saving mechanisms and resulting in lost user progress"; "These terminations result in an immediate, non-graceful process exit."
- https://developer.android.com/guide/components/processes-and-threads: "When an application component starts and the application doesn't have any other components running, the Android system starts a new Linux process ..."; "Android might decide to shut down a process at some point, when resources are required by other processes that are more immediately serving the user."; "it more readily shuts down a process hosting activities that are no longer visible on screen".
- https://developer.android.com/training/data-storage/app-specific: "When the user uninstalls your app, the files saved in app-specific storage are removed." https://developer.android.com/training/data-storage: media and documents are not removed on uninstall.
- https://developer.android.com/guide/platform: "The foundation of the Android platform is the Linux kernel."; HAL "consists of multiple library modules, each of which implements an interface for a specific type of hardware component, such as the camera or Bluetooth module."; "When a framework API makes a call to access device hardware, the Android system loads the library module for that hardware component."
- https://developer.android.com/privacy-and-security/security-tips: "Your application runs in a secure sandbox environment, so other processes on the system can't access your code or private data."
- https://developer.android.com/training/permissions/requesting: "Every Android app runs in a limited-access sandbox."; denied twice means no dialog again; "In certain situations, the permission might be denied automatically ... (A permission might be granted automatically as well.)"; apps should degrade gracefully on denial.
- https://developer.android.com/guide/topics/permissions/overview: install-time permissions "automatically grant[ed] ... when the user installs"; runtime permissions "also known as dangerous permissions"; "the system presents a runtime permission prompt"; "The microphone and camera provide access to particularly sensitive information"; "You can fulfill many use cases ... such as taking photos ... without needing to declare any permissions."
- https://developer.android.com/reference/android/Manifest.permission (read with curl): `CAMERA` "Required to be able to access the camera device ... Protection level: dangerous".
- https://developer.android.com/studio/debug/logcat: Logcat window in Android Studio shows "messages that you added to your app with the Log class, messages from services that run on Android, or system messages".
- https://developer.android.com/kotlin: "Kotlin is fully supported for Android development with tools and resources to help you succeed."; "Android's modern UI toolkit is built on Kotlin".
- https://kotlinlang.org/docs/run-code-snippets.html: Playground "is an online application for writing, running, and sharing Kotlin code in your browser"; "no need to create a new project and build it" (intro, quick ways in general); "Write the executable part in the body of the `main()` function." No login or install statement.
- https://kotlinlang.org/docs/kotlin-tour-hello-world.html: "`fun` is used to declare a function"; "The `main()` function is where your program starts from"; "`println()` and `print()` functions print their arguments to standard output"; the Hello world example. The page says nothing about misspelled names or missing quotes.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/println.html: "Prints the given message and the line separator to the standard output stream."
- https://kotlinlang.org/docs/strings.html: "To declare a `String` literal, enclose the value in double quotes." It does not say what happens to an unclosed literal.
- https://kotlinlang.org/docs/getting-started.html: "Kotlin is a modern language that's concise, multiplatform, and interoperable with Java and other languages."

## Claims I could not verify (all textbook level)

RAM stands for random-access memory; RAM is volatile; storage is non-volatile; memory is faster and usually smaller than storage; an OS manages hardware and shares it among programs; the OS starts before apps; a computer has no unwritten knowledge and follows one instruction at a time (taught in the earlier CPU leaf); consumer "memory" wording (edited out above). No official page reachable from this box states these. Secondary tutorial and vendor glossaries agree. Treated as general CS knowledge; a later pass with a textbook or NIST glossary is welcome but does not block.
