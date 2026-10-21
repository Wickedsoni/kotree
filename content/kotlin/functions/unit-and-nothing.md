---
id: kotlin/functions/unit-and-nothing
title: What does a function return when it has nothing to give back?
status: shadow
difficulty: easy
order: 15
requires: [kotlin/functions/declaring-functions]
summary: A function that only prints still gives something back, a value called Unit. Nothing is a different type for code that never finishes.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/functions.html (block body without return type is Unit; "Unlike void in other programming languages, Unit is an actual type, and you can use its value"; Unit need not be written; must be written in a function type such as () -> Unit); kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/ ("The type with only one value: the Unit object. This type corresponds to the void type in Java"); kotlinlang.org/docs/exceptions.html (Nothing: built-in type that is a subtype of all other types, for code that never completes successfully; compiler marks code after it unreachable; TODO() always throws NotImplementedError); developer.android.com/develop/ui/compose/state (onNameChange: (String) -> Unit; onValueChange: (T) -> Unit "an event that requests the value to change"; TextField updates only when its value parameter changes). The printed text kotlin.Unit is general knowledge (the API page does not show it) and is labelled so in the body. Snippets are hand-traced, not run. -->
## The question

A function prints a greeting and does nothing else. It has no answer to hand back. Yet you can call it, and Kotlin does not complain that something is missing. So what did the call give back? Is it zero, a blank, or something else?

## Mental model

Think of a postbox on a street corner.

- The postbox is the function.
- The letter you post is what you hand in to the function.
- The clunk of the letter dropping inside is the job the function does, such as printing a line.
- A little flag pops up on the side and says "done". That flag is what the call gives back.
- The flag carries no message. It is the same flag every time.
- A slip from the post-office clerk, with a number on it, is a real answer to a real question.

You post a letter and walk away with empty hands. But the box did answer: you can look at the flag.

**Where the analogy stops:** A real flag can tell you something, such as "mail is waiting". The Kotlin flag is always the same, so it tells you nothing except that the job finished. Also, a real postbox always finishes its clunk. Some Kotlin code never finishes, and then no flag ever pops up.

## Build the logic

### Notice

Ana posts a letter. She hears the clunk and sees the flag pop up. Her hands are empty. A moment later she walks into the post office and asks the clerk to weigh a parcel. The clerk reads the number off the scale and hands her a slip with it. The postbox gave her no slip. It gave her only the flag.

### Guess

Ana is asked, "What did the postbox give back?" Here are three ideas.

- A. Nothing at all. The question makes no sense.
- B. A blank slip, a zero or the mark for "nothing here".
- C. The same flag every time, which says "done" and carries no message.

Idea A is how some older languages work, so it feels natural. Idea B feels right because the postbox gave her no slip. But in Kotlin every call gives something back, and the something has a kind. Idea C is the answer.

### Build

1. Every call gives something back, and what comes back has a kind.
2. If the job has an answer to give, the kind is the kind of the answer, such as a whole number.
3. If the job has no answer, the kind is "the done flag".
4. There is only one done flag, so it can hold no information.
5. Because the flag is always the same, you do not have to write the "you get back" line for it. The shop fills it in.
6. You can still look at the flag, for example by printing it. It shows a fixed word.
7. The flag is not a slip. A name made to hold a text or a number refuses it.
8. A few jobs never finish at all, because they crash or run for ever. No flag ever pops up. They have a kind of their own: "never comes back".

### Name it

The done flag is **`Unit`**, the type that has only one value, also called `Unit`. The Kotlin API documentation says this type corresponds to `void` in Java, which is an older language that Android apps can also be written in. The functions page adds that, unlike `void`, `Unit` is an actual type and you can use its value. If a function has braces and no written return type, Kotlin treats it as returning `Unit`. You do not have to write `: Unit` or `return Unit`.

Printing the flag shows the text `kotlin.Unit`. That text is general knowledge, because the documentation pages do not show it. It is not the same as `null`, which is the mark for "nothing here". The flag is a real value.

```kotlin
fun sayHi(name: String) {
    println("Hi, $name")
}

fun main() {
    val flag = sayHi("Ana")
    println(flag)
}
```

This prints `Hi, Ana` and then `kotlin.Unit`. The call prints the line, which is the clunk. The flag it gives back is kept under the name `flag` and printed on the next line.

Kotlin refuses to run this one, because a name made to hold text cannot hold the flag. The documentation says `sayHi` returns `Unit`, and the refusal follows from that (our own reasoning).

```kotlin
// does not compile
fun sayHi(name: String) {
    println("Hi, $name")
}

fun main() {
    val greeting: String = sayHi("Ana")
    println(greeting)
}
```

A job that never finishes has the type **`Nothing`**. The exceptions page calls it a built-in type that fits wherever any other type is expected. It says `Nothing` stands for functions or expressions that never complete successfully, either because they always throw an exception or because they run for ever. `TODO()` is the one to remember. The documentation says it is a placeholder that marks code needing future work, and that it always throws a `NotImplementedError`. This is why the next program compiles even though its `Int` promise is never kept (our own reasoning).

```kotlin
fun notReadyYet(): Int {
    TODO("write this later")
}

fun main() {
    println("The program starts")
}
```

It prints `The program starts`. Nobody calls `notReadyYet`, so its body never runs.

## Predict the output

```challenge
type: predict
run: false
id: print-of-print
verified: kotlin-2.4.21
code: |
  fun main() {
      println(println("a"))
  }
options:
  - id: a
    text: "a, then null"
    why: "You may have expected the mark for nothing. The inner println drops its letter and raises the done flag, and the flag is a real value, not null."
  - id: b
    text: "a, then kotlin.Unit"
    correct: true
    why: "The inner println does its clunk and prints a. It gives back the done flag, and the outer println prints the flag."
  - id: c
    text: "a"
    why: "You may have thought the outer println has nothing to print. It has the flag to print."
output: |
  a
  kotlin.Unit
```

```challenge
type: predict
run: false
id: flag-in-a-name
verified: kotlin-2.4.21
code: |
  fun shout(word: String) {
      println(word.uppercase())
  }

  fun main() {
      val result = shout("hi")
      println("Got: $result")
  }
options:
  - id: a
    text: "HI, then Got: HI"
    why: "You may have thought the result is the text that was printed. Printing is the clunk, and the flag is what comes back."
  - id: b
    text: "HI, then Got: null"
    why: "You may have thought a postbox with no slip gives the mark for nothing. It gives the done flag."
  - id: c
    text: "HI, then Got: kotlin.Unit"
    correct: true
    why: "The call prints HI, which is the clunk. The flag is kept under result, and the template shows the flag as kotlin.Unit."
output: |
  HI
  Got: kotlin.Unit
```

```challenge
type: predict
run: false
id: todo-is-called
verified: kotlin-2.4.21
code: |
  fun price(): Int {
      TODO("ask the shop")
  }

  fun main() {
      println("Start")
      val p = price()
      println("End")
  }
options:
  - id: a
    text: "Start, then the program stops with an error"
    correct: true
    why: "The call to price never comes back, because TODO always throws. The flag never pops up, so the line that prints End is never reached."
  - id: b
    text: "Start, then End"
    why: "You may have read TODO as a note to yourself. It is a real call that always throws an error."
  - id: c
    text: "Kotlin refuses to run it, because price never hands back an Int"
    why: "You may have expected the missing Int to be refused. A job that never comes back fits any kind of answer, so Kotlin accepts it."
output: |
  Start
```

## Tricks to remember

- Posting a letter: the clunk is the job, the flag is the answer.
- The flag is always the same, so the flag holds no news.
- Printing is not giving back.
- `Nothing` means the box never comes back. `Unit` means it came back with the flag.

## Common mistakes

- **Thinking no answer means null.** The postbox gave no slip, so it feels like "nothing here". But every call gives a value, and `Unit` is a real one. `null` has a different job, for a box that may hold nothing.
- **Mixing up printing with giving back.** Text shows up when the function runs, so it feels like the answer. The text is the clunk. What comes back is the flag, and a name made for text cannot hold it.
- **Storing the flag in a name meant for a text or number.** A call looks like it could give back anything, so it feels fine. Kotlin refuses it. There is rarely a reason to store the flag at all.
- **Thinking `TODO()` is a comment.** It sits quietly in the code, so it feels like a note. It is a call, and it throws an error when it runs.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its state page shows `HelloContent(name: String, onNameChange: (String) -> Unit)`. The part `(String) -> Unit` describes a small piece of code the screen can run later. It takes a text, and the `Unit` after the arrow says it gives nothing useful back. The functions page says you must write `Unit` when it is part of a type like this one. The page describes the matching `onValueChange: (T) -> Unit` as an event that requests the value to change.

A symptom you may see is a text box that ignores typing. The page says a `TextField` does not update itself. It updates only when its `value` parameter changes. This next part is our own reasoning: a piece of code that gives back `Unit` has only its side job to offer, which is to store the new text. If that job does nothing, no new text reaches the box, and the typing seems to vanish.

## Say it in an interview

**What does a Kotlin function return if it has no `return` statement?**
It returns `Unit`. The documentation says a function with braces and no written return type is treated as returning `Unit`, and that you do not have to write it.

**How is `Unit` different from `void` in Java?**
The API page says `Unit` corresponds to `void`. The functions page adds that `Unit` is an actual type and you can use its value. The functions page says you must write `Unit` in a function type such as `() -> Unit`. My own reasoning: a function type always names what its code gives back, and for code with nothing useful to give back the name is `Unit`.

**Is `Unit` the same as `null`?**
No. `Unit` is a value, the only one of its type. `null` is the mark for "nothing here" and is allowed only in types written with a question mark. A function that returns `Unit` has not returned nothing, it has returned the flag.

**What is `Nothing`, and where would you meet it?**
The exceptions page calls `Nothing` a built-in type that fits wherever any other type is expected, for code that never completes successfully because it always throws or never stops. `TODO()` returns it and always throws `NotImplementedError`, and the compiler marks the code after such a call as unreachable. The trade-off: `TODO()` lets unfinished code compile, but if it runs, the app stops.

## Recap

1. A function with nothing to give back still gives back `Unit`, a real value with one possible case.
2. `Unit` is not null, and printing is not giving back.
3. `Nothing` is for code that never finishes, such as `TODO()`, which always throws.

## You can now

- Predict what a call to a printing function gives back, and what is printed when you print it.
- Predict the output when a printing function's result is stored and printed.
- Predict what happens when a function whose body is `TODO()` is called.
