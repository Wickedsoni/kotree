---
id: foundation/how-computers-work/what-is-an-os
title: Who decides which app gets the screen and the memory?
status: live
difficulty: easy
order: 6
requires: [foundation/how-computers-work/what-is-a-cpu, foundation/how-computers-work/memory-vs-storage]
summary: One trusted program starts first and hands out the screen, the memory and the hardware, so apps keep out of each other's way.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [building-manager]
playground: false
fruit: false
upnext: false
---

## The question

You are playing a game. A chat message pops up. A music app keeps playing in the background. A photo app is busy saving a picture. That is four apps, one screen, one pool of memory and one camera.

What stops them from grabbing the same things at the same moment and wrecking each other's work? Who is in charge?

## Mental model

Think of an apartment building with a manager.

- The tenants are the apps. Each one wants a place to live and a turn at the shared things.
- Each flat is a private area of memory. A tenant keeps their own things inside it.
- The key opens one flat. A tenant has the key to their own flat and no other.
- The noticeboard in the lobby is the screen. Only one poster can cover it at a time.
- The manager hands out flats and keys, takes the flats back when a tenant leaves, and decides who gets the noticeboard next.

Tenants never walk into each other's flats. If a tenant wants the noticeboard, or anything else shared, they ask the manager.

**Where the analogy stops:** A building manager is a person who stands outside and watches. The program that does this job is software. It runs on the same processor (the CPU) as the apps, and it starts before any of them. Also, a building has fixed flats. On a phone, a flat is made when an app starts and handed back when the app is gone. And some things are shared on purpose: Android lets apps share the memory that holds the same system code and resources, but not each app's private data.

## See it move

```sim
id: building-manager
template: lanes
title: "Apartment building with and without a manager"
intro: "Chat, Music and Camera each live in their own flat. The noticeboard in the lobby is empty and the manager is on duty."
lanes:
  - { id: "flats", label: "Flats (each app's own memory)", kind: "row", slots: 3, index0: true }
  - { id: "board", label: "Noticeboard (the screen)", kind: "row", slots: 1, index0: true }
  - { id: "line", label: "Waiting line for the noticeboard", kind: "queue", slots: 3, index0: true }
items:
  - { id: "chat", label: "Chat", at: "flats.0" }
  - { id: "music", label: "Music", at: "flats.1" }
  - { id: "camera", label: "Camera", at: "flats.2" }
vars: { manager: "yes", served: 0 }
show:
  - { var: "manager", label: "Manager on duty" }
  - { var: "served", label: "Turns given" }
actions:
  - id: ask
    label: "Apps ask for the noticeboard"
    cases:
      - when: { var: { manager: "yes" } }
        do:
          - spawn: { id: "t1", label: "Chat's request", at: "line.0" }
          - spawn: { id: "t2", label: "Music's request", at: "line.1" }
          - spawn: { id: "t3", label: "Camera's request", at: "line.2" }
          - set: { served: 0 }
          - say: "Chat, Music and Camera all ask for the noticeboard. They wait in one line, Chat first."
      - do:
          - spawn: { id: "t1", label: "x", at: "line.0" }
          - spawn: { id: "t2", label: "x", at: "line.1" }
          - spawn: { id: "t3", label: "x", at: "line.2" }
          - remove: "t1"
          - remove: "t2"
          - remove: "t3"
          - spawn: { id: "mess", label: "Three posters piled on one board", at: "board.0" }
          - mark: { mess: "bad" }
          - say: "With no manager, all three apps put up a poster at once. The posters pile up on the noticeboard and cover each other."
  - id: give
    label: "Manager gives out the noticeboard"
    cases:
      - when: { var: { manager: "yes", served: 0 }, at: { t1: "line.0" } }
        do:
          - move: { t1: "board.0", t2: "line.0", t3: "line.1" }
          - set: { served: 1 }
          - say: "The manager puts Chat's poster on the noticeboard first, because Chat asked first. Music and Camera keep waiting."
      - when: { var: { manager: "yes", served: 1 } }
        do:
          - remove: "t1"
          - move: { t2: "board.0", t3: "line.0" }
          - set: { served: 2 }
          - say: "Chat's poster comes down. The manager gives the noticeboard to Music, the next in line."
      - when: { var: { manager: "yes", served: 2 } }
        do:
          - remove: "t2"
          - move: { t3: "board.0" }
          - set: { served: 3 }
          - say: "Music's poster comes down. Camera gets the noticeboard last."
      - when: { var: { manager: "yes", served: 3 } }
        do:
          - remove: "t3"
          - set: { served: 4 }
          - say: "Camera's poster comes down. Every app had a turn and no poster covered another."
    refuse: "Nothing to hand out. Either nobody has asked yet, or there is no manager to hand out turns."
  - id: remove-manager
    label: "Remove the manager"
    cases:
      - when: { var: { manager: "yes" } }
        do:
          - set: { manager: "no" }
          - say: "The manager is gone. Nobody hands out flats or turns any more."
    refuse: "The manager is already gone."
drops:
  - item: chat
    to: "flats.1"
    when: { var: { manager: "no" }, at: { chat: "flats.0" } }
    do:
      - remove: "music"
      - mark: { chat: "bad" }
      - say: "With no manager, Chat walks into flat 1 and writes over Music's things. Music is wrecked."
    refuse: "Nothing changes. With the manager on duty, Chat is kept in its own flat, flat 0, so Music's things stay safe."
script: ["ask", "give", "give", "give", "chat>flats.1", "remove-manager", "ask", "chat>flats.1"]
try:
  - text: "Press \"Apps ask for the noticeboard\". Then press \"Manager gives out the noticeboard\" three times and watch the order."
    done: ["ask", "give", "give", "give"]
  - text: "Drag Chat onto Music's flat (flat 1). See what the manager does."
    done: ["chat>flats.1"]
  - text: "Press \"Remove the manager\" and let the apps ask again. Then drag Chat onto Music's flat once more."
    done: ["remove-manager", "ask", "chat>flats.1"]
```

**What you should notice:** With the manager, each app gets its own flat and a turn on the noticeboard. With the manager removed, apps land on the same flat and their things overlap.

## Build the logic

### Notice

Three apps are running: Chat, Music and Camera. Chat wants to show a message on the screen. Music wants to remember where the song is up to. Camera wants to keep a photo it has taken.

All three need space in the same memory. If nobody is in charge, Chat might drop its words onto the spot where Music keeps the song position. The song would jump, and Chat would show nonsense.

### Guess

Who should settle this? Here are three ideas.

- A. The apps agree among themselves and take turns politely.
- B. One trusted program that all apps must ask. It hands out the space and the turns.
- C. You decide every time an app wants something.

Idea A works only if every app behaves. One badly written app breaks it for everyone. Idea C would bury you in questions, because apps ask for things all the time. Idea B is the answer.

### Build

1. One trusted program starts first, before any app.
2. When an app starts, it gets its own private area of memory. The trusted program keeps a list of who has which area.
3. When an app wants something shared, such as the screen, it asks. The trusted program decides who goes next.
4. An app does not reach the hardware itself. It asks the trusted program, which talks to the hardware.
5. If an app asks for something it was not given, such as another app's area, the answer is no.
6. When an app is gone, its area goes back on the list for the next app.

### Name it

The trusted program is the **operating system**, or OS for short. It manages the hardware and shares it among the apps. Windows, macOS, Linux and Android are operating systems.

The core of an operating system is its **kernel**. The Android documentation says the foundation of Android is the Linux kernel.

A running app, together with the memory the OS has given it, is a **process**. The Android documentation says that when part of an app starts and no other part of that app is running, the system starts a new process for it.

When an app needs something sensitive outside its own area, such as your camera or your contacts, Android asks you first. That question is a **permission** request.

| Apartment building | Official name | Android example |
|---|---|---|
| Tenant | App (running as a process) | Your chat app |
| Flat | The app's private area of memory | The chat app's own working space |
| Key to your flat only | Each app can reach only its own area | Other apps cannot read the chat app's private data |
| Noticeboard | The screen | The app you opened is the one you see |
| Manager | Operating system | Android, built on the Linux kernel |
| Asking the manager | Permission | A pop-up asking if an app may use your camera |

## Predict the output

```challenge
type: predict
run: false
id: who-gets-the-board
verified: not-run
code: |
  A toy building. The toy manager's rule:
  the noticeboard goes to whoever asked first.

  Maps asked at 9:01 and has the board now.
  Chat asked at 9:02.
  Camera asked at 9:03.

  Maps finishes. Who gets the board next?
  (This is the toy rule, not how Android works.)
options:
  - id: a
    text: "Camera"
    why: "You may have given the board to the newest request. The toy rule serves the oldest request first."
  - id: b
    text: "Chat"
    correct: true
    why: "The manager follows the rule. Chat asked before Camera, so Chat is next."
  - id: c
    text: "Maps again"
    why: "You may have thought the app already on the board keeps it. Maps has finished, so the manager hands the board on."
output: "Chat"
```

```challenge
type: predict
run: false
id: no-manager
verified: not-run
code: |
  A toy building with the manager removed.
  Nobody hands out flats, so every tenant picks a spot themselves.

  Chat picks memory spot 7 and stores the word "hello".
  Music also picks memory spot 7 and stores the number 42.

  What does Chat find at spot 7 afterwards?
options:
  - id: a
    text: "hello, because Chat got there first"
    why: "You may have expected the first tenant to keep the spot. With no manager there is no list saying who owns it."
  - id: b
    text: "42, because Music wrote over it"
    correct: true
    why: "Nobody was keeping track of the flats, so two tenants used the same one. The later write replaced the earlier one."
  - id: c
    text: "Both hello and 42 side by side"
    why: "You may have thought a spot holds two things. A spot holds one thing, so the second replaces the first."
output: "42, because Music wrote over it"
```

```challenge
type: predict
run: false
id: ask-for-the-key
verified: not-run
code: |
  A chat app asks to use your camera for the first time.
  The camera is outside the app's own flat.
  The Android documentation says what you see on the screen
  when an app asks for something like this.
  What do you see?
options:
  - id: a
    text: "Nothing. The app takes the camera quietly"
    why: "You may have thought apps can use anything on the phone. Each app stays in its own flat unless the manager lets it out."
  - id: b
    text: "A message from the system, where you choose whether to allow it"
    correct: true
    why: "The manager does not hand over keys on its own. The documentation says users see a system permission dialog and choose whether to grant the permission."
  - id: c
    text: "The app crashes and the phone restarts"
    why: "You may have expected a hard stop. A refusal is a normal answer, and the app is expected to carry on without the camera."
output: "A message from the system, where you choose whether to allow it"
```

## Tricks to remember

- Apps ask. The manager decides.
- Own flat, own key. No key for the flat next door.
- Take the manager away and everyone's things end up in the same room.

## Common mistakes

- **Thinking apps talk to the hardware directly.** An app seems to "use the camera", so it feels like a direct link. In fact the app asks, and the OS does the talking to the camera. Next time, picture the app asking the manager.
- **Thinking the OS is an app like the others.** It runs on the same processor and looks like a program, so the mix-up is natural. The difference is the job: the OS starts first and decides what the apps get. Ask who hands out the flats.
- **Thinking an app can read any other app's data.** On a computer you can open files, so it feels like apps can do the same to each other. The Android documentation says other processes on the system can't access an app's private data. Ask which flat the data lives in.

## Where this shows up in Android

You install a new photo app and open it. A small box appears, asking if the app may use your camera. That box comes from the system, not from the app. It is the manager asking you before it hands over a key. The Android documentation says every Android app runs in a limited-access area, which it calls a **sandbox**. An app that wants something sensitive outside it has to ask.

The same manager explains a second symptom. You switch from a game to the camera and back, and the game starts from the title screen. Android may shut down a process when other processes need the resources more, so the manager took back a flat. Anything the game had not saved went with it.

Under both sits the Linux kernel. The Android documentation also describes a layer that sits between Android's app tools and the hardware, with one piece of code for each kind of hardware, such as the camera. Apps reach the camera through the manager's layers, not directly.

## Recap

1. The operating system starts first and hands out memory, the screen and the hardware.
2. Each app gets its own private area and asks for anything it was not given.
3. Without that manager, apps would overwrite each other and fight for the screen.

## You can now

- Explain why two apps cannot grab the same memory or the whole screen, using the apartment building.
- Predict which app gets a shared thing next under a stated rule.
- Predict what goes wrong when the manager is removed.
