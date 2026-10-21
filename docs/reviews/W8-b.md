# Review W8-b

Reviewer: independent. Scope: three shadow leaves checked against `docs/lesson-checklist.md`.
- `content/kotlin/interfaces-and-inheritance/abstract-classes.md`
- `content/kotlin/interfaces-and-inheritance/polymorphism-and-type-checks.md`
- `content/kotlin/special-classes/data-classes.md`

No reviewed file was edited. Compiler: `scripts/kt-run.sh` (Kotlin 2.4.21). `./gradlew :site:validateContent --offline` ran clean (no errors).

Per leaf: abstract-classes FIX (mechanical), polymorphism-and-type-checks FIX (mechanical, plus one owner item), data-classes FIX (mechanical).

## Compile and run results

Every `kotlin` block and every Predict `code` block was extracted and run. No compiler warnings.

| Leaf | Block | Result |
|---|---|---|
| abstract-classes | main snippet | `Rex says Woof` / `Pip says Tweet` (as claimed) |
| abstract-classes | `Pet("Rex")` | fails: "cannot create an instance of an abstract class" (as claimed) |
| abstract-classes | interface `var count: Int = 0` | fails: "property initializers in interfaces are prohibited" (as claimed) |
| abstract-classes | Predict `pets-fill-the-gap`, `each-house-own-count`, `lamp-switch-state` | match `output:` and the `correct: true` option (c, b, a) |
| polymorphism | snippet 1 | `hammer bang saw hammer bang` (as claimed) |
| polymorphism | snippet 2 | `bang`, `null` (as claimed) |
| polymorphism | `t.swing()` on `Tool` | fails: unresolved reference 'swing' (as claimed) |
| polymorphism | Predict `when-over-mixed-box`, `polite-cast-gives-nothing`, `swing-without-looking` | `cut zzz/hit bang`; `bang/null`; `start` then `ClassCastException: Saw cannot be cast to Hammer`. All match; stdout is only `start` (stderr excluded, correct) |
| data-classes | main snippet | `true false Receipt(item=milk, total=5) false` (as claimed) |
| data-classes | `data class Receipt(item: String, ...)` | fails: "primary constructor of data class must only have property ('val' / 'var') parameters" (as claimed) |
| data-classes | Predict `data-versus-plain-equals`, `pencil-note-is-ignored`, `reading-a-book-aloud` | all match |

Predict outputs need no fill-in. They are present and correct. All nine still say `verified: not-run` (FIX 2).

## Facts checked against page bodies (fetched 21 Oct 2026)

- classes.html: abstract classes "can't be instantiated directly"; "can have both abstract and non-abstract members"; "you must use the abstract keyword explicitly"; "You don't need to annotate abstract classes or functions with the open keyword because they are implicitly inheritable by default"; abstract members are overridden in a subclass with `override`. All confirmed.
- interfaces.html: "interfaces cannot store state"; "Properties declared in interfaces can't have backing fields"; "A class or object can implement one or more interfaces". Confirmed.
- data-classes.html: derived members (`equals()`/`hashCode()` pair, `toString()` as `User(name=John, age=42)`, `componentN()`, `copy()`); at least one parameter; all `val`/`var`; "can't be abstract, open, sealed, or inner"; body properties excluded, with the same `Person(name)` / `age` example. Confirmed.
- equality.html: `==` is structural via `equals`, `===` is referential; the default `equals` is referential; non-data classes do not override it. Confirmed.
- typecasts.html: `is` checks "at runtime"; compiler tracks checks for immutable values and inserts casts; failed `as` throws `ClassCastException` and is "the unsafe operator"; failed `as?` returns `null` and is "the safe operator". Confirmed.
- ViewModel API reference body: `public abstract class ViewModel`. Confirmed from the page body, so the VERIFY note at abstract-classes line 20 is resolved.
- state-production page body: `class DiceRollViewModel : ViewModel()` and `data class DiceUiState(...)` (StateFlow tab) both exist. Confirmed.
- Room page body: "Each instance of User represents a row in a user table in the app's database", with `@Entity data class User(...)`. Confirmed.

Could not verify: none. Items labelled "my own reasoning" or "general knowledge" in the leaves are correctly labelled and I did not treat them as official claims.

## Checklist items 1 to 13

Pass: recap is one sentence per line (1); Question blocks are 5 sentences or fewer (2); inferred and general claims are labelled (4); `output` blocks are stdout only (5); correct letters vary and `why` lines lead to the option (6); no "next lesson" language (7); frontmatter id, title, difficulty, requires, fruit and order match the roadmap rows (order 47, 48, 49 match roadmap position) (8); SOURCES comment at line 18 (9); Interview section placed after Android, before Recap, 4 questions each (11).

## FIX list

1. Mechanical (`fixer`), checklist item 9. Remove the stray VERIFY comments now resolved: abstract-classes lines 19 and 20 (compile now run; ViewModel `abstract` read from the API reference body); polymorphism line 19; data-classes line 19. Keep a note only for polymorphism line 20 (sim spec missing, see FIX 7).
2. Mechanical (`fixer`/`housekeeper`), checklist item 5. Change `verified: not-run` to `verified: kotlin-2.4.21` for all nine Predict items: abstract-classes (`pets-fill-the-gap`, `each-house-own-count`, `lamp-switch-state`), polymorphism (`when-over-mixed-box`, `polite-cast-gives-nothing`, `swing-without-looking`), data-classes (`data-versus-plain-equals`, `pencil-note-is-ignored`, `reading-a-book-aloud`). Same treatment as W7-b item 5.
3. Mechanical, abstract-classes, checklist item 3. Jargon used with no definition: "backing fields" (table line 73, Common mistakes line 287, interview line 298; gloss as "a hidden spot that stores the value"); "instantiated" (Name it line 66, interview line 298; add "made" in brackets); "members" and "body" (lines 66 and 298; one short gloss each at first use).
4. Mechanical, polymorphism-and-type-checks, checklist item 3. Line 87 uses "runtime" and "operator" with no definition, and line 89 uses "exception" via `ClassCastException`. Add a short gloss at first use ("runtime means while the program is running"; "an exception is a crash signal that stops the program").
5. Mechanical, data-classes Recap line 247. "A plain class treats two objects as equal only if they are the same one" is true by default only (a plain class can override `equals`, per equality.html). Add "by default".
6. Owner / `lesson-writer`, optional, checklist item 13 (deferrable as in W7-b item 2). Seeds in `docs/practice/interview-kotlin.md` not used: all three build exercises (abstract base class with state; mixed shape list with `is` and an unsafe cast failure; data class print and equality) have no hands-on task; wrong belief "`IntArray` in a data class compares by contents" and the mid-level question on arrays and mutable collections (data-classes); wrong belief "abstract properties have backing fields" (abstract-classes). The practice file lists `https://kotlinlang.org/docs/type-checks.html` which returns 404; the leaf correctly uses `typecasts.html`, so keep that.
7. Owner / `physics-designer`, polymorphism-and-type-checks line 41 and 12. Sim `mixed-tools-chute` is in `docs/sims/needed-ids.md` (line 38) but `docs/sims/mixed-tools-chute.md` does not exist. The leaf must not go `live` until the spec and implementation exist (the leaf's "Try this" steps describe a sim that is not built). Not a text defect.
8. Note for the orchestrator, no change to the leaf. data-classes `requires: kotlin/classes-and-objects/two-kinds-of-equality` is a valid roadmap id (position 42), but no content file exists for it yet. Publish that leaf before or with data-classes.

VERDICT: FIX

## Fixes applied

- FIX 1: stray VERIFY comments removed (abstract-classes 19-20; polymorphism 19; data-classes 19). Polymorphism line 20 note kept.
- FIX 2: `verified: not-run` changed to `verified: kotlin-2.4.21` in all nine Predict blocks across the three leaves.
- FIX 3: abstract-classes glosses added for "backing fields", "instantiated (made)", "members (functions and properties)", "body (no code)".
- FIX 4: polymorphism glosses added for "runtime" and "exception".
- FIX 5: data-classes line 247 now reads "...the same one by default."
- Check: `./gradlew :site:validateContent` ran OK (validateContent: ok, BUILD SUCCESSFUL).
- FIX 6 to 8 not in scope (owner and orchestrator items left open).

VERDICT (re-check, final): PASS for all 3 leaves
