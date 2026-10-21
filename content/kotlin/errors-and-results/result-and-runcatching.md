---
id: kotlin/errors-and-results/result-and-runcatching
title: How can a function hand back either a value or a failure without throwing?
status: shadow
difficulty: hard
order: 62
requires: [kotlin/errors-and-results/try-catch-finally, kotlin/special-classes/sealed-classes]
summary: A function can return a value or a failure in one package. See how the caller opens it.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/ : Result<out T> is "a discriminated union that encapsulates a successful outcome with a value of type T or a failure with an arbitrary Throwable exception"; isSuccess, isFailure, getOrNull, exceptionOrNull, fold, getOrElse, getOrDefault, getOrThrow, map, mapCatching, onSuccess, onFailure, recover.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/run-catching.html : runCatching calls the block, catches any Throwable thrown from it and encapsulates it as a failure.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/map.html (Result.map) : map rethrows any Throwable thrown by transform; mapCatching encapsulates exceptions.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/get-or-else.html : getOrElse signature; shorthand for fold(onSuccess = { it }, onFailure = onFailure).
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int.html : String.toInt() throws NumberFormatException for an invalid string.
- https://developer.android.com/kotlin/coroutines : Android's own sealed class Result<out R> with Success and Error ("we have our own Result class"); caller checks with when.
- https://developer.android.com/kotlin/coroutines/coroutines-best-practices : do not consume CancellationException; prefer catching specific exception types.
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/errors-and-results/result-and-runcatching.
- The roadmap's wording note on Result was checked against the Result API page above; no marker remains.
-->

## The question

A function turns the text a user typed into a number. For "42" it hands back 42, but for "x" it cannot. How should it tell the caller? A number such as -1 could be a real answer, and stopping the whole program may spoil the caller's plan. Is there a way to hand back both outcomes in one package?

## Mental model

Think of a parcel that comes in the post.

- The parcel always arrives. Inside is either the item you ordered or a note that says why it could not be sent.
- The sender is the function. It packs the parcel.
- The packer is a helper who wraps any attempt: if the attempt works, the item goes in, and if it breaks, a note goes in.
- The receiver is the caller. The receiver opens the parcel and checks which one it holds.
- Taking the item or a fallback is opening the parcel and using a spare if there is only a note.
- A parcel left on the doorstep with its seal unbroken is a parcel nobody opened.

**Where the analogy stops:** A real note is written by a person who chose to explain. Here the note is the failure that broke the attempt, whatever it was. And a real unopened parcel soon gets noticed. A Kotlin parcel can sit unopened and nobody will be told.

## Build the logic

### Notice

Maya's helper takes text and hands back a number. For "42" it can say 42. For "x" it cannot make a number. The caller is a screen that wants to show the number, or show a message if there is none. Sam's helper, with the same job, stops the whole program when the text is "x".

### Guess

How should Maya's helper tell the caller that it failed? Here are three ideas.

- A. Hand back a special number such as -1 for "failed".
- B. Stop the program and make the caller prepare a net.
- C. Always hand back a parcel that holds either the number or a note.

Idea A mixes a real answer and a failure in one number, and a caller who forgets to check uses -1 as if it were real. Idea B is right when the failure cannot be repaired, but a caller with a plan has to wrap every call. Idea C lets the caller see both outcomes in one place, and it is the answer for failures a caller can expect.

### Build

1. Make the parcel a single kind of thing that holds either an item or a note.
2. The packer tries the work. If it works, the item goes in. If it breaks, the note goes in.
3. The receiver must open the parcel to find out which one is inside.
4. The receiver can ask "did it work?", or take the item or a spare, or give two plans: one for the item and one for the note.
5. The receiver can also change the item inside, for example by adding one. If there is only a note, the change is skipped and the note stays.
6. If the change itself breaks, it is a new failure. A plain change does not pack it into the parcel, so the failure escapes and stops the program. A packing change does pack it.
7. Do not leave a parcel unopened. Nothing is printed, and the failure disappears from view.

### Name it

**`Result<T>`** is the parcel. The Kotlin documentation says it encapsulates a successful outcome with a value of type `T`, or a failure with an arbitrary `Throwable` exception. A **`runCatching`** call is the packer. The documentation says it calls the block, catches any `Throwable` thrown from it, and wraps it as a failure.

The curly brackets after `runCatching` hold a small block of code that the function runs for you. That kind of block is called a lambda (general knowledge). `getOrElse`, `mapCatching` and `fold` take blocks like that too.

`"x".toInt()` turns text into a whole number. The documentation says it throws a `NumberFormatException` when the text is not a valid number. The calls below open the parcel in different ways.

```kotlin
fun main() {
    val good = runCatching { "42".toInt() }
    val bad = runCatching { "x".toInt() }
    println(good.getOrElse { -1 })
    println(bad.getOrElse { -1 })
    println(bad.isFailure)
}
```

It prints `42`, `-1` and `true`. The `getOrElse` call opens the parcel and returns the item, or runs its block on the note and returns that. The `isFailure` property tells you which it is.

The documentation says `fold` gives two plans: the result of `onSuccess` for an item, or the result of `onFailure` for a note.

```kotlin
fun main() {
    val r = runCatching { 10 / 2 }
    val text = r.fold(
        onSuccess = { "got " + it },
        onFailure = { "failed" }
    )
    println(text)
}
```

It prints `got 5`. The documentation says `map` changes the item and rethrows any exception thrown by its step. For a step that may break, `mapCatching` packs the new failure into the parcel.

```kotlin
fun divide(a: Int, b: Int): Int {
    return a / b
}

fun main() {
    val ok = Result.success(2)
    val safe = ok.mapCatching { divide(it, 0) }
    println(safe.isFailure)
}
```

It prints `true`. The step broke, and the parcel now holds a note.

## Predict the output

```challenge
type: predict
run: false
id: open-with-a-spare
verified: kotlin-2.4.21
code: |
  fun main() {
      val a = runCatching { "x".toInt() }.getOrElse { -1 }
      val b = runCatching { "7".toInt() }.getOrElse { -1 }
      println(a)
      println(b)
  }
options:
  - id: a
    text: "0 and 7"
    why: "You may have expected a failed number to become 0. The spare you packed is -1, so that is what comes out."
  - id: b
    text: "The program stops at the first line"
    why: "You may have thought the packer lets the failure through. It wraps any failure in a parcel, so nothing stops."
  - id: c
    text: "-1 and 7"
    correct: true
    why: "The first parcel holds a note, so the spare -1 is used. The second parcel holds the item 7, so the spare is ignored."
output: |
  -1
  7
```

```challenge
type: predict
run: false
id: parcel-left-unopened
verified: kotlin-2.4.21
code: |
  fun main() {
      val r = runCatching { "x".toInt() }
      println("step 1")
      println("step 2")
  }
options:
  - id: a
    text: "step 1 and step 2"
    correct: true
    why: "The failure was packed into the parcel, and nobody opened it. Nothing stops and nothing is printed about the note."
  - id: b
    text: "The program stops with a NumberFormatException before step 1"
    why: "You may have thought the failure still stops the program. The packer wrapped it, so the program carries on."
  - id: c
    text: "step 1, step 2 and then a message about the failure"
    why: "You may have expected a message. Nothing prints the note by itself. Only the receiver reading it does."
output: |
  step 1
  step 2
```

```challenge
type: predict
run: false
id: map-or-map-catching
verified: kotlin-2.4.21
code: |
  fun divide(a: Int, b: Int): Int {
      return a / b
  }

  fun main() {
      val ok = Result.success(5)
      val safe = ok.mapCatching { divide(it, 0) }
      println(safe.isFailure)
      println("before")
      val risky = ok.map { divide(it, 0) }
      println("after")
  }
options:
  - id: a
    text: "true, before, after"
    why: "You may have thought map also packs the new failure. Only the packing change does. A plain map lets the failure escape and stop the program."
  - id: b
    text: "true, before, and then the program stops"
    correct: true
    why: "The packing change turns the broken step into a note, so isFailure is true. The plain change does not, so the failure escapes on that line, the program stops and after never prints."
  - id: c
    text: "The program stops before printing anything"
    why: "You may have thought the first change also stops the program. It packs the failure, so the program reaches the next lines."
output: |
  true
  before
```

## Tricks to remember

- A Result is a parcel: an item or a note, never both.
- Open it: `getOrElse` for a spare, `fold` for two plans, `isFailure` to ask.
- `map` for a safe step, `mapCatching` for a step that may break.
- A parcel nobody opens hides the failure.

## Common mistakes

- **Treating `runCatching` as a safe replacement for `try` and `catch` everywhere.** It never stops the program, so it feels safer. It wraps every `Throwable`, including ones you should let through, such as the cancellation signal in the interview section. Use it where a caller can expect and use a failure.
- **Never checking the parcel.** The line compiles, runs and prints nothing wrong, so it looks fine. The failure is hidden, and later lines use a value that never arrived. Open the parcel with `getOrElse`, `fold` or `isFailure`.
- **Expecting `map` to pack a failure from its own step.** The name suggests it wraps everything. The documentation says `map` rethrows any exception thrown by its step. Use `mapCatching` when the step may break.
- **Wrapping a failure and forgetting why it happened.** `getOrNull()` is short, so it is tempting. It throws the note away. Keep the note with `exceptionOrNull()` or `fold` when the reason matters.

## Where this shows up in Android

Picture a login screen that shows a blank page when the network is down, instead of an error message. The Android coroutines guide builds a login call that hands back a sealed `Result` with `Success` and `Error`. The page says "we have our own Result class", so this is Android's sample class and not the one in this lesson. The idea is the same: a parcel. The caller checks it with `when`, and the `else` branch is where the page says to show the error in the UI. If that branch is left empty, the note is never read, which is the unopened parcel.

## Say it in an interview

**What is `Result<T>`, and how does it avoid throwing?**
It is a type that holds either a successful value of type `T` or a failure with a `Throwable`, as the Kotlin documentation says. A function can return it, and the caller checks which one it is, with calls such as `isSuccess`, `getOrNull`, `getOrElse` or `fold`. Nothing is thrown at the call site.

**How is `runCatching` different from `try` and `catch`?**
The documentation says `runCatching` calls the block and catches any `Throwable`, then returns it as a failure inside a `Result`. A `try` expression picks which exception types to catch. My own reasoning: `runCatching` is shorter when you want one value or a failure, but it catches everything, so it is less careful.

**Why be careful with `Result` as a public return type?**
The failure inside is always a `Throwable`, so the caller cannot tell from the type what can go wrong. My own reasoning: a caller can also forget to check the parcel, and the failure then disappears. A sealed class with named cases, like Android's `Success` and `Error` sample, lets the compiler check that each case is handled.

**What goes wrong when `runCatching` is used inside a coroutine?**
The documentation says `runCatching` catches any `Throwable`. The Android coroutines best-practices page says not to consume a `CancellationException`: do not catch it, or always rethrow it if caught. My own reasoning: a `runCatching` around work that can be cancelled may swallow the cancellation, so the work carries on when it should stop. Coroutines are not covered here.

## Recap

1. A `Result` holds either a value or a failure, so a function can return both outcomes without throwing.
2. `runCatching` wraps any failure, and `getOrElse` and `fold` open the parcel.
3. `map` lets a failing step escape and stop the program, but `mapCatching` packs it.

## You can now

- Predict what `runCatching` and `getOrElse` give for good text and bad text.
- Spot a `Result` that is never checked and explain what it hides.
- Predict which of `map` and `mapCatching` lets a failing step stop the program.
