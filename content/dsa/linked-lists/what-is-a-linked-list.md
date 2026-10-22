---
id: dsa/linked-lists/what-is-a-linked-list
title: How is a chain of nodes different from a row of boxes?
status: live
difficulty: easy
order: 10
requires: [dsa/arrays/insert-and-delete-in-an-array]
summary: A linked list is a chain where each item only knows where the next one is, so reaching item 500 means following 500 links.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [treasure-hunt-clues]
playground: true
fruit: false
upnext: false
---

## The question

A music app wants to show song 500 from a playlist. If the songs sit in numbered boxes in a row, the app opens box 500 at once. But picture a playlist where each song only knows where the next song is kept. How would the app ever find song 500? And why would anyone store a list that way?

## Mental model

Think of a treasure hunt.

- The start note is in your pocket. It tells you where clue 1 is hidden.
- A clue is a card with two things: a message, and a note on where the next clue is hidden.
- The hiding places are spread around the park, in any order the writer chose.
- The last clue says there are no more clues.
- A row of boxes is the other way to store a list: numbered boxes side by side, where you can open box 500 without opening the others.

In the hunt you cannot skip ahead. To get clue 5 you read the start note, then clue 1, then clue 2, then clue 3, then clue 4.

**Where the analogy stops:** In the park you walk for minutes between clues. A program follows a note almost at once, but it still takes one step for each note. Also, in a real hunt the writer chooses the hiding places. In a program the computer chooses where each clue is kept, and you cannot count on them being near each other. The simple chain in this lesson only goes forward, so you cannot walk back to an earlier clue.

## See it move

```sim
id: treasure-hunt-clues
template: zones
title: A treasure hunt where each clue points to the next
intro: "You hold a start note in your pocket. Five clues are hidden at the Pond, the Hill and the Barn, in no neat order. Each clue says where the next one is."
zones:
  - { id: pocket, label: "In your pocket" }
  - { id: pond, label: "Pond" }
  - { id: hill, label: "Hill" }
  - { id: barn, label: "Barn" }
items:
  - { id: start-note, label: "Start note: clue 1 is at the Pond", at: pocket }
  - { id: clue-1, label: "Hidden note, not read yet", at: pond }
  - { id: clue-2, label: "Hidden note, not read yet", at: hill }
  - { id: clue-3, label: "Hidden note, not read yet", at: barn }
  - { id: clue-4, label: "Hidden note, not read yet", at: pond }
  - { id: clue-5, label: "Hidden note, not read yet", at: hill }
vars: { found: 0 }
show:
  - { var: found, label: "Clues found" }
actions:
  - id: follow
    label: "Follow the note in your hand"
    cases:
      - when: { var: { found: 0 } }
        do:
          - add: { found: 1 }
          - mark: { clue-1: good }
          - label: { clue-1: "Clue 1: clue 2 is at the Hill" }
          - light: [clue-1]
          - say: "The start note says clue 1 is at the Pond. You walk there and find clue 1. It says clue 2 is at the Hill."
      - when: { var: { found: 1 } }
        do:
          - add: { found: 1 }
          - mark: { clue-2: good }
          - label: { clue-2: "Clue 2: clue 3 is at the Barn" }
          - light: [clue-2]
          - say: "Clue 1 says clue 2 is at the Hill. You walk there and find clue 2. It says clue 3 is at the Barn."
      - when: { var: { found: 2 } }
        do:
          - add: { found: 1 }
          - mark: { clue-3: good }
          - label: { clue-3: "Clue 3: clue 4 is at the Pond" }
          - light: [clue-3]
          - say: "Clue 2 says clue 3 is at the Barn. You walk there and find clue 3. It says clue 4 is at the Pond."
      - when: { var: { found: 3 } }
        do:
          - add: { found: 1 }
          - mark: { clue-4: good }
          - label: { clue-4: "Clue 4: clue 5 is at the Hill" }
          - light: [clue-4]
          - say: "Clue 3 says clue 4 is at the Pond. You walk there and find clue 4. It says clue 5 is at the Hill."
      - when: { var: { found: 4 } }
        do:
          - add: { found: 1 }
          - mark: { clue-5: good }
          - label: { clue-5: "Clue 5: no more clues" }
          - light: [clue-5]
          - say: "Clue 4 says clue 5 is at the Hill. You walk there and find clue 5. It says there are no more clues."
      - when: { var: { found: 5 } }
        do:
          - say: "Clue 5 says there are no more clues, so there is no note left to follow. The hunt is over."
  - id: jump-to-4
    label: "Go straight to clue 4"
    cases:
      - when: { var: { found: 3 } }
        do:
          - add: { found: 1 }
          - mark: { clue-4: good }
          - label: { clue-4: "Clue 4: clue 5 is at the Hill" }
          - light: [clue-4]
          - say: "You have read clue 3, and it says clue 4 is at the Pond. So you know the way and you find clue 4."
      - when: { var: { found: 4 } }
        do:
          - say: "You already have clue 4."
      - when: { var: { found: 5 } }
        do:
          - say: "You are past clue 4 already."
    refuse: "No note you hold says where clue 4 is. You have found {found} clues, and only clue 3 gives the way."
script: [jump-to-4, follow, follow, follow, jump-to-4, follow]
try:
  - text: "Press \"Go straight to clue 4\" before you have read any clue."
    done: [jump-to-4]
  - text: "Press \"Follow the note in your hand\" three times. Watch the Pond, the Hill and the Barn: the clues are not in order."
    done: [follow, follow, follow]
  - text: "Press \"Go straight to clue 4\" again, then press \"Follow the note in your hand\" once more."
    done: [jump-to-4, follow]
```

**What you should notice:** The clues sit in scattered places, you can reach clue 4 only after reading clue 3, and finding clue 5 takes five notes.

## Build the logic

### Notice

The hunt has five clues. The start note says clue 1 is at the Pond. Clue 1 says clue 2 is at the Hill. Clue 2 says clue 3 is at the Barn, and so on. Clue 5 says there are no more clues.

Clue 2 is not next to clue 1. It is wherever the writer hid it. Only the note on clue 1 tells you where.

### Guess

Where is clue 2 hidden compared with clue 1?

- A. Always right next to it, like boxes in a row.
- B. Wherever the writer chose. Clue 1 says where.
- C. Always at the Pond.

Idea A is how a row of boxes works, not a hunt. Idea C has no reason behind it. Idea B is the answer.

Now count the notes. How many notes do you follow to reach clue 5, counting the start note?

- A. 5 notes, one for each clue.
- B. 4 notes, because clue 1 needs no note.
- C. 1 note, because the start note lists all the places.

Idea B forgets the start note, which is the first note you follow. Idea C is how a row of boxes works. Idea A is the answer.

### Build

1. Each clue holds two things: its own message, and a note on where the next clue is hidden.
2. You hold only one thing at the start: the note that points to clue 1.
3. To visit a clue, follow the note in your hand to its hiding place. Read the message. Now hold the note on that clue.
4. The last clue holds a note that says there is nothing next.
5. To reach clue k, follow k notes in order. There is no shortcut.
6. To visit every clue, follow one note for each clue. For n clues that is n notes.
7. Never put down the start note while you walk. Use a second hand to follow the trail, or you lose the way back to clue 1.

### Name it

A **node** is one clue: it holds a value and a **reference** to the next node. A reference is a note that says where another object is kept (general knowledge). The note in your pocket, the one that points at the first node, is called the **head**. The last node's note holds **null**, which means nothing is there. A **linked list** is a chain of nodes where each node holds a reference to the next one, and you reach the chain from the head.

In Kotlin, a class can keep its values in its parentheses with `val` or `var`, as the Kotlin documentation shows. The type `Node?` with a question mark means a Node or nothing. The documentation says to check for null with an `if` before using a value that might be null.

```kotlin
class Node(val value: String, var next: Node?)

fun main() {
    val clue3 = Node("Barn", null)
    val clue2 = Node("Hill", clue3)
    val clue1 = Node("Pond", clue2)
    val head: Node? = clue1

    var current: Node? = head
    var number = 1
    while (current != null) {
        println("clue $number: ${current.value}")
        current = current.next
        number++
    }
}
```

This prints `clue 1: Pond`, then `clue 2: Hill`, then `clue 3: Barn`. The variable `current` is the second hand: it follows the trail while `head` stays put. The loop stops when `current` becomes null, which is the last clue saying there are no more.

## Predict the output

```challenge
type: predict
id: three-hops-from-the-start
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, var next: Node?)

  fun main() {
      val a = Node(10, Node(20, Node(30, Node(40, null))))
      var current = a
      for (hop in 1..3) {
          val following = current.next
          if (following != null) current = following
      }
      println(current.value)
  }
options:
  - id: a
    text: "30"
    why: "You may have counted the node you start on as hop 1. A hop is one note followed, and you start on 10 before any hop."
  - id: b
    text: "40"
    correct: true
    why: "Hop 1 reaches 20, hop 2 reaches 30 and hop 3 reaches 40. Each hop follows the note on the clue you are standing at."
  - id: c
    text: "20"
    why: "You may have taken only one hop. The loop follows the note three times, one clue further each time."
output: |
  40
```

```challenge
type: predict
id: count-the-clues
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, var next: Node?)

  fun main() {
      val a = Node(5, Node(6, Node(7, null)))
      var count = 0
      var current: Node? = a
      while (current != null) {
          count++
          current = current.next
      }
      println(count)
  }
options:
  - id: a
    text: "2"
    why: "You may have counted the links between the nodes. The loop counts nodes, so it adds one for each clue it stands at."
  - id: b
    text: "7"
    why: "You may have read the last value. The program prints how many nodes it visited, not a value from one."
  - id: c
    text: "3"
    correct: true
    why: "The loop runs once for each clue it stands at. After the third clue the note says nothing is next, so the loop stops with a count of 3."
output: |
  3
```

## Try it

1. **Write.** Copy the program from the second Predict item into the editor.
2. **Run.** You should see `3`. The chain has three nodes.
3. **Break.** Change `Node(6, Node(7, null))` to `Node(6, null)`. Run it again. You should see `2`. The note on the second node no longer points at the third, so the walk never meets it.
4. **Fix.** Put `Node(6, Node(7, null))` back and run it. You should see `3` again.

## Fix the bug

```challenge
type: fix
id: fix-chain-loops-back
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, var next: Node?)

  fun printChain(head: Node?) {
      var current = head
      var steps = 0
      // The cap of 6 steps stops a broken chain from running for ever.
      while (current != null && steps < 6) {
          println(current.value)
          current = current.next
          steps++
      }
  }

  fun main() {
      val third = Node(30, null)
      val second = Node(20, third)
      val first = Node(10, second)
      third.next = first
      printChain(first)
  }
hints:
  - "The chain has three nodes, but six values print. Look at the line in main that runs after the three nodes are made."
  - "The last clue must say there are no more clues. Remove the line that sends the last node back to the start."
expected: |
  10
  20
  30
fix: |
  class Node(val value: Int, var next: Node?)

  fun printChain(head: Node?) {
      var current = head
      var steps = 0
      // The cap of 6 steps stops a broken chain from running for ever.
      while (current != null && steps < 6) {
          println(current.value)
          current = current.next
          steps++
      }
  }

  fun main() {
      val third = Node(30, null)
      val second = Node(20, third)
      val first = Node(10, second)
      printChain(first)
  }
explanation: "The last clue pointed back at the first clue, so the walk went round the trail again. With null in the last note, the walk stops after three clues."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, the last node's `next` is null by design, and the loop checks for it with `current != null`. For question 4, in the broken chain each clue is visited twice, and the cap of 6 steps only makes the run finite, it does not fix the chain.

## Tricks to remember

- Row of boxes: jump by number. Chain of clues: walk by notes.
- Reaching clue k costs k notes. Visiting all n clues costs n notes.
- The end of the chain is a note that holds null.
- Keep the start note safe. Walk with a second hand.

## Common mistakes

- **Thinking the items sit side by side in memory.** A list prints in order, so it feels like boxes in a row. Each node is its own object and the computer can keep it anywhere. Only the notes join them (general knowledge).
- **Expecting to jump to item 500.** In a row of boxes you open box 500 at once, so you try the same here. A chain has no index to jump with. You follow 500 notes from the head.
- **Moving the head while you walk.** It feels natural to use one variable for everything. If you overwrite the only note that points at clue 1, you have lost the start of the chain. Walk with a second variable, as `current` does.
- **Forgetting to stop at null.** The last note says nothing is next, but the loop does not know that unless you ask. Without the `current != null` check, the walk would try to follow a note that is not there.

## Where this shows up in Android

Picture a music player where each song only knows which song plays next. Skipping ahead five songs means five steps, and jumping to song 500 means 500. The Java documentation for `LinkedList` says that operations that index into the list traverse it from the beginning or the end, whichever is closer to the index. My own reasoning: a screen that keeps asking such a list for item 500, again and again while it draws, repeats those walks. The Android page on slow rendering says an app must render frames in under 16 ms to reach 60 frames per second, and that slow frames show up as stutter, called jank. I have no official figure for how long a walk of 500 notes takes on a phone. The part of this lesson that explains the symptom is that reaching item k costs k steps.

## Say it in an interview

**Why does finding the 500th element take about n steps in a linked list, but one step in an array?**
An array keeps items side by side, so the place of item 500 comes from simple arithmetic: the start plus 500 times the item size (general knowledge). A linked list keeps only a link from each node to the next, so you follow 500 links from the head. The Java documentation for `LinkedList` says that operations that index into the list traverse it from the beginning or the end, whichever is closer.

**How do you find the middle of a linked list in one pass?**
Start two walkers at the head. Move one walker 1 node at a time and the other 2 nodes at a time. When the fast walker reaches the end, the slow walker is in the middle. It takes one pass and no extra memory, and you do not need to know the length first (general knowledge).

**How do you tell that a chain loops back on itself?**
Use the same two walkers. If there is a loop, the fast walker catches up with the slow one and they meet. If there is no loop, the fast walker reaches null. This is called Floyd's tortoise and hare (general knowledge). A simpler way is to remember every node you have seen, but it costs extra memory.

**What marks the end of a chain, and what goes wrong if it is missing?**
The last node's `next` holds null, and a walk stops when it sees null. If the last node points back at an earlier node, the walk never ends, as in the Fix the bug task. My own reasoning: in Kotlin the type `Node?` makes the compiler remind you to handle the null case.

## Recap

1. A linked list is a chain of nodes, and each node holds a reference to the next.
2. Reaching item k means following k links from the head, with no shortcut.
3. The last node holds null, which tells a walk to stop.

## You can now

- Explain how a chain of nodes differs from a row of boxes, using the treasure hunt.
- Predict the value reached after following the next link three times.
- Count the nodes of a chain with a loop.
- Fix a chain whose last node points back at the first.

## Practise on these

Say how many links you follow before you code. The list runs from the gentlest to the hardest. LeetCode gives you its own `ListNode` class with `val` and `next`, which works like the `Node` here.

1. LeetCode 876, Middle of the Linked List (Easy). Use a slow walker and a fast walker to find the middle in one pass. https://leetcode.com/problems/middle-of-the-linked-list/
2. LeetCode 141, Linked List Cycle (Easy). Detect whether the notes loop back on themselves, using no extra memory. https://leetcode.com/problems/linked-list-cycle/
3. LeetCode 142, Linked List Cycle II (Medium). Find the node where the loop starts. https://leetcode.com/problems/linked-list-cycle-ii/
4. LeetCode 138, Copy List with Random Pointer (Medium). Copy a chain whose nodes hold a second, arbitrary reference. https://leetcode.com/problems/copy-list-with-random-pointer/
