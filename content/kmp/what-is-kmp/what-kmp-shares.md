---
id: kmp/what-is-kmp/what-kmp-shares
title: "Which parts of an app can I write once, and which must I still write for each phone?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: Kotlin Multiplatform shares logic you choose, such as rules and data code. Camera, notifications and other phone features stay with each platform.
video: ""
publishedOn:
updatedOn:
sims: [shared-or-platform-bins]
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. KMP claims were checked on 21 Oct 2026 against https://kotlinlang.org/docs/multiplatform/kmp-overview.html (share isolated modules such as networking or storage; share all business logic with native UI; UI sharing optional via Compose Multiplatform; platforms listed: Android, iOS, desktop, web, server) and https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual.html (expect/actual give common code access to platform-specific APIs; the page prefers interfaces where they work). Platform stability statuses are deliberately NOT stated: the roadmap row says to reopen the supported-platforms table and copy each status with its date at publish time. Which of the six sim features count as "shared" is the author's roadmap list; the claim that saved-data code and screen layout can be shared rests on the overview page wording (storage module; optional UI sharing) and should be reread at publish. This is a concept leaf (no Mac needed), so no evidence labels are used. The sim id shared-or-platform-bins has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/shared-or-platform-bins.md). Predict items are paper-and-pencil and were hand-checked only. -->
## The question

A small team has one shop app on Android and wants the same app on iPhone. Their manager says: "Write it once, and it works on both." The team lead says: "Not everything."

So which parts of the app can really be written once, and which parts must still be written for each phone?

## Mental model

Think of a restaurant chain with one recipe book and a different oven in each kitchen.

- The recipe book is the code you write once.
- The kitchens are the phones, one kitchen for Android and one for iPhone.
- The ovens are the features that belong to each phone, such as its camera or its notification system.
- The cook in each kitchen follows the same recipe but uses the oven that kitchen has.

The recipes do not change from city to city. The oven knobs do, so each kitchen needs its own cook who knows its oven.

**Where the analogy stops:** In a real chain the whole menu is shared. Here, you choose how much to share. You can share only a few recipes, or most of them. And one thing is optional: you can even share how the dining room looks, but you do not have to.

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

- A. All six, because the two phones are different.
- B. None, because the code is written once.
- C. Only the ones that use a part of the phone itself, such as the camera and notifications. The rest can be shared.

Idea A is what people say who have tried to share and got stuck. Idea B is what the slogan sounds like. Idea C is closest, with one more twist for screen layout.

### Build

1. Ask of each feature: does it only use numbers, text and rules?
2. If yes, it does not depend on which phone it runs on. It can live in the shared recipe book.
3. Ask: does it need something only this phone has, such as the camera or the notification system?
4. If yes, the real work must be done by each phone's own oven.
5. The shared recipe can still say "I need a photo". It states what it needs, and each kitchen supplies it.
6. For how the screen looks, you choose: share it, or build it separately on each phone.

### Name it

**Kotlin Multiplatform**, called **KMP**, is a Kotlin technology from JetBrains that lets you share code across Android, iOS, desktop, web and server while keeping the benefits of native development. The Kotlin documentation says you can share code on your own terms: share isolated modules such as networking or storage, or share all your business logic and keep the UI native.

The **shared code** is the part written once, and the **platform-specific code** is the part each phone gets on its own. To let shared code use something that belongs to one platform, the documentation describes **expect/actual** declarations. Shared code declares what it expects, and each platform supplies the actual version. The same page says that where a standard language feature such as an interface does the job, it is preferred.

For the screen, the documentation names three options: build the UI natively on each platform, share it with **Compose Multiplatform**, or mix them. Sharing the screen is a choice, not a requirement.

| Feature | Where it can live | Why |
|---|---|---|
| Price rules | Shared | Only numbers and rules |
| Login rules | Shared | Only text and rules |
| Saved data | Shared, as a storage module | The documentation names storage as a module you can share |
| Screen layout | Your choice | Native on each phone, or shared with Compose Multiplatform |
| Camera | Platform-specific, behind a shared promise | The phone's camera is the phone's own feature |
| Notifications | Platform-specific, behind a shared promise | Each phone has its own notification system |

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
    text: "Feature 1, the price rule"
    correct: true
    why: "It only uses numbers and a rule, so it is the same recipe in every kitchen. The camera and notification are the ovens."
  - id: c
    text: "Feature 2, the camera"
    why: "You may have picked the feature that sounds like a feature. The camera is the oven, so each kitchen needs its own cook."
output: "Feature 1, the price rule"
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
    text: "Yes, the whole app must be shared once you start"
    why: "You may have thought sharing is all or nothing. The Kotlin documentation says you share on your own terms, even one module."
  - id: b
    text: "No, sharing the screen is optional, and each phone can keep a native screen"
    correct: true
    why: "The recipe book can hold only some recipes. You may share the screen with Compose Multiplatform or build it natively, or mix both."
  - id: c
    text: "No, a screen can never be shared"
    why: "You may have heard that screens differ. Compose Multiplatform exists to share screens, and the choice is yours."
output: "No, sharing the screen is optional, and each phone can keep a native screen"
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
    why: "You may have thought shared code can reach every part of a phone. It cannot see the phone's own features by itself."
  - id: b
    text: "It states what it needs, and each phone supplies the real camera code"
    correct: true
    why: "The recipe says what it needs, and each kitchen provides its own oven. The documentation calls one way of doing this expect and actual."
  - id: c
    text: "It leaves the photo out of the app on one of the phones"
    why: "You may have thought a missing feature is the only answer. The shared recipe can wait for each phone to fill in the missing part."
output: "It states what it needs, and each phone supplies the real camera code"
```

## Tricks to remember

- One recipe book, one oven per kitchen.
- Numbers and rules go in the shared book. Phone features need their own oven.
- Share on your own terms: a little, or a lot.
- The screen is a choice. Camera and notifications are not.

## Common mistakes

- **Thinking Kotlin Multiplatform lets you write the whole app once, and it looks and works the same everywhere.** The slogan makes it sound like magic. In fact you choose what to share, and phone features still need each phone's own part.
- **Thinking you must share everything or nothing.** Starting small feels risky, so people think the move is all in. The documentation says you can share one module and grow later.
- **Thinking shared code can use the camera directly.** Kotlin runs on every phone, so it feels like it can reach everything. Shared code can only say what it expects. Each phone provides the real work.
- **Thinking sharing the screen is the point of KMP.** Shared screens get the most attention. Sharing price rules or login rules already saves effort and keeps both apps in agreement.

## Where this shows up in Android

A shop's Android app takes 5 off when the total is over 50, but the iPhone app takes 5 off only over 60. A customer sees different totals on two phones. This is what two copies of one rule do over time. Putting the price rule in shared code means both phones follow the same recipe.

On the Android side, the shared module works like any other Kotlin code your app uses, and the camera and notification parts remain in the Android part of the project. So when a symptom is "the two apps disagree", look first at logic that was copied. When it is "this feature works on one phone only", look at the part that talks to the phone itself.

## Recap

1. Kotlin Multiplatform shares the code you choose, from one module to most of the app.
2. Rules and data code can be shared. Phone features stay with each platform.
3. Sharing the screen is a choice, not a requirement.

## You can now

- Sort six features of an app into shared code, platform-specific code, or your choice.
- Explain why shared code cannot call a phone's camera directly, using the oven.
- Say why "write the whole app once" is not what KMP promises.
