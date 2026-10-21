---
id: kotlin/special-classes/enum-classes
title: How do I list every allowed choice, like the days of the week?
status: shadow
difficulty: easy
order: 51
requires: [kotlin/control-flow/when-expression]
summary: An enum class is a fixed list of allowed choices. See how to loop over them and why a when can check every one.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live; read 21 Oct 2026):
- https://kotlinlang.org/docs/enum-classes.html (declaration, name, ordinal, entries, valueOf, when without else)
- https://kotlinlang.org/docs/control-flow.html (when as an expression must cover all cases)
- https://developer.android.com/topic/libraries/architecture/lifecycle (Lifecycle.State.RESUMED and STARTED used as values)
Code not compiled: no shell in this session. Outputs hand-traced twice. Predict items are run: false, verified: not-run.
-->

## The question

A school app shows a timetable. One teacher types the day as text and spells it "Wensday". The app shows an empty timetable and nobody can say why. How can a program say "the day must be one of these seven, and nothing else"? And how can it visit all seven in turn?

## Mental model

Think of a drink machine with exactly five buttons.

- The machine is the list of allowed choices. Its design is fixed when it is built.
- Each button is one choice, with a label such as "Tea" or "Cocoa".
- The buttons sit in a row, so each has a place. The first place is number 0.
- Pressing a button picks one drink. A sixth button does not exist, so you cannot press it.
- The price sheet beside the machine has one line for each button.

**Where the analogy stops:** A real technician can bolt on a sixth button and leave the price sheet old. In code, a new button means editing the program and building it again. Kotlin can also refuse a price sheet that lacks a line, before anyone presses anything.

## Build the logic

### Notice

The machine has five buttons in a row: Tea, Coffee, Cocoa, Juice and Water. Mia presses the third one and gets cocoa. The price sheet says Tea 2, Coffee 3, Cocoa 4, Juice 3 and Water 1. The cashier walks along the row once to add up one of each drink. Later the technician adds a sixth button, Soup, and forgets to add a line to the price sheet.

### Guess

How should the program remember which button was pressed? Here are three ideas.

- A. As free text typed by the user, such as "cocoa".
- B. As one choice from a fixed row that the program itself defines.
- C. As a number, such as 3.

Idea A lets "cocoa", "Cocoa" and "cocao" be three different drinks, and the typo shows up only when someone looks. Idea C works until someone sends 7, and then nobody remembers what 3 meant. Idea B is the answer.

When Soup arrives with no price line, what should the price sheet do? Here are three ideas.

- A. Keep an "anything else" line at the bottom that gives a made-up price.
- B. Keep exactly one line per button, so a missing line is spotted.
- C. Trust the cashier to notice.

Idea A feels safe, but Soup is then priced by the "anything else" line and nobody is told. Idea C depends on one person's memory. Idea B is the answer.

### Build

1. Write the row of buttons once, in order, each with a label.
2. Give every button a place number, starting from 0.
3. A button is not text. It is one of the fixed choices, and nothing outside the row can be pressed.
4. To visit every button, walk along the row from the first to the last.
5. Write the price sheet with one line per button and no "anything else" line.
6. If a button is added later, a price sheet that must hand back a price and lacks its line is refused.

### Name it

An **enum class** is a class whose objects are a fixed list that you write out once. The Kotlin documentation says you write `enum class` and list the **enum constants** inside curly brackets, separated by commas, as in `enum class Drink { TEA, COFFEE, COCOA }`. You reach one with the class name and a dot, such as `Drink.TEA`. The documentation's examples write the constants in capitals.

The documentation says every enum constant has built-in `name` and `ordinal` properties: its name, and its position counted from `0`. It also says `entries` is a specialised list of all the constants, so the usual list tools work on it, such as `size`.

To visit every constant, use a **`for` loop**, which repeats its block once for each item in a list (general knowledge). In `for (d in Drink.entries)`, the name `d` holds each constant in turn, in order.

A `when` over an enum works as in the `when` lesson. The documentation says that when you use all enum constants in a `when` expression, you do not need an `else` branch. The control flow page says a `when` expression must cover all cases, or the compiler reports an error.

| Drink machine | In Kotlin |
|---|---|
| The machine's design | The enum class |
| A button | An enum constant |
| The button's label | `name` |
| The button's place in the row | `ordinal` |
| The whole row | `entries` |
| The price sheet | A `when` |

This program counts the buttons and visits each one.

```kotlin
enum class Drink { TEA, COFFEE, COCOA, JUICE, WATER }

fun main() {
    println(Drink.entries.size)
    println(Drink.COCOA.name)
    println(Drink.COCOA.ordinal)
    for (d in Drink.entries) {
        println(d)
    }
}
```

It prints `5`, `COCOA` and `2`, then one drink per line: `TEA`, `COFFEE`, `COCOA`, `JUICE`, `WATER`. Cocoa is the third button, so its place is 2.

This program is the price sheet, with one line per button.

```kotlin
enum class Drink { TEA, COFFEE, COCOA }

fun price(d: Drink): Int {
    return when (d) {
        Drink.TEA -> 2
        Drink.COFFEE -> 3
        Drink.COCOA -> 4
    }
}

fun main() {
    println(price(Drink.COFFEE))
}
```

It prints `3`. There is no `else`, because every button has a line.

Kotlin refuses to run this one, because the price sheet must hand back a price and `COCOA` has no line.

```kotlin
// does not compile
enum class Drink { TEA, COFFEE, COCOA }

fun price(d: Drink): Int {
    return when (d) {
        Drink.TEA -> 2
        Drink.COFFEE -> 3
    }
}

fun main() {
    println(price(Drink.COCOA))
}
```

## Predict the output

```challenge
type: predict
run: false
id: count-name-and-place
verified: not-run
code: |
  enum class Day { MON, TUE, WED, THU, FRI }

  fun main() {
      println(Day.entries.size)
      println(Day.WED.ordinal)
      println(Day.FRI.name)
  }
options:
  - id: a
    text: "5, 3, FRI"
    why: "You may have counted places from 1. The first button is place 0, so the third button, WED, is place 2."
  - id: b
    text: "4, 2, FRI"
    why: "You may have used the last place number as the count. Places start at 0, so five buttons end at place 4, but there are still five buttons."
  - id: c
    text: "5, 2, FRI"
    correct: true
    why: "The row has five buttons. WED is the third, so its place is 2, and the label of the last button is FRI."
output: |
  5
  2
  FRI
```

```challenge
type: predict
run: false
id: else-hides-a-new-button
verified: not-run
code: |
  enum class Drink { TEA, COFFEE, COCOA }

  fun label(d: Drink): String {
      return when (d) {
          Drink.TEA -> "hot leaf"
          else -> "other"
      }
  }

  fun main() {
      println(label(Drink.TEA))
      println(label(Drink.COCOA))
  }
options:
  - id: a
    text: "hot leaf, then other"
    correct: true
    why: "TEA has its own line. COCOA has none, so it falls to the bottom line. That is how a missing button hides."
  - id: b
    text: "hot leaf, then COCOA"
    why: "You may have expected the label of the button to come back. The bottom line gives the text other for every button without its own line."
  - id: c
    text: "The program is refused, because COFFEE and COCOA have no line"
    why: "You may have expected the missing lines to be spotted. The bottom line covers them, so Kotlin sees nothing missing."
output: |
  hot leaf
  other
```

```challenge
type: predict
run: false
id: add-up-the-price-sheet
verified: not-run
code: |
  enum class Drink { TEA, COFFEE, COCOA }

  fun price(d: Drink): Int {
      return when (d) {
          Drink.TEA -> 2
          Drink.COFFEE -> 3
          Drink.COCOA -> 4
      }
  }

  fun main() {
      var total = 0
      for (d in Drink.entries) {
          total = total + price(d)
      }
      println(total)
  }
options:
  - id: a
    text: "3"
    why: "You may have counted the buttons. The loop adds the price of each one, not the number of them."
  - id: b
    text: "9"
    correct: true
    why: "The cashier walks the row once: 2 for tea, 3 for coffee and 4 for cocoa. The total grows to 9."
  - id: c
    text: "2, 3 and 4 on three lines"
    why: "You may have expected each price to be printed. The print line sits after the loop, so it runs once with the total."
output: |
  9
```

## Tricks to remember

- One button, one choice. A sixth button does not exist.
- Places count from 0, so the fifth button is place 4.
- No "anything else" line on the price sheet. A missing line then shows.
- On your own machine, write an enum with three constants. Loop over `entries` and print `name` and `ordinal`. Then add a fourth constant and see which `when` stops compiling.

## Common mistakes

- **Treating a constant as text.** `Drink.TEA` and `"TEA"` look alike. A button is not its label. Use `name` when you need the text, and compare constants with constants.
- **Adding `else` to a `when` over an enum to be safe.** It feels careful. A new constant then falls into `else` and nobody is told. List every constant and let the compiler warn you.
- **Counting places from 1.** People count from 1 in daily life. The documentation says `ordinal` starts at `0`.
- **Using free text where a fixed list would do.** Text is quick to type. A typo like "Wensday" is then a valid value until someone looks. Make an enum, so only the real choices exist.

## Where this shows up in Android

The Android Lifecycle page has a code sample that picks between `Lifecycle.State.RESUMED` and `Lifecycle.State.STARTED` for a screen page. A **lifecycle** is the list of stages a screen goes through, such as being on view or being hidden (general knowledge). `Lifecycle.State` is an enum class (general knowledge), and those two names are buttons on the drink machine.

The symptom is a screen that does nothing for a new case, such as a new kind of loading state added by a library update. My own reasoning: if the code used `else`, the new case went down that road silently. A `when` with one line per constant would have stopped the build and told you where to look.

## Say it in an interview

**What is an enum class, and when would you use one?**
It is a class with a fixed list of constants. The Kotlin documentation says you list them in the class body, separated by commas. I use one when a value can only be one of a known few, like a day or a direction, so a typo cannot create a new value.

**What do `name`, `ordinal` and `entries` give you?**
The documentation says `name` is the constant's name and `ordinal` is its position, starting from `0`. `entries` is a list of all the constants, so you can loop over it. Older code uses `values()`, which gives an array (general knowledge).

**Why can a `when` over an enum skip `else`?**
The documentation says that when you use all the constants in a `when` expression, `else` is not needed. My own reasoning: leaving it out is useful, because adding a constant later makes the compiler flag every `when` that must hand back a value and has no line for it.

**What goes wrong when you turn text from a server into an enum with `valueOf`?**
The documentation says `valueOf()` throws an `IllegalArgumentException` if no constant has that name. My own reasoning: if the server adds a new value, the app crashes at that line. Look the text up in `entries` yourself and decide what to do when it is missing.

## Recap

1. An enum class is a fixed list of allowed choices, each with a name and a position from 0.
2. `entries` gives every constant in order, so a `for` loop can visit each one.
3. A `when` that returns a value must cover every constant, and an `else` can hide a new one.

## You can now

- Predict what `entries.size`, `ordinal` and `name` print for a small enum.
- Predict which line of a `when` with an `else` handles a constant that has no line of its own.
- Predict the total when a loop adds up a `when` result for every constant in `entries`.
