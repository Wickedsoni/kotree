---
id: kotlin/errors-and-results/exceptions
title: What happens to my program when code throws an exception?
status: shadow
difficulty: medium
order: 60
requires: [kotlin/functions/declaring-functions, kotlin/classes-and-objects/what-is-a-class]
summary: Some lines stop the whole program when something goes wrong. See which lines get skipped, and how to refuse bad input early.
video: ""
publishedOn:
updatedOn:
sims: [fire-alarm-rooms]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/docs/exceptions.html : exceptions are subclasses of Exception, which is a subclass of Throwable; Throwable has two direct subclasses, Error and Exception; the throw keyword; Kotlin treats all exceptions as unchecked, so you do not need to declare or handle them; require() throws IllegalArgumentException for input checks, check() throws IllegalStateException for state checks, error() throws IllegalStateException.
- https://developer.android.com/topic/performance/issues/crash : "An app that is written using Java or Kotlin crashes if it throws an unhandled exception, represented by the Throwable class"; Android ends the app's process and shows a dialog that the app has stopped.
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/errors-and-results/exceptions.
-->

## The question

A program asks a helper to check a customer's age. The helper finds -5 and gives up halfway through its work. What does the program do next? Does it carry on with the next line, as if nothing happened? And how far does the stopping spread: only the line that failed, only that helper, or the whole program?

## Mental model

Think of a meeting held in three nested rooms, with a fire alarm.

- The meeting is the program. It has an agenda of numbered lines.
- The rooms are functions. The hall calls the office, and the office calls the desk.
- A line of the agenda runs inside whichever room it belongs to.
- The fire alarm is the moment something goes wrong.
- When the alarm rings, the work stops on the spot. The rest of that room's lines are skipped.
- Everyone then leaves, one room after another. The desk empties, then the office, then the hall. No room finishes its remaining lines.
- A note on the board says why the meeting stopped.

**Where the analogy stops:** A real building can also have a warden at a door who sends people back in. This page does not cover how to stop the alarm. And a real alarm is set off by chance, while a program rings its alarm on purpose, with a line you can read.

## See it move

<!-- SIM: fire-alarm-rooms -->

**Try this:**
1. Set the alarm to ring at line 4, inside the desk, and press Step until the end.
2. Move the alarm to line 2, in the office, and play again.
3. Set the alarm to "never" and play again.
4. (Optional reveal) Tick "Check the guest list at the door" and play with the alarm still set.

**What you should notice:** An alarm in the desk also crosses out lines that belong to the office and the hall. A check at the door stops the alarm before any line runs.

## Build the logic

### Notice

A program has three rooms. The hall starts the meeting and calls the office. The office starts its work and calls the desk. The desk checks an age. The age is -5, which cannot be right. After the desk, the office has one more line, and after the office, the hall has one more line.

### Guess

The desk finds the bad age. What should happen to the rest of the work? Here are three ideas.

- A. Only the desk's current line is skipped, and everything else carries on.
- B. The rest of the desk is skipped, and the office and the hall carry on.
- C. The alarm rings, and every room stops where it is, from the desk outwards.

Idea A hides a broken state: the office would carry on with an age that was never checked. Idea B stops one room but lets the others use a bad result. Idea C is the answer. The program does not carry on from a place where something is wrong.

### Build

1. A line of code can decide that something is wrong and ring the alarm. It rings it with a note that says what went wrong.
2. The line that rings the alarm is the last line that runs in its room.
3. The room's remaining lines are skipped.
4. The room that called it is stopped too, at the line where it made the call, and its remaining lines are skipped.
5. The alarm moves outwards, room by room, until it passes the hall.
6. With nobody to stop it, the program ends, and the note is shown.
7. To avoid ringing the alarm in the middle of the work, check the input at the door of the room, before any other line runs.

### Name it

An **exception** is the alarm: a value that describes what went wrong, which stops the normal flow of the program. You ring it with **`throw`**, as in `throw IllegalArgumentException("age cannot be below 0")`. The Kotlin documentation says exceptions are subclasses of `Exception`, which is a subclass of `Throwable`, and that `Throwable` also has `Error` as a direct subclass.

```kotlin
fun desk(age: Int) {
    println("desk: start")
    if (age < 0) {
        throw IllegalArgumentException("age cannot be below 0")
    }
    println("desk: end")
}

fun office(age: Int) {
    println("office: start")
    desk(age)
    println("office: end")
}

fun main() {
    println("hall: start")
    office(-5)
    println("hall: end")
}
```

It prints `hall: start`, `office: start` and `desk: start`. Then the program stops with an `IllegalArgumentException`. The lines `desk: end`, `office: end` and `hall: end` never run. The note, with the message, is shown as an error and not as normal output.

The documentation has two helpers for checking at the door. **`require`** checks that the input is valid and throws `IllegalArgumentException` if it is not. **`check`** checks that the state of an object is valid and throws `IllegalStateException`.

```kotlin
fun setAge(age: Int): Int {
    require(age >= 0) { "age cannot be below 0" }
    return age
}

fun main() {
    println(setAge(30))
    println(setAge(-5))
    println("done")
}
```

It prints `30`. Then the second call rings the alarm, and `done` is never printed.

The curly brackets after `require(age >= 0)` hold a small block of code that makes the message. That kind of block is called a lambda (general knowledge). Kotlin runs it only if the check fails (general knowledge).

## Predict the output

```challenge
type: predict
run: false
id: skipped-lines-spread
verified: kotlin-2.4.21
code: |
  fun inspect(n: Int) {
      println("looking at " + n)
      if (n < 0) throw IllegalArgumentException("negative")
      println("fine")
  }

  fun main() {
      inspect(3)
      inspect(-1)
      inspect(5)
  }
options:
  - id: a
    text: "looking at 3, fine, looking at -1, looking at 5, fine"
    why: "You may have thought only the rest of the failing call is skipped and main carries on. The alarm leaves every room, so main stops too."
  - id: b
    text: "looking at 3, fine, looking at -1"
    correct: true
    why: "The first call finishes normally. The second call rings the alarm before fine, and the alarm empties main as well, so the third call never starts."
  - id: c
    text: "looking at 3, fine, looking at -1, fine, looking at 5, fine"
    why: "You may have thought only the throw line is skipped. The line after it is skipped too, and so is everything after the call."
output: |
  looking at 3
  fine
  looking at -1
```

```challenge
type: predict
run: false
id: alarm-leaves-the-caller
verified: kotlin-2.4.21
code: |
  fun inner() {
      println("inner")
      throw IllegalStateException("boom")
  }

  fun main() {
      println("main start")
      inner()
      println("main end")
  }
options:
  - id: a
    text: "main start, inner, main end"
    why: "You may have thought the alarm stays in the room where it rang. It moves outwards to the room that called it, so main end is skipped."
  - id: b
    text: "main start and then the program stops"
    why: "You may have thought the alarm rings as soon as inner is called. The alarm rings on the throw line, after inner prints."
  - id: c
    text: "main start, inner"
    correct: true
    why: "The alarm rings in inner, on the throw line. Then main empties too, so no later line in any room runs."
output: |
  main start
  inner
```

```challenge
type: predict
run: false
id: check-at-the-door
verified: kotlin-2.4.21
code: |
  fun setAge(age: Int): Int {
      require(age >= 0) { "age cannot be below 0" }
      println("age set")
      return age
  }

  fun main() {
      println(setAge(30))
      println(setAge(-5))
      println("done")
  }
options:
  - id: a
    text: "age set, 30"
    correct: true
    why: "The first call passes the check at the door and prints both lines. The second call fails the check before age set can print, and the alarm ends the program."
  - id: b
    text: "age set, 30, age set, -5, done"
    why: "You may have thought the check only prints a warning. A failed check rings the alarm and the program stops."
  - id: c
    text: "age set, 30, done"
    why: "You may have thought only the bad call is lost. The alarm leaves main too, so done is skipped."
output: |
  age set
  30
```

## Tricks to remember

- An alarm empties every room from the one where it rang, outwards.
- The line that throws is the last line that runs in its room.
- Check at the door: `require` for bad input, `check` for a bad state.
- No one to stop the alarm means the program ends.

## Common mistakes

- **Thinking an exception only stops the line that failed.** The next line sits in the same room, so it can feel as if only the failed line is lost. The alarm leaves every room until something stops it. Look for the first throw to find where the output stops.
- **Letting bad input travel on.** A small helper can do some work with -5 without a failure, so it feels fine to wait and see. The bad value reaches lines far from the cause. Add `require` at the door of the function.
- **Using `require` for a bad state, or `check` for bad input.** Both throw, so they look the same. The documentation says `require` is for input and `check` is for the state of an object. The names then tell the reader what went wrong.
- **Expecting Kotlin to force you to declare exceptions.** Some languages make you list them. The documentation says Kotlin treats all exceptions as unchecked, so you do not have to declare or handle them. This means a missing check will not be flagged for you.

## Where this shows up in Android

Picture an app that closes in the middle of a task, with a dialog saying that the app has stopped. The Android crash page says that a Kotlin or Java app crashes if it throws an unhandled exception, represented by the `Throwable` class. It also says Android ends the app's process and shows a dialog. That is the fire alarm reaching the hall with nobody to stop it. This lesson explains why the lines after the throw never ran, even though they sat in other functions.

## Say it in an interview

**What is an exception, and what does throwing one do to the program?**
It is an object that describes what went wrong, thrown with the `throw` keyword. The normal flow stops at that line, and the exception moves out through each calling function. If nothing handles it, the program ends. The Kotlin documentation says all exceptions are subclasses of `Throwable`.

**Are checked exceptions enforced in Kotlin as in Java?**
No. The documentation says Kotlin treats all exceptions as unchecked, so you can catch them but you do not have to declare or handle them. My own reasoning: a missing handler will not be pointed out by the compiler, so you must think about failures yourself.

**Why did Kotlin leave out checked exceptions?**
The Kotlin page I read does not give the reason. General knowledge: the usual argument is that forced handling leads to boilerplate and to empty handlers that hide errors. My own reasoning: that is a trade-off, because the compiler no longer reminds you what a function can throw.

**What are the differences between `require`, `check` and `error`?**
The documentation lists them. `require` checks input and throws `IllegalArgumentException`. `check` checks the state of an object and throws `IllegalStateException`. `error` is for an illegal state or condition and also throws `IllegalStateException`. Use `require` at the top of a function to refuse bad input early.

## Recap

1. Throwing an exception stops the normal flow at that line.
2. The exception leaves every calling function in turn, and the program ends if nothing stops it.
3. `require` and `check` refuse bad input or a bad state at the start.

## You can now

- Predict which lines of a program print before an exception stops it.
- Predict that an exception thrown in an inner function also skips the lines in the functions that called it.
- Predict what a failed `require` does to the rest of a program.
