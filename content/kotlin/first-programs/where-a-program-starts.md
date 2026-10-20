---
id: kotlin/first-programs/where-a-program-starts
title: Where does a Kotlin program start running?
status: shadow
difficulty: easy
order: 1
requires: []
summary: A Kotlin program starts in one function named main. Other functions wait until a line calls them.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/basic-syntax.html (entry point is the main function; println adds a line break); kotlinlang.org/docs/kotlin-tour-hello-world.html (main is where the program starts; a function body is written in curly braces); developer.android.com/guide/components/fundamentals (each component is an entry point; activity is the entry point for interacting with the user and one screen; Android apps do not have a single entry point and there is no main()). Textbook, no single official page: other functions run only when called, and a call returns to the next line. Second form of main: kotlinlang.org/docs/basic-syntax.html (fun main(args: Array<String>), a variable number of String arguments). Interview section: same pages; the "no class needed" answer rests on the top-level main in the documentation's examples. Own reasoning: last paragraph of the Android section; the trade-off sentence in the last interview answer. General knowledge: what "arguments" means. Hand-traced, not compiled. -->
## The question

You open a file with three blocks of steps, and each block has its own name. You press Run. Which block does the computer start with: the first one in the file, the biggest one, or another? And what happens to the other two?

## Mental model

Think of a house.

- The house is your whole program.
- The rooms are blocks of steps, each with a name: "kitchen", "hall", "garden".
- The front door is the one place every visit begins. It has a sign with the same word on it, every time.
- The visitor is the computer.

A visitor always comes in through the front door, whichever room they will end up in. Inside a room, the visitor does the steps from top to bottom. A step can say "go into the kitchen". Then the visitor does the kitchen steps and walks back to where they left off.

A room nobody sends the visitor into stays empty. It is still part of the house.

**Where the analogy stops:** A real visitor chooses where to wander. A computer never chooses. It goes only where the steps send it. Also, in a real house the room next to the door feels like the first room. In a file, the room listed first is not special. Only the sign on the front door matters.

## Build the logic

### Notice

A file lists three rooms in this order: kitchen, hall and garden.

- The kitchen has one step: print "Tea".
- The hall has three steps: print "Hello", go into the kitchen, print "Bye".
- The garden has one step: print "Flowers".

One room has the front door sign on it. The visitor arrives at the house.

### Guess

Which room does the visitor enter first?

- A. Every room at the same time.
- B. The room that is listed first in the file.
- C. The room behind the front door, which has a special name.

Idea B is the most common guess. It feels right because we read files from the top. Idea A is how some people picture a computer, but one visitor cannot be in three rooms. Idea C is how Kotlin works.

### Build

1. The computer needs one known place to start. Without it, it would not know where to begin.
2. So everyone agrees on one fixed name for the front door.
3. The visitor enters through that door and does its steps from top to bottom.
4. A room with any other name does nothing until a step sends the visitor there. That step can sit in the front-door room or in another room the visitor is already in.
5. After the visitor finishes a room, they walk back to the step after the one that sent them in.
6. When the last step of the front-door room is done, the visit is over.

### Name it

The front door is called the **entry point**. The Kotlin documentation says the entry point of a Kotlin application is the `main` function. It shows two forms: `fun main()` and `fun main(args: Array<String>)`. The second accepts a variable number of `String` arguments, which means pieces of text handed to the program when it starts (that plain meaning is general knowledge). Both start the program. This lesson uses the short form.

A **function** is a named block of steps. You write one with the keyword `fun`, then its name, then `()`, then the steps inside curly braces `{ }`. The function named **`main`** is the front door.

A function with any other name runs only when some line **calls** it. To call a function, you write its name followed by `()`.

**`println`** is the Kotlin command that prints its text and adds a line break, so the next thing you print starts on a new line. The documentation says the same.

```kotlin
fun greet() {
    println("Hello from greet")
}

fun main() {
    println("Start")
    greet()
    println("End")
}
```

This program has two functions. It starts in `main`, which is the second one in the file. It prints `Start`. Then the line `greet()` calls `greet`, which prints `Hello from greet`. Then `main` goes on and prints `End`. The output is three lines: `Start`, `Hello from greet`, `End`.

## Predict the output

```challenge
type: predict
run: false
id: helper-above-main
verified: not-run
code: |
  fun first() {
      println("A")
  }

  fun main() {
      println("B")
      first()
      println("C")
  }
options:
  - id: a
    text: "A, then B, then C"
    why: "You may have read the file from the top. The visitor does not enter the room listed first. It enters through the front door, and that door is main."
  - id: b
    text: "B, then A, then C"
    correct: true
    why: "The visitor enters through the front door and prints B. The line first() sends the visitor into the room named first, which prints A. Then the visitor walks back and prints C."
  - id: c
    text: "B, then C"
    why: "You may have thought a room that is not the front door never opens. It does open, when a step in main sends the visitor in."
output: |
  B
  A
  C
```

```challenge
type: predict
run: false
id: room-nobody-visits
verified: not-run
code: |
  fun secret() {
      println("Secret room")
  }

  fun main() {
      println("Front door")
  }
options:
  - id: a
    text: "Front door"
    correct: true
    why: "The visitor enters through the front door and prints its text. No line calls secret, so that room stays empty."
  - id: b
    text: "Secret room, then Front door"
    why: "You may have expected the room listed first to open first. The visitor enters at the front door, and no step sends it into secret."
  - id: c
    text: "Front door, then Secret room"
    why: "You may have thought every room gets visited in the end. A room is visited only when a step sends the visitor there."
output: "Front door"
```

```challenge
type: predict
run: false
id: which-sign-is-the-door
verified: not-run
code: |
  fun start() {
      println("A")
  }

  fun mian() {
      println("B")
  }

  fun main() {
      println("C")
  }
options:
  - id: a
    text: "A, because start sounds like the place to begin"
    why: "You may have trusted the word. The front door is the name main, and start is an ordinary room here."
  - id: b
    text: "B, because mian is almost main"
    why: "You may have read the sign loosely. One wrong letter makes mian a different name, so it is an ordinary room."
  - id: c
    text: "C, because the sign on the door reads main"
    correct: true
    why: "The visitor enters through the door whose sign says main exactly. No line calls start or mian, so those rooms stay empty."
output: "C"
```

## Tricks to remember

- One front door, and its sign says `main`.
- Written is not visited. A function only runs when something calls it.
- A function name that is one letter off is a different function.
- Inside a room, steps go from top to bottom.

## Common mistakes

- **Thinking Kotlin runs the file from the first line down.** It feels right because we read a page from the top. Look for `main` first. That is where the visit starts.
- **Putting a function above `main` and expecting it to run first.** The file order feels like a to-do list. Only the call decides when a function runs. A function above `main` waits like any other room.
- **Writing a function and forgetting to call it.** The function looks finished, so it feels done. Check that some line in `main`, or in a function that `main` calls, says its name followed by `()`.
- **Misspelling `main`.** One wrong letter looks harmless. Kotlin reads it as a different name, so the program has no front door. Read the name letter by letter.

## Where this shows up in Android

Open an Android app's code and you will not find one front door. The Android documentation says: "Android apps don't have a single entry point: there's no `main()` function". Instead, an app is made of **components**, which are parts of the app that the system can start. The documentation says each component is an entry point through which the system or a user can enter your app.

An **activity** is one such component. The documentation calls it the entry point for interacting with the user, and says it represents a single screen. The Android system, which is the part of the phone that runs apps, decides which component to start.

My own reasoning: once the system has started a component, the rules from this lesson still hold inside your code. A function runs when some line calls it, and steps run from top to bottom. So when a function in an app never seems to run, the first check is the same. Does some line, in code the system started, call it?

## Say it in an interview

**What is the entry point of a Kotlin program, and what forms can it take?**
The entry point is the `main` function. The Kotlin documentation shows two forms: `fun main()` and `fun main(args: Array<String>)`. The second accepts a variable number of `String` arguments, which are pieces of text handed to the program when it starts. Both start the program.

**Does `main` have to sit inside a class?**
No. In Kotlin, `main` can be a top-level function, which means a function written straight in the file. It is not wrapped in a **class**, which is a named container that can hold code. The Kotlin documentation's examples do exactly that. General knowledge: some other languages need that wrapper.

**Does a function written above `main` run first?**
No. Only a call decides when a function runs. The order in the file is not the order of running. A function that nobody calls never runs.

**Why does an Android app have no `main`?**
The Android documentation says Android apps don't have a single entry point: there's no `main()` function. Each component is an entry point, and the system decides which one to start. My own reasoning: the trade-off is that your code can be entered at any component, such as a screen, so you cannot assume one fixed start.

## Recap

1. A Kotlin program starts in the function named `main`.
2. Other functions run only when a line calls them by name.
3. The order of functions in the file does not decide what runs first.

## You can now

- Predict the order of lines printed by a program where a helper function sits above `main`.
- Explain why a function that nobody calls prints nothing.
- Spot the function that is the entry point in a file with a near-miss name such as `mian`.
