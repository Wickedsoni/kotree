---
id: android-core/what-is-an-app/what-is-inside-an-app
title: "What is actually inside an Android app?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: An Android app is a set of separate parts, not one program. Four kinds exist, and the system can start each on its own.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/guide/components/fundamentals (four component types and their definitions; "entry point"; "there's no main() function"; the manifest declaration rule; a service may play music in the background; a content provider lets other apps query or modify data "if the content provider permits it"). The Predict items were traced by hand. The situations in them are invented teaching examples. No sim: the roadmap row says a table is enough. -->

## The question

You open a music app, press play, and go back to your home screen. The screen is gone, but the music keeps playing. Then the phone says the battery is low, and the app hears it too. If an app is one program, how can a part of it work with no screen? What is actually inside an Android app?

## Mental model

Think of a restaurant.

- The dining room is where customers sit and order. It is the part people see and touch.
- The kitchen works out of sight. Food keeps cooking even when the dining room is empty.
- The doorbell rings when something arrives from outside. Someone stops what they are doing and answers it.
- The shared pantry is a shelf of ingredients. Other people can ask for some of them, but only if the restaurant allows it.
- The sign by the entrance lists every room that exists.
- The owner decides which room to open and when.

A restaurant is not one person doing one long list of jobs. It is a few rooms, each with its own job.

**Where the analogy stops:** The owner walks to the kitchen and switches it on. In Android the phone's system does that, and it can start any one room without the others. Also, a restaurant has one front door. An Android app has several, and the system or the user can use any of them.

## Build the logic

### Notice

Look at the music app again. Three things happened.

- You pressed play on a screen.
- The sound kept going after the screen closed.
- The phone told the app that the battery was low.

These are three different jobs. One needs a screen. One must work with no screen. One is the app being told about something that happened outside it.

### Guess

How many kinds of building block would you give an app?

- A. One: a single program that runs from top to bottom, and the screen is part of it.
- B. Two: one for screens and one for everything else.
- C. Four: one for screens, one for background work, one for hearing news from the system and one for sharing stored data.

Idea A is the most common guess, because earlier programs ran from top to bottom. Idea B hides the difference between hearing news and doing work. Android uses idea C.

### Build

1. Give every job that has a screen its own part. The user sees and touches only this part.
2. Give work that must continue with no screen its own part. It never shows anything.
3. Give "something happened outside the app" its own part. It wakes up, reacts and is done.
4. Give "a shelf of stored data other parts can read" its own part, with rules about who may ask.
5. Write a list of all the parts, so the system knows each one exists before it tries to start it.

### Name it

The Android documentation calls the parts **app components**. It says each component is an **entry point**, a door through which the system or a user can enter your app. That is why the documentation says Android apps do not have a single entry point: there is no `main()` function.

There are four kinds.

| Restaurant | Component | What the documentation says |
|---|---|---|
| Dining room | **Activity** | A single screen with a user interface |
| Kitchen | **Service** | Runs in the background for long-running work. It does not provide a user interface |
| Doorbell | **Broadcast receiver** | Lets the system deliver events to the app outside of a regular user flow |
| Shared pantry | **Content provider** | Manages a shared set of app data. Other apps can query or modify it if the provider permits it |

The sign by the entrance is the **manifest**, a file named `AndroidManifest.xml`. The documentation says an activity, service or content provider that is in your source but not declared in the manifest is not visible to the system and can never run. A receiver can be declared there or registered in code.

This page has no program to run. A table is the whole map.

## Predict the output

```challenge
type: predict
run: false
id: what-keeps-working
verified: not-run
code: |
  An app has these four parts:
    Part 1: the screen that shows the song name and a Play button
    Part 2: the code that plays the music
    Part 3: the code that reacts when the headphones are unplugged
    Part 4: the store of saved playlists
  You press Play and go back to the home screen, so the screen closes.
  Which part is meant to keep playing the music with no screen?
options:
  - id: a
    text: "Part 1, the screen"
    why: "You may have thought the Play button owns the music. The screen is the dining room. When the room is empty, the kitchen can still be cooking."
  - id: b
    text: "Part 2, the code that plays the music"
    correct: true
    why: "This is the kitchen. A service has no screen and keeps running in the background. Playing music is the example the documentation gives."
  - id: c
    text: "Part 4, the store of saved playlists"
    why: "You may have picked the part that holds the data. The pantry holds ingredients. It does not cook anything."
output: |
  Part 2, the code that plays the music
```

```challenge
type: predict
run: false
id: sort-the-four-parts
verified: not-run
code: |
  Match each situation to a kind of part.
    Situation 1: the user looks at a settings page and taps a switch.
    Situation 2: the phone announces that it was plugged in to charge.
    Situation 3: the app shares its contacts list with another app, with permission.
  Which list of kinds is right, for situations 1, 2, 3?
options:
  - id: a
    text: "Activity, content provider, broadcast receiver"
    why: "You may have swapped 2 and 3. A plug-in announcement comes from outside and rings the doorbell. A shared list of data sits on the pantry shelf."
  - id: b
    text: "Service, broadcast receiver, content provider"
    why: "You may have thought a service handles every tap. A page the user looks at and touches is the dining room, which is an activity."
  - id: c
    text: "Activity, broadcast receiver, content provider"
    correct: true
    why: "A screen the user touches is the dining room. News from the system rings the doorbell. Data that other apps may ask for sits in the pantry."
output: |
  Activity, broadcast receiver, content provider
```

```challenge
type: predict
run: false
id: remove-the-dining-room
verified: not-run
code: |
  A small music app has one service that plays songs and one activity that shows the screen.
  A developer deletes the activity and keeps the service.
  What does the user lose?
options:
  - id: a
    text: "The user can no longer see or tap anything in the app, so there is no way to choose a song"
    correct: true
    why: "The dining room is gone. The kitchen still exists, but nobody can walk in, sit down and order."
  - id: b
    text: "The service stops existing too, because it needs the activity to run"
    why: "You may have thought the rooms depend on each other. Each one is its own door, and the system can start a service without an activity."
  - id: c
    text: "Nothing changes, because the screen was never needed"
    why: "You may have thought the screen is decoration. It is the part where the user speaks to the app."
output: |
  The user can no longer see or tap anything in the app, so there is no way to choose a song
```

## Tricks to remember

- Four rooms, four jobs: see it, work behind it, hear news, share data.
- Dining room, kitchen, doorbell, pantry: activity, service, receiver, provider.
- No `main()`: the system picks which door to open.
- A room missing from the sign by the entrance can never open.

## Common mistakes

- **Thinking an app is one big program that runs from top to bottom.** It feels right because every program you wrote so far did. Picture a restaurant with several rooms, and ask which room a piece of code belongs to.
- **Thinking an activity is the whole app.** The screen is the part you see, so it feels like everything. The same app can hold background work and receivers that you never see.
- **Thinking all four kinds are used in every app.** The list is long, so it feels like a checklist. An app uses a kind only when a job needs it.
- **Forgetting to list a part in the manifest.** The code exists, so it feels finished. The documentation says an undeclared activity, service or provider can never run.

## Where this shows up in Android

Go back to the opening picture. You pressed play and left the screen, yet the music continued. The documentation gives this exact job to a service: playing music in the background while the user is in a different app. The screen was the dining room, and it closed. The kitchen kept cooking.

The low-battery message is an example of a system-wide announcement, which is the job the documentation gives a broadcast receiver (my own example). So when you see "something keeps happening after I leave the screen" or "my app reacted with no screen showing", look first at a service or a broadcast receiver, not an activity.

## Say it in an interview

These questions are my own, written to fit this lesson. The branch's interview file asks about APK contents, which another lesson covers.

**What are the four app components, and what does each one do?**
Activity, service, broadcast receiver and content provider. An activity is one screen with a user interface. A service runs long work in the background with no interface. A broadcast receiver lets the system deliver an event to the app. A content provider manages shared app data that other apps can use if it permits.

**Android apps have no `main()`. How does an app start?**
The documentation says each component is an entry point that the system or a user can use to enter the app, so there is no single entry point. The system starts the component that is needed. It does not run the app from top to bottom.

**Why must components be declared in the manifest?**
The documentation says an activity, service or content provider that is not declared is not visible to the system and can never run. A broadcast receiver is the exception, because it can also be registered in code. The manifest is how the system learns what exists.

**When would you use a service instead of an activity?**
Use a service for work that must go on with no screen, such as playing music while the user is in another app. My own reasoning: a service has no interface, so if the job needs the user to see or touch something, it belongs in an activity.

## Recap

1. An Android app is a set of components, not one program that runs from top to bottom.
2. The four kinds are activity, service, broadcast receiver and content provider.
3. Each component is its own entry point, so Android has no single `main()`.

## You can now

- Predict which kind of part keeps working after the screen closes.
- Sort a situation, such as a tap on a switch or a system announcement, into the right kind of component.
- Say what the user loses when the screen part is removed and the background part stays.
