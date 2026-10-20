---
id: foundation/how-an-app-is-born/run-your-first-app
title: How do I run my own first app on a real phone?
status: shadow
difficulty: easy
order: 34
requires: [foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon]
summary: Make a new project in Android Studio, switch on USB debugging on your phone, plug it in and press Run.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: developer.android.com/studio/intro (Android Studio is the official IDE for Android app development); developer.android.com/studio/projects/create-project (New Project screen; Empty Activity is the recommended starting point for Compose projects; Name, Package name, Save location, Minimum API level; Finish creates the project with some basic code and resources); developer.android.com/studio/debug/dev-options (tap Build Number seven times to enable Developer options; Build number sits under Settings, About phone on a Pixel and the location varies by maker; USB debugging lets the device communicate with the development machine through adb; menu path on Android 9 and higher is Settings, System, Advanced, Developer Options, USB debugging; Wireless debugging); developer.android.com/studio/run/device (plugged in over USB, click Run to build and run your app on the device; the RSA key dialog and unlocking the device; Troubleshoot Device Connections and Restart ADB server; Windows USB driver; wireless debugging on Android 11 and higher; adb devices); developer.android.com/studio/run (select the app in the run configurations menu, the device in the target device menu, click Run; the debug version is built by default and is for development only; no device means create an Android Virtual Device or connect a physical device). Own reasoning: no store or account step appears on the device page; the analogy. No code, no output. -->
## The question

You have read how an app is built, installed and started. Now you want to see your own app on your own phone. Do you need a store, a paid account, or something else? And what must you switch on first?

## Mental model

Think of planting your first seed and watching the first sprout break the soil.

- The seed is a new project, the starting pack of files for an app.
- The garden bed is your phone.
- The permission slip is a hidden setting on the phone that says "this computer may plant here".
- The path to the garden is a cable, or a wireless link.
- The watering can is the Run button.
- The sprout is your app's first screen on the phone.

You plant the seed on the laptop, get the permission slip signed on the phone, walk the path, and water. Then the sprout appears.

**Where the analogy stops:** A garden bed does not ask for permission. A phone does, and it asks twice: first you switch the setting on, and then you approve this computer. Also, a seed grows by itself. Here the building, installing and starting come from the steps you met in the earlier lessons, once you press Run.

## Build the logic

### Notice

Maya has a laptop and a phone. Her goal is one thing: her first app, open on her phone. Some of the work belongs on the laptop, some on the phone, and some is the link between them.

### Guess

How does a phone agree to take an app from a computer? Here are three ideas.

- A. It takes any app from any computer, at any time.
- B. The app must first be sealed and sent through the store.
- C. You switch on a hidden setting, and the phone asks you to approve this computer.

Idea A would be unsafe. Idea B is how apps reach other people, as in the install lesson. Your own test copy takes a shorter way. Idea C is the answer.

### Build

1. Install Android Studio on the laptop. The Android documentation calls it the official tool for Android app development.
2. Choose New Project, then the Empty Activity template. This is the seed.
3. Fill in the Name, the Package name, the Save location and the Minimum API level, and press Finish. Android Studio makes a project with some basic code and resources.
4. On the phone, make the hidden settings appear. Tap Build number seven times. On a Pixel it sits under Settings, then About phone.
5. In the hidden settings, switch on USB debugging.
6. Plug the phone into the laptop. The phone may ask whether to allow debugging from this computer. Unlock the phone and approve.
7. In Android Studio, pick your app in the run configurations menu and pick your phone in the target device menu.
8. Press Run.
9. The sprout appears: your app opens on the phone.

### Name it

**Android Studio** is the official **IDE**, which stands for Integrated Development Environment. An IDE is a program for writing code, and for building and running it. A **template** is a ready-made starting project. **Empty Activity** is the template the documentation recommends for new Compose projects. Compose is the toolkit for building Android screens in Kotlin.

**Developer options** is a hidden screen of settings on the phone. The documentation says you tap the Build number option seven times to turn it on. It adds that the screen might be located or named differently on some devices.

**USB debugging** is a setting in Developer options. The documentation says it lets your phone communicate with your computer through **adb**, which stands for Android Debug Bridge. When you plug in, the system shows a dialog asking whether to accept a key that allows debugging through this computer. The documentation says debugging commands cannot run unless you can unlock the device and acknowledge the dialog.

The documentation says Android 11 and higher also supports wireless debugging, over Wi-Fi.

| Seed to sprout | In real life |
|---|---|
| The seed | New Project with the Empty Activity template |
| The garden bed | Your phone |
| The permission slip | Developer options, USB debugging, and the approval dialog |
| The path | A USB cable, or Wi-Fi pairing |
| The watering can | The Run button, with your phone chosen as the target |
| The sprout | Your app open on the phone |

My own reading of the pages: nothing in these steps mentions a store listing or a paid account.

## Predict the output

```challenge
type: predict
run: false
id: setting-before-run
verified: not-run
code: |
  Maya has made a new project and plugged her phone into the laptop.
  The phone will not take the app from her computer.
  Which setting must be on, in Developer options?
options:
  - id: a
    text: "USB debugging"
    correct: true
    why: "This is the permission slip. The documentation says it lets your phone communicate with your computer through adb."
  - id: b
    text: "Battery saver"
    why: "You may have thought the phone needs to stay awake. Saving power is not the permission the phone is waiting for."
  - id: c
    text: "Do not disturb"
    why: "You may have thought the phone blocks anything noisy. That setting is about alerts, not about accepting an app from a computer."
output: "USB debugging"
```

```challenge
type: predict
run: false
id: phone-not-in-the-list
verified: not-run
code: |
  Maya plugged in her phone, and USB debugging is on.
  Android Studio shows no phone.
  Her phone is locked, with a question on the screen
  about allowing debugging from this computer.
  What is the best next step?
options:
  - id: a
    text: "Reinstall Android Studio"
    why: "You may have thought the tool is broken. The phone is still waiting for an answer, so nothing on the laptop can fix it."
  - id: b
    text: "Publish the app to a store first"
    why: "You may have thought the store is the only door. The device page does not list a store step, and the phone is waiting on a dialog."
  - id: c
    text: "Unlock the phone and accept the question"
    correct: true
    why: "The documentation says debugging commands cannot run unless you can unlock the device and acknowledge the dialog. If the phone still does not appear, the documentation names the Troubleshoot Device Connections assistant."
output: "Unlock the phone and accept the question"
```

```challenge
type: predict
run: false
id: store-before-test
verified: not-run
code: |
  Ravi has made a project today.
  A friend says he cannot see it on his phone
  until the app is published to a store.
  Is the friend right?
options:
  - id: a
    text: "Yes, every app must go through a store before it can be on a phone"
    why: "You may have thought the install lesson is the only road. That road is for other people. Your own test copy is put on the phone straight from the laptop."
  - id: b
    text: "No, he can switch on USB debugging, connect the phone and press Run"
    correct: true
    why: "The device page lists switching on USB debugging, plugging in and pressing Run, with no store step. This is my own reading of the page."
  - id: c
    text: "Yes, but only with a paid account"
    why: "You may have thought a fee gates the first run. The device page lists no account or fee in its steps."
output: "No, he can switch on USB debugging, connect the phone and press Run"
```

## Tricks to remember

- Seed, bed, slip, path, can, sprout.
- Tap Build number seven times to find the hidden settings.
- The phone asks twice: switch it on, then approve this computer.
- Phone locked, no phone in the list. Unlock first.

## Common mistakes

- **Thinking you must publish to a store first.** Every app you install seems to come from one, so this feels like the only road. The device page lists USB debugging, a cable and Run, and no store step. Try the short road first.
- **Not finding Developer options.** The setting is hidden, so it feels missing. The documentation says to tap Build number seven times, and that the place can differ by device. Search the settings for "Build number".
- **Plugging in a locked phone and waiting.** The laptop seems to be doing nothing, so the cause feels like the laptop. The phone is asking a question. Unlock it and answer.
- **Forgetting the driver on Windows.** The cable fits, so it feels done. The documentation says to install a USB driver for ADB on Windows, if applicable.

## Where this shows up in Android

You plug in your phone, and Android Studio's device list stays empty. That is the symptom of steps 5 and 6. The documentation says the Troubleshoot Device Connections assistant gives step-by-step instructions, and that a Restart ADB server button is there if the phone still does not appear. It also says that running `adb devices` lists the device when it is connected.

A second symptom. The app opens on your phone, but it is a development copy. The documentation says Android Studio builds the debug version by default, and that this version is for use only during development. Sealing it for other people is the job of the signing step from the earlier lessons.

## Recap

1. Make a project, switch on USB debugging on your phone, connect it and press Run.
2. The phone asks you to approve the computer, so unlock it and answer.
3. The first run needs no store and no paid account in the steps the documentation lists.

## You can now

- Name the phone setting that must be on before it accepts an app from your computer.
- Choose the next step when the phone is plugged in but missing from Android Studio.
- Explain why you can run your own app on your phone without publishing it.
