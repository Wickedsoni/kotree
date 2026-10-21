---
id: kotlin/classes-and-objects/member-functions
title: How does an object do things with its own data?
status: live
difficulty: easy
order: 38
requires: [kotlin/classes-and-objects/constructors]
summary: A function written inside a class works on the one object you call it on. See why two objects never mix up their data.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---

## The question

A bank keeps two passbooks, one for Ada and one for Ben. A customer says, "Add 50 to Ada's book." Only Ada's total should grow, and Ben's must stay the same. The steps for adding money are written once. How does one set of steps know which book to change?

## Mental model

Think of a bank teller who works only on the passbook you hand over.

- The teller's instruction sheet is the action, such as "add money". There is one sheet for all customers.
- Each passbook is one object. It has its own page with a balance on it.
- The amount you say aloud, such as "50", is an input to the action.
- Handing the passbook across the counter says which book to work on.
- The teller reads the balance from the book in their hand and writes the new balance back into the same book.

**Where the analogy stops:** A real teller remembers things between customers and might mix two books up. In code, an action keeps nothing of its own. It touches only the object it was called on, and the inputs you gave it.

## Build the logic

### Notice

Ada's book shows 100. Ben's book shows 20. The customer hands over Ada's book and says "add 50". The teller follows the sheet: read the balance in the book, add 50, write it back. Ada's book now shows 150. Ben's book still shows 20, because it never left the shelf.

### Guess

How does the sheet know which book to change? Here are three ideas.

- A. The sheet changes the book that was handed over, and no other.
- B. The sheet changes every book in the bank.
- C. The sheet changes a master book kept at the back of the bank.

Idea B is the common worry: the sheet is shared, so it must change everything. The sheet is shared, but the books are not. Idea C would mean the passbooks hold nothing. Idea A is the answer.

### Build

1. Write the steps for "add money" once, on the sheet that belongs to the design of the book.
2. Inside the steps, "the balance" always means the balance on the book in hand.
3. To use the steps, name the book first, then say the action and its input.
4. Each time the steps run, they work on exactly one book.
5. Another book can be handed over later. Its balance is separate.

### Name it

A **member function** is a function defined inside a class. The Kotlin documentation says you call it by writing the instance name, then a dot, then the function name. The object before the dot is the passbook you hand over. Inside the function, the name of a property means that object's property. The documentation says `this` refers to the current object of the class, and you can write `this.name` if you want to say it out loud.

```kotlin
class Account(val owner: String, var balance: Int) {
    fun deposit(amount: Int) {
        balance = balance + amount
    }
}

fun main() {
    val ada = Account("Ada", 100)
    val ben = Account("Ben", 20)
    ada.deposit(50)
    println(ada.owner + " " + ada.balance)
    println(ben.owner + " " + ben.balance)
}
```

It prints `Ada 150` and then `Ben 20`. The call `ada.deposit(50)` handed over Ada's book, so only Ada's balance changed.

A member function can call another member function of the same object by its name. The next program adds twice, then once on a different object.

```kotlin
class Counter(var count: Int) {
    fun add(step: Int) {
        count = count + step
    }

    fun addTwice(step: Int) {
        add(step)
        add(step)
    }
}

fun main() {
    val a = Counter(0)
    val b = Counter(10)
    a.addTwice(3)
    b.add(1)
    println(a.count)
    println(b.count)
}
```

It prints `6` and then `11`. Both calls to `add` inside `addTwice` work on `a`, the object `addTwice` was called on.

## Predict the output

```challenge
type: predict
run: false
id: one-deposit-two-accounts
verified: kotlin-2.4.21
code: |
  class Account(val owner: String, var balance: Int) {
      fun deposit(amount: Int) {
          balance = balance + amount
      }
  }

  fun main() {
      val ada = Account("Ada", 100)
      val ben = Account("Ben", 20)
      ada.deposit(50)
      println(ada.balance)
      println(ben.balance)
  }
options:
  - id: a
    text: "150 and 70"
    why: "You may have thought the sheet is shared, so it changes every book. The sheet is shared, but the call works only on the book handed over."
  - id: b
    text: "150 and 20"
    correct: true
    why: "Ada's book was handed over, so the teller changed her balance. Ben's book stayed on the shelf."
  - id: c
    text: "100 and 20"
    why: "You may have thought the teller only reads the book. The line balance = balance + amount writes the new total back."
output: |
  150
  20
```

```challenge
type: predict
run: false
id: sum-never-written-back
verified: kotlin-2.4.21
code: |
  class Account(var balance: Int) {
      fun deposit(amount: Int) {
          val newBalance = balance + amount
      }
  }

  fun main() {
      val acc = Account(100)
      acc.deposit(50)
      println(acc.balance)
  }
options:
  - id: a
    text: "150"
    why: "You may have thought working out the total changes the book. The teller wrote the total on a scrap of paper and threw it away."
  - id: b
    text: "50"
    why: "You may have thought the amount replaces the balance. Nothing was written to the book, so the old balance stays."
  - id: c
    text: "100"
    correct: true
    why: "newBalance is a note on the teller's scrap paper. Nobody wrote it into the book, so the balance is still 100."
output: |
  100
```

```challenge
type: predict
run: false
id: add-twice-on-one-counter
verified: kotlin-2.4.21
code: |
  class Counter(var count: Int) {
      fun add(step: Int) {
          count = count + step
      }

      fun addTwice(step: Int) {
          add(step)
          add(step)
      }
  }

  fun main() {
      val a = Counter(0)
      val b = Counter(10)
      a.addTwice(3)
      b.add(1)
      println(a.count)
      println(b.count)
  }
options:
  - id: a
    text: "6 and 11"
    correct: true
    why: "addTwice was called on a, so both adds land in a's book: 0 + 3 + 3. b's book started at 10 and got one add of 1."
  - id: b
    text: "6 and 1"
    why: "You may have forgotten that b started at 10, not 0. Its book already showed 10 before the add."
  - id: c
    text: "3 and 11"
    why: "You may have thought addTwice adds once. It calls add two times, and both calls use the same book."
output: |
  6
  11
```

## Tricks to remember

- The object before the dot is the book on the counter.
- One instruction sheet, many books.
- Working something out is not the same as writing it down. Store the result, or it is gone.
- Inside a member function, a property name means this object's property.

## Common mistakes

- **Thinking a member function changes every object.** The instructions are written once, so it feels like they act on all books. Each call works on the one object before the dot.
- **Working out the new total but not storing it.** The line `val newBalance = balance + amount` looks like it does the job. It only makes a note that disappears when the function ends. Assign the result to the property.
- **Calling the function on the class name.** Writing `Account.deposit(50)` feels like asking the bank in general. A member function needs a book handed over, so call it on an object such as `ada`.

## Where this shows up in Android

The Android activity lifecycle page shows a class named `ExampleActivity` with a function `onCreate`. The page says you must implement this callback, which fires when the system first creates the activity. `onCreate` is a member function: it runs on one activity object, and it can read and change the data stored in that object (general knowledge). My own reasoning: if a screen shows unexpected data after it is opened twice, ask which object the function was called on. That is the question this lesson trains.

## Say it in an interview

**How do you declare and call a member function in Kotlin?**
You write a `fun` inside the class body. The Kotlin documentation says you call it by writing the instance name, a dot, and the function name. For example, `ada.deposit(50)`.

**How does a member function know which object it works on?**
It works on the object before the dot. The documentation says that in a member of a class, `this` refers to the current object. Writing a property name on its own inside the function means the property of that object.

**If two objects share one function, why do they not mix up their data?**
The function is written once, but each call is tied to one object, and each object has its own property values. This is general knowledge: the object is passed to the function behind the scenes. Both of our accounts used the same `deposit`, and only Ada's balance changed.

**Why put a function inside a class instead of a separate function that takes the object?**
My own reasoning: the rules for changing the data sit next to the data, so a reader finds them in one place. The trade-off is that the class grows as more actions are added.

## Recap

1. A member function is a function written inside a class.
2. It works on the one object you call it on.
3. A result you work out must be stored in a property, or it is lost.

## You can now

- Predict which object's data changes when a member function is called on one of two objects.
- Spot a member function that works out a new value but never stores it.
- Explain what `ada.deposit(50)` does to Ada's account and to Ben's.
