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

```sim
id: binary-switches
template: lanes
title: "A row of switches and a counter"
intro: "Eight switches sit in the dark row, with an empty spot at the left end. Each chip shows what that switch is worth when it is lit. The counter shows 0."
lanes:
  - { id: "dark", label: "Dark row: switches that are off", kind: "row", slots: 9, index0: false }
  - { id: "lit", label: "Lit row: switches that are on", kind: "row", slots: 9, index0: false }
items:
  - { id: "w1", label: "Worth 1", at: "dark.8" }
  - { id: "w2", label: "Worth 2", at: "dark.7" }
  - { id: "w4", label: "Worth 4", at: "dark.6" }
  - { id: "w8", label: "Worth 8", at: "dark.5" }
  - { id: "w16", label: "Worth 16", at: "dark.4" }
  - { id: "w32", label: "Worth 32", at: "dark.3" }
  - { id: "w64", label: "Worth 64", at: "dark.2" }
  - { id: "w128", label: "Worth 128", at: "dark.1" }
vars: { total: 0, patterns: 256, nine: 0 }
show:
  - { var: "total", label: "Counter" }
  - { var: "patterns", label: "Different patterns" }
actions:
  - id: add-switch
    label: "Add a ninth switch"
    cases:
      - when: { var: { nine: 0 } }
        do:
          - spawn: { id: "w256", label: "Worth 256", at: "dark.0" }
          - set: { nine: 1, patterns: 512 }
          - say: "A ninth switch joins the dark row at the left end. It is worth 256, and the row now has 512 patterns, double the 256 before."
    refuse: "The ninth switch is already on the wall."
drops:
  - item: w1
    to: "lit.8"
    when: { at: { w1: "dark.8" } }
    do:
      - add: { total: 1 }
      - say: "The switch worth 1 is lit. The counter goes up by 1 and shows {total}."
    refuse: "The switch worth 1 is already lit."
  - item: w1
    to: "dark.8"
    when: { at: { w1: "lit.8" } }
    do:
      - add: { total: -1 }
      - say: "The switch worth 1 goes dark. The counter goes down by 1 and shows {total}."
    refuse: "The switch worth 1 is already dark."
  - item: w2
    to: "lit.7"
    when: { at: { w2: "dark.7" } }
    do:
      - add: { total: 2 }
      - say: "The switch worth 2 is lit. The counter goes up by 2 and shows {total}."
    refuse: "The switch worth 2 is already lit."
  - item: w2
    to: "dark.7"
    when: { at: { w2: "lit.7" } }
    do:
      - add: { total: -2 }
      - say: "The switch worth 2 goes dark. The counter goes down by 2 and shows {total}."
    refuse: "The switch worth 2 is already dark."
  - item: w4
    to: "lit.6"
    when: { at: { w4: "dark.6" } }
    do:
      - add: { total: 4 }
      - say: "The switch worth 4 is lit. The counter goes up by 4 and shows {total}."
    refuse: "The switch worth 4 is already lit."
  - item: w4
    to: "dark.6"
    when: { at: { w4: "lit.6" } }
    do:
      - add: { total: -4 }
      - say: "The switch worth 4 goes dark. The counter goes down by 4 and shows {total}."
    refuse: "The switch worth 4 is already dark."
  - item: w8
    to: "lit.5"
    when: { at: { w8: "dark.5" } }
    do:
      - add: { total: 8 }
      - say: "The switch worth 8 is lit. The counter goes up by 8 and shows {total}."
    refuse: "The switch worth 8 is already lit."
  - item: w8
    to: "dark.5"
    when: { at: { w8: "lit.5" } }
    do:
      - add: { total: -8 }
      - say: "The switch worth 8 goes dark. The counter goes down by 8 and shows {total}."
    refuse: "The switch worth 8 is already dark."
  - item: w16
    to: "lit.4"
    when: { at: { w16: "dark.4" } }
    do:
      - add: { total: 16 }
      - say: "The switch worth 16 is lit. The counter goes up by 16 and shows {total}."
    refuse: "The switch worth 16 is already lit."
  - item: w16
    to: "dark.4"
    when: { at: { w16: "lit.4" } }
    do:
      - add: { total: -16 }
      - say: "The switch worth 16 goes dark. The counter goes down by 16 and shows {total}."
    refuse: "The switch worth 16 is already dark."
  - item: w32
    to: "lit.3"
    when: { at: { w32: "dark.3" } }
    do:
      - add: { total: 32 }
      - say: "The switch worth 32 is lit. The counter goes up by 32 and shows {total}."
    refuse: "The switch worth 32 is already lit."
  - item: w32
    to: "dark.3"
    when: { at: { w32: "lit.3" } }
    do:
      - add: { total: -32 }
      - say: "The switch worth 32 goes dark. The counter goes down by 32 and shows {total}."
    refuse: "The switch worth 32 is already dark."
  - item: w64
    to: "lit.2"
    when: { at: { w64: "dark.2" } }
    do:
      - add: { total: 64 }
      - say: "The switch worth 64 is lit. The counter goes up by 64 and shows {total}."
    refuse: "The switch worth 64 is already lit."
  - item: w64
    to: "dark.2"
    when: { at: { w64: "lit.2" } }
    do:
      - add: { total: -64 }
      - say: "The switch worth 64 goes dark. The counter goes down by 64 and shows {total}."
    refuse: "The switch worth 64 is already dark."
  - item: w128
    to: "lit.1"
    when: { at: { w128: "dark.1" } }
    do:
      - add: { total: 128 }
      - say: "The switch worth 128 is lit. The counter goes up by 128 and shows {total}."
    refuse: "The switch worth 128 is already lit."
  - item: w128
    to: "dark.1"
    when: { at: { w128: "lit.1" } }
    do:
      - add: { total: -128 }
      - say: "The switch worth 128 goes dark. The counter goes down by 128 and shows {total}."
    refuse: "The switch worth 128 is already dark."
  - item: w256
    to: "lit.0"
    when: { at: { w256: "dark.0" } }
    do:
      - add: { total: 256 }
      - say: "The switch worth 256 is lit. The counter goes up by 256 and shows {total}."
    refuse: "The switch worth 256 is already lit."
  - item: w256
    to: "dark.0"
    when: { at: { w256: "lit.0" } }
    do:
      - add: { total: -256 }
      - say: "The switch worth 256 goes dark. The counter goes down by 256 and shows {total}."
    refuse: "The switch worth 256 is already dark."
script: ["w1>lit.8", "w4>lit.6", "w8>lit.5", "add-switch", "w256>lit.0"]
try:
  - text: "Drag the switch worth 1 and then the switch worth 4 from the dark row to the lit row. The counter shows 5."
    done: ["w1>lit.8", "w4>lit.6"]
  - text: "Drag the switch worth 8 to the lit row. The counter goes up by exactly 8."
    done: ["w8>lit.5"]
  - text: "Press \"Add a ninth switch\" and drag it to the lit row. Then drag the other switches over too and read the counter. The patterns number doubled."
    done: ["add-switch", "w256>lit.0"]
```

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
