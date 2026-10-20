---
id: ui/first-composable/what-is-declarative-ui
title: "Why does Compose describe the screen instead of changing it step by step?"
status: shadow
difficulty: medium
order: 1
requires: []
summary: Compose builds the screen from the current data. You describe how it should look for that data, and you do not patch it step by step.
video: ""
publishedOn:
updatedOn:
sims: [declarative-vs-steps]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. Android claims were checked on 21 Oct 2026 against https://developer.android.com/develop/ui/compose/mental-model (declarative API, "call the same composable function with different arguments", recomposition, "conceptually regenerating the entire screen from scratch, then applying only the necessary changes", only functions that might have changed are called). The sim id declarative-vs-steps has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/declarative-vs-steps.md; kind code-to-screen, so it needs the caption "A picture of what Android does. Not a real phone." and its sources). Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. Model code is plain Kotlin and labelled "not real Android". -->
## The question

You build a screen with a number on it and a slider. Slide the slider and the number should change. That sounds fine so far. But the screen also has a title that says "Count is 5" and a badge that says "5 left".

You move the slider five times quickly. One label still shows an old value. Why does that happen, and is there a way to build the screen so it cannot happen?

## Mental model

Think of redecorating a room.

- The room is the screen.
- The photo of the finished room is a full picture of how everything should look right now.
- The painter is the one who makes the room match what they are given.
- A list of brush strokes is the other way to ask: "paint this wall blue, move the lamp left, remove the poster".

With brush strokes, the painter does only what the list says. If you forget one stroke, one wall stays old. With a photo, the painter makes the room match the photo, so nothing can be forgotten. When you want a new look, you hand over a new photo.

**Where the analogy stops:** A real painter repaints the whole room for each new photo. Compose does not redo all the work each time. It starts from the full description, then changes only what is different. Also, a photo is frozen. Your description is a piece of code, so it can work out the picture from a number.

## See it move

<!-- SIM: declarative-vs-steps -->

**Try this:**
1. Slide the number on the left panel from 0 to 8, one slide at a time. Then do it again on the right panel.
2. Slide five times quickly on both panels. Look at both labels on each panel.
3. (Optional reveal) Press "Skip one instruction" on the left panel and slide again.

**What you should notice:** The left panel follows a list of changes and drifts out of step when one change is missing, while the right panel is described again from the number and always matches.

## Build the logic

### Notice

A screen shows a number in two places: a big title and a small badge. The number changes from 1 to 2 to 3.

With a list of changes, each new number needs two instructions: "change the title" and "change the badge". After ten new numbers, you have written twenty instructions. If someone adds a third place that shows the number, all the lists need one more line.

### Guess

How could the screen always match the number?

- A. Be careful and never forget an instruction.
- B. Keep a note of which places show the number, and update them in a fixed order.
- C. Stop writing changes. Write one description of the whole screen that uses the number, and build the screen from it whenever the number changes.

Idea A works until the screen gets bigger or someone else edits it. Idea B is the same list in a different shape. Idea C removes the problem, because the screen has no old parts left to forget.

### Build

1. Pick the one thing that changes: the number.
2. Write the screen as a description that uses that number in every place it appears.
3. When the number changes, hand the new number to the description.
4. The screen is rebuilt from the new description, so every place matches.
5. To change what the screen looks like, edit the description. You do not write a list of changes.

### Name it

The number is the **state**: the data the screen depends on. Describing the screen from that data is **declarative UI**. Telling the screen what to change, step by step, is the **imperative** way.

The Android documentation says that with the older imperative approach you walk the tree of views, for example with `findViewById()`, and change them with calls such as `button.setText(String)`. It warns that manipulating views by hand increases the likelihood of errors, and that the work grows with the number of views that need updating.

In Compose, the documentation says you update the UI by calling the same composable function with different arguments. A **composable** is a Kotlin function that describes a piece of the screen. The next lesson shows how one is written.

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
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
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
    text: "Count is 8"
    why: "You may have thought the label follows the number by itself. With a list of changes, nobody told the label about the 8, so it still shows the old picture."
  - id: b
    text: "Count is 5"
    correct: true
    why: "The list of changes had one line for the 5 and none for the 8. The painter did only what the list said, so the label stayed old."
  - id: c
    text: "Count is 0"
    why: "You may have thought no update ran. One did run, for the 5. The missing one is the update for the 8."
output: |
  Count is 5
```

```challenge
type: predict
id: slides-with-a-description
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
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
    text: "Count is 5, then Count is 8"
    correct: true
    why: "Each new number gets a fresh description of the whole screen. There is no old part left to forget."
  - id: c
    text: "Count is 8, then Count is 8"
    why: "You may have thought only the last number matters. Each call builds its own picture, so the 5 is shown first."
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
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
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

- **Thinking Compose redraws the entire screen every time anything changes.** It feels right because you describe the whole screen each time. The Android documentation says Compose conceptually regenerates the entire screen from scratch, then applies only the necessary changes. It calls only the functions that might have changed and skips the rest.
- **Writing "change this label" code inside a description.** Old habits make it feel natural to patch. A description says how the screen looks for the state. It should not hold a list of edits.
- **Keeping the same number in two places.** A second copy feels like a handy shortcut. The copy can fall behind. Keep one state and let both places read it.
- **Expecting to find and edit a piece of the screen later.** In the older approach you could look a view up and change it. In Compose you change the state, and the description does the rest.

## Where this shows up in Android

Think of a shopping app where the cart title says "2 items" but the small badge on the cart icon still says "1". That is the drifting left panel from this lesson. In the older view-based approach, each place had its own change call, and the Android documentation says that manual updating raises the chance of errors as the number of views grows.

With Compose, the title and the badge are both described from the same cart count, and a new count builds the description again. If you see two parts of a screen disagreeing, ask: do they read the same state, or does each get its own patch?

## Recap

1. A declarative screen is described from the current state, not edited step by step.
2. When the state changes, the description is used again with the new data.
3. Compose only changes what differs, so describing everything does not mean redrawing everything.

## You can now

- Explain the difference between describing a screen from state and changing it step by step, using the photo and the brush strokes.
- Predict which label is out of date after a series of quick changes made with a list of instructions.
- Fix a screen where one label was left behind by building both labels from the same state.
