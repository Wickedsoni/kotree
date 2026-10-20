---
id: android-core/what-is-an-app/what-is-inside-an-app
title: "What is actually inside an Android app?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: An Android app is a set of separate parts, not one program. Four kinds exist, and each one can be started on its own.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. Android claims were checked on 21 Oct 2026 against https://developer.android.com/guide/components/fundamentals (four component types, "no main()", manifest, service has no UI, provider sharing). Not checked: how each part behaves under current background limits on the newest Android versions (kept out of the text on purpose). Predict items were traced by hand only. No sim: the roadmap row says a labelled diagram is enough. -->
## The question

You open a music app, press play, and swipe back to your home screen. The screen is gone, but the music keeps playing. A moment later your phone buzzes because the battery is low, and the music app notices too.

If an app is one program, how can a part of it keep working with no screen? What is actually inside an Android app?

## Mental model

Think of a restaurant.

- The dining room is the part customers see and use. It is where people sit and order.
- The kitchen works out of sight. Food keeps cooking even when the dining room is empty.
- The doorbell rings when something arrives from outside. Someone stops what they are doing and answers it.
- The shared pantry is a shelf of ingredients. Other people can ask for some of them through a front desk, if the restaurant allows it.

A restaurant is not one person doing one long list of jobs. It is a few places, each with its own job. The owner decides which place to open and when.

**Where the analogy stops:** A restaurant owner walks to the kitchen and switches it on. In Android, the phone's system does that for you, and it can start any one of these parts without the others. A restaurant also has one front door. An Android app has several doors, and the system can use any of them.

## Build the logic

### Notice

Look at the music app again. Three things happened.

- You pressed play on a screen.
- The sound kept going after the screen closed.
- The phone told the app that the battery was low.

These are three different jobs. One needs a screen. One must work with no screen. One is the app being told about something that happened outside it.

### Guess

How many kinds of building block would you give an app?

- A. One. A single program that runs from the top to the bottom, and the screen is part of it.
- B. Two. One for screens and one for everything else.
- C. Four. One for screens, one for background work, one for hearing news from the system and one for sharing stored data.

Idea A is the most common guess, because earlier programs ran from top to bottom. Idea B hides the difference between hearing news and doing work. Android uses idea C.

### Build

1. Give every job that has a screen its own part. The user sees and touches only this part.
2. Give work that must continue without a screen its own part. It never shows anything.
3. Give "something happened outside the app" its own part. It wakes up, reacts, and is done.
4. Give "a shelf of stored data other parts can read" its own part, with rules about who may ask.
5. Write down a list of all the parts, so the system knows each one exists before it tries to start it.

### Name it

The Android documentation calls the parts **app components** and says they are the essential building blocks of an Android app. Each component is an entry point: a door the system or a user can use to enter your app. This is why Android apps do not have a single entry point and there is no `main()` function.

There are four kinds.

| Restaurant | Component | What it does |
|---|---|---|
| Dining room | **Activity** | One screen with a user interface, where the user interacts with the app |
| Kitchen | **Service** | Runs in the background for long jobs. It has no user interface |
| Doorbell | **Broadcast receiver** | Lets the system deliver an event, such as a low battery announcement, to the app |
| Shared pantry | **Content provider** | Manages a shared set of app data. Other apps can read or change it only if the provider allows |

Step 5 of Build is the **manifest**, a file named `AndroidManifest.xml`. The app lists its components there. The documentation says that an activity, service or content provider that is not declared in the manifest is not visible to the system and can never run. A receiver may be declared in the manifest or registered in code.

This lesson only names the four kinds. Later lessons open each one.

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
    why: "This is the kitchen. A service is a part with no screen that keeps running in the background, and playing music is the example the Android documentation gives."
  - id: c
    text: "Part 4, the store of saved playlists"
    why: "You may have picked the part that holds the data. The pantry only holds ingredients. It does not cook anything."
output: "Part 2, the code that plays the music"
```

```challenge
type: predict
run: false
id: sort-the-four-parts
verified: not-run
code: |
  Match each situation to a kind of part.
    Situation 1: the user is looking at a settings page and taps a switch.
    Situation 2: the phone announces that it has just been plugged in to charge.
    Situation 3: the app makes its contacts list available to another app, with permission.
  Which list of kinds is right, for situations 1, 2, 3?
options:
  - id: a
    text: "Activity, content provider, broadcast receiver"
    why: "You may have swapped 2 and 3. A plug-in announcement comes from outside and wakes the doorbell. A shared list of data belongs on the pantry shelf."
  - id: b
    text: "Service, broadcast receiver, content provider"
    why: "You may have thought a service handles every tap. A page the user looks at and touches is the dining room, which is an activity."
  - id: c
    text: "Activity, broadcast receiver, content provider"
    correct: true
    why: "A screen the user touches is the dining room. News from the system rings the doorbell. Data that other apps may ask for sits in the shared pantry."
output: "Activity, broadcast receiver, content provider"
```

```challenge
type: predict
run: false
id: remove-the-dining-room
verified: not-run
code: |
  A small music app has a service that plays songs and one activity that shows the screen.
  A developer deletes the activity and keeps the service.
  What does the user lose?
options:
  - id: a
    text: "The user can no longer see or tap anything in the app, so there is no way to choose a song"
    correct: true
    why: "The dining room is gone. The kitchen still exists, but nobody can walk in, sit down and order."
  - id: b
    text: "The service stops existing too, because it needs the activity to run"
    why: "You may have thought the parts depend on each other. Each part is its own door, and the system can start a service without an activity."
  - id: c
    text: "Nothing changes, because the screen was never needed"
    why: "You may have thought the screen is only decoration. It is the part where the user speaks to the app."
output: "The user can no longer see or tap anything in the app, so there is no way to choose a song"
```

## Tricks to remember

- Four parts, four jobs: see it, work behind it, hear news, share data.
- Restaurant order: dining room, kitchen, doorbell, pantry.
- No `main()` in Android: the system picks which door to open.
- A part that is not in the manifest cannot start.

## Common mistakes

- **Thinking an app is one big program that runs from top to bottom.** It feels right because every program you wrote so far did. Picture a restaurant with several rooms instead, and ask which room a piece of code belongs to.
- **Thinking an activity is the whole app.** The screen is the part you see, so it feels like everything. The same app can hold background work and receivers that you never see.
- **Thinking all four kinds are used in every app.** The list is long, so it feels like a checklist. Many apps use only an activity at first. The other kinds are added when a job needs them.
- **Thinking you start your own parts the way you call a function.** Calling a function feels familiar. The Android system starts the parts, and it needs them listed in the manifest first.

## Where this shows up in Android

Go back to the opening picture. You pressed play and left the screen, yet the music continued. The Android documentation gives this exact kind of job to a service: playing music while the user is in another app. The screen was the dining room, and it closed. The kitchen kept cooking.

The phone buzzing about low battery is the doorbell. The system delivered an event to the app, outside of any screen the user had open.

So when you see a symptom like "something keeps happening after I leave the screen" or "my app reacted with no screen showing", this lesson tells you where to look first: a service or a broadcast receiver, not an activity.

## Recap

1. An Android app is a set of components, not one program that runs from top to bottom.
2. The four kinds are activity, service, broadcast receiver and content provider.
3. The system starts a component through the manifest list, so Android has no `main()`.

## You can now

- Name the four kinds of app component and say in one line what each does.
- Predict which kind of part keeps working after the screen closes.
- Sort a situation, such as a user tapping a switch or the system announcing a low battery, into the right kind of component.
