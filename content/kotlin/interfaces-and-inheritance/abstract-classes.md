---
id: kotlin/interfaces-and-inheritance/abstract-classes
title: When should I use an abstract class instead of an interface?
status: shadow
difficulty: medium
order: 47
requires: [kotlin/interfaces-and-inheritance/open-and-override]
summary: An abstract class is a half-finished class that cannot be made on its own. Learn when it fits better than an interface.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/classes.html#abstract-classes ; https://kotlinlang.org/docs/interfaces.html ; https://kotlinlang.org/docs/inheritance.html ; https://developer.android.com/reference/androidx/lifecycle/ViewModel ; https://developer.android.com/topic/architecture/ui-layer/state-production -->
<!-- ⚠️ VERIFY: compile not run for any kotlin block in this leaf (Kotlin 2.4.21, docs/06 section 5). All outputs were traced by hand. -->
<!-- ⚠️ VERIFY: "ViewModel is declared as an abstract class" comes from a search summary of the API reference page, not from reading the page body. -->
## The question

You are building ten different houses. All ten need the same walls, the same wiring and the same front door. Only the rooms inside differ. Would you draw the walls ten times? And what if the town also has a rule book that lists what every house must have, but builds nothing?

## Mental model

Think of a half-built house kit, and a building code.

- The kit is a class with the walls already built. It comes with the wiring and the front door too.
- The empty rooms in the kit are the parts you must fill in yourself.
- You cannot live in a kit. You must finish the rooms to get a house you can live in.
- A finished house is a class you can make an object from.
- The building code is a list of rules, such as "must have a fire exit". It builds nothing and holds no furniture.
- A house can follow several codes, but it can be built from only one kit.

**Where the analogy stops:** A real kit is a box of parts that gets used up. In code, the kit is a class, and you can build any number of houses from it. Each house gets its own copy of the stored data, so one house never changes another.

## Build the logic

### Notice

Three pets live in a shelter: Rex the dog, Tom the cat and Pip the bird. Every pet has a name. Every pet can introduce itself with the sentence "name says sound". The name and the sentence are the same for all pets. Only the sound differs.

### Guess

Where should the name and the introduction sentence live? Here are three ideas.

- A. Write them again inside every pet.
- B. Write them once in a half-finished pet design that says "the sound must be filled in by each pet".
- C. Write a rule book that lists "has a name" and "introduces itself", and let each pet follow it.

Idea A means ten pets, ten copies, ten places to fix. Idea C lists what is needed but gives no place to keep the name. Idea B keeps the name and the sentence once.

Does the rule book still have a job? Yes, when many unrelated things must promise the same ability. A pet and a robot could both follow "can introduce itself". They share no walls.

### Build

1. Write the shared parts once: the name and the introduction sentence.
2. Leave a gap for the part that differs: the sound. The design must say that the gap exists.
3. Refuse to make a pet from the half-finished design, because the gap is empty.
4. Each real pet builds on the design, fills the gap and passes in its name.
5. For abilities that unrelated things share, write a rule book instead. It lists promises and holds no data.

### Name it

An **abstract class** is a class that cannot be made directly. The Kotlin documentation says abstract classes are classes that can't be instantiated directly, and are designed to be inherited by other classes. It says an abstract class can have both abstract and non-abstract members. To declare a member as **abstract**, you must use the `abstract` keyword explicitly. An abstract member has no body, and the child fills it in with `override`. The documentation says you don't need to write `open` on abstract classes or functions.

Here is how the two tools compare.

| Question | Interface | Abstract class |
|---|---|---|
| Can I make an object from it? | No | No |
| Can it keep data? | No. The documentation says interface properties cannot have backing fields. | Yes. It can have non-abstract properties. |
| How many can one class use? | One or more. The documentation says a class can implement one or more interfaces. | One parent class (general knowledge). |

```kotlin
abstract class Pet(val name: String) {
    abstract fun sound(): String

    fun intro(): String {
        return name + " says " + sound()
    }
}

class Dog(name: String) : Pet(name) {
    override fun sound(): String {
        return "Woof"
    }
}

class Bird(name: String) : Pet(name) {
    override fun sound(): String {
        return "Tweet"
    }
}

fun main() {
    val pets: List<Pet> = listOf(Dog("Rex"), Bird("Pip"))
    for (p in pets) {
        println(p.intro())
    }
}
```

It prints `Rex says Woof` and then `Pip says Tweet`. The name and `intro` are written once in `Pet`. Each pet fills in only the sound and passes its name to the parent.

Kotlin refuses to run the next two programs. The first makes an object from the half-finished design. The second tries to keep data in a rule book.

```kotlin
// does not compile
abstract class Pet(val name: String) {
    abstract fun sound(): String
}

fun main() {
    val p = Pet("Rex")
}
```

```kotlin
// does not compile
interface Counter {
    var count: Int = 0
}

fun main() {
    println("never runs")
}
```

## Predict the output

```challenge
type: predict
run: false
id: pets-fill-the-gap
verified: not-run
code: |
  abstract class Pet(val name: String) {
      abstract fun sound(): String

      fun intro(): String {
          return name + " says " + sound()
      }
  }

  class Dog(name: String) : Pet(name) {
      override fun sound(): String {
          return "Woof"
      }
  }

  class Cat(name: String) : Pet(name) {
      override fun sound(): String {
          return "Meow"
      }
  }

  fun main() {
      val pets: List<Pet> = listOf(Dog("Rex"), Cat("Tom"))
      for (p in pets) {
          println(p.intro())
      }
  }
options:
  - id: a
    text: "Rex says Woof, then Tom says Woof"
    why: "You may have thought the shared intro decides the sound. The sound is the gap each pet fills, so Tom uses his own."
  - id: b
    text: "The program is refused, because Pet is abstract and cannot be in a list"
    why: "You may have thought an abstract class cannot be used at all. It cannot be made directly, but it can be the type of a variable or a list."
  - id: c
    text: "Rex says Woof, then Tom says Meow"
    correct: true
    why: "intro is written once in the design and calls sound. Each pet filled the gap with its own sound."
output: |
  Rex says Woof
  Tom says Meow
```

```challenge
type: predict
run: false
id: each-house-own-count
verified: not-run
code: |
  abstract class Counter {
      var count = 0

      fun hit() {
          count = count + 1
          report()
      }

      abstract fun report()
  }

  class LoudCounter : Counter() {
      override fun report() {
          println("count is " + count)
      }
  }

  fun main() {
      val a = LoudCounter()
      val b = LoudCounter()
      a.hit()
      a.hit()
      b.hit()
  }
options:
  - id: a
    text: "count is 1, count is 2, count is 3"
    why: "You may have thought the walls are shared between houses. Each house built from the kit gets its own copy of count."
  - id: b
    text: "count is 1, count is 2, count is 1"
    correct: true
    why: "The kit keeps the data. a and b are two separate houses, so a counts 1 and 2 and b starts again at 1."
  - id: c
    text: "count is 1, count is 1, count is 1"
    why: "You may have thought count resets after each report. It stays in the house, so a's second hit makes it 2."
output: |
  count is 1
  count is 2
  count is 1
```

```challenge
type: predict
run: false
id: lamp-switch-state
verified: not-run
code: |
  abstract class Light(var on: Boolean) {
      fun flip() {
          on = !on
      }

      abstract fun glow(): String
  }

  class Lamp : Light(false) {
      override fun glow(): String {
          if (on) {
              return "bright"
          } else {
              return "dark"
          }
      }
  }

  fun main() {
      val lamp = Lamp()
      println(lamp.glow())
      lamp.flip()
      println(lamp.glow())
      lamp.flip()
      println(lamp.glow())
  }
options:
  - id: a
    text: "dark, bright, dark"
    correct: true
    why: "Lamp passes false to the kit, so it starts dark. Each flip turns the stored value over, so the glow goes dark, bright, dark."
  - id: b
    text: "bright, dark, bright"
    why: "You may have read false as switched on. Lamp passes false, which means off, so the first glow is dark."
  - id: c
    text: "dark, bright, bright"
    why: "You may have thought the second flip does nothing. Every call to flip turns the stored value over again."
output: |
  dark
  bright
  dark
```

## Tricks to remember

- A kit has walls and empty rooms. A code has only rules.
- `abstract` on a class means you cannot make it. `abstract` on a function means no body.
- Need to keep data? Reach for the kit. Need unrelated things to promise one ability? Reach for the code.
- A house follows many codes but is built from one kit.

## Common mistakes

- **Thinking abstract classes and interfaces are the same thing with different names.** Both cannot be made directly, so they look alike. An interface cannot keep data and a class can follow many, while an abstract class can keep data and a class can build on only one.
- **Putting shared data in an interface.** It feels like a place for "things every pet has". The documentation says interface properties cannot have backing fields. Keep the data in a class.
- **Writing `open` on an abstract class.** It feels needed because the class is built on. The documentation says abstract classes and functions are inheritable by default, so the word is not needed.
- **Trying to build one from the design.** `Pet("Rex")` looks like a normal call. The design has an empty room, so Kotlin refuses. Build a `Dog` or a `Bird`.

## Where this shows up in Android

The Android page on producing UI state shows `class DiceRollViewModel : ViewModel()`. The API reference describes `ViewModel` as an abstract class. You never build a plain `ViewModel`. You build your own class on top of it and add your data. The brackets after `ViewModel` are the call to the parent, as with `Pet(name)` above. If you ever see "cannot create an instance of an abstract class", you tried to make the kit instead of a house.

## Say it in an interview

**What is an abstract class, and how does it differ from an interface?**
An abstract class cannot be instantiated directly and can have both abstract and non-abstract members, according to the Kotlin documentation. An interface lists promises and cannot hold backing fields. A class can implement many interfaces but builds on one parent class (general knowledge).

**Can you create an object of an abstract class?**
No. The documentation says abstract classes can't be instantiated directly. You make an object of a child class that fills in every abstract member.

**When do you choose an abstract class over an interface?**
My own reasoning: choose an abstract class when closely related classes share data or finished functions, as with the pets and their names. Choose an interface when unrelated classes must promise the same ability.

**What can go wrong with abstract classes?**
My own reasoning: they grow. Each shared helper and each stored value ties every child to the parent. A parent that holds too much becomes hard to change, because every child depends on it. Keep the abstract class small, and keep abilities in interfaces.

## Recap

1. An abstract class cannot be made directly, but it can hold data and finished functions.
2. Its abstract functions have no body, and each child fills them in with `override`.
3. Use an abstract class to share data and steps, and an interface to share a promise.

## You can now

- Predict the output of a program where a child fills in an abstract function.
- Predict that two objects built from the same abstract class keep separate data.
- Explain why shared data belongs in an abstract class and not in an interface.
