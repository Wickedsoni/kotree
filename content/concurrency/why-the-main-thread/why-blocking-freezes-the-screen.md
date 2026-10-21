---
id: concurrency/why-the-main-thread/why-blocking-freezes-the-screen
title: "Why does one slow job freeze the whole screen?"
status: live
difficulty: medium
order: 2
requires: [concurrency/why-the-main-thread/what-the-main-thread-does]
summary: "One thread draws your screen and runs your tap code, so a slow tap job stops the drawing until it ends."
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [blocked-main-thread-timeline]
playground: true
fruit: false
upnext: false
---


## The question

You tap Save in a notes app. The button stays pressed, the spinner stops turning, and your next taps do nothing. A few seconds later a box asks whether to close the app. The code is correct, and it only reads one big file. Why does one slow job stop the whole screen, even the spinner?

## Mental model

Think again of the small shop with one cashier.

- The cashier is the one worker that serves every job.
- The customers are the jobs. A tap customer wants the Save code to run. A draw customer wants the window display tidied.
- The line is where the customers wait their turn.
- The window display is the screen. If the cashier does not tidy it, it stays as it was.
- The stockroom is where the big file is kept.

A tick is a tiny slice of time on the shop clock. Every 16 ticks, a new draw customer joins the line. Now the cashier takes the tap customer, who asks for a count of the stockroom. The cashier starts counting. The line stands still, and customers who arrive keep waiting.

**Where the analogy stops:** A shopper can see that the cashier has gone, but on a phone the picture just stops. A shop can also hire a second cashier, as an app can start other workers. Here I call the first cashier's line the main line, and any second cashier's line a helper line.

## See it move

```sim
id: blocked-main-thread-timeline
template: lanes
title: "A click job that holds the counter"
intro: "One counter serves every job. A draw job joins the line every 16 ticks. The picture shows picture 0 and the timeline bar is empty."
lanes:
  - { id: "line", label: "Line of draw jobs (the front job is at the counter)", kind: "queue", slots: 6, index0: false }
  - { id: "view", label: "Window display and timeline bar", kind: "row", slots: 2, index0: false }
items:
  - { id: "picture", label: "Screen: picture 0", at: "view.0" }
  - { id: "bar", label: "Timeline: nothing yet", at: "view.1" }
vars: { drawn: 0, waiting: 0, busy: 0 }
show:
  - { var: "drawn", label: "Pictures drawn" }
  - { var: "waiting", label: "Draw jobs waiting" }
  - { var: "busy", label: "Counter busy (ticks)" }
actions:
  - id: tap-short
    label: "Tap with a short click job (10 ticks)"
    cases:
      - unless: { at: { d1: "line.0" } }
        do:
          - add: { drawn: 1 }
          - set: { busy: 10, waiting: 0 }
          - label: { picture: "Screen: picture {drawn}", bar: "Timeline: 10 ticks (smooth)" }
          - mark: { picture: "plain", bar: "good" }
          - say: "The click job takes 10 ticks, less than the 16 ticks between draw jobs. A draw job is served on time and the picture changes to picture {drawn}."
    refuse: "Draw jobs are still stuck in the line. Press \"Let the long job end\" first."
  - id: tap-long
    label: "Tap with a long click job (800 ticks)"
    cases:
      - unless: { at: { d1: "line.0" } }
        do:
          - spawn: { id: "d1", label: "Draw job", at: "line.0" }
          - spawn: { id: "d2", label: "Draw job", at: "line.1" }
          - spawn: { id: "d3", label: "Draw job", at: "line.2" }
          - spawn: { id: "d4", label: "Draw job", at: "line.3" }
          - set: { busy: 800, waiting: 50 }
          - label: { picture: "Screen: picture {drawn} (frozen)", bar: "Timeline: 800 ticks (frozen frames)" }
          - mark: { picture: "changed", bar: "picked" }
          - say: "The click job holds the counter for 800 ticks. In that time 50 draw jobs join the line and none is served, so the picture stays at picture {drawn}."
    refuse: "Draw jobs are still stuck in the line. Press \"Let the long job end\" first."
  - id: tap-huge
    label: "Tap with a huge click job (6000 ticks)"
    cases:
      - unless: { at: { d1: "line.0" } }
        do:
          - spawn: { id: "d1", label: "Draw job", at: "line.0" }
          - spawn: { id: "d2", label: "Draw job", at: "line.1" }
          - spawn: { id: "d3", label: "Draw job", at: "line.2" }
          - spawn: { id: "d4", label: "Draw job", at: "line.3" }
          - set: { busy: 6000, waiting: 375 }
          - label: { picture: "Screen: picture {drawn} (frozen)", bar: "Timeline: 6000 ticks, past the 5000 mark" }
          - mark: { picture: "changed", bar: "bad" }
          - say: "The click job holds the counter for 6000 ticks, past the five-second mark of 5000 ticks. 375 draw jobs wait and the timeline bar turns red. Android may now offer to close the app."
    refuse: "Draw jobs are still stuck in the line. Press \"Let the long job end\" first."
  - id: finish
    label: "Let the long job end"
    cases:
      - when: { at: { d1: "line.0" } }
        do:
          - remove: "d1"
          - remove: "d2"
          - remove: "d3"
          - remove: "d4"
          - set: { waiting: 0 }
          - add: { drawn: 1 }
          - label: { picture: "Screen: picture {drawn}" }
          - mark: { picture: "plain", bar: "plain" }
          - say: "The long job ends and the cashier goes on with the line. The picture only changes once, to picture {drawn}, because the chances to redraw have already passed."
    refuse: "No draw jobs are waiting, so there is nothing to finish."
script: ["tap-short", "tap-long", "finish", "tap-huge"]
try:
  - text: "Press \"Tap with a short click job\". A draw job is served on time and the picture changes."
    done: ["tap-short"]
  - text: "Press \"Tap with a long click job (800 ticks)\". Watch the draw jobs pile up and the picture stop changing."
    done: ["tap-long"]
  - text: "Press \"Let the long job end\". Then press \"Tap with a huge click job (6000 ticks)\" and watch the timeline bar pass the five-second mark and turn red."
    done: ["finish", "tap-huge"]
```

**What you should notice:** While the long job holds the counter, no draw job is served and the picture stays the same, and past five seconds the bar turns red.

## Build the logic

### Notice

A shop has one cashier. Every 16 ticks, a draw customer joins the line. A tap customer arrives and needs 800 ticks, because the stockroom is big.

### Guess

How many of those draw customers does the cashier serve before the tap customer is done? Here are three ideas.

- A. About 50, because each one is quick.
- B. None. The cashier is busy with one customer, so the draw customers wait in the line.
- C. About 25, because the cashier swaps between the two kinds of customer.

Idea A treats each job on its own. But one cashier serves one job at a time. Idea C pictures a cashier who can stop in the middle, and this one cannot. Idea B is the answer.

### Build

1. Every 16 ticks, a draw customer joins the line.
2. The cashier takes the first customer in the line and finishes that job before the next one.
3. For those 800 ticks nobody else is served.
4. In that time 800 divided by 16 draw customers join the line, which is 50. The window display is not tidied for any of them.
5. When the long job ends, the cashier goes on with the line. The 50 chances to update the picture have already passed.
6. If the wait gets long enough, the box that offers to close the app appears.

### Name it

The **main thread** is the one thread, or worker, that draws your screen and runs your tap code (the documentation also calls it the UI thread). A thread that cannot take its next job because it is busy with a long one is **blocked**. The Android documentation says that when the main thread is blocked, no events can be dispatched, including drawing events. An **event** is something that happens, such as a tap. A **frame** is one picture of the screen. The render documentation says an app must render a frame in under 16 ms to reach 60 frames per second (fps). It says 11 ms for 90 fps and 8 ms for 120 fps. If a frame takes longer, the system is forced to skip frames and the user sees stuttering, which the documentation calls **jank**.

The documentation puts the times in bands.

| Time a frame or event waits | What it is called |
|---|---|
| Under 16 ms | Smooth for 60 fps (my own label) |
| Between 16 ms and 700 ms | Slow frames |
| Between 700 ms and 5 s | Frozen frames |
| More than 5 s | **ANR**, short for Application Not Responding |

The ANR page says an ANR is triggered if the app has not responded to an input event, such as a screen touch, within 5 seconds. When the app is in the foreground, the system shows a dialog that gives the user a chance to force quit.

Here is the shop in plain Kotlin. It is a model, not real Android. One step is one millisecond. I use exactly 16 steps per draw job to keep the sums whole. The documentation says under 16 ms, so treat these as round numbers of my own.

```kotlin
// A model in plain Kotlin, not real Android. One step is one millisecond.
fun main() {
    val drawEvery = 16
    val clickJob = 800
    val missed = clickJob / drawEvery
    println("Draw jobs missed: " + missed)
}
```

This prints `Draw jobs missed: 50`. The `/` between two whole numbers drops any remainder.

## Predict the output

```challenge
type: predict
id: missed-draws-in-480
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android. One step is one millisecond.
  fun main() {
      val drawEvery = 16
      val clickJob = 480
      println("Draw jobs missed: " + clickJob / drawEvery)
  }
options:
  - id: a
    text: "Draw jobs missed: 1"
    why: "You may have counted the one tap. The cashier is stuck for 480 ticks, and a new draw customer joins every 16 of them."
  - id: b
    text: "Draw jobs missed: 480"
    why: "You may have counted one draw job for every tick. A draw job joins the line only once every 16 ticks."
  - id: c
    text: "Draw jobs missed: 30"
    correct: true
    why: "The cashier is busy for 480 ticks. A new draw customer joins the line every 16 ticks, and 480 divided by 16 is 30."
output: |
  Draw jobs missed: 30
```

```challenge
type: predict
id: name-the-wait-band
verified: kotlin-2.4.21
code: |
  fun main() {
      val ms = 900
      val label = when {
          ms < 16 -> "smooth"
          ms < 700 -> "slow"
          ms < 5000 -> "frozen"
          else -> "not responding"
      }
      println(label)
  }
options:
  - id: a
    text: "slow"
    why: "You may have thought anything under a second is slow. The bands stop being slow at 700 ms, and 900 is past that."
  - id: b
    text: "frozen"
    correct: true
    why: "The check goes down the bands in order. 900 is not under 16 and not under 700, but it is under 5000, so it lands in the frozen band."
  - id: c
    text: "not responding"
    why: "You may have felt that 900 ms is already too long. The not-responding band starts above 5 seconds, which is 5000 ms."
output: |
  frozen
```

## Try it

1. **Write.** Type the program from Name it into the editor.
2. **Run.** You should see one line: `Draw jobs missed: 50`.
3. **Break.** Change `800` to `8000` and change nothing else. Run it again. The program still runs, and it prints `Draw jobs missed: 500`. The number is a picture of a screen that stood still for eight seconds, longer than the five-second mark.
4. **Fix.** Change `8000` to `8` and run it. It prints `Draw jobs missed: 0`, because a job shorter than one draw interval is done before the next draw customer arrives.

## Fix the bug

```challenge
type: fix
id: fix-file-read-on-main-line
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android. One step is one millisecond.
  const val DRAW_EVERY = 16
  const val FILE_READ = 640

  fun main() {
      // Click handler: read the saved file, then update the label.
      val labelSteps = 2
      val mainLineBusy = FILE_READ + labelSteps
      val helperLineBusy = 0
      println("Main line busy: " + mainLineBusy)
      println("Helper line busy: " + helperLineBusy)
      println("Draw jobs missed: " + mainLineBusy / DRAW_EVERY)
  }
hints:
  - "Look at the line that adds FILE_READ to mainLineBusy. What do the draw jobs wait for?"
  - "A job on the helper line does not make the main line wait. Move the file read there."
expected: |
  Main line busy: 2
  Helper line busy: 640
  Draw jobs missed: 0
fix: |
  // A model in plain Kotlin, not real Android. One step is one millisecond.
  const val DRAW_EVERY = 16
  const val FILE_READ = 640

  fun main() {
      // Click handler: the file is read on the helper line.
      val labelSteps = 2
      val mainLineBusy = labelSteps
      val helperLineBusy = FILE_READ
      println("Main line busy: " + mainLineBusy)
      println("Helper line busy: " + helperLineBusy)
      println("Draw jobs missed: " + mainLineBusy / DRAW_EVERY)
  }
explanation: "The file read sat on the main line, so 640 steps of waiting piled up in front of every draw job. On the helper line it costs the main line nothing."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. Question 4 matters here: a click handler runs on every tap, so the original code blocks the main line again on every Save.

## Tricks to remember

- One cashier, one line. A long job in front stops everything behind it.
- Draw jobs arrive every 16 ticks. A job longer than that makes the picture skip.
- Task, in an Android project: add Thread.sleep(200) to a button's click handler, run it, then change it to 6000 ms and see what the screen does.
- 16 ms is the frame, 700 ms is frozen, and 5 s is the box to close the app.

## Common mistakes

- **Thinking freezing happens only on slow phones or with huge data.** A slow phone feels like the cause. The render documentation says a frame that takes more than 16 ms is slow, so even a short read can skip a draw. Check the job length, not the phone.
- **Thinking nothing bad happens before 5 seconds.** The five-second box is the number people remember, so it feels like the limit. It is only the point where the system gives up and offers to close the app. The stutter starts at 16 ms.
- **Thinking a frozen screen means the app crashed.** The screen stands still, so it feels broken. The ANR page says the dialog lets the user force quit, which is a different thing from a crash.

## Where this shows up in Android

You tap Save, the button stays pressed, the spinner stops, and your next taps do nothing. Your Save code was a long job at the front of the line, and every draw job waited behind it.

The processes-and-threads documentation says that if the UI thread is blocked for more than a few seconds, the user sees the "application not responding" dialog. It also lists two rules for the single-thread model: do not block the UI thread, and do not access the UI toolkit from outside it. My own reasoning: when a screen freezes right after one tap, look first at what that tap's code does before it returns.

## Say it in an interview

**What does the user see when a slow job runs on the main thread?**
The screen stops updating. The documentation says that while the main thread is blocked, no events can be dispatched, including drawing events. If it stays blocked for more than a few seconds, the user gets the "application not responding" dialog.

**What is an ANR, and when does the system raise one for input?**
An ANR is an Application Not Responding error. The ANR page says one is triggered when the app has not responded to an input event, such as a screen touch, within 5 seconds. It also lists other triggers, such as a broadcast receiver that runs too long (5 seconds when the app has an activity in the foreground).

**Why can a 30 ms read cause stutter when it is far below 5 seconds?**
The render documentation says an app must render a frame in under 16 ms for 60 fps. If a job overruns that window, the system part called the Choreographer drops the frame, so the user sees jank. The 5 seconds is only the point where the system shows the dialog, and the trouble starts much earlier.

**What goes wrong if the main thread waits for a lock that a background thread holds?**
A lock is a sign that says one worker at a time (general knowledge). If the main thread waits for a lock while the other worker is doing slow reading, the main thread is blocked for as long as that reading takes. My own reasoning: the freeze looks the same as a slow job on the main thread, and it can reach an ANR.

## Recap

1. The main thread draws the screen and runs your tap code, one job at a time.
2. A long job blocks the line, so draw jobs wait and the picture stands still.
3. Frames need under 16 ms, and five seconds without answering a tap raises an ANR.

## You can now

- Predict how many draw jobs a long job misses in the model.
- Place a wait of a given length in the smooth, slow, frozen or ANR band.
- Fix a model where a file read blocks the main line, so no draw job is missed.
