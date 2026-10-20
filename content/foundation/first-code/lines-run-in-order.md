---
id: foundation/first-code/lines-run-in-order
title: In what order does a computer run my lines?
status: live
difficulty: easy
order: 9
requires: [foundation/first-code/run-your-first-program]
summary: A program runs its lines one at a time, from the top down. Swap two lines and the printed result swaps too.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [line-by-line-stepper]
playground: false
fruit: false
upnext: false
---

## The question

You write a note for a friend: "Open the door. Walk in. Say hello." Your friend does the steps in that order. Now you shuffle the note: "Say hello. Walk in. Open the door." Your friend talks to a closed door.

A program is a list of steps like that note. When a computer reads yours, does it do all the steps at once, or one by one? And what changes if you swap two of them?

## Mental model

Think of a queue at a ticket counter.

- The clerk is the computer. There is one clerk.
- The people in the queue are the lines of your program, standing in the order you wrote them.
- The person at the front is the next line to run.
- A notice board hangs beside the counter. It is where results show up.
- When a person is served, they write one message on the board and walk away. The next person steps forward.

The clerk never serves two people at once. The clerk never serves someone from the middle of the queue while the front person waits.

**Where the analogy stops:** A real queue can have people pushing in, leaving, or a second counter serving two people at the same time. This page has none of that. Also, real people choose where to stand. In a program, you choose the order when you write it. Some lines can send the clerk somewhere else, but this page covers only the straight path.

## See it move

<!-- SIM: line-by-line-stepper -->

**Try this:**
1. Press Step and watch the bar move down the lines, one line each press. Watch the board fill up.
2. Drag the last line to the top. Press Step until the bar reaches the end. Compare the board with the first run.
3. Drag two lines so a greeting reads backwards on the board.

**What you should notice:** The bar visits one line at a time from the top, and the board fills in that same order.

## Build the logic

### Notice

The counter has three people. Ann says "Ready". Bo says "Set". Cy says "Go".

The clerk serves Ann first, and the board shows Ready. Then Bo steps up, and the board adds Set. Then Cy, and the board adds Go. Read from top to bottom, the board says Ready, Set, Go.

### Guess

Now Cy and Ann swap places. The queue is Cy, Bo, Ann. What will the board show?

- A. Still Ready, Set, Go, because the clerk knows the right order.
- B. Go, Set, Ready, because the board follows the queue.
- C. All three messages at once, in a jumble.

Idea A gives the clerk knowledge that was never written down. The clerk has only the queue. Idea C gives the clerk three pairs of hands. There is one clerk, so one person at a time. Idea B is the answer.

### Build

1. The clerk serves only the person at the front.
2. That person writes their message on the board and leaves.
3. The next person steps forward.
4. This repeats until nobody is left.
5. So the board shows the messages in queue order. Change the queue and you change the board. Nothing else changes.
6. A person can only rely on what the people ahead of them have already done.

### Name it

Each line that does one job is a **statement**. A program carries out its statements from the top down, one at a time.

What a program shows or prints is its **output**. In Kotlin, **`println`** prints the text inside its brackets, then moves to a new line. **`main()`** is where a Kotlin program starts, so its first line runs first.

```kotlin
fun main() {
    println("Ready")
    println("Set")
    println("Go")
}
```

Read it as a queue of three people. This program prints `Ready`, then `Set`, then `Go`, each on its own line. Swap the first and last `println` lines, and the output becomes `Go`, `Set`, `Ready`.

## Predict the output

```challenge
type: predict
run: false
id: ready-set-go
verified: not-run
code: |
  fun main() {
      println("Ready")
      println("Set")
      println("Go")
  }
options:
  - id: a
    text: "Go, Set, Ready, each on its own line"
    why: "You may have read the queue from the back. The clerk serves the front person first, so the top line prints first."
  - id: b
    text: "Ready, Set, Go, each on its own line"
    correct: true
    why: "The clerk serves the queue from the front. The top line runs first, and each println moves to a new line."
  - id: c
    text: "Ready Set Go on one line"
    why: "You may have expected the messages to run together. Each println ends its line, so each message lands on its own row of the board."
output: |
  Ready
  Set
  Go
```

```challenge
type: predict
run: false
id: greeting-backwards
verified: not-run
code: |
  fun main() {
      // The author wanted Hello first, then Sam.
      println("Sam")
      println("Hello")
  }
options:
  - id: a
    text: "Hello, then Sam"
    why: "You may have expected the clerk to follow the note on the first line. That note is only for people. The clerk serves the lines in the queue order."
  - id: b
    text: "Sam, then Hello"
    correct: true
    why: "Sam is first in the queue, so Sam writes on the board first. The wish in the note changes nothing."
  - id: c
    text: "The program refuses to run because the order looks odd"
    why: "You may have expected the clerk to notice the mix-up. The clerk cannot tell what you meant, so the lines run as written."
output: |
  Sam
  Hello
```

```challenge
type: predict
run: false
id: last-line-to-the-top
verified: not-run
code: |
  fun main() {
      // Before: one, two, three, four.
      // The last line was moved to the top.
      println("four")
      println("one")
      println("two")
      println("three")
  }
options:
  - id: a
    text: "one, two, three, four"
    why: "You may have followed the old order in the note. The queue is what counts, and four now stands at the front."
  - id: b
    text: "four, one, two, three"
    correct: true
    why: "Four moved to the front of the queue, so it is served first. The others keep their order behind it."
  - id: c
    text: "four, three, two, one"
    why: "You may have thought moving one person reverses the whole queue. Only one person moved. The rest stay in their order."
output: |
  four
  one
  two
  three
```

## Tricks to remember

- First in the queue, first on the board.
- Swap two people, swap two messages.
- One clerk serves one person at a time.
- Trace with your finger from the top line, never from the line you are curious about.

## Common mistakes

- **Thinking all the lines happen at the same time.** A program runs so fast that the results seem to appear together. But the computer follows your lines one after another. Next time, put your finger on the top line and move down one line at a time.
- **Expecting the computer to put the lines in a sensible order.** A friend would fix a muddled note without being asked. The clerk has only the queue, so the lines run as written. If the order is not what you want, move the line yourself.
- **Starting at the line you care about.** It feels quicker to jump to the line that prints the odd result. But that line depends on everything ahead of it. Start at the top and trace down.

## Where this shows up in Android

Picture a shopping app that shows "Order placed", and a moment later shows "Payment did not go through". One possible cause is plain: the message line was written before the payment check. The steps ran in the order they were written. The fix is to move one line, not to add a new one.

The Android documentation says Kotlin is fully supported for Android development. This page teaches only the straight top-to-bottom path.

## Recap

1. A program runs its statements one at a time, from the top down.
2. The output follows that order, so swapping two lines swaps the output.
3. The computer does not fix the order for you, so you write it.

## You can now

- Predict the output of a short program by following its lines from the top.
- Explain why swapping two lines swaps the output, using the queue.
- Predict the output after a line is moved to the top.
