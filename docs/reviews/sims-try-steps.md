# Review: ADR 009 sim blocks, Try this reachability and prose consistency

Scope: the ```sim block in the 39 leaves touched by commits 9d737e3, 27cb3ac (stepper, states), 0f51006, bc79464 (lanes), 5e1c9a1 (flow).
Method: a Python port of `SimCore.fold`, `TryProgressCalc.of` and `TemplateSimHost.fire` (same-id setting events replace the previous history entry) replayed every `done` list, every `script`, and the prose of each leaf. Source of truth: `site/src/jsMain/kotlin/dev/kotree/site/sims/core/*` and `TemplateSimHost.kt`.
NOT done: browser preview, keyboard-only run, reduced-motion run. Only core semantics were traced. Facts of Kotlin/Android in the prose were not re-verified here (not the scope of this review).

VERDICT: FIX (5 leaves need a change; 34 pass)

## Mechanical results (all 39 leaves)

- Every leaf has exactly one sim fence, `sims:` frontmatter equals the fence id, no legacy `<!-- SIM -->` marker, Try count 1-3 plus at most 1 optional, exactly one `say` per case, every `done` event exists (action, drop, toggle, slider value in range and on step), every quoted control name in a Try step matches a real label.
- No slider or toggle step asks for a value the control already holds, except where noted.
- Refusals inside Try steps: what-is-an-os (`walk-in`, step 2 promises "See what the manager does"), what-is-a-list (`ask-3`, step 2, prose promises the shake), exceptions (`next` after alarm=2, text says "The run replays"), lateinit (`guest` after `usher`, text says "the guest is too late"). All four are promised or visible in the step text. Accepted.
- Hoisting check (last setting event wins from event 0): traced for every leaf with toggles or sliders. Results match the step texts, except the first FIX below.

## Per leaf

| Leaf | Verdict | Note |
|---|---|---|
| android-core/what-is-an-app/who-starts-your-app | PASS | |
| app-networking/requests-from-an-app/what-an-app-sends-and-gets | PASS | |
| concurrency/why-the-main-thread/what-the-main-thread-does | PASS | "Tick" for a switch, see global note 3 |
| concurrency/why-the-main-thread/why-blocking-freezes-the-screen | PASS | numbers 16 / 800 / 50 / 5000 / 6000 agree with prose |
| daa/analysis/best-worst-average-case | PASS | note A below |
| foundation/debugging-mindset/find-a-bug-without-guessing | PASS | 16 rooms, 4 tries, fuse/room wording agree |
| foundation/first-code/lines-run-in-order | PASS | Ready/Set/Go swap agrees with Notice and Guess |
| foundation/from-code-to-running/what-is-a-compiler | PASS | |
| foundation/from-code-to-running/what-is-the-jvm | PASS | |
| foundation/how-an-app-is-born/how-an-app-gets-installed | PASS | |
| foundation/how-an-app-is-born/what-happens-when-i-build | FIX | Try steps 2 and 3 cannot both complete, FIX 1 |
| foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon | PASS | open=on shows 3 log rows, "fewer steps" holds |
| foundation/how-computers-work/what-does-a-computer-do | PASS | note B |
| foundation/how-computers-work/what-is-a-bit | PASS | drop targets are column-aligned (1 to lit.8 ... 256 to lit.0), step 3 reachable; prose agrees |
| foundation/how-computers-work/what-is-a-cpu | FIX | prose card differs from sim card, FIX 2 |
| foundation/how-computers-work/what-is-an-os | PASS | |
| foundation/programming-thinking/how-a-program-decides | FIX | `{temp}` in `intro`, FIX 3 |
| foundation/programming-thinking/how-a-program-repeats | PASS | boil slider 3..5 is reachable from 3 |
| foundation/programming-thinking/loop-over-a-list | PASS | start=10 gives 13/18/20/24, wipe=on ends on total 4 |
| foundation/programming-thinking/trace-code-by-hand | PASS | start=3 then Step prints 9; table in prose equals sim. note C |
| foundation/programming-thinking/what-is-a-function | PASS | |
| foundation/programming-thinking/what-is-a-list | PASS | needed-ids promised a typed number; the sim uses buttons and the prose never says "type" |
| foundation/programming-thinking/what-is-a-variable | FIX | "Trace it like the sim", FIX 4 |
| kmp/what-is-kmp/how-kotlin-reaches-an-iphone | PASS | |
| kotlin/collections/index-out-of-bounds | PASS | |
| kotlin/control-flow/break-and-continue | PASS | both rules on: tray 3 letters, unread 3 letters, prose agrees. "Tick" note |
| kotlin/control-flow/when-expression | FIX | prose chute list has a "2" chute the sim lacks, FIX 5 |
| kotlin/errors-and-results/exceptions | PASS | slider is 0/2/4/6 only; texts use 4, 2, 0, 4; line-to-room map (1 hall, 2 office, 3 and 4 desk, 5 office, 6 hall) agrees with prose; step 4 shows "fails before any line runs" |
| kotlin/errors-and-results/try-catch-finally | PASS | |
| kotlin/extensions-scope-and-delegates/lateinit | PASS | |
| kotlin/extensions-scope-and-delegates/property-delegates | FIX | "slot 1", FIX 6 |
| kotlin/interfaces-and-inheritance/polymorphism-and-type-checks | PASS | |
| kotlin/lambdas/map-filter-and-friends | PASS | note D |
| kotlin/null-safety/safe-call-and-elvis | PASS | |
| kotlin/special-classes/sealed-classes | PASS | |
| networking/how-the-internet-works/how-data-travels-in-packets | PASS | arrival 2,1,4,5 agrees with the Notice |
| security/threat-thinking/who-would-attack-my-app | PASS | |
| testing/why-test/why-tests-exist | PASS | note E |
| tools/git/branches | PASS | |

Not listed as a problem after tracing (agents had flagged them): find-a-bug-without-guessing, what-is-a-list, index-out-of-bounds, why-blocking-freezes-the-screen, what-the-main-thread-does, loop-over-a-list, what-is-a-bit, bytecode-adapters, fork-and-gate (apart from the intro), bakery-build-line (apart from FIX 1), launch-path-manifest, factory-line-stations, why-tests-exist. Exceptions alarm slider 0/2/4/6 and trace-code-by-hand are consistent with their prose.

## FIX list

1. `content/foundation/how-an-app-is-born/what-happens-when-i-build.md`, sim `try` steps 2 and 3 (lines 113-116). Type: sim data, mechanical (fixer can apply the text below; owner may prefer other wording).
   Problem: `TemplateSimHost.fire` replaces the last history entry when it is a setting of the same id. Step 2 ends with `stamper=off`; step 3's first event is `stamper=on` straight after it, so `stamper=off` is overwritten, step 2 un-lights, and step 3 (blocked behind it) can never light by following the text. Reproduced: history after the natural clicks is `next x5, stamper=on, mixer=off, next` and progress is `[true, false, false]`.
   Old (lines 115-116):
   ```
     - text: "Switch the stamp station back on, switch off \"Mixing station\" and press \"Move the box on\". Later stations have nothing to work on."
       done: [ "stamper=on", "mixer=off", next ]
   ```
   New:
   ```
     - text: "Now also switch off \"Mixing station\" and press \"Move the box on\". Later stations have nothing to work on."
       done: [ "mixer=off", next ]
   ```
   Check: with stamper still off and mixer off, every `next` leaves the box at the start ("Nothing made"), so the text is true. Also the leaf's `script` has `"stamper=off", "stamper=on"` back to back; Step merges them too, which is harmless.

2. `content/foundation/how-computers-work/what-is-a-cpu.md`, line 116 (after the sim). Type: prose, mechanical. Problem: the sim card puts "Go back to line 1" on line 4 (line 3 is "Serve the dish"); the Notice, Guess and first Predict put it on line 3. A learner who runs the sim then reads the questions sees two different cards. Smallest true fix is a bridge sentence (full rewrite of Notice, Guess and the predict block is the alternative and needs the owner).
   Old: `**What you should notice:** The finger moves one line per ring, only a "go back" line sends it up, and with no ring it stays put.`
   New: `**What you should notice:** The finger moves one line per ring, only a "go back" line sends it up, and with no ring it stays put. In the animation the go-back line is line 4. The cards in the questions below put it on line 3, and the idea is the same.`

3. `content/foundation/programming-thinking/how-a-program-decides.md`, line 45, sim `intro`. Type: sim data, mechanical. Problem: `SimCore.initial` and `TextDescription` use `spec.intro` without interpolation, so "Text description" and Reset read the literal `{temp}`.
   Old: `intro: The ball waits at the fork. The temperature is {temp}. Only one sign is on the fork, and it asks "above 25?".`
   New: `intro: The ball waits at the fork. The temperature starts at 20. Only one sign is on the fork, and it asks "above 25?".`

4. `content/foundation/programming-thinking/what-is-a-variable.md`, line 169. Type: prose, mechanical. Problem: "Trace it like the sim" suggests the Kotlin program is the sim's program. The sim runs `var score = 0`, `score = 5`, `var halfTime = score`, `score = score + 1`, `println(score)`, `println(halfTime)`; the prose program has `println` after each assignment and no `halfTime`.
   Old: `Trace it like the sim. The first` (start of line 169)
   New: `Trace it the same way as the sim, one line at a time. This program is a different one from the sim's. The first`

5. `content/kotlin/control-flow/when-expression.md`, lines 145 and 147 (Notice). Type: prose, mechanical. Problem: the sim has four chutes `1`, `3 or 4`, `5 to 9`, `7 to 12` plus the bin; the Notice lists a `2` chute and says letter 4 passes "2". (The `2 ->` branch in the Kotlin sample is fine, it is code.)
   Old (145): `The office has these chutes from the top: "1", "2", "3 or 4", "5 to 9", and the bin "everything else".`
   New: `The office has these chutes from the top: "1", "3 or 4", "5 to 9", "7 to 12", and the bin "everything else".`
   Old (147): `- Letter 4 passes "1", then "2", then lands in "3 or 4". It never meets the chutes below.`
   New: `- Letter 4 passes "1", then lands in "3 or 4". It never meets the chutes below.`
   Check that the Guess below (chutes "5 to 9" above "7 to 12") still reads well: it does.

6. `content/kotlin/extensions-scope-and-delegates/property-delegates.md`, line 95. Type: prose, mechanical. Problem: "slot 1" means nothing in this sim; the toggle is a switch ("Brew when the shop opens") and the Try step says "Switch on".
   Old: `The tick-box moves the brew to slot 1 whether or not anyone presses.`
   New: `The switch moves the brew to opening time, whether or not anyone presses.`

## Notes (no FIX required; owner may polish)

A. best-worst-average-case: Mental model lists "left pocket, right pocket, bag, desk, shelf" as the five places; the sim keeps the keys in a pocket outside the five shelf places. Wording only.
B. what-does-a-computer-do: Notice says five presses (H E L L O); the sim has four keys. Separate example, not contradictory.
C. trace-code-by-hand: Mental model says "helper called double", the sim and Name it use `triple`. Mental model is generic; consider "triple" there.
D. map-filter-and-friends: Notice repaints in capitals, the sim repaints with "Mr". Separate example.
E. why-tests-exist: Mental model is a pilot checklist; the sim is twenty lamps and is not introduced in the analogy. Consider one sentence in the Mental model.

## Global items for the owner (not per leaf)

1. Core: `TextDescription.lines` calls `SimCore.initial(spec, spec.script)`, which hoists every setting in the script. For scripts that change a toggle or slider (what-is-a-cpu, fork-and-gate, exceptions, try-catch-finally, bakery, parcel-to-phone and others) the generated text description plays the early events with the LAST setting values, so it can read differently from the live Step trace. Needs `ui-engineer`.
2. Wording: Try steps say "Tick ..." for switches (what-the-main-thread-does, break-and-continue, loop-over-a-list). Controls are `role="switch"` buttons labelled "Label: On/Off". "Switch on" is the consistent verb. Mechanical, low priority.
3. `docs/lesson-checklist.md` is mid-edit in the working tree (WP8 in progress); at the time of this review item 8 still says "one `<!-- SIM -->` marker". Judged against ADR 009 section 7 instead. Re-check item 14 text once WP8 is committed.
4. Almost every `say` is more than one sentence (ADR 009 section 7 wants one plain true sentence). Not listed per leaf; decide whether the rule or the content changes.
5. Browser checks still owed: `/preview/<id>` for each leaf, mouse then keyboard-only then reduced motion; confirm keyboard "Drop in" buttons for the drag leaves (what-is-a-bit, best-worst-average-case, find-a-bug-without-guessing, who-would-attack-my-app).

## Fixes applied

- FIX 1 to 6 applied as written (what-happens-when-i-build, what-is-a-cpu, how-a-program-decides, what-is-a-variable, when-expression, property-delegates). validateContent: ok.
