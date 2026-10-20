---
id: kotlin/null-safety/nullable-types
title: How does Kotlin mark a value that might be missing?
status: shadow
difficulty: easy
order: 18
requires: [kotlin/null-safety/what-is-null]
summary: A question mark after a type says the value might be missing. Kotlin then refuses to use it until you deal with that.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/null-safety.html ("To allow null values, declare a variable with a ? sign right after the variable type"; "Null can not be a value of a non-null type String"; the type system distinguishes nullable and non-nullable types; the compiler reports an error for a direct .length on a nullable); kotlinlang.org/docs/java-interop.html (Java types with nullability annotations are represented as actual nullable or non-nullable Kotlin types; without them they are platform types); developer.android.com/guide/components/activities/activity-lifecycle (onCreate(savedInstanceState: Bundle?) and "If the activity has never existed before, the value of the Bundle object is null"); developer.android.com/kotlin/interop (every non-primitive parameter, return and field type in a public API should have a nullability annotation; non-annotated types are platform types). The reading that ? alone does not make code safe, and the line-by-line refusal in the trace item, are derived. Snippets are hand-traced, not run. -->
## The question

An address form has two boxes. The street name is filled in for every address. The flat number is filled in for some addresses and left empty for houses. How can Kotlin know which box may be empty and which never is? And what does the question mark after a type name really promise?

## Mental model

Think of a wall of parcel lockers.

- The wall of lockers is your program.
- A locker door is a name that holds a value.
- The parcel inside is the value. An empty locker holds nothing.
- The sticker on the door has two parts: the kind of parcel (a text or a number) and a promise. One promise reads "always has a parcel". The other reads "may be empty".
- The courier who puts a parcel in is the line that gives the name its value.
- The person who collects is the code that uses the value.

The sticker is fixed to the door before the day starts. Nobody may break its promise.

**Where the analogy stops:** A real sticker only describes the locker, and a courier could ignore it. Kotlin checks every delivery and every collection against the stickers before the program runs. Also, "may be empty" does not mean "is empty right now". A "may be empty" locker can hold a parcel, and the sticker does not make it safe to open without looking.

## Build the logic

### Notice

Locker 1 has the sticker "always has a parcel (a text)". Locker 2 has the sticker "may be empty (a text)".

- A courier brings a parcel to Locker 1. That is allowed.
- A courier brings nothing to Locker 1. The door refuses, before the day starts.
- A courier brings nothing to Locker 2. That is allowed.
- A collector reaches into Locker 1 and counts the letters in the parcel.
- A collector reaches into Locker 2 and is told to look first.

### Guess

A text parcel sits in Locker 1. Someone wants to move it into Locker 2, and later to move whatever is in Locker 2 into Locker 1. What does the wall allow? Here are three ideas.

- A. Both moves are allowed.
- B. Locker 1 to Locker 2 is allowed. Locker 2 to Locker 1 is refused.
- C. Locker 2 to Locker 1 is allowed. Locker 1 to Locker 2 is refused.

Idea A feels fair because the parcel is a text either way. But Locker 2 might be empty, and Locker 1 may never be. Idea C gets it the wrong way round. Idea B is the answer.

### Build

1. Put a sticker on every locker door. The sticker names the kind of parcel and the promise.
2. For an "always" door, refuse any delivery that could be nothing.
3. For a "may be empty" door, accept a parcel or nothing.
4. A parcel from an "always" door fits into a "may be empty" door, because that door accepts everything.
5. A parcel from a "may be empty" door is refused by an "always" door, because it might be nothing. This holds even when the parcel is there right now.
6. Reaching into a "may be empty" door to use what is inside is refused until you look first or deal with the empty case.
7. The sticker does not fill or empty a locker. It only says what is allowed.

### Name it

A **nullable type** is a type with a question mark after it, such as `String?` or `Int?`. The Kotlin documentation says that to allow `null` values, you declare a variable with a `?` right after the type. Any type has a nullable twin. A **non-null type** has no question mark, such as `String`. The documentation says the type system tells apart the types that can hold `null` and the types that cannot. It gives `null` as an example of what a non-null `String` cannot hold.

The rules from the build steps are these:

| Locker wall | In Kotlin |
|---|---|
| Sticker "always has a parcel" | A type without a question mark, such as `String` |
| Sticker "may be empty" | A type with a question mark, such as `String?` |
| Parcel from an "always" door into a "may be empty" door | Allowed |
| Parcel from a "may be empty" door into an "always" door | Refused |
| Using the contents of a "may be empty" door directly | Refused |

```kotlin
val street: String = "Elm Road"
val flat: String? = null

fun main() {
    val copy: String? = street
    println(copy)
    println(flat)
    println(flat == null)
}
```

This prints `Elm Road`, then `null`, then `true`. The copy of `street` into a `String?` is allowed. Printing an empty `String?` is allowed. The question `flat == null` answers `true` because nothing is there.

Kotlin refuses to run the next two programs. In each, the `flat` locker holds a parcel, and the sticker still says "may be empty".

```kotlin
// does not compile
val flat: String? = "4B"

fun main() {
    val sure: String = flat
    println(sure)
}
```

```kotlin
// does not compile
val flat: String? = "4B"

fun main() {
    println(flat.length)
}
```

## Predict the output

```challenge
type: predict
run: false
id: seats-and-nothing
verified: not-run
code: |
  val seats: Int? = 3
  val spare: Int? = null

  fun main() {
      println(seats)
      println(spare)
      println(seats == spare)
  }
options:
  - id: a
    text: "3, then 0, then false"
    why: "You may have expected an empty locker to show as zero. Nothing is not zero, and it prints as the word null."
  - id: b
    text: "3, then null, then false"
    correct: true
    why: "The seats locker holds 3 and the spare locker is empty. Two lockers with the same sticker still do not hold the same thing, so the question gets no."
  - id: c
    text: "3, then null, then true"
    why: "You may have thought two lockers with the same sticker count as equal. One holds a parcel and one holds nothing, so they differ."
output: |
  3
  null
  false
```

```challenge
type: predict
run: false
id: which-lines-are-refused
verified: not-run
code: |
  val a: String = "hi"      // line 1
  val b: String? = "hi"     // line 2
  val c: String = null      // line 3
  val d: String? = a        // line 4
  val e: String = b         // line 5
options:
  - id: a
    text: "Line 3 only"
    why: "You may have thought line 5 passes because b holds hi. The sticker on b says may be empty, whatever it holds now."
  - id: b
    text: "Lines 3, 4 and 5"
    why: "You may have thought a parcel cannot move from an always door to a may-be-empty door. That move is allowed, because that door accepts everything."
  - id: c
    text: "Lines 3 and 5"
    correct: true
    why: "Line 3 hands nothing to an always door. Line 5 moves a parcel from a may-be-empty door into an always door, and that door must refuse it. Lines 1, 2 and 4 are fine."
output: |
  Kotlin refuses line 3 and line 5.
```

```challenge
type: predict
run: false
id: parcel-in-maybe-door
verified: not-run
code: |
  val code: String = "A1"
  val maybe: String? = "B2"

  fun main() {
      val first: String? = code
      val second: String? = maybe
      println(first)
      println(second)
  }
options:
  - id: a
    text: "A1, then B2"
    correct: true
    why: "A may-be-empty locker can hold a parcel. Both lockers hold one, and printing a locker shows what is inside."
  - id: b
    text: "A1, then null"
    why: "You may have read may be empty as is empty. The sticker only says what is allowed, and this locker holds B2."
  - id: c
    text: "Kotlin refuses the first copy"
    why: "You may have thought a parcel from an always door cannot go in a may-be-empty door. That move is allowed."
output: |
  A1
  B2
```

## Tricks to remember

- The sticker says what is allowed, not what is inside.
- "Always" into "maybe" is fine. "Maybe" into "always" is refused.
- A question mark does not fill the locker. It only lets it be empty.
- Look before you reach into a "may be empty" door.

## Common mistakes

- **Reading `String?` as "is empty".** The words "may be empty" sound like a state. The sticker is a rule about what is allowed, and the locker may hold a parcel right now.
- **Adding `?` to silence a red line.** It feels like the question mark makes the code safe. It lets the name be empty, and then every use of it needs a look first. Add the `?` when "nothing here" is a real answer, not to make a message go away.
- **Copying a `String?` into a `String` because the value is there.** You saw the parcel arrive, so it feels safe. Kotlin judges by the sticker and refuses the copy. Deal with the empty case first, then use the value.

## Where this shows up in Android

An **activity** is one screen of an Android app. When the system makes one, it calls `onCreate(savedInstanceState: Bundle?)`, and the Android page about how a screen is created and destroyed shows that exact line. The type is `Bundle?`, which is a locker with the sticker "may be empty". The page says `savedInstanceState` is a `Bundle` object holding the activity's previously saved state. It says: "If the activity has never existed before, the value of the `Bundle` object is null."

So the first time a screen opens, that locker is empty, and later it may hold a parcel. This next part is our own reasoning: if Kotlin let you reach into `savedInstanceState` without a look, the code would work after the screen has been rebuilt and crash on the very first open. The question mark makes Kotlin refuse that code before the app runs.

## Say it in an interview

**What is the difference between `String` and `String?`?**
A `String` can never hold `null`, and a `String?` can hold a text or `null`. A `String` fits where a `String?` is expected, but not the other way round. The trade-off: you handle the empty case, and in return the compiler finds missing-value mistakes before the app runs.

**Does adding a question mark make code null-safe?**
No. It only lets the name be `null`. After that, the compiler refuses to use it until you check or handle it. The safety comes from that handling. My own reasoning: a `?` added only to remove an error moves the problem and does not solve it.

**Why can you put a `String` into a `String?` but not the reverse?**
A `String?` accepts a text or nothing, so a text always fits. A `String` accepts only a text, so a `String?` might break it. The documentation shows that `null` itself is refused by a non-null `String`.

**Where do nullable types come from when you call Java code?**
The Kotlin documentation says Java types that carry nullability annotations are read as real nullable or non-null Kotlin types. Without annotations they become platform types, and the checks are relaxed. The Android interop page says every non-primitive parameter, return and field type in a public API should have a nullability annotation. So an annotation in Java gives your Kotlin code the right sticker.

## Recap

1. A question mark after a type, such as `String?`, says the value may be `null`.
2. A `String` fits into a `String?`, but a `String?` is refused by a `String`.
3. The question mark does not make code safe, and Kotlin refuses direct use of the value.

## You can now

- Predict what a program prints when nullable boxes hold a value and when they hold `null`.
- Spot which lines Kotlin refuses when `String` and `String?` names are copied to each other.
- Predict what a nullable name holds when the code gives it a text.
