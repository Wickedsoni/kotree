---
id: kotlin/control-flow/if-as-an-expression
title: How can an if hand back a value?
status: shadow
difficulty: easy
order: 8
requires: [kotlin/types-and-variables/val-vs-var]
summary: In Kotlin an if can give back a value you keep in a name. Both of its paths must give one.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/control-flow.html (if as an expression, else required, last expression of a block, else if); https://developer.android.com/develop/ui/compose/animation/value-based (animateFloatAsState with an if value). Read 20 Oct 2026. All outputs hand-traced, not run. No sim: roadmap row says a single rule, and Foundation's fork sim covers the flow. -->
## The question

A weather screen has one message box. When it rains, the box must hold "Take an umbrella". When it does not, the box must hold "Enjoy the sun". You already know how to make a program choose between two paths. Could the choice itself hand you the sentence, so you can drop it straight into the box?

## Mental model

Think of a vending machine with one slot.

- The machine is the choice.
- The question on the front panel is "Is it cold?".
- The machine holds two drinks: tea on the left and coffee on the right.
- The slot is where exactly one drink drops out.
- Your mug is where you keep the drink. It has your name on it.

On a cold day the machine answers yes, and tea drops into the slot. On a warm day it answers no, and coffee drops. Either way you take the drink and put it in your mug.

**Where the analogy stops:** A real machine can run out of coffee and give you nothing. Kotlin will not let a machine like this be switched on with one drink missing. Also, a real machine only drops a drink, but a Kotlin side can do other jobs first, and the last thing on that side is what drops.

## Build the logic

### Notice

The machine's panel asks "Is it cold?".

- On a cold day the machine gives tea. You put the tea in the mug called drink.
- On a warm day the machine gives coffee. You put the coffee in the same mug.

You never see both drinks. Exactly one drops, and the mug called drink always ends up with something.

### Guess

The shop stocked tea but forgot the coffee. Today is warm. What should happen? Here are three ideas.

- A. The machine gives nothing, and you carry on with an empty mug.
- B. The shop is not allowed to switch the machine on until both drinks are stocked.
- C. The machine gives tea anyway.

Idea A is how a real machine behaves, which is why it feels right. But then the mug called drink might be empty, and the next step would not know what to do with it. Idea C changes your order without telling you. Idea B is how Kotlin works.

### Build

1. Read the question on the front panel.
2. If the answer is yes, take the last item from the tea side. If the answer is no, take the last item from the coffee side.
3. Whatever drops out is the value of the whole machine. Put it in a mug, or use it straight away.
4. Both sides must be stocked, or one answer would leave you with nothing.
5. A side may do other jobs first, such as ringing a bell. Only its last item drops into the slot.

### Name it

An **expression** is a piece of code that works out to a value. The Kotlin documentation says you can write `if` as an expression, which lets you assign its returned value directly to a variable. In this form, an `else` branch is required.

The yes-or-no question inside the round brackets is the **condition**. Each side of the machine is a **branch**. The word `else` starts the second branch, and `val` makes a name that keeps its value. The documentation says each branch can be a block, where the value of the last expression becomes the result. It also says the `if` expression serves the same purpose as the ternary operator, a short question-mark form found in other languages.

| Vending machine | In Kotlin |
|---|---|
| The machine | The `if` expression |
| Question on the panel | The condition |
| Tea side and coffee side | The `if` branch and the `else` branch |
| The slot | The result of the whole `if` |
| Your mug | The name made with `val` |

In the program below, the first `if` has two plain sides. The second has a block on each side.

```kotlin
fun main() {
    val cold = true
    val drink = if (cold) "tea" else "coffee"
    println(drink)

    val minutes = 45
    val message = if (minutes > 30) {
        println("Long wait")
        "Please sit down"
    } else {
        "Come right in"
    }
    println(message)
}
```

It prints `tea`, then `Long wait`, then `Please sit down`. In the second `if`, the `println` is a side job. The last item on the chosen side, the text "Please sit down", is what drops into `message`.

Kotlin refuses to run this one, because the machine has only one side stocked.

```kotlin
// does not compile
fun main() {
    val cold = true
    val drink = if (cold) "tea"
    println(drink)
}
```

## Predict the output

```challenge
type: predict
run: false
id: label-pos-or-neg
verified: not-run
code: |
  fun main() {
      val x = -4
      val label = if (x > 0) "pos" else "neg"
      println(label)
  }
options:
  - id: a
    text: "pos"
    why: "You may have read the panel as a yes. The question is whether x is greater than 0, and -4 is not."
  - id: b
    text: "neg"
    correct: true
    why: "The answer to the panel is no, so the coffee side drops. The text neg goes into the mug called label."
  - id: c
    text: "-4"
    why: "You may have expected the machine to hand back what you gave it. It hands back the drink on the chosen side."
output: |
  neg
```

```challenge
type: predict
run: false
id: busy-queue-block
verified: not-run
code: |
  fun main() {
      val queue = 12
      val note = if (queue > 10) {
          println("Busy")
          "Wait"
      } else {
          "Go"
      }
      println(note)
  }
options:
  - id: a
    text: "Busy"
    why: "You may have stopped at the side job. The last item on the chosen side drops into the mug, and the last println then shows it."
  - id: b
    text: "Wait"
    why: "You may have skipped the side job. The bell still rings before the drink drops, so Busy prints first."
  - id: c
    text: "Busy, then Wait"
    correct: true
    why: "The answer is yes, so the left side runs. It rings the bell first, which prints Busy, and the last item, Wait, drops into the mug."
output: |
  Busy
  Wait
```

```challenge
type: predict
run: false
id: three-drink-machine
verified: not-run
code: |
  fun main() {
      val temp = 20
      val drink = if (temp > 25) "juice" else if (temp > 15) "tea" else "soup"
      println(drink)
  }
options:
  - id: a
    text: "tea"
    correct: true
    why: "The first panel asks for above 25, and the answer is no. The second panel asks for above 15, and the answer is yes, so tea drops."
  - id: b
    text: "juice"
    why: "You may have expected the first drink to win. It wins only when its question is answered yes, and 20 is not above 25."
  - id: c
    text: "soup"
    why: "You may have expected the last drink to win. It drops only when every question above it is answered no."
output: |
  tea
```

## Tricks to remember

- One slot, one drink.
- Both sides stocked: an `if` used as a value needs its `else`.
- The last item on a side is the one that drops.
- To get a value out of an `if`, put the `if` after the `=`.

## Common mistakes

- **Leaving out the `else` when the `if` hands back a value.** A one-sided `if` was fine when it only ran steps, so it feels fine here. When the `if` hands back a value, the machine needs both drinks stocked. Add the `else`.
- **Expecting the side job to be the value.** A `println` on the chosen side feels like the point of that side. Only the last item drops. Put the value you want last.
- **Using the machine without a mug.** You write the `if`, but nothing keeps the result. The drink drops and is gone. Put `val name =` in front when you want to keep it.

## Where this shows up in Android

A part of a screen should look faded when it is switched off. **Jetpack Compose** is the modern toolkit for building Android UI. Its animation page shows a line that picks the alpha, which is how see-through something is. The line reads `if (enabled) 1f else 0.5f`.

The page says the alpha is an animation value between the provided values, `1f` or `0.5f` in this case. That is the machine from this lesson. The condition is `enabled`, and the two sides are the two see-through levels. This next part is our own reasoning: if the `else` side were missing, the screen would have no level to use when the part is switched off.

## Recap

1. An `if` can hand back a value, which you keep in a name with `val`.
2. When an `if` hands back a value, it needs an `else` branch.
3. If a branch is a block, the last expression in the block is the value.

## You can now

- Predict the value a short if-else expression gives for a given number.
- Predict the lines printed when a branch holds a side job and a value.
- Predict which drink an else-if chain picks.
