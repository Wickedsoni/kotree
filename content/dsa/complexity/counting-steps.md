---
id: dsa/complexity/counting-steps
title: How many steps does this code take for 10 items, 100 items and 1000 items?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Count how often a loop's work runs as a list grows. A loop inside a loop grows far faster than a single loop.
video: ""
publishedOn:
updatedOn:
sims: [step-counter-bars]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- Kotlin ranges, 1..4 includes both ends: https://kotlinlang.org/docs/ranges.html
- Kotlin for loops over ranges and collections: https://kotlinlang.org/docs/control-flow.html
- Android, frames must render in under 16 ms for 60 fps, overrun drops the frame: https://developer.android.com/topic/performance/vitals/render
- Practice problems: docs/practice/practice-dsa.md (tags [both] and [A]; LeetCode 268 is tag [B], number from memory, re-check).
- Theory leaf: the step counts are arithmetic traced by hand; the challenge snippets were run with kotlin 2.4.21 (scripts/kt-run.sh).
-->

## The question

Your app shows a list of items. With 10 test items the screen opens at once. With 1,000 real items it feels slow, and with 5,000 it stutters. Only the list grew. How can you tell, before you run anything, how much slower the code gets when the list is 10 times bigger?

## Mental model

Think of a postman counting footsteps.

- The houses are the items in the list.
- The footsteps are the work the code does.
- The street is one walk from the first house to the last.
- A second walk along the whole street, started again at every house, is a walk inside a walk.

On a street with 10 houses, the postman takes about 10 footsteps. With 100 houses he takes about 100. The footsteps grow as fast as the houses do. Now imagine that at every house he walks the whole street again to greet every house. That is far more walking.

**Where the analogy stops:** A postman's footsteps are all the same size. The work of code is not. One piece of work might add two numbers, and another might compare two words. This lesson counts how many times the work runs, so it can say how the count grows. It does not say how many seconds the code takes. Also, a postman gets tired, and a computer does not.

## See it move

<!-- SIM: step-counter-bars -->

**Try this:**
1. Set the slider to 10, then 100, then 1000 houses. Read the bar and the footstep counter for the single walk.
2. Watch the second bar, for a walk inside a walk, at the same three sizes.
3. Double the number of houses and watch how each bar changes.

**What you should notice:** The bar for a single walk grows in step with the houses, and the bar for a walk inside a walk grows much faster.

## Build the logic

### Notice

The postman has a street of 5 houses. He walks past each house once and drops a letter. That is one footstep per house, so 5 footsteps for 5 houses.

Now he changes his habit. At every house he walks the whole street again to greet all 5 houses. Each of the 5 houses sends him past 5 houses.

### Guess

What happens to the footsteps if the street gets 10 times longer, from 10 houses to 100, and he walks it once?

- A. They stay the same, because the postman is fast.
- B. They become 10 times bigger, from 10 to 100.
- C. They become 100 times bigger, from 10 to 1,000.

Idea A mixes up speed with footsteps. A faster postman still takes as many footsteps. Idea C is the jump you get with a walk inside a walk, not with one walk. Idea B is the answer for one walk.

Now the habit change. For 5 houses, how many footsteps is the walk inside a walk?

- A. 5.
- B. 10.
- C. 25.

Idea A forgets the second walk. Idea B adds the two walks, when they multiply. Idea C is the answer: 5 times 5.

### Build

1. Pick a number for the size of the list. Call it the size.
2. Count one footstep each time the work for one house runs.
3. For one walk that visits every house once, the count equals the size.
4. For a walk inside a walk, the inner walk runs fully for every house of the outer walk, so the count equals the size times the size.
5. To compare two pieces of code, count for 10, 100 and 1000 houses and see how fast each count grows.
6. Do not judge by running the code once on one machine. The machine, the load and the data all change the time. The count does not change.

### Name it

A **step** is one unit of work you decide to count, such as one trip through the work inside a loop. The size of the input, here the number of items, is called **n**. A **loop** repeats its work. A **nested loop** is a loop inside another loop.

In Kotlin, `for (i in 1..n)` repeats its work n times. The Kotlin documentation says the range `1..4` includes both the start and the end, so it covers 1, 2, 3 and 4. The line `steps++` adds 1 to the counter named `steps`.

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

It prints three lines, one for each size. Each line holds the counts in this table.

| n | One loop | Loop inside a loop |
|---|---|---|
| 10 | 10 | 100 |
| 100 | 100 | 10000 |
| 1000 | 1000 | 1000000 |

When n grows 10 times, one loop takes 10 times the steps. The nested loop takes 100 times the steps. This is the check "count steps for 10, 100 and 1000 items".

## Predict the output

```challenge
type: predict
run: false
id: count-nested-3-by-4
verified: kotlin-2.4.21
code: |
  fun main() {
      var steps = 0
      for (house in 1..3) {
          for (turn in 1..4) {
              steps++
          }
      }
      println(steps)
  }
options:
  - id: a
    text: "7"
    why: "You may have added 3 and 4. The inner walk runs fully for each house of the outer walk, so the counts multiply."
  - id: b
    text: "3"
    why: "You may have counted only the outer street. Each house also starts the inner walk of 4 footsteps."
  - id: c
    text: "12"
    correct: true
    why: "3 houses, and at each one the postman takes 4 footsteps. 3 times 4 is 12."
output: |
  12
```

```challenge
type: predict
run: false
id: largest-in-one-pass
verified: kotlin-2.4.21
code: |
  fun main() {
      val items = listOf(3, 9, 4, 7, 1)
      var steps = 0
      var best = items[0]
      for (item in items) {
          steps++
          if (item > best) best = item
      }
      println("$best $steps")
  }
options:
  - id: a
    text: "9 5"
    correct: true
    why: "The postman passes each of the 5 houses once, remembering the biggest number so far. That is 5 footsteps, and the biggest is 9."
  - id: b
    text: "9 25"
    why: "You may have expected a walk inside a walk. This code has one loop, so the count is 5, not 5 times 5."
  - id: c
    text: "9 1"
    why: "You may have counted the one line that adds. That line runs once for every house, so the count reaches 5."
output: |
  9 5
```

```challenge
type: predict
run: false
id: double-the-street
verified: kotlin-2.4.21
code: |
  fun main() {
      for (n in listOf(10, 20)) {
          var steps = 0
          for (i in 1..n) {
              for (j in 1..n) {
                  steps++
              }
          }
          println(steps)
      }
  }
options:
  - id: a
    text: "100, then 200"
    why: "You may have expected double the houses to give double the footsteps. That is true for one walk. A walk inside a walk multiplies, so it grows faster."
  - id: b
    text: "100, then 400"
    correct: true
    why: "10 times 10 is 100. With twice the houses, 20 times 20 is 400. Doubling the street quadruples the footsteps."
  - id: c
    text: "100, then 120"
    why: "You may have added 20 to the first count. The inner walk starts again at every house, so the counts multiply, not add."
output: |
  100
  400
```

## Tricks to remember

- Count footsteps, not seconds.
- One walk: n footsteps. A walk inside a walk: n times n.
- Ten times the houses means ten times the footsteps for one walk, and a hundred times for a walk inside a walk.
- If it feels fine at 20 houses, count at 5,000.

## Common mistakes

- **Judging speed by running the code once on your laptop.** One quick run feels like proof. But your laptop is faster than many phones, and a small test list hides the growth. Count the steps for bigger sizes instead.
- **Adding two loops when one is inside the other.** Two loops look like two jobs. When one sits inside the other, the counts multiply.
- **Counting lines of code instead of steps.** Short code feels fast. A short loop inside a loop can take far more steps than a long list of single lines.
- **Sorting the whole list to find the biggest item.** It gives the right answer and looks tidy. A hand-written sort such as bubble sort uses a loop inside a loop (general knowledge), while one pass that remembers the biggest so far needs n steps.

## Where this shows up in Android

Picture a list screen that feels fine with the 20 items you used while testing, then stutters when a real user has 5,000. The Android page on slow rendering says an app must render each frame in under 16 ms to reach 60 frames per second. A frame that overruns is dropped. This next part is my own reasoning. A loop inside a loop over 5,000 items takes 25,000,000 steps, while one loop takes 5,000. The test list was too small to show the difference.

This lesson gives you the first question for that symptom. Is there a loop inside a loop, and how many items does it run over?

## Say it in an interview

**Why count how often the inner loop runs, instead of counting lines of code?**
Lines are written once, but a loop body runs many times. A 3-line nested loop over n items runs its body n times n, while 30 straight lines run 30 times. So the loop count tells you how the work grows, and the line count does not.

**Why count steps instead of measuring milliseconds?**
Time depends on the machine, what else is running and the data. The step count does not, and you can work it out before running anything. The trade-off is that steps ignore the cost of each step, so my own reasoning is to count first, then measure on a real device.

**An algorithm takes 1,000 steps for n = 10 and 1,000,000 steps for n = 100. What do you expect for n = 1,000?**
The size grew 10 times and the steps grew 1,000 times, which fits steps that grow like n times n times n. That predicts about 1,000,000,000 steps at n = 1,000. My own reasoning: two data points only suggest a pattern, so I would read the code to check for three nested loops.

**A loop runs n times and calls a helper that scans 100 items each time. How many steps for n = 10, 100 and 1000?**
That is n times 100: 1,000, then 10,000, then 100,000. The 100 stays fixed, so the steps grow in step with n. If the helper scanned a list that also grows with n, the count would be n times n instead.

## Recap

1. A step is one counted unit of work, and n is the number of items.
2. One loop takes n steps, and a loop inside a loop takes n times n.
3. Count steps for 10, 100 and 1000 items to see how fast the work grows.

## You can now

- Count the steps of a nested loop with different sizes, without running the code.
- Predict how the step count changes when the number of items doubles.
- Spot that finding the biggest item needs one pass of n steps.

## Practise on these

Each problem asks you to see how many times a loop runs. The list runs from the gentlest problem to the hardest.

1. LeetCode 1480, Running Sum of 1d Array. Count the loop runs for one pass over n numbers. https://leetcode.com/problems/running-sum-of-1d-array/
2. LeetCode 1342, Number of Steps to Reduce a Number to Zero. Count the operations as you halve or subtract, and watch how the count grows with the input. https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/
3. LeetCode 268, Missing Number. Compare a method with several passes against one pass over the list. https://leetcode.com/problems/missing-number/
4. LeetCode 1512, Number of Good Pairs. Count the steps of checking every pair with a loop inside a loop. https://leetcode.com/problems/number-of-good-pairs/
