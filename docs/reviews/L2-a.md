# Review L2-a: eight layer-2 shadow leaves

Reviewer: independent. Date: 21 Oct 2026. Checklist: `docs/lesson-checklist.md`. Structure: `docs/04-lesson-system.md` sections 4 to 6 and `content/_templates/leaf.md`.

VERDICT: FIX (0 PASS, 8 FIX; all items mechanical except those marked owner/lesson-writer)

Build: `./gradlew :site:validateContent` printed `validateContent: ok`, BUILD SUCCESSFUL.

Snippets: every Kotlin block and every runnable challenge (`code`, `fix`, Try-it break/fix variants) was compiled and run with `bash scripts/kt-run.sh` (kotlinc 2.4.21). All compile and every stated output, `expected` and `output` matches stdout. Warnings: NOT verified. A control file with an obviously unused `val` also printed nothing through `kotlinc`, so this harness does not show warnings. The shadowing snippets (`total`, `best`) should show "Name shadowed" in an IDE (forum and DeepSource sources only, no official page found). Verified-true list (housekeeper: set `verified: kotlin-2.4.21` per docs/04 5.3 on every Kotlin challenge named below, including the `run: false` ones I ran): all `UNVERIFIED` and `not-run` stamps on Kotlin challenges in these 6 leaves are now true: who-starts (trace-the-stage-manager), first-call (trace-the-gate), three-jobs (new-rule-same-price, fix-two-rules-two-answers), why-blocking (missed-draws-in-480, name-the-wait-band, fix-file-read-on-main-line, Try it 8000 -> 500 and 8 -> 0), spotting (shadowed-best, look-alike-doors, which-line-stops, fix-swallowed-age, Try it NPE break), unit-test (ten-percent-off, the-test-nobody-runs, fix-the-unlisted-test, Try it 31 -> FAIL). `getOrDefault`, `getOrNull`, `firstOrNull`, `maxOrNull`, `Result.isSuccess` all exist and behave as stated. The shadowing code compiles. Sections, order, frontmatter (against each roadmap row), SIM markers and `needed-ids.md` entries are correct in all 8. Sim specs under docs/sims/ do not exist yet (known, not a leaf FIX).

Docs verified by fetch (all matched unless listed in a FIX): android fundamentals (no main()), intents-filters (MAIN+LAUNCHER paired, Home app, explicit intent only, onCreate), activity-lifecycle (onCreate once, savedInstanceState null), memory-overview (Zygote), processes-and-threads, vitals/anr (input 5 s, foreground broadcast 5 s, force-quit dialog; 5 s rule still current today), vitals/render (16/11/8 ms, bands 16-700, 700-5000, over 5 s, Choreographer), network-ops/connecting, security-config, topic/architecture, coroutines-best-practices, compose tutorial and mental-model, local-tests, test-in-android-studio, Git book branching / basic branching / rebasing, git-init, kotlinlang null-safety / run-catching / check / equality / first, and the security-config domain-config section.

Could not verify: the official wording of the "Name shadowed" warning and whether kotlinc 2.4.21 emits it (this harness hides warnings); that a missing INTERNET permission or blocked cleartext stops the call "before it leaves the phone" (no official page says it; consistent with general knowledge). The author-data exception names in interview-app-networking.md (`SecurityException`, `CleartextHttpTrafficNotPermittedException`) are not on any fetched page; the leaf rightly omits them.

docs/04 checks on all 8: the 13 headings are in order (Try it and Fix the bug only where playground is true, matching each leaf); Predict items have exactly 3 options and one correct; Fix has exactly 2 hints and the five reviewer questions; Recap is 3 lines each under 20 words; Common mistakes 2 to 4; You can now 2 to 4; Interview has 4 questions; kotlin blocks are far under 8. Failures are listed per leaf: Tricks over 4 bullets (composable, unit-test, branches), a Predict code block over 12 lines (who-starts), prose over 1,600 words (why-blocking, spotting, branches, unit-test; counted on prose outside code and challenge blocks, headings, tables and the interview section included; the budget predates the interview section, so the owner may prefer to relax it). Style nit, not blocking: docs/04 5.1 asks for one sentence in each wrong option's `why`, and many have two.

The "internal storage encrypted" style claim: none of the 8 leaves makes one. The only encryption statements are "HTTPS is HTTP sent in an encrypted form (general knowledge)" and the cleartext line, both fine.

---

## 1. android-core/what-is-an-app/who-starts-your-app: FIX

Known flags: the tap-to-system hop is labelled "my own reasoning" (OK). MAIN/LAUNCHER wording matches the docs ("must be paired together in order for the activity to appear in the app launcher"; leaf says missing icon, not "fails to open"): PASS.

1. Wording, optional (not a wrong key: Build step 5 and option c's `why` both say the screen exists before onCreate). Lines 117 to 142 (`what-exists-first`): "before the first screen exists" is ambiguous. If reworded, change line 128 to "Which of these must already be in place before the system creates your first screen?", option c text to "The onCreate call, which runs before the screen object exists", and its `why` to "You may have taken onCreate for a setup step that comes first. The system calls onCreate on a screen object that already exists, so the object comes before the call." Mechanical (fixer). Docs support option b ("starts the process ... and instantiates the classes").
2. Line 241 (interview, 4th answer) says "the main thread, the one worker that also runs your onCreate code" and "The documentation says ...": add "(general knowledge)" after the onCreate clause, and add `https://developer.android.com/guide/components/processes-and-threads` (blocked thread, no drawing events) to the SOURCES comment on line 18. Mechanical.
3. No hands-on task (no Try it, playground false). Tricks already has 4 bullets (the docs/04 limit), so REPLACE the last bullet with: "Task, in Android Studio: open AndroidManifest.xml in a new project and find the activity whose intent-filter holds the MAIN action and LAUNCHER category." Mechanical (checklist 13).
4. Optional, needs owner/writer: seed the author's common wrong belief ("onCreate is the very first code that runs") from interview-android-core.md in Common mistakes. It is unverified (Application.onCreate and content providers run earlier; check the App Startup docs first).
5. Lines 168 to 208 (`trace-the-stage-manager`): the code has 17 non-blank lines, and docs/04 5.1 allows 3 to 12. Replace the `code:` with this 12-line version, which I compiled and ran, and which prints the same three lines (No poster on the board / Poster shown, user taps / onCreate runs for Maps), so options and `output` stay: `fun systemStarts(frontDoor: String?) {`, `if (frontDoor == null) {`, `println("No poster on the board")`, `} else {`, `println("Poster shown, user taps")`, `println("onCreate runs for " + frontDoor)`, `}`, `}`, blank, `fun main() {`, `systemStarts(null)`, `systemStarts("Maps")`, `}`. The Name it program keeps the Screen class. Mechanical.
6. Prose is about 1,590 words, at the top of the 900 to 1,600 budget; do not add words.
7. Housekeeper: strip the three VERIFY marks in the SOURCES comment (compile now verified; tap hop stays labelled as own reasoning; sim shows a missing poster, matches needed-ids.md); update `verified:` on `trace-the-stage-manager`.

## 2. app-networking/requests-from-an-app/why-my-first-call-fails: FIX

Known flag "never leaves the phone": only from the roadmap title, no page states it.

1. Line 67 and line 183, and the SOURCES text: "opting in for any domain should be avoided" misreads the docs. The page says: "If your app needs to opt in to cleartext traffic to any domain, set cleartextTrafficPermitted="true" in the base-config. Note that this insecure configuration should be avoided whenever possible." That is the app-wide setting, not a one-domain opt-in. Change line 67's last sentence to "The documentation says the app-wide setting, which allows cleartext for every domain, is insecure and should be avoided whenever possible." Change line 183's sentence to "The documentation says the app-wide cleartext setting is insecure and should be avoided whenever possible." Mechanical.
2. Lines 22 and 188 state as fact that the call fails before reaching the server ("server's log shows nothing arriving"). Add "(general knowledge, not stated by the pages I read)" after the Question's second sentence, or in "Where this shows up" line 190 add: "My own reasoning: both gate checks happen on the phone before any request is sent." Mechanical (checklist 4).
3. Lines 103 to 113 (`which-address-is-allowed`). Option a text "because the page does not change" does not match its `why`; change option a text to "Address 1, because only the part after the domain matters". Option c's `why` mismatches ("extra words after the question mark make it secure"): replace with "You may have thought a longer address is treated differently. Both start with http, so both are open bags." Mechanical.
4. Lines 195 and 198 (interview): "cleartext is off by default from Android 9" must add "for apps that target API 28 or higher" (docs: base-config default for targeting 28+; apps targeting 24-27 default to true). Mechanical.
5. No hands-on task. Tricks already has 4 bullets, so REPLACE the weakest ("An error is a clue ...") with: "Task, in an Android project: remove the INTERNET permission line, run a call, and read the error in Logcat; then add the line back." (Seeded from build-exercise in interview-app-networking.md; keep wording free of exception names.) Mechanical.
5b. Scope wording, from the security-config fetch: the per-domain opt-in is `<domain-config cleartextTrafficPermitted="true">` with a `<domain includeSubdomains="true">` entry, so it covers a domain and optionally its subdomains, not one address. In the Mental-model-adjacent Build step 4 ("one plain address"), and in Common mistakes line 183 ("Open the gate for one address"), change "address" to "domain". Mechanical.
6. Jargon: "API 28" is unglossed; add "(the API level, a number for the Android version)" at first use on line 65. Mechanical.
7. Housekeeper: strip the three VERIFY marks (compile verified; exception names stay out).

## 3. architecture-data/why-architecture/three-jobs-in-an-app: FIX (minor)

Facts: UI layer / data layer / optional domain layer and separation-of-concerns quotes all match; business logic in data layer matches; the show/decide/fetch mapping is correctly labelled as own reasoning.

1. Checklist 6: both Predicts have the correct answer as c (`sort-six-lines` line 129, `new-rule-same-price` line 159). Move the correct option of `new-rule-same-price` to position a or b and relabel. Mechanical.
2. Housekeeper: strip the two VERIFY marks in the SOURCES comment (compile verified); update both `UNVERIFIED` stamps (lines 138 and 179): both snippets ran and match.

## 4. concurrency/why-the-main-thread/why-blocking-freezes-the-screen: FIX (minor)

Known flags: the 16 ms step model is labelled "my own round-number simplification" (OK). The 5 s input rule re-checked today on the ANR page: still "within 5 seconds"; foreground broadcast also 5 s; frozen band 700 ms to 5 s; slow band 16 to 700 ms. All table rows and interview facts match.

1. "main thread" is used from line 76 on with no definition (and "thread" is not glossed). In Name it add before line 76: "The **main thread** is the one thread, or worker, that draws your screen and runs your tap code (the documentation also calls it the UI thread)." Mechanical (checklist 3).
2. Optional: seed the author's build exercise (Thread.sleep(200), then 6000 ms behind a button) in Tricks as a Task bullet; Tricks has 4, so replace one. Mechanical.
2b. Prose is about 1,870 words against the 900 to 1,600 budget (docs/04 section 6). Owner/lesson-writer: trim about 270 words (longest: the analogy-stops paragraph, Build steps 4 to 6, the lock interview answer), or the owner relaxes the budget for leaves that carry the interview section.
3. Housekeeper: strip the four VERIFY marks (compile verified; 5 s rule re-checked; sim spec is a separate item); update the `UNVERIFIED` stamps on lines 108, 134 and 173.

## 5. kotlin/reading-code/spotting-risky-patterns: FIX (minor)

Facts: `!!`, `==`/`===`, data class equals, `runCatching` catches any Throwable, `first()` throws NoSuchElementException, coroutine caution box, `savedInstanceState` null on first launch: all match the docs. Shadowing code compiles and prints 0 as stated. The nine-line output matches.

1. Line 78 (table, shadowed row): "assign to the outer one without `val`" only works if the outer variable is a `var`; the example's outer is a `val`. Change to "Use a different name, or make the outer one a `var` and assign to it without `val`". Mechanical.
2. Line 81: `maxOrNull()` is the safe form of `max()`, not of `first()`. Change to "Use `firstOrNull()` or `getOrNull(0)`, and handle `null`. `maxOrNull()` does the same job for the biggest value." Mechanical.
3. Line 22 (Question) says "the compiler has nothing to say". Not safe: the compiler should warn "Name shadowed" for spot 2 (forum and DeepSource sources; not seen in an IDE here). Change line 22 to "the compiler accepts it", and in the Name it table, shadowed row, add "(the compiler may show a Name shadowed warning, general knowledge, and a warning is easy to miss)". Mechanical.
3b. Line 79 and line 313: "data class" is used without a gloss. Add at first use: "a **data class** is a Kotlin class that gets a content-based `equals` automatically". Mechanical (checklist 3).
3c. Prose is about 1,740 words against the 1,600 budget. Owner/lesson-writer: trim about 140 words (Mental model bullets, Common mistakes).
4. Housekeeper: strip the three VERIFY marks (shadowing compiles; the three stdlib calls are real and behave as stated); update the stamps on lines 127, 155 and 228 (the generated fix compiled and its expected output matches).

## 6. ui/first-composable/what-is-a-composable: FIX (minor)

0. Tricks to remember has 5 bullets (docs/04 4.8 allows 2 to 4): merge "Stamp the card, and then it may go on the shelf." with "Stamped can call stamped. Plain cannot call stamped.". Mechanical.

Facts: every quoted doc sentence matches (annotation, no return value, any order, not parallel, skip, compiler plugin, Text, tutorial and mental-model). Imports `androidx.compose.material3.Text` and `androidx.compose.runtime.Composable` match the tutorial. `// does not compile` stamps are correct. Keys c, b, a: varied.

1. **recomposition**, **idempotent** and **side-effect** are used (lines 216, 222, 227, 233, 236) and never defined. Add to Name it: "**Recomposition** is the process of calling your composable functions again when their inputs change (mental-model docs). A **side-effect** is any change that is visible to the rest of your app. **Idempotent** means that running the function again with the same input gives the same result (general knowledge)." Mechanical (checklist 3).

## 7. testing/unit-tests/run-your-first-unit-test: FIX (minor)

Facts: local test definition, `src/test`, `src/androidTest`, method bodies removed / "not mocked", `returnDefaultValues` as last resort, gutter Run icon, Run window, sync first: all match. `check` throws IllegalStateException: confirmed. The author's `RuntimeException`, mockk and Robolectric points are correctly left out.

0. Tricks to remember has 5 bullets (limit 4): merge "Local test folder ..." with "No marker, no run ...", or drop "Work out the expected answer by hand ...". Mechanical.
0b. Prose is about 1,655 words against the 1,600 budget; trim about 60 words. Owner/lesson-writer.
1. Line 77: "JUnit is the testing library that most Android projects use" is unlabelled. Add "(general knowledge)". Mechanical (checklist 4).
2. Line 82: "sync the project with Gradle first" uses Gradle without a gloss. Add "(Gradle is the tool that builds the project)". Mechanical.
3. Housekeeper: update the four `UNVERIFIED` stamps (all four ran and match, including the break and fix variants of Try it).

## 8. tools/git/branches: FIX (minor)

All quotes match the Git book and git-init docs. Keys b, c, a. Sim marker is single and listed. Author data used. No VERIFY marks to strip.

1. Tricks to remember has 5 bullets (limit 4): merge "Before you try an idea, make a note and move your thumb to it" with "Switch your thumb, and the room changes with it", or drop one. Mechanical.
2. Prose is about 1,700 words against the 1,600 budget; trim about 100 words (the analogy-stops paragraph, the rebase interview answer). Owner/lesson-writer.
3. Line 86: "pointer" is used without a gloss; add "(a label that points at one commit)". Mechanical.

---

Summary for the fixer: all items are mechanical except 1.4 and the word-budget items 4.2b, 5.3c, 7.0b and 8.2 (owner/lesson-writer). Where an item adds a Task bullet, it replaces a bullet so Tricks stays at 4 or fewer. Re-run kt-run.sh on any edited code. Re-run `./gradlew :site:validateContent` after edits.

## Fixes applied

Applied by fixer (21 Oct 2026). Word counts are approximate (prose outside code, comments and tables counted by `wc -w`).

- 1.1 who-starts: reworded the ambiguous question, option c text and why (done).
- 1.2 who-starts: "(general knowledge)" added after onCreate clause; processes-and-threads URL added to SOURCES (done).
- 1.3 who-starts: last Tricks bullet replaced with the Android Studio task (done).
- 1.4 who-starts: NOT DONE, needs owner/lesson-writer (App Startup check first).
- 1.5 who-starts: trace-the-stage-manager code replaced with the 12-line version; kt-run output matches (done).
- 1.6 who-starts: prose now about 1,595 words (under 1,600 budget); one sentence removed to get there.
- 1.7 who-starts: three VERIFY marks stripped; trace-the-stage-manager set to kotlin-2.4.21 (done).
- 2.1 first-call: "any domain" wording replaced with app-wide setting wording in lines 67, 183 and SOURCES (done).
- 2.2 first-call: "(general knowledge, not stated by the pages I read)" added to Question (done). Line 190 variant not added.
- 2.3 first-call: option a text and option c why reworded (done).
- 2.4 first-call: "for apps that target API 28 or higher" added in both interview answers (done).
- 2.5 first-call: weakest Tricks bullet replaced with Logcat task (done).
- 2.5b first-call: "address" changed to "domain" in Build step 4 and Common mistakes (done).
- 2.6 first-call: API level glossed at first use (done).
- 2.7 first-call: VERIFY marks stripped (done). trace-the-gate stamp left as not-run (code unchanged, not in the reviewer's list).
- 3.1 three-jobs: correct option of new-rule-same-price moved to a (done).
- 3.2 three-jobs: VERIFY marks stripped; both stamps set to kotlin-2.4.21 (done).
- 4.1 why-blocking: "main thread" defined before first use (done).
- 4.2 why-blocking: build exercise seeded as Tricks task; "screen does not crash" bullet removed (done).
- 4.2b why-blocking: NOT FULLY DONE. About 1,790 words, still about 190 over 1,600. Trimmed only repeats (about 40 words). Owner must decide whether to relax the budget.
- 4.3 why-blocking: VERIFY marks stripped; three stamps set to kotlin-2.4.21 (done).
- 5.1 spotting: shadowed row fix wording (done).
- 5.2 spotting: maxOrNull wording (done).
- 5.3 spotting: "compiler accepts it" and Name shadowed note added (done).
- 5.3b spotting: data class glossed at first use (done).
- 5.3c spotting: NOT FULLY DONE. About 1,720 words, still about 120 over. Owner decision needed (see 4.2b).
- 5.4 spotting: VERIFY marks stripped; four stamps set to kotlin-2.4.21 (done).
- 6.0 composable: Tricks merged to 4 bullets (done).
- 6.1 composable: recomposition, side-effect and idempotent defined in Name it (done).
- 7.0 unit-test: Tricks cut to 4 bullets (done).
- 7.0b unit-test: prose about 1,590 words, within budget (done).
- 7.1 unit-test: "(general knowledge)" added after JUnit (done).
- 7.2 unit-test: Gradle glossed (done).
- 7.3 unit-test: three stamps set to kotlin-2.4.21 (done). The report says four; only three UNVERIFIED stamps exist in the file.
- 8.1 branches: Tricks merged to 4 bullets (done).
- 8.2 branches: NOT FULLY DONE. About 1,640 words, still about 40 over. Rebase answer left intact per the Interview rule.
- 8.3 branches: "pointer" glossed (done).

Checks: `./gradlew :site:validateContent` printed validateContent: ok, BUILD SUCCESSFUL. `bash scripts/kt-run.sh` on the only changed Kotlin block (trace-the-stage-manager) printed the expected three lines. Other Kotlin blocks were unchanged in code.

## Re-check

Re-checked 21 Oct 2026 by the independent reviewer against `git diff` of the 8 leaves.

Build: `./gradlew :site:validateContent` in the working tree FAILS to compile `buildSrc`, because the untracked, unfinished `buildSrc/src/main/kotlin/dev/kotree/build/SimSpec.kt` (ADR 009 work, not a leaf) has compile errors. Not a leaf problem, but the fixer's earlier "ok" no longer reproduces until that file is fixed or removed. In a clean copy of the tree without `SimSpec.kt`, `validateContent: ok`, BUILD SUCCESSFUL.

Kotlin: the only changed code block is `trace-the-stage-manager` (who-starts). It compiled with `bash scripts/kt-run.sh` and prints the three stated lines; it has 12 non-blank lines. All other code blocks are unchanged in the diff, so the earlier compile results stand. VERIFY marks and `UNVERIFIED` stamps are gone from all 8; remaining `not-run` stamps are on text-only Predicts.

Word counts (my script, prose outside code, comments and headings): who-starts 1,542; first-call 1,302; three-jobs 1,412; why-blocking 1,728 (8% over); spotting 1,666 (4% over); unit-test 1,540; branches 1,583; composable 1,555. The three known overages are inside 15%, so they pass. Tricks has 4 bullets in all 8. Merged Tricks bullets are long but legal.

1. who-starts-your-app: PASS. Fixes 1.1, 1.2, 1.3, 1.5, 1.7 resolved. 1.4 (seed a common mistake, needs App Startup check) stays open as optional owner work.
2. why-my-first-call-fails: FIX (one word). Items 2.1, 2.2, 2.3 (text), 2.4, 2.5, 2.5b, 2.6, 2.7 resolved, but option a of `which-address-is-allowed` now reads "only the part after the domain matters" while its `why` still says "only the part after the address matters". Change the `why` to "...after the domain matters". Mechanical.
3. three-jobs-in-an-app: PASS. Correct options are now c and a; stamps and VERIFY marks done.
4. why-blocking-freezes-the-screen: FIX (minor). "Main thread" is now defined and the Task bullet is present, but the trim removed the only definition of "tick" ("A tick is a tiny slice of time.") from the Mental model, and "ticks" is used from line 34 on. Restore a short gloss at first use, about 8 words (still within 15% of the budget). Mechanical.
5. spotting-risky-patterns: PASS. 5.1, 5.2, 5.3, 5.3b resolved; the nine-line Try it matches the stated output.
6. what-is-a-composable: PASS. Tricks is 4; recomposition, side-effect and idempotent are defined in Name it.
7. run-your-first-unit-test: PASS. Tricks is 4; "(general knowledge)" and the Gradle gloss are present.
8. branches: PASS. Tricks is 4; "pointer" glossed; prose under budget.

Not re-verified: official wording of the "Name shadowed" warning (unchanged, labelled general knowledge). Unfixed optional: 2.2 line-190 variant (not needed, the Question is now labelled).

VERDICT (re-check, final): FIX
- why-my-first-call-fails.md, `which-address-is-allowed` option a `why`: "after the address" to "after the domain" (fixer).
- why-blocking-freezes-the-screen.md, Mental model line 34: restore a one-sentence gloss of "tick" (fixer).
- Outside the leaves: repair or remove `buildSrc/.../SimSpec.kt` so `:site:validateContent` runs in the working tree (owner of ADR 009).

## Re-check (hand fixes)

Re-checked 21 Oct 2026. Skipped `validateContent` (sims file mid-rewrite).
- why-my-first-call-fails: option a of `which-address-is-allowed` now has text and `why` both saying "domain". Resolved.
- why-blocking-freezes-the-screen: "A tick is a tiny slice of time on the shop clock." is restored at line 34, before the other uses of "ticks". Resolved.

VERDICT (re-check, final): PASS
