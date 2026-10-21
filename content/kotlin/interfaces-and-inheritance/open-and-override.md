---
id: kotlin/interfaces-and-inheritance/open-and-override
title: How does one class build on another class?
status: live
difficulty: medium
order: 46
requires: [kotlin/interfaces-and-inheritance/what-is-an-interface]
summary: A class must be marked open before another class can build on it. See how override changes a step and super keeps the original.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---

## The question

Your gran has a family soup recipe. Your cousin wants the same soup with pepper added. Can he start from gran's recipe and change one step, or must he write the whole recipe again? And what if gran's book is locked so that nobody may change it?

## Mental model

Think of a family recipe book.

- Each recipe is a class. Its steps are the functions.
- Every recipe book is locked by default. Nobody may build on it.
- A note on the cover, "you may build on this recipe", opens the whole recipe for building.
- A note beside one step, "you may change this step", opens that one step.
- Your cousin's recipe starts from gran's recipe and has every step she has.
- When your cousin changes a step, he writes "my change" next to it.
- "Do gran's version of this step first" lets him keep her step and add to it.

**Where the analogy stops:** A real cousin can scribble on gran's book if he wants to. In code, Kotlin refuses before the program runs. A real cook also chooses which recipe to cook. In code, the object you made decides which version of a step runs, whatever label the variable has.

## Build the logic

### Notice

Gran's soup recipe says: "boil water, add salt". Her cover has the note "you may build on this recipe". Beside the first step is a note "you may change this step". The second step, "serve hot", has no note. Your cousin starts from her recipe. He changes the first step to "boil water, add salt, add pepper". He cannot change "serve hot".

### Guess

What should Kotlin do when a cousin tries to change a step with no note? Here are three ideas.

- A. Allow it. Anyone can change anything.
- B. Allow it but warn him in the margin.
- C. Refuse it before the program runs.

Idea A is how many people think every language works. My own reasoning: the reason to lock steps is that gran may change her recipe later, and a cousin's recipe that depends on it could break. Idea C is what Kotlin does.

What should happen when your cousin makes his soup but stores it in a jar labelled "Soup"? Here are three ideas.

- A. The soup inside decides, so his step runs.
- B. The jar's label decides, so gran's step runs.
- C. Both steps run.

Idea A is the answer.

### Build

1. Lock every recipe by default.
2. To let others build on it, the owner writes a cover note.
3. To let others change a step, the owner writes a note beside that step.
4. A new recipe names the one it builds on, and gets all its steps.
5. A step that the new recipe changes carries the words "my change".
6. If the new recipe wants gran's version of the step too, it says "do gran's version first", then adds its own.
7. Cooking the new recipe uses the new recipe's steps, even if the jar is labelled with gran's name.

### Name it

In Kotlin, classes are **final** by default. The documentation says they cannot be inherited. A class is opened for building on by marking it with **open**. The class that is built on is the parent class, called the **superclass**. The new one is a child class, called a **subclass**, and it **inherits** from the superclass. A subclass names its superclass after a colon, with brackets: `class PepperSoup : Soup()`. The documentation's example writes `class Circle() : Shape()`.

A modifier is a word such as `open` or `override` written before a function or class. A signature is the same name and the same inputs. The documentation says that if a function has no `open` modifier, a function with the same signature in a child is not allowed, either with `override` or without it. A function that may be changed is marked `open`. The child changes it with `override`, as you saw before, and the documentation says that modifier is required. To run the parent's version of a step, the child calls **super**, as in `super.steps()`.

```kotlin
open class Soup {
    open fun steps(): String {
        return "boil water, add salt"
    }

    fun serve(): String {
        return "serve hot"
    }
}

class PepperSoup : Soup() {
    override fun steps(): String {
        return super.steps() + ", add pepper"
    }
}

fun main() {
    val dish: Soup = PepperSoup()
    println(dish.steps())
    println(dish.serve())
}
```

It prints `boil water, add salt, add pepper` and then `serve hot`. The variable `dish` is labelled `Soup`, but the object inside is a `PepperSoup`, so its `steps` runs. The `serve` step was not open and was not changed.

Kotlin refuses to run the next two programs. The first builds on a class that is not open. The second changes a step that is not open.

```kotlin
// does not compile
class Soup

class PepperSoup : Soup()

fun main() {
    println("never runs")
}
```

```kotlin
// does not compile
open class Soup {
    fun serve(): String {
        return "serve hot"
    }
}

class PepperSoup : Soup() {
    override fun serve(): String {
        return "serve with pepper"
    }
}

fun main() {
    println("never runs")
}
```

## Predict the output

```challenge
type: predict
run: false
id: dog-held-as-animal
verified: kotlin-2.4.21
code: |
  open class Animal {
      open fun sound(): String {
          return "..."
      }
  }

  class Dog : Animal() {
      override fun sound(): String {
          return "Woof"
      }
  }

  fun main() {
      val a: Animal = Dog()
      println(a.sound())
  }
options:
  - id: a
    text: "..."
    why: "You may have thought the jar's label decides. The jar says Animal, but the thing inside is a Dog, and the Dog changed the step."
  - id: b
    text: "Woof"
    correct: true
    why: "The object that was made is a Dog, so the Dog's version of the step runs. The label on the jar does not change that."
  - id: c
    text: "The program is refused, because a Dog cannot go in a jar labelled Animal"
    why: "You may have thought the label and the object must match exactly. A Dog is built on Animal, so it fits in an Animal jar."
output: |
  Woof
```

```challenge
type: predict
run: false
id: forgot-gran-first
verified: kotlin-2.4.21
code: |
  open class Account {
      open fun close(): String {
          return "balance zeroed"
      }
  }

  class SavingsAccount : Account() {
      override fun close(): String {
          return "interest paid"
      }
  }

  class CheckingAccount : Account() {
      override fun close(): String {
          return super.close() + ", card cancelled"
      }
  }

  fun main() {
      println(SavingsAccount().close())
      println(CheckingAccount().close())
  }
options:
  - id: a
    text: "interest paid, then balance zeroed, card cancelled"
    correct: true
    why: "The savings recipe changed the step and never said to do gran's version first, so only its own words appear. The checking recipe did gran's version first, then added its own."
  - id: b
    text: "balance zeroed, then balance zeroed, card cancelled"
    why: "You may have thought gran's step always runs. A changed step replaces hers unless the child calls super."
  - id: c
    text: "interest paid, then card cancelled"
    why: "You may have thought super.close() adds nothing. It runs the parent's close first, so those words come before the new ones."
output: |
  interest paid
  balance zeroed, card cancelled
```

```challenge
type: predict
run: false
id: changed-and-unchanged-steps
verified: kotlin-2.4.21
code: |
  open class Vehicle {
      open fun honk(): String {
          return "beep"
      }

      fun wheels(): Int {
          return 4
      }
  }

  class Truck : Vehicle() {
      override fun honk(): String {
          return "HONK"
      }
  }

  class Van : Vehicle()

  fun main() {
      val list: List<Vehicle> = listOf(Truck(), Van())
      for (v in list) {
          println(v.honk() + " " + v.wheels())
      }
  }
options:
  - id: a
    text: "HONK 4, then HONK 4"
    why: "You may have thought a change in one child reaches every child. Van never changed the step, so it keeps gran's version."
  - id: b
    text: "beep 4, then beep 4"
    why: "You may have thought the label Vehicle decides. The Truck changed honk, so its own version runs."
  - id: c
    text: "HONK 4, then beep 4"
    correct: true
    why: "Truck changed honk and gets its own. Van changed nothing, so it uses the parent's honk. Both get the parent's wheels step."
output: |
  HONK 4
  beep 4
```

## Tricks to remember

- Locked by default. `open` unlocks, `override` changes.
- The object inside the jar decides which step runs, not the label on the jar.
- A changed step replaces the parent's. Call `super` to keep the parent's first.
- A step with no `open` note stays gran's.

## Common mistakes

- **Thinking every class can be built on by default.** In some other languages it works that way, so it feels normal. In Kotlin a class is final unless it carries `open`. Add `open` to the parent when building on it is the plan.
- **Forgetting `override` on a child's function.** The names match, so it feels like enough. The documentation says the modifier is required, and the compiler complains without it.
- **Overriding a step and leaving out `super`.** The child's step looks complete. It replaces the parent's work, and the parent's work is lost. Call `super` first when the parent's work still needs to happen.
- **Opening every step in case one is needed later.** It feels flexible. Any open step can be changed by a child in a way the parent did not expect. Open only the steps that need to change.

## Where this shows up in Android

The Android activity lifecycle page shows `class ExampleActivity : ComponentActivity()` with `override fun onCreate(savedInstanceState: Bundle?)`, and the first line inside is `super.onCreate(savedInstanceState)`. The page calls `onCreate` a callback you must implement, and that fires when the system first creates the activity. Your activity builds on a class written by Android, and `super` runs the parent's version first. My own reasoning: if a screen starts without something the parent normally sets up, check whether a `super` call was removed.

## Say it in an interview

**Why must a Kotlin class be marked `open` before it can be inherited?**
The Kotlin documentation says classes are final by default, so they cannot be inherited. You choose to open one. My own reasoning: this stops other code building on a class that its author never meant to be changed.

**What do `override` and `super` do?**
`override` marks a function in a subclass that replaces a function from the superclass. The documentation says it is required. `super` lets the subclass call the superclass's version, as in `super.draw()`. Leave it out and the parent's work does not run.

**Does an overriding function stay open for grandchildren?**
Yes. The documentation says a member marked `override` is itself open. To stop that, write `final override`.

**What is the trade-off of building on a class?**
My own reasoning: you get the superclass's work for free, but the subclass depends on how the superclass behaves. If the superclass changes, the subclass can break. Kotlin's locked-by-default design makes the author choose to take that risk.

## Recap

1. A Kotlin class and its functions are locked until marked `open`.
2. A child uses `override` to replace a step, and `super` to call the parent's version.
3. The object that was made decides which version of a step runs, whatever the variable's type.

## You can now

- Predict which version of a function runs when a child object is held in a parent-typed variable.
- Predict whether a child's output includes the parent's work when `super` is or is not called.
- Explain why Kotlin refuses a child of a class that is not marked `open`.
