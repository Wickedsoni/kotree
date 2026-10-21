---
id: kotlin/types-and-variables/number-conversions
title: Why won't Kotlin put an Int where a Long is expected?
status: live
difficulty: medium
order: 7
requires: [kotlin/types-and-variables/basic-types]
summary: Kotlin keeps number sizes apart, so you convert an Int to a Long yourself. Adding past a size's top mark wraps around.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [overflow-cups]
playground: false
fruit: false
upnext: false
---

## The question

A storage screen adds up the sizes of two video files. One file is 2 billion in size and the other is 1 billion, and the screen shows a negative total. In another place in the same app, you try to put a small whole number into a slot made for a bigger kind of number, and Kotlin refuses to run. Why does Kotlin refuse, and why did the total turn negative?

## Mental model

Think of a coffee shop with two kinds of container.

- The small cup holds a drink up to a mark on its side. The mark reads 2,147,483,647.
- The big jug holds much more. Its mark reads 9,223,372,036,854,775,807.
- The amount of drink is the number.
- The staff behind the counter check every order before any drink is poured.
- An order slip is a note that says "pour this into the jug".

The staff will not move a drink from the small cup into the jug on their own, even though it fits. You hand over an order slip first, and then they pour a copy into the jug.

**Where the analogy stops:** A real cup spills when it is too full, and you see the mess. A number that goes past its cup's top mark wraps around to a huge negative number, and nothing warns you.

## See it move

```sim
id: overflow-cups
template: chart
title: A small cup, a mark and a big jug
intro: The small cup (Int) holds 2000000000 and the big jug (Long) is empty. A third bar marks the top of the small cup, 2147483647.
axes: { x: Container, y: Amount of drink }
bars:
  - { id: small, label: Small cup (Int), value: { var: small } }
  - { id: mark, label: Small cup top mark, value: 2147483647 }
  - { id: jug, label: Big jug (Long), value: { var: jug } }
vars: { small: 2000000000, jug: 0, compiler: strict }
actions:
  - id: pour-plain
    label: Pour small cup into jug, no order slip
    cases:
      # The compiler never pours without a slip, so this case can never hold.
      - when: { var: { compiler: lenient } }
        do:
          - set: { jug: 0 }
          - say: This never runs.
    refuse: Refused. The staff will not pour an Int into a Long without an order slip, so Kotlin does not compile it.
  - id: pour-slip
    label: Pour small cup into jug, with an order slip (toLong)
    cases:
      - when: { var: { small: 2000000000 } }
        do:
          - set: { jug: 2000000000 }
          - say: With the order slip, a copy of 2000000000 goes into the jug.
      - when: { var: { small: 2147483647 } }
        do:
          - set: { jug: 2147483647 }
          - say: With the order slip, a copy of the full cup, 2147483647, goes into the jug.
      - when: { var: { small: -2147483648 } }
        do:
          - set: { jug: -2147483648 }
          - say: The slip copies the wrapped number too, because the wrap already happened in the small cup.
  - id: fill
    label: Fill small cup to its mark
    do:
      - set: { small: 2147483647 }
      - say: The small cup is full to its top mark, 2147483647.
  - id: drop-small
    label: Add one drop to the small cup
    cases:
      - when: { var: { small: 2147483647 } }
        do:
          - set: { small: -2147483648 }
          - say: One drop past the top mark wraps the small cup to -2147483648, and nothing warns you.
    refuse: This drop does not wrap, because the small cup is not at its top mark. Fill it first.
  - id: drop-jug
    label: Add one drop to the big jug
    cases:
      - when: { var: { jug: 2147483647 } }
        do:
          - set: { jug: 2147483648 }
          - say: The jug now holds 2147483648 and does not wrap, because its top mark is 9223372036854775807.
    refuse: The jug is not holding a full small cup yet. Fill the small cup, then pour it in with an order slip.
script: [ pour-plain, pour-slip, fill, drop-small, fill, pour-slip, drop-jug ]
try:
  - text: Press "Pour small cup into jug, no order slip", then the same pour with an order slip.
    done: [ pour-plain, pour-slip ]
  - text: Press "Fill small cup to its mark", then "Add one drop to the small cup".
    done: [ fill, drop-small ]
  - text: Fill the small cup again and pour it into the jug with an order slip. Then add one drop to the jug and read how much room is left.
    done: [ fill, pour-slip, drop-jug ]
```

**What you should notice:** The jug accepts the small cup's drink only with an order slip, and a small cup that gets one drop past its mark wraps around to the bottom.

## Build the logic

### Notice

Two video files have sizes of 2,000,000,000 and 1,000,000,000. Each fits in the small cup, because each is below 2,147,483,647. Together they make 3,000,000,000. That is past the small cup's mark, but it fits in the jug with plenty of room.

### Guess

The small cup holds 7, and a counter further along expects a jug. What should the staff do? Here are three ideas.

- A. Pour the 7 into the jug without saying anything, because the jug is bigger.
- B. Ask for an order slip first, even though the 7 would fit.
- C. Refuse for ever, because the cups are different sizes.

Idea A feels kind. But the Kotlin documentation says numeric types are not stand-ins for one another, and that Kotlin requires explicit conversions to avoid silent data loss and unexpected behavior. Idea C is too strict, since a slip is allowed. Idea B is the answer. This next part is our own reasoning: one rule in both directions means you never have to remember which direction is the safe one.

### Build

1. Every number lives in a container of one size, and the size is part of its label.
2. A container of one size never passes for a container of another size.
3. To move a number to another size, write an order slip naming the new size. The staff pour a copy into a new container of that size.
4. The same slip idea works from big to small. That direction can lose drops, so the slip is where you take that risk on purpose.
5. Adding a small-cup amount to a jug amount is allowed, and the answer comes out in the jug.
6. A plain number written straight into the code, such as 1000, takes the size of the container it is poured into, if it fits.
7. A small cup that gets past its mark wraps around. So write the slip before you add, not after.

### Name it

A whole number that fits in the small cup has the type **`Int`**. The Kotlin documentation lists its smallest and largest values as -2,147,483,648 and 2,147,483,647. The jug is the type **`Long`**, with a range up to 9,223,372,036,854,775,807.

The order slip is an **explicit conversion**: a call to a function such as `toLong()` that gives you a copy of the number in another type. The documentation lists `toByte()`, `toShort()`, `toInt()`, `toLong()`, `toFloat()` and `toDouble()`. A number written with an `L` after it, such as `1000000000L`, is a `Long`. The documentation calls this the `L` suffix.

When an answer is past the top mark, **overflow** happens. The documentation says the result wraps around because the value no longer fits, and that the compiler does not automatically produce an error.

| Coffee shop object | In Kotlin |
|---|---|
| Small cup | `Int` |
| Big jug | `Long` |
| Mark on the side | The largest value the type can hold |
| Staff behind the counter | The compiler |
| Order slip | `toLong()` |
| Drink spilling over | Overflow, which wraps around instead |

This program is refused, because it puts an `Int` where a `Long` is expected.

```kotlin
// does not compile
fun main() {
    val small: Int = 7
    val big: Long = small
    println(big)
}
```

This program writes the slip first, then adds in the jug.

```kotlin
fun main() {
    val small: Int = 2000000000
    val big: Long = small.toLong()
    val total: Long = big + 1000000000L
    println(total)
}
```

It prints `3000000000`. The line `small.toLong()` is the order slip. The sum is added in a `Long`, so nothing wraps around.

## Predict the output

```challenge
type: predict
run: false
id: int-max-plus-one
verified: not-run
code: |
  fun main() {
      val intNumber: Int = 2147483647
      println(intNumber + 1)
  }
options:
  - id: a
    text: "2147483648"
    why: "You may have expected the cup to hold one more drop. 2147483647 is the top mark of an Int, so there is no room for that drop."
  - id: b
    text: "-2147483648"
    correct: true
    why: "The cup is full to its mark. One more drop does not spill. The number wraps around to the bottom of the range."
  - id: c
    text: "The program stops with an error"
    why: "You may have expected an error, as with a spill. The documentation says the compiler does not automatically produce an error on overflow."
output: |
  -2147483648
```

```challenge
type: predict
run: false
id: slip-after-the-sum
verified: not-run
code: |
  fun main() {
      // Goal: the total of two file sizes, as a Long: 3000000000.
      val first: Int = 2000000000
      val second: Int = 1000000000
      val total: Long = (first + second).toLong()
      println(total)
  }
options:
  - id: a
    text: "3000000000"
    why: "You may have read the goal instead of the code. The two Int amounts are added in the small cup before the slip is written."
  - id: b
    text: "2147483647"
    why: "You may have expected the cup to stop at its mark. It wraps around instead, so the total does not stop there."
  - id: c
    text: "-1294967296"
    correct: true
    why: "The sum is added in the small cup and wraps around to -1294967296. The slip turns that wrong number into a Long, but it cannot bring back the lost drops."
output: |
  -1294967296
```

```challenge
type: predict
run: false
id: slip-before-the-sum
verified: not-run
code: |
  fun main() {
      val first: Int = 2000000000
      val second: Int = 1000000000
      val total: Long = first.toLong() + second
      println(total)
  }
options:
  - id: a
    text: "3000000000"
    correct: true
    why: "The slip comes first, so the sum starts in the jug. Adding a small-cup amount to a jug amount is allowed, and the answer comes out in the jug."
  - id: b
    text: "-1294967296"
    why: "You may have expected the small cup to wrap. The first amount is already in the jug before the add happens."
  - id: c
    text: "It does not compile, because an Int and a Long are mixed in one sum"
    why: "You may have expected the staff to refuse any mixing. They refuse only a whole container passing for another size. Mixed sums are allowed."
output: |
  3000000000
```

## Tricks to remember

- Slip first, then pour: convert, then add.
- Past the top mark, a number wraps. It does not spill.
- An `L` after a number means the jug.
- The compiler checks sizes. Only you can check the top mark.

## Common mistakes

- **Expecting the jug to take a small cup's drink on its own.** The jug has more room, so it feels as if the move should be free. Kotlin wants the slip. Write `toLong()` where the `Long` is needed.
- **Writing the slip after the sum.** `(first + second).toLong()` looks safe because `Long` is big. By then the sum has already wrapped. Convert each number first, then add.
- **Waiting for a warning when a number gets too big.** The compiler is so strict about sizes that it feels as if it would catch this too. The documentation says it does not automatically produce an error. Check the top mark yourself.

## Where this shows up in Android

A storage screen lists videos and shows the total space they use. Picture a total of -1294967296 on the screen, which is the number from the second Predict item.

Code in an Android app can call code written in Java. The Kotlin documentation says a Java `long` arrives in Kotlin as a `Long`. So a Java method that returns a `long` gives your Kotlin code a `Long`. This next part is our own reasoning: if you keep the running total in an `Int`, it wraps once it passes about 2 billion, and the screen shows a negative size.

## Say it in an interview

**Why does Kotlin not turn an `Int` into a `Long` for you?**
The Kotlin documentation says numeric types are not subtypes of one another, and that Kotlin requires explicit conversions to avoid silent data loss and unexpected behavior. The cost is a few extra characters, such as `toLong()`. My own reasoning: the same rule works in both directions, so you never have to remember which direction is the safe one.

**What do you get when you add 1 to the largest `Int`?**
You get `-2147483648`. The documentation says the result wraps around because the value no longer fits in `Int`. It also says the compiler does not automatically produce an error, so nothing warns you. The trade-off is that the check is left to you.

**How do you add two big `Int` sizes without wrapping?**
Convert each number to `Long` first, then add, as in `first.toLong() + second`. Writing `(first + second).toLong()` is too late, because the sum already wrapped in an `Int`. My own reasoning: the slip must come before the sum.

**What does `toInt()` do to a `Long` that is too big?**
The documentation says a value converted to a smaller numeric type may not keep its original value. When you run it, `3000000000L.toInt()` gave `-1294967296`, the same number as the wrapped sum. So going from big to small is where you take the risk on purpose.

## Recap

1. Kotlin makes you write `toLong()` before an `Int` can go where a `Long` is expected.
2. An `Int` sum past 2,147,483,647 wraps around to a negative number, and nothing reports an error.
3. Convert each number to `Long` before you add them, not after.

## You can now

- Predict the number printed when an Int sum goes past its top mark.
- Spot a conversion that happens after the sum instead of before it.
- Predict the result of adding an Int to a Long.
