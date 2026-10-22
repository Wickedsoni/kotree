---
id: dsa/arrays/how-arraylist-grows
title: How can a list keep growing when its array has a fixed size?
status: live
difficulty: medium
order: 7
requires: [dsa/arrays/insert-and-delete-in-an-array]
summary: When its array is full, a list builds a bigger one and copies everything across. Moves are rare, so adding stays quick.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [moving-day-flats]
playground: true
fruit: false
upnext: false
---

## The question

A feed app builds a list of 100,000 rows by calling add again and again. The memory under a list is a fixed number of spots, so it cannot stretch. How can the list keep growing? And why does add stay quick even when the list is huge?

## Mental model

Think of a family that keeps moving to a bigger flat.

- The flat is the fixed block of memory under the list.
- The rooms are its spots.
- The furniture is the items.
- Moving day is copying every item into the new place.
- The bigger flat is a new, larger block, and the old flat is given up.

While there is a free room, a new piece of furniture goes straight in. When every room is full, the family rents a bigger flat, carries every piece over, then brings in the new piece. Moving day is costly. But after a move to a flat with twice the rooms, there are many free rooms, so the next moves are far apart.

**Where the analogy stops:** A family picks a flat it likes. A list follows a rule, and the Java SE documentation does not say what that rule is. This lesson doubles the size because the sums are clean. A real list may grow by a different amount. Also, the old flat is not kept. The computer stops using it.

## See it move

```sim
id: moving-day-flats
template: lanes
title: Furniture, a small flat and a big flat
intro: "Eight pieces of furniture wait outside. A small flat has 4 rooms and a big flat has 8. Nothing is inside yet and nothing has been carried."
lanes:
  - { id: outside, label: "Furniture waiting outside", kind: row, slots: 8, index0: true }
  - { id: small, label: "Small flat, 4 rooms", kind: row, slots: 4, index0: true }
  - { id: big, label: "Big flat, 8 rooms", kind: row, slots: 8, index0: true }
items:
  - { id: f1, label: "Piece 1", at: "outside.0" }
  - { id: f2, label: "Piece 2", at: "outside.1" }
  - { id: f3, label: "Piece 3", at: "outside.2" }
  - { id: f4, label: "Piece 4", at: "outside.3" }
  - { id: f5, label: "Piece 5", at: "outside.4" }
  - { id: f6, label: "Piece 6", at: "outside.5" }
  - { id: f7, label: "Piece 7", at: "outside.6" }
  - { id: f8, label: "Piece 8", at: "outside.7" }
vars: { size: 0, copies: 0 }
show:
  - { var: size, label: "Pieces inside" }
  - { var: copies, label: "Pieces carried on moving day" }
actions:
  - id: bring
    label: "Bring in the next piece"
    cases:
      - when: { var: { size: 0 } }
        do:
          - push: { small: f1 }
          - add: { size: 1 }
          - say: "Piece 1 goes into the small flat. There was a free room, so nothing is carried."
      - when: { var: { size: 1 } }
        do:
          - push: { small: f2 }
          - add: { size: 1 }
          - say: "Piece 2 goes into the small flat. A free room, so nothing is carried."
      - when: { var: { size: 2 } }
        do:
          - push: { small: f3 }
          - add: { size: 1 }
          - say: "Piece 3 goes into the small flat. A free room, so nothing is carried."
      - when: { var: { size: 3 } }
        do:
          - push: { small: f4 }
          - add: { size: 1 }
          - say: "Piece 4 fills the last room of the small flat. Nothing is carried yet."
      - when: { var: { size: 4 } }
        do:
          - move: { f1: "big.0", f2: "big.1", f3: "big.2", f4: "big.3" }
          - push: { big: f5 }
          - add: { size: 1, copies: 4 }
          - say: "The small flat is full. The family moves to the big flat, carries all 4 pieces over, then brings in piece 5. Pieces carried so far: {copies}."
      - when: { var: { size: 5 } }
        do:
          - push: { big: f6 }
          - add: { size: 1 }
          - say: "Piece 6 goes into the big flat. A free room, so nothing is carried. Pieces carried so far: {copies}."
      - when: { var: { size: 6 } }
        do:
          - push: { big: f7 }
          - add: { size: 1 }
          - say: "Piece 7 goes into the big flat. Nothing is carried. Pieces carried so far: {copies}."
      - when: { var: { size: 7 } }
        do:
          - push: { big: f8 }
          - add: { size: 1 }
          - say: "Piece 8 fills the big flat. Nothing is carried. Pieces carried in total: {copies}."
    refuse: "All 8 pieces are inside and the big flat is full. The next piece would mean another move to a flat with 16 rooms."
script: [ bring, bring, bring, bring, bring, bring, bring, bring ]
try:
  - text: "Press \"Bring in the next piece\" four times. Watch the carried counter stay at 0."
    done: [ bring, bring, bring, bring ]
  - text: "Press it once more. The small flat is full, so read what the family has to do."
    done: [ bring ]
  - text: "Press it three more times until all 8 pieces are inside. Read the carried counter."
    done: [ bring, bring, bring ]
```

**What you should notice:** Only the fifth piece triggers a move, and the other seven pieces are brought in without carrying anything.

## Build the logic

### Notice

A flat has 4 rooms, and 4 pieces of furniture fill it. A fifth piece arrives. There is no free room, and you cannot push the walls out.

### Guess

What can the family do?

- A. Throw out the last piece to make room.
- B. Rent a bigger flat, carry all 4 pieces over, then add the fifth.
- C. Stack the fifth piece on top of the fourth.

Idea A loses furniture, and a list must not lose items. Idea C breaks the rule that one room holds one piece. Idea B is the answer.

Next question. How big should the new flat be?

- A. One room bigger, with space for the new piece only.
- B. Twice as big.

With idea A, the next piece fills the flat again and the family moves again, carrying everything. Every add would be a moving day. Idea B leaves free rooms, so the next moves come later and later.

### Build

1. If a room is free, put the new piece in it. That is 1 step.
2. If every room is full, rent a flat with twice the rooms.
3. Carry every piece into the new flat. Then put the new piece in.
4. Give up the old flat.
5. Count the cost. Starting from 1 room and adding 8 pieces, the family carries 1, then 2, then 4, which is 7 pieces in total.
6. Growing by one room each time carries 1, 2, 3 up to 7, which is 28. For 1,000 pieces, growing by one room carries 499,500, while doubling carries about 1,000.
7. Spread the carrying over all the adds. With doubling, it comes to about one carried piece per add, whatever the size. That is why add stays quick on average.

### Name it

The number of rooms is the **capacity**. The number of pieces inside is the **size**. The Java SE documentation says an `ArrayList` has a capacity, which is the size of the array used to store its elements. The capacity is always at least the list size. It also says the details of the growth policy are not specified, beyond the fact that adding an element has constant amortized time cost.

**Amortized** means the cost is averaged over many adds. A few adds are costly, most are cheap, and the average is small. The same page says that adding n elements requires O(n) time.

In Kotlin on the JVM, `ArrayList` is the Java class (it is a typealias, a second name for it). The code below is a tiny hand-made version that doubles. It starts with room for 2.

```kotlin
class GrowingList {
    private var items = IntArray(2)
    var size = 0
        private set
    var copies = 0
        private set

    fun add(value: Int) {
        if (size == items.size) {
            items = items.copyOf(items.size * 2)
            copies += size
        }
        items[size] = value
        size++
    }
}

fun main() {
    val list = GrowingList()
    for (n in 1..8) {
        list.add(n)
    }
    println("Size: ${list.size}")
    println("Items copied in total: ${list.copies}")
}
```

Here `copyOf(n)` makes a new array of length n with the old items copied in; the new spots start as 0. `private set` means only the class itself can change `size` and `copies`.

This prints `Size: 8` and `Items copied in total: 6`. The moves happen when the list holds 2 items and when it holds 4. They copy 2 and then 4.

The Java SE documentation says an application can raise the capacity before adding many elements with `ensureCapacity`, which may reduce the amount of incremental reallocation. Kotlin's `ArrayList(initialCapacity)` also takes a starting capacity. These facts are for the JVM. The Kotlin page says `ensureCapacity` does nothing on JS.

## Predict the output

```challenge
type: predict
id: copies-for-eight-adds
verified: kotlin-2.4.21
code: |
  fun main() {
      var capacity = 1
      var size = 0
      var copies = 0
      repeat(8) {
          if (size == capacity) { copies += size; capacity *= 2 }
          size++
      }
      println("$copies $capacity")
  }
options:
  - id: a
    text: "8 8"
    why: "You may have counted one copy per add. Only an add that finds a full flat carries anything."
  - id: b
    text: "7 8"
    correct: true
    why: "The flat fills at 1, 2 and 4 pieces. Those three moves carry 1, then 2, then 4 pieces, which is 7. The final flat has 8 rooms."
  - id: c
    text: "4 8"
    why: "You may have counted only the last move. Each move carries every piece that is inside."
output: |
  7 8
```

```challenge
type: predict
id: copies-when-growing-by-one
verified: kotlin-2.4.21
code: |
  fun main() {
      var copies = 0
      var size = 0
      repeat(5) {
          // The flat grows by one room each time, so every add carries all pieces inside.
          copies += size
          size++
      }
      println(copies)
  }
options:
  - id: a
    text: "10"
    correct: true
    why: "The five adds carry 0, 1, 2, 3 and 4 pieces. That is 10, already more than the 5 adds. Doubling would carry less."
  - id: b
    text: "5"
    why: "You may have counted one carried piece per add. Each add carries everything already inside."
  - id: c
    text: "15"
    why: "You may have counted 1 + 2 + 3 + 4 + 5. The first add finds an empty flat, so it carries nothing."
output: |
  10
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `Size: 8` and `Items copied in total: 6`.
3. **Break.** Change `items.size * 2` to `items.size + 1`. Run it again. The list still holds 8 items, but the copies jump from 6 to 27, because the list now moves on almost every add.
4. **Fix.** Change `items.size + 1` back to `items.size * 2`, then change `IntArray(2)` to `IntArray(8)`. Run it. You should see `Size: 8` and `Items copied in total: 0`. The list started with room for all 8, so it never had to move.

## Fix the bug

```challenge
type: fix
id: fix-plus-in-a-loop
generated: true
verified: kotlin-2.4.21
code: |
  fun buildRows(count: Int): List<String> {
      var rows = listOf<String>()
      var newLists = 0
      for (i in 1..count) {
          val before = rows
          rows += "row-$i"
          if (rows !== before) {
              newLists++
          }
      }
      println("New lists built: $newLists")
      return rows
  }

  fun main() {
      val rows = buildRows(1000)
      println("Rows: ${rows.size}")
  }
hints:
  - "Look at the line with +=. Does it change the list in place, or does the variable end up pointing at a new list?"
  - "On a var that holds a read-only List, += builds a new list each time. A MutableList held in a val changes in place."
expected: |
  New lists built: 0
  Rows: 1000
fix: |
  fun buildRows(count: Int): List<String> {
      val rows = mutableListOf<String>()
      var newLists = 0
      for (i in 1..count) {
          val before = rows
          rows += "row-$i"
          if (rows !== before) {
              newLists++
          }
      }
      println("New lists built: $newLists")
      return rows
  }

  fun main() {
      val rows = buildRows(1000)
      println("Rows: ${rows.size}")
  }
explanation: "The Kotlin documentation says += on a var read-only list builds a new list and points the variable at it. On a MutableList in a val, += changes the list in place, so no new list is built."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a small answer: no null values are in play. Ask it anyway. Question 4 matters most: the `+=` line runs 1,000 times, so what does each run build?

## Tricks to remember

- Full flat? Rent a bigger one, carry everything, then add.
- Grow by a factor, not by a fixed few rooms.
- Moving is costly, but moves come further apart.
- Know the size in advance? Ask for the rooms at the start.

## Common mistakes

- **Thinking add costs nothing whatever the size.** Most adds are cheap, so it feels free. The add that finds a full array copies every item. The average is small, but one add can be slow.
- **Using `list += item` on a `var` read-only list inside a loop.** It reads like adding. The Kotlin documentation says it builds a new list each time, so a loop of n adds copies about n times n items. Use a `MutableList` instead.
- **Growing by a fixed number of rooms.** Adding 10 rooms each time sounds sensible. The moves stay frequent and each one carries more, so n adds cost about n times n. Doubling spaces the moves out.
- **Thinking removing items shrinks the memory.** The Java page I read does not say that removing shrinks the capacity. The only way it names to shrink the capacity is `trimToSize`.

## Where this shows up in Android

Picture a screen that builds 10,000 rows in a loop before it shows them. If the code uses `rows += row` on a `var` that holds a read-only list, each turn builds a new list and copies every row so far. This part is my own reasoning: on the thread that draws the screen, that wait can push the first frame past the 16 ms budget from the Android slow-rendering page. The symptom is a screen that opens late or stutters when a user has a big feed.

Two cures follow from this lesson. Use a `MutableList`, which changes in place. And when you know about how many rows you will build, give the `ArrayList` a starting capacity.

## Say it in an interview

**If an `ArrayList` doubles its array whenever it fills, why is `add` still O(1) amortized?**
Each doubling copies all the items, but doublings come further apart. For n adds the copies add up to less than 2n, so the average is a few steps per add. The Java SE documentation promises amortized constant time and says adding n elements takes O(n) time. It does not promise doubling, so I would say "grows by a factor" (my own reasoning).

**Why grow by a factor and not by a fixed amount like 100 slots?**
With a fixed amount, the number of moves grows in step with n, and each move copies up to n items. That makes n adds cost about n times n. With a factor, the moves become rarer as the list grows. General knowledge.

**When should you give `ArrayList` an initial capacity?**
When you know roughly how many items are coming. The Java SE documentation says `ensureCapacity` may reduce incremental reallocation. The trade-off is memory: if you ask for far more than you use, the extra rooms sit empty. I would measure before tuning (my own reasoning).

**Does removing items shrink an `ArrayList`'s memory?**
The Java page I read does not say that it does. It names `trimToSize` as the way to trim the capacity to the current size. So after a big list is emptied, the memory can stay until you trim or drop the list.

## Recap

1. A list keeps its items in a fixed array and builds a bigger one when it is full.
2. Each move copies every item, but growing by a factor makes moves rare.
3. So add is cheap on average, and a known size lets you skip the moves.

## You can now

- Predict how many items are copied while a doubling list takes eight adds.
- Explain why growing by a factor beats growing by a fixed amount.
- Fix a loop that uses `+=` on a read-only list so it stops building new lists.

## Practise on these

Each problem grows or fills a list. Say how many times it copies before you code. The list runs from the gentlest to the hardest.

1. LeetCode 1929, Concatenation of Array (Easy). Build a new array twice the size and copy into it. https://leetcode.com/problems/concatenation-of-array/
2. LeetCode 118, Pascal's Triangle (Easy). Grow a list of rows, each with a size you know in advance. https://leetcode.com/problems/pascals-triangle/
3. HackerRank Dynamic Array (Easy), in Data Structures, Arrays. Append to lists that grow as queries arrive. https://www.hackerrank.com/challenges/dynamic-array/problem
4. LeetCode 443, String Compression (Medium). Compare appending to a growing buffer with packing the array in place. https://leetcode.com/problems/string-compression/
5. LeetCode 380, Insert Delete GetRandom O(1) (Medium). Pair a growing list with a map so add and delete stay fast. https://leetcode.com/problems/insert-delete-getrandom-o1/
