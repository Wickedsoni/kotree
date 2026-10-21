# Review W7-b

Reviewer: independent. Scope: four shadow leaves, checked against `docs/lesson-checklist.md` and `content/_templates/leaf.md`.
Compiler: `scripts/kt-run.sh` (Kotlin 2.4.21). No reviewed file was edited.

VERDICT: FIX (minor; all four leaves compile and every stated output is correct)

Per leaf: closures PASS, what-is-a-class PASS, constructors PASS, map-filter-and-friends FIX (item 1 only).

## Compile and run results

Every `kotlin` block and every Predict `code` block was extracted and run. No compiler warnings were produced.

| Leaf | Block | Result |
|---|---|---|
| map-filter-and-friends | main snippet | prints exactly the six values in the prose (`[BRAEBURN, FUJI, PIPPIN]` ... `null`, `21`) |
| map-filter-and-friends | `// does not compile` | fails: "return type mismatch: expected 'Boolean', actual 'String'" (as claimed) |
| map-filter-and-friends | Predict `order-of-the-stations`, `basket-stays-full`, `repaint-where-test-meant` | stdout matches `output:` and the `correct: true` option in all three |
| closures | both snippets | `I see a tulip` / `I see a rose`; `1 2 1` (as claimed) |
| closures | Predict `two-windows-one-plant`, `made-early-looked-late`, `photo-in-a-pot` | `count is 2`; `n is 3` twice; `11`. All match key and `output:` |
| what-is-a-class | main snippet | `icing: plain` / `icing: pink` (as claimed) |
| what-is-a-class | `// does not compile` | fails: unresolved reference 'icing' (as claimed) |
| what-is-a-class | Predict `three-cookies-one-change`, `show-your-icing`, `fixed-spot-and-open-spot` | `pink plain plain`; `icing: pink`/`icing: plain`; `star pink`. All match |
| constructors | snippets 1 and 2 | outputs match the prose exactly |
| constructors | `// does not compile` | fails: unresolved reference 'name' (as claimed) |
| constructors | Predict `checks-run-per-form`, `top-to-bottom-in-the-design`, `pre-filled-answers` | all match key and `output:` |

Predict outputs need no fill-in. They are already present and correct.

## Facts checked against official docs

All fetched and confirmed:
- collection-filtering, collection-transformations, collection-grouping, collection-elements, collection-aggregate, collection-ordering: quotes on `filter` (original unchanged, result is a `List`), `map` (same order), `groupBy` (Map of key to List), `first`/`firstOrNull`, `sumOf`, `sortedBy` (new collection).
- Compose lists page: `groupBy { it.firstName[0] }`, the comment "This ideally would be done in the ViewModel", and `items(messages) { message -> MessageRow(message) }`.
- lambdas.html: closure and "captured variables can be modified in the lambda".
- Compose state page: `var name by remember { mutableStateOf("") }` and `onValueChange = { name = it }`. The page does not use the word "closure", as the leaf says.
- classes.html: no `new`; `val`/`var` in primary constructor; defaults; `init` blocks run in order with property initializers; secondary constructors must delegate; parameters without `val`/`var` are not stored. The `require(age > 0)` example exists. The secondary-constructor example `constructor(name: String, age: String) : this(name, age.toIntOrNull()...)` exists, so the interview claim "text turned into a number" is true.
- kotlin-tour-classes: recommends `val` unless the value must change.
- Activities page: `class MyActivity : ComponentActivity()`, no `main`, "`onCreate` ... fires when the system creates your activity".
- ViewModel factories page: dependencies as constructor parameters; `MyViewModel(myRepository, savedStateHandle)`; no factory for no dependencies or only `SavedStateHandle`.

Could not verify: none. Minor wording: the leaf says the documentation "calls" things; the tour's exact line is "Characteristics of a class's object can be declared in properties". This is a paraphrase, not a fault.

## Checklist items 1 to 11

Recap is one sentence per line, Question blocks are 5 sentences or fewer, correct letters vary, wrong options are plausible, the Interview section is placed correctly with 4 questions each, the SOURCES comment is at line 18, no VERIFY marks remain, frontmatter matches the roadmap rows (id, title, difficulty, requires, fruit), each SIM marker appears once, and all three sim ids are in `docs/sims/needed-ids.md`. All `requires` ids exist in the roadmap. Predict items are `run: false` with `verified: not-run`, which fits `playground: false`.

## FIX list

1. map-filter-and-friends.md, whole leaf (checklist item 13): `docs/practice/interview-kotlin.md` (section `kotlin/lambdas/map-filter-and-friends`) was not used. Missing: the eager versus lazy `Sequence` question and its answer points; the seeded wrong belief "`asSequence()` is always faster"; the build exercise (map/filter chain eager vs lazy); `https://kotlinlang.org/docs/sequences.html` in SOURCES. Owner/lesson-writer needed, not mechanical. The sequences concept is not taught elsewhere in the roadmap, so an acceptable alternative is for the owner to decide to defer it with a note in the interview answer.
2. Optional, low priority, lesson-writer: closures, what-is-a-class and constructors also leave out parts of their seeded data. Examples: the "closure cannot mutate" wrong belief and the `Ref` wrapper note; "classes are final by default" and the visibility default; the senior question on `init` reading a property declared further down. The leaves cover the core question of each. Fine to defer, because those seeds need inheritance or concurrency knowledge that the prerequisites lack.
3. Mechanical (`fixer`), checklist item 3: bold the jargon at first use. map-filter-and-friends: `Map` (Name it, groupBy bullet) and `ViewModel` (Android section). closures: **scope** (Name it, "outer scope") and **lambda** (Mental model, "is called a lambda"). constructors: **framework** is used in the Android section without a definition. Add a short gloss or reword.
4. Mechanical, constructors.md "Guess" section: the second Guess (order of pre-printed lines and checks) gives only "Idea B is the answer." with no reason for A and C (checklist item 6, "why must lead to the option"). Add one sentence each.
5. Mechanical, optional (`housekeeper`): the 12 Predict items now have a real run on 2.4.21 behind them. If the owner wants this recorded, change `verified: not-run` to `verified: kotlin-2.4.21` per the template. Leave it as is if paper-and-pencil leaves are meant to keep `not-run`.
6. map-filter-and-friends.md line 35: "Kotlin finishes one whole station before the next one starts (general knowledge)" is true for lists but not for `Sequence`. Mechanical: add "on a list" to the sentence.
