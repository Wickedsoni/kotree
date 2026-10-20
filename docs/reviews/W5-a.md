# Review W5-a: eight shadow leaves (how-an-app-is-born end, first-programs, types-and-variables)

Date 21 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31 and L33. Voice: L29. Nothing in the leaves was edited.
Checked against `docs/lesson-checklist.md` as it stands now (item 11 was changed during this review to `## Say it in an interview`, placed after "Where this shows up in Android" and before "Recap"; `content/_templates/leaf.md` line 194 has it), `content/_templates/leaf.md`, `docs/04`, `docs/06` §3, L29, hard rules.

VERDICT: FIX (0 PASS, 8 FIX). Every item is mechanical (`fixer` can do it; replacement text is given). Four items need an owner decision outside the leaves and are listed in "Owner items".

| Leaf | Verdict | Items |
|---|---|---|
| `foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon` | FIX | 6 |
| `foundation/how-an-app-is-born/run-your-first-app` | FIX | 4 |
| `kotlin/first-programs/where-a-program-starts` | FIX | 2 |
| `kotlin/first-programs/print-values-in-text` | FIX | 2 |
| `kotlin/first-programs/shape-of-a-kotlin-file` | FIX | 2 |
| `kotlin/types-and-variables/val-vs-var` | FIX | 4 |
| `kotlin/types-and-variables/basic-types` | FIX | 4 |
| `kotlin/types-and-variables/type-inference` | FIX | 1 |

Common FIX in all eight (checklist 11): none of the eight has a `## Say it in an interview` section. Insert it directly before `## Recap`. Drafted text is in each leaf's last item. All facts in the drafts were checked against the pages named there; points that are mine are labelled in the text.

## What passed on all eight

- Frontmatter: keys and order match `docs/00` §8.2; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/foundation/_roadmap.md` lines 189-198, `content/kotlin/_roadmap.md` lines 9-44); `order` 33, 34 and 1 to 6 are in sequence; `summary` under 25 words and not a copy of the title; `status: shadow`; `video: ""`; dates empty; `upnext: false`.
- `playground: false`, every Predict `run: false` and `verified: not-run`, no Try it, no Fix the bug. This is allowed by `docs/06` §3.1 and was accepted in W4-a. `sims` equals the SIM marker (tap: `hotel-bell-startup`; val-vs-var: `mug-and-whiteboard-tag`; others `[]` with `none:` in the roadmap row). Both ids are in `docs/sims/needed-ids.md` lines 27-28.
- Nine headings, or ten with See it move on the two sim leaves (twelve minus Try it and Fix the bug, minus See it move where `sims: []`), exact names, correct order. No empty heading, no raw HTML, no Video or Capstone text, no VERIFY marks, SOURCES comment on line 18. No emoji. Recap: three lines, one sentence each, no semicolons, under 20 words. Question blocks: 4 to 5 sentences, a `?`, no code, no bold. "Where the analogy stops" present. One analogy per leaf. Notice, Guess and Build hold no Kotlin syntax. Correct letters vary (tap c b a; run a c b; where b a c; print b c a; shape b a c; val a c b; basic c b a; type a c b). Prose length 1,100 to 1,550 words.
- Banned words (T4): one hit, see tap item 1.

## Code and Predict keys (my own run)

No `kotlinc` is installed, but Gradle's bundled compiler is: I ran `K2JVMCompiler` from `kotlin-compiler-embeddable-2.3.21.jar` (K2, Kotlin 2.3.21; the pinned 2.4.21 is not on this machine) directly, not through Gradle. I extracted all 8 `kotlin` blocks and all 18 challenge `code` fields of the six Kotlin leaves (basic-types `four-look-alikes` was wrapped in `fun main` and printed `[Int, Double, Char, String]`). Tap and run have no code.

- All 21 programs that should compile did, with 0 warnings. Every output equals the text, the `output` field and the key: `Start/Hello from greet/End`; `B A C`; `Front door`; `C`; `Hi, Sam, you are 20 / Total: 7 / KotlinLang`; `Total: 8`; `Name: Sam.length`; `KotlinLang / Kotlin Lang`; `Good morning / Done`; `Hello / Done`; `A / E`; `Space Run: 2 lives left`; `6`; `Moon Walk: 6 steps left`; `Maya, 15, 19.99, true, A`; `2 / 2.5`; `19.99 / 19`; `5 19.99 A 3000000000 5`; `3 / 3.5`; `2000000 / 3000000000`.
- Every claimed "does not compile" really fails: val-vs-var `title = "Moon Walk"` and Predict line 5 (`'val' cannot be reassigned`); type-inference `count = "five"` and Predict line 4 (`assignment type mismatch`); shape `lid-never-closed` (`Expecting '}'`). The line numbers in the keys (line 5, line 4, line 5 for the closing brace) are right.
- Distractor numbers are right: `3_000_000_000 - 2^32 = -1294967296`; skipping line 3 gives 4; `+=` read as `=` gives 3.
- Note, not FIX: three compile-failure Predicts (`lid-never-closed`, `which-line-is-refused`, `text-into-a-number-name`) put a sentence in `output`. Real stdout is empty. Accepted because `run: false` items show `output` as the answer (`docs/06` §3.2), as in W4-a.

## Fact check (official pages read 21 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Cold, warm, hot start wording; the system's three tasks (load and launch, blank starting window, create process); the app process's five steps in the leaf's order; blank window stays until first draw, then swapped; TTID = time to first frame, reported automatically | developer.android.com/topic/performance/vitals/launch-time | Matches, except the order of the starting window and the process (tap item 5) |
| Every app runs in its own Linux process; system starts it when a component needs it and shuts it down when no longer needed or memory is needed; "no single entry point: there's no `main()` function"; each component is an entry point; activity = entry point for the user, one screen | developer.android.com/guide/components/fundamentals | Matches, but the page says "By default" (tap item 3) |
| Main thread "in charge of dispatching events ... including drawing events"; process may be shut down for other processes | .../processes-and-threads | Matches |
| Main activity is "the first screen to appear when the user launches the app" | .../activities/intro-activities | Matches |
| Android Studio is "the official IDE for Android app development" | developer.android.com/studio/intro | Matches |
| New Project, Empty Activity recommended for Compose, Name / Package name / Save location / Minimum API level, Finish "with some basic code and resources" | .../projects/create-project | Matches |
| Tap Build Number seven times; Pixel path Settings > About phone; "might be located or named differently"; USB debugging through adb; Wireless debugging | .../debug/dev-options | Matches |
| Run configurations menu, target device menu, Run; debug version "intended for use only during development"; RSA key dialog; unlock and acknowledge; Troubleshoot Device Connections; Restart ADB server; Windows USB driver "(if applicable)"; `adb devices`; wireless on Android 11+; no store or account in the steps | .../studio/run and .../studio/run/device | Matches |
| `var name by remember { mutableStateOf("") }`, `onValueChange = { name = it }`, "nothing happens" when `value` does not change | .../compose/state | Matches |
| `Text(text = "Hello $name!")`; `Column` "arrange elements vertically" with two `Text` calls | .../compose/tutorial | Matches |
| `amountInput` line, `toDoubleOrNull() ?: 0.0`, `tipPercent` line, "could be null if the text field is empty" | .../codelabs/basic-android-kotlin-compose-calculate-tip | Matches (the null sentence is about `tipPercent`) |
| `main` is the entry point; second form with `Array<String>`; `println` adds a line break; val once, var reassign, `x += 1`; nested block comments; `val x = 5` infers Int | kotlinlang.org/docs/basic-syntax.html | Matches; second form not in the leaf (where item 1) |
| Templates embed variables and expressions; `${}` for expressions or text next to a variable; `"${language}Lang"`; "usually easier to read and more idiomatic" | .../strings.html | Matches |
| Four spaces; "Omit semicolons whenever possible"; line breaks significant | .../coding-conventions.html | Matches |
| Int range; Int inferred if it fits else Long; L suffix; Double by default; not subtypes, explicit conversions; `5 / 2` is 2 | .../numbers.html | Matches |
| `Char` "is not a numeric type"; Char inferred from single quotes | .../characters.html | Matches |
| `Double.toInt`: fractional part rounded down towards zero | .../kotlin/-double/to-int.html | Matches |
| "Use `val` as much as possible"; write ops on a mutable collection allowed under a `val`; reassign is a compile error | .../collections-overview.html | Matches |
| Every variable has a type; types tell the compiler what you may do; "type inference"; `if` condition in parentheses, action in braces; body of a function in curly braces | .../kotlin-tour-basic-types.html, .../kotlin-tour-hello-world.html, .../control-flow.html | Matches |

Could not verify from an official page (all labelled in the leaves unless item says otherwise): the plain definition of "process" (tap item 2, unlabelled); that the home screen passes the tap to the system (labelled own simplification); "no paid account" (absence of evidence, labelled "my own reading"); anything on Kotlin 2.4.21. `play.kotlinlang.org` was not needed.

## Leaf 1: what-happens-when-i-tap-an-icon: FIX

1. Line 124 (Predict `step-before-the-first-draw`, option c `why`), banned word "just" (T4). Old: `The furniture is set out just before the lights come on.` New: `The furniture is set out right before the lights come on.`
2. Line 78 (Name it), general knowledge is not labelled (checklist 4). Old: `A **process** is a running app together with the memory the system gave it.` New: `A **process** is a running app together with the memory the system gave it. That plain meaning is general knowledge, not a quote from the Android pages.`
3. Line 78 (Name it), "by default" dropped, and a 37-word sentence (T7). Old: `The Android documentation says each app runs in its own Linux process. It says the system starts the process when any of the app's parts need to run, and shuts it down when it is no longer needed or when the system must get memory back for other apps.` New: `The Android documentation says that by default each app runs in its own Linux process. It says the system starts the process when any of the app's parts need to run. It shuts the process down when it is no longer needed, or when the system must get memory back for other apps.`
4. Line 80 (Name it), 33 words, two ideas (T7). Old: `The documentation says a **warm start** does a subset of the cold start's work, and a **hot start** has lower overhead still, because the system brings the app's main screen to the foreground.` New: `The documentation says a **warm start** does a subset of the cold start's work. A **hot start** has lower overhead still, because the system brings the app's main screen to the foreground.`
5. Lines 70-71 (Build steps 4 and 5) and line 144 (Predict `what-the-guest-sees-first`, option b `why`), the order disagrees with the launch-time page. The page lists the system's cold-start tasks as: load and launch the app, display a blank starting window immediately after launch, create the app process. The leaf makes the room first and then hangs the card "at once", and its Predict `why` says the window is shown "while the process is created". The page does not say the list is strictly timed, so make the text order-neutral and label the reading. Old (step 4): `4. If no room is open, the manager gets a new one ready.` New: `4. If no room is open, the manager starts getting a new one ready.` Old (step 5): `5. At once, the manager hangs the blank welcome card in the doorway.` New: `5. Straight away, the manager hangs the blank welcome card in the doorway. The documentation lists the card before the room is made. My own reasoning: the two happen close together.` Old (line 144): `The documentation says the system shows a blank starting window while the process is created.` New: `The documentation says the system shows a blank starting window immediately after launch.` Step 8 (`starts again from step 4`) stays correct.
6. Insert before `## Recap` (line 194), and also line 29 (Mental model uses "process" before it is defined; checklist 3). Old (line 29): `A room is a private place for the app to run in. It is the app's process.` New: `A room is a private place for the app to run in. It stands for a running copy of the app.` Then insert:

```
## Say it in an interview

**What are cold, warm and hot starts?**
A cold start is an app starting from scratch, with no process yet. A warm start does a subset of that work. A hot start has lower overhead still, because the system only brings the app's screen to the foreground. The trade-off is time: the more has to be built, the longer the wait.

**What happens between the tap and the first frame on a cold start?**
The launch-time page lists the system's tasks as: load and launch the app, show a blank starting window, and create the app process. Then the app process creates the app object, launches the main thread, creates the main activity, initializes the UI and draws it. After the first draw, the system swaps the blank window for the main activity.

**Why does the system show a blank window first?**
My own reasoning: it answers the tap at once, so the phone does not look frozen while the process is made. The cost is that slow work in the five steps keeps the blank window up longer. The documentation measures this wait as time to initial display, the time to show the first frame.

**Is an app always running in the background?**
No. The documentation says the system starts the process when a component needs it. It may shut the process down when it is no longer needed or when memory is needed for other apps. So the next tap may be a cold start.
```

## Leaf 2: run-your-first-app: FIX

1. Line 58 (Build step 3), "Package name" and "Minimum API level" are never explained (checklist 3). Old: `3. Fill in the Name, the Package name, the Save location and the Minimum API level, and press Finish. Android Studio makes a project with some basic code and resources.` New: `3. Fill in the Name, the Package name, the Save location and the Minimum API level, and press Finish. The documentation says the Package name also becomes the app's application ID, which is the ID used for publishing. The Minimum API level is the oldest Android version you want your app to support. Android Studio makes a project with some basic code and resources.`
2. Line 68 (Name it), "Compose" is defined but not bold (T2). Old: `Compose is the toolkit for building Android screens in Kotlin.` New: `**Compose** is the toolkit for building Android screens in Kotlin.`
3. Lines 128-129 (Predict `phone-not-in-the-list`, option b), the distractor is not plausible as a next step (checklist 6; the store idea is already Predict 3). Old: `text: "Publish the app to a store first"` / `why: "You may have thought the store is the only door. The device page does not list a store step, and the phone is waiting on a dialog."` New: `text: "Switch USB debugging off and on again"` / `why: "You may have thought the switch got stuck. The setting is on, and the phone is still waiting for an answer to its question."`
4. Insert before `## Recap` (line 181):

```
## Say it in an interview

**What do you set up so Android Studio can put an app on your phone?**
You switch on Developer options by tapping Build number seven times, then switch on USB debugging. On Windows you may also need a USB driver, if applicable. When you plug in, you unlock the phone and approve this computer. The documentation lists these steps.

**What is adb?**
It stands for Android Debug Bridge. The documentation says USB debugging lets your phone communicate with your computer through adb. Running `adb devices` lists the devices that are connected.

**The phone does not show up in Android Studio. What do you check?**
First, is the phone unlocked, and did you answer its question? Then open Tools, Troubleshoot Device Connections. If the phone still does not appear, the same assistant has a Restart ADB server button. The order is my own reasoning. The tools are from the documentation.

**Is the app you press Run on the app you ship?**
No. The documentation says Android Studio builds the debug version by default, and that it is for development only. The version for other people is signed, as in the install lesson. The trade-off: the debug version is quick to test with, but it is not meant to be released.
```

## Leaf 3: where-a-program-starts: FIX

1. Line 71 (Name it), the second documented form of `main` is missing; a fresher meets `fun main(args: Array<String>)` in older code. Old: `The front door is called the **entry point**. The Kotlin documentation says the entry point of a Kotlin application is the `main` function.` New: `The front door is called the **entry point**. The Kotlin documentation says the entry point of a Kotlin application is the `main` function. It shows two forms: `fun main()` and `fun main(args: Array<String>)`. The second accepts a variable number of `String` arguments, which means pieces of text handed to the program when it starts (that plain meaning is general knowledge). Both start the program. This lesson uses the short form.`
2. Insert before `## Recap` (line 207):

```
## Say it in an interview

**What is the entry point of a Kotlin program, and what forms can it take?**
The entry point is the `main` function. The Kotlin documentation shows two forms: `fun main()` and `fun main(args: Array<String>)`. The second accepts a variable number of `String` arguments, which are pieces of text handed to the program when it starts. Both start the program.

**Does `main` have to sit inside a class, like in Java?**
No. In Kotlin, `main` can be a top-level function, which means a function written straight in the file, not inside any class. The Kotlin documentation's examples do exactly that.

**Does a function written above `main` run first?**
No. Only a call decides when a function runs. The order in the file is not the order of running. A function that nobody calls never runs.

**Why does an Android app have no `main`?**
The Android documentation says Android apps don't have a single entry point: there's no `main()` function. Each component is an entry point, and the system decides which one to start. My own reasoning: the trade-off is that your code can be entered at a screen, a service or a broadcast, so you cannot assume one fixed start.
```

## Leaf 4: print-values-in-text: FIX

1. Line 180 (Where this shows up in Android), "function" and "body" are used undefined on this page (checklist 3 lists "body"). Old: `The Android Compose tutorial has a function called `MessageCard` that is passed a name. Its body is `Text(text = "Hello $name!")`.` New: `The Android Compose tutorial has a piece of code called `MessageCard`. It is given a name and runs the line `Text(text = "Hello $name!")`.`
2. Insert before `## Recap` (line 184):

```
## Say it in an interview

**What is a string template?**
It is a string with `$name` or `${expression}` marks that Kotlin fills in. The Kotlin documentation says templates let you embed variables and expressions directly inside a piece of text in quotes.

**Why use a template instead of the plus sign?**
Both work. The documentation says templates are usually easier to read and more idiomatic. It also says that with the plus sign, Kotlin creates a new `String` for each join, which may not be beneficial when you join many pieces.

**When do you need `${ }` instead of `$`?**
When you insert an expression, or when a variable sits right next to other text. The documentation's example is `"${language}Lang"`, which prints `KotlinLang`.

**How do you print a literal dollar sign?**
The Kotlin characters page lists `\$` as the escape sequence for a dollar sign. So `"\$name"` prints `$name` and does not fill in a value.
```

## Leaf 5: shape-of-a-kotlin-file: FIX

1. Line 70 (Name it), "body", "function" and "condition" are used before they are defined (checklist 3; `if` and the question in parentheses are defined only in the next paragraph). Old: `The Kotlin tour says the body of a function is written within curly braces. The documentation for `if` says the condition goes in parentheses and the action to take goes in curly braces.` New: `The Kotlin tour says the steps of a function are written within curly braces. The documentation for `if` says the check goes in parentheses and the action to take goes in curly braces.`
2. Insert before `## Recap` (line 202):

```
## Say it in an interview

**Does indentation matter in Kotlin, like in Python?**
No. Braces decide which lines belong to a block. The coding conventions say to use four spaces for indentation, which is a style rule for people. My own reasoning: a badly indented file can still compile and mislead the reader, so keep it tidy.

**Do you need semicolons?**
No. The coding conventions say semicolons are optional and tell you to omit them whenever possible. They add that line breaks are therefore significant.

**Can comments be nested?**
Yes. The Kotlin basic syntax page says block comments can be nested. My own reasoning: this lets you comment out code that already holds a block comment.

**What is the difference between a statement and a block?**
A statement is one instruction, such as a `println` call. A block is a group of statements between `{` and `}`. This is general knowledge, in the plain words of this lesson.
```

## Leaf 6: val-vs-var: FIX

1. Line 195 (Common mistakes), "mutable collection" is jargon the learner has not met (checklist 3). Old: `The collections page says write operations on a mutable collection are still possible when it is assigned to a `val`. The tag is locked, and the tea can still change.` New: `The Kotlin collections page shows a list you can add to, assigned to a `val`. Adding to it is allowed. Only giving the name a different list is refused. The tag is locked, and the tea can still change.`
2. Line 201 (Where this shows up in Android), 31-word sentence with a defining aside (T7). Old: `The Android documentation for **Jetpack Compose**, the toolkit for building Android screens in Kotlin, keeps the text a person types in a line like `var name by remember { mutableStateOf("") }`.` New: `**Jetpack Compose** is the toolkit for building Android screens in Kotlin. Its Android documentation keeps the text a person types in a line like `var name by remember { mutableStateOf("") }`.`
3. Line 203 (Where this shows up in Android), "its name needs a whiteboard tag" is too wide. The same Compose page lists `val mutableState = remember { mutableStateOf(default) }` as an equivalent way, where a `val` is fine. Old: `The text in a box changes as the person types, so its name needs a whiteboard tag. My own reasoning: in the same example,` New: `The text in a box changes as the person types. In this line, the name itself is given a new value each time, so it needs a whiteboard tag. My own reasoning: in the same example,`
4. Insert before `## Recap` (line 205):

```
## Say it in an interview

**What is the difference between `val` and `var`?**
A `val` is assigned a value only once and cannot be reassigned. A `var` can be reassigned. The Kotlin documentation calls them read-only and mutable variables.

**Why prefer `val`?**
The collections page says to use `val` as much as possible for safer and more robust code. My own reasoning: with a `val`, no later line can change which value the name means, so there are fewer places to look when a value is wrong. The trade-off: when a value must change, such as a counter, you need a `var`.

**Is `val` the same as immutable?**
No. A `val` stops the name from being reassigned. The collections page shows a list you can add to, assigned to a `val`. Adding to it is allowed, and only giving the name a different list is refused. So `val` locks the tag, not what is inside.

**In Compose, `var name by remember { mutableStateOf("") }` uses `var`. Could it use `val`?**
In another form, yes. Compose keeps the text a person types in state, which is a value the screen reads. The Compose state page lists three equivalent ways to declare it, and one is `val mutableState = remember { mutableStateOf(default) }`. There the `val` keeps the name fixed and the value inside the state changes. My own reasoning: with `by`, the code writes `name = it`, so that form needs a `var`.
```

## Leaf 7: basic-types: FIX

1. Line 70 (Name it), "range" is on the checklist 3 list of undefined terms. Old: `Its range runs from -2,147,483,648 to 2,147,483,647.` New: `Its smallest value is -2,147,483,648 and its largest is 2,147,483,647.`
2. Lines 188 and 193 (Tricks, Common mistakes), industry accuracy (L29): "money never goes in the egg box" and "use a `Double`" are not sound advice for real money. Cents in an `Int` or `Long` is a normal choice; a `Double` is approximate. My run: `0.1 + 0.2` prints `0.30000000000000004`. Old (188): `- Money has a part after the point, so it never goes in the egg box.` New: `- A price with cents does not fit an `Int` of whole units. The cents are cut off.` Old (193): `- **Storing a price in an `Int`.** A price looks like a count of coins. Converting 19.99 to an `Int` leaves 19. Use a `Double` when the value has a part after the point.` New: `- **Storing a price in an `Int` of whole units.** A price looks like a count of coins. Converting 19.99 to an `Int` leaves 19. A `Double` keeps the .99 here. General knowledge, not from the Kotlin pages: a `Double` holds many decimals only approximately, so real money code often keeps cents in a `Long`, or uses `BigDecimal`, a type made for exact decimal numbers.`
3. Line 201 (Where this shows up in Android), `null` and `?:` are used with no meaning. Old: `the codelab says the result could be null if the text field is empty, so the code supplies 0.0.` New: `the codelab says the result could be `null`, which means no number, if the text field is empty. The `?:` mark means "otherwise use": if there is no number, the code uses 0.0.`
4. Insert before `## Recap` (line 203):

```
## Say it in an interview

**What are the sizes and limits of `Int` and `Long`?**
`Int` takes 32 bits, the on-or-off switches a number is stored in, and holds -2,147,483,648 to 2,147,483,647. `Long` takes 64 bits and holds -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807. The Kotlin numbers page lists both. For a plain whole number, Kotlin infers `Int` if it fits and `Long` if it does not.

**Why is `5 / 2` equal to `2`?**
The documentation says division between integer values always returns an integer, and the compiler discards the fractional part. To keep the .5, write `5.0 / 2`, which is a `Double`.

**What happens when an `Int` goes past its maximum?**
General knowledge: it wraps around, so `Int.MAX_VALUE + 1` gives -2147483648. If a count can pass about two billion, use a `Long`. The trade-off is memory: a `Long` is 64 bits and an `Int` is 32.

**Should money be stored in a `Double`?**
General knowledge: usually not. A `Double` holds many decimals only approximately, and `0.1 + 0.2` prints `0.30000000000000004`. Real code often keeps cents in a `Long`, or uses `BigDecimal`, a type made for exact decimal numbers.
```

## Leaf 8: type-inference: FIX

1. Insert before `## Recap` (line 205). Nothing else needs changing.

```
## Say it in an interview

**Is Kotlin dynamically typed because you can leave the type out?**
No. The Kotlin FAQ calls Kotlin statically typed, which means types are checked before the program runs. The tour says every variable has a type, and that when you leave it out the compiler infers it. So the type is fixed before the program runs, and text put into a name inferred as `Int` is refused.

**What types do `val a = 5` and `val b = 3_000_000_000` get?**
`a` is an `Int`, because 5 fits. `b` is a `Long`, because it does not fit in an `Int`. The numbers page says Kotlin infers `Int` if the value fits, otherwise `Long`.

**When should you write the type anyway?**
When you want a type Kotlin would not pick, such as `val small: Long = 5`. The coding conventions also say libraries, which are code other people build on, should always write out the types of what their functions give back, so a change inside does not change them by accident. My own reasoning: for code only you read, inference saves noise. For code other people build on, a written type is a promise.
```

## Owner items (not leaf edits)

1. `docs/04` §4 (table, "twelve body sections"), §9 acceptance and the §10 worked example still list twelve headings. The template and checklist now have a thirteenth, `## Say it in an interview`. I found no code that enforces the heading list (no `.kt` or `.kts` file mentions the heading names), so nothing breaks, but the doc should be updated by the orchestrator so the next reviewer does not read it as a defect.
2. Sim divergence, `val-vs-var`: the roadmap cell (`content/kotlin/_roadmap.md` line 424) asks for "two labelled jars" while the same row's analogy column is a mug and a whiteboard name tag. The leaf uses mugs and tags, and `docs/sims/needed-ids.md` line 28 records why. Judgement: not a rule 5 breach. Rule 5 protects the structure (stems, leaf ids, titles, difficulty, requires, fruit), and V5 checks only those fields, all of which match. The leaf is right on the merits: `docs/04` §3.2 allows one analogy and forbids a second object in later sections, and a jar would be a second object. The roadmap cell should still be aligned so it does not contradict its own analogy: `curriculum-designer` changes the cell to the mug sim. Also in `docs/sims/needed-ids.md` line 27 (`hotel-bell-startup`): it says a room is made ready and then the blank card goes up; make that order-neutral to match tap item 5 (the sim-spec owner, `physics-designer`, or the orchestrator). The same applies to the VERIFY notes in the Foundation roadmap rows 282 and 283: the start-up steps and the Empty Activity / Developer options facts were verified here, so only the "on the publish date" re-check of the Android Studio steps remains.
3. Dependency, Foundation: `foundation/first-code/run-your-first-program` (shadow, order 8) is a transitive requirement of both `what-happens-when-i-tap-an-icon` (via 19 ancestors, one shadow) and `run-your-first-app` (20 ancestors, two shadow: that leaf and the tap leaf). `lines-run-in-order` is already live and requires it, so a live leaf with a shadow prerequisite is not new and `validateContent` allows it (V3 checks only that the id exists and comes earlier). Coherence is otherwise good: the browser leaf says Android Studio is met "in the last lesson of this part", which is `run-your-first-app`, and the phone leaf never contradicts "no project to create" because that sentence is about the Playground. Two things for whoever reviews that leaf next: it still carries `UNVERIFIED` fields and a VERIFY comment (Playground availability), and its sentence "which you meet in the last lesson of this part of the course" is a forward promise (checklist 7). Recommendation: review it in the next wave so the chain is live end to end; it should not block these two.
4. Policy note: the Kotlin leaves are `playground: false`, so the roadmap's `F:` and `B:` tasks (for example the `val` counter, the misspelled `main`) are not present. This follows `docs/06` §3.1 and W4-a. For a fresher this loses the write, run, break, fix loop, which is the main way the site teaches. Once a CI run can confirm outputs, these leaves should be upgraded to `playground: true` with Try it and Fix the bug. My runs above show the existing code would pass such a run.

## Re-check

Date 20 Oct 2026. Independent reviewer, leaves not edited. Scope: every FIX above applied, plus the new `## Say it in an interview` sections.

VERDICT: FIX (4 PASS, 4 FIX). All four remaining items are mechanical (`fixer` can do them; replacement text given).

| Leaf | Re-check |
|---|---|
| `foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon` | FIX (1 item) |
| `foundation/how-an-app-is-born/run-your-first-app` | PASS |
| `kotlin/first-programs/where-a-program-starts` | FIX (1 item) |
| `kotlin/first-programs/print-values-in-text` | PASS |
| `kotlin/first-programs/shape-of-a-kotlin-file` | PASS |
| `kotlin/types-and-variables/val-vs-var` | FIX (1 item) |
| `kotlin/types-and-variables/basic-types` | FIX (1 item) |
| `kotlin/types-and-variables/type-inference` | PASS |

### What I verified

- All FIX items from the sections above are applied, with the replacement text as given (tap 1 to 6, run 1 to 4 with the line 129 wording as drafted, where 1, print 1, shape 1, val 1 to 3, basic 1 to 3). Run item 4 says "the earlier lessons" instead of "the install lesson"; that is fine.
- Each leaf has `## Say it in an interview` after "Where this shows up in Android" and before "Recap", four questions, plain words, why and trade-off, labels for own reasoning and general knowledge (checklist 11). Body headings and order are otherwise unchanged. Frontmatter unchanged. Zero T4 banned words. No internal-process wording in learner text (the SOURCES comment on line 18 is the only place, and it is stripped at go-live). No emoji.
- Code: I compiled the new snippets with the Gradle-bundled K2 compiler 2.3.21 (no `kotlinc` here, 0 warnings): `"\$name"` prints `$name`; `Int.MAX_VALUE + 1` prints `-2147483648`; `0.1 + 0.2` prints `0.30000000000000004`; `5 / 2` prints `2` and `5.0 / 2` prints `2.5`; `val small: Long = 5` compiles; `3_000_000_000` is a `Long`; `Long` min and max are -9223372036854775808 and 9223372036854775807. The eight leaves' code blocks were unchanged since the first run.
- Facts, read 20 Oct 2026 against the official pages: strings.html (`+` creates a new `String` per operation, "may not be beneficial in loops or when you assemble many pieces"; templates "usually easier to read and more idiomatic"; `${language}Lang`; `${}` for an expression or a variable next to text); characters.html (`\$` is "Dollar sign"); coding-conventions.html ("semicolons are optional, and therefore line breaks are significant"; "four spaces"; libraries "always explicitly specify function return types and property types ... to avoid accidentally changing the return type"); numbers.html (integer division returns an integer, "The compiler discards the fractional part"; `Int` 32 bits and `Long` 64 bits with the limits in the leaf; `Int` if it fits, otherwise `Long`); faq.html ("open-source statically typed"); create-project (Package name becomes the namespace and "your project's application ID (used as the ID for publishing)"; Minimum API level is "the API level you want your app to support"; Empty Activity recommended for Compose); studio/run/device (Tools > Troubleshoot Device Connections; Restart ADB server; `adb devices`; unlock and acknowledge the dialog; Windows USB driver "(if applicable)"); compose/state (three equivalent `MutableState` declarations, including `val mutableState = remember { mutableStateOf(default) }`). All match the leaves. Not verifiable from an official page and already labelled: the plain meaning of "process" and "statically typed", `Int` overflow wrap-around, `Double` being approximate, `BigDecimal` for money.

### Remaining FIX

1. `what-happens-when-i-tap-an-icon`, interview answer "Is an app always running in the background?" (line 206): "component" is on the checklist 3 list of undefined terms, and the lesson says "the app's parts". Old: `The documentation says the system starts the process when a component needs it.` New: `The documentation says the system starts the process when any of the app's parts need to run.` Mechanical.
2. `where-a-program-starts`, interview answers on lines 212-213 and 219: "class", "Java", "service" and "broadcast" are never defined, and this is the first lesson of the Kotlin branch. (a) Old question: `**Does `main` have to sit inside a class, like in Java?**` New: `**Does `main` have to sit inside a class?**`. Old answer: `No. In Kotlin, `main` can be a top-level function, which means a function written straight in the file, not inside any class. The Kotlin documentation's examples do exactly that.` New: `No. In Kotlin, `main` can be a top-level function, which means a function written straight in the file. It is not wrapped in a **class**, which is a named container that can hold code. The Kotlin documentation's examples do exactly that. General knowledge: some other languages need that wrapper.` (b) Old: `My own reasoning: the trade-off is that your code can be entered at a screen, a service or a broadcast, so you cannot assume one fixed start.` New: `My own reasoning: the trade-off is that your code can be entered at any component, such as a screen, so you cannot assume one fixed start.` Mechanical.
3. `val-vs-var`, interview answer on line 217: "`by`" and "`it`" are never explained (the lesson tells the reader that only `var` matters in that line). Old: `My own reasoning: with `by`, the code writes `name = it`, so that form needs a `var`.` New: `My own reasoning: in the line with `var`, the example later writes `name = it`, which gives `name` the new text, so that form needs a `var`.` Mechanical.
4. `basic-types`, interview answers on lines 206 and 212: "infers" is not met until the next lesson, and `Int.MAX_VALUE` is an undefined name. Line 206 old: `For a plain whole number, Kotlin infers `Int` if it fits and `Long` if it does not.` New: `For a plain whole number, Kotlin picks `Int` if it fits and `Long` if it does not.` Line 212 old: `General knowledge: it wraps around, so `Int.MAX_VALUE + 1` gives -2147483648.` New: `General knowledge: it wraps around, so the largest `Int`, 2147483647, plus 1 gives -2147483648. My run of Kotlin 2.3.21 printed that.` Mechanical.

Not a defect: run-your-first-app glosses Minimum API level as "the oldest Android version you want your app to support"; the page says "the API level you want your app to support", with a lower level meaning more devices, so the gloss is faithful.
