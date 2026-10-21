---
id: kotlin/classes-and-objects/getters-and-setters
title: How can a property run code when it is read or written?
status: shadow
difficulty: medium
order: 40
requires: [kotlin/classes-and-objects/private-and-public]
summary: A property can run your code each time it is read or written. See how that checks values and builds answers on demand.
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
- https://kotlinlang.org/docs/properties.html (custom getters and setters, backing fields, field, no backing field for a computed property)
- https://developer.android.com/kotlin/flow/stateflow-and-sharedflow (backing property comment)
- docs/practice/interview-kotlin.md seed for this leaf. The recursion claim is NOT on the properties page; the body labels it general knowledge.
Code not compiled: no shell in this session. Outputs hand-traced twice. Predict items are run: false, verified: not-run.
-->

## The question

A shop app lets a worker type a new price for a pen. Someone types -5, and the shelf label now shows a price of -5. How can the program refuse a bad price at the moment it is written? And how can the label say "pen: 5" when no such text was ever stored anywhere?

## Mental model

Think of a lobby with a shelf and a receptionist.

- The shelf slot is the place where a value is kept.
- A parcel brought to the desk is a new value that someone wants stored.
- The receptionist's check on the way in is a rule that can accept or send back the parcel.
- A visitor never walks to the shelf. They ask the receptionist, who fetches or stores for them.
- Some questions have no slot at all. The receptionist looks at other slots and writes a fresh answer each time she is asked.

**Where the analogy stops:** A real receptionist can forget a check or be away from the desk. In code the receptionist always runs, every time. Also, a visitor cannot tell from outside whether there is a receptionist, because asking for a price and writing a price look the same either way.

## Build the logic

### Notice

Kim hands in the parcel "price 5" for a pen. The receptionist checks that it is not below zero and puts it on the shelf. Next Kim hands in "price minus 3". The receptionist sends it back and the slot still holds 5. A visitor asks for the label. No slot is named label. The receptionist reads the name slot and the price slot and writes "pen: 5" on the spot. If the price changes, the next label changes too.

### Guess

What should happen when Kim hands in a parcel? Here are three ideas.

- A. It goes straight to the shelf, with no check.
- B. The receptionist keeps one copy on her desk and another on the shelf.
- C. The receptionist checks it first, then shelves it only if it passes.

Idea A lets a bad price in. Idea B makes two copies that can drift apart. Idea C is the answer.

Where should the answer to "label?" come from? Here are three ideas.

- A. From the other slots, worked out each time someone asks.
- B. From a slot that was filled once, when the item was made.
- C. From whatever the last visitor was told.

Idea B goes stale as soon as the price changes. Idea C mixes up visitors. Idea A is the answer.

The receptionist is told to put the parcel into "price". But "price" is the name visitors use, and using it means going through the receptionist. What happens? Here are three ideas.

- A. The parcel goes onto the shelf.
- B. The receptionist hands the parcel to herself, checks it again, hands it to herself again, and never stops.
- C. The parcel is refused.

Idea A forgets that the visitors' name leads back to her. Idea C has no source. Idea B is the answer, so she needs a different name for "the slot itself".

### Build

1. Keep the value in a slot on the shelf.
2. Put the receptionist between visitors and the slot, for writing, for reading, or for both.
3. On the way in, the receptionist runs a check and shelves the parcel only if it passes.
4. On the way out, she may hand over the slot as it is, or write a fresh answer from other slots.
5. A question with a fresh answer needs no slot of its own.
6. When she shelves a parcel, she must say "the slot itself", never the visitors' name.

### Name it

A **property** is a named value of an object. The Kotlin documentation says you can write your own **getter**, which is code that runs every time the property is read, and your own **setter**, which is code that runs every time you assign to it. It says the setter does not run during initialisation, which is the first value given on the property's own line. By convention the setter's parameter is called `value`.

A **backing field** is the slot that stores the value. The documentation says the compiler makes one when a value needs to be stored, and you reach it inside a getter or setter with the word **`field`**. It also says a property like `isEmpty`, whose value is worked out from `size` each time, has no backing field.

Writing the property's own name inside its own setter calls the setter again. My own reasoning, from general knowledge: this never ends, and the program fails. Use `field` for the slot.

This program has a setter that checks and a getter that works out an answer.

```kotlin
class Item(val name: String) {
    var price: Int = 0
        set(value) {
            if (value >= 0) {
                field = value
            }
        }

    val label: String
        get() = name + ": " + price
}

fun main() {
    val item = Item("pen")
    item.price = 5
    println(item.label)
    item.price = -3
    println(item.label)
}
```

It prints `pen: 5` and then `pen: 5` again. The first assignment passes the check. The second is sent back, so the slot still holds 5. The label is built each time from the name and the price.

Kotlin refuses to run this one, because `area` is a `val` with a getter, so there is no slot to write into.

```kotlin
// does not compile
class Box(val width: Int, val height: Int) {
    val area: Int
        get() = width * height
}

fun main() {
    val b = Box(2, 3)
    b.area = 20
}
```

## Predict the output

```challenge
type: predict
run: false
id: area-is-worked-out-each-time
verified: not-run
code: |
  class Box(var width: Int, var height: Int) {
      val area: Int
          get() = width * height
  }

  fun main() {
      val b = Box(2, 3)
      val first = b.area
      b.width = 10
      println(first)
      println(b.area)
  }
options:
  - id: a
    text: "6, then 6"
    why: "You may have thought area was stored when the box was made. It has no slot, so the receptionist works it out again at each question."
  - id: b
    text: "6, then 30"
    correct: true
    why: "The first question gets 6 and that answer is kept in first. After the width changes, the next question is worked out fresh as 10 times 3."
  - id: c
    text: "30, then 30"
    why: "You may have thought first would follow the box. It holds the number the receptionist gave at that moment, and a number does not change later."
output: |
  6
  30
```

```challenge
type: predict
run: false
id: starting-value-skips-the-check
verified: not-run
code: |
  class Item {
      var price: Int = 0
          set(value) {
              if (value > 0) {
                  field = value
              }
          }
  }

  fun main() {
      val item = Item()
      println(item.price)
      item.price = 5
      item.price = 0
      println(item.price)
  }
options:
  - id: a
    text: "0, then 0"
    why: "You may have thought the 5 was sent back because the first value fails the check. The check only runs on later assignments."
  - id: b
    text: "The program is refused, because the starting 0 fails the check"
    why: "You may have expected the receptionist to check the first value too. The documentation says the setter does not run during initialisation."
  - id: c
    text: "0, then 5"
    correct: true
    why: "The starting 0 goes on the shelf without a check. The 5 passes and the 0 that follows is sent back, so the slot keeps 5."
output: |
  0
  5
```

```challenge
type: predict
run: false
id: setter-runs-on-write-only
verified: not-run
code: |
  class Tag {
      var text: String = "a"
          set(value) {
              field = value + value
          }
  }

  fun main() {
      val t = Tag()
      t.text = "ab"
      println(t.text)
      println(t.text)
  }
options:
  - id: a
    text: "abab, then abab"
    correct: true
    why: "The parcel was changed once, on the way in. Reading it twice hands over the same shelved value both times."
  - id: b
    text: "ab, then ab"
    why: "You may have thought the receptionist only checks and never changes the parcel. This one stores a doubled version."
  - id: c
    text: "abab, then abababab"
    why: "You may have thought the setter runs on every read. It runs only when something is written."
output: |
  abab
  abab
```

## Tricks to remember

- Visitors ask the receptionist. They never touch the shelf.
- A setter runs when you write. A getter runs when you read.
- Inside the receptionist's rules, say `field` for the slot itself.
- On your own machine, write a property with a setter that refuses values below zero. Then check that a bad value is sent back, and that a good one is stored.

## Common mistakes

- **Writing the property's own name inside its setter.** It reads as "store the value". It sends the parcel back through the receptionist, which never ends (general knowledge). Write `field = value`.
- **Thinking a property is always a stored value.** Most are, so it feels true. A getter can work out its answer from other properties, with no slot behind it.
- **Expecting the setter to run on the starting value.** The line `= 0` looks like an assignment. The documentation says the setter does not run during initialisation, so check the starting value yourself.
- **Putting slow or surprising work in a setter.** A setter looks like a plain write, so a reader does not expect work. My own reasoning: keep accessors small, or use a named function so the reader sees that something happens.

## Where this shows up in Android

The Android StateFlow page shows a class that keeps a private `MutableStateFlow` called `_uiState` and a public `uiState`. A code comment there calls the private one a "Backing property to avoid state updates from other classes". This lesson does not explain `StateFlow`. A **backing property** here means a private property that keeps the value, with a public one next to it for others to read (the page's wording).

The symptom is a screen showing a value that nobody should have been able to set, such as a negative quantity. My own reasoning: a setter with a check, or a private property behind a public one, puts the rule in one place. When the bad value appears, you look at that one place.

## Say it in an interview

**How do custom getters and setters work on a Kotlin property?**
The Kotlin documentation says a custom getter runs every time the property is read, and a custom setter runs every time you assign to it, except during initialisation. You use them for checks, formatting or working a value out from other properties.

**What is a backing field, and when do you need `field`?**
It is the stored value behind a property. The documentation says you reach it with `field` inside a getter or setter. You need `field` when the setter must store the value, because the property's own name would call the setter again.

**Why does using the property name inside its own setter cause trouble?**
The name calls the setter, which calls the name, which calls the setter. My own reasoning, from general knowledge: this never ends and the program fails. The properties page does not state this, so I label it general knowledge.

**What can go wrong when setters do heavy work?**
My own reasoning: `item.price = 5` looks like a cheap write, so nobody expects a network call or a long calculation there. The result is slow screens and bugs that are hard to trace. The trade-off is a function with a clear name, which is longer to write but shows its cost.

## Recap

1. A getter runs each time a property is read, and a setter runs each time it is written.
2. A property whose value is worked out from others needs no stored slot.
3. Inside a setter, `field` is the stored slot, and the property's own name would call the setter again.

## You can now

- Predict the values a computed property gives before and after another property changes.
- Predict whether a setter's check runs on the starting value and on later assignments.
- Predict what a read returns after a setter changed the value on the way in.
