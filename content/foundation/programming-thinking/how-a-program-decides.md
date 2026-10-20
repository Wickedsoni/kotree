---
id: foundation/programming-thinking/how-a-program-decides
title: How does a program choose what to do next?
status: shadow
difficulty: easy
order: 14
requires: [foundation/programming-thinking/yes-or-no-questions]
summary: A program asks a yes-or-no question and follows one path. The steps on the other path are skipped.
video: ""
publishedOn:
updatedOn:
sims: [fork-and-gate]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 20 Oct 2026). Kotlin if, else, else if: https://kotlinlang.org/docs/control-flow.html ("To use if in Kotlin, add the condition to check within parentheses () and the action to take if the result is true within curly braces {}. You can use else and else if for additional branches and checks."; sample "if (heightAlice < heightBob) taller = heightBob" with no else). https://kotlinlang.org/docs/basic-syntax.html (if/else sample, maxOf). The control-flow page says nothing about the order of if / else if checks or about else being skipped when the condition is true, so the lesson teaches both as what a hand trace shows, not as a quoted rule. Android: https://developer.android.com/develop/ui/compose/state ("if you don't want to display the greeting if the name is empty, use the state in an if statement"); https://developer.android.com/develop/ui/compose/documentation ("Jetpack Compose is the modern toolkit for building Android UI"). No Kotlin compiler on the authoring machine: every output below is hand-traced, not run. -->
## The question

You open a weather app. When it rains, the app shows an umbrella. When it does not, it shows a sun. The app never shows both.

How does a program decide which of two things to do? And what happens to the steps it did not choose?

## Mental model

Think of a small ball rolling down a garden path.

- The ball is the program. It is always at one place on the path.
- The path is the list of steps, in order.
- The path splits at a fork. A sign at the fork says "left if raining, right if not".
- A gate at the fork swings toward the side the sign names.
- After a short way, the two paths meet again, and the ball rolls on.

The ball does not look at both paths. It reads the sign, the gate swings, and the ball takes one path.

**Where the analogy stops:** A walker in a garden can look at the sky, change their mind and turn back. A program reads the sign once, with the values it holds at that moment, and goes. Also, a garden fork has two paths. A program can have a fork with only one path. Then the ball just rolls on when the answer is no.

## See it move

<!-- SIM: fork-and-gate -->

**Try this:**
1. Drag the temperature slider and watch the gate swing before the ball rolls.
2. Add a second fork, then roll the ball with a temperature of 30 and again with 20.
3. Swap the order of the two signs and roll the ball with a temperature of 30 again.

**What you should notice:** The ball takes exactly one path, and the order of the signs can change which one.

## Build the logic

### Notice

The weather app has a fork with a sign: "raining? left to the umbrella, right to the sun."

- Monday it rains. The gate swings left. The ball passes the umbrella and rolls on. It never visits the sun.
- Tuesday it is dry. The gate swings right. The ball passes the sun and rolls on. It never visits the umbrella.

On both days the ball ends up on the same path after the paths meet.

### Guess

It is Monday and it rains. What does the ball do? Here are three ideas.

- A. It visits the umbrella, then walks back and visits the sun too.
- B. It visits the umbrella only. The steps on the sun path are skipped.
- C. It waits at the fork until the rain stops.

Idea A feels natural because a program reads steps one after another, so it seems to read every step. But the gate sends the ball down one path, and the other path is not walked. Idea C gives the ball a patience it does not have. The sign is read once. Idea B is the answer.

### Build

1. The ball reaches the fork.
2. Read the sign. It is a yes-or-no question, answered with the values the program holds right now.
3. If the answer is yes, the gate swings to the yes path.
4. If the answer is no, the gate swings to the other path. If there is no other path, the ball rolls straight on.
5. The ball walks the chosen path and no other.
6. The paths meet again, and the ball carries on with the next step.

Sometimes three or more paths are needed. Put a second fork on the "no" path. Now the ball asks the first sign. If the answer is yes, it leaves, and the second sign is never read. Only when the first answer is no does the ball reach the second sign.

So the order of the signs matters. Imagine a hot-weather sign saying "above 25" and a warm-weather sign saying "above 15". On a day of 30, the ball reads the first sign it meets. If that is "above 15", the answer is yes and the ball leaves. It never reaches the "above 25" sign. Put "above 25" first, and 30 reaches it.

### Name it

The keyword for a fork is **`if`**. After it, in round brackets, comes the **condition**: the yes-or-no question. After the brackets, in curly braces, come the steps for the yes path. The Kotlin documentation describes it this way: add the condition within parentheses and the action to take if the result is true within curly braces.

The path for the no answer starts with **`else`**. A second fork on the no path is written **`else if`**. Each path out of a fork is a **branch**. The documentation says you can use `else` and `else if` for additional branches and checks. An `if` with no `else` is allowed. The documentation has a sample with no `else`: when the condition is `false`, nothing extra happens.

This program has three branches.

```kotlin
fun main() {
    val temp = 20
    if (temp > 25) {
        println("Hot")
    } else if (temp > 15) {
        println("Warm")
    } else {
        println("Cool")
    }
    println("Done")
}
```

It prints `Warm` and then `Done`. The first sign asks "above 25?" and 20 is not. The ball moves to the second sign, "above 15?", and the answer is yes. It walks that branch only. Then the paths meet and `Done` prints, whatever the weather.

## Predict the output

```challenge
type: predict
run: false
id: temp-thirty
verified: not-run
code: |
  fun main() {
      val temp = 30
      if (temp > 25) {
          println("Hot")
      } else if (temp > 15) {
          println("Warm")
      } else {
          println("Cool")
      }
      println("Done")
  }
options:
  - id: a
    text: "Hot, then Warm, then Done"
    why: "You may have expected both signs to say yes, so both paths run. The ball leaves at the first yes, and the second sign is never read."
  - id: b
    text: "Hot, then Done"
    correct: true
    why: "30 is above 25, so the gate swings to the Hot path at the first sign. The ball walks that path only, and then rolls on to Done."
  - id: c
    text: "Warm, then Done"
    why: "You may have noticed that 30 is above 15 too. But the ball meets the above-25 sign first, and it leaves there."
output: "Hot, then Done"
```

```challenge
type: predict
run: false
id: signs-in-wrong-order
verified: not-run
code: |
  Goal: a day of 30 should print Hot.
  Someone else wrote this:

  fun main() {
      val temp = 30
      if (temp > 15) {
          println("Warm")
      } else if (temp > 25) {
          println("Hot")
      }
      println("Done")
  }
options:
  - id: a
    text: "Hot, then Done"
    why: "You may have read the goal instead of the code. The above-15 sign comes first here, and 30 passes it."
  - id: b
    text: "Warm, then Done"
    correct: true
    why: "30 is above 15, so the ball leaves at the first sign. The above-25 sign is never read, so Hot can never print for any day."
  - id: c
    text: "Warm, then Hot, then Done"
    why: "You may have expected both signs to run. Only one path is walked after a fork."
output: "Warm, then Done"
```

```challenge
type: predict
run: false
id: fork-with-one-path
verified: not-run
code: |
  fun main() {
      val temp = 10
      if (temp > 15) {
          println("Warm")
      }
      println("Done")
  }
options:
  - id: a
    text: "Warm, then Done"
    why: "You may have thought the Warm line runs anyway. The answer is no, so the gate keeps the ball off that path."
  - id: b
    text: "Done"
    correct: true
    why: "The answer is no and the fork has no other path, so the ball rolls straight on. Only the Done step runs."
  - id: c
    text: "The program stops, because there is no else"
    why: "You may have expected the missing path to be a problem. A fork with one path is allowed. The ball just rolls on."
output: "Done"
```

## Tricks to remember

- One fork, one path.
- The first yes wins. After that, the gate is shut behind the ball.
- Put the strictest sign first. A strict sign says "above 25". A loose sign says "above 15".
- No else means no second path. When the answer is no, the ball keeps rolling.

## Common mistakes

- **Thinking both sides of the fork run, one after the other.** A program reads lines in order, so it feels as if every line gets read. After a fork, only the chosen path is walked. The other path's steps never run.
- **Putting the loose sign first.** "Above 15" feels like the natural place to start. But then 30 leaves at that sign, and the "above 25" sign never gets a turn. Put the strictest sign first.
- **Expecting something to happen on the no answer when there is no else.** If you wrote only the yes path, the no answer has nowhere to go. The ball rolls on and nothing is shown. Add an `else` when the no answer needs its own steps.

## Where this shows up in Android

A profile screen should greet the user by name. When the name box is empty, the screen shows a stray "Hello, " with nothing after it. The screen is wrong because nobody asked "is there a name?" before drawing the greeting.

The Android documentation for **Jetpack Compose**, the modern toolkit for building Android UI, shows how to avoid this. Its state page says that if you do not want to display the greeting when the name is empty, you use the state in an `if` statement. That is one fork with one path. When the answer is yes, the ball walks the path that draws the greeting. When the answer is no, there is no other path, and the greeting is left out.

## Recap

1. A program asks a yes-or-no question and follows one path out of the fork.
2. The first yes wins, so the order of the questions matters.
3. A fork with no else means that the answer no skips the steps and rolls on.

## You can now

- Predict which message prints when a chain of if and else if checks meets a given value.
- Spot an if chain whose order makes one message impossible to reach.
- Predict what a program prints when an if has no else and the answer is no.
