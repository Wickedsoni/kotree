---
id: kotlin/extensions-scope-and-delegates/lateinit
title: How can a property be filled in after the object is made?
status: live
difficulty: medium
order: 58
requires: [kotlin/classes-and-objects/constructors, kotlin/null-safety/nullable-types]
summary: A property can be promised now and filled in later. See what happens when something reads it too early.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [reserved-seat-timeline]
playground: false
fruit: false
upnext: false
---

## The question

A screen is built in one step, but one of its parts, a text box, only appears a moment later. The code on that screen wants to treat the text box as always there, with no "might be empty" mark on it. How can a property be filled in after the object is made? And what happens if some line reads it before it was filled in?

## Mental model

Think of a hall with a chair under a "reserved" sign.

- The chair is a property of the hall.
- The guest is the value that belongs in the property.
- The reserved sign is a promise: "a guest will sit here before anyone asks about the chair".
- A guest arriving is the moment the property is filled in.
- The usher pointing at the chair and asking "who sits here?" is a line of code reading the property.
- If the usher points before the guest arrives, the chair tips over.

**Where the analogy stops:** A real chair sits quietly empty until somebody notices. In Kotlin, every read of the property is checked, and a read before the guest arrives stops the program on the spot. And a real guest can leave, which makes the chair empty again. In Kotlin, once filled, the chair cannot be made empty again.

## See it move

```sim
id: reserved-seat-timeline
template: stepper
title: A hall with one reserved chair
intro: "The chair has a reserved sign and no guest yet. Two cards are ready to run, in any order you choose."
lines:
  - { id: lg, text: "Card: Guest arrives" }
  - { id: lu, text: "Card: Usher points at the chair" }
board:
  - { var: chair, label: The chair }
log: { columns: [Card run, What happens] }
vars: { chair: "empty, reserved sign", ask: "off" }
toggles: [ { id: ask, label: Ask if the chair is taken first, var: ask } ]
actions:
  - id: guest
    label: Guest arrives
    cases:
      - when: { var: { chair: "empty, reserved sign" } }
        do:
          - set: { chair: "Ana sits here" }
          - light: [ lg ]
          - row: [ "Guest arrives", "Ana sits in the chair" ]
          - say: "The guest, Ana, arrives and sits in the reserved chair."
    refuse: "The chair is already filled, or the program has stopped. Press Clear the hall to start again."
  - id: usher
    label: Usher points at the chair
    cases:
      - when: { var: { chair: "Ana sits here" } }
        do:
          - light: [ lu ]
          - row: [ "Usher points", "The chair answers: Ana" ]
          - say: "The usher points at the chair, and the chair answers: Ana. The promise held."
      - when: { var: { chair: "empty, reserved sign", ask: "on" } }
        do:
          - light: [ lu ]
          - row: [ "Usher asks first", "Not taken yet, so nothing tips" ]
          - say: "The usher asks first whether the chair is taken. It is not taken yet, so the usher does not point, and nothing tips over."
      - when: { var: { chair: "empty, reserved sign" } }
        do:
          - set: { chair: "tipped over, program stopped" }
          - light: [ lu ]
          - row: [ "Usher points", "The chair tips over" ]
          - say: "The usher points at an empty chair before any guest arrived. The chair tips over, and the program stops."
    refuse: "The program has stopped. Press Clear the hall to start again."
  - id: clear
    label: Clear the hall
    do:
      - set: { chair: "empty, reserved sign" }
      - light: []
      - row: [ "Clear the hall", "A new hall, the chair is empty again" ]
      - say: "A new hall is set up. The chair is empty again, with its reserved sign."
script: [ guest, usher, clear, usher ]
try:
  - text: "Press \"Guest arrives\", then \"Usher points at the chair\". The guest is seated before anyone asks, so the chair answers."
    done: [ guest, usher ]
  - text: "Press \"Clear the hall\", then press \"Usher points at the chair\" first and \"Guest arrives\" after it. The chair tips over, and the guest is too late."
    done: [ clear, usher, guest ]
  - text: "Press \"Clear the hall\" again, leave out the guest card, and press only \"Usher points at the chair\". With no guest card, the chair tips over too."
    done: [ clear, usher ]
  - text: "(Optional) Switch on \"Ask if the chair is taken first\". The usher asks first, so the chair does not tip."
    done: [ "ask=on" ]
    optional: true
```

**What you should notice:** The chair works only when the guest arrives before the usher points, and the check in step 4 lets the usher skip pointing instead of tipping the chair.

## Build the logic

### Notice

The ticket office builds a chair now, but the guest's name is only known when the person arrives. The usher will ask "who sits here?" later, many times. The office does not want every usher to first check "is this chair empty?", because for most of the evening a guest will be there.

### Guess

How can the office handle a chair that is filled later? Here are three ideas.

- A. Mark the chair "might be empty" and make every usher check it first.
- B. Put a made-up guest, such as an empty name, in the chair until the real one arrives.
- C. Put up a reserved sign that promises a guest will come, and let the program stop loudly if the promise breaks.

Idea A is honest, and it is the right choice when an empty chair is a normal case, but it makes every reader do the check. Idea B hides the truth: an empty name looks like a real guest. Idea C moves the check from every reader to a single place, the promise, and it is the answer here.

### Build

1. Declare the chair without a guest, and say it is a reserved chair.
2. The chair must be changeable, because the guest arrives after the chair exists.
3. Never mark the chair "might be empty". The point is that readers can treat it as always filled.
4. When the guest arrives, fill the chair once. After that, readers see the guest.
5. If a reader asks before the guest arrives, the program stops with a clear note about which chair was empty.
6. If a reader wants to ask politely first, there is a way to ask "is the chair taken yet?" without tipping it.

### Name it

**`lateinit`** is a word you put before a `var` property to promise that it will be filled in later. The Kotlin documentation says it is for cases where you cannot fill a property in the constructor. One case is a helper object that is handed in from outside, which is called dependency injection (general knowledge). Another is a unit test that fills it in during its set-up step.

The documentation lists the rules. The property must be a `var`, so it can be changed after the object is made. It may be a top-level property, a local variable or a property in the class body. It cannot be in the primary constructor and cannot have a custom getter or setter. It must not be nullable, and it must not be a primitive type.

If you read it before it is filled, Kotlin throws an **`UninitializedPropertyAccessException`**, which is an exception, a signal that stops the program. The documentation shows the message as `lateinit property report has not been initialized`. To ask politely, the documentation says to use **`isInitialized`** on a reference to the property, written `this::guest.isInitialized`. It works only where you can already reach the property, for example inside the same class.

```kotlin
class Seat {
    lateinit var guest: String

    fun status() {
        if (this::guest.isInitialized) {
            println("taken by " + guest)
        } else {
            println("empty")
        }
    }
}

fun main() {
    val seat = Seat()
    seat.status()
    seat.guest = "Ana"
    seat.status()
}
```

It prints `empty` and then `taken by Ana`. The first call asked politely and the chair was not taken yet. After the guest was set, the second call found the guest.

Kotlin refuses to run this one, because a reserved chair has to be changeable.

```kotlin
// does not compile
class Seat {
    lateinit val guest: String
}

fun main() {
    println(Seat())
}
```

## Predict the output

```challenge
type: predict
run: false
id: usher-points-too-early
verified: kotlin-2.4.21
code: |
  class Seat {
      lateinit var guest: String
  }

  fun main() {
      val seat = Seat()
      println("Start")
      println(seat.guest)
      println("End")
  }
options:
  - id: a
    text: "Start, null, End"
    why: "You may have expected the chair to hold the word null. A reserved chair is not marked as possibly empty, so there is no null to print."
  - id: b
    text: "Start, then the program stops on the next line"
    correct: true
    why: "The usher points at the chair before any guest arrived, so the chair tips over. Start was printed first, and End is never reached."
  - id: c
    text: "Start, a blank line, End"
    why: "You may have expected an empty name. Nobody put a name in the chair, so there is nothing to print and the program stops."
output: |
  Start
```

```challenge
type: predict
run: false
id: guest-can-change
verified: kotlin-2.4.21
code: |
  class Seat {
      lateinit var guest: String
  }

  fun main() {
      val seat = Seat()
      seat.guest = "Ana"
      println(seat.guest)
      seat.guest = "Raj"
      println(seat.guest)
  }
options:
  - id: a
    text: "Ana, then the program stops, because a reserved chair can be filled only once"
    why: "You may have thought the promise is used up after one guest. The chair is a changeable property, so a new guest can take it."
  - id: b
    text: "Ana and Ana"
    why: "You may have thought the second guest is ignored. The chair takes the new value."
  - id: c
    text: "Ana and Raj"
    correct: true
    why: "The first guest filled the chair and the usher read Ana. The chair is changeable, so Raj took it and the next read gives Raj."
output: |
  Ana
  Raj
```

```challenge
type: predict
run: false
id: usher-in-the-builder
verified: kotlin-2.4.21
code: |
  class Seat {
      lateinit var guest: String
      init {
          println("Building")
          println(guest)
      }
  }

  fun main() {
      Seat()
      println("Done")
  }
options:
  - id: a
    text: "Building, then the program stops"
    correct: true
    why: "The init block runs while the chair is being built, before any guest could arrive. The usher in the builder points at an empty chair."
  - id: b
    text: "Building, null, Done"
    why: "You may have expected null to show up for an empty chair. A reserved chair has no null in it."
  - id: c
    text: "Building, Done"
    why: "You may have thought the empty chair is ignored. A read of the chair is always checked, even inside init."
output: |
  Building
```

## Tricks to remember

- Reserved sign means "filled later, and I promise".
- It is always `lateinit var`, never `val`, and never with a `?`.
- Read early and the chair tips over. Ask `isInitialized` first to be polite.
- If an empty chair is a normal case, use a nullable type instead.

## Common mistakes

- **Thinking `lateinit` is a safe way to avoid nulls.** The type has no question mark, so it feels safe. The check has only moved from the compiler to the moment of reading, and a bad read stops the program. Use a nullable type when "not there yet" is a normal state.
- **Reading the property in `init` before it is assigned.** The `init` block is in the class, so the line feels like it can see the chair. It runs while the object is being built, before anyone could fill the chair. Move the read to after the guest arrives.
- **Writing `lateinit val`.** A guest arrives later, but `val` feels safer. A `val` cannot be changed after it is set, so a late arrival makes no sense. The compiler refuses. Use `var`.
- **Putting `lateinit` on an `Int` or other primitive type.** It feels like any other property. The documentation says the property must not be a primitive type. Give an `Int` a starting value instead.

## Where this shows up in Android

Picture a screen that crashes the moment it opens, and the note says `lateinit property statusTextView has not been initialized`. A **Fragment** is one part of a screen, with its own steps that Android calls as it starts and stops (general knowledge). The Android Kotlin patterns page shows a `LoginFragment` with a `lateinit` text view that is assigned inside `onViewCreated`, the step Android calls once the part's views exist (general knowledge). The page says that reading a `lateinit` property before it is set throws `UninitializedPropertyAccessException`. A code line that reads the text view before `onViewCreated` has run is the usher pointing at an empty chair.

Another page shows an Activity, which is one full screen of an app (general knowledge), using `lateinit var binding`. For a Fragment it uses a nullable value cleared in `onDestroyView`, the step Android calls when the part's views go away. The reason is that fragments outlive their views. My own reasoning: a `lateinit` property cannot be set back to empty, so a Fragment needs a nullable property when the guest has to leave.

## Say it in an interview

**What is `lateinit`, and what problem does it solve?**
It lets a non-null `var` property be declared without a value and filled in later. The Kotlin documentation says it fits cases like dependency injection or a unit test's set-up step, where the constructor is not a convenient place to fill the property. The point is that readers can use it as a normal non-null property, with no null checks.

**Which types can be `lateinit`, and why not an `Int`?**
Only `var` properties that are non-null and not primitive types, as the documentation says. The reason for the primitive rule is not on the Kotlin page I read. General knowledge: the "not yet set" state is stored as a null, and a primitive such as `Int` has no null to store.

**What happens if you read a `lateinit` property too early?**
It throws `UninitializedPropertyAccessException`. The message names the property. You can check first with `this::name.isInitialized`, which works only where you can reach the property.

**When would you not use `lateinit`?**
When "not there yet" is a normal state, use a nullable type and handle null. My own reasoning: `lateinit` moves the failure from compile time to run time, so use it only when you can promise the order, for example when a framework fills the property before your code runs. In a Fragment, the Android view binding page uses a nullable property cleared in `onDestroyView`, because fragments outlive their views.

## Recap

1. `lateinit` promises that a non-null `var` will be filled in later.
2. Reading it too early stops the program with an `UninitializedPropertyAccessException`.
3. `isInitialized` asks whether it has been filled in, without stopping the program.

## You can now

- Predict what a program prints when it reads a `lateinit` property before it is set.
- Predict that a `lateinit var` can be assigned again after it is set.
- Spot a read of a `lateinit` property inside `init`, before it is assigned.
