---
id: kotlin/lambdas/closures
title: How does a lambda remember variables from outside it?
status: shadow
difficulty: medium
order: 35
requires: [kotlin/lambdas/lambda-syntax, kotlin/functions/scope-and-shadowing]
summary: A lambda can read and change variables from the code around it. See why it always uses the latest value.
video: ""
publishedOn:
updatedOn:
sims: [garden-window-view]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/lambdas.html ; https://kotlinlang.org/docs/inline-functions.html ; https://developer.android.com/develop/ui/compose/state -->
## The question

You make a small piece of code that prints a number from outside it. When you make it, the number is 1. Then the number changes to 3. When the piece of code finally runs, it prints 3. Why does it show what the number is now, and not what it was when the piece of code was made?

## Mental model

Think of a window onto a garden.

- The house is the small piece of code. Its walls keep out everything except what the window shows.
- The garden is the code around the house, where the small piece of code was made.
- A plant in the garden is a named value that can change. What is growing there right now is the value.
- The window connects the house to the plant. Looking through it is running the small piece of code.
- The gardener can swap the plant at any time. Whoever looks afterwards sees the new plant.
- A photo of the garden shows the plant as it was at one moment. It never updates.
- Two windows onto the same garden show the same plant.

**Where the analogy stops:** A real window only lets you look. In Kotlin, the small piece of code is called a lambda, and it can also reach out and change the plant. Also, a real house stays on its plot. A lambda can leave the place where it was made, for example when a function hands it back, and it still reaches the garden it came from.

## See it move

<!-- SIM: garden-window-view -->

**Try this:**
1. Press "Look through window A" and read what the window shows.
2. Press "Gardener swaps the plant". Look through window A and then through window B.
3. (Optional reveal) Tick "Take a photo first", swap the plant again, and compare the photo with the windows.

**What you should notice:** Both windows show the plant as it is now, and only the photo keeps the old plant.

## Build the logic

### Notice

A garden has one pot with a tulip. A house with window A looks onto it. The gardener swaps the tulip for a rose. Someone looks through window A and sees the rose. A second house with window B on the same garden also shows the rose. A photo taken when the tulip was there still shows the tulip.

### Guess

The gardener swapped the plant after window A was built. Who sees the rose? Here are three ideas.

- A. Only windows built after the swap.
- B. Every window onto the garden, each time someone looks through it.
- C. Nobody. Each window kept the tulip it saw when it was built.

Idea C feels right if you think a window is a photo. But a photo and a window are different things. Idea A mixes up when the window was built with when it is used. Idea B is the answer.

### Build

1. A window is attached to the plant's spot in the garden. It is not attached to what grew there at the time.
2. Every look through the window reads the plant as it is right now.
3. A house can also reach through the window and change the plant. Everyone else then sees the change.
4. Two windows onto the same plant share it.
5. To freeze the plant, take a photo: copy the plant into a separate pot and look at that pot.
6. A function that builds a house and hands it back builds a new garden each time it runs. Its houses see only that garden, and the garden lasts as long as a house that uses it (general knowledge).

### Name it

A **closure** is what a lambda can reach in the code around it. The Kotlin documentation says a lambda expression can access its closure, which includes the variables declared in the outer scope, meaning the code around the lambda. It says the variables captured in the closure can be modified in the lambda. To **capture** a variable means that the lambda keeps a connection to it.

My own reasoning: the lambda holds the variable itself and not a copy of its value. If it held a copy, a change made inside the lambda could not be seen outside it, and the documentation says such changes can be made.

This program shows the window and the garden.

```kotlin
fun main() {
    var plant = "tulip"
    val look = { println("I see a " + plant) }
    look()
    plant = "rose"
    look()
}
```

It prints `I see a tulip` and then `I see a rose`. The lambda `look` was made once, while `plant` held `tulip`. The second call shows the rose because the lambda reads the variable every time it runs.

This program shows a function that builds a house and hands it back.

```kotlin
fun makeCounter(): () -> Int {
    var count = 0
    return {
        count = count + 1
        count
    }
}

fun main() {
    val a = makeCounter()
    val b = makeCounter()
    println(a())
    println(a())
    println(b())
}
```

It prints `1`, `2` and `1`. Each call of `makeCounter` builds a new garden with its own `count`. The lambda `a` keeps counting in its garden even though `makeCounter` has finished. The lambda `b` has a different garden, so it starts at 1.

| Window and garden | In Kotlin |
|---|---|
| The house | The lambda |
| The garden | The code around the lambda |
| The plant | A variable |
| The window | The closure |
| Looking through the window | Running the lambda |
| A photo | A copy kept in a separate `val` |

## Predict the output

```challenge
type: predict
run: false
id: two-windows-one-plant
verified: not-run
code: |
  fun main() {
      var count = 0
      val add = { count = count + 1 }
      val show = { println("count is " + count) }
      add()
      add()
      show()
  }
options:
  - id: a
    text: "count is 2"
    correct: true
    why: "Both houses have a window onto the same plant. The add house changes it twice, and the show house reads it as it is now."
  - id: b
    text: "count is 0"
    why: "You may have thought show kept the 0 it saw when it was built. A window reads the plant at the moment someone looks."
  - id: c
    text: "count is 1"
    why: "You may have thought each call of add starts again from 0. The plant keeps what add wrote, so the second call makes it 2."
output: |
  count is 2
```

```challenge
type: predict
run: false
id: made-early-looked-late
verified: not-run
code: |
  fun main() {
      var n = 1
      val first = { println("n is " + n) }
      n = 2
      val second = { println("n is " + n) }
      n = 3
      first()
      second()
  }
options:
  - id: a
    text: "n is 1 and then n is 2"
    why: "You may have thought each lambda kept the number from the moment it was made. Both look onto the same plant, and nothing was kept."
  - id: b
    text: "n is 3 and then n is 3"
    correct: true
    why: "Both windows look onto n. By the time either one is used, the gardener has made n equal to 3."
  - id: c
    text: "n is 1 and then n is 3"
    why: "You may have thought only the first lambda is out of date. The first one was made early, but it still reads the plant as it is now."
output: |
  n is 3
  n is 3
```

```challenge
type: predict
run: false
id: photo-in-a-pot
verified: not-run
code: |
  fun main() {
      var n = 1
      val saved = n
      val look = { println(n + saved) }
      n = 10
      look()
  }
options:
  - id: a
    text: "2"
    why: "You may have thought the lambda froze both numbers. Only saved is a separate pot, and n is still the plant in the garden."
  - id: b
    text: "20"
    why: "You may have thought saved follows n. The line val saved = n copied the 1 into a separate pot, and that pot does not change."
  - id: c
    text: "11"
    correct: true
    why: "The window reads n as it is now, which is 10. The photo in saved still holds the 1 that n had when the photo was taken. The total is 11."
output: |
  11
```

## Tricks to remember

- A lambda is a window, not a photo.
- Two lambdas on the same variable share one plant.
- To freeze a value, copy it into a `val` before you make the lambda.
- Each call of a function that builds a lambda builds a new garden.

## Common mistakes

- **Believing a lambda copies the variable's value when it is made.** Handing numbers to a function copies them, so a lambda feels the same. A lambda keeps a connection to the variable. Copy the value into a `val` if you want a photo.
- **Expecting lambdas made at different times to show different values.** The order of the lines feels like it should decide. All of them look at the same variable and show its latest value when they run. Check where the variable last changed before the lambda runs.
- **Expecting two counters from the same builder to share one number.** They come from the same code, so they feel like one thing. Each call builds its own variable, so each counter counts alone.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its state page shows a text box with the lambda `onValueChange = { name = it }`. The variable `name` is declared just above it, in a line that starts `var name by remember`. This lesson does not explain the rest of that line. The lambda reaches `name`, which was made outside it. Each time a person types, Compose calls the lambda with the new text as `it`, and the lambda writes that text into `name`. The page does not use the word closure. Calling this a closure is my own naming.

This next part is my own reasoning. If the lambda held only a copy of the first value, `name` outside it would never change, and the box would show the same text however much a person typed.

## Say it in an interview

**What is a closure?**
It is a lambda together with the variables from the code around it that it uses. The Kotlin documentation says a lambda can access its closure, which includes the variables declared in the outer scope. The lambda keeps a connection to those variables.

**Does a lambda capture the value or the variable?**
The variable. The documentation says captured variables can be modified in the lambda. My own reasoning: that only works if the lambda holds the variable itself, which is why a lambda made early shows the latest value when it runs.

**What happens to a captured variable when the function that made it finishes?**
In the counter example, `count` lives on, because the returned lambda still uses it. Each call of `makeCounter` gets its own `count`. This is general knowledge, shown by the program above. My own reasoning: the trade-off is memory, because a lambda that outlives its function keeps those variables alive.

**How do you make a lambda use a value from one moment in time?**
Copy the value into a `val` before you make the lambda, and use the copy. The lambda then reads the copy, which never changes. The trade-off is that the lambda will not follow changes made to the original afterwards, so choose the copy only when you want that.

## Recap

1. A lambda keeps a connection to the variables around it, not a copy of their values.
2. When the lambda runs, it reads and can change the variable as it is at that moment.
3. To keep a value from one moment, copy it into a separate `val` first.

## You can now

- Predict what two lambdas that share a variable print.
- Predict what a lambda prints when its variable changed after the lambda was made.
- Predict a result when one value was copied into a `val` and another was not.
