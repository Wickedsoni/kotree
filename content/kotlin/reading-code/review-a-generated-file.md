---
id: kotlin/reading-code/review-a-generated-file
title: Would I accept this generated file in review?
status: live
difficulty: hard
order: 65
requires: [kotlin/reading-code/spotting-risky-patterns]
summary: Walk a generated file four times with one question each, write down the worst risks with line numbers, fix one and decide.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [review-walk-through]
playground: true
fruit: true
upnext: false
---

## The question

A tool writes 150 lines of Kotlin for you in ten seconds. It compiles, the names are clear and the one test you tried passes. Would you put your name on it? And if you only have ten minutes, what would you check before you sign?

## Mental model

Think of the final inspection of a house before you sign for it.

- The house is the generated file. Each room is one function.
- The inspector walks the whole house four times. On each walk the inspector asks one question and looks in every room for the answer.
- The report is a short list. It names the three worst problems, and for each one it gives the room, which is the line number.
- Your signature is approval. Move-in day is the day real users run the file.

A house can look fine from the street and still have damp under the sink. The inspector does not trust the paint.

**Where the analogy stops:** A house is inspected once and then lived in. A file keeps changing, so each change needs a new look. A seller may hide a fault, but a generator hides nothing. It writes confident code and does not know the odd cases your users will bring. And in a house you cannot run the odd case in a minute. In code you can, and you should.

## See it move

```sim
id: review-walk-through
template: stepper
title: Four walks through a generated file
intro: "A small generated file of ten lines stands ready for approval. No walk has been made, and the report is empty."
code: true
lines:
  - { id: l1, text: "val history = mutableListOf<Int>()" }
  - { id: l2, text: "fun average(scores: List<Int>): Int {" }
  - { id: l3, text: "    return scores.sum() / scores.size" }
  - { id: l4, text: "}" }
  - { id: l5, text: "fun nameLength(name: String?): Int {" }
  - { id: l6, text: "    return name!!.length" }
  - { id: l7, text: "}" }
  - { id: l8, text: "fun remember(score: Int) {" }
  - { id: l9, text: "    history.add(score)" }
  - { id: l10, text: "}" }
board:
  - { var: found, label: "Risks written down" }
log: { columns: ["Walk", "Question", "Risk and room"] }
vars: { found: 0, p1: 0, p2: 0, p3: 0, p4: 0 }
actions:
  - id: pass-types
    label: "Walk 1: types"
    cases:
      - when: { var: { p1: 0 } }
        do:
          - set: { p1: 1 }
          - add: { found: 1 }
          - light: [l2, l3]
          - row: ["1", "What are the types?", "Line 3: an Int divided by an Int drops the half"]
          - say: "Walk 1 asks about types: in the room at line 3, an Int divided by an Int drops the half, so the average of 7 and 8 comes out as 7."
    refuse: "You already made this walk. Pick another walk, or press Decide."
  - id: pass-runs
    label: "Walk 2: what runs"
    cases:
      - when: { var: { p2: 0 } }
        do:
          - set: { p2: 1 }
          - add: { found: 1 }
          - light: [l3]
          - row: ["2", "What runs, and in what order?", "Line 3: an empty list divides 0 by 0"]
          - say: "Walk 2 asks what runs: in the same room, an empty list gives 0 divided by 0, and that stops the program."
    refuse: "You already made this walk. Pick another walk, or press Decide."
  - id: pass-null
    label: "Walk 3: what can be missing"
    cases:
      - when: { var: { p3: 0 } }
        do:
          - set: { p3: 1 }
          - add: { found: 1 }
          - light: [l5, l6]
          - row: ["3", "What can be missing?", "Line 6: !! promises the name is there"]
          - say: "Walk 3 asks what can be missing: at line 6 the !! promises that the name is there, and a missing name stops the program."
    refuse: "You already made this walk. Pick another walk, or press Decide."
  - id: pass-twice
    label: "Walk 4: what can run twice"
    cases:
      - when: { var: { p4: 0 } }
        do:
          - set: { p4: 1 }
          - add: { found: 1 }
          - light: [l8, l9]
          - row: ["4", "What can run twice?", "Line 9: every call adds an entry and none is removed"]
          - say: "Walk 4 asks what can run twice: at line 9 every call adds one more entry to the history, and nothing removes one, so it only grows."
    refuse: "You already made this walk. Pick another walk, or press Decide."
  - id: decide
    label: "Decide: sign or send back"
    cases:
      - when: { var: { found: 4 } }
        do:
          - light: [l3, l6, l9]
          - row: ["-", "Decision", "Send back: lines 3, 6 and 9"]
          - say: "Do not sign yet: line 3 stops the program on an empty list, line 6 stops it on a missing name, and line 9 grows without limit, so send the file back with these three line numbers and ask for a run that proves each fix."
    refuse: "Finish all four walks first. A decision before the inspection is a guess."
script: [pass-types, pass-runs, pass-null, pass-twice, decide]
try:
  - text: "Press \"Walk 1: types\" and \"Walk 2: what runs\". After each walk, read the row that is added to the report."
    done: [pass-types, pass-runs]
  - text: "Press \"Walk 3: what can be missing\" and \"Walk 4: what can run twice\"."
    done: [pass-null, pass-twice]
  - text: "Press \"Decide: sign or send back\" and read which three line numbers go back with the file."
    done: [decide]
```

**What you should notice:** Each walk lights different rooms and adds one row to the report, and only after all four walks does the decision name three line numbers.

## Build the logic

### Notice

A teammate sends a file of 150 lines. Nothing in it looks wrong. The rooms have clear names and the doors are in neat rows. You have ten minutes before the file goes into the app.

You could read from the top to the bottom, but that only shows you what the file says. It does not show you what each room does when something odd walks in.

### Guess

How should you spend the ten minutes?

- A. Walk the file four times, each time with one question, and write down the three worst problems with their line numbers.
- B. Read once from the top and trust the neat names.
- C. Run the file once with normal data and sign if it works.

Idea A is the inspection. Idea B trusts the paint. Idea C feels sensible, but the rooms that fail are the ones normal data never reaches.

### Build

1. First walk: what are the types? Look at what each name is and what each room hands back.
2. Second walk: what runs, and in what order? Follow the program from where it starts.
3. Third walk: what can be missing? Look for a promise that something is always there.
4. Fourth walk: what can run twice? Look for a room that adds to something that is never cleaned up.
5. After each walk, write the line number and the odd input that hurts it. A doubt with no line number cannot be checked.
6. Rank the problems. Ask for each one: how bad is the damage, and how likely is the odd input? Keep the worst three.
7. Pick one problem and fix it. Then run the odd input and show that the damage is gone.
8. Decide in one of three ways: sign, sign after the fixes, or send it back. Say why in one sentence.

### Name it

A **pass** is one trip through the file with one question in mind. A **risk** is a line that can stop the program or give a wrong answer on an input you have not tried. A **review report** is the short list of risks, each with its line number and the odd input that hurts it, plus your decision.

The four questions are the passes from the reading-order leaf, and the risky lines are the ones from the risky-patterns leaf. This lesson puts them together. Three facts from the Kotlin documentation sit behind the file below. The Kotlin documentation says division of two integers always returns an integer and the compiler discards the fractional part. It says the `!!` operator results in a NullPointerException, called an **NPE** for short, when the value is `null`. And it lists `!!` among the only possible causes of an NPE in Kotlin, so a clean build does not rule it out.

The Predict items use two helpers. **`runCatching`** runs a block of code and hands back either its value or the failure, so the program does not stop. `exceptionOrNull()` gives the failure, or `null` if there was none, and `javaClass.simpleName` is the short name of that failure's type, such as `ArithmeticException`. This lets one program show what each risky line does with an odd input.

Here is the file from the sim. It compiles. It works on normal data.

```kotlin
val history = mutableListOf<Int>()

fun average(scores: List<Int>): Int {
    return scores.sum() / scores.size
}

fun nameLength(name: String?): Int {
    return name!!.length
}

fun remember(score: Int) {
    history.add(score)
}

fun main() {
    println(average(listOf(7, 8)))
    println(nameLength("Asha"))
    remember(9)
    println(history)
}
```

This prints three lines: `7`, `4` and `[9]`. The average of 7 and 8 is really 7.5, so the first line is already the first risk. The other lines hide the rest.

## Predict the output

```challenge
type: predict
id: int-division-drops-half
verified: kotlin-2.4.21
code: |
  fun main() {
      val scores = listOf(7, 8)
      println(scores.sum() / scores.size)
      println(scores.sum().toDouble() / scores.size)
  }
options:
  - id: a
    text: "7 and 7.5"
    correct: true
    why: "The first line divides an Int by an Int, and the half is thrown away. The second line makes one side a Double, so the half is kept. This is the types walk."
  - id: b
    text: "7.5 and 7.5"
    why: "You may have expected the first division to keep the half. Dividing two whole numbers gives a whole number."
  - id: c
    text: "8 and 7.5"
    why: "You may have thought the result rounds up. Kotlin drops the fractional part and does not round it."
output: |
  7
  7.5
```

```challenge
type: predict
id: missing-name-promise
verified: kotlin-2.4.21
code: |
  fun nameLength(name: String?): Int {
      return name!!.length
  }

  fun main() {
      println(runCatching { nameLength("Asha") }.getOrNull())
      println(runCatching { nameLength(null) }.exceptionOrNull()?.javaClass?.simpleName)
      println("done")
  }
options:
  - id: a
    text: "4, then the program stops before done"
    why: "You may have thought a stopped room ends the whole inspection. Here runCatching catches the failure, so the program goes on."
  - id: b
    text: "4, NullPointerException, done"
    correct: true
    why: "The promise holds for Asha, so the length 4 comes back. For a missing name the promise breaks, and runCatching hands back the failure, which prints its name. Then done prints."
  - id: c
    text: "4, null, done"
    why: "You may have expected the !! to hand back null for a missing name. It does not. It stops with an exception, and that exception is the name that prints."
output: |
  4
  NullPointerException
  done
```

```challenge
type: predict
id: average-of-empty-list
verified: kotlin-2.4.21
code: |
  fun average(scores: List<Int>): Int {
      return scores.sum() / scores.size
  }

  fun main() {
      println("start")
      println(runCatching { average(emptyList()) }.exceptionOrNull()?.javaClass?.simpleName)
      println("end")
  }
options:
  - id: a
    text: "start, 0, end"
    why: "You may have thought an empty list averages to 0. The sum is 0, but the size is 0 too, and dividing by 0 is the problem."
  - id: b
    text: "start, and then the program stops"
    why: "You may have forgotten runCatching. It catches the failure, so the program goes on to end."
  - id: c
    text: "start, ArithmeticException, end"
    correct: true
    why: "An empty list has sum 0 and size 0, so the division is 0 divided by 0. Whole-number division by zero stops the room, and runCatching hands back the failure."
output: |
  start
  ArithmeticException
  end
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `7`, `4` and `[9]` on three lines.
3. **Break.** Change `listOf(7, 8)` to `emptyList()` and change nothing else. Run it again. The program stops on the first line with an error, and `4` and `[9]` never print.
4. **Fix.** Add `if (scores.isEmpty()) return 0` as the first line inside `average`, and run it. You should see `0`, `4` and `[9]`. The crash is gone, but `0` now looks like a real average. The next section fixes it in a better way.

## Fix the bug

```challenge
type: fix
id: fix-average-of-nobody
generated: true
verified: kotlin-2.4.21
code: |
  data class Student(val name: String, val scores: List<Int>)

  fun average(scores: List<Int>): Int {
      return scores.sum() / scores.size
  }

  fun report(student: Student): String {
      return "${student.name}: ${average(student.scores)}"
  }

  fun main() {
      val students = listOf(
          Student("Asha", listOf(80, 90)),
          Student("Ben", emptyList()),
          Student("Chi", listOf(70))
      )
      for (student in students) {
          println(report(student))
      }
  }
hints:
  - "Run it and read the output. Which student is the last one printed, and what is special about the next one?"
  - "Dividing by the size of an empty list stops the program. Let average say 'no answer' with a value that is not an Int, and let report decide what to print."
expected: |
  Asha: 85
  Ben: no scores
  Chi: 70
fix: |
  data class Student(val name: String, val scores: List<Int>)

  fun average(scores: List<Int>): Int? {
      if (scores.isEmpty()) return null
      return scores.sum() / scores.size
  }

  fun report(student: Student): String {
      val avg = average(student.scores)
      return if (avg == null) "${student.name}: no scores" else "${student.name}: $avg"
  }

  fun main() {
      val students = listOf(
          Student("Asha", listOf(80, 90)),
          Student("Ben", emptyList()),
          Student("Chi", listOf(70))
      )
      for (student in students) {
          println(report(student))
      }
  }
explanation: "The empty list made the division stop the whole program, so Chi was never reached. Returning null keeps the failure visible and lets report choose what to show, with no fake 0."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

Question 2 is the point of this task: the loop is meant to print three students, and the second one ends it. For question 3, nothing is null in the original, because the empty list is the odd input.

## Tricks to remember

- Four walks, four questions: types, what runs, what can be missing, what can run twice.
- No line number, no risk. Write the room next to every doubt.
- Normal data tells you the house stands. Odd data tells you whether it is safe to live in.
- A fix is not finished until you have run the odd input and watched the damage disappear.
- Task: take or generate a file of 120 to 200 lines that you did not write. Make the four walks. List its three biggest risks, each with its line number. Fix one, show a run that proves it, and say whether you would accept the file and why.

## Common mistakes

- **Thinking a file that ran once on your input is ready.** It ran, so it feels finished. The rooms that fail are the ones your input never reached. Try an empty list, a missing value and a bad text.
- **Writing "this looks risky" with no line number.** It feels like a review. Nobody can check it. Name the line and the odd input.
- **Fixing a crash by returning a default.** The crash stops, so it feels fixed. The failure now looks like a real answer, as in Try it. Let the function say "no answer" and let the caller decide.
- **Reviewing in one pass from the top.** It feels thorough. In one pass you tend to ask only one question and miss the others. Ask a new question on every walk.

Tool or teammate, the person who can check the file is the person who stays in the job.

## Where this shows up in Android

An activity is one screen of an app (general knowledge). The Android documentation says its `onCreate` method receives `savedInstanceState`, a `Bundle`, a small bag of saved values (general knowledge), that holds the activity's previously saved state, and that the value is `null` if the activity has never existed before. A generated screen that writes `savedInstanceState!!` makes a promise that is false the first time the screen opens. That is line 6 of the sim.

The Android documentation says that when an app crashes, Android terminates the app's process and shows a dialog saying the app has stopped. My own reasoning: a user who sees that dialog on first launch is the user your one passing test never was. That is why the report asks for odd inputs and not only normal ones.

## Say it in an interview

**A tool generates 150 lines of Kotlin and the tests pass. What do you do before you approve it?**
I walk it four times with one question each: types, what runs, what can be missing and what can run twice. I write each doubt with its line number and the odd input that hurts it. Passing tests only prove the inputs the author chose, so I try the odd ones myself (my own reasoning).

**How do you choose the three biggest risks?**
I ask two things for each one: how bad is the damage and how likely is the odd input. A line that stops the program for a common input ranks above a slow leak for a rare one. This is my own reasoning, not an official rule.

**How do you show that your fix works?**
I run the odd input before and after. Before, the program stops or gives a wrong answer. After, it behaves, and the normal input still gives the same result. The point is a run, not an opinion.

**Why can code with no compiler warnings still crash?**
The compiler checks that the types fit together, not what a line does with an odd input. The Kotlin documentation lists `!!` among the only possible causes of an NPE, and `String.toInt()` throws a `NumberFormatException` for text that is not a number. Integer division by an empty list's size is the same kind of hidden stop: the Android reference for `ArithmeticException` says an integer "divide by zero" throws it.

## Recap

1. Review a generated file in four walks, one question each.
2. Write every risk with a line number and the odd input that hurts it.
3. Fix one risk, show a run that proves it, and decide.

## You can now

- Predict what a short generated function does with an empty list or a missing value.
- Fix a generated function that stops on an empty list by letting it return no answer.
- Name the four passes in order and the kind of risk each one finds.
- Write a review report of three risks, each with its line number and a decision.
