---
id: kotlin/strings/everyday-string-functions
title: Which String functions do I need every day?
status: shadow
difficulty: easy
order: 23
requires: [kotlin/strings/strings-are-sequences, kotlin/functions/declaring-functions]
summary: String functions such as trim and uppercase hand back a new String and leave the original alone. Meet the everyday ones.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/strings.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/trim.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/replace.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/split.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/contains.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/starts-with.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/is-blank.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/substring.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/-string-builder/ ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/build-string.html ; https://developer.android.com/kotlin/common-patterns -->
## The question

A user types the name " Sam " into a form, with a space before and after. Your code runs a clean-up step to remove the spaces, then prints the name. The spaces are still there. Why did the clean-up seem to do nothing?

## Mental model

Think of a photocopier in an office.

- The original page is the text stored under a name.
- The photocopier is a string function.
- The job button says what to do, such as "remove blank margins" or "make everything capitals".
- The copy that slides into the tray is the result.
- A folder with a label is a name that keeps the copy.

You put the original in, press a button and take the copy from the tray. The original page is back on the glass, exactly as it was. If you leave the copy in the tray and walk away, nobody has it.

**Where the analogy stops:** A real page can be scribbled on with a pen. A Kotlin `String` cannot be changed at all, so a copy is the only way to get different text. Also, not every button prints a page. Some buttons hand back a yes or no, or a stack of pieces.

## Build the logic

### Notice

The original page says " Sam ", with a blank margin on each side. An office worker presses "remove blank margins". A copy reading "Sam" slides out. The original still reads " Sam ". The worker files the copy in a folder labelled "tidy". Later the worker reads the "tidy" folder and finds "Sam". Reading the original again finds the margins.

### Guess

After the copier runs, what does the original page look like? Here are three ideas.

- A. It is tidied too, because the copier worked on it.
- B. It is exactly as it was.
- C. It is thrown away and only the copy exists.

Idea A feels right because the button was pressed on that page. Idea C feels right because the copy is what you wanted. Idea B is the answer.

### Build

1. Put the original page in the copier.
2. Press one job button, such as remove the margins, make capitals, swap a word, or cut at a mark.
3. The copier hands back something new: a copy of the page, a yes or no, or a stack of pieces.
4. The original is untouched.
5. To keep what came out, put it in a labelled folder.
6. To do two jobs, put the first copy into the copier again and press the next button.

### Name it

The Kotlin documentation says the **`String`** type is **immutable**: after you create a string, its contents stay the same, and any operation that appears to modify the string actually creates a new string. So every string function hands back a new value, and the original keeps its text. Keep the result with `val`, or use it straight away.

**Whitespace** means blank characters such as spaces, tabs and line breaks. This table lists the everyday functions.

| Job on the copier | Kotlin | What comes out |
|---|---|---|
| Remove blank margins | `trim()` | A new `String` |
| All capitals, or all small | `uppercase()`, `lowercase()` | A new `String` |
| Swap every copy of a phrase | `replace("old", "new")` | A new `String` |
| Cut at a mark | `split(",")` | A **list** of strings, which is a row of values numbered from 0 |
| Does it hold a phrase? | `contains("am")` | `true` or `false` |
| Does it start with a phrase? | `startsWith("Dr")` | `true` or `false` |
| Is it empty or only blanks? | `isBlank()` | `true` or `false` |
| Cut out a slice | `substring(0, 3)` | A new `String` |

The documentation says `trim()` returns a string with leading and trailing whitespace removed. That means the two ends only. It says `replace` replaces all occurrences. By default, `contains` and `startsWith` treat a capital letter and a small letter as different. For `substring(start, end)`, the start index is included and the end index is left out.

```kotlin
fun main() {
    val name = "  Sam  "
    val tidy = name.trim()
    println("[$name]")
    println("[$tidy]")
    println(tidy.uppercase())
    println(tidy.replace("a", "o"))
    println(tidy.contains("am"))
}
```

It prints `[  Sam  ]`, `[Sam]`, `SAM`, `Som` and `true`. The variable `name` keeps its margins to the end. The variable `tidy` holds the copy. The last three lines use the copy without changing it.

## Predict the output

```challenge
type: predict
run: false
id: copy-not-filed
verified: not-run
code: |
  fun main() {
      val s = "hello"
      s.uppercase()
      println(s)
  }
options:
  - id: a
    text: "hello"
    correct: true
    why: "The copier printed a capital copy, but nobody filed it. The original page was never touched, so s still reads hello."
  - id: b
    text: "HELLO"
    why: "You may have expected the button to change the original page. It only makes a copy."
  - id: c
    text: "The program does not compile"
    why: "You may have expected Kotlin to refuse a copy nobody files. It lets the copy fall in the bin, and the original stays as it was."
output: |
  hello
```

```challenge
type: predict
run: false
id: trim-then-split
verified: not-run
code: |
  fun main() {
      val line = " red,green ,blue "
      val parts = line.trim().split(",")
      println(parts.size)
      println("[${parts[1]}]")
  }
options:
  - id: a
    text: "3, then [green]"
    why: "You may have expected the clean-up to tidy every piece. It removes margins from the two ends of the whole line only, so the space after green stays."
  - id: b
    text: "3, then [green ]"
    correct: true
    why: "The first copy has its end margins removed. The second copy is cut at each comma into three pieces. The piece with number 1 is the second one, green with its trailing space."
  - id: c
    text: "3, then [red]"
    why: "You may have counted pieces from 1. The pieces are numbered from 0, so number 1 is the second piece."
output: |
  3
  [green ]
```

```challenge
type: predict
run: false
id: replace-every-copy
verified: not-run
code: |
  fun main() {
      val road = "a-b-c"
      val fixed = road.replace("-", "+")
      println(fixed)
      println(road)
  }
options:
  - id: a
    text: "a+b+c, then a+b+c"
    why: "You may have expected the original to change as well. The copier leaves the original page as it was."
  - id: b
    text: "a+b-c, then a-b-c"
    why: "You may have expected only one swap. The documentation says replace swaps all occurrences."
  - id: c
    text: "a+b+c, then a-b-c"
    correct: true
    why: "Every dash on the copy is swapped for a plus. The copy goes into the folder called fixed, and the original page keeps its dashes."
output: |
  a+b+c
  a-b-c
```

## Tricks to remember

- Original in, copy out, original unchanged.
- No folder, no copy: keep the result with `val`.
- `trim()` cleans the two ends only.
- Two jobs? Put the copy back in the copier.

## Common mistakes

- **Calling a function and ignoring what it hands back.** A call that tidies something sounds like it tidies that very thing, so a bare `name.trim()` feels finished. In Kotlin the tidy copy is lost. Write `val tidy = name.trim()` and use `tidy`.
- **Expecting `trim()` to remove spaces inside the text.** The word "clean" sounds like it should do everything. `trim()` removes whitespace at the start and end only. To change spaces inside, use `replace`.
- **Expecting `split` on an empty string to give an empty list.** Cutting nothing sounds like it gives no pieces. The documentation says a text with no mark in it gives a list with one entry, the whole text. For `""` that entry is an empty text.

## Where this shows up in Android

The Android page on common Kotlin patterns says: if you want to trim `name` so that its value does not include leading or trailing whitespace, use Kotlin's `trim` function. Its example is `val accountName = account.name?.trim()`. The `?.` part is a dot that skips the call when the name is missing, and this lesson leaves it aside. The rest is the photocopier: the trimmed copy is kept in `accountName`.

This next part is our own reasoning: if a sign-in screen saved the email as typed, a space at the end would be saved with it. A later check would then find a different email and refuse the person. Trimming a copy first and saving that copy avoids it.

## Say it in an interview

**What does it mean that a `String` is immutable, and what is the cost?**
The documentation says that after you create a `String`, its contents stay the same, and any operation that appears to modify it creates a new string. General knowledge: that makes a `String` safe to share between parts of a program. The cost is that every change makes a new string. The documentation says `+` creates a new `String` for each operation, which may not be beneficial in loops or when you assemble many pieces.

**How do you build a long string from many pieces?**
Use `StringBuilder` or `buildString()`. The documentation describes `StringBuilder` as a mutable sequence of characters for efficient string operations, and says `buildString()` creates a `StringBuilder` internally and returns the resulting string. The trade-off: for a few pieces, string templates are usually easier to read and more idiomatic, so use the builder when you assemble many pieces.

**Does `trim()` change the string it is called on? What does it remove?**
No. It returns a new string with leading and trailing whitespace removed, and the original stays as it was. It does not touch whitespace inside the text.

**What does `split` return for a text with no delimiter?**
The documentation says the list has one element, the whole text. If the text ends with a delimiter, the list ends with an empty string. So `"".split(",")` gives a list with one empty string, not an empty list.

## Recap

1. Kotlin strings never change, so a string function hands back a new value.
2. The original keeps its text, so keep the result in a name.
3. `trim()` cleans the two ends, and `replace` swaps every match.

## You can now

- Predict what a name holds after a string function is called and the result is dropped.
- Predict the pieces and the spaces left after `trim()` and `split`.
- Predict what `replace` gives for a text with several matches.
