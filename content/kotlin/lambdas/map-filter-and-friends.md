---
id: kotlin/lambdas/map-filter-and-friends
title: How do I transform a list without writing a loop?
status: shadow
difficulty: medium
order: 34
requires: [kotlin/lambdas/lambda-syntax, kotlin/collections/list-vs-mutable-list]
summary: Filter and map build a new list from an old one. See how they chain and what stays untouched.
video: ""
publishedOn:
updatedOn:
sims: [factory-line-stations]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/collection-filtering.html ; https://kotlinlang.org/docs/collection-transformations.html ; https://kotlinlang.org/docs/collection-ordering.html ; https://kotlinlang.org/docs/collection-grouping.html ; https://kotlinlang.org/docs/collection-elements.html ; https://kotlinlang.org/docs/collection-aggregate.html ; https://developer.android.com/develop/ui/compose/lists -->
## The question

A shop app has a list of 200 product names. The screen must show only the names longer than three letters, written in capitals. You could write a loop, a second list and a counter. Is there a shorter way to say what you want, without spelling out how to get it?

## Mental model

Think of a factory line for apples.

- A basket at the start holds the apples. Each apple wears a name tag, such as "Fuji".
- A conveyor belt carries copies of the apples past a row of stations.
- The throw-out station tests each apple. Apples that fail the test leave the belt.
- The repaint station rewrites each tag in a new way, such as all capitals.
- The line-up station puts the apples in order by something you can measure, such as the length of the tag.
- A crate at the end of the belt collects what is left.
- Each station follows a note that says what to test or what to change.

**Where the analogy stops:** In a real factory the apples are changed and the basket empties. Here the basket stays exactly as it was, and the crate holds new things. Also, a real line moves all the apples through together. Kotlin finishes one whole station before the next one starts (general knowledge).

## See it move

<!-- SIM: factory-line-stations -->

**Try this:**
1. Snap on "throw out tags of 3 letters or fewer" and press Run. Look at the crate and at the basket.
2. Add "put Mr in front of each tag" after it and run again.
3. Drag "put Mr in front" above the throw-out station and run again.

**What you should notice:** The crate changes with every run, the basket always stays full, and putting "Mr" first lets short tags stay on the belt.

## Build the logic

### Notice

The basket holds four apples tagged Braeburn, Cox, Fuji and Pippin. The throw-out station removes tags of three letters or fewer, so Cox leaves the belt. The repaint station rewrites the tags in capitals. The crate ends with BRAEBURN, FUJI and PIPPIN. The basket still holds all four apples with their old tags.

### Guess

After the line has run, what does the basket hold? Here are three ideas.

- A. Only the three apples that passed.
- B. All four apples, as they were.
- C. Three apples with capital tags.

Idea A and idea C feel right if you picture the stations working on the basket itself. But the belt carries copies, and the crate collects the results. Idea B is the answer.

### Build

1. Start with the basket. Never touch it.
2. A throw-out station asks each apple a yes-or-no question from a note. It keeps the apples that get yes and puts them in a new crate.
3. A repaint station follows a note for each apple and puts the new result in a new crate, in the same order.
4. A line-up station takes a measure from a note and puts the apples in order of that measure, in a new crate.
5. A sort-into-trays station puts each apple on a tray labelled with the note's answer.
6. A totals station adds up a number taken from each apple.
7. To find the first apple that passes a test, there are two ways. The strict way stops the line if none passes. The polite way answers "nothing here" and the line carries on.
8. A chain of stations hands the crate from one to the next. Swapping two stations can change what comes out.

### Name it

A **predicate** is a lambda that answers true or false. The function **`filter`** is the throw-out station. The Kotlin documentation says `filter` called with a predicate returns the elements that match it. The result of `filter` on a list is a new list, and the documentation says the original collection stays unchanged.

The function **`map`** is the repaint station. The documentation says it applies the lambda to each element and returns the list of the lambda's results, in the same order.

You chain stations with dots. In `fruit.filter { ... }.map { ... }`, `map` works on the list that `filter` gave back. The documentation says these functions build new collections from existing ones.

The friends are named here and used once in the program below.

- **`sortedBy`** is the line-up station. The documentation says it sorts by the value your lambda picks and returns a new collection.
- **`groupBy`** is the sort-into-trays station. The documentation says it returns a `Map`, a table that pairs each key with a value. Each key is the lambda's answer, and each value is the `List` of elements that gave that answer.
- **`firstOrNull`** is the polite first-apple search. The documentation says it returns the first element that matches, or **`null`** if none does. The value `null` is the special value for "nothing here".
- The plain `first` is the strict search. The documentation says it throws an **exception** if nothing matches. An exception is a stop of the program with an error.
- **`sumOf`** is the totals station. The documentation says it adds up the numbers your lambda picks.

| Factory line | In Kotlin |
|---|---|
| The basket | The original list |
| The throw-out station and its note | `filter` and a predicate |
| The repaint station and its note | `map` and its lambda |
| The line-up station | `sortedBy` |
| The sort-into-trays station | `groupBy` |
| The polite first-apple search | `firstOrNull` |
| The totals station | `sumOf` |
| The crate | The new list |

```kotlin
fun main() {
    val fruit = listOf("Braeburn", "Cox", "Fuji", "Pippin")
    val result = fruit.filter { it.length > 3 }.map { it.uppercase() }
    println(result)
    println(fruit)
    println(fruit.sortedBy { it.length })
    println(fruit.groupBy { it.length > 3 })
    println(fruit.firstOrNull { it.length > 10 })
    println(fruit.sumOf { it.length })
}
```

It prints `[BRAEBURN, FUJI, PIPPIN]`, `[Braeburn, Cox, Fuji, Pippin]`, `[Cox, Fuji, Pippin, Braeburn]`, `{true=[Braeburn, Fuji, Pippin], false=[Cox]}`, `null` and `21`. The second line shows the basket untouched. No name has more than 10 letters, so `firstOrNull` answers `null`. The lengths 8, 3, 4 and 6 add up to 21.

Kotlin refuses to run this one, because a `filter` note must answer true or false and this one answers with text.

```kotlin
// does not compile
fun main() {
    val fruit = listOf("Cox", "Fuji")
    val result = fruit.filter { it.uppercase() }
    println(result)
}
```

## Predict the output

```challenge
type: predict
run: false
id: order-of-the-stations
verified: not-run
code: |
  fun main() {
      val names = listOf("Bo", "Eve", "Cleo")
      val filterFirst = names.filter { it.length > 2 }.map { "Mr " + it }
      val mapFirst = names.map { "Mr " + it }.filter { it.length > 2 }
      println(filterFirst)
      println(mapFirst)
  }
options:
  - id: a
    text: "[Mr Eve, Mr Cleo] and then [Mr Eve, Mr Cleo]"
    why: "You may have thought the order of stations never matters. In the second line the Mr station runs first, so even Bo grows long enough to pass the test."
  - id: b
    text: "[Mr Eve, Mr Cleo] and then [Mr Bo, Mr Eve, Mr Cleo]"
    correct: true
    why: "In the first line Bo is thrown out before the repaint. In the second line every tag becomes longer than 2 first, so nothing is thrown out."
  - id: c
    text: "[Mr Bo, Mr Eve, Mr Cleo] and then [Mr Eve, Mr Cleo]"
    why: "You may have swapped the two lines. The first line tests the short tags before the Mr station runs, so Bo leaves the belt."
output: |
  [Mr Eve, Mr Cleo]
  [Mr Bo, Mr Eve, Mr Cleo]
```

```challenge
type: predict
run: false
id: basket-stays-full
verified: not-run
code: |
  fun main() {
      val prices = listOf(5, 20, 8, 30)
      val cheap = prices.filter { it < 10 }
      val doubled = cheap.map { it * 2 }
      println(cheap)
      println(doubled)
      println(prices)
  }
options:
  - id: a
    text: "[5, 8] then [10, 16] then [5, 20, 8, 30]"
    correct: true
    why: "The throw-out station fills a new crate with 5 and 8. The repaint station fills another with 10 and 16. The basket named prices was never touched."
  - id: b
    text: "[5, 8] then [10, 16] then [5, 8]"
    why: "You may have thought the throw-out station removes apples from the basket. It copies the passing apples into a new crate."
  - id: c
    text: "[5, 8] then [10, 16] then [10, 40, 16, 60]"
    why: "You may have thought the repaint station changes the basket. It writes its results to a new crate and leaves the basket alone."
output: |
  [5, 8]
  [10, 16]
  [5, 20, 8, 30]
```

```challenge
type: predict
run: false
id: repaint-where-test-meant
verified: not-run
code: |
  fun main() {
      val scores = listOf(40, 75, 90, 55)
      val passed = scores.map { it >= 60 }
      println(passed)
      println(passed.size)
  }
options:
  - id: a
    text: "[75, 90] and then 2"
    why: "You may have expected the list to keep the passing scores. That is the job of the throw-out station. The repaint station writes one result for every apple."
  - id: b
    text: "[false, true, true, false] and then 2"
    why: "You may have counted only the true answers. The repaint station makes one result per apple, so the crate holds four results."
  - id: c
    text: "[false, true, true, false] and then 4"
    correct: true
    why: "The repaint station turns each score into true or false and keeps all four results in order. The crate holds 4 items."
output: |
  [false, true, true, false]
  4
```

## Tricks to remember

- `filter` throws apples out. `map` repaints every apple. Both fill a new crate.
- The basket is never touched. Keep the crate in a name, or it is lost.
- The `filter` note answers true or false. The `map` note answers with a new thing.
- Each station works on the crate that the one before it filled, so the order matters.

## Common mistakes

- **Believing `filter` and `map` change the original list.** The words sound like something done to the list itself. They build a new list, and the original stays as it was. Keep the result in a `val`.
- **Using `map` where `filter` was meant.** A test feels like it should remove things. `map` gives one answer per item, so a test gives a list of true and false. Use `filter` to keep or drop items.
- **Calling `names.filter { ... }` and not keeping the result.** The line looks like it did the work. The crate is thrown away, and `names` is the same. Write `val kept = names.filter { ... }`.
- **Using `first` when nothing may match.** It reads shorter. If no item passes, it stops the program with an error. Use `firstOrNull` and decide what to do with `null`.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its lists page shows a contacts screen with a heading for each first letter. It builds the groups with `groupBy`, using the first letter of each contact's first name as the key. Each key is then paired with the list of contacts that start with that letter.

A comment in the page says this would ideally be done in the `ViewModel`, which is a part of an app that holds a screen's data (general knowledge). The page then goes through the groups and shows a heading and the contacts under it. The same page shows `items(messages) { message -> MessageRow(message) }`, where the lambda draws one row for each message.

This next part is my own reasoning: if the note for `groupBy` picked a letter from the last name, the headings on the screen would show the wrong letters, and the list would still look tidy.

## Say it in an interview

**What is the difference between `map` and `filter`?**
`filter` keeps the items that pass a yes-or-no test, so the result can be shorter. `map` turns every item into a new one, so the result has the same number of items. The documentation says `map` returns the list of the lambda's results in the original order.

**Do `filter` and `map` change the original list?**
No. The documentation says `filter` leaves the original collection unchanged, so it works on read-only and mutable ones. It says these functions build new collections from existing ones. My own reasoning: the cost is a new list for each step in a chain.

**What is the difference between `first` and `firstOrNull`?**
Both look for the first item that passes a test. The documentation says that if nothing matches, `first` throws an exception and `firstOrNull` returns `null`. Use `firstOrNull` when "no match" is a normal answer, and then handle the `null`.

**Does the order of the steps in a chain matter?**
Yes. Each step works on the list the step before gave. In the first Predict item, putting `map` before `filter` kept an item that `filter` first would have dropped. My own reasoning: putting `filter` first also means later steps handle fewer items.

## Recap

1. The function `filter` keeps the items that pass a test, and `map` turns each item into a new one.
2. Both build a new list and leave the original list as it was.
3. In a chain, each step works on the list the step before gave, so the order matters.

## You can now

- Predict whether swapping `map` and `filter` in a chain changes the result.
- Predict what the original list holds after a chain has run.
- Spot what `map` gives when a test was meant, a list of true and false.
