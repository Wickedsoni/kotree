---
id: foundation/programming-thinking/expressions
title: How does a program calculate a new value?
status: live
difficulty: easy
order: 12
requires: [foundation/programming-thinking/what-is-a-type]
summary: A program works out sums with symbols. Dividing two whole numbers drops the leftover, so 5 divided by 2 is 2.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []
playground: false
fruit: false
upnext: false
---

## The question

You share 5 sweets between 2 friends. Maths says 2.5 sweets each. But ask a program for 5 divided by 2, and it answers 2.

Is the program broken? And when a sum has several parts, such as 2 plus 3 times 4, which part does the program work out first?

## Mental model

Think of a shop till that deals only in whole coins.

- The till is the part of the program that works out sums.
- A whole coin is a whole number. The till has no half coins.
- When the till shares coins between people, it gives each person whole coins.
- Any coins left over stay in the till's tray. The till does not show them.
- A second drawer in the till holds part coins. The till opens it only when one of the amounts already has a decimal point.

Share 5 coins between 2 people. Each gets 2 coins, and 1 coin is left in the tray. The till shows 2.

**Where the analogy stops:** A real till shows the change or rounds it. Kotlin does neither. It throws the leftover away without any warning, and it does not round up. You can still ask for the leftover on its own. Also, a real till works with one drawer at a time. A Kotlin sum can mix both kinds of number.

## Build the logic

### Notice

A drink costs 2 coins. Three pizzas cost 4 coins each. What is the bill?

You work out the pizzas first: three lots of 4 is 12. Then you add the drink: 14. Nobody adds the drink to the number of pizzas first.

Now a second case. You have 7 coins to share between 2 friends. Each gets 3, and 1 coin is left in the tray.

### Guess

The whole-coin till is asked to share 7 coins between 2 people. What does it show?

- A. 3.5, because the till finds the exact answer.
- B. 4, because the till rounds up.
- C. 3, because the leftover coin is dropped.

Idea A needs half coins, and this till has none. Idea B is believable, because shops do round. But this till does not round. It drops the leftover. Idea C is the answer.

### Build

1. Each piece of arithmetic takes two amounts and gives back one.
2. If both amounts are whole coins, the answer is whole coins.
3. When you share whole coins, the leftover coins are dropped from the answer.
4. To learn the leftover, ask the till for it separately.
5. If either amount has a decimal point in it, the till opens the part-coin drawer. The answer then has a decimal point too.
6. In a longer sum, the multiplying and sharing parts are worked out first. The adding and taking away parts come after.
7. Brackets are worked out before anything else.

### Name it

An **expression** is a piece of code that Kotlin works out to a single value. An **operator** is a symbol that tells it how to combine the amounts. The Kotlin documentation lists these arithmetic operators.

| Operator | Meaning |
|---|---|
| `+` | add |
| `-` | take away |
| `*` | multiply |
| `/` | divide |
| `%` | **remainder**: what is left over after dividing |

A whole number in Kotlin is an `Int`. A number with a decimal point is a `Double`. When both amounts are `Int`, the division is **integer division**. The Kotlin documentation says the result is a whole number, and Kotlin discards the fractional part, which is everything after the decimal point. To get a decimal result, make at least one amount a `Double`. A number written with a decimal point, such as `2.0`, is a `Double`.

| Expression | Result | Why |
|---|---|---|
| `7 / 2` | `3` | Both whole, so the leftover is dropped. |
| `7 % 2` | `1` | The coin left over. |
| `7.0 / 2` | `3.5` | One amount has a decimal point. |
| `2 + 3 * 4` | `14` | The `3 * 4` part comes first. |
| `(2 + 3) * 4` | `20` | The brackets come first. |

Kotlin's rules for reading a sum put multiplication, division and remainder in a tighter group than addition and subtraction. So they are worked out first. Brackets group a part so it is worked out before the rest.

```kotlin
fun main() {
    println(7 / 2)
    println(7 % 2)
    println(7.0 / 2)
    println(2 + 3 * 4)
    println((2 + 3) * 4)
}
```

This prints `3`, `1`, `3.5`, `14` and `20`, each on its own line.

## Predict the output

```challenge
type: predict
run: false
id: seven-two-three-ways
verified: not-run
code: |
  fun main() {
      println(7 / 2)
      println(7 % 2)
      println(7.0 / 2)
  }
options:
  - id: a
    text: "3.5, 1, 3.5"
    why: "You may have expected the exact answer every time. Both amounts in the first line are whole, so the till drops the leftover."
  - id: b
    text: "3, 1, 3.5"
    correct: true
    why: "The first line shares whole coins and drops the leftover. The second line asks for that leftover coin. The third has a decimal point, so the part-coin drawer opens."
  - id: c
    text: "3, 1, 3"
    why: "You may have thought the decimal point makes no difference. A decimal point on either amount opens the part-coin drawer, so the answer keeps its fraction."
output: |
  3
  1
  3.5
```

```challenge
type: predict
run: false
id: average-of-two-whole-numbers
verified: not-run
code: |
  fun main() {
      val a = 4
      val b = 7
      println((a + b) / 2)
  }
options:
  - id: a
    text: "5.5"
    why: "You may have worked it out by hand. The sum is 11, and 11 shared between 2 whole-coin people leaves a coin in the tray, so the answer is whole."
  - id: b
    text: "5"
    correct: true
    why: "The brackets first: 4 plus 7 is 11. Then 11 shared between 2 gives 5, and the leftover coin is dropped."
  - id: c
    text: "6"
    why: "You may have rounded 5.5 up. The till does not round. It drops the leftover, so the answer is 5."
output: "5"
```

```challenge
type: predict
run: false
id: drink-and-pizzas
verified: not-run
code: |
  fun main() {
      println(2 + 3 * 4)
      println((2 + 3) * 4)
  }
options:
  - id: a
    text: "20, then 20"
    why: "You may have worked left to right every time. The multiplying part goes first, so the first line is 14."
  - id: b
    text: "14, then 20"
    correct: true
    why: "In the first line, the pizzas (3 times 4 is 12) come first, then the drink: 14. In the second line, the brackets come first, giving 5 times 4, which is 20."
  - id: c
    text: "14, then 14"
    why: "You may have thought brackets change nothing. They make the till work out 2 plus 3 first, so the second line is 20."
output: |
  14
  20
```

## Tricks to remember

- Whole in, whole out.
- The slash shares. The percent sign counts what is left.
- Put a decimal point on one amount to keep the change.
- Times and shares go first. Brackets go before everything.

## Common mistakes

- **Expecting 5 divided by 2 to give 2.5.** A calculator does give 2.5, so this feels right. With two whole numbers, Kotlin gives 2. Write one of them with a decimal point, such as `5 / 2.0`, to get 2.5.
- **Expecting the till to round.** Rounding is what a shop does with prices, so it feels natural. But Kotlin drops the leftover. An average of 4 and 7 comes out as 5, not 6.
- **Working a sum from left to right.** We read words that way, so the habit is strong. Multiplying goes before adding. Use brackets when you want the adding to go first.

## Where this shows up in Android

Picture a downloads screen that shows "3 of 4 files done". The progress bar should fill three quarters of the way.

The Android documentation says a progress bar takes a number between 0.0 and 1.0. If the app divides 3 by 4 using whole numbers, the answer is 0, so the bar stays empty. The documentation's own sample turns the whole number into a decimal first, and then divides, by writing `i.toFloat() / 100`. That is the part-coin drawer from this lesson.

## Recap

1. Dividing two whole numbers gives a whole number, and the leftover is dropped.
2. The `%` operator gives the leftover, and a decimal point on one number keeps the fraction.
3. Multiplying and dividing happen before adding and taking away, unless brackets say otherwise.

## You can now

- Predict the result of whole-number division, remainder and decimal division.
- Predict how brackets change the result of a sum.
- Spot why the average of two whole numbers loses its fraction.
