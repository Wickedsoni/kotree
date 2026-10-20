---
id: kotlin/functions/declaring-functions
title: How do I write a function in Kotlin?
status: live
difficulty: easy
order: 12
requires: [kotlin/control-flow/if-as-an-expression]
summary: A Kotlin function has a name, boxes for its inputs, a kind of result and steps in braces. This is what each part means.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []
playground: false
fruit: false
upnext: false
---

## The question

A shop prints posters. For each order, someone works out the area of the poster from its width and height. Today there are 3 orders and tomorrow there may be 30. You want to write the steps once and reuse them with new numbers. What does the first line of that set-up have to say?

## Mental model

Think of an order form at a print shop.

- The form is the whole reusable job.
- The title at the top is its name, such as "Area".
- The boxes are the blanks you fill in. Each box has a label and says what goes in it, such as "width: a number".
- A line at the top says what you get back, such as "You get back: a number".
- The clerk's steps are the work done behind the counter once you hand the form in.
- The printed sheet the clerk hands back is the result.

You fill in the boxes, hand the form in, and get the sheet back. The same form works for every customer.

**Where the analogy stops:** A paper form is filled in once. A function can be filled in as often as you like, with new values each time. Also, a print shop always hands you something, but some Kotlin functions hand back nothing useful. `main` is one of them, which is why it has no "you get back" line.

## Build the logic

### Notice

The shop has a form called "Area" with two boxes, "width" and "height". One customer writes 2 and 3. The clerk multiplies them and hands back 6. The next customer writes 4 and 5 and gets back 20. It is the same form with different numbers.

### Guess

What must the form say before any customer writes on it? Here are three ideas.

- A. Only its title.
- B. Its title, its boxes with what goes in each, and what kind of thing you get back.
- C. Nothing. The clerk can work it all out from the steps.

Idea A leaves the customer unsure what to write. Idea C feels clever, but the Kotlin documentation says it does not work out the "you get back" line for a function whose steps sit in curly braces. You must write it, unless the function hands back nothing useful. Idea B is the answer.

### Build

1. Give the form a title.
2. List the boxes. For each one, write a label and what goes in it.
3. Write the "you get back" line, which says the kind of thing the clerk hands over.
4. Write the clerk's steps.
5. Make the last step hand the sheet back.
6. To use the form, write its title and fill in the boxes in the order they are listed. Keep the sheet you get back under a name, or use it straight away.

### Name it

A **function** is a named block of steps you can use again. You declare one with the keyword `fun`. Here is how `fun area(w: Int, h: Int): Int` breaks down.

- `area` is the name, the title on the form.
- `w: Int` and `h: Int` are the **parameters**, the boxes. `Int` is the type for whole numbers. The documentation says to write each one as `name: Type`, to separate them with commas, and to give each a type.
- `: Int` after the round brackets is the **return type**, the "you get back" line. The documentation says a function with a block body must always state its return type. The only exception is a function that hands back nothing useful, such as `main`. Its return type is **`Unit`**, and you may leave it out.
- The curly braces hold the **body**, the clerk's steps.
- **`return`** hands the sheet back. In the body below, it hands back `w * h`.

Using the form is called a **call**, and the values you fill in are the **arguments**. In `area(2, 3)`, the 2 goes in the first box and the 3 in the second.

| Print shop | In Kotlin |
|---|---|
| The form | The function |
| The title | The name |
| A box and its label | A parameter |
| What a customer writes in a box | An argument |
| The "you get back" line | The return type |
| The clerk's steps | The body |
| The sheet handed back | The value that `return` gives |

```kotlin
fun area(w: Int, h: Int): Int {
    return w * h
}

fun main() {
    val small = area(2, 3)
    println(small)
    println(area(4, 5))
}
```

It prints `6`, then `20`. The call `area(2, 3)` runs the body with `w` as 2 and `h` as 3, and `return` hands back 6. That value is kept under the name `small`. The second call is used straight away inside `println`.

Kotlin refuses to run this one, because it promises an `Int` but its body never hands one back. The documentation requires the return type, and the refusal is our own reasoning from that rule.

```kotlin
// does not compile
fun area(w: Int, h: Int): Int {
    val result = w * h
}

fun main() {
    println(area(2, 3))
}
```

## Predict the output

```challenge
type: predict
run: false
id: area-seven-two
verified: not-run
code: |
  fun area(w: Int, h: Int): Int {
      return w * h
  }

  fun main() {
      println(area(7, 2))
  }
options:
  - id: a
    text: "9"
    why: "You may have added the two numbers. The clerk's steps say w * h, which multiplies them."
  - id: b
    text: "72"
    why: "You may have joined the digits side by side. The boxes hold numbers, so the clerk multiplies them."
  - id: c
    text: "14"
    correct: true
    why: "The 7 goes in the first box and the 2 in the second. The clerk multiplies them and hands back 14."
output: |
  14
```

```challenge
type: predict
run: false
id: boxes-in-order
verified: not-run
code: |
  fun remaining(total: Int, used: Int): Int {
      return total - used
  }

  fun main() {
      println(remaining(3, 10))
  }
options:
  - id: a
    text: "7"
    why: "You may have expected the bigger number to go in the first box. Boxes are filled in the order they are listed, so total is 3."
  - id: b
    text: "-7"
    correct: true
    why: "The first argument, 3, goes in the box called total, and 10 goes in used. The clerk works out 3 - 10, which is -7."
  - id: c
    text: "13"
    why: "You may have added the two numbers. The clerk's steps say total - used."
output: |
  -7
```

```challenge
type: predict
run: false
id: sheet-into-another-form
verified: not-run
code: |
  fun twice(x: Int): Int {
      return x * 2
  }

  fun main() {
      val first = twice(3)
      println(twice(first))
  }
options:
  - id: a
    text: "12"
    correct: true
    why: "The first call hands back 6. That sheet is written in the box of the second call, which hands back 12."
  - id: b
    text: "6"
    why: "You may have stopped after the first form. The second call fills the box with 6 and doubles it again."
  - id: c
    text: "3"
    why: "You may have expected the input back. The clerk hands back the result of the steps, not what was written in the box."
output: |
  12
```

## Tricks to remember

- Name, boxes, what comes back, steps: that is the whole form.
- A box is written as label, colon, type.
- `return` hands the sheet back and ends the clerk's work.
- Boxes are filled in the order they are listed.

## Common mistakes

- **Skipping the return type.** Other parts of Kotlin work out types for you, so leaving it out feels fine. A function with its steps in braces that hands back a value must state it. Write the type after the round brackets.
- **Declaring the return type but forgetting `return`.** The "you get back" line makes the form look finished. The clerk never hands anything over, and Kotlin refuses to run it. End the body with `return` and the value.
- **Swapping the arguments.** The numbers look the same, so it feels harmless. The first value goes into the first box. Read the box labels before you fill them.

## Where this shows up in Android

Two rows on a screen have the same shape but show different names. **Jetpack Compose** is the modern toolkit for building Android UI. Its modifiers page shows a function whose first line is `private fun Greeting(name: String)`, with the label `@Composable` above it. This lesson does not cover those two extra words. The rest is the form from this lesson: `fun`, the name `Greeting`, and one box called `name` that takes text. `Greeting` has no "you get back" line because it hands back nothing useful, the exception from Name it.

This next part is our own reasoning: the screen can use the same function twice, once with each person's name. If a call put the wrong value in the box, a row would show the wrong name. The steps are written once, and the two rows differ only in what was written in the box.

## Say it in an interview

**What is the difference between a parameter and an argument?**
A parameter is the box in the function's declaration, such as `w: Int`. An argument is the value you put in the box when you call it, such as the 2 in `area(2, 3)`. The box is written once, and the values change with every call.

**Is the return type optional?**
Not for a block-body function that hands back a value. The Kotlin documentation says it must state its return type, and the only exception is a function that returns `Unit`, such as `main`. My own reasoning: the written type is a promise that callers can read without studying the body.

**Can a function return early?**
Yes. `return` hands the value back and ends the function at once, so it need not be the last line (general knowledge). The trade-off is that several `return` lines can be harder to follow than one.

**What does a function with no `return` give back?**
It gives back `Unit`, the type for "nothing useful". The documentation says you may leave that type out. `main` is the example in this lesson.

## Recap

1. A function has a name, parameters, a return type and a body.
2. A parameter is written as a name, a colon and a type.
3. A block-body function that hands back a value must state its return type and use `return`.

## You can now

- Predict the value a call with two arguments gives back.
- Predict how the order of the arguments changes the result.
- Predict the result when one call's result is used as the next call's argument.
