---
id: architecture-data/viewmodel/what-is-a-viewmodel
title: "What can a ViewModel hold that the screen cannot?"
status: shadow
difficulty: medium
order: 3
requires: [architecture-data/why-architecture/three-jobs-in-an-app]
summary: "A ViewModel is a keeper of screen state. It is not thrown away when the screen is rebuilt, for example on rotation."
video: ""
publishedOn:
updatedOn:
sims: [stage-and-backstage]
playground: true
fruit: false
upnext: false
---

<!--
SOURCES (read 21 Oct 2026):
- https://developer.android.com/topic/libraries/architecture/viewmodel (business logic or screen level state holder; caches state and persists it through configuration changes; remains in memory until its ViewModelStoreOwner disappears; should not hold Context or Resources; viewModel() from lifecycle-viewmodel-compose)
- https://developer.android.com/guide/topics/resources/runtime-changes (on a configuration change the system destroys the activity instance and creates a new one; state kept as fields is cleared)
- https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate (ViewModel handles configuration changes; SavedStateHandle is the backup for system-initiated process death)
⚠️ VERIFY: compile and run not done for any kotlin block. The CounterViewModel block is marked "does not compile" because it needs the Android lifecycle library.
⚠️ VERIFY: the roadmap says to re-check ViewModel behaviour before go-live (volatile). Checked on the three pages above on 21 Oct 2026.
⚠️ VERIFY: the Android lifecycle and concurrency leaves named in the roadmap are not required here, so this leaf teaches rotation recreation in one paragraph from the runtime-changes page.
-->

## The question

You tap a button three times and the counter on the screen reads 3. You turn the phone sideways and the counter reads 0. What did the screen forget? Where could the number have been kept so that it stays?

## Mental model

Think of a stage with a stage manager.

- The stage is the screen. It is the only part the audience sees.
- The actors are the objects that make up the screen.
- A costume change is the phone being turned. The actor walks off and a new actor walks on.
- The props are the data, such as the counter. A prop left on the stage walks off with the actor.
- The stage manager waits backstage and keeps the props safe while the costumes change.

When the new actor walks on, the actor asks the stage manager for the props. The audience never sees the stage manager.

**Where the analogy stops:** A stage manager works the whole show and decides when props leave. A ViewModel is dropped only when the screen it belongs to is gone for good. Also, if the whole theatre burns down, which is the system ending the app, the props are lost too. The stage manager does not survive that.

## See it move

```sim
id: stage-and-backstage
template: zones
title: Props on the stage and props backstage
intro: "An actor in costume 1 stands on the stage next to a counter prop that reads 0. Backstage is empty."
zones:
  - { id: stage, label: "Stage (the screen)" }
  - { id: backstage, label: "Backstage (the stage manager)" }
items:
  - { id: actor, label: "Actor in costume 1", at: stage }
  - { id: counter, label: "Counter prop: 0", at: stage }
vars: { count: 0, costume: 1 }
show:
  - { var: count, label: "Counter value" }
  - { var: costume, label: "Costume number" }
actions:
  - id: add
    label: "Add one"
    do:
      - add: { count: 1 }
      - label: { counter: "Counter prop: {count}" }
      - say: "The audience sees the counter prop go up to {count}."
  - id: stash
    label: "Move the counter prop backstage"
    cases:
      - when: { at: { counter: stage } }
        do:
          - move: { counter: backstage }
          - say: "The stage manager takes the counter prop backstage and keeps it safe. The stage shows only the actor."
    refuse: "The counter prop is already backstage."
  - id: rotate
    label: "Turn the phone sideways"
    cases:
      - when: { at: { counter: backstage } }
        do:
          - add: { costume: 1 }
          - label: { actor: "Actor in costume {costume}" }
          - say: "The actor walks off and a new actor walks on in costume {costume}. The new actor asks backstage and finds the counter prop still at {count}."
      - do:
          - add: { costume: 1 }
          - set: { count: 0 }
          - label: { actor: "Actor in costume {costume}", counter: "Counter prop: 0" }
          - say: "The actor walks off, and the counter prop on the stage goes with the actor. The new actor in costume {costume} starts with a counter prop at 0."
script: [ add, add, rotate, add, stash, rotate ]
try:
  - text: "Press \"Add one\" twice. The counter prop on the stage reads 2."
    done: [ add, add ]
  - text: "Press \"Turn the phone sideways\". The actor changes costume and the counter prop starts again at 0."
    done: [ rotate ]
  - text: "Press \"Add one\", then \"Move the counter prop backstage\", then \"Turn the phone sideways\" again. This time the counter keeps its value."
    done: [ add, stash, rotate ]
```

**What you should notice:** A prop left on the stage walks off with the old actor, and a prop kept backstage is still there when the new actor walks on.

## Build the logic

### Notice

A counter screen has one number and one button. Ada taps three times and the number reads 3. She turns the phone. The old screen is thrown away and a new one is built from scratch. The new screen shows 0.

### Guess

Where could the number live so that it survives the rebuild? Here are three ideas.

- A. Inside the screen's own objects, only written more carefully.
- B. In a separate keeper that the screen asks for the number, and that is not thrown away when the screen is rebuilt.
- C. Nowhere. Ask the user to tap three times again.

Idea A fails because the screen's own objects are exactly what is thrown away. Idea C is a bad deal for the user. Idea B is the answer.

### Build

1. Move the number out of the screen and into a keeper.
2. Every time the screen is built, it asks the keeper for the number and shows it.
3. A tap goes to the keeper, and the keeper changes the number.
4. When the phone turns, the screen is thrown away and built again. The keeper stays where it is.
5. The keeper is thrown away only when the screen is gone for good, for example when the user leaves it.
6. If the system ends the whole app, the keeper goes too.

### Name it

A **configuration change** is a change to the device while the app runs, such as turning the screen sideways. The Android documentation says the system recreates the activity: it destroys the existing instance and creates a new one. It also says this clears any state kept as fields in the activity. An **activity** is the window that holds your screen.

The keeper is a **ViewModel**. The documentation calls it a business logic or screen level state holder, and says its principal advantage is that it caches state and persists it through configuration changes. A **state holder** is an object that keeps the data a screen shows. Business logic means the rules that decide what the data is, such as adding one to a counter. The documentation says a ViewModel stays in memory until its owner disappears. For an activity, that is when it finishes. In Compose you ask for one with the `viewModel()` function.

It also says a ViewModel should not hold references to lifecycle-related things such as a `Context` or `Resources`, because it can live longer than the screen and a held reference would leak memory.

| Stage | Android |
|---|---|
| Stage | The screen |
| Actor changes costume | The configuration change |
| Stage manager | The ViewModel |
| Prop | A piece of screen data |

Here is a ViewModel for the counter. It needs the Android lifecycle library, so it cannot run on this page. A real screen also has to be told when the number changes, and this page leaves that out.

```kotlin
// does not compile
// Needs the Android lifecycle library, so it cannot run on this page.
class CounterViewModel : ViewModel() {
    var count = 0
        private set

    fun add() {
        count++
    }
}
```

Here is the idea in plain Kotlin. It is a model, not real Android. A new `Screen` stands for the rebuilt screen. The `Keeper` is made once.

```kotlin
// A model in plain Kotlin, not real Android.
class Keeper {
    var count = 0
}

class Screen(private val keeper: Keeper) {
    var ownCount = 0

    fun tap() {
        ownCount++
        keeper.count++
    }
}

fun main() {
    val keeper = Keeper()
    var screen = Screen(keeper)
    screen.tap()
    screen.tap()
    screen = Screen(keeper)
    println("Screen's own count: " + screen.ownCount)
    println("Keeper's count: " + keeper.count)
}
```

This prints `Screen's own count: 0`, then `Keeper's count: 2`. The new `Screen` starts from nothing. The `Keeper` was never replaced.

## Predict the output

```challenge
type: predict
id: screen-and-keeper-after-rebuild
verified: "UNVERIFIED: compile not run"
code: |
  object Keeper { var count = 0 }
  class Screen { var own = 0 }

  fun tap(screen: Screen) { screen.own++; Keeper.count++ }

  fun main() {
      var screen = Screen()
      tap(screen)
      screen = Screen()
      tap(screen)
      println("Screen " + screen.own + ", keeper " + Keeper.count)
  }
options:
  - id: a
    text: "Screen 1, keeper 2"
    correct: true
    why: "The new screen starts at 0, and the second tap takes it to 1. The keeper was never replaced, so it counted both taps."
  - id: b
    text: "Screen 2, keeper 2"
    why: "You may have thought the rebuilt screen keeps what the old one counted. Props left on the stage walk off with the old actor."
  - id: c
    text: "Screen 1, keeper 1"
    why: "You may have thought the keeper starts again too. Only the screen was rebuilt. The stage manager stayed backstage."
output: |
  Screen 1, keeper 2
```

```challenge
type: predict
id: keeper-made-once
verified: "UNVERIFIED: compile not run"
code: |
  class Keeper { init { println("keeper created") } }
  class Screen(val keeper: Keeper) { init { println("screen created") } }

  fun main() {
      val keeper = Keeper()
      repeat(2) { Screen(keeper) }
  }
options:
  - id: a
    text: "keeper created, screen created, keeper created, screen created"
    why: "You may have thought each new screen brings a new keeper. The keeper is made once, before the screens, and handed to each one."
  - id: b
    text: "keeper created, screen created, screen created"
    correct: true
    why: "The stage manager is hired once. Each costume change brings a new actor, so the screen is created twice and the keeper once."
  - id: c
    text: "screen created, screen created, keeper created"
    why: "You may have expected the screens first. The keeper is made on the first line of main, so it comes before any screen."
output: |
  keeper created
  screen created
  screen created
```

## Try it

1. **Write.** Copy the plain-Kotlin model from Name it into the editor.
2. **Run.** You should see `Screen's own count: 0` and `Keeper's count: 2`.
3. **Break.** Delete the line `keeper.count++` inside `tap`, and change nothing else. Run it again. Both counts are now 0. The taps never reached the keeper, so it had nothing to keep.
4. **Fix.** Put the line `keeper.count++` back and run it. You should see `Screen's own count: 0` and `Keeper's count: 2` again.

## Fix the bug

```challenge
type: fix
id: fix-counter-kept-in-screen
verified: "UNVERIFIED: compile not run"
code: |
  // A model in plain Kotlin, not real Android.
  class CounterKeeper {
      var count = 0
  }

  class CounterScreen(private val keeper: CounterKeeper) {
      private var taps = 0

      fun tap() {
          taps = taps + 1
      }

      fun label(): String {
          return "Taps: " + taps
      }
  }

  fun main() {
      val keeper = CounterKeeper()
      var screen = CounterScreen(keeper)
      screen.tap()
      screen.tap()
      // The phone turns, so the screen is built again.
      screen = CounterScreen(keeper)
      println(screen.label())
  }
hints:
  - "Look at where tap stores the number. What happens to the screen's own fields when the screen is built again?"
  - "The keeper is the part that survives. Make tap and label use the keeper's count."
expected: |
  Taps: 2
fix: |
  // A model in plain Kotlin, not real Android.
  class CounterKeeper {
      var count = 0
  }

  class CounterScreen(private val keeper: CounterKeeper) {
      fun tap() {
          keeper.count = keeper.count + 1
      }

      fun label(): String {
          return "Taps: " + keeper.count
      }
  }

  fun main() {
      val keeper = CounterKeeper()
      var screen = CounterScreen(keeper)
      screen.tap()
      screen.tap()
      // The phone turns, so the screen is built again.
      screen = CounterScreen(keeper)
      println(screen.label())
  }
explanation: "The number sat on the stage in the screen's own field, so it walked off when the screen was rebuilt. Kept in the keeper, it is still there for the new screen."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. For question 4, the screen is built twice in `main`, and the first one's `taps` is lost. The keeper is built once.

## Tricks to remember

- Props on the stage walk off. Props backstage stay.
- A ViewModel is a stage manager: the audience never sees it.
- Turning the phone rebuilds the screen. It does not rebuild the keeper.
- Ask: "If the screen is rebuilt, do I need this number again?" If yes, it belongs backstage.
- Task, in an Android project with Compose: put a counter in a ViewModel, show it on the screen, rotate the emulator, and see whether it keeps its value.

## Common mistakes

- **Thinking a ViewModel is a second Activity.** Both seem to sit behind the screen and both have lifecycle words in their docs. An activity is the window. A ViewModel is a plain keeper of data and logic, and it has no window of its own.
- **Thinking a ViewModel survives everything.** Surviving rotation feels like surviving anything. The documentation says it is dropped when its owner is gone for good, and it recommends `SavedStateHandle` as a backup for system-initiated process death. If the system ends the app, the stage manager goes too.
- **Putting a Context or a screen object inside the ViewModel.** It feels handy to keep a handle on the screen. The documentation says a ViewModel should not hold references to lifecycle-related things such as a `Context`, because it can live longer than the screen and leak memory.

## Where this shows up in Android

A user fills in a form, turns the phone, and the form is empty. The documentation says the system recreates the activity on a configuration change and clears any state kept as fields in it. The form text was a prop on the stage. Kept in a ViewModel, it would still be there.

Another symptom: the user leaves the app for a while, the system ends it to free memory, and the screen comes back empty. The ViewModel was lost with the app. The documentation points to `SavedStateHandle` for that case.

## Say it in an interview

**What problem does a ViewModel solve that a plain variable in the screen cannot?**
The screen is destroyed and recreated on a configuration change such as rotation, and state kept as fields is cleared. The Android documentation says a ViewModel caches state and persists it through configuration changes. So the user keeps their data after turning the phone.

**What are the two main jobs of a ViewModel?**
The documentation says its key benefits are two: it lets you persist UI state and it gives you access to business logic. In plain words, it holds the data the screen shows and the rules that change that data.

**Should a ViewModel hold a reference to a Context or a screen?**
No. The documentation says ViewModels should not hold references to lifecycle-related APIs such as `Context` or `Resources`, to prevent memory leaks, because they can live longer than their owner. My own reasoning: pass the ViewModel plain data and let the screen look up things like strings.

**Does a ViewModel survive the app being killed?**
No. The documentation says ViewModel objects handle configuration changes, and that for system-initiated process death you may want `SavedStateHandle` as a backup. The trade-off is that saved state must be small and light, so large data belongs in local storage.

## Recap

1. Turning the phone rebuilds the screen, and state kept in the screen's own fields is lost.
2. A ViewModel is a keeper of screen state that survives that rebuild.
3. It is dropped when its owner is gone for good, and it does not survive the app being ended.

## You can now

- Predict which counter survives a screen rebuild in a short plain-Kotlin model.
- Predict how many keepers and screens a model creates when the screen is rebuilt.
- Fix a model that keeps its counter in the screen so the value survives a rebuild.
