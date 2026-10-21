---
id: foundation/how-computers-work/what-does-a-computer-do
title: What does a computer actually do all day?
status: live
difficulty: easy
order: 1
requires: []
summary: A computer takes something in, works on it step by step, and hands a result out. That happens again and again, all day.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [input-process-output]
playground: false
fruit: false
upnext: false
---

## The question

You tap a heart under a photo. A moment later the heart turns red. Phones feel clever, as if they think about what you want.

Is that what is happening? What is a computer actually doing, over and over, all day long?

## Mental model

Think of a kitchen.

- The ingredients are whatever arrives from outside: a tap, a key press, a photo from the camera, a message.
- The cook is the part that works on the ingredients.
- The recipe is the list of steps the cook follows, one after another, exactly as written.
- The dish is what leaves the kitchen: something you can see or hear.

The cook gets ingredients, follows the recipe, and sends out a dish. Then the next ingredients arrive and the cook starts again. A busy kitchen does this all day.

**Where the analogy stops:** A real cook tastes the soup, guesses, and fixes mistakes because the cook understands food. A computer never understands anything. It follows the recipe word for word, even if the recipe is silly. It also never gets tired, and it works through steps far faster than any cook.

## See it move

```sim
id: input-process-output
template: flow
title: Key presses through the kitchen
intro: Four key presses wait at the input. The cook is at the station. Nothing has been sent yet.
stations:
  - { id: input, label: "Input: ingredients arrive" }
  - { id: cook, label: "Processing: the cook follows the recipe" }
  - { id: output, label: "Output: the dish leaves" }
items:
  - { id: k1, label: "Key H", at: input }
  - { id: k2, label: "Key E", at: input }
  - { id: k3, label: "Key L", at: input }
  - { id: k4, label: "Key L again", at: input }
vars: { cook: "on" }
toggles:
  - { id: cook, label: Cook at the station, var: cook }
actions:
  - id: send
    label: Send the next key press
    cases:
      - when: { at: { k1: input }, var: { cook: "on" } }
        do:
          - move: { k1: output }
          - label: { k1: "Letter H" }
          - mark: { k1: good }
          - light: [ output ]
          - say: "Key H arrives. The cook follows the recipe and turns it into the letter H. The dish leaves: H on the screen."
      - when: { at: { k1: input } }
        do:
          - move: { k1: cook }
          - label: { k1: "H waits: no cook" }
          - mark: { k1: bad }
          - light: [ cook ]
          - say: "Key H arrives, but nobody is at the cook's station. Nothing works on it, so nothing comes out."
      - when: { at: { k2: input }, var: { cook: "on" } }
        do:
          - move: { k2: output }
          - label: { k2: "Letter E" }
          - mark: { k2: good }
          - light: [ output ]
          - say: "Key E takes the same three stops: it arrives, the cook works on it, and the letter E leaves."
      - when: { at: { k2: input } }
        do:
          - move: { k2: cook }
          - label: { k2: "E waits: no cook" }
          - mark: { k2: bad }
          - light: [ cook ]
          - say: "Key E arrives and waits at the empty station too. Still nothing comes out."
      - when: { at: { k3: input }, var: { cook: "on" } }
        do:
          - move: { k3: output }
          - label: { k3: "Letter L" }
          - mark: { k3: good }
          - light: [ output ]
          - say: "Key L makes the same three stops again: it arrives, it is worked on, and the letter L leaves."
      - when: { at: { k3: input } }
        do:
          - move: { k3: cook }
          - label: { k3: "L waits: no cook" }
          - mark: { k3: bad }
          - light: [ cook ]
          - say: "Key L arrives and waits at the empty station. Nothing comes out."
      - when: { at: { k4: input }, var: { cook: "on" } }
        do:
          - move: { k4: output }
          - label: { k4: "Letter L" }
          - mark: { k4: good }
          - light: [ output ]
          - say: "The last key takes the same three stops too. Four keys, four trips, the same shape every time."
      - when: { at: { k4: input } }
        do:
          - move: { k4: cook }
          - label: { k4: "L waits: no cook" }
          - mark: { k4: bad }
          - light: [ cook ]
          - say: "The last key arrives and waits at the empty station. With no cook, no key makes a dish."
    refuse: All four key presses have been sent. Press Reset to send them again.
script: [ send, send, send, "cook=off", send ]
try:
  - text: "Press \"Send the next key press\". Watch the first key arrive, pass the cook and leave as a letter."
    done: [ send ]
  - text: "Press it two more times. Each key makes the same three stops."
    done: [ send, send ]
  - text: "Switch off \"Cook at the station\" and send the last key. The sim replays the earlier keys with the new setting, so every key waits at the empty station and nothing useful comes out."
    done: [ "cook=off", send ]
```

**What you should notice:** Every token takes the same three stops in the same order, and with the middle box gone, nothing useful comes out.

## Build the logic

### Notice

You press the letter H in a notes app. A moment later, an H appears on the screen. Between those two moments, something travelled from your finger to the screen.

Now press E. The same trip happens again. Then L, then L, then O. Five presses, five trips, the same shape every time.

### Guess

Think about the trip from your key press to the letter on the screen. How many different jobs does the computer do along the way?

- A. One job: it understands what you want.
- B. Two jobs: it stores the letter, then it shows the letter.
- C. Three jobs: it takes the press in, works on it, and hands a result out.

Idea A is the guess this page wants to change. The computer does not understand you. Idea B misses the step where the press is turned into the letter. Idea C matches the kitchen: ingredients arrive, the cook works, a dish leaves.

### Build

1. Something arrives from outside the computer, like a key press or a tap.
2. The computer follows steps that were written down in advance. It does one small step, then the next.
3. A result goes out to something you can see or hear, like the screen or the speaker.
4. The computer then waits for the next thing to arrive and does the whole trip again.

Nothing in these four steps needs the computer to understand anything. It needs steps, and it needs to follow them.

### Name it

The three jobs have official names.

- **Input** is anything that goes into the computer from outside, such as a tap, a key press, a camera picture or a message.
- **Processing** is the computer working on the input, by following its **instructions**. Instructions are steps written in advance and followed in order.
- **Output** is anything the computer sends out, such as pixels on a screen, a sound or a buzz.

| Kitchen | Official name | Phone example |
|---|---|---|
| Ingredients arrive | Input | You tap the heart |
| The cook follows the recipe | Processing | The phone works out which photo was tapped and what colour the heart should be |
| The dish leaves | Output | The heart turns red on the screen |

A computer also remembers things between one trip and the next. Remembering has its own lesson, so this page leaves it out.

## Predict the output

```challenge
type: predict
run: false
id: order-of-a-keypress
verified: not-run
code: |
  You press the key H in a notes app.
  Three things happen, listed in a mixed-up order:
    Step 1: The letter H appears on the screen.
    Step 2: The computer works out which letter the key stands for.
    Step 3: The computer receives your key press.
  In what order do they happen?
options:
  - id: a
    text: "Step 1, then Step 2, then Step 3"
    why: "You may have read the list top to bottom, but it is mixed up on purpose, and a dish cannot leave the kitchen before the ingredients arrive."
  - id: b
    text: "Step 3, then Step 2, then Step 1"
    correct: true
    why: "The ingredients arrive first (the press), the cook works on them (works out the letter), and then the dish leaves (the H appears)."
  - id: c
    text: "Step 2, then Step 3, then Step 1"
    why: "You may have thought the computer works before anything arrives, but the cook cannot start until something comes in."
output: "Step 3, then Step 2, then Step 1"
```

```challenge
type: predict
run: false
id: find-the-processing-step
verified: not-run
code: |
  A weather app does three things:
    Step 1: The phone's sensor sends the number 31.
    Step 2: The app turns 31 into the text "31 degrees".
    Step 3: The screen shows "31 degrees".
  Which one is the processing step?
options:
  - id: a
    text: "Step 1"
    why: "You may have picked the first step because it has a number in it, but that step is the ingredients arriving, which is the input."
  - id: b
    text: "Step 2"
    correct: true
    why: "The cook works on the ingredients here. The number comes in as a number and leaves this step as text, because the recipe says so."
  - id: c
    text: "Step 3"
    why: "You may have picked the step you can see, but showing the text is the dish leaving the kitchen, which is the output."
output: "Step 2"
```

```challenge
type: predict
run: false
id: remove-the-cook
verified: not-run
code: |
  Imagine a phone where the processing step is removed.
  The touch screen still sends your tap to the computer.
  The screen is still ready to show things.
  You tap the heart. What happens?
options:
  - id: a
    text: "The heart turns red, because the tap and the screen are enough"
    why: "You may have thought the screen colours itself, but nobody has told it which colour to use, and a kitchen with ingredients but no cook sends out no dish."
  - id: b
    text: "The tap arrives, but nothing changes on the screen"
    correct: true
    why: "The ingredients arrive, but there is no cook to follow the recipe, so no dish leaves."
  - id: c
    text: "The phone turns itself off"
    why: "You may have expected a dramatic failure, but missing work usually shows up as nothing happening, not as a shutdown."
output: "The tap arrives, but nothing changes on the screen"
```

## Tricks to remember

- In, work, out, repeat. Ingredients, cook, dish, next order.
- The recipe decides everything. The cook never has to understand the food.
- If you tap and nothing comes out, ask which of the three stops lost the order.

## Common mistakes

- **Thinking the computer understands you.** Apps answer so quickly and so well that it feels like thinking. But what you see is steps followed in order. Next time, ask: which recipe line did this?
- **Thinking input means only the keyboard.** The keyboard is the example everyone learns first. Taps, the camera, the microphone and an arriving message are all ingredients too.
- **Thinking one trip is the whole job.** After one tap, the work feels finished. The computer waits and does the same trip again for the next tap.

## Where this shows up in Android

Picture the heart under the photo again. You touch the screen, and Android passes the touch to the part of the app you touched. The Android documentation for its classic View system says that when a View such as a Button is touched, the framework calls the `onTouchEvent()` method on that object. That is the input arriving.

The app's own instructions then decide what to do with it, for example which colour the heart should have. That is processing. The heart turning red is the output.

This gives you a first question for a symptom like "I tapped Like and nothing happened". Did the tap never reach the app's instructions, or did the instructions run and send out no result? The three stops tell you where to look.

## Recap

1. A computer takes something in, works on it, and sends a result out.
2. The working is steps written in advance, followed in order, with no understanding.
3. After each trip, it waits and does the same thing again.

## You can now

- Put the stops of an everyday app action in order: input, processing, output.
- Pick out the processing step in a short description of an app.
- Predict what you would see if the processing stop were missing.
