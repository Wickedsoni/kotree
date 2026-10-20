---
id: foundation/first-code/run-your-first-program
title: Where can I run code without installing anything?
status: shadow
difficulty: easy
order: 8
requires: [foundation/first-code/what-is-code]
summary: You can type a Kotlin program into a web page, press Run, and read the answer. There is no project to create.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips this comment at go-live). Checked on 20 Oct 2026 against:
https://kotlinlang.org/docs/run-code-snippets.html ("Kotlin Playground is an online application for writing, running, and sharing Kotlin code in your browser."; "there's no need to create a new project and build it."; "Write the executable part in the body of the main() function."; "IntelliJ IDEA and Android Studio support Kotlin scratch files."; the page has no statement about login or installing the Playground);
https://kotlinlang.org/docs/kotlin-tour-hello-world.html ("The main() function is where your program starts from"; "println() and print() functions print their arguments to standard output"; the page does not say how the two differ);
https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/println.html (println "Prints the given message and the line separator to the standard output stream.");
https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/print.html (print "Prints the given message to the standard output stream.");
https://kotlinlang.org/docs/strings.html ("To declare a String literal, enclose the value in double quotes");
https://developer.android.com/kotlin ("Kotlin is fully supported for Android development");
https://developer.android.com/studio/debug/logcat (Logcat shows "messages that you added to your app with the Log class, messages from services that run on Android, or system messages"; the page does not mention println).
Run status: the Name it block and the three Predict items were hand-traced only, not compiled (no shell in the authoring session). Predict items are run: false, verified: not-run. The Playground is not built on this site yet, so the page has no editor. -->
## The question

By now you know that a computer needs exact instructions. Now you want to try some. You have a laptop with a web browser, and nothing else.

Do you have to install big tools first? Or is there somewhere you can type a few lines, press a button, and see what happens?

## Mental model

Think of writing a note and pressing a doorbell.

- The note is your code. You write it before you ring.
- The doorbell is the Run button. One press sends the note.
- The helper behind the door is a computer that reads your note and does what it says.
- The reply pushed under the door is the answer you see: the text your program prints.

You write, you ring, you read the reply. If the note has a mistake, the reply is a complaint instead of an answer. You fix the note and ring again.

**Where the analogy stops:** A real doorbell does not read anything. Run sends your whole note away to be carried out, so it needs an internet connection. A real helper might also guess what you meant. This one never does, as you saw in the last lesson. And this page has no doorbell of its own: you ring it on another web page, which you will find below.

## Build the logic

### Notice

Zed has a laptop and wants to see his first program run. He has heard that programmers use large tools with many menus. He has not installed any of them. He wonders if he is stuck.

### Guess

What is the least he needs? Here are three ideas.

- A. He must install a full set of programmer tools before he can run a single line.
- B. A web page with a box to type in, a button to press, and a place to read the answer.
- C. A second computer that already has a program installed.

Idea A is true for building big projects, which comes later. It is not true for a first program. Idea C is a believable guess, but it adds a computer where none is needed. Idea B is enough.

### Build

1. You need a place to type your code.
2. You need a button that sends the code to something that can carry it out.
3. You need a place that shows what came back.
4. If what came back is a complaint, you read it, fix the code, and press the button again.

Nothing in these four steps needs a project or a build.

### Name it

The place that does all this is the **Kotlin Playground**. The Kotlin documentation calls it an online application for writing, running and sharing Kotlin code in your browser. The same page says that for quick checks there is no need to create a new project and build it.

It has three parts. The **editor** is the box where you type. The Run button sends your code. The **output** is the area below that shows what the program printed.

A Kotlin program for the Playground has this shape. You write the part that runs inside **`main()`**, which is where the program starts. The line **`println`** prints what is inside the brackets and then moves to a new line. Text is wrapped in double quotes, and a piece of text like that is a **string**.

```kotlin
fun main() {
    println("Hi")
}
```

This program prints `Hi`. The quote marks mark where the text begins and ends, and they are not printed.

**Where you press Run today.** This page cannot run code for you. To ring the doorbell yourself, open the Kotlin Playground at `play.kotlinlang.org`, which is the address the Kotlin documentation gives. Paste the program above and press Run.

The Kotlin documentation names one more quick way. IntelliJ IDEA and Android Studio support Kotlin scratch files, which run drafts on the fly. That needs an installed tool, so start with the Playground.

## Predict the output

Read each program and choose an answer before you open the explanation. You need no editor for these.

```challenge
type: predict
run: false
id: print-hi
verified: not-run
code: |
  fun main() {
      println("Hi")
  }
options:
  - id: a
    text: "\"Hi\" with the quote marks"
    why: "You may have copied the quote marks too. They only tell Kotlin where the text starts and stops, so they are not printed."
  - id: b
    text: "Hi"
    correct: true
    why: "The doorbell sends the note, and the reply is the text between the quotes."
  - id: c
    text: "println(\"Hi\")"
    why: "You may have expected the line itself to come back. The reply is what the line prints, not the line."
output: "Hi"
```

```challenge
type: predict
run: false
id: text-is-not-sum
verified: not-run
code: |
  fun main() {
      println("2 + 3")
  }
options:
  - id: a
    text: "2 + 3"
    correct: true
    why: "The quotes say copy this exactly. The helper behind the door copies the text and does no sums on it."
  - id: b
    text: "5"
    why: "You may have expected the computer to add. Anything between quotes is text and is copied as it is."
  - id: c
    text: "The program stops, because 2 + 3 is not a word"
    why: "You may have thought text must be a word. Any characters between the quotes are fine."
output: "2 + 3"
```

```challenge
type: predict
run: false
id: find-the-missing-quote
verified: not-run
code: |
  One line of this program is wrong. Which line would you fix?

  fun main() {
      println("Welcome")
      println("Step 1: write the code)
      println("Step 2: press Run")
  }
options:
  - id: a
    text: "The line with Welcome"
    why: "You may have looked at the first line only. It has an opening quote and a closing quote, so it is complete."
  - id: b
    text: "The line with Step 2"
    why: "You may have picked it because it is last. It has both quote marks, so nothing is missing."
  - id: c
    text: "The line with Step 1"
    correct: true
    why: "Text sits between two double quotes. This line opens a quote and never closes it, so Kotlin cannot tell where the text ends."
output: "The Step 1 line is missing its closing quote mark, after the word code."
```

## Tricks to remember

- Note, doorbell, reply. Write, Run, read.
- Quote marks wrap text. One opening quote needs one closing quote.
- The reply is what the program prints, not the code itself.
- An error is a reply too. Read it, fix the note, ring again.

## Common mistakes

- **Thinking you must install big tools first.** Programmers talk about large tools, so it feels like a first step. For a first program in the browser, you do not need them. They matter later, for whole apps.
- **Reading code and expecting output without pressing Run.** The code is on the screen, so it feels as if it has already happened. A note does nothing until the doorbell rings.
- **Reading an error as a sign you broke something.** A red message feels like a scolding. It is a reply that says where the note went wrong. Read the line it points to, fix it, and run again.
- **Forgetting the closing quote.** The opening quote feels like the whole job. Check that every opening quote has a closing one.

## Where this shows up in Android

The Playground runs plain Kotlin, not Android apps. A real Android app is built with Android Studio, a tool you install on your computer.

The lines you run here are the same kind of Kotlin that apps are written in. The Android documentation says Kotlin is fully supported for Android development. So a button that does nothing on a phone is, deep down, a few missing or wrong lines like these.

An app has no output area under a button. To see what an app is doing, developers read Logcat, a window in Android Studio. The Android documentation says Logcat shows messages that the developer added to the app, plus messages from the system. It plays the same part as the output area: the place where you read the reply.

## Say it in an interview

**Q: How would you try a few lines of Kotlin quickly, without making a project?**
A: I would open the Kotlin Playground in a browser, type the code, and press Run. The Kotlin documentation also names scratch files in IntelliJ IDEA and Android Studio. The trade-off is that the Playground needs an internet connection, while a scratch file needs an installed tool.

**Q: Where does a Kotlin program start?**
A: In the `main()` function. The Kotlin documentation says that is where the program starts from. Code outside it does not run by itself.

**Q: What is the difference between `print` and `println`?**
A: Both print a message to standard output. The documentation says `println` also prints the line separator, so the next message starts on a new line. `print` does not, so the messages stay on one line.

**Q: A phone app has no console under its buttons. How do you see what it prints?**
A: In Android Studio I would open Logcat. The Android documentation says it shows messages the developer added to the app, plus system messages. In my own reasoning, it plays the role the output area plays in the Playground.

## Recap

1. The Kotlin Playground lets you type a program, press Run, and read the output in your browser.
2. The reply is what the program prints, and text goes between double quotes.
3. An error is a reply too, so fix the line it points to and run again.

## You can now

- Explain where to run a first Kotlin program without installing anything.
- Predict what a one-line `println` program prints.
- Find the line in a short program whose text is missing its closing quote.
