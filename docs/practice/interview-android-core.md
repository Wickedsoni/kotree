# interview: android-core

## android-core/what-is-an-app/what-is-inside-an-app
interview-questions:
- [intern] What are the core files and folders inside a compiled Android APK or AAB?
- [junior] Why does Android execute DEX bytecode (.dex) instead of standard Java class files?
- [mid] What is the trade-off between shipping an Android App Bundle (AAB) versus a universal APK?
- [senior] What goes wrong in production when multidex, resource shrinking, or native library ABIs are misconfigured?
what-interviewers-listen-for:
- Knowledge of classes.dex, resources.arsc, compiled AndroidManifest.xml, assets, and lib/ ABI folders.
- App Bundle download size optimization via split APKs vs Play App Signing dependency.
- Production risks of unoptimized DEX sizing and missing native library symbols.
build-exercise: Build a debug APK in Android Studio, open it via Build -> Analyze APK, and inspect classes.dex, resources.arsc, and the compiled manifest structure.
official-references:
- Configure your build | https://developer.android.com/build
- Analyze your build with APK Analyzer | https://developer.android.com/studio/debug/apk-analyzer
common-wrong-beliefs:
- APK files contain human-readable XML layout and manifest files | XML files inside an APK are compiled into binary XML format.

## android-core/what-is-an-app/who-starts-your-app
interview-questions:
- [intern] If you never write a `main()` function in Android, what entry point starts your app?
- [junior] What is the role of the Zygote process when launching a new Android application?
- [mid] Why does Android fork processes from Zygote instead of starting a fresh Dalvik/ART virtual machine from scratch?
- [senior] What goes wrong in production when heavy initialization blocks ActivityThread.main on the main looper during startup?
what-interviewers-listen-for:
- Zygote forks preloaded framework system processes, invoking ActivityThread.main() and starting the main Looper.
- Copy-on-write memory sharing trade-off versus cold start overhead and ANR risks.
build-exercise: Place a breakpoint inside Application.onCreate() and inspect the call stack up to ActivityThread.main and ZygoteInit.
official-references:
- Processes and app lifecycle | https://developer.android.com/guide/components/activities/process-lifecycle
- Overview of memory management | https://developer.android.com/topic/performance/memory-overview
common-wrong-beliefs:
- Activity.onCreate is the very first code that executes in an app | ContentProviders, Application.onCreate, and ActivityThread initialization run before any Activity.

## android-core/activities/what-is-an-activity
interview-questions:
- [intern] What is an Activity in Android and what is its primary responsibility?
- [junior] How does an Activity attach a Jetpack Compose UI hierarchy to the screen window?
- [mid] Why has modern Android architecture shifted toward single-Activity applications over multiple Activities?
- [senior] What goes wrong in production when an Activity is treated as a god object holding business logic and network states?
what-interviewers-listen-for:
- Activity manages a window entry point managed by ActivityTaskManagerService.
- Single-Activity simplifies shared ViewModel states and navigation transitions vs multi-Activity process boundaries.
build-exercise: Create a ComponentActivity calling setContent, and log window.decorView hierarchy in onResume to inspect how ComposeView attaches.
official-references:
- Introduction to activities | https://developer.android.com/guide/components/activities/intro-activities
common-wrong-beliefs:
- An Activity is the screen UI itself | An Activity is a system component hosting a Window into which Views or Composables are rendered.

## android-core/activities/what-is-context
interview-questions:
- [intern] What is a Context in Android and why do so many APIs require it?
- [junior] What is the difference between an Activity Context and an Application Context?
- [mid] Why is Context designed as a wrapper hierarchy (ContextWrapper) rather than a global singleton?
- [senior] What goes wrong in production when you pass an Activity Context into a singleton or use Application Context to inflate themed UI?
what-interviewers-listen-for:
- Context bridges application code to system services, resources, themes, and private storage.
- Activity Context carries theme and window token but risks memory leaks if retained; Application Context lives with the process but lacks UI theming.
build-exercise: Pass Application Context vs Activity Context when inflating a themed view or showing a Dialog, and observe theme mismatches or WindowManager BadTokenExceptions.
official-references:
- Context | https://developer.android.com/reference/android/content/Context
common-wrong-beliefs:
- Application Context and Activity Context are completely interchangeable | Application Context lacks Activity theme overrides and cannot attach UI windows tied to an Activity token.

## android-core/activities/back-stack
interview-questions:
- [intern] What happens to the previous screen when you open a new Activity or destination?
- [junior] How does the Android back stack manage task history and screen destruction?
- [mid] What is the difference between standard launch mode and singleTop, singleTask, or singleInstance activity launch modes?
- [senior] What goes wrong in production when custom launch modes interact incorrectly with deep links and multi-window back stacks?
what-interviewers-listen-for:
- Activities are managed in a Last-In, First-Out (LIFO) back stack within a Task.
- Launch modes control task affinity and instance reuse, preventing deep stack duplication.
build-exercise: Launch Activity A -> B -> C, press back, and log lifecycle callbacks to verify stack popping and recreation order.
official-references:
- Tasks and back stack | https://developer.android.com/guide/components/activities/tasks-and-back-stack
common-wrong-beliefs:
- Setting singleTask on every Activity solves all back navigation problems | Overusing custom launch modes breaks standard back stack predictability and deep link navigation flows.

## android-core/lifecycle/what-is-the-lifecycle
interview-questions:
- [intern] What are the primary callback states of an Android Activity lifecycle (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`)?
- [junior] Why does Android notify components about state transitions instead of letting apps run continuously?
- [mid] How do configuration changes and process death interact with the standard lifecycle callbacks?
- [senior] What goes wrong in production when asynchronous callbacks update UI views after `onStop` or `onDestroy`?
what-interviewers-listen-for:
- Lifecycle callbacks coordinate system resource allocation, foreground visibility, and state preservation.
- UI updates after stop/destroy cause memory leaks and IllegalStateExceptions.
build-exercise: Log all lifecycle methods in an Activity during rotation, home button presses, and app closure.
official-references:
- Activity lifecycle | https://developer.android.com/guide/components/activities/activity-lifecycle
common-wrong-beliefs:
- `onPause` is guaranteed to save user data and critical state | `onPause` is brief; critical persistence should happen in `onStop` or `onSaveInstanceState`.

## android-core/lifecycle/lifecycle-callbacks-order
interview-questions:
- [intern] In what exact order do `onCreate`, `onStart`, and `onResume` run when an Activity starts up?
- [junior] When transitioning from Activity A to Activity B, in what order do A's pause/stop and B's create/start/resume execute?
- [mid] Why does Android execute Activity A's `onPause` before Activity B's `onCreate`?
- [senior] What goes wrong in production when assuming Activity A is fully stopped before Activity B begins loading data?
what-interviewers-listen-for:
- Transition order: A.onPause -> B.onCreate -> B.onStart -> B.onResume -> A.onStop.
- Ensuring smooth animations requires keeping A active until B's window is visible.
build-exercise: Implement two Activities, log every lifecycle method with timestamps during navigation, and observe interleaving order.
official-references:
- Understanding the Activity Lifecycle | https://developer.android.com/guide/components/activities/activity-lifecycle
common-wrong-beliefs:
- Activity A's `onStop` fires before Activity B's `onCreate` starts | Activity B starts and renders its first frame before Activity A is officially stopped.

## android-core/lifecycle/lifecycle-aware-code
interview-questions:
- [intern] What is a Lifecycle-Aware component, and what problem did Jetpack Lifecycle solve?
- [junior] How do `LifecycleOwner` and `LifecycleObserver` coordinate work with UI lifecycles?
- [mid] How does `repeatOnLifecycle` protect Kotlin flows from collecting when the app is in the background?
- [senior] What goes wrong in production when legacy `LiveData` or manual lifecycle observers omit unregistering listeners in `onDestroy`?
what-interviewers-listen-for:
- Lifecycle-aware components tie data streams and background work directly to lifecycle states (e.g., STARTED).
- `repeatOnLifecycle` suspends collection automatically on `onStop` and restarts on `onStart`.
build-exercise: Collect a Flow inside an Activity using `repeatOnLifecycle(Lifecycle.State.STARTED)`, push the app to background, emit values, and verify collection pauses.
official-references:
- Lifecycle-aware components | https://developer.android.com/topic/libraries/architecture/lifecycle
common-wrong-beliefs:
- `lifecycleScope.launch` automatically stops collecting flows when the app goes to the background | `lifecycleScope.launch` remains active until `ON_DESTROY`; `repeatOnLifecycle` is required to stop collection during `ON_STOP`.

## android-core/lifecycle/leaking-an-activity
interview-questions:
- [intern] What is an Activity memory leak, and why are they dangerous?
- [junior] How do static references, long-running background threads, and coroutines leak an Activity instance?
- [mid] How do you use LeakCanary or Android Studio Memory Profiler to detect retained Activity instances?
- [senior] What goes wrong in production when memory leaks accumulate under low-memory conditions, triggering frequent OutOfMemoryErrors?
what-interviewers-listen-for:
- Leaks occur when GC roots (static variables, singleton callbacks, active threads) retain short-lived Activity references after destruction.
- Accumulating leaks exhaust heap memory, causing sluggishness and OOM crashes.
build-exercise: Assign an Activity instance to a static variable or uncancelled thread, rotate the screen twice, and capture a heap dump in Android Studio to find the retained Activity.
official-references:
- Investigate memory usage with Memory Profiler | https://developer.android.com/studio/profile/memory-profiler
- LeakCanary | https://square.github.io/leakcanary/
common-wrong-beliefs:
- Setting an Activity variable to `null` inside `onDestroy` guarantees the garbage collector will immediately reclaim it | GC runs asynchronously; holding indirect references elsewhere still prevents reclamation.

## android-core/configuration-changes/why-rotation-restarts
interview-questions:
- [intern] Why does turning your phone from portrait to landscape restart your Activity by default?
- [junior] What resources and configuration parameters (screen size, locale, orientation) trigger configuration changes?
- [mid] Why does Android destroy and recreate the Activity rather than just stretching the UI?
- [senior] What goes wrong in production when developers handle configuration changes manually via `android:configChanges` without reloading resources?
what-interviewers-listen-for:
- Configuration changes require reloading alternative resources (layout-land, drawables, strings).
- Recreating the Activity ensures UI binds correctly to the new resource qualifiers.
- Using `android:configChanges` bypasses automatic resource reloading, causing broken layouts on locale or dark mode switches.
build-exercise: Type text into an EditText, rotate the screen, and observe the input vanish due to default Activity recreation.
official-references:
- Handle configuration changes | https://developer.android.com/guide/topics/resources/runtime-changes
common-wrong-beliefs:
- Activity recreation during rotation is a bug in the Android framework | It is an intentional design feature to ensure layouts and resources match updated configuration qualifiers.

## android-core/configuration-changes/surviving-a-config-change
interview-questions:
- [intern] Where should screen state live so it survives a device rotation?
- [junior] What is the difference between `onSaveInstanceState` (saved state handle) and `ViewModel` for surviving configuration changes?
- [mid] Why can `ViewModel` survive configuration changes while an Activity is destroyed?
- [senior] What goes wrong in production when large bitmaps or database connections are stored in `onSaveInstanceState` (Binder transaction limits)?
what-interviewers-listen-for:
- ViewModels survive configuration changes because the OS retains the ViewModel store during non-finish rotations.
- `onSaveInstanceState` serializes lightweight state into a Bundle passing through Binder transactions (capped at ~1MB).
build-exercise: Store a counter in a ViewModel, rotate the screen 5 times, and verify the counter value is preserved without `onSaveInstanceState`.
official-references:
- Save UI states | https://developer.android.com/topic/libraries/architecture/saving-states
common-wrong-beliefs:
- `ViewModel` survives process death when the OS kills the app in the background | ViewModels only survive configuration changes; they do not survive process death (SavedStateHandle is required for process death).

## android-core/process-death/what-is-process-death
interview-questions:
- [intern] What is process death, and why does Android kill background app processes?
- [junior] How does the Android Low Memory Killer (LMK) prioritize which app process to terminate?
- [mid] What is the difference between configuration change (Activity recreation) and process death (full app restart)?
- [senior] What goes wrong in production when apps assume static variables and singleton states persist after the app is backgrounded?
what-interviewers-listen-for:
- The OS reclaims memory from background apps based on adj scores when RAM is low.
- Process death wipes out all static variables and singleton memory states.
- Restoring state requires `SavedStateHandle` or local database persistence.
build-exercise: Put your app in the background, use ADB (`adb shell am kill <package>`) to simulate process death, reopen the app from recents, and observe state loss.
official-references:
- Processes and app lifecycle | https://developer.android.com/guide/components/activities/process-lifecycle
common-wrong-beliefs:
- If your app is in the background, its process stays in memory indefinitely until the user swipes it away | The OS kills background processes whenever RAM is needed for foreground tasks.

## android-core/process-death/testing-process-death
interview-questions:
- [intern] How do you test that your app gracefully restores state after being killed in the background?
- [junior] How do you use ADB commands or Developer Options ("Don't keep activities") to simulate background process termination?
- [mid] How does `SavedStateHandle` in Jetpack ViewModels facilitate seamless process death state restoration?
- [senior] What goes wrong in production when testing state restoration only via screen rotation instead of full process kill?
what-interviewers-listen-for:
- Testing process death requires killing the app process via ADB or setting "Don't keep activities" in Developer Options.
- `SavedStateHandle` persists state across process death seamlessly.
build-exercise: Enable "Don't keep activities" in Android Studio/Device Developer Options, navigate to screen B, press Home, return to the app, and verify state recovery.
official-references:
- UI state saving | https://developer.android.com/topic/libraries/architecture/saving-states
common-wrong-beliefs:
- Testing with screen rotation is sufficient to verify that your app handles process death correctly | Rotation preserves process memory (ViewModels survive), whereas process death destroys all heap memory.

## android-core/manifest-and-resources/what-is-the-manifest
interview-questions:
- [intern] What is the `AndroidManifest.xml` file, and what essential information does it provide to the Android OS?
- [junior] Why must all components (Activities, Services, BroadcastReceivers, ContentProviders) be declared in the manifest?
- [mid] What are intent filters, and how do they declare which intents a component can handle?
- [senior] What goes wrong in production when components are exported (`android:exported="true"`) without proper permission guards or security checks?
what-interviewers-listen-for:
- Manifest declares app identity, components, permissions, hardware features, and min/target SDK versions.
- Exported components without permissions expose entry points to malicious third-party apps.
build-exercise: Inspect the merged AndroidManifest.xml in the build directory (`app/build/intermediates/...`) to see how manifest merger combines module manifests.
official-references:
- App manifest overview | https://developer.android.com/guide/topics/manifest/manifest-intro
common-wrong-beliefs:
- Android Studio automatically sets `android:exported="false"` for all activities by default | Activities with intent filters default to `exported="true"` in older target SDKs, posing security risks if unconfigured.

## android-core/manifest-and-resources/sdk-versions
interview-questions:
- [intern] What is the difference between `minSdk`, `targetSdk`, and `compileSdk` in Gradle build files?
- [junior] Why does Google Play enforce minimum `targetSdk` requirements for app updates?
- [mid] How do backward compatibility libraries (AndroidX) allow using modern APIs on older devices (`minSdk`)?
- [senior] What goes wrong in production when behavior changes introduced in a new `targetSdk` are not tested before upgrading?
what-interviewers-listen-for:
- `minSdk` sets oldest supported OS version; `targetSdk` indicates tested behavior version; `compileSdk` provides APIs compiled against.
- Upgrading `targetSdk` opts the app into new OS security and privacy behaviors.
build-exercise: Check `build.gradle.kts`, modify `targetSdk`, sync Gradle, and observe compiler warnings regarding behavior changes.
official-references:
- Understand SDK versions | https://developer.android.com/build/releases/manifest-version
common-wrong-beliefs:
- Raising `compileSdk` automatically raises your app's `minSdk` and drops support for older phones | `compileSdk` only affects the API version used for compilation, not the runtime `minSdk`.

## android-core/manifest-and-resources/what-are-resources
interview-questions:
- [intern] Why do strings, images, and layout definitions live in `res/` folders instead of being hardcoded in code?
- [junior] What is the `R` class, and how does Gradle generate integer resource IDs for assets?
- [mid] How does resource compilation optimize XML and image assets into binary formats inside the APK?
- [senior] What goes wrong in production when dynamic resource resolution fails due to missing qualifiers or configuration mismatches?
what-interviewers-listen-for:
- Separating code from resources enables internationalization, multi-device layouts, and theme switching.
- Gradle generates the `R` class linking string and drawable identifiers at compile time.
build-exercise: Add a new string resource in `strings.xml`, reference it in Kotlin code via `R.string.my_string`, and inspect its generated ID.
official-references:
- App resources overview | https://developer.android.com/guide/topics/resources/providing-resources
common-wrong-beliefs:
- Resource IDs generated in the `R` class remain constant across different builds and app versions | Resource IDs are dynamically allocated during compilation builds and can change between releases.

## android-core/manifest-and-resources/resource-qualifiers
interview-questions:
- [intern] How does Android automatically select the correct resource (e.g., Hindi text or dark mode drawables) based on device settings?
- [junior] What are resource qualifiers (e.g., `-hi`, `-land`, `-night`, `-sw600dp`), and how do precedence rules work?
- [mid] How does smallest width (`sw600dp`) differ from screen orientation qualifiers for tablet layouts?
- [senior] What goes wrong in production when unsupported or conflicting resource qualifiers cause fallback resource crashes?
what-interviewers-listen-for:
- Qualifiers match device configuration dynamically at runtime.
- Smallest width (`sw600dp`) guarantees physical screen dimensions regardless of rotation, making it superior to orientation qualifiers for tablets.
build-exercise: Create a `values-hi` resource folder with a Hindi translation of a string, change device language to Hindi, and verify automatic text switching.
official-references:
- Accessing resources | https://developer.android.com/guide/topics/resources/accessing-resources
common-wrong-beliefs:
- Using screen orientation (`-land`) is the best way to design adaptive tablet layouts | Smallest width qualifiers (`sw600dp`) are recommended over orientation qualifiers because they remain stable during rotation.

## android-core/intents/what-is-an-intent
interview-questions:
- [intern] What is an Intent in Android, and what are the two main types (Explicit vs Implicit)?
- [junior] How do you pass data between activities or components using Intent extras?
- [mid] Why does Android use Intents as a messaging system across application boundaries?
- [senior] What goes wrong in production when passing large parcelable objects or bitmaps through Intent extras (TransactionTooLargeException)?
what-interviewers-listen-for:
- Intents are messaging objects used to request actions from other app components.
- Explicit intents target a specific component; implicit intents declare an action for any capable app to handle.
- Intent Bundle transactions are capped at ~1MB; exceeding it throws `TransactionTooLargeException`.
build-exercise: Create an explicit Intent passing a primitive extra string to a second Activity, and retrieve it in `onCreate`.
official-references:
- Intents and intent filters | https://developer.android.com/guide/components/intents-filters
common-wrong-beliefs:
- You can pass arbitrarily large data objects (like a 5MB image) safely through Intent extras between activities | Intent bundle data passes through Binder IPC memory limits (~1MB), causing crashes if exceeded.

## android-core/intents/implicit-intents
interview-questions:
- [intern] What is an implicit intent, and how does the system resolve which app handles it?
- [junior] What is a Resolver Activity (Chooser dialog), and when does it appear?
- [mid] How do Android 11+ package visibility rules (`<queries>`) affect implicit intent resolution?
- [senior] What goes wrong in production when an implicit intent is fired with no matching apps installed on the device (ActivityNotFoundException)?
what-interviewers-listen-for:
- Implicit intents declare actions (`ACTION_SEND`, `ACTION_VIEW`) without specifying a target component.
- Android 11+ package visibility restrictions require declaring `<queries>` in the manifest to see other installed apps.
- Always wrap implicit intent calls in `resolveActivity` checks or `try/catch` to prevent crashes.
build-exercise: Fire an implicit intent to open a web URL, wrap it in a `try/catch` for `ActivityNotFoundException`, and test it.
official-references:
- Android package visibility | https://developer.android.com/training/package-visibility
common-wrong-beliefs:
- Every app on the device is automatically visible to your app's implicit intents on modern Android versions | Package visibility rules introduced in Android 11 hide other apps unless explicitly declared in the manifest.

## android-core/intents/getting-a-result-back
interview-questions:
- [intern] How do you launch another Activity and receive a result back when it finishes?
- [junior] Why was `startActivityForResult` deprecated in favor of Jetpack's `ActivityResultLauncher` API?
- [mid] How do contract classes (`ActivityResultContracts.GetContent()`, `RequestPermission()`) simplify result handling?
- [senior] What goes wrong in production when registering activity result launchers outside of component initialization (lifecycle timing crash)?
what-interviewers-listen-for:
- `ActivityResultLauncher` prevents memory leaks, type-safety issues, and callback fragmentation.
- Launchers must be registered before the Activity state reaches RESUMED during initialization.
build-exercise: Use `registerForActivityResult` with `ActivityResultContracts.TakePicture` or `GetContent` to pick an image and display it.
official-references:
- Get a result from an activity | https://developer.android.com/training/basics/intents/result
common-wrong-beliefs:
- You can register an `ActivityResultLauncher` anywhere inside your click listeners or button callbacks | Launchers must be registered during component initialization before creation completes.

## android-core/intents/deep-links
interview-questions:
- [intern] What is a deep link, and how does it allow users to open specific content inside your app from a web URL?
- [junior] What is the difference between standard deep links and Android App Links (verified domain links)?
- [mid] How do intent filters in the manifest configure deep link handling for URIs?
- [senior] What goes wrong in production when deep links fail to reconstruct the correct back stack, leaving the user trapped on a single screen when pressing back?
what-interviewers-listen-for:
- Deep links map URIs to app screens via intent filters or Navigation Compose deep link builders.
- App Links use Digital Asset Links (`assetlinks.json`) to verify domain ownership securely without browser chooser dialogs.
- Proper deep linking must construct an artificial back stack so the user can navigate upward correctly.
build-exercise: Add a deep link intent filter to an Activity in the manifest, and test firing it via ADB (`adb shell am start -W -a android.intent.action.VIEW -d "app://example.com/details"`).
official-references:
- Android App Links | https://developer.android.com/training/app-links
common-wrong-beliefs:
- Standard deep links automatically prompt the user to verify domain ownership | Standard deep links show a browser chooser dialog if multiple apps match; verified App Links bypass this using assetlinks.json.

## android-core/permissions/why-permissions
interview-questions:
- [intern] Why does Android enforce a runtime permission model for sensitive features like camera, location, and contacts?
- [junior] What is the difference between normal permissions (granted at install) and dangerous permissions (requiring runtime approval)?
- [mid] How does permission sandboxing protect user privacy and device security?
- [senior] What goes wrong in production when assuming permissions remain granted indefinitely without checking revocation in system settings?
what-interviewers-listen-for:
- Runtime permissions protect user privacy by gating sensitive sensors and data behind explicit user consent.
- Users can revoke permissions at any time in system settings; apps must check permissions before every sensitive operation.
build-exercise: Check camera permission status using `ContextCompat.checkSelfPermission` before attempting to access the camera API.
official-references:
- Permissions overview | https://developer.android.com/guide/topics/permissions/overview
common-wrong-beliefs:
- Once a user grants a runtime permission, it stays granted forever until the app is uninstalled | Users can revoke permissions at any time via system settings, requiring apps to check permissions on every resume.

## android-core/permissions/asking-at-runtime
interview-questions:
- [intern] How do you request a runtime permission from the user in Jetpack Compose or Activities?
- [junior] When should you show a rationale explaining why your app needs a specific permission (`shouldShowRequestPermissionRationale`)?
- [mid] What is the UX best practice for timing permission requests (contextual permission requests vs startup prompts)?
- [senior] What goes wrong in production when requesting permissions immediately upon app cold start before establishing user trust?
what-interviewers-listen-for:
- Request permissions contextually when the user initiates a feature requiring them.
- `shouldShowRequestPermissionRationale` helps determine if the user previously denied the request.
build-exercise: Request location permission using `ActivityResultContracts.RequestPermission` and handle granted versus denied outcomes.
official-references:
- Request app permissions | https://developer.android.com/training/permissions/requesting
common-wrong-beliefs:
- Requesting all necessary dangerous permissions simultaneously on cold app launch yields higher grant rates | Users frequently deny permissions requested without immediate context or value.

## android-core/permissions/when-the-user-says-no
interview-questions:
- [intern] What should your app do when the user denies a permission request?
- [junior] What happens when a user selects "Don't ask again" (permanent denial) on a permission dialog?
- [mid] How do you guide the user to system settings when permissions are permanently denied?
- [senior] What goes wrong in production when an app enters an infinite permission request loop after repeated user denials?
what-interviewers-listen-for:
- Gracefully degrade feature functionality when permissions are denied without crashing.
- Permanent denial requires redirecting the user to system app settings via an Intent.
build-exercise: Handle permission denial by showing an educational UI banner explaining feature limitations and providing a link to system settings.
official-references:
- Request app permissions | https://developer.android.com/training/permissions/usage-notes
common-wrong-beliefs:
- You can keep displaying the system permission pop-up dialog repeatedly even after the user selects "Don't ask again" | Once permanently denied, the system suppresses further pop-ups; you must direct the user to system settings.

## android-core/background-work/why-background-is-limited
interview-questions:
- [intern] Why does modern Android heavily restrict background execution and wakelocks when apps are closed?
- [junior] How do Doze mode and App Standby preserve battery life on Android devices?
- [mid] Why are raw background threads or infinite background services insufficient for deferred jobs?
- [senior] What goes wrong in production when apps abuse background alarms or wakelocks, causing rapid battery drain and OEM task killer blacklisting?
what-interviewers-listen-for:
- Aggressive background restrictions save battery and thermal stamina.
- Doze mode groups network and CPU sync tasks into periodic maintenance windows.
- Unconstrained background work gets terminated by the OS or OEM battery management tools.
build-exercise: Put your app in the background, check how CPU and network access are restricted, and review Doze mode documentation.
official-references:
- Optimize for Doze and App Standby | https://developer.android.com/training/monitoring-device-state/doze-standby
common-wrong-beliefs:
- Starting a raw background thread ensures your app can continue syncing data while the phone is locked in your pocket | The OS suspends CPU execution and cuts network access in Doze mode, killing unconstrained background threads.

## android-core/background-work/what-is-workmanager
interview-questions:
- [intern] What is WorkManager, and when should you use it instead of coroutines or services?
- [junior] What is the difference between `OneTimeWorkRequest` and `PeriodicWorkRequest`?
- [mid] How does WorkManager guarantee execution even if the app crashes or the phone reboots?
- [senior] What goes wrong in production when WorkManager tasks perform long-running blocking operations without reporting progress or handling exponential backoff?
what-interviewers-listen-for:
- WorkManager is the recommended persistent background work library for deferred, guaranteed tasks.
- Work requests are stored in an internal SQLite database, surviving app restarts and reboots.
- Exponential backoff handles transient network or server failures gracefully.
build-exercise: Enqueue a OneTimeWorkRequest worker that logs a message, and verify it runs successfully.
official-references:
- WorkManager overview | https://developer.android.com/topic/libraries/architecture/workmanager
common-wrong-beliefs:
- WorkManager is designed for immediate real-time background tasks that must execute the exact millisecond requested | WorkManager is for deferred, guaranteed work; execution timing is managed by the OS based on constraints and system load.

## android-core/background-work/work-constraints
interview-questions:
- [intern] How do you configure WorkManager constraints (e.g., `NetworkType.CONNECTED`, `RequiresCharging`)?
- [junior] Why are constraints crucial for preserving user data caps and battery life?
- [mid] How does WorkManager handle task execution when constraints transition from met to unmet midway?
- [senior] What goes wrong in production when restrictive constraints (like unmetered Wi-Fi plus charging) prevent deferred data sync jobs from ever running on low-usage devices?
what-interviewers-listen-for:
- Constraints ensure work only runs when specific hardware/network conditions are met.
- Unmet constraints pause work automatically until conditions return to valid states.
build-exercise: Create a WorkRequest with `Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresCharging(true).build()` and enqueue it.
official-references:
- WorkManager constraints | https://developer.android.com/topic/libraries/architecture/workmanager/work-states#constraints
common-wrong-beliefs:
- WorkManager constraints are only checked when the work is first enqueued | Constraints are monitored continuously; if constraints fail during execution, WorkManager stops the worker and retries later.

## android-core/background-work/when-work-fails
interview-questions:
- [intern] What happens when background work performed by a WorkManager worker fails?
- [junior] How do you configure retry policies with exponential backoff on a WorkRequest?
- [mid] What is the difference between `Result.success()`, `Result.failure()`, and `Result.retry()`?
- [senior] What goes wrong in production when a worker fails repeatedly without exponential backoff, hammering a struggling server?
what-interviewers-listen-for:
- `Result.retry()` schedules work to run again according to backoff policy.
- Exponential backoff prevents overwhelming servers during network outages.
build-exercise: Write a Worker that returns `Result.retry()` on failure, and configure a backoff criteria policy.
official-references:
- WorkManager retry and backoff | https://developer.android.com/topic/libraries/architecture/workmanager/retries
common-wrong-beliefs:
- Returning `Result.failure()` tells WorkManager to automatically retry the task indefinitely | `Result.failure()` terminates the work request permanently; `Result.retry()` is required to trigger retry logic.

## android-core/services-and-notifications/what-is-a-notification
interview-questions:
- [intern] How do you display a push notification to the user in Android?
- [junior] What are notification channels (introduced in Android 8.0), and why are they mandatory?
- [mid] How do you handle notification permission requests (`POST_NOTIFICATIONS` in Android 13+) and user opt-outs?
- [senior] What goes wrong in production when notification channels lack proper importance levels or users block categories entirely?
what-interviewers-listen-for:
- Notifications alert users to time-sensitive events via NotificationManager.
- Notification channels let users control specific notification categories (promotions vs critical alerts).
- Android 13+ requires requesting the `POST_NOTIFICATIONS` runtime permission.
build-exercise: Create a notification channel, build a NotificationCompat notification, and display it using NotificationManagerCompat.
official-references:
- Notifications overview | https://developer.android.com/develop/ui/views/notifications
common-wrong-beliefs:
- You can post notifications directly without creating a notification channel on modern Android versions | Notification channels are mandatory on Android 8.0 (API 26) and higher; notifications posted without a valid channel are dropped.

## android-core/services-and-notifications/what-is-a-foreground-service
interview-questions:
- [intern] What is a Foreground Service, and why does it display a persistent notification?
- [junior] When should you use a Foreground Service instead of WorkManager or standard background coroutines (e.g., audio playback, active navigation)?
- [mid] What are Foreground Service type requirements (e.g., `mediaPlayback`, `location`) introduced in modern Android versions?
- [senior] What goes wrong in production when starting a Foreground Service from the background on Android 12+ (ForegroundServiceStartNotAllowedException)?
what-interviewers-listen-for:
- Foreground services perform long-running tasks visible to the user via mandatory persistent notifications, preventing OS termination.
- Android 12+ blocks starting foreground services from background apps unless specific exemption criteria are met.
build-exercise: Implement a Foreground Service for audio playback or location tracking, attaching the required foreground service type manifest declaration.
official-references:
- Foreground services overview | https://developer.android.com/guide/components/foreground-services
common-wrong-beliefs:
- You can start a Foreground Service from any background broadcast receiver or worker at any time on modern Android | Android 12+ strictly prohibits starting foreground services from the background, throwing an exception unless specific exceptions apply.

## android-core/services-and-notifications/choosing-background-tool
interview-questions:
- [intern] How do you decide whether a background job should be a coroutine, WorkManager, or a Foreground Service?
- [junior] What criteria separate immediate user-driven tasks from deferred uploads and persistent foreground streams?
- [mid] What are the architectural pitfalls of choosing a Foreground Service for tasks that could easily be deferred via WorkManager?
- [senior] What goes wrong in production when system resource limits kill improperly categorized background tasks?
what-interviewers-listen-for:
- Use coroutines for in-memory UI work; WorkManager for deferred reliable tasks; Foreground Services for user-visible active tasks.
- Misusing Foreground Services drains battery and violates Play Store policy.
build-exercise: Create a decision matrix matching scenario requirements (e.g., log upload, music streaming, database cleanup) to the correct background tool.
official-references:
- Guide to background processing | https://developer.android.com/guide/background
common-wrong-beliefs:
- A Foreground Service is always the safest way to ensure your background network sync job finishes without getting killed | WorkManager is designed for deferred network syncs and uses fewer system resources than a Foreground Service.

## android-core/files-and-sharing/app-private-files
interview-questions:
- [intern] Where can your app save files privately so that no other app or user can read them (`context.filesDir`, `cacheDir`)?
- [junior] What is the difference between internal app-specific storage and shared external storage?
- [mid] Why are internal app files automatically deleted when the user uninstalls the app?
- [senior] What goes wrong in production when storing sensitive unencrypted tokens in app-private files on unencrypted devices?
what-interviewers-listen-for:
- App-private storage (`filesDir`, `cacheDir`) is sandboxed to the app's Linux UID, inaccessible to other apps without root access.
- Cache directory files can be purged by the OS under low storage pressure.
build-exercise: Write a string to `context.filesDir.resolve("test.txt")` and read it back successfully.
official-references:
- Storage options | https://developer.android.com/training/data-storage
common-wrong-beliefs:
- Files saved in `context.filesDir` are encrypted by default on all Android devices | App-private storage is sandboxed, but encryption depends on system-level file-based encryption (FBE), not automatic app-level encryption.

## android-core/files-and-sharing/picking-photos-and-files
interview-questions:
- [intern] How do you let the user pick a photo or file without granting your app broad access to their entire photo gallery?
- [junior] What is the Photo Picker API (introduced in Android 13, backported via Jetpack), and why is it preferred over legacy `READ_EXTERNAL_STORAGE`?
- [mid] How do storage access framework (SAF) intents work for picking arbitrary document files?
- [senior] What goes wrong in production when apps request broad storage permissions unnecessarily, violating Google Play privacy policies?
what-interviewers-listen-for:
- Photo Picker provides a secure system UI letting users select specific photos without granting storage permissions.
- Play Store enforces strict permission policies against broad storage access when Photo Picker suffices.
build-exercise: Launch the Photo Picker using `ActivityResultContracts.PickVisualMedia()` and display the selected image URI.
official-references:
- Photo picker | https://developer.android.com/training/data-storage/shared/photopicker
common-wrong-beliefs:
- Your app needs `READ_EXTERNAL_STORAGE` permission to let a user select a profile picture from their gallery | The system Photo Picker allows users to select photos without requiring any runtime storage permissions.

## android-core/files-and-sharing/sharing-with-other-apps
interview-questions:
- [intern] How do you securely share a file (like a PDF or exported image) with another app using `FileProvider`?
- [junior] Why does passing a `file://` Uri throw a `FileUriExposedException` on modern Android versions?
- [mid] How do content URIs (`content://`) and temporary permission grants (`FLAG_GRANT_READ_URI_PERMISSION`) work?
- [senior] What goes wrong in production when FileProvider paths are misconfigured in `res/xml/provider_paths.xml`, causing security exceptions?
what-interviewers-listen-for:
- `FileProvider` generates secure `content://` URIs with temporary read/write access permissions.
- `file://` URIs expose internal file paths directly, causing `FileUriExposedException`.
build-exercise: Configure a `FileProvider` in the manifest and `provider_paths.xml`, and share a private file via an implicit intent with temporary permission flags.
official-references:
- Share files | https://developer.android.com/training/secure-file-sharing
common-wrong-beliefs:
- You can pass a standard `file://` URI directly in an intent to share a file with another app | Modern Android throws a `FileUriExposedException` when sharing `file://` URIs; you must use `FileProvider` to generate `content://` URIs.

## android-core/app-startup/what-is-the-application-class
interview-questions:
- [intern] What is the `Application` class, and when is its `onCreate()` method called relative to Activities and Services?
- [junior] Why should you avoid heavy blocking initialization inside `Application.onCreate()`?
- [mid] What is the App Startup library, and how does it initialize components lazily or in ordered sequences?
- [senior] What goes wrong in production when multiple SDK initializers execute synchronously in `Application.onCreate()`, delaying cold start time?
what-interviewers-listen-for:
- Application is the base class for maintaining global application state, initialized before any Activity.
- Heavy initialization in Application.onCreate directly delays cold start time and degrades startup vitals.
- Jetpack App Startup unifies library initializers cleanly, avoiding ContentProvider initialization hacks.
build-exercise: Create a custom Application class, log initialization timing in `onCreate()`, and inspect cold start latency.
official-references:
- Application class | https://developer.android.com/reference/android/app/Application
- App Startup library | https://developer.android.com/topic/libraries/architecture/app-startup
common-wrong-beliefs:
- Initializing analytics, databases, and third-party SDKs inside `Application.onCreate()` is best practice because it runs early | Heavy synchronous initialization in Application.onCreate increases cold start latency and harms app performance metrics.

## android-core/app-startup/cold-warm-hot-start
interview-questions:
- [intern] What is the difference between a cold start, a warm start, and a hot start in Android apps?
- [junior] Why does a cold start take significantly more time and system resources than a warm or hot start?
- [mid] How do system metrics (TTID - Time to Initial Display, TTFD - Time to Full Display) measure startup performance?
- [senior] What goes wrong in production when cold start optimization is neglected, leading to poor Play Vitals rankings and user abandonment?
what-interviewers-listen-for:
- Cold start initializes the process and app from scratch; warm start reuses process state while recreating activities; hot start restores the app from memory.
- Metrics like TTID and TTFD track perceived loading speed in production.
build-exercise: Measure cold start timings using ADB (`am start -W`) and analyze startup logs in Logcat.
official-references:
- Understand app startup time | https://developer.android.com/topic/performance/vitals/launch-time
common-wrong-beliefs:
- A warm start requires forking a brand-new Linux process and initializing the Application class from scratch | Warm starts reuse the existing running app process, bypassing process creation and Application initialization.

## android-core/app-startup/from-tap-to-first-frame
interview-questions:
- [intern] What happens step by step between tapping an app icon on the home screen and seeing the first rendered frame?
- [junior] What is the role of the starting window (splash screen) during cold app startup?
- [mid] How does the Android 12+ Core Splash Screen API (`SplashScreen`) manage window transitions smoothly?
- [senior] What goes wrong in production when splash screen animations block waiting for slow network calls before showing the UI?
what-interviewers-listen-for:
- Steps: Launcher intent -> OS process fork -> Application creation -> Activity creation -> Layout inflation -> Choreographer frame traversal.
- Core SplashScreen API standardizes launch transitions, preventing custom sluggish XML splash screens.
build-exercise: Implement the Android 12+ SplashScreen API theme in your app, set an exit animation listener, and verify smooth startup transitions.
official-references:
- Splash screen API | https://developer.android.com/develop/ui/views/launch/splash-screen
common-wrong-beliefs:
- Creating a custom full-screen Activity acting as a splash screen is the modern recommended way to brand app launches | Custom splash activities cause slow cold starts and duplicate window rendering; use the official Android 12+ Core SplashScreen API instead.

## Self-check
1. Configure your build | https://developer.android.com/build (Verify URL structure)
2. Processes and app lifecycle | https://developer.android.com/guide/components/activities/process-lifecycle (Verify URL structure)
3. Introduction to activities | https://developer.android.com/guide/components/activities/intro-activities (Verify URL structure)
4. Context | https://developer.android.com/reference/android/content/Context (Verify reference URL)
5. Tasks and back stack | https://developer.android.com/guide/components/activities/tasks-and-back-stack (Verify URL structure)
6. Activity lifecycle | https://developer.android.com/guide/components/activities/activity-lifecycle (Verify URL structure)
7. Lifecycle-aware components | https://developer.android.com/topic/libraries/architecture/lifecycle (Verify URL structure)
8. Investigate memory usage with Memory Profiler | https://developer.android.com/studio/profile/memory-profiler (Verify URL structure)
9. Handle configuration changes | https://developer.android.com/guide/topics/resources/runtime-changes (Verify URL structure)
10. WorkManager overview | https://developer.android.com/topic/libraries/architecture/workmanager (Verify URL structure)