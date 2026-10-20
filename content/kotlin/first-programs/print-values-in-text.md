---
id: kotlin/first-programs/print-values-in-text
title: How do I put a value inside a sentence I print?
status: shadow
difficulty: easy
order: 2
requires: [kotlin/first-programs/where-a-program-starts]
summary: Write the sentence once with a marked blank for each value, and Kotlin fills the blanks when it prints.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/strings.html (string templates embed variables and expressions in a String literal; $ for a variable; ${} for an expression or for a variable next to other text; the + operator also combines strings but templates are usually easier to read and more idiomatic; "${language}Lang" prints KotlinLang); kotlinlang.org/docs/basic-syntax.html and kotlin-tour-hello-world.html (val; template expressions start with $); developer.android.com/develop/ui/compose/tutorial (Text(text = "Hello $name!") in MessageCard). Own reasoning: $name takes only the name, so .length stays plain text (derived from the strings page rule for ${}); what the screen shows without the $ in the Android section. Outputs hand-traced, not compiled. -->
## The question

A sign-up page knows two facts about a person: the name Sam and the age 20. It must print "Hi, Sam, you are 20". Tomorrow the person is Ravi, who is 31, and the sentence must change by itself. How do you write one sentence that takes its values from somewhere else?

## Mental model

Think of a form letter with blanks.

- The form letter is the sentence you want to print: "Hi, ___, you are ___".
- A blank is a marked spot in the letter where a value will go.
- A card holds one fact. The name card says Sam. The age card says 20.
- Each card has a label on top, such as "name" or "age".
- The clerk is the computer. It looks at each blank, finds the card with that label, copies the fact in and prints the finished letter.

The letter is written once. When the cards change, the same letter gives a new sentence.

**Where the analogy stops:** A real clerk fills a letter once and posts it. A program fills the blanks every time it runs, with whatever the cards hold at that moment. Also, a paper blank can hold only a card's fact. A Kotlin blank can also hold a small calculation, such as a sum.

## Build the logic

### Notice

Two cards sit on the desk: name says Sam, age says 20. The letter reads "Hi, ___, you are ___". The clerk fills the first blank from the name card and the second from the age card. The printed sentence is "Hi, Sam, you are 20".

### Guess

How should the program build that sentence? Here are three ideas.

- A. Cut the sentence into pieces and glue the pieces together with a plus sign every time.
- B. Type Sam and 20 straight into the sentence.
- C. Write the sentence once, with a marked blank where each value goes, and name the card for each blank.

Idea B works for Sam and breaks the moment the person is Ravi. Idea A can work. The Kotlin documentation says you can combine strings with the plus sign, but it says templates are usually easier to read and more idiomatic. Idea C is the answer.

### Build

1. Write the sentence once, with a marked blank where each value goes.
2. Put each fact on a card with a label.
3. In each blank, write the label of the card it should use.
4. If a blank needs a small calculation, put the calculation inside a bigger mark, so the clerk knows where it ends.
5. If a label touches other letters, use the bigger mark too, so the clerk knows where the label ends.
6. The clerk fills every blank and prints the finished sentence.

### Name it

A card with a label is a **variable**: a name that holds a value. You make one with the keyword **`val`**, then the name, then `=`, then the value. The line `val name = "Sam"` makes a variable called `name` that holds the text Sam.

Text in double quotes is a **string**. A number such as `20` has no quotes.

The sentence with blanks is a **string template**. The documentation says string templates let you embed variables and expressions directly inside a string. A dollar sign `$` followed by a name marks a blank. The bigger mark is `${ }`: put a calculation inside the braces, or put a name inside them when it touches other letters.

```kotlin
fun main() {
    val name = "Sam"
    val age = 20
    val a = 3
    val b = 4
    val language = "Kotlin"
    println("Hi, $name, you are $age")
    println("Total: ${a + b}")
    println("${language}Lang")
}
```

It prints three lines: `Hi, Sam, you are 20`, then `Total: 7`, then `KotlinLang`. The first line uses plain `$` marks. The second does a sum inside the blank. The third needs the braces, because `Lang` touches the name.

The documentation says to use `${ }` when you insert an expression. My own reasoning: this means `$name` takes only the name. Anything after the name, such as `.length`, stays plain text.

## Predict the output

```challenge
type: predict
run: false
id: total-with-braces
verified: not-run
code: |
  fun main() {
      val a = 6
      val b = 2
      println("Total: ${a + b}")
  }
options:
  - id: a
    text: "Total: a + b"
    why: "You may have thought the letters stay as written. The marks tell the clerk to work out what is inside, so the sum is filled in."
  - id: b
    text: "Total: 8"
    correct: true
    why: "The bigger mark holds a calculation. The clerk works out 6 + 2 and writes 8 in the blank."
  - id: c
    text: "Total: 62"
    why: "You may have thought the two cards are glued side by side. The braces hold a sum, so the clerk adds them first."
output: "Total: 8"
```

```challenge
type: predict
run: false
id: label-touches-dot
verified: not-run
code: |
  fun main() {
      val name = "Sam"
      println("Name: $name.length")
  }
options:
  - id: a
    text: "Name: 3"
    why: "You may have expected the length of Sam. Only the bigger mark works out a calculation, and this blank uses the small mark."
  - id: b
    text: "Name: name.length"
    why: "You may have thought the clerk skips this blank. The small mark still fills in the card, so the word Sam appears."
  - id: c
    text: "Name: Sam.length"
    correct: true
    why: "The small mark takes only the label name, so the clerk writes Sam. The .length that follows is ordinary letters in the letter."
output: "Name: Sam.length"
```

```challenge
type: predict
run: false
id: braces-keep-the-name-apart
verified: not-run
code: |
  fun main() {
      val language = "Kotlin"
      println("${language}Lang")
      println("$language Lang")
  }
options:
  - id: a
    text: "KotlinLang, then Kotlin Lang"
    correct: true
    why: "The bigger mark shows where the label ends, so Lang sits right against the fact. In the second line the space is part of the letter."
  - id: b
    text: "KotlinLang, then KotlinLang"
    why: "You may have thought spaces vanish. A space inside the letter stays, so the second line has a gap."
  - id: c
    text: "Kotlin, then Kotlin"
    why: "You may have expected the word Lang to be dropped. It is ordinary text in the letter, so it is printed."
output: |
  KotlinLang
  Kotlin Lang
```

## Tricks to remember

- Write the letter once, fill the blanks many times.
- `$name` takes the name and nothing more.
- Use `${ }` when you need a sum, or when letters touch the name.
- A card changes, and the same letter prints a new sentence.

## Common mistakes

- **Gluing text and numbers with a plus every time.** It feels right because it works. The Kotlin documentation says templates are usually easier to read and more idiomatic, so reach for `$` first.
- **Forgetting the dollar sign.** The quotes make everything inside look like plain text, and it is. Without `$`, the word name itself prints, not the value. Check that each blank starts with `$`.
- **Expecting `$name.length` to give a length.** The dot looks like part of the blank. The small mark stops at the name. Put the calculation in `${name.length}`.

## Where this shows up in Android

**Jetpack Compose** is the toolkit for building Android screens in Kotlin. The Android Compose tutorial has a function called `MessageCard` that is passed a name. Its body is `Text(text = "Hello $name!")`. A **`Text`** call puts a piece of text on the screen.

That line is a string template. For Sam the screen shows `Hello Sam!`. For Ravi the same line shows `Hello Ravi!`. My own reasoning: if you forget the `$`, the screen shows `Hello name!`, because the letters are then just text.

## Recap

1. A string template is a sentence with marked blanks that Kotlin fills in when it prints.
2. A dollar sign and a name fill one blank, and `${ }` holds a calculation.
3. The small mark stops at the name, so `$name.length` prints the name and then `.length`.

## You can now

- Predict what a `${ }` blank with a sum prints.
- Spot why `$name.length` does not print a length.
- Predict when a `${ }` mark is needed because letters touch the name.
