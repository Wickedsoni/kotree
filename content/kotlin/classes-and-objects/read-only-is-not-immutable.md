---
id: kotlin/classes-and-objects/read-only-is-not-immutable
title: Can a read-only list still change under me?
status: live
difficulty: medium
order: 43
requires: [kotlin/classes-and-objects/objects-are-references, kotlin/collections/list-vs-mutable-list]
summary: A read-only list stops you from changing it, not everyone else. See how it can change under you and how a copy stops that.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [shop-window-restock]
playground: false
fruit: false
upnext: false
---

## The question

A shop class shows its list of stock to the rest of the program. Another part of the code reads the list and prints 2 items. A moment later, with nobody touching that part of the code, it prints 3. The list was marked as one that cannot be changed. How did it change?

## Mental model

Think of a shop with a front window and a stock room.

- The stock room holds the real items. It is the list itself.
- The front window shows the stock room through glass. A passer-by can look, count and search, but cannot open the glass.
- The back door lets staff move things in the stock room. Whoever has the back door can change the stock room.
- A photo of the window is a copy of what it showed at one moment.
- A display glued down, with no back door at all, never changes for anyone.
- The sign above the window says which shop it shows.

**Where the analogy stops:** A real window sits between you and a separate room. In code, the window is only a kind of name for the very same list, so one list can have a window name and a back-door name at once. Also, the glass shows what you may do with your name. It does not tell you whether somebody else holds the back door.

## See it move

```sim
id: shop-window-restock
template: compare
title: A shop window for the customer and a back door for staff
intro: The stock room holds 2 items. The customer sees them through the front window, and staff can reach the stock room through the back door. No photo has been taken.
sides:
  - id: customer
    label: Customer side (the window)
    boxes:
      - { id: view, label: "Customer's view: window shows 2 items" }
      - { id: photo, label: "Photo: none taken" }
  - id: staff
    label: Staff side (the back door)
    boxes:
      - { id: stock, label: "Stock room: 2 items" }
vars: { items: 2, viewing: window }
actions:
  - id: try-open
    label: Customer tries to open the window
    do:
      - light: [ customer.view ]
      - mark: { customer.view: bad }
      - say: Refused. The window is only a read-only name for the stock room, so the customer cannot open it to add or remove items.
  - id: restock
    label: Staff restock from behind
    cases:
      - when: { var: { viewing: window } }
        do:
          - add: { items: 1 }
          - label:
              staff.stock: "Stock room: {items} items"
              customer.view: "Customer's view: window shows {items} items"
          - light: [ staff.stock, customer.view ]
          - say: Staff restock from behind. The stock room and the window both show {items} items.
      - when: { var: { viewing: photo } }
        do:
          - add: { items: 1 }
          - label:
              staff.stock: "Stock room: {items} items"
          - light: [ staff.stock ]
          - say: Staff restock. The stock room has {items} items, but the customer is looking at the photo, so their view does not change.
  - id: photo
    label: Take a photo of the window
    do:
      - label:
          customer.photo: "Photo: {items} items (frozen)"
      - mark: { customer.photo: good }
      - light: [ customer.photo ]
      - say: A photo of the window is taken. It shows {items} items and does not change when the stock room does.
  - id: hand-photo
    label: Hand over the photo, not the window
    cases:
      - when: { mark: { customer.photo: good } }
        do:
          - set: { viewing: photo }
          - label:
              customer.view: "Customer's view: the photo, {items} items (frozen)"
          - light: [ customer.view ]
          - say: The customer is handed the photo, not the window, so later restocking cannot reach what they see.
    refuse: Take a photo of the window first, then hand it over.
script: [ try-open, restock, photo, restock, hand-photo, restock ]
try:
  - text: Press "Customer tries to open the window". Then press "Staff restock from behind" and read the count in the window.
    done: [ try-open, restock ]
  - text: Press "Take a photo of the window", then let staff restock again. Compare the photo with the window.
    done: [ photo, restock ]
  - text: (Optional reveal) Press "Hand over the photo, not the window". Let staff restock again and watch the customer's view.
    done: [ hand-photo, restock ]
    optional: true
```

**What you should notice:** The glass blocks the customer from changing the display. Staff behind it still can, and the customer sees every change. A photo stays as it was.

## Build the logic

### Notice

A shop window shows a hat and a scarf. A street artist is asked to draw it and counts 2 items. The artist cannot open the glass. A worker comes in through the back door and adds a coat. The artist looks up and counts 3. Now the shop owner hands a second artist a photo of the window instead. That artist counts 2 and still counts 2 after the coat arrives.

### Guess

The artist cannot open the glass. Is the display guaranteed not to change while the artist draws? Here are three ideas.

- A. Yes, glass means nothing can change.
- B. Only if the shop is closed.
- C. No, staff behind the glass can still change it.

Idea A feels right because the glass stops the artist from touching anything. But the glass only stops the artist. Idea B is not part of the rule at all. Idea C is the answer.

How can the shop give someone a display that will not change under them? Here are three ideas.

- A. Hand over a photo of the window.
- B. Hand over the window.
- C. Ask the artist to promise to ignore any change.

Idea B shows every change. Idea C depends on the artist's memory. Idea A is the answer.

### Build

1. The stock room holds the real items.
2. Whoever holds the back door can change the stock room.
3. A window is a way to look at the stock room. The glass stops the looker from changing it.
4. The glass does not stop staff, so what the window shows can change under the looker.
5. To give someone a display that stays put, give them a photo. Later changes to the stock room do not reach a photo.
6. Only a display glued down, with no back door at all, can never change. Glass alone does not promise that.
7. The sign above the window says which shop it shows. Putting up a different sign is not the same as rearranging the shop.

### Name it

The Kotlin documentation says each collection type comes as a pair of **interfaces**, which are two lists of what you may do with a collection. A **read-only** one lets you access the elements, and a **mutable** one, meaning one that can be changed, adds write operations. So read-only means you cannot change the list through this name. It does not say that nobody can.

The documentation also says a mutable collection can still be written to when it is assigned to a `val`. The `val` is the sign above the window. It protects the sign, not the stock.

**Immutable** means the list cannot change at all, through any name (general knowledge). The Compose page on Android calls `listOf()` immutable. My own reasoning: the type `List` promises only that you cannot write through it, so keep the two words apart.

The documentation says copying functions such as `toList()` create a snapshot at a specific moment, and later changes to the original do not affect the copy. It also says that when you assign an existing collection to a new variable, changes made through one reference show in all of them. The photo has one more limit. The documentation's example shows that a copy holds the same objects as the original, so a change to one of those objects shows in both.

In the next programs, the word `private` in front of a property means only code inside the class may use it (general knowledge). The stock room has a locked back door.

This class shows its window, and so leaks its stock room.

```kotlin
class Shop {
    private val stock = mutableListOf("hat", "scarf")
    val window: List<String> = stock

    fun restock(item: String) {
        stock.add(item)
    }
}

fun main() {
    val shop = Shop()
    val seen = shop.window
    println(seen.size)
    shop.restock("coat")
    println(seen.size)
}
```

It prints `2` and then `3`. The property `window` is a `List`, so the caller cannot add to it. But it is the same list as `stock`, so `restock` changes what the caller sees.

This class hands out a photo instead.

```kotlin
class Shop {
    private val stock = mutableListOf("hat", "scarf")

    fun window(): List<String> {
        return stock.toList()
    }

    fun restock(item: String) {
        stock.add(item)
    }
}

fun main() {
    val shop = Shop()
    val seen = shop.window()
    shop.restock("coat")
    println(seen.size)
    println(shop.window().size)
}
```

It prints `2` and then `3`. The first caller holds a photo from before the coat arrived. A new call takes a new photo.

Kotlin refuses to run this one, because the glass blocks adding.

```kotlin
// does not compile
fun main() {
    val stock = mutableListOf("hat")
    val window: List<String> = stock
    window.add("coat")
}
```

## Predict the output

```challenge
type: predict
run: false
id: window-follows-the-stock-room
verified: kotlin-2.4.21
code: |
  fun main() {
      val stock = mutableListOf("hat", "scarf")
      val window: List<String> = stock
      stock.add("coat")
      println(window.size)
      println(window)
  }
options:
  - id: a
    text: "2, then [hat, scarf]"
    why: "You may have thought the glass shows a frozen picture. A window shows the stock room as it is now."
  - id: b
    text: "The program is refused, because stock changed after window was made"
    why: "You may have expected the glass to forbid any change. It only stops the looker. Staff with the back door may still add."
  - id: c
    text: "3, then [hat, scarf, coat]"
    correct: true
    why: "The window and the stock room are the same list. Staff added the coat, so the window shows it."
output: |
  3
  [hat, scarf, coat]
```

```challenge
type: predict
run: false
id: photo-versus-window
verified: kotlin-2.4.21
code: |
  fun main() {
      val stock = mutableListOf("hat", "scarf")
      val photo = stock.toList()
      val window: List<String> = stock
      stock.add("coat")
      println(photo.size)
      println(window.size)
  }
options:
  - id: a
    text: "2, then 3"
    correct: true
    why: "The photo was taken before the coat arrived and never updates. The window looks at the stock room, so it shows 3."
  - id: b
    text: "3, then 3"
    why: "You may have thought a photo follows the shop. A copy is a separate list from the moment it is made."
  - id: c
    text: "2, then 2"
    why: "You may have thought the window freezes too. The window is another name for the stock room, not a picture of it."
output: |
  2
  3
```

```challenge
type: predict
run: false
id: shop-hands-out-a-photo
verified: kotlin-2.4.21
code: |
  class Shop {
      private val stock = mutableListOf("hat")

      fun window(): List<String> {
          return stock.toList()
      }

      fun restock(item: String) {
          stock.add(item)
      }
  }

  fun main() {
      val shop = Shop()
      val seen = shop.window()
      shop.restock("coat")
      println(seen.size)
      println(shop.window().size)
  }
options:
  - id: a
    text: "2, then 2"
    why: "You may have thought seen follows the stock room. It holds a photo taken before the coat arrived."
  - id: b
    text: "1, then 2"
    correct: true
    why: "seen is a photo of one hat. The coat arrived afterwards, and the second call to window takes a new photo with two items."
  - id: c
    text: "1, then 1"
    why: "You may have thought the shop's stock never changes. Staff can still restock through the shop's own function."
output: |
  1
  2
```

## Tricks to remember

- Glass stops the looker. It does not stop the staff.
- `val` is the sign above the window. It does not lock the stock room.
- Want a display that stays put? Hand over a photo with `toList()`.
- On your own machine, make a class that keeps a `MutableList` private and shows it as a `List` property. Add an item through the class, and print the shown list before and after.

## Common mistakes

- **Believing a read-only list never changes.** The word sounds like a lock. It only describes your name for the list. Another name may still write to it.
- **Thinking `val` freezes the list.** It feels like "value", so it feels fixed. The documentation says a mutable collection in a `val` can still be written to. Use `toList()` when you need a fixed picture.
- **Showing the window when you meant to hand over a photo.** Typing a `List` type on a property feels like protection. The same list sits behind it. Return `toList()` from a function when callers must not see later changes.
- **Thinking a photo freezes the items inside.** The list is copied, so it feels fully copied. The documentation's example shows the copy holds the same objects as the original, so a change to one of those objects shows in both.

## Where this shows up in Android

**Compose** is the Android toolkit for describing screens in Kotlin code (general knowledge). The Android Compose state page has a caution. It says that using mutable objects such as `ArrayList<T>` or `mutableListOf()` as state in Compose causes users to see incorrect or stale data. It says Compose does not observe those objects and so a change does not trigger a redraw. It recommends a holder that tells the screen when its value changes, such as `State<List<T>>`, together with the immutable `listOf()`.

This lesson shows the other side of the same problem. The list on the screen is a window onto a stock room that staff can change. My own reasoning: a screen can show old items because nobody told it that the stock changed, and a second screen can show a list that changed under it.

The Android architecture recommendations page says that for simple cases it is acceptable to use a `MutableStateFlow` exposed as an immutable `StateFlow`. A `StateFlow` is a holder for a value that a screen can watch, and the `MutableStateFlow` is its changeable form. A `ViewModel` is a class that holds a screen's data (both general knowledge). The shop with its back door private and a window public is the same shape.

## Say it in an interview

**Can a read-only collection still change?**
Yes. The Kotlin documentation says each collection type has a read-only interface and a mutable one that adds write operations. A `List` name stops you from writing, but the same list can have a `MutableList` name held elsewhere. The documentation says new references to an existing collection see changes made through any of them.

**What is the difference between `val` and an immutable collection?**
The documentation says a mutable collection assigned to a `val` can still be written to, and the `val` protects the reference. So `val` fixes which list the name points at. Immutable means the list cannot change at all (general knowledge). For a real one, the `kotlinx.collections.immutable` library offers `persistentListOf` and `toPersistentList()`. Its README calls it experimental, so its API may change.

**Why does a class copy a list before it hands it out?**
My own reasoning: a copy cannot change under the caller, and the caller cannot change the class's data by accident. The trade-off is a copy each time, which costs memory and time for big lists. The documentation says `toList()` makes a snapshot, and that the copy still holds the same objects.

**What goes wrong when a `ViewModel` shows a read-only list that is backed by a mutable one?**
My own reasoning: any code that holds the mutable one can change what the screen reads, without the screen being told. The Compose page says changes to non-observable mutable objects do not trigger a redraw, so users can see stale data. Keep the mutable part private and share an observable, read-only view.

## Recap

1. A read-only name stops you from changing a list, but not whoever holds a changeable name.
2. A `val` fixes which list a name points at and does not freeze the list.
3. A copy from `toList()` is a snapshot that later changes do not reach.

## You can now

- Predict what a read-only name shows after the changeable list behind it grows.
- Predict the sizes seen through a photo and through a window after an item is added.
- Predict what a caller sees when a class hands out a copy instead of its own list.
