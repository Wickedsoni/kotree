---
id: kotlin/collections/list-vs-mutable-list
title: Why can't I add an item to a List?
status: live
difficulty: easy
order: 25
requires: [kotlin/types-and-variables/val-vs-var]
summary: Kotlin has lists you can only read and lists you can change. Learn which is which, and why val does not decide.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [menu-board-cover]
playground: false
fruit: false
upnext: false
---

## The question

A café app shows today's menu as a row of dishes. One part of the code hands the menu to another part, and that part tries to add a new dish. Kotlin refuses to run it. A different row in the same file accepts the same request without a complaint. Why can one row take a new dish and the other cannot?

## Mental model

Think of the menu board in a café.

- The board on the wall is the list. Dishes are written in order, numbered from 0.
- The owner holds the marker. With it she can add a dish, wipe one off or rewrite one.
- A guest looks at the board through a clear plastic cover. Through the cover the guest can read any dish, count the dishes and check whether tea is on the board. The guest cannot write.
- The hook on the wall is the name your code uses for the board.
- A photo of the board is a copy taken at one moment.

The cover takes the marker out of the guest's hand. It does not freeze the board.

**Where the analogy stops:** A real cover is a separate sheet laid on one board. In Kotlin, the cover is only the kind of name you write for the board, so the same board can have a covered name and a marker name at once. Also, a list made with `listOf` is handed out with its cover already on.

## See it move

<!-- SIM: menu-board-cover -->

**Try this:**
1. Press "Add a dish" on the board that has only a cover. Then press it on the board that has a marker.
2. Hang the cover over the marker board. Press "Owner adds a dish" and watch the guest's view.
3. Press "Take a photo" and let the owner add another dish. Compare the photo with the board.

**What you should notice:** The cover blocks writing but the board behind it still changes and the guest sees the change, while a photo stays as it was when it was taken.

## Build the logic

### Notice

A café board lists soup, salad and tea. The owner holds the marker. A guest reads the board through a cover. The guest asks how many dishes there are and gets 3. The guest tries to add cake, and the cover blocks it. The owner then adds cake with the marker. The guest, still looking through the cover, now counts 4 dishes.

### Guess

The guest's cover blocks writing. What does that say about the board behind it? Here are three ideas.

- A. Nothing behind a cover can change.
- B. The board can still change, and the guest sees the change.
- C. The cover shows a frozen picture, so the guest still sees 3 dishes.

Idea A feels right because "covered" sounds like "locked". Idea C feels right because a guest cannot write. But a cover is a window, not a picture. Idea B is the answer.

### Build

1. A board holds dishes in order, numbered from 0.
2. Whoever holds a marker may add, wipe or rewrite dishes.
3. Whoever has only a cover may read, count and search.
4. A cover removes one person's marker. It does not stop the owner, so the board can still change.
5. The hook says which board hangs on the wall. Hanging a different board on the hook is a different question from writing on the board.
6. To hand someone a board that will not change under them, take a photo and hand over the photo.

### Name it

The Kotlin documentation says each collection type comes as a pair of types, called **interfaces**. One is read-only, for accessing the elements. The other adds write operations such as adding, removing and updating.

**`List`** is the read-only one. The documentation says it stores elements in a specified order and provides indexed access to them. **`MutableList`** is a `List` with write operations, so it has `add`. The function `listOf(...)` creates a new read-only list. The function `mutableListOf(...)` creates one you can change. The mark `List<String>` means a list of text values.

**Read-only** means you cannot change the list through this name. It does not mean the list can never change. The documentation shows a `MutableList` named `sourceList` and a `List` name `referenceList` that points at the same list. After an item is added through `sourceList`, printing `referenceList` shows the current state of `sourceList`.

The hook is `val` or `var`. The documentation says write operations on a mutable collection are still possible even if it is assigned to a `val`, while giving the name a different list is a compile error.

The photo is **`toList()`**. The documentation says copying functions such as `toList()` create a snapshot of a collection at a specific moment. If you add or remove elements from the original afterwards, this does not affect the copy.

| Café | Kotlin |
|---|---|
| The board | The list |
| The marker | Write operations such as `add` |
| The clear cover | A `List` name |
| The owner's name for the board | A `MutableList` name |
| The hook | `val` or `var` |
| The photo | `toList()` |

```kotlin
fun main() {
    val board = mutableListOf("soup", "salad", "tea")
    val cover: List<String> = board
    board.add("cake")
    println(cover.size)
    println(cover[3])
    val photo = cover.toList()
    board.add("pie")
    println(photo.size)
    println(cover.size)
}
```

It prints `4`, `cake`, `4` and `5`. Both names point at one list, so `cover` sees the cake. The photo is taken when the list has 4 dishes. The pie goes onto the board only, so the photo stays at 4 and the cover sees 5.

Kotlin refuses to run this one, because a `List` created by `listOf` has no `add` to call.

```kotlin
// does not compile
fun main() {
    val menu = listOf("soup", "salad")
    menu.add("tea")
}
```

## Predict the output

```challenge
type: predict
run: false
id: val-hook-still-writes
verified: kotlin-2.4.21
code: |
  fun main() {
      val tea = mutableListOf("green")
      tea.add("black")
      println(tea.size)
      println(tea)
  }
options:
  - id: a
    text: "1, then [green]"
    why: "You may have thought a val hook locks the board. The hook only decides which board hangs there, and the marker still works."
  - id: b
    text: "The program does not compile, because tea is a val"
    why: "You may have expected Kotlin to refuse writing under a val. Only hanging a different board on the hook is refused."
  - id: c
    text: "2, then [green, black]"
    correct: true
    why: "The owner holds the marker, so add writes a new dish on the board. The hook never moved."
output: |
  2
  [green, black]
```

```challenge
type: predict
run: false
id: cover-sees-the-change
verified: kotlin-2.4.21
code: |
  fun main() {
      val board = mutableListOf("soup")
      val cover: List<String> = board
      board.add("tea")
      println(cover.size)
  }
options:
  - id: a
    text: "1"
    why: "You may have thought the cover shows a frozen picture. A cover is a window onto the live board."
  - id: b
    text: "2"
    correct: true
    why: "The cover and the owner's name point at the same board. When the owner adds tea, the guest looking through the cover counts two dishes."
  - id: c
    text: "The program does not compile"
    why: "You may have expected a clash between the two kinds of name. A mutable list has everything a read-only list has, so a covered name may point at it."
output: |
  2
```

```challenge
type: predict
run: false
id: photo-stays-old
verified: kotlin-2.4.21
code: |
  fun main() {
      val board = mutableListOf("a", "b")
      val photo = board.toList()
      board.add("c")
      println(photo.size)
      println(board.size)
  }
options:
  - id: a
    text: "2, then 3"
    correct: true
    why: "The photo was taken when the board held two dishes, so it stays at 2. The board itself grew to 3."
  - id: b
    text: "3, then 3"
    why: "You may have expected the photo to follow the board. A photo is a moment in time, and it does not update."
  - id: c
    text: "2, then 2"
    why: "You may have thought the board stops changing once a photo exists. The owner still holds the marker, so c was added."
output: |
  2
  3
```

## Tricks to remember

- The cover takes away the marker. It does not freeze the board.
- `val` locks the hook, not the board.
- `List` reads. `MutableList` reads and writes.
- `toList()` takes a photo, and a photo never updates.

## Common mistakes

- **Believing that `val` makes a list unchangeable.** The word `val` sounds like "never changes", so it feels right. It locks which board hangs on the hook. A `mutableListOf` list under a `val` can still take `add`. Choose `List` or `MutableList` for what you want to allow.
- **Believing that a `List` can never change.** The name `List` sounds final. It is read-only through that name only. Whoever keeps a `MutableList` name for the same board can still write. If you need a board that will not change under you, take a photo with `toList()`.
- **Changing `val` to `var` to make `add` work.** It feels like the fix for any refusal. A `var` lets you hang a different board on the hook, and a `listOf` board still has no `add`. Use `mutableListOf` when you need to write.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android UI. A Compose screen draws itself from **state**, which is a value the screen reads and redraws when it changes. The Compose state page has a caution: using mutable objects such as `ArrayList<T>` or `mutableListOf()` as state causes your users to see incorrect or stale data. It explains that such objects are not observable by Compose and do not trigger a recomposition, which is a redraw, when they change. It recommends `State<List<T>>` and the immutable `listOf()`.

The page calls `listOf()` immutable. This lesson says read-only, because a `List` name can point at a list that someone else changes. This next part is our own reasoning: a person taps "Add dish", the code writes to a `mutableListOf` list, and the new dish does not appear on the screen. The board changed, but nothing told the screen to look again.

## Say it in an interview

**Is a Kotlin `List` immutable?**
No, it is read-only. The documentation shows a `List` name that sees the changes made through a `MutableList` name for the same list. If you need a copy that will not change, `toList()` creates a snapshot. The cost is one copy of the list, and the items inside are shared.

**What is the difference between `val list = mutableListOf(...)` and `var list = listOf(...)`?**
The first hook is fixed, but the board can be written on. The second hook can hang a different board, but each board is read-only. The documentation says write operations on a mutable collection are possible under a `val`, while giving the name a different list is an error.

**Why does Kotlin split `List` and `MutableList` instead of having one type?**
The documentation says the read-only collection types are covariant. In plain words, if every `Rectangle` is a `Shape`, a `List<Rectangle>` can be used where a `List<Shape>` is required. It says mutable collections are not, because that would lead to runtime failures. My own reasoning: nobody can add a circle through a read-only name, so the swap is safe, but a mutable list of rectangles could be handed a circle.

**Should a function return `List` or `MutableList`?**
My own reasoning: return `List`, so callers do not get a marker. The trade-off is that the owner may still change the list behind it. Return `toList()` when callers need a stable snapshot, and accept the cost of the copy.

## Recap

1. A List can only be read, and a MutableList can also be written.
2. The val keyword locks the name, not the list, so a mutable list under val can still change.
3. A List name sees later changes to its list, and toList() makes a copy that stays fixed.

## You can now

- Predict whether a call to add works on a given list name.
- Predict what a read-only name shows after the mutable list behind it changes.
- Predict the sizes after a toList() copy is taken and the original grows.
