---
id: dsa/stacks/what-is-a-stack
title: Which item comes out first from a stack?
status: live
difficulty: easy
order: 13
requires: [dsa/arrays/arrays-in-memory]
summary: A stack hands back the item you added last. See why, and why you can never take one from the middle.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [plate-pile]
playground: true
fruit: false
upnext: false
---

## The question

You press Back on your phone and land on the screen you were looking at a minute ago. You press Back again, and the screen before that appears. Nobody asks which screen you want. Which one comes back first, and why that one?

## Mental model

Think of a pile of clean plates on a canteen counter.

- The pile is the stack. It stands on the counter in one place.
- The top plate is the only plate you can reach.
- Putting a plate down means placing it on top. It becomes the new top plate.
- Taking a plate means lifting the top one off. The plate under it becomes the new top.
- Looking at the top plate without lifting it is allowed.
- The plate at the bottom has waited longest and is the last to leave.

So the plate you put down last is the first one you pick up.

**Where the analogy stops:** With real plates you can slide one out of the middle if you try, and the pile may wobble. A stack in code has no middle door. The code can only ask for the top. Also, a real pile has a height limit (general knowledge), and plates are all alike. A stack in a program holds anything, and it grows as long as the program has memory (general knowledge).

## See it move

```sim
id: plate-pile
template: lanes
title: "A pile of canteen plates"
intro: "Three plates wait on the counter. The pile and the served tray are empty."
lanes:
  - { id: counter, label: "Counter (plates waiting)", kind: row, slots: 3, index0: false }
  - { id: pile, label: "Pile (top plate is taken first)", kind: stack, slots: 3 }
  - { id: served, label: "Served tray", kind: row, slots: 3, index0: false }
items:
  - { id: p1, label: "Plate 1", at: "counter.0" }
  - { id: p2, label: "Plate 2", at: "counter.1" }
  - { id: p3, label: "Plate 3", at: "counter.2" }
vars: { onpile: 0, served: 0, rule: stack }
show:
  - { var: onpile, label: "Plates on the pile" }
  - { var: served, label: "Plates served" }
actions:
  - id: put-next
    label: "Put the next plate on the pile"
    cases:
      - when: { at: { p1: "counter.0" } }
        do:
          - push: { pile: p1 }
          - add: { onpile: 1 }
          - say: "Plate 1 goes on the pile, and as the only plate it is the top."
      - when: { at: { p2: "counter.1" } }
        do:
          - push: { pile: p2 }
          - add: { onpile: 1 }
          - say: "Plate 2 goes on top of the pile and is the top now."
      - when: { at: { p3: "counter.2" } }
        do:
          - push: { pile: p3 }
          - add: { onpile: 1 }
          - say: "Plate 3 goes on top of the pile and is the top now."
    refuse: "No plate is waiting on the counter."
  - id: take-top
    label: "Take the top plate"
    cases:
      - when: { at: { p3: "pile.2" } }
        do:
          - move: { p3: "served.0" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 3 is on top, so it leaves first."
      - when: { at: { p3: "pile.1" } }
        do:
          - move: { p3: "served.0" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 3 is on top, so it leaves first."
      - when: { at: { p3: "pile.0" } }
        do:
          - move: { p3: "served.0" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 3 is the only plate on the pile, so it is the top and it leaves."
      - when: { at: { p2: "pile.1" } }
        do:
          - move: { p2: "served.1" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 2 is on top of plate 1, so plate 2 leaves first."
      - when: { at: { p2: "pile.0" } }
        do:
          - move: { p2: "served.1" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 2 is the only plate on the pile, so it is the top and it leaves."
      - when: { at: { p1: "pile.0" } }
        do:
          - move: { p1: "served.2" }
          - add: { onpile: -1, served: 1 }
          - say: "Plate 1 is the only plate on the pile, so it is the top and it leaves."
    refuse: "The pile is empty, so there is no top plate to take."
  - id: pull-middle
    label: "Pull a plate from the middle"
    cases:
      # The pile only ever opens at the top, so this case can never hold and the action always shows its refusal.
      - when: { var: { rule: any } }
        do:
          - say: "The middle plate comes out."
    refuse: "Refused: a plate in the middle has plates on top of it, so pulling it would topple the pile, and a stack gives out only the top plate."
script: [put-next, put-next, put-next, take-top, pull-middle, take-top, take-top]
try:
  - text: "Press \"Put the next plate on the pile\" three times. Watch each new plate land on top."
    done: [put-next, put-next, put-next]
  - text: "Press \"Take the top plate\" once. Read which plate left."
    done: [take-top]
  - text: "Press \"Pull a plate from the middle\" and read the answer."
    done: [pull-middle]
  - text: "(Optional) Press \"Take the top plate\" until the pile is empty, then once more."
    done: [take-top, take-top, take-top]
    optional: true
```

**What you should notice:** Plates leave the pile in the opposite order to the one in which they went on, and the pile refuses to give up a plate from the middle.

## Build the logic

### Notice

You put plate 1 on the counter. You put plate 2 on top of it. You put plate 3 on top of that. Now three plates stand in one pile, and only plate 3 can be reached.

### Guess

You take plates off the pile one at a time until it is empty. In what order do they come off?

- A. Plate 3, plate 2, plate 1.
- B. Plate 1, plate 2, plate 3.
- C. Plate 2, plate 3, plate 1.

Idea B lets the first plate placed leave first. A pile does not work that way, because plate 1 is at the bottom under two others. Idea C needs a hand reaching into the middle. The pile has no middle door, so C cannot happen. Idea A is the answer: the pile gives plates back in the opposite order.

### Build

1. Give the pile exactly one door: the top.
2. To add a plate, place it on top. The plate you placed is now the top.
3. To take a plate, lift the top one. The plate under it becomes the top.
4. To only look, read the top plate and leave it where it is.
5. If the pile is empty, there is no top plate. Decide what to do: stop with an error or answer "nothing here".
6. Because the top is the only door, the last plate placed is always the first one to leave.

### Name it

A **stack** is a pile that follows one rule: **last in, first out**, or **LIFO** for short. The item added last is the first one out. Three actions go with it. To **push** is to add on top. To **pop** is to take the top off and hand it back. To **peek** is to look at the top without removing it.

Kotlin's `ArrayDeque` can play the part of a stack. The Kotlin collections overview says `ArrayDeque` fills the role of both a stack and a queue, and that it is realised with a resizable array. The names differ from the plate story. `addLast` pushes, `removeLast` pops and `last()` peeks. The Java documentation for the old `Stack` class says the `Deque` interface (a pile or queue open at both ends) and its implementations give a more complete and consistent set of LIFO operations and should be used in preference to `Stack`.

```kotlin
fun main() {
    val plates = ArrayDeque<String>()
    plates.addLast("plate 1")
    plates.addLast("plate 2")
    plates.addLast("plate 3")
    println("Top: ${plates.last()}")
    println("Taken: ${plates.removeLast()}")
    println("Taken: ${plates.removeLast()}")
    println("Left: $plates")
}
```

This prints `Top: plate 3`, then `Taken: plate 3`, then `Taken: plate 2`, then `Left: [plate 1]`. Looking at the top changed nothing. Each take removed the top.

An empty stack has no top. The Kotlin reference says `removeLast()` on an empty `ArrayDeque` throws a `NoSuchElementException`, and `removeLastOrNull()` returns `null` instead.

## Predict the output

```challenge
type: predict
id: push-three-pop-two
verified: kotlin-2.4.21
code: |
  fun main() {
      val pile = ArrayDeque<Int>()
      pile.addLast(1)
      pile.addLast(2)
      pile.addLast(3)
      println(pile.removeLast())
      println(pile.removeLast())
      println(pile.last())
  }
options:
  - id: a
    text: "1, then 2, then 3"
    why: "You may have expected the first plate to leave first. A pile gives the top plate first, and the top is the last one placed."
  - id: b
    text: "3, then 2, then 1"
    correct: true
    why: "Plate 3 was placed last, so it leaves first. Then plate 2 is the top and leaves. Plate 1 is the only plate left, so looking at the top shows 1."
  - id: c
    text: "3, then 2, then 2"
    why: "You may have thought the last line shows the plate that was taken. Looking at the top shows the plate that is on top now, which is plate 1."
output: |
  3
  2
  1
```

```challenge
type: predict
id: look-or-take
verified: kotlin-2.4.21
code: |
  fun main() {
      val pile = ArrayDeque<String>()
      pile.addLast("A")
      pile.addLast("B")
      println(pile.last())
      println(pile.size)
      pile.removeLast()
      println(pile.size)
  }
options:
  - id: a
    text: "B, then 1, then 1"
    why: "You may have thought looking at the top plate lifts it. Looking leaves the plate where it is, so the pile still has 2 plates."
  - id: b
    text: "A, then 2, then 1"
    why: "You may have thought the first plate placed is the top. The plate placed last, B, is on top."
  - id: c
    text: "B, then 2, then 1"
    correct: true
    why: "B is on top, so looking shows B and the pile still holds 2 plates. Taking the top then leaves 1 plate."
output: |
  B
  2
  1
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see four lines: `Top: plate 3`, `Taken: plate 3`, `Taken: plate 2` and `Left: [plate 1]`.
3. **Break.** After the last `println` in `main`, add these two lines and change nothing else: `println("Taken: ${plates.removeLast()}")` twice. Run it. The first new line prints `Taken: plate 1`. The pile is now empty, so the second one stops the program with an error.
4. **Fix.** Change the second new line to use `removeLastOrNull()` instead of `removeLast()`. Run it. It now prints `Taken: null` and the program ends without an error. The pile was empty, so there was no top plate to hand back.

## Fix the bug

```challenge
type: fix
id: fix-pop-from-the-wrong-end
verified: kotlin-2.4.21
code: |
  class PlatePile {
      private val plates = mutableListOf<String>()

      fun push(plate: String) {
          plates.add(plate)
      }

      fun pop(): String {
          return plates.removeAt(0)
      }

      fun isEmpty(): Boolean {
          return plates.isEmpty()
      }
  }

  fun main() {
      val pile = PlatePile()
      pile.push("plate 1")
      pile.push("plate 2")
      pile.push("plate 3")
      while (!pile.isEmpty()) {
          println("Took: ${pile.pop()}")
      }
  }
hints:
  - "The plates come off in the order they went on. Look at the pop function: which end of the row does removeAt(0) take from?"
  - "The plate placed last sits at the end of the row. A pop must take from that end."
expected: |
  Took: plate 3
  Took: plate 2
  Took: plate 1
fix: |
  class PlatePile {
      private val plates = mutableListOf<String>()

      fun push(plate: String) {
          plates.add(plate)
      }

      fun pop(): String {
          return plates.removeAt(plates.lastIndex)
      }

      fun isEmpty(): Boolean {
          return plates.isEmpty()
      }
  }

  fun main() {
      val pile = PlatePile()
      pile.push("plate 1")
      pile.push("plate 2")
      pile.push("plate 3")
      while (!pile.isEmpty()) {
          println("Took: ${pile.pop()}")
      }
  }
explanation: "The push added plates at the end of the row, so the top plate is the last item. Taking index 0 takes the bottom plate, which breaks the rule of last in, first out."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, this code has no null values in play, but `pop` on an empty pile would stop the program with an error. For question 4, `pop` runs three times, once per loop turn.

## Tricks to remember

- Last in, first out: the plate you put down last is the plate you pick up first.
- Push is put on top. Pop is take off the top. Peek is look at the top.
- Task: write the pile after each step of "push 1, push 2, pop, push 3". Say which plate is on top each time.
- The row grows at the end, so the top of the stack is the end of the row, not index 0.

## Common mistakes

- **Thinking a stack lets you take any item.** A pile of items feels like something you can reach into from any side. A stack has one door, the top. If you need the middle, you need a different structure.
- **Writing pop as `removeAt(0)`.** Index 0 feels like "the first one", so it seems right. Index 0 is the bottom plate. Take from the end, where the newest plate sits.
- **Popping an empty stack.** It feels safe because the code worked while plates were there. The Kotlin reference says `removeLast()` throws a `NoSuchElementException` on an empty `ArrayDeque`. Check `isEmpty()` first or use `removeLastOrNull()`.
- **Mixing up peek and pop.** Both show the top plate, so they feel like one action. Peek leaves the plate where it is and pop takes it away.

## Where this shows up in Android

You open a list, tap an item to see its details, and tap again for a photo. Now you press Back twice. The photo goes first, then the details, and the list is back where you started. That is a stack at work.

The Android documentation says that when the current activity (one screen of an app) starts another, the new activity is pushed on top of the back stack (the pile of screens the user has visited) and takes focus. When the user presses Back, the current activity is popped from the top of the stack and destroyed, and the previous one resumes. It says outright that the back stack operates as a last in, first out structure. The same page notes one exception: on Android 12 and higher, Back from a root launcher activity (the home screen that a task starts from) moves the task (the screens one app has opened for the user) to the background instead of finishing it. The part of this lesson that explains the whole behaviour is "the last plate placed is the first to leave".

## Say it in an interview

**How does the Android back stack behave like a LIFO stack?**
Each time a screen (one activity, which is one screen of an app) starts another, the new screen is pushed on top. When the user presses Back, the top screen is popped and destroyed, and the one below resumes. The Android documentation calls this a last in, first out structure. The trade-off is that Back always undoes the most recent step, so to reach a screen two steps down you press Back twice (my own reasoning).

**Why use `ArrayDeque` instead of `java.util.Stack` in Kotlin?**
The Java documentation says the `Deque` interface and its implementations give a more complete and consistent set of LIFO operations and should be used in preference to `Stack`. The Kotlin collections overview says `ArrayDeque` fills the role of both a stack and a queue, and it is built on a resizable array. So one class covers both jobs. In Kotlin I use `addLast`, `removeLast` and `last()` for push, pop and peek.

**What happens when you pop an empty stack?**
It depends on the call. The Kotlin `removeLast()` throws a `NoSuchElementException`, and `removeLastOrNull()` returns `null`. The Java `Stack.pop()` throws an `EmptyStackException`. I pick the throwing call when an empty stack means a bug, and the `OrNull` call when empty is a normal case.

**How can a stack tell you its smallest item in one step?**
Keep a second stack beside it that holds the smallest value so far. Push to both, pop from both, and read the top of the second one. The price is twice the memory for a fast answer. This is my own reasoning, and it is the classic Min Stack problem.

## Recap

1. A stack gives back the item added last, so the order out is the reverse of the order in.
2. Push adds on top, pop takes the top off, and peek only looks.
3. A stack has one door, so you can never take an item from the middle.

## You can now

- Predict the order items leave a stack after a list of pushes and pops.
- Explain why the item added last comes out first.
- Fix a pop that takes the wrong end of the row.
- Say what happens when you pop an empty stack, and two ways to handle it.

## Practise on these

Say the stack after every step on paper before you code. The list runs from the gentlest to the hardest.

1. LeetCode 682, Baseball Game (Easy). Push scores and pop or peek at earlier ones, like undo and history. https://leetcode.com/problems/baseball-game/
2. LeetCode 1047, Remove All Adjacent Duplicates In String (Easy). Use the top of the stack to cancel a matching neighbour. https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
3. LeetCode 232, Implement Queue using Stacks (Easy). Pour items between two stacks to turn last-in-first-out into first-in-first-out. https://leetcode.com/problems/implement-queue-using-stacks/
4. LeetCode 225, Implement Stack using Queues (Easy). Build a stack out of a queue to see both orders side by side. https://leetcode.com/problems/implement-stack-using-queues/
5. LeetCode 155, Min Stack (Medium). Track the smallest item so push, pop, top and minimum are all fast. https://leetcode.com/problems/min-stack/
6. LeetCode 150, Evaluate Reverse Polish Notation (Medium). Evaluate sums written like `3 4 +`: push numbers, and pop two at each sign. https://leetcode.com/problems/evaluate-reverse-polish-notation/
7. LeetCode 739, Daily Temperatures (Medium). Keep days still waiting for a warmer one on a stack, and clear the colder ones when a warmer day arrives. https://leetcode.com/problems/daily-temperatures/
8. LeetCode 42, Trapping Rain Water (Hard). Use a stack of bar positions to fill the gaps between taller bars. https://leetcode.com/problems/trapping-rain-water/
