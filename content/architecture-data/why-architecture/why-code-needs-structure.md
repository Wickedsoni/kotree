---
id: architecture-data/why-architecture/why-code-needs-structure
title: "Why does an app with everything in one file become impossible to change?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: When one function fetches, decides and draws, every change risks breaking the other two jobs. Splitting the jobs fixes that.
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. Android claims were checked on 21 Oct 2026 against https://developer.android.com/topic/architecture (separation of concerns as the most important principle; "a common mistake to write all your code in an Activity"; activities are destroyed and recreated by the OS so they should not hold data; testing gets harder when logic is mixed). No sim: the roadmap row says a before-and-after layout is enough. Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. The Fix the bug block is marked generated: true; the stem flag ai-review in the roadmap was not checked by this draft. Model code is plain Kotlin and labelled "not real Android". -->
## The question

A small shop app shows a cart total. One day the owner says: "Give a discount when the total is over 60, not over 50." You open the screen file to change one number.

You find the code that gets the prices, the code that adds them up and the code that puts the text on the screen, all mixed together. Where do you cut? And what else might you break by touching that line?

## Mental model

Think of a kitchen where the cook, the waiter and the cashier are the same tired person.

- The cook makes the food.
- The waiter carries it to the table.
- The cashier works out the bill.
- The one person who does all three is the screen file with everything inside.

On a quiet day this works. Then the menu changes, and the cook's changes tangle with the bill. If the one person is off sick, nothing happens at all. If you want a second person to help, you do not know which job to hand over.

**Where the analogy stops:** In a real kitchen you can see three jobs happening in three places. In code, the three jobs are invisible, because they are lines of text in the same file. You can only see the mixing by reading. Also, a restaurant has to hire staff for each job. In code, splitting the jobs costs only a few small functions.

## Build the logic

### Notice

Here is the cart screen in plain words. It does three jobs in a row.

1. It gets the list of prices: 40, 25 and 10.
2. It adds them up to 75, and takes 5 off if the total is over 50.
3. It writes "Total: 70" on the screen.

The three jobs live in one function, one after another, and the second job's numbers are used directly by the third.

### Guess

The owner changes the discount rule. Which part of the function do you have to edit?

- A. Only the part that adds up the prices.
- B. The part that gets the prices, because the rule uses them.
- C. You do not know, because the three jobs are all in one place and you have to read all of it.

In a small function you can guess A and be right. Idea C is the honest answer when the function is forty lines long and written by someone else. The next steps remove the need to guess.

### Build

1. Give each job its own named place: one for getting the prices, one for working out the total, one for showing it.
2. Let each place do its own job and nothing else.
3. Pass the answer from one place to the next, like plates going from the cook to the waiter.
4. When the owner changes the discount, you go to the place that works out the total, and no other.
5. If the screen's text changes, you go to the showing place. The rule is never touched.

### Name it

Giving each job its own place has a name: **separation of concerns**. The Android architecture guide calls it the most important principle, and describes it as separating your app into methods, classes, files, packages, modules and layers that have clearly defined responsibilities and boundaries. The way an app's jobs are arranged is its **architecture**.

The same guide says it is a common mistake to write all your code in an `Activity`, because the Android system controls its lifecycle and often destroys and recreates it, for example when the screen is rotated. It also says mixing the logic together makes testing much harder.

Here is the cart again, split in three. This is a model in plain Kotlin, not real Android.

```kotlin
// A model in plain Kotlin, not real Android.
fun fetchPrices(): List<Int> {
    return listOf(40, 25, 10)
}

fun calculateTotal(prices: List<Int>): Int {
    var total = 0
    for (p in prices) total += p
    if (total > 50) total -= 5
    return total
}

fun drawTotal(total: Int) {
    println("Total: $total")
}

fun main() {
    drawTotal(calculateTotal(fetchPrices()))
}
```

It prints `Total: 70`, the same as the mixed version. The behaviour did not change. Only the places did. To change the discount you edit `calculateTotal`, and the other two functions are not touched.

## Predict the output

```challenge
type: predict
id: mixed-cart-total
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun showCartScreen() {
      val prices = listOf(40, 25, 10)
      var total = 0
      for (p in prices) total += p
      if (total > 50) total -= 5
      println("Total: $total")
  }

  fun main() {
      showCartScreen()
  }
options:
  - id: a
    text: "Total: 75"
    why: "You may have forgotten the discount line. 75 is over 50, so 5 comes off."
  - id: b
    text: "Total: 70"
    correct: true
    why: "40 plus 25 plus 10 is 75. The rule works out the bill and takes 5 off because 75 is over 50."
  - id: c
    text: "Total: 5"
    why: "You may have read the 5 as the new total. It is the amount taken off, not the result."
output: |
  Total: 70
```

```challenge
type: predict
id: change-the-rule
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun showCartScreen() {
      val prices = listOf(40, 25, 10)
      var total = 0
      for (p in prices) total += p
      if (total > 80) total -= 5
      println("Total: $total")
  }

  fun main() {
      showCartScreen()
  }
options:
  - id: a
    text: "Total: 70"
    why: "You may have read it as the old rule. The line now says 80, and 75 is not over 80, so nothing comes off."
  - id: b
    text: "Total: 75"
    correct: true
    why: "The owner changed one number in the bill step. 75 is no longer over 80, so the cashier takes nothing off."
  - id: c
    text: "Total: 80"
    why: "You may have used the 80 as the total. It is only the limit that triggers the discount."
output: |
  Total: 75
```

## Try it

1. **Write.** Type the three-function cart from Name it into the editor.
2. **Run.** You should see one line: `Total: 70`.
3. **Break.** In `calculateTotal`, change the 50 to 80 and change nothing else. Run it again.
4. **Fix.** You see `Total: 75`, because the discount no longer applies. You edited one line in one function, and the other two never changed. Put the 50 back and run it. The total is 70 again.

## Fix the bug

```challenge
type: fix
id: fix-all-in-one-profile
generated: true
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  fun showProfileScreen() {
      // Pretend the server sent this text.
      val raw = "age=17;name=Ada"
      val parts = raw.split(";")
      val name = parts[0].substringAfter("=")
      val age = parts[1].substringAfter("=").toInt()
      val label = if (age >= 18) "adult" else "minor"
      println("Name: $name")
      println("Status: $label")
  }

  fun main() {
      showProfileScreen()
  }
hints:
  - "Which line stops the program? Check what parts[1] holds when the text starts with age."
  - "Split the jobs: one place to get the text, one to read it by its names, one to show it."
expected: |
  Name: Ada
  Status: minor
fix: |
  // A model in plain Kotlin, not real Android.
  fun fetchRaw(): String {
      return "age=17;name=Ada"
  }

  fun parse(raw: String): Map<String, String> {
      return raw.split(";").associate { it.substringBefore("=") to it.substringAfter("=") }
  }

  fun drawProfile(fields: Map<String, String>) {
      val age = fields.getValue("age").toInt()
      val label = if (age >= 18) "adult" else "minor"
      println("Name: ${fields.getValue("name")}")
      println("Status: $label")
  }

  fun main() {
      drawProfile(parse(fetchRaw()))
  }
explanation: "The one tidy function assumed the name always comes first, and that guess was buried between fetching and drawing. With the jobs apart, the reading step reads by key, and the other two do not care."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

The code looks tidy and confident, so question 5 matters. A reviewer should ask which job made the wrong assumption, and whether that job can be fixed without touching the other two.

## Tricks to remember

- One person, three jobs: change one, risk all.
- Plates go cook, waiter, cashier. Each place hands over an answer and keeps its own job.
- Before you edit, ask: which job is this change about?
- A function that fetches, decides and draws has three jobs and three reasons to break.

## Common mistakes

- **Thinking architecture is extra ceremony for big companies, and a small app does not need it.** A small app feels too small to split. But the first rule change already makes you read the whole file. Splitting costs a few functions now and saves a long hunt later.
- **Splitting by file size.** Cutting a long function in half feels tidy. The cut should follow the jobs: getting, deciding and showing, not line counts.
- **Putting the data in the screen code because it is close at hand.** It feels convenient. The Android guide warns that the screen component can be destroyed and recreated, so data kept there can be lost.
- **Splitting too early into many tiny pieces.** Many small names feel professional. Start with the three jobs and split further only when one place gets two jobs again.

## Where this shows up in Android

Picture a profile screen where the user rotates the phone and the data vanishes, or where a small change to a price rule breaks the layout. Both symptoms come from one place doing several jobs. The Android architecture guide says the screen component's lifecycle is controlled by the system, which often destroys and recreates it, so it should not hold your app's data, and it should only host the user interface.

The rule change that touched the layout is the tired person from this lesson. When the rule lives in its own place, the screen never has to change.

## Recap

1. One function that fetches, decides and draws makes every change risky.
2. Separation of concerns gives each job its own place and its own reason to change.
3. Splitting changes where the code lives, not what the app does.

## You can now

- Explain why mixing fetching, deciding and drawing makes a small change risky, using the one tired person.
- Predict the output of a mixed function after a rule change, and say which place the change belongs in.
- Fix a generated one-function screen by splitting getting, reading and showing into separate functions.
