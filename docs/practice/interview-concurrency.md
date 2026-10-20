# interview: concurrency

## concurrency/why-the-main-thread/what-the-main-thread-does
interview-questions:
- [intern] Which thread is responsible for measuring, laying out, drawing UI, and handling click events in Android?
- [junior] How do Looper, MessageQueue, and Handler work together on the Android main thread?
- [mid] Why is the Android UI toolkit single-threaded instead of allowing any background thread to mutate UI components directly?
- [senior] What goes wrong in production when messages flood the main Looper MessageQueue faster than VSYNC frames can process them?
what-interviewers-listen-for:
- The main (UI) thread runs a Looper that processes input events, lifecycle callbacks, and Choreographer frame renders sequentially from a MessageQueue.
- Single-threaded UI avoids expensive lock contention and deadlocks across the view/composition tree, at the trade-off of requiring strict offloading of blocking work.
- Flooding the main queue delays VSYNC traversal messages, causing dropped frames (jank) and delayed touch responses.
build-exercise: Log Thread.currentThread().name inside Activity.onCreate, a Compose Button onClick, and a Choreographer.FrameCallback. Inspect the call stack in the debugger to see Looper.loop() driving all three on main.
official-references:
- Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
- Looper | https://developer.android.com/reference/android/os/Looper
common-wrong-beliefs:
- Each Activity or screen runs on its own thread | All Activities, Views, and Compose UI blocks in the same process run on the single shared main thread by default.
- Jetpack Compose runs composables on background threads by default | Composition and measurement run on the main thread unless explicitly configured otherwise.

## concurrency/why-the-main-thread/why-blocking-freezes-the-screen
interview-questions:
- [intern] What happens visually to an app when you run a slow file read or network request on the main thread?
- [junior] What is an ANR (Application Not Responding) dialog and what timeout triggers it for input events?
- [mid] Why can a 30ms synchronous disk read cause visible stutter even though it is nowhere near the 5-second ANR threshold?
- [senior] What goes wrong in production when a main-thread call waits on a lock held by a background thread doing I/O?
what-interviewers-listen-for:
- At 60Hz a frame has ~16.6ms (and ~8.3ms at 120Hz); blocking the main thread prevents Choreographer from drawing the next frame, freezing animations and scroll.
- Input dispatch timeouts (~5 seconds) trigger system ANRs, which hurt Play Store vitals and crash rankings.
- Priority inversion occurs when the high-priority main thread blocks on a monitor/mutex held by a lower-priority background thread.
build-exercise: Put a Thread.sleep(200) inside a button click while a CircularProgressIndicator is spinning on screen, then increase it to 6000ms and tap repeatedly. Observe the immediate animation freeze at 200ms and the system ANR dialog after 5 seconds of blocked input.
official-references:
- Keeping your app responsive | https://developer.android.com/training/articles/perf-anr
- Understand ANRs | https://developer.android.com/topic/performance/vitals/anr
common-wrong-beliefs:
- If an operation takes less than 5 seconds on the main thread it is fine | Anything over a few milliseconds causes skipped frames and visible jank.

## concurrency/threads/running-work-on-another-thread
interview-questions:
- [intern] How do you start work on a background thread in Kotlin or Java, and why can't that thread directly update a UI View?
- [junior] How do you pass a result from a background thread back to the main thread without coroutines?
- [mid] Why is using an ExecutorService with a thread pool better than calling Thread { ... }.start() for every task?
- [senior] What goes wrong in production when an unbounded thread pool is used for bursty background tasks?
what-interviewers-listen-for:
- OS threads carry ~1MB stack allocation and kernel context-switch overhead; pooling reuses threads and caps concurrency.
- Returning to the main thread requires posting a Runnable to a main-looper Handler (`Handler(Looper.getMainLooper()).post`).
- Unbounded thread creation under burst load causes OutOfMemoryError (unable to create native thread) and CPU thrashing.
build-exercise: Spawn a background Thread that computes a result and tries to update a classic TextView directly (observing CalledFromWrongThreadException), then fix it using Handler(Looper.getMainLooper()).post.
official-references:
- Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
- Handler | https://developer.android.com/reference/android/os/Handler
common-wrong-beliefs:
- Calling Thread.run() starts a new background thread | Calling run() executes on the current thread; start() is required to allocate a new OS thread.

## concurrency/threads/why-raw-threads-get-messy
interview-questions:
- [intern] What makes chaining two sequential background tasks difficult when using raw threads and callbacks?
- [junior] How can a running background thread cause a memory leak when the user closes an Activity?
- [mid] Why is cancellation across multiple dependent threads and callbacks error-prone compared to structured concurrency?
- [senior] What goes wrong in production when uncaught exceptions occur on raw background threads vs the main thread?
what-interviewers-listen-for:
- Raw threads have no automatic lifecycle binding, leading to leaked implicit references, wasted CPU, and race conditions when callbacks arrive after destruction.
- Coordinating cancellation, error propagation, and fan-out/fan-in across threads requires manual Thread.interrupt() checks and complex synchronization.
- An uncaught exception on ANY thread triggers the process default UncaughtExceptionHandler and crashes the Android app.
build-exercise: Start a 10-second loop in a raw Thread inside an Activity that logs a field of the Activity every second, then rotate the device 3 times. Inspect Memory Profiler or logs to see 3 old Activity instances kept alive until their threads finish.
official-references:
- Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines
common-wrong-beliefs:
- An unhandled exception on a background thread only kills that background thread | On Android, the default UncaughtExceptionHandler terminates the entire app process when any thread throws an uncaught exception.

## concurrency/coroutine-basics/what-is-a-coroutine
interview-questions:
- [intern] What is a coroutine in Kotlin and why is it often called a "lightweight thread"?
- [junior] How can thousands of coroutines run concurrently on only a handful of OS threads?
- [mid] What is the trade-off between cooperative suspension in coroutines and preemptive multitasking in OS threads?
- [senior] What goes wrong in production when CPU-bound or blocking I/O code runs inside coroutines without yielding or switching dispatchers?
what-interviewers-listen-for:
- Coroutines are suspendable computations (state machines compiled via Continuation objects) scheduled onto a pool of carrier OS threads.
- Suspension frees the underlying thread to run other coroutines without kernel context switches or 1MB stack allocations per task.
- Because coroutines are cooperative, a coroutine that blocks its carrier thread starves other coroutines scheduled on the same dispatcher.
build-exercise: Launch 50,000 coroutines that each call delay(1000) and increment an AtomicInteger, and compare memory/time against spawning 5,000 OS Threads doing Thread.sleep(1000). Observe the coroutines finish effortlessly while OS threads hit OOM or massive lag.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines
- Use Kotlin Coroutines in your Android App | https://developer.android.com/codelabs/kotlin-coroutines
common-wrong-beliefs:
- Every coroutine creates a background thread | A coroutine is an object on the heap that runs on whatever thread its Dispatcher assigns, including the main thread.

## concurrency/coroutine-basics/launch-a-coroutine
interview-questions:
- [intern] What does the `launch` coroutine builder do, and what type does it return?
- [junior] Does `launch` block the thread that called it while the coroutine inside runs?
- [mid] Why does `launch` return a `Job` instead of the result of the lambda, and how is that `Job` used?
- [senior] What goes wrong in production when developers call `scope.launch` inside a tight loop without tracking or limiting concurrency?
what-interviewers-listen-for:
- `launch` starts a "fire-and-forget" coroutine for work that does not return a result value, returning a `Job` handle for lifecycle tracking and cancellation.
- Code immediately following `launch` continues executing without waiting for the launched coroutine unless `.join()` is called.
- Launching unbounded coroutines that perform network or DB operations can exhaust connection pools or memory if not bounded.
build-exercise: Write a function that logs "A", calls `scope.launch { delay(100); log("B") }`, and logs "C". Run it to observe "A, C, B", then use the returned `Job` to call `job.join()` and observe "A, B, C".
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Job | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/
common-wrong-beliefs:
- Code inside `launch { }` always runs immediately before the next line | `launch` schedules the coroutine for execution according to its Dispatcher; the surrounding code usually continues first.

## concurrency/coroutine-basics/async-and-await
interview-questions:
- [intern] When should you use `async` instead of `launch`, and what does `Deferred.await()` do?
- [junior] Why does calling `await()` immediately on the line right after `async` defeat the purpose of `async`?
- [mid] What is the trade-off between calling two suspend functions sequentially vs wrapping them in `coroutineScope { async ... }`?
- [senior] What goes wrong in production when one `async` branch throws an exception while another `async` branch is still running?
what-interviewers-listen-for:
- `async` starts a concurrent coroutine that returns a `Deferred<T>`, allowing parallel decomposition when multiple independent operations run before calling `await()`.
- Calling `async { a() }.await()` sequentially adds coroutine allocation overhead over calling `a()` directly.
- Under structured concurrency (`coroutineScope`), an exception in one `async` child immediately cancels the sibling `async` and propagates out of the scope even before `.await()` is reached.
build-exercise: Write two suspend functions that each delay 1000ms. Call them sequentially and log elapsed time (~2000ms), then wrap both in `coroutineScope { val a = async { ... }; val b = async { ... }; a.await() + b.await() }` and verify it completes in ~1000ms.
official-references:
- Composing suspending functions | https://kotlinlang.org/docs/composing-suspending-functions.html
- Deferred | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-deferred/
common-wrong-beliefs:
- An exception inside `async` is never thrown until you call `.await()` | In a normal `coroutineScope` or `viewModelScope`, an uncaught exception in `async` immediately notifies the parent job and cancels the scope, even if `await()` is never called.

## concurrency/suspend/what-suspend-means
interview-questions:
- [intern] What does the `suspend` keyword on a function tell the Kotlin compiler?
- [junior] Why can a `suspend` function only be called from another `suspend` function or a coroutine?
- [mid] How does the Kotlin compiler transform a `suspend` function under the hood using Continuation-Passing Style (CPS)?
- [senior] What goes wrong in production if you mark a CPU-heavy or blocking function with `suspend` without switching dispatchers inside it?
what-interviewers-listen-for:
- `suspend` marks a function that can pause execution at suspension points and resume later without blocking the carrier thread.
- The compiler rewrites the function to accept a hidden `Continuation` parameter and generates a state machine (`label` switch) storing local variables across suspension points.
- Adding `suspend` alone does not make a function asynchronous or move it off the main thread; the compiler emits an "unused suspend" warning if no suspension points exist inside.
build-exercise: Write a simple suspend function with two `delay()` calls and local variables in between. Use Android Studio's "Tools -> Kotlin -> Show Kotlin Bytecode" and click "Decompile" to inspect the generated `Continuation` state machine and `label` switch.
official-references:
- Composing suspending functions | https://kotlinlang.org/docs/composing-suspending-functions.html
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
common-wrong-beliefs:
- Marking a function `suspend` automatically runs it on a background thread | `suspend` only enables suspension; the function runs on the caller's thread unless it switches context (e.g., via `withContext`).

## concurrency/suspend/suspend-vs-blocking
interview-questions:
- [intern] What is the difference between `delay(1000)` and `Thread.sleep(1000)`?
- [junior] What happens to the UI if you call `Thread.sleep(1000)` vs `delay(1000)` inside `lifecycleScope.launch` on `Dispatchers.Main`?
- [mid] Why is calling `runBlocking` on the Android main thread dangerous, and where is `runBlocking` actually meant to be used?
- [senior] What goes wrong in production when a legacy blocking SDK call is invoked directly inside a coroutine running on `Dispatchers.Default` or `Dispatchers.Main`?
what-interviewers-listen-for:
- Blocking parks the OS thread so no other work can use it; suspending saves the coroutine's continuation state and releases the thread to process other tasks or UI frames.
- `runBlocking` blocks the calling thread until all coroutines inside finish; on `Main`, it freezes the UI or deadlocks if an inner coroutine dispatches back to `Main`.
- Blocking calls inside `Dispatchers.Default` (which is sized to CPU core count) can starve all CPU-bound coroutines across the app.
build-exercise: Launch two coroutines on a single-threaded dispatcher (`Executors.newSingleThreadExecutor().asCoroutineDispatcher()`). First have both call `Thread.sleep(1000)` and log start/end timestamps; then switch to `delay(1000)` and observe how `delay` interleaves both in ~1 second total while `sleep` takes ~2 seconds.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
common-wrong-beliefs:
- `runBlocking` is a normal way to bridge suspend functions into Android UI click listeners | `runBlocking` parks the calling thread and can cause ANRs or deadlocks on the main thread; use `lifecycleScope.launch` or `viewModelScope.launch` instead.

## concurrency/dispatchers/main-io-and-default
interview-questions:
- [intern] What are the three main `Dispatchers` provided by `kotlinx.coroutines`, and what is each used for?
- [junior] Why should JSON parsing of a very large payload run on `Dispatchers.Default` rather than `Dispatchers.IO`?
- [mid] How do `Dispatchers.Default` and `Dispatchers.IO` share threads under the hood, and what is the trade-off of `Dispatchers.IO`'s 64-thread limit?
- [senior] What goes wrong in production when dozens of coroutines do slow blocking I/O on `Dispatchers.IO`, and when should `limitedParallelism` be used?
what-interviewers-listen-for:
- `Main` is backed by the Android UI Looper; `Default` is sized to CPU cores (`max(2, CPU_COUNT)`) for CPU-intensive work; `IO` allows up to `max(64, CPU_COUNT)` threads for blocking disk/network I/O.
- `Default` and `IO` share the same underlying coroutine scheduler thread pool, avoiding an actual OS thread context switch when shifting between `Default` and `IO` via `withContext`.
- Slow third-party blocking calls can saturate all 64 `IO` threads; `Dispatchers.IO.limitedParallelism(n)` isolates or expands capacity safely.
build-exercise: Log `Thread.currentThread().name` before and after switching from `Dispatchers.Default` to `Dispatchers.IO` via `withContext`, then launch 70 coroutines on `Dispatchers.IO` each calling `Thread.sleep(2000)` to see the 64-thread cap delay the remaining 6 tasks.
official-references:
- Coroutine context and dispatchers | https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
common-wrong-beliefs:
- `Dispatchers.IO` has an unlimited number of threads | By default, `Dispatchers.IO` is elastic up to 64 threads (or the number of CPU cores if higher) unless `limitedParallelism` creates a separate view.

## concurrency/dispatchers/main-safe-functions
interview-questions:
- [intern] What does it mean for a `suspend` function to be "main-safe"?
- [junior] How do you use `withContext` to make a blocking repository or file function safe to call from `Dispatchers.Main`?
- [mid] Why should the data/repository layer be responsible for switching dispatchers with `withContext` rather than forcing ViewModels to wrap calls in `launch(Dispatchers.IO)`?
- [senior] What goes wrong in production when every small helper function wraps trivial in-memory operations in `withContext(Dispatchers.IO)`?
what-interviewers-listen-for:
- A main-safe suspend function never blocks the main thread; if it performs blocking I/O or heavy CPU work, it internally moves execution to `Dispatchers.IO` or `Dispatchers.Default` using `withContext`.
- Centralizing dispatcher switching at the lowest blocking layer keeps ViewModels clean, testable with a single test dispatcher, and prevents callers from forgetting which thread a method requires.
- Overusing `withContext` inside tight loops causes unnecessary dispatch overhead and thread hops.
build-exercise: Enable `StrictMode.setThreadPolicy` with `detectDiskReads()` and `penaltyDeath()` in your Activity. Call a repository suspend function from `viewModelScope.launch` (on `Main`) that reads a file, watch StrictMode crash the app, and fix the repository function with `withContext(Dispatchers.IO)`.
official-references:
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
- Use Kotlin Coroutines in your Android App | https://developer.android.com/codelabs/kotlin-coroutines
common-wrong-beliefs:
- ViewModels should always call `viewModelScope.launch(Dispatchers.IO)` | ViewModels should launch on `Dispatchers.Main` (the default of `viewModelScope`) and rely on main-safe suspend functions in the data layer.

## concurrency/scopes/what-is-a-coroutine-scope
interview-questions:
- [intern] What is a `CoroutineScope` and why do coroutine builders like `launch` require one?
- [junior] What elements make up a `CoroutineContext` inside a `CoroutineScope`?
- [mid] Why is `GlobalScope` discouraged in production Android code, and what is the trade-off of creating a custom `CoroutineScope`?
- [senior] What goes wrong in production if a custom `CoroutineScope` is created in a repository or manager class and never cancelled, or cancelled once and reused?
what-interviewers-listen-for:
- A `CoroutineScope` holds a `CoroutineContext` (typically combining a `Job`, `CoroutineDispatcher`, `CoroutineName`, and `CoroutineExceptionHandler`) that defines the lifetime of all coroutines launched in it.
- When a scope is cancelled, all child coroutines are cancelled automatically; once cancelled, a `CoroutineScope` cannot launch new coroutines.
- `GlobalScope` bypasses structured concurrency, leaking work beyond component lifetimes and making testing difficult.
build-exercise: Create a custom `CoroutineScope(SupervisorJob() + Dispatchers.Default)`, launch a repeating coroutine, call `scope.cancel()`, and then try calling `scope.launch { ... }` again. Observe that the second coroutine silently never executes because the scope's Job is already in the Cancelled state.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Coroutine context and dispatchers | https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html
common-wrong-beliefs:
- Calling `scope.cancel()` resets the scope so you can launch fresh coroutines later | A cancelled `Job` is terminal; any subsequent `launch` on a cancelled `CoroutineScope` is ignored. To cancel only children while keeping the scope alive, use `scope.coroutineContext.cancelChildren()`.

## concurrency/scopes/parent-and-child-coroutines
interview-questions:
- [intern] When you launch a coroutine inside another coroutine, what is their parent-child relationship?
- [junior] Does a parent coroutine finish as soon as its own code block ends, or does it wait for its child coroutines?
- [mid] How does structured concurrency guarantee that no child coroutine leaks when a parent operation is cancelled or fails?
- [senior] What goes wrong in production when someone passes a new `Job()` into `launch(Job())` inside an existing scope?
what-interviewers-listen-for:
- Under structured concurrency, a parent coroutine waits for all children to complete (`Completing` state), cancelling the parent cancels all children, and an uncaught exception in a child cancels the parent and siblings (unless supervised).
- Passing `launch(Job())` breaks the parent-child hierarchy: the child no longer cancels when the parent scope cancels, leaking the coroutine.
build-exercise: Inside a parent `launch`, spawn two child `launch` blocks: one using the inherited context and one using `launch(Job())`. Cancel the parent scope after 500ms and observe that the normal child stops immediately while the `launch(Job())` child continues running forever.
official-references:
- Coroutine context and dispatchers | https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html
- Cancellation and timeouts | https://kotlinlang.org/docs/cancellation-and-timeouts.html
common-wrong-beliefs:
- A parent coroutine completes as soon as the last line of its lambda executes | A parent Job remains in the `Completing` state until every child coroutine launched inside it has finished.

## concurrency/scopes/scopes-in-android-code
interview-questions:
- [intern] What is `viewModelScope` and when is it automatically cancelled?
- [junior] What is the difference between `lifecycleScope`, `viewModelScope`, and `rememberCoroutineScope()` in Compose?
- [mid] Why does `viewModelScope` use `SupervisorJob() + Dispatchers.Main.immediate` by default?
- [senior] What goes wrong in production when an app uses `viewModelScope` for work that must survive the user navigating away from the screen (like submitting a payment or analytics flush)?
what-interviewers-listen-for:
- `viewModelScope` lives until `ViewModel.onCleared()`; `lifecycleScope` lives until `Lifecycle.Event.ON_DESTROY`; `rememberCoroutineScope()` is bound to the Composable's presence in the Composition.
- `Dispatchers.Main.immediate` avoids an extra Looper dispatch hop if already on the main thread, and `SupervisorJob` stops one failed UI operation from permanently killing `viewModelScope`.
- Work that must outlive the screen belongs in an application-scoped coroutine injected into the data layer or `WorkManager` if it must survive process death.
build-exercise: Launch a 5-second countdown in `lifecycleScope`, `viewModelScope`, and `rememberCoroutineScope()`. Rotate the screen at second 2 and navigate back at second 4 to observe which countdowns reset or stop on rotation vs back navigation.
official-references:
- Use Kotlin coroutines with lifecycle-aware components | https://developer.android.com/topic/libraries/architecture/coroutines
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
common-wrong-beliefs:
- `lifecycleScope.launch` pauses automatically when the Activity goes into the background | `lifecycleScope.launch` continues running until `ON_DESTROY`; you must use `repeatOnLifecycle(Lifecycle.State.STARTED)` to stop and restart work with screen visibility.

## concurrency/cancellation-and-errors/cancel-is-cooperative
interview-questions:
- [intern] If you call `job.cancel()` on a coroutine running a `while (true)` calculation loop, why does the loop keep running?
- [junior] Name three ways to make a CPU-intensive loop inside a coroutine cooperative with cancellation.
- [mid] Why did Kotlin design coroutine cancellation to be cooperative rather than forcibly killing the underlying thread like `Thread.stop()`?
- [senior] What goes wrong in production when wrapping a third-party callback API with `suspendCoroutine` instead of `suspendCancellableCoroutine`?
what-interviewers-listen-for:
- Cancellation only takes effect at suspension points that check the Job state (like `delay`, `yield`) or explicit checks (`ensureActive()`, `isActive`).
- Forcibly terminating execution halfway through mutating shared state corrupts locks and invariants; cooperative cancellation lets `finally` blocks clean up resources safely.
- Using `suspendCoroutine` ignores scope cancellation and omits `invokeOnCancellation`, leaking the underlying network call or listener.
build-exercise: Write a coroutine on `Dispatchers.Default` that computes prime numbers in a `while (i < 10_000_000)` loop and cancel its Job after 100ms. Watch it run to completion anyway, then add `ensureActive()` inside the loop and observe immediate termination.
official-references:
- Cancellation and timeouts | https://kotlinlang.org/docs/cancellation-and-timeouts.html
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
common-wrong-beliefs:
- Calling `job.cancel()` immediately stops the coroutine at the current line of code | `cancel()` only sets the Job's state to `Cancelling`; the coroutine keeps running until it hits a cancellable suspension point or checks `isActive`/`ensureActive()`.

## concurrency/cancellation-and-errors/exceptions-in-coroutines
interview-questions:
- [intern] Why does wrapping `scope.launch { throw RuntimeException() }` in an outer `try / catch` block fail to catch the exception?
- [junior] Where should you place a `try / catch` block to properly handle an exception thrown inside `launch`?
- [mid] What is the difference between how `launch` and `async` treat uncaught exceptions, and what role does `CoroutineExceptionHandler` play?
- [senior] What goes wrong in production when a developer installs a `CoroutineExceptionHandler` on a child `launch` instead of the root scope?
what-interviewers-listen-for:
- `launch` starts asynchronously; the outer `try / catch` only wraps the scheduling of the coroutine, not its asynchronous execution, and uncaught exceptions propagate up the Job hierarchy to the parent.
- `CoroutineExceptionHandler` is a last-resort global/root handler for uncaught exceptions in root `launch` coroutines; it does not prevent sibling cancellation under a normal `Job` and has no effect when installed on non-root child coroutines (except direct children of `SupervisorJob`).
build-exercise: Wrap `scope.launch { throw IllegalStateException("Boom") }` in an outer `try / catch` and run it to see the crash bypass the `catch`. Move `try / catch` inside the `launch` block and also test attaching a `CoroutineExceptionHandler` to the root scope.
official-references:
- Coroutine exceptions handling | https://kotlinlang.org/docs/exception-handling.html
- Exceptions in coroutines | https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c
common-wrong-beliefs:
- Surrounding `scope.launch { ... }` with `try / catch` catches exceptions thrown inside the coroutine | `launch` returns a `Job` immediately and propagates exceptions up through the `CoroutineScope`'s Job hierarchy, completely bypassing an external `try / catch`.

## concurrency/cancellation-and-errors/supervisor-and-sibling-failure
interview-questions:
- [intern] What happens to sibling coroutines when one child coroutine fails inside a regular `coroutineScope`?
- [junior] How does `supervisorScope` or `SupervisorJob` change what happens when one child coroutine throws an exception?
- [mid] Why is `viewModelScope` backed by a `SupervisorJob` rather than a standard `Job`?
- [senior] What goes wrong in production when a developer writes `withContext(SupervisorJob())` thinking it will isolate child failures?
what-interviewers-listen-for:
- In a standard `Job` / `coroutineScope`, failure is bilateral: one child's uncaught exception cancels the parent and all other siblings.
- In `SupervisorJob` / `supervisorScope`, failure propagates only downward: a child failure does not cancel the parent or siblings, though the failed child still requires error handling (`try/catch` or `CoroutineExceptionHandler`) to avoid crashing on Android.
- Passing `SupervisorJob()` as the argument to `launch(SupervisorJob())` or `withContext(SupervisorJob())` makes the `SupervisorJob` a single parent of the block while its children still share a regular sub-Job—use `supervisorScope { ... }` instead.
build-exercise: Inside a `coroutineScope`, launch two children where Child 1 throws an exception at 100ms and Child 2 logs at 300ms (with a `CoroutineExceptionHandler` on the root). Observe Child 2 get cancelled; then replace `coroutineScope` with `supervisorScope` and observe Child 2 finish normally.
official-references:
- Coroutine exceptions handling | https://kotlinlang.org/docs/exception-handling.html
common-wrong-beliefs:
- Using `supervisorScope` automatically catches and silences exceptions in its child `launch` blocks | `supervisorScope` only prevents sibling/parent cancellation; an unhandled exception in a child `launch` will still crash the app unless caught or handled by a `CoroutineExceptionHandler`.

## concurrency/cancellation-and-errors/never-swallow-cancellation
interview-questions:
- [intern] What exception do suspending functions throw when their coroutine is cancelled?
- [junior] Why is writing `catch (e: Exception)` around a suspending call dangerous if you only log the error and do not rethrow `CancellationException`?
- [mid] How does `runCatching` interact with `CancellationException`, and why can it silently break structured concurrency?
- [senior] What goes wrong in production when a retry loop or cleanup block catches `Throwable` or `Exception`, swallows `CancellationException`, and continues executing network requests?
what-interviewers-listen-for:
- Coroutines use `CancellationException` as the mechanism to unwind the call stack when a job is cancelled.
- Catching `Exception` or `Throwable` (including via `runCatching`) catches `CancellationException`; if not rethrown (`if (e is CancellationException) throw e` or `currentCoroutineContext().ensureActive()`), the coroutine continues executing subsequent lines in a "zombie" cancelled state.
- If you must run a suspending function inside a `finally` block after cancellation, you must wrap it in `withContext(NonCancellable)`.
build-exercise: Write a coroutine with two sequential `delay(500)` calls where the first is wrapped in `try { delay(500) } catch (e: Exception) { println("Caught $e") }`. Cancel the job at 200ms and observe the coroutine still run the code after the `try/catch`; then add `if (e is CancellationException) throw e` and observe proper termination.
official-references:
- Cancellation and timeouts | https://kotlinlang.org/docs/cancellation-and-timeouts.html
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
common-wrong-beliefs:
- `runCatching { someSuspendFun() }` is safe to use directly for error handling in coroutines | `runCatching` catches `Throwable`, which includes `CancellationException`, swallowing cancellation signals unless `CancellationException` is explicitly rethrown.

## concurrency/flow-basics/what-is-a-flow
interview-questions:
- [intern] What problem does a Kotlin `Flow<T>` solve that a single `suspend` function cannot?
- [junior] How do `flow { ... }`, `emit()`, and `collect()` work together to stream values?
- [mid] Why is `Flow` built on top of suspending functions rather than requiring complex reactive backpressure subscriptions like RxJava `Flowable`?
- [senior] What goes wrong in production when someone attempts to call `emit()` from multiple threads or a nested `launch` inside a standard `flow { }` builder?
what-interviewers-listen-for:
- A `suspend` function returns a single asynchronous value, while `Flow<T>` emits multiple asynchronous values sequentially over time.
- Because `emit` and `collect` are `suspend` functions, `Flow` gets natural cooperative backpressure: if the collector is slow, `emit` suspends the producer automatically.
- Standard `flow { }` enforces context preservation and sequential emission; emitting from another coroutine or dispatcher inside `flow { }` throws `IllegalStateException` (use `channelFlow` or `callbackFlow` for concurrent/callback emissions).
build-exercise: Build a `flow { for (i in 1..5) { delay(200); emit(i) } }` and collect it with a collector that calls `delay(500)` on each item. Log timestamps on both `emit` and `collect` to see how the slow collector naturally suspends the producer.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- Kotlin flows on Android | https://developer.android.com/kotlin/flow
common-wrong-beliefs:
- You can attach a callback listener and call `emit()` inside a standard `flow { }` builder | `flow { }` is strictly sequential and cannot emit from a different coroutine context or non-suspending callback; you must use `callbackFlow` with `trySend` and `awaitClose`.

## concurrency/flow-basics/flows-are-cold
interview-questions:
- [intern] What does it mean to say that a standard `flow { ... }` is "cold"?
- [junior] If two different UI components call `.collect()` on the same cold `Flow` instance, how many times does the producer block inside `flow { }` run?
- [mid] What is the trade-off between a cold `Flow` and a hot flow (`StateFlow` / `SharedFlow`) for repository data streams?
- [senior] What goes wrong in production when a cold `Flow` that opens a database query or network polling loop is passed around and collected by multiple consumers without sharing?
what-interviewers-listen-for:
- Cold flows are lazy recipes: the builder code does not execute until a terminal operator like `collect()` is called, and each collector triggers an independent execution from scratch.
- Cold flows save memory and resources when nothing is observing, but duplicate expensive upstream work (network calls, DB queries, sensor listeners) if collected multiple times without `shareIn` or `stateIn`.
build-exercise: Create a `val numbers = flow { println("Producer started"); emit(1); emit(2) }`. Observe that nothing prints before `collect()`, then collect `numbers` twice in two separate coroutines and observe `"Producer started"` print twice.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- Kotlin flows on Android | https://developer.android.com/kotlin/flow
common-wrong-beliefs:
- Creating a `val myFlow = flow { api.fetchData() }` starts fetching data immediately in the background | A cold `Flow` does nothing when instantiated; the code inside `flow { }` only executes when a terminal operator like `collect()` is invoked.

## concurrency/flow-basics/collect-and-cancel
interview-questions:
- [intern] Why does a line of code placed directly after `stateFlow.collect { ... }` inside a single `launch` block never execute?
- [junior] How do you collect two different flows concurrently inside `viewModelScope` or `lifecycleScope`?
- [mid] How does `launchIn(scope)` differ from calling `flow.collect()` directly?
- [senior] What goes wrong in production when an Android UI collects a flow inside `lifecycleScope.launch` without `repeatOnLifecycle` or `collectAsStateWithLifecycle`?
what-interviewers-listen-for:
- `collect()` is a suspending function that only returns when the upstream flow completes; since hot flows (`StateFlow`, `SharedFlow`) and database observation flows never complete normally, `collect` suspends forever until its coroutine is cancelled.
- Collecting multiple infinite flows requires launching separate child coroutines (or using `launchIn(scope)` / `combine`).
- Collecting directly in `lifecycleScope.launch` keeps upstream producers active (e.g., location updates, Room queries) while the app is in the background (`ON_STOP`), wasting battery and risking crashes on View updates.
build-exercise: Inside a single `launch` block, call `stateFlow1.collect { ... }` followed by `stateFlow2.collect { ... }` and log when each starts. Observe that `stateFlow2` is never collected because `stateFlow1.collect` never returns, then fix it by launching each in its own child coroutine.
official-references:
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
common-wrong-beliefs:
- `flow.collect { ... }` registers a non-blocking listener and immediately moves to the next line of code | `collect` is a suspending call that suspends the calling coroutine until the flow finishes or the coroutine is cancelled.

## concurrency/flow-operators/transform-a-flow
interview-questions:
- [intern] How do intermediate operators like `map` and `filter` transform a `Flow`, and are they cold or hot?
- [junior] What is the difference between `map`, `transform`, and `onEach` on a `Flow`?
- [mid] How do `catch` and `onCompletion` work in a Flow pipeline, and why does the position of `.catch { }` in the chain matter?
- [senior] What goes wrong in production when an exception occurs inside the downstream `collect { }` block while `.catch { }` is used upstream?
what-interviewers-listen-for:
- Intermediate operators (`map`, `filter`, `transform`, `take`, `distinctUntilChanged`) return a new cold `Flow` without executing anything until a terminal operator (`collect`, `first`, `toList`) is called.
- The `catch` operator is strictly upstream-only: it catches exceptions thrown in the `flow { }` builder or operators above it, and can `emit()` fallback values, but it NEVER catches exceptions thrown in operators or `collect` below it.
build-exercise: Create a flow of integers `1..5`, chain `.map { if (it == 3) error("Upstream") else it * 2 }`, attach `.catch { emit(-1) }`, and collect it. Then move the `error()` throw inside the `collect { }` block and observe that `.catch` does not intercept downstream collector crashes.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- Kotlin flows on Android | https://developer.android.com/kotlin/flow
common-wrong-beliefs:
- Placing `.catch { }` right before `.collect { }` catches exceptions thrown inside the `collect` lambda | `.catch` only catches upstream exceptions; to protect code inside `collect` without crashing, move the logic into `.onEach { }` before `.catch { }.collect()`.

## concurrency/flow-operators/flow-on-and-context
interview-questions:
- [intern] On which dispatcher does the code inside `flow { ... }` run if you do not specify `flowOn`?
- [junior] How does `flowOn(Dispatchers.IO)` change the execution thread of a Flow pipeline, and does it affect operators placed before it or after it?
- [mid] Why does switching dispatchers with `flowOn` introduce a channel buffer between upstream and downstream?
- [senior] What goes wrong in production when a developer wraps `emit()` inside `withContext(Dispatchers.IO)` inside a `flow { }` builder instead of using `flowOn`?
what-interviewers-listen-for:
- By default, a `Flow` runs in the `CoroutineContext` of its collector (context preservation).
- `flowOn` changes the dispatcher for all **upstream** operators preceding it (up to any earlier `flowOn`), leaving downstream operators and `collect` on the collector's context.
- When `flowOn` changes the dispatcher, it launches an upstream coroutine producing into a channel buffer, changing execution from purely sequential to concurrent buffered emission.
build-exercise: Build a flow with `.map { logThread("map1") }.flowOn(Dispatchers.IO).map { logThread("map2") }` and collect it on `Dispatchers.Main`. Observe that the builder and `map1` log an IO thread while `map2` and `collect` log the Main thread.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- Kotlin flows on Android | https://developer.android.com/kotlin/flow
common-wrong-beliefs:
- `flowOn` changes the thread for the operators and `collect` block that come after it | `flowOn` only affects upstream operators that appear *before* it in the chain.

## concurrency/flow-operators/combine-flows
interview-questions:
- [intern] How do you merge the latest values of two different flows (e.g., a user profile flow and a settings flow) into a single UI state flow?
- [junior] What is the difference between `combine` and `zip` when pairing emissions from two flows?
- [mid] When Does `combine(flowA, flowB)` emit its very first value, and what happens if `flowB` hasn't emitted anything yet?
- [senior] What goes wrong in production when combining 4+ database/network flows where one upstream flow never emits an initial value on an empty table or slow endpoint?
what-interviewers-listen-for:
- `combine` waits until every participating flow has emitted at least one value, and thereafter emits a new combined result whenever ANY upstream flow emits a new value using the latest cached values of the others.
- `zip` pairs emissions strictly 1-to-1 by index (1st with 1st, 2nd with 2nd) and completes as soon as the shortest flow completes.
- If one upstream flow in `combine` hangs or never emits its first item, the entire combined UI state flow hangs in its initial loading state forever (fixable with `.onStart { emit(default) }`).
build-exercise: Create `flowA` emitting every 300ms and `flowB` emitting every 1000ms. Print outputs using `combine` vs `zip`, then make `flowB` delay 5 seconds before its first emission to observe `combine` waiting 5 seconds before producing its first item.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
common-wrong-beliefs:
- `combine` emits immediately as soon as the first flow has a value | `combine` waits until *all* input flows have emitted at least one value before running its transform block for the first time.

## concurrency/flow-operators/search-as-you-type
interview-questions:
- [intern] When building a search-as-you-type bar with Flow, which operator waits for the user to pause typing before firing a request?
- [junior] What roles do `debounce`, `distinctUntilChanged`, and `filter` play in a search query pipeline?
- [mid] Why should a search pipeline use `flatMapLatest` instead of `map` or `flatMapMerge` when making network requests for each query?
- [senior] What goes wrong in production if a network error occurs inside the inner flow of `flatMapLatest` and `.catch` is placed outside `flatMapLatest`?
what-interviewers-listen-for:
- `debounce(timeout)` drops rapid intermediate keystrokes; `distinctUntilChanged()` avoids re-querying if the user types and deletes a character returning to the same query.
- `flatMapLatest` automatically cancels the in-flight coroutine of the previous search query as soon as a new query arrives, preventing out-of-order responses ("race condition between slow query 'a' and fast query 'ab'") and wasted bandwidth.
- If `.catch` is placed outside `flatMapLatest` on the outer query flow, an exception completes the entire outer search stream and the search bar stops reacting to future keystrokes; `.catch` must be placed on the inner request flow inside `flatMapLatest`.
build-exercise: Wire a `MutableStateFlow("")` representing search input through `debounce(300)`, `distinctUntilChanged()`, and `flatMapLatest { query -> flow { delay(500); emit("Results for $query") } }`. Emit `"k"`, `"ko"`, `"kot"` 100ms apart and verify only `"Results for kot"` completes, then test simulating an exception on `"ko"` with `.catch` inside vs outside `flatMapLatest`.
official-references:
- Asynchronous Flow | https://kotlinlang.org/docs/flow.html
- Kotlin flows on Android | https://developer.android.com/kotlin/flow
common-wrong-beliefs:
- Using `.map { api.search(it) }` with `debounce` prevents out-of-order or redundant network work | `map` waits sequentially for an already-started slow request to finish before starting the next; `flatMapLatest` is required to actively cancel the obsolete in-flight request.

## concurrency/state-and-shared-flow/state-flow
interview-questions:
- [intern] What is `StateFlow`, how does it differ from a regular cold `Flow`, and why does it require an initial value?
- [junior] Why does updating a `MutableStateFlow` with an structurally equal value (`==`) not trigger collectors to run?
- [mid] Why should you use `mutableStateFlow.update { current -> ... }` instead of `mutableStateFlow.value = mutableStateFlow.value.copy(...)` when multiple coroutines update state?
- [senior] What goes wrong in production when someone stores a mutable list inside a `StateFlow`, mutates the list in place, and reassigns it to `.value`?
what-interviewers-listen-for:
- `StateFlow` is a hot, conflated, single-value state holder (`replay = 1`) that always has a current `.value` and immediately emits the latest state to new collectors.
- `StateFlow` performs equality-based conflation (`Any.equals`): it skips emission if the new value equals the old value, and if a collector is slow, intermediate states are dropped in favor of the most recent state.
- `MutableStateFlow.update { ... }` uses an atomic compare-and-set loop so concurrent state updates never overwrite each other's fields.
build-exercise: Create a `MutableStateFlow(User(count = 0))` and launch 100 coroutines on `Dispatchers.Default` that increment `count` using `state.value = state.value.copy(count = state.value.count + 1)`. Observe lost updates (`count < 100`), then switch to `state.update { it.copy(count = it.count + 1) }` and verify `count == 100`.
official-references:
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
- StateFlow | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/
common-wrong-beliefs:
- Mutating a list in place and reassigning `stateFlow.value = sameListInstance` updates the UI | `StateFlow` compares the old and new reference with `==`; since it is the same mutated instance (or equal before/after), `StateFlow` considers the value unchanged and emits nothing.

## concurrency/state-and-shared-flow/one-off-events
interview-questions:
- [intern] Why does representing a snackbar message or navigation trigger cleanly in Android cause problems if you never clear it after showing it?
- [junior] What happens on screen rotation if a ViewModel emits a one-off UI event into a `SharedFlow(replay = 0)` vs storing it in `StateFlow`?
- [mid] Why does Google's architecture guidance recommend modeling transient UI messages as state (with a "shown/consumed" callback) rather than exposing a `SharedFlow` or `Channel` for UI events?
- [senior] What goes wrong in production when a ViewModel pushes a critical navigation or dialog event into a `SharedFlow(replay = 0)` or `Channel` right as the Activity is stopping or recreating?
what-interviewers-listen-for:
- A `SharedFlow(replay = 0)` drops emissions completely if no collector is active at that exact millisecond (e.g., during configuration change or when the app is briefly stopped).
- A buffered `Channel(BUFFERED)` guarantees delivery to one collector, but if the coroutine collecting the channel is cancelled while handling the event mid-frame during recreation, the event can still be lost unless modeled as state with an explicit `onMessageShown(id)` acknowledgement.
build-exercise: Emit a "Payment Successful" message after a 2-second delay from a ViewModel using `MutableSharedFlow<String>(replay = 0)` vs a `StateFlow<UiState>` list of messages with `onMessageDismissed(id)`. Trigger the action, immediately rotate the phone during the 2-second delay, and observe when `SharedFlow` drops the message while state-based modeling retains it until dismissed.
official-references:
- UI events | https://developer.android.com/topic/architecture/ui-layer/events
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
common-wrong-beliefs:
- `MutableSharedFlow()` with default parameters is reliable for one-off UI events from a ViewModel | With `replay = 0` and no active subscribers during lifecycle stop/rotation, events emitted to `SharedFlow` are immediately lost.

## concurrency/state-and-shared-flow/share-a-cold-flow
interview-questions:
- [intern] Which operators convert a cold `Flow` into a hot `StateFlow` or `SharedFlow`?
- [junior] What are the three parameters required by `stateIn(scope, started, initialValue)`?
- [mid] Why is `SharingStarted.WhileSubscribed(5000)` preferred over `SharingStarted.Eagerly` or `SharingStarted.Lazily` in Android ViewModels?
- [senior] What goes wrong in production if you call `.stateIn(viewModelScope, ...)` inside a function or getter every time the UI calls it, instead of storing it in a `val` property?
what-interviewers-listen-for:
- `stateIn` and `shareIn` multicast a single upstream cold flow execution across multiple downstream collectors.
- `SharingStarted.WhileSubscribed(5000)` stops the upstream flow 5 seconds after the last UI collector disappears (saving battery when the user backgrounds the app), while the 5-second grace period keeps the upstream alive across instant configuration changes (screen rotation).
- Calling `stateIn` in a function (`fun getState() = repo.flow.stateIn(...)`) creates a brand-new hot flow and upstream subscription on every call/recomposition, leaking coroutines in `viewModelScope`.
build-exercise: Create a cold flow that logs `"Upstream started"` and `"Upstream cancelled"` in `onStart` / `onCompletion`, and convert it with `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)`. Collect it with `collectAsStateWithLifecycle()`, rotate the screen (upstream stays alive), then press the Home button and wait 5 seconds (observe `"Upstream cancelled"` in Logcat).
official-references:
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
- SharingStarted | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-sharing-started/
common-wrong-beliefs:
- `SharingStarted.Lazily` stops the upstream flow when the user puts the app in the background | `Lazily` starts on the first subscriber and NEVER stops until the entire `CoroutineScope` (`viewModelScope`) is cancelled.

## concurrency/channels/channels-between-coroutines
interview-questions:
- [intern] How does a `Channel<T>` differ from a `SharedFlow<T>` when two coroutines are receiving from it?
- [junior] What is the difference between `Channel.RENDEZVOUS`, `Channel.BUFFERED`, `Channel.CONFLATED`, and `Channel.UNLIMITED`?
- [mid] When does `channel.send()` suspend, and how does `BufferOverflow` (`SUSPEND`, `DROP_OLDEST`, `DROP_LATEST`) change that behavior?
- [senior] What goes wrong in production when an `UNLIMITED` channel is used between a fast producer and a slow consumer, or when a producer finishes without calling `channel.close()`?
what-interviewers-listen-for:
- `SharedFlow` broadcasts every emission to ALL collectors (fan-out multicast), whereas `Channel` delivers each sent element to EXACTLY ONE receiver (queue/work-distribution semantics).
- A `RENDEZVOUS` channel (default, capacity 0) suspends `send()` until a receiver calls `receive()`; `BUFFERED` buffers up to 64 items before suspending.
- `UNLIMITED` channels risk `OutOfMemoryError` if the consumer lags behind, and forgetting `channel.close()` causes `for (x in channel)` consumers to suspend forever.
build-exercise: Create a `Channel<Int>()` and launch two worker coroutines that both iterate `for (item in channel)` while a producer sends numbers `1..10` and closes the channel. Compare how the numbers are split between the two workers versus how a `SharedFlow` delivers all 10 numbers to both workers.
official-references:
- Channels | https://kotlinlang.org/docs/channels.html
- Channel | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/
common-wrong-beliefs:
- All coroutines listening to a `Channel` receive every item sent to it | A `Channel` is a single-delivery queue; each sent item is consumed by only one waiting receiver.

## concurrency/shared-mutable-state/race-conditions-in-coroutines
interview-questions:
- [intern] Why can two coroutines incrementing the same `var count = 0` variable produce the wrong total even without raw threads?
- [junior] What is the difference between using `Mutex` (`mutex.withLock { ... }`) and Java's `synchronized` block inside a coroutine?
- [mid] What are the trade-offs between using `AtomicInteger`/`AtomicReference`, a `Mutex`, or confining state to a single-threaded dispatcher (`limitedParallelism(1)`)?
- [senior] What goes wrong in production when a coroutine calls a function that tries to re-acquire the same `kotlinx.coroutines.sync.Mutex` it already holds?
what-interviewers-listen-for:
- Coroutines on multi-threaded dispatchers (`Default`, `IO`) run in parallel on multiple OS threads, and even on a single thread, a suspension point (`delay`, network call) between reading and writing shared state creates a race condition.
- `Mutex.withLock` suspends the waiting coroutine instead of blocking the carrier thread like `synchronized`, and permits suspension points inside the critical section.
- Crucially, `kotlinx.coroutines.sync.Mutex` is **NOT reentrant**: if a coroutine holding a `Mutex` calls a helper function that attempts `mutex.withLock` on the same `Mutex` instance, it deadlocks permanently.
build-exercise: Launch 1,000 coroutines on `Dispatchers.Default` that each increment `var counter = 0` 100 times and print the result (observing < 100,000). Fix it with `Mutex().withLock`, and then try calling a nested function that also calls `mutex.withLock` on the same mutex to observe the non-reentrant deadlock.
official-references:
- Shared mutable state and concurrency | https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html
- Mutex | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.sync/-mutex/
common-wrong-beliefs:
- `kotlinx.coroutines.sync.Mutex` is reentrant just like Java's `synchronized` or `ReentrantLock` | `Mutex` is non-reentrant; locking the same `Mutex` twice within the same coroutine call chain deadlocks the coroutine.
- Coroutines eliminate race conditions automatically | Coroutines running on multi-threaded dispatchers or suspending mid-mutation suffer from the exact same data races and torn reads/writes as threads.

## concurrency/putting-it-together/fix-a-frozen-screen
interview-questions:
- [intern] If a screen freezes when the user taps a "Load Report" button, what are the first two places you check in the code?
- [junior] How can you use StrictMode or the Android Studio CPU Profiler to pinpoint which function is blocking the main thread?
- [mid] Why might a screen still freeze even when the ViewModel uses `viewModelScope.launch` and calls a `suspend` function?
- [senior] What goes wrong in production when a main-thread freeze is caused by main-thread lock contention or synchronous DataStore/SharedPreferences reads during startup?
what-interviewers-listen-for:
- `viewModelScope.launch` runs on `Dispatchers.Main.immediate`; if a called `suspend` function performs heavy JSON deserialization, Bitmap decoding, or Room/disk access without switching to `Dispatchers.Default` or `Dispatchers.IO`, it blocks the main thread directly.
- System trace / CPU profiler flame charts show long main-thread slices (`Choreographer#doFrame` or coroutine continuation `invokeSuspend`) exceeding 16ms.
- Synchronous `SharedPreferences.getString()` on uncached files or `runBlocking { dataStore.data.first() }` blocks the UI thread waiting on disk I/O.
build-exercise: Build a sample screen where tapping a button calls a `suspend` function that parses a 5MB JSON string or sorts a 2-million-item list without `withContext`, while a loading spinner is on screen. Record a System Trace in Android Studio Profiler to see the long main-thread block, then wrap the work in `withContext(Dispatchers.Default)` and verify smooth 60fps rendering in a second trace.
official-references:
- Keeping your app responsive | https://developer.android.com/training/articles/perf-anr
- Inspect CPU activity with CPU Profiler | https://developer.android.com/studio/profile/cpu-profiler
common-wrong-beliefs:
- Anything running inside `viewModelScope.launch { ... }` is automatically off the main thread | `viewModelScope` defaults to `Dispatchers.Main.immediate`, so any synchronous CPU or I/O work inside it freezes the UI unless moved via `withContext`.

## concurrency/putting-it-together/review-generated-async-code
interview-questions:
- [intern] In a code review, what red flags do you look for when you see `GlobalScope.launch` or `runBlocking` in an Android pull request?
- [junior] What is wrong with a ViewModel function that takes a `Dispatcher` hardcoded as `Dispatchers.IO` inside `launch(Dispatchers.IO)` and updates UI state directly?
- [mid] List four subtle bugs you often find in AI-generated or rushed coroutine/Flow code during a code review.
- [senior] How would you review and refactor a class that mixes callbacks, untracked `CoroutineScope(Dispatchers.IO).launch`, `catch (e: Exception)` swallowing cancellation, and non-atomic `MutableStateFlow.value` mutations?
what-interviewers-listen-for:
- Ad-hoc scope creation (`CoroutineScope(Dispatchers.IO).launch { ... }`) on every method call leaks unstructured coroutines with no cancellation path.
- Swallowing `CancellationException` in generic `catch (e: Exception)` blocks or `runCatching` breaks structured cancellation.
- Hardcoding dispatchers instead of injecting them breaks deterministic unit testing with `StandardTestDispatcher`/`UnconfinedTestDispatcher`, and reading/writing `stateFlow.value` non-atomically causes race conditions.
build-exercise: Write (or generate) a flawed repository/ViewModel snippet containing `CoroutineScope(Dispatchers.IO).launch`, `try { delay(1000) } catch (e: Exception) { log(e) }`, and `state.value = state.value.copy(...)`. Refactor it step-by-step to use injected dispatchers, structured `viewModelScope`, `CancellationException` rethrowing, and `state.update { ... }`, writing a `runTest` unit test to verify cancellation and state transitions.
official-references:
- Best practices for coroutines in Android | https://developer.android.com/kotlin/coroutines/coroutines-best-practices
- Testing Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines/test
common-wrong-beliefs:
- `CoroutineScope(Dispatchers.IO).launch { ... }` is a clean one-liner for background work | Creating an ad-hoc `CoroutineScope` inline throws away the reference to the scope, abandoning structured concurrency and leaking the job past component destruction.

## Self-check
1. Exceptions in coroutines | https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c (Official Android Developers Medium post; verify if non-developer.android.com links are accepted by your parser)
2. Understand ANRs | https://developer.android.com/topic/performance/vitals/anr (Verify exact page title casing on developer.android.com)
3. UI events | https://developer.android.com/topic/architecture/ui-layer/events (Verify exact H1 title vs breadcrumb title on developer.android.com)
4. Inspect CPU activity with CPU Profiler | https://developer.android.com/studio/profile/cpu-profiler (Page title may have been updated to "Profile CPU activity" in newer Studio docs)
5. Job | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/ (Verify KDoc URL structure across kotlinx.coroutines versions)
6. Deferred | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-deferred/ (Verify KDoc URL structure)
7. StateFlow | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/ (Verify KDoc URL structure)
8. SharingStarted | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-sharing-started/ (Verify KDoc URL structure)
9. Channel | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/ (Verify KDoc URL structure)
10. Mutex | https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.sync/-mutex/ (Verify KDoc URL structure)