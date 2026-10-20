---
id: foundation/from-code-to-running/what-is-art
title: How does an Android phone run Kotlin code?
status: shadow
difficulty: medium
order: 29
requires: [foundation/from-code-to-running/what-is-the-jvm, foundation/how-computers-work/what-is-an-os]
summary: A phone does not run Kotlin as you wrote it. The code is rewritten into a compact form that the phone's own program runs.
video: ""
publishedOn:
updatedOn:
sims: [phone-kitchen-two-ways]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 20 Oct 2026): developer.android.com/guide/platform (each app runs in its own process with its own instance of ART; ART executes DEX files, a bytecode format designed for Android with a minimal memory footprint; AOT and JIT compilation; build tools such as d8 produce DEX); developer.android.com/topic/performance/baselineprofiles/overview (without a profile, code is JIT-compiled in memory after being interpreted, or written to an odex file in the background when the device is idle; after install or update the first runs are suboptimal; the version table); developer.android.com/build/jdks (the JVM is not used on Android devices or emulators; bytecode is generated for Java and Kotlin source); developer.android.com/tools/d8 (d8 compiles Java bytecode into DEX bytecode); developer.android.com/guide/components/fundamentals (each process has its own VM, so app code runs in isolation). Not reached: source.android.com. Own reasoning is labelled in the text. -->

## The question

You write Kotlin on a laptop. A few minutes later the same app opens on your phone. Yet a phone is small, and its insides are not the same as a laptop's. Who on the phone reads your code and carries it out? Is it the same helper a laptop uses?

## Mental model

Think of one recipe cooked in two kinds of kitchen.

- The recipe is your Kotlin code, written the way you like to read it.
- The standard cards are the first rewrite of the recipe. Big kitchens everywhere can cook from them.
- The big kitchen is the kind you find on a laptop. It has its own cooks, and they read standard cards.
- The phone kitchen is small, with little counter space. It has its own cooks, and they read a different, more compact card.
- The clerk is the person who rewrites each standard card into a compact card before the box goes to the phone.
- The head cook is the one who runs a phone kitchen station. Every app gets its own station and its own head cook.
- The cooks have three habits. They can read a card line by line while cooking. They can jot a quick-prep sheet for a step that keeps coming back. Or they can prepare sheets before the step is needed.
- The quiet hour is a time when nobody is ordering, so the kitchen can prepare sheets ahead.

**Where the analogy stops:** The cooks are programs, not people, and nobody can ask them to work differently. Android decides how they work, and its rules changed between versions. Also, a recipe is cooked once per order. A running app keeps going until you leave it.

## See it move

<!-- SIM: phone-kitchen-two-ways -->

**Try this:**
1. Press "Cook" and watch which cooks carry out the compact cards on the phone.
2. Flip the toggle to "prepare ahead" and press "Cook" again. Then flip it to "prepare while cooking". Watch when the quick-prep sheets get made.

**What you should notice:** The phone's own cooks carry out the compact cards, and the toggle only changes when the quick-prep sheets are made.

## Build the logic

### Notice

Your recipe is for soup. The big kitchen's cooks read standard cards. The phone kitchen is small, so its cooks read compact cards. It is the same soup with a different card style.

| Step | What exists | Where it happens |
|---|---|---|
| 1 | Your recipe | Your laptop |
| 2 | Standard cards | Your laptop |
| 3 | Compact cards | Your laptop |
| 4 | Cooks cook from the compact cards | The phone |

Steps 1 to 3 finish before the phone is involved. Step 4 happens on the phone, with the phone's own cooks.

### Guess

The standard cards are ready. Can the phone's cooks use them as they are? Here are three ideas.

- A. Yes. A phone is a small laptop, so the same cooks work there.
- B. No. The cards must first be rewritten into the compact style that the phone's cooks read.
- C. The phone's cooks go back and read the original recipe.

Idea A treats the phone as a small laptop, but its cooks are different. Idea C needs the recipe on the phone, but what travels to the phone is the finished set of cards. Idea B is the answer.

### Build

1. Rewrite the recipe into standard cards.
2. The clerk rewrites the standard cards into compact cards.
3. The compact cards travel to the phone in a box.
4. On the phone, each app gets its own station and its own head cook.
5. At first, the head cook reads a compact card line by line while cooking.
6. When a step keeps coming back, the head cook jots a quick-prep sheet and uses it next time.
7. In the quiet hour, the kitchen can prepare sheets ahead for steps it expects to need.

### Name it

Step 1 produces **bytecode**: a compact list of instructions that is not tied to one exact CPU. The Android documentation says bytecode is generated for both Java and Kotlin source code. On a laptop, the program that runs bytecode is the **JVM**, short for Java Virtual Machine. The same documentation says the JVM is not used on Android devices or emulators.

Step 2 is done by a tool called **d8**. To **compile** is to translate code into a form that runs. The documentation says d8 compiles Java bytecode into DEX bytecode. **DEX** is short for Dalvik Executable format. The documentation calls it a bytecode format designed specifically for Android and optimized for a minimal memory footprint.

Step 4 is the **Android Runtime (ART)**. ART runs an app by executing DEX files. The documentation says that on Android 5.0 (API level 21) and higher, each app runs in its own **process**, which is a running app together with the memory it was given, and with its own instance of ART.

Steps 5 to 7 are three ways of running a step. To be **interpreted** means to be read and carried out line by line, with no preparation first. In **just-in-time (JIT) compilation**, a step is translated while the app is running. In **ahead-of-time (AOT) compilation**, a step is translated before it is needed. The documentation says that without a profile, code is either JIT-compiled in memory after being interpreted, or written to a file in the background when the device is idle.

| Kitchen | Official name | What it does on Android |
|---|---|---|
| Recipe | Kotlin source code | What you write |
| Standard cards | Bytecode | The first rewrite, made for the JVM kind of kitchen |
| Clerk | d8 | Rewrites bytecode into DEX |
| Compact cards | DEX | The form that goes to the phone |
| Phone kitchen's cooks | ART | Run the DEX on the phone |
| Read as they cook | Interpreted | No preparation first |
| Jot a sheet while cooking | JIT compilation | Prepare a step while the app runs |
| Prepare in the quiet hour | AOT compilation | Prepare a step before it is needed |

The documentation also shows that the rules changed. On Android 5 and 6 the whole app was optimized during install, which made long waits. From Android 7 the phone prepares only part of the app ahead, and it can add more when the device is idle. That is as deep as this page goes.

## Predict the output

Follow the boxes on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: who-cooks-on-the-phone
verified: not-run
code: |
  Laptop side:  Kotlin recipe -> standard cards -> clerk -> compact cards
  The box with the compact cards is sent to the phone.
  Phone side:   ??? cooks from the compact cards

  Which cooks work on the phone?
options:
  - id: a
    text: "The big kitchen's cooks (the JVM)"
    why: "You may have expected the laptop's cooks, because the standard cards are made for them. The Android documentation says the JVM is not used on Android devices or emulators."
  - id: b
    text: "The phone's own cooks (ART)"
    correct: true
    why: "The compact cards are DEX, and the Android documentation says ART runs apps by executing DEX files. The phone has its own kitchen with its own cooks."
  - id: c
    text: "The clerk, who rewrites the cards again at every tap"
    why: "You may have thought the rewriting happens on the phone each time. The clerk finished before the box was sent."
output: "The phone's own cooks (ART)"
```

```challenge
type: predict
run: false
id: prepare-ahead-or-while-cooking
verified: not-run
code: |
  Moment 1: A cook reads a card line by line and cooks as they read.
  Moment 2: A cook sees the same step for the fifth time and jots a quick-prep sheet.
  Moment 3: In the quiet hour, the kitchen prepares sheets for steps it expects to need.

  Which moment is preparing ahead of time?
options:
  - id: a
    text: "Moment 1"
    why: "You may have counted any early work. Reading as you cook prepares nothing, so it is called interpreting."
  - id: b
    text: "Moment 2"
    why: "You may have counted the sheet. It is made while cooking, which is JIT compilation, not ahead of time."
  - id: c
    text: "Moment 3"
    correct: true
    why: "The sheets are made before the steps are needed. The Android documentation says prepared code can be written in the background while the device is idle."
output: "Moment 3"
```

```challenge
type: predict
run: false
id: first-open-after-install
verified: not-run
code: |
  Maya installs a new app. No sheets are ready yet.
  She opens it, uses it, and leaves it alone.
  Later the phone is idle, and the kitchen prepares more sheets.
  She opens the app again.

  Which sentence fits the Android documentation?
options:
  - id: a
    text: "The first open can feel slower than a later one, because less is prepared at first"
    correct: true
    why: "At first the cooks have few sheets, so more of the work is reading line by line. The documentation says users have a suboptimal experience from the first run after an install or update until new code paths are optimized."
  - id: b
    text: "Both opens feel the same, because the cards never change"
    why: "You may have counted only the cards. The cards stay the same, but how much of them is prepared changes."
  - id: c
    text: "The second open is slower, because preparing ahead uses up the app"
    why: "You may have thought preparing makes things worse. Preparing aims to make the same steps run faster."
output: "The first open can feel slower than a later one, because less is prepared at first"
```

## Tricks to remember

- Standard cards for the big kitchen. Compact cards for the phone kitchen.
- The clerk works before the box ships. The phone's cooks work after it arrives.
- Read, jot, prepare ahead: interpreted, JIT, AOT.
- JVM cooks are laptop cooks. ART cooks are phone cooks.

## Common mistakes

- **Thinking the phone runs the JVM.** The JVM was the cook you met most recently, so it feels like the cook in every kitchen. The Android documentation says the JVM is not used on Android devices. Picture ART as the phone's own head cook.
- **Thinking my Kotlin text is on the phone.** You typed it, so it feels like the thing that ships. What reaches the phone is the compact cards. Ask which card style is in the box.
- **Thinking all the preparing happens once, on my laptop.** One Build button feels like one step. Some preparing happens on the phone, at install time or in the quiet hour, as the documentation describes.
- **Reading a slow first open as broken code.** A slow start feels like a bug. The same cards can run slower before the steps are prepared. Check whether it happens only the first time.

## Where this shows up in Android

A new app feels slow the first time you open it after an install or an update. Later opens feel smoother. The Android documentation says that after installing or updating an app, users have a suboptimal experience from the first time they run it until new code paths are optimized. That is the moment when few quick-prep sheets are ready.

App makers can ship a **Baseline Profile**, which is a list of important code paths that ART can prepare ahead with AOT compilation. The documentation says this avoids the interpreting and JIT steps for the listed paths.

Here is a second symptom. One app stops working, and the others on your phone carry on. The documentation says each app runs in its own process with its own instance of ART. My own reasoning: separate head cooks are why one app's trouble does not usually stop the rest.

## Recap

1. Your Kotlin becomes bytecode, and then DEX, before it reaches an Android phone.
2. The phone's own runtime, ART, runs the DEX. The JVM is not used.
3. ART reads as it goes, prepares while running, and prepares ahead.

## You can now

- Name the program that runs an app on an Android phone, and the one it does not use.
- Sort a described moment as interpreted, JIT or AOT.
- Predict why an app can feel slower the first time it opens after an install.
