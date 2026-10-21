```yaml
branch: profiling
bough: craft
stems:
  - id: measure-first
    title: "Measure First"
    why: "A fresher who guesses which code is slow fixes the wrong thing, so the branch starts with measuring."
    leaves:
      - id: measure-before-you-fix
        title: "Why must I measure before I try to make my app faster?"
        difficulty: easy
        requires: []
        fruit: false
  - id: profiler-tour
    title: "Profiler Tour"
    why: "Every later leaf reads a timeline or a chart, so a fresher must know what a profiler screen shows and what it cannot show."
    leaves:
      - id: read-a-profiler-screen
        title: "What do the timeline and the charts on a profiler show me?"
        difficulty: medium
        requires: [profiling/measure-first/measure-before-you-fix]
        fruit: false
  - id: cpu
    title: "CPU"
    why: "A slow function or a job on the wrong thread is the commonest cause of a slow app, and a chart shows it in seconds."
    leaves:
      - id: find-the-hot-function
        title: "How do I find the function that uses the most CPU time?"
        difficulty: medium
        requires: [profiling/profiler-tour/read-a-profiler-screen]
        fruit: false
      - id: catch-main-thread-work
        title: "How do I find work that runs on the main thread and should not?"
        difficulty: medium
        requires: [profiling/cpu/find-the-hot-function]
        fruit: false
  - id: memory-and-leaks
    title: "Memory and Leaks"
    why: "Memory that is never freed makes an app slow, then kills it, and the cause is almost never visible in the code."
    leaves:
      - id: read-memory-use
        title: "How do I see what is using my app's memory?"
        difficulty: medium
        requires: [profiling/profiler-tour/read-a-profiler-screen]
        fruit: false
      - id: find-a-leak
        title: "How do I find what is keeping an old screen in memory?"
        difficulty: hard
        requires: [profiling/memory-and-leaks/read-memory-use]
        fruit: false
  - id: jank-and-frames
    title: "Jank and Frames"
    why: "A stutter is a missed frame, and a fresher needs the frame budget and a trace to see which work missed it."
    leaves:
      - id: frame-budget
        title: "How long does one frame get before the screen stutters?"
        difficulty: easy
        requires: [profiling/measure-first/measure-before-you-fix]
        fruit: false
      - id: find-the-janky-frame
        title: "How do I find a slow frame and see what it was doing?"
        difficulty: hard
        requires: [profiling/jank-and-frames/frame-budget, profiling/profiler-tour/read-a-profiler-screen]
        fruit: false
  - id: compose-performance
    title: "Compose Performance"
    why: "A Compose screen can redraw far more than needed, and generated Compose code often does."
    leaves:
      - id: see-needless-recomposition
        title: "How do I see which composables redraw more often than they should?"
        difficulty: medium
        requires: [profiling/jank-and-frames/frame-budget]
        fruit: false
      - id: fix-slow-compose-code
        title: "Which small change stops a screen from redrawing on every frame?"
        difficulty: medium
        requires: [profiling/compose-performance/see-needless-recomposition]
        fruit: false
  - id: startup
    title: "Startup"
    why: "The first second decides if users stay, and generated apps often do heavy work before the first screen."
    leaves:
      - id: measure-startup
        title: "How do I measure how long my app takes to start?"
        difficulty: medium
        requires: [profiling/profiler-tour/read-a-profiler-screen]
        fruit: false
      - id: speed-up-startup
        title: "What work can I remove or delay so the first screen shows sooner?"
        difficulty: medium
        requires: [profiling/startup/measure-startup, profiling/cpu/catch-main-thread-work]
        fruit: false
      - id: baseline-profiles
        title: "What does a baseline profile do for a user's first launch?"
        difficulty: medium
        requires: [profiling/startup/measure-startup]
        fruit: false
  - id: battery-and-network
    title: "Battery and Network"
    why: "An app can feel fast and still drain a phone, and users uninstall apps that do."
    leaves:
      - id: find-battery-and-network-drain
        title: "How do I find what is draining the battery or using data?"
        difficulty: medium
        requires: [profiling/profiler-tour/read-a-profiler-screen]
        fruit: false
  - id: app-size
    title: "App Size"
    why: "A bigger download means fewer installs, and a fresher should know the few switches that shrink it."
    leaves:
      - id: shrink-the-app
        title: "What makes my app download smaller?"
        difficulty: easy
        requires: [profiling/measure-first/measure-before-you-fix]
        fruit: false
  - id: putting-it-together
    title: "Putting It Together"
    why: "The checkpoint ability is naming the tool and the first thing to look at, so the branch ends on a full hunt for the slowest part."
    leaves:
      - id: find-the-slowest-part
        title: "How do I find the slowest part of a slow app and prove my fix helped?"
        difficulty: hard
        requires: [profiling/cpu/catch-main-thread-work, profiling/jank-and-frames/find-the-janky-frame, profiling/startup/speed-up-startup]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. The branch is phase 5 and comes after `testing` in the tie-break order. The author has no emulator on the site (L15, D8): profiler results are shown through reference videos and recorded charts played as `animation` sims (`docs/05` §4.2), in which the learner switches between recorded runs of pre-written code variants and watches a timeline change. This branch uses `animation`, not `code-to-screen` (author decision 18 Oct 2026). Nothing in this branch runs a profiler in the browser.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing` (phase 1) — used by measure-before-you-fix and find-the-slowest-part.
- Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu` and `foundation/how-computers-work/memory-vs-storage` (phase 1) — used by the cpu and memory-and-leaks stems.
- Assumes Foundation: `foundation/from-code-to-running/what-is-art` (phase 1) — used by baseline-profiles (code is compiled before it runs).
- Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file` (phase 1) — used by shrink-the-app.
- Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references` and `kotlin/lambdas/closures` (phase 2) — used by find-a-leak (a reference or a captured variable keeps an object alive).
- Assumes DSA: `dsa/complexity/reading-complexity-in-code` (phase 2) — used by find-the-hot-function (a function can be slow because it is called too often or because its work grows with the input).
- Assumes Tools: `tools/gradle-basics/what-gradle-does` (phase 5, earlier in the tie-break) — release versus debug build types and where the shrink switches live.
- Assumes Tools: `tools/adb-and-logcat/adb-basics` (phase 5, earlier in the tie-break) — used by measure-startup and find-battery-and-network-drain (the commands that start a trace).
- Assumes Android core: `android-core/lifecycle/leaking-an-activity` (phase 3) — used by find-a-leak (that leaf teaches why; this branch teaches finding it).
- Assumes Android core: `android-core/app-startup/cold-warm-hot-start` and `android-core/app-startup/what-is-the-application-class` (phase 5, Android core is earlier in the tie-break) — used by measure-startup and speed-up-startup.
- Assumes Android core: `android-core/app-startup/from-tap-to-first-frame` (phase 5) — used by speed-up-startup (the timeline of steps that can be delayed).
- Assumes Android core: `android-core/background-work/why-background-is-limited` (phase 5) — used by find-battery-and-network-drain.
- Assumes Concurrency: `concurrency/why-the-main-thread/what-the-main-thread-does`, `concurrency/why-the-main-thread/why-blocking-freezes-the-screen` and `concurrency/dispatchers/main-safe-functions` (phase 4) — used by catch-main-thread-work and frame-budget.
- Assumes UI: `ui/state-and-recomposition/what-is-recomposition` and `ui/state-and-recomposition/remember` (phase 3) — used by see-needless-recomposition and fix-slow-compose-code.
- Assumes UI: `ui/lists/lazy-column` and `ui/lists/list-keys` (phase 3) — used by fix-slow-compose-code (stable keys).
- Assumes Networking inside apps: `app-networking/caching/reusing-saved-answers` (phase 4) — used by find-battery-and-network-drain (the cure for repeated requests).
- Assumes Testing: `testing/device-tests/local-vs-device-tests` (phase 5, Testing is earlier in the tie-break) — used by measure-startup (a Macrobenchmark runs as a test on a device, so a fresher must know what a device test is).
- Helps to know OS: `os/memory/stack-and-heap` and `os/scheduling/one-cpu-many-apps` — the memory and CPU leaves recap them in one paragraph.
- Helps to know OS: `os/android-os/low-memory-killer` (phase 6) — the low-memory killer; `read-memory-use` names it without relying on it.
- Helps to know Architecture and data: `architecture-data/viewmodel/viewmodel-lifetime` — find-a-leak recaps what should outlive a screen.
- Helps to know Computer networking: `networking/mobile-networks-and-latency/flaky-mobile-networks` — find-battery-and-network-drain recaps why radio use costs battery.
- Helps to know DAA: `daa` roadmap mentions frame-time percentiles in its median leaf; `frame-budget` here uses only the plain idea "the slowest frames matter most" and needs no DAA leaf. No dependency.
- Helps to know Native layer: `native-ndk/native-memory/native-memory-has-no-gc` (phase 6) — "native allocations" are named in `read-memory-use`, not taught.

Foundation is recommended first ("Start here"), then Kotlin, Android core, UI, Concurrency and Architecture and data. `android-core/_roadmap.md` cites `profiling/memory-and-leaks/find-a-leak` and `profiling/startup/measure-startup` as `Helps to know`; those ids are kept exactly. `ui/_roadmap.md` hands "Compose performance and stability" to this branch, which is the stem `compose-performance`.

Challenge note: profiler output cannot be produced in the browser. Every `P:` is a question about a recorded chart, trace or timeline shown as an image or animation, or about a plain-Kotlin model (a loop that counts "frames" and "milliseconds"). Every `F:` is a plain-Kotlin fix of a slow function that the Playground can run and time with counts, not wall-clock milliseconds, or a read-only "which line is the cause" task. ⚠️ VERIFY on kotlinlang.org that Playground timings are too noisy for challenge answers; use counts.

Facts checked against official docs on 18 Oct 2026, to be rechecked by `reviewer` before each leaf goes live:

- Profiler (developer.android.com/studio/profile): the page lists these profiler tasks: record a system trace, sample the callstack, record Java/Kotlin methods, record Java/Kotlin allocations, capture a heap dump, record native allocations, inspect your app live, inspect pre-built APKs. The chart terms are call chart, events table, flame chart, top-down and bottom-up. It states Android Gradle Plugin 7.3 or higher and a test device on API 29 or higher with Google Play. A profileable app uses the release variant (the manifest tag `<profileable android:shell="true" />` is on by default); Java/Kotlin allocations, heap dumps and the interaction timeline need a debuggable app. Task labels in the IDE change between releases, so no slug or title uses one. ⚠️ VERIFY the exact labels again before each leaf.
- Rendering (developer.android.com/topic/performance/vitals/render): frames must render in under 16 ms for 60 fps, about 11 ms at 90 fps and 8 ms at 120 fps. Slow frames take between 16 ms and 700 ms; frozen frames take longer than 700 ms; the page's table puts an ANR above 5 s. The tools named there are Profile GPU Rendering, Perfetto's FrameTimeline, the Android CPU Profiler and the Android Memory Profiler.
- Tracing (developer.android.com/topic/performance/tracing): system tracing options are the Android Studio profilers, the Perfetto command-line tool (Android 10 and higher), the on-device System Tracing app and the legacy Systrace; traces open in the Perfetto UI at ui.perfetto.dev. System tracing does not show which methods run inside the app process; use the CPU profiler for that. Deeper Perfetto work is in `## Later`.
- Startup (developer.android.com/topic/performance/appstartup/analysis-optimization): TTID is time to initial display (first frame); TTFD is time to full display; `reportFullyDrawn` tells the system the app is fully drawn; look at operations longer than 20 ms; enable StrictMode thread policy in debug builds. ⚠️ VERIFY: cold, warm and hot start definitions and the Android vitals thresholds for slow startup (not in the text read; the Android core roadmap already owns the definitions).
- Macrobenchmark (developer.android.com/topic/performance/benchmarking/macrobenchmark-overview): a Jetpack library for startup and scrolling or animation tests; the JUnit4 rule is `MacrobenchmarkRule` with `measureRepeated`; built-in metrics used here are `StartupTimingMetric` and `FrameTimingMetric`; it runs from a separate test module created with the Android Studio Benchmark module template. Custom metrics are in `## Later`.
- Baseline Profiles (developer.android.com/topic/performance/baselineprofiles/overview): the page says they improve code execution speed by about 30 % from the first launch by compiling listed code paths ahead of time at install; the figure is for code execution in general, not startup alone. Generation uses Macrobenchmark's `BaselineProfileRule` and a Baseline Profile Gradle plugin; Google Play ships the profile with the app; other stores may only apply it after a background step. ⚠️ VERIFY library versions and plugin id before the leaf is written.
- Memory (developer.android.com/topic/performance/memory-overview): garbage collection can push work past the 16 ms frame budget; memory is only freed when references are dropped; going over the per-app heap limit throws `OutOfMemoryError`. ⚠️ VERIFY: the term "leak" and the heap-dump workflow on "Investigating your RAM usage", the LMK page, and whether to name LeakCanary (the page does not mention it).
- Compose performance (developer.android.com/develop/ui/compose/performance): measure a release build with R8 enabled; baseline profiles; stability and strong skipping; `remember`, lazy list `key`, `derivedStateOf`, deferring state reads with lambda-based modifiers such as `Modifier.offset { }`. ⚠️ VERIFY: Layout Inspector recomposition counts and composition tracing (not in the text read).
- Shrinking (developer.android.com/build/shrink-code): R8 does code shrinking, logical optimisation and obfuscation; `isMinifyEnabled` and `isShrinkResources` in the release build type; from AGP 9.3 a single `optimization` block is available and the older settings still work. The page gives no general size-saving figure. ⚠️ VERIFY: App Bundle behaviour on the bundle page (not in the text read).
- Power profiler and network usage: ⚠️ VERIFY page names and steps on developer.android.com; none were read.

## Stems

### measure-first · Measure First

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `measure-before-you-fix` | easy | "What is the first thing I do when someone says my app is slow?" | A doctor takes a temperature before prescribing: guessing the medicine can make things worse. | "I can see which code is slow just by reading it." | sim: animation a bar chart of five suspect functions; the learner guesses the slowest, then the measured bars show a different winner, and a before and after stopwatch shows a "fix" that changed nothing. | `P:` pick the slowest of three suspects, then compare with the recorded bars · `F:` a slow plain-Kotlin program where the obvious fix changes nothing (count steps, not seconds) · `B:` fix the wrong function and re-measure | Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. Names the rule "measure on a release-like build, not a debug build" (Compose performance page). |

### profiler-tour · Profiler Tour

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `read-a-profiler-screen` | medium | "What do the timeline and the charts on a profiler show me?" | A hospital monitor: several lines (heartbeat, breathing, temperature) share one time axis, and each line answers a different question. | "A profiler tells me where the bug is." | sim: animation a recorded profiler screen with tracks for CPU, memory and events on one time axis; the learner drags a time window and the call chart, flame chart and top-down table update; a quiz asks which chart answers which question. | `P:` which chart answers each of four questions · `P:` read the busiest 200 ms in a recorded flame chart · `B:` pick a window with nothing happening and say what the charts show | volatile. Shown with a reference video; the profiler is never run in the browser. Checked 18 Oct 2026 (developer.android.com/studio/profile): a profileable app uses the release variant, while heap dumps and Java/Kotlin allocations need a debuggable app. Task labels in the IDE change between releases; no slug or title uses one. ⚠️ VERIFY labels before live. |

### cpu · CPU

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `find-the-hot-function` | medium | "How do I find the function that uses the most CPU time?" | A shop bill sorted by item: the top lines show where the money goes. | "The function with the most code is the slow one." | sim: animation a flame chart where box width is time; the learner clicks the widest box to see its callers; a switch between "sample every few milliseconds" (cheap, can miss short calls) and "record every call" (exact, slows the app) changes the picture. | `P:` on a recorded flame chart, which function has the most time of its own · `F:` a function that is slow because it is called a thousand times with the same answer (plain Kotlin, count steps) · `B:` switch to recording every call and say why the app feels slower | Assumes DSA: `dsa/complexity/reading-complexity-in-code`. Names sampling and method tracing; the IDE labels are in the profiler leaf. ⚠️ VERIFY current labels. |
| `catch-main-thread-work` | medium | "How do I find work that runs on the main thread and should not?" | A one-lane shop counter: you look at who is standing at the counter doing paperwork while customers queue. | "If the app does not crash, the main thread is fine." | sim: animation the phone and task queue from `concurrency/why-the-main-thread/what-the-main-thread-does`, replayed as two recorded runs; the learner switches between reading a file inline and moving the read off the main thread, and a per-thread timeline shows the long bar and the frame ticks. | `P:` on a recorded per-thread timeline, which thread holds the long bar · `F(ai):` generated code that parses a big JSON text inside a click handler · `F(ai):` a screen that opens a database on the main thread in its start-up code · `B:` turn on a strict check in a debug build and read the report line (read-only) | Checked 18 Oct 2026 (startup page): StrictMode thread policy can be enabled in debug builds to catch main-thread violations. Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen`, `concurrency/dispatchers/main-safe-functions`. |

### memory-and-leaks · Memory and Leaks

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `read-memory-use` | medium | "How do I see what is using my app's memory?" | A warehouse stock sheet: not just the total, but which shelves keep growing. | "If memory use goes up, it is a leak." | sim: animation a memory line that rises and falls in a saw shape as the garbage collector runs; "allocate in a loop" makes the saw faster, "hold on to items" raises the floor; a dump button lists objects by count. | `P:` which of two recorded memory graphs shows a leak · `F(ai):` a generated loop that creates a new object for every item on every frame (plain Kotlin, count allocations) · `B:` keep every item in a list and watch the floor rise | Checked 18 Oct 2026 (memory overview): garbage collection can push work past the 16 ms frame budget; memory is only freed when references are dropped; a per-app heap limit raises `OutOfMemoryError`. Assumes Foundation: `foundation/how-computers-work/memory-vs-storage`. Names native allocations only. ⚠️ VERIFY: the heap-dump steps (page "Investigating your RAM usage"), the low-memory killer page and whether LeakCanary may be named. |
| `find-a-leak` | hard | "How do I find what is keeping an old screen in memory?" | A library book that never came back: you look at the borrower cards to see who still holds it. | "Forcing garbage collection frees a leak." | sim: animation a heap dump drawn as boxes joined by ropes; the learner rotates the phone three times, takes a dump, finds three old screen boxes and follows each rope back to a long-lived object; cutting the rope frees them. | `P:` on a recorded reference chain, which object is the root holding the old screen · `P:` after cutting one rope, how many old screens remain · `F:` match each line of a given class to the rope it creates (read-only) · `B:` take two dumps one rotation apart and compare the counts | Heap dumps need a debuggable app (checked 18 Oct 2026). Assumes Android core: `android-core/lifecycle/leaking-an-activity`. Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references`, `kotlin/lambdas/closures`. Helps to know Architecture and data: `architecture-data/viewmodel/viewmodel-lifetime`. |

### jank-and-frames · Jank and Frames

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `frame-budget` | easy | "How long does one frame get before the screen stutters?" | A train timetable: a train leaves every 16 ms whether the passengers are ready or not. | "A frame that takes 17 ms is just shown 1 ms late." | sim: animation a conveyor belt that moves one tray every 16 ms (with 11 ms and 8 ms options); heavy work on a tray makes it miss its slot and the screen repeats the last tray; counters separate slow frames from frozen frames. | `P:` at 120 frames a second, which of five frame times miss the budget · `P:` how many frames does one 100 ms job skip at 60 frames a second (model) · `B:` raise the refresh rate and re-check the same job | Checked 18 Oct 2026 (render page): under 16 ms for 60 fps, 11 ms for 90 fps, 8 ms for 120 fps; a missed frame is dropped, not shown late; slow frames 16 ms to 700 ms; frozen frames longer than 700 ms. The page uses rounded values. Assumes Concurrency: `concurrency/why-the-main-thread/what-the-main-thread-does`. |
| `find-the-janky-frame` | hard | "How do I find a slow frame and see what it was doing?" | A security-camera timeline: find the moment the door was open, then zoom in on who was in the room. | "The average frame time tells me if the app is smooth." | sim: animation a recorded trace: a row of frame ticks (green under the budget, red over) above thread rows; the learner clicks a red tick and the main-thread row shows the slice that used the time. | `P:` on a recorded trace, which slice made frame 41 miss the budget · `P:` is the cause on the main thread or elsewhere · `B:` compare two traces with the same average but different slowest frames | Checked 18 Oct 2026 (render and tracing pages): Perfetto FrameTimeline helps track slow or frozen frames; Profile GPU Rendering gives on-screen bars; traces open at ui.perfetto.dev. Deep Perfetto work is in `## Later`. ⚠️ VERIFY the Android Studio route to record a system trace and how the UI marks jank. Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen`. |

### compose-performance · Compose Performance

Flags: phase=5 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `see-needless-recomposition` | medium | "How do I see which composables redraw more often than they should?" | A class register read out in full every time one pupil moves, with a tally next to each name showing who was called. | "Compose only redraws what changed, so it is always efficient." | sim: animation a recorded list screen with a counter badge on every part; the learner switches between recorded runs of two code variants (value read high up or low down, unstable list parameter) and watches the badges climb as a timer changes. | `P:` which badges climb when the timer changes · `F(ai):` a generated screen that reads a fast-changing value at the top, so everything below redraws · `B:` move the read one level down | volatile. Assumes UI: `ui/state-and-recomposition/what-is-recomposition`. ⚠️ VERIFY: Layout Inspector recomposition counts, composition tracing, and what strong skipping changes in the current Compose compiler; the Compose performance page names stability and strong skipping but its subpages were not read. |
| `fix-slow-compose-code` | medium | "Which small change stops a screen from redrawing on every frame?" | Changing only the price on one shelf label instead of reprinting every label in the shop. | "Adding `remember` everywhere makes a screen faster." | sim: animation the same recorded list screen; the learner toggles the fixes (stable item keys, `derivedStateOf`, reading the value inside a lambda, `remember`) one at a time and watches the badges and a frame-time bar drop. | `F(ai):` a generated scrolling list with no item keys · `F(ai):` a generated animation that reads its offset in composition instead of in a lambda modifier · `P:` which fix removes which counter · `B:` apply a fix to a screen that did not need it and read the numbers | Checked 18 Oct 2026 (Compose performance page): `remember`, lazy list `key`, `derivedStateOf`, deferring state reads with lambda-based modifiers such as `Modifier.offset { }`; measure in release mode with R8. Assumes UI: `ui/lists/lazy-column`, `ui/lists/list-keys`, `ui/state-and-recomposition/remember`. |

### startup · Startup

Flags: phase=5 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `measure-startup` | medium | "How do I measure how long my app takes to start?" | A stopwatch at a race: start at the gun, stop at the first finish line, and run several laps because one lap can be luck. | "One run on my own phone tells me my startup time." | sim: animation ten laps drawn as dots on a time axis; the learner changes the number of repeats and watches the middle value settle; a bar splits time into first frame and fully usable, with a marker the app can move. | `P:` from ten recorded times, give the middle one · `P:` which bar is first frame and which is fully usable · `B:` place the fully-drawn marker too early and see the wrong number | volatile. Checked 18 Oct 2026 (startup and Macrobenchmark pages): TTID is time to initial display, TTFD is time to full display, `reportFullyDrawn` reports the app is fully drawn; `MacrobenchmarkRule` with `measureRepeated` and `StartupTimingMetric`, in a separate test module from the Android Studio Benchmark template. Shown in a reference video. ⚠️ VERIFY: Android vitals thresholds for slow startup. Assumes Android core: `android-core/app-startup/cold-warm-hot-start`. Assumes Testing: `testing/device-tests/local-vs-device-tests`. Helps to know Tools: `tools/adb-and-logcat/adb-basics`. |
| `speed-up-startup` | medium | "What work can I remove or delay so the first screen shows sooner?" | A restaurant opening: unlock the door and light the stove first, and let deliveries wait until after the first guest sits down. | "Everything in the Application class must finish before the first screen." | sim: animation the tap-to-first-frame timeline from `android-core/app-startup/from-tap-to-first-frame` with a stopwatch per step; the learner moves each start-up task between "before first frame" and "after first frame" and watches the first-frame marker move. | `F(ai):` a generated Application class that opens a database and starts three SDKs on the main thread · `P:` which tasks to move to get under a target time (model) · `B:` move every task after the first frame and say what breaks | Checked 18 Oct 2026 (startup page): look first at operations longer than 20 ms. Assumes Android core: `android-core/app-startup/what-is-the-application-class`, `android-core/app-startup/from-tap-to-first-frame`. |
| `baseline-profiles` | medium | "What does a baseline profile do for a user's first launch?" | A chef's prepared ingredients: the most-used items are chopped before the first order, so the first customer does not wait. | "The app only gets faster after a user has used it for days." | sim: animation two phones side by side at first launch: one with a list of code paths prepared at install, one preparing as it goes; the learner chooses which journeys go in the list and the first-launch bars shrink only for those. | `P:` which of three journeys gain if only startup is listed · `F(ai):` a generated profile recorder that never opens the scrolling screen users use most · `B:` list nothing and compare | volatile. Checked 18 Oct 2026 (Baseline Profiles page): the page says about 30 % better code execution from first launch, for the listed paths, not for startup alone; generated with `BaselineProfileRule` from Macrobenchmark; Google Play ships the profile. ⚠️ VERIFY library versions, the Gradle plugin id and the minimum Android version. Assumes Foundation: `foundation/from-code-to-running/what-is-art`. |

### battery-and-network · Battery and Network

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `find-battery-and-network-drain` | medium | "How do I find what is draining the battery or using data?" | A separate electricity meter per room: you find the room that uses most when nobody is home. | "If the screen is off, my app cannot use battery." | sim: animation a battery tank with a tap per feature (location, polling, wake-ups) and a recorded power chart; the learner closes taps one by one and the drain line flattens; a request list shows repeated identical calls. | `P:` which spike on the recorded chart matches the 30-second polling · `F(ai):` a generated screen that asks the server every 5 seconds, change it to reuse saved answers · `B:` turn the screen off and see what still drains | volatile. ⚠️ VERIFY: the power profiler page, the network inspection tool and their steps; none were read. Assumes Android core: `android-core/background-work/why-background-is-limited`. Assumes Networking inside apps: `app-networking/caching/reusing-saved-answers`. Helps to know Computer networking: `networking/mobile-networks-and-latency/flaky-mobile-networks`. |

### app-size · App Size

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `shrink-the-app` | easy | "What makes my app download smaller?" | Packing a suitcase: leave out what you will not wear and fold the rest tightly. | "Deleting a few images is the only way to make an app smaller." | sim: animation a suitcase holding libraries, code, images and languages; the learner switches code shrinking, resource shrinking and removing unused libraries on, the bar drops, and a warning appears when a needed class is removed. | `P:` which of five items the shrinker can remove · `F(ai):` a generated build file that leaves shrinking off for release · `B:` turn shrinking on and read a "missing class" failure (read-only) | volatile. Checked 18 Oct 2026 (developer.android.com/build/shrink-code): R8 does code shrinking, logical optimisation and obfuscation; `isMinifyEnabled` and `isShrinkResources` in the release build type; a single `optimization` block exists from AGP 9.3 and the older settings still work. ⚠️ VERIFY: Android App Bundle behaviour. Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file`. Helps to know Tools: `tools/gradle-basics/what-gradle-does`. R8 tuning in depth is in `## Later`. |

### putting-it-together · Putting It Together

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `find-the-slowest-part` | hard | "For a slow screen, which tool do I open first, what do I look at, and how do I prove the fix?" | A detective: do not accuse the loudest suspect; gather evidence in order, check each alibi and confirm after the arrest. | "The first thing that looks slow is the cause." | sim: animation a recorded phone with a vague complaint ("my feed is slow"); the learner picks a symptom (start, scroll, memory, battery), receives the matching tool card and a recorded result, chooses a fix variant, and a before and after bar proves it. | `F(ai):` review a generated screen with a main-thread read, a missing list key and a heavy Application class, and rank the three by measured cost · `P:` for four complaints, name the tool and the first thing to look at · `B:` apply two fixes at once and say what you can no longer tell | Checkpoint ability: "Name the tool and the first thing to look at for jank" (`docs/01` §6.2). Fruit (see `## Fruits`). Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `profiling/putting-it-together/find-the-slowest-part` | "Make a slow screen measurably faster" | Start from a screen you did not write that has at least two planted problems · for each problem, write the symptom, the tool you chose and the first thing you looked at before you touched code · record before and after numbers from a release-like build, at least 3 runs, using the middle value · change one thing at a time · say which fix gave the biggest change and which gave none · state one thing the profiler could not tell you | "How I find the slowest part instead of guessing": name the tool and the first thing to look at for jank (`docs/01` §6.2 profiling ability, L24). |

## Later

- Perfetto trace deep dive: reading raw trace queries is a specialist skill after the basics here.
- Custom Macrobenchmark metrics and custom trace sections: measurement design for teams.
- R8 tuning in depth: keep rules and shrinker debugging come after the first switches.
- Graphics and game rendering profiling: a separate field from app screens.
- Profiling native code: belongs after the native layer branch.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 10 stems, 16 leaves, all `shadow`. | +16 | Est. 14, band 11–17: inside the band (+14 %). Beyond the sketch: startup has 3 leaves (`baseline-profiles` is a separate mechanism from moving work, so it fails the merge test); `catch-main-thread-work` and `find-the-hot-function` split cpu into reading a chart and finding wrong-thread work; a final stem `putting-it-together` added so the last leaf is a synthesis leaf and the fruit sits on it (F2). Stem ids `memory-and-leaks` and `startup` kept as `android-core` cites them; `compose-performance` exists because `ui` hands Compose performance to this branch. |
| 2026-10-18 | Difficulty mix: 3 easy, 10 medium, 3 hard (19 % hard). Sims: 16 of 16 leaves (100 %). | 0 | Almost every profiling idea depends on behaviour you cannot see in the code (Q2) and is commonly misjudged (Q4), so the mix sits medium-heavy against the 40/45/15 sanity target in `docs/08` §4; hard stays under 30 %. The sim share is far above the 15–25 % guide because there is no emulator on the site: every profiler result is shown as a recorded `animation`, never as a live profiler. |
| 2026-10-18 | Sim kind fixed (author decision 18 Oct 2026): five profiling sims that said `code-to-screen` now say `animation` (`catch-main-thread-work`, `see-needless-recomposition`, `fix-slow-compose-code`, `speed-up-startup` and `find-the-slowest-part`); the four rows the A2e review questioned (`read-a-profiler-screen`, `find-a-leak`, `frame-budget`, `baseline-profiles`) already said `animation` and stay. | 0 | No leaf added or removed. |
