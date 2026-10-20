---
id: foundation/programming-thinking/what-is-a-function
title: How do I give a set of steps a name and reuse it?
status: shadow
difficulty: easy
order: 19
requires: [foundation/programming-thinking/what-is-a-variable]
summary: Write a set of steps once, give it a name, and run it each time you say that name.
video: ""
publishedOn:
updatedOn:
sims: [recipe-card-call]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live): kotlinlang.org/docs/kotlin-tour-functions.html ("You can declare your own functions in Kotlin using the fun keyword"; hello() sample prints Hello, world!; "Function parameters are written within parentheses ()"; "The body of a function is written within curly braces {}"); kotlinlang.org/docs/basic-syntax.html ("An entry point of a Kotlin application is the main function"); developer.android.com/develop/ui/compose/mental-model ("build your user interface by defining a set of composable functions that take in data and emit UI elements"; "All composable functions must have this annotation"). Own reasoning: copy-paste drift symptom, "a function runs only when called" (language semantics, textbook level). Outputs hand-traced, not run (no kotlinc). -->
## The question

A game plays a ding and shows a star each time the player scores. That happens in ten places in the code. You could copy the two steps ten times. Then the day the star changes shape, you must find all ten copies.

Why not give the steps a name and use the name instead?

## Mental model

Think of a recipe card kept in a drawer, and a cook who follows it.

- The card has a name at the top, such as "Make toast".
- Under the name, the steps are written once: put bread in, press the lever, wait, take the toast out.
- A card in the drawer does nothing. The cook does not follow it until someone asks.
- To use the card, someone says its name aloud: "Make toast".
- When the name is said, the cook keeps a finger on the current place, goes to the card, follows every step from top to bottom, then returns to the finger and carries on.
- Saying the name twice means the cook follows the card twice.
- If a step changes, you fix it on the one card. Every later use follows the new step.

**Where the analogy stops:** A real cook reads a card with judgement. A cook may skip a step that makes no sense, or start cooking without being asked. A computer follows every step exactly as written, and only when the name is said. Also, a real card sits in one place. Your program can use the same card from as many places as you like.

## See it move

<!-- SIM: recipe-card-call -->

**Try this:**
1. Press "Step" and follow the finger. Watch where it goes when it reaches a line that says the card's name.
2. Press "Say the name again" and step through again.
3. Press "Remove every name line" and step through once more.

**What you should notice:** The finger jumps into the card at every line that says its name and comes back after, and a card whose name is never said is never followed.

## Build the logic

### Notice

A cook makes toast at breakfast and again at lunch. Writing out the four steps again at lunch wastes room, and a mistake can creep into the second copy.

Better to write the four steps once, on a card titled "Make toast". At breakfast the cook says "Make toast". At lunch the cook says it again. The card is the same both times.

### Guess

The card is written and put in the drawer. When does the cook follow it?

- A. As soon as the card is written.
- B. Each time someone says its name.
- C. Once, at the very start of the day.

Idea A feels natural, because writing steps down looks like starting them. But a written card only says how to make toast. It does not make any. Idea C would follow the card whether or not anyone wants toast. Idea B is the answer.

### Build

1. Write the steps once, under a name.
2. Put the card away. Nothing happens yet.
3. At the place where you want the steps done, say the card's name.
4. The cook keeps a finger on the current line, goes to the card, and follows every step in order.
5. The cook returns to the finger and carries on with the line after the name.
6. Saying the name again follows the card again.
7. To change a step, fix it on the card. Every use now follows the fixed step.

### Name it

A card is a **function**: a named set of steps. Writing the card is **declaring** the function. In Kotlin you write `fun`, then the name, then a pair of parentheses `()`, then the steps inside curly braces `{}`. The Kotlin tour says you declare your own functions with the `fun` keyword.

Saying the card's name is **calling** the function. You write its name and the parentheses.

You have already met one function. The Kotlin documentation says a Kotlin program starts in the `main` function. Here, `main` calls a card named `greet`.

```kotlin
fun greet() {
    println("Hello")
    println("Welcome")
}

fun main() {
    greet()
    println("Between")
    greet()
}
```

Kotlin starts in `main`. The first line calls `greet`, so the finger goes into the card and prints `Hello` and `Welcome`. Then it returns to `main` and prints `Between`. The last line calls `greet` again, and `Hello` and `Welcome` print a second time. The card is written once and followed twice.

## Predict the output

```challenge
type: predict
run: false
id: hello-called-twice
verified: not-run
code: |
  fun sayHello() {
      println("Hello")
  }

  fun main() {
      sayHello()
      sayHello()
  }
options:
  - id: a
    text: "Hello prints once"
    why: "You may have counted how many times the card is written. Count the calls instead. Each call follows the card once."
  - id: b
    text: "Hello prints three times"
    why: "You may have counted the card as one more use. Writing the card does nothing, and only the two calls follow it."
  - id: c
    text: "Hello prints twice"
    correct: true
    why: "The card is written once and called twice. Each call follows the card once, so Hello prints two times."
output: |
  Hello
  Hello
```

```challenge
type: predict
run: false
id: card-never-called
verified: not-run
code: |
  fun sayHello() {
      println("Hello")
  }

  fun main() {
      println("Start")
  }
options:
  - id: a
    text: "Only Start"
    correct: true
    why: "Nobody says the card's name, so the card stays in the drawer. Only Start prints."
  - id: b
    text: "Hello, then Start"
    why: "You may have expected the card to be followed as soon as it is written. A card does nothing until someone calls its name."
  - id: c
    text: "Start, then Hello"
    why: "You may have expected the card to be followed after main finishes. A card is followed only when it is called."
output: "Start"
```

```challenge
type: predict
run: false
id: call-in-the-middle
verified: not-run
code: |
  fun sayHello() {
      println("Hello")
  }

  fun main() {
      println("One")
      sayHello()
      println("Two")
  }
options:
  - id: a
    text: "Hello, One, Two"
    why: "You may have thought the card runs first because it sits first in the file. The place where a card is written does not set when it runs. The call does."
  - id: b
    text: "One, Hello, Two"
    correct: true
    why: "The finger prints One, then goes into the card at the call and prints Hello. It returns and prints Two."
  - id: c
    text: "One, Two, Hello"
    why: "You may have expected the call to wait until main finished. The finger goes to the card at the call and comes straight back."
output: |
  One
  Hello
  Two
```

## Tricks to remember

- Write the card once. Say its name as often as you like.
- Declaring writes the card. Calling follows it.
- A card in the drawer does nothing.
- After the card, the finger returns to the line after the call.

## Common mistakes

- **Expecting a function to run as soon as it is written.** Writing steps down looks like starting them. A written function only sits in the drawer. It runs when you call it.
- **Copying the steps instead of using a name.** Copying feels quicker today. The day a step changes, you must find every copy. With one card, you fix one place.
- **Losing track of where the finger goes next.** The lines after a call can look like they run before the card. The finger goes into the card, finishes it, and then returns to the line after the call.

## Where this shows up in Android

A button looks blue on one screen and green on another. Nobody changed the design on purpose. This part is my own reasoning, not the documentation's. If each screen has its own copy of the button's steps, a fix made in one copy never reaches the other.

Jetpack Compose is the modern toolkit for building Android UI. The Android documentation says you build your user interface by defining a set of **composable functions**, which take in data and emit UI elements. It says every one must have the `@Composable` **annotation**, a label written above the function. When one function describes the button, every screen calls the same card.

## Recap

1. A function is a named set of steps, written once.
2. Writing a function does nothing, and calling it follows its steps.
3. The program goes into the function at the call and returns to the next line.

## You can now

- Predict how many times a function's steps run when it is called several times.
- Spot a function that is written but never called.
- Predict the order of output when a call sits between two prints.
