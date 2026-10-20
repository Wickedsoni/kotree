---
id: foundation/programming-thinking/trace-code-by-hand
title: How can I know what code prints before I run it?
status: shadow
difficulty: medium
order: 21
requires: [foundation/programming-thinking/loop-over-a-list, foundation/programming-thinking/function-inputs-and-outputs]
summary: Write down what each line does to your numbers, one row at a time, and you can name the output without a computer.
video: ""
publishedOn:
updatedOn:
sims: [trace-table-stepper]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/functions.html (fun keyword, parameters as name: Type, return, calling a function; sample double(x: Int)); kotlinlang.org/docs/basic-syntax.html (var, println); kotlinlang.org/docs/control-flow.html (for, while); developer.android.com/studio/debug (debugger pauses at a line; examine variables, continue line by line); developer.android.com/develop/ui/compose/state (State in an app is any value that can change over time). Tracing as a method is textbook. Not compiled: no kotlinc here; every output traced by hand twice. -->

## The question

A friend sends you five lines of code and asks what it prints. You have no computer with you, only a pencil and paper. Can you still work out the answer? And how do you keep track of numbers that change on every line?

## Mental model

Think of walking a treasure map with a pencil.

- The map is the program. Each mark on the trail is one line.
- You are the walker. You take one step at a time, in the order the map gives.
- A pencil cross shows the line you are standing on.
- A pouch hangs at your belt. It holds one purse for each name the map uses, and each purse holds one number.
- A logbook has one row for every step. In each row you write what every purse holds after that step.
- Sometimes the map says "take this number to the helper on the trail called double". That trail is a detour. You walk the detour, carry one number back from the helper, and return to the cross you left.
- When the map says to say a number aloud, the program prints it. What gets said aloud is the output.

**Where the analogy stops:** A real walker sees the whole land. A program shows you nothing except the lines, and a line does nothing until you reach it. Also, the computer keeps no logbook. It swaps the number in a purse and forgets the old one at once. The logbook is your tool, and it is how you keep the old numbers in sight.

## See it move

<!-- SIM: trace-table-stepper -->

**Try this:**
1. Before each press of Step, write in your logbook what you think the next row will be. Then press Step and compare.
2. When the cross reaches the detour, press Step and watch which purse gets the number carried back.
3. Change the number in the first purse and trace the program again.

**What you should notice:** Most rows change one purse and copy the rest, and the last row holds what the program says aloud.

## Build the logic

### Notice

Here is a plan for a walk. Start with 0 coins in the purse called total. Walk three rounds, with your round number going 1, 2, then 3. In each round, take your round number to a helper who doubles it. Add the doubled number to total. At the end, say the total aloud.

Your logbook looks like this.

| Step | Round number | Coins in total | Said aloud |
|---|---|---|---|
| start | - | 0 | |
| round 1 | 1 | 2 | |
| round 2 | 2 | 6 | |
| round 3 | 3 | 12 | |
| end | 3 | 12 | 12 |

The helper handed back 2, then 4, then 6. Each time, those coins went on top of what the purse already held.

### Guess

Look at round 2. The purse holds 2 coins. The round number is 2, so the helper hands back 4. The map says "add the doubled number to total". How many coins are in the purse after that line?

- A. 6. The purse keeps its 2 coins and takes 4 more.
- B. 4. The doubled number replaces what was in the purse.
- C. 2. The add has not changed anything yet.

Idea B forgets the old coins. The map said add, not replace. Idea C reads the row above and not the new row. Idea A is the answer.

### Build

1. Make a column for every purse the map names. Write the starting number of each purse in the first row.
2. Put the cross on the first line.
3. Do that one line only. Add a row. Write the new number for the purse the line changed. Copy every other purse unchanged.
4. If the line is a detour, walk the helper's lines with the same method. When the helper hands a number back, carry it to the line you left and use it there.
5. If the map says to repeat, move the cross back to the top of the repeated part. Keep the purses as they are. Only a line that says so changes a purse.
6. Whenever a line says to say something aloud, write it in a "said aloud" column.
7. When the cross passes the last line, stop. The "said aloud" column is the output.

### Name it

Doing this with a pencil is called **tracing** the code by hand. The logbook is a **trace table**: one column for each name, one row for each step.

Each purse is a **variable**: a name that holds a value, which can change. The detour is a **function**: a named set of steps. The Kotlin documentation says you declare one with the `fun` keyword, put its parameters in parentheses, write each as `name: Type`, and give the type it hands back. A **parameter** is the name that receives the value you carry in. The word `return` hands a value back. A line that uses the function's name with a value in brackets, such as `triple(2)`, is a **call**.

This program has one function and one call. Trace it with the method above.

```kotlin
fun triple(n: Int): Int {
    return n * 3
}

fun main() {
    var coins = 1
    coins = coins + triple(2)
    println(coins)
}
```

Number the lines from the top: `fun triple` is line 1, `return n * 3` is line 2, and `fun main` is line 5. Then the table is this.

| Step | Line | n | coins | Printed |
|---|---|---|---|---|
| 1 | 6 | - | 1 | |
| 2 | 7, starts the call `triple(2)` | - | 1 | |
| 3 | 2, inside `triple` | 2 | 1 | |
| 4 | 7, adds the 6 that came back | - | 7 | |
| 5 | 8 | - | 7 | 7 |

Line 7 shows up twice. The first time you set off on the detour. The second time you come back with 6 and finish the sum. The program prints `7`. The purse `n` exists only during the detour.

## Predict the output

Follow the method on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: loop-and-function-total
verified: not-run
code: |
  fun double(n: Int): Int {
      return n * 2
  }

  fun main() {
      var total = 0
      for (i in 1..3) {
          total = total + double(i)
      }
      println(total)
  }
options:
  - id: a
    text: "6"
    why: "You may have added 1, 2 and 3 and left out the detour. Each round carries its number to double and adds what comes back."
  - id: b
    text: "3"
    why: "You may have taken the last round number. The line that speaks aloud reads the purse called total, not the round number."
  - id: c
    text: "12"
    correct: true
    why: "The helper hands back 2, then 4, then 6. The purse goes 2, 6, 12, and 12 is said aloud."
output: "12"
```

```challenge
type: predict
run: false
id: while-trace-two-purses
verified: not-run
code: |
  fun main() {
      var left = 10
      var rounds = 0
      while (left > 4) {
          left = left - 3
          rounds = rounds + 1
      }
      println(rounds)
      println(left)
  }
options:
  - id: a
    text: "2, then 4"
    correct: true
    why: "The purses go from 10 and 0 to 7 and 1, then to 4 and 2. Then the check 4 > 4 says no, so the cross leaves the repeat."
  - id: b
    text: "3, then 1"
    why: "You may have let a third round run. At 4 the check asks if 4 is greater than 4, and the answer is no."
  - id: c
    text: "2, then 7"
    why: "You may have read left from the row before the last round. The repeat ran twice, so left went 10, 7, 4."
output: |
  2
  4
```

```challenge
type: predict
run: false
id: purse-reset-in-the-round
verified: not-run
code: |
  fun main() {
      for (i in 1..3) {
          var total = 0
          total = total + i
          println(total)
      }
  }
  // Goal: print the running totals 1, 3, 6.
options:
  - id: a
    text: "1, 3, 6"
    why: "You may have read the goal instead of the lines. The line var total = 0 sits inside the repeat, so every round starts from a fresh purse."
  - id: b
    text: "1, 2, 3"
    correct: true
    why: "Each round puts 0 in the purse again, then adds the round number. The purse holds 1, then 2, then 3."
  - id: c
    text: "0, 0, 0"
    why: "You may have stopped at the reset line. The add line still runs in each round, before the print."
output: |
  1
  2
  3
```

## Tricks to remember

- One line, one row.
- Copy down every purse you did not touch.
- A detour always returns to the line it left.
- A repeat keeps the purses unless a line inside it resets them.

## Common mistakes

- **Jumping to the answer.** Looking at the last line and guessing feels quicker. Short programs hide steps, such as a purse that is reset inside a repeat. Write the rows, and you see where a number changes.
- **Leaving a purse out of a row.** You only wrote the purse that changed, so the next row has a gap. Copy every column into every row, even when nothing changed.
- **Treating the detour as one step.** The helper's lines are steps too. Write the value that arrives in the parameter, and write the value that comes back.
- **Skipping the table because it feels slow.** Five lines feel small enough to do in your head. The table takes a minute and catches the step where a number changes.

## Where this shows up in Android

A cart screen shows a total of 12 when you expected 20. Nothing crashed. The numbers on that screen come from values that change as the program runs. The Android documentation on Jetpack Compose state says: "State in an app is any value that can change over time."

Android Studio has a tool that does your logbook for you. The Android debugging page says a breakpoint pauses your app at a line of code. While it is paused, you can examine variables and continue line by line. That is the same method as this lesson, done by the computer. Doing it by hand first tells you what you should expect to see in each row.

## Recap

1. Tracing means doing one line at a time and writing every changed value in a table.
2. A function call sends you into its lines and brings one value back.
3. A repeat keeps your values unless a line inside it resets them.

## You can now

- Predict the output of a short program that repeats lines and calls a function by filling in a trace table.
- Follow a function call into its lines and back, writing the value that returns.
- Spot a variable that a repeat resets in the wrong place.
