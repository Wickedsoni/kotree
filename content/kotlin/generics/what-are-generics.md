---
id: kotlin/generics/what-are-generics
title: How can one List type hold Strings in one place and Ints in another?
status: shadow
difficulty: medium
order: 54
requires: [kotlin/collections/list-vs-mutable-list, kotlin/interfaces-and-inheritance/what-is-an-interface]
summary: The part in angle brackets tells the compiler what a list holds. See why that saves you checks, and what List of Any costs.
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
- https://kotlinlang.org/docs/generics.html (type parameters, inference of type arguments, type erasure, is-checks on generic types)
- https://developer.android.com/kotlin/flow/stateflow-and-sharedflow (StateFlow<LatestNewsUiState>, List<ArticleHeadline>)
- https://developer.android.com/develop/ui/compose/state (State<List<T>>)
The roadmap row's P about the inferred type of listOf(1, "a") is not used: the type is not asserted. The code declares its types.
The row's F task (a List<Any> that forces casts everywhere) appears as a Predict item, because playground is false.
The roadmap says no sim, because the demo is compiler errors on the wrong item type. This leaf follows that.
-->

## The question

A function reads the first item of a list and asks how many letters it has. It works when the list holds names. It stops being allowed when the list holds a mix of names and numbers, because a number has no letters. What does the `<String>` in `List<String>` tell the compiler, and why does it let you skip a check?

## Mental model

Think of a storage tub with a label on the lid.

- The tub is a list, a container that holds items in order.
- The label says what may go in, such as "socks only" or "numbers only".
- An inspector at the till reads the label when you buy. Anything that does not match is refused.
- Because only socks may go in, whatever you pull out of a "socks only" tub is a sock.
- A tub labelled "anything" accepts forks and socks alike. Whatever comes out could be either.
- It is the same tub design every time. Only the label changes.

**Where the analogy stops:** A real label stays on the tub for ever. In code, the inspector reads the label while the program is being built and then peels it off. A running program cannot read it. This matters in the last part of the lesson.

## Build the logic

### Notice

A sock drawer holds a tub labelled "socks only" with a red sock and a blue sock. Someone tries to put a fork in. The inspector at the till refuses it. You pull out the first item and you know it is a sock, so you can ask for its length at once. Another tub is labelled "anything" and holds a red sock and a fork. You pull out the first item. Is it a sock or a fork? You must look before you ask about its length.

### Guess

What does the label do? Here are three ideas.

- A. Nothing. It is only decoration.
- B. It is checked only when you pull something out, and a wrong item makes the program stop.
- C. It tells the inspector what may go in, and tells you what comes out.

Idea A treats the label as a comment. Idea B describes a world with no inspector, where a wrong item is found late. Idea C is the answer.

Is a tub labelled "anything" the best all-purpose tub? Here are three ideas.

- A. It holds everything, but you lose what you know about the items that come out, so you look at each one.
- B. Yes. It holds everything, so it is the best.
- C. It is refused, because a tub must have a narrow label.

Idea B feels right because more is better. But the cost shows up each time you pull something out. Idea C is not true, because a tub may carry the wide label "anything", as `List<Any>` shows. Idea A is the answer.

### Build

1. It is one tub design. Each tub gets its own label when you buy it.
2. The label says what may go in. The inspector refuses anything else.
3. Only socks go in, so whatever comes out is a sock. You never check.
4. A tub labelled "anything" takes everything, so each item that comes out needs a look first.
5. Choose the narrowest label that fits.
6. The inspector reads the label at the till, then it comes off. A running program cannot read it.

### Name it

A **generic type** is a type with a gap for another type, written in angle brackets, as in `List<String>`. The part in the brackets is the **type argument**. The Kotlin documentation says that classes can have **type parameters**, which are gaps for a type that you fill in when you use the class. It also says that if the **compiler**, the tool that builds your code, can work out the type arguments, you do not need to write them. In this lesson they are written out, so you can see them.

`List<String>` is a list of `String`, which is the "socks only" tub. `List<Int>` is the "numbers only" tub. `List<Any>` is the "anything" tub, where `Any` is a type that every value has.

The documentation says the type safety checks for generic types are done at **compile time**, which is while the program is being built. At **run time**, which is while the program is running, the instances of generic types do not hold information about their type arguments. This is called **type erasure**. The documentation says that because of it, the compiler prohibits checks such as `ints is List<Int>`. Here `is` asks whether a value is of a given type, and the next part of this lesson shows it at work. The compiler allows a check against `List<*>`, a list whose item type is left open. The items are then typed as `Any?`, which means any value, or no value (`null`).

This program uses two narrow tubs. No check is needed when an item comes out.

```kotlin
fun main() {
    val socks: List<String> = listOf("red", "blue")
    val first = socks[0]
    println(first.length)
    val nums: List<Int> = listOf(10, 20)
    println(nums[1] + 1)
}
```

It prints `3` and `21`. The item from `socks` is known to be a `String`, so `length` is allowed. The item from `nums` is known to be an `Int`, so adding 1 is allowed.

The **`is` check** asks whether a value is of a given type, as in `first is String`. Inside the braces after the `if`, Kotlin then treats `first` as a `String` (general knowledge). The `for` loop in the later programs hands each item of a list to its name, one at a time.

This program uses the "anything" tub. Each item needs a look before you ask about its length.

```kotlin
fun main() {
    val tub: List<Any> = listOf("red", 7)
    val first = tub[0]
    if (first is String) {
        println(first.length)
    }
}
```

It prints `3`. The `is` check is the look. Without it, Kotlin does not know that `first` has a length.

Kotlin refuses to run this one. The inspector rejects the number in the "socks only" tub, and the item from the "anything" tub has no length.

```kotlin
// does not compile
fun main() {
    val socks: List<String> = listOf("red", 5)
    val tub: List<Any> = listOf("red", 5)
    println(tub[0].length)
}
```

## Predict the output

```challenge
type: predict
run: false
id: lengths-from-a-socks-only-tub
verified: kotlin-2.4.21
code: |
  fun main() {
      val socks: List<String> = listOf("red", "blue")
      val total = socks[0].length + socks[1].length
      println(total)
  }
options:
  - id: a
    text: "2"
    why: "You may have counted the items. The code adds the letters in each name, not the number of names."
  - id: b
    text: "7"
    correct: true
    why: "Only socks can be in this tub, so each item is known to have a length. red has 3 letters and blue has 4."
  - id: c
    text: "34"
    why: "You may have joined the two numbers as text. The length of a name is an Int, so the plus adds them."
output: |
  7
```

```challenge
type: predict
run: false
id: anything-tub-needs-a-look
verified: kotlin-2.4.21
code: |
  fun main() {
      val tub: List<Any> = listOf("red", 7, "blue")
      var count = 0
      for (item in tub) {
          if (item is String) {
              count = count + 1
          }
      }
      println(count)
  }
options:
  - id: a
    text: "2"
    correct: true
    why: "The look lets only the names through. red and blue pass, and the 7 does not, so the count is 2."
  - id: b
    text: "3"
    why: "You may have counted every item. The look only counts the items that are text."
  - id: c
    text: "The program is refused, because a List<Any> cannot hold both text and numbers"
    why: "You may have thought a mixed tub is refused. The tub labelled anything takes everything, which is why you must look at each item."
output: |
  2
```

```challenge
type: predict
run: false
id: add-to-a-labelled-tub
verified: kotlin-2.4.21
code: |
  fun main() {
      val tub: MutableList<String> = mutableListOf("red")
      tub.add("blue")
      println(tub.size)
      println(tub[1].length)
  }
options:
  - id: a
    text: "2, then blue"
    why: "You may have expected the item itself. The code prints its length, which is a number."
  - id: b
    text: "1, then 3"
    why: "You may have thought the second item was not added. The label says String, and blue is a String, so the inspector lets it in."
  - id: c
    text: "2, then 4"
    correct: true
    why: "Blue matches the label, so it goes in and the tub holds two. The item at place 1 is blue, which has 4 letters."
output: |
  2
  4
```

## Tricks to remember

- Read `List<String>` as "list of String". The label comes second.
- A narrow label means no check when an item comes out.
- The inspector reads the label at the till. A running program cannot.
- On your own machine, write `fun check(x: Any) { if (x is List<String>) println("yes") }` and read what Kotlin says. It refuses, because `x` has type `Any` and the label is erased.

## Common mistakes

- **Treating `List<Any>` as the same as any generic list.** "Any" sounds like "all lists". It is one tub with the widest label. Every item that comes out needs a look. Prefer `List<String>` when only text goes in.
- **Expecting a run-time check on the label.** The label is in the code, so it feels like it travels with the list. The documentation says type arguments are erased, so a check such as `x is List<Int>` is refused when `x` has type `Any`. Check the items instead, or use `List<*>`.
- **Thinking `List<Int>` and `List<String>` are different kinds of tub at run time.** The labels differ, so the tubs feel different. The documentation says instances of `Foo<Bar>` and `Foo<Baz?>` are erased to `Foo<*>`. Here `Foo` stands for any generic type, such as `List`, and `Bar` and `Baz` for two different item types. The label is for the inspector only.

## Where this shows up in Android

The Android page on StateFlow shows `StateFlow<LatestNewsUiState>`, and a `Success` kind that holds `List<ArticleHeadline>`. The Compose state page recommends `State<List<T>>`. Read them from the inside out. A `State` tub holds a `List` tub that holds items of the type `T`, which stands for "some type" (general knowledge). This lesson does not explain `StateFlow` or `State`.

The symptom is a red underline on a list, saying the types do not match, such as a list of IDs passed where a list of names was expected. My own reasoning: that is the inspector at the till refusing a tub with the wrong label, before the app runs. Reading the angle brackets tells you which tub the code wants.

## Say it in an interview

**What are generics, and what problem do they solve?**
The Kotlin documentation says classes can have type parameters. With `List<String>`, the compiler knows what the list holds and checks at compile time that only text goes in. What comes out is a `String`, so no cast is needed. A **cast** is an order to treat a value as a given type.

**What is type erasure?**
The documentation says generic type checks are done at compile time. At run time, the instances do not hold their type arguments, so `Foo<Bar>` and `Foo<Baz?>` are erased to `Foo<*>`. The label is gone once the program runs.

**Why can you not write `if (list is List<String>)` when `list` has type `Any`?**
The documentation says there is no general way to check at run time whether a generic instance was made with certain type arguments, and the compiler prohibits such `is` checks. You may check against `List<*>`, and the items are then typed as `Any?`. My own reasoning: when `list` is already typed `List<String>`, the same line compiles, because the type is known.

**What goes wrong when you cast a value to a generic type?**
My own reasoning, from general knowledge: because the label is erased, a cast to `List<String>` cannot be checked at run time. A list with the wrong items passes the cast, and the failure shows later, when an item is used as a `String`, far from the cast. Check the items you rely on, or keep the label right from the start.

## Recap

1. The part in angle brackets tells the compiler what a list may hold.
2. A narrow label means items come out with a known type and need no check.
3. Type arguments are erased at run time, so `is List<String>` is refused on a value typed `Any`.

## You can now

- Predict what code prints when it reads items from a list with a narrow label.
- Predict how many items pass an `is` check in a `List<Any>`.
- Predict the size and a property of an item after adding to a `MutableList<String>`.
