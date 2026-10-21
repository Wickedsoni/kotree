---
id: kotlin/reading-code/spotting-risky-patterns
title: "Which lines in tidy-looking Kotlin deserve a second look?"
status: shadow
difficulty: hard
order: 64
requires: [kotlin/reading-code/reading-order-for-code, kotlin/errors-and-results/result-and-runcatching]
summary: "Five kinds of line hide most bugs in neat Kotlin: a not-null promise, a shadowed name, a look-alike comparison, a swallowed failure, and a call on an empty list."
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/null-safety.html (!! and NullPointerException) ; https://kotlinlang.org/docs/equality.html (== calls equals, structural; === is referential; data classes override equals) ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/run-catching.html (runCatching catches any Throwable and wraps it as a failure) ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/first.html (first() throws NoSuchElementException if the list is empty) ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/max-or-null.html (maxOrNull returns null if the collection is empty) ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int-or-null.html (toIntOrNull returns null if the string is not a valid Int) ; https://developer.android.com/kotlin/coroutines/coroutines-best-practices (do not consume CancellationException; prefer specific exception types) ; https://developer.android.com/guide/components/activities/activity-lifecycle (savedInstanceState is null if the activity has never existed before). ⚠️ VERIFY: compile not run for any kotlin block (no shell), including that the shadowed-name code compiles with only warnings. ⚠️ VERIFY: getOrNull, firstOrNull and getOrDefault are used from stdlib knowledge; their pages were not fetched. ⚠️ VERIFY: the roadmap lists three F(ai) tasks but a leaf has exactly one Fix block; this leaf uses the runCatching task for Fix and the other two as Predict items. -->

## The question

A teammate hands you a 40-line Kotlin file. The names are clear, the spacing is neat, and the compiler has nothing to say. Does that mean it is safe? And if you have only five minutes, which lines do you look at first?

## Mental model

Think of a home inspector walking through a tidy house.

- The house looks fine from the street. The paint is fresh and the garden is cut.
- The inspector does not check every brick. Damp, the slow water that rots a house, hides in the same five spots every time.
- Under the sink is the first spot. A pipe there can leak for months behind a closed door.
- Under a fresh coat of paint is the second spot. A stain can be covered, and the stain is still there.
- Behind two look-alike doors is the third. Two doors that look the same may lead to the same room or to two different rooms.
- Inside a sealed drain is the fourth. A covered drain hides a blockage that nobody sees.
- In an empty loft is the fifth. The plan says boxes are stored there, and today there are none.

The inspector goes straight to the five spots, and asks what damage each one could be hiding.

**Where the analogy stops:** An inspector can see and touch damp. A risky line of code looks as neat as a safe one, and only reading what it does with an odd input shows the problem. Also, damp spreads by itself, while a bug waits for one particular input, such as an empty list. And five spots are the common ones, not every one.

## Build the logic

### Notice

Five lines from a neat file are described here. The compiler accepts all five.

1. A line that promises a name is never missing.
2. A line inside a loop that makes a new variable with the same spelling as one outside the loop.
3. A line that asks whether two doors are the very same door.
4. A line that wraps a number-reading step so that any problem quietly becomes 0.
5. A line that takes the first name from a list without checking that the list has one.

### Guess

How can you find lines like these in five minutes? Here are three ideas.

- A. Read from the top to the bottom and trust the neat names.
- B. Look for the five spots, and for each one ask what odd input would hurt it.
- C. Run the file once with normal data. If it works, it is fine.

Idea A trusts the surface. Idea C feels sensible, but each of these lines works with normal data and waits for a rare one. Idea B is the answer.

### Build

1. Pick a line. Ask what odd input would hurt it.
2. The odd inputs are few: a value that is missing, an attempt that fails, a list with nothing in it, two things that look alike, and two names that look alike.
3. A risky line makes a promise, or hides something, or assumes something. It does not plan for the odd input.
4. Trace the line in your head with the odd input. If it stops, or it quietly gives a wrong answer, flag it.
5. Write down the line number and the odd input. A doubt with no line number cannot be checked.
6. Say what you saw. Do not rewrite the file yet.

### Name it

Each spot is a Kotlin pattern, shown in the table below.

| Spot | Pattern | Why damp hides there | Safer habit |
|---|---|---|---|
| Under the sink | `!!`, the **not-null assertion** | The compiler trusts your promise. The program stops with a NullPointerException only when the value is missing | Check for null, or use `?.` and `?:` |
| Under fresh paint | A **shadowed** name: a variable inside a block with the same name as one outside | The inner one hides the outer one, and a change to the inner one never reaches the outer | Use a different name, or assign to the outer one without `val` |
| Look-alike doors | `==` against `===` | `==` asks whether two things have equal content. `===` asks whether they are the same object. A data class makes `==` compare content | Use `==` for "same content". Use `===` only for "same object" |
| Sealed drain | `runCatching` with a default value | It catches any **`Throwable`**, anything that can be thrown, and wraps it as a failure. A default then makes a bad input look like a real answer | Use `toIntOrNull()` and handle `null` by name |
| Empty loft | `first()` on a list | The Kotlin API page says it throws a `NoSuchElementException` if the list is empty | Use `firstOrNull()`, `getOrNull(0)` or `maxOrNull()` and handle `null` |

Here are the five spots in one program. Only the safe versions run.

```kotlin
data class Door(val number: Int)

val nickname: String? = null

fun parseAge(text: String): Int = runCatching { text.toInt() }.getOrDefault(0)

fun main() {
    // Spot 1: the risky line would be println(nickname!!.length)
    println(nickname?.length)

    // Spot 2: the inner total hides the outer one
    val total = 10
    for (i in 1..2) {
        val total = i * 5
        println(total)
    }
    println(total)

    // Spot 3: same content, not the same object
    val a = Door(1)
    val b = Door(1)
    println(a == b)
    println(a === b)

    // Spot 4: a bad text and a real 0 look the same
    println(parseAge("x"))
    println(parseAge("0"))

    // Spot 5: first() would stop on an empty list
    val names = emptyList<String>()
    println(names.firstOrNull())
}
```

This prints nine lines: `null`, `5`, `10`, `10`, `true`, `false`, `0`, `0` and `null`. The two zeros are the clue to spot 4: "x" and "0" give the same answer.

## Predict the output

```challenge
type: predict
id: shadowed-best
verified: "UNVERIFIED: not run, no shell"
code: |
  fun main() {
      var best = 0
      for (score in listOf(3, 9, 4)) {
          if (score > best) {
              val best = score
          }
      }
      println(best)
  }
options:
  - id: a
    text: "9"
    why: "You may have read the inner line as an update to the best you can see from outside. The new val makes a second variable with the same spelling."
  - id: b
    text: "0"
    correct: true
    why: "The inner best is a new variable that hides the outer one inside the block. The outer best is never changed, so the paint covers the stain and the stain stays at 0."
  - id: c
    text: "4"
    why: "You may have pictured the loop leaving the last score behind. Nothing is ever stored in the outer best."
output: |
  0
```

```challenge
type: predict
id: look-alike-doors
verified: "UNVERIFIED: not run, no shell"
code: |
  data class Door(val number: Int)

  fun main() {
      val a = Door(4)
      val b = Door(4)
      val c = a
      println(a == b)
      println(a === b)
      println(a === c)
  }
options:
  - id: a
    text: "true, true, true"
    why: "You may have thought two equal doors are one object. They are two objects with the same content."
  - id: b
    text: "false, false, true"
    why: "You may have thought == asks whether the two are the same object. For a data class it compares content, so a and b are equal."
  - id: c
    text: "true, false, true"
    correct: true
    why: "Two doors built alike are equal in content, so == is true. They are two doors, so === is false. The door c is the same door as a, so === is true."
output: |
  true
  false
  true
```

```challenge
type: predict
run: false
id: which-line-stops
verified: not-run
code: |
  fun main() {
      val names = emptyList<String>()
      println(names.getOrNull(0))
      println(names.firstOrNull())
      println(names.maxOrNull())
      println(names.first())
      println("done")
  }
options:
  - id: a
    text: "null, null, null, then the program stops on the fourth line and done never prints"
    correct: true
    why: "The first three calls hand back null for an empty loft and print it. Only first() has no null to hand back, so it stops the program."
  - id: b
    text: "null, null, null, null, done"
    why: "You may have thought every call returns null for an empty list. first() throws instead, as its documentation says."
  - id: c
    text: "The program stops on the first line, because the list is empty"
    why: "You may have thought any use of an empty list stops the program. The calls with OrNull in the name give null instead."
output: |
  null
  null
  null
```

## Try it

1. **Write.** Type the five-spots program from Name it into the editor.
2. **Run.** You should see nine lines: `null`, `5`, `10`, `10`, `true`, `false`, `0`, `0` and `null`.
3. **Break.** Change `println(nickname?.length)` to `println(nickname!!.length)` and change nothing else. Run it again. The program stops at once with a NullPointerException, and none of the other eight lines print.
4. **Fix.** Put `?.` back in place of `!!` and run it. The nine lines return.

## Fix the bug

```challenge
type: fix
id: fix-swallowed-age
generated: true
verified: "UNVERIFIED: not run, no shell"
code: |
  fun readAge(text: String): Int {
      // Turn the typed text into a number. Any problem gives 0.
      return runCatching { text.trim().toInt() }.getOrDefault(0)
  }

  fun describe(text: String): String {
      val age = readAge(text)
      return if (age >= 18) "adult" else "child"
  }

  fun main() {
      val typed = listOf("42", "abc", "7")
      for (text in typed) {
          println(text + " -> " + describe(text))
      }
  }
hints:
  - "Look at the line for abc in the output. Is a person who typed abc really a child?"
  - "A bad text and a real 0 look the same here. Let readAge say 'no number' with a value that is not an Int, and let describe handle it."
expected: |
  42 -> adult
  abc -> unknown
  7 -> child
fix: |
  fun readAge(text: String): Int? {
      // Turn the typed text into a number. A bad text gives null.
      return text.trim().toIntOrNull()
  }

  fun describe(text: String): String {
      val age = readAge(text)
      if (age == null) return "unknown"
      return if (age >= 18) "adult" else "child"
  }

  fun main() {
      val typed = listOf("42", "abc", "7")
      for (text in typed) {
          println(text + " -> " + describe(text))
      }
  }
explanation: "runCatching with a default hid the failure in the drain, so abc became a child. Returning null keeps the failure visible, and describe now has to decide what to do with it."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

Question 3 is the point of this task: in the original, nothing is ever missing, because the failure was turned into a number.

## Tricks to remember

- Sink, paint, doors, drain, loft: `!!`, a shadowed name, `===`, `runCatching`, `first()`.
- Ask of every suspect line: what odd input hurts this?
- Neat is not safe. The compiler checks the shape, not the odd inputs.
- Task: take a file you wrote and find one line of each kind. Write down its line number and the odd input.

## Common mistakes

- **Thinking tidy formatting and good names mean the code is correct.** A neat file feels cared for. The compiler checks that the types fit together, not what a line does with a missing value or an empty list.
- **Testing only with normal data.** If it works once, it feels finished. These five lines work with normal data and wait for the one odd input. Try the empty list, the missing value and the bad text.
- **Wrapping a failing line in `runCatching` to make the crash go away.** The crash stops, so it feels fixed. The failure is still there. It has moved into a default that looks like a real answer.
- **Using `===` to ask whether two values are equal.** It reads as "equal, with extra strength". It asks about the same object, which can be false for two equal data class values.

## Where this shows up in Android

An activity is one screen of an app (general knowledge). The Android documentation says its `onCreate` method receives `savedInstanceState`, a `Bundle` that holds the activity's previously saved state, and that this value is `null` if the activity has never existed before. So `savedInstanceState!!` makes a promise that is false the first time the screen opens. My own reasoning: the app can crash on a fresh start while it works in later tests. That is spot 1.

Spot 4 shows up as a price that reads `0` on the screen when the server sent text the code could not turn into a number. My own reasoning: the screen looks fine and the failure has been swallowed.

## Say it in an interview

**What do you look for first in a Kotlin file that looks tidy?**
I look for a handful of spots: `!!` promises, names that shadow another name, `===` used for equality, failures caught and replaced with a default, and calls like `first()` on a list that may be empty. For each one I ask what odd input hurts it. My own reasoning: I write down the line number, so the review can be checked.

**Why is wrapping a block in `runCatching` risky?**
The Kotlin documentation says `runCatching` catches any `Throwable` and wraps it as a failure. If you then replace the failure with a default, a bad input looks like a real answer. The Android coroutines page also says not to consume `CancellationException` and to prefer catching specific exception types. A broad catch can break cancellation, which is how coroutines stop work that is no longer needed.

**What is the difference between `==` and `===` in Kotlin?**
The documentation says `==` is structural equality: it calls `equals`. `===` is referential equality and is true only if both sides point to the same object. A data class overrides `equals`, so two data class values with the same content are `==` but not `===`.

**If Kotlin code compiles with no warnings, is it safe?**
No. The compiler checks that the types and syntax fit together. It does not check what a line does with a missing value, an empty list or a failed attempt. The null-safety documentation lists `!!` as an operator that can still cause a NullPointerException, so a clean build can hide a crash.

## Recap

1. Neat Kotlin can still hide bugs in a few common spots.
2. The five spots are `!!`, a shadowed name, `===`, a swallowed failure and `first()` on an empty list.
3. For each spot, ask what odd input hurts the line, and write down its line number.

## You can now

- Predict which of several calls stops the program on an empty list.
- Predict what a shadowed variable leaves unchanged outside its block.
- Fix a function that turns a failed number-reading into a real-looking 0.
- Explain in your own words why `a == b` and `a === b` can differ.
