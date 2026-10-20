---
id: kotlin/types-and-variables/type-inference
title: How does Kotlin know a type I never wrote?
status: shadow
difficulty: easy
order: 6
requires: [kotlin/types-and-variables/basic-types]
summary: Kotlin reads how a value is written and picks its type for you. The name then keeps that type.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/kotlin-tour-basic-types.html ("Every variable and data structure in Kotlin has a type"; types tell the compiler what you are allowed to do; "Kotlin's ability to infer the type is called type inference"); kotlinlang.org/docs/basic-syntax.html (type inference section, val x = 5 and Int is inferred); kotlinlang.org/docs/numbers.html (Int inferred if the value fits, otherwise Long; L suffix; floating-point literals are Double by default; val oneBillion: Long = 1_000_000_000); kotlinlang.org/docs/characters.html (Kotlin infers Char from single quotes); kotlinlang.org/docs/booleans.html (Kotlin infers Boolean); kotlinlang.org/docs/faq.html (Kotlin is statically typed); developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip (tipPercent converted to a Double with no type written). Own reasoning: assigning text to a name first given a number is refused, because the pages say every variable has a type and the type limits what you may do; the pages show the refusal only for null into a String. Outputs hand-traced, not compiled. -->
## The question

You write a line that gives a name the number 5. You never write what kind of value it is. Yet Kotlin treats the name as a whole number. How does Kotlin know, and does a name with no written kind hold anything you like?

## Mental model

Think of a shop assistant at the door of a big shop.

- You walk in holding something. That is the value.
- The shop has aisles. Each aisle is a type: shoes, jugs, light switches.
- The assistant looks at what you hold and points to the aisle.
- A sign is the type written out. You can hold it up and say "shoes" yourself.

You never had to say "shoes". The assistant saw the shoe in your hand. Once the assistant has pointed, that is your aisle, and you stay in it.

**Where the analogy stops:** A real assistant judges by eye and can get it wrong. Kotlin follows fixed rules about how a value is written, such as a point or quotation marks, so it picks the same way every time. Also, a real customer may wander to another aisle. A Kotlin name keeps the type it got.

## Build the logic

### Notice

Three customers come in. One holds a shoe, and the assistant says "shoes". One holds a measuring jug, and the assistant says "jugs". One holds a light switch, and the assistant says "switches". None of them said a word. The assistant read each item.

### Guess

What does the assistant do about the aisle when you say nothing? Here are three ideas.

- A. Gives you no aisle, so you can swap what you hold for anything.
- B. Looks at what you hold, picks the aisle, and that aisle stays yours.
- C. Makes you say the aisle every time you come in.

Idea A is the common guess, because "no sign" sounds like "no rule". But the assistant did pick an aisle. Idea C would work, but you would have to say the aisle on every line. Kotlin does it the way Idea B says.

### Build

1. You show a value.
2. The assistant looks at how it is written. A whole number goes to the whole-number aisle. A number with a point goes to the decimal aisle.
3. One character in single quotes goes to the character aisle. Words in double quotes go to the text aisle. A yes-or-no answer goes to the yes-or-no aisle.
4. A whole number too big for the whole-number aisle goes to the big-number aisle.
5. The aisle you got stays yours. If you later try to bring something from another aisle, the assistant refuses.
6. If you want an aisle the assistant would not pick, hold up a sign and say it yourself.

### Name it

Kotlin's ability to work out a type for you is called **type inference**. The Kotlin documentation says every variable has a type, and that when you leave the type out, Kotlin infers it.

Here are the reading rules, from the documentation. The underscores in `3_000_000_000` are only for reading. They help your eye count the zeros and do not change the number.

| What you write | Type Kotlin infers |
|---|---|
| `5` | `Int`, if the number fits in an `Int` |
| `3_000_000_000` | `Long`, because it does not fit in an `Int` |
| `10L` | `Long`, because of the `L` after the number |
| `19.99` | `Double` |
| `'A'` | `Char` |
| `true` | `Boolean` |

The written type is still allowed. You hold up the sign: `val small: Long = 5`. The documentation shows a line like this to make a `Long`.

```kotlin
fun main() {
    val count = 5
    val price = 19.99
    val letter = 'A'
    val big = 3_000_000_000
    val small: Long = 5
    println("$count $price $letter $big $small")
}
```

It prints `5 19.99 A 3000000000 5`. The first four names have no written type. The last one has a sign.

Inference does not turn a name into a free-for-all. My own reasoning: the pages say every variable has a type, and the type limits what you may do. So this program is refused.

```kotlin
// does not compile
fun main() {
    var count = 5
    count = "five"
    println(count)
}
```

## Predict the output

```challenge
type: predict
run: false
id: seven-and-seven-point-zero
verified: not-run
code: |
  fun main() {
      val a = 7
      val b = 7.0
      println(a / 2)
      println(b / 2)
  }
options:
  - id: a
    text: "3, then 3.5"
    correct: true
    why: "The assistant sends 7 to the whole-number aisle, so dividing gives a whole number, 3. 7.0 has a point, so it goes to the decimal aisle and keeps its .5."
  - id: b
    text: "3.5, then 3.5"
    why: "You may have thought a number with no sign is a decimal. A whole number goes to the whole-number aisle, and dividing there drops the fraction."
  - id: c
    text: "3, then 3"
    why: "You may have thought the point is ignored. A number written with a point is read as a decimal, so its fraction stays."
output: |
  3
  3.5
```

```challenge
type: predict
run: false
id: too-big-for-an-int
verified: not-run
code: |
  fun main() {
      val small = 2_000_000
      val big = 3_000_000_000
      println(small)
      println(big)
  }
options:
  - id: a
    text: "The program is refused, because 3000000000 does not fit in an Int"
    why: "You may have thought the whole-number aisle is the only one. The documentation says Kotlin infers a Long when the value does not fit in an Int."
  - id: b
    text: "2000000, then -1294967296"
    why: "You may have thought the big number wraps around inside an Int. Kotlin picks the big-number aisle for it, so nothing wraps."
  - id: c
    text: "2000000, then 3000000000"
    correct: true
    why: "The assistant sends the first number to the whole-number aisle and the second to the big-number aisle, because it does not fit. Both print as written."
output: |
  2000000
  3000000000
```

```challenge
type: predict
run: false
id: text-into-a-number-name
verified: not-run
code: |
  fun main() {
      var count = 5
      count = 6
      count = "seven"
      println(count)
  }
options:
  - id: a
    text: "Line 3 is refused, because count already has a number"
    why: "You may have thought a name cannot change at all. A var can change, and 6 is a whole number, so it stays in the same aisle."
  - id: b
    text: "Line 4 is refused, because seven is text and count is in the whole-number aisle"
    correct: true
    why: "The first value, 5, put count in the whole-number aisle. 6 stays in that aisle. A word in quotes belongs in the text aisle, so it is turned away."
  - id: c
    text: "Nothing is refused, and it prints seven"
    why: "You may have thought a name with no written type holds anything. It got a type from 5, and it keeps it."
output: "Line 4 is refused, so nothing runs"
```

## Tricks to remember

- No sign does not mean no type. The assistant read the item.
- A point means Double. An `L` means Long. Single quotes mean Char.
- The first value picks the aisle, and the name stays there.
- Want a different aisle? Hold up the sign: write the type.

## Common mistakes

- **Thinking a name with no written type can hold anything.** No type on the page feels like no rule. But the Kotlin tour says every variable has a type. Kotlin worked it out from the value.
- **Expecting `val x = 5` to be a `Long` or a `Double`.** You may mean a big number. The value `5` fits in an `Int`, so that is the type. Write `5L` or `val x: Long = 5` to say what you mean.
- **Reading `5.0` as a whole number.** The zero after the point looks harmless. A number written with a point is a `Double`, so `7.0 / 2` keeps its .5.

## Where this shows up in Android

The Android Tip Time codelab builds a screen with **Jetpack Compose**, the toolkit for building Android screens in Kotlin. It has the line `val tipPercent = tipInput.toDoubleOrNull() ?: 0.0`. The part on the right turns the typed text into a `Double`, or gives 0.0 when there is no number. No type is written after `tipPercent`. The codelab's own words are that this line converts the tip text to a `Double`, so `tipPercent` is a `Double`.

The same codelab writes `val amount = amountInput.toDoubleOrNull() ?: 0.0`, and again no type is written. The app can then do number work with `amount`. My own reasoning: when an Android project has a line with no type, the right-hand side is where the type comes from. Reading that side tells you what the name holds.

## Recap

1. Kotlin works out a variable's type from how its value is written.
2. A point makes a Double, an L makes a Long, and single quotes make a Char.
3. A name keeps the type it first gets, even when you never wrote that type.

## You can now

- Predict what dividing a plain number and a number with a point prints.
- Predict when Kotlin picks a Long for a big whole number.
- Spot the line that is refused when text is put into a name that holds a number.
