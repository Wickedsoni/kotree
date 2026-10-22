# Review DSA-B: four dsa shadow leaves (strings x2, linked lists x2)

Reviewer: independent. Date: 22 Oct 2026. Checklist: `docs/lesson-checklist.md` (items 1 to 14).

Leaves: `content/dsa/strings/text-building-cost.md`, `content/dsa/strings/compare-text-by-characters.md`, `content/dsa/linked-lists/what-is-a-linked-list.md`, `content/dsa/linked-lists/insert-and-remove-nodes.md`.

VERDICT: FIX (small items only; no wrong fact, no wrong key, no failing snippet, no broken sim step).

## Build

`./gradlew :site:validateContent` printed `validateContent: ok`. All four sim blocks pass SM0 to SM11. Frontmatter (difficulty, order, requires, sims, fruit) matches the `content/dsa/_roadmap.md` rows. The four sim ids are in `docs/sims/needed-ids.md` with a template. `requires` pointing at shadow arrays leaves is accepted, as briefed.

## Snippets (compiled and run with `bash scripts/kt-run.sh`, kotlinc 2.4.21)

Every Kotlin block, every challenge `code` and `fix`, and every Try-it break and fix variant was compiled and run. No compiler warnings. Real stdout matches every `output`, `expected` and prose claim:

- text-building-cost: name-it block `true`, `true`, `red green blue pink gray`; predicts `15` and `55 10`; Try it `5050 100` and `100 100` (hand-checked); fix-csv-export start prints `553` / `25753`, fix prints `553` / `553`.
- compare-text-by-characters: name-it block `true false false`; predicts `1 -1 0` and `0 1 -1`; the Fix start code really throws `ArrayIndexOutOfBoundsException` (Index -21, capital L, first loop), the fix prints `true true`.
- what-is-a-linked-list: name-it block `clue 1: Pond` ... `clue 3: Barn`; predicts `40` and `3`; Try it `2`; the broken chain prints `10 20 30 10 20 30` (six values, as the hint says), the fix prints `10 20 30`. The smart cast on `current` in `while (current != null && steps < 6)` compiles.
- insert-and-remove-nodes: name-it block prints the three stated lines; predicts `10 30` and `1 2`; Try it break gives `Ada -> Zed`; Fix start prints `Ada`, fix prints `Ada -> Cy -> Dee`.

Guess and Predict keys are true; correct letters vary; wrong options are plausible.

## Facts checked against official pages (fetched 22 Oct 2026)

- kotlinlang.org/docs/strings.html: immutable; "any operation that appears to modify the string actually creates a new string"; `+` creates a new String, "may not be beneficial in loops"; `buildString()` or `StringBuilder` "collect all pieces in a single mutable buffer and produce only one string at the end"; templates "usually easier to read". All match the text.
- operator-overloading: `a += b` falls back to `a = a + b` when `plusAssign` is not available. Matches.
- arrays.html: `IntArray(5)` zeros; `[]` read and write; out-of-range index throws `ArrayIndexOutOfBoundsException`. The page does not show `++` on an element (the leaf's VERIFY note says so; it compiles and runs).
- Char.minus(Char) returns Int (API page). characters.html: Char is a UTF-16 code unit; the broccoli emoji has length 2; code points as Int. Match.
- developer.android.com render page: "under 16ms ... 60 fps"; slow frames cause stuttering "called jank". Match.
- Java SE 21 LinkedList: doubly-linked; "Operations that index into the list will traverse the list from the beginning or the end, whichever is closer". Match.
- LeetCode numbers, titles and difficulty (1108, 151, 394, 68, 242, 383, 387, 49, 438, 876, 141, 142, 138, 203, 237, 19, 25) agree with the `[both]` rows in `docs/practice/practice-dsa.md` and with my own knowledge. I could not open leetcode.com to re-check the difficulty labels live; this is the one claim not verified on the source.

## Sims (ADR 009 and checklist item 14)

Templates fit: chart (growth), lanes (bins), zones (places), stepper (order of steps). No colours, sizes or coordinates. No `phone` or `timeline`. All Try steps use buttons or sliders only. I hand-traced `site/.../sims/core/SimCore.kt` (a refused event stays in history, settings are hoisted, `.at` reads var `x`):

- rewrite-the-letter: `words=100`, `words=1000`, `words=500` and both count buttons are reachable; `{fresh.at}` is 5050 at 100; poly `[0,0.5,0.5]` = n(n+1)/2. Prose matches.
- tile-bins: step 1 `put-listen, lift-silver` leaves t and n marked bad; step 2 `put-listen` resets labels and marks, then `lift-silent` ends all good. The refusal before dropping is true.
- treasure-hunt-clues: step 1 `jump-to-4` is refused (found 0); three `follow` presses give found 3; step 3 `jump-to-4` then `follow` gives found 4 then 5. Reachable.
- conga-line-hands: step 1 ends at ada=Cy and the walk says "Ada, Cy, Dee"; step 2 ends at ada=nobody and the walk says only Ada is left. Matches "What you should notice".

NOT done: I did not open `/preview/<id>` in a browser, so the mouse, keyboard-only and reduced-motion passes of item 14 are not checked by me (no dev server was run). The `chart` slider needs about 99 arrow presses from 10 to 1000 by keyboard (End key reaches it); acceptable.

## FIX list

1. `what-is-a-linked-list.md`, sim `treasure-hunt-clues` (needs sim-engineer or owner). Every clue note ("Clue 3: clue 4 is at the Pond") and its place are drawn from the start, so the learner can see where clue 4 is while the refusal says "No note you hold says where clue 4 is". The picture contradicts the lesson idea. Start the items as hidden (for example "Clue 2: not found yet") and reveal the text with a `label:` effect when a clue is found. Keep the Try steps and ids.
2. `text-building-cost.md`, Fix the bug `explanation` (mechanical): "about 25,000 characters" but the real run writes 25,753. Say "about 26,000".
3. All four leaves (mechanical, housekeeper or fixer): `verified: "UNVERIFIED: compile not run"` on every challenge is now out of date. Set `verified: kotlin-2.4.21` (all outputs confirmed on 22 Oct 2026) and drop the two "compile not run" VERIFY lines in the SOURCES comment. The VERIFY about the capital-L exception (compare) and the smart cast (linked) are confirmed too.
4. `compare-text-by-characters.md`, "Where this shows up in Android" (mechanical): "the Kotlin documentation says ... passing a locale such as Turkish can change the result". The page text only says `lowercase()` uses "the invariant locale"; Turkish appears in its code samples, not its description. Reword to "the page's example shows" and label "behaves the same on every phone" as my own reasoning. Also define "locale" in a few words or drop it (checklist item 3).
5. `compare-text-by-characters.md`, interview answer 3 (mechanical): "outside the Basic Multilingual Plane" and "surrogate pair" are used without plain words. Say "one of the emoji that does not fit in a single Char". Interview answer 2 uses `HashMap<Char, Int>` without saying what a map is; add "a map stores a count for each key" (item 3).
6. `text-building-cost.md`, "Name it" (mechanical): `plusAssign` is named without a definition and "template" (`"$a $b"`) is used in Tricks and Interview without a one-line definition (item 3). Add "a function named plusAssign, which String does not have" and "a template puts values inside quotes".
7. `text-building-cost.md`, interview answer 2 (mechanical): "So each piece is written about once and the total is O(n)" follows a documentation sentence but is not from the page. Add "(general knowledge)" (item 4).
8. `insert-and-remove-nodes.md`, Predict `let-go-before-grab` option b `why` (mechanical): "The first dancer let go of the dancer holding 3" is wrong in the conga picture, where 3 is the dancer, not the holder. Use "let go of dancer 3 before the newcomer grabbed it".
9. Item 14 browser pass (owner or a reviewer with a dev server): open `/preview/rewrite-the-letter`, `/preview/tile-bins`, `/preview/treasure-hunt-clues`, `/preview/conga-line-hands` and do each Try step by mouse, keyboard only, and reduced motion. Re-do `treasure-hunt-clues` after FIX 1.

## Re-check (22 Oct 2026)

Checked: `:site:validateContent` re-run, prints `validateContent: ok`. No site Kotlin changed (`git status` shows only content and docs), so no Kotlin source needed rebuilding. All 36 Kotlin blocks and challenge `code`/`fix` snippets of the seven leaves (strings x2, linked lists x3, stacks x2) were recompiled and run with `bash scripts/kt-run.sh` (kotlinc 2.4.21): no compiler warnings, outputs equal the stated ones (compare Fix start still throws `ArrayIndexOutOfBoundsException`, as designed).

1. Treasure-hunt-clues rework: PASS. Items start as "Hidden note, not read yet"; each `follow` and `jump-to-4` case reveals text with a `label:` effect (the zones layout and the text view both read `state.labels`). Traced: step 1 `jump-to-4` is refused at found 0; three `follow` give found 3 with clues 1 to 3 revealed; step 3 `jump-to-4` then `follow` reach found 4 and 5. Script and ids unchanged. The places stay visible (that shows the scatter), the contents do not.
2. text-building-cost "about 26,000": fixed.
3. `verified: kotlin-2.4.21` on all challenges and the two "compile not run" VERIFY lines: fixed.
4. compare-text-by-characters Android: now "its example shows", "locale" defined, "My own reasoning" label: fixed.
5. Emoji and map wording: fixed.
6. `plusAssign` and template definitions: fixed.
7. "(general knowledge)" on the O(n) builder claim: fixed.
8. Conga `let-go-before-grab` option b `why`: fixed.
9. Browser pass (mouse, keyboard, reduced motion) for the four sims: still NOT done by me (no dev server run). It stays owed to the layer build, same as L3-a/b/c.

Non-blocking nits for the housekeeper (inside the SOURCES comment, stripped at go-live): compare l.26 to 27, linked `what-is-a-linked-list` l.25 still carry ⚠️ VERIFY lines for points now confirmed by a run.

VERDICT (re-check, final): PASS
