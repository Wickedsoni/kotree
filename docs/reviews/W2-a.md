# W2-a review: 4 shadow leaves (programming basics)

Date: 20 Oct 2026. Reviewer: independent. Gate: L31. Nothing in the leaves was edited.
Policy applied: a SIM marker with no built sim does not block (renderer draws nothing for unregistered sims, `LessonView.kt` `simRegistry`). The `<!-- SOURCES -->` comment on line 18 is a go-live strip for `housekeeper`, not a FAIL.

Build: `./gradlew :site:validateContent` prints `validateContent: ok`, `BUILD SUCCESSFUL`, no warnings.

VERDICT: FIX (3 of 4 PASS; lines-run-in-order needs one mechanical edit)

| Leaf | Verdict |
|---|---|
| foundation/first-code/lines-run-in-order | FIX (1 item) |
| foundation/programming-thinking/what-is-a-variable | PASS |
| foundation/programming-thinking/what-is-a-type | PASS |
| foundation/programming-thinking/expressions | PASS |

## FIX list (mechanical, `fixer` can do it)

1. `content/foundation/first-code/lines-run-in-order.md`, line 191 (Common mistakes, first bullet).
   Problem: "chip" is an unexplained term here (L29), and "the chip works through the steps one after another" contradicts `what-is-a-cpu`, which says a phone chip has several cores. The lesson claim is about program order, not hardware.
   Old: `But the chip works through the steps one after another.`
   New: `But the computer follows your lines one after another.`

what-is-a-type: no FIX items (see optional note below).

## Per-leaf notes

### lines-run-in-order: FIX (item 1)
Structure, frontmatter (keys, order, `order: 9`, `requires`, title match roadmap), 12-heading layout minus Try it and Fix the bug (playground false), three predict blocks with 3 options each, one `correct: true`, all `output` fields: OK.
Hand-trace of all three outputs (Ready/Set/Go; Sam/Hello; four/one/two/three): correct. Name it block compiles. `println` and `main` claims match basic-syntax.html ("An entry point of a Kotlin application is the main function"; "`println` prints its arguments and adds a line break").
Android claim "Kotlin is fully supported for Android development" matches developer.android.com/kotlin ("Kotlin is fully supported for Android development with tools and resources to help you succeed."). It is true but does not support the paragraph around it (optional: cut that sentence and the one after it).

### what-is-a-variable: PASS
Traces: 0, 5, 6 (Name it); 2; 20; 9 then 3. All correct. `val score = 0; score = 1` is a compile error (cannot reassign a val, per basic-syntax.html), correctly marked `// does not compile`.
Verified against kotlinlang.org/docs/basic-syntax.html: val assigned once and not reassignable; var reassignable; "You can use variables only after initializing them". Verified against developer.android.com/develop/ui/compose/state: "State in an app is any value that can change over time."
Optional, not blocking: `var` and `val` are not bold on first use (T2 asks for bold on first use of each term); "A copy is a fresh ball" is true for numbers only (fine for this lesson).

### what-is-a-type: PASS
Traces: `5 + 5` gives 10 and `"5" + 5` gives 55 (Name it block: 10 then 55); `a + a` is 4 and `b + b` is 33; Program A prints `age: 12`, B does not compile, C prints `12age`. All correct.
`Int.plus` has six overloads (Byte, Short, Int, Long, Float, Double), none for String or Char, so `5 + "five"` does not compile: confirmed on kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/plus.html. `String.plus(other: Any?)` joins with the string form: confirmed, sample `"Kotlin " + 2` gives `Kotlin 2`. "Every variable and data structure in Kotlin has a type. Types are important because they tell the compiler what you are allowed to do": confirmed on kotlin-tour-basic-types.html. Compose text-field sample (`var name by remember { mutableStateOf("") }`, greeting `Hello, $name`) confirmed on the state page.
Optional, not blocking: (a) Guess option C (line 56, `"five5"`) is the result of the text-first order, while the request is number-first, and line 58 says so. The page is coherent, so this is a distractor preference. If wanted: line 56 `...pour it into the bottle's word, giving a bottle marked "five5".` to `...join it to the word, giving a bottle marked "5five".` and line 58 `...and Kotlin does this, but only when the bottle comes first.` to `. Kotlin does join a number onto text, but only when the text comes first.` (b) Line 85 says a number with no decimal point is an `Int`; numbers.html says `Int` if it fits, otherwise `Long`. Fine for the examples used. Line 203 is a long sentence with two ideas (T7); could split at "and".

### expressions: PASS
Traces: `7 / 2` = 3, `7 % 2` = 1, `7.0 / 2` = 3.5, `2 + 3 * 4` = 14, `(2 + 3) * 4` = 20, `(4 + 7) / 2` = 5. All correct.
Confirmed on numbers.html: "Division between integer values always returns an integer result. The compiler discards the fractional part"; "To return a floating-point result, make at least one operand a Float or Double"; floating literals infer Double; operators `+ - * / %`. Precedence confirmed from the Kotlin spec grammar (additiveExpression is built from multiplicativeExpression). Compose progress page confirmed: "Pass a Float between 0.0 and 1.0" and `updateProgress(i.toFloat() / 100)`. The claim "3 divided by 4 with whole numbers gives 0" is correct.
Optional, not blocking: "brackets" is the UK word; Kotlin docs say parentheses. The negative-operand case is correctly left out.

## Cross-cutting notes (not FAIL, for the owner)

- All four leaves use `playground: false` with `run: false` and `verified: not-run` on code that a learner could run. This is allowed by `docs/06` §5 (hand-traced answers) and the validator accepts it, but it drops the roadmap's `F:` (Fix the bug) tasks for these rows, and the template calls `playground: false` a theory leaf. If a Kotlin run becomes available, re-verify and consider restoring Try it and Fix the bug.
- No Kotlin was run (no kotlinc). Every output above was traced by hand a second time by this reviewer, and every "does not compile" claim was checked against the official API page, not a compiler.
- `lines-run-in-order` and `what-is-a-variable` have SIM markers (`line-by-line-stepper`, `labelled-jars`) whose sims are not built. Their "Try this" lists tell the learner to press Step and drag lines, which will read oddly until the sims exist. Allowed by policy.
- Banned words (T4), exclamation marks, emoji, raw HTML, VERIFY markers, Video or Capstone text: none found in any of the four leaves. Prose word counts: 1078, 1416, 1305, 1179 (budget 900 to 1600).

## Unverified claims
- `5 + "five"` being refused is derived from the `Int.plus` member overloads only (no extension `plus` on Int checked). Derived, not compiled, as the leaf's own SOURCES comment says.
- The SOURCES comment of `expressions` cites `-int/rem.html` and `spec/syntax-and-grammar.html`. Neither was fetched. Precedence was confirmed on `spec/expressions.html` instead; the `rem` mapping was confirmed on operator-overloading.html.
- "Everything inside a computer is stored as numbers" (what-is-a-type): textbook CS, no official page.
- "A program's lines run top to bottom": textbook, no official page (stated in the SOURCES comment).
