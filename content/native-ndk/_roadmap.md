```yaml
branch: native-ndk
bough: craft
stems:
  - id: what-is-native
    title: What Is Native Code
    why: A fresher meets .so files, C++ folders and "JNI" in real projects and must know what they are before touching them.
    leaves:
      - id: what-is-native-code
        title: "What is native code inside an Android app?"
        difficulty: easy
        requires: []
        fruit: false
  - id: art-and-dex
    title: ART and Dex
    why: Knowing how Kotlin code is packed and run on a phone explains why native code is a different kind of passenger.
    leaves:
      - id: what-is-dex
        title: "What is inside classes.dex?"
        difficulty: easy
        requires: [native-ndk/what-is-native/what-is-native-code]
        fruit: false
      - id: how-art-runs-code
        title: "How does ART turn my app's code into fast machine code over time?"
        difficulty: medium
        requires: [native-ndk/art-and-dex/what-is-dex]
        fruit: false
  - id: jni-basics
    title: JNI Basics
    why: JNI is the doorway between Kotlin and C++, and generated JNI code fails in quiet, expensive ways.
    leaves:
      - id: kotlin-calls-cpp
        title: "How does a Kotlin function call a function written in C++?"
        difficulty: medium
        requires: [native-ndk/what-is-native/what-is-native-code, native-ndk/art-and-dex/how-art-runs-code]
        fruit: false
      - id: jni-types-and-strings
        title: "Why can't I pass a Kotlin String to C++ like a normal argument?"
        difficulty: medium
        requires: [native-ndk/jni-basics/kotlin-calls-cpp]
        fruit: false
      - id: jni-references
        title: "Why does C++ code that holds on to a Kotlin object leak or crash later?"
        difficulty: hard
        requires: [native-ndk/jni-basics/jni-types-and-strings]
        fruit: false
  - id: ndk-build
    title: Building Native Code
    why: Native code only reaches the phone if Gradle, CMake and the CPU types line up.
    leaves:
      - id: add-cpp-to-a-project
        title: "How do I add a C++ file to my app and get Gradle to build it?"
        difficulty: medium
        requires: [native-ndk/jni-basics/kotlin-calls-cpp]
        fruit: false
      - id: abis-and-so-files
        title: "Why does my app contain a separate .so file for each kind of CPU?"
        difficulty: easy
        requires: [native-ndk/ndk-build/add-cpp-to-a-project]
        fruit: false
  - id: native-memory
    title: Native Memory
    why: Native code has no garbage collector, so one forgotten free or one wrong page size can leak memory or block a Play release.
    leaves:
      - id: native-memory-has-no-gc
        title: "Who frees memory that C++ code allocates?"
        difficulty: medium
        requires: [native-ndk/jni-basics/jni-references]
        fruit: false
      - id: memory-page-sizes
        title: "Why must my native libraries support 16 KB memory pages?"
        difficulty: hard
        requires: [native-ndk/ndk-build/abis-and-so-files, native-ndk/native-memory/native-memory-has-no-gc]
        fruit: false
  - id: when-to-go-native
    title: When to Go Native
    why: Most apps never need C++; a fresher must be able to say no with a reason.
    leaves:
      - id: should-i-go-native
        title: "Should this feature be Kotlin, a ready-made library, or my own C++?"
        difficulty: medium
        requires: [native-ndk/native-memory/memory-page-sizes]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Every stem is phase 6. Every `Assumes` target below sits in phase 5 or lower, or in `tools` (earlier than `native-ndk` in the `docs/08` §10.1 tie-break). This branch is at overview level: a fresher learns to read and review native code and to decide whether to use it, not to write a game engine. There is no Android emulator or simulator in Kotree (L15), so every Android-behaviour sim is `code-to-screen` or a plain animation.

- Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu` (phase 1) — used by what-is-native-code and abis-and-so-files.
- Assumes Foundation: `foundation/from-code-to-running/what-is-a-compiler` and `foundation/from-code-to-running/what-is-the-jvm` (phase 1) — used by what-is-native-code and what-is-dex.
- Assumes Foundation: `foundation/from-code-to-running/what-is-art` and `foundation/how-an-app-is-born/what-is-an-app-file` (phase 1) — used by what-is-dex and how-art-runs-code; this branch goes one step deeper, still at overview level.
- Assumes Foundation: `foundation/how-an-app-is-born/what-happens-when-i-build` (phase 1) — used by add-cpp-to-a-project.
- Assumes Kotlin: `kotlin/functions/declaring-functions` and `kotlin/strings/strings-are-sequences` (phase 2) — used by kotlin-calls-cpp and jni-types-and-strings.
- Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references` (phase 2) — used by jni-references.
- Assumes Tools and ecosystem: `tools/gradle-basics/what-gradle-does` (phase 5) — used by add-cpp-to-a-project.
- Assumes Tools and ecosystem: `tools/publishing/release-builds-and-signing` (phase 6, earlier branch in the tie-break) — used by memory-page-sizes (what an app bundle holds).
- Helps to know OS: `os/memory/stack-and-heap` and `os/memory/what-keeps-an-object-alive` (phase 3) — native-memory-has-no-gc recaps stack and heap in one paragraph.
- Helps to know Profiling: `profiling/memory-and-leaks/find-a-leak` — native-memory-has-no-gc names the tool only.
- Helps to know Security: `security/threat-thinking/who-would-attack-my-app` — native-memory-has-no-gc names buffer overflows as a security risk in one sentence.
- Helps to know Android core: `android-core/what-is-an-app/what-is-inside-an-app` — the lessons show where the native library sits next to the screen code.

Challenge note: neither C++ nor JNI loads in the Kotlin Playground. Every `P:` and `F:` on native code is either a read-only "which line is wrong" task on a short C++ or Kotlin snippet, or a plain-Kotlin model of the rule (for example a map of declared native names versus registered functions that throws if a name is missing). ⚠️ VERIFY with `docs/06` once written.

## Stems

### what-is-native · What Is Native Code

Flags: phase=6

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-native-code` | easy | "What is a .so file in my app?" | A kitchen that buys a ready-made sauce from a specialist factory: the sauce is already cooked for one stove type, while your own recipe cards are cooked fresh in your kitchen. | "Native code is always faster than Kotlin, so serious apps should use it." | none: a labelled diagram of an app package with a code box and a library box is enough; nothing moves | `P:` on a diagram: which of five files in an app package are native · `B:` remove the library box and say which feature stops | Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`, `foundation/from-code-to-running/what-is-a-compiler`, `foundation/from-code-to-running/what-is-the-jvm`. Names C, C++ and Rust only as languages that compile to native code (Rust named once, not taught). Checked 18 Oct 2026 (developer.android.com/ndk/guides): the NDK lets you use C and C++ in Android apps; use it for low latency or heavy compute, or to reuse existing C/C++ libraries. ⚠️ VERIFY again before live. |

### art-and-dex · ART and Dex

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-dex` | easy | "What is the file that holds my Kotlin code inside an app?" | A phone-sized travel edition of a cookbook: the same recipes, packed tight so they fit a small bag. | "My app contains my .kt files." | sim: an animation of class files being squeezed by a packer into one small dex box inside the app package; the learner removes the box and the phone has nothing to run | `P:` on the sim: which part of the package holds Kotlin code, and which holds C++ code · `B:` count the boxes in a package that has one native library | Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file`. Overview only: D8, R8 and multidex are named once. ⚠️ VERIFY dex format and D8 wording against source.android.com. |
| `how-art-runs-code` | medium | "Why is my app slower the first time I run it after install than after a day of use?" | A new waiter who reads each order card slowly at first, then memorises the popular dishes and stops reading the card. | "The phone translates the whole app into machine code at install time." | sim: a recipe card is read line by line at first; a counter marks lines used often; hot lines are copied onto a fast card and the speed meter rises; a toggle shows "translate ahead of time" versus "while running" | `P:` on the sim: which lines are on the fast card after 1,000 uses · `B:` turn off profile-guided translation and compare the first-start speed | volatile. Assumes Foundation: `foundation/from-code-to-running/what-is-art`. Checked 18 Oct 2026 (via `content/foundation/_roadmap.md`, source.android.com "Configure ART"): since Android 7, ART mixes ahead-of-time compilation, just-in-time compilation and interpretation, guided by profiles. ⚠️ VERIFY again before live; Baseline Profiles belong to Profiling. Native code is already machine code, so ART does not translate it. |

### jni-basics · JNI Basics

Flags: phase=6 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `kotlin-calls-cpp` | medium | "Why does my app crash with UnsatisfiedLinkError when the Kotlin and C++ names look right?" | A phone book with a strict spelling rule: a call goes through only if the name in the book matches the name you dial, letter for letter. | "If both functions exist, Kotlin finds the C++ one by itself." | sim: a Kotlin call ball rolls to a switchboard that looks up its exact name; a misspelt package part makes the ball fall off the board with the error name | `P:` which of three C++ function names matches a given Kotlin class and method · `F(ai):` a generated C++ function whose name still uses the old package name · `B:` forget loadLibrary and read the error | Assumes Kotlin: `kotlin/functions/declaring-functions`. Checked 18 Oct 2026 (developer.android.com/training/articles/perf-jni): load with `System.loadLibrary` from a static initialiser; `RegisterNatives` from `JNI_OnLoad` catches mistakes at load time; name-based lookup shows signature errors only on first call. ⚠️ VERIFY again before live. |
| `jni-types-and-strings` | medium | "Why does C++ need extra steps just to read a Kotlin String, and what must it give back?" | Borrowing a book from a library: you must return it by the due date, or nobody else can borrow it. | "A Kotlin String and a C++ string are the same thing." | none: a conversion table and a short code reading are clearer than motion | `P:` which call pairs with `GetStringUTFChars` · `F(ai):` a tidy native function that reads a string and never releases it · `F:` a jstring used after its function returned | Assumes Kotlin: `kotlin/strings/strings-are-sequences`. Checked 18 Oct 2026 (JNI tips): always release what you get; data given to `NewStringUTF` must be valid Modified UTF-8. ⚠️ VERIFY again before live. |
| `jni-references` | hard | "When does C++ keep a Kotlin object alive, and who lets it go?" | A coat-check: while the desk holds your ticket the coat cannot be given away, and a ticket valid only for one visit cannot be used on the next. | "If C++ holds a variable pointing at a Kotlin object, it stays valid like a normal variable." | sim: Kotlin objects float in a pool watched by a collector; a local rope (valid for one call) is cut when the native call returns; a global rope stays until cut by hand, so objects pile up | `F(ai):` a confident function that stores a local reference in a static variable · `F(ai):` a loop that makes thousands of local references without deleting them · `F(ai):` a global reference that is never deleted · `P:` which reference is still valid after the native function returns | Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references`. Checked 18 Oct 2026 (JNI tips): local references are valid only for the current native call and thread; keep an object longer with `NewGlobalRef` and release it with `DeleteGlobalRef`; compare with `IsSameObject`, not `==`; only 16 local slots are guaranteed, but Android 8.0+ allows unlimited local references; threads attached from native code must detach. Wording "leaked local reference" in `docs/08` §5 is narrowed here to loops and attached threads. ⚠️ VERIFY again before live. |

### ndk-build · Building Native Code

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `add-cpp-to-a-project` | medium | "Which three files must agree before my C++ code runs in my app?" | A relay race: the build file hands the baton to the C++ build script, which hands it to the library, which Kotlin finally loads. | "I just drop a .cpp file in the project and it runs." | sim: code-to-screen a build chain of three boxes (Gradle, CMake script, library) with a switch on each; breaking one box shows which error screen the learner gets | `P:` given a CMake script, what is the library called · `F(ai):` a generated CMakeLists.txt whose library name differs from the name Kotlin loads · `F:` a Gradle file that never points at the CMake script | volatile. Assumes Tools and ecosystem: `tools/gradle-basics/what-gradle-does`. Checked 18 Oct 2026 (developer.android.com/ndk/guides): CMake is Android Studio's default and recommended for new projects; ndk-build is supported for existing ones; the workflow is native sources, a CMake script, link Gradle to it, build. ⚠️ VERIFY: the current Android Studio "Native C++" template name and the Gradle `externalNativeBuild` block. |
| `abis-and-so-files` | easy | "Why does my app have a .so for arm64-v8a and another for x86_64?" | Shoes made in several sizes: the shop stocks every size, but each foot only ever uses one pair. | "One native library works on every phone." | none: a folder diagram and a size table are enough | `P:` on a folder diagram: which library does a 64-bit ARM phone load · `F:` an app with only an x86_64 library and a phone that is arm64-v8a · `B:` add a fourth CPU type and note the size change | Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`. ⚠️ VERIFY: current ABI list and the ABIs Google Play expects. |

### native-memory · Native Memory

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `native-memory-has-no-gc` | medium | "If my C++ code asks for memory and forgets to give it back, who cleans up?" | Renting storage lockers with no caretaker: nobody empties a locker for you, and a locker you forget keeps charging. | "Memory is freed automatically in every language the app uses." | sim: two shelves, a Kotlin shelf with a sweeper and a C++ shelf with none; boxes pile up on the C++ shelf until the room is full and the app is killed | `P:` how many boxes remain on each shelf after 5 calls · `F(ai):` a native function that allocates a buffer per call and never frees it · `F:` memory freed twice | Helps to know OS: `os/memory/stack-and-heap`. Helps to know Profiling: `profiling/memory-and-leaks/find-a-leak`. Helps to know Security: `security/threat-thinking/who-would-attack-my-app`. ⚠️ VERIFY: current Android guidance on native memory use and leak tools (developer.android.com NDK pages). |
| `memory-page-sizes` | hard | "Why might Google Play refuse my update because of one native library?" | A warehouse that moves goods only on whole pallets: if your goods sit between pallet edges, the forklift cannot lift them. | "Page size is the phone maker's problem, not mine." | sim: code-to-screen the library file is cut into pages; with 4 KB marks the slices fit a 4 KB device, but on a 16 KB device they straddle boundaries and the app fails to open until the library is realigned | `P:` which of three libraries is 16 KB aligned (read-only check output) · `F:` code that hard-codes 4096 as the page size · `B:` rebuild the sim library with the 16 KB flag | volatile. Checked 18 Oct 2026 (developer.android.com/guide/practices/page-sizes): apps targeting Android 15 (API 35) and higher must support 16 KB page sizes on 64-bit devices; from 1 February 2027 updates that do not support it cannot be released; NDK r28+ aligns by default, NDK r27 and lower need linker flags; AGP 8.5.1+ with uncompressed libraries; check with `adb shell getconf PAGE_SIZE`. ⚠️ VERIFY again before live; dates and NDK versions change. |

### when-to-go-native · When to Go Native

Flags: phase=6

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `should-i-go-native` | medium | "Do I need C++ for this feature, or is Kotlin or a library enough?" | Buying a ready-made cupboard, building one from a kit, or cutting your own wood: pick the cheapest that still fits. | "Writing the hot part in C++ always makes the app faster." | none: a decision chart with four questions; practise with `P:` on the chart | `P:` match five features (image filter, login form, audio effect, list screen, video codec) to Kotlin, library or C++ · `F(ai):` a generated plan that rewrites a network call in C++ for speed · `B:` add a one-line cost note for each choice | Synthesis leaf. Fruit (see `## Fruits`). Helps to know Profiling: `profiling/cpu/find-the-hot-function`. |

## Fruits

| Leaf | Capstone | Constraints | Proves |
|---|---|---|---|
| `native-ndk/when-to-go-native/should-i-go-native` | "Hello from C++, and a note on whether it was worth it" | One app that calls one C++ function and shows its result on screen · the function takes a string and gives a string back, with every `Get` paired with a `Release` · state the library name, the ABIs you ship and your 16 KB page-size check · write a half-page note comparing it to a Kotlin version, naming one cost of going native · no code copied that you cannot explain line by line | Reads and decides about native code as one part of the whole-tree check in `docs/01` §6.2 (L24, "what could make it slow"). ⚠️ VERIFY: `docs/01` §6.2 has no `native-ndk` ability row; the owner must add one or confirm this wording. |

## Later

- CMake in depth, because a first project needs only the template's script.
- Native crash symbolication, because it needs a crash-reporting setup the tree covers elsewhere.
- Vulkan and game graphics, a large separate field beyond a first full app.
- SIMD and hand-tuned CPU instructions, which only specialists need.
- JNI exceptions and calling Kotlin back from C++ in depth, useful once a team owns real native code.
- Rust and other languages on Android, named once in the first leaf only.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 6 stems, 11 leaves, all shadow. | +11 | Est. 10, band 8-12: inside the band. Sketch had native-memory 1; split into native-memory-has-no-gc and memory-page-sizes because the Play 16 KB page-size rule is a separate, volatile idea (split test). |
| 2026-10-18 | A2e review fixes: reworded `what-is-native-code` and `what-is-dex` to one idea each (slugs unchanged); "why an app ever contains native code" and "why dex is not a JVM file" move into the lesson bodies. One `⚠️ VERIFY` added to `native-memory-has-no-gc`. | 0 | No leaf added or removed. A split was not used: native-ndk is 1 below its band top. |
| 2026-10-18 | Sim share note (A2e review): 7 of 11 leaves have a `sim:` cell (64 %), above the 15–25 % guide in `docs/08` §6. Kept because memory, references and the build pipeline are hidden-behaviour topics; the branch is small, so the workload for `docs/05` is 7 sims. | 0 | Logged, no leaf cut. |
