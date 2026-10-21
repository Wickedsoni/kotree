# W9-a review: two-kinds-of-equality (and data-classes follow-on)

Reviewer: independent. Date: 21 Oct 2026. Files reviewed (not edited):
`content/kotlin/classes-and-objects/two-kinds-of-equality.md`, `content/kotlin/special-classes/data-classes.md`.
Checked against `docs/lesson-checklist.md` items 1 to 13, `docs/04`, `docs/09` section 8.

## Compile and run (scripts/kt-run.sh, Kotlin 2.4.21)

All five Kotlin blocks in the leaf compile with no warnings and print exactly the stated output.

| Block | Result |
|---|---|
| Card (a==b, a===b, a==c, a===c) | false, false, true, true. Matches prose. |
| String hello/world | true, false. Matches prose. |
| Predict ada-written-twice | true, true, false. Key a is correct. |
| Predict change-shows-through-the-tag | 5, false, true. Key c is correct. |
| Predict two-seats-same-numbers | false, true, true. Key b is correct. |

Correct letters across the leaf: Guess B, then a, c, b. Varied. Good.
`gradlew :site:validateContent` ran clean.

data-classes (re-run): all 5 blocks match their stated output; the `// does not compile` block fails with "primary constructor of data class must only have property ('val' / 'var') parameters". Correct.

## Facts checked against page bodies (fetched 21 Oct 2026)

- equality.html: "verifies the memory addresses of two objects"; `a?.equals(b) ?: (b === null)`; `equals()` inherited from `Any`, default is referential; value and data classes override automatically; non-data classes do not override; `a == null` translated to `a === null`. All match.
- hash-code.html: equal by `equals()` implies same hash code. Matches.
- Compose stability page: stable params use structural equality (`Object.equals()`) and are skipped when equal; unstable params use `===` with strong skipping (default Kotlin 2.0.20+); `Contact` has `var` properties; "a newly allocated Contact instance with identical values will still force ContactDetails to recompose". All match the lesson's wording.
- data-classes.html: `equals()`/`hashCode()` pair, toString form, primary-constructor-only rule, body property example (`age`), `val`/`var` requirement, "can't be abstract, open, sealed, or inner". All match data-classes.md.
- Not verified: Room and UI-state page claims in data-classes.md (out of scope for this task).

## Checklist result for two-kinds-of-equality

Passes: recap one sentence each; Question block within 5 sentences; no "next lesson" language; interview section has 4 questions with why and trade-off and labels "my own reasoning"/"general knowledge" correctly; frontmatter fields, `requires`, `difficulty`, `fruit` match the roadmap row; one SIM marker; sim id is in `docs/sims/needed-ids.md`; SOURCES comment near line 18; section order correct.

## data-classes as a follow-on

Reads correctly. `requires` points to this leaf; "plain class uses default `equals`, referential" in data-classes agrees with this leaf; `===` handling is consistent; vocabulary (equals, structural) is not re-introduced inconsistently. One formatting defect (FIX 5).

## Observations (no action needed)

- Checklist 13: the supplied build-exercise says compare two data class instances; the leaf uses plain classes (line 220) because data classes are taught later. This is correct sequencing. The supplied wrong belief "`==` is referential like Java" is not addressed directly; the audience is absolute freshers, so acceptable.
- `requires: objects-are-references` is a valid roadmap id, but `content/kotlin/classes-and-objects/objects-are-references.md` does not exist yet. Publish it first or with this leaf (same issue W8-b raised for data-classes).

VERDICT: FIX

## FIX list

1. two-kinds-of-equality.md, line 19. A stray `⚠️ VERIFY: compile not run` comment remains (checklist 9; `docs/04` line 310). All blocks now compile and match. Delete line 19. Mechanical (`fixer`).
2. two-kinds-of-equality.md, three `challenge` blocks (ids `ada-written-twice`, `change-shows-through-the-tag`, `two-seats-same-numbers`). Change `verified: not-run` to `verified: kotlin-2.4.21`, since this review ran and confirmed each. Mechanical (`fixer`). Also add `verified` evidence for the two plain `kotlin` blocks if the pipeline needs it (none required today).
3. two-kinds-of-equality.md, "Where this shows up in Android" (line 232), and interview answers (lines 240, 246). Jargon used without definition (checklist 3): "stable/unstable parameters", "strong skipping mode", "instance", "hash code", "HashSet", "HashMap", `Any`. Each needs a plain bold definition at first use, or a rewording to inputs and "same object". Mechanical for `fixer`/`lesson-writer`; no owner decision needed.
4. two-kinds-of-equality.md frontmatter `sims: [twins-or-one-person]` vs roadmap row (`content/kotlin/_roadmap.md` line 511): row says "reuse `objects-are-references` sim, with a same-object light and a same-content light". Checklist 8 requires a match. Needs the owner (`curriculum-designer`): either update the roadmap row to name `twins-or-one-person` as a new sim, or change the leaf. Also, `docs/sims/twins-or-one-person.md` spec does not exist yet (only the needed-ids entry), so the leaf cannot go `live` with a working sim until `physics-designer` and `sim-engineer` deliver it.
5. data-classes.md, line 18. The SOURCES comment and the heading are on one line: `...stack/state-production -->## The question`. A Markdown renderer treats the rest of that line as part of the HTML comment, so the heading is swallowed, and the housekeeper strip at go-live would delete it too. Insert a newline (and blank line) after `-->`. Mechanical (`fixer`). Only this file has the pattern.
6. Dependency: `objects-are-references.md` is missing (see Observations). Neither leaf should go `live` before it. Owner/orchestrator scheduling, not a text fix.

## Fixes applied

- FIX 1: deleted the stray VERIFY comment in two-kinds-of-equality.md.
- FIX 2: three challenge blocks set to `verified: kotlin-2.4.21`.
- FIX 3: plain definitions added for stable/unstable parameters, strong skipping mode, object, hash code, HashSet, HashMap.
- FIX 4: skipped (kept `twins-or-one-person`, per owner instruction).
- FIX 5: newline added after the SOURCES comment in data-classes.md.
- FIX 6: not applied (owned by another agent).
- Check: `./gradlew :site:validateContent` ok.

## Re-check

Reviewer: independent, 21 Oct 2026, commit 51b42a7 (and HEAD 23d99a7). Reviewed files not edited.

- FIX 1: confirmed. The VERIFY comment is gone; no `VERIFY` marker remains in two-kinds-of-equality.md. (Nit, optional: line 19 `## The question` now follows the SOURCES comment with no blank line; it renders correctly.)
- FIX 2: confirmed. The three predict blocks say `verified: kotlin-2.4.21`. All five Kotlin blocks were recompiled with `scripts/kt-run.sh` (Kotlin 2.4.21): outputs are `false false true true`; `true false`; `true true false`; `5 false true`; `false true true`. They match the stated outputs and keys. No snippet text changed in the fix commit.
- FIX 3: mostly confirmed. "stable parameters", "unstable parameters", "strong skipping mode", "hash code", "HashSet", "HashMap" now have plain bold definitions; "instance" became "object". Not done: `Any` and "inherits" at line 81 ("every class inherits `equals` from `Any`") were named in the original FIX 3 and are still undefined. Mechanical (`fixer`): add a plain phrase, for example "`Any` is the top class that every Kotlin class comes from (general knowledge)".
- FIX 4: skipped by owner decision; accepted. Go-live note: `docs/sims/twins-or-one-person.md` does not exist yet, so the leaf cannot go `live` with a working sim.
- FIX 5: confirmed. data-classes.md now has the SOURCES comment, a blank line, then `## The question`. No snippet text changed.
- FIX 6: `objects-are-references.md` now exists (shadow). It has its own FIX list in `docs/reviews/W9-b.md`, so neither leaf can go `live` yet.
- `./gradlew :site:validateContent`: `validateContent: ok`.

FIX list (re-check):

1. two-kinds-of-equality.md, line 81. `Any` and "inherits" are used without a definition (original FIX 3). Mechanical (`fixer`).

VERDICT (re-check, final): FIX (one mechanical item above)

## Fixes applied

- Re-check FIX 1: `Any` and "inherits" defined inline in two-kinds-of-equality.md (line 81).
- Roadmap sim mismatch for `two-kinds-of-equality` (`twins-or-one-person`) logged in docs/NEEDS-ME.md item 3 for curriculum-designer.
- Check: `./gradlew :site:validateContent` ok.

VERDICT (re-check, final): PASS for all 2 leaves (two-kinds-of-equality, data-classes). validateContent ok.
