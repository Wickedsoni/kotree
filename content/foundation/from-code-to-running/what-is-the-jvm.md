---
id: foundation/from-code-to-running/what-is-the-jvm
title: How can the same Kotlin code run on many different computers?
status: shadow
difficulty: medium
order: 28
requires: [foundation/from-code-to-running/compiler-vs-interpreter]
summary: Compile once into a shared in-between form, and a helper program on each computer can run it.
video: ""
publishedOn:
updatedOn:
sims: [bytecode-adapters]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/faq.html (fetched 20 Oct 2026): "When targeting the JVM, Kotlin produces Java-compatible bytecode."; "When targeting native, Kotlin will produce platform-specific code (via LLVM)."; "Kotlin is 100% compatible with the JVM". kotlinlang.org/docs/native-overview.html: Kotlin/Native is "a technology for compiling Kotlin code to native binaries that can run without a virtual machine". kotlinlang.org/docs/command-line.html: kotlinc hello.kt -include-runtime -d hello.jar; java -jar hello.jar. kotlinlang.org/docs/gradle-compiler-options.html: jvmTarget is the "Target version of the generated JVM bytecode". developer.android.com/build/jdks: the JDK contains "The Java Virtual Machine (JVM), an interpreter that executes Java applications"; "The JVM is not used on Android devices or emulators."; "Note that not all functions are available on Android." developer.android.com/tools/d8: d8 compiles "Java bytecode into DEX bytecode that runs on Android devices". Chips follow different steps, and one JVM per kind of computer: general programming knowledge. Outputs hand-traced, not run. -->
## The question

You write one Kotlin program on a Windows laptop. Your friend runs it on a Mac. A server in an office runs it on a third kind of machine. You wrote it once. How can all three run it?

## Mental model

Think of a traveller with one favourite lamp and a bag of power adapters.

- The lamp is your program.
- The plug on the lamp has one fixed shape. That shape is the same wherever the traveller goes.
- Each country has its own wall socket. A wall socket stands for one kind of computer.
- An adapter fits the lamp's plug to the socket of one country. There is one adapter for each country.
- A country with no adapter in the bag means the lamp stays dark there.

**Where the analogy stops:** An adapter only reshapes the plug, but the helper program in the real story does work all the time the program runs. Also, a plug shape never changes, but the shared form in the real story comes in versions.

## See it move

<!-- SIM: bytecode-adapters -->

**Try this:**
1. Press "Send the stack" and watch the same tiles reach the three machine boxes.
2. Take the adapter off one machine and send the stack again.
3. Put the adapter back. Add a fourth machine box and see what it needs before it can run the stack.

**What you should notice:** A machine runs the stack only when it has its own adapter, and the stack itself never changes.

## Build the logic

### Notice

A traveller owns one lamp and plans three trips. One country has round holes in its sockets, another has flat holes, and the third has three slots. The lamp's plug has the same shape on every trip.

### Guess

How can the one lamp work in all three countries?

- A. Build a new lamp for each country, with a plug that fits its sockets.
- B. Ask every country to change its sockets to match the lamp.
- C. Keep the lamp as it is, and carry an adapter for each country's socket.

Idea A works, but every change to the lamp has to be made three times. Idea B is not in your power. Idea C is the answer.

### Build

1. Each kind of computer has its own chip, and each chip follows its own kind of steps. This is the wall socket.
2. Steps made for one kind of chip fit only that kind. Aiming a program at three kinds of computer means three separate builds, repeated after every change.
3. Instead, build once into one shared in-between form that no chip owns. This is the plug shape that is the same everywhere.
4. On each computer, install a helper program made for that computer. It knows how to run the shared form there. This is the adapter.
5. The program now runs on any machine that has the helper. One build, many machines.
6. A machine without the helper cannot run it, even though the program is the same.

### Name it

The shared in-between form is **bytecode**. The helper program is the **Java Virtual Machine**, or **JVM**: it runs bytecode on the computer where it is installed. The Android documentation describes the JVM as "an interpreter that executes Java applications". An **interpreter** is a program that carries out each step as it reads it. The Kotlin documentation says: "When targeting the JVM, Kotlin produces Java-compatible bytecode." It also says: "Kotlin is 100% compatible with the JVM". Bytecode comes in versions, and a Kotlin compiler option called `jvmTarget` is described as the "Target version of the generated JVM bytecode".

The JVM is not a programming language. Kotlin and Java are languages. The JVM runs what their compilers produce. Each kind of computer needs a JVM made for it. These four sentences are general knowledge, not spelled out in the Kotlin documentation.

Kotlin does not always go through bytecode. The Kotlin documentation says that when targeting native, Kotlin produces platform-specific code. Its native page says this code can run without a virtual machine, which is a helper program like the JVM. That version is built again for each kind of machine.

The documentation shows the two steps for the JVM way with a file called `hello.kt`. The first command is the compiler, and the second runs the result: `kotlinc hello.kt -include-runtime -d hello.jar`, then `java -jar hello.jar`.

```kotlin
fun main() {
    println("Hello")
}
```

Saved as `hello.kt` and run with those two commands, this program prints `Hello`.

## Predict the output

Follow each item on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: adapter-removed
verified: not-run
code: |
  One bytecode stack: it makes a machine print its own name.

  Machines, in this order:
    Laptop    has a JVM
    Server    has a JVM
    Old-box   has no JVM

  Every machine that can run the stack prints its name.
  What is printed?
options:
  - id: a
    text: "Laptop, then Server"
    correct: true
    why: "Laptop and Server each have an adapter, so each runs the same stack. Old-box has no adapter, so the lamp stays dark there."
  - id: b
    text: "Laptop, then Server, then Old-box"
    why: "You may have thought the same stack fits every machine on its own. The plug shape is the same everywhere, but a machine needs its own adapter to use it."
  - id: c
    text: "Laptop only"
    why: "You may have thought only the machine you built on can run the stack. Any machine with an adapter can."
output: |
  Laptop
  Server
```

```challenge
type: predict
run: false
id: builds-per-machine
verified: not-run
code: |
  fun main() {
      val machineKinds = 3
      val updates = 4
      // $name shows a value. ${...} shows the result of a calculation.
      println("One build per machine: ${machineKinds * updates}")
      println("One shared build: $updates")
  }
options:
  - id: a
    text: "One build per machine: 4, then One shared build: 12"
    why: "You may have swapped the two ways. The shared form is built once per update, so it is the smaller number."
  - id: b
    text: "One build per machine: 7, then One shared build: 4"
    why: "You may have added 3 and 4. Every update is built for every kind of machine, so the numbers multiply."
  - id: c
    text: "One build per machine: 12, then One shared build: 4"
    correct: true
    why: "Without the shared plug, each of the 4 updates is made for 3 kinds of machine, so 3 times 4 is 12. With the shared plug, each update is made once, so it is 4."
output: |
  One build per machine: 12
  One shared build: 4
```

```challenge
type: predict
run: false
id: which-is-the-language
verified: not-run
code: |
  Four names are listed:
    Kotlin     a programming language
    Java       a programming language
    bytecode   the shared in-between form
    JVM        the program that runs bytecode

  How many of the four are programming languages?
options:
  - id: a
    text: "3"
    why: "You may have counted the JVM, because Java is in its name. The JVM is the adapter, so it is a program that runs bytecode and not a language."
  - id: b
    text: "2"
    correct: true
    why: "Kotlin and Java are the languages, the lamps people write. Bytecode is the plug shape and the JVM is the adapter, and neither is a language."
  - id: c
    text: "1"
    why: "You may have counted only Kotlin because this is a Kotlin course. Java is also a language. It is the JVM and bytecode that are not."
output: "2"
```

## Tricks to remember

- Same plug, different adapters.
- Build once, run where a JVM lives.
- The JVM is the adapter, not the language.
- No adapter, no light.

## Common mistakes

- **Thinking the JVM is a programming language.** The name has "Java" in it, so it sounds like one. It is a program that runs bytecode. Kotlin and Java are the languages.
- **Thinking shared code runs anywhere on its own.** One plug shape feels like it needs nothing else. A machine still needs its own JVM.
- **Thinking Kotlin always becomes bytecode.** Learning the JVM way first makes it feel like the only way. Kotlin can also produce platform-specific code when it targets native.
- **Thinking the JVM does the compiling.** The two steps feel like one Java step. The compiler runs first and makes the bytecode. The JVM runs it afterwards.

## Where this shows up in Android

Android Studio is the program most Android developers write their apps in. The Android documentation says you use the JVM to run Android Studio and the Gradle build tool, which builds your app. It also says "The JVM is not used on Android devices or emulators." So your laptop has an adapter, and the phone does not use that one.

A second tool bridges the gap. The Android documentation says `d8` is a command-line tool that Android Studio and the Android Gradle plugin use to compile your project's Java bytecode into **DEX** bytecode, the form that runs on Android devices. The page that describes the JVM also notes that not all functions in the Java libraries are available on Android. A symptom would be code that works in a small Kotlin program on your laptop but is missing on the phone. That example is our own reasoning.

## Recap

1. Different chips follow different steps, so steps made for one kind of chip fit only that kind.
2. Kotlin for the JVM is compiled once into bytecode, a shared in-between form.
3. A JVM on each computer runs the bytecode, and the JVM is a program, not a language.

## You can now

- Predict which machines can run the same bytecode when one of them has no JVM.
- Work out how many builds a shared in-between form saves when there are several machines and updates.
- Sort Kotlin, Java, bytecode and the JVM into programming languages and things that are not.
