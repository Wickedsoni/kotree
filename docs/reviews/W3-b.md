# Review W3-b: four shadow leaves (trace by hand, debugging mindset)

Date 20 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31. Voice: L29. Nothing in the leaves was edited. No gradle run (as instructed). No `kotlinc`: every output and every Predict key was re-traced by hand by me.
Policy applied: a SIM marker with no built sim does not block; the `<!-- SOURCES -->` comment at line 18 is `housekeeper`'s.

VERDICT: FIX (1 PASS, 3 FIX; every FIX is mechanical, `fixer` can do it; no owner decision blocks a leaf)

| Leaf | Verdict |
|---|---|
| `foundation/programming-thinking/trace-code-by-hand` | FIX (2 items) |
| `foundation/debugging-mindset/reading-error-messages` | FIX (3 items) |
| `foundation/debugging-mindset/what-is-a-bug` | PASS |
| `foundation/debugging-mindset/find-a-bug-without-guessing` | FIX (3 items) |

## Ruling: sim change in find-a-bug-without-guessing (hard rule 5)

Not a violation. Rule 5 protects the tree structure: leaf ids, titles, difficulty, `requires`, `fruit` (V5). All of those equal the roadmap rows (checked for all four leaves, `content/foundation/_roadmap.md` lines 117-145). The `_roadmap.md` file was not touched (git status clean). The leaf follows the row's own Analogy cell ("Finding a blown fuse by switching rooms off one half at a time"); only the row's Sim cell says pipes and check-valves, so the row contradicts itself. `docs/04` §3.2 and §3.4 (one analogy; the sim only shows what the text says) make the fuse reading the binding one. The sim keeps the roadmap's mechanic: 16 segments, one hidden fault, learner halves, counts checks, 4 tries. `docs/sims/needed-ids.md` line 15 already records the divergence. No FIX. Follow-up for `curriculum-designer` (not blocking, not a `fixer` job): reword the Sim cell on roadmap line 259 to fuse and rooms so the row stops contradicting itself. It should land before `physics-designer` picks up the sim.

## Hand-traces (mine)

trace-code-by-hand
- Notice table: total 0, 2, 6, 12; said aloud 12. Correct.
- Name it program: `triple(2)` = 6; `coins` 1 + 6 = 7; prints `7`. Line numbers (`fun triple` 1, `return` 2, `fun main` 5, then 6, 7, 8) and all 5 table rows correct. Compiles, no warnings.
- `loop-and-function-total`: double(1..3) = 2, 4, 6; total 2, 6, 12; prints 12. Key c correct.
- `while-trace-two-purses`: left 10, 7, 4; rounds 0, 1, 2; `4 > 4` false; prints 2 then 4. Key a correct. Option b "3, then 1" is exactly what a third round would print.
- `purse-reset-in-the-round`: total reset each round; prints 1, 2, 3. Key b correct.

reading-error-messages
- `nme` program: `println` is line 3 counting from `fun main`. Correct.
- `fix-the-named-word`: `println(lst)` is line 5; after the fix prints Ada, Lovelace. Key a correct.
- `lines-before-the-crash`: prints `Start`, then `brew(0)` throws; `End` never runs. Key c correct. (The exception text goes to stderr, so stdout is `Start`.)
- `types-do-not-match`: line 2 is the bad line; fixed `age = 12`, prints 13. Key b correct.
- Predict items 1 and 3 show code that does not compile as written; `output` is the stdout of the fixed program, and the code comment says so. Accepted as a design choice (answers are hand-traced, `run: false`).

what-is-a-bug
- 3 + 4 = 7; 3 * 4 = 12. `pens + coinsEach` = 9 (key c). (10 + 20 + 30) / 2 = 30 (key a). `age > 18` with 18 is false, prints `child` (key b). All correct; all compile.

find-a-bug-without-guessing
- Name it: coins 5, 10, prints `check: coins is 10`, then 7. Correct.
- `print-inside-the-loop`: `1..<4` gives 1, 2, 3; totals 5, 10, 15; four output lines as in the block. Key b correct.
- `halving-checks`: 16, 8, 4, 2, 1 = 4 tries. Key c correct.
- `middle-check-clears-half`: 2 * 3 = 6; + 4 = 10 (middle: 10); - 5 = 5; 5 / 2 = 2 (Int); prints `middle: 10`, `end: 2`. Key a correct.

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Declare with `fun`, parameters in parentheses, `name: Type`, return type; `double(x: Int)` sample | kotlinlang.org/docs/functions.html | Matches |
| "When an exception is thrown, it interrupts the normal execution of the program"; throwing indicates an unexpected runtime error | kotlinlang.org/docs/exceptions.html | Matches (the leaf paraphrases the second, fine) |
| Stack trace = report of the sequence of function calls leading to the point of error; `throw ArithmeticException("This is an arithmetic exception!")` sample; output `Exception in thread "main" java.lang.ArithmeticException: This is an arithmetic exception!`, `at MainKt.main(Main.kt:3)`, `at MainKt.main(Main.kt)` | same | Matches. See reading FIX 3: line 3 is the throw line in the documentation's file, not in the leaf's snippet |
| "Unresolved reference: itemsRepository"; fix is to pass `ItemsRepository` into the view model; "Notice the type mismatch error" | developer.android.com/codelabs/basic-android-kotlin-compose-update-data-room | Matches |
| "A stack trace generates when your app crashes because of an error or an exception"; "a list of method calls that lead to the exception being thrown, together with the filenames and line numbers where the calls happened"; click the highlighted filenames to open the source | developer.android.com/studio/debug/stacktraces | Matches |
| Line breakpoint pauses the app at a line; while paused you can examine variables and continue line by line; Log messages "help you understand the execution flow" and "can also tell you what part of your app failed"; "Remove debug log messages and stack trace print calls ... when you are ready to publish your app" | developer.android.com/studio/debug | Matches |
| `println` prints its arguments and adds a line break; `"a is $a"` template inserts the value | kotlinlang.org/docs/basic-syntax.html | Matches |
| "State in an app is any value that can change over time" | developer.android.com/develop/ui/compose/state | Matches (re-fetched in this review) |
| `..<` excludes the end; integer division drops the fraction | ranges / numbers pages | Carried over from W2-a and W2-b, not re-fetched |

Could not verify from an official page (textbook or own reasoning, accepted, none blocks): the three kinds of failure and the word "bug"; tracing with a table; halving a search (binary search idea); "print debugging" as a name; "a compile error appears before the program runs" as a general rule; that the documentation's `Main.kt:3` counts a sample-marker comment line (inferred, see reading FIX 3). Quoted error text in reading-error-messages is traceable to the pages above, and the leaf says "wording varies by Kotlin version"; note `docs/04` §5.4 says not to copy error text that was not produced by a run, so the owner may want to reconcile that rule with the L31 instruction.

Checked and OK on all four: frontmatter keys, order and values; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows; `order` 21, 22, 23, 24 is correct; `sims` equals SIM markers (`trace-table-stepper`, `[]`, `[]`, `fuse-halving-search`; both ids are in `docs/sims/needed-ids.md`); `playground: false` with `run: false` and `verified: not-run`, no Try it or Fix the bug; headings in template order; all challenge blocks parse as YAML, 3 options each, exactly one `correct`, a `why` on every option; Question 4, 4, 4, 5 sentences, no code, no bold; Recap three lines under 20 words, one sentence each; prose 1,414 / 1,255 / 1,138 / 1,139 words (budget 900 to 1,600); T4 banned words, exclamation marks, non-ASCII, emoji, VERIFY, video, capstone, next-lesson promises: none in prose (the two `!` hits in reading-error-messages lines 90 and 99 are inside the documentation's quoted sample); every bold term appears once; Notice, Guess and Build contain no Kotlin syntax; Android sections are reading-only and name a symptom; "Where the analogy stops" present in all four; every "You can now" line has a matching task; correct option letters vary (c, a, b / a, c, b / c, a, b / b, c, a).

## Leaf 1: trace-code-by-hand: FIX

1. `content/foundation/programming-thinking/trace-code-by-hand.md`, line 33 (Mental model, detour bullet). The "helper" is used in Notice, Guess, Build, three `why` lines and Common mistakes but is never named in the Mental model (`docs/04` §4.2: every analogy object named once; §7: one word per thing). Old: `- Sometimes the map says "follow the trail called double, and carry this number". That is a detour. You walk the detour, carry one number back, and return to the cross you left.` New: `- Sometimes the map says "take this number to the helper on the trail called double". That trail is a detour. You walk the detour, carry one number back from the helper, and return to the cross you left.` (mechanical)
2. Same file, after line 33, new bullet (the "said aloud" column is used from Notice on but is never mapped to printing). New line: `- When the map says to say a number aloud, the program prints it. What gets said aloud is the output.` (mechanical)
3. Lines 244 and 246 (You can now). The page says "repeat" everywhere and never defines "loop" (`docs/04` §7). Old 244: `- Predict the output of a short program with a loop and a function by filling in a trace table.` New: `- Predict the output of a short program that repeats lines and calls a function by filling in a trace table.` Old 246: `- Spot a variable that a loop resets in the wrong place.` New: `- Spot a variable that a repeat resets in the wrong place.` (mechanical)

Optional (not FIX): the Notice table already shows 6 after round 2, so Guess is answered before the learner guesses; blank or drop the round 2 row in Notice if wanted. Line 90 says "parentheses" and then "brackets"; pick one word (the docs say parentheses).

## Leaf 2: reading-error-messages: FIX

1. `content/foundation/debugging-mindset/reading-error-messages.md`, line 126 (`fix-the-named-word`, option a `why`). "purse" is an object from another lesson's analogy (`docs/04` §3.2 rule 4), and the correct `why` should speak the dashboard analogy. Old: `why: "The message named lst on line 5. The purse that holds the last name is called last. With that fixed, both lines run."` New: `why: "The message pointed at lst on line 5, like a light pointing at one page of the manual. The name that holds the last name is last. With that fixed, both lines run."` (mechanical)
2. Lines 61, 199, 224. The test "did it print anything first" is wrong for a crash that happens before any print (the documentation's own `ArithmeticException` sample prints nothing first), so a learner would call it a compile error. Make the test "did the program start".
   - Line 61 old: `1. Ask when the message appeared. Was it before the program started, or after it printed something?` New: `1. Ask when the message appeared. Was it before the program started, or after it started running?`
   - Line 199 old: `- Before or after "Start"? That tells you which kind of message it is.` New: `- Did the program start running? That tells you which kind of message it is.`
   - Line 224 old: `- Tell a compile error from a crash by whether the program printed anything first.` New: `- Tell a compile error from a crash by whether the program started running.`
   (mechanical)
3. Line 94 and before line 102. Line 94 says "The documentation shows this message for it" but the message is never shown, only its parts in the table. The table's `Main.kt:3` is the line in the documentation's file, but in the snippet above it the `throw` is on line 2, so a learner counting lines finds `}`. Line 94 old: `The documentation shows this message for it.` New: `The documentation's sample output for it has the parts in the table below (wording varies by Kotlin version).` Insert before line 102 (`Read the first line first.`): `In the documentation, the throw is on line 3, so its message says Main.kt:3. In the snippet above, the throw is on line 2, so your own run should point at line 2. If you see `at` lines, the program was running when it crashed.` (mechanical; the last sentence gives a learner a cue they can observe when the program printed nothing before the crash)

Optional (not FIX), analogy language in `why` lines: `docs/04` §5.1 asks the correct `why` to speak the analogy. Besides the one fixed in item 1, reading items 2 and 3 and find-a-bug item 1 (`print-inside-the-loop`) state the rule without the analogy words. Left optional for all of them; fine to leave while the explanations are plain and true.

Optional (not FIX): the "key turns" versus "while driving" light pair in Mental model is never tied back to compile error versus exception in Name it; one sentence there would use the analogy again. "Crash" in the Predict and Recap is never tied to "exception" (add "(an exception)" at first use if wanted). Line 72 counts "from `fun main`"; the `// does not compile` comment above it is line 1 of the block, so some learners will count 4.

## Leaf 3: what-is-a-bug: PASS

No FIX items. Checked: roadmap match; analogy objects (card, cook, salt, dish, taster) named in Mental model and reused in Notice, Build, Predict `why` lines, Tricks and Common mistakes; terms compile error, exception, stack trace, logic bug and bug bold once and defined in the same sentence; T5 (the word "wrong" describes code and outputs, never the learner, same reading as W2-b); the Android section says "This part is our own reasoning", and its stack-trace quote matches the page.
Optional (not FIX): "It prints a **stack trace**" (line 68) is loose, since the runtime prints it; fine for this level.

## Leaf 4: find-a-bug-without-guessing: FIX

1. `content/foundation/debugging-mindset/find-a-bug-without-guessing.md`, line 87 (Name it). The string template `$coins` (also `$item`, `$total`, `$value` in the Predict items) is used but never explained, and no earlier Foundation leaf teaches it (searched `content/foundation`). Old: `A label in the text tells you which check you are reading.` New: `A label in the text tells you which check you are reading. Inside the quotes, `$coins` is a **string template**: Kotlin swaps it for the current value of `coins`.` (mechanical)
2. Lines 123, 127, 160, 228. "range" and "loop" are used but never defined or bolded on this page (`docs/04` §7). Use plain words.
   - Line 123 old: `The range stops one below 4, so the loop runs for items 1, 2 and 3 only.` New: `The `..<` sign stops one below 4, so the repeated part runs for items 1, 2 and 3 only.`
   - Line 127 old: `The print inside the loop shows 5, 10 and 15 for items 1 to 3. Item 4 is never visited, so the bug is in the range.` New: `The print inside the repeated part shows 5, 10 and 15 for items 1 to 3. Item 4 is never visited, so the bug is in the `1..<4` part.`
   - Line 160 old: `The loop keeps going until one room is left.` New: `The repeated part keeps going until one room is left.`
   - Line 228 old: `- Predict what a print line inside a loop shows, and use it to spot a loop that stops one round short.` New: `- Predict what a print line inside a repeated part shows, and use it to spot a repeat that stops one round short.`
   (mechanical)
3. Lines 47 and 73. `docs/04` §7: prefer a number over "many". Line 47 old: `while testing one room at a time can take many more.` New: `while testing one room at a time can take up to 15.` Line 73 old: `Idea C will find it, but it can need many tries.` New: `Idea C will find it, but it can need up to 15 tries.` (One at a time, rooms 1 to 15 clear, and room 16 is then known by elimination, the same counting the page uses for 4 tries.) (mechanical)

Optional (not FIX): the Notice table already shows the halving numbers, so Guess is answered before the learner guesses. Line 160 option b `why` says "stopped after the first halving", but 8 is the rooms value, not tries; "You may have read the value of rooms after one halving. The program prints tries." is closer. Line 193 option c `why` explains 6 but not the `end: 0` half of the option. Line 218 is one sentence of about 35 words with two quotes (T7); split after "check".

## Cross-leaf notes

- Sims `trace-table-stepper` and `fuse-halving-search` are only one-line entries in `docs/sims/needed-ids.md`. Not blocking (L31 ruling). The text under each marker works with the sim off.
- Roadmap `F:` and `F(ai):` tasks are delivered as Predict items because `playground: false` (same pattern as W2-b N2).
- Go-live step for `housekeeper`: strip the line-18 SOURCES comment from each leaf, keep the SIM markers, set `status`, `publishedOn`, `updatedOn`.

## After the FIX items

Re-check only that the edited lines match the New text. No answer key changes. Leaf 3 may go live as is. Leaves 1, 2 and 4 become PASS once their FIX items are applied.

## Re-check (20 Oct 2026)

Method: grep and read of every edited line against the New text above. No leaf edited; SOURCES comments left to `housekeeper`. No answer keys, output blocks or `why` correctness changed. W3-a fixes were already committed (7100586); W3-b fixes are in the working tree.

- `trace-code-by-hand`: PASS. Items 1, 2, 3 match the New text (lines 33, 34, 244, 246). "helper" and "output" now mapped before use. No new jargon; no sentence over the limit.
- `reading-error-messages`: PASS. Item 1 (line 128, "purse" removed, dashboard wording), item 2 (lines 61, 201, 226, "did the program start"), item 3 (line 94 and new line 102) match. The new paragraph is three short sentences; line 2 matches the snippet.
- `find-a-bug-without-guessing`: PASS. Item 1 (string template bolded and defined, line 87), item 2 (lines 123, 127, 160, 228; no "loop"/"range" left in prose, only the id `print-inside-the-loop`), item 3 (lines 47, 73: "up to 15") match.
- W3-a `loop-over-a-list`: PASS (lines 90, 105 match). `what-is-a-function`: PASS (lines 84, 209 match; composable functions and annotation bold once). `function-inputs-and-outputs`: PASS (lines 96, 208, 216 match; Jetpack Compose explained before use).

Remaining fixes: none. Optional items from the first review are unchanged and still optional.
