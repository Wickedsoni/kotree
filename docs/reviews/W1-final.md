# W1 final re-check

Date: 20 Oct 2026. Scope: 7 leaves listed in the task. `run-your-first-program` is OUT of scope (stays shadow). Nothing edited in the leaves.
Policy (L31 ruling): a SIM marker with no built sim does not block a leaf. The renderer draws nothing for an unregistered sim (LessonView.kt simRegistry).

Build: `./gradlew :site:validateContent` prints `validateContent: ok`, `BUILD SUCCESSFUL`, no warnings.

VERDICT: PASS (7 of 7)

| Leaf | Verdict | Remaining fix |
|---|---|---|
| what-does-a-computer-do | PASS | none |
| what-is-a-bit | PASS | none |
| how-letters-become-numbers | PASS | none |
| what-is-a-cpu | PASS | none |
| memory-vs-storage | PASS | none |
| what-is-an-os | PASS | none |
| what-is-code | PASS | none |

## Verification notes

- W1-a bit items 1 to 4, letters items 1 to 4 and 6 to 7, cpu items 1 to 5 and 7: each present with the exact reviewer wording (checked line by line). The cpu Guess and Predict `after-line-three` now read consistently with the answer `Line 1`.
- W1-b memory items 1 to 5, os items 1 to 7, what-is-code items 1 to 4: all applied. OS line 38 is 27 words but is one idea (T7 not triggered).
- Strip step, not a FAIL: each leaf still carries its `<!-- SOURCES -->` or `<!-- VERIFY -->` comment on line 18 (what-is-code: lines 18 to 19). This is a go-live step for `housekeeper` (V12), which must delete it and keep the `<!-- SIM: ... -->` marker. The source trail is in `W1-a.md` and `W1-b.md`.
- Git note: memory-vs-storage, what-is-an-os and what-is-code fixes are uncommitted in the working tree. Commit before `housekeeper` runs.
- Optional items (not applied, not blocking): bit line 36 "The next lesson", letters line 62 "the last lesson", memory `desk-is-full` gives away the answer, what-is-code line 26 sentence of 26 words.
- Still unverifiable and accepted as textbook CS (per W1-a and W1-b): RAM and storage volatility, fetch-execute loop, OS start order. No Kotlin was run (no kotlinc); the one Hello world block in what-is-code is read-only with no run claim.
