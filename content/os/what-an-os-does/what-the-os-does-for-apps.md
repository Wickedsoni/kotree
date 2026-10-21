---
id: os/what-an-os-does/what-the-os-does-for-apps
title: What does the operating system do for my app that my code cannot do alone?
status: shadow
difficulty: easy
order: 1
requires: []
summary: The operating system shares the phone between apps and keeps them apart. Your code asks it for the screen, storage and CPU time.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/guide/platform ("The foundation of the Android platform is the Linux kernel"; from Android 5.0 each app runs in its own process with its own instance of ART). https://developer.android.com/media/optimize/audio-focus (only one app can hold audio focus at a time; from Android 12 the system forces the playing app to fade out when another app requests focus; before Android 12 the system cannot stop an app that keeps playing loudly). The unique user ID per app comes from https://source.android.com/docs/security/app-sandbox, which could not be opened from here; the sentence was read in a search excerpt of that page. General knowledge, not from an Android page: what a process is, CPU time shared in turns, the four resources an OS shares. Interview data: docs/practice/practice-os.md. It says problems: none for this leaf, so there is no Practise on these list. No sim: the roadmap row says one picture of three bands. Predict items are traced by hand. -->

## The question

You are listening to music. You open a video app and press play. On a recent Android phone, the music often fades out and the video takes over the sound. Nobody asked you to pick one, and your app's code never said "stop the other app". So what makes that call, and what does it do for every app on the phone?

## Mental model

Think of an apartment building with a building manager.

- The building is the phone.
- The tenants are the apps.
- The rooms are memory, the places where a tenant keeps things while working.
- The keys are the right to use the screen, the speaker, the camera or the storage.
- The electricity is CPU time, the power that lets a tenant do any work at all. The CPU is the part of the phone that carries out instructions.
- The manager is the operating system.

A tenant cannot walk into the electrical room and flip switches. A tenant asks the manager. The manager hands out rooms, keys and electricity so tenants do not fight over them. The manager also makes sure one tenant cannot open another tenant's door.

**Where the analogy stops:** A real manager is a person who can bend a rule. The operating system is code, and it follows fixed rules. It also does not sit in a separate office. Every tenant's request passes through it, and you never see it happen.

## Build the logic

### Notice

Picture three bands, one on top of the other.

- The top band holds the tenants: your app, a music app, a camera app.
- The middle band holds the manager.
- The bottom band holds the building's equipment: the screen, the speaker, the storage chip and the processor.

No tenant touches the bottom band directly. Everything passes through the middle band.

### Guess

Your app wants to save a photo, and the music app is saving a file at the same moment. Who should decide who goes first?

- A. Whoever started first keeps the storage until it is done.
- B. One shared helper that every app must ask, so nobody collides.
- C. Each app, by checking whether the other app is busy.

Idea C cannot work. Each app is written by a different person who has never met the others. Idea A lets one slow app block everyone. Idea B is the building manager, and it is what phones do.

### Build

1. Give every tenant its own private room, so one tenant cannot read another tenant's room.
2. Put one manager between the tenants and the equipment.
3. Make tenants ask the manager for everything shared: the screen, the speaker, the storage.
4. Let the manager hand out electricity in small turns, so every tenant gets some.

A tenant that follows this does less work by itself. It does not need to know which other tenants exist.

### Name it

- The **operating system**, or **OS**, is the program that shares the hardware between apps and keeps the apps apart. Hardware means the physical parts of the phone.
- A **process** is one running program with its own private memory (general knowledge). In the building, it is a tenant in their own room.
- **CPU time** is the share of the processor an app gets to run on. The OS hands it out in small turns (general knowledge).

| Building | Official name | Phone example |
|---|---|---|
| The manager | Operating system | Android |
| A tenant in their own room | Process | Your app, running |
| Electricity | CPU time | Your app gets a turn to run |
| A key | Permission to use a device | The speaker, the camera |

There is no program to run on this page. It is a picture of who does what.

## Predict the output

```challenge
type: predict
run: false
id: who-does-the-job
verified: not-run
code: |
  Four jobs. Who does each one, the OS or your code?
    1. Decide which app gets the next turn on the CPU.
    2. Stop your app from reading another app's memory.
    3. Run the lambda (a small block of code) that you wrote inside your app.
    4. Decide what text your own screen should show.
  Which list is correct?
options:
  - id: a
    text: "1 is your code, 2 is the OS, 3 is your code, 4 is your code"
    why: "You may have thought each app picks its own turn. If apps picked their own turns, any app could keep the electricity for ever. The manager picks."
  - id: b
    text: "1 is the OS, 2 is the OS, 3 is your code, 4 is your code"
    correct: true
    why: "The manager hands out electricity and keeps rooms apart. The work a tenant does inside their own room, like running your lambda or choosing your text, belongs to the tenant."
  - id: c
    text: "1 is the OS, 2 is your code, 3 is the OS, 4 is your code"
    why: "You may have thought apps guard their own memory. A tenant cannot lock a door that the manager controls. The OS keeps the rooms apart."
output: |
  1 is the OS, 2 is the OS, 3 is your code, 4 is your code
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
    text: "Both send sound to the speaker at once, and nothing decides how they mix"
    correct: true
    why: "Without the manager nobody hands out the key. Two tenants use the same room at the same time and get in each other's way."
  - id: b
    text: "Each app waits its turn politely"
    why: "You may have trusted every app to wait. Apps are written by strangers, so nothing makes them wait when no manager sits between them."
  - id: c
    text: "The louder app wins and the other goes silent"
    why: "You may have expected a winner to emerge. Without the manager nothing picks one, so both push sound at the speaker and the result is a mess, not a tidy winner."
output: |
  Both send sound to the speaker at once, and nothing decides how they mix
```

## Tricks to remember

- Apps ask, the OS decides, the hardware obeys.
- Three bands from top to bottom: apps, OS, hardware. Nothing skips the middle band.
- Rooms, keys, electricity: memory, permission, CPU time.

## Common mistakes

- **Thinking the app talks to the screen or the camera by itself.** It feels true because your code looks like it draws a button. Your code asks, and the OS works with the hardware.
- **Thinking the OS is the same thing as the hardware.** The phone feels like one object. The OS is a program that runs on the hardware, as your app is a program that runs on the OS.
- **Thinking the OS always forces apps to behave.** Some sharing depends on apps cooperating. Audio on older Android versions is one case, and the Android section below says how it changed.

## Where this shows up in Android

Go back to the music and the video app. The Android documentation says the foundation of the platform is the Linux kernel, which is the core part of the OS that sits right on top of the hardware. It also says that from Android 5.0 each app runs in its own process. The Android security documentation adds that every app gets its own user ID, which is a number the system uses to tell apps apart. That is the private room.

For sound, Android has a rule called audio focus. The documentation says only one app can hold audio focus at a time. It also says that before Android 12, the system could not stop an app that kept playing loudly after losing focus. From Android 12, it says the system forces the playing app to fade out when another app requests focus. The page lists conditions, for example that the playing app is a media or game app and the new app asks for full focus, so it does not happen in every case.

So when your music fades out, the manager often made it happen. If two apps clash, ask what the OS hands out and who is allowed to ask.

## Say it in an interview

**What would happen if two apps wrote to the screen or the speaker directly, with no operating system?**
Nothing would decide who goes first, so the apps would get in each other's way. The OS sits between the apps and the hardware, takes the requests and shares the device. The trade-off is that apps must ask instead of grabbing.

**Which resources does the operating system share between programs?**
This answer is general knowledge. It shares CPU time, memory, files on storage and devices such as the screen and speaker. In each case many programs want one thing, and the OS keeps them from colliding.

**How does Android keep one app out of another app's memory?**
Each app runs in its own process, and the system gives it its own user ID. The Android documentation says that from Android 5.0 each app runs in its own process. The user ID is how the system tells the apps apart. This is how the private room works.

**Why can the sound of one app fade out when you start another?**
Android has audio focus, and only one app can hold it at a time. From Android 12 the system itself can force the playing app to fade out when another app requests focus, under conditions, for example when the playing app is a media or game app. Before that, the system could not stop an app that kept playing, so it depended on apps being polite. That shows the difference between a rule the OS enforces and a rule apps agree to follow.

## Recap

1. The operating system sits between apps and hardware and shares the hardware between them.
2. Each Android app runs in its own process, so apps stay apart.
3. Apps ask the OS for the screen, storage and CPU time instead of taking them.

## You can now

- Sort jobs such as sharing the CPU or running your lambda into "OS does it" or "my code does it".
- Predict what goes wrong when two apps use the speaker with no manager in between.
- Explain what the operating system does for an app, using the building manager.
