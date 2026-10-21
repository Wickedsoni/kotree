---
id: foundation/how-an-app-is-born/what-happens-when-i-build
title: What happens when I press Build?
status: live
difficulty: medium
order: 31
requires: [foundation/how-an-app-is-born/what-is-an-app-file, foundation/from-code-to-running/what-is-a-compiler]
summary: Pressing Build runs a line of steps that translate your code, pack everything into one file and stamp it with a signature.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [bakery-build-line]
playground: false
fruit: false
upnext: false
---


## The question

You press Build in your editor. A bar fills, and a minute later you have something that can be put on a phone. What happened in that minute? Did the tool copy your files across, or did it change them on the way?

## Mental model

Think of a bakery line.

- The ingredients are your code, your pictures and your words.
- The mixing station turns the ingredients into dough.
- The oven bakes the dough into loaves in the form the phone can run.
- The label card is a page that lists what is in the box.
- The packing table puts the loaves, the picture sheets, the printed words and the label card into one box.
- The stamp station presses the maker's stamp onto the box.
- The line manager starts each station when the one before it has finished. The manager never skips a station.
- The house stamp is a plain stamp. Every test box gets it automatically.
- The shop's own stamp is a private stamp. A box meant for customers needs it.

**Where the analogy stops:** A bakery stamp is ink. A real stamp here is a digital mark made with a key, which is a secret code. Test boxes get the house stamp without being asked, so in daily work you never see the stamp station missing. The animation takes the stamp away to make a point.

## See it move

```sim
id: bakery-build-line
template: flow
title: A bakery line from ingredients to phone
intro: One box of ingredients waits at the start of the line. Both the mixing station and the stamp station are in place.
stations:
  - { id: src, label: Ingredients }
  - { id: mix, label: Mix }
  - { id: bake, label: Bake }
  - { id: pack, label: Pack }
  - { id: stamp, label: Stamp }
  - { id: phone, label: Phone }
items:
  - { id: box, label: Raw box, at: src }
vars: { mixer: "on", stamper: "on" }
toggles:
  - { id: mixer, label: Mixing station, var: mixer }
  - { id: stamper, label: Stamp station, var: stamper }
actions:
  - id: next
    label: Move the box on
    cases:
      - when: { at: { box: src }, var: { mixer: "off" } }
        do:
          - label: { box: "Nothing made" }
          - mark: { box: bad }
          - light: [ src ]
          - say: "The mixing station is missing, so no dough is made. Bake, Pack and Stamp have nothing to work on, and the box cannot move on."
      - when: { at: { box: src } }
        do:
          - move: { box: mix }
          - label: { box: "Dough" }
          - mark: { box: plain }
          - light: [ mix ]
          - say: "Mix: your code, pictures and words go in. Dough, and pictures and words ready to pack, come out."
      - when: { at: { box: mix } }
        do:
          - move: { box: bake }
          - label: { box: "Loaves" }
          - light: [ bake ]
          - say: "Bake: the dough goes in. Loaves the phone can run come out."
      - when: { at: { box: bake } }
        do:
          - move: { box: pack }
          - label: { box: "One box" }
          - light: [ pack ]
          - say: "Pack: the loaves, the pictures, the words and the label card go in. One box comes out."
      - when: { at: { box: pack }, var: { stamper: "off" } }
        do:
          - move: { box: phone }
          - label: { box: "Unsigned box" }
          - mark: { box: bad }
          - light: [ phone ]
          - say: "There is no stamp station, so the box skips it and reaches the phone with no stamp. Android requires all APKs to be signed before they are installed."
      - when: { at: { box: pack } }
        do:
          - move: { box: stamp }
          - label: { box: "Stamped box" }
          - light: [ stamp ]
          - say: "Stamp: the finished box goes in. The same box, with a stamp on it, comes out."
      - when: { at: { box: stamp } }
        do:
          - move: { box: phone }
          - label: { box: "Signed box" }
          - mark: { box: good }
          - light: [ phone ]
          - say: "The signed box leaves the line and reaches the phone."
    refuse: The box is already at the phone, or stuck at the start. Press Reset to run the line again.
script: [ next, next, next, next, next, "stamper=off", "stamper=on", "mixer=off", next ]
try:
  - text: "Press \"Move the box on\" five times. At each stop, read what goes in and what comes out."
    done: [ next, next, next, next, next ]
  - text: "Switch off \"Stamp station\". The sim replays the line without it, and the box reaches the phone with no stamp."
    done: [ "stamper=off" ]
  - text: "Switch the stamp station back on, switch off \"Mixing station\" and press \"Move the box on\". Later stations have nothing to work on."
    done: [ "stamper=on", "mixer=off", next ]
```

**What you should notice:** Without the stamp, the box reaches the phone unstamped. Without the mixing station, the later stations have nothing to work on.

## Build the logic

### Notice

Maya has three things: her code, a logo picture and a page of words. She wants a box she can hand to a phone. She cannot hand over the ingredients as they are. The phone does not run her recipe as written. It runs a compact form of it.

| Station | What goes in | What comes out |
|---|---|---|
| Mix | Her code, her pictures, her words | Dough, and the pictures and words ready to pack |
| Bake | The dough | Loaves the phone can run |
| Pack | The loaves, the pictures, the words, the label card | One box |
| Stamp | The box | The same box with a stamp on it |

### Guess

The four stations are ready. In which order should a box travel through them? Here are three ideas.

- A. Mix, bake, pack, stamp.
- B. Stamp the empty box first, then fill it.
- C. Pack the raw ingredients first, then mix and bake inside the box.

Idea B puts the stamp on a box with nothing in it. The stamp belongs on the finished box. Idea C bakes the dough after the box is shut, which is too late. Idea A is the answer.

### Build

1. Mix. Check the ingredients and turn them into dough. If an ingredient is spoiled, the line stops here and nothing moves on.
2. Bake. Turn the dough into loaves the phone can run.
3. Pack. Put the loaves, the picture sheets, the printed words and the label card into one box.
4. Stamp. Press a stamp on the finished box.
5. The box leaves the line. It is one file, ready to hand to a phone.
6. The line manager runs the stations in this order, and each station uses what the one before it made.

### Name it

The whole line is a **build**. The Android documentation says the Android build system compiles app resources and source code and packages them into APKs or Android App Bundles. I split that into four stations. This four-station picture is my own summary, not a list from one Android page.

**Mix** is to **compile**: to translate code into a form that runs. The documentation says bytecode is generated for compiled Java and Kotlin source. **Bytecode** is a compact list of instructions that is not tied to one exact kind of processor. The resources are compiled too.

**Bake** is the conversion. A tool called **d8** compiles Java bytecode into DEX bytecode. **DEX** is the compact code form made for Android. The build tools run d8 for you.

**Pack** puts the DEX code, the resources and the label card, called the **manifest**, into one file. An **APK** is the file a phone installs. An **AAB**, or Android App Bundle, is a publishing format that Google Play turns into APKs.

**Stamp** is **signing**: adding a digital signature made with a key. A **certificate** is the proof of who signed. The documentation says Android requires all APKs to be signed with a certificate before they are installed or updated. My own reasoning from that rule: a file with no signature is not installed.

The line manager is **Gradle**, the build tool that runs the steps in order. The documentation says you can run every build step of an Android project with it. It also says the Android Gradle plugin, an add-on, registers the steps needed to build an APK. This page does not go further into Gradle.

By default there are two kinds of build. A **debug build** is for testing, and the build system signs it with a default key, the house stamp. A **release build** is for users, and the build system does not sign it until you give it your own signing settings, the shop's own stamp.

| Bakery | Station | Real step | What comes out |
|---|---|---|---|
| Mix | Compile | Source code and resources are compiled | Bytecode and compiled resources |
| Bake | Convert | d8 turns bytecode into DEX | DEX code |
| Pack | Pack | The DEX code, resources and label card go into one file | An APK or an AAB |
| Stamp | Sign | A key adds a signature | A signed file |

## Predict the output

Follow the line on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: stations-in-order
verified: not-run
code: |
  Four stations: Pack, Mix, Stamp, Bake.
  Put them in the order a box travels, from first to last.
options:
  - id: a
    text: "Mix, Pack, Bake, Stamp"
    why: "You may have packed the dough before baking it. Loaves are packed after the oven, so Bake comes before Pack."
  - id: b
    text: "Mix, Bake, Pack, Stamp"
    correct: true
    why: "Mix makes the dough, Bake makes the loaves, Pack boxes them, and Stamp goes on the finished box."
  - id: c
    text: "Bake, Mix, Stamp, Pack"
    why: "You may have started with the oven. There is no dough to bake until Mix has made it."
output: "Mix, Bake, Pack, Stamp"
```

```challenge
type: predict
run: false
id: box-without-a-stamp
verified: not-run
code: |
  A release box leaves the line with no stamp at all,
  because no signing settings were given.
  The maker hands it to a phone.
  What happens?
options:
  - id: a
    text: "The phone will not install it, because Android requires every APK to be signed"
    correct: true
    why: "The box left the line without the shop's stamp. The documentation says Android requires all APKs to be signed before they are installed."
  - id: b
    text: "The phone installs it and presses its own stamp on it"
    why: "You may have thought the phone repairs the box. The stamp belongs to the build side, and the requirement comes before install."
  - id: c
    text: "The phone installs it and shows a warning"
    why: "You may have expected a softer answer. The documentation states a requirement, not a warning."
output: "The phone will not install it, because Android requires every APK to be signed"
```

```challenge
type: predict
run: false
id: no-mixing-station
verified: not-run
code: |
  Line: Mix -> Bake -> Pack -> Stamp
  The Mix station is taken away, and the line runs.
  What does the Bake station receive?
options:
  - id: a
    text: "The dough, as before"
    why: "You may have assumed the dough still appears. Mixing is what makes it."
  - id: b
    text: "The raw ingredients, and it bakes them as they are"
    why: "You may have thought the oven can use anything. The documentation says d8 works on bytecode, and the bytecode comes from compiling."
  - id: c
    text: "Nothing to bake, so Pack and Stamp have nothing to work with either"
    correct: true
    why: "Each station uses what the one before made. With Mix gone there is no dough, so nothing moves on. This answer is my own reasoning from the order of the stations."
output: "Nothing to bake, so Pack and Stamp have nothing to work with either"
```

## Tricks to remember

- Mix, Bake, Pack, Stamp. Say it in order.
- Test boxes get the house stamp. Customer boxes need the shop's own stamp.
- Build changes your files. It does more than copy them.
- The stamp goes on last, on the finished box.

## Common mistakes

- **Thinking Build only copies my files.** Files end up on the phone, so copying feels likely. Four steps change them: translating, converting, packing and signing. Look at what comes out of each station.
- **Thinking the stamp is optional.** The house stamp is added without being asked, so you never see it. Android requires every APK to be signed before install. Ask which stamp the box has.
- **Sending a test box to the store.** It installs on your own phone, so it feels ready. The documentation says most app stores do not accept apps signed with a debug certificate.
- **Reading a build failure as a phone problem.** The phone feels like the place where apps live. The build runs on your computer, before the phone sees anything. A spoiled ingredient stops the line at the first station.

## Where this shows up in Android

You send a friend the file the build gave you. It installs on your phone, but the store refuses to publish it. The documentation says the debug certificate is created by the build tools and is insecure by design, so most app stores, including Google Play, do not accept apps signed with it. Your file came from a debug build. A release build must be signed with your own private key, the secret code only you hold.

Here is a second symptom. You are ready to publish a new app, and the store wants a bundle, not an APK. The documentation says all new apps submitted after August 2021 are required to use AABs. It also says an APK suits a quick test on your own device. So the Pack station makes a different file for each job.

## Recap

1. Build translates your code, converts it for Android, packs it into one file and signs it.
2. The stations run in order, and each one uses what the one before made.
3. Android requires every APK to be signed before it is installed.

## You can now

- Put the four build steps in order.
- Predict what happens to a file that was never signed.
- Predict what the later steps receive when an early step is missing.
