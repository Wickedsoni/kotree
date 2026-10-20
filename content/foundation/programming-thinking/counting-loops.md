---
id: foundation/programming-thinking/counting-loops
title: How do I repeat something exactly ten times?
status: shadow
difficulty: medium
order: 16
requires: [foundation/programming-thinking/how-a-program-repeats]
summary: A counting loop names its first and last number up front. It runs once for each number in between, last number included.
video: ""
publishedOn:
updatedOn:
sims: [numbered-stairs-loop]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 20 Oct 2026). Ranges: https://kotlinlang.org/docs/ranges.html ("A range represents an ordered set of values with a defined start and end. By default, it increments by 1 at each step. For example, 1..4 represents the numbers 1, 2, 3, and 4."; the .. operator "includes both the start and end values"; the ..< operator "includes the start value but excludes the end value"; samples "for (i in 1..4) print(i) // 1234", "for (i in 4 downTo 1) print(i) // 4321", "for (i in 0..8 step 2) print(i)" giving 02468; "To iterate numbers in reverse order, use the downTo function instead of .."). For over a range: https://kotlinlang.org/docs/control-flow.html ("To iterate over a range of numbers, use a range expression with .. and ..< operators"; "for (i in 1..6)" prints 123456 and "for (i in 1..<6)" prints 12345). The output of 1..<4 (123) is derived from the 1..<6 sample, not quoted. Android Compose lists: https://developer.android.com/develop/ui/compose/lists (Column with messages.forEach; LazyColumn with items(5) { index -> Text("Item: $index") } commented "Add 5 items"; the page uses forEach and items, not a for loop). No Kotlin compiler on the authoring machine: every output below is hand-traced, not run. -->
## The question

A game gives the player ten lives, and the screen must show ten hearts. You could write the heart-drawing step ten times. That is slow, and the day the game gives twelve lives, you have to find every copy.

How do you tell a program to repeat something exactly ten times, no more and no fewer? And how do you know which numbers it counts?

## Mental model

Think of climbing a staircase where every step has a number painted on it.

- The staircase is the whole job, from start to finish.
- The first step is where you start. The last step is where you finish.
- The number painted on the step you stand on is the number you are on right now.
- Saying that number aloud is the work you do on each step.

You know the first step and the last step before you take a single step. So you also know how many steps you will climb. You never have to look up and ask "am I at the top yet?", because the painted numbers tell you.

**Where the analogy stops:** A real staircase has a top step you can see, and a landing beyond it. A range of numbers is only a rule that hands out one number at a time. It is you who decides whether the last number counts. Also, a real staircase goes one step at a time. A rule for numbers can skip steps or go down.

## See it move

<!-- SIM: numbered-stairs-loop -->

**Try this:**
1. Set the first step to 1 and the last step to 10, then press Run. Watch the number shown on each step.
2. Switch from "include the last step" to "stop below the last step" and run again.
3. Switch to climbing down and run again.

**What you should notice:** Each number is handed out once, and stopping below the last step leaves the top number out.

## Build the logic

### Notice

A staircase has ten steps, painted 1 to 10. You climb it and say each number aloud as you stand on the step: 1, 2, 3, and so on up to 10.

You said ten numbers, so you climbed ten steps. You knew that before you set off, because you knew the first step and the last step.

### Guess

The rule says "climb from step 1 to step 10 and say each number". Which numbers do you say? Here are three ideas.

- A. 1 to 9. The top step is where you stop, so you do not say its number.
- B. 1 to 10. You stand on both the first step and the last step, and you say both numbers.
- C. 2 to 10. You start by stepping up from the floor, so step 1 is already behind you.

Idea A feels right because "up to 10" can sound like "stopping before 10". But the rule names step 10 as the last step. You stand on it, so you say its number. Idea C counts the floor as step 1, and the rule did not say that. Idea B is the answer.

### Build

1. Before you start, decide the first step number and the last step number.
2. Stand on the first step.
3. Do the job for this step. Say its number.
4. If you are on the last step, stop. If not, move to the next step number.
5. Go back to step 3.
6. The staircase does the counting. You never write "add one" yourself.

You can change the rule in three ways.

- To leave the top step out, say "stop one below the last step". Then the last number you say is one smaller.
- To climb down, start at the top step and move down one number each time.
- To skip steps, move two numbers at a time, so you stand on steps 1, 3, 5 and so on.

### Name it

A loop that runs once for each number in a rule is a **`for`** loop. The rule is a **range**: an ordered set of numbers with a start and an end. The Kotlin documentation says a range increments by 1 at each step by default, and that `1..4` stands for the numbers 1, 2, 3 and 4.

The loop looks like this: `for (i in 1..10)`. The word `in` means "take each number of the range, one at a time". The name `i` is the **loop variable**. It holds the number painted on the step you are standing on. Kotlin sets it for you at the start of every round, so you do not write a line that adds one.

The two dots include both ends. The documentation says `..` includes both the start and the end value. The symbol `..<` includes the start but leaves out the end. Here are the four rules from the Build steps.

| Rule in words | Kotlin | Numbers it hands out |
|---|---|---|
| first to last, both included | `1..4` | 1, 2, 3, 4 |
| stop one below the last | `1..<4` | 1, 2, 3 |
| count down | `4 downTo 1` | 4, 3, 2, 1 |
| skip steps | `0..8 step 2` | 0, 2, 4, 6, 8 |

So `1..10` hands out ten numbers, and `1..<10` hands out nine.

This program says each number from 1 to 4, then says it is done.

```kotlin
fun main() {
    for (i in 1..4) {
        println(i)
    }
    println("Done")
}
```

It prints `1`, `2`, `3`, `4` and `Done`, one per line. The loop variable `i` is 1 in the first round and 4 in the last. After the last round, the loop is over and `Done` prints.

## Predict the output

```challenge
type: predict
run: false
id: last-number-printed
verified: not-run
code: |
  fun main() {
      for (i in 1..10) {
          println(i)
      }
  }
  // Which is the last number printed?
options:
  - id: a
    text: "9"
    why: "You may have read the range as stopping before the end. Two dots include both ends, so you stand on step 10 too."
  - id: b
    text: "10"
    correct: true
    why: "Two dots hand out every number from the first to the last, last one included. The last round says 10."
  - id: c
    text: "11"
    why: "You may have expected the loop to go one step past the top. The staircase ends at step 10, so no step 11 exists."
output: "10"
```

```challenge
type: predict
run: false
id: sum-misses-five
verified: not-run
code: |
  Goal: add up 1, 2, 3, 4 and 5, which makes 15.
  Someone else wrote this:

  fun main() {
      var total = 0
      for (i in 1..<5) {
          total = total + i
      }
      println(total)
  }
options:
  - id: a
    text: "15"
    why: "You may have read the goal instead of the code. The range stops one below 5, so step 5 is never visited."
  - id: b
    text: "10"
    correct: true
    why: "The steps visited are 1, 2, 3 and 4, and 1 + 2 + 3 + 4 is 10. The top step is left out, so the total is short."
  - id: c
    text: "4"
    why: "You may have taken the last number reached. The total adds up every number visited, not only the last one."
output: "10"
```

```challenge
type: predict
run: false
id: count-down-to-go
verified: not-run
code: |
  fun main() {
      for (i in 3 downTo 1) {
          println(i)
      }
      println("Go")
  }
options:
  - id: a
    text: "1, 2, 3, then Go"
    why: "You may have expected the numbers to appear in the order they are written. The word downTo starts at the first number and moves down."
  - id: b
    text: "3, 2, 1, then Go"
    correct: true
    why: "The climb starts on step 3 and moves down one number each round. It ends on step 1, and then Go prints."
  - id: c
    text: "3, 2, 1, 0, then Go"
    why: "You may have counted down to zero, like a rocket launch. This rule ends on step 1, so 0 is never visited."
output: "3, 2, 1, then Go"
```

## Tricks to remember

- Know the first step and the last step before you start.
- Two dots keep the last number. Two dots with a less-than sign drop it.
- Ten painted steps, from 1 to 10, mean ten rounds.
- To go down, say down to. To skip, say step.

## Common mistakes

- **Thinking `1..10` stops at 9.** "Up to 10" often means "before 10" in daily talk, so this feels right. The documentation says the two dots include both the start and the end value. Use `..<` when you want to leave the end out.
- **Mixing up the two forms when you need an exact count.** `1..<10` looks almost the same as `1..10`, and it hands out nine numbers, not ten. Write the numbers out on paper and count them before you trust the rule.
- **Writing the range backwards to count down.** `3..1` reads naturally as "from 3 to 1". The documentation says to use `downTo` instead of `..` to go in reverse order.

## Where this shows up in Android

A chat screen has ten messages, but only nine rows appear. The last message is missing, and nobody sees an error.

The Android documentation for lists in **Jetpack Compose**, the modern toolkit for building Android UI, builds rows with a call such as `items(5)`, which its code comment says adds 5 items, and with `forEach` over a list. It does not use a `for` loop there. The idea is the same one you met here: the app must know how many rows to draw, from the first to the last. This part is our own reasoning. If the count stops one short, as in the `1..<5` item above, the last row is never drawn. The program ran without complaint, but the staircase ended one step too early.

## Recap

1. A counting loop knows its first and last number before it starts.
2. Two dots include the last number, and dots with a less-than sign leave it out.
3. The loop variable holds the current number, and downTo and step change the direction and the gap.

## You can now

- Predict the last number a for loop over a range prints.
- Spot a range that stops one step short of the number you wanted.
- Predict what a loop with downTo prints.
