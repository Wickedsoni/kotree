# Review: sim Try this steps, chart and compare leaves (commit 2d80907)

Scope: 14 leaves, `chart` (9) and `compare` (6, one of them phone-kitchen-two-ways). Method: every `done` list was traced by hand against `SimCore.fold`, `TryProgressCalc`, `ChartMath` and the slider/toggle event rules in `TemplateSimHost.fire`. Prose outside each fence was read against what the sim really shows. Lesson-checklist §7 (no "next lesson" promises, no untrue "on the screen" claims) was grepped on all 14 files: no hit that talks about the sim. The "screen" mentions are real-app contexts.

Reachability facts used: refused events stay in history; slider and toggle events are hoisted (last value applies from the first action); consecutive events of the same slider collapse to the last one; `{x.exit}` and `{curve.at}` resolve through `ChartMath.published`. I did not run the site build, so the YAML schema rules (SM1 to SM11) were checked by reading only.

VERDICT: FIX (5 leaves need prose edits, 9 leaves PASS)

## Per leaf

| Leaf (sim id) | Try steps reachable | Prose vs sim | Verdict |
|---|---|---|---|
| foundation/programming-thinking/counting-loops (numbered-stairs-loop) | yes: `climb`; `include=off, climb`; `down=on, climb` | Prose never promises first/last sliders; the staircase is fixed at 5 steps. The needed-ids row still says sliders (registry only, see note A). | PASS |
| kotlin/control-flow/ranges-and-for-loops (tape-measure-range) | yes; step 3 `down, step2=on` gives "mark 1 missed", as the step says | Fixed marks 1 to 6 match the sim intro. Prose uses other numbers (0 to 10, step 3) only as the abstract rule. | PASS |
| daa/analysis/big-o-omega-theta (ceiling-and-floor-band) | yes: `n0=2,check` says none and 31; `n0=1,check` says 1 and 31 (ceiling fails at 1: 5 > 4); `n=40` | "What you should notice" claims a shape n² ceiling and "a ceiling below the curve". The sim has neither. The intro also hides the n² floor. | FIX 1, 2 |
| dsa/complexity/big-o-notation (drop-the-small-parts) | yes; n3, n10, n1000 readouts match the lesson table (16/9, 37/100, 3007/1000000); B lower to n = 4, higher from n = 5 | OK. `drop` only adds a sentence, because the line n is always drawn. Acceptable. | PASS |
| dsa/complexity/counting-steps (step-counter-bars) | yes; houses=100, 1000, then 500 then 1000 | Notice says "bar", the sim draws lines. | FIX 3 |
| kotlin/types-and-variables/number-conversions (overflow-cups) | yes; the refused `pour-plain` stays in history, so step 1 lights | Step 3 says "read how much room is left". No readout shows room left (only the say text names the jug's top mark). | FIX 4 |
| profiling/measure-first/measure-before-you-fix (measure-bars) | yes; `guess=2`, `measure`, `fix, measure-again` (stopwatch 340 to 310) | OK. Numbers (340, 310) differ from the lesson's 300 ms story, but both are marked invented. | PASS |
| profiling/profiler-tour/read-a-profiler-screen (hospital-monitor) | yes; optional step 4 does not block | Sim has no rows, no drag and no table. Notice sentence and one say text claim them. | FIX 5, 6 |
| foundation/from-code-to-running/what-is-art (phone-kitchen-two-ways) | yes; `quiet-hour` before `cook-ahead` satisfies the `at` condition | Notice says "the toggle". The sim has buttons, no toggle. | FIX 7 |
| foundation/from-code-to-running/compiler-vs-interpreter (translation-lanes) | yes; the three actions have no condition | Mental model says "typo on page 3", the sim and Predict say "sentence 3". | FIX 8 |
| kotlin/classes-and-objects/read-only-is-not-immutable (shop-window-restock) | yes; the optional step needs `photo` first (mark good), which step 2 did | OK. "The customer sees every change" holds until the optional reveal, which is the point of it. | PASS |
| kotlin/collections/list-vs-mutable-list (menu-board-cover) | yes | OK | PASS |
| kotlin/special-classes/data-class-copy (form-photocopier) | yes; `note` after `copy, age31` runs with folder 7, then `own-folder, note` hits folder 8 | OK. The optional "Show folder numbers" tick-box in needed-ids is not in the sim or the prose (note A). | PASS |
| ui/first-composable/what-is-declarative-ui (declarative-vs-steps) | yes; the hoisted `skip=on` replays all four presses with skip on, so the left badge stays "0 left" while the title says 4. This is the ADR rule and the step's claim holds. | OK. No slider or phone in the sim; the lesson does not mention one. | PASS |

## FIX list (all mechanical, `fixer` can do them; paths under `content/`)

1. `daa/analysis/big-o-omega-theta.md`, "What you should notice" line (about line 67).
   Old: `**What you should notice:** With the shape n, a floor and a ceiling can both hold. With the shape n², the ceiling holds and the floor fails, and a ceiling below the curve fails once n is large.`
   New: `**What you should notice:** With the shape n, the floor 3n and the ceiling 4n both hold, and the ceiling 4n holds only from n0 = 2, because it fails at n = 1. With the shape n², the floor 0.1 times n squared fails once n reaches 31.`
2. Same file, sim `intro:` (line 39).
   Old: `The code takes 3n + 2 steps. A ceiling line 4n and a floor line 3n of the same shape n are drawn, and so are a ceiling band and a floor band you can test from a starting point n0.`
   New: `The code takes 3n + 2 steps. The plot shows the code, a floor line 3n, a ceiling line 4n and a floor line 0.1 times n squared. You can test the ceiling and the n squared floor from a starting point n0.`
3. `dsa/complexity/counting-steps.md`, line 65.
   Old: `The bar for a single walk grows in step with the houses, and the bar for a walk inside a walk grows much faster.`
   New: `The line for a single walk grows in step with the houses, and the line for a walk inside a walk grows much faster.`
4. `kotlin/types-and-variables/number-conversions.md`, Try step 3 (line 101).
   Old: `Then add one drop to the jug and read how much room is left.`
   New: `Then add one drop to the jug and read what the jug holds now and where its top mark is.`
5. `profiling/profiler-tour/read-a-profiler-screen.md`, line 89.
   Old: `Every row lines up on one clock, and each row or chart answers a different question.`
   New: `The busy window and the quiet window split the same 400 ms very differently, and each question is answered by a different row or chart.`
6. Same file, `busy` action `say` (line 58).
   Old: `where the CPU usage row spikes. The table shows decodeImage using 240 of the 400 ms.`
   New: `where a CPU usage row would spike. The bars show decodeImage using 240 of the 400 ms.`
7. `foundation/from-code-to-running/what-is-art.md`, line 92.
   Old: `and the toggle only changes when the quick-prep sheets are made.`
   New: `and the choice of side only changes when the quick-prep sheets are made.`
8. `foundation/from-code-to-running/compiler-vs-interpreter.md`, line 30.
   Old: `- A typo on page 3 is a sentence that breaks the rules of the language.`
   New: `- A typo in sentence 3 is a sentence that breaks the rules of the language.`
   (Smoother alternative for the owner: `- A typo in sentence 3 is a sentence that breaks the rules of the language.` is a little circular; `- The typo is a mistake in sentence 3 that breaks the rules of the language.` also works.)

Optional, not required for PASS: `counting-loops` Try step 3 says "The numbers come in the other order". With "Include the last step" still off, the sim says `5 4 3 2` (step 1 left out). Suggested new text: `Switch "Climb down" on and press "Climb" again. The numbers come in the other order, and the last step in this direction, step 1, is still left out.`

## Notes

A. Registry drift (owner decision, `docs/sims/needed-ids.md`). Rows for numbered-stairs-loop, tape-measure-range, ceiling-and-floor-band, drop-the-small-parts, step-counter-bars, hospital-monitor, declarative-vs-steps, phone-kitchen-two-ways, form-photocopier, overflow-cups, shop-window-restock and measure-bars describe a richer sim than the template one (first/last sliders, shape picker, drag window, flame chart, optional tick-boxes, a toggle, "learner picks cup sizes", a footstep counter beside bars). The lessons do not promise these, so they are not lesson FIXes. Either the owner keeps the rows as the brief for later bespoke sims (the ADR 009 star candidates), or `housekeeper` trims each row to what the template sim does. Decide once.

B. Hoisting quirk (no FIX): in declarative-vs-steps and similar sims, switching a toggle on replays earlier presses with the new value. A learner who does Try step 2 sees the left badge stuck at "0 left" for the whole run. This follows ADR 009 section 2.

C. Unverified: the YAML-to-JSON schema rules (SM1 to SM11) and the live `/preview/<id>` mouse, keyboard and reduced-motion passes. Not run in this review (hand trace only).

## Fixes applied

- FIX 1 to 8 applied as written (big-o-omega-theta x2, counting-steps, number-conversions, read-a-profiler-screen x2, what-is-art, compiler-vs-interpreter). validateContent: ok.
