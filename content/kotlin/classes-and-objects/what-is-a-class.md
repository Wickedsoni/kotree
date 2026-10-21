---
id: kotlin/classes-and-objects/what-is-a-class
title: How do I bundle data and the actions on it into one thing?
status: live
difficulty: easy
order: 36
requires: [kotlin/functions/declaring-functions, kotlin/types-and-variables/val-vs-var]
summary: A class describes a thing once, and each object made from it holds its own data. Learn the difference between the two.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [cookie-cutter-stamp]
playground: false
fruit: false
upnext: false
---

## The question

A game has three dogs. Each dog has a name and can bark. You could keep three name variables and three separate bark steps. With 300 dogs, that falls apart. How can you describe "a dog" once, and then get as many dogs as you like, each with its own name?

## Mental model

Think of a cookie cutter and the cookies it makes.

- The cutter fixes the shape and the spots where toppings go, such as one spot for icing.
- A cookie is one real thing pressed out of dough with the cutter.
- Each cookie has its own icing. The cutter says where icing goes, and each cookie holds its own colour.
- A cookie can do things, such as "show your icing".
- Pressing the cutter into dough makes one new cookie.
- A label on the plate says which cookie you mean.
- You can eat a cookie. You cannot eat the cutter.

**Where the analogy stops:** A real cutter is a tool you can hold and press. A Kotlin class is only a description. It does nothing until the program presses it. Also, you do not eat a cookie in code. You can use the same cookie as often as you like.

## See it move

<!-- SIM: cookie-cutter-stamp -->

**Try this:**
1. Press "Stamp" three times and look at the tray.
2. Click one cookie and change its icing. Look at the other two.
3. Add a "sprinkles" spot to the cutter. The tray clears, as when a program runs again. Stamp two cookies.
4. (Optional reveal) Press "Eat the cutter".

**What you should notice:** Each press of Stamp makes a new cookie, changing one cookie leaves the others alone, and the cutter cannot be eaten.

## Build the logic

### Notice

A baker has one cutter and presses it three times. Three cookies sit on the tray. All three start with plain icing. The baker changes the icing on the second cookie to pink. The first and third cookies are still plain. The baker says "show your icing" to each cookie in turn, and each one shows its own colour. When the baker says "show your icing" to the cutter, nothing happens, because the cutter has no icing.

### Guess

Where does each cookie's icing live? Here are three ideas.

- A. On the cutter. All cookies share one icing.
- B. On each cookie. The cutter only says that icing exists.
- C. On the plate label.

Idea A feels right because the cutter is written once. But then pink icing on one cookie would change them all. Idea C confuses the label, which only says which cookie you mean, with the cookie itself. Idea B is the answer.

### Build

1. Describe the thing once: which spots it has, and what each spot starts with.
2. Describe what it can do, using those spots.
3. Press the description into dough. Each press makes one new thing that has its own spots.
4. Give each new thing a label, so you can say which one you mean.
5. To use a spot or an action, name the cookie first, then the spot or action. Naming the cutter is not enough.
6. Some spots are fixed after the cookie is made, and some may change.

### Name it

A **class** is the description, the cutter. The Kotlin documentation says you declare one with the `class` keyword and a name. A class can hold properties and functions.

An **object** is one thing made from a class, the cookie. The documentation also calls it an instance. The documentation says you make one by writing the class name followed by round brackets, like calling a function, and that Kotlin has no `new` keyword.

A **property** is a spot on the cookie, such as the icing. The documentation says the characteristics of an object are declared in properties. Properties use `val` for a spot that cannot be changed and `var` for one that can. The documentation recommends `val` unless the value must change after the object is made.

A **member function** is an action a cookie can do. The documentation says member functions are declared inside the class body, which is the curly brackets after the class name. To use a property or a member function, write the object's name, a dot, and then the property or function name.

| Cookie cutter | In Kotlin |
|---|---|
| The cutter | The class |
| One cookie | An object, or instance |
| Pressing the cutter | `Cookie()` |
| The plate label | A variable that holds the object |
| A topping spot | A property |
| "Show your icing" | A member function |
| Naming the cookie first | `pinkOne.icing` |

```kotlin
class Cookie {
    var icing = "plain"
    fun show() {
        println("icing: " + icing)
    }
}

fun main() {
    val plainOne = Cookie()
    val pinkOne = Cookie()
    pinkOne.icing = "pink"
    plainOne.show()
    pinkOne.show()
}
```

It prints `icing: plain` and then `icing: pink`. The class `Cookie` is written once. The two calls of `Cookie()` make two objects, each with its own `icing`. Inside `show`, the name `icing` means the icing of the cookie that `show` was called on.

Kotlin refuses to run this one, because it asks the cutter for an icing, and only a pressed cookie has one.

```kotlin
// does not compile
class Cookie {
    var icing = "plain"
}

fun main() {
    println(Cookie.icing)
}
```

## Predict the output

```challenge
type: predict
run: false
id: three-cookies-one-change
verified: kotlin-2.4.21
code: |
  class Cookie {
      var icing = "plain"
  }

  fun main() {
      val first = Cookie()
      val second = Cookie()
      val third = Cookie()
      first.icing = "pink"
      println(first.icing + " " + second.icing + " " + third.icing)
  }
options:
  - id: a
    text: "pink pink pink"
    why: "You may have thought the icing lives on the cutter. Each cookie has its own icing, so changing one leaves the others alone."
  - id: b
    text: "plain plain plain"
    why: "You may have thought the change is ignored. The line first.icing = pink changes the icing of that one cookie."
  - id: c
    text: "pink plain plain"
    correct: true
    why: "Three presses made three cookies, each starting plain. Only the first cookie's icing was changed."
output: |
  pink plain plain
```

```challenge
type: predict
run: false
id: show-your-icing
verified: kotlin-2.4.21
code: |
  class Cookie {
      var icing = "plain"
      fun show() { println("icing: " + icing) }
  }

  fun main() {
      val pinkOne = Cookie()
      pinkOne.icing = "pink"
      pinkOne.show()
      Cookie().show()
  }
options:
  - id: a
    text: "icing: pink and then icing: plain"
    correct: true
    why: "The first show is called on the cookie with pink icing. The second line presses a brand new cookie, which starts plain."
  - id: b
    text: "icing: pink and then icing: pink"
    why: "You may have thought the change reached the cutter. A new cookie starts from the cutter, so it begins plain."
  - id: c
    text: "icing: plain and then icing: plain"
    why: "You may have thought the change was lost. The change stays on the cookie it was made on, and show reads that cookie's icing."
output: |
  icing: pink
  icing: plain
```

```challenge
type: predict
run: false
id: fixed-spot-and-open-spot
verified: kotlin-2.4.21
code: |
  class Cookie {
      val shape = "star"
      var icing = "plain"
  }

  fun main() {
      val cookie = Cookie()
      cookie.icing = "pink"
      println(cookie.shape + " " + cookie.icing)
  }
options:
  - id: a
    text: "star plain"
    why: "You may have thought the change to icing was dropped. A var spot can be changed after the cookie is made."
  - id: b
    text: "star pink"
    correct: true
    why: "The shape spot is fixed and keeps star. The icing spot is open, so it changed to pink. The label cookie is a val, which only means it always names this cookie."
  - id: c
    text: "The program is refused, because cookie is a val"
    why: "You may have thought a val label locks the cookie. It only locks which cookie the label names, and the icing spot can still change."
output: |
  star pink
```

## Tricks to remember

- The cutter is written once. Every press makes a new cookie.
- Each cookie has its own spots.
- Name the cookie, then a dot, then the spot. Never the cutter.
- A `val` spot is fixed. A `var` spot can change.

## Common mistakes

- **Treating the class as the object.** The class name is the thing you wrote, so it feels like the thing itself. `Cookie.icing` asks the cutter, which has no icing. Press a cookie first, with `Cookie()`, and keep it in a name.
- **Expecting a change on one object to change them all.** A property feels like a setting on the class. Each object holds its own copy of each spot, so changing one leaves the others alone.
- **Writing `new` before the class name.** Some other languages need it, so it feels required. The documentation says Kotlin has no `new` keyword. Write `Cookie()`.

## Where this shows up in Android

An **activity** is the part of an Android app that stands for one screen. The Android activities page shows one written as a class: `class MyActivity : ComponentActivity()` with a function called `onCreate` inside. This lesson does not cover the part after the colon. The page says that apps are not started from a `main` function. Instead, the Android system starts code in an activity by calling specific functions that match stages of its life. It says `onCreate` fires when the system creates your activity.

So you write the cutter for a screen and never write `MyActivity()` yourself. This next part is my own reasoning: the system presses the cutter and hands you the cookie. That is why you describe the screen once and the system makes the object when it needs one.

## Say it in an interview

**What is the difference between a class and an object?**
A class is the description of a kind of thing, with its properties and functions. An object, also called an instance, is one thing made from that class. One class can make many objects, and each object holds its own property values.

**Do you need `new` to make an object in Kotlin?**
No. The Kotlin documentation says you write the class name followed by round brackets, like a function call, and that there is no `new` keyword.

**Should a property be `val` or `var`?**
The Kotlin tour recommends `val` unless the value has to change after the object is made. My own reasoning: a `val` property cannot be reassigned, so fewer lines can change it behind your back. The trade-off is that when a value really changes, such as a score, it needs `var`.

**What is a member function, and how does it know which object's data to use?**
A member function is a function declared inside the class body. The documentation says to call it with a dot after an object. It works on the object you called it on, so `pinkOne.show()` shows the icing of `pinkOne`. This is general knowledge, shown by the Predict item above.

## Recap

1. A class describes a kind of thing once, and an object is one real thing made from it.
2. Each object holds its own property values, and a change to one leaves the others alone.
3. You use a property or member function through an object, never through the class name.

## You can now

- Predict what three objects from one class hold after one of them is changed.
- Predict what a member function shows for each object it is called on.
- Predict which property of an object can be changed and which cannot.
