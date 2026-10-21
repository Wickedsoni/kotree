---
id: foundation/from-code-to-running/what-is-a-compiler
title: How does my code become instructions the CPU understands?
status: live
difficulty: easy
order: 26
requires: [foundation/first-code/what-is-code, foundation/debugging-mindset/reading-error-messages]
summary: A compiler checks your whole program first, then translates it into steps a machine can follow. If a rule breaks, nothing runs.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [translator-box]
playground: false
fruit: false
upnext: false
---

## The question

You type a few lines of Kotlin and press Run. The main chip of your computer has never heard of Kotlin, because it only follows its own tiny steps. Still, your program runs. And sometimes a message appears before anything starts. What happens to your text in between?

## Mental model

Think of a translator who works for you.

- Your letter is the program text you typed, written in words a person can read.
- The reader on the far side is the main chip. It reads only one language, made of small numbered tiles.
- The translator reads your whole letter before passing on a single tile. It checks that every sentence follows the rules of the language.
- Each sentence becomes a few tiles. The stack of tiles is the translated letter.
- If a sentence makes no sense, the translator hands the letter back with a red note that names the sentence. No tiles go to the reader.
- When the translation works, the reader follows the tiles one at a time, in order.

**Where the analogy stops:** A human translator can often work out what a muddled sentence meant. A compiler cannot. It follows fixed rules and stops. Also, a reader would have to think, but the chip does no thinking: it only follows tiles. And the translator checks that the sentences follow the rules, not that they say what you meant. A letter can be translated perfectly and still ask for the wrong thing.

## See it move

```sim
id: translator-box
template: flow
title: A translator box between cards and the reader
intro: Two cards lie on your desk, one clean and one with a typo. The translator box and the reader are empty.
stations:
  - { id: desk, label: Your cards }
  - { id: box, label: Translator box }
  - { id: reader, label: "Reader: the main chip" }
items:
  - { id: clean, label: Clean card, at: desk }
  - { id: typo, label: Card with a typo, at: desk }
vars: { fixed: 0 }
actions:
  - id: drop-clean
    label: Drop the clean card in
    cases:
      - when: { at: { clean: desk } }
        do:
          - move: { clean: reader }
          - label: { clean: "Stack of tiles" }
          - mark: { clean: good }
          - light: [ reader ]
          - say: "The translator reads the whole card first and finds nothing wrong. Each sentence becomes a few tiles, and the stack of tiles reaches the reader. The reader follows the tiles one at a time."
    refuse: The clean card has already been dropped in. Press Reset to start again.
  - id: drop-typo
    label: Drop the card with the typo in
    cases:
      - when: { at: { typo: desk }, var: { fixed: 0 } }
        do:
          - move: { typo: box }
          - label: { typo: "Handed back: red note" }
          - mark: { typo: bad }
          - light: [ box ]
          - say: "The translator reads the whole card before passing on a single tile. One sentence breaks a rule, so the card is handed back with a red note that names that sentence. No tiles reach the reader, not even for the good sentences."
      - when: { at: { typo: desk }, var: { fixed: 1 } }
        do:
          - move: { typo: reader }
          - label: { typo: "Stack of tiles (fixed)" }
          - mark: { typo: good }
          - light: [ reader ]
          - say: "The fixed card has no sentence that breaks a rule, so the translator makes tiles and the stack reaches the reader."
    refuse: The card is not on your desk. Press "Fix the typo" to take it back.
  - id: fix
    label: Fix the typo
    cases:
      - when: { var: { fixed: 0 } }
        do:
          - set: { fixed: 1 }
          - move: { typo: desk }
          - label: { typo: "Card, typo fixed" }
          - mark: { typo: plain }
          - light: [ desk ]
          - say: The typo is fixed and the card is back on your desk. Drop it in again.
    refuse: The typo is already fixed.
script: [ drop-clean, drop-typo, fix, drop-typo ]
try:
  - text: "Press \"Drop the clean card in\". Watch the stack of tiles reach the reader."
    done: [ drop-clean ]
  - text: "Press \"Drop the card with the typo in\". See where it stops and read the red note."
    done: [ drop-typo ]
  - text: "Press \"Fix the typo\", then drop that card in again."
    done: [ fix, drop-typo ]
```

**What you should notice:** A card with one bad sentence is stopped at the door, and none of its tiles reach the reader, not even the ones for the good sentences.

## Build the logic

### Notice

Two letters sit on the desk. Bo's letter has three sentences, and all three follow the rules of the language. Ada's letter also has three sentences, but the second one uses a word that is not in the language. The reader on the far side understands only tiles. Both letters go to the translator.

### Guess

What should the translator do with Ada's letter?

- A. Translate sentence 1 and pass its tiles on. Stop when it reaches sentence 2.
- B. Read all three sentences first. Pass nothing on, and hand back a red note that names sentence 2.
- C. Work out what Ada probably meant and translate that.

Idea A sends part of the job on before anyone knows the rest is broken. Idea C turns the translator into a guesser, and a wrong guess would do something Ada never asked for. Idea B is the answer.

### Build

1. Ada writes the letter in words people can read.
2. The translator reads the whole letter before translating anything.
3. It checks each sentence against the rules: only known words, and the right kind of thing in each place.
4. If a sentence breaks a rule, the translator stops. The red note names the sentence and says what is wrong. Nothing goes to the reader.
5. If every sentence passes, the translator turns each one into tiles, in order.
6. The stack of tiles goes to the reader, who follows them one at a time.
7. If Ada changes the letter, the translator does all the steps again.

### Name it

The text you type is **source code**. The tool that reads all of it, checks the rules and translates it is a **compiler**. A broken rule gives a **compile error**, which is the red note. The tiles are **instructions**: tiny steps such as "add these two numbers". The main chip, the **CPU**, follows them one at a time.

What the tiles look like depends on where the program is meant to run. One target is the **JVM**, a program that runs **bytecode**. Bytecode is an in-between form that is the same on every kind of computer, so it is not yet the chip's own steps. That description is general knowledge, not from the Kotlin documentation. The Kotlin documentation says: "When targeting the JVM, Kotlin produces Java-compatible bytecode." Another target is native. The documentation says that when targeting native, Kotlin produces platform-specific code. That means steps for one kind of chip.

On the command line, the Kotlin compiler is called `kotlinc`. The documentation compiles a file called `hello.kt` with `kotlinc hello.kt -include-runtime -d hello.jar`. It says the `-d` option gives the output path for the generated files. Compiling is its own step, and it happens before the program runs.

```kotlin
fun main() {
    println("Hello")
}
```

This program follows every rule, so the compiler accepts it. When its steps are followed, `Hello` appears. Change `println` to `printn` and the compiler refuses the file before anything is shown, because it cannot find that name.

## Predict the output

Work these out on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: typo-in-the-middle
verified: not-run
code: |
  fun main() {
      println("Pack")
      printn("Send")
      println("Done")
  }
  // The compiler cannot find the name printn on line 3 and refuses the file.
  // Fix that name. What does the program print?
options:
  - id: a
    text: "Pack"
    why: "You may have thought the lines before the typo run first. The translator reads the whole letter before passing anything on, so nothing ran at all."
  - id: b
    text: "Pack, then Done"
    why: "You may have expected the bad sentence to be skipped. The translator never skips one. It hands the letter back until the typo is fixed."
  - id: c
    text: "Pack, then Send, then Done"
    correct: true
    why: "Once the name is fixed, every sentence follows the rules. The translator passes the whole stack of tiles on, and the reader follows all three in order."
output: |
  Pack
  Send
  Done
```

```challenge
type: predict
run: false
id: index-passes-the-checks
verified: not-run
code: |
  fun main() {
      println("Start")
      val names = listOf("Ada", "Ben")
      println(names[2])
      println("End")
  }
options:
  - id: a
    text: "Nothing prints, because the compiler counts the items first"
    why: "You may have expected the translator to count the items. It checks names and kinds of values. It does not follow the program to see how long a list is while it runs."
  - id: b
    text: "Start, then the program stops"
    correct: true
    why: "Every sentence follows the rules, so the translator passes the tiles on and the reader starts. It prints Start, then fails at position 2, which is past the end, and never reaches End."
  - id: c
    text: "Start, then Ben, then End"
    why: "You may have counted from 1. The first item sits at position 0, so position 2 is one past the last item."
output: "Start"
```

## Tricks to remember

- Read it all, then run it.
- One bad sentence, no tiles.
- The chip reads tiles, never your text.
- The translator checks the rules, not what you meant.

## Common mistakes

- **Thinking the computer runs your text as you typed it.** The text looks like instructions, so it feels like they run as written. The chip only follows tiles. Your text is turned into tiles first.
- **Expecting the lines before a typo to run.** A program feels like it starts at line 1. The compiler reads everything first, so a typo anywhere stops the whole run.
- **Reading "it compiled" as "it is right".** No red note feels like approval. The compiler checks the rules, not what you meant. A program can pass and still give a wrong answer or stop while running.
- **Arguing with the red note.** It feels like the compiler is being picky. The note names the sentence where it stopped, so open that line and read it, and the line above it.

## Where this shows up in Android

Android Studio is the program most Android developers write their apps in. You press Run, and the app does not open. The Build Output tab lists errors instead.

The Android documentation says the build system compiles app resources and source code and packages them. It also says that if you receive build-time or compile-time errors, you can inspect the tree and select an element to read the error output. That the app does not open is our own reasoning: a failed compile means no new program was made to start. It matches the first Predict item.

## Recap

1. A compiler reads all your source code first, checks every rule, then translates it.
2. If one rule breaks, the compiler stops and nothing runs.
3. Passing the compiler shows the rules hold, not that the program does what you meant.

## You can now

- Predict what a program prints once the one line the compiler refuses is fixed.
- Predict that a program can pass the compiler and still stop while it runs.
