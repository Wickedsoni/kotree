---
id: foundation/programming-thinking/what-is-a-variable
title: How does a program remember a value?
status: live
difficulty: easy
order: 10
requires: [foundation/first-code/lines-run-in-order, foundation/how-computers-work/memory-vs-storage]
summary: A variable is a named place that holds one value. Give it a new value and the old one is gone.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [labelled-jars]
playground: false
fruit: false
upnext: false
---

## The question

You play a game and the score shows 0. You score a goal and it shows 1. You score again and it shows 2.

Where did the game keep that number between goals? And when the new number arrived, what happened to the old one?

## Mental model

Think of a labelled jar on a shelf.

- The shelf is the computer's **memory**, the place where it keeps what it is working on right now.
- A jar on the shelf is one place in that memory.
- The label on the jar is the name you give it, such as "score".
- A ball inside the jar has a number on it. That ball is what the jar holds.
- A jar holds exactly one ball.
- To read a jar, you look at the number on the ball. The ball stays where it is.
- To change a jar, you drop in a new ball. The old ball falls out and is gone.

**Where the analogy stops:** A real jar can hold many balls, and you can pick up the one that fell out. Here, a jar holds one ball, and the old one is lost for good. Also, a ball in a real jar is a thing you can touch. In the computer it is only a number stored in memory.

## See it move

```sim
id: labelled-jars
template: stepper
title: Labelled jars on a shelf
intro: "The shelf is empty. The program below will put a ball with a number into each jar, one line at a time."
lines:
  - { id: l1, text: "var score = 0" }
  - { id: l2, text: "score = 5" }
  - { id: l3, text: "var halfTime = score" }
  - { id: l4, text: "score = score + 1" }
  - { id: l5, text: "println(score)" }
  - { id: l6, text: "println(halfTime)" }
code: true
board:
  - { var: score, label: "Jar: score" }
  - { var: halfTime, label: "Jar: halfTime" }
log: { columns: [Line, Jar, Ball before, Ball after] }
vars: { pc: 1, score: "no jar yet", halfTime: "no jar yet" }
actions:
  - id: step
    label: Run the next line
    cases:
      - when: { var: { pc: 1 } }
        do:
          - set: { score: 0, pc: 2 }
          - light: [ l1 ]
          - row: [ "1", "score", "no jar yet", "0" ]
          - say: "A jar labelled score appears and gets a first ball with 0 on it."
      - when: { var: { pc: 2 } }
        do:
          - set: { score: 5, pc: 3 }
          - light: [ l2 ]
          - row: [ "2", "score", "0", "5" ]
          - say: "A new ball with 5 drops into the score jar. The old ball with 0 falls out and is gone."
      - when: { var: { pc: 3 } }
        do:
          - set: { halfTime: 5, pc: 4 }
          - light: [ l3 ]
          - row: [ "3", "halfTime", "no jar yet", "5" ]
          - say: "A second jar, halfTime, gets a fresh ball with 5 on it, copied from score. The two jars are separate."
      - when: { var: { pc: 4 } }
        do:
          - set: { score: 6, pc: 5 }
          - light: [ l4 ]
          - row: [ "4", "score", "5 (read first)", "6" ]
          - say: "Kotlin reads the old ball, 5, and works out 6. Then the new ball drops in and the 5 ball falls out. The halfTime jar still holds 5."
      - when: { var: { pc: 5 } }
        do:
          - set: { pc: 6 }
          - light: [ l5 ]
          - row: [ "5", "score (read)", "6", "6 (unchanged)" ]
          - say: "println reads the score jar and prints 6. Reading does not change the jar."
      - when: { var: { pc: 6 } }
        do:
          - set: { pc: 7 }
          - light: [ l6 ]
          - row: [ "6", "halfTime (read)", "5", "5 (unchanged)" ]
          - say: "println prints 5. The halfTime jar kept its own ball when score changed."
      - when: { var: { pc: 7 } }
        do:
          - say: "The program has finished. Press Reset to run it again."
script: [ step, step, step, step, step, step ]
try:
  - text: "Press Step twice. Line 1 makes the score jar. Line 2 drops in a new ball, and the old ball falls out."
    done: [ step, step ]
  - text: "Press Step twice more. Line 3 copies score into a second jar. Line 4 builds a new number from the old one: the old ball is read first, then the new ball drops in."
    done: [ step, step ]
  - text: "Press Step twice more to print both jars. The halfTime jar kept its own ball."
    done: [ step, step ]
```

**What you should notice:** A new ball replaces the old one, and a copy keeps its own ball.

## Build the logic

### Notice

The "score" jar holds a ball with 0 on it. You score a goal. A new ball with 1 on it drops into the jar, and the 0 ball falls out. You score again. A ball with 2 drops in, and the 1 ball falls out.

At half-time you want to remember the score. You write the same number on a fresh ball and drop it into a second jar, labelled "half-time". Then the match goes on and "score" changes. The "half-time" jar does not change.

### Guess

When you drop a new ball into a jar that already holds one, what happens to the old ball?

- A. Both balls stay in the jar, so the jar keeps a history.
- B. The old ball falls out and is gone, and only the new ball is left.
- C. The computer refuses, because the jar is already used.

Idea A would need a bigger container than a jar, because a jar holds one ball. Idea C is believable, because the jar is full, but the whole point of a jar is that you can change it. Idea B is the answer.

Here is a second question. To add one to the score, the computer must make the new ball from the old ball's number. In what order?

- A. Drop the new ball first, then read the jar.
- B. Read the old ball, work out the new number, then drop the new ball in.
- C. It cannot be done, because a number cannot be one more than itself.

Idea A reads the jar after the old ball is already gone. Idea C is a very common guess. It treats the step as a claim that two things are equal. It is a step to carry out, and idea B is how it runs.

### Build

1. Pick a label for the jar. Choose a name that says what the jar holds.
2. Put a first ball in at the start. A jar must get a ball before anyone reads it.
3. To read the jar, look at the number on the ball. Reading never changes the jar.
4. To change the jar, drop in a new ball. The old ball falls out.
5. To make the new number from the old one, read the old ball first and work out the new number. Only then drop the new ball in.
6. To copy a jar, write the number on a fresh ball and drop it into a second jar. The two jars are now separate.

### Name it

A jar is a **variable**: a named place in memory that holds one **value**. The label is the name of the variable. The ball's number is its value.

Creating a variable is called **declaring** it. In Kotlin, you declare one with the keyword `var`, then the name, then `=`, then the first value. The Kotlin documentation says `var` is for variables that can be reassigned.

Putting a new value into a variable is **assignment**. It is also written with `=`. Here `=` does not mean "is equal to". It means "put this into the jar". So `score = score + 1` reads: work out the right side using the old value, then store the answer in `score`.

Here is a program that traces the jar.

```kotlin
fun main() {
    var score = 0
    println(score)
    score = 5
    println(score)
    score = score + 1
    println(score)
}
```

Trace it the same way as the sim, one line at a time. This program is a different one from the sim's. The first `println` prints `0`. After `score = 5`, the old ball is gone and the next `println` prints `5`. In `score = score + 1`, Kotlin reads the 5, works out 6, and stores it. The last `println` prints `6`.

The keyword `val` makes a jar that accepts one ball only. The Kotlin documentation says a `val` is assigned a value only once and cannot be reassigned. Kotlin refuses to run this program.

```kotlin
// does not compile
fun main() {
    val score = 0
    score = 1
}
```

The same page says you can use a variable only after it has been given a value.

## Predict the output

```challenge
type: predict
run: false
id: x-after-three-assignments
verified: not-run
code: |
  fun main() {
      var x = 1
      x = 4
      x = 2
      println(x)
  }
options:
  - id: a
    text: "7"
    why: "You may have added up all the balls that went in. A jar holds one ball, so only the last one is left."
  - id: b
    text: "2"
    correct: true
    why: "Each new ball pushes the old one out. The last ball to go in has 2 on it, so that is what the jar holds."
  - id: c
    text: "1"
    why: "You may have thought the first ball stays. A new ball replaces the old one every time, so the first ball left long ago."
output: "2"
```

```challenge
type: predict
run: false
id: total-grows-twice
verified: not-run
code: |
  fun main() {
      var total = 10
      total = total + 5
      total = total + 5
      println(total)
  }
options:
  - id: a
    text: "10"
    why: "You may have thought the jar returns to its first ball. Each line builds on whatever ball is in the jar now."
  - id: b
    text: "15"
    why: "You may have started both lines from 10. The second line reads the jar after the first line, so it reads 15, not 10."
  - id: c
    text: "20"
    correct: true
    why: "The first line reads 10 and drops in 15. The second line reads that 15 and drops in 20."
output: "20"
```

```challenge
type: predict
run: false
id: copy-keeps-its-ball
verified: not-run
code: |
  fun main() {
      var jar = 3
      var copy = jar
      jar = 9
      println(jar)
      println(copy)
  }
options:
  - id: a
    text: "9 and 9"
    why: "You may have thought the copy follows the jar. The copy got its own fresh ball when it was made, so it does not follow."
  - id: b
    text: "9 and 3"
    correct: true
    why: "The copy was made when the jar held 3, with its own ball. Changing the jar to 9 does not touch the copy."
  - id: c
    text: "3 and 3"
    why: "You may have thought the jar still holds its first ball. The line that sets the jar to 9 replaced it, so the first print shows 9."
output: |
  9
  3
```

## Tricks to remember

- One jar, one ball.
- Reading looks. Assigning replaces.
- Work out the right side first, then drop the result in the jar.
- A copy is a fresh ball. It does not follow the original.

## Common mistakes

- **Reading `score = score + 1` as a maths equation.** At school, `=` means "is equal to", so a number that equals itself plus one looks impossible. In code, the line is a step: read the jar, add one, put the answer back.
- **Thinking a copy stays linked to the original.** Right after copying, both jars show the same number, so they look like one thing. A copy is a new ball. Change one jar and the other stays as it was.
- **Reading a jar before a ball is in it.** An empty jar feels like it should hold 0. The Kotlin documentation says you can use a variable only after it has been given a value. Put a first ball in when you make the jar.

## Where this shows up in Android

Picture the cart count on a shopping app. You tap Add, and the number on the badge goes from 2 to 3. The app keeps that count in a variable. The new number replaces the old one.

The Android documentation says that state in an app is any value that can change over time. The cart count is such a value. The jar changes first, and then the app updates the badge to match.

## Recap

1. A variable is a named place in memory that holds one value.
2. Assigning a new value replaces the old one, which is lost.
3. A line such as `score = score + 1` reads the old value, then stores the new one.

## You can now

- Predict the value of a variable after several assignments.
- Explain why `score = score + 1` is a step and not a broken equation.
- Predict whether a copy changes when the original does.
