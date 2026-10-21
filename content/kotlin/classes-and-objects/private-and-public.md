---
id: kotlin/classes-and-objects/private-and-public
title: Why hide some parts of a class with private?
status: shadow
difficulty: easy
order: 39
requires: [kotlin/classes-and-objects/member-functions]
summary: Private keeps a property or function usable only inside its own class. See how that stops outside code breaking the class's rules.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/visibility-modifiers.html ; https://developer.android.com/topic/architecture/ui-layer/state-production -->
## The question

A shop keeps its cash in a drawer behind the counter. Customers can ask for change or pay a bill, but they cannot open the drawer and take notes out. Why does the shop do it that way? And how could a program give its data the same protection?

## Mental model

Think of a cash drawer behind a shop counter.

- The customers are the rest of the program, outside the class.
- The counter is the edge of the class.
- The teller is a function you are allowed to call from outside.
- The cash drawer is a property that only the teller may touch.
- The teller's rules, such as "no negative amounts", are checks that run whenever someone asks.
- A tidy-up task the teller does in the back room is a helper that customers never see.

**Where the analogy stops:** A real shop hides the drawer to stop theft. In code, hiding is about keeping the class's rules in one place. The compiler checks it before the program runs, and it is not a lock against attackers.

## Build the logic

### Notice

Ada's shop holds 100 in the drawer. A customer says, "Add 50." The teller checks that 50 is above zero, puts the notes in and the drawer holds 150. A second customer says, "Add minus 30." The teller refuses, because the rule says amounts must be above zero. The drawer still holds 150. Now a third customer reaches over the counter, opens the drawer and sets it to minus 500. No rule ran.

### Guess

How can the shop stop the third customer? Here are three ideas.

- A. Put a sign on the drawer that says "please do not touch".
- B. Hire a guard to watch every customer.
- C. Move the drawer behind the counter so that only the teller can reach it.

Idea A depends on every customer reading and obeying. Idea B costs a lot and still lets a rule slip when the guard looks away. Idea C means nobody can break the rule, because nobody outside can reach the drawer.

### Build

1. Decide which parts customers may use: the teller's calls.
2. Decide which parts only the teller may use: the drawer and the back-room helpers.
3. Mark each part with one of those two labels.
4. Customers can only ask the teller. If a customer reaches for the drawer, the shop refuses before opening time.
5. Anything with no label is open to customers.

### Name it

A **visibility modifier** is a word in front of a member that says who may use it. The Kotlin documentation lists four: `private`, `protected`, `internal` and `public`. The documentation says the default visibility is `public`, so a member with no word is open to everybody.

The documentation says `private` means the member is visible inside this class only, including all its members. So `private` is the drawer behind the counter. The documentation says `protected` is the same as `private`, but classes built on top of this one can see it too. It says `internal` means any client inside the same module that sees the class sees its `internal` members. The documentation says a module is a set of Kotlin files compiled together. Those two words are named here and are not used in the programs below.

```kotlin
class Account(private var balance: Int) {
    fun deposit(amount: Int) {
        if (amount > 0) {
            balance = balance + amount
        }
    }

    fun show(): Int {
        return balance
    }
}

fun main() {
    val acc = Account(100)
    acc.deposit(50)
    acc.deposit(-30)
    println(acc.show())
}
```

It prints `150`. The call `acc.deposit(-30)` ran the teller's rule and was refused. The only way to see the balance is `show()`.

Kotlin refuses to run this program, because `balance` is private and `main` is outside the class.

```kotlin
// does not compile
class Account(private var balance: Int)

fun main() {
    val acc = Account(100)
    acc.balance = 5
}
```

## Predict the output

```challenge
type: predict
run: false
id: refused-negative-deposit
verified: kotlin-2.4.21
code: |
  class Account(private var balance: Int) {
      fun deposit(amount: Int) {
          if (amount > 0) {
              balance = balance + amount
          }
      }

      fun show(): Int {
          return balance
      }
  }

  fun main() {
      val acc = Account(100)
      acc.deposit(50)
      acc.deposit(-30)
      println(acc.show())
  }
options:
  - id: a
    text: "120"
    why: "You may have expected the minus 30 to go through. The teller's rule only lets amounts above zero into the drawer."
  - id: b
    text: "100"
    why: "You may have thought a refused call undoes everything before it. The first deposit already went into the drawer."
  - id: c
    text: "150"
    correct: true
    why: "50 passed the rule and went in. Minus 30 failed the rule, so the teller left the drawer alone."
output: |
  150
```

```challenge
type: predict
run: false
id: private-helper-in-back-room
verified: kotlin-2.4.21
code: |
  class Till(private var cash: Int) {
      private fun note(text: String) {
          println("note: " + text)
      }

      fun take(amount: Int) {
          if (amount <= cash) {
              cash = cash - amount
              note("took " + amount)
          } else {
              note("not enough")
          }
      }
  }

  fun main() {
      val till = Till(30)
      till.take(20)
      till.take(20)
  }
options:
  - id: a
    text: "note: took 20, then note: not enough"
    correct: true
    why: "The first take fits in the drawer's 30. That leaves 10, so the second take of 20 is refused. The back-room helper note is called by the teller, which is allowed."
  - id: b
    text: "note: took 20, then note: took 20"
    why: "You may have forgotten that the drawer shrinks. After the first take only 10 is left."
  - id: c
    text: "The program is refused, because note is private and take uses it"
    why: "You may have thought a private helper cannot be used at all. Code inside the same class can use it."
output: |
  note: took 20
  note: not enough
```

```challenge
type: predict
run: false
id: no-label-means-open
verified: kotlin-2.4.21
code: |
  class Box(var size: Int, private var secret: Int) {
      fun reveal(): Int {
          return secret
      }
  }

  fun main() {
      val b = Box(1, 2)
      b.size = 10
      println(b.size)
      println(b.reveal())
  }
options:
  - id: a
    text: "The program is refused, because size cannot be set from main"
    why: "You may have thought every property is hidden unless said otherwise. The documentation says the default is public."
  - id: b
    text: "10, then 2"
    correct: true
    why: "size has no label, so it is open to customers and main may set it. secret is behind the counter, and only reveal reaches it."
  - id: c
    text: "1, then 2"
    why: "You may have thought the assignment was ignored. A public var can be set from outside, so size is now 10."
output: |
  10
  2
```

## Tricks to remember

- Customers ask the teller. They never open the drawer.
- No label means open to all. Say `private` when you mean hidden.
- Start with everything private, then open only what outside code needs.
- A refused call at compile time is cheaper than a bad balance at run time.

## Common mistakes

- **Believing private is for hiding code from other programmers.** The word "hide" sounds like secrecy. Anyone can read the file. Private tells the compiler who may use a member, so the class's rules cannot be skipped.
- **Leaving every property public because it is shorter.** It works, and nothing shows an error. Then any line can put in a bad value such as a negative balance. Make the property private and write a function for the change.
- **Making a helper private and then calling it from outside.** It feels like a normal function. It is the back room. Call a public function instead, and let that one use the helper.

## Where this shows up in Android

The Android page on producing UI state shows a class `DiceRollViewModel` with `private val _uiState = MutableStateFlow(DiceUiState())` and a public `val uiState: StateFlow<DiceUiState>` next to it. The page says that with flows you typically create one or more private backing `MutableStateFlow` instances to propagate state changes. This lesson does not explain those two types. My own reasoning: the screen can read `uiState`, but only the class itself can change it. If a screen shows a value nobody can explain, search for who was allowed to change it. A private property shrinks that search to one class.

## Say it in an interview

**What visibility modifiers does Kotlin have, and what is the default?**
There are four: `private`, `protected`, `internal` and `public`. The Kotlin documentation says the default is `public`. A member with no modifier is open to everyone who can see its class.

**What does `internal` mean?**
The documentation says any client inside the same module that sees the declaring class sees its `internal` members. The documentation says a module is a set of Kotlin files compiled together. A Gradle source set is one example, according to the documentation.

**Why make a property private if the compiler is the only thing that checks it?**
So the class controls how its data changes. Outside code must call a function, and the function can refuse a bad value. My own reasoning: when something looks wrong, you search only inside the class.

**What is the trade-off of making everything private?**
My own reasoning: you write more functions to let outside code read or change things, and a rule that is too tight makes the class hard to use. A good habit is to keep a member private until outside code has a real reason to use it.

## Recap

1. Private means a member can be used only inside its own class.
2. With no modifier, a member is public, so anyone can use it.
3. Outside code must call a public function, and the function can refuse a bad value.

## You can now

- Predict whether a call from outside the class reaches a private member.
- Explain why a public balance lets a bad value in and a private balance does not.
- Predict that a private helper function can be called from inside its own class.
