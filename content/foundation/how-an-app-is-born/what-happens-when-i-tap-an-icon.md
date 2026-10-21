---
id: foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon
title: What happens between tapping an icon and seeing the app?
status: live
difficulty: medium
order: 33
requires: [foundation/how-an-app-is-born/how-an-app-gets-installed, foundation/how-computers-work/what-is-an-os]
summary: The phone starts a running copy of the app if none exists, the app sets itself up, and its first screen is drawn.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [hotel-bell-startup]
playground: false
fruit: false
upnext: false
---

## The question

You tap an app icon. A moment later the app's first screen appears. What did the phone do in that moment? Was the app already running and waiting for your tap, or did something start from nothing?

## Mental model

Think of ringing the bell at a hotel front desk.

- You are the guest. The tap is the bell.
- The manager at the desk is the Android system.
- A room is a private place for the app to run in. It stands for a running copy of the app.
- A blank welcome card in the doorway is what the guest sees while the room is being made ready.
- The staff in the room do their jobs in a fixed order. One waiter handles everything the guest sees and touches.
- The lounge is the first screen of the app. The lights coming on are the first draw.

When you ring, the manager checks whether the app already has a room open. If it does, you go straight in. If it does not, the manager gets a room ready first.

**Where the analogy stops:** A hotel room is built by people. A phone's room is software, made far faster. A hotel keeps a room until the guest leaves, but Android may close an app's room when other apps need the resources more. Also, the hotel is my picture. The Android pages describe the steps, not the bell.

## See it move

```sim
id: hotel-bell-startup
template: stepper
title: A hotel front desk and the bell
intro: "The guest stands at the front desk. No room is open for the app yet, and the bell has not been rung."
lines:
  - { id: l1, text: "You ring the bell (the tap)" }
  - { id: l2, text: "The manager checks for an open room" }
  - { id: l3, text: "A room is made ready (the app's process starts)" }
  - { id: l4, text: "A blank welcome card goes up in the doorway" }
  - { id: l5, text: "Job 1: set up the desk book (create the app object)" }
  - { id: l6, text: "Job 2: call the waiter (launch the main thread)" }
  - { id: l7, text: "Job 3: open the lounge (create the main activity)" }
  - { id: l8, text: "Job 4: set out the furniture (initialize the UI)" }
  - { id: l9, text: "Job 5: switch on the lights (draw the UI)" }
  - { id: l10, text: "The card comes down and the lounge shows" }
board:
  - { var: room, label: The room }
  - { var: card, label: The welcome card }
log: { columns: [Hotel, Android] }
vars: { pc: 0, room: "none", card: "none", open: "off" }
toggles: [ { id: open, label: Room already open, var: open } ]
actions:
  - id: ring
    label: Ring the bell
    cases:
      - when: { var: { pc: 0 } }
        do:
          - set: { pc: 1 }
          - light: [ l1 ]
          - row: [ "The guest rings the bell", "You tap the icon" ]
          - say: "The guest rings the bell at the front desk. This is the tap."
    refuse: "The bell has already rung. Press Do the next job."
  - id: next
    label: Do the next job
    cases:
      - when: { var: { pc: 1 } }
        do:
          - set: { pc: 2 }
          - light: [ l2 ]
          - row: [ "The manager checks for an open room", "The system checks for a running process" ]
          - say: "The manager checks whether the app already has a room open."
      - when: { var: { pc: 2, open: "on" } }
        do:
          - set: { pc: 99, room: "open", card: "none" }
          - light: [ l10 ]
          - row: [ "The manager takes you to the open room, and fewer jobs are needed", "The app's process already exists" ]
          - say: "A room is already open, so the manager takes you straight to it. Fewer jobs are needed, and the lounge shows sooner."
      - when: { var: { pc: 2 } }
        do:
          - set: { pc: 3, room: "being made ready" }
          - light: [ l3 ]
          - row: [ "No room is open, so a room is made ready", "The system starts the app's process" ]
          - say: "No room is open, so the manager starts getting a new one ready."
      - when: { var: { pc: 3 } }
        do:
          - set: { pc: 4, card: "up" }
          - light: [ l4 ]
          - row: [ "A blank welcome card goes up in the doorway", "The blank starting window shows" ]
          - say: "The manager hangs a blank welcome card in the doorway."
      - when: { var: { pc: 4 } }
        do:
          - set: { pc: 5 }
          - light: [ l5 ]
          - row: [ "Set up the desk book", "Create the app object" ]
          - say: "In the room, the first job is to set up the desk book."
      - when: { var: { pc: 5 } }
        do:
          - set: { pc: 6 }
          - light: [ l6 ]
          - row: [ "Call the waiter", "Launch the main thread" ]
          - say: "The second job is to call the waiter, who handles everything the guest sees and touches."
      - when: { var: { pc: 6 } }
        do:
          - set: { pc: 7 }
          - light: [ l7 ]
          - row: [ "Open the lounge", "Create the main activity" ]
          - say: "The third job is to open the lounge, the first screen of the app."
      - when: { var: { pc: 7 } }
        do:
          - set: { pc: 8 }
          - light: [ l8 ]
          - row: [ "Set out the furniture", "Initialize the UI" ]
          - say: "The fourth job is to set out the furniture in the lounge."
      - when: { var: { pc: 8 } }
        do:
          - set: { pc: 9 }
          - light: [ l9 ]
          - row: [ "Switch on the lights", "Draw the UI" ]
          - say: "The fifth job is to switch on the lights. This is the first draw."
      - when: { var: { pc: 9 } }
        do:
          - set: { pc: 99, room: "open", card: "down" }
          - light: [ l10 ]
          - row: [ "The card comes down and you see the lounge", "The system swaps the blank window for the main activity" ]
          - say: "The manager takes down the welcome card, and you see the lounge."
      - when: { var: { pc: 99 } }
        do:
          - say: "The app is showing. Press Reset to ring the bell again."
    refuse: "Ring the bell first."
script: [ ring, next, next, next, next, next, next, next, next, next ]
try:
  - text: "Press \"Ring the bell\", then press \"Do the next job\" until the lounge shows. Each step lights up in order. (Or press Play, and use Speed to slow it down.)"
    done: [ ring, next, next, next, next, next, next, next, next, next ]
  - text: "(Optional) Switch on \"Room already open\". The same ring now replays with fewer steps."
    done: [ "open=on" ]
    optional: true
```

**What you should notice:** With no room open, the manager gets one ready and the staff do five jobs before the lounge lights up. With a room open, fewer steps run.

## Build the logic

### Notice

Maya switches her phone on in the morning and taps the chat icon. The first screen takes a moment to appear. She leaves the chat, then taps the icon again a minute later. This time it opens faster. Same icon, same app, two different waits.

### Guess

What does the phone do on that first tap? Here are three ideas.

- A. The app was already running behind the scenes, and the phone only has to show its screen.
- B. The system starts a running copy of the app, the app sets itself up, and then its first screen is drawn.
- C. The home screen turns into the app.

Idea A is the common guess. It is sometimes true, which is why the second tap was faster. But after a restart nothing is running yet. Idea C is a picture of how smooth it looks. Idea B is what happens when no copy is running.

### Build

1. You ring the bell. This is the tap. My own simplification: the page I read starts at the system, so I leave out the home screen handing your tap on.
2. The manager checks whether the app already has a room open.
3. If a room is open, the manager takes you to it. Fewer jobs are needed, so it is quicker.
4. If no room is open, the manager starts getting a new one ready.
5. Straight away, the manager hangs the blank welcome card in the doorway. The documentation lists the card before the room is made. My own reasoning: the two happen close together.
6. In the room, the staff do five jobs in order. They set up the desk book, call the waiter, open the lounge, set out the furniture and switch on the lights.
7. When the lights come on, the manager takes down the card. You see the lounge.
8. Later the manager may close a room that is not needed, so the next ring starts again from step 4.

### Name it

A **process** is a running app together with the memory the system gave it. That plain meaning is general knowledge, not a quote from the Android pages. The Android documentation says that by default each app runs in its own Linux process. It says the system starts the process when any of the app's parts need to run. It shuts the process down when it is no longer needed, or when the system must get memory back for other apps.

A start with no process is a **cold start**. The documentation says it means an app starting from scratch, such as the first launch since the phone booted or since the system killed the app. A start where the process is already there is lighter. The documentation says a **warm start** does a subset of the cold start's work. A **hot start** has lower overhead still, because the system brings the app's main screen to the foreground.

At the start of a cold start the system shows a **starting window**, which the documentation describes as a blank window shown immediately after launch. Then the app process takes five steps, in the order the documentation gives them.

| Hotel | Android |
|---|---|
| Ring the bell | You tap the icon |
| The manager | The Android system |
| A room | The app's process |
| Blank welcome card | The blank starting window |
| Set up the desk book | Create the app object |
| Call the waiter | Launch the main thread |
| Open the lounge | Create the main activity |
| Set out the furniture | Initialize the UI |
| Switch on the lights | Draw the UI |

The **main thread** is the line of work the system starts the app with. In plain words, it handles your touches and the drawing of the screen. The documentation says it is in charge of dispatching events to the user interface, including drawing events. An **activity** is one screen of an app. The **main activity** is the one the documentation calls the first screen to appear when the user launches the app. After the first draw, the documentation says the system swaps the blank window for the main activity, and the user can start using the app.

## Predict the output

```challenge
type: predict
run: false
id: step-before-the-first-draw
verified: not-run
code: |
  A cold start, after the blank card is up.
  The staff do five jobs in this order:
    1. Create the app object
    2. Launch the main thread
    3. Create the main activity
    4. Initialize the UI
    5. Draw the UI
  Which job comes right before the first draw?
options:
  - id: a
    text: "Launch the main thread"
    why: "You may have picked the waiter because the waiter does the drawing. The waiter is called in as job 2. The job right before the lights is setting out the furniture."
  - id: b
    text: "Create the app object"
    why: "You may have picked the first job. It is the first in the list, not the one next to the drawing."
  - id: c
    text: "Initialize the UI"
    correct: true
    why: "The furniture is set out right before the lights come on. The documentation lists initializing the UI as the step right before drawing it."
output: "Initialize the UI"
```

```challenge
type: predict
run: false
id: what-the-guest-sees-first
verified: not-run
code: |
  Maya taps an icon on a cold start.
  The room is still being made ready.
  What does she see in the doorway?
options:
  - id: a
    text: "The app's real first screen"
    why: "You may have expected the lounge at once. The lounge is made only after the staff finish their jobs."
  - id: b
    text: "A blank starting window"
    correct: true
    why: "The manager hangs the welcome card right after launch. The documentation says the system shows a blank starting window immediately after launch."
  - id: c
    text: "The home screen, as if nothing was tapped"
    why: "You may have thought the phone ignores the tap until it is ready. The documentation says the system shows the blank window for the app immediately."
output: "A blank starting window"
```

```challenge
type: predict
run: false
id: second-tap-is-faster
verified: not-run
code: |
  Maya taps the chat icon after the phone restarts. It takes a while.
  She leaves, and taps the icon again a minute later.
  It opens faster.
  Which idea fits best?
options:
  - id: a
    text: "Less work is needed, because the app is already partly or fully set up (a warm or hot start)"
    correct: true
    why: "The room was still open, so the manager did not make a new one. The documentation says a warm start does a subset of the work, and a hot start has lower overhead."
  - id: b
    text: "The app never stops running, so every tap is the same"
    why: "You may have thought apps stay open for ever. The documentation says Android might shut down a process when resources are needed elsewhere."
  - id: c
    text: "The phone skips drawing the screen the second time"
    why: "You may have thought drawing is optional. The screen still has to be shown, but the earlier setup steps may not repeat."
output: "Less work is needed, because the app is already partly or fully set up (a warm or hot start)"
```

## Tricks to remember

- Ring, room, card, jobs, lights.
- No room open means a cold start. A room open means less work.
- The blank card goes up first. The lounge comes last.
- Five jobs, in order: book, waiter, lounge, furniture, lights.

## Common mistakes

- **Thinking the app is always running, waiting for your tap.** Icons sit there all day, so it feels like the apps do too. The Android documentation says the system shuts a process down when it is no longer needed or when memory is needed. Ask whether a room is open.
- **Thinking every tap does the same work.** An app that opens fast once feels like a fast app. The documentation names cold, warm and hot starts, and they do different amounts of work.
- **Thinking the blank window means the app crashed.** A blank moment feels like nothing is happening. The documentation says the system shows a blank starting window on purpose, until the first draw.

## Where this shows up in Android

You restart your phone and tap an app. It takes longer to appear than it did the day before. My own reasoning: this fits a cold start, because after a restart no room is open for the app yet.

Android measures the wait. The documentation calls it time to initial display, the time it takes to display the first frame of the app's UI. It says the Android Framework reports it automatically for every app.

## Say it in an interview

**What are cold, warm and hot starts?**
A cold start is an app starting from scratch, with no process yet. A warm start does a subset of that work. A hot start has lower overhead still, because the system only brings the app's screen to the foreground. The trade-off is time: the more has to be built, the longer the wait.

**What happens between the tap and the first frame on a cold start?**
The launch-time page lists the system's tasks as: load and launch the app, show a blank starting window, and create the app process. Then the app process creates the app object, launches the main thread, creates the main activity, initializes the UI and draws it. After the first draw, the system swaps the blank window for the main activity.

**Why does the system show a blank window first?**
My own reasoning: it answers the tap at once, so the phone does not look frozen while the process is made. The cost is that slow work in the five steps keeps the blank window up longer. The documentation measures this wait as time to initial display, the time to show the first frame.

**Is an app always running in the background?**
No. The documentation says the system starts the process when any of the app's parts need to run. It may shut the process down when it is no longer needed or when memory is needed for other apps. So the next tap may be a cold start.

## Recap

1. If the app has no running copy, the system starts one before anything is shown.
2. On a cold start the app sets itself up in five steps, and then draws its first screen.
3. A warm or hot start skips some of that work, so the app opens faster.

## You can now

- Name the cold-start job that comes right before the first draw.
- Predict what you see first on a cold start.
- Explain why a second tap on an app can be faster than the first.
