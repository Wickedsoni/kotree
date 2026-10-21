---
id: kotlin/generics/writing-generic-code
title: How do I write a function that works for any type?
status: shadow
difficulty: medium
order: 55
requires: [kotlin/generics/what-are-generics]
summary: One function can work for numbers, words or anything else. See how its blank for the kind is filled at each call.
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
- https://kotlinlang.org/docs/generics.html : type parameters go before the function name; type arguments can be left out when inferred; default upper bound is Any?; out and in; erasure and the ban on is-checks such as ints is List<Int>.
- https://developer.android.com/topic/libraries/architecture/livedata : MutableLiveData<String> snippet; "LiveData is a wrapper that can be used with any data".
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/generics/writing-generic-code.
- Code blocks and Predict outputs were hand-traced and not compiled in this session (no shell). Run scripts/kt-run.sh before review.
-->

## The question

A shop needs a helper that gives back the first jar on a shelf, or a fallback jar when the shelf is empty. One shelf holds numbers, another holds words, and a third holds dates. Must the shop build a separate helper for each kind of shelf? Or can one helper serve all of them?

## Mental model

Think of a labelling machine in a kitchen.

- A jar is one value.
- The machine is the function. You feed it jars and it does its work.
- The machine has one blank slot marked "kind of jar". Nobody fills it in when the machine is built.
- When you feed in a jar of numbers, the slot reads "numbers". When you feed in a jar of words, it reads "words". The machine fills the slot itself, by looking at what you gave it.
- Inside the machine, every part that says "this kind of jar" points at the same slot. So the jar that goes in and the jar that comes out are always the same kind.

**Where the analogy stops:** A real machine looks at the jar while it runs. Kotlin fills the slot before the program starts, while it checks your code. Also, the slot is only a blank. It is never a jar of its own kind.

## Build the logic

### Notice

The shop wants "first jar on the shelf, or the fallback jar". For a shelf of number jars holding 4 and 5, with a fallback of 0, the answer is 4. For an empty shelf of word jars with a fallback of "none", the answer is "none". The steps are the same each time: look at the shelf, check if it is empty, hand back either the first jar or the fallback. Only the kind of jar changes.

### Guess

How should the shop build this? Here are three ideas.

- A. Build one machine for numbers, a second for words and a third for dates.
- B. Build one machine with a blank for the kind of jar, and fill the blank each time it is used.
- C. Build one machine that accepts any jar and forgets what kind it was.

Idea A works, but the three machines are the same except for one word, so a fix in one must be copied into the other two. Idea C compiles, but you lose track of the kind. You put in a number jar and get back a jar you know nothing about. Idea B is the answer.

### Build

1. Write the steps once: if the shelf is empty, hand back the fallback; otherwise hand back the first jar.
2. Add the blank slot, "kind of jar", and write it everywhere the steps say "this kind".
3. Say once, at the top, that the machine has a blank slot and what it is called.
4. When the machine is used, look at the jars you feed in and fill the slot from them.
5. If the jars disagree about the kind, the machine is refused before the program starts.
6. Inside the machine, treat a jar as a closed jar. The slot could be filled with anything, so the machine may not assume it can ask for a number of letters.

### Name it

A **generic function** is a function with a blank for the kind of value it works on. The blank is a **type parameter**. The Kotlin documentation says type parameters are placed before the name of the function. The documentation's examples name the blank `T`. Other names such as `E` are also seen (general knowledge), but this lesson sticks to `T`.

The documentation says type arguments can be left out if they can be inferred from the context. A **type argument** is the kind that fills the blank at one call. When you write `firstOr(listOf(4, 5), 0)`, Kotlin sees `Int` values and fills `T` with `Int`. You may also write it yourself, as in `emptyList<String>()`.

The documentation says the default upper bound, the widest kind the blank may be, is `Any?`. That is why step 6 holds. An `Int` has no `.length`, and `T` could be an `Int`, so the compiler refuses.

```kotlin
fun <T> firstOr(list: List<T>, default: T): T {
    return if (list.isEmpty()) default else list[0]
}

fun main() {
    val a = firstOr(listOf(4, 5), 0)
    val b = firstOr(emptyList<String>(), "none")
    println(a)
    println(b)
}
```

It prints `4` and then `none`. The first call fills `T` with `Int`. The second call fills it with `String`. The same steps ran both times.

Kotlin refuses to run this one, because `T` could be any kind, and not every kind has a `length`.

```kotlin
// does not compile
fun <T> sizeOf(item: T): Int {
    return item.length
}

fun main() {
    println(sizeOf("hello"))
}
```

## Predict the output

```challenge
type: predict
run: false
id: fill-the-blank-each-call
verified: not-run
code: |
  fun <T> firstOr(list: List<T>, default: T): T {
      return if (list.isEmpty()) default else list[0]
  }

  fun main() {
      println(firstOr(listOf(5, 6), 0) + 1)
      println(firstOr(emptyList<String>(), "none") + "!")
  }
options:
  - id: a
    text: "1 and none!"
    why: "You may have thought the fallback is always used. The fallback is for an empty shelf, and this shelf holds 5 and 6."
  - id: b
    text: "6 and none!"
    correct: true
    why: "The first call fills the blank with numbers, hands back 5, and adding 1 gives 6. The second shelf is empty, so the fallback none comes back, and the words get a ! added."
  - id: c
    text: "The program is refused, because the blank cannot be added to 1"
    why: "You may have expected the blank to stay a mystery. At each call it is filled in, so the first result is a number and the second is a word."
output: |
  6
  none!
```

```challenge
type: predict
run: false
id: blank-is-not-a-jar
verified: not-run
code: |
  fun <T> twice(item: T): List<T> {
      return listOf(item, item)
  }

  fun main() {
      println(twice("hi"))
      println(twice(3).size)
  }
options:
  - id: a
    text: "[T, T] and 2"
    why: "You may have thought T is a real jar called T. It is only a blank, and the first call fills it with the word hi."
  - id: b
    text: "[hi, hi] and [3, 3]"
    why: "You may have read .size as the list itself. It asks how many jars are in the list, which is a number."
  - id: c
    text: "[hi, hi] and 2"
    correct: true
    why: "The machine puts the jar in twice. The first call prints the list of two words. The second call has a list of two number jars, and its size is 2."
output: |
  [hi, hi]
  2
```

## Tricks to remember

- The blank goes in angle brackets before the function name, and every "this kind" in the function points back at it.
- One shelf, one kind: the blank is filled the same way everywhere in one call.
- If the same function exists twice for two kinds, a blank is waiting to be added.
- A blank could be anything, so you can only use what every kind has.

## Common mistakes

- **Thinking `T` is a real kind called `T`.** The letter looks like a name, like `Int`. It is a blank. Kotlin replaces it at every call.
- **Using `Any` instead of a blank.** `Any` also accepts every kind, so it feels the same. But the result comes back as `Any`, and you lose track of the kind. A blank keeps the kind of the input and the output the same.
- **Writing the same function twice, once for `Int` and once for `String`.** Each one works on its own, so it feels finished. A change in one copy has to be repeated in the other. Write the steps once and add a blank.
- **Calling something on a blank, such as `.length`.** You know the jar holds a word, so it feels fine. The function must work for every kind, so the compiler checks against the widest one.

## Where this shows up in Android

Picture a screen that shows a user's name, and another that shows a list of scores. Both need a holder for data that the screen can watch (general knowledge). The Android LiveData page shows `MutableLiveData<String>` and says LiveData is a wrapper that can be used with any data, including a `List`. That is one holder with a blank for the kind of data. Here `<String>` fills the blank. This lesson puts a blank on a function. A class gets a blank in the same way.

## Say it in an interview

**How do you write a generic function in Kotlin?**
Put the type parameter in angle brackets before the function name, as in `fun <T> firstOr(list: List<T>, default: T): T`. Use `T` everywhere the function means "this kind". The Kotlin documentation says the type argument can usually be left out, because Kotlin infers it from the call.

**Why can't I call `.length` on a value of type `T`?**
By default `T` can be any type, even a nullable one. The documentation says the default upper bound is `Any?`. To use more, you give `T` a bound, such as `T : Comparable<T>` in the documentation's `sort` example. My own reasoning: the bound is a promise to the compiler that every allowed kind has the tools you want to use.

**What do `out` and `in` mean on a type parameter?**
The documentation says `out` means the type is only produced, so it may appear in return positions and never be consumed. `in` is the other way: it is only consumed and never produced. A short way to remember it is "producer out, consumer in". My own reasoning: you meet this when you write your own class, not when you write a single function.

**Can you check at run time whether a list is a `List<String>`?**
No. The documentation says generic type information is erased at run time, and the compiler refuses checks such as `ints is List<Int>`. You can check against a star-projected type like `List<*>`. The check at compile time is what keeps your lists honest.

## Recap

1. A generic function has a blank for the kind of value, written in angle brackets before its name.
2. Kotlin fills the blank at each call from the values you pass in.
3. A blank could be any kind, so inside the function you can only use what every kind has.

## You can now

- Predict what a call to a generic function returns for different kinds of input.
- Explain why `T` in a generic function is a blank and not a real type called `T`.
