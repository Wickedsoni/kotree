---
id: kotlin/strings/text-to-numbers
title: How do I turn the text "42" into a number without crashing?
status: shadow
difficulty: medium
order: 24
requires: [kotlin/strings/strings-are-sequences, kotlin/null-safety/safe-call-and-elvis]
summary: Text from a keyboard is not a number yet. Two functions convert it, and only one of them survives bad input.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int-or-null.html ; https://developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip -->
## The question

A user types the letters abc into a box that asks for an age. One app closes on the spot. Another app shows "please type a number" and carries on. Both apps read the same text. What does the second app do that the first does not?

## Mental model

Think of a shop with a till and a cashier.

- The banknote is the text the person typed. A note may be real, such as "42", or fake, such as "abc".
- The till is the number your program wants to hold and calculate with.
- The cashier checks each note before it goes into the till. Two kinds of cashier work at this shop.
- The strict cashier presses the alarm when a note is fake. The whole shop stops.
- The polite cashier hands a fake note back with a slip that reads "no number here".
- The slip is not a note. It stands for no money at all.

A real note goes into the till and you get a number. A fake note ends in one of two ways, depending on which cashier you picked.

**Where the analogy stops:** A real cashier judges a note by its look and may accept a slightly crumpled one. The Kotlin check is exact: a space in the text is enough to refuse it. Also, a real shop decides for itself what to do with a refused note. In Kotlin, you write that decision in your code.

## Build the logic

### Notice

Three notes arrive: "42", "abc" and an empty note with nothing written on it. The till wants a whole number. The note "42" fits. The note "abc" has letters on it. The empty note has no digits at all.

### Guess

What should a cashier do with the note "abc"? Here are three ideas.

- A. Put a 0 in the till and say nothing.
- B. Press the alarm and stop the shop.
- C. Hand the note back with a slip that says "no number here".

Idea A hides the problem. Zero is a real amount, so nobody can tell a fake note from a customer who really typed 0. Ideas B and C are both real choices in Kotlin. Idea B suits a note your own program printed, where a fake would mean a bug. Idea C suits a note a person typed, where a fake is normal.

### Build

1. Look at the whole note. It may have an optional plus or minus at the front, then digits only.
2. Check that the number fits in the till. Some numbers are too big.
3. If the note passes, put the number in the till.
4. If it fails, the strict cashier presses the alarm and the program stops with an error.
5. If it fails, the polite cashier hands back the slip "no number here".
6. Whoever receives the slip must choose: use a stand-in number, or ask the person to type again.
7. A person can add spaces without meaning to. Remove the blank margins before the check.

### Name it

The function **`toInt()`** is the strict cashier. The Kotlin documentation says the text must consist of an optional leading `+` or `-` sign and decimal digits (`0-9`), and fit the `Int` range. Otherwise it throws a `NumberFormatException`. An **exception** is an error that stops the program at that line.

The function **`toIntOrNull()`** is the polite cashier. The documentation says it parses the text to an `Int` or returns `null` if the text is not a valid representation of an `Int`. The slip is **`null`**, the mark for no value. The result has the type `Int?`, which means a whole number or no value. Add `?:`, the **Elvis operator**, after it to say "otherwise use this": `typed.toIntOrNull() ?: -1`.

| Text | `toIntOrNull()` gives | `toInt()` gives |
|---|---|---|
| `"42"` | `42` | `42` |
| `"042"` | `42` | `42` |
| `"-42"` | `-42` | `-42` |
| `"-1a"` | `null` | stops with an error |
| `" 22 "` (with spaces) | `null` | stops with an error |
| `"1_00"` | `null` | stops with an error |
| `"2147483648"` (too big) | `null` | stops with an error |
| `"abc"` and `""` | `null` | stops with an error |

The documentation lists the first seven rows. The last row follows from its rule: letters are not digits, and an empty text has no digit at all. That reading is my own reasoning.

```kotlin
fun main() {
    val typed = "12a"
    val age = typed.toIntOrNull() ?: -1
    println(age)
    println("42".toInt() + 1)
    println("042".toIntOrNull())
    println(" 22 ".toIntOrNull())
}
```

It prints `-1`, `43`, `42` and `null`. The text "12a" fails the check, so the slip is replaced by -1. The strict cashier is fine with "42" and the program adds 1. The leading zero in "042" is accepted. The spaces in " 22 " make the slip.

This next program compiles, but the strict cashier presses the alarm at the second line.

```kotlin
fun main() {
    println("before")
    println("abc".toInt())
    println("after")
}
```

It prints `before` and then stops with a `NumberFormatException`. The word `after` never appears.

## Predict the output

```challenge
type: predict
run: false
id: slip-then-stand-in
verified: not-run
code: |
  fun main() {
      val a = "12a".toIntOrNull() ?: -1
      val b = "12".toIntOrNull() ?: -1
      println(a)
      println(b)
  }
options:
  - id: a
    text: "0, then 12"
    why: "You may have expected bad text to turn into 0. The polite cashier hands back a slip, and the ?: part replaces the slip with -1."
  - id: b
    text: "12, then 12"
    why: "You may have expected Kotlin to read the digits and ignore the letter. The whole note must be a number, and 12a is not."
  - id: c
    text: "-1, then 12"
    correct: true
    why: "The note 12a fails the check, so the slip is replaced by -1. The note 12 is real and goes into the till as 12."
output: |
  -1
  12
```

```challenge
type: predict
run: false
id: spaces-on-the-note
verified: not-run
code: |
  fun main() {
      val raw = " 42 "
      println(raw.toIntOrNull())
      println(raw.trim().toIntOrNull())
  }
options:
  - id: a
    text: "42, then 42"
    why: "You may have expected the spaces to be ignored. The documentation lists text with spaces as giving null."
  - id: b
    text: "null, then 42"
    correct: true
    why: "The note with spaces fails the exact check and gets a slip. After the blank margins are removed, the copy reads 42 and passes."
  - id: c
    text: "null, then null"
    why: "You may have thought trim only changes how the text looks. It hands back a new text without the margins, and that text is a clean 42."
output: |
  null
  42
```

```challenge
type: predict
run: false
id: empty-note-alarm
verified: not-run
code: |
  fun main() {
      println("start")
      println("".toInt())
      println("end")
  }
options:
  - id: a
    text: "start, then the program stops with an error"
    correct: true
    why: "The strict cashier gets a note with no digits and presses the alarm. The shop stops before it prints end."
  - id: b
    text: "start, 0, end"
    why: "You may have expected an empty note to count as zero. The strict cashier does not guess. It stops."
  - id: c
    text: "start, null, end"
    why: "You may have mixed up the two cashiers. Only the polite one hands back a slip. The strict one stops the shop."
output: |
  start
```

## Tricks to remember

- OrNull is the polite cashier. The plain name is the strict one.
- The slip says "no number here". It is not 0.
- Spaces make a fake note. Remove the margins first.
- `?:` means "otherwise use this".

## Common mistakes

- **Thinking `toInt()` gives 0 for bad text.** Zero feels like the natural "nothing" for a number, so this guess comes naturally. Kotlin stops the program with an error. Use `toIntOrNull()` when a person typed the text.
- **Forgetting spaces.** A person may type a space after a number and not see it. The exact check refuses it. Call `trim()` first.
- **Choosing 0 as the stand-in.** It feels safe because it is a number. But 0 can be a real answer, so you cannot tell a typed 0 from bad text. Choose a stand-in that cannot be a real answer, or ask the person to type again. This is my own reasoning.
- **Adding `!!` after `toIntOrNull()`.** It makes the slip go away on screen. If the note was fake, the program still stops. Handle the slip instead.

## Where this shows up in Android

The Android tip calculator lesson reads a number from a text box. Its step reads: define a `val` named `tipPercent` that converts the `tipInput` variable to a `Double`, use an Elvis operator and return `0` if the value is `null`. The page adds that this value could be `null` if the text field is empty. The line is `val tipPercent = tipInput.toDoubleOrNull() ?: 0.0`. A `Double` is a number with a decimal part, and `toDoubleOrNull()` is the same polite cashier for that kind of number.

This next part is our own reasoning: the box is empty when the screen first opens. The strict version would press the alarm before the person typed anything, and the app would close. The polite version lets the screen show a result for an empty box.

## Say it in an interview

**What is the difference between `toInt()` and `toIntOrNull()`?**
`toInt()` throws a `NumberFormatException` if the text is not a valid `Int`. `toIntOrNull()` returns `null` instead. Use `toIntOrNull()` for text a person typed, where bad input is normal. My own reasoning: use `toInt()` when bad text would mean a bug in your own code, because a loud stop shows the bug early.

**What text does `toIntOrNull()` accept?**
The documentation says the text must be an optional leading `+` or `-` and decimal digits, and fit the `Int` range. Its samples give `null` for `"2147483648"`, `"-1a"`, `"1_00"` and `" 22 "` with spaces. So call `trim()` first if spaces are possible.

**Why does Kotlin not return 0 for bad text?**
My own reasoning: 0 is a real number, so the caller could never tell bad text from a typed 0. Returning `null` makes the type `Int?`, and Kotlin then makes you deal with the missing case before you do any math.

**What does `?:` do in `text.toIntOrNull() ?: 0`, and what is the risk?**
It means "use the left side, and if it is `null`, use the right side". The risk is that the stand-in hides the problem. A screen may show a result for text that was never a number, so choose the stand-in on purpose or ask the person to type again.

## Recap

1. `toInt()` stops the program with an exception when the text is not a number.
2. `toIntOrNull()` gives back null instead, and `?:` supplies a stand-in.
3. The whole text must be digits, so a space around the number makes it fail.

## You can now

- Predict the result of `toIntOrNull()` followed by `?:` for good and bad text.
- Predict whether a `toInt()` call on a given text stops the program.
- Spot why text with spaces gives null and clean it first with `trim()`.
