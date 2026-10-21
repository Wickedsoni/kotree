---
id: daa/analysis/loop-invariants
title: How can I be sure a loop gives the right answer for every input, not just my tests?
status: live
difficulty: medium
order: 3
requires: [daa/analysis/best-worst-average-case]
summary: "A loop invariant is a fact that stays true after every turn of a loop. Check it at the start, each turn and the end."
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [till-check-scale]
playground: true
fruit: false
upnext: false
---

## The question

A weather screen shows the warmest reading of the day. Your code passes five test lists. Then a winter user sees 0 degrees as the warmest reading, when every reading was below freezing. How can you be sure a loop is right for every input, not only the ones you tried?

## Mental model

Think of checking a till at a shop.

- The till is the drawer where the money is. It holds the running total.
- The opening float is the money in the till before the first customer. Here it is 10.
- The customers are the items the loop handles, one per turn.
- The receipt roll is the list of prices written down so far.
- The till check is one sentence you test after each customer: the money in the till equals the float plus the total of the receipts.

If the check holds after every customer, the till is right at the end. If it fails, you know which customer caused it.

**Where the analogy stops:** A cashier counts the money, but a loop invariant is a claim argued in words, so it covers inputs nobody has counted. A till check can also pass by luck when two mistakes cancel out. A careful argument does not rely on luck.

## See it move

```sim
id: till-check-scale
template: stepper
title: A till check after every customer
intro: "The till holds the opening float of 10. The receipt roll is empty. Three customers wait with prices 5, 3 and 4."
code: true
lines:
  - { id: l1, text: "till = 10 (the opening float)" }
  - { id: l2, text: "for each customer:" }
  - { id: l3, text: "    till = till + price" }
  - { id: l4, text: "    receipts = receipts + price" }
  - { id: l5, text: "    check: till == 10 + receipts" }
board:
  - { var: till, label: "Till" }
  - { var: receipts, label: "Receipt roll total" }
  - { var: level, label: "Check" }
log: { columns: [Customer, Till, Receipts, Check] }
vars: { pc: 1, k: 0, till: 10, receipts: 0, level: "not yet", skip: "off" }
toggles:
  - { id: skip, label: "Forget the receipt for customer 2", var: skip }
actions:
  - id: serve
    label: "Serve the next customer"
    cases:
      - when: { var: { pc: 1 } }
        do:
          - add: { k: 1, till: 5, receipts: 5 }
          - set: { pc: 2, level: holds }
          - light: [ l3, l4, l5 ]
          - row: [ "{k}", "{till}", "{receipts}", "{level}" ]
          - say: "Customer 1 pays 5. The till reads 15 and the receipt roll totals 5. 10 plus 5 is 15, so the check holds."
      - when: { var: { pc: 2, skip: "on" } }
        do:
          - add: { k: 1, till: 3 }
          - set: { pc: 3, level: broken }
          - light: [ l3, l5 ]
          - row: [ "{k}", "{till}", "{receipts}", "{level}" ]
          - say: "Customer 2 pays 3, but the receipt is forgotten. The till reads 18 and the roll still totals 5. 10 plus 5 is 15, not 18, so the check fails at customer 2."
      - when: { var: { pc: 2 } }
        do:
          - add: { k: 1, till: 3, receipts: 3 }
          - set: { pc: 3, level: holds }
          - light: [ l3, l4, l5 ]
          - row: [ "{k}", "{till}", "{receipts}", "{level}" ]
          - say: "Customer 2 pays 3. The till reads 18 and the roll totals 8. 10 plus 8 is 18, so the check holds."
      - when: { var: { pc: 3, skip: "on" } }
        do:
          - add: { k: 1, till: 4, receipts: 4 }
          - set: { pc: 4, level: broken }
          - light: [ l3, l4, l5 ]
          - row: [ "{k}", "{till}", "{receipts}", "{level}" ]
          - say: "Customer 3 pays 4. The till reads 22 and the roll totals 9. 10 plus 9 is 19, not 22, so the check still fails. It failed first at customer 2."
      - when: { var: { pc: 3 } }
        do:
          - add: { k: 1, till: 4, receipts: 4 }
          - set: { pc: 4, level: holds }
          - light: [ l3, l4, l5 ]
          - row: [ "{k}", "{till}", "{receipts}", "{level}" ]
          - say: "Customer 3 pays 4. The till reads 22 and the roll totals 12. 10 plus 12 is 22, so the check holds."
      - when: { var: { pc: 4 } }
        do:
          - say: "All three customers are served. Switch the forgotten receipt on or off, or press Reset."
script: [ serve, serve, serve, "skip=on" ]
try:
  - text: "Press Step three times, or press \"Serve the next customer\" three times. After each customer the check line lights and the check holds."
    done: [ serve, serve, serve ]
  - text: "Switch on \"Forget the receipt for customer 2\". The steps replay. Find the first row where the check reads broken."
    done: [ "skip=on" ]
```

**What you should notice:** With no receipt forgotten, the check holds after every customer. With one forgotten, it fails at that exact customer.

## Build the logic

### Notice

The till opens with 10. Customer 1 pays 5, customer 2 pays 3, customer 3 pays 4. After customer 1 the till holds 15 and the receipts total 5. The till is always 10 more than the receipts.

### Guess

How could you be sure that is true for a line of customers you have never seen? Here are three ideas.

- A. Try many lines of customers and see whether it holds.
- B. Show that it is true at the start, that one customer cannot break it, and so it is true after the last one.
- C. Check it once at the end of the day only.

Idea A only covers the lines you thought of. Idea C cannot say which customer caused the error. Idea B covers every line of customers. It is the answer.

### Build

1. Write the sentence you want to keep true. It must be about the variables the loop changes. Here it is: till equals 10 plus the receipt total.
2. Before the first turn the till holds 10 and the receipts total 0, so it is true.
3. Show that one turn cannot break it. A turn adds the same price to the till and the receipts, so it stays true.
4. When the customers run out, the sentence still holds, so the till equals 10 plus all the receipts.
5. If any step fails, the loop has a bug. The step that fails tells you where to look.

### Name it

A **loop invariant** is a statement about the loop's variables that is true before the loop starts and again after every turn. A turn is one trip around the loop. Textbooks such as Introduction to Algorithms (CLRS) use three steps (general knowledge). **Initialization** means the statement is true before the first turn. **Maintenance** means that if it is true before a turn, it is still true after the turn. **Termination** means that when the loop stops, the statement and the stop condition together give the answer you wanted.

| Till check | Loop invariant step | Running total |
|---|---|---|
| The till holds the float and no receipts yet | Initialization | `till = 10` and `receipts = 0` |
| One customer adds the same price to both | Maintenance | `till += price` and `receipts += price` |
| No customers are left | Termination | `till` equals 10 plus every price |

In Kotlin you can watch an invariant with **`check`**. The Kotlin documentation says `check` throws an `IllegalStateException` if the value is false, so the program stops. An invariant is usually a written argument, and `check` tests your claim on real inputs.

```kotlin
fun main() {
    val prices = listOf(5, 3, 4)
    val opening = 10
    var till = opening
    var receipts = 0
    for (price in prices) {
        till += price
        receipts += price
        check(till == opening + receipts)
    }
    println("Till: " + till)
}
```

This prints `Till: 22`. The `check` line is the invariant, and it stays true after every turn.

Now the warmest-reading loop. Its invariant is: `best` is the largest reading seen so far. If you start `best` at 0, the statement is false for a list of freezing readings. That is an initialization failure, and the invariant points at the line that sets the starting value.

## Predict the output

```challenge
type: predict
id: running-total-last-line
verified: kotlin-2.4.21
code: |
  // What is the last line printed?
  fun main() {
      val prices = listOf(4, 6, 2)
      var total = 0
      for (price in prices) {
          total += price
          println("After " + price + ": " + total)
      }
  }
options:
  - id: a
    text: "After 2: 2"
    why: "You may have thought each line shows only its own price. The total keeps the sum of every price handled so far, like the till."
  - id: b
    text: "After 2: 6"
    why: "You may have added only the first and last prices. The invariant says the total holds every price handled so far, and that includes the 6."
  - id: c
    text: "After 2: 12"
    correct: true
    why: "After each turn the total equals the sum of the prices handled so far. After the third turn that is 4 plus 6 plus 2, which is 12."
output: |
  After 4: 4
  After 6: 10
  After 2: 12
```

```challenge
type: predict
id: largest-starting-at-zero
verified: kotlin-2.4.21
code: |
  fun main() {
      val temps = listOf(-5, -2, -9)
      var largest = 0
      for (t in temps) {
          if (t > largest) {
              largest = t
          }
      }
      println("Largest: " + largest)
  }
options:
  - id: a
    text: "Largest: 0"
    correct: true
    why: "The statement 'largest is the biggest reading seen so far' is false from the start, because 0 is not a reading. No reading beats 0, so 0 stays."
  - id: b
    text: "Largest: -2"
    why: "You may have given the right answer for the list. The loop does not find it, because the starting value 0 is bigger than every reading."
  - id: c
    text: "Largest: -5"
    why: "You may have thought the loop keeps the first reading. It starts at 0, not at the first reading, and no reading is bigger than 0."
output: |
  Largest: 0
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see one line: `Till: 22`.
3. **Break.** Change `receipts += price` to `receipts += 1` and change nothing else. Run it again. The program stops in the first turn and never prints `Till`.
4. **Fix.** Change `receipts += 1` back to `receipts += price`. Run it. You should see `Till: 22` again.

## Fix the bug

```challenge
type: fix
id: fix-warmest-starts-at-zero
generated: true
verified: kotlin-2.4.21
code: |
  // Finds the warmest reading in a list of temperatures.
  fun warmest(readings: List<Int>): Int {
      var best = 0
      for (reading in readings) {
          if (reading > best) {
              best = reading
          }
      }
      return best
  }

  fun main() {
      println("Warmest: " + warmest(listOf(12, 18, 15)))
      println("Warmest: " + warmest(listOf(-8, -3, -6)))
  }
hints:
  - "Look at the line that sets the starting value of best. Is it a reading from the list?"
  - "The invariant says best is the largest reading seen so far. Start best at the first reading."
expected: |
  Warmest: 18
  Warmest: -3
fix: |
  // Finds the warmest reading in a list of temperatures.
  fun warmest(readings: List<Int>): Int {
      var best = readings[0]
      for (reading in readings) {
          if (reading > best) {
              best = reading
          }
      }
      return best
  }

  fun main() {
      println("Warmest: " + warmest(listOf(12, 18, 15)))
      println("Warmest: " + warmest(listOf(-8, -3, -6)))
  }
explanation: "Starting best at 0 broke the statement before the first turn. Starting at the first reading makes it true from the start, and each turn keeps it true."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

No value here is null, but a list can be empty, and then `readings[0]` stops the program. A reviewer would ask what should happen for an empty list.

## Tricks to remember

- Three steps: true at the start, kept by every turn, useful at the end.
- The till check after every customer finds the exact customer that broke it.
- A starting value is part of the proof. A wrong start breaks the invariant before the first turn.

## Common mistakes

- **Thinking that passing tests proves the loop is correct.** Five green tests feel like proof, but they only cover the inputs you thought of.
- **Picking a starting value that looks neutral.** Starting a "largest" at 0 feels safe, but it fails for freezing readings. Start from a value that is really in the list.
- **Choosing an invariant that is true but useless.** "The till is not empty" stays true after every turn and says nothing about the answer. Choose a statement that, with the stop condition, gives the result you want.

## Where this shows up in Android

A cart screen adds up prices, and a refund is a negative price. A weather screen finds the warmest reading, and in winter every reading is below zero. The loop passes its tests and shows a wrong number to one real user. My own reasoning: the starting value is the usual culprit, and stating the invariant before writing the loop finds it.

## Say it in an interview

**What are the three steps to prove a loop invariant?**
Initialization, maintenance and termination: the statement holds before the first turn, each turn keeps it true, and at the end the statement and the stop condition give the answer. General knowledge.

**What is the loop invariant of a binary search with `while (low <= high)`?**
If the target is in the array, it is at an index from `low` to `high`. Before the loop it holds, because the range is the whole array. Each turn throws away a half that cannot hold the target, so it still holds. When the range is empty, the target was not in the array. This is general knowledge.

**Why does it help to say your invariant aloud in an interview?**
My own reasoning: stating the invariant makes you check the start, the update and the end, so off-by-one mistakes show up early.

**Do passing tests prove a loop correct?**
No. Tests show the loop works on those inputs. An invariant argument covers every input, but only if the statement is right, so I use both. This is my own reasoning.

## Recap

1. A loop invariant is a fact about the variables that holds before the loop and after every turn.
2. Prove it in three steps: it holds at the start, each turn keeps it, and the end uses it.
3. Passing tests show the loop works on those inputs, while an invariant argument covers every input.

## You can now

- Predict the running total after each turn of a short loop.
- Predict why a "largest value" loop that starts at 0 fails on freezing readings.
- Fix a loop whose starting value breaks its invariant.
- Spot the first customer at which a till check breaks.

## Practise on these

Each problem keeps a fact true after every turn. State the fact before you write the loop, and check the exact rules. The list runs from easiest to hardest.

1. LeetCode 704, Binary Search. The range from low to high always holds the target if it is in the array. https://leetcode.com/problems/binary-search/
2. LeetCode 26, Remove Duplicates from Sorted Array. The front part of the array holds every different value seen so far, in order. https://leetcode.com/problems/remove-duplicates-from-sorted-array/
3. LeetCode 283, Move Zeroes. The front part holds the non-zero values seen so far, in their original order. https://leetcode.com/problems/move-zeroes/
4. LeetCode 53, Maximum Subarray. After each turn, the running value is the best sum of a stretch that ends at the current position. https://leetcode.com/problems/maximum-subarray/
5. LeetCode 75, Sort Colors. The array is split into three finished parts and one part not yet looked at. https://leetcode.com/problems/sort-colors/
6. LeetCode 42, Trapping Rain Water. Two pointers move inward, and the side with the lower wall decides the water at its position. https://leetcode.com/problems/trapping-rain-water/
