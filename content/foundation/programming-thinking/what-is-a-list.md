---
id: foundation/programming-thinking/what-is-a-list
title: How do I keep many values under one name?
status: live
difficulty: easy
order: 17
requires: [foundation/programming-thinking/what-is-a-variable]
summary: Keep many values in a row under one name. Each one has a number, and the first one is number 0.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [post-office-boxes]
playground: false
fruit: false
upnext: false
---

## The question

A music app has five songs saved. You want the third one. Do you make five separate names, one for each song?

That gets messy when the app holds fifty songs. How do you keep many values under one name, and how do you ask for the third one?

## Mental model

Think of a wall of post-office boxes, built in one row.

- The wall is the whole collection. It has one sign on top, such as "names".
- Each box holds one item, such as a name.
- Each box has a number painted on its door. The first box is number 0. The next is number 1, then 2, and so on.
- The number on a door tells you how many boxes you walk past from the first box. The first box is 0 boxes away. The third box is 2 boxes away.
- The boxes keep their order. Box 1 always sits right after box 0.
- To fetch an item, you give the sign on the wall and a door number.

**Where the analogy stops:** A real post office can skip numbers or start at 1. You can also walk up to a door with no box behind it and see a blank wall. The row of boxes in a program has no gaps and always starts at 0. Ask for a number with no box, and the program stops with an error.

## See it move

<!-- SIM: post-office-boxes -->

**Try this:**
1. Click each number on the doors and watch which box lights up.
2. Type 3 into "Ask for number" on the wall of three boxes.
3. Press "Add a box" and ask for 3 again.

**What you should notice:** The door numbers start at 0, so the last of three boxes is door 2, and a number with no box shakes the wall.

## Build the logic

### Notice

Picture a wall of three boxes under the sign "names". Box 0 holds Ada. Box 1 holds Ben. Box 2 holds Cy.

You want what is in the third box. Look at the door numbers: 0, 1 and 2. The number 3 appears nowhere on the wall.

### Guess

You want the third box. Which door number do you ask for?

- A. Door 2, because two boxes come before it.
- B. Door 3, because it is the third box.

Idea B is how people count in daily life, so it feels right. But the door number says how many boxes you walk past from the first box. The third box has two boxes before it. Idea A is the answer.

Here is a second question. The wall has doors 0, 1 and 2. You ask for door 3. What should happen?

- A. The wall hands you the last item instead.
- B. The wall hands you nothing and the program carries on.
- C. The program stops, because no such box exists.

Idea A hides your mistake, because you get an item you did not ask for. Idea B is believable. But the program would carry on with a missing item and give wrong answers later. The Kotlin documentation says a door number that is too big causes an error. Idea C is the answer.

### Build

1. Give the whole wall one sign, a name that says what the boxes hold.
2. Put the items in the boxes in order, one item per box.
3. Paint each door with the number of boxes you walk past from the first. That gives 0, 1, 2 and so on.
4. To fetch an item, give the sign and a door number.
5. Count the boxes to know how many there are. With 3 boxes, the last door is 2. The last door is always the count minus one.
6. A door number below 0, or a number as big as the count, has no box. The program stops.

### Name it

A row of values kept under one name is a **list**. In Kotlin you make one with **`listOf`**. The Kotlin documentation says lists store items in the order they are added.

The door number is the **index**. The first item has index 0. You fetch an item by writing the list's name and the index in square brackets, like `names[1]`. The count of boxes is `names.size`. For reading an item by position, the documentation says the first has position 0 and the last has position size minus one.

```kotlin
fun main() {
    val names = listOf("Ada", "Ben", "Cy")
    println(names[0])
    println(names[2])
    println(names.size)
}
```

The first `println` prints `Ada`. The second prints `Cy`, the item behind door 2. The last prints `3`, because there are three boxes. The last door is 2, which is 3 minus 1.

Now think about `names[3]`. The program stops with an `IndexOutOfBoundsException` when it reaches that line. The Kotlin library page for `List.get` says this happens when the index is less than zero, or greater than or equal to the size. So `names[-1]` stops the program too.

## Predict the output

```challenge
type: predict
run: false
id: names-door-one
verified: not-run
code: |
  fun main() {
      val names = listOf("Ada", "Ben", "Cy")
      println(names[1])
  }
options:
  - id: a
    text: "Ada"
    why: "You may have counted the first box as 1. The first box is door 0, so door 1 is the second box."
  - id: b
    text: "Ben"
    correct: true
    why: "Door 1 is one box past the first box. That is the second box, and it holds Ben."
  - id: c
    text: "Cy"
    why: "Cy sits behind door 2. Door 1 is the second box, not the third."
output: "Ben"
```

```challenge
type: predict
run: false
id: door-three-of-three
verified: not-run
code: |
  fun main() {
      val names = listOf("Ada", "Ben", "Cy")
      println("Start")
      println(names[3])
      println("End")
  }
options:
  - id: a
    text: "Start, then it prints Cy, and then it prints End"
    why: "You may have taken door 3 to mean the third box. Door 3 would be a fourth box, and this wall has none."
  - id: b
    text: "Start, then nothing for the missing box, and then End"
    why: "You may have expected a missing box to give a blank and let the program go on. A door with no box stops the program."
  - id: c
    text: "Start, then the program stops"
    correct: true
    why: "The wall has doors 0, 1 and 2. Door 3 has no box, so the program stops at that line and End never prints. Only Start appears in the normal output."
output: "Start"
```

```challenge
type: predict
run: false
id: team-size-and-last
verified: not-run
code: |
  fun main() {
      val team = listOf("Ada", "Ben", "Cy", "Di", "Ed")
      println(team.size)
      println(team[4])
  }
options:
  - id: a
    text: "5, then Ed"
    correct: true
    why: "Five boxes, so the size is 5. Door 4 is four boxes past the first, which is the fifth box and holds Ed."
  - id: b
    text: "4, then Ed"
    why: "You may have taken the last door number as the count. The size counts boxes, so it is 5."
  - id: c
    text: "5, then Di"
    why: "You may have counted doors from 1, so door 4 became the fourth box. Door 4 is the fifth box."
output: |
  5
  Ed
```

## Tricks to remember

- The door number is how many boxes you walk past from the first.
- Three boxes have doors 0, 1 and 2.
- The last door is the count minus one.
- No door, no item. The program stops.

## Common mistakes

- **Asking for door 3 on a wall of three boxes.** It feels right because the box you want is the third one. Count the boxes you walk past instead. The third box is door 2.
- **Using the count as the last door number.** A wall of five boxes feels like it ends at 5. The count says how many boxes there are, and the last door is one less.
- **Using -1 to mean the last item.** It feels like "one back from the end". The library page says an index less than zero stops the program. The documentation gives `last()` for the last item and `first()` for the first.

## Where this shows up in Android

A chat app keeps its messages in a row, and the screen draws one row for each message. The Android documentation shows this with a function that takes `messages: List<Message>`. It also shows a function that takes `names: List<String>` and greets each name.

Picture a chat screen that closes the moment you open an empty chat. This part is my own reasoning, not the documentation's. If the code asks for the first message with `messages[0]`, an empty chat has no box 0. The program stops, and the app closes.

## Recap

1. A list keeps many values in order under one name.
2. Each item has an index, and the first index is 0.
3. Asking for an index with no item stops the program.

## You can now

- Predict which item a list index fetches.
- Spot a request for an index that is past the end of a list.
- Work out the last index of a list from its size.
