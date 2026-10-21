# Review W6-a: eight shadow leaves (functions end, null-safety)

Date 21 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31 and L33. Voice: L29. Nothing in the leaves was edited.
Checked against `docs/lesson-checklist.md` (all 12 items), `content/_templates/leaf.md`, `docs/04`, `docs/05` section 10, `docs/06` section 5, hard rules.

VERDICT: FIX (0 PASS, 8 FIX). Every item is mechanical (`fixer` can do it; replacement text is given). Owner items are listed at the end and do not block the fixes.

| Leaf | Verdict | Items |
|---|---|---|
| `kotlin/functions/single-expression-functions` | FIX | 5 |
| `kotlin/functions/unit-and-nothing` | FIX | 4 |
| `kotlin/functions/scope-and-shadowing` | FIX | 6 |
| `kotlin/null-safety/what-is-null` | FIX | 3 |
| `kotlin/null-safety/nullable-types` | FIX | 3 |
| `kotlin/null-safety/safe-call-and-elvis` | FIX | 3 |
| `kotlin/null-safety/smart-casts` | FIX | 7 |
| `kotlin/null-safety/when-to-use-double-bang` | FIX | 6 |

## How I ran things

`bash scripts/kt-run.sh` with kotlinc 2.4.21 (JRE 21), already installed at `~/.kotlinc`. I extracted every `kotlin` fence (20, of which 8 are marked `// does not compile`) and every challenge `code` field (24) into files and ran each one: 44 programs, of which 35 compile and run cleanly and 9 fail to compile exactly as the leaves say (the 8 fences plus the `which-lines-are-refused` Predict). I also wrote extra probes for the interview answers (recursion, `fun empty() = null`, `if` bodies, smart-cast rules for local `var`, `val`, `var` property, custom getter, `open` property, closure capture and a second module, a Java class returning null, `requireNotNull`, `checkNotNull`, `null!!`). `./gradlew :site:validateContent` ran once at the end: `validateContent: ok`. No other Gradle run was active (one idle daemon only).

Zero compiler warnings on any of the 35 programs that compile. Every program that should compile did.

## What passed on all eight

- Frontmatter: keys and order match `docs/00` section 8.2; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/kotlin/_roadmap.md` lines 86-128); `order` 14 to 21 follows `default-and-named-arguments` (13); `status: shadow`; `video: ""`; dates empty; `upnext: false`; `summary` 21 to 23 words, not a copy of the title. `sims` equals the SIM marker (what-is-null `null-lunch-box`, safe-call `knock-and-note`, scope `house-and-street-names`; the other five `[]`). All three ids are in `docs/sims/needed-ids.md`.
- `playground: false`, every Predict `run: false`, no Try it, no Fix the bug. Allowed by `docs/04` section 4 and `docs/06` section 3.1 and accepted in W4-a and W5-a.
- Headings: 9 (or 10 with See it move), exact names, correct order, `## Say it in an interview` after "Where this shows up in Android" and before Recap, 4 questions each. No empty heading, no raw HTML, no Video or Capstone text, no VERIFY mark, no emoji or non-ASCII outside the SOURCES comment, no banned word (T4), no `!` in prose, no "coming next". SOURCES comment on line 18. Recap is three one-sentence lines under 20 words, no semicolons. Question blocks are 3 to 5 sentences with a `?` and no code block. "Where the analogy stops" present; one analogy per leaf; Notice, Guess and Build hold no Kotlin syntax. Correct letters vary in every leaf (single b c a, unit b c a, scope b c a, what-is-null b a c, nullable b c a, safe c b a, smart b c a, when c a b). Each Predict has 3 options, one `correct`, and a `why` on every option that leads to the option.
- Every `output` equals real stdout (see the table), except the note under nullable-types.
- T7 threshold I applied: a sentence of 30 words or more with two ideas is split (W5-a did the same for 33 and 37 words). Not counted: "What you should notice" lines (the template asks for one sentence; the `what-is-null` one is verbatim from `docs/04` section 10), table cells, and list bullets whose length comes from the bold lead-in. Sentences of 25 to 29 words were left alone.
- Every "does not compile" block really fails, with the claimed reason (table below).

## Real results (my runs, Kotlin 2.4.21)

| Leaf | Block | Real result | Leaf says | Match |
|---|---|---|---|---|
| single | Name it 1 | `8` `8` | `8` `8` | yes |
| single | Name it 2 (braces, no return, `val result: Int = double(4)`) | error: initializer type mismatch: expected 'Int', actual 'Unit' | refused, because the body is Unit | yes |
| single | half-of-seven | `3` `6` (key b) | b | yes |
| single | two-customers | `25` (key c) | c | yes |
| single | sign-inside-sign | `3` `3` (key a) | a | yes |
| unit | Name it 1 | `Hi, Ana` `kotlin.Unit` | same | yes |
| unit | Name it 2 (`val greeting: String = sayHi("Ana")`) | error: expected 'String', actual 'Unit' | refused | yes |
| unit | Name it 3 (`TODO()` in `fun price(): Int`) | compiles; prints `The program starts` | same | yes |
| unit | print-of-print | `a` `kotlin.Unit` (key b) | b | yes |
| unit | flag-in-a-name | `HI` `Got: kotlin.Unit` (key c) | c | yes |
| unit | todo-is-called | `Start`, then `kotlin.NotImplementedError: An operation is not implemented: ask the shop` on stderr (key a) | a, output `Start` | yes |
| scope | Name it 1 | `inside: 99` `outside: 10` | same | yes |
| scope | Name it 2 (`message` after the loop) | error: unresolved reference 'message' | refused | yes |
| scope | inner-total-hides | `99` `10` (key b) | b | yes |
| scope | street-person-changed | `12` (key c) | c | yes |
| scope | add-up-gives-zero | `0` (key a) | a | yes |
| what-is-null | Name it 1 | `Ada` `null` `false` | same | yes |
| what-is-null | `val firstName: String = null` | error: null cannot be a value of a non-null type 'String' | refused | yes |
| what-is-null | `middleName.length` on `String?` | error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?' | refused | yes |
| what-is-null | print-null-box | `Middle name: null` `Done` (key b) | b | yes |
| what-is-null | bang-on-empty-box | `Start`, then `java.lang.NullPointerException` (key a) | a | yes |
| what-is-null | empty-note-is-not-null | `false` `true` (key c) | c | yes |
| nullable | Name it 1 | `Elm Road` `null` `true` | same | yes |
| nullable | `val sure: String = flat` | error: initializer type mismatch: expected 'String', actual 'String?' | refused | yes |
| nullable | `flat.length` | error: only safe (?.) or non-null asserted (!!.) calls ... | refused | yes |
| nullable | seats-and-nothing | `3` `null` `false` (key b) | b | yes |
| nullable | which-lines-are-refused | errors on line 3 (`null cannot be a value of a non-null type 'String'`) and line 5 (expected 'String', actual 'String?') only | lines 3 and 5 (key c) | yes |
| nullable | parcel-in-maybe-door | `A1` `B2` (key a) | a | yes |
| safe-call | Name it 1 | `null` `3` `0` `4` | same | yes |
| safe-call | Name it 2 (`?: return`) | `Start` | same | yes |
| safe-call | chain-and-note | `4` `0` (key c) | c | yes |
| safe-call | answer-is-no-reply | `Size: null` `-1` (key b) | b | yes |
| safe-call | note-says-go-home | `Start` (key a) | a | yes |
| smart | Name it 1 | `3` `No other` | same | yes |
| smart | `var current` at top level, `current.length` | error: smart cast to 'String' is impossible, because 'current' is a mutable property that could be mutated concurrently | refused, other code could change it | yes |
| smart | copy into local `val` | `3` | same | yes |
| smart | check-then-use | `3` `Done` (key b) | b | yes |
| smart | send-away-then-use | `No note` (key c) | c | yes |
| smart | swap-after-use | `3` `null` (key a) | a | yes |
| when | Name it 1 (`current!!.length`) | `3` | same | yes |
| when | Name it 2 | `Age: 18` `43` | same | yes |
| when | parse-with-bang | `Start`, then `java.lang.NullPointerException` (key c) | c | yes |
| when | note-instead-of-oath | `Age: 18` `43` (key a) | a | yes |
| when | oath-checked-where-used | `3` `Done` (key b) | b | yes |

Interview probes (all on 2.4.21):
- `fun fact(n: Int) = if (n <= 1) 1 else n * fact(n - 1)` fails: "type checking has run into a recursive problem". With `: Int` it prints 120.
- `fun empty() = null` COMPILES (prints `null`); its inferred type is `Nothing?` and `empty()?.length` fails. The documentation says it "always require[s] a return type". See single item 2.
- `fun f(x: Int) { x * 2 }` returns `kotlin.Unit`; `fun sign(n: Int) = if (n < 0) "negative" else "not negative"` works.
- Smart cast on a `val` property in the same module: works. On a `var` property: refused ("mutable property that could be mutated concurrently"). Custom getter and `open val`: refused ("a property that has an open or custom getter"). Local `var` changed in a lambda: refused ("mutated in a capturing closure"). Public `val` of a class from another module: refused ("a public API property declared in different module"). `&&` and `||` right-hand side, early `return`: work. `if (pr != null) pr!!.length` on a `var` property compiles and runs.
- Java returning null: `val p = J.nul(); p.length` throws NullPointerException; `val s: String = J.nul()` throws NullPointerException ("nul(...) must not be null"); `val q: String? = J.nul()` is fine.
- `null!!` throws `java.lang.NullPointerException` with message null. `requireNotNull(x) { "msg" }` throws IllegalArgumentException "msg"; without a message "Required value was null."; `checkNotNull` throws IllegalStateException.
- `user?.name ?: "Guest"` gives Guest; `?: throw IllegalArgumentException("name expected")` works; `runIt { }` stored in a `val` prints `kotlin.Unit`.
- Name shadowing: a local `val price = 99` inside `if` inside `main` that has `val price = 10`, a local `val a` shadowing a parameter `a`, a loop-local `var total` shadowing an outer `var total`: all compile with NO warning. The compiler's own diagnostic list (`FirErrors`) in this jar has no NAME_SHADOWING entry, `-Xreport-all-warnings` printed only an unrelated warning (USELESS_CAST on a probe line), and `-Xwarning-level=NAME_SHADOWING:warning` failed with `warning with name "NAME_SHADOWING" does not exist`. See scope items 2 to 4.
- Use of a street name above an inner declaration of the same name: `val x = 1` top level, `println(x); val x = 2; println(x)` in `main` prints `1` then `2`. See scope item 1.

## Fact check (official pages read 21 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Single expression: braces optional after `=`; return type "most of the time" not needed; recursive functions and `fun empty() = null` "always require" a return type; `Number` example; `return` inside an expression body needs the type | kotlinlang.org/docs/functions.html | Matches, except `fun empty() = null` (single item 2) |
| Block body with no return type is Unit; "Unlike void ... Unit is an actual type"; you must write Unit in a function type such as `() -> Unit` | same | Matches |
| Unit "The type with only one value ... corresponds to the void type in Java" | kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/ | Matches |
| Nothing: built-in subtype of all types; never completes successfully (throws or infinite loop); `TODO()` placeholder always throws NotImplementedError; compiler marks code after a Nothing call unreachable | kotlinlang.org/docs/exceptions.html | Matches (the unreachable sentence is on the page; its example shows no code after the call) |
| `if` can be written as an expression and gives its value (used in single Q4) | kotlinlang.org/docs/control-flow.html | Matches: "You can also write `if` as an expression ... In this form, an `else` branch is required." The leaf attributes this to the documentation on `if`, but the page is missing from its SOURCES comment (single item 5) |
| Libraries state return types and property types explicitly | kotlinlang.org/docs/coding-conventions.html | Matches |
| Expression function `override fun toString(): String = "Hey"`; the type may be omitted | developer.android.com/kotlin/style-guide | Matches |
| Scope definition, nesting, function bodies are statement scopes, values accessible only after the declaration point, binding a name already available through linked scopes is allowed, the word "shadow" is not used | kotlinlang.org/spec/scopes-and-identifiers.html | Matches (the leaf says so itself) |
| `var name by remember { mutableStateOf("") }`, `onValueChange = { name = it }`, `onNameChange: (String) -> Unit`, `onValueChange: (T) -> Unit` "an event that requests the value to change", TextField "doesn't update itself" | developer.android.com/develop/ui/compose/state | Matches |
| `?` allows null; `a = null` on a non-null String; compiler error on `b.length`; `?.` returns null instead of an NPE; chain returns null if any part is null; Elvis returns the right side only if the left is null; `return` and `throw` are expressions usable on the right; `name expected` example; `!!` gives an NPE; the four-part list of the only causes of an NPE | kotlinlang.org/docs/null-safety.html | Matches |
| Smart cast prerequisites for `val` local, `val` property, `var` local, `var` property; negative check then return; `&&` and `||` | kotlinlang.org/docs/typecasts.html | Matches, except `val` local is "Always, except local delegated properties" (smart item 1) |
| Platform types, `T!` means T or T?, no compile-time nullability errors, assertion on assignment to a non-null type, assertions when passing platform values to Kotlin functions, annotated Java types become real nullable or non-null types | kotlinlang.org/docs/java-interop.html | Matches |
| Every non-primitive parameter, return and field type in a public API should have a nullability annotation; non-annotated types are platform types with ambiguous nullability | developer.android.com/kotlin/interop | Matches |
| `onCreate(savedInstanceState: Bundle?)`; "If the activity has never existed before, the value of the Bundle object is null" | developer.android.com/guide/components/activities/activity-lifecycle | Matches |
| `if (savedInstanceState == null)` adds the fragment once; "savedInstanceState is no longer null" after recreation | developer.android.com/guide/fragments/create | Matches |
| `val tipPercent = tipInput.toDoubleOrNull() ?: 0.0`; "This value could be null if the text field is empty" (the sentence is about `tipInput`) | developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip | Matches |
| `toIntOrNull` returns null if the text is not a valid Int | kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int-or-null.html | Matches (my run: `"12a"` gives null) |
| `requireNotNull` throws IllegalArgumentException, with lazyMessage; smart cast afterwards | kotlinlang.org/api/core/kotlin-stdlib/kotlin/require-not-null.html | Matches; `checkNotNull` IllegalStateException confirmed by my run |
| Function parameters are read-only inside the body (implicitly `val`) | kotlinlang.org/docs/functions.html (Parameters) | Page text confirmed from the docs source; smart item 5 uses it |

Could not verify from an official page:
- "The Kotlin compiler usually warns you" about shadowing. My run on 2.4.21 printed no warning (scope item 3). An editor such as Android Studio may show one, but I could not confirm that from an official page.
- That `Nothing`-typed calls make the next line "unreachable" as a compiler message: stated on the exceptions page, not demonstrated there, not shown by `kotlinc` output (the command-line compiler prints very few warnings). Leaf wording follows the page, so no FIX.
- Anything outside Kotlin 2.4.21 on the JVM. The leaves make no claim about other targets.

## Judgement on the sim deviations (rule 5)

- `knock-and-note` (safe-call-and-elvis) and `house-and-street-names` (scope-and-shadowing) replace the roadmap's Sim cells ("reuse null-lunch-box with a ?. gate", "nested coloured rooms; a variable is a ball"). Hard rule 5 protects the structure of the tree: ids, titles, difficulty, requires, fruit. Those are all unchanged and equal to the YAML. A Sim cell is a plan inside the table, V5 does not read it, and each deviation is logged with its reason in `docs/sims/needed-ids.md` (as in W5 for `mug-and-whiteboard-tag` and others). The roadmap's own Analogy cells ("Knocking on a door", "Two people called Sam") match the leaves, and a lunch-box sim inside a door-knocking leaf would break `docs/04` section 3.2 rule 4 (one analogy). Checklist item 8 ("frontmatter must match the roadmap row") is met for the keys V5 reads, but `sims` differs from the roadmap's Sim cell in these two leaves, and that is a real deviation. It is the right call because the roadmap row itself conflicts (door or house analogy plus a lunch-box or ball sim). Verdict: acceptable, no rule 5 breach. The Sim cells are now stale: owner item 1, which is a precondition for `housekeeper` flipping these two leaves to `live`, not only a tidy-up.
- `null-lunch-box` in `what-is-null` matches `docs/05` section 10: same id; the four Try-this items are word for word the ones in `docs/04` section 10 and match section 10.5 steps 1, 2, 4 and 5 ("Look inside first" kept, Q1); box labels "always has a sandwich" and "might be empty"; button names "Count the slices", Reset, "Look inside first"; the "What you should notice" sentence is the example's; the empty-note idea (`""` is a sandwich) is in Mental model as in the spec's objects table. The needed-ids line matches the spec. Pass.

## Leaf 1: single-expression-functions: FIX

1. Line 91. `block body` is a jargon term not defined on the page (checklist 3). Old: `The documentation says a function with a block body and no written return type is treated as returning `Unit`, which means nothing useful.` New: `The documentation says a function with braces and no written return type is treated as returning `Unit`, which means nothing useful.`
2. Line 217 (interview Q2). The sentence is 30 words and, as written, wrong on one point: it says you "must" write the type for `fun empty() = null`. The official page says that ("always require[s]"), but on Kotlin 2.4.21 that line compiles (inferred type `Nothing?`); only the self-calling function is really refused (my runs). Keep the leaf consistent with the official page by dropping the clause from the answer; line 74 already paraphrases the page ("write the type when the compiler can run into problems, for example ... `fun empty() = null`") and stays. The discrepancy is owner item 4. Old: `The documentation says Kotlin works it out for a single expression, but you must write it for a function that calls itself and for something like `fun empty() = null`.` New: `The documentation says Kotlin works it out for a single expression. You must write it for a function that calls itself, because the compiler refuses that function without one.`
3. Line 209 (Where this shows up in Android), 35 words (T7). Old: `This next part is our own reasoning: because an expression function holds one calculation, when a screen shows the wrong text, the cause is on that one line or in the values handed to it.` New: `This next part is our own reasoning. An expression function holds one calculation. So when a screen shows the wrong text, the cause is on that one line or in the values handed to it.`
4. `verified` (all three ran and match). Line 111: `verified: not-run` -> `verified: kotlin-2.4.21` (half-of-seven). Line 139 -> `verified: kotlin-2.4.21` (two-customers). Line 167 -> `verified: kotlin-2.4.21` (sign-inside-sign).
5. Line 18 (SOURCES comment). Q4 says "as the documentation on `if` says", but `kotlinlang.org/docs/control-flow.html` is not listed. I read it: it confirms the claim. Old: `developer.android.com/kotlin/style-guide (expression functions,` New: `kotlinlang.org/docs/control-flow.html (if can be written as an expression, an else branch is required); developer.android.com/kotlin/style-guide (expression functions,`

## Leaf 2: unit-and-nothing: FIX

1. Line 21 (The question). `null` is an unmet term on the first screenful (docs/04 section 3.1; it is defined only in Name it, line 69; the Kotlin branch meets null properly in `what-is-null`, which this leaf does not require). Old: `Is it null, zero, or something else?` New: `Is it zero, a blank, or something else?`
2. Line 209 (Common mistakes, item 2). Untrue "on the screen" claim (checklist 7; same as W3-a item): `println` writes to the output, not to an app screen. Old: `Text appears on the screen, so it feels like the answer.` New: `Text shows up when the function runs, so it feels like the answer.`
3. Line 225 (interview Q2). The "because it is a real type ... which is why you must write it" link is not what the functions page says, and the sentence is 33 words. Old: `My own reasoning: because it is a real type, it can appear where any type is expected, which is why you must write it in a function type such as `() -> Unit`.` New: `The functions page says you must write `Unit` in a function type such as `() -> Unit`. My own reasoning: a function type always names what its code gives back, and for code with nothing useful to give back the name is `Unit`.`
4. `verified` (all three ran and match). Line 118 `verified: not-run` -> `verified: kotlin-2.4.21` (print-of-print). Line 143 -> `verified: kotlin-2.4.21` (flag-in-a-name). Line 173 -> `verified: kotlin-2.4.21` (todo-is-called; stdout is `Start`, the error goes to stderr).

## Leaf 3: scope-and-shadowing: FIX

1. Line 72 (Build step 2). Incomplete and misleading for the street-and-house picture: a name used above an inner declaration of the same spelling reads the OUTER one (my run: top-level `val x = 1`, then in `main` `println(x)`, `val x = 2`, `println(x)` prints `1` then `2`). "Finds nobody" is only true when no outer person has that name. This is a classic interview trap. Old: `A call to them before that line finds nobody.` New: `A call before that line does not find them, so it keeps looking outwards.`
2. Line 85 (Name it). The compiler warning claim is not true for the pinned compiler and is not on any official page I read (evidence in "Interview probes"). The paragraph also has a 32-word sentence (T7). Old: `The specification does not use the word shadowing, but it does say that giving a name to a value is allowed even when the name is already available from an outer scope. The word shadowing is general knowledge. The Kotlin compiler usually warns you when you do it (also general knowledge), because the same spelling for two people causes mix-ups.` New: `The specification does not use the word shadowing. It does say that giving a name to a value is allowed even when the name is already available from an outer scope. The word shadowing is general knowledge. The same spelling for two people causes mix-ups, so many teams avoid it (my own reasoning). The compiler accepts the program below. Some editors show a warning for it (general knowledge).`
3. Line 218 (Common mistakes, item 3). Built on the same warning. Old: `**Ignoring the hidden-name warning.** The program still runs, so the warning feels harmless. It is the compiler telling you that two people share a name. Rename the inner one so the next reader does not need to count houses.` New: `**Leaving two people with one name.** The program still runs, so it feels harmless. But the next reader must count houses to know who answers. Rename the inner one.`
4. Line 237 (interview Q3). Same claim. Old: `The specification allows this, and the compiler usually warns about it (general knowledge).` New: `The specification allows this, and the compiler accepts it. Some editors show a warning (general knowledge).`
5. Line 226 (Android section), 33 words (T7). Old: `This next part is our own reasoning: if the code in the braces made a new `name` instead of giving a new value to the street's `name`, the real one would never change.` New: `This next part is our own reasoning. Suppose the code in the braces made a new `name` instead of giving a new value to the street's `name`. Then the real one would never change.`
6. `verified` (all three ran and match). Line 119 `verified: not-run` -> `verified: kotlin-2.4.21` (inner-total-hides). Line 149 -> `verified: kotlin-2.4.21` (street-person-changed). Line 178 -> `verified: kotlin-2.4.21` (add-up-gives-zero).

Note, not FIX: prose is about 1,690 words, over the 1,600 budget in `docs/04` section 6 (the interview section added by L33 is the reason). Owner item 3.

## Leaf 4: what-is-null: FIX

1. Line 220 (Where this shows up in Android). `nullability annotation` is used without a meaning (checklist 3 lists "annotation" among the Android terms to define). Old: `The Android Kotlin interop page says that Java types with no nullability annotation are read as platform types, which have ambiguous nullability.` New: `The Android Kotlin interop page says that Java types with no nullability annotation (a tag in the Java code that says whether a value may be null, general knowledge) are read as platform types, which have ambiguous nullability.`
2. Line 234 (interview Q4), 33 words (T7). Old: `If you put such a value in a non-null variable and it is null, Kotlin throws a NullPointerException, so write `String?` on your own variable or add nullability annotations in the Java code.` New: `If you put such a value in a non-null variable and it is null, Kotlin throws a NullPointerException. So write `String?` on your own variable, or add nullability annotations in the Java code.`
3. `verified` (all three ran and match). Line 122 `verified: not-run` -> `verified: kotlin-2.4.21` (print-null-box). Line 150 -> `verified: kotlin-2.4.21` (bang-on-empty-box; stdout is `Start`, NPE on stderr). Line 178 -> `verified: kotlin-2.4.21` (empty-note-is-not-null).

The interview answers check out: null is "no value" (general knowledge, labelled); the list of NPE causes matches the page (explicit throw, `!!`, initialisation data inconsistency, Java interoperation); platform type `String!` means `String` or `String?`, checks relaxed, assigning a null platform value to a non-null variable throws NullPointerException (my run confirmed, message "nul(...) must not be null").

## Leaf 5: nullable-types: FIX

1. Line 240 (interview Q4). `Java`, `nullability annotation` and `platform types` are used without meaning on this page (checklist 3; docs/04 section 7 says define each term on every page). Old: `The Kotlin documentation says Java types that carry nullability annotations are read as real nullable or non-null Kotlin types. Without annotations they become platform types, and the checks are relaxed. The Android interop page says every non-primitive parameter, return and field type in a public API should have a nullability annotation. So an annotation in Java gives your Kotlin code the right sticker.` New: `Java is an older language that many Android libraries use, and a Java type can carry a nullability annotation, a tag that says whether a value may be null (general knowledge). The Kotlin documentation says such a type is read as a real nullable or non-null Kotlin type. Without the tag it becomes a **platform type**, where Kotlin does not know and the checks are relaxed. The Android interop page says every non-primitive parameter, return and field type in a public API should have the tag.`
2. Line 226 (Android section), 34 words (T7). Old: `This next part is our own reasoning: if Kotlin let you reach into `savedInstanceState` without a look, the code would work after the screen has been rebuilt and crash on the very first open.` New: `This next part is our own reasoning. Suppose Kotlin let you reach into `savedInstanceState` without a look. The code would work after the screen has been rebuilt and crash on the very first open.`
3. `verified` (all three ran and match). Line 125 `verified: not-run` -> `verified: kotlin-2.4.21` (seats-and-nothing). Line 156 -> `verified: kotlin-2.4.21` (which-lines-are-refused; I compiled the five lines and got exactly the errors on lines 3 and 5). Line 182 -> `verified: kotlin-2.4.21` (parcel-in-maybe-door).

Note, not FIX (accepted in W4-a and W5-a): `which-lines-are-refused` has no `fun main` and its `output` is a sentence, because the real result is a compile error with no stdout. `run: false` items show `output` as the answer (`docs/06` section 3.2).

## Leaf 6: safe-call-and-elvis: FIX

1. Line 81 (Name it). `expression` is used without meaning on this page (the earlier definition is in another leaf; docs/04 section 7). Old: `The documentation says that if the expression on the left of `?:` is not `null`, it is returned.` New: `The documentation says that if the **expression** on the left of `?:` (a piece of code that works out a value) is not `null`, it is returned.`
2. Line 219 (Android section), 32 words (T7). Old: `This next part is our own reasoning: with `!!` in place of the note, an empty box would close the app, and a user who leaves it empty would see the crash.` New: `This next part is our own reasoning. With `!!` in place of the note, an empty box would close the app. A user who leaves it empty would see the crash.`
3. `verified` (all three ran and match). Line 118 `verified: not-run` -> `verified: kotlin-2.4.21` (chain-and-note). Line 147 -> `verified: kotlin-2.4.21` (answer-is-no-reply). Line 176 -> `verified: kotlin-2.4.21` (note-says-go-home).

Sim deviation `knock-and-note`: acceptable, see above. The Try-this list (four items, the last an optional reveal) matches the needed-ids line.

## Leaf 7: smart-casts: FIX

1. Line 72 (table, `val` local row). Documentation says "Always, except local delegated properties". Old: `| A `val` made inside a function | Always |` New: `| A `val` made inside a function | Always, except a local delegated property, which is not covered here |`
2. Line 74 (table, `val` property row). Leaves out the other-module rule, which interviewers ask about; my compile confirmed it ("a public API property declared in different module"). Old: `It does not work on a property marked `open` or on one with a custom getter. Both belong to classes |` New: `It does not work on a property marked `open` (a class may replace it), on one with a custom getter (code that runs each time it is read), or on one declared in another module. The first two belong to classes |`
3. Line 236 (Android section). Bold the Android terms at first use (checklist 3). Old: `The Android page on creating fragments, which are reusable parts of a screen, shows` New: `The Android page on creating **fragments**, which are reusable parts of a screen, shows` and old: `An activity is one screen of the app.` New: `An **activity** is one screen of the app.`
4. Line 238 (Android section), 39 words (T7). Old: `This next part is our own reasoning: in the other branch, after a check for `!= null`, the code can use the `Bundle` straight away, because function inputs are read-only (general knowledge) and the smart cast works on them.` New: `This next part is our own reasoning. In the other branch, after a check for `!= null`, the code can use the `Bundle` straight away. The Kotlin functions page says function parameters are read-only, so the smart cast works on them.`
5. Line 246 (interview Q2). Add the other failing cases, all confirmed by compile. Old: `So the check proves nothing about the next line. My own reasoning:` New: `So the check proves nothing about the next line. A `val` property also fails when it is `open`, has a custom getter, or is declared in another module. My own reasoning:`
6. Line 252 (interview Q4), 37 words (T7). Old: `The documentation says the compiler knows a cast is safe if a negative check leads to a return, and that it can smart cast on the right of `&&` and `||` after a check on the left.` New: `The documentation says the compiler knows a cast is safe if a negative check leads to a return. It also says the compiler can smart cast on the right of `&&` and `||` after a check on the left.`
7. `verified` (all three ran and match). Line 129 `verified: not-run` -> `verified: kotlin-2.4.21` (check-then-use). Line 163 -> `verified: kotlin-2.4.21` (send-away-then-use). Line 193 -> `verified: kotlin-2.4.21` (swap-after-use).

## Leaf 8: when-to-use-double-bang: FIX

1. Line 216 (Where this shows up in Android, para 2). `activity` is used (in "the Android activity page") before it is defined, and it is not bold. Old: `Another place is the Android activity page. It shows `onCreate(savedInstanceState: Bundle?)` and says that if the activity has never existed before, the value of the `Bundle` object is `null`. An activity is one screen of the app. Our own reasoning:` New: `Another place is the Android page about an **activity**, which is one screen of the app. It shows `onCreate(savedInstanceState: Bundle?)` and says that if the activity has never existed before, the value of the `Bundle` object is `null`. Our own reasoning:`
2. Line 218 (Android para 3). `Java`, `nullability annotation` and `platform type` are used without meaning on this page (checklist 3; docs/04 section 7). Old: `Values from Java are the third place. The Android interop page says every non-primitive parameter, return and field type in a public API should have a nullability annotation. Without one, Kotlin sees a platform type, and it cannot tell you whether the value can be missing.` New: `Values from Java, an older language that many Android libraries use, are the third place. A nullability annotation is a tag in the Java code that says whether a value may be null (general knowledge). The Android interop page says every non-primitive parameter, return and field type in a public API should have one. Without one, Kotlin sees a **platform type**, which means it cannot tell you whether the value can be missing.`
3. Line 72 (Name it). `Elvis operator` is used without meaning on this page (the earlier definition is in another leaf; docs/04 section 7). Old: `which uses `throw` on the right of the Elvis operator: `?: throw IllegalArgumentException("name expected")`.` New: `which uses `throw` on the right of the Elvis operator, `?:`, the fallback that is used only when its left side is `null`: `?: throw IllegalArgumentException("name expected")`.` The recap's "safe call" (line 238) was defined in `safe-call-and-elvis`, not here; add after it `(`?.`, a call that gives `null` instead of crashing)` in line 238: Old: `3. Otherwise use a safe call, a fallback, or `requireNotNull` with a message.` New: `3. Otherwise use a safe call (`?.`, which gives `null` instead of crashing), a fallback, or `requireNotNull` with a message.`
4. Line 214 (Android section), 31 words (T7). Old: `This next part is our own reasoning: if that line ended in `!!`, the app would close whenever the box is empty, and every test with a filled box would pass.` New: `This next part is our own reasoning. If that line ended in `!!`, the app would close whenever the box is empty. Every test with a filled box would still pass.`
5. Line 226 (interview Q2), 30 words (T7). Old: `My own reasoning: use `require` when a caller handed you something bad and `check` when your own state is wrong, and write a message so the crash report explains itself.` New: `My own reasoning: use `require` when a caller handed you something bad and `check` when your own state is wrong. Write a message either way, so the crash report explains itself.`
6. `verified` (all three ran and match). Line 114 `verified: not-run` -> `verified: kotlin-2.4.21` (parse-with-bang; stdout `Start`). Line 142 -> `verified: kotlin-2.4.21` (note-instead-of-oath). Line 172 -> `verified: kotlin-2.4.21` (oath-checked-where-used).

Interview answers check out: `!!` throws NullPointerException with no message (my run: message null); `requireNotNull` IllegalArgumentException with the lambda message, `checkNotNull` IllegalStateException (run); toIntOrNull of `"12a"` is null; assignment of a null platform value to a non-null variable throws NullPointerException (run).

## After the fixes

Run `./gradlew :site:validateContent` once more. No other check is needed (no frontmatter or structure change).

## Owner items (outside the leaves)

1. `content/kotlin/_roadmap.md`, Sim cells of `safe-call-and-elvis` (now `knock-and-note`) and `scope-and-shadowing` (now `house-and-street-names`) are stale. Through `curriculum-designer` (rule 5), align the two cells with `docs/sims/needed-ids.md`, as the W5 cells were. Not read by V5, so the fixes are not blocked, but this must be done before `housekeeper` flips `safe-call-and-elvis` or `scope-and-shadowing` to `live` (checklist item 8).
2. The roadmap Challenges cells list `F`, `F(ai)` and `B` tasks for all eight leaves, and the `null-safety` stem carries `ai-review`. The leaves ship Predict-only (`playground: false`), so no Fix the bug and no five reviewer questions. This follows the W4-a and W5-a decision for Kotlin leaves; confirm it is still wanted for the `ai-review` stem when Phase 6 (the Playground) lands, or the F tasks must be added.
3. Word budget: `scope-and-shadowing` (about 1,690) and `when-to-use-double-bang` (about 1,675) exceed the 1,600-word prose budget of `docs/04` section 6 by about 5 percent, because of the interview section (L33). Either widen the budget in `docs/04` section 6 or accept. Not a FIX.
4. The official functions page says `fun empty() = null` "always require[s]" a return type, but kotlinc 2.4.21 compiles it. Possible upstream docs issue; the leaf wording after FIX 2 avoids depending on it.
5. kotlinc 2.4.21 prints no name-shadowing warning. If the author wants the "warning" fact back in the leaf, it needs an IDE check (Android Studio) that I could not make.
