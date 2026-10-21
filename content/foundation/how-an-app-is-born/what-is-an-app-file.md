---
id: foundation/how-an-app-is-born/what-is-an-app-file
title: What is inside an app file on a phone?
status: live
difficulty: easy
order: 30
requires: [foundation/from-code-to-running/what-is-art]
summary: An app is one sealed file that holds translated code, pictures, text and a label card telling the phone what is inside.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [flat-pack-app-box]
playground: false
fruit: false
upnext: false
---


## The question

You tap a button on a store page and an app lands on your phone. It has a name, an icon, pictures and text in your language. It also has things it can do. Where did all of that come from? Is an app one program, or a whole box of things?

## Mental model

Think of a flat-pack furniture box.

- The box is the app file. It is one sealed thing that you can move around.
- The parts bag holds the pieces that do the work. These are your code, already translated for the phone.
- The picture sheets and the printed words are the images and the text that the app shows.
- The label card on the lid lists what is in the box and says what the app may ask to use.
- The seal across the lid shows who packed the box.
- The master carton is a bigger box that the maker sends to a shop. The shop packs a smaller box for each customer from it.

If the label card is missing, a pile of parts is all you have. Nobody can tell which part starts first or what the pieces are for.

**Where the analogy stops:** You assemble furniture yourself, but nobody assembles an app by hand. The phone reads the label card and sets the app up. Also, the animation takes the label card away to make a point. The Android documentation says the system must read the label card to know a part exists, so for the main kinds of part, one that is not listed cannot be started.

## See it move

```sim
id: flat-pack-app-box
template: zones
title: A flat-pack box, its four piles and the phone
intro: A sealed box stands for the app file. Nothing is unpacked and the phone is empty.
zones:
  - { id: box, label: App file (the sealed box) }
  - { id: piles, label: Four piles }
  - { id: phone, label: Phone }
items:
  - { id: sealed, label: Sealed app file, at: box }
actions:
  - id: unpack
    label: Unpack
    cases:
      - when: { at: { sealed: box } }
        do:
          - remove: sealed
          - spawn: { id: parts, label: "Parts bag (translated code)", at: piles }
          - spawn: { id: pictures, label: "Picture sheets (images)", at: piles }
          - spawn: { id: words, label: "Printed words (text)", at: piles }
          - spawn: { id: card, label: "Label card (the manifest)", at: piles }
          - say: "The box opens into four piles: code, pictures, words and the label card."
    refuse: "The box is already open. Press Reset to pack it again."
  - id: read-card
    label: Read the label card
    cases:
      - when: { at: { card: piles } }
        do:
          - mark: { card: picked }
          - say: "The label card lists the parts of the app, such as its screens, and what the app asks to use."
    refuse: "There is no label card to read."
  - id: remove-card
    label: Remove the label card
    cases:
      - when: { at: { card: piles } }
        do:
          - remove: card
          - say: "The label card is gone. The parts bag is still full, but nothing lists the parts."
    refuse: "There is no label card to remove. Unpack the box first."
  - id: to-phone
    label: Unpack onto the phone
    cases:
      - when: { at: { card: piles } }
        do:
          - move: { parts: phone, pictures: phone, words: phone, card: phone }
          - mark: { parts: good }
          - say: "The phone reads the label card, sets the app up and can start each part the card lists."
      - when: { at: { parts: piles } }
        do:
          - move: { parts: phone, pictures: phone, words: phone }
          - mark: { parts: bad }
          - say: "With the label card removed, the phone has no list of the parts, so it cannot start any of them."
    refuse: "Nothing is waiting in the piles. Press Unpack first, or press Reset to start again."
script: [unpack, read-card, remove-card, to-phone]
try:
  - text: "Press \"Unpack\" and watch the box open into four piles: code, pictures, words and the label card."
    done: [unpack]
  - text: "Read the label card to see what it lists."
    done: [read-card]
  - text: "Press \"Remove the label card\", then press \"Unpack onto the phone\"."
    done: [remove-card, to-phone]
```

**What you should notice:** With the label card removed, the phone has no list of the parts, so it cannot start any of them.

## Build the logic

### Notice

Open a flat-pack box on the floor. You find four piles. One is the parts bag. One is the picture sheets. One is the printed words, which come in more than one language. The last is the label card.

| Pile | What it holds |
|---|---|
| Parts bag | The pieces that do the work |
| Picture sheets | The images the app shows |
| Printed words | The text the app shows, in each language |
| Label card | A list of the parts and what the app needs |

Take the label card away, and you still have every part. You cannot tell which piece to use first.

### Guess

The phone is holding the sealed box. It must learn which parts exist, which part to open, and what the app may ask to use. Where should it look? Here are three ideas.

- A. Try each part until one of them starts.
- B. Ask the store every time the app opens.
- C. Read the label card on the lid.

Idea A would waste time and might never find what it needs. Idea B needs the store to answer every time, but the box is already on the phone. Idea C is the answer.

### Build

1. Put the working pieces in the parts bag.
2. Add the picture sheets and the printed words.
3. Write the label card. List every part the phone may need to start, what the app may ask to use, and what the phone must have.
4. Fix the label card to the box.
5. Seal the lid with the maker's seal.
6. Hand over the whole box as one file.
7. When the phone gets the box, it reads the label card first.

### Name it

The box is an **APK**, short for Android package. The Android documentation says it is an archive file with an `.apk` suffix. An archive is one file that holds many files. The APK holds the contents an app needs when it runs, and it is the file that devices use to install the app.

Inside it you find these piles.

- The compiled code is the code after it has been translated for the phone. It is stored in **DEX** files. DEX, short for Dalvik Executable format, is a compact code form made for Android. The phone's own program for running apps executes it.
- The **resources** are the images, the text and the other files the app uses that are not code. One resource file keeps values that change with the phone's settings, such as the same text in each language.
- The **manifest** is the label card. Its file name is `AndroidManifest.xml`. A **component** is a building block that the system can start, such as a screen of the app. The documentation says the manifest must declare the app's components and the hardware and software features it requires. It must also declare the **permissions** the app needs, which are the things it asks the phone to let it use. For the main kinds of component, such as a screen of the app, the system can't start one that the manifest does not list.

The manifest also carries the app's icon and name. In the documentation's example, the `application` entry sets `android:icon` and `android:label`, and both point to resources, a picture and a piece of text. So the manifest names the picture and the words, and the resources hold them.

The seal is a **signature**, a digital mark that shows who packed the file. The documentation says Android requires all APKs to be digitally signed before they are installed.

There is a second kind of box. An **AAB**, or Android App Bundle, is the master carton. The documentation calls it a publishing format that holds all your compiled code and resources, and defers APK generation and signing to Google Play. It adds that, unlike an APK, you cannot deploy an app bundle directly to a device. The Android build documentation says all new apps submitted after August 2021 are required to use AABs.

| Flat-pack | Official name | In the file |
|---|---|---|
| The box | APK | One archive file |
| Parts bag | Compiled code | DEX files |
| Pictures and printed words | Resources | Images and text |
| Label card | Manifest | `AndroidManifest.xml` |
| Seal | Signature | A digital mark on the APK |
| Master carton | AAB | What you send to Google Play |

## Predict the output

Follow the boxes on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: which-pile-names-the-icon
verified: not-run
code: |
  Inside the box:
    parts bag       (the compiled code)
    picture sheets  (images, including the picture for the app icon)
    printed words   (text, including the words for the app name)
    label card      (the manifest)

  The phone must show the app's icon and name.
  Which pile says which picture and which words to use?
options:
  - id: a
    text: "The parts bag"
    why: "You may have thought the code decides everything. The code does the work, and the Android documentation puts the icon and label settings in the manifest."
  - id: b
    text: "The picture sheets"
    why: "You may have looked where the picture is stored. The sheets hold the picture, but the documentation's example names it from the manifest."
  - id: c
    text: "The label card (the manifest)"
    correct: true
    why: "The label card lists the icon and label settings, and they point to the picture sheets and the printed words. The card names them and the sheets hold them."
output: "The label card (the manifest)"
```

```challenge
type: predict
run: false
id: a-file-called-game-aab
verified: not-run
code: |
  A friend sends a file called game.aab and says: install this.
  You try to put it onto your phone.
  What happens?
options:
  - id: a
    text: "It cannot be put onto the phone directly. It is a master carton, and Google Play builds APKs from it"
    correct: true
    why: "The master carton is for the shop. The Android documentation says that, unlike an APK, you cannot deploy an app bundle directly to a device."
  - id: b
    text: "It installs the same way as any APK"
    why: "You may have expected one file type. An AAB is a publishing format, and an APK is the file that devices install."
  - id: c
    text: "It installs, but without the pictures"
    why: "You may have thought a bundle is a smaller box. It holds all the compiled code and resources, and it is not installed directly."
output: "It cannot be put onto the phone directly. It is a master carton, and Google Play builds APKs from it"
```

```challenge
type: predict
run: false
id: part-missing-from-the-label
verified: not-run
code: |
  A maker writes a new screen for the app, a part the system can start.
  The code is in the parts bag, but the maker forgets to list it on the label card.
  The box is sealed and sent. The system tries to start that component.
  What happens?
options:
  - id: a
    text: "It starts, because the code is in the parts bag"
    why: "You may have thought that having the part is enough. The documentation says the system must read the manifest to know a component exists."
  - id: b
    text: "The system cannot start it, because the label card does not list it"
    correct: true
    why: "A part that is not on the label card is not known to the phone. The documentation says that if a component is not declared in the manifest, the system can't start it."
  - id: c
    text: "The phone adds it to the label card on its own"
    why: "You may have thought the phone repairs the list. The documentation says the app declares its components in the manifest, so the maker writes that list."
output: "The system cannot start it, because the label card does not list it"
```

## Tricks to remember

- One box, four piles: code, pictures, words, label card.
- The label card is read first.
- APK is the box a phone opens. AAB is the master carton for the shop.
- A part that is not on the label card is a part the phone cannot start.

## Common mistakes

- **Thinking an app is one program file with nothing else.** On the phone it looks like a single icon, so it feels like a single thing. The APK also holds resources and a manifest. Picture the four piles.
- **Mixing up APK and AAB.** Both are called the app file, and both hold the code. A bundle cannot be put on a device directly, and an APK can. Ask which one the phone will open.
- **Thinking the name and icon live in the code.** The code runs the app, so it feels like the place that sets how it looks. The manifest's `application` entry sets the icon and label. Look at the label card.

## Where this shows up in Android

A friend sends you a file that ends in `.aab`, and your phone will not install it. The file is a bundle. The documentation says you cannot deploy one directly to a device, and that Google Play builds the APKs from it.

Here is a second symptom. A developer adds a new screen to an app, and nothing happens when they try to open it. My own reasoning: the first thing to check is the manifest. The documentation says that a screen left out of the manifest is not visible to the system and can never run.

## Recap

1. An app file packs compiled code, resources and a manifest into one sealed archive.
2. The manifest lists the parts and tells the phone about the app.
3. An APK installs on a phone, but an AAB is only for publishing.

## You can now

- Name the part of an app file that points to the app's name and icon.
- Explain why a file ending in `.aab` cannot be installed directly.
- Predict what happens to a component that is missing from the manifest.
