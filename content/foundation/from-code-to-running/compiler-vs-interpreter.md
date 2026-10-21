---
id: foundation/from-code-to-running/compiler-vs-interpreter
title: Why do some languages translate everything first and others line by line?
status: live
difficulty: easy
order: 27
requires: [foundation/from-code-to-running/what-is-a-compiler]
summary: A compiler translates the whole program before it runs. An interpreter translates and runs one step at a time. Kotlin compiles first.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [translation-lanes]
playground: false
fruit: false
upnext: false
---

## The question

A friend learning another language says, "My program printed two lines and then stopped with an error." In Kotlin, a typo usually shows up before anything prints. Both of you wrote a typo. Why do two languages behave so differently?

## Mental model

Think of a book in a language you cannot read, and two ways to hear it in your own language.

- The book is the program.
- The print-shop translator reads the whole book first and prints a full copy in your language. After that you can read the copy as often as you like, and the translator is not needed.
- The live speaker sits beside you. The speaker reads one sentence from the book and says it in your language at once, then reads the next. The speaker has to be there every time you want to hear the book.
- You are the one who does what each sentence says.
- A typo on page 3 is a sentence that breaks the rules of the language.

**Where the analogy stops:** Real tools are not always one or the other. Many programs are translated in two stages: a print shop makes a copy, and then a live speaker reads that copy. Also, a live speaker in real life is slow, but a real interpreter is a program and can be fast, so do not read "live" as "slow".

## See it move

```sim
id: translation-lanes
template: compare
title: A print-shop lane and a live-speaker lane
intro: A book has a typo on sentence 3. One lane prints a full copy first and the other says each sentence as it is read. Nothing is heard yet.
sides:
  - id: print-shop
    label: Print-shop lane
    boxes:
      - { id: book, label: The book to translate }
      - { id: check, label: Check the whole book first }
      - { id: heard, label: "Sentences heard: 0" }
      - { id: count, label: "Translations done: 0" }
  - id: live-speaker
    label: Live-speaker lane
    boxes:
      - { id: book, label: The book to translate }
      - { id: speak, label: Say each sentence as it is read }
      - { id: heard, label: "Sentences heard: 0" }
      - { id: count, label: "Translations done: 0" }
actions:
  - id: send-typo
    label: Send the book with the typo
    do:
      - light: [ print-shop.check, live-speaker.speak ]
      - label:
          print-shop.check: "Check: typo found on sentence 3, so no copy is printed"
          print-shop.heard: "Sentences heard: 0"
          print-shop.count: "Translations done: 0"
          live-speaker.speak: "Said sentences 1 and 2, then stopped at the typo"
          live-speaker.heard: "Sentences heard: 2"
          live-speaker.count: "Translations done: 2 (one per sentence)"
      - say: In the print-shop lane the typo is found first, so nothing is heard. In the live-speaker lane sentences 1 and 2 are heard before it stops at sentence 3.
  - id: send-clean
    label: Send the clean book
    do:
      - light: [ print-shop.check, live-speaker.speak ]
      - label:
          print-shop.check: "Check: no typo, so a full copy is printed"
          print-shop.heard: "Sentences heard: 3"
          print-shop.count: "Translations done: 1 (the whole book, once)"
          live-speaker.speak: "Said sentences 1, 2 and 3 as they were read"
          live-speaker.heard: "Sentences heard: 3"
          live-speaker.count: "Translations done: 3 (one per sentence)"
      - say: With no typo, both lanes get all 3 sentences across. The print shop translated once, and the live speaker translated each sentence as it came.
  - id: hear-three
    label: Hear the clean book three times
    do:
      - light: [ print-shop.heard, live-speaker.heard ]
      - label:
          print-shop.check: "The printed copy is read again, with no new translation"
          print-shop.heard: "Sentences heard: 9 (3 sentences, 3 times)"
          print-shop.count: "Translations done: 1 (the copy is reused)"
          live-speaker.speak: "The speaker must say every sentence again"
          live-speaker.heard: "Sentences heard: 9 (3 sentences, 3 times)"
          live-speaker.count: "Translations done: 9 (3 sentences, 3 times)"
      - say: Hearing the book three times, the print shop still translated once, and the live speaker translated every sentence again each time.
script: [ send-typo, send-clean, hear-three ]
try:
  - text: Press "Send the book with the typo". Compare how many sentences are heard in each lane.
    done: [ send-typo ]
  - text: Press "Send the clean book". Compare the translation counters of the two lanes.
    done: [ send-clean ]
  - text: Press "Hear the clean book three times". Compare the counters again.
    done: [ hear-three ]
```

**What you should notice:** In the print-shop lane the typo stops everything before any sentence is heard, but in the live-speaker lane the sentences before the typo are heard first.

## Build the logic

### Notice

Here is a small book with three sentences: "Pack the bag." "Send the bag." "Zxq the bag." The word "Zxq" is not in the language. You ask each helper in turn to let you hear the book.

### Guess

What do you hear from each helper?

- A. Print shop: sentences 1 and 2, then an error. Live speaker: nothing, only an error.
- B. Print shop: nothing, only a red note. Live speaker: sentences 1 and 2, then it stops at sentence 3.
- C. Both helpers skip the bad word and say the other two sentences.

Idea A swaps the helpers. Idea C gives the helpers a skill neither has, because a broken sentence is never skipped. Idea B is the answer.

### Build

1. The print shop reads and checks the whole book, then prints a copy. A bad sentence stops the work at the shop's desk, and you hear nothing.
2. The translation work is done once. You can listen to the copy again without paying for it again.
3. The live speaker reads and says one sentence at a time. A bad sentence is found only when the speaker reaches it, so the sentences before it are already heard.
4. The speaker translates again every time you ask to hear the book.
5. Compare the helpers by three questions: when is a mistake found, how often is the work done, and what does the listener receive.

### Name it

A **compiler** translates the whole program before it runs, and the translated copy is what runs. An **interpreter** reads a program and carries out each step as it goes, with no separate translated copy. These plain versions of both ideas, the table below and the point that real tools often mix them are general programming knowledge, not from the Kotlin documentation.

| Question | Compile first | Interpret as you go |
|---|---|---|
| When is the whole program checked? | Before it runs | Each step, when it is reached |
| A typo on line 3 | Nothing runs, and a message appears first | Lines 1 and 2 run, then it stops at line 3 |
| How often is the translation done? | Once per change | Again, each time it runs |
| What reaches the machine? | A translated copy | The original text, read by the interpreter |

Kotlin works the compile-first way. The Kotlin command-line page compiles a file with `kotlinc`, in a step of its own, before anything runs. Python and JavaScript are common examples of languages that are usually run by an interpreter. That is general programming knowledge, not from the Kotlin documentation.

Kotlin also has a shell where you type code and see results. The documentation says that with the `-Xrepl` option, you can type any valid Kotlin code in the shell and see the results. It also says a Kotlin script is a source file ending in `.kts` with top-level executable code, run with `kotlinc -script`. These pages do not say how the shell or scripts work inside, so this lesson makes no claim that they run line by line.

## Predict the output

Follow each item on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: live-lane-stops-at-three
verified: not-run
code: |
  Book:
  1. say "Pack"
  2. say "Send"
  3. sayy "Done"
  (The word sayy is not in the language.)

  Send this book down the live-speaker lane.
  What is said aloud before the speaker stops?
options:
  - id: a
    text: "Nothing"
    why: "You may be thinking of the print-shop lane, which reads the whole book before it says anything. The live speaker says each sentence as soon as it is read."
  - id: b
    text: "Pack, then Send"
    correct: true
    why: "The speaker reads sentence 1 and says it, then reads sentence 2 and says it. The broken word turns up only at sentence 3, so the speaker stops there."
  - id: c
    text: "Pack, then Send, then Done"
    why: "You may have expected the speaker to skip the broken word. No lane skips a broken sentence. The live lane stops later than the print-shop lane."
output: |
  Pack
  Send
```

```challenge
type: predict
run: false
id: translation-work-count
verified: not-run
code: |
  fun main() {
      val sentences = 5
      val timesHeard = 3
      // $name shows a value. ${...} shows the result of a calculation.
      println("Print shop: $sentences")
      println("Live speaker: ${sentences * timesHeard}")
  }
options:
  - id: a
    text: "Print shop: 5, then Live speaker: 15"
    correct: true
    why: "The print shop translates the five sentences once and you reuse its copy. The live speaker translates all five again each time you listen, so 5 times 3 is 15."
  - id: b
    text: "Print shop: 15, then Live speaker: 5"
    why: "You may have swapped the helpers. The print shop pays for the work once. The live speaker pays each time."
  - id: c
    text: "Print shop: 5, then Live speaker: 5"
    why: "You may have thought both helpers translate once. The live speaker has no copy to reuse, so the work repeats on every listen."
output: |
  Print shop: 5
  Live speaker: 15
```

## Tricks to remember

- Print shop: check it all, copy once.
- Live speaker: say as you read, and stop where it breaks.
- A message before the first line prints points to compile first.
- Lines that run and then stop point to going step by step.

## Common mistakes

- **Believing Kotlin is read and run line by line, like a script.** The lines of a program do run from top to bottom, so it feels line by line. That is the running. Before it, Kotlin was already checked and translated as a whole.
- **Calling one way the winner.** A contest feels natural when there are two options. Each pays at a different time. Compile first finds typos before anything runs, and going step by step lets you see results at once.
- **Expecting an error in the same place in every language.** One language shows the message before the first line prints, and another shows it halfway. Check which way you are working with before you hunt for the line.
- **Thinking every tool is only one of the two.** The two names feel like two boxes. Many real systems use both stages, as the Android section shows.

## Where this shows up in Android

Android Studio is the program most Android developers write their apps in. When you press Run, one of two things can happen. The app does not open, and the Build Output tab lists errors. Or the app opens and then closes. The first is the compile stage, and the second is the app running after it was built.

The Android documentation says that if you receive build-time or compile-time errors, you can inspect the tree and select an element to read the error output. The same documentation describes the **Java Virtual Machine (JVM)** as "an interpreter that executes Java applications". It says you use the JVM to run Android Studio and the Gradle build tool, which builds your app. So in one workflow, the build compiles your code, and the JVM, an interpreter, is what runs the build tools. Matching the two symptoms to the two stages (compile, then run) is our own reasoning.

## Recap

1. A compiler translates everything first, and an interpreter translates as it runs.
2. A typo stops everything with compile first, but earlier lines still run step by step.
3. Kotlin compiles first, and many real systems combine both ways.

## You can now

- Predict which lines run before a broken line when a program is run step by step.
- Work out how many translations each way does when a program runs several times.
