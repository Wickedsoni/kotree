---
id: kotlin/null-safety/when-to-use-double-bang
title: When is !! ever the right choice?
status: shadow
difficulty: medium
order: 21
requires: [kotlin/null-safety/safe-call-and-elvis]
summary: The !! operator turns a missing value into a crash. It is right only when you can point to why the value is there.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/null-safety.html ("The not-null assertion operator !! converts any value to a non-nullable type"; "if the value is null, the !! operator forces it to be treated as non-nullable, which results in an NPE"; "The only possible causes of an NPE in Kotlin are": explicit throw NullPointerException, the !! operator, data inconsistency during initialization, Java interoperation; return and throw on the right of Elvis; the IllegalArgumentException("name expected") example); kotlinlang.org/api/core/kotlin-stdlib/kotlin/require-not-null.html (requireNotNull throws IllegalArgumentException if the value is null, otherwise returns it, with an optional lazyMessage; the sample says the value is smart cast afterwards); kotlinlang.org/api/core/kotlin-stdlib/kotlin/check-not-null.html (checkNotNull throws IllegalStateException if the value is null); kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-int-or-null.html (toIntOrNull returns null if the string is not a valid Int); kotlinlang.org/docs/java-interop.html (platform types, assignment of a null platform value to a non-null type throws a NullPointerException, assertions are emitted when passing platform values to Kotlin functions expecting non-null values); developer.android.com/kotlin/interop (every non-primitive parameter, return and field type in a public API should have a nullability annotation); developer.android.com/guide/components/activities/activity-lifecycle (savedInstanceState is null if the activity has never existed before); developer.android.com/codelabs/basic-android-kotlin-compose-calculate-tip ("This value could be null if the text field is empty"). The decision table, when-it-is-acceptable rules and the trade-offs marked "my own reasoning" are derived. Snippets are hand-traced, not run. -->
## The question

A tip app turns the typed text into a number with a line that ends in the `!!` operator. It passes every test. Then a user types the letter O instead of the number zero, and the app closes. Is `!!` ever the right choice, and how would you tell?

## Mental model

Think of swearing an oath that a parcel is in a locker, without looking inside.

- The locker is a name whose sticker says "may be empty".
- The parcel is the value. An empty locker holds nothing.
- The oath is the promise "I swear a parcel is in locker 7."
- The judge is the running program. The judge believes every oath and acts on it at once.
- Looking inside first is the careful way: knock, leave a note, or go home.
- A false oath is a crash. The app stops on that line.

If you put the parcel there a minute ago and nobody else has the key, the oath is fine. If you swear because the sticker annoyed you, you are gambling.

**Where the analogy stops:** A real oath-taker alone pays for a false oath. In an app, a false oath stops the whole app on that line, and the user feels it. Also, the compiler can never check your oath. It believes you.

## Build the logic

### Notice

Three people swear an oath about three lockers.

- Ana put a parcel in locker 4 a minute ago. Nobody else has the key. She swears it is there.
- Ben swears locker 9 has a parcel, because "it always did when I tested". A stranger courier fills that locker.
- Cy swears locker 7 has a parcel, because a red line on his plan said he must.

### Guess

Which oaths are safe? Here are three ideas.

- A. All three. An oath is a formality, because the judge believes it.
- B. Only the oath where the swearer can point to a reason the parcel is there.
- C. None. Nobody should ever swear without looking.

Idea A is how a hurried person thinks, and the judge does believe every oath. But belief does not put a parcel in the locker. Idea C is safe, but Ana really does know. Idea B is the answer.

### Build

1. Before you swear, ask: how do I know the parcel is there? Say the reason aloud.
2. Reasons that count: you put it there and nothing could have taken it out; you already looked inside; the surrounding code guarantees it and you can say how.
3. Reasons that do not count: "it was full in my test", "the red line went away", "users always fill it in".
4. With no good reason, look first. Knock and ask, leave a note, or go home.
5. If you do swear, swear in words. Say what is missing, so that a false oath stops the program with a message that explains itself.
6. A false oath stops the program on that line. Nothing after it runs.
7. Compare the costs. A false oath is loud and early. A note with the wrong default is quiet and may leave a wrong answer on screen.

### Name it

The oath is the **not-null assertion**, written `!!`. The Kotlin documentation says it converts any value to a non-nullable type, and that if the value is `null`, the result is a **NullPointerException**, called an NPE for short. The documentation's list of the only possible causes of an NPE in Kotlin includes the `!!` operator, so a crash from one is never a surprise.

Swearing in words is `requireNotNull`. The Kotlin library page says it throws an `IllegalArgumentException` if the value is `null`, and otherwise returns the value. You can give it a message in braces. Its sample says the value is smart cast afterwards, which means Kotlin treats it as not null from then on. A sibling, `checkNotNull`, throws an `IllegalStateException` instead. The null-safety page adds one more way, which uses `throw` on the right of the Elvis operator, `?:`, the fallback that is used only when its left side is `null`: `?: throw IllegalArgumentException("name expected")`.

This table is our own summary of the build steps.

| Situation | What to write |
|---|---|
| You can point to the reason the value is there | `!!` is acceptable. A short comment saying why helps the next reader |
| The value comes from a user, a file or text that might not be a number | `?:` with a fallback that is true for the app, or `?: return` plus a message to the user |
| `null` here means a bug in your own code, and you want the stop to explain itself | `requireNotNull(value) { "message" }` |
| The value comes from Java code and the type is unclear | Write `String?` on your own name, then handle the empty case |
| You want only to remove the red line | None of these. Read the red line first |

```kotlin
var current: String? = null

fun main() {
    current = "Ada"
    println(current!!.length)
}
```

This prints `3`. The locker was filled on the line before, and nothing else ran in between. The assertion is true here, and it is needed because Kotlin does not trust a `var` written straight in the file to stay the same.

```kotlin
val typed: String = "12a"

fun main() {
    val safe = typed.toIntOrNull() ?: 18
    println("Age: $safe")
    val age = requireNotNull("42".toIntOrNull()) { "Age must be a number" }
    println(age + 1)
}
```

This prints `Age: 18` and then `43`. The Kotlin library page says `toIntOrNull()` gives back `null` if the text is not a valid whole number, and `"12a"` is not. The note `?: 18` is read. The text `"42"` is a number, so the oath in `requireNotNull` is true and `age` is 42.

## Predict the output

```challenge
type: predict
run: false
id: parse-with-bang
verified: kotlin-2.4.21
code: |
  val typed: String = "12a"

  fun main() {
      println("Start")
      val age = typed.toIntOrNull()!!
      println("Age: $age")
  }
options:
  - id: a
    text: "Start, then Age: 0"
    why: "You may have expected a failed parse to give zero. A failed parse gives no answer, and the oath says there is one."
  - id: b
    text: "Start, then Age: 12"
    why: "You may have expected the digits at the front to be used. The whole text must be a number, and 12a is not."
  - id: c
    text: "Start, then the program stops with a NullPointerException"
    correct: true
    why: "The text 12a is not a whole number, so the locker is empty. The oath said it was full, so the program stops on that line and Age is never printed."
output: |
  Start
```

```challenge
type: predict
run: false
id: note-instead-of-oath
verified: kotlin-2.4.21
code: |
  val typed: String = "12a"

  fun main() {
      val age = typed.toIntOrNull() ?: 18
      println("Age: $age")
      val ok = "42".toIntOrNull()!!
      println(ok + 1)
  }
options:
  - id: a
    text: "Age: 18, then 43"
    correct: true
    why: "The first parse fails, so the note is read and gives 18. The second parse works, so the oath is true and 42 plus 1 is 43."
  - id: b
    text: "Age: 12, then 43"
    why: "You may have expected the digits at the front to be used. The whole text 12a is not a number, so the note is read."
  - id: c
    text: "Age: 18, then the program stops with a NullPointerException"
    why: "You may have expected every oath to fail. An oath fails only when the locker is empty, and 42 is a number."
output: |
  Age: 18
  43
```

```challenge
type: predict
run: false
id: oath-checked-where-used
verified: kotlin-2.4.21
code: |
  var current: String? = null

  fun main() {
      current = "Ada"
      println(current!!.length)
      current = null
      println("Done")
  }
options:
  - id: a
    text: "3, then the program stops with a NullPointerException"
    why: "You may have thought the later empty locker breaks the earlier oath. The judge checks an oath on the line where it is sworn, and then the locker held Ada."
  - id: b
    text: "3, then Done"
    correct: true
    why: "At the oath, the locker held Ada, so the oath was true and counted 3 letters. After that nobody asks about the locker, so emptying it does no harm."
  - id: c
    text: "Done"
    why: "You may have thought the oath fails before anything prints. The locker is filled on the line before the oath, so the oath is true."
output: |
  3
  Done
```

## Tricks to remember

- Say the reason aloud. No reason, no oath.
- `!!` is a promise, and the judge believes it at once.
- A parcel you put there a minute ago can be sworn to. A parcel from outside cannot.
- If you must swear, swear in words: `requireNotNull` with a message.

## Common mistakes

- **Using `!!` to make the red line go away.** The red line looks like a nuisance, and `!!` removes it. The red line was Kotlin telling you the value might be missing. Look first, or leave a note.
- **Swearing on a value that came from a user.** It works in your test, so it feels safe. A user types a letter, leaves a box empty or pastes in a space, and the oath breaks. Use `?:` with an honest fallback, and say something to the user.
- **Swapping every `!!` for a default of 0 or an empty text.** A crash is loud, so a quiet default feels kinder. A wrong default can put a wrong number on screen, with no sign that anything failed. Choose a default that is true for the app, or stop with a message.
- **Accepting `!!` in code you did not write.** It ran in the demo, so it looks fine. Ask: what is the reason it is not null? If nobody can say, replace it. The person who can check the code is the person who stays in the job.

## Where this shows up in Android

The Android tip-calculator codelab turns the typed text into a number and says the value could be `null` if the text field is empty. This next part is our own reasoning. If that line ended in `!!`, the app would close whenever the box is empty. Every test with a filled box would still pass.

Another place is the Android page about an **activity**, which is one screen of the app. It shows `onCreate(savedInstanceState: Bundle?)` and says that if the activity has never existed before, the value of the `Bundle` object is `null`. Our own reasoning: writing `savedInstanceState!!` there would work after the screen is rebuilt and close the app the very first time it opens.

Values from Java, an older language that many Android libraries use, are the third place. A nullability annotation is a tag in the Java code that says whether a value may be null (general knowledge). The Android interop page says every non-primitive parameter, return and field type in a public API should have one. Without one, Kotlin sees a **platform type**, which means it cannot tell you whether the value can be missing.

## Say it in an interview

**When is `!!` acceptable?**
When you can point to the reason the value is not null, and the compiler cannot see it. For example, you set the value on the line before, or an earlier check guarantees it. My own reasoning: write a short comment with the reason, because the next reader cannot see it either. It is not acceptable to silence the red line, or on values from users, files or the network.

**What does `!!` throw, and how is that different from `requireNotNull`?**
`!!` throws a NullPointerException with no explanation. The library page says `requireNotNull` throws an `IllegalArgumentException` and accepts a message, while `checkNotNull` throws an `IllegalStateException`. My own reasoning: use `require` when a caller handed you something bad and `check` when your own state is wrong. Write a message either way, so the crash report explains itself.

**What is the trade-off between `!!` and `?:` with a default?**
The `!!` stops loudly at the exact line, so you find the bug early, but the user sees a crash. A default keeps the app running, but if the default is wrong, the user sees wrong data and nobody is told. My own reasoning: for user input, choose an honest default or a message. For a broken promise inside your own code, a loud stop is better.

**Can a non-null type still hold `null` at runtime?**
Yes, through Java. The Kotlin documentation says a platform value gets no nullability error at compile time, and if you assign a null one to a non-null variable, Kotlin throws a NullPointerException. The compiler also adds assertions when platform values are passed to Kotlin functions that expect non-null values. The fix is to write the nullable type on your own variable, or to add nullability annotations in the Java code.

## Recap

1. `!!` turns a missing value into a NullPointerException on that line.
2. Use `!!` only when you can say aloud why the value is there.
3. Otherwise use a safe call (`?.`, which gives `null` instead of crashing), a fallback, or `requireNotNull` with a message.

## You can now

- Predict where a program stops when `!!` meets a failed text-to-number conversion.
- Predict the result when one conversion has a fallback and another uses `!!` on a good value.
- Predict whether a `!!` crashes when the value was set on the line before it.
