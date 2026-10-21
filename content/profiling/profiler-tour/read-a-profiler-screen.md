---
id: profiling/profiler-tour/read-a-profiler-screen
title: "What do the timeline and the charts on a profiler show me?"
status: shadow
difficulty: medium
order: 2
requires: [profiling/measure-first/measure-before-you-fix]
summary: A profiler shows several rows on one clock, and each row or chart answers a different question about where time or memory went.
video: ""
publishedOn:
updatedOn:
sims: [hospital-monitor]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live):
- https://developer.android.com/studio/profile/inspect-app-live : the "View Live Telemetry" task; rows Interactions (needs a debuggable app and API 26+), CPU usage (percentage of total available CPU time, plus total thread count), Threads (green running or runnable, yellow waiting on I/O, gray sleeping), Memory (stacked graph by memory category).
- https://developer.android.com/studio/profile/inspect-traces : Call Chart, Flame Chart, Top Down, Bottom Up, Events; axes and colours; "the horizontal axis doesn't represent a timeline" for the flame chart; Self, Children, Total; percentages "over the selected range".
- https://developer.android.com/studio/profile/chart-glossary/top-bottom-charts : Top Down expands to callees; Bottom Up expands to callers; Self, Children, Total.
- https://developer.android.com/studio/profile : debuggable apps "come with some performance costs"; a profileable app is based on the release variant; allocations and heap dumps need a debuggable app.
- https://developer.android.com/studio/profile/android-profiler : start a task from the Home tab; finished tasks are saved in Past Recordings.
- Deviations from author data (docs/practice/interview-profiling.md): the data lists Energy and Network timelines and "Sampled vs Instrumented" recordings. The pages above do not show them, so they are not claimed. The roadmap's "busiest 200 ms in a flame chart" is not used, because the flame chart's horizontal axis is not time.
- Task labels in the IDE change between releases (roadmap note); the lesson names one label from the docs and says so.
-->

## The question

You record your app for a few seconds, and a screen full of rows and charts appears. Some rows rise and fall. Some are blocks stacked like steps. One of them must say why the list stuttered. Which one, and what does each of the others tell you?

## Mental model

Think of the monitor beside a hospital bed.

- The patient is your app.
- The monitor is the profiler screen.
- Each line on the monitor is one row. It answers one question, such as how fast the heart beats or how deep the breathing is.
- The clock along the bottom is the shared time axis. All the lines use the same clock.
- A printout of one minute, zoomed in, is a chart. It shows more detail about a short stretch.

A nurse does not read one line alone. The nurse looks at the same moment on every line and asks what changed together.

**Where the analogy stops:** A monitor tells a nurse that the patient is unwell. A profiler does not tell you where the bug is. It shows where time and memory went, and you decide what that means. Also, a monitor has fixed lines. The rows you get depend on the app and on the kind of recording you start. And a hospital sensor does not change the patient, but a debug build does change how fast your app runs.

## See it move

<!-- SIM: hospital-monitor -->

**Try this:**
1. Look at the rows on one clock. Drag a time window over a busy moment and watch the call chart, flame chart and table change.
2. Move the window to a quiet moment and read the same charts again.
3. Answer the quiz that asks which row or chart answers which question.

**What you should notice:** Every row lines up on one clock, and each row or chart answers a different question.

## Build the logic

### Notice

A list in your app stutters about two seconds after you open the screen. You open the profiler and see four rows on one clock. A spike appears at the two-second mark on the CPU row. At the same mark, one **thread** in the thread row turns yellow. A thread is one line of work that your app runs, and an app can run several at once (general knowledge).

You have two clues at one moment. You still do not know which function was slow.

### Guess

What should you do first?

- A. Read the rows to find out when something happened and which part of the app was busy.
- B. Open the largest function in your code and start rewriting it.
- C. Wait and record again until the stutter goes away.

Idea B is the guess from the last lesson, and it can fix the wrong part. Idea C hopes the problem hides, and it will not. Idea A is the nurse reading every line at the same moment.

### Build

1. Start a recording and use the app until the problem happens.
2. Read the rows first. They tell you when something happened and how big it was.
3. Pick the stretch of time around the problem. The Android documentation says the percentages in the trace pane are calculated over the selected range.
4. Open a chart for that stretch. The chart tells you which calls ran.
5. Ask which call used the most time, and fix that one, as in the last lesson.

The rows say when. The charts say what.

### Name it

The profiler can show a live overview. The Android documentation says to start it with the **View Live Telemetry** task. (Task names can change between Android Studio versions.) It then draws these **rows**, one per question.

| Row | What it shows, from the Android documentation |
|---|---|
| Interactions | User interaction and app lifecycle events along a timeline. My own reasoning: a tap is an interaction, and a screen opening is a lifecycle event. It needs a debuggable app and a device on API level 26 or higher. |
| CPU usage | Your app's CPU usage as a percentage of the total available CPU time, and the number of threads it uses. |
| Threads | One line per thread, coloured by what the thread does. |
| Memory | A stacked graph of how much memory each memory category uses. |

In the thread row, green means the thread is active or ready to use the CPU. Yellow means it is active but waiting on an **I/O operation**, which is a read or write to something slow, such as the disk or the network. Gray means it is sleeping and using no CPU time.

When you record a trace of calls, you get **charts** for the stretch you pick. A **call** is one function being run. A **callee** is a function that was called, and a **caller** is the function that called it. A **call stack** is the chain of calls that led to the current one. The Android documentation describes the charts like this.

| Chart | How to read it |
|---|---|
| **Call Chart** | The horizontal axis is the period and timing of a call, and its callees are shown along the vertical axis. |
| **Flame Chart** | It merges identical call stacks. The horizontal axis is not a timeline. It shows the relative amount of time each call takes. |
| **Top Down** | Expand a call to see the calls it made (its callees). |
| **Bottom Up** | Expand a call to see who called it (its callers). It is useful for sorting calls by CPU time. |

Three numbers describe each call. **Self** is the time a call spent in its own code. **Children** is the time it spent running the calls it made. **Total** is Self plus Children.

One more thing is about the build. A **debuggable** app is built from the debug variant, and the documentation says it comes with some performance costs. A **profileable** app is built from the release variant, the one users get, and it still lets the profiler in. A **heap dump** is a snapshot of what the app holds in memory, and an **allocation recording** lists each time the app asked for memory (general knowledge). The documentation says both need a debuggable app.

This page has no program to run. The numbers on it are invented for teaching.

## Predict the output

```challenge
type: predict
run: false
id: which-row-shows-waiting
verified: not-run
code: |
  The list stutters. You want to know whether one of your app's
  threads was waiting on a disk or network operation at that moment.

  Rows on the profiler:
    1. CPU usage
    2. Memory
    3. Threads
  Which row shows it?
options:
  - id: a
    text: "CPU usage"
    why: "You may have thought a busy moment shows as CPU use. This row shows the total CPU use and the thread count, and it does not say what each thread is waiting for."
  - id: b
    text: "Threads"
    correct: true
    why: "Each thread has its own line on the clock. The Android documentation says yellow means the thread is waiting on an I/O operation such as disk or network."
  - id: c
    text: "Memory"
    why: "You may have looked for a pile-up. This row shows how much memory each category uses, and it does not say what a thread is waiting for."
output: |
  Threads
```

```challenge
type: predict
run: false
id: self-children-total
verified: not-run
code: |
  A call to loadList() is shown in the Top Down tab:

    Total     60 ms
    Children  45 ms
    Self      ?

  Total is Self plus Children.
  What is Self?
options:
  - id: a
    text: "105 ms"
    why: "You may have added the two numbers. Total is already the sum, so the other two must add up to 60."
  - id: b
    text: "45 ms"
    why: "You may have copied the Children number. Children is the time spent in the calls it made, and Self is what is left."
  - id: c
    text: "15 ms"
    correct: true
    why: "Total is Self plus Children, so Self is 60 minus 45. That is the time loadList() spent in its own code."
output: |
  15 ms
```

```challenge
type: predict
run: false
id: wide-bar-in-flame-chart
verified: not-run
code: |
  In a Flame Chart, the bar for parseJson() is twice as wide
  as the bar for drawRow().
  What does the width tell you?
options:
  - id: a
    text: "parseJson() takes a larger share of the time"
    correct: true
    why: "The Flame Chart merges identical call stacks, and width shows the relative amount of time each call takes. A wide bar is a bigger share."
  - id: b
    text: "parseJson() ran later in the recording"
    why: "You may have read the horizontal axis as a clock. The Android documentation says the flame chart's horizontal axis does not represent a timeline."
  - id: c
    text: "parseJson() ran on more threads"
    why: "You may have read width as a count of threads. Width shows relative time, and the thread row is where threads are shown."
output: |
  parseJson() takes a larger share of the time
```

## Tricks to remember

- Rows say when. Charts say what.
- The Flame Chart's width is a share of time, and it is not a clock.
- Self plus Children is Total.
- Yellow thread: active but waiting on I/O. Gray thread: sleeping.
- Check the build first, because a debug build changes the speed.
- Task: start the View Live Telemetry task on a debuggable run of any app, tap around, and match one spike on the CPU row with an event on the Interactions row.

## Common mistakes

- **Thinking the profiler tells me where the bug is.** It feels right because the screen is full of answers. It shows where time and memory went. You still decide which of those is the problem, and you measure again after a fix.
- **Reading the Flame Chart's horizontal axis as time.** The Call Chart does use it as time, so the two look alike. The Android documentation says the flame chart merges identical calls and the axis shows relative time. Use the Call Chart to see what ran when.
- **Reading a CPU spike as the cause on its own.** The spike stands out on the screen. It does not say which thread or which call. Look at the thread row and open a chart for the same stretch.
- **Trusting numbers from a debug build.** It is the build you run all day, so it feels normal. The documentation says a debuggable app has some performance costs. Record a release-based profileable build when you can.

## Where this shows up in Android

A screen stutters when you scroll. In the profiler, you record while scrolling, then look at the same moment on every row. The CPU row shows a spike. The thread row shows which thread was green and which was waiting in yellow.

Then you pick that stretch and open a chart. If one call has a large Self time in the Top Down tab, you have the function to look at. If the biggest Total belongs to a call that mostly waits on its children, the Children number points to the calls under it.

## Say it in an interview

**What do the rows in the Android Studio profiler's live view show?**
The Android documentation lists an Interactions row, a CPU usage row, a Threads row and a Memory row. CPU usage is a percentage of total available CPU time, and the thread row is coloured by what each thread does. I read them together at the same moment, because each row alone answers only one question.

**What is the difference between a Call Chart, a Flame Chart, Top Down and Bottom Up?**
The Call Chart uses the horizontal axis as time and shows callees below. The Flame Chart merges identical call stacks, and its width is relative time, not a clock. In Top Down, expanding a call shows its callees. In Bottom Up, expanding a call shows its callers, which helps to sort by CPU time.

**What do Self, Children and Total mean?**
Self is the time a call spent in its own code. Children is the time it spent in the calls it made. Total is Self plus Children. My own reasoning: a large Total with a small Self means I should look at the calls underneath.

**Why record on a profileable build and not a debuggable one?**
The documentation says a debuggable app comes with some performance costs, and a profileable app is based on the release variant. So the numbers from a profileable build are closer to what users see. The trade-off is that heap dumps and Java or Kotlin allocation recordings need a debuggable app.

## Recap

1. A profiler shows several rows on one clock, and each row answers its own question.
2. Charts for a chosen stretch show which calls ran, and the Flame Chart's width is relative time.
3. Self plus Children is Total, and a profiler shows where the time went, not where the bug is.

## You can now

- Pick the row or chart that answers a stated question about a recording.
- Work out Self from a Total and a Children number.
- Explain why the Flame Chart's horizontal axis is not a clock.
