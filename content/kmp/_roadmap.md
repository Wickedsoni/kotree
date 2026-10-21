```yaml
branch: kmp
bough: architecture-kmp
stems:
  - id: what-is-kmp
    title: "What Is Kotlin Multiplatform"
    why: "A fresher must know what Kotlin Multiplatform shares, what it does not, and how Kotlin ends up inside an iPhone app before touching a project."
    leaves:
      - id: what-kmp-shares
        title: "Which parts of an app can I write once, and which must I still write for each phone?"
        difficulty: easy
        requires: []
        fruit: false
      - id: how-kotlin-reaches-an-iphone
        title: "How does the same Kotlin end up inside an app on an iPhone?"
        difficulty: medium
        requires: [kmp/what-is-kmp/what-kmp-shares]
        fruit: false
  - id: source-sets
    title: "Source Sets"
    why: "Every shared project is a set of folders that decide which code sees which platform, and generated code often breaks that line."
    leaves:
      - id: project-layout-and-targets
        title: "Which folder holds code for which platform, and what does the build file declare?"
        difficulty: medium
        requires: [kmp/what-is-kmp/how-kotlin-reaches-an-iphone]
        fruit: false
      - id: what-common-code-cannot-use
        title: "Why can the shared folder not call Android or iPhone code directly?"
        difficulty: medium
        requires: [kmp/source-sets/project-layout-and-targets]
        fruit: false
  - id: expect-actual
    title: "Expect and Actual"
    why: "When shared code needs something each phone does its own way, a fresher must know the safest way to ask for it."
    leaves:
      - id: expect-and-actual
        title: "How does shared code ask for something each platform does its own way?"
        difficulty: medium
        requires: [kmp/source-sets/what-common-code-cannot-use]
        fruit: false
      - id: interface-instead-of-expect-actual
        title: "When is an interface better than expect and actual?"
        difficulty: medium
        requires: [kmp/expect-actual/expect-and-actual]
        fruit: false
  - id: shared-logic
    title: "Shared Logic"
    why: "The biggest saving comes from sharing rules and state, and the biggest waste comes from sharing the wrong things first."
    leaves:
      - id: what-to-share-first
        title: "Which pieces of my app should move into the shared module first?"
        difficulty: medium
        requires: [kmp/source-sets/what-common-code-cannot-use, kmp/expect-actual/interface-instead-of-expect-actual]
        fruit: false
      - id: shared-viewmodel-and-state
        title: "Can one state holder drive both an Android screen and an iPhone screen?"
        difficulty: hard
        requires: [kmp/shared-logic/what-to-share-first]
        fruit: false
  - id: shared-networking
    title: "Shared Networking"
    why: "One API client in shared code removes the most duplicated work, but each phone still has its own network rules."
    leaves:
      - id: a-shared-api-client
        title: "How do I call the same API from shared code on both phones?"
        difficulty: medium
        requires: [kmp/shared-logic/what-to-share-first, kmp/expect-actual/interface-instead-of-expect-actual]
        fruit: false
      - id: ktor-plugins
        title: "How do plugins add features such as JSON and the token header to a shared client?"
        difficulty: easy
        requires: [kmp/shared-networking/a-shared-api-client]
        fruit: false
      - id: platform-rules-still-apply
        title: "Which Android and iPhone network rules still apply when the call is shared?"
        difficulty: easy
        requires: [kmp/shared-networking/a-shared-api-client]
        fruit: false
  - id: shared-storage
    title: "Shared Storage"
    why: "Saved data is where file locations differ most, so a fresher must see exactly which small part stays per platform."
    leaves:
      - id: shared-settings
        title: "How do I save one small setting once for both phones?"
        difficulty: easy
        requires: [kmp/expect-actual/interface-instead-of-expect-actual]
        fruit: false
      - id: shared-database
        title: "How do I define one database for both phones when only the file location differs?"
        difficulty: medium
        requires: [kmp/expect-actual/interface-instead-of-expect-actual]
        fruit: false
      - id: shared-repository
        title: "How do the shared API client and the shared database work together behind one repository?"
        difficulty: hard
        requires: [kmp/shared-networking/a-shared-api-client, kmp/shared-storage/shared-database]
        fruit: false
  - id: compose-multiplatform
    title: "Compose Multiplatform"
    why: "Sharing the screens too is the last big saving, and a fresher must know what is shared, what is not and what is still changing."
    leaves:
      - id: what-is-compose-multiplatform
        title: "How is Compose Multiplatform related to the Jetpack Compose I already know?"
        difficulty: easy
        requires: [kmp/source-sets/project-layout-and-targets]
        fruit: false
      - id: shared-ui-screen
        title: "How does one shared screen get started by each app?"
        difficulty: medium
        requires: [kmp/compose-multiplatform/what-is-compose-multiplatform, kmp/shared-logic/shared-viewmodel-and-state]
        fruit: false
      - id: shared-resources
        title: "Where do images, texts and fonts live so one shared screen can use them on both phones?"
        difficulty: easy
        requires: [kmp/compose-multiplatform/shared-ui-screen]
        fruit: false
      - id: navigation-and-back-on-both
        title: "How does one navigation setup handle the back button and the swipe-back gesture?"
        difficulty: medium
        requires: [kmp/compose-multiplatform/shared-ui-screen]
        fruit: false
  - id: swift-interop
    title: "Swift Interop"
    why: "The iPhone side of the app is written in Swift, so a fresher must read how shared Kotlin looks from there and when to keep a screen native."
    leaves:
      - id: calling-kotlin-from-swift
        title: "What does shared Kotlin look like when Swift calls it?"
        difficulty: hard
        requires: [kmp/what-is-kmp/how-kotlin-reaches-an-iphone, kmp/shared-logic/shared-viewmodel-and-state]
        fruit: false
      - id: mixing-native-and-shared-ui
        title: "Can one app use a native iPhone screen for one part and a shared screen for another?"
        difficulty: medium
        requires: [kmp/compose-multiplatform/shared-ui-screen, kmp/swift-interop/calling-kotlin-from-swift]
        fruit: false
  - id: libraries
    title: "Libraries"
    why: "Most shared apps depend on libraries, and one JVM-only library in the shared folder breaks the iPhone build."
    leaves:
      - id: choosing-kmp-libraries
        title: "How can I tell that a library works on both phones before I add it?"
        difficulty: easy
        requires: [kmp/shared-storage/shared-repository]
        fruit: false
  - id: shipping-to-ios
    title: "Shipping to iOS"
    why: "A shared feature only counts when it runs on both phones, and the iPhone side has its own steps to a real build."
    leaves:
      - id: ship-a-shared-feature-to-both-platforms
        title: "What takes one shared feature from my code to a build on an Android phone and an iPhone?"
        difficulty: medium
        requires: [kmp/swift-interop/mixing-native-and-shared-ui, kmp/libraries/choosing-kmp-libraries]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Every stem is phase 6. Every `Assumes` target below sits in phase 5 or lower, or in a branch earlier in the `docs/08` §10.1 tie-break; Security comes after this branch, so it is `Helps to know` only.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

Mac needed: a leaf tagged `Mac needed` has an iOS-side part (Xcode, the Swift project or an iPhone build) that only runs on macOS. The author works on Windows with no Mac and free tiers only (L27). Checked 18 Oct 2026 (`docs/08` §8.1): Kotlin/Native cannot build iOS binaries on Windows (M3), the iOS targets are skipped on a Windows host (M4: stated in the Gradle DSL reference, to be confirmed by experiment E4), and the free route is a public GitHub repository whose free `macos-latest` runner builds the iOS side (O1, O2). Each `Mac needed` leaf is taught as concept plus shared code on Windows first, then the iOS result shown from a linked CI run, with the label from `docs/08` §8.1 printed beside each iOS-side claim ("Ran on my Windows machine", "Seen in this CI run", "From the docs, not run by me"). Proposal for the author to confirm (not yet a rule; lesson structure belongs to `docs/04`, publishing gates to L5 and `reviewer`): a `Mac needed` leaf goes live only when the author links a green CI run for it or notes a borrowed-Mac session. The Kotlin Playground model tasks still work anywhere. The desktop target (Stable, runs on Windows) may be used as the author's own run of a shared screen; it is not a leaf (see Later).

Mac needed leaves, what Windows covers and what the CI run supplies (the experiments E1 to E4 are in `docs/08` §8.1):

- `how-kotlin-reaches-an-iphone`: Windows covers the build file, the target list, the belt sim and the Android output; CI supplies the log lines of the framework link step (E1, E2). Label: Seen in this CI run.
- `shared-viewmodel-and-state`: Windows covers the state holder in `commonMain`, the plain-Kotlin model and the Android screen; CI compiles the Swift owner class with `xcodebuild`. Label: Seen in this CI run (build only, not run).
- `platform-rules-still-apply`: Windows covers the Android permission and the two-column checklist; the Apple App Transport Security rule is read from Apple's docs. Label: From the docs, not run by me.
- `shared-settings`: Windows covers the `commonMain` code and the Android file path; CI compiles the iOS path code. Label: Seen in this CI run (build only).
- `shared-database`: Windows covers the `commonMain` setup and the Android builder; CI compiles the iOS builder. Label: Seen in this CI run (build only).
- `shared-ui-screen`: Windows covers the shared screen run on Android and on desktop; CI compiles the `iosMain` entry and the Swift wrapper, and gives a screenshot only if E3 works. Label: Seen in this CI run (build only unless E3 passes).
- `navigation-and-back-on-both`: Windows covers the stack logic, Android back and the Esc key on desktop; the iPhone swipe is docs only. Label: From the docs, not run by me.
- `calling-kotlin-from-swift`: Windows covers the Kotlin shapes (enum, sealed class, suspend function); CI supplies the generated header file as an artifact (E2). Label: Seen in this CI run.
- `mixing-native-and-shared-ui`: Windows covers the Android equivalent (`compose-inside-views`); CI compiles the Swift and UIKit wrappers. Label: Seen in this CI run (build only).
- `ship-a-shared-feature-to-both-platforms`: Windows covers Gradle, the Android release build and the shared module; CI builds the iPhone app for the Xcode test device; signing and TestFlight need a paid Apple account (99 USD a year, outside L27). Label: Seen in this CI run for the build; From the docs, not run by me for signing and release.

Where-tag key (every Notes cell starts with a `Where:` tag that says which part is shared and which is per platform):

- Where: commonMain — shared code that compiles for every target.
- Where: androidMain/iosMain — one small piece per platform, written twice.
- Where: Gradle — build-file setup, not app code.
- Where: Swift side — the Xcode project or Swift code in the iPhone app.
- Where: concept — an idea with no code location.
- A cell can combine tags with a plus sign.

Prerequisites:

- Assumes Foundation: `foundation/from-code-to-running/what-is-a-compiler`, `foundation/from-code-to-running/what-is-the-jvm` and `foundation/from-code-to-running/what-is-art` (phase 1) — used by how-kotlin-reaches-an-iphone.
- Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file` (phase 1) — used by what-kmp-shares.
- Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface` (phase 3) — used by interface-instead-of-expect-actual.
- Assumes Kotlin: `kotlin/special-classes/sealed-classes` (phase 3) — screen state and Swift mapping.
- Assumes Kotlin: `kotlin/generics/what-are-generics` (phase 3) — generics lose detail on the Swift side.
- Assumes Android core and lifecycles: `android-core/activities/what-is-an-activity` (phase 3) — the Android entry point of the shared screen.
- Assumes Android core and lifecycles: `android-core/configuration-changes/surviving-a-config-change` (phase 3) — used by shared-viewmodel-and-state.
- Assumes Android core and lifecycles: `android-core/manifest-and-resources/what-are-resources` (phase 5) — used by shared-resources.
- Assumes UI: `ui/first-composable/what-is-a-composable` and `ui/state-and-recomposition/state-hoisting` (phase 3) — the shared screens are Compose screens.
- Assumes UI: `ui/navigation/what-is-navigation` and `ui/navigation/back-navigation` (phase 3) — used by navigation-and-back-on-both.
- Assumes Networking inside apps: `app-networking/http-client/first-get-call`, `app-networking/http-client/interceptors-and-logging`, `app-networking/json/json-to-kotlin-objects`, `app-networking/auth-tokens/how-apps-prove-who-they-are` and `app-networking/errors-and-retries/timeouts` and `app-networking/errors-and-retries/cancelling-a-call` (phase 4) — shared-networking re-uses the ideas and teaches only what changes in shared code.
- Assumes Networking inside apps: `app-networking/requests-from-an-app/why-my-first-call-fails` (phase 4) — used by platform-rules-still-apply.
- Assumes Networking inside apps: `app-networking/errors-and-retries/kinds-of-failure` and `app-networking/sync/keeping-phone-and-server-in-step` (phase 4) — used by shared-repository.
- Assumes Networking inside apps: `app-networking/images/images-from-urls` (phase 4) — used by choosing-kmp-libraries (image loading as a library example).
- Assumes Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel`, `architecture-data/ui-state-and-udf/what-is-ui-state` and `architecture-data/layers-and-mapping/layers-and-their-jobs` (phase 4) — used by shared-logic.
- Assumes Architecture and data: `architecture-data/repositories/what-is-a-repository`, `architecture-data/offline-first/single-source-of-truth`, `architecture-data/key-value-storage/store-a-setting-with-datastore` and `architecture-data/databases/room-entities-and-daos` (phase 4) — used by shared-storage.
- Assumes Architecture and data: `architecture-data/modules/what-is-a-module` and `architecture-data/modules/module-boundaries` (phase 4) — used by what-to-share-first and project-layout-and-targets.
- Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine`, `concurrency/dispatchers/main-io-and-default` and `concurrency/state-and-shared-flow/state-flow` (phase 4) — shared code is coroutine code.
- Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow` (phase 4) — used by calling-kotlin-from-swift.
- Assumes Tools and ecosystem: `tools/gradle-basics/what-gradle-does` (phase 5) — used by project-layout-and-targets.
- Helps to know Tools and ecosystem: `tools/ci/what-is-ci` and `tools/publishing/release-builds-and-signing` (phase 6) — ship-a-shared-feature-to-both-platforms names them once.
- Helps to know Testing: `testing/test-doubles/what-is-a-test-double` — shared code is tested with a plain-Kotlin fake, as in interface-instead-of-expect-actual.
- Helps to know Security: `security/secure-storage/where-data-lives-on-a-phone` and `security/app-integrity/can-the-server-trust-the-app` — named once; the lessons live there.

Verification status (all checked 18 Oct 2026 unless stated; ⚠️ VERIFY again before each leaf goes live):

- Expect/actual (settled, re-read 18 Oct 2026, kotlinlang.org multiplatform-expect-actual, footer 13 May 2026): only expected and actual classes are Beta; functions, properties, objects and type aliases are not labelled Beta; the page advises standard language constructs (such as interfaces) wherever possible; the classes warning is silenced with the `-Xexpect-actual-classes` compiler option. Re-check at publish: look for the word Beta.
- Hierarchy: kotlinlang.org describes a default hierarchy template that creates shared intermediate source sets such as `iosMain` from the declared targets.
- Ktor engines: ktor.io names the OkHttp engine for Android and the Darwin engine for iOS (it uses NSURLSession).
- Room for KMP (settled, developer.android.com/kotlin/multiplatform/room, 2026-09-09): "the only part of the API you must define in platform-specific source sets" is the database builder; `BundledSQLiteDriver` is recommended; the page shows Android, iOS and JVM desktop. Its sample names `androidx.room3` artifacts with an alpha-labelled version, so the names that looked unfamiliar earlier are the page's real names. No artifact or version is pinned here; copy them from the page, with its date, when the leaf is written.
- Jetpack libraries on KMP (partly settled): ViewModel supports KMP from 2.8.0 (developer.android.com/kotlin/multiplatform/viewmodel, 2026-09-23); Preferences DataStore only, from 1.1.0, on Android, iOS, web and JVM desktop (.../datastore, 2026-10-12); the setup index lists ViewModel, Room, SQLite, DataStore and Navigation Event (.../setup, 2026-03-06). Lifecycle is read (see the `shared-viewmodel-and-state` row); the Paging KMP page was not read. ⚠️ VERIFY: before writing any line on Paging, open its page under developer.android.com and copy the supported targets with the date.
- Compose Multiplatform status (settled with a caveat, `docs/08` §8.1 M5, M6): kotlinlang.org supported-platforms page (footer 10 Sep 2025, over a year old) lists Android, iOS and desktop (JVM) as Stable and web on Kotlin/Wasm as Beta; the compatibility page (footer 15 Oct 2026) names version 1.12.2 and gives no labels. A newer official page (kotlinlang.org kotlin-multiplatform-react-native, footer 21 Jul 2026) repeats "stable for iOS, Android, and desktop, with web in Beta". The May 2025 JetBrains blog title is a secondary source. Re-read the table at publish and print its date in the lesson.
- Swift side (settled, 18 Oct 2026): the Objective-C interop page (footer 12 Aug 2026) still calls Swift async for suspend functions "highly experimental", and says enums import as classes with a property per entry; Swift export (kotlinlang.org native-swift-export, footer 28 Aug 2026) is Alpha ("still incomplete, so breaking changes are expected"). The Objective-C page does not cover sealed classes: read them from a generated header (`docs/08` §8.1 E2).
- iOS builds need macOS and Xcode (settled, `docs/08` §8.1 M1 to M3): kotlinlang.org FAQ (footer 01 Oct 2026), quickstart (footer 02 Oct 2026) and Kotlin/Native target support (footer 19 Aug 2026: "Building final binaries for Apple targets on Linux and Windows is also not possible"). Free route for the author: a public repository and the free `macos-latest` runner (`docs/08` §8.1 O1, O2).
- App Transport Security (settled, Apple documentation for NSAppTransportSecurity via developer.apple.com/tutorials/data/documentation/bundleresources/information-property-list/nsapptransportsecurity.json, no revision date, copyright 2026): ATS requires HTTP connections made through the URL Loading System to use HTTPS and blocks those that do not meet its checks; `NSAllowsArbitraryLoads` defaults to NO. The Ktor Darwin engine uses NSURLSession (ktor.io client-engines, footer 28 Aug 2026).

Challenge note: the Kotlin Playground cannot run Gradle source sets, Ktor or Compose. Challenges use a plain-Kotlin model (an interface and two classes standing for Android and iPhone), a folder tree or build file shown read-only, or a "which line is wrong" task. ⚠️ VERIFY with `docs/06` once written. Android-only behaviour is shown with `code-to-screen` sims (`docs/05` §4.3); there is no emulator or simulator (L15). Gradle and source-set ideas have no Android screen, so they use the `animation` kind or `none:`.

## Stems

### what-is-kmp · What Is Kotlin Multiplatform

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-kmp-shares` | easy | "Which of these six features can live in shared code, and which must stay on each phone?" | A restaurant chain with one recipe book but a different oven in each kitchen: recipes are shared, the oven knobs are not. | "Kotlin Multiplatform lets me write the whole app once, and it looks and works the same everywhere." | `sim:` features (price rules, login rules, camera, notifications, screen layout, saved data) roll down a belt and the learner drops each into a shared bin or a per-platform bin; the bin lights up with the reason. | `P:` which of six features can live in shared code · `B:` drop the camera into the shared bin and read why it bounces | Where: concept. Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file`. Checked 18 Oct 2026 (kotlinlang.org supported-platforms, footer 10 Sep 2025, over a year old): Android, iOS, desktop (JVM) and server (JVM) Stable; web Kotlin/JS Stable, Kotlin/Wasm Beta. ⚠️ VERIFY at publish: reopen the table and copy each status with its date. |
| `how-kotlin-reaches-an-iphone` | medium | "Why does shared Kotlin need no Java virtual machine inside an iPhone app?" | One script translated by two different translators and printed as two books, each in its reader's own language. | "Kotlin on iPhone is interpreted, or ships a Java virtual machine inside the app." | `sim:` animation: one block of source reaches a fork; one belt turns it into code for the Android runtime, the other into a native binary packed as a framework that the iPhone app links; the learner removes the iOS target and the second belt goes dark. | `P:` which output file each target produces (table) · `F:` a build file that lists no iOS target, so the iPhone project finds nothing to link (read-only) · `B:` remove the iOS target | Where: Gradle. Mac needed. Assumes Foundation: `foundation/from-code-to-running/what-is-a-compiler`, `foundation/from-code-to-running/what-is-the-jvm`, `foundation/from-code-to-running/what-is-art`. Checked 18 Oct 2026: Kotlin/Native builds final binaries for iOS targets on a macOS host only (kotlinlang.org native-target-support, footer 19 Aug 2026), and the IDE runs Xcode under the hood to build iOS frameworks (quickstart, footer 02 Oct 2026). iOS evidence: CI log (`docs/08` §8.1 E1, E2). How Xcode picks up the framework (kotlinlang.org multiplatform-direct-integration, footer 01 Oct 2026): a Run Script build phase calls the `embedAndSignAppleFrameworkForXcode` Gradle task of the shared module; the task exists only if the iOS target declares `binaries.framework`; the phase sits before Compile Sources. The Kotlin/Native memory manager is not checked: say nothing about it. |

### source-sets · Source Sets

Flags: phase=6 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `project-layout-and-targets` | medium | "A file sits in iosMain: which targets compile it?" | A floor plan with a shared corridor in the middle and one wing for each side: a corridor room is open to everyone, a wing room only to its own side. | "commonMain is a copy that gets pasted into each platform folder." | `none:` a folder tree and a build file side by side; labelled and static, because no screen or motion is involved. | `P:` which targets compile a file in iosMain · `F(ai):` a generated build file that declares only a phone target, so an iPhone or the Xcode test device cannot run the app · `B:` add a target and list the new folders | Where: Gradle. Assumes Tools and ecosystem: `tools/gradle-basics/what-gradle-does`. Assumes Architecture and data: `architecture-data/modules/what-is-a-module`. Checked 18 Oct 2026 (kotlinlang.org): the default hierarchy template builds shared intermediate source sets such as iosMain from the declared targets. Checked 18 Oct 2026: Google's plugin is `com.android.kotlin.multiplatform.library`, and the old `androidTarget` block is renamed `android` (kotlinlang.org compatibility guide, footer 08 Oct 2026; developer.android.com/kotlin/multiplatform, 2026-10-12). Pin no plugin version. ⚠️ VERIFY: the project wizard's option wording (kmp.jetbrains.com showed no option names to the fetch tool); test: open it in a browser and copy the labels. |
| `what-common-code-cannot-use` | medium | "Why does an import of Android's Log fail in commonMain, and what can commonMain use?" | A shared kitchen holds only tools every cook already owns; it cannot plug in a stove, because the stove brand differs per branch. | "If it compiles on Android, it compiles in the shared folder." | `sim:` animation: imports drop as balls through three layers (commonMain, iosMain, androidMain); each layer lets through only what it can see; the learner moves a file between layers and watches the build light. | `F(ai):` a generated shared file that imports Android's Log · `F(ai):` a shared file that uses a JVM-only time or id function · `P:` which of five imports compile in commonMain · `B:` move the file to androidMain and say what iOS loses | Where: commonMain. Reading AI code: platform API in common code is the typical defect (`docs/08` §5). ⚠️ VERIFY: which standard-library and time APIs are available in commonMain on current Kotlin (not read). Test: put each of the five imports into a `commonMain` file of a Windows project and run `gradlew build`; the failures are the answers to the `P:` task, not a list from memory. |

### expect-actual · Expect and Actual

Flags: phase=6 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `expect-and-actual` | medium | "What does the compiler check when shared code says expect and a platform says actual?" | A job advert at head office saying "we need a driver": every branch hires its own, and the head office stays blocked until each branch has. | "The actual version is picked while the app runs, like an if on the platform." | `sim:` reuse `what-common-code-cannot-use` sim, with a socket in commonMain and a plug in each platform layer; pull a plug out and the build light turns red. | `P:` what a platform-name function prints on each target (plain-Kotlin model) · `F(ai):` an expect function with no actual for iOS (read-only) · `F(ai):` an actual whose parameter type differs from its expect · `B:` add a target and find the missing actual | Where: commonMain + androidMain/iosMain. Checked 18 Oct 2026 (kotlinlang.org multiplatform-expect-actual, footer 13 May 2026): only expected and actual classes are Beta; functions and properties are not labelled Beta; classes need `-Xexpect-actual-classes` to silence the warning. ⚠️ VERIFY at publish: reopen the page and look for the word Beta. |
| `interface-instead-of-expect-actual` | medium | "When should I pass an interface into shared code instead of declaring an expect class?" | A standard USB port: any device that fits works, and a test can plug in a toy device. | "Expect and actual is the main way shared code reaches platform features." | `none:` a design choice; two short listings side by side, each with a test fake, are clearer than motion. | `F(ai):` a confident expect class where an interface would do, which the docs discourage · `F(ai):` an expect class used with the compiler warning left unexplained · `P:` which design lets a test swap in a fake · `B:` rewrite the expect class as an interface | Where: commonMain + androidMain/iosMain. Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface`. Checked 18 Oct 2026 (same page, footer 13 May 2026): "rely on standard language constructs wherever possible"; an interface in `commonMain` lets a test swap in a fake; expect and actual is mainly needed in dependency-injection setup. ⚠️ VERIFY at publish: if classes are no longer Beta, change only the Beta line. Helps to know Architecture and data: `architecture-data/dependency-injection/what-is-a-dependency`. Helps to know Testing: `testing/test-doubles/what-is-a-test-double`. |

### shared-logic · Shared Logic

Flags: phase=6

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-to-share-first` | medium | "Which three pieces of my app go into the shared module first, and why those?" | Packing for a trip for two: share the map and the tent, but each person carries their own boots. | "The more code I share, the better, so share the screens and the camera first." | `none:` a ranking decision; a table of real features with a risk and a saving column is the material. | `P:` order six features from safest to riskiest to share · `F(ai):` a generated shared module that wraps every platform call in an expect class · `B:` move one feature out and count what still changes per platform | Where: concept. Assumes Architecture and data: `architecture-data/layers-and-mapping/layers-and-their-jobs` and `architecture-data/modules/module-boundaries`. |
| `shared-viewmodel-and-state` | hard | "Can one state holder drive both an Android screen and an iPhone screen, and what must it never hold?" | One scoreboard seen from two stands: the numbers are the same, the seats are different. | "A ViewModel is an Android-only class, so iPhone needs its own copy of the logic." | `sim:` code-to-screen one state holder drives two drawn phones (an Android frame and an iPhone frame); changing the state in code updates both, rotating the Android frame redraws only that frame. | `P:` what both screens show after two events · `F(ai):` a shared state holder that keeps an Android Context · `F(ai):` state exposed as a mutable flow so the iPhone side can change it · `B:` add a state field and see both screens | Where: commonMain + Swift side. Mac needed. Assumes Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel` and `architecture-data/ui-state-and-udf/what-is-ui-state`. Assumes Concurrency: `concurrency/state-and-shared-flow/state-flow`. Assumes Android core and lifecycles: `android-core/configuration-changes/surviving-a-config-change`. Checked 18 Oct 2026 (developer.android.com/kotlin/multiplatform/viewmodel, 2026-09-23): Jetpack ViewModel supports KMP from 2.8.0 (no version in the lesson). On the iPhone side the dependency is exported to the framework and a Swift class that owns a `ViewModelStore` asks for the ViewModel through a helper written in `iosMain`; `viewModelScope` uses the main dispatcher, which desktop lacks unless a Swing coroutines artifact is added. Lifecycle: "supports the following platforms in total: JVM (Android and Desktop), Native (Linux, iOS, watchOS, macOS, MinGW), and Web (JavaScript, WasmJS)" (developer.android.com/jetpack/androidx/releases/lifecycle, version 2.9.3 notes dated 27 Aug 2025; later notes not read). ⚠️ VERIFY at publish: reopen the release notes for newer entries. |

### shared-networking · Shared Networking

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `a-shared-api-client` | medium | "How can one API client in shared code work on both phones, using a different engine on each?" | A shared dialling plan with a different handset on each desk: the rules are the same, the hardware is not. | "The Android client I already wrote can simply move into the shared folder." | `none:` a two-box diagram (one shared client, one engine per target); the ideas are those of the Networking-inside-apps branch. | `P:` which engine each target uses (table) · `F(ai):` a generated shared client that imports an Android-only engine · `B:` remove the iOS engine dependency and read the build error | Where: commonMain + Gradle. Assumes Networking inside apps: `app-networking/http-client/first-get-call`. Checked 18 Oct 2026 (ktor.io/docs/client-engines.html, footer 28 Aug 2026): the OkHttp engine is for JVM and Android (Android 5.0+); the Darwin engine targets Apple systems and uses NSURLSession. How an engine is chosen (default-engines artifact, per-source-set dependency, or expect and actual) was read earlier on 18 Oct and not re-fetched. ⚠️ VERIFY at publish: re-read the choice rules, and whether the Retrofit and OkHttp client taught in app-networking runs outside the JVM (not checked); test: read each library's own target list and say so in the lesson. |
| `ktor-plugins` | easy | "Which plugin adds the token, JSON handling or a timeout to every call?" | A shared toolbox with labelled drawers, the same in every branch. | "Plugins must be set up again for each platform." | `none:` a mapping from earlier ideas to plugin names; a table is clearer than motion. | `P:` which plugin adds the token to every call · `F(ai):` a shared client that logs the authorisation header · `B:` remove the timeout plugin and say what the user would see on a slow network | Where: commonMain. Assumes Networking inside apps: `app-networking/json/json-to-kotlin-objects`, `app-networking/http-client/interceptors-and-logging`, `app-networking/auth-tokens/how-apps-prove-who-they-are`, `app-networking/errors-and-retries/timeouts`. Checked 18 Oct 2026 (ktor.io/docs/client-plugins.html, 19 Dec 2025): `ContentNegotiation` and `Logging` are named there. Each plugin's own page gives its install line: `install(Auth) { bearer { ... } }` with artifact `ktor-client-auth` (ktor.io/docs/client-bearer-auth.html, 08 May 2026); `install(HttpTimeout)` (ktor.io/docs/client-timeout.html, 20 Oct 2025); `install(DefaultRequest)` or `defaultRequest { header(...) }` (ktor.io/docs/client-default-request.html, 20 May 2026). Re-read the three pages at publish; no version in the lesson. |
| `platform-rules-still-apply` | easy | "Which setting do I still need in the Android project and in the iPhone project?" | The same delivery truck crossing two borders, each with its own paperwork. | "If the network code is shared, the platform settings are shared too." | `none:` a two-column checklist is the whole lesson. | `P:` which setting an iPhone project needs for a plain-HTTP address · `F:` a shared client that works on Android but fails on iPhone for an http address (read-only) · `B:` remove the internet permission on the Android side | Where: Gradle + androidMain/iosMain + Swift side. Mac needed. Assumes Networking inside apps: `app-networking/requests-from-an-app/why-my-first-call-fails`. Checked 18 Oct 2026 (Apple documentation for NSAppTransportSecurity, no revision date, copyright 2026): App Transport Security requires HTTP connections made through the URL Loading System to use HTTPS and blocks the rest; `NSAllowsArbitraryLoads` defaults to NO. The Info.plist edit itself is shown read-only (label: From the docs, not run by me, `docs/08` §8.1). |

### shared-storage · Shared Storage

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `shared-settings` | easy | "Which part of saving one setting is shared, and which part is the file path?" | A sticky note on the family fridge: the note is the same, but the fridge stands in a different room in each home. | "Saving a file works the same on both phones, so the path is the same." | `none:` setup and a pattern; side-by-side files are clearer. | `P:` which source set holds the file-path code · `F(ai):` shared code that reads Android's files folder · `B:` add the iOS path and see the app save there | Where: commonMain + androidMain/iosMain. Mac needed (the iOS file path). Assumes Architecture and data: `architecture-data/key-value-storage/store-a-setting-with-datastore`. Checked 18 Oct 2026 (developer.android.com/kotlin/multiplatform/datastore, 2026-10-12): only Preferences DataStore is supported on KMP, from DataStore 1.1.0; creating it needs a storage object from platform code (the files folder on Android, a document-directory path on iPhone); the page covers Android, iOS, web and JVM desktop. Multiplatform settings libraries are named only, not compared. |
| `shared-database` | medium | "What is the one platform-specific part of a shared database?" | One filing cabinet design built in every office, with the cabinet put in a different room in each. | "A shared database library means there is no platform code left." | `none:` a pattern with one platform piece; the file tree with the single platform file highlighted is clearer. | `P:` which part of the database setup is platform-specific · `F(ai):` a shared query method that is not suspend and returns an Android-only observable type · `B:` remove the iOS builder and read the error | Where: commonMain + androidMain/iosMain + Gradle. Mac needed (the iOS builder). Assumes Architecture and data: `architecture-data/databases/room-entities-and-daos`. Checked 18 Oct 2026 (developer.android.com/kotlin/multiplatform/room, 2026-09-09): only the database builder is platform-specific (file path); `BundledSQLiteDriver` is the recommended driver. The page's sample names `androidx.room3` artifacts at an alpha-labelled version: pin no artifact, version or method; at publish copy them from the page with its date and say if they are still alpha. Another SQL library is named only. |
| `shared-repository` | hard | "In what order does one shared repository check the database, call the API and report failure?" | A library desk: look on the shelf first, order from the warehouse if the book is missing, and tell the reader plainly if the warehouse is shut. | "The repository must be written twice because storage differs per platform." | `none:` a layered flow; stepping through a plain-Kotlin fake of the repository is clearer than motion. | `P:` which source set holds each class in the stack (table) · `F(ai):` a refresh that clears the table before the network call succeeds · `F(ai):` a repository that returns the database library's types to the screen · `B:` turn the fake network off | Where: commonMain. Assumes Architecture and data: `architecture-data/repositories/what-is-a-repository` and `architecture-data/offline-first/single-source-of-truth`. Assumes Networking inside apps: `app-networking/errors-and-retries/kinds-of-failure`, `app-networking/sync/keeping-phone-and-server-in-step`. Room's KMP page lists Android, iOS and JVM desktop only (2026-09-09). ⚠️ VERIFY at publish for any other target: read each shared database library's own target list and name only libraries that list every target the repository declares. |

### compose-multiplatform · Compose Multiplatform

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-compose-multiplatform` | easy | "Is Compose Multiplatform the same as Jetpack Compose, and does it draw native iPhone buttons?" | A touring stage play: the script is the same, but each theatre has its own lights and its own stage. | "Compose Multiplatform uses the real native iPhone buttons." | `none:` a layer diagram (your composables, the Compose runtime, each platform's drawing surface); labelled and static. | `P:` which layer differs per platform · `F(ai):` a shared screen that imports an Android-only Compose library · `B:` name what you would check before using a widget in shared UI | Where: concept. Assumes UI: `ui/first-composable/what-is-a-composable`. Checked 18 Oct 2026: iOS, Android and desktop (JVM) Stable, web on Kotlin/Wasm Beta, per kotlinlang.org supported-platforms (footer 10 Sep 2025, over a year old; `docs/08` §8.1 M6). Same Compose compiler and runtime and the same APIs as Jetpack Compose (kotlinlang.org compose-multiplatform-and-jetpack-compose, footer 27 Nov 2025). Rendering: the official comparison page (kotlinlang.org kotlin-multiplatform-react-native, footer 21 Jul 2026) says Compose Multiplatform uses the Skia engine (with Metal among its back ends) and that a UI "can be fully native or shared via Compose Multiplatform"; it does not say outright that no native buttons are used. Lesson wording: "draws its own UI with Skia; native iPhone views come in only through `UIKitView`" (`UIKitView` from kotlinlang.org compose-uikit-integration, footer 19 Mar 2026). ⚠️ VERIFY at publish: copy the current status with its date, and look for a sentence on native widgets to quote. No version numbers in the lesson. |
| `shared-ui-screen` | medium | "Which function starts the same shared screen inside each of the two apps?" | One stage set shown through two theatres' stage doors. | "Shared UI means there is no Android or iPhone code left at all." | `none:` the result is best seen in a reference video of one screen on two phones; the entry-point listing is the lesson. | `P:` which function starts the shared screen on each platform (read-only listing) · `F(ai):` a shared screen that reads Android's LocalContext · `B:` change one shared text and count how many files change | Where: commonMain + androidMain/iosMain + Swift side. Mac needed. Assumes UI: `ui/state-and-recomposition/state-hoisting`, `ui/layouts/scaffold-and-slots`. Assumes Android core and lifecycles: `android-core/activities/what-is-an-activity`. Checked 18 Oct 2026 (kotlinlang.org compose-swiftui-integration, footer 10 Jun 2026): a Kotlin function returns a `UIViewController` built with `ComposeUIViewController { ... }`; Swift wraps it in a `UIViewControllerRepresentable`; the Swift-side name (such as `Main_iosKt.MainViewController`) is generated from the Kotlin file name, so it is not fixed. The page also says the app crashes at run time without the `CADisableMinimumFrameDurationOnPhone` key in Info.plist. The same screen runs on the desktop target on Windows. iOS evidence: CI build; a screenshot only if E3 works (`docs/08` §8.1). |
| `shared-resources` | easy | "Where do I put one image, one text and one font so both phones show them?" | A shared wardrobe with one door for each house. | "Android's R class and res folder work in shared code." | `none:` a setup rule; a folder diagram of the shared resource folder is enough. | `P:` which path holds the shared text · `F(ai):` a shared screen that uses an R string · `B:` add a second language text and say which file changes | Where: commonMain + Gradle. Assumes Android core and lifecycles: `android-core/manifest-and-resources/what-are-resources`. Checked 18 Oct 2026 (kotlinlang.org compose-multiplatform-resources-setup, footer 02 Sep 2026, and .../resources-usage, footer 10 Oct 2026): files go under a `composeResources` folder in the source set, with `drawable`, `font`, `values` (strings) and `files` folders; a build generates the `Res` class (internal by default); `stringResource`, `painterResource` and `Font` read from it; `painterResource` is synchronous on all targets except web. ⚠️ VERIFY at publish: the page changes between releases; reopen both and copy the names with the date. |
| `navigation-and-back-on-both` | medium | "What happens to the stack when Android's back button or the iPhone swipe is used?" | One road map used under two countries' traffic customs. | "Back works the same on both phones, so nothing is platform-specific." | `none:` a rule table of gestures against stack changes is clearer than motion. | `P:` the stack after three screens and one back · `F(ai):` a shared navigation setup with an Android-only back handler · `B:` open the same screen twice | Where: commonMain + androidMain/iosMain. Mac needed (the iPhone swipe). Assumes UI: `ui/navigation/what-is-navigation`, `ui/navigation/back-navigation`. Helps to know Android core and lifecycles: `android-core/activities/back-stack`. Checked 18 Oct 2026 (kotlinlang.org compose-navigation, footer 21 Jul 2026): the multiplatform Navigation library (a port of AndroidX Navigation) turns the back gesture on each platform into "previous screen": the swipe on iPhone, the Esc key on desktop. On iPhone the handler is on by default and can be switched off with `enableBackGesture` in the `ComposeUIViewController` configuration; custom enter or exit animations switch off the default swipe animation. The page showed a release-candidate version; give no version. ⚠️ VERIFY at publish: Navigation 3 has its own page that was not read; decide which library the lesson teaches. |

### swift-interop · Swift Interop

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `calling-kotlin-from-swift` | hard | "What does a Kotlin sealed class, enum or suspend function look like to a Swift developer?" | A translated book where names change and a few jokes do not survive. | "Swift sees my Kotlin code exactly as I wrote it." | `none:` a mapping table of Kotlin shape against Swift shape is the material. | `P:` how a Kotlin enum and a sealed class appear in Swift (table) · `F(ai):` a shared suspend function whose errors Swift cannot see · `B:` add a case to a sealed class and say what the Swift switch needs | Where: Swift side + commonMain. Mac needed. Assumes Kotlin: `kotlin/special-classes/sealed-classes`, `kotlin/generics/what-are-generics`. Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow`. Checked 18 Oct 2026 (kotlinlang.org Objective-C interop, footer 12 Aug 2026): enums become classes with a property per entry; a suspend function without `@Throws` propagates only cancellation; Swift async for suspend functions is still called "highly experimental". Swift export (native-swift-export, footer 28 Aug 2026) is Alpha, "still incomplete, so breaking changes are expected"; it maps enums and sealed hierarchies to Swift enums, suspend functions to `async`, flows to `AsyncSequence`, and erases generics to their upper bounds. Teach the default export first and name Swift export as Alpha. ⚠️ VERIFY: how a sealed class looks under the default export (that page does not cover it); test: read it in the generated header from the CI run (`docs/08` §8.1 E2). Helper libraries are named only. |
| `mixing-native-and-shared-ui` | medium | "Can one part of the app be a native iPhone screen while the rest is shared?" | A building where some rooms are furnished by one shop and some by another, joined by normal doors. | "I must pick all-shared or all-native UI for the whole app." | `none:` an ownership picture; a reference video of the two kinds on one phone is clearer. | `P:` which side owns each screen in a given diagram · `F(ai):` a shared screen that draws its own iPhone-style title bar and duplicates the native one · `B:` replace one shared screen with a native one | Where: Swift side + commonMain. Mac needed. Helps to know UI: `ui/views-and-interop/compose-inside-views` (the same idea on Android). Checked 18 Oct 2026 (kotlinlang.org compose-swiftui-integration, footer 10 Jun 2026, and compose-uikit-integration, footer 19 Mar 2026): `UIKitView` and `UIKitViewController` place native iPhone views inside Compose; a `UIHostingController` wraps a SwiftUI view for that; a Kotlin entry function returning the Compose view controller lets UIKit or SwiftUI place it. ⚠️ VERIFY at publish: reopen both pages, since these names move between releases. |

### libraries · Libraries

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `choosing-kmp-libraries` | easy | "How do I check, in one minute, that a library works in the shared folder?" | Checking the plug type before buying an appliance abroad. | "A library that works on Android also works in the shared folder." | `none:` a checklist (listed targets, last release, shared-folder example); nothing moves. | `P:` from a library's listed targets, can commonMain use it · `F(ai):` a generated build file that adds a JVM-only library to commonMain · `B:` replace it with a multiplatform alternative | Where: Gradle. Assumes Networking inside apps: `app-networking/images/images-from-urls`. Helps to know Architecture and data: `architecture-data/dependency-injection/what-is-a-dependency`. klibs.io is titled "Kotlin Multiplatform Libraries (KMP)" and links a JetBrains issue tracker (18 Oct 2026, no date; the page did not say whether it filters by target). ⚠️ VERIFY: whether klibs.io has a target filter, and which image-loading and injection libraries support more than Android; test: open klibs.io in a browser, then read each library's own target list, as with the Ktor engines page (ktor.io/docs/client-engines.html, footer 28 Aug 2026). |

### shipping-to-ios · Shipping to iOS

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `ship-a-shared-feature-to-both-platforms` | medium | "Which steps take one shared feature from my code to a build on an Android phone and an iPhone?" | Opening two shop branches from one warehouse: the stock is shared, the licences and signs are not. | "If it runs on the Xcode test device, it is ready for the App Store." | `none:` a step checklist; a reference video of the Xcode steps is clearer than motion. | `P:` order six release steps · `F(ai):` a build script that builds the iPhone app on a Windows runner (read-only) · `B:` remove signing and read the error | Where: Gradle + Swift side. Mac needed. Synthesis leaf; fruit. Assumes Tools and ecosystem: `tools/gradle-basics/what-gradle-does`. Helps to know Tools and ecosystem: `tools/ci/what-is-ci` and `tools/publishing/release-builds-and-signing`. Helps to know Security: `security/app-integrity/can-the-server-trust-the-app`. Checked 18 Oct 2026: iOS builds need macOS and Xcode (kotlinlang.org FAQ, footer 01 Oct 2026; quickstart, footer 02 Oct 2026). The free route to a build is the official CI workflow: `macos-latest`, `xcodebuild`, built for the Xcode test device so no signing is needed (kotlinlang.org github-actions-for-kmp, footer 18 Mar 2026; `docs/08` §8.1 O2). The Apple Developer Program costs 99 USD per year (developer.apple.com/programs/whats-included, no date), so signing, TestFlight and App Store steps are outside L27 and are taught as "From the docs, not run by me". ⚠️ VERIFY: the exact account and signing steps; test: read Apple's distribution pages before writing any step; until then none is written. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `kmp/shipping-to-ios/ship-a-shared-feature-to-both-platforms` | "One feature, two phones" | The shared module holds the models, the API client, the storage and the state holder · no Android or iPhone import inside commonMain · every expect and actual (or interface) is justified in one line · the iPhone screen is either shared or native, with the reason written down · a recorded run on an Android phone, and for iOS either a run on an iPhone or the Xcode test device or, for a learner with no Mac, a linked run of a free macOS CI build with its log kept (`docs/08` §8.1) · a short note lists what stayed per platform and why | The `docs/01` §6.2 KMP ability "which code can be shared and which must stay per platform" (self-test: sort 10 pieces of code), and the L24 promise to build and ship for Android and iOS (`docs/01` §1.2). Checked against `docs/01` on 18 Oct 2026. Only fruit: every stem is phase 6, so F3 has no earlier phase-segment end to hold a second one. |

## Later

- Kotlin/Wasm and web targets: the branch teaches Android and iPhone first.
- Desktop targets: not needed for a phone app team.
- Publishing a KMP library: a library-author topic.
- Kotlin/Native memory internals: only needed when chasing rare crashes.
- Calling C libraries from Kotlin/Native: close to the native branch and rarely needed in app code.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 10 stems, 22 leaves, all `shadow`. | +22 | Estimate 22, band 18–26: inside the band. Followed the `docs/08` §9 sketch (2, 2, 2, 2, 3, 3, 4, 2, 1, 1). Reordered the last two stems (libraries before shipping-to-ios) so the last leaf is a synthesis leaf that carries the fruit (F2). Shared-networking and shared-storage cover only what changes in shared code and point to the Networking-inside-apps and Architecture and data branches for the rest. |
| 2026-10-18 | A2e review fixes: reworded `a-shared-api-client`, `ktor-plugins`, `shared-ui-screen` and `calling-kotlin-from-swift` to one idea each (slugs unchanged); replaced the Xcode phone-tool wording with "iPhone or the Xcode test device" in three places (a real tool, not a site claim, but `docs/08` §6 keeps that word out of the roadmaps); `Assumes` notes that pointed at the old app-networking leaf `timeouts-and-cancellation` now point at the two leaves it was split into; one `⚠️ VERIFY` added to `what-common-code-cannot-use`. | 0 | No leaf added or removed. |
| 2026-10-18 | "KMP without a Mac" pass: researched what a Windows author can and cannot do (`docs/08` §8.1); rewrote the Mac needed paragraph and added a per-leaf list of what Windows covers and what a CI run supplies; settled the KMP and iOS `⚠️ VERIFY` marks with source and date, or kept them with a concrete test; fruit constraint now allows a linked CI build when no Mac is available. No leaf, id, order, difficulty, `requires` or YAML change. | 0 | Author works on Windows with free tiers only (L27); count stays 22. |

