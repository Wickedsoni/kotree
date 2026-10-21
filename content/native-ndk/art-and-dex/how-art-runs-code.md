---
id: native-ndk/art-and-dex/how-art-runs-code
title: "How does ART turn my app's code into fast machine code over time?"
status: shadow
difficulty: medium
order: 3
requires: [native-ndk/art-and-dex/what-is-dex]
summary: ART reads your code slowly at first, learns which parts run most, and turns those into fast machine code.
video: ""
publishedOn:
updatedOn:
sims: [waiter-learns-the-card]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips this comment at go-live):
Android Open Source Project, Configure ART (since Android 7 a hybrid of AOT compilation, JIT compilation and interpretation; AOT can be profile-guided; the first few runs interpret methods that are not AOT-compiled; frequently executed methods are JIT-compiled; a compilation daemon re-compiles when the device is idle and charging; compiler filters verify, speed, speed-profile): https://source.android.com/docs/core/runtime/configure
Android Developers, Baseline Profiles overview (without a profile, code is JIT-compiled in memory after being interpreted, or written to an odex file in the background when the device is idle; users have a suboptimal experience from the first run until new code paths are optimized; during installation ART AOT-compiles the methods in the profile; about 30% faster from the first launch): https://developer.android.com/topic/performance/baselineprofiles/overview
Android NDK guides (the NDK compiles C and C++ into a native library packaged into the APK; Java calls it through JNI): https://developer.android.com/ndk/guides
No practice or interview seed exists for the native-ndk branch; the interview answers below are written from these pages.
-->
<!-- ⚠️ VERIFY: (1) the roadmap row says VERIFY again before live; the three Android pages above were read on 21 Oct 2026. (2) No page read states in so many words that ART does not translate native code; that sentence is labelled general knowledge and my own reasoning. (3) The open VERIFY at native-ndk/_roadmap.md line 121 is on the what-is-dex row (dex format and D8 wording). This leaf makes no dex-format or D8 claim. (4) The picture numbers (3 orders, 20 of 5,000 methods) are made up; no page gives a threshold. -->
## The question

You install an app and open it. The first start feels slow. A few days later the same app starts faster, and you changed nothing. Did the phone do something while you were not looking? What did it do to the app's code, and why not do it all before you first opened the app?

## Mental model

Think of a new waiter on the first night.

- The waiter is **ART**, the Android Runtime. It is the part of Android that runs your app's code.
- An order card lists the steps to make one dish. The card is a piece of your app's code.
- Reading the card line by line is slow, but the waiter can start at once.
- Memorising a dish means the waiter no longer needs the card. That is the fast way.
- A list of popular dishes can arrive with the app, written by the people who know the menu.
- A quiet night after closing is the time the waiter studies.

At first the waiter reads every card. After a few orders the waiter notices which dishes come up all the time. Those get memorised. The rare dishes stay on the card, because memorising them would waste the waiter's time.

**Where the analogy stops:** A waiter keeps a memorised dish in their head and forgets it later. ART keeps some of its fast versions in memory and writes others to a file on the phone. The numbers in the picture, such as "three orders", are made up. The Android pages I read give no threshold.
## See it move

```sim
id: waiter-learns-the-card
template: states
title: A new waiter learns one dish
intro: "A new waiter has the card for one dish, Pasta. The waiter reads it line by line. No list of popular dishes came with the app, and nothing is memorised yet."
nodes:
  - { id: read, label: "Reading the card line by line" }
  - { id: memo, label: "Memorised while working" }
  - { id: ahead, label: "Memorised before the shift" }
edges:
  - { from: read, to: memo, label: "ordered often" }
  - { from: read, to: ahead, label: "on the list" }
  - { from: memo, to: ahead, label: "quiet night" }
start: read
layout: row
items:
  - { id: dish, label: "Pasta order", at: read }
vars: { orders: 0, speed: "none yet", profile: "off" }
show:
  - { var: orders, label: "Times Pasta was ordered" }
  - { var: speed, label: "Service speed" }
toggles:
  - { id: profile, label: "Popular-dishes list arrives with the app", var: profile }
actions:
  - id: serve
    label: Serve the dish
    cases:
      - when: { at: { dish: ahead } }
        do:
          - add: { orders: 1 }
          - set: { speed: "fast" }
          - light: [ahead]
          - say: "The waiter serves the dish from memory made before the shift. No card is read, so even the first order is quick."
      - when: { at: { dish: memo } }
        do:
          - add: { orders: 1 }
          - set: { speed: "fast" }
          - light: [memo]
          - say: "The waiter serves the dish from memory. No card is read, so this order is quick."
      - when: { at: { dish: read }, var: { orders: 2 } }
        do:
          - add: { orders: 1 }
          - set: { speed: "slow" }
          - move: { dish: memo }
          - light: ["read>memo"]
          - say: "This is the third order, so the waiter memorises the dish while working. This order is still read from the card. The next one will be quick."
      - when: { at: { dish: read } }
        do:
          - add: { orders: 1 }
          - set: { speed: "slow" }
          - light: [read]
          - say: "The waiter reads the card line by line. It is slow, but it works at once. Pasta has now been ordered {orders} times."
  - id: overnight
    label: Leave the phone idle and charging
    cases:
      - when: { at: { dish: memo } }
        do:
          - move: { dish: ahead }
          - light: ["memo>ahead"]
          - say: "The phone is idle and charging. The waiter studies the popular dish, so it is memorised before the next shift."
      - when: { at: { dish: ahead } }
        do:
          - say: "The dish is already memorised before the shift, so there is nothing more to study."
    refuse: "Nothing has been memorised while working yet, so there is nothing to study overnight."
  - id: reinstall
    label: Reinstall the app
    do:
      - move: { dish: read }
      - set: { orders: 0, speed: "none yet" }
      - light: [read]
      - say: "The app is installed fresh. The waiter knows no dish and reads the card again."
  - id: prep
    label: Memorise ahead from the list
    cases:
      - when: { at: { dish: read }, var: { profile: "on" } }
        do:
          - move: { dish: ahead }
          - light: ["read>ahead"]
          - say: "Before the first customer, the waiter used the list and memorised the popular dish. The first order will be quick."
      - when: { at: { dish: read } }
        do:
          - say: "There is no list, so the waiter has nothing to memorise before the shift. The first orders will be read from the card."
    refuse: "The dish is already memorised, so there is nothing to prepare."
script: [serve, serve, serve, serve, overnight, reinstall, "profile=on", prep, serve]
try:
  - text: "Press \"Serve the dish\" three times. Watch the dish move from reading the card to memorised while working."
    done: [serve, serve, serve]
  - text: "Press \"Leave the phone idle and charging\". Watch the dish move to memorised before the shift."
    done: [overnight]
  - text: "(Optional) Press \"Reinstall the app\", switch on the popular-dishes list, press \"Memorise ahead from the list\" and then \"Serve the dish\". The first order is quick this time."
    done: [reinstall, "profile=on", prep, serve]
    optional: true
```

**What you should notice:** The waiter starts slow, speeds up for the dish that keeps coming back, and with a list of popular dishes the very first order is already quick.

## Build the logic

### Notice

On the first night a customer orders Pasta. The waiter has never made it, so the waiter takes out the card and reads it line by line. It takes a while. After a week, the waiter serves Pasta without opening the card. Rarely ordered dishes still need the card.

### Guess

How does the waiter get fast? Here are three ideas.

- A. The waiter always reads the cards but learns to read faster.
- B. The waiter memorises every card on the menu before the first customer comes.
- C. The waiter reads cards at first, notices which dishes are ordered often, and memorises those.

Idea A never stops reading. Idea B is a common belief about phones. It would mean the restaurant opens late, and most of that memorising would be for dishes nobody orders. Idea C is the picture the Android documentation draws.

### Build

1. A customer orders a dish. If it is not memorised, read the card line by line. It is slow, and it works from the first minute.
2. Count how often each dish is ordered.
3. When a dish is ordered often, memorise it while working. From then on, serve it from memory.
4. A list of popular dishes can come with the app. The waiter then memorises those dishes before the first customer.
5. When the restaurant is quiet and the phone is charging, the waiter studies again, so the next shift starts faster.
6. Rare dishes stay on the card, so no time or space is wasted on them.

### Name it

ART runs your app's code on the phone. The code arrives as dex, the compact form from the previous lesson. **Machine code** is the set of instructions the phone's processor runs directly. ART can run your code in three ways.

| Way | Waiter picture | When it happens |
|---|---|---|
| **Interpretation** | Reading the card line by line | Whenever a method is not yet compiled |
| **JIT compilation** | Memorising a dish while working | While the app runs, for methods that run often |
| **AOT compilation** | Memorising a dish before the shift | Before the method runs: at install for listed methods, or while the phone is idle |

JIT and AOT are the official short names. JIT means the code is compiled while the app runs, at the moment it is needed. AOT means it is compiled before the method runs. To **compile** means to turn code into machine code. A **profile** is a list of the methods an app runs most. When AOT uses a profile, it is called profile-guided.

The Android Open Source Project page on configuring ART says that starting in Android 7, ART uses a hybrid combination of AOT compilation, JIT compilation and interpretation. It says AOT compilation can be profile-guided. It says the first few times an app runs, methods that are not AOT-compiled are interpreted, and among those, the ones that run often are then JIT-compiled. It says that when the device is idle and charging, a compilation daemon runs to re-compile the application. A **daemon** is a program that runs quietly in the background (general knowledge). My own reasoning: the idle step works from the profile that the first runs collected. The page says the default for apps is the `speed-profile` filter, which compiles the methods listed in the profile.

The same page says that an app installed without a dex metadata file, a side file that can carry the profile (general knowledge), gets no AOT compilation. That is why the common belief "the phone translates the whole app at install" does not match the documentation. At install, ART compiles ahead only the methods that a profile lists.

A **Baseline Profile** is a profile that an app ships with. The Android documentation says that without one, code is JIT-compiled in memory after being interpreted, or written to a file in the background when the device is idle. It says that during installation ART performs AOT compilation of the methods in the profile. It also says users have a suboptimal experience from the first run until new code paths are optimized. And it says a Baseline Profile improves code execution speed by about 30% from the first launch, which the page presents as a typical result, not a promise.

One more thing from the NDK documentation: native C and C++ code is compiled into a native library and packaged into the APK, and Java calls it through JNI. That text says nothing about ART translating it. My own reasoning, and general knowledge: native code is already machine code when it is packaged, so ART has no card to read for it.

## Predict the output

```challenge
type: predict
run: false
id: which-dishes-are-memorised
verified: not-run
code: |
  In this picture, the waiter memorises a dish
  once it has been ordered 3 times.

  Orders tonight, in this order:
    Soup, Pasta, Soup, Tea, Soup, Pasta, Soup

  Which dishes are memorised by the end of the night?
options:
  - id: a
    text: "Soup and Pasta"
    why: "You may have memorised any dish that came up twice. In this picture the waiter needs three orders, and Pasta came up twice."
  - id: b
    text: "All three dishes"
    why: "You may have thought the waiter memorises everything. Tea was ordered once, and rare dishes stay on the card."
  - id: c
    text: "Soup only"
    correct: true
    why: "Soup was ordered four times, so the waiter memorised it after the third. Pasta twice and Tea once stay on the card."
output: |
  Soup only
```

```challenge
type: predict
run: false
id: first-runs-without-a-list
verified: not-run
code: |
  An app is installed with no popular-dishes list.
  You open it for the first time.

  How does ART run the app's methods in the first few runs?
options:
  - id: a
    text: "Every method was already turned into machine code at install"
    why: "You may have thought the phone translates the whole app at install. Without a profile, the documentation says no AOT compilation is done."
  - id: b
    text: "Methods are read step by step, and the ones that run often get compiled while the app runs"
    correct: true
    why: "The new waiter reads the cards at first and memorises the dishes that keep coming back. That is interpretation first, then JIT compilation for the busy methods."
  - id: c
    text: "Nothing runs until every method has been compiled"
    why: "You may have thought the waiter must memorise before serving. The waiter starts at once, reading the card."
output: |
  Methods are read step by step, and the ones that run often get compiled while the app runs
```

```challenge
type: predict
run: false
id: list-at-install
verified: not-run
code: |
  An app ships with a popular-dishes list that names
  20 methods out of 5,000.

  At install, which methods does ART compile ahead of time?
options:
  - id: a
    text: "The 20 methods on the list"
    correct: true
    why: "The waiter uses the list to memorise the popular dishes before the first customer. The documentation says ART AOT-compiles the methods in the profile during installation."
  - id: b
    text: "All 5,000 methods"
    why: "You may have thought a list means everything gets memorised. Only the listed dishes are, and the rest stay on the card."
  - id: c
    text: "None, because nothing is compiled until the app runs"
    why: "You may have thought all compiling happens while the app runs. A list lets ART compile the listed methods before the first start."
output: |
  The 20 methods on the list
```

## Tricks to remember

- Read first, learn, then memorise the popular dishes.
- Interpretation is the card. JIT is memorising while serving. AOT is memorising before the shift.
- A list of popular dishes makes the first order quick.
- Rare dishes stay on the card, so not everything is compiled.
- Task: write down three dishes your own app runs all the time, such as the code that draws the first screen, and say why a list would help the first start.

## Common mistakes

- **Thinking the phone translates the whole app at install.** It feels natural, because "install" sounds like "prepare everything". The documentation says that without a profile no AOT compilation is done, and with one only the listed methods are.
- **Thinking compiled code is always better.** Fast machine code sounds like a free win. Memorising a rare dish wastes the waiter's time and space, which is my own reasoning.
- **Thinking ART translates C++ too.** The app contains C++, so it feels like one more card. Native code is already machine code when it is packaged (general knowledge, my own reasoning), so ART has no card to read for it.
- **Thinking the speed stays for ever.** The waiter feels trained. The Baseline Profiles page says that after installing or updating an app, users have a suboptimal experience until new code paths are optimized.

## Where this shows up in Android

The symptom is an app that starts slowly right after install or update and faster after some use. The Android documentation on Baseline Profiles says that after installing or updating an app, users have a suboptimal experience from the first run until new code paths are optimized. In the picture, that is the new waiter reading the cards.

The same page says that shipping a Baseline Profile lets ART AOT-compile the listed code paths, and that this improves execution speed by about 30% from the first launch. The part of this lesson that explains it is the popular-dishes list.

## Say it in an interview

**What does ART do with my app's code?**
It runs it, using a mix. The Android documentation says that since Android 7, ART combines AOT compilation, JIT compilation and interpretation, and that AOT can be guided by a profile. Methods that are not compiled are interpreted, and the ones that run often are compiled.

**Why can an app be slower right after install than a few days later?**
At first, much of the code is interpreted, and the busy parts are compiled as the app runs. When the phone is idle and charging, a daemon re-compiles the app. The Baseline Profiles page says users have a suboptimal experience from the first run until new code paths are optimized. A Baseline Profile moves some of that work to install time.

**What is the difference between AOT, JIT and interpretation?**
Interpretation reads the code step by step. JIT compiles busy methods into machine code while the app runs. AOT compiles methods before they run, at install for the methods a profile lists, or while the phone is idle. The trade-off is that compiling takes time and space, so ART does not compile everything (my own reasoning).

**Does ART compile my C++ code?**
No, as far as I know. The NDK documentation says C and C++ are compiled into a native library that is packaged into the APK. My own reasoning: that library already holds machine code, so there is nothing for ART to translate. This is general knowledge, and I did not find a page that says it in those words.

## Recap

1. ART interprets code at first, then compiles the methods that run often.
2. A profile lets ART compile the busiest methods before the first start.
3. Native C++ code arrives as machine code, so ART has nothing to compile for it.

## You can now

- Explain why an app can start slower right after install than days later.
- Predict which methods ART compiles at install when an app ships a profile.
- Sort interpretation, JIT and AOT by when each one happens.
