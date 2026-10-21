---
id: kotlin/control-flow/break-and-continue
title: How do I stop a loop early or skip one turn?
status: live
difficulty: easy
order: 11
requires: [kotlin/control-flow/ranges-and-for-loops]
summary: Break leaves a loop at once. Continue skips the rest of one turn and moves to the next.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [letter-stack-gates]
playground: false
fruit: false
upnext: false
---

## The question

A search screen goes through a list of 100 names to find one person. It finds her at name number 3 and keeps checking the other 97. On another screen, one name in the list is blank, and that name must be skipped while the rest are shown. How do you tell a job that goes through a list to stop early, and how do you tell it to skip only one turn?

## Mental model

Think of reading a stack of letters at a desk.

- The stack is the list of things to go through.
- You are the job that goes through them. You take the top letter, read it, and take the next.
- Each letter you finish goes into the done tray.
- One rule card says "junk mail: put it aside unread and take the next letter".
- Another rule card says "a STOP letter: throw away this letter and the whole rest of the stack, and finish".
- Whatever you planned for after the stack, such as making tea, still happens.

**Where the analogy stops:** At a desk you can see the whole stack, but the job sees one letter at a time. Also, if your stack sits inside a bigger box of stacks, the STOP card only ends the stack in your hands, not the whole box.

## See it move

```sim
id: letter-stack-gates
template: lanes
title: "Reading a stack of letters with two rule cards"
intro: "Six letters sit in a stack, letter 1 on top. The done tray and the unread pile are empty and no rule card is in play."
lanes:
  - { id: "stack", label: "Stack of letters (top letter is read first)", kind: "stack", slots: 6, index0: false }
  - { id: "tray", label: "Done tray", kind: "row", slots: 6, index0: false }
  - { id: "unread", label: "Unread pile (set aside or thrown away)", kind: "row", slots: 6, index0: false }
items:
  - { id: "l1", label: "Letter 1", at: "stack.5" }
  - { id: "l2", label: "Letter 2", at: "stack.4" }
  - { id: "l3", label: "Letter 3", at: "stack.3" }
  - { id: "l4", label: "Letter 4", at: "stack.2" }
  - { id: "l5", label: "Letter 5", at: "stack.1" }
  - { id: "l6", label: "Letter 6", at: "stack.0" }
vars: { next: 0, read: 0, skipped: 0, junk: "off", stop: "off" }
show:
  - { var: "read", label: "Letters in the done tray" }
  - { var: "skipped", label: "Letters left unread" }
toggles:
  - { id: "junk", label: "Junk mail rule for letter 3", var: "junk" }
  - { id: "stop", label: "STOP letter rule for letter 5", var: "stop" }
actions:
  - id: take
    label: "Take the next letter"
    cases:
      - when: { var: { next: 0 } }
        do:
          - push: { tray: "l1" }
          - add: { read: 1, next: 1 }
          - say: "Letter 1 is read and goes into the done tray."
      - when: { var: { next: 1 } }
        do:
          - push: { tray: "l2" }
          - add: { read: 1, next: 1 }
          - say: "Letter 2 is read and goes into the done tray."
      - when: { var: { next: 2, junk: "on" } }
        do:
          - push: { unread: "l3" }
          - add: { skipped: 1, next: 1 }
          - say: "Letter 3 is junk mail. You put it aside unread and take the next letter."
      - when: { var: { next: 2 } }
        do:
          - push: { tray: "l3" }
          - add: { read: 1, next: 1 }
          - say: "Letter 3 is read and goes into the done tray."
      - when: { var: { next: 3 } }
        do:
          - push: { tray: "l4" }
          - add: { read: 1, next: 1 }
          - say: "Letter 4 is read and goes into the done tray."
      - when: { var: { next: 4, stop: "on" } }
        do:
          - push: { unread: "l5" }
          - push: { unread: "l6" }
          - add: { skipped: 2 }
          - set: { next: 6 }
          - say: "Letter 5 is a STOP letter. You throw it away with the rest of the stack and leave the stack. Then you make tea."
      - when: { var: { next: 4 } }
        do:
          - push: { tray: "l5" }
          - add: { read: 1, next: 1 }
          - say: "Letter 5 is read and goes into the done tray."
      - when: { var: { next: 5 } }
        do:
          - push: { tray: "l6" }
          - add: { read: 1, next: 1 }
          - say: "Letter 6 is read and goes into the done tray. The stack is empty."
      - when: { var: { next: 6 } }
        do:
          - say: "The stack is finished. Now you make tea."
script: ["take", "take", "take", "take", "take", "take", "junk=on", "stop=on"]
try:
  - text: "Press \"Take the next letter\" six times with no rule cards. Count the letters in the done tray."
    done: ["take", "take", "take", "take", "take", "take"]
  - text: "Tick the junk mail rule card for letter 3. The sim replays your six turns with the rule."
    done: ["junk=on"]
  - text: "Tick the STOP letter rule card for letter 5 as well. Count the letters in the done tray and in the unread pile."
    done: ["stop=on"]
```

**What you should notice:** The junk rule leaves one letter unread and carries on, while the STOP rule leaves all the remaining letters unread.

## Build the logic

### Notice

The stack holds letters numbered 1 to 6. Letter 3 is junk mail. Letter 5 is a STOP letter. You work through the stack.

- Letters 1 and 2 are read and go into the tray.
- Letter 3 is junk. You put it aside and take letter 4.
- Letter 4 is read and goes into the tray.
- Letter 5 is a STOP letter. You throw it away with letter 6, and the stack is finished.

After that you go and make tea.

### Guess

Letter 3 is junk mail. What should you do next? Here are three ideas.

- A. Stop the whole job, because something unusual turned up.
- B. Put letter 3 aside and take letter 4 as usual.
- C. Read letter 3 anyway and throw it away afterwards.

Idea A feels right, because skipping sounds like a kind of stopping. But the junk card only covers one letter. The rest of the stack still needs reading. Idea C reads the letter the rule said to skip. Idea B is the answer.

### Build

1. Take the next letter from the stack.
2. Look at the rule cards before you read it.
3. If the junk card fits, put the letter aside and go back to step 1. Nothing further down your plan for this letter happens.
4. If the STOP card fits, throw away this letter and the rest of the stack. Leave the stack.
5. If no card fits, read the letter and put it in the tray. Then go back to step 1.
6. When the stack runs out, the job is over. After the stack, your plan carries on.

### Name it

A job that does the same steps for each item in turn is a **loop**. One pass through the steps for a single letter is a **round**, which programmers call an **iteration**. The Kotlin documentation says **`break`** terminates the nearest enclosing loop. It also says **`continue`** proceeds to the next step of the nearest enclosing loop. With only one loop in the program, the nearest enclosing loop is that loop.

The sign `==` asks "is this equal to that?". It gives yes or no, so it works as the question inside an `if`, which runs its steps only when the answer is yes.

| Desk | In Kotlin |
|---|---|
| The stack | The range, such as `1..6` |
| You, taking one letter at a time | The `for` loop |
| The letter in your hand | The name `letter`, which holds the current number |
| The junk mail card | `continue` |
| The STOP letter card | `break` |
| Making tea after the stack | The code after the loop |

This program works through letters 1 to 6.

```kotlin
fun main() {
    for (letter in 1..6) {
        if (letter == 3) {
            continue
        }
        if (letter == 5) {
            break
        }
        println(letter)
    }
    println("Done")
}
```

It prints `1`, `2`, `4` and `Done`. Letter 3 hits `continue`, so the `println` below it is skipped for that round and the loop moves to letter 4. Letter 5 hits `break`, so the loop ends. The line `println("Done")` is after the loop, so it still runs.

## Predict the output

```challenge
type: predict
run: false
id: skip-two-stop-five
verified: not-run
code: |
  fun main() {
      for (n in 1..8) {
          if (n == 2) {
              continue
          }
          if (n == 5) {
              break
          }
          println(n)
      }
  }
options:
  - id: a
    text: "1, 2, 3, 4"
    why: "You may have expected the junk card to do nothing. Letter 2 is put aside unread, so it never reaches the tray."
  - id: b
    text: "1, 3, 4, 6, 7, 8"
    why: "You may have treated the STOP card as another junk card. At letter 5 the whole rest of the stack is thrown away."
  - id: c
    text: "1, 3, 4"
    correct: true
    why: "Letter 2 is put aside and the job carries on. At letter 5 the job ends, so letters 5 to 8 never reach the tray."
output: |
  1
  3
  4
```

```challenge
type: predict
run: false
id: stop-where-skip-meant
verified: not-run
code: |
  fun main() {
      // Goal: print 1 to 6 but leave out 3.
      for (n in 1..6) {
          if (n == 3) {
              break
          }
          println(n)
      }
  }
options:
  - id: a
    text: "1, 2"
    correct: true
    why: "At letter 3 the STOP card fits, so the job ends and the rest of the stack is thrown away. The goal needed the junk card, which is continue."
  - id: b
    text: "1, 2, 4, 5, 6"
    why: "You may have read the goal instead of the code. That is what the junk card would give, but this code uses the STOP card."
  - id: c
    text: "1, 2, 3, 4, 5, 6"
    why: "You may have expected the rule card to be ignored. It fits letter 3, so it is used."
output: |
  1
  2
```

```challenge
type: predict
run: false
id: stop-then-done
verified: not-run
code: |
  fun main() {
      for (n in 1..5) {
          println(n)
          if (n == 2) {
              break
          }
      }
      println("Done")
  }
options:
  - id: a
    text: "1, 2"
    why: "You may have expected the STOP card to end the whole program. It ends only the loop, and the plan for after the stack still happens."
  - id: b
    text: "1, 2, Done"
    correct: true
    why: "Letter 2 is read, then the STOP card fits and the stack is thrown away. Making tea, which is the last line, still happens."
  - id: c
    text: "1, 2, 3, 4, 5, Done"
    why: "You may have thought the card applies only to its own letter. It ends the whole stack."
output: |
  1
  2
  Done
```

## Tricks to remember

- `continue`: skip this letter, keep the stack.
- `break`: leave the stack, keep the day.
- Both look at one letter, but only `break` throws the rest away.
- Whatever is written after the loop still runs after a `break`.

## Common mistakes

- **Using `break` when you meant `continue`.** Both say "do not process this one", so they feel alike. `break` also throws away every letter after it. Use `continue` when only this letter should be skipped.
- **Thinking `continue` stops the loop.** In daily talk, skipping and stopping both mean "not this one", so they feel alike. `continue` skips the rest of one round and starts the next. Count the rounds on paper.
- **Expecting `break` to stop the whole program.** The STOP card feels final. It ends only the loop it sits in. The lines after the loop still run.

## Where this shows up in Android

No Android page is cited in this section. This part is our own reasoning.

Picture a contacts screen that shows names from a list. One contact has a blank name. If the code uses `break` on a blank name, every row after it is missing, and nothing reports an error. If it uses `continue`, only the blank row is left out. That is the second Predict item, in a screen.

## Say it in an interview

**What is the difference between `break` and `continue`?**
The documentation says `break` terminates the nearest enclosing loop, and `continue` proceeds to the next step of the nearest enclosing loop. So `continue` skips one iteration and `break` ends all of them. My own reasoning: using the wrong one is a quiet bug, because nothing reports an error.

**Does the code after the loop still run after a `break`?**
Yes. `break` ends only the loop, not the function or the program. The line after the loop runs as normal, as in the `Done` example. My own reasoning: this is why you can use `break` to leave early and still finish the job.

**In nested loops, which loop does `break` end?**
The nearest enclosing one. The documentation says a `break` with a label, such as `break@loop`, jumps to the point right after the loop marked with that label. That lets you leave an outer loop from an inner one. The trade-off is code that is harder to follow, so use it sparingly (my own reasoning).

## Recap

1. `continue` skips the rest of one round and moves on to the next.
2. `break` ends the nearest loop at once, and the code after the loop still runs.
3. Using `break` where you meant `continue` throws away every later item.

## You can now

- Predict the numbers printed by a loop that uses both continue and break.
- Spot a break that was meant to be a continue.
- Predict that code after a loop still runs after a break.
