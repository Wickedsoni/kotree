---
id: kotlin/extensions-scope-and-delegates/scope-functions
title: What do let, apply, also, run and with actually do?
status: shadow
difficulty: hard
order: 57
requires: [kotlin/extensions-scope-and-delegates/extension-functions, kotlin/lambdas/closures, kotlin/null-safety/safe-call-and-elvis]
summary: Five helpers run a block of code on one object. See the two choices that tell them apart.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/docs/scope-functions.html : the table of let, run, with, apply, also (object reference, return value, whether it is an extension); the function selection list; the apply Person example; the let example with ?.let; the advice to avoid nesting scope functions and to be careful when chaining.
- https://developer.android.com/guide/components/intents-filters : Kotlin example "val sendIntent = Intent().apply { action = ...; putExtra(...); type = ... }".
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/extensions-scope-and-delegates/scope-functions.
- Non-local break and continue in these lambdas: not covered here; the Kotlin scope-functions page had no text on it when read.
- Code blocks and Predict outputs were hand-traced and not compiled in this session (no shell). Run scripts/kt-run.sh before review.
-->

## The question

A tidy-looking piece of code builds a settings object. It uses one short helper to fill it in, another to print a note about it, and a third to compute a number from it. They all look alike: a name, curly brackets and a few lines inside. Are they the same tool with different names? And if you swap one for another, what changes: the lines inside, or what the whole thing hands back?

## Mental model

Think of a kitchen with a pot of soup and five tools that look alike.

- The pot is the object you are working on.
- Each tool is one helper. You put the tool to the pot, do some steps, and take the tool away.
- Inside the steps, you can talk about the pot in one of two ways. Either you stand inside the pot, so you can name the salt directly. Or you stick a label on the pot, and say "label, salt" each time.
- When the steps end, you carry something out of the kitchen. It is either the pot itself, or the dish you made while working on it.
- Two choices, so four combinations. A fifth tool matches one of them, but you hand it the pot instead of putting the tool to the pot.

**Where the analogy stops:** Real kitchen tools look different and do different work. These five do almost the same work. They differ in the two choices only: how the pot is named inside, and what you carry out.

## Build the logic

### Notice

A cook has a pot of soup. Four jobs come up.

1. Set the pot up: add 2 spoons of salt and 1 of pepper, then hand the pot to the next cook.
2. Measure the pot: add up the salt and the pepper, and report one number.
3. Peek at the pot: write a note on the board about it, then hand the pot on.
4. The pot might not be there at all. The cook must only work if there is a pot, and report nothing if there is not.

### Guess

Looking at jobs 1 and 2, what must a tool decide about what it hands back? Here are three ideas.

- A. It always hands back the pot.
- B. It always hands back the last thing made inside.
- C. Each tool has its own rule, and you choose a tool by the rule you need.

Idea A fits job 1 but gives you the pot when job 2 asks for a number. Idea B fits job 2 but loses the pot in job 1. Idea C is the answer.

The second choice is how the pot is named inside. Here are three ideas.

- A. Always stand inside the pot.
- B. Always use a label.
- C. Both are useful, so tools come in both styles.

Standing inside the pot gives short lines but can hide where a name comes from. A label is longer to write but always shows which pot you mean. Idea C is the answer.

### Build

1. Pick how the pot is named inside: stand inside the pot, or use a label.
2. Pick what you carry out: the pot, or the last thing made.
3. Two choices give four tools. Standing inside and carrying the pot out is for setting up. A label and carrying the pot out is for a side note, such as the peek.
4. Standing inside and carrying out the last thing made is for working something out from the pot. A label and carrying out the last thing made is for the same job, and it suits a pot that might be missing.
5. The fifth tool also stands inside and carries out the last thing made. You hand it the pot, instead of putting the tool to the pot.
6. When the pot is missing, the tool is skipped and what you carry out is nothing.
7. Only use one tool at a time on one pot. A tool inside a tool means two pots and two labels, and it is easy to mix them up.

### Name it

A **scope function** runs a block of code on an object. The block is a lambda. The Kotlin documentation lists five of them, and the table below is from the documentation.

| Function | Name for the object inside | What it hands back |
|---|---|---|
| `let` | `it` | The result of the lambda |
| `run` | `this` | The result of the lambda |
| `with` | `this` | The result of the lambda |
| `apply` | `this` | The object itself |
| `also` | `it` | The object itself |

The documentation says `with` is not an extension function: you pass the object in as an argument, as in `with(pot) { ... }`. The others are called on the object, as in `pot.apply { ... }`. The `this` name means you stand inside the pot, so you can write `salt` instead of `it.salt`.

The documentation's guide for choosing says `apply` is for object configuration, `also` is for additional effects, `let` is for executing a lambda on a non-nullable object, and `with` is for grouping calls on an object. It also says the use cases overlap, so teams choose by their own conventions.

```kotlin
class Soup(var salt: Int = 0, var pepper: Int = 0)

fun main() {
    val a = Soup().apply {
        salt = 2
        pepper = 1
    }
    val b = a.let { it.salt + it.pepper }
    println(a.salt)
    println(b)
}
```

It prints `2` and then `3`. The `apply` block stood inside the pot and set `salt` and `pepper`. It handed back the pot, which `a` now holds. The `let` block used the label `it` and handed back the number 3.

The documentation's example for a pot that might be missing is `str?.let { ... }`. The `?.` skips the block when the value is `null`, so the whole line gives `null`.

## Predict the output

```challenge
type: predict
run: false
id: pot-or-dish
verified: not-run
code: |
  fun main() {
      val x = StringBuilder().apply { append("a") }
      val y = StringBuilder().let {
          it.append("a")
          5
      }
      println(x)
      println(y)
  }
options:
  - id: a
    text: "a and a"
    why: "You may have thought every tool hands the pot back. let hands back the last thing made, which is the number 5."
  - id: b
    text: "5 and 5"
    why: "You may have thought apply hands back the last line. apply always hands back the pot, which holds the text a."
  - id: c
    text: "a and 5"
    correct: true
    why: "apply carries out the pot, and the pot holds a. let carries out the dish made inside, the number 5."
output: |
  a
  5
```

```challenge
type: predict
run: false
id: pot-missing
verified: not-run
code: |
  val maybe: String? = null

  fun main() {
      val size = maybe?.let { it.length }
      println(size)
  }
options:
  - id: a
    text: "0"
    why: "You may have expected a count of zero. When the pot is missing the tool is skipped, so no count is made."
  - id: b
    text: "null"
    correct: true
    why: "The pot is missing, so the let block never runs. What comes out is nothing, which prints as null."
  - id: c
    text: "The program stops with an error"
    why: "You may have expected a crash. The ?. skips the block, so no length is asked for."
output: |
  null
```

```challenge
type: predict
run: false
id: two-labels
verified: not-run
code: |
  fun main() {
      val outer = "abc"
      val result = outer.let {
          val inner = "hello"
          inner.let { it.length }
      }
      println(result)
  }
options:
  - id: a
    text: "5"
    correct: true
    why: "The inner tool puts a new label on the inner pot, so it means hello inside the inner block. The length of hello is 5."
  - id: b
    text: "3"
    why: "You may have thought the label still points at the outer pot, abc. The inner block gets its own label."
  - id: c
    text: "The program is refused, because the label is already in use"
    why: "You may have expected two labels with one name to clash. Kotlin lets the inner one take over, which is exactly why a reader can lose track."
output: |
  5
```

## Tricks to remember

- Two questions: how is the pot named inside, and what do I carry out?
- `this` stands inside the pot. `it` is a label on the pot.
- `apply` and `also` carry out the pot. `let`, `run` and `with` carry out the dish.
- One tool at a time. A tool inside a tool is two pots and two labels.

## Common mistakes

- **Treating the five as interchangeable.** They look alike, so any choice feels fine. They hand back different things, and an `apply` where a `let` was needed hands back the pot instead of the number.
- **Using `apply` to compute a value.** `apply` feels like the general one. It carries out the pot, so the value you computed is lost. Use `let` or `run` when you need the dish.
- **Putting one tool inside another.** It feels tidy, because each block is short. The documentation says to avoid nesting scope functions, because it is easy to confuse the current object and the value of `this` or `it`. Name the inner value or write a plain line.
- **Forgetting that `?.let` can hand back `null`.** The block returns a number, so the whole line feels like it returns a number. If the value is missing, nothing runs and `null` comes out.

## Where this shows up in Android

Picture code that starts another screen in an app. The Android intents guide builds the message in one go: `val sendIntent = Intent().apply { action = ...; putExtra(...); type = ... }`. Inside the curly brackets, `action` and `type` are set on the `Intent` itself, with no `sendIntent.` in front. That is the `this` style. And `apply` hands back the `Intent`, which is why `sendIntent` ends up holding it. If a reader mistook `apply` for `let`, they would expect a number or a text back and not the `Intent`.

## Say it in an interview

**What are the five scope functions, and when do you use each?**
They are `let`, `run`, `with`, `apply` and `also`. The Kotlin documentation says `apply` is for configuring an object, `also` is for extra effects like logging, `let` is for running code on a non-null value, and `with` is for grouping calls on one object. It adds that the use cases overlap, so follow your team's habits.

**What is the difference between `this` and `it` in them?**
`run`, `with` and `apply` call the block with `this` as the object, so you can use its members without a name in front. `let` and `also` use `it`. My own reasoning: `it` is clearer when the block also uses members of the outer class, because you can see which object is which.

**What do they hand back?**
`apply` and `also` hand back the object. `let`, `run` and `with` hand back the result of the lambda. So `apply` fits building an object and `let` fits turning a value into another value.

**What goes wrong when scope functions are nested?**
The documentation recommends avoiding nesting and being careful with chaining, because it is easy to get confused about the current object and the value of `this` or `it`. A second `it` hides the first. My own reasoning: a reviewer must read every layer to know which object a name means, so a tidy-looking chain can hide a mistake.

## Recap

1. A scope function runs a block on an object, and the five differ in two choices.
2. One choice is the name inside, `this` or `it`; the other is what comes back.
3. `apply` and `also` hand back the object, and `let`, `run` and `with` hand back the lambda's result.

## You can now

- Predict what `apply` and `let` hand back for the same object.
- Predict the result of `?.let` on a value that is `null`.
- Spot which object `it` means in a nested `let`.
