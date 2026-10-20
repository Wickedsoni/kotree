---
id: kotlin/first-programs/where-a-program-starts
title: Where does a Kotlin program start running?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Every Kotlin program begins at one function named main. Other functions wait until main calls them.
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: compile and run not done for any kotlin block in this leaf (Kotlin 2.4.21, docs/06 section 5). Also unknown: what the Playground shows when a program has no main function (Fix the bug). -->
## The question

You open a file that holds three small blocks of steps. Each block has its own name. You press Run.

Which block does the computer start with? The first one in the file? The biggest one? And what happens to the other two?

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

- A. The room that is listed first in the file.
- B. The room behind the front door, which has a special name.
- C. Every room at the same time.

Idea A is the most common guess. It feels right because we read files from the top. Idea C is how some people picture a computer, but one visitor cannot be in three rooms. Idea B is how Kotlin works.

### Build

1. The computer needs one known place to start. Without it, it would not know where to begin.
2. So everyone agrees on one fixed name for the front door.
3. The visitor enters through that door and does its steps from top to bottom.
4. A room with any other name does nothing until a step inside the door room (or inside another room that was visited) sends the visitor there.
5. After the visitor finishes a room, they walk back to the step after the one that sent them in.
6. When the last step of the front-door room is done, the visit is over.

### Name it

The front door is called the **entry point**. In Kotlin, the entry point is a **function** named **`main`**. A function is a named block of steps. You write one with the keyword `fun`, then its name, then `()`, then the steps inside curly braces `{ }`.

A function that is not `main` runs only when some line **calls** it. To call a function, you write its name followed by `()`.

**`println`** is the Kotlin command that prints its text and then moves to the next line.

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

This program has two functions. It starts in `main`, which is the second one in the file. It prints `Start`. Then the line `greet()` calls `greet`, which prints `Hello from greet`. Then `main` goes on and prints `End`.

You may also see `fun main(args: Array<String>)`. The Kotlin documentation shows it as another form of `main` that accepts text arguments. This lesson uses the short form.

## Predict the output

```challenge
type: predict
id: helper-above-main
verified: "UNVERIFIED: compile and run not done"
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
id: room-nobody-visits
verified: "UNVERIFIED: compile and run not done"
code: |
  fun secret() {
      println("Secret room")
  }

  fun main() {
      println("Front door")
  }
options:
  - id: a
    text: "Secret room, then Front door"
    why: "You may have expected the room listed first to open first. The visitor enters at the front door, and no step sends it into secret."
  - id: b
    text: "Front door"
    correct: true
    why: "The visitor enters through the front door and prints its text. No line calls secret, so that room stays empty."
  - id: c
    text: "Front door, then Secret room"
    why: "You may have thought every room gets visited in the end. A room is visited only when a step sends the visitor there."
output: "Front door"
```

## Try it

1. **Write.** Type the program from Name it into the editor: the `greet` function, then `main`.
2. **Run.** You should see three lines: `Start`, `Hello from greet`, `End`.
3. **Break.** Delete the line `greet()` and nothing else. Run it again.
4. **Fix.** You should now see only `Start` and `End`, because no step sends the visitor into the `greet` room. Type `greet()` back in the same place and run it. The three lines return.

## Fix the bug

```challenge
type: fix
id: fix-misspelled-main
verified: "UNVERIFIED: compile and run not done"
code: |
  fun welcome() {
      println("Welcome to the app")
  }

  fun countdown() {
      println("3")
      println("2")
      println("1")
  }

  fun mian() {
      welcome()
      countdown()
      println("Go")
  }
hints:
  - "Find the function that calls the other two. Read its name letter by letter."
  - "Kotlin starts a program in a function named exactly main."
expected: |
  Welcome to the app
  3
  2
  1
  Go
fix: |
  fun welcome() {
      println("Welcome to the app")
  }

  fun countdown() {
      println("3")
      println("2")
      println("1")
  }

  fun main() {
      welcome()
      countdown()
      println("Go")
  }
explanation: "The front door must have the sign main. A function named mian is an ordinary room, so no visitor ever enters through it."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a short answer: this code has no null values in play. Question 4 is also short: nothing runs twice. Spend your time on question 2.

## Tricks to remember

- One front door, and its sign says `main`.
- Written is not visited. A function only runs when something calls it.
- A function name that is one letter off is a different function.
- Inside a room, steps go from top to bottom.

## Common mistakes

- **Thinking Kotlin runs the file from the first line down.** It feels right because we read a page from the top and because the first lessons showed a short program. Look for `main` first. That is where the visit starts.
- **Putting a function above `main` and expecting it to run first.** The file order feels like a to-do list. Only the call decides when a function runs. A function above `main` waits like any other room.
- **Writing a function and forgetting to call it.** The function looks finished, so it feels done. Check that some line in `main` (or in a function that `main` calls) says its name followed by `()`.
- **Misspelling `main`.** One wrong letter looks harmless. Kotlin reads it as a different name, so the program has no front door. Read the name letter by letter when nothing runs.

## Where this shows up in Android

Open a new Android project and search for `main()`. You will not find one in the code you write. The Android documentation says that Android apps do not have a single entry point and that there is no `main()` function.

Instead, the system starts a component of your app. An activity, which is one screen of your app, is one such entry point. So the Android system picks where the visit starts, but inside your code the same rules from this lesson hold. A function runs when something calls it, and steps go from top to bottom inside it.

If you follow a symptom like "my function never runs", this lesson gives the first check: does some line, in code the system started, actually call it?

## Recap

1. A Kotlin program starts in the function named `main`.
2. Other functions run only when a line calls them by name.
3. The order of functions in the file does not decide what runs first.

## You can now

- Predict the order of lines printed by a program where a helper function sits above `main`.
- Explain why a function that nobody calls prints nothing.
- Fix a program where `main` is misspelled so that it runs.
