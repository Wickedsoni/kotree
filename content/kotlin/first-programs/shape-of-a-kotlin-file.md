---
id: kotlin/first-programs/shape-of-a-kotlin-file
title: How do I read the shape of a Kotlin file?
status: shadow
difficulty: easy
order: 3
requires: [kotlin/first-programs/where-a-program-starts]
summary: Curly braces mark where each group of steps starts and ends, and comments are notes the computer skips.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/kotlin-tour-hello-world.html (the body of a function is written within curly braces); kotlinlang.org/docs/control-flow.html (the condition of an if goes in parentheses and the action in curly braces); kotlinlang.org/docs/basic-syntax.html (single-line and block comments; block comments can be nested); kotlinlang.org/docs/coding-conventions.html (four spaces for indentation; omit semicolons whenever possible; semicolons are optional, so line breaks are significant); developer.android.com/develop/ui/compose/tutorial (Column arranges elements vertically; Column example with two Text calls). Own reasoning: indentation does not decide membership of a block (the pages give braces for blocks and list indentation as a style rule); an unclosed brace stops the file from compiling; the Android symptom. Outputs hand-traced, not compiled. -->
## The question

A Kotlin file is full of curly braces, the { and } marks, and some sit inside others. One line is pushed far to the right, so it looks as if it belongs to the group of steps above it. Which braces belong together, and which lines sit inside which group? Does how far a line is pushed to the right decide it?

## Mental model

Think of nested boxes in a parcel.

- The parcel is the whole file.
- A box holds a group of steps. A box can sit inside a bigger box.
- Opening the lid of a box starts the group. Closing that same lid ends it.
- Every step is one slip of paper in a box. It says one thing to do.
- A sticky note is words for people. The post worker who opens the boxes skips every note.

The rule of the parcel is simple. A lid you open must be closed before the box around it can be closed. So lids close in pairs, the innermost box first.

**Where the analogy stops:** In a real parcel you can see which box sits inside which. In a Kotlin file the computer does not look at the picture. It counts opening lids and closing lids. Also, a real sticky note stays on the outside of a box. A Kotlin note can sit at the end of a step's line, or even inside another note.

## Build the logic

### Notice

A parcel holds a big box called main. Inside it are three things, in this order:

1. A slip that says "save the hour".
2. A smaller box that opens only if the hour is before noon. Inside it is one slip: "print Good morning".
3. A slip that says "print Done", after the small box is closed.

Which slips belong to the small box?

### Guess

How can you tell which slips are inside the small box?

- A. The slips between the lid that opens the small box and the lid that closes it.
- B. The slips that are pushed furthest to the right on the page.
- C. Every slip below the small box's name until the end of the file.

Idea B is a common guess, because tidy files do push inner slips to the right. Idea C forgets that a lid ends the box. Idea A is the answer. The pushing to the right is a habit that helps people read, and the computer counts lids.

### Build

1. A box starts at its opening lid and ends at the closing lid that pairs with it.
2. Every slip inside is one step. The steps run from top to bottom.
3. A box can sit inside another box. Count lids to see which box a slip is in.
4. Every opened lid needs one closing lid. The latest lid still open is the first to close.
5. A sticky note is words for people. The computer skips it.
6. A step usually ends where its line ends. You do not need a mark to end it.

### Name it

A group of steps between `{` and `}` is a **block**. The Kotlin tour says the body of a function is written within curly braces. The documentation for `if` says the condition goes in parentheses and the action to take goes in curly braces.

An **`if`** runs the steps in its block only when the question in the parentheses `( )` is answered yes. In `if (hour < 12)` the question is "is hour smaller than 12?". The line `val hour = 9` gives the name `hour` to the number 9.

One instruction, such as `println("Done")`, is a **statement**. It usually ends at the end of its line. The coding conventions page says semicolons are optional and tells you to omit them whenever possible.

A **comment** is a note for people that Kotlin skips. `//` starts a note that runs to the end of the line. `/*` starts a note that runs until `*/`, over as many lines as you like. The documentation says block comments can be nested.

Here is the parcel from Notice, written as a file.

```kotlin
// This program greets in the morning.
fun main() {
    val hour = 9
    if (hour < 12) {
        println("Good morning") // inside the if block
    }
    println("Done") /* outside the if block */
}
```

It prints two lines: `Good morning`, then `Done`. The `if` block holds one statement. The line with `Done` comes after the closing brace of the block, so it is outside.

The coding conventions page says to use four spaces for indentation. My own reasoning: the pages give curly braces for blocks and list indentation as a style rule. So Kotlin does not read indentation to decide what is inside a block. It reads the braces.

## Predict the output

```challenge
type: predict
run: false
id: pushed-right-but-outside
verified: not-run
code: |
  fun main() {
      val hour = 15
      if (hour < 12) {
          println("Morning")
      }
          println("Hello")
      println("Done")
  }
options:
  - id: a
    text: "Morning, then Hello, then Done"
    why: "You may have thought the hour question is always yes. Here hour is 15, which is not below 12, so the small box stays closed."
  - id: b
    text: "Hello, then Done"
    correct: true
    why: "The small box closed at its lid on line 5, so Hello is outside it, however far right it sits. Hour 15 skips Morning. Hello and Done both run."
  - id: c
    text: "Done"
    why: "You may have thought the pushed-right line belongs to the small box. The computer counts lids and ignores the pushing, so Hello is outside and prints."
output: |
  Hello
  Done
```

```challenge
type: predict
run: false
id: lid-never-closed
verified: not-run
code: |
  fun greet() {
      println("Hi")

  fun main() {
      greet()
  }
options:
  - id: a
    text: "The file does not compile, because the box that greet opens is never closed"
    correct: true
    why: "greet opens one lid and no lid closes it. By my own reasoning, the computer reads main as a step inside greet, and then runs out of file while greet is still open."
  - id: b
    text: "It prints Hi"
    why: "You may have read greet and main as two separate boxes. The missing closing lid means main sits inside greet, and the file never finishes."
  - id: c
    text: "It compiles and prints nothing"
    why: "You may have thought the computer fixes the lid by itself. It does not guess where a box should end."
output: "The file does not compile, so nothing is printed"
```

```challenge
type: predict
run: false
id: notes-the-computer-skips
verified: not-run
code: |
  fun main() {
      println("A")
      // println("B")
      /* println("C")
         println("D") */
      println("E")
  }
options:
  - id: a
    text: "A, B, C, D, E"
    why: "You may have read each println as a step. Two of them sit inside sticky notes, and the computer skips notes."
  - id: b
    text: "A, C, D, E"
    why: "You may have thought only the double slash hides a line. The slash and star pair hides everything up to the closing star and slash."
  - id: c
    text: "A, E"
    correct: true
    why: "The double slash hides B for the rest of its line. The slash and star pair hides C and D. Only A and E are real steps."
output: |
  A
  E
```

## Tricks to remember

- Lids come in pairs. Open one, close one, innermost first.
- Pushed right is for people. Braces are for the computer.
- A comment is a sticky note the computer skips.
- When something is outside the block, count the lids from the top.

## Common mistakes

- **Trusting the indentation like in Python.** Tidy files push inner lines to the right, so the shape looks like the meaning. In Kotlin, the braces decide. Count the lids.
- **Closing the box one line too early.** The closing brace looks harmless. Every line after it is now outside the block. Check where each `}` pairs with its `{`.
- **Deleting one brace and reading the error far away.** The brace you lost is in the middle, yet the file only ends at the bottom. My own reasoning: match braces from the inside out, and find the pair that has no partner.
- **Leaving a step inside a comment by accident.** The note hides the step, and the program runs without it. Look for `//` and `/*` in front of a line that should run.

## Where this shows up in Android

**Jetpack Compose** is the toolkit for building Android screens in Kotlin, and its code is blocks inside blocks. The Android Compose tutorial uses `Column`, which it says lets you arrange elements vertically. Its example puts two `Text` calls, each of which shows a piece of text, between the braces of `Column`. One shows an author and the other shows a message body.

My own reasoning: if a closing brace lands one line too early, the second `Text` is no longer inside `Column`. A screen can then look wrong, with a piece of text away from the rest, while the file still compiles. Counting the lids shows which block each call is in.

## Recap

1. Curly braces mark where each group of steps starts and ends, and they nest in pairs.
2. Indentation helps people read, but Kotlin counts the braces to decide what is inside a block.
3. A comment is a note for people, and Kotlin skips it.

## You can now

- Predict which lines run when a line is pushed right but sits outside a block.
- Spot a block that is never closed.
- Predict which lines a `//` comment and a `/* */` comment hide.
