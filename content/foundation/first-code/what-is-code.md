---
id: foundation/first-code/what-is-code
title: Why does a computer need such exact instructions?
status: shadow
difficulty: easy
order: 7
requires: [foundation/how-computers-work/what-is-a-cpu]
summary: A computer does only what is written, one step at a time. So every step has to be spelled out, in order and exactly.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: (1) The Predict items use a toy robot and were traced by hand only; no code runs in a Predict item on this leaf. (2) The read-only program in Name it is the documented Kotlin Hello world; it has not been compiled by me (no shell in this session). Kotlin 2.4.21 compile still needed. (3) The claim that a computer has no unwritten knowledge rests on the earlier CPU leaf (fetch one instruction, do it, move on); no separate authoritative source was fetched. Replace or confirm with a computer-science text before live.
Checked on 21 Oct 2026 against: https://kotlinlang.org/docs/kotlin-tour-hello-world.html ("fun is used to declare a function"; "The main() function is where your program starts from"; "println() and print() functions print their arguments to standard output"; example fun main() { println("Hello, world!") } with output Hello, world!); https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/println.html (println "Prints the given message and the line separator to the standard output stream."); https://kotlinlang.org/docs/strings.html ("To declare a String literal, enclose the value in double quotes"); https://kotlinlang.org/docs/getting-started.html ("Kotlin is a modern language that's concise, multiplatform, and interoperable with Java and other languages."); https://developer.android.com/kotlin ("Kotlin is fully supported for Android development"; "Android's modern UI toolkit is built on Kotlin"). -->
## The question

You say to a friend, "Make me a sandwich." Your friend finds the bread, picks a filling, and does not put the sandwich in a sock. You never had to say any of that.

Now you say the same words to a computer. It cannot do it. Why does a computer need such exact instructions, when a person understands a sloppy note?

## Mental model

Think of a very literal robot waiter.

- The robot waiter does exactly what is on its order slip, one line at a time.
- The order slip is the list of steps you write for it.
- The sandwich is what you want at the end.
- The robot knows a few words, such as "bread", "cheese", "put" and "plate". It knows nothing else.

If a step is not on the slip, the robot does not do it. If a word is spelled wrong, it is a word the robot does not know. If the steps are in the wrong order, you get a different sandwich.

**Where the analogy stops:** A real robot can stop and ask, "Did you mean cheese?" A computer running your instructions cannot. It cannot see your kitchen either. It sees only what is written.

## Build the logic

### Notice

Here are two order slips for the robot, side by side.

| Slip 1 (sloppy) | Slip 2 (exact) |
|---|---|
| Make a sandwich. | 1. Put a slice of bread on the plate. |
| | 2. Put cheese on the bread. |
| | 3. Put a second slice of bread on top. |

A person reads slip 1 and fills in a hundred things that are not written: which bread, how many slices, where to put it. Slip 2 writes down every step the person was doing without thinking.

### Guess

The computer follows one instruction at a time, as you saw with the CPU. So why can it not fill in the missing steps of slip 1? Here are three ideas.

- A. It is not clever enough yet, but it will learn to guess.
- B. It has no unwritten knowledge. It has the steps it was given, and nothing else.
- C. It could guess, but it chooses not to.

Idea A feels right because some apps do accept a sloppy note. But the guessing was built in ahead of time, as exact steps written by people. The computer that runs it still follows exact steps. Idea C gives the computer a choice it does not have. Idea B is the answer.

### Build

1. The computer can only do what is written.
2. So every step has to be written, even the ones a person does without thinking.
3. The steps run one at a time, from the top. So the order matters.
4. Each step must use words the computer knows, spelled the way it expects. A misspelled word is an unknown word.
5. Words inside quote marks are copied as they are. The computer does not look for a meaning inside them.

### Name it

**Code** is a list of instructions written for a computer. A **program** is a complete piece of code that does one job.

Code is written in a **programming language**: a fixed set of words and rules the computer can be given. Kotlin is a programming language. The Kotlin documentation calls it a modern language.

The spelling and punctuation rules of a language are its **syntax**. The robot's word "cheese" is the same idea: the computer knows the word only when it is spelled right.

Here is a complete Kotlin program. You will run it in the next lesson. Do not worry about every word yet.

```kotlin
fun main() {
    println("Hello, world!")
}
```

Read it like an order slip with two parts. **`main()`** is where the program starts, so the computer begins here. **`println`** prints what is inside the brackets, and then moves to a new line. The text sits between double quotes, which is how Kotlin marks text. Change one letter of `println` and the computer no longer knows the word, like the robot and "chese".

## Predict the output

```challenge
type: predict
run: false
id: bread-on-the-outside
verified: not-run
code: |
  The robot puts things in a pile, in the order of the slip.
  Which slip gives bread on the outside, with cheese in the middle?

  Slip A:
    1. Put a slice of bread on the plate.
    2. Put cheese on the bread.
    3. Put a second slice of bread on top.

  Slip B:
    1. Put cheese on the plate.
    2. Put a slice of bread on the cheese.
    3. Put a second slice of bread on the first slice.
options:
  - id: a
    text: "Slip A"
    correct: true
    why: "The robot builds the pile from the bottom up: bread, cheese, bread. The order on the slip is the order of the pile."
  - id: b
    text: "Slip B"
    why: "You may have counted the same three things. Slip B puts the cheese on the plate first, so the cheese ends up at the bottom, outside."
  - id: c
    text: "Both, because the robot works out what a sandwich is"
    why: "You may have expected the robot to know what you meant. It follows the slip, so the same ingredients in a different order give a different pile."
output: "Slip A"
```

```challenge
type: predict
run: false
id: one-step-missing
verified: not-run
code: |
  Slip A, with step 2 torn off:
    1. Put a slice of bread on the plate.
    3. Put a second slice of bread on top.
  (The robot's rule: it does only the steps it is given.)
  What does the robot make?
options:
  - id: a
    text: "A cheese sandwich, because the robot knows sandwiches have cheese"
    why: "You may have expected the robot to fill the gap. It has no unwritten knowledge, so it does not."
  - id: b
    text: "Two slices of bread with nothing between them"
    correct: true
    why: "The cheese step is gone, so no cheese is put down. The robot does the steps that are left, and the second slice lands on the first."
  - id: c
    text: "Nothing at all, because the robot sees the missing step and stops"
    why: "You may have expected the robot to notice a gap. It cannot see what was left out. It does what it has."
output: "Two slices of bread with nothing between them"
```

```challenge
type: predict
run: false
id: robot-and-chese
verified: not-run
code: |
  The robot knows the word "cheese". It does not know "chese".
  The slip says:
    1. Put a slice of bread on the plate.
    2. Put chese on the bread.
  What happens at step 2?
options:
  - id: a
    text: "The robot puts cheese on the bread anyway"
    why: "You may have thought the robot sees past a typing slip, as a person would. It knows only the words it was given."
  - id: b
    text: "The robot cannot do step 2, because it does not know the word"
    correct: true
    why: "A misspelled word is an unknown word. The robot cannot guess that you meant cheese."
  - id: c
    text: "The robot puts some other filling on the bread"
    why: "You may have expected a nearby guess. The robot makes no guesses. A word it does not know is a step it cannot do."
output: "The robot cannot do step 2, because it does not know the word"
```

## Tricks to remember

- Not on the slip, not done.
- Same steps in a different order make a different sandwich.
- A wrong letter is a word the robot has never heard.
- Quote marks mean "copy this exactly".

## Common mistakes

- **Expecting the computer to figure out what you meant.** Spell-check and voice assistants feel like mind-reading. But their guessing was built from exact steps too. Next time, ask: which step did I leave out?
- **Skipping steps that feel too small to write.** To you, "put cheese on the bread" hides picking up the cheese. The robot has no hidden steps. Write each one.
- **Treating one wrong letter as a small slip.** A person reads "chese" and moves on. The robot reads an unknown word and stops. In code, a single character can be the whole problem.

## Where this shows up in Android

Think of a Like button in an app that does nothing when you tap it. Somebody wrote the order slip for that tap. If a step is missing, or a word is wrong, the app does what the slip says, and the slip says nothing useful.

Android apps are written in code like the program above. The Android documentation says Kotlin is fully supported for Android development. So the exactness you met here is the same exactness behind every screen on a phone.

## Recap

1. A computer does only the steps it is given, one at a time.
2. So every step must be written, in the right order, with words it knows.
3. Code is those written steps, in a programming language such as Kotlin.

## You can now

- Explain why a computer needs exact instructions, using the robot waiter.
- Pick which of two step lists gives the result you want.
- Predict what happens when a step is missing or a word is misspelled.
