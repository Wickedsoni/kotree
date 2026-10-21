# Review W8-a: four shadow leaves (member-functions, private-and-public, what-is-an-interface, open-and-override)

Date 21 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31, `docs/lesson-checklist.md`. No reviewed file was edited.
Compiler: `scripts/kt-run.sh`, kotlinc-jvm 2.4.21. `./gradlew :site:validateContent`: `validateContent: ok`, BUILD SUCCESSFUL.

## Code run (kotlinc 2.4.21)

All 21 programs compiled or failed as stated: 11 kotlin blocks (4 are `// does not compile`) and 12 Predict `code` fields. No compiler warnings appeared.

| Check | Result |
|---|---|
| 7 runnable kotlin blocks | Compile; stdout equals the sentence under each block |
| 4 `// does not compile` blocks | All fail, as stated: "this type is final, so it cannot be extended"; "'serve' in 'Soup' is final and cannot be overridden"; "cannot access 'var balance: Int': it is private in 'Account'"; "class 'Scooter' is not abstract and does not implement abstract member"; "interface 'Drivable' does not have constructors" |
| 12 Predict items | Real stdout equals the `output` field and the `correct: true` option in every item |
| Common mistake `Account.deposit(50)` (member-functions) | Fails: "unresolved reference 'deposit'" |

## Fact check (page bodies read 21 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Member function defined inside a class; call = instance name, `.`, function name | kotlinlang.org/docs/functions.html | Match |
| `this` refers to the current object of the class; `this.name` | kotlinlang.org/docs/this-expressions.html | Match |
| Default visibility is `public`; `private` = this class only, including all its members; `protected` = as private plus subclasses; `internal` = any client inside the module that sees the declaring class | kotlinlang.org/docs/visibility-modifiers.html | Match. The page itself defines a module as "a set of Kotlin files compiled together" (item 5) |
| Interfaces contain abstract methods and method implementations; no backing fields in interface properties; `super<A>.foo()` | kotlinlang.org/docs/interfaces.html | Match |
| Classes final by default, `open`, `override` required, no-`open` function cannot be overridden, `override` member is itself open, `final override`, `super.draw()` | kotlinlang.org/docs/inheritance.html | Match, except the Circle example wording (item 6) |
| `View.OnClickListener` is `public static interface`; one abstract method `onClick(View v)`; "Called when a view has been clicked" | developer.android.com/reference/android/view/View.OnClickListener (full page body read) | Match; two small additions need labels (item 3) |
| `ExampleActivity : ComponentActivity()`, `override fun onCreate`, `super.onCreate(savedInstanceState)`; "You must implement this callback, which fires when the system first creates the activity" | developer.android.com/guide/components/activities/activity-lifecycle | Match |
| `private val _uiState = MutableStateFlow(DiceUiState())`, `val uiState: StateFlow<DiceUiState>`; "private backing MutableStateFlow instances" | developer.android.com/topic/architecture/ui-layer/state-production | Match (the page says "With flows, this typically means creating ..."; the leaf paraphrases, acceptable) |

The `input-events` URL in the interface SOURCES comment opens (HTTP 200). Claims I could not verify: none beyond the items below.

## Structure

Frontmatter equals the roadmap rows (id, title, difficulty, requires, fruit, sims); `order` 38, 39, 45, 46 are in sequence. Twelve headings in the right order, with the Interview section before Recap. Prose excluding code and the Interview section: 907 / 970 / 956 / 1,125 words, inside 1,600. Summaries under 25 words. Recap lines are one sentence, under 20 words. Question blocks are 4 to 5 sentences. Correct Predict letters vary in each leaf. Interview sections hold 4 questions each.

## FIX list (all mechanical, `fixer` can do them)

1. All four leaves, every Predict block (12 items) and line 19 (`what-is-an-interface` lines 19 and 20). Change `verified: not-run` to `verified: kotlin-2.4.21`; the 12 outputs were confirmed above. Delete the "compile not run" VERIFY comment in each leaf, and in `what-is-an-interface` also the OnClickListener VERIFY comment (line 20; the page body was read and matches). The VERIFY comments are also the only non-ASCII characters (checklist 9).
2. Guess keys (checklist 6). The correct idea is always B: `member-functions` lines 46-48, `private-and-public` lines 47-49, `what-is-an-interface` lines 47-49, `open-and-override` both Guess blocks (lines 48-50 and 56-58). Reorder the options so the correct letters differ across the four leaves and across the two blocks in `open-and-override` (for example A, C, A, C). Keep the "Idea X is the answer" sentences in step.
3. `what-is-an-interface` line 314. The page says "a callback to be invoked when a view is clicked". "such as a button" and "Your code gives the button something that keeps that promise, and the button calls it on a tap" are not on the page. Mark that sentence "(general knowledge)" or cut "such as a button".
4. `member-functions` line 233. "onCreate is a member function: it runs on one activity object, and it can read and change the data stored in that object" is unlabeled; add "(general knowledge)". "If a screen shows unexpected data after it is opened twice ..." is unlabeled; add "My own reasoning:".
5. `private-and-public` lines 65 and 242. The module definition is labelled "(general knowledge)", but the visibility page states it. Say "The documentation says a module is a set of Kotlin files compiled together". Line 242: "a Gradle module is one example" is not what the page lists (it lists an IntelliJ IDEA module, a Maven project and a Gradle source set). Write "a Gradle source set is one example, according to the documentation".
6. `open-and-override` line 74. "The documentation's example writes `class Circle : Shape()`" is not exact; the page has `class Circle() : Shape()`. Quote it exactly, or say "similar to".
7. `open-and-override` lines 76, 274, 275, 289. **modifier** and **signature** are used without definition (checklist 3). Add short definitions at first use: modifier = a word like `open` or `override` written before a function or class; same signature = same name and same inputs. Also line 52: "The reason to lock steps is that gran may change her recipe later ..." is the leaf's own reasoning; prefix "My own reasoning:". Also `override` is bolded in both `what-is-an-interface` and `open-and-override`; in the second, unbold it or say "as you saw before".
8. `what-is-an-interface` line 35. "you can never plug something into a socket that has no device behind it" makes no sense in the analogy. Replace with the point that matters: an interface has no body of its own, so you cannot make an object from it.
9. Optional, not blocking. `private-and-public` "You can now" lines 258-259 have no Predict item that tests exactly that (the outside-call refusal is shown only in a non-compiling block); consider rewording to match the Predict items. `private-and-public` line 241: "client" and "declaring class" in the Interview answer are undefined for a beginner.

VERDICT: FIX

## Fixes applied

- Items 1-8 applied to the four leaves (verified field, VERIFY comments, Guess keys now A, C, A, C/A, labels, module wording, Circle quote, modifier/signature definitions, interface analogy line).
- Item 9 not applied (optional, judgment): see OPEN QUESTIONS in the orchestrator report.
- `./gradlew :site:validateContent`: validateContent: ok, BUILD SUCCESSFUL.
- No VERDICT PASS line written, because item 9 is not applied.

VERDICT (re-check, final): PASS for all 4 leaves (item 9 optional, skipped). validateContent ok.
