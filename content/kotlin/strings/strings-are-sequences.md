---
id: kotlin/strings/strings-are-sequences
title: How do I get one character out of a String?
status: shadow
difficulty: easy
order: 22
requires: [kotlin/types-and-variables/basic-types]
summary: A String is a row of characters numbered from 0. Learn to read one, count them and find the last.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/strings.html ; https://kotlinlang.org/docs/characters.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-char/ ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/get-or-null.html ; https://developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip -->
## The question

A game asks for the player's name and shows only its first letter inside a round badge. For the name Kotlin, the badge should show K. How does a program pick one letter out of a name? And what should happen when the program asks for a letter that is not there?

## Mental model

Think of a string of beads on a thread.

- The thread with all its beads is the whole text.
- Each bead is one letter, digit, space or symbol.
- A small tag hangs under each bead with its position number. The first tag says 0, not 1, because the number counts how many beads sit before it.
- Counting every bead tells you how long the thread is.
- Asking for a bead means naming a tag number.

Ask for tag 0 and you get the first bead. Ask for a tag that no bead carries and there is nothing to hand over.

**Where the analogy stops:** On a real thread you can slide a bead off and thread a new one. Kotlin text cannot be changed after it is made, so no bead is ever swapped. Also, a real bead is one whole thing, while a few symbols, such as some emoji, take two beads in Kotlin. The interview questions at the end explain that.

## Build the logic

### Notice

The thread for "Kotlin" has six beads: K, o, t, l, i, n. Their tags read 0, 1, 2, 3, 4, 5. The bead with tag 0 is K, and the bead with tag 5 is n. No bead carries tag 6, because only six beads exist.

### Guess

Which tag does the first bead carry? Here are three ideas.

- A. Tag 1, because people count from one.
- B. Tag 0, because a tag counts the beads before it.
- C. It depends on how long the thread is.

Idea A is how we count apples, and it is the most common wrong guess. Idea C mixes up the tag with the count. Idea B is the answer.

### Build

1. Count the beads. For "Kotlin" the count is 6.
2. Give the first bead tag 0, and give each bead after it a tag one higher.
3. The last bead then carries the count minus 1. Six beads end at tag 5.
4. To get a bead, ask the thread for the bead with a certain tag.
5. If you name a tag that no bead carries, the program stops with an error.

### Name it

Text in Kotlin has the type **`String`**. Each bead is a **`Char`**, one character written in single quotes, such as `'K'`. The tag number is the **index**. The Kotlin documentation says a string index starts at zero, and you read one character with square brackets, as in `language[0]`. The number of characters is the **`length`**. A dot after a name asks the value about itself, so `language.length` asks the text how long it is.

The last index is `length - 1`. The documentation says that if you try to access an index outside the valid range, Kotlin throws an exception. An **exception** is an error that stops the program at that line.

| Beads | In Kotlin |
|---|---|
| The thread | A `String` |
| One bead | A `Char` |
| A tag number | An index, counted from 0 |
| How many beads | `length` |
| The last tag | `length - 1` |

```kotlin
fun main() {
    val language = "Kotlin"
    println(language.length)
    println(language[0])
    println(language[language.length - 1])
}
```

It prints `6`, then `K`, then `n`. The first `println` counts the beads. The second asks for tag 0. The third asks for tag 5, which is `6 - 1`.

Kotlin refuses to run this one, because a `String` has no way to swap a bead. The documentation says a `String` is **immutable**, which means its contents stay the same after it is created.

```kotlin
// does not compile
fun main() {
    val language = "Kotlin"
    language[0] = 'k'
}
```

## Predict the output

```challenge
type: predict
run: false
id: length-and-last-bead
verified: not-run
code: |
  fun main() {
      val word = "hello"
      println(word.length)
      println(word[4])
  }
options:
  - id: a
    text: "5, then l"
    why: "You may have counted tags from 1. Tag 4 is the fifth bead, which is o, because the tags start at 0."
  - id: b
    text: "4, then o"
    why: "You may have taken the length to be the last tag. The length counts the beads, so it is 5."
  - id: c
    text: "5, then o"
    correct: true
    why: "The thread has five beads, so the length is 5. Tags run 0 to 4, and the bead with tag 4 is the last one, o."
output: |
  5
  o
```

```challenge
type: predict
run: false
id: last-bead-by-length
verified: not-run
code: |
  fun main() {
      val city = "Paris"
      println(city[city.length - 1])
      println(city[0])
  }
options:
  - id: a
    text: "s, then P"
    correct: true
    why: "Paris has five beads, so the last tag is 5 - 1, which is 4. Tag 4 holds s, and tag 0 holds P."
  - id: b
    text: "The program stops, because the tag 5 does not exist"
    why: "You may have used the length itself as the tag. The code asks for the length minus 1, which is tag 4."
  - id: c
    text: "i, then P"
    why: "You may have stepped back two beads from the end. The last bead is one step back from the count."
output: |
  s
  P
```

```challenge
type: predict
run: false
id: tag-equal-to-length
verified: not-run
code: |
  fun main() {
      val code = "ab"
      println("start")
      println(code[code.length])
      println("end")
  }
options:
  - id: a
    text: "start, b, end"
    why: "You may have expected the thread to hand over its last bead. The tag 2 belongs to a third bead, and there is none."
  - id: b
    text: "start, then the program stops with an error"
    correct: true
    why: "The thread has two beads with tags 0 and 1. Asking for tag 2 names a bead that does not exist, so the program stops before it prints end."
  - id: c
    text: "start, null, end"
    why: "You may have expected a missing bead to give back nothing. Square brackets do not do that. They stop the program."
output: |
  start
```

## Tricks to remember

- The first bead carries tag 0.
- The last tag is the length minus 1.
- The length is never a valid tag.
- Six beads, tags 0 to 5.

## Common mistakes

- **Counting the first letter as position 1.** Everyday counting starts at one, so it feels natural. Kotlin counts how many beads come before the tag, so the first tag is 0.
- **Using the length as the last tag.** The length is the count, so it looks like the end of the thread. The last tag is one less. Write `length - 1`.
- **Trying to swap a letter with `text[0] = 'k'`.** It works for some other containers, so it feels allowed. A `String` cannot be changed, and Kotlin refuses to run the line. Make a new text instead.

## Where this shows up in Android

The Android tip calculator lesson keeps the text of an input box in a variable that starts as the empty text `""`. The box is on the screen before the person has typed anything. Empty text is a thread with zero beads, so its length is 0 and it has no tag 0.

This next part is our own reasoning: a round badge that shows the first letter of a name would read tag 0 of that text. On the first screen there is no bead with that tag, so the program stops with an exception and the app closes. The badge code needs to know the length first.

## Say it in an interview

**What does `"Kotlin"[0]` give, and what type is it?**
It gives `'K'`, a `Char`. The Kotlin documentation says a string index starts at zero. The last valid index is `length - 1`.

**What does it mean that a `String` is immutable?**
The documentation says that after you create a `String`, its contents stay the same, and any operation that looks like a change creates a new string. General knowledge: because it never changes, many parts of a program can share one `String` safely. The trade-off is that every change makes a new string.

**Is the `length` of a string always the number of symbols a person sees?**
No. The Kotlin characters page says a `Char` is one UTF-16 code unit, which is one 16-bit piece of how text is stored as numbers. A symbol outside the basic range, such as an emoji, is stored as two `Char` values called a surrogate pair. The page shows an emoji with a length of 2. My own reasoning: an app that cuts a name at 10 characters could cut an emoji in half. The page says to use Unicode code points, stored as `Int`, to handle such symbols one by one.

**How do you read a character that may not exist?**
Square brackets throw an exception when the index is out of range. The standard library has `getOrNull`, which returns the character at the index or `null` if the index is out of bounds. `null` is the mark for no value, and the result type is `Char?`. The trade-off: a crash shows a bug loudly, while `null` makes you decide what to do.

## Recap

1. A String is a row of characters numbered from 0.
2. The length counts the characters, so the last index is the length minus 1.
3. Asking for an index outside the string throws an exception and stops the program.

## You can now

- Predict the length and a chosen character of a short string.
- Predict which character `text[text.length - 1]` reads.
- Spot an index equal to the length that stops the program.
