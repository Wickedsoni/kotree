---
id: architecture-data/why-architecture/three-jobs-in-an-app
title: "Which code shows things, which decides, and which fetches?"
status: live
difficulty: easy
order: 2
requires: [architecture-data/why-architecture/why-code-needs-structure]
summary: "Every line of an app screen does one of three jobs: it shows something, it decides something, or it fetches data."
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: true
fruit: false
upnext: false
---


## The question

A shopping screen shows a price and the words "Too dear" when the price is above 50. The shop changes the rule to 60. You open the screen's code and find the rule, the call that asks the server for the price, and the colours all mixed in one block. Which lines do you change? And how can you tell, before you edit, which line does which job?

## Mental model

Think of a restaurant with three places.

- The dining room is where guests see things. It shows the plate and the menu card. It does not cook or count.
- The head waiter decides things. The waiter reads the order, applies the house rules, and says "yes, that is a full plate" or "no, that needs more".
- The storeroom holds the stock. It hands out ingredients when asked and keeps them safe.

The chain goes one way. The storeroom hands stock to the head waiter. The head waiter hands a decided plate to the dining room. The dining room never walks to the storeroom.

**Where the analogy stops:** In a restaurant, three places are three rooms with three people. In code, one function can do all three jobs, and nothing stops it. Also, the official Android guide does not use these three names. It uses two layers, a UI layer and a data layer, plus an optional third. The head waiter in this lesson is my own teaching split.

## Build the logic

### Notice

Six lines from the shopping screen are listed here, in the order they sit in the file.

1. Ask the server for the price.
2. Say "Too dear" when the price is above 50.
3. Draw the label in a bold red row.
4. Keep the price saved, so the next visit is quick.
5. Say "Fair price" when the price is 50 or below.
6. Turn the server's reply into a number.

### Guess

How should you sort these lines into groups? Here are three ideas.

- A. By where they sit on the screen: top lines, middle lines, bottom lines.
- B. By job: lines that show, lines that decide, lines that fetch.
- C. Do not sort them. For a small app, one block is fine.

Idea A groups by position, and position says nothing about the job. Idea C works until the rule changes from 50 to 60, as in the opening question. Idea B is the answer.

### Build

1. Read one line. Ask what the line is for.
2. If it puts something in front of the user, it belongs in the dining room.
3. If it picks an answer by a rule, it belongs to the head waiter.
4. If it gets stock or keeps stock, it belongs in the storeroom.
5. Some lines look like two jobs glued together. Split them before you sort.
6. Let each answer travel one way: storeroom to head waiter, head waiter to dining room.

With this sort, line 3 is the dining room. Lines 2 and 5 are the head waiter. Lines 1, 4 and 6 are the storeroom. The rule change to 60 touches only lines 2 and 5.

### Name it

The Android guide to app architecture says to design each app with at least two **layers**. A layer is a group of code with one clear job. The **UI layer** displays application data on the screen. The **data layer** contains the business logic of the app and exposes application data. **Business logic** is the set of rules that give the app its value, such as "above 50 is too dear". The guide also describes an optional **domain layer**, used only when needed to handle complexity or reuse.

Here is how my three jobs line up with the guide. This mapping is my own reasoning.

| Restaurant | Job in this lesson | Closest name in the guide |
|---|---|---|
| Dining room | Show | UI layer |
| Head waiter | Decide | Business logic, which the guide puts in the data layer, or in the optional domain layer |
| Storeroom | Fetch | Data layer |

In Kotlin, a job can be a plain **function**. Here is the shopping screen, split into three functions. It is a model in plain Kotlin, not real Android.

```kotlin
// A model in plain Kotlin, not real Android.
fun fetchPrice(): Int = 30

fun decideLabel(price: Int): String =
    if (price > 50) "Too dear" else "Fair price"

fun showRow(label: String) {
    println("Row: " + label)
}

fun main() {
    val price = fetchPrice()
    val label = decideLabel(price)
    showRow(label)
}
```

This prints `Row: Fair price`. `fetchPrice` plays the storeroom, `decideLabel` plays the head waiter, and `showRow` plays the dining room. Each answer travels one way.

## Predict the output

```challenge
type: predict
id: sort-six-lines
verified: kotlin-2.4.21
code: |
  // Six lines from a mixed shopping screen:
  //   1. Ask the server for the price            (Fetch)
  //   2. Say "Too dear" when the price is above 50 (Decide)
  //   3. Draw the label in a bold red row         (Show)
  //   4. Keep the price saved for the next visit  (Fetch)
  //   5. Say "Fair price" when the price is 50 or below (Decide)
  //   6. Turn the server's reply into a number    (Fetch)

  fun askServer(): Int = 1
  fun tooDear(): Int = 2
  fun drawRow(): Int = 3
  fun keepPrice(): Int = 4
  fun fairPrice(): Int = 5
  fun readReply(): Int = 6

  fun main() {
      println("Show: ${drawRow()}. Decide: ${tooDear()} and ${fairPrice()}. Fetch: ${askServer()}, ${keepPrice()} and ${readReply()}.")
  }
options:
  - id: a
    text: "Show: 3 and 5. Decide: 2. Fetch: 1, 4 and 6."
    why: "You may have put line 5 in the dining room because it ends up as words on screen. Choosing the words by a rule is the head waiter's job."
  - id: b
    text: "Show: 2, 3 and 5. Decide: none. Fetch: 1, 4 and 6."
    why: "You may have thought drawing includes the rule. The dining room only puts the answer on screen. The rule is separate, and it is the line you change when the shop moves from 50 to 60."
  - id: c
    text: "Show: 3. Decide: 2 and 5. Fetch: 1, 4 and 6."
    correct: true
    why: "Line 3 only draws. Lines 2 and 5 pick an answer by a rule, so they are the head waiter. Lines 1, 4 and 6 get and keep the stock."
output: |
  Show: 3. Decide: 2 and 5. Fetch: 1, 4 and 6.
```

```challenge
type: predict
id: new-rule-same-price
verified: kotlin-2.4.21
code: |
  fun fetchPrice(): Int = 55

  fun decideLabel(price: Int): String =
      if (price > 60) "Too dear" else "Fair price"

  fun showRow(label: String) {
      println("Row: " + label)
  }

  fun main() {
      showRow(decideLabel(fetchPrice()))
  }
options:
  - id: a
    text: "Row: Fair price"
    correct: true
    why: "The storeroom hands over 55. The head waiter's rule says too dear only above 60, so the answer is Fair price, and the dining room shows it."
  - id: b
    text: "Row: 55"
    why: "You may have thought the dining room shows the stock as it is. It shows the label the head waiter decided."
  - id: c
    text: "Row: Too dear"
    why: "You may have used the old rule of 50. The head waiter now says too dear only above 60, and 55 is not above 60."
output: |
  Row: Fair price
```

## Try it

1. **Write.** Type the three-function program from Name it into the editor.
2. **Run.** You should see one line: `Row: Fair price`.
3. **Break.** In `main`, change `decideLabel(price)` to `decideLabel()` and change nothing else. Run it again. The compiler refuses to run it, because the head waiter was handed no stock to decide about.
4. **Fix.** Put `price` back inside the brackets and run it. You see `Row: Fair price` again. Then change `30` in `fetchPrice` to `80` and run it. The row now says `Too dear`, and you edited only the storeroom.

## Fix the bug

```challenge
type: fix
id: fix-two-rules-two-answers
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android.
  fun fetchPrice(): Int = 50

  fun decideLabel(price: Int): String =
      if (price > 50) "Too dear" else "Fair price"

  fun showListRow(label: String) {
      println("List row: " + label)
  }

  fun showDetailPage() {
      // The detail page gets its own price and applies its own rule.
      val price = fetchPrice()
      val label = if (price >= 50) "Too dear" else "Fair price"
      println("Detail page: " + label)
  }

  fun main() {
      val label = decideLabel(fetchPrice())
      showListRow(label)
      showDetailPage()
  }
hints:
  - "Read the rule in decideLabel and the rule inside showDetailPage. Do they give the same answer when the price is 50?"
  - "Only the head waiter should decide. The detail page should be handed the answer instead of fetching and deciding for itself."
expected: |
  List row: Fair price
  Detail page: Fair price
fix: |
  // A model in plain Kotlin, not real Android.
  fun fetchPrice(): Int = 50

  fun decideLabel(price: Int): String =
      if (price > 50) "Too dear" else "Fair price"

  fun showListRow(label: String) {
      println("List row: " + label)
  }

  fun showDetailPage(label: String) {
      println("Detail page: " + label)
  }

  fun main() {
      val label = decideLabel(fetchPrice())
      showListRow(label)
      showDetailPage(label)
  }
explanation: "The detail page was a dining room that also fetched and decided, so it kept its own copy of the rule, and the copy had a different edge. Handing it the head waiter's answer leaves one rule."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. Question 4 matters here: `showDetailPage` fetches the price itself, so every time it runs, it makes one more trip to the storeroom.

## Tricks to remember

- Dining room shows, head waiter decides, storeroom fetches.
- Ask of every line: what is this line for?
- Answers travel one way: storeroom, head waiter, dining room.
- Two copies of a rule give two answers one day.

## Common mistakes

- **Thinking each job needs its own screen or its own library.** Three jobs sound like three big parts. A job is a role. In the model above, each is one small function.
- **Putting the rule in the drawing code, because that is the only place that uses it.** It saves a step today. When a second screen needs the same rule, a copy of it gets written, and the two copies drift apart, as in the Fix the bug task.
- **Treating the three names as official Android names.** They sound formal. The guide uses UI layer, data layer and an optional domain layer, so learn both sets of names.

## Where this shows up in Android

A shopping app shows "Fair price" in the list and "Too dear" on the detail page for the same item. Nothing crashed. Two screens kept two copies of the rule, and one copy had a different edge, which is the bug in the Fix the bug task.

My own reasoning: when a screen's drawing code asks the server for data itself, it may ask again each time the screen is drawn again. Keeping the fetch in the storeroom means the dining room only shows what it was handed.

## Say it in an interview

**What are the layers in the recommended Android app architecture?**
The guide says to design each app with at least two layers. The UI layer displays application data on the screen. The data layer contains the business logic and exposes application data. A domain layer is optional and sits between them.

**Why separate the code into layers at all?**
The guide calls separation of concerns the most important principle: parts with clearly defined responsibilities and boundaries. My own reasoning: when each job lives in one place, a change such as a new rule touches one place and not every screen.

**Is the domain layer required?**
No. The guide says it is optional and to use it only when needed, for example to handle complexity or favor reuse. For a small app with simple rules, two layers are enough.

**Where would you put a rule such as "above 50 is too dear"?**
The guide says business logic belongs in the data layer, or in the optional domain layer when it is complex or reused. My own reasoning: it should not sit in drawing code, because two screens would end up with two copies that can disagree.

## Recap

1. Every line of a screen shows something, decides something, or fetches something.
2. Answers travel one way: from fetching to deciding to showing.
3. The Android guide names a UI layer, a data layer and an optional domain layer.

## You can now

- Sort lines of a mixed screen into show, decide and fetch.
- Predict what a small three-function program prints after its rule changes.
- Fix a screen that keeps its own copy of a rule, so one function decides.
