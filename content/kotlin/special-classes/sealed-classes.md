---
id: kotlin/special-classes/sealed-classes
title: How do I model loading, success or error so I never forget a case?
status: shadow
difficulty: hard
order: 52
requires: [kotlin/special-classes/enum-classes, kotlin/interfaces-and-inheritance/polymorphism-and-type-checks]
summary: A sealed type lists every kind a value can be. See why Kotlin stops the build when a when forgets one.
video: ""
publishedOn:
updatedOn:
sims: [three-lamp-signal]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live; read 21 Oct 2026):
- https://kotlinlang.org/docs/sealed-classes.html (definition, same package rule, exhaustive when with no else, sealed interface)
- https://kotlinlang.org/docs/control-flow.html (when as an expression must cover all cases)
- https://developer.android.com/topic/architecture/recommendations (UI state can be a sealed class if the states are exclusive)
- https://developer.android.com/kotlin/flow/stateflow-and-sharedflow (LatestNewsUiState sealed class with Success and Error, and the when over it)
Roadmap note about when statements versus expressions: the body claims exhaustiveness for expressions only, as the pages state.
The roadmap row's F(ai) and B tasks are not built: playground is false. The type is named LoadState, not Result.
Sim id three-lamp-signal appended to docs/sims/needed-ids.md. Roadmap row says balls and trays; the leaf uses lamps and a plan card to keep one analogy.
-->

## The question

A news screen can be waiting for data, can have the news, or can have failed with a reason. A programmer writes the code for waiting and for news, and forgets the failure. When the network drops, the screen shows a spinner for ever. How can the program make sure that every case is handled? And how can it keep telling you, even after someone adds a new case next year?

## Mental model

Think of a signal post with exactly three lamps and a driver with a plan card.

- The signal post is the whole set of states the screen can be in.
- Each lamp is one kind of state: "wait", "got news" and "problem".
- Some lamps carry a sign. The "got news" lamp shows a headline and the "problem" lamp shows a reason. The "wait" lamp shows nothing.
- The driver's plan card has one line for each lamp.
- The city office owns the signal. Only the city office can fit a new lamp to the post.
- A catch-all line on the card says "any other lamp: stop".

**Where the analogy stops:** A real lamp is on or off. Here the "got news" lamp can light again and again, and each time it may carry a different headline. Also a real city can fit a new lamp without telling the drivers. Kotlin tells the person who wrote the plan card, before any car moves.

## See it move

<!-- SIM: three-lamp-signal -->

**Try this:**
1. Press "Send a car". Watch the driver follow the plan line for each lamp in turn.
2. Remove the plan line for the "problem" lamp and press "Check the plan". Read the build light.
3. Press "Fit a fourth lamp" and check the plan again.
4. (Optional reveal) Tick "Add a catch-all line", fit the fourth lamp again, and watch which line the driver follows.

**What you should notice:** A plan card with a missing line turns the build light red before any car moves. A catch-all line keeps the light green, even though a new lamp is handled in a way nobody chose.

## Build the logic

### Notice

The signal for the news screen has three lamps: wait, got news and problem. The "got news" lamp carries a headline. The "problem" lamp carries a reason. The driver's plan card says: wait, stay put; got news, read the headline; problem, show the reason. One day the city fits a fourth lamp, "retrying", and nobody updates the card.

### Guess

How should the plan card be written so no lamp is forgotten? Here are three ideas.

- A. One line per lamp, and a last line that says "any other lamp: stop".
- B. A short card, and the driver learns the rest by heart.
- C. One line per lamp and no catch-all, so a missing line can be found.

Idea A feels safe. But a new lamp then falls into the last line and nobody is told. Idea B depends on memory. Idea C is the answer.

Who should be allowed to fit new lamps? Here are three ideas.

- A. Only the city office that owns the post.
- B. Any driver who wants one.
- C. Nobody, ever.

Idea B means no card could ever be complete. Idea C leaves the post stuck for ever. Idea A is the answer.

### Build

1. Fit the post with a fixed set of lamps. Only the city office may add one.
2. A lamp may carry a sign with facts of its own. Each time the lamp lights, the sign can say something new.
3. The driver's plan card has one line per lamp and no catch-all line.
4. To read a lamp's sign, first check which lamp it is. After that check, the sign can be read.
5. If the city adds a lamp, every card that must hand back an answer and lacks the line is refused before any car moves.
6. A catch-all line hides the new lamp, because the card never looks refused.

### Name it

A **sealed** type is a type with a closed list of kinds. It is written as a `sealed class` or a `sealed interface`. An **interface** is a list of things a type promises to do. A **subclass** is one kind of a type, written with a colon, as in `class Success(val text: String) : LoadState`. The colon is the same one you saw in `class Hammer : Tool`.

The Kotlin documentation says all direct subclasses of a sealed type are known at **compile time**, which means while the program is being built, before it runs. It also says no other subclasses may appear outside the **module** and **package** where the type is defined. A module is one built unit of a project. A package is a named group of code files. The documentation adds that direct subclasses must be declared in the same package.

The documentation says that when you use a `when` expression with a sealed type, the **compiler** checks exhaustively that all cases are covered. The compiler is the tool that checks your code and builds the program. With that check, you do not need an `else`. **Exhaustive** means every kind has a line. The control flow page says a `when` expression must cover all cases, or the compiler reports an error. This lesson claims that rule only for `when` used as an expression, the form that hands back a value.

| | Enum class | Sealed interface |
|---|---|---|
| One choice is | A single fixed value | A kind, made as often as you like |
| Facts it carries | The same facts (called properties) on every constant | Each kind can have its own facts |
| `when` without `else` | Allowed when every choice has a line | Allowed when every kind has a line |

This program has one plan line for each of three lamps.

```kotlin
sealed interface LoadState

class Loading : LoadState
class Success(val text: String) : LoadState
class Failed(val reason: String) : LoadState

fun describe(state: LoadState): String {
    return when (state) {
        is Loading -> "please wait"
        is Success -> "got " + state.text
        is Failed -> "problem: " + state.reason
    }
}

fun main() {
    println(describe(Loading()))
    println(describe(Success("news")))
    println(describe(Failed("no signal")))
}
```

It prints `please wait`, `got news` and `problem: no signal`. Each line starts with an **`is` check**, which asks whether the value is a given kind. Inside that line, the code can read the sign, such as `state.text`.

Kotlin refuses to run this one, because the plan card must hand back a text and the `Failed` lamp has no line.

```kotlin
// does not compile
sealed interface LoadState

class Loading : LoadState
class Success(val text: String) : LoadState
class Failed(val reason: String) : LoadState

fun describe(state: LoadState): String {
    return when (state) {
        is Loading -> "please wait"
        is Success -> "got " + state.text
    }
}

fun main() {
    println(describe(Loading()))
}
```

## Predict the output

```challenge
type: predict
run: false
id: which-lamp-which-line
verified: kotlin-2.4.21
code: |
  sealed interface Load
  class Waiting : Load
  class Done(val n: Int) : Load

  fun show(x: Load): String {
      return when (x) {
          is Waiting -> "wait"
          is Done -> "done " + x.n
      }
  }

  fun main() {
      println(show(Done(3)))
      println(show(Waiting()))
  }
options:
  - id: a
    text: "done 3, then wait"
    correct: true
    why: "The driver reads the lamp first and follows its own line. The first call is the Done lamp with the sign 3, and the second is the Waiting lamp."
  - id: b
    text: "wait, then done 3"
    why: "You may have thought the lines run in the order they are written. Only the line for the lit lamp runs, and the calls come in the order of main."
  - id: c
    text: "The program is refused, because there is no else"
    why: "You may have expected a catch-all line to be needed. Every kind of Load has a line, so the card is complete."
output: |
  done 3
  wait
```

```challenge
type: predict
run: false
id: catch-all-hides-a-lamp
verified: kotlin-2.4.21
code: |
  sealed interface Load
  class Waiting : Load
  class Done : Load
  class Failed : Load

  fun show(x: Load): String {
      return when (x) {
          is Done -> "ok"
          else -> "wait"
      }
  }

  fun main() {
      println(show(Done()))
      println(show(Failed()))
  }
options:
  - id: a
    text: "ok, then ok"
    why: "You may have thought a failure counts as done. The Failed lamp has no line of its own, so it follows the catch-all."
  - id: b
    text: "ok, then wait"
    correct: true
    why: "Done has its own line. Failed has none, so the catch-all line answers wait, and nobody is told that the lamp was forgotten."
  - id: c
    text: "The program is refused, because Failed has no line"
    why: "You may have expected the missing line to be spotted. The catch-all covers it, so Kotlin sees nothing missing."
output: |
  ok
  wait
```

```challenge
type: predict
run: false
id: read-the-sign-after-the-check
verified: kotlin-2.4.21
code: |
  sealed interface Load
  class Done(val n: Int) : Load
  class Failed(val reason: String) : Load

  fun main() {
      val items: List<Load> = listOf(Done(2), Failed("no signal"), Done(5))
      var total = 0
      for (x in items) {
          if (x is Done) {
              total = total + x.n
          }
      }
      println(total)
  }
options:
  - id: a
    text: "2"
    why: "You may have thought the loop stops at the first lamp. It visits every lamp in the list, in order."
  - id: b
    text: "The program is refused, because x.n is not allowed on a Load"
    why: "You may have thought the sign cannot be read from a plain Load. After the check that x is a Done, Kotlin lets you read its sign."
  - id: c
    text: "7"
    correct: true
    why: "The check lets only the Done lamps through. Their signs are 2 and 5, and the Failed lamp adds nothing."
output: |
  7
```

## Tricks to remember

- Closed list of lamps, one plan line each.
- No catch-all line on a card that must hand back an answer.
- Check the lamp first. Then read its sign.
- On your own machine, write a sealed interface for a network answer with `Loading`, `Success` and `Failed`. Write a `when` with a line for each. Then remove one line and read what Kotlin says.

## Common mistakes

- **Thinking a sealed type is the same as an enum.** Both have a closed list, so they feel alike. An enum choice is one fixed value. Each kind of a sealed type can carry its own facts, such as a headline or a reason.
- **Adding `else` to be safe.** It feels careful. A new kind falls into the last line, and the compiler never says so. Write one line for each kind.
- **Thinking sealed types cannot hold facts.** The empty lamp in the examples gives that impression. The `Success` and `Failed` kinds above hold a text each. The kinds are normal classes with properties.
- **Putting a subclass in another package.** It feels like the code could live anywhere. The documentation says direct subclasses must be in the same package, so Kotlin refuses a lamp fitted from outside.

## Where this shows up in Android

The Android page on exposing StateFlow shows a sealed class called `LatestNewsUiState` with a `Success` kind that holds news and an `Error` kind that holds an exception. The page then shows `when (uiState)` with an `is` line for each kind. **UI state** is the set of values a screen shows (general knowledge). The Android architecture recommendations page says a screen's UI state can be a sealed class if the different states are exclusive.

The symptom is a screen that never leaves its spinner, or stays blank after an error. My own reasoning: the code handled the waiting and news lamps and never looked at the problem lamp. A `when` with a line for every kind makes that forgotten line a build error. This lesson does not explain `StateFlow`.

## Say it in an interview

**What is a sealed class or interface?**
The Kotlin documentation says sealed classes and interfaces give controlled inheritance. All direct subclasses are known at compile time, and no other subclasses may appear outside the module and package where it is defined. So the list of kinds is closed.

**Why are sealed types a good fit for Loading, Success and Error?**
A screen is in exactly one of those states at a time, and each state may carry different facts, such as news or a reason. The Android recommendations page says UI state can be a sealed class if the states are exclusive. My own reasoning: the closed list lets the compiler check every `when`.

**How does the compiler check that a `when` is complete?**
The documentation says that with a sealed type the compiler checks that all possible cases are covered, and you do not need an `else`. The control flow page says a `when` expression must cover all cases, or the compiler reports an error. That is for the form that hands back a value.

**What goes wrong in a real project with sealed types?**
My own reasoning: an `else` line hides a new kind, so the new state is handled in a way nobody chose. The documentation says subclasses must stay in the module and package, so a type you get from a library cannot be extended by you. My own reasoning: if a library adds a kind in a new version, a `when` of yours that has no `else` may stop compiling, and that is the check working.

## Recap

1. A sealed type has a closed list of kinds, and each kind can carry its own facts.
2. A `when` that hands back a value must have a line for every kind.
3. An `else` line hides a new kind, so leave it out.

## You can now

- Predict which line of a `when` runs for each kind of a sealed type.
- Predict what a catch-all `else` answers for a kind that has no line of its own.
- Predict the result of reading a kind's facts after an `is` check inside a loop.
