---
id: dsa/arrays/insert-and-delete-in-an-array
title: Why is removing the first item of a big list slow?
status: live
difficulty: easy
order: 6
requires: [dsa/arrays/arrays-in-memory]
summary: Items in an array sit side by side with no gaps. Removing the first one makes every item behind it move one spot.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [cinema-row-shuffle]
playground: true
fruit: false
upnext: false
---

## The question

A chat app keeps its messages in a list. Each time a new message arrives, the code removes the oldest one from the top. With 20 messages nobody notices. With a very big list the screen starts to lag, even though only one message is removed each time. Why does removing the first item cost so much more than removing the last?

## Mental model

Think of one row of cinema seats, numbered from 0.

- The row is the array. The seats are its spots.
- A person in a seat is an item.
- The rule of this cinema is that people sit together from seat 0, with no empty seats in the middle.
- When someone leaves, the people behind move down so the row stays packed.

If the person in the last seat leaves, nobody else has to move. If the person in seat 0 leaves, everyone behind them moves one seat. If someone new wants seat 0, everyone moves one seat the other way to make room.

**Where the analogy stops:** Real cinema-goers would leave a gap. An array does not allow gaps, because reading by index needs the items packed. The computer also moves its items in one quick sweep, not one person at a time, but it still touches every one.

## See it move

```sim
id: cinema-row-shuffle
template: lanes
title: A packed row where someone leaves
intro: "Five people sit in seats 0 to 4 of one packed row. Nobody has left yet and no seat has moved."
lanes:
  - { id: row, label: "Cinema row, seats from 0", kind: row, slots: 5, index0: true }
items:
  - { id: ana, label: "Ana", at: "row.0" }
  - { id: ben, label: "Ben", at: "row.1" }
  - { id: cy, label: "Cy", at: "row.2" }
  - { id: di, label: "Di", at: "row.3" }
  - { id: eli, label: "Eli", at: "row.4" }
vars: { moves: 0, firstGone: 0, lastGone: 0 }
show:
  - { var: moves, label: "Seats moved so far" }
actions:
  - id: leave-last
    label: "Last person leaves"
    cases:
      - when: { var: { lastGone: 0 } }
        do:
          - remove: eli
          - set: { lastGone: 1 }
          - say: "Eli leaves the last seat. Nobody behind Eli has to move. Seats moved so far: {moves}."
    refuse: "The last person has already left. Press Reset to start again."
  - id: leave-first
    label: "First person leaves"
    cases:
      - when: { var: { firstGone: 0, lastGone: 0 } }
        do:
          - remove: ana
          - move: { ben: "row.0", cy: "row.1", di: "row.2", eli: "row.3" }
          - add: { moves: 4 }
          - set: { firstGone: 1 }
          - say: "Ana leaves seat 0. Ben, Cy, Di and Eli each move down one seat to keep the row packed. Seats moved so far: {moves}."
      - when: { var: { firstGone: 0, lastGone: 1 } }
        do:
          - remove: ana
          - move: { ben: "row.0", cy: "row.1", di: "row.2" }
          - add: { moves: 3 }
          - set: { firstGone: 1 }
          - say: "Ana leaves seat 0. Ben, Cy and Di each move down one seat. Seats moved so far: {moves}."
    refuse: "The first person has already left. Press Reset to start again."
script: [ leave-last, leave-first ]
try:
  - text: "Press \"Last person leaves\". Read the counter of seats moved."
    done: [ leave-last ]
  - text: "Press \"First person leaves\". Read the counter again and see how many people had to slide down."
    done: [ leave-first ]
```

**What you should notice:** The last person leaving moves nobody, and the first person leaving moves everyone behind.

## Build the logic

### Notice

Five people sit in seats 0 to 4: Ana, Ben, Cy, Di and Eli. Ana leaves. Seat 0 is empty, but the rule says the row must stay packed from seat 0. So Ben moves to seat 0, Cy to seat 1, Di to seat 2 and Eli to seat 3. That is 4 moves.

Now try Eli leaving instead. Nobody sits behind Eli. That is 0 moves.

### Guess

A row has 100,000 people. The person in seat 0 leaves. How many people move?

- A. None. A gap stays at the front.
- B. 99,999, everyone who sat behind.
- C. 1, to fill the gap.

Idea A breaks the rule that the row stays packed. Idea C thinks only the next person steps in, and then the gap would move one seat back. Idea B is the answer.

What if the person in the middle seat leaves? About 50,000 people sit behind and move.

### Build

1. To remove the item at seat i, every item behind it moves one seat down. That is about n minus i minus 1 moves.
2. Removing the last item has 0 moves.
3. To add an item at seat i, every item from seat i onward moves one seat up. That is about n minus i moves. The row also needs a free seat at the end.
4. Adding at the end has 0 moves.
5. So the front is the worst place and the end is the best place.
6. In the Big O words, the front and the middle cost O(n). The end costs O(1).

### Name it

Taking an item out is a **delete**, and putting an item in at a chosen spot is an **insert**. The items that slide to close or open the gap are **shifted**.

In Kotlin, `removeAt(index)` deletes the item at that index. The Kotlin documentation says all elements placed after the index are shifted 1 position left. `add(index, element)` inserts. The Java SE documentation for `ArrayList` says it shifts the element currently at that position, and any subsequent elements, to the right. In Kotlin on the JVM, `ArrayList` is that same Java class.

```kotlin
fun main() {
    val row = arrayListOf("A", "B", "C", "D", "E")
    row.removeAt(row.lastIndex)
    println(row)
    row.removeAt(0)
    println(row)
    row.add(0, "Z")
    println(row)
}
```

This prints `[A, B, C, D]`, then `[B, C, D]`, then `[Z, B, C, D]`. The first line moved nobody. The second and third each slid three items.

## Predict the output

```challenge
type: predict
id: remove-first-of-four
verified: kotlin-2.4.21
code: |
  fun main() {
      val seats = arrayListOf("Ana", "Ben", "Cy", "Di")
      seats.removeAt(0)
      println(seats)
      println(seats[0])
  }
options:
  - id: a
    text: "[Ana, Ben, Cy], then Ana"
    why: "You may have removed the last seat. removeAt(0) takes seat 0, the first one."
  - id: b
    text: "[Ben, Cy, Di], then Ben"
    correct: true
    why: "Ana leaves seat 0 and everyone behind slides down one seat. Seat 0 now holds Ben."
  - id: c
    text: "[Ben, Cy, Di], then Ana"
    why: "You may have thought Ana's name stays on seat 0. The row stays packed, so Ben moves into seat 0."
output: |
  [Ben, Cy, Di]
  Ben
```

```challenge
type: predict
id: insert-in-the-middle
verified: kotlin-2.4.21
code: |
  fun main() {
      val seats = arrayListOf("Ana", "Ben", "Cy")
      seats.add(1, "Zed")
      println(seats)
      seats.removeAt(seats.lastIndex)
      println(seats)
  }
options:
  - id: a
    text: "[Ana, Ben, Zed, Cy], then [Ana, Ben, Zed]"
    why: "You may have put Zed after seat 1. add(1, ...) puts Zed in seat 1, and Ben moves up to seat 2."
  - id: b
    text: "[Zed, Ana, Ben, Cy], then [Zed, Ana, Ben]"
    why: "You may have thought add puts the new item first. The 1 is a seat number, and seat 0 stays with Ana."
  - id: c
    text: "[Ana, Zed, Ben, Cy], then [Ana, Zed, Ben]"
    correct: true
    why: "Zed sits in seat 1, and Ben and Cy shift up one seat. Then the last seat, which is Cy's, is emptied with no shifting."
output: |
  [Ana, Zed, Ben, Cy]
  [Ana, Zed, Ben]
```

```challenge
type: predict
id: count-the-moves
verified: kotlin-2.4.21
code: |
  fun main() {
      val seats = arrayListOf(1, 2, 3, 4, 5, 6)
      val removeIndex = 1
      val moves = seats.size - 1 - removeIndex
      seats.removeAt(removeIndex)
      println("$moves ${seats.size}")
  }
options:
  - id: a
    text: "4 5"
    correct: true
    why: "Six people, and seat 1 leaves. The 4 people behind seat 1 each move one seat, and 5 people are left."
  - id: b
    text: "5 5"
    why: "You may have counted everyone but the one who left. The person in seat 0 sits before seat 1 and does not move."
  - id: c
    text: "1 5"
    why: "You may have counted only the person who fills the gap. Everyone behind the gap moves, not just the next one."
output: |
  4 5
```

## Try it

1. **Write.** Copy this program into the editor. It counts how many people move while the row is emptied from the front, and from the back.

   ```kotlin
   fun countMoves(removeFront: Boolean): Int {
       val line = MutableList(1000) { it }
       var moved = 0
       while (line.isNotEmpty()) {
           val index = if (removeFront) 0 else line.lastIndex
           moved += line.size - 1 - index
           line.removeAt(index)
       }
       return moved
   }

   fun main() {
       println("Front: ${countMoves(true)}")
       println("Back: ${countMoves(false)}")
   }
   ```
2. **Run.** You should see `Front: 499500` and `Back: 0`. Emptying from the front moved people 499,500 times. Emptying from the back moved nobody.
3. **Break.** Change `1000` to `2000`. Run it again. Nothing crashes, but the front number jumps to `1999000`, about four times bigger, while the back number stays `0`.
4. **Fix.** Put `1000` back and run it. The numbers return to `499500` and `0`. The lesson: if the order allows it, take items from the end.

## Fix the bug

```challenge
type: fix
id: fix-remove-first-in-loop
generated: true
verified: kotlin-2.4.21
code: |
  class TaskRunner(private val tasks: MutableList<String>) {
      var done = 0
      var shifted = 0

      fun runAll() {
          while (tasks.isNotEmpty()) {
              val task = tasks.removeAt(0)
              shifted += tasks.size
              if (task.isNotEmpty()) {
                  done++
              }
          }
      }
  }

  fun main() {
      val tasks = MutableList(1000) { "task-$it" }
      val runner = TaskRunner(tasks)
      runner.runAll()
      println("Done: ${runner.done}")
      println("Shifted: ${runner.shifted}")
  }
hints:
  - "Look at the line with removeAt(0). Each time it runs, what happens to all the items behind it?"
  - "You do not need to take tasks out one at a time. Walk through the list in order, then clear it once at the end."
expected: |
  Done: 1000
  Shifted: 0
fix: |
  class TaskRunner(private val tasks: MutableList<String>) {
      var done = 0
      var shifted = 0

      fun runAll() {
          for (task in tasks) {
              if (task.isNotEmpty()) {
                  done++
              }
          }
          tasks.clear()
      }
  }

  fun main() {
      val tasks = MutableList(1000) { "task-$it" }
      val runner = TaskRunner(tasks)
      runner.runAll()
      println("Done: ${runner.done}")
      println("Shifted: ${runner.shifted}")
  }
explanation: "removeAt(0) makes every item behind it move down a seat, on every turn of the loop. Walking the list in order keeps the first-in, first-out order and moves nothing."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a small answer: no null values are in play. Ask it anyway. Question 4 matters most: what does the loop repeat, and what does each repeat cost?

## Tricks to remember

- Front costs a shuffle, back costs nothing.
- Every seat behind the gap moves.
- A loop that removes from the front is a loop inside a loop in disguise.
- If order does not matter, swap the item with the last one, then remove the last.

## Common mistakes

- **Thinking removal leaves a gap and costs nothing.** In a real room a gap is free. In an array the items must stay packed so reading by index still works, so the items behind must move.
- **Calling `removeAt(0)` inside a loop over a big list.** One call looks cheap. The loop repeats it, so the shuffling happens again for every turn, which adds up to roughly n times n moves.
- **Thinking insert and delete cost the same everywhere.** The cost depends on where. Front and middle shift items, and the end does not.

## Where this shows up in Android

Picture a chat screen that keeps its messages in a list and removes the oldest message from the top each time a new one arrives. With a big list, each new message shifts every other message down by one spot.

The Android page on slow rendering says an app must render each frame in under 16 ms for 60 frames per second, and that slow rendering makes the system skip frames, which the user sees as stutter. This part is my own reasoning: if that shifting runs on the thread that draws the screen, a large list can push a frame past 16 ms. The symptom is a chat that scrolls smoothly in testing and lags in a busy room. Taking from the end avoids the shuffle.

## Say it in an interview

**Why does `removeAt(0)` on an `ArrayList` of 100,000 items take O(n), while removing the last item takes O(1)?**
The list keeps its items packed in an array. Removing seat 0 shifts all the items behind it down one place, which is about n moves. The Kotlin documentation says all elements after the index are shifted 1 position left. Removing the last item has nothing behind it, so nothing moves.

**If order does not matter, how can you delete the item at index i in O(1)?**
Copy the last item into index i, then remove the last item. The last removal moves nothing, so the whole delete is constant time. The price is that the order changes (general knowledge).

**Why is `removeAll { ... }` usually faster than calling `removeAt(i)` in a loop?**
`removeAt` in a loop can shift the tail many times, which is roughly n times n work. A single `removeAll` can do the job in one pass. The Kotlin docs say what it does but do not promise the cost, so this is my own reasoning about how a pass-based removal works.

**Why is it best to merge into an array with spare room at the end from right to left?**
If you write from the left, you overwrite items that you have not read yet. Writing from the back into the free space never touches unread items, so nothing needs to be shifted first. General knowledge.

## Recap

1. An array stays packed, so a delete or insert shifts the items behind it.
2. Removing the first item moves about n items, and removing the last moves none.
3. A loop that removes from the front repeats that shuffling on every turn.

## You can now

- Predict what a list holds after `removeAt` and `add(index, ...)`.
- Explain why removing from the front costs O(n) and removing from the end costs O(1).
- Fix a loop that uses `removeAt(0)` on every turn.

## Practise on these

Each problem asks you to remove or insert items in an array. Count how many items move before you code. The list runs from the gentlest to the hardest.

1. LeetCode 27, Remove Element (Easy). Overwrite the unwanted items in one pass instead of shifting after every removal. https://leetcode.com/problems/remove-element/
2. LeetCode 26, Remove Duplicates from Sorted Array (Easy). Pack the unique items to the front with a reading spot and a writing spot. https://leetcode.com/problems/remove-duplicates-from-sorted-array/
3. LeetCode 88, Merge Sorted Array (Easy). Write from the back so unread items are not overwritten. https://leetcode.com/problems/merge-sorted-array/
4. HackerRank Left Rotation (Easy), in Data Structures, Arrays. See why shifting left one by one is slow. https://www.hackerrank.com/challenges/array-left-rotation/problem
5. LeetCode 80, Remove Duplicates from Sorted Array II (Medium). Pack the items to the front when up to two copies are allowed. https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
