# Prompt: interview questions, build exercises and official references for Kotree (copy everything below the line into the other agent)

---

You are helping build a learning website for an Android/Kotlin/KMP developer whose goal is to understand these topics deeply enough to pass interviews from INTERN to SENIOR level and to build and optimise real apps. Below is the full list of topics (leaves) that are NOT algorithms/OS: Kotlin, Android core, UI/Compose, architecture/data, concurrency/coroutines, testing, profiling, tools, KMP, networking, app networking, security, native/NDK, foundation.

TASK: for EVERY leaf listed, produce the data below. Accuracy beats volume: leave a field out rather than guess.

For each leaf give:
1. `interview-questions`: 4 to 6 questions that interviewers REALLY ask about this topic, tagged with the level they are usually asked at: `[intern]`, `[junior]`, `[mid]`, `[senior]`. Include at least one "why / trade-off" question and, where fitting, one "what goes wrong in production" question.
2. `what-interviewers-listen-for`: 2 to 3 short bullet points (the key idea, the trade-off, the pitfall a strong answer mentions).
3. `build-exercise`: ONE small hands-on exercise (30 to 90 minutes) that makes the reader truly understand the topic by building or breaking something (for example "write a tiny ViewModel that survives rotation, then kill the process and watch what is lost"). Describe the task and the expected observation in 2 to 3 lines. Do not copy long code.
4. `official-references`: 1 to 3 official links (developer.android.com, kotlinlang.org, source.android.com, kotlinx.coroutines docs, kotlinlang.org/docs/multiplatform) with the exact page title. Only links you are sure exist. Include a Google or JetBrains codelab if one exists for the topic (exact title and URL).
5. `common-wrong-beliefs`: 1 to 2 things beginners and mid-level developers wrongly believe about this topic (one line each, with the correction).

RULES:
- Do not invent URLs, library names, API names or version numbers. If unsure, omit.
- Prefer current practice (Kotlin 2.x, Jetpack Compose, Navigation, Hilt/Koin, coroutines + Flow, Room, Retrofit/Ktor, KMP with Compose Multiplatform).
- Do not copy copyrighted problem statements or articles; titles, links and your own short descriptions only.
- Output one Markdown file per branch named `interview-<branch>.md` in exactly this shape so a script can read it. The heading id must match the leaf id below.

```
# interview: kotlin

## kotlin/<stem-id>/<leaf-id>
interview-questions:
- [junior] What is the difference between val and var, and what does val NOT guarantee?
- [mid] ...
what-interviewers-listen-for:
- ...
build-exercise: ...
official-references:
- <exact page title> | <url>
common-wrong-beliefs:
- Belief | Correction
```

- At the end of each branch file add `## Self-check` listing the 10 items (links or API names) you are LEAST sure about.

TOPICS (391 leaves). Format: `branch/stem/leaf` : question the leaf answers [difficulty].

## Branch: android-core


### Stem: what-is-an-app (What Is an App)

- `android-core/what-is-an-app/what-is-inside-an-app` : What is actually inside an Android app? [easy]
- `android-core/what-is-an-app/who-starts-your-app` : Who starts my app if I never call main()? [easy]

### Stem: activities (Activities)

- `android-core/activities/what-is-an-activity` : What is the Activity that opens when my app starts? [easy]
- `android-core/activities/what-is-context` : What is a Context, the key card that Android functions ask for? [medium]
- `android-core/activities/back-stack` : What happens to the old screen when I open a new one? [medium]

### Stem: lifecycle (Lifecycle)

- `android-core/lifecycle/what-is-the-lifecycle` : Why does Android keep telling my screen what state it is in? [medium]
- `android-core/lifecycle/lifecycle-callbacks-order` : In what order do onCreate, onStart, onResume and their partners run? [medium]
- `android-core/lifecycle/lifecycle-aware-code` : How can my code start and stop by itself with the screen? [medium]
- `android-core/lifecycle/leaking-an-activity` : Why is keeping a reference to an Activity dangerous? [hard]

### Stem: configuration-changes (Configuration Changes)

- `android-core/configuration-changes/why-rotation-restarts` : Why does turning my phone wipe what I typed? [medium]
- `android-core/configuration-changes/surviving-a-config-change` : Where can screen state live so it survives a rotation? [hard]

### Stem: process-death (Process Death)

- `android-core/process-death/what-is-process-death` : Why does my app sometimes come back half-empty after I leave it for a while? [hard]
- `android-core/process-death/testing-process-death` : How do I check that my app survives being killed in the background? [easy]

### Stem: manifest-and-resources (Manifest and Resources)

- `android-core/manifest-and-resources/what-is-the-manifest` : What does the manifest tell the system about my app? [easy]
- `android-core/manifest-and-resources/sdk-versions` : What do minSdk, targetSdk and compileSdk decide? [medium]
- `android-core/manifest-and-resources/what-are-resources` : Why do texts and images live in a res folder instead of in my code? [easy]
- `android-core/manifest-and-resources/resource-qualifiers` : How does the app pick Hindi text on a phone set to Hindi? [medium]

### Stem: intents (Intents)

- `android-core/intents/what-is-an-intent` : How does one screen ask for another screen to open? [easy]
- `android-core/intents/implicit-intents` : How can my app ask any app that can share to share this? [medium]
- `android-core/intents/getting-a-result-back` : How does my app get an answer back from another screen or app? [medium]
- `android-core/intents/deep-links` : How does tapping a web link open a screen inside my app? [medium]

### Stem: permissions (Permissions)

- `android-core/permissions/why-permissions` : Why must my app ask before using the camera? [easy]
- `android-core/permissions/asking-at-runtime` : How do I ask for a permission at the right moment? [medium]
- `android-core/permissions/when-the-user-says-no` : What should my app do when the user says no? [medium]

### Stem: background-work (Background Work)

- `android-core/background-work/why-background-is-limited` : Why does Android stop my app from working when it is not on screen? [medium]
- `android-core/background-work/what-is-workmanager` : How do I run work that must finish even if the user closes the app? [medium]
- `android-core/background-work/work-constraints` : How do I make work wait until the phone is on Wi-Fi or charging? [easy]
- `android-core/background-work/when-work-fails` : What happens when background work fails halfway? [medium]

### Stem: services-and-notifications (Services and Notifications)

- `android-core/services-and-notifications/what-is-a-notification` : How does my app show a message when the user is not in it? [medium]
- `android-core/services-and-notifications/what-is-a-foreground-service` : How does a music app keep playing after I leave it? [hard]
- `android-core/services-and-notifications/choosing-background-tool` : Should this job be a coroutine, WorkManager or a foreground service? [medium]

### Stem: files-and-sharing (Files and Sharing)

- `android-core/files-and-sharing/app-private-files` : Where can my app save a file that only it can read? [easy]
- `android-core/files-and-sharing/picking-photos-and-files` : How can the user give my app one photo without opening the whole gallery? [medium]
- `android-core/files-and-sharing/sharing-with-other-apps` : How do I send a file to another app safely? [hard]

### Stem: app-startup (App Startup)

- `android-core/app-startup/what-is-the-application-class` : What runs before my first screen appears? [easy]
- `android-core/app-startup/cold-warm-hot-start` : Why does my app open slowly the first time and fast the next? [medium]
- `android-core/app-startup/from-tap-to-first-frame` : What happens, step by step, between tapping my icon and seeing my screen? [medium]

## Branch: app-networking


### Stem: requests-from-an-app (Requests from an App)

- `app-networking/requests-from-an-app/what-an-app-sends-and-gets` : What does my app actually send to a server, and what comes back? [easy]
- `app-networking/requests-from-an-app/why-my-first-call-fails` : Why does my first network call fail before it even leaves the phone? [easy]

### Stem: http-client (The HTTP Client)

- `app-networking/http-client/first-get-call` : How do I fetch data from an address with a few lines of Kotlin? [medium]
- `app-networking/http-client/building-requests` : How do I send a search word, an id in the address, or a form to the server? [easy]
- `app-networking/http-client/interceptors-and-logging` : How can I add a header or a log line to every call? [easy]

### Stem: json (JSON)

- `app-networking/json/json-to-kotlin-objects` : How does text from the server become a Kotlin object? [easy]
- `app-networking/json/when-json-does-not-match` : What happens when the server adds, drops or renames a field? [medium]

### Stem: errors-and-retries (Errors and Retries)

- `app-networking/errors-and-retries/kinds-of-failure` : How do I tell the different ways a network call can fail? [medium]
- `app-networking/errors-and-retries/timeouts` : How long will my call wait before it gives up? [medium]
- `app-networking/errors-and-retries/cancelling-a-call` : Does my call stop when the user leaves the screen? [hard]
- `app-networking/errors-and-retries/retry-with-backoff` : How do I retry a failed call without making things worse? [medium]

### Stem: auth-tokens (Auth Tokens)

- `app-networking/auth-tokens/how-apps-prove-who-they-are` : What is a token, and why does the app send it instead of the password? [medium]
- `app-networking/auth-tokens/refreshing-an-expired-token` : What should happen when the server says my token has expired? [hard]

### Stem: caching (Caching)

- `app-networking/caching/reusing-saved-answers` : When can the app reuse a saved answer instead of asking the server again? [medium]

### Stem: images (Images)

- `app-networking/images/images-from-urls` : How does a picture from the internet reach the screen without freezing the list? [medium]

### Stem: real-time (Real-Time Updates)

- `app-networking/real-time/live-updates-without-asking` : How can the server send my app news without my app asking every few seconds? [medium]

### Stem: sync (Sync)

- `app-networking/sync/keeping-phone-and-server-in-step` : What happens when the user edits offline and the server changed the same item? [hard]

## Branch: architecture-data


### Stem: why-architecture (Why Architecture)

- `architecture-data/why-architecture/why-code-needs-structure` : Why does an app with everything in one file become impossible to change? [easy]
- `architecture-data/why-architecture/three-jobs-in-an-app` : Which code shows things, which decides, and which fetches? [easy]

### Stem: viewmodel (ViewModel)

- `architecture-data/viewmodel/what-is-a-viewmodel` : What can a ViewModel hold that the screen cannot? [medium]
- `architecture-data/viewmodel/viewmodel-lifetime` : When is a ViewModel created and when does it die? [hard]
- `architecture-data/viewmodel/keep-the-screen-out-of-the-viewmodel` : Why must a ViewModel never hold a screen or a Context? [medium]
- `architecture-data/viewmodel/viewmodel-and-process-death` : How do I keep a small value when Android kills my app? [hard]

### Stem: ui-state-and-udf (UI State and One-Way Data Flow)

- `architecture-data/ui-state-and-udf/what-is-ui-state` : Why describe a whole screen with one state object? [medium]
- `architecture-data/ui-state-and-udf/loading-success-error` : How do I model loading, success and error so no case is forgotten? [medium]
- `architecture-data/ui-state-and-udf/state-down-events-up` : Why does state flow down to the screen while events flow up? [hard]
- `architecture-data/ui-state-and-udf/expose-state-not-mutable-state` : Why does a ViewModel keep its state private and show the screen a read-only copy? [easy]

### Stem: repositories (Repositories)

- `architecture-data/repositories/what-is-a-repository` : Who should the ViewModel ask for data? [easy]
- `architecture-data/repositories/repository-interface-and-fakes` : How do I swap real data for fake data without changing the screen? [medium]

### Stem: dependency-injection (Dependency Injection)

- `architecture-data/dependency-injection/what-is-a-dependency` : Why is a class that builds its own helpers hard to test and change? [easy]
- `architecture-data/dependency-injection/constructor-injection` : How do I hand a class the helpers it needs? [easy]
- `architecture-data/dependency-injection/what-a-di-library-does` : What does a DI library do that I would otherwise write by hand? [medium]
- `architecture-data/dependency-injection/singletons-and-scopes-in-di` : Why did two screens get the same object, or two different ones? [hard]

### Stem: key-value-storage (Key-Value Storage)

- `architecture-data/key-value-storage/store-a-setting-with-datastore` : Where do I save a small setting like the chosen language? [medium]
- `architecture-data/key-value-storage/reading-old-sharedpreferences-code` : How do I read old SharedPreferences code? [easy]

### Stem: databases (Databases)

- `architecture-data/databases/rows-tables-and-keys` : How does a database table let me find exactly one row? [easy]
- `architecture-data/databases/sql-basics` : How do I ask a database for the rows I want? [medium]
- `architecture-data/databases/room-entities-and-daos` : What are the three pieces of a Room database? [medium]
- `architecture-data/databases/room-and-flow` : How does the screen update by itself when a row in the database changes? [medium]
- `architecture-data/databases/relations-between-tables` : How do I store a list that belongs to one item, like orders for a customer? [hard]

### Stem: offline-first (Offline First)

- `architecture-data/offline-first/single-source-of-truth` : When data comes from both the network and the disk, which one does the screen trust? [medium]
- `architecture-data/offline-first/cache-then-refresh` : Why does the screen show old data first and then update? [medium]
- `architecture-data/offline-first/offline-writes` : What happens to a save the user makes while offline? [hard]

### Stem: paging (Paging)

- `architecture-data/paging/paging-big-lists` : How do I show a very long list without loading all of it? [hard]

### Stem: modules (Modules)

- `architecture-data/modules/what-is-a-module` : Why would I split one app into several modules? [medium]
- `architecture-data/modules/module-boundaries` : What may one module see of another? [medium]

### Stem: layers-and-mapping (Layers and Mapping)

- `architecture-data/layers-and-mapping/layers-and-their-jobs` : What job does each layer of an app do? [medium]
- `architecture-data/layers-and-mapping/mapping-between-models` : Why keep a database model, a network model and a screen model apart? [medium]

### Stem: putting-it-together (Putting It Together)

- `architecture-data/putting-it-together/move-state-into-a-viewmodel` : How do I move state from a screen into a ViewModel? [medium]
- `architecture-data/putting-it-together/review-generated-screen-code` : Would I accept this generated screen, ViewModel and repository in review? [hard]

## Branch: concurrency


### Stem: why-the-main-thread (Why the Main Thread Matters)

- `concurrency/why-the-main-thread/what-the-main-thread-does` : Which thread draws my screen and runs my click code? [easy]
- `concurrency/why-the-main-thread/why-blocking-freezes-the-screen` : Why does one slow job freeze the whole screen? [medium]

### Stem: threads (Threads)

- `concurrency/threads/running-work-on-another-thread` : How do I run a slow job beside the main thread? [easy]
- `concurrency/threads/why-raw-threads-get-messy` : What goes wrong when I use plain threads for everything? [medium]

### Stem: coroutine-basics (Coroutine Basics)

- `concurrency/coroutine-basics/what-is-a-coroutine` : How is a coroutine different from a thread? [medium]
- `concurrency/coroutine-basics/launch-a-coroutine` : How do I start a job that runs beside my code with launch? [easy]
- `concurrency/coroutine-basics/async-and-await` : How do I run two jobs at the same time and use both answers? [medium]

### Stem: suspend (Suspend Functions)

- `concurrency/suspend/what-suspend-means` : What happens to the thread when a suspend function has to wait? [hard]
- `concurrency/suspend/suspend-vs-blocking` : Why does delay keep the screen alive while Thread.sleep freezes it? [medium]

### Stem: dispatchers (Dispatchers)

- `concurrency/dispatchers/main-io-and-default` : Which dispatcher should this job run on? [medium]
- `concurrency/dispatchers/main-safe-functions` : How do I write a suspend function that is safe to call from the main thread? [medium]

### Stem: scopes (Scopes)

- `concurrency/scopes/what-is-a-coroutine-scope` : Who owns a coroutine, and what happens when the owner goes away? [medium]
- `concurrency/scopes/parent-and-child-coroutines` : Why does a parent coroutine wait for its children? [hard]
- `concurrency/scopes/scopes-in-android-code` : Which scope should a ViewModel, a screen and a button use? [medium]

### Stem: cancellation-and-errors (Cancellation and Errors)

- `concurrency/cancellation-and-errors/cancel-is-cooperative` : Why does cancel() not stop my loop? [hard]
- `concurrency/cancellation-and-errors/exceptions-in-coroutines` : Why does my try/catch around launch not catch the crash? [hard]
- `concurrency/cancellation-and-errors/supervisor-and-sibling-failure` : How can one failed job leave its siblings running? [medium]
- `concurrency/cancellation-and-errors/never-swallow-cancellation` : Why must I never hide a CancellationException? [medium]

### Stem: flow-basics (Flow Basics)

- `concurrency/flow-basics/what-is-a-flow` : How can a function hand back many values over time instead of one? [medium]
- `concurrency/flow-basics/flows-are-cold` : When does the code inside flow { } actually run? [medium]
- `concurrency/flow-basics/collect-and-cancel` : Why does the line after collect never run? [medium]

### Stem: flow-operators (Flow Operators)

- `concurrency/flow-operators/transform-a-flow` : How do I change the values of a flow on their way to the screen? [easy]
- `concurrency/flow-operators/flow-on-and-context` : On which thread does each step of a flow run? [medium]
- `concurrency/flow-operators/combine-flows` : How do I build one screen value from two flows? [medium]
- `concurrency/flow-operators/search-as-you-type` : How do I search while the user types without sending a request per letter? [hard]

### Stem: state-and-shared-flow (StateFlow and SharedFlow)

- `concurrency/state-and-shared-flow/state-flow` : How does a StateFlow always know the latest value? [medium]
- `concurrency/state-and-shared-flow/one-off-events` : How do I show a message once and not again after rotation? [hard]
- `concurrency/state-and-shared-flow/share-a-cold-flow` : How do I share one cold flow with many collectors? [medium]

### Stem: channels (Channels)

- `concurrency/channels/channels-between-coroutines` : How do two coroutines pass items to each other one at a time? [medium]

### Stem: shared-mutable-state (Shared Mutable State)

- `concurrency/shared-mutable-state/race-conditions-in-coroutines` : Why does a counter changed by many coroutines end up too small? [hard]

### Stem: putting-it-together (Putting It Together)

- `concurrency/putting-it-together/fix-a-frozen-screen` : How do I find why a screen freezes and choose the right fix? [medium]
- `concurrency/putting-it-together/review-generated-async-code` : Would I accept this generated async code in review? [hard]

## Branch: foundation


### Stem: how-computers-work (How Computers Work)

- `foundation/how-computers-work/what-does-a-computer-do` : What does a computer actually do all day? [easy]
- `foundation/how-computers-work/what-is-a-bit` : How can a computer store everything as 0s and 1s? [easy]
- `foundation/how-computers-work/how-letters-become-numbers` : How does a computer store a letter like A? [easy]
- `foundation/how-computers-work/what-is-a-cpu` : How does the CPU follow instructions one at a time? [easy]
- `foundation/how-computers-work/memory-vs-storage` : Why does a phone have both memory and storage? [easy]
- `foundation/how-computers-work/what-is-an-os` : Who decides which app gets the screen and the memory? [easy]

### Stem: first-code (First Code)

- `foundation/first-code/what-is-code` : Why does a computer need such exact instructions? [easy]
- `foundation/first-code/run-your-first-program` : Where can I run code without installing anything? [easy]
- `foundation/first-code/lines-run-in-order` : In what order does a computer run my lines? [easy]

### Stem: programming-thinking (Programming Thinking)

- `foundation/programming-thinking/what-is-a-variable` : How does a program remember a value? [easy]
- `foundation/programming-thinking/what-is-a-type` : Why can't I add a word to a number? [easy]
- `foundation/programming-thinking/expressions` : How does a program calculate a new value? [easy]
- `foundation/programming-thinking/yes-or-no-questions` : How does a program ask a yes-or-no question? [easy]
- `foundation/programming-thinking/how-a-program-decides` : How does a program choose what to do next? [easy]
- `foundation/programming-thinking/how-a-program-repeats` : How does a program keep doing something until a condition changes? [easy]
- `foundation/programming-thinking/counting-loops` : How do I repeat something exactly ten times? [medium]
- `foundation/programming-thinking/what-is-a-list` : How do I keep many values under one name? [easy]
- `foundation/programming-thinking/loop-over-a-list` : How do I do something to every item in a list? [medium]
- `foundation/programming-thinking/what-is-a-function` : How do I give a set of steps a name and reuse it? [easy]
- `foundation/programming-thinking/function-inputs-and-outputs` : How does a function take something in and hand something back? [medium]
- `foundation/programming-thinking/trace-code-by-hand` : How can I know what code prints before I run it? [medium]

### Stem: debugging-mindset (Debugging Mindset)

- `foundation/debugging-mindset/reading-error-messages` : What is an error message trying to tell me? [easy]
- `foundation/debugging-mindset/what-is-a-bug` : How can code run without errors and still be wrong? [easy]
- `foundation/debugging-mindset/find-a-bug-without-guessing` : How do I find a bug without guessing? [medium]
- `foundation/debugging-mindset/check-code-you-did-not-write` : How do I check code that someone else, or an AI, wrote for me? [medium]

### Stem: from-code-to-running (From Code to Running)

- `foundation/from-code-to-running/what-is-a-compiler` : How does my code become instructions the CPU understands? [easy]
- `foundation/from-code-to-running/compiler-vs-interpreter` : Why do some languages translate everything first and others line by line? [easy]
- `foundation/from-code-to-running/what-is-the-jvm` : How can the same Kotlin code run on many different computers? [medium]
- `foundation/from-code-to-running/what-is-art` : How does an Android phone run Kotlin code? [medium]

### Stem: how-an-app-is-born (How an App Is Born)

- `foundation/how-an-app-is-born/what-is-an-app-file` : What is inside an app file on a phone? [easy]
- `foundation/how-an-app-is-born/what-happens-when-i-build` : What happens when I press Build? [medium]
- `foundation/how-an-app-is-born/how-an-app-gets-installed` : How does an app get from a store onto my phone? [easy]
- `foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon` : What happens between tapping an icon and seeing the app? [medium]
- `foundation/how-an-app-is-born/run-your-first-app` : How do I run my own first app on a real phone? [easy]

## Branch: kmp


### Stem: what-is-kmp (What Is Kotlin Multiplatform)

- `kmp/what-is-kmp/what-kmp-shares` : Which parts of an app can I write once, and which must I still write for each phone? [easy]
- `kmp/what-is-kmp/how-kotlin-reaches-an-iphone` : How does the same Kotlin end up inside an app on an iPhone? [medium]

### Stem: source-sets (Source Sets)

- `kmp/source-sets/project-layout-and-targets` : Which folder holds code for which platform, and what does the build file declare? [medium]
- `kmp/source-sets/what-common-code-cannot-use` : Why can the shared folder not call Android or iPhone code directly? [medium]

### Stem: expect-actual (Expect and Actual)

- `kmp/expect-actual/expect-and-actual` : How does shared code ask for something each platform does its own way? [medium]
- `kmp/expect-actual/interface-instead-of-expect-actual` : When is an interface better than expect and actual? [medium]

### Stem: shared-logic (Shared Logic)

- `kmp/shared-logic/what-to-share-first` : Which pieces of my app should move into the shared module first? [medium]
- `kmp/shared-logic/shared-viewmodel-and-state` : Can one state holder drive both an Android screen and an iPhone screen? [hard]

### Stem: shared-networking (Shared Networking)

- `kmp/shared-networking/a-shared-api-client` : How do I call the same API from shared code on both phones? [medium]
- `kmp/shared-networking/ktor-plugins` : How do plugins add features such as JSON and the token header to a shared client? [easy]
- `kmp/shared-networking/platform-rules-still-apply` : Which Android and iPhone network rules still apply when the call is shared? [easy]

### Stem: shared-storage (Shared Storage)

- `kmp/shared-storage/shared-settings` : How do I save one small setting once for both phones? [easy]
- `kmp/shared-storage/shared-database` : How do I define one database for both phones when only the file location differs? [medium]
- `kmp/shared-storage/shared-repository` : How do the shared API client and the shared database work together behind one repository? [hard]

### Stem: compose-multiplatform (Compose Multiplatform)

- `kmp/compose-multiplatform/what-is-compose-multiplatform` : How is Compose Multiplatform related to the Jetpack Compose I already know? [easy]
- `kmp/compose-multiplatform/shared-ui-screen` : How does one shared screen get started by each app? [medium]
- `kmp/compose-multiplatform/shared-resources` : Where do images, texts and fonts live so one shared screen can use them on both phones? [easy]
- `kmp/compose-multiplatform/navigation-and-back-on-both` : How does one navigation setup handle the back button and the swipe-back gesture? [medium]

### Stem: swift-interop (Swift Interop)

- `kmp/swift-interop/calling-kotlin-from-swift` : What does shared Kotlin look like when Swift calls it? [hard]
- `kmp/swift-interop/mixing-native-and-shared-ui` : Can one app use a native iPhone screen for one part and a shared screen for another? [medium]

### Stem: libraries (Libraries)

- `kmp/libraries/choosing-kmp-libraries` : How can I tell that a library works on both phones before I add it? [easy]

### Stem: shipping-to-ios (Shipping to iOS)

- `kmp/shipping-to-ios/ship-a-shared-feature-to-both-platforms` : What takes one shared feature from my code to a build on an Android phone and an iPhone? [medium]

## Branch: kotlin


### Stem: first-programs (First Programs)

- `kotlin/first-programs/where-a-program-starts` : Where does a Kotlin program start running? [easy]
- `kotlin/first-programs/print-values-in-text` : How do I put a value inside a sentence I print? [easy]
- `kotlin/first-programs/shape-of-a-kotlin-file` : How do I read the shape of a Kotlin file? [easy]

### Stem: types-and-variables (Types and Variables)

- `kotlin/types-and-variables/val-vs-var` : When should a value be allowed to change? [easy]
- `kotlin/types-and-variables/basic-types` : Which kinds of values does Kotlin know out of the box? [easy]
- `kotlin/types-and-variables/type-inference` : How does Kotlin know a type I never wrote? [easy]
- `kotlin/types-and-variables/number-conversions` : Why won't Kotlin put an Int where a Long is expected? [medium]

### Stem: control-flow (Control Flow)

- `kotlin/control-flow/if-as-an-expression` : How can an if hand back a value? [easy]
- `kotlin/control-flow/when-expression` : How do I choose between many cases without a ladder of ifs? [easy]
- `kotlin/control-flow/ranges-and-for-loops` : Where does a Kotlin range start and stop? [easy]
- `kotlin/control-flow/break-and-continue` : How do I stop a loop early or skip one turn? [easy]

### Stem: functions (Functions)

- `kotlin/functions/declaring-functions` : How do I write a function in Kotlin? [easy]
- `kotlin/functions/default-and-named-arguments` : How can a function have optional inputs? [easy]
- `kotlin/functions/single-expression-functions` : Why do some Kotlin functions use = instead of braces? [easy]
- `kotlin/functions/unit-and-nothing` : What does a function return when it has nothing to give back? [easy]
- `kotlin/functions/scope-and-shadowing` : Why can't I use a variable outside the braces where I made it? [medium]

### Stem: null-safety (Null Safety)

- `kotlin/null-safety/what-is-null` : What is null and why does it crash apps? [easy]
- `kotlin/null-safety/nullable-types` : How does Kotlin mark a value that might be missing? [easy]
- `kotlin/null-safety/safe-call-and-elvis` : How do I use a maybe-missing value without crashing? [medium]
- `kotlin/null-safety/smart-casts` : How does Kotlin know a value is not null after I check it? [medium]
- `kotlin/null-safety/when-to-use-double-bang` : When is !! ever the right choice? [medium]

### Stem: strings (Strings)

- `kotlin/strings/strings-are-sequences` : How do I get one character out of a String? [easy]
- `kotlin/strings/everyday-string-functions` : Which String functions do I need every day? [easy]
- `kotlin/strings/text-to-numbers` : How do I turn the text "42" into a number without crashing? [medium]

### Stem: collections (Collections)

- `kotlin/collections/list-vs-mutable-list` : Why can't I add an item to a List? [easy]
- `kotlin/collections/index-out-of-bounds` : Why does asking for item 5 crash a list of five items? [easy]
- `kotlin/collections/sets` : How do I keep only unique values? [easy]
- `kotlin/collections/maps` : How do I look up a value by its key? [easy]
- `kotlin/collections/pairs-and-destructuring` : How do I unpack two values at once? [easy]
- `kotlin/collections/arrays-vs-lists` : When does Kotlin code use an Array instead of a List? [easy]

### Stem: lambdas (Lambdas)

- `kotlin/lambdas/functions-as-values` : How can a function be stored in a variable? [medium]
- `kotlin/lambdas/lambda-syntax` : How do I read a lambda, even when it sits outside the brackets? [medium]
- `kotlin/lambdas/higher-order-functions` : How do I write a function that takes another function? [medium]
- `kotlin/lambdas/map-filter-and-friends` : How do I transform a list without writing a loop? [medium]
- `kotlin/lambdas/closures` : How does a lambda remember variables from outside it? [medium]

### Stem: classes-and-objects (Classes and Objects)

- `kotlin/classes-and-objects/what-is-a-class` : How do I bundle data and the actions on it into one thing? [easy]
- `kotlin/classes-and-objects/constructors` : How does a new object get its starting values? [easy]
- `kotlin/classes-and-objects/member-functions` : How does an object do things with its own data? [easy]
- `kotlin/classes-and-objects/private-and-public` : Why hide some parts of a class with private? [easy]
- `kotlin/classes-and-objects/getters-and-setters` : How can a property run code when it is read or written? [medium]
- `kotlin/classes-and-objects/objects-are-references` : Why does changing one variable change another? [hard]
- `kotlin/classes-and-objects/two-kinds-of-equality` : When are two objects equal in Kotlin? [medium]
- `kotlin/classes-and-objects/read-only-is-not-immutable` : Can a read-only list still change under me? [medium]
- `kotlin/classes-and-objects/model-a-small-program` : How do I turn a real-world problem into classes and functions? [medium]

### Stem: interfaces-and-inheritance (Interfaces and Inheritance)

- `kotlin/interfaces-and-inheritance/what-is-an-interface` : How can different classes promise the same ability? [medium]
- `kotlin/interfaces-and-inheritance/open-and-override` : How does one class build on another class? [medium]
- `kotlin/interfaces-and-inheritance/abstract-classes` : When should I use an abstract class instead of an interface? [medium]
- `kotlin/interfaces-and-inheritance/polymorphism-and-type-checks` : How can one variable safely hold many kinds of objects? [hard]

### Stem: special-classes (Special Classes)

- `kotlin/special-classes/data-classes` : Why does Kotlin write equals and toString for some classes? [medium]
- `kotlin/special-classes/data-class-copy` : How do I change one field without changing the original object? [medium]
- `kotlin/special-classes/enum-classes` : How do I list every allowed choice, like the days of the week? [easy]
- `kotlin/special-classes/sealed-classes` : How do I model loading, success or error so I never forget a case? [hard]
- `kotlin/special-classes/object-declarations` : How do I make exactly one of something in the whole program? [medium]

### Stem: generics (Generics)

- `kotlin/generics/what-are-generics` : How can one List type hold Strings in one place and Ints in another? [medium]
- `kotlin/generics/writing-generic-code` : How do I write a function that works for any type? [medium]

### Stem: extensions-scope-and-delegates (Extensions, Scope Functions and Delegates)

- `kotlin/extensions-scope-and-delegates/extension-functions` : How can I add a function to a class I did not write? [medium]
- `kotlin/extensions-scope-and-delegates/scope-functions` : What do let, apply, also, run and with actually do? [hard]
- `kotlin/extensions-scope-and-delegates/lateinit` : How can a property be filled in after the object is made? [medium]
- `kotlin/extensions-scope-and-delegates/property-delegates` : What does by mean in val x by lazy? [medium]

### Stem: errors-and-results (Errors and Results)

- `kotlin/errors-and-results/exceptions` : What happens to my program when code throws an exception? [medium]
- `kotlin/errors-and-results/try-catch-finally` : How do I catch a failure and keep going? [medium]
- `kotlin/errors-and-results/result-and-runcatching` : How can a function hand back either a value or a failure without throwing? [hard]

### Stem: reading-code (Reading Code)

- `kotlin/reading-code/reading-order-for-code` : In what order should I read an unfamiliar Kotlin file? [medium]
- `kotlin/reading-code/spotting-risky-patterns` : Which lines in tidy-looking Kotlin deserve a second look? [hard]
- `kotlin/reading-code/review-a-generated-file` : Would I accept this generated file in review? [hard]

## Branch: native-ndk


### Stem: what-is-native (What Is Native Code)

- `native-ndk/what-is-native/what-is-native-code` : What is native code inside an Android app? [easy]

### Stem: art-and-dex (ART and Dex)

- `native-ndk/art-and-dex/what-is-dex` : What is inside classes.dex? [easy]
- `native-ndk/art-and-dex/how-art-runs-code` : How does ART turn my app's code into fast machine code over time? [medium]

### Stem: jni-basics (JNI Basics)

- `native-ndk/jni-basics/kotlin-calls-cpp` : How does a Kotlin function call a function written in C++? [medium]
- `native-ndk/jni-basics/jni-types-and-strings` : Why can't I pass a Kotlin String to C++ like a normal argument? [medium]
- `native-ndk/jni-basics/jni-references` : Why does C++ code that holds on to a Kotlin object leak or crash later? [hard]

### Stem: ndk-build (Building Native Code)

- `native-ndk/ndk-build/add-cpp-to-a-project` : How do I add a C++ file to my app and get Gradle to build it? [medium]
- `native-ndk/ndk-build/abis-and-so-files` : Why does my app contain a separate .so file for each kind of CPU? [easy]

### Stem: native-memory (Native Memory)

- `native-ndk/native-memory/native-memory-has-no-gc` : Who frees memory that C++ code allocates? [medium]
- `native-ndk/native-memory/memory-page-sizes` : Why must my native libraries support 16 KB memory pages? [hard]

### Stem: when-to-go-native (When to Go Native)

- `native-ndk/when-to-go-native/should-i-go-native` : Should this feature be Kotlin, a ready-made library, or my own C++? [medium]

## Branch: networking


### Stem: how-the-internet-works (How the Internet Works)

- `networking/how-the-internet-works/clients-and-servers` : When my app loads data, which computer asks and which one answers? [easy]
- `networking/how-the-internet-works/how-data-travels-in-packets` : How does a message cross the internet as many small packets? [easy]

### Stem: layers (Layers)

- `networking/layers/why-networks-use-layers` : Why does a message pass through several layers before it leaves my phone? [medium]

### Stem: ip-and-dns (Addresses and Names)

- `networking/ip-and-dns/ip-addresses` : What does an IP address tell the network about my phone? [easy]
- `networking/ip-and-dns/how-packets-find-a-route` : How does a packet find its way across many routers? [medium]
- `networking/ip-and-dns/dns` : How does a name like example.com become an address? [medium]

### Stem: tcp-and-udp (TCP and UDP)

- `networking/tcp-and-udp/ports-and-sockets` : How does data reach the right app on my phone? [medium]
- `networking/tcp-and-udp/tcp-reliable-delivery` : How does TCP make an unreliable network feel reliable? [hard]
- `networking/tcp-and-udp/udp-speed-over-reliability` : Why do video calls skip the safety checks that downloads use? [easy]

### Stem: http (HTTP)

- `networking/http/request-and-response` : What do a request and a response look like on the wire? [easy]
- `networking/http/http-methods` : Which requests are safe to send twice? [easy]
- `networking/http/status-codes` : What does the number at the top of a response tell my app? [easy]
- `networking/http/headers-and-bodies` : Where does information go in a request, in the headers or in the body? [easy]
- `networking/http/stateless-requests` : Why must every request prove who I am again? [medium]

### Stem: tls (HTTPS and TLS)

- `networking/tls/why-https` : What can someone on the same Wi-Fi read when an app uses plain HTTP? [medium]
- `networking/tls/certificates-and-trust` : How does my phone know it is talking to the real server? [hard]

### Stem: mobile-networks-and-latency (Mobile Networks and Latency)

- `networking/mobile-networks-and-latency/latency-and-round-trips` : Why are ten small requests slower than one big one? [medium]
- `networking/mobile-networks-and-latency/flaky-mobile-networks` : What happens to a request when the signal drops or the network changes? [medium]

### Stem: api-styles (API Styles)

- `networking/api-styles/rest-style-apis` : What does a URL like /users/42/posts ask for? [easy]

### Stem: websockets (WebSockets)

- `networking/websockets/websockets-and-live-updates` : How can a server send my phone a message without being asked? [medium]

### Stem: putting-it-together (Putting It Together)

- `networking/putting-it-together/diagnose-a-failed-request` : Which step of the journey failed when a request does not work? [hard]

## Branch: profiling


### Stem: measure-first (Measure First)

- `profiling/measure-first/measure-before-you-fix` : Why must I measure before I try to make my app faster? [easy]

### Stem: profiler-tour (Profiler Tour)

- `profiling/profiler-tour/read-a-profiler-screen` : What do the timeline and the charts on a profiler show me? [medium]

### Stem: cpu (CPU)

- `profiling/cpu/find-the-hot-function` : How do I find the function that uses the most CPU time? [medium]
- `profiling/cpu/catch-main-thread-work` : How do I find work that runs on the main thread and should not? [medium]

### Stem: memory-and-leaks (Memory and Leaks)

- `profiling/memory-and-leaks/read-memory-use` : How do I see what is using my app's memory? [medium]
- `profiling/memory-and-leaks/find-a-leak` : How do I find what is keeping an old screen in memory? [hard]

### Stem: jank-and-frames (Jank and Frames)

- `profiling/jank-and-frames/frame-budget` : How long does one frame get before the screen stutters? [easy]
- `profiling/jank-and-frames/find-the-janky-frame` : How do I find a slow frame and see what it was doing? [hard]

### Stem: compose-performance (Compose Performance)

- `profiling/compose-performance/see-needless-recomposition` : How do I see which composables redraw more often than they should? [medium]
- `profiling/compose-performance/fix-slow-compose-code` : Which small change stops a screen from redrawing on every frame? [medium]

### Stem: startup (Startup)

- `profiling/startup/measure-startup` : How do I measure how long my app takes to start? [medium]
- `profiling/startup/speed-up-startup` : What work can I remove or delay so the first screen shows sooner? [medium]
- `profiling/startup/baseline-profiles` : What does a baseline profile do for a user's first launch? [medium]

### Stem: battery-and-network (Battery and Network)

- `profiling/battery-and-network/find-battery-and-network-drain` : How do I find what is draining the battery or using data? [medium]

### Stem: app-size (App Size)

- `profiling/app-size/shrink-the-app` : What makes my app download smaller? [easy]

### Stem: putting-it-together (Putting It Together)

- `profiling/putting-it-together/find-the-slowest-part` : How do I find the slowest part of a slow app and prove my fix helped? [hard]

## Branch: security


### Stem: threat-thinking (Threat Thinking)

- `security/threat-thinking/who-would-attack-my-app` : How do I think like someone who wants to attack my app? [easy]

### Stem: secure-storage (Secure Storage)

- `security/secure-storage/where-data-lives-on-a-phone` : Which of my app's saved data can another app or person read? [easy]
- `security/secure-storage/keystore-and-encryption` : How can an app lock data with a key that the app itself cannot copy? [hard]

### Stem: network-security (Network Security)

- `security/network-security/https-and-cleartext` : What stops someone on the same Wi-Fi from reading my app's traffic? [medium]
- `security/network-security/network-security-config-and-pinning` : When should my app trust only one certificate, and what breaks when it changes? [hard]

### Stem: auth (Sign-In and Tokens)

- `security/auth/why-tokens-not-passwords` : Why should the app keep a short-lived token and never the password? [medium]
- `security/auth/sign-in-with-credential-manager` : How can users sign in with a passkey or saved account instead of typing a password? [medium]

### Stem: privacy-and-permissions (Privacy and Permissions)

- `security/privacy-and-permissions/ask-for-less-data` : What is the least data my app needs? [easy]
- `security/privacy-and-permissions/tell-users-what-you-collect` : What must I tell people about the data my app collects? [easy]

### Stem: obfuscation (Obfuscation)

- `security/obfuscation/what-obfuscation-really-hides` : If I turn on code shrinking, is the API key in my app hidden? [medium]

### Stem: webviews (WebViews)

- `security/webviews/webview-risks` : What can go wrong when my app shows a web page inside itself? [medium]

### Stem: app-integrity (App Integrity)

- `security/app-integrity/can-the-server-trust-the-app` : How can my server tell a genuine copy of my app from a fake one? [hard]

### Stem: secrets-and-logs (Secrets and Logs)

- `security/secrets-and-logs/secrets-in-source-code` : Why is an API key in my code or my git history already public? [medium]
- `security/secrets-and-logs/sensitive-data-in-logs` : Why must a token or a password never reach Logcat or a crash report? [easy]

### Stem: putting-it-together (Putting It Together)

- `security/putting-it-together/spot-three-leaks` : Can I read a short snippet and name three places where it leaks data? [medium]

## Branch: testing


### Stem: why-test (Why Test)

- `testing/why-test/why-tests-exist` : What does a test give me that tapping through the app does not? [easy]

### Stem: unit-tests (Unit Tests)

- `testing/unit-tests/run-your-first-unit-test` : How do I write and run one test on my own computer? [easy]
- `testing/unit-tests/arrange-act-assert` : What are the three parts of a test that anyone can read? [easy]

### Stem: assertions (Assertions)

- `testing/assertions/assert-the-right-thing` : Which check should I use to compare a result with what I expect? [easy]
- `testing/assertions/watch-a-test-fail` : Why must I see a new test fail before I trust it? [easy]

### Stem: test-doubles (Test Doubles)

- `testing/test-doubles/what-is-a-test-double` : Why do tests use a stand-in instead of the real thing? [easy]
- `testing/test-doubles/fake-stub-or-mock` : Should this stand-in be a fake, a stub or a mock? [medium]
- `testing/test-doubles/tests-that-always-pass` : Why is my test green even though the code is broken? [medium]

### Stem: testing-coroutines (Testing Coroutines)

- `testing/testing-coroutines/run-test-and-virtual-time` : How do I test code that waits five seconds without waiting five seconds? [hard]
- `testing/testing-coroutines/inject-the-dispatcher` : Why must a class receive its dispatcher instead of choosing one? [medium]
- `testing/testing-coroutines/test-a-flow` : How do I check the values a flow sends, in order? [medium]

### Stem: testing-viewmodels (Testing ViewModels)

- `testing/testing-viewmodels/test-viewmodel-state` : How do I check that a ViewModel goes from loading to success? [medium]
- `testing/testing-viewmodels/test-the-error-path` : How do I test what the screen shows when the server fails? [medium]

### Stem: device-tests (Device Tests)

- `testing/device-tests/local-vs-device-tests` : Which tests can run on my computer and which need a phone? [easy]

### Stem: ui-tests (UI Tests)

- `testing/ui-tests/test-a-composable` : How does a test tap a button and check the text on a screen? [medium]
- `testing/ui-tests/find-nodes-with-semantics` : How does a UI test find the right element when text repeats or changes? [medium]
- `testing/ui-tests/test-a-screen-with-fake-state` : How do I test a whole screen without a real ViewModel or network? [medium]

### Stem: screenshot-tests (Screenshot Tests)

- `testing/screenshot-tests/catch-a-visual-change` : How can a test notice that my button moved a few pixels? [medium]

### Stem: flaky-tests (Flaky Tests)

- `testing/flaky-tests/why-a-test-flakes` : Why does the same test pass, then fail, with no code change? [hard]
- `testing/flaky-tests/make-a-test-reliable` : How do I make a flaky test steady? [medium]

### Stem: putting-it-together (Putting It Together)

- `testing/putting-it-together/review-generated-tests` : Would I accept this generated test file in review? [hard]
- `testing/putting-it-together/prove-a-bug-is-fixed` : How do I prove a bug is fixed with a test that fails first and then passes? [medium]

## Branch: tools


### Stem: git (Git)

- `tools/git/what-is-a-commit` : How do I save a snapshot of my project that I can return to later? [medium]
- `tools/git/branches` : How can I try an idea without breaking my working code? [medium]
- `tools/git/merge-conflicts` : What do I do when git says two changes collide? [hard]
- `tools/git/remotes-and-pull-requests` : How does my code get to GitHub and into a teammate's project? [hard]

### Stem: android-studio (Android Studio and IntelliJ)

- `tools/android-studio/find-your-way-around-studio` : Where are my code, my build output and my run button in Android Studio? [easy]
- `tools/android-studio/navigate-and-refactor` : How do I jump to code, find usages and rename safely? [easy]
- `tools/android-studio/debug-with-breakpoints` : How do I pause my app on a line and look at its variables? [medium]

### Stem: gradle-basics (Gradle Basics)

- `tools/gradle-basics/what-gradle-does` : What does Gradle do when I press Sync? [medium]
- `tools/gradle-basics/dependencies-and-configurations` : How does a dependency line decide who can use a library? [medium]
- `tools/gradle-basics/version-catalogs` : Where do library versions live so that every module uses the same one? [easy]

### Stem: adb-and-logcat (ADB and Logcat)

- `tools/adb-and-logcat/adb-basics` : How do I talk to my phone from my computer? [easy]
- `tools/adb-and-logcat/reading-logcat` : How do I find my crash among thousands of log lines? [medium]

### Stem: lint-and-static-analysis (Lint and Static Analysis)

- `tools/lint-and-static-analysis/what-lint-warns-about` : How do I tell a real lint warning from noise? [easy]

### Stem: ci (Continuous Integration)

- `tools/ci/what-is-ci` : What runs automatically when I push my code? [easy]
- `tools/ci/a-first-ci-workflow` : How do I make GitHub build my app and run tests on every pull request? [medium]

### Stem: publishing (Publishing)

- `tools/publishing/release-builds-and-signing` : What makes a build one that I can ship? [hard]
- `tools/publishing/from-commit-to-store` : Can I name every step from a commit to an update on a user's phone? [hard]

## Branch: ui


### Stem: first-composable (First Composable)

- `ui/first-composable/what-is-declarative-ui` : Why does Compose describe the screen instead of changing it step by step? [medium]
- `ui/first-composable/what-is-a-composable` : What makes a Kotlin function a composable? [easy]
- `ui/first-composable/text-and-images` : How do I put words and pictures on the screen? [easy]
- `ui/first-composable/previews` : How can I see my screen without running the app? [easy]

### Stem: modifiers (Modifiers)

- `ui/modifiers/what-is-a-modifier` : How do I give a composable a size, padding or a background? [easy]
- `ui/modifiers/modifier-order` : Why does the order of modifiers change what I see? [medium]
- `ui/modifiers/reacting-to-taps` : How does a composable react when I tap it? [easy]

### Stem: layouts (Layouts)

- `ui/layouts/column-row-box` : How do I place things above, beside or on top of each other? [easy]
- `ui/layouts/alignment-and-arrangement` : How do I push items to the centre, to the edges or spread them out? [medium]
- `ui/layouts/weight-and-space` : How do I make one item take all the leftover space? [easy]
- `ui/layouts/scaffold-and-slots` : How do the top bar, the floating button and the content fit together? [medium]

### Stem: state-and-recomposition (State and Recomposition)

- `ui/state-and-recomposition/what-is-recomposition` : Why does my screen not change when my variable changes? [medium]
- `ui/state-and-recomposition/remember` : Why does my counter go back to zero every time the screen redraws? [medium]
- `ui/state-and-recomposition/remember-saveable` : Why does my typed text vanish on rotation even with remember? [medium]
- `ui/state-and-recomposition/state-with-lists` : Why does adding an item to my list not update the screen? [medium]
- `ui/state-and-recomposition/state-hoisting` : Who should own a piece of state, the button or its parent? [medium]
- `ui/state-and-recomposition/remember-with-keys` : Why does my screen show old data after its input changed? [hard]

### Stem: lists (Lists)

- `ui/lists/lazy-column` : How do I show a list of a thousand items without freezing the phone? [medium]
- `ui/lists/list-keys` : Why do the wrong rows move when I delete an item? [hard]
- `ui/lists/lazy-grid` : How do I lay items out in a grid like a photo gallery? [easy]

### Stem: theming (Theming)

- `ui/theming/material-theme` : Where do my app's colours and fonts come from? [easy]
- `ui/theming/using-theme-tokens` : Why write MaterialTheme.colorScheme.primary instead of Color.Blue? [easy]
- `ui/theming/matching-system-look` : How does my app follow the phone's own look settings? [medium]

### Stem: navigation (Navigation)

- `ui/navigation/what-is-navigation` : How does a Compose app move from one screen to the next? [medium]
- `ui/navigation/passing-arguments` : How does the next screen know which item I tapped? [medium]
- `ui/navigation/back-navigation` : What should happen when the user goes back? [medium]

### Stem: side-effects (Side Effects)

- `ui/side-effects/why-side-effects` : Why must I not start a network call straight from a composable? [medium]
- `ui/side-effects/launched-effect` : How do I run a coroutine when a screen appears? [hard]
- `ui/side-effects/coroutine-from-a-click` : How do I start a coroutine from a button tap? [medium]
- `ui/side-effects/disposable-effect` : How do I clean up when a composable leaves the screen? [medium]
- `ui/side-effects/collecting-flows` : How does a screen show values that keep arriving over time? [hard]

### Stem: forms-and-input (Forms and Input)

- `ui/forms-and-input/text-field` : Why can I not type in my text field? [medium]
- `ui/forms-and-input/keyboard-options` : How do I show the number keyboard and a Next button? [easy]
- `ui/forms-and-input/validating-input` : How do I tell the user what is wrong with what they typed? [medium]
- `ui/forms-and-input/choice-controls` : How do I let the user switch something on or pick one option? [easy]

### Stem: animation (Animation)

- `ui/animation/animate-as-state` : How do I make a size or colour change glide instead of jump? [easy]
- `ui/animation/animated-visibility` : How do I make something appear and disappear smoothly? [easy]
- `ui/animation/gesture-driven-animation` : How does a card follow my finger and spring back? [hard]

### Stem: accessibility (Accessibility)

- `ui/accessibility/what-a-screen-reader-hears` : What does a blind user hear when they open my screen? [medium]
- `ui/accessibility/big-enough-to-tap` : Why is my small icon button hard to tap? [easy]
- `ui/accessibility/font-scaling` : What happens to my layout when the user makes text huge? [medium]

### Stem: adaptive-layouts (Adaptive Layouts)

- `ui/adaptive-layouts/drawing-behind-system-bars` : Why does my app draw under the status bar? [hard]
- `ui/adaptive-layouts/window-size-classes` : How does one app look right on a phone, a foldable and a tablet? [medium]

### Stem: views-and-interop (Views and Interop)

- `ui/views-and-interop/what-are-views` : How were Android screens built before Compose? [easy]
- `ui/views-and-interop/reading-xml-layouts` : How do I read an XML layout someone else wrote? [medium]
- `ui/views-and-interop/compose-inside-views` : How do I add a Compose screen to an app built with Views? [medium]
- `ui/views-and-interop/views-inside-compose` : How do I use an old View, like a map, inside Compose? [medium]

### Stem: custom-drawing (Custom Drawing)

- `ui/custom-drawing/canvas-basics` : How do I draw my own shapes, like a bar chart? [medium]
- `ui/custom-drawing/build-a-custom-control` : How do I build my own control, like a star rating bar, from scratch? [hard]
