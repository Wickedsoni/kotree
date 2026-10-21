---
id: kotlin/lambdas/lambda-syntax
title: How do I read a lambda, even when it sits outside the brackets?
status: shadow
difficulty: medium
order: 32
requires: [kotlin/lambdas/functions-as-values]
summary: A lambda is a small function written on the spot between curly brackets. Learn its parts, the name it, and the trailing form.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/lambdas.html ; https://kotlinlang.org/docs/kotlin-tour-functions.html ; https://kotlinlang.org/docs/returns.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/repeat.html ; https://developer.android.com/develop/ui/compose/kotlin -->
## The question

You are reading Kotlin code that greets every name in a list. The instruction for each name sits in curly brackets. Sometimes those curly brackets come after the round brackets, not inside them. Sometimes the instruction never gives the name a label at all. How do you read a line like that?

## Mental model

Think of a sticky note handed to a helper.

- The sticky note holds one short order, such as "shout this name".
- A box is drawn around the order. The box is the edge of the note.
- A note can start with a label for the thing the helper hands you. A line then separates the label from the order.
- The order is written below the line, one step per line.
- The last line of the note is the answer the note gives back.
- The helper has a pile of cards and reads the note once for each card.
- The helper's folder has a cover. You can tuck the note inside the folder, or stick it on the outside of the cover.

**Where the analogy stops:** A real note is read by one person, once. A Kotlin note can be read many times, by any helper that has a slot for it. Also, sticking the note on the outside of the cover and tucking it inside mean the same thing to Kotlin. Only the writing differs.

## Build the logic

### Notice

A teacher gives a helper three name cards: Ana, Bo and Cleo. She also gives a note that says "shout the card". The helper reads the note three times, once for each card. A second note says "card n: write n plus 1". A third note has two lines: "double the card", then "add 1 to that". The helper gets back the result of the last line. The teacher sticks the first note on the outside of the helper's folder.

### Guess

The first note never gives the card a label. How can the note talk about the card? Here are three ideas.

- A. It cannot. Every note must give its card a label.
- B. The helper gives a lone card a short default label.
- C. The note must always use the word "card".

Idea A feels safe, but Kotlin allows a shortcut when there is exactly one card. Idea C invents a rule. Idea B is the answer.

The third note has two lines. What does it give back? Here are three ideas.

- A. The result of the first line.
- B. The result of the last line.
- C. Nothing, unless a line says "give this back".

Idea C comes from writing functions, where a line says it. A note works differently. Idea B is the answer.

### Build

1. A note is a bordered square. Everything between the borders belongs to the note.
2. Above the dividing line you write labels for what the helper hands in. Below it you write the orders.
3. The last line is the answer. No line has to say "give this back".
4. If the helper hands in exactly one thing, you may drop the labels and the dividing line. The thing gets a short default label.
5. If the helper's last slot is for a note, you may stick the note outside the round brackets. If that slot is the only one, the round brackets can go.
6. To leave a note early, say which note you are leaving. Without that, you are leaving the whole job.

### Name it

A **lambda expression** is a small function written on the spot. The Kotlin documentation says it is always surrounded by curly brackets. The inputs, which are called parameters, come before an arrow `->`. The steps go after the arrow. If the lambda does not give back **`Unit`**, which means nothing useful, the documentation says its last expression is treated as the value it gives back. Here the last expression means the last line.

The default label is **`it`**. The documentation says that when a lambda has a single input, you do not need to name it, and the arrow can be left out. The input is then named `it`.

A **trailing lambda** is a lambda written outside the round brackets. The documentation says that if the last parameter of a function is a function, the lambda can be placed outside the round brackets. If the lambda is the only argument, the round brackets can be left out entirely.

To leave a lambda early, you write a **qualified return**: `return@label`. The returns page says that such a label has the same name as the function the lambda is passed to, so `return@forEach` leaves only the lambda. It says this is similar to `continue` in a loop.

In this program, `listOf` makes a list. `forEach` is a ready-made helper that reads the note once for each item. `repeat(2)` reads its note two times.

```kotlin
fun main() {
    val addOne: (Int) -> Int = { x -> x + 1 }
    println(addOne(4))

    val names = listOf("Ana", "Bo")
    names.forEach { println(it) }
    names.forEach({ name -> println("Hi, " + name) })

    repeat(2) { println("tick") }
}
```

It prints `5`, `Ana`, `Bo`, `Hi, Ana`, `Hi, Bo`, `tick` and `tick`. The first `forEach` uses `it` and a trailing lambda. The second writes the label `name` and keeps the lambda inside the round brackets. Both do the same job.

Here is a note with two lines. The last line is the answer.

```kotlin
fun main() {
    val note = { x: Int ->
        val doubled = x * 2
        doubled + 1
    }
    println(note(5))
}
```

It prints `11`. The note doubles 5 to get 10, and the last line adds 1.

Kotlin refuses to run this one, because it does not know what kind of thing `x` is. The documentation says you must give the types either in the lambda or in the function type.

```kotlin
// does not compile
fun main() {
    val note = { x -> x + 1 }
    println(note(4))
}
```

## Predict the output

```challenge
type: predict
run: false
id: last-line-is-the-answer
verified: not-run
code: |
  fun main() {
      val note = { x: Int ->
          val plusOne = x + 1
          plusOne * 2
      }
      println(note(3))
  }
options:
  - id: a
    text: "4"
    why: "You may have taken the first line as the answer. A note gives back its last line, and the first line only sets up plusOne."
  - id: b
    text: "8"
    correct: true
    why: "The first line makes plusOne equal to 4. The last line doubles it to 8, and the last line is the answer."
  - id: c
    text: "The program does not compile, because there is no return"
    why: "You may have expected a line that says give this back. A note does not need one, because its last line is the answer."
output: |
  8
```

```challenge
type: predict
run: false
id: print-as-last-line
verified: not-run
code: |
  fun main() {
      val sizeNote = { word: String ->
          val n = word.length
          println(n)
      }
      val result = sizeNote("kiwi")
      println(result)
  }
options:
  - id: a
    text: "4 and then 4"
    why: "You may have expected the note to give back n. The last line is println(n), so the answer is whatever println gives back."
  - id: b
    text: "4 and then nothing is printed"
    why: "You may have thought a note with no useful answer gives an empty line. It gives back the value called Unit, and printing that shows its name."
  - id: c
    text: "4 and then kotlin.Unit"
    correct: true
    why: "The first 4 comes from the print inside the note. The last line is println(n), which gives back Unit, so result holds Unit and printing it shows kotlin.Unit."
output: |
  4
  kotlin.Unit
```

```challenge
type: predict
run: false
id: leave-the-note-early
verified: not-run
code: |
  fun main() {
      listOf(1, 2, 3).forEach {
          if (it == 2) return@forEach
          println(it)
      }
      println("done")
  }
options:
  - id: a
    text: "1, 3, done"
    correct: true
    why: "For the card 2, the note is left early, so nothing is printed. The helper goes on to card 3, and done prints after the helper finishes."
  - id: b
    text: "1, done"
    why: "You may have thought leaving the note ends the whole job. return@forEach leaves only the note for that card, and the helper reads it again for the next card."
  - id: c
    text: "1, 2, 3, done"
    why: "You may have thought the if line does nothing. When the card is 2, the note is left before the print line."
output: |
  1
  3
  done
```

## Tricks to remember

- The last line of a note is its answer. No `return` is needed.
- One card and no label means the card is called `it`.
- Curly brackets after round brackets are a lambda in the last slot, not a block of code that runs on the spot.
- `return@name` leaves the note. A plain return would leave the whole function.

## Common mistakes

- **Writing `return` at the end of a lambda, as in a function.** A function needs it, so a note feels the same. The last line is already the answer. Write the value on the last line.
- **Ending a lambda with a print line and expecting a useful answer.** The print feels like the finishing touch. A print gives back `Unit`, which means nothing useful, and that becomes the answer. Printing a `Unit` shows the text `kotlin.Unit`, which is general knowledge because the documentation pages do not show it. Put the value you want on the last line.
- **Reading `names.forEach { ... }` as a block that runs once on the spot.** Curly brackets in an `if` do run on the spot, so this feels right. Here they are a note handed to `forEach`, and the helper reads it once for each name.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its Kotlin page says that the last parameter of every layout, such as `Column`, is `content`, which is a function that adds the child elements to the screen. So `Column { Text("Some text") }` hands a trailing lambda to `content`, and the round brackets are left out because it is the only argument. A `Button` shows both forms in one call: `onClick = { ... }` sits inside the round brackets, and a second lambda follows them.

This is a common first puzzle in a Compose file. The braces after `Column` look like a block of code with no function around it. They are the note that `Column` reads to find out what to put on the screen.

## Say it in an interview

**What is `it` in a Kotlin lambda?**
It is the default name of a lambda's only input. The documentation says you can leave the input's name and the arrow out, and the compiler names the input `it`. My own reasoning: it keeps short lambdas short, but when one lambda sits inside another, a written name such as `name` is clearer.

**What is a trailing lambda, and why does Kotlin allow it?**
It is a lambda written outside the round brackets. The documentation says this is allowed when the lambda is the last parameter, and the round brackets can go if it is the only argument. My own reasoning: the call then reads like a built-in block, which is why Compose code looks the way it does.

**What does a lambda give back, and how do you return from the middle?**
It gives back the value of its last expression, unless it gives back `Unit`. To leave early you use a label, such as `return@forEach`. The documentation says a bare `return`, with no label, returns from the nearest function around it, so inside a lambda it would leave the function that holds the lambda. The label leaves only the lambda.

**What is the difference between a lambda and a function reference?**
Both are function values. A lambda is written on the spot and can hold its own small steps. The documentation says a function reference, written `::name`, points at a function that already exists. My own reasoning: use a reference when a function with the right header already exists, and a lambda when the work is small and only needed here.

## Recap

1. A lambda is a function in curly brackets, with its inputs before the arrow and its steps after it.
2. The last line is the answer, and a lone parameter can be called `it`.
3. A lambda in the last slot can sit outside the round brackets, and `return@name` leaves only the lambda.

## You can now

- Predict the value a lambda gives back from its last line.
- Spot the value a lambda gives back when its last line is a print.
- Predict which items a `forEach` prints when `return@forEach` skips one.
