---
id: kmp/what-is-kmp/how-kotlin-reaches-an-iphone
title: "How does the same Kotlin end up inside an app on an iPhone?"
status: live
difficulty: medium
order: 2
requires: [kmp/what-is-kmp/what-kmp-shares]
summary: One Kotlin source is built twice. For Android it becomes Android bytecode. For iPhone it becomes a native framework, with no Java virtual machine.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [two-belts-one-source]
playground: false
fruit: false
upnext: false
---


## The question

You write one Kotlin function and both your Android app and your iPhone app use it. Kotlin is often linked with Java, so does the iPhone app carry a Java virtual machine inside it? If not, what does the phone actually run? And what has to happen between your source file and the iPhone?

## Mental model

Think of one script translated by two different translators.

- The script is your shared Kotlin source.
- Translator A prints a book for Android readers. The Android runtime reads it.
- Translator B prints a different book for iPhone readers. The iPhone app reads it directly, with no helper in between.
- Both books tell the same story.

**Where the analogy stops:** A human translator can change the meaning. A compiler has to keep the behaviour the same. The parts of the script that say "each phone supplies its own" are the exception, because there each book gets different words. Also, a book is just read. The iPhone's book is a program the processor runs.

## See it move

```sim
id: two-belts-one-source
template: flow
title: One Kotlin source, two belts, two outputs
intro: The shared Kotlin source waits at the fork. An Android target and an iOS target are both declared.
stations:
  - { id: src, label: "Shared Kotlin source" }
  - { id: jvm, label: "Android belt: Kotlin/JVM compiler makes class files" }
  - { id: d8, label: "Android belt: d8 makes DEX code" }
  - { id: nat, label: "iPhone belt: Kotlin/Native compiler makes a native binary" }
  - { id: fw, label: "iPhone belt: packed as a framework" }
paths:
  - [ src, jvm, d8 ]
  - [ src, nat, fw ]
items:
  - { id: ka, label: Kotlin source, at: src }
  - { id: ki, label: Kotlin source, at: src }
vars: { ios: "on" }
toggles:
  - { id: ios, label: iOS target declared, var: ios }
actions:
  - id: build
    label: Build
    cases:
      - when: { at: { ka: src }, var: { ios: "on" } }
        do:
          - move: { ka: d8, ki: fw }
          - label: { ka: "DEX code", ki: "Native framework" }
          - mark: { ka: good, ki: good }
          - light: [ d8, fw ]
          - say: "The shared source goes down both belts. The Android belt ends in DEX code. The iPhone belt ends in a native framework that the Xcode project links, with no virtual machine on the phone."
      - when: { at: { ka: src } }
        do:
          - move: { ka: d8 }
          - label: { ka: "DEX code", ki: "No belt" }
          - mark: { ka: good, ki: bad }
          - light: [ d8 ]
          - say: "With no iOS target declared, the iPhone belt never starts. No native framework is made, and Xcode would have nothing to link. The Android belt still ends in DEX code."
    refuse: The build already ran. Press "Clean" to run it again.
  - id: clean
    label: Clean
    do:
      - move: { ka: src, ki: src }
      - label: { ka: Kotlin source, ki: Kotlin source }
      - mark: { ka: plain, ki: plain }
      - light: [ src ]
      - say: The outputs are cleared and the source is back at the fork.
script: [ build, clean, "ios=off", build ]
try:
  - text: "Press \"Build\". Watch the source reach the fork and each belt make its output."
    done: [ build ]
  - text: "Press \"Clean\", switch off \"iOS target declared\" and press \"Build\" again. Watch what happens on the iPhone belt."
    done: [ clean, "ios=off", build ]
```

**What you should notice:** The Android belt keeps working without the iOS target, and the iPhone belt goes dark.

## Build the logic

### Notice

The shared function lives in a shared module. The Android app and the iPhone app each need a version they can use. The two phones understand different kinds of files.

Three tools appear below, so here they are in plain words (general knowledge). **Gradle** is the build tool that runs the compile steps. **Xcode** is Apple's tool for building iPhone apps. **Bytecode** is a compact, ready-made instruction format that a runtime reads instead of source code. **Swift** is the language iPhone apps are usually written in. A **framework** is a bundle of compiled code that an Apple app project links in (general knowledge).

### Guess

How does the iPhone get its version?

- A. The Kotlin is compiled once more, for the iPhone, into a file the iPhone app can use directly.
- B. The iPhone app carries a Java virtual machine and runs the same file as Android.
- C. The iPhone app reads the Kotlin source text and runs it line by line.

Idea A is the second translator. Idea B is the worry in the question. Idea C would need an extra program on the phone to read the text.

### Build

1. Write the shared code once, in the shared module.
2. Declare which phones you build for. Each choice is called a target.
3. For the Android target, the compiler makes bytecode for the Java virtual machine, and a tool turns it into the form Android runs.
4. For the iPhone target, a different compiler makes a native file, packed as a framework.
5. The iPhone project in Xcode links that framework, and its Swift code imports it by name.
6. If no iPhone target is declared, step 4 never happens, and step 5 has nothing to link.

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Discover your project](https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html), [Integrate into an existing app](https://kotlinlang.org/docs/multiplatform/multiplatform-integrate-in-existing-app.html)) Step 6 is my own reasoning from the docs' rule that the Xcode task exists only when a framework is declared.

### Name it

A **compiler** turns source code into another form. A **target** is one kind of device you build the shared code for. A **native binary** is a program file made of instructions the processor runs directly, with no virtual machine in between (general knowledge).

The Android belt. The Kotlin documentation says that when the shared module is built into an Android library, the common Kotlin is treated as Kotlin/JVM. The compiler produces JVM class files. The Android d8 tool then compiles Java bytecode into DEX bytecode that runs on Android devices.

The iPhone belt. The Kotlin documentation says **Kotlin/Native** is a technology for compiling Kotlin code to native binaries that can run without a virtual machine. It includes an LLVM-based backend for the Kotlin compiler. **LLVM** is a compiler toolkit. The page says Kotlin/Native is designed for platforms where virtual machines are not desirable or possible, such as embedded devices or iOS. When the shared module is built into an iOS framework, common Kotlin is treated as Kotlin/Native. The iOS targets are `iosArm64` for devices and `iosSimulatorArm64` for the simulator on an Apple silicon Mac.

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Discover your project](https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html))

**Objective-C** is an older Apple language, and Swift can import frameworks in that form (general knowledge). The documentation says a framework declared with `binaries.framework` is an Objective-C framework, and `baseName` sets its name. One of its own examples names the framework `sharedKit`, and the Swift code then writes `import sharedKit`.
From the docs, not run by me. ([Build native binaries](https://kotlinlang.org/docs/multiplatform/multiplatform-build-native-binaries.html), [Integrate into an existing app](https://kotlinlang.org/docs/multiplatform/multiplatform-integrate-in-existing-app.html))

Here is the shared source. It is ordinary Kotlin, and it is the single input to both belts.

```kotlin
fun greeting(name: String): String = "Hello, $name"

fun main() {
    println(greeting("Ana"))
}
```

This prints `Hello, Ana`. The function `greeting` uses only text, so it needs nothing from either phone.

Here is the shape of the build-file part that adds the iPhone framework. It is a piece of a build file, so it does not compile on its own.

```kotlin
// does not compile
// Shape only: this goes inside a shared module's build.gradle.kts
kotlin {
    val iosTargets = listOf(iosArm64(), iosSimulatorArm64())

    iosTargets.forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }
}
```

For each iOS target, `binaries.framework` asks for a framework named `shared`. This follows the shape of the example in the Kotlin documentation, without its XCFramework lines.

From the docs, not run by me. ([Build native binaries](https://kotlinlang.org/docs/multiplatform/multiplatform-build-native-binaries.html))

Two more rules from the docs complete the iPhone belt. A **Run Script build phase** is a step inside an Xcode build that runs a command you supply (general knowledge). The Xcode project calls the Gradle task `embedAndSignAppleFrameworkForXcode` in such a phase. The documentation says this task is registered only if `binaries.framework` is declared, and that the phase belongs before the Compile Sources phase. The Kotlin/Native target support page says that building final binaries for Apple targets on Linux and Windows is not possible. The overview says Xcode and its command-line tools are needed. So the iPhone belt needs a Mac.

From the docs, not run by me. ([Direct integration](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html), [Target support](https://kotlinlang.org/docs/native-target-support.html), [Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html))

| Translator | Official name | What it produces |
|---|---|---|
| The Android translator | Kotlin/JVM, then d8 | Class files, then DEX bytecode for Android |
| The iPhone translator | Kotlin/Native | A native framework for the iPhone app |
| The order for a book | A target in the build file | Tells the build which belts to run |

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Discover your project](https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html))

## Predict the output

```challenge
type: predict
run: false
id: output-of-each-target
verified: not-run
code: |
  Target     | The shared Kotlin is built into
  -----------|--------------------------------
  Android    | ?
  iosArm64   | ?
  Which pair fits?
options:
  - id: a
    text: "Both give the same bytecode, and the iPhone runs it with a Java virtual machine"
    why: "You may have thought one file serves every phone. The docs say Kotlin/Native compiles to native binaries that run without a virtual machine."
  - id: b
    text: "Android gives a framework, and iosArm64 gives DEX bytecode"
    why: "You may have swapped the two translators. DEX is Android's form, and the framework is the iPhone's."
  - id: c
    text: "Android gives JVM class files (then DEX), and iosArm64 gives a native framework"
    correct: true
    why: "Two translators, two books. The Android library is built as Kotlin/JVM, and the iOS framework is built as Kotlin/Native."
output: |
  Android gives JVM class files (then DEX), and iosArm64 gives a native framework
```

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Discover your project](https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html))

```challenge
type: predict
run: false
id: windows-only-team
verified: not-run
code: |
  A team has only Windows computers.
  They want the final iPhone binaries from their shared module.
  What do the Kotlin docs say?
options:
  - id: a
    text: "Windows can build everything a Mac can, so it works"
    why: "You may have thought Kotlin runs the same everywhere. The target support page says final binaries for Apple targets cannot be built on Windows."
  - id: b
    text: "The final iPhone binaries cannot be built on Windows, because Apple targets need a Mac host"
    correct: true
    why: "The second translator needs Apple's tools to finish its book. The docs say building final binaries for Apple targets on Linux and Windows is not possible."
  - id: c
    text: "It works if they install a Java virtual machine for iOS"
    why: "You may have pictured the iPhone needing a Java helper. The docs describe Kotlin/Native as running without a virtual machine, so there is nothing of that kind to install."
output: |
  The final iPhone binaries cannot be built on Windows, because Apple targets need a Mac host
```

From the docs, not run by me. ([Kotlin/Native target support](https://kotlinlang.org/docs/native-target-support.html))

```challenge
type: predict
run: false
id: no-ios-target
verified: not-run
code: |
  A shared module declares only an Android target.
  The iPhone project's Run Script step asks Gradle for
  embedAndSignAppleFrameworkForXcode.
  What does the docs' rule say about that task?
options:
  - id: a
    text: "It is not registered, because no binaries.framework is declared, so there is nothing to call"
    correct: true
    why: "No order was given for the iPhone book, so translator B never ran. The docs say the task registers only if binaries.framework is declared."
  - id: b
    text: "Gradle builds a framework anyway, from the Android output"
    why: "You may have expected Gradle to fill the gap. The Android output is bytecode for the Android runtime, not a framework."
  - id: c
    text: "Xcode compiles the Kotlin itself"
    why: "You may have thought Xcode reads Kotlin. Xcode only calls the Gradle task and links the framework it makes."
output: |
  It is not registered, because no binaries.framework is declared, so there is nothing to call
```

From the docs, not run by me. ([Direct integration with Xcode](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html)) The step from "only an Android target" to "no `binaries.framework`" is my own reasoning.

## Tricks to remember

- The iPhone belt makes a native framework. No Java virtual machine rides inside. One script, two translators, two books.
- No iOS target means no framework, and nothing for Xcode to link.
- Final iPhone binaries need a Mac.
- Try this on a real project by reading only: open the build file of a shared module and look for `binaries.framework` inside the iOS target block. If it is missing, the Xcode step has nothing to call.

From the docs, not run by me. ([Direct integration with Xcode](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html), [Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Target support](https://kotlinlang.org/docs/native-target-support.html))

## Common mistakes

- **Thinking Kotlin on iPhone runs on a Java virtual machine.** Kotlin is often linked with Java, so it feels natural. The docs describe Kotlin/Native as compiling to native binaries without a virtual machine.
- **Thinking Kotlin on iPhone is read line by line.** The iPhone app gets a compiled framework that the build made before the app ever starts.
- **Thinking one compiled file serves both phones.** The word shared suggests one file. The source is shared, and each target builds its own output.
- **Thinking a Windows laptop can build the whole project.** The Android half works anywhere, so the whole thing feels portable. The iPhone half needs a Mac.

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Target support](https://kotlinlang.org/docs/native-target-support.html))

## Where this shows up in Android

A common sentence on a team is this. "I can build the Android app, but not the iPhone framework, on my Windows laptop." The Android belt finishes with DEX bytecode, and it does not depend on the iOS target (my own reasoning). The iPhone belt needs Apple's tools.

From the docs, not run by me. ([Kotlin/Native target support](https://kotlinlang.org/docs/native-target-support.html))

The other everyday case is the reverse: a build file that lists only an Android target. The Android app builds and runs fine, and the iPhone project finds nothing to link. My own reasoning from the docs' rule is that Xcode then fails at the Run Script step, because the task it asks for is not registered. I did not see that failure message in any document, so I do not quote one.

From the docs, not run by me. ([Direct integration with Xcode](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html))

## Say it in an interview

**How does Kotlin run inside an iOS app next to Swift?**
The shared Kotlin is compiled for the iOS target by Kotlin/Native into a framework. The Xcode project links it, and Swift imports it by name. According to the Kotlin documentation, no virtual machine is involved.

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html), [Integrate into an existing app](https://kotlinlang.org/docs/multiplatform/multiplatform-integrate-in-existing-app.html))

**What is Kotlin/Native, and what is LLVM's part?**
Kotlin/Native is a technology for compiling Kotlin to native binaries that can run without a virtual machine. It includes an LLVM-based backend for the Kotlin compiler, so LLVM is part of the compiler's machinery. The trade-off is a separate build per platform, and an Apple host for Apple targets.

From the docs, not run by me. ([Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html))

**Why does a team need a Mac to ship the iPhone side?**
The Kotlin/Native target support page says final binaries for Apple targets cannot be built on Linux or Windows. Apple targets also need Xcode and its command-line tools. A Windows machine can still build and test the Android half (my own reasoning).

From the docs, not run by me. ([Target support](https://kotlinlang.org/docs/native-target-support.html), [Kotlin/Native overview](https://kotlinlang.org/docs/native-overview.html))

**What goes wrong if the iPhone project cannot find the Kotlin framework?**
Check the link between Xcode and Gradle. The documentation says Xcode calls the `embedAndSignAppleFrameworkForXcode` task in a Run Script phase, that the task exists only if `binaries.framework` is declared, and that the phase sits before Compile Sources. Missing declaration or a wrong phase order are the first places to look (my own reasoning).

From the docs, not run by me. ([Direct integration with Xcode](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html))

## Recap

1. The same Kotlin source is built once per target.
2. Android gets bytecode for its runtime, and the iPhone gets a native framework.
3. No iOS target means no framework, and building one needs a Mac.

## You can now

- Say what an iPhone app runs instead of a Java virtual machine.
- Predict which output file each target produces.
- Explain why a build file with no iOS target leaves the iPhone project nothing to link.
