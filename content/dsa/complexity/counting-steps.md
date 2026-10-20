---
id: dsa/complexity/counting-steps
title: How many steps does this code take for 10 items, 100 items and 1000 items?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Count the steps a loop takes as the list grows. One loop grows in step with the list, and a loop inside a loop grows far faster.
video: ""
publishedOn:
updatedOn:
sims: [step-counter-bars]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. This is a theory leaf: the facts are counting arithmetic, checked by running the code, not claims about Android documentation. The Android sentence in the last section ("a list that feels fine with 20 items and stutters with 5,000") is an illustration from the roadmap row, not an official-docs claim; no documentation figure for any phone is stated. The sim id step-counter-bars has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/step-counter-bars.md). Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. The Fix the bug block is marked generated: true; the stem flag ai-review in the roadmap was not checked by this draft. -->
## The question

Your app shows a list of items. With 10 test items the screen opens at once. With 1,000 real items it feels slow, and with 5,000 it stutters.

Nobody changed the code. Only the list grew. How can you tell, before you run anything, how much slower the code will get when the list is 10 times bigger?

## Mental model

Think of a postman counting footsteps.

- The houses are the items in the list.
- The footsteps are the steps the code takes.
- The street is the loop: the postman walks from the first house to the last.
- A second round of walking for each house is a loop inside a loop.

On a street with 10 houses, the postman takes about 10 steps. With 100 houses he takes about 100. The steps grow in the same way the houses do. Now imagine that at every house he walks the whole street again to check on every other house. That is a lot more walking.

**Where the analogy stops:** A postman's steps are all the same size. The steps of code are not. One step might add two numbers, and another might compare two words. This lesson counts a step as one trip through the work inside a loop, so it can say how the count grows. It does not say how many seconds the code takes. Also, a postman gets tired, but a computer does not.

## See it move

<!-- SIM: step-counter-bars -->

**Try this:**
1. Set the slider to 10, then 100, then 1000 items. Watch the bar for one loop and the step counter.
2. Watch the second bar, for a loop inside a loop, at the same three sizes.
3. Double the number of items and watch how each bar changes.

**What you should notice:** The bar for one loop grows in step with the items, and the bar for a loop inside a loop grows much faster.

## Build the logic

### Notice

The postman has a street of 5 houses. He walks past each house once and drops a letter.

- House 1: one step.
- House 2: one step.
- House 3: one step.
- House 4: one step.
- House 5: one step.

Total: 5 steps for 5 houses.

### Guess

What happens to the steps if the street gets 10 times longer, from 10 houses to 100?

- A. The steps stay the same, because the postman is fast.
- B. The steps become 10 times bigger, from 10 to 100.
- C. The steps become 100 times bigger, from 10 to 1,000.

Idea A confuses speed with steps. A faster postman still walks as many steps. Idea B is right for one walk down the street. Idea C is what happens in the next question.

Now change the story. At every house, the postman walks the whole street again to greet every house. For 5 houses, how many steps is that? Each of the 5 houses sends him past all 5 houses.

- A. 5 steps.
- B. 10 steps.
- C. 25 steps.

Idea A forgets the second walk. Idea B adds the two walks together when they multiply. Idea C is the answer: 5 times 5.

### Build

1. Pick a number for the size of the list. Call it n.
2. Count one step each time the work inside the loop runs.
3. For one loop that visits every item once, the steps equal n.
4. For a loop inside a loop, the inner loop runs fully for every item of the outer loop, so the steps equal n times n.
5. To compare two pieces of code, count their steps for 10, 100 and 1000 items and see how fast each count grows.
6. Never judge by running the code once on one machine, because the machine, the load and the data all change the time. The count does not change.

### Name it

A **step** is one unit of work you decide to count, such as one trip through the work in a loop. The size of the input, here the number of items, is called **n**. A **loop** repeats its work, and a **nested loop** is a loop inside another loop.

In Kotlin, `for (i in 1..n)` repeats its work n times, once for each number from 1 to n. `steps++` adds 1 to the counter named `steps`.

```kotlin
fun oneLoop(n: Int): Int {
    var steps = 0
    for (i in 1..n) {
        steps++
    }
    return steps
}

fun twoLoops(n: Int): Int {
    var steps = 0
    for (i in 1..n) {
        for (j in 1..n) {
            steps++
        }
    }
    return steps
}

fun main() {
    for (n in listOf(10, 100, 1000)) {
        println("n = $n: one loop ${oneLoop(n)}, two loops ${twoLoops(n)}")
    }
}
```

It prints three lines, one for each size.

| n | One loop | Loop inside a loop |
|---|---|---|
| 10 | 10 | 100 |
| 100 | 100 | 10000 |
| 1000 | 1000 | 1000000 |

When n grows 10 times, one loop takes 10 times the steps. The nested loop takes 100 times the steps. This is the check "count steps for 10, 100 and 1000 items". The next lesson gives this growth a short name.

## Predict the output

```challenge
type: predict
id: count-one-loop
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  fun main() {
      val n = 5
      var steps = 0
      for (i in 1..n) {
          steps++
      }
      println(steps)
  }
options:
  - id: a
    text: "1"
    why: "You may have counted the one place that adds. It runs once for every house, so the steps add up."
  - id: b
    text: "5"
    correct: true
    why: "The postman walks past 5 houses once, one step each, so the counter ends at 5."
  - id: c
    text: "25"
    why: "You may have multiplied 5 by 5. That happens with a second loop inside this one, and this code has only one."
output: |
  5
```

```challenge
type: predict
id: count-nested-loop
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  fun main() {
      val n = 5
      var steps = 0
      for (i in 1..n) {
          for (j in 1..n) {
              steps++
          }
      }
      println(steps)
  }
options:
  - id: a
    text: "10"
    why: "You may have added the two loops. The inner loop runs completely for each trip of the outer loop, so the counts multiply."
  - id: b
    text: "5"
    why: "You may have counted only the outer street. Each house also sends the postman along the whole street again."
  - id: c
    text: "25"
    correct: true
    why: "5 houses, and for each one the postman walks past all 5 again. 5 times 5 is 25."
output: |
  25
```

## Try it

1. **Write.** Type the program from Name it into the editor: `oneLoop`, `twoLoops` and `main`.
2. **Run.** You should see three lines. For n = 1000 the nested count is 1000000.
3. **Break.** In `main`, change the 1000 in the list to 2000 and change nothing else. Run it again.
4. **Fix.** The last line now says `one loop 2000, two loops 4000000`. The street is twice as long, so one loop has twice the steps and the nested loop has four times the steps. Change 2000 back to 1000 and run it. You see `1000` and `1000000` again.

## Fix the bug

```challenge
type: fix
id: fix-sort-to-find-largest
generated: true
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  var steps = 0

  fun largestBySorting(items: List<Int>): Int {
      val sorted = items.toMutableList()
      for (i in sorted.indices) {
          for (j in 0 until sorted.size - 1 - i) {
              steps++
              if (sorted[j] > sorted[j + 1]) {
                  val temp = sorted[j]
                  sorted[j] = sorted[j + 1]
                  sorted[j + 1] = temp
              }
          }
      }
      return sorted.last()
  }

  fun main() {
      val items = (1..100).toList()
      println("Largest: ${largestBySorting(items)}")
      println("Steps: $steps")
  }
hints:
  - "The answer is right, but look at the steps line. Which loops make the count so large?"
  - "To find the largest, you do not need the whole list in order. Look at each item once and remember the biggest so far."
expected: |
  Largest: 100
  Steps: 100
fix: |
  var steps = 0

  fun largest(items: List<Int>): Int {
      var best = items[0]
      for (item in items) {
          steps++
          if (item > best) {
              best = item
          }
      }
      return best
  }

  fun main() {
      val items = (1..100).toList()
      println("Largest: ${largest(items)}")
      println("Steps: $steps")
  }
explanation: "Sorting the whole list is a loop inside a loop, so the steps grow with n times n. A single pass that remembers the best so far needs only n steps."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

The code is tidy and gives the right answer, so a quick read passes it. The step count is what a careful reviewer checks. Question 4 matters most here: what runs again and again, and how many times?

## Tricks to remember

- Count steps, not seconds.
- One loop: n steps. A loop inside a loop: n times n.
- Ten times the items means ten times the steps for one loop, and a hundred times for a loop inside a loop.
- If it feels fine at 20 items, test the count at 5,000.

## Common mistakes

- **Judging speed by running the code once on your laptop.** One quick run feels like proof. But your laptop is faster than many phones, and a small test list hides the growth. Count the steps for bigger sizes instead.
- **Adding two loops when one is inside the other.** Two loops look like two jobs. When one sits inside the other, the counts multiply.
- **Counting lines of code instead of steps.** Short code feels fast. A short loop inside a loop can take many more steps than a long straight list of lines.
- **Thinking a faster machine fixes a growing count.** It feels like power should solve it. A faster machine takes fewer seconds per step, but the steps still grow with n, and with a nested loop the list soon wins.

## Where this shows up in Android

Think of a list screen that feels fine with the 20 items you used while testing, then stutters when a real user has 5,000. A loop inside a loop at 5,000 items takes 25,000,000 steps, while one loop takes 5,000. The test list was too small to show the difference.

This lesson gives you the first question for that symptom. Is there a loop inside a loop, and how many items does it run over? The step count answers it before you open a profiler.

## Recap

1. A step is one counted trip through the work in a loop, and n is the number of items.
2. One loop takes n steps. A loop inside a loop takes n times n.
3. Count steps for 10, 100 and 1000 items to see how fast the work grows.

## You can now

- Count the steps of a single loop and a nested loop for a given n, without running the code.
- Predict how the step count changes when the number of items doubles.
- Fix code that does a whole sort when one pass over the list gives the same answer.
