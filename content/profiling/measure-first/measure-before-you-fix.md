---
id: profiling/measure-first/measure-before-you-fix
title: Why must I measure before I try to make my app faster?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Guessing which code is slow often picks the wrong code. A measurement shows where the time goes, so your fix goes to the right place.
video: ""
publishedOn:
updatedOn:
sims: [measure-bars]
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: (1) sim id measure-bars has no spec in docs/sims yet (physics-designer: five suspect bars, learner guesses the slowest, measured bars reveal a different winner, before/after stopwatch shows a fix that changed nothing). (2) The numbers in the lesson (200, 60, 40 ms and so on) are made up for teaching, invented on 21 Oct 2026, not measured from a real app; the lesson says so. (3) Facts checked 21 Oct 2026 at developer.android.com/topic/performance/vitals/render: "Run a release—or at least non-debuggable—version of your app. The ART runtime disables several important optimizations to support debugging features"; method traces have heavy overhead and can show false jank; Profile GPU Rendering and Systrace exist for finding jank; frame benchmark is 16 ms. The page has no sentence that says "measure before you optimize"; that rule is this lesson's teaching, not a quote. (4) The roadmap F item (a slow plain-Kotlin program, count steps) was NOT written: playground false, so the leaf has no runnable code and no Fix the bug. Assumes foundation/debugging-mindset/find-a-bug-without-guessing (not drafted yet). -->
## The question

A user says your app is slow. You open the code and one function looks heavy: it has a loop and a lot of lines. You spend a day making it faster. The user opens the app again and says it still feels slow.

What should you have done first? And how do you find the real slow part before you spend the day?

## Mental model

Think of a doctor with a thermometer.

- The patient is your app.
- The complaint, "I feel unwell", is "the app is slow".
- The thermometer is a measurement tool, which tells you the real number.
- The medicine is the fix you write.

A good doctor does not look at the patient and prescribe the strongest medicine. The doctor takes a temperature first. Then the medicine goes to the real problem. Guessing the medicine can make things worse.

**Where the analogy stops:** A temperature is one number and you read it once. A slow app has many parts that each take some time, so a measurement is a list of numbers, one for each part. Also, you measure again after the fix. A doctor does that too, but an app is far cheaper to measure many times.

## See it move

<!-- SIM: measure-bars -->

**Try this:**
1. Look at the five suspect functions. Pick the one you think is the slowest.
2. Press "Measure". Watch the bars grow to their real size.
3. Fix the function you picked, then press "Measure again" and compare the stopwatch before and after.

**What you should notice:** The slowest bar is often not the one you guessed, and a fix on the wrong function leaves the stopwatch where it was.

## Build the logic

### Notice

A screen takes 300 milliseconds to open. The author of the app looks at the code for the layout of the screen. It looks long and busy. So the author works on that function and makes it twice as fast.

The screen still takes almost 300 milliseconds. The author is surprised.

### Guess

What was the author missing?

- A. A faster phone.
- B. A list that says how many milliseconds each part took.
- C. A fix that speeds up every part of the screen at once.

Idea A hides the problem instead of finding it. Idea C does not exist for a real app. Idea B is the thermometer: without it, the author fixed the part that looked big, not the part that was slow.

### Build

1. Say what you want to know, for example "how long does the screen take to open".
2. Measure the whole thing first, so you have one starting number.
3. Measure the parts, so you can see where the time goes.
4. Choose the biggest part and fix only that one.
5. Measure the whole thing again and compare with the starting number.

If the number did not move, the fix went to the wrong place, or the fix did nothing. Either way, you now know.

### Name it

- **Measuring** means recording a real number, such as milliseconds, with a tool, instead of guessing.
- A **profiler** is a tool that records where a program spends its time.
- A **bottleneck** is the one part that takes most of the time. Making any other part faster barely helps.
- A **baseline** is the number you measured before the fix. You compare the new number against it.

| Doctor | Official name | App example |
|---|---|---|
| The thermometer | Profiler | A tool that records where the time goes |
| The first temperature | Baseline | The screen takes 300 ms to open |
| The part with the fever | Bottleneck | Waiting for the network takes 200 ms |
| The medicine | The fix | Your change to the code |

One rule goes with this: measure on a build that looks like what the user runs. The Android documentation says to run a release, or at least non-debuggable, version of your app. The reason it gives is that the runtime turns off several important optimizations to support debugging. A debug build can look slower than the real app, and its slow parts may differ.

There is no code to run on this page. The numbers below are made up for teaching, not measured from a real app.

## Predict the output

```challenge
type: predict
run: false
id: pick-the-bottleneck
verified: not-run
code: |
  A screen takes 300 ms to open. A profiler recorded three parts:
    Parse the data:      60 ms
    Wait for the network: 200 ms
    Lay out the screen:   40 ms
  Which part is the bottleneck?
options:
  - id: a
    text: "Lay out the screen, because its code is the longest"
    why: "You may have judged by how much code there is. The thermometer gives milliseconds, not lines. This part has the shortest time, 40 ms."
  - id: b
    text: "Wait for the network"
    correct: true
    why: "The fever is where the number is highest. 200 of the 300 ms go there, so the medicine goes there."
  - id: c
    text: "Parse the data, because parsing sounds heavy"
    why: "You may have guessed from the name. A guess is what the measurement replaces. Parsing took 60 ms, less than a third of the wait."
output: "Wait for the network"
```

```challenge
type: predict
run: false
id: fix-the-wrong-part
verified: not-run
code: |
  The same screen takes 300 ms to open.
  Parse the data:       60 ms
  Wait for the network: 200 ms
  Lay out the screen:   40 ms
  You make "lay out the screen" twice as fast.
  How long does the screen take to open now?
options:
  - id: a
    text: "150 ms"
    why: "You may have thought that making one part twice as fast halves the whole. Only that part gets faster. The rest of the 300 ms is untouched."
  - id: b
    text: "280 ms"
    correct: true
    why: "Lay out used to take 40 ms and now takes 20 ms, so you saved 20 ms. 300 minus 20 is 280. The fever part, the network wait, did not change."
  - id: c
    text: "300 ms"
    why: "You may have expected a fix on the wrong part to change nothing. It saves a little, 20 ms, but the user would not notice. A baseline lets you see that."
output: "280 ms"
```

## Tricks to remember

- Thermometer first, medicine second.
- Guess, then measure, then compare. The measurement settles it.
- Fix the biggest bar only. The small bars cannot save you.
- Measure on a build like the user's, not on a debug build.

## Common mistakes

- **Thinking you can see the slow code by reading it.** Long, busy code looks slow, so it feels like the culprit. A function with many lines can finish in one millisecond while one short line waits on the network. Measure first and let the number choose.
- **Measuring only once.** One number feels like enough proof that the fix worked. Run the same measurement again on the same kind of build, and compare it with the baseline.
- **Measuring on a debug build.** It is the build you already have open. The Android documentation says the runtime turns off several important optimizations to support debugging, so the numbers may not match what users see. Use a release or non-debuggable build.

## Where this shows up in Android

The symptom is a screen that feels slow or choppy, which Android calls jank. The Android documentation uses a benchmark of 16 milliseconds per frame, and tells you to inspect which frames take longer than that and look for what is wrong. It names tools for finding where the time goes, such as Profile GPU Rendering, Systrace and Perfetto. It also warns that method traces add a lot of overhead and can show false jank.

So when someone says "the app is slow", the first move is to pick the tool, take the baseline number, and find the biggest bar. This lesson teaches why that comes first. Later lessons in the profiling branch show the tools.

## Recap

1. Measure first, so your fix goes to the real slow part.
2. Take a baseline, fix the biggest part only, and measure again.
3. Measure on a release-like build, not a debug build.

## You can now

- Explain why you measure before you fix, using the doctor and the thermometer.
- Pick the bottleneck from a list of measured parts.
- Predict how much a fix on the wrong part changes the total time.
