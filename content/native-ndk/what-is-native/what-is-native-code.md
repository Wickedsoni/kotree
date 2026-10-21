---
id: native-ndk/what-is-native/what-is-native-code
title: What is native code inside an Android app?
status: shadow
difficulty: easy
order: 1
requires: []
summary: Native code is already turned into instructions for one kind of CPU. On Android it sits in a file ending in .so.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/ndk/guides ("The Native Development Kit (NDK) is a set of tools that allows you to use C and C++ code with Android"; use it to "achieve low latency or run computationally intensive applications, such as games or physics simulations" and to "reuse your own or other developers' C or C++ libraries"; "may not be appropriate for most novice Android programmers"; "Gradle uses the build script to import source code into your Android Studio project and package your native library (the SO file) into the APK"). https://developer.android.com/ndk/guides/abis (each combination of CPU and instruction set has its own ABI; supported ABIs armeabi-v7a, arm64-v8a, x86, x86_64; libraries live at /lib/<abi>/lib<name>.so; "the package manager unpacks only the most appropriate machine code for the target device"; "If there is no shared-object file at all, the application builds and installs, but crashes at runtime"). The page https://source.android.com/docs/core/runtime could not be opened in this session, so the lesson makes no claim about DEX or ART. Rust is named once and not taught. The five-file package in Predict is an invented example, not a real app. Assumes foundation/how-computers-work/what-is-a-cpu, foundation/from-code-to-running/what-is-a-compiler and foundation/from-code-to-running/what-is-the-jvm. The Predict items were traced by hand. -->

## The question

You open an Android project and find a folder named `cpp`, or a file that ends in `.so`. Your Kotlin files are all in another folder. Nobody told you what this other thing is. What is the code in that file, and how is it different from the Kotlin code you write?

## Mental model

Think of a kitchen that buys a ready-made sauce from a specialist factory.

- The kitchen is your app.
- Your recipe cards are your Kotlin code.
- The factory is the place where C or C++ code is turned into finished machine instructions.
- The sauce bottle is the native library, the `.so` file. It arrives already cooked.
- The stove type is the kind of CPU. A bottle cooked for one stove type does not work on another.

Your kitchen opens the bottle and pours it in. It does not cook the sauce again.

**Where the analogy stops:** Your Kotlin code is not cooked from raw on the phone either. It is turned into a form that does not belong to one kind of CPU, and the phone finishes the job (general knowledge). The difference is that the bottle is finished for one stove before the app is even built. Also, a sauce is only food. A native library is code that your app calls and trusts, and a mistake inside it can crash your app. And a good sauce does not improve every dish: native code is not faster at everything.

## Build the logic

### Notice

Open the file you install, the app package, and look inside. You find your compiled app code, a file that describes the app, and your pictures and screen layouts.

Sometimes there is also a folder named `lib`. Inside it are folders named after kinds of CPU, like `arm64-v8a` and `x86_64`. In each of them is a file ending in `.so`.

### Guess

What is in those `.so` files?

- A. Pictures for different screen sizes.
- B. Code that was already turned into instructions for one kind of CPU.
- C. The same Kotlin code, but faster.

Idea A sounds sensible because the folders look like screen sizes, but they are named after CPUs. Idea C treats native code as a speed switch. It is a different kind of file, not a faster copy of your Kotlin. Idea B is the sauce bottle.

### Build

1. A developer writes a part of the app in a language like C or C++.
2. A compiler, the factory, turns that code into instructions for one kind of CPU. It does this before the app is built.
3. The result is one file per kind of CPU, because a bottle cooked for one stove does not fit another.
4. The app package carries these files in its `lib` folder.
5. When the app is installed, the phone unpacks only the most appropriate one for its own CPU.
6. Your app opens the bottle and calls what is inside.

If no bottle is there at all, your recipe cards still look fine, but the dish that needs the sauce cannot be made.

### Name it

- **Native code** is code that was compiled ahead of time into instructions for one kind of CPU, so the CPU can run it directly.
- A **native library** is a bundle of native code that your app can call. On Android it is a file ending in **`.so`**, short for shared object.
- The **NDK**, the Native Development Kit, is the set of tools Android gives you to use C and C++ code in an app.
- An **ABI** is the name for one combination of CPU and instruction set, for example `arm64-v8a`. The documentation says libraries sit at `/lib/<abi>/lib<name>.so` in the package.
- **Gradle** is the build tool that packages the `.so` file into the app package.

| Kitchen | Official name | In the app package |
|---|---|---|
| Your recipe cards | Your Kotlin code | Your compiled app code |
| The factory | The C or C++ compiler | The NDK build |
| The sauce bottle | Native library | `lib/arm64-v8a/libaudio.so` |
| The stove type | ABI | `arm64-v8a`, `x86_64` |

C and C++ are the languages the Android documentation describes for native code. Rust is another language that compiles to native code (general knowledge). It is named here only so you recognise the word.

This page has no program to run. You read a package like a map.

## Predict the output

```challenge
type: predict
run: false
id: which-files-are-native
verified: not-run
code: |
  An app package holds these five files (an invented example):
    1. classes.dex                      (the compiled app code)
    2. AndroidManifest.xml              (describes the app)
    3. lib/arm64-v8a/libaudio.so
    4. lib/x86_64/libaudio.so
    5. res/layout/main.xml              (a screen layout)
  Which files are native libraries?
options:
  - id: a
    text: "Files 1 and 3"
    why: "You may have thought the compiled app code is also a sauce bottle. It is a recipe card for the phone to use when the app runs. Only the .so files are bottles."
  - id: b
    text: "All five files"
    why: "You may have thought everything in the package is native. A description file and a layout are plain files. Only the bottles carry ready-made CPU instructions."
  - id: c
    text: "Files 3 and 4"
    correct: true
    why: "Both end in .so. They are the same sauce cooked for two stove types, one for arm64-v8a CPUs and one for x86_64 CPUs."
output: |
  Files 3 and 4
```

```challenge
type: predict
run: false
id: remove-the-bottle
verified: not-run
code: |
  The audio feature of the app calls into libaudio.so.
  The app is built with no libaudio.so file at all.
  The app builds and installs.
  What happens when the audio feature runs?
options:
  - id: a
    text: "The app crashes at that moment, because the library is not there"
    correct: true
    why: "The kitchen goes to pour the sauce and no bottle exists. The Android documentation says an app with no shared-object file builds and installs, but crashes at runtime."
  - id: b
    text: "Your Kotlin code takes over and plays the sound anyway"
    why: "You may have thought the Kotlin recipe card could do the same job. Kotlin only calls the bottle. It does not hold a copy of the sauce."
  - id: c
    text: "The app refuses to install"
    why: "You may have expected the install to stop the problem. The documentation says it installs. The problem shows when the feature runs."
output: |
  The app crashes at that moment, because the library is not there
```

## Tricks to remember

- `.so` means a bottle that arrived already cooked.
- One bottle per stove type: one `.so` per ABI folder.
- Native is a tool for some jobs, not a faster mode for everything.
- No bottle at all, no sauce: the app can build and install, then crash when the feature runs.

## Common mistakes

- **Thinking native code is always faster than Kotlin.** It sounds right because native code runs directly on the CPU. The documentation lists a few jobs where native code helps: low latency, heavy computing such as games or physics simulations, and reuse of C or C++ libraries. It also says the NDK may not suit most novice Android programmers.
- **Thinking one `.so` works on every phone.** It is one app, so it feels like one file. Phones have different CPUs, and each ABI has its own folder in the package.
- **Thinking the `cpp` folder is your Kotlin code in disguise.** It sits in the same project, so it feels like part of the same thing. It is a different language, compiled ahead of time into a bottle.

## Where this shows up in Android

Think of an app that uses a game engine or an audio library. In the project you find a `cpp` folder and, after a build, `.so` files in the package. The Android documentation says Gradle packages your native library, the SO file, into the app package.

Here is a symptom this lesson explains: an app installs fine, but crashes the moment a feature opens. The documentation says that if there is no shared-object file at all, the app builds and installs but crashes at runtime. My own reasoning: a package that carries libraries for only some ABIs could fail the same way on a phone with a different CPU. The page does not say so, but it is worth checking which ABI folders the package has.

## Say it in an interview

These questions are my own, written for this lesson.

**What is native code, and how does it differ from Kotlin?**
Native code is compiled ahead of time into instructions for one kind of CPU, so the CPU can run it directly. On Android it is usually C or C++ built with the NDK into a `.so` file. Kotlin code is turned into a form that is not tied to one CPU, and the phone finishes the job (general knowledge).

**What is an ABI, and why does an app package have several `lib` folders?**
An ABI is one combination of CPU and instruction set, such as `arm64-v8a`. The documentation says each combination has its own ABI and libraries sit at `/lib/<abi>/lib<name>.so`. The package manager unpacks only the most appropriate machine code for the phone.

**When would you use the NDK, and when not?**
The documentation gives three reasons: low latency, heavy computing such as games or physics simulations, and reuse of existing C or C++ libraries. It also says the NDK may not suit most novice Android programmers who need only Java or framework APIs. My own reasoning: native code adds a second language and a second build to maintain, so use it only when one of the three reasons applies.

**What happens if the `.so` file is missing?**
The documentation says that with no shared-object file at all, the app builds and installs, but crashes at runtime. The crash shows up when the feature that calls the library runs, not at install time.

## Recap

1. Native code is compiled ahead of time into instructions for one kind of CPU.
2. On Android, native code sits in `.so` files, one per ABI.
3. Native code helps for some jobs, and it is not faster for everything.

## You can now

- Pick the native files out of a list of files in an app package.
- Predict what happens when an app is built with no native library and calls into one.
