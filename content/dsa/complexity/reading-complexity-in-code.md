---
id: dsa/complexity/reading-complexity-in-code
title: Which of these loops hides an O(n²) cost?
status: shadow
difficulty: medium
order: 3
requires: [dsa/complexity/big-o-notation]
summary: One loop can hide a second loop inside a single call. See how to spot it, count it and remove it.
video: ""
publishedOn:
updatedOn:
sims: [hidden-second-loop]
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips this comment at go-live):
Java SE 17 ArrayList class description (size, isEmpty, get, set constant time; add amortized constant; all other operations linear, roughly speaking): https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
Java SE 17 HashSet class description (constant time add, remove, contains, size, assuming the hash function disperses the elements): https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashSet.html
Kotlin MutableSet.add (true if the element has been added, false if already contained): https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-mutable-set/add.html
Android slow rendering (16 ms per frame, dropped frame, jank): https://developer.android.com/topic/performance/vitals/render
Practice data: docs/practice/practice-dsa.md, section for this leaf ([both] problems only, plus [A] 14 whose number and title are well known).
-->
## The question

Your app merges 2,000 items from the server into the saved list. With 20 test items it ran at once. On a real phone the screen freezes for a moment. The code has one loop, and one loop looks cheap. So where is the extra cost hiding, and how can you find it by reading the code?

## Mental model

Think of a school corridor.

- The corridor is your list. The students already inside stand in a line.
- A student walking in is one turn of your loop.
- Before a new student may join the line, they shake hands with every student already there, to check whether they have met. That check is the call hidden inside the loop.
- A sign-in board hangs by the door. Each student writes their name on it, and the next student can look for a name in one glance.
- Counting handshakes is counting steps.

You see one student walk in per turn. You do not see the handshakes unless you ask what each turn really does. With 20 students nobody notices. With 2,000 students the handshakes are about two million.

**Where the analogy stops:** A real student stops shaking hands after meeting someone they know. A list check stops at the first match too, so the pile-up is biggest when every newcomer is new. Also, a sign-in board is one glance only if the names are spread out well on it. A real lookup of that kind depends on a good spread of its keys (see Name it).

## See it move

```sim
id: hidden-second-loop
template: chart
title: Two ways to check a class as it walks in
intro: "A class of n students walks into a corridor. The plot shows n from 0 to 1000, and the buttons read the costs at 10, 100 and 1000 students."
axes: { x: "n (students)", y: "Steps" }
curves:
  - { id: board, label: "Sign-in board: one glance per student (n steps)", f: { poly: [0, 1, 0] } }
  - { id: hands, label: "Handshake check: n times (n minus 1), divided by 2", f: { poly: [0, -0.5, 0.5] } }
scale: { x: [0, 1000] }
vars: { x: 1 }
actions:
  - id: n10
    label: Set n to 10
    do:
      - set: { x: 10 }
      - say: "At n = {x}, the sign-in board costs {board.at} steps and the handshake check costs {hands.at}."
  - id: n100
    label: Set n to 100
    do:
      - set: { x: 100 }
      - say: "At n = {x}, the sign-in board costs {board.at} steps and the handshake check costs {hands.at}, about 100 times more than at 10 students."
  - id: n1000
    label: Set n to 1000
    do:
      - set: { x: 1000 }
      - say: "At n = {x}, the sign-in board costs {board.at} steps and the handshake check costs {hands.at}."
  - id: compare
    label: Compare with the sign-in board
    do:
      - say: "The sign-in board turns each round of handshakes into one glance, so at n = {x} the class costs {board.at} steps instead of {hands.at}."
script: [n10, n100, n1000, compare]
try:
  - text: "Press \"Set n to 10\", \"Set n to 100\" and \"Set n to 1000\". Each time, read both costs."
    done: [n10, n100, n1000]
  - text: "Press \"Compare with the sign-in board\" and read how many steps are saved."
    done: [compare]
```

**What you should notice:** When the class grows 10 times, the sign-in board cost grows 10 times, and the handshake cost grows about 100 times.

## Build the logic

### Notice

Five students walk in one at a time. Each one shakes hands with everyone already in the corridor, then joins the line.

| Student | Handshakes when they arrive |
|---|---|
| 1st | 0 |
| 2nd | 1 |
| 3rd | 2 |
| 4th | 3 |
| 5th | 4 |

That is 10 handshakes for 5 students. For 10 students it is 45. For 100 students it is 4,950. The line gets longer every turn, so every new check is longer than the one before.

### Guess

A class of 100 students walks in one at a time. About how many handshakes happen in total?

- A. About 100, one per student.
- B. About 5,000, because each student meets everyone already there.
- C. About 200, two per student.

Idea A counts the turns you can see and forgets what each turn does. Idea C has no reason behind it. Idea B follows the story: the line grows, so each check is longer than the last.

### Build

1. Find the loop you can see. Count its turns. Call that number n.
2. Look at every call inside it. Ask: does this call itself walk through a whole collection?
3. If it does, that call is a second loop, written in one line. Estimate how far it walks. It usually walks as far as the collection is long.
4. Multiply. n turns, each walking about n, is about n times n steps.
5. If the call is a single glance, like the sign-in board, each turn costs 1. Then n turns cost about n.
6. Ask when the walk stops early. A walk that ends at the first match is cheap when matches are common. The worst case is the newcomer nobody has met.

### Name it

A **hidden loop** is a call that walks through a collection while you only see one line. A loop that holds a hidden loop costs about n times n steps. That growth is **quadratic**, written O(n²) and said "order n squared".

Here are four calls to check in any loop.

| Call | What it does | Cost for n items |
|---|---|---|
| `list.contains(x)` | Walks the list until it finds x | Up to about n |
| `list.indexOf(x)` | Walks the list until it finds x | Up to about n |
| `list.remove(x)` | Finds x first, then closes the gap | About n |
| `set.contains(x)` or `set.add(x)` on a `HashSet` | Jumps to where x would be | About 1 on average |

The Java documentation for `ArrayList` says that `size`, `isEmpty`, `get` and `set` run in constant time, which means about one step however long the list is, and that all other operations run in linear time, which means about n steps for n items, roughly speaking, except that `add` takes amortized constant time, which means one step on average over many adds. `contains`, `indexOf` and `remove(x)` are among the other operations. The Java documentation for `HashSet` says it offers constant time for `add`, `remove`, `contains` and `size`, assuming the hash function, the rule that decides where each element is stored, spreads the elements properly. The Kotlin pages for these calls state no cost, so the idea that a Kotlin list behaves like `ArrayList` is general knowledge.

Kotlin's `MutableSet.add` gives back `true` if it added the element and `false` if the element was already there. So one call both checks and adds, in about one glance.

This program counts the steps of a scan inside a loop. The function `containsCounted` stands in for `contains` so that the hidden steps can be counted. Every number in the list is new, so every scan runs to the end.

```kotlin
var steps = 0

fun containsCounted(list: List<Int>, target: Int): Boolean {
    for (item in list) {
        steps++
        if (item == target) return true
    }
    return false
}

fun uniqueWithScan(items: List<Int>): List<Int> {
    val seen = mutableListOf<Int>()
    for (item in items) {
        if (!containsCounted(seen, item)) {
            seen.add(item)
        }
    }
    return seen
}

fun main() {
    for (n in listOf(10, 100, 1000)) {
        steps = 0
        uniqueWithScan(List(n) { it })
        println("n = $n: $steps steps")
    }
}
```

This prints three lines.

| n | Steps |
|---|---|
| 10 | 45 |
| 100 | 4950 |
| 1000 | 499500 |

Ten times more items cost about 100 times more steps.

## Predict the output

```challenge
type: predict
id: count-the-hidden-steps
verified: kotlin-2.4.21
code: |
  fun main() {
      val items = listOf(5, 7, 9, 11)
      val seen = mutableListOf<Int>()
      var steps = 0
      for (item in items) {
          repeat(seen.size) { steps++ }
          seen.add(item)
      }
      println(steps)
  }
options:
  - id: a
    text: "4"
    why: "You may have counted the turns you can see. The line inside each turn also walks, and it walks further every time."
  - id: b
    text: "16"
    why: "You may have used n times n. The line holds 0, 1, 2 and 3 students when the next one arrives, so it is less than n times n."
  - id: c
    text: "6"
    correct: true
    why: "The corridor holds 0, then 1, then 2, then 3 students when each new one arrives. The handshakes are 0 + 1 + 2 + 3, which is 6."
output: |
  6
```

```challenge
type: predict
id: sign-in-board-keeps-first
verified: kotlin-2.4.21
code: |
  fun main() {
      val items = listOf(4, 8, 4, 2, 8, 6)
      val seen = HashSet<Int>()
      val result = mutableListOf<Int>()
      for (item in items) {
          if (seen.add(item)) {
              result.add(item)
          }
      }
      println(result)
  }
options:
  - id: a
    text: "[4, 8, 4, 2, 8, 6]"
    why: "You may have thought the check never says no. A name already on the board makes add give back false, so the student is not added."
  - id: b
    text: "[4, 8, 2, 6]"
    correct: true
    why: "Each student writes their name on the sign-in board. A name that is already there is turned away, so each number joins the list once, in arrival order. One glance replaces the handshakes."
  - id: c
    text: "[2, 4, 6, 8]"
    why: "You may have expected the board to sort the names. The board only remembers names. The result list keeps the order in which new students arrived."
output: |
  [4, 8, 2, 6]
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see three lines: `n = 10: 45 steps`, `n = 100: 4950 steps` and `n = 1000: 499500 steps`. Each time n grows 10 times, the steps grow about 100 times.
3. **Break.** Change `List(n) { it }` to `List(n) { 0 }` and change nothing else. Run it again. The steps fall to 9, 99 and 999. Every newcomer is the same as the first student, so the handshake ends at the first match. The cost depends on the data, and the worst case is the newcomer nobody has met.
4. **Fix.** Put `List(n) { it }` back and run it. The lines `45`, `4950` and `499500` come back.

## Fix the bug

```challenge
type: fix
id: fix-merge-scan
generated: true
verified: kotlin-2.4.21
code: |
  data class Item(val id: Int, val title: String)

  var checks = 0

  fun hasId(list: List<Item>, id: Int): Boolean {
      for (item in list) {
          checks++
          if (item.id == id) return true
      }
      return false
  }

  fun mergeItems(local: List<Item>, server: List<Item>): List<Item> {
      val merged = local.toMutableList()
      for (fromServer in server) {
          if (!hasId(local, fromServer.id)) {
              merged.add(fromServer)
          }
      }
      return merged
  }

  fun main() {
      val local = List(100) { Item(it, "Local $it") }
      val server = List(100) { Item(it + 50, "Server ${it + 50}") }
      val merged = mergeItems(local, server)
      println("merged: ${merged.size} items")
      println("id checks: $checks")
  }
hints:
  - "The merged count is right, but the second line is far too high. Look at line 16: how many items does hasId walk through each time it runs?"
  - "Build a set of the local ids once. Then ask the set about each server item. A set answers 'is this id here?' in about one glance."
expected: |
  merged: 150 items
  id checks: 200
fix: |
  data class Item(val id: Int, val title: String)

  var checks = 0

  fun mergeItems(local: List<Item>, server: List<Item>): List<Item> {
      val knownIds = HashSet<Int>()
      for (item in local) {
          checks++
          knownIds.add(item.id)
      }
      val merged = local.toMutableList()
      for (fromServer in server) {
          checks++
          if (knownIds.add(fromServer.id)) {
              merged.add(fromServer)
          }
      }
      return merged
  }

  fun main() {
      val local = List(100) { Item(it, "Local $it") }
      val server = List(100) { Item(it + 50, "Server ${it + 50}") }
      val merged = mergeItems(local, server)
      println("merged: ${merged.size} items")
      println("id checks: $checks")
  }
explanation: "The old code walked the local list for every server item, about 100 steps each time. The set is filled once, 100 steps, and then answers each server item in one glance, 100 more."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, this code has no null values in play. For question 4, `hasId` runs once for every server item, and each run is a loop of its own.

## Tricks to remember

- A loop, then a call: ask what the call walks through.
- n turns, each walking about n steps, is about n times n.
- Handshakes with everyone already there is slow. A glance at the sign-in board is fast.
- Task: open a list function you wrote and circle every `contains`, `indexOf` and `remove(x)` that sits inside a loop. Say the cost of each circled line.

## Common mistakes

- **Thinking one loop means O(n).** You can see one `for`, so it feels like one pass. A call inside it can be a loop too. Ask what each line inside the loop really does.
- **Trusting a test with 20 items.** It runs fast, so the code feels fine. At 20 items n times n is only 400 steps. Try 2,000 items before you decide.
- **Thinking a short line is a cheap line.** `contains` is one word, so it feels like one step. The word hides a walk through the list.
- **Thinking the cost is the same for every input.** The Run step shows one fixed pattern. The Break step shows that a walk that ends at the first match costs far less. Plan for the newcomer nobody has met.

## Where this shows up in Android

The Android documentation says an app must render frames in under 16 ms to reach 60 frames per second. If it overruns, the system skips frames and the user sees stutter, which the page calls jank. Now picture a feed that refreshes by matching each of 2,000 server items against the whole saved list, the same shape as the Fix the bug task. That is about two million checks. My own reasoning: if the merge runs while the screen waits to draw, the checks come first, and I have no official figure for how long two million checks take on a phone. The symptom is a feed that refreshes smoothly in a test with 20 items and stutters for a user with a long history. The part of this lesson that explains it is the hidden loop in the matching call.

## Say it in an interview

**What hidden cost appears when you call `list.contains(x)` inside a `for` loop over the same list?**
Each `contains` walks the list, so n turns of up to n steps each give O(n²). The Java documentation for `ArrayList` puts `contains` among the operations that run in linear time. A `HashSet` fixes it. Its documentation says `contains` takes constant time as long as the hash function spreads the elements well. The trade-off is extra memory for the set (my own reasoning).

**Does a `while` loop inside a `for` loop always mean O(n²)?**
No. The cost is the total number of steps, not the number of loops. If the inner loop only moves a pointer forward and never goes back, all the inner turns together add up to about n. That makes the whole function O(n). This is general knowledge, and the sliding-window problems in the practice list show it.

**How do you work out the cost of `.filter { }.map { }.sortedBy { }` on a list?**
Each call is its own pass, one after the other, so the costs add. `filter` and `map` are each about n, and a sort is about n log n. The biggest term wins, so the chain is about n log n. This is general knowledge. If one of the lambdas calls `contains` on a list, that lambda multiplies the cost.

**How would you spot an accidental O(n²) on a list screen?**
I would look inside the code that builds each row for calls that walk a whole list, such as a search, a `contains` or a `filter` per item. With n rows and a list of n, that is n times n. Then I would count steps or use a profiler before and after the change (my own reasoning).

## Recap

1. One visible loop can hide a second loop inside a call such as `contains`.
2. A loop of n turns that calls an n-step call takes about n times n steps.
3. A set lookup takes about one step, so it removes the hidden loop.

## You can now

- Spot the call that hides a second loop inside a function with one loop.
- Predict how many steps a scan inside a loop takes for a short list.
- Fix a merge that scans a list for every item by using a set.
- Explain why 10 times more items can mean about 100 times more steps.

## Practise on these

Say the cost of the slow way and the fast way before you code. The list runs from the gentlest to the hardest.

1. LeetCode 283, Move Zeroes (Easy). Spot how shifting items inside a loop hides O(n²) work, then fix it in O(n). https://leetcode.com/problems/move-zeroes/
2. LeetCode 14, Longest Common Prefix (Easy). Count two loops whose bounds depend on the number of strings and on their length. https://leetcode.com/problems/longest-common-prefix/
3. LeetCode 209, Minimum Size Subarray Sum (Medium). See why a `while` inside a `for` stays O(n) when both pointers only move forward. https://leetcode.com/problems/minimum-size-subarray-sum/
4. LeetCode 15, 3Sum (Medium). Combine a sort of O(n log n) with a scan to reach O(n²) instead of O(n³). https://leetcode.com/problems/3sum/
5. LeetCode 84, Largest Rectangle in Histogram (Hard). Show that each item is pushed and popped once, so the total is O(n). https://leetcode.com/problems/largest-rectangle-in-histogram/
