---
id: concurrency/threads/running-work-on-another-thread
title: "How do I run a slow job beside the main thread?"
status: shadow
difficulty: easy
order: 3
requires: [concurrency/why-the-main-thread/why-blocking-freezes-the-screen]
summary: "Start a helper thread for the slow job so the main thread keeps drawing. The job itself does not get faster."
video: ""
publishedOn:
updatedOn:
sims: [second-cashier]
playground: true
fruit: false
upnext: false
---

<!--
SOURCES (read 21 Oct 2026):
- https://developer.android.com/guide/components/processes-and-threads (the main thread is also called the UI thread; two rules: do not block the UI thread, do not access the UI toolkit from outside it; non-instant work goes in background or worker threads; Activity.runOnUiThread, View.post and View.postDelayed reach the UI thread)
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.concurrent/thread.html (thread(start = true, ..., name, block) creates a thread that runs the block and, by default, starts it)
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html (start schedules the thread and it runs independently of the current thread; a thread can be started once; run is not meant to be called directly and runs the task; join waits for the thread to terminate)
⚠️ VERIFY: compile and run not done for any kotlin block. Output order relies on join(), so it should be fixed, but a reviewer must run each block. The thread name "main" for the Playground's main thread is not documented here.
⚠️ VERIFY: the roadmap says the Playground block shows the print order of a main program and a thread. docs/06 asks for deterministic output, so every block here uses join() to fix the order.
⚠️ VERIFY: the roadmap Notes mention os/threads/what-is-a-thread as a helpful earlier leaf. This leaf recaps what a thread is in the Name it paragraph.
-->

## The question

You tap Save and the screen freezes while a big file is read. The read has to happen. How do you get it done without stopping the screen? And does the read finish sooner if you do it somewhere else?

## Mental model

Think of the small shop from before, with one cashier.

- The main counter is the only counter. Its cashier serves every customer and also tidies the window display.
- A draw customer joins the line every 16 ticks and wants the display tidied. A tick is a tiny slice of shop time.
- A heavy order is a job that takes 800 ticks, such as counting the whole stockroom.
- A helper counter is a second counter with its own cashier.
- The window display is the screen.

If the heavy order goes to the main counter, the line stands still for 800 ticks. If it goes to the helper counter, the main counter keeps serving. The heavy order still takes 800 ticks. The helper never tidies the window display. It leaves a note in the main line, and the main cashier tidies it.

**Where the analogy stops:** A second cashier costs wages, and a second thread costs memory (general knowledge). Real cashiers can talk to each other and agree, but two threads can both change the same data and clash. Also, in a shop you see who is serving whom. In code you have to ask which thread is running a line.

## See it move

```sim
id: second-cashier
template: lanes
title: A heavy order and a helper counter
intro: "One main counter draws the window display. A heavy order is about to arrive. The helper counter is closed."
lanes:
  - { id: main-line, label: "Main counter's line (it draws the window display)", kind: queue, slots: 3 }
  - { id: helper-line, label: "Helper counter's line", kind: queue, slots: 2 }
  - { id: window, label: "Window display", kind: row, slots: 1 }
items:
  - { id: display, label: "Display: old price", at: "window.0" }
vars: { helper: "off", busy: 0, missed: 0, shown: 0 }
show:
  - { var: missed, label: "Draw jobs the main counter missed" }
  - { var: shown, label: "Results shown on the display" }
toggles:
  - { id: open-helper, label: "Open a helper counter", var: helper }
actions:
  - id: bring
    label: "Bring a heavy order (800 ticks)"
    cases:
      - when: { var: { busy: 0, helper: "on" } }
        do:
          - spawn: { id: heavy, label: "Heavy order (800 ticks)", at: "helper-line.0" }
          - set: { busy: 1, missed: 0 }
          - say: "The heavy order goes to the helper counter. It still needs 800 ticks, but the main counter keeps serving draw jobs, so it misses {missed}."
      - when: { var: { busy: 0 } }
        do:
          - spawn: { id: heavy, label: "Heavy order (800 ticks)", at: "main-line.0" }
          - mark: { heavy: bad }
          - set: { busy: 1, missed: 50 }
          - say: "The heavy order goes to the main counter and holds it for 800 ticks. Draw jobs arrive every 16 ticks, so the main counter misses {missed} of them."
    refuse: "A heavy order is already being served. Press Reset to start again."
  - id: report
    label: "Helper hands the result over"
    cases:
      - when: { at: { heavy: "helper-line.0" } }
        do:
          - remove: heavy
          - spawn: { id: note, label: "Note: the result", at: "main-line.0" }
          - say: "The helper finishes and does not touch the window display. It leaves a note in the main line."
    refuse: "No heavy order is waiting at the helper counter. Open the helper counter and bring a heavy order first."
  - id: serve
    label: "Main counter reads the note"
    cases:
      - when: { at: { note: "main-line.0" } }
        do:
          - remove: note
          - label: { display: "Display: new price" }
          - add: { shown: 1 }
          - say: "The main cashier reads the note and tidies the window display. The display now shows the new price."
    refuse: "There is no note in the main line yet."
script: [ bring, "open-helper=on", report, serve ]
try:
  - text: "Press \"Bring a heavy order\" while the helper counter is closed. The order sits at the main counter. Read the missed count."
    done: [ bring ]
  - text: "Switch on \"Open a helper counter\". The same order now goes to the helper counter and the missed count drops to 0. The order still needs 800 ticks."
    done: [ "open-helper=on" ]
  - text: "Press \"Helper hands the result over\", then \"Main counter reads the note\". Only the main counter changes the window display."
    done: [ report, serve ]
```

**What you should notice:** The heavy order takes 800 ticks at either counter, but with a helper the main counter misses no draw jobs, and only the main counter touches the window display.

## Build the logic

### Notice

The shop has one cashier. A draw customer joins the line every 16 ticks. A heavy order arrives and needs 800 ticks. If the main cashier takes it, 800 divided by 16 is 50 draw customers wait in the line, and the display is not tidied once.

### Guess

What should the shop do? Here are three ideas.

- A. Open a second counter, and the heavy order will finish in about 200 ticks instead of 800.
- B. Open a second counter and send the heavy order there. It still takes 800 ticks, but the main line keeps moving.
- C. Tell the draw customers to wait until the heavy order is done.

Idea A is believable, because more workers sounds like less time. But one order is still one job for one cashier, so it takes as long. Idea C is what happens when nothing is done. Idea B is the answer.

### Build

1. Open a second counter with its own cashier.
2. Hand the heavy order to the second cashier. The main cashier carries on with the line.
3. The heavy order still takes 800 ticks. It is not faster. The main line is only free.
4. When the helper finishes, it must not tidy the window display itself. Only the main cashier does that.
5. So the helper leaves a note in the main line. The main cashier reads the note when its turn comes and tidies the display.
6. If the main cashier waits at the helper counter for the order to finish, nothing is gained. The main line stands still again.

### Name it

A **thread** is one worker that runs one job at a time. Every app starts with the main thread. The Android documentation says that if you have operations that are not instantaneous, do them in separate background or worker threads. It gives two rules for the main thread, which it also calls the UI thread: do not block it, and do not access the Android UI toolkit from outside it. The UI toolkit is the part of Android that draws the screen.

In Kotlin, the **`thread`** function from `kotlin.concurrent` creates a thread that runs the block you give it. The Kotlin documentation says that by default the thread is started at once. To **start** a thread means to ask the system to begin running it beside the current thread. The Java documentation says a started thread runs independently of the current thread. A thread can be started only once.

The method **`join`** waits for a thread to finish. The Java documentation says it waits for the thread to terminate. Without `join`, the main thread carries on and does not wait.

There is a trap in the name `run`. The Java documentation says `run` is not meant to be called directly, and that it runs the task. Calling `run` yourself runs the job on the thread that called it. Only starting a thread gives you a new one.

To get a result back to the screen, the Android documentation lists `Activity.runOnUiThread`, `View.post` and `View.postDelayed`. These hand a piece of work to the UI thread, like the note in the main line.

| Shop | Kotlin |
|---|---|
| Open a helper counter and send the order | `thread { ... }` |
| Wait for the order to be done | `helper.join()` |
| The helper leaves a note in the main line | `runOnUiThread`, `View.post` |

Here is the smallest program. It prints in a fixed order, because the main thread waits at `join`.

```kotlin
import kotlin.concurrent.thread

fun main() {
    println("Main: start")
    val helper = thread(name = "helper") {
        println("Helper: counting the stockroom")
    }
    helper.join()
    println("Main: done")
}
```

This prints `Main: start`, then `Helper: counting the stockroom`, then `Main: done`. The last line waits for `join`, so the helper line always comes before it.

## Predict the output

```challenge
type: predict
id: which-thread-prints
verified: "UNVERIFIED: compile not run"
code: |
  import kotlin.concurrent.thread

  fun main() {
      val helper = thread(name = "helper") {
          println("Job runs on: " + Thread.currentThread().name)
      }
      helper.join()
      println("Main runs on: " + Thread.currentThread().name)
  }
options:
  - id: a
    text: "Job runs on: main, then Main runs on: main"
    why: "You may have thought a thread block still runs on the main counter. Starting a thread opens a helper counter, and the job runs there."
  - id: b
    text: "Job runs on: helper, then Main runs on: helper"
    why: "You may have thought the helper takes over everything. It only takes the job. The line after join runs back on the main counter."
  - id: c
    text: "Job runs on: helper, then Main runs on: main"
    correct: true
    why: "The block is the heavy order, and it goes to the helper counter. The main line waits at join and then carries on at its own counter."
output: |
  Job runs on: helper
  Main runs on: main
```

```challenge
type: predict
id: run-is-not-start
verified: "UNVERIFIED: compile not run"
code: |
  fun main() {
      val job = Thread { println("Job runs on: " + Thread.currentThread().name) }
      job.run()
      println("Main runs on: " + Thread.currentThread().name)
  }
options:
  - id: a
    text: "Job runs on: main, then Main runs on: main"
    correct: true
    why: "Calling run yourself hands the order to the cashier who is already standing there. No helper counter was opened, so the job runs on the main counter."
  - id: b
    text: "Job runs on: Thread-0, then Main runs on: main"
    why: "You may have thought run opens a helper counter. Only start does that. Calling run does the job where you stand."
  - id: c
    text: "Only Main runs on: main, because the job was never started"
    why: "You may have thought a job does nothing until it is started. A thread started with start runs beside you. But run is an ordinary method call, so the job runs at once on your own thread."
output: |
  Job runs on: main
  Main runs on: main
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `Main: start`, then `Helper: counting the stockroom`, then `Main: done`.
3. **Break.** Delete the line `helper.join()` and change nothing else. Run it a few times. The main thread no longer waits, so `Main: done` can come before or after the helper line. Do not expect the same order every time.
4. **Fix.** Put the line `helper.join()` back. The three lines come out in the same order every time again.

## Fix the bug

```challenge
type: fix
id: fix-slow-job-on-main-line
verified: "UNVERIFIED: compile not run"
code: |
  // Not real Android. Thread.sleep stands for a slow job.
  fun loadReport(): String {
      Thread.sleep(300)
      return "report ready"
  }

  fun main() {
      println("Screen: drawing")
      // The slow job runs on the main line, so the line below waits for it.
      val report = loadReport()
      println("Job: " + report)
      println("Screen: still drawing")
  }
hints:
  - "Look at the line that calls loadReport. Which thread runs it, and what has to wait?"
  - "Start a helper thread for loadReport and keep its result. Print the screen line, then wait with join before you print the job line."
expected: |
  Screen: drawing
  Screen: still drawing
  Job: report ready
fix: |
  import kotlin.concurrent.thread

  // Not real Android. Thread.sleep stands for a slow job.
  fun loadReport(): String {
      Thread.sleep(300)
      return "report ready"
  }

  fun main() {
      println("Screen: drawing")
      var report = ""
      val helper = thread(name = "helper") {
          report = loadReport()
      }
      println("Screen: still drawing")
      helper.join()
      println("Job: " + report)
  }
explanation: "The slow job sat on the main line, so the screen line waited behind it. On a helper thread the screen line prints at once, and join makes the main line wait only when it needs the result."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. For question 4, a click handler runs on every tap, so this code would start a new helper thread each time. Starting too many threads has a cost, and that is a reason to look for a better tool.

## Tricks to remember

- A helper counter does not make the order faster. It frees the main line.
- `start` opens a helper counter. `run` does the job where you stand.
- `join` is waiting at the helper counter. Do it only when you need the result.
- Only the main counter tidies the window display.
- Task, in a plain Kotlin project: start ten threads that each print their number, and run it a few times. The order changes, and that shows why you cannot count on it.

## Common mistakes

- **Thinking a new thread makes the job faster.** More workers sounds like less time. One job is still one job, so it takes as long. What changes is that the main line is free.
- **Calling `run` instead of starting the thread.** The name sounds like "run it". The Java documentation says `run` is not meant to be called directly. It runs the job on your own thread, so nothing moves to a helper.
- **Touching the screen from the helper thread.** The result is ready, so updating the label right there feels natural. The Android documentation says not to access the UI toolkit from outside the UI thread. Hand the result to the main thread instead.
- **Reading the helper's result before it is ready.** The code is right below the line that starts the thread, so the result feels ready. The helper may still be working. Wait with `join`, or hand the result back when it is done.

## Where this shows up in Android

You tap Save and the screen freezes while a file is read. That is the heavy order at the main counter. The Android documentation tells you to do work that is not instantaneous in a separate worker thread, and not to block the UI thread.

A second symptom: the file is read in a helper thread, and then the code tries to update a label from that thread. The documentation says the Android UI toolkit is not thread-safe, so do all changes to the UI from the UI thread. The note in the main line is how the result gets there. The documentation lists `runOnUiThread` and `View.post` for this. Its own examples also show executors and Kotlin coroutines as ways to run the work.

## Say it in an interview

**How do you run work on a background thread in Kotlin, and why can't that thread change the UI?**
In Kotlin I can use the `thread` function from `kotlin.concurrent`, which starts a thread that runs my block. The Android documentation says non-instant work belongs in a worker thread. It also says the UI toolkit is not thread-safe, so all changes to the UI must come from the UI thread.

**How do you get a result from a background thread back to the screen?**
You hand a piece of work to the UI thread. The Android documentation lists `Activity.runOnUiThread`, `View.post` and `View.postDelayed` for this. The trade-off is that you must remember to do it every time. My own reasoning: forgetting is a common bug, and later tools such as coroutines make it harder to forget.

**What is the difference between `start` and `run` on a Thread?**
`start` asks the system to run the thread beside the current one. The Java documentation says a thread can be started only once. `run` is not meant to be called directly, and calling it runs the task on the thread you are already on, so nothing runs in the background.

**Why use a thread pool instead of a new thread for each task?**
This answer is general knowledge and my own reasoning. Each thread costs memory, and starting one takes time. A pool reuses a fixed number of threads, so a burst of tasks queues up and does not create hundreds of threads. The trade-off is that tasks may wait in the queue.

## Recap

1. A helper thread runs the slow job so the main thread keeps drawing.
2. The job takes the same time, and only the main thread may touch the screen.
3. Starting a thread opens a helper, calling `run` does not, and `join` waits for the result.

## You can now

- Predict which thread prints a line in a short program that uses `join`.
- Predict what calling `run` instead of `start` does.
- Fix a program where a slow job on the main line delays the lines after it.
- Explain, using the shop, why a helper thread does not make the job faster.
