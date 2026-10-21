---
id: kotlin/collections/index-out-of-bounds
title: Why does asking for item 5 crash a list of five items?
status: shadow
difficulty: easy
order: 26
requires: [kotlin/collections/list-vs-mutable-list, kotlin/null-safety/nullable-types]
summary: A list of five items has places 0 to 4. Learn what asking for place 5 does and how to ask safely.
video: ""
publishedOn:
updatedOn:
sims: [inn-room-clerk]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/list-operations.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/get-or-null.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/element-at.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/last-index.html ; https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/ ; https://developer.android.com/develop/ui/compose/layouts/pager -->
## The question

A list holds five names. One line of code asks for "item 5", and the app closes. Another line asks for the same item and gets back an answer that means "nothing there", with no crash. Both lines look at the same list. What is different about how they ask?

## Mental model

Think of an inn with five rooms.

- The inn is the list. Each room holds one item.
- Every room has a door number, and the numbers start at 0. Five rooms have doors 0, 1, 2, 3 and 4.
- The guest is the code that wants something from a room.
- A guest who walks straight to a door number has no one to help. If the door does not exist, the guest walks into the wall.
- The front-desk clerk knows the inn. A guest who asks the clerk gets the room, or a slip that reads "no such room".
- The slip is not a room. It stands for no room at all.

**Where the analogy stops:** A real clerk might offer you a nearby room. The Kotlin clerk never guesses: you get exactly the room you asked for, or the slip. Also, the slip is only useful if the guest reads it. You have to write what happens next.

## See it move

<!-- SIM: inn-room-clerk -->

**Try this:**
1. Type door number 2 and press "Walk to the door". Then type 5 and press it again.
2. Type 5 again and press "Ask the clerk".
3. Remove all the rooms and ask for door 0, both ways.

**What you should notice:** Walking to a door that does not exist stops the guest at the wall, while the clerk answers the same request with a "no such room" slip.

## Build the logic

### Notice

The inn has five rooms: Sky, Sea, Sun, Star and Moon, behind doors 0 to 4. A guest asks for door 2 and gets Sun. A guest asks for door 4 and gets Moon. A guest asks for door 5 and finds no door there, because the last door is 4. An empty inn has no doors at all, so even door 0 is missing.

### Guess

A guest asks for door 5 in a five-room inn. What should happen? Here are three ideas.

- A. The guest is given the nearest room, Moon.
- B. The guest walks into the wall and the visit stops.
- C. The clerk hands over a slip that says "no such room".

Idea A feels kind, but Kotlin never guesses what you meant. Ideas B and C both happen in Kotlin, depending on how you ask. Walking straight to the door gives B, and asking the clerk gives C.

### Build

1. Count the rooms. Here the count is 5.
2. The doors run from 0 up to the count minus 1, which is 4. The count itself is never a door.
3. A door number below 0 or above the last door has no room.
4. Walking straight to a missing door stops the visit with an error.
5. Asking the clerk about a missing door gives the slip.
6. Whoever gets the slip must choose what to do: show a stand-in message, or tell the person.
7. The last door is the count minus 1. An empty inn has count 0, so its last door number would be -1, which is no door.

### Name it

Reading a list item with square brackets, as in `rooms[2]`, is the guest walking to the door. The Kotlin documentation says that if the index is out of range, an exception is thrown. An **exception** is an error that stops the program at that line. **Index out of bounds** means the index is outside the valid range, from 0 to the last item. The documentation's samples for a list of three items show that `elementAt(3)` throws an `IndexOutOfBoundsException` and that `getOrNull(3)` gives `null`. My own reasoning: square brackets use the same bounds, so index 3 on that list fails too.

The count is the list's `size`. The documentation says `lastIndex` is the index of the last item in the list, or -1 if the list is empty.

The clerk is **`getOrNull(index)`**. The documentation says it returns the element at the given index, or `null` if the index is out of bounds. The slip is **`null`**, the mark for no value, so the answer has a type with a question mark, such as `String?`. The operator `?:` is the **Elvis operator**. It means "use the left side, and if that is `null`, use the right side". So `rooms.getOrNull(5) ?: "no such room"` turns the slip into a message.

| Inn | In Kotlin |
|---|---|
| The inn | A list |
| A room | An item |
| A door number | An index, counted from 0 |
| The number of rooms | `size` |
| The last door | `lastIndex` |
| Walking to the door | `rooms[5]` |
| Asking the clerk | `rooms.getOrNull(5)` |
| The slip | `null` |

```kotlin
fun main() {
    val rooms = listOf("Sky", "Sea", "Sun", "Star", "Moon")
    println(rooms.size)
    println(rooms[rooms.lastIndex])
    println(rooms.getOrNull(5))
    println(rooms.getOrNull(5) ?: "no such room")
    println(rooms.getOrNull(2) ?: "no such room")
}
```

It prints `5`, `Moon`, `null`, `no such room` and `Sun`. The size is 5, so the last door is 4 and holds Moon. Door 5 has no room, so the clerk hands over the slip, which prints as `null`. The Elvis operator swaps the slip for a message. Door 2 has a room, so the message is not needed.

This next program compiles, but the guest walks into the wall at the third line.

```kotlin
fun main() {
    val rooms = listOf("Sky", "Sea")
    println("asking")
    println(rooms[2])
    println("done")
}
```

It prints `asking` and then stops with an exception. The word `done` never appears.

## Predict the output

```challenge
type: predict
run: false
id: clerk-with-stand-in
verified: kotlin-2.4.21
code: |
  fun main() {
      val names = listOf("Ana", "Ben", "Cy")
      println(names.getOrNull(1) ?: "none")
      println(names.getOrNull(9) ?: "none")
  }
options:
  - id: a
    text: "Ana, then none"
    why: "You may have counted doors from 1. Door 1 is the second room, which holds Ben."
  - id: b
    text: "Ben, then none"
    correct: true
    why: "Door 1 holds Ben, so the clerk takes the guest there. Door 9 does not exist, so the clerk's slip is replaced by the message none."
  - id: c
    text: "Ben, then null"
    why: "You may have expected the slip to print as null. The Elvis part replaces the slip with the message none."
output: |
  Ben
  none
```

```challenge
type: predict
run: false
id: door-number-equals-count
verified: kotlin-2.4.21
code: |
  fun main() {
      val names = listOf("Ana", "Ben", "Cy")
      println(names.getOrNull(names.size))
      println(names.getOrNull(names.lastIndex))
  }
options:
  - id: a
    text: "Cy, then Cy"
    why: "You may have taken the size to be the last door. The size is 3, and the last door is 2."
  - id: b
    text: "The program stops, because door 3 does not exist"
    why: "You may have expected the clerk to walk into the wall too. Asking the clerk gives a slip, not a crash."
  - id: c
    text: "null, then Cy"
    correct: true
    why: "Three rooms have doors 0 to 2, so door 3 has no room and the clerk hands over the slip. The last door is 2, which holds Cy."
output: |
  null
  Cy
```

```challenge
type: predict
run: false
id: three-from-the-end
verified: kotlin-2.4.21
code: |
  fun main() {
      val items = listOf("a", "b")
      println("before")
      println(items[items.size - 3])
      println("after")
  }
options:
  - id: a
    text: "before, then the program stops with an exception"
    correct: true
    why: "The list has two rooms, and 2 - 3 is -1. There is no door -1, so the guest walks into the wall before after is printed."
  - id: b
    text: "before, b, after"
    why: "You may have expected a negative number to count back from the end, as some other languages do. For a Kotlin list, -1 is an ordinary door number with no room behind it."
  - id: c
    text: "before, null, after"
    why: "You may have expected square brackets to hand back a slip. Only getOrNull does that."
output: |
  before
```

## Tricks to remember

- Five rooms, doors 0 to 4.
- The count is never a door. The last door is the count minus 1.
- Unsure the room exists? Ask the clerk with `getOrNull`.
- An empty inn has no door 0.

## Common mistakes

- **Asking for the door that equals the count.** Five items sound like "item 5", so `list[5]` feels right. The doors stop at 4. Use `list[list.lastIndex]` for the last item.
- **Expecting square brackets to return `null` for a missing item.** Other parts of Kotlin give `null` for "nothing here", so it feels consistent. Square brackets stop the program. Only `getOrNull` hands back the slip.
- **Throwing away the slip with `!!`.** Writing `rooms.getOrNull(5)!!` makes the red line go away. If the room is missing, the program stops again. Handle the slip with `?:` instead.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android UI. Its pager page covers a pager, which is a part of the screen that holds a fixed number of pages. A sample on that page sets the page count to 10 with `rememberPagerState(pageCount = { 10 })`, with the comment `// Display 10 items`, and the page content receives the page number as `page`. General knowledge: page numbers start at 0, so those ten pages are numbered 0 to 9.

This next part is our own reasoning: suppose the page content reads `names[page]` from a list of 8 names while the page count says 10. The person swipes to the ninth page, the code asks for door 8 in a list with doors 0 to 7, and the app closes. The reading would have to stop at the list size, or use `getOrNull`.

## Say it in an interview

**What happens when you read `list[list.size]`?**
The index is out of bounds, so an exception is thrown. The documentation says `elementAt` throws an `IndexOutOfBoundsException` for such an index. The last valid index is `size - 1`, which `lastIndex` gives. A common slip is to treat the size as the last place.

**How do you read an item that may not exist without crashing?**
Use `getOrNull(index)`. The documentation says it returns the element at the index, or `null` if the index is out of bounds. Then use `?:` to supply a stand-in, such as `list.getOrNull(9) ?: "none"`.

**Is a crash or a `null` better when the index is wrong?**
My own reasoning: it depends on who is wrong. If the index must be valid, a wrong one means a bug, and a loud crash shows it early. If a missing item is normal, such as an empty list on the first screen, `null` lets the code handle it. The risk of a stand-in is that it can hide a real bug.

**How do you safely read the last item of a list that may be empty?**
Use `list.lastOrNull()`. The documentation says it returns the last element, or `null` if the list is empty. The longer form `list.getOrNull(list.lastIndex)` also works. For an empty list, `lastIndex` is -1, which is out of bounds, so you get `null`.

## Recap

1. A list of five items has indexes 0 to 4, so the last index is the size minus 1.
2. Square brackets throw an exception for an index outside the list.
3. getOrNull returns null for such an index, and the Elvis operator turns that null into a stand-in.

## You can now

- Predict the value of getOrNull with a stand-in for an index inside and outside a list.
- Predict whether a given index, including one equal to the size, stops the program.
- Spot an index that is negative or equal to the size.
