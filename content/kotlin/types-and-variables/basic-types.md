---
id: kotlin/types-and-variables/basic-types
title: Which kinds of values does Kotlin know out of the box?
status: shadow
difficulty: easy
order: 5
requires: [kotlin/types-and-variables/val-vs-var]
summary: Kotlin has a different type for whole numbers, decimals, yes or no, one character and text. Pick the one that fits the value.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/types-overview.html (table of basic types and their definitions; everything is an object in the sense that you can call members on any variable); kotlinlang.org/docs/numbers.html (Int range; Int inferred if the value fits, otherwise Long; Double by default for floating-point literals; Float with f; numeric types are not subtypes of one another and need explicit conversions; 5 / 2 is 2; toInt, toDouble); kotlinlang.org/api/core/kotlin-stdlib/kotlin/-double/to-int.html (fractional part is rounded down towards zero); kotlinlang.org/docs/booleans.html (true and false); kotlinlang.org/docs/characters.html (single quotes; a Char is not a numeric type); kotlinlang.org/docs/kotlin-tour-basic-types.html (types tell the compiler what you are allowed to do); developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip (amountInput String, toDoubleOrNull, tipInput converted to Double, null when the field is empty). Interview section: kotlinlang.org/docs/numbers.html (Int and Long minimum and maximum values and bit sizes in the integer types table; division between integer values returns an integer and the compiler discards the fractional part; Int inferred if the value fits, otherwise Long). General knowledge, not from the Kotlin pages: Int overflow wraps around (Int.MAX_VALUE + 1 is -2147483648); a Double is approximate (0.1 + 0.2 prints 0.30000000000000004, a K2 run in review W5-a); money is often kept in a Long of cents or in BigDecimal. Own reasoning: why a price in an Int of whole units is wrong; the Android symptom. Outputs hand-traced, not compiled. -->
## The question

Maya is writing a shop app. It must keep her age, the price 19.99, whether she is a student, her grade letter and her name. Are all five the same kind of thing? Which kind of value should each one be, and what goes wrong if you pick the wrong kind?

## Mental model

Think of the containers in a kitchen.

- An egg box holds whole things you count, such as eggs.
- A measuring jug holds an amount that can have a fraction, such as 0.5 of a litre.
- An oven switch is either on or off.
- A letter magnet on the fridge shows one single letter.
- A recipe card holds words.
- The label on each container says what it is for.

Each container is made for one kind of thing. You would not weigh flour in an egg box.

**Where the analogy stops:** In a real kitchen you can tip eggs into a jug. Kotlin is stricter. Its documentation says number types are not interchangeable and that you must ask for a conversion. Also, a real egg box has a fixed number of holes, and a whole-number container in Kotlin has a limit too. It stops at 2,147,483,647.

## Build the logic

### Notice

Maya's five facts are the number 15, the number 19.99, a yes or no, the letter A and the word Maya. Look at them next to each other. Fifteen is a whole count. 19.99 has a part after the point. Yes or no has only two answers. The letter A is one character. Maya is a row of characters.

### Guess

Which container fits the price 19.99? Here are three ideas.

- A. The measuring jug, because the price has a part after the point.
- B. The egg box, because a price is a whole number of coins.
- C. The recipe card, because 19.99 is something you write down.

Idea B loses the .99. Idea C keeps the digits, but a recipe card is for words. You cannot add up two recipe cards. Idea A is the answer.

### Build

1. Ask: is it a count of whole things, such as an age? Use the egg box.
2. Ask: can it have a part after the point, such as a price? Use the jug.
3. Ask: is it only yes or no? Use the oven switch.
4. Ask: is it one single character? Use the letter magnet.
5. Ask: is it words or any other row of characters? Use the recipe card.
6. The label tells the program what it may do with the container. It may add eggs together. It may not add switches.
7. A container does not pour into another by itself. If you want a jug amount in an egg box, you must say so, and the part after the point is lost.

### Name it

Each container label is a **type**: a rule about what kind of value a name can hold. The Kotlin documentation says types tell Kotlin what you are allowed to do with a variable. You write the type after the name, with a colon: `val age: Int = 15`.

Kotlin's types for these five facts are below.

- **`Int`** holds whole numbers. Its smallest value is -2,147,483,648 and its largest is 2,147,483,647.
- **`Double`** holds numbers with a fractional part. It is the type Kotlin picks for a number written with a point.
- **`Boolean`** holds one of two values, `true` or `false`.
- **`Char`** holds a single character, written in single quotes. The documentation says `Char` is not a numeric type.
- **`String`** holds a sequence of characters, written in double quotes.

Kotlin also has `Long`, for whole numbers too big for an `Int`, and `Float`, for decimals written with an `f` after them. The documentation lists more, such as `Byte`, `Short` and the unsigned types.

Two small tools show how strict the types are. Division between whole numbers gives a whole number: the documentation shows `5 / 2` giving `2`. The function `toInt()` turns a `Double` into an `Int`, and the documentation says the fractional part is rounded down towards zero.

Four values that look alike have four different types.

| What you write | Type | Why |
|---|---|---|
| `3` | `Int` | A whole number that fits in an `Int` |
| `3.0` | `Double` | A number with a point |
| `'3'` | `Char` | One character in single quotes |
| `"3"` | `String` | Text in double quotes |

```kotlin
fun main() {
    val age: Int = 15
    val price: Double = 19.99
    val isStudent: Boolean = true
    val grade: Char = 'A'
    val name: String = "Maya"
    println("$name, $age, $price, $isStudent, $grade")
}
```

It prints `Maya, 15, 19.99, true, A`. Each name has the container that fits its value.

## Predict the output

```challenge
type: predict
run: false
id: whole-and-decimal-division
verified: not-run
code: |
  fun main() {
      println(5 / 2)
      println(5.0 / 2)
  }
options:
  - id: a
    text: "2, then 2"
    why: "You may have thought the point makes no difference. A number written with a point is a Double, and a Double keeps its fraction."
  - id: b
    text: "2.5, then 2.5"
    why: "You may have thought the jug is always used for division. Two whole numbers go into the egg box, so the fraction is dropped."
  - id: c
    text: "2, then 2.5"
    correct: true
    why: "5 and 2 are both whole numbers, so the answer is a whole number, 2. 5.0 has a point, so it is a Double and the answer keeps its .5."
output: |
  2
  2.5
```

```challenge
type: predict
run: false
id: price-in-an-egg-box
verified: not-run
code: |
  fun main() {
      val shown: Double = 19.99
      val stored: Int = shown.toInt()
      println(shown)
      println(stored)
  }
options:
  - id: a
    text: "19.99, then 20"
    why: "You may have expected rounding to the nearest whole number. The documentation says the fractional part is rounded down towards zero."
  - id: b
    text: "19.99, then 19"
    correct: true
    why: "The jug holds 19.99. Pouring it into the egg box drops the part after the point, so the cents are gone."
  - id: c
    text: "19.99, then 19.99"
    why: "You may have thought the egg box can hold a fraction. An Int holds whole numbers only."
output: |
  19.99
  19
```

```challenge
type: predict
run: false
id: four-look-alikes
verified: not-run
code: |
  val a = 3
  val b = 3.0
  val c = '3'
  val d = "3"
  // Which type does Kotlin pick for a, b, c and d, in that order?
options:
  - id: a
    text: "Int, Double, Char, String"
    correct: true
    why: "A whole number is an Int, a number with a point is a Double, single quotes make a Char and double quotes make a String."
  - id: b
    text: "Int, Int, Char, String"
    why: "You may have thought 3.0 is the same as 3. The point makes it a Double."
  - id: c
    text: "Int, Double, String, String"
    why: "You may have thought quotes of any kind make text. Single quotes around one character make a Char."
output: "Int, Double, Char, String"
```

## Tricks to remember

- Count it: Int. Measure it: Double. On or off: Boolean.
- Single quotes hold one character. Double quotes hold text.
- A 3 in quotes is text, not a number.
- A price with cents does not fit an `Int` of whole units. The cents are cut off.

## Common mistakes

- **Thinking every number is a number and the type does not matter.** All of them look like digits, so one container seems enough. But `5 / 2` and `5.0 / 2` give different answers. Choose the container for the job.
- **Storing a price in an `Int` of whole units.** A price looks like a count of coins. Converting 19.99 to an `Int` leaves 19. A `Double` keeps the .99 here. General knowledge, not from the Kotlin pages: a `Double` holds many decimals only approximately, so real money code often keeps cents in a `Long`, or uses `BigDecimal`, a type made for exact decimal numbers.
- **Putting quotes around a number.** `"20"` looks like twenty. It is a `String`, a row of characters. My own reasoning: the type tells Kotlin what you may do with it, so arithmetic on text is not on offer.
- **Treating a digit character as a number.** `'7'` shows a digit. The documentation says `Char` is not a numeric type.

## Where this shows up in Android

The Android Tip Time codelab builds a screen with **Jetpack Compose**, the toolkit for building Android screens in Kotlin. The screen has a box where a person types the bill. The box gives the app its text, so the code holds it in a line like `var amountInput by remember { mutableStateOf("") }`. The words `remember` and `mutableStateOf` are Compose's way of keeping a value. The empty quotes mean the starting value is a `String`.

The codelab then writes `val amount = amountInput.toDoubleOrNull() ?: 0.0` to turn that text into a `Double`. For its tip box, the codelab says the result could be `null`, which means no number, if the text field is empty. The `?:` mark means "otherwise use": if there is no number, the code uses 0.0. My own reasoning: the typed 19.99 arrives as text, so the app cannot work out a tip until it is turned into a number.

## Say it in an interview

**What are the sizes and limits of `Int` and `Long`?**
`Int` takes 32 bits, the on-or-off switches a number is stored in, and holds -2,147,483,648 to 2,147,483,647. `Long` takes 64 bits and holds -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807. The Kotlin numbers page lists both. For a plain whole number, Kotlin picks `Int` if it fits and `Long` if it does not.

**Why is `5 / 2` equal to `2`?**
The documentation says division between integer values always returns an integer, and the compiler discards the fractional part. To keep the .5, write `5.0 / 2`, which is a `Double`.

**What happens when an `Int` goes past its maximum?**
General knowledge: it wraps around, so the largest `Int`, 2147483647, plus 1 gives -2147483648. My run of Kotlin 2.3.21 printed that. If a count can pass about two billion, use a `Long`. The trade-off is memory: a `Long` is 64 bits and an `Int` is 32.

**Should money be stored in a `Double`?**
General knowledge: usually not. A `Double` holds many decimals only approximately, and `0.1 + 0.2` prints `0.30000000000000004`. Real code often keeps cents in a `Long`, or uses `BigDecimal`, a type made for exact decimal numbers.

## Recap

1. Kotlin has a type for whole numbers, decimals, yes or no, one character and text.
2. The type decides what you may do with a value and whether a fraction survives.
3. `3`, `3.0`, `'3'` and `"3"` are four different types.

## You can now

- Predict the result of dividing whole numbers and of dividing a number with a point.
- Predict what is lost when a `Double` price is converted to an `Int`.
- Sort a value such as `3`, `3.0`, `'3'` or `"3"` into its type.
