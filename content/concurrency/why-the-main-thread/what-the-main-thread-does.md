---
id: concurrency/why-the-main-thread/what-the-main-thread-does
title: "Which thread draws my screen and runs my click code?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: One thread, the main thread, draws your screen and runs your tap code. Every job it has waits in one line.
video: ""
publishedOn:
updatedOn:
sims: [main-thread-queue]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. Android claims were checked on 21 Oct 2026 against https://developer.android.com/guide/components/processes-and-threads (main thread = UI thread dispatches events including drawing; lifecycle callbacks run on it; long work on it blocks all events including drawing; the page notes that under special circumstances an app's main thread might not be its UI thread, which this lesson leaves out). The sentence that parts of an app run in the same thread by default, called the main thread, is from memory of the same page and was not confirmed in the fetched summary: re-read it before live. NOT checked against an official page: the picture of tasks waiting in one line. It is labelled a simplified model in the text; the Looper/MessageQueue detail is deliberately not named. The sim id main-thread-queue has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/main-thread-queue.md; kind code-to-screen, so it needs the caption "A picture of what Android does. Not a real phone." and its sources). Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. Model code is plain Kotlin and labelled "not real Android". -->
## The question

You tap a Like button. A heart turns red. A second later you tap a different button, and the screen gives a little jump.

Your code runs when you tap, and something draws the heart. Is that the same worker doing both jobs? And does each button get its own worker, so taps never wait for each other?

## Mental model

Think of a small shop with one cashier.

- The cashier is the one worker who does the jobs.
- The customers are the jobs: serve someone, tidy the window, answer the phone.
- The line is the queue where every customer waits their turn.
- The window display is the screen. Only the cashier tidies it.

The cashier serves the customer at the front. When that customer is done, the next one steps up. Nobody is served out of order, and the cashier does one job at a time.

**Where the analogy stops:** A real shop can hire more cashiers. An Android app also has other workers, called background workers, and later lessons use them. But only the cashier is allowed to touch the window display. Also, a real queue is a simplified picture here. This lesson uses it to show the idea of waiting, not to describe how Android is built inside.

## See it move

<!-- SIM: main-thread-queue -->

**Try this:**
1. Press "Tap" three times in a row. Watch each tap's job wait in the line, and watch the counter serve the front of the line.
2. Switch the tap job from short to long, then tap once. Watch where the next "Draw" job waits.

**What you should notice:** One counter serves one job at a time, and a long job in front makes the draw job behind it wait.

## Build the logic

### Notice

A shop has one cashier and three customers in line. The first customer wants to pay, which takes 1 minute. The second wants a big gift wrapped, which takes 5 minutes. The third wants to pay, which takes 1 minute.

The cashier starts with the first customer. The third customer cannot be served until the second is done.

### Guess

How long until the third customer is finished?

- A. 1 minute, because that customer's job is short.
- B. 7 minutes, because they wait for the two before them and then take their own turn.
- C. 5 minutes, because only the longest job matters.

Idea A feels right because a short job sounds fast. But one cashier means one line. Idea C counts only the slow job and forgets the first customer. Idea B is how a single line works.

### Build

1. There is one worker for the screen and the tap handling.
2. Every job, such as "draw" or "run the tap code", joins one line.
3. The worker takes the first job in the line and does all of it.
4. Only then does it take the next job.
5. So a job finishes at the time of everything in front of it, plus its own time.

### Name it

A **thread** is one worker line of steps. The Android documentation says the parts of an app run in the same thread by default, called the **main thread**. The main thread is also called the **UI thread**. In the documentation's words, it is in charge of dispatching events to the user interface widgets, including drawing events.

That is why one thread does both jobs in the opening question. Drawing is one kind of job. Handling your tap is another. Your click code runs on the same thread that draws the heart. The documentation also says that system calls to each app component, such as the callbacks that run when a screen opens, are dispatched from that thread.

A single job is a **task**. Here is the shop as a model in plain Kotlin, not real Android. It serves the tasks in the order they were added.

```kotlin
// A model in plain Kotlin, not real Android.
fun main() {
    val queue = ArrayDeque<String>()
    queue.addLast("run tap code")
    queue.addLast("draw screen")
    queue.addLast("run timer code")
    while (queue.isNotEmpty()) {
        println("Counter does: " + queue.removeFirst())
    }
}
```

It prints three lines, in the order the tasks were added: `Counter does: run tap code`, then `Counter does: draw screen`, then `Counter does: run timer code`.

## Predict the output

```challenge
type: predict
id: order-in-the-line
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun main() {
      val queue = ArrayDeque<String>()
      queue.addLast("run tap code")
      queue.addLast("draw screen")
      queue.addLast("run timer code")
      while (queue.isNotEmpty()) {
          println("Counter does: " + queue.removeFirst())
      }
  }
options:
  - id: a
    text: "draw screen first, then the two code tasks"
    why: "You may have thought drawing is more important, so it jumps the line. One cashier serves the line in order."
  - id: b
    text: "run tap code, draw screen, run timer code"
    correct: true
    why: "The tasks join the line in this order, and the cashier serves the front of the line each time."
  - id: c
    text: "All three at the same time"
    why: "You may have pictured three workers. There is one cashier, so one job runs at a time."
output: |
  Counter does: run tap code
  Counter does: draw screen
  Counter does: run timer code
```

```challenge
type: predict
id: when-does-the-second-draw-finish
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun main() {
      val tasks = listOf("draw" to 1, "tap code" to 5, "draw" to 1)
      var clock = 0
      for (task in tasks) {
          clock += task.second
          println("${task.first} finished at $clock")
      }
  }
options:
  - id: a
    text: "draw finished at 1, tap code finished at 5, draw finished at 1"
    why: "You may have counted each job on its own. In one line, each job also waits for everything in front of it."
  - id: b
    text: "draw finished at 1, tap code finished at 6, draw finished at 7"
    correct: true
    why: "The tap code starts after the first draw, and the second draw waits for both of them before it takes its own 1."
  - id: c
    text: "draw finished at 1, tap code finished at 6, draw finished at 2"
    why: "You may have thought the short draw skips ahead. The cashier never reorders the line."
output: |
  draw finished at 1
  tap code finished at 6
  draw finished at 7
```

## Try it

1. **Write.** Type the second program from Predict the output into the editor.
2. **Run.** You should see three lines: `draw finished at 1`, `tap code finished at 6`, `draw finished at 7`.
3. **Break.** Change the 5 next to `"tap code"` to 50 and change nothing else. Run it again.
4. **Fix.** The second draw now finishes at 52 instead of 7. The code is not wrong. It shows what a slow job does to the line behind it. Change the 50 back to 5 and run it. You see 1, 6 and 7 again. A later lesson shows how to move slow work to another worker.

## Fix the bug

```challenge
type: fix
id: fix-slow-job-in-front
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun main() {
      // We want the first picture on the screen right away,
      // then the big list loaded, then the list drawn.
      val tasks = listOf(
          "load big list" to 40,
          "draw screen" to 1,
          "draw list" to 1
      )
      var clock = 0
      for (task in tasks) {
          clock += task.second
          println("${task.first} finished at $clock")
      }
  }
hints:
  - "Read the time that 'draw screen' finishes at. Is it the first picture, right away?"
  - "A job finishes after everything in front of it. What stands in front of 'draw screen'?"
expected: |
  draw screen finished at 1
  load big list finished at 41
  draw list finished at 42
fix: |
  // A model in plain Kotlin, not real Android.
  fun main() {
      val tasks = listOf(
          "draw screen" to 1,
          "load big list" to 40,
          "draw list" to 1
      )
      var clock = 0
      for (task in tasks) {
          clock += task.second
          println("${task.first} finished at $clock")
      }
  }
explanation: "The slow job stood in front of the draw job, so the first picture waited 40 steps. Moving the draw job to the front lets it finish at 1. The slow job still blocks the line behind it."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a short answer: this code has no null values in play. Spend your time on question 2, and on a new question: is moving the draw job to the front the real fix, or does the 40-step job still hurt the user later?

## Tricks to remember

- One cashier, one line, one job at a time.
- A job finishes after everything in front of it, plus its own time.
- Draw and tap code share the same cashier.
- A short job behind a long job still waits.

## Common mistakes

- **Thinking each button has its own thread, so taps never wait for each other.** It feels right because buttons look separate on the screen. The Android documentation says the main thread dispatches events to the widgets, so every tap is handled in the same line.
- **Thinking drawing is a separate worker from your code.** Drawing feels like the system's job and not yours. The documentation puts drawing events on the same thread that runs your callbacks, so your code can delay the next draw.
- **Thinking a short job is always quick.** Its own work is quick, but it also waits for every job in front of it.
- **Thinking the line reorders itself to be fair.** It feels fair to let small jobs go first. In this lesson's picture, the cashier serves the line in order.

## Where this shows up in Android

You tap a button and the screen freezes for a moment: the heart does not turn red, the animation stutters. Your tap code was a long job at the front of the line, and the draw job waited behind it.

The Android documentation says that long operations on the UI thread, such as network access or database queries, block the whole UI, and when the thread is blocked no events can be dispatched, including drawing events. If it stays blocked for more than a few seconds, the system shows the "application not responding" dialog.

The next lesson in this branch looks at exactly this freeze.

## Recap

1. One thread, the main thread, draws the screen and runs your tap code.
2. Its jobs wait in one line, and it does one job at a time.
3. A job finishes after everything in front of it, so a long job delays the next draw.

## You can now

- Explain why a tap handler and the screen drawing share one worker, using the cashier and the line.
- Predict the order and finish times of three tasks that share one line.
- Fix a task list where a slow job delays the first picture.
