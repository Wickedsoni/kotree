---
id: android-core/what-is-an-app/who-starts-your-app
title: "Who starts my app if I never call main()?"
status: shadow
difficulty: easy
order: 2
requires: [android-core/what-is-an-app/what-is-inside-an-app]
summary: "No main() starts an Android app. The system starts one screen your manifest marks as the front door, and calls its first method."
video: ""
publishedOn:
updatedOn:
sims: [launch-path-manifest]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://developer.android.com/guide/components/fundamentals (no single entry point, no main(); the system starts the process when a component needs to run; the manifest declares components) ; https://developer.android.com/guide/components/intents-filters (MAIN plus LAUNCHER must be paired for the activity to appear in the app launcher; the Home app finds them; the system starts the matching activity by calling onCreate) ; https://developer.android.com/guide/components/activities/activity-lifecycle (onCreate fires when the system first creates the activity) ; https://developer.android.com/topic/performance/memory-overview (each app process is forked from Zygote; a process is kept cached after the user leaves). ⚠️ VERIFY: compile not run for the kotlin block (no shell). ⚠️ VERIFY: the hop "the home screen asks the system to start the screen" is my own simplified reasoning, no official page states the tap mechanics. ⚠️ VERIFY: the roadmap sim text says the app "fails to open" without the launcher pair, but the docs only say the activity does not appear in the launcher; the sim should show a missing icon. -->

## The question

Every program you have written so far starts at a function called main(). In an Android app you write screens, and you never write a line that says "open my screen". Yet you tap the icon and your screen appears. Who started it? And how does Android know which of your screens to open first?

## Mental model

Think of a theatre.

- The stage manager decides when each actor walks on. The stage manager is the phone's system.
- The actors are the parts of your app, such as its screens. An actor never walks on by itself.
- The cast list pinned backstage names every actor. It also marks the one who opens the show. This list is your app's **manifest**, a file named `AndroidManifest.xml` that tells the system what parts the app has.
- The poster board in the lobby has one poster for each show. Tapping a poster tells the stage manager to start that show.
- The stage is where a show happens. If no show of yours is running, the stage manager sets up a stage first.

So your code does not start the show. It waits to be called on.

**Where the analogy stops:** A theatre show starts once and runs to the end. Android can also start your parts through other doors, for example when another app asks. It may keep the stage standing after you leave, so the next start is faster. And the poster board is itself an app on the phone, not a person in the lobby.

## See it move

<!-- SIM: launch-path-manifest -->

**Try this:**
1. Set the cast list to "front door marked", then press "Tap icon". Watch the ball go from the poster board to the stage manager, to a new stage, and then to your first screen.
2. Set the cast list to "no front door marked", and look at the poster board.
3. Tick "Stage already set up" and tap again. Count the boxes the ball passes.

**What you should notice:** With a front door marked, the tap leads to a stage and a first screen. With none marked, the poster board has no poster to tap.

## Build the logic

### Notice

A new phone has 40 apps. Each app has its own icon on the home screen. You tap the icon of a weather app, and its main screen opens. You never saw the weather app's code run before you tapped.

### Guess

Who makes the weather screen appear? Here are three ideas.

- A. The screen starts itself when the phone is switched on.
- B. The home screen asks the system to start the app, and the system does it.
- C. The weather app has a hidden main() that Android runs.

Idea A cannot be right, because 40 apps would all open at once. Idea C feels natural, because every program so far had a main(). The Android documentation says there is no main() function. Idea B is how it works.

### Build

1. Your cast list names the screen that opens the show. It carries two marks: "this is the main entry" and "show this in the poster board".
2. The home screen is an app that reads the cast lists of all installed apps. It puts one poster on its board for each front door it finds.
3. When you tap a poster, the home screen asks the system to start that front door. This hop is my own simplified picture of what happens.
4. If your app has no stage running, the system sets one up first.
5. The system creates the screen and calls its first method. Your screen's code runs now, because the system called it.

### Name it

The home screen app is the **launcher**. An **activity** is one screen of your app. The Android documentation says the Home app fills the app launcher by finding every activity whose entry in the manifest has two marks: the action `android.intent.action.MAIN` and the category `android.intent.category.LAUNCHER`. They sit inside an **intent filter**, an entry in the manifest that says what kinds of requests a component accepts. The documentation says the two must be paired for the activity to appear in the app launcher.

A request to start something is an **intent**. In plain words, an intent is a message that asks the system to start a component (general knowledge). The documentation calls each part of an app an **entry point**: a door through which the system or a user can enter your app. It says the system starts the **process** if it is not already running. A process is one running copy of your app, kept apart from other apps. When the system creates the activity, it calls its **onCreate** method, which the documentation describes as the place for basic startup logic that happens only once for the life of the activity.

| Theatre | Android |
|---|---|
| Stage manager | The system |
| Cast list | The manifest |
| Mark "opens the show" and "put me on the poster board" | The MAIN action and the LAUNCHER category in an intent filter |
| Poster board | The launcher |
| Stage | The process |
| An actor's first line | `onCreate` |

Here is the whole idea in plain Kotlin. It is a model, not real Android. The system decides, and the screen only waits.

```kotlin
// A model in plain Kotlin, not real Android.
class Screen(val name: String) {
    fun onCreate() {
        println("onCreate runs for " + name)
    }
}

fun systemStarts(frontDoor: Screen?) {
    if (frontDoor == null) {
        println("No poster on the board")
    } else {
        println("Poster shown, user taps")
        frontDoor.onCreate()
    }
}

fun main() {
    systemStarts(Screen("WeatherHome"))
    systemStarts(null)
}
```

This prints `Poster shown, user taps`, then `onCreate runs for WeatherHome`, then `No poster on the board`. Nobody in the program calls `onCreate` directly. The function `systemStarts` plays the stage manager.

## Predict the output

```challenge
type: predict
run: false
id: what-exists-first
verified: not-run
code: |
  The ball passes these boxes in order:
    1. The user taps the poster
    2. The home screen asks the system to start the front door
    3. The system sets up a new process, if the app has none running
    4. The system creates the first screen and calls its onCreate
  Which of these is there before the first screen exists?
options:
  - id: a
    text: "Your own main() function, which starts the app"
    why: "You may have expected a start line like in earlier programs. The documentation says Android apps have no main() function."
  - id: b
    text: "The process the system sets up for the app"
    correct: true
    why: "The stage comes first. The system sets it up if no show of yours is running, and only then creates the screen on it."
  - id: c
    text: "The onCreate call, which runs before the screen is made"
    why: "You may have taken onCreate for a setup step that comes first. The system calls it as it creates the screen, so the screen has to exist."
output: |
  The process the system sets up for the app
```

```challenge
type: predict
run: false
id: delete-the-front-door-marks
verified: not-run
code: |
  A weather app has one screen. Its manifest entry carries the MAIN action and the LAUNCHER category.
  A developer deletes the intent filter that holds those two marks. The screen class stays in the code.
  What does the user see on the home screen?
options:
  - id: a
    text: "The icon is still there and opens a blank screen"
    why: "You may have thought the screen class alone is enough. The launcher builds its board from the marks in the manifest, not from your code."
  - id: b
    text: "The poster for the app is missing from the board"
    correct: true
    why: "The documentation says the two marks must be paired for the activity to appear in the app launcher. With them gone, the board has no poster."
  - id: c
    text: "Nothing changes, because the screen is still in the code"
    why: "You may have thought code decides what shows. The stage manager works from the cast list, so the list decides."
output: |
  The poster for the app is missing from the board
```

```challenge
type: predict
run: false
id: trace-the-stage-manager
verified: not-run
code: |
  class Screen(val name: String) {
      fun onCreate() {
          println("onCreate runs for " + name)
      }
  }

  fun systemStarts(frontDoor: Screen?) {
      if (frontDoor == null) {
          println("No poster on the board")
      } else {
          println("Poster shown, user taps")
          frontDoor.onCreate()
      }
  }

  fun main() {
      systemStarts(null)
      systemStarts(Screen("Maps"))
  }
options:
  - id: a
    text: "No poster on the board, Poster shown, user taps, onCreate runs for Maps"
    correct: true
    why: "The first call has no front door, so no poster. The second call has one, so the poster shows, the user taps and the system calls onCreate."
  - id: b
    text: "Poster shown, user taps, onCreate runs for Maps, No poster on the board"
    why: "You may have run the Maps call first. Look at main: it calls systemStarts(null) before it calls the Maps one."
  - id: c
    text: "No poster on the board, onCreate runs for Maps"
    why: "You may have skipped the poster line. A front door that exists always prints the poster line before onCreate."
output: |
  No poster on the board
  Poster shown, user taps
  onCreate runs for Maps
```

## Tricks to remember

- No main(): the stage manager rings the bell.
- Two marks on the cast list put a screen on the poster board.
- Your screen never walks on by itself. The system calls its onCreate.
- The stage comes first, then the screen.

## Common mistakes

- **Thinking the app runs its own start line, like a console program.** Every program before this one did, so it feels normal. Ask who calls your code. The system does, through a part of your app that the manifest names.
- **Calling onCreate yourself.** The method is part of your screen, so it feels like yours to call. The system calls it when it creates the screen. Your job is to fill it with startup work.
- **Marking every screen as the front door.** Each screen feels like it needs a way in. Only the screen that opens the show needs the MAIN and LAUNCHER marks. The documentation says an activity with no intent filter can be started only with an explicit intent, a message that names that screen directly.

## Where this shows up in Android

You install your own app and cannot find its icon. The build succeeded and the screen class exists. The fix is on the cast list. The documentation says the Home app builds the launcher from the MAIN and LAUNCHER pair, so a screen without that pair has no icon.

A second symptom is a slow first screen. The system calls `onCreate` for you, and the documentation says it is the place for basic startup logic. My own reasoning: the screen cannot be drawn until your `onCreate` code returns, so heavy work in it delays the first picture.

## Say it in an interview

**There is no main() in an Android app. What starts it?**
The documentation says each component is an entry point and Android apps have no single entry point. For a normal app, one activity carries the MAIN action and LAUNCHER category in its manifest entry, and the launcher shows it. When the user taps its icon, the system starts the app's process if needed and creates that activity (the exact tap hop is my own reasoning).

**What is Zygote?**
The memory documentation says each app process is forked from an existing process called Zygote. Zygote starts when the system boots and loads common framework code and resources. To start an app process, the system forks Zygote and then loads the app's code into the new process.

**Why fork from Zygote instead of building each process from nothing?**
The documentation gives the reason: most of the RAM pages for framework code and resources can be shared across all app processes. The trade-off is that every app process starts from the same preloaded base, so shared code is loaded once. My own reasoning: this is why starting an app is cheaper than loading all of Android again.

**What goes wrong if the first screen does heavy work in onCreate?**
The screen is drawn by the main thread, the one worker that also runs your onCreate code. The documentation says no drawing events can be dispatched while that thread is blocked. So the user sees a blank or frozen start (my own reasoning). Keep onCreate light, and move slow work elsewhere.

## Recap

1. Android apps have no main(), so the system decides which part of your app to start.
2. The manifest marks one screen with MAIN and LAUNCHER, and the launcher shows its icon.
3. After a tap, the system sets up a process if needed and calls the screen's onCreate.

## You can now

- Predict which step exists before the first screen: the process, not main().
- Predict what the home screen shows when the MAIN and LAUNCHER marks are deleted.
- Trace a small model in which the system, not your code, calls a screen's first method.
