---
id: testing/why-test/why-tests-exist
title: What does a test give me that tapping through the app does not?
status: shadow
difficulty: easy
order: 1
requires: []
summary: A test is a small program that checks your code the same way every time. It finds a broken feature in seconds, so you need not remember to look.
video: ""
publishedOn:
updatedOn:
sims: [tests-lamps]
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/training/testing/fundamentals ("manual testing scales poorly" and regressions in your app's behavior can be overlooked; automated testing "is faster, more repeatable, and generally gives you more actionable feedback about your app earlier"; local tests "execute on your development machine or a server" and are "usually small and fast"). The page does not define regression, so the definition here is marked as general knowledge. The page does not say how local tests run, so nothing is claimed about the JVM. Interview data: docs/practice/interview-testing.md (test-driven development and living documentation are not on the page, so they are left out). The kotlin blocks and outputs are unchanged from the earlier draft, whose notes record runs on Kotlin 2.4.0 only; none were run for this rewrite, so every challenge says not-run and a 2.4.21 run is still needed. Sim tests-lamps is new, see docs/sims/needed-ids.md. -->

## The question

You fix a bug in the checkout screen, tap through it once, and it works. Two days later a user says the cart total is wrong on another screen that you never touched. You cannot tap through every screen after every change. So how do you find out that your fix broke something else? And how do you find out fast?

## Mental model

Think of a pilot's pre-flight checklist.

- The plane is your app.
- The checklist is a written list of checks: flaps, fuel, radio, brakes.
- Each tick on the list is one check.
- The pilot does the same checks, in the same order, on every flight, even after flying the same plane for years.

A pilot does not trust memory and does not think "it flew fine yesterday". The list does the remembering. If one check fails, the plane does not take off.

**Where the analogy stops:** A pilot goes through a checklist by hand, and it can take ten minutes. A test checklist is run by a computer, so it can run hundreds of checks in a second. Also, a pilot checks the real plane. A test often checks one small part of the app on its own, without the rest of the app around it.

## See it move

<!-- SIM: tests-lamps -->

**Try this:**
1. Look at the twenty lamps. Each lamp is one feature of an app. Change one line of the app's code.
2. Start the "tap by hand" clock and count how long it takes to find the lamp that turned red.
3. Reset, change the same line, and press "Run the checklist".

**What you should notice:** One lamp turns red after the change, and the checklist finds it within a second, while tapping by hand takes minutes.

## Build the logic

### Notice

Your app has a function that works out the price of an order. Tonight you change it. Another screen also uses that function, and nobody opens that screen to look.

Now picture a list written in advance: "is 3 pieces at 10 each worth 30?" and "is 0 pieces worth 0?". After your change, somebody reads the answers off that list in a second.

### Guess

How should you find out that a change broke an old feature?

- A. Run the app and tap through the screens you remember.
- B. Ask a friend to tap through it for you.
- C. Write the questions and the right answers down once, and let the computer ask them every time.

Idea A forgets the screens you do not remember. Idea B is the same as A with more people and more time. Idea C is the checklist, and it never forgets.

### Build

1. Pick one small piece of the app, for example the price function.
2. Write down one question for it, with the answer you expect.
3. Run the piece, and compare what it really gave with the answer you wrote.
4. Print whether they matched.
5. Keep the list. Run it again after every change.

If every line says it matched, the plane may fly. If one line says it did not match, you know exactly which check to look at.

### Name it

- A **test** is a small program that runs a piece of your code and compares the result with the answer you expected.
- A **check** is one question and its expected answer, one tick on the list.
- A **regression** is an old feature that used to work and broke after a change (general knowledge).

A real project uses a testing library for this. Here you write the check by hand. Ask the question with `==`, which gives `true` when two values are equal, and print it.

```kotlin
fun price(quantity: Int, unit: Int): Int = quantity * unit

fun main() {
    println("3 at 10: ${price(3, 10) == 30}")
    println("0 at 10: ${price(0, 10) == 0}")
}
```

The `${...}` part puts the answer into the printed text. Each line is a check. It prints `true` when the answer matched what we expected, and `false` when it did not.

## Predict the output

```challenge
type: predict
id: break-the-price
verified: not-run
code: |
  fun price(quantity: Int, unit: Int): Int = quantity + unit

  fun main() {
      println("3 at 10: ${price(3, 10) == 30}")
      println("0 at 10: ${price(0, 10) == 0}")
      println("5 at 0: ${price(5, 0) == 0}")
      println("2 at 2: ${price(2, 2) == 4}")
  }
options:
  - id: a
    text: "true, true, true, true"
    why: "You may have read the old version of the function. Someone changed the `*` to a `+`. The checklist is there to catch that."
  - id: b
    text: "false, false, false, false"
    why: "You may have thought a broken plane fails every check. This break happens to give the right answer for 2 and 2, so one check does not notice it."
  - id: c
    text: "false, false, false, true"
    correct: true
    why: "The checklist ticks each check on its own. 3 plus 10 is 13, not 30, so the check fails. 2 plus 2 is 4, which is also 2 times 2, so that one check still passes."
output: |
  3 at 10: false
  0 at 10: false
  5 at 0: false
  2 at 2: true
```

```challenge
type: predict
id: one-check-misses
verified: not-run
code: |
  fun twice(n: Int): Int = n * n

  fun main() {
      println("twice(2) is 4: ${twice(2) == 4}")
      println("twice(3) is 6: ${twice(3) == 6}")
      println("twice(0) is 0: ${twice(0) == 0}")
  }
options:
  - id: a
    text: "true, false, true"
    correct: true
    why: "3 times 3 is 9, not 6, so the middle check fails. 2 and 0 give the right answer by luck, so those two ticks pass. A checklist only catches what it checks."
  - id: b
    text: "true, true, true"
    why: "You may have read `twice` by its name. The code multiplies n by itself, which is not twice n. A check only knows the answer you wrote down."
  - id: c
    text: "false, false, false"
    why: "You may have thought the name is wrong everywhere. For 2 and for 0, the function happens to give the expected answer."
output: |
  twice(2) is 4: true
  twice(3) is 6: false
  twice(0) is 0: true
```

## Try it

1. **Write.** Type this program into the editor.

```kotlin
fun price(quantity: Int, unit: Int): Int = quantity * unit

fun main() {
    println("3 at 10: ${price(3, 10) == 30}")
    println("0 at 10: ${price(0, 10) == 0}")
    println("5 at 0: ${price(5, 0) == 0}")
    println("2 at 2: ${price(2, 2) == 4}")
}
```

2. **Run.** You should see four lines, and every one of them says `true`. All four checks pass.
3. **Break.** Change the `*` in `price` to a `+`. Run it again. Three lines say `false` and the last line still says `true`.
4. **Fix.** Change the `+` back to `*`. Run it. All four lines say `true` again. You found the break without opening any screen.

## Fix the bug

```challenge
type: fix
id: fix-the-discount
verified: not-run
code: |
  // Takes a percentage off a price.
  // For example, 10 percent off 200 should give 180.
  fun withDiscount(price: Int, percent: Int): Int = price - price * percent

  fun main() {
      // Each line prints true when the answer matches what we expect.
      println("200 off 10 percent: ${withDiscount(200, 10) == 180}")
      println("50 off 20 percent: ${withDiscount(50, 20) == 40}")
      println("100 off 0 percent: ${withDiscount(100, 0) == 100}")
      println("10 off 100 percent: ${withDiscount(10, 100) == 0}")
  }
hints:
  - "Look at the line that starts with fun. Work out by hand what 200 minus 200 times 10 gives."
  - "A percentage is a part of 100. Divide by 100 before you take it off."
expected: |
  200 off 10 percent: true
  50 off 20 percent: true
  100 off 0 percent: true
  10 off 100 percent: true
fix: |
  // Takes a percentage off a price.
  // For example, 10 percent off 200 should give 180.
  fun withDiscount(price: Int, percent: Int): Int = price - price * percent / 100

  fun main() {
      // Each line prints true when the answer matches what we expect.
      println("200 off 10 percent: ${withDiscount(200, 10) == 180}")
      println("50 off 20 percent: ${withDiscount(50, 20) == 40}")
      println("100 off 0 percent: ${withDiscount(100, 0) == 100}")
      println("10 off 100 percent: ${withDiscount(10, 100) == 0}")
  }
explanation: "Ten percent is ten out of a hundred, so the amount taken off is price times percent divided by 100."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

## Tricks to remember

- Checklist, not memory. The list does the remembering.
- A check is a question with a written-down answer.
- A passing list is not proof of a perfect app. It only covers what it checks.
- Run the list after every change, not only when you feel unsure.

## Common mistakes

- **Thinking tests are extra work for big teams.** Tapping through the app feels quicker when the app is small. But the cost of tapping grows with every feature you add, and a test costs almost nothing to run again. Start with one check for the part you changed today.
- **Thinking all checks passing means no bugs.** A full row of `true` feels like proof. In the second Predict item, two checks passed on broken code. Add a check for a case that would fail.
- **Writing the expected answer by copying what the code gave.** It feels fast, and the check will pass. But then the check only repeats the bug. Work out the right answer by hand first, then write it down.

## Where this shows up in Android

Picture a shopping app. A teammate changes the function that adds up the cart, and the order screen still works. A week later a user sees the wrong total on the invoice screen, which calls the same function. That symptom is a regression, and a short list of checks on the cart function would have shown it on the day of the change.

The Android testing documentation says manual testing scales poorly and that regressions in your app's behavior can slip past you. It says automated testing is faster, more repeatable and gives feedback earlier. It also says local tests run on your development machine or a server and are usually small and fast. That is the kind of check this lesson wrote by hand.

## Say it in an interview

**What does an automated test give you that tapping through the app does not?**
It runs the same checks the same way every time, so a change that breaks an old feature shows up fast. The Android documentation says automated testing is faster and more repeatable. Tapping scales poorly, because every new feature adds more screens to check by hand.

**Does a passing test suite prove the app has no bugs?**
No. A test only checks the cases someone wrote down. In this lesson, two checks passed on broken code. So I also add a check for a case that would fail, and I work out the expected answer by hand before I write it.

**How do tests help when you change old code?**
My own reasoning: the tests are a checklist that still works after the change. If a check turns red, you know which behaviour broke, and you can fix it before a user finds it. Without them, you rely on remembering every place the code is used.

**What goes wrong if a team relies only on manual testing?**
The Android documentation says manual testing scales poorly and that regressions can slip past you. A change in one place can break a screen nobody opens again before release. The trade-off is that writing tests costs time up front, but running them again costs almost nothing.

## Recap

1. A test runs a piece of code and compares the answer with one you wrote in advance.
2. Running the list after every change finds an old feature that broke.
3. A passing list only covers the cases it checks.

## You can now

- Predict which checks print `true` and which print `false` after a change to a function.
- Fix a function so that every check prints `true`.
- Explain what a test gives you that tapping through the app does not, using the pilot's checklist.
