---
id: kotlin/extensions-scope-and-delegates/extension-functions
title: How can I add a function to a class I did not write?
status: shadow
difficulty: medium
order: 56
requires: [kotlin/classes-and-objects/member-functions]
summary: Add a function to a class you cannot edit. See what it can reach, and when the class's own function wins.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/docs/extensions.html : extensions do not modify the class; the truncate example; member wins over extension with the same receiver type, name and compatible arguments; extensions are dispatched statically; an extension outside the class cannot reach private or protected members (User example); Shape/Rectangle getName example.
- https://developer.android.com/kotlin/ktx : Android KTX is a set of Kotlin extensions; SharedPreferences.edit extension function signature and the edit { } call.
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/extensions-scope-and-delegates/extension-functions.
- Code blocks and Predict outputs were hand-traced and not compiled in this session (no shell). Run scripts/kt-run.sh before review.
-->

## The question

Your team writes the same two steps all day on text: turn it into capital letters, then add an exclamation mark. Everyone wants to ask a word to "shout", the way they ask it for other things like its length. But the text type was written by someone else, and you cannot open it and add a function. Can you still make that work? And if you can, what stops your function from reaching everything the text type owns?

## Mental model

Think of a phone case with a stand built in.

- The phone is a class someone else made. You cannot open it up.
- The phone's own buttons and sockets are its member functions.
- The case is the new function you write. You put it on from outside.
- Propping the phone up on the stand is calling the new function on a phone.
- The case touches only the outside of the phone: the screen, the buttons and the sockets. It cannot reach the battery inside.
- If the phone already has a built-in stand, you use the built-in one and ignore the case's stand.

**Where the analogy stops:** A real case is a new object, wrapped around the phone. In Kotlin nothing wraps the class. The new function is only a new way of writing a call, and the phone is exactly the same as before.

## Build the logic

### Notice

Maya has a word, "hi". She wants "HI!" back. She does it with two steps every time, in many places. Her teammates copy the two steps and sometimes forget the exclamation mark. Everyone would rather ask the word itself: "shout". The word's kind, text, has no such ability, and Maya cannot add one.

### Guess

How can Maya get a "shout" that she calls on a word? Here are three ideas.

- A. Open the text kind and add the ability to it.
- B. Write the ability outside, and mark that it is meant to be called on a word.
- C. Make a new kind that copies text and adds the ability.

Idea A is not possible, because the text kind belongs to someone else. Idea C means every word in the program must be turned into the copy first, and the copy is a different kind from text. Idea B is the answer.

### Build

1. Write the function outside the phone, as a normal function.
2. Say which kind of thing it is called on, in front of its name.
3. Inside, the thing it was called on has a name. Use it to read and work on that thing.
4. Use only what is on the outside of the phone. The inside is closed to you.
5. Call it in the usual way: the thing, a dot, the name.
6. If the phone already has a function with the same name and the same inputs, the phone's own is used and your case is ignored.

### Name it

An **extension function** is a function you write outside a class and call as if it belonged to that class. The Kotlin documentation says extensions do not modify the classes they extend, and that you do not add new members. You write the class name in front of the function name. That class is the **receiver type**. Inside, the word `this` is the value the function was called on.

```kotlin
fun String.shout(): String {
    return this.uppercase() + "!"
}

fun main() {
    val word = "hi"
    println(word.shout())
    println(word)
}
```

It prints `HI!` and then `hi`. The first line used the case. The second line shows the word was not changed: `uppercase()` made a new text and left the old one alone.

The documentation says a member wins when it has the same receiver type, the same name and arguments that fit. In this program, both the phone and the case have a function called `stand`.

```kotlin
class Phone {
    fun stand() {
        println("built-in stand")
    }
}

fun Phone.stand() {
    println("case stand")
}

fun main() {
    Phone().stand()
}
```

It prints `built-in stand`. The extension is never called.

The documentation says an extension declared outside its receiver type cannot reach its `private` or `protected` members. Kotlin refuses to run this one, because `cash` is private to `Wallet`.

```kotlin
// does not compile
class Wallet(private val cash: Int)

fun Wallet.showCash() {
    println(cash)
}

fun main() {
    Wallet(5).showCash()
}
```

## Predict the output

```challenge
type: predict
run: false
id: member-beats-case
verified: not-run
code: |
  class Phone {
      fun stand() {
          println("built-in stand")
      }
  }
  fun Phone.stand() {
      println("case stand")
  }

  fun main() {
      Phone().stand()
  }
options:
  - id: a
    text: "built-in stand"
    correct: true
    why: "The phone already has a stand of its own. When the phone and the case offer the same name, the phone's own function is used."
  - id: b
    text: "case stand"
    why: "You may have thought the newer function wins. The phone's own function comes first."
  - id: c
    text: "built-in stand and then case stand"
    why: "You may have thought both stands are used. Only one function is chosen for each call."
output: |
  built-in stand
```

```challenge
type: predict
run: false
id: case-leaves-phone-alone
verified: not-run
code: |
  fun String.shout(): String {
      return this.uppercase() + "!"
  }

  fun main() {
      val word = "hi"
      println(word.shout())
      println(word)
  }
options:
  - id: a
    text: "HI! and HI!"
    why: "You may have thought the case changes the phone. The word is untouched, and uppercase() made a new text."
  - id: b
    text: "The program is refused, because the text kind has no shout function"
    why: "You may have expected only the phone's own functions to be callable. The case gives you a new way of writing the call."
  - id: c
    text: "HI! and hi"
    correct: true
    why: "The case builds a new text, HI!, and hands it back. The original word is still hi."
output: |
  HI!
  hi
```

## Tricks to remember

- A case changes how you write the call. It does not change the phone.
- The phone's own function beats a case with the same name.
- A case touches the outside of the phone only, so private things are out of reach.
- In front of the name is the kind it is called on. Inside, `this` is the thing itself.

## Common mistakes

- **Thinking an extension changes the original class.** The call looks like a member call, so it feels like the class gained a function. The documentation says extensions do not modify the class. Only your call changes.
- **Trying to read a private property from an extension.** The extension reads like part of the class, so the private value feels in reach. It is outside, and the compiler refuses. Use the public functions and properties of the class.
- **Writing an extension with the same name as a member.** It looks like it overrides the member. The member wins, so your code is never called. Pick a different name.
- **Expecting an extension to pick the real object's version, like a member does.** The call looks the same as a member call, so it feels like the real object decides. The documentation says extensions are dispatched statically: the compiler picks the function from the declared type of the value, not from the real object. Check the declared type before you trust the call.

## Where this shows up in Android

Picture code that saves a setting with `sharedPreferences.edit { putBoolean("key", value) }`. A `SharedPreferences` is a small store of settings saved on the phone (general knowledge). A reader looks through the list of functions of `SharedPreferences` and cannot find that `edit` with curly brackets. The Android KTX page says Android KTX is a set of Kotlin extensions, and that `SharedPreferences.edit` is an extension function from Android KTX. So it is a case on a class that Android wrote, and not a function inside the class. This lesson explains why: the phone is unchanged, and the case adds a new way to call. The signature on that page also shows a lambda and a default value. That is more than this lesson covers.

## Say it in an interview

**What is an extension function, and why would you use one?**
It is a function you write outside a class and call as if it were a member. The Kotlin documentation says it lets you add functionality without inheritance and without changing the class. It is useful when you do not own the class, such as `String` or an Android class.

**Are extension functions resolved at compile time or at run time?**
At compile time. The documentation says they are dispatched statically, so the compiler picks the function from the declared type of the receiver, not from the real object. Its example has a `Shape` and a `Rectangle`, and the call through a `Shape` variable runs the `Shape` extension. My own reasoning: this is because the function is not really a member, so there is nothing to look up at run time.

**What happens if the class later gets a member with the same name as your extension?**
The member wins. The documentation says the member takes precedence when the receiver type, name and arguments fit. My own reasoning: your extension is still there but is no longer called, so the behaviour of the program can change without any line of yours changing.

**Can an extension use a private property of the class?**
No. The documentation says an extension declared outside the class cannot access its `private` or `protected` members. It works through the public functions and properties, as its `isSecure()` example does.

## Recap

1. An extension function is written outside a class and called as if it were a member.
2. It does not change the class and cannot reach private members.
3. If the class has a member with the same name and inputs, the member wins.

## You can now

- Predict which function runs when a member and an extension share a name.
- Predict that calling an extension on a value does not change the original value.
