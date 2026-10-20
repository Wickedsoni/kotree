---
id: foundation/debugging-mindset/reading-error-messages
title: What is an error message trying to tell me?
status: shadow
difficulty: easy
order: 22
requires: [foundation/programming-thinking/what-is-a-type]
summary: An error message names what went wrong and where. Learn to read the line number and the key word first.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: kotlinlang.org/docs/exceptions.html (exception is thrown, interrupts normal execution; stack trace definition; sample first line "Exception in thread "main" java.lang.ArithmeticException: This is an arithmetic exception!" and the at lines with Main.kt:3; the program throw ArithmeticException(...)); developer.android.com/codelabs/basic-android-kotlin-compose-update-data-room ("Unresolved reference: itemsRepository", "Notice the type mismatch error"); developer.android.com/studio/debug/stacktraces (list of method calls with filenames and line numbers; click filenames to open source). Exact compile-error wording varies by Kotlin version, so the lesson names the words only in plain terms. Not compiled: no kotlinc here. -->

## The question

You press Run and red text fills the screen. It looks like a wall of words, and your stomach drops. Did you break something? Which part of that text do you read first?

## Mental model

Think of a warning light on a car dashboard.

- The car is your program.
- The dashboard is the place where messages appear.
- The warning light is the error message. It comes on because the car noticed a problem. It does not mean you broke the engine.
- The symbol on the light names the system: oil, brakes, battery. In an error message, the key words name the kind of problem.
- The manual has one page for each symbol. The line number in a message is like a page number: it tells you where to look.
- Some lights come on when you turn the key, and the car will not start. Others come on while you drive, and the car stops.

**Where the analogy stops:** A warning light is a picture, but an error message is words you can read. A light does not tell you which page of the manual to open, but a message gives a line number. Also, the line a message names is where the problem was noticed. The real slip can sit on the line above, so read both. This part is our own reasoning, not the documentation's.

## Build the logic

### Notice

Look at two cases.

In the first, you type a word that the program was never told about. You press Run. Nothing runs, not even the lines before the bad one. A message says it does not know that word, and it gives a line number.

In the second, the program starts and says "Start". Then it stops. A message appears. It names a kind of problem, it has a sentence in it, and below that are lines that name places in your code.

Both look alarming. In both, the computer is pointing at something.

### Guess

A message says "line 5". You open line 5 and see nothing odd. What do you do?

- A. Decide the message is wrong and ignore it.
- B. Read the word the message names, then read line 5 and the line above it.
- C. Delete line 5 and run again.

Idea A throws away the one clue you were given. Idea C removes a line without knowing why it was flagged. Idea B is the answer.

### Build

1. Ask when the message appeared. Was it before the program started, or after it printed something?
2. Read the first line of the message. It names the problem in a few words.
3. Find the line number. If there is a file name, note it too.
4. Open that line. Look for the word the message names.
5. Read the line above as well.
6. Fix one thing, then run again. The next message may be a different one, and that is progress.

### Name it

A message that appears before the program starts comes from the **compiler**: the tool that reads your whole program first and refuses to run it if a rule is broken. Such a message is a **compile error**. Nothing runs, even the lines that come before the problem.

The program below uses a name that does not exist. The compiler refuses it. Counting from `fun main`, the message points at line 3, the `println` line, and names the word `nme`.

```kotlin
// does not compile
fun main() {
    val name = "Ada"
    println(nme)
}
```

The exact wording of compile errors varies by Kotlin version. The Android codelabs show one wording for this kind of problem: "Unresolved reference: itemsRepository", for a name used where it is not available. They also show a second kind, which they call a type mismatch error: a value of one type where another was expected.

A message that appears after the program started comes from an **exception**. The Kotlin documentation says that throwing an exception indicates that an unexpected error has occurred while the program runs, and that it interrupts the normal execution of the program. So lines before the crash have already run, and lines after it never do.

This program is the one the Kotlin documentation uses to show an exception.

```kotlin
fun main() {
    throw ArithmeticException("This is an arithmetic exception!")
}
```

The documentation shows this message for it. The report is called a **stack trace**: it shows the sequence of function calls leading to the point where the error happened.

| Part of the message | In the documentation sample | What it tells you |
|---|---|---|
| First line, the type | `java.lang.ArithmeticException` | The kind of problem |
| First line, the message | `"This is an arithmetic exception!"` | The words the programmer wrote |
| A line that starts with `at` | `at MainKt.main(Main.kt:3)` | The function, the file and the line number |

Read the first line first. Then look at the `at` line that names your file, and open that line number.

## Predict the output

Each item gives you a message in words. Use it, then work out what the program prints. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: fix-the-named-word
verified: not-run
code: |
  fun main() {
      val first = "Ada"
      val last = "Lovelace"
      println(first)
      println(lst)
  }
  // The compiler points at line 5 and says it cannot resolve the name lst.
  // Fix the name on that line. What does the program print?
options:
  - id: a
    text: "Ada, then Lovelace"
    correct: true
    why: "The message named lst on line 5. The purse that holds the last name is called last. With that fixed, both lines run."
  - id: b
    text: "Ada, then lst"
    why: "You may have expected the word to print as you typed it. After the fix, the name stands for the value it holds."
  - id: c
    text: "Ada only"
    why: "You may have thought line 5 would be skipped. Once the name is right, every line runs."
output: |
  Ada
  Lovelace
```

```challenge
type: predict
run: false
id: lines-before-the-crash
verified: not-run
code: |
  fun brew(teaBags: Int) {
      if (teaBags == 0) {
          throw IllegalStateException("Tea jar is empty")
      }
  }

  fun main() {
      println("Start")
      brew(0)
      println("End")
  }
  // The crash message says: Tea jar is empty. What does println print before it?
options:
  - id: a
    text: "Nothing"
    why: "You may have treated it like a compile error. A crash happens while the program runs, so lines before it have already run."
  - id: b
    text: "Start, then End"
    why: "You may have expected the program to carry on. A thrown exception interrupts normal execution, so End never runs."
  - id: c
    text: "Start"
    correct: true
    why: "Start runs first. Then brew(0) throws, and the program stops before the line that prints End."
output: "Start"
```

```challenge
type: predict
run: false
id: types-do-not-match
verified: not-run
code: |
  fun main() {
      val age: Int = "12"
      println(age + 1)
  }
  // The compiler points at line 2: it expected a whole number (Int) and found text (String).
  // Fix line 2 so the value fits the type Int. What does the program then print?
options:
  - id: a
    text: "121"
    why: "You may have joined text and a number. After the fix, age holds a number, so the plus sign adds."
  - id: b
    text: "13"
    correct: true
    why: "The message said line 2 holds text where a whole number belongs. Written as the number 12, age plus 1 is 13."
  - id: c
    text: "12"
    why: "You may have dropped the plus 1. The print line still runs and adds 1 to age."
output: "13"
```

## Tricks to remember

- A warning light is a clue, not an accusation.
- Before or after "Start"? That tells you which kind of message it is.
- First line says what. Line number says where.
- Read the named line, then the line above it.

## Common mistakes

- **Thinking the message means you broke the computer.** Red text feels like damage. It is the computer telling you what it could not do. Read it like a note.
- **Reading the whole message at once.** A long message looks like it must all matter. The first line and one line number are enough to start with.
- **Fixing a line the message did not name.** A line that looks wrong feels like the culprit. Fix the one the message names first, then run again and read the new message.
- **Fixing five things before running.** It feels faster. A new message after each fix tells you whether the last change helped.

## Where this shows up in Android

You change a class in an Android project and the editor shows an error on a name. The Android codelab for Room shows this case: "You now get an error - Unresolved reference: itemsRepository." Its fix is to pass the missing object into the class that uses it. The key words named the problem, and the name pointed at the line.

An app can also crash while it runs. The Android documentation says a stack trace shows "a list of method calls that lead to the exception being thrown, together with the filenames and line numbers where the calls happened." In Android Studio, you click the highlighted file name to open the source. That is the same two steps as here: the first line says what, the file and line say where.

## Recap

1. An error message names the kind of problem and the place where it was noticed.
2. A compile error appears before the program runs, and a crash appears after it has started.
3. Read the first line, open the named line, and read the line above it.

## You can now

- Tell a compile error from a crash by whether the program printed anything first.
- Find the word and the line a compile message names, and predict the output after the fix.
- Predict which lines run before an exception stops a program.
