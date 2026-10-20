# Review W4-a: four shadow leaves (check AI code; compiler, interpreter, JVM)

Date 20 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31. Voice: L29. Nothing in the leaves was edited. No `kotlinc` on this machine, so every output and every Predict key was re-traced by hand by me.
Policy applied: a SIM marker with no built sim does not block (`translator-box`, `translation-lanes`, `bytecode-adapters` are in `docs/sims/needed-ids.md` lines 20-22); the line-18 `SOURCES` comment is `housekeeper`'s.
`./gradlew :site:validateContent` run once: `validateContent: ok`, BUILD SUCCESSFUL.

VERDICT: FIX (0 PASS, 4 FIX; every item is mechanical, `fixer` can do it; none needs the owner)

| Leaf | Verdict | Items |
|---|---|---|
| `check-code-you-did-not-write` | FIX | 3 |
| `what-is-a-compiler` | FIX | 2 |
| `compiler-vs-interpreter` | FIX | 5 |
| `what-is-the-jvm` | FIX | 5 |

Checked and OK on all four: frontmatter keys and order; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/foundation/_roadmap.md` lines 141-164); `order` 25 to 28 is free and in sequence; `summary` under 25 words; `sims` equals the SIM marker (`[]` for the debugging leaf, whose row says `none:`); `playground: false`, every Predict `run: false` and `verified: not-run`, no Try it, no Fix the bug; 12-heading order with exact names; The question is 4 or 5 sentences with no code and no bold; Notice, Guess and Build hold no Kotlin syntax; "Where the analogy stops" present (3 to 5 sentences in three leaves; `docs/04` §4.2 says one or two, but every earlier passed leaf does the same, so not a FIX); each Predict has 3 options, one `correct`, a `why` on every option; Tricks 4, Common mistakes 4; Recap is three numbered lines (see sentence items below); prose 1,192 to 1,303 words (budget 900 to 1,600); T4 banned words, `!` in prose, emoji, non-ASCII outside the SOURCES comments, VERIFY, video, capstone, "coming soon": zero hits; no next-lesson promise; no "on the screen" claim; each bold term appears once; every "You can now" line has a matching Predict item; Android sections are reading-only and name a symptom, with own reasoning labelled.

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| "Because AI is an evolving technology, it can generate output that sounds plausible but is factually incorrect. We recommend that you validate all output from Gemini before you use it."; "carefully test all code for errors, bugs, and vulnerabilities before relying on it"; Gemini is built into Android Studio | developer.android.com/studio/gemini/faq | Matches, quotes exact |
| `List.get` throws `IndexOutOfBoundsException` for an index outside 0 until size; `listOf()` returns an empty read-only list | kotlinlang.org/api/core/kotlin-stdlib (W3-a read, SOURCES agree) | Matches |
| "When targeting the JVM, Kotlin produces Java-compatible bytecode."; "When targeting native, Kotlin will produce platform-specific code (via LLVM)."; "Kotlin is 100% compatible with the JVM" | kotlinlang.org/docs/faq.html | Matches, exact. The last one answers "Can I use Kotlin for backend development?", used here in a wider sense; accurate |
| `kotlinc hello.kt -include-runtime -d hello.jar`; "-d ... output path for generated class files"; `java -jar hello.jar`; `-Xrepl` shell: "you can type any valid Kotlin code and see the results"; "A Kotlin script is a Kotlin source file (.kts) with top-level executable code"; `kotlinc -script` is shown | kotlinlang.org/docs/command-line.html | Matches. The page's `hello.kt` prints `Hello, World!`; the leaf's own program prints `Hello` (hand-traced, fine) |
| Kotlin/Native: "compiling Kotlin code to native binaries that can run without a virtual machine" | kotlinlang.org/docs/native-overview.html | Matches |
| `jvmTarget`: "Target version of the generated JVM bytecode" | kotlinlang.org/docs/gradle-compiler-options.html | Matches (JVM attribute table) |
| JVM is "an interpreter that executes Java applications"; "You use the JVM to run the Android Studio IDE and the Gradle build tool"; "The JVM is not used on Android devices or emulators."; "Note that not all functions are available on Android." (this last note is in the JDK libraries bullet) | developer.android.com/build/jdks | Matches. See jvm FIX 5: the note is from this page, not the d8 page |
| `d8` "compile your project's Java bytecode into DEX bytecode that runs on Android devices"; used by Android Studio and the Android Gradle plugin | developer.android.com/tools/d8 | Matches. The page says nothing about unavailable functions |
| "The Android build system compiles app resources and source code and packages them into APKs or Android App Bundles" | developer.android.com/build | Matches |
| "If you receive build-time or compile-time errors, inspect the tree and select an element to read the error output"; the Build Output tab lists the Gradle tasks as a tree and shows errors | developer.android.com/studio/run (section Monitor the build process) | Matches |

Could not verify from an official page (accepted if labelled): that bytecode is "the same on every kind of computer"; that each kind of computer needs its own JVM; what an interpreter is, the compile-first versus interpret table, Python and JavaScript as usually interpreted (labelled general knowledge in the leaf); that the Kotlin compiler does not check list length (own reasoning); an empty-cart crash, an app that opens then closes, and code missing on the phone (own reasoning, labelled). No quoted compiler or Playground message appears in any leaf.

## Hand-trace (mine, independent)

check-code-you-did-not-write: Name it `highest`: [3,9,4] gives best 3, 9, stays 9; [7] gives 7; [-5,-2,-8] gives best -5, then -2; prints 9, 7, -2 (text agrees). `largest-of-three`: `5 > 2` returns 5; key b, output `5`. `total-of-prices`: `1..<3` is i = 1, 2; 2 + 4 = 6; key c; "11" is the full sum, "4" the last price. `highest-of-nothing`: `Start`, then `numbers[0]` on an empty list throws; stdout `Start`; key a.
what-is-a-compiler: Name it prints `Hello`. `typo-in-the-middle` (code as fixed): Pack, Send, Done; key c; the typo is on line 3 counting from `fun main`. `index-passes-the-checks`: Start, then `names[2]` throws on a 2-item list; stdout `Start`; key b.
compiler-vs-interpreter: `live-lane-stops-at-three`: Pack, Send, then stops at sentence 3; key b. `translation-work-count`: 5 and 5 * 3 = 15, so `Print shop: 5` then `Live speaker: 15`; key a; the program compiles.
what-is-the-jvm: Name it prints `Hello`. `adapter-removed`: Laptop, Server; key a. `builds-per-machine`: 3 * 4 = 12 and 4; `One build per machine: 12`, `One shared build: 4`; key c; option b is 3 + 4. `which-is-the-language`: Kotlin and Java, so 2; key b.
Correct-option letters vary: b, c, a / c, b / b, a / a, c, b.

## Leaf 1: check-code-you-did-not-write: FIX

1. File `check-code-you-did-not-write.md`, line 123 (Predict `largest-of-three`, option c `why`). The explanation does not lead to the answer "2": starting at the middle check, `2 > 9` is false and the code returns `c`, which is 9. Old: `You may have started at the middle check. The checks run from the top, and the first one already hands back an answer.` New: `You may have picked the middle number. The function never hands back b here. The first check is a > b, and it hands back a.` (mechanical)
2. File `check-code-you-did-not-write.md`, line 68 (Name it). `type` is a new term, defined but not bold (T2), and the sentence is 24 words with two ideas (T7). Old: `` `List<Int>` is the type, meaning the kind of value, for a list of whole numbers, and `numbers[0]` is the first item of the list. `` New: `` `List<Int>` is the **type** for a list of whole numbers. A type is the kind of value. `numbers[0]` is the first item of the list. `` (mechanical)
3. File `check-code-you-did-not-write.md`, line 91 (Name it). `The person who can check the work is the person who stays in the job.` is an unsupported claim and a slogan (T6), and it may scare a learner with low confidence (T8). Old: `Checking is not an insult to the writer. People and tools both slip, and the owner is the one who signs. The person who can check the work is the person who stays in the job.` New: `Checking is not an insult to the writer. People and tools both slip, and the owner is the one who signs.` (mechanical)

After this, PASS. Optional (not FIX): line 60, "trust your hand answer first" could mislead when the hand answer is the slip; consider `redo your hand answer once, then find the line where they split`.

## Leaf 2: what-is-a-compiler: FIX

1. File `what-is-a-compiler.md`, line 77 (Name it). "Bytecode ... is the same on every kind of computer" and "a program that runs bytecode" are not in the official pages as worded and carry no label (needed for compiler and JVM claims). Old: `Bytecode is an in-between form that is the same on every kind of computer, so it is not yet the chip's own steps.` New: `Bytecode is an in-between form that is the same on every kind of computer, so it is not yet the chip's own steps. That description is general knowledge, not from the Kotlin documentation.` (mechanical)
2. File `what-is-a-compiler.md`, line 77 (Name it). "LLVM" is jargon inside a quote and is never defined. Old: `The documentation says: "When targeting native, Kotlin will produce platform-specific code (via LLVM)." That means steps for one kind of chip.` New: `The documentation says that when targeting native, Kotlin produces platform-specific code. That means steps for one kind of chip.` (mechanical)
3. File `what-is-a-compiler.md`, line 172 (Recap, item 2). Two clauses joined by a semicolon; the Recap must be one sentence per line. Old: `If one rule breaks, nothing runs; if all pass, it translates your code into steps.` New: `If one rule breaks, the compiler stops and nothing runs.` and in line 171 Old: `A compiler reads all your source code first and checks every rule.` New: `A compiler reads all your source code first, checks every rule, then translates it.` (mechanical)

After this, PASS.

## Leaf 3: compiler-vs-interpreter: FIX

1. File `compiler-vs-interpreter.md`, line 21 (The question). "your mistakes usually show up before anything prints" is too wide: a run-time stop (the previous leaf, Predict 2) shows up after printing. Old: `In Kotlin, your mistakes usually show up before anything prints.` New: `In Kotlin, a typo usually shows up before anything prints.` (mechanical)
2. File `compiler-vs-interpreter.md`, line 72 (Name it). The plain definitions, the table and "real tools often mix them" have no label (needed for compiler and interpreter claims; only Python and JavaScript are labelled). Old: `Here are the plain versions of both ideas. Real tools often mix them.` New: `These plain versions of both ideas, the table below and the point that real tools often mix them are general programming knowledge, not from the Kotlin documentation.` (mechanical)
3. File `compiler-vs-interpreter.md`, line 128 (Predict `translation-work-count`, code comment). The comment explains `$sentences` but `${sentences * timesHeard}` is not explained; the template was taught only in `find-a-bug-without-guessing`. Old: `    // $sentences shows the value of sentences.` New: `    // $name shows a value. ${...} shows the result of a calculation.` (mechanical)
4. File `compiler-vs-interpreter.md`, line 166 (Where this shows up in Android). "So one workflow holds both ideas, a compiler to translate and an interpreter to run" reads as if the JVM interpreter runs the app. The cited page says the JVM runs Android Studio and Gradle, and says "The JVM is not used on Android devices or emulators." Old: `So one workflow holds both ideas, a compiler to translate and an interpreter to run. Matching the two symptoms to the two stages is our own reasoning.` New: `So in one workflow, the build compiles your code, and the JVM, an interpreter, is what runs the build tools. Matching the two symptoms to the two stages (compile, then run) is our own reasoning.` Also line 164: Old: `The first is the compile stage, and the second is the app running.` New: `The first is the compile stage, and the second is the app running after it was built.` (mechanical)
5. File `compiler-vs-interpreter.md`, lines 170-171 (Recap, items 1 and 2). Two clauses joined by a semicolon in each line; Recap must be one sentence per line. Old 1: `A compiler translates everything before running; an interpreter translates and runs step by step.` New 1: `A compiler translates everything first, and an interpreter translates as it runs.` Old 2: `With compile first, a typo stops everything; with an interpreter, earlier lines can run first.` New 2: `A typo stops everything with compile first, but earlier lines still run step by step.` (mechanical)

After this, PASS.

## Leaf 4: what-is-the-jvm: FIX

1. File `what-is-the-jvm.md`, line 73 (Name it). (a) "An interpreter" is defined but not bold (T2). (b) "Java Development Kit" is jargon, never defined, and not used again. Old: `An interpreter is a program that carries out each step as it reads it. The documentation also says the Java Development Kit contains the JVM.` New: `An **interpreter** is a program that carries out each step as it reads it.` (mechanical)
2. File `what-is-the-jvm.md`, line 75 (Name it). "That part" does not say which sentences are general knowledge. Old: `That part is general knowledge, not spelled out in the Kotlin documentation.` New: `These four sentences are general knowledge, not spelled out in the Kotlin documentation.` (mechanical)
3. File `what-is-the-jvm.md`, line 77 (Name it). "LLVM" is jargon inside a quote and is never defined. Old: `The Kotlin documentation says: "When targeting native, Kotlin will produce platform-specific code (via LLVM)." Its native page says this code can run without a virtual machine,` New: `The Kotlin documentation says that when targeting native, Kotlin produces platform-specific code. Its native page says this code can run without a virtual machine,` (mechanical)
4. File `what-is-the-jvm.md`, line 132 (Predict `builds-per-machine`, code). `${...}` and `$updates` are used with no explanation. Add a comment line after `val updates = 4`. New line: `    // $name shows a value. ${...} shows the result of a calculation.` The code stays under 12 lines. (mechanical)
5. File `what-is-the-jvm.md`, line 197 (Where this shows up in Android). Wrong source: "That documentation also notes" follows the d8 sentence, but the "not all functions are available on Android" note is on the JDK page (`build/jdks`), not the d8 page. Old: `That documentation also notes that not all functions from the Java libraries are available on Android.` New: `The page that describes the JVM also notes that not all functions in the Java libraries are available on Android.` (mechanical)

After this, PASS.

## Cross-leaf notes (not FIX)

- The `kotlin` blocks here: Name it in the debugging leaf, compiler and JVM leaves compile as written (complete `fun main()`); the compiler Predict `typo-in-the-middle` and `index-passes-the-checks` are hand-traced paper items. Nothing was run; none of the answers depend on a compiler message.
- Roadmap row for `compiler-vs-interpreter` says "reuse `what-is-a-compiler` sim"; the leaf uses a new id `translation-lanes`, recorded in `needed-ids.md` line 21 as extending `translator-box`. Same pattern as the W3-b ruling (rule 5 protects ids, titles, difficulty, requires, fruit, which all match). No FIX; `curriculum-designer` may align the row.
- Roadmap Challenges `F:` / `F(ai):` tasks for these leaves are not delivered as Fix the bug because `playground` is false; accepted W3 pattern (the debugging leaf delivers the `F(ai):` ideas as Predict items).
- `housekeeper` at go-live: strip the line-18 SOURCES comments; keep the SIM markers.
- Do not paste real compiler messages later (`docs/04` §5.4): none are quoted now.

## Re-check (orchestrator, 21 Oct 2026)
All FIXes applied (see W4-b.md Re-check): PASS.
