# interview: testing

## testing/why-test/why-tests-exist
interview-questions:
- [intern] What does an automated test give you that manual tapping through the app does not?
- [junior] What is the difference between writing tests before code (Test-Driven Development) versus testing after code is written?
- [mid] Why do automated tests serve as living documentation and guardrails against regression bugs during refactoring?
- [senior] What goes wrong in production when engineering teams rely solely on manual QA without automated unit and integration test suites?
what-interviewers-listen-for:
- Automated tests provide fast, repeatable verification of business logic and UI correctness across code changes.
- TDD drives modular, decoupled, and testable architecture design.
- Manual QA is slow, expensive, prone to human fatigue, and fails to scale across complex refactoring cycles.
build-exercise: Write a simple utility function, write a test suite covering normal and edge cases, deliberately break the utility logic, and run the test to see immediate failure feedback.
official-references:
- Test your app | https://developer.android.com/training/testing
common-wrong-beliefs:
- Writing automated tests takes too much time and slows down feature delivery compared to manual testing | While initial test writing takes time, automated test suites drastically reduce debugging time and prevent regressions during long-term maintenance.

## testing/unit-tests/run-your-first-unit-test
interview-questions:
- [intern] What is a unit test, and where do unit test files live in an Android Gradle project (`src/test` vs `src/androidTest`)?
- [junior] How do you write and execute a local unit test on your JVM without needing an Android emulator or device?
- [mid] Why are local unit tests executed on the host JVM significantly faster than instrumented device tests?
- [senior] What goes wrong in production when unit tests rely on Android framework dependencies (`android.util.Log` or `Context`) without mocking or abstraction?
what-interviewers-listen-for:
- Local unit tests execute locally on the host JVM, providing instantaneous feedback.
- Android framework classes (`android.*`) are stubbed out by default in JVM unit tests (`junit` / `mockk`), throwing `RuntimeException` unless mocked or using Robolectric.
build-exercise: Create a local unit test file under `src/test/java/...`, write a test asserting a simple mathematical calculation, and run it via Gradle or Android Studio.
official-references:
- Run local unit tests | https://developer.android.com/training/testing/local-tests
common-wrong-beliefs:
- You need a connected Android device or emulator to run basic Kotlin business logic unit tests | Local unit tests run on your computer's JVM in milliseconds without requiring any device or emulator.

## testing/unit-tests/arrange-act-assert
interview-questions:
- [intern] What are the three phases of the Arrange-Act-Assert (AAA) pattern in writing readable unit tests?
- [junior] How does structuring every test with clear AAA separation improve test maintainability and readability?
- [mid] What is the difference between setting up mock data in `Arrange` versus executing the target function in `Act`?
- [senior] What goes wrong in production when tests interleave arrangement, execution, and assertions randomly across complex helper methods?
what-interviewers-listen-for:
- Arrange: set up objects, state, and mocks; Act: invoke the target unit under test; Assert: verify expected outcomes and invariants.
- Clear AAA structure separates test setup from execution and verification, making failure diagnosis instantaneous.
build-exercise: Write a unit test verifying a calculation function, explicitly labeling comments `// Arrange`, `// Act`, and `// Assert`.
official-references:
- Write local unit tests | https://developer.android.com/training/testing/local-tests#build
common-wrong-beliefs:
- Combining setup and assertion statements into a single continuous block makes tests shorter and more concise | Mixing arrangement and assertions obscures test intent and makes debugging failures difficult.

## testing/assertions/assert-the-right-thing
interview-questions:
- [intern] What assertion libraries (like JUnit assertions or Truth) do you use to verify test outcomes?
- [junior] What is the difference between checking referential equality versus structural equality in assertions (`assertEquals` vs `assertThat`)?
- [mid] Why is Google's Truth library (`assertThat(actual).isEqualTo(expected)`) preferred over JUnit assertions for readability?
- [senior] What goes wrong in production when tests assert overly broad state or private internal implementation details instead of public behavior contracts?
what-interviewers-listen-for:
- Truth fluent assertions provide highly readable failure messages (`assertThat(result).containsExactly(...)`).
- Testing public behavior contracts rather than private implementation details ensures refactoring resilience.
build-exercise: Write assertions using JUnit (`assertEquals`) and Google Truth (`assertThat`), and compare failure output messages when an assertion fails.
official-references:
- Google Truth assertion library | https://truth.dev/
common-wrong-beliefs:
- A test that asserts internal private field states is better because it verifies every internal variable | Asserting private implementation details makes tests brittle and tightly coupled, breaking whenever internal refactoring occurs.

## testing/assertions/watch-a-test-fail
interview-questions:
- [intern] Why must you always watch a newly written unit test fail before you trust that it tests what you intended?
- [junior] What is a "false positive" green test, and how does it happen when a test asserts nothing or checks the wrong condition?
- [mid] How does witnessing a test fail prove that your test is actually executing your code path and can detect regressions?
- [senior] What goes wrong in production when teams inherit green test suites that pass vacuously without asserting real business rules?
what-interviewers-listen-for:
- A test that hasn't failed might be asserting a tautology (`assertTrue(true)`) or testing dead code.
- Red-Green-Refactor discipline ensures tests are rigorous and sensitive to actual code changes.
build-exercise: Write a test, assert an incorrect expected value, run it to watch it fail (Red), and then fix the assertion to pass (Green).
official-references:
- Test-driven development | https://developer.android.com/training/testing/fundamentals#tdd
common-wrong-beliefs:
- If a new unit test turns green immediately on its first run, it guarantees your test code is correct and comprehensive | Immediate green tests can be false positives that fail to assert actual functionality or are testing nothing at all.

## testing/test-doubles/what-is-a-test-double
interview-questions:
- [intern] What is a test double, and why do unit tests use stand-ins instead of real network clients or databases?
- [junior] What is the difference between unit testing (isolated) and integration testing (real components)?
- [mid] Why do slow external I/O dependencies make unit tests flaky and impractical without test doubles?
- [senior] What goes wrong in production when tests rely on real network calls or live databases instead of deterministic test doubles?
what-interviewers-listen-for:
- Test doubles replace production dependencies to isolate the unit under test, ensuring speed and determinism.
- Real network and disk operations introduce latency, flakiness, and external environment dependencies.
build-exercise: Create a test double (fake implementation) of a repository interface and pass it into a ViewModel unit test.
official-references:
- Test doubles in Android | https://developer.android.com/training/testing/fundamentals#test-doubles
common-wrong-beliefs:
- Writing true unit tests requires connecting to real production databases and APIs to verify end-to-end integration | Unit tests isolate individual components using test doubles; integration tests handle multi-component verification.

## testing/test-doubles/fake-stub-or-mock
interview-questions:
- [intern] What is the difference between a Fake, a Stub, and a Mock?
- [junior] When should you use a hand-written Fake implementation versus an automated Mocking library (like MockK)?
- [mid] Why are hand-written Fakes (e.g., `FakeUserRepository : UserRepository`) often preferred over complex MockK stubbing for complex data layers?
- [senior] What goes wrong in production when overusing strict MockK mocks that verify every single method call order (over-specification)?
what-interviewers-listen-for:
- Fakes have working implementations (like an in-memory list); Stubs return canned answers; Mocks verify behavior expectations (calls and arguments).
- Hand-written Fakes are robust against refactoring, whereas strict Mocks break when internal method call sequences change.
build-exercise: Write a hand-written Fake repository containing an in-memory list, and compare it with a MockK-stubbed repository in two separate unit tests.
official-references:
- MockK documentation | https://mockk.io/
common-wrong-beliefs:
- Mocks and Fakes are interchangeable terms for the exact same testing object | Fakes contain working logic (e.g., in-memory storage), whereas Mocks/Stubs verify behavior and return canned stubbed responses.

## testing/test-doubles/tests-that-always-pass
interview-questions:
- [intern] Why is a unit test that always passes even when the production code is broken worse than having no test at all?
- [junior] What causes tests to pass falsely (e.g., incorrect mocking, unawaited coroutines, missing assertions)?
- [mid] How do code coverage tools (`Jacoco`) measure test execution paths, and why is 100% code coverage a misleading metric?
- [senior] What goes wrong in production when teams chase high code coverage percentages while writing weak assertions that verify nothing meaningful?
what-interviewers-listen-for:
- False confidence from passing tests masks critical production bugs.
- Code coverage measures line execution, not assertion strength or logic correctness.
build-exercise: Write a unit test with an empty assertion block (`// TODO: add assert`), run it (passing green), and analyze why high coverage with zero assertions is useless.
official-references:
- Test coverage | https://developer.android.com/training/testing/fundamentals#coverage
common-wrong-beliefs:
- Achieving 100% code coverage guarantees your app contains zero bugs and all business logic is thoroughly validated | Code coverage only proves lines were executed during tests; it does not verify that assertions correctly validate business rules.

## testing/testing-coroutines/run-test-and-virtual-time
interview-questions:
- [intern] How do you test coroutines and suspending functions in unit tests using `runTest`?
- [junior] What is virtual time in `StandardTestDispatcher`, and how does it skip `delay()` calls instantly without waiting?
- [mid] What is the difference between `StandardTestDispatcher` and `UnconfinedTestDispatcher`?
- [senior] What goes wrong in production when coroutine tests fail due to uncompleted child jobs or uncaught exceptions lingering in test scopes?
what-interviewers-listen-for:
- `runTest` is the standard coroutine test builder that advances virtual time instantly over `delay()` calls.
- `StandardTestDispatcher` queues tasks requiring manual or automatic yielding, while `UnconfinedTestDispatcher` executes coroutines eagerly.
build-exercise: Write a coroutine test using `runTest` containing a `delay(5000)` call, and verify that the test completes instantly in virtual time.
official-references:
- Test Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines/test
common-wrong-beliefs:
- Testing a suspending function that waits 5 seconds requires your unit test execution to actually sleep and wait for 5 real seconds | `runTest` uses virtual time to skip delays instantly.

## testing/testing-coroutines/inject-the-dispatcher
interview-questions:
- [intern] Why must a class receive its `CoroutineDispatcher` via constructor injection instead of hardcoding `Dispatchers.IO` or `Dispatchers.Main`?
- [junior] How do you set a Main dispatcher in tests using `Dispatchers.setMain()` and `Dispatchers.resetMain()`?
- [mid] How does injecting a `TestDispatcher` into ViewModels or repositories enable deterministic coroutine testing?
- [senior] What goes wrong in production and unit tests when code references `Dispatchers.Main` directly without initializing a test dispatcher (`Module not initialized`)?
what-interviewers-listen-for:
- Injecting dispatchers allows swapping `Dispatchers.Main` and `Dispatchers.IO` with `StandardTestDispatcher` in unit tests.
- Hardcoding `Dispatchers.Main` throws `Module with the Main dispatcher is missing` in JVM unit tests because the Android Main Looper is absent.
build-exercise: Write a ViewModel that takes an injected `CoroutineDispatcher`, write a test setting `Dispatchers.setMain(StandardTestDispatcher())`, and test a coroutine action.
official-references:
- Create a test dispatcher | https://developer.android.com/kotlin/coroutines/test#dispatchers
common-wrong-beliefs:
- You can call code referencing `Dispatchers.Main` directly inside a local JVM unit test without setting a test dispatcher | JVM unit tests lack the Android UI looper; calling `Dispatchers.Main` directly crashes unless `Dispatchers.setMain()` is configured.

## testing/testing-coroutines/test-a-flow
interview-questions:
- [intern] How do you test the values emitted by a Kotlin `Flow` in chronological order?
- [junior] What is the `Turbine` library (`test { ... }`), and how does it simplify testing flows?
- [mid] Why does collecting a cold or hot flow in a unit test require special handling to avoid hanging the test indefinitely?
- [senior] What goes wrong in production when flow test assertions fail because emissions arrive out of order due to uncoordinated dispatchers?
what-interviewers-listen-for:
- Turbine provides a clean DSL (`flow.test { awaitItem(); awaitComplete() }`) for asserting flow emissions.
- Testing flows without timeout protection or proper collectors hangs unit tests when flows never complete.
build-exercise: Create a flow emitting three numbers, and use Turbine (`flow.test { assertThat(awaitItem()).isEqualTo(1); ... }`) to verify each item in order.
official-references:
- Turbine flow testing library | https://github.com/cashapp/turbine
- Test Kotlin flows on Android | https://developer.android.com/kotlin/flow/test-and-consume
common-wrong-beliefs:
- Collecting a standard `StateFlow` inside a unit test using `.collect { }` returns control to the next line of code immediately | `collect` is a suspending call that never returns for hot flows, hanging your unit test unless canceled or tested via Turbine.

## testing/testing-viewmodels/test-viewmodel-state
interview-questions:
- [intern] How do you test that a ViewModel transitions correctly from loading to success state?
- [junior] How do you test ViewModels exposed via `StateFlow` using Turbine or test state collectors?
- [mid] Why must you configure the main dispatcher rule (`TestDispatcherRule`) in ViewModel unit tests?
- [senior] What goes wrong in production when ViewModel tests fail to verify initial state emissions or concurrency edge cases?
what-interviewers-listen-for:
- ViewModel state tests verify that calling an action triggers expected `StateFlow` emissions.
- Setting up a JUnit Rule or extension for `Dispatchers.setMain` ensures clean test execution per test method.
build-exercise: Write a unit test for a ViewModel using Turbine, call a load function, and assert that state transitions from `Loading` to `Success`.
official-references:
- Test ViewModel state | https://developer.android.com/topic/libraries/architecture/viewmodel#testing
common-wrong-beliefs:
- ViewModel unit tests require running an actual Android Activity and Android UI instrumentation test | ViewModels contain pure business logic and state management, making them fully testable via fast local JVM unit tests.

## testing/testing-viewmodels/test-the-error-path
interview-questions:
- [intern] How do you test how your ViewModel and UI handle network failures or error exceptions?
- [junior] How do you configure a Fake repository to throw an exception when testing error handling?
- [mid] Why is testing error states and retry mechanisms as important as testing the happy path?
- [senior] What goes wrong in production when teams only test happy-path data flows, leaving error states unhandled and crashing the app on network drops?
what-interviewers-listen-for:
- Testing error paths verifies that repositories throwing exceptions result in error UI states (`UiState.Error`).
- Fakes configured to throw exceptions simulate network failures deterministically.
build-exercise: Configure a Fake repository to throw an IOException, trigger it from a ViewModel test, and assert that the state updates to an error message.
official-references:
- Test error handling in ViewModels | https://developer.android.com/topic/libraries/architecture/viewmodel#testing
common-wrong-beliefs:
- Testing error paths is difficult because you need to physically disconnect your computer's internet router during tests | Fakes and mocks allow simulating network and database exceptions instantly in unit tests.

## testing/device-tests/local-vs-device-tests
interview-questions:
- [intern] What is the difference between local unit tests (`src/test`) and instrumented device tests (`src/androidTest`)?
- [junior] When must a test run as an instrumented test on a real device or emulator instead of a local JVM?
- [mid] What is the trade-off between instrumented tests (high fidelity, slow execution) and local unit tests (fast, mocked)?
- [senior] What goes wrong in production when relying entirely on slow instrumented tests for every single business logic check, slowing CI pipelines to a crawl?
what-interviewers-listen-for:
- Local tests run on the JVM (fast); instrumented tests run on Android devices/emulators (slow, high fidelity with real OS services).
- Business logic belongs in local unit tests; UI rendering and OS integration belong in instrumented tests.
build-exercise: Compare execution times between a local JVM test and an instrumented AndroidJUnit4 test in Android Studio.
official-references:
- Types of tests | https://developer.android.com/training/testing/fundamentals#test-types
common-wrong-beliefs:
- All unit tests should be written as instrumented device tests so they run in a real Android environment | Instrumented tests are slow and resource-intensive; business logic should be tested in fast local JVM unit tests.

## testing/ui-tests/test-a-composable
interview-questions:
- [intern] How do you write a UI test for a Jetpack Compose component using `ComposeTestRule` (`createComposeRule()`)?
- [junior] How do you find Composable nodes on screen using matchers like `hasText()`, `hasClickAction()`, or `hasTestTag()`?
- [mid] How do you simulate user interactions like clicks, text input, and scrolling in Compose UI tests?
- [senior] What goes wrong in production when Compose UI tests rely on unstable strings or layout text that changes during internationalization?
what-interviewers-listen-for:
- `ComposeTestRule` launches composables in an isolated test environment.
- Matchers locate nodes (`onNodeWithTag`, `onNodeWithText`), and actions (`performClick`, `performTextInput`) simulate user input.
- Using `testTag` (`Modifier.testTag`) instead of hardcoded strings prevents internationalization test breaks.
build-exercise: Write a Compose UI test that sets content to a button, clicks it using `performClick()`, and asserts that a text counter increments.
official-references:
- Test Jetpack Compose UI | https://developer.android.com/develop/ui/compose/testing
common-wrong-beliefs:
- Compose UI tests require running the entire app and navigating through multiple screens to test a single button | `ComposeTestRule` allows testing individual Composable functions in isolation without launching the whole app.

## testing/ui-tests/find-nodes-with-semantics
interview-questions:
- [intern] How does a Compose UI test locate elements on the screen using semantic matchers?
- [junior] What is the difference between matching by text (`hasText`) versus matching by test tag (`hasTestTag`)?
- [mid] Why is `testTag` recommended over text matching for automated UI testing in multilingual apps?
- [senior] What goes wrong in production when UI test locators break every time a copywriter updates button text or strings?
what-interviewers-listen-for:
- Semantic matchers query the Compose semantics tree for nodes matching specified criteria.
- Text matching breaks when strings change or localize; `testTag` provides stable test locators.
build-exercise: Add `Modifier.testTag("submit_button")` to a button, and locate it in a UI test using `onNodeWithTag("submit_button")`.
official-references:
- Semantics in Compose testing | https://developer.android.com/develop/ui/compose/testing#finders
common-wrong-beliefs:
- Matching UI elements by visible screen text in automated tests is best practice because it tests what the user sees | Text-based finders break when UI copy changes or localizes into other languages; `testTag` provides stable locators.

## testing/ui-tests/test-a-screen-with-fake-state
interview-questions:
- [intern] How do you test an entire Composable screen by passing fake UI state directly without a real ViewModel or network?
- [junior] Why does state hoisting make whole-screen UI testing straightforward and deterministic?
- [mid] How do you verify that clicking a button on a stateless screen invokes the expected event lambda callback?
- [senior] What goes wrong in production when screens are tightly coupled to ViewModels, making headless unit testing impossible?
what-interviewers-listen-for:
- Stateless screens accept state models and event lambdas as parameters, allowing tests to inject fake state and assert event callbacks.
- Decoupling screens from ViewModels enables fast, reliable UI tests.
build-exercise: Write a UI test for a stateless screen composable, passing a fake `UiState.Success` object and verifying that clicking a button triggers the lambda callback.
official-references:
- Test composable screens | https://developer.android.com/develop/ui/compose/testing#testing-state
common-wrong-beliefs:
- Testing a screen requires instantiating its real ViewModel and making real database or network calls | Stateless screens should be tested by injecting fake UI state directly, avoiding ViewModels and network dependencies in UI tests.

## testing/screenshot-tests/catch-a-visual-change
interview-questions:
- [intern] What is a screenshot test (visual regression test), and how does it catch unintended UI regressions?
- [junior] How do screenshot testing libraries (like Paparazzi or Roborlectric screenshot testing) capture rendered composables on the JVM?
- [mid] How do golden image comparisons work, and how do you handle intentional UI design updates?
- [senior] What goes wrong in production when screenshot tests fail across different operating systems or font rendering engines due to pixel-level discrepancies?
what-interviewers-listen-for:
- Screenshot tests render composables into bitmap images and compare them against approved "golden" baseline images.
- Running screenshot tests on the JVM (via Paparazzi) avoids emulator startup overhead.
- Pixel rendering discrepancies across host OS machines require running screenshot checks in standardized CI container environments.
build-exercise: Set up a screenshot test for a Composable card using Paparazzi or Roborlectric, capture the golden image, and verify baseline comparison.
official-references:
- Paparazzi screenshot testing | https://github.com/cashapp/paparazzi
common-wrong-beliefs:
- Screenshot tests can only be executed by running slow emulators on connected devices | Modern JVM-based screenshot testing tools render composables directly on the host JVM without needing emulators.

## testing/flaky-tests/why-a-test-flakes
interview-questions:
- [intern] What is a flaky test, and why are flaky tests toxic to a software engineering team's CI/CD pipeline?
- [junior] Name three common causes of flakiness in Android UI and coroutine tests (e.g., hardcoded `Thread.sleep`, race conditions, unawaited animations).
- [mid] Why do hardcoded delays (`delay(1000)` or `Thread.sleep`) cause flaky tests under high CI server load?
- [senior] What goes wrong in production and CI when engineering teams ignore flaky tests by simply re-running failing builds until they pass?
what-interviewers-listen-for:
- Flaky tests pass sometimes and fail sometimes without code changes, destroying trust in CI/CD pipelines.
- Causes include hardcoded timers, uncoordinated coroutines, network dependencies, and idling resource stalls.
- Re-running flaky tests masks underlying race conditions.
build-exercise: Write a test with a hardcoded `Thread.sleep` waiting for a background task, explain why it's flaky under load, and refactor it to use proper coroutine test synchronization.
official-references:
- Flaky tests best practices | https://developer.android.com/training/testing/fundamentals#flaky-tests
common-wrong-beliefs:
- A test that fails 1% of the time due to timing issues is acceptable as long as re-running the CI build fixes it | Flaky tests erode trust in test suites and hide genuine race conditions.

## testing/flaky-tests/make-a-test-reliable
interview-questions:
- [intern] How do you refactor a flaky, time-dependent test into a reliable, deterministic test?
- [junior] How do Idling Resources or coroutine test dispatchers eliminate race conditions in asynchronous tests?
- [mid] Why is replacing artificial sleep delays with explicit state observation or virtual time advancement essential?
- [senior] What goes wrong in production when asynchronous test assertions check intermediate states instead of final stable conditions?
what-interviewers-listen-for:
- Replace time-based sleeps with deterministic test dispatchers, virtual time, and explicit flow emissions.
- Ensure all coroutine jobs complete before asserting final states.
build-exercise: Refactor a flaky time-sleep test to use Turbine and `runTest` virtual time, ensuring 100% deterministic execution.
official-references:
- Make tests reliable | https://developer.android.com/kotlin/coroutines/test#reliability
common-wrong-beliefs:
- Adding longer `Thread.sleep` durations fixes flaky asynchronous tests permanently | Increasing sleep time only masks race conditions; proper coroutine test synchronization and virtual time are required.

## testing/putting-it-together/review-generated-tests
interview-questions:
- [intern] In a code review, what red flags do you look for in AI-generated or developer-written unit tests?
- [junior] Why is a test with zero assertions or tests that catch all exceptions (`try/catch` swallowing failures) invalid?
- [mid] How do you spot tests that mock everything and test nothing (mocking concrete data classes or library internals)?
- [senior] What goes wrong in production when accepting generated test suites that test mock behavior rather than real production code logic?
what-interviewers-listen-for:
- Spotting missing assertions, over-mocking, untracked coroutine scopes, and tests that pass vacuously.
- Ensuring tests verify public behavior contracts rather than mock invocation counts.
build-exercise: Review a flawed AI-generated test file containing missing assertions and over-mocking, and refactor it into a clean, robust test suite.
official-references:
- Test review guidelines | https://developer.android.com/training/testing/fundamentals
common-wrong-beliefs:
- Any test generated by AI or tools that turns green in CI is automatically a high-quality, reliable test | Generated tests frequently contain weak assertions or mock trivial behavior without testing actual business logic.

## testing/putting-it-together/prove-a-bug-is-fixed
interview-questions:
- [intern] What is the test-first approach to fixing a production bug (reproduce with a failing test first)?
- [junior] How do you write a regression test that fails when the bug is present and passes once the fix is applied?
- [mid] Why does proving a bug with a failing test before writing the fix guarantee that your fix actually addresses the root cause?
- [senior] What goes wrong in production when bug fixes are shipped without regression tests, leading to recurring bugs in future releases?
what-interviewers-listen-for:
- Write a failing unit test reproducing the bug report (Red), apply the code fix (Green), and refactor.
- Regression tests prevent the same bug from reappearing in subsequent releases.
build-exercise: Take a broken function with a known edge-case bug, write a unit test exposing the failure, apply the fix, and verify the test turns green.
official-references:
- Fix bugs with regression tests | https://developer.android.com/training/testing/fundamentals#bug-fixing
common-wrong-beliefs:
- Fixing a bug directly in code and manually tapping through the screen is sufficient proof that the bug is fully resolved | Writing a failing regression test first proves the bug existed and ensures it will never regress.

## Self-check
1. Test your app | https://developer.android.com/training/testing (Verify exact URL path)
2. Run local unit tests | https://developer.android.com/training/testing/local-tests (Verify exact URL path)
3. Google Truth assertion library | https://truth.dev/ (Verify URL structure)
4. Test Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines/test (Verify path)
5. Turbine flow testing library | https://github.com/cashapp/turbine (Verify GitHub URL)
6. Test ViewModel state | https://developer.android.com/topic/libraries/architecture/viewmodel#testing (Verify anchors)
7. Test Jetpack Compose UI | https://developer.android.com/develop/ui/compose/testing (Verify path)
8. Paparazzi screenshot testing | https://github.com/cashapp/paparazzi (Verify GitHub URL)
9. Types of tests | https://developer.android.com/training/testing/fundamentals#test-types (Verify anchors)
10. Fix bugs with regression tests | https://developer.android.com/training/testing/fundamentals#bug-fixing (Verify anchors)