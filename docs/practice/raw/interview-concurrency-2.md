# interview: concurrency

## concurrency/why-the-main-thread/what-the-main-thread-does
interview-questions:
- [intern] What is the main (UI) thread in Android, and what work runs on it?
- [junior] How do `Looper`, `MessageQueue`, and `Handler` keep the main thread alive and processing UI events?
- [mid] Why are UI toolkits like Android Views and Compose main-thread confined rather than allowing any background thread to mutate UI nodes directly?
- [senior] What goes wrong in production when a background thread mutates a View or non-thread-safe UI state directly without synchronization or main-thread dispatch?
what-interviewers-listen-for:
- The main thread runs an event loop (`Looper.loop()`) that processes input events, lifecycle callbacks, and `Choreographer` vsync frame callbacks sequentially.
- Confining UI tree mutation to a single thread avoids expensive lock contention and deadlocks across thousands of UI nodes on every frame.
build-exercise: Log `Looper.myLooper() == Looper.getMainLooper()` inside a button click listener, a `LaunchedEffect`, and a background `Thread { }`, then try updating a traditional `TextView.text` from the background thread to observe `CalledFromWrongThreadException`.
official-references:
- Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
common-wrong-beliefs:
- Android creates a separate thread for each Activity or screen | Every Activity, Service, and Composable in your process shares the exact same single main thread.

## concurrency/why-the-main-thread/why-blocking-freezes-the-screen
interview-questions:
- [intern] Why does running a slow 2-second task on the main thread make scrolling and animations freeze?
- [junior] How much time does the main thread have to finish a single frame on a 60 Hz display vs a 120 Hz display?
- [mid] What triggers an Application Not Responding (ANR) dialog on Android, and what is the timeout threshold for input dispatch?
- [senior] What goes wrong in production when synchronous disk reads, lock contention, or Binder calls accumulate on the main thread during startup or scrolling?
what-interviewers-listen-for:
- Because the main `MessageQueue` is single-threaded, any long message delays subsequent `Choreographer.doFrame` callbacks (missing the 16.6ms / 8.3ms vsync deadline = dropped frames/jank).
- If an input event or key broadcast cannot be processed within ~5 seconds because the main thread is blocked or deadlocked, the system triggers an ANR.
build-exercise: Enable `StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build()` in `Application.onCreate()`, read a file on the main thread, and inspect the `StrictMode` violation in Logcat.
official-references:
- Keep your app responsive | https://developer.android.com/training/articles/perf-anr
- Slow rendering | https://developer.android.com/topic/performance/vitals/render
common-wrong-beliefs:
- An ANR only happens if a single loop runs for more than 5 seconds on the main thread | An ANR also happens when the main thread waits on a `synchronized` lock or `runBlocking` held by a slow background thread.

## concurrency/threads/running-work-on-another-thread
interview-questions:
- [intern] How do you run a piece of code on a background OS thread, and why can't that thread touch UI views directly?
- [junior] How much memory does a single JVM/OS thread stack allocate, and why is a thread pool (`ExecutorService`) better than `Thread { }.start()`?
- [mid] What is the difference between CPU-bound work (image filtering, JSON parsing) and I/O-bound work (waiting on network or disk) when sizing a thread pool?
- [senior] What goes wrong in production if an app spawns a new raw `Thread` on every scroll event or network call (`OutOfMemoryError: unable to create native thread`)?
what-interviewers-listen-for:
- Each OS thread reserves ~1 MB of virtual stack memory and requires kernel context switches; thread pools reuse a bounded set of worker threads.
- CPU-bound pools should match CPU core count to avoid context-switch thrashing, whereas I/O-bound pools need more threads because threads sit blocked in kernel I/O wait.
build-exercise: Compare memory usage and execution time in Profiler when running 1,000 short tasks by spawning 1,000 raw `Thread` instances versus submitting them to a fixed thread pool or coroutine dispatcher.
official-references:
- Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
common-wrong-beliefs:
- More threads always make a program finish faster | Once active compute threads exceed CPU core count, OS context switching and cache eviction actually slow execution down.

## concurrency/threads/why-raw-threads-get-messy
interview-questions:
- [intern] What happens when you chain 3 sequential async tasks using raw threads and callbacks ("callback hell")?
- [junior] How do you cancel a running `Thread` in Java/Kotlin, and why is it error-prone?
- [mid] Why is propagating exceptions and lifecycle cancellation across multiple raw threads or `ExecutorService` futures difficult?
- [senior] What goes wrong in production when background threads outlive the screen that started them and hold references to outer objects?
what-interviewers-listen-for:
- Raw threads lack structured concurrency (no automatic parent-child cancellation, no automatic exception propagation to a supervisor, and verbose callback marshalling back to the main thread).
- `Thread.interrupt()` only sets a boolean flag or throws `InterruptedException` on blocking calls; developers must manually wire cancellation tokens everywhere.
build-exercise: Write two dependent background tasks using nested callbacks and `Handler(Looper.getMainLooper()).post { }`, then rewrite the exact same logic in 4 sequential lines inside a coroutine.
official-references:
- Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines
common-wrong-beliefs:
- Calling `thread.interrupt()` immediately stops any running thread | `interrupt()` only sets an interruption flag; if the thread isn't inside a blocking sleep/wait or checking `isInterrupted`, it keeps running.

## concurrency/coroutine-basics/what-is-a-coroutine
interview-questions:
- [intern] How is a Kotlin coroutine different from an OS thread?
- [junior] Why can an Android app easily launch 50,000 concurrent coroutines when launching 50,000 OS threads would crash the app?
- [mid] How can thousands of coroutines multiplexed onto a small pool of threads suspend and resume on different threads?
- [senior] What goes wrong in production when a developer assumes coroutines never use threads and runs blocking I/O on `Dispatchers.Main` or `Dispatchers.Default`?
what-interviewers-listen-for:
- A coroutine is a lightweight, suspendable computation (an object on the heap holding state and a `Continuation`) scheduled onto a pool of underlying OS threads by a `CoroutineDispatcher`.
- Suspending frees the underlying OS thread to execute other coroutines rather than parking the thread stack in kernel space.
build-exercise: Launch 10,000 coroutines inside `runBlocking` that each `delay(1000L)` and log `Thread.currentThread().name` before and after `delay` on `Dispatchers.Default` to see multiple coroutines sharing a handful of worker threads.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Kotlin coroutines on Android | https://developer.android.com/kotlin/coroutines
common-wrong-beliefs:
- Coroutines replace threads so no threads exist under the hood | Coroutines always execute on OS threads managed by a `CoroutineDispatcher`.

## concurrency/coroutine-basics/launch-a-coroutine
interview-questions:
- [intern] What does `scope.launch { ... }` do, and what does it return?
- [junior] In what order do lines before, inside, and immediately after `scope.launch { ... }` execute?
- [mid] What can you do with the `Job` returned by `launch` (`join()`, `cancel()`, `isActive`)?
- [senior] What goes wrong in production when code launches a coroutine with `launch` to save data and immediately reads the database on the next line outside `launch` assuming the save already finished?
what-interviewers-listen-for:
- `launch` is a "fire-and-forget" coroutine builder for work that doesn't return a result value; it schedules the coroutine and immediately returns a `Job` handle without suspending the caller.
- Code right after `launch { ... }` runs concurrently (and usually before the launched block completes) unless you `.join()` the `Job` or run sequentially inside the same coroutine.
build-exercise: Log `"1"`, call `scope.launch { delay(100); log("2") }`, and log `"3"` on the line after `launch`. Predict and verify the print order (`1, 3, 2`), then add `job.join()` and observe (`1, 2, 3`).
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
common-wrong-beliefs:
- Code inside `launch { }` always starts running on a background thread | `launch` inherits the `CoroutineDispatcher` of its parent `CoroutineScope` (for `viewModelScope` and `lifecycleScope`, that is `Dispatchers.Main`) unless overridden.

## concurrency/coroutine-basics/async-and-await
interview-questions:
- [intern] How do you run two independent network calls in parallel and combine their results using `async` and `await`?
- [junior] What is the difference between `Job` (returned by `launch`) and `Deferred<T>` (returned by `async`)?
- [mid] Why does calling `val a = async { fetchA() }.await()` followed by `val b = async { fetchB() }.await()` run sequentially instead of in parallel?
- [senior] What goes wrong in production when developers use `async { ... }` without ever calling `.await()`, especially regarding exception handling?
what-interviewers-listen-for:
- Starting both `val aDeferred = async { ... }` and `val bDeferred = async { ... }` before calling `aDeferred.await() + bDeferred.await()` runs them concurrently, cutting total wait time from $T_A + T_B$ to $\max(T_A, T_B)$.
- Calling `.await()` on the same line as `async` suspends immediately before the second `async` is even created, losing all parallelism.
build-exercise: Write two suspend functions that each `delay(1000)`. Measure elapsed time with `measureTimeMillis` when calling them sequentially (~2000ms) vs concurrently with `coroutineScope { val a = async { ... }; val b = async { ... }; a.await() + b.await() }` (~1000ms).
official-references:
- Composing suspending functions | https://kotlinlang.org/docs/composing-suspending-functions.html
common-wrong-beliefs:
- Wrapping a suspend call in `async { }.await()` makes it faster | `async { f() }.await()` on a single call is equivalent to just calling `f()` directly, with extra allocation overhead.

## concurrency/suspend/what-suspend-means
interview-questions:
- [intern] What does the `suspend` keyword on a function tell the Kotlin compiler?
- [junior] When a `suspend` function hits a suspension point (like `delay` or a network call), what happens to the thread it was running on?
- [mid] How does the Kotlin compiler transform a `suspend` function under the hood using Continuation-Passing Style (CPS) and a state machine?
- [senior] What goes wrong in production when a developer puts the `suspend` keyword on a CPU-heavy function (like parsing a 20 MB file) and calls it on `Dispatchers.Main` without `withContext`?
what-interviewers-listen-for:
- The compiler rewrites `suspend fun foo(): T` by adding an implicit `Continuation<T>` parameter and generating a state machine (`label`) that saves local variables across suspension points (`COROUTINE_SUSPENDED`).
- Marking a function `suspend` does NOT move it to a background thread; if it performs synchronous blocking work without switching dispatchers, it still blocks the caller's thread.
build-exercise: Write a simple `suspend fun steps()` that calls `delay(100)` twice with local variables in between, then open **Tools -> Kotlin -> Show Kotlin Bytecode -> Decompile** in Android Studio to inspect the generated `Continuation` state machine and `label` switch.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
- Improve app performance with Kotlin coroutines | https://developer.android.com/kotlin/coroutines/coroutines-adv
common-wrong-beliefs:
- Adding the `suspend` keyword to a function makes it run on a background thread automatically | `suspend` only enables suspension points; a `suspend` function runs on the caller's thread until it switches context with `withContext`.

## concurrency/suspend/suspend-vs-blocking
interview-questions:
- [intern] Why does `delay(2000)` on `Dispatchers.Main` keep the screen responsive while `Thread.sleep(2000)` freezes it?
- [junior] When is `runBlocking` acceptable (such as in `main()` or tests), and why must you never call `runBlocking` on the Android main thread?
- [mid] How does a blocking I/O call (like `InputStream.read()` or a synchronous OkHttp `.execute()`) differ from a suspending call at the OS thread level?
- [senior] What goes wrong in production if `runBlocking` is called on `Dispatchers.Main` waiting for a coroutine that itself needs `Dispatchers.Main` to complete?
what-interviewers-listen-for:
- `delay()` schedules a resume callback on the dispatcher's event loop and immediately returns control of the thread; `Thread.sleep()` and `runBlocking` park the OS thread itself.
- Calling `runBlocking` on `Dispatchers.Main` blocks the main `Looper`, causing instant jank/ANRs and an immediate deadlock if any inner coroutine dispatches back to `Dispatchers.Main`.
build-exercise: Put a spinning `CircularProgressIndicator` on screen and add two buttons: one that calls `Thread.sleep(2000)` inside a coroutine on `Dispatchers.Main`, and one that calls `delay(2000)`. Tap both and observe the spinner freeze vs keep spinning.
official-references:
- Coroutines basics | https://kotlinlang.org/docs/coroutines-basics.html
common-wrong-beliefs:
- Calling `Thread.sleep()