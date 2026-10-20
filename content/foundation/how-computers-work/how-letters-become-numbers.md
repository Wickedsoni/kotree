---
id: foundation/how-computers-work/how-letters-become-numbers
title: How does a computer store a letter like A?
status: shadow
difficulty: easy
order: 3
requires: [foundation/how-computers-work/what-is-a-bit]
summary: A computer does not keep text as shapes. It keeps a number, and a shared list says which letter that number means.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 20 Oct 2026): https://kotlinlang.org/docs/characters.html (every character has a numeric Unicode value; 'A'.code prints 65; arithmetic on characters follows Unicode values, so 'a' + 1 is 'b' and 'a' - 32 is 'A'; so the lowercase letter a is 32 more than A, which is 97). https://developer.android.com/jetpack/compose/text/emoji (without support for the latest emoji fonts, later emoji might show as a small square box called tofu). Unicode is named at overview level only, as the roadmap row asks. The table is a printed table, not a sim: a lookup table is a single rule (roadmap row says why sims is empty). The three Predict items are paper-and-pencil (verified: not-run). housekeeper: remove this comment before the leaf goes live (V12). -->
## The question

You type the letter A in a message and press send. Your friend's phone shows an A. But inside a computer there are only tiny parts that are on or off.

So where is the A? A computer has no pencil and no ink. How does a shape like A get from your phone to your friend's phone?

## Mental model

Think of a locker room.

- Each locker has a number painted on its door.
- A printed list hangs by the door. It says which letter belongs to which locker number. Locker 65 is for A, and the next locker is for B.
- Inside the lockers there are no letters. The only thing the computer carries around is the locker number.
- Everyone in the building uses the same list.

To store an A, the computer writes down 65. To show it, someone looks up 65 on the list and sees that it means A.

**Where the analogy stops:** A real locker holds a thing, and a locker number is only a label. In a computer the number is the only thing that exists. Nothing is stored in a locker. The shape you see on screen is drawn later by a **font**, which is a set of pictures that the screen uses. Also, a real building has one list on one wall. Many computers each have their own copy, so they all have to hold the same list or they will read each other's numbers wrongly.

## Build the logic

### Notice

Take the word CAB. Each of its three letters is a shape, and the computer does not keep text as shapes. It keeps a number instead. If every letter has its own number, the word CAB becomes three numbers in a row.

### Guess

How should the numbers be given out? Here are three ideas.

- A. Each phone picks its own numbers for the letters, in whatever order it likes.
- B. A single list of letters and numbers is agreed first, and every phone uses it.
- C. The computer keeps a tiny picture of each letter and gives each picture a number.

Idea A breaks as soon as two phones talk: your A could be a B on the other phone. Idea C still needs a list of the numbers, and the picture is not what travels; only its number travels. Idea B is what is done.

### Build

1. Write the letters in a fixed order, and give each a whole number. Start with A at 65, so B is 66, C is 67, and so on.
2. Give lowercase letters their own numbers, because "a" and "A" are different shapes. A lowercase a gets 97.
3. Keep this list in every phone and every computer, so they all agree.
4. To store a word, store the numbers of its letters, one after another.
5. To show a word, look each number up on the list, and let the font draw that letter's shape.

The numbers are whole numbers, so each one can be kept as a pattern of switches, as in the last lesson.

### Name it

The agreed list is a **character set**: a list that gives every character its own number. The big shared list in use today is called **Unicode**. The Kotlin documentation says that every character has a numeric Unicode value.

Here are a few rows. The number for A is the one Kotlin prints for `'A'.code`, and B and C follow in order. The small letter a has its own, later number.

| Number | Letter |
|---|---|
| 65 | A |
| 66 | B |
| 67 | C |
| 97 | a |

The word CAB is stored as the numbers 67, 65, 66.

Kotlin can show you a character's number. The `.code` of a character is its number. Kotlin's documentation shows that `'A'.code` is 65, and that `'a' + 1` gives `'b'` because arithmetic on characters follows the Unicode numbers. This lesson does not need you to write any Kotlin. It only shows that the list is real and not a metaphor.

## Predict the output

```challenge
type: predict
run: false
id: number-of-d
verified: not-run
code: |
  The list says: A is 65.
  Letters are numbered in order: A, B, C, D...
  What number does the letter D get?
options:
  - id: a
    text: "66"
    why: "You may have stopped one step early, but 66 is B, and D is two steps further on."
  - id: b
    text: "68"
    correct: true
    why: "A is locker 65, B is 66, C is 67, and D is the next locker, 68."
  - id: c
    text: "4"
    why: "You may have used the place of D in the alphabet, but the list starts at 65 for A, so D is 65 plus 3."
output: "68"
```

```challenge
type: predict
run: false
id: swap-two-rows
verified: not-run
code: |
  A phone's list has two rows swapped:
    65 means B
    66 means A
    67 means C (not swapped)
  A message arrives with the numbers 67, 65, 66.
  Using this swapped list, what word is shown?
options:
  - id: a
    text: "CAB"
    why: "You may have read the numbers with the normal list, but this phone uses the swapped one, and it looks each number up there."
  - id: b
    text: "CBA"
    correct: true
    why: "The phone looks up 67 for C, then 65 which it lists as B, then 66 which it lists as A. The numbers are right but the list is not shared."
  - id: c
    text: "ACB"
    why: "You may have changed the order of the letters, but the numbers keep their order and only the lookup changed."
output: "CBA"
```

```challenge
type: predict
run: false
id: two-lists-two-phones
verified: not-run
code: |
  Phone 1 list: 65 means A, 66 means B.
  Phone 2 list: 65 means B, 66 means A.
  Phone 1 sends the letter A.
  What does Phone 2 show?
options:
  - id: a
    text: "A"
    why: "You may have thought the letter travels, but only the number 65 travels, and Phone 2 looks it up in its own list."
  - id: b
    text: "65"
    why: "You may have expected the raw number, but a phone turns the number back into a letter shape using its list."
  - id: c
    text: "B"
    correct: true
    why: "Phone 1 sends the locker number 65. Phone 2 reads 65 on a different list, where 65 means B."
output: "B"
```

## Tricks to remember

- A computer keeps the locker number, not the letter.
- One list for everyone, or the numbers get read wrongly.
- Capital A and small a are two lockers: 65 and 97.
- The font draws the shape. The number only says which one.

## Common mistakes

- **Thinking the computer stores the shape of each letter.** Text on screen looks like drawings, so it feels like drawings are stored. What is stored is the number. A font turns the number into the shape when it is time to show it.
- **Thinking a capital and a small letter share a number.** In speech we say "the letter A" for both. The computer has one locker for A and a different one for a, so the numbers differ.
- **Thinking a number is a letter's alphabet position.** A being first makes 1 feel right. The list starts A at 65, so use the list.

## Where this shows up in Android

You open a chat and a friend's newest emoji shows up as a small empty square instead of a picture. The Android documentation calls this small square box **tofu**. It says that if an app does not support the latest emoji fonts, later emoji might be displayed that way. It also says that Android 11 (API level 30) and lower cannot update the emoji font.

This is what the locker room explains. In the locker-room picture, the message carries a number. The phone looks the number up and draws its picture. If the phone's font has no picture for that number, you see the box. So the box points to the drawing side, not to the number.

## Recap

1. A computer stores a letter as a whole number, never as a shape.
2. An agreed list says which number is which letter, and the big shared one is Unicode.
3. A font draws the shape when the number is shown.

## You can now

- Explain why a computer stores the number 65 and not the shape of A.
- Predict the number of the next letter when you know the number of A.
- Predict what a word reads back as when two rows of the list are swapped.
- Explain why two phones must share one list.
