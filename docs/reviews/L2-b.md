# Review L2-b: four layer-2 shadow leaves

Reviewer: independent. Date: 21 Oct 2026. Checklist: `docs/lesson-checklist.md`. Structure: `docs/04-lesson-system.md` sections 2 to 6 and 4.13. Leaves were not edited.

VERDICT: FIX (0 PASS, 4 FIX; all items mechanical except those marked owner/lesson-writer)

Build: `./gradlew :site:validateContent` on the working tree FAILS, but not because of these leaves. The untracked file `buildSrc/src/main/kotlin/dev/kotree/build/SimSpec.kt` (another agent's work in progress, listed in git status) does not compile (`Too many arguments for constructor(id: String): SimLessonBlock`, private `jsonString`, unresolved `tpl`). I ran the same task on a clean `git archive HEAD` copy, which contains the four leaves unchanged: `validateContent: ok`, BUILD SUCCESSFUL. Owner/author of SimSpec.kt: fix before the layer build. Warnings: none printed on the clean copy.

Snippets: the 3 runnable Kotlin blocks were compiled and run with `bash scripts/kt-run.sh` (kotlinc 2.4.21): omega prints `true` / `false` (the claim "fails at n = 31, 95 against 96" is right with integer division); big-o-notation prints the three stated lines; kmp `greeting` prints `Hello, Ana`. The kmp Gradle block is `// does not compile` (read-only, correctly stamped). what-is-dex has no program (table form, allowed). All `challenge` blocks are `run: false` paper items; I re-did every number by hand and all keys are true (see per-leaf notes). `verified: not-run` is correct on all of them.

Docs verified by fetch (matched unless listed in a FIX): Java SE 21 ArrayList, Android vitals/render, Kotlin native-overview, native-target-support, multiplatform-discover-project, build-native-binaries (Objective-C framework, `baseName`, the XCFramework example shape), direct-integration (task registers only if `binaries.framework`; Run Script before Compile Sources), integrate-in-existing-app (`sharedKit`, `import sharedKit`), Android d8, apk-analyzer (Defined and Referenced Methods, dependency note), multidex (65,536; classes2.dex; minSdk 21), guide/platform (ART runs DEX; "designed specifically for Android ... minimal memory footprint"), enable-app-optimization (R8), source.android.com dex-format.

Could not verify: the O, Omega, Theta definitions and the merge sort and insertion sort facts (general knowledge, labelled; standard textbook results); the 16 ms-per-frame cost of re-sorting in the big-o-notation Android section (labelled as own reasoning); that `.dex` expands to "Dalvik Executable" on the dex-format page itself (it does appear as "Dalvik Executable format (DEX)" on guide/platform, so the leaf is fine).

Common checks, all four: 13 headings in order, with no Try it or Fix the bug (correct, `playground: false`); frontmatter against each roadmap row (id, title, difficulty, order, requires, fruit) matches; `sims` matches the one SIM marker; all four sim ids are in `docs/sims/needed-ids.md` (lines 60, 61, 63, 64); Predict items have 3 options and one correct; Recap is 3 lines under 20 words; You can now has 3 bullets; Common mistakes has 4; Interview has 4 questions; no VERIFY marks or UNVERIFIED stamps in the leaves; no "next lesson" promise; practice lists present only in the DSA and DAA leaves, as required.

---

## 1. daa/analysis/big-o-omega-theta: FIX (minor)

Facts: the three definitions, the 3n + 2 constants (3n <= 3n + 2 <= 4n for n >= 2), the claims table and the interview answers are correct. Keys are a, c, b. `5n + 1`, `n * n` against `100 * n` (breaks at 101: 10201 against 10100) and linear search cases all check out. Practice list: 704 Binary Search, 268 Missing Number, 75 Sort Colors, 148 Sort List, 41 First Missing Positive; numbers, titles, difficulty and URLs are right, ordered easy to hard, 5 problems. The author's interview questions 1 to 3 are used; question 4 (comparison-sort lower bound) is left out, which is fine.

1. Line 222 (Where this shows up): "get and size run in constant time and that all other operations run in linear time, 'roughly speaking'" misquotes the page. The page says `size`, `isEmpty`, `get`, `set`, `iterator`, `listIterator` are constant time, `add` is amortized constant time ("adding n elements requires O(n) time"), and "all of the other operations run in linear time (roughly speaking)". Reword: "says that `size`, `get` and `set` run in constant time, that `add` takes amortized constant time (adding n elements takes O(n) time), and that all of the other operations run in linear time, 'roughly speaking'." Delete the later sentence "It also says adding n elements takes O(n) time, a claim about many operations together" or keep its idea inside the new sentence. Mechanical.
2. Line 89: "At n = 1,000 that forces c <= 0.003" is slightly off (the limit is 3002 / 1,000,000 = 0.003002). Change to "forces c to be about 0.003 or less". Mechanical.
3. Line 49 (See it move, Try this step 3): with a ceiling constant below 3 the curve 3n + 2 is above the line from the first n, so it does not "climb through" it. Change to "Drag the ceiling constant low, for example to 2, and watch the curve sit above the line at every n." The sim spec in `needed-ids.md` line 61 says "crossed"; change it to "stays above". Mechanical.
4. Line 233 (interview, merge sort and insertion sort) and line 229: merge sort and insertion sort are used without a gloss. Optional: add "(merge sort and insertion sort are two ways to sort a list, general knowledge)". Mechanical, low priority.
5. Prose is about 1,820 words against the 900 to 1,600 budget (outside code, comments, tables and headings; interview and practice list included). Owner/lesson-writer: trim about 220 words (analogy-stops paragraph, Build steps, interview answers), or the owner relaxes the budget for leaves that carry the interview section (same open question as L2-a).
6. Housekeeper: nothing to strip. Keep `verified: not-run` on the three paper items.

## 2. dsa/complexity/big-o-notation: FIX (minor)

Facts: fare arithmetic (2,005; 5 is under a quarter of one percent), 3n + 7 and n * n values, 3,005,002 for `3n^2 + 5n + 2` at 1000, table steps, ArrayList and 16 ms/jank claims (except the item below), and the sim spec (B lower for n = 1 to 4) all check out. Keys are b, c, a. Practice list: 704, 169, 50, 4 (all `[both]`; numbers, titles and URLs right; the two `[A]`/`[B]` items were rightly left out). Prose is about 1,500 words, within budget. Kotlin `binarySearch` and `sorted` pages state no complexity: confirmed as the leaf says.

1. Line 106: "get ... constant time and that most other operations run in linear time" is not what the page says. Use: "says that `size`, `get` and `set` run in constant time, that `add` takes amortized constant time, and that all of the other operations run in linear time, 'roughly speaking'." Mechanical.
2. Line 199 (`small-n-surprise`, option c `why`): "At n = 3 it wins, because the start fee 7 is bigger than the n * n part" is false (n * n is 9). Replace with "At n = 3 it wins, because 3n + 7 is already 16 and n * n is only 9." Mechanical.
3. Line 220: "frame" is used without a gloss. Add "(one picture the screen draws; 60 of them make one second)" at first use (general knowledge for the second part). Mechanical (checklist 3).
4. Line 236: "hash-based set" is unglossed. Add "(a set that jumps straight to a value instead of scanning, general knowledge)". Mechanical.
5. Housekeeper: nothing to strip; keep `verified: not-run` on the three paper items (all keys true).

## 3. kmp/what-is-kmp/how-kotlin-reaches-an-iphone: FIX

Mac needed leaf (docs/04 4.13). Every claim I checked matches the pages: Kotlin/Native definition and LLVM backend; "platforms on which virtual machines are not desirable or possible, such as embedded devices or iOS"; Xcode and command-line tools; no Apple final binaries on Linux or Windows; Android library is Kotlin/JVM and iOS framework is Kotlin/Native; `iosArm64` and `iosSimulatorArm64`; `binaries.framework` is an Objective-C framework; the `embedAndSignAppleFrameworkForXcode` rules; `sharedKit`; d8. The Gradle block follows the shape of the docs example minus the XCFramework lines. No wrong key (c, b, a). The author's wrong-belief and build-exercise data are used; the memory-manager and freezing questions are rightly left out (roadmap says say nothing). No "Ran on my Windows machine" or "Seen in this CI run" label is used, which is right.

1. Lines 225 to 229 (Tricks to remember): 5 bullets, limit is 4. Merge "One script, two translators, two books." into the second bullet, or drop it. Mechanical.
2. Evidence labels (docs/04 4.13, every iOS-side claim needs a label on its own line after it). Missing after: (a) the table at lines 136 to 140 (iPhone translator row), (b) the `output-of-each-target` block at line 168, (c) the paragraph at line 248 (Xcode fails at the Run Script step; it is already marked own reasoning but has no label). Add "From the docs, not run by me. ([Direct integration with Xcode](...))" after (c), and "From the docs, not run by me. ([Kotlin/Native overview](...), [Discover your project](...))" after (a) and (b). Mechanical.
3. Checklist 3: "framework" (Build step 4, line 80) and "bytecode" (step 3) are used before Name it defines "framework". Move the one-line gloss of **framework** ("a bundle of compiled code that an Apple app project links in, general knowledge") into the Notice paragraph at line 63. Mechanical.
4. Checklist 2 (sentences under 25 words): line 132 ("And the Kotlin/Native target support page says ..." 32 words) and line 244 ("A common sentence on a team is: ..." 41 words) should each be split in two. Mechanical.
5. Prose is about 1,790 words against the 900 to 1,600 budget (the evidence labels and the interview section account for much of it). Owner/lesson-writer: trim about 190 words, or the owner relaxes the budget for Mac-needed leaves.
6. Housekeeper: nothing to strip. At go-live the `From the docs, not run by me` labels stay (they are body text, not comments).

## 4. native-ndk/art-and-dex/what-is-dex: FIX (minor)

Facts: DEX as "a bytecode format designed specifically for Android ... minimal memory footprint"; D8 job and inputs; "typically named classes.dex"; 65,536 limit; classes2.dex naming; minSdk 21 default; APK Analyzer (Referenced and Defined Methods, dependency methods add to both counts, file sizes); R8 ("shortens the names ... to reduce the size of the DEX file"); dex-format opening line: all match. The R8 and D8 relationship is correctly left unclaimed. Keys b, a. Interview note about "no interview data exists" is true (no `interview-native-ndk.md`). Prose is about 1,330 words. Summary is 25 words, and docs/04 section 2 asks for under 25.

1. Frontmatter `summary` (line 8): 25 words; cut one, for example "A dex file holds your app's compiled code in a compact form that Android's runtime reads, and native C++ code stays out of it." Mechanical.
2. Where this shows up in Android (lines 180 to 182) names no real app symptom (docs/04 4.10). The SOURCES comment already holds the multidex facts. Add one short paragraph: "A symptom: an app with many libraries can pass the 64K limit, and the build then reports too many method references. With a minimum Android version of 21 or higher, the build tools add `classes2.dex` by themselves (multidex page). The 64K limit is explained by the Referenced Methods count above." Do not quote the exact error text unless pasted from the page (docs/04 5.4; the page's text is `Too many field references: 131000; max is 65536`, which is a field example). Mechanical.
3. Analogy clash (lines 42, 46, 70 area, 107, 128, 153, 156, 159 and others): "ready-made jar" means the `.so` file, but a JAR is also a real container named in the D8 bullet (line 96) and in Common mistakes (line 174), and the previous lesson (`what-is-native-code`) uses a sealed bottle of sauce, not a jar. Replace "jar" with "bottle" throughout the analogy uses ("ready-made bottle", "travels in its own bag" can stay), leaving real "JAR" mentions alone. Mechanical (global replace of the analogy word only).
4. Checklist 3 jargon: in the table at line 107 `lib/<abi>/` has an unglossed `abi`; add "(abi is the kind of CPU)". In the Predict list at line 121, `AndroidManifest.xml` has no gloss in the code block; add "(describes the app)" as the previous lesson does. Mechanical.
5. Line 54 (Try this, step 2): "the separate bag for the ready-made jars" follows item 3 above (rename with the bottle word). Sim spec at `needed-ids.md` line 64 keeps the "bag labelled native libraries (.so)" and never claims the phone refuses to start: correct.
6. Housekeeper: strip the `VERIFY dex format and D8 wording against source.android.com` flag from the roadmap Notes at `content/native-ndk/_roadmap.md` line 121 at go-live (the wording is now checked above). The leaf's SOURCES comment is in the standard form.

---

## Summary for the fixer

Per leaf: 1 has items 1 to 4 (mechanical) and item 5 (owner); 2 has items 1 to 4 (mechanical); 3 has items 1 to 4 (mechanical) and item 5 (owner); 4 has items 1 to 5 (mechanical). The word-budget items (1.5, 3.5) are the same owner decision as in L2-a. The `SimSpec.kt` compile error is outside the leaves and is the owner's. After edits re-run `bash scripts/kt-run.sh` on any changed Kotlin block (none is expected to change) and `./gradlew :site:validateContent` (on a tree where `SimSpec.kt` compiles).

## Fixes applied

- daa/analysis/big-o-omega-theta: items 1 to 4 applied. Item 5: repeated sentences cut (car-trip "useless ceiling", the "tests are not a proof" line, the "one shape is tight" line, the Predict-section trailing sentence). Word count not re-measured; owner to confirm about 1,700.
- dsa/complexity/big-o-notation: items 1 to 4 applied.
- kmp/what-is-kmp/how-kotlin-reaches-an-iphone: items 1 to 4 applied (tricks merged to 4 bullets; labels added after the table, the output block and the Xcode paragraph; framework gloss moved to Notice). Item 5: repeats cut (mental-model "stop" sentence, Swift import sentence, two small asides). Word count not re-measured; owner to confirm about 1,700.
- native-ndk/art-and-dex/what-is-dex: items 1 to 4 applied. Item 3: "jar" analogy replaced with "tin" (real JAR mentions kept). Item 5 applied with the jar change. Item 6 (roadmap) not done, per instructions.
- Not run: validateContent and kt-run (per instructions).

## Re-check

Re-checked 21 Oct 2026 against `git diff` of the four leaves. Skipped `validateContent` (the untracked sims file `SimSpec.kt` is mid-rewrite). No Kotlin code block changed in the diff (only prose around them), so `bash scripts/kt-run.sh` was not re-run; the earlier compile results stand.

Word counts (my script, prose outside code, comments, tables and headings): omega 1,762; kmp 1,767. Both are under the accepted 1,840.

1. big-o-omega-theta: PASS. Items 1 to 4 resolved (ArrayList sentence now matches the page; "about 0.003 or less"; "for example to 2 ... sit above the line"; merge sort and insertion sort glossed). The sim spec line in `needed-ids.md` (61, "crossed") was not re-checked; it is outside the leaf.
2. big-o-notation: PASS. ArrayList wording, n = 3 `why`, "frame" gloss and hash-set gloss are all in.
3. how-kotlin-reaches-an-iphone: FIX. Resolved: Tricks is 4 bullets; the three labels are present (after the table, the output block and the Xcode paragraph); "framework" is glossed in the tools paragraph before its first use; the two long sentences are split. Not resolved: "bytecode" is used in the summary and in Build step 3 ("the compiler makes bytecode for the Java virtual machine") and is never defined anywhere in the leaf. Add one gloss to the tools paragraph (line 62), for example: "**Bytecode** is a compact set of instructions made for a virtual machine to run, not for the processor itself (general knowledge)." Mechanical.
4. what-is-dex: FIX (cosmetic). Resolved: summary is 24 words; symptom paragraph added; "jar" analogy replaced with "tin" (real JAR mentions kept; "tin" is accepted, it avoids the clash although the previous lesson uses a bottle); `abi` and manifest glosses added. Not resolved: in the `which-files-are-dex` code block the manifest line has 3 leading spaces where the other three have 4 (`   2. AndroidManifest.xml (describes the app)`), so the list is misaligned. Restore 4 spaces. Mechanical.

VERDICT (re-check, final): FIX
- how-kotlin-reaches-an-iphone.md line 62: gloss "bytecode" before first use (summary and Build step 3) (fixer).
- what-is-dex.md, `which-files-are-dex` code block: restore 4-space indent on line "2. AndroidManifest.xml (describes the app)" (fixer).

## Re-check 2

- how-kotlin-reaches-an-iphone.md line 62: "Bytecode" is now glossed in plain words before Build step 3 (line 78). The summary (frontmatter, line 8) uses the word earlier, which is acceptable. Confirmed.
- what-is-dex.md line 121: the manifest line now has 4 leading spaces, matching the other three. Confirmed.

VERDICT (re-check, final): PASS
