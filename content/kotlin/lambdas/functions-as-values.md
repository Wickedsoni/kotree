---
id: kotlin/lambdas/functions-as-values
title: How can a function be stored in a variable?
status: shadow
difficulty: medium
order: 31
requires: [kotlin/functions/declaring-functions]
summary: A function can sit in a variable, like a number. Learn the type that describes it and how to store it without running it.
video: ""
publishedOn:
updatedOn:
sims: [recipe-card-handoff]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://kotlinlang.org/docs/lambdas.html ; https://kotlinlang.org/docs/kotlin-tour-functions.html ; https://developer.android.com/develop/ui/compose/kotlin -->
## The question

A friend asks you to bring a dessert to a party. You do not bake it now. You write the steps on a card and hand the card over, and your friend bakes it when the guests arrive. Can a program do the same with a piece of code? Can it hand a set of steps to another part of the program and let that part choose when to run them?

## Mental model

Think of a recipe card.

- The card lists steps. It is not the dish.
- Cooking is following the steps. The dish is what you get when you finish.
- The top of the card has a header: what the cook must bring, and what the dish will be. For example: "needs: a name, makes: a greeting".
- A pocket holds one card. The pocket has a label that says which kind of card fits.
- Handing the card to a friend moves the card. Nothing is cooked on the way.
- The friend cooks when they choose, and may cook the same card twice with different ingredients.

**Where the analogy stops:** A paper card is one thing in one place. In Kotlin a card is a value like a number, so it can sit in a pocket, move to another pocket or be handed to other code. Also, cooking never uses a card up. The same card can be cooked any number of times.

## See it move

<!-- SIM: recipe-card-handoff -->

**Try this:**
1. Drag the card from Mia's pocket into Sam's pocket. Look at the dish counter.
2. Press "Cook with Ana", then "Cook with Bo". Look at the counter again.
3. (Optional reveal) Tick "Cook while handing over" and drag the card back to Mia.

**What you should notice:** Moving the card cooks nothing, each press of "Cook" makes one dish, and the card stays in the pocket afterwards.

## Build the logic

### Notice

Mia has a card with the header "needs: a name, makes: a greeting". She does not cook. She puts the card in her pocket, then hands it to Sam. The dish counter still reads 0. Sam cooks the card with the name Ana and gets "Hello, Ana". He cooks it again with the name Bo and gets "Hello, Bo". The counter reads 2, and Sam still holds the card.

### Guess

When Mia hands Sam the card, when do the steps run? Here are three ideas.

- A. When the whole program starts.
- B. At once, as the card leaves her hand.
- C. Only when Sam cooks it.

Idea B feels right because handing something over sounds like an action. But the card is only steps on paper. Idea A mixes up writing the card with using it. Idea C is the answer.

### Build

1. A card has a header: what it needs and what it makes.
2. Writing the card's name and cooking the card are two different things.
3. To cook, you say the card's name and put the ingredients in brackets.
4. To hand the card over without cooking, you point at its name with a special mark and leave off the brackets.
5. A pocket for a card needs a label with the same header: what goes in, what comes out.
6. Cooking twice makes two dishes. The card is the same afterwards.

### Name it

A **function type** is the header of a card, written as a type. The Kotlin documentation says `(A, B) -> C` is the type of functions that take two arguments of types `A` and `B` and give back a value of type `C`. So `(String) -> String` is a card that needs one piece of text and makes one piece of text. The documentation says the `Unit` return type cannot be left out in a function type. **`Unit`** is the type for "nothing useful", so a card that makes nothing useful has the header `() -> Unit`.

A **function reference** points at a function that already exists, without cooking it. The documentation says to write `::functionName` to pass or store a named function as a value. It names two places where such a function can be written: straight in the file, or inside another function. To cook a stored card, write the pocket's name and the ingredients in brackets. The documentation says `f(x)` and `f.invoke(x)` both work.

Kotlin treats a function as a value that you can store, hand to other code and give back. People call these first-class functions (general knowledge).

| Recipe card | In Kotlin |
|---|---|
| The card | A function |
| The header | The function type |
| The pocket and its label | A variable with a function type |
| Pointing at the card without cooking | `::greet` |
| Cooking the card | `card("Ana")` |
| The dish | The value the call gives back |

```kotlin
fun greet(name: String): String {
    println("cooking")
    return "Hello, $name"
}

fun main() {
    val card: (String) -> String = ::greet
    println("card stored")
    println(card("Ana"))
    println(card("Bo"))
}
```

It prints `card stored`, `cooking`, `Hello, Ana`, `cooking` and `Hello, Bo`. Storing the card prints nothing from `greet`. Each call of `card` cooks it once, so `cooking` appears twice.

Kotlin refuses to run this one, because the header promises a number while `greet` makes text.

```kotlin
// does not compile
fun greet(name: String): String {
    return "Hello, $name"
}

fun main() {
    val card: (String) -> Int = ::greet
    println(card("Ana"))
}
```

## Predict the output

```challenge
type: predict
run: false
id: stored-not-cooked
verified: kotlin-2.4.21
code: |
  fun task() {
      println("run")
  }

  fun main() {
      val saved = ::task
      println("stored")
      saved()
      saved()
  }
options:
  - id: a
    text: "run, stored, run, run"
    why: "You may have thought storing the card cooks it. The pointer ::task only points at the card, so nothing runs on that line."
  - id: b
    text: "stored, run, run"
    correct: true
    why: "Storing the card cooks nothing. The two calls with brackets cook it twice, so run appears two times after stored."
  - id: c
    text: "stored"
    why: "You may have thought a stored card never gets cooked. Each saved() with brackets is a cooking."
output: |
  stored
  run
  run
```

```challenge
type: predict
run: false
id: swap-the-card
verified: kotlin-2.4.21
code: |
  fun twice(x: Int): Int {
      return x * 2
  }

  fun square(x: Int): Int {
      return x * x
  }

  fun main() {
      var card: (Int) -> Int = ::twice
      println(card(5))
      card = ::square
      println(card(5))
  }
options:
  - id: a
    text: "10 and then 10"
    why: "You may have thought the pocket keeps the first card for ever. A var pocket can hold a different card of the same header."
  - id: b
    text: "The program does not compile, because card already holds a function"
    why: "You may have expected a pocket to take one card only. Both cards have the header (Int) -> Int, so either fits."
  - id: c
    text: "10 and then 25"
    correct: true
    why: "The first call cooks twice with 5 and gives 10. The pocket then holds square, and the second call cooks that card and gives 25."
output: |
  10
  25
```

```challenge
type: predict
run: false
id: length-of-the-dish
verified: kotlin-2.4.21
code: |
  fun label(n: Int, thing: String): String {
      return "$n $thing"
  }

  fun main() {
      val card: (Int, String) -> String = ::label
      val dish = card(3, "eggs")
      println(dish.length)
  }
options:
  - id: a
    text: "6"
    correct: true
    why: "The header says the dish is text. Cooking with 3 and eggs makes the text 3 eggs, which has 6 characters counting the space."
  - id: b
    text: "2"
    why: "You may have counted the ingredients the card needs. The length belongs to the dish, which is the text that comes out."
  - id: c
    text: "3"
    why: "You may have read the first ingredient as the dish. The dish is the whole text the steps make."
output: |
  6
```

## Tricks to remember

- A name with brackets cooks. A name with `::` carries the card.
- The function type is the card's header: what goes in, what comes out.
- Cooking never uses up a card.
- A `() -> Unit` card needs nothing and makes nothing useful.

## Common mistakes

- **Writing `val f = greet("Ana")` and expecting a stored card.** It reads like "f is greet", so it feels right. The brackets cook the card at once, and `f` holds the dish. Write `::greet` to keep the card.
- **Leaving `Unit` out of a function type.** A function declaration lets you skip `Unit`, so a type feels the same. The documentation says it cannot be left out here. Write `() -> Unit`.
- **Thinking a stored function runs when it is stored.** Storing sounds like doing. The card only runs when you cook it with brackets.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its Kotlin page says that the `Button` function, which draws a button, has an `onClick` parameter whose value is a function. The button calls that function when the user clicks. The page's first example passes a function that is defined elsewhere, with `onClick = myClickFunction`.

That is the card in the pocket: the button holds the card and cooks it on a tap. This next part is my own reasoning. If you wrote `onClick = handleTap()` with brackets, you would cook the card now and hand the button a dish instead of a card. Kotlin would refuse, because the button wants a function and gets a result.

## Say it in an interview

**What does the type `(Int) -> String` mean?**
It is the type of a function that takes one `Int` and gives back a `String`. The Kotlin documentation writes it as the parameter types in brackets, then an arrow, then the return type. A function that gives back nothing useful uses `Unit`, and the documentation says `Unit` cannot be left out in a function type.

**How do you pass an existing function as a value?**
You write `::name`, which the documentation calls a function reference. It works for a named function written in the file or inside another function. The function is not run when you write it. It runs only when someone calls it with `f(x)` or `f.invoke(x)`.

**What is the difference between `val f = ::greet` and `val f = greet("Ana")`?**
The first stores the function and runs nothing. The second runs `greet` at once and stores the result. People mix them up because both read like "f is greet".

**Why would you keep a function in a variable?**
So the code that decides when to run it can be different from the code that wrote it. The Compose page shows this: a button holds a click function and calls it on a tap. My own reasoning: the trade-off is that a reader has to follow where the call really happens.

## Recap

1. A function type such as `(Int) -> String` describes what a function takes and gives back.
2. Writing `::name` stores or passes a function without running it.
3. A stored function runs only when you call it with brackets.

## You can now

- Predict how many times a stored function runs.
- Predict what a variable gives when it is switched to another function with the same type.
- Predict the type and length of the value a stored function gives back.
