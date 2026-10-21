---
id: security/threat-thinking/who-would-attack-my-app
title: How do I think like someone who wants to attack my app?
status: shadow
difficulty: easy
order: 1
requires: []
summary: To protect an app, list who might want its data and which door they would try. Attackers go for the weakest door, not the strongest lock.
video: ""
publishedOn:
updatedOn:
sims: [app-house-doors]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026): https://developer.android.com/privacy-and-security/security-tips (use HTTPS over HTTP because mobile devices often join unsecured networks such as public Wi-Fi hotspots; files on internal storage are by default accessible only to your app; files on external storage, such as SD cards, are globally readable and writable; logs are a shared resource available to an app with the READ_LOGS permission). https://developer.android.com/privacy-and-security/risks/log-info-disclosure (since Android 4.1 only privileged system apps can be granted READ_LOGS, but some pre-loaded apps on some devices declare it, so logging to logcat is discouraged; do not log sensitive data). The page does not use the words attack surface or threat modelling, so those are general knowledge. Also general knowledge: scripts that try many apps, and what a person who holds a copy of the app file can do with it. Interview data: docs/practice/interview-security.md. Sim app-house-doors is new, see docs/sims/needed-ids.md. Predict items are traced by hand. -->

## The question

You are building a small notes app. A friend asks, "Why would anyone attack that? Nobody cares about your notes." But the app holds a login, some notes and a few messages. If someone wanted them, how would they try to get them? And how can you find out before they do?

## Mental model

Think of a house with doors, windows and a spare key under the mat.

- The house is your app and the data inside it.
- Each door or window is a way in or out: the screen, the files, the network, the logs, the installed app file.
- The lock on a door is a protection you add.
- The burglar is anyone who wants what is inside.
- The spare key under the mat is a mistake that makes one way in simple for a stranger.

A burglar does not pick the strongest lock. A burglar walks around the house and tries the weakest door. A very strong lock on one door does nothing if the window is open.

**Where the analogy stops:** A house has a few doors, and you can count them. An app has more ways in, and some are hidden: a line in a log, a file saved in the wrong place, a copy of the app given to someone you never met. Also, a real burglar has to come to your street. A burglar on the internet can try thousands of apps in one night (general knowledge).

## See it move

<!-- SIM: app-house-doors -->

**Try this:**
1. Look at the house. Each door is one way into the app: the screen, the files, the network, the logs and the installed app file.
2. Press "Send the burglar". Watch which door is tried first.
3. Place a lock on one door and send the burglar again. Then lock every door but one.

**What you should notice:** The burglar always walks to the door with no lock, and a strong lock on one door does not stop the use of another.

## Build the logic

### Notice

Your notes app does four things. It shows notes on the screen. It saves notes in a file. It sends the notes to a server over the network. And while it works, it writes lines to a log, a running record of what the app did.

Each of those four things is a place where something goes in or comes out. Each one is a door.

### Guess

You have time to protect only one door well. What should you do?

- A. Hide the app so nobody finds out it exists.
- B. Put the strongest lock you can find on the door that feels most important.
- C. List all the doors, then check which one has no lock at all.

Idea A does not work for an app anyone can install. Idea B leaves a back door open. Idea C is how a burglar thinks, and it is how you should check your own house.

### Build

1. Draw the house: write down every place data goes in or comes out.
2. Name who might want in. Think of a real person for each door.
3. For each door, ask how that person would try it.
4. Mark the doors that have a lock and the doors that do not.
5. Fix the unlocked door that is easiest to reach, then repeat from step 3.

You are not trying to build a wall. You are trying to leave no open door.

### Name it

- A **threat** is something that could harm your app or its data.
- An **attacker** is a person who tries to carry out a threat.
- The **attack surface** is the full list of doors an attacker could try (general knowledge).
- **Threat thinking**, also called threat modelling, is the habit of listing the attackers and the doors before you build the locks (general knowledge).

Four more words appear in the Predict section.

- **HTTP** is the plain way an app talks to a server. Anyone on the same network can read it (general knowledge).
- **HTTPS** is the same talk with a lock on it (general knowledge).
- **Internal storage** is the private area where an app saves its files. By default only that app can open them.
- **External storage** is a shared area, such as an SD card. Other apps can read and write files there.

Here are four attackers to picture for a phone app. The doors are my own reasoning, based on the Android pages in the sources.

| Attacker | The door they use |
|---|---|
| A snoop on the same network | The network |
| Another app on the same phone | Files saved where any app can read them |
| A person holding the phone | The screen |
| A person with a copy of the app file | The installed app file |

There is no code to run on this page. It is a way of looking at an app.

## Predict the output

```challenge
type: predict
run: false
id: stranger-on-wifi
verified: not-run
code: |
  Your app has three doors:
    Door 1: The screen. Only the person holding the phone can use it.
    Door 2: The network. The app sends login details to the server over plain HTTP.
    Door 3: The app's own files in internal storage.
  A stranger sits on the same public Wi-Fi as your user.
  Which door does the stranger use?
options:
  - id: a
    text: "Door 2, the network"
    correct: true
    why: "The stranger is on the same Wi-Fi, so the network is the door within reach. Plain HTTP has no lock. The Android documentation says to use HTTPS because phones often join unsecured networks such as public Wi-Fi."
  - id: b
    text: "Door 1, the screen"
    why: "You may have thought the stranger would use the app itself. But the stranger is not holding the phone. Door 1 is for a person with the phone in hand."
  - id: c
    text: "Door 3, the app's own files"
    why: "You may have picked the files because they hold the data. The Android documentation says internal files are accessible only to your app by default. That door has a lock already."
output: |
  Door 2, the network
```

```challenge
type: predict
run: false
id: lock-all-but-one
verified: not-run
code: |
  Your app has four doors:
    Door 1: The screen          (locked with a phone lock)
    Door 2: The network         (locked with HTTPS)
    Door 3: The internal files  (locked, only your app can open them)
    Door 4: Notes saved to external storage (open)
  A burglar tries every door. Which door does he walk through?
options:
  - id: a
    text: "Door 2, because the network is where data travels"
    why: "You may have expected the burglar to go for the busiest door. A burglar picks the weakest door, not the busiest one. This door has a lock."
  - id: b
    text: "Door 4, the notes on external storage"
    correct: true
    why: "It is the spare key under the mat. Every other door has a lock, so the burglar takes the one that is open. The Android documentation says files on external storage are globally readable and writable."
  - id: c
    text: "Door 3, because the files hold the most data"
    why: "You may have judged by value. A burglar picks by ease. Internal files are locked to other apps."
output: |
  Door 4, the notes on external storage
```

## Tricks to remember

- Weakest door first. Check the doors, not the locks.
- List the attackers, then list the doors.
- A strong lock on one door does not close the others.
- "Nobody would bother" is a story. Someone may have a reason, or a script that tries many apps.

## Common mistakes

- **Thinking nobody would bother to attack a small app.** It feels true because you would not care about a small app yourself. My own reasoning: a script can try many apps without anyone caring about yours, and a login saved in your app may also work somewhere bigger. Treat the data as worth stealing.
- **Putting all your effort on one strong lock.** It feels responsible to do one thing very well. A burglar does not test the strong lock. Check every door, and fix the unlocked one first.
- **Forgetting the doors you did not draw.** Logs and files are not on the screen, so it feels as if they do not count. If data goes through them, they are doors. Draw them before you fix anything.

## Where this shows up in Android

Take the notes app. A user reads a note on a train and joins the free Wi-Fi. If the app sends data over plain HTTP, the snoop on that network has an open door. The Android documentation says to use HTTPS wherever the server supports it, because phones often join unsecured networks such as public Wi-Fi hotspots.

Now take the files door. Android keeps an app's internal storage closed to other apps by default. The documentation says files on external storage are readable and writable by anyone. Where you save a file decides which attacker can reach it.

Now take the log door. A log line is written to logcat, the phone's shared log. The documentation says that since Android 4.1 only privileged system apps can be granted permission to read it, but some apps that come pre-installed on some devices ask for it. So it tells you not to log sensitive data. A debug line that prints a password can be the easiest door in the app.

## Say it in an interview

**Who might attack a mobile app, and how?**
I would name four. A snoop on the same network uses the network. Another app on the phone looks for files saved where any app can read them. A person holding the phone uses the screen. A person with a copy of the app file can look inside it (general knowledge). Each attacker has a different door, so I list the attackers first.

**Why does an attacker go for the weakest door, not the strongest lock?**
The attacker wants the data, not a fight with the best lock. One open door is enough, so a strong lock on another door does not help. The trade-off is that protecting everything costs time. That is why I list the doors and fix the easiest open one first.

**What is threat modelling and why do it early?**
It is the habit of listing who might attack, what they want and which doors they would try, before building protections. Doing it early shows an unlocked door while it is cheap to fix. This is general knowledge, and formal methods with fixed checklists exist for bigger teams.

**Can the app trust checks that run inside the app?**
General knowledge: not for anything that matters. A person with a copy of the app can look at and change what runs on their own device. So a rule such as "only paid users may do this" must also be checked on the server. The trade-off is extra work on the server, which is worth it for important rules.

## Recap

1. List the attackers and the doors each one could try.
2. A burglar uses the weakest door, so every door matters.
3. Fix the unlocked door that is easiest to reach first.

## You can now

- Pick the door a stranger on the same Wi-Fi would use.
- Predict which door a burglar uses when every door but one is locked.
- Explain how an attacker thinks, using the house with doors.
