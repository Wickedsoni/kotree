---
id: kotlin/types-and-variables/val-vs-var
title: When should a value be allowed to change?
status: live
difficulty: easy
order: 4
requires: [kotlin/first-programs/print-values-in-text]
summary: A val name cannot be pointed at a new value. A var name can. Choose val unless the value must change.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [mug-and-whiteboard-tag]
playground: false
fruit: false
upnext: false
---

## The question

A game keeps two facts. The title, Space Run, never changes. The number of lives starts at 3 and drops each time the player is hit. Both facts need a name in the program. Should both names behave the same way, or should one of them be locked?

## Mental model

Think of name tags and mugs on a shelf.

- A mug is a value. It holds one thing, such as a title or a number.
- A name tag says which mug a name means. "title" means this mug, "lives" means that one.
- A tag printed on a mug can never be pointed at another mug. That is the locked kind.
- A tag on a small whiteboard can be wiped and written again, so the name points at another mug. That is the changeable kind.
- Tea in a mug is what is inside the value. A printed tag does not stop you from pouring more tea in.

You pick the kind of tag when you first put it on the shelf.

**Where the analogy stops:** A printed tag in real life is stopped by glue and ink. In Kotlin the program is refused before it runs, when you try to move a locked name. Also, whether something can change inside a mug depends on the kind of value. A number cannot be topped up like tea. A list you can add to can.

## See it move

<!-- SIM: mug-and-whiteboard-tag -->

**Try this:**
1. Drag the printed tag "price" onto another mug.
2. Drag the whiteboard tag "lives" onto another mug.
3. (Optional reveal) Press "Pour tea" on the mug that has the printed tag.

**What you should notice:** The printed tag snaps back to its own mug, the whiteboard tag moves, and tea can still be poured into the mug with the printed tag.

## Build the logic

### Notice

The game has two mugs on the shelf. One holds the title "Space Run". The other holds the number 3, the lives. After a hit, the lives should be 2. After another hit, 1.

### Guess

Which kind of tag should each fact get? Here are three ideas.

- A. Both get a whiteboard tag, in case anything changes.
- B. The title gets a printed tag and the lives get a whiteboard tag.
- C. Both get a printed tag, so nothing can go wrong.

Idea C fails at the first hit, because the lives tag has to point at a new number. Idea A works, but then nothing stops a step from changing the title by accident. Idea B is the answer.

### Build

1. List the facts the program keeps.
2. For each fact ask: will this name ever need to point at a different mug while the program runs?
3. If the answer is no, use a printed tag. No step can move it by accident.
4. If the answer is yes, use a whiteboard tag.
5. A step that changes a whiteboard tag wipes the old mug off and writes the new mug on.
6. If a step tries to move a printed tag, the program is refused before it runs.

### Name it

A **variable** is a name for a value. Kotlin has two kinds.

A **`val`** variable is read-only. The documentation says you assign it a value only once, and you cannot reassign it a different value after that. A **`var`** variable can be reassigned. To **reassign** is to make a name point at a new value.

The documentation for collections gives advice that fits the printed tag: use `val` as much as possible for safer and more robust code. So start with `val`, and change it to `var` only when the value must change.

```kotlin
fun main() {
    val title = "Space Run"
    var lives = 3
    lives = lives - 1
    println("$title: $lives lives left")
}
```

It prints `Space Run: 2 lives left`. The name `lives` is reassigned once, and `title` never is.

This program is refused, because the line `title = "Moon Walk"` tries to move a printed tag.

```kotlin
// does not compile
fun main() {
    val title = "Space Run"
    title = "Moon Walk"
    println(title)
}
```

Kotlin also has a short form for a change: `lives += 1` means the same as `lives = lives + 1`. The documentation shows `x += 1` on a `var`.

## Predict the output

```challenge
type: predict
run: false
id: which-line-is-refused
verified: not-run
code: |
  fun main() {
      val price = 10
      var count = 1
      count = 2
      price = 12
      println(count)
  }
options:
  - id: a
    text: "Line 5 is refused, so nothing runs"
    correct: true
    why: "price has a printed tag, so line 5 tries to move a locked tag. count has a whiteboard tag, so line 4 is fine."
  - id: b
    text: "Line 4 is refused, so nothing runs"
    why: "You may have thought a name cannot change after it has a first number. That is true only for the printed tag, and count has a whiteboard tag."
  - id: c
    text: "Line 3 is refused, so nothing runs"
    why: "You may have thought two kinds of tag in one program clash. They do not. Each name keeps its own kind."
output: "Line 5 is refused, so nothing runs"
```

```challenge
type: predict
run: false
id: score-changes-twice
verified: not-run
code: |
  fun main() {
      var score = 1
      score = score + 2
      score += 3
      println(score)
  }
options:
  - id: a
    text: "3"
    why: "You may have read += as a plain =. It adds to the number the tag points at, so the second change builds on the first."
  - id: b
    text: "4"
    why: "You may have skipped line 3. The whiteboard tag was rewritten twice, so both changes count."
  - id: c
    text: "6"
    correct: true
    why: "The whiteboard tag starts at 1. Line 3 makes it 3, and line 4 adds 3 to make 6."
output: "6"
```

```challenge
type: predict
run: false
id: both-whiteboards
verified: not-run
code: |
  fun main() {
      var title = "Moon Walk"
      var steps = 10
      steps = steps - 4
      println("$title: $steps steps left")
  }
options:
  - id: a
    text: "The program is refused, because title never changes"
    why: "You may have thought a whiteboard tag must be used. A whiteboard tag only allows a change. Nothing makes you use it."
  - id: b
    text: "Moon Walk: 6 steps left"
    correct: true
    why: "A whiteboard tag on a name that never changes is allowed. The program runs, but nothing now protects title from an accidental change."
  - id: c
    text: "Moon Walk: 10 steps left"
    why: "You may have thought steps keeps its first number. The whiteboard tag was rewritten on line 3, so the new number is 6."
output: "Moon Walk: 6 steps left"
```

## Tricks to remember

- Printed tag: val. Whiteboard tag: var.
- Start with val. Switch to var only when a value must change.
- A printed tag locks the name. It does not lock the tea inside.
- If a name never changes, a var gives you nothing and costs you the protection.

## Common mistakes

- **Thinking `val` means the value can never change inside.** The word read-only sounds like frozen. The Kotlin collections page shows a list you can add to, assigned to a `val`. Adding to it is allowed. Only giving the name a different list is refused. The tag is locked, and the tea can still change. Ask what the name points at.
- **Using `var` everywhere, to be safe.** It feels safe because it always compiles. But then any step can change any name. Use `val` first and let the program tell you when a `var` is needed.
- **Using `val` for a counter.** A counter looks like a fact you set once. It changes every round. If a name must point at a new number, give it a whiteboard tag.

## Where this shows up in Android

**Jetpack Compose** is the toolkit for building Android screens in Kotlin. Its Android documentation keeps the text a person types in a line like `var name by remember { mutableStateOf("") }`. The words `remember` and `mutableStateOf` are Compose's way of keeping a value. Here only the first word, `var`, matters.

The text in a box changes as the person types. In this line, the name itself is given a new value each time, so it needs a whiteboard tag. My own reasoning: in the same example, `onValueChange = { name = it }` hands the new text to `name`, and that kind of reassignment is refused for a `val`. The page also warns what happens when the state is not updated: you try to enter text and nothing happens, because the box updates when its `value` changes.

## Say it in an interview

**What is the difference between `val` and `var`?**
A `val` is assigned a value only once and cannot be reassigned. A `var` can be reassigned. The Kotlin documentation calls them read-only and mutable variables.

**Why prefer `val`?**
The collections page says to use `val` as much as possible for safer and more robust code. My own reasoning: with a `val`, no later line can change which value the name means, so there are fewer places to look when a value is wrong. The trade-off: when a value must change, such as a counter, you need a `var`.

**Is `val` the same as immutable?**
No. A `val` stops the name from being reassigned. The collections page shows a list you can add to, assigned to a `val`. Adding to it is allowed, and only giving the name a different list is refused. So `val` locks the tag, not what is inside.

**In Compose, `var name by remember { mutableStateOf("") }` uses `var`. Could it use `val`?**
In another form, yes. Compose keeps the text a person types in state, which is a value the screen reads. The Compose state page lists three equivalent ways to declare it, and one is `val mutableState = remember { mutableStateOf(default) }`. There the `val` keeps the name fixed and the value inside the state changes. My own reasoning: in the line with `var`, the example later writes `name = it`, which gives `name` the new text, so that form needs a `var`.

## Recap

1. A `val` name can be set once and never pointed at a new value.
2. A `var` name can be reassigned, so use it only when the value must change.
3. A `val` name locks which value it means, not what changes inside that value.

## You can now

- Spot the line Kotlin refuses when a `val` is reassigned.
- Predict the value of a `var` after several changes.
- Explain why a `var` for a name that never changes still runs but gives no protection.
