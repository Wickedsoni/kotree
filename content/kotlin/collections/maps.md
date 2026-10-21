---
id: kotlin/collections/maps
title: How do I look up a value by its key?
status: live
difficulty: easy
order: 28
requires: [kotlin/collections/list-vs-mutable-list, kotlin/null-safety/nullable-types]
summary: Look up a value by a label instead of a number. A missing label gives back nothing and does not crash.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---

## The question

A café app stores a price for each drink: tea, coffee and juice. When a customer taps tea, the app needs the price of tea. Searching a long row of prices for the word tea would be slow and clumsy. Is there a way to jump straight from the name to its price? And what should happen when someone asks for a drink the café does not sell?

## Mental model

Think of a coat check desk.

- The desk is the whole collection.
- A ticket number is what you hand in. Each ticket number is used once.
- A coat is what you get back.
- The hooks behind the desk are arranged so the clerk goes straight to the hook for your ticket. Nobody walks along the whole row of coats.
- If a ticket has no coat, the clerk says "no coat with that ticket".
- Two different tickets may hold two coats that look the same.

Handing in a coat under a ticket number that is already in use replaces the coat on that hook.

**Where the analogy stops:** At a real desk, the clerk gives you the ticket when you hand in the coat. In Kotlin you choose the ticket yourself. Also, a real clerk could mix up two coats. A Kotlin map never does, as long as the tickets count as equal only when they really are.

## Build the logic

### Notice

The desk holds three coats: ticket 7 has a red coat, ticket 12 has a blue coat and ticket 30 has a green coat. A customer hands in ticket 12 and gets the blue coat. Another customer hands in ticket 99. No hook has that number. A third customer hands in a new coat under ticket 12, and the blue coat is replaced.

### Guess

What should happen when someone hands in ticket 99? Here are three ideas.

- A. The desk stops all work until a manager arrives.
- B. The clerk says "no coat with that ticket" and nothing else goes wrong.
- C. The clerk hands over the coat with the nearest number, ticket 30.

Idea C is a guess, and Kotlin never guesses. Idea A does exist as a separate way of asking, and it suits a ticket your own program guarantees. For an ordinary question, Idea B is the answer.

### Build

1. File each coat under one ticket. A ticket number is never used twice.
2. To get a coat, hand in a ticket. The clerk goes straight to its hook.
3. If the hook holds a coat, you get it. If it holds nothing, you get "no coat".
4. So the answer is either a coat or "no coat". Your code must be ready for both.
5. A coat filed under a ticket that is already used replaces the old coat.
6. Different tickets may hold equal coats. Only the tickets must differ.

### Name it

A **`Map`** stores key-value pairs. The Kotlin documentation says keys are unique, but different keys can be paired with equal values. The ticket is the **key** and the coat is the **value**. You write a map with `mapOf("tea" to 3, "coffee" to 4)`. The word `to` builds a **`Pair`**, which is two values held together. A read-only map is made with `mapOf` and a changeable one with `mutableMapOf`. If a map starts empty, tell Kotlin the types in angle brackets: `<String, Int>` means text keys and whole-number values.

The brackets `prices["tea"]` are the desk asking about a ticket. The documentation says that if the key is not found, `get` and `[]` return `null`. **`null`** is the mark for no value, so the answer has a question mark in its type, here `Int?`. The mark `?:` is the **Elvis operator**. It means "use the left side, and if that is `null`, use the right side". So `prices["juice"] ?: 0` gives a number whatever happens.

The strict way to ask is `getValue(key)`. The documentation says it throws an exception if the key is not found, and the `getValue` page names it as `NoSuchElementException`. An **exception** is an error that stops the program at that line.

Writing with `prices["tea"] = 5` or `put` adds a new pair or overwrites the value for a key that already exists. The documentation says the `mapOf` entries are iterated in the order they were specified, and that if several pairs have the same key, the map holds the value from the last of them.

| Coat check | In Kotlin |
|---|---|
| The desk | A `Map` |
| A ticket number | A key |
| A coat | A value |
| Asking about a ticket | `prices["tea"]` |
| "No coat" | `null` |
| The strict way of asking | `getValue("tea")` |
| Filing a coat | `prices["tea"] = 5` |

```kotlin
fun main() {
    val prices = mutableMapOf("tea" to 3, "coffee" to 4)
    println(prices["tea"])
    println(prices["juice"])
    println(prices["juice"] ?: 0)
    prices["tea"] = 5
    prices["juice"] = 6
    println(prices["tea"])
    println(prices.size)
    println(prices.getValue("coffee"))
}
```

It prints `3`, `null`, `0`, `5`, `3` and `4`. The ticket "juice" has no coat at first, so the answer is `null`, and the Elvis operator replaces it with 0. Then tea is replaced by 5 and juice is added, so the desk holds three coats.

This next program compiles, but the strict way of asking stops it at the third line.

```kotlin
fun main() {
    val prices = mapOf("tea" to 3)
    println("asking")
    println(prices.getValue("juice"))
    println("done")
}
```

It prints `asking` and then stops with an exception. The word `done` never appears.

## Predict the output

```challenge
type: predict
run: false
id: missing-ticket-stand-in
verified: kotlin-2.4.21
code: |
  fun main() {
      val prices = mapOf("coffee" to 4, "juice" to 6)
      println(prices["coffee"] ?: 0)
      println(prices["tea"] ?: 0)
  }
options:
  - id: a
    text: "4, then null"
    why: "You may have expected the missing ticket to print null. The Elvis part replaces the answer null with 0."
  - id: b
    text: "4, then 0"
    correct: true
    why: "Ticket coffee has a coat, so the clerk hands over 4. Ticket tea has no coat, so the clerk says no coat, and the Elvis part swaps that for 0."
  - id: c
    text: "4, then the program stops"
    why: "You may have expected a missing key to crash. Square brackets answer null for a missing key. Only getValue stops the program."
output: |
  4
  0
```

```challenge
type: predict
run: false
id: add-to-the-count
verified: kotlin-2.4.21
code: |
  fun main() {
      val counts = mutableMapOf<String, Int>()
      counts["a"] = (counts["a"] ?: 0) + 1
      counts["a"] = (counts["a"] ?: 0) + 1
      counts["b"] = (counts["b"] ?: 0) + 1
      println(counts["a"])
      println(counts["b"])
      println(counts.size)
  }
options:
  - id: a
    text: "1, 1, 3"
    why: "You may have expected each write to add a new row. A key that is already used keeps one hook, and the new value replaces the old one."
  - id: b
    text: "1, 1, 2"
    why: "You may have expected the second write to start again at 1. The code reads the old count first and adds 1 to it."
  - id: c
    text: "2, 1, 2"
    correct: true
    why: "The first write to a finds no coat, so 0 + 1 is filed. The second write finds 1 and files 2 over it. The ticket b is filed once with 1. Two tickets are in use."
output: |
  2
  1
  2
```

```challenge
type: predict
run: false
id: same-ticket-twice
verified: kotlin-2.4.21
code: |
  fun main() {
      val tickets = mapOf(7 to "red", 12 to "blue", 7 to "green")
      println(tickets.size)
      println(tickets[7])
  }
options:
  - id: a
    text: "2, then green"
    correct: true
    why: "Ticket 7 is used twice, so it has one hook. The documentation says the map keeps the value from the last pair, which is green. Tickets 7 and 12 give a size of 2."
  - id: b
    text: "3, then red"
    why: "You may have expected every pair to get its own hook. A ticket number is never used twice."
  - id: c
    text: "2, then red"
    why: "You may have expected the first coat to win. For a repeated key, the last pair wins."
output: |
  2
  green
```

## Tricks to remember

- Ticket in, coat or "no coat" out.
- Square brackets can answer `null`, so plan for "no coat".
- The same ticket again replaces the coat.
- Tickets are unique. Coats may repeat.

## Common mistakes

- **Expecting a missing key to crash the program.** A missing item crashes a list, so it feels the same here. `prices["tea"]` answers `null` instead. Only `getValue` stops the program.
- **Using the answer as a plain number.** The coat looks like a number, so `prices["tea"] + 1` feels fine. The answer is `Int?`, and Kotlin refuses to run it until you handle `null`, for example with `?:`.
- **Writing a counter that overwrites.** Setting `counts["a"] = 1` every time feels like counting. It replaces the old count. Read the old count first with `counts["a"] ?: 0` and add 1.
- **Adding `!!` to silence the question mark.** It makes the red line go away. If the ticket has no coat, the program stops. Use `?:` to give a stand-in.

## Where this shows up in Android

The Android lesson on higher-order functions with collections groups cookies into a map. Its code reads `val softBakedMenu = groupedMenu[true] ?: listOf()`. The page says that because the result of subscripting a `Map` is nullable, you can use the Elvis operator to return an empty list. The Compose lists page also has a function whose input is a `Map<Char, List<Contact>>`.

This next part is our own reasoning: a menu screen with no soft-baked cookies shows an empty section instead of closing, because the missing ticket gives a stand-in and not a crash.

## Say it in an interview

**What does `map[key]` return for a missing key, and what does `getValue` do?**
`map[key]` returns `null`, so its type ends with a question mark. `getValue(key)` throws an exception, `NoSuchElementException`, as the documentation says. Use `[]` with `?:` when a missing key is normal. Use `getValue` when your code guarantees the key, so a bug shows loudly.

**In what order does a map iterate?**
The documentation says `mapOf` entries are iterated in the order they were specified. It says a `HashMap` gives no order guarantee and that you should not rely on one, and it points to `LinkedHashMap` when you need insertion order. My own reasoning: write code that does not depend on order unless the type promises it.

**What is the `equals` and `hashCode` contract, and why does it matter for map keys?**
Two keys equal by `equals` must give the same hash code, a number computed from the key. The `HashMap` page says it relies on both to find entries, and that breaking this can lead to incorrect behavior. My own reasoning in coat-check words: equal tickets must lead to the same section, and a ticket must never change after filing. The page warns that a key changed after use may become unreachable.

**Why use a `Map` instead of a list of pairs?**
The `HashMap` page says it provides fast lookups based on keys, and the map guarantees one value per key. A list of pairs would be searched one by one, and could hold the same key twice. The trade-off, my own reasoning: a map uses more memory to give those quick lookups and has no numbered places.

## Recap

1. A map files each value under a unique key and finds it by that key.
2. Square brackets give null for a missing key, and getValue stops the program.
3. Writing under an existing key replaces its value, so a counter must read the old count first.

## You can now

- Predict the result of a map lookup with a stand-in for a missing key.
- Predict the size and value of a map built with a repeated key.
- Predict the counts produced by a read-then-add counter.
