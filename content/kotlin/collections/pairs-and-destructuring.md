---
id: kotlin/collections/pairs-and-destructuring
title: How do I unpack two values at once?
status: shadow
difficulty: easy
order: 29
requires: [kotlin/collections/maps]
summary: A Pair holds two values. Unpacking copies them into two names by position, and it also works on map entries.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/destructuring-declarations.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-pair/ ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-triple/ ; https://kotlinlang.org/docs/constructing-collections.html ; https://developer.android.com/develop/ui/compose/kotlin ; https://developer.android.com/develop/ui/compose/state -->
## The question

A shop keeps each drink with its price: tea and 3. You want to carry the two together as one thing, and later take them out again under two names. How do you pack two values into one box, and how do you unpack them? And if you unpack in the wrong order, which name gets which value?

## Mental model

Think of a lunch box with two compartments, and two plates.

- The lunch box is the pair of values. It has a first compartment and a second compartment.
- The food in each compartment is one value.
- The plates are the names you choose for the values.
- Unpacking means setting out the plates and filling them from the box: the first compartment goes to the first plate, and the second to the second.
- A box with three compartments is possible too.
- A bin at the side takes any compartment you do not want.

The box does not know the names on your plates. It only knows which compartment came first.

**Where the analogy stops:** Real food leaves the box when you unpack it. In Kotlin the box keeps its values, and you can unpack it again. Also, a real lunch box could have any number of compartments. Kotlin's ready-made boxes have exactly two or three.

## Build the logic

### Notice

The box holds "tea" in the first compartment and 3 in the second. A cook sets out two plates labelled drink and price. Drink gets "tea" and price gets 3. A second cook labels the plates the other way round: price first, then drink. The first plate now gets "tea", under the label price, and the second gets 3, under the label drink.

### Guess

What decides which plate gets which food? Here are three ideas.

- A. The labels on the plates. A plate labelled drink looks for the drink.
- B. The order. The first plate takes the first compartment, and the second plate takes the second.
- C. The weight of the food.

Idea A feels right because we name things to say what they are. But the box has no idea what the labels mean. Idea C is not a rule at all. Idea B is the answer.

### Build

1. Put two foods in a two-compartment box.
2. Set out one plate for each compartment you want to use.
3. Fill the plates in order. The first plate gets the first compartment, and the second plate gets the second.
4. Choose the plate labels to match the order. The box cannot fix a swapped label.
5. If you do not want a compartment, put a bin in its place.
6. The box still holds everything after unpacking.
7. A shelf of such boxes can be walked one box at a time, and each box unpacked into two plates. The entries of a map are like that.

### Name it

A **`Pair`** holds two values. The Kotlin documentation says there is no meaning attached to the values, so it can be used for any purpose, and two pairs are equal if both components are equal. You make one with `Pair("tea", 3)` or `"tea" to 3`. Its parts are `first` and `second`. A **`Triple`** holds three values, `first`, `second` and `third`.

Unpacking is a **destructuring declaration**. The documentation says it creates multiple variables at once, as in `val (drink, price) = box`. It is compiled into calls of `component1()` and `component2()`, so the variables follow the order of those functions. The documentation shows that swapping the names swaps the values.

An underscore marks a value you do not need, as in `val (_, second) = triple`. The documentation says the skipped components are not called.

A **`for` loop** runs the lines in its braces once for each entry in a collection. A map entry is a pair of key and value, so `for ((name, cost) in menu)` unpacks every entry into two names. This is the documented form `for ((key, value) in map)`.

Your own types can be unpacked as well. A **data class** is a class, a named design for grouping values, written with the word `data`. The documentation says data classes automatically declare the `componentN()` functions, so unpacking works for them.

| Lunch box | In Kotlin |
|---|---|
| The box | A `Pair` or `Triple` |
| A compartment | `first`, `second`, `third` |
| Setting out plates | `val (a, b) = box` |
| A plate label | The name you choose |
| The bin | `_` |
| A shelf of boxes | A map, walked with `for ((k, v) in map)` |

```kotlin
fun main() {
    val box = Pair("tea", 3)
    val (drink, price) = box
    println(drink)
    println(price)
    println(box.first)
    val (_, second) = Triple(1, "x", true)
    println(second)
    val menu = mapOf("tea" to 3, "juice" to 6)
    for ((name, cost) in menu) {
        println("$name costs $cost")
    }
}
```

It prints `tea`, `3`, `tea`, `x`, `tea costs 3` and `juice costs 6`. The first plate gets "tea" and the second gets 3. The box still holds "tea", so `box.first` works after unpacking. The bin takes the first compartment of the triple, so `second` holds "x". The map is walked in the order it was written.

Kotlin refuses to run this one, because a `Pair` has only two compartments and the code asks for three plates.

```kotlin
// does not compile
fun main() {
    val box = Pair("tea", 3)
    val (a, b, c) = box
    println(a)
}
```

## Predict the output

```challenge
type: predict
run: false
id: walk-the-shelf
verified: not-run
code: |
  fun main() {
      val ages = mapOf("Ana" to 31, "Ben" to 28)
      for ((k, v) in ages) {
          println("$k is $v")
      }
  }
options:
  - id: a
    text: "31 is Ana, then 28 is Ben"
    why: "You may have expected the number to go to the first plate because it is a number. The first plate takes the first compartment, which is the key."
  - id: b
    text: "Ana is 31, then Ben is 28"
    correct: true
    why: "Each entry is a box with the key first and the value second. The first plate k takes the key and the second plate v takes the value. The map is walked in the order it was written."
  - id: c
    text: "Ben is 28, then Ana is 31"
    why: "You may have expected the shelf to be walked backwards. The documentation says mapOf entries are iterated in the order they were specified."
output: |
  Ana is 31
  Ben is 28
```

```challenge
type: predict
run: false
id: swapped-plate-labels
verified: not-run
code: |
  fun main() {
      val item = Pair("tea", 3)
      val (price, name) = item
      println("$name costs $price")
  }
options:
  - id: a
    text: "tea costs 3"
    why: "You may have read the plate labels as a hint to the box. The box only knows the order, so the first plate gets tea."
  - id: b
    text: "price costs name"
    why: "You may have read the plate labels as plain words. The dollar sign in the text fills in what each plate holds."
  - id: c
    text: "3 costs tea"
    correct: true
    why: "The first plate, labelled price, takes the first compartment, tea. The second plate, labelled name, takes 3. The text then reads the plates by their labels."
output: |
  3 costs tea
```

```challenge
type: predict
run: false
id: bin-in-the-middle
verified: not-run
code: |
  fun main() {
      val (a, _, c) = Triple("x", "y", "z")
      val box = Pair(a, c)
      println(box.second + box.first)
  }
options:
  - id: a
    text: "zx"
    correct: true
    why: "The bin takes the middle y. Plates a and c hold x and z, so the new box holds x, then z. Its second part plus its first part reads z then x."
  - id: b
    text: "xz"
    why: "You may have joined first and second in the order they sit in the box. The code writes second first."
  - id: c
    text: "zy"
    why: "You may have thought the bin shifts the next compartment into place. A bin only skips its compartment, so c still holds z and y is never used."
output: |
  zx
```

## Tricks to remember

- Plates fill in order. Labels are yours to choose.
- `Pair` has two compartments and `Triple` has three.
- The underscore is the bin.
- To walk a map, unpack each entry into a key plate and a value plate.

## Common mistakes

- **Expecting the names to be matched to values.** Names usually say what a thing is, so it feels like they should find their own values. Unpacking follows order only. Choose the names to match the order, and read them once more.
- **Unpacking a `Pair` into three names.** A `Triple` has three, so it feels like a bigger `Pair` should work. A `Pair` has two compartments, and Kotlin refuses to run the line. Use the right box for the right count.
- **Using a `Pair` everywhere.** It is quick to write, so it feels like the tidy choice. The words `first` and `second` say nothing about what they hold. The documentation says it is often better to have your data named properly. Choose a data class when the meaning matters.

## Where this shows up in Android

The Android page on Kotlin for **Jetpack Compose**, the modern toolkit for building Android UI, has a section on destructuring data classes. It shows `val (name, age) = mary` for a `Person`, and says you will often see that kind of code in Compose functions, such as `val (image, title, subtitle) = createRefs()`. On the Compose state page, one of three equivalent ways to declare a piece of state is `val (value, setValue) = remember { mutableStateOf(default) }`. The page does not call this destructuring. My own reading: the syntax is the same unpacking into two names.

This next part is our own reasoning: if those two names were swapped, the name `value` would hold the way to change the state, and the screen would not show the text a person typed.

## Say it in an interview

**How does `val (a, b) = pair` work?**
The documentation says it is compiled into `val a = pair.component1()` and `val b = pair.component2()`. The `componentN()` functions must be marked with the word `operator`, which lets Kotlin call them for this syntax. A `Pair` and any data class provide them, so unpacking works for both.

**Does destructuring match by name or by position?**
By position. The documentation says the variables correspond to the order of the `componentN()` functions, and shows `val (email, username) = user` giving `email` the first property, which is the username. So the order of your names must match the order of the properties.

**Should a function return a `Pair` or a data class for two values?**
The documentation says a data class is a compact way to return several values, and that a `Pair` is possible but it is often better to have your data named properly. My own reasoning: a `Pair` is quick for a local helper, while a named class makes call sites easier to read months later.

**How do you ignore one value, and what happens to it?**
Put an underscore in its place. The documentation says the `componentN()` function is not called for a skipped component. You also keep the original pair, since unpacking copies out the values and leaves the box as it was.

## Recap

1. A Pair holds two values, and a Triple holds three.
2. Destructuring fills your names in order, not by matching the names.
3. For a map entry, unpacking gives you the key and the value.

## You can now

- Predict which value each name holds after destructuring a Pair.
- Predict the output of a loop that unpacks every map entry.
- Spot swapped names in a destructuring line.
