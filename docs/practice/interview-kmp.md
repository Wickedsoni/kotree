# interview: kmp

## kmp/what-is-kmp/what-kmp-shares
interview-questions:
- [intern] What is Kotlin Multiplatform (KMP), and which parts of an application logic can be shared between Android and iOS?
- [junior] What components must still be written natively for each platform in a Kotlin Multiplatform project?
- [mid] What is the architectural trade-off between sharing 100% of business logic vs maintaining platform-specific abstractions?
- [senior] What goes wrong in production when shared KMP modules make implicit assumptions about platform thread execution models or platform-specific date/time libraries?
what-interviewers-listen-for:
- KMP shares business logic, domain models, viewmodels, repositories, and networking/database code, while platform-specific UI and system integrations remain native.
- Sharing core logic reduces duplication and ensures consistent business rules, but requires careful interface design for platform-specific capabilities.
- Platform timezone and date implementations differ significantly between Android (java.time) and iOS (Foundation Date), requiring careful expect/actual or library wrappers (like kotlinx-datetime).
build-exercise: Create a shared common module with a simple calculator function, and call it from both Android and iOS source sets. Observe how shared business logic compiles to both JVM bytecode and native binaries.
official-references:
- Kotlin Multiplatform overview | https://kotlinlang.org/docs/multiplatform.html
common-wrong-beliefs:
- Kotlin Multiplatform lets you write 100% of your entire app including native UI and system APIs once in Kotlin | KMP shares business and domain logic, but UI and platform-specific OS APIs require native implementation or Compose Multiplatform abstractions.

## kmp/what-is-kmp/how-kotlin-reaches-an-iphone
interview-questions:
- [intern] How does Kotlin code compile and run inside an iOS application alongside Swift?
- [junior] What is Kotlin Native, and what is the role of Kotlin/Native's LLVM compiler backend?
- [mid] How does Kotlin/Native's automatic memory management differ from the JVM's garbage collector, especially regarding strict concurrency rules?
- [senior] What goes wrong in production when sharing state across Kotlin/Native worker threads without adhering to immutability or modern memory manager rules?
what-interviewers-listen-for:
- Kotlin/Native compiles shared Kotlin source code directly into native ARM64 machine code packaged as an Apple Framework (`.framework` or `.xcframework`).
- Modern Kotlin/Native features a concurrent garbage collector, removing historical freezing restrictions while requiring thread-safe data ownership.
- LLVM compiles intermediate representation into optimized native binaries for iOS devices and simulators.
build-exercise: Inspect the Gradle build task that generates the Apple framework output from a KMP shared module, and check how it links into Xcode.
official-references:
- Kotlin Multiplatform development steps | https://kotlinlang.org/docs/multiplatform-getting-started.html
- Kotlin/Native memory management | https://kotlinlang.org/docs/native-memory-manager.html
common-wrong-beliefs:
- Kotlin code running on iOS requires a JVM installed on the iPhone | Kotlin/Native compiles Kotlin source code directly into native machine code binary instructions that execute natively on iOS without any virtual machine.

## kmp/source-sets/project-layout-and-targets
interview-questions:
- [intern] What is the standard directory structure of a Kotlin Multiplatform project (`commonMain`, `androidMain`, `iosMain`)?
- [junior] What does the Gradle build file declare regarding targets (e.g., `androidTarget()`, `iosX64()`, `iosArm64()`, `iosSimulatorArm64()`)?
- [mid] How do hierarchical project structure (HPS) source sets allow sharing code between iOS-only targets (`iosMain`) without duplicating code?
- [senior] What goes wrong in production when adding a multiplatform library dependency to `commonMain` that lacks support for one of the configured target platforms?
what-interviewers-listen-for:
- Source sets organize code hierarchically: `commonMain` is shared by all, intermediate source sets like `iosMain` share iOS-specific code, and platform targets compile for specific architectures.
- Adding dependencies to `commonMain` requires that the library publishes artifacts supporting all targeted platforms; unsupported targets fail during Gradle sync.
build-exercise: Open a KMP project structure in Android Studio, inspect the `build.gradle.kts` file targets configuration, and examine the `commonMain` vs `androidMain` directory layout.
official-references:
- Multiplatform project layout | https://kotlinlang.org/docs/multiplatform-discover-project.html
common-wrong-beliefs:
- Every platform target in a KMP project must have its own completely separate copy of network and storage code | Hierarchical project structures allow sharing code across subsets of platforms (e.g., all mobile devices in `mobileMain`) before hitting platform-specific targets.

## kmp/source-sets/what-common-code-cannot-use
interview-questions:
- [intern] Why can code inside `commonMain` not call Android-specific (`android.*`) or iOS-specific (`UIKit.*`) APIs directly?
- [junior] How does the compiler enforce that shared common code remains platform-agnostic?
- [mid] What is the architectural pattern used when common code needs to invoke platform-specific capabilities (Expect/Actual)?
- [senior] What goes wrong in production when developers attempt to smuggle platform-specific dependencies into `commonMain` via reflection or JVM-only libraries?
what-interviewers-listen-for:
- `commonMain` compiles against common Kotlin standard library APIs and multiplatform libraries, lacking access to platform-specific SDKs.
- Using platform classes directly in common code causes unresolved reference compilation errors.
- Expect/Actual mechanism provides contract declarations in common code implemented natively per platform.
build-exercise: Attempt to reference `android.os.Build` or `UIKit.UIDevice` directly inside a `commonMain` Kotlin file and observe the compiler unresolved reference error.
official-references:
- Multiplatform compatibility | https://kotlinlang.org/docs/multiplatform-connect-to-apis.html
common-wrong-beliefs:
- You can use Java reflection or JVM-specific libraries inside `commonMain` as long as you only run the code on Android | `commonMain` must compile successfully for iOS and desktop targets as well, so JVM-only libraries and reflection fail compilation entirely.

## kmp/expect-actual/expect-and-actual
interview-questions:
- [intern] What are `expect` and `actual` declarations in Kotlin Multiplatform, and what problem do they solve?
- [junior] How do you write an `expect class` or `expect fun` in `commonMain` and provide corresponding `actual` implementations in `androidMain` and `iosMain`?
- [mid] Why does the Kotlin compiler require `actual` declarations to match `expect` signatures precisely across every target?
- [senior] What goes wrong in production when expect/actual declarations diverge in behavior or state management across Android and iOS implementations?
what-interviewers-listen-for:
- `expect` declares an API contract in common code; `actual` provides the platform-specific implementation.
- The compiler enforces strict signature matching, ensuring all targets implement the required contract.
- Behavioral divergence between actual implementations leads to platform-specific bugs and inconsistent app behavior.
build-exercise: Write an `expect class PlatformInfo` in `commonMain` with a function `getName()`, and provide `actual` implementations in `androidMain` (returning "Android") and `iosMain` (returning "iOS").
official-references:
- Expect and actual declarations | https://kotlinlang.org/docs/multiplatform-connect-to-apis.html
common-wrong-beliefs:
- `expect` and `actual` declarations are resolved dynamically at runtime | Expect/actual resolution happens entirely at compile time via static multiplatform linking; there is no runtime reflection overhead.

## kmp/expect-actual/interface-instead-of-expect-actual
interview-questions:
- [intern] When is using a shared interface injected via Dependency Injection preferred over using `expect` and `actual` declarations?
- [junior] What are the testability advantages of defining a common interface instead of platform-specific expect/actual classes?
- [mid] What is the trade-off between compile-time enforcement (`expect/actual`) and runtime polymorphism (interfaces + DI)?
- [senior] What goes wrong in production when overusing top-level `expect/actual` functions instead of object-oriented dependency injection for platform services?
what-interviewers-listen-for:
- Interfaces injected via DI allow swapping implementations easily for unit testing (providing fakes), whereas expect/actual functions are statically bound at compile time.
- `expect/actual` is best for platform primitive wrappers or top-level system bindings, while interfaces excel for swappable repositories and services.
build-exercise: Refactor a platform logger from an `expect/actual` top-level function into a common interface `Logger` implemented separately in Android and iOS modules.
official-references:
- Multiplatform architectural best practices | https://kotlinlang.org/docs/multiplatform-best-practices.html
common-wrong-beliefs:
- `expect` and `actual` should be used for every single platform-specific service and repository in a KMP app | Interfaces combined with dependency injection provide better testability and modularity than static `expect/actual` declarations.

## kmp/shared-logic/what-to-share-first
interview-questions:
- [intern] Which architectural layers of a mobile app should be moved into the KMP shared module first?
- [junior] Why are data models, networking clients, local databases, and ViewModels ideal candidates for early sharing?
- [mid] What is the migration strategy for transitioning an existing native Android app to KMP without rewriting the whole codebase at once?
- [senior] What goes wrong in production when attempting to share complex platform-bound UI components or navigation graphs too early in a KMP migration?
what-interviewers-listen-for:
- Share business logic, domain models, validation rules, and data layers first; UI and navigation can follow using Compose Multiplatform or native views.
- Incremental adoption minimizes risk by keeping existing UI screens while migrating underlying repositories to shared code.
build-exercise: Extract a data validation domain model and a repository interface from an Android-only module into a new KMP shared module.
official-references:
- Migrate to Kotlin Multiplatform | https://kotlinlang.org/docs/multiplatform-migrate-existing-app.html
common-wrong-beliefs:
- Migrating to Kotlin Multiplatform requires rewriting your entire Android and iOS app from scratch in one massive release | KMP allows incremental extraction, sharing core business logic while keeping existing UI screens intact.

## kmp/shared-logic/shared-viewmodel-and-state
interview-questions:
- [intern] Can a single shared ViewModel (or state holder) drive both an Android Compose screen and an iOS SwiftUI screen?
- [junior] How do `StateFlow` and shared coroutines function when consumed by Swift UI code on iOS?
- [mid] How do you bridge Kotlin's `StateFlow` to Swift publishers or observable objects (`ObservableObject`) in SwiftUI?
- [senior] What goes wrong in production when Swift UI components retain shared Kotlin objects across lifecycle boundaries, causing memory leaks or coroutine scoping bugs?
what-interviewers-listen-for:
- Yes, shared ViewModels using `StateFlow` and `viewModelScope` can drive multiplatform UI.
- Swift interop requires bridging coroutine flows to Swift Combine publishers or observation wrappers (using libraries like SKIE or custom flow wrappers).
- Lifecycle observation differences between Android (`LifecycleEventObserver`) and iOS (`onDisappear`) require careful scope management.
build-exercise: Create a shared ViewModel with a `MutableStateFlow` in common code, and write a simple Swift wrapper or use SKIE to observe it in a SwiftUI view.
official-references:
- SwiftUI and Kotlin Multiplatform | https://kotlinlang.org/docs/multiplatform-mobile-swiftui.html
common-wrong-beliefs:
- You cannot use Jetpack-style ViewModels or `StateFlow` in shared KMP code destined for iOS | Shared ViewModels and `StateFlow` work natively in KMP; Swift can observe them with lightweight wrapper extensions or modern interop tooling.

## kmp/shared-networking/a-shared-api-client
interview-questions:
- [intern] Which HTTP client library is standard for making network requests in shared KMP code?
- [junior] How do you configure Ktor HttpClient in `commonMain` with JSON serialization and logging plugins?
- [mid] How does Ktor leverage platform-specific HTTP engines (CIO, Darwin, Android) under the hood?
- [senior] What goes wrong in production when Ktor client engines are misconfigured on iOS (e.g., missing Darwin engine dependency or transport security ATS blocks)?
what-interviewers-listen-for:
- Ktor is the standard multiplatform HTTP client.
- Ktor delegates low-level socket and TLS operations to platform engines: `OkHttp` or `Android` on Android, and `Darwin` (`NSURLSession`) on iOS.
- Missing platform engine dependencies cause runtime `No suitable HTTP engine found` exceptions.
build-exercise: Configure a Ktor `HttpClient` in `commonMain` using `ContentNegotiation` (Kotlinx Serialization) and the Darwin/Android engine, and perform a test GET request.
official-references:
- Ktor documentation | https://ktor.io/docs/client.html
- Ktor multiplatform tutorial | https://ktor.io/docs/getting-started-ktor-client.html
common-wrong-beliefs:
- Ktor implements its own custom networking stack from scratch across all platforms | Ktor acts as a multiplatform client facade that delegates actual socket communication to battle-tested native engines (`OkHttp` on Android, `NSURLSession/Darwin` on iOS).

## kmp/shared-networking/ktor-plugins
interview-questions:
- [intern] How do Ktor plugins add features like JSON deserialization, authentication headers, and request logging to shared API calls?
- [junior] How does `ContentNegotiation` with `kotlinx.serialization` parse JSON into shared Kotlin data classes?
- [mid] How do Ktor `Install(Auth)` or custom `ClientPlugin` interceptors attach bearer tokens to requests automatically?
- [senior] What goes wrong in production when token refresh interceptors in Ktor trigger deadlocks or recursive re-authentication loops?
what-interviewers-listen-for:
- Ktor plugins intercept request and response pipelines for logging, serialization, and authentication.
- `ContentNegotiation` serializes request bodies and deserializes JSON responses automatically.
- Interceptors must handle concurrency safely to prevent infinite loops during token expiration refreshes.
build-exercise: Install `ContentNegotiation` and `Logging` plugins into a Ktor client in `commonMain`, and inspect serialized JSON request/response payloads in Logcat/Console.
official-references:
- Ktor client plugins | https://ktor.io/docs/client-plugins.html
common-wrong-beliefs:
- Ktor requires platform-specific JSON parsing libraries like Gson or Moshi in shared code | Ktor integrates with `kotlinx.serialization` for 100% multiplatform serializable data parsing.

## kmp/shared-networking/platform-rules-still-apply
interview-questions:
- [intern] Even when networking code is written in shared KMP code, what platform-specific networking rules still apply on Android and iOS?
- [junior] How do Android's Cleartext Traffic restrictions and iOS's App Transport Security (ATS) affect shared KMP network calls?
- [mid] Why do SSL certificate pinning or custom trust stores require platform-specific configuration even in a KMP project?
- [senior] What goes wrong in production when iOS builds reject HTTP endpoints because ATS blocks non-HTTPS traffic by default?
what-interviewers-listen-for:
- Shared networking code still executes within the security sandbox of the host OS.
- Android Network Security Config and iOS Info.plist App Transport Security (ATS) rules govern permitted connections, cleartext policies, and domain exceptions.
- Certificate pinning implementations require platform-specific engine hooks (`OkHttp` certificate pinner vs `NSURLSession` delegate).
build-exercise: Attempt to hit an unencrypted `http://` endpoint from shared KMP code on iOS, observe the ATS rejection error, and configure Info.plist exception domains.
official-references:
- Ktor client engines | https://ktor.io/docs/client-engines.html
common-wrong-beliefs:
- Writing networking code in KMP shared modules bypasses Android network security policies and iOS App Transport Security rules | Shared code runs on top of native platform HTTP engines and must obey all OS-level network security, TLS, and transport policies.

## kmp/shared-storage/shared-settings
interview-questions:
- [intern] How do you share simple key-value settings storage (like SharedPreferences or NSUserDefaults) across platforms in KMP?
- [junior] What multiplatform library provides a unified key-value storage API for Android and iOS?
- [mid] How does `Multiplatform Settings` wrap Android's SharedPreferences/DataStore and iOS's NSUserDefaults?
- [senior] What goes wrong in production when storing sensitive auth tokens in plain SharedPreferences/NSUserDefaults instead of encrypted secure storage?
what-interviewers-listen-for:
- Libraries like `Multiplatform Settings` provide a common key-value API backed by `SharedPreferences`/`DataStore` on Android and `NSUserDefaults` on iOS.
- Plain key-value stores are unencrypted and insecure for sensitive credentials.
- Secure storage requires platform-specific implementations (`EncryptedSharedPreferences` vs Keychain).
build-exercise: Initialize `Multiplatform Settings` in `commonMain`, save a boolean preference flag, and read it back on both Android and iOS.
official-references:
- Multiplatform Settings library | https://github.com/russhwolf/multiplatform-settings
common-wrong-beliefs:
- Android's `SharedPreferences` or iOS's `NSUserDefaults` can be called directly inside shared common code | Platform storage APIs differ entirely; multiplatform libraries or expect/actual wrappers are required to bridge them.

## kmp/shared-storage/shared-database
interview-questions:
- [intern] How do you share a local SQL database between Android and iOS in a Kotlin Multiplatform project?
- [junior] What is SQLDelight, and how does it generate type-safe Kotlin APIs from standard SQL statements?
- [mid] How do SQLDelight drivers (`AndroidSqliteDriver` vs `NativeSqliteDriver`) initialize the database file path per platform?
- [senior] What goes wrong in production when schema migrations (`.sq` migration files) are misconfigured during app updates on SQLite databases?
what-interviewers-listen-for:
- SQLDelight is the standard multiplatform database library, compiling `.sq` SQL files into type-safe Kotlin queries and models.
- Platform-specific drivers supply the correct local file path context for Android and iOS (`NSDocumentDirectory`).
- Database migrations require explicit versioned migration scripts to prevent data loss or schema corruption on upgrade.
build-exercise: Write a simple SQLDelight `.sq` file with a CREATE TABLE statement and a select query, configure platform drivers, and perform a local insert/query test.
official-references:
- SQLDelight documentation | https://cashapp.github.io/sqldelight/
common-wrong-beliefs:
- Room database can be used directly in KMP shared code across both Android and iOS | Room relies heavily on JVM annotation processing and SQLite bindings tied to Android; SQLDelight is the multiplatform equivalent designed for KMP.

## kmp/shared-storage/shared-repository
interview-questions:
- [intern] How do a shared Kktor API client and a shared SQLDelight database collaborate behind a single shared Repository class?
- [junior] How do you implement an offline-first caching strategy (fetch from cache, sync with network) inside a shared KMP repository?
- [mid] How do Kotlin Flows flow seamlessly from SQLDelight database queries up to shared ViewModels across platforms?
- [senior] What goes wrong in production when repository coroutines encounter unhandled concurrent database write locks or transaction failures across threads?
what-interviewers-listen-for:
- Shared repositories orchestrate data flow by exposing SQLDelight reactive `Flow` queries as the single source of truth while refreshing via Ktor.
- SQLDelight natively supports returning `Flow<List<Model>>`, updating UI automatically when database rows change.
- Multi-threaded writes require proper transaction management and dispatcher confinement to avoid SQLite `SQLITE_BUSY` locks.
build-exercise: Build a shared Repository class in `commonMain` that queries a SQLDelight database flow and triggers a Ktor network sync refresh.
official-references:
- SQLDelight Flow queries | https://cashapp.github.io/sqldelight/features/coroutines/
- Kotlin Multiplatform architecture | https://kotlinlang.org/docs/multiplatform-samples.html
common-wrong-beliefs:
- Repositories in KMP cannot use reactive streams like Flow to push database changes to platform UIs | SQLDelight queries expose Kotlin `Flow` objects that flow smoothly into shared `StateFlow` and multiplatform UI collectors.

## kmp/compose-multiplatform/what-is-compose-multiplatform
interview-questions:
- [intern] What is Compose Multiplatform, and how does it relate to Jetpack Compose on Android?
- [junior] Which platforms can render UI using Compose Multiplatform (Android, iOS, Desktop, Web)?
- [mid] How does Compose Multiplatform render UI on iOS (Skia canvas vs native UIKit views)?
- [senior] What goes wrong in production when Compose Multiplatform iOS apps fail to integrate native iOS gestures, accessibility, or typography correctly?
what-interviewers-listen-for:
- Compose Multiplatform extends Jetpack Compose to desktop, web, and iOS by leveraging JetBrains' multiplatform rendering engine powered by Skia (Skiko).
- On iOS, Compose renders UI onto a hardware-accelerated Skia canvas inside a UIKit view controller.
- Skia rendering requires special attention for native iOS accessibility (VoiceOver) and touch gesture tuning.
build-exercise: Create a simple Composable button and text element in `commonMain` and render it simultaneously inside an Android app and an iOS app project.
official-references:
- Compose Multiplatform | https://www.jetbrains.com/compose-multiplatform/
common-wrong-beliefs:
- Compose Multiplatform compiles your Jetpack Compose code into native iOS `UIView` components under the hood | Compose Multiplatform draws UI components directly onto a hardware-accelerated Skia graphics canvas inside an iOS view controller rather than translating them to native UIKit widgets.

## kmp/compose-multiplatform/shared-ui-screen
interview-questions:
- [intern] How do you structure a shared UI screen so it can be launched as the root view of both an Android Activity and an iOS UIViewController?
- [junior] What is `ComposeUIViewController` in iOS Compose multiplatform integration?
- [mid] How do shared Compose screens handle safe areas, status bars, and keyboard insets across Android and iOS?
- [senior] What goes wrong in production when shared Compose UI layouts fail to adapt to varying screen aspect ratios and split-screen window sizes across devices?
what-interviewers-listen-for:
- Android hosts shared UI via `setContent { App() }`, while iOS hosts it via `ComposeUIViewController { App() }` wrapped in a UIKit UIViewController.
- Window insets and safe areas require multiplatform padding adjustments to avoid overlapping system notches.
build-exercise: Write a shared App composable and wrap it in `ComposeUIViewController` for iOS and `setContent` for Android to verify dual-platform hosting.
official-references:
- Compose Multiplatform iOS getting started | https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-ios-ui-getting-started.html
common-wrong-beliefs:
- You need a separate UI entry point file for every single screen in iOS compared to Android when using Compose Multiplatform | A single root shared Composable can be hosted inside your platform entry points (`MainActivity` and `MainViewController`), rendering identical shared UI across both systems.

## kmp/compose-multiplatform/shared-resources
interview-questions:
- [intern] Where do shared images, string resources, and fonts live in a Compose Multiplatform project so both Android and iOS can access them?
- [junior] What is the Compose Multiplatform Resources library (`composeResources`), and how does it generate type-safe resource accessors (e.g., `Res.string.title`, `Res.drawable.logo`)?
- [mid] How does the build system bundle and optimize resources for Android (APK/AAB) versus iOS (Asset Catalogs)?
- [senior] What goes wrong in production when referencing resource names dynamically at runtime without compile-time safety generated by plugin processors?
what-interviewers-listen-for:
- Resources live in the common source set under `commonMain/composeResources/` (categorized into `drawable`, `string`, `font`, `files`).
- The multiplatform resources plugin generates type-safe accessors at compile time (`Res.string...`).
- Compile-time safety prevents missing resource crashes at runtime across platforms.
build-exercise: Add an image and a string resource inside `commonMain/composeResources/`, reference them using generated `Res.drawable` and `Res.string` accessors in a Composable, and run it.
official-references:
- Compose Multiplatform resources | https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-images-resources.html
common-wrong-beliefs:
- You must maintain separate `res/values/strings.xml` folders on Android and separate Localizable.strings files on iOS for every shared string when using Compose Multiplatform | Compose Multiplatform resources centralize multiplatform assets in `commonMain/composeResources/` with compile-time type safety.

## kmp/compose-multiplatform/navigation-and-back-on-both
interview-questions:
- [intern] How do you handle screen navigation and back button/gesture handling in a Compose Multiplatform app across Android and iOS?
- [junior] How do navigation libraries like Jetpack Navigation or Voyager support multiplatform back stacks?
- [mid] How do you reconcile Android's physical/gesture back button with iOS's edge-swipe back gesture in shared UI?
- [senior] What goes wrong in production when multiplatform back stack state is lost during iOS view controller dismissal or Android configuration changes?
what-interviewers-listen-for:
- Multiplatform navigation libraries (like Jetpack Navigation Compose Multiplatform or Voyager) manage cross-platform back stacks.
- Android hardware back button events must be intercepted and mapped to back stack pops, while iOS relies on swipe-back gestures or navigation bar back buttons.
build-exercise: Set up a two-screen navigation flow in a shared Compose Multiplatform module and verify back navigation works via Android back button and iOS swipe gesture.
official-references:
- Compose Multiplatform navigation | https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation.html
common-wrong-beliefs:
- Android's hardware back button behavior maps automatically to iOS without any configuration in Compose Multiplatform | iOS devices lack a physical back button, requiring explicit navigation bar controllers or edge-swipe gesture handlers paired with your multiplatform navigation setup.

## kmp/swift-interop/calling-kotlin-from-swift
interview-questions:
- [intern] What does shared Kotlin code look like when called from Swift in an iOS project?
- [junior] How does Kotlin's nullability (`String?`) and collection types (`List`, `Map`) translate into Swift optionals (`String?`) and arrays (`Array`)?
- [mid] What are Kotlin `suspend` functions converted into when exposed to Swift (completion handlers / async-await)?
- [senior] What goes wrong in production when Swift code attempts to call Kotlin APIs that use unsupported types (like inline value classes or raw unsigned primitives) without proper interop annotations?
what-interviewers-listen-for:
- Kotlin classes map to Swift classes, `List` maps to Swift `Array`, `Map` maps to Swift `Dictionary`, and nullables map to optionals.
- Kotlin `suspend` functions map to Swift `async/await` methods (or completion handlers in older interop modes).
- Unsigned integers or complex generics sometimes require explicit bridging or `@ObjC` / `@Throws` annotations.
build-exercise: Write a shared Kotlin suspend function returning a list of data class items, and call it from Swift using `await` inside an iOS ViewModel.
official-references:
- Interoperability with Swift / Objective-C | https://kotlinlang.org/docs/native-objc-interop.html
common-wrong-beliefs:
- Kotlin `suspend` functions cannot be called as native `async/await` functions in Swift | Modern Kotlin/Native and Swift interop (including Xcode async bridging) automatically translates Kotlin suspending functions into native Swift `async/await` methods.

## kmp/swift-interop/mixing-native-and-shared-ui
interview-questions:
- [intern] Can a Kotlin Multiplatform app mix native iOS screens (SwiftUI / UIKit) with shared Compose Multiplatform screens?
- [junior] How do you embed a shared Compose screen inside an existing native iOS view controller hierarchy?
- [mid] When is a hybrid approach (native screens for platform-heavy features, shared Compose for core flows) recommended over 100% shared UI?
- [senior] What goes wrong in production when passing complex state objects back and forth across the native Swift and shared Kotlin UI boundary without lifecycle synchronization?
what-interviewers-listen-for:
- Yes, KMP supports hybrid architectures where specific screens are native Swift and others are shared Compose.
- `ComposeUIViewController` can be pushed onto a standard iOS `UINavigationController` just like any native view controller.
- Hybrid setups are ideal for gradually migrating mature native apps to KMP.
build-exercise: Embed a shared Compose Composable inside a native SwiftUI view hierarchy using `UIViewControllerRepresentable`.
official-references:
- Compose Multiplatform interoperability | https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-ios-ui-integration.html
common-wrong-beliefs:
- Adopting Compose Multiplatform requires your entire iOS app UI to be built in Kotlin with zero native Swift screens permitted | Compose Multiplatform screens can be embedded selectively inside existing native iOS SwiftUI or UIKit view hierarchies component by component.

## kmp/libraries/choosing-kmp-libraries
interview-questions:
- [intern] How can you determine whether a third-party library supports Kotlin Multiplatform before adding it to your project?
- [junior] What Gradle artifact naming convention indicates that a library supports KMP (e.g., `-multiplatform` or platform-specific artifact suffixes)?
- [mid] Why can't you include a standard JVM-only library (like standard Gson or Retrofit) directly in `commonMain`?
- [senior] What goes wrong in production when adopting an immature KMP community library that lacks proper iOS memory manager compatibility or target support?
what-interviewers-listen-for:
- Check library documentation, Maven Central artifacts for multiplatform metadata (`.klib`), and community support.
- JVM-only libraries depend on JVM bytecode and reflection, failing compilation against Kotlin/Native iOS targets.
- Immature KMP libraries risk compilation breaks during Xcode updates or memory crashes under Kotlin/Native strict concurrency rules.
build-exercise: Search Maven Central for a popular multiplatform library (like `kotlinx-serialization` or `ktor`), and examine its published Gradle module metadata and target artifacts.
official-references:
- Kotlin Multiplatform libraries | https://kotlinlang.org/docs/multiplatform-libraries.html
common-wrong-beliefs:
- Any standard Java or Android library available on Maven Central can be added to a KMP `commonMain` source set | Libraries added to `commonMain` must explicitly publish multiplatform targets (`.klib`); JVM-only libraries cannot compile for iOS targets.

## kmp/shipping-to-ios/ship-a-shared-feature-to-both-platforms
interview-questions:
- [intern] What is the complete workflow of taking a new shared feature from your KMP codebase to running builds on an Android phone and an iPhone?
- [junior] How does Gradle build the Android app bundle and export the Apple framework (`.framework` or `.xcframework`) for Xcode?
- [mid] How do CI/CD pipelines (GitHub Actions) automate building and testing KMP projects across Linux/Windows (Android) and macOS (iOS)?
- [senior] What goes wrong in production when local developer Xcode signing or CocoaPods/Swift Package Manager framework paths break during KMP framework updates?
what-interviewers-listen-for:
- Workflow: write shared Kotlin code in `commonMain`, build via Gradle tasks (`embedAndSignAppleFrameworkForXcode`), consume framework in Xcode for iOS, and build APK/AAB for Android.
- CI/CD requires macOS runners to compile iOS frameworks and run iOS simulator tests.
- Framework path misconfigurations between Xcode build phases and Gradle outputs break iOS compilation.
build-exercise: Run the Gradle task to build the iOS framework from your KMP project and verify it links correctly into an Xcode workspace build.
official-references:
- Kotlin Multiplatform wizard and workflow | https://kmp.jetbrains.com/
- Multiplatform CI setup | https://kotlinlang.org/docs/multiplatform-ci.html
common-wrong-beliefs:
- Building an iOS app from a KMP project can be done entirely on a Windows or Linux PC without a macOS machine or Xcode | Compiling iOS binaries, running iOS simulators, and building Xcode projects strictly require macOS and Xcode.

## Self-check
1. Kotlin Multiplatform overview | https://kotlinlang.org/docs/multiplatform.html (Verify exact URL path)
2. Kotlin/Native memory management | https://kotlinlang.org/docs/native-memory-manager.html (Verify exact title)
3. Multiplatform project layout | https://kotlinlang.org/docs/multiplatform-discover-project.html (Verify exact URL path)
4. Multiplatform compatibility | https://kotlinlang.org/docs/multiplatform-connect-to-apis.html (Verify exact URL path)
5. Migrate to Kotlin Multiplatform | https://kotlinlang.org/docs/multiplatform-migrate-existing-app.html (Verify exact URL path)
6. SwiftUI and Kotlin Multiplatform | https://kotlinlang.org/docs/multiplatform-mobile-swiftui.html (Verify exact URL path)
7. Ktor multiplatform tutorial | https://ktor.io/docs/getting-started-ktor-client.html (Verify exact URL path)
8. Multiplatform Settings library | https://github.com/russhwolf/multiplatform-settings (Verify GitHub repository URL format)
9. SQLDelight documentation | https://cashapp.github.io/sqldelight/ (Verify exact URL path)
10. Compose Multiplatform | https://www.jetbrains.com/compose-multiplatform/ (Verify JetBrains documentation URL)