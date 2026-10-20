# Review W5-b: seven shadow leaves (number conversions, control flow, functions)

Date 20 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31/L33. Voice: L29. Nothing in the leaves was edited.
Checked against `docs/lesson-checklist.md` (all 12 items; item 12 is DSA/DAA/OS only, n/a), `content/_templates/leaf.md`, `docs/04`, hard rules, `content/kotlin/_roadmap.md` rows, and kotlinlang.org / developer.android.com pages read 20 Oct 2026.

VERDICT: FIX (0 PASS, 7 FIX)

Every leaf fails on one shared item (no interview section, checklist item 11). Everything else is small and mostly mechanical, except `declaring-functions`, which has a real accuracy problem (the Unit exception).

| Leaf | Verdict |
|---|---|
| `kotlin/types-and-variables/number-conversions` | FIX (5 items) |
| `kotlin/control-flow/if-as-an-expression` | FIX (3 items) |
| `kotlin/control-flow/when-expression` | FIX (6 items) |
| `kotlin/control-flow/ranges-and-for-loops` | FIX (2 items) |
| `kotlin/control-flow/break-and-continue` | FIX (2 items) |
| `kotlin/functions/declaring-functions` | FIX (10 items) |
| `kotlin/functions/default-and-named-arguments` | FIX (2 items) |

## How I checked code (better than hand-tracing only)

A Kotlin compiler was available: `kotlin-compiler-embeddable-2.3.21` from the Gradle distribution (K2, not the pinned 2.4.21 of `docs/07` §1, so the leaves keep `verified: not-run`). I extracted every `kotlin` block and every Predict `code` block (24 programs) and compiled and ran them. Results:
- All 19 programs not marked `// does not compile` compile with no warnings, and every Predict `output` equals the real stdout byte for byte. Every `correct: true` option matches its `output`.
- All 5 `// does not compile` blocks really fail: number-conversions ("initializer type mismatch: expected 'Long', actual 'Int'"), if ("'if' must have both main and 'else' branches when used as an expression"), when ("'when' expression must be exhaustive. Add an 'else' branch."), declaring-functions ("missing return statement"), default-and-named ("no value passed for parameter 'drink'").
- Key letters vary in every leaf (b/c/a, b/c/a, c/a/b, b/c/a, c/b/a, c/a/b... no leaf uses one letter three times).
- Extra facts I ran for the interview sections below: `5..1` is empty; `1..9 step 0` throws `IllegalArgumentException: Step must be positive, was: 0.`; a default value is evaluated on every call that leaves it out; a default can use an earlier parameter; `3000000000L.toInt()` is `-1294967296`; non-local `break` and `continue` inside a `forEach` lambda compile only when a loop encloses the `forEach` call (at top level they fail with "'break' and 'continue' are only allowed inside loops").

`./gradlew :site:validateContent` (run once): `validateContent: ok`, BUILD SUCCESSFUL, exit 0. No VERIFY marks, no "next lesson" text, no "Coming next" in any of the seven leaves.

## Checked and OK on all seven

Frontmatter keys and order; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/kotlin/_roadmap.md` lines 43-85); `order` 7, 8, 9, 10, 11, 12, 13 is correct (3 first-programs + 4 types leaves, then position); `status: shadow`; `video: ""`; `playground: false` with every Predict `run: false` and `verified: not-run` (accepted W2/W3 pattern; the roadmap `F:` and `B:` tasks are delivered as Predict items); twelve-heading order minus Try it and Fix the bug; one SIM marker equal to `sims` where a sim exists, none where `sims: []`; all four sim ids are in `docs/sims/needed-ids.md`; the SOURCES comment sits at line 18; The-question blocks are 4 or 5 sentences; Recaps are three one-sentence lines under 20 words; Predict sections have 3 options and one key; first use of Name-it terms is bold.

## Rulings

1. Sim divergences (`tape-measure-range`, `letter-stack-gates`; also `sorting-chutes` letters instead of a ball, and `overflow-cups` wrap instead of spill): **acceptable, not a rule 5 violation.** Rule 5 protects tree structure (id, title, difficulty, requires, fruit; build rule V5), and all seven leaves equal their rows. The roadmap Sim cells are notes. The roadmap contradicts itself on these two rows: the Mental-model cell says tape measure / stack of letters, but the Sim cell says reuse the staircase / spoon-and-pot sim, which would force a second analogy and break L29 and `docs/04` §3.2. The leaf analogy is the binding one (`docs/04` §3.4: the sim shows only what the text says), and the divergences are already recorded in `docs/sims/needed-ids.md`. Same ruling as W4-b. Follow-up, owner level, not blocking: `curriculum-designer` rewords the Sim cells of roadmap lines 436 to 438 (and 427 for "spill") so `sim-engineer` builds to the leaf text.
2. Interview section (checklist item 11) is **missing from all seven leaves**, and the rule has a gap: the checklist says "each leaf ends with the questions an interviewer asks ... ('Say it aloud')" but `docs/04` §4 and `content/_templates/leaf.md` fix twelve headings and have no such heading; no validator or renderer checks body headings (I searched `buildSrc` and `site` for every section name; only test fixture file names matched; rules V1 to V11 in `docs/14` §3.1 cover registry, slugs, `requires`, ids, roadmap equality and tables only; `Validate.kt` skips `_templates`), so adding one will not fail the build. **Owner decision needed once, for all leaves:** the heading name and position. My recommendation: a thirteenth heading `## Say it aloud` placed last (after "You can now"), 2 to 4 interviewer questions each with a model answer of at most 4 short sentences in plain words, labelled per checklist item 4 where it is own reasoning or general knowledge. `architect` then updates `docs/04` §4 (it says "twelve" in the §4 title and in the line below it), `content/_templates/leaf.md` and the `docs/04` §10 worked example; no code change is needed. Until the owner decides, `lesson-writer` can already write the content under that heading. Suggested questions per leaf (facts verified by me as noted) are in each FIX 1 below.
3. Guard conditions: the roadmap Notes for `when-expression` say "mention only". The leaf mentions them only in the SOURCES comment (stripped at go-live), so the body never does. Fold one sentence into the interview section (see when-expression FIX 1). Verified: stable since Kotlin 2.2.0 (kotlinlang.org/docs/whatsnew22.html, "Stable"); the control-flow page says they need a `when` with a subject and cannot be combined with commas in one branch.

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Int and Long ranges (-2,147,483,648 to 2,147,483,647; up to 9,223,372,036,854,775,807) | kotlinlang.org/docs/numbers.html | Matches |
| "Numeric types are not subtypes of one another. Kotlin requires explicit conversions to avoid silent data loss and unexpected behavior"; `toByte() ... toDouble()` list; `L` suffix; Int + Long arithmetic gives Long; overflow "wraps around"; "The compiler does not automatically produce an error when integer overflow occurs"; example `2147483647 + 1` gives `-2147483648` | same | Matches |
| A plain literal takes the expected integer type if it fits (number-conversions Build step 6) | same (Integer Literal Type; range check `val tooBig: Byte = 128` is an error) | Supported. The page says `val one: Double = 1` is an error, so "takes the size of the container" holds for whole-number types only; the leaf only uses whole numbers. No change needed |
| Java `long` maps to `kotlin.Long` | kotlinlang.org/docs/java-interop.html | Matches |
| `if` as expression: `else` required; same purpose as the ternary operator; a block's last expression is the value | kotlinlang.org/docs/control-flow.html | Matches (the page text says "an `else` branch is required") |
| `when` compares its argument with each branch in order until one is satisfied; as an expression it must cover all cases; commas group conditions; `in` checks a range | same | Matches |
| `..` includes both ends; `..<` excludes the end; "increments by 1 by default"; `1..4` is 1, 2, 3, 4; `downTo` "instead of `..`" for reverse; `1..9 step 3` ends at 7; last-element rule | kotlinlang.org/docs/ranges.html | Matches. The page does not mention `until` |
| `until` returns a range "up to but excluding" the end value (empty if end is not larger) | kotlinlang.org/api/core/kotlin-stdlib/kotlin.ranges/until.html | Matches |
| `break` terminates the nearest enclosing loop; `continue` proceeds to the next step of the nearest enclosing loop | kotlinlang.org/docs/returns.html | Matches |
| Block-body function must state its return type; "The only exception is when the function returns `Unit`"; `name: Type` separated by commas | kotlinlang.org/docs/functions.html | Matches, but the leaf drops the exception (declaring-functions FIX 2) |
| Default values used when the call omits the argument; named arguments in any order; "after the first skipped argument, you must name all subsequent arguments"; `Unit` return type may be left out | same | Matches |
| `animateFloatAsState(if (enabled) 1f else 0.5f, ...)` and "the alpha value is now an animation value between the provided values (`1f` or `0.5f` in this case)" | developer.android.com/develop/ui/compose/animation/value-based | Matches |
| `transition.animateDp { state -> when (state) { BoxState.Collapsed -> 1.dp; BoxState.Expanded -> 0.dp } }`; `enum class BoxState { Collapsed, Expanded }`; "Specify the target values for each of the states" | same | Matches |
| `valueRange = 0f..50f`; "The range of values that the slider can take" | developer.android.com/develop/ui/compose/components/slider | Matches |
| `drawSquare(sideLength = 30, thickness = 5, edgeColor = Color.Red)` "self-documenting"; "Most Compose libraries use default arguments"; `Text(text = "Hello, Android!")` and the "By specifying only the `text` parameter" sentence | developer.android.com/develop/ui/compose/kotlin | Matches. Note: on that page `drawSquare(30, 5, Color.Red)` is the Java-style call; the leaf only calls it "a call such as", which is fine |
| `@Composable private fun Greeting(name: String)` | developer.android.com/develop/ui/compose/modifiers | Matches. It has no declared return type, which exposes declaring-functions FIX 2 |

Claims I could not verify: none of the cited sentences failed. Not verified (and not claimed by the leaves): the compiler wording of each error message on Kotlin 2.4.21 (I used 2.3.21).

---

# Per-leaf FIX lists

## kotlin/types-and-variables/number-conversions

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section (checklist 11). Add after "You can now". Suggested questions, all answers traceable to the numbers page or to my runs: "Why does Kotlin not turn an Int into a Long for you?" (numeric types are not subtypes; explicit conversion avoids silent data loss; one rule both ways). "What does `Int.MAX_VALUE + 1` give?" (`-2147483648`, no exception, no compiler error). "How do you add two Int sizes without wrapping?" (convert each to Long first; `(a + b).toLong()` is too late). "What does `toInt()` do to a Long that is too big?" (may not keep the value; `3000000000L.toInt()` is `-1294967296`, the same number as the wrapped sum; the numbers page states this rule for narrowing in general). Label any Java `Math.addExact` remark "general knowledge" or leave it out.
2. Line 76. Old: `The Kotlin documentation lists its range as -2,147,483,648 to 2,147,483,647.` New: `The Kotlin documentation lists its smallest and largest values as -2,147,483,648 and 2,147,483,647.` Reason: checklist 3 names "range" as jargon that must not be used before it is defined (it is defined in a later leaf). Mechanical (`fixer`).
3. Line 137 (Predict `int-max-plus-one`, option c `why`). Old: `You may have expected a warning, as with a spill. The documentation says the compiler does not automatically produce an error on overflow.` New: `You may have expected an error, as with a spill. The documentation says the compiler does not automatically produce an error on overflow.` Reason: checklist 6, the `why` must lead to option c, which says "error", not "warning". Mechanical.
4. Line 219 (Recap 2). Old: `An `Int` that goes past 2,147,483,647 wraps around to a negative number without any error.` New: `An `Int` sum past 2,147,483,647 wraps around to a negative number, and nothing reports an error.` Reason: a product can wrap to a positive number; "sum" makes the claim true. Mechanical.
5. Line 44 (Try this 3). Old: `3. Do the same with the jug.` New: `3. Put the same amount in the jug, add one more drop, and see how much room is left.` Reason: "the same" has no defined outcome; the sim spec in `docs/sims/needed-ids.md` (`overflow-cups`) says only that the small cup wraps and the jug "has far more room", and "What you should notice" says nothing about the jug. The new step matches the spec. Mechanical.

## kotlin/control-flow/if-as-an-expression

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section. Suggested: "Does Kotlin have a ternary operator?" (no; `if` is an expression and serves the same purpose; docs quote). "Why must an `if` that gives a value have an `else`?" (every path must give a value, or the name could be empty). "What is the value of a block branch?" (its last expression; earlier lines are side jobs, like `println("Long wait")`). "When is the `else` optional?" (when the `if` is only a statement). Do not state the type of mixed branches (`if (c) 1 else "one"` compiles; its type is a common supertype) unless the writer verifies it on the language spec.
2. Line 70. Old: `The documentation says each branch can be a block, where the value of the last expression becomes the result.` New: `The documentation says each branch can be a **block**, which is a group of steps between curly braces. The value of the last expression in the block becomes the result.` Reason: checklist 3, "block" is used without a definition (line 80 uses it again). Mechanical.
3. Line 215 (Android; `docs/04` §4.10 asks for one real symptom, and this section describes a use). Old: `This next part is our own reasoning: if the `else` side were missing, the screen would have no level to use when the part is switched off.` New: `This next part is our own reasoning: if the two levels were swapped, a switched-on part would look faded. If the `else` side were missing, the screen would have no level to use when the part is switched off.` Mechanical.

## kotlin/control-flow/when-expression

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section. Suggested: "Does Kotlin `when` fall through like a Java `switch`?" (no; the first match runs and the walk stops; no `break` needed). "Why does the order of branches matter?" (first match wins, so a wide range above a narrow one hides it; this is `wide-label-first`). "When must a `when` have an `else`?" (as an expression it must cover all cases; as a statement it need not). "What are guard conditions?" (an extra `if` after the main condition of a branch; stable since Kotlin 2.2; only with a subject; not with comma-grouped conditions). Satisfies the roadmap "mention only" note.
2. Line 36 (Where the analogy stops). Old: `so two labels can both fit, and only the first one is ever asked about that letter.` New: `so two labels can both fit, but the walk stops at the first one that fits and the second is never asked.` Reason: as written it says only one label is ever asked, which is false (every label above the match is asked). Mechanical.
3. Line 44 (Try this 2). The step drags a chute labelled "7 to 12", but the sim spec in `docs/sims/needed-ids.md` (line for `sorting-chutes`) lists only "1", "2", "3 or 4", "5 to 9" and the bin, so the step cannot be done and, with non-overlapping chutes, reordering shows nothing. New text for needed-ids.md, after `"5 to 9"`: `and a fifth chute "7 to 12" that overlaps "5 to 9"`. Mechanical (`fixer`, file `docs/sims/needed-ids.md`, not the leaf); `sim-engineer` then builds to it.
4. Line 83. Old: `The documentation says you can use `when` as an expression or as a statement.` New: `The documentation says you can use `when` as an expression or as a **statement**, which is a line that does a job and gives no value.` Reason: checklist 3, "statement" undefined. Mechanical.
5. Line 237 (Android). The doc snippet has no `else` because `BoxState` is an enum, while the leaf says a value-giving `when` "usually" needs `else`; a careful reader sees a contradiction. Insert before `This next part is our own reasoning:`: `There is no `else` there, because `BoxState` is an enum, a fixed list of names, and the two branches already cover both of them.` Reason: the enum definition is on the same page; coverage-without-else for an enum is general knowledge, so add `(general knowledge)` after the sentence. Mechanical.
6. Line 237 (Android; same paragraph as FIX 5, apply together; `docs/04` §4.10 asks for one real symptom). Old: `This next part is our own reasoning: a `when` that listed only one state would leave the other state with no width, which is why Kotlin asks you to cover every case.` New: `This next part is our own reasoning: with the two widths swapped, an expanded box would show a thin border and a collapsed box none. A `when` that listed only one state would leave the other state with no width, which is why Kotlin asks you to cover every case.` Mechanical.

## kotlin/control-flow/ranges-and-for-loops

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section. Suggested: "Does `1 until 5` include 5?" (no; `until` and `..<` are the same, `..` includes the end). "What does `for (i in 5..1)` print?" (nothing, the range is empty; use `downTo`; verified by my run, and the `until` page states empty ranges for `until`). "What happens with `step 0` or a negative step?" (`IllegalArgumentException: Step must be positive`; verified by my run; `downTo` carries the direction). "How do you loop over a list's positions?" (stop before the size with `..<`; the writer may add `indices` only after checking the stdlib page). "What is the last number of `1..9 step 3`?" (7, from the ranges page).
2. Line 8 (`summary`). 27 words; `docs/04` line 62 (frontmatter table) limit is under 25 words. Old: `A Kotlin range says where to start, where to stop and how big each step is. Some forms include the stop number and some leave it out.` New: `A Kotlin range says where to start, where to stop and how big each step is. Some forms leave the stop number out.` (23 words). Mechanical.

## kotlin/control-flow/break-and-continue

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section. Suggested: "What is the difference between `break` and `continue`?" (`continue` skips the rest of one pass; `break` ends the nearest loop; the code after the loop still runs). "In nested loops, which loop does `break` end?" (the nearest enclosing one; a label such as `break@outer` jumps out of a named outer loop; returns page). "Can I use `break` or `continue` inside `forEach`?" (since Kotlin 2.2 non-local `break` and `continue` are stable for lambdas passed to inline functions, but only when a loop encloses the call; at the top level of a function they do not compile, which I confirmed with the compiler; `return@forEach` acts like `continue`). Label the `return@forEach` line "general knowledge" unless the writer cites the page.
2. Line 81. Old: `One pass through the steps for a single letter is a **round**.` New: `One pass through the steps for a single letter is a **round**, which programmers call an **iteration**.` Reason: L33, the learner must be able to use the interview word. Mechanical.

## kotlin/functions/declaring-functions

VERDICT: FIX (FIX 2 to 8 are one accuracy problem and should be applied together)

1. (owner decision on heading, then `lesson-writer`) Missing interview section. Suggested: "What is the difference between a parameter and an argument?" (the box vs what you write in it). "Is the return type optional?" (not for a block-body function that gives back a value; the only exception is `Unit`, and `fun main()` is one; this is the roadmap misconception). "Can a function return early?" (yes, `return` ends the function at once, so it need not be the last line). "What does a function with no `return` give back?" (`Unit`; say only what the functions page says).
2. Line 69. The leaf says a block-body function "must always state its return type", but the page continues "The only exception is when the function returns `Unit`". Old: `The documentation says a function with a block body must always state its return type.` New: `The documentation says a function with a block body must always state its return type. The only exception is a function that hands back nothing useful, such as `main`. Its return type is **`Unit`**, and you may leave it out.` Reason for the wording: `Unit` is bolded and defined where it first appears (checklist 3) and the two ideas are in two sentences (L29). Mechanical.
3. Line 52 (Guess). Old: `You must always write it.` New: `You must write it, unless the function hands back nothing useful.` Mechanical.
4. Line 36 (Where the analogy stops). It says "this lesson uses only functions that do", but every program here has `fun main()` with no return type, and the Android section shows one more. Old: `Also, a print shop always hands you something, but some Kotlin functions hand back nothing useful, and this lesson uses only functions that do.` New: `Also, a print shop always hands you something, but some Kotlin functions hand back nothing useful. `main` is one of them, which is why it has no "you get back" line.` Mechanical.
5. Line 208 (Common mistakes 1). Old: `A function with its steps in braces must state it.` New: `A function with its steps in braces that hands back a value must state it.` Mechanical.
6. Line 222 (Recap 3). Old: `A function with a block body must state its return type and use `return`.` New: `A block-body function that hands back a value must state its return type and use `return`.` (17 words). Mechanical.
7. Line 214 (Android). The `Greeting` snippet has no return type; the paragraph calls it "the form from this lesson". Append to the first paragraph: ``Greeting` has no "you get back" line because it hands back nothing useful, the exception from Name it.` Mechanical.
8. Line 203 (Tricks). Old: `- `return` is the last thing the clerk does.` New: `- `return` hands the sheet back and ends the clerk's work.` Reason: an early `return` is not the last line; this matters for the interview question above. Mechanical.
9. Line 73. "Call" is used (lines 97, Predict items) but never defined. Old: `When you use the form, the values you fill in are the **arguments**.` New: `Using the form is called a **call**, and the values you fill in are the **arguments**.` Mechanical.
10. Line 216 (Android; `docs/04` §4.10). Old: `This next part is our own reasoning: the screen can use the same function twice, once with each person's name.` New: `This next part is our own reasoning: the screen can use the same function twice, once with each person's name. If a call put the wrong value in the box, a row would show the wrong name.` Mechanical.
Recorded, not a FIX: the `// does not compile` block is correctly labelled "our own reasoning from that rule"; the real compiler message ("missing return statement") confirms it.

## kotlin/functions/default-and-named-arguments

VERDICT: FIX

1. (owner decision on heading, then `lesson-writer`) Missing interview section, and this leaf has the largest depth gap: nothing says when a default is worked out. Suggested: "When is a default value evaluated?" (on every call that leaves the argument out, not once; I ran `fun g(x: Int = next())` and two calls gave 1 then 2; the functions page `read` example shows the same: the default's `println` runs only when the argument is omitted). "Can a default use an earlier parameter?" (yes, `fun f(a: Int, b: Int = a * 2)`; verified by my run). "Why use defaults instead of overloads?" (the Compose Kotlin page: defaults reduce the need for overloaded functions). "What is the rule after a skipped argument?" (name every argument that follows; functions page). "What if a default parameter comes before one without a default?" (you can use the default only by naming the argument that follows; functions page). Leave out `@JvmOverloads` and override rules unless the writer verifies them on the pages.
2. Line 79. `Unit` is used with no definition (checklist 3). The leaf requires `declaring-functions`, whose FIX 2 introduces and defines `Unit`; apply that first. Old: `This function prints and hands nothing back, so no return type is written. The documentation says the return type is optional only for a function that returns `Unit`.` New: `This function prints and hands nothing back, so its return type is `Unit`. The documentation says you may leave out the type only for such a function.` Mechanical.

---

## Hand-traces (mine, all confirmed by the compiler run)

number-conversions: `int-max-plus-one` is -2147483648 (b); `slip-after-the-sum` is 3000000000 - 4294967296 = -1294967296 (c); `slip-before-the-sum` is 3000000000 (a).
if-as-an-expression: `label-pos-or-neg` neg (b); `busy-queue-block` Busy, Wait (c); `three-drink-machine` tea (a).
when-expression: `first-chute-wins` small (c); `nothing-fits-so-bin` other (a); `wide-label-first` back (b).
ranges-and-for-loops: `down-to-by-three` 10, 7, 4, 1 (b); `dots-versus-until` 123 then 12 (c); `one-past-the-last-mark` 0, 1, 2, 3 (a).
break-and-continue: `skip-two-stop-five` 1, 3, 4 (c); `stop-where-skip-meant` 1, 2 (a); `stop-then-done` 1, 2, Done (b).
declaring-functions: 14 (c); -7 (b); 12 (a).
default-and-named-arguments: `greet-ana` Hi, Ana (c); `swap-by-name` -7 then 7 (a); `skip-the-size` tea, medium, oat then tea, small, regular (b).
