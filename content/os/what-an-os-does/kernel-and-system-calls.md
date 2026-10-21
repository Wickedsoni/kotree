---
id: os/what-an-os-does/kernel-and-system-calls
title: How does my app ask the operating system to use the hardware for it?
status: live
difficulty: easy
order: 2
requires: [os/what-an-os-does/what-the-os-does-for-apps]
summary: Your app cannot touch the hardware itself. It passes a request through one doorway, and the kernel decides whether to do it.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---


## The question

Your app says "read this file", and the text appears. Your code never moved the disk's parts or told the storage chip what to do. So who did? And why is your app not allowed to do it itself?

## Mental model

Think of a bank counter behind glass.

- You are the app. You stand in the lobby.
- The vault holds the valuable things: the files, the camera, the network card. Those are the hardware.
- A slip is your request, written on a fixed form.
- The slot in the glass is the one doorway. Every slip goes through it.
- The staff in the vault are the **kernel**.

You do not walk into the vault. You write a slip and push it through the slot. The staff read it, decide if they will do it, and push an answer back. The answer may be "done" or "refused".

**Where the analogy stops:** A teller can chat, bend a rule, or take a verbal request. The kernel takes only the exact forms it knows. The glass is not a building feature either. The processor itself enforces it (general knowledge), so a program cannot climb over. Also, a bank has one counter. A phone runs many apps, and each passes slips through the same doorway at once.

## Build the logic

### Notice

Look at these four lines of an app.

1. Add 2 and 3 and call the result total.
2. Read the file "notes.txt" into a text.
3. Print the text.
4. Open a network connection to a server.

Line 1 uses only the processor's own sums. Lines 2, 3 and 4 need something from the vault: storage, a screen or log, and a network card.

### Guess

Who should be allowed to touch the vault?

- A. Every app, directly, so it is fast.
- B. Only trusted apps, which the phone recognises by their name.
- C. Nobody directly. Every request goes through one doorway where it can be checked.

Idea A fails because one buggy app could overwrite another app's files. Idea B fails because a name can be faked, and it still lets trusted apps break things by accident. Idea C is the glass counter.

### Build

1. Split the world in two. One side is where apps run, with limited rights. The other side is where the kernel runs, with full rights over the hardware.
2. Put the wall between them in the processor, not only in software (general knowledge). A normal program cannot step over it.
3. Cut exactly one doorway through the wall. Every request must go through it.
4. Give requests a fixed form: a number for the kind of job, such as read, write or open, and the details.
5. At the doorway, the kernel checks who is asking and whether they have the right. Then it either does the job or refuses with an error code.
6. The app gets the answer back and carries on. The app must be ready for a refusal.

### Name it

The **kernel** is the core of the operating system. It runs with full rights over the hardware (general knowledge). On Linux the kernel is called the Linux kernel, and the Android documentation says the foundation of the Android platform is the Linux kernel.

Your app runs in **user mode**, which is a limited setting of the processor. The kernel runs in **kernel mode**, which is the full-rights setting. The two names come from general knowledge about processors.

A **system call** is a request from a program to the kernel through the one doorway. The Linux manual page says the system call is the fundamental interface between an application and the Linux kernel. The same page says programs usually do not make system calls directly but through wrapper functions in a library. A **wrapper function** is a small ready-made function that makes the call for you.

Kotlin never shows you the doorway. Library calls such as reading a file go through the runtime (the program that runs your Kotlin code) and a library, and one of them makes the system call (general knowledge). The code below has one line that stays on the processor's own side and one that has to ask.

```kotlin
import java.io.File

fun main() {
    val total = 2 + 3
    println(total)
    println(File("missing.txt").exists())
}
```

If no file called `missing.txt` is in the folder where the program runs, it prints `5`, then `false`. The sum never leaves the processor. The `exists()` call needs the answer from the file system, which belongs to the kernel, so the runtime asks it (general knowledge).

| Bank | Official name | Phone example |
|---|---|---|
| The vault staff | Kernel | The Linux kernel under Android |
| The lobby | User mode | Your app's code |
| The slip | System call | "open this file" |
| "Refused" | Error code | "permission denied" or "no such file" |

## Predict the output

```challenge
type: predict
run: false
id: which-lines-ask-the-os
verified: not-run
code: |
  1. total = 2 + 3
  2. text = read the file "notes.txt"
  3. print the text
  4. open a network connection to a server
  Which lines end in a request to the operating system?
options:
  - id: a
    text: "Lines 2 and 4 only"
    why: "You may have counted only the obvious hardware. Printing also sends text out to a screen, a log or a file, which the OS owns."
  - id: b
    text: "Lines 2, 3 and 4"
    correct: true
    why: "Line 1 is only a sum, which the processor does alone. Files, output and the network sit in the vault, so each needs a slip through the slot."
  - id: c
    text: "All four lines"
    why: "You may have thought the OS is behind everything. A sum on the processor's own numbers stays in the lobby."
output: |
  Lines 2, 3 and 4
```

```challenge
type: predict
run: false
id: asking-for-a-missing-file
verified: not-run
code: |
  The app asks to open "missing.txt".
  No file has that name.
  What comes back through the slot?
options:
  - id: a
    text: "The OS makes an empty file and hands it over"
    why: "You may have thought the staff always say yes. The Linux manual says open creates a file only if the request asked for that (O_CREAT). A plain open of a missing file does not."
  - id: b
    text: "The OS stops the whole phone"
    why: "You may have pictured a failed request as a crash of the building. One refused slip is a normal answer, and everything else keeps running."
  - id: c
    text: "An error answer that the program can read and handle"
    correct: true
    why: "The staff push back a refusal on the slip. The Linux manual lists ENOENT, 'the named file does not exist'. Your code must check for it."
output: |
  An error answer that the program can read and handle
```

```challenge
type: predict
run: false
id: a-file-you-have-no-right-to
verified: not-run
code: |
  The app asks to open a file that belongs to another app.
  What does the kernel do?
options:
  - id: a
    text: "It refuses with a 'permission denied' error"
    correct: true
    why: "At the slot the staff check who is asking. The Linux manual lists EACCES, 'the requested access to the file is not allowed'."
  - id: b
    text: "It hands the file over because the app asked politely"
    why: "You may have thought a polite request is enough. The staff check the right to the vault item, not the manners."
  - id: c
    text: "It shows a pop-up and waits for the user to tap Allow"
    why: "You may have mixed this up with Android permission pop-ups, which exist for some features such as the camera. A file rule of this kind is checked by the kernel with no pop-up."
output: |
  It refuses with a 'permission denied' error
```

## Tricks to remember

- Slip, slot, vault. The app writes, the kernel does.
- One doorway, a fixed form, and a check at the slot.
- A system call can say no, so the app must check the answer.
- Sums stay in the lobby. Files, output and the network go through the slot.

## Common mistakes

- **Thinking the app runs straight on the hardware.** The screen shows your code drawing a button, so it feels direct. Your code asks and the kernel does the work. Look for the request in the path.
- **Thinking a system call is another ordinary function call.** It looks like one in the code. It crosses a wall, so it costs more than a normal call (general knowledge). Make fewer, bigger requests.
- **Ignoring the error that comes back.** A refusal feels rare. Missing files and missing rights are everyday cases, so read the answer.
- **Thinking the kernel is the whole operating system.** The words get used together. The kernel is the core part. The apps, services and settings around it are also part of what you use.

## Where this shows up in Android

The symptom is an error that says an app was not allowed. Two common forms are a "permission denied" message when opening a file, and a `SecurityException`, which Java and Android throw when a request is refused (general knowledge).

The Android documentation gives an example. Its Android 14 page says the `SCHEDULE_EXACT_ALARM` permission is required to set exact alarms with calls like `setExact()`, or a `SecurityException` will be thrown. Your app asked for something, and the system said no because the right was missing. The page does not say whether the kernel or an Android system service made this check.

The Android security page adds why files behave this way. Android gives each app its own user ID and uses it to set up a kernel-level sandbox, and by default apps have limited access to the operating system. So a request to open another app's file is checked by the kernel. That last step is my own reasoning from those sentences. Which layer produces which kind of refusal is not stated on the pages. The lesson is that when you see an access error, ask which request was refused and what right it lacked.

## Say it in an interview

**What happens when user code makes a system call?**
This is general knowledge. The program runs a special processor instruction that switches from user mode to kernel mode and jumps to a fixed entry point in the kernel. The kernel checks the request, does the work, and switches back to user mode with the result. On 64-bit ARM phones, that instruction is called svc.

**Why is a system call more expensive than a normal function call?**
It crosses the wall between user mode and kernel mode. The processor must switch modes and the kernel must check the request, even if the actual work is small. This is general knowledge. The trade-off is safety: the wall is what keeps one app from damaging another.

**Why do buffered streams read and write in big chunks?**
A system call has a fixed cost, so one call per byte wastes time. A buffered stream collects many bytes in the app's own memory and asks the kernel once for a larger block. This is general knowledge and my own reasoning: fewer, bigger requests are cheaper.

**An app asks to open a file it has no right to. What should it do?**
Expect a refusal and handle it. The Linux manual lists an error for "access not allowed" and one for "no such file". The app should show a clear message or pick another action, not crash or carry on as if it had the data.

## Recap

1. Apps run in a limited setting and cannot touch the hardware directly.
2. A system call is a request through one doorway to the kernel.
3. The kernel checks the request and can refuse, so apps must handle errors.

## You can now

- Sort lines of code into "stays on the processor" and "needs a system call".
- Predict what comes back when an app asks for a missing file or one it may not open.
- Explain why a system call costs more than an ordinary function call.
