---
id: foundation/debugging-mindset/find-a-bug-without-guessing
title: How do I find a bug without guessing?
status: live
difficulty: medium
order: 24
requires: [foundation/debugging-mindset/what-is-a-bug, foundation/programming-thinking/trace-code-by-hand]
summary: Check the middle of your program, then halve the search each time. You find the one bad line in a few checks.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [fuse-halving-search]
playground: false
fruit: false
upnext: false
---


## The question

Your program has 40 lines and prints the wrong answer. You change a number and run it. You change another and run it again. After twenty minutes you are more lost than when you started. Is there a way to find the bad line in a few tries?

## Mental model

Think of a house with 16 rooms in a row, all on one fuse.

- The house is your program, and each room is a stretch of lines.
- The fuse box is where you find out whether something is wrong.
- The fuse that blows is the wrong output.
- One room has a faulty lamp. That lamp is the bad line.
- You are the person with a switch for each room.

You do not replace every lamp in the house. You switch everything off and switch on 8 rooms. If the fuse blows, the faulty lamp is among those 8. If the fuse holds, it is among the other 8. Either way, half the house is cleared in one try.

**Where the analogy stops:** Rooms in a house are independent. Lines in a program are not. They run in order, and each line uses what the lines before it left behind. That is why a check in the middle clears the first half when the value there is right. Also, a house usually has one faulty lamp, but a program can have two bugs. Fix one, run again, and look again.

## See it move

```sim
id: fuse-halving-search
template: lanes
title: "Finding one faulty lamp among sixteen rooms"
intro: "Sixteen rooms share one fuse and one lamp is faulty. The marker sits on room 1. Drag it to room N to switch on rooms 1 to N."
lanes:
  - { id: "rooms", label: "Rooms 1 to 16 on one fuse", kind: "row", slots: 16, index0: false }
items:
  - { id: "probe", label: "Marker", at: "rooms.0" }
vars: { lamp: 11, tries: 0, f1: "holds", f2: "holds", f3: "holds", f4: "holds", f5: "holds", f6: "holds", f7: "holds", f8: "holds", f9: "holds", f10: "holds", f11: "blows", f12: "blows", f13: "blows", f14: "blows", f15: "blows", f16: "blows" }
show:
  - { var: "tries", label: "Tries" }
actions:
  - id: move-lamp
    label: "Move the faulty lamp"
    cases:
      - when: { var: { lamp: 11 } }
        do:
          - set: { lamp: 5, tries: 0, f1: "holds", f2: "holds", f3: "holds", f4: "holds", f5: "blows", f6: "blows", f7: "blows", f8: "blows", f9: "blows", f10: "blows", f11: "blows", f12: "blows", f13: "blows", f14: "blows", f15: "blows", f16: "blows" }
          - move: { probe: "rooms.0" }
          - say: "The faulty lamp moves to another room. The tries counter starts again at 0."
      - when: { var: { lamp: 5 } }
        do:
          - set: { lamp: 11, tries: 0, f1: "holds", f2: "holds", f3: "holds", f4: "holds", f5: "holds", f6: "holds", f7: "holds", f8: "holds", f9: "holds", f10: "holds", f11: "blows", f12: "blows", f13: "blows", f14: "blows", f15: "blows", f16: "blows" }
          - move: { probe: "rooms.0" }
          - say: "The faulty lamp moves to another room. The tries counter starts again at 0."
  - id: reveal
    label: "Open the faulty room"
    cases:
      - when: { var: { lamp: 11 } }
        do:
          - say: "The faulty lamp is in room 11. You used {tries} tries to find it."
      - when: { var: { lamp: 5 } }
        do:
          - say: "The faulty lamp is in room 5. You used {tries} tries to find it."
drops:
  - item: probe
    to: "rooms.0"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 1 are switched on. The fuse {f1}. Tries so far: {tries}."
  - item: probe
    to: "rooms.1"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 2 are switched on. The fuse {f2}. Tries so far: {tries}."
  - item: probe
    to: "rooms.2"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 3 are switched on. The fuse {f3}. Tries so far: {tries}."
  - item: probe
    to: "rooms.3"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 4 are switched on. The fuse {f4}. Tries so far: {tries}."
  - item: probe
    to: "rooms.4"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 5 are switched on. The fuse {f5}. Tries so far: {tries}."
  - item: probe
    to: "rooms.5"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 6 are switched on. The fuse {f6}. Tries so far: {tries}."
  - item: probe
    to: "rooms.6"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 7 are switched on. The fuse {f7}. Tries so far: {tries}."
  - item: probe
    to: "rooms.7"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 8 are switched on. The fuse {f8}. Tries so far: {tries}."
  - item: probe
    to: "rooms.8"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 9 are switched on. The fuse {f9}. Tries so far: {tries}."
  - item: probe
    to: "rooms.9"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 10 are switched on. The fuse {f10}. Tries so far: {tries}."
  - item: probe
    to: "rooms.10"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 11 are switched on. The fuse {f11}. Tries so far: {tries}."
  - item: probe
    to: "rooms.11"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 12 are switched on. The fuse {f12}. Tries so far: {tries}."
  - item: probe
    to: "rooms.12"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 13 are switched on. The fuse {f13}. Tries so far: {tries}."
  - item: probe
    to: "rooms.13"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 14 are switched on. The fuse {f14}. Tries so far: {tries}."
  - item: probe
    to: "rooms.14"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 15 are switched on. The fuse {f15}. Tries so far: {tries}."
  - item: probe
    to: "rooms.15"
    do:
      - add: { tries: 1 }
      - say: "Rooms 1 to 16 are switched on. The fuse {f16}. Tries so far: {tries}."
script: ["probe>rooms.7", "probe>rooms.11", "probe>rooms.9", "probe>rooms.10", "reveal", "move-lamp", "probe>rooms.7", "probe>rooms.3", "probe>rooms.5", "probe>rooms.4", "reveal"]
try:
  - text: "Drag the marker to room 8. That switches on rooms 1 to 8. Does the fuse blow?"
    done: ["probe>rooms.7"]
  - text: "Keep halving what is left: drag the marker to a new room each time. When you know the room, press \"Open the faulty room\" and read the tries."
    done: ["reveal"]
  - text: "Press \"Move the faulty lamp\", then find it again in as few tries as you can and open the room."
    done: ["move-lamp", "reveal"]
```

**What you should notice:** Each try cuts the rooms left in half, so 16 rooms take 4 tries, while testing one room at a time can take up to 15.

## Build the logic

### Notice

The house has 16 rooms and the fuse blows. Here is what halving does to the number of rooms that could hold the lamp.

| Try | Rooms still possible |
|---|---|
| start | 16 |
| 1 | 8 |
| 2 | 4 |
| 3 | 2 |
| 4 | 1 |

After 4 tries you know the room. You never needed to look at all 16.

### Guess

The fuse keeps blowing. How do you find the faulty lamp?

- A. Switch on half the rooms, watch the fuse, and clear the half that is fine.
- B. Replace every lamp in the house and see whether the problem goes away.
- C. Check the rooms one by one, from room 1.

Idea B is the habit of changing things until it works. It may fix the house, but you will not learn which lamp was bad. Idea C will find it, but it can need up to 15 tries. Idea A is the answer.

### Build

1. Write down what you expected and what you got. The gap between them is the bug.
2. Find a point where you are sure the answer is right, such as the start. Find a point where it is wrong, such as the end.
3. Pick a line in the middle. Work out by hand what the value should be there. Tracing by hand does this.
4. Make the program show the real value at that line.
5. If the real value matches, the bug is after the middle. If not, the bug is at or before it. Throw away the half that is fine.
6. Repeat on the half that is left until one line remains.
7. Change that one line. Run again and compare with what you wrote in step 1.

### Name it

Finding and fixing a bug is **debugging**. The check in step 4 is **print debugging**: you add a `println` line that shows a value while the program runs. The Kotlin documentation says `println` prints its arguments and adds a line break. A label in the text tells you which check you are reading. Inside the quotes, `$coins` is a **string template**: Kotlin swaps it for the current value of `coins`.

```kotlin
fun main() {
    var coins = 5
    coins = coins * 2
    println("check: coins is $coins")
    coins = coins - 3
    println(coins)
}
```

This prints `check: coins is 10`, then `7`. If you expected 10 at the check, the first half is clear. The bug, if there is one, sits in the lines below it.

## Predict the output

Work these out on paper. Press "Show the answer" when you have a guess.

```challenge
type: predict
run: false
id: print-inside-the-loop
verified: not-run
code: |
  fun main() {
      // Add up 4 items that cost 5 coins each.
      var total = 0
      for (item in 1..<4) {
          total = total + 5
          println("item $item, total $total")
      }
      println("Total: $total")
  }
options:
  - id: a
    text: "Four item lines, then Total: 20"
    why: "You may have read the comment. The `..<` sign stops one below 4, so the repeated part runs for items 1, 2 and 3 only."
  - id: b
    text: "Three item lines, then Total: 15"
    correct: true
    why: "The print inside the repeated part shows 5, 10 and 15 for items 1 to 3. Item 4 is never visited, so the bug is in the `1..<4` part."
  - id: c
    text: "Four item lines, then Total: 15"
    why: "You may have counted four lines but added three times. Each line comes from one round, so three rounds give three lines."
output: |
  item 1, total 5
  item 2, total 10
  item 3, total 15
  Total: 15
```

```challenge
type: predict
run: false
id: halving-checks
verified: not-run
code: |
  fun main() {
      var rooms = 16
      var tries = 0
      while (rooms > 1) {
          rooms = rooms / 2
          tries = tries + 1
      }
      println(tries)
  }
  // rooms: how many rooms could still hold the faulty lamp.
options:
  - id: a
    text: "16"
    why: "You may have counted one try per room. Each try here halves the rooms, so far fewer tries are needed."
  - id: b
    text: "8"
    why: "You may have stopped after the first halving. The repeated part keeps going until one room is left."
  - id: c
    text: "4"
    correct: true
    why: "Rooms go 16, 8, 4, 2, 1. That is four halvings, so four tries find the lamp."
output: "4"
```

```challenge
type: predict
run: false
id: middle-check-clears-half
verified: not-run
code: |
  // Goal: the program should end with 2.5.
  fun main() {
      var value = 2
      value = value * 3
      value = value + 4
      println("middle: $value")
      value = value - 5
      value = value / 2
      println("end: $value")
  }
options:
  - id: a
    text: "middle: 10, then end: 2"
    correct: true
    why: "The middle check shows 10, which is right, so the first half is clear. The last line divides whole numbers, so 5 divided by 2 gives 2. The bug is in that line."
  - id: b
    text: "middle: 10, then end: 2.5"
    why: "You may have read the goal. Dividing a whole number by a whole number drops the leftover, so the end is 2."
  - id: c
    text: "middle: 6, then end: 0"
    why: "You may have shown the value before the plus 4 line. The middle check sits after it, so it shows 10."
output: |
  middle: 10
  end: 2
```

## Tricks to remember

- Halve, do not hunt.
- Know what you expect before you look.
- Check the middle, not the end. The end only tells you what you already know.
- Change one thing, then run.

## Common mistakes

- **Changing several things at once.** It feels fast. If the answer changes, you cannot tell which change did it, and you may have added a new bug. Change one thing, then run.
- **Picking the bad line by feel.** A line that looks odd feels guilty. Show a value to check it. A line that looks fine can be the bad one.
- **Checking only at the end.** The end is where you already know the answer is wrong. Put the check in the middle, where it clears half the lines.
- **Leaving the prints in.** They helped, so they feel harmless. Remove them when the bug is fixed, as the next section explains.

## Where this shows up in Android

A list screen shows 9 rows when the data has 10 items. You do not guess and change numbers. You check a value in the middle of the code that builds the rows.

The Android documentation gives two ways to check. The Log class writes messages, and the page says log messages "help you understand the execution flow" and "can also tell you what part of your app failed." The debugger can pause your app at a line, and the page says that while it is paused you can examine variables and continue line by line. The page also says to remove debug log messages and stack trace print calls when you are ready to publish your app. Using these to halve the search is our own reasoning.

## Recap

1. Write down what you expected, then compare it with what the program gave.
2. Check the value in the middle, and clear the half that is fine.
3. Repeat until one line is left, then change only that line.

## You can now

- Predict what a print line inside a repeated part shows, and use it to spot a repeat that stops one round short.
- Explain why checking the middle clears half the lines.
- Work out how many halvings it takes to narrow 16 places down to one.
