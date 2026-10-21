```yaml
branch: android-core
bough: kotlin-android
stems:
  - id: what-is-an-app
    title: What Is an App
    why: A fresher must know what pieces an Android app is made of before writing any screen.
    leaves:
      - id: what-is-inside-an-app
        title: "What is actually inside an Android app?"
        difficulty: easy
        requires: []
        fruit: false
      - id: who-starts-your-app
        title: "Who starts my app if I never call main()?"
        difficulty: easy
        requires: [android-core/what-is-an-app/what-is-inside-an-app]
        fruit: false
  - id: activities
    title: Activities
    why: Every app opens through an Activity, and AI-written code passes its Context everywhere.
    leaves:
      - id: what-is-an-activity
        title: "What is the Activity that opens when my app starts?"
        difficulty: easy
        requires: [android-core/what-is-an-app/who-starts-your-app]
        fruit: false
      - id: what-is-context
        title: "What is a Context, the key card that Android functions ask for?"
        difficulty: medium
        requires: [android-core/activities/what-is-an-activity]
        fruit: false
      - id: back-stack
        title: "What happens to the old screen when I open a new one?"
        difficulty: medium
        requires: [android-core/activities/what-is-an-activity]
        fruit: false
  - id: lifecycle
    title: Lifecycle
    why: The system starts, pauses and stops screens on its own; code that ignores this leaks memory or crashes.
    leaves:
      - id: what-is-the-lifecycle
        title: "Why does Android keep telling my screen what state it is in?"
        difficulty: medium
        requires: [android-core/activities/what-is-an-activity]
        fruit: false
      - id: lifecycle-callbacks-order
        title: "In what order do onCreate, onStart, onResume and their partners run?"
        difficulty: medium
        requires: [android-core/lifecycle/what-is-the-lifecycle]
        fruit: false
      - id: lifecycle-aware-code
        title: "How can my code start and stop by itself with the screen?"
        difficulty: medium
        requires: [android-core/lifecycle/lifecycle-callbacks-order]
        fruit: false
      - id: leaking-an-activity
        title: "Why is keeping a reference to an Activity dangerous?"
        difficulty: hard
        requires: [android-core/activities/what-is-context, android-core/lifecycle/lifecycle-callbacks-order]
        fruit: false
  - id: configuration-changes
    title: Configuration Changes
    why: Rotation, resizing and language changes rebuild the screen, the most common cause of lost user input.
    leaves:
      - id: why-rotation-restarts
        title: "Why does turning my phone wipe what I typed?"
        difficulty: medium
        requires: [android-core/lifecycle/lifecycle-callbacks-order]
        fruit: false
      - id: surviving-a-config-change
        title: "Where can screen state live so it survives a rotation?"
        difficulty: hard
        requires: [android-core/configuration-changes/why-rotation-restarts]
        fruit: false
  - id: process-death
    title: Process Death
    why: Android kills background apps to free memory, and a real app must come back as if nothing happened.
    leaves:
      - id: what-is-process-death
        title: "Why does my app sometimes come back half-empty after I leave it for a while?"
        difficulty: hard
        requires: [android-core/configuration-changes/surviving-a-config-change]
        fruit: false
      - id: testing-process-death
        title: "How do I check that my app survives being killed in the background?"
        difficulty: easy
        requires: [android-core/process-death/what-is-process-death]
        fruit: true
  - id: manifest-and-resources
    title: Manifest and Resources
    why: The manifest is how the system learns what the app can do, and resources let one app fit every phone and language.
    leaves:
      - id: what-is-the-manifest
        title: "What does the manifest tell the system about my app?"
        difficulty: easy
        requires: [android-core/what-is-an-app/what-is-inside-an-app, android-core/activities/what-is-an-activity]
        fruit: false
      - id: sdk-versions
        title: "What do minSdk, targetSdk and compileSdk decide?"
        difficulty: medium
        requires: [android-core/manifest-and-resources/what-is-the-manifest]
        fruit: false
      - id: what-are-resources
        title: "Why do texts and images live in a res folder instead of in my code?"
        difficulty: easy
        requires: [android-core/activities/what-is-an-activity]
        fruit: false
      - id: resource-qualifiers
        title: "How does the app pick Hindi text on a phone set to Hindi?"
        difficulty: medium
        requires: [android-core/manifest-and-resources/what-are-resources]
        fruit: false
  - id: intents
    title: Intents
    why: Intents are how screens and apps ask each other for things, including links from the web.
    leaves:
      - id: what-is-an-intent
        title: "How does one screen ask for another screen to open?"
        difficulty: easy
        requires: [android-core/activities/what-is-an-activity, android-core/activities/back-stack]
        fruit: false
      - id: implicit-intents
        title: "How can my app ask any app that can share to share this?"
        difficulty: medium
        requires: [android-core/intents/what-is-an-intent, android-core/manifest-and-resources/what-is-the-manifest]
        fruit: false
      - id: getting-a-result-back
        title: "How does my app get an answer back from another screen or app?"
        difficulty: medium
        requires: [android-core/intents/implicit-intents]
        fruit: false
      - id: deep-links
        title: "How does tapping a web link open a screen inside my app?"
        difficulty: medium
        requires: [android-core/intents/implicit-intents]
        fruit: false
  - id: permissions
    title: Permissions
    why: Camera, location and notifications need the user's yes, and apps that ask badly get uninstalled.
    leaves:
      - id: why-permissions
        title: "Why must my app ask before using the camera?"
        difficulty: easy
        requires: [android-core/manifest-and-resources/what-is-the-manifest]
        fruit: false
      - id: asking-at-runtime
        title: "How do I ask for a permission at the right moment?"
        difficulty: medium
        requires: [android-core/permissions/why-permissions, android-core/intents/getting-a-result-back]
        fruit: false
      - id: when-the-user-says-no
        title: "What should my app do when the user says no?"
        difficulty: medium
        requires: [android-core/permissions/asking-at-runtime]
        fruit: false
  - id: background-work
    title: Background Work
    why: Uploads and syncs must finish after the user leaves, inside the limits Android sets to save battery.
    leaves:
      - id: why-background-is-limited
        title: "Why does Android stop my app from working when it is not on screen?"
        difficulty: medium
        requires: [android-core/lifecycle/what-is-the-lifecycle, android-core/process-death/what-is-process-death]
        fruit: false
      - id: what-is-workmanager
        title: "How do I run work that must finish even if the user closes the app?"
        difficulty: medium
        requires: [android-core/background-work/why-background-is-limited]
        fruit: false
      - id: work-constraints
        title: "How do I make work wait until the phone is on Wi-Fi or charging?"
        difficulty: easy
        requires: [android-core/background-work/what-is-workmanager]
        fruit: false
      - id: when-work-fails
        title: "What happens when background work fails halfway?"
        difficulty: medium
        requires: [android-core/background-work/what-is-workmanager]
        fruit: false
  - id: services-and-notifications
    title: Services and Notifications
    why: Music, navigation and calls keep running with a visible notification, under strict system rules.
    leaves:
      - id: what-is-a-notification
        title: "How does my app show a message when the user is not in it?"
        difficulty: medium
        requires: [android-core/permissions/asking-at-runtime]
        fruit: false
      - id: what-is-a-foreground-service
        title: "How does a music app keep playing after I leave it?"
        difficulty: hard
        requires: [android-core/services-and-notifications/what-is-a-notification, android-core/background-work/why-background-is-limited]
        fruit: false
      - id: choosing-background-tool
        title: "Should this job be a coroutine, WorkManager or a foreground service?"
        difficulty: medium
        requires: [android-core/background-work/what-is-workmanager, android-core/services-and-notifications/what-is-a-foreground-service]
        fruit: false
  - id: files-and-sharing
    title: Files and Sharing
    why: Apps save files, take photos from the user and hand files to other apps without exposing everything.
    leaves:
      - id: app-private-files
        title: "Where can my app save a file that only it can read?"
        difficulty: easy
        requires: [android-core/activities/what-is-context]
        fruit: false
      - id: picking-photos-and-files
        title: "How can the user give my app one photo without opening the whole gallery?"
        difficulty: medium
        requires: [android-core/intents/getting-a-result-back, android-core/permissions/when-the-user-says-no]
        fruit: false
      - id: sharing-with-other-apps
        title: "How do I send a file to another app safely?"
        difficulty: hard
        requires: [android-core/intents/implicit-intents, android-core/files-and-sharing/app-private-files]
        fruit: false
  - id: app-startup
    title: App Startup
    why: The first second decides if users stay, and it ties together everything the branch taught.
    leaves:
      - id: what-is-the-application-class
        title: "What runs before my first screen appears?"
        difficulty: easy
        requires: [android-core/what-is-an-app/who-starts-your-app, android-core/activities/what-is-context]
        fruit: false
      - id: cold-warm-hot-start
        title: "Why does my app open slowly the first time and fast the next?"
        difficulty: medium
        requires: [android-core/app-startup/what-is-the-application-class, android-core/process-death/what-is-process-death]
        fruit: false
      - id: from-tap-to-first-frame
        title: "What happens, step by step, between tapping my icon and seeing my screen?"
        difficulty: medium
        requires: [android-core/app-startup/cold-warm-hot-start, android-core/lifecycle/lifecycle-callbacks-order]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Sims in this branch are browser animations of what changes on the phone screen; there is no Android emulator (L15, D8).

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file` (phase 1) — used by what-is-inside-an-app.
- Assumes Foundation: `foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon` (phase 1) — used by who-starts-your-app.
- Assumes Foundation: `foundation/from-code-to-running/what-is-art` (phase 1) — used by who-starts-your-app and cold-warm-hot-start.
- Assumes Kotlin: `kotlin/functions/declaring-functions`, `kotlin/lambdas/lambda-syntax` and `kotlin/classes-and-objects/what-is-a-class` (phase 2) — every code sample defines classes and functions and passes lambdas.
- Assumes Kotlin: `kotlin/interfaces-and-inheritance/open-and-override` (phase 3, overriding `onCreate`) — used by what-is-an-activity and lifecycle-callbacks-order.
- Assumes Kotlin: `kotlin/null-safety/safe-call-and-elvis` (phase 2) — the saved-state Bundle and intent extras can be null.
- Assumes OS: `os/processes/what-is-a-process` (what a process is) and `os/processes/process-states` — used by who-starts-your-app and what-is-process-death.
- Assumes OS: `os/memory/what-keeps-an-object-alive` (what holds an object alive) — used by leaking-an-activity.
- Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine` (phase 4) — used by what-is-workmanager (phase 5 stem, so allowed).
- Helps to know Tools: `tools/adb-and-logcat/adb-basics` (phase 5) — testing-process-death recaps the one adb command it needs.
- Helps to know Tools: `tools/gradle-basics/what-gradle-does` (phase 5) — sdk-versions recaps where the numbers live in the build file.
- Helps to know UI: `ui/state-and-recomposition/remember-saveable` — surviving-a-config-change mentions rememberSaveable in one paragraph.
- Helps to know Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel` (phase 4) — surviving-a-config-change and what-is-process-death recap a ViewModel in one paragraph; they never depend on it.

Challenge note: Android framework code does not run in the Kotlin Playground. Every `P:` and `F:` on framework code is either a plain-Kotlin model of the behaviour (for example a fake lifecycle that prints callbacks) or a read-only "which line is wrong" task. ⚠️ VERIFY with `docs/06` once written.

## Stems

### what-is-an-app · What Is an App

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-inside-an-app` | easy | "What are the four kinds of building blocks an Android app can have?" | A restaurant: the dining room you see (Activity), the kitchen working out of sight (Service), the doorbell (receiver), the shared pantry (provider). | "An app is one big program that runs from top to bottom." | none: a labelled diagram of the four parts is enough; nothing moves | `P:` on a diagram: which part keeps working after the screen closes · `B:` remove the screen part and say what the user loses | Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file`. Content providers and receivers are named only; depth is in `## Later`. |
| `who-starts-your-app` | easy | "Who calls my code first if I never write main()?" | A theatre: the stage manager decides when each actor walks on; actors never walk on by themselves. | "My app runs its own code first, like a console program." | sim: code-to-screen the learner flips between a manifest that declares the launcher screen and one that does not; a ball rolls from the tapped icon through launcher, system and a new process into the first screen, or stops at the missing step and the drawn phone shows the app failing to open | `P:` on the sim: which box exists before the first screen does · `B:` predict what happens if the screen is never declared | Assumes Foundation: `foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon`. Assumes OS: `os/processes/what-is-a-process`. |

### activities · Activities

Flags: phase=3 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-an-activity` | easy | "What does MainActivity do before my Compose screen shows?" | A picture frame hung on the wall: the frame is given to you; you choose the picture (setContent). | "Every screen in my app needs its own Activity." | none: one rule about where the screen starts; the editor and a short reference video are enough | `P:` read a 10-line MainActivity and say which line puts the screen up · `B:` move setContent above super.onCreate and predict the effect | Assumes Kotlin: `kotlin/interfaces-and-inheritance/open-and-override`. Single-Activity apps are the default today. ⚠️ VERIFY: current Android Studio "Empty Activity" template uses ComponentActivity and enableEdgeToEdge. |
| `what-is-context` | medium | "Why does almost every Android function ask for a Context?" | A hotel room key card: it opens your room, the gym, the pool; an old card from a checked-out guest should not be kept. | "Any Context is the same, so pass whichever one is nearby." | none: one rule (which Context to pass); the leak itself is animated in leaking-an-activity | `P:` which of three calls needs the Activity and which can take the application Context · `F(ai):` a helper object that stores an Activity Context "for convenience" | Leads into leaking-an-activity. Which Context to pass is a one-line rule of thumb inside this one idea, not a second topic. |
| `back-stack` | medium | "Why does pressing back return me to the screen before?" | A stack of plates: new plates go on top; you can only take the top one off. | "The old screen is closed and gone when a new one opens." | sim: code-to-screen the learner flips between a start call that always opens a new screen and one that reuses the screen already on top; screens drop as plates onto a stack, and back lifts the top plate off with a predictive peek of the plate below | `P:` open A, B, C, press back twice: which screen shows · `B:` open the same screen three times and count the plates | volatile. Checked 18 Oct 2026 (developer.android.com, Android 16 behaviour changes): apps targeting API 36 on Android 16+ get predictive back animations by default and onBackPressed is not called. ⚠️ VERIFY again before live. |

### lifecycle · Lifecycle

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-the-lifecycle` | medium | "What states can a screen be in while the app is open?" | A shop's day: unlocked, lights on, doors open to customers, doors shut, locked for the night. | "My screen is either open or closed, nothing in between." | sim: code-to-screen the learner picks one of three small screen-code variants (empty, work in onResume, work in onStart) and presses Home, opens another app or rotates; a lift moves between floors Created, Started, Resumed and the drawn phone shows what the screen does on each floor | `P:` on the sim: which floor is the screen on while a dialog from another app covers it · `B:` press Home in the sim and watch which floors are passed | Assumes Kotlin: `kotlin/classes-and-objects/what-is-a-class`. |
| `lifecycle-callbacks-order` | medium | "Which callback runs when the user presses Home, and which when they come back?" | Morning and night routine: you always wake before you dress, and undress before you sleep, in the same order. | "onDestroy always runs when the user leaves the app." | sim: code-to-screen reuse what-is-the-lifecycle sim, with each floor printing its callback name to a log as the lift passes, and the learner flips between a variant that saves the draft in onStop and one that saves it only in onDestroy | `P:` predict the log for open, Home, return (plain-Kotlin model of the callbacks) · `F(ai):` a generated screen saves the draft in onDestroy only · `B:` swap onStart and onResume work | Plain-Kotlin model only; framework code is read, not run. |
| `lifecycle-aware-code` | medium | "How can a location tracker stop by itself when my screen goes away?" | Motion-sensor lights in a corridor: they turn on when someone enters and off when they leave, with no switch to forget. | "I have to remember to stop everything by hand in onStop." | sim: code-to-screen the learner flips between a manual start-and-stop version and a lifecycle-aware version; a lamp attached to the lift of the what-is-the-lifecycle sim lights only on Started and above, and in the manual version it stays lit after the screen leaves | `P:` which observer callback fires on rotation · `F(ai):` tidy code that starts a listener in onStart and never stops it | Helps to know Concurrency: `concurrency/scopes/scopes-in-android-code` (lifecycleScope named only). |
| `leaking-an-activity` | hard | "Why does holding an old screen in a variable fill up memory?" | A coat-check ticket: while you keep the ticket, the coat cannot be given away, even after you left the building. | "When a screen closes, everything it used is freed automatically." | sim: code-to-screen the learner flips between a variant that caches the Activity in a long-lived object and a variant that holds the application Context; each rotation makes a new screen box, and in the first variant a rope from the long-lived object keeps old boxes from falling into the bin until memory fills and the app crashes | `F(ai):` a confident singleton that caches the Activity for "faster access" · `F(ai):` a callback registered and never removed · `P:` how many screens are in memory after 3 rotations | Assumes OS: `os/memory/what-keeps-an-object-alive`. Helps to know Profiling: `profiling/memory-and-leaks/find-a-leak`. |

### configuration-changes · Configuration Changes

Flags: phase=3 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-rotation-restarts` | medium | "Why is my screen rebuilt from scratch when the phone rotates?" | A shop that rebuilds its whole window display each time the season changes; loose items on the counter get swept away. | "Rotation only turns the picture; my variables stay." | sim: code-to-screen the learner flips between a plain-field version and a saved-state version of the same counter screen, then rotates the drawn phone; the screen box breaks and is rebuilt, and loose balls (plain variables) roll away while balls in a labelled safe box stay | `P:` counter shows 5, rotate: what does it show (plain-Kotlin model) · `F(ai):` a generated screen keeps the form text in a plain Activity field · `B:` change language in the sim and watch the same rebuild | volatile. Checked 18 Oct 2026 (Android 16 behaviour changes): apps targeting API 36 cannot lock orientation or resizability on displays sw600dp and wider, and the opt-out ends at API 37. Android 17 page: the keyboard is not kept visible after rotation unless requested. ⚠️ VERIFY before live. |
| `surviving-a-config-change` | hard | "Which place keeps my state through a rotation: a field, saved state or a ViewModel?" | Three places to leave your bag on a train: on the seat (lost at the next stop), at the luggage desk (small items only, survives), with a friend who stays on board (big items, until the trip ends). | "Saving state means writing everything to a file." | sim: code-to-screen reuse why-rotation-restarts sim; the learner flips between field, saved-state and ViewModel code variants for each piece of state (three containers of different size and toughness), then rotates or kills the process and sees which balls survive | `F(ai):` a full list of 500 items pushed into the saved-state Bundle · `P:` which container survives rotation but not the app being killed · `B:` put a big object into the small saved box | Helps to know UI: `ui/state-and-recomposition/remember-saveable`. Helps to know Architecture and data: `architecture-data/viewmodel/viewmodel-lifetime`. ⚠️ VERIFY Bundle size guidance on developer.android.com. |

### process-death · Process Death

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-process-death` | hard | "Why can my app be killed while it still shows in the recent apps list?" | A restaurant that clears your table while you step out, but keeps a note so a waiter can reset your plates when you return. | "If my app is in the recent apps list, it is still running." | sim: code-to-screen reuse surviving-a-config-change code variants (field, saved state, ViewModel); several app boxes in memory, opening a big game pushes the oldest boxes off the shelf, and returning rebuilds the drawn screen from the small saved note only | `P:` after process death, which of field, saved state and ViewModel still holds the value · `B:` fill memory in the sim until your app is cleared | Assumes OS: `os/processes/process-states`. Helps to know OS: `os/android-os/low-memory-killer` (phase 6). |
| `testing-process-death` | easy | "How can I make Android kill my app on purpose to test it?" | A fire drill: you do not wait for a real fire to find out if people know the exit. | "If rotation works, process death works too." | none: tooling steps (developer option and one adb command), shown in a reference video | `P:` which value is lost after the drill in a given screen · `F:` a screen that passes the rotation test but loses data after the drill | Helps to know Tools: `tools/adb-and-logcat/adb-basics`. Fruit (see `## Fruits`). ⚠️ VERIFY current name of the "Don't keep activities" developer option. |

### manifest-and-resources · Manifest and Resources

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-the-manifest` | easy | "How does the phone know my app has a camera screen before it runs any of my code?" | The sign and menu outside a shop: people know what is inside before they walk in. | "The manifest is generated junk I never need to read." | none: a file-reading skill; an annotated manifest on the page is enough | `P:` read a manifest: which screen opens from the launcher · `F:` a screen that crashes when opened because it is not declared (read-only) | volatile. ⚠️ VERIFY: the manifest entries the current Empty Activity template generates and the launcher intent filter. |
| `sdk-versions` | medium | "Why does the same code behave differently when I raise targetSdk?" | A building code: minSdk is the oldest house you will build for, targetSdk is the rulebook year you promise to follow, compileSdk is the newest catalogue of parts you can order. | "Raising targetSdk only unlocks new features; nothing old changes." | none: three numbers and one rule; a table of behaviour changes per target is clearer than motion | `P:` given three numbers, can the app use a newer feature and on which phones · `F(ai):` a generated build file that sets targetSdk lower than the store allows · `B:` raise targetSdk and list which behaviour pages to read | volatile. Helps to know Tools: `tools/gradle-basics/what-gradle-does`. ⚠️ VERIFY: current Google Play minimum targetSdk and how Android Studio sets compileSdk. |
| `what-are-resources` | easy | "Why should button text live in strings.xml instead of inside my code?" | A restaurant menu printed separately from the kitchen: you can reprint it in a new language without retraining the cooks. | "Resources are only for images." | none: one rule about where things live; the editor and a folder diagram are enough | `P:` which R name points to which file · `F(ai):` a screen with every label hard-coded in the Kotlin code | Helps to know UI: `ui/first-composable/text-and-images` (stringResource named only). volatile. ⚠️ VERIFY: the resource folder layout and string-resource tooling in current Android Studio. |
| `resource-qualifiers` | medium | "How does the app choose between values-hi and values when the phone is set to Hindi?" | A post office sorter: each letter goes to the most specific matching box, and to the general box if no specific box fits. | "I have to check the language in code with an if." | sim: a sorting machine takes device labels (language, screen density, width) and drops each resource request into the best-matching folder, falling back to the default folder | `P:` on the sim: phone in Marathi with only values and values-hi, which text shows · `B:` delete the default folder and watch the crash | Kotree has no dark mode (L9), but learners' apps may use a night qualifier; mention it only as one more label. volatile. ⚠️ VERIFY: how the app picks a language on current Android (per-app language setting and the qualifier fallback order). |

### intents · Intents

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-an-intent` | easy | "How does a button open another screen that the system then puts on the back stack?" | A written request slip handed to a receptionist, naming exactly which room you want. | "One screen calls the other screen's code directly." | none: one rule; the back-stack sim already shows the result | `P:` which screen is on top after startActivity · `F:` an extra written under one key and read under another (plain-Kotlin map model) | — |
| `implicit-intents` | medium | "How does Android find an app that can open a web page for me?" | Asking a help desk "who can fix a tap?" instead of naming one plumber. | "If no app matches, Android shows a polite message by itself." | sim: code-to-screen the learner flips between an intent with no check, an intent with a check, and a manifest with or without a matching filter; a request ball labelled with an action rolls through a sorting office and lands with every app whose sign matches, or falls off with a crash if none does | `P:` with two matching apps, what does the user see · `F(ai):` tidy share code with no check that any app can handle it · `B:` remove the intent filter in the sim | volatile. ⚠️ VERIFY: package visibility rules (queries element) for apps targeting recent APIs. |
| `getting-a-result-back` | medium | "How does my screen receive the photo the camera app took?" | Leaving a self-addressed envelope with a request, so the answer comes back to the right desk. | "I can wait for the answer on the next line, like a normal function call." | none: the order of events is shown with the plain-Kotlin model of a callback | `P:` in what order do the two log lines print (callback model) · `F(ai):` a generated screen registers for a result inside a click handler · `B:` rotate before the answer arrives | ⚠️ VERIFY: Activity Result API rule that registration must happen before the screen is started. |
| `deep-links` | medium | "How does a link in a message open the right product screen in my app?" | A house address: the postman can walk straight to one room if the address names it. | "Any link to my website opens my app automatically." | none: a mapping rule from link to screen; a flow diagram and reference video are enough | `P:` which screen opens for a given link and filter · `F:` a link that opens the app on the home screen instead of the product | volatile. ⚠️ VERIFY: current App Links verification (assetlinks.json) and how Android shows unverified links. |

### permissions · Permissions

Flags: phase=5 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-permissions` | easy | "Why can my app use the internet without asking but must ask for the camera?" | A building: the lobby is open to everyone, but the server room needs a signed pass from the owner. | "Writing a permission in the manifest is enough to use the camera." | none: a single rule (normal vs dangerous); a two-column table is enough | `P:` which of five permissions shows a dialog · `F(ai):` a manifest asking for six permissions the app never uses | volatile. ⚠️ VERIFY list of runtime permissions on current Android. |
| `asking-at-runtime` | medium | "When is the right moment to show the permission dialog?" | Asking a neighbour to borrow a ladder when you are holding the light bulb, not the day you move in. | "Ask for everything on the first launch so it is done." | none: a timing rule best shown with a short reference video of good and bad prompts | `F(ai):` generated code that requests camera, location and contacts in onCreate · `P:` what the dialog shows on second ask | volatile. Checked fact needed: notification permission on Android 13+ ⚠️ VERIFY wording. |
| `when-the-user-says-no` | medium | "What should my app show after the user denies a permission twice?" | A shop assistant who hears "no thanks" and shows another way to pay instead of blocking the door. | "If the user says no, I can just ask again until they say yes." | none: branching rule; a flow chart with three exits is enough | `F(ai):` tidy code that crashes when the permission is denied · `F(ai):` a loop that keeps asking · `B:` deny forever and find what still works | volatile. ⚠️ VERIFY: "only this time" and approximate-location options; how often the system still shows the dialog after denial. |

### background-work · Background Work

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-background-is-limited` | medium | "Why does my timer stop when the phone has been in my pocket for an hour?" | A shared water tank in a building: if every flat leaves its tap running at night, nobody has water in the morning, so the caretaker closes taps. | "My app can keep running forever if I start a thread." | sim: code-to-screen the learner flips between a plain thread that loops forever and a scheduled-work variant; a battery tank drains through one tap per background app, the system closes taps as the screen sleeps and opens them in short maintenance windows, and a timeline shows when each variant gets to run | `P:` on the sim: when does a sleeping phone let the job run · `B:` open ten taps and watch the battery | volatile. Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen`. ⚠️ VERIFY current Doze and App Standby bucket behaviour. |
| `what-is-workmanager` | medium | "How do I upload a photo so it finishes even if the user swipes my app away?" | A courier booking: you hand over the parcel and a receipt; the courier delivers it even if you leave home. | "WorkManager runs my work right now, exactly when I ask." | none: the timing is shown by reuse of why-background-is-limited sim in work-constraints and when-work-fails | `P:` which runs first, the next line of code or the work · `F(ai):` a generated Worker that launches a coroutine on its own scope and returns success at once | Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine` (CoroutineWorker). ⚠️ VERIFY current WorkManager guide. |
| `work-constraints` | easy | "How do I make a big download wait for Wi-Fi?" | A delivery marked "only when someone is home": the courier waits for the condition, not the clock. | "If the condition is false, the work is cancelled." | sim: code-to-screen reuse why-background-is-limited sim with switches for Wi-Fi and charging; a parcel waits at the gate until both lights are green | `P:` on the sim: phone on mobile data, charging; does the work start · `B:` add an impossible condition | — |
| `when-work-fails` | medium | "What happens to my upload if the network drops halfway?" | A courier who finds nobody home comes back later, waiting longer each time. | "Failed work is retried instantly and forever." | sim: code-to-screen the learner flips a Worker between returning success, retry and failure; a ball bounces back after each failed attempt with a growing gap between tries (backoff), until success or a give-up line | `P:` with a 10-second backoff that doubles, when is try 3 · `F:` a Worker that returns failure for a temporary network error | ⚠️ VERIFY current minimum backoff and retry rules in WorkManager docs. |

### services-and-notifications · Services and Notifications

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-notification` | medium | "Why does my notification never appear on a new phone?" | A building notice board with labelled sections: you must pin your notice in a section, and the tenant must agree to see notices at all. | "Building a notification is enough to show it." | none: rules (channel, permission) best shown with a checklist and a reference video | `P:` which of three missing pieces stops the notification · `F(ai):` generated code with no channel and no permission check | volatile. ⚠️ VERIFY: notification permission from Android 13 and channel rules. |
| `what-is-a-foreground-service` | hard | "How does a music app keep playing after I leave it?" | A street musician with a licence badge on show: they can keep playing in public only while the badge is visible. | "A service is a background thread that runs as long as I like." | sim: code-to-screen the learner flips between a service started with and without the foreground call, and between service types; a service box sits in memory, and without a visible badge (notification) the system sweeps it away, with a badge it stays until a timer for its type runs out | `P:` which service type fits a step counter · `F(ai):` a confident service started without a type · `B:` remove the notification in the sim | volatile. Checked 18 Oct 2026 (Android 15 behaviour changes): dataSync foreground services may run 6 hours in 24 and get onTimeout; BOOT_COMPLETED cannot start some types. ⚠️ VERIFY type rules (Android 14+) before live. |
| `choosing-background-tool` | medium | "Which tool do I pick for a sync, a song and a quick save?" | Choosing between walking, a bus and a taxi: each fits a distance, a cost and a deadline. | "WorkManager is always the right answer for anything in the background." | none: a decision chart; P: on the chart | `P:` match five jobs to three tools · `F(ai):` generated code that uses a foreground service for a one-second save | Helps to know Concurrency: `concurrency/scopes/what-is-a-coroutine-scope`. |

### files-and-sharing · Files and Sharing

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `app-private-files` | easy | "Where can my app save a draft that other apps cannot read?" | A locker in a gym that only your key opens, plus a tray by the door that staff empty when it is full (cache). | "Any file I save is visible in the phone's Files app." | none: a folder diagram with two places is enough | `P:` after uninstall, which file is still there · `F:` a draft saved to cache and later missing | Helps to know OS: `os/files/files-folders-and-owners` (phase 6, recap given). |
| `picking-photos-and-files` | medium | "How does my app get one photo the user chose without a storage permission?" | A bank teller who passes you one document through the window instead of giving you the vault key. | "To read a photo I must ask for permission to read all storage." | none: the flow is the result-back flow already taught; a reference video shows the picker | `F(ai):` generated code asking for full media access to pick one avatar · `P:` what the app receives (a content address, not a file path) | volatile. ⚠️ VERIFY: photo picker availability across versions and current media permissions. |
| `sharing-with-other-apps` | hard | "How do I let an email app read one file of mine without exposing the rest?" | A valet ticket: it lets one person drive one car for a short time, not open your garage. | "I can just send the other app my file path." | none: a permission hand-over is clearer as a three-step diagram than as motion | `F(ai):` tidy share code that sends a raw file path · `P:` can the receiving app still read the file tomorrow | volatile. Checked 18 Oct 2026 (Android 17 behaviour changes page): automatic URI permissions for Send and ImageCapture intents are planned to end in Android 18; grant them explicitly. ⚠️ VERIFY before live. |

### app-startup · App Startup

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-the-application-class` | easy | "Which of my code runs once, before any screen, every time the app starts?" | Opening a shop in the morning: lights and till first, before the first customer walks in. | "Application.onCreate runs only once, at install." | none: one rule about what runs first; the full timeline is animated in from-tap-to-first-frame | `F(ai):` generated code that loads a big database and three SDKs in Application.onCreate · `P:` how many times does it run after process death | volatile. ⚠️ VERIFY: current startup guidance on what belongs in Application.onCreate (developer.android.com app startup). |
| `cold-warm-hot-start` | medium | "Why is opening my app after a reboot slower than opening it again a minute later?" | Starting a car on a winter morning versus restarting it after a short stop. | "Startup time is just how fast my first screen draws." | sim: code-to-screen the learner flips Application.onCreate between empty and one second of work; three tracks race from icon tap to first frame, cold start must build the process, warm reuses it, hot only brings the screen forward, and the drawn phone shows when the first frame appears | `P:` after process death, which start is it · `B:` add one second of work to Application in the sim | volatile. Helps to know Profiling: `profiling/startup/measure-startup`. ⚠️ VERIFY current definitions and splash screen API behaviour. |
| `from-tap-to-first-frame` | medium | "Can I name, in order, every step from a tap on the icon to the first frame?" | A train journey from ticket gate to seat: every station in order, and where delays happen. | "The app starts with my first screen's code." | sim: code-to-screen reuse who-starts-your-app sim extended with Application, Activity callbacks and first frame, with a stopwatch per step; the learner flips between a startup that loads everything in Application.onCreate and one that defers it | `P:` order eight shuffled steps · `F(ai):` a review of a generated startup path: find the step that blocks the first frame | Synthesis leaf. Fruit (see `## Fruits`). volatile. ⚠️ VERIFY: the current names of the steps between process start and first frame, including the splash screen step. |

## Fruits

| Leaf | Capstone | Constraints | Proves |
|---|---|---|---|
| `android-core/process-death/testing-process-death` | "A notes screen that never loses a word" | One screen with a text field and a list · survives rotation, language change and process death · prove each with a recorded test run · no data written to disk except saved state · write which container holds each piece of state and why | The app keeps user input through rotation and being killed (`docs/01` §6.2 android-core ability). ⚠️ VERIFY exact ability wording in `docs/01`. |
| `android-core/app-startup/from-tap-to-first-frame` | "A photo-backup helper" | Lets the user pick photos without full storage access · uploads them in the background only on Wi-Fi, with retries · shows a notification when done, after asking at the right moment · handles a denied permission without crashing · cold start under a target you measure and state | Uses intents, permissions, background work, notifications, files and startup together (L24). |

## Later

- Fragments, because new apps use one Activity with Compose; reading old fragment code is a later topic.
- Content providers in depth, because apps rarely write their own; reading one through a content address is enough here.
- Broadcast receivers in depth, because most uses moved to WorkManager and system callbacks.
- Home-screen widgets, a separate UI toolkit not needed for a first full app.
- App shortcuts, nice polish but not needed to ship.
- Multi-window details, beyond what configuration changes already teach.
- Chaining several background jobs, useful but not needed for a first real sync.
- Bound services and inter-process communication, rare in modern apps.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 12 stems, 37 leaves, all shadow. | +37 | Est. 34, band 27–41: inside the band. Added what-is-context, sdk-versions, deep-links and from-tap-to-first-frame beyond the sketch because AI-written Android code depends on them; merged the sketch's Activity basics into one leaf. Stem manifest-and-resources placed before intents because intent filters live in the manifest. |
| 2026-10-18 | Foundation and Kotlin `Assumes` changed from stem IDs to full leaf IDs. | 0 | Those two roadmaps now exist. |
| 2026-10-18 | A2e review fixes: eight Android-behaviour sims (`who-starts-your-app`, `back-stack`, `what-is-the-lifecycle`, `lifecycle-callbacks-order`, `what-is-process-death`, `why-background-is-limited`, `cold-warm-hot-start`, `from-tap-to-first-frame`) now say `sim: code-to-screen` and name the code variants the learner flips (`docs/08` §6, author decision 18 Oct 2026). `what-is-context` reworded to one idea (what a Context is); slug unchanged. `⚠️ VERIFY` notes added to five volatile leaves. | 0 | No leaf added or removed. |
