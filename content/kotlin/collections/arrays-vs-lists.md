---
id: kotlin/collections/arrays-vs-lists
title: When does Kotlin code use an Array instead of a List?
status: shadow
difficulty: easy
order: 30
requires: [kotlin/collections/list-vs-mutable-list]
summary: An array has a fixed number of slots you can change. Learn how it differs from a list and when code uses one.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/arrays.html ; https://kotlinlang.org/docs/collections-overview.html ; https://developer.android.com/develop/ui/compose/graphics/draw/brush -->
## The question

You open a teammate's Kotlin file. One place keeps a row of numbers in a List, which you have already met. Another place keeps a row of numbers in something written with the word Array. Both rows hold numbers in order. Why does Kotlin have two kinds, and when would you pick the second one?

## Mental model

Think of an egg box and a shopping bag.

- The egg box has a fixed number of slots, such as 6. The number is set when the box is made.
- Each slot holds one egg and has a number on the lid. The numbers start at 0.
- You can swap the egg in any slot at any time. Nothing covers the box.
- The shopping bag is the kind of list you may add to and take from. It grows and shrinks as you shop.
- A name tag on the shelf says which box to use. Two tags can point at one box.
- Two egg boxes are two things, even when they hold the same eggs in the same slots.
- Some boxes are built for eggs only, so each egg lies straight in its slot. Other boxes take anything, so each egg sits in its own paper cup first. The cups cost a little space and time.

**Where the analogy stops:** You can see a paper cup in a real box. In Kotlin, the cups are added for you, and you never see them. Also, a real shopper compares two boxes by looking at the eggs. Kotlin's plain "same?" question on an array looks at the box, not the eggs.

## Build the logic

### Notice

Mia has an egg box with 6 slots. She swaps the egg in slot 2 for a bigger one, and the box allows it. She wants a seventh slot, but the box has none. So she takes a new box with 7 slots and moves the eggs across. Her brother has a second box with the same eggs in the same slots. Tom has a shopping bag. He adds milk, adds bread and takes the milk out again.

### Guess

Mia asks the computer: "Is my brother's box the same as mine?" What does the computer check? Here are three ideas.

- A. Whether the two name tags lead to one box.
- B. Whether the two boxes have the same number of slots.
- C. Whether the two boxes hold the same eggs in the same slots.

Idea C is what most people expect, and it is what a shopping bag does. Idea B is too weak, because two boxes with 6 slots can hold different eggs. For an egg box, the plain question is idea A. Looking at the eggs needs a different question.

### Build

1. An egg box gets its number of slots when it is made. The number never changes.
2. Any slot can be swapped at any time. Nothing locks the box.
3. To get a bigger box, make a new one and move the eggs across.
4. A name tag only says which box to use. Locking the tag does not lock the eggs.
5. The plain question "are these the same?" asks whether two tags lead to one box.
6. To ask "do these hold the same eggs in the same slots?", use a different question.
7. To print the eggs, first line them up as one piece of text. Handing over the whole box shows a label for the box, not the eggs.
8. A shopping bag can grow and shrink, and it is compared by what it holds. Reach for the bag first. Use the egg box when the size is fixed and speed matters, or when other code asks for one.

### Name it

An **array** holds a fixed number of values of the same type, in order. The Kotlin documentation says each value is reached by its position, called its **index**. The first index is 0 (general knowledge). You make one with `arrayOf(...)`. For whole numbers you can use `intArrayOf(...)`, which makes an **`IntArray`**. Writing `IntArray(3)` makes a box of 3 slots that all hold 0. You read a slot with `box[1]` and change it with `box[1] = 9`.

The documentation says `val` on an array only stops you from giving the name a different array. The items stay changeable in both `val` and `var` arrays. It also says an array has a fixed size and has no read-only version. To change the size, you make a new array and copy the items across.

The paper cups have a name. The documentation says the `Array` class wraps number values in objects, which is called **boxing**. Arrays built for plain number types, such as `IntArray`, store the values without boxing.

For comparing, the documentation says `==` on arrays checks whether both names point at one array. To compare the items, it tells you to use **`contentEquals`**. To show the items, use `joinToString()`, which joins them into one piece of text. Printing the array itself shows a label for the array and not the items. That last point is general knowledge, because the arrays page does not show it. The function `toList()` copies an array into a list, and the documentation says later changes to the array do not show in the copy.

| Egg box and bag | In Kotlin |
|---|---|
| The egg box | An array |
| The number on a slot | The index |
| Eggs lying straight in the slots | An `IntArray` |
| Eggs in paper cups | An `Array<Int>`, or a list of numbers |
| The shopping bag | A `List` or `MutableList` |
| "Same box?" | `==` on two arrays |
| "Same eggs in the same slots?" | `contentEquals` |
| Copying the eggs into a new bag | `toList()` |

This program shows each rule once.

```kotlin
fun main() {
    val box = intArrayOf(4, 8, 15)
    val twin = intArrayOf(4, 8, 15)
    println(box == twin)
    println(box.contentEquals(twin))
    box[1] = 9
    println(box.joinToString())
    val bag = box.toList()
    box[0] = 0
    println(bag)
}
```

It prints `false`, `true`, `4, 9, 15` and `[4, 9, 15]`. The two boxes are different boxes, so `==` says `false`. Their eggs match, so `contentEquals` says `true`. The list is a copy made after the swap, so changing slot 0 of the box afterwards does not reach it.

Kotlin refuses to run this one, because an array has no way to grow.

```kotlin
// does not compile
fun main() {
    val box = intArrayOf(1, 2, 3)
    box.add(4)
}
```

## Predict the output

```challenge
type: predict
run: false
id: same-box-or-same-eggs
verified: kotlin-2.4.21
code: |
  fun main() {
      val first = intArrayOf(1, 2)
      val alias = first
      val twin = intArrayOf(1, 2)
      println(first == alias)
      println(first == twin)
      println(first.contentEquals(twin))
  }
options:
  - id: a
    text: "false, false, true"
    why: "You may have thought two tags can never mean the same box. The tag alias was hung on the box that first names, so the plain question says yes."
  - id: b
    text: "true, true, true"
    why: "You may have expected the plain question to look at the eggs. For boxes it asks whether it is one box, and twin is a different box."
  - id: c
    text: "true, false, true"
    correct: true
    why: "alias is a second tag on the same box, so the plain question says true. twin is a new box, so it says false. contentEquals looks at the eggs, which match."
output: |
  true
  false
  true
```

```challenge
type: predict
run: false
id: copy-before-the-swap
verified: kotlin-2.4.21
code: |
  fun main() {
      val box = intArrayOf(5, 6, 7)
      box[2] = 70
      val bag = box.toList()
      box[0] = 50
      println(box.joinToString())
      println(bag)
  }
options:
  - id: a
    text: "50, 6, 70 and then [50, 6, 70]"
    why: "You may have thought the bag follows the box. toList() packs the eggs into a new bag at that moment, and the bag does not change afterwards."
  - id: b
    text: "50, 6, 70 and then [5, 6, 70]"
    correct: true
    why: "The swap in slot 2 happened before the bag was packed, so the bag holds 70. The swap in slot 0 came after, so the bag still holds 5."
  - id: c
    text: "The program does not compile, because box is a val"
    why: "You may have expected the locked tag to lock the eggs. The tag is locked, and the eggs can still be swapped."
output: |
  50, 6, 70
  [5, 6, 70]
```

```challenge
type: predict
run: false
id: new-box-of-three
verified: kotlin-2.4.21
code: |
  fun main() {
      val box = IntArray(3)
      box[1] = 4
      println(box.joinToString())
      println(box.size)
  }
options:
  - id: a
    text: "0, 4, 0 and then 3"
    correct: true
    why: "The box has 3 slots from the moment it is made, and every slot starts with a 0. Only slot 1 was swapped."
  - id: b
    text: "4 and then 1"
    why: "You may have thought the box only has the slots you filled. The size was fixed at 3 when the box was made."
  - id: c
    text: "The program stops, because slots 0 and 2 are empty"
    why: "You may have expected an empty slot to hold nothing. A box made this way puts a 0 in every slot."
output: |
  0, 4, 0
  3
```

## Tricks to remember

- An egg box has a fixed number of slots. A shopping bag can grow.
- The plain `==` asks "same box?". The call `contentEquals` asks "same eggs?".
- Line the eggs up with `joinToString()` before you print an array.
- Reach for the bag first. Use the egg box when the size is fixed or other code asks for one.

## Common mistakes

- **Comparing two arrays with `==` and expecting a check of the items.** It works that way for lists, so it feels right. For arrays it asks whether both names lead to one array, and Kotlin gives `false`. The compiler only warns, and the warning is easy to miss. Use `contentEquals`.
- **Thinking `val` makes the items fixed.** The word sounds like "locked". The documentation says it only stops the name from pointing at a different array. Swap items freely, or copy with `toList()` if you need a frozen set.
- **Printing an array and expecting the items.** A list prints its items, so an array feels the same. Printing the array itself shows a label for the array (general knowledge). Use `joinToString()`.
- **Trying to add to an array.** A bag grows, so the box feels like it should. An array has no `add`. Make a new array and copy, or use a `MutableList`.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its brush page draws a gradient, which is a colour that fades into another colour. In one snippet the colour stops are written with `arrayOf(...)`, and each entry pairs a position with a colour. Elsewhere on the same page the colours are written with `listOf(...)`. So real Android code uses both kinds side by side. The page does not explain why it picks one or the other.

This next part is my own reasoning. Picture a settings screen that keeps four slider positions in an array. It lights up a Save button when `saved != current`. Two arrays with the same four numbers count as different. The compiler warns about that `==`, but only warns, which is easy to miss. So the Save button stays lit when nothing has changed. A check with `contentEquals`, or a list, would ask the question the screen meant.

## Say it in an interview

**What is the difference between an Array and a List in Kotlin?**
An array has a fixed size, and its items can always be changed. A `List` can be read-only, and a `MutableList` can grow. The documentation's comparison also says `==` on arrays compares whether both names point at one array, while on collections it compares what is inside. To grow an array you make a new one and copy. The documentation recommends collections for most uses.

**When would you pick an array?**
The documentation says arrays are for working with Java code and for low-level or speed-sensitive code. Java is another language that Android and many libraries use (general knowledge). My own reasoning: in everyday app code a `List` is safer, because it can be read-only and compares by content. Arrays also turn up in algorithm exercises (general knowledge).

**Why does Kotlin have `IntArray` as well as `Array<Int>`?**
The documentation says the `Array` class boxes number values into objects, and `IntArray` stores them without boxing. The trade-off is that `IntArray` is a different type from `Array<Int>` and from `List<Int>`, so you convert when another function wants the other one. My own reasoning: the saving matters most when there are very many numbers.

**How do you compare the items of two arrays?**
Use `contentEquals`. The documentation says not to use `==` for that, because `==` checks whether both names point at one array. For arrays inside arrays it names `contentDeepEquals`. The danger of `==` is that it compiles and gives `false`, so the bug hides. The compiler only warns, and the warning is easy to miss.

## Recap

1. An array has a fixed number of slots, and any slot can be changed.
2. The `==` check on arrays asks whether it is one array, so use `contentEquals` for items.
3. An `IntArray` stores plain numbers without boxing, and `toList()` copies an array into a list.

## You can now

- Predict whether `==` on two arrays gives true or false.
- Predict what a list copied from an array shows after the array changes.
- Predict the items and size of a new `IntArray` after one slot is set.
