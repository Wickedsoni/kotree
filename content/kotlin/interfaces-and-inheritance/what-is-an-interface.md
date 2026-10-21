---
id: kotlin/interfaces-and-inheritance/what-is-an-interface
title: How can different classes promise the same ability?
status: shadow
difficulty: medium
order: 45
requires: [kotlin/classes-and-objects/member-functions]
summary: An interface is a list of promises. Any class that keeps them all can be used wherever that list is asked for.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/interfaces.html ; https://developer.android.com/reference/android/view/View.OnClickListener ; https://developer.android.com/develop/ui/views/touch-and-input/input-events -->
## The question

A phone charger fits the wall socket in your room, and so does a lamp, a fan and a laptop. The people who made them never met. How can one socket work with all of them? And how could a program ask for "anything that can do this job" without caring who made it?

## Mental model

Think of a wall socket.

- The socket shape is the list of promises, such as "has two flat pins".
- A plug is a class. It keeps the promises by having the right pins.
- A device with that plug is an object.
- The house wiring is the rest of the program. It only knows the socket shape, never the device.
- A new kind of device fits as soon as its plug has the right shape.

**Where the analogy stops:** A socket is a real shape in the wall. An interface only lists names and inputs and outputs, and it says nothing about how the job is done. Also, one device can have plugs for several sockets, and a socket shape is never a device: an interface has no body of its own, so you cannot make an object from it.

## Build the logic

### Notice

A garage has a button labelled "describe your vehicle". It works for a car, which answers "Car with 4 wheels". It also works for a bike, which answers "Bike with 2 wheels". The button has no idea which is which. It only knows that whatever is parked there can answer the question.

### Guess

How can the garage button work with both? Here are three ideas.

- A. The garage writes down the one question every vehicle must be able to answer, and each vehicle answers in its own way.
- B. The button has a separate rule for each vehicle it knows.
- C. Cars and bikes are forced to answer the same words.

Idea B breaks as soon as a scooter arrives: the button has no rule for it. Idea C throws away what makes each vehicle different. Idea A is the answer.

### Build

1. Write down the list of promises, such as "can describe itself".
2. Give the list a name, such as "Drivable".
3. Each vehicle says "I keep the promises of Drivable".
4. Each vehicle then supplies its own answer to every promise on the list.
5. If a vehicle forgets one answer, the garage refuses it before opening.
6. The button asks for "anything Drivable". Any vehicle that keeps the list fits.
7. A promise may come with a ready-made answer. A vehicle can then keep that answer or supply its own.

### Name it

An **interface** is a named list of promised functions. The Kotlin documentation says interfaces can contain declarations of abstract methods, as well as method implementations. An abstract method here is a promise with no body. A function that has a body in the interface is the ready-made answer.

To keep the promises, a class names the interface after a colon: `class Car : Drivable`. We say the class **implements** the interface. The class then writes each promised function with the word **override** in front, which marks the function that keeps the promise. The documentation's example marks the function that way.

```kotlin
interface Drivable {
    fun describe(): String
}

class Car : Drivable {
    override fun describe(): String {
        return "Car with 4 wheels"
    }
}

class Bike : Drivable {
    override fun describe(): String {
        return "Bike with 2 wheels"
    }
}

fun main() {
    val vehicles: List<Drivable> = listOf(Car(), Bike())
    for (v in vehicles) {
        println(v.describe())
    }
}
```

It prints `Car with 4 wheels` and then `Bike with 2 wheels`. The list asks for `Drivable`, and both a `Car` and a `Bike` fit.

The next program has a ready-made answer. `Ada` keeps it, and `Robot` supplies its own.

```kotlin
interface Greeter {
    fun name(): String

    fun greet(): String {
        return "Hello, " + name()
    }
}

class Ada : Greeter {
    override fun name(): String {
        return "Ada"
    }
}

class Robot : Greeter {
    override fun name(): String {
        return "Robot"
    }

    override fun greet(): String {
        return "Beep, " + name()
    }
}

fun main() {
    println(Ada().greet())
    println(Robot().greet())
}
```

It prints `Hello, Ada` and then `Beep, Robot`.

Kotlin refuses to run the next two programs. The first class forgets a promise. The second tries to make a device from the socket shape itself.

```kotlin
// does not compile
interface Drivable {
    fun describe(): String
}

class Scooter : Drivable

fun main() {
    println("never runs")
}
```

```kotlin
// does not compile
interface Drivable {
    fun describe(): String
}

fun main() {
    val d = Drivable()
}
```

## Predict the output

```challenge
type: predict
run: false
id: list-of-drivable
verified: kotlin-2.4.21
code: |
  interface Drivable {
      fun describe(): String
  }

  class Car : Drivable {
      override fun describe(): String {
          return "Car with 4 wheels"
      }
  }

  class Bike : Drivable {
      override fun describe(): String {
          return "Bike with 2 wheels"
      }
  }

  fun main() {
      val vehicles: List<Drivable> = listOf(Car(), Bike())
      for (v in vehicles) {
          println(v.describe())
      }
  }
options:
  - id: a
    text: "Car with 4 wheels, then Bike with 2 wheels"
    correct: true
    why: "The button visits the vehicles in the order they are listed. Each one answers with its own describe, so the car and the bike give different words."
  - id: b
    text: "Bike with 2 wheels, then Car with 4 wheels"
    why: "You may have expected the list to be sorted. The loop visits the items in the order they were put in the list."
  - id: c
    text: "The program is refused, because the list holds two different classes"
    why: "You may have thought a list needs one class. Both classes keep the promises of Drivable, so both fit the socket."
output: |
  Car with 4 wheels
  Bike with 2 wheels
```

```challenge
type: predict
run: false
id: ready-made-answer
verified: kotlin-2.4.21
code: |
  interface Helper {
      fun who(): String

      fun hello(): String {
          return "Hi, " + who()
      }
  }

  class Maya : Helper {
      override fun who(): String {
          return "Maya"
      }
  }

  class Bot : Helper {
      override fun who(): String {
          return "Bot"
      }

      override fun hello(): String {
          return "Beep, " + who()
      }
  }

  fun main() {
      println(Maya().hello())
      println(Bot().hello())
  }
options:
  - id: a
    text: "Hi, Maya then Hi, Bot"
    why: "You may have thought the ready-made answer always wins. Bot wrote its own hello, so its version runs."
  - id: b
    text: "Beep, Maya then Beep, Bot"
    why: "You may have thought one override changes the answer for every class. Only Bot wrote its own hello. Maya keeps the ready-made one."
  - id: c
    text: "Hi, Maya then Beep, Bot"
    correct: true
    why: "Maya never supplied her own hello, so she keeps the ready-made answer. Bot supplied one, so Bot uses it."
output: |
  Hi, Maya
  Beep, Bot
```

```challenge
type: predict
run: false
id: one-function-any-plug
verified: kotlin-2.4.21
code: |
  interface Speaker {
      fun speak(): String
  }

  class Dog : Speaker {
      override fun speak(): String {
          return "Woof"
      }
  }

  class Cat : Speaker {
      override fun speak(): String {
          return "Meow"
      }
  }

  fun announce(s: Speaker) {
      println("Says " + s.speak())
  }

  fun main() {
      announce(Cat())
      announce(Dog())
  }
options:
  - id: a
    text: "Says Woof, then Says Meow"
    why: "You may have read the classes in the order they are written. The calls in main run top to bottom, and the cat comes first."
  - id: b
    text: "Says Meow, then Says Woof"
    correct: true
    why: "announce asks for anything Speaker. The cat fits and answers Meow, then the dog fits and answers Woof."
  - id: c
    text: "The program is refused, because announce takes only one kind of object"
    why: "You may have thought the parameter names one class. It names the socket shape, and both Cat and Dog have the plug for it."
output: |
  Says Meow
  Says Woof
```

## Tricks to remember

- An interface is a socket shape, not a device.
- A colon after the class name says "I keep these promises".
- Forget one answer and the garage refuses you before opening.
- Ask for the socket shape (the interface), not for one device.

## Common mistakes

- **Trying to make an object from an interface.** The word "Drivable" sounds like a thing you could build. It is only the list of promises. Make a `Car` or a `Bike` instead.
- **Keeping only some of the promises.** A class that names the interface feels finished. The documentation's example class gives a body to the function that has none in the interface. If a body is missing, Kotlin refuses to run the program.
- **Writing a separate rule for each class in the code that uses them.** It works until a third class arrives. Ask for the interface instead, so a new class fits without a change.

## Where this shows up in Android

The Android reference lists `View.OnClickListener` as an interface with one function, `onClick`, that is called when a view has been clicked. Your code gives the button something that keeps that promise, and the button calls it on a tap (general knowledge). The button never needs to know what your class is called. If a tap does nothing, ask whether the right object was given to the button. That last point is my own reasoning.

## Say it in an interview

**What is an interface in Kotlin, and what can it contain?**
It is a named list of promised functions. The Kotlin documentation says interfaces can contain declarations of abstract methods, as well as method implementations. A class that implements it must give a body to each promise that has none.

**Can an interface property store a value?**
No. The documentation says properties declared in interfaces cannot have backing fields, so the accessors cannot refer to them. A class that implements the interface holds the actual value.

**What happens if a class gets the same function from two interfaces?**
The documentation says the class must say how it implements it. Inside the function it can name which parent to call, using `super<A>.foo()` and `super<B>.foo()`. This is the case where two promises with ready-made answers collide.

**Why ask for an interface instead of a concrete class?**
So new classes fit without changing the code that uses them. My own reasoning: the trade-off is one more name to learn, and the interface cannot hold values the way a class can.

## Recap

1. An interface is a named list of promised functions.
2. A class keeps the promises by naming the interface and overriding each function.
3. Code that asks for the interface accepts any class that keeps the promises.

## You can now

- Predict which version of a function runs for each object in a list of one interface type.
- Predict when a ready-made answer in an interface is used and when it is replaced.
- Explain why one function can accept both a Cat and a Dog.
