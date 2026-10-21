```yaml
branch: architecture-data
bough: architecture-kmp
stems:
  - id: why-architecture
    title: "Why Architecture"
    why: "A fresher needs to see why one big screen file becomes impossible to change before any pattern makes sense."
    leaves:
      - id: why-code-needs-structure
        title: "Why does an app with everything in one file become impossible to change?"
        difficulty: easy
        requires: []
        fruit: false
      - id: three-jobs-in-an-app
        title: "Which code shows things, which decides, and which fetches?"
        difficulty: easy
        requires: [architecture-data/why-architecture/why-code-needs-structure]
        fruit: false
  - id: viewmodel
    title: "ViewModel"
    why: "The ViewModel is where screen state lives in modern Android, and most lost-state bugs come from misusing it."
    leaves:
      - id: what-is-a-viewmodel
        title: "What can a ViewModel hold that the screen cannot?"
        difficulty: medium
        requires: [architecture-data/why-architecture/three-jobs-in-an-app]
        fruit: false
      - id: viewmodel-lifetime
        title: "When is a ViewModel created and when does it die?"
        difficulty: hard
        requires: [architecture-data/viewmodel/what-is-a-viewmodel]
        fruit: false
      - id: keep-the-screen-out-of-the-viewmodel
        title: "Why must a ViewModel never hold a screen or a Context?"
        difficulty: medium
        requires: [architecture-data/viewmodel/what-is-a-viewmodel]
        fruit: false
      - id: viewmodel-and-process-death
        title: "How do I keep a small value when Android kills my app?"
        difficulty: hard
        requires: [architecture-data/viewmodel/viewmodel-lifetime]
        fruit: false
  - id: ui-state-and-udf
    title: "UI State and One-Way Data Flow"
    why: "Where state lives and who may change it is the checkpoint ability, and generated screens often get it wrong."
    leaves:
      - id: what-is-ui-state
        title: "Why describe a whole screen with one state object?"
        difficulty: medium
        requires: [architecture-data/viewmodel/what-is-a-viewmodel]
        fruit: false
      - id: loading-success-error
        title: "How do I model loading, success and error so no case is forgotten?"
        difficulty: medium
        requires: [architecture-data/ui-state-and-udf/what-is-ui-state]
        fruit: false
      - id: state-down-events-up
        title: "Why does state flow down to the screen while events flow up?"
        difficulty: hard
        requires: [architecture-data/ui-state-and-udf/loading-success-error]
        fruit: false
      - id: expose-state-not-mutable-state
        title: "Why does a ViewModel keep its state private and show the screen a read-only copy?"
        difficulty: easy
        requires: [architecture-data/ui-state-and-udf/state-down-events-up]
        fruit: false
  - id: repositories
    title: "Repositories"
    why: "A repository is the one place that knows where data comes from, so screens never talk to the database or network directly."
    leaves:
      - id: what-is-a-repository
        title: "Who should the ViewModel ask for data?"
        difficulty: easy
        requires: [architecture-data/viewmodel/what-is-a-viewmodel]
        fruit: false
      - id: repository-interface-and-fakes
        title: "How do I swap real data for fake data without changing the screen?"
        difficulty: medium
        requires: [architecture-data/repositories/what-is-a-repository]
        fruit: false
  - id: dependency-injection
    title: "Dependency Injection"
    why: "Generated Android code is full of injected classes and annotations that a fresher must read without fear."
    leaves:
      - id: what-is-a-dependency
        title: "Why is a class that builds its own helpers hard to test and change?"
        difficulty: easy
        requires: [architecture-data/repositories/repository-interface-and-fakes]
        fruit: false
      - id: constructor-injection
        title: "How do I hand a class the helpers it needs?"
        difficulty: easy
        requires: [architecture-data/dependency-injection/what-is-a-dependency]
        fruit: false
      - id: what-a-di-library-does
        title: "What does a DI library do that I would otherwise write by hand?"
        difficulty: medium
        requires: [architecture-data/dependency-injection/constructor-injection, architecture-data/viewmodel/what-is-a-viewmodel]
        fruit: false
      - id: singletons-and-scopes-in-di
        title: "Why did two screens get the same object, or two different ones?"
        difficulty: hard
        requires: [architecture-data/dependency-injection/what-a-di-library-does]
        fruit: false
  - id: key-value-storage
    title: "Key-Value Storage"
    why: "Settings and small flags need a safe place that survives closing the app, without a full database."
    leaves:
      - id: store-a-setting-with-datastore
        title: "Where do I save a small setting like the chosen language?"
        difficulty: medium
        requires: [architecture-data/repositories/what-is-a-repository]
        fruit: false
      - id: reading-old-sharedpreferences-code
        title: "How do I read old SharedPreferences code?"
        difficulty: easy
        requires: [architecture-data/key-value-storage/store-a-setting-with-datastore]
        fruit: false
  - id: databases
    title: "Databases"
    why: "Lists, histories and saved items outgrow a settings file, and Room is how most Android apps keep them."
    leaves:
      - id: rows-tables-and-keys
        title: "How does a database table let me find exactly one row?"
        difficulty: easy
        requires: [architecture-data/repositories/what-is-a-repository]
        fruit: false
      - id: sql-basics
        title: "How do I ask a database for the rows I want?"
        difficulty: medium
        requires: [architecture-data/databases/rows-tables-and-keys]
        fruit: false
      - id: room-entities-and-daos
        title: "What are the three pieces of a Room database?"
        difficulty: medium
        requires: [architecture-data/databases/sql-basics, architecture-data/repositories/repository-interface-and-fakes]
        fruit: false
      - id: room-and-flow
        title: "How does the screen update by itself when a row in the database changes?"
        difficulty: medium
        requires: [architecture-data/databases/room-entities-and-daos]
        fruit: false
      - id: relations-between-tables
        title: "How do I store a list that belongs to one item, like orders for a customer?"
        difficulty: hard
        requires: [architecture-data/databases/room-and-flow]
        fruit: false
  - id: offline-first
    title: "Offline First"
    why: "Phones lose signal all the time, so the screen should read from local data and refresh quietly."
    leaves:
      - id: single-source-of-truth
        title: "When data comes from both the network and the disk, which one does the screen trust?"
        difficulty: medium
        requires: [architecture-data/databases/room-and-flow, architecture-data/repositories/repository-interface-and-fakes]
        fruit: false
      - id: cache-then-refresh
        title: "Why does the screen show old data first and then update?"
        difficulty: medium
        requires: [architecture-data/offline-first/single-source-of-truth]
        fruit: false
      - id: offline-writes
        title: "What happens to a save the user makes while offline?"
        difficulty: hard
        requires: [architecture-data/offline-first/cache-then-refresh]
        fruit: false
  - id: paging
    title: "Paging"
    why: "Showing ten thousand rows by loading them all runs out of memory, so big lists load in pages."
    leaves:
      - id: paging-big-lists
        title: "How do I show a very long list without loading all of it?"
        difficulty: hard
        requires: [architecture-data/databases/room-and-flow]
        fruit: false
  - id: modules
    title: "Modules"
    why: "Larger apps are split into modules, and a fresher must know why and what one module may use from another."
    leaves:
      - id: what-is-a-module
        title: "Why would I split one app into several modules?"
        difficulty: medium
        requires: [architecture-data/repositories/repository-interface-and-fakes]
        fruit: false
      - id: module-boundaries
        title: "What may one module see of another?"
        difficulty: medium
        requires: [architecture-data/modules/what-is-a-module, architecture-data/dependency-injection/constructor-injection]
        fruit: false
  - id: layers-and-mapping
    title: "Layers and Mapping"
    why: "Naming the layers and keeping their models apart stops one change from rippling through the whole app."
    leaves:
      - id: layers-and-their-jobs
        title: "What job does each layer of an app do?"
        difficulty: medium
        requires: [architecture-data/repositories/repository-interface-and-fakes, architecture-data/ui-state-and-udf/expose-state-not-mutable-state, architecture-data/modules/what-is-a-module]
        fruit: false
      - id: mapping-between-models
        title: "Why keep a database model, a network model and a screen model apart?"
        difficulty: medium
        requires: [architecture-data/layers-and-mapping/layers-and-their-jobs, architecture-data/databases/room-entities-and-daos]
        fruit: false
  - id: putting-it-together
    title: "Putting It Together"
    why: "The checkpoint ability is moving state into a ViewModel and judging where generated code keeps its state."
    leaves:
      - id: move-state-into-a-viewmodel
        title: "How do I move state from a screen into a ViewModel?"
        difficulty: medium
        requires: [architecture-data/ui-state-and-udf/expose-state-not-mutable-state, architecture-data/viewmodel/viewmodel-lifetime]
        fruit: false
      - id: review-generated-screen-code
        title: "Would I accept this generated screen, ViewModel and repository in review?"
        difficulty: hard
        requires: [architecture-data/putting-it-together/move-state-into-a-viewmodel, architecture-data/layers-and-mapping/mapping-between-models, architecture-data/offline-first/single-source-of-truth, architecture-data/dependency-injection/singletons-and-scopes-in-di]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. The branch is phase 4. Android framework code does not run in the browser editor and there is no Android emulator or simulator (L15, D8). Android-behaviour sims use `code-to-screen`: the learner flips between pre-written code variants and watches a drawn screen and timeline change.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write` (phase 1) — used by review-generated-screen-code.
- Assumes Kotlin: `kotlin/classes-and-objects/what-is-a-class` (phase 2) — every leaf defines classes.
- Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface` (phase 3) — used by repository-interface-and-fakes.
- Assumes Kotlin: `kotlin/special-classes/sealed-classes` (phase 3) — used by loading-success-error.
- Assumes Kotlin: `kotlin/special-classes/data-class-copy` (phase 3) — used by what-is-ui-state (new state by copy).
- Assumes Kotlin: `kotlin/classes-and-objects/read-only-is-not-immutable` (phase 2) — used by expose-state-not-mutable-state.
- Assumes Kotlin: `kotlin/extensions-scope-and-delegates/property-delegates` (phase 3) — `by viewModels()` and `by lazy` are read, not written.
- Assumes Android core: `android-core/lifecycle/what-is-the-lifecycle` and `android-core/configuration-changes/why-rotation-restarts` (phase 3) — used by what-is-a-viewmodel and viewmodel-lifetime.
- Assumes Android core: `android-core/configuration-changes/surviving-a-config-change` and `android-core/process-death/what-is-process-death` (phase 3) — used by viewmodel-and-process-death.
- Assumes Android core: `android-core/activities/what-is-context` (phase 3) — used by keep-the-screen-out-of-the-viewmodel.
- Assumes UI: `ui/state-and-recomposition/state-hoisting` (phase 3) — used by state-down-events-up and move-state-into-a-viewmodel.
- Assumes UI: `ui/lists/lazy-column` (phase 3) — used by paging-big-lists.
- Assumes Concurrency: `concurrency/scopes/scopes-in-android-code` (phase 4, earlier branch in the tie-break order) — used by what-is-a-viewmodel (`viewModelScope`).
- Assumes Concurrency: `concurrency/state-and-shared-flow/state-flow` — used by expose-state-not-mutable-state.
- Assumes Concurrency: `concurrency/dispatchers/main-safe-functions` — used by room-entities-and-daos and repository-interface-and-fakes.
- Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow` — used by room-and-flow and single-source-of-truth.
- Assumes Concurrency: `concurrency/state-and-shared-flow/share-a-cold-flow` — used by cache-then-refresh.
- Helps to know Networking: `networking/http/request-and-response` (phase 4) — single-source-of-truth recaps "data from a server" in one paragraph.
- Helps to know OS: `os/files/files-folders-and-owners` (phase 6) — rows-tables-and-keys recaps what a file on disk is.
- Helps to know App networking: `app-networking/http-client/first-get-call` (phase 4, later in the tie-break order) — cache-then-refresh and offline-writes recap a network call in one paragraph; no leaf depends on it.
- Helps to know UI: `ui/navigation/passing-arguments` (phase 3) — viewmodel-and-process-death mentions how a screen receives an id argument.
- Helps to know UI: `ui/side-effects/collecting-flows` (phase 5) — collecting a state flow in a screen is taught there; this branch names it only.
- Helps to know Android core: `android-core/background-work/what-is-workmanager` (phase 5) — offline-writes mentions WorkManager as the way to retry a save later.
- Helps to know Tools: `tools/gradle-basics/dependencies-and-configurations` (phase 5) — module-boundaries and what-a-di-library-does recap where a dependency is declared.
- Helps to know KMP: `kmp/shared-storage/shared-database` (phase 6) — Room and DataStore are noted as multiplatform-capable; the KMP branch teaches the setup.

Foundation is recommended first ("Start here"), then Kotlin, Android core and UI phase-3 stems, then `concurrency`. Later branches assume this one: `app-networking` assumes the repository leaves, `testing` assumes `viewmodel` and `repositories`, and `kmp` assumes the whole data layer.

Challenge note: framework code (ViewModel, Room, DataStore, injection libraries) does not run in the Kotlin Playground. Every `P:` and `F:` on framework code is a plain-Kotlin model (a fake ViewModel class, a map as a table, a manual constructor) or a read-only "which line is wrong" task. ⚠️ VERIFY with `docs/06` once written.

Facts checked against official docs on 18 Oct 2026, to be rechecked by `reviewer` before each leaf goes live:

- ViewModel (developer.android.com/topic/libraries/architecture/viewmodel): caches state through configuration changes; stays until its owner disappears (activity finished, navigation entry removed from the back stack, or composable leaves the Composition); `SavedStateHandle` persists data across process death; `viewModelScope` follows the ViewModel's lifecycle; ViewModels should not hold lifecycle-related references such as `Context` or `Resources` (the page names those two, not Activity or View); do not pass ViewModels to other classes.
- DataStore (developer.android.com/topic/libraries/architecture/datastore): for small datasets; Room is advised for large or complex data; Preferences DataStore is the migration target from SharedPreferences; one instance per file per process; the stored type must be immutable; do not read or write it directly inside composables; Kotlin Multiplatform is supported.
- Room (developer.android.com/training/data-storage/room): three components, database class, entities, DAOs; DAO functions can be `suspend`; Kotlin Multiplatform is supported. The page did not state the main-thread rule or `Flow` return types in the part read. ⚠️ VERIFY both before room-entities-and-daos and room-and-flow.
- Architecture guide (developer.android.com/topic/architecture): UI layer, data layer, optional domain layer; single source of truth, where only the owner can change the data; unidirectional data flow, where state flows down and events flow up; in an offline-first app the source of truth is typically a database.
- Coroutines best practices (developer.android.com/kotlin/coroutines/coroutines-best-practices): expose immutable types; inject dispatchers.
- Everything else (Hilt and other DI libraries, Paging 3, module visibility rules, current Room and DataStore versions) is not asserted here. ⚠️ VERIFY on the publish date.

## Stems

### why-architecture · Why Architecture

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-code-needs-structure` | easy | "What goes wrong when a screen file does the drawing, the thinking and the fetching all in one place?" | A kitchen where the cook, waiter and cashier are one tired person: any change to one job breaks the other two. | "Architecture is extra ceremony for big companies; a small app does not need it." | `none:` a before-and-after file layout diagram is enough; nothing moves. | `P:` which line must change when the price rule changes in a 40-line mixed screen · `F(ai):` a generated screen that fetches, parses and draws in one function · `B:` add one feature and count edited places | Assumes Kotlin: `kotlin/classes-and-objects/what-is-a-class`. |
| `three-jobs-in-an-app` | easy | "Which code shows things, which decides, and which fetches?" | A restaurant: the dining room shows, the head waiter decides, the storeroom holds the stock. | "Each job needs its own screen or its own library." | `none:` sorting code lines into three labelled boxes is a diagram exercise. | `P:` sort ten lines of a mixed screen into show, decide and fetch · `F:` a line that fetches placed in the drawing code | Names the three jobs once; the layers leaf later gives them formal names. |

### viewmodel · ViewModel

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-viewmodel` | medium | "What can a ViewModel hold that the screen itself cannot?" | A stage manager who keeps the props while actors change costume; the audience sees only the stage. | "A ViewModel is a second Activity." | `none:` a role idea; a diagram of screen, ViewModel and data is enough, and the rotation animation comes in `viewmodel-lifetime`. | `P:` where a counter starts again after rotation in a plain-Kotlin model, with and without a ViewModel · `F:` a counter kept as a screen field · `B:` move it into the model object | volatile. Assumes Android core: `android-core/lifecycle/what-is-the-lifecycle`. Assumes Concurrency: `concurrency/scopes/scopes-in-android-code`. Checked 18 Oct 2026: ViewModel caches state through configuration changes. ⚠️ VERIFY again before live. |
| `viewmodel-lifetime` | hard | "When is a ViewModel created and when does it die?" | A shop's regular storeroom: the storeroom stays while staff change shifts, but is cleared when the shop closes for good. | "A ViewModel lives forever, or until the app closes." | `sim: code-to-screen` a phone and a timeline; the learner rotates, presses back, navigates away and closes the screen, and watches the ViewModel box stay or be cleared. | `P:` after which of five actions the counter restarts · `F(ai):` a generated screen that creates the ViewModel with its constructor on every composition · `B:` rotate before the first data arrives | volatile. Assumes Android core: `android-core/configuration-changes/why-rotation-restarts`. Checked 18 Oct 2026: a ViewModel remains until its owner disappears (activity finished, navigation entry removed from the back stack, composable leaves the Composition). ⚠️ VERIFY. |
| `keep-the-screen-out-of-the-viewmodel` | medium | "Why must a ViewModel never hold a screen or a Context?" | A coat-check ticket again: while the storeroom holds the ticket, the coat cannot be given away. | "Storing the Activity in the ViewModel is handy and harmless." | `none:` the memory picture is already animated in `android-core/lifecycle/leaking-an-activity`; a short diagram is enough here. | `F(ai):` a generated ViewModel that keeps an Activity "to show a toast" · `F(ai):` a ViewModel that stores a Context from its constructor · `P:` how many old screens stay in memory after three rotations | Assumes Android core: `android-core/activities/what-is-context`. Checked 18 Oct 2026: ViewModels should not hold lifecycle-related references such as `Context` or `Resources`. ⚠️ VERIFY the Application-context exception wording. |
| `viewmodel-and-process-death` | hard | "How do I keep a small value when Android kills my app while it is in the background?" | A note pinned to the fridge for the next cook: small, simple, and still there after the kitchen is rebuilt. | "A ViewModel survives everything, including the app being killed." | `sim: code-to-screen` reuse `viewmodel-lifetime` sim, with a "kill the app" button; the ViewModel box vanishes and only a small saved note brings back the value. | `P:` which of field, ViewModel and `SavedStateHandle` still holds a value after process death · `F(ai):` a search query kept only in a ViewModel field and lost after the drill · `B:` save a large list into the saved note | volatile. Assumes Android core: `android-core/process-death/what-is-process-death`. Helps to know UI: `ui/navigation/passing-arguments` (arguments reach the ViewModel through saved state). Checked 18 Oct 2026: `SavedStateHandle` persists data across process death. ⚠️ VERIFY the size guidance. |

### ui-state-and-udf · UI State and One-Way Data Flow

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-ui-state` | medium | "Why describe a whole screen with one state object?" | A restaurant order board: one board shows everything the kitchen needs to know right now. | "State is just a few variables I update as events happen." | `none:` one rule; a screen with four loose variables beside the same screen as one object is clearer as two code panels. | `P:` which screens are possible with three Boolean flags and which with one state object · `F(ai):` a screen with `isLoading` and `isError` that are both true · `B:` add a field and count the edited places | Assumes Kotlin: `kotlin/special-classes/data-class-copy`. |
| `loading-success-error` | medium | "How do I model loading, success and error so no case is forgotten?" | A traffic light with three lamps: the driver must have a plan for every colour. | "Booleans like `isLoading` and `hasError` are simpler than a sealed type." | `none:` the compiler error when a case is missing is the demo; the Kotlin sealed-class tray picture is reused in the page text. | `P:` which branch runs for `Error("x")` · `F(ai):` a generated `when` with `else -> {}` that swallows a new case · `F(ai):` loading and error states that can both be on · `B:` add a fourth state and remove the `else` | Assumes Kotlin: `kotlin/special-classes/sealed-classes`. |
| `state-down-events-up` | hard | "Why does state flow down to the screen while events flow up to the ViewModel?" | A control tower: planes report events up, the tower sends orders down, and no plane changes the board itself. | "The screen can change the state directly, which is quicker." | `sim: code-to-screen` a phone and a ViewModel with two arrows, state down and events up; the learner taps in the phone and watches the arrows and the redraw; a direct-edit variant breaks the arrows and shows state out of sync after rotation. | `P:` the sequence of calls after one tap · `F(ai):` a screen that edits a copy of the list it was given and never tells the ViewModel · `B:` change the state from two places | volatile. Assumes UI: `ui/state-and-recomposition/state-hoisting`. Checked 18 Oct 2026 (architecture guide): state flows in one direction, events flow in the opposite direction. ⚠️ VERIFY. |
| `expose-state-not-mutable-state` | easy | "Why does the ViewModel keep its state private and show the screen a read-only copy?" | A noticeboard behind glass: anyone may read, only the caretaker has the key. | "A read-only type means the data can never change." | `none:` a visibility rule; two code panels with compiler errors are the demo. | `F(ai):` a ViewModel with a public `MutableStateFlow` that three screens change · `P:` which line fails to compile · `B:` expose the mutable one and change it from the screen | Assumes Concurrency: `concurrency/state-and-shared-flow/state-flow`. Assumes Kotlin: `kotlin/classes-and-objects/read-only-is-not-immutable`. Checked 18 Oct 2026 (coroutines best practices): prefer exposing immutable types so changes are centralised. |

### repositories · Repositories

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-repository` | easy | "Who should the ViewModel ask for data?" | A librarian: you ask for a book by title and do not care which shelf or building holds it. | "A repository is just a class that wraps one database table." | `none:` a role idea; a diagram with one box in the middle and two data sources behind it is enough. | `P:` which class knows about the network in a three-class diagram · `F(ai):` a ViewModel that calls the database directly · `B:` change the data source and count changed classes | Checked 18 Oct 2026 (architecture guide): the data layer is made of repositories, each with zero or more data sources. |
| `repository-interface-and-fakes` | medium | "How do I swap real data for fake data without changing the screen?" | A stunt double: the camera does not know or care who stands in for the actor. | "Interfaces are only for big projects; I can just call the class." | `none:` a design pattern; a before-and-after code panel with a plain-Kotlin fake repository is clearer. | `P:` output of a ViewModel model using a fake repository · `F:` a ViewModel that creates its own repository and cannot take a fake · `B:` make the fake return an error | Assumes Kotlin: `kotlin/interfaces-and-inheritance/what-is-an-interface`. Assumes Concurrency: `concurrency/dispatchers/main-safe-functions`. Testing branch builds on this. |

### dependency-injection · Dependency Injection

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-dependency` | easy | "Why is a class that builds its own helpers hard to test and change?" | A cook who grows their own wheat: to change the flour you must change the farm. | "Dependency injection is a library." | `none:` a before-and-after code panel is enough. | `P:` how many places change when the helper changes, with and without hand-over · `F:` a class that creates its own repository inside its body | The word is explained before any library appears. ⚠️ VERIFY: current Android guidance on dependency injection (developer.android.com) before it goes live. |
| `constructor-injection` | easy | "How do I hand a class the helpers it needs?" | Arriving at work and being given your tools, instead of fetching them yourself. | "Injection needs a special framework." | `none:` syntax and design rule; the editor is enough. | `P:` output with a real and a fake helper passed in · `F:` a helper passed in but ignored because the class makes its own · `B:` pass nothing | Pure Kotlin; runs in the Playground. ⚠️ VERIFY: current Android guidance names constructor injection as the preferred way (developer.android.com). |
| `what-a-di-library-does` | medium | "What does a DI library do that I would otherwise write by hand?" | An agency that delivers the right tools to each worker's desk before the shift starts. | "A DI library makes the app faster." | `none:` annotation reading; an annotated example next to the same code written by hand is clearer. | `P:` which class receives the repository in an annotated example · `F(ai):` a generated `@Inject` class missing the binding that makes the build fail · `B:` remove the module that provides the interface | volatile. Assumes Kotlin: `kotlin/extensions-scope-and-delegates/property-delegates`. ⚠️ VERIFY: current Google guidance on Hilt versus Koin versus manual injection, and which are available for KMP; no claim made here. Helps to know Tools: `tools/gradle-basics/dependencies-and-configurations`. |
| `singletons-and-scopes-in-di` | hard | "Why did two screens get the same object, or two different ones?" | A shared photocopier on a floor versus a personal printer on each desk: sharing saves money but shares paper jams. | "Every injected object is shared, or every injected object is new." | `sim:` a ball factory with three rules (one for the whole app, one per screen, new each time); two screens ask for a ball and the learner sees whether they hold the same ball. | `P:` do two screens see the same counter under each rule · `F(ai):` a generated app-wide object holding per-user state that leaks · `B:` change the rule and rotate | Names `Singleton`, per-screen and per-ViewModel lifetimes. ⚠️ VERIFY scope names for the chosen library. |

### key-value-storage · Key-Value Storage

Flags: phase=4 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `store-a-setting-with-datastore` | medium | "Where do I save a small setting like the chosen language?" | A sticky note on the fridge: small, always in the same place, for a few facts. | "DataStore is a small database for any list." | `none:` a choice rule; a table of "setting versus list" is clearer than motion. | `P:` which of five items belong in key-value storage · `F(ai):` a generated screen that reads DataStore inside a composable body · `F(ai):` two DataStore instances for the same file · `B:` store a list of 500 items | volatile. Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow`. Checked 18 Oct 2026 (DataStore guide): small datasets only, Room for large or complex data; one instance per file per process; immutable type; avoid reading it directly in composables. ⚠️ VERIFY again before live. |
| `reading-old-sharedpreferences-code` | easy | "What do the get and put calls in old SharedPreferences code do?" | An old paper address book: it still works, but the new system is the one being kept up to date. | "SharedPreferences is the current best way because most tutorials use it." | `none:` reading skill; side-by-side old and new code panels are enough. | `F(ai):` a generated helper that uses SharedPreferences with a blocking write on the main thread · `P:` where an old key appears in the new code · `B:` migrate one key by hand | Checked 18 Oct 2026 (DataStore guide): migrating from SharedPreferences to DataStore is recommended. Mentions Android core `android-core/files-and-sharing/app-private-files` as a recap. |

### databases · Databases

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `rows-tables-and-keys` | easy | "How does a table's key point to exactly one row?" | A school register: one page per class, one line per student, and a roll number that finds exactly one of them. | "A database is just a big file I read from top to bottom." | `none:` a table diagram with a key column is enough. | `P:` which row does key 3 find in a table of five rows · `F:` two rows that share a key · `B:` delete a row and look for its key | Helps to know OS: `os/files/files-folders-and-owners`. |
| `sql-basics` | medium | "How do I ask a database for the rows I want?" | Asking the librarian for all books by one author, sorted by year, instead of reading every shelf. | "I need to load the whole table and filter it in Kotlin." | `none:` a language rule; a small table and three queries in the editor are enough. | `P:` which rows `SELECT ... WHERE ...` returns on a given table · `F:` an `UPDATE` without `WHERE` that changes every row · `B:` delete without a filter | Covers `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `WHERE`. The editor shows a plain-Kotlin list model; real SQL is read, not run. ⚠️ VERIFY whether any SQL runner fits the editor. |
| `room-entities-and-daos` | medium | "Which three pieces do I write to get a Room database: an entity, a DAO and a database class?" | A form template that prints the register layout, plus a list of the questions you may ask the register. | "Room runs on the main thread because the data is small." | `none:` annotation reading; one annotated class, one DAO and one database class in a diagram are enough. | `P:` which table and column a given annotated field creates · `F(ai):` a generated DAO with a blocking query called from a screen · `F(ai):` an entity missing a primary key · `B:` rename a column | volatile. Assumes Concurrency: `concurrency/dispatchers/main-safe-functions`. Checked 18 Oct 2026 (Room guide): three components, database class, entities, DAOs; DAO functions can be `suspend`; Kotlin Multiplatform supported. ⚠️ VERIFY: Room's main-thread rule and current version. |
| `room-and-flow` | medium | "How does the screen update by itself when a row in the database changes?" | A departures board wired to the ticket office: when a ticket is sold, the board changes without anyone asking. | "I have to reload the list manually after every insert." | `sim: code-to-screen` a phone showing a list, a database table beside it and an insert button; flip between a one-shot read and a `Flow` read and watch the list update by itself or go stale. | `P:` how many times the list redraws after three inserts · `F(ai):` a screen that reads once and shows stale data · `B:` insert from a second place | volatile. Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow`. ⚠️ VERIFY: Room returns `Flow` from DAO queries and the current guidance for exposing it. |
| `relations-between-tables` | hard | "How do I store a list that belongs to one item, like orders for a customer?" | A customer card holding a pile of order slips, each slip stamped with the customer's number. | "I can store a list inside one column." | `none:` a table diagram with a key arrow from each order row to its customer row is enough; the delete rule is a two-line code panel. | `P:` how many rows a join returns for a customer with three orders · `F(ai):` a generated entity holding a `List` of children as a single column · `B:` delete the parent | ⚠️ VERIFY current Room relation annotations (names and any KMP differences). Database migrations at scale are in `## Later`. |

### offline-first · Offline First

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `single-source-of-truth` | medium | "When data comes from both the network and the disk, which one does the screen trust?" | A kitchen pantry: groceries arrive from the shop, but cooks only ever take from the pantry. | "The screen should read from the network first and fall back to the disk." | `none:` a design rule; a diagram of arrows into one store and one arrow out is clearer than motion. | `P:` which source the screen reads in each of three designs · `F(ai):` a repository that returns network data and writes the database on the side · `B:` read from two sources at once | Helps to know Networking: `networking/http/request-and-response`. Checked 18 Oct 2026 (architecture guide): only the owner of data can change it; in an offline-first app the source of truth is typically a database. |
| `cache-then-refresh` | medium | "Why does the screen show old data first and then update?" | A shop window that shows yesterday's display while the new stock is being unpacked. | "Showing old data is a bug; the screen should wait for fresh data." | `sim: code-to-screen` a phone and a slow network bar on a timeline; with a database in the middle the list shows at once and changes when fresh data lands, without it the phone shows a spinner. | `P:` what the screen shows at seconds 0, 2 and 5 · `F(ai):` a screen that waits for the network and shows a spinner with data already on the disk · `B:` turn off the network in the sim | Helps to know App networking: `app-networking/http-client/first-get-call`. |
| `offline-writes` | hard | "What happens to a save the user makes while offline?" | Posting a letter into a box when the post office is shut: it is safe, and goes out when the doors open. | "If the network call fails, the save should fail." | `sim:` parcels pile in an outbox when the gate is shut and leave in order when it opens; the learner shuts the gate, adds parcels and opens it; a failing parcel is retried, not lost. | `P:` the order in which three queued saves reach the server · `F(ai):` a save that writes only to the server and loses the edit when offline · `B:` close the gate mid-send | Helps to know Android core: `android-core/background-work/when-work-fails` (WorkManager retries). Conflict resolution at scale is in `## Later` of `app-networking`. |

### paging · Paging

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `paging-big-lists` | hard | "How do I show a very long list without loading all of it?" | A book read one page at a time instead of carrying the printed library to your chair. | "Paging is only for slow networks." | `none:` a window-over-a-shelf diagram and a count of items in memory are enough; the page-size arithmetic is a Predict task. | `P:` how many items are in memory after scrolling to item 250 with a page size of 50 · `F(ai):` a screen that loads 10,000 rows in one query · `B:` set the page size to 1 | volatile. Assumes UI: `ui/lists/lazy-column`. ⚠️ VERIFY: current Paging library name and version and its Compose helper; no claim made here. Paging in depth is in `## Later`. |

### modules · Modules

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-module` | medium | "Why would I split one app into several modules?" | A mall with separate shops: each shop has its own door, key and stock, but shares the building. | "Modules make a small app faster." | `none:` a structure idea; a folder diagram before and after is enough. | `P:` which modules rebuild when one file changes in a three-module diagram · `F(ai):` a generated project with one module per class · `B:` merge two modules | Helps to know Tools: `tools/gradle-basics/dependencies-and-configurations`. ⚠️ VERIFY: current recommended module types in the architecture guide. |
| `module-boundaries` | medium | "What may one module see of another?" | A shop's counter: customers see the menu, not the kitchen. | "If a module can see another module's code, it should use it." | `none:` a visibility rule; a dependency arrow diagram with one forbidden arrow is enough. | `P:` which of five arrows is allowed · `F(ai):` a feature module that depends on another feature module · `B:` add a circular dependency | Assumes `architecture-data/dependency-injection/constructor-injection`. ⚠️ VERIFY: the difference between `api` and `implementation` against the Gradle docs. |

### layers-and-mapping · Layers and Mapping

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `layers-and-their-jobs` | medium | "What job does each layer of an app do?" | A restaurant: front of house, kitchen and storeroom, each with one job and one door to the next. | "More layers always means a better app." | `none:` a naming leaf; the three-job diagram from `three-jobs-in-an-app` is redrawn with official names. | `P:` which layer a given class belongs to · `F(ai):` a use-case class that only forwards one call · `B:` remove the optional layer | Checked 18 Oct 2026 (architecture guide): UI layer, data layer, and an optional domain layer ("use only when needed"). Clean-architecture debates are in `## Later`. |
| `mapping-between-models` | medium | "Why keep a database model, a network model and a screen model apart?" | Three forms for one person: a government record, a courier label and a name badge, each with only what it needs. | "One `User` class for everything saves work." | `none:` a code pattern; three small classes and two mapper functions in the editor are enough. | `P:` output of a mapper function · `F(ai):` one class annotated for the database, the network and the screen · `B:` rename a field in one model and count what breaks | Assumes `architecture-data/databases/room-entities-and-daos`. |

### putting-it-together · Putting It Together

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `move-state-into-a-viewmodel` | medium | "How do I move state from a screen into a ViewModel in a small example?" | Moving the family documents from a bag you carry into a locked cabinet at home. | "Moving state is just cutting and pasting variables." | `none:` a refactoring task; before and after code panels are the material. | `F(ai):` a generated counter-and-list screen that holds its state in the screen and loses it on rotation · `F(ai):` the move done halfway, leaving a copy in the screen · `P:` what shows after rotation before and after the move | Checkpoint ability: "Where state lives, and who is allowed to change it" (`docs/01` §6.2). Assumes UI: `ui/state-and-recomposition/state-hoisting`. |
| `review-generated-screen-code` | hard | "Which three risks in this generated screen, ViewModel and repository would stop me approving it?" | A final house inspection: walk every room, write down the three worst problems. | "If the screen works, the structure is fine." | `none:` a review exercise on a real file; a checklist (state owner, screen to database, scope, mapping, source of truth) is the material. | `F(ai):` review a 150-line generated feature and list 3 risks with line numbers · `F(ai):` a screen that talks to the database directly · `F(ai):` public mutable state · `P:` predict what survives rotation and process death | Branch synthesis; fruit (last leaf, `docs/08` §12 F2). Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `architecture-data/putting-it-together/review-generated-screen-code` | "A notes feature with one owner for its state" | One list screen and one detail screen · state owned by a ViewModel, exposed read-only · data read from a local database and refreshed in the background · a repository the ViewModel asks, with a fake used in a run · no screen, Activity or Context held by the ViewModel · write down where each piece of state lives and who may change it, then review an AI-generated version and list its three biggest risks | Says where state lives and who may change it, and moves state into a ViewModel (`docs/01` §6.2 architecture-data ability, L24). |

## Later

- MVI frameworks and event-reducer libraries: a pattern to read after plain one-way flow is solid.
- Clean-architecture use-case debates: one optional layer is enough for a first full app.
- Paging in depth, such as remote mediators: the single paging leaf covers the idea.
- DI library internals and multi-module graphs: reading injected code is enough.
- Database migrations at scale: one migration is learned when the first schema change appears.
- Full-text search and database indexes: tuning, not first-app knowledge.
- Encrypted databases: security branch territory.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 12 stems, 33 leaves, all `shadow`. | +33 | Estimate 30, band 24–36: inside the band (+10 %). Over the sketch because: `viewmodel` has 4 leaves (lifetime, screen-out and process-death each carry a different misconception); `ui-state-and-udf` has 4 (loading-success-error separated from what-is-ui-state, since Boolean flags are a distinct trap); `putting-it-together` added so the last leaf is a synthesis leaf and the fruit proves the `docs/01` §6.2 ability (F2); `reading-old-sharedpreferences-code` added because old tutorials still use it. `layers-and-mapping` is a stem with two leaves, as sketched. |
| 2026-10-18 | Difficulty mix: 8 easy, 17 medium, 8 hard (24 % hard). Sims: 7 of 33 leaves (21 %). | 0 | Within the 15–25 % sim guide and under the 30 % hard cap. Easy share is 24 % against a 40 % sanity target because most leaves touch hidden lifetime or state behaviour (Q2) and are commonly wrong (Q4). |
| 2026-10-18 | One-idea titles (A2e review, `docs/08` §3 split test): reworded `reading-old-sharedpreferences-code`, `rows-tables-and-keys`, `room-entities-and-daos` (now "the three pieces of a Room database", one pattern, as with `first-get-call`) and `layers-and-their-jobs`; slugs unchanged. `⚠️ VERIFY` added to two dependency-injection leaves. | 0 | No leaf added or removed. A Room split was not used: it would push this branch toward the band top (33 now, high 36) and add seven reference edits for a pattern that reads as one page. |
