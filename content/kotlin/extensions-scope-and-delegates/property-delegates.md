---
id: kotlin/extensions-scope-and-delegates/property-delegates
title: What does by mean in val x by lazy?
status: shadow
difficulty: medium
order: 59
requires: [kotlin/extensions-scope-and-delegates/lateinit, kotlin/lambdas/lambda-syntax]
summary: The word by hands a property to a helper. See why some work runs once, on the first read, or never.
video: ""
publishedOn:
updatedOn:
sims: [lazy-coffee-machine]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://kotlinlang.org/docs/delegated-properties.html : syntax "val/var <property name>: <Type> by <expression>"; getValue is called when you read a delegated property; lazy() takes a lambda and returns a Lazy<T>; the first get executes the lambda and remembers the result; default is synchronized; LazyThreadSafetyMode.PUBLICATION and NONE; Delegates.observable handler runs after the assignment; vetoable runs before.
- https://developer.android.com/topic/libraries/architecture/livedata : val currentName: MutableLiveData<String> by lazy { MutableLiveData<String>() }.
- https://developer.android.com/develop/ui/compose/state : "var value by remember { mutableStateOf(default) }" and "The by delegate syntax requires the following imports" (getValue, setValue).
- https://developer.android.com/topic/libraries/architecture/views/viewmodel/viewmodel-apis-views : "val viewModel: MyViewModel by viewModels()".
- Interview seed: docs/practice/interview-kotlin.md, section kotlin/extensions-scope-and-delegates/property-delegates.
- Code blocks and Predict outputs were hand-traced and not compiled in this session (no shell). Run scripts/kt-run.sh before review. The Kotlin page shows no output for its lazy example; the printed order here is derived from the rule that the first read runs the block.
-->

## The question

A coffee shop has a big machine that takes five minutes to brew a pot and uses a lot of beans. Some days nobody orders coffee until 10, and some days nobody orders it at all. When should the shop brew? And once a pot is made, should the second customer wait for another brew?

## Mental model

Think of that coffee machine in a shop.

- The shop is the object. It has a property called "coffee".
- Asking for coffee is reading the property.
- The machine is a helper. The shop does not brew by itself. It hands the job of answering "coffee" to the machine.
- The machine has a recipe: the steps to brew.
- The first press of the button runs the recipe. The machine brews one pot and keeps it warm.
- Every press after that pours from the warm pot. The recipe does not run again.
- If nobody presses the button, the machine never brews.

**Where the analogy stops:** A real pot goes cold or runs out, and then you brew again. In Kotlin, the first result is kept for as long as the object lives, and the recipe never runs a second time.

## See it move

<!-- SIM: lazy-coffee-machine -->

**Try this:**
1. Drag the first "Press the button" card to slot 3 and press Play.
2. Add two more cards after it and press Play again.
3. Remove every card and press Play.
4. (Optional reveal) Tick "Brew when the shop opens" and press Play with the cards at slot 3.

**What you should notice:** One brew happens at the first press and the count stays at 1 after that, no brew happens with no press, and the tick-box moves the brew to slot 1 whether or not anyone presses.

## Build the logic

### Notice

The shop opens. Maya orders coffee at 10. The machine brews one pot. Sam orders at 11 and gets a cup from the same pot. Ana orders at 12 and gets one more. On a quiet day, nobody orders and the machine stays off. The brewing happened once on a busy day and zero times on a quiet day.

### Guess

When should the shop brew? Here are three ideas.

- A. At 8, when the shop opens, whether or not anyone will order.
- B. At every order, so each cup is fresh.
- C. At the first order, and keep the pot for everyone after.

Idea A wastes beans on a quiet day. Idea B makes every customer wait five minutes. Idea C is the answer for a job that is costly and may never be needed.

There is a second question. Who should do the brewing and remembering: the shop, or a helper? Here are three ideas.

- A. The shop writes the brewing steps in its own code every time it needs them.
- B. The shop hands the job to a ready-made helper and only gives it the recipe.
- C. Every customer brews their own.

Idea A repeats the same steps for every property that works this way. Idea C is unsafe, because pots multiply. Idea B is the answer.

### Build

1. Write the recipe: the steps that make the value.
2. Do not run the recipe when the shop is set up.
3. Hand the property to a helper, and give the helper the recipe.
4. On the first read, the helper runs the recipe and keeps the result.
5. On every later read, the helper hands back the kept result.
6. If nobody ever reads the property, the recipe never runs.

### Name it

A **property delegate** is a helper that takes over the reading, and sometimes the writing, of a property. You give the property to the helper with the word `by`. The Kotlin documentation gives the syntax as `val/var <property name>: <Type> by <expression>`. When you read a delegated property, the helper's `getValue()` function is called.

The helper in this lesson is **`lazy`**. The documentation says `lazy()` is a function that takes a lambda and returns a `Lazy<T>` object that can serve as a delegate. The first call to read the property runs the lambda and remembers the result, and later calls return the remembered result.

```kotlin
val coffee: String by lazy {
    println("brewing")
    "espresso"
}

fun main() {
    println("shop open")
    println(coffee)
    println(coffee)
}
```

It prints `shop open`, `brewing`, `espresso` and `espresso`. The block did not run when the program started. It ran on the first `println(coffee)`, and its last line, the text `espresso`, was kept. The second read printed the kept text and did not print `brewing` again.

A **thread** is one line of work that runs at the same time as other lines of work (general knowledge). The documentation says `lazy` is synchronized by default: if two threads read at once, the value is computed in only one of them, and all of them see the same value. It also names `LazyThreadSafetyMode.NONE`, which drops that protection.

## Predict the output

```challenge
type: predict
run: false
id: brew-on-first-press
verified: not-run
code: |
  class Shop {
      val coffee: String by lazy {
          println("brewing")
          "espresso"
      }
  }

  fun main() {
      val shop = Shop()
      println("opened")
      println(shop.coffee)
      println(shop.coffee)
  }
options:
  - id: a
    text: "brewing, opened, espresso, espresso"
    why: "You may have thought the machine brews when the shop is built. Building the shop only hands the job to the machine."
  - id: b
    text: "opened, brewing, espresso, brewing, espresso"
    why: "You may have thought every press brews again. Only the first press runs the recipe, and later presses pour from the warm pot."
  - id: c
    text: "opened, brewing, espresso, espresso"
    correct: true
    why: "Nothing brews at opening. The first press brews one pot, and the second press pours from the same pot."
output: |
  opened
  brewing
  espresso
  espresso
```

```challenge
type: predict
run: false
id: nobody-presses
verified: not-run
code: |
  class Shop {
      val coffee: String by lazy {
          println("brewing")
          "espresso"
      }
  }

  fun main() {
      Shop()
      println("closed")
  }
options:
  - id: a
    text: "brewing and then closed"
    why: "You may have expected the pot to brew as the shop is built. With no press, the machine stays off."
  - id: b
    text: "closed"
    correct: true
    why: "Nobody pressed the button, so the recipe never ran and brewing was never printed."
  - id: c
    text: "closed and then brewing"
    why: "You may have thought the brew happens at the end of the program. It happens only on a read, and there was none."
output: |
  closed
```

## Tricks to remember

- `by` means "this helper answers for the property".
- `lazy` brews on the first read, keeps the pot, and never brews again.
- No read means no brew.
- Heavy and maybe-never-needed work belongs in `lazy`. Cheap work can stay a plain property.

## Common mistakes

- **Thinking the `lazy` block runs when the object is created.** The line sits inside the class, so it feels like it runs with the other setup lines. It runs on the first read. Add a print inside the block to see when.
- **Expecting the block to run again on every read.** A block with curly brackets looks like a function call. The first result is kept, so later reads return the same value. If you need a fresh value each time, use a function.
- **Putting cheap work in `lazy`.** It sounds like a free speed-up. Each lazy property has a helper object and some bookkeeping (general knowledge). Use it when the work is heavy or may never be needed.
- **Using `NONE` mode in code that many threads share.** It feels faster. The documentation says `LazyThreadSafetyMode.NONE` has no thread-safety guarantees, so use it only when the first read always happens on one thread.

## Where this shows up in Android

Picture a screen where a holder for the user's name is made only when the screen first needs it. The Android LiveData page declares `val currentName: MutableLiveData<String> by lazy { MutableLiveData<String>() }`. That is a coffee machine: the holder is built on the first read and reused after that.

Two other `by` lines appear all over Android code, and this lesson only names them. The ViewModel page shows `val viewModel: MyViewModel by viewModels()`, which is a helper that supplies the ViewModel. The Compose state page shows `var value by remember { mutableStateOf(default) }` and says the `by` syntax needs the `getValue` and `setValue` imports. In both lines, the word `by` hands the property to a helper.

## Say it in an interview

**What does the `by` keyword mean in `val x by lazy { ... }`?**
It hands the property to a delegate, a helper object that answers for it. The Kotlin documentation says reading the property calls the delegate's `getValue()`. With `lazy`, the first read runs the block and remembers the result, and later reads return that result.

**How do `lazy`, `observable` and `vetoable` differ?**
`lazy` computes a value once, on first read. The documentation says `Delegates.observable()` takes an initial value and a handler that is called every time you assign to the property, after the assignment. `vetoable()` is for handlers that can reject a change, and the documentation says its handler is called before the new value is assigned.

**Is `lazy` thread-safe?**
By default, yes. The documentation says the evaluation is synchronized by default: the value is computed in only one thread, and every thread sees the same value. `LazyThreadSafetyMode.PUBLICATION` and `NONE` loosen this.

**What goes wrong with `LazyThreadSafetyMode.NONE`?**
The documentation says it has no thread-safety guarantees, and is for when the first read always happens on the same thread. My own reasoning: if two threads do read it at the same time, the block could run twice and the threads could see different values, which is a bug that rarely shows up in testing.

## Recap

1. `by` hands a property to a helper that answers for it.
2. A `lazy` property runs its block on the first read and keeps the result.
3. If nobody reads a `lazy` property, its block never runs.

## You can now

- Predict the order of prints when a `lazy` property is read twice.
- Predict that a `lazy` block never runs when nobody reads the property.
