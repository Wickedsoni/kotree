---
id: kotlin/null-safety/safe-call-and-elvis
title: How do I use a maybe-missing value without crashing?
status: live
difficulty: medium
order: 19
requires: [kotlin/null-safety/nullable-types]
summary: A safe call asks a question only when the value is there. An Elvis fallback gives an answer when it is not.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [knock-and-note]
playground: false
fruit: false
upnext: false
---

## The question

A tip calculator has a box for the bill. The user leaves it empty and presses the button. The app must still show a number, and it must not close. How can code ask a question about a value that might be missing and still get an answer?

## Mental model

Think of knocking on a door.

- A street of houses is your program. Each door is a name that may hold nothing.
- The person behind the door is the value. A door with nobody home is the mark for nothing here.
- A polite knock with a question, such as "how many letters are in your name?", is asking about the value.
- The reply is the answer you carry away.
- "No reply" is what you carry away from an empty house.
- A note that says "if nobody answers, write this instead" is the fallback.
- Breaking in to ask without knocking is using the value without looking. The street rules refuse it before you leave home.

You knock, and if someone is there you get an answer. If nobody is there, you do not break in. You carry "no reply" home, or you leave your note.

**Where the analogy stops:** A real visitor sees at once that nobody is home and decides what to do. In code, "no reply" travels on as a value, and the next line has to deal with it too. Also, when a visitor asks a chain of questions, one empty door stops the whole chain.

## See it move

<!-- SIM: knock-and-note -->

**Try this:**
1. Send the visitor to the house with someone home and press "Ask for the name's length".
2. Send the visitor to the empty house and ask again.
3. Tick "Leave a note: 0" and ask at the empty house again.
4. (Optional reveal) Tick "Walk straight in" and press "Ask for the name's length" at the empty house.

**What you should notice:** A knock at an empty house brings back "no reply" and the visitor goes on, the note turns "no reply" into 0, and walking straight in is refused before the visitor leaves.

## Build the logic

### Notice

Three houses stand on the street. Ada lives in the first, nobody lives in the second, and Bob lives in the third. The visitor wants the number of letters in each resident's name. Ada's name has 3 letters. Bob's name has 3 letters. The second house is empty.

### Guess

The visitor knocks politely at the empty house and asks for the number of letters. What does the visitor bring back? Here are three ideas.

- A. 0 letters.
- B. "No reply", which is itself a kind of answer that may be missing.
- C. A crash.

Idea A is tempting because 0 is a number and the visitor wants a number. But 0 would claim that someone gave a name with no letters. Idea C is what breaking in would do. A polite knock does not crash. Idea B is the answer.

### Build

1. Before asking, knock. Ask the question only if someone answers.
2. If nobody answers, do not ask. The reply for the whole question is "no reply".
3. So the result of a polite question is "a number, or no reply". The result may be missing, which means its type has a question mark.
4. If you ask several questions in a row, check the door before each one. The first empty door ends the chain with "no reply".
5. If you need a real number every time, add a note: "if no reply, use this number instead".
6. The note is read only when the reply is "no reply". If someone answered, the note is ignored.
7. The note can also say "go home now", or it can sound an alarm. Both stop the visit.

### Name it

The polite knock is the **safe call**, written `?.`. The Kotlin documentation says that, instead of throwing a NullPointerException, if the object is `null`, the `?.` returns `null`. The documentation adds that a chain of safe calls returns `null` if any part of it is `null`.

The note is the **Elvis operator**, written `?:`. The documentation says that if the **expression** on the left of `?:` (a piece of code that works out a value) is not `null`, it is returned. Otherwise the expression on the right is returned. The right side can also be `return`, which leaves the function, or `throw`, which stops the program with an error you choose. The documentation says you can use them there because `throw` and `return` are expressions.

```kotlin
val name: String? = null
val other: String? = "Ada"
val padded: String? = "  Oslo "

fun main() {
    println(name?.length)
    println(other?.length)
    println(name?.length ?: 0)
    println(padded?.trim()?.length ?: 0)
}
```

This prints `null`, `3`, `0` and `4`. The first knock finds nobody, so the reply is no reply and `println` prints `null`. The second finds Ada and counts 3 letters. The third leaves a note with 0, and it is used because the reply is no reply. In the fourth, `trim()` takes the spaces off both ends of the text (general knowledge), so `Oslo` has 4 letters. A missing door anywhere in that chain would have given the note.

This program leaves the function when the draft is missing.

```kotlin
val draft: String? = null

fun main() {
    println("Start")
    val text = draft ?: return
    println("Saved: $text")
}
```

It prints `Start` and nothing else. Nobody was home, so the note said "go home now". There is no error and no message.

## Predict the output

```challenge
type: predict
run: false
id: chain-and-note
verified: kotlin-2.4.21
code: |
  val city: String? = "  Oslo "
  val missing: String? = null

  fun main() {
      println(city?.trim()?.length ?: 0)
      println(missing?.trim()?.length ?: 0)
  }
options:
  - id: a
    text: "7, then 0"
    why: "You may have counted the spaces. The visitor asks trim to take them off first, so the letters left are the 4 in Oslo."
  - id: b
    text: "4, then null"
    why: "You may have forgotten the note. The note after the chain turns no reply into 0."
  - id: c
    text: "4, then 0"
    correct: true
    why: "Someone is home at the first house, the spaces come off and 4 letters are counted. The second house is empty, so the chain ends with no reply, and the note gives 0."
output: |
  4
  0
```

```challenge
type: predict
run: false
id: answer-is-no-reply
verified: kotlin-2.4.21
code: |
  val title: String? = null

  fun main() {
      val size = title?.length
      println("Size: $size")
      println(title?.length ?: -1)
  }
options:
  - id: a
    text: "Size: 0, then -1"
    why: "You may have expected the visitor to report zero letters. With no note, the report from an empty house is no reply."
  - id: b
    text: "Size: null, then -1"
    correct: true
    why: "A polite knock at an empty house brings back no reply, which a template shows as null. The second line has a note, so it prints -1."
  - id: c
    text: "The program stops at the first line"
    why: "You may have thought any question to an empty house crashes. A knock never crashes. Breaking in would."
output: |
  Size: null
  -1
```

```challenge
type: predict
run: false
id: note-says-go-home
verified: kotlin-2.4.21
code: |
  val draft: String? = null

  fun main() {
      println("Start")
      val text = draft ?: return
      println("Saved: $text")
      println("End")
  }
options:
  - id: a
    text: "Start"
    correct: true
    why: "Nobody was home, so the note said go home now. The lines after it never run, and no error or message tells you so."
  - id: b
    text: "Start, Saved: null, End"
    why: "You may have expected the note to be ignored. The note is read exactly when the reply is no reply, and here it is."
  - id: c
    text: "Start, then the program stops with a NullPointerException"
    why: "You may have expected a crash. A note that says go home leaves quietly, which is why it can hide a missing value."
output: |
  Start
```

## Tricks to remember

- Knock before you ask: `?.` asks only if someone is home.
- No reply is a result, not a crash. It travels on.
- `?:` is the note. It is read only when the left side is `null`.
- A note that says "go home" leaves without a sound.

## Common mistakes

- **Thinking `?.` hides the problem.** A knock at an empty house brings back no reply, so the null seems to vanish. It travels on as the result. Deal with it with a note, or let the next line accept a missing result.
- **Choosing a note without thinking about the number.** A note of 0 keeps the app running, so it feels safe. If 0 is a wrong answer for the app, the screen now shows a wrong number and nobody notices. Pick a fallback that is true for your app.
- **Using `?: return` and forgetting that it is silent.** The app does not close, so it feels fine. A save that quietly gives up looks like a save that worked. My own reasoning: show a message or log it when the skip matters.
- **Removing the note and being surprised when Kotlin refuses.** Without `?:`, the result is a `String?` or an `Int?`, and Kotlin will not let you use it as a plain value. The refusal is the signal to leave a note.

## Where this shows up in Android

The Android tip-calculator codelab turns the typed text into a number with the line `val tipPercent = tipInput.toDoubleOrNull() ?: 0.0`. The function `toDoubleOrNull()` gives back a number, or `null`. The Kotlin library page for the matching `toIntOrNull()` says it returns `null` if the text is not a valid number. The codelab says this value could be `null` if the text field is empty. The note `?: 0.0` turns that case into 0.0.

That is the case this lesson handles: the text field is empty, so there is no number to use. With the note, the calculation goes on with 0.0 instead of stopping. This next part is our own reasoning. With `!!` in place of the note, an empty box would close the app. A user who leaves it empty would see the crash.

## Say it in an interview

**What does `user?.name ?: "Guest"` give when `user` is null?**
It gives `"Guest"`. The safe call `user?.name` returns `null` when `user` is `null`, instead of crashing. Then the Elvis operator sees the `null` on its left and returns the text on its right. If `user` is not null, you get the name.

**What is the difference between `?.` and `!!`?**
Both can meet a `null`. The Kotlin documentation says `?.` returns `null` there, and `!!` throws a NullPointerException. The trade-off: `?.` lets the app go on with a missing result that you must handle, and `!!` stops loudly at the line where the promise broke.

**Does a safe call hide the bug?**
It can. The null does not vanish: it becomes the result and travels until something handles it. A fallback such as `?: 0` hides a real problem if 0 is wrong for the app, and `?: return` quietly skips work. My own reasoning: choose an honest fallback, and make a silent skip visible when it matters.

**What can be on the right side of `?:`?**
Any expression. The documentation says `return` and `throw` work there, because they are expressions. The documentation's example ends a line with `?: throw IllegalArgumentException("name expected")`, which demands a value and stops with a clear message if it is missing. The right side is used only when the left side is `null`.

## Recap

1. A safe call, `?.`, asks only when the value is there and returns `null` otherwise.
2. The Elvis operator, `?:`, gives a fallback answer when its left side is `null`.
3. A fallback of `return` leaves the function quietly, so it can hide a missing value.

## You can now

- Predict the result of a chain of safe calls that ends with an Elvis fallback.
- Predict what a safe call gives back when the value is `null`.
- Spot when `?: return` quietly skips the rest of a function.
