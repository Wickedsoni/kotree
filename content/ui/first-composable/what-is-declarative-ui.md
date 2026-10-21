---
id: ui/first-composable/what-is-declarative-ui
title: "Why does Compose describe the screen instead of changing it step by step?"
status: shadow
difficulty: medium
order: 1
requires: []
summary: The screen is built from the current data. You describe how it should look, and you do not patch it step by step.
video: ""
publishedOn:
updatedOn:
sims: [declarative-vs-steps]
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/develop/ui/compose/mental-model ("a declarative API that lets you render your app UI without imperatively mutating frontend views"; "You update the UI by calling the same composable function with different arguments"; the imperative way is to "walk the tree using functions like findViewById()" and call methods like button.setText(String); "Manipulating views manually increases the likelihood of errors"; "If a piece of data is rendered in multiple places, you might forget to update one of the views that shows it"; "conceptually regenerating the entire screen from scratch, then applying only the necessary changes"; recomposition "calls only the functions or lambdas that might have changed, and skips the rest"; composable functions should be "fast, idempotent, and free of side-effects"; "A side-effect is any change that is visible to the rest of your app"). Interview data: docs/practice/interview-ui.md (its senior question about lifting state and its claim that Compose eliminates inconsistent UI are not on the page, so they are not used). The kotlin blocks and outputs are unchanged from the earlier draft, whose notes record runs on Kotlin 2.4.20 only; for this rewrite, the challenges were run with kotlin 2.4.21 (scripts/kt-run.sh) and every output matched, so slides-with-a-list, slides-with-a-description and fix-badge-left-behind say kotlin-2.4.21. Compose definition: https://developer.android.com/compose ("Jetpack Compose is Android's recommended modern toolkit for building native UI."). Common mistakes bullet 4: https://developer.android.com/develop/ui/compose/mental-model ("In Compose's declarative approach, widgets are relatively stateless and don't expose setter or getter functions."). All code is a model in plain Kotlin, not real Android. Sim declarative-vs-steps is new and is a code-to-screen sim, see docs/sims/needed-ids.md. -->

## The question

You build a screen with a slider and a number. The same number shows in two places: a title that says "Count is 5" and a badge that says "5 left". You move the slider five times quickly, and one label still shows an old value. Why does that happen? And is there a way to build the screen so it cannot happen?

## Mental model

Think of redecorating a room.

- The room is the screen.
- The photo of the finished room is a full picture of how everything should look right now.
- The painter is the one who makes the room match what they are given.
- A list of brush strokes is the other way to ask: "paint this wall blue, move the lamp left, remove the poster".

With brush strokes, the painter does only what the list says. If you forget one stroke, one wall stays old. With a photo, the painter makes the room match the photo, so nothing can be forgotten. When you want a new look, you hand over a new photo.

**Where the analogy stops:** A real painter repaints the whole room for each new photo. Compose does not redo all the work each time. It starts from the full description, then changes only what is different. Also, a photo is frozen. Your description is code, so it can work out the picture from a number.

## See it move

<!-- SIM: declarative-vs-steps -->

**Try this:**
1. Slide the number on the left panel from 0 to 8, one slide at a time. Then do it again on the right panel.
2. Slide five times quickly on both panels. Look at both labels on each panel.
3. (Optional reveal) Press "Skip one instruction" on the left panel and slide again.

**What you should notice:** The left panel follows a list of changes and drifts out of step when one change is missing. The right panel is described again from the number and always matches.

## Build the logic

### Notice

A screen shows a number in two places: a big title and a small badge. The number changes from 1 to 2 to 3.

With a list of changes, each new number needs two instructions: "change the title" and "change the badge". After ten new numbers, you have written twenty instructions. If someone adds a third place that shows the number, every list needs one more line.

### Guess

How could the screen always match the number?

- A. Keep a note of which places show the number, and update them in a fixed order.
- B. Be careful and never forget an instruction.
- C. Stop writing changes. Write one description of the whole screen that uses the number, and build the screen from it whenever the number changes.

Idea B works until the screen gets bigger or someone else edits it. Idea A is the same list in a different shape. Idea C removes the problem, because the screen has no old parts left to forget.

### Build

1. Pick the one thing that changes: the number.
2. Write the screen as a description that uses that number in every place it appears.
3. When the number changes, hand the new number to the description.
4. The screen is rebuilt from the new description, so every place matches.
5. To change what the screen looks like, edit the description. You do not write a list of changes.

### Name it

The number is the **state**: the data the screen depends on. Describing the screen from that data is **declarative UI**. Telling the screen what to change, step by step, is the **imperative** way.

The Android documentation says the common imperative way is to walk the tree of views using functions like `findViewById()` and change them with calls like `button.setText(String)`. A **view** is one item on the screen, such as a button or a piece of text. The documentation adds that manipulating views manually increases the likelihood of errors. It also says that if a piece of data is shown in several places, you might forget to update one of them.

Jetpack Compose is Android's recommended modern toolkit for building native UI. A **composable** is a Kotlin function that describes a piece of the screen. In Compose, the documentation says you update the UI by calling the same composable function with different arguments.

Here is the idea as a model in plain Kotlin, not real Android. The function takes the state and returns the whole screen as text.

```kotlin
// A model in plain Kotlin, not real Android.
fun describeScreen(count: Int): String {
    return "Title: Count is $count | Badge: $count left"
}

fun main() {
    println(describeScreen(1))
    println(describeScreen(2))
}
```

This prints `Title: Count is 1 | Badge: 1 left`, then `Title: Count is 2 | Badge: 2 left`. There is one function, so both places always agree.

## Predict the output

```challenge
type: predict
id: slides-with-a-list
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android.
  var label = "Count is 0"

  fun main() {
      var count = 0
      count = 5
      label = "Count is $count"
      count = 8
      println(label)
  }
options:
  - id: a
    text: "Count is 5"
    correct: true
    why: "The list of changes had one line for the 5 and none for the 8. The painter did only what the list said, so the label stayed old."
  - id: b
    text: "Count is 8"
    why: "You may have thought the label follows the number by itself. With a list of changes, nobody told the label about the 8, so it still shows the old picture."
  - id: c
    text: "Count is 0"
    why: "You may have thought no update ran. One did run, for the 5. The missing one is the update for the 8."
output: |
  Count is 5
```

```challenge
type: predict
id: slides-with-a-description
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android.
  fun describeScreen(count: Int): String {
      return "Count is $count"
  }

  fun main() {
      println(describeScreen(5))
      println(describeScreen(8))
  }
options:
  - id: a
    text: "Count is 5, then Count is 5"
    why: "You may have expected the first picture to stay. Each call is a new photo, so the new number is used."
  - id: b
    text: "Count is 8, then Count is 8"
    why: "You may have thought only the last number matters. Each call builds its own picture, so the 5 is shown first."
  - id: c
    text: "Count is 5, then Count is 8"
    correct: true
    why: "Each new number gets a fresh description of the whole screen. There is no old part left to forget."
output: |
  Count is 5
  Count is 8
```

## Try it

1. **Write.** Type the second program from Predict the output into the editor: `describeScreen` and then `main` with two `println` lines.
2. **Run.** You should see two lines, `Count is 5` and `Count is 8`.
3. **Break.** In `describeScreen`, replace `$count` with the digit `0` and change nothing else. Run it again.
4. **Fix.** Both lines now say `Count is 0`, because the description no longer uses the state. Put `$count` back and run it. The numbers follow again.

## Fix the bug

```challenge
type: fix
id: fix-badge-left-behind
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android.
  var title = "Cart: 0"
  var badge = "Badge: 0"

  fun addItem(items: Int) {
      title = "Cart: $items"
  }

  fun main() {
      addItem(1)
      addItem(2)
      println(title)
      println(badge)
  }
hints:
  - "Look at what addItem changes. Which of the two labels does it leave alone?"
  - "Instead of patching each label, build both lines again from the number."
expected: |
  Cart: 2
  Badge: 2
fix: |
  // A model in plain Kotlin, not real Android.
  fun describeCart(items: Int): List<String> {
      return listOf("Cart: $items", "Badge: $items")
  }

  fun main() {
      val screen = describeCart(2)
      println(screen[0])
      println(screen[1])
  }
explanation: "The broken version patched one label and forgot the other. The fixed version describes both labels from the same number, so they cannot disagree."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a short answer: this code has no null values in play. Question 4 is the useful one. `addItem` runs twice, and each run must leave every label correct.

## Tricks to remember

- Hand over a new photo, not a list of brush strokes.
- One number in, the whole screen out.
- If two places show the same data, build both from the same data.
- A forgotten brush stroke is the bug. A photo has nothing to forget.

## Common mistakes

- **Thinking Compose redraws the entire screen every time anything changes.** It feels right because you describe the whole screen each time. The Android documentation says Compose works by conceptually regenerating the entire screen from scratch, then applying only the necessary changes. It calls only the functions that might have changed and skips the rest.
- **Writing "change this label" code inside a description.** Old habits make it feel natural to patch. A description says how the screen looks for the state. The documentation says composable functions should be free of side-effects, and a side-effect is any change visible to the rest of your app.
- **Keeping the same number in two places.** A second copy feels like a handy shortcut. The copy can fall behind. Keep one state and let both places read it.
- **Expecting to find and edit a piece of the screen later.** In the older approach you looked a view up and changed it. The documentation says Compose widgets do not expose setter or getter functions, so you change the state and the description does the rest.

## Where this shows up in Android

Think of a shopping app where the cart title says "2 items" but the small badge on the cart icon still says "1". That is the drifting left panel from this page. In the older view-based approach, each place had its own change call. The Android documentation says that if a piece of data is shown in several places, you might forget to update one.

With Compose, the title and the badge are both described from the same cart count, and a new count builds the description again. If you see two parts of a screen disagreeing, ask whether they read the same state, or whether each one got its own patch.

## Say it in an interview

**What is the difference between imperative and declarative UI?**
In the imperative way, you find a view and change it step by step, for example with `findViewById()` and `setText`. In the declarative way, you describe how the screen looks for the current state, and you update it by calling the same composable function with different arguments. That is how the Android documentation puts it.

**Why does Compose describe the screen as a function of state?**
When every place on the screen is built from the same state, they cannot disagree. The documentation says manual updating increases the likelihood of errors, because you might forget to update one view that shows the data. The trade-off is that you must keep the state in one place.

**Does Compose redraw the whole screen on every change?**
No. The documentation says the technique works by conceptually regenerating the entire screen, then applying only the necessary changes. Recomposition means Compose runs the description again after a change. On recomposition it calls only the functions that might have changed and skips the rest. So describing everything does not mean redoing everything.

**Why should a composable function be free of side-effects?**
The documentation says a side-effect is any change visible to the rest of your app. It says you should never depend on side-effects from running a composable, because recomposition may be skipped. My own reasoning: a description that also edits other things can leave the screen out of step, which is the bug this page is about.

## Recap

1. A declarative screen is described from the current state, not edited step by step.
2. When the state changes, the description is used again with the new data.
3. Compose changes only what differs, so describing everything does not mean redrawing everything.

## You can now

- Predict which label is out of date after a series of changes made with a list of instructions.
- Fix a screen where one label was left behind by building both labels from the same state.
- Explain the difference between describing a screen from state and changing it step by step, using the photo and the brush strokes.
