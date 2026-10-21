# Review W7-a: four shadow leaves (arrays-vs-lists, functions-as-values, lambda-syntax, higher-order-functions)

Date 21 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31, checklist `docs/lesson-checklist.md`. Nothing in the leaves was edited.
Compiler: `scripts/kt-run.sh`, kotlinc-jvm 2.4.21 (the pinned version). `./gradlew :site:validateContent`: `validateContent: ok`, BUILD SUCCESSFUL.

VERDICT: FIX (0 PASS, 4 FIX; items 1 to 9 are mechanical, `fixer` can do them; item 10 needs the owner)

| Leaf | Verdict | Items |
|---|---|---|
| `arrays-vs-lists` | FIX | 1, 2, 3, 4, 5, 10 |
| `functions-as-values` | FIX | 5 |
| `lambda-syntax` | FIX | 5, 6, 7, 8, 9, 10 |
| `higher-order-functions` | FIX | 5, 8 |

## Code run (kotlinc 2.4.21)

Every `kotlin` block and every Predict `code` was compiled and run (21 programs: 9 kotlin blocks, of which 4 are `// does not compile`, and 12 Predict items).

| Check | Result |
|---|---|
| Runnable kotlin blocks (arrays k1; functions-as-values k1; lambda-syntax k1, k2; higher-order k1) | Compile; stdout equals the text under each block, line for line |
| `// does not compile` blocks (arrays k2, functions-as-values k2, lambda-syntax k3, higher-order k2) | All four fail to compile, as stated. Errors seen: unresolved `add` on `IntArray`; `(String) -> Int` vs `KFunction1<String, String>`; "an explicit type is required on a value parameter"; `Unit` given where `() -> Unit` expected |
| All 12 Predict items | Real stdout equals the `output` field exactly, and the `correct: true` option matches the real output in every item |
| Warnings | None, except `arrays-vs-lists` k1 and Predict `same-box-or-same-eggs`: the compiler warns "'==' on arrays compares only references" (see item 1) |

The 12 outputs are confirmed on 2.4.21. The `verified: not-run` fields can become `verified: kotlin-2.4.21` (`docs/04` §5.3, `docs/06` §3; `what-is-null.md` does this). That is item 5.

## Fact check (official pages, read 21 Oct 2026)

| Claim in the leaves | Page | Result |
|---|---|---|
| `Array` boxes primitives; primitive-type arrays store values without boxing; `val` only stops reassigning; fixed size, no read-only variant; no `.add()`; `==` compares references; use `contentEquals` / `contentDeepEquals`; `toList()` copies and does not reflect later changes; collections preferred, arrays for Java interop and low-level needs; `IntArray(3)` is zeros | kotlinlang.org/docs/arrays.html | Match |
| "The first index is 0" attributed to the documentation | same | NOT on the page. Examples use `a[0]`, the text never says it (item 2) |
| Compose brush page: `arrayOf` for `colorStops` (Float to Color pairs); `listOf` for colours elsewhere on the same page | developer.android.com/develop/ui/compose/graphics/draw/brush | Match |
| `(A, B) -> C`; `Unit` cannot be omitted; `::functionName` for top-level or local functions; `f(x)` and `f.invoke(x)`; first-class wording; higher-order definition | kotlinlang.org/docs/lambdas.html | Match |
| Lambda in curly braces; params before `->`; last expression is the value if not `Unit`; `it`; trailing lambda; parentheses may go if the only argument; `return@label` | same | Match |
| "The documentation says you must give the types either in the lambda or in the function type" | same | NOT stated. The page says types are optional when inferred and is silent on the failure case (item 6) |
| Implicit label has the name of the function the lambda is passed to; local return is "similar to the use of `continue`"; bare `return` leaves the nearest enclosing function | kotlinlang.org/docs/returns.html | Match (but see item 7) |
| Runtime penalties of function values (function objects, closures, allocations, virtual calls); `inline` affects the function and its lambdas; "Inlining may cause the generated code to grow ... avoiding inlining large functions" | kotlinlang.org/docs/inline-functions.html | Match |
| `repeat` is `inline fun repeat(times: Int, action: (Int) -> Unit)`; the index is zero-based; zero times does not call `action` | kotlinlang.org/api/core/kotlin-stdlib/kotlin/repeat.html | Match. The inline-functions page does NOT show `repeat` (item 8) |
| Compose Kotlin page: "Kotlin supports higher-order functions ... Compose builds upon this approach"; `Button` has an `onClick` lambda parameter the button calls on click; `onClick = myClickFunction`; `Button(onClick = { ... }) { ... }`; layouts' last parameter is `content`, "a function which emits the child UI elements"; `Column { ... }` with parentheses omitted | developer.android.com/develop/ui/compose/kotlin | Match |

Could not verify (accepted when labelled): that the printed form of an array is a label like `[I@55f96302` (I ran it: it prints that; the leaf labels it general knowledge, fine); that `kotlin.Unit` is printed for a `Unit` value (I ran it; labelled general knowledge); "People call these first-class functions" is in fact on the lambdas page, so the "(general knowledge)" label in `functions-as-values` is only over-cautious.

## Checked and OK

Frontmatter keys and order; `id` equals path; `title`, `difficulty`, `requires`, `fruit`, `sims` equal the roadmap rows (`content/kotlin/_roadmap.md` lines 178-201 and 485-495); `order` 30 to 33 is free and in sequence; summaries under 25 words; `playground: false`, all Predict `run: false`; `recipe-card-handoff` is in `docs/sims/needed-ids.md` and has one SIM marker (marker without a built sim does not block, as in W4-a); headings in the 12-heading order with exact names (13 with See it move); no empty heading; The question is 4 to 5 sentences, has `?`, no code, no bold; Notice, Guess and Build have no Kotlin syntax; each Predict has 3 options, one `correct`, a `why` on every option, correct letters vary across Predict items (c, b, a / b, c, a / b, c, a / b, c, a); at most 6 kotlin blocks per leaf (limit 8); Predict code under 12 lines; Recap is three one-sentence numbered lines under 20 words; Tricks 4, Common mistakes 3 to 4; Interview section has 3 to 4 questions in the right place; every "You can now" line has a matching Predict item; SOURCES comment at line 18; no VERIFY, video, capstone, "coming soon", next-lesson promise; no banned T4 words, no `!` in prose, no non-ASCII, no emoji; each bold term appears once; Android sections are reading-only and own reasoning is labelled except item 9.

## FIX list

1. `arrays-vs-lists.md` line 213 ("Kotlin gives `false` without any error"), line 236 ("it compiles and gives `false`, so the bug hides"), line 222 (Save-button example). Running the leaf's own code on kotlinc 2.4.21 shows the compiler does warn on `==` between arrays ("'==' on arrays compares only references. Replace '==' with 'contentEquals' ..."). The text implies silence. Add one sentence that the compiler only warns, not stops, and that the warning is easy to miss (my run; do not quote the message text, `docs/04` §5.4 allows it only if pasted exactly). Mechanical.
2. `arrays-vs-lists.md` line 66. "The Kotlin documentation says each value is reached by its position, called its index, and the first index is 0." The arrays page says only "accessed by index"; it never states that counting starts at 0. Move "the first index is 0" out of the documentation sentence and label it general knowledge (checklist item 4). Mechanical.
3. `arrays-vs-lists.md` line 35 ("the compiler adds the cups"), line 227 ("compares references"), line 61 ("on a screen"). "compiler" and "references" are used without a definition (checklist item 3); "show the eggs on a screen" is a loose screen claim (item 7, a print goes to the console). Say "Kotlin adds the cups", "compares whether both names point at one array", "print the eggs". Mechanical.
4. (merged into item 5a; kept so numbering stays stable.)
5. Guess letters and `verified` fields, all four leaves. (a) Every Guess key is B (`arrays-vs-lists` line 51; `functions-as-values` line 61; `lambda-syntax` lines 51 and 59; `higher-order-functions` line 50). Vary them: for example A, C, B+A, C. Update the "Idea X is the answer" sentence with each move. (b) Set every Predict `verified: not-run` to `verified: kotlin-2.4.21`: all 12 outputs were confirmed by the run above (arrays lines 119, 148, 179; functions-as-values 125, 158, 194; lambda-syntax 127, 155, 185; higher-order 115, 149, 182). Mechanical.
6. `lambda-syntax.md` line 111. "The documentation says you must give the types either in the lambda or in the function type." The lambdas page does not say this. Replace with what the run showed: "Kotlin refuses to run this one, because nothing tells it what type `x` is." Mechanical.
7. `lambda-syntax.md` Build step 6 (line 68), Tricks line 216, interview answer line 239. They say a plain `return` "would leave the whole function" / "leaves the whole job". The returns page says "nearest enclosing function"; I also compiled a bare `return` in a stored lambda and it is rejected ("'return' is prohibited here"). It is only allowed in an inline lambda such as `forEach`. Add "in a `forEach` lambda" so the learner does not try it in `val note = { ... return ... }`. Mechanical.
8. Terms used before they are defined or bolded (checklist 3): `lambda-syntax.md` line 76 "argument", line 226 "layout", "child elements"; `higher-order-functions.md` line 34 (Mental model, "the lambda it holds" before lambda is explained on this page) and line 64 ("trailing lambda"); `higher-order-functions.md` line 236 says "The documentation shows `repeat` as an `inline` function": the inline page does not, the `repeat` API page does. Reword to "The `repeat` reference page shows it as `inline`". Define "lambda" in one clause at first use. Mechanical.
9. `lambda-syntax.md` line 228. "This is a common first puzzle in a Compose file." Unverified opinion shown as fact (checklist 4). Prefix "My own reasoning:". Also checklist 13: add the supplied common-wrong-belief "`it` works for lambdas with two or more parameters" to Common mistakes (true: `it` only exists for exactly one parameter, `docs/practice/interview-kotlin.md` line 515; lambdas page agrees). `higher-order-functions.md` already uses its supplied belief. Mechanical.
10. Length and supplied data (needs the owner). Prose (headings and tables included, code and comments excluded): `arrays-vs-lists` 1,778 words and `lambda-syntax` 1,663 words, over the 1,600 budget (`docs/04` §6); `functions-as-values` 1,462 and `higher-order-functions` 1,395 are inside it. The interview section adds about 300 words per leaf, after the budget was set. Owner decides: allow the interview section outside the 1,600 count, or trim about 180 words from `arrays-vs-lists` (the Mental model bullets and the Android own-reasoning paragraph are the cheapest cuts). Also checklist 13: none of the four leaves uses the supplied `build-exercise` line (`docs/practice/interview-kotlin.md` lines 479, 495, 511, 527), and the senior and "first-class citizens" questions are not used. Not a FIX unless the owner wants it; the leaves have `playground: false` and no Try it, so a hands-on task would go into Tricks to remember.

## Fixes applied

Items 1 to 3, 5 to 9 applied on 21 Oct 2026 to the four leaves (Guess keys varied, `verified: kotlin-2.4.21` on all 12 Predict items). Item 4 merged into 5. Item 10 not applied: owner decision pending. Build check result is recorded in the handoff report.

Optional (not FIX): sentence length (T7). Over 25 words: `functions-as-values` lines 34, 74, 242, 251; `lambda-syntax` lines 72, 78, 226, 239, 242; `higher-order-functions` lines 225 (35 words), 233, 236. `functions-as-values` line 78 "(general knowledge)" can say "the documentation calls these first-class" because the lambdas page does. `lambda-syntax` Guess 1 option C ("the note must always use the word card") is a weak distractor; "the note must always call it `x`" is more believable.
