---
id: dsa/complexity/big-o-notation
title: What does O(n) say about my code, and what does it ignore?
status: shadow
difficulty: medium
order: 2
requires: [dsa/complexity/counting-steps]
summary: O(n) names how fast the step count grows as the input grows. It ignores the number in front and the small parts.
video: ""
publishedOn:
updatedOn:
sims: [drop-the-small-parts]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES
- Android, "Slow rendering" (16 ms frame, jank): https://developer.android.com/topic/performance/vitals/render (page said last updated 2026-09-21)
- Java SE 21 ArrayList class description (constant time get, linear time for the rest): https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html
- Kotlin binarySearch (sorted list needed; page states no complexity): https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/binary-search.html
- Kotlin sorted() (stable; page states no complexity): https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/sorted.html
- Everything else is general knowledge or my own reasoning and is labelled in the text.
- Practice data: docs/practice/practice-dsa.md, section dsa/complexity/big-o-notation.
-->

## The question

Your app shows a list and sorts it every time the screen redraws. With 20 test items it feels fine. A teammate says: "That sort is O(n log n), so the redraw costs more as the list grows." What does that sentence actually tell you? And what does it leave out?

## Mental model

Think of a taxi quote: "5 to start, then about 2 per kilometre".

- The trip length is n, the size of the input.
- The total fare is the number of steps your code takes.
- The 5 to start is the small part that does not grow with the trip.
- The 2 per kilometre is the rate. The rate decides the fare on a long trip.

For a 1,000 km trip the fare is 2,005. The 5 is not even a quarter of one percent of it. If someone asks "how does the fare grow?", the useful answer is "in step with the kilometres". You drop the start fee because it stops mattering on a long trip.

**Where the analogy stops:** A taxi rate of 2 or 3 changes your bill, and you would care. Big O drops that number too, so two pieces of code with the same Big O can still differ by three times. A taxi also has one trip. Code has to behave for every size of input.

## See it move

<!-- SIM: drop-the-small-parts -->

**Try this:**
1. Slide n to 3, then to 10, then to 1000. Watch which of the two curves is lower each time.
2. Switch on "Drop the small parts" and watch the formula 3n + 7 turn into n.

**What you should notice:** The lower curve changes as n grows, and dropping the small parts keeps the shape of each curve.

## Build the logic

### Notice

Two pieces of code count their steps.

- Code A takes 3n + 7 steps.
- Code B takes n times n steps.

At n = 3, code A takes 16 steps and code B takes 9. At n = 10, code A takes 37 and code B takes 100. At n = 1000, code A takes 3,007 and code B takes 1,000,000.

### Guess

At n = 1000, how many steps does the code 3n² + 5n + 2 take, and where do nearly all of them come from?

- A. From the 3n² part, which is 3,000,000 of the 3,005,002 steps.
- B. From the 2, because it is the only part that never changes.
- C. From the 5n part, because it is the middle term.

Idea A is what the arithmetic says. Idea B confuses "never changes" with "matters most". The 2 is the start fee. Idea C picks the middle without checking the numbers.

### Build

1. Write the step count as a formula in n, for example 3n² + 5n + 2.
2. Find the part that grows fastest as n grows. Here it is 3n².
3. Drop every smaller part. The 5n and the 2 go.
4. Drop the number in front of what is left. The 3 goes.
5. What remains is n². You write that as O(n²).

You throw away information on purpose. The question you keep is: when the input gets 10 times bigger, what happens to the steps?

### Name it

**Big O** is a short way to say how the step count grows as n grows. You read O(n) as "order n". The steps grow in step with n. Strictly, O(n) is an upper limit on that growth. This lesson uses it the way a code review does (general knowledge).

Big O ignores three things:

- The number in front. 3n and 100n are both O(n).
- The smaller parts. n² + 50n is O(n²).
- The size of one step. A step can be quick or slow. Big O only counts how many.

Here are the five names you will meet most. The steps column is for n = 1000 (the last two are rounded).

| Name | You say | Picture | Example | Steps at n = 1000 |
|---|---|---|---|---|
| Constant | O(1) | Same work for any n | Read item 5 of a list by its position | 1 |
| Logarithmic | O(log n) | Halve the problem each time | Binary search in a sorted list | about 10 |
| Linear | O(n) | One pass over everything | Check whether a list contains a value | 1,000 |
| Linearithmic | O(n log n) | A pass for each halving | Sort a list with a good general sort | about 10,000 |
| Quadratic | O(n²) | A loop inside a loop | Compare every item with every other | 1,000,000 |

Some of these are written down by a library. The Java SE documentation says the `get` operation of `ArrayList` runs in constant time and that most other operations run in linear time, "roughly speaking". The rest of the table is general knowledge. The Kotlin pages for `binarySearch` and `sorted` state no complexity at all.

```kotlin
fun stepsA(n: Int): Int = 3 * n + 7

fun stepsB(n: Int): Int = n * n

fun main() {
    for (n in listOf(3, 10, 1000)) {
        println("n = $n: A takes ${stepsA(n)}, B takes ${stepsB(n)}")
    }
}
```

This prints three lines.

| n | A takes | B takes |
|---|---|---|
| 3 | 16 | 9 |
| 10 | 37 | 100 |
| 1000 | 3007 | 1000000 |

At n = 3 the "slower" shape B is actually the cheaper one. Big O describes what happens as n gets big, not what happens at 3.

## Predict the output

```challenge
type: predict
run: false
id: bigger-at-a-thousand
verified: not-run
code: |
  Code A takes n * n steps.
  Code B takes 50 * n steps.
  Take n = 1000.
  Which code takes more steps?
options:
  - id: a
    text: "Code B, because 50 is bigger than 1"
    why: "You may have compared the numbers in front. At n = 1000, the n * n part is 1000 times n, which is far more than 50 times n."
  - id: b
    text: "Code A: 1000000 steps against 50000"
    correct: true
    why: "The rate in the taxi quote grows with the trip. n * n has a rate of n per kilometre, so it beats a fixed rate of 50 once n passes 50."
  - id: c
    text: "They take the same number of steps"
    why: "You may have thought both are 'big'. 1000000 and 50000 are 20 times apart."
output: |
  Code A: 1000000 steps against 50000
```

```challenge
type: predict
run: false
id: simplify-the-formula
verified: not-run
code: |
  A piece of code takes 3n² + 5n + 2 steps.
  Which is its Big O?
options:
  - id: a
    text: "O(3n²)"
    why: "You dropped the small parts but kept the 3. The number in front is dropped too."
  - id: b
    text: "O(n² + n)"
    why: "You kept the 5n. At n = 1000 it is 5,000 steps against 3,000,000, so it is a small part."
  - id: c
    text: "O(n²)"
    correct: true
    why: "Keep the part that grows fastest, drop the smaller parts, then drop the number in front. Like the taxi: only the rate on a long trip is left."
output: |
  O(n²)
```

```challenge
type: predict
run: false
id: small-n-surprise
verified: not-run
code: |
  Code A takes 3n + 7 steps.
  Code B takes n * n steps.
  Which statement is true?
options:
  - id: a
    text: "B is cheaper at n = 3 (9 against 16), but dearer at n = 10 (100 against 37)"
    correct: true
    why: "Big O says what happens as n grows. For a very small trip the start fee is still big, so the curves can swap places."
  - id: b
    text: "A is cheaper at every n"
    why: "You may have trusted the Big O names alone. At n = 3, B takes 9 steps and A takes 16."
  - id: c
    text: "B is dearer at every n"
    why: "You may have expected the quadratic shape to lose everywhere. At n = 3 it wins, because the start fee 7 is bigger than the n * n part."
output: |
  B is cheaper at n = 3 (9 against 16), but dearer at n = 10 (100 against 37)
```

## Tricks to remember

- Keep the fastest-growing part. Drop the rest. Drop the number in front.
- Ask: if n gets 10 times bigger, what happens to the steps?
- Big O names the shape of growth. It is not a count and not seconds.
- For a small n, the shape that loses later can win now.

## Common mistakes

- **Thinking O(n) means exactly n steps.** The name looks like a count. It hides the number in front and the small parts, so 3n + 7 is also O(n).
- **Thinking O(1) means instant.** The word constant sounds like fast. It means the work does not grow with n. One constant step can still be a slow one.
- **Thinking two O(n) pieces of code run equally fast.** Same name feels like same speed. Big O drops the number in front, so one can take 3 times the steps of the other.
- **Choosing the code with the better Big O at a tiny n.** The label feels like a promise. At n = 3 the plain n * n code beat 3n + 7, so measure when n is small.

## Where this shows up in Android

The Android documentation on slow rendering says an app must render each frame in under 16 ms to reach 60 frames per second. If it overruns, the system drops the frame, and the user sees stutter, which the page calls jank.

Now picture the screen that re-sorts its list on every redraw. This part is my own reasoning, and I do not have an official number. A good general sort is O(n log n) work, and here it runs again for every frame. With 20 items the cost is tiny. With 5,000 items the same sort has far more to do on every frame, so some frames overrun the 16 ms. The symptom is a list that scrolls fine in testing and stutters for a real user. The shape of the growth, not the first test, is what explains it.

## Say it in an interview

**Why does Big O drop constants and lower-order terms like the 50 in O(2n + 50)?**
As n grows, the biggest term decides the cost, so the rest stops mattering. The constants also depend on the machine and the language, so they say little about growth. The trade-off is that constants matter for small n and when you compare two pieces of code with the same Big O (my own reasoning).

**When can an O(n²) algorithm beat an O(n log n) one in real code?**
When n is small, or when each step of the O(n²) code is very cheap. This is general knowledge: sorting libraries often switch to a simple quadratic sort such as insertion sort for tiny ranges inside a faster sort. The lesson is to know your real n before you chase a better label.

**Is O(1) guaranteed to be instant?**
No. O(1) means the work does not grow with the input size. A single constant step could still be slow. And a quick O(n) loop over 10 items can beat a slow O(1) call.

**What is the time complexity of looping over a list of n items and calling `contains` on a set inside the loop?**
With a hash-based set, `contains` is O(1) on average (general knowledge), so the loop is O(n). If the second collection is a list of m items, `contains` scans it, so the loop is O(n × m). The answer depends on what kind of collection is behind `contains`.

## Recap

1. Big O names how the step count grows as the input grows.
2. It drops the number in front and every smaller part.
3. It describes large n, so a small n can behave differently.

## You can now

- Simplify a step formula such as 3n² + 5n + 2 to its Big O.
- Predict which of two step formulas is bigger at n = 1000.
- Explain why O(n) does not mean exactly n steps.

## Practise on these

Each problem has a slow way and a fast way. Say the Big O of both before you code. The list runs from the gentlest to the hardest.

1. LeetCode 704, Binary Search (Easy). Halve the range each time and compare O(log n) with a plain O(n) scan. https://leetcode.com/problems/binary-search/
2. LeetCode 169, Majority Element (Easy). Compare sorting, a counting map and a one-pass voting method, and say the time and the extra memory of each. https://leetcode.com/problems/majority-element/
3. LeetCode 50, Pow(x, n) (Medium). Cut n multiplications down to about log n by squaring. https://leetcode.com/problems/powx-n/
4. LeetCode 4, Median of Two Sorted Arrays (Hard). The statement asks for O(log(m + n)) time, where merging the two lists is too slow. https://leetcode.com/problems/median-of-two-sorted-arrays/
