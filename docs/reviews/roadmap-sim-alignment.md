# Roadmap Sim cell alignment (curriculum-designer, 21 Oct 2026)

Scope: Sim cells (and the Notes cells that reviews named) of rows whose leaf `sims` deviates from the roadmap. No leaf id, title, difficulty, order, requires or fruit was touched. No leaf file was touched. Neither `foundation` nor `kotlin` `_roadmap.md` has a `sims` key in its YAML block, so only table cells changed. Validators V5/V-roadmap do not read these cells.

Wording rule: each new Sim cell uses the leaf's sim id plus the description in `docs/sims/needed-ids.md`, without the "phone refuses/rejects the box" claim (W4-b: the docs do not state it).

| Roadmap row | Cell | Change | Source |
|---|---|---|---|
| foundation/debugging-mindset/find-a-bug-without-guessing | Sim | pipes and check-valves to `fuse-halving-search` (16 rooms, one fuse, 4 tries) | W3-b ruling; needed-ids 15 |
| foundation/first-code/run-your-first-program | Sim, Challenges, Notes | no Playground demo, points to play.kotlinlang.org; `F:` became `P:` (find the missing quote); login VERIFY kept for Phase 6 | W5-c item 8 |
| foundation/from-code-to-running/compiler-vs-interpreter | Sim | "reuse" to `translation-lanes` extends `translator-box` | W4-a; needed-ids 21 |
| foundation/how-an-app-is-born/what-is-an-app-file | Sim, Notes | `flat-pack-app-box`, no refusal claim; Notes add W4-b confirmation, VERIFY kept | W4-b ruling 1; needed-ids 17 |
| foundation/how-an-app-is-born/what-happens-when-i-build | Sim, Notes | `bakery-build-line` (mix, bake, pack, stamp); Notes say the order is the writer's labelled summary | W4-b rulings 1-2; needed-ids 18 |
| foundation/how-an-app-is-born/how-an-app-gets-installed | Notes | W4-b confirmation appended, VERIFY kept | W4-b ruling 1 |
| foundation/from-code-to-running/what-is-art | Notes | W4-b: "Configure ART" unreachable, leaf rests on the baselineprofiles page; VERIFY kept | W4-b ruling 3 |
| kotlin/types-and-variables/val-vs-var | Sim | jars to `mug-and-whiteboard-tag` | needed-ids 28 |
| kotlin/types-and-variables/number-conversions | Sim | spill to wrap: `overflow-cups` | W5-b ruling 1; needed-ids 23 |
| kotlin/control-flow/when-expression | Sim | ball to letters: `sorting-chutes` | W5-b ruling 1; needed-ids 24 |
| kotlin/control-flow/ranges-and-for-loops | Sim | reuse staircase to `tape-measure-range` | W5-b ruling 1; needed-ids 25 |
| kotlin/control-flow/break-and-continue | Sim | reuse spoon-and-pot to `letter-stack-gates` | W5-b ruling 1; needed-ids 26 |
| kotlin/functions/scope-and-shadowing | Sim | rooms and balls to `house-and-street-names` | W6-a item 1; needed-ids 31 |
| kotlin/null-safety/safe-call-and-elvis | Sim | reuse lunch box to `knock-and-note` | W6-a item 1; needed-ids 30 |
| kotlin/collections/list-vs-mutable-list | Sim | shelves behind glass to `menu-board-cover` | W6-b ruling 1; needed-ids 32 |
| kotlin/collections/index-out-of-bounds | Sim | reuse post-office boxes to `inn-room-clerk` | W6-b ruling 1; needed-ids 33 |
| kotlin/lambdas/closures | Sim | jar and string to `garden-window-view` (leaf and Analogy cell are the garden window) | found in leaf; no review |

## Open questions (not edited)

1. Challenges cells still use old sim words: `find-a-bug-without-guessing` P ("16 segments", "leak"), `what-happens-when-i-build` P/B ("not signed", "compile station"), and `P:` "on the sim" items in the Kotlin rows above. A reviewer or `challenge-builder` should reword them to the leaf's Predict items.
2. Sim ids `factory-line-stations`, `recipe-card-handoff`, `garden-window-view`, `cookie-cutter-stamp` (kotlin lambdas and `what-is-a-class`) are not in `docs/sims/needed-ids.md`. Their Sim cells in `map-filter-and-friends` (balls vs apple tags), `functions-as-values` and `what-is-a-class` are close enough and were left as is.
3. `counting-loops`, `loop-over-a-list` and `what-happens-when-i-tap-an-icon` use new ids (`numbered-stairs-loop`, `basket-checkout-loop`, `hotel-bell-startup`) that extend or detail the roadmap text. Not recorded as divergences, left as is.
4. W6-a item 2 and W6-b ruling 2 (Challenges `F`/`F(ai)` tasks vs Predict-only leaves in `ai-review` stems) remain an owner decision.
5. Other branches' leaves with `sims` were not compared; no review flagged them.
