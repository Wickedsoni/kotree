---
id: foundation/how-an-app-is-born/how-an-app-gets-installed
title: How does an app get from a store onto my phone?
status: live
difficulty: easy
order: 32
requires: [foundation/how-an-app-is-born/what-happens-when-i-build]
summary: The store packs an app for your phone, your phone checks its seal, and then it sets the app up in a private space.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [parcel-to-phone]
playground: false
fruit: false
upnext: false
---


## The question

You tap Install on a store page. A few seconds later there is a new icon on your phone. The store did not send all the files the app maker works with, and your phone did not build the app itself. So what travelled, and what did your phone do with it before the icon appeared?

## Mental model

Think of posting a sealed parcel.

- The master carton is the big box the maker sends to the shop.
- The shop opens the master carton and packs a parcel for your address. The parcel has one main box and a few small extra boxes.
- The picture sheets and the printed words in the parcel are the images and the text for your phone's screen and language.
- The label card inside the parcel lists what it holds.
- The delivery slip says what your phone is like, such as its screen and its language.
- The seal on the parcel shows who packed it.
- The post office carries the parcel to you. On a phone, that is the internet.
- Your front door is where you check the seal, before you open anything.
- Your home has a locked room for each app. A key fits one room only.

**Where the analogy stops:** A real parcel is a thing that moves. An app is a copy of files, and the store sends a copy to each phone. Also, you check a real seal with your eyes, but a phone compares digital marks. And a real shop does not pack a new box for each customer, but Google Play does pack one for each kind of phone.

## See it move

```sim
id: parcel-to-phone
template: flow
title: A sealed parcel from the shop to a phone
intro: The master carton sits on the shop shelf. The parcel will be packed for a phone with a small screen and English. The seal is on.
stations:
  - { id: shelf, label: Shop shelf }
  - { id: post, label: "Post office (the internet)" }
  - { id: door, label: "Front door: seal check" }
  - { id: room, label: Locked room }
items:
  - { id: parcel, label: Master carton, at: shelf }
vars: { big: "off", seal: "on" }
toggles:
  - { id: big, label: Big screen and Hindi phone, var: big }
  - { id: seal, label: Seal on the parcel, var: seal }
actions:
  - id: send
    label: Send the parcel
    cases:
      - when: { at: { parcel: shelf }, var: { seal: "off" } }
        do:
          - move: { parcel: door }
          - label: { parcel: "No seal" }
          - mark: { parcel: bad }
          - light: [ door ]
          - say: "The parcel has no seal. Android requires all APKs to be signed before they are installed, so the check at the front door stops it. It never reaches a locked room."
      - when: { at: { parcel: shelf }, var: { big: "on" } }
        do:
          - move: { parcel: room }
          - label: { parcel: "Base, big screen, Hindi" }
          - mark: { parcel: good }
          - light: [ room ]
          - say: "The shop works out what this phone is like and packs a parcel for it: a base box, a big-screen box and a Hindi words box. The parcel is sealed and delivered, the seal passes the check at the door, and the app is unpacked into its own locked room."
      - when: { at: { parcel: shelf } }
        do:
          - move: { parcel: room }
          - label: { parcel: "Base, small screen, English" }
          - mark: { parcel: good }
          - light: [ room ]
          - say: "The shop works out what this phone is like and packs a parcel for it: a base box, a small-screen box and an English words box. The parcel is sealed and delivered, the seal passes the check at the door, and the app is unpacked into its own locked room."
    refuse: The parcel has already left the shelf. Press "Take the parcel back" first.
  - id: back
    label: Take the parcel back
    do:
      - move: { parcel: shelf }
      - label: { parcel: Master carton }
      - mark: { parcel: plain }
      - light: [ shelf ]
      - say: The parcel goes back to the shop shelf as the master carton.
  - id: look
    label: Look in the locked room
    cases:
      - when: { at: { parcel: room } }
        do:
          - mark: { parcel: picked }
          - say: "The room belongs to this app alone, and its key fits this room only. In Android each app gets its own ID number, and only that ID can reach the app's files."
    refuse: The parcel is not in a locked room yet. Send it first, with the seal on.
script: [ send, back, "big=on", send, look ]
try:
  - text: "Press \"Send the parcel\". Follow it from the shop shelf, past the seal check, into the locked room."
    done: [ send ]
  - text: "Press \"Take the parcel back\", switch on \"Big screen and Hindi phone\" and send again. Compare the boxes named on the parcel."
    done: [ back, "big=on", send ]
  - text: "Press \"Look in the locked room\"."
    done: [ look ]
  - text: "(Optional reveal) Take the parcel back, switch off \"Seal on the parcel\" and send again. Watch where it stops."
    done: [ back, "seal=off", send ]
    optional: true
```

**What you should notice:** The parcel is packed for each phone, the phone checks the seal at the door, and the app ends up in its own locked room.

## Build the logic

### Notice

Maya and Ravi want the same app. Maya's phone has a big screen and Hindi as its language. Ravi's phone has a small screen and English. The shop packs one parcel for each of them. Each parcel holds only what that phone needs, such as the right picture sheets and the right printed words.

### Guess

What should the shop send to each phone? Here are three ideas.

- A. All the files the maker works with, to everyone.
- B. A parcel packed for each phone's screen and language.
- C. The same biggest box for everyone, with every picture and every language in it.

Idea A sends far more than a phone can use, and the phone would have to build the app itself. Idea C would work, but each phone would carry things it never uses. The Android documentation says Google Play serves each device only the code and resources it needs. Idea B is the answer.

### Build

1. The maker sends the master carton to the shop.
2. When you ask for the app, the shop works out what your phone is like.
3. The shop packs a parcel for that phone: one main box, and extra small boxes for things like its screen and its language.
4. The shop seals the parcel with the maker's seal.
5. The post office delivers it.
6. At your door, you check the seal. If there is no seal, you do not take the parcel in.
7. If this parcel replaces an app you already have, its seal must match the seal on the one already in your home.
8. The label card is read, and the parcel is opened.
9. The app is given its own locked room, with a key that fits that room only.

### Name it

The master carton is an **AAB**, an Android App Bundle. The store uses it to build the parcel. The Android documentation says Google Play uses your app bundle to generate and serve optimized APKs for each device configuration. An **APK** is the file a phone installs.

The parcel is a set of **split APKs**: a base APK plus configuration APKs, which are small extra pieces for things like screen and language. The documentation calls the installed package the base APK plus configuration APKs.

The seal is a **signature**, a digital mark made with a private key, which is a secret code. With **Play App Signing**, the maker lets Google keep that key, and Google does the sealing for the maker. A **certificate** is the proof of who signed it. The documentation says Android requires all APKs to be signed with a certificate before they are installed or updated. On an update, the system compares the certificates in the new version and in the existing one, and allows the update if they match.

The label card is the **manifest**, the file that lists the parts of the app and what it needs.

The locked room is a **sandbox**. The documentation says each Android app lives in its own security sandbox. By default the system gives each app a unique ID number, and it sets permissions on the app's files so that only that ID can reach them.

One more word covers a different route. An app that is not installed through the Google Play Store is **sideloaded**. The documentation says a sideloaded install that is missing one or more required split APKs fails on devices running Android 10 or higher.

The documentation does not list install steps in order. The order in Build is my own reasoning: the seal check, then the label card, then the locked room.

| Parcel | Official name | On Android |
|---|---|---|
| Master carton | AAB | What the maker sends to Google Play |
| Parcel packed for your phone | Split APKs | A base APK plus configuration APKs |
| Seal | Signature | A certificate on each APK |
| Checking the seal at the door | Signature check | Android requires signed APKs for install and update |
| Your home's locked room | Sandbox | Each app has its own ID and its own files |

## Predict the output

Follow the parcel on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: phone-checks-the-seal
verified: not-run
code: |
  A parcel reaches the door of Maya's phone.
  The phone has not opened it yet.
  What does the phone check before it installs the app?
options:
  - id: a
    text: "Whether the maker's whole working folder is inside"
    why: "You may have thought the phone builds the app. The parcel already holds ready-to-install files, so there is nothing to build."
  - id: b
    text: "The seal: whether it is signed, and for an update, whether it matches the earlier seal"
    correct: true
    why: "The documentation says Android requires APKs to be signed before they are installed or updated. For an update, the new certificate must match the existing one."
  - id: c
    text: "Nothing. The phone opens every parcel it is handed"
    why: "You may have thought a tap is enough. The signature requirement means the phone looks at the seal first."
output: "The seal: whether it is signed, and for an update, whether it matches the earlier seal"
```

```challenge
type: predict
run: false
id: parcel-for-each-phone
verified: not-run
code: |
  Maya's phone: big screen, Hindi.
  Ravi's phone: small screen, English.
  Both ask the shop for the same app.
  What does each phone receive?
options:
  - id: a
    text: "A parcel packed for that phone, holding only what it needs"
    correct: true
    why: "The shop opens the master carton and packs a parcel for each address. The documentation says only the code and resources a specific device needs are downloaded."
  - id: b
    text: "The same full box for both, with every picture and language"
    why: "You may have expected one box for everyone. Google Play uses the bundle to generate and serve APKs for each device configuration."
  - id: c
    text: "The maker's working folder, so each phone can build its own app"
    why: "You may have thought the phone does the building. The building happened on the maker's side, before the master carton was sent."
output: "A parcel packed for that phone, holding only what it needs"
```

```challenge
type: predict
run: false
id: missing-pieces
verified: not-run
code: |
  Ravi copies the small configuration piece of an app from his phone.
  He puts it on a friend's phone that runs Android 10 and installs it by hand.
  The main box and the other required pieces are not copied.
  What happens?
options:
  - id: a
    text: "It installs, but only partly"
    why: "You may have expected a smaller app. The documentation does not describe a partial result; it says the install fails."
  - id: b
    text: "It installs and shows a blank screen"
    why: "You may have thought the app opens without its main box. The phone does not get that far."
  - id: c
    text: "The install fails"
    correct: true
    why: "A parcel without all its required boxes cannot be taken in. The documentation says partial installs of sideloaded apps fail on devices running Android 10 or higher."
output: "The install fails"
```

## Tricks to remember

- The shop packs for your phone, not for the whole world.
- The seal is checked at the door, before the parcel is opened.
- One app, one locked room, one key.
- All the required pieces, or no install.

## Common mistakes

- **Thinking the store sends the maker's working folder.** The app feels like one thing, so one big shipment seems natural. What arrives is a set of ready-to-install files packed for your phone. Ask what the parcel holds.
- **Thinking the phone opens anything it is handed.** The install looks like a tap and a wait, so it feels like there is no checking. Android requires every APK to be signed before install or update.
- **Copying one piece of an app and expecting it to install.** The app looks like one icon, so one file feels enough. An app can come as several pieces, and a missing required piece makes the install fail on Android 10 and higher.

## Where this shows up in Android

You copy part of an app from one phone to another, and the install fails. That app was sideloaded, because it did not come through the store. The documentation says that when you download through Google Play, Google ensures all required components of the app are installed.

Here is a second symptom. An update will not go over the app already on your phone. The documentation says the system allows an update if the certificates of the old and new versions match. My own reasoning: when they do not match, the update is not allowed, and you may have to start from a fresh install.

## Recap

1. The store packs a parcel for your phone's screen and language, not the maker's whole folder.
2. Your phone checks the signature before it installs, and an update must match the old seal.
3. The phone gives each installed app its own private space, called a sandbox.

## You can now

- Name what the phone checks before it installs a downloaded app.
- Predict what two phones with different screens and languages each receive.
- Predict what happens when you install an app with required pieces missing.
