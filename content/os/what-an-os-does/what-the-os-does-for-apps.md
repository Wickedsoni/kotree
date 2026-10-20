---
id: os/what-an-os-does/what-the-os-does-for-apps
title: What does the operating system do for my app that my code cannot do alone?
status: shadow
difficulty: easy
order: 1
requires: []
summary: The operating system shares the phone between apps and keeps them apart. Your code asks it for the screen, the storage and CPU time.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: Foundation leaves foundation/how-computers-work/what-is-an-os and what-is-a-cpu are assumed by the roadmap row but are not drafted yet. Facts checked 21 Oct 2026: source.android.com/docs/security/app-sandbox (each app gets a unique user ID and runs in its own process; the kernel enforces the sandbox), developer.android.com/guide/platform (the foundation of the Android platform is the Linux kernel), developer.android.com/media/optimize/audio-focus (only one app holds audio focus; from Android 12 the system fades out the first app when another requests focus). NOT checked against an official page: "the OS gives each app a turn on the CPU" (general OS theory, no Android page read). No sim: the roadmap row says one still diagram of three bands, motion comes in the next leaf. The diagram asset does not exist yet. Predict items were traced by hand only. -->
## The question

You are listening to music. You open a video app and press play. The music fades out and the video takes over the sound. Nobody asked you to pick one.

Your app's code never said "stop the other app". Something else made that call. What is it, and what does it do for every app on the phone?

## Mental model

Think of an apartment building with a building manager.

- The building is the phone.
- The tenants are the apps.
- The rooms are memory, the places where a tenant keeps things while working.
- The keys are the right to use the screen, the camera, the speaker or the storage.
- The electricity is CPU time, the power that lets a tenant do any work at all.
- The manager is the operating system.

A tenant cannot walk into the electrical room and flip switches. A tenant asks the manager. The manager hands out rooms, keys and electricity so tenants do not fight over them. The manager also makes sure one tenant cannot open another tenant's door.

**Where the analogy stops:** A real manager is a person who walks around and can bend a rule. The operating system is also code. It does not decide by mood, it follows fixed rules. And it does not live in a separate office. Every tenant's request goes through it, thousands of times a second, and you never see it happen.

## Build the logic

### Notice

Picture three bands, one on top of the other.

- The top band holds the apps: your app, a music app, a camera app.
- The middle band holds the operating system.
- The bottom band holds the hardware: the screen, the speaker, the storage chip and the processor.

No app touches the bottom band directly. Everything passes through the middle band.

### Guess

Your app wants to save a photo and the music app is also saving a file at the same moment. Who should decide who goes first?

- A. Each app, by checking whether the other app is busy.
- B. Whoever started first keeps the storage until it is done.
- C. One shared helper that every app must ask, so nobody collides.

Idea A cannot work. Each app is written by a different person who has never met the others. Idea B makes a slow app block everyone. Idea C is the building manager, and it is what phones do.

### Build

1. Give every app its own private room, so one app cannot read another app's memory.
2. Put one manager between the apps and the hardware.
3. Make apps ask the manager for everything shared: the screen, the speaker, the storage.
4. Let the manager hand out electricity in small turns, so every app gets some CPU time.

An app that follows this does less work by itself. It does not need to know what other apps exist.

### Name it

- The **operating system**, or **OS**, is the program that shares the hardware between apps and keeps the apps apart.
- A **process** is one running program with its own private memory. In the building, it is a tenant in their own flat.
- **CPU time** is the share of the processor an app gets to run on. The OS hands it out in small turns.

| Building | Official name | Phone example |
|---|---|---|
| The manager | Operating system | Android |
| A tenant in their own flat | A process | Your app, running |
| Electricity | CPU time | Your app gets a turn to run |
| A key | Permission to use a device | The speaker, the camera |

There is no program to run on this page. This lesson is a picture of who does what.

## Predict the output

```challenge
type: predict
run: false
id: who-does-the-job
verified: not-run
code: |
  Four jobs. Who does each one, the OS or your code?
    1. Decide which app gets the next turn on the CPU.
    2. Turn a photo into a list of pixels to show.
    3. Stop your app from reading another app's memory.
    4. Run the lambda you wrote inside your app.
  Which list is correct for jobs 1, 3 and 4?
options:
  - id: a
    text: "1 is the OS, 3 is the OS, 4 is your code"
    correct: true
    why: "The manager hands out electricity and keeps flats apart. The work a tenant does inside their own flat, like running your lambda, belongs to the tenant."
  - id: b
    text: "1 is your code, 3 is the OS, 4 is your code"
    why: "You may have thought each app picks its own turn. If apps picked their own turns, any app could keep the electricity forever. The manager picks."
  - id: c
    text: "1 is the OS, 3 is your code, 4 is the OS"
    why: "You may have thought apps guard their own memory. A tenant cannot lock a door that the manager controls. The OS keeps the flats apart."
output: "1 is the OS, 3 is the OS, 4 is your code"
```

```challenge
type: predict
run: false
id: remove-the-manager
verified: not-run
code: |
  Take the middle band away. Apps now talk to the hardware directly.
  Two apps both want to play sound at the same moment.
  What happens?
options:
  - id: a
    text: "Nothing changes, each app waits politely"
    why: "You may have trusted every app to wait. Apps are written by strangers, so nothing makes them wait. Without a manager, nobody sits between them."
  - id: b
    text: "Both write to the speaker at once and nobody controls the mix"
    correct: true
    why: "Without the manager nobody hands out the key. Two tenants use the same room at the same time and get in each other's way."
  - id: c
    text: "The phone refuses to start"
    why: "You may have expected a clean stop. The hardware has no rules of its own about sharing. Missing rules usually show up as a mess, not as a refusal."
output: "Both write to the speaker at once and nobody controls the mix"
```

## Tricks to remember

- Apps ask. The OS decides. The hardware obeys.
- Three bands, top to bottom: apps, OS, hardware. Nothing skips the middle band.
- Rooms, keys, electricity: memory, permission, CPU time.

## Common mistakes

- **Thinking the app talks to the screen or the camera by itself.** It feels true because your code looks like it draws a button. Your code asks, and the OS does the drawing work with the hardware.
- **Thinking the OS is the same thing as the hardware.** The phone feels like one object. The OS is a program that runs on the hardware, like your app, which is a program that runs on the OS.
- **Thinking the OS is always strict.** Some sharing rules rely on apps behaving. Audio focus on older Android versions is one example, and the next section says how it changed.

## Where this shows up in Android

Go back to the music and the video app. Android uses the Linux kernel as the foundation of the platform, and the kernel is the part of the OS that sits right on top of the hardware. Android also gives each app its own user ID and its own process, so one app cannot reach into another app's memory. That is the private flat.

For sound, Android has a rule called audio focus. The Android documentation says only one app can hold audio focus at a time. On Android 11 and lower, the system could not force an app to stop. It depended on the app obeying. From Android 12, the documentation says the system forces the first app's audio to fade out when another app asks for focus.

So when your music fades out, the manager made it happen. That is a symptom you can now read: if two apps clash, ask what the OS hands out and who is allowed to ask.

## Recap

1. The operating system sits between apps and hardware and shares the hardware.
2. Each app gets its own private process, so apps stay apart.
3. Apps ask the OS for the screen, storage and CPU time.

## You can now

- Explain what an operating system does for an app, using the building manager.
- Sort jobs such as sharing the CPU or running your lambda into "OS does it" or "my code does it".
- Predict what goes wrong when two apps use the speaker with no manager in between.
