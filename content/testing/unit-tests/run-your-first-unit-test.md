---
id: testing/unit-tests/run-your-first-unit-test
title: "How do I write and run one test on my own computer?"
status: shadow
difficulty: easy
order: 2
requires: [testing/why-test/why-tests-exist]
summary: A local unit test is one small function in the test folder that calls your code and checks the answer, and it runs on your computer without a phone.
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live):
- https://developer.android.com/training/testing/local-tests : "A local test runs directly on your own workstation, rather than an Android device or emulator ... it uses your local Java Virtual Machine (JVM)"; sources "in module-name/src/test/"; dependencies applied with testImplementation (docs form: testImplementation "junit:junit:$jUnitVersion"); Kotlin @Test example with org.junit.Test and org.junit.Assert; android.jar stub methods whose code "has been removed" so "the test throws an exception" ("Method ... not mocked"); returnDefaultValues is "a last resort".
- https://developer.android.com/studio/test/test-in-android-studio : local unit tests at module-name/src/test/java/, instrumented tests at module-name/src/androidTest/java/; create a test with Control+Shift+T and choose JUnit4; run with the gutter "Run test" icon or right-click then Run; results appear in the Run window; sync the project first.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/check.html : check(value, lazyMessage) throws IllegalStateException when the value is false.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/run-catching.html : runCatching returns the result of the block, "catching any Throwable".
- Not in the pages I read, so labelled "general knowledge" in the text: that the runner skips functions without @Test.
- Deviations from author data (docs/practice/interview-testing.md): its RuntimeException, mockk and Robolectric points are unsourced, so only the documented "throws an exception / not mocked" behaviour is claimed. No JUnit version number is printed, because the docs use a variable and the template version was not checked.
-->

## The question

You wrote a function that adds up a cart. You want to know if it is right. Do you have to install the app on a phone and tap through it? Where do you write the check, and how do you run it?

## Mental model

Think of a practice net at a cricket ground.

- The match is the full app running on a phone, with everyone watching.
- The net is a small space next to the ground. You practise there alone.
- One shot is one call to one function.
- The coach is the check. The coach knows what a good shot looks like and says yes or no.
- The scoreboard tick is the green mark for a pass. A red mark is a miss.

At the net you do not wait for a match. You play one shot, the coach answers in a second, and you play it again.

**Where the analogy stops:** The net has a real ball and a real bat, so what you learn there carries over to the match. A local test has no real phone. If your code asks Android for something, the test can fail, because the Android parts are empty stand-ins. Also, a net does not prove you are ready for a match. A passing test only covers what it checks.

## Build the logic

### Notice

You have a function `cartTotal` that adds a list of prices. You think it is right. To see it work, you would have to build the app, install it, open the cart screen and add two items.

That takes minutes. The function itself is only a few lines. It does not need a screen to give an answer.

### Guess

How could you check `cartTotal` without a phone?

- A. Install the app and tap through the cart screen.
- B. Read the code again and decide it looks right.
- C. Call the function from a small piece of code on your computer, with a known input, and compare the answer with the one you expect.

Idea A works, and it is the slow match. Idea B is only a guess, and it misses bugs. Idea C is the net.

### Build

1. Pick the function to check, here `cartTotal`.
2. Write a small test function. It calls `cartTotal(listOf(10, 20))` and compares the answer with 30, which you worked out by hand.
3. Mark the function as a test, so the test runner knows to run it.
4. Put it in the test folder, so it is kept apart from the code that ships in the app.
5. Press run. A green tick means every check passed. A red mark shows which check failed and why.
6. Change the code and press run again.

### Name it

- A **unit test** is a small test that checks one small piece of code, such as one function.
- A **local test** is a test that runs on your own computer. The Android documentation says it uses your local Java Virtual Machine (JVM) and not a device or an emulator.
- The **test folder** is `src/test` inside your module. The documentation says local test sources go in `module-name/src/test/`, and that this folder already exists in a new Android Studio project.
- **JUnit** is the testing library that most Android projects use. It supplies the test marker and the checks.
- An **annotation** is a label written with an `@` before a function. The **`@Test`** annotation marks a function as a test.
- An **assertion** is a check inside a test. `assertEquals(expected, actual)` is one, and it fails when the two values differ.
- An **instrumented test** is a test that runs on a device or emulator. Its code goes in `src/androidTest`, apart from `src/test`.

In Android Studio, the documentation says you run a test by pressing the **Run test** icon in the gutter next to the test, or by right-clicking it and choosing Run. The results appear in the Run window. The documentation also says to sync the project with Gradle first.

Here is what a real test looks like. It needs JUnit, so it only builds inside an Android project, in the `src/test` folder.

```kotlin
// does not compile
// It needs JUnit, so it builds only inside an Android project.
import org.junit.Assert.assertEquals
import org.junit.Test

class CartTest {
    @Test
    fun addsThePrices() {
        assertEquals(30, cartTotal(listOf(10, 20)))
    }
}
```

In this lesson you can run a plain-Kotlin model of a test. It is not real JUnit. A function called `check` stops with an error when its condition is false. A small runner catches that error and prints a result line.

```kotlin
// A model in plain Kotlin, not real JUnit.
fun runOne(name: String, test: () -> Unit) {
    val result = runCatching(test)
    println(if (result.isSuccess) "PASS $name" else "FAIL $name")
}

fun cartTotal(prices: List<Int>): Int = prices.sum()

fun main() {
    runOne("addsThePrices") { check(cartTotal(listOf(10, 20)) == 30) }
}
```

`check` throws an `IllegalStateException` when its condition is false, according to the Kotlin documentation. `runCatching` runs the function and catches any error. `isSuccess` is true when nothing was thrown. The program prints `PASS addsThePrices`.

## Predict the output

```challenge
type: predict
id: ten-percent-off
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  fun runOne(name: String, test: () -> Unit) {
      val result = runCatching(test)
      println(if (result.isSuccess) "PASS $name" else "FAIL $name")
  }

  fun discounted(price: Int): Int = price - price / 10

  fun main() {
      runOne("tenPercentOff95") { check(discounted(95) == 85) }
  }
options:
  - id: a
    text: "PASS tenPercentOff95"
    why: "You may have worked out 10 percent of 95 as 9.5 and taken it off. The code divides whole numbers, so 95 divided by 10 is 9, and the answer is 86."
  - id: b
    text: "FAIL tenPercentOff95"
    correct: true
    why: "The coach expected 85, and the shot gave 95 minus 9, which is 86. The check fails, and the runner prints FAIL."
  - id: c
    text: "Nothing is printed, because the program stops with an error"
    why: "You may have thought a failed check ends the program. The runner catches the error and prints a result line, as a test runner does."
output: |
  FAIL tenPercentOff95
```

```challenge
type: predict
id: the-test-nobody-runs
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  fun runOne(name: String, test: () -> Unit) {
      val result = runCatching(test)
      println(if (result.isSuccess) "PASS $name" else "FAIL $name")
  }

  fun checkOne() = check(1 + 1 == 2)
  fun checkTwo() = check(1 + 1 == 3)

  fun main() {
      runOne("checkOne", ::checkOne)
  }
options:
  - id: a
    text: "FAIL checkTwo"
    why: "You may have thought every function with check in it runs. The runner runs only what it is handed, and checkTwo is never handed over."
  - id: b
    text: "PASS checkOne, then FAIL checkTwo"
    why: "You may have counted both functions. Only checkOne is passed to the runner, so checkTwo never gets a turn."
  - id: c
    text: "PASS checkOne"
    correct: true
    why: "The coach only watches the shots that are played. checkTwo would fail, but nobody plays it, so a green board hides it."
output: |
  PASS checkOne
```

## Try it

1. **Write.** Type the plain-Kotlin model above into the editor: `runOne`, `cartTotal` and `main`.
2. **Run.** You should see one line, `PASS addsThePrices`.
3. **Break.** Change the 30 in the `check` to 31 and change nothing else. Run it again.
4. **Fix.** The output is now `FAIL addsThePrices`, because the expected number is wrong. Change 31 back to 30 and run it. It prints `PASS addsThePrices` again.

## Fix the bug

```challenge
type: fix
id: fix-the-unlisted-test
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  // A model in plain Kotlin, not real JUnit.
  // A test only runs if it is on the list the runner is given.
  fun runAll(tests: Map<String, () -> Unit>) {
      for ((name, test) in tests) {
          val result = runCatching(test)
          println(if (result.isSuccess) "PASS $name" else "FAIL $name")
      }
  }

  fun cartTotal(prices: List<Int>): Int = prices.sum()

  fun totalOfTwo() = check(cartTotal(listOf(10, 20)) == 30)
  fun totalOfNone() = check(cartTotal(emptyList()) == 0)
  fun totalWithNegative() = check(cartTotal(listOf(10, -5)) == 5)

  fun main() {
      runAll(
          mapOf(
              "totalOfTwo" to ::totalOfTwo,
              "totalOfNone" to ::totalOfNone
          )
      )
  }
hints:
  - "Count the test functions, then count the lines of output. Do they match?"
  - "A test that is not on the runner's list is never played. Add the missing one."
expected: |
  PASS totalOfTwo
  PASS totalOfNone
  PASS totalWithNegative
fix: |
  // A model in plain Kotlin, not real JUnit.
  // A test only runs if it is on the list the runner is given.
  fun runAll(tests: Map<String, () -> Unit>) {
      for ((name, test) in tests) {
          val result = runCatching(test)
          println(if (result.isSuccess) "PASS $name" else "FAIL $name")
      }
  }

  fun cartTotal(prices: List<Int>): Int = prices.sum()

  fun totalOfTwo() = check(cartTotal(listOf(10, 20)) == 30)
  fun totalOfNone() = check(cartTotal(emptyList()) == 0)
  fun totalWithNegative() = check(cartTotal(listOf(10, -5)) == 5)

  fun main() {
      runAll(
          mapOf(
              "totalOfTwo" to ::totalOfTwo,
              "totalOfNone" to ::totalOfNone,
              "totalWithNegative" to ::totalWithNegative
          )
      )
  }
explanation: "The third test was written and never handed to the runner, so the board stayed green without it. A real test needs its marker for the same reason."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 2 is the useful one. Only the tests on the list run, in the order of the list. Nothing warns you about a test that is not on it.

## Tricks to remember

- Net before match: test one function alone, in a second.
- Local test folder is `src/test`. Device tests live in `src/androidTest`.
- No marker, no run. A function without `@Test` is only a function (general knowledge).
- Work out the expected answer by hand, then write it into the check.
- Task: make a test function for a tiny calculation, run it from the gutter icon, change the expected number so it fails, then change it back.

## Common mistakes

- **Thinking I must install the app on a phone to test my code.** Tapping through the app is how you see it work, so it feels like the only way. A local test runs on your computer's JVM, and the function does not need a screen.
- **Writing a test and never seeing it run.** A passing board feels like good news. If the marker is missing, or the test is not on the list, it never runs. Make it fail once on purpose, to see that it runs at all.
- **Calling Android code inside a local test.** The code looks like any other function call. The Android documentation says the Android library used in local tests has its method bodies removed, so a call throws an exception with a "not mocked" message. Keep the logic you test free of Android calls.
- **Putting the test in the wrong folder.** Both folders look alike. The documentation puts local tests in `src/test` and instrumented tests in `src/androidTest`. Ask whether the test needs a device.

## Where this shows up in Android

You change the cart code, and you want to know it still adds up. You press the Run test icon next to your test, and the Run window shows green in a second. No phone is plugged in.

A different symptom: a test that calls `Log.d` or reads a `Context` fails with a "not mocked" message. The local-tests documentation explains why. The Android library in a local test has no real method bodies, so a call into it throws an exception. The documentation offers a setting called `returnDefaultValues`, and calls it a last resort, because the empty values it returns can let failing tests pass.

## Say it in an interview

**What is a unit test, and where do local tests live in an Android project?**
A unit test checks one small piece of code, such as one function. The Android documentation says local test sources go in `module-name/src/test/`, and instrumented tests, which run on a device, go in `src/androidTest`. I choose the folder by asking whether the test needs a device.

**How do you write and run a local test without an emulator?**
I write a function with the `@Test` annotation in `src/test`, call my code and use an assertion such as `assertEquals`. The documentation says local tests run on the workstation's JVM. In Android Studio I press the Run test icon in the gutter, and the results show in the Run window.

**Why do local tests run faster than instrumented tests?**
They need no device or emulator, because the documentation says they run on your own workstation's JVM. My own reasoning: skipping the install and the device start-up removes most of the waiting, so I can run them after every small change.

**What goes wrong when a unit test uses Android classes like `Log` or `Context`?**
The documentation says the Android library used for local tests has its method bodies removed, so a call throws an exception, with an error such as "Method ... not mocked". The option `returnDefaultValues` hides it, and the documentation calls it a last resort. My own reasoning: it is better to keep the logic in plain Kotlin that does not touch Android, and test that.

## Recap

1. A local unit test is a small marked function in `src/test` that checks one piece of code.
2. It runs on your computer's JVM, so it needs no phone or emulator.
3. A test that is never run proves nothing, so make a new test fail once.

## You can now

- Predict whether a described test passes or fails.
- Fix a test that never runs because it is not on the list.
- Explain, using the practice net, why a local test needs no phone.
