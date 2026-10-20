---
id: kotlin/null-safety/smart-casts
title: How does Kotlin know a value is not null after I check it?
status: shadow
difficulty: medium
order: 20
requires: [kotlin/null-safety/nullable-types, kotlin/control-flow/if-as-an-expression]
summary: After you check that a value is not null, Kotlin lets you use it as a plain value, but only when nothing can change it.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/typecasts.html ("The compiler tracks the type checks and explicit casts for immutable values and inserts implicit (safe) casts automatically"; a negative check that leads to a return; smart cast prerequisites: val local variables always, val properties if private or internal or checked in the same module and not open and no custom getter, var local variables if not modified between the check and its usage and not captured in a lambda that modifies it, var properties never "because the variable can be modified at any time by other code"; right-hand side of && and ||); kotlinlang.org/docs/null-safety.html ("the example above only works when the compiler can guarantee that b doesn't change between the check and its usage, same as the smart cast prerequisites"); developer.android.com/guide/fragments/create (if (savedInstanceState == null) adds the fragment only once; "when the activity is recreated, savedInstanceState is no longer null"). That a function's inputs are read-only, the copy-into-a-local-val fix and the Android field picture are general knowledge or derived and are labelled in the body. Snippets are hand-traced, not run. -->
## The question

You check that a name is not null, and on the next line Kotlin lets you use it as if it had no question mark. Change the name into something that can change later, and the very same line is refused. Why does Kotlin trust one check and not the other?

## Mental model

Think of a bouncer at a club door.

- The club is the part of the code after the check.
- The bouncer is Kotlin.
- The ticket check at the door is the null check.
- A guest is a value.
- A private booth is a place only this room can reach, so nobody else can swap the guest.
- An open table is a place anyone can reach, so a waiter can swap the guest at any time.

The bouncer checks each guest once, at the door. Inside, nobody asks for the ticket again, as long as nobody could have swapped the guest.

**Where the analogy stops:** A real bouncer watches what happens inside. Kotlin does not watch the program run. It reads the code before the program starts and decides whether a swap could happen. Also, the bouncer trusts a person, while Kotlin changes how it sees the value: after the check, it treats the value as having no question mark.

## Build the logic

### Notice

The door has a bouncer who checks tickets. Ada shows a ticket and sits in a private booth. Bob shows a ticket and sits at the open table, where waiters swap guests whenever they like. Inside the club a worker calls out to Ada. The worker does not need to look at her ticket again. When the worker calls to the open table, the guest may have been swapped for someone with no ticket.

### Guess

After the one check at the door, which guests may the bouncer trust inside? Here are three ideas.

- A. All of them, because they all showed a ticket.
- B. Only the guests nobody could have swapped since the check.
- C. None of them. The bouncer must check again every time.

Idea A feels fair, because the ticket was real. But the guest at the open table may no longer be the one who showed it. Idea C is safe, but it makes the bouncer slow and the rule pointless. Idea B is the answer.

### Build

1. Check at the door: does this guest have a ticket?
2. If yes, inside the club the guest counts as ticketed.
3. If no, send the guest away. Then every line after the door only meets guests with tickets.
4. Trust holds only if nobody could swap the guest between the check and the use.
5. A guest in a private booth, which nobody can change, is always trusted.
6. A guest you swap yourself is trusted up to the swap and not after it.
7. A guest at the open table, which other code could change at any time, is never trusted.

### Name it

A **smart cast** is when Kotlin treats a value as a more exact type after a check. The Kotlin documentation says the compiler tracks the type checks for immutable values, which are values that cannot change, and inserts the cast for you. For a missing value, the check is `!= null`, and the more exact type is the same type without the question mark. Inside the `if`, a `String?` is treated as a `String`.

The documentation says smart casts work only when the compiler can guarantee that the variable will not change between the check and its use. A name written straight in the file, outside any function, is a **property**. Here is the documentation's list, in this lesson's words.

| The name | Does the smart cast work? |
|---|---|
| A `val` made inside a function | Always |
| A `var` made inside a function | If it is not changed between the check and the use, and no small piece of code in braces that runs later changes it |
| A `val` property | If it is in your own module, which is the part of the project it was declared in. It does not work on a property marked `open` or on one with a custom getter. Both belong to classes |
| A `var` property | Never, because other code can change it at any time |

```kotlin
val nickname: String? = "Ada"
val other: String? = null

fun main() {
    if (nickname != null) {
        println(nickname.length)
    }
    if (other == null) {
        println("No other")
        return
    }
    println(other.length)
}
```

This prints `3` and then `No other`. The first check lets in `nickname`, which holds `Ada`, so the guest counts as a plain `String` inside the `if`. The second check sends `other` away with `return`, so the line `other.length` is never reached. The documentation says the compiler is smart enough to know a cast is safe if a negative check leads to a return.

Kotlin refuses to run this next program. The name `current` is a `var` written straight in the file, so another part of the code could change it at any time.

```kotlin
// does not compile
var current: String? = "Ada"

fun main() {
    if (current != null) {
        println(current.length)
    }
}
```

The fix is to take a copy into a `val` made inside the function, and check the copy. Nobody else can reach the copy.

```kotlin
var current: String? = "Ada"

fun main() {
    val copy = current
    if (copy != null) {
        println(copy.length)
    }
}
```

This prints `3`. This copy-into-a-local-`val` fix is our own reasoning from the list above.

## Predict the output

```challenge
type: predict
run: false
id: check-then-use
verified: not-run
code: |
  val nickname: String? = "Ada"
  val other: String? = null

  fun main() {
      if (nickname != null) {
          println(nickname.length)
      }
      if (other != null) {
          println(other.length)
      }
      println("Done")
  }
options:
  - id: a
    text: "3, then 0, then Done"
    why: "You may have expected the second check to let a guest in. The other box is empty, so the guest has no ticket and the line behind the door never runs."
  - id: b
    text: "3, then Done"
    correct: true
    why: "Ada has a ticket, so her line runs and counts 3 letters. The other box has no ticket, so the line behind that door is skipped. Done is outside both doors."
  - id: c
    text: "3, then the program stops with a NullPointerException"
    why: "You may have expected the second line to crash. It sits behind the door and runs only for a guest with a ticket."
output: |
  3
  Done
```

```challenge
type: predict
run: false
id: send-away-then-use
verified: not-run
code: |
  val note: String? = null

  fun main() {
      if (note == null) {
          println("No note")
          return
      }
      println(note.length)
  }
options:
  - id: a
    text: "No note, then 0"
    why: "You may have thought the program goes on after the return. A return leaves the function, so the last line never runs."
  - id: b
    text: "Kotlin refuses the last line, because note might be null"
    why: "You may have thought a check only protects the lines inside its braces. A send-away that leaves the function also protects every line after the door."
  - id: c
    text: "No note"
    correct: true
    why: "The note has no ticket, so the guest is sent away and the function ends. The last line is only reached by guests with tickets, which is why Kotlin allows it."
output: |
  No note
```

```challenge
type: predict
run: false
id: swap-after-use
verified: not-run
code: |
  val first: String? = "Ada"

  fun main() {
      var name: String? = first
      if (name != null) {
          println(name.length)
          name = null
          println(name)
      }
  }
options:
  - id: a
    text: "3, then null"
    correct: true
    why: "The guest is trusted up to the swap, so counting 3 letters is allowed. Then you swap the guest for nothing yourself, and printing the empty seat shows null."
  - id: b
    text: "Kotlin refuses the first println, because name is a var"
    why: "You may have thought any guest that can be swapped is never trusted. A var made inside a function is trusted until the swap, and the swap comes after the count."
  - id: c
    text: "3, then 0"
    why: "You may have expected an empty seat to count as zero. After the swap the seat holds nothing, and printing it shows null."
output: |
  3
  null
```

## Tricks to remember

- Check once at the door, then use inside.
- Trust lasts until a swap. A `val` cannot be swapped.
- A `var` property can be swapped by anyone, so it is never trusted. Copy it into a `val`.
- A send-away with `return` protects every line after it.

## Common mistakes

- **Expecting one null check to cover the whole function.** The check feels like it makes the name safe for good. It protects the lines behind the door, and only while nobody can swap the guest. Use the value close to the check.
- **Adding `!!` when Kotlin refuses a checked `var` property.** The check feels like proof, so the refusal feels unfair. The refusal means other code could have changed the value after your check, and `!!` only turns that risk into a crash. Copy the value into a local `val`, check the copy and use it.
- **Changing the name between the check and the use.** The check is still on the page, so it feels as if it still counts. A swap in between cancels the trust. Do the use before you change the name.

## Where this shows up in Android

The Android page on creating fragments, which are reusable parts of a screen, shows `override fun onCreate(savedInstanceState: Bundle?)` followed by `if (savedInstanceState == null)`. An activity is one screen of the app. The page says the part is added only when the activity is first created. When the activity is recreated, `savedInstanceState` is no longer `null`.

That `Bundle?` is a name that may be empty. This next part is our own reasoning: in the other branch, after a check for `!= null`, the code can use the `Bundle` straight away, because function inputs are read-only (general knowledge) and the smart cast works on them. A screen field that other code can set later is different. Android Studio would show the refusal from this lesson there, and the fix is the local copy.

## Say it in an interview

**What is a smart cast?**
It is when the compiler treats a value as a more exact type because of a check. The Kotlin documentation says the compiler tracks the type checks for values that cannot change and inserts the cast for you. After `if (x != null)`, a `String?` is used as a `String`. The trade-off: you save a cast or a `!!`, but you depend on the compiler being able to prove nothing changes the value.

**Why does a smart cast not work on a `var` property?**
The documentation says a `var` property never works, because other code can change it at any time. So the check proves nothing about the next line. My own reasoning: "at any time" includes code that runs at the same time as yours, which can change the value between two lines.

**You see `if (x != null) x!!.length` on a `var` property. What would you say in review?**
The `!!` silences the error but keeps the risk, because the value can still become `null` between the check and the use. A safer way is to copy the property into a local `val`, then check and use the copy. Or use a safe call with a fallback. My own reasoning: the local copy costs one line and removes the reason for the crash.

**Does a smart cast work after an early return, or inside `&&`?**
Yes to both. The documentation says the compiler knows a cast is safe if a negative check leads to a return. It also says the compiler can smart cast on the right-hand side of `&&` and `||` if there is a check on the left. And a `var` made inside a function loses the cast if it is changed between check and use, or captured by a lambda, which is a small piece of code in braces that can run later, and that changes it.

## Recap

1. After a null check, Kotlin treats the value as having no question mark.
2. The cast works only when nothing can change the value between the check and the use.
3. A `var` property is never trusted, so copy it into a local `val` first.

## You can now

- Predict which lines run after a null check on a name that holds `null` and on one that does not.
- Predict what the lines after a check with an early `return` do.
- Predict whether a changeable local name is still trusted before and after it is changed.
