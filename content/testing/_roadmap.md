```yaml
branch: testing
bough: craft
stems:
  - id: why-test
    title: "Why Test"
    why: "A fresher who only taps through the app cannot know a fix worked or that an old feature still works, so the branch starts with what a test gives."
    leaves:
      - id: why-tests-exist
        title: "What does a test give me that tapping through the app does not?"
        difficulty: easy
        requires: []
        fruit: false
  - id: unit-tests
    title: "Unit Tests"
    why: "The smallest test runs on your own computer in a second, and every other kind of test copies its shape."
    leaves:
      - id: run-your-first-unit-test
        title: "How do I write and run one test on my own computer?"
        difficulty: easy
        requires: [testing/why-test/why-tests-exist]
        fruit: false
      - id: arrange-act-assert
        title: "What are the three parts of a test that anyone can read?"
        difficulty: easy
        requires: [testing/unit-tests/run-your-first-unit-test]
        fruit: false
  - id: assertions
    title: "Assertions"
    why: "A test is only as good as its check, and a test never seen failing proves nothing."
    leaves:
      - id: assert-the-right-thing
        title: "Which check should I use to compare a result with what I expect?"
        difficulty: easy
        requires: [testing/unit-tests/arrange-act-assert]
        fruit: false
      - id: watch-a-test-fail
        title: "Why must I see a new test fail before I trust it?"
        difficulty: easy
        requires: [testing/assertions/assert-the-right-thing]
        fruit: false
  - id: test-doubles
    title: "Test Doubles"
    why: "Tests need stand-ins for the network, the clock and the database, and badly chosen stand-ins make tests that always pass."
    leaves:
      - id: what-is-a-test-double
        title: "Why do tests use a stand-in instead of the real thing?"
        difficulty: easy
        requires: [testing/unit-tests/arrange-act-assert]
        fruit: false
      - id: fake-stub-or-mock
        title: "Should this stand-in be a fake, a stub or a mock?"
        difficulty: medium
        requires: [testing/test-doubles/what-is-a-test-double]
        fruit: false
      - id: tests-that-always-pass
        title: "Why is my test green even though the code is broken?"
        difficulty: medium
        requires: [testing/test-doubles/fake-stub-or-mock, testing/assertions/watch-a-test-fail]
        fruit: false
  - id: testing-coroutines
    title: "Testing Coroutines"
    why: "Almost every Android data call waits, and a test that really waits is slow, flaky or wrong."
    leaves:
      - id: run-test-and-virtual-time
        title: "How do I test code that waits five seconds without waiting five seconds?"
        difficulty: hard
        requires: [testing/unit-tests/arrange-act-assert]
        fruit: false
      - id: inject-the-dispatcher
        title: "Why must a class receive its dispatcher instead of choosing one?"
        difficulty: medium
        requires: [testing/testing-coroutines/run-test-and-virtual-time, testing/test-doubles/what-is-a-test-double]
        fruit: false
      - id: test-a-flow
        title: "How do I check the values a flow sends, in order?"
        difficulty: medium
        requires: [testing/testing-coroutines/run-test-and-virtual-time]
        fruit: false
  - id: testing-viewmodels
    title: "Testing ViewModels"
    why: "The ViewModel holds the screen's logic, so it is the highest-value thing to test and needs no phone."
    leaves:
      - id: test-viewmodel-state
        title: "How do I check that a ViewModel goes from loading to success?"
        difficulty: medium
        requires: [testing/testing-coroutines/test-a-flow, testing/testing-coroutines/inject-the-dispatcher]
        fruit: false
      - id: test-the-error-path
        title: "How do I test what the screen shows when the server fails?"
        difficulty: medium
        requires: [testing/testing-viewmodels/test-viewmodel-state, testing/test-doubles/fake-stub-or-mock]
        fruit: false
  - id: device-tests
    title: "Device Tests"
    why: "Some behaviour only exists on a real phone, and a fresher must know which tests cost a device and which do not."
    leaves:
      - id: local-vs-device-tests
        title: "Which tests can run on my computer and which need a phone?"
        difficulty: easy
        requires: [testing/unit-tests/run-your-first-unit-test]
        fruit: false
  - id: ui-tests
    title: "UI Tests"
    why: "Taps, text and navigation can break while every ViewModel test stays green, so screens need their own tests."
    leaves:
      - id: test-a-composable
        title: "How does a test tap a button and check the text on a screen?"
        difficulty: medium
        requires: [testing/device-tests/local-vs-device-tests]
        fruit: false
      - id: find-nodes-with-semantics
        title: "How does a UI test find the right element when text repeats or changes?"
        difficulty: medium
        requires: [testing/ui-tests/test-a-composable]
        fruit: false
      - id: test-a-screen-with-fake-state
        title: "How do I test a whole screen without a real ViewModel or network?"
        difficulty: medium
        requires: [testing/ui-tests/find-nodes-with-semantics, testing/test-doubles/what-is-a-test-double]
        fruit: false
  - id: screenshot-tests
    title: "Screenshot Tests"
    why: "Some bugs are only visible, such as a moved button or a wrong colour, and a screenshot test catches them without a human."
    leaves:
      - id: catch-a-visual-change
        title: "How can a test notice that my button moved a few pixels?"
        difficulty: medium
        requires: [testing/ui-tests/test-a-composable]
        fruit: false
  - id: flaky-tests
    title: "Flaky Tests"
    why: "A test that fails at random teaches the team to ignore red, so a fresher must know the usual causes and cures."
    leaves:
      - id: why-a-test-flakes
        title: "Why does the same test pass, then fail, with no code change?"
        difficulty: hard
        requires: [testing/testing-coroutines/inject-the-dispatcher, testing/ui-tests/test-a-composable]
        fruit: false
      - id: make-a-test-reliable
        title: "How do I make a flaky test steady?"
        difficulty: medium
        requires: [testing/flaky-tests/why-a-test-flakes]
        fruit: false
  - id: putting-it-together
    title: "Putting It Together"
    why: "The checkpoint ability is proving a bug is fixed and judging tests you did not write, so the branch ends on both."
    leaves:
      - id: review-generated-tests
        title: "Would I accept this generated test file in review?"
        difficulty: hard
        requires: [testing/test-doubles/tests-that-always-pass, testing/flaky-tests/make-a-test-reliable]
        fruit: false
      - id: prove-a-bug-is-fixed
        title: "How do I prove a bug is fixed with a test that fails first and then passes?"
        difficulty: medium
        requires: [testing/assertions/watch-a-test-fail, testing/testing-viewmodels/test-the-error-path, testing/putting-it-together/review-generated-tests]
        fruit: true
```

## Branch prerequisites

All 22 leaves are `shadow` on 18 Oct 2026. Nothing is published. The branch is phase 5. There is no Android emulator or simulator in the browser (L15, D8): local tests that use plain Kotlin run in the Kotlin Playground as models; anything that needs a phone (device tests, Compose UI tests, screenshot tests) is shown with a reference video or a `code-to-screen` sim, where the learner flips between pre-written code variants and watches a drawn screen or a pass/fail timeline change.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/debugging-mindset/what-is-a-bug` and `foundation/debugging-mindset/find-a-bug-without-guessing` (phase 1) — used by watch-a-test-fail and prove-a-bug-is-fixed.
- Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write` (phase 1) — used by review-generated-tests.
- Assumes Foundation: `foundation/programming-thinking/trace-code-by-hand` (phase 1) — used by every Predict-the-output task on test results.
- Assumes Kotlin: `kotlin/functions/declaring-functions` and `kotlin/lambdas/lambda-syntax` (phase 2) — a test is a function and `assertFailsWith { }` takes a lambda.
- Assumes Kotlin: `kotlin/classes-and-objects/two-kinds-of-equality` and `kotlin/special-classes/data-classes` (phase 2/3) — used by assert-the-right-thing (an equality check compares values or references).
- Assumes Kotlin: `kotlin/errors-and-results/exceptions` (phase 3) — used by assert-the-right-thing when a test expects a failure.
- Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface` (phase 3) — used by what-is-a-test-double (a double implements the same interface).
- Assumes Kotlin: `kotlin/reading-code/spotting-risky-patterns` (phase 3) — used by review-generated-tests.
- Assumes Tools: `tools/android-studio/find-your-way-around-studio` (phase 2) — run-your-first-unit-test uses the run gutter icon.
- Assumes Tools: `tools/gradle-basics/dependencies-and-configurations` (phase 5, earlier in the tie-break) — where test dependencies live in the build file.
- Assumes Concurrency: `concurrency/suspend/what-suspend-means` and `concurrency/coroutine-basics/launch-a-coroutine` (phase 4) — used by run-test-and-virtual-time.
- Assumes Concurrency: `concurrency/dispatchers/main-safe-functions` and `concurrency/dispatchers/main-io-and-default` (phase 4) — used by inject-the-dispatcher.
- Assumes Concurrency: `concurrency/flow-basics/collect-and-cancel` and `concurrency/state-and-shared-flow/state-flow` (phase 4) — used by test-a-flow and test-viewmodel-state.
- Assumes Concurrency: `concurrency/scopes/scopes-in-android-code` (phase 4) — used by test-viewmodel-state (`viewModelScope` needs a replaced Main dispatcher in a local test).
- Assumes Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel` and `architecture-data/ui-state-and-udf/loading-success-error` (phase 4) — used by test-viewmodel-state.
- Assumes Architecture and data: `architecture-data/repositories/repository-interface-and-fakes` and `architecture-data/dependency-injection/constructor-injection` (phase 4) — fakes are introduced there; this branch adds stubs, mocks and the traps.
- Assumes Architecture and data: `architecture-data/ui-state-and-udf/expose-state-not-mutable-state` (phase 4) — used by test-viewmodel-state (the test reads the exposed state).
- Assumes Networking inside apps: `app-networking/errors-and-retries/kinds-of-failure` (phase 4) — used by test-the-error-path.
- Assumes UI: `ui/first-composable/what-is-a-composable` and `ui/modifiers/reacting-to-taps` (phase 3) — used by test-a-composable.
- Assumes UI: `ui/accessibility/what-a-screen-reader-hears` (phase 5, UI is earlier in the tie-break) — used by find-nodes-with-semantics (the test tree and the screen-reader tree come from the same semantics).
- Assumes UI: `ui/state-and-recomposition/state-hoisting` (phase 3) — used by test-a-screen-with-fake-state.
- Assumes UI: `ui/first-composable/previews` (phase 3) — used by catch-a-visual-change (screenshot tests start from previews).
- Assumes Android core: `android-core/activities/what-is-an-activity` (phase 3) — used by local-vs-device-tests and test-a-composable (a rule can start an Activity).
- Helps to know OS: `os/threads/what-is-a-thread` and `os/synchronisation/race-conditions` — why-a-test-flakes recaps a race in one paragraph.
- Helps to know Android core: `android-core/process-death/testing-process-death` — the "kill it on purpose" idea is a manual test; this branch automates checks like it.
- Helps to know Profiling: `profiling/measure-first/measure-before-you-fix` — a slow test is a measuring problem too. No leaf depends on it.

Foundation is recommended first ("Start here"), then Kotlin, Android core, UI phase-3 stems, Concurrency and Architecture and data. `app-networking/_roadmap.md` and `kmp/_roadmap.md` cite `testing/test-doubles/what-is-a-test-double` as `Helps to know`; that id is kept exactly.

Challenge note: Android framework code and Compose UI tests do not run in the Kotlin Playground. Every `P:` and `F:` on a UI test is a plain-Kotlin model (a fake "screen" with a tap method and a text value) or a read-only "which line is wrong" task. ⚠️ VERIFY on kotlinlang.org whether the Playground can run `kotlin.test` assertions and `kotlinx-coroutines-test` (`runTest`) before any leaf goes live; until then, tasks use `check()` / `require()` / `println` as plain-Kotlin models and `runTest` leaves are read-only.

Facts checked against official docs on 18 Oct 2026, to be rechecked by `reviewer` before each leaf goes live:

- Coroutine testing (developer.android.com/kotlin/coroutines/test): the library is `kotlinx-coroutines-test` (version 1.6 or later). `runTest` skips delays and uses a `StandardTestDispatcher` by default; `advanceUntilIdle()`, `advanceTimeBy(ms)` and `runCurrent()` move virtual time; `UnconfinedTestDispatcher` starts coroutines eagerly; `Dispatchers.setMain` and `Dispatchers.resetMain` replace Main in local tests. The docs' `MainDispatcherRule` is sample code to copy into your own project, not a library class. Dispatchers should be injected, and all test dispatchers in one test must share the scheduler (`testScheduler`).
- Compose testing (developer.android.com/develop/ui/compose/testing): `createComposeRule()` (needs `ui-test-manifest` in debug) and `createAndroidComposeRule<YourActivity>()`; `ui-test-junit4` artifact; finders and actions such as `onNodeWithText`, `performClick`, `assertIsDisplayed`. ⚠️ VERIFY: `testTag`, `onNodeWithTag` and the merged versus unmerged tree behaviour on the Semantics page.
- Test types (developer.android.com/training/testing/fundamentals): the page names "local tests" (run on your machine, also called host-side) and "instrumented tests" (run on a physical or emulated device). ⚠️ VERIFY: the `src/test` and `src/androidTest` folder names, the definitions of fake, stub and mock, and the definition of a flaky test; those pages were not in the text read.
- Screenshot tests (developer.android.com/studio/preview/compose-screenshot-testing): Compose Preview Screenshot Testing is still in development (experimental); plugin id `com.android.compose.screenshot`; source set `screenshotTest`; annotation `@PreviewTest`; tasks `updateDebugScreenshotTest` and `validateDebugScreenshotTest`; the HTML report is written under `build/reports/screenshotTest`. The page says the standalone plugin is deprecated from AGP 9.5.0-alpha03 in favour of AGP test suites. ⚠️ VERIFY again before live; the whole stem is `volatile`.

## Stems

### why-test · Why Test

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-tests-exist` | easy | "What does a test give me that tapping through the app does not?" | A pilot's pre-flight checklist: same checks, same order, every flight, with no need to remember. | "Tests are extra work for big teams; I will just run the app and look." | sim: animation twenty old features as lamps; the learner changes one line and a hidden break turns one lamp red within a second, while a hand-tapping clock needs minutes to find it. | `P:` after a change to one function, which of five listed checks turns red · `B:` change the code and compare how long finding the break takes by hand and by test | Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. |

### unit-tests · Unit Tests

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `run-your-first-unit-test` | easy | "Where do I write a test and how do I run it without a phone?" | A practice net at a cricket ground: you test one shot alone, in seconds, with no full match. | "To test my code I must install the app on a phone first." | none: tooling steps (the run icon, the test folder, the green tick), shown in a reference video; the Playground runs a plain-Kotlin model of a test. | `P:` read a 6-line test and say if it passes (plain-Kotlin model using `check()`) · `F:` a test function that never runs because it has no test marker (read-only) · `B:` change the expected number so it fails | Assumes Tools: `tools/android-studio/find-your-way-around-studio`. Assumes Kotlin: `kotlin/functions/declaring-functions`. ⚠️ VERIFY: the `src/test` folder name, the JUnit version in the current Android Studio template and the Gradle line that adds it. |
| `arrange-act-assert` | easy | "What are the three parts of a test that anyone can read?" | A science experiment write-up: set the equipment up, do the one thing, record what you saw. | "One test should check everything the function can do." | none: a structure rule; a three-colour code block shows it. | `P:` which line is the act · `F:` a test with setup mixed into the check, split it into three parts · `B:` add a second act and say why the failure message gets unclear | Names the "one reason to fail" habit and test names that say what and when. |

### assertions · Assertions

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `assert-the-right-thing` | easy | "Which check should I use to compare a result with what I expect?" | Counting a shop till against the receipt total: the two columns must be the right way round, or the error note reads backwards. | "`assertTrue(a == b)` says the same thing as an equality check." | none: a choice rule; a table of what each failing check prints is clearer than motion. | `P:` which message prints when two checks fail · `F:` a test that compares two objects by identity instead of value · `F:` expected and actual swapped, read the confusing message · `B:` compare two decimal numbers exactly | Assumes Kotlin: `kotlin/classes-and-objects/two-kinds-of-equality`, `kotlin/special-classes/data-classes` and `kotlin/errors-and-results/exceptions`. Names `assertEquals`, `assertTrue` and `assertFailsWith`. ⚠️ VERIFY: kotlin.test versus JUnit names in the current Android template. |
| `watch-a-test-fail` | easy | "Why must I see a new test fail before I trust it?" | The test button on a smoke alarm: a dead alarm and a working one look the same until you press it. | "A green test means my code is right." | sim: animation a five-line function with a bug toggle and a red or green bar; a weak test stays green with the bug in, a good test goes red with the bug and green with the fix. | `P:` red or green for four pairs of test and code · `F:` a test that still passes with the bug in the code, make it fail · `B:` break the code on a different line and see if the test notices | First half of "fail first, then pass"; paid off in `prove-a-bug-is-fixed`. Assumes Foundation: `foundation/debugging-mindset/what-is-a-bug`. |

### test-doubles · Test Doubles

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-test-double` | easy | "Why do tests use a stand-in instead of the real thing?" | A stunt double in a film: the real actor stays safe and the scene can be shot again and again. | "Testing with a fake means I am not testing the real code." | none: one idea with one diagram (the caller talks to an interface; the real network or a stand-in sits behind it). | `P:` which of three classes can be swapped in without changing the caller · `F:` a class that builds its own real dependency inside, so it cannot be tested · `B:` swap in a stand-in that always fails | Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface`. Assumes Architecture and data: `architecture-data/repositories/repository-interface-and-fakes` and `architecture-data/dependency-injection/constructor-injection`. |
| `fake-stub-or-mock` | medium | "Should this stand-in be a fake, a stub or a mock?" | A rehearsal: a small kitchen that really cooks (fake), a prop menu that always says the same thing (stub), a spy who writes down every order (mock). | "Mocks are always better than fakes; a mocking library does everything." | sim: animation three stand-in machines on a test bench; ask each the same question twice: the fake keeps state, the stub repeats itself, the mock shows a tally of calls. | `P:` what each double returns on the second call · `F(ai):` a generated test that mocks the very class it is testing · `B:` change the order of two calls and see which double notices | ⚠️ VERIFY: definitions on developer.android.com "Using test doubles in Android". Mocking libraries are named only. ⚠️ VERIFY: which mocking library to name and its Kotlin support. |
| `tests-that-always-pass` | medium | "Why is my test green even though the code is broken?" | A student who marks their own homework using an answer key copied from their own answers. | "More tests with more mocks means more safety." | sim: animation a bug-injection button breaks the code one line at a time; weak tests stay green because the stand-in returns what the test told it, strong tests turn red; a scoreboard counts bugs caught. | `F(ai):` a generated test that sets up a stub to return 5 and asserts it returns 5 · `F(ai):` a test with a call but no check · `P:` which of four tests fail if the function returns 0 · `B:` delete the body of the code under test and count tests still green | Typical `ai-review` defect from `docs/08` §5. Mutation testing is in `## Later`; this leaf only uses the "inject a bug" idea. |

### testing-coroutines · Testing Coroutines

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `run-test-and-virtual-time` | hard | "How do I test code that waits five seconds without waiting five seconds?" | A film editor fast-forwarding a tape: the five seconds are still on the tape, but you jump over them. | "A test of `delay` takes as long as the delay." | sim: animation two clocks: a slow real clock and a test clock that jumps straight to the next queued task; the learner presses advance and queued coroutines run only when time is moved. | `P:` what prints before and after `advanceUntilIdle()` (plain-Kotlin model) · `F:` a test that asserts before the queued coroutine has run · `F:` a test that waits with `Thread.sleep` · `B:` add `withContext` to a delay and watch virtual time stop skipping | Checked 18 Oct 2026 (coroutine testing page): `runTest` skips delays; it uses `StandardTestDispatcher`; `advanceUntilIdle`, `advanceTimeBy`, `runCurrent`; if code switches dispatchers the delay is no longer skipped. Assumes Concurrency: `concurrency/suspend/what-suspend-means`, `concurrency/coroutine-basics/launch-a-coroutine`. ⚠️ VERIFY Playground runs `runTest`. |
| `inject-the-dispatcher` | medium | "Why must a class receive its dispatcher instead of choosing one?" | A kitchen with a built-in oven timer the inspector cannot speed up, versus a plug-in timer the inspector can swap for a fast one. | "Hard-coding `Dispatchers.IO` is harmless; the test will cope." | sim: animation two pipes: with the dispatcher built in, the test timeline shows real waiting; with an injected test dispatcher the same code skips the wait. | `F(ai):` a repository with a hard-coded dispatcher that makes its test slow or flaky · `P:` which of three tests may skip the delay · `B:` swap a standard test dispatcher for an unconfined one and watch the order change | Checked 18 Oct 2026: inject dispatchers; all test dispatchers in one test must share the scheduler (`testScheduler`). Assumes Concurrency: `concurrency/dispatchers/main-safe-functions`, `concurrency/dispatchers/main-io-and-default`. |
| `test-a-flow` | medium | "How do I check the values a flow sends, in order?" | A doorman with a clicker and a notebook: he counts who walked through and in what order. | "Collecting a flow in a test ends by itself, like a list." | sim: reuse `concurrency/flow-basics/what-is-a-flow` sim idea: a tap drips into a cup, with a test clipboard that collects drops into a list as the learner advances virtual time. | `P:` what the collected list holds after two time steps (plain-Kotlin model) · `F:` a test that collects an endless flow and never finishes · `F(ai):` a generated test that checks only the first value of a flow of three · `B:` collect the same flow twice | Checked 18 Oct 2026: `TestScope.backgroundScope` is cancelled at the end of the test. Assumes Concurrency: `concurrency/flow-basics/collect-and-cancel`, `concurrency/state-and-shared-flow/state-flow`. Third-party flow-test helpers are named only. ⚠️ VERIFY before naming one. |

### testing-viewmodels · Testing ViewModels

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `test-viewmodel-state` | medium | "How do I check that a ViewModel goes from loading to success?" | A traffic-light controller tested on a bench: press the button, read the lamps, no road needed. | "ViewModel tests need an emulator or a phone." | sim: animation three lamps (Loading, Success, Error) on a timeline; a fake repository answers when the learner advances virtual time, and the lamp changes only after the answer. | `P:` which state does the ViewModel hold right after it is created, before time moves (plain-Kotlin model) · `F(ai):` a generated ViewModel test that reads the state before the coroutine ran · `F:` a test that never replaces the Main dispatcher (read-only error message) · `B:` swap an unconfined test dispatcher for a standard one | Checked 18 Oct 2026: `Dispatchers.setMain` and `resetMain`; the docs' JUnit rule is sample code to copy, not a library class. Assumes Concurrency: `concurrency/scopes/scopes-in-android-code`. Assumes Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel`, `architecture-data/ui-state-and-udf/loading-success-error`, `architecture-data/ui-state-and-udf/expose-state-not-mutable-state`. |
| `test-the-error-path` | medium | "How do I test what the screen shows when the server fails?" | A fire drill: you practise the bad day on purpose so nobody learns the exit during a real fire. | "Happy-path tests are enough; errors are rare." | sim: reuse `test-viewmodel-state` sim with a switch on the fake repository (success, timeout, empty list, server error); the lamps show which state each leads to. | `P:` which UI state follows each of four fake answers · `F(ai):` a generated test suite with only success cases · `B:` make the fake throw instead of returning an error value | Assumes Networking inside apps: `app-networking/errors-and-retries/kinds-of-failure`. |

### device-tests · Device Tests

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `local-vs-device-tests` | easy | "Which tests can run on my computer and which need a phone?" | Checking a recipe by reading it and tasting it in your own kitchen, versus serving it in the real restaurant. | "Every Android test needs an emulator or a phone." | none: a decision table (needs a screen, needs the Android framework, needs real hardware) plus a reference video of a test running on a device. | `P:` sort eight checks into "runs on my computer" and "needs a phone" · `F(ai):` a generated test placed in the device test folder that only checks a plain function, slow for no reason · `B:` add one Android class to a local test and read the failure (read-only) | volatile. Checked 18 Oct 2026: the testing fundamentals page names "local tests" (host-side) and "instrumented tests" (on a physical or emulated device). ⚠️ VERIFY: folder names, the testing-pyramid wording and any framework that runs Android code on the computer; name none until checked. Assumes Android core: `android-core/activities/what-is-an-activity`. |

### ui-tests · UI Tests

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `test-a-composable` | medium | "How does a test tap a button and check the text on a screen?" | A robot customer in a shop: it walks in, presses the bell and reads the sign that changes. | "A UI test clicks screen pixels at fixed positions." | sim: code-to-screen a drawn phone with a button and a text; the learner flips between test lines (find by text, click, check shown) and screen variants and watches a robot finger act and a green or red tick appear. | `P:` pass or fail for a click-then-check on a counter screen (plain-Kotlin model) · `F:` a test that checks the text before the click · `B:` rename the button text and watch the test fail | volatile. Checked 18 Oct 2026 (Compose testing page): `createComposeRule()` (needs the `ui-test-manifest` debug dependency) and `createAndroidComposeRule<YourActivity>()`; `onNodeWithText`, `performClick`, `assertIsDisplayed`; the page says tests wait for the UI to be idle. ⚠️ VERIFY dependency lines again. Assumes UI: `ui/first-composable/what-is-a-composable`, `ui/modifiers/reacting-to-taps`. |
| `find-nodes-with-semantics` | medium | "How does a UI test find the right element when text repeats or changes?" | Name tags at a conference: finding "Priya" by face fails when two people look alike, while a numbered badge never changes. | "The test looks at pixels, so it finds things the way my eyes do." | sim: code-to-screen a screen with two buttons both labelled "Add"; the learner switches the finder (by text, by tag, by description) and sees which boxes light up, and a pass or a "more than one match" failure. | `P:` how many elements this finder matches · `F(ai):` a generated test that finds a button by a text shown three times · `F:` a tag put on the wrong element · `B:` add a second label with the same text | volatile. Assumes UI: `ui/accessibility/what-a-screen-reader-hears`. ⚠️ VERIFY on the Compose Semantics page: `testTag`, `onNodeWithTag`, merged versus unmerged tree and `useUnmergedTree`. |
| `test-a-screen-with-fake-state` | medium | "How do I test a whole screen without a real ViewModel or network?" | A puppet show: you control every state of the puppet (loading, error, full) to see how the stage reacts. | "A real UI test must run the whole app with the real server." | sim: code-to-screen the same phone with state cards (Loading, Error, Empty, Full) fed into the screen; the drawn screen changes, a test line checks the matching text and a tap callback adds a line to an "events up" log. | `P:` which check passes for the Error card · `F(ai):` a generated UI test that starts the real ViewModel and calls the real network · `B:` add a state the screen does not handle | volatile. Assumes UI: `ui/state-and-recomposition/state-hoisting`. Helps to know Architecture and data: `architecture-data/ui-state-and-udf/state-down-events-up`. ⚠️ VERIFY: current Compose UI-test API names and rule setup on developer.android.com. |

### screenshot-tests · Screenshot Tests

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `catch-a-visual-change` | medium | "How can a test notice that my button moved a few pixels?" | A spot-the-difference puzzle: the old photo is kept, the new one is laid over it, and any mismatch glows. | "If the text is still there, the screen is still right." | sim: animation an old reference image and a new image side by side with a glowing difference layer; the learner nudges a padding or colour and watches pass or fail change, and a tolerance slider shows why tiny noise matters. | `P:` pass or fail for three small edits · `F(ai):` a generated setup that re-records the reference image on every run so the test never fails · `B:` change a font size | volatile. Checked 18 Oct 2026 (developer.android.com/studio/preview/compose-screenshot-testing): Compose Preview Screenshot Testing is still in development; plugin id `com.android.compose.screenshot`; source set `screenshotTest`; annotation `@PreviewTest`; tasks `updateDebugScreenshotTest` and `validateDebugScreenshotTest`; the page says the standalone plugin is deprecated from AGP 9.5.0-alpha03 in favour of AGP test suites. ⚠️ VERIFY which route to teach before this leaf is written; name no third-party tool until checked. Assumes UI: `ui/first-composable/previews`. |

### flaky-tests · Flaky Tests

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-a-test-flakes` | hard | "Why does the same test pass, then fail, with no code change?" | A kitchen scale that shows a different number each time because someone leans on the counter: the weighing is fine, the room is not. | "If a test fails once and passes on a rerun, ignore it." | sim: animation a grid of twenty runs of one test; the learner switches on causes (real clock, leftover data from another test, real network, thread order, animation timing) and tiles turn red at random; switching a cause off turns its red tiles green. | `P:` which of five tests could flake and why · `F(ai):` a generated test that waits with `Thread.sleep(500)` and hopes · `F(ai):` two tests that share a global list so their order matters · `B:` run the same test in a different order | Q1, Q2 and Q4 apply. Helps to know OS: `os/threads/what-is-a-thread` and `os/synchronisation/race-conditions`. ⚠️ VERIFY the definition of a flaky test on developer.android.com. |
| `make-a-test-reliable` | medium | "How do I make a flaky test steady?" | Making a recipe repeatable: weigh the flour instead of "a handful" and set the oven to a fixed temperature. | "A longer sleep fixes it." | sim: reuse `why-a-test-flakes` sim with the cures as switches (fake clock, fresh data per test, fake network, wait for idle instead of sleep) that turn tiles green one by one. | `F(ai):` replace a sleep with virtual time or a proper wait · `F(ai):` reset shared state before each test · `P:` which cure removes which cause · `B:` bring one cause back and watch tiles flicker | Retrying a failed test until it passes hides the cause; the leaf says so. Setup and cleanup annotations are named only. ⚠️ VERIFY wording. |

### putting-it-together · Putting It Together

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `review-generated-tests` | hard | "Which three risks in this generated test file would stop me approving it?" | A building inspector's checklist: a house with fresh paint can still have no foundation. | "If an AI wrote many tests and they pass, the code is covered." | none: a review exercise on a real file; the editor and a checklist (can it fail, what does it fake, does it wait, does it check one thing) are the material. | `F(ai):` review an 80-line generated test file and list 3 risks with line numbers · `F(ai):` fix one test and show it now fails on a broken version · `P:` which tests survive an emptied function body · `B:` add a missing error case | Synthesis. Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write`. Assumes Kotlin: `kotlin/reading-code/spotting-risky-patterns`. |
| `prove-a-bug-is-fixed` | medium | "How do I prove a bug is fixed with a test that fails first and then passes?" | A plumber who films the leaking pipe first, repairs it, films the dry pipe, and leaves the camera running to catch the next leak. | "Once I fix the bug and it works on my phone, I am done." | sim: animation a bug report card, a red or green bar and a code panel with a fix toggle; steps in order: reproduce, write the test (red), fix (green), undo the fix (red again) to prove the test guards the code. | `F:` write the failing test for a given bug report, then fix it · `F(ai):` a generated "fix" whose test already passed before the fix · `P:` red or green for four combinations of test, bug and fix · `B:` undo the fix and confirm the test fails | Checkpoint ability: "Write one test that fails first and then passes" (`docs/01` §6.2). Fruit (see `## Fruits`). Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `testing/putting-it-together/prove-a-bug-is-fixed` | "Prove one real bug is fixed" | Start from a bug report for a small ViewModel you wrote or were given · write one test at the lowest level that can show the bug · show the failing run before the fix, the passing run after, and a failing run again with the fix undone · no real network and no sleeping inside the test · run the test 20 times in a row without one failure · add one test an AI assistant generated for the same bug and write whether you would accept it and why | "How I would prove a bug is fixed": write one test that fails first and then passes (`docs/01` §6.2 testing ability, L24). |

## Later

- Property-based testing: powerful, but not needed to prove a first bug fixed.
- Mutation testing: tools that inject bugs automatically; the testing leaf on tests that always pass teaches the idea by hand.
- Contract tests between app and server: a team-scale topic.
- Test architecture for large modular apps: needs the modules stem and real project scale.
- Running tests on many device models in the cloud: an infrastructure topic.
- Hilt and dependency-injection test setup: depends on the library chosen in the architecture branch.
- End-to-end test suites and test reports for a team: a project-management topic.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 11 stems, 22 leaves, all `shadow`. | +22 | Est. 20, band 16–24: inside the band (+10 %). Beyond the sketch: `watch-a-test-fail` added in assertions because "fail first" needs its own page before `prove-a-bug-is-fixed`; `tests-that-always-pass` kept as its own leaf (second misconception in test-doubles); `review-generated-tests` and `prove-a-bug-is-fixed` added as the synthesis end so the fruit sits on the last leaf (F2). Stems `testing-coroutines` and `testing-viewmodels` placed before `ui-tests` because ViewModel tests need virtual time and injected dispatchers. `device-tests` placed before `ui-tests` because UI tests run on a device. `screenshot-tests` moved before `flaky-tests` so the branch ends on the checkpoint. |
| 2026-10-18 | Difficulty mix: 7 easy, 12 medium, 3 hard (14 % hard). Sims: 16 of 22 leaves (73 %). | 0 | The mix matches the 40/45/15 sanity band closely. The sim share is above the 15–25 % guide for "elsewhere" because most testing ideas are about time, order and pass or fail state, and no emulator exists on the site; each sim is an animation or a `code-to-screen`, never a running Android test. |
