---
id: kotlin/errors-and-results/try-catch-finally
title: How do I catch a failure and keep going?
status: shadow
difficulty: medium
order: 61
requires: [kotlin/errors-and-results/exceptions]
summary: Catch a failure, clean up and carry on. See which blocks run, and why an empty catch hides trouble.
video: ""
publishedOn:
updatedOn:
sims: [net-and-sweeper]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/docs/exceptions.html : a try block must be followed by at least one catch block or a finally block; try can be used as an expression and its value is the last expression of the try or catch block that ran; finally always executes whether or not the try block throws, and it does not change the result of the try-catch; the count() / ArithmeticException example.
- https://developer.android.com/guide/components/intents-filters : Kotlin sample "try { startActivity(sendIntent) } catch (e: ActivityNotFoundException) { ... }" with the comment about no activity being able to handle the intent.
- https://developer.android.com/kotlin/coroutines/coroutines-best-practices : "don't consume exceptions of type CancellationException (don't catch them, or always rethrow them if caught)"; "Prefer catching specific exception types like IOException over generic types like Exception or Throwable".
- https://developer.android.com/topic/performance/issues/crash : an unhandled exception crashes the app and shows a dialog.
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/errors-and-results/try-catch-finally.
-->

## The question

A trapeze artist is halfway through a show of three tricks when she misses a catch. If nothing is under her, the show ends there and the crowd goes home. But a show can have a net below it, and a cleaner who tidies the stage afterwards. Which parts of the show still happen after a fall? And does the net make the show safe, whatever goes wrong?

## Mental model

Think of a trapeze show with a safety net and a cleaner.

- The show is a group of tricks. Each trick is a line of code.
- A fall is the failure. When a line fails, the rest of the show is skipped.
- The net is under the show. It is made for one kind of fall, and the kind is written on it.
- Being caught by the net means the show ends early, and the act carries on after the show.
- The cleaner sweeps up after the show, whether anyone fell or not.
- A show can hand back a prize at the end. Either the last trick gives it, or the net does.

**Where the analogy stops:** A real net catches anyone who falls. A Kotlin net catches only the kind of fall written on it, and lets all other falls through. A real cleaner might be stopped by a fire. In Kotlin, the cleaner's code runs whether the show fell or not.

## See it move

<!-- SIM: net-and-sweeper -->

**Try this:**
1. Choose "Fall at trick 2", put the net under the small show and press Run.
2. Drag the net away and press Run again.
3. Put the net under the big show and press Run, then switch the sweeper off and run once more.

**What you should notice:** A net stops the fall, and play carries on after the show it sits under. The sweeper runs whether anyone fell or not. With no net, the act stops after the sweeper.

## Build the logic

### Notice

The show has three tricks. In a good show, all three happen, the net is never used and the cleaner sweeps. If the artist falls on trick 2, trick 3 never happens. The net below catches her, and the crowd sees the show carry on with whatever comes after. The cleaner sweeps in both cases. In a show with no net, the artist falls, the cleaner sweeps, and then the fall carries on outwards and stops every show above this one.

### Guess

Say the artist falls on trick 2 and a net is below. Which parts happen, and in what order? Here are three ideas.

- A. Trick 1, the net, then the cleaner.
- B. Trick 1, trick 3, then the net.
- C. Trick 1, the cleaner, then the net.

Idea B has trick 3 happening after the fall, but the rest of the show is skipped. Idea C lets the cleaner go first, but the net catches before the cleaner tidies. Idea A is the answer.

The net is a good tool, but how wide should it be? Here are three ideas.

- A. Write on it "any fall at all", so nothing can get past.
- B. Write on it only the kind of fall you expect and know how to deal with.
- C. Leave it blank, and let it do nothing when it catches.

Idea A feels safe, but it also catches falls you never planned for and cannot fix. Idea C catches the fall and then hides it, so nobody learns that something went wrong. Idea B is the answer.

### Build

1. Put the risky tricks inside a show.
2. Under the show, hang a net and write on it the one kind of fall it is for.
3. If a fall happens and its kind matches the net, the rest of the show is skipped and the net's steps run.
4. After the net's steps, the act carries on after the whole show.
5. If the kind does not match, the fall is not caught and it moves outwards. The act is not saved.
6. Put the cleaner's steps in a separate place. They run after the show in every case.
7. If the show is meant to hand back a prize, the prize is the last step of the trick or of the net.

### Name it

A **`try`** block is the show: the code that might fail. A **`catch`** block is the net: it names the kind of exception it handles, in brackets after `catch`, and holds the code that runs when one of that kind happens. A **`finally`** block is the cleaner. The Kotlin documentation says a `try` block must have at least one `catch` block or a `finally` block. It also says `finally` always executes, whether or not `try` throws.

```kotlin
fun divide(a: Int, b: Int): Int {
    return a / b
}

fun main() {
    try {
        println("trick 1")
        println(divide(10, 0))
        println("trick 3")
    } catch (e: ArithmeticException) {
        println("net caught it")
    } finally {
        println("sweeper")
    }
    println("after the show")
}
```

It prints `trick 1`, `net caught it`, `sweeper` and `after the show`. The call to `divide` failed, so `trick 3` was skipped. The net caught an `ArithmeticException`, the cleaner ran, and the program went on.

The documentation also says `try` is an expression. Its value is the last expression of the `try` block, or of the `catch` block if one ran. It adds that `finally` does not change this result.

```kotlin
fun divide(a: Int, b: Int): Int {
    return a / b
}

fun main() {
    val prize: Int = try {
        divide(10, 0)
    } catch (e: ArithmeticException) {
        -1
    } finally {
        println("sweeper")
    }
    println(prize)
}
```

It prints `sweeper` and then `-1`. The net handed back -1 as the prize, and the cleaner's lines did not change it.

## Predict the output

```challenge
type: predict
run: false
id: order-of-the-show
verified: kotlin-2.4.21
code: |
  fun main() {
      try {
          println("A")
          throw IllegalStateException("x")
      } catch (e: IllegalStateException) {
          println("B")
      } finally {
          println("C")
      }
      println("D")
  }
options:
  - id: a
    text: "A, B, D"
    why: "You may have thought the cleaner only comes when nobody fell. The cleaner comes every time."
  - id: b
    text: "A, C, B, D"
    why: "You may have thought the cleaner goes first. The net catches the fall first, and the cleaner tidies after."
  - id: c
    text: "A, B, C, D"
    correct: true
    why: "Trick A runs, then the fall. The net catches it and prints B. The cleaner prints C, and the act carries on with D."
output: |
  A
  B
  C
  D
```

```challenge
type: predict
run: false
id: wrong-net
verified: kotlin-2.4.21
code: |
  fun main() {
      try {
          println("start")
          throw IllegalStateException("x")
      } catch (e: IllegalArgumentException) {
          println("wrong net")
      } finally {
          println("sweeper")
      }
      println("after")
  }
options:
  - id: a
    text: "start, sweeper"
    correct: true
    why: "The net is written for another kind of fall, so it lets this one through. The cleaner still runs, and then the fall carries on outwards and ends the program."
  - id: b
    text: "start, wrong net, sweeper, after"
    why: "You may have thought any net catches any fall. A net catches only the kind written on it."
  - id: c
    text: "start, sweeper, after"
    why: "You may have thought the cleaner makes the show safe. It tidies up, but the fall is not caught, so after never runs."
output: |
  start
  sweeper
```

```challenge
type: predict
run: false
id: prize-from-the-net
verified: kotlin-2.4.21
code: |
  fun main() {
      val zero = 0
      val result = try {
          10 / zero
      } catch (e: ArithmeticException) {
          -1
      } finally {
          println("sweeper")
      }
      println(result)
  }
options:
  - id: a
    text: "-1 and then sweeper"
    why: "You may have thought the cleaner comes after the prize is shown. The cleaner finishes before the next line runs."
  - id: b
    text: "sweeper and then -1"
    correct: true
    why: "The division by zero falls and the net hands back -1 as the prize. The cleaner prints sweeper, and then println shows the prize."
  - id: c
    text: "sweeper and then 0"
    why: "You may have thought dividing by zero gives 0. It fails, and the net hands back -1."
output: |
  sweeper
  -1
```

## Tricks to remember

- Net first, cleaner second, then life goes on after the show.
- Write on the net only the fall you expect. A wide net hides surprises.
- The cleaner always comes, so keep clean-up work there.
- A show can hand back a prize: `val prize = try { ... } catch ... { ... }`.

## Common mistakes

- **Catching `Exception` everywhere to be safe.** It feels like one net for every fall. It also catches falls you never expected, and the program carries on in a bad state. The Android coroutines page says to prefer catching specific types like `IOException` over `Exception` or `Throwable`. Catch only what you can deal with.
- **Writing an empty catch.** The crash goes away, so it feels fixed. The failure is hidden, and the program carries on with a wrong result and no clue why. At least print the problem or hand back a clear value like -1.
- **Putting the net under the wrong show.** The risky line is in one show and the net is under another, so a fall slips past. Put the net under the show that holds the line that can fall.
- **Thinking `try` cannot give a value, and using a `var` set in two places.** Many languages treat it as a statement only (general knowledge). The documentation says `try` is an expression, so `val prize = try { ... } catch (e: SomeException) { -1 }` gives the value straight to a `val`. Clean-up still belongs in `finally`, which runs every time.

## Where this shows up in Android

Picture a button that shares a link. An **Intent** is a message that asks Android to start something, such as another app's screen (general knowledge). If no app on the phone can handle it, the app can close with the stopped dialog. The Android intents guide shows `try { startActivity(sendIntent) } catch (e: ActivityNotFoundException) { ... }`, with a comment to define what the app should do if no activity can handle the intent. That is a net written for one kind of fall, and the crash page says an unhandled exception is what crashes an app. The net here keeps the app open, but what goes inside it is the app's choice.

## Say it in an interview

**How do `try`, `catch` and `finally` work together?**
`try` holds the code that may fail. `catch` handles one kind of exception, and `finally` holds code that always runs. The Kotlin documentation says a `try` needs at least one `catch` or a `finally`, and that `finally` runs whether or not an exception is thrown.

**Can `try` return a value in Kotlin?**
Yes. The documentation says `try` is an expression, and its value is the last expression of the block that ran, either the `try` block or the `catch` block. It adds that `finally` does not change the result. So you can write `val n = try { ... } catch (e: SomeException) { -1 }`.

**What happens if the `try` block and the `catch` block both throw?**
The documentation says `finally` always executes. My own reasoning: the exception from the `catch` block is not caught by the same `try`, so `finally` runs first and the new exception then moves outwards.

**What can go wrong when you catch a broad `Exception` in Android code?**
The Android coroutines best-practices page says not to consume a `CancellationException`: do not catch it, or always rethrow it if caught. It also says to prefer specific types like `IOException`. My own reasoning: a broad catch can swallow a cancellation signal and leave work running that should have stopped. Coroutines are not covered here.

## Recap

1. `try` holds risky code, `catch` handles one kind of failure, and `finally` always runs.
2. Catch only the failures you expect and can deal with, and never leave a catch empty.
3. A `try` can hand back a value, and `finally` does not change it.

## You can now

- Predict the order of prints when `try`, `catch` and `finally` all run.
- Predict that a catch for the wrong kind of exception lets the failure through but `finally` still runs.
- Predict the value a `try` expression gives when a failure is caught.
