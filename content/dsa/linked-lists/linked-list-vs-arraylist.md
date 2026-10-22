---
id: dsa/linked-lists/linked-list-vs-arraylist
title: When is a linked list actually the better choice?
status: live
difficulty: medium
order: 12
requires: [dsa/linked-lists/insert-and-remove-nodes, dsa/arrays/how-arraylist-grows]
summary: A linked list inserts without sliding, but finding the spot costs a walk. See when that trade wins.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [index-cards-vs-address-book]
playground: true
fruit: false
upnext: false
---

## The question

A friend tells you how to build a music playlist. Use a linked list, because adding a song in the middle never makes the other songs shuffle along. That sounds smart. Yet the usual advice is to reach for an ArrayList first. So when is the chain of nodes really the better choice?

## Mental model

Think of two ways to keep 1,000 names.

- The pocket address book is the **ArrayList**, a list kept in one resizable array. It has numbered lines, one name per line, in order. To read line 500 you open the book straight at line 500.
- Writing a new name on line 1 means every name below it is rewritten one line lower. That is the sliding.
- The loose index cards tied with string are the **linked list**, a chain of nodes where each card links to the next. Each card holds one name and a string to the next card, and a second string back to the one before.
- To read the 500th name you start at the first card and follow the strings, one card at a time.
- To tie in a new card you undo one string and tie two. No other card moves.

So the cards win at one thing: tying in a card is quick once you stand at the spot. Standing at the spot can take 500 steps.

**Where the analogy stops:** The steps in this lesson are counted ticks, not seconds. A real computer is quick at reading neighbouring lines of a book together, and cards are small separate things that can sit far apart (general knowledge). The Java documentation for ArrayList says its constant factor is low compared with LinkedList, so counted steps do not tell the whole story. Also, a paper address book cannot slide names by itself. A program does the sliding, and it is fast.

## See it move

```sim
id: index-cards-vs-address-book
template: chart
title: "Address book and index cards: steps for one job"
intro: "A pocket address book and a set of index cards tied with string each hold 100 names. The bars are empty until you pick a job."
axes: { x: "Way of keeping names", y: "Steps" }
bars:
  - { id: book, label: "Address book (ArrayList)", value: { var: bk } }
  - { id: cards, label: "Index cards tied with string (LinkedList)", value: { var: cd } }
vars: { bk: 0, cd: 0, big: "off" }
show:
  - { var: bk, label: "Address book steps" }
  - { var: cd, label: "Index card steps" }
toggles:
  - { id: big, label: "Use 1,000 names instead of 100", var: big }
actions:
  - id: read-middle
    label: "Read the name in the middle"
    cases:
      - when: { var: { big: "on" } }
        do:
          - set: { bk: 1, cd: 500 }
          - say: "With 1,000 names the book opens at the middle line in 1 step, while the cards need about 500 steps along the string."
      - do:
          - set: { bk: 1, cd: 50 }
          - say: "With 100 names the book opens at the middle line in 1 step, while the cards need about 50 steps along the string."
  - id: insert-front
    label: "Insert at the front"
    cases:
      - when: { var: { big: "on" } }
        do:
          - set: { bk: 1000, cd: 1 }
          - say: "With 1,000 names every name slides one line down, about 1,000 steps, while the cards need 1 step to write a card and tie it to the old first card."
      - do:
          - set: { bk: 100, cd: 1 }
          - say: "With 100 names every name slides one line down, about 100 steps, while the cards need 1 step to write a card and tie it to the old first card."
  - id: insert-by-position
    label: "Insert in the middle, by position"
    cases:
      - when: { var: { big: "on" } }
        do:
          - set: { bk: 500, cd: 501 }
          - say: "The book slides about 500 names, and the cards walk about 500 steps to find the spot and 1 step to tie in the card, so neither side wins."
      - do:
          - set: { bk: 50, cd: 51 }
          - say: "The book slides about 50 names, and the cards walk about 50 steps to find the spot and 1 step to tie in the card, so neither side wins."
  - id: insert-held
    label: "Insert next to a card you already hold"
    cases:
      - when: { var: { big: "on" } }
        do:
          - set: { bk: 500, cd: 1 }
          - say: "The book still slides about 500 names, but the cards need 1 step because you already stand at the spot."
      - do:
          - set: { bk: 50, cd: 1 }
          - say: "The book still slides about 50 names, but the cards need 1 step because you already stand at the spot."
script: [read-middle, insert-front, insert-by-position, insert-held]
try:
  - text: "Press \"Read the name in the middle\" and compare the two bars."
    done: [read-middle]
  - text: "Press \"Insert at the front\" and compare the bars."
    done: [insert-front]
  - text: "Press \"Insert in the middle, by position\". Then press \"Insert next to a card you already hold\". Read both bars each time."
    done: [insert-by-position, insert-held]
  - text: "(Optional) Switch on \"Use 1,000 names instead of 100\" and press \"Insert at the front\" again."
    done: ["big=on", insert-front]
    optional: true
```

**What you should notice:** The cards win only when you already hold the spot or work at the front, and they lose or tie when you must find a position by walking.

## Build the logic

### Notice

Take a list of 1,000 names. Four jobs come up all the time: read the name at a position, add a name at the front, add a name at a position in the middle, and add a name next to one you are already looking at. The book and the cards do each job in a different way.

### Guess

Your friend says the cards always win at adding names. Which job could make that false?

- A. Adding a name at the front.
- B. Adding a name at line 500 when all you know is the number 500.
- C. Adding a name right after a card you are already holding.

Idea A is where the cards win, because only one string changes. Idea C is the same, because you are already at the spot. Idea B is the answer: to reach line 500 you must follow about 500 strings first, and the walk costs as much as the sliding you avoided.

### Build

1. Write down the job in two parts: finding the spot, then changing the list there.
2. In the book, finding line 500 takes 1 step, because the line number tells you where to open.
3. In the book, changing the list at line 500 means sliding every name below it, about half the book for the middle.
4. With the cards, finding the 500th card means following strings from the nearer end, about 500 steps for the middle.
5. With the cards, changing the list at a spot you hold means untying one string and tying two: 1 step.
6. Add the two parts. The cards win only when part one is free, because you already hold the spot, or the spot is the front or the back.
7. If part one is a walk, the cards pay in walking what the book pays in sliding. Then the book is the safer pick, because each name sits in its own numbered line.

### Name it

The usual list in Kotlin is the **ArrayList**, a list kept in one resizable array. The Kotlin collections overview says the default implementation of `MutableList` is `ArrayList`, which you can think of as a resizable array. The chain of cards is the **LinkedList**. On the JVM it is the Java class `java.util.LinkedList`, which its documentation calls a doubly-linked list: each node has a link forward and a link back.

The Java documentation for `ArrayList` says `get` runs in **constant time**, which means about 1 step however long the list is. It says `add` runs in **amortized constant time**, which means about 1 step on average over many adds. It says all other operations run in **linear time**, roughly speaking, which means about n steps for n names. The same page says the constant factor is low compared with `LinkedList`. The documentation for `LinkedList` says operations that index into the list traverse it from the beginning or the end, whichever is closer.

| Job | ArrayList (book) | LinkedList (cards) |
|---|---|---|
| Read the name at position i | about 1 step | up to about n / 2 steps |
| Add at the front | about n steps (slide) | about 1 step |
| Add in the middle, by position | about n / 2 steps (slide) | about n / 2 steps (walk), then 1 |
| Add next to a card you already hold | about n / 2 steps (slide) | about 1 step |

The Java pages give no step counts for the `LinkedList` column. Those counts are my own reasoning from how a chain works, and "about 1 step beside a card you hold" is general knowledge. The library class hides its cards, so holding one needs an **iterator**, which is a marker that moves along the list, or your own node class (my own reasoning).

This program counts the hops along a chain of nodes and compares them with one read from a list that sits in a row.

```kotlin
class Node(val value: Int, val next: Node?)

fun buildChain(n: Int): Node? {
    var head: Node? = null
    for (value in n downTo 1) {
        head = Node(value, head)
    }
    return head
}

fun hopsToReach(head: Node?, index: Int): Int {
    var current = head
    var hops = 0
    while (hops < index && current != null) {
        current = current.next
        hops++
    }
    return hops
}

fun main() {
    for (n in listOf(10, 100, 1000)) {
        val chain = buildChain(n)
        val row = List(n) { it + 1 }
        println("n = $n: chain hops ${hopsToReach(chain, n / 2)}, row reads 1 (value ${row[n / 2]})")
    }
}
```

It prints three lines. The chain needs 5, 50 and 500 hops to reach the middle. The row needs one read each time.

Here are the two real classes in use. `java.util.LinkedList` needs an import because Kotlin does not import it for you.

```kotlin
import java.util.LinkedList

fun main() {
    val cards = LinkedList<String>()
    cards.addFirst("Cy")
    cards.addFirst("Ben")
    cards.addFirst("Ada")
    val book = mutableListOf("Ada", "Ben", "Cy")
    book.add(0, "Zoe")
    println(cards)
    println(cards[2])
    println(book)
}
```

This prints `[Ada, Ben, Cy]`, then `Cy`, then `[Zoe, Ada, Ben, Cy]`. Both lists look the same from outside. The cost of each call is what differs.

## Predict the output

```challenge
type: predict
id: follow-three-strings
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, val next: Node?)

  fun main() {
      val chain = Node(10, Node(20, Node(30, Node(40, Node(50, null)))))
      var current: Node? = chain
      var hops = 0
      while (hops < 3) {
          current = current?.next
          hops++
      }
      println("value ${current?.value} after $hops hops")
  }
options:
  - id: a
    text: "value 40 after 1 hop"
    why: "You may have expected a jump straight to the card, like opening the book at a line. A chain has no jump. Every hop follows one string."
  - id: b
    text: "value 30 after 3 hops"
    why: "You may have counted the first card as a hop. You start on the first card, so three hops land on the fourth card, which holds 40."
  - id: c
    text: "value 40 after 3 hops"
    correct: true
    why: "You start at the card holding 10 and follow three strings: to 20, to 30, to 40. Reaching the fourth card costs three hops."
output: |
  value 40 after 3 hops
```

```challenge
type: predict
id: slide-the-names
verified: kotlin-2.4.21
code: |
  fun main() {
      val book = mutableListOf("Ada", "Ben", "Cy", "Dee")
      var slides = 0
      book.add("")
      for (i in book.lastIndex downTo 1) {
          book[i] = book[i - 1]
          slides++
      }
      book[0] = "Zoe"
      println(book)
      println("slides: $slides")
  }
options:
  - id: a
    text: "[Zoe, Ada, Ben, Cy, Dee] and slides: 4"
    correct: true
    why: "Every one of the four names moves down one line to make room on line 1. Four names, four slides, then Zoe is written on line 1."
  - id: b
    text: "[Zoe, Ada, Ben, Cy, Dee] and slides: 1"
    why: "You may have counted only the new name. The cost of the front insert is the names that slide, and all four of them do."
  - id: c
    text: "[Zoe, Ada, Ben, Cy, Dee] and slides: 0"
    why: "You may have thought a gap costs nothing. The gap on line 1 only appears because every name below it moved down."
output: |
  [Zoe, Ada, Ben, Cy, Dee]
  slides: 4
```

## Try it

1. **Write.** Copy the first program from Name it (the one with `buildChain`) into the editor.
2. **Run.** You should see three lines. The chain needs `5`, `50` and `500` hops to reach the middle, and the row always reads once. The values reached are `6`, `51` and `501`.
3. **Break.** Change `n / 2` in the call `hopsToReach(chain, n / 2)` to `n - 1` and change nothing else. Run it again. The chain now needs `9`, `99` and `999` hops, because the last card is as far from the front as a card can be. The row still reads once.
4. **Fix.** Put `n / 2` back and run it. The hops `5`, `50` and `500` return. Asking for a position is the move that costs the cards.

## Fix the bug

```challenge
type: fix
id: fix-song-at-in-a-loop
generated: true
verified: kotlin-2.4.21
code: |
  class Node(val value: Int, val next: Node?)
  var hops = 0
  class Playlist(val head: Node?, val size: Int) {
      fun songAt(index: Int): Int {
          var current = head
          repeat(index) {
              current = current?.next
              hops++
          }
          return current?.value ?: 0
      }
  }

  fun totalLength(playlist: Playlist): Int {
      var total = 0
      for (i in 0 until playlist.size) {
          total += playlist.songAt(i)
      }
      return total
  }

  fun main() {
      var head: Node? = null
      for (length in 100 downTo 1) {
          head = Node(length, head)
      }
      val playlist = Playlist(head, 100)
      println("total: ${totalLength(playlist)}")
      println("hops: $hops")
  }
hints:
  - "The total is right, but the hops are far too high. Look at the for loop in totalLength: how many hops does each call to songAt make?"
  - "A chain is cheap to walk once from the front and costly to ask by position. Walk the cards once and add up as you go."
expected: |
  total: 5050
  hops: 100
fix: |
  class Node(val value: Int, val next: Node?)

  var hops = 0

  class Playlist(val head: Node?, val size: Int)

  fun totalLength(playlist: Playlist): Int {
      var total = 0
      var current = playlist.head
      while (current != null) {
          total += current.value
          current = current.next
          hops++
      }
      return total
  }

  fun main() {
      var head: Node? = null
      for (length in 100 downTo 1) {
          head = Node(length, head)
      }
      val playlist = Playlist(head, 100)
      println("total: ${totalLength(playlist)}")
      println("hops: $hops")
  }
explanation: "The old code asked for each song by position, so it walked from the front again every time: 0 + 1 + 2 + ... + 99 hops. Walking the chain once costs one hop per card."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, the end of the chain is `null`, and the walk uses it to know when to stop. For question 4, `songAt` runs once for every song, and each run is a walk of its own.

## Tricks to remember

- Two parts to every insert: find the spot, then change the list there. The cards only win the second part.
- The book opens at a line number. The cards need a walk.
- Task: write down your last three list jobs as "find" and "change". Circle the ones where you already held the spot.
- Start with the book. Switch to cards only when you can say the reason aloud.

## Common mistakes

- **Thinking a linked list is always faster at insert and delete.** No sliding sounds like no cost. The walk to the spot can cost as much as the sliding. Count both parts.
- **Calling `get(i)` for every `i` in a loop over a `LinkedList`.** It reads like code for a row, so it looks fine. Each call walks from the nearer end again, so use a for-each loop that walks once.
- **Using a `LinkedList` as a queue or a stack.** Textbooks draw queues as chains, so it feels natural. The Java **`ArrayDeque`** (a class that adds and removes at both ends) documentation says it is likely to be faster than `LinkedList` when used as a queue, and Kotlin's `ArrayDeque` is a resizable array.
- **Comparing step counts only.** A smaller count feels like a win. The Java documentation for `ArrayList` says its constant factor is low compared with `LinkedList`, so measure when the counts are close.

## Where this shows up in Android

Imagine a chat screen that scrolls smoothly with 50 messages and stutters with 5,000. The code reads message i for each row with `get(i)` from a `LinkedList`, so each row walks from the front or back again. That is the Fix the bug problem, and the Build steps explain it (my own reasoning).

The Android reference for `LruCache` describes a cache (a store that keeps recent answers close), where each accessed value moves to the head of a queue, and when the cache is full the value at the end of the queue is evicted (thrown out to make room). The AOSP source of `LruCache` keeps its values in a `LinkedHashMap`, which the Java documentation calls a hash table and linked list implementation of the `Map` interface, with a doubly-linked list running through all of its entries. My own reasoning: moving one item to the front without sliding the others is the job the cards do well, and it is the part of this lesson called "tying in a card next to one you already hold".

## Say it in an interview

**Why does Kotlin use ArrayList by default and rarely suggest LinkedList?**
The Kotlin collections overview says the default `MutableList` implementation is `ArrayList`, a resizable array. The Java documentation says `get` takes constant time and that the constant factor of `ArrayList` is low compared with `LinkedList`. A row also keeps names in numbered lines, so reading by position is one step. I use `LinkedList` only when I have a reason.

**If inserting in the middle of a LinkedList is one step once you are at the node, why is `add(index, element)` still slow?**
The call has to find the node first. The Java documentation says indexed operations walk from the nearer end, so the walk is about n / 2 steps. The tying-in is cheap and the finding is not.

**Name a case where a doubly linked list beats an ArrayList.**
An LRU cache, where LRU means least recently used. On every read the item moves to the front. If a map hands me the node, I unlink it and link it at the front in about 1 step each, and no other item moves. An `ArrayList` would slide the items between (my own reasoning).

**Why prefer ArrayDeque over LinkedList for a queue or a stack in Kotlin?**
Kotlin's `ArrayDeque` can add and remove at both ends and fills the role of a stack and a queue. The Java `ArrayDeque` documentation says it is likely to be faster than `LinkedList` when used as a queue. It avoids one object per item (my own reasoning).

## Recap

1. An insert has two parts: finding the spot, and changing the list there.
2. A linked list changes the list in one step but may need a long walk to find the spot.
3. An ArrayList reads by position in one step, so it is the usual first choice.

## You can now

- Explain why inserting in the middle of a LinkedList by position is not cheap.
- Predict how many hops a chain needs to reach a card at a given position.
- Fix a loop that calls a position-based `get` on a chain so it walks the chain once.
- Count the slides an ArrayList makes when a name is added at the front of a short list.

## Practise on these

Say which structure you would pick and why before you code. The list runs from the gentlest to the hardest.

1. LeetCode 21, Merge Two Sorted Lists (Easy). Splice existing nodes together without copying them into a new row. https://leetcode.com/problems/merge-two-sorted-lists/
2. LeetCode 234, Palindrome Linked List (Easy). See the extra work needed when a chain has no read-by-position. https://leetcode.com/problems/palindrome-linked-list/
3. LeetCode 707, Design Linked List (Medium). Build `get`, add at the head, add at the tail, add at an index and delete at an index, and feel each cost. https://leetcode.com/problems/design-linked-list/
4. LeetCode 1472, Design Browser History (Medium). Compare a chain of pages against a row with a cursor for visit, back and forward. https://leetcode.com/problems/design-browser-history/
5. LeetCode 143, Reorder List (Medium). Find the middle, reverse half and merge, all without reading by position. https://leetcode.com/problems/reorder-list/
6. LeetCode 146, LRU Cache (Medium). The textbook case where a doubly linked list plus a map beats a row. https://leetcode.com/problems/lru-cache/
7. LeetCode 460, LFU Cache (Hard). Keep one doubly linked list per use count so eviction stays quick. https://leetcode.com/problems/lfu-cache/
