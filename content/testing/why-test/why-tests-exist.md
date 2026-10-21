---
id: testing/why-test/why-tests-exist
title: What does a test give me that tapping through the app does not?
status: live
difficulty: easy
order: 1
requires: []
summary: A test is a small program that checks your code the same way every time, so you need not remember to look.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [tests-lamps]
playground: true
fruit: false
upnext: false
---

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

```sim
id: tests-lamps
template: lanes
title: "Twenty feature lamps, tapped by hand or checked by a list"
intro: "Twenty lamps stand for twenty features of an app. All of them are green and no line of code has changed yet."
lanes:
  - { id: "row1", label: "Features 1 to 10", kind: "row", slots: 10, index0: false }
  - { id: "row2", label: "Features 11 to 20", kind: "row", slots: 10, index0: false }
items:
  - { id: "l1", label: "Lamp 1", at: "row1.0" }
  - { id: "l2", label: "Lamp 2", at: "row1.1" }
  - { id: "l3", label: "Lamp 3", at: "row1.2" }
  - { id: "l4", label: "Lamp 4", at: "row1.3" }
  - { id: "l5", label: "Lamp 5", at: "row1.4" }
  - { id: "l6", label: "Lamp 6", at: "row1.5" }
  - { id: "l7", label: "Lamp 7", at: "row1.6" }
  - { id: "l8", label: "Lamp 8", at: "row1.7" }
  - { id: "l9", label: "Lamp 9", at: "row1.8" }
  - { id: "l10", label: "Lamp 10", at: "row1.9" }
  - { id: "l11", label: "Lamp 11", at: "row2.0" }
  - { id: "l12", label: "Lamp 12", at: "row2.1" }
  - { id: "l13", label: "Lamp 13", at: "row2.2" }
  - { id: "l14", label: "Lamp 14", at: "row2.3" }
  - { id: "l15", label: "Lamp 15", at: "row2.4" }
  - { id: "l16", label: "Lamp 16", at: "row2.5" }
  - { id: "l17", label: "Lamp 17", at: "row2.6" }
  - { id: "l18", label: "Lamp 18", at: "row2.7" }
  - { id: "l19", label: "Lamp 19", at: "row2.8" }
  - { id: "l20", label: "Lamp 20", at: "row2.9" }
vars: { changed: 0, tapped: 0, minutes: 0, seconds: 0 }
show:
  - { var: "minutes", label: "Minutes tapping by hand" }
  - { var: "seconds", label: "Seconds with the checklist" }
actions:
  - id: change
    label: "Change one line of the code"
    cases:
      - when: { var: { changed: 0 } }
        do:
          - set: { changed: 1 }
          - say: "You change one line of the code. A hidden break now sits behind one lamp, but no lamp shows it yet."
    refuse: "You already changed a line. Press Reset to start again."
  - id: tap
    label: "Tap through five lamps by hand"
    cases:
      - when: { var: { changed: 1, tapped: 0 } }
        do:
          - mark: { l1: "picked", l2: "picked", l3: "picked", l4: "picked", l5: "picked" }
          - set: { tapped: 5 }
          - add: { minutes: 5 }
          - say: "You tap features 1 to 5 by hand. That is {minutes} minutes and every lamp you tried looks fine."
      - when: { var: { changed: 1, tapped: 5 } }
        do:
          - mark: { l6: "picked", l7: "picked", l8: "picked", l9: "picked", l10: "picked" }
          - set: { tapped: 10 }
          - add: { minutes: 5 }
          - say: "You tap features 6 to 10 by hand. {minutes} minutes so far and all fine."
      - when: { var: { changed: 1, tapped: 10 } }
        do:
          - mark: { l11: "picked", l12: "picked", l13: "picked", l14: "picked", l15: "picked" }
          - mark: { l13: "bad" }
          - set: { tapped: 15 }
          - add: { minutes: 5 }
          - say: "You tap features 11 to 15 and lamp 13 is red. Finding it took {minutes} minutes by hand."
      - when: { var: { changed: 1, tapped: 15 } }
        do:
          - say: "You already found the red lamp by hand."
    refuse: "Change a line of the code first, so there is something to find."
  - id: checklist
    label: "Run the checklist"
    cases:
      - when: { var: { changed: 1 } }
        do:
          - mark: { l1: "good", l2: "good", l3: "good", l4: "good", l5: "good", l6: "good", l7: "good", l8: "good", l9: "good", l10: "good", l11: "good", l12: "good", l13: "good", l14: "good", l15: "good", l16: "good", l17: "good", l18: "good", l19: "good", l20: "good" }
          - mark: { l13: "bad" }
          - set: { seconds: 1 }
          - say: "The checklist runs all twenty checks in about a second. Lamp 13 is red."
      - do:
          - mark: { l1: "good", l2: "good", l3: "good", l4: "good", l5: "good", l6: "good", l7: "good", l8: "good", l9: "good", l10: "good", l11: "good", l12: "good", l13: "good", l14: "good", l15: "good", l16: "good", l17: "good", l18: "good", l19: "good", l20: "good" }
          - say: "The checklist runs all twenty checks. Every lamp is green, because nothing has changed."
script: ["change", "tap", "tap", "tap", "checklist"]
try:
  - text: "Look at the twenty lamps. Each lamp is one feature of the app. Press \"Change one line of the code\"."
    done: ["change"]
  - text: "Press \"Tap through five lamps by hand\" until you find the lamp that turned red. Count the minutes."
    done: ["tap", "tap", "tap"]
  - text: "Now press \"Run the checklist\". Compare the seconds with the minutes."
    done: ["checklist"]
```

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
verified: kotlin-2.4.21
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
verified: kotlin-2.4.21
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
verified: kotlin-2.4.21
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
It runs the same checks the same way every time, so a change that breaks an old feature shows up fast. The Android documentation says automated testing is faster and more repeatable. My own reasoning: tapping scales poorly, because every new feature adds more screens to check by hand.

**Does a passing test suite prove the app has no bugs?**
No. A test only checks the cases someone wrote down. In this lesson, two checks passed on broken code. So I also add a check for a case that would fail, and I work out the expected answer by hand before I write it.

**How do tests help when you change old code?**
My own reasoning: the tests are a checklist that still works after the change. If a check turns red, you know which behaviour broke, and you can fix it before a user finds it. Without them, you rely on remembering every place the code is used.

**What goes wrong if a team relies only on manual testing?**
The Android documentation says manual testing scales poorly and that regressions can slip past you. A change in one place can break a screen nobody opens again before release. My own reasoning: the trade-off is that writing tests costs time up front, but running them again costs almost nothing.

## Recap

1. A test runs a piece of code and compares the answer with one you wrote in advance.
2. Running the list after every change finds an old feature that broke.
3. A passing list only covers the cases it checks.

## You can now

- Predict which checks print `true` and which print `false` after a change to a function.
- Fix a function so that every check prints `true`.
- Explain what a test gives you that tapping through the app does not, using the pilot's checklist.
