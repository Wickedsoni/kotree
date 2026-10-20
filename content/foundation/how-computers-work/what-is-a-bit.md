---
id: foundation/how-computers-work/what-is-a-bit
title: How can a computer store everything as 0s and 1s?
status: live
difficulty: easy
order: 2
requires: [foundation/how-computers-work/what-does-a-computer-do]
summary: A row of switches, each on or off, can stand for any whole number. That is how a computer holds numbers.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [binary-switches]
playground: false
fruit: false
upnext: false
---

## The question

Your phone shows a red heart, a song, a message from a friend. Inside it, the smallest piece of memory can only be in one of two states, like a light switch that is on or off.

How can a pile of things that are only on or off hold a number? And if it can hold a number, how could it hold a colour or a word?

## Mental model

Think of a long hallway with a row of light switches on the wall.

- The hallway is the computer's memory, the place where it keeps things.
- Each light switch is one piece of memory. It is on or off, and nothing in between.
- A sign above each switch tells you what that switch is worth when it is on.
- The pattern of the whole row, which switches are on and which are off, is the thing being kept.

One switch can only say "yes" or "no". A row of them says much more, because the pattern changes when any one switch changes. You read the pattern with a rule everyone agrees on, and the rule turns the pattern into a number.

**Where the analogy stops:** A real wall switch is big, and a person flips it. Computer switches are tiny, there are billions of them, and the machine flips them itself. A real switch also means nothing on its own, and a computer pattern is the same. The pattern 0100 0001 is a number in one program and a letter in another. What it means depends on the rule the reader uses. The next lesson shows the rule for letters.

## See it move

<!-- SIM: binary-switches -->

**Try this:**
1. Flip switches until the counter shows 5. Then flip them until it shows 200.
2. Look at the sign above each switch. Flip only the switch that is worth 8 and watch the counter change by 8.
3. Press "Add a ninth switch". Turn every switch on and read the counter.

**What you should notice:** A lit switch adds its worth to the counter, and each extra switch doubles the patterns.

## Build the logic

### Notice

Look at a row of three switches. Each can be on or off. Write them as boxes, with a lit box for on and a dark box for off:

| Switch A | Switch B | Switch C |
|---|---|---|
| dark | dark | dark |
| dark | dark | lit |
| dark | lit | dark |
| dark | lit | lit |
| lit | dark | dark |
| lit | dark | lit |
| lit | lit | dark |
| lit | lit | lit |

That list has eight lines, and no pattern appears twice. Each line is a different pattern.

### Guess

You want each pattern to stand for a different number. How should the row be turned into a number? Here are three ideas.

- A. Count how many switches are lit.
- B. Give each switch its own worth, and add up the worths of the lit ones.
- C. Read the lit and dark boxes as digits, like the digits of a phone number.

Idea A fails fast. The patterns "lit, dark" and "dark, lit" both have one lit switch, so they would both mean 1, and you could not tell them apart. Idea C only moves the question: what does a lit box mean as a digit? Idea B keeps every pattern different, and it is what computers do.

### Build

1. Start at the right-hand switch. Give it a worth of 1.
2. Give each switch to its left a worth that is double the one on its right: 1, 2, 4, 8, 16, 32, 64, 128.
3. To read the row, add up the worths of the lit switches. Dark switches add nothing.
4. All switches dark means 0.
5. Count the patterns. One switch has 2 patterns. Each extra switch doubles the count, so three switches give 2 × 2 × 2 = 8 patterns, and eight switches give 256.

Because the first pattern is 0, a row with 256 patterns reaches up to 255, not 256.

### Name it

Each switch is a **bit**, short for "binary digit": it holds one of two values, written 0 and 1. Counting with only two values and doubling worths is called **binary**. A group of 8 bits is a **byte**.

Here is the row for the pattern 0000 0101, with 1 for a lit switch and 0 for a dark one:

| Worth | 128 | 64 | 32 | 16 | 8 | 4 | 2 | 1 |
|---|---|---|---|---|---|---|---|---|
| Switch | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 1 |

The lit switches are worth 4 and 1, so the number is 4 + 1 = 5.

Kotlin lets you write a number in binary by putting `0b` in front of it. The Kotlin documentation shows `0b00001011` as an example, and by the rule above its value is 8 + 2 + 1 = 11. Kotlin's `Byte` type is 8 bits wide. It counts from -128 to 127 so it can hold negative numbers, but this lesson counts from 0.

A picture, a song and a word are not stored as pictures, songs and words. They are stored as numbers, and so as patterns of bits, together with agreed rules for reading them back.

## Predict the output

```challenge
type: predict
run: false
id: read-the-pattern
verified: not-run
code: |
  Eight switches, worths from left to right:
    128  64  32  16  8  4  2  1
  Pattern: 0 0 0 0 0 1 0 1
  What number do these switches make?
options:
  - id: a
    text: "3"
    why: "You may have counted the lit switches, but the pattern is read by adding the worths of the lit ones, not by counting them."
  - id: b
    text: "5"
    correct: true
    why: "The lit switches are worth 4 and 1, and 4 + 1 = 5."
  - id: c
    text: "101"
    why: "You may have read the pattern as the digits 1, 0, 1 in everyday counting, but each switch has a worth, so 101 here means 4 + 0 + 1."
output: "5"
```

```challenge
type: predict
run: false
id: count-the-patterns
verified: not-run
code: |
  A hallway has 3 switches.
  Each one is on or off.
  How many different patterns can the row make?
options:
  - id: a
    text: "3"
    why: "You may have counted the switches, but each switch doubles the number of patterns instead of adding one."
  - id: b
    text: "6"
    why: "You may have multiplied 3 switches by 2 states, but the choices multiply once per switch: 2 × 2 × 2."
  - id: c
    text: "8"
    correct: true
    why: "Every switch you add doubles the patterns, so 2 × 2 × 2 = 8, as in the table of eight lines."
output: "8"
```

```challenge
type: predict
run: false
id: add-a-ninth-switch
verified: not-run
code: |
  A row of 8 switches can make 256 patterns,
  so its biggest number is 255.
  You add a 9th switch, worth 256, at the left end.
  What is the biggest number the row can make now?
options:
  - id: a
    text: "256"
    why: "You may have thought the new switch only adds its own worth, but the other eight switches still count, and all nine lit make 255 + 256."
  - id: b
    text: "511"
    correct: true
    why: "All nine lit add up to 255 + 256 = 511. The count starts at 0, so 512 patterns reach up to 511."
  - id: c
    text: "512"
    why: "You may have given the number of patterns, but the list starts at 0, so the biggest number is one less than the count."
output: "511"
```

## Tricks to remember

- Worths double from the right: 1, 2, 4, 8, 16, 32, 64, 128.
- Add the lit worths, never count the lit switches.
- One more switch in the hallway means twice as many patterns.
- Patterns reach up to one less than the count, because counting starts at 0.

## Common mistakes

- **Thinking the computer keeps real pictures and words inside.** Phones show photos and text so well that it feels like they hold the real thing. Inside there are only switch patterns and rules for reading them. Next time, ask: which rule turns this pattern into what I see?
- **Counting the lit switches.** Counting feels natural, so 0000 0101 looks like 2. Each switch has a worth, so add the worths of the lit ones.
- **Reading a pattern like everyday digits.** The pattern 101 looks like "one hundred and one". Here it means 4 + 0 + 1. Check the worth sign above each switch first.
- **Thinking one more switch adds one more pattern.** It feels like adding one more to a list. In fact the number of patterns doubles.

## Where this shows up in Android

Picture a red heart under a photo. The heart's red is a number the app chose. The Android reference for its `Color` class says the most common way to hold a colour is a colour int. It packs four parts (alpha, which is opacity, then red, green and blue) into one 32-bit whole number. That is 32 switches in a row, read by an agreed rule.

So when a button shows the wrong shade, one of those patterns holds a different number than the developer meant. The wrong colour is not a drawing error. It is the right reading of the wrong number.

## Recap

1. A bit is one switch, on or off, written 0 or 1.
2. Each switch has a worth that doubles to the left, and you add the worths of the lit ones.
3. Every extra switch doubles the patterns, so eight switches give 256, which is 0 to 255.

## You can now

- Predict the number that a row of eight switches makes.
- Work out how many patterns a row of switches can make.
- Explain why the biggest number is one less than the number of patterns.
