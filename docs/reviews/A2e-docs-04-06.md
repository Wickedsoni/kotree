# Review A2e: docs/04, content/_templates/leaf.md, docs/06

VERDICT: FIX

Reviewed against docs/00 (with 18 Oct 2026 amendments L15, L25, L26, D15-D17), docs/01 (P1-P10, tone section 7), and docs/03, 05, 07. No edits were made to the reviewed files.

## What passes

- Template and the docs/04 section 10 worked example have the same 12 `## ` headings, in the same order, with identical names. They match the table in docs/04 section 4. The template's sub-headings (Notice, Guess, Build, Name it) match too.
- Frontmatter keys and order match docs/00 section 8.2 in the template, in the docs/04 section 2 table, and in the worked example. `upnext` is optional and V9 is applied correctly.
- No Capstone or Video heading is written in any body. Both are only mentioned as things the author must not write (04 lines 30-32 and 349-351, template lines 35-37). `video: ""` handling matches L26 and 07 section 6.5.
- No `/kotree` base path in any of the three files. grep confirms the only hits are in docs/07 (the "must not appear" check).
- No emulator or simulator promise. The "no android.*" rule in 06 section 1 matches 04 sections 3.4 and 4.10.
- No banned words, exclamation marks or red/shaming copy in the three files. Copy in 06 section 6 follows the "clue, not failure" rule (docs/01 section 3.4).
- Kotlin facts in the worked example were checked against https://kotlinlang.org/docs/null-safety.html:
  - `val s: String = null` does not compile.
  - `.length` on `String?` does not compile.
  - `b!!.length` on null throws NPE.
  - Printing a null `String?` gives `null`, which also settles the example's own "not verified" row.
  - The list of NPE causes includes `!!` and Java interop.
  - The Java interop claim was not re-read. Only the null-safety page was fetched.
- The example's code blocks were not compiled by me (no Kotlin CLI used). They are small and look correct on reading, but they stay unverified until run.

## FIX list

1. **[docs/04 section 5.3 vs docs/06 section 3, and docs/07 section 6.2 / 6.4] Two incompatible challenge formats. Severity: HIGH. Needs owner.**
   - docs/04 specifies plain Markdown under a `<!-- CHALLENGE -->` comment: `**Answer:**`, `**Why:**`, `**If you picked another one:**`, `**Hint 1/2:**`, `**Fixed version:**`.
   - docs/06 specifies a fenced block with info string `challenge` holding YAML (`type`, `id`, `verified`, `options[].why`, `hints`, `expected`, `fix`).
   - docs/04 says no other marker may be invented, and docs/07 rewrites and validates only the SIM marker. There is no sync rule for CHALLENGE and no `validateContent` rule for either form.
   - Not buildable until one format is chosen.
   - Suggested fix: keep docs/04's plain Markdown as the single authoring format, because it reads on GitHub and offline. Then make docs/06 section 3 a parse spec for that Markdown, and add a CHALLENGE rewrite plus validation rules to docs/07 sections 6.2 and 6.4.
   - Missing machine data in docs/04's Predict item: the exact stdout used by "Run it to check". docs/06 has `output`, docs/04 has nothing. Add an `**Output:**` fenced block, or let the owner choose.
   - Missing in docs/06's format: the five reviewer questions and the AI-style marker. 04 section 3.6 has the questions, and 06's `generated: true` has no equivalent in 04.
   - If docs/06's YAML is kept instead, amend docs/04 sections 4.5, 4.7, 5.1-5.3 and the template, because the template would then need a `challenge` block.

2. **[docs/04 vs docs/06 vs docs/09 line 93] Counts disagree. Severity: MED. Mechanical once item 1 is decided.**

   | Item | docs/04 | docs/06 | docs/09 |
   |---|---|---|---|
   | Predict items | 1 to 3 (5.1, 6) | 2 to 3 (3.2, 8) | 2 to 3 |
   | Predict options | exactly 3 (5.1) | 3 or 4 (3.2) | |
   | Fix hints | 2 (5.2) | exactly 3 (3.3) | |
   | Fix code length | 10 to 30 lines (5.2) | under ~15 lines (8) | |
   | Predict on a no-code leaf | always required (4) | "none on leaves with no code" (8) | |

   - Pick one set of numbers. Suggested: 1 to 3 Predict, exactly 3 options, 2 hints, 10 to 30 lines.
   - For the no-code case, align on 04: Predict is paper-and-pencil on theory leaves.

3. **[docs/06 line 7 banner, line 5, section 9 E1 and E5] Stale L15 conflict banner. Severity: MED. Mechanical.**
   - L15 and D8 were amended on 18 Oct 2026, and docs/00 now says no emulator or simulator.
   - Delete the "CONFLICT WITH L15" block and the "docs/00 wins on paper" sentence.
   - Mark E1 as done.
   - Check E5 against docs/10, whose exact filename I did not locate.

4. **[docs/04 line 5 and section 10 intro, O1, and the example comment line 431] Stale "docs/05 does not exist yet". Severity: MED. Mechanical.**
   - docs/05 and docs/06 exist now.
   - `null-lunch-box` is specified in docs/05 section 12 (around line 450), so it is no longer a placeholder.
   - Update line 5 ("Neither exists yet"), the comment on example line 431, the section 10 intro and O1.
   - The `⚠️ VERIFY` for compile-not-run must stay until the code is run.

5. **[docs/04 section 2 `playground`, section 4.4, and the template] The `playground` rule is ambiguous. Severity: MED. Needs owner.**
   - Section 4.4 says every Name it ends with a runnable `kotlin` program, and Predict items are also `kotlin` programs with `fun main()`.
   - Section 2 says `playground` is true exactly when runnable code exists, with a parenthetical excluding Name it.
   - Read literally, every leaf would be `playground: true`, and Try it and Fix the bug would never be optional.
   - Define "runnable" precisely. Suggested wording: a block the learner can run in the editor, which Name it blocks are or are not, with the answer written down.
   - Say whether Predict's "Run it to check" requires `playground: true`. It must, since it runs code.

6. **[docs/06 section 2.2] Playground option names and claims. Severity: MED. Mechanical (see the verification table below).**
   - Pin the Kotlin version with `data-version`, not only `data-min-compiler-version`.
   - Output height: `data-output-height` is documented only for the `canvas` target, so a fixed ~120 px for `java` is unconfirmed.
   - "Hide lines outside the sample region" is not what the README says. It says only the `//sampleStart` to `//sampleEnd` region is editable.
   - Remove the ⚠️ VERIFY tags that are now settled.

7. **[docs/06 section 2.1 and section 3.4] Smaller detail fixes. Severity: LOW. Mechanical.**
   - The init function returns a Promise of an array of instances, per the source JSDoc. State this and remove the ⚠️ VERIFY.
   - The local storage key `kotree:challenge:<leafId>:<challengeId>` should follow docs/03 section 9's `kotree.v1.*` namespace, for example `kotree.v1.challenge.<leafId>.<challengeId>`. docs/06 itself says to match docs/03.

8. **[docs/06 sections 5 and 3.1] The Kotlin version example is old and the pin is unrecorded. Severity: LOW. Needs owner.**
   - `kotlin-2.0.21` is a placeholder. The latest stable is 2.4.20 (7 Sep 2026, per kotlinlang.org "what's new"); I found that on one page only.
   - docs/07 contains no pinned Kotlin version, so 06 section 5's "recorded in docs/07" has no target.
   - `architect` should pin the version in docs/07 (E4), choosing one the Playground offers.

9. **[docs/04 lines 22, 26, 331 and the template line 36 vs docs/03 line 331] Button label mismatch. Severity: LOW. Mechanical.**
   - docs/04 and the template say "Mark as finished".
   - docs/03 says the toggle is "I finished this".
   - Use docs/03's wording.

10. **[docs/04 lines 20 and 26, section 9.1] `tree.json` data source. Severity: LOW. Needs owner.**
    - docs/04 says the layout reads `tree.json`, following docs/03.
    - docs/07 section 6.4 says generated `TreeData.kt` is chosen and a runtime `tree.json` fetch is rejected.
    - This is a docs/03 vs docs/07 conflict that 04 inherits. Fix it at the source (`architect`), then reword docs/04.

11. **[docs/04 section 10 example, Name it, line 508] "The last two lines are skipped". Severity: LOW. Mechanical.**
    - The skipped part is the three-line `if` block. Reword to "the `if` block is skipped".

12. **[docs/04 section 10, Mental model vs Tricks] Extra analogy object. Severity: LOW. Mechanical.**
    - The "note with no words" object appears in "Where the analogy stops" and in Tricks, but is not listed in the Mental model's object list.
    - 04 section 3.2 says every object is named once there. Add it to the list, or drop it from Tricks.

## Verified quickly by web search (5 of 8 budget used)

| Item | Result | Source |
|---|---|---|
| Playground script URL and `@1` tag | OK | README uses `https://unpkg.com/kotlin-playground@1`. package.json `main` is `dist/playground.min.js`, so 06's full path resolves. Current version 1.34.0. |
| Init function name and Promise | `KotlinPlayground(selector)` from the CDN, `playground(selector, options)` from NPM. Source JSDoc: returns `Promise<Array<ExecutableCode>>`. | README and src/index.js |
| Option names | `data-target-platform` (java default), `theme` (`idea\|darcula\|default`), `data-autocomplete` (default false), `data-min-compiler-version`, `data-version`, `data-highlight-only`, `from` / `to`. | README |
| Sample markers | `//sampleStart` and `//sampleEnd` mark the editable region. Hiding is not stated. | README |
| Offline and server-down behaviour | **Not documented.** Stays ⚠️ VERIFY. Needs a real test in dev tools. | README, src/index.js |
| Kotlin null-safety facts for the worked example | All confirmed. | https://kotlinlang.org/docs/null-safety.html |

Not settled, left for `architect`:
- Whether Kobweb keeps HTML comments in Markdown. The kobwebx-markdown README URL returned 404.
- Whether `{{{ Foo("x") }}}` with an argument is accepted.
- Offline Playground behaviour.
- The `kotlin-playground` README is silent on the compile-server failure path.

## Sources

- https://github.com/JetBrains/kotlin-playground (README, package.json, src/index.js)
- https://kotlinlang.org/docs/null-safety.html
- https://kotlinlang.org/docs/whatsnew2420.html
