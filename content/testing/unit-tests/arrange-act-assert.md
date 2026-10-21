---
id: testing/unit-tests/arrange-act-assert
title: "What are the three parts of a test that anyone can read?"
status: live
difficulty: easy
order: 3
requires: [testing/unit-tests/run-your-first-unit-test]
summary: A readable test sets things up, does one thing, then checks the result, in that order.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [experiment-write-up]
playground: true
fruit: false
upnext: false
---

## The question

A test fails, and the report says only "FAIL". You open the test and find 30 lines of mixed work: building things, calling the code, and checking answers, all tangled together. Which line broke it? How should a test be laid out so that anyone can read it and see what went wrong?

## Mental model

Think of a science experiment write-up.

- The equipment is what you set up before you start: the beakers, the weights, the starting amounts.
- The one thing you do is the single step of the experiment, such as heating the water.
- The note of what you saw is the result you write down and compare with what you expected.
- The title of the write-up says what was tested and under which conditions.

A good write-up always has the same three parts, in the same order. Set up, do one thing, record what you saw. A reader can find any of them in a second.

**Where the analogy stops:** A scientist may run many steps and watch many results in one experiment. A test does one thing and checks it. Also, a scientist writes the result after the experiment, but a test states the expected result before it runs, so the test can say that something is wrong.

## See it move

```sim
id: experiment-write-up
template: stepper
title: "A test written as an experiment write-up"
intro: "A write-up has three parts: set the equipment up, do the one thing, record what you saw. Nothing has run yet, so the board is empty."
lines:
  - { id: h1, text: "// Arrange: set the equipment up" }
  - { id: l1, text: "val cart = Cart()" }
  - { id: l2, text: "cart.add(10)" }
  - { id: l3, text: "cart.add(20)" }
  - { id: l4, text: "val expected = 30 (31 when the switch is on)" }
  - { id: h2, text: "// Act: do the one thing" }
  - { id: l5, text: "val total = cart.total()" }
  - { id: h3, text: "// Assert: record what you saw" }
  - { id: l6, text: "check(total == expected)" }
code: true
board:
  - { var: items, label: "Items in the cart" }
  - { var: expected, label: "expected" }
  - { var: total, label: "total" }
  - { var: verdict, label: "Verdict" }
vars: { pc: 1, items: 0, expected: "not yet", total: "not yet", verdict: "not yet", wrong: "off" }
actions:
  - id: next
    label: "Run the next line"
    cases:
      - when: { var: { pc: 1 } }
        do:
          - set: { pc: 2, items: 0 }
          - light: [l1]
          - say: "Arrange: the equipment is set up, an empty cart."
      - when: { var: { pc: 2 } }
        do:
          - set: { pc: 3, items: 1 }
          - light: [l2]
          - say: "Arrange: an item priced 10 goes in the cart."
      - when: { var: { pc: 3 } }
        do:
          - set: { pc: 4, items: 2 }
          - light: [l3]
          - say: "Arrange: an item priced 20 goes in. The cart holds two items."
      - when: { var: { pc: 4, wrong: "off" } }
        do:
          - set: { pc: 5, expected: 30 }
          - light: [l4]
          - say: "Arrange: the number we expect is written down before anything runs: 30."
      - when: { var: { pc: 4, wrong: "on" } }
        do:
          - set: { pc: 5, expected: 31 }
          - light: [l4]
          - say: "Arrange: this time the number we expect is 31."
      - when: { var: { pc: 5 } }
        do:
          - set: { pc: 6, total: 30 }
          - light: [l5]
          - say: "Act: the one thing we test runs. The cart says its total is 30."
      - when: { var: { pc: 6, wrong: "off" } }
        do:
          - set: { pc: 7, verdict: "pass" }
          - light: [l6]
          - say: "Assert: what we saw, 30, equals what we expected, 30, so the test passes."
      - when: { var: { pc: 6, wrong: "on" } }
        do:
          - set: { pc: 7, verdict: "fail" }
          - light: [l6]
          - say: "Assert: we saw 30 but expected 31, so the test fails, and the failure points at this line."
      - when: { var: { pc: 7 } }
        do:
          - say: "The test is finished. Press Start again to run it once more."
  - id: again
    label: "Start again"
    do:
      - set: { pc: 1, items: 0, expected: "not yet", total: "not yet", verdict: "not yet" }
      - light: []
      - say: "The write-up starts again from the first line."
toggles:
  - { id: wrong, label: "Expect 31 instead of 30", var: wrong }
script: [next, next, next, next, next, next]
try:
  - text: 'Press "Run the next line" four times. The four Arrange lines light up one at a time.'
    done: [next, next, next, next]
  - text: 'Press "Run the next line" twice more. The Act line runs, then the Assert line gives the verdict.'
    done: [next, next]
  - text: 'Press "Start again" and switch on "Expect 31 instead of 30". Press "Run the next line" six times and watch the Assert line fail.'
    done: [again, "wrong=on", next, next, next, next, next, next]
    optional: true
```

**What you should notice:** The Arrange lines only build things, the Act is one line, and only the Assert line can say pass or fail, so a failure points straight at it.

## Build the logic

### Notice

You test a cart that adds up prices. The test makes a cart, adds a price of 10 and a price of 20, asks for the total, and checks that it is 30. It does these things in that order. When it fails, you want to know in one look: was the cart built wrongly, was the total wrong, or was the expected number wrong?

### Guess

How should the test be laid out so a failure is quick to read?

- A. Mix everything: check as you go, build more as you go, call the code more than once.
- B. Build everything first, then do one call, then check the result.
- C. Put the check first, so you know what you want before you start.

Idea A makes every line a suspect. Idea C cannot work, because there is nothing to check before the code has run. Idea B is the write-up.

### Build

1. Set the equipment up: make the cart, put the prices in, and write down the number you expect.
2. Do one thing: ask the cart for its total. Keep the answer in a named box.
3. Record what you saw: compare the named box with the number you expected.
4. If they differ, say both numbers in the message: "expected 30 but saw 40".
5. Give the test a name that says what is tested and when, so a report is readable before you open the test.
6. Make each test set up its own equipment. A test must not rely on what another test left behind.

### Name it

The three parts are called **Arrange**, **Act** and **Assert**, or **AAA** for short. Arrange sets up the objects and the numbers. Act calls the code you are testing, once. Assert checks the result against the expected one. These names are general knowledge and not an Android rule. The Android documentation shows the same shape with comments: a Given part, a when part and a then part. It also says a test method contains the code to exercise and verify a single aspect of the component.

An **assertion** is the check in the last part. In plain Kotlin, `check(condition)` stops with an `IllegalStateException` when its condition is false, according to the Kotlin documentation. The form `check(condition) { message }` uses the result of the message block as the message of that exception. The **expected** value is the number you wrote down, and the **actual** value is what the code gave.

In a real Android project the same test uses JUnit. The Android documentation says local test sources go in `module-name/src/test/` and a test method begins with the `@Test` annotation. The next block needs JUnit, so it builds only inside an Android project.

```kotlin
// does not compile
// It needs JUnit, so it builds only inside an Android project.
import org.junit.Assert.assertEquals
import org.junit.Test

class CartTest {
    @Test
    fun totalOfTwoItems() {
        // Arrange
        val cart = Cart()
        cart.add(10)
        cart.add(20)
        val expected = 30

        // Act
        val total = cart.total()

        // Assert
        assertEquals(expected, total)
    }
}
```

In this lesson you can run a plain-Kotlin model, as in the last lesson. It is not JUnit. A small runner catches the error that `check` throws and prints one line, with the message when the test fails.

```kotlin
// A model in plain Kotlin, not real JUnit.
class Cart {
    private val prices = mutableListOf<Int>()
    fun add(price: Int) { prices.add(price) }
    fun total(): Int = prices.sum()
}

fun runOne(name: String, test: () -> Unit) {
    val failure = runCatching(test).exceptionOrNull()
    if (failure == null) println("PASS $name") else println("FAIL $name: ${failure.message}")
}

fun main() {
    runOne("totalOfTwoItems") {
        // Arrange: set the equipment up
        val cart = Cart()
        cart.add(10)
        cart.add(20)
        val expected = 30

        // Act: do the one thing
        val total = cart.total()

        // Assert: record what you saw
        check(total == expected) { "expected $expected but saw $total" }
    }
}
```

It prints `PASS totalOfTwoItems`. `runCatching` runs the test and catches any error, and `exceptionOrNull()` gives that error, or nothing when the test passed.

## Predict the output

```challenge
type: predict
id: which-test-fails-and-says-what
verified: kotlin-2.4.21
code: |
  class Cart {
      private val prices = mutableListOf<Int>()
      fun add(price: Int) { prices.add(price) }
      fun total(): Int = prices.sum()
  }

  fun runOne(name: String, test: () -> Unit) {
      val failure = runCatching(test).exceptionOrNull()
      if (failure == null) println("PASS $name") else println("FAIL $name: ${failure.message}")
  }

  fun main() {
      runOne("emptyCartIsZero") {
          val cart = Cart()
          val expected = 0
          val total = cart.total()
          check(total == expected) { "expected $expected but saw $total" }
      }
      runOne("twoItems") {
          val cart = Cart()
          cart.add(10)
          cart.add(20)
          val expected = 40
          val total = cart.total()
          check(total == expected) { "expected $expected but saw $total" }
      }
  }
options:
  - id: a
    text: "PASS emptyCartIsZero, then PASS twoItems"
    why: "You may have read the expected 40 as the answer the cart gives. The cart holds 10 and 20, so its total is 30, and the note of what we expected does not match."
  - id: b
    text: "PASS emptyCartIsZero, then FAIL twoItems: expected 40 but saw 30"
    correct: true
    why: "The empty cart totals 0, so the first write-up matches. In the second, the Arrange part wrote down 40, the Act gave 30, and the Assert part says both numbers."
  - id: c
    text: "PASS emptyCartIsZero, then FAIL twoItems: expected 30 but saw 40"
    why: "You may have swapped the two numbers. The message names the expected number first, and that is the one written in the Arrange part, 40."
output: |
  PASS emptyCartIsZero
  FAIL twoItems: expected 40 but saw 30
```

```challenge
type: predict
id: a-second-act-hides-the-cause
verified: kotlin-2.4.21
code: |
  class Cart {
      private val prices = mutableListOf<Int>()
      fun add(price: Int) { prices.add(price) }
      fun total(): Int = prices.sum()
      fun clear() { prices.clear() }
  }

  fun runOne(name: String, test: () -> Unit) {
      val failure = runCatching(test).exceptionOrNull()
      if (failure == null) println("PASS $name") else println("FAIL $name: ${failure.message}")
  }

  fun main() {
      runOne("addThenClear") {
          val cart = Cart()
          cart.add(10)
          cart.add(20)
          cart.clear()
          val expected = 30
          val total = cart.total()
          check(total == expected) { "expected $expected but saw $total" }
      }
  }
options:
  - id: a
    text: "PASS addThenClear"
    why: "You may have skipped the line cart.clear(). It runs before the Act, so the cart is empty by the time the total is asked for."
  - id: b
    text: "FAIL addThenClear: expected 30 but saw 10"
    why: "You may have thought clear() removes only the last price. It removes every price, so nothing is left to add up."
  - id: c
    text: "FAIL addThenClear: expected 30 but saw 0"
    correct: true
    why: "The equipment was set up with two prices and then a second act emptied it. The failure message says 0, and nothing in it tells you the extra act is the cause."
output: |
  FAIL addThenClear: expected 30 but saw 0
```

## Try it

1. **Write.** Type the plain-Kotlin model from Name it into the editor: `Cart`, `runOne` and `main` with the one test.
2. **Run.** You should see one line, `PASS totalOfTwoItems`.
3. **Break.** Change `val expected = 30` to `val expected = 31` and change nothing else. Run it again. The line now says `FAIL totalOfTwoItems: expected 31 but saw 30`. The message names both numbers, so you can see at once that the expected number is the wrong one.
4. **Fix.** Change 31 back to 30 and run it. You should see `PASS totalOfTwoItems` again.

## Fix the bug

```challenge
type: fix
id: fix-shared-cart-between-tests
verified: kotlin-2.4.21
code: |
  class Cart {
      private val prices = mutableListOf<Int>()
      fun add(price: Int) { prices.add(price) }
      fun total(): Int = prices.sum()
  }

  fun runOne(name: String, test: () -> Unit) {
      val failure = runCatching(test).exceptionOrNull()
      if (failure == null) println("PASS $name") else println("FAIL $name: ${failure.message}")
  }

  val sharedCart = Cart()

  fun main() {
      runOne("oneItem") {
          sharedCart.add(10)
          val total = sharedCart.total()
          check(total == 10) { "expected 10 but saw $total" }
      }
      runOne("twoItems") {
          sharedCart.add(10)
          sharedCart.add(20)
          val total = sharedCart.total()
          check(total == 30) { "expected 30 but saw $total" }
      }
  }
hints:
  - "Run it and read the second line. The cart should hold 10 and 20, so where did the extra 10 come from?"
  - "Each test must arrange its own equipment. Make a new cart inside each test."
expected: |
  PASS oneItem
  PASS twoItems
fix: |
  class Cart {
      private val prices = mutableListOf<Int>()
      fun add(price: Int) { prices.add(price) }
      fun total(): Int = prices.sum()
  }

  fun runOne(name: String, test: () -> Unit) {
      val failure = runCatching(test).exceptionOrNull()
      if (failure == null) println("PASS $name") else println("FAIL $name: ${failure.message}")
  }

  fun main() {
      runOne("oneItem") {
          val cart = Cart()
          cart.add(10)
          val total = cart.total()
          check(total == 10) { "expected 10 but saw $total" }
      }
      runOne("twoItems") {
          val cart = Cart()
          cart.add(10)
          cart.add(20)
          val total = cart.total()
          check(total == 30) { "expected 30 but saw $total" }
      }
  }
explanation: "The first test left a price in the shared cart, so the second test started with extra equipment. A new cart in each test means each write-up starts clean."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 2 is the useful one. The tests run in the order written, and the second one sees what the first one left in the shared cart. This code has no null values in play.

## Tricks to remember

- Set up, do one thing, record what you saw. Same order every time.
- One Act per test, so a failure has one suspect.
- Say both numbers in the failure message: expected, then saw.
- Each test brings its own equipment. Nothing is shared between write-ups.
- Task: write a test for any small function with the comments `// Arrange`, `// Act` and `// Assert`, then change the expected number so it fails.

## Common mistakes

- **Putting every check for the function in one test.** One long test feels thorough, and it saves typing. When it fails, you do not know which check broke. Write one test for each thing the function should do, with one Act each.
- **Calling the code twice in one test.** A second Act feels like a small extra. The failure message then fits two causes, as in the `addThenClear` item. Move the second call into its own test.
- **Sharing equipment between tests.** A shared cart saves a line in each test. The tests now depend on order, and one test can break another. Make a new one in each Arrange.
- **Giving a test a name like `test1`.** It feels fine while you write it. A report that says `test1` failed tells nobody what broke. Name the test for what and when, for example `totalOfTwoItems`.

## Where this shows up in Android

A CI report lists one failed test, and you can read its name and the line `expected 40 but saw 30` without opening the code. That works because the test stated its expected number first and kept the Act to one call (my own reasoning).

A second symptom is a test that passes when you run it alone and fails when you run the whole class. My own reasoning, from general knowledge: an earlier test left something behind, as the shared cart did in the Fix the bug task. Local tests live in `src/test` and a test method starts with `@Test`, as the Android documentation says.

## Say it in an interview

**What are the three phases of the Arrange-Act-Assert pattern?**
Arrange sets up the objects and the data. Act calls the one piece of code under test. Assert checks the result against the expected one. This is general knowledge. The Android local-tests page shows the same shape with Given, when and then comments.

**Why does separating the phases help whoever has to maintain the test?**
Each part has one job, so a reader finds the setup, the call and the check without hunting. When it fails, the assert line names the expected and actual values, and the act is one call, so there is one suspect. My own reasoning: a test that tangles the three needs a reread every time it fails.

**What belongs in Arrange and what belongs in Act?**
Arrange builds everything the test needs, including the expected number. Act is the one call to the code under test, and nothing else. If I find myself calling the code in Arrange, that is a sign of a second act, and I would move it into its own test. This answer is my own reasoning.

**What goes wrong when tests mix setup, calls and checks, or share objects?**
My own reasoning: a failure fits many causes, so people spend time guessing. Shared objects make tests depend on their order, so a test passes alone and fails in the full run. I would give each test its own setup and one Act, and I would name it for what and when.

## Recap

1. A readable test has three parts in order: Arrange, Act, Assert.
2. A test makes one call to the code under test, so a failure has one suspect.
3. Each test sets up its own equipment and says both numbers when it fails.

## You can now

- Name the three parts of a test and say what each one does.
- Predict the output line of a short test, including its failure message.
- Fix a test that shares equipment with another test.
