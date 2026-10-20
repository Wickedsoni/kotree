---
id: daa/analysis/best-worst-average-case
title: Why can the same code be fast on one input and slow on another?
status: shadow
difficulty: easy
order: 1
requires: []
summary: The same search can take 1 step or 1,000 steps on lists of equal size. Best, worst and average case say which one you mean.
video: ""
publishedOn:
updatedOn:
sims: [linear-search-shelf]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. This is a theory leaf: the facts are step counts and one small average, checked by running the code (linear search over 5 and 1000 items) and by hand (average of 1 to 5 is 3). The Android sentence in the last section is an illustration from the roadmap row, not an official-docs claim. The roadmap row says the leaf assumes dsa/complexity/big-o-notation, which sits in another branch and is not in requires (V3, same-branch only); this draft does not use Big O notation, so it stands alone. The sim id linear-search-shelf has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/linear-search-shelf.md). Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. The Fix the bug block is marked generated: true; the stem flag ai-review in the roadmap was not checked by this draft. -->
## The question

A developer says: "I tested my search on a list of 1,000 names. It took one step." Another developer tries the same code on another list of 1,000 names. It took a thousand steps.

Same code. Same size of list. How can the speed be so different? And which number should you believe?

## Mental model

Think of looking for your keys.

- The pockets and places you check are the items in a list.
- You check them one by one, in a fixed order: left pocket, right pocket, bag, desk, and so on.
- Sometimes the keys are in the first place you look.
- Sometimes they are in the last place you look.
- Sometimes they are not anywhere you looked, and you only learn that after checking every place.

How long the search takes depends on where the keys happen to be, not only on how many places there are.

**Where the analogy stops:** When you look for your keys, you remember where you have been, and you might guess the likely spots first. A program that checks the list in order has no hunches. It always checks the next item. Also, a real person tires and speeds up, but the step count here stays exactly the same each time for the same list.

## See it move

<!-- SIM: linear-search-shelf -->

**Try this:**
1. Put the target ball at the front of the shelf and press "Search". Read the step count.
2. Put the target in the middle, then at the end, and search each time.
3. Remove the target from the shelf and search again.

**What you should notice:** The shelf has the same number of balls each time, but the step count depends on where the target sits, and a missing target costs the most.

## Build the logic

### Notice

A shelf has 5 balls with these numbers, in this order: 5, 8, 2, 9, 4. You look for one number by checking the balls from the left, one at a time, and counting each check as one step.

- Looking for 5: the first ball matches. 1 step.
- Looking for 4: the last ball matches. 5 steps.
- Looking for 7: no ball matches. You check all 5 and then know it is missing. 5 steps.

### Guess

What speed would you say this search has?

- A. 1 step, because that is the fastest it can be.
- B. 5 steps, because that is the most it can take.
- C. It depends on where the target is, so a single number is not enough.

Idea A is the number a lucky test shows. Idea B is the number a worst-luck test shows. Idea C is true, and we can say it more precisely.

### Build

1. Pick the size of the list, here 5.
2. Look at the luckiest list of that size: the target is at the front. Count the steps. That is the least the search can take.
3. Look at the unluckiest list of that size: the target is last, or missing. Count the steps. That is the most the search can take.
4. For something in between, decide how the target is placed. For example, assume it is equally likely to be in any of the 5 places, and average the steps.
5. When you tell someone how fast the code is, say which of the three you mean.

### Name it

Checking items one after another from the start is a **linear search**. The three answers have names.

- The **best case** is the input of a given size that makes the code take the fewest steps. For the shelf: 1 step.
- The **worst case** is the input of the same size that makes it take the most steps. For the shelf: 5 steps, with the target last or missing.
- The **average case** is the typical number of steps, under a stated assumption about the input. If the target is equally likely to be in any of the 5 places, the steps are 1, 2, 3, 4 and 5, and their average is 3.

The word **input** here means the data you give the code. Two inputs of the same size can need very different steps.

```kotlin
fun search(items: List<Int>, target: Int): Int {
    var steps = 0
    for (item in items) {
        steps++
        if (item == target) return steps
    }
    return steps
}

fun main() {
    val items = listOf(5, 8, 2, 9, 4)
    println(search(items, 5))
    println(search(items, 4))
    println(search(items, 7))
}
```

This prints `1`, then `5`, then `5`. `search` counts one step for each item it checks and stops as soon as it finds the target. When the target is missing, it checks every item.

## Predict the output

```challenge
type: predict
id: best-and-worst-steps
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  fun search(items: List<Int>, target: Int): Int {
      var steps = 0
      for (item in items) {
          steps++
          if (item == target) return steps
      }
      return steps
  }

  fun main() {
      val items = listOf(5, 8, 2, 9, 4)
      println(search(items, 5))
      println(search(items, 4))
  }
options:
  - id: a
    text: "1, then 1"
    why: "You may have expected the same speed for the same code. The first number is the best case. The 4 sits last, so that search takes more steps."
  - id: b
    text: "5, then 5"
    why: "You may have expected the worst case every time. The 5 is the first ball, so the first search stops after one check."
  - id: c
    text: "1, then 5"
    correct: true
    why: "The 5 is in the first pocket, so one step. The 4 is in the last pocket, so five steps. Same code, same list, different targets."
output: |
  1
  5
```

```challenge
type: predict
id: missing-target-steps
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  fun search(items: List<Int>, target: Int): Int {
      var steps = 0
      for (item in items) {
          steps++
          if (item == target) return steps
      }
      return steps
  }

  fun main() {
      val items = listOf(5, 8, 2, 9, 4)
      println(search(items, 7))
  }
options:
  - id: a
    text: "0"
    why: "You may have thought a missing target costs nothing. The search only learns it is missing after checking every place."
  - id: b
    text: "1"
    why: "You may have thought it gives up at the first mismatch. It keeps looking until the list ends."
  - id: c
    text: "5"
    correct: true
    why: "No pocket holds a 7, so you check all five before you know. A missing target is a worst case."
output: |
  5
```

## Try it

1. **Write.** Type the `search` function and a `main` that searches the list `listOf(5, 8, 2, 9, 4)` for the target 5 and prints the steps.
2. **Run.** You should see one line: `1`.
3. **Break.** Change the list to `listOf(8, 2, 9, 4, 5)` and change nothing else. Run it again.
4. **Fix.** The line now says `5`. The code did not change. The input did. Put the list back as `listOf(5, 8, 2, 9, 4)` and run it. The line says `1` again.

## Fix the bug

```challenge
type: fix
id: fix-timed-on-a-lucky-input
generated: true
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  var steps = 0

  fun findIndex(items: List<Int>, target: Int): Int {
      for (i in items.indices) {
          steps++
          if (items[i] == target) {
              return i
          }
      }
      return -1
  }

  fun main() {
      // We want to know the most steps this search can take for 1000 items.
      val items = (1..1000).toList()
      findIndex(items, 1)
      println("Steps for 1000 items: $steps")
  }
hints:
  - "The code is fine. Look at the item that main searches for, and where it sits in the list."
  - "To see the most steps, search for the item the loop reaches last."
expected: |
  Steps for 1000 items: 1000
fix: |
  var steps = 0

  fun findIndex(items: List<Int>, target: Int): Int {
      for (i in items.indices) {
          steps++
          if (items[i] == target) {
              return i
          }
      }
      return -1
  }

  fun main() {
      val items = (1..1000).toList()
      findIndex(items, 1000)
      println("Steps for 1000 items: $steps")
  }
explanation: "The test searched for the first item, so it measured the best case and made the search look fast. Searching for the last item shows the worst case."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, the function returns -1 for a missing item instead of null. A search for a missing item is also a worst case, so check that your test covers it. Question 5 is the main one: would you accept a speed claim that was measured on one lucky input?

## Tricks to remember

- Same code, different input, different steps.
- Best case is the lucky pocket. Worst case is the last pocket, or no pocket.
- Say which case you mean when you say how fast the code is.
- Test the unlucky input, not only the lucky one.

## Common mistakes

- **Thinking an algorithm has one speed.** One timing feels like the answer. The same code needs 1 step on one input and 1,000 on another, so ask which input.
- **Testing only on a small tidy list.** A tidy list feels like a fair test. If the target is near the front, the test shows the best case and hides the worst.
- **Thinking the average case is always the middle.** The middle sounds fair. The average depends on an assumption about where targets sit, so state the assumption.
- **Forgetting that a missing item costs the most.** Finding something feels like the main job. A search that must prove something is missing checks every place.

## Where this shows up in Android

An app is tested with a small, tidy list where the item you open is near the top, and it feels fast. Then a real user has a long, messy list and the item sits at the bottom, or is not there at all, and the screen slows down. The test only ever hit the best case.

When someone says "this is fast on my phone", the first question is: on which input? Check the worst case before you ship.

## Recap

1. The same code can take very different steps on inputs of the same size.
2. Best case is the fewest steps, worst case is the most, average case needs a stated assumption.
3. A speed claim means little until it says which case it measured.

## You can now

- Explain best, worst and average case using the keys and the pockets.
- Predict the steps a linear search takes for a target at the front, at the end and missing.
- Fix a speed test that measured only the best case.
