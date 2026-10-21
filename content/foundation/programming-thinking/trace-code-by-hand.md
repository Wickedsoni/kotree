---
id: foundation/programming-thinking/trace-code-by-hand
title: How can I know what code prints before I run it?
status: live
difficulty: medium
order: 21
requires: [foundation/programming-thinking/loop-over-a-list, foundation/programming-thinking/function-inputs-and-outputs]
summary: Write down what each line does to your numbers, one row at a time, and you can name the output without a computer.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [trace-table-stepper]
playground: false
fruit: false
upnext: false
---


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

```sim
id: trace-table-stepper
template: stepper
title: A treasure map and its logbook
intro: "The map is a short program with a detour called triple. The logbook table is empty, and the cross waits on line 6."
lines:
  - { id: l1, text: "1  fun triple(n: Int): Int {" }
  - { id: l2, text: "2      return n * 3" }
  - { id: l3, text: "3  }" }
  - { id: l4, text: "4  (blank line)" }
  - { id: l5, text: "5  fun main() {" }
  - { id: l6, text: "6      var coins = 1   (the slider changes this 1)" }
  - { id: l7, text: "7      coins = coins + triple(2)" }
  - { id: l8, text: "8      println(coins)" }
  - { id: l9, text: "9  }" }
code: true
log: { columns: [Step, Line, n, coins, Printed] }
vars: { pc: 1, k: 0, start: 1, coins: 0 }
sliders: [ { id: start, label: Coins in the first purse, var: start, min: 0, max: 5, step: 1 } ]
actions:
  - id: step
    label: Walk one line
    cases:
      - when: { var: { pc: 1 } }
        do:
          - add: { k: 1 }
          - set: { pc: 2 }
          - light: [ l6 ]
          - row: [ "{k}", "6", "-", "{start}", "-" ]
          - say: "The cross is on line 6. The purse coins holds {start}."
      - when: { var: { pc: 2 } }
        do:
          - add: { k: 1 }
          - set: { pc: 3 }
          - light: [ l7 ]
          - row: [ "{k}", "7, starts the call triple(2)", "-", "{start}", "-" ]
          - say: "Line 7 starts a detour to triple and carries the number 2. The purse coins still holds {start}."
      - when: { var: { pc: 3 } }
        do:
          - add: { k: 1 }
          - set: { pc: 4 }
          - light: [ l2 ]
          - row: [ "{k}", "2, inside triple", "2", "{start}", "-" ]
          - say: "Inside triple, the purse n holds 2. The line works out 2 times 3, which is 6, and hands it back."
      - when: { var: { pc: 4, start: 0 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 6 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 0. The purse coins now holds {coins}."
      - when: { var: { pc: 4, start: 1 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 7 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 1. The purse coins now holds {coins}."
      - when: { var: { pc: 4, start: 2 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 8 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 2. The purse coins now holds {coins}."
      - when: { var: { pc: 4, start: 3 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 9 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 3. The purse coins now holds {coins}."
      - when: { var: { pc: 4, start: 4 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 10 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 4. The purse coins now holds {coins}."
      - when: { var: { pc: 4, start: 5 } }
        do:
          - add: { k: 1 }
          - set: { pc: 5, coins: 11 }
          - light: [ l7 ]
          - row: [ "{k}", "7, adds the 6 that came back", "-", "{coins}", "-" ]
          - say: "Back on line 7, the 6 is added to 5. The purse coins now holds {coins}."
      - when: { var: { pc: 5 } }
        do:
          - add: { k: 1 }
          - set: { pc: 6 }
          - light: [ l8 ]
          - row: [ "{k}", "8", "-", "{coins}", "{coins}" ]
          - say: "Line 8 says the number aloud: {coins}. That is the output."
      - when: { var: { pc: 6 } }
        do:
          - say: "The cross has passed the last line. The Printed column holds the output. Press Reset to trace again."
script: [ step, step, step, step, step ]
try:
  - text: "Before each press, say aloud what you think the next row of the table will be. Then press Step and compare. Do this for the first two rows."
    done: [ step, step ]
  - text: "Press Step twice more. The cross walks into triple, then returns to line 7 with the 6 that came back."
    done: [ step, step ]
  - text: "Move the slider to 3, then press Step once more. Every coins cell in the table changes, because the first purse started with a different number."
    done: [ "start=3", step ]
```

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
