# Review W5-c: foundation/first-code/run-your-first-program (re-check)

Date 20 Oct 2026. Reviewer: independent (did not write the leaf). Gate: L31, L33. Leaf not edited.

VERDICT: FIX (all items mechanical except item 8; after items 1-7 the leaf is PASS by this review)

## Compiler used

Real Kotlin 2.4.21: `kotlinc-jvm 2.4.21 (JRE 21.0.12.1)`, the official `kotlin-compiler-2.4.21.zip` from the JetBrains GitHub release, unpacked in the scratchpad. This is the pinned version in `docs/06` §5, so `kotlin-2.4.21` is an honest `verified` value.

## Compile and run results

| Block | Result on 2.4.21 |
|---|---|
| `kotlin` fence (line 80) and `print-hi` code | compiles, 0 warnings, stdout `Hi` (quote marks not printed) |
| `text-is-not-sum` | compiles, 0 warnings, stdout `2 + 3` |
| `find-the-missing-quote` (program lines only, the instruction sentence is not Kotlin) | does not compile: `3:37 syntax error: Expecting '"'`, then follow-on errors on line 4. The first error is on the Step 1 line, so key c is right |
| extra check for Q3: `print("A") print("B") println("C") println("D")` | `ABC` newline `D`, matches the interview answer |

Keys: print-hi b, text-is-not-sum a, find-the-missing-quote c. One correct each, letters vary (b a c). `output` fields match. Third `output` is a sentence, not stdout (compile error has no stdout); accepted as in W4-a and W5-a for `run: false` items.

`./gradlew :site:validateContent` : `validateContent: ok`, BUILD SUCCESSFUL.

## Structure and voice (all OK)

Frontmatter equals the roadmap row (id, title, difficulty easy, requires, fruit false, order 8). Ten headings in order; no Try it, Fix the bug or See it move (correct for `playground: false`, `sims: []`). Question block: 5 sentences, has `?`, no code, no bold. One analogy plus "Where the analogy stops". Notice/Guess/Build hold no Kotlin syntax. Recap: 3 one-sentence lines, each under 20 words. Interview section: 4 questions, 3 sentences each, trade-off labelled "my own reasoning" in Q1 and Q4. Prose about 1,300 words; no banned T4 word; no `!`; no emoji. No internal-process wording in learner text ("last lesson" is the required prior leaf, fine). Bold once per term.

Fact check (kotlinlang.org, read today): the Playground is "an online application for writing, running, and sharing Kotlin code in your browser"; the page links `https://play.kotlinlang.org/`; "In the Playground's editor area, you can write code just as you would in a source file"; "there's no need to create a new project and build it" (intro, for quick checks); scratch files are an IDE feature. The page says nothing about a Run button, an output area, a network need, login or install. println/print match the API pages. Logcat and "Kotlin is fully supported" match W1-b.

## FIX list

1. File `content/foundation/first-code/run-your-first-program.md`, lines 99, 122, 145 (the three `verified:` fields). Old: `verified: not-run`. New: `verified: kotlin-2.4.21` on all three (print-hi, text-is-not-sum, find-the-missing-quote). Keep `run: false`. The validator (CH8) accepts it and CH10 does not tie `verified` to `run`. Mechanical.
2. Line 26 (SOURCES comment, last sentence). Old: `Run status: the Name it block and the three Predict items were hand-traced only, not compiled (no shell in the authoring session). Predict items are run: false, verified: not-run. The Playground is not built on this site yet, so the page has no editor.` New: `Run status: the Name it block and the three Predict items were compiled and run on Kotlin 2.4.21 (W5-c); the missing-quote program fails to compile as stated. Predict items are run: false, verified: kotlin-2.4.21. The Playground is not built on this site yet, so the page has no editor.` Mechanical.
3. Line 44. Old: `Run sends your whole note away to be carried out. In my own reasoning, an online page like that needs an internet connection.` New: `In my own reasoning, Run sends your whole note away to be carried out, and an online page like that needs an internet connection.` (The docs page does not describe what Run does.) Mechanical.
4. Line 75. Old: `It has three parts. The **editor** is the box where you type. The Run button sends your code. The **output** is the area below that shows what the program printed.` New: `The Kotlin documentation names its **editor** area, where you write code. In my own reasoning, the rest works like the doorbell and the reply: a Run button sends the code, and an **output** area shows what the program printed.` (Run button and output area are not on the cited page.) Mechanical.
5. Lines 184 and 186. Old (184): `The Playground runs plain Kotlin, not Android apps.` New: `In my own reasoning, the Playground runs plain Kotlin, not Android apps.` Old (186): `So a button that does nothing on a phone is, deep down, a few missing or wrong lines like these.` New: `So, in my own reasoning, a button that does nothing on a phone often comes from a few missing or wrong lines like these.` Mechanical.
6. Lines 199 and 201 (interview, undefined jargon, checklist item 3). Old (199): `Both print a message to standard output. The documentation says \`println\` also prints the line separator, so the next message starts on a new line.` New: `Both print a message to the output. The documentation says \`println\` also ends the line, so the next message starts on a new line.` Old (201): `A phone app has no console under its buttons.` New: `A phone app has no output area under its buttons.` Mechanical.
7. Housekeeper step, `docs/NEEDS-ME.md` item 1: replace "the authoring session had no shell ... not compiled" with "compiled and run on Kotlin 2.4.21 and validateContent ok in W5-c; leaf PASS after FIX items 1-6". Mechanical.
8. [owner: `curriculum-designer`, hard rule 5] `content/foundation/_roadmap.md`, row `run-your-first-program` (line 229) is stale against the leaf. Sim cell says "the live Kotlin Playground on the page is the demo"; the page has no editor. Challenges cell lists an `F:` task; the leaf has no Fix the bug (the third Predict replaces it). Notes cell still holds `⚠️ VERIFY` about login. Proposed: Sim `none:` tooling steps; the page has no editor yet, so it points to play.kotlinlang.org. Challenges `P:` what does `println("Hi")` print · `P:` find the line with the missing quote mark. Notes: keep the login VERIFY for Phase 6 or move it to docs/11. V5 checks do not read these cells, so the build is unaffected, but "roadmap row says why" for omitting See it move is only true after this edit.

Optional, not a FIX: the third Predict puts the instruction sentence inside `code`, so the page shows it in a code block. Also the roadmap promised a runnable demo; adding a Phase 6 Try it later would need `playground: true`.

## Could not verify

The Playground's Run button, output area and need for internet (undocumented on kotlinlang.org; labelled as own reasoning by items 3 and 4). Login and "free" claims are not made by the leaf. Not retested: play.kotlinlang.org is unreachable from this box.

## Final (orchestrator, 21 Oct 2026)
All FIX items for the leaves in this report were applied as specified (re-check fixes included) and validateContent passes. Verdict for the leaves in this report: PASS.
