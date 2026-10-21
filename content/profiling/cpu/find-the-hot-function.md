---
id: profiling/cpu/find-the-hot-function
title: "How do I find the function that uses the most CPU time?"
status: live
difficulty: medium
order: 3
requires: [profiling/profiler-tour/read-a-profiler-screen]
summary: The hot function spends the most time in its own code, and a profiler sorts a recording so it comes first.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [shop-bill-sorted]
playground: true
fruit: false
upnext: false
---

## The question

A screen with a long list stutters when you scroll. You open the code and find one big function with 60 lines. Is that the slow one? Or is a tiny function somewhere doing the damage? How can you tell without guessing?

## Mental model

Think of the bill from a shop, sorted so the biggest amount comes first.

- The shop is your app.
- The bill is one recording of your app while it is slow.
- Each line on the bill is one function.
- The amount on a line is the time that function spent in its own code.
- A line's amount is the price of one call times the number of calls. A cheap item bought 1000 times can cost more than one expensive item bought once.
- The top line of the sorted bill shows where the time went.

You do not guess which item cost the most. You sort the bill and read the top line.

**Where the analogy stops:** A shop bill is exact. A recording is an estimate, or it changes the thing it measures, as you will see. Also, a price in a shop stays the same, but the time of a function changes from one recording to the next.

## See it move

```sim
id: shop-bill-sorted
template: chart
title: "A shop bill for four functions"
intro: "The bill has four lines, one for each function, and every line shows 0 because nothing is recorded yet. The numbers are invented for teaching, not measured from a real app."
axes: { x: Function, y: "Milliseconds of its own work" }
bars:
  - { id: names, label: loadNames, value: { var: a } }
  - { id: build, label: buildReceipt, value: { var: b } }
  - { id: tax, label: lookupTax, value: { var: c } }
  - { id: draw, label: drawRow, value: { var: d } }
vars: { a: 0, b: 0, c: 0, d: 0, exact: "off", fixed: "no" }
toggles:
  - { id: exact, label: "Record every call", var: exact }
actions:
  - id: record
    label: "Record the app"
    cases:
      - when: { var: { exact: "off", fixed: "no" } }
        do:
          - set: { a: 8, b: 20, c: 62, d: 10 }
          - say: "The recording looks at what is running many times a second. lookupTax has the tallest bar, 62 of 100 milliseconds, although it has only three lines of code."
      - when: { var: { exact: "on", fixed: "no" } }
        do:
          - set: { a: 11, b: 27, c: 150, d: 15 }
          - say: "Every call is written down, so nothing is missed. The recording costs time too, so every bar is taller than before, and lookupTax grows most because it is called 1000 times."
      - when: { var: { exact: "off", fixed: "yes" } }
        do:
          - set: { a: 8, b: 20, c: 1, d: 10 }
          - say: "lookupTax is now a sliver, and buildReceipt has the tallest bar."
      - when: { var: { exact: "on", fixed: "yes" } }
        do:
          - set: { a: 11, b: 27, c: 2, d: 15 }
          - say: "With every call written down, lookupTax is still a sliver, and buildReceipt has the tallest bar."
  - id: ask-callers
    label: "Ask who called the tallest bar"
    cases:
      - when: { var: { fixed: "no" } }
        do:
          - say: "In the Bottom Up list, opening lookupTax shows its caller. It is buildReceipt, which calls lookupTax once for every line of the receipt, 1000 times."
      - when: { var: { fixed: "yes" } }
        do:
          - say: "lookupTax is now called once, so the tallest bar belongs to buildReceipt."
  - id: remember
    label: "Keep the answer after the first call"
    cases:
      - when: { var: { exact: "off" } }
        do:
          - set: { a: 8, b: 20, c: 1, d: 10, fixed: "yes" }
          - say: "The answer is looked up once and kept, so lookupTax drops to 1 millisecond and buildReceipt is now the tallest bar."
      - when: { var: { exact: "on" } }
        do:
          - set: { a: 11, b: 27, c: 2, d: 15, fixed: "yes" }
          - say: "The answer is looked up once and kept, so lookupTax drops to 2 milliseconds and buildReceipt is now the tallest bar."
script: [record, ask-callers, remember]
try:
  - text: 'Press "Record the app" and find the tallest bar on the bill.'
    done: [record]
  - text: 'Press "Ask who called the tallest bar".'
    done: [ask-callers]
  - text: 'Switch on "Record every call" and watch the bars grow. Then press "Record the app" again and read what it says.'
    done: ["exact=on", record]
  - text: 'Press "Keep the answer after the first call" and read which bar is the tallest now.'
    done: [remember]
    optional: true
```

**What you should notice:** The function with only three lines of code has the tallest bar because it is called 1000 times, and recording every call makes every bar taller.

## Build the logic

### Notice

A list shows 1000 receipt lines and stutters. One function, `buildReceipt`, is long and looks busy. Another function, `lookupTax`, has three lines and looks harmless. Each receipt line calls `lookupTax` once.

You have a feeling about which one is slow. You have no number.

### Guess

How do you find the function that costs the most time?

- A. Pick the function with the most lines of code.
- B. Read all the code again and decide which part looks heavy.
- C. Record the app while it is slow, sort the recording by time of own work, and read the top line.

Idea A is the common guess, and it often points at the wrong function. Idea B is the same guess with more reading. Idea C is the sorted bill.

### Build

1. Make a short recording while the problem happens. A few seconds is enough.
2. Sort the recording by the time each function spent in its own code, biggest first.
3. Read the top line. That is the function to look at first.
4. Ask who called it, and how often. A cheap call made 1000 times adds up.
5. Fix it. Either call it less often, or keep the answer after the first call, if the answer is always the same.
6. Record again. The bill must change, or the fix did not help.

### Name it

A **profiler** is a tool that records where your app spends its time. The Android Studio profiler can sample the callstack, or record the Java and Kotlin methods that run. The **hot function** is the function at the top of the bill, the one that uses the most time. (Hot function is a common name, from general knowledge, and not an Android Studio label.)

To **sample** means to look at what is running again and again, many times a second, and count what it finds (general knowledge). The Android documentation says to select the **Find CPU Hotspots (Callstack Sample)** task on a device with Android 8.0 (API level 26) or higher. Sampling costs little, and a call that is shorter than the gap between two looks can be missed (general knowledge).

**Method recording** is the other way. The documentation says the profiler uses runtime instrumentation to put timestamps at the entry and the exit of each method call. Nothing is missed. The documentation recommends recordings of five seconds or less, and says the timing might deviate from production because of the overhead of the recording itself. (Task names change between Android Studio versions.)

Three numbers describe each call, as in the last lesson. **Self** is the time the call spent in its own code. **Children** is the time it spent in the calls it made. **Total** is Self plus Children. To find the hot function, read Self. A function high on Total may only be the caller of the hot one.

The documentation says to use the **flame chart** or the **Top Down** chart to identify the methods that take the most time. The flame chart merges identical call stacks, so its horizontal axis is relative time and not a timeline. The **Bottom Up** tab shows who called a function, and the documentation says it is useful for sorting by CPU time.

Here is the idea in plain Kotlin. It counts calls instead of time, because time changes on every run and a count does not.

```kotlin
var calls = 0

fun lookupTax(): Int {
    calls++
    return 8
}

fun main() {
    var total = 0
    repeat(1000) { total += lookupTax() }
    println("total = $total, calls = $calls")
}
```

It prints `total = 8000, calls = 1000`. Each call is tiny, but there are 1000 of them, and the answer is the same every time.

## Predict the output

```challenge
type: predict
id: five-calls-five-answers
verified: kotlin-2.4.21
code: |
  var calls = 0

  fun lookupTax(): Int {
      calls++
      return 8
  }

  fun main() {
      for (i in 1..5) {
          println(lookupTax() * i)
      }
      println("calls = $calls")
  }
options:
  - id: a
    text: "8, 16, 24, 32, 40, then calls = 1"
    why: "You may have thought a function that always gives 8 is only run once. Every line that says lookupTax() runs it again, so each pass of the loop adds one call."
  - id: b
    text: "8, 16, 24, 32, 40, then calls = 5"
    correct: true
    why: "The loop reads the bill five times, so lookupTax is called five times. The answers are 8 times 1, 2, 3, 4 and 5, and the count is 5."
  - id: c
    text: "8, 8, 8, 8, 8, then calls = 5"
    why: "You may have forgotten the times i. The count of 5 is right, but each printed number is 8 times the loop number."
output: |
  8
  16
  24
  32
  40
  calls = 5
```

```challenge
type: predict
id: ask-once-before-the-loop
verified: kotlin-2.4.21
code: |
  var calls = 0

  fun lookupTax(): Int {
      calls++
      return 8
  }

  fun main() {
      val tax = lookupTax()
      var total = 0
      for (i in 1..1000) {
          total += tax
      }
      println("total = $total, calls = $calls")
  }
options:
  - id: a
    text: "total = 8000, calls = 1000"
    why: "You may have counted the 1000 passes of the loop. Only the line val tax = lookupTax() calls the function, and it runs once."
  - id: b
    text: "total = 8, calls = 1"
    why: "You may have thought the answer is added only once. The loop adds the kept answer, 8, on all 1000 passes, so the total is 8000."
  - id: c
    text: "total = 8000, calls = 1"
    correct: true
    why: "The answer is looked up once and kept. The bill has one cheap line, and the loop only adds the kept number 1000 times."
output: |
  total = 8000, calls = 1
```

## Try it

1. **Write.** Type the program from Name it into the editor: the `calls` counter, `lookupTax` and `main` with the `repeat`.
2. **Run.** You should see one line, `total = 8000, calls = 1000`.
3. **Break.** Change `1000` to `2000` and change nothing else. Run it again. Nothing crashes, but the line now says `total = 16000, calls = 2000`. The number of calls doubled, and a profiler would show a wider bar for it.
4. **Fix.** Add the line `val tax = lookupTax()` before the `repeat`, and change the body of the `repeat` to `total += tax`. Run it. You should see `total = 16000, calls = 1`. The answer is the same and the calls dropped from 2000 to 1.

## Fix the bug

```challenge
type: fix
id: fix-tax-lookup-in-loop
verified: kotlin-2.4.21
code: |
  var taxLookups = 0

  fun taxRateFor(country: String): Int {
      taxLookups++
      return if (country == "IN") 18 else 10
  }

  fun priceWithTax(price: Int, country: String): Int {
      return price + price * taxRateFor(country) / 100
  }

  fun main() {
      val prices = listOf(100, 200, 300, 400, 500)
      var total = 0
      for (price in prices) {
          total += priceWithTax(price, "IN")
      }
      println("total = $total")
      println("tax lookups = $taxLookups")
  }
hints:
  - "Run it and read the last line. How many prices are there, and how many tax lookups did the program make?"
  - "The tax rate for one country is the same for every price. Look it up once, before the loop."
expected: |
  total = 1770
  tax lookups = 1
fix: |
  var taxLookups = 0

  fun taxRateFor(country: String): Int {
      taxLookups++
      return if (country == "IN") 18 else 10
  }

  fun priceWithTax(price: Int, rate: Int): Int {
      return price + price * rate / 100
  }

  fun main() {
      val prices = listOf(100, 200, 300, 400, 500)
      val rate = taxRateFor("IN")
      var total = 0
      for (price in prices) {
          total += priceWithTax(price, rate)
      }
      println("total = $total")
      println("tax lookups = $taxLookups")
  }
explanation: "The rate was asked for again on every price, so one cheap call was made five times. Asking once before the loop gives the same total with one call."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 4 is the useful one. `taxRateFor` runs once for every price, though its answer never changes. This code has no null values in play.

## Tricks to remember

- Most code is not most time. Sort the bill and read the top line.
- Read Self for the hot function. Total also counts everything the function calls.
- A cheap item bought 1000 times can be the biggest line on the bill.
- Task: record about five seconds of an app doing a heavy loop, open the flame chart, and name the widest bar.

## Common mistakes

- **Thinking the function with the most code is the slow one.** A long function feels like a lot of work. Time is price times calls, and a short function called 1000 times can win. Sort the recording and read the top line.
- **Reading Total instead of Self.** The first function in the list often has the biggest Total, because it called everything else. Self shows the time a function used in its own code, so use it to find the hot function.
- **Thinking a wide bar always means heavy maths.** It feels natural, because wide means busy. My own reasoning: a function can also be slow because it waits, for example on a disk or the network. Check the thread row from the last lesson before you rewrite the code.
- **Trusting the numbers of a recording as exact.** They look precise. The documentation says that timing from method recording might deviate from production because of the overhead. Use the numbers to compare functions, and record again after a fix.

## Where this shows up in Android

A list stutters while you scroll. You record the scroll for about five seconds, which is the length the documentation recommends for method recording, and open Bottom Up sorted by CPU time. The top line is a small function that the code calls for every row (an example: what you see depends on your app).

This lesson explains the top line. A small function called once per row adds up over a long list. Fixing it means calling it less often, as in the Fix the bug task. Then you record again and the bar is gone.

## Say it in an interview

**How do you find the function that uses the most CPU time in your app?**
I record the slow moment with the Android Studio profiler, then open the flame chart or the Top Down chart. The documentation names both for finding the methods that take the most time. I read Self time, not Total, and I use Bottom Up to sort by CPU time.

**What is a flame chart, and how do you read it?**
The documentation says it merges identical call stacks into one longer bar. Its horizontal axis is not a timeline. It shows the relative time each function takes, and the callees that use the most CPU time appear first. A wide bar is a big share of time, not a moment in time.

**What is the difference between wall clock time and CPU time?**
This answer is general knowledge, not from the pages I read. Wall clock time counts every second that passes, including waiting. CPU time counts only the time the processor ran the code. My own reasoning: a function that waits on the network can have a large wall clock time and a small CPU time.

**What goes wrong when the profiler points at a function that is only waiting?**
My own reasoning: you rewrite the maths and nothing gets faster, because the time was spent waiting. The thread row shows a waiting thread in a different colour from a running one, so I check it first. I also record again after every change to see whether the bar really shrank.

## Recap

1. The hot function is the one with the most time in its own code, which is Self.
2. Sampling costs little and can miss short calls, and recording every call misses none but slows the app.
3. A cheap function called thousands of times can top the bill, so call it less often.

## You can now

- Explain why the function with the most lines of code is not always the slow one.
- Predict how many times a function runs when a loop calls it on every pass.
- Fix a loop that asks for the same answer again and again.
