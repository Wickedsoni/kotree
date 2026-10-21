---
id: kotlin/lambdas/higher-order-functions
title: How do I write a function that takes another function?
status: shadow
difficulty: medium
order: 33
requires: [kotlin/lambdas/lambda-syntax]
summary: A higher-order function takes another function as input and decides when to run it. Learn to write your own.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/lambdas.html ; https://kotlinlang.org/docs/inline-functions.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/repeat.html ; https://developer.android.com/develop/ui/compose/kotlin -->
## The question

Three screens in an app each need to show a loading spinner, do some work, and then hide the spinner. The spinner steps are the same every time. Only the work in the middle changes. How can you write the spinner steps once and let each screen hand over its own work?

## Mental model

Think of a tour company with one guide.

- The guide has a fixed routine: welcome the group, visit the stops, say goodbye.
- A customer brings a list of stops. The list is the customer's own.
- The booking form has a field called "your stops". The field says what a stop looks like, for example "a stop is given a number".
- When the routine reaches the stops, the guide follows the list. The guide may follow it twice, once or never.
- The guide never reads what is at the stops. The guide only follows the list.
- Any company can train its own guide with its own routine.

**Where the analogy stops:** A real guide can read your list before the tour and decide what to do. A function does not look inside the lambda it holds. It can only call it and take whatever comes back. Also, no booking office stops you from sending a list that the guide never follows.

## Build the logic

### Notice

The company has one guide. Customer one hands over the stops "bakery" and "park". Customer two hands over only "museum". The guide's routine is the same for both: welcome, follow the list, goodbye. A third customer hands over a list but asks for a tour of zero stops. The guide welcomes the group and says goodbye without following the list at all.

### Guess

The guide has never seen the stops on a list. How does the routine know what to do at them? Here are three ideas.

- A. The guide has to be rewritten for every customer.
- B. The routine has an empty slot, "follow the list here". The customer fills it when booking.
- C. The guide guesses what the stops are.

Idea A defeats the point of one routine. Idea C is how mistakes happen. Idea B is the answer.

### Build

1. Write the routine once, with a slot at the point where the customer's list belongs.
2. Give the slot a name and a header that says what a stop looks like: what the guide hands in, and what comes back.
3. At the slot, the guide follows the list. In code terms, following the list is calling a function.
4. The routine decides how often to follow it: never, once or many times.
5. The guide may hand a value to each stop, such as the stop number.
6. A customer can fill the slot with a list written on the spot or with a list that already exists.
7. Anyone can write a guide. You do not need a special licence.

### Name it

A **higher-order function** is a function that takes another function as an input, which is called a parameter, or gives one back. The Kotlin documentation describes it as a function that receives other functions as parameters. In the function's first line, the slot is a parameter with a function type, for example `visit: (Int) -> Unit`. That reads: the parameter `visit` is a function that takes one whole number and gives back nothing useful. Inside the function's steps, you call it the way you call any function stored in a variable, with `visit(1)`. When you call the higher-order function, you hand it a lambda, often as a trailing lambda.

| Tour company | In Kotlin |
|---|---|
| The guide | The higher-order function |
| The fixed routine | The function's own steps |
| The field "your stops" | A parameter with a function type |
| The customer's list of stops | The lambda you hand over |
| Following the list | Calling the parameter, like `visit(1)` |
| The stop number given to each stop | The value passed into the lambda |

```kotlin
fun twice(action: () -> Unit) {
    action()
    action()
}

fun tour(stops: Int, visit: (Int) -> Unit) {
    println("Welcome")
    repeat(stops) { visit(it + 1) }
    println("Goodbye")
}

fun main() {
    twice { println("hello") }
    tour(2) { println("Stop " + it) }
}
```

It prints `hello`, `hello`, `Welcome`, `Stop 1`, `Stop 2` and `Goodbye`. The function `twice` follows its list two times. The function `tour` follows its list once per stop and gives each stop its number. The `it` inside `repeat` is the position counted from 0, so `visit(it + 1)` hands over 1 and then 2.

Kotlin refuses to run this one, because `println("hello")` runs at once and gives back `Unit`, while `twice` wants a function.

```kotlin
// does not compile
fun twice(action: () -> Unit) {
    action()
    action()
}

fun main() {
    twice(println("hello"))
}
```

## Predict the output

```challenge
type: predict
run: false
id: guide-follows-twice
verified: not-run
code: |
  fun twice(action: () -> Unit) {
      println("start")
      action()
      action()
      println("end")
  }

  fun main() {
      twice { println("hello") }
  }
options:
  - id: a
    text: "start, hello, end"
    why: "You may have thought the guide follows the list once. The routine has two lines that follow the list, so hello appears twice."
  - id: b
    text: "start, hello, hello, end"
    correct: true
    why: "The guide welcomes with start, follows the list twice, then says end. The lambda runs only where the routine calls it."
  - id: c
    text: "hello, start, end"
    why: "You may have thought the list is followed as soon as it is handed over. The guide holds it and follows it only at the slot."
output: |
  start
  hello
  hello
  end
```

```challenge
type: predict
run: false
id: zero-stops-booked
verified: not-run
code: |
  fun times(count: Int, action: () -> Unit) {
      repeat(count) { action() }
      println("finished")
  }

  fun main() {
      times(0) { println("work") }
      times(2) { println("work") }
  }
options:
  - id: a
    text: "work, finished, work, work, finished"
    why: "You may have thought the guide follows the list at least once. For a count of 0 the guide never follows it."
  - id: b
    text: "finished, work, finished"
    why: "You may have read the count as a yes or no. The guide follows the list once for each stop booked, so 2 gives work twice."
  - id: c
    text: "finished, work, work, finished"
    correct: true
    why: "With 0 stops the guide skips the list and only says finished. With 2 stops the guide follows it twice, then says finished."
output: |
  finished
  work
  work
  finished
```

```challenge
type: predict
run: false
id: value-given-to-the-stop
verified: not-run
code: |
  fun withTotal(a: Int, b: Int, show: (Int) -> Unit) {
      show(a + b)
  }

  fun main() {
      withTotal(3, 4) { println("Total: " + it) }
      withTotal(10, 5) { total -> println("Sum is $total") }
  }
options:
  - id: a
    text: "Total: 7 and then Sum is 15"
    correct: true
    why: "The guide hands each stop the sum of the two numbers. The first stop calls it, and the second stop gives it the label total."
  - id: b
    text: "Total: 3 and then Sum is 10"
    why: "You may have thought the stop gets the first number. The guide hands over a + b, which is the sum."
  - id: c
    text: "The program does not compile, because the second call labels the value and the first does not"
    why: "You may have expected every list to label the value the same way. A lone value may be called it or given a label, and each list chooses."
output: |
  Total: 7
  Sum is 15
```

## Tricks to remember

- Write the guide once. Let each customer bring the stops.
- The parameter's function type is the header of the slot.
- A parameter's name with brackets follows the list. Without brackets it does nothing.
- The guide decides when the list is followed, and how often.

## Common mistakes

- **Believing only Kotlin's built-in functions can take a lambda.** The ready-made helpers are the ones you meet first, so they feel special. You can write a function with a parameter such as `action: () -> Unit`, and it works the same way.
- **Handing the result instead of the list.** Writing `twice(println("hello"))` reads like "twice, hello". The brackets run `println` at once, and the guide receives its result. Write the list as a lambda: `twice { println("hello") }`.
- **Holding the parameter but never calling it.** Taking the lambda in the first line feels like using it. If the body never writes `action()`, the stops never happen and nothing appears.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its Kotlin page says that Kotlin supports higher-order functions, functions that receive other functions as parameters, and that Compose builds on this. The `Button` function, which draws a button, has an `onClick` parameter. The value of that parameter is a function, which the button calls when the user clicks it.

That is the guide and the list. The people who wrote `Button` wrote the routine once. Your screen hands over its own stops. This next part is my own reasoning: the button calls your lambda once for each tap, and zero times if nobody taps, the same as the booking of 0 stops in the second Predict item.

## Say it in an interview

**What is a higher-order function?**
It is a function that takes another function as a parameter, or gives one back. The Kotlin documentation says Compose builds on this idea. For example, `Button` has an `onClick` parameter that holds a function, and the button calls it on a click.

**Why write a function that takes a function?**
You write the shared routine once, and each caller supplies the part that changes. In the spinner example, the show and hide steps stay in one place. My own reasoning: the trade-off is that a reader must follow where the lambda is called, because the call is no longer on the line that wrote it.

**What does `inline` do, and why would you use it?**
The documentation says that using functions as values adds run-time cost, because of extra memory use and extra calls. Marking a function `inline` makes the compiler copy the function's body, and the lambdas passed to it, into the place where it is called. The documentation warns that this can make the generated code grow, so you avoid inlining large functions. The documentation shows `repeat` as an `inline` function.

**Who decides when the lambda runs, and how often?**
The function that holds it. It may call it once, many times or never, as `times(0)` shows. My own reasoning: when you hand a lambda to a function you did not write, read what the function does with it.

## Recap

1. A higher-order function takes another function as a parameter and calls it when its own steps say so.
2. The parameter's function type says what the lambda is handed and what it gives back.
3. The function decides how often the lambda runs, even never.

## You can now

- Predict the output when a function calls its lambda twice.
- Predict the output when a function is asked to call its lambda zero times.
- Predict the value a function hands into the lambda it receives.
