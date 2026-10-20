---
id: kotlin/control-flow/when-expression
title: How do I choose between many cases without a ladder of ifs?
status: shadow
difficulty: easy
order: 9
requires: [kotlin/control-flow/if-as-an-expression]
summary: A when checks a value against a list of cases from the top. The first case that matches gives the result.
video: ""
publishedOn:
updatedOn:
sims: [sorting-chutes]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/control-flow.html (when as expression and statement, first matching branch, else, commas, in ranges, exhaustiveness); https://developer.android.com/develop/ui/compose/animation/value-based (when (state) giving a Dp). Read 20 Oct 2026. All outputs hand-traced, not run. Interview section: same control-flow page (when compares in order; statement need not be exhaustive; expression must; guard conditions need a subject and no comma conditions); https://kotlinlang.org/docs/whatsnew22.html (guard conditions stable in Kotlin 2.2.0, per W5-b review). Guard conditions are only mentioned in the interview section, no code. -->
## The question

A ticket machine hands out numbered tickets. Ticket 1 goes to the first lane, ticket 2 to the second lane, tickets 3 and 4 to the third lane, and tickets 5 to 9 to the later lane. Everything else goes to a spare lane. You could write a question for each case, one under the other, but the pile grows fast. Is there a tidier way to say "look at this number and pick one of these answers"?

## Mental model

Think of a sorting office.

- The office is the whole job of picking one answer.
- A letter arrives with a number on the envelope.
- A rail runs past a row of chutes, from the top of the office to the bottom.
- Each chute has a label, such as "1", "3 or 4" or "5 to 9".
- Each chute ends in a tray that holds the answer for that chute.
- At the end of the rail is a bin marked "everything else".

The letter slides along the rail and meets the labels one at a time. It drops into the first chute whose label fits. Then it is gone from the rail.

**Where the analogy stops:** A real letter falls into a chute and cannot be in two places. In code, each label is a separate question asked from the top, so two labels can both fit, but the walk stops at the first one that fits and the second is never asked.

## See it move

<!-- SIM: sorting-chutes -->

**Try this:**
1. Drop letters numbered 1, 4, 7 and 20 down the rail.
2. Drag the chute labelled "7 to 12" above the chute labelled "5 to 9", then drop letter 7 again.
3. Remove the "everything else" bin and drop letter 20.

**What you should notice:** A letter falls into the first chute that fits, the order of the chutes can change which one that is, and a letter with no chute at all has nowhere to go.

## Build the logic

### Notice

The office has these chutes from the top: "1", "2", "3 or 4", "5 to 9", and the bin "everything else".

- Letter 4 passes "1", then "2", then lands in "3 or 4". It never meets the chutes below.
- Letter 20 passes every label and lands in the bin.

Each letter ends in exactly one place.

### Guess

Suppose the office has a chute "5 to 9" and, below it, a chute "7 to 12". Letter 7 arrives, and both labels fit. Where does it go? Here are three ideas.

- A. Into both. A copy of the letter drops into each chute.
- B. Into the first chute that fits, which is "5 to 9".
- C. Into the chute that fits best.

Idea A feels right if you picture every label getting a turn. But one letter cannot split. Idea C needs someone to judge which fit is best, and the rail does not do that. Idea B is the answer.

### Build

1. Take the number on the letter.
2. Walk down the chutes from the top and check each label against the number.
3. The first label that fits sends the letter into that chute. The walk stops there, and the chutes below are never checked.
4. A label may list several numbers or a stretch of numbers, such as "3 or 4" or "5 to 9".
5. The tray at the end of the chute holds the answer. That answer is what the whole office hands back.
6. The bin at the bottom catches every letter that fit nothing above. If the office must hand back an answer for every possible letter, the bin must exist.

### Name it

**`when`** is the Kotlin word for the sorting office. The Kotlin documentation says it compares its argument against each branch in order until one branch condition is satisfied. The value after the word `when`, in round brackets, is the **subject**: the letter. Each line of the form `label -> answer` is a **branch**: a chute. A comma lists several labels for one branch. The word `in` followed by `5..9` checks whether the number is in the stretch from 5 to 9, with both ends included. The word `else` is the bin.

The documentation says you can use `when` as an expression or as a **statement**, which is a line that does a job and gives no value. As an expression it returns a value, and you must cover all possible cases. The value of the first matching branch becomes the value of the whole expression. For a number subject, the `else` branch is how you cover all the cases.

| Sorting office | In Kotlin |
|---|---|
| The office | The `when` |
| The number on the envelope | The subject |
| A chute | A branch |
| A chute label | The label before `->` |
| The tray with the answer | The value after `->` |
| The "everything else" bin | The `else` branch |

This program sorts ticket 4.

```kotlin
fun main() {
    val ticket = 4
    val lane = when (ticket) {
        1 -> "first"
        2 -> "second"
        3, 4 -> "third"
        in 5..9 -> "later"
        else -> "other"
    }
    println(lane)
}
```

It prints `third`. Ticket 4 is not 1 and not 2, and the label `3, 4` fits it. The walk stops there.

Kotlin refuses to run this one, because a `when` that hands back a value has no `else` bin and some numbers fit no chute.

```kotlin
// does not compile
fun main() {
    val ticket = 20
    val lane = when (ticket) {
        1 -> "first"
        2 -> "second"
    }
    println(lane)
}
```

## Predict the output

```challenge
type: predict
run: false
id: first-chute-wins
verified: not-run
code: |
  fun main() {
      val n = 7
      val size = when (n) {
          in 1..9 -> "small"
          in 5..20 -> "medium"
          else -> "large"
      }
      println(size)
  }
options:
  - id: a
    text: "medium"
    why: "You may have expected the chute with the tighter fit to win. The rail checks labels from the top, and 7 fits the first one."
  - id: b
    text: "small, then medium"
    why: "You may have expected a copy of the letter to drop into both chutes. One letter drops into one chute only."
  - id: c
    text: "small"
    correct: true
    why: "The label 1 to 9 is checked first, and 7 fits it. The letter drops there, and the chute below is never checked."
output: |
  small
```

```challenge
type: predict
run: false
id: nothing-fits-so-bin
verified: not-run
code: |
  fun main() {
      val ticket = 12
      val lane = when (ticket) {
          1 -> "first"
          3, 4 -> "third"
          in 5..9 -> "later"
          else -> "other"
      }
      println(lane)
  }
options:
  - id: a
    text: "other"
    correct: true
    why: "The letter 12 passes 1, 3 or 4, and 5 to 9 without fitting any. It slides on to the bin at the bottom."
  - id: b
    text: "later"
    why: "You may have read 5 to 9 as 5 and everything above it. The label stops at 9, so 12 does not fit."
  - id: c
    text: "The program stops, because 12 has no chute"
    why: "You may have expected an unmatched letter to cause trouble. The bin catches every letter that fits nothing above it."
output: |
  other
```

```challenge
type: predict
run: false
id: wide-label-first
verified: not-run
code: |
  fun main() {
      // Goal: tickets 1 to 3 print front. Tickets 4 to 100 print back.
      val ticket = 2
      val seat = when (ticket) {
          in 1..100 -> "back"
          in 1..3 -> "front"
          else -> "none"
      }
      println(seat)
  }
options:
  - id: a
    text: "front"
    why: "You may have read the goal instead of the code. The wide label is checked first, and 2 fits it."
  - id: b
    text: "back"
    correct: true
    why: "The label 1 to 100 sits above 1 to 3, so the letter drops into it first. The narrow chute below can never be reached."
  - id: c
    text: "front, then back"
    why: "You may have expected both labels to fire. After the first fit, the rail stops."
output: |
  back
```

## Tricks to remember

- First fit wins. Then the rail stops.
- Put the narrow label above the wide one.
- Commas mean "or": `3, 4` is one chute for two numbers.
- A `when` that hands back a value needs its `else` bin.

## Common mistakes

- **Expecting every matching branch to run.** It feels natural to think every label that fits gets a turn. In Kotlin the first fit wins and the rest are skipped. Order your branches on purpose.
- **Putting a wide range above a narrow one.** Listing cases from biggest to smallest feels tidy. The narrow chute then never gets a letter. Put the narrow label first.
- **Forgetting the `else` bin.** With a few cases written, it feels complete. A letter you did not think of has nowhere to go, and Kotlin refuses to run. Add `else`.

## Where this shows up in Android

A box on screen can be in one of two states, Collapsed or Expanded, and its border should be thin in one and gone in the other. **Jetpack Compose** is the modern toolkit for building Android UI. Its animation page picks the border width with a `when` on the state. One branch reads `BoxState.Collapsed -> 1.dp` and the other reads `BoxState.Expanded -> 0.dp`, where `dp` is a unit of size on the screen.

The page says to specify the target values for each of the states. That is the sorting office: the state is the letter, and each width is the answer in a tray. There is no `else` there, because `BoxState` is an **enum**, a fixed list of names, and the two branches already cover both of them (general knowledge).

This next part is our own reasoning: with the two widths swapped, an expanded box would show a thin border and a collapsed box none. A `when` that listed only one state would leave the other state with no width, which is why Kotlin asks you to cover every case.

## Say it in an interview

**Does a Kotlin `when` fall through like a Java `switch`?**
No (a Java `switch` can fall through, which is general knowledge). The documentation says `when` compares its argument with each branch in order until one is satisfied. The first match runs and the walk stops, so you do not write `break`. My own reasoning: this removes the bug of forgetting to stop.

**Why does the order of branches matter?**
The first match wins. A wide label above a narrow one hides the narrow one, so the narrow branch can never run. Put the narrow label first.

**When must a `when` have an `else`?**
The documentation says a `when` used as an expression must cover all possible cases, and a `when` used as a statement does not. With a number subject, `else` is how you cover them. The trade-off is that an expression can never be left with no value.

**What are guard conditions?**
A guard condition is an extra `if` after the main condition, in the same branch. The documentation says it works only when the `when` has a subject, and not with several conditions joined by commas. It is stable since Kotlin 2.2. It lets one branch check both the kind of value and one more fact.

## Recap

1. A `when` checks a value against branches from the top, and the first match gives the result.
2. Commas and `in` ranges let one branch cover several numbers, and `else` catches the rest.
3. A `when` that hands back a value must cover every case, usually with `else`.

## You can now

- Predict which branch of a when runs when two labels fit.
- Predict the result when only the else branch fits.
- Spot a wide range placed above a narrow one that makes it unreachable.
