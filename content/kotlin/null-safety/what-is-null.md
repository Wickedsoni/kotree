---
id: kotlin/null-safety/what-is-null
title: What is null and why does it crash apps?
status: shadow
difficulty: easy
order: 17
requires: [kotlin/types-and-variables/basic-types]
summary: Null means nothing is here. See how Kotlin catches it before your app runs, and how a crash still gets through.
video: ""
publishedOn:
updatedOn:
sims: [null-lunch-box]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/null-safety.html (nullable types with ?, "Null can not be a value of a non-null type String", the compiler reports an error for b.length on a nullable, the !! operator and the NPE it causes, the list "The only possible causes of an NPE in Kotlin"); kotlinlang.org/docs/java-interop.html (platform types, T!, "Kotlin does not issue nullability errors at compile time, but the call may fail at runtime", assigning a platform value to a non-null type can throw NullPointerException, nullability annotations make Java types nullable or non-null); kotlinlang.org/docs/strings.html (a null String? in a template prints null); developer.android.com/kotlin/interop ("Non-annotated types are interpreted as platform types, which have ambiguous nullability"). The profile-screen picture, the difference between null and empty text and the interview trade-offs marked "my own reasoning" are derived. Snippets are hand-traced, not run. -->
## The question

A user fills in a sign-up form and leaves the middle name blank. The app closes on the spot. Nothing in the form looked wrong.

Your code asked for the number of letters in a middle name that was not there. What should a program do when you ask about something that is not there?

## Mental model

Think of a lunch box with a label on the lid.

- The lunch box is a place in your program that holds a value.
- The sandwich is the value.
- An empty box means nothing is here.
- A note with no words on it is not an empty box. It stands for a name with zero letters.
- The label says what kind of box it is. One label reads "always has a sandwich". The other reads "might be empty".
- Asking "how many slices?" means using the value.

If the label says "always has a sandwich", you can ask about the slices straight away. If the label says "might be empty", you must open the box and look first.

**Where the analogy stops:** In real life you open an empty box and see air. In code, "nothing here" is a special value, and Kotlin reads the labels before the program starts. So many mistakes are caught before anyone takes a bite. Also, a box holding a note with no words is not an empty box. That difference matters later.

## See it move

<!-- SIM: null-lunch-box -->

**Try this:**
1. Drag a sandwich into the box labelled "might be empty". Drag it out again.
2. Press "Count the slices" while that box is empty.
3. Try to leave the box labelled "always has a sandwich" empty.
4. (Optional reveal) Press Reset. Tick "Look inside first", then press "Count the slices" on the empty "might be empty" box.

**What you should notice:** The "might be empty" box lets you leave without a sandwich, the "always has a sandwich" box refuses to stay empty, and counting slices in an empty box stops the sim unless you looked first.

## Build the logic

### Notice

The form has three boxes: first name, middle name and last name. Ada fills in all three. Sam leaves the middle one empty. The app wants to print how many letters each name has. For Ada it can count. For Sam's middle name there is nothing to count.

### Guess

What should the app do when it reaches Sam's empty box? Here are three ideas.

- A. Pretend the box holds a name with zero letters.
- B. Mark the box as "nothing here" and look for that mark before counting.
- C. Count anyway and see what happens.

Idea A hides the truth. Sam has no middle name, which is not the same as a middle name made of nothing. Idea C is the crash from the opening question. Idea B is what Kotlin does.

### Build

1. Give every box a label: "always has a value" or "might have nothing".
2. Never hand nothing to a box labelled "always". Refuse it before the program starts.
3. Before asking about what is inside a box labelled "might have nothing", find out whether something is there.
4. If you skip that and ask anyway, the program stops with an error.

### Name it

The mark for "nothing here" is **`null`**. A box that must always hold a value has a **non-null type**, written `String`. A box that might hold nothing has a **nullable type**, written with a question mark: `String?`. The Kotlin documentation says: "To allow null values, declare a variable with a ? sign right after the variable type."

The question from step 3, "is anything here?", is written `middleName != null`. It answers `true` or `false`. The opposite question, `middleName == null`, answers `true` when nothing is here.

```kotlin
val firstName: String = "Ada"
val middleName: String? = null

fun main() {
    println(firstName)
    println(middleName)
    println(middleName != null)
}
```

This prints `Ada`, then `null`, then `false`. Printing an empty box is fine. The question `middleName != null` answers `false` because nothing is here.

Kotlin refuses to run the next two programs. They break rules 2 and 3.

```kotlin
// does not compile
val firstName: String = null

fun main() {
    println(firstName)
}
```

```kotlin
// does not compile
val middleName: String? = null

fun main() {
    println(middleName.length)
}
```

Kotlin has one more tool. You can tell it "trust me, this box is not empty" by writing `!!` after the name. This is the **not-null assertion**. If the box is empty after all, the program stops with a **NullPointerException**, called an NPE for short. That is the crash from step 4.

## Predict the output

```challenge
type: predict
run: false
id: print-null-box
verified: not-run
code: |
  val middleName: String? = null

  fun main() {
      println("Middle name: $middleName")
      println("Done")
  }
options:
  - id: a
    text: "Middle name: followed by nothing, then Done"
    why: "You may have expected a blank. An empty box prints as the word null, not as nothing."
  - id: b
    text: "Middle name: null, then Done"
    correct: true
    why: "The box is allowed to be empty. Printing an empty box shows the word null. Nobody asked about the contents, so nothing crashes."
  - id: c
    text: "The program stops before it prints anything"
    why: "You may have expected a crash. Null only hurts when you ask about what is inside."
output: |
  Middle name: null
  Done
```

```challenge
type: predict
run: false
id: bang-on-empty-box
verified: not-run
code: |
  val middleName: String? = null

  fun main() {
      println("Start")
      println(middleName!!.length)
      println("End")
  }
options:
  - id: a
    text: "Start, then the program stops with a NullPointerException"
    correct: true
    why: "The !! told Kotlin to trust you that the box has a sandwich. The box was empty, so the promise broke on that line and End was never reached."
  - id: b
    text: "Start, 0, End"
    why: "You may have expected an empty box to have zero slices. Null is not zero. There is nothing to count."
  - id: c
    text: "Start, null, End"
    why: "The word null shows up only when you print the box itself. Here you asked for its length, which needs a sandwich."
output: |
  Start
```

```challenge
type: predict
run: false
id: empty-note-is-not-null
verified: not-run
code: |
  val nickname: String? = ""
  val surname: String? = null

  fun main() {
      println(nickname == null)
      println(surname == null)
  }
options:
  - id: a
    text: "true, then true"
    why: "You may have counted a note with no words as an empty box. A note with no words is still a sandwich, so the box is not empty."
  - id: b
    text: "false, then false"
    why: "You may have thought the empty box was not empty. The surname box holds nothing, so the question gets a yes."
  - id: c
    text: "false, then true"
    correct: true
    why: "The nickname box holds a note with no words. That is something, so it is not nothing. The surname box is empty, so the answer there is yes."
output: |
  false
  true
```

## Tricks to remember

- Read the label before the lunch. Look at the type before you look at the value.
- A question mark after the type means "might be empty".
- `!!` is a promise. If the box is empty, the app pays for it.
- A note with no words (`""`) is a sandwich. `null` is no sandwich.

## Common mistakes

- **Treating null and empty text as the same.** In everyday talk both mean "no name". In code, `""` is a text with zero letters and `null` is no text. Choose `String?` when "nothing here" is a real answer.
- **Adding `!!` to make the red line go away.** It feels like the compiler is being fussy. The red line was the compiler's warning. Check for `null` first.
- **Thinking Kotlin makes crashes from null impossible.** The compiler is strict, so this feels true. The Kotlin documentation lists the `!!` operator and calls into Java code among the causes of an NPE.

## Where this shows up in Android

Picture a profile screen that shows the user's middle name. A new user has none. If the code uses `!!` on that value, the screen crashes the moment it opens. Everything that looked fine in testing used users who had a middle name. This next part is our own reasoning: the label and the check in this lesson are what stand between that user and the crash.

The same crash can come from Java, an older language that many Android libraries are written in. The Android Kotlin interop page says that Java types with no nullability annotation are read as platform types, which have ambiguous nullability. The Kotlin documentation says Kotlin does not report nullability errors at compile time for such values, but the call may fail while the app runs. So for any value that comes from Java code, look at where it comes from.

## Say it in an interview

**What is null, and why does Kotlin treat it specially?**
Null means no value. In many languages any reference, which is a name that points to an object, can be null, and using it crashes with a NullPointerException (general knowledge). Kotlin splits its types into ones that can hold null and ones that cannot, and the compiler checks the split before the app runs. The trade-off is that you must say where null is allowed and then deal with it.

**Is null the same as 0 or an empty string?**
No. An empty string is a text with zero letters, which is a real value. Zero is a real number. Null is no value at all. My own reasoning: if "no answer yet" is different from "the answer is empty" in your app, a nullable type keeps those two apart.

**Does Kotlin make a NullPointerException impossible?**
No. The Kotlin documentation says the only possible causes are an explicit throw of one, the `!!` operator, data that is not ready during setup, and Java interoperation. The last one covers a member access on a null platform value and other issues from Java code. So the safety is strong, but it has gaps at the edges.

**What is a platform type?**
It is how Kotlin sees a value from Java when Java does not say whether it can be null. Tools write it as `String!`, meaning `String` or `String?`, and the documentation says the checks are relaxed, so the call may fail while the app runs. If you put such a value in a non-null variable and it is null, Kotlin throws a NullPointerException, so write `String?` on your own variable or add nullability annotations in the Java code. The trade-off is convenience with Java libraries against the null guarantee.

## Recap

1. `null` means nothing is here.
2. A `String?` box may hold it, and a `String` box never does.
3. Using `!!` on an empty box crashes the app with an NPE.

## You can now

- Predict what a program prints when it prints a `String?` that holds `null`.
- Predict where a program stops when `!!` meets `null`.
- Predict the answer of `== null` for `null` and for an empty text.
