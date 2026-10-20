# Review W2-b: four programming-thinking shadow leaves

Reviewer: independent (did not write the leaves). Gate: L31 (reviewer PASS before `live`), L29 voice, hard rules, `docs/04` §8 checklist, `docs/06` §3.
Sources reached: kotlinlang.org (docs and spec), developer.android.com. Not reachable and not needed: play.kotlinlang.org, Wikipedia. No `kotlinc`, so nothing was run. Every output was re-traced by hand by me (see each leaf). No gradle was run.
Policy applied: a SIM marker with no built sim does not block (L31 ruling); the `<!-- SOURCES ... -->` comment at line 18 is stripped by `housekeeper` at go-live and is not a FAIL. The `<!-- SIM: id -->` marker stays.

## Summary

| Leaf | Verdict | Items |
|---|---|---|
| `yes-or-no-questions` | PASS | 0 FIX (optional notes only) |
| `how-a-program-decides` | FIX | 2 mechanical |
| `how-a-program-repeats` | FIX | 3 mechanical |
| `counting-loops` | FIX | 1 mechanical |

All FIX items are mechanical (`fixer` can do them). After they are applied, all four leaves are PASS with no re-trace needed, because no output or key changes. No owner decision blocks any leaf.

Checked and OK on all four: frontmatter keys and order; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/foundation/_roadmap.md` lines 77-96); `order` 13, 14, 15, 16 is correct (6 + 3 + 3 earlier leaves, then position in stem); `sims` equals the SIM marker (`[]`, `fork-and-gate`, `loop-track-gate`, `numbered-stairs-loop`, all listed in `docs/sims/needed-ids.md`); `playground: false` with every Predict `run: false` / `verified: not-run`, no Try it, no Fix the bug; headings in template order with exact names (yes-or-no has no See it move, the roadmap row says `none:` with a reason); summaries 18 to 22 words; prose 1,281 to 1,355 words (budget 900 to 1,600); one `kotlin` block each (limit 8); every kotlin block is a complete program; 3 Predict blocks each, 3 options, exactly one `correct`, a `why` on every option, YAML parses (checked with a parser); Recap is three lines under 20 words; no emoji, no non-ASCII, no `!` in prose, no video, capstone, previous/next or "coming soon" text; T4 banned words: zero hits except the two in the decides leaf below; every bold term is bold once; Notice, Guess and Build contain no Kotlin syntax; every "You can now" line has a matching Predict item; Android sections are reading-only and name a symptom.

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Boolean "represents logical values: true and false"; 0 and 1 are not Boolean; AND/OR/NOT wording; `&&` evaluated before `\|\|` | kotlinlang.org/docs/booleans.html | Matches |
| `if`: condition in parentheses, action in curly braces; `else` and `else if` for additional branches; sample with no `else` (`if (heightAlice < heightBob) taller = heightBob`) | kotlinlang.org/docs/control-flow.html | Matches |
| Only one branch of an `if` runs (and so "first yes wins" in an `else if` chain) | kotlinlang.org/spec/expressions.html, conditional expressions: "If the condition evaluates to true, the first branch ... is evaluated if it is present, otherwise the second branch" | Supported by the spec; the leaf's own SOURCES note says the control-flow page does not state it. Optionally add the spec URL to the trail. |
| `while` checks the condition, runs the body, returns to the check; garage sample is the same shape | control-flow page | Matches (three lines then "Garage is full!") |
| `var` declares variables that can be reassigned | kotlinlang.org/docs/basic-syntax.html | Matches |
| Range definition; default step 1; `1..4` is 1, 2, 3, 4; `..` includes both ends; `..<` excludes the end; `downTo` for reverse; `0..8 step 2` gives 02468 | kotlinlang.org/docs/ranges.html | Matches |
| `for (i in 1..6)` prints 123456 and `for (i in 1..<6)` prints 12345 | control-flow page | Matches; `1..<4` giving 1, 2, 3 follows from the second sample |
| Compose `Button` has `enabled`; "When false, this parameter makes the button appear unavailable and inactive." | developer.android.com/develop/ui/compose/components/button | Matches |
| `if (name.isNotEmpty()) { Text("Hello, $name!") ... }` hides the greeting when the name is empty | developer.android.com/develop/ui/compose/state | Matches |
| "Jetpack Compose is the modern toolkit for building Android UI" | developer.android.com/develop/ui/compose/documentation | Matches |
| ANR when the app "can't respond to user input on the main thread--also known as the UI thread"; no response to key press or tap within 5 seconds; "Don't perform blocking or long-running operations on the app's main thread." | developer.android.com/topic/performance/anrs/keep-your-app-responsive | Matches. The page does not mention endless loops; the leaf says so and labels the link as its own reasoning. Good. |
| `items(5)` with code comment "Add 5 items"; `messages.forEach` in a `Column`; no `for` loop on the page | developer.android.com/develop/ui/compose/lists | Matches (searched the page text for `for (`, no hit) |

Could not verify from an official page (language semantics or textbook level, accepted, none blocks): an iteration is "one run of the body"; the loop "check comes first" is true of `while` (see repeats item 3); "put the strictest sign first" is teaching advice, correct for nested thresholds; `==` versus `=` wording.

## Leaf 1: yes-or-no-questions: PASS

Hand-trace (mine, independent):
- Name it program: `age = 17`, `oldEnough = 17 >= 18 = false`; prints `false`; `false && true` = `false`; `false || true` = `true`; `!false` = `true`. Stated "false, false, true and true": correct.
- Comparison table: `20 > 18` true; `17 < 18` true; `18 >= 18` true; `19 <= 18` false; `18 == 18` true; `17 != 18` true. All six correct.
- AND/OR truth table: all four rows correct.
- `two-questions-joined`: `3 > 2` true, `2 > 5` false, AND gives `false`. Key b correct.
- `sign-says-18`: `17 > 16` is `true`. Key b correct, and the roadmap `F:` idea (a check that lets 17-year-olds in) is met as a Predict.
- `or-and-not`: `true || false` is `true`; `!true` is `false`. Key b correct.
- Compile: the Name it program compiles (complete `main`, Boolean values, no unused or unreachable code).

Optional notes (not FIX, owner choice):
- Line 27 says "cinema door" while The question uses a club door. Same idea; one place name would sound cleaner aloud.
- Line 33 ("A second turnstile in a row is a second question.") is never reused later. Harmless.
- Line 208: "In maths, one equals sign is both a statement and a question" is a loose claim. A safer wording: "In maths class one equals sign is the only one you use, so it feels the same."
- Line 214 gives two symptoms; only the second (button too early) and the exactly-18 case are explained below it. The first ("every box filled but the button stays grey") is not tied to a lesson part.

## Leaf 2: how-a-program-decides: FIX

Hand-trace (mine):
- Name it, `temp = 20`: `20 > 25` false, `20 > 15` true, prints `Warm`, then `Done`. Correct as stated.
- `temp-thirty`: `30 > 25` true, prints `Hot`, then `Done`. Key b correct.
- `signs-in-wrong-order`: `30 > 15` true, prints `Warm`, then `Done`; `Hot` is unreachable because `temp > 25` implies `temp > 15`. Key b correct.
- `fork-with-one-path`: `10 > 15` false, no else, prints `Done`. Key b correct.

FIX list:
1. Line 38 (Where the analogy stops). T4 banned word "just". Old: `Then the ball just rolls on when the answer is no.` New: `Then the ball rolls on when the answer is no.` (mechanical)
2. Line 197 (`fork-with-one-path`, option c `why`). T4 banned word "just". Old: `A fork with one path is allowed. The ball just rolls on.` New: `A fork with one path is allowed. The ball rolls on.` (mechanical)

Optional (not FIX): line 218 is 27 words. Split: `Its state page has an example. When the name is empty, the greeting is hidden, and the check sits in an `if` statement.` Line 216 "The screen is wrong because" could read "The screen shows this because" (T5 spirit).

## Leaf 3: how-a-program-repeats: FIX

Hand-trace (mine):
- Name it: `heat` 0, 1, 2 pass `heat < 3` (three `Stir`), `heat` 3 fails, then `Boiling`. Correct as stated. `heat = heat + 1` reassigns a `var`: compiles, no compiler warning.
- Notice: three stirs and four checks (the fourth is the one that says no). Correct.
- `how-many-stirs` (`heat < 4`): 0, 1, 2, 3 pass, so 4 stirs. Key b correct; option a (3) and c (5) are believable miscounts.
- `stove-never-lit`: `heat` stays 0, `0 < 3` always true, endless `Stir`, `Boiling` never prints. Key b correct. (Kotlin may hint that `heat` is never modified; the item is paper-only so this is not shown to the learner.)
- `at-most-four` (`heat <= 4`): 0, 1, 2, 3, 4 pass, so 5 stirs. Key b correct. "You can now" line 3 (one extra run) matches.
- Common mistake 2: `while (heat == 3)` at `heat = 0` is false at the first check, so the body never runs. Correct.

FIX list:
1. Line 37 (Where the analogy stops). The term "loop" is used twice before Name it defines and bolds it (line 86); Mental model must not use the Kotlin-side term (`docs/04` §4.2, §3.3, T2). Old: `A program has no stove. A loop only checks a number. Unless a step inside the loop changes that number, the check sees the same number every time, and the cook stirs for ever.` New: `A program has no stove. It only checks a number. Unless a step in the repeated part changes that number, the check sees the same number every time, and the cook stirs for ever.` (mechanical)
2. Line 44 (Try this, item 1). "The gate" is an object that is not in the Mental model, so the text breaks the one-analogy rule and does not make sense with the sim off (`docs/04` §3.2, §4.3). Old: `Press Step and watch the spoon go round once. The gate checks the thermometer, and the stove adds one mark.` New: `Press Step and watch the spoon go round once. Before the stir, the thermometer is checked, and the stove adds one mark.` (mechanical; `sim-engineer` may still draw a gate, which is the cook's question)
3. Line 196 (Tricks). Overgeneralised: a `do-while` loop runs its body before the check, and the control-flow page names both. Old: `The check comes first, so a loop can run zero times.` New: `The check comes first, so a `while` loop can run zero times.` (mechanical)

Optional (not FIX):
- Lines 82, 67: plain English "while" ("keeps stirring while the answer is yes", "for a while") sits in Build and Guess before the keyword appears. It reads as a good bridge and is not Kotlin syntax; left to the owner.
- Line 208: "This part is our own reasoning." `docs/04` T3 uses "I" for the author. Consider "This part is my own reasoning, not the documentation's." Same in counting-loops line 215.
- Sim note for `sim-engineer`: Try this items 2 and 3 say "press Run", but the one-line sim description in `docs/sims/needed-ids.md` names only Step. The built sim needs a Run control.

## Leaf 4: counting-loops: FIX

Hand-trace (mine):
- Name it program: `i` = 1, 2, 3, 4, then `Done`. Correct.
- Table: `1..4` gives 1, 2, 3, 4; `1..<4` gives 1, 2, 3; `4 downTo 1` gives 4, 3, 2, 1; `0..8 step 2` gives 0, 2, 4, 6, 8. All correct. "`1..10` hands out ten numbers, `1..<10` nine": correct.
- `last-number-printed` (`1..10`): last is 10. Key b correct.
- `sum-misses-five` (`1..<5`): i = 1, 2, 3, 4; total 1 + 2 + 3 + 4 = 10. Key b correct; option c (4) is the believable "last number" slip.
- `count-down-to-go`: `3 downTo 1` prints 3, 2, 1, then `Go`. Key b correct.
- Compile: `..<` is stable (Kotlin 1.9 and later); all programs are complete.

FIX list:
1. Line 36 (Where the analogy stops). The term "range" is used before Name it defines and bolds it (line 84). Old: `A range of numbers is only a rule that hands out one number at a time.` New: `A rule that hands out numbers gives you one number at a time.` (mechanical)

Optional (not FIX): line 36 also introduces "a landing beyond it", a small second image; "A real staircase has a top step you can see." says the same without it. Common mistake 3 (line 207) could add what `3..1` gives (no numbers at all); the ranges page does not state it, so leave it out unless a run confirms it.

## Cross-leaf notes (not FIX)

N1. Answer position. In all 12 Predict items the correct option is `b`. A learner can spot the pattern after one page. Reordering the options in about half the items would help; it changes only option order and `id` letters, not the content. Owner choice, mechanical if wanted.
N2. Roadmap `F:` and `B:` tasks. The roadmap rows promise a Fix the bug (and a Break) task for each leaf. With `playground: false` these are delivered as Predict items instead (`sign-says-18`, `signs-in-wrong-order`, `sum-misses-five`, `stove-never-lit`, `at-most-four`, `count-down-to-go`). That matches the rule in `docs/04` §2 and the accepted W1-b pattern. When Phase 6 (Playground) ships, `curriculum-designer` and `challenge-builder` may add real Try it and Fix the bug sections; slugs and challenge ids already in the files must not change.
N3. Sims. Three sims (`fork-and-gate`, `loop-track-gate`, `numbered-stairs-loop`) are only one-line entries in `docs/sims/needed-ids.md`. Per the L31 ruling this does not block. The plain text under each marker works with the sim switched off (after repeats FIX 2).
N4. Go-live step for `housekeeper`: strip the line-18 SOURCES comment from each leaf; keep the `<!-- SIM: ... -->` marker; set `status`, `publishedOn`, `updatedOn`. The comment holds the source trail; the table above records the quotes I verified.
