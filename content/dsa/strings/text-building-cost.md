---
id: dsa/strings/text-building-cost
title: Why is joining text with + inside a loop slow?
status: live
difficulty: medium
order: 8
requires: [dsa/arrays/arrays-in-memory, dsa/complexity/reading-complexity-in-code]
summary: A String never changes, so each + in a loop copies all the text so far. A builder writes each piece once.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [rewrite-the-letter]
playground: true
fruit: false
upnext: false
---

## The question

A phone app exports a log as one long piece of text. With 50 log lines it finishes at once. With 50,000 lines the screen waits for a long time. The code only adds one more line to the end each turn, and adding to the end sounds like a small job. So why does the total job grow so much faster than the number of lines?

## Mental model

Think of writing a letter on paper that cannot be changed once the ink is dry.

- The letter is the text so far.
- A word is one piece you want to add.
- To add a word to a letter that cannot change, you take fresh paper, copy every old word onto it, then write the new word. The old paper is thrown away.
- The work sheet is a long sheet with spare room at the end. You write each new word in the next empty space.
- The cost is counted in words written on paper, old or new.

With fresh paper, a letter of 100 words needs 100 old words copied before the 101st can be added. With the work sheet, adding the 101st costs 1 word.

**Where the analogy stops:** Code copies letters, not words, and a computer copies very fast. I count words so the numbers stay small. The work sheet also needs one fair copy at the end. That copy grows in step with the number of words, so I leave it out of the counts.

## See it move

```sim
id: rewrite-the-letter
template: chart
title: Words written to build a letter one word at a time
intro: "A writer adds words to a letter one at a time. One way uses fresh paper every time. The other uses a work sheet. The slider starts at 10 words."
axes: { x: "Words in the letter", y: "Words written in total" }
curves:
  - { id: fresh, label: "Fresh paper each time (1 + 2 + ... + n)", f: { poly: [0, 0.5, 0.5] } }
  - { id: sheet, label: "Work sheet (n)", f: { poly: [0, 1, 0] } }
scale: { x: [0, 1000] }
vars: { x: 10 }
sliders:
  - { id: words, label: "Words in the letter", var: x, min: 10, max: 1000, step: 10 }
actions:
  - id: count-fresh
    label: "Count the fresh paper way"
    do:
      - say: "With {x} words, the fresh paper way writes {fresh.at} words in total."
  - id: count-sheet
    label: "Count the work sheet way"
    do:
      - say: "With {x} words, the work sheet way writes {sheet.at} words in total."
script: ["words=100", count-fresh, count-sheet]
try:
  - text: "Slide the words to 100 and press both count buttons. Compare the two totals."
    done: ["words=100", count-fresh, count-sheet]
  - text: "Slide the words to 1000 and press both count buttons again. Look at how far apart the two lines are now."
    done: ["words=1000", count-fresh, count-sheet]
  - text: "Slide the words to 500 and count the fresh paper way. Then slide to 1000 and count again. Compare the two totals."
    done: ["words=500", count-fresh, "words=1000", count-fresh]
```

**What you should notice:** The work sheet total grows in step with the words, and the fresh paper total grows much faster, so doubling the words roughly quadruples it.

## Build the logic

### Notice

A letter has 5 words. You add them one at a time, always on fresh paper.

| Adding word number | Old words copied | New word written | Words written this turn |
|---|---|---|---|
| 1 | 0 | 1 | 1 |
| 2 | 1 | 1 | 2 |
| 3 | 2 | 1 | 3 |
| 4 | 3 | 1 | 4 |
| 5 | 4 | 1 | 5 |

The five turns write 1 + 2 + 3 + 4 + 5, which is 15 words. The finished letter has only 5.

### Guess

The letter has 100 words and you add word number 101 on fresh paper. How many words get written this turn?

- A. 1, only the new word.
- B. 101, the 100 old words and the new one.
- C. 10,100, because it is 100 times 101.

Idea A forgets the copy. Idea C multiplies, but the copies only add to the new word. Idea B is the answer.

Now the whole job. You build a 100-word letter one word at a time on fresh paper. About how many words are written in total?

- A. About 100, one for each word.
- B. About 200, two for each word.
- C. About 5,000, because every turn is longer than the one before.

Idea A counts only the new words. Idea B has no reason behind it. Idea C follows the table: the turns write 1, 2, 3 and so on up to 100, and that adds up to 5,050.

### Build

1. Adding word number k means copying the k minus 1 old words, then writing the new one. That is k words written.
2. Add up every turn for n words: 1 + 2 + 3 + and so on up to n.
3. Pair the first turn with the last, the second with the one before the last, and so on. Each pair adds up to n + 1, and there are half of n pairs. So the total is n times (n + 1), divided by 2. This is general knowledge (plain arithmetic), not a Kotlin fact.
4. For 5 words that is 15. For 10 words it is 55. For 100 words it is 5,050. For 1,000 words it is 500,500.
5. On a work sheet each word is written once, so the total is n.
6. Ten times more words means about 100 times more writing on fresh paper, and 10 times more on the work sheet.

The copy is a hidden second loop, like the one in the earlier lesson on reading complexity in code.

### Name it

Text in Kotlin behaves like the letter that cannot change. A **String** is **immutable**, which means its contents never change after it is made. The Kotlin documentation says any operation that appears to change a String creates a new one, and that `+` makes a new String each time. It adds that this may not be good in loops, and suggests `buildString()` or `StringBuilder`.

The line `result += word` looks like adding to the end. The Kotlin page on operators says `a += b` becomes `a = a + b` when there is no `plusAssign` (a function named plusAssign, which String does not have). So `result += word` makes a new String every turn, and the old one is dropped.

A **StringBuilder** is a mutable (changeable) sequence of characters. It collects all the pieces in one **buffer**, which is one place in memory where the pieces are written one after another. The function **buildString** makes a StringBuilder for you, runs your block, and gives back the finished String. The Kotlin documentation says both produce only one String at the end.

```kotlin
fun main() {
    val words = listOf("red", "green", "blue", "pink", "gray")

    var joined = ""
    for (word in words) {
        joined += "$word "
    }

    val builder = StringBuilder()
    for (word in words) {
        builder.append(word).append(' ')
    }

    val built = buildString {
        for (word in words) {
            append(word).append(' ')
        }
    }

    println(joined == builder.toString())
    println(joined == built)
    println(joined.trim())
}
```

This prints `true`, then `true`, then `red green blue pink gray`. All three ways make the same text. Only the work behind them is different.

## Predict the output

```challenge
type: predict
id: fresh-paper-five-words
verified: kotlin-2.4.21
code: |
  fun main() {
      var written = 0
      for (turn in 1..5) {
          val copied = turn - 1
          written += copied + 1
      }
      println(written)
  }
options:
  - id: a
    text: "5"
    why: "You may have counted one word for each turn. Each turn also copies the old words, so the later turns write more."
  - id: b
    text: "15"
    correct: true
    why: "Turn k copies k minus 1 old words and writes 1 new one. The turns write 1, 2, 3, 4 and 5 words, and that adds up to 15."
  - id: c
    text: "10"
    why: "You may have counted only the copied words: 0 + 1 + 2 + 3 + 4. Each turn also writes its new word, which adds 5 more."
output: |
  15
```

```challenge
type: predict
id: fresh-paper-versus-sheet
verified: kotlin-2.4.21
code: |
  fun main() {
      val n = 10
      var freshPaper = 0
      var workSheet = 0
      for (turn in 1..n) {
          freshPaper += turn
          workSheet += 1
      }
      println("$freshPaper $workSheet")
  }
options:
  - id: a
    text: "55 10"
    correct: true
    why: "Fresh paper writes 1 + 2 + and so on up to 10, which is 55. The work sheet writes each of the 10 words once."
  - id: b
    text: "100 10"
    why: "You may have used n times n. The early turns are short, so the total is about half of that."
  - id: c
    text: "10 10"
    why: "You may have thought both ways write each word once. Only the work sheet does. Fresh paper copies the old words every turn."
output: |
  55 10
```

## Try it

1. **Write.** Copy the program from the second Predict item into the editor.
2. **Run.** You should see `55 10`. The fresh paper way wrote 55 words and the work sheet way wrote 10.
3. **Break.** Change `val n = 10` to `val n = 100`. Run it again. You should see `5050 100`. The work sheet total grew 10 times, and the fresh paper total grew about 90 times.
4. **Fix.** Change `freshPaper += turn` to `freshPaper += 1`. Run it. You should see `100 100`. Now both counts write each word once, the work sheet way.

## Fix the bug

This task has a counter that models the copying, because a slow run does not show up in the printed text. The counter adds the old text length and the new line length for every line.

```challenge
type: fix
id: fix-csv-export
generated: true
verified: kotlin-2.4.21
code: |
  class LogRow(val time: Int, val message: String)

  var written = 0

  fun exportCsv(rows: List<LogRow>): String {
      var csv = "time,message\n"
      written += csv.length
      for (row in rows) {
          val line = "${row.time},${row.message}\n"
          // += makes a new String, so the old text is copied again
          written += csv.length + line.length
          csv += line
      }
      return csv
  }

  fun main() {
      val rows = List(90) { LogRow(it + 10, "ok") }
      val csv = exportCsv(rows)
      println("characters in file: ${csv.length}")
      println("characters written: $written")
  }
hints:
  - "Run it. The file size is right, but the second number is far bigger than the first. Look at the line inside the loop that adds to csv."
  - "A String never changes, so each += writes a new one. Collect the lines in a buildString block, so each line is written once."
expected: |
  characters in file: 553
  characters written: 553
fix: |
  class LogRow(val time: Int, val message: String)

  var written = 0

  fun exportCsv(rows: List<LogRow>): String {
      val header = "time,message\n"
      written += header.length
      return buildString {
          append(header)
          for (row in rows) {
              val line = "${row.time},${row.message}\n"
              written += line.length
              append(line)
          }
      }
  }

  fun main() {
      val rows = List(90) { LogRow(it + 10, "ok") }
      val csv = exportCsv(rows)
      println("characters in file: ${csv.length}")
      println("characters written: $written")
  }
explanation: "The old code copied the whole text so far for every row, about 26,000 characters for a 553-character file. The builder writes each line once."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, this code has no null values in play. For question 4, the line with `+=` runs once for each row, and each run copies all the text so far.

## Tricks to remember

- Text is a letter in dry ink. To change it, copy it all onto fresh paper.
- Fresh paper writes 1 + 2 + and so on up to n. The work sheet writes n.
- A loop that keeps adding pieces to text: reach for `buildString`.
- Two or three pieces in one line: a template (a template puts values inside quotes), like `"$a $b"`, is fine.

## Common mistakes

- **Thinking a String grows in place when you add text.** The `+=` sign looks like adding to the end of the same text, so it feels free. It makes a new String and drops the old one. Use `buildString` when a loop adds the pieces.
- **Using a builder for everything.** "Builders are faster" feels like a rule. For two or three pieces in one line, the Kotlin page says string templates are usually easier to read. Save the builder for loops.
- **Testing with 50 lines.** It finishes at once, so the code feels fine. In the model, fresh paper writes 1,275 words for 50 lines and about 1.25 billion for 50,000 lines. Try the big size before you decide.
- **Making a new builder inside the loop.** A fresh builder for each turn feels tidy. Then each turn starts with an empty builder and the earlier pieces are not in it. Make the builder once, before the loop.

## Where this shows up in Android

Picture a screen that waits while the app builds a long text of log lines to save or share. With 50 lines it is instant. With 50,000 lines it is slow. The Android page on slow rendering says an app must render each frame in under 16 ms to reach 60 frames per second, and that slow frames make the user see stutter, which the page calls jank. My own reasoning: if the text is built while the screen waits to draw, the fresh paper cost can make frames late, because it grows like half of n times n. The page gives no figure for building text, so I give no timing. The part of this lesson that explains the symptom is the copy hidden inside every `+=`.

## Say it in an interview

**Why does `result += char` inside a loop of n steps copy about n times n characters in total?**
A String cannot change, so each `+=` builds a new String and copies the old text into it. The Kotlin documentation says `+` creates a new String each time. The copies are 1, 2, 3 and so on up to n characters, which add up to about half of n times n. Growth like n times n is called quadratic and written O(n²), as in the earlier lesson on reading complexity. The sum is general knowledge; the documentation only says a new String is made and that loops are a poor place for it.

**How does `StringBuilder` avoid that?**
It keeps one mutable buffer of characters and writes new text at the end of it. The Kotlin documentation says it collects all the pieces in a single buffer and produces one String at the end. So each piece is written about once and the total is O(n) (general knowledge). My own reasoning: when its room runs out it moves to a bigger buffer, much like an `ArrayList`, so the cost per piece stays small.

**Why are Strings immutable, and what do you gain?**
Because a String never changes, it can be shared safely between parts of a program. It also works as a key in a map, because its hash code cannot change under you. This is general knowledge. The price is what this lesson is about: every change is a new String.

**Is `+` always bad? When do you use a template, `+` or a builder?**
No. For a few pieces in one expression, a template (values inside quotes), like `"$a $b"`, is usually easier to read, according to the Kotlin documentation, and the cost is small. Use `buildString` or `StringBuilder` when a loop keeps adding pieces, because each turn would copy all the text so far.

## Recap

1. A String never changes, so each `+` or `+=` builds a brand new String.
2. Adding n pieces one at a time copies about half of n times n characters.
3. Use `buildString` for loops, so each piece is written once.

## You can now

- Explain why a String cannot grow in place, using the letter on fresh paper.
- Predict how many words are written when 5 or 10 pieces are joined one at a time.
- Fix an export loop that uses `+=` by switching to `buildString`.

## Practise on these

Say the cost of the slow way and the builder way before you code. The list runs from the gentlest to the hardest.

1. LeetCode 1108, Defanging an IP Address (Easy). Build a changed copy of a string in one pass with a builder. https://leetcode.com/problems/defanging-an-ip-address/
2. LeetCode 151, Reverse Words in a String (Medium). Split the text, then join the words in reverse order without repeated `+=`. https://leetcode.com/problems/reverse-words-in-a-string/
3. LeetCode 394, Decode String (Medium). Build repeated pieces with a builder instead of joining again and again. https://leetcode.com/problems/decode-string/
4. LeetCode 68, Text Justification (Hard). Build each line of exact width in one pass. https://leetcode.com/problems/text-justification/
