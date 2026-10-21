---
id: kmp/what-is-kmp/what-kmp-shares
title: "Which parts of an app can I write once, and which must I still write for each phone?"
status: live
difficulty: easy
order: 1
requires: []
summary: Kotlin Multiplatform shares the logic you choose, such as rules and data code. Camera, notifications and other phone features stay with each platform.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [shared-or-platform-bins]
playground: false
fruit: false
upnext: false
---

## The question

A small team has one shop app on Android and wants the same app on iPhone. Their manager says: "Write it once, and it works on both." The team lead says: "Not everything." So which parts of the app can really be written once, and which parts must still be written for each phone?

## Mental model

Think of a restaurant chain with one recipe book and a different oven in each kitchen.

- The recipe book is the code you write once.
- The kitchens are the phones, one for Android and one for iPhone.
- The ovens are the features that belong to each phone, such as its camera or its notification system.
- The cook in each kitchen follows the same recipe but uses the oven that kitchen has.
- The dining room is how the app looks. The chain may share one design, or each kitchen may build its own.

The recipes do not change from city to city. The oven knobs do, so each kitchen needs a cook who knows its oven.

**Where the analogy stops:** In a real chain the whole menu is shared. Here you choose how much to share, from a few recipes to most of them. And a recipe can say "I need an oven that bakes", and each kitchen then supplies its own.

## See it move

<!-- SIM: shared-or-platform-bins -->

**Try this:**
1. Let the six features roll down the belt. Drop each one into the shared bin or the per-phone bin, and read the reason the bin shows.
2. Drop "camera" into the shared bin and read why it bounces back.
3. Drop "screen layout" into each bin in turn and read both reasons.

**What you should notice:** Rules and data code can sit in the shared bin, camera and notifications must go to the per-phone bin, and screen layout can go to either.

## Build the logic

### Notice

The shop app has six features.

1. Price rules: take 5 off when the total is over 50.
2. Login rules: a password needs at least 8 characters.
3. Saved data: remember the user's cart.
4. Screen layout: where the title and the button sit.
5. Camera: scan a barcode.
6. Notifications: tell the user their order shipped.

### Guess

Which of these need to be written twice, once per phone?

- A. None, because the code is written once.
- B. Only the ones that use a part of the phone itself, such as the camera and notifications. The rest can be shared.
- C. All six, because the two phones are different.

Idea A is what the slogan sounds like. Idea C is what people say after they tried to share and got stuck. Idea B is closest, with one more twist for screen layout.

### Build

1. Ask of each feature: does it only use numbers, text and rules?
2. If yes, it does not depend on which phone it runs on. It can live in the shared recipe book.
3. Ask: does it need something only this phone has, such as the camera or the notification system?
4. If yes, the real work must be done by each phone's own oven.
5. The shared recipe can still say "I need a photo". It states what it needs, and each kitchen supplies it.
6. For how the screen looks, you choose: share it, or build it separately on each phone.

### Name it

**Kotlin Multiplatform**, called **KMP**, is an open-source technology from JetBrains. The Kotlin documentation says it enables sharing code across Android, iOS, desktop, web and server while retaining the advantages of native development. Native here means built with each phone's own tools, so the app feels at home on that phone (general knowledge). The documentation also says you can share code on your terms. You can share a **module**, a self-contained part of a project, such as networking or storage, and expand the shared code over time. Or you can share all of your business logic, the rules of the app, while keeping the UI native. The UI, short for user interface, is the screens the user sees and touches.

The **shared code** is the part written once. The **platform-specific code** is the part each phone gets on its own. The documentation says **expect/actual** declarations let you access platform-specific APIs from Kotlin Multiplatform modules. An API is a set of ready-made calls that a phone offers. Shared code declares what it expects, and each platform supplies the actual version. The same page says to rely on standard language constructs wherever possible instead of expect and actual, so an interface is the first thing to try (my own reading of that sentence). An interface is a list of what something must do, with no code behind it.

For the screen, the documentation names three options: keep the UI native on each platform, share it with **Compose Multiplatform**, which is JetBrains' way to share screen code across platforms, or move to it gradually. Sharing the screen is a choice, not a requirement.

| Feature | Where it can live | Why |
|---|---|---|
| Price rules | Shared | Only numbers and rules |
| Login rules | Shared | Only text and rules |
| Saved data | Shared | The documentation names storage as a module you can share |
| Screen layout | Your choice | Native on each phone, or shared with Compose Multiplatform |
| Camera | Platform-specific, behind a shared promise | My own reasoning: the camera is the phone's own feature, which is what expect/actual is for |
| Notifications | Platform-specific, behind a shared promise | My own reasoning: each phone has its own notification system |

This page has no program to run. The table is the whole map.

## Predict the output

```challenge
type: predict
run: false
id: which-can-be-shared
verified: not-run
code: |
  A shop app has three features:
    Feature 1: the rule "take 5 off when the total is over 50"
    Feature 2: taking a photo with the phone's camera
    Feature 3: showing a notification on the phone
  Which feature can be written once in plain shared Kotlin, with no help from a phone?
options:
  - id: a
    text: "All three, because it is all Kotlin"
    why: "You may have thought Kotlin everywhere means the same code everywhere. The camera and notifications need each phone's own system, which Kotlin alone does not provide."
  - id: b
    text: "Feature 2, the camera"
    why: "You may have picked the feature that sounds like a feature. The camera is the oven, so each kitchen needs its own cook."
  - id: c
    text: "Feature 1, the price rule"
    correct: true
    why: "It only uses numbers and a rule, so it is the same recipe in every kitchen. The camera and the notification are the ovens."
output: |
  Feature 1, the price rule
```

```challenge
type: predict
run: false
id: sharing-the-screen
verified: not-run
code: |
  A team shares its price rules and saved data.
  They ask: do we have to share the screen layout as well?
  What is the right answer?
options:
  - id: a
    text: "No, sharing the screen is optional, and each phone can keep a native screen"
    correct: true
    why: "The recipe book can hold only some recipes. The documentation says you can keep the UI native or move to Compose Multiplatform gradually."
  - id: b
    text: "Yes, the whole app must be shared once you start"
    why: "You may have thought sharing is all or nothing. The documentation says you share on your own terms, even one module."
  - id: c
    text: "No, a screen can never be shared"
    why: "You may have heard that screens differ. Compose Multiplatform exists to share UI code, and the choice is yours."
output: |
  No, sharing the screen is optional, and each phone can keep a native screen
```

```challenge
type: predict
run: false
id: shared-code-needs-a-photo
verified: not-run
code: |
  Shared code needs a photo from the camera.
  It cannot talk to the camera itself.
  What does it do?
options:
  - id: a
    text: "It calls the camera directly, because shared code can reach everything"
    why: "You may have thought shared code can reach every part of a phone. It cannot see a phone's own features by itself."
  - id: b
    text: "It states what it needs, and each phone supplies the real camera code"
    correct: true
    why: "The recipe says what it needs, and each kitchen provides its own oven. The documentation calls one way of doing this expect and actual."
  - id: c
    text: "It leaves the photo out of the app on one of the phones"
    why: "You may have thought a missing feature is the only answer. The shared recipe can wait for each phone to fill in the missing part."
output: |
  It states what it needs, and each phone supplies the real camera code
```

## Tricks to remember

- One recipe book, one oven per kitchen.
- Numbers and rules go in the shared book. Phone features need their own oven.
- Share on your own terms: a little, or a lot.
- The screen is a choice. The camera and notifications are not.

## Common mistakes

- **Thinking Kotlin Multiplatform lets you write the whole app once, and it looks and works the same everywhere.** The slogan makes it sound like magic. You choose what to share, and phone features still need each phone's own part.
- **Thinking you must share everything or nothing.** Starting small feels risky, so a big move feels necessary. The documentation says you can share one module and expand over time.
- **Thinking shared code can use the camera directly.** Kotlin runs on every phone, so it feels like it can reach everything. Shared code can only say what it expects. Each phone provides the real work.
- **Thinking sharing the screen is the point of KMP.** Shared screens get the most attention. Sharing price rules or login rules already saves effort and keeps both apps in agreement.

## Where this shows up in Android

A shop's Android app takes 5 off when the total is over 50, but its iPhone app takes 5 off only over 60. A customer sees different totals on two phones. This is what two copies of one rule do over time. If the price rule lives in shared code, both phones follow the same recipe.

My own reasoning: when a symptom is "the two apps disagree", look first at logic that was copied. When it is "this feature works on one phone only", look at the part that talks to that phone itself.

## Say it in an interview

**What is Kotlin Multiplatform, and what can you share?**
It is an open-source technology from JetBrains for sharing code across Android, iOS, desktop, web and server while keeping the benefits of native development. You can share one module, such as networking or storage, or all your business logic. Sharing the UI is optional.

**What still has to be written for each platform?**
Anything that uses a platform's own features, such as the camera or notifications. Shared code declares what it expects, and each platform supplies the actual version. The documentation calls this expect and actual (the camera and notification examples are mine).

**Should you use expect/actual or an interface?**
Try the interface first. The documentation says to rely on standard language constructs wherever possible instead of expected and actual declarations. Use expect/actual when an interface does not do the job.

**What is the trade-off in sharing as much as possible?**
This is my own reasoning, built on the documentation's point that you can share on your own terms. More sharing means fewer copies of a rule that can drift apart. But the shared code must hide each platform's differences behind clear promises, and that takes design work. Starting with one module lets a team learn before it commits.

## Recap

1. Kotlin Multiplatform shares the code you choose, from one module to most of the app.
2. Rules and data code can be shared, while phone features stay with each platform.
3. Sharing the screen is a choice, not a requirement.

## You can now

- Pick the feature that can be written once in shared code from a list of price rule, camera and notification.
- Say whether sharing the screen is required when a team already shares its rules and data.
- Predict what shared code does when it needs the phone's camera.
