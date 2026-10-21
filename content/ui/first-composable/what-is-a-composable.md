---
id: ui/first-composable/what-is-a-composable
title: "What makes a Kotlin function a composable?"
status: live
difficulty: easy
order: 2
requires: [ui/first-composable/what-is-declarative-ui]
summary: A composable is a Kotlin function marked with @Composable that describes a piece of the screen, and only another composable can call it.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---


## The question

In a tutorial you see two Kotlin functions side by side. One has the line `@Composable` above it, and the other does not. Both look like ordinary Kotlin. What does that one line change? And why does Android Studio complain when you call the first function from the second?

## Mental model

Think of recipe cards with a stamp.

- A recipe card is a function. It says how to make one thing.
- The stamp says "this card can go on the display shelf".
- The display shelf is the description of your screen. What sits on the shelf is what the user sees.
- A card can ask for other cards to be put beside it. A stamped card can ask for other stamped cards.
- A plain card, with no stamp, cannot ask for a stamped card to be put on the shelf.

A card with a stamp is still a recipe. The stamp only says where it is allowed to go and what the kitchen may do with it.

**Where the analogy stops:** A stamp on paper does nothing by itself. The stamp in Kotlin is read by a tool that rewrites the function while the app is built. Also, a recipe card gives you a dish to keep. A composable gives you nothing back. It describes a piece of the screen. And a card is used once per order, but a composable can run again whenever its data changes.

## Build the logic

### Notice

Here are two functions that both greet a person. The first is stamped. The second is not.

| Function | Stamp | What it is for |
|---|---|---|
| `Greeting(name)` | `@Composable` | Describes a greeting on the screen |
| `logTap()` | none | Prints a line to the log |

Both are written in Kotlin and take a few lines. Only the first one is meant to go on the display shelf.

### Guess

How could the tools know which functions describe the screen?

- A. By running every function and seeing which ones draw something.
- B. By their names: a function with "Screen" in its name is for the screen.
- C. By a marker written on the function that the build tool can read.

Idea B breaks as soon as someone picks a different name. Idea A would run code only to find out what it is. Idea C is the stamp.

### Build

1. Write a function that takes the data it needs, such as a name.
2. Put the stamp on it, `@Composable`.
3. Inside it, ask for other stamped cards, such as `Text`, which comes from the Compose library.
4. Call your function from another stamped function, or from the `setContent` block, where an activity starts its screen.
5. When the app is built, a tool reads the stamp and turns the function into part of the screen.
6. A plain function that tries to call a stamped one is rejected, since a plain card cannot ask for a stamped card.

### Name it

- An **annotation** is a label written with an `@` before a function. It gives the tools extra information about that function.
- **`@Composable`** is the annotation for composables. The Android documentation says all composable functions must have it, and that it informs the Compose compiler that the function is meant to convert data into UI.
- A **composable function**, or a **composable**, is a function with that annotation. The documentation says composable functions take in data and emit UI elements.
- **Recomposition** is the process of calling your composable functions again when their inputs change (mental-model docs). A **side-effect** is any change that is visible to the rest of your app. **Idempotent** means that running the function again with the same input gives the same result (general knowledge).
- **`setContent`** is where an activity starts its screen. The tutorial says the `setContent` block defines the activity's layout, and that composable functions are called there. An **activity** is one screen of an Android app.
- **`Text`** is a composable from the Compose library. The tutorial says it displays a text label on the screen.
- A **compiler plugin** is an add-on to the Kotlin compiler. The documentation says Compose uses one to transform composable functions into the app's UI elements.

Here is a real composable. It needs the Compose libraries, so it only builds inside an Android project.

```kotlin
// does not compile
// It needs the Compose libraries, so it builds only inside an Android project.
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
```

The function has no `return` line, and it gives back no value. The documentation says functions that emit UI don't need to return anything, because they describe the target screen state instead of constructing UI widgets. The examples in the documentation start composable names with a capital letter, such as `MessageCard`.

The rule from the documentation is: "Composable functions can only be called from other composable functions." Here is a call that breaks it.

```kotlin
// does not compile
// A plain function cannot call a composable.
fun onSaveClicked() {
    Greeting("Ana")
}
```

The fix is not to stamp this handler. A click handler is not a place for describing the screen. My own reasoning: a handler should change the data, and the stamped functions then describe the screen from that data, as in the last lesson.

This page has no program to run. Compose code needs an Android project.

## Predict the output

```challenge
type: predict
run: false
id: which-function-fails
verified: not-run
code: |
  // does not compile for one of the three functions below
  @Composable
  fun Title() { Text("Hi") }

  @Composable
  fun Page() { Title() }

  fun Footer() { Title() }

  Which function is rejected?
options:
  - id: a
    text: "Title"
    why: "You may have blamed the function that calls Text. Title has the stamp, so it can ask for other stamped cards."
  - id: b
    text: "Page"
    why: "You may have thought a stamped function cannot call another stamped one. The documentation says composables are called from other composables, and Page is one."
  - id: c
    text: "Footer"
    correct: true
    why: "Footer has no stamp, and it asks for a stamped card. The documentation says composable functions can only be called from other composable functions."
output: |
  Footer
```

```challenge
type: predict
run: false
id: what-does-a-composable-return
verified: not-run
code: |
  @Composable
  fun Greeting(name: String) {
      Text(text = "Hello $name!")
  }

  What does a call to Greeting("Ana") give back to its caller?
options:
  - id: a
    text: "A text view object that the caller can keep and change later"
    why: "You may have expected a widget to hold on to. The documentation says composables describe the screen state instead of constructing UI widgets."
  - id: b
    text: "Nothing: it describes a piece of the screen"
    correct: true
    why: "The documentation says the function doesn't return anything, because it describes the target screen state. To change the screen, you change the data it was given."
  - id: c
    text: "The String \"Hello Ana!\""
    why: "You may have read the string inside Text as the result. That string is passed on to Text, and Greeting itself returns nothing."
output: |
  Nothing: it describes a piece of the screen
```

```challenge
type: predict
run: false
id: composable-in-click-handler
verified: not-run
code: |
  A button's click handler is written like this:

      onClick = { Greeting("Ana") }

  Greeting has the @Composable stamp.
  The click handler is a plain function, with no stamp.
  Does this call work?
options:
  - id: a
    text: "No: a plain function cannot call a stamped one"
    correct: true
    why: "The stamp on Greeting is not enough. The caller must also be a stamped card, and a click handler is a plain function (general knowledge)."
  - id: b
    text: "Yes, because Greeting has the stamp"
    why: "You may have thought the stamp opens every door. The rule is about the caller: only stamped functions may call stamped ones."
  - id: c
    text: "Yes, but only the first time the button is tapped"
    why: "You may have thought the problem shows up later. This is rejected when the app is built, before anyone taps anything."
output: |
  No: a plain function cannot call a stamped one
```

## Tricks to remember

- Stamp the card, and then it may go on the shelf. Stamped can call stamped. Plain cannot call stamped.
- A composable describes the screen, and it gives nothing back.
- A click handler changes data. It does not draw.
- Task, in an Android project with Compose: write a small `@Composable` function that shows a `Text`, call it from the `setContent` block, then remove the stamp and read the build error.

## Common mistakes

- **Thinking a composable returns a View object I can keep.** The old view approach had objects to hold on to, so it feels natural. The Android documentation says a composable doesn't return anything, because it describes the target screen state. Change the data, not a widget.
- **Calling a composable from a click handler or a plain function.** It looks like any other function call. The documentation says composable functions can only be called from other composable functions. Change the state in the handler, and let the screen description follow.
- **Thinking the stamp is only a comment.** It sits on one line, so it feels like decoration. The documentation says the Compose compiler uses it to transform the function. Leaving it off changes what the build does.
- **Thinking a composable runs once, top to bottom.** A normal function does, so it feels the same. The documentation says composable functions can execute in any order, and that recomposition skips as much as possible. Do not rely on the order of lines, or on a function running.

## Where this shows up in Android

You paste a function into your project, and the build reports an error on a call to `Text`. My own reasoning: the function is missing its `@Composable` stamp, or it is being called from a plain function. The sentence from the documentation explains it: composable functions can only be called from other composable functions.

A second symptom: you add a line that prints to the log inside a composable, and the line appears more than you expected. The documentation says recomposition runs composables again when data changes. My own reasoning: the function runs whenever Compose needs it, so a composable should only describe the screen and not do other jobs.

## Say it in an interview

**What makes a Kotlin function a composable?**
The `@Composable` annotation. The Android documentation says all composable functions must have it, and that it informs the Compose compiler that the function is meant to convert data into UI. These functions take in data and emit UI elements, and they do not return anything.

**What does the annotation do, and how does Compose use it?**
The documentation says Compose uses a Kotlin compiler plugin to transform composable functions into the app's UI elements. From general knowledge, not from the pages I read: the plugin also adds a hidden extra parameter to each composable so that the runtime can track it. I would say only the first part with certainty.

**Why can a composable run out of order, run again, or be skipped?**
The documentation says composable functions can execute in any order, and that recomposition skips as many composables as possible. So a composable must be fast, idempotent and free of side-effects. The same page notes that they cannot currently run in parallel, but Compose code should be written as if they could.

**What goes wrong when a composable has side effects or blocks?**
The documentation says to depend on no side-effects from running a composable, because recomposition may be skipped. My own reasoning: blocking work in a composable holds up the screen description, and changing state while composing can cause more recomposition. Put such work in a click handler or in a place built for side effects.

## Recap

1. A composable is a Kotlin function marked `@Composable` that describes a piece of the screen.
2. Only another composable can call a composable, so a plain function cannot.
3. A composable returns nothing, and Compose may run it again, in any order, or skip it.

## You can now

- Pick the function that fails to build from a short list of composables and plain functions.
- Explain what a composable gives back to its caller, and what it does not.
- Explain, using the stamped recipe card, why a click handler cannot call a composable.
