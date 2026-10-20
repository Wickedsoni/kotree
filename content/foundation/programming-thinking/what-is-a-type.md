---
id: foundation/programming-thinking/what-is-a-type
title: Why can't I add a word to a number?
status: live
difficulty: easy
order: 11
requires: [foundation/programming-thinking/what-is-a-variable, foundation/how-computers-work/how-letters-become-numbers]
summary: Every value has a kind, such as whole number or text. The kind decides what a plus sign does with it.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []
playground: false
fruit: false
upnext: false
---

## The question

Ask a program to add the number 5 and the number 5, and it answers 10. Put quote marks around the first 5 and ask again. Now it answers 55.

Ask it to add the number 5 first and then the word "five", and it will not run at all. Why do three nearly identical questions get three such different results?

## Mental model

Think of two kinds of container: an egg box and a milk bottle.

- An egg box holds eggs. Eggs are whole numbers. You can count eggs, and you can add two boxes together to get a bigger count.
- A milk bottle holds milk. Milk is text. You can pour two bottles into one, but you cannot count milk.
- The label on each container says which kind it is. The kind tells you which actions make sense.
- A bottle with the digit 5 written on it is still a bottle. It is not five eggs.

Five eggs plus five eggs is ten eggs. A bottle labelled "5" poured into another bottle labelled "5" gives a bottle labelled "55". Eggs and milk do not mix, so a request to add eggs to milk is refused.

**Where the analogy stops:** In a kitchen, order does not matter when you mix things. In Kotlin, it does. A bottle on the left will take a number on its right and write the number down as text. An egg box on the left refuses a bottle on its right. Also, Kotlin checks the labels before the program starts, which a kitchen never does.

## Build the logic

### Notice

Look at three requests to the computer.

- Five eggs plus five eggs.
- A bottle marked "5" plus a bottle marked "5".
- Five eggs plus a bottle marked "five".

The first gives ten eggs. The second pours the two together and gives a bottle marked "55". The third mixes eggs and milk.

### Guess

What should the computer do with the third request? Here are three ideas.

- A. Read "five" as the number 5 and give ten eggs.
- B. Refuse, because eggs and milk do not mix.
- C. Write the 5 down as text and pour it into the bottle's word, giving a bottle marked "five5".

Idea A is believable, because a person would read "five" as 5. But the computer sees a bottle, not a count. Idea C is also believable, and Kotlin does this, but only when the bottle comes first. Here the eggs come first, so idea B is the answer. Kotlin decides by which container is on the left.

### Build

1. Every value has a kind: a whole number, a number with a decimal point, or text.
2. The kind decides which actions make sense. Eggs can be counted and added. Bottles can be poured together.
3. Inside the computer, everything is stored as numbers. A label is needed to say whether a stored item is a count or a piece of text.
4. When you ask for a plus, the computer looks at the container on the left to decide what plus means.
5. If the left container is a number, plus means add, and the right container must also be a number.
6. If the left container is text, plus means pour together. The right side, even a number, is written down as text first.
7. Quote marks mark a bottle. So "5" is a bottle, and 5 is five eggs.
8. These checks happen before the program starts. A mismatch stops the program from starting at all.

### Name it

The kind of a value is its **type**. The Kotlin documentation says every variable has a type, and that types tell the compiler what you are allowed to do with the variable.

The **compiler** is the tool that checks your code and turns it into something the computer can run, before the program starts. When it finds a mismatch, it refuses to run the program.

Three types cover this lesson.

| Type | What it holds | Example |
|---|---|---|
| `Int` | Whole numbers | `5` |
| `Double` | Numbers with a decimal point | `2.5` |
| `String` | Text, between double quotes | `"5"` |

Kotlin works out the type from the value. The Kotlin documentation calls this **type inference**. The documentation says a number written with no decimal point is an `Int`, and a number written with one is a `Double`. A value in double quotes is a `String`. So `5` is a number and `"5"` is text.

The `+` sign means different things for different types. The Kotlin reference lists what a whole number's plus accepts. It accepts numbers only, and none of them is text. Text is different. Its plus joins the text with the text form of whatever comes next. The documentation's own sample prints `Kotlin 2` for `"Kotlin " + 2`.

```kotlin
fun main() {
    val count = 5
    val label = "5"
    println(count + 5)
    println(label + 5)
}
```

This prints `10`, then `55`. The first line adds eggs. The second pours the number 5 into the bottle as text.

Kotlin refuses to run the next program. The egg box is on the left, and a bottle is on the right.

```kotlin
// does not compile
fun main() {
    val count = 5
    println(count + "five")
}
```

## Predict the output

```challenge
type: predict
run: false
id: five-in-quotes-plus-five
verified: not-run
code: |
  fun main() {
      println("5" + 5)
  }
options:
  - id: a
    text: "10"
    why: "You may have read the digit in the quote marks as a number. Quote marks make a bottle, and a bottle on the left means pour, not add."
  - id: b
    text: "55"
    correct: true
    why: "The bottle comes first, so plus means pour together. The number 5 is written down as text and added to the end."
  - id: c
    text: "The program refuses to run"
    why: "You may have thought text and a number never mix. A bottle on the left does take a number on its right, written down as text."
output: "55"
```

```challenge
type: predict
run: false
id: two-eggs-two-bottles
verified: not-run
code: |
  fun main() {
      val a = 2
      val b = "3"
      println(a + a)
      println(b + b)
  }
options:
  - id: a
    text: "4, then 6"
    why: "You may have read the 3 in quote marks as a number. It is a bottle, so two of them pour together instead of adding."
  - id: b
    text: "4, then 33"
    correct: true
    why: "Two egg boxes add to 4. Two bottles marked 3 pour into one bottle marked 33."
  - id: c
    text: "22, then 33"
    why: "You may have thought plus always pours. Plus pours only when the left side is text. With numbers on both sides it adds."
output: |
  4
  33
```

```challenge
type: predict
run: false
id: which-one-is-refused
verified: not-run
code: |
  Three programs. Each has one println line inside main.
  A:  println("age: " + 12)
  B:  println(12 + "age")
  C:  println("12" + "age")
  Which program does Kotlin refuse to run?
options:
  - id: a
    text: "Program A"
    why: "You may have thought a number cannot go into text. A bottle on the left takes it, written down as text, so A prints age: 12."
  - id: b
    text: "Program B"
    correct: true
    why: "An egg box is on the left and a bottle is on the right. Number plus text is the refused order."
  - id: c
    text: "Program C"
    why: "You may have seen digits and text together. Both sides are bottles, so they pour together and C prints 12age."
output: "Program B"
```

## Tricks to remember

- Quote marks make a bottle.
- Eggs add. Bottles pour.
- The left side decides what plus means.
- The kinds are checked before the program starts.

## Common mistakes

- **Treating "5" and 5 as the same.** They look alike on screen, so they feel alike. Look for the quote marks. With quote marks it is text, and without them it is a number.
- **Thinking text and a number are refused in both orders.** The lesson title makes this feel right. Remember the order. Text on the left pours the number in as text. A number on the left refuses text.
- **Expecting the computer to read the digits and do the sum.** A person sees "3" and thinks of three. The computer sees a bottle with a mark on it. To get a sum, you need the number itself, not a bottle that shows its digit.

## Where this shows up in Android

Picture a shopping app with a box where you type how many items you want. The Android documentation's text-field example keeps what the user types in a variable that starts as empty text, and shows it back inside a greeting.

Suppose an app keeps the typed quantities as text. Then a cart total can show 33 instead of 6 when it adds two quantities of 3. The plus pours them together instead of adding. That is the bottle on the left, from this lesson.

## Recap

1. Every value has a type, such as `Int` for whole numbers and `String` for text.
2. Quote marks make text, so `"5"` and `5` are different things.
3. Plus adds numbers and joins text, and a number on the left refuses text.

## You can now

- Predict what a plus gives for numbers, for text, and for text followed by a number.
- Explain why `"5"` and `5` are different, using the bottle and the egg box.
- Spot which of several plus lines Kotlin will refuse to run.
