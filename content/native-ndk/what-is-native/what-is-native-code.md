---
id: native-ndk/what-is-native/what-is-native-code
title: What is native code inside an Android app?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Native code is code already turned into instructions for one kind of CPU. In an Android app it usually sits in a file that ends in .so.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: Assumes foundation/how-computers-work/what-is-a-cpu, foundation/from-code-to-running/what-is-a-compiler and foundation/from-code-to-running/what-is-the-jvm (none drafted yet). Facts checked 21 Oct 2026: developer.android.com/ndk/guides (NDK = tools to use C and C++ with Android; use it for low latency or heavy compute, or to reuse C/C++ libraries; "may not be appropriate for most novice Android programmers"; Gradle packages the native library, the SO file, into the APK), developer.android.com/ndk/guides/abis (each CPU and instruction set combination has its own ABI; ABIs armeabi-v7a, arm64-v8a, x86, x86_64; libraries live at /lib/<abi>/lib<name>.so in the APK; install unpacks the most appropriate machine code; "If there is no shared-object file at all, the application builds and installs, but crashes at runtime."), source.android.com/docs/core/runtime (ART runs DEX bytecode). NOT claimed from an official page: that Kotlin source compiles to DEX (the runtime page does not say it; checked on the Android build docs it is widely stated but not re-read here). Rust is named once, as roadmap says; no Rust claim from an official page. Roadmap row says no sim: one labelled diagram of an app package; that diagram asset does not exist yet. The Predict file lists are an invented teaching example, not a real app. Predict items traced by hand only. -->
## The question

You open an Android project and find a folder named `cpp`, or a file that ends in `.so`. Your Kotlin files are all in another folder. Nobody told you what this other thing is.

What is the code in that file, and how is it different from the Kotlin code you write?

## Mental model

Think of a kitchen that buys a ready-made sauce from a specialist factory.

- The kitchen is your app.
- Your recipe cards are your Kotlin code. They are read and cooked fresh, step by step, when the app runs.
- The factory is the place where C or C++ code is turned into finished machine instructions.
- The sauce bottle is the native library, the `.so` file. It arrives already cooked.
- The stove type is the kind of CPU. A bottle cooked for one stove type does not work on another.

Your kitchen opens the bottle and pours it in. It does not cook the sauce again.

**Where the analogy stops:** A sauce bottle is a thing you eat. A native library is more code that your app calls and trusts, and a mistake inside it can crash your app. Also, a good sauce does not make every dish better. Native code is not faster at everything, only in some jobs.

## Build the logic

### Notice

Open an Android app package, the file you install, and look inside. You find your compiled app code, the manifest that describes the app, and your pictures and layouts.

Sometimes there is also a folder named `lib`. Inside it are folders named after CPU kinds, like `arm64-v8a` and `x86_64`, and in each of them, a file ending in `.so`.

### Guess

What is in those `.so` files?

- A. Pictures for different screen sizes.
- B. The same Kotlin code, but faster.
- C. Code that was already turned into instructions for one kind of CPU.

Idea A sounds sensible because the folders look like screen sizes, but they are named after CPUs. Idea B treats native code as a speed-up switch. It is a different kind of file, not a faster version of your Kotlin. Idea C is the sauce bottle.

### Build

1. A developer writes a part of the app in a language like C or C++.
2. A compiler, the factory, turns that code into instructions for one kind of CPU. It does this before the app is built.
3. The result is one file per kind of CPU, because a bottle cooked for one stove does not fit another.
4. The app package carries all of these files in the `lib` folder.
5. When the app is installed, the phone keeps the one that matches its own CPU.
6. Your app opens the bottle and calls what is inside.

If the bottle is missing, your recipe cards still look fine, but the dish that needs the sauce cannot be made.

### Name it

- **Native code** is code that was compiled ahead of time into instructions for one kind of CPU, so the CPU can run it directly.
- A **native library** is a bundle of native code that your app can call. On Android it is a file ending in **`.so`**.
- The **NDK**, short for Native Development Kit, is the set of tools Android gives you to use C and C++ in an app.
- An **ABI** is the name for one combination of CPU and instruction set, for example `arm64-v8a`. The `.so` files are stored in a folder named after it.

| Kitchen | Official name | In the app package |
|---|---|---|
| Your recipe cards | Your Kotlin code | Your compiled app code |
| The factory | The C or C++ compiler | The NDK build |
| The sauce bottle | Native library | `lib/arm64-v8a/libaudio.so` |
| The stove type | ABI | `arm64-v8a`, `x86_64` |

C and C++ are the languages the Android documentation describes for native code. Rust is another language that compiles to native code, and it is named only so you recognise the word.

There is no program to run on this page. This lesson reads a package like a map.

## Predict the output

```challenge
type: predict
run: false
id: which-files-are-native
verified: not-run
code: |
  An app package holds these five files:
    1. classes.dex                      (the compiled app code)
    2. AndroidManifest.xml              (describes the app)
    3. lib/arm64-v8a/libaudio.so
    4. lib/x86_64/libaudio.so
    5. res/layout/main.xml              (a screen layout)
  Which files are native libraries?
options:
  - id: a
    text: "Files 1 and 3"
    why: "You may have thought the compiled app code is also a sauce bottle. It is a recipe card: the phone reads and runs it when the app runs. Only the .so files are bottles."
  - id: b
    text: "Files 3 and 4"
    correct: true
    why: "Both end in .so. They are the same sauce cooked for two stove types, one for arm64-v8a CPUs and one for x86_64 CPUs."
  - id: c
    text: "All five files"
    why: "You may have thought everything in the package is native. A manifest and a layout are plain description files. Only the bottles carry ready-made CPU instructions."
output: "Files 3 and 4"
```

```challenge
type: predict
run: false
id: remove-the-bottle
verified: not-run
code: |
  The audio feature of the app calls into libaudio.so.
  You delete both libaudio.so files from the package.
  The app still builds and installs.
  What happens when the audio feature runs?
options:
  - id: a
    text: "Your Kotlin code takes over and plays the sound anyway"
    why: "You may have thought the Kotlin recipe card could do the same job. Kotlin only calls the bottle. It does not hold a copy of the sauce."
  - id: b
    text: "The app crashes at that moment, because the library is not there"
    correct: true
    why: "The kitchen goes to pour the sauce and the bottle is gone. The Android documentation says an app with no shared-object file builds and installs, but crashes at runtime."
  - id: c
    text: "The app refuses to install"
    why: "You may have expected the install to stop the problem. The documentation says it installs. The problem only shows when the feature runs."
output: "The app crashes at that moment, because the library is not there"
```

## Tricks to remember

- `.so` means a bottle that arrived already cooked.
- One bottle per stove type: one `.so` per CPU kind (ABI).
- Native is a tool for some jobs, not a faster mode for everything.
- No bottle, no sauce: if the `.so` is missing, the app can build and install, then crash when the feature runs.

## Common mistakes

- **Thinking native code is always faster than Kotlin.** It sounds right because native code runs directly on the CPU. The Android documentation lists a few jobs where native code helps: low latency, heavy computing like games or physics, and reusing existing C or C++ libraries. For most apps, Kotlin is the right tool, and the documentation says the NDK may not suit most novice programmers.
- **Thinking one `.so` works on every phone.** It feels like one app, so it feels like one file. Phones have different CPUs. Each kind needs its own `.so`, which is why the `lib` folder has several subfolders.
- **Thinking the `cpp` folder is your Kotlin code in disguise.** It sits in the same project, so it feels like part of the same thing. It is a different language that is compiled ahead of time into a bottle. Edit it with that in mind.

## Where this shows up in Android

Think of an app that uses a game engine or an audio library. In the project you find a `cpp` folder and, after a build, `.so` files in the package. The Android documentation says Android Studio uses Gradle to compile your C or C++ code into a native library and packages the `.so` file into the app package.

Here is a symptom this lesson explains: an app installs fine on your phone, but it crashes the moment a feature opens on another phone. One reason can be that the package has no `.so` for that phone's CPU kind. The documentation says an app with no matching library installs but crashes when it runs. Check which ABI folders are in the package and which CPU the phone has.

## Recap

1. Native code is compiled ahead of time into instructions for one kind of CPU.
2. On Android, native code sits in `.so` files, one per CPU kind.
3. Native code helps for some jobs, and it is not faster for everything.

## You can now

- Explain what a `.so` file is, using the ready-made sauce.
- Pick the native files out of a list of files in an app package.
- Predict what happens when an app calls a native library that is missing.
