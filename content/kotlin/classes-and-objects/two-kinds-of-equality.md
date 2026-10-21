---
id: kotlin/classes-and-objects/two-kinds-of-equality
title: When are two objects equal in Kotlin?
status: live
difficulty: medium
order: 42
requires: [kotlin/classes-and-objects/objects-are-references]
summary: Two objects can look the same and still be two objects. See the two questions Kotlin can ask, and which one a plain class answers.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [twins-or-one-person]
playground: false
fruit: false
upnext: false
---

## The question

Two shoppers each hand a cashier a loyalty card with the number 7 on it. Are these the same card, or two cards that look alike? A program that compares two objects meets the same puzzle. When it asks whether two objects are equal, what is it really asking?

## Mental model

Think of identical twins in a crowd, with name tags pinned on them.

- A person is one object.
- A name tag is a variable. It points at one person. Two tags can point at the same person.
- The question "is this the same person?" asks whether two tags lead to one person.
- The question "are these two alike?" asks whether two people match in the details that matter.
- A registry clerk answers the "alike" question. The clerk follows a rule. Each kind of person can come with its own rule.
- If a kind of person comes with no rule, the clerk uses the plain one: alike means the same person.

**Where the analogy stops:** A real clerk can look at two people and judge. In code, nobody judges by sight. The "alike" rule is only what a programmer wrote down, and it can be a poor rule. Also, real twins can never be identical in every detail. Two objects can.

## See it move

<!-- SIM: twins-or-one-person -->

**Try this:**
1. Pin tag y onto the same person as tag x. Watch the two lights, "Same person?" and "Alike?".
2. Pin tag y onto the twin instead. Watch the lights again.
3. Change the twin's hair colour and look at the "Alike?" light.
4. (Optional reveal) Tick "Give this kind of person its own rule" and repeat steps 2 and 3.

**What you should notice:** With the plain rule the two lights always agree, and only after the kind of person gets its own rule can "Alike?" be on while "Same person?" is off.

## Build the logic

### Notice

Ana and Ben are twins. Both are 160 cm tall with black hair. Three tags are pinned: tag x on Ana, tag y on Ana, and tag z on Ben. Tag x and tag y lead to one person. Tag x and tag z lead to two people who look the same.

Now someone asks, "Are x and z the same?" The answer depends on the question. They are two people, so they are not the same person. They match in height and hair, so they are alike.

### Guess

A program is asked whether tag x and tag z are equal. The clerk has no special rule for this kind of person. What does the clerk answer? Here are three ideas.

- A. The clerk compares every detail, finds a match, and says yes.
- B. The clerk uses the plain rule, asks whether it is the same person, and says no.
- C. The clerk always says no, even for tag x against tag y.

Idea A feels natural, because that is how people judge twins. Idea C forgets that tag x and tag y lead to one person, who is certainly the same as themselves. Idea B is the answer. The clerk only compares details when the kind of person comes with a rule that says which details count.

### Build

1. Keep two separate questions: "same person?" and "alike?".
2. Answer "same person?" by checking whether the two tags lead to one person. No details are needed.
3. Answer "alike?" by asking the clerk.
4. If the kind of person has no rule, the clerk falls back on step 2.
5. If the kind of person has a rule, the clerk follows it. The rule says which details are compared.
6. If the left tag points at nobody, the clerk cannot be asked. The left side is checked first, and then it is asked whether the right tag also points at nobody.

### Name it

The "same person?" question is **referential equality**, written `===`. It is true when two variables point at one object. Its opposite is `!==`. The Kotlin documentation says it checks "the memory addresses of two objects".

The "alike?" question is **structural equality**, written `==`. Its opposite is `!=`. The documentation says `a == b` is translated to `a?.equals(b) ?: (b === null)`. In words, that is step 6: if `a` is not null, ask `a`'s **equals** function, which is the clerk. If `a` is null, check that `b` is null too.

The documentation says every class inherits (gets automatically from a parent) `equals` from `Any` (the built-in top class that all Kotlin classes extend), and by default it implements referential equality. It also says a class that is not a data class does not change this unless its author writes a rule. Two special kinds of class, which the page calls data classes and value classes, get a rule written for them automatically. How you write your own rule is not covered here, because it needs the word `override`.

This program uses a plain class with no rule.

```kotlin
class Card(val code: Int)

fun main() {
    val a = Card(7)
    val b = Card(7)
    val c = a
    println(a == b)
    println(a === b)
    println(a == c)
    println(a === c)
}
```

It prints `false`, `false`, `true` and `true`. Cards `a` and `b` are two objects with the same number, so they are not the same person and the plain rule says they are not alike. Tag `c` was pinned on the same person as `a`, so both questions say yes.

Text works differently, because `String` comes with its own rule (general knowledge: it compares the letters, in order). The documentation's example prints `true` for two texts both written as `"hello"`.

```kotlin
fun main() {
    val a = "hello"
    val b = "hello"
    val c = "world"
    println(a == b)
    println(a == c)
}
```

It prints `true` and then `false`. Use `==` to compare text. The documentation's own text example uses `==` for this job.

## Predict the output

```challenge
type: predict
run: false
id: ada-written-twice
verified: kotlin-2.4.21
code: |
  fun main() {
      val first = "Ada"
      val second = "Ada"
      val third = first
      println(first == second)
      println(first === third)
      println(second != third)
  }
options:
  - id: a
    text: "true, true, false"
    correct: true
    why: "Text comes with its own rule, so the two Adas are alike. Tag third was pinned on the same person as first. Second and third are alike, so != says false."
  - id: b
    text: "false, true, false"
    why: "You may have thought two lines of text mean two different people, and that == asks about sameness. For text, == asks whether the letters match."
  - id: c
    text: "true, false, true"
    why: "You may have read != as a second kind of sameness. It is only the opposite of ==, and second and third are alike, so it says false."
output: |
  true
  true
  false
```

```challenge
type: predict
run: false
id: change-shows-through-the-tag
verified: kotlin-2.4.21
code: |
  class Mug(var size: Int)

  fun main() {
      val x = Mug(3)
      val y = Mug(3)
      val z = y
      z.size = 5
      println(y.size)
      println(x == y)
      println(y === z)
  }
options:
  - id: a
    text: "3, true, true"
    why: "You may have thought z is a copy. Tag z is another tag on y's person, so the change shows through y."
  - id: b
    text: "5, true, true"
    why: "You may have thought == compares the sizes. This kind of person has no rule, and x is a different person, so the plain rule says no."
  - id: c
    text: "5, false, true"
    correct: true
    why: "Tags y and z lead to one person, so the size shows 5 through y and y === z is true. Person x is a separate person and the plain rule asks only about sameness."
output: |
  5
  false
  true
```

```challenge
type: predict
run: false
id: two-seats-same-numbers
verified: kotlin-2.4.21
code: |
  class Seat(val row: Int, val number: Int)

  fun main() {
      val a = Seat(1, 4)
      val b = Seat(1, 4)
      println(a == b)
      println(a != b)
      println(a == a)
  }
options:
  - id: a
    text: "true, false, true"
    why: "You may have thought two seats with the same row and number are alike. Nobody gave this kind of person a rule, so the clerk uses the plain one."
  - id: b
    text: "false, true, true"
    correct: true
    why: "Seats a and b are two people, and the plain rule says only the same person is alike. A person is the same as themselves, so a against a says true."
  - id: c
    text: "false, true, false"
    why: "You may have thought a person is not alike to themselves. Tag a against tag a leads to one person, so the answer is yes."
output: |
  false
  true
  true
```

## Tricks to remember

- Two questions, two tags: "same person?" is `===`, "alike?" is `==`.
- No rule written means alike is the same person.
- Text has a rule, so compare text with `==`.
- Try it on your own machine: make two plain-class objects with the same field values, compare them with `==` and `===`, then pin a third tag on the first and compare again.

## Common mistakes

- **Expecting `==` to compare the fields of every class.** It works that way for text and feels like a general rule. For a plain class, the clerk uses the plain rule unless the author wrote another. Check whether the class has a rule before you trust `==`.
- **Using `===` to compare text or other values.** It looks stricter and so safer. It asks about sameness, which is a different question. Use `==` when you mean "alike".
- **Thinking `val b = a` made a second person.** Giving a second tag a name feels like making a copy. It pins another tag on the same person, so a change through one tag shows through the other.

## Where this shows up in Android

A **composable** is a function that describes one part of the screen. **Recomposition** is Compose running a composable again because its inputs may have changed. A row on a screen can redraw although nothing on it changed.

The Compose stability page explains one cause. It says Compose compares **stable parameters** (inputs Compose knows cannot change behind its back) using structural equality, and skips the composable if they are equal to the previous values. For **unstable parameters** (inputs Compose cannot make that promise about), with **strong skipping mode** on (a compiler setting that is the default from Kotlin 2.0.20), it compares with `===` and skips only if the same object is passed. The page's example has a `Contact` class with `var` properties. It says passing a newly created `Contact` with identical values still makes `ContactDetails` recompose. That is the twin case in this lesson: two objects that are alike but not the same person, compared by sameness.

## Say it in an interview

**What is the difference between `==` and `===` in Kotlin?**
`==` is structural equality. It asks whether two objects are equal by their `equals` function. `===` is referential equality. It is true only when both variables point at the same object. A good example is two separate objects with the same field values: `===` says no, and `==` says whatever `equals` says.

**How does Kotlin turn `a == b` into a call to `equals`?**
The documentation says `a == b` is translated to `a?.equals(b) ?: (b === null)`. If `a` is not null, Kotlin calls `equals`. If `a` is null, it checks that `b` is null as well. On the JVM this is the same `equals` that Java code uses (general knowledge).

**Why is the translation written with a null check?**
My own reasoning: so that `==` never crashes when the left side is null. You can write `a == null` without a guard, and the documentation says Kotlin turns it into `a === null`.

**What goes wrong if you write `equals` and forget `hashCode`?**
The `hashCode` documentation says two objects that are equal by `equals` must give the same **hash code** (a number computed from the object's fields). If you break that, hash-based collections such as **`HashSet`** (a set) and **`HashMap`** (a map), which look items up by that number, can fail to find an object that is equal to one inside (general knowledge: they use the hash code to decide where to look). Data classes avoid the problem because the compiler generates both functions together, says the data class page.

## Recap

1. `===` asks whether two variables point at the same object, and `==` asks the object's `equals` function.
2. A plain class has no rule of its own, so `==` on two separate objects says false.
3. Text has its own rule, so `==` compares its letters.

## You can now

- Predict the result of `==` and `!=` on two separate objects of a plain class that hold the same values.
- Predict the result of `===` after one variable is set from another.
- Predict how a change through one variable shows through another tag on the same object.
