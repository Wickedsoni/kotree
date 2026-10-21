---
id: profiling/measure-first/measure-before-you-fix
title: Why must I measure before I try to make my app faster?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Guessing which code is slow often picks the wrong code. A measurement shows where the time goes, so your fix lands in the right place.
video: ""
publishedOn:
updatedOn:
sims: [measure-bars]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/topic/performance/vitals/render ("Run a release or at least non-debuggable version of your app. The ART runtime disables several important optimizations to support debugging features"; frames must render in under 16 ms for 60 frames per second; method traces "produce false-positive janks due to heavy overhead"; Profile GPU Rendering, Systrace and Perfetto named for finding jank). The page has no sentence that says "measure before you optimize", so that rule is this lesson's teaching, not a quote. The numbers in the lesson (300, 200, 60, 40 ms) are invented for teaching and the lesson says so. General knowledge: premature optimization. Interview data: docs/practice/interview-profiling.md (its link to developer.android.com/topic/performance/profiling returns 404, so it is not used). Sim measure-bars is new, see docs/sims/needed-ids.md. Predict items are traced by hand. -->

## The question

A user says your app is slow. You open the code, and one function looks heavy, with many lines. You spend a day making it faster. The user tries again and says it still feels slow. What should you have done first?

## Mental model

Think of a doctor with a thermometer.

- The patient is your app.
- The complaint, "I feel unwell", is "the app is slow".
- The thermometer is a measuring tool. It gives you a real number instead of a guess.
- The fever is the one part of the body that is far too hot. In an app, it is the part that takes most of the time.
- The medicine is the fix you write.

A good doctor does not look at the patient and prescribe the strongest medicine. The doctor takes a temperature first, finds the fever, and gives the medicine for that. Guessing the medicine can make things worse.

**Where the analogy stops:** A temperature is one number that you read once. A slow app has many parts, and each part has its own time, so a measurement is a list of numbers. Also, a doctor rarely measures the same patient a hundred times. With an app you can measure before and after as often as you like.

## See it move

<!-- SIM: measure-bars -->

**Try this:**
1. Look at the five suspect functions. Pick the one you think is the slowest.
2. Press "Measure". Watch the bars grow to their real size.
3. Fix the function you picked, then press "Measure again" and compare the stopwatch before and after.

**What you should notice:** The slowest bar is often not the one you guessed, and a fix on the wrong function leaves the stopwatch where it was.

## Build the logic

### Notice

A screen takes 300 milliseconds to open. A millisecond, written ms, is a thousandth of a second. The developer reads the code that lays out the screen. It looks long and busy, so the developer makes that function twice as fast.

The screen still takes almost 300 ms to open. The developer is surprised.

### Guess

What was the developer missing?

- A. A list that says how many milliseconds each part took.
- B. A faster phone.
- C. A fix that speeds up every part of the screen at once.

Idea B hides the problem instead of finding it. Idea C does not exist for a real app. Idea A is the thermometer. Without it, the developer fixed the part that looked big, not the part that was slow.

### Build

1. Say what you want to know, for example "how long does the screen take to open".
2. Measure the whole thing first, so you have one starting number.
3. Measure the parts, so you can see where the time goes.
4. Choose the biggest part, the fever, and fix only that one.
5. Measure the whole thing again and compare it with the starting number.

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
| The fever | Bottleneck | Waiting for the network takes 200 ms |
| The medicine | The fix | Your change to the code |

One more rule goes with this: measure on a build that looks like what the user runs. A **build** is the version of the app that the tools produce. The Android documentation says to run a release, or at least non-debuggable, version of your app. Its reason is that the runtime, which is the part of Android that runs your app, disables several important optimizations to support debugging. A **debug build** is made for testing and carries extra support for debugging. A **release build** is the one users get. A non-debuggable build is one with that debugging support switched off. A debug build can look slower than the real app.

There is no code to run on this page. The numbers are made up for teaching and were not measured from a real app.

## Predict the output

```challenge
type: predict
run: false
id: pick-the-bottleneck
verified: not-run
code: |
  A screen takes 300 ms to open. A profiler recorded three parts:
    Parse the data:       60 ms
    Wait for the network: 200 ms
    Lay out the screen:   40 ms
  Which part is the bottleneck?
options:
  - id: a
    text: "Lay out the screen, because its code is the longest"
    why: "You may have judged by how much code there is. The thermometer gives milliseconds, not lines. This part has the shortest time, 40 ms."
  - id: b
    text: "Parse the data, because parsing sounds heavy"
    why: "You may have guessed from the name. A guess is what the measurement replaces. Parsing took 60 ms, less than a third of the wait."
  - id: c
    text: "Wait for the network"
    correct: true
    why: "The fever is where the number is highest. 200 of the 300 ms go there, so the medicine goes there."
output: |
  Wait for the network
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
    text: "280 ms"
    correct: true
    why: "Lay out used to take 40 ms and now takes 20 ms, so you saved 20 ms. 300 minus 20 is 280. The fever, the network wait, did not change."
  - id: b
    text: "150 ms"
    why: "You may have thought that making one part twice as fast halves the whole. Only that part gets faster. The rest of the 300 ms is untouched."
  - id: c
    text: "300 ms"
    why: "You may have expected a fix on the wrong part to change nothing. It saves a little, 20 ms, but the user would not notice. A baseline lets you see that."
output: |
  280 ms
```

## Tricks to remember

- Thermometer first, medicine second.
- Guess, measure, compare. The measurement settles it.
- Fix the biggest bar only. The small bars cannot save you.
- Measure on a build like the user's, not on a debug build.

## Common mistakes

- **Thinking you can see the slow code by reading it.** Long, busy code looks slow, so it feels like the culprit. A function with many lines can finish in one millisecond while one short line waits on the network. Measure first and let the number choose.
- **Measuring only once.** One number feels like enough proof that the fix worked. Run the same measurement again on the same kind of build and compare it with the baseline.
- **Measuring on a debug build.** It is the build you already have open. The documentation says the runtime disables several optimizations to support debugging, so the numbers may not match what users see. Use a release or non-debuggable build.

## Where this shows up in Android

The symptom is a screen that feels slow or choppy. Android calls this jank. A **frame** is one picture on the screen, and the Android documentation says your app must render frames in under 16 ms to reach 60 frames per second. So jank means some frames took longer than 16 ms.

The same page names tools for finding where the time goes: Profile GPU Rendering, Systrace and Perfetto. These are recording tools: they note what the phone did and when. It also warns that method traces can show false jank because of their heavy overhead. When someone says "the app is slow", the first move is to pick a tool, take the baseline and find the biggest bar. That is the order this lesson teaches.

## Say it in an interview

**Why must you measure before you try to make an app faster?**
A guess often picks the wrong code, and then the user sees no change. A measurement shows which part takes the time, so the fix goes to that part. It also gives a baseline, a number to compare with after the fix.

**What is premature optimization?**
This answer is general knowledge. It means making code faster before you know it is the slow part. You spend time and may make the code harder to read, and the user feels nothing. Measuring first is the cure.

**How does a profiler stop you from chasing the wrong problem?**
It records where the program spends its time, so you can read the biggest part off a list instead of guessing from the code. The trade-off is that a measurement is only useful if the build looks like the user's. The Android documentation says to use a release or non-debuggable build, because the runtime disables optimizations for debugging.

**What goes wrong if you change complex code based on a hunch?**
My own reasoning: you spend a day on a part that was not slow, and you may add new bugs while changing it. The screen still feels slow, and the baseline would have shown that. A good answer also says you measure again after the fix.

## Recap

1. Measure first, so your fix goes to the part that is really slow.
2. Take a baseline, fix only the biggest part, then measure again.
3. Measure on a release-like build, not on a debug build.

## You can now

- Pick the bottleneck from a list of measured parts.
- Predict how much a fix on the wrong part changes the total time.
- Explain why you measure before you fix, using the doctor and the thermometer.
