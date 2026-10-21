---
id: kmp/source-sets/project-layout-and-targets
title: "Which folder holds code for which platform, and what does the build file declare?"
status: shadow
difficulty: medium
order: 3
requires: [kmp/what-is-kmp/how-kotlin-reaches-an-iphone]
summary: Each folder under src is a source set for some targets. The build file declares the targets, and that decides which folders get compiled.
video: ""
publishedOn:
updatedOn:
sims: [floor-plan-targets]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips this comment at go-live):
Kotlin, Discover your project (commonMain compiles to all declared targets; each target has a platform source set that compiles for only that target; iosMain shares code among the iOS targets; targets are declared with DSL calls in the kotlin block; src directories): https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html
Kotlin, Hierarchical project structure (default hierarchy template creates the shared source sets from the declared targets; sharing code among some but not all targets): https://kotlinlang.org/docs/multiplatform/multiplatform-hierarchy.html
Kotlin, Add dependencies (common code needs to compile for every declared target; a library can be added to chosen source sets only): https://kotlinlang.org/docs/multiplatform/multiplatform-add-dependencies.html
Android, KMP library plugin (com.android.kotlin.multiplatform.library, the android block, androidMain): https://developer.android.com/kotlin/multiplatform/plugin
Interview seed: docs/practice/interview-kmp.md section for this leaf. Its androidTarget() and iosX64() wording was not used as fact (androidTarget() appears only in the Android page's migration example, iosX64 was on no page read).
-->
<!-- ⚠️ VERIFY: (1) the roadmap note that androidTarget was renamed android is NOT claimed here; the pages read show android { } for the new plugin and androidTarget() only in a migration example. (2) The exact wording of the project wizard options (kmp.jetbrains.com) is not used. (3) iOS-side claims are from the docs, not run. -->
## The question

A teammate shows you a shared project. One folder is named for the iPhone, one for Android and one for code that every phone uses. They point at a file in the iPhone folder and ask: which builds read it? You cannot answer by looking at the folder alone. What decides which folder is read for which phone, and where does that decision live?

## Mental model

Think of a floor plan of a small block.

- The block is your shared project.
- The shared corridor in the middle is the folder every builder may walk through.
- The Android wing and the iPhone wing are rooms for one kind of builder only.
- The plan on the wall lists which wings the block really has.
- A builder is a build for one **target**, which means one kind of phone. It walks the corridor and its own wing.

A room in the corridor is open to every builder. A room in a wing is open only to its own builder. If the plan lists no iPhone wing, no iPhone builder comes.

**Where the analogy stops:** A room in a block is one room that everyone walks through. A file in the shared folder is read once for each build, and each build makes its own result from it. The Kotlin documentation says the shared folder compiles to all declared targets. Nothing is copied into the wings. Also, a wing can use the corridor, but the corridor cannot use anything that lives in a wing.

## See it move

```sim
id: floor-plan-targets
template: compare
title: Which rooms each build reads
intro: "The block has a shared corridor, an Android wing and an iPhone wing. Both builders are shown and nothing has been built yet."
sides:
  - id: android
    label: "Android builder"
    boxes:
      - { id: corridor, label: "commonMain: the shared corridor" }
      - { id: android-wing, label: "androidMain: the Android wing" }
      - { id: iphone-wing, label: "iosMain: the iPhone wing" }
  - id: iphone
    label: "iPhone builder (device and simulator)"
    boxes:
      - { id: corridor, label: "commonMain: the shared corridor" }
      - { id: android-wing, label: "androidMain: the Android wing" }
      - { id: iphone-wing, label: "iosMain: the iPhone wing" }
vars: { ios: "on" }
toggles:
  - { id: ios, label: "iPhone wing listed on the plan", var: ios }
actions:
  - id: build-android
    label: Build for Android
    do:
      - mark: { "android.corridor": good, "android.android-wing": good, "android.iphone-wing": ghost }
      - light: ["android.corridor", "android.android-wing"]
      - say: "The Android builder reads the shared corridor and the Android wing. The iPhone wing is not part of this build."
  - id: build-ios
    label: Build for iPhone
    cases:
      - when: { var: { ios: "on" } }
        do:
          - mark: { "iphone.corridor": good, "iphone.iphone-wing": good, "iphone.android-wing": ghost }
          - light: ["iphone.corridor", "iphone.iphone-wing"]
          - say: "The iPhone builder reads the shared corridor and the iPhone wing, for the device and for the simulator. The Android wing is not part of this build."
      - do:
          - mark: { "iphone.corridor": ghost, "iphone.android-wing": ghost, "iphone.iphone-wing": ghost }
          - light: []
          - say: "The plan lists no iPhone wing, so the iPhone builder never comes. It reads no room, not even the shared corridor."
script: [build-android, build-ios, "ios=off", build-ios]
try:
  - text: "Press \"Build for Android\". Look at which two rooms light up on the Android side."
    done: [build-android]
  - text: "Press \"Build for iPhone\". The corridor is read again, the iPhone wing is read now, and the Android wing is not."
    done: [build-ios]
  - text: "Switch off \"iPhone wing listed on the plan\", then press \"Build for iPhone\" again. Watch the iPhone side."
    done: ["ios=off", build-ios]
```

**What you should notice:** The shared corridor is read by every builder that exists, each wing is read only by its own builder, and with no iPhone wing on the plan the iPhone side builds nothing.

## Build the logic

### Notice

Three files sit in three folders. `Greeting.kt` is in `commonMain`. `Battery.kt` is in `androidMain`. `Clock.kt` is in `iosMain`. Two builders will work on the block: one for Android and one for the iPhone.

### Guess

Which builders read `Clock.kt` in `iosMain`?

- A. No builder, because only the shared corridor is ever read.
- B. Every builder, because every folder under the project is read.
- C. Only the iPhone builders.

Idea A forgets that a wing exists to be read. Idea B treats the folders as one pile. Idea C follows the floor plan: a wing room is open to its own builder.

### Build

1. Make one folder for everyone and one folder for each kind of phone.
2. Write the plan. It lists the wings the block really has. Each listed wing gets a builder.
3. A builder reads the corridor and its own wing, and nothing else.
4. If two builders of the same kind can share a room, such as the builder for a real iPhone and the builder for the practice phone on a computer, put that room between the corridor and their wings.
5. A wing that the plan does not list has no builder, so nobody reads its rooms.

### Name it

A **source set** is a group of Kotlin files that are compiled together for one or more targets. A target is one kind of device you build the shared code for. The folders live as directories inside `src`. The Kotlin documentation says each source set is usually stored in a directory named after it.

| Folder under `src` | Compiled for |
|---|---|
| `commonMain` | All declared targets |
| `androidMain` | The Android target only |
| `iosMain` | The iOS targets, the device and the simulator |

The documentation says `commonMain` is present in all multiplatform projects and compiles to all declared targets. It says each target has a platform source set that compiles for only that target. It says that for iOS apps you usually work with `iosMain`, which is shared among the iOS targets.

The build file is the plan. Targets are declared with calls inside a `kotlin { }` block. The Kotlin documentation shows `iosArm64()` for 64-bit iPhones and says to use `iosSimulatorArm64` as the simulator target on an Apple silicon Mac.

From the docs, not run by me. ([Discover your project](https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project.html))

An **intermediate source set** is a source set that sits between `commonMain` and some targets and holds code for only some of them. `iosMain` is one. You do not make it by hand. The Kotlin Gradle plugin has a built-in **default hierarchy template**. The documentation says that when you declare the targets `android`, `iosArm64` and `iosSimulatorArm64`, the plugin finds suitable shared source sets from the template and creates them for you.

From the docs, not run by me. ([Hierarchical project structure](https://kotlinlang.org/docs/multiplatform/multiplatform-hierarchy.html))

For Android, the Android documentation names the Gradle plugin `com.android.kotlin.multiplatform.library` as the officially supported tool for adding an Android target to a shared library module. Its example puts an `android { }` block, with a `namespace`, inside `kotlin { }`, and the Android source set is `androidMain`. Its migration section shows the older `androidTarget()` call. This leaf names no plugin version.

Here is the shape of the target part of a build file.

```kotlin
// does not compile
// Shape only: this goes inside a shared module's build.gradle.kts
kotlin {
    android {
        namespace = "com.example.shared"
    }
    iosArm64()
    iosSimulatorArm64()
}
```

The real file also needs more settings, such as `compileSdk` and `minSdk`, which the Android page's example sets. With these three targets declared, the folders `commonMain`, `androidMain` and `iosMain` all have a builder.

## Predict the output

```challenge
type: predict
run: false
id: who-compiles-ios-main
verified: not-run
code: |
  The build file declares these targets:
    android
    iosArm64
    iosSimulatorArm64

  The folders are commonMain, androidMain and iosMain.

  Which targets compile src/iosMain/kotlin/Clock.kt?
options:
  - id: a
    text: "All three targets"
    why: "You may have thought every folder is read by every builder. A wing room is read only by its own builders."
  - id: b
    text: "iosArm64 only, because that is the real iPhone"
    why: "You may have thought the practice phone has its own wing. The iPhone wing is shared by the device builder and the simulator builder."
  - id: c
    text: "iosArm64 and iosSimulatorArm64, and not android"
    correct: true
    why: "iosMain is the iPhone wing. The device builder and the simulator builder both enter it, and the Android builder does not."
output: |
  iosArm64 and iosSimulatorArm64, and not android
```

```challenge
type: predict
run: false
id: which-phones-does-this-build-reach
verified: not-run
code: |
  This generated build file is the whole kotlin block:

    kotlin {
        android {
            namespace = "com.example.shared"
        }
    }

  The folders are commonMain, androidMain and iosMain.

  Which phones can this build produce code for?
options:
  - id: a
    text: "Android and iPhone, because the folder iosMain exists"
    why: "You may have thought a folder adds a builder. The plan, which is the build file, lists the wings. A folder alone is an unused room."
  - id: b
    text: "Android only, because only an Android target is declared"
    correct: true
    why: "The plan lists one wing, so one builder comes. The iosMain room is never read, and the iPhone side has nothing to build."
  - id: c
    text: "Neither, because the plugin lines are missing"
    why: "You may have noticed that the plugin lines are not shown. This file shows only the kotlin block, and it does declare an Android target."
output: |
  Android only, because only an Android target is declared
```

```challenge
type: predict
run: false
id: who-compiles-common-main
verified: not-run
code: |
  The build file declares: android and iosArm64.

  The file src/commonMain/kotlin/Greeting.kt exists.

  Which targets compile it?
options:
  - id: a
    text: "Both, because commonMain compiles to all declared targets"
    correct: true
    why: "The shared corridor is open to every builder that exists. Each builder reads the same file and makes its own result."
  - id: b
    text: "Neither, until a copy is made in each platform folder"
    why: "You may have thought the shared folder is pasted into each wing. It is read in place, once for each build."
  - id: c
    text: "Only android, because it is listed first"
    why: "You may have thought the first wing owns the corridor. The order of the targets does not matter."
output: |
  Both, because commonMain compiles to all declared targets
```

## Tricks to remember

- Corridor for all, wing for one, plan on the wall decides who comes.
- A folder alone builds nothing. The build file lists the targets.
- `iosMain` is the room both iPhone builders share.
- Task: open a shared project in Android Studio, find the `kotlin { }` block in `build.gradle.kts`, and list every target it declares. Then list the folders under `src` and say which builders read each one.

## Common mistakes

- **Thinking the shared folder is pasted into each platform folder.** It feels that way because each phone ends up with its own result. The file is read in place, once for each build.
- **Adding a folder and expecting a build.** A folder named `iosMain` looks like an iPhone setup. The build file must declare an iOS target before anything is made for the iPhone.
- **Putting a file in the wrong wing.** A file in `androidMain` feels shared because it is in the project. Only the Android builder reads it, so the iPhone builder cannot see it.
- **Thinking the first listed target owns the shared folder.** A list has a first line, so it feels special. Every declared target reads the shared folder equally.

## Where this shows up in Android

The Android documentation says the `com.android.kotlin.multiplatform.library` plugin adds an Android target to a Kotlin Multiplatform library module, and its Android source set is `androidMain`. So in an Android Studio project the Android builder reads `commonMain` and `androidMain`, as in the sim.

A common symptom is an app that builds and runs on Android while nobody on the iPhone team can find the shared code. My own reasoning: a build file that declares only an Android target, as in the second Predict item, is one cause. The part of this lesson that explains it is the plan on the wall, which is the build file.

## Say it in an interview

**What is the standard folder layout of a Kotlin Multiplatform project?**
Folders sit under `src`, one for each source set: `commonMain` for code shared by every target, and one for each platform such as `androidMain` and `iosMain`. The Kotlin documentation says `commonMain` is in all multiplatform projects and compiles to all declared targets. Each platform source set compiles for only its own target, apart from shared ones like `iosMain`.

**What does the build file declare?**
It declares the targets, with calls inside a `kotlin { }` block, such as `iosArm64()` and `iosSimulatorArm64()`, and an `android { }` block when the Android library plugin is used. The targets decide which source sets exist and who reads which folder. Older projects use `androidTarget()`, which the Android documentation shows in its migration section.

**How can code be shared between all iOS targets without copying it?**
Put it in the intermediate source set `iosMain`. The Kotlin documentation says the default hierarchy template creates the shared source sets for you from the targets you declare. So the device builder and the simulator builder read the same files.

**What goes wrong if you add a library to `commonMain` that does not support one of your targets?**
The Kotlin documentation says common code needs to compile for every declared target. It does not describe the exact error. My own reasoning: the build for the unsupported target cannot use the library. The documentation shows the way out: add the library only to the source sets that can use it.

## Recap

1. Each folder under `src` is a source set, read only by the targets it belongs to.
2. The shared folder compiles to all declared targets, in place and never copied.
3. The build file declares the targets, so a folder without a target builds nothing.

## You can now

- Predict which targets compile a file in `commonMain`, `androidMain` or `iosMain`.
- Read a short `kotlin { }` block and say which phones it can build for.
- Explain why the shared folder is read once for each build and is not copied.
