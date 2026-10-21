---
id: architecture-data/why-architecture/why-code-needs-structure
title: "Why does an app with everything in one file become impossible to change?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: When one function gets data, decides and draws, a small change can break the other jobs. Splitting the jobs keeps each change small.
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/topic/architecture (separation of concerns is "the most important principle"; "It's a common mistake to write all your code in an Activity"; the Android OS "frequently" destroys and recreates activities, for example on screen rotation; mixing the network-fetch and database logic in one place makes testing "much more difficult, if not impossible"). The roadmap row has an F(ai) task, but the stem's Flags line shows only phase=4, so Fix the bug carries generated: true by the row, not by the flag. No sim: the roadmap row says a before-and-after layout is enough. The code is a plain Kotlin model, not real Android. It ran on Kotlin 2.4.20 only, and the outputs were traced by hand for this rewrite. -->

## The question

A small shop app shows a cart total. The owner says: "Take 5 off when the total is over 80, not over 50." You open the cart screen's file to change one number. You find the code that gets the prices, the code that adds them up and the code that writes the text all mixed together. Where do you cut, and what else might you break?

## Mental model

Think of a kitchen where the cook, the waiter and the cashier are the same tired person.

- The cook makes the food.
- The waiter carries it to the table.
- The cashier works out the bill.
- A plate is the answer one job hands to the next.
- The one person who does all three is the screen's file with everything inside.

On a quiet day this works. Then the menu changes, and the cook's changes tangle with the bill. If you want a second person to help, you do not know which job to hand over.

**Where the analogy stops:** In a real kitchen you can see three jobs in three places. In code the jobs are lines of text in the same file, so you only see the mixing by reading. Also, a restaurant must hire a person for each job. In code, splitting the jobs costs a few small functions.

## Build the logic

### Notice

Here is the cart screen in plain words. It does three jobs in a row.

1. It gets the list of prices: 40, 25 and 10.
2. It adds them up to 75, and takes 5 off because 75 is over 50.
3. It shows the line "Total: 70".

The three jobs live in one function. The second job's numbers go straight into the third.

### Guess

The owner changes the discount rule. Which part of the function do you edit?

- A. Only the part that adds up the prices.
- B. The part that gets the prices, because the rule uses them.
- C. You do not know until you have read all of it, because the three jobs sit together.

In a small function you can guess A and be right. Idea C is the honest answer when the function is forty lines long and someone else wrote it. The next steps remove the need to guess.

### Build

1. Give each job its own named place: one for getting the prices, one for working out the total, one for showing it.
2. Let each place do its own job and nothing else.
3. Pass the answer from one place to the next, like plates going from the cook to the waiter to the cashier.
4. When the owner changes the discount, go to the place that works out the total, and no other.
5. When the wording of the line changes, go to the showing place. The rule is never touched.

### Name it

Giving each job its own place has a name: **separation of concerns**. The Android architecture guide calls it the most important principle. It means separating your app into methods, classes, files, packages, modules and layers that have clearly defined responsibilities and boundaries. The way an app's jobs are arranged is its **architecture**.

The same guide says it is a common mistake to write all your code in an **Activity**, the Android class that hosts a screen's user interface. It says the Android system controls the **lifecycle** of an activity, meaning when it is created and destroyed, and frequently destroys and recreates it, for example when the screen is rotated. It also says that mixing the logic of two jobs in one place makes testing much more difficult, if not impossible. Its example is fetching data from the network and saving it in a database.

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

It prints `Total: 70`, the same as the mixed version. The behaviour did not change. Only the places did. To change the discount you edit `calculateTotal`, and the other two functions stay as they are.

## Predict the output

```challenge
type: predict
id: mixed-cart-total
verified: not-run
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
    text: "Total: 5"
    why: "You may have read the 5 as the new total. It is the amount taken off, not the result."
  - id: c
    text: "Total: 70"
    correct: true
    why: "40 plus 25 plus 10 is 75. The cashier's rule takes 5 off because 75 is over 50."
output: |
  Total: 70
```

```challenge
type: predict
id: change-the-rule
verified: not-run
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
    text: "Total: 75"
    correct: true
    why: "The owner changed one number in the cashier's job. 75 is not over 80, so nothing comes off."
  - id: b
    text: "Total: 70"
    why: "You may have read it as the old rule. The line now says 80, and 75 is not over 80."
  - id: c
    text: "Total: 80"
    why: "You may have used the 80 as the total. It is only the limit that starts the discount."
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
verified: not-run
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
  - "Split the jobs: one place to get the text, one to read a field by its name, one to show it."
expected: |
  Name: Ada
  Status: minor
fix: |
  // A model in plain Kotlin, not real Android.
  fun fetchRaw(): String {
      return "age=17;name=Ada"
  }

  fun readField(raw: String, key: String): String {
      for (part in raw.split(";")) {
          if (part.startsWith(key + "=")) return part.substringAfter("=")
      }
      return ""
  }

  fun drawProfile(name: String, age: Int) {
      val label = if (age >= 18) "adult" else "minor"
      println("Name: $name")
      println("Status: $label")
  }

  fun main() {
      val raw = fetchRaw()
      drawProfile(readField(raw, "name"), readField(raw, "age").toInt())
  }
explanation: "The tidy function guessed that the name always comes first, and that guess was buried between getting and drawing. With the jobs apart, the reading place finds a field by its name and the other two do not care."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

The code looks tidy and confident, so question 5 matters. Ask which job made the wrong assumption, and whether it can be fixed without touching the other two. For question 3, ask what the fixed version does when the text has no age field.

## Tricks to remember

- One person, three jobs: change one, risk all.
- Plates go cook, waiter, cashier. Each place hands over an answer and keeps its own job.
- Before you edit, ask: which job is this change about?

## Common mistakes

- **Thinking architecture is extra ceremony for big companies.** A small app feels too small to split. But the first rule change already makes you read the whole file. Splitting costs a few functions now and saves a long hunt later.
- **Thinking architecture means picking an acronym such as MVVM or MVI.** The names get the attention. This lesson names none of them. The idea underneath is clear jobs with clear boundaries.
- **Splitting by file size.** Cutting a long function in half feels tidy. Follow the jobs, getting, deciding and showing, not the line count.
- **Keeping the app's data in the screen's code because it is close at hand.** It feels convenient. The Android guide says the system often destroys and recreates an activity, so data kept there can be lost.

## Where this shows up in Android

Picture a profile screen where the user rotates the phone and the data vanishes, or where a small change to a price rule breaks the layout. Both symptoms come from one place doing several jobs.

The Android architecture guide says the primary role of an activity is to host the app's user interface, and that the system controls its lifecycle and frequently destroys and recreates it on actions like screen rotation. Data that lives in the activity goes with it. The rule change that touched the layout is the tired person from this lesson. When the rule lives in its own place, the screen never has to change.

## Say it in an interview

**Why is it hard to maintain an app that puts the UI, the business rules and the data fetching in one Activity?**
A change to one job can break the other two, and you cannot tell which lines belong to which job without reading it all. The architecture guide calls it a common mistake to write all your code in an activity. It also says mixing the logic of two jobs makes testing much more difficult, if not impossible. My own reasoning: separate plain functions can be tested without a phone.

**What does separation of concerns mean in an Android app?**
It means splitting the app into methods, classes, files, packages, modules and layers that each have clearly defined responsibilities and boundaries. The guide calls it the most important principle. The idea is that each place has one reason to change.

**Why should the app's data not live in the Activity?**
The guide says the Android OS controls the lifecycle of an activity and frequently destroys and recreates it, for example on screen rotation. Data kept in the activity can go with it. Its main job is to host the UI.

**Is it worth adding layers to a tiny two-screen prototype?**
This is my own reasoning, because the guide gives no rule. Extra layers add files to read and pass data through. For a throwaway prototype, one function with clear sections may be enough. As soon as a second person works on it or the rules start to change, the split pays for itself.

## Recap

1. One function that gets data, decides and draws makes every change risky.
2. Separation of concerns gives each job its own place and its own reason to change.
3. Splitting changes where the code lives, not what the app does.

## You can now

- Predict the output of a mixed function after a rule change.
- Say which place a rule change belongs in, using the cook, the waiter and the cashier.
- Fix a generated one-function screen by splitting getting, reading and showing into separate functions.
