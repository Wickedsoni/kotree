---
id: kotlin/functions/default-and-named-arguments
title: How can a function have optional inputs?
status: shadow
difficulty: easy
order: 13
requires: [kotlin/functions/declaring-functions]
summary: A function can fill in an input for you when you leave it out. You can also name the inputs you pass.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/functions.html (default arguments, named arguments in any order, name every argument after the first skipped one, Unit return type optional); https://developer.android.com/develop/ui/compose/kotlin (drawSquare with defaults and named arguments, Text(text = ...), most Compose libraries use default arguments). Read 20 Oct 2026. All outputs hand-traced, not run. No sim: roadmap row says a syntax rule, and the editor is enough. -->
## The question

A coffee order has three questions: the drink, the size and the milk. Most customers want a medium size and regular milk. Making everyone answer all three every time is slow, and making them remember the order of the boxes is worse. How can a function let you skip the answers you do not care about, and still make it clear which answer is which?

## Mental model

Think of ordering a coffee from a slip with three boxes.

- The slip is the order form. Its boxes are drink, size and milk.
- The barista is the one who makes the drink from the slip.
- The house default is what the coffee shop prints beside a box: "size: medium" and "milk: regular".
- If you leave a box with a house default empty, you get the default.
- You can fill the boxes from the top, in order. Or you can write a box's name on the slip, such as "milk: oat", so the barista knows which box your answer belongs to.

**Where the analogy stops:** A real barista can ask "which milk?" when a box is empty. The function never asks, so a box with no house default must always be filled.

## Build the logic

### Notice

Three customers use the slip.

- Ana writes "latte" in the drink box and leaves the rest empty. She gets a latte, medium, with regular milk.
- Ben writes "latte" and "small". The milk box is empty, so he gets regular milk.
- Cy writes "latte" and wants oat milk, but does not care about size.

### Guess

Cy wants oat milk and wants the size left to the house default. How should Cy write the slip? Here are three ideas.

- A. Write "latte" and then "oat", and trust the barista to see that oat is a milk.
- B. Write "latte", "medium" and "oat", filling every box in order.
- C. Write "latte", and then "milk: oat" with the box's name.

Idea A needs a barista who guesses, and a function does not guess. Boxes are filled from the top, so "oat" would land in the size box. Idea B works, but Cy has to repeat the default by hand. Idea C is the answer, because the size box is left empty and keeps its default.

### Build

1. Print a house default beside a box when most customers will want the same answer.
2. A box with no default printed must always be filled in.
3. A customer fills the boxes from the top, in order. Boxes left empty at the end take their defaults.
4. To skip a box in the middle, write the name of a later box on the slip, such as "milk: oat".
5. After you skip a box, write the name of every box that follows it.
6. When boxes are named, the order you write them in does not matter.
7. Writing a box's name only says which box an answer belongs to. The barista makes the drink the same way.

### Name it

A **default value** is a value written beside a parameter with an equals sign. The Kotlin documentation says Kotlin uses it when you call the function without providing an argument that corresponds to that parameter. In `size: String = "medium"`, the type is `String`, which means text, and `"medium"` is the default value.

A **named argument** is an argument written as `name = value`. The documentation says that when you use named arguments, you can list them in any order. It also says that after the first skipped argument, you must name all subsequent arguments.

| Coffee shop | In Kotlin |
|---|---|
| The slip | The function |
| A box | A parameter |
| The house default | The default value |
| Filling the boxes from the top | Passing arguments by position |
| Writing "milk: oat" | A named argument, `milk = "oat"` |

In the text inside quotes below, `$drink` is replaced by the value of `drink`. This function prints and hands nothing back, so no return type is written. The documentation says the return type is optional only for a function that returns `Unit`.

```kotlin
fun order(drink: String, size: String = "medium", milk: String = "regular") {
    println("$drink, $size, $milk")
}

fun main() {
    order("latte")
    order("latte", "small")
    order("latte", milk = "oat")
}
```

It prints three lines: `latte, medium, regular`, then `latte, small, regular`, then `latte, medium, oat`. The first call fills only the drink. The second fills the first two boxes from the top. The third fills the drink, skips the size, and names the milk.

Kotlin refuses to run this one, because the drink box has no default and no value.

```kotlin
// does not compile
fun order(drink: String, size: String = "medium") {
    println("$drink, $size")
}

fun main() {
    order(size = "small")
}
```

## Predict the output

```challenge
type: predict
run: false
id: greet-ana
verified: not-run
code: |
  fun greet(name: String, greeting: String = "Hi") {
      println("$greeting, $name")
  }

  fun main() {
      greet(name = "Ana")
  }
options:
  - id: a
    text: "Ana, Hi"
    why: "You may have expected the values in the order they were written in the call. The text inside the quotes puts greeting first and name second."
  - id: b
    text: "It does not compile, because greeting was left out"
    why: "You may have expected every box to be filled. This box has a house default, so leaving it empty is allowed."
  - id: c
    text: "Hi, Ana"
    correct: true
    why: "The name box gets Ana. The greeting box is empty, so it takes its house default, Hi."
output: |
  Hi, Ana
```

```challenge
type: predict
run: false
id: swap-by-name
verified: not-run
code: |
  fun gap(start: Int, end: Int): Int {
      return end - start
  }

  fun main() {
      println(gap(10, 3))
      println(gap(end = 10, start = 3))
  }
options:
  - id: a
    text: "-7, then 7"
    correct: true
    why: "In the first call the boxes are filled from the top, so start is 10 and end is 3. In the second call the names decide, so start is 3 and end is 10."
  - id: b
    text: "7, then 7"
    why: "You may have expected Kotlin to put the bigger number in the right box. The first call fills the boxes from the top, so start is 10."
  - id: c
    text: "-7, then -7"
    why: "You may have thought the names change nothing. Names say which box each value belongs to, so the order in the call no longer matters."
output: |
  -7
  7
```

```challenge
type: predict
run: false
id: skip-the-size
verified: not-run
code: |
  fun order(drink: String, size: String = "medium", milk: String = "regular") {
      println("$drink, $size, $milk")
  }

  fun main() {
      order("tea", milk = "oat")
      order("tea", "small")
  }
options:
  - id: a
    text: "tea, oat, regular, then tea, small, regular"
    why: "You may have expected oat to fill the next box from the top. The name milk sends it to the milk box."
  - id: b
    text: "tea, medium, oat, then tea, small, regular"
    correct: true
    why: "The first call names the milk box, so the size keeps its house default. The second call fills the size box from the top, and the milk keeps its default."
  - id: c
    text: "tea, medium, oat, then tea, medium, small"
    why: "You may have thought the second call fills the milk box. Boxes are filled from the top, so small goes in the size box."
output: |
  tea, medium, oat
  tea, small, regular
```

## Tricks to remember

- Empty box with a house default: you get the default.
- No default, no skipping: that box must be filled.
- A name on the slip says where the answer goes. It does not change the drink.
- After you skip a box, name everything that follows.

## Common mistakes

- **Thinking a named argument changes what the function does.** Writing a name on the slip feels like giving an instruction. It only says which box the value goes in. The function works the same way.
- **Passing values by position after a skipped box.** Skipping the size and writing only "oat" feels natural. Boxes are filled from the top, so oat lands in the size box. Name the milk: `milk = "oat"`.
- **Swapping two values of the same type.** Two whole numbers fit either box, so Kotlin runs it without complaint. The answer comes out wrong. Name both arguments when the boxes look alike.

## Where this shows up in Android

Reading a call such as `drawSquare(30, 5, Color.Red)` is hard, because you cannot tell what the numbers mean. **Jetpack Compose** is the modern toolkit for building Android UI. Its Kotlin page shows the clearer form, `drawSquare(sideLength = 30, thickness = 5, edgeColor = Color.Red)`, and calls it self-documenting.

The same page says most Compose libraries use default arguments. It shows `Text(text = "Hello, Android!")` and says that by specifying only the `text` parameter, you document that you want the default values for all the others. That is the coffee slip: you fill the one box that matters and leave the rest to the house defaults.

## Recap

1. A default value after a parameter lets callers leave that argument out.
2. A named argument says which parameter a value is for, so the order does not matter.
3. After you skip a parameter, name every argument that follows it.

## You can now

- Predict the output of a call that leaves out an argument with a default value.
- Predict the result when two arguments are passed by position and then by name.
- Spot two arguments of the same type that are in the wrong order.
