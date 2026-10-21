---
id: native-ndk/art-and-dex/what-is-dex
title: "What is inside classes.dex?"
status: shadow
difficulty: easy
order: 2
requires: [native-ndk/what-is-native/what-is-native-code]
summary: A dex file holds your app's compiled code in a compact form that Android's runtime reads, and native C++ code is kept out of it.
video: ""
publishedOn:
updatedOn:
sims: [dex-packer]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live):
- https://source.android.com/docs/core/runtime/dex-format : .dex files "hold a set of class definitions and their associated adjunct data"; lists of identifiers for methods referred to by the file.
- https://developer.android.com/tools/d8 : d8 compiles Java bytecode into DEX bytecode; input is *.class files or JAR/APK/ZIP; "the main DEX file, which is typically named classes.dex".
- https://developer.android.com/build/multidex : 65,536 method-reference limit per DEX file; classes.dex, classes2.dex, ...; minSdk 21 and higher enables multidex by default; error text "Too many field references: 131000; max is 65536".
- https://developer.android.com/guide/platform : ART executes DEX files, "a bytecode format designed specifically for Android that's optimized for a minimal memory footprint"; each app has its own ART instance.
- https://source.android.com/docs/core/runtime : "ART as the runtime executes the Dalvik executable (DEX) format and DEX bytecode specification."
- https://developer.android.com/studio/debug/apk-analyzer : DEX file viewer, Referenced Methods counts methods defined in your code AND dependency libraries.
- https://developer.android.com/topic/performance/app-optimization/enable-app-optimization : R8 removes unused code and resources, rewrites code; shortens names "to reduce the size of the DEX file".
- Not in any page I read, labelled in the text as general knowledge: one .class file per class; Kotlin compiles to bytecode.
- Not verified, so not claimed: that R8 itself writes the DEX output; that a package with no classes.dex cannot start (the sim only shows that the compiled Kotlin is gone).
-->

## The question

You unzip an app file and find a file named `classes.dex`. Your project had many `.kt` files, and none of them is in the package. Where did your Kotlin go? What is this file that took its place?

## Mental model

Think of a pocket edition of a cookbook.

- Your full cookbook is your project. Every recipe sits on its own loose page. One loose page is one class.
- The packer is a machine that reads all the loose pages and prints them tightly into one small book.
- The pocket edition is the small book. It is `classes.dex`.
- The pocket reader is a cook who can only read pocket editions. On a phone, that cook is the Android runtime.
- A ready-made jar from a factory is not a recipe. It cannot be printed in the book, so it travels in its own bag.

Your recipes are not thrown away. They are packed so they fit a small bag. The reader never sees your loose pages.

**Where the analogy stops:** A pocket edition has the same words as the big book, only smaller. A dex file is a different format, and a laptop's own reader cannot open it directly (general knowledge). Also, a dex file holds the recipes of the libraries you use, as well as yours.

## See it move

<!-- SIM: dex-packer -->

**Try this:**
1. Press "Pack" and watch the loose class pages slide into the packer and come out as one dex box inside the package.
2. Click the package. Find the dex box and the separate bag for the ready-made jars.
3. (Optional reveal) Remove the dex box and look at what is left of the Kotlin code.

**What you should notice:** The compiled Kotlin code lives in the dex box, and the C++ jars sit in a different bag.

## Build the logic

### Notice

Look at what you wrote and what the package holds.

| What you wrote | What the package holds |
|---|---|
| `MainActivity.kt`, `Cart.kt`, `Price.kt` | `classes.dex` |
| `audio.cpp` | `lib/arm64-v8a/libaudio.so` |
| The libraries you added | More recipes inside `classes.dex` |

The package has no `.kt` file. It has one dex file and one `.so` file.

### Guess

What does `classes.dex` hold?

- A. Copies of your `.kt` files, squeezed smaller.
- B. Compiled classes, yours and your libraries', in a compact form made for Android.
- C. Your C++ code, ready to run on the CPU.

Idea A would ship your text and leave the phone to read it. That is not the plan. Idea C describes the `.so` file from the last lesson. Idea B is the pocket edition.

### Build

1. The Kotlin compiler turns each `.kt` file into **bytecode**, a compact list of instructions that is not tied to one kind of CPU (general knowledge). The bytecode is kept as one `.class` file per class (general knowledge).
2. A tool called D8 reads those class files, and the class files of your libraries. It writes them into DEX bytecode.
3. The package carries the dex file. It also carries your `.so` files in its `lib` folder.
4. On the phone, the Android runtime reads the dex file. The `.so` files are not dex, so they are never read this way.

The Kotlin text is gone from the package. The compiled classes took its place.

### Name it

- **DEX** is short for Dalvik Executable format. The Android documentation calls it a bytecode format designed specifically for Android and optimized for a minimal memory footprint.
- **`classes.dex`** is the file that holds your compiled classes. The D8 documentation says the main dex file is typically named `classes.dex`.
- **D8** is the tool that compiles Java bytecode into DEX bytecode. Its input can be `.class` files or containers such as JAR, APK or ZIP files.
- **ART**, the Android Runtime, is the phone's own program that runs an app. The Android documentation says it executes the DEX format.
- One dex file can refer to at most 65,536 methods. This is called the **64K limit**. The documentation says that when an app needs more, the build tools add `classes2.dex` and more, which is called **multidex**. This lesson does not go further into it.
- **R8** is Android's app optimizer. The documentation says it removes unused code and resources, and rewrites code. It is named here once, and this lesson does not teach it.

| Cookbook | Official name | In the package |
|---|---|---|
| The loose pages | Class files | Made on the developer's computer |
| The packer | D8 | Part of the build |
| The pocket edition | A dex file | `classes.dex` |
| The pocket reader | ART | Runs on the phone |
| The ready-made jar | A native library | `lib/<abi>/lib<name>.so` |

This page has no program to run. You read a package like a map.

## Predict the output

```challenge
type: predict
run: false
id: which-files-are-dex
verified: not-run
code: |
  An app package holds these four files (an invented example):
    1. classes.dex
    2. AndroidManifest.xml
    3. lib/arm64-v8a/libgame.so
    4. res/drawable/logo.png
  Which file holds the compiled Kotlin classes?
options:
  - id: a
    text: "File 3, because the library is the code"
    why: "You may have counted the compiled library as your Kotlin. A .so file is a ready-made jar built from C or C++, and it is not a dex file."
  - id: b
    text: "File 1"
    correct: true
    why: "The pocket edition holds your compiled classes and your libraries' classes. The Android documentation says the main dex file is typically named classes.dex."
  - id: c
    text: "File 2, because it lists everything in the app"
    why: "You may have thought the list is the code. The manifest describes the app, and the recipes are in the dex file."
output: |
  File 1
```

```challenge
type: predict
run: false
id: count-the-boxes
verified: not-run
code: |
  An app has Kotlin code and one C++ library, built for one CPU kind.
  All the Kotlin classes and the libraries' classes are packed into one dex file.
  How many dex files and how many .so files are in the package?
options:
  - id: a
    text: "One dex file and one .so file"
    correct: true
    why: "One pocket edition holds the compiled Kotlin and the libraries' classes, and the jar travels in its own bag."
  - id: b
    text: "Two dex files, one for Kotlin and one for C++, and no .so file"
    why: "You may have thought each language gets its own volume. C++ is compiled to machine instructions for a CPU, and it does not go into a dex file."
  - id: c
    text: "No dex file, because the native library replaces the Kotlin code"
    why: "You may have thought native code takes over. The Kotlin code still has to be packed, and the native library only adds one more box."
output: |
  One dex file and one .so file
```

## Tricks to remember

- The pocket edition holds recipes. The jars travel in their own bag.
- Loose pages in, pocket edition out. D8 is the packer.
- The pocket edition holds your recipes and your libraries' recipes.
- No `.kt` file ships. The compiled classes do.

## Common mistakes

- **Thinking the app contains your `.kt` files.** You wrote them, so they feel like what ships. The package holds compiled classes in dex files. Picture the pocket edition, not the loose pages.
- **Thinking a dex file is a renamed `.class` or `.jar`.** The names look related, so they feel alike. The Android documentation calls DEX a separate format made for Android, and D8 is the tool that converts into it.
- **Thinking a dex file holds only my own code.** I wrote only my part, so it feels like all of it. The APK Analyzer documentation says a dex file's method counts include methods from dependency libraries.
- **Thinking C++ code is inside the dex file.** Both are "code", so they feel like the same pile. Native code is already machine instructions, and it sits in `.so` files in the `lib` folder.

## Where this shows up in Android

You can look inside a package without running it. The APK Analyzer in Android Studio shows the size of files such as the DEX and resource files, and you can open the `classes.dex` file in its DEX file viewer. You will not find your `.kt` files there. You will find compiled classes and packages.

The viewer's Referenced Methods column counts the methods the dex file refers to, including methods defined in dependency libraries. The Defined Methods column counts only the methods defined in the dex file. The documentation says a dependency you package adds to both counts. So the numbers are bigger than the number of functions you wrote.

## Say it in an interview

These questions are my own, written for this lesson. No interview data exists for this branch.

**What is a DEX file, and why does Android use it?**
It is a file of compiled classes in a bytecode format designed specifically for Android and optimized for a minimal memory footprint, as the Android documentation puts it. The tool D8 compiles Java bytecode into DEX, and ART, the Android runtime, executes it. The trade-off is an extra build step between your Kotlin and the phone.

**Does a dex file hold only my own code?**
No. The APK Analyzer documentation says the methods a dex file refers to include methods from dependency libraries, and a packaged dependency adds to the counts. So the size of the dex file depends on the libraries as well as on what I wrote. The trade-off is that a heavy library costs space in every build.

**Where does C++ code go in an app package?**
It is not in a dex file. The NDK build compiles it into `.so` files, which sit at `lib/<abi>/lib<name>.so` in the package. A dex file is only for bytecode.

**What do D8 and R8 do?**
The documentation says D8 compiles Java bytecode into DEX bytecode. R8 is the app optimizer that removes unused code and resources and rewrites code, and it shortens names to reduce the size of the DEX file. The pages I read do not say exactly how the two relate, so I state each job on its own.

## Recap

1. A dex file holds your compiled classes and your libraries' classes in a compact Android format.
2. D8 packs class files into dex, which is the format the Android runtime reads.
3. Native C++ code is not in dex files, because it lives in `.so` files.

## You can now

- Pick the dex files out of a list of files in an app package.
- Predict how many dex files and `.so` files a described app package holds.
- Explain, using the pocket edition, why no `.kt` file ships in an app.
