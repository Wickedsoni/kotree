---
id: kotlin/reading-code/reading-order-for-code
title: In what order should I read an unfamiliar Kotlin file?
status: shadow
difficulty: medium
order: 63
requires: [kotlin/null-safety/when-to-use-double-bang, kotlin/extensions-scope-and-delegates/scope-functions]
summary: Read an unfamiliar file with four questions in a fixed order: types, what runs when, what can be null, and what can run twice.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- Class layout order and "related stuff together, read top to bottom": https://kotlinlang.org/docs/coding-conventions.html
- init blocks run in written order with property initializers; parameters without val or var are not stored: https://kotlinlang.org/docs/classes.html
- Member wins over an extension with the same name; extensions are dispatched statically: https://kotlinlang.org/docs/extensions.html
- Activity onCreate runs once per activity instance; rotation creates a new instance; savedInstanceState is null the first time: https://developer.android.com/guide/components/activities/activity-lifecycle
- The four-question order comes from docs/01 section 3.5 and the roadmap row; the car analogy is the roadmap's.
- Supplied interview data (docs/practice/interview-kotlin.md) said public methods then private helpers; the coding-conventions page does not say that, so the lesson follows the page.
-->

## The question

You open a Kotlin file you did not write. It is 60 lines long, the names are tidy, and the formatting is clean. Where do you start reading? Do you go from the first line to the last, like a story? And what should you look for on the way, so that a hidden crash does not get past you?

## Mental model

Think of inspecting a used car before you buy it.

- The papers come first. They tell you what the car is: make, model, owner. This is the check on what everything is.
- The engine comes next. You start it and watch what moves, and in what order. This is the check on what runs when.
- The brakes come third. You look for a part that must be there and might be missing. This is the check on what can be missing.
- The test drive comes last. You drive round the block twice and see whether the second lap behaves like the first. This is the check on what can happen twice.

You never start with the radio. You do the checks in an order where each one makes the next one easier.

**Where the analogy stops:** A car is inspected by touching it. Code is read by looking, and nothing runs while you read. A car also stays the same between your checks, while a file has code that runs in an order you cannot see from the page. That is why the second question is a question, not a glance.

## Build the logic

### Notice

A shop file defines a cart. The cart has an owner, an optional coupon code, a list of items and a running total. Another part of the file makes a cart, adds a pen and a book, and prints a summary. A last line asks for the coupon in capital letters. The file looks fine on the first read. It stops on that last line when the cart has no coupon.

### Guess

How should you read this file to find that stop before running it? Here are three ideas.

- A. From the first line to the last, as you read a story.
- B. Start with the function that looks most important and trust the names.
- C. Ask the same few questions in a fixed order, and find the answers in the file.

Idea A feels natural, and the Kotlin style guide does say a reader going down a class should be able to follow the logic. But the order on the page is not the order of running. Idea B trusts names, and a tidy name is not proof. Idea C is the answer.

### Build

1. First, learn what everything is. For each name, find its kind of value: the constructor boxes, the property types, what each function hands back. Do not think about running yet.
2. Second, learn what runs when. Find where the program starts. Then find what runs when an object is made, and in what order. Functions inside a class run only when something calls them.
3. Third, learn what can be missing. Look for every place a value might be absent, and for every place the code promises it is not.
4. Fourth, learn what can happen twice. Look for anything that can be called again, or made again, and ask what changes the second time.
5. For each answer, write down the line number. A doubt with no line number is a feeling, and a feeling cannot be checked.
6. Only now ask whether you would accept the file.

### Name it

Each trip through the file with one question in mind is a **pass**. The four passes are types, what runs when, what can be null, and what can run twice.

For the second pass, three Kotlin facts matter. A **property initializer** is the `= ...` part of a property that gives it its starting value. An **init block**, written `init { ... }`, holds code that runs when an object is made. The Kotlin documentation says init blocks run in the order they appear in the class body, along with property initializers. That is why the order on the page matters inside a class, and why it is different from the order of the functions.

The Kotlin coding conventions list the order of a class: property declarations and initializer blocks, then secondary constructors, then method declarations, then the companion object. They also say not to sort methods alphabetically or by visibility, and to keep related things together so a reader going from top to bottom can follow the logic. So top to bottom is how you find things, not how the code runs.

For the third pass, look for `?` after a type, for `?.` and `?:`, and for the **not-null assertion** `!!`, which stops the program with a NullPointerException when the value is null.

Here is the cart file. The four passes follow the code.

```kotlin
class Cart(val owner: String, private val coupon: String?) {
    val items = mutableListOf<String>()
    var total = 0

    init {
        println("Cart for " + owner)
    }

    fun add(name: String, price: Int): Cart {
        items.add(name)
        total += price
        return this
    }

    fun couponLabel(): String = coupon!!.uppercase()

    fun summary(): String = owner + ": " + items.size + " items, " + total
}

fun main() {
    val cart = Cart("Ana", null).apply {
        add("pen", 3)
        add("book", 12)
    }
    println(cart.summary())
    println(cart.couponLabel())
}
```

Pass 1, types: `owner` is a `String`, `coupon` is a `String?`, `items` is a list of text, `total` is an `Int`, and `add` hands back a `Cart`. Pass 2, what runs when: `main` starts the program. `Cart("Ana", null)` runs the property lines and the `init` block, so `Cart for Ana` prints first. Then `apply` runs the two `add` calls, and then `summary` prints `Ana: 2 items, 15`. Pass 3, what can be null: `coupon` is `String?`, and `couponLabel` puts `!!` on it. In `main` the coupon is `null`, so the last line stops with a NullPointerException. Pass 4, what can run twice: `add` can be called again and `total` keeps growing, and every new `Cart` runs `init` again.

## Predict the output

```challenge
type: predict
run: false
id: what-runs-first
verified: not-run
code: |
  class Report(val title: String) {
      val tag = title.uppercase()
      init { println("init sees " + tag) }
      fun show() { println("show " + tag) }
  }
  fun main() {
      val r = Report("sales")
      println("made")
      r.show()
  }
options:
  - id: a
    text: "made, init sees SALES, show SALES"
    why: "You may have thought the init block waits until the object is used. It runs as soon as the object is made, so before the line that prints made."
  - id: b
    text: "init sees SALES, show SALES, made"
    why: "You may have thought every function runs when the object is made. A function runs only when something calls it, and show is called after made."
  - id: c
    text: "init sees SALES, made, show SALES"
    correct: true
    why: "Making the object runs the property line and then the init block, so init prints first. Then main prints made, and only then does r.show() run the function."
output: |
  init sees SALES
  made
  show SALES
```

```challenge
type: predict
run: false
id: bang-hidden-in-a-tidy-class
verified: not-run
code: |
  class Profile(val name: String, val nickname: String?) {
      fun greeting(): String = "Hi " + (nickname ?: name)
      fun shout(): String = nickname!!.uppercase()
  }

  fun main() {
      val p = Profile("Ana", null)
      println(p.greeting())
      println(p.shout())
      println("done")
  }
options:
  - id: a
    text: "Hi Ana, then ANA, then done"
    why: "You may have thought the !! falls back to the name. Only the ?: on the line above does that. The !! has no fallback."
  - id: b
    text: "Hi Ana, then the program stops with a NullPointerException"
    correct: true
    why: "The nickname is null. greeting uses a fallback, so it prints Hi Ana. shout swears the nickname is there, so the program stops on that line and done never prints."
  - id: c
    text: "The program stops before it prints anything"
    why: "You may have thought !! is checked when the object is made. It is checked only on the line where it runs, and that line comes after the first print."
output: |
  Hi Ana
```

```challenge
type: predict
run: false
id: init-runs-per-object
verified: not-run
code: |
  class Counter {
      var count = 0
      init { println("new counter") }
      fun hit() { count++ }
  }
  fun main() {
      val a = Counter()
      a.hit()
      a.hit()
      val b = Counter()
      println(a.count.toString() + " " + b.count)
  }
options:
  - id: a
    text: "new counter, new counter, 2 0"
    correct: true
    why: "Each Counter that is made runs its own init, so new counter prints twice. Each has its own count: a was hit twice and b never."
  - id: b
    text: "new counter, 2 0"
    why: "You may have thought init runs once for the whole file. It runs again every time an object is made."
  - id: c
    text: "new counter, new counter, 2 2"
    why: "You may have thought the two counters share one count. Each object has its own count, so b stays at 0."
output: |
  new counter
  new counter
  2 0
```

## Tricks to remember

- Papers, engine, brakes, test drive: types, what runs when, what can be null, what can run twice.
- Page order is not running order. Find `main` and the `init` blocks first.
- A tidy name is not a papers check. Look at the type.
- Write a line number next to every doubt.

## Common mistakes

- **Reading from the first line to the last like a story.** It feels natural, and the style guide even says top to bottom should follow the logic. But functions run when they are called, not where they sit. Start from where the program starts.
- **Trusting a tidy name or neat formatting.** A name like `couponLabel` sounds safe. Neat code feels checked. Read the type and the body, and ask the null question.
- **Treating a function and an init block alike.** Both are blocks of code in a class. The init block runs when an object is made, and a function runs only when it is called.
- **Skipping the second pass on a call you did not see.** A function that looks harmless can be called from three places. Ask what changes the second time it runs.

## Where this shows up in Android

An **activity** is one screen of an app (general knowledge). The Android page on the activity lifecycle says `onCreate` is for startup logic that happens only once for the entire life of the activity. It also says a configuration change such as rotation destroys the activity and creates a new instance, and `onCreate` is called on the new one. So code in `onCreate` can run again when the user turns the phone. This next part is my own reasoning: a message or a request placed there can show up twice. That is the fourth pass, what can run twice.

The same page says `onCreate` receives `savedInstanceState`, a `Bundle` that is `null` if the activity has never existed before. That is the third pass, what can be null.

## Say it in an interview

**In what order do you read an unfamiliar Kotlin file?**
I make four passes. First types: what each name is. Second what runs when: where the program starts, and what runs when an object is made. Third what can be null. Fourth what can run twice. My own reasoning: I write a line number beside each doubt, so the review is something I can check.

**Why do properties and init blocks come first in a Kotlin class?**
The Kotlin coding conventions give the order: properties and initializer blocks, secondary constructors, methods, then the companion object. The documentation says init blocks run in written order with property initializers. My own reasoning: a reader then meets what the object holds before the code that uses it.

**Are the members of a class sorted alphabetically or by visibility?**
No. The conventions say not to sort methods alphabetically or by visibility. Put related things together, so a reader going from top to bottom can follow what happens. My own reasoning: a sorted file scatters the lines that belong together.

**How do extension functions change the way you trace code?**
They are not inside the class, so you also look in the file and its imports. The documentation says extensions do not change the class, that they are dispatched statically, and that a member function wins over an extension with the same name and arguments. My own reasoning: when a call does something odd, check whether an extension or a member is being called.

## Recap

1. Read an unfamiliar file with four questions in a fixed order, not from the first line to the last.
2. The four questions are types, what runs when, what can be null, and what can run twice.
3. Page order is not running order, so find where the program starts and what runs on creation.

## You can now

- Predict what prints first when an object is made and its functions are called later.
- Spot the line that can stop a tidy-looking class when a value is null.
- Predict what runs again for each new object and what stays separate.
