---
id: os/processes/what-is-a-process
title: Why does one crashing app leave all the other apps running?
status: live
difficulty: easy
order: 3
requires: [os/what-an-os-does/kernel-and-system-calls]
summary: Each running app lives in its own process, with its own walls. A crash stops that process and leaves the others running.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [sealed-flats]
playground: false
fruit: false
upnext: false
---

## The question

You have a map app, a music player and a notes app open. The music player stops working, and a message tells you it has stopped. The map and the notes carry on as if nothing happened. Why does one broken app not take the whole phone down with it?

## Mental model

Think of a block of flats, run by a caretaker.

- The block is your phone. The caretaker who runs it is the operating system.
- The plan of a flat, kept on paper in a cupboard, is a **program**. A program that is not running does nothing.
- A flat that someone lives in is a **process**. The caretaker builds it from the plan when you open the app.
- The people and furniture inside are the app's running code and the memory it uses.
- The door and walls are what keep each flat apart from the next.
- A fire in one flat is a crash.

A fire in one flat is bad for that flat. The doors and walls hold, so the fire does not walk into the next flat. The caretaker closes the burnt flat, and the other flats carry on.

**Where the analogy stops:** A fire in a real block can spread through a shared pipe. Processes can share things too, but only in ways the operating system allows. Also, the caretaker may close a quiet flat on purpose when the block runs short of space. Android may do the same to a process. And a flat is made of bricks, while a process is made of memory and settings that the operating system keeps apart.

## See it move

```sim
id: sealed-flats
template: zones
title: Flats in a block, each with its own walls
intro: "A block has three flats: Maps, Music and Notes. Each flat has its own door and walls, and all three are lived in. A fourth flat is free."
zones:
  - { id: maps-flat, label: "Flat 1" }
  - { id: music-flat, label: "Flat 2" }
  - { id: notes-flat, label: "Flat 3" }
  - { id: free-flat, label: "Free flat" }
items:
  - { id: maps, label: "Maps", at: maps-flat }
  - { id: music, label: "Music", at: music-flat }
  - { id: notes, label: "Notes", at: notes-flat }
vars: { running: 3, copies: 0, walls: "on" }
show:
  - { var: running, label: "Flats lived in" }
toggles:
  - { id: walls, label: "Walls between the flats", var: walls }
actions:
  - id: fire
    label: Start a fire in Flat 2
    cases:
      - when: { mark: { music: bad } }
        do:
          - say: "Flat 2 is already burnt out. Flats 1 and 3 are untouched."
      - do:
          - mark: { music: bad }
          - label: { music: "Music: fire, stopped" }
          - add: { running: -1 }
          - say: "Flat 2 is on fire and the people leave it, and the door and walls hold so Flats 1 and 3 carry on, with {running} flats still lived in."
  - id: spread
    label: Let the mess spread to Flat 3
    cases:
      - when: { mark: { music: bad }, var: { walls: "off" } }
        unless: { mark: { notes: bad } }
        do:
          - mark: { notes: bad }
          - label: { notes: "Notes: spoiled" }
          - add: { running: -1 }
          - say: "With no walls, the mess from Flat 2 flows into Flat 3 and Notes is spoiled as well, with {running} flats still lived in."
      - when: { mark: { notes: bad } }
        do:
          - say: "Flat 3 is already spoiled. Nothing more can happen to it."
    refuse: "The wall stops it. Nothing from Flat 2 gets into Flat 3, so Notes carries on."
  - id: copy
    label: Open Notes a second time
    cases:
      - when: { var: { copies: 0 } }
        do:
          - add: { copies: 1 }
          - add: { running: 1 }
          - spawn: { id: notes-two, label: "Notes, second run", at: free-flat }
          - say: "A second run of Notes gets its own new flat, built from the same plan, so two flats now hold Notes and each has its own walls."
    refuse: "Both Notes flats are already there."
script: [fire, spread, copy, "walls=off", spread]
try:
  - text: "Press \"Start a fire in Flat 2\". Look at Flats 1 and 3."
    done: [fire]
  - text: "Press \"Let the mess spread to Flat 3\". The wall stops it."
    done: [spread]
  - text: "Press \"Open Notes a second time\". A new flat appears for the second run."
    done: [copy]
  - text: "(Optional) Switch off \"Walls between the flats\". Your earlier presses play again with no walls, so Flat 3 is spoiled. Then press \"Let the mess spread to Flat 3\" once more and read what is said. This is what the walls prevent."
    done: ["walls=off", spread]
    optional: true
```

**What you should notice:** The fire stays in Flat 2 while the walls are up, and a second run of the same app gets a flat of its own.

## Build the logic

### Notice

Three flats stand side by side. The Maps flat, the Music flat and the Notes flat are all lived in. Then the Music flat catches fire. The people inside leave. In the other two flats, the lights stay on.

### Guess

How does the block make sure a fire in one flat cannot reach the others? Here are three ideas.

- A. Each flat is sealed with its own walls and door. The caretaker checks any request that tries to cross a wall.
- B. Everyone lives in one big hall. If the hall burns, everyone leaves.
- C. Only one flat is lived in at a time, so nothing can spread.

Idea A is what the operating system does. Idea B is what you get with no walls at all. Idea C would mean a phone that runs one app at a time.

### Build

1. A program is a plan on paper. On its own it does nothing.
2. When you open an app, the caretaker builds a flat from the plan. This lived-in flat has its own door, its own walls and its own rooms for the memory it needs.
3. Everything the app does happens inside its own flat.
4. A flat cannot reach through a wall into another flat. If two flats must share something, they ask the caretaker, and the caretaker decides.
5. If something goes wrong inside a flat, the caretaker closes only that flat.
6. Start the same plan twice and you get two separate flats. The second one starts empty. What the first flat remembered stays in the first flat, and when a flat is closed, what it remembered is gone.

### Name it

A program is a file of instructions that sits on the disk. A process is a program that is running, together with the memory the operating system keeps for it. **Isolation** means the operating system keeps one process's memory and settings apart from another's.

| Block picture | Word |
|---|---|
| Plan in the cupboard | Program, such as an installed app file |
| A flat lived in | Process |
| People and furniture inside | The running code and its memory |
| Door and walls | Isolation, kept by the operating system |
| Fire | Crash |
| Caretaker | Operating system |

Starting the same program twice gives two processes, one for each start (general knowledge).

Three facts from the Android documentation fit this picture. The Application Sandbox page says Android assigns a unique user ID, a number the system uses to tell who owns what (general knowledge), to each app and runs it in its own process. It says Android uses that ID to set up a kernel-level **sandbox**, a walled area that the operating system keeps an app inside, and that the kernel enforces security between apps at the process level. The processes page says that by default all the **components** of one app run in the same process, and that most apps do not change this. A component is one part of an app, such as a screen or a background service (general knowledge). The crash page says that when an app crashes, Android terminates the app's process and displays a dialog to let the user know that the app has stopped.

The processes page also says that the manifest, the file that lists an app's parts, can give each component an `android:process` attribute, which can name a process for that component to run in. It says components of different apps can share a process only if the apps share the same Linux user ID and are signed with the same certificate, the developer's digital signature on the app (general knowledge). The Application Sandbox page says that by default apps cannot interact with each other.

## Predict the output

```challenge
type: predict
run: false
id: two-starts-two-processes
verified: not-run
code: |
  On a laptop you open two terminal windows
  and start the same small program in each one. Both are still running.

  How many processes are running that program now?
options:
  - id: a
    text: "One, because there is one program file"
    why: "You may have counted the plans in the cupboard. There is one plan, but each start builds its own flat."
  - id: b
    text: "Two, one for each start"
    correct: true
    why: "The program is the plan on paper. Each time you start it, the caretaker builds a new flat from it, so two starts make two processes."
  - id: c
    text: "None, until you type something into the editor"
    why: "You may have thought a flat only exists when someone is busy in it. The flat is built when the program starts."
output: |
  Two, one for each start
```

```challenge
type: predict
run: false
id: counter-forgotten
verified: not-run
code: |
  A small program keeps a counter in memory.
  It starts the counter at 0, adds 1, prints the counter and ends.

  You start the program three times in a row.
  What do the three runs print?
options:
  - id: a
    text: "1, 2, 3"
    why: "You may have thought the counter carries on from one run to the next. Each run is a new flat, and the old flat's memory is gone when it closes."
  - id: b
    text: "0, 0, 0"
    why: "You may have forgotten the step that adds 1. Each run adds 1 to its own counter before it prints."
  - id: c
    text: "1, 1, 1"
    correct: true
    why: "Each run is a new flat that starts empty. The counter starts at 0, becomes 1, is printed and is lost when the flat closes. The next run cannot see it."
output: |
  1, 1, 1
```

```challenge
type: predict
run: false
id: crash-in-one-screen
verified: not-run
code: |
  Your app has screen A and screen B.
  Neither screen changes any process setting.
  Screen A crashes with an error that nothing handles.

  What happens to screen B?
options:
  - id: a
    text: "It stops too, because the whole app shares one process and Android ends that process"
    correct: true
    why: "By default all of an app's parts live in one flat. The crash ends that flat, so screen B goes with screen A. Other apps' flats are untouched."
  - id: b
    text: "It carries on, because every screen has its own process"
    why: "You may have given each screen its own flat. By default, an app's parts share one flat."
  - id: c
    text: "The whole phone restarts"
    why: "You may have thought a crash reaches every flat. The walls keep it inside the app's own flat."
output: |
  It stops too, because the whole app shares one process and Android ends that process
```

## Tricks to remember

- Program is the plan. Process is the flat lived in.
- One fire, one flat. The walls hold.
- Same plan started twice means two flats, and each starts empty.
- An app is one flat by default, so a crash in one screen closes the whole app.
- Task: start a small program twice, for example in two terminal windows, then open the system's list of running programs and count its entries.

## Common mistakes

- **Thinking one crashing app crashes the whole phone.** One bad moment feels like the whole phone is shaky. The crash ends the app's own process, and the others carry on.
- **Thinking a program and a process are the same thing.** People say "the app" for both. The plan on paper is the program. The flat lived in is the process, and you can have two flats from one plan.
- **Thinking two screens of one app are two processes.** Two screens feel like two flats. The Android documentation says that by default all of an app's components run in one process.
- **Thinking a second run remembers the first.** It is the same program, so it feels like the same flat. A new run is a new flat, and what an earlier flat kept in memory is gone.

## Where this shows up in Android

When an app crashes, you see a dialog that says the app has stopped, and the other apps stay open. The Android documentation says Android terminates the app's process and displays that dialog. The part of this lesson that explains it is the flat with its own walls.

There is a second symptom. The processes page says Android might shut down a process when resources are needed by other processes that are more immediately serving the user, and that a process is started again when there is work for it to do. So an app can be closed without a crash, like a quiet flat closed by the caretaker. My own reasoning: that is why a screen can come back with its old state lost, and it is a topic of its own.

## Say it in an interview

**What is the difference between a program and a process?**
A program is a file of instructions on the disk. A process is a running copy of it, with its own memory. Starting the same program twice gives two processes. The operating system keeps a record for each process, often called a process control block (general knowledge).

**Why does a crash in one Android app leave the others running?**
The Android documentation says each app runs in its own process, and that a crash makes Android terminate that app's process. The kernel keeps processes apart, so the others carry on. The trade-off is that apps cannot share memory directly, so they need other ways to pass data, which the operating system provides (my own reasoning).

**How does Android keep apps apart?**
The Application Sandbox page says Android assigns a unique user ID to each app and runs it in its own process. The kernel uses that ID to set up the sandbox and enforces it at the process level. By default apps cannot interact with each other.

**When is `android:process` useful, and when is it risky?**
The processes page says it can put a component in its own process, so components of one app can run apart. My own reasoning: it can keep a risky component from taking down the rest of the app. The cost is more memory and the loss of direct sharing, because a separate process has separate memory. The documentation says most apps do not change the default.

## Recap

1. A program is a file, and a process is that program running with its own memory.
2. The operating system keeps processes apart, so a crash stays in one process.
3. By default an Android app runs in one process, so a crash closes the whole app.

## You can now

- Explain why one crashing app leaves the other apps running.
- Predict how many processes exist after the same program is started twice.
- Predict what a program prints on a second run when it kept its counter only in memory.
- Predict what happens to a second screen of the same app when the first one crashes.
