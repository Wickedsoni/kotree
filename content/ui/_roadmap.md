```yaml
branch: ui
bough: kotlin-android
stems:
  - id: first-composable
    title: First Composable
    why: A fresher must see how a Kotlin function becomes something on the screen before anything else in UI makes sense.
    leaves:
      - id: what-is-declarative-ui
        title: "Why does Compose describe the screen instead of changing it step by step?"
        difficulty: medium
        requires: []
        fruit: false
      - id: what-is-a-composable
        title: "What makes a Kotlin function a composable?"
        difficulty: easy
        requires: [ui/first-composable/what-is-declarative-ui]
        fruit: false
      - id: text-and-images
        title: "How do I put words and pictures on the screen?"
        difficulty: easy
        requires: [ui/first-composable/what-is-a-composable]
        fruit: false
      - id: previews
        title: "How can I see my screen without running the app?"
        difficulty: easy
        requires: [ui/first-composable/what-is-a-composable]
        fruit: false
  - id: modifiers
    title: Modifiers
    why: Size, spacing, colour and clicks all come from modifiers, and their order changes the result.
    leaves:
      - id: what-is-a-modifier
        title: "How do I give a composable a size, padding or a background?"
        difficulty: easy
        requires: [ui/first-composable/text-and-images]
        fruit: false
      - id: modifier-order
        title: "Why does the order of modifiers change what I see?"
        difficulty: medium
        requires: [ui/modifiers/what-is-a-modifier]
        fruit: false
      - id: reacting-to-taps
        title: "How does a composable react when I tap it?"
        difficulty: easy
        requires: [ui/modifiers/what-is-a-modifier]
        fruit: false
  - id: layouts
    title: Layouts
    why: Every screen is boxes placed in rows, columns and layers, plus a standard frame with bars.
    leaves:
      - id: column-row-box
        title: "How do I place things above, beside or on top of each other?"
        difficulty: easy
        requires: [ui/first-composable/text-and-images, ui/modifiers/what-is-a-modifier]
        fruit: false
      - id: alignment-and-arrangement
        title: "How do I push items to the centre, to the edges or spread them out?"
        difficulty: medium
        requires: [ui/layouts/column-row-box]
        fruit: false
      - id: weight-and-space
        title: "How do I make one item take all the leftover space?"
        difficulty: easy
        requires: [ui/layouts/column-row-box]
        fruit: false
      - id: scaffold-and-slots
        title: "How do the top bar, the floating button and the content fit together?"
        difficulty: medium
        requires: [ui/layouts/column-row-box]
        fruit: false
  - id: state-and-recomposition
    title: State and Recomposition
    why: Screens change only when state changes, and wrongly held state is the most common Compose bug in AI-written code.
    leaves:
      - id: what-is-recomposition
        title: "Why does my screen not change when my variable changes?"
        difficulty: medium
        requires: [ui/modifiers/reacting-to-taps]
        fruit: false
      - id: remember
        title: "Why does my counter go back to zero every time the screen redraws?"
        difficulty: medium
        requires: [ui/state-and-recomposition/what-is-recomposition]
        fruit: false
      - id: remember-saveable
        title: "Why does my typed text vanish on rotation even with remember?"
        difficulty: medium
        requires: [ui/state-and-recomposition/remember]
        fruit: false
      - id: state-with-lists
        title: "Why does adding an item to my list not update the screen?"
        difficulty: medium
        requires: [ui/state-and-recomposition/remember]
        fruit: false
      - id: state-hoisting
        title: "Who should own a piece of state, the button or its parent?"
        difficulty: medium
        requires: [ui/state-and-recomposition/remember, ui/modifiers/reacting-to-taps]
        fruit: false
      - id: remember-with-keys
        title: "Why does my screen show old data after its input changed?"
        difficulty: hard
        requires: [ui/state-and-recomposition/remember, ui/state-and-recomposition/state-hoisting]
        fruit: false
  - id: lists
    title: Lists
    why: Feeds, chats and galleries are long lists that must scroll smoothly and keep the right row in the right place.
    leaves:
      - id: lazy-column
        title: "How do I show a list of a thousand items without freezing the phone?"
        difficulty: medium
        requires: [ui/layouts/column-row-box, ui/state-and-recomposition/remember]
        fruit: false
      - id: list-keys
        title: "Why do the wrong rows move when I delete an item?"
        difficulty: hard
        requires: [ui/lists/lazy-column, ui/state-and-recomposition/state-with-lists]
        fruit: false
      - id: lazy-grid
        title: "How do I lay items out in a grid like a photo gallery?"
        difficulty: easy
        requires: [ui/lists/lazy-column]
        fruit: false
  - id: theming
    title: Theming
    why: Colours and type come from one theme so the whole app changes in one place.
    leaves:
      - id: material-theme
        title: "Where do my app's colours and fonts come from?"
        difficulty: easy
        requires: [ui/first-composable/text-and-images]
        fruit: false
      - id: using-theme-tokens
        title: "Why write MaterialTheme.colorScheme.primary instead of Color.Blue?"
        difficulty: easy
        requires: [ui/theming/material-theme]
        fruit: false
      - id: matching-system-look
        title: "How does my app follow the phone's own look settings?"
        difficulty: medium
        requires: [ui/theming/using-theme-tokens]
        fruit: false
  - id: navigation
    title: Navigation
    why: Real apps have many screens, and moving between them with the right data and back behaviour is core work.
    leaves:
      - id: what-is-navigation
        title: "How does a Compose app move from one screen to the next?"
        difficulty: medium
        requires: [ui/layouts/scaffold-and-slots, ui/state-and-recomposition/state-hoisting]
        fruit: false
      - id: passing-arguments
        title: "How does the next screen know which item I tapped?"
        difficulty: medium
        requires: [ui/navigation/what-is-navigation]
        fruit: false
      - id: back-navigation
        title: "What should happen when the user goes back?"
        difficulty: medium
        requires: [ui/navigation/what-is-navigation]
        fruit: true
  - id: side-effects
    title: Side Effects
    why: Loading data, timers and listeners must run at the right moment, not on every redraw.
    leaves:
      - id: why-side-effects
        title: "Why must I not start a network call straight from a composable?"
        difficulty: medium
        requires: [ui/state-and-recomposition/what-is-recomposition]
        fruit: false
      - id: launched-effect
        title: "How do I run a coroutine when a screen appears?"
        difficulty: hard
        requires: [ui/side-effects/why-side-effects, ui/state-and-recomposition/remember-with-keys]
        fruit: false
      - id: coroutine-from-a-click
        title: "How do I start a coroutine from a button tap?"
        difficulty: medium
        requires: [ui/side-effects/why-side-effects, ui/modifiers/reacting-to-taps]
        fruit: false
      - id: disposable-effect
        title: "How do I clean up when a composable leaves the screen?"
        difficulty: medium
        requires: [ui/side-effects/launched-effect]
        fruit: false
      - id: collecting-flows
        title: "How does a screen show values that keep arriving over time?"
        difficulty: hard
        requires: [ui/side-effects/launched-effect, ui/state-and-recomposition/state-hoisting]
        fruit: false
  - id: forms-and-input
    title: Forms and Input
    why: Sign-up, search and settings screens are made of text fields and choices that must behave well.
    leaves:
      - id: text-field
        title: "Why can I not type in my text field?"
        difficulty: medium
        requires: [ui/state-and-recomposition/state-hoisting]
        fruit: false
      - id: keyboard-options
        title: "How do I show the number keyboard and a Next button?"
        difficulty: easy
        requires: [ui/forms-and-input/text-field]
        fruit: false
      - id: validating-input
        title: "How do I tell the user what is wrong with what they typed?"
        difficulty: medium
        requires: [ui/forms-and-input/text-field]
        fruit: false
      - id: choice-controls
        title: "How do I let the user switch something on or pick one option?"
        difficulty: easy
        requires: [ui/state-and-recomposition/state-hoisting]
        fruit: false
  - id: animation
    title: Animation
    why: Motion explains change; a fresher must make it smooth without making it slow or confusing.
    leaves:
      - id: animate-as-state
        title: "How do I make a size or colour change glide instead of jump?"
        difficulty: easy
        requires: [ui/state-and-recomposition/remember]
        fruit: false
      - id: animated-visibility
        title: "How do I make something appear and disappear smoothly?"
        difficulty: easy
        requires: [ui/animation/animate-as-state]
        fruit: false
      - id: gesture-driven-animation
        title: "How does a card follow my finger and spring back?"
        difficulty: hard
        requires: [ui/animation/animate-as-state, ui/side-effects/coroutine-from-a-click]
        fruit: false
  - id: accessibility
    title: Accessibility
    why: Many users rely on screen readers, large text and big touch targets, and app stores and laws expect it.
    leaves:
      - id: what-a-screen-reader-hears
        title: "What does a blind user hear when they open my screen?"
        difficulty: medium
        requires: [ui/first-composable/text-and-images, ui/modifiers/reacting-to-taps]
        fruit: false
      - id: big-enough-to-tap
        title: "Why is my small icon button hard to tap?"
        difficulty: easy
        requires: [ui/modifiers/reacting-to-taps]
        fruit: false
      - id: font-scaling
        title: "What happens to my layout when the user makes text huge?"
        difficulty: medium
        requires: [ui/layouts/weight-and-space]
        fruit: false
  - id: adaptive-layouts
    title: Adaptive Layouts
    why: Apps now draw behind the system bars and must fit phones, foldables and tablets.
    leaves:
      - id: drawing-behind-system-bars
        title: "Why does my app draw under the status bar?"
        difficulty: hard
        requires: [ui/layouts/scaffold-and-slots]
        fruit: false
      - id: window-size-classes
        title: "How does one app look right on a phone, a foldable and a tablet?"
        difficulty: medium
        requires: [ui/layouts/scaffold-and-slots, ui/navigation/what-is-navigation]
        fruit: false
  - id: views-and-interop
    title: Views and Interop
    why: Most existing apps still contain XML and Views, and a one-person team must read and mix them.
    leaves:
      - id: what-are-views
        title: "How were Android screens built before Compose?"
        difficulty: easy
        requires: [ui/first-composable/what-is-declarative-ui]
        fruit: false
      - id: reading-xml-layouts
        title: "How do I read an XML layout someone else wrote?"
        difficulty: medium
        requires: [ui/views-and-interop/what-are-views, ui/layouts/column-row-box]
        fruit: false
      - id: compose-inside-views
        title: "How do I add a Compose screen to an app built with Views?"
        difficulty: medium
        requires: [ui/views-and-interop/reading-xml-layouts]
        fruit: false
      - id: views-inside-compose
        title: "How do I use an old View, like a map, inside Compose?"
        difficulty: medium
        requires: [ui/views-and-interop/reading-xml-layouts]
        fruit: false
  - id: custom-drawing
    title: Custom Drawing
    why: Charts, gauges and custom controls need drawing by hand, which pulls the whole branch together.
    leaves:
      - id: canvas-basics
        title: "How do I draw my own shapes, like a bar chart?"
        difficulty: medium
        requires: [ui/modifiers/what-is-a-modifier]
        fruit: false
      - id: build-a-custom-control
        title: "How do I build my own control, like a star rating bar, from scratch?"
        difficulty: hard
        requires: [ui/custom-drawing/canvas-basics, ui/animation/gesture-driven-animation, ui/accessibility/what-a-screen-reader-hears]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. There is no Android emulator in the browser; sims are browser animations that show what changes on screen when the code changes (L15, D8).

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/programming-thinking/what-is-a-variable`, `foundation/programming-thinking/how-a-program-decides` and `foundation/programming-thinking/what-is-a-function` (phase 1).
- Assumes Kotlin: `kotlin/functions/declaring-functions`, `kotlin/lambdas/functions-as-values` and `kotlin/lambdas/lambda-syntax` (phase 2, function types and trailing lambdas) — every composable is a function and most take a lambda.
- Assumes Kotlin: `kotlin/classes-and-objects/what-is-a-class` and `kotlin/classes-and-objects/objects-are-references` (phase 2) — state holders, and why changing a list in place is not a new value (state-with-lists).
- Assumes Kotlin: `kotlin/special-classes/data-classes` and `kotlin/special-classes/sealed-classes` (phase 3) — list items and screen routes.
- Assumes Kotlin: `kotlin/extensions-scope-and-delegates/property-delegates` (phase 3, the `by` keyword) — used by remember and state.
- Assumes Android core: `android-core/activities/what-is-an-activity` — used by what-is-a-composable (setContent is where Compose starts).
- Assumes Android core: `android-core/configuration-changes/why-rotation-restarts` — used by remember-saveable.
- Assumes Android core: `android-core/activities/back-stack` — used by back-navigation.
- Assumes Android core: `android-core/configuration-changes/surviving-a-config-change` — used by window-size-classes (phase 5 stem).
- Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine`, `concurrency/scopes/what-is-a-coroutine-scope`, `concurrency/cancellation-and-errors/cancel-is-cooperative` (phase 4) — used by the side-effects stem (phase 5).
- Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow` (phase 4) — used by collecting-flows.
- Helps to know Android core: `android-core/manifest-and-resources/what-are-resources` (phase 5) — text-and-images recaps stringResource and painterResource in one paragraph.
- Helps to know Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel` (phase 4) — state-hoisting and collecting-flows recap a ViewModel; phase-3 leaves never depend on it.

Challenge note: Compose code cannot run in the standard Kotlin Playground JVM target. `P:` and `F:` tasks use plain-Kotlin models of the idea (for example a fake recomposition loop that prints) or read-only "which line is wrong" tasks. ⚠️ VERIFY whether Kotlin Playground's Compose (Wasm) target can run small Compose snippets; if yes, `docs/06` may allow real Compose tasks.

## Stems

### first-composable · First Composable

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-declarative-ui` | medium | "Why do I describe the whole screen for a given state instead of telling it what to change?" | Giving a painter a photo of the finished room instead of a list of brush strokes; for a new look you hand over a new photo. | "Compose redraws the entire screen every time anything changes." | sim: code-to-screen the learner flips between an instruction-by-instruction version and a describe-from-state version of the same screen; a slider sets a number; the left panel follows step-by-step instructions and drifts out of sync, the right panel is re-described from the number and always matches | `P:` on the sim: which panel is wrong after five quick slides · `B:` skip one instruction on the left panel | — |
| `what-is-a-composable` | easy | "What does @Composable change about a function?" | A stamp on a recipe card that says "this card can go on the display shelf". | "A composable returns a View object I can keep." | none: a syntax rule; the editor and an annotated example are enough | `P:` which of four functions can be called inside setContent · `F:` a composable called from a plain click handler (read-only) | Assumes Android core: `android-core/activities/what-is-an-activity`. Assumes Kotlin: `kotlin/functions/declaring-functions`. |
| `text-and-images` | easy | "Which composables put a word and a picture on the screen?" | Writing on a postcard and sticking a photo next to it. | "Images need a size in pixels to show at all." | none: two building blocks; a preview shows the result | `P:` what shows for an Image with no contentDescription · `F(ai):` text hard-coded instead of a string resource | Helps to know Android core: `android-core/manifest-and-resources/what-are-resources`. contentDescription is named here and taught in accessibility. |
| `previews` | easy | "How do I check a screen in two sizes without a phone?" | A tailor's mannequin: try the suit on a model before the customer arrives. | "A preview runs my whole app." | none: tooling steps shown in a reference video | `P:` which preview shows the long-name case · `B:` make a preview with huge font and find what breaks | ⚠️ VERIFY current @Preview parameters in Android Studio docs. |

### modifiers · Modifiers

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-modifier` | easy | "How do I add space, size and a background to a piece of text?" | Accessories on a phone: a case, a strap, a screen guard, each adds one thing. | "Every composable has its own padding and colour parameters." | none: one rule; a live preview beside the code is enough | `P:` how big is a 100-dp box with 8-dp padding · `B:` remove the size modifier | — |
| `modifier-order` | medium | "Why does padding before background look different from background before padding?" | Wrapping a gift: paper then ribbon is not the same as ribbon then paper. | "Modifier order does not matter, like settings in a list." | sim: code-to-screen the learner flips between modifier-order code variants; modifier blocks stack as layers around a box; the learner drags blocks to reorder and sees the painted area and the clickable area change | `P:` which order makes the whole padded area tappable · `F(ai):` a confident card with clickable after padding, so edges do not respond · `B:` swap two blocks in the sim | — |
| `reacting-to-taps` | easy | "How do I run code when the user taps a card?" | A doorbell: pressing it runs whatever is wired to it. | "Only Button can be tapped." | none: one rule; the ripple is visible in the preview | `P:` what prints after two taps (plain-Kotlin lambda model) · `F:` a click lambda that is called at once instead of passed | Assumes Kotlin: `kotlin/lambdas/functions-as-values`. |

### layouts · Layouts

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `column-row-box` | easy | "Which container stacks items, which lines them up, which layers them?" | A bookshelf (Column), a train of carriages (Row) and a pile of papers on a desk (Box). | "I need to give every item an x and y position." | none: three fixed containers, not a change over time; a preview of each with the same blocks is enough | `P:` sketch what a Row inside a Column shows · `B:` put 20 items in a Row and watch them leave the screen | — |
| `alignment-and-arrangement` | medium | "Which setting moves items along the main direction and which across it?" | Seats on a bus: arrangement is how passengers spread along the aisle, alignment is whether they sit by the window or the aisle. | "Alignment and arrangement are two names for the same thing." | none: two parameters, not a change over time; a live preview with both values is enough | `P:` which dial centres a button in a Column horizontally · `F:` a Column using the wrong parameter to centre its items | — |
| `weight-and-space` | easy | "How do I make a search field take all the space next to its button?" | Filling a suitcase: put the fixed items in first, then let the soft jumper fill what is left. | "Weight is a size in dp." | none: one sizing rule; a preview at two widths is enough | `P:` widths of two items with weights 1 and 2 in a 300-dp row · `B:` give every item weight | — |
| `scaffold-and-slots` | medium | "How do I get a top bar, a floating button and content that do not overlap?" | A newspaper page template: fixed places for the headline, the photo and the story. | "I can ignore the padding Scaffold gives my content." | none: a fixed template; an annotated screenshot shows the slots | `F(ai):` generated code that ignores Scaffold's content padding so the list hides under the top bar · `P:` which slot holds the floating button | Insets tie to drawing-behind-system-bars. |

### state-and-recomposition · State and Recomposition

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-recomposition` | medium | "What makes Compose redraw part of my screen?" | A scoreboard operator who repaints the score only when the referee calls a new score, not when someone whispers it. | "Changing any variable updates the screen." | sim: code-to-screen the learner flips between a plain-variable version and a state version of a counter; a screen made of tiles, each wired to the state it reads; changing a state ball lights and repaints only the wired tiles; changing a plain variable lights nothing | `P:` which text updates after the tap (plain-Kotlin model) · `F(ai):` tidy counter code using a plain var · `B:` wire two tiles to one state | Assumes Kotlin: `kotlin/extensions-scope-and-delegates/property-delegates`. |
| `remember` | medium | "Why does my state reset unless I wrap it in remember?" | A whiteboard in a classroom: wiped at the end of every lesson unless you write on the noticeboard that stays. | "mutableStateOf alone keeps my value." | sim: code-to-screen reuse what-is-recomposition sim; the learner flips between code with and without remember, and each repaint resets unremembered balls to zero | `F(ai):` mutableStateOf without remember inside a composable · `P:` value shown after three taps | — |
| `remember-saveable` | medium | "Which kind of remember keeps my text after the phone rotates?" | A note in your pocket versus a note on the café table: only the pocket note leaves with you. | "remember survives rotation." | none: the rebuild is already animated in the android-core rotation sim; link to it | `F(ai):` a search box that loses its query on rotation · `P:` which values are there after rotation and after process death | Assumes Android core: `android-core/configuration-changes/why-rotation-restarts`. |
| `state-with-lists` | medium | "Why does list.add() inside state not redraw, but a new list does?" | Telling the post office you moved by changing the house number versus painting the inside of the same house: they only notice a new address. | "Changing what is inside the list counts as a change." | sim: code-to-screen reuse what-is-recomposition sim; the learner flips between code that changes a list in place and code that builds a new list, with a list box; adding inside the same box rings no bell, swapping in a new box rings it | `F(ai):` a mutable list inside mutableStateOf that is changed in place · `P:` does the screen show 3 or 4 items | — |
| `state-hoisting` | medium | "Why should a text field get its value and a change callback from its parent?" | A TV remote: the remote sends button presses; the TV holds the channel. One TV, many remotes. | "Each composable should own its own state to be simple." | sim: code-to-screen the learner flips between local-state and hoisted-state code; two widgets show the same value; with local state they drift apart, with the state lifted to the parent they stay in step | `F(ai):` two composables each with their own copy of the same value · `P:` after typing in one, what shows in the other | Helps to know Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel`. |
| `remember-with-keys` | hard | "Why does a remembered value stay stale when the input it came from changes?" | A sticky note that says "today's price": it stays on the shelf even after the price changes, unless you replace it when the date changes. | "remember recalculates when its inputs change." | sim: code-to-screen reuse what-is-recomposition sim; the learner flips between remember with and without a key, and a remembered box keeps its old ball until a key light changes | `F(ai):` remember with no key around a value computed from a parameter · `P:` value shown after the user id changes | — |

### lists · Lists

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `lazy-column` | medium | "Why does a LazyColumn scroll smoothly through a thousand rows when a Column freezes?" | A sushi conveyor belt: only the plates in front of you are on the belt, and plates are refilled as they come round. | "A lazy list builds all its rows, just later." | sim: code-to-screen the learner flips between a Column version and a LazyColumn version of the same feed; a conveyor belt behind a window; rows are built as they enter the window and returned to a bin as they leave; a counter shows rows alive in memory | `F(ai):` a generated feed using Column and forEach over 1,000 items · `P:` on the sim: how many rows exist while 8 fit on screen · `B:` scroll very fast | — |
| `list-keys` | hard | "Why must each row have a stable key?" | Coats on numbered hooks versus a pile of coats: take one from the pile and everyone grabs the wrong coat. | "The row's position is a good enough key." | sim: code-to-screen reuse lazy-column sim; the learner flips between code with and without keys; deleting row 2 without keys shifts every checkbox to the wrong row, with keys the right row leaves | `F(ai):` items without a key where each row holds a remembered checkbox · `P:` which row stays ticked after deleting the first item | — |
| `lazy-grid` | easy | "How do I show photos in columns that adapt to screen width?" | Tiles on a bathroom wall: the size is fixed, the number per row depends on the wall. | "A grid is a Column of Rows." | none: layout rule; a preview at two widths is enough | `P:` how many columns fit in 360 dp with 120-dp cells · `F(ai):` a nested Column of Rows for 500 photos | — |

### theming · Theming

Flags: phase=3 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `material-theme` | easy | "Where does a Button get its colour if I never set one?" | A school uniform rulebook: every student looks right without choosing each item. | "Material colours are fixed by Google and cannot be changed." | none: a lookup rule; a colour-role diagram is enough | `P:` which colour role a Button container uses · `B:` change one role and see every screen change | ⚠️ VERIFY current Material 3 colour roles and type scale names. |
| `using-theme-tokens` | easy | "Why does a hard-coded colour break my app later?" | Writing "the manager" on a door instead of a person's name: it stays right when people change. | "Hard-coded colours are fine if they look right today." | none: a single rule | `F(ai):` a confident screen full of Color(0xFF...) values and fixed font sizes · `P:` what changes when the theme changes | Same rule Kotree itself follows (design tokens, `docs/02`). |
| `matching-system-look` | medium | "How does my app switch to dark colours or wallpaper colours when the phone does?" | A chameleon taking the colours of the branch it sits on. | "Dark theme is just inverting the colours." | none: a setting read once by the theme; a reference video shows the switch | `P:` which colour scheme is picked on an older phone · `F:` a theme that ignores the system setting | volatile. Kotree has no dark mode (L9); this teaches the learner's own app. ⚠️ VERIFY dynamic colour minimum version and current API names. |

### navigation · Navigation

Flags: phase=3 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-navigation` | medium | "How does an app decide which screen is showing right now?" | A stack of cards in your hand: the top card is the screen; adding or removing cards changes it. | "Each screen opens the next screen by calling it." | sim: reuse android-core back-stack sim with screens as a list the learner can push and pop | `P:` the back stack after three navigations and one back · `F(ai):` a screen that navigates on every recomposition instead of on a tap | volatile. Checked 18 Oct 2026: Jetpack Navigation 3 (1.0) is stable since Nov 2025 (Android Developers blog); Navigation Compose (NavHost, NavController) is still common in existing and AI-written code. ⚠️ VERIFY which one to teach first; teach the back-stack idea so both read the same. |
| `passing-arguments` | medium | "How does the detail screen get the id of the tapped item?" | A cloakroom ticket: pass the small number, not the coat. | "Pass the whole object to the next screen." | none: data hand-over rule; the editor model is enough | `F(ai):` a generated route that serialises a whole object with an image · `P:` what the detail screen receives | volatile. ⚠️ VERIFY current type-safe route API. Assumes Kotlin: `kotlin/special-classes/data-classes`. |
| `back-navigation` | medium | "What should back do on a screen with an open dialog or a half-filled form?" | A lift's "previous floor" button that must check nobody is stuck in the door. | "Back always closes the current screen." | none: the back stack is already shown by the what-is-navigation sim; a decision diagram for dialog, form and screen plus a reference video of the predictive-back peek are enough | `P:` which screen shows after back from a dialog over a detail screen · `F(ai):` back handling that skips the unsaved-changes check | volatile. Assumes Android core: `android-core/activities/back-stack`. Checked 18 Oct 2026 (Android 16 behaviour changes): predictive back is on by default for apps targeting API 36. ⚠️ VERIFY Compose back-handler API. Fruit (see `## Fruits`). |

### side-effects · Side Effects

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-side-effects` | medium | "Why does a request written inside a composable run forty times?" | A shop assistant who reorders stock every time a customer glances at the shelf. | "A composable runs once, like onCreate." | sim: code-to-screen reuse what-is-recomposition sim; the learner flips between a request in the composable body and a request in an effect; each repaint fires a ball down a pipe to a server, and a counter shows how many requests were sent | `F(ai):` a confident screen that calls the repository directly in its body · `P:` how many calls after five state changes | Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine`. |
| `launched-effect` | hard | "When does LaunchedEffect start, cancel and restart its coroutine?" | A kitchen timer tied to an order number: a new order number throws away the old timer and starts a new one. | "LaunchedEffect(Unit) is always the safe choice." | sim: code-to-screen the learner flips between effect-key code variants (Unit, the id, a changing value); a timeline with a key lamp; each key change cancels the running coroutine bar and starts a new one; leaving the screen cancels it | `F(ai):` LaunchedEffect(Unit) that loads data for a user id that later changes · `P:` how many loads after the id changes twice · `B:` use a key that changes every frame | Assumes Concurrency: `concurrency/scopes/what-is-a-coroutine-scope` and `concurrency/cancellation-and-errors/cancel-is-cooperative`. |
| `coroutine-from-a-click` | medium | "Which scope should a button use to start a coroutine?" | Borrowing a pen tied to the desk: it goes away with the desk, never left in someone's pocket. | "GlobalScope is fine for a quick button action." | none: a scope choice; the cancellation is already animated in launched-effect | `F(ai):` a button that launches in GlobalScope · `P:` what happens to the job when the screen closes | — |
| `disposable-effect` | medium | "How do I remove a listener when my composable leaves the screen?" | Signing out of a gym: you sign in when you arrive and must sign out when you leave. | "Compose cleans up any listener I add by itself." | none: the enter/leave timing reuses the launched-effect sim | `F(ai):` a sensor listener registered in a side effect and never removed · `P:` how many listeners after opening the screen three times | Helps to know Android core: `android-core/lifecycle/lifecycle-aware-code`. |
| `collecting-flows` | hard | "How does a screen show a changing value only while it is visible?" | A radio that plays the station while you are in the room and switches itself off when you leave. | "collectAsState and the lifecycle-aware version are the same." | sim: code-to-screen the learner flips between plain and lifecycle-aware collection code; values drip from a tap into a cup on the screen; when the app goes to the background the lifecycle-aware cup closes, the plain one keeps filling and drains battery | `F(ai):` generated code collecting a location flow with no lifecycle awareness · `P:` does the value update while the app is in the background | Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow`. Helps to know Architecture and data: `architecture-data/viewmodel/what-is-a-viewmodel`. ⚠️ VERIFY collectAsStateWithLifecycle artifact and current guidance. |

### forms-and-input · Forms and Input

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `text-field` | medium | "Why does a text field show nothing I type unless state feeds it back?" | A typist with a mirror: letters appear on the page only if someone writes them there. | "A text field remembers what I typed by itself." | none: the up-and-down flow is already shown by the state-hoisting sim; a two-arrow diagram (event up, value down) is enough | `F(ai):` a TextField whose onValueChange is empty · `P:` what the box shows after typing "hi" | volatile. ⚠️ VERIFY: whether the state-based text field API is the recommended default now. |
| `keyboard-options` | easy | "How do I get a number pad and move to the next field with one key?" | A form at a bank counter with a box for numbers only and an arrow saying "next box". | "The keyboard type also blocks letters from being typed." | none: settings; a reference video shows the keyboards | `P:` which keyboard shows for a phone field · `F:` an email field showing the plain keyboard | volatile. ⚠️ VERIFY Android 17 keyboard-after-rotation note (developer.android.com behaviour changes). |
| `validating-input` | medium | "When should my form tell the user an email is wrong?" | A teacher who checks your answer when you hand it in, not while you are still writing. | "Show the error from the first letter typed." | none: a timing rule; a three-state diagram is enough | `P:` which message shows after typing then leaving the field · `F(ai):` validation that shows "invalid email" on an empty, untouched field | volatile. ⚠️ VERIFY: current Compose text-field and focus APIs on developer.android.com. |
| `choice-controls` | easy | "When do I use a switch, a checkbox or radio buttons?" | A light switch, a shopping checklist and a single-answer exam question. | "A checkbox and a switch do the same job, so either is fine." | none: a choice rule; a side-by-side picture is enough | `P:` pick the control for five settings · `F:` radio buttons that allow two answers at once | volatile. ⚠️ VERIFY: current Material 3 switch, checkbox and radio-button components and their selection semantics. |

### animation · Animation

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `animate-as-state` | easy | "How do I make a box grow smoothly when its state changes?" | A door with a soft-close hinge: you push it, it glides instead of slamming. | "Animation needs a timer and a loop I write myself." | sim: code-to-screen the learner flips between animation-spec code variants; a box on a spring; the learner changes target size and drags stiffness and damping sliders to see bounce or glide | `P:` which spring settings overshoot · `B:` set damping to zero | Physics fit: spring (`docs/05`). |
| `animated-visibility` | easy | "How do I make a message slide in and fade out?" | A theatre curtain that opens and closes instead of the actor teleporting on stage. | "Hiding something means removing it with an if, so it cannot animate." | none: one API; the spring idea is already animated in animate-as-state | `P:` what the user sees when visible flips quickly twice · `F:` an if-block that pops instead of animating | — |
| `gesture-driven-animation` | hard | "How does a swiped card follow my finger and spring back if I let go too early?" | A rubber band pinned to a board: it stretches with your finger and snaps back when released. | "Following the finger and springing back are two separate animations I must sync by hand." | sim: drag a card; past a line it flies off, before the line a spring pulls it back; the learner can fling fast or slow | `P:` does a slow drag past half-way dismiss the card · `F(ai):` gesture code that starts a new animation without stopping the running one | Physics fit: spring and fling (`docs/05`). |

### accessibility · Accessibility

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-a-screen-reader-hears` | medium | "What does TalkBack read out for an icon button with no description?" | A tour guide describing a museum room to a visitor who cannot see it. | "Screen readers read the screen like a picture." | none: shown best with a reference video of TalkBack reading a screen; `P:` uses a transcript | `P:` what TalkBack says for three elements (transcript) · `F(ai):` icons with contentDescription "icon" and decorative images that are read aloud | ⚠️ VERIFY Compose semantics API names. |
| `big-enough-to-tap` | easy | "Why should every tappable thing be at least about the size of a fingertip?" | A lift button the size of a grain of rice. | "If it looks fine, it is big enough to tap." | none: a single size rule; an overlay showing touch areas is enough | `P:` which of three buttons fails the minimum size · `F:` a 24-dp icon with a click and no extra touch area | ⚠️ VERIFY minimum touch target value in Material and Android guidance. |
| `font-scaling` | medium | "What breaks when the user sets the largest text size?" | A label printed for a jar that must still fit when the letters get bigger. | "Fixed heights keep my layout neat for everyone." | none: a preview at font scale 2 shows the break better than motion | `F(ai):` a card with fixed height and text in sp that clips at large font · `P:` which of two layouts clips | ⚠️ VERIFY current maximum system font scale. |

### adaptive-layouts · Adaptive Layouts

Flags: phase=5 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `drawing-behind-system-bars` | hard | "What keeps my top bar from hiding under the clock?" | A poster on a wall with light switches: you can paint behind the switches, but the text must stay clear of them. | "I can turn off edge-to-edge for good with a theme setting." | sim: code-to-screen the learner flips between inset-handling code variants; a phone frame with status bar, cut-out and gesture bar; content slides under them until the learner switches insets padding on for each edge | `P:` which edge needs padding for a bottom button · `F(ai):` a generated screen that hard-codes 24 dp for the status bar · `B:` turn the phone to landscape with a cut-out | volatile. Checked 18 Oct 2026 (developer.android.com): apps targeting API 35 are edge-to-edge by default on Android 15; apps targeting API 36 on Android 16 cannot opt out. ⚠️ VERIFY before live. |
| `window-size-classes` | medium | "How does my app choose one pane on a phone and two panes on a tablet?" | A restaurant that uses one long table for a small group and joins tables for a big group. | "I can lock my app to portrait on tablets." | none: three fixed layouts at the size-class boundaries; a diagram at three widths is enough | `P:` which layout shows at 700 dp wide · `F(ai):` code that checks the device model instead of the window size | volatile. Assumes Android core: `android-core/configuration-changes/surviving-a-config-change`. Checked 18 Oct 2026 (Android 16 behaviour changes): apps targeting API 36 cannot restrict orientation on displays sw600dp and wider; the opt-out ends at API 37. ⚠️ VERIFY current window size class breakpoints. |

### views-and-interop · Views and Interop

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-are-views` | easy | "How is a screen built from View objects that my code changes by hand?" | Furniture you move around a room yourself, compared with handing the painter a new photo. | "Views are dead and I will never meet them." | none: a concept compared with what-is-declarative-ui; the earlier sim already shows the contrast | `P:` which line changes the text in a View-based screen · `B:` forget one update line and see the screen go stale | — |
| `reading-xml-layouts` | medium | "Can I sketch the screen an XML layout file describes?" | Reading a house floor plan before walking in. | "XML layouts are code that runs top to bottom." | none: a reading skill; a side-by-side XML and screenshot is enough | `P:` sketch the screen of a 20-line layout · `F:` an id used in code that does not match the XML (read-only) | — |
| `compose-inside-views` | medium | "How do I put a new Compose screen into an old XML-based app?" | Fitting a modern kitchen into an old house: one wall is opened, the rest stays. | "To use Compose I must rewrite the whole app." | none: a wiring rule; a diagram of the container is enough | `P:` which line hosts Compose inside the layout · `F(ai):` Compose inside a View with the wrong disposal strategy | Helps to know: fragments are in android-core `## Later`. ⚠️ VERIFY ComposeView disposal strategy guidance. |
| `views-inside-compose` | medium | "How do I show a map View inside a Compose screen?" | Hanging an old framed painting on a new wall with an adapter hook. | "Old Views cannot appear inside Compose." | none: a wiring rule; a code-and-preview pair is enough | `P:` which block runs once and which on every update · `F(ai):` a View created inside the update block on every recomposition | ⚠️ VERIFY AndroidView factory and update rules. |

### custom-drawing · Custom Drawing

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `canvas-basics` | medium | "Where is the point (0, 0) and how do I draw a bar there?" | Drawing on graph paper where the top-left corner is the start and y grows downward. | "y goes up, like in maths class." | none: a fixed coordinate rule; a labelled grid diagram with y growing downward is enough | `P:` where a circle at (50, 150) appears · `F:` a bar chart drawn upside down | Same coordinate idea Kotree's own tree uses. |
| `build-a-custom-control` | hard | "Can I build a star rating control that draws, reacts to touch, animates and speaks to TalkBack?" | A carpenter making a chair from planks: every earlier tool is used once. | "A custom control only needs to look right." | sim: reuse gesture-driven-animation sim on a row of stars; dragging fills stars with a spring, and a transcript line shows what TalkBack says | `P:` what TalkBack says after choosing 3 stars · `F(ai):` a generated rating bar that looks right but cannot be used with TalkBack · `B:` tap between two stars | Synthesis leaf. Fruit (see `## Fruits`). |

## Fruits

| Leaf | Capstone | Constraints | Proves |
|---|---|---|---|
| `ui/navigation/back-navigation` | "A three-screen reading list" | List, detail and add screens · list of at least 200 items scrolls smoothly with stable keys · typed text survives rotation · all colours and fonts from the theme · back from the add screen asks before throwing away a half-filled form | Builds a multi-screen Compose app with correct state (`docs/01` §6.2 ui ability). ⚠️ VERIFY exact ability wording in `docs/01`. |
| `ui/custom-drawing/build-a-custom-control` | "A habit tracker that works for everyone" | One custom drawn control · data loads with a side effect that restarts only when its key changes · works at the largest font size and with TalkBack · fits phone and tablet with one-pane and two-pane layouts · no content under the system bars | A one-person team can ship polished, accessible, adaptive UI (L24). |

## Later

- Custom layouts that measure and place children, because rows, columns and grids cover a first full app.
- Shared-element transitions, polish beyond the animation stem.
- Wear OS and TV UI, separate form factors with their own rules.
- MotionLayout in legacy Views, rare in new code.
- Advanced Canvas shaders, beyond what charts and controls need.
- RecyclerView adapters in depth, because reading-xml-layouts and the interop leaves cover reading old screens.
- Compose performance and stability, taught in the Profiling branch instead.
- Sticky headers and nested scrolling, useful polish after lists are solid.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 14 stems, 49 leaves, all shadow. | +49 | Est. 44, band 35–53: inside the band. Added text-and-images, state-with-lists, collecting-flows and choice-controls beyond the sketch because AI-written Compose code fails there often. Moved modifiers before layouts because weight and Scaffold padding are modifiers. |
| 2026-10-18 | Sims cut from 22 to 17 of 49 leaves (about 35 %): column-row-box, back-navigation, text-field, window-size-classes and canvas-basics now use a preview, diagram or earlier sim. Foundation and Kotlin `Assumes` changed from stem IDs to full leaf IDs. | 0 | `docs/08` §6 share guide; Foundation and Kotlin roadmaps now exist. |
