# Review W3-a: four programming-thinking shadow leaves (list, function)

VERDICT: FIX (3 leaves FIX, 1 PASS; all items mechanical, none needs the owner)

Reviewer: independent (did not write the leaves). Gate: L31, voice L29, `docs/04` §8, `docs/06` §3 and §5. Date 20 Oct 2026.
Sources reached: kotlinlang.org, developer.android.com. No `kotlinc`: every output and every Predict key was re-traced by hand (below). `./gradlew :site:validateContent` run once: `validateContent: ok`, BUILD SUCCESSFUL.
Policy applied (L31): a SIM marker with no built sim does not block (sims `post-office-boxes`, `basket-checkout-loop`, `recipe-card-call` are listed in `docs/sims/needed-ids.md`); the line-18 `SOURCES` comment is for `housekeeper`.

| Leaf | Verdict | Items |
|---|---|---|
| `what-is-a-list` | PASS | 0 |
| `loop-over-a-list` | FIX | 2 mechanical |
| `what-is-a-function` | FIX | 2 mechanical |
| `function-inputs-and-outputs` | FIX | 3 mechanical |

Checked and OK on all four: frontmatter keys and order; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/foundation/_roadmap.md` lines 97-116); `order` 17 to 20; `sims` equals the SIM marker (`[]` for inputs-and-outputs, whose roadmap row says `none:` with a reason, so no See it move); `playground: false`, every Predict `run: false` and `verified: not-run`, no Try it, no Fix the bug; 12-heading order with exact names; The question is 4 or 5 sentences and has no code or bold; Build the logic has no Kotlin syntax in Notice, Guess or Build; one analogy and "Where the analogy stops" present; each Predict has 3 options, one `correct`, a `why` on every option; Recap is three one-sentence lines under 20 words; prose 1,156 to 1,325 words (budget 900 to 1,600); at most 5 kotlin blocks per leaf; no emoji, no non-ASCII outside the SOURCES comments, no `!` in prose; T4 banned-word grep: zero body hits; no next-lesson promise, no video, no capstone, no "coming soon" text; every "You can now" line has a matching Predict item; every Android section is reading-only and names a symptom, with own-reasoning labelled "my own reasoning"; every `output` block is the real stdout for its program (multi-line ones use `|`).

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| `List.get` throws `IndexOutOfBoundsException` if index is less than zero or greater than or equal to size | kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/get.html | Matches (the page sample `list[3]` "will fail with IndexOutOfBoundsException") |
| First element has position 0, last has position size - 1; `first()` and `last()` aliases | kotlinlang.org/docs/collection-elements.html | Matches |
| `for (item in collection)`, `indices` for index loops, `1..<6` | kotlinlang.org/docs/control-flow.html (per prior W2 reads; SOURCES quotes agree) | Matches |
| `fun` declares functions; parameters need types, comma separated; return type after `()` with a colon; `return` "exit or return something"; `Unit` "a type with only one value"; named arguments allow any order; `sum` prints 3 | kotlinlang.org/docs/kotlin-tour-functions.html | Matches, quotes exact |
| `main` is the entry point | kotlinlang.org/docs/basic-syntax.html (per SOURCES) | Matches |
| `Greeting(names: List<String>)` with `for (name in names)`; "takes in a list of names and generates a greeting for each user" | developer.android.com/develop/ui/compose/mental-model | Matches |
| "build your user interface by defining a set of composable functions that take in data and emit UI elements"; "Composable functions can accept parameters, which let the app logic describe the UI"; "All composable functions must have this annotation" | same page | Matches. The page gives no definition of "composable function" (see inputs FIX 3) |
| `MessageList(messages: List<Message>)` with `Column { messages.forEach { ... } }` | developer.android.com/develop/ui/compose/lists | Matches |

Could not verify from an official page (accepted, none blocks): "a function runs only when called" (language semantics); the cart-total, empty-chat, blue/green button and swapped-title symptoms (own reasoning, labelled as such); the compile error for `val kept: Int = showDouble(4)` (derived, follows from `Unit` being the return type; not run, no message quoted); no compiler warning for an unused return value or an unused top-level function (believed default-off, not run).

## Hand-trace (mine, independent)

what-is-a-list: `listOf("Ada","Ben","Cy")`: `names[0]` Ada, `names[2]` Cy, size 3. `names-door-one` key b (Ben) correct. `door-three-of-three`: `Start` printed, `names[3]` throws, `End` never prints; stdout is `Start`; key c correct. `team-size-and-last`: size 5, `team[4]` is Ed (Ada 0, Ben 1, Cy 2, Di 3, Ed 4); key a correct, options b and c are believable slips.
loop-over-a-list: Name it: 3, 8, 10, 14; prints 14. `cart-total-thirteen`: 4+1+6+2 = 13, key c correct, "12" is one short, "2" is the last price. `wipe-inside-the-loop`: last round sets 0 then adds 2, prints 2, key a correct. `start-at-place-one`: size 4, `1..<4` is 1, 2, 3, so 5+2+4 = 11, key b correct; 14 is the full sum, 9 skips the 5.
what-is-a-function: Name it prints Hello, Welcome, Between, Hello, Welcome (text agrees). `hello-called-twice`: Hello twice, key c. `card-never-called`: only Start, key a. `call-in-the-middle`: One, Hello, Two, key b.
function-inputs-and-outputs: Name it: `kept` 8, prints 9, then `showDouble(4)` prints 8 (text agrees). `double-of-double`: 3+3 = 6, 6+6 = 12, key a. `answer-thrown-away`: only Done, key c. `swapped-arguments`: `twiceFirst(1,2)` = 1+1+2 = 4; `twiceFirst(2,1)` = 2+2+1 = 5; key b; `output` is 4 then 5.

## Leaf 1: what-is-a-list: PASS

No FIX items. Optional notes (not FIX):
- Line 100: on the JVM `listOf` of several items is an array-backed list, so the real exception class may be `ArrayIndexOutOfBoundsException` (a subclass of `IndexOutOfBoundsException`). The leaf names the documented class and never prints a message, so it is accurate. Do not paste a stack trace in later.
- Line 100: the word "exception" is never defined; the sentence reads fine as "an error with this name". Optional: `stops with an error called IndexOutOfBoundsException`.
- Line 87: the docs say "position", the page says "index"; it is clear from context.

## Leaf 2: loop-over-a-list: FIX

1. File `loop-over-a-list.md`, line 90 (Name it). "range" is used without being defined or bolded on this page (`docs/04` §7: define each term at first use, even if an earlier leaf did). Old: `In a counting loop it held a number from a range.` New: `In a counting loop it held one number at a time.` (mechanical)
2. File `loop-over-a-list.md`, line 105 (Name it). "index" is a new technical term, defined in the sentence but not bold (T2). Old: `This number is called its index.` New: `This number is called its **index**.` (mechanical)

After this, PASS. Optional: Try this item 2 (start the display at 10) is not echoed in "What you should notice"; harmless.

## Leaf 3: what-is-a-function: FIX

1. File `what-is-a-function.md`, line 84 (Name it). "entry point" is jargon used without a definition. Old: `The Kotlin documentation says the entry point of a Kotlin program is the `main` function.` New: `The Kotlin documentation says a Kotlin program starts in the `main` function.` (mechanical)
2. File `what-is-a-function.md`, line 209 (Where this shows up in Android). "composable functions" and "annotation" are used without a definition, and "annotation" is explained but not bold (T2). Old: `The Android documentation says that in Jetpack Compose you build your user interface by defining a set of composable functions. Jetpack Compose is the modern toolkit for building Android UI. The documentation says all composable functions must have the `@Composable` annotation, which is a label written above the function.` New: `Jetpack Compose is the modern toolkit for building Android UI. The Android documentation says you build your user interface by defining a set of **composable functions**, which take in data and emit UI elements. It says every one must have the `@Composable` **annotation**, a label written above the function.` Keep the last sentence ("When one function describes the button, every screen calls the same card."). (mechanical)

After this, PASS. Optional: line 33 is one 33-word sentence (a list of actions); splitting after "goes to the card" would meet T7.

## Leaf 4: function-inputs-and-outputs: FIX

1. File `function-inputs-and-outputs.md`, line 96 (Name it). `Unit` is a new term, defined but not bold (T2). Old: `has the return type `Unit`, a type with only one value.` New: `has the return type **`Unit`**, a type with only one value.` (mechanical)
2. File `function-inputs-and-outputs.md`, line 208 (Common mistakes, item 1). "both show a number on the screen" is untrue as written: a function that only returns shows nothing, which is the lesson. Old: `When you run the program, both show a number on the screen.` New: `On the screen, `showDouble(4)` and `println(double(4))` look the same: both show 8.` (mechanical)
3. File `function-inputs-and-outputs.md`, line 216 (Where this shows up in Android). "composable functions" is not defined, and "Jetpack Compose" is used one sentence before it is explained. Old: `The Android documentation says that in Jetpack Compose you define composable functions that take in data and emit UI elements. It adds that composable functions can accept parameters, which let the app logic describe the UI. Jetpack Compose is the modern toolkit for building Android UI.` New: `Jetpack Compose is the modern toolkit for building Android UI. The Android documentation says you build it from **composable functions**, which take in data and emit UI elements. It adds that composable functions can accept parameters, which let the app logic describe the UI.` (mechanical)

After this, PASS. Optional (owner or `curriculum-designer`): the roadmap `F:` task ("a function that prints the answer but returns nothing") is covered only by the `// does not compile` block in Name it, not as a Predict item; `answer-thrown-away` is the opposite case. The accepted W1-b/W2 pattern delivers `F:` as a Predict; consider a fourth item is not allowed (limit 3), so swap or accept as is. Not blocking.

## Cross-leaf notes (not FIX)

- Answer position: keys are a, b, c, then c, b, a (list leaf mixes well); function leaves also mix a, c, b. No pattern problem this wave.
- Housekeeper at go-live: strip the line-18 SOURCES comment from each leaf; keep the `<!-- SIM: ... -->` markers; the three sims (`post-office-boxes`, `basket-checkout-loop`, `recipe-card-call`) remain one-line entries in `docs/sims/needed-ids.md` and do not block under L31.
- `basket-checkout-loop` Try this item 2 needs a "starting number" control in the built sim (the spec line in `needed-ids.md` has it; keep it).
