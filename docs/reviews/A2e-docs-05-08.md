# Review A2e: docs/05 and docs/08

Reviewed 18 Oct 2026 against `docs/00` (L15, L26 amendments), `docs/01`, `docs/04`, `docs/07`, `docs/10` (cross-checks) and the 17 `content/*/_roadmap.md` files. No reviewed file was edited.

VERDICT: FIX (no blockers; 2 medium, rest low)

## What passed

- No emulator or simulator promise anywhere in `docs/05` or `docs/08`. Every mention is a rejection (`docs/05` lines 10, 148, 160, 612; `docs/08` line 200). `docs/01` 348, `docs/04` 120/225/378 and `docs/06` agree.
- L26 (optional video) is not contradicted; `docs/05` line 160 says reference videos stay optional.
- Budgets match `docs/07` §8 (lines 301, 311-314) exactly: Matter.js 0 B on home and at most 30 KB gzip; Three.js at most 200 KB; shared sim runtime 12 KB; per sim 6 KB; images 30 KB; CLS 0.05; 16.7 ms p95; 4-core reference laptop. `docs/07` line 313 already carries the reworded sim-code row that `docs/05` §3.1 asked `architect` to write. The sim marker rewrite (`docs/05` C3) is done in `docs/07` line 231.
- Leaf-count arithmetic in `docs/08` is correct:
  - The Est. column sums to 450 and the v1 column to 820.
  - Every ±20 % band is computed correctly. Band lows sum to 361 and highs to 539, which match the stated 360-540 within rounding.
  - Every stem sketch sums to its branch Est.
  - Phase table: 32 + 54 + 82 + 112 + 115 + 55 = 450. Each phase cell also re-adds from the sketches.
- Timeline: 450 / 3 = 150 weeks = 2 y 10.6 m (stated "2 y 11 m"). 450 / 144 per year = 3 y 1.5 m ("3 y 2 m"). 450 / 2.5 = 180 weeks = 3 y 5.5 m ("3 y 6 m"). Foundation 32 / 3 = 10.7 weeks = 2.5 months. Phase 3 ends at 168 leaves = 56 weeks = 1 y 1 m. Phase 4 ends at 280 leaves = 93 weeks = 1 y 9.5 m (stated "1 y 10 m"). All fine.
- Rules R1-R7 are consistent with each other, §2.3, §9.1 and the §14 checklist. Real roadmaps follow the §2 section order, have a `Flags:` line on every stem, a `## Later` section, and at least one `ai-review` stem in all 17.
- Matter.js facts verified against npm, the brm.io API docs and unpkg:
  - Latest is 0.20.0, published 23 Jun 2024. The license is MIT.
  - `build/matter.min.js` exists and is 83.5 kB, which fits the quoted 81 KB.
  - Runner `delta` is 1000/60 and fixed; `maxFrameTime` is 1000/30; `maxUpdates` is null (auto).
  - Engine defaults (iterations 6/4/2, `enableSleeping` false, gravity y 1, scale 0.001, `timeScale` 1) are correct.
  - Body defaults (restitution 0, friction 0.1, frictionStatic 0.5, frictionAir 0.01, density 0.001, slop 0.05, sleepThreshold 60) are correct.
  - Restitution combines as max and friction as min. `inertia: Infinity` prevents rotation. `chamfer` is read by `Bodies` factories.
- Spec geometry in `null-lunch-box` is consistent: box widths and centres, sandwich resting y, mass about 7.7, 14 static parts, 3 sensors.

## FIX list

1. **Medium.** `docs/05` §3.1 Three.js row (line 88) and §4.4 (line 166). The plan is a single `three-<version>.module.min.js`. Since r171, `three.module.js` imports `three.core.js`, so the library is two files (forum thread, `cdnjs` 0.172 and 0.181 listings). npm latest is 0.186.1. Fix: name both files, say the entry file is loaded with `import()` or a `<script type="module">`, and re-measure the combined gzip size against the 200 KB budget. Needs `architect`. Not mechanical.
2. **Medium.** `docs/05` §7.3 (line 366) and §10.3 (lines 500-501). The rule is a minimum 44 × 44 px hit size for draggable bodies, but the sandwiches are 96 × 40, which is 4 px short of the rule even at 1:1 scale and smaller on a narrower frame. Fix: set `sandwichHeight` to at least 44 and recheck the 50-unit inner box height, or add an invisible hit-slop and state it. Needs `physics-designer`.
3. **Medium.** `docs/08` §9 (lines 257-276, 280), §10.1 (lines 314-322), §13 (lines 380-383). Real M is **481**, not 450: +6.9 %, above the 5 % trigger stated on line 280. The table should be updated. Real counts per branch with Est. and Band:

   | Branch | Real | Est. | Band |
   |---|---|---|---|
   | foundation | 34 | 32 | 26-38 |
   | kotlin | 65 | 60 | 48-72 |
   | android-core | 37 | 34 | 27-41 |
   | ui | 49 | 44 | 35-53 |
   | concurrency | 32 | 28 | 22-34 |
   | architecture-data | 33 | 30 | 24-36 |
   | app-networking | 16 | 16 | 13-19 |
   | kmp | 22 | 22 | 18-26 |
   | dsa | 46 | 46 | 37-55 |
   | daa | 22 | 22 | 18-26 |
   | os | 23 | 22 | 18-26 |
   | networking | 21 | 20 | 16-24 |
   | testing | 22 | 20 | 16-24 |
   | profiling | 16 | 14 | 11-17 |
   | native-ndk | 11 | 10 | 8-12 |
   | security | 15 | 14 | 11-17 |
   | tools | 17 | 16 | 13-19 |

   All 17 are inside their Bands, so R2 holds. Only the headline totals are stale. Real phase totals are 34 / 60 / 84 / 120 / 125 / 58 = 481. Timelines at the real size: 3 a week gives about 160 weeks (3 y 1 m); with one rest week per quarter, 3 y 4 m; at 2.5 a week, 3 y 9 m. Foundation is done in about 2.6 months. Phase 3 ends at 178 leaves (about 1 y 1 m) and phase 4 at 298 leaves (about 1 y 11 m). Fix: add a "Real (481)" column, update the totals and the §13 text, and log the update. Owner: `curriculum-designer` with `housekeeper`, since `housekeeper` reports the real M.
4. **Low.** `docs/08` F6 (line 372). It says "about 22 fruits". The roadmaps hold 20 (12 branches have 1, 4 have 2, foundation has 0). Fix: "about 20".
5. **Low.** `docs/08` §12 example (line 374). `reading-code` is the last stem of `kotlin`, so its last leaf is the F2 fruit, not an "extra fruit (F3)". Also, all of `kotlin` sits in phases 2-3. Fix: reword the example. Mechanical.
6. **Low.** `docs/08` §3 (line 98) says reading time is 10-15 min with a 20 min limit. `docs/04` §6 says 8-12 min for reading and 15-25 min for doing. Fix: align `docs/08` to `docs/04` (owner of lesson shape) or document why they differ.
7. **Low.** `docs/05` line 281 cites "`docs/08` §2 budget table". The table is in `docs/08` §3. Mechanical.
8. **Low.** Phase mismatch. `docs/05` lines 47 and 632 put the determinism replay test in "Phase 6". `docs/10` puts the physics layer in Phase 5 and the editor in Phase 6. Fix: "Phase 5". Mechanical.
9. **Low.** `docs/05` stale pointers to closed items. §3.2 last paragraph (line 106) calls the marker rewrite "an open item (C3)", but it is settled in `docs/07` line 231. §9 line 442 points to C2; §10 intro (line 450) points to C1; §3.1 line 92 still asks `architect` to reword the `docs/07` row, which is done. §11.2 holds five empty "resolved" rows. Mechanical.
10. **Low.** `docs/05` and `docs/10` disagree on the Phase 5 sims. `docs/10` lists `call-stack`, `hash-table-buckets` and `activity-lifecycle`. `docs/05` uses `call-stack-tube`, `lifecycle-timeline` (a `code-to-screen` sim) and has no hash sim ID. `docs/07` line 241 also uses `SimHost("call-stack")`. `docs/10` also says "identical results on reload" and "stepped fallback", while `docs/05` promises the same state, not the same coordinates, and a settled-state fade. Fix in `docs/10` and the `docs/07` examples. Owner: orchestrator.
11. **Low.** `docs/08` §15 Q3 says `docs/04`, `05` and `06` "are not written yet", but they exist. Also Q4 asks whether `docs/03` should mention `## Later`; it still does not. Mechanical for the first part, owner needed for Q4.
12. **Low (design note, not an error).** `docs/08` §10.2 asks for 2 app-track leaves and 1 theory leaf a week. Phase 2 has only 9 theory leaves (dsa) out of 60 real leaves, and phase 6 has about 6 `os` leaves out of 58. The mix cannot hold in those phases. The eligibility rule allows slack, so add a one-line note that the mix is best-effort.

## Unverified (cannot be settled from official docs, left as VERIFY)

- Matter.js has no continuous collision detection. The README is silent; this is common knowledge, not documented.
- Determinism on one machine.
- `Body.setStatic(false)` restoring the earlier mass. The Body docs do not say so.
- The `~25 KB gzip` figure for Matter.js (DepScope). Only the 83.5 kB minified size was confirmed; measure the shipped file.
- The combined gzip size of Three.js (see item 1).
- The Kotlin docs line on `plain` module kind describes compiled output only. They do not confirm that `external` declarations without `@JsModule` resolve under the module kind Kobweb uses. The risk is low; test in Phase 0.
- Planck 1.5 about 68 KB gzip and Rapier 0.19.x sizes. They are used only in rejected alternatives and do not affect any decision.

## Sources checked

https://registry.npmjs.org/matter-js · https://registry.npmjs.org/three · https://brm.io/matter-js/docs/classes/Runner.html · https://brm.io/matter-js/docs/classes/Body.html · https://brm.io/matter-js/docs/classes/Engine.html · https://app.unpkg.com/matter-js@0.20.0/files/build · https://github.com/liabru/matter-js · https://kotlinlang.org/docs/js-modules.html · https://discourse.threejs.org/t/three-js-now-imports-three-core-js/75187
