---
id: foundation/debugging-mindset/what-is-a-bug
title: How can code run without errors and still be wrong?
status: live
difficulty: easy
order: 23
requires: [foundation/debugging-mindset/reading-error-messages]
summary: A program can run with no error and still give the wrong answer. The computer follows your lines, not your wishes.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []
playground: false
fruit: false
upnext: false
---


## The question

You ask a program for the price of 3 sweets at 4 coins each. It answers 7. There is no red text and no crash. Where did the mistake hide?

## Mental model

Think of a recipe card and a cook who follows it exactly.

- The recipe card is your program.
- The cook is the computer. The cook does each line in order and never asks "is this sensible?".
- The amount of salt on a line is a value in your program.
- The finished dish is the output.
- You are the taster. You are the only one who knows what the dish should taste like.

A recipe can fail in three ways. The card may be in a language the cook cannot read, so the cook refuses to start. The card may say "add the eggs" when there are no eggs, so the cook starts and then stops halfway. Or the card may say "1 kg of salt" where you meant "1 g". The cook follows it perfectly, and the dish is ruined.

**Where the analogy stops:** A human cook might spot that 1 kg of salt is silly and ask you. A computer never asks. Also, a ruined dish is obvious on the first bite, but a wrong output such as 7 instead of 12 looks believable. You only catch it if you knew the right answer first.

## Build the logic

### Notice

You want the price of 3 sweets at 4 coins each. On paper, that is 3 groups of 4 coins, which makes 12. You hand the job to the cook. The cook says "7" and stops. Nobody complained. The cook added 3 and 4 instead of grouping them.

The cook did nothing wrong. The card said to add.

### Guess

The screen shows a price and there is no message anywhere. Which of these is true?

- A. No message means the answer is right.
- B. The computer knows what you wanted, so it checked the answer.
- C. The computer follows your lines. No message only means no rule was broken.

Idea A feels right because red text usually means trouble. Idea B gives the cook a mind reader's skill. The cook has only the card. Idea C is the answer.

### Build

1. Before you run anything, say what you want in words and numbers: "3 sweets at 4 coins each make 12 coins".
2. Run the program and note what it gave.
3. Compare the two. If they differ, there is a bug, whether or not a message appeared.
4. Sort the failure: did it refuse to run, did it stop halfway, or did it run to the end with the wrong answer?
5. Look for the line where your card says something other than what you meant.

### Name it

A **bug** is a mistake in a program that makes it do something other than what you wanted. There are three kinds of failure. They differ in when they happen and whether a message appears.

Two of the kinds come with a message. A **compile error** is a message from the compiler, the tool that checks your whole program before it runs. An **exception** is a problem that interrupts the program while it runs. It prints a **stack trace**, which is a list of the function calls that led to it.

| Kind | What happens | Message? | In the recipe |
|---|---|---|---|
| Will not run | The compiler refuses the program, and nothing runs | Yes, a compile error | The cook cannot read the card |
| Crashes | The program starts, then an exception interrupts normal execution | Yes, with a stack trace | The cook stops halfway |
| Wrong answer | The program runs to the end and prints something | No | The cook follows "1 kg of salt" |

The third kind is called a **logic bug**: every line is allowed, but together they do the wrong thing. Kotlin cannot see your wish, so it cannot warn you.

This program has one. It runs, prints a number and gives no message.

```kotlin
fun main() {
    val sweets = 3
    val coinsEach = 4
    println(sweets + coinsEach)
}
```

It prints `7`. The line obeys the rules, because adding two whole numbers is allowed. It is wrong for the job. The price of 3 sweets at 4 coins each needs `sweets * coinsEach`, which is 12.

## Predict the output

Work out the answer on paper first. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: pens-total
verified: not-run
code: |
  // Goal: 6 pens at 3 coins each should cost 18 coins.
  fun main() {
      val pens = 6
      val coinsEach = 3
      println(pens + coinsEach)
  }
options:
  - id: a
    text: "18"
    why: "You may have read the goal on the first line. The cook follows the code, and the code adds."
  - id: b
    text: "An error message"
    why: "You may have expected the wrong sum to be refused. Adding two whole numbers is allowed, so no rule is broken."
  - id: c
    text: "9"
    correct: true
    why: "The line says to add 6 and 3. The cook does exactly that and prints 9. The card is wrong, and the cook has no way to know."
output: "9"
```

```challenge
type: predict
run: false
id: neat-average
verified: not-run
code: |
  // Written by a teammate: works out the average of three prices.
  fun main() {
      val first = 10
      val second = 20
      val third = 30
      val average = (first + second + third) / 2
      println(average)
  }
options:
  - id: a
    text: "30"
    correct: true
    why: "The three prices add up to 60. The line divides by 2, not 3, so the dish is 30. The comment on top does not change what the cook does."
  - id: b
    text: "20"
    why: "You may have trusted the comment. The average of 10, 20 and 30 is 20, but the code divides by the wrong number."
  - id: c
    text: "60"
    why: "You may have stopped after the sum. The next part of the line still divides it."
output: "30"
```

```challenge
type: predict
run: false
id: exactly-eighteen
verified: not-run
code: |
  val age = 18

  fun main() {
      // Show "adult" for 18 and over.
      if (age > 18) {
          println("adult")
      } else {
          println("child")
      }
  }
options:
  - id: a
    text: "adult"
    why: "You may have followed the comment. The sign on the line says greater than 18, and 18 is not greater than 18."
  - id: b
    text: "child"
    correct: true
    why: "The check is greater than 18, and 18 is not. So the else part runs. The recipe says 1 kg where it should say 1 g."
  - id: c
    text: "An error message"
    why: "You may have expected the wrong sign to be refused. Both signs are allowed, so Kotlin runs it and prints the answer."
output: "child"
```

## Tricks to remember

- No red text does not mean the dish is right.
- Taste first: write down the answer you expect before you run.
- Three ways to fail: cannot read, stops halfway, ruined dish.
- The cook obeys the card. Read your card the way the cook would.

## Common mistakes

- **Trusting a program because it shows no red text.** Red text usually means trouble, so silence feels like success. A wrong-answer bug is silent. Compare the output with the answer you worked out first.
- **Blaming the computer.** The computer prints the wrong number, so it feels like its fault. It followed your lines exactly. Look for the line that says something other than what you meant.
- **Trusting tidy code with a confident comment.** A neat comment feels like proof. The comment is words, and the cook never reads it. Trace one case by hand and see what the lines do.
- **Running first and thinking later.** If you have no expected answer, any output looks fine. Write the expected answer down before you press Run.

## Where this shows up in Android

A shopping app shows a cart total of 40 when the items cost 60. Nothing crashed, and nothing turned red.

The Android documentation says a stack trace generates when your app crashes because of an error or an exception. A wrong total does not crash the app, so there is no trace to read. This part is our own reasoning. In an app, the only sign of a logic bug is that someone notices the number is wrong. That is why a tester, and you, must know the right answer before checking the screen.

## Recap

1. A bug is a mistake that makes a program do something other than what you meant.
2. A program can fail by refusing to run, by crashing, or by giving a wrong answer.
3. A missing error message does not prove the answer is right.

## You can now

- Predict that a program runs to the end with a wrong answer and no message.
- Work out the answer you expect before running, and compare it with the output.
- Spot the line in tidy, commented code that does something other than the comment says.
