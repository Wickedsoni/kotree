# W9-b review: objects-are-references

Reviewer: independent. Date: 21 Oct 2026. File reviewed (not edited): `content/kotlin/classes-and-objects/objects-are-references.md`.
Checked against `docs/lesson-checklist.md` (1 to 13), `docs/04-lesson-system.md` sections 4, 7, 8, and `content/kotlin/_roadmap.md` (row at line 241 and mnemonic row at line 510).

## Compile and run (scripts/kt-run.sh, Kotlin 2.4.21)

| Block | Result |
|---|---|
| Name it 1 (`val b = a`) | Compiles, no warnings. Prints `blue`, `blue`. Matches prose. |
| Name it 2 (`repaint(mine)`) | Compiles. Prints `green`. Matches. |
| Name it 3 (`// does not compile`) | Fails: `error: 'val' cannot be reassigned` (line `flat = Flat("green")`). Correct, and matches the functions page example. |
| Name it 4 (`b = Flat(a.sofa)`) | Compiles. Prints `red`, `red`, `blue`. Matches. |
| Predict spare-key-and-second-flat | `blue red`. Key b is correct. |
| Predict swap-the-key-then-repaint | `blue pink`. Key c is correct. |
| Predict backup-is-the-same-flat | `dark`. Key a is correct. |

Predict keys: b, c, a. Varied. Every wrong option is plausible and every `why` leads to its option. All three challenge blocks still say `verified: not-run` although each now compiles and prints the stated output (see FIX 1).
`./gradlew :site:validateContent`: `validateContent: ok`.

## Facts checked against page bodies (fetched 21 Oct 2026)

- functions.html: "When you pass an object to a function, the compiler passes a copy of the reference to that object. The copied reference points to the same object, so the function can modify the object's mutable state." and "Function parameters are read-only inside the function body (implicitly declared as `val`), so you can't reassign them". Matches Name it and interview answer 2.
- equality.html: "two references pointing to the same object" is in the intro list at the top; "Referential equality verifies the memory addresses of two objects" is in the Referential equality section. Both quotes are real, so the SOURCES comment is right.
- data-classes.html: "The `copy()` function creates a shallow copy of the instance... references to other objects are shared." Matches Common mistakes 4 and interview answer 3.
- shared-mutable-state-and-concurrency.html: "Coroutines can be executed parallelly using a multi-threaded dispatcher", "the main problem being synchronization of access to shared mutable state"; solutions are thread-safe data structures, thread confinement, mutex. Matches interview answer 4. The lesson correctly names no actors.
- Compose state page: the Caution on `ArrayList<T>`/`mutableListOf()`, "not observable by Compose and don't trigger a recomposition", recommends `State<List<T>>` with immutable `listOf()`. Matches.
- classes.html: link opens (HTTP 200). No claim in the body depends on it.
- Could not verify: none. Items labelled "my own reasoning" and "general knowledge" are labelled correctly.

## Checklist and structure

Passes: frontmatter keys and order match `docs/00` 8.2 and the roadmap row (title, `difficulty: hard`, `requires`, `fruit`); `status: shadow`; `id` equals path; `order: 41` sits between constructors (37) and two-kinds-of-equality (42); one SIM marker whose id is in `sims` and in `docs/sims/needed-ids.md` (line 40); section order and names match section 4 (Try it and Fix the bug correctly absent for `playground: false`); Question block is 5 sentences with no code or bold; Mental model has "Where the analogy stops"; Recap is three one-sentence lines under 20 words; interview section has 4 questions with why and trade-off, correct labels; You can now lines each map to a Predict item; no banned words (T4), no emoji or exclamation marks, no "next lesson" language, no VERIFY marks; supplied interview data (docs/practice/interview-kotlin.md line 645) used: all four levels, the three listen-for bullets, the wrong belief (as Common mistake 1), and the build exercise (Tricks, with a plain class because data classes come later).

## FIX list

1. Three `challenge` blocks (ids `spare-key-and-second-flat`, `swap-the-key-then-repaint`, `backup-is-the-same-flat`). Change `verified: not-run` to `verified: kotlin-2.4.21`; this review compiled and ran each and the output matches. Mechanical (`fixer`). Reviewer did not edit the file.
2. Line 18, SOURCES comment. It ends "Code was hand-traced twice, not compiled (no shell)", which is now false. Delete that sentence. Mechanical (`fixer`).
3. Jargon used before it is defined or bolded (checklist 3, `docs/04` section 7: define each term the first time it appears on the page). Mechanical (`lesson-writer`/`fixer`):
   - Mental model and Name it: **object**, **property**, **variable**, **constructor**, `val` / `var` appear without a plain definition. Give each a bold plain meaning at first use, or reword to the analogy.
   - Common mistakes 4 and interview answer 3: "shallow copy" is quoted but never explained. Add one plain sentence (copies the outer object, shares what it points to).
   - Android section: "mutable", "immutable", "observable data holder", "state", "redraw". Interview answers: "instance", "shared mutable state", "thread-safe", "mutex", "state". Define at first use or reword.
   - Interview answer 4: "coroutine" is defined but not bold (T2). Bold it once.
4. Common mistakes item 4 (line 267) has only the mistake and a fact. It lacks the "why it feels right" part and the "what to do instead" part (`docs/04` 4.9). Add both in one sentence each, for example: a hand-made copy feels complete; copy the inner objects too, or build them fresh. Mechanical (`fixer`).
5. Guess section: both questions have correct letter B (first of three, then second of two). Checklist 6 asks to vary the correct letter. Swap the order of the options in one of them (and the matching sentence below). Mechanical (`fixer`). Minor.
6. Frontmatter `sims: [two-keys-one-flat]` vs roadmap row (`content/kotlin/_roadmap.md` line 510): the row describes "tags on strings tied to objects floating in a memory box"; the leaf and `docs/sims/needed-ids.md` use keys and flats. Checklist 8 wants a match. Needs the owner (`curriculum-designer`): update the row's sim text to keys and flats, or change the leaf. This is the same issue as W9-a FIX 4.
7. Sim not built: no spec for `two-keys-one-flat` exists in `docs/sims/` (only the needed-ids entry), and `docs/04` section 4.3 says a live leaf has a working sim or no See it move. The leaf must stay `shadow` until `physics-designer` and `sim-engineer` deliver it. Orchestrator scheduling, not a text fix.

VERDICT: FIX

VERDICT (re-check, final): FIX (items 1 to 7 above; 1 to 5 are mechanical, 6 and 7 need the owner/orchestrator)

## Fixes applied

- FIX 1: three challenge blocks set to `verified: kotlin-2.4.21`.
- FIX 2: deleted the "hand-traced twice, not compiled" sentence from the SOURCES comment.
- FIX 3: bold plain definitions added for object, property, variable, constructor, val/var, reference, shallow copy, Compose state, mutable, immutable, observable data holder, redraw, coroutine, shared mutable state, thread-safe, mutex. "instance" reworded to "object".
- FIX 4: Common mistakes 4 now has "why it feels right" and "what to do instead".
- FIX 5: Guess question 1 options reordered (correct answer is now A); matching sentence updated.
- FIX 6: not applied (roadmap). Logged in docs/NEEDS-ME.md item 3.
- FIX 7: not applied (sim not built; scheduling).
- Check: `./gradlew :site:validateContent` ok.

VERDICT (re-check, final): PASS for objects-are-references. validateContent ok; sim specs deferred.
