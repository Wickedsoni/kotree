```yaml
branch: kotlin
bough: kotlin-android
stems:
  - id: first-programs
    title: First Programs
    why: A fresher needs to know where a Kotlin program starts and how its text is shaped before reading any other code.
    leaves:
      - id: where-a-program-starts
        title: Where does a Kotlin program start running?
        difficulty: easy
        requires: []
        fruit: false
      - id: print-values-in-text
        title: How do I put a value inside a sentence I print?
        difficulty: easy
        requires: [kotlin/first-programs/where-a-program-starts]
        fruit: false
      - id: shape-of-a-kotlin-file
        title: How do I read the shape of a Kotlin file?
        difficulty: easy
        requires: [kotlin/first-programs/where-a-program-starts]
        fruit: false
  - id: types-and-variables
    title: Types and Variables
    why: Every Kotlin line names values and their types; misreading them causes most beginner errors.
    leaves:
      - id: val-vs-var
        title: When should a value be allowed to change?
        difficulty: easy
        requires: [kotlin/first-programs/print-values-in-text]
        fruit: false
      - id: basic-types
        title: Which kinds of values does Kotlin know out of the box?
        difficulty: easy
        requires: [kotlin/types-and-variables/val-vs-var]
        fruit: false
      - id: type-inference
        title: How does Kotlin know a type I never wrote?
        difficulty: easy
        requires: [kotlin/types-and-variables/basic-types]
        fruit: false
      - id: number-conversions
        title: Why won't Kotlin put an Int where a Long is expected?
        difficulty: medium
        requires: [kotlin/types-and-variables/basic-types]
        fruit: false
  - id: control-flow
    title: Control Flow
    why: Kotlin's if and when give back values, which is how Android code chooses what to show.
    leaves:
      - id: if-as-an-expression
        title: How can an if hand back a value?
        difficulty: easy
        requires: [kotlin/types-and-variables/val-vs-var]
        fruit: false
      - id: when-expression
        title: How do I choose between many cases without a ladder of ifs?
        difficulty: easy
        requires: [kotlin/control-flow/if-as-an-expression]
        fruit: false
      - id: ranges-and-for-loops
        title: Where does a Kotlin range start and stop?
        difficulty: easy
        requires: [kotlin/control-flow/if-as-an-expression]
        fruit: false
      - id: break-and-continue
        title: How do I stop a loop early or skip one turn?
        difficulty: easy
        requires: [kotlin/control-flow/ranges-and-for-loops]
        fruit: false
  - id: functions
    title: Functions
    why: Kotlin functions have defaults, named arguments and short forms that appear in every Android file.
    leaves:
      - id: declaring-functions
        title: How do I write a function in Kotlin?
        difficulty: easy
        requires: [kotlin/control-flow/if-as-an-expression]
        fruit: false
      - id: default-and-named-arguments
        title: How can a function have optional inputs?
        difficulty: easy
        requires: [kotlin/functions/declaring-functions]
        fruit: false
      - id: single-expression-functions
        title: Why do some Kotlin functions use = instead of braces?
        difficulty: easy
        requires: [kotlin/functions/declaring-functions]
        fruit: false
      - id: unit-and-nothing
        title: What does a function return when it has nothing to give back?
        difficulty: easy
        requires: [kotlin/functions/declaring-functions]
        fruit: false
      - id: scope-and-shadowing
        title: Why can't I use a variable outside the braces where I made it?
        difficulty: medium
        requires: [kotlin/functions/declaring-functions, kotlin/control-flow/ranges-and-for-loops]
        fruit: false
  - id: null-safety
    title: Null Safety
    why: Missing values are the most common crash in apps, and Kotlin's type system is built to stop them.
    leaves:
      - id: what-is-null
        title: What is null and why does it crash apps?
        difficulty: easy
        requires: [kotlin/types-and-variables/basic-types]
        fruit: false
      - id: nullable-types
        title: How does Kotlin mark a value that might be missing?
        difficulty: easy
        requires: [kotlin/null-safety/what-is-null]
        fruit: false
      - id: safe-call-and-elvis
        title: How do I use a maybe-missing value without crashing?
        difficulty: medium
        requires: [kotlin/null-safety/nullable-types]
        fruit: false
      - id: smart-casts
        title: How does Kotlin know a value is not null after I check it?
        difficulty: medium
        requires: [kotlin/null-safety/nullable-types, kotlin/control-flow/if-as-an-expression]
        fruit: false
      - id: when-to-use-double-bang
        title: When is !! ever the right choice?
        difficulty: medium
        requires: [kotlin/null-safety/safe-call-and-elvis]
        fruit: false
  - id: strings
    title: Strings
    why: Apps show, read and check text all day, and user input always arrives as text.
    leaves:
      - id: strings-are-sequences
        title: How do I get one character out of a String?
        difficulty: easy
        requires: [kotlin/types-and-variables/basic-types]
        fruit: false
      - id: everyday-string-functions
        title: Which String functions do I need every day?
        difficulty: easy
        requires: [kotlin/strings/strings-are-sequences, kotlin/functions/declaring-functions]
        fruit: false
      - id: text-to-numbers
        title: How do I turn the text "42" into a number without crashing?
        difficulty: medium
        requires: [kotlin/strings/strings-are-sequences, kotlin/null-safety/safe-call-and-elvis]
        fruit: false
  - id: collections
    title: Collections
    why: Every screen shows a list of something, so a fresher must hold, find and look up many values.
    leaves:
      - id: list-vs-mutable-list
        title: Why can't I add an item to a List?
        difficulty: easy
        requires: [kotlin/types-and-variables/val-vs-var]
        fruit: false
      - id: index-out-of-bounds
        title: Why does asking for item 5 crash a list of five items?
        difficulty: easy
        requires: [kotlin/collections/list-vs-mutable-list, kotlin/null-safety/nullable-types]
        fruit: false
      - id: sets
        title: How do I keep only unique values?
        difficulty: easy
        requires: [kotlin/collections/list-vs-mutable-list]
        fruit: false
      - id: maps
        title: How do I look up a value by its key?
        difficulty: easy
        requires: [kotlin/collections/list-vs-mutable-list, kotlin/null-safety/nullable-types]
        fruit: false
      - id: pairs-and-destructuring
        title: How do I unpack two values at once?
        difficulty: easy
        requires: [kotlin/collections/maps]
        fruit: false
      - id: arrays-vs-lists
        title: When does Kotlin code use an Array instead of a List?
        difficulty: easy
        requires: [kotlin/collections/list-vs-mutable-list]
        fruit: false
  - id: lambdas
    title: Lambdas
    why: Compose, click handlers and list operations are all lambdas, so no Android code reads clearly without them.
    leaves:
      - id: functions-as-values
        title: How can a function be stored in a variable?
        difficulty: medium
        requires: [kotlin/functions/declaring-functions]
        fruit: false
      - id: lambda-syntax
        title: How do I read a lambda, even when it sits outside the brackets?
        difficulty: medium
        requires: [kotlin/lambdas/functions-as-values]
        fruit: false
      - id: higher-order-functions
        title: How do I write a function that takes another function?
        difficulty: medium
        requires: [kotlin/lambdas/lambda-syntax]
        fruit: false
      - id: map-filter-and-friends
        title: How do I transform a list without writing a loop?
        difficulty: medium
        requires: [kotlin/lambdas/lambda-syntax, kotlin/collections/list-vs-mutable-list]
        fruit: false
      - id: closures
        title: How does a lambda remember variables from outside it?
        difficulty: medium
        requires: [kotlin/lambdas/lambda-syntax, kotlin/functions/scope-and-shadowing]
        fruit: false
  - id: classes-and-objects
    title: Classes and Objects
    why: Android code is built from classes, and most hidden bugs come from not knowing that objects are shared by reference.
    leaves:
      - id: what-is-a-class
        title: How do I bundle data and the actions on it into one thing?
        difficulty: easy
        requires: [kotlin/functions/declaring-functions, kotlin/types-and-variables/val-vs-var]
        fruit: false
      - id: constructors
        title: How does a new object get its starting values?
        difficulty: easy
        requires: [kotlin/classes-and-objects/what-is-a-class]
        fruit: false
      - id: member-functions
        title: How does an object do things with its own data?
        difficulty: easy
        requires: [kotlin/classes-and-objects/constructors]
        fruit: false
      - id: private-and-public
        title: Why hide some parts of a class with private?
        difficulty: easy
        requires: [kotlin/classes-and-objects/member-functions]
        fruit: false
      - id: getters-and-setters
        title: How can a property run code when it is read or written?
        difficulty: medium
        requires: [kotlin/classes-and-objects/private-and-public]
        fruit: false
      - id: objects-are-references
        title: Why does changing one variable change another?
        difficulty: hard
        requires: [kotlin/classes-and-objects/constructors]
        fruit: false
      - id: two-kinds-of-equality
        title: When are two objects equal in Kotlin?
        difficulty: medium
        requires: [kotlin/classes-and-objects/objects-are-references]
        fruit: false
      - id: read-only-is-not-immutable
        title: Can a read-only list still change under me?
        difficulty: medium
        requires: [kotlin/classes-and-objects/objects-are-references, kotlin/collections/list-vs-mutable-list]
        fruit: false
      - id: model-a-small-program
        title: How do I turn a real-world problem into classes and functions?
        difficulty: medium
        requires: [kotlin/classes-and-objects/private-and-public, kotlin/lambdas/map-filter-and-friends]
        fruit: true
  - id: interfaces-and-inheritance
    title: Interfaces and Inheritance
    why: Android and library code is full of interfaces and overridden functions that a fresher must follow.
    leaves:
      - id: what-is-an-interface
        title: How can different classes promise the same ability?
        difficulty: medium
        requires: [kotlin/classes-and-objects/member-functions]
        fruit: false
      - id: open-and-override
        title: How does one class build on another class?
        difficulty: medium
        requires: [kotlin/interfaces-and-inheritance/what-is-an-interface]
        fruit: false
      - id: abstract-classes
        title: When should I use an abstract class instead of an interface?
        difficulty: medium
        requires: [kotlin/interfaces-and-inheritance/open-and-override]
        fruit: false
      - id: polymorphism-and-type-checks
        title: How can one variable safely hold many kinds of objects?
        difficulty: hard
        requires: [kotlin/interfaces-and-inheritance/what-is-an-interface, kotlin/null-safety/smart-casts]
        fruit: false
  - id: special-classes
    title: Special Classes
    why: Data, enum, sealed and object declarations model app state and appear in almost every generated Android file.
    leaves:
      - id: data-classes
        title: Why does Kotlin write equals and toString for some classes?
        difficulty: medium
        requires: [kotlin/classes-and-objects/two-kinds-of-equality]
        fruit: false
      - id: data-class-copy
        title: How do I change one field without changing the original object?
        difficulty: medium
        requires: [kotlin/special-classes/data-classes, kotlin/collections/pairs-and-destructuring]
        fruit: false
      - id: enum-classes
        title: How do I list every allowed choice, like the days of the week?
        difficulty: easy
        requires: [kotlin/control-flow/when-expression]
        fruit: false
      - id: sealed-classes
        title: How do I model loading, success or error so I never forget a case?
        difficulty: hard
        requires: [kotlin/special-classes/enum-classes, kotlin/interfaces-and-inheritance/polymorphism-and-type-checks]
        fruit: false
      - id: object-declarations
        title: How do I make exactly one of something in the whole program?
        difficulty: medium
        requires: [kotlin/classes-and-objects/constructors]
        fruit: false
  - id: generics
    title: Generics
    why: Types like List<String> and Result<User> are everywhere, and a fresher must read the angle brackets.
    leaves:
      - id: what-are-generics
        title: How can one List type hold Strings in one place and Ints in another?
        difficulty: medium
        requires: [kotlin/collections/list-vs-mutable-list, kotlin/interfaces-and-inheritance/what-is-an-interface]
        fruit: false
      - id: writing-generic-code
        title: How do I write a function that works for any type?
        difficulty: medium
        requires: [kotlin/generics/what-are-generics]
        fruit: false
  - id: extensions-scope-and-delegates
    title: Extensions, Scope Functions and Delegates
    why: let, apply, extension functions and by lazy fill AI-written Android code, and they hide what runs and when.
    leaves:
      - id: extension-functions
        title: How can I add a function to a class I did not write?
        difficulty: medium
        requires: [kotlin/classes-and-objects/member-functions]
        fruit: false
      - id: scope-functions
        title: What do let, apply, also, run and with actually do?
        difficulty: hard
        requires: [kotlin/extensions-scope-and-delegates/extension-functions, kotlin/lambdas/closures, kotlin/null-safety/safe-call-and-elvis]
        fruit: false
      - id: lateinit
        title: How can a property be filled in after the object is made?
        difficulty: medium
        requires: [kotlin/classes-and-objects/constructors, kotlin/null-safety/nullable-types]
        fruit: false
      - id: property-delegates
        title: What does by mean in val x by lazy?
        difficulty: medium
        requires: [kotlin/extensions-scope-and-delegates/lateinit, kotlin/lambdas/lambda-syntax]
        fruit: false
  - id: errors-and-results
    title: Errors and Results
    why: Apps meet bad input and failed requests all the time, and must fail politely instead of crashing.
    leaves:
      - id: exceptions
        title: What happens to my program when code throws an exception?
        difficulty: medium
        requires: [kotlin/functions/declaring-functions, kotlin/classes-and-objects/what-is-a-class]
        fruit: false
      - id: try-catch-finally
        title: How do I catch a failure and keep going?
        difficulty: medium
        requires: [kotlin/errors-and-results/exceptions]
        fruit: false
      - id: result-and-runcatching
        title: How can a function hand back either a value or a failure without throwing?
        difficulty: hard
        requires: [kotlin/errors-and-results/try-catch-finally, kotlin/special-classes/sealed-classes]
        fruit: false
  - id: reading-code
    title: Reading Code
    why: A one-person team reads far more code than it writes, much of it generated, and must spot risks fast.
    leaves:
      - id: reading-order-for-code
        title: In what order should I read an unfamiliar Kotlin file?
        difficulty: medium
        requires: [kotlin/null-safety/when-to-use-double-bang, kotlin/extensions-scope-and-delegates/scope-functions]
        fruit: false
      - id: spotting-risky-patterns
        title: Which lines in tidy-looking Kotlin deserve a second look?
        difficulty: hard
        requires: [kotlin/reading-code/reading-order-for-code, kotlin/errors-and-results/result-and-runcatching]
        fruit: false
      - id: review-a-generated-file
        title: Would I accept this generated file in review?
        difficulty: hard
        requires: [kotlin/reading-code/spotting-risky-patterns]
        fruit: true
```

## Branch prerequisites

- Assumes Foundation: `foundation/first-code/run-your-first-program` — every leaf runs code in the Kotlin Playground.
- Assumes Foundation: `foundation/programming-thinking/trace-code-by-hand` — variables, decisions, loops, lists and functions are not re-taught from zero; this branch teaches the Kotlin way of writing them.
- Assumes Foundation: `foundation/debugging-mindset/reading-error-messages` — compiler errors are used as teaching material from the first stem.
- Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write` — the reading-code stem builds on this checklist.
- Assumes Foundation: `foundation/from-code-to-running/what-is-the-jvm` — mentioned when the branch explains `Int` versus `Long` and `==` versus `===`.

Foundation is recommended first ("Start here"). After Foundation, this is the recommended next branch. No Android APIs are used: every example runs in the browser editor.

All 65 leaves are `shadow` on 18 Oct 2026. Nothing is published.

⚠️ VERIFY before each leaf goes live: the default Kotlin version of the embedded Kotlin Playground, so every example compiles there.

## Stems

### first-programs · First Programs

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `where-a-program-starts` | easy | "Which function runs first when a Kotlin program starts?" | The front door of a house: every visitor comes in through it, whatever room they want. | "Kotlin runs the file from the first line downwards, like a list." | `none:` a single rule about the entry point; the editor run button shows it. | `P:` which line prints first when a helper function sits above `main` · `F:` a program with `main` misspelled | Assumes Foundation: `foundation/first-code/lines-run-in-order` |
| `print-values-in-text` | easy | "How do I print "Hi, Sam, you are 20" from two variables?" | A form letter with blanks that get filled in with each person's name. | "I must glue text and numbers with + every time." | `none:` syntax rule (string templates); the editor is enough. | `P:` output of `"Total: ${a + b}"` · `F:` `$name.length` prints the name followed by ".length" | — |
| `shape-of-a-kotlin-file` | easy | "Which braces belong to which block in this file?" | Nested boxes in a parcel: every lid must close the box it opened. | "Indentation decides what belongs together, like in Python." | `none:` a reading skill; colour-matched braces in a static code view are clearer. | `P:` which line is inside the `if` block · `F:` a missing closing brace far from the error message · `B:` delete one brace | Covers statements, blocks, comments. |

### types-and-variables · Types and Variables

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `val-vs-var` | easy | "Why would I choose `val` when `var` lets me change the value later?" | A name printed on a mug (val) versus a whiteboard name tag you can wipe (var). | "`val` means the value itself can never change inside." | `sim:` `mug-and-whiteboard-tag`: a shelf with mugs, one printed name tag ("price") and one whiteboard tag ("lives"); dragging the printed tag to another mug makes it snap back, dragging the whiteboard tag moves it; an optional "Pour tea" button adds tea to the mug under the printed tag, which stays allowed. | `P:` which line fails to compile · `F:` a counter declared with `val` · `B:` change every `val` to `var` and see nothing complains | The "inside can still change" point returns in `read-only-is-not-immutable`. |
| `basic-types` | easy | "Which type should hold an age, a price, a yes/no answer and a single letter?" | Different containers in a kitchen: egg box, jug, salt shaker, each made for one thing. | "Every number is just a number; the type does not matter." | `none:` a reference table of types with sizes is clearer than motion. | `P:` the type of `3`, `3.0`, `'3'` and `"3"` · `F:` a price stored as `Int` loses the cents | — |
| `type-inference` | easy | "How does Kotlin know `val n = 5` is an Int?" | A shop assistant who sees you holding a shoe and knows you want the shoe aisle. | "No type written means the variable has no type and can hold anything." | `none:` a single rule; hovering in the editor shows the inferred type. | `P:` the type Kotlin picks for `val x = 10L` · `F:` assigning text to a variable first given a number | — |
| `number-conversions` | medium | "Why must I write `.toLong()` to put an Int into a Long?" | Pouring from a small cup into a big jug is safe, but the café still makes you ask for the bigger cup. | "Kotlin widens small numbers into big ones automatically, like Java." | `sim:` `overflow-cups`: a small cup and a big jug, each with a top mark; pouring the small cup into the jug needs an "order slip" tick-box; filling the small cup to its mark and adding one drop wraps its level to the bottom (overflow wraps, it does not spill), while the jug has far more room; the learner picks the two cup sizes. | `P:` what `Int.MAX_VALUE + 1` prints · `F:` a total of large file sizes overflows an Int · `B:` remove the `.toLong()` | — |

### control-flow · Control Flow

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `if-as-an-expression` | easy | "How do I set a variable to one of two values with a single `if`?" | A vending machine that hands you either tea or coffee from the same slot. | "`if` can only run code; it cannot give back a value." | `none:` a single rule; Foundation's fork sim already covers the flow. | `P:` value of `val label = if (x > 0) "pos" else "neg"` · `F:` an `if` expression missing its `else` | Assumes Foundation: `foundation/programming-thinking/how-a-program-decides` |
| `when-expression` | easy | "Which branch of a `when` runs for a given value?" | A sorting office with labelled chutes: each letter drops into the first chute that matches. | "Every matching branch of `when` runs, like a switch that falls through." | `sim:` `sorting-chutes`: numbered letters slide along a rail past labelled chutes ("1", "2", "3 or 4", "5 to 9") and a fifth chute "7 to 12" that overlaps "5 to 9", and drop into the first chute that fits, ending in an "everything else" bin; the learner drops letters, drags chutes to reorder them, and removes the bin so a letter has nowhere to go. | `P:` which branch runs when two conditions both match · `F:` a range branch placed after a wider one so it never runs | Guard conditions (`if` inside a branch) are stable since Kotlin 2.2.0 (kotlinlang.org "What's new in 2.2", checked 18 Oct 2026); mention only. ⚠️ VERIFY Playground version supports them. |
| `ranges-and-for-loops` | easy | "Does `1 until 5` include 5?" | A tape measure: you say where to start, where to stop, and how big each step is. | "`..` and `until` mean the same thing." | `sim:` `tape-measure-range`: a numbered tape measure with start and stop sliders, a step-size slider and switches for "include the stop mark", "stop before the stop mark" (two spellings) and "count down"; "Place stickers" puts a sticker on each visited mark. | `P:` what `for (i in 10 downTo 0 step 3)` prints · `F:` a loop over list indices that goes one past the end | — |
| `break-and-continue` | easy | "What is the difference between `break` and `continue` inside a loop?" | Reading a stack of letters: `continue` skips one letter, `break` throws the rest away. | "`continue` stops the loop." | `sim:` `letter-stack-gates`: a stack of six numbered letters is read one at a time into a done tray; the learner adds a "junk mail" rule card (letter 3 is put aside, the stack goes on) and a "STOP letter" rule card (letter 5 and the rest are thrown away, then the "make tea" step still runs). | `P:` numbers printed by a loop with both · `F:` `break` used where `continue` was meant, so the search stops early | — |

### functions · Functions

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `declaring-functions` | easy | "What does each part of `fun area(w: Int, h: Int): Int` mean?" | An order form at a print shop: the form's name, the boxes you fill, and what you get back. | "The return type is optional everywhere, so I can skip it on any function." | `none:` syntax rule; the editor is enough. | `P:` what `area(2, 3)` returns · `F:` a function that declares `Int` but forgets `return` | Assumes Foundation: `foundation/programming-thinking/function-inputs-and-outputs` |
| `default-and-named-arguments` | easy | "How can I call a function without passing every input?" | Ordering a coffee: if you say nothing about milk, you get the house default. | "Named arguments change what the function does." | `none:` syntax rule; the editor is enough. | `P:` output of `greet(name = "Ana")` when `greeting` defaults to "Hi" · `F:` two Int arguments passed in the wrong order, fixed by naming them | Compose functions rely on this heavily. |
| `single-expression-functions` | easy | "Is `fun double(x: Int) = x * 2` the same as one with braces and `return`?" | A shop sign that says "Keys cut here" instead of a full menu: same service, shorter notice. | "The `=` form is a variable, not a function." | `none:` syntax rule; side-by-side code is enough. | `P:` what `double(4)` returns · `F:` braces added after `=` so the function returns a lambda by mistake | — |
| `unit-and-nothing` | easy | "What type does a function return when it only prints?" | A postbox: you put a letter in and get nothing back, but the box still "answered". | "A function with no return value returns null." | `none:` a single rule about types; the editor's type hints show it. | `P:` what `println(println("a"))` prints · `F:` assigning a Unit function's result and using it as a String | `Nothing` named once (for `TODO()` and `throw`). |
| `scope-and-shadowing` | medium | "Which `count` does this line read when two variables share the name?" | Two people called Sam: one in your house, one in the street; at home, "Sam" means the one in the house. | "A variable made inside a loop still exists after the loop." | `sim:` `house-and-street-names`: a street with Sam the baker and Joe the postman, and a house holding another Sam and a Mia; a figure called Lee stands in the house or on the street; "Call Sam" and "Call Mia" send a call that looks in the house first and then outwards and lights the person who answers, or shows no answer; the learner can drag the house's Sam out. | `P:` value printed when an inner `val total` hides the outer one · `F(ai):` a tidy function where an inner `val result` shadows the outer one, so the caller always gets 0 · `B:` rename the inner variable | — |

### null-safety · Null Safety

Flags: phase=2 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-null` | easy | "Why does my app crash when a field is empty?" | A lunch box with a label on the lid ("always has a sandwich" or "might be empty"): an empty box holds nothing, which is not the same as a note with no words. | "null is the same as 0 or an empty string." | `sim:` `null-lunch-box` (`docs/05` §10): two labelled boxes and a draggable sandwich; counting slices in the empty "might be empty" box stops the sim. | `P:` what does `println("Middle name: $middleName")` print when it is null (no `?.`, taught later in `safe-call-and-elvis`) · `F(ai):` tidy code with `!!` on user input · `B:` remove the `?` | Requires: `basic-types`. Assumes Foundation: `foundation/programming-thinking/how-a-program-decides`, `foundation/programming-thinking/function-inputs-and-outputs`. Worked example in `docs/04` §10. |
| `nullable-types` | easy | "What does the `?` in `String?` promise?" | A parcel locker marked "may be empty" versus one marked "always has a parcel". | "Adding `?` makes the code safe by itself." | `none:` a single type rule; the compiler errors are the demo. | `P:` which of four lines compiles · `F:` a non-null `String` given a value that may be null | — |
| `safe-call-and-elvis` | medium | "What does `user?.name ?: "Guest"` give when user is null?" | Knocking on a door: if nobody answers, you leave a note instead of breaking in. | "`?.` hides the problem, so the null still crashes later." | `sim:` `knock-and-note`: a visitor on a street with two houses, one with a resident at home and one empty; "Ask for the name's length" with a polite knock (`?.`) brings back the number or "no reply" and the visitor carries on; a tick-box "Leave a note: 0" (`?:`) turns "no reply" into 0; an optional "Walk straight in" tick-box shows the street refusing the visit before the visitor leaves. | `P:` output of a chain `a?.b?.c ?: "none"` · `F(ai):` neat code with `?: return` that silently skips saving · `B:` remove the `?:` | — |
| `smart-casts` | medium | "Why can I call `.length` on a nullable String right after checking it is not null?" | A bouncer who checks your ticket once at the door; inside, nobody asks again. | "After one null check, the value is safe everywhere in the function." | `none:` a compiler rule; the editor highlights smart casts, which is clearer than motion. | `P:` which line compiles after `if (name != null)` · `F(ai):` a confident check on a `var` property that does not smart-cast, "fixed" with `!!` | — |
| `when-to-use-double-bang` | medium | "When is it safe to write `!!`?" | Swearing a parcel is in the locker without looking: fine if you just put it there, a disaster if not. | "`!!` is a quick fix for the red underline." | `none:` a judgment rule; a decision table of real cases is clearer. | `F(ai):` a generated settings reader that uses `!!` on a map lookup · `F(ai):` `!!` on a parsed number replaced with a default · `P:` exception type thrown by `null!!` | — |

### strings · Strings

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `strings-are-sequences` | easy | "What does `"Kotlin"[0]` give back?" | A string of beads, each bead a letter, numbered from 0. | "The first letter is at position 1." | `none:` reuse of Foundation's numbered-box idea in a static diagram is enough. | `P:` what `"hello".length` and `"hello"[4]` print · `F:` reading the last character with index `length` | — |
| `everyday-string-functions` | easy | "Why does `name.trim()` not change `name`?" | A photocopier: you get a tidied copy, the original page stays as it was. | "String functions edit the original text in place." | `none:` a single rule (Strings never change); before/after values in the editor show it. | `P:` value of `s` after `s.uppercase()` is called without assigning · `F:` an email check that forgets to keep the trimmed result · `B:` call `split` on an empty string | — |
| `text-to-numbers` | medium | "What should happen when the user types "abc" where I expect a number?" | A cashier checking a banknote before taking it: a fake one is refused politely, not swallowed. | "`toInt()` returns 0 when the text is not a number." | `none:` a single rule about two functions; a table of inputs and results is clearer. | `P:` result of `"12a".toIntOrNull() ?: -1` · `F:` `toInt()` on user input crashes on "" · `B:` feed it " 42 " with spaces | Links null safety to real input. |

### collections · Collections

Flags: phase=2 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `list-vs-mutable-list` | easy | "Why does a list from `listOf` refuse `add` when one from `mutableListOf` allows it?" | A printed menu versus a whiteboard menu: you can read both, but only write on one. | "`val list` means the list can never change." | `sim:` `menu-board-cover`: a café menu board on a wall hook, one board with only a clear cover (read-only List) and one with an owner's marker (MutableList); "Add a dish" is refused through the cover and works with the marker; hanging the cover over the marker board shows "Owner adds a dish" appearing in the guest's view; an optional "Take a photo" button (`toList`) freezes a copy that stays at the old count. | `P:` which line fails to compile · `F(ai):` a helper that returns `List` and a caller that tries to `add` to it · `B:` change `val` to `var` and see it still cannot `add` | — |
| `index-out-of-bounds` | easy | "How do I ask for an item that may not exist without crashing?" | Asking a hotel for room 6 in a five-room inn: a polite clerk says "no such room" instead of walking you into a wall. | "An index past the end returns null." | `sim:` `inn-room-clerk`: an inn with five numbered doors 0 to 4; the learner types a door number and presses "Walk to the door" (a missing door shakes the wall and stops the guest) or "Ask the clerk" (`getOrNull`: a missing door gives a "no such room" slip and the guest carries on); removing all rooms shows door 0 missing both ways. | `P:` output of `list.getOrNull(9) ?: "none"` · `F(ai):` a tidy "last three items" function that crashes on a list of two · `B:` ask for index `size` | — |
| `sets` | easy | "What happens when I add the same value to a Set twice?" | A guest list at a door: a name already on the list is not written again. | "A Set keeps items in the order I added them, always, like a list with an index." | `none:` a single rule; printing a Set after duplicate adds shows it. | `P:` size of `setOf(1, 2, 2, 3)` · `F:` duplicates counted twice in a list where a Set was meant | — |
| `maps` | easy | "Why does `map["key"]` give a nullable value?" | A coat check: hand in a ticket number, get the coat, or hear "no coat with that ticket". | "Looking up a missing key crashes the program." | `none:` a single lookup rule; a key-value table in the editor is enough. | `P:` output of `prices["tea"] ?: 0` for a missing key · `F(ai):` neat code that calls `getValue` on user input and crashes · `F(ai):` a word counter that overwrites counts instead of adding | — |
| `pairs-and-destructuring` | easy | "What do `a` and `b` hold after `val (a, b) = Pair(1, "x")`?" | Unpacking a lunch box with two compartments into two plates. | "Destructuring copies values by name, not by position." | `none:` syntax rule; the editor is enough. | `P:` output of `for ((k, v) in map)` · `F:` swapped names in a destructuring line print the price as the name | — |
| `arrays-vs-lists` | easy | "Why does some Kotlin code write `IntArray` instead of `List<Int>`?" | An egg box with a fixed number of slots versus a shopping bag that can grow. | "Arrays and Lists print the same, so they are the same." | `none:` a choice rule; a comparison table is clearer. | `P:` what `println(intArrayOf(1, 2))` prints · `F(ai):` comparing two arrays with `==` and always getting false | Arrays appear in DSA code. |

### lambdas · Lambdas

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `functions-as-values` | medium | "What is the type `(Int) -> String` describing?" | A recipe card handed to a friend: the card is not the dish, but they can cook it later. | "Passing a function runs it straight away." | `sim:` a card slides from one box to another without being opened; the learner presses "cook" and only then does the function run. | `P:` how many times "run" prints when a function is stored but not called · `F:` `val f = greet()` where `val f = ::greet` was meant | — |
| `lambda-syntax` | medium | "In `list.forEach { println(it) }`, what is `it` and where are the brackets?" | A sticky note handed over with an order: "do this with each one". | "The last line of a lambda is ignored unless I write `return`." | `none:` syntax reading; colour-coded parts of real lambdas in the editor are clearer. | `P:` value returned by `{ x: Int -> x + 1; x * 2 }(3)` · `F:` a lambda whose result is the wrong last line · `B:` move the trailing lambda inside the brackets | Trailing lambdas are the shape of all Compose code. |
| `higher-order-functions` | medium | "How does `repeatTwice(action)` run code it was handed?" | Hiring a tour guide and handing them your own list of stops. | "Only Kotlin's built-in functions can take a lambda." | `none:` a pattern rule; stepping through calls in the editor is enough. | `P:` output when a function calls its lambda twice · `F:` a function that takes a lambda but never calls it | — |
| `map-filter-and-friends` | medium | "What does `names.filter { it.length > 3 }.map { it.uppercase() }` produce?" | A factory line: one station throws out bad apples, the next paints the good ones. | "`filter` and `map` change the original list." | `sim:` a conveyor belt with snap-on stations (filter, map, sortedBy, take); balls flow through; the learner reorders stations and watches the output change. | `P:` result of a three-step chain · `F:` `map` used where `filter` was meant · `B:` swap the order of `take` and `sortedBy` | Covers `sortedBy`, `groupBy`, `firstOrNull`, `sumOf` by name. |
| `closures` | medium | "Why does the lambda print 3 when the variable was 1 when the lambda was made?" | A window onto a garden, not a photo of it: you always see the garden as it is now. | "A lambda copies the variable's value when it is created." | `sim:` `garden-window-view`: a house with windows A and B onto one garden plant; "Look through window A" shows the plant now; "Gardener swaps the plant" then looking through either window shows the new plant; an optional "Take a photo first" tick-box keeps the old plant in a photo that never updates. | `P:` output of a counter captured by two lambdas · `F:` a list of click lambdas that all report the same final value | — |

### classes-and-objects · Classes and Objects

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-class` | easy | "What is the difference between a class and an object?" | A cookie cutter and the cookies it makes. | "A class is the object itself." | `sim:` a cookie cutter stamps cookies with different toppings; the learner changes the cutter and every new cookie changes shape. | `P:` how many objects exist after three `Dog()` calls · `F:` calling a member on the class name instead of an object | — |
| `constructors` | easy | "Where do `name` and `age` come from in `class Person(val name: String, val age: Int)`?" | Filling in a new-member form before you get your membership card. | "Every property must be set later with a separate line." | `none:` syntax rule; the editor is enough. | `P:` what an `init` block prints when two objects are made · `F:` a constructor parameter without `val` used as a property | — |
| `member-functions` | easy | "How does `account.deposit(50)` know which account to change?" | A bank teller who works only on the passbook you hand over. | "A function inside a class is shared, so it changes every object." | `none:` the idea is ownership, shown with two objects in the editor. | `P:` balances of two accounts after one deposit · `F:` a member function that updates a local variable instead of the property | — |
| `private-and-public` | easy | "Why can't outside code set `balance` directly?" | A cash drawer behind the counter: customers ask the teller, they do not reach in. | "`private` is for hiding code from other programmers for security." | `none:` a visibility rule; compiler errors are the demo. | `P:` which line fails to compile · `F:` a public `balance` set to a negative number from outside | `internal` named; modules come in later branches. |
| `getters-and-setters` | medium | "What runs when I write `item.price = 5` on a property with a custom setter?" | A receptionist who checks every parcel before it goes on the shelf. | "A property is always just a stored value." | `none:` a single rule; stepping through the setter in the editor shows it. | `P:` value read from a property whose getter computes it · `F:` a setter that assigns to itself and loops forever (`field` missing) | — |
| `objects-are-references` | hard | "Why did changing `b` also change `a` after `val b = a`?" | Two house keys to the same flat: a new sofa is seen whoever opens the door. | "`val b = a` makes a copy of the object." | `sim:` variables are tags on strings tied to objects floating in a memory box; assigning ties a second string to the same object; the learner changes the object and both tags show it. | `P:` output after changing a property through a second variable · `F:` a "backup" that is the same object as the original · `B:` make a real copy and compare | Helps the later `ui` and `architecture-data` state leaves. |
| `two-kinds-of-equality` | medium | "When is `a == b` true but `a === b` false?" | Two identical twins (equal) versus one person seen twice (the same). | "`==` checks whether they are the same object." | `sim:` reuse `objects-are-references` sim, with a "same object" light and a "same content" light. | `P:` results of `==` and `===` for two equal-looking Strings and two plain objects · `F:` two plain-class objects never equal with `==` | — |
| `read-only-is-not-immutable` | medium | "How can a `List` I hold change when nobody touched my variable?" | A shop window you cannot open, but the staff can rearrange it from behind. | "A read-only `List` can never change." | `sim:` reuse `objects-are-references` sim, with a read-only tag and a mutable tag tied to the same list; changing it through the mutable tag shows through the read-only one. | `P:` size of a read-only view after the owner adds an item · `F:` a class that exposes its `MutableList` as `List` and leaks changes · `B:` cast the `List` back to `MutableList` | — |
| `model-a-small-program` | medium | "How do I decide which classes, properties and functions a small problem needs?" | Planning a kitchen before building it: what tools exist, who uses them, what each one does. | "Good design means as many classes as possible." | `none:` a design skill; nouns-and-verbs notes and a class sketch are the material. | `P:` which class should own a "total price" function · `F:` a design where one class does everything · `B:` add a new feature and see which class must change | Synthesis leaf; fruit (end of the phase-2 segment). |

### interfaces-and-inheritance · Interfaces and Inheritance

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-an-interface` | medium | "What does `class Car : Drivable` promise to the rest of the code?" | A wall socket: any plug of the right shape works, whoever made the device. | "An interface is a class you can make objects from." | `none:` a contract idea; a diagram of classes snapping into one socket shape is enough. | `P:` which `describe()` runs for each object in a list of `Drivable` · `F:` a class that claims an interface but misses one function | — |
| `open-and-override` | medium | "Why must a Kotlin class be marked `open` before another class can extend it?" | A family recipe that is locked unless the cook writes "you may change this". | "Every class can be extended by default, like in Java." | `none:` a language rule; compiler errors are the demo. | `P:` which `sound()` prints for a `Dog` held as an `Animal` · `F:` an override that forgets `super` and loses the parent's work | — |
| `abstract-classes` | medium | "When do I pick an abstract class over an interface?" | A half-built house kit with the walls done and the rooms left for you, versus a building code that only lists rules. | "Abstract classes and interfaces are the same thing with different names." | `none:` a choice rule; a comparison table with one example each is clearer. | `P:` which line fails when creating an abstract class directly · `F:` shared state put in an interface that cannot hold it | — |
| `polymorphism-and-type-checks` | hard | "Why is `as` dangerous where `is` is safe?" | A box of mixed tools: you can check a tool is a hammer before swinging it, or just swing and hope. | "If it compiles, the cast must be safe." | `sim:` mixed shapes slide down one chute; an `is` sorter routes each shape safely; an `as` gate jams and stops the line when the wrong shape arrives. | `P:` output of a `when (shape) { is Circle -> … }` over a mixed list · `F:` `as Circle` on a list that contains a Square · `B:` replace `as` with `as?` | — |

### special-classes · Special Classes

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `data-classes` | medium | "Why are two `data class` objects with the same values equal when two plain ones are not?" | Two receipts with the same items and total: you treat them as the same receipt. | "`data` only makes `toString` prettier." | `none:` a generated-code rule; printing and comparing in the editor shows it. | `P:` result of `==` for two equal data objects and two equal plain objects · `F(ai):` a generated model with a `var` field used as a map key, then changed so the lookup fails | — |
| `data-class-copy` | medium | "How do I get a new object with one field changed and the old one untouched?" | Photocopying a form and changing one box on the copy. | "`copy` makes deep copies of everything inside." | `sim:` reuse `kotlin/classes-and-objects/objects-are-references` sim; `copy` makes a new object whose list property still points to the same inner list. | `P:` output after changing a list inside a copied object · `F(ai):` neat state-update code that mutates the old object instead of using `copy` · `B:` copy, then add to the shared inner list | Prepares UI state updates in later branches. |
| `enum-classes` | easy | "How do I loop over every value of an enum?" | A drink machine with exactly five buttons: no sixth drink exists. | "Enum values are just Strings with a fancy name." | `none:` a single rule; the editor is enough. | `P:` what `Day.entries.size` prints · `F:` a `when` over an enum with an `else` that hides a new value | ⚠️ VERIFY: `entries` is preferred over `values()` (kotlinlang.org "Enum classes") on the publish date. |
| `sealed-classes` | hard | "Why does a `when` over a sealed type warn me when I forget a case?" | A traffic light with exactly three lamps: the driver must have a plan for each. | "A sealed class is the same as an enum." | `sim:` a ball can roll into one of three labelled trays (Loading, Success, Error); remove a tray and the build light turns red before anything runs. | `P:` which branch runs for `Result.Error("x")` · `F(ai):` a generated screen-state `when` with `else -> {}` that swallows a new case · `B:` add a fourth subclass and remove the `else` | ⚠️ VERIFY: wording of exhaustiveness rules for `when` statements versus expressions on kotlinlang.org "Sealed classes". |
| `object-declarations` | medium | "Why is there only ever one `Settings` when it is declared with `object`?" | The single town clock on the square: everybody reads the same one. | "An `object` is created fresh every time I use its name." | `none:` a single rule; printing identity in the editor shows it. | `P:` what a counter in an `object` prints after two different callers increment it · `F(ai):` a generated helper `object` holding per-user state that leaks between users · `P:` what a `companion object` function call looks like from outside | Names `companion object` and `data object`. ⚠️ VERIFY: `data object` stable status on kotlinlang.org on the publish date. |

### generics · Generics

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-are-generics` | medium | "What does the `<String>` in `List<String>` tell the compiler?" | A labelled storage tub: the label says "socks only", so you never pull out a fork. | "`List<Any>` is the same as a generic list." | `none:` a type rule; compiler errors on the wrong item type are the demo. | `P:` type of `listOf(1, "a")` · `F:` a `List<Any>` that forces casts everywhere | — |
| `writing-generic-code` | medium | "How does `fun <T> firstOr(list: List<T>, default: T): T` work for any type?" | A labelling machine that prints whatever word you feed it on any jar. | "`T` is a real type called T." | `none:` a type rule; the editor's inferred types show it. | `P:` the type inferred for `firstOr(listOf(1), 0)` · `F:` a function written twice for Int and String that one generic version replaces | Variance (`in`/`out`) stays in `## Later`. |

### extensions-scope-and-delegates · Extensions, Scope Functions and Delegates

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `extension-functions` | medium | "Where does `"hi".shout()` come from when String has no `shout` function?" | A phone case with a stand: the phone is unchanged, but you can now prop it up. | "An extension function changes the original class." | `none:` a call-resolution rule; jumping to the definition in the editor shows it. | `P:` which function wins when a member and an extension have the same name · `F:` an extension that tries to read a private property | — |
| `scope-functions` | hard | "What value does `user?.let { save(it) }` hand back?" | Five kitchen tools that look alike (spoon, ladle, scoop): each serves a different job. | "`let`, `apply`, `also`, `run` and `with` are interchangeable." | `none:` a reading rule; a 5-row table (receiver name, return value, typical use) beats motion. | `P:` value of `val x = StringBuilder().apply { append("a") }` · `F(ai):` nested `let` chains where `it` refers to the wrong object · `B:` swap `apply` for `also` | Reading AI code: overused nesting is the defect. Non-local `break`/`continue` inside these lambdas is stable since Kotlin 2.2.0 (kotlinlang.org, checked 18 Oct 2026); mention only. |
| `lateinit` | medium | "What happens if I read a `lateinit` property before it is set?" | A reserved seat with a "taken" sign: fine until someone tries to sit on the empty chair. | "`lateinit` is a safe way to avoid nulls." | `sim:` a seat with a reserved sign; a timeline slider moves "set" and "read" events; reading before setting tips the chair over. | `P:` exception thrown when reading too early · `F:` a `lateinit` read in `init` before it is assigned · `B:` move the assignment after the read | Common in Android test and older UI code. |
| `property-delegates` | medium | "When does the block inside `by lazy { }` run?" | A coffee machine that brews only when the first person presses the button, then keeps the pot warm. | "The `lazy` block runs when the object is created." | `sim:` a timeline with a dormant machine; the first read starts it (one brew), later reads take from the pot; the learner moves the first read along the timeline. | `P:` order of prints with a `lazy` property read twice · `F:` heavy work in a normal property that `by lazy` fixes · `B:` read the property zero times | Names `by` as delegation; Android's `by viewModels()` and Compose's `by remember` are recapped only. |

### errors-and-results · Errors and Results

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `exceptions` | medium | "Which lines are skipped when an exception is thrown?" | A fire alarm: everyone leaves the room at once and the meeting never finishes. | "An exception only stops the one line that failed." | `sim:` a ball travelling down nested function boxes; a throw makes it shoot back up through every box until someone catches it or it falls out of the program. | `P:` output of a function that throws halfway · `F:` `require(age >= 0)` missing so bad input travels on · `B:` throw from the innermost function | Names `require` and `check`. |
| `try-catch-finally` | medium | "Which blocks run when an exception is thrown inside `try`?" | A safety net under a trapeze with a cleaner who sweeps up after every show, fall or not. | "Catching `Exception` everywhere makes code safe." | `sim:` reuse `exceptions` sim, with a net (catch) placed on one box and a sweeper (finally) that always runs. | `P:` print order with `try`, `catch` and `finally` · `F:` an empty `catch` that hides a real failure · `B:` move the net to an outer box | — |
| `result-and-runcatching` | hard | "How does a caller tell success from failure when a function returns `Result<User>`?" | A parcel that arrives with either the item or a note saying why it could not be sent. | "`runCatching` is always the safe replacement for try/catch." | `none:` a type-design idea; a side-by-side of throwing and `Result` code is clearer. | `P:` output of `runCatching { "x".toInt() }.getOrElse { -1 }` · `F:` a `Result` whose failure is never checked · `B:` throw inside `map` on a `Result` | Helps to know Concurrency: `concurrency/cancellation-and-errors/never-swallow-cancellation` — `runCatching` also catches cancellation; recap in one line. ⚠️ VERIFY wording against kotlinlang.org `Result` API docs. |

### reading-code · Reading Code

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `reading-order-for-code` | medium | "What four questions do I ask, in order, when I open a file I did not write?" (types, what runs when, what can be null, what can run twice) | Inspecting a used car: papers first, then the engine, then the brakes, then a test drive. | "You read code from the top line to the bottom line like a story." | `none:` a reading method; an annotated 60-line file is the material. | `F(ai):` a 60-line generated class with a hidden `!!` found by the "what can be null" pass · `P:` which function runs first when the class is created | Reading order from `docs/01` §3.5 and `docs/08` §5 rule 5. |
| `spotting-risky-patterns` | hard | "Which patterns in clean-looking Kotlin are most likely to hide a bug?" | A home inspector who knows the five places damp always hides. | "Tidy formatting and good names mean the code is correct." | `none:` a pattern catalogue; short snippets side by side are clearer. | `F(ai):` shadowed variable · `F(ai):` `==` versus `===` on objects · `F(ai):` `runCatching` that swallows every failure · `P:` which of four snippets crashes on empty input | — |
| `review-a-generated-file` | hard | "Which three risks in this generated file would stop me approving it?" | A final house inspection before you sign: walk every room, write down the three worst problems. | "If it runs once on my input, it is ready." | `none:` a review exercise on a real file; the editor and a checklist are the material. | `F(ai):` review a 150-line generated file, list its 3 biggest risks with line numbers, fix one · `P:` predict its output on an empty list | Branch synthesis; fruit (last leaf, `docs/08` §12 F2). |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `kotlin/classes-and-objects/model-a-small-program` | Console library or to-do manager | Runs in the Kotlin Playground, no Android APIs · at least 3 classes, each with one clear job · uses a `List` or `Map` and at least one `filter`/`map` chain · no `!!` anywhere · every input that can be missing is handled with `?.` or `?:` · a short note lists which class owns which data and why | Writes a small, correct Kotlin program from a plain-English problem. ⚠️ VERIFY: map to the exact `docs/01` §6.2 ability wording. |
| `kotlin/reading-code/review-a-generated-file` | Review a 150-line generated file | Pick or generate a 120–200 line Kotlin file you did not write · list its 3 biggest risks, each pointing to a line · use the four-pass reading order · fix one risk and show a run that proves it · say whether you would accept the file and why | The "read a 200-line AI-written file and list its three biggest risks" check (`docs/01` §6.2, L24). Extra fruit F3: 21 leaves after the first fruit. |

## Later

- Inline and reified functions: library-author tools; readers only need to recognise the keyword.
- Building DSLs with lambdas with receivers: Compose and Gradle are read, not built, at this stage.
- Operator overloading: rare in app code.
- Value classes: niche optimisation.
- Contracts: library-author feature.
- Generic variance in depth (in and out, star projections): the two generics leaves cover reading them.
- Kotlin reflection: rarely needed in app code.
- Raw and multi-dollar strings in depth: one mention in the strings stem is enough.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 15 stems, 65 leaves, all `shadow`. | +65 | Inside the 48–72 band (estimate 60, +8 %). Over the sketch because: `smart-casts` added to null-safety (one separate misconception); `read-only-is-not-immutable` and `two-kinds-of-equality` sit in classes-and-objects because they need references first; a synthesis leaf `model-a-small-program` carries the phase-2 fruit; `getters-and-setters` kept because generated code uses custom getters everywhere. Integer division moved to Foundation (`expressions`). |
