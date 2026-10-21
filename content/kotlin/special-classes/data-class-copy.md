---
id: kotlin/special-classes/data-class-copy
title: How do I change one field without changing the original object?
status: live
difficulty: medium
order: 50
requires: [kotlin/special-classes/data-classes, kotlin/collections/pairs-and-destructuring]
summary: The copy function makes a new data object with one value changed. See what it copies and what it still shares.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [form-photocopier]
playground: false
fruit: false
upnext: false
---

## The question

A screen keeps its current settings in one object. The user turns on dark mode, so the program needs a new settings object where dark mode is on and every other value is the same. It must keep the old settings too, in case the user taps Undo. Must the program copy every value by hand? Or can it say "the same as before, except this one"?

## Mental model

Think of a paper form and a photocopier.

- The form is an object. Its boxes are its properties.
- A box with a plain fact, such as a name or an age, holds the fact itself.
- A box can also hold a folder number, which says "see folder 7 in the filing cabinet". The folder is another object, such as a list of notes.
- The photocopier makes a new sheet with the same answer in every box.
- You write a new answer on the copy. The original on the desk stays as it was.
- The photocopier copies the folder number, not the folder.

**Where the analogy stops:** With a real copier you copy first and write after. In Kotlin, one call does both, and you name the box you want changed. Also, a real copier cannot copy a folder at all. In code, copying the folder is something you do on purpose, with a new folder.

## See it move

```sim
id: form-photocopier
template: compare
title: A paper form, its photocopy and the filing cabinet
intro: One form sits on the desk, with the name Ada, the age 30 and the folder number 7. Folder 7 in the filing cabinet holds one note. The photocopy has not been made yet.
sides:
  - id: desk
    label: Forms on the desk
    boxes:
      - { id: original, label: "Original form: Ada, age 30, folder 7" }
      - { id: copy, label: "Copy: not made yet" }
  - id: cabinet
    label: Filing cabinet
    boxes:
      - { id: f7, label: "Folder 7: notes in it: 1" }
      - { id: f8, label: "Folder 8: not made yet" }
vars: { stage: blank, copyAge: 30, folder: 7, n7: 1, n8: 0 }
actions:
  - id: copy
    label: Photocopy the form
    do:
      - set: { stage: copied, copyAge: 30, folder: 7 }
      - label:
          desk.copy: "Copy: Ada, age {copyAge}, folder {folder}"
      - light: [ desk.copy ]
      - say: The photocopier makes a second form with the same answers, including folder number 7.
  - id: age31
    label: Write 31 in the copy's age box
    cases:
      - when: { var: { stage: copied } }
        do:
          - set: { copyAge: 31 }
          - label:
              desk.copy: "Copy: Ada, age {copyAge}, folder {folder}"
          - light: [ desk.copy ]
          - say: The copy's age box now says 31, and the original still says 30.
    refuse: Make the photocopy first.
  - id: note
    label: Add a note through the copy
    cases:
      - when: { var: { stage: copied, folder: 7 } }
        do:
          - add: { n7: 1 }
          - label:
              cabinet.f7: "Folder 7: notes in it: {n7}"
              desk.original: "Original form: Ada, age 30, folder 7 (notes in folder: {n7})"
              desk.copy: "Copy: Ada, age {copyAge}, folder 7 (notes in folder: {n7})"
          - light: [ cabinet.f7 ]
          - say: A note added through the copy goes into folder 7, so both forms point at a folder with {n7} notes.
      - when: { var: { stage: copied, folder: 8 } }
        do:
          - add: { n8: 1 }
          - label:
              cabinet.f8: "Folder 8: notes in it: {n8}"
              desk.copy: "Copy: Ada, age {copyAge}, folder 8 (notes in folder: {n8})"
          - light: [ cabinet.f8 ]
          - say: The note goes into folder 8. Only the copy points there, so the original's folder 7 is unchanged.
    refuse: Make the photocopy first.
  - id: own-folder
    label: Give the copy a folder of its own
    cases:
      - when: { var: { stage: copied } }
        do:
          - set: { folder: 8 }
          - label:
              desk.copy: "Copy: Ada, age {copyAge}, folder 8 (notes in folder: {n8})"
              cabinet.f8: "Folder 8: notes in it: {n8}"
          - light: [ desk.copy, cabinet.f8 ]
          - say: The copy now carries a new folder number, 8. The original still points at folder 7.
    refuse: Make the photocopy first.
script: [ copy, age31, note, own-folder, note ]
try:
  - text: Press "Photocopy the form" and then "Write 31 in the copy's age box". Read both forms.
    done: [ copy, age31 ]
  - text: Press "Add a note through the copy". Read the folder number on both forms and the notes in the folder.
    done: [ note ]
  - text: Press "Give the copy a folder of its own". Then add a note through the copy again.
    done: [ own-folder, note ]
```

**What you should notice:** Writing a plain fact on the copy leaves the original alone, but a note added to a shared folder shows through both forms.

## Build the logic

### Notice

Ada's form says name Ada, age 30, folder number 7. Folder 7 in the cabinet holds one note. The clerk needs a form for next year that says age 31 and is otherwise the same. Writing over Ada's form would lose the age 30. So the clerk photocopies it and writes 31 in the age box of the copy. Then the clerk adds a note to folder 7, using the copy. Ada's form still says folder 7, and now folder 7 holds two notes.

### Guess

How can the clerk get a form with one box changed and the old form untouched? Here are three ideas.

- A. Write the new answer over the box on the original.
- B. Fill in a new form from memory.
- C. Photocopy the form and write the new answer on the copy.

Idea A loses the old answer. Idea B risks a box being forgotten. Idea C is the answer.

What happens to folder 7 when the form is photocopied? Here are three ideas.

- A. The copy holds the same folder number, so there is still one folder.
- B. The folder is copied too, and the copy points to a new folder, number 8.
- C. Folder 7 is emptied.

Idea B feels natural, because a copy "should" be complete. But the copier only sees the number printed in the box. Idea C has no source. Idea A is the answer.

### Build

1. Never write over a form you may need again.
2. Photocopy it. The copy is a new sheet with the same answer in every box.
3. Write the one new answer on the copy. The original does not change.
4. A box with a plain fact copies the fact. A box with a folder number copies only the number.
5. Two forms with the same folder number share one folder. A note added through either form shows through both.
6. If the copy needs its own folder, make a new folder and put its number in the copy's box when you copy.

### Name it

The Kotlin documentation says you use the **`copy()`** function to copy an object, allowing you to alter some of its properties while keeping the rest unchanged. It is made for you on a data class. The **primary constructor** is the brackets right after the class name, where the boxes are listed. The documentation says the properties declared there are the ones `copy()` uses, so a value declared in the class body is left out.

To change one box, write its name and the new answer inside the brackets, as in `copy(age = 31)`. A **named argument** is a value written with the parameter's name in front of it, so Kotlin knows which box you mean (general knowledge). A box you do not name keeps its old answer.

The documentation says `copy()` creates a **shallow copy**, which means it does not copy components recursively. So references to other objects are shared. Its example has a list in a property. A change made through the original shows through the copy, and the other way round.

This program changes one plain box.

```kotlin
data class Form(val name: String, val age: Int)

fun main() {
    val original = Form("Ada", 30)
    val next = original.copy(age = 31)
    println(original)
    println(next)
    println(original == next)
}
```

It prints `Form(name=Ada, age=30)`, then `Form(name=Ada, age=31)`, then `false`. The original is untouched. The two forms differ in one box, so `==` is `false`.

This program shares a folder.

```kotlin
data class Folder(val owner: String, val notes: MutableList<String>)

fun main() {
    val original = Folder("Ada", mutableListOf("a"))
    val second = original.copy()
    second.notes.add("b")
    println(original)
    println(second)
}
```

It prints `Folder(owner=Ada, notes=[a, b])` twice. The copy holds the same folder number as the original, so one note added through the copy shows through both.

Kotlin refuses to run this one, because the age box is a `val`. That is why `copy()` exists.

```kotlin
// does not compile
data class Form(val name: String, val age: Int)

fun main() {
    val original = Form("Ada", 30)
    original.age = 31
}
```

## Predict the output

```challenge
type: predict
run: false
id: copy-one-plain-box
verified: kotlin-2.4.21
code: |
  data class Form(val name: String, val age: Int)

  fun main() {
      val a = Form("Ada", 30)
      val b = a.copy(age = 31)
      println(a.age)
      println(b.age)
      println(b.name)
  }
options:
  - id: a
    text: "31, 31, Ada"
    why: "You may have thought copy writes on the form you hold. It makes a new sheet and writes on that one."
  - id: b
    text: "30, 31, Ada"
    correct: true
    why: "The photocopy gets age 31 and keeps the name. The original sheet still says 30."
  - id: c
    text: "The program is refused, because age is a val and cannot change"
    why: "You may have thought a val box blocks the change. Nothing is written on the original. The new sheet is filled in with 31 as it is made."
output: |
  30
  31
  Ada
```

```challenge
type: predict
run: false
id: copy-shares-the-folder
verified: kotlin-2.4.21
code: |
  data class Folder(val owner: String, val notes: MutableList<String>)

  fun main() {
      val a = Folder("Ada", mutableListOf("x"))
      val b = a.copy(owner = "Bo")
      b.notes.add("y")
      println(a.notes.size)
      println(b.notes.size)
  }
options:
  - id: a
    text: "1, then 2"
    why: "You may have thought the copier copies the folder too. It copies only the folder number, so both forms lead to the same folder."
  - id: b
    text: "The program is refused, because notes is a val"
    why: "You may have thought a val list cannot grow. The val fixes which folder the box points to, and the folder itself can still take notes."
  - id: c
    text: "2, then 2"
    correct: true
    why: "Changing the owner on the copy does not touch the folder. Both forms hold the same folder number, so the new note shows through both."
output: |
  2
  2
```

```challenge
type: predict
run: false
id: copy-with-its-own-folder
verified: kotlin-2.4.21
code: |
  data class Folder(val owner: String, val notes: MutableList<String>)

  fun main() {
      val a = Folder("Ada", mutableListOf("x"))
      val b = a.copy(notes = mutableListOf("x"))
      b.notes.add("y")
      println(a.notes)
      println(b.notes)
      println(a == b)
  }
options:
  - id: a
    text: "[x], [x, y], false"
    correct: true
    why: "The copy was given a folder of its own, so the note y goes only into that one. The two forms now hold different notes, so they are not equal."
  - id: b
    text: "[x, y], [x, y], true"
    why: "You may have thought the copy still shares the folder. This time its folder number was replaced with a new folder."
  - id: c
    text: "[x], [x, y], true"
    why: "You may have thought the owner decides equality. A data class compares every value in the brackets after the class name, and the notes now differ."
output: |
  [x]
  [x, y]
  false
```

## Tricks to remember

- Photocopy first, write on the copy. The original stays.
- A box with a plain fact is copied. A box with a folder number copies only the number.
- Name the box you change: `copy(age = 31)`.
- On your own machine, make a data class, call `.copy(name = "New Name")`, and print both objects. Then give it a `MutableList`, copy it, add to the list through the copy and print both again.

## Common mistakes

- **Expecting `copy()` to copy the inside objects too.** "Copy" sounds complete. The documentation says it is shallow, and references to other objects are shared. Pass a new list into the copy when it needs its own.
- **Calling `copy()` and ignoring the result.** The word suggests the form on your desk changes. The call gives you a new object and leaves the old one alone, so keep the result in a name.
- **Changing a `var` in place instead of copying.** It is shorter. Every part of the program that holds that object sees the change at once. Use `val` properties and `copy()` so an old object stays as it was.
- **Expecting a value declared in the class body to be copied.** It looks like part of the class. The documentation says only primary constructor properties are used. My own reasoning, confirmed by a test run: the others are not carried over and start from their starting value on the copy.

## Where this shows up in Android

The Android page on producing UI state shows `_uiState.update { currentState -> currentState.copy(...) }`, where the dice state has values such as `firstDieValue` and a `numberOfRolls` that goes up by one. It also shows `it.copy(isTaskSaved = true)`. **UI state** is the set of values a screen shows (general knowledge). This lesson does not explain `update` or the flow around it.

Both lines say "the same state as before, except this value", as the photocopy does. My own reasoning: a screen that holds the old object and the new object can tell they differ, and the old one is still there if you need it. If a list inside the state is changed in place, the folder is shared and the old state changes too, so the screen has nothing to compare.

## Say it in an interview

**What does `copy()` do on a data class?**
The Kotlin documentation says it copies an object and lets you change some properties while keeping the rest the same. You name the properties to change, such as `copy(age = 31)`. The compiler writes it for you from the primary constructor.

**Is `copy()` a deep copy?**
No. The documentation says it creates a shallow copy and does not copy components recursively, so references to other objects are shared. Its example has a mutable list in a property, and an add through the copy shows through the original.

**Why do people pair immutable data classes with `copy()` for UI state?**
My own reasoning: each change makes a new state object and the old one stays as it was, so no part of the program changes under another. The Android state page does this with `copy()` inside `update`. The trade-off is a new object per change, which is small for a few values.

**What goes wrong when a state object holds a mutable list and you use `copy()`?**
The old and new state share one list. My own reasoning: a change to the list shows through both, so the old state changes and the screen has nothing to compare. Make a new list for each new state, or use a read-only list that you replace rather than change.

## Recap

1. `copy()` makes a new data object with the named values changed and the rest the same.
2. The copy is shallow, so a property that holds another object points to the same one.
3. Give the copy a new list or object when it needs its own.

## You can now

- Predict the values of an original and its copy after `copy(age = 31)`.
- Predict what a list shows after a change through a copy that shares it.
- Predict how a copy with its own new list differs from its original.
