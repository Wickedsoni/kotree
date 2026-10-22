---
id: dsa/stacks/matching-brackets
title: How does a stack check that every bracket has its partner?
status: live
difficulty: medium
order: 14
requires: [dsa/stacks/what-is-a-stack]
summary: Counting open and close brackets misses a clash like ([)]. See how a stack of open brackets catches it.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [nested-boxes-check]
playground: true
fruit: false
upnext: false
---

## The question

A friend sends you a line of text with four brackets: `([)]`. He says every bracket has its partner. You count two openers and two closers, so counting agrees with him. Yet a program that reads brackets refuses the line. How can a program tell, and what did counting miss?

## Mental model

Think of boxes nested inside each other, like gift boxes.

- A big box has a round lid. A smaller box that fits inside it has a square lid.
- Opening a box means you are now inside it.
- The boxes you are inside, in order, are the open boxes. The one you opened last is the innermost.
- Closing a box means putting its lid on. You can only close the box you are inside right now, the innermost one.
- A lid of the wrong shape does not fit.

So the box you opened last must be the first one you close. A line is balanced when every box that was opened gets closed in the right order.

**Where the analogy stops:** Real boxes cannot be closed in the wrong order, because the lid will not fit. A line of text can hold any marks in any order, so the checker must refuse the wrong order itself. Also, a real box holds things. In text, other characters may sit between the marks, and the checker steps over them.

## See it move

```sim
id: nested-boxes-check
template: lanes
title: "Checking ( [ ) ] with a stack of open boxes"
intro: "Four marks wait to be read from left to right: a round open mark, a square open mark, a round close mark and a square close mark. No box is open yet."
lanes:
  - { id: marks, label: "Marks still to read", kind: row, slots: 4, index0: false }
  - { id: boxes, label: "Open boxes (innermost on top)", kind: stack, slots: 4 }
  - { id: closed, label: "Boxes already closed", kind: row, slots: 4, index0: false }
items:
  - { id: c1, label: "(", at: "marks.0" }
  - { id: c2, label: "[", at: "marks.1" }
  - { id: c3, label: ")", at: "marks.2" }
  - { id: c4, label: "]", at: "marks.3" }
vars: { pos: 0, mode: bad, opens: 0, closes: 0, verdict: "not checked yet" }
show:
  - { var: verdict, label: "Verdict" }
  - { var: opens, label: "Open marks read" }
  - { var: closes, label: "Close marks read" }
actions:
  - id: read-next
    label: "Read the next mark"
    cases:
      - when: { var: { pos: 0 } }
        do:
          - push: { boxes: c1 }
          - add: { opens: 1, pos: 1 }
          - say: "The ( opens a round box, and you are now inside it, so it goes on the stack of open boxes."
      - when: { var: { pos: 1 } }
        do:
          - push: { boxes: c2 }
          - add: { opens: 1, pos: 1 }
          - say: "The [ opens a square box inside the round one, so it goes on top of the stack."
      - when: { var: { pos: 2, mode: bad } }
        do:
          - mark: { c3: bad, c2: picked }
          - add: { closes: 1 }
          - set: { pos: 9, verdict: "not balanced" }
          - say: "The ) wants to close a round box, but the box on top is square, so the check stops: not balanced."
      - when: { var: { pos: 2, mode: good } }
        do:
          - move: { c2: "closed.0", c3: "closed.1" }
          - add: { closes: 1, pos: 1 }
          - say: "The ] closes a square box, and the box on top is square, so they fit and that box is closed."
      - when: { var: { pos: 3, mode: good } }
        do:
          - move: { c1: "closed.2", c4: "closed.3" }
          - add: { closes: 1 }
          - set: { pos: 9, verdict: "balanced" }
          - say: "The ) closes a round box, and the box on top is round, so they fit and, with no box left open, the line is balanced."
    refuse: "The check has finished. Press \"Try the other string\" to start again."
  - id: count-only
    label: "Count the marks instead"
    do:
      - say: "Counting finds 2 open marks and 2 close marks, so it says balanced, though it cannot see that the square box was still open when the round lid came down."
  - id: swap-string
    label: "Try the other string"
    cases:
      - when: { var: { mode: bad } }
        do:
          - move: { c1: "marks.0", c2: "marks.1", c3: "marks.2", c4: "marks.3" }
          - label: { c3: "]", c4: ")" }
          - mark: { c1: plain, c2: plain, c3: plain, c4: plain }
          - set: { pos: 0, mode: good, opens: 0, closes: 0, verdict: "not checked yet" }
          - say: "The marks are back in a row and now read ( [ ] ), so read them one at a time."
      - when: { var: { mode: good } }
        do:
          - move: { c1: "marks.0", c2: "marks.1", c3: "marks.2", c4: "marks.3" }
          - label: { c3: ")", c4: "]" }
          - mark: { c1: plain, c2: plain, c3: plain, c4: plain }
          - set: { pos: 0, mode: bad, opens: 0, closes: 0, verdict: "not checked yet" }
          - say: "The marks are back in a row and now read ( [ ) ], so read them one at a time."
script: [read-next, read-next, read-next, count-only, swap-string, read-next, read-next, read-next, read-next]
try:
  - text: "Press \"Read the next mark\" three times on the line ( [ ) ]. Watch what the third mark meets."
    done: [read-next, read-next, read-next]
  - text: "Press \"Count the marks instead\" and compare its answer with the stack's answer."
    done: [count-only]
  - text: "Press \"Try the other string\", then read all four marks of ( [ ] ) one at a time."
    done: [swap-string, read-next, read-next, read-next, read-next]
```

**What you should notice:** On `([)]` the stack stops at the third mark because the open box on top is square, while counting sees two openers and two closers and says nothing is wrong.

## Build the logic

### Notice

Take two lines: `([])` and `([)]`. Both have two openers and two closers. In the first, the round box is opened, then the square box inside it, then the square box is closed, then the round box. In the second, the round box is opened, then the square box, then the round lid comes down while the square box is still open.

### Guess

How can a program catch `([)]` and still accept `([])`?

- A. Count the openers and the closers and compare.
- B. Check that the first mark and the last mark are a matching pair.
- C. Remember the open boxes, newest on top, and make every closer fit the newest one.

Idea A gives 2 and 2 for both lines, so it cannot tell them apart. Idea B looks at only the outer pair. It accepts `(([)])`, where the clash sits inside, and it rejects the fine line `()[]`. Idea C is the answer, and it works for every line.

### Build

1. Start with no box open.
2. Read the line from left to right, one mark at a time.
3. If the mark opens a box, note it as the newest open box.
4. If the mark closes a box, look at the newest open box. If no box is open, or the shape is different, the line is not balanced. Stop.
5. If the shape fits, that box is closed. Take it off the open boxes.
6. At the end of the line, if any box is still open, the line is not balanced. Otherwise it is.

Two edge cases come from steps 4 and 6. A closer with no box open, as in `)(`, is refused at step 4. Openers that never close, as in `(((`, are caught at step 6.

### Name it

A line is **balanced** when every opening bracket has a closing bracket of the same kind, and the brackets close in the reverse order they were opened. The pile of open boxes is a **stack**, a pile where only the newest item can be reached, so the last one in is the first one out. To **push** is to add an item on top. To **pop** is to take the top item off and hand it back. An opener is pushed, and a closer pops the top and compares.

In Kotlin the stack is an `ArrayDeque<Char>`. The code uses `addLast` to push and `removeLastOrNull()` to pop. The Kotlin reference says `removeLastOrNull()` returns `null` when the deque is empty, so a closer with no open box gives `null`, and `null` never equals a bracket. The `when` picks a branch by the mark. A branch such as `'(', '[', '{'` lists several marks.

```kotlin
fun isBalanced(text: String): Boolean {
    val opened = ArrayDeque<Char>()
    for (ch in text) {
        when (ch) {
            '(', '[', '{' -> opened.addLast(ch)
            ')' -> if (opened.removeLastOrNull() != '(') return false
            ']' -> if (opened.removeLastOrNull() != '[') return false
            '}' -> if (opened.removeLastOrNull() != '{') return false
        }
    }
    return opened.isEmpty()
}

fun main() {
    for (text in listOf("([])", "([)]", "(((", ")(", "", "{[()]}")) {
        println("\"$text\" -> ${isBalanced(text)}")
    }
}
```

This prints six lines: `"([])" -> true`, `"([)]" -> false`, `"(((" -> false`, `")(" -> false`, `"" -> true` and `"{[()]}" -> true`. The empty line is balanced because no box is ever left open.

## Predict the output

```challenge
type: predict
id: top-when-closer-arrives
verified: kotlin-2.4.21
code: |
  fun main() {
      val stack = ArrayDeque<Char>()
      for (ch in "([)]") {
          if (ch == ')') {
              println("Top is ${stack.last()}")
              break
          }
          stack.addLast(ch)
      }
  }
options:
  - id: a
    text: "Top is ["
    correct: true
    why: "The round box was opened first and the square box after it. The innermost open box is square, so the round lid meets a square box."
  - id: b
    text: "Top is ("
    why: "You may have expected the round lid to meet its own round box. The square box was opened after it, so the square box is on top."
  - id: c
    text: "Top is )"
    why: "You may have thought the closer is added before you look. The code looks first, and only openers go on the stack."
output: |
  Top is [
```

```challenge
type: predict
id: leftover-opener
verified: kotlin-2.4.21
code: |
  fun main() {
      val stack = ArrayDeque<Char>()
      for (ch in "(()") {
          if (ch == '(') stack.addLast(ch) else stack.removeLast()
      }
      println(stack.size)
  }
options:
  - id: a
    text: "0"
    why: "You may have matched every closer and forgotten that there are two openers and one closer. One round box is still open."
  - id: b
    text: "1"
    correct: true
    why: "Two round boxes are opened and one is closed. One box is still open, so the stack holds 1 mark and the line is not balanced."
  - id: c
    text: "3"
    why: "You may have counted every mark in the line. Only open boxes stay on the stack, and a closer removes one."
output: |
  1
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see six lines. `"([])" -> true`, `"([)]" -> false`, `"(((" -> false`, `")(" -> false`, `"" -> true` and `"{[()]}" -> true`.
3. **Break.** Change `"([])"` in the list to `"([]))"` and change nothing else. Run it again. The first line now reads `"([]))" -> false`. The last closer finds no open box, so the line is refused.
4. **Fix.** Change `"([]))"` back to `"([])"` and run it. The first line is `"([])" -> true` again.

## Fix the bug

```challenge
type: fix
id: fix-counting-checker
generated: true
verified: kotlin-2.4.21
code: |
  fun isBalanced(text: String): Boolean {
      var depth = 0
      for (ch in text) {
          if (ch == '(' || ch == '[') {
              depth++
          }
          if (ch == ')' || ch == ']') {
              depth--
          }
          if (depth < 0) {
              return false
          }
      }
      return depth == 0
  }

  fun main() {
      val samples = listOf("([])", "([)]", "(()", "())(")
      for (sample in samples) {
          println("$sample -> ${isBalanced(sample)}")
      }
  }
hints:
  - "Look at the second line of output: ([)] should be refused. Which line of isBalanced remembers which shape of box is open?"
  - "Counting keeps how many boxes are open but forgets which ones. Keep the open boxes in a stack, and compare each closer with the top."
expected: |
  ([]) -> true
  ([)] -> false
  (() -> false
  ())( -> false
fix: |
  fun isBalanced(text: String): Boolean {
      val opened = ArrayDeque<Char>()
      for (ch in text) {
          when (ch) {
              '(', '[' -> opened.addLast(ch)
              ')' -> if (opened.removeLastOrNull() != '(') return false
              ']' -> if (opened.removeLastOrNull() != '[') return false
          }
      }
      return opened.isEmpty()
  }

  fun main() {
      val samples = listOf("([])", "([)]", "(()", "())(")
      for (sample in samples) {
          println("$sample -> ${isBalanced(sample)}")
      }
  }
explanation: "The counter only knew how many boxes were open, so ([)] looked fine. The stack remembers which box is innermost, so the round lid meets the square box and the line is refused."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, in the fixed code `removeLastOrNull()` gives `null` when no box is open, and the comparison treats that as a mismatch. For question 4, the `for` loop body runs once per character, and `isBalanced` runs once per sample line.

## Tricks to remember

- Last box opened, first box closed. That is the whole rule.
- Counting knows how many boxes are open. The stack knows which one is on top.
- Two checks at the end of every run: no closer found an empty stack, and no opener is left over.
- Task: run `{[(])}` through the six Build steps on paper, and write the stack after each mark.

## Common mistakes

- **Thinking counting is enough.** Equal totals feel like proof of partners. `([)]` has equal totals and still clashes. A stack checks the order, and a count cannot.
- **Forgetting the closer with nothing open.** The code worked for lines that start with an opener, so it feels done. A line like `)(` pops an empty stack. The Kotlin reference says `removeLast()` throws a `NoSuchElementException` on an empty `ArrayDeque`, so use `removeLastOrNull()` and treat `null` as a mismatch.
- **Forgetting to check the stack at the end.** The loop finished without a refusal, so it feels like a pass. `(((` never refuses anything. Return `opened.isEmpty()`, not `true`.
- **Counting each kind separately.** One counter for round marks and one for square marks feels like it adds care. `([)]` still gives zero for both. Only the order catches the clash.

## Where this shows up in Android

A screen can be described in an **XML layout file** (a file that lists the parts of a screen), with **tags** (the marked names in angle brackets) nested inside each other. The Android documentation says a layout file has exactly one root element, and that child elements are added to build a **View hierarchy** (the nested tree of parts on screen), much like nested elements in HTML. The XML specification says the name in a closing tag must match the element type in the opening tag. So a closing tag for the wrong box is not well-formed XML.

Picture a layout file with one closing tag in the wrong place. The tags look complete, because every opening tag has a closing tag. Yet the nesting is broken, and a program reading the file must refuse it. The part of this lesson that explains why is "close the innermost open box first". My own reasoning: a reader of such a file can keep the open tags in a stack, as this lesson does with brackets, but I have not checked how Android's own reader is built.

## Say it in an interview

**Why can you not validate `()`, `[]` and `{}` by counting +1 for open and -1 for close?**
Counting forgets the order and the kinds. The line `([)]` has as many openers as closers, so the count ends at zero, and it is still invalid. Even one counter per kind gives zero for each kind. A stack remembers which opener is innermost, so a closer can be compared with it.

**For one kind of bracket, such as `(` and `)`, can you check a line without a stack?**
Yes, with one number. Add 1 for an opener and subtract 1 for a closer; if the number goes below zero at any point, or is not zero at the end, the line is invalid. With one kind of bracket nothing else needs remembering, so memory stays constant. Several kinds need a stack that grows with the nesting depth (general knowledge).

**Which two edge cases break a simple bracket checker?**
A closer when nothing is open, such as `)(`, and openers left over at the end, such as `(((`. With a Kotlin `ArrayDeque`, `removeLast()` throws a `NoSuchElementException` on an empty deque, so I use `removeLastOrNull()`. At the end I return whether the stack is empty.

**How would you report the exact position of the first mismatch?**
Push the position of each opener. Its character can be read from the text at that position. When a closer does not fit, the position I am reading is the answer, and the popped position tells me which opener it clashed with. This is my own reasoning, and the same trick helps in the Longest Valid Parentheses problem.

## Recap

1. Counting brackets misses clashes like `([)]`, because it forgets the order and the kinds.
2. An opener goes onto a stack, and a closer must fit the top.
3. A line is balanced only if no closer finds an empty stack and no opener is left.

## You can now

- Explain why counting open and close brackets cannot catch `([)]`.
- Predict what is on top of the stack when a closer arrives.
- Fix a bracket checker that only counts so it uses a stack.
- Name the two edge cases: a closer with nothing open, and openers left at the end.

## Practise on these

Run each example on paper first and write the stack after every mark. The list runs from the gentlest to the hardest.

1. LeetCode 20, Valid Parentheses (Easy). Push openers and check that every closer fits the top. https://leetcode.com/problems/valid-parentheses/
2. LeetCode 1614, Maximum Nesting Depth of the Parentheses (Easy). Track how many boxes are open at once to find the deepest nesting. https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/
3. LeetCode 921, Minimum Add to Make Parentheses Valid (Medium). Count the unmatched openers and closers to repair a line. https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/
4. HackerRank, Balanced Brackets (Medium). Handle a closer with nothing open and openers left at the end. https://www.hackerrank.com/challenges/balanced-brackets/problem
5. LeetCode 1249, Minimum Remove to Make Valid Parentheses (Medium). Push positions so you know exactly which brackets to delete. https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/
6. LeetCode 856, Score of Parentheses (Medium). Keep a running score for each open box on the stack. https://leetcode.com/problems/score-of-parentheses/
7. LeetCode 32, Longest Valid Parentheses (Hard). Use a stack of positions to measure the longest balanced stretch. https://leetcode.com/problems/longest-valid-parentheses/
