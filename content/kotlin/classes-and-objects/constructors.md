---
id: kotlin/classes-and-objects/constructors
title: How does a new object get its starting values?
status: shadow
difficulty: easy
order: 37
requires: [kotlin/classes-and-objects/what-is-a-class]
summary: A constructor is the form you fill in to make an object. See how its boxes become properties and how defaults work.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/classes.html ; https://kotlinlang.org/docs/kotlin-tour-classes.html ; https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories -->
## The question

You join a gym. You fill in a form with your name and age, and only then do you get your membership card. A program that makes a new card can work the same way. Where do the name and age on the new card come from? Must the program write each one onto the card with a separate line after the card exists?

## Mental model

Think of the gym's new-member form.

- The gym has one design for its membership cards. A new card is made each time a form is handed in.
- The form has boxes, and each box has a label, such as "name" and "age".
- Some boxes are marked "print on the card". Their answers appear on the card, in ink if they never change, or in pencil if they may change.
- Boxes without that mark help the clerk while the card is made. They do not end up on the card.
- A pre-filled answer in a box, such as "age: 18", is used when you leave the box empty.
- The clerk has a list of checks. They run each time a form is handed in, from top to bottom.
- A shorter form with fewer boxes also exists. The clerk copies it onto the main form, and the main form's work finishes first.

**Where the analogy stops:** A real form is filled in by one person at a desk, and a real clerk might skip a check. In code, any line can hand in a form, thousands of times a second. And every check always runs, in the order it is written.

## Build the logic

### Notice

Maya hands in a form with the name Maya and the age 30. Both boxes are marked "print on the card". The clerk runs the checks, and one of them announces "Card made for Maya". The card comes out with Maya and 30 on it. Sam leaves the age box empty. It was pre-filled with 18, so his card shows 18. The age box is in pencil, so Sam can change it to 19 afterwards. Maya's card and Sam's card never mix up.

### Guess

Where does the card get Maya's name and age? Here are three ideas.

- A. They are written onto a blank card with separate lines, after the card exists.
- B. They come from the form, before the card exists, so the card is complete when it is made.
- C. The card finds them out for itself.

Idea A feels right to people who have seen other languages, and it does work, but it leaves a half-made card between the lines. Idea C has no source for the facts. Idea B is the answer.

The design also lists pre-printed lines and the clerk's checks. In what order do they happen? Here are three ideas.

- A. All the pre-printed lines first, then all the checks.
- B. From top to bottom, in the order they are written in the design.
- C. All the checks first, then the pre-printed lines.

Idea A feels tidy, but the design is one list read from top to bottom, not two groups. Idea C has the same fault, and it is worse because a check would run before the pre-printed lines it may need. Idea B is the answer.

### Build

1. Put the boxes on the form for the card's design. Give each box a label and a kind of answer.
2. Mark which boxes print on the card. For those, choose ink if the answer never changes, or pencil if it may change.
3. Boxes without the mark are only for the clerk's work. They do not end up on the card, and nobody can read them from the card afterwards.
4. Write the clerk's checks. They run each time a form is handed in.
5. The clerk works through everything written in the design from top to bottom, pre-printed lines and checks together.
6. Add pre-filled answers for any box that may be left empty.
7. A shorter form must be copied onto the main form. The main form's work always finishes before the shorter form's own extra steps.

### Name it

A **constructor** is the form for making an object. The Kotlin documentation says the **primary constructor** is declared in the class header, which is the line with the class name, as in `class Member(val name: String, var age: Int = 18)`. Each box is a **parameter**, a name with a type.

The words `val` and `var` in front of a parameter make it a property. The documentation says `val` makes a read-only property and `var` makes a changeable one. A parameter with neither is not stored in the object. The documentation says it is available only inside the class body, which means the curly brackets after the header.

A **default value** is the pre-filled answer. The documentation says that if no value is passed when the object is made, the properties use their default values.

An **init block**, written `init { ... }`, holds the clerk's checks. The documentation says it runs when the primary constructor runs. You may write several, and they run in the order they appear in the class body, together with the property initializers. A **property initializer** is the part of a property line that gives it its starting value, such as `= "T" + id`.

A **secondary constructor** is the shorter form. The documentation says you write it with the word `constructor` inside the class body, and it must delegate to the primary constructor. To delegate means to pass the work on, written `: this(...)`. The documentation says the primary constructor's logic runs before any secondary constructor logic.

| Membership form | In Kotlin |
|---|---|
| The card design | The class |
| The main form | The primary constructor |
| A box and its label | A parameter |
| Printed in ink | `val` before the parameter |
| Printed in pencil | `var` before the parameter |
| A pre-filled answer | A default value |
| The clerk's checks | An `init` block |
| The shorter form | A secondary constructor |

This program shows `val`, `var`, a default value and an `init` block.

```kotlin
class Member(val name: String, var age: Int = 18) {
    init {
        println("Card made for " + name)
    }
}

fun main() {
    val maya = Member("Maya", 30)
    val sam = Member("Sam")
    sam.age = 19
    println(maya.name + " " + maya.age)
    println(sam.name + " " + sam.age)
}
```

It prints `Card made for Maya`, `Card made for Sam`, `Maya 30` and `Sam 19`. Each form hands in once, and the `init` block runs at that moment. Sam's age started as the default 18 and was changed to 19 afterwards.

This program shows a secondary constructor.

```kotlin
class Pet(val name: String, val legs: Int) {
    init {
        println("main form: " + name)
    }

    constructor(name: String) : this(name, 4) {
        println("short form: " + name)
    }
}

fun main() {
    val rex = Pet("Rex")
    println(rex.legs)
}
```

It prints `main form: Rex`, `short form: Rex` and `4`. The short form passes `Rex` and `4` to the main form. The main form's `init` block runs first, and then the short form's own line.

Kotlin refuses to run this one, because `name` has neither `val` nor `var`, so it is not stored on the card.

```kotlin
// does not compile
class Member(name: String)

fun main() {
    val m = Member("Maya")
    println(m.name)
}
```

## Predict the output

```challenge
type: predict
run: false
id: checks-run-per-form
verified: kotlin-2.4.21
code: |
  class Badge(val label: String) {
      init {
          println("Badge for " + label)
      }
  }

  fun main() {
      Badge("A")
      println("between")
      Badge("B")
  }
options:
  - id: a
    text: "between, Badge for A, Badge for B"
    why: "You may have thought the checks wait until the card is used. The clerk's checks run as soon as each form is handed in."
  - id: b
    text: "Badge for A, between, Badge for B"
    correct: true
    why: "The first form is handed in and its checks run. Then between prints, and the second form's checks run when it is handed in."
  - id: c
    text: "Badge for A, Badge for B, between"
    why: "You may have thought all the forms are handled before the rest of main. The lines of main run in order, so the second form comes after between."
output: |
  Badge for A
  between
  Badge for B
```

```challenge
type: predict
run: false
id: top-to-bottom-in-the-design
verified: kotlin-2.4.21
code: |
  class Mix(val id: Int) {
      init { println("one: " + id) }
      val tag = "T" + id
      init { println("two: " + tag) }
  }

  fun main() {
      Mix(7)
  }
options:
  - id: a
    text: "two: T7 and then one: 7"
    why: "You may have thought the pre-printed lines come before every check. The documentation says they run together, in the order written."
  - id: b
    text: "one: 7 and then two: null"
    why: "You may have thought tag is empty at the second check. The line that sets tag sits above the second check, so it has run by then."
  - id: c
    text: "one: 7 and then two: T7"
    correct: true
    why: "The clerk goes top to bottom. The first check runs, then the tag line gives tag its value, then the second check reads it."
output: |
  one: 7
  two: T7
```

```challenge
type: predict
run: false
id: pre-filled-answers
verified: kotlin-2.4.21
code: |
  class Plan(val name: String = "basic", val seats: Int = 1)

  fun main() {
      val a = Plan()
      val b = Plan("team")
      val c = Plan("pro", 5)
      println(a.name + " " + a.seats)
      println(b.name + " " + b.seats)
      println(c.name + " " + c.seats)
  }
options:
  - id: a
    text: "basic 1, team 1, pro 5"
    correct: true
    why: "The empty form uses both pre-filled answers. The second form fills name and uses the pre-filled seats. The third form fills both boxes."
  - id: b
    text: "basic 1, team 0, pro 5"
    why: "You may have thought an empty box becomes 0. The form pre-fills seats with 1, so the second card shows 1."
  - id: c
    text: "The program is refused, because Plan(\"team\") gives one answer for two boxes"
    why: "You may have expected every box to need an answer. A box with a pre-filled answer may be left empty."
output: |
  basic 1
  team 1
  pro 5
```

## Tricks to remember

- The form is filled in before the card exists.
- `val` is ink, `var` is pencil, and no mark means it is gone after the clerk is done.
- The clerk works top to bottom: pre-printed lines and checks in the order written.
- A short form always goes through the main form first.

## Common mistakes

- **Leaving out `val` or `var` and then using the parameter as a property.** Writing the label on the form feels like putting it on the card. Without `val` or `var`, the answer is only for the clerk. Add `val` or `var` if the card must keep it.
- **Setting every property afterwards with a separate line.** It works in other languages, and it reads clearly. The card sits half made between the lines. Put the starting answers on the form so the card is whole when it is made.
- **Expecting all pre-printed lines to run before the checks.** Grouping them feels tidy. They run together, in the order written. Put a line above any check that reads it.
- **Writing a secondary constructor that does not pass the work on.** The short form looks complete. The documentation says it must delegate to the primary constructor, and Kotlin refuses if it does not.

## Where this shows up in Android

A **ViewModel** is a class that holds the data for a screen (general knowledge). The Android ViewModel factory page says a `ViewModel` can take dependencies as parameters in its constructor. A dependency is something the class needs to do its job, such as a place to load data from (general knowledge). The page's example is a class called `MyViewModel` whose form has two boxes, `myRepository` and `savedStateHandle`. This lesson does not explain those two types.

The page also says the **framework**, which is the ready-made Android code that runs your app (general knowledge), provides the `ViewModel`, so a special helper called a factory is needed to create it when it has dependencies. If the `ViewModel` takes no dependencies, or only one of the type `SavedStateHandle`, no factory is needed. This next part is my own reasoning: the system is the clerk, and a new box on the form is a box the clerk does not know how to fill. The factory tells the clerk what to write in it.

## Say it in an interview

**What is the difference between a primary and a secondary constructor?**
The primary constructor is declared in the class header. A secondary constructor is written with the word `constructor` inside the class body. The Kotlin documentation says a secondary constructor must delegate to the primary one, so the primary constructor's work runs first.

**Does a constructor parameter become a property?**
Only with `val` or `var` in front of it. The documentation says a parameter without them is not stored and is available only inside the class body. The tour recommends `val` unless the value must change after the object is made.

**In what order do `init` blocks and property initializers run?**
In the order they appear in the class body. The documentation says you can write several `init` blocks and they run together with the property initializers. Constructor parameters can be used in them. The documentation's example uses an `init` block to check that an age is positive.

**When would you use default values and when a secondary constructor?**
Default values fit when an input may be left out, like the age 18. A secondary constructor fits when the input is a different shape, such as text that has to be turned into a number first. The documentation's example does that. My own reasoning: defaults mean fewer constructors to read, so try them first.

## Recap

1. A primary constructor takes the starting values, and `val` or `var` makes a parameter a property.
2. Default values fill the boxes you leave out.
3. Init blocks and property initializers run top to bottom, in the order they are written.

## You can now

- Predict what an init block prints when objects are made.
- Predict the order of output when init blocks and property initializers are mixed.
- Predict the values an object holds when some constructor boxes are left out.
