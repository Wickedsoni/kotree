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
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: (1) NOT COMPILED OR RUN: no shell in this session. Every kotlin block and every challenge block here is UNVERIFIED until run on Kotlin 2.4.21 (docs/06 section 5). The Break step (println misspelled as printn) and the Fix the bug program (missing closing quote) are expected to fail to compile; confirm by running, and do not quote the message. (2) Roadmap note: confirm that the Kotlin Playground still embeds and runs without login and without anything to install on the publish date. play.kotlinlang.org did not load from this session. The body says only what kotlinlang.org says: it is an online application in the browser, and you need no project or build. (3) The body says Run needs an internet connection; this follows from the Playground being an online application (docs/06 section 2.3), not from a sentence in kotlinlang.org. (4) Try it holds no code fence: how a Try it fence becomes runnable is open (docs/06 E7), so step 1 points at the first Predict item. (5) The sim list is empty on purpose: the roadmap row says the live Playground on the page is the demo.
Checked on 21 Oct 2026 against: https://kotlinlang.org/docs/run-code-snippets.html ("Kotlin Playground is an online application for writing, running, and sharing Kotlin code in your browser."; "there's no need to create a new project and build it."; "Write the executable part in the body of the main() function."); https://kotlinlang.org/docs/kotlin-tour-hello-world.html ("The main() function is where your program starts from"; "println() and print() functions print their arguments to standard output"); https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/println.html (println "Prints the given message and the line separator to the standard output stream."); https://kotlinlang.org/docs/strings.html ("To declare a String literal, enclose the value in double quotes"); https://developer.android.com/kotlin ("Android's modern UI toolkit is built on Kotlin."); https://developer.android.com/studio/debug/logcat (Logcat shows "messages that you added to your app with the Log class"; the page does not mention println). -->
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

**Where the analogy stops:** A real doorbell does not read anything. Run sends your whole note away to be carried out, and it needs an internet connection to do that. Also, a real helper might guess what you meant. This one never does, as you saw in the last lesson.

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

The place that does all this is the **Kotlin Playground**. The Kotlin documentation calls it an online application for writing, running and sharing Kotlin code in your browser, and says there is no need to create a new project and build it.

It has three parts. The **editor** is the box where you type. The Run button sends your code. The **output** is the area below that shows what the program printed.

A Kotlin program for the Playground has this shape. You write the part that runs inside **`main()`**, which is where the program starts. The line **`println`** prints what is inside the brackets and then moves to a new line. Text is wrapped in double quotes, and a piece of text like that is a **string**.

```kotlin
fun main() {
    println("Hi")
}
```

This program prints `Hi`. The quote marks mark where the text begins and ends, and they are not printed.

## Predict the output

```challenge
type: predict
id: print-hi
verified: "UNVERIFIED: compile not run"
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
output: |
  Hi
```

```challenge
type: predict
id: text-is-not-sum
verified: "UNVERIFIED: compile not run"
code: |
  fun main() {
      println("2 + 3")
  }
options:
  - id: a
    text: "5"
    why: "You may have expected the computer to add. Anything between quotes is text and is copied as it is."
  - id: b
    text: "2 + 3"
    correct: true
    why: "The quotes say copy this exactly. The helper behind the door copies the text and does no sums on it."
  - id: c
    text: "The program stops, because 2 + 3 is not a word"
    why: "You may have thought text must be a word. Any characters between the quotes are fine."
output: |
  2 + 3
```

```challenge
type: predict
id: two-lines
verified: "UNVERIFIED: compile not run"
code: |
  fun main() {
      println("Hello")
      println("Kotlin")
  }
options:
  - id: a
    text: "Hello Kotlin on one line"
    why: "You may have expected a space in between. println ends the line after each message, so each text gets its own line."
  - id: b
    text: "Hello on one line, then Kotlin on the next line"
    correct: true
    why: "Each println prints its message and then moves to a new line, so the replies stack under each other."
  - id: c
    text: "HelloKotlin on one line"
    why: "You may have thought the texts would be glued together. println moves to a new line each time, so they are not."
output: |
  Hello
  Kotlin
```

## Try it

1. **Write.** Copy the program from the first Predict item into the editor. Change the text between the quote marks to `My first program ran`. Keep the quote marks.
2. **Run.** Press Run. The output area shows `My first program ran` on one line.
3. **Break.** Change exactly one thing: change `println` to `printn`. Press Run again. The program does not run, and the editor shows an error message instead of your text.
4. **Fix.** Change `printn` back to `println` and press Run. You should see `My first program ran` again. The error was one wrong letter in a word the helper has to know.

## Fix the bug

```challenge
type: fix
id: fix-missing-quote
verified: "UNVERIFIED: compile not run"
code: |
  fun main() {
      // A short welcome card, printed one line at a time
      println("Welcome to Kotree")
      println("Today you run your first program")
      println("Step 1: write the code)
      println("Step 2: press Run")
      println("Step 3: read the answer below")
      println("You ran it yourself")
      println("See you in the next lesson")
  }
hints:
  - "Look at the lines that start with Step. One of them looks different at the end."
  - "Text in Kotlin sits between two double quotes. Every opening quote needs a closing quote."
expected: |
  Welcome to Kotree
  Today you run your first program
  Step 1: write the code
  Step 2: press Run
  Step 3: read the answer below
  You ran it yourself
  See you in the next lesson
fix: |
  fun main() {
      // A short welcome card, printed one line at a time
      println("Welcome to Kotree")
      println("Today you run your first program")
      println("Step 1: write the code")
      println("Step 2: press Run")
      println("Step 3: read the answer below")
      println("You ran it yourself")
      println("See you in the next lesson")
  }
explanation: "Kotlin needs a closing double quote to know where the text ends. Without it, the program cannot run."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 and question 4 have short answers: no null values are in play, and nothing runs twice.

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

The Playground runs plain Kotlin, not Android apps. A real Android app is built with Android Studio, which you meet in the last lesson of this part of the course.

The lines you run here are the same kind of Kotlin that apps are written in. The Android documentation says Kotlin is fully supported for Android development. So a button that does nothing on a phone is, deep down, a few missing or wrong lines like these.

An app has no output area under a button. To see what an app is doing, developers read Logcat, a window in Android Studio. The Android documentation says Logcat shows messages that the developer added to the app, plus messages from the system. It plays the same part as the output area: the place where you read the reply.

## Recap

1. The Kotlin Playground lets you type a program, press Run, and read the output in your browser.
2. The reply is what the program prints. Text goes between double quotes.
3. An error is a reply too. Fix the line it points to and run again.

## You can now

- Explain where to run a first Kotlin program without installing anything.
- Predict what a one-line `println` program prints.
- Fix a program that stops because a piece of text is missing its closing quote.
