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

<!-- ⚠️ VERIFY: (1) sim id app-house-doors has no spec in docs/sims yet (physics-designer: floor plan of an app as a house with doors for screen, files, network, logs and installed package; a thief ball tries each door; the learner places locks and sees the thief take the unguarded door). (2) Facts checked 21 Oct 2026 at developer.android.com/topic/security/best-practices: "Use HTTPS over HTTP anywhere that HTTPS is supported on the server, because mobile devices frequently connect on networks that aren't secured, such as public Wi-Fi hotspots"; "By default, files that you create on internal storage are accessible only to your app"; "logs are a shared resource and are available to an application with the READ_LOGS permission"; files on external storage are "globally readable and writable"; set android:exported to false for components not meant for other apps. The page does not use the words "attack surface". (3) NOT checked against an official page: that attackers often use scripts that try thousands of apps and that a small app's login may work elsewhere (Common mistakes; general knowledge); and that a person with a copy of the app file can open it and read what is inside (general knowledge; read developer.android.com security docs on app signing and reverse engineering before live). The READ_LOGS statement may be narrower on newer Android versions than the page's one line suggests; read the permission page before live. (4) Assumes nothing from Foundation. (5) Predict items traced by hand only. The roadmap sim row is the only visual; no diagram asset exists. -->
## The question

You are building a small notes app. A friend asks, "Why would anyone attack that? Nobody cares about your notes."

But your app holds a login, some notes and a few messages. If someone wanted them, how would they try to get them? And how can you find out before they do?

## Mental model

Think of a house with doors, windows and a spare key under the mat.

- The house is your app and the data inside it.
- Each door or window is a way in or out: the screen, the files, the network, the logs.
- The lock on a door is a protection you add.
- The burglar is anyone who wants what is inside.
- The spare key under the mat is a mistake that makes one way in simple for a stranger.

A burglar does not pick the strongest lock. He walks around the house and tries the weakest door. Making one door very strong does nothing if the window is open.

**Where the analogy stops:** A house has a few doors, and you can count them. An app has many more ways in, and some are hidden: a log line, a file in the wrong place, a copy of the app downloaded by someone you never met. Also, a burglar has to come to your street. An attacker on the internet can try thousands of apps in one night.

## See it move

<!-- SIM: app-house-doors -->

**Try this:**
1. Look at the house. Each door is one way into the app: the screen, the files, the network, the logs and the installed package.
2. Press "Send the thief". Watch which door he tries first.
3. Place a lock on one door and send the thief again. Then lock every door but one.

**What you should notice:** The thief always walks to the door with no lock, and a strong lock on one door does not stop him from using another.

## Build the logic

### Notice

Your notes app does four things. It shows notes on the screen. It saves notes in a file. It sends the notes to a server over the network. And while it works, it writes lines to a log, a running record of what the app did.

Each of those four things is a place where something goes in or comes out. Each one is a door.

### Guess

You have time to protect only one door well. What should you do?

- A. Put the strongest lock you can find on the door that feels most important.
- B. List all the doors, then check which one has no lock at all.
- C. Hide the app so nobody finds out it exists.

Idea A leaves a back door open. Idea C does not work for an app anyone can install. Idea B is how a burglar thinks, and it is how you should check your own house.

### Build

1. Draw the house: write down every place data goes in or comes out.
2. Name who might want in. Think of a real person for each door.
3. For each door, ask: how would that person try it?
4. Mark the doors that have a lock and the doors that do not.
5. Fix the unlocked door that is easiest to reach, then repeat from step 3.

You are not trying to build a wall. You are trying to leave no open door.

### Name it

- A **threat** is something that could harm your app or its data.
- An **attacker** is a person who tries to carry out a threat.
- An **attack surface** is the full list of doors an attacker could try.
- **Threat thinking**, also called threat modelling, is the habit of listing the attackers and the doors before you build the locks.

Here are four attackers to picture for a phone app:

| Attacker | The door they use |
|---|---|
| A snoop on the same network | The network |
| Another app on the same phone | Files, logs and shared parts of the app |
| A person holding the phone | The screen |
| A person with a copy of the app file | The installed package |

There is no code to run on this page. This lesson is a way of looking at an app.

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
    text: "Door 1, the screen"
    why: "You may have thought the stranger would use the app itself. But the stranger is not holding the phone. Door 1 is for a person with the phone in hand."
  - id: b
    text: "Door 2, the network"
    correct: true
    why: "The stranger is on the same Wi-Fi, so the network is the door within reach. Plain HTTP has no lock, so the stranger can try it. The Android documentation says to use HTTPS because mobile devices often use unsecured networks such as public Wi-Fi."
  - id: c
    text: "Door 3, the app's own files"
    why: "You may have picked the files because they hold the data. The Android documentation says internal files are accessible only to your app by default. That door has a lock already."
output: "Door 2, the network"
```

```challenge
type: predict
run: false
id: lock-all-but-one
verified: not-run
code: |
  Your app has four doors:
    Door 1: The screen             (locked with a phone lock)
    Door 2: The network            (locked with HTTPS)
    Door 3: The internal files     (locked, only your app can open them)
    Door 4: The logs               (open: the app prints the user's login code to the log)
  A thief tries every door. Which door does he walk through?
options:
  - id: a
    text: "Door 2, because the network is where data travels"
    why: "You may have expected the thief to go for the busiest door. The thief picks the weakest door, not the busiest one. This door has a lock."
  - id: b
    text: "Door 3, because the files hold the most data"
    why: "You may have judged by value. A burglar picks by ease. Internal files are locked to other apps."
  - id: c
    text: "Door 4, the logs"
    correct: true
    why: "It is the spare key under the mat. Every other door has a lock, so the thief takes the one that is open. The Android documentation warns that logs can be read by other apps and says not to log personal data."
output: "Door 4, the logs"
```

## Tricks to remember

- Weakest door first. Check the doors, not the locks.
- List the attackers, then list the doors.
- A strong lock on one door does not close the others.
- An app nobody would attack is a story. Someone always has a reason, or a script that tries.

## Common mistakes

- **Thinking nobody would bother to attack a small app.** It feels true because you would not care about a small app yourself. But attackers often use scripts that try thousands of apps, and a small app may hold a login that works somewhere bigger. Treat the data as worth stealing.
- **Putting all your effort on one strong lock.** It feels responsible to do one thing very well. A burglar does not test the strong lock. Check every door, and fix the unlocked one first.
- **Forgetting the doors you did not draw.** Logs and files are not on screen, so it feels as if they do not count. If data goes through them, they are doors. Draw them before you fix anything.

## Where this shows up in Android

Take the notes app. A user reads a note on a train and connects to the free Wi-Fi. If the app sends data over plain HTTP, the snoop on that network has an open door. The Android documentation says to use HTTPS wherever the server supports it, because phones often use unsecured networks such as public Wi-Fi hotspots.

Now take the log door. The documentation says logs are a shared resource that another app with the READ_LOGS permission can read, and it says to limit logging of user information. So a debug line that prints a password can be the easiest door in the whole app.

The documentation also says files on internal storage are accessible only to your app by default, while files on external storage are readable and writable by anyone. Where you save a file decides which attacker can reach it.

## Recap

1. List the attackers and the doors an attacker could try.
2. A burglar uses the weakest door, so every door matters.
3. Fix the unlocked door that is easiest to reach first.

## You can now

- Explain how an attacker thinks, using the house with doors.
- Name four attackers and the door each one would use.
- Predict which door a thief uses when every door but one is locked.
