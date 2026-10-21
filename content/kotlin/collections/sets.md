---
id: kotlin/collections/sets
title: How do I keep only unique values?
status: live
difficulty: easy
order: 27
requires: [kotlin/collections/list-vs-mutable-list]
summary: A Set holds each value once. Adding the same value twice changes nothing. See how it differs from a list.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---

## The question

At a party door, a host ticks off names as guests arrive. Ana arrives, then Ben, then Ana again through the side door. How many guests are at the party? And what should the host do with the second Ana?

## Mental model

Think of a guest list on a clipboard at the party door.

- The clipboard is the collection of names.
- Each name on it is one guest.
- The host checks the clipboard before writing. A name that is already there is not written again.
- Asking "is Ana here?" means reading the clipboard.
- The clipboard has no row numbers, so you cannot ask for "guest number 2".
- The marker is what lets the host add or cross out names.

**Where the analogy stops:** A real host remembers who came first, and might read the names back in that order. Kotlin's `setOf` and `mutableSetOf` do keep the order you gave, but the `Set` type itself does not promise any order. Also, a real host compares faces. Kotlin compares values, so "Red" and "red" count as two different guests.

## Build the logic

### Notice

The door log reads Ana, Ben, Ana, Cy, Ben. The host's clipboard ends with three names: Ana, Ben and Cy. Each arrival after the first for the same name changed nothing on the clipboard.

### Guess

What should the host do when Ana arrives a second time? Here are three ideas.

- A. Write her name again, so the clipboard has a line for every arrival.
- B. Leave the clipboard alone, because Ana is already on it.
- C. Cross out her first line and write her again at the bottom.

Idea A is how a plain row of names behaves, and it counts Ana twice. Idea C does extra work that nobody needs. Idea B is the answer.

### Build

1. Keep one clipboard. It may start empty.
2. When a name arrives, look for it on the clipboard.
3. If it is not there, write it. If it is there, do nothing.
4. The number of names on the clipboard is then the number of different guests.
5. "Is Ana here?" is answered by reading the clipboard. No row numbers are needed.
6. Two names count as the same only if they match exactly. "Red" and "red" are different.

### Name it

The Kotlin documentation describes a **`Set`** as a generic unordered collection of unique elements. An element is one value in the collection. You make a read-only one with `setOf(...)` and a changeable one with `mutableSetOf(...)`. A `MutableSet` has `add`. Adding a value that is already there leaves the set as it was. The word `in` asks "is it here?", as in `"Ben" in guests`, and `size` counts the elements.

The documentation says kinds of set usually decide uniqueness with `equals`, the function that says whether two values are equal. For text, equal means the same letters in the same case.

Order needs care. The `Set` page says a set does not guarantee any particular order for **iteration**, which means going through the items one by one, although particular kinds of set may have a fixed order. The pages for `setOf` and `mutableSetOf` say the elements are iterated in the order you gave them. The page for **`HashSet`**, one kind of set, says it does not guarantee any order and that you should not rely on one. The page for **`LinkedHashSet`** says it iterates in the order elements were inserted, and that adding an element that is already there does not change that order.

Two sets are equal when they hold the same elements, whatever the order. The documentation's `setOf` page shows `setOf(1, 2, 3) == setOf(3, 2, 1)` giving `true`. Two lists are equal only if each place holds an equal element, so order matters for lists.

To remove duplicates from a list, `toSet()` returns a set of all elements and keeps the order. The function `distinct()` returns a list that keeps only the first of equal elements.

```kotlin
fun main() {
    val guests = mutableSetOf("Ana", "Ben")
    guests.add("Ana")
    guests.add("Cy")
    println(guests.size)
    println("Ben" in guests)
    println("Dee" in guests)
    println(setOf(1, 2, 3) == setOf(3, 2, 1))
}
```

It prints `3`, `true`, `false` and `true`. The second Ana changes nothing, so the set holds Ana, Ben and Cy. Ben is on the list and Dee is not. The two sets of numbers hold the same elements, so they are equal.

Kotlin refuses to run this one, because a set has no row numbers to ask for.

```kotlin
// does not compile
fun main() {
    val guests = setOf("Ana", "Ben")
    println(guests[0])
}
```

## Predict the output

```challenge
type: predict
run: false
id: size-of-repeated-values
verified: kotlin-2.4.21
code: |
  fun main() {
      val numbers = setOf(1, 2, 2, 3)
      println(numbers.size)
      println(numbers)
  }
options:
  - id: a
    text: "4, then [1, 2, 2, 3]"
    why: "You may have treated it like a plain row of names. The host does not write a name that is already on the clipboard."
  - id: b
    text: "2, then [1, 3]"
    why: "You may have thought a repeated value is removed completely. The host keeps one 2 on the clipboard."
  - id: c
    text: "3, then [1, 2, 3]"
    correct: true
    why: "The second 2 finds 2 already on the clipboard, so nothing is written. Three different values remain."
output: |
  3
  [1, 2, 3]
```

```challenge
type: predict
run: false
id: red-is-not-red
verified: kotlin-2.4.21
code: |
  fun main() {
      val seen = mutableSetOf("red")
      seen.add("blue")
      seen.add("red")
      seen.add("Red")
      println(seen.size)
  }
options:
  - id: a
    text: "2"
    why: "You may have read Red and red as the same guest. Values match only when the letters and their case match exactly."
  - id: b
    text: "3"
    correct: true
    why: "The second red is already on the clipboard, so it is skipped. Red has a capital letter, so it is a new guest. The clipboard holds red, blue and Red."
  - id: c
    text: "4"
    why: "You may have counted every add. Only a value that is not already on the clipboard gets written."
output: |
  3
```

```challenge
type: predict
run: false
id: order-for-sets-and-lists
verified: kotlin-2.4.21
code: |
  fun main() {
      val a = setOf("x", "y")
      val b = setOf("y", "x")
      val c = listOf("x", "y")
      val d = listOf("y", "x")
      println(a == b)
      println(c == d)
  }
options:
  - id: a
    text: "true, then false"
    correct: true
    why: "The two clipboards hold the same names, so they are equal. The two lists hold the names in different places, so they are not equal."
  - id: b
    text: "false, then false"
    why: "You may have expected the order to matter for the clipboards too. A set only asks which names are on it."
  - id: c
    text: "true, then true"
    why: "You may have expected order never to matter. For a list, each numbered place must hold an equal element."
output: |
  true
  false
```

## Tricks to remember

- A name already on the clipboard is not written again.
- A set answers "is it here?". It does not answer "who is number 2?".
- `setOf` keeps your order, but the `Set` type does not promise it.
- Case counts: "Red" and "red" are two guests.

## Common mistakes

- **Expecting a set to keep your order everywhere.** `setOf` and `mutableSetOf` do keep it, so it feels like a rule of sets. The `Set` type promises no order, and `HashSet` says not to rely on one. If order matters, say so in your code.
- **Reaching for a set item with `set[0]`.** A list works that way, so it feels natural. A set has no row numbers, and Kotlin refuses to run it. Read the items one by one, or turn the set into a list.
- **Counting repeats in a list when you meant different values.** A row of names keeps every arrival, so its size is the number of arrivals. Use a set when you want the number of different values.
- **Treating "Red" and "red" as the same.** They look like the same word. A set compares exactly, so make the case the same first.

## Where this shows up in Android

The Android page on a dynamic top app bar lets a person long-press rows in a list to select them. It keeps the selected rows in `var selectedItems by rememberSaveable { mutableStateOf(setOf<Int>()) }`, a set of the selected row numbers. A long-click runs `if (isItemSelected) selectedItems -= index else selectedItems += index`, and the bar shows "Selected ${selectedItems.size} items".

This next part is our own reasoning: with a set, selecting the same row twice never counts it twice, and the number in the bar is the number of different rows. A plain row of numbers could hold the same row number twice and show a count that is too big.

## Say it in an interview

**When do you use a `Set` instead of a `List`?**
Use a `Set` when you need unique values and often ask "is it here?". Use a `List` when order, numbered places or repeats matter. The documentation says a `Set` holds unique elements, while a `List` gives indexed access in a specified order. The `HashSet` page says it gives fast lookups, while a list has to check items one by one (general knowledge).

**Is a `Set` ordered?**
The `Set` type does not guarantee any order. The documentation says `setOf` and `mutableSetOf` iterate in the order you gave. It says `HashSet` guarantees no order and you should not rely on one, and `LinkedHashSet` keeps insertion order. My own reasoning: if your code depends on order, name a type that promises it, or keep a list.

**How does a set decide that two values are the same?**
The documentation says kinds of set usually use `equals`. For text that means the same letters in the same case. The documentation's contract for `hashCode` says that two objects equal by `equals` must give the same hash code, which is a number computed from the value. My own reasoning: if your own type breaks that contract, a `HashSet` may hold two values that you think are equal.

**How do you remove duplicates from a list?**
`toSet()` gives a set and keeps the order of the list. `distinct()` gives a list and keeps the first of equal elements. The trade-off is the result type: a set has no numbered places, and a list still has them.

## Recap

1. A Set holds each value once, and adding a value that is already there changes nothing.
2. Values match by equals, so "Red" and "red" are two different values.
3. The Set type promises no order, although setOf and mutableSetOf keep the order you gave.

## You can now

- Predict the size and contents of a set built from repeated values.
- Predict whether two values count as the same in a set.
- Predict whether two sets or two lists are equal when the order differs.
