---
id: foundation/debugging-mindset/check-code-you-did-not-write
title: How do I check code that someone else, or an AI, wrote for me?
status: live
difficulty: medium
order: 25
requires: [foundation/debugging-mindset/find-a-bug-without-guessing]
summary: A clean look and a neat comment do not prove code works. Check one known answer, then odd inputs, before you accept it.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []
playground: false
fruit: false
upnext: false
---

## The question

A teammate, or an AI tool, sends you a tidy block of code. The names are clear, and a neat comment on top says what it does. You are tired, and it looks right. How do you find out whether it really does what the comment says?

## Mental model

Think of a builder you hired to fit out your new flat.

- The flat is your program.
- The builder is whoever wrote the code: a teammate, a website, an AI tool.
- The paint is how the code looks: tidy names, neat comments, even spacing.
- The light switches and taps are what the code does when it runs.
- The order sheet is what you asked for, written in plain words.
- You are the owner. You sign off on the work, and the flat is yours afterwards.

A fresh coat of paint tells you nothing about the taps. So you turn on one tap whose result you know. Then you try the odd ones: the tap at the far end of the line, and the switch nobody uses.

**Where the analogy stops:** A builder's work is hidden inside the walls, but code can be read line by line. Also, testing a tap in a real flat costs water and time, while running code costs almost nothing. And a builder has a name and a reputation, while code looks the same whether an expert, a beginner or a tool wrote it.

## Build the logic

### Notice

The note on the door says "All tested, everything works." The paint is perfect. You turn on the first tap, and water comes out. You turn on the second tap, and water comes out. The third tap sits at the far end of the pipe. You turn it on, and nothing comes out.

### Guess

The note says "All tested, everything works", and the paint is perfect. What do you do before you sign?

- A. Sign. Work this tidy must have been checked.
- B. Decide what each tap and switch must do. Try one where you know the result, then the odd ones, such as the far end of the line.
- C. Test only what the note says was tested.

Idea A trusts the paint. Idea C checks only what the builder chose to check. Idea B is the answer.

### Build

1. Write one sentence about what the code must do. Do it before you read the code. This is your order sheet.
2. Pick a small example whose answer you can work out by hand.
3. Work through the code one line at a time, writing down each value. Compare your answer with what the code gives.
4. Try the odd examples: a row with no items in it, a row with one item, the biggest or smallest value, a negative number, and the answer sitting first, in the middle and last.
5. If the code and your hand answer disagree, trust your hand answer first. Find the line where they split, using the halving idea: check the middle and clear the half that is fine.
6. Ask yourself: could I explain every line to someone else? If not, do not sign yet.
7. Sign off, or send the work back with the exact example that failed.

### Name it

When someone other than the writer reads and checks code before it is accepted, that is a **code review**. Working through code by hand, one line at a time, is **tracing**. An example at the edge of what the code was built for, such as nothing at all, one item, or the biggest value, is an **edge case**.

The code below is a **function**: a named set of steps that takes values in and hands one back. This one takes a **list**, which is a row of items kept in order. `List<Int>` is the **type** for a list of whole numbers. A type is the kind of value. `numbers[0]` is the first item of the list.

```kotlin
// Returns the highest number in the list.
fun highest(numbers: List<Int>): Int {
    var best = numbers[0]
    for (n in numbers) {
        if (n > best) {
            best = n
        }
    }
    return best
}

fun main() {
    println(highest(listOf(3, 9, 4)))
    println(highest(listOf(7)))
    println(highest(listOf(-5, -2, -8)))
}
```

This prints `9`, then `7`, then `-2`. The third check matters: the code starts from the first item and not from 0, so a list of negative numbers still works. All three checks pass. That is still not proof, because there may be an example you did not try.

Checking is not an insult to the writer. People and tools both slip, and the owner is the one who signs.
## Predict the output

Work these out on paper. Each function has a neat comment on top. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: largest-of-three
verified: not-run
code: |
  // Returns the largest of three numbers.
  fun largest(a: Int, b: Int, c: Int): Int {
      if (a > b) { return a }
      if (b > c) { return b }
      return c
  }

  fun main() {
      println(largest(5, 2, 9))
  }
options:
  - id: a
    text: "9"
    why: "You may have answered the question in the comment instead of following the lines. The first check hands back a as soon as it beats b, and never looks at c."
  - id: b
    text: "5"
    correct: true
    why: "5 is bigger than 2, so the first check hands back 5 straight away. The tap at the far end, c, is never tested."
  - id: c
    text: "2"
    why: "You may have picked the middle number. The function never hands back b here. The first check is a > b, and it hands back a."
output: "5"
```

```challenge
type: predict
run: false
id: total-of-prices
verified: not-run
code: |
  // Adds up every price in the list.
  fun total(prices: List<Int>): Int {
      var sum = 0
      for (i in 1..<prices.size) {
          sum = sum + prices[i]
      }
      return sum
  }

  fun main() {
      println(total(listOf(5, 2, 4)))
  }
options:
  - id: a
    text: "11"
    why: "You may have trusted the comment. The counting starts at position 1, and the first price sits at position 0, so the 5 is never added."
  - id: b
    text: "4"
    why: "You may have kept only the last price. The sum keeps growing with each round, so the 2 and the 4 are both added."
  - id: c
    text: "6"
    correct: true
    why: "Positions 1 and 2 hold the 2 and the 4, and 2 plus 4 is 6. The first tap, the 5, was never turned on."
output: "6"
```

```challenge
type: predict
run: false
id: highest-of-nothing
verified: not-run
code: |
  // Returns the highest number in the list.
  fun highest(numbers: List<Int>): Int {
      var best = numbers[0]
      for (n in numbers) { if (n > best) { best = n } }
      return best
  }

  fun main() {
      println("Start")
      println(highest(listOf<Int>()))
      println("End")
  }
  // listOf<Int>() makes a list with no items in it.
options:
  - id: a
    text: "Start, then the program stops with an error"
    correct: true
    why: "The list has no items, yet the first line of the function asks for the item at position 0. That far tap fails before End can print."
  - id: b
    text: "Start, 0, End"
    why: "You may have expected a list with nothing in it to give 0. The function never makes up an answer. It asks for item 0, and there is none."
  - id: c
    text: "Nothing, because the compiler refuses the program"
    why: "You may have expected the compiler to catch this. The program follows every rule, so it starts. The problem only shows up when the line runs."
output: "Start"
```

## Tricks to remember

- Paint is not plumbing. A tidy look tells you nothing about what runs.
- One known answer first, then the odd ones.
- Always test the far tap: the first item, the last item, nothing, one, and a negative number.
- If you cannot explain a line, do not sign.

## Common mistakes

- **Trusting tidy code with a neat comment.** Care in the paint feels like care in the work. The comment is only words, and the computer never reads it. Work out one answer by hand and trace the code with it.
- **Testing only the example the writer gave.** It feels fair to start there. It is the one case the writer already knew worked. Pick your own example, and include an odd one.
- **Stopping after one good run.** One correct answer feels like proof. It shows that one example works. Try the empty and one-item cases too.
- **Sending it back with "it feels wrong".** The feeling is real, but nobody can act on it. Name the example that failed, what you expected and what you got.

## Where this shows up in Android

A shopping app crashes the moment someone opens an empty cart. The code that finds the cheapest item looks tidy. It asks for the first item of a list, and the list is empty. This is the third Predict item in a shop. The example is our own reasoning, not from the documentation.

Android Studio is the program most Android developers write their apps in. It has an AI helper called Gemini, and the Android Studio documentation has a page of questions and answers about it. The page says: "Because AI is an evolving technology, it can generate output that sounds plausible but is factually incorrect. We recommend that you validate all output from Gemini before you use it." About code, it tells you to test all code carefully for errors and bugs before relying on it. The same steps work for code from a teammate.

## Recap

1. A tidy look and a neat comment do not show that code is right.
2. Write what the code must do, then check one example you can answer by hand.
3. Then try edge cases such as an empty list, one item and negative numbers.

## You can now

- Predict what a tidy function prints when its comment and its lines disagree.
- Work out by hand what a short function gives for a small example, and compare it with its comment.
- Spot the edge case, such as an empty list, that makes a short function stop.
