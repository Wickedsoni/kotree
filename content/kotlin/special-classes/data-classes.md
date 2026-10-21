---
id: kotlin/special-classes/data-classes
title: Why does Kotlin write equals and toString for some classes?
status: shadow
difficulty: medium
order: 49
requires: [kotlin/classes-and-objects/two-kinds-of-equality]
summary: A data class compares and prints itself by its main values. See which values count and which do not.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/data-classes.html ; https://kotlinlang.org/docs/equality.html ; https://developer.android.com/training/data-storage/room ; https://developer.android.com/topic/architecture/ui-layer/state-production -->

## The question

A shop prints two receipts for the same order. Both list milk and a total of 5. Can you hand one in for a refund when the shop only has the other one on file? Or does the shop say they are different, because they are two pieces of paper? How could a program decide?

## Mental model

Think of paper receipts and a shop that checks them.

- A receipt is an object.
- The printed lines on it, such as the item and the total, are the values that make up the receipt.
- A pencil note on the back, such as "queue number 4", is extra and is not part of the receipt.
- The shop's rule for "same receipt" decides whether two papers count as one.
- Reading a receipt aloud, line by line, is how you print it.

**Where the analogy stops:** Real receipts often carry a number that is different on every copy. A plain class works like that: two objects are equal only if they are the same piece of paper. A data class compares the printed lines and ignores which piece of paper it is. Also, two equal receipts are still two separate pieces of paper.

## Build the logic

### Notice

Two receipts lie on the counter. Both say "milk" and "total 5". On the back of one, the cashier wrote "queue 4" in pencil. On the back of the other, "queue 9". The shop is asked: are these the same receipt?

### Guess

What should count as the same receipt? Here are three ideas.

- A. Only if it is literally the same piece of paper.
- B. If the printed lines match, whatever the pencil notes say.
- C. If the printed lines and the pencil notes all match.

Idea A is how a plain class works. Idea C counts a note that was never part of the receipt. Idea B is what a data class does.

How should the shop read a receipt aloud? Here are three ideas.

- A. Say only the name of the kind of paper.
- B. Read the printed lines in a fixed pattern, such as "Receipt: item milk, total 5".
- C. Read every line and every pencil note.

Idea B is the answer, and the pencil notes stay out.

### Build

1. Decide which values are printed on the receipt. Those are the ones that count.
2. Anything written elsewhere, such as the pencil note, is not compared and not read aloud.
3. Two receipts are the same when each printed value matches.
4. Reading aloud always follows the same pattern, name first, then each printed value.
5. Two matching receipts are still two pieces of paper.

### Name it

A **data class** is a class marked with the word `data` in front, as in `data class Receipt(val item: String, val total: Int)`. The Kotlin documentation says the compiler derives these members from all properties declared in the primary constructor: an `equals()` and `hashCode()` pair, a `toString()` of the form `User(name=John, age=42)`, the `componentN()` functions, which unpack the values, and a `copy()` function, which makes a changed duplicate. This lesson uses `equals` and `toString`. The documentation says `hashCode` goes with `equals` as a pair. The other two are not used here.

The `==` operator calls `equals`. The documentation says a plain class gets the default `equals`, which checks whether two references point to the same object. A data class overrides it. So `==` on two data class objects compares the printed values, and `===` still asks whether they are the same piece of paper.

The documentation says the compiler only uses the properties inside the primary constructor, which is the brackets after the class name. To leave one out, declare it inside the class body, the curly brackets. It also says every primary constructor parameter must be marked `val` or `var`, and there must be at least one.

```kotlin
data class Receipt(val item: String, val total: Int) {
    var queue: Int = 0
}

class PlainReceipt(val item: String, val total: Int)

fun main() {
    val a = Receipt("milk", 5)
    val b = Receipt("milk", 5)
    a.queue = 4
    b.queue = 9
    println(a == b)
    println(a === b)
    println(a)
    val p = PlainReceipt("milk", 5)
    val q = PlainReceipt("milk", 5)
    println(p == q)
}
```

It prints `true`, `false`, `Receipt(item=milk, total=5)` and `false`. The pencil note `queue` is in the class body, so it is left out of `==` and out of the printout. The plain receipts hold the same values, but they are two pieces of paper, so `==` is `false`.

Kotlin refuses to run this program, because `item` is not marked `val` or `var`.

```kotlin
// does not compile
data class Receipt(item: String, val total: Int)

fun main() {
    println("never runs")
}
```

## Predict the output

```challenge
type: predict
run: false
id: data-versus-plain-equals
verified: kotlin-2.4.21
code: |
  data class Ticket(val seat: Int, val row: String)

  class Pass(val seat: Int, val row: String)

  fun main() {
      println(Ticket(4, "B") == Ticket(4, "B"))
      println(Pass(4, "B") == Pass(4, "B"))
      println(Ticket(4, "B") == Ticket(5, "B"))
  }
options:
  - id: a
    text: "true, true, false"
    why: "You may have thought two plain objects with the same values are equal. A plain class compares the piece of paper, so the two passes are different."
  - id: b
    text: "true, false, false"
    correct: true
    why: "The two tickets have the same printed lines, so they match. The two passes are two pieces of paper. The last pair differs in seat."
  - id: c
    text: "true, false, true"
    why: "You may have thought only the row matters. Every value in the main brackets is compared, so seat 4 and seat 5 differ."
output: |
  true
  false
  false
```

```challenge
type: predict
run: false
id: pencil-note-is-ignored
verified: kotlin-2.4.21
code: |
  data class User(val name: String) {
      var visits: Int = 0
  }

  fun main() {
      val a = User("Ada")
      val b = User("Ada")
      a.visits = 3
      b.visits = 7
      println(a == b)
      println(a)
      println(b.visits)
  }
options:
  - id: a
    text: "false, User(name=Ada), 7"
    why: "You may have counted the pencil note in the comparison. Only the values in the main brackets are compared, and visits is in the body."
  - id: b
    text: "true, User(name=Ada, visits=3), 7"
    why: "You may have thought the printout reads every value. It reads only the main brackets, so visits is left out."
  - id: c
    text: "true, User(name=Ada), 7"
    correct: true
    why: "visits sits in the body, like a pencil note. It is left out of == and out of the printout, but each object still holds its own value."
output: |
  true
  User(name=Ada)
  7
```

```challenge
type: predict
run: false
id: reading-a-book-aloud
verified: kotlin-2.4.21
code: |
  data class Book(val title: String, val pages: Int)

  fun main() {
      val b = Book("Kotlin", 300)
      println(b)
      println("Reading " + b)
      println(b == Book("Kotlin", 300))
  }
options:
  - id: a
    text: "Book(title=Kotlin, pages=300), Reading Book(title=Kotlin, pages=300), true"
    correct: true
    why: "Printing a data class reads out the name and each printed value. Adding it to text uses the same reading. The second Book has the same values, so == is true."
  - id: b
    text: "Kotlin, Reading Kotlin, true"
    why: "You may have thought only the first value is read. The reading covers every value in the main brackets, with names."
  - id: c
    text: "Book(title=Kotlin, pages=300), Reading Book(title=Kotlin, pages=300), false"
    why: "You may have thought a fresh Book is a different receipt. For a data class, equal values are enough for == to be true."
output: |
  Book(title=Kotlin, pages=300)
  Reading Book(title=Kotlin, pages=300)
  true
```

## Tricks to remember

- Main brackets count. Body values do not.
- `==` asks "same printed lines?". `===` asks "same piece of paper?".
- `data` writes the comparison and the reading aloud so you do not.
- Every main-bracket parameter needs `val` or `var`.

## Common mistakes

- **Thinking `data` only makes the printout nicer.** The printout is the part you see first, so it feels like the whole job. The same keyword changes `==` too. Two data objects with the same values are equal.
- **Putting a value in the body and expecting it to count.** Inside the class feels like part of the class. The documentation says only primary constructor properties are used. Move the value into the main brackets if it should count.
- **Using a changeable `var` in the main brackets for an object that is stored in a set or used as a map key.** The object and its value feel like one thing. My own reasoning: a set and a map find an object using values that come from its fields, so a changed value may stop the object being found. Prefer `val` for values that identify the object.
- **Expecting `===` to be true for two equal data objects.** They match, so it feels the same. `===` asks whether it is the same object, and two separate receipts are not.

## Where this shows up in Android

The Android Room page defines a data entity with `@Entity data class User(...)` and says each instance of `User` represents a row in a `user` table in the app's database. The Android page on UI state shows `data class DiceUiState(...)` holding values for one screen. This lesson does not explain those two. My own reasoning: when two `User` objects read from the database hold the same values, `==` says they are the same user without comparing them field by field in your own code. If a list shows the same person twice where one row was expected, ask which values are in the main brackets.

## Say it in an interview

**What does a data class give you?**
The Kotlin documentation says the compiler derives `equals()` and `hashCode()`, `toString()`, `componentN()` functions and `copy()` from the properties declared in the primary constructor. This lesson covered `equals` and `toString`.

**What are the requirements for a data class?**
The documentation says the primary constructor must have at least one parameter, and all its parameters must be marked `val` or `var`. It also says data classes can't be abstract, open, sealed or inner.

**Which properties does the generated `equals` compare?**
Only the ones in the primary constructor. The documentation's example has `age` declared in the class body, so two people with the same name but different ages are still equal. To leave a property out, put it in the body.

**What can go wrong with a `var` in a data class?**
My own reasoning: if the object is stored in a set or used as a map key, changing a value can change where the collection looks for it, and a lookup may fail. The trade-off is that `val` values are safer but need a new object for each change.

## Recap

1. A data class makes `==` compare values, and not whether two objects are the same one.
2. Only the properties in the primary constructor are compared and printed.
3. A plain class treats two objects as equal only if they are the same one by default.

## You can now

- Predict whether `==` is true for two data class objects and for two plain objects.
- Predict whether a property in the class body changes `==` or the printout.
- Predict the text printed for a data class object.
