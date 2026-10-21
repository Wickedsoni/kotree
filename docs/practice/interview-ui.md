# interview: ui

## ui/first-composable/what-is-declarative-ui
interview-questions:
- [intern] What is the fundamental difference between imperative UI (Views) and declarative UI (Jetpack Compose)?
- [junior] Why does Compose describe the screen as a function of state rather than mutating view properties step by step?
- [mid] What are the trade-offs of declarative UI regarding recomposition overhead versus imperative manual view updates?
- [senior] What goes wrong in production when developers attempt to store and mutate local mutable references inside Composables instead of lifting state?
what-interviewers-listen-for:
- Imperative UI requires manual mutation of view references (`textView.text = ...`); declarative UI re-runs Composable functions automatically whenever state changes.
- Declarative UI eliminates inconsistent UI states caused by missing manual view invalidations.
- Recomposition overhead is optimized via smart skipping, but improper state placement causes cascading recompositions.
build-exercise: Create a counter in imperative Views (`findViewById` + click listener setting text) vs declarative Compose (`val count = remember { mutableStateOf(0) }`), and observe how Compose recalculates state automatically.
official-references:
- Jetpack Compose thinking | https://developer.android.com/develop/ui/compose/mental-model
common-wrong-beliefs:
- Jetpack Compose destroys and recreates the entire view hierarchy from scratch on every state change | Compose performs intelligent recomposition, updating only the specific nodes whose state inputs have changed.

## ui/first-composable/what-is-a-composable
interview-questions:
- [intern] What makes a Kotlin function a Composable function in Jetpack Compose?
- [junior] What is the purpose of the `@Composable` annotation, and how does the Compose compiler plugin transform annotated functions?
- [mid] Why can Composable functions execute out of order, in parallel, or skip recomposition entirely?
- [senior] What goes wrong in production when a Composable function performs side effects, blocking I/O, or thread modifications during composition?
what-interviewers-listen-for:
- `@Composable` tells the Compose compiler plugin to transform the function signature, injecting a `Composer` parameter to manage the tree node lifecycle.
- Composables must be pure, idempotent, and side-effect free because the runtime can run them concurrently or skip them.
- Performing side effects during composition causes infinite recomposition loops or UI crashes.
build-exercise: Write a custom Composable function annotated with `@Composable`, inspect its generated Kotlin bytecode or intermediate representation in Android Studio, and observe the injected `Composer` parameter.
official-references:
- Jetpack Compose programming model | https://developer.android.com/develop/ui/compose/mental-model#compose-compiler
common-wrong-beliefs:
- A Composable function executes sequentially from top to bottom exactly once like a traditional rendering method | Compose can execute functions in any order, skip them entirely, or re-run them frequently during recomposition.

## ui/first-composable/text-and-images
interview-questions:
- [intern] How do you display text and images on the screen using Jetpack Compose (`Text`, `Image`)?
- [junior] How do image loading libraries like Coil integrate with Jetpack Compose (`rememberAsyncImagePainter`)?
- [mid] How does accessibility content description work on images and text in Compose?
- [senior] What goes wrong in production when loading uncompressed high-resolution remote images in lazy lists without resizing or caching?
what-interviewers-listen-for:
- `Text` renders styled strings using `TextStyle` and `Color`; `Image` renders `Painter` objects.
- Image loaders like Coil handle memory caching, disk caching, and bitmap decoding asynchronously.
- Failing to provide content descriptions on images breaks screen readers for visually impaired users.
build-exercise: Load a remote image URL using Coil's `AsyncImage` Composable inside a card, and test loading placeholder states.
official-references:
- Images in Compose | https://developer.android.com/develop/ui/compose/graphics/images/loading
- Text in Compose | https://developer.android.com/develop/ui/compose/text
common-wrong-beliefs:
- You can pass a raw URL string directly into a Compose `Image` element without an image loading library | `Image` takes a `Painter`; remote URLs require third-party libraries like Coil to fetch, decode, and cache bitmaps.

## ui/first-composable/previews
interview-questions:
- [intern] What is the `@Preview` annotation in Jetpack Compose, and how does it help development velocity?
- [junior] How do you pass parameters or use `@PreviewParameter` to preview multiple mock data states simultaneously?
- [mid] How do multi-preview annotations and system theme previews (`uiMode = Configuration.UI_MODE_NIGHT_YES`) work?
- [senior] What goes wrong in production when Composable UI components rely on real ViewModel or Repository dependencies, making them impossible to preview?
what-interviewers-listen-for:
- `@Preview` renders composables inside Android Studio's split pane without running the full app on an emulator.
- Stateless "dumb" UI components accept parameters directly, enabling robust previewing.
- Stateful composables tied directly to ViewModels cannot be previewed easily without state hoisting.
build-exercise: Create a Composable UI card, add three `@Preview` annotations configured for light mode, dark mode, and font scaling, and view them in split mode.
official-references:
- Preview your UI with composables | https://developer.android.com/develop/ui/compose/tooling/previews
common-wrong-beliefs:
- You need to deploy your app to a physical device or emulator every single time you want to check a minor UI margin adjustment | Android Studio's `@Preview` pane renders composables instantly without app deployment.

## ui/modifiers/what-is-a-modifier
interview-questions:
- [intern] What is a `Modifier` in Jetpack Compose, and what responsibilities does it govern?
- [junior] How do you chain modifiers to adjust size, padding, background color, and click behavior?
- [mid] How are modifiers represented under the hood as an immutable linked list of elements?
- [senior] What goes wrong in production when custom modifiers create unnecessary object allocations on every recomposition?
what-interviewers-listen-for:
- Modifiers decorate or augment composables (layout constraints, padding, background, gesture input).
- Modifiers form an immutable linked list node chain.
- Allocating new modifier objects inside frequently recomposed composables causes GC allocation pressure.
build-exercise: Apply a chained modifier (`Modifier.padding(8.dp).background(Color.Blue).clickable { }`) to a Box, and inspect how order alters the final box layout.
official-references:
- Modifiers in Compose | https://developer.android.com/develop/ui/compose/modifiers
common-wrong-beliefs:
- Modifiers are mutable objects that you can modify in place by calling `.add()` or `.set()` | Modifiers are strictly immutable; every modifier method returns a brand-new combined modifier node instance.

## ui/modifiers/modifier-order
interview-questions:
- [intern] Why does the order of chained modifiers matter in Jetpack Compose?
- [junior] What is the difference between applying `.padding(8.dp).background(Color.Red)` versus `.background(Color.Red).padding(8.dp)`?
- [mid] How do measurement and drawing passes evaluate modifier chains sequentially from outside-in and inside-out?
- [senior] What goes wrong in production when incorrect modifier ordering causes touch target bounds to misalign with visual padding?
what-interviewers-listen-for:
- Modifiers execute sequentially: padding applied before background pads the outer area, whereas padding applied after background pads inside the colored region.
- Order dictates layout constraints passed down to child nodes.
build-exercise: Compare `Modifier.size(100.dp).padding(10.dp)` with `Modifier.padding(10.dp).size(100.dp)` on a colored box, and observe the visual layout difference.
official-references:
- Order of modifiers in Compose | https://developer.android.com/develop/ui/compose/modifiers#order-matters
common-wrong-beliefs:
- The order in which you chain modifier functions has no impact on the final visual rendering as long as the same functions are present | Modifier order strictly dictates layout and drawing execution sequence.

## ui/modifiers/reacting-to-taps
interview-questions:
- [intern] How do you make a non-button Composable (like a Box or Row) respond to tap and click gestures?
- [junior] What is the difference between `.clickable { }`, `.combinedClickable { }`, and pointer input modifiers (`pointerInput`)?
- [mid] How do ripple effects and indication parameters work inside click modifiers?
- [senior] What goes wrong in production when handling complex multi-touch gestures without respecting touch slop and pointer consumption?
what-interviewers-listen-for:
- `.clickable` adds tap handling, accessibility semantics, and visual ripple feedback.
- Pointer input modifiers provide raw touch event stream access (`detectTapGestures`, `detectDragGestures`).
build-exercise: Add `.clickable { }` to a custom card layout, observe the built-in ripple touch feedback, and test disabling indication.
official-references:
- Gestures in Compose | https://developer.android.com/develop/ui/compose/touch-input/gestures
common-wrong-beliefs:
- You must wrap every clickable element inside a standard Android View or Button widget to handle clicks | Any Composable can receive click events by appending `.clickable { }` to its modifier chain.

## ui/layouts/column-row-box
interview-questions:
- [intern] What are the three core layout building blocks in Jetpack Compose (`Column`, `Row`, `Box`)?
- [junior] When should you choose `Column` versus `Row` versus `Box`?
- [mid] How does `Box` allow overlapping child composables (z-index layering) unlike traditional linear layouts?
- [senior] What goes wrong in production when nesting deep hierarchies of `Column` and `Row` layouts instead of using custom layouts or `ConstraintLayout`?
what-interviewers-listen-for:
- `Column` stacks vertically; `Row` aligns horizontally; `Box` layers elements on top of each other in z-order.
- Deeply nested linear layouts cause performance degradation during measurement passes.
build-exercise: Build a profile card layout using a `Box` as the base image container, overlaying a gradient scrim and text details inside a `Column`.
official-references:
- Layouts in Compose | https://developer.android.com/develop/ui/compose/layouts/basics
common-wrong-beliefs:
- `Box` positions its children sequentially side-by-side just like a `Row` | `Box` stacks children on top of each other in overlapping layers (z-index order).

## ui/layouts/alignment-and-arrangement
interview-questions:
- [intern] How do you align items within a `Column` or `Row` (alignment vs arrangement)?
- [junior] What is the difference between `Arrangement.SpaceBetween`, `Arrangement.SpaceAround`, and `Arrangement.SpaceEvenly`?
- [mid] How do `Alignment.Horizontal`, `Alignment.Vertical`, and `Alignment.Center` position children?
- [senior] What goes wrong in production when mixing conflicting alignment parameters on parent layout containers versus child modifiers?
what-interviewers-listen-for:
- Arrangement governs spacing along the main axis; Alignment governs positioning along the cross axis.
- `Alignment.Companion` modifiers can also be applied to specific children (`Modifier.align()`).
build-exercise: Create a `Row` with three items, setting `horizontalArrangement = Arrangement.SpaceBetween` and `verticalAlignment = Alignment.CenterVertically`, and observe positioning.
official-references:
- Column, Row, and Box layouts | https://developer.android.com/develop/ui/compose/layouts/basics#column-row-box
common-wrong-beliefs:
- Arrangement applies to the cross axis while alignment applies to the main axis | Arrangement controls main axis distribution; alignment controls cross axis placement.

## ui/layouts/weight-and-space
interview-questions:
- [intern] How do you make one item take up all leftover space inside a `Row` or `Column` using `Modifier.weight`?
- [junior] How do weight distribution percentages work when multiple weighted items share a container?
- [mid] What is the difference between `fillMaxWidth` and `weight(1f)` inside linear layouts?
- [senior] What goes wrong in production when using `weight` inside unconstrained containers where measuring passes require multiple passes?
what-interviewers-listen-for:
- `Modifier.weight(fill = true)` distributes remaining flexible space proportionally among weighted children.
- Unconstrained containers cause IllegalStateException or infinite measurement loops when weighted children demand dynamic sizing.
build-exercise: Build a `Row` with a fixed-width icon on the left, a weighted `Text` in the middle (`Modifier.weight(1f)`), and a fixed button on the right.
official-references:
- Row and Column scopes | https://developer.android.com/develop/ui/compose/layouts/basics#weight
common-wrong-beliefs:
- `Modifier.weight()` can be used inside any arbitrary container like a `Box` or `ConstraintLayout` | `Modifier.weight` is a scoped modifier restricted exclusively to `RowScope` and `ColumnScope`.

## ui/layouts/scaffold-and-slots
interview-questions:
- [intern] What is a `Scaffold` in Jetpack Compose, and what standard material design slots does it provide?
- [junior] What is the slot API pattern (`content: @Composable () -> Unit`), and why is it powerful for reusable components?
- [mid] How do `TopAppBar`, `FloatingActionButton`, and `SnackbarHost` coordinate inside a `Scaffold` layout?
- [senior] What goes wrong in production when passing complex state dependencies directly into reusable slot-based composables, coupling components tightly?
what-interviewers-listen-for:
- Scaffold provides top-level layout structure for Material Design apps (top bars, bottom bars, snackbars).
- Slot APIs enable high reusability by injecting lambda composable content blocks into container skeletons.
build-exercise: Build a screen using `Scaffold` with a `TopAppBar`, a `FloatingActionButton`, and a `SnackbarHost` driven by a `SnackbarHostState`.
official-references:
- Material components and layouts | https://developer.android.com/develop/ui/compose/components/scaffold
common-wrong-beliefs:
- A Scaffold is mandatory for every single screen you build in Jetpack Compose | Scaffold is an optional helper container for Material Design layouts; simple screens can be built with basic `Box` or `Column` layouts.

## ui/state-and-recomposition/what-is-recomposition
interview-questions:
- [intern] What is recomposition in Jetpack Compose, and when does it trigger?
- [junior] How does the Compose runtime track state reads inside Composable functions to determine when to recompose?
- [mid] What are the three phases of a Compose frame (Composition -> Layout -> Drawing)?
- [senior] What goes wrong in production when high-frequency state updates (like scroll offsets or sensor values) trigger unnecessary full recompositions?
what-interviewers-listen-for:
- Recomposition re-executes Composable functions when their read state inputs change.
- Three phases: Composition (what to show) -> Layout (where to place) -> Drawing (how to render). Modifying state that only affects Drawing avoids full recomposition.
build-exercise: Log a statement inside a Composable function body, update an unread state vs a read state, and observe Logcat to see when recomposition executes.
official-references:
- Jetpack Compose phases | https://developer.android.com/develop/ui/composephased
common-wrong-beliefs:
- Changing any state variable anywhere in your app causes the entire UI tree to recompose from the root | Compose intelligently tracks state reads and skips recomposing child branches whose state inputs have not changed.

## ui/state-and-recomposition/remember
interview-questions:
- [intern] What does `remember` do in Jetpack Compose, and why is it needed across recompositions?
- [junior] What happens to a variable stored with `remember` when the Composable leaves the Composition and re-enters?
- [mid] Why does `remember` not survive configuration changes (like screen rotation)?
- [senior] What goes wrong in production when complex initialization logic inside `remember { ... }` executes on every recomposition instead of using initializers?
what-interviewers-listen-for:
- `remember` stores state values across recompositions during the Composable's lifetime in the composition tree.
- It does not survive configuration changes or process death; use `rememberSaveable` or `ViewModel` for persistence.
build-exercise: Create a counter using a plain local `var count = 0` vs `val count = remember { mutableStateOf(0) }`, click to increment, and observe why plain variables reset on recomposition.
official-references:
- State and Jetpack Compose | https://developer.android.com/develop/ui/compose/state
common-wrong-beliefs:
- Values stored with `remember` survive screen rotation and configuration changes | `remember` only survives recompositions; rotation destroys the Activity and resets all `remember` states.

## ui/state-and-recomposition/remember-saveable
interview-questions:
- [intern] What is the difference between `remember` and `rememberSaveable`?
- [junior] What types of objects can `rememberSaveable` store across configuration changes and process death?
- [mid] How does `rememberSaveable` integrate with `SavedStateHandle` and Android Bundles under the hood?
- [senior] What goes wrong in production when storing custom non-parcelable, non-serializable objects inside `rememberSaveable`?
what-interviewers-listen-for:
- `rememberSaveable` preserves state across configuration changes and process death by saving into a Bundle.
- Objects must be primitive, Parcelable, Serializable, or backed by a custom `Saver`.
build-exercise: Type text into a TextField using `rememberSaveable`, rotate the screen, and verify the text is preserved.
official-references:
- Restoring state in Compose | https://developer.android.com/develop/ui/compose/state-saving
common-wrong-beliefs:
- `rememberSaveable` can serialize and save any arbitrary complex Kotlin object graph without custom adapters | Objects must be primitive, Parcelable, Serializable, or registered with a custom `Saver` to be stored in the Bundle.

## ui/state-and-recomposition/state-with-lists
interview-questions:
- [intern] Why does mutating items inside a `MutableList` stored in `remember { mutableStateOf(...) }` sometimes fail to trigger recomposition?
- [junior] What is the difference between mutating a list reference versus reassigning or using Compose's `SnapshotStateList`?
- [mid] How does `SnapshotStateList` track individual element additions and removals under the hood?
- [senior] What goes wrong in production when modifying list elements in place without triggering snapshot notifications, causing stale UI lists?
what-interviewers-listen-for:
- `mutableStateOf(mutableListOf())` holds a reference to the list object; mutating items inside the list does not change the reference, so Compose doesn't detect a state change.
- `mutableStateListOf()` or reassigning a new list instance triggers recomposition properly.
build-exercise: Create a list state using `mutableStateOf(mutableListOf())`, add an item, and observe the UI failing to update; then fix it using `mutableStateListOf()`.
official-references:
- State and Jetpack Compose | https://developer.android.com/develop/ui/compose/state#state-in-other-types
common-wrong-beliefs:
- Calling `myList.add(newItem)` on a standard `mutableListOf` wrapped in `mutableStateOf` automatically triggers UI recomposition | Standard lists are not observable by Compose snapshots; you must use `mutableStateListOf()` or assign a brand-new list instance.

## ui/state-and-recomposition/state-hoisting
interview-questions:
- [intern] What is state hoisting, and why is it a core architectural pattern in Jetpack Compose?
- [junior] What is the difference between a stateful composable and a stateless composable?
- [mid] How do state lambdas (`value: T, onValueChange: (T) -> Unit`) enable unidirectional data flow (UDF) in UI components?
- [senior] What goes wrong in production when leaf composables manage their own internal state instead of hoisting state to ViewModels or parents?
what-interviewers-listen-for:
- State hoisting moves state out of a composable to make it stateless, testable, and reusable.
- Stateless composables accept value and event lambdas (`value` + `onValueChange`), implementing One-Way Data Flow.
build-exercise: Refactor a stateful text input card holding internal state into a stateless component accepting `text` and `onTextChanged` parameters.
official-references:
- State hoisting in Compose | https://developer.android.com/develop/ui/compose/state#state-hoisting
common-wrong-beliefs:
- Reusable UI components should always fetch and manage their own data and state internally to keep parent screens clean | Reusable components should be stateless and hoist their state upward so parents and ViewModels control data flow.

## ui/state-and-recomposition/remember-with-keys
interview-questions:
- [intern] What is the purpose of the `key` parameter in `remember(key1, key2)`?
- [junior] When does a `remember` block recalculate its initializer lambda based on key changes?
- [mid] How does `key` scoping prevent stale cached states when recycling or switching items in lists?
- [senior] What goes wrong in production when omitting keys in lazy list items or stateful sub-composables leads to cross-contamination of user input across rows?
what-interviewers-listen-for:
- Keys invalidate the remembered value and re-run the initializer whenever any key parameter changes.
- In lists, keys ensure state stays pinned to the correct item identity during reordering or deletion.
build-exercise: Create a remembered counter keyed to a user ID string (`remember(userId) { mutableStateOf(0) }`), change the ID, and observe the counter reset.
official-references:
- Restoring state in Compose | https://developer.android.com/develop/ui/compose/state-saving#keys
common-wrong-beliefs:
- `remember` without keys never resets its value throughout the entire lifecycle of the Composable | If inputs change and are passed as keys to `remember(key)`, the stored value is invalidated and recalculated.

## ui/lists/lazy-column
interview-questions:
- [intern] How do you display a long list of items efficiently in Jetpack Compose using `LazyColumn` and `LazyRow`?
- [junior] Why should you never use a standard `Column` with a `forEach` loop for long or infinite lists?
- [mid] How does `LazyColumn` virtualize items under the hood (recycling and composing only visible items)?
- [senior] What goes wrong in production when nesting a `LazyColumn` directly inside another vertical scrollable `Column` without height constraints?
what-interviewers-listen-for:
- `LazyColumn` only composes and lays out currently visible items (virtualization), preventing memory bloat.
- Nesting unbounded scrollables (`Column(Modifier.verticalScroll)` enclosing a `LazyColumn`) crashes with an `IllegalStateException` because height measurement becomes unconstrained.
build-exercise: Build a `LazyColumn` rendering 1,000 items, and verify in the Layout Inspector that only visible rows are active in the composition tree.
official-references:
- Lists and grids in Compose | https://developer.android.com/develop/ui/compose/lists
common-wrong-beliefs:
- Placing a `LazyColumn` inside a regular vertically scrollable `Column` is a valid way to create a multi-section scrolling screen | Nesting scrollables of the same orientation throws an exception; use `items` inside a single `LazyColumn` instead.

## ui/lists/list-keys
interview-questions:
- [intern] Why does providing a `key` parameter to items in a `LazyColumn` (`items(items, key = { it.id })`) matter?
- [junior] What happens when you delete, insert, or reorder items in a `LazyColumn` that lacks item keys?
- [mid] How do stable item keys prevent UI state bugs (like checkboxes checking the wrong row) during list mutations?
- [senior] What goes wrong in production when using volatile list indices as keys instead of unique database IDs?
what-interviewers-listen-for:
- Item keys give the Compose runtime stable identities for list items.
- Without keys, items rely on position index; inserting an item shifts indices, attaching old UI state to the wrong data item.
- Using item indices as keys defeats recycling optimization when items reorder.
build-exercise: Build a list with interactive checkboxes, delete the first item *without* providing a `key`, and observe how checkbox states mismatch rows; then add unique keys to fix it.
official-references:
- Lazy list item keys | https://developer.android.com/develop/ui/compose/lists#item-keys
common-wrong-beliefs:
- Using the list item's position index (`index`) as the `key` in `LazyColumn` is fully recommended for best performance | Using indices as keys breaks item state retention during reordering and insertion; stable unique identifiers (`id`) must be used.

## ui/lists/lazy-grid
interview-questions:
- [intern] How do you display items in a multi-column grid using `LazyVerticalGrid` and `LazyHorizontalGrid`?
- [junior] What is the difference between `GridCells.Fixed(count)` and `GridCells.Adaptive(minSize)`?
- [mid] How do spans (`Span` / `maxLineSpan`) allow specific grid items to occupy multiple columns?
- [senior] What goes wrong in production when lazy grids contain items with wildly varying aspect ratios without proper staggered layout support?
what-interviewers-listen-for:
- `LazyVerticalGrid` lays out items in columns with fixed or adaptive cell width rules.
- `GridCells.Adaptive` calculates column counts dynamically based on screen width.
build-exercise: Build a photo gallery grid using `LazyVerticalGrid` with `GridCells.Adaptive(120.dp)` and render items.
official-references:
- Grids in Compose | https://developer.android.com/develop/ui/compose/lists#grids
common-wrong-beliefs:
- `LazyVerticalGrid` requires you to manually calculate column counts based on device screen rotation changes | `GridCells.Adaptive` computes column counts automatically based on available screen width.

## ui/theming/material-theme
interview-questions:
- [intern] What is `MaterialTheme` in Jetpack Compose, and what three pillars does it establish (ColorScheme, Typography, Shapes)?
- [junior] How do you customize and wrap your app content inside a custom `MaterialTheme` wrapper?
- [mid] How does Material 3 dynamic color (`dynamicLightColorScheme` / `dynamicDarkColorScheme`) extract colors from Android 12+ device wallpapers?
- [senior] What goes wrong in production when hardcoding raw hex colors instead of referencing theme tokens across large codebases?
what-interviewers-listen-for:
- MaterialTheme configures design tokens (`colorScheme`, `typography`, `shapes`) for the entire subtree.
- Dynamic color adapts app color palettes to user wallpaper themes on Android 12+.
- Hardcoding raw colors breaks dark mode support and design system consistency.
build-exercise: Create a custom `MaterialTheme` defining a custom primary color and typography style, wrap a screen in it, and verify child composables inherit the tokens.
official-references:
- Material 3 in Compose | https://developer.android.com/develop/ui/compose/designsystems/material3
common-wrong-beliefs:
- Material 3 dynamic color works automatically on all Android device versions out of the box | Dynamic color requires Android 12 (API 31)+ and supported device manufacturers.

## ui/theming/using-theme-tokens
interview-questions:
- [intern] Why should you write `MaterialTheme.colorScheme.primary` instead of hardcoding `Color.Blue`?
- [junior] How do theme tokens enable seamless light mode and dark mode switching automatically?
- [mid] What are surface colors (`Surface`, `onSurface`, `background`, `onBackground`), and why are `on*` colors critical for contrast?
- [senior] What goes wrong in production when custom composables ignore theme color tokens, rendering illegible text in dark mode?
what-interviewers-listen-for:
- Theme tokens point to dynamic design system variables that switch values based on light/dark theme context.
- `on*` colors guarantee accessible contrast ratios against their paired background surfaces.
build-exercise: Create a text element using `MaterialTheme.colorScheme.onSurface` inside a `Surface` container, toggle dark mode in preview, and observe contrast adaptation.
official-references:
- Material 3 color tokens | https://developer.android.com/develop/ui/compose/designsystems/material3#color
common-wrong-beliefs:
- Hardcoding `Color.White` for text and `Color.Black` for backgrounds ensures perfect light and dark mode compatibility | Hardcoding colors breaks dark mode where backgrounds become dark and text must become light.

## ui/theming/matching-system-look
interview-questions:
- [intern] How does a Compose app detect and follow the system-wide dark mode setting?
- [junior] What is the difference between `isSystemInDarkTheme()` and forcing a manual light/dark theme toggle?
- [mid] How do configuration changes in system appearance trigger recomposition across themed composables?
- [senior] What goes wrong in production when custom system UI bars (status bar / navigation bar) fail to match theme contrast settings?
what-interviewers-listen-for:
- `isSystemInDarkTheme()` queries system UI configuration flags.
- Allowing user preference override requires hoisting dark mode state in app settings.
build-exercise: Wrap app content in `MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme())` and toggle device dark mode.
official-references:
- Dark theme in Compose | https://developer.android.com/develop/ui/compose/designsystems/material3#dark-theme
common-wrong-beliefs:
- Jetpack Compose automatically switches between light and dark color schemes without checking `isSystemInDarkTheme()` or providing conditional color schemes | You must supply both light and dark `ColorScheme` configurations to `MaterialTheme`.

## ui/navigation/what-is-navigation
interview-questions:
- [intern] How does navigation work in a Jetpack Compose application using Navigation Compose?
- [junior] What is a `NavHost`, a `NavController`, and a route string?
- [mid] How does Navigation Compose manage the back stack and screen transitions declaratively?
- [senior] What goes wrong in production when navigation route definitions use loose string concatenation instead of type-safe Kotlin serialization routes?
what-interviewers-listen-for:
- `NavHost` links a `NavController` to a navigation graph defined by routes.
- Type-safe navigation (introduced in Navigation 2.8.0 via Kotlin serialization) replaces brittle string routes with typed objects.
build-exercise: Set up a `NavHost` with two destinations (Home and Details) using `NavController`, and trigger navigation on button click.
official-references:
- Navigation with Compose | https://developer.android.com/guide/navigation/design
common-wrong-beliefs:
- Navigation Compose requires creating multiple separate Activities or Fragments for each screen | Navigation Compose swaps out Composable destinations within a single Activity host container.

## ui/navigation/passing-arguments
interview-questions:
- [intern] How do you pass arguments (like an item ID) from one screen to the next in Navigation Compose?
- [junior] What is the difference between path arguments (`"{itemId}"`) and query parameters (`"?name={name}"`)?
- [mid] How does type-safe navigation with Kotlin serialization pass complex objects or primitives as route arguments?
- [senior] What goes wrong in production when passing massive data objects through navigation route strings instead of passing IDs and fetching from a repository?
what-interviewers-listen-for:
- Arguments are defined in route templates and extracted via `NavBackStackEntry`.
- Passing large objects through navigation arguments causes Bundle size limit overflows; pass IDs instead.
- Type-safe navigation serializes objects into route parameters cleanly.
build-exercise: Define a route with an integer argument (`"details/{id}"`), pass an ID from screen A, and retrieve it in screen B using `NavBackStackEntry.arguments`.
official-references:
- Type-safe navigation | https://developer.android.com/guide/navigation/type-safe
common-wrong-beliefs:
- You can pass large, complex data class objects directly through navigation route arguments without serialization | Navigation arguments pass through Bundles and strings; pass unique IDs and load details from a local database or repository.

## ui/navigation/back-navigation
interview-questions:
- [intern] How do you handle back navigation and pop up the back stack in Navigation Compose (`navController.popBackStack()`)?
- [junior] How do you clear screens from the back stack (e.g., popping login screens after successful authentication)?
- [mid] What is the difference between `popUpTo` and `inclusive = true` when navigating forward?
- [senior] What goes wrong in production when back stack pop behaviors leave authentication or onboarding screens accessible via the hardware back button?
what-interviewers-listen-for:
- `popBackStack()` navigates backward.
- `popUpTo(route) { inclusive = true }` clears intermediate destinations from the back stack, preventing users from navigating back to login/splash screens.
build-exercise: Navigate from Login -> Dashboard, and use `popUpTo("login") { inclusive = true }` to prevent returning to Login on back press.
official-references:
- Navigation pop up to | https://developer.android.com/guide/navigation/navigation-conditional
common-wrong-beliefs:
- Pressing the hardware back button always pops the immediate previous screen without any custom back stack configuration | You can configure custom `popUpTo` rules to skip authentication or onboarding screens when popping back.

## ui/side-effects/why-side-effects
interview-questions:
- [intern] What is a side effect in Jetpack Compose, and why must Composable functions remain pure?
- [junior] What happens if you perform a network request or database write directly inside the body of a Composable function?
- [mid] Why does Compose provide dedicated Effect APIs (`LaunchedEffect`, `DisposableEffect`) instead of allowing raw side effects during composition?
- [senior] What goes wrong in production when side effects are executed in composition blocks that run multiple times unpredictably?
what-interviewers-listen-for:
- Side effects modify state outside the scope of the Composable (network calls, logging, analytics).
- Because recomposition can re-run or cancel functions at any time, raw side effects inside composable bodies execute multiple times unexpectedly.
- Effect APIs tie side effects to lifecycle events securely.
build-exercise: Place a `println` or network call directly inside a Composable body, rotate the screen twice, and observe it execute multiple times unexpectedly.
official-references:
- Side-effects in Compose | https://developer.android.com/develop/ui/compose/side-effects
common-wrong-beliefs:
- Putting a network call directly inside a Composable function body ensures it fetches data once when the screen opens | Recomposition causes Composable bodies to re-run frequently, triggering duplicate network calls.

## ui/side-effects/launched-effect
interview-questions:
- [intern] What does `LaunchedEffect` do in Jetpack Compose, and when does its coroutine start?
- [junior] How do keys passed to `LaunchedEffect(key1)` control when the coroutine is cancelled and restarted?
- [mid] When should you use `LaunchedEffect` vs triggering work from a ViewModel?
- [senior] What goes wrong in production when passing unstable objects or changing keys to `LaunchedEffect` causes coroutines to restart on every keystroke?
what-interviewers-listen-for:
- `LaunchedEffect` runs a coroutine scoped to the Composable's appearance in the composition.
- Changing key parameters cancels the existing coroutine and launches a new one.
- Side effects like fetching screen data or showing one-off snackbars belong in `LaunchedEffect`.
build-exercise: Use `LaunchedEffect(Unit)` to trigger a coroutine showing a welcome Toast exactly once when a screen enters the composition.
official-references:
- LaunchedEffect API | https://developer.android.com/develop/ui/compose/side-effects#launchedeffect
common-wrong-beliefs:
- `LaunchedEffect` runs every single time a Composable recomposes | `LaunchedEffect` only restarts its coroutine if its key parameters change, not on normal recompositions.

## ui/side-effects/coroutine-from-a-click
interview-questions:
- [intern] How do you start a coroutine from inside a button click listener or event callback in Compose?
- [junior] What is `rememberCoroutineScope()`, and why can't you call `LaunchedEffect` or `suspend` functions directly inside a click lambda?
- [mid] When is `rememberCoroutineScope()` required versus using `LaunchedEffect`?
- [senior] What goes wrong in production when coroutines launched from click scopes outlive the screen without proper cancellation?
what-interviewers-listen-for:
- Click listeners are normal lambdas, not composable scopes, so they require `rememberCoroutineScope()` to obtain a coroutine scope.
- `rememberCoroutineScope()` binds the scope to the composition lifetime, cancelling running jobs when the composable leaves.
build-exercise: Obtain a scope via `val scope = rememberCoroutineScope()`, and trigger a delayed background action inside a button click `onClick = { scope.launch { ... } }`.
official-references:
- rememberCoroutineScope | https://developer.android.com/develop/ui/compose/side-effects#remembercoroutinescope
common-wrong-beliefs:
- You can call `launch` or suspend functions directly inside a button's `onClick` lambda without a coroutine scope | `onClick` is a regular lambda; you must use `rememberCoroutineScope()` to launch coroutines from user interactions.

## ui/side-effects/disposable-effect
interview-questions:
- [intern] What is `DisposableEffect`, and when do you need its cleanup block (`onDispose`)?
- [junior] How does `DisposableEffect` differ from `LaunchedEffect`?
- [mid] When should you use `DisposableEffect` (e.g., registering lifecycle observers, location updates, sensor listeners)?
- [senior] What goes wrong in production when failing to unregister listeners or observers inside `onDispose`, causing memory leaks?
what-interviewers-listen-for:
- `DisposableEffect` manages side effects that require cleanup when the composable leaves composition or keys change.
- The `onDispose` block guarantees cleanup execution, preventing resource and listener leaks.
build-exercise: Register an event listener or observer inside `DisposableEffect` and unregister it inside `onDispose`.
official-references:
- DisposableEffect API | https://developer.android.com/develop/ui/compose/side-effects#disposableeffect
common-wrong-beliefs:
- `LaunchedEffect` can handle resource cleanup just as well as `DisposableEffect` | `LaunchedEffect` only handles coroutine cancellation; `DisposableEffect` is required for non-coroutine resource unregistration and cleanup.

## ui/side-effects/collecting-flows
interview-questions:
- [intern] How do you collect a Kotlin Flow inside a Jetpack Compose screen?
- [junior] What is `collectAsStateWithLifecycle()`, and why is it preferred over raw `collectAsState()`?
- [mid] How does `collectAsStateWithLifecycle()` pause flow collection when the app goes into the background (`ON_STOP`)?
- [senior] What goes wrong in production when using raw `collectAsState()` with cold repository flows while the app is backgrounded?
what-interviewers-listen-for:
- `collectAsStateWithLifecycle()` collects flows safely by tying collection strictly to lifecycle states (`STARTED`).
- Raw `collectAsState()` continues collecting in the background (`ON_STOP`), wasting battery and risking crashes on UI updates.
build-exercise: Collect a ViewModel `StateFlow` inside a Composable using `collectAsStateWithLifecycle()`, push the app to the background, and verify upstream producers pause.
official-references:
- Consume flows safely in Jetpack Compose | https://developer.android.com/kotlin/flow/test-and-consume#collectasstatewithlifecycle
common-wrong-beliefs:
- `collectAsState()` automatically pauses collection when the user presses the Home button and puts the app in the background | `collectAsState()` keeps collecting in the background; you must use `collectAsStateWithLifecycle()` to pause collection during `ON_STOP`.

## ui/forms-and-input/text-field
interview-questions:
- [intern] Why is `TextField` in Jetpack Compose a stateless component (`value` + `onValueChange`)?
- [junior] How do you implement state hoisting for text input fields?
- [mid] What is the difference between `TextField` (Filled) and `OutlinedTextField`?
- [senior] What goes wrong in production when text fields store their input state internally rather than hoisting it to a ViewModel or parent?
what-interviewers-listen-for:
- `TextField` is stateless by design, requiring explicit state hoisting so parents and ViewModels control input value changes.
- Internal state storage prevents parents from reading or validating input values cleanly.
build-exercise: Build a stateful screen holding `var text by rememberSaveable { mutableStateOf("") }` and pass it to an `OutlinedTextField` with `onValueChange = { text = it }`.
official-references:
- Text fields in Compose | https://developer.android.com/develop/ui/compose/text/text-fields
common-wrong-beliefs:
- Compose `TextField` manages its typed text string internally by default without requiring an `onValueChange` state update | `TextField` is stateless; if you do not update the `value` parameter inside `onValueChange`, typing characters into the field will have no effect.

## ui/forms-and-input/keyboard-options
interview-questions:
- [intern] How do you configure the software keyboard type (e.g., number pad, email address) in a Compose `TextField`?
- [junior] How do `KeyboardOptions` and `KeyboardActions` control action buttons like "Done", "Next", or "Search"?
- [mid] How do you manage software keyboard visibility programmatically using `SoftwareKeyboardController`?
- [senior] What goes wrong in production when keyboard action buttons fail to clear focus or hide the keyboard on submission?
what-interviewers-listen-for:
- `KeyboardOptions` configures keyboard type, capitalization, and IME action buttons.
- `KeyboardActions` defines callbacks executed when the user taps the IME action button.
build-exercise: Configure a `TextField` with `KeyboardType.Number` and an IME action `ImeAction.Done` that hides the keyboard on click.
official-references:
- Keyboard actions in Compose | https://developer.android.com/develop/ui/compose/text/user-input
common-wrong-beliefs:
- The software keyboard closes automatically whenever the user taps outside a text field without manual focus clearing | Tapping outside does not automatically clear focus or dismiss the keyboard; explicit focus management or keyboard controller calls are required.

## ui/forms-and-input/validating-input
interview-questions:
- [intern] How do you validate user input in a Compose form (e.g., checking email format or password length)?
- [junior] How do you display error states and error messages on an `OutlinedTextField` (`isError = true`, `supportingText`)?
- [mid] What is the architectural pattern for managing multi-field form validation state in ViewModels?
- [senior] What goes wrong in production when validating input synchronously on every keystroke for expensive regex or database checks?
what-interviewers-listen-for:
- `OutlinedTextField` supports `isError` flags and `supportingText` slots for error feedback.
- Heavy validation checks should be debounced to prevent UI jank during fast typing.
build-exercise: Create an email input field that flags `isError = true` and shows an error message when the input lacks an `@` symbol.
official-references:
- TextField error states | https://developer.android.com/develop/ui/compose/text/text-fields#states
common-wrong-beliefs:
- Input validation logic must be executed inside the Composable rendering function itself | Validation logic belongs in the ViewModel or domain layer, keeping the UI clean and testable.

## ui/forms-and-input/choice-controls
interview-questions:
- [intern] How do you implement checkboxes, radio buttons, and switches in Jetpack Compose?
- [junior] How do you build a selection group using radio buttons with proper state hoisting?
- [mid] Why do Material 3 choice controls wrap elements inside clickable rows rather than relying solely on the tiny toggle control?
- [senior] What goes wrong in production when choice control click targets are too small for comfortable touch accessibility?
what-interviewers-listen-for:
- Checkbox, RadioButton, and Switch represent binary or grouped selections.
- Wrapping the toggle control and label inside a parent row with `.selectable()` or `.clickable()` ensures a large touch target.
build-exercise: Build a settings row with a `Switch` and a text label where tapping anywhere on the row toggles the switch state.
official-references:
- Selection controls in Compose | https://developer.android.com/develop/ui/compose/components/switch
common-wrong-beliefs:
- Users should tap directly on the tiny thumb or box of a switch or checkbox to toggle it | Touch accessibility guidelines dictate that the entire surrounding row should be clickable, not just the tiny control.

## ui/animation/animate-as-state
interview-questions:
- [intern] How do you animate a single property (like size, color, or alpha) smoothly using `animate*AsState`?
- [junior] What is the difference between `animateDpAsState`, `animateColorAsState`, and `animateFloatAsState`?
- [mid] How do spring animations and tween specs customize animation physics and duration?
- [senior] What goes wrong in production when animating layout size properties (`Modifier.size`) constantly, triggering expensive re-measurement passes?
what-interviewers-listen-for:
- `animate*AsState` creates smooth value transitions whenever target state changes.
- Animating layout-affecting modifiers (`size`, `padding`) triggers layout measurement passes on every frame, risking jank; animating drawing modifiers (`scale`, `alpha`, `offset`) is faster.
build-exercise: Animate a box's background color from blue to red using `animateColorAsState` on a button click.
official-references:
- Animation in Compose | https://developer.android.com/develop/ui/compose/animation/oss-animations
common-wrong-beliefs:
- Animating layout size modifiers (`Modifier.size`) is just as performant as animating drawing modifiers (`Modifier.graphicsLayer`) | Layout size changes trigger re-measurement and re-layout passes on every frame, whereas `graphicsLayer` animations run purely in the drawing phase without layout passes.

## ui/animation/animated-visibility
interview-questions:
- [intern] How do you make a Composable appear and disappear smoothly using `AnimatedVisibility`?
- [junior] How do enter and exit transitions (`fadeIn`, `slideInVertically`, `expandVertically`) customize `AnimatedVisibility` behavior?
- [mid] How does `AnimatedVisibility` optimize composition by unmounting hidden composables from the tree?
- [senior] What goes wrong in production when animating complex entry/exit transitions on large screen trees without proper layout bounds constraints?
what-interviewers-listen-for:
- `AnimatedVisibility` animates content entry and exit while removing invisible nodes from the composition tree.
- Custom enter/exit transition specs combine alpha, scale, and slide effects.
build-exercise: Wrap a card in `AnimatedVisibility` triggered by a boolean toggle button, observing smooth fade and slide transitions.
official-references:
- AnimatedVisibility | https://developer.android.com/develop/ui/compose/animation/animated-visibility
common-wrong-beliefs:
- `AnimatedVisibility` simply hides composables using `View.GONE` visibility while keeping them fully active in the composition tree | `AnimatedVisibility` unmounts and removes hidden composables from the composition tree when invisible.

## ui/animation/gesture-driven-animation
interview-questions:
- [intern] How do you make a card follow your finger and spring back when released using gesture animations?
- [junior] What is `Animatable`, and how do you use `.animateTo()` inside pointer input gesture detectors?
- [mid] How do anchored draggables and swipe-to-dismiss modifiers work for advanced gestures?
- [senior] What goes wrong in production when gesture animation handlers fail to handle velocity and fling decay smoothly?
what-interviewers-listen-for:
- Pointer input combined with `Animatable` enables physics-based gesture tracking.
- Fling velocities and decay animations create natural momentum after touch release.
build-exercise: Build a draggable card using `Modifier.pointerInput` and `Animatable` that drags across the screen and springs back to center on release.
official-references:
- Gestures and animation | https://developer.android.com/develop/ui/compose/touch-input/pointer-input/gestures
common-wrong-beliefs:
- Gesture-driven animations require complex legacy Android `Scroller` and `GestureDetector` classes inside Compose | Compose provides modern, high-performance gesture detectors and `Animatable` physics APIs natively.

## ui/accessibility/what-a-screen-reader-hears
interview-questions:
- [intern] What does a blind user's screen reader (TalkBack) hear when interacting with your Compose UI?
- [junior] How do you use semantics modifiers (`Modifier.semantics`) to customize screen reader announcements and labels?
- [mid] How do you merge child accessibility nodes or mark custom components as buttons or checkboxes?
- [senior] What goes wrong in production when custom graphical icons or cards lack accessibility semantics, locking out visually impaired users?
what-interviewers-listen-for:
- Semantics trees run parallel to the composition tree, providing accessibility data to screen readers.
- `Modifier.semantics` lets developers define content descriptions, roles, and state descriptions.
build-exercise: Add custom accessibility semantics and content descriptions to a custom clickable image card using `Modifier.semantics`.
official-references:
- Accessibility in Compose | https://developer.android.com/develop/ui/compose/accessibility
common-wrong-beliefs:
- Jetpack Compose automatically generates perfect screen reader announcements for every custom canvas drawing and icon out of the box | Custom graphical elements require explicit semantics annotations so screen readers know what they represent.

## ui/accessibility/big-enough-to-tap
interview-questions:
- [intern] What are the official minimum touch target size guidelines for interactive elements in Android apps?
- [junior] Why is a tiny 24x24dp icon button hard to tap for users with motor impairments?
- [mid] How do you expand touch target bounds without altering visual layout size using `Modifier.minimumInteractiveComponentSize()` or padding?
- [senior] What goes wrong in production when failing accessibility touch target audits, leading to Play Store compliance flags?
what-interviewers-listen-for:
- Minimum touch target size is 48x48dp (or 48dp diameter).
- Expanding touch targets without inflating visual bounds is achieved using `minimumInteractiveComponentSize()`.
build-exercise: Wrap a small 24x24dp icon button with `Modifier.minimumInteractiveComponentSize()` to satisfy 48dp touch target requirements without visual distortion.
official-references:
- Touch target sizes | https://developer.android.com/develop/ui/compose/accessibility#touch-targets
common-wrong-beliefs:
- Making a clickable visual button icon 24x24dp on screen is sufficient as long as it looks clean | Accessibility guidelines require interactive touch targets to be at least 48x48dp to accommodate users with motor impairments.

## ui/accessibility/font-scaling
interview-questions:
- [intern] What happens to your Composable UI layout when a user increases the system font size in accessibility settings?
- [junior] Why do fixed hardcoded height constraints on text containers cause text clipping when fonts scale up?
- [mid] How do you test your layouts against extreme font scaling (up to 200%+) in Android Studio previews?
- [senior] What goes wrong in production when text views clip or overlap because layouts were designed assuming fixed default font sizes?
what-interviewers-listen-for:
- Users frequently scale system font sizes for readability.
- Hardcoded height constraints clip scaled text; use flexible heights (`wrapContentHeight`, weight) instead.
build-exercise: Preview a text card with system font scale set to 200% in `@Preview` and verify layout resilience against text clipping.
official-references:
- Support font scaling | https://developer.android.com/develop/ui/compose/text/fonts#font-scaling
common-wrong-beliefs:
- Designing layouts with fixed pixel heights (`Modifier.height(40.dp)`) for text rows guarantees consistent visual presentation across all user devices | Fixed heights clip text when users increase system accessibility font sizes; flexible sizing must be used.

## ui/adaptive-layouts/drawing-behind-system-bars
interview-questions:
- [intern] Why do modern Android apps draw content behind the status bar and navigation bar (Edge-to-Edge display)?
- [junior] How do `WindowInsets` and modifiers like `windowInsetsPadding` protect UI content from overlapping system bars?
- [mid] What is the difference between `WindowInsets.statusBars`, `navigationBars`, and `ime` (software keyboard) insets?
- [senior] What goes wrong in production when failing to handle keyboard `ime` insets, causing bottom form fields to be hidden behind the software keyboard?
what-interviewers-listen-for:
- Edge-to-edge rendering utilizes full screen display estate behind system bars.
- `WindowInsets` handle system bar and keyboard padding dynamically.
- Ignoring IME insets hides input fields when keyboards pop up.
build-exercise: Apply `Modifier.imePadding()` to a bottom chat input bar and verify it slides up smoothly when the software keyboard appears.
official-references:
- Edge-to-edge display in Compose | https://developer.android.com/develop/ui/compose/layouts/insets
common-wrong-beliefs:
- Modern Android apps should restrict their layouts strictly inside safe window bounds below the status bar manually | Modern Android enforces edge-to-edge displays where apps must draw behind system bars and manage padding using WindowInsets.

## ui/adaptive-layouts/window-size-classes
interview-questions:
- [intern] What are Window Size Classes (Compact, Medium, Expanded), and how do they help apps adapt to phones, foldables, and tablets?
- [junior] How do you calculate current window size classes using `calculateWindowSizeClass()`?
- [mid] How do you design responsive layouts that switch from a single-pane phone list to a two-pane master-detail tablet layout dynamically?
- [senior] What goes wrong in production when tablet layouts simply stretch phone UI elements across wide screens instead of adapting to multi-pane navigation?
what-interviewers-listen-for:
- Window Size Classes categorize screen widths and heights into standard breakpoints (Compact, Medium, Expanded).
- Responsive layouts adapt navigation and layout structure based on device form factor at runtime.
build-exercise: Write a responsive screen that checks window width size class, rendering a single-pane list on Compact (phone) and a two-pane split view on Expanded (tablet).
official-references:
- Support different screen sizes | https://developer.android.com/develop/ui/compose/layouts/adaptive/window-size-classes
common-wrong-beliefs:
- A UI layout designed for a standard phone screen looks and functions great on large tablets without any form-factor adaptations | Phone layouts stretched across wide tablets leave excessive whitespace; Window Size Classes should be used to build adaptive multi-pane layouts.

## ui/views-and-interop/what-are-views
interview-questions:
- [intern] What were legacy Android Views (`TextView`, `Button`, `LinearLayout`) and XML layouts before Jetpack Compose?
- [junior] How did imperative View inflation work via `LayoutInflater.inflate()`?
- [mid] Why did Google transition from XML-based View hierarchies to declarative Jetpack Compose?
- [senior] What goes wrong in production when legacy custom View subclasses manage complex invalidation and measurement passes incorrectly?
what-interviewers-listen-for:
- Legacy Views used XML inflation and imperative state mutation (`findViewById`, setters).
- Compose replaced XML with pure Kotlin declarative composables, eliminating inflation overhead and state synchronization bugs.
build-exercise: Inspect an old XML layout file to understand how legacy View attributes mapped to Java classes.
official-references:
- Views and XML layout legacy | https://developer.android.com/develop/ui/views/layout/declaring-layout
common-wrong-beliefs:
- XML layouts are still the recommended primary UI framework for building new Android screens today | Jetpack Compose is Google's modern recommended declarative UI framework for all new Android development.

## ui/views-and-interop/reading-xml-layouts
interview-questions:
- [intern] How do you read and understand legacy XML layout files when maintaining older codebases?
- [junior] What is the mapping between XML attributes (e.g., `android:layout_margin`) and Compose modifiers?
- [mid] How do ConstraintLayout guidelines and chains translate into Compose layouts?
- [senior] What goes wrong in production when attempting blind automated conversions of complex legacy XML layouts into broken Compose code?
what-interviewers-listen-for:
- Mapping XML attributes to Compose equivalents (`layout_width` -> `Modifier.width`, `padding` -> `Modifier.padding`).
- Understanding legacy View hierarchies helps during gradual code migrations.
build-exercise: Take a simple legacy XML linear layout snippet and manually translate it into an equivalent Compose `Column`.
official-references:
- Migrate from XML to Compose | https://developer.android.com/develop/ui/compose/migrate
common-wrong-beliefs:
- You must rewrite an entire legacy app's XML UI codebase overnight before you can start adopting Jetpack Compose | Compose interoperates seamlessly with Views, allowing gradual screen-by-screen migration.

## ui/views-and-interop/compose-inside-views
interview-questions:
- [intern] How do you embed a modern Compose screen inside an existing legacy app built with traditional XML Views?
- [junior] What is `ComposeView`, and how do you attach it inside an XML layout or legacy Fragment?
- [mid] How do theme wrappers (`MaterialTheme`) propagate when embedding Compose inside View hierarchies?
- [senior] What goes wrong in production when lifecycle owners or SavedStateRegistry are missing in mixed View-Compose embedding hierarchies?
what-interviewers-listen-for:
- `ComposeView` acts as a bridge host allowing Composable content to be inflated inside traditional XML layouts.
- State and theme context must be provided correctly to the embedded ComposeView.
build-exercise: Place a `ComposeView` inside a legacy XML layout file, and call `composeView.setContent { MyComposable() }` inside an Activity.
official-references:
- Compose in View hierarchies | https://developer.android.com/develop/ui/compose/migrate/interoperability/compose-in-views
common-wrong-beliefs:
- You cannot use Jetpack Compose components anywhere inside an app that uses traditional XML layouts and Fragments | `ComposeView` allows embedding Compose UI blocks directly inside existing legacy View hierarchies.

## ui/views-and-interop/views-inside-compose
interview-questions:
- [intern] How do you use a legacy Android View (like `MapView` or `WebView`) inside a Jetpack Compose screen?
- [junior] What is `AndroidView`, and how does it bridge legacy View lifecycles into Compose?
- [mid] How do factory and update lambdas (`factory = { Context -> ... }`, `update = { view -> ... }`) manage legacy View state inside Compose?
- [senior] What goes wrong in production when embedding heavy legacy Views (like MapViews) without managing composition recycling and memory teardown?
what-interviewers-listen-for:
- `AndroidView` hosts legacy View instances inside Composable functions.
- The `factory` lambda creates the view once; the `update` lambda re-runs whenever state changes.
build-exercise: Embed a legacy `WebView` or `TextView` inside a Compose screen using `AndroidView`.
official-references:
- Views in Compose | https://developer.android.com/develop/ui/compose/migrate/interoperability/views-in-compose
common-wrong-beliefs:
- You cannot display legacy custom Views or third-party mapping libraries that lack Compose wrappers inside a Compose app | `AndroidView` allows hosting any traditional Android View directly inside a Compose layout.

## ui/custom-drawing/canvas-basics
interview-questions:
- [intern] How do you draw custom shapes, lines, and arcs in Jetpack Compose using the `Canvas` Composable?
- [junior] What is the difference between drawing via Composables versus custom Canvas drawing commands (`drawRect`, `drawCircle`, `drawPath`)?
- [mid] How do `DrawScope` coordinates, paths, and gradient brushes work for custom graphics?
- [senior] What goes wrong in production when performing heavy calculations or object allocations inside high-frequency Canvas draw lambdas?
what-interviewers-listen-for:
- `Canvas` provides a `DrawScope` rendering context for drawing custom 2D vector shapes and paths.
- Allocating objects inside draw lambdas triggers excessive garbage collection on every frame.
build-exercise: Draw a custom pie chart or progress bar using `Canvas` and `DrawScope` drawing commands.
official-references:
- Graphics in Compose | https://developer.android.com/develop/ui/compose/graphics/draw/canvas
common-wrong-beliefs:
- Allocating new paint objects or path calculators inside a Canvas draw lambda is harmless | Allocating objects inside draw scopes causes heavy garbage collection thrashing during frame rendering; objects should be remembered or cached outside.

## ui/custom-drawing/build-a-custom-control
interview-questions:
- [intern] How do you build a fully custom interactive control from scratch in Jetpack Compose (combining layout, drawing, and gesture input)?
- [junior] What is the difference between custom drawing (`Canvas`) and custom layout (`Layout` composable)?
- [mid] How do custom `Layout` composables measure and place child elements manually using measurement passes?
- [senior] What goes wrong in production when custom layout measurement logic violates Compose measurement constraint rules?
what-interviewers-listen-for:
- Custom layouts use the `Layout` composable to measure and place children manually.
- Custom controls combine gesture detection (`pointerInput`), custom drawing (`Canvas`), and state hoisting.
build-exercise: Build a custom interactive range slider or rating star bar using `Canvas` and pointer input gesture detectors.
official-references:
- Custom layouts in Compose | https://developer.android.com/develop/ui/compose/layouts/custom
common-wrong-beliefs:
- Building custom UI controls in Compose requires writing complex legacy `View` measurement and `onDraw` subclasses | Compose provides `Layout` and `Canvas` composables to build custom controls natively in Kotlin.

## Self-check
1. Jetpack Compose thinking | https://developer.android.com/develop/ui/compose/mental-model (Verify exact URL path)
2. Jetpack Compose programming model | https://developer.android.com/develop/ui/compose/mental-model#compose-compiler (Verify URL anchors)
3. Images in Compose | https://developer.android.com/develop/ui/compose/graphics/images/loading (Verify path)
4. Preview your UI with composables | https://developer.android.com/develop/ui/compose/tooling/previews (Verify path)
5. Modifiers in Compose | https://developer.android.com/develop/ui/compose/modifiers (Verify path)
6. Layouts in Compose | https://developer.android.com/develop/ui/compose/layouts/basics (Verify path)
7. Jetpack Compose phases | https://developer.android.com/develop/ui/composephased (Verify exact URL path)
8. State and Jetpack Compose | https://developer.android.com/develop/ui/compose/state (Verify path)
9. Lists and grids in Compose | https://developer.android.com/develop/ui/compose/lists (Verify path)
10. Material 3 in Compose | https://developer.android.com/develop/ui/compose/designsystems/material3 (Verify path)