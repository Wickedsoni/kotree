# W1-a review: foundation/how-computers-work, leaves 1 to 4

Reviewed 20 Oct 2026 by `reviewer` under L31 (autonomous publishing; reviewer is the gate). Files are in `content/foundation/how-computers-work/`. Nothing was edited.

VERDICT: FIX (content: 1 leaf PASS, 3 leaves FIX; all FIX items are small and mechanical)

| Leaf | Verdict | Go-live hold |
|---|---|---|
| what-does-a-computer-do | VERDICT: PASS | HOLD until sim `input-process-output` ships |
| what-is-a-bit | VERDICT: FIX (5 items) | HOLD until sim `binary-switches` ships |
| how-letters-become-numbers | VERDICT: FIX (7 items) | none (no sim). Publish after its `requires` leaf `what-is-a-bit`, which is itself on HOLD |
| what-is-a-cpu | VERDICT: FIX (7 items) | HOLD until sim `recipe-card-pointer` ships |

## Go-live HOLD for the three sim leaves (needs owner)

Content PASS is not a go-live PASS. `docs/04` 4.3 and P2: "a live leaf either has a working sim or no See it move section", and no placeholder text. `docs/sims/` holds only `needed-ids.md`; no sim spec and no built sim exists for `input-process-output`, `binary-switches` or `recipe-card-pointer`. I found no validator (V1 to V12, CH1 to CH10) that checks a SIM id on a live leaf against a built sim, so the build will not stop it. `housekeeper` must not set `live` on leaves 1, 2 or 4 until `sim-engineer` ships the sim and a sim review passes. Dropping the See it move section is not an alternative: the roadmap rows require these sims, and `sims: []` needs the roadmap to say why not. Owner decision: keep the HOLD, or amend the roadmap rows.

## Build

`cd /home/user/kotree && ./gradlew :site:validateContent` printed `validateContent: ok` and `BUILD SUCCESSFUL` (first try, no 429, no warnings). It checks shadow leaves only for V1 to V11, so it says nothing about V12 (see below).

## Checks that passed for all four leaves

- Frontmatter: all keys in `docs/00` 8.2 order; `id`, `title`, `difficulty`, `order`, `requires`, `fruit` equal the roadmap rows; `status: shadow`; dates empty; `video: ""`; `upnext: false`.
- `sims` equals the SIM marker (leaves 1, 2, 4) or `[]` with a roadmap `none:` reason (leaf 3). Sim ids are in `docs/sims/needed-ids.md`; the Try-this items match those one-line specs.
- `playground: false`: no Try it, no Fix the bug; every Predict is `run: false`, `verified: not-run`, 3 options, one correct, a `why` on each. I hand-traced all 12 Predict items; every marked answer is right.
- Section order, exact heading names, no empty headings, no Video or Capstone text, Notice/Guess/Build/Name it in order, "Where the analogy stops" present, one analogy per leaf.
- Voice (L29, T1 to T8): zero banned words, zero exclamation marks, zero emoji, no "wrong/incorrect/failed" shaming, each already-bold term is bold once (first-use bolding gaps are FIX items below). Prose is 1,056 to 1,302 words (budget 900 to 1,600).
- Design/hard rules: no colours, fonts or spacing, no images (so no alt text needed), no emulator mention. Reduced-motion and keyboard checks belong to the sims, which do not exist yet.
- Kotlin runs: none possible here (no kotlinc). None of the four leaves claims a run. No `kotlin` fence exists, so no code block is unverified.

## Facts checked (official pages, read 20 Oct 2026)

| Claim | Source | Result |
|---|---|---|
| Kotlin `Byte` is 8 bits | kotlinlang.org/docs/numbers.html, table row `Byte | 8 | -128 | 127` | Confirmed (range missing from the leaf, see bit FIX 2) |
| Binary literal `0b00001011` | same page, "Binaries: `0b00001011`" | Confirmed |
| Every character has a numeric Unicode value; `'A'.code` is 65; `'a' + 1` is `b`; `'a' - 32` is `A` | kotlinlang.org/docs/characters.html | Confirmed |
| "It shows an emoji as a Unicode character too" | same page | NOT supported. The page's emoji example is a 2-`Char` string, and the page says such characters "cannot be represented by a single `Char`". See letters FIX 2 |
| Tofu, later emoji, Android 11 (API 30) and lower cannot update the emoji font | developer.android.com/jetpack/compose/text/emoji | Confirmed: "later emoji might be displayed as a small square box called tofu"; "Android versions 11 (API level 30) and lower can't update the emoji font" |
| `onTouchEvent()` called on a touched View such as a Button | developer.android.com/develop/ui/views/touch-and-input/input-events | Confirmed, verbatim idea |
| ANR; shown when the main thread cannot respond to input; 5 seconds for a key press or screen tap; do not block the main thread | developer.android.com/topic/performance/anrs/keep-your-app-responsive | Confirmed |
| Color int: 4 components packed in a single 32-bit integer, "the most common representation" | developer.android.com/reference/android/graphics/Color (fetched with curl; the page also lists Color longs and Color instances) | Confirmed, but the leaf says "a colour is held in one whole number" and drops "most common". See bit FIX 1 |
| Phone chips usually have several cores | developer.android.com/reference/android/os/Process, `getExclusiveCores()`: "the foreground process may have one or more CPU cores exclusively reserved for it" | Supports "several cores exist". The words "usually" and "several" are textbook-standard; keep them |

Not verifiable from allowed sources (textbook-standard, kept with plain wording, no edit needed): fetch, decode, execute loop; program counter; jump; clock sets the pace; a switch is one of two states; binary place values and 2^n counts (arithmetic, checked by hand: 8 switches 256, 9 switches 512, max 511).

## VERIFY comments resolved

- what-is-a-bit line 18: the `Color` page now opened and read. Resolved by bit FIX 1 (the wording must match the page). Strip the whole comment.
- what-is-a-cpu line 18: the fetch-execute, program counter, jump and clock claims are textbook-standard and are worded plainly in the leaf; the "several cores" claim has the Android `Process.getExclusiveCores()` support above. Resolved. Strip the whole comment.

## Strip before live (V12)

V12 flags any raw HTML in a live body except the exact SIM marker. Each of these leaves has exactly one `<!-- SOURCES ... -->` comment on line 18, right above `## The question`. `housekeeper` must delete line 18 in all four leaves when setting `live`:
- what-does-a-computer-do (SOURCES only, no VERIFY)
- what-is-a-bit (SOURCES plus ⚠️ VERIFY)
- how-letters-become-numbers (SOURCES only)
- what-is-a-cpu (SOURCES plus ⚠️ VERIFY)
Keep the `<!-- SIM: ... -->` markers in leaves 1, 2, 4. Move the source URLs into the commit message or `docs/reviews` (this file holds them above) so the record is not lost.

---

## what-does-a-computer-do: PASS

No FIX items. Notes: Android section says the classic View system calls `onTouchEvent()`, which matches the page. Recap lines are one sentence each. Strip line 18 at go-live (housekeeper).

## what-is-a-bit: FIX

1. `what-is-a-bit.md` line 193 (Where this shows up in Android), mechanical.
   Old: `The Android reference for its `Color` class says a colour is held in one whole number, with four parts (opacity, red, green and blue) packed into 32 bits.`
   New: `The Android reference for its `Color` class says the most common way to hold a colour is a colour int. It packs four parts (alpha, which is opacity, then red, green and blue) into one 32-bit whole number.`
   Reason: the page names three representations (ints, longs, `Color` objects); "a colour is held in one whole number" overstates it.
2. `what-is-a-bit.md` line 100 (Name it), mechanical.
   Old: `Kotlin's `Byte` type is 8 bits wide.`
   New: `Kotlin's `Byte` type is 8 bits wide. It counts from -128 to 127 so it can hold negative numbers, but this lesson counts from 0.`
   Reason: the leaf says 8 switches reach 255, and the Kotlin table says `Byte` tops out at 127. A learner who tries `Byte` would be confused.
3. `what-is-a-bit.md` lines 200 to 201 (Recap), mechanical. Template: each line is one sentence.
   Old 2: `Each switch has a worth that doubles to the left. Add the worths of the lit ones.`
   New 2: `Each switch has a worth that doubles to the left, and you add the worths of the lit ones.`
   Old 3: `Every extra switch doubles the patterns. Eight switches give 256, which is 0 to 255.`
   New 3: `Every extra switch doubles the patterns, so eight switches give 256, which is 0 to 255.`
4. `what-is-a-bit.md` line 47 (What you should notice), mechanical. 30 words, two ideas (T7).
   Old: `Each switch adds its own worth to the counter when it is on, and each extra switch doubles how many different patterns the row can make.`
   New: `A lit switch adds its worth to the counter, and each extra switch doubles the patterns.`
5. `what-is-a-bit.md` line 18, mechanical, at go-live: delete the SOURCES / VERIFY comment (V12).

Optional, not blocking: line 36 says "The next lesson shows the rule for letters." That is true today but goes stale if the branch is reordered. Safer: `A later lesson shows the rule for letters.`

## how-letters-become-numbers: FIX

1. `how-letters-become-numbers.md` line 8 (summary) and line 42 (Notice), mechanical. "Cannot keep a shape" is too strong: a font is stored shapes, and the leaf says so itself.
   Line 8 old: `A computer cannot keep the shape of a letter. It keeps a number, and a shared list says which letter that number means.`
   Line 8 new: `A computer does not keep text as shapes. It keeps a number, and a shared list says which letter that number means.`
   Line 42 old: `...and the computer cannot keep a shape. But it can keep a number.`
   Line 42 new: `...and the computer does not keep text as shapes. It keeps a number instead.`
2. `how-letters-become-numbers.md` line 66 (Name it), mechanical.
   Old: `The Kotlin documentation says that every character has a numeric Unicode value, and it shows an emoji as a Unicode character too.`
   New: `The Kotlin documentation says that every character has a numeric Unicode value.`
   Reason: the page's emoji example shows an emoji does not fit in one `Char`. The leaf's clause is not what the page shows.
3. `how-letters-become-numbers.md` line 178 (Recap), mechanical.
   Old: `An agreed list says which number is which letter. The big shared one is Unicode.`
   New: `An agreed list says which number is which letter, and the big shared one is Unicode.`
4. `how-letters-become-numbers.md` line 171 (Android), mechanical. 34-word sentence (T7).
   Old: `It says that if an app does not support the latest emoji fonts, later emoji might be displayed that way, and that Android 11 (API level 30) and lower cannot update the emoji font.`
   New: `It says that if an app does not support the latest emoji fonts, later emoji might be displayed that way. It also says that Android 11 (API level 30) and lower cannot update the emoji font.`
5. `how-letters-become-numbers.md` line 18, mechanical, at go-live: delete the SOURCES comment (V12).
6. `how-letters-become-numbers.md` line 36 (Mental model), mechanical (T2: bold the first use of a technical term). "Font" is a technical term first used here.
   Old: `The shape you see on screen is drawn later by a font, which is a set of pictures that the screen uses.`
   New: `The shape you see on screen is drawn later by a **font**, which is a set of pictures that the screen uses.`
7. `how-letters-become-numbers.md` line 171 (Android), mechanical (T2). "Tofu" is a technical term named by the Android page.
   Old: `The Android documentation calls this a small square box called tofu.`
   New: `The Android documentation calls this small square box **tofu**.`

Optional, not blocking: line 62 `as in the last lesson` → `as in the bit lesson` (same staleness reason).

## what-is-a-cpu: FIX

1. `what-is-a-cpu.md` lines 65 and 68 (Guess), mechanical (exact text given). Ring and step are confused. In the model one ring does one line, so "go back to line 1" takes its own ring, and cracking the egg again is the next ring. Option B packs two steps into one ring and contradicts "one line per ring".
   Line 65 old: `The bell rings a third time. The finger is on line 3, which says "go back to line 1". What happens at the next ring?`
   Line 65 new: `The bell rings a third time, and the cook does line 3, which says "go back to line 1". Where does the finger go, and what does the ring after that do?`
   Line 68 old: `- B. The cook moves the finger to line 1 and cracks another egg.`
   Line 68 new: `- B. The finger goes to line 1, and the next ring cracks another egg.`
   Line 71 (answer paragraph) needs no change.
2. `what-is-a-cpu.md` lines 113 to 114 (Predict `after-line-three`), mechanical. Same ambiguity: "the bell rings ... the next ring" can be read as line 3 itself, which is not an option.
   Old: `The finger is on line 3 and the bell rings.` then `Which line does the cook do at the next ring?`
   New: `The finger is on line 3 and the bell rings, so the cook does line 3.` then `Which line does the cook do at the ring after that?`
   The options, `output: "Line 1"` and the `why` texts stay.
3. `what-is-a-cpu.md` line 190 (Common mistakes), mechanical. As written, "a phone chip usually has several cooks, that it looks simultaneous" blurs real parallel cores with fast steps.
   Old: `The steps are so fast, and a phone chip usually has several cooks, that it looks simultaneous.`
   New: `The steps are so fast that it looks simultaneous. A phone chip also has several cores, so a few things do run side by side, but each core still follows its own card one step at a time.`
4. `what-is-a-cpu.md` lines 203 to 204 (Recap), mechanical.
   Old 2: `A program counter marks the next instruction. A jump can change it.`
   New 2: `A program counter marks the next instruction, and a jump can change it.`
   Old 3: `The clock sets the pace. One long step makes everything behind it wait.`
   New 3: `The clock sets the pace, and one long step makes everything behind it wait.`
5. `what-is-a-cpu.md` line 48 (What you should notice), mechanical. 40 words, four ideas (T7).
   Old: `The finger moves one line per ring of the bell, it never skips a line by itself, a "go back" line sends it up to an earlier one, and with no ring it does not move.`
   New: `The finger moves one line per ring, only a "go back" line sends it up, and with no ring it stays put.`
6. `what-is-a-cpu.md` line 18, mechanical, at go-live: delete the SOURCES / VERIFY comment (V12).
7. `what-is-a-cpu.md` lines 37 and 196 (T2: bold the first use of a technical term), mechanical. Bold the first use of "cores", "ANR" and "main thread", each already defined in its sentence or the next.
   Line 37 old: `It usually has several cores, which are like several cooks,`
   Line 37 new: `It usually has several **cores**, which are like several cooks,`
   Line 196 old: `The Android documentation calls this an ANR, short for "application not responding". It says the system shows one when an app cannot answer user input on its main thread.`
   Line 196 new: `The Android documentation calls this an **ANR**, short for "application not responding". It says the system shows one when an app cannot answer user input on its **main thread**.`
   Line 198 (`The main thread is the part of an app that answers your taps.`) stays: it supplies the plain meaning.

## Not checked

- Sims do not exist yet (see the HOLD section), so keyboard path, reduced motion and sim contrast cannot be reviewed. The sim-engineer review must cover them (docs/05).
- No Kotlin was run (no kotlinc). The leaves contain no Kotlin code, so nothing needs a run.
