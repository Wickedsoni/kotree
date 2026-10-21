---
id: kotlin/special-classes/object-declarations
title: How do I make exactly one of something in the whole program?
status: shadow
difficulty: medium
order: 53
requires: [kotlin/classes-and-objects/constructors]
summary: An object exists exactly once. See how the whole program shares it, and what a companion object is.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live; read 21 Oct 2026):
- https://kotlinlang.org/docs/object-declarations.html (object declaration, lazy thread-safe initialisation, companion object, data object toString)
- https://developer.android.com/training/data-storage/room (singleton note for the database object)
Roadmap note about data object stability: not claimed. The page shows data object and its toString; it states no version or status.
The roadmap row's F(ai) task is not built: playground is false.
Code not compiled: no shell in this session. Outputs hand-traced twice. Predict items are run: false, verified: not-run.
-->

## The question

A game keeps the player's score. One part of the program adds points when a coin is picked up. Another part draws the score on the screen. If each part makes its own score keeper, the screen shows 0 while the player has 40 points. How can the whole program share exactly one score keeper?

## Mental model

Think of the one town clock on the square.

- The clock is the one thing everybody shares. The whole town looks at the same clock.
- The clock has a name, "Town Clock", so nobody needs to ask where it is.
- It is built the first time somebody looks up at it, not before.
- If the baker sets the time, the postman reads the baker's time.
- Nobody can order a second clock for the square.
- A clock bolted to the wall of one building design is shared by every building of that design. You reach it by naming the design.

**Where the analogy stops:** A real clock is built before anyone looks at it. In code it is built on first use. Also, a real town shares the clock among people who live there. A program shares it among every part of the code, every user and every test, which can cause trouble.

## Build the logic

### Notice

The square has one clock that shows 0 ticks. The bakery adds 1 tick each time it sells a loaf. The post office adds 10 ticks each time it sends a parcel. After one loaf and one parcel, both buildings look up and read 11. A new shop opens and asks for a clock of its own. There is only the one on the square.

### Guess

How many clocks should the square have? Here are three ideas.

- A. One for each building that looks up.
- B. One for each visitor.
- C. Exactly one, built when it is first looked at.

Idea A means the bakery reads 1 and the post office reads 10. Idea B has the same fault. Idea C is the answer.

How does a building refer to the clock? Here are three ideas.

- A. By its name, "Town Clock". It is already there.
- B. By asking for a copy.
- C. By ordering a new one with that name.

Idea B gives a copy that would drift. Idea C breaks the rule of one clock. Idea A is the answer.

### Build

1. Build the clock once, on the square, and give it a name.
2. Every building refers to it by that name. Nobody orders a new one.
3. It is built the first time somebody looks at it.
4. Whatever one building sets, the next building reads.
5. For something shared by every building of one design, bolt a clock to the design. You reach it through the design's name.
6. Think before putting anything on a shared clock. Every building, every visitor and every test shares it.

### Name it

An **object declaration** is written with the word `object` and a name. The Kotlin documentation says it lets you define a class and create an instance of it in a single step, which is useful for implementing **singletons**. A singleton is a class that has exactly one instance (general knowledge). The documentation says that the initialisation of an object declaration is thread-safe and done on first access. **Thread-safe** means safe when several parts of a program run at the same time (general knowledge). You refer to the object by its name directly, as in `Counter.hit()`.

A **companion object** is an object declaration inside a class, marked with the word `companion`. The documentation says it lets you define class-level functions and properties, and that its members can be called by using the class name as the qualifier. It also says a companion object is initialised when its class is loaded.

A **`data object`** is an object declaration with the word `data` in front. The documentation says the compiler then makes `toString()` return the actual name of the object. Without `data`, printing an object shows its name plus an extra code.

This program shares one counter between two calls.

```kotlin
object Counter {
    var count = 0

    fun hit() {
        count = count + 1
    }
}

fun main() {
    Counter.hit()
    Counter.hit()
    println(Counter.count)
}
```

It prints `2`. There is only one `Counter`, so both calls change the same `count`.

This program has a companion object that makes tickets.

```kotlin
class User(val name: String) {
    companion object {
        fun guest(): User {
            return User("guest")
        }
    }
}

fun main() {
    val u = User.guest()
    println(u.name)
}
```

It prints `guest`. The call `User.guest()` uses the class name, with no `User` object needed first.

This program prints a `data object`.

```kotlin
data object Home

fun main() {
    println(Home)
}
```

It prints `Home`.

Kotlin refuses to run this one, because an object cannot be made a second time.

```kotlin
// does not compile
object Counter {
    var count = 0
}

fun main() {
    val c = Counter()
    println(c.count)
}
```

## Predict the output

```challenge
type: predict
run: false
id: two-callers-one-clock
verified: not-run
code: |
  object Clock {
      var ticks = 0
  }

  fun bakery() {
      Clock.ticks = Clock.ticks + 1
  }

  fun post() {
      Clock.ticks = Clock.ticks + 10
  }

  fun main() {
      bakery()
      post()
      println(Clock.ticks)
  }
options:
  - id: a
    text: "10"
    why: "You may have thought the last caller wins and starts from 0. Each caller adds to what the clock already shows."
  - id: b
    text: "1"
    why: "You may have thought each building has its own clock. There is one clock on the square, so the post office adds to the bakery's tick."
  - id: c
    text: "11"
    correct: true
    why: "Both buildings look at the same clock. The bakery makes it 1, and the post office adds 10 to that."
output: |
  11
```

```challenge
type: predict
run: false
id: companion-counts-all-tickets
verified: not-run
code: |
  class Ticket(val no: Int) {
      companion object {
          var made = 0

          fun next(): Ticket {
              made = made + 1
              return Ticket(made)
          }
      }
  }

  fun main() {
      val a = Ticket.next()
      val b = Ticket.next()
      println(a.no)
      println(b.no)
      println(Ticket.made)
  }
options:
  - id: a
    text: "1, 2, 2"
    correct: true
    why: "The clock bolted to the Ticket design counts every ticket. The first call makes it 1, the second makes it 2, and Ticket.made reads 2."
  - id: b
    text: "1, 1, 1"
    why: "You may have thought each ticket has its own counter. The companion's made belongs to the design, so all tickets share it."
  - id: c
    text: "1, 2, 0"
    why: "You may have thought made starts again at the end. Nothing resets it, so it keeps the count."
output: |
  1
  2
  2
```

```challenge
type: predict
run: false
id: shared-session-leaks
verified: not-run
code: |
  object Session {
      var user = "nobody"
  }

  fun login(name: String) {
      Session.user = name
  }

  fun main() {
      login("Ana")
      login("Ben")
      println(Session.user)
  }
options:
  - id: a
    text: "Ana"
    why: "You may have thought the first login keeps the clock. The second login sets the same clock again, so Ben replaces Ana."
  - id: b
    text: "Ben"
    correct: true
    why: "There is one Session on the square. Ana set it, then Ben set the same one, so only Ben is left. Two people cannot each have a Session this way."
  - id: c
    text: "nobody"
    why: "You may have thought each call works on a fresh clock that starts at nobody. The object is built once and keeps what was set."
output: |
  Ben
```

## Tricks to remember

- One clock on the square. Nobody orders a second.
- Use its name directly. Never write `Counter()`.
- A companion is a clock bolted to the design. Reach it through the class name.
- On your own machine, make an object with a property and a function. Change it from two different functions, then print it. Then make a class with a companion and call the companion function through the class name.

## Common mistakes

- **Thinking an object is made fresh each time you write its name.** A name often means "make a new one". For an object it means "go to the one that exists". Anything set earlier is still there.
- **Keeping one user's facts in an object.** It feels tidy to have one place for the session. Every user and every test then shares that one place, so Ben's login replaces Ana's. Keep per-user facts in objects you make for each user.
- **Trying to build another one.** The class-like body invites `Counter()`. An object is already built, and Kotlin refuses the call.
- **Putting a value in a companion when each object needs its own.** The companion is inside the class, so it feels per-object. It belongs to the class and is shared by every object. Put it in the main brackets or the body instead.

## Where this shows up in Android

The Android Room page has a note. It says that if your app runs in a single process, you should follow the singleton design pattern when instantiating an `AppDatabase` object, because each `RoomDatabase` instance is fairly expensive and you rarely need access to multiple instances within a single process. **Room** is the Android library for keeping an app's data in a database on the phone (general knowledge). This lesson does not explain the database class itself.

This lesson shows the tool for that one-of-something: an object, or a companion object that holds the single instance. The symptom of the opposite mistake is an app that opens the database again and again and gets slow. My own reasoning: the other danger is a shared object that holds one user's data, so a second user sees the first user's name after a sign-out.

## Say it in an interview

**What is an object declaration, and how does it relate to the singleton pattern?**
The Kotlin documentation says an object declaration lets you define a class and create its instance in a single step, which is useful for implementing singletons. You refer to it by its name. Nobody can call a constructor to make a second one.

**When is an object initialised?**
The documentation says an object declaration is initialised lazily, on first access, and that the initialisation is thread-safe. So the work happens the first time something uses it, and not when the app starts.

**What is a companion object?**
The documentation says it is an object declaration inside a class, marked with `companion`, for class-level functions and properties such as factory methods and constants. You call its members with the class name. It is initialised when the class is loaded.

**What goes wrong when an object holds changing state?**
My own reasoning: every part of the program shares it, so one user's data can leak into another user's session, and one test can leave data behind for the next. Concurrent code can also change it at the same time. The Room page gives a good reason for a singleton, a costly thing you want only once. The trade-off is that a shared thing is hard to reset.

## Recap

1. An `object` is one instance made on first use and shared by the whole program.
2. A companion object holds class-level functions and values, reached through the class name.
3. Anything kept in an object is shared by every user and every test.

## You can now

- Predict what an object's property shows after two different callers change it.
- Predict how a companion's counter behaves across several objects of its class.
- Predict which value remains when two logins set the same shared object.
