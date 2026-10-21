---
id: daa/analysis/best-worst-average-case
title: Why can the same code be fast on one input and slow on another?
status: shadow
difficulty: easy
order: 1
requires: []
summary: A search can take 1 step or 1,000 steps on lists of equal size. Best, worst and average case say which one you mean.
video: ""
publishedOn:
updatedOn:
sims: [linear-search-shelf]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- Theory leaf: the step counts are traced by hand and the one average (1 to 5 gives 3) is arithmetic. The best-and-worst-steps snippet was run with kotlin 2.4.21 (scripts/kt-run.sh); the two non-Kotlin items were not run.
- Kotlin for loops over a collection: https://kotlinlang.org/docs/control-flow.html
- Best, worst and average case, amortized cost, linear search: general knowledge, labelled in the text.
- Android Where-this-shows-up paragraph is an illustration from the roadmap row, labelled "my own reasoning".
- Practice problems: docs/practice/practice-daa.md (LeetCode 28 and HackerRank Running Time of Algorithms; numbers and URLs copied from the supplied file, not opened; reviewer re-checks).
-->

## The question

A developer says: "I tested my search on a list of 1,000 names. It took one step." Another developer runs the same code on another list of 1,000 names. It takes a thousand steps, with the same code and the same size of list. How can the speed be so different, and which number should you believe?

## Mental model

Think of looking for your keys.

- The places you check are the items in a list: left pocket, right pocket, bag, desk, shelf.
- You check them one by one, in a fixed order.
- Each place holds one thing with a number on it. Your keys carry a number too, and you want the place that holds it.
- Sometimes the keys are in the first place you look.
- Sometimes they are in the last place you look.
- Sometimes they are in none of the places, and you only learn that after checking every one.

How long the search takes depends on where the keys happen to be, not only on how many places there are.

**Where the analogy stops:** When you look for your keys, you remember where you have been, and you might guess the likely spots first. A program that checks in order has no hunches. It always checks the next place. Also, a real person tires and speeds up, but the check count stays exactly the same each time for the same places and the same keys.

## See it move

<!-- SIM: linear-search-shelf -->

**Try this:**
1. Put the keys in the first place and press "Search". Read the check count.
2. Put the keys in the middle place, then in the last place, and search each time.
3. Take the keys out of every place and search again.

**What you should notice:** The row has the same number of places each time. The check count depends on where the keys sit, and missing keys cost the most.

## Build the logic

### Notice

Five places in a row hold these numbers, in this order: 5, 8, 2, 9, 4. You look for the place holding one number by checking from the left, one place at a time. Each check is one step.

- Looking for 5: the first place matches. 1 step.
- Looking for 4: the last place matches. 5 steps.
- Looking for 7: no place matches. You check all 5 and only then know it is missing. 5 steps.

### Guess

What speed would you say this search has?

- A. 1 step, because that is the fewest it can take.
- B. 5 steps, because that is the most it can take.
- C. It depends on where the keys are, so one number is not enough.

Idea A is the number a lucky test shows. Idea B is the number an unlucky test shows. Idea C is true, and we can say it more precisely.

### Build

1. Pick the number of places, here 5.
2. Find the luckiest case for that many places: the keys are in the first place. Count the steps. That is the fewest the search can take.
3. Find the unluckiest case for that many places: the keys are in the last place, or in none. Count the steps. That is the most the search can take.
4. For something in between, decide where the keys are likely to be. For example, assume they are equally likely to be in any of the 5 places. Then the steps are 1, 2, 3, 4 and 5, and you average them.
5. When you tell someone how fast the search is, say which of the three you mean.

### Name it

Checking items one after another from the start is a **linear search**. The three answers have names.

- The **best case** is the input of a given size that makes the code take the fewest steps. For the five places: 1 step.
- The **worst case** is the input of the same size that makes it take the most steps. For the five places: 5 steps, with the keys last or missing.
- The **average case** is the typical number of steps under a stated assumption about the input. If the keys are equally likely to be in any of the 5 places, the steps are 1, 2, 3, 4 and 5, and their average is 3.

The word **input** here means the data you give the code. Two inputs of the same size can need very different numbers of steps.

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

This prints `1`, then `5`, then `5`. The function `search` counts one step for each place it checks and stops as soon as it finds the target. When the target is missing, it checks every place.

## Predict the output

```challenge
type: predict
run: false
id: best-and-worst-steps
verified: kotlin-2.4.21
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
      println(search(listOf(3, 6, 1, 8), 3))
      println(search(listOf(3, 6, 1, 8), 8))
  }
options:
  - id: a
    text: "1, then 4"
    correct: true
    why: "The 3 sits in the first place, so one check finds the keys. The 8 sits in the last place, so four checks are needed. Same code, same row, different keys."
  - id: b
    text: "1, then 1"
    why: "You may have expected the same speed for the same code. The first number is the best case. The 8 sits last, so that search takes more steps."
  - id: c
    text: "4, then 4"
    why: "You may have expected the worst case every time. The 3 is in the first place, so the first search stops after one check."
output: |
  1
  4
```

```challenge
type: predict
run: false
id: pick-the-worst-key
verified: not-run
code: |
  Row of places:  10, 20, 30, 40, 50
  The developer searched for 10 and saw 1 step.
  Which of these keys makes the search take the most steps?
    A. 10
    B. 30
    C. 50
options:
  - id: a
    text: "10"
    why: "You may have picked the key the developer already tried. It sits in the first place, so it is the best case, and the search stops after one check."
  - id: b
    text: "30"
    why: "You may have picked the middle. It needs 3 checks, which is more than 1 but not the most."
  - id: c
    text: "50"
    correct: true
    why: "The 50 sits in the last place, so all 5 places are checked. That is the most steps for a key that is in the row."
output: |
  50
```

```challenge
type: predict
run: false
id: average-of-five
verified: not-run
code: |
  Row of places:  5, 8, 2, 9, 4
  Key looked for:   5  8  2  9  4   (each equally likely)
  Steps to find it: 1  2  3  4  5
  What is the average number of steps?
options:
  - id: a
    text: "5"
    why: "You may have picked the largest count. That is the worst case. The average adds up the five counts and divides by 5."
  - id: b
    text: "3"
    correct: true
    why: "1 plus 2 plus 3 plus 4 plus 5 is 15, and 15 divided by 5 places is 3. This holds only if each place is equally likely."
  - id: c
    text: "1"
    why: "You may have picked the best case. The average counts every place, not only the lucky one."
output: |
  3
```

## Tricks to remember

- Same code, different input, different steps.
- Best case is the first place. Worst case is the last place, or no place.
- Say which case you mean when you say how fast the search is.
- Test with the unlucky key, not only the lucky one.

## Common mistakes

- **Thinking an algorithm has one speed.** One timing feels like the answer. The same code needs 1 step on one input and 1,000 on another, so ask which input.
- **Testing only on a small tidy list.** A tidy list feels like a fair test. If the key is near the front, the test shows the best case and hides the worst.
- **Thinking the average case is always the middle.** The middle sounds fair. The average depends on an assumption about where the keys sit, so state the assumption.
- **Forgetting that missing keys cost the most.** Finding something feels like the main job. A search that must prove something is missing checks every place.

## Where this shows up in Android

Picture an app tested with a small, tidy list where the item you open is near the top. It feels fast. Then a real user has a long, messy list and the item sits at the bottom, or is not there at all, and the screen slows down. This is my own reasoning, not an official figure. The test only ever hit the best case.

When someone says "this is fast on my phone", the first question from this lesson is: on which input? Check the worst case before you ship.

## Say it in an interview

**What are the best, average and worst case of a linear search, and which do you quote?**
Best case: the target is first, so 1 step. Worst case: the target is last or missing, so n steps for n items. Average case: if every place is equally likely, about half of n. Interviewers write these as O(1), O(n) and O(n), where **Big O** is a short way to say how the steps grow as n grows. My own reasoning: quote the worst case when you want a promise that holds for every input, because the average rests on an assumption about the input.

**What is a worst-case input? Give one for linear search.**
It is the input of a given size that makes the code take the most steps. For linear search it is a target in the last place or not in the list at all. It depends on the algorithm: a list that is already sorted is not a bad input for linear search.

**What is the difference between average case and amortized cost?**
Average case needs an assumption about the input, such as every position being equally likely. Amortized cost is a guarantee over a whole run of operations, with no assumption about the input. For example, adding to a growable list is usually one step, but now and then the list copies everything into a bigger space. Averaged over many adds, the cost per add stays small (general knowledge).

**How would you test that a search is fast?**
Test the worst case on purpose: the target in the last place and a target that is missing. Also test typical data, and say which case each number comes from. My own reasoning: one timing on a lucky input proves nothing.

## Recap

1. The same code can take very different numbers of steps on inputs of the same size.
2. Best case is the fewest steps, worst case is the most, and average case needs a stated assumption.
3. A speed claim means little until it says which case it measured.

## You can now

- Predict the steps a linear search takes for a key at the front and at the end.
- Pick the key that gives the worst case for a given row.
- Work out the average steps of a linear search when every place is equally likely.

## Practise on these

Each problem has a worst-case input that is different from the typical one. Check the statement for the exact rules. The list runs from the gentlest problem to the hardest.

1. LeetCode 28, Find the Index of the First Occurrence in a String. A plain search through the text is quick on most inputs and slow on texts like "aaaa...ab". https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
2. HackerRank, Running Time of Algorithms (Algorithms, Sorting). Count the shifts insertion sort does on a sorted list (the best case) and on a reversed list (the worst case). https://www.hackerrank.com/challenges/runningtime/problem
3. LeetCode 81, Search in Rotated Sorted Array II. Count the checks when the list has repeated values and you cannot tell which half is sorted. https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
4. LeetCode 912, Sort an Array. Count the steps a sort takes on a sorted list and on a reversed list. https://leetcode.com/problems/sort-an-array/
5. LeetCode 215, Kth Largest Element in an Array. Compare a method that sorts the whole list with one that stops once it finds the answer. https://leetcode.com/problems/kth-largest-element-in-an-array/
