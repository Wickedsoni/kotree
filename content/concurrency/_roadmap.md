```yaml
branch: concurrency
bough: kotlin-android
stems:
  - id: why-the-main-thread
    title: "Why the Main Thread Matters"
    why: "Every screen is drawn and every tap is handled by one thread, so a fresher must know why a slow job on it freezes the app."
    leaves:
      - id: what-the-main-thread-does
        title: "Which thread draws my screen and runs my click code?"
        difficulty: easy
        requires: []
        fruit: false
      - id: why-blocking-freezes-the-screen
        title: "Why does one slow job freeze the whole screen?"
        difficulty: medium
        requires: [concurrency/why-the-main-thread/what-the-main-thread-does]
        fruit: false
  - id: threads
    title: "Threads"
    why: "Coroutines only make sense once a fresher has seen a plain thread do the job and seen where it gets awkward."
    leaves:
      - id: running-work-on-another-thread
        title: "How do I run a slow job beside the main thread?"
        difficulty: easy
        requires: [concurrency/why-the-main-thread/why-blocking-freezes-the-screen]
        fruit: false
      - id: why-raw-threads-get-messy
        title: "What goes wrong when I use plain threads for everything?"
        difficulty: medium
        requires: [concurrency/threads/running-work-on-another-thread]
        fruit: false
  - id: coroutine-basics
    title: "Coroutine Basics"
    why: "Coroutines are how modern Android code waits for work without freezing, and every later leaf builds on them."
    leaves:
      - id: what-is-a-coroutine
        title: "How is a coroutine different from a thread?"
        difficulty: medium
        requires: [concurrency/threads/why-raw-threads-get-messy]
        fruit: false
      - id: launch-a-coroutine
        title: "How do I start a job that runs beside my code with launch?"
        difficulty: easy
        requires: [concurrency/coroutine-basics/what-is-a-coroutine]
        fruit: false
      - id: async-and-await
        title: "How do I run two jobs at the same time and use both answers?"
        difficulty: medium
        requires: [concurrency/coroutine-basics/launch-a-coroutine]
        fruit: false
  - id: suspend
    title: "Suspend Functions"
    why: "The word suspend appears in almost every modern Android data call, and misreading it causes freezes."
    leaves:
      - id: what-suspend-means
        title: "What happens to the thread when a suspend function has to wait?"
        difficulty: hard
        requires: [concurrency/coroutine-basics/async-and-await]
        fruit: false
      - id: suspend-vs-blocking
        title: "Why does delay keep the screen alive while Thread.sleep freezes it?"
        difficulty: medium
        requires: [concurrency/suspend/what-suspend-means, concurrency/why-the-main-thread/why-blocking-freezes-the-screen]
        fruit: false
  - id: dispatchers
    title: "Dispatchers"
    why: "Choosing where a coroutine runs decides whether the screen stays smooth, and generated code often gets it wrong."
    leaves:
      - id: main-io-and-default
        title: "Which dispatcher should this job run on?"
        difficulty: medium
        requires: [concurrency/suspend/suspend-vs-blocking]
        fruit: false
      - id: main-safe-functions
        title: "How do I write a suspend function that is safe to call from the main thread?"
        difficulty: medium
        requires: [concurrency/dispatchers/main-io-and-default]
        fruit: false
  - id: scopes
    title: "Scopes"
    why: "A coroutine launched in the wrong scope leaks, runs after the screen is gone, or never stops."
    leaves:
      - id: what-is-a-coroutine-scope
        title: "Who owns a coroutine, and what happens when the owner goes away?"
        difficulty: medium
        requires: [concurrency/coroutine-basics/launch-a-coroutine, concurrency/dispatchers/main-safe-functions]
        fruit: false
      - id: parent-and-child-coroutines
        title: "Why does a parent coroutine wait for its children?"
        difficulty: hard
        requires: [concurrency/scopes/what-is-a-coroutine-scope, concurrency/coroutine-basics/async-and-await]
        fruit: false
      - id: scopes-in-android-code
        title: "Which scope should a ViewModel, a screen and a button use?"
        difficulty: medium
        requires: [concurrency/scopes/what-is-a-coroutine-scope]
        fruit: false
  - id: cancellation-and-errors
    title: "Cancellation and Errors"
    why: "Stopping work and handling failure correctly separates a stable app from one that leaks, crashes or keeps running."
    leaves:
      - id: cancel-is-cooperative
        title: "Why does cancel() not stop my loop?"
        difficulty: hard
        requires: [concurrency/scopes/parent-and-child-coroutines]
        fruit: false
      - id: exceptions-in-coroutines
        title: "Why does my try/catch around launch not catch the crash?"
        difficulty: hard
        requires: [concurrency/scopes/parent-and-child-coroutines, concurrency/coroutine-basics/async-and-await]
        fruit: false
      - id: supervisor-and-sibling-failure
        title: "How can one failed job leave its siblings running?"
        difficulty: medium
        requires: [concurrency/scopes/parent-and-child-coroutines, concurrency/cancellation-and-errors/exceptions-in-coroutines]
        fruit: false
      - id: never-swallow-cancellation
        title: "Why must I never hide a CancellationException?"
        difficulty: medium
        requires: [concurrency/cancellation-and-errors/cancel-is-cooperative, concurrency/cancellation-and-errors/exceptions-in-coroutines]
        fruit: false
  - id: flow-basics
    title: "Flow Basics"
    why: "Screens react to values that change over time, and Flow is how Android code hands those values over."
    leaves:
      - id: what-is-a-flow
        title: "How can a function hand back many values over time instead of one?"
        difficulty: medium
        requires: [concurrency/suspend/what-suspend-means]
        fruit: false
      - id: flows-are-cold
        title: "When does the code inside flow { } actually run?"
        difficulty: medium
        requires: [concurrency/flow-basics/what-is-a-flow]
        fruit: false
      - id: collect-and-cancel
        title: "Why does the line after collect never run?"
        difficulty: medium
        requires: [concurrency/flow-basics/flows-are-cold, concurrency/scopes/what-is-a-coroutine-scope]
        fruit: false
  - id: flow-operators
    title: "Flow Operators"
    why: "Real screens combine, delay and reshape several streams, and typical generated code chains these operators."
    leaves:
      - id: transform-a-flow
        title: "How do I change the values of a flow on their way to the screen?"
        difficulty: easy
        requires: [concurrency/flow-basics/collect-and-cancel]
        fruit: false
      - id: flow-on-and-context
        title: "On which thread does each step of a flow run?"
        difficulty: medium
        requires: [concurrency/flow-operators/transform-a-flow, concurrency/dispatchers/main-io-and-default]
        fruit: false
      - id: combine-flows
        title: "How do I build one screen value from two flows?"
        difficulty: medium
        requires: [concurrency/flow-operators/transform-a-flow]
        fruit: false
      - id: search-as-you-type
        title: "How do I search while the user types without sending a request per letter?"
        difficulty: hard
        requires: [concurrency/flow-operators/combine-flows, concurrency/cancellation-and-errors/cancel-is-cooperative]
        fruit: false
  - id: state-and-shared-flow
    title: "StateFlow and SharedFlow"
    why: "Screen state and one-time events travel through these two flow types, and mixing them up causes repeated messages."
    leaves:
      - id: state-flow
        title: "How does a StateFlow always know the latest value?"
        difficulty: medium
        requires: [concurrency/flow-basics/collect-and-cancel]
        fruit: false
      - id: one-off-events
        title: "How do I show a message once and not again after rotation?"
        difficulty: hard
        requires: [concurrency/state-and-shared-flow/state-flow]
        fruit: false
      - id: share-a-cold-flow
        title: "How do I share one cold flow with many collectors?"
        difficulty: medium
        requires: [concurrency/state-and-shared-flow/state-flow, concurrency/flow-basics/flows-are-cold]
        fruit: false
  - id: channels
    title: "Channels"
    why: "A fresher meets channels when reading flow and library code, and should know what they pass between coroutines."
    leaves:
      - id: channels-between-coroutines
        title: "How do two coroutines pass items to each other one at a time?"
        difficulty: medium
        requires: [concurrency/coroutine-basics/async-and-await, concurrency/flow-basics/flows-are-cold]
        fruit: false
  - id: shared-mutable-state
    title: "Shared Mutable State"
    why: "Two coroutines changing one value at the same time silently lose updates, the hardest bug to see in code."
    leaves:
      - id: race-conditions-in-coroutines
        title: "Why does a counter changed by many coroutines end up too small?"
        difficulty: hard
        requires: [concurrency/coroutine-basics/launch-a-coroutine, concurrency/dispatchers/main-io-and-default]
        fruit: false
  - id: putting-it-together
    title: "Putting It Together"
    why: "The checkpoint ability is fixing a frozen screen and judging generated async code, so the branch ends on both."
    leaves:
      - id: fix-a-frozen-screen
        title: "How do I find why a screen freezes and choose the right fix?"
        difficulty: medium
        requires: [concurrency/why-the-main-thread/why-blocking-freezes-the-screen, concurrency/dispatchers/main-safe-functions, concurrency/scopes/scopes-in-android-code]
        fruit: false
      - id: review-generated-async-code
        title: "Would I accept this generated async code in review?"
        difficulty: hard
        requires: [concurrency/putting-it-together/fix-a-frozen-screen, concurrency/cancellation-and-errors/never-swallow-cancellation]
        fruit: true
```

## Branch prerequisites

All 32 leaves are `shadow` on 18 Oct 2026. Nothing is published. The branch is phase 4. Android framework code does not run in the browser editor and there is no Android emulator or simulator (L15, D8). Android-behaviour sims use `code-to-screen`: the learner flips between pre-written code variants and watches a drawn screen and timeline change.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu` (phase 1) — used by what-the-main-thread-does (one worker doing one job at a time).
- Assumes Foundation: `foundation/programming-thinking/trace-code-by-hand` (phase 1) — used by every Predict-the-output task on ordering.
- Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing` (phase 1) — used by fix-a-frozen-screen.
- Assumes Kotlin: `kotlin/lambdas/lambda-syntax` and `kotlin/lambdas/higher-order-functions` (phase 2) — `launch { }`, `async { }` and `collect { }` all take a lambda.
- Assumes Kotlin: `kotlin/lambdas/closures` (phase 2) — used by race-conditions-in-coroutines (a lambda sharing one variable).
- Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references` (phase 2) — used by race-conditions-in-coroutines and state-flow.
- Assumes Kotlin: `kotlin/errors-and-results/try-catch-finally` (phase 3) — used by exceptions-in-coroutines and never-swallow-cancellation.
- Assumes Kotlin: `kotlin/errors-and-results/result-and-runcatching` (phase 3) — never-swallow-cancellation shows why `runCatching` also needs care.
- Assumes Kotlin: `kotlin/lambdas/map-filter-and-friends` (phase 2) — transform-a-flow reuses the list-operator picture.
- Assumes Android core: `android-core/lifecycle/what-is-the-lifecycle` (phase 3) — used by scopes-in-android-code and one-off-events.
- Assumes Android core: `android-core/configuration-changes/why-rotation-restarts` (phase 3) — used by one-off-events.
- Helps to know OS: `os/threads/what-is-a-thread` (phase 3) — running-work-on-another-thread recaps what a thread is in one paragraph.
- Helps to know OS: `os/synchronisation/race-conditions` (phase 4) — race-conditions-in-coroutines recaps a race in one paragraph.
- Helps to know Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel` (phase 4, later in the tie-break order) — scopes-in-android-code recaps `viewModelScope` in one paragraph; no leaf depends on it.
- Helps to know UI: `ui/state-and-recomposition/what-is-recomposition` (phase 3) — state-flow recaps how a screen redraws when a value changes.
- Helps to know UI: `ui/side-effects/launched-effect`, `ui/side-effects/coroutine-from-a-click` and `ui/side-effects/collecting-flows` (phase 5) — `LaunchedEffect`, `rememberCoroutineScope` and `collectAsStateWithLifecycle` are taught there; this branch names them only.

Foundation is recommended first ("Start here"), then Kotlin, then the Android core and UI phase-3 stems. The UI side-effects stem and Android core background-work stem assume this branch (`ui/_roadmap.md` and `android-core/_roadmap.md` cite `coroutine-basics`, `scopes`, `cancellation-and-errors`, `flow-basics` and `why-the-main-thread`; those slugs are kept exactly).

Challenge note: kotlinx.coroutines code runs only if the embedded Kotlin Playground includes the library. ⚠️ VERIFY on kotlinlang.org that the Playground resolves `kotlinx.coroutines` and which version, before any leaf goes live. Android framework code (ViewModel scopes, lifecycle) is a plain-Kotlin model or a read-only "which line is wrong" task.

Facts checked against official docs on 18 Oct 2026, to be rechecked by `reviewer` before each leaf goes live:

- ANR input timeout: "5 seconds" when the app has a foreground activity (developer.android.com/topic/performance/vitals/anr). The page gives no number for apps without a foreground activity; do not invent one.
- Coroutines best practices (developer.android.com/kotlin/coroutines/coroutines-best-practices): do not hardcode `Dispatchers`; suspend functions should be main-safe; avoid `GlobalScope`; do not consume `CancellationException`, and prefer specific exception types over `Exception` or `Throwable`; expose immutable types.

## Stems

### why-the-main-thread · Why the Main Thread Matters

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-the-main-thread-does` | easy | "Which thread draws my screen and runs my click code?" | A shop with one cashier who serves customers and also tidies the window; every job joins a single queue. | "Each button has its own thread, so taps never wait for each other." | `sim: code-to-screen` a drawn phone and a queue of tasks at one counter; the learner swaps a click handler between short and long variants and watches the next frame slot and taps wait. | `P:` in what order do three queued tasks finish (plain-Kotlin model) · `B:` add a second task in front of the draw task | Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`. |
| `why-blocking-freezes-the-screen` | medium | "Why does one slow job freeze the screen, and what does the user see?" | The cashier is sent to count the stockroom: the queue stands still and customers leave. | "Freezing only happens on slow phones or with huge data." | `sim: code-to-screen` same phone and queue as the previous leaf; the learner lengthens a loop in the click handler and the drawn screen stops updating while a timeline bar turns red at the app-not-responding mark. | `P:` how many frames are skipped by a 1-second loop in the model · `F:` a click handler that reads a big file inline · `B:` shorten the job until the screen stays smooth | volatile. Checked 18 Oct 2026 (developer.android.com ANR page): input not handled within 5 seconds, when the app has a foreground activity, triggers an ANR. ⚠️ VERIFY again before live. Helps to know Foundation: `foundation/debugging-mindset/reading-error-messages`. |

### threads · Threads

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `running-work-on-another-thread` | easy | "How do I run a slow job so the main thread keeps going?" | Opening a second counter with a second cashier for the heavy orders. | "A new thread makes the job itself faster." | `none:` the print order of a main program and a thread in the Playground shows the idea; the counter picture is already in `what-the-main-thread-does`. | `P:` which line prints first after `thread { }` starts (Playground) · `F:` a slow loop run on the main thread of a console program that blocks later prints · `B:` start ten threads | Helps to know OS: `os/threads/what-is-a-thread`. |
| `why-raw-threads-get-messy` | medium | "What goes wrong when I use plain threads for everything?" | Hiring a temp worker for every small errand with no manager: nobody knows who has finished or how to call them back. | "More threads always means more speed." | `none:` three threads printing in a jumbled order in the Playground, plus a three-pain checklist, show it better than motion. | `P:` order of results from three threads that sleep for different times · `F:` a result written to a variable from a thread and read too early · `B:` start a thread that you cannot stop | Names the three pains: handing results back, stopping a thread, cost per thread. |

### coroutine-basics · Coroutine Basics

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-coroutine` | medium | "How is a coroutine different from a thread?" | A waiter who takes your order, serves other tables while the kitchen cooks, and returns when the food is ready; the restaurant does not hire one waiter per table. | "A coroutine is just a lighter thread." | `sim:` a few waiter balls (threads) and many order tickets (coroutines); tickets pause while waiting and are picked up by any free waiter. | `P:` how many threads exist while 1000 tickets wait (model) · `B:` make every ticket hold its waiter while waiting and see the waiters run out | ⚠️ VERIFY Playground runs kotlinx.coroutines. |
| `launch-a-coroutine` | easy | "How do I start a job with launch and carry on with my own code?" | Handing an order slip to the kitchen and walking on, keeping the ticket stub. | "`launch` waits for the job and then returns." | `none:` one rule about print order; running `launch` with prints before and after it in the Playground shows it. | `P:` print order of a `launch` and the line after it · `F:` a program that ends before the launched job prints · `B:` call `join` on the returned job | Assumes Kotlin: `kotlin/lambdas/higher-order-functions`. Uses `runBlocking` once, only to keep the Playground program alive. |
| `async-and-await` | medium | "How do I run two jobs at the same time and use both answers?" | Sending two people to two shops and waiting at the door for both bags. | "`async` and `await` on the next line is faster than doing the jobs one after another." | `sim:` two shop trips on a timeline; when `await` sits right after each `async` the bars run one after another, when both start first they overlap, and total time shrinks. | `P:` total time of two 1-second jobs started in two different ways · `F:` two independent calls awaited one after the other so the total is the sum · `B:` await a job twice | — |

### suspend · Suspend Functions

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-suspend-means` | hard | "What happens to the thread when a suspend function has to wait?" | A bookmark in a book: the reader can leave and read another book, and later opens the same page. | "`suspend` makes a function run on another thread." | `sim:` a thread ball walks a function's lines; at a suspension point the function parks on a shelf and the thread walks away to other work; when the wait ends any free thread resumes from the same line. | `P:` which thread prints before and after a suspension point (model) · `F:` a regular function that calls a suspend function · `B:` remove `suspend` from a function that waits | Hard because it needs the "thread is freed" picture. Reuse across later leaves. |
| `suspend-vs-blocking` | medium | "Why does delay keep the screen alive while Thread.sleep freezes it?" | Waiting in a café with a buzzer, free to walk around, versus standing at the till so nobody else is served. | "`delay` and `Thread.sleep` do the same thing with different names." | `sim: code-to-screen` same phone as `why-blocking-freezes-the-screen`; flip a line between `Thread.sleep` and `delay` and watch the drawn screen freeze or keep animating. | `P:` which of two programs prints on time (Playground) · `F(ai):` a tidy function that uses `Thread.sleep` inside a coroutine · `B:` swap the two calls | Assumes Foundation: `foundation/programming-thinking/trace-code-by-hand`. |

### dispatchers · Dispatchers

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `main-io-and-default` | medium | "Which dispatcher should a file read, a sort and a screen update each use?" | Three staff pools in a shop: the front desk, the warehouse crew for fetching, the back-office team for sums. | "`Dispatchers.IO` is the safe choice for everything." | `none:` a choice rule; a three-row table plus printing the thread name per dispatcher in the Playground is clearer than motion. | `P:` which pool runs a job after `withContext(Dispatchers.Default)` (model) · `F(ai):` a confident function that sorts a big list on Main · `B:` move the file read to Main | Dispatcher names only; custom dispatchers and limited parallelism are in `## Later`. |
| `main-safe-functions` | medium | "How do I write a suspend function that is safe to call from the main thread?" | A lift button: you press it and the lift crew decide who carries the boxes; the front desk never does. | "The caller must remember to switch threads before calling." | `sim: code-to-screen` a screen calls a function; flip between a version that moves the work with `withContext` and one that does not, and watch the drawn screen freeze or stay smooth. | `P:` which thread a function runs on after `withContext` returns (model) · `F(ai):` a repository function that reads a file on whichever thread called it · `F(ai):` a hardcoded `Dispatchers.IO` that makes a test slow or flaky · `B:` call it from Main | Checked 18 Oct 2026 (coroutines best practices): suspend functions should be main-safe, and `Dispatchers` should be injected, not hardcoded. Injection itself is taught in `architecture-data`. |

### scopes · Scopes

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-coroutine-scope` | medium | "Who owns a coroutine, and what happens when the owner goes away?" | A class register: when the lesson ends, every name on the register goes home. | "A coroutine runs until its code finishes, whatever happens around it." | `sim:` coroutine bars hang from a scope bar on a timeline; cancelling the scope cuts every bar, and a coroutine launched outside any scope keeps running. | `P:` which prints appear after the scope is cancelled (Playground) · `F(ai):` a launch in `GlobalScope` that outlives its owner · `B:` cancel the scope at different moments | Assumes Kotlin: `kotlin/lambdas/closures`. Checked 18 Oct 2026: `GlobalScope` is discouraged in the coroutines best practices page. |
| `parent-and-child-coroutines` | hard | "Why does a parent coroutine wait for its children, and what happens to them if it is cancelled?" | A group tour: the guide cannot end the tour until every member is back, and if the tour is called off everyone is told. | "A coroutine started inside another is completely independent." | `sim:` a parent bar with child bars; the parent bar stays open until the last child ends; cancel the parent and children cut; a child failure shoots up to the parent and across to siblings. | `P:` when "done" prints for a parent with two children of different lengths · `F(ai):` a function that launches children in a fresh scope so the caller never waits · `F(ai):` a `launch` inside `coroutineScope` that is expected to outlive it · `B:` cancel one child | Q1–Q4 all apply: one of the hardest leaves in the branch. Structured concurrency is the key idea. |
| `scopes-in-android-code` | medium | "Which scope should a ViewModel, a screen and a button use?" | A rental lease: when the lease ends (screen leaves) what you left in the flat is cleared, but a rented storage unit nobody empties (`GlobalScope`) keeps filling. | "`GlobalScope` is fine for a quick button action." | `sim: code-to-screen` a phone screen and three coroutine bars tied to Activity, ViewModel and Global scopes; the learner closes the screen or rotates and watches which bars survive. | `P:` which of three jobs keeps running after the screen closes · `F(ai):` a button handler that launches in `GlobalScope` · `F(ai):` a ViewModel that creates its own `CoroutineScope` and never cancels it · `B:` rotate the phone | volatile. Assumes Android core: `android-core/lifecycle/what-is-the-lifecycle`. Names `viewModelScope`, `lifecycleScope`, `rememberCoroutineScope`. ⚠️ VERIFY current scope names and artifacts. Checked 18 Oct 2026 (ViewModel guide): `viewModelScope` follows the ViewModel's lifecycle. |

### cancellation-and-errors · Cancellation and Errors

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `cancel-is-cooperative` | hard | "Why does cancel() not stop my loop?" | A polite note saying "please stop" left on a desk: a worker with head down never reads it, one who looks up regularly does. | "`cancel()` kills the coroutine instantly." | `sim:` a worker ball on a belt and a "stop" flag; a loop that never checks keeps running past the flag; adding a check (`isActive`, `ensureActive`, a suspension point) lets it stop. | `P:` how many lines print after `cancel()` with and without a check · `F(ai):` a tidy heavy loop with no cancellation check · `B:` cancel during `Thread.sleep` | Names `ensureActive` and `isActive`. ⚠️ VERIFY API names against kotlinlang.org coroutines docs. |
| `exceptions-in-coroutines` | hard | "Why does my try/catch around launch not catch the crash?" | A smoke alarm that wakes the whole building, not only the room with the fire; a sign on your own door does not stop it. | "A try/catch around `launch` catches errors inside the launched code." | `none:` the error path is shown by the prints in the Playground and a static diagram of the parent chain; the moving version is already in `parent-and-child-coroutines`. | `P:` which prints appear when a child throws (Playground) · `F(ai):` a `try` around `launch` that gives a false sense of safety · `F(ai):` an `async` whose failure surfaces far from where it was written | Assumes Kotlin: `kotlin/errors-and-results/try-catch-finally`. Names `CoroutineExceptionHandler` once. ⚠️ VERIFY wording on kotlinlang.org "Coroutine exceptions handling". |
| `supervisor-and-sibling-failure` | medium | "How can one failed job leave its siblings running?" | A fire door between rooms: a fire in one room stays in that room. | "If one child fails, only that child is affected." | `none:` how many children finish with and without `supervisorScope` is printed in the Playground; the sibling picture is in `parent-and-child-coroutines`. | `P:` how many of three children finish when the second fails, with and without a supervisor · `F:` a screen loading three independent sections that all disappear when one request fails · `B:` put the supervisor in the wrong place | Names `supervisorScope` and `SupervisorJob`. |
| `never-swallow-cancellation` | medium | "Why must I never hide a CancellationException?" | A recall notice on a loudspeaker: if one worker covers their ears, the whole shift cannot go home. | "Catching `Exception` is always safe because it covers everything." | `none:` a rule; prints after `cancel()` with and without a catch-all in the Playground show it. | `P:` does the job stop after `cancel()` when a catch-all is inside · `F(ai):` neat retry code with `catch (e: Exception)` that never lets the job stop · `F(ai):` `runCatching` wrapped around a suspend call · `B:` rethrow the exception | Checked 18 Oct 2026 (coroutines best practices): do not catch `CancellationException`, or rethrow it; prefer specific exception types. Links to `kotlin/errors-and-results/result-and-runcatching`. |

### flow-basics · Flow Basics

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-flow` | medium | "How can a function hand back many values over time instead of one?" | A tap dripping into a cup: the cup (collector) sees each drop as it comes. | "A flow is just a List that fills up later." | `sim:` a tap drips balls into a cup along a timeline; a suspend function drops one ball and stops, a flow keeps dripping. | `P:` order of prints from a flow that emits three values with a delay · `F:` a function that returns a `List` where values should arrive one by one · `B:` emit slower than the collector | Uses a suspend lambda; Assumes Kotlin: `kotlin/lambdas/lambda-syntax`. |
| `flows-are-cold` | medium | "When does the code inside flow { } actually run?" | A restaurant kitchen that cooks only when an order arrives, and cooks again for each new order. | "The flow runs once when I create it, and everyone shares the result." | `none:` counting how many times "start" prints with two collectors in the Playground shows it. | `P:` how many times "start" prints with two collectors · `F:` a flow that makes a network call per collector · `B:` collect the same flow twice | The hot version comes in `share-a-cold-flow`. |
| `collect-and-cancel` | medium | "Why does the line after collect never run?" | Standing at a tap holding a cup: you cannot do anything else until the tap turns off, and if it never turns off you wait forever. | "`collect` hands me the values and moves on." | `none:` an endless flow and a print after `collect` in the Playground shows the block; no extra picture is needed after `what-is-a-flow`. | `P:` which line prints after collecting an endless flow · `F(ai):` two `collect` calls in a row where the second never runs · `B:` cancel the scope | Assumes Kotlin: `kotlin/lambdas/closures`. Android lifecycle-aware collection is taught in `ui/side-effects/collecting-flows`; named only here. |

### flow-operators · Flow Operators

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `transform-a-flow` | easy | "How do I change the values of a flow on their way to the screen?" | An assembly line with stations: one removes bad items, one paints, one counts. | "Operators run when I write them, not when someone collects." | `none:` the conveyor picture is already in `kotlin/lambdas/map-filter-and-friends`; printed output of a flow chain in the Playground is enough. | `P:` output of `map`, `filter`, `take` and `onEach` on a flow · `F:` `onEach` used where `map` was meant · `B:` swap `take` and `filter` | Assumes Kotlin: `kotlin/lambdas/map-filter-and-friends`. |
| `flow-on-and-context` | medium | "On which thread does each step of a flow run?" | A relay where upstream workers sit in another building; the finished parcel always lands on the collector's desk. | "`flowOn` changes the thread of the whole chain, including the steps after it." | `none:` printing the thread name inside each step in the Playground shows which step runs where; a static pipe diagram marks `flowOn`. | `P:` which pool each of three steps runs on · `F(ai):` a flow that reads a file on Main because `flowOn` sits in the wrong place · `B:` move `flowOn` upstream | Assumes `concurrency/dispatchers/main-io-and-default`. |
| `combine-flows` | medium | "How do I build one screen value from two flows?" | A mixing tap: hot and cold feed in, and the output changes whenever either changes. | "`combine` waits for both to finish before sending anything." | `none:` a timestamped print of each combined result in the Playground shows when it fires. | `P:` number of results from two flows with different emit counts · `F:` two collectors where one `combine` was meant · `B:` make one flow emit nothing | Mentions `zip` once, with the difference. |
| `search-as-you-type` | hard | "How do I search while the user types without sending a request per letter?" | A waiter who waits for you to finish your sentence, and drops the first order if you change your mind. | "`debounce` alone stops old requests from finishing." | `sim:` letters arrive on a timeline; `debounce` waits for a pause, then `flatMapLatest` cancels the previous request bar when a new one starts. | `P:` how many requests are sent for five keystrokes with a pause after the third · `F(ai):` a generated search that fires a request on every keystroke · `F(ai):` stale results arriving after newer ones · `B:` remove `flatMapLatest` | Assumes `concurrency/cancellation-and-errors/cancel-is-cooperative`. Names `debounce`, `flatMapLatest`, `distinctUntilChanged`. ⚠️ VERIFY whether any are still marked experimental on kotlinlang.org. |

### state-and-shared-flow · StateFlow and SharedFlow

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `state-flow` | medium | "How does a StateFlow always know the latest value?" | A scoreboard that shows only the latest score; anyone who walks in reads it straight away. | "A StateFlow keeps every value it was ever given." | `none:` a late collector and repeated equal updates printed in the Playground show the single-slot behaviour. | `P:` what a late collector receives after three updates · `F(ai):` a mutable list changed in place inside a `StateFlow` so no screen update happens · `B:` set the same value twice | Assumes Kotlin: `kotlin/classes-and-objects/two-kinds-of-equality` and `kotlin/classes-and-objects/read-only-is-not-immutable`. Helps to know UI: `ui/state-and-recomposition/what-is-recomposition`. ⚠️ VERIFY: kotlinx.coroutines StateFlow docs on equality-based conflation and `value` updates. |
| `one-off-events` | hard | "How do I show a message once and not again after rotation?" | A doorbell that rings once versus a signboard that shows the same message to every visitor. | "Putting a toast message in a `StateFlow` shows it exactly once." | `sim: code-to-screen` a phone and a message source; flip between state-held and event-style variants, rotate the phone, and watch the toast repeat, vanish or show once. | `P:` how many toasts show after one rotation, per variant · `F(ai):` a generated ViewModel that shows a snackbar from a `StateFlow` and shows it again after rotation · `B:` rotate before the message is collected | volatile. Assumes Android core: `android-core/configuration-changes/why-rotation-restarts`. ⚠️ VERIFY: current Android guidance on one-off UI events (state versus `SharedFlow`/`Channel`) on developer.android.com before this leaf is written. |
| `share-a-cold-flow` | medium | "How do I share one cold flow with many collectors?" | One lecture shown on TV for everyone instead of a separate teacher for each student. | "`stateIn` and `shareIn` make the flow run forever." | `none:` counting "start" prints before and after `stateIn` in the Playground shows one run feeding many collectors. | `P:` how many times "start" prints with three collectors before and after `stateIn` · `F(ai):` a repository flow with a network call per screen · `B:` change the start rule | volatile. ⚠️ VERIFY: `SharingStarted.WhileSubscribed` and its timeout value as recommended on developer.android.com; do not claim a number until checked. |

### channels · Channels

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `channels-between-coroutines` | medium | "How do two coroutines pass items to each other one at a time?" | A hand-off hatch between the kitchen and the counter: one plate at a time, and each plate goes to exactly one waiter. | "A channel is the same as a flow; every collector gets every item." | `none:` a niche tool; printed send and receive order in the Playground, with a one-line hatch diagram, is enough. | `P:` which receiver gets which item with two receivers · `F:` a sender that never closes the channel so the receiver waits forever · `B:` shrink the hatch to size zero | Actors are in `## Later`. Reading `callbackFlow` and `channelFlow` is named only. |

### shared-mutable-state · Shared Mutable State

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `race-conditions-in-coroutines` | hard | "Why does a counter changed by many coroutines end up too small?" | Two people updating one whiteboard total: both read 5, both write 6, and one update vanishes. | "`counter++` is one step, so it cannot be interrupted." | `sim:` many ticket balls read a shared jar, add one in their hands and put it back; interleaving turns two adds into one; the learner switches on a lock (Mutex), an atomic counter or confinement and the count becomes right. | `P:` the likely range of a counter after 1000 parallel increments · `F(ai):` tidy parallel code that updates a shared `var` from `Dispatchers.Default` · `F(ai):` a `StateFlow` updated with read-then-write instead of `update` · `B:` run the same code on a single thread | Assumes Kotlin: `kotlin/classes-and-objects/objects-are-references`. Names `Mutex`, `AtomicInteger`, `MutableStateFlow.update`. Helps to know OS: `os/synchronisation/race-conditions`. ⚠️ VERIFY API names. |

### putting-it-together · Putting It Together

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `fix-a-frozen-screen` | medium | "A screen freezes when I tap a button: what is the cause and which fix do I choose?" | A doctor's visit: symptoms first, then the cause, then the smallest treatment that works. | "A freeze means the phone is too slow, not the code." | `sim: code-to-screen` a phone with a frozen button and four code variants of the click handler; the learner picks the variant that keeps the drawn screen smooth and checks the timeline. | `F(ai):` a generated ViewModel function that reads a file inline from a click · `F(ai):` a blocking `runBlocking` inside a screen callback · `P:` which of three fixes still freezes · `B:` add a second slow call | Checkpoint ability: "Why a long task must not run on the main thread, and what a coroutine does instead" (`docs/01` §6.2). Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. |
| `review-generated-async-code` | hard | "Which three risks in this generated async file would stop me approving it?" | A final house inspection: walk every room, write down the three worst problems. | "If it runs on my phone, the concurrency is correct." | `none:` a review exercise on a real file; the editor and a checklist (scope, dispatcher, cancellation, errors, shared state) are the material. | `F(ai):` review a 100-line generated file and list 3 risks with line numbers · `F(ai):` fix one and show a run that proves it · `P:` predict what happens on rotation | Branch synthesis; fruit (last leaf, `docs/08` §12 F2). Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `concurrency/putting-it-together/review-generated-async-code` | "Unfreeze and review an async screen" | Start from a 100–150 line screen with a ViewModel that you did not write · find and show at least one freeze, one scope bug and one cancellation bug · fix them without hard-coded dispatchers or `GlobalScope` · every fix shows a run or a test that proves it · write what happens on rotation before and after · list which of your findings an AI assistant is most likely to repeat | Fixes a screen that freezes and explains why a coroutine is the cure (`docs/01` §6.2 concurrency ability, L24). |

## Later

- The select expression: rare outside library code.
- Actors: niche pattern; the channels leaf covers the idea.
- Custom dispatchers and limited parallelism: tuning, not first-app knowledge.
- RxJava: reading old code only, after the rest of the tree.
- Flow internals (how operators are built): library-author topic.
- Coroutine debugging tools: tooling depth.
- Testing coroutines: belongs in the testing branch, not here.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 13 stems, 32 leaves, all `shadow`. | +32 | Estimate 28, band 22–34: inside the band (+14 %). Over the sketch because: `exceptions-in-coroutines` and `supervisor-and-sibling-failure` are split (two different misconceptions); `search-as-you-type` and `flow-on-and-context` added to flow-operators because typical generated screens use them; `share-a-cold-flow` kept because repositories expose flows; a final stem `putting-it-together` added so the last leaf is a synthesis leaf and the fruit proves the `docs/01` §6.2 ability (F2). `shared-mutable-state` and `channels` stay at one leaf each. |
| 2026-10-18 | Difficulty mix: 4 easy, 20 medium, 8 hard (25 % hard). Sims: 16 of 32 leaves (50 %). | 0 | The mix skews to medium because nearly every concurrency leaf scores on Q2 (hidden timing) and Q4 (commonly wrong), against the 40/45/15 sanity target in `docs/08` §4. Hard share stays under 30 %. Sim share sits at the top of the 35–50 % guide. |
