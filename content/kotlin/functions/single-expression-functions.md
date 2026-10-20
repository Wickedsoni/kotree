---
id: kotlin/functions/single-expression-functions
title: Why do some Kotlin functions use = instead of braces?
status: shadow
difficulty: easy
order: 14
requires: [kotlin/functions/declaring-functions]
summary: A function that is one calculation can swap its braces and return for an equals sign. It is still a normal function.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/functions.html (single-expression syntax, return type inferred "most of the time", must be declared for recursive functions and fun empty() = null, check the inferred type e.g. Number, block body without a return type is Unit); kotlinlang.org/docs/numbers.html (integer division drops the fractional part); kotlinlang.org/docs/coding-conventions.html (libraries state function return types); developer.android.com/kotlin/style-guide (expression functions, override fun toString(): String = "Hey", implicit return type may be omitted). The no-sim reasoning, the doesn't-compile block and the interview trade-offs marked "my own reasoning" are derived, not quoted. Snippets are hand-traced, not run. -->
## The question

You open a Kotlin file and find two functions that do the same job. One is wrapped in curly braces and ends with the word return. The other has an equals sign and a short calculation, with no braces at all. Is the second one a function, or something else? And when are you allowed to write it that way?

## Mental model

Think of a shop that cuts keys. The shop can tell customers about a service in two ways.

- The service is the job the shop does. It stands for the function.
- The long menu board lists the service name, what to bring, the steps, a line saying "hand the result over" and a line saying what you get back. It stands for the braces form.
- The short sign says the service name, what to bring and one line about what you get. It stands for the equals form.
- The customer who walks up to the counter with something to bring is the call.
- What the customer carries out is the result.

Whichever notice hangs on the wall, the customer gets the same service.

**Where the analogy stops:** A real shop can put a short sign on any service, even one with many steps. Kotlin allows the short form only when the whole job is one calculation, and the answer to that calculation is what you get back.

## Build the logic

### Notice

The shop has two notices for doubling a number.

- The long board says: "Doubling. Bring one whole number. Step: multiply it by 2. Hand the result over. You get back a whole number."
- The short sign says: "Doubling. Bring one whole number. You get that number times 2."

A customer brings 4 to either notice and gets 8. Another customer brings 10 and gets 20. The service never changes. Only the notice is shorter.

### Guess

When may the shop use the short sign? Here are three ideas.

- A. Always, for any job.
- B. Only when the whole job is one calculation, and its answer is what the customer gets.
- C. Only when the job prints something.

Idea A feels right, because a short sign sounds like a tidier menu. But a job with several steps has no room for them on a one-line sign. Idea C mixes up showing text with giving an answer. Idea B is the answer.

### Build

1. Check the job. Is it one calculation, and is its answer what the customer gets?
2. Write the service name and what to bring, as on the long board.
3. Do not open a steps board. Write a short join word, then the calculation.
4. The answer to the calculation is handed over by itself. There is no "hand the result over" line.
5. The "you get back" line can be left out when the kind of answer is clear from the calculation. Two times a whole number is a whole number.
6. If the kind is not clear, or you want a different kind, write the "you get back" line.
7. If the job needs several steps, go back to the long board.

### Name it

A **single-expression function** is a function whose body is one **expression**, a piece of code that works out a value, such as `x * 2`. The Kotlin documentation says that when the body is a single expression, you can omit the curly braces and write the body after an `=` symbol. There is no `return`, because the value of the expression is what the function gives back.

The `=` here is part of the function. It still has a name and boxes, and you still call it with round brackets. It is not a variable.

The documentation says that most of the time you do not have to write the return type, because Kotlin works it out from the expression. Here it works out `Int`. It also says to write the type when the compiler can run into problems, for example in a function that calls itself, or in `fun empty() = null`. And it says that if you want a different type than the one Kotlin picks, you must say so. Its example is a `double` function that should return `Number` instead of `Int`.

```kotlin
fun doubleWithBraces(x: Int): Int {
    return x * 2
}

fun double(x: Int) = x * 2

fun main() {
    println(doubleWithBraces(4))
    println(double(4))
}
```

This prints `8` and then `8`. The first function is the long board. The second is the short sign. Both are called the same way.

Kotlin refuses to run this next program. The braces open a steps board, and nothing on it hands a result over. The documentation says a function with a block body and no written return type is treated as returning `Unit`, which means nothing useful. A whole-number name cannot hold that. The refusal is our own reasoning from that rule.

```kotlin
// does not compile
fun double(x: Int) {
    x * 2
}

fun main() {
    val result: Int = double(4)
    println(result)
}
```

## Predict the output

```challenge
type: predict
run: false
id: half-of-seven
verified: not-run
code: |
  fun half(x: Int) = x / 2

  fun main() {
      println(half(7))
      println(half(7) * 2)
  }
options:
  - id: a
    text: "3.5, then 7"
    why: "You may have expected a decimal. Seven and 2 are whole numbers, so the sign gives a whole number and the .5 is dropped."
  - id: b
    text: "3, then 6"
    correct: true
    why: "The sign says: get x divided by 2. Seven divided by 2 is 3 with the .5 dropped, and 3 times 2 is 6."
  - id: c
    text: "3, then 7"
    why: "You may have thought halving and doubling undo each other. The .5 was dropped on the first customer, so it cannot come back."
output: |
  3
  6
```

```challenge
type: predict
run: false
id: two-customers
verified: not-run
code: |
  fun total(price: Int, count: Int) = price * count

  fun main() {
      val first = total(5, 2)
      val second = total(5, 3)
      println(first + second)
  }
options:
  - id: a
    text: "20"
    why: "You may have thought the sign hands every customer the first answer, 10. Each customer gets a fresh calculation."
  - id: b
    text: "15"
    why: "You may have kept only the last customer's answer. Both answers are added together."
  - id: c
    text: "25"
    correct: true
    why: "The first customer gets 5 times 2, which is 10. The second gets 5 times 3, which is 15. The shop works each one out again, and 10 plus 15 is 25."
output: |
  25
```

```challenge
type: predict
run: false
id: sign-inside-sign
verified: not-run
code: |
  fun plusOne(n: Int) = n + 1

  fun main() {
      val a = plusOne(1)
      println(plusOne(a))
      println(plusOne(plusOne(plusOne(0))))
  }
options:
  - id: a
    text: "3, then 3"
    correct: true
    why: "The first customer gets 2, and that answer goes to the counter again and comes back as 3. The three nested customers turn 0 into 1, then 2, then 3."
  - id: b
    text: "2, then 3"
    why: "You may have printed the value of a. The line prints the result of a new call, with a as the thing brought."
  - id: c
    text: "3, then 1"
    why: "You may have thought the inner calls are skipped. The shop answers every call, starting from the inside."
output: |
  3
  3
```

## Tricks to remember

- One calculation, one sign: braces out, equals in.
- No braces, no `return`. The calculation is the answer.
- It is still a function. You call it with round brackets.
- Several steps? Back to the long board.

## Common mistakes

- **Reading the `=` as a variable.** The line looks like a name set to a calculation, so it feels like a `val`. A variable is used without round brackets. This one has a name and boxes, and the shop works the answer out again on every call.
- **Dropping the `=` and keeping the braces.** The two forms look close, so a missing sign feels small. Braces without `return` give back nothing useful. Write the sign, or write `return`.
- **Trusting the type Kotlin picks.** Leaving the type out feels tidy. The documentation says to check the actual result, because the type Kotlin picks may be less useful than the one you wanted. Write the type when it matters.

## Where this shows up in Android

The Android Kotlin style guide says that when a function contains only a single expression, it can be written as an expression function. Its example is `override fun toString(): String = "Hey"`. The word `override` is not covered here. The rest is this lesson: a name, empty round brackets, a return type `String`, an equals sign and one expression. The guide also shows the same line with the type left out.

This next part is our own reasoning: because an expression function holds one calculation, when a screen shows the wrong text, the cause is on that one line or in the values handed to it. There are no hidden steps to search.

## Say it in an interview

**Is `fun double(x: Int) = x * 2` the same as the version with braces and `return`?**
It does the same job and you call it the same way. The Kotlin documentation says that when the body is a single expression you can omit the braces and write it after an `=`. The trade-off is length against room: the short form is tidy for one calculation, and the braces form is needed when there are several steps.

**Do you have to write the return type?**
Often not. The documentation says Kotlin works it out for a single expression, but you must write it for a function that calls itself and for something like `fun empty() = null`. The coding conventions say library code, which other people build on, should state its return types so a change inside does not alter them by accident. My own reasoning: for code only your team reads, a worked-out type saves noise.

**What is the difference between `fun f(x: Int) = x * 2` and `fun f(x: Int) { x * 2 }`?**
The first gives back `x * 2`. The second has braces and no `return`, so the documentation says its return type is `Unit` and the calculation is thrown away. One missing `=` changes what the function gives back.

**Can the single expression be an `if`?**
Yes, because an `if` that gives a value is an expression, as the documentation on `if` says. For example `fun sign(n: Int) = if (n < 0) "negative" else "not negative"`. My own reasoning: when the `if` grows long or needs side jobs, braces read better.

## Recap

1. A function whose body is one calculation can use an equals sign instead of braces and `return`.
2. Kotlin can usually work out the return type of an equals-sign function, but you may write it.
3. An equals-sign function is still a function, called with round brackets, not a variable.

## You can now

- Predict the value a one-line function gives back when you call it.
- Predict the answer when a one-line function divides two whole numbers.
- Predict the result when one call is used inside another call.
