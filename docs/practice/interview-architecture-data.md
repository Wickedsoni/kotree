# interview: architecture-data

## architecture-data/why-architecture/why-code-needs-structure
interview-questions:
- [intern] Why does putting UI rendering, business rules, and database queries inside a single Activity or Composable file become hard to maintain?
- [junior] What does "separation of concerns" mean in an Android app?
- [mid] What is the trade-off between adding architectural layers (UI, Domain, Data) in a tiny 2-screen prototype versus a multi-team production app?
- [senior] What goes wrong in production when UI code directly mutates database entities or parses raw network responses?
what-interviewers-listen-for:
- Separation of concerns isolates failure modes, enables unit testing on the JVM without an emulator, and prevents lifecycle recreation from cancelling data operations.
- Over-engineering adds boilerplate in trivial apps, while under-structuring couples UI lifecycle to IO and makes changes ripple across the entire codebase.
build-exercise: Take a single Composable that fetches JSON and calculates a discount inside `LaunchedEffect`, extract the calculation and data fetch into a plain Kotlin class, and write a fast JVM unit test for it.
official-references:
- Guide to app architecture | https://developer.android.com/topic/architecture
common-wrong-beliefs:
- App architecture is only about picking acronyms like MVVM or MVI | Architecture is about clear boundaries, dependency direction, and lifecycle ownership so code is testable and resilient.

## architecture-data/why-architecture/three-jobs-in-an-app
interview-questions:
- [intern] What are the distinct responsibilities of the UI layer, Domain layer, and Data layer in Google's recommended Android architecture?
- [junior] In which direction do dependencies point between the UI layer and the Data layer?
- [mid] Why is the Domain (UseCase) layer optional in Android architecture, and when does adding it genuinely pay off?
- [senior] What goes wrong in production when every single repository method has a 1-to-1 pass-through UseCase class that does nothing?
what-interviewers-listen-for:
- UI layer renders state and dispatches user events; Domain layer encapsulates reusable or multi-repository business logic; Data layer owns sources of truth and persistence.
- Dependencies flow strictly inward/downward (UI -> Domain -> Data); pass-through UseCases that only forward a single call add boilerplate without value.
build-exercise: Draw the dependency graph of a screen that displays a user's profile and recent orders, placing UI state in the ViewModel, combination logic in a UseCase, and network/DB calls in Repositories.
official-references:
- Guide to app architecture | https://developer.android.com/topic/architecture
- Domain layer | https://developer.android.com/topic/architecture/domain-layer
common-wrong-beliefs:
- Every Android project must have a UseCase class for every Repository function | The Domain layer is only needed when business logic is complex or reused across multiple ViewModels.

## architecture-data/viewmodel/what-is-a-viewmodel
interview-questions:
- [intern] What problem does Jetpack `ViewModel` solve that a plain variable inside a Composable or Activity cannot?
- [junior] What are the two main responsibilities of a `ViewModel` in the UI layer?
- [mid] When should state live inside a plain Compose state holder class (`remember { ... }`) instead of an `androidx.lifecycle.ViewModel`?
- [senior] What goes wrong in production when a single shared `activityViewModels()` instance accumulates state for 20 unrelated screens?
what-interviewers-listen-for:
- `ViewModel` holds screen-level business state across configuration changes and provides `viewModelScope` for lifecycle-bound asynchronous work.
- Ephemeral UI-element state (scroll position, animation state) belongs in a plain UI state holder, while screen business state belongs in a `ViewModel` scoped to the navigation destination.
build-exercise: Compare a screen state held in `remember` vs a `ViewModel` when rotating the emulator and when navigating forward to a detail screen and pressing Back.
official-references:
- ViewModel overview | https://developer.android.com/topic/libraries/architecture/viewmodel
- State holders and UI State | https://developer.android.com/topic/architecture/ui-layer/stateholders
common-wrong-beliefs:
- Every Composable widget needs its own `ViewModel` | `ViewModel` is for screen-level or navigation-destination state; reusable UI components should be stateless or use plain `remember` state holders.

## architecture-data/viewmodel/viewmodel-lifetime
interview-questions:
- [intern] When is a `ViewModel` created, and when is its `onCleared()` method called?
- [junior] How does `ViewModelStoreOwner` determine how long a `ViewModel` stays alive in an Activity vs a Compose Navigation `NavBackStackEntry`?
- [mid] How does `ViewModelProvider` retain the same `ViewModel` instance across an Activity recreation during rotation?
- [senior] What goes wrong in production when a `ViewModel` is accidentally scoped to the host `Activity` instead of the `NavBackStackEntry`?
what-interviewers-listen-for:
- A `ViewModel` lives in a `ViewModelStore` owned by a `ViewModelStoreOwner` (Activity, Fragment, or `NavBackStackEntry`) and calls `onCleared()` only when that owner finishes permanently (is popped from the back stack).
- Scoping a screen's ViewModel to the Activity keeps old screen state and memory alive for the entire app session even after the user navigates away.
build-exercise: Override `onCleared()` with a log statement in a ViewModel scoped to a Compose Navigation route. Rotate the screen (no log), navigate forward (no log), then press Back to pop the route and observe `onCleared()` run.
official-references:
- ViewModel overview | https://developer.android.com/topic/libraries/architecture/viewmodel
common-wrong-beliefs:
- `onCleared()` runs every time the screen rotates | Rotation destroys the Activity instance, not the `ViewModelStore`; `onCleared()` only runs when the screen permanently leaves the back stack.

## architecture-data/viewmodel/keep-the-screen-out-of-the-viewmodel
interview-questions:
- [intern] Why must a `ViewModel` never hold a reference to an `Activity`, `View`, or Composable lambda that captures UI context?
- [junior] Why does holding Android framework UI classes inside a `ViewModel` make unit testing harder?
- [mid] If a `ViewModel` needs a localized error message, why is exposing a string resource ID or semantic error enum better than injecting `Context`?
- [senior] What goes wrong in production when a `ViewModel` holds a listener or callback referencing the old `Activity` across a rotation?
what-interviewers-listen-for:
- `ViewModel` outlives the `Activity` instance across configuration changes; holding any `Activity` or `View` reference leaks the entire destroyed UI hierarchy.
- Keeping `android.*` UI dependencies out of `ViewModel` allows fast JVM unit tests without Robolectric and lets the UI resolve localized strings dynamically when the user switches languages.
build-exercise: Pass a sealed error state (`ErrorState.NetworkTimeout`) from a `ViewModel` and resolve `stringResource(R.string.network_timeout)` inside the Composable, then switch device language and verify the message updates immediately.
official-references:
- ViewModel overview | https://developer.android.com/topic/libraries/architecture/viewmodel
common-wrong-beliefs:
- Resolving strings with `applicationContext.getString(...)` inside `ViewModel.init` is always safe | If the user changes the device or app language, strings cached in `ViewModel.init` stay stuck in the old language.

## architecture-data/viewmodel/viewmodel-and-process-death
interview-questions:
- [intern] What happens to the properties inside a `ViewModel` when Android kills your backgrounded app process?
- [junior] How does `SavedStateHandle` inside a `ViewModel` preserve small values like a search query or route argument across process death?
- [mid] What is the difference between storing full list data in `SavedStateHandle` versus storing only the filter/query keys and re-reading the list from Room?
- [senior] What goes wrong in production when a `ViewModel` reads navigation arguments from a custom init method instead of `SavedStateHandle`?
what-interviewers-listen-for:
- `SavedStateHandle` automatically reads navigation route arguments and persists small key-value state (`getStateFlow`) into the system saved-state `Bundle`.
- Only small state identifiers (IDs, search queries, filter tabs) belong in `SavedStateHandle`; actual domain datasets should reload from the persistent Data layer (Room/DataStore).
build-exercise: Use `savedStateHandle.getStateFlow("query", "")` in a `ViewModel` to back a search field. Type a query, background the app, run `adb shell am kill <package>`, reopen from Recents, and verify the query is still there.
official-references:
- Saved State module for ViewModel | https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
- Save UI states | https://developer.android.com/topic/libraries/architecture/saving-states
common-wrong-beliefs:
- `SavedStateHandle` is meant to store entire API responses so you don't refetch after process death | `SavedStateHandle` writes to the Binder saved-state `Bundle` (~1 MB shared limit) and should only store lightweight UI state and IDs.

## architecture-data/ui-state-and-udf/what-is-ui-state
interview-questions:
- [intern] What is UI state, and why is it modeled as an immutable Kotlin `data class` or `sealed interface`?
- [junior] What is the advantage of exposing a single `uiState: StateFlow<ScreenUiState>` from a ViewModel instead of 8 separate `StateFlow` properties?
- [mid] What is the trade-off between a single `data class` with flags (`isLoading`, `items`, `error`) versus a `sealed interface` (`Loading`, `Success`, `Error`)?
- [senior] What goes wrong in production when a screen exposes separate `isLoading`, `isError`, and `data` streams that update asynchronously out of order?
what-interviewers-listen-for:
- Immutability guarantees a consistent snapshot of the screen at any point in time and works cleanly with Compose smart recomposition.
- Multiple independent streams can emit intermediate illegal combinations (e.g., `isLoading = true` AND `isError = true` simultaneously), causing UI flicker.
build-exercise: Refactor a ViewModel with three separate `MutableStateFlow` properties (`loading`, `items`, `error`) into a single immutable `UiState` exposed as one `StateFlow`, and verify atomic updates with `.update { ... }`.
official-references:
- UI layer | https://developer.android.com/topic/architecture/ui-layer
common-wrong-beliefs:
- Emitting a new `UiState` data class causes the entire Compose screen to recompose from scratch | Compose compares individual fields read by each child Composable and skips any subtree whose inputs did not change.

## architecture-data/ui-state-and-udf/loading-success-error
interview-questions:
- [intern] How do you model `Loading`, `Success`, and `Error` states in Kotlin so the compiler forces the UI to handle all three?
- [junior] When the user pulls to refresh a screen that already has data on screen, why does a strict `Loading | Success | Error` sealed interface wipe out the visible list?
- [mid] How do you design a UI state model that supports both initial full-screen loading/error states AND refreshing/error banners while showing cached data?
- [senior] What goes wrong in production when a background refresh failure replaces a user's cached offline feed with a full-screen error page?
what-interviewers-listen-for:
- Sealed interfaces (`sealed interface FeedUiState`) enforce exhaustive `when` branches for mutually exclusive screen states.
- When cached data can coexist with a refresh indicator or transient error, embedding `isRefreshing: Boolean` or `errorMessage: String?` inside the `Success` / data state avoids wiping visible content.
build-exercise: Build a screen with a `sealed interface` (`Loading`, `EmptyError`, `Content(val items: List<String>, val isRefreshing: Boolean)`), trigger a refresh failure while items are visible, and confirm the list stays on screen.
official-references:
- UI layer | https://developer.android.com/topic/architecture/ui-layer
common-wrong-beliefs:
- `Loading`, `Success`, and `Error` are always mutually exclusive in every screen | In offline-first or pull-to-refresh screens, `Success` data is often displayed simultaneously with a loading indicator or error banner.

## architecture-data/ui-state-and-udf/state-down-events-up
interview-questions:
- [intern] What does "state flows down, events flow up" mean in Unidirectional Data Flow (UDF)?
- [junior] Walk through the exact loop when a user taps a "Bookmark" button in a UDF architecture.
- [mid] Why does UDF forbid a Composable from immediately toggling its own local boolean when a bookmark button is tapped if the source of truth is in the repository?
- [senior] What goes wrong in production when two parts of a screen mutate local copies of state instead of routing events through a single source of truth?
what-interviewers-listen-for:
- UDF cycle: UI sends user event up to ViewModel -> ViewModel updates data layer -> Data layer emits new state -> ViewModel transforms to `UiState` flowing down to UI.
- Single direction of state mutation eliminates state desynchronization bugs, makes bugs reproducible from state snapshots, and simplifies testing.
build-exercise: Trace and log the 4 steps of a "Like" button tap: (1) UI calls `onLikeClick(id)`, (2) ViewModel calls repository, (3) Repository updates Flow, (4) UI recomposes from the new `UiState`.
official-references:
- UI layer | https://developer.android.com/topic/architecture/ui-layer
- State and Jetpack Compose | https://developer.android.com/develop/ui/compose/state
common-wrong-beliefs:
- Unidirectional Data Flow is a library you have to add to `build.gradle.kts` | UDF is an architectural pattern built directly with Kotlin `StateFlow` and Compose state callbacks.

## architecture-data/ui-state-and-udf/expose-state-not-mutable-state
interview-questions:
- [intern] Why does a ViewModel declare `private val _uiState = MutableStateFlow(...)` and expose `val uiState: StateFlow<...> = _uiState.asStateFlow()`?
- [junior] What is the difference between upcasting `: StateFlow<T>` versus calling `.asStateFlow()`?
- [mid] Why should you update a `MutableStateFlow` using `_uiState.update { it.copy(...) }` instead of `_uiState.value = _uiState.value.copy(...)`?
- [senior] What goes wrong in production when two coroutines concurrently assign `_uiState.value = _uiState.value.copy(...)` without `.update {}`?
what-interviewers-listen-for:
- `.asStateFlow()` wraps the `MutableStateFlow` in a read-only `ReadOnlyStateFlow` so callers cannot cast it back (`as MutableStateFlow`) and mutate state from the UI.
- `_uiState.update { ... }` uses an atomic compare-and-set loop, preventing lost updates when multiple coroutines modify different fields of `UiState` concurrently.
build-exercise: Launch two coroutines that each update a different field of `_uiState` 1,000 times using `.value = _uiState.value.copy(...)` vs `.update { it.copy(...) }`, and assert whether any updates are lost.
official-references:
- StateFlow and SharedFlow | https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
common-wrong-beliefs:
- Declaring `val uiState: StateFlow<UiState> = _uiState` prevents callers from mutating it | Callers can still do `(viewModel.uiState as MutableStateFlow).value = ...` unless `.asStateFlow()` is used to wrap it.

## architecture-data/repositories/what-is-a-repository
interview-questions:
- [intern] What is a Repository in Android architecture, and why shouldn't a ViewModel call Retrofit or Room DAOs directly?
- [junior] How does a Repository coordinate between a remote network data source and a local database data source?
- [mid] What is the trade-off of having one gigantic `AppRepository` versus one Repository per domain aggregate (e.g., `UserRepository`, `OrderRepository`)?
- [senior] What goes wrong in production when two ViewModels bypass a shared Repository and cache their own separate copies of the same user state?
what-interviewers-listen-for:
- Repositories abstract data sources (network, Room, DataStore, memory cache), centralize data mapping, and enforce a single source of truth.
- Splitting repositories by domain aggregate keeps classes cohesive and allows sharing data across screens without creating a monolithic god repository.
build-exercise: Create a `NotesRepository` that holds an in-memory `MutableStateFlow<List<Note>>`, inject it into both a `NoteListViewModel` and an `AddNoteViewModel`, and observe how adding a note immediately updates the list screen.
official-references:
- Data layer | https://developer.android.com/topic/architecture/data-layer
common-wrong-beliefs:
- A Repository should contain UI formatting logic like turning a timestamp into `"2 hours ago"` | Repositories expose domain models; UI formatting belongs in the ViewModel or UI layer.

## architecture-data/repositories/repository-interface-and-fakes
interview-questions:
- [intern] How does defining a `UserRepository` interface let you swap real network calls for fake data in tests and Compose Previews?
- [junior] How does a `FakeUserRepository` differ from a Mockito/MockK mock in a unit test?
- [mid] Why does Google's Android testing guidance prefer writing stateful Fake repositories over mocking every repository method call?
- [senior] What goes wrong in production test suites when tests rely heavily on brittle verification mocks (`verify { repo.save(any()) }`) instead of asserting observable state changes on a Fake?
what-interviewers-listen-for:
- A Fake is a lightweight working implementation (e.g., backed by a `MutableStateFlow` or `MutableList`) that behaves realistically under state transitions.
- Fakes test actual behavior and survive internal refactoring, whereas interaction mocks couple tests tightly to implementation details.
build-exercise: Extract a `TasksRepository` interface, implement a `FakeTasksRepository` backed by a `MutableStateFlow<List<Task>>`, and use it to test `TasksViewModel` in under 50ms on the JVM.
official-references:
- Use test doubles in Android | https://developer.android.com/training/testing/fundamentals/test-doubles
common-wrong-beliefs:
- You need a mocking library to test a ViewModel that depends on a Repository | A simple hand-written Fake implementing the Repository interface with a `MutableStateFlow` is cleaner, faster, and catches more bugs.

## architecture-data/dependency-injection/what-is-a-dependency
interview-questions:
- [intern] If `ProfileViewModel` creates `val repo = UserRepository(RetrofitClient.api)` inside its body, why is it hard to test?
- [junior] What is Inversion of Control (IoC) when managing class dependencies?
- [mid] Why is hidden global state (`ObjectSingleton.getInstance()`) considered an anti-pattern compared to explicit dependencies?
- [senior] What goes wrong in production when classes construct their own `OkHttpClient`, `RoomDatabase`, or `CoroutineScope` internally instead of receiving shared instances?
what-interviewers-listen-for:
- Hardcoded instantiation tightly couples a class to concrete production implementations, making it impossible to substitute test doubles or share expensive resources.
- Passing dependencies from the outside makes a class's requirements explicit in its signature and lets the caller control object lifetimes.
build-exercise: Take a class that instantiates `System.currentTimeMillis()` or a network client internally, refactor it to accept a `Clock` or `Api` interface parameter, and write a deterministic unit test.
official-references:
- Dependency injection in Android | https://developer.android.com/training/dependency-injection
common-wrong-beliefs:
- Using Kotlin `object` singletons everywhere is just as testable as Dependency Injection | Global `object` singletons retain state across tests, causing test order pollution and preventing parallel test execution.

## architecture-data/dependency-injection/constructor-injection
interview-questions:
- [intern] What is constructor injection, and how do you write it in Kotlin?
- [junior] Why is constructor injection preferred over field (`lateinit var`) injection whenever possible?
- [mid] Why can't you use constructor injection directly on an Android `Activity` or `Service` without framework support?
- [senior] What goes wrong in production when a class uses field injection (`@Inject lateinit var`) and a method is called before the injection framework initializes the fields?
what-interviewers-listen-for:
- Constructor injection allows properties to be `private val` (immutable and non-null) and guarantees the object cannot be constructed in an incomplete state.
- The Android OS instantiates `Activity` and `Service` classes via no-arg reflection, requiring entry-point field injection or factory hooks (`ViewModelProvider.Factory`).
build-exercise: Wire a manual `AppContainer` class in `Application` that constructs a `Repository` and passes it via constructor injection into a `ViewModelProvider.Factory`.
official-references:
- Dependency injection in Android | https://developer.android.com/training/dependency-injection
- Manual dependency injection | https://developer.android.com/training/dependency-injection/manual
common-wrong-beliefs:
- Dependency injection requires a third-party library like Hilt or Koin | Constructor injection and a manual container class are pure Kotlin and require zero libraries.

## architecture-data/dependency-injection/what-a-di-library-does
interview-questions:
- [intern] What tedious boilerplate does a DI framework like Hilt or Koin automate for you?
- [junior] What is the difference between `@Inject constructor(...)` and an `@Provides` / `@Binds` module function in Hilt?
- [mid] What is the trade-off between compile-time DI code generation (Dagger/Hilt/kotlin-inject) and runtime service locator/DSL resolution (Koin)?
- [senior] What goes wrong in production when a runtime DI container is missing a binding for a rarely visited screen and no dependency graph verification test exists?
what-interviewers-listen-for:
- DI libraries build the transitive dependency graph, manage scope lifetimes, and integrate with `ViewModel` and Navigation factories automatically.
- Compile-time DI (Hilt) fails the build immediately if a binding is missing or cyclical, whereas runtime DSLs (Koin) have faster build times and simpler KMP setup but require `checkModules()` tests to catch missing bindings before runtime.
build-exercise: Annotate a Repository with `@Inject constructor()`, a ViewModel with `@HiltViewModel @Inject constructor(...)`, and an Activity with `@AndroidEntryPoint`, then inspect the generated factory classes in `build/generated/`.
official-references:
- Dependency injection with Hilt | https://developer.android.com/training/dependency-injection/hilt-android
common-wrong-beliefs:
- Hilt uses slow runtime reflection to inject dependencies on Android | Hilt/Dagger generates plain factory code at compile time with zero runtime reflection overhead.

## architecture-data/dependency-injection/singletons-and-scopes-in-di
interview-questions:
- [intern] If a class has `@Inject constructor()` in Hilt WITHOUT `@Singleton`, do two ViewModels receive the same instance or two different instances?
- [junior] When should a dependency be scoped as `@Singleton` versus `@ViewModelScoped` or unscoped?
- [mid] Why does scoping every class as `@Singleton` hurt memory usage and cause stale-state bugs?
- [senior] What goes wrong in production when a `@Singleton` class injects a narrower-scoped dependency (or captures user-session state that isn't cleared on logout)?
what-interviewers-listen-for:
- Unscoped bindings create a new instance at every injection site; scoped bindings (`@Singleton`, `@ActivityRetainedScoped`, `@ViewModelScoped`) cache a single instance within that component's lifetime.
- `@Singleton` objects live in RAM until process death—only stateless expensive clients (`OkHttpClient`, `RoomDatabase`) or app-wide state holders belong in `@Singleton`.
build-exercise: Create a counter class with `@Inject constructor()`. Inject it into two screens first without `@Singleton` and then with `@Singleton`, and observe whether increments on Screen A appear on Screen B.
official-references:
- Dependency injection with Hilt | https://developer.android.com/training/dependency-injection/hilt-android
common-wrong-beliefs:
- Adding `@Inject constructor` makes a class a singleton by default | In Hilt/Dagger, bindings are unscoped by default and create a fresh instance every time they are injected unless annotated with a scope like `@Singleton`.

## architecture-data/key-value-storage/store-a-setting-with-datastore
interview-questions:
- [intern] What is Jetpack DataStore, and what kind of data is it designed to store?
- [junior] What is the difference between Preferences DataStore and Proto DataStore?
- [mid] Why is Jetpack DataStore preferred over `SharedPreferences` in modern Kotlin/coroutines apps?
- [senior] What goes wrong in production if you instantiate two `PreferenceDataStoreFactory` instances pointing to the same `.preferences_pb` file in the same process?
what-interviewers-listen-for:
- DataStore exposes data as a cold `Flow` on `Dispatchers.IO`, performs atomic transactional updates via `edit { }`, and never blocks the main thread.
- DataStore enforces a strict single-instance-per-file rule; creating two instances for the same file throws an `IllegalStateException` to prevent data corruption.
build-exercise: Create a singleton `Context.dataStore by preferencesDataStore("settings")`, write a dark-mode boolean inside `dataStore.edit { }`, and collect `dataStore.data.map { ... }` as a `StateFlow` in a ViewModel.
official-references:
- App Architecture: Data Layer - DataStore | https://developer.android.com/topic/libraries/architecture/datastore
common-wrong-beliefs:
- DataStore is a drop-in replacement for relational databases like Room | DataStore reads and writes the whole preferences file atomically and is only for small key-value datasets or typed config objects, not large queryable lists.

## architecture-data/key-value-storage/reading-old-sharedpreferences-code
interview-questions:
- [intern] In legacy `SharedPreferences` code, what is the difference between `.commit()` and `.apply()`?
- [junior] Why can calling `SharedPreferences.apply()` still cause an ANR when the user navigates between Activities or presses Home?
- [mid] How do you migrate an existing app's `SharedPreferences` file into Jetpack DataStore without losing users' saved settings?
- [senior] What goes wrong in production when an app stores a `MutableSet<String>` in `SharedPreferences`, mutates the returned set in place, and saves it back?
what-interviewers-listen-for:
- `.commit()` blocks the calling thread on disk write; `.apply()` updates memory immediately and schedules an async disk write—but `ActivityThread` blocks `onStop` waiting for queued `apply()` writes (`QueuedWork.waitToFinish()`), causing ANRs.
- `SharedPreferencesMigrations` moves legacy XML keys into DataStore automatically on first access and deletes the old XML file.
build-exercise: Create a `SharedPreferences` XML entry, configure `PreferenceDataStoreFactory` with `SharedPreferencesMigration(context, "legacy_prefs")`, read the migrated value from DataStore, and check Device Explorer to confirm the old XML file was migrated.
official-references:
- App Architecture: Data Layer - DataStore | https://developer.android.com/topic/libraries/architecture/datastore
- Save simple data with SharedPreferences | https://developer.android.com/training/data-storage/shared-preferences
common-wrong-beliefs:
- `SharedPreferences.apply()` never blocks the main thread | Android's `QueuedWork` forces the main thread to wait for pending `apply()` disk writes during `Activity.onStop()` and `Service.onStop()`, which is a notorious source of production ANRs.

## architecture-data/databases/rows-tables-and-keys
interview-questions:
- [intern] What is a primary key in a relational database table, and why must every row have one?
- [junior] What is the difference between an auto-generated local integer primary key and a server-provided UUID/string ID?
- [mid] How does adding an index (`@Index`) on a column speed up `SELECT ... WHERE` queries, and what is the trade-off on `INSERT`/`UPDATE`?
- [senior] What goes wrong in production when an app uses `@PrimaryKey(autoGenerate = true)` for items synced from a server and runs `@Upsert` on network refresh?
what-interviewers-listen-for:
- Primary keys uniquely identify rows (backed by a B-tree index in SQLite) and enable deterministic updates/upserts.
- Secondary indexes turn $O(N)$ full-table scans into $O(\log N)$ B-tree lookups, at the cost of extra disk space and B-tree maintenance on writes.
build-exercise: Create a Room `@Entity` using a server `id: String` as `@PrimaryKey`, insert the same ID twice using `@Upsert`, and verify the row updates in place instead of duplicating.
official-references:
- Save data in a local database using Room | https://developer.android.com/training/data-storage/room
- Define data using Room entities | https://developer.android.com/training/data-storage/room/defining-data
common-wrong-beliefs:
- Every Room table should use `@PrimaryKey(autoGenerate = true)` | If an entity comes from a backend with its own stable ID, using `autoGenerate = true` breaks `@Upsert` and creates duplicate rows on every sync.

## architecture-data/databases/sql-basics
interview-questions:
- [intern] What do `SELECT`, `WHERE`, `ORDER BY`, and `LIMIT` each do in a SQL query?
- [junior] Why should you always pass parameters with `:param` placeholders in `@Query` instead of building a SQL string with Kotlin interpolation?
- [mid] What is the difference between `COUNT(*)`, `GROUP BY`, and filtering rows in Kotlin after loading the whole table into memory?
- [senior] What goes wrong in production when an app loads 50,000 rows from SQLite just to call `.filter { ... }.take(20)` in Kotlin?
what-interviewers-listen-for:
- SQLite executes filtering, sorting, and pagination natively in C using indexes before allocating any JVM/Kotlin objects.
- Bound parameters (`WHERE title LIKE :query`) prevent SQL injection and allow SQLite to cache compiled `sqlite3_stmt` query plans.
build-exercise: Write a Room `@Query("SELECT * FROM tasks WHERE isCompleted = :done ORDER BY createdAt DESC LIMIT :limit")` and verify in Android Studio's App Inspection Database Inspector how fast SQLite filters 10,000 rows.
official-references:
- Accessing data using Room DAOs | https://developer.android.com/training/data-storage/room/accessing-data
- Debug your database with the Database Inspector | https://developer.android.com/studio/inspect/database
common-wrong-beliefs:
- Filtering a list in Kotlin with `.filter {}` is just as fast as a SQL `WHERE` clause | `.filter {}` in Kotlin requires reading every row from disk and allocating thousands of JVM objects before discarding them.

## architecture-data/databases/room-entities-and-daos
interview-questions:
- [intern] What are the three core components of a Room database (`@Database`, `@Entity`, `@Dao`)?
- [junior] How does Room validate your SQL queries before you even run the app on a phone?
- [mid] Why should `Room.databaseBuilder(...)` be created once as a singleton, and what happens during a schema version upgrade (`Migration`)?
- [senior] What goes wrong in production if you bump `@Database(version = 2)` after adding a column and ship with `.fallbackToDestructiveMigration()`?
what-interviewers-listen-for:
- Room's KSP annotation processor verifies SQL syntax and table/column names at compile time and generates the DAO and `RoomDatabase` implementations.
- `.fallbackToDestructiveMigration()` drops and recreates all tables when no `Migration` path matches, silently wiping all local user data in production.
build-exercise: Create a Room database at `version = 1`, insert a row, then add a new column to the `@Entity`, bump to `version = 2`, write an `AutoMigration` (or manual `Migration(1, 2)`), and verify the existing row survives.
official-references:
- Save data in a local database using Room | https://developer.android.com/training/data-storage/room
- Migrate your Room database | https://developer.android.com/training/data-storage/room/migrating-db-versions
common-wrong-beliefs:
- Changing a field in an `@Entity` class automatically updates the SQLite table on existing users' phones | You must increment the `@Database` version and supply an `AutoMigration` or `Migration`, or Room throws an `IllegalStateException` on startup.

## architecture-data/databases/room-and-flow
interview-questions:
- [intern] If a Room DAO function returns `Flow<List<Task>>`, why shouldn't you also mark that function with the `suspend` keyword?
- [junior] How does Room know when to emit a new list to a `Flow` collector after an `@Insert` or `@Update` happens elsewhere?
- [mid] Why does Room's `InvalidationTracker` emit at the table level rather than row level, and how can `distinctUntilChanged()` prevent unnecessary UI updates?
- [senior] What goes wrong in production when a DAO wraps a 500-row batch insert in a loop of single `@Insert` calls while the UI is collecting a `Flow` on that table?
what-interviewers-listen-for:
- Room's `InvalidationTracker` monitors modified tables via SQLite triggers/transaction hooks and re-executes active observable queries on a background executor whenever the table changes.
- Inserting 500 items one by one triggers 500 separate transactions and 500 re-queries; passing `List<Entity>` to a single `@Insert` or wrapping in `@Transaction` emits only once after commit.
build-exercise: Observe a `Flow<List<Item>>` from a Room DAO while inserting 100 items one-by-one in a loop vs passing all 100 as a `List<Item>` to a single `@Upsert` method, and count how many times the Flow emits.
official-references:
- Write asynchronous DAO queries | https://developer.android.com/training/data-storage/room/async-queries
common-wrong-beliefs:
- Room's `Flow<T>` emits only the single row that changed | Room's `InvalidationTracker` tracks dirty tables, so any write to the `tasks` table re-runs active `SELECT` queries watching `tasks`; use `.distinctUntilChanged()` on single-row queries.

## architecture-data/databases/relations-between-tables
interview-questions:
- [intern] How do you model a one-to-many relationship (like one `User` with many `Order`s) across two Room tables?
- [junior] What do `@Embedded` and `@Relation` do in a Room intermediate data class, and why does the DAO query need `@Transaction`?
- [mid] What is the trade-off between storing a child list using a `@TypeConverter` (JSON string column) versus a normalized child table with `@ForeignKey`?
- [senior] What goes wrong in production if you query a `@Relation` without `@Transaction` while another thread is updating the child table?
what-interviewers-listen-for:
- Room fetches `@Relation` data by running two separate `SELECT` queries under the hood; `@Transaction` ensures both queries read a consistent snapshot.
- `@ForeignKey(onDelete = ForeignKey.CASCADE)` preserves relational integrity and lets SQLite query/index child items individually, whereas a JSON `@TypeConverter` blob cannot be indexed or partially updated.
build-exercise: Create `AuthorEntity` and `BookEntity` (with a foreign key `authorId`), build an `AuthorWithBooks` class using `@Embedded` and `@Relation`, and query it with a `@Transaction @Query` method.
official-references:
- Define relationships between objects | https://developer.android.com/training/data-storage/room/relationships
common-wrong-beliefs:
- Room `@Entity` classes can directly hold `@Relation` lists | Room forbids `@Relation` inside an `@Entity` to prevent hidden lazy-loading on the main thread; `@Relation` is used on a plain POJO/data class returned by a DAO query.

## architecture-data/offline-first/single-source-of-truth
interview-questions:
- [intern] In an offline-first app, when data exists both on the server and in the local Room database, which one should the UI observe?
- [junior] Walk through how a network refresh updates the screen when Room is the Single Source of Truth (SSOT).
- [mid] What is the trade-off between making Room the Single Source of Truth versus returning network responses directly to the ViewModel?
- [senior] What goes wrong in production when a screen displays data from a network call directly while another screen updates the local database for the same item?
what-interviewers-listen-for:
- The UI exclusively collects `Flow`s from the local database (SSOT); network responses are never returned directly to the UI—they are written into Room, which automatically triggers a new `Flow` emission.
- SSOT guarantees consistency across all screens and offline availability, at the cost of maintaining local Room entities and schema migrations.
build-exercise: Build a Repository where `observeItems(): Flow<List<Item>>` reads only from `dao.observeAll()` and `suspend fun refresh()` fetches from an API and calls `dao.upsertAll(items)`.
official-references:
- Build an offline-first app | https://developer.android.com/topic/architecture/data-layer/offline-first
common-wrong-beliefs:
- In an offline-first app, the Repository returns network data when online and only reads the database when offline | The UI always reads from the local database; being online simply means a background refresh updates that database.

## architecture-data/offline-first/cache-then-refresh
interview-questions:
- [intern] Why does an offline-first screen show cached data from disk immediately and then update a moment later when the network responds?
- [junior] How do you expose both the cached Room `Flow` and the status of the ongoing network refresh (`isRefreshing` / `syncError`) to the UI?
- [mid] How do you avoid wiping out or flickering the user's cached Room data when refreshing a table from the network (`@Transaction` delete-and-insert vs `@Upsert`)?
- [senior] What goes wrong in production when a refresh function calls `dao.deleteAll()` followed by `dao.insertAll()` without wrapping both inside a single `@Transaction`?
what-interviewers-listen-for:
- Combining the Room data `Flow` with a refresh state `Flow` lets the screen show instant cached content alongside a non-intrusive loading indicator or error snackbar.
- Calling `deleteAll()` then `insertAll()` outside a `@Transaction` causes Room's `Flow` to emit `emptyList()` in between, making the screen flash blank on every refresh.
build-exercise: Write a DAO refresh method that calls `deleteAll()` and `insertAll(items)` with and without `@Transaction`, and observe in the UI how omitting `@Transaction` emits an intermediate empty list.
official-references:
- Build an offline-first app | https://developer.android.com/topic/architecture/data-layer/offline-first
common-wrong-beliefs:
- If the network refresh fails, the repository should emit an error that replaces the cached list | When cached data exists, network refresh errors should surface as a transient banner/snackbar while keeping the cached list visible.

## architecture-data/offline-first/offline-writes
interview-questions:
- [intern] What should happen when a user creates or edits a note while their phone has no internet connection?
- [junior] What is the difference between optimistic local updates (writing to Room immediately) and waiting for the server before updating the UI?
- [mid] How do you handle rolling back or flagging an optimistic write if the server later rejects it with a `400` or `409` error?
- [senior] What goes wrong in production if an offline user creates a parent item (with a temporary local ID) and a child item referencing it before syncing with the server?
what-interviewers-listen-for:
- Write locally to Room immediately with a sync status flag (`PENDING_CREATE`, `PENDING_UPDATE`, `SYNCED`) and enqueue a `WorkManager` job with a network constraint to push pending rows.
- Generating client-side UUIDs as stable primary keys avoids foreign-key remapping headaches when creating related items offline.
build-exercise: Add a `SyncStatus` enum column (`PENDING`, `SYNCED`, `FAILED`) to a Room entity, insert new items as `PENDING`, and show a small cloud-sync icon on rows until a simulated worker flips them to `SYNCED`.
official-references:
- Build an offline-first app | https://developer.android.com/topic/architecture/data-layer/offline-first
common-wrong-beliefs:
- You can use `viewModelScope` to wait for connectivity and upload offline edits | If the user closes the app while offline, `viewModelScope` is cancelled; offline writes must be persisted to disk and synced via `WorkManager`.

## architecture-data/paging/paging-big-lists
interview-questions:
- [intern] Why can't you load 100,000 rows from a server or database into a single `List` in memory?
- [junior] What are the roles of `PagingSource`, `RemoteMediator`, `Pager`, and `LazyPagingItems` in Jetpack Paging 3?
- [mid] Why must `Flow<PagingData<T>>` be cached with `.cachedIn(viewModelScope)` in the ViewModel?
- [senior] What goes wrong in production when an app uses offset-based pagination (`?offset=20&limit=20`) on a rapidly changing live feed without keyset/cursor pagination or `RemoteMediator`?
what-interviewers-listen-for:
- `PagingSource` loads pages from a single source (like Room), while `RemoteMediator` fetches network pages into Room (SSOT) when `PagingSource` runs out of local items.
- Omitting `.cachedIn(viewModelScope)` causes configuration changes or multiple collectors to restart paging from page 0, and throws an exception if collected twice.
build-exercise: Implement a custom in-memory `PagingSource<Int, String>` that simulates a 100ms delay per 20-item page, expose it via `Pager(...).flow.cachedIn(viewModelScope)`, and render it with `collectAsLazyPagingItems()` in a `LazyColumn`.
official-references:
- Paging library overview | https://developer.android.com/topic/libraries/architecture/paging/v3-overview
- Page from network and database | https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
common-wrong-beliefs:
- `LazyColumn` alone solves loading 50,000 items from a database or server | `LazyColumn` only virtualizes UI composition for items already in memory; you still need Paging 3 to avoid loading all 50,000 objects into RAM at once.

## architecture-data/modules/what-is-a-module
interview-questions:
- [intern] What is a Gradle module, and why do medium-to-large Android projects split `:app` into multiple modules?
- [junior] How does splitting an app into `:core:*` and `:feature:*` modules improve Gradle build times?
- [mid] What is the trade-off between modularizing too early (dozens of tiny micro-modules) versus keeping a single `:app` monolith?
- [senior] What goes wrong in production builds when a central `:core:common` module becomes a dumping ground that every module depends on?
what-interviewers-listen-for:
- Modularization enables parallel Gradle compilation, incremental task avoidance/build caching, strict visibility boundaries (`internal`), and clear team ownership.
- If every module depends on a bloated `:core:common` module that changes frequently, any edit inside `:core:common` invalidates the build cache of the entire dependency tree, destroying incremental build speed.
build-exercise: Create a `:core:model` Kotlin library module and a `:feature:profile` Android library module, wire them into `:app`, and run `./gradlew assembleDebug --scan` to inspect parallel task execution.
official-references:
- Guide to Android app modularization | https://developer.android.com/topic/modularization
common-wrong-beliefs:
- Adding more Gradle modules always makes the build faster | Poorly structured modules with deep sequential chains or a frequently modified `:core` module can actually increase Gradle configuration and compilation time.

## architecture-data/modules/module-boundaries
interview-questions:
- [intern] Why does Gradle forbid circular dependencies between two modules (`:feature:a` depending on `:feature:b` while `:feature:b` depends on `:feature:a`)?
- [junior] What is the difference between `implementation` and `api` in a module's `dependencies { }` block, and how does `internal` visibility help enforce module boundaries?
- [mid] If `:feature:checkout` needs to navigate to `:feature:orders` without depending on `:feature:orders` directly, how do you structure the navigation contract?
- [senior] What goes wrong in a multi-module project when developers use `api(...)` instead of `implementation(...)` across modules?
what-interviewers-listen-for:
- Feature modules should not depend directly on each other's implementation; they communicate through navigation contracts wired in `:app` or lightweight `:feature:*:api` modules.
- Using `api` leaks transitive dependencies onto consumers' compile classpaths, meaning any ABI change inside the dependency forces recompilation of every upstream module.
build-exercise: Put a Room Entity inside `:core:database` marked `internal`, expose a public domain model from `:core:model` via `implementation`, and verify that `:feature:home` cannot even import the database entity.
official-references:
- Guide to Android app modularization | https://developer.android.com/topic/modularization
- Common modularization patterns | https://developer.android.com/topic/modularization/patterns
common-wrong-beliefs:
- `public` classes in a library module are accessible to every module in the project | A module can only see `public` classes of modules on its compile classpath (`implementation` or transitive `api`); `internal` restricts access strictly to that single Gradle module.

## architecture-data/layers-and-mapping/layers-and-their-jobs
interview-questions:
- [intern] What is the specific job of each layer (UI, Domain, Data) when a user opens a product detail screen?
- [junior] Where should input validation, retry policies, and currency formatting each live across the three layers?
- [mid] Why should exceptions or low-level SQLite/Retrofit types from the Data layer not leak directly into the UI layer?
- [senior] What goes wrong in production when business rules (like calculating cart totals or eligibility) are duplicated inside Composables instead of living in the Domain/ViewModel layer?
what-interviewers-listen-for:
- Data layer handles persistence/IO/caching; Domain layer enforces business invariants; UI layer handles presentation state, formatting, and user interaction.
- Leaky abstractions force UI code to change whenever you switch from Retrofit to Ktor or alter a database schema.
build-exercise: Trace a feature that loads a product price in cents from an API, caches it in Room, applies a member discount rule, and formats it into a localized currency string—placing each step in its proper layer.
official-references:
- Guide to app architecture | https://developer.android.com/topic/architecture
common-wrong-beliefs:
- The ViewModel is part of the Domain layer | The ViewModel is a UI-layer state holder that adapts Domain/Data outputs into screen-specific `UiState`.

## architecture-data/layers-and-mapping/mapping-between-models
interview-questions:
- [intern] What are the three distinct model classes (`UserDto`, `UserEntity`, `User` / `UserUiModel`) often used for the same concept?
- [junior] How do Kotlin extension functions (`fun UserDto.toEntity()`, `fun UserEntity.toDomain()`) keep mapping code clean and readable?
- [mid] What is the trade-off between creating separate DTO, Entity, and Domain classes versus reusing a single class with `@Serializable` and `@Entity` annotations?
- [senior] What goes wrong in production when a single class is annotated with both `@Serializable` (network) and `@Entity` (Room) and the backend renames or drops a JSON field?
what-interviewers-listen-for:
- DTOs match the external API JSON schema, Entities match the local SQLite table schema and indexes, and Domain/UI models match business logic and Compose stability requirements.
- Coupling `@Serializable` and `@Entity` onto one class means a backend JSON change forces a Room database schema migration (or crashes users) and overwrites local-only columns like `isBookmarked`.
build-exercise: Write `ArticleDto` (nullable fields from network), `ArticleEntity` (Room primary key + `isBookmarked`), and `Article` (clean non-null domain model), along with `.toEntity()` and `.toDomain()` mapper extensions.
official-references:
- Data layer | https://developer.android.com/topic/architecture/data-layer
common-wrong-beliefs:
- Writing separate network DTO and Room Entity classes is pointless boilerplate | Mixing network and database annotations on one class causes remote JSON changes to break local database schemas and overwrite local user state.

## architecture-data/putting-it-together/move-state-into-a-viewmodel
interview-questions:
- [intern] How do you tell which state in a Composable belongs in a `ViewModel` versus staying in `remember` inside the Composable?
- [junior] Why is passing two lambdas (`uiState: UiState, onEvent: (UiEvent) -> Unit`) into a stateless screen Composable better than passing the `ViewModel` instance all the way down?
- [mid] When moving state from a Composable's `LaunchedEffect` into a `ViewModel`, why should initial data loading use `stateIn` (`WhileSubscribed(5000)`) instead of a side-effect call from the UI?
- [senior] What goes wrong in production when a Composable calls `viewModel.loadData()` inside `LaunchedEffect(Unit)` every time the user rotates the phone or navigates back?
what-interviewers-listen-for:
- Separating the route-level Composable (which injects the `ViewModel` and calls `collectAsStateWithLifecycle`) from the stateless screen Composable enables instant `@Preview` rendering and fast UI tests without Hilt/ViewModels.
- Triggering loads from `LaunchedEffect(Unit)` re-fires on every configuration change and back-stack pop; reactive streams in the ViewModel (`stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Loading)`) survive rotation seamlessly.
build-exercise: Refactor a Composable that fetches data in `LaunchedEffect` and stores it in `remember` into a Route Composable + stateless Screen Composable + `ViewModel` exposing `StateFlow<UiState>`, then write a `@Preview` for the Screen.
official-references:
- State holders and UI State | https://developer.android.com/topic/architecture/ui-layer/stateholders
- ViewModel overview | https://developer.android.com/topic/libraries/architecture/viewmodel
common-wrong-beliefs:
- Calling `viewModel.fetchData()` inside `LaunchedEffect(Unit)` runs only once per screen lifetime | `LaunchedEffect(Unit)` leaves and re-enters composition on every screen rotation and back-navigation, re-triggering the fetch unless guarded or replaced by a reactive `StateFlow`.

## architecture-data/putting-it-together/review-generated-screen-code
interview-questions:
- [intern] When reviewing generated Kotlin/Android code for a new screen, what are the first 3 red flags you check in the ViewModel and Composable?
- [junior] Why is `collectAsState()` instead of `collectAsStateWithLifecycle()` a bug to flag in an Android code review?
- [mid] If generated code exposes `val state = MutableStateFlow(...)` publicly, catches `Exception` broadly, or injects `Context` into the ViewModel, why must you reject it?
- [senior] What goes wrong in production when AI-generated or boilerplate code creates a new `Repository` or `Room.databaseBuilder` inside a ViewModel or Composable instead of injecting it?
what-interviewers-listen-for:
- Checklist: exposed mutable state, lifecycle-unaware flow collection, swallowed `CancellationException`, hardcoded dispatchers (`Dispatchers.IO`), non-atomic `_state.value = ...` updates, and leaked `Context` references.
- Reviewing architectural boundaries ensures testability, rotation survival, process-death safety, and main-thread safety before merging.
build-exercise: Take a deliberately flawed snippet (public `MutableStateFlow`, `LaunchedEffect` calling `repo.fetch()`, `catch (e: Exception)`, `collectAsState()`) and fix all 4 defects so a unit test with `runTest` passes cleanly.
official-references:
- Guide to app architecture | https://developer.android.com/topic/architecture
- UI layer | https://developer.android.com/topic/architecture/ui-layer
common-wrong-beliefs:
- If generated Android code compiles without warnings and works when tapped once, it is production-ready | Lifecycle leaks, process-death bugs, race conditions, and swallowed cancellations rarely show up on a single happy-path tap.

## Self-check
- State holders and UI State | https://developer.android.com/topic/architecture/ui-layer/stateholders
- Saved State module for ViewModel | https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
- Use test doubles in Android | https://developer.android.com/training/testing/fundamentals/test-doubles
- Manual dependency injection | https://developer.android.com/training/dependency-injection/manual
- App Architecture: Data Layer - DataStore | https://developer.android.com/topic/libraries/architecture/datastore
- Define relationships between objects | https://developer.android.com/training/data-storage/room/relationships
- Page from network and database | https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
- Common modularization patterns | https://developer.android.com/topic/modularization/patterns
- Build an offline-first app | https://developer.android.com/topic/architecture/data-layer/offline-first
- Write asynchronous DAO queries | https://developer.android.com/training/data-storage/room/async-queries