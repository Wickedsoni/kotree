---
id: foundation/programming-thinking/yes-or-no-questions
title: How does a program ask a yes-or-no question?
status: shadow
difficulty: easy
order: 13
requires: [foundation/programming-thinking/expressions]
summary: A program asks a yes-or-no question by comparing two values. The answer is itself a value it can keep, print or combine.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 20 Oct 2026). Booleans: https://kotlinlang.org/docs/booleans.html ("The Boolean type represents logical values: true and false."; "You cannot assign an integer value to a Boolean variable. In Kotlin, 0 and 1 are not Boolean values."; AND "returns true only if both operands are true"; OR "returns true if at least one operand is true"; NOT "inverts a Boolean value"; "&& before ||", with true || false && false giving true; sample val isPositive = number > 0 // true). Comparison and equality operators: https://kotlinlang.org/docs/operator-overloading.html (a > b, a < b, a >= b, a <= b, a == b, a != b). Equality: https://kotlinlang.org/docs/equality.html (== and != are structural equality). Variables, val and var, reassignment with =: https://kotlinlang.org/docs/basic-syntax.html. Android: https://developer.android.com/develop/ui/compose/components/button (enabled: "When false, this parameter makes the button appear unavailable and inactive."); https://developer.android.com/develop/ui/compose/state (if statement on state hides the greeting when the name is empty); https://developer.android.com/develop/ui/compose/documentation ("Jetpack Compose is the modern toolkit for building Android UI"). No Kotlin compiler on the authoring machine: every output below is hand-traced, not run. Not from a doc page: the turnstile picture and the claim that asking never changes the value asked about (it follows from the comparison operators being separate from =). -->
## The question

A club has one rule on its door: guests must be 18 or older. A guard looks at each guest's card and says yes or no. The guard never says "maybe".

A program has to apply the same rule to thousands of ages, with no guard to ask. How does it ask "is this person 18 or older?" so that the answer is always a clear yes or no?

## Mental model

Think of a turnstile at a cinema door.

- The turnstile is the place where the question is asked.
- The sign on the turnstile is the question. It says "18 or older".
- The visitor's card holds a value, such as an age.
- The light above the turnstile is the answer. It shows green for yes or red for no. It never shows anything else.
- A second turnstile in a row is a second question.

The turnstile reads the card and hands it straight back. The card is the same as before.

**Where the analogy stops:** A real turnstile only answers and lets people through. In a program, the answer is a value of its own. You can keep it under a name, print it, or feed it into another question. A turnstile has no way to do that with its light.

## Build the logic

### Notice

Three visitors come to the turnstile. The sign says "18 or older".

- Mia shows a card with age 17. The light turns red.
- Raj shows a card with age 18. The light turns green.
- Lena shows a card with age 30. The light turns green.

Each time, the card goes back to the visitor with the same age on it.

### Guess

What can the light show? Here are three ideas.

- A. A number that says how far the age is from 18.
- B. One of exactly two answers: yes or no.
- C. A short phrase such as "nearly old enough".

Idea A feels useful, because a number holds more detail. But the sign asked a yes-or-no question, and a turnstile can open or stay shut, nothing in between. Idea C has the same problem. Idea B is the answer.

### Build

1. Take the value from the visitor's card.
2. Read the sign. It names a way to compare, such as more than, less than, at least, at most, the same as, or not the same as. It also names the value to compare with.
3. Compare the two. The light shows yes or no, and nothing in between.
4. Hand the card back unchanged. Asking a question never changes the thing you ask about.
5. Changing the age on the card is a different job from asking about it. Do not mix them up.
6. Two lights can be joined into one answer in three ways. Open only if both lights are green. Open if at least one light is green. Or flip one light, so green becomes red and red becomes green.

### Name it

A yes-or-no answer is a **Boolean**: a value that is either `true` or `false`. The Kotlin documentation says the `Boolean` type represents logical values `true` and `false`. It also says that 0 and 1 are not Boolean values.

A **comparison** asks a question about two values and gives back a Boolean. These are the six comparison symbols.

| Plain words | Symbol | Example | Answer |
|---|---|---|---|
| more than | `>` | `20 > 18` | true |
| less than | `<` | `17 < 18` | true |
| at least | `>=` | `18 >= 18` | true |
| at most | `<=` | `19 <= 18` | false |
| the same as | `==` | `18 == 18` | true |
| not the same as | `!=` | `17 != 18` | true |

Look at the difference between `=` and `==`. A single `=` gives a name a value, as in `age = 18`. A double `==` asks a question, as in `age == 18`. The first changes the card. The second leaves it alone.

Three symbols join or flip Boolean answers.

- **AND**, written `&&`, gives `true` only if both answers are `true`.
- **OR**, written `||`, gives `true` if at least one answer is `true`.
- **NOT**, written `!`, turns `true` into `false` and `false` into `true`.

Here is the full picture for AND and OR.

| Left | Right | AND | OR |
|---|---|---|---|
| true | true | true | true |
| true | false | false | true |
| false | true | false | true |
| false | false | false | false |

When you mix them, Kotlin works out `&&` before `||`. Use brackets so a reader does not have to remember that.

This program asks one comparison, keeps the answer under a name, and then uses it.

```kotlin
fun main() {
    val age = 17
    val hasTicket = true
    val oldEnough = age >= 18
    println(oldEnough)
    println(oldEnough && hasTicket)
    println(oldEnough || hasTicket)
    println(!oldEnough)
}
```

It prints `false`, `false`, `true` and `true`, one per line. Mia is 17, so `oldEnough` is `false`. AND needs both answers to be `true`, so it gives `false`. OR needs one, and `hasTicket` is `true`. NOT flips `false` into `true`.

## Predict the output

```challenge
type: predict
run: false
id: two-questions-joined
verified: not-run
code: |
  fun main() {
      println(3 > 2 && 2 > 5)
  }
options:
  - id: a
    text: "true"
    why: "You may have looked only at the first question. AND needs both lights green, and 2 > 5 is a red light."
  - id: b
    text: "false"
    correct: true
    why: "The first question says yes and the second says no. AND opens only when both lights are green, so the joined answer is no."
  - id: c
    text: "true and false, one for each question"
    why: "You may have expected one answer per question. The AND symbol joins the two lights into one answer, and println prints one value."
output: "false"
```

```challenge
type: predict
run: false
id: sign-says-18
verified: not-run
code: |
  The sign says: 18 or older.
  Someone else wrote this check.

  fun main() {
      val age = 17
      val canEnter = age > 16
      println(canEnter)
  }
options:
  - id: a
    text: "false, because 17 is under 18"
    why: "You may have read the sign instead of the code. The code asks for more than 16, not for 18 or older."
  - id: b
    text: "true, so the check lets Mia in by mistake"
    correct: true
    why: "17 is more than 16, so the light turns green. The sign wanted at least 18, so the check should compare with that."
  - id: c
    text: "The program stops with an error"
    why: "You may have expected a wrong check to break the program. A comparison always gives yes or no, so nothing stops."
output: "true"
```

```challenge
type: predict
run: false
id: or-and-not
verified: not-run
code: |
  fun main() {
      val hasTicket = true
      val isStaff = false
      println(hasTicket || isStaff)
      println(!hasTicket)
  }
options:
  - id: a
    text: "false, then false"
    why: "You may have expected OR to need both lights green. OR opens when at least one light is green."
  - id: b
    text: "true, then false"
    correct: true
    why: "One green light is enough for OR, so the first line is true. NOT flips the green ticket light to red, so the second line is false."
  - id: c
    text: "true, then true"
    why: "You may have thought NOT keeps the light as it is. NOT flips it, so green becomes red."
output: |
  true
  false
```

## Tricks to remember

- One question, one light, two answers: green or red.
- Asking leaves the card alone. A single equals sign changes it, a double equals sign asks.
- AND needs every light green. OR needs one.
- At least means the number itself counts. More than means it does not.

## Common mistakes

- **Writing one equals sign when you mean to ask.** In maths, one equals sign is both a statement and a question, so it feels the same. In Kotlin, `=` gives a name a value, and `==` asks. Say "is it the same as" aloud and reach for `==`.
- **Forgetting that more than leaves out the number itself.** The sign says "18 or older", and `>` feels close enough. With `>`, the age 18 gets a red light. Pick `>=` when the number itself should get in.
- **Reading OR as "one or the other, not both".** In daily talk, "or" often means one only. In a program, OR is also `true` when both are `true`, as the table shows. Read it as "at least one".

## Where this shows up in Android

You fill in every box on a sign-up screen, but the Send button stays greyed out. Or the opposite: the button turns on when only one box is filled.

The Android documentation for the button in **Jetpack Compose**, the modern toolkit for building Android UI, lists an `enabled` setting. When it is `false`, the button appears unavailable and inactive. That setting takes a yes-or-no answer. If the code uses OR where it needed AND, one filled box gives one green light, and the button turns on too early. If a comparison asks for more than where it needed at least, the guest who is exactly 18 stays red.

The Compose state page shows the same kind of question steering the screen. Its example hides a greeting while the name is empty. The program asks a yes-or-no question about the name, and the answer decides what appears.

## Recap

1. A yes-or-no question gives one of two answers, true or false, called a Boolean.
2. Comparison symbols ask the question, and a single equals sign sets a value instead.
3. AND needs both answers true, OR needs at least one, and NOT flips an answer.

## You can now

- Predict the answer when two comparisons are joined with AND, as in `3 > 2 && 2 > 5`.
- Spot which comparison lets a 17-year-old through a sign that says 18 or older.
- Predict what OR and NOT give for two yes-or-no answers.
