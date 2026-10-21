---
id: concurrency/why-the-main-thread/what-the-main-thread-does
title: "Which thread draws my screen and runs my click code?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: One thread, the main thread, draws your screen and runs your tap code. In our picture, its jobs wait in one line.
video: ""
publishedOn:
updatedOn:
sims: [main-thread-queue]
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/guide/components/processes-and-threads ("By default, all components of the same application run in the same process and thread, called the main thread"; the thread "is in charge of dispatching events to the appropriate user interface widgets, including drawing events"; "The system does not create a separate thread for each instance of a component"; "When the thread is blocked, no events can be dispatched, including drawing events"; ANR dialog if blocked "for more than a few seconds"; the two rules of the single-thread model). The picture of jobs waiting in one line is a simplified model and is labelled "my own reasoning" in the text. No Looper or MessageQueue detail is claimed, because the Looper reference page could not be read in this session. The sim main-thread-queue is a code-to-screen sim, so it needs the caption "A picture of what Android does. Not a real phone." The code is a plain Kotlin model, not real Android. It ran on Kotlin 2.4.20 only, and the outputs were traced by hand for this rewrite. -->

## The question

You tap a Like button and a heart turns red. A second later you tap a different button, and the screen gives a little jump. Your code runs when you tap, and something draws the heart. Is the same worker doing both jobs? Does each button get its own worker, so taps never wait for each other?

## Mental model

Think of a small shop with one cashier.

- The cashier is the one worker who does the jobs.
- The customers are the jobs: serve someone, tidy the window, answer the phone.
- The line is where every customer waits their turn.
- The window display is the screen. Only the cashier tidies it.

The cashier serves the customer at the front. When that customer is done, the next one steps up. Nobody is served out of order, and the cashier does one job at a time.

**Where the analogy stops:** A real shop can hire more cashiers, and an app can start other workers too. But the Android documentation says you must not touch the screen's tools from any worker except the main one. Also, the line is my own simplified picture of waiting. It is not a description of how Android is built inside.

## See it move

<!-- SIM: main-thread-queue -->

**Try this:**
1. Press "Tap" three times in a row. Watch each tap's job wait in the line, and watch the counter serve the front of the line.
2. Switch the tap job from short to long, then tap once. Watch where the next "Draw" job waits.

**What you should notice:** One counter serves one job at a time, and a long job in front makes the draw job behind it wait.

## Build the logic

### Notice

A shop has one cashier and three customers in line. The first customer wants to pay, which takes 1 minute. The second wants a big gift wrapped, which takes 5 minutes. The third wants to pay, which takes 1 minute. The cashier starts with the first customer. The third customer cannot be served until the second is done.

### Guess

How long until the third customer is finished?

- A. 1 minute, because that customer's job is short.
- B. 5 minutes, because only the longest job matters.
- C. 7 minutes, because they wait for the two before them and then take their own turn.

Idea A feels right because a short job sounds fast. But one cashier means one line. Idea B counts only the slow job and forgets the first customer. Idea C is how a single line works.

### Build

1. One worker does the screen jobs and the tap jobs.
2. Every job, such as "draw" or "run the tap code", joins one line.
3. The worker takes the first job in the line and does all of it.
4. Only then does it take the next job.
5. So a job finishes at the time of everything in front of it, plus its own time.

### Name it

A **thread** is one worker following a line of steps. The Android documentation says that by default all components of the same app run in the same thread, called the **main thread**. It is also called the **UI thread**. The documentation says this thread is in charge of dispatching events to the user interface widgets, including drawing events.

That is why one thread does both jobs in the opening question. Drawing is one kind of job. Handling your tap is another. The documentation also says the system does not create a separate thread for each instance of a component, and that system calls to each component are dispatched from the main thread.

A single job is a **task**. The line is my own reasoning, a simplified model of waiting. Here is the shop in plain Kotlin, not real Android. It serves the tasks in the order they were added.

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

It prints `Counter does: run tap code`, then `Counter does: draw screen`, then `Counter does: run timer code`.

## Predict the output

```challenge
type: predict
id: order-in-the-line
verified: not-run
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
    text: "All three at the same time"
    why: "You may have pictured three workers. There is one cashier, so one job runs at a time."
  - id: c
    text: "run tap code, draw screen, run timer code"
    correct: true
    why: "The tasks join the line in this order, and the cashier serves the front of the line each time."
output: |
  Counter does: run tap code
  Counter does: draw screen
  Counter does: run timer code
```

```challenge
type: predict
id: when-does-the-second-draw-finish
verified: not-run
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
    text: "draw finished at 1, tap code finished at 6, draw finished at 7"
    correct: true
    why: "The tap code starts after the first draw, and the second draw waits for both of them before it takes its own 1."
  - id: b
    text: "draw finished at 1, tap code finished at 5, draw finished at 1"
    why: "You may have counted each job on its own. In one line, each job also waits for everything in front of it."
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
2. **Run.** You should see three lines: `draw finished at 1`, `tap code finished at 6` and `draw finished at 7`.
3. **Break.** Change the 5 next to `"tap code"` to 50 and change nothing else. Run it again.
4. **Fix.** The second draw now finishes at 52 instead of 7. The code is not wrong. It shows what a slow job does to the line behind it. Change the 50 back to 5 and run it. You see 1, 6 and 7 again.

## Fix the bug

```challenge
type: fix
id: fix-slow-job-in-front
verified: not-run
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
  - "Read the time that 'draw screen' finishes at. Is that the first picture, right away?"
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
explanation: "The slow job stood in front of the draw job, so the first picture waited 40 steps. Moving the draw job to the front lets it finish at 1."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. Spend your time on question 2, and on one more: after the fix, the 40-step job still holds the line. Does it still hurt the user?

## Tricks to remember

- One cashier, one line, one job at a time.
- A job finishes after everything in front of it, plus its own time.
- Drawing and tap code share the same cashier.

## Common mistakes

- **Thinking each button or screen has its own thread, so taps never wait for each other.** Buttons look separate on the screen, so it feels right. The documentation says the system does not create a separate thread for each instance of a component. Every tap is handled by the same main thread.
- **Thinking drawing is a separate worker from your code.** Drawing feels like the system's job, not yours. The documentation puts drawing events on the same thread that handles your events, so slow code can delay the next draw.
- **Thinking a short job is always quick.** Its own work is quick, but it also waits for every job in front of it.

## Where this shows up in Android

You tap a button and the screen freezes for a moment: the heart does not turn red, the animation stutters. Your tap code was a long job at the front of the line, and the draw job waited behind it.

The documentation says that when the main thread is blocked, no events can be dispatched, including drawing events. It adds that if the thread is blocked for more than a few seconds, the user gets the "application not responding" dialog. Its two rules for Android's single-thread model are: do not block the UI thread, and do not touch the UI toolkit from outside it.

## Say it in an interview

The interview file for this branch asks about Looper and frame timing. The pages read for this lesson do not cover those, so these questions stay with what they do cover.

**Which thread draws the screen and runs your click code?**
The main thread, also called the UI thread. The documentation says it dispatches events to the user interface widgets, including drawing events. Your tap code runs on it too, which is why slow tap code delays the next draw.

**Does each Activity or button get its own thread?**
No. The documentation says all components in a process are created on the main thread and the system does not create a separate thread for each one. Two taps cannot run at the same moment on it. One waits for the other.

**What happens if you run a slow job on the main thread?**
While it is blocked, no events can be dispatched, including drawing. The screen freezes. If it stays blocked for more than a few seconds, the system shows the application-not-responding dialog.

**Why does Android keep screen work on one thread?**
The documentation gives the rule but not the reason. My own reasoning (general knowledge): if many workers changed the same screen at once, they could change the same widget in conflicting ways. One thread removes that risk. The cost is that slow work must go somewhere else.

## Recap

1. One thread, the main thread, draws the screen and runs your tap code.
2. In our picture, its jobs wait in one line, one job at a time.
3. A job finishes after everything in front of it, so a long job delays the next draw.

## You can now

- Predict the order in which three tasks in one line are served.
- Predict the finish times of three tasks that share one line.
- Fix a task list where a slow job delays the first picture.
