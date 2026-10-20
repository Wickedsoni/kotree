---
id: kotlin/control-flow/ranges-and-for-loops
title: Where does a Kotlin range start and stop?
status: shadow
difficulty: easy
order: 10
requires: [kotlin/control-flow/if-as-an-expression]
summary: A Kotlin range says where to start, where to stop and how big each step is. Some forms include the stop number and some leave it out.
video: ""
publishedOn:
updatedOn:
sims: [tape-measure-range]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/ranges.html (range definition, .., ..<, downTo, step, last element of a progression); https://kotlinlang.org/docs/control-flow.html (for loops over ranges); https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.ranges/until.html (until excludes the end); https://developer.android.com/develop/ui/compose/components/slider (valueRange = 0f..50f). Read 20 Oct 2026. All outputs hand-traced, not run. Sim differs from the roadmap row, which says to reuse the staircase sim: see docs/sims/needed-ids.md. -->
## The question

A shelf has 5 boxes, numbered 1 to 5, and you want to tick each box once. You write a rule that says "from 1 up to 5". Does box 5 get ticked? And how would you tick every third box, or tick the boxes from the top down?

## Mental model

Think of a tape measure laid out on the floor, with a number printed on every mark.

- The tape measure is the row of numbers you can choose from.
- The start mark is where you begin.
- The stop mark is where you finish.
- A sticker goes on every mark you visit. Putting a sticker on a mark is the work you do for that number.
- The step size is how many marks you move between stickers. It is usually one.

You say where to start, where to stop, and how big each step is. Then you walk along the tape and place stickers.

**Where the analogy stops:** On a real tape the stop mark is something you can see, so you put a sticker on it without thinking. In Kotlin, whether the stop mark gets a sticker depends on the symbol you pick. Also, a real tape can be read either way, but a Kotlin range goes only the way you ask, so counting down needs its own word.

## See it move

<!-- SIM: tape-measure-range -->

**Try this:**
1. Set the start to 1 and the stop to 5, press "Place stickers", then switch to "stop before the stop mark" and place them again.
2. Switch to "count down", set the start to 10 and the stop to 0, and set the step size to 3.
3. Compare the two choices that both stop before the stop mark. They have different names but the same stickers.

**What you should notice:** Whether the stop mark gets a sticker depends on the choice, and the step size can make you miss the stop mark.

## Build the logic

### Notice

The tape has marks 0 to 10. A rule says "stickers from mark 1 to mark 5". You walk to mark 1 and place a sticker. You move one mark at a time. The question is what you do at mark 5.

### Guess

Does mark 5 get a sticker? Here are three ideas.

- A. Yes, always. The stop mark is part of the job.
- B. No, always. "Up to 5" means before 5.
- C. It depends on the exact wording of the rule.

Idea A and idea B both feel natural, because everyday talk uses "up to" both ways. Kotlin has two wordings. One puts a sticker on the stop mark and one does not. Idea C is the answer, so the exact wording matters.

### Build

1. Pick the start mark.
2. Pick the stop mark.
3. Decide whether the stop mark itself gets a sticker.
4. Pick the step size. If you say nothing, you move one mark at a time.
5. To go down the tape, begin at the high mark and ask to count down.
6. Walk the marks in order. At each mark, do the work for that number.
7. A big step size can jump over the stop mark. With marks 1 to 9 and a step size of 3, the stickers go on 1, 4 and 7.

### Name it

A **range** is an ordered set of values with a defined start and end. The Kotlin documentation says it increments by 1 at each step by default, and that `1..4` represents the numbers 1, 2, 3 and 4.

The table lists the rules from Build in Kotlin. The documentation says `..` includes both the start and the end value, and `..<` includes the start value but excludes the end value. The standard library describes `until` as returning a range from this value up to but excluding the end value.

| Rule in words | Kotlin | Stickers |
|---|---|---|
| Start to stop, stop included | `1..5` | 1, 2, 3, 4, 5 |
| Stop before the stop mark | `1..<5` | 1, 2, 3, 4 |
| Same, written with a word | `1 until 5` | 1, 2, 3, 4 |
| Count down | `5 downTo 1` | 5, 4, 3, 2, 1 |
| Bigger steps | `1..9 step 3` | 1, 4, 7 |
| Count down in bigger steps | `10 downTo 0 step 3` | 10, 7, 4, 1 |

The tape is the range, the start and stop marks are the two numbers, and the step size is the number after `step`. Putting a sticker is the work inside the loop. A **`for` loop** repeats its work once for each number in a range. The name after `for` is the **loop variable**. It holds the number of the mark you are standing on.

This program places stickers on marks 1 to 4, then counts down by threes.

```kotlin
fun main() {
    for (i in 1..<5) {
        println(i)
    }
    println("---")
    for (i in 10 downTo 0 step 3) {
        println(i)
    }
}
```

It prints `1`, `2`, `3`, `4`, then `---`, then `10`, `7`, `4`, `1`. The first loop stops before 5. The second starts at 10, moves down by 3 each time, and ends at 1, because the next mark, -2, is below 0.

In the Predict items below, `print(i)` writes the number without starting a new line, and `println()` with nothing inside starts one.

## Predict the output

```challenge
type: predict
run: false
id: down-to-by-three
verified: not-run
code: |
  fun main() {
      for (i in 10 downTo 0 step 3) {
          println(i)
      }
  }
options:
  - id: a
    text: "10, 7, 4, 1, 0"
    why: "You may have expected the walk to end on the stop mark. A step of 3 from 10 lands on 1, and the next mark, -2, is past 0."
  - id: b
    text: "10, 7, 4, 1"
    correct: true
    why: "The walk starts at 10 and moves down 3 marks each time. After 1, the next mark would be -2, so the last sticker goes on 1."
  - id: c
    text: "9, 6, 3, 0"
    why: "You may have started counting one mark after the start. A sticker goes on the start mark itself, which is 10."
output: |
  10
  7
  4
  1
```

```challenge
type: predict
run: false
id: dots-versus-until
verified: not-run
code: |
  fun main() {
      for (i in 1..3) {
          print(i)
      }
      println()
      for (i in 1 until 3) {
          print(i)
      }
      println()
  }
options:
  - id: a
    text: "123 and 123"
    why: "You may have thought the two forms mean the same. The dots give the stop mark a sticker and until does not."
  - id: b
    text: "12 and 12"
    why: "You may have thought both forms leave the stop mark out. Only until does."
  - id: c
    text: "123, then 12"
    correct: true
    why: "The dots place a sticker on every mark from 1 to 3, stop mark included. The word until stops before the stop mark, so 3 is left out."
output: |
  123
  12
```

```challenge
type: predict
run: false
id: one-past-the-last-mark
verified: not-run
code: |
  fun main() {
      // The tape has 3 marks to use, numbered 0, 1 and 2.
      val markCount = 3
      for (i in 0..markCount) {
          println(i)
      }
  }
options:
  - id: a
    text: "0, 1, 2, 3"
    correct: true
    why: "The dots put a sticker on the stop mark too. Mark 3 is one past the last mark you meant to use, which is 2."
  - id: b
    text: "0, 1, 2"
    why: "You may have read the goal instead of the code. Those are the marks you wanted, but the dots include the stop mark."
  - id: c
    text: "1, 2, 3"
    why: "You may have started counting from 1. The tape starts at the start mark, which is 0 here."
output: |
  0
  1
  2
  3
```

## Tricks to remember

- Two dots keep the stop mark. Dots with a less-than sign drop it.
- `until` is the same as `..<`: it stops before the stop mark.
- To go down, say `downTo`. To skip marks, say `step`.
- Count your stickers on paper before you trust a range.

## Common mistakes

- **Thinking `..` and `until` mean the same.** Both read as "from here to there", so they feel like twins. The dots include the stop mark and `until` leaves it out. Write the marks on paper and count them.
- **Looping to the size of a list with the dots.** Marks numbered from 0 feel as if they should end at the count, so `0..3` for three items looks right. The last mark is numbered one less than the count. Stop before the count with `..<`.
- **Writing a range backwards to count down.** `5..1` reads naturally as "from 5 to 1". The documentation says to use `downTo` instead of `..` to go in reverse order.

## Where this shows up in Android

A volume bar on a phone should reach its highest level. **Jetpack Compose** is the modern toolkit for building Android UI. Its slider page sets the range of values a slider can take with `valueRange = 0f..50f`, and describes that parameter as the range of values the slider can take.

The numbers there are decimals, but the two dots mean the same thing: from the start value to the end value, both included. This next part is our own reasoning: if a slider's range stopped one step short of its end value, the thumb could never reach the top of the bar, and nothing would report an error.

## Recap

1. A range has a start, a stop and a step size, which is one by default.
2. Two dots include the stop number, and `..<` or `until` leave it out.
3. A `for` loop runs once per number, and `downTo` and `step` change the direction and the gap.

## You can now

- Predict the numbers printed by a loop that uses downTo and step.
- Predict the difference between a loop over dots and a loop over until.
- Spot a range that goes one number past the last mark you meant to use.
