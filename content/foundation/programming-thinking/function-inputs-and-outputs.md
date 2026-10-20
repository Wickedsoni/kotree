---
id: foundation/programming-thinking/function-inputs-and-outputs
title: How does a function take something in and hand something back?
status: shadow
difficulty: medium
order: 20
requires: [foundation/programming-thinking/what-is-a-function]
summary: A function can take values in and hand a value back. Showing a value on screen is not the same as handing it back.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live): kotlinlang.org/docs/kotlin-tour-functions.html ("Each parameter must have a type, and multiple parameters must be separated by commas"; "The return type is written after the function's parentheses (), separated by a colon :"; "The return keyword is used to exit or return something from a function"; sum(x: Int, y: Int): Int sample prints 3; Unit: "If your function doesn't return a useful value, then its return type is Unit. Unit is a type with only one value"; named arguments: "If you do include parameter names, then you can write the parameters in any order"); kotlinlang.org/docs/functions.html (double(x: Int): Int sample, double(5) is 10; "Declare function parameters using Pascal notation: name: Type"); String + String joining is taught in what-is-a-type; developer.android.com/develop/ui/compose/mental-model ("composable functions that take in data and emit UI elements"; "Composable functions can accept parameters, which let the app logic describe the UI"). Derived, not run: val kept: Int = showDouble(4) is refused because the function hands back Unit; without parameter names the order of arguments decides. Roadmap says none for a sim: the idea is a contract, so a before/after table is used. Outputs hand-traced, not run (no kotlinc). -->
## The question

A program asks a helper step for double 4. The helper shows 8 on the screen. The program now wants to add 1 to that 8, and it cannot.

Why not? What is the difference between showing a value and handing it back?

## Mental model

Think of a vending machine that swaps your coins for twice as many coins.

- The machine is a named set of steps. Each time you use it, it follows the same steps.
- The slot is where something goes in. A label on the slot says what kind of thing it accepts, such as "whole coins".
- The coins you push in this time are the actual things going through the slot.
- Inside, the machine works with whatever you pushed in.
- The tray is where the result drops. You take it and use it as you like. You can keep it in your pocket or push it into another machine.
- The light on the front flashes a message for people who are watching. Nothing drops into the tray when it flashes. Nobody can pick up a light.

**Where the analogy stops:** A real machine may take many coins at once, give change, or refuse a coin. A program is stricter. Each slot takes one value of the kind its label says, and the tray holds one value for each use. Also, a real light is read by people. In a program, the light is the screen. No other part of the program can read it.

## Build the logic

### Notice

Two machines stand side by side. Both say "doubler" on the front. You push 3 coins into the first. The light flashes 6, and the tray stays empty. You push 3 coins into the second. Six coins drop into the tray, and the front stays dark.

Now you want to double the answer again. With the second machine, you pick up the 6 coins and push them into the slot. With the first, the 6 was only a light. You have nothing to push in.

### Guess

You want to double 3, and then double the answer. The first machine only flashes 6. Can the program feed that answer into a machine again?

- A. Yes, the machine remembers the 6 and passes it on.
- B. Yes, the program reads the 6 from the light.
- C. No, nothing dropped into the tray, so there is nothing to pass on.

Idea A gives the machine a memory it was never built with. Idea B treats the light like the tray, but the light is for people. Idea C is the answer.

### Build

1. Decide what goes in. Give each slot a name and a label for the kind of thing it takes.
2. Write the steps inside the machine using the names of the slots.
3. Decide what drops into the tray, and what kind of thing it is.
4. Drop it with a "drop in the tray" step. The machine stops there.
5. To use the machine, push something into each slot. Take what drops into the tray and use it, or keep it, or push it into another machine.
6. A flashing light is a separate step. It shows something, but it drops nothing.

### Name it

The machine is a **function**, a named set of steps. A slot is a **parameter**: a name and a type inside the parentheses, such as `x: Int`. A **type** says what kind of value goes in, and `Int` means a whole number. The Kotlin documentation says each parameter must have a type, and several parameters are separated by commas.

The coins you push in this time are the **argument**. In `double(4)`, the argument is 4.

What drops into the tray is the **return value**. You drop it with the keyword `return`. The Kotlin tour says `return` is used to exit or return something from a function. The kind of value in the tray is the **return type**. It is written after the parentheses, after a colon, like `: Int`.

```kotlin
fun double(x: Int): Int {
    return x + x
}

fun showDouble(x: Int) {
    println(x + x)
}

fun main() {
    val kept = double(4)
    println(kept + 1)
    showDouble(4)
}
```

The call `double(4)` hands back 8, and `kept` holds it. The first `println` prints `9`, which is 8 plus 1. The call `showDouble(4)` prints `8` and hands nothing useful back. A table makes the difference clear.

| Call | What the light shows | What drops into the tray |
|---|---|---|
| `double(4)` | nothing | 8 |
| `showDouble(4)` | 8 | nothing useful |

The Kotlin tour says a function with no useful value to hand back has the return type `Unit`, a type with only one value. That is why `showDouble` has no `: Int` after its parentheses. Kotlin refuses to run this program, because `showDouble` hands back `Unit` and the line asks for a whole number.

```kotlin
// does not compile
fun showDouble(x: Int) {
    println(x + x)
}

fun main() {
    val kept: Int = showDouble(4)
    println(kept + 1)
}
```

One more rule. The documentation says that if you write the parameter names in a call, you can give the arguments in any order. Without names, the order decides. The first argument goes into the first slot, and the second into the second.

## Predict the output

```challenge
type: predict
run: false
id: double-of-double
verified: not-run
code: |
  fun double(x: Int): Int {
      return x + x
  }

  fun main() {
      println(double(double(3)))
  }
options:
  - id: a
    text: "12"
    correct: true
    why: "The inner call drops 6 into the tray. That 6 goes into the slot of the outer call, which drops 12."
  - id: b
    text: "6"
    why: "You may have stopped after the inner call. The outer call takes that 6 and doubles it again."
  - id: c
    text: "9"
    why: "You may have added the original 3 to the 6. The outer machine only sees what the inner machine dropped in the tray."
output: "12"
```

```challenge
type: predict
run: false
id: answer-thrown-away
verified: not-run
code: |
  fun double(x: Int): Int {
      return x + x
  }

  fun main() {
      double(5)
      println("Done")
  }
options:
  - id: a
    text: "10, then Done"
    why: "You may have thought double shows its answer. It only drops the 10 into the tray, and nobody picked it up."
  - id: b
    text: "Nothing prints at all"
    why: "The line that prints Done still runs. Ignoring the tray does not stop the program."
  - id: c
    text: "Done"
    correct: true
    why: "The call drops 10 into the tray, and the program does nothing with it. The only line that shows anything is the one that prints Done."
output: "Done"
```

```challenge
type: predict
run: false
id: swapped-arguments
verified: not-run
code: |
  fun join(first: String, second: String): String {
      return first + second
  }

  fun main() {
      println(join("sun", "day"))
      println(join("day", "sun"))
  }
options:
  - id: a
    text: "sunday, then sunday"
    why: "You may have thought the machine sorts its inputs. The first argument goes into the first slot, so the order you give is the order you get."
  - id: b
    text: "sunday, then daysun"
    correct: true
    why: "The first call puts sun in the first slot and day in the second, so it hands back sunday. The second call swaps them and hands back daysun."
  - id: c
    text: "daysun, then sunday"
    why: "You may have read the lines in reverse. The first line runs first, and it puts sun first."
output: |
  sunday
  daysun
```

## Tricks to remember

- Slots are for going in. The tray is for coming out.
- The light is for people. The tray is for the program.
- Without names, the first argument fills the first slot.
- Call inside call: the inner machine finishes first, and its tray feeds the outer slot.

## Common mistakes

- **Treating printing and returning as the same thing.** When you run the program, both show a number on the screen. Only the tray can be used by later code. If a later line needs the number, make the function return it.
- **Calling a function that returns a value and ignoring the tray.** The call `double(5)` on its own line feels like it should show 10. It drops the 10 into the tray and nothing picks it up. Print it or keep it in a variable.
- **Mixing up the order of the arguments.** The names of the slots feel like they sort things out. Without names in the call, the order decides. Check which slot comes first.

## Where this shows up in Android

A profile card shows the title where the subtitle should be, and the subtitle where the title should be. Nothing crashes. This part is my own reasoning, not the documentation's. If the code gives the two arguments in the wrong order, each one goes into the wrong slot.

The Android documentation says that in Jetpack Compose you define composable functions that take in data and emit UI elements. It adds that composable functions can accept parameters, which let the app logic describe the UI. Jetpack Compose is the modern toolkit for building Android UI.

## Recap

1. A parameter is a slot, and the argument is the value you put in it.
2. The return keyword drops a value into the tray, where later code can use it.
3. Printing shows a value to people, but only a returned value can be used by later code.

## You can now

- Predict the result of one function call placed inside another.
- Spot a function result that is thrown away because nobody prints or keeps it.
- Predict the output when the arguments of a call are in a different order.
