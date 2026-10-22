---
id: dsa/linked-lists/insert-and-remove-nodes
title: How do I remove a node without breaking the chain?
status: live
difficulty: medium
order: 11
requires: [dsa/linked-lists/what-is-a-linked-list]
summary: To remove a node, link the one before it to the one after it first. Letting go first loses the rest of the chain.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [conga-line-hands]
playground: true
fruit: false
upnext: false
---

## The question

A to-do app keeps its tasks in a chain, where each task only knows which task comes next. The user deletes task 2 of 5. If the app drops task 2 and does nothing else, what happens to tasks 3, 4 and 5? How do you take one item out of the middle of a chain without breaking it?

## Mental model

Think of a conga line.

- Each dancer is one item in the chain. One item like this is called a **node**.
- Each dancer holds on to the dancer behind with one hand. That hand is the link to the next item.
- The leader at the front is the **head**. Someone has to know who the leader is.
- The last dancer holds nobody.
- A hand can hold only one dancer at a time. A dancer whom nobody holds has dropped out of the line.

Take a line of four: Ada holds Ben, Ben holds Cy, Cy holds Dee, and Dee holds nobody. No dancer can see past the one in their hand.

**Where the analogy stops:** In a real conga line, a dancer who drops out walks off and you can see the gap. In code, a dancer nobody holds cannot be reached from the leader. Nothing marks the loss, and the program cannot tell you a dancer is missing. Also, a dancer who has been let go still exists in the computer's memory for a while. The system tidies it away later (general knowledge). And each grab takes one step, in the order you choose.

## See it move

```sim
id: conga-line-hands
template: stepper
title: A conga line where each dancer holds the next
intro: "Ada, Ben, Cy and Dee dance in a line. Each dancer's hand holds the next dancer, and Dee holds nobody. Ben wants to leave the line."
lines:
  - { id: grab, text: "Ada grabs the dancer Ben is holding." }
  - { id: release, text: "Ben lets go." }
  - { id: check, text: "Walk the line from Ada and call the names." }
board:
  - { var: ada, label: "Ada's hand holds" }
  - { var: ben, label: "Ben's hand holds" }
  - { var: cy, label: "Cy's hand holds" }
  - { var: dee, label: "Dee's hand holds" }
vars: { ada: Ben, ben: Cy, cy: Dee, dee: nobody }
actions:
  - id: new-line
    label: "Make a fresh line"
    do:
      - set: { ada: Ben, ben: Cy }
      - light: []
      - say: "A fresh line forms. Ada holds Ben, Ben holds Cy, Cy holds Dee, and Dee holds nobody."
  - id: ada-grabs
    label: "Ada grabs the dancer Ben is holding"
    cases:
      - when: { var: { ben: Cy } }
        do:
          - set: { ada: Cy }
          - light: [grab]
          - say: "Ada grabs Cy, the dancer Ben was holding. Ada's hand now holds Cy."
      - when: { var: { ben: nobody } }
        do:
          - set: { ada: nobody }
          - light: [grab]
          - say: "Ben has already let go, so Ada grabs nobody. Ada's hand now holds nobody."
  - id: ben-lets-go
    label: "Ben lets go"
    do:
      - set: { ben: nobody }
      - light: [release]
      - say: "Ben lets go of Cy. Ben's hand now holds nobody."
  - id: walk
    label: "Walk the line from Ada"
    cases:
      - when: { var: { ada: Ben, ben: nobody } }
        do:
          - light: [check]
          - say: "Ada, Ben. That is 2 dancers. Cy and Dee were left behind when Ben let go."
      - when: { var: { ada: Ben, ben: Cy } }
        do:
          - light: [check]
          - say: "Ada, Ben, Cy, Dee. All 4 dancers are in the line."
      - when: { var: { ada: Cy } }
        do:
          - light: [check]
          - say: "Ada, Cy, Dee. Ben is out of the line and nobody is lost."
      - when: { var: { ada: nobody } }
        do:
          - light: [check]
          - say: "Only Ada is left in the line. Ben, Cy and Dee are lost."
script: [ada-grabs, ben-lets-go, walk, new-line, ben-lets-go, ada-grabs, walk]
try:
  - text: "Press \"Ada grabs the dancer Ben is holding\", then \"Ben lets go\", then \"Walk the line from Ada\". Read the names."
    done: [ada-grabs, ben-lets-go, walk]
  - text: "Press \"Make a fresh line\". This time press \"Ben lets go\" first, then \"Ada grabs the dancer Ben is holding\", then \"Walk the line from Ada\"."
    done: [new-line, ben-lets-go, ada-grabs, walk]
```

**What you should notice:** When Ada grabs Cy before Ben lets go, the walk finds Ada, Cy and Dee. When Ben lets go first, Ada has nothing to grab and the walk finds only Ada.

## Build the logic

### Notice

The line is Ada, Ben, Cy and Dee. Ada holds Ben, Ben holds Cy, Cy holds Dee, and Dee holds nobody. Ben wants out. Only Ben's hand knows who comes after Ben. Only Ada's hand knows Ben is there at all.

### Guess

What should happen so that Cy and Dee stay in the line?

- A. Ben walks away and the line mends itself.
- B. Ada grabs Cy, the dancer Ben is holding, and then Ben lets go.
- C. Ben lets go of Cy first, and then Ada grabs whoever Ben was holding.

Idea A hopes for something no hand does: a hand holds what it holds, and nothing mends it. Idea C fails because once Ben lets go, nobody knows who Cy is. Idea B is the answer.

Now the opposite job. Zed joins the line between Ada and Ben. Which hand moves first?

- A. Zed grabs Ben.
- B. Ada lets go of Ben and grabs Zed.
- C. The order does not matter.

Idea B drops Ben, Cy and Dee: once Ada lets go of Ben, nobody is holding them. Idea C ignores that the order decides who gets dropped. Idea A is the answer.

### Build

To remove a dancer:

1. Walk from the leader to find the dancer in front of the one who is leaving. Hands only point forward, so you must stop one dancer early.
2. That dancer grabs the dancer whom the leaver is holding.
3. The leaver lets go.

To add a dancer:

1. Walk to find the dancer in front of the spot.
2. The newcomer grabs the dancer whom the front dancer is holding.
3. The front dancer grabs the newcomer.

The rule behind both: grab the rest of the line before you let go of anything.

Two special cases. The leader has nobody in front, so removing the leader means the head moves to the second dancer. The last dancer holds nobody, so the dancer in front of the last one ends up holding nobody and the line ends one dancer sooner.

Finding the spot can take many steps. Each grab takes one step, and a remove or an add needs only two or three grabs however long the line is.

### Name it

A chain of nodes where each node holds a link to the next is a **linked list**. In the code, the type `Node?` means a Node or nothing, and nothing is written `null`. The hand of the last dancer holds `null`.

To **remove** a node, the node in front takes over the link of the node being removed. In Kotlin, with the `next` property as the hand, that is `ada.next = ben.next`. To **insert** a node, first make the new node hold the node that follows, then make the node in front hold the new node. The first step happens when the new node is made: `Node("Zed", ada.next)`. The second step is `ada.next = zed`.

```kotlin
class Node(val name: String, var next: Node?)

fun printLine(head: Node?) {
    var current = head
    while (current != null) {
        print(current.name)
        if (current.next != null) print(" -> ")
        current = current.next
    }
    println()
}

fun main() {
    val dee = Node("Dee", null)
    val cy = Node("Cy", dee)
    val ben = Node("Ben", cy)
    val ada = Node("Ada", ben)
    printLine(ada)

    ada.next = ben.next
    printLine(ada)

    val zed = Node("Zed", ada.next)
    ada.next = zed
    printLine(ada)
}
```

This prints three lines: `Ada -> Ben -> Cy -> Dee`, then `Ada -> Cy -> Dee`, then `Ada -> Zed -> Cy -> Dee`. If the removed node is the last one, its `next` is null, so the node in front ends up holding null and the chain ends there.

## Predict the output

```challenge
type: predict
id: remove-the-second
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, var next: Node?)

  fun main() {
      val b = Node(20, Node(30, null))
      val a = Node(10, b)
      a.next = b.next
      var current: Node? = a
      while (current != null) {
          println(current.value)
          current = current.next
      }
  }
options:
  - id: a
    text: "10, then 20, then 30"
    why: "You may have expected the removal to change nothing. The line a.next = b.next makes the first dancer hold the third, so the second is skipped."
  - id: b
    text: "10, then 20"
    why: "You may have thought the last dancer is the one removed. The dancer whose hand was changed is the first, and it now holds the third."
  - id: c
    text: "10, then 30"
    correct: true
    why: "The first dancer grabs the dancer the second one is holding. The walk goes from 10 straight to 30, and 20 is no longer in the line."
output: |
  10
  30
```

```challenge
type: predict
id: let-go-before-grab
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, var next: Node?)

  fun main() {
      val first = Node(1, Node(3, null))
      val newcomer = Node(2, null)
      first.next = newcomer
      var current: Node? = first
      while (current != null) {
          println(current.value)
          current = current.next
      }
  }
options:
  - id: a
    text: "1, then 2, then 3"
    why: "You may have thought the old note to 3 mends itself. The first dancer let go of 3 to grab 2, and 2 holds nobody."
  - id: b
    text: "1, then 2"
    correct: true
    why: "The first dancer let go of dancer 3 before the newcomer grabbed it. Nobody holds 3 now, so the walk ends at 2."
  - id: c
    text: "1, then 3"
    why: "You may have thought the newcomer was never added. The first dancer now holds the newcomer, so 2 is in the line."
output: |
  1
  2
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see three lines: `Ada -> Ben -> Cy -> Dee`, `Ada -> Cy -> Dee` and `Ada -> Zed -> Cy -> Dee`.
3. **Break.** Change `Node("Zed", ada.next)` to `Node("Zed", null)`. Run it again. The third line becomes `Ada -> Zed`. The newcomer never grabbed Cy, so Cy and Dee fell out of the line.
4. **Fix.** Put `ada.next` back inside `Node("Zed", ada.next)` and run it. The third line is `Ada -> Zed -> Cy -> Dee` again.

## Fix the bug

```challenge
type: fix
id: fix-remove-order
verified: kotlin-2.4.21
code: |
  class Node(val name: String, var next: Node?)

  fun printLine(head: Node?) {
      var current = head
      while (current != null) {
          print(current.name)
          if (current.next != null) print(" -> ")
          current = current.next
      }
      println()
  }

  fun removeAfter(previous: Node) {
      val target = previous.next
      if (target != null) {
          target.next = null
          previous.next = target.next
      }
  }

  fun main() {
      val dee = Node("Dee", null)
      val cy = Node("Cy", dee)
      val ben = Node("Ben", cy)
      val ada = Node("Ada", ben)
      removeAfter(ada)
      printLine(ada)
  }
hints:
  - "Run it. Only Ada is left, but Cy and Dee should still be in the line. Look at the two lines inside the if: which one runs first?"
  - "Ada must grab Cy before Ben lets go of Cy. Swap the two lines."
expected: |
  Ada -> Cy -> Dee
fix: |
  class Node(val name: String, var next: Node?)

  fun printLine(head: Node?) {
      var current = head
      while (current != null) {
          print(current.name)
          if (current.next != null) print(" -> ")
          current = current.next
      }
      println()
  }

  fun removeAfter(previous: Node) {
      val target = previous.next
      if (target != null) {
          previous.next = target.next
          target.next = null
      }
  }

  fun main() {
      val dee = Node("Dee", null)
      val cy = Node("Cy", dee)
      val ben = Node("Ben", cy)
      val ada = Node("Ada", ben)
      removeAfter(ada)
      printLine(ada)
  }
explanation: "Ben let go of Cy before Ada grabbed Cy, so Ada's hand got nobody. Grabbing first keeps Cy and Dee in the line."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, `target` can be null when nobody is behind `previous`, and the `if` handles that. For question 4, `removeAfter` runs once here, and calling it twice would remove two dancers.

## Tricks to remember

- Grab before you let go.
- Insert: the newcomer grabs first, then the dancer in front grabs the newcomer.
- Removing changes the hand of the dancer in front, not the hand of the leaver.
- Finding the spot costs walking. Relinking costs two or three grabs.

## Common mistakes

- **Thinking a removed node deletes itself from the chain.** In everyday apps, a delete button does the whole job, so it feels like the chain mends itself. No hand mends anything. Someone must grab the next dancer.
- **Letting go before grabbing.** Ben letting go first feels like the polite order. Then nobody knows who Cy is, and Cy and Dee are cut off. Grab first, then let go.
- **Forgetting that the leader is special.** The leader has no dancer in front, so there is no hand to change. The head has to move to the second dancer instead.
- **Stopping the walk on the leaver.** You find Ben, and it feels right to stop there. Hands only point forward, so from Ben you cannot reach Ada. Stop one dancer early, on the one in front.

## Where this shows up in Android

Picture a to-do list or playlist where the user drags one task out of the middle. In a list kept as a row of boxes, every later task slides forward one place. In a chain, only one hand changes. My own reasoning: this is why chains are described as good at removing from the middle, but the app must first walk to the task, which is the cost from the earlier lesson. I have no official Android page that says which kind of list a given screen uses. The symptom of getting it wrong is a task that vanishes together with every task below it, which is the cut-off tail in the Fix the bug task. The Java documentation says `LinkedList` is a doubly-linked list, so each node holds a link to the one in front as well as the one behind.

## Say it in an interview

**What problem does a dummy head node solve when you insert or delete in a linked list?**
The first node has nobody in front, so inserting or deleting at the front needs special code that changes the head itself. A dummy head is an extra empty node placed before the first real one. Then every real node has a node in front, and the same code works at the front. The trade-off is one extra node (general knowledge).

**In what order do you assign links when you insert a new node between `prev` and `prev.next`, and what breaks if you swap them?**
First `newNode.next = prev.next`, then `prev.next = newNode`. If you swap them, `prev.next` already points at the new node, so `newNode.next = prev.next` makes the new node point at itself, and the rest of the list is lost. This is my own reasoning, traced by hand.

**Why can a doubly linked list delete a node in constant time (a fixed number of steps, however long the list is) when you hold that node, but a singly linked list cannot?**
A doubly linked node holds links to both its next and previous nodes, so from the node you can reach the node in front and relink it at once. A singly linked node does not know who is in front, so you must walk from the head to find it. The Java documentation says `LinkedList` is doubly-linked. The trade-off is one extra link in every node (general knowledge).

**Why does the trick of copying the next node's value fail for the last node?**
The trick deletes a node you hold when you cannot reach the node in front. You copy the next node's value into the node you hold and then skip the next node. The last node has no next node to copy from, so it cannot work there (general knowledge). Interview problems that use this trick promise that the node is not the last one.

## Recap

1. Removing a node means linking the node in front to the node behind it.
2. Always grab the rest of the chain before you let go of anything.
3. Finding the spot costs steps, but relinking takes only two or three moves.

## You can now

- Explain why a removed node does not mend the chain on its own.
- Predict which nodes are left after the second node of a chain is removed.
- Predict what an insert in the wrong order leaves behind.
- Fix a removal that lets go before it grabs.

## Practise on these

Draw the hands before you code, and say which hand changes first. The list runs from the gentlest to the hardest. LeetCode gives you its own `ListNode` class with `val` and `next`, which works like the `Node` here.

1. LeetCode 203, Remove Linked List Elements (Easy). Use a dummy head so that removing at the front looks the same as removing in the middle. https://leetcode.com/problems/remove-linked-list-elements/
2. LeetCode 237, Delete Node in a Linked List (Medium). Remove a node when you are not given the node in front. https://leetcode.com/problems/delete-node-in-a-linked-list/
3. LeetCode 19, Remove Nth Node From End of List (Medium). Keep a gap between two walkers to stop one dancer early. https://leetcode.com/problems/remove-nth-node-from-end-of-list/
4. LeetCode 25, Reverse Nodes in k-Group (Hard). Combine counting a group, reversing it and reconnecting the ends. https://leetcode.com/problems/reverse-nodes-in-k-group/
