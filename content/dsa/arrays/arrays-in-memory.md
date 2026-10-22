---
id: dsa/arrays/arrays-in-memory
title: Why is reading item 500 as quick as reading item 1?
status: live
difficulty: easy
order: 5
requires: [dsa/complexity/big-o-notation]
summary: An array keeps items side by side, all the same size, so the computer jumps straight to any numbered spot in one step.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [locker-row-reach]
playground: true
fruit: false
upnext: false
---

## The question

A shopping app keeps 1,000 items in a list. You ask for item 500. Then you ask for item 1. Does the second answer come back faster? Does the computer have to go past 499 items to reach the 500th?

## Mental model

Think of a long corridor of lockers.

- The corridor is the memory. The lockers stand side by side with no gaps.
- Every locker is the same width.
- Each locker has a number painted on it. The first is number 0.
- A bag in a locker is one item.
- The corridor door is where the row starts.

To reach locker 500, you do not open lockers 0 to 499. You know the width of one locker, so you work out how far locker 500 is from the door and walk straight there. Locker 1 and locker 500 each take one calculation.

**Where the analogy stops:** In a real corridor, a farther locker means a longer walk. A computer reaches any address in about the same time. Real lockers can also differ in size. In a row of whole numbers every spot is the same size, and that is what makes the calculation work.

## See it move

```sim
id: locker-row-reach
template: lanes
title: Lockers with numbers and lockers without
intro: "The top row has numbered lockers. The bottom row is the same lockers with no numbers painted, so you can only count from the door. Nothing has been opened yet."
lanes:
  - { id: lockers, label: "Lockers with numbers, from 0", kind: row, slots: 8, index0: true }
  - { id: plain, label: "Same lockers, no numbers painted", kind: row, slots: 8 }
items:
  - { id: lk0, label: "Bag A", at: "lockers.0" }
  - { id: lk1, label: "Bag B", at: "lockers.1" }
  - { id: lk2, label: "Bag C", at: "lockers.2" }
  - { id: lk3, label: "Bag D", at: "lockers.3" }
  - { id: lk4, label: "Bag E", at: "lockers.4" }
  - { id: lk5, label: "Bag F", at: "lockers.5" }
  - { id: lk6, label: "Bag G", at: "lockers.6" }
  - { id: lk7, label: "Bag H", at: "lockers.7" }
  - { id: pl0, label: "Bag A", at: "plain.0" }
  - { id: pl1, label: "Bag B", at: "plain.1" }
  - { id: pl2, label: "Bag C", at: "plain.2" }
  - { id: pl3, label: "Bag D", at: "plain.3" }
  - { id: pl4, label: "Bag E", at: "plain.4" }
  - { id: pl5, label: "Bag F", at: "plain.5" }
  - { id: pl6, label: "Bag G", at: "plain.6" }
  - { id: pl7, label: "Bag H", at: "plain.7" }
vars: { numberedSteps: 0, countedSteps: 0 }
show:
  - { var: numberedSteps, label: "Steps with numbers" }
  - { var: countedSteps, label: "Steps counting from the door" }
actions:
  - id: open0
    label: "Go to locker 0 by number"
    do:
      - add: { numberedSteps: 1 }
      - light: [ lk0 ]
      - say: "Locker 0. You read the number and go straight there. That is 1 step."
  - id: open5
    label: "Go to locker 5 by number"
    do:
      - add: { numberedSteps: 1 }
      - light: [ lk5 ]
      - say: "Locker 5. You work out where it is and go straight there. That is 1 step."
  - id: open7
    label: "Go to locker 7 by number"
    do:
      - add: { numberedSteps: 1 }
      - light: [ lk7 ]
      - say: "Locker 7. You go straight there. That is still 1 step."
  - id: count0
    label: "Count to the 1st plain locker"
    do:
      - add: { countedSteps: 1 }
      - light: [ pl0 ]
      - say: "The 1st locker is at the door. That is 1 step."
  - id: count5
    label: "Count to the 6th plain locker"
    do:
      - add: { countedSteps: 6 }
      - light: [ pl0, pl1, pl2, pl3, pl4, pl5 ]
      - say: "With no numbers you pass 5 lockers and reach the 6th. That is 6 steps."
  - id: count7
    label: "Count to the 8th plain locker"
    do:
      - add: { countedSteps: 8 }
      - light: [ pl0, pl1, pl2, pl3, pl4, pl5, pl6, pl7 ]
      - say: "You pass 7 lockers and reach the 8th. That is 8 steps."
script: [ open0, open7, count0, count7, open5, count5 ]
try:
  - text: "Press \"Go to locker 0 by number\", then \"Go to locker 7 by number\". Compare the two steps."
    done: [ open0, open7 ]
  - text: "Press \"Count to the 1st plain locker\", then \"Count to the 8th plain locker\". Compare the two counts."
    done: [ count0, count7 ]
  - text: "(Optional) Press \"Go to locker 5 by number\" and \"Count to the 6th plain locker\" and compare."
    done: [ open5, count5 ]
    optional: true
```

**What you should notice:** Going to a numbered locker always costs 1 step, and counting from the door costs more the farther the locker is.

## Build the logic

### Notice

A corridor has 8 lockers, numbered 0 to 7, each 1 metre wide. Locker 0 starts at the door. Locker 5 starts 5 metres from the door. Locker 7 starts 7 metres from the door.

You did not pass any other locker to learn those numbers.

### Guess

How can the computer reach item 500 without going past the first 499?

- A. It goes past them very fast, so you do not notice.
- B. It works out the spot from the start of the row, the item number and the width of one spot.
- C. It keeps a second list that says where every item is.

Idea A is the belief behind the question, and it is not what happens. Idea C could work, but it needs extra memory, and the row does not need it. Idea B is the answer.

### Build

1. Put the items side by side with no gaps.
2. Make every spot the same width.
3. Remember one thing: where the row starts.
4. To find item number i, work out: start + i × width.
5. That is one multiplication and one addition, whether i is 1 or 500. So the time does not grow with the number of items.
6. Number the spots from 0. Item 0 is zero widths from the start.
7. The price: the row has a fixed length. You must know how many spots you need before you start.

### Name it

An **array** is a row of items of the same type, side by side. The number of a spot is its **index**, and the first index is 0. The Kotlin documentation says an array holds a fixed number of values and its elements are ordered and accessed by index.

Reading by index takes the same time for any index. In the Big O lesson this is O(1), constant time.

In Kotlin, `intArrayOf` makes an array of `Int` values. `scores[3]` reads the spot with index 3. `scores.size` is the number of spots, and `scores.lastIndex` is the last valid index. The documentation says `IntArray` stores values without boxing (it does not wrap each number in an object), while `Array<Int>` usually boxes them.

```kotlin
fun main() {
    val scores = intArrayOf(40, 55, 70, 85, 100)
    println(scores[0])
    println(scores[3])
    println(scores.size)
    println(scores.lastIndex)
}
```

This prints `40`, `85`, `5` and `4`. There are 5 spots, so the last index is 4.

Ask for an index outside the array and the Kotlin documentation says Kotlin throws `ArrayIndexOutOfBoundsException` at runtime.

## Predict the output

```challenge
type: predict
id: write-then-read-spot
verified: kotlin-2.4.21
code: |
  fun main() {
      val arr = IntArray(4)
      arr[0] = 5
      arr[2] = 9
      arr[2] = arr[0] + 1
      println(arr[2])
      println(arr[1])
  }
options:
  - id: a
    text: "9, then 0"
    why: "You may have read the first write to spot 2. The third line writes to spot 2 again, so the 9 is gone."
  - id: b
    text: "6, then 0"
    correct: true
    why: "Spot 2 first held 9, then took the value of spot 0 plus 1, which is 6. Spot 1 was never written, so it keeps the starting 0 that IntArray(4) gives."
  - id: c
    text: "6, then 5"
    why: "You may have counted the spots from 1. Index 1 is the second locker, which was never filled. Index 0 holds the 5."
output: |
  6
  0
```

```challenge
type: predict
id: size-and-last-index
verified: kotlin-2.4.21
code: |
  fun main() {
      val nums = intArrayOf(10, 20, 30, 40)
      println(nums.size)
      println(nums.lastIndex)
      println(nums[nums.lastIndex])
  }
options:
  - id: a
    text: "4, 4, 40"
    why: "You may have used the number of spots as the last index. The numbering starts at 0, so the last index is one less."
  - id: b
    text: "3, 3, 30"
    why: "You may have counted the lockers by their last number. size counts the lockers, so it prints 4."
  - id: c
    text: "4, 3, 40"
    correct: true
    why: "There are 4 lockers, numbered 0 to 3. The last one holds 40."
output: |
  4
  3
  40
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `40`, `85`, `5` and `4`, each on its own line.
3. **Break.** Change `scores[3]` to `scores[5]`. Run it. It prints `40` and then stops with an `ArrayIndexOutOfBoundsException`, because the array has no locker 5.
4. **Fix.** Change `scores[5]` to `scores[scores.lastIndex]`. Run it. You should see `40`, `100`, `5` and `4`.

## Fix the bug

```challenge
type: fix
id: fix-loop-one-too-far
verified: kotlin-2.4.21
code: |
  fun totalOf(readings: IntArray): Int {
      var total = 0
      for (i in 0..readings.size) {
          total += readings[i]
      }
      return total
  }

  fun main() {
      val temperatures = intArrayOf(21, 24, 19, 25, 23)
      val total = totalOf(temperatures)
      println("Readings: ${temperatures.size}")
      println("Total: $total")
  }
hints:
  - "Look at the for line. What is the last number the loop asks for, and is there a locker with that number?"
  - "Five lockers are numbered 0 to 4. The range 0..size goes one step past the last locker."
expected: |
  Readings: 5
  Total: 112
fix: |
  fun totalOf(readings: IntArray): Int {
      var total = 0
      for (i in 0 until readings.size) {
          total += readings[i]
      }
      return total
  }

  fun main() {
      val temperatures = intArrayOf(21, 24, 19, 25, 23)
      val total = totalOf(temperatures)
      println("Readings: ${temperatures.size}")
      println("Total: $total")
  }
explanation: "The range 0..size includes the number of lockers as an index, but the last locker is size minus 1. until stops one before the end."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a small answer: no null values are in play. Ask it anyway. Spend your time on question 2, and on how many times the loop body runs.

## Tricks to remember

- The number is how many widths you are from the start. So the first is 0.
- Address = start + index × width. One calculation, any index.
- size is one more than lastIndex.
- Reading by number is a jump. Looking for a value may still be a walk.

## Common mistakes

- **Thinking the computer steps over the first 499 items.** A corridor makes this feel right. It works out the spot from the start and the width instead.
- **Using size as the last index.** Counting items from 1 feels natural. The last index is size minus 1, or `lastIndex`.
- **Treating `Array<Int>` and `IntArray` as the same.** Both look like rows of numbers. The Kotlin documentation says `IntArray` avoids boxing, while `Array<Int>` usually boxes each number.
- **Thinking a fast read means a fast search.** Reading item 500 is one jump. Finding which spot holds the value 77 may mean looking at every spot.

## Where this shows up in Android

Picture a list screen with 10,000 rows. The Compose `LazyColumn` is Android's scrolling list. Its `items(count)` call takes a block of code that receives the index of the row to draw. The screen draws only the rows you can see, and for each one it asks for the item at an index. (general knowledge)

This part is my own reasoning. If reading by index got slower for higher numbers, rows near the bottom would load more slowly than rows at the top. Because it does not, scrolling far down feels the same as scrolling near the top. The Java SE documentation says `ArrayList.get` runs in constant time, and an `ArrayList` is a common list behind such screens (my own reasoning).

The matching bug is asking for position 1,000 in a list of 1,000 items. The last position is 999, so the screen asks for a spot that does not exist and the app closes with an index error (my own reasoning).

A photo is also one long row. The Compose `ImageBitmap.readPixels` call copies pixels into an `IntArray`, with each value holding the ARGB colour of one pixel. Pixel number k is one jump away. (my own reasoning)

## Say it in an interview

**How does the CPU find `arr[500]` in one step?**
It takes the address where the array starts, adds 500 times the size of one item, and reads from there. That is one multiplication and one addition, so the cost does not depend on the index. This is general knowledge, simplified.

**What is the difference between `IntArray` and `Array<Int>` in Kotlin?**
The Kotlin documentation says `IntArray` stores values without boxing and maps to `int[]`, while `Array<Int>` usually boxes the values into objects. On the JVM, my understanding is that `Array<Int>` holds references to separate objects (general knowledge). So `IntArray` uses less memory and is friendlier to the CPU.

**Why must every item in the array be the same size?**
So that start + index × size lands exactly on the start of an item. If sizes differed, you would have to walk past the earlier items to find where the 500th begins. For arrays of objects, the array holds same-size references, and the objects live elsewhere (general knowledge).

**Why is reading a 2D array row by row usually faster than column by column?**
The items of one row sit next to each other in memory. The CPU fetches memory in small blocks, so after reading one item the next ones are already close (general knowledge). Going down a column jumps between rows. My own reasoning: the gap shows up with large arrays and is hard to see with small ones.

## Recap

1. An array keeps same-size items side by side, numbered from 0.
2. The computer works out an item's spot as start plus index times size.
3. So reading any index takes the same time, but the length is fixed.

## You can now

- Predict what a short program prints after it writes into and reads from array spots.
- Explain why reading item 500 costs the same as reading item 1.
- Fix a loop that reads one spot past the end of an array.

## Practise on these

Each problem reads or writes array spots by index. Say which index each step touches before you code. The list runs from the gentlest to the hardest.

1. LeetCode 66, Plus One (Easy). Walk the digits from the last index backwards and handle the carry. https://leetcode.com/problems/plus-one/
2. LeetCode 121, Best Time to Buy and Sell Stock (Easy). One pass over the array, reading each spot in order. https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
3. LeetCode 189, Rotate Array (Medium). Move items to new indexes in place, using three reversals. https://leetcode.com/problems/rotate-array/
4. LeetCode 54, Spiral Matrix (Medium). Keep every index inside the edges of a 2D array. https://leetcode.com/problems/spiral-matrix/
5. LeetCode 48, Rotate Image (Medium). Map each row and column to its new place without leaving the array. https://leetcode.com/problems/rotate-image/
