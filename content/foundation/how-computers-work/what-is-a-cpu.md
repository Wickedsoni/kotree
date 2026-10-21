---
id: foundation/how-computers-work/what-is-a-cpu
title: How does the CPU follow instructions one at a time?
status: live
difficulty: easy
order: 4
requires: [foundation/how-computers-work/what-does-a-computer-do]
summary: The main chip reads one instruction, does it, then moves to the next. It never skips ahead unless an instruction tells it to.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [recipe-card-pointer]
playground: false
fruit: false
upnext: false
---

## The question

You tap an app and it opens. You scroll and the list moves. Many things seem to happen all at the same moment inside a phone.

Is that true? When the phone follows a long list of steps, how does its main chip get through them? Does it do everything at once, or does it take one step at a time?

## Mental model

Think of a cook in a kitchen, reading a recipe card.

- The cook is the part of the chip that does the work.
- The recipe card is a numbered list of steps, one per line.
- The cook's finger rests on one line. That is the next step to do.
- A bell rings, and each ring lets the cook do one step.
- A line may say "go back to line 1". The cook then moves the finger there.

The cook does this over and over. Look at the line under the finger. Do what it says. Move the finger down to the next line. Wait for the bell.

**Where the analogy stops:** A real phone chip is not one cook. It usually has several **cores**, which are like several cooks, each with a card and a finger of their own. Also, one real step may take more than one ring of the bell, and some chips start the next step before the last one ends. This page follows one cook and one card, which is the picture you need first.

## See it move

```sim
id: recipe-card-pointer
template: stepper
title: A cook, a recipe card and a bell
intro: "The card has three lines, plus a fourth that the cook ignores for now. The finger rests on line 1. The bell has not rung."
lines:
  - { id: l1, text: "1. Crack an egg" }
  - { id: l2, text: "2. Stir" }
  - { id: l3, text: "3. Serve the dish" }
  - { id: l4, text: "4. Go back to line 1 (used only when the switch is on)" }
board:
  - { var: at, label: Finger rests on }
  - { var: bell, label: Bell rings per second }
log: { columns: [Ring, Line done, Finger moves to] }
vars: { at: "line 1", bell: 1, rings: 0, jump: "off" }
toggles: [ { id: jump, label: Use line 4, var: jump } ]
sliders: [ { id: bell, label: Bell rings per second, var: bell, min: 0, max: 3, step: 1 } ]
actions:
  - id: ring
    label: Ring the bell
    cases:
      - when: { var: { bell: 0 } }
        do:
          - row: [ "no ring", "none", "stays on {at}" ]
          - say: "The bell is set to zero, so it never rings. The cook does nothing and the finger stays on {at}."
      - when: { var: { at: "line 1" } }
        do:
          - add: { rings: 1 }
          - set: { at: "line 2" }
          - light: [ l1 ]
          - row: [ "{rings}", "line 1", "line 2" ]
          - say: "The bell rings. The cook cracks an egg, and the finger moves down to line 2."
      - when: { var: { at: "line 2" } }
        do:
          - add: { rings: 1 }
          - set: { at: "line 3" }
          - light: [ l2 ]
          - row: [ "{rings}", "line 2", "line 3" ]
          - say: "The bell rings. The cook stirs, and the finger moves down to line 3."
      - when: { var: { at: "line 3", jump: "off" } }
        do:
          - add: { rings: 1 }
          - set: { at: "off the card" }
          - light: [ l3 ]
          - row: [ "{rings}", "line 3", "off the card" ]
          - say: "The bell rings. The cook serves the dish. Line 4 is not in use, so the card is finished."
      - when: { var: { at: "line 3" } }
        do:
          - add: { rings: 1 }
          - set: { at: "line 4" }
          - light: [ l3 ]
          - row: [ "{rings}", "line 3", "line 4" ]
          - say: "The bell rings. The cook serves the dish, and the finger moves down to line 4."
      - when: { var: { at: "line 4" } }
        do:
          - add: { rings: 1 }
          - set: { at: "line 1" }
          - light: [ l4 ]
          - row: [ "{rings}", "line 4", "line 1" ]
          - say: "The bell rings. Line 4 says go back to line 1, so the finger jumps up. Only a go-back line moves it up."
      - when: { var: { at: "off the card" } }
        do:
          - say: "The card is finished, so there is no line left to do. Press Reset to start again."
script: [ ring, ring, ring, "jump=on", ring, ring, ring ]
try:
  - text: "Press Step three times. Each press is one ring of the bell, and the finger does one line per ring."
    done: [ ring, ring, ring ]
  - text: "Switch on \"Use line 4\", then press Step. Line 4 says go back to line 1, so the finger jumps up."
    done: [ "jump=on", ring ]
  - text: "Move the bell slider to 0, then press Step. With no ring, nothing is done and the finger stays put."
    done: [ "bell=0", ring ]
  - text: "(Optional) Move the bell slider to 3 and press Step. The same line is done, only more rings come each second."
    done: [ "bell=3", ring ]
    optional: true
```

**What you should notice:** The finger moves one line per ring, only a "go back" line sends it up, and with no ring it stays put.

## Build the logic

### Notice

A recipe card has four lines.

1. Crack an egg.
2. Stir.
3. Go back to line 1.
4. Serve the dish.

The cook starts with the finger on line 1. A bell rings. The cook cracks an egg. The finger moves to line 2. The bell rings again. The cook stirs.

### Guess

The bell rings a third time, and the cook does line 3, which says "go back to line 1". Where does the finger go, and what does the ring after that do?

- A. The cook jumps ahead to line 4 and serves the dish.
- B. The finger goes to line 1, and the next ring cracks another egg.
- C. The cook does lines 1 to 4 all together.

Idea A treats "go back" as if it said "go forward". Idea C is the guess this page wants to change: one cook has one finger, and a finger can rest on one line at a time. Idea B follows the card exactly, so it is the right one.

### Build

1. Put the finger on the first line.
2. Wait for the bell.
3. Read the line under the finger. Work out what it asks.
4. Do what it asks.
5. Unless the line said to go somewhere else, move the finger to the next line down. If the line said to go somewhere else, put the finger there instead.
6. Go back to step 2.

Nothing here is done in parallel. One line, then the next, in the order the finger moves.

### Name it

The cook is the **CPU**, short for central processing unit: the part of the computer that follows instructions. An **instruction** is one step written in advance, such as "add these two numbers" or "go to line 1".

The loop in the Build steps has three named parts.

| Kitchen | Official name | What happens |
|---|---|---|
| Read the line under the finger | **Fetch** | The CPU gets the next instruction from memory. |
| Work out what it asks | **Decode** | The CPU works out what the instruction means. |
| Do what it asks | **Execute** | The CPU carries the instruction out. |

The finger is the **program counter**: it keeps the place of the next instruction. After each instruction it moves to the next one, unless an instruction is a **jump**, which sets it somewhere else. "Go back to line 1" is a jump.

The bell is the **clock**. It sets the pace at which the CPU does its steps. A faster clock means more steps in each second. A clock that never rings means no step is ever done.

## Predict the output

```challenge
type: predict
run: false
id: after-line-three
verified: not-run
code: |
  Recipe card:
    1. Crack an egg
    2. Stir
    3. Go back to line 1
    4. Serve the dish
  The finger is on line 3 and the bell rings, so the cook does line 3.
  Which line does the cook do at the ring after that?
options:
  - id: a
    text: "Line 4"
    why: "You may have read the card as always going down, but a go-back line moves the finger up instead."
  - id: b
    text: "Line 1"
    correct: true
    why: "Line 3 sets the finger back on line 1. The cook reads the card exactly as written, so line 1 comes next."
  - id: c
    text: "Line 2"
    why: "You may have gone back one line, but line 3 says to go to line 1, not to the line before it."
output: "Line 1"
```

```challenge
type: predict
run: false
id: lines-done-after-two-rings
verified: not-run
code: |
  Recipe card:
    1. Add salt
    2. Stir
    3. Serve the dish
  The finger starts on line 1.
  The bell rings twice. Which lines have been done?
options:
  - id: a
    text: "Only line 1"
    why: "You may have counted one ring as one line and lost track of the second ring, but each ring lets the cook do one line."
  - id: b
    text: "Lines 1 and 2"
    correct: true
    why: "One line per ring, in order. The first ring does line 1 and the second ring does line 2, so the finger now rests on line 3."
  - id: c
    text: "Lines 1, 2 and 3"
    why: "You may have expected the cook to do the whole card at once, but one cook does one line at a time."
output: "Lines 1 and 2"
```

```challenge
type: predict
run: false
id: bell-set-to-zero
verified: not-run
code: |
  Recipe card:
    1. Add salt
    2. Stir
    3. Serve the dish
  The bell is set to ring zero times per second.
  The finger is on line 1. What happens?
options:
  - id: a
    text: "The cook finishes the whole card immediately"
    why: "You may have thought a zero bell means no waiting, but the bell is what lets the cook do a step at all."
  - id: b
    text: "Nothing is done, and the finger stays on line 1"
    correct: true
    why: "The cook does one line per ring. With no ring, the cook never does line 1, and the finger never moves."
  - id: c
    text: "The cook skips to line 3"
    why: "You may have expected a skip, but the cook never moves the finger unless a line says so or the bell rings."
output: "Nothing is done, and the finger stays on line 1"
```

## Tricks to remember

- Look, do, move on. Fetch, decode, execute, then the finger moves.
- The finger rests on one line at a time.
- A go-back line is a jump. It moves the finger, it does not skip it.
- No bell, no steps.

## Common mistakes

- **Thinking the CPU does everything at once.** Apps feel as if everything happens together. One cook with one finger does one step at a time. The steps are so fast that it looks simultaneous. A phone chip also has several cores, so a few things do run side by side, but each core still follows its own card one step at a time.
- **Thinking the CPU can skip a line by itself.** It feels like a smart reader would skip the dull parts. The CPU only moves the finger down a line or to the place an instruction names.
- **Thinking a faster clock changes what the program does.** A faster bell finishes the same card sooner. The steps and their order stay the same.

## Where this shows up in Android

You tap a button in an app, and the app freezes for several seconds. Then Android shows a message that the app is not responding. The Android documentation calls this an **ANR**, short for "application not responding". It says the system shows one when an app cannot answer user input on its **main thread**. The page gives 5 seconds as the limit for an input event such as a screen tap, and says blocking work on that thread causes it.

The main thread is the part of an app that answers your taps. In our picture it is one cook with one card, even on a chip that has several cooks. If one line on the card is a very long job, the next tap has to wait until the cook reaches it. That wait is what you see as a freeze. The Android documentation says to move long-running work off the main thread, so the cook who answers taps stays free.

## Recap

1. The CPU reads one instruction, does it, and moves on to the next.
2. A program counter marks the next instruction, and a jump can change it.
3. The clock sets the pace, and one long step makes everything behind it wait.

## You can now

- Predict which instruction runs next, including after a jump line.
- Explain why a CPU following one card does one step at a time.
- Predict what happens to a program when the clock never ticks.
