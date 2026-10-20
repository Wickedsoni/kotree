---
id: foundation/programming-thinking/loop-over-a-list
title: How do I do something to every item in a list?
status: shadow
difficulty: medium
order: 18
requires: [foundation/programming-thinking/counting-loops, foundation/programming-thinking/what-is-a-list]
summary: Do the same step to every value in a row. A running total starts at zero and grows by one value per round.
video: ""
publishedOn:
updatedOn:
sims: [basket-checkout-loop]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live): kotlinlang.org/docs/control-flow.html (For loops: "for (item in collection) print(item)"; shoppingList sample with items Milk, Bananas, Bread; "to iterate through an array or a list with an index, use the indices property"; ranges 1..<6); kotlinlang.org/docs/kotlin-tour-collections.html (lists keep order; listOf; [] access); kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/get.html (names size and the IndexOutOfBoundsException rule); developer.android.com/develop/ui/compose/mental-model (Greeting(names: List<String>) with for (name in names): "takes in a list of names and generates a greeting for each user"); developer.android.com/develop/ui/compose/lists (messages.forEach in a Column, MessageRow). Own reasoning: cart total symptom, wipe-the-display bug. Outputs hand-traced, not run (no kotlinc). -->
## The question

A shopping app shows a cart with 4 items. At the bottom it must show the total price. Next week the same cart holds 40 items.

How do you add up every price, without writing one line for each item?

## Mental model

Think of a cashier at a till with a basket of shopping.

- The basket holds the items in a row. Each item has a price on it.
- The cashier takes one item at a time, in the order they lie in the basket.
- Each place in the basket has a number. The first place is number 0, the next is 1, and so on.
- The scanner beeps once for each item. One beep is one round of work.
- The display on the till shows the running total, which is the amount so far.
- Before the first beep, the display shows 0.
- On each beep, the display shows its old number plus the price of the item the scanner has read.
- The display has a wipe button. Wiping sets it back to 0 and loses everything counted so far.
- The cashier stops when no unscanned item is left in the basket.

**Where the analogy stops:** A real cashier can scan items in any order, skip one, or look back at an earlier item. The program takes each item once, in the order they lie. A real cashier sees that the basket is empty. A program has no eyes. It steps through the row and stops after the last item. Also, a real till prints a receipt by itself. A program shows a total only when you tell it to.

## See it move

<!-- SIM: basket-checkout-loop -->

**Try this:**
1. Press "Step" and watch the cashier scan one item and the display change.
2. Press "Reset", set the display's starting number to 10, and press "Step" until the basket is empty.
3. Tick "Wipe the display before each scan" and step through the basket again.

**What you should notice:** Each beep adds one price to what the display already shows, and wiping before each scan leaves only the last price.

## Build the logic

### Notice

A basket holds four items priced 3, 5, 2 and 4. The cashier scans the first item, and the display shows 3. The second beep makes it 8. The third makes it 10. The fourth makes it 14.

The cashier did the same two things four times: scan the next item, then add its price to the display. Each time, the display kept what it already had.

### Guess

Before the first beep, what should the display show?

- A. The price of the first item.
- B. 0.
- C. The number of items in the basket.

Idea A counts the first item twice, once to start the display and again at its beep. Idea C mixes up how many items there are with how much they cost. Zero adds nothing to the total, so idea B is the answer.

Here is a second question. The basket holds 4 items today and 40 tomorrow. How many instructions do you write for the cashier?

- A. One rule that is used again for each item: take the next item, add its price.
- B. One instruction for each item, so 4 now and 40 tomorrow.
- C. One instruction for the first item and a different one for all the others.

Idea B feels safe, because you can see every step. But the day the basket changes size, you must rewrite it. Idea C needs a special case you do not need. Idea A is the answer. You describe one round, and the repeat does the rest.

### Build

1. Put 0 on the display before the cashier starts. Do this once.
2. Take the items from the basket in order, one at a time.
3. For each item, read its price. Show the old number plus that price. Never wipe the display in the middle of the job.
4. When no unscanned item is left, stop.
5. Whatever the display shows is the total.

### Name it

The basket is a **list**, a row of values kept under one name. A job that repeats for each item in a list is done with a **`for` loop**, the same kind of loop you used to count. The Kotlin documentation shows the shape as `for (item in collection)`. The word `in` means "take each item, one at a time".

The name between the brackets is the **loop variable**. It holds the item the cashier is scanning right now. Kotlin sets it for you at the start of every round. In a counting loop it held one number at a time. In a loop over a list it holds the item itself.

```kotlin
fun main() {
    val prices = listOf(3, 5, 2, 4)
    var total = 0
    for (price in prices) {
        total = total + price
    }
    println(total)
}
```

The line `var total = 0` sits above the loop, so it runs once. In round 1, `price` is 3 and `total` becomes 3. Then `total` becomes 8, then 10, then 14. After the last item the loop ends, and `println` prints `14`.

Sometimes you need the place number of each item too. This number is called its **index**. The documentation says that to go through a list with an index, you can use the `indices` property, as in `for (i in prices.indices)`. Then `prices[i]` is the item at that place.

## Predict the output

```challenge
type: predict
run: false
id: cart-total-thirteen
verified: not-run
code: |
  fun main() {
      val prices = listOf(4, 1, 6, 2)
      var total = 0
      for (price in prices) {
          total = total + price
      }
      println(total)
  }
options:
  - id: a
    text: "2"
    why: "You may have thought each round replaces the total with the newest price. Each round adds to what the display already shows."
  - id: b
    text: "12"
    why: "You may have missed one item. The loop visits all four, so 4 + 1 + 6 + 2 are all added."
  - id: c
    text: "13"
    correct: true
    why: "The display starts at 0 and gains 4, 1, 6 and 2 in turn. That makes 4, 5, 11 and then 13."
output: "13"
```

```challenge
type: predict
run: false
id: wipe-inside-the-loop
verified: not-run
code: |
  fun main() {
      val prices = listOf(3, 5, 2)
      var total = 0
      for (price in prices) {
          total = 0
          total = total + price
      }
      println(total)
  }
options:
  - id: a
    text: "2"
    correct: true
    why: "Every round wipes the display to 0 before adding the price. After the last round only the last price, 2, is left."
  - id: b
    text: "10"
    why: "You may have read the goal instead of the code. The wipe in each round loses the earlier prices."
  - id: c
    text: "0"
    why: "You may have stopped at the wipe line. The next line adds the price back in, so the display ends on 2."
output: "2"
```

```challenge
type: predict
run: false
id: start-at-place-one
verified: not-run
code: |
  fun main() {
      val prices = listOf(3, 5, 2, 4)
      var total = 0
      for (i in 1..<prices.size) {
          total = total + prices[i]
      }
      println(total)
  }
options:
  - id: a
    text: "14"
    why: "You may have thought the loop visits every place. It starts at place 1, so place 0, which holds 3, is never scanned."
  - id: b
    text: "11"
    correct: true
    why: "The places visited are 1, 2 and 3. They hold 5, 2 and 4, and the sum is 11. The first item is skipped."
  - id: c
    text: "9"
    why: "You may have skipped the wrong item. The loop skips place 0, which holds 3, and not the item with 5."
output: "11"
```

## Tricks to remember

- Put 0 on the display before the cashier starts, never during the job.
- Describe one round, and the loop repeats it.
- Add to what the display shows. Never wipe in the middle.
- A loop over a list hands you the item. A loop over `indices` hands you the place number.

## Common mistakes

- **Writing one line for each item.** It feels safe because every step is visible. It breaks on the fifth item. Write one round and let the loop repeat it.
- **Setting the total back to 0 inside the loop.** It feels like a clean start for each item. It wipes the display at every beep, so only the last price is left. Set it to 0 once, above the loop.
- **Starting the place numbers at 1.** In daily life you count the first item as 1. Place numbers start at 0, so a loop that starts at 1 skips the first item. Loop over the list itself, or use `indices`.

## Where this shows up in Android

The Android documentation shows a function that takes a list of names and says it "generates a greeting for each user". It does this with the same `for (name in names)` shape you met here. The lists page also draws one row for each message in a list.

Picture a cart screen where the total shows only the price of the last item. This part is my own reasoning, not the documentation's. If the code sets the total to 0 inside the loop, every round wipes the display. The rows are right, but the total is not.

## Recap

1. A for loop over a list repeats one step for each item, in order.
2. A running total starts at 0 before the loop and gets each value added.
3. Setting the total back to 0 inside the loop leaves only the last item.

## You can now

- Predict the total that a loop over a list prints.
- Spot a total that is reset to 0 inside the loop.
- Predict which items a loop skips when its place numbers start at 1.
