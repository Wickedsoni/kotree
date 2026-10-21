---
id: android-core/activities/what-is-an-activity
title: "What is the Activity that opens when my app starts?"
status: shadow
difficulty: easy
order: 3
requires: [android-core/what-is-an-app/who-starts-your-app]
summary: "An Activity is the window the system gives your app. One line inside it, setContent, chooses what the user sees."
video: ""
publishedOn:
updatedOn:
sims: [picture-frame-hook]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (read 21 Oct 2026):
- https://developer.android.com/guide/components/activities/intro-activities (activity = window for the UI, entry point, callbacks instead of main, onCreate fires when the system creates the activity, next callback is onStart, setContent in Compose, one activity in modern Compose apps, main activity, MyActivity sample)
- https://developer.android.com/guide/topics/resources/runtime-changes (the system destroys the activity and creates a new instance on a configuration change; state kept as fields is cleared)
-->

## The question

You open a weather app. A screen with today's forecast appears. You never wrote a line that says "show the screen now", yet something puts it up. What opens first when your app starts, and which line of your code decides what you see?

## Mental model

Think of a picture frame hung on a wall.

- The wall is the phone's window, the space where your app can draw.
- The frame is given to you. You do not build it and you do not hang it.
- The person who hangs the frame is the Android system. They hang it when your app starts.
- The picture is what the user sees.
- Choosing the picture is one line in your code. You can only do it after the frame is on the wall.

**Where the analogy stops:** A real frame holds one picture for good. Your frame can show one picture after another, so one frame can serve a whole app. Also, the system can take the frame down and hang a new one, for example when the user turns the phone sideways. Then your picture has to be chosen again.

## See it move

```sim
id: picture-frame-hook
template: stepper
title: A frame on the wall and the line that picks the picture
intro: "The wall is bare. The system is about to hang a frame. Your code has five lines and none has run yet."
code: true
lines:
  - { id: l1, text: "class MainActivity : ComponentActivity() {" }
  - { id: l2, text: "    override fun onCreate(savedInstanceState: Bundle?) {" }
  - { id: l3, text: "        super.onCreate(savedInstanceState)" }
  - { id: l4, text: "        setContent { Text(\"Forecast\") }" }
  - { id: l5, text: "    }" }
board:
  - { var: frame, label: "Frame" }
  - { var: picture, label: "Picture in the frame" }
log: { columns: [Step, Line that ran, The frame now holds] }
vars: { pc: 1, step: 0, frame: "No frame yet", picture: "Empty frame", skip: "off" }
toggles:
  - { id: skip, label: "Leave out the setContent line", var: skip }
actions:
  - id: run
    label: "Run the next line"
    cases:
      - when: { var: { pc: 1 } }
        do:
          - add: { step: 1 }
          - set: { pc: 2, frame: "Hung on the wall" }
          - light: [ l2 ]
          - row: [ "{step}", "onCreate starts", "{picture}" ]
          - say: "The system hangs the frame on the wall and calls onCreate on it. You did not call it yourself. The frame is empty."
      - when: { var: { pc: 2 } }
        do:
          - add: { step: 1 }
          - set: { pc: 3 }
          - light: [ l3 ]
          - row: [ "{step}", "super.onCreate", "{picture}" ]
          - say: "The first line passes the call up to the ready-made frame code, so its standard setup runs. The frame is still empty."
      - when: { var: { pc: 3, skip: "on" } }
        do:
          - add: { step: 1 }
          - set: { pc: 4 }
          - light: [ l5 ]
          - row: [ "{step}", "no setContent line", "{picture}" ]
          - say: "You left out the setContent line. Nothing chooses a picture, so the frame stays empty."
      - when: { var: { pc: 3 } }
        do:
          - add: { step: 1 }
          - set: { pc: 4, picture: "Forecast" }
          - light: [ l4 ]
          - row: [ "{step}", "setContent", "{picture}" ]
          - say: "The setContent line chooses the picture. The forecast now hangs in the frame."
      - when: { var: { pc: 4, skip: "on" } }
        do:
          - add: { step: 1 }
          - set: { pc: 5 }
          - light: [ l5 ]
          - row: [ "{step}", "end of onCreate", "{picture}" ]
          - say: "The method ends with an empty frame, so the user sees an empty screen."
      - when: { var: { pc: 4 } }
        do:
          - add: { step: 1 }
          - set: { pc: 5 }
          - light: [ l5 ]
          - row: [ "{step}", "end of onCreate", "{picture}" ]
          - say: "The method ends and the forecast stays in the frame. The next call the system makes is onStart."
      - when: { var: { pc: 5 } }
        do:
          - say: "All the lines have run. Switch the setContent line off or on, or press Reset."
script: [ run, run, run, run, "skip=on" ]
try:
  - text: "Press Step four times, or press \"Run the next line\" four times. The frame is on the wall first, and the picture appears only at the setContent line."
    done: [ run, run, run, run ]
  - text: "Switch on \"Leave out the setContent line\". The steps replay, and the frame stays empty."
    done: [ "skip=on" ]
```

**What you should notice:** The frame is on the wall before your code runs, and the picture appears only at the setContent line.

## Build the logic

### Notice

The weather app has one forecast screen. A user taps the icon. Before any forecast can show, two jobs must be done. Something must give the app a window to draw in. Then someone must say what goes in that window.

### Guess

Who does each job? Here are three ideas.

- A. Your code does both: it makes the window and fills it.
- B. The system gives the window and calls your code. Your code fills it.
- C. The system does both jobs and your code has no say.

Idea A cannot work, because no line of yours runs first. Idea C would make every app show the same thing. Idea B is how it works.

### Build

1. The system starts your app and creates the frame. It hangs the frame on the wall.
2. The system calls one method on the frame, a "you are hung" call. You never call it yourself.
3. In that method, your code first lets the ready-made frame do its standard setup.
4. Then your code says which picture goes in the frame.
5. When the method ends, the system carries on with its next steps. If step 4 is missing, the frame stays empty.
6. One frame can show many pictures one after another. So an app with three screens does not need three frames.

### Name it

An **Activity** is the class the system creates to give your app a window to draw in. The Android documentation says it is the **entry point** for the app's interaction with the user. An entry point is a door the system can use to come into your app. It also says Android starts code in an Activity by calling callback methods, not by calling a `main` method. A **callback** is a method that the system calls for you at a set moment.

The callback in step 2 is **`onCreate`**. The documentation says it fires when the system creates your activity. The documentation's own sample extends a class named **`ComponentActivity`** and calls **`setContent`** inside `onCreate`. For a Compose app, it says to use this callback to set up your host composable with `setContent`. A **composable** is a function that describes a piece of the screen. The picture in the frame is made of composables.

| Picture frame | Android | In the code |
|---|---|---|
| The person who hangs the frame | The system | Calls `onCreate` for you |
| The frame | The activity | `class MainActivity : ComponentActivity()` |
| Standard setup | The parent class | `super.onCreate(...)` |
| Choosing the picture | Setting the content | `setContent { ... }` |

Here is the shape the documentation shows. It needs the Android libraries, so it cannot run on this page.

```kotlin
// does not compile
// Needs the Android libraries, so it cannot run on this page.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(text = "Forecast")
        }
    }
}
```

The project template in your Android Studio may add more lines to this class. My own reasoning: the shape above is the part that matters for this page.

The documentation also says that in a modern Compose app, one activity is the only activity necessary. It hosts the screens as composables, and the app does not need one activity per screen. The first screen to appear is the main activity. A file called the **manifest** lists the activity, using an `<activity>` element.

Here is the same idea in plain Kotlin. It is a model, not real Android. The `main` function plays the system.

```kotlin
// A model in plain Kotlin, not real Android.
open class Frame {
    open fun onCreate() {
        println("Frame: standard setup")
    }

    fun setContent(picture: String) {
        println("Picture in the frame: " + picture)
    }
}

class MainFrame : Frame() {
    override fun onCreate() {
        super.onCreate()
        setContent("Forecast")
    }
}

fun main() {
    val frame = MainFrame()
    frame.onCreate()
}
```

This prints `Frame: standard setup`, then `Picture in the frame: Forecast`. Nothing in `MainFrame` calls its own `onCreate`. The `main` function does it, as the system does for an activity.

## Predict the output

```challenge
type: predict
run: false
id: which-line-sets-the-screen
verified: not-run
code: |
  Read this class. The line numbers are not part of the code.
   1  class MainActivity : ComponentActivity() {
   2      override fun onCreate(savedInstanceState: Bundle?) {
   3          super.onCreate(savedInstanceState)
   4          val title = "Forecast"
   5          setContent {
   6              Text(text = title)
   7          }
   8          println("onCreate is done")
   9      }
  10  }
  Which line puts the content into the frame?
options:
  - id: a
    text: "Line 3"
    why: "You may have thought the first line in onCreate builds the screen. Line 3 only lets the ready-made frame do its standard setup."
  - id: b
    text: "Line 5"
    correct: true
    why: "Choosing the picture is the setContent call. Line 6 describes the picture, and line 5 hangs it in the frame."
  - id: c
    text: "Line 6"
    why: "You may have picked the line that holds the text. It describes the picture, but only the setContent call on line 5 puts it into the frame."
output: |
  Line 5
```

```challenge
type: predict
run: false
id: three-screens-how-many-frames
verified: not-run
code: |
  A weather app has three screens: forecast, map and settings.
  It is a modern Compose app.
  How many activities does it need, following the Android documentation?
options:
  - id: a
    text: "One, and the three screens are swapped inside it"
    correct: true
    why: "The documentation says one activity is the only activity necessary in a modern Compose app. The frame stays and the pictures change."
  - id: b
    text: "Three, one for each screen"
    why: "You may have matched one frame to one picture. That was how older apps were often built. Today one frame can show many pictures."
  - id: c
    text: "None, because an Android app has no main"
    why: "You may have remembered that no main starts an app. The system still needs a frame, and the activity is that frame."
output: |
  One, and the three screens are swapped inside it
```

## Tricks to remember

- Frame first, picture second. You never call `onCreate`: the system calls it for you.
- No `setContent`, no picture. One frame, many pictures: a modern app needs one activity.
- Task, in Android Studio: open the `MainActivity` of a new Compose project and find the `setContent` call inside `onCreate`.

## Common mistakes

- **Thinking every screen needs its own Activity.** The word "screen" and many older tutorials push this idea. The Android documentation says a modern Compose app needs one activity, which hosts the screens as composables.
- **Thinking the Activity is the screen's content.** Tutorials put the content code right next to the activity, so the two blur together. The activity gives the window. The content inside it is made of composables.
- **Thinking `onCreate` runs once per app start.** The name sounds like a one-time event. The Android documentation says the system recreates an activity on a configuration change such as turning the phone. It destroys the old instance and creates a new one, so `onCreate` runs again.

## Where this shows up in Android

You turn a phone sideways and a form you were typing loses what you typed. The frame was taken down and a new one was hung, and the documentation says this clears any state kept as fields in the activity. This page explains why the frame is rebuilt. Where to keep the typed text safe is a different question.

A different symptom: an app opens to an empty screen. One cause is a frame with no picture, which is what happens when `setContent` is never called. That cause is my own reasoning from the model on this page, not a documentation sentence.

## Say it in an interview

**What is an Activity, and what is its job?**
An Activity is the class that gives your app the window it draws its UI in. The Android documentation calls it the entry point for the app's interaction with the user. The system starts code in it by calling callbacks such as `onCreate`, not a `main` method.

**How does a Compose UI get onto the screen from an Activity?**
You call `setContent` inside `onCreate`. The documentation says to use that callback to set up your host composable with `setContent`. The Activity gives the window, and the composables inside it draw the screen.

**Why do modern apps use one Activity instead of one per screen?**
The documentation says a modern Compose app needs only one activity, with the screens hosted as composables and navigation destinations swapped inside it. My own reasoning: one window owner means fewer pieces to start and a simpler back and forth between screens. The trade-off is that other apps can still enter yours through more than one door, so some apps keep extra activities.

**What goes wrong if you keep business data and network state as fields in the Activity?**
The documentation says the system recreates the activity on a configuration change and clears any state kept as fields. So the data and the loading state disappear when the user turns the phone. My own reasoning: keep that state in a longer-lived holder, which is the job of a ViewModel.

## Recap

1. An Activity is the window the system gives your app, and the system calls its `onCreate`.
2. Inside `onCreate`, the `setContent` call chooses what the user sees.
3. A modern Compose app needs one Activity and swaps its screens inside it.

## You can now

- Point to the line in a short MainActivity that puts the content on the screen.
- Predict how many Activities a modern three-screen Compose app needs.
- Explain, using the picture frame, why a screen stays empty when `setContent` is left out.
