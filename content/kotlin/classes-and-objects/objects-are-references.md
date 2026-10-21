---
id: kotlin/classes-and-objects/objects-are-references
title: Why does changing one variable change another?
status: live
difficulty: hard
order: 41
requires: [kotlin/classes-and-objects/constructors]
summary: Two variables can lead to one object. See why a change through one shows in the other, and how to make a real copy.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [two-keys-one-flat]
playground: false
fruit: false
upnext: false
---


## The question

A settings screen has a Cancel button. Before the user edits anything, the program saves a "backup" of the settings. The user switches the theme to dark and then taps Cancel, but the screen stays dark. The backup says dark too, and nobody touched it. How did the backup change on its own?

## Mental model

Think of flats and keys.

- A flat is one **object**, a thing the program makes once in memory. It exists once, at one street address.
- The sofa inside is one of its **properties**, a named value stored inside the object. It can be repainted.
- A key is a **variable**, a name that holds a reference. It is not the flat. It only lets you reach one flat.
- Cutting a spare key is assigning one variable to another. The flat does not get copied.
- Asking a builder for a new flat is calling a **constructor**, the code that makes a new object. Now there are two flats.
- Handing your key to a friend who does a job for you is passing an object to a function. The friend gets a spare key of their own.
- A key marked "fixed" can never be swapped for a key to another flat. That is a read-only variable, declared with `val`.

**Where the analogy stops:** A real key is a thing you can see, and it costs time and money to cut. In code, a key costs nothing to copy, and you never see the street address. Also, nothing in code stops you from holding a hundred spare keys by accident.

## See it move

```sim
id: two-keys-one-flat
template: zones
title: Keys, flats and a sofa
intro: Ana holds a key to flat 1, which has a red sofa. A second flat has not been built yet.
zones:
  - { id: flat-1, label: Flat 1 }
  - { id: flat-2, label: Flat 2 }
items:
  - { id: key-ana, label: "Ana's key", at: flat-1 }
vars: { sofa1: red, sofa2: "no flat yet", built: 0 }
show:
  - { var: sofa1, label: Sofa in flat 1 }
  - { var: sofa2, label: Sofa in flat 2 }
actions:
  - id: cut
    label: Cut a spare key
    do:
      - spawn: { id: key-ben, label: "Ben's key", at: flat-1 }
      - say: "A spare key was cut. Ben's key opens the same flat 1 as Ana's key. The flat was not copied."
  - id: ben-paints
    label: Ben repaints the sofa
    cases:
      - when: { at: { key-ben: flat-1 } }
        do:
          - set: { sofa1: blue }
          - say: "Ben used his key to flat 1 and painted the sofa blue. Ana's key leads to the same flat, so her sofa is blue too."
      - when: { at: { key-ben: flat-2 } }
        do:
          - set: { sofa2: blue }
          - say: "Ben used his key to flat 2 and painted that sofa blue. Flat 1 is not touched."
    refuse: "Ben has no key yet. Press Cut a spare key first."
  - id: ana-paints
    label: Ana repaints the sofa
    do:
      - set: { sofa1: green }
      - say: "Ana used her key to flat 1 and painted the sofa green. Every key to flat 1 shows green. Flat 2 is not touched."
  - id: build
    label: Build a second flat
    cases:
      - when: { var: { built: 0 } }
        do:
          - set: { built: 1, sofa2: grey }
          - label: { flat-2: "Flat 2 (new)" }
          - say: "A new flat was built with a grey sofa. It is a second object, and no key leads to it yet."
    refuse: "The second flat is already built."
  - id: numbers
    label: Show the door numbers
    cases:
      - when: { at: { key-ben: flat-2 } }
        do:
          - label: { key-ana: "Ana's key: door 1", key-ben: "Ben's key: door 2" }
          - say: "Ana's key opens door 1 and Ben's key opens door 2. Two different flats."
      - when: { at: { key-ben: flat-1 } }
        do:
          - label: { key-ana: "Ana's key: door 1", key-ben: "Ben's key: door 1" }
          - say: "Both keys show door 1. One flat, two keys."
    refuse: "Only Ana's key exists so far. It opens door 1."
drops:
  - item: key-ben
    to: flat-2
    when: { var: { built: 1 } }
    do:
      - say: "Ben gave back his spare key and got a key to flat 2. It leads to a different flat."
    refuse: "There is no second flat yet. Press Build a second flat first."
  - item: key-ben
    to: flat-1
    do:
      - say: "Ben's key leads to flat 1 again."
script: [cut, ben-paints, build, "key-ben>flat-2", ben-paints, ana-paints, numbers]
try:
  - text: "Press \"Cut a spare key\" so Ben gets a copy of Ana's key. Then press \"Ben repaints the sofa\"."
    done: [cut, ben-paints]
  - text: "Press \"Build a second flat\" and give Ben a key to it. Repaint again from Ben's side."
    done: [build, "key-ben>flat-2", ben-paints]
  - text: "Press \"Ana repaints the sofa\" and watch which flats change."
    done: [ana-paints]
  - text: "(Optional reveal) Press \"Show the door numbers\" and compare the numbers on the two keys with their places before and after step 2."
    done: [numbers]
    optional: true
```

**What you should notice:** A repaint through any key shows through every key to that flat, and a key to a second flat leaves the first flat alone.

## Build the logic

### Notice

Ana owns a flat with a red sofa. She cuts a spare key and gives it to Ben. Ben lets himself in and paints the sofa blue. Ana comes home, opens the door with her own key, and finds a blue sofa.

Later, Ben throws his spare key away and gets a key to a different flat. He paints that sofa green. Ana's sofa is still blue.

### Guess

Ben has a spare copy of Ana's key and repaints the sofa blue. What does Ana find when she opens her door? Here are three ideas.

- A. A blue sofa, because there is one flat and two keys.
- B. A red sofa, because Ben's key opens his own copy of the flat.
- C. A locked door, because two keys cannot work at once.

Idea B feels right if you picture photocopying a paper. A key is not a paper copy of the flat. It is a way to get in. Idea C has no source: keys do not block each other. Idea A is the answer.

Now Ben swaps his key for a key to a different flat and repaints that sofa. Does Ana's sofa change?

- A. Yes, the two flats are linked because Ben once had her key.
- B. No, he is now standing in another flat.

Idea A feels natural because the key came from Ana. But Ben's old key is gone. Idea B is the answer.

### Build

1. Every flat exists once. Two keys do not make two flats.
2. A key does not hold the flat. It tells you how to reach one.
3. Handing a key to someone cuts a spare. It does not build anything.
4. Anyone with a key to a flat can repaint its sofa. Everyone with a key to that flat sees the new colour.
5. Swapping your own key for a key to another flat changes only what you can reach. Nobody else is affected.
6. To get a flat that is independent, ask the builder for a second flat and have the same sofa put in it. A new flat and an old flat do not share anything.
7. A friend who works for you gets a spare of your key. The friend may repaint your sofa. The friend may not swap your own key for another.
8. A key marked "fixed" cannot be swapped. The sofa behind that key can still be repainted.

### Name it

A variable that holds an object really holds a **reference**, which is a way to reach the object and not the object itself. A variable declared with `var` can be pointed at another object later. One declared with `val` cannot. The Kotlin documentation talks about "two references pointing to the same object". Writing `val b = a` copies the reference, so `a` and `b` reach the same object.

Here is the spare-key case.

```kotlin
class Flat(var sofa: String)

fun main() {
    val a = Flat("red")
    val b = a
    b.sofa = "blue"
    println(a.sofa)
    println(b.sofa)
}
```

It prints `blue` and `blue`. Only one `Flat` was ever made. The line `val b = a` cut a spare key. Then `b.sofa = "blue"` repainted the one flat, and both variables show it.

The Kotlin documentation says that when you pass an object to a function, the compiler passes a copy of the reference. The copy points to the same object, so the function can change the object's properties. The documentation also says function parameters are read-only inside the function. That means a function cannot point your variable at a different object.

```kotlin
class Flat(var sofa: String)

fun repaint(flat: Flat) {
    flat.sofa = "green"
}

fun main() {
    val mine = Flat("red")
    repaint(mine)
    println(mine.sofa)
}
```

It prints `green`. The variable `mine` is a `val`, so its key is fixed. The flat behind it still changed. A `val` stops you from swapping the key. It does not freeze the object.

Kotlin refuses to run this one, because the function tries to swap the key it was handed.

```kotlin
// does not compile
class Flat(var sofa: String)

fun swap(flat: Flat) {
    flat = Flat("green")
}

fun main() {
    val mine = Flat("red")
    swap(mine)
    println(mine.sofa)
}
```

To get a real copy, build a new object. Here the constructor is the builder.

```kotlin
class Flat(var sofa: String)

fun main() {
    val a = Flat("red")
    var b = a
    println(b.sofa)
    b = Flat(a.sofa)
    b.sofa = "blue"
    println(a.sofa)
    println(b.sofa)
}
```

It prints `red`, then `red`, then `blue`. At first `b` is a spare key to the flat that `a` opens. The line `b = Flat(a.sofa)` gives `b` a key to a second flat, which starts with the same sofa. Repainting through `b` now changes only the second flat.

## Predict the output

```challenge
type: predict
run: false
id: spare-key-and-second-flat
verified: kotlin-2.4.21
code: |
  class Flat(var sofa: String)

  fun main() {
      val a = Flat("red")
      val b = a
      val c = Flat("red")
      b.sofa = "blue"
      println(a.sofa + " " + c.sofa)
  }
options:
  - id: a
    text: "red red"
    why: "You may have thought val b = a builds a second flat. It only cuts a spare key, so the repaint shows through a."
  - id: b
    text: "blue red"
    correct: true
    why: "b is a spare key to the flat that a opens, so the repaint shows through a. c is a key to a second flat that nobody touched."
  - id: c
    text: "blue blue"
    why: "You may have thought two flats with the same red sofa are joined. Each flat is separate unless one key is copied."
output: |
  blue red
```

```challenge
type: predict
run: false
id: swap-the-key-then-repaint
verified: kotlin-2.4.21
code: |
  class Flat(var sofa: String)

  fun main() {
      val a = Flat("red")
      var b = a
      b.sofa = "blue"
      b = Flat("green")
      b.sofa = "pink"
      println(a.sofa + " " + b.sofa)
  }
options:
  - id: a
    text: "red pink"
    why: "You may have thought the first repaint only touched b. At that moment b and a opened the same flat, so a shows blue."
  - id: b
    text: "pink pink"
    why: "You may have thought b stays tied to a for ever. After b = Flat(\"green\") it opens a different flat."
  - id: c
    text: "blue pink"
    correct: true
    why: "The first repaint went through a shared key, so a's flat is blue. Then b was given a key to a new flat, and the pink went onto that one."
output: |
  blue pink
```

```challenge
type: predict
run: false
id: backup-is-the-same-flat
verified: kotlin-2.4.21
code: |
  class Settings(var theme: String)

  fun makeBackup(current: Settings): Settings {
      return current
  }

  fun main() {
      val current = Settings("light")
      val backup = makeBackup(current)
      current.theme = "dark"
      println(backup.theme)
  }
options:
  - id: a
    text: "dark"
    correct: true
    why: "makeBackup hands back the key it was given. It builds no new flat, so backup and current reach the same settings."
  - id: b
    text: "light"
    why: "You may have thought a function called makeBackup builds a copy. A name does not build anything. Only a constructor call does."
  - id: c
    text: "The program is refused, because current is a val"
    why: "You may have thought val freezes the settings. It only fixes the key, so the theme can still change."
output: |
  dark
```

## Tricks to remember

- A variable is a key, not the flat.
- Assigning cuts a spare key. Only a constructor call builds a flat.
- `val` fixes the key. The sofa can still be repainted.
- On your own machine, make a class with one `var`, point two variables at one object, change it through the second, and print through the first. Then repeat with a second object built by the constructor.

## Common mistakes

- **Treating `val b = a` as a copy.** It reads like "make b from a". It cuts a spare key and builds no flat. If you need an independent object, call the constructor and pass in the values.
- **Thinking `val` freezes the object.** The word sounds like "locked". It only fixes which flat the variable reaches. The properties behind it can still change if they are `var`.
- **Expecting a function to swap your variable.** Your friend holds a spare of your key, so it feels like they hold the real one. The documentation says parameters are read-only, so Kotlin refuses. My own reasoning: return the new object and store it in your own variable.
- **Thinking a hand-made copy copies everything.** It feels right because the new flat looks complete. But if the flat holds keys to other objects, the new flat gets spares of those keys, so those objects are still shared. The documentation says the same of a data class `copy()`: it makes a shallow copy (a copy of the outer object only, which still shares what it points to), and references to other objects are shared. A data class is a class made for holding data (general knowledge). What to do instead: copy the inner objects too, or build them fresh.

## Where this shows up in Android

Picture a screen that shows a list of items. The code keeps the list in Compose **state** (the values the screen shows and reacts to), and then adds an item to that same list. The screen does not change, or it shows old items. This lesson explains why: the flat was repainted, but no one told the screen.

**Jetpack Compose** is the Android toolkit for describing screens in Kotlin code (general knowledge). The Android Compose state page has a caution. It says that using **mutable** objects (objects that can be changed in place), such as `ArrayList<T>` or `mutableListOf()`, as state in Compose causes users to see incorrect or stale data. It says Compose does not observe those objects, so a change to them does not trigger a **redraw** (drawing the screen again). It recommends an **observable data holder** (a container that Compose watches for changes) such as `State<List<T>>` with an **immutable** (cannot be changed) `listOf()`.

The link to this lesson is my own reasoning: when the list is changed in place, every key still reaches the same list, so nothing visible about the key has changed.

## Say it in an interview

**What happens when you write `val b = a` for an object?**
It copies the reference, not the object. Both variables now reach the same object, so a change through one shows through the other. The Kotlin equality page describes this as "two references pointing to the same object".

**If you pass an object to a function and the function changes one of its properties, does the caller see it?**
Yes. The Kotlin functions page says the compiler passes a copy of the reference, and the copy points to the same object. So the function can change the object's state. The page also says parameters are read-only, so the function cannot point the caller's variable at a different object.

**Why can two variables point at the same object, and how do you get an independent copy?**
Handing out a reference is cheap, and it lets two parts of a program share one thing (my own reasoning). The cost is that a change made by one part is seen by the other. For an independent copy, build a new object, for example with the constructor. For a data class, `copy()` does it, but the documentation says the copy is shallow (a copy of the outer object only): references to other objects inside are shared.

**What can go wrong when several coroutines share one mutable object?**
A **coroutine** is a task that can run alongside other tasks (general knowledge). The Kotlin documentation says coroutines can run in parallel and that the main problem is synchronising access to **shared mutable state** (one changeable object that several tasks use). It lists **thread-safe** data structures (safe when several threads use them at once), keeping all access on one thread, and a **mutex** (a lock so only one task at a time can change the data). My own reasoning: another way out is to give each task its own copy, or to share an object that nobody changes, so there is nothing to fight over.

## Recap

1. A variable holds a reference to an object, not the object itself.
2. Assigning one variable to another copies the reference, so both reach the same object.
3. To get an independent object, build a new one with the constructor.

## You can now

- Predict what two variables print after a property is changed through one of them.
- Predict which variables still share an object after one of them is given a new object.
- Spot a function that returns the same object when the author meant a copy.
