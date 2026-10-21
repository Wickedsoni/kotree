---
id: kotlin/classes-and-objects/model-a-small-program
title: How do I turn a real-world problem into classes and functions?
status: shadow
difficulty: medium
order: 44
requires: [kotlin/classes-and-objects/private-and-public, kotlin/lambdas/map-filter-and-friends]
summary: Plan a small program from a story. See how to pick classes and give each action to the class that holds its data.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: true
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live; read 21 Oct 2026):
- https://developer.android.com/topic/architecture (separation of concerns; UI layer and data layer roles)
- docs/practice/interview-kotlin.md seed for this leaf. Composition and inheritance points are labelled general knowledge or my own reasoning.
The roadmap says no sim: a design skill. The roadmap's F and B tasks are not built: playground is false. fruit: true is from the roadmap, and the page has no capstone link.
Code not compiled: no shell in this session. Outputs hand-traced twice. Predict items are run: false, verified: not-run.
-->

## The question

A school tuck shop asks for a small program. Pupils put snacks in a basket, and the screen shows the total price and the names of the snacks that cost more than 2. You open an empty file. Where do you start? How do you decide what becomes a class, what becomes a value, and which class does each action?

## Mental model

Think of planning a kitchen before you build it.

- The kitchen plan is your design. It exists before any cooking.
- A station, such as the fridge or the oven, is a class. It has its own stuff and its own job.
- What a station holds, such as the eggs in the fridge, is a property.
- What a station does, such as the oven baking, is a function.
- The cook who walks between the stations is the main function. The cook asks, and does not rummage.
- A locked cupboard that only the station's owner opens is a private part.
- A giant all-in-one machine does every job in one box.

**Where the analogy stops:** A real kitchen is costly to rebuild, so people plan first. Code is cheaper to change, but a tangled design still costs you every time you add a feature. Also, from one oven design you can make as many ovens as you like. A real kitchen would not.

## Build the logic

### Notice

The story says: "A pupil picks snacks. Each snack has a name and a price. The pupil puts snacks in a basket. The screen shows the total of the basket and the names of the snacks that cost more than 2." Underline the nouns: pupil, snack, name, price, basket, total, screen. Underline the verbs: pick, put, show the total, show the names. A basket holds three snacks: crisps at 2, juice at 3 and cake at 4.

### Guess

Which nouns should become stations? Here are three ideas.

- A. Every noun, including name and price.
- B. One big station called Shop that knows everything.
- C. The nouns with their own facts or their own job, which are snack and basket. Name and price are facts about a snack.

Idea A makes a station for a single fact, which is more work and no clearer. Idea B is the giant all-in-one machine. Idea C is the answer.

Where should "total price" live? Here are three ideas.

- A. On the basket, because the basket holds the snacks.
- B. On the snack, because a snack has a price.
- C. On the big Shop station, which knows everything.

Idea B gives a snack a job it cannot do, because it knows only its own price. Idea C works until every new feature has to edit it. Idea A is the answer.

### Build

1. Write the story in a few plain sentences.
2. Underline the nouns and the verbs.
3. A noun with its own facts or its own job becomes a station. A noun that is only a fact about another one becomes a box on that station.
4. Give each action to the station that already holds the facts it needs.
5. If a station needs facts from another, it holds that other station and asks through the other's public jobs.
6. Lock away anything that outsiders should not change.
7. Test the plan by adding a feature, such as a discount. Only one station should need to change.

### Name it

To **model** a problem is to choose the classes, properties and functions that stand for the real thing (general knowledge). A snack is a class with the properties `name` and `price`. A basket is a class with the functions `add`, `total` and `namesOver`.

**Composition** means one class holds objects of another class (general knowledge). The basket holds snacks, so a basket has snacks. The Android architecture page puts the main idea like this: "separating your app into methods, classes, files, packages, modules and layers that have clearly defined responsibilities and boundaries". A **responsibility** is the job a class owns. Here the basket owns the total.

The basket keeps its list private, as in the `private` lesson. Outsiders use `add`, `total` and `namesOver`. The two list tools come from the `map`, `filter` and `sumOf` lesson.

This program is the whole plan.

```kotlin
class Snack(val name: String, val price: Int)

class Basket {
    private val snacks: MutableList<Snack> = mutableListOf()

    fun add(snack: Snack) {
        snacks.add(snack)
    }

    fun total(): Int {
        return snacks.sumOf { it.price }
    }

    fun namesOver(limit: Int): List<String> {
        return snacks.filter { it.price > limit }.map { it.name }
    }
}

fun main() {
    val basket = Basket()
    basket.add(Snack("crisps", 2))
    basket.add(Snack("juice", 3))
    basket.add(Snack("cake", 4))
    println(basket.total())
    println(basket.namesOver(2))
}
```

It prints `9` and `[juice, cake]`. The basket owns the total and the filtered names, so the cook in `main` only asks. Crisps cost 2, which is not more than 2, so they are left out.

Kotlin refuses to run this one, because the cook tried to reach into a locked cupboard.

```kotlin
// does not compile
class Snack(val name: String, val price: Int)

class Basket {
    private val snacks: MutableList<Snack> = mutableListOf()
}

fun main() {
    val basket = Basket()
    basket.snacks.add(Snack("cake", 4))
}
```

## Predict the output

```challenge
type: predict
run: false
id: each-basket-has-its-own-snacks
verified: not-run
code: |
  class Snack(val name: String, val price: Int)

  class Basket {
      private val snacks: MutableList<Snack> = mutableListOf()

      fun add(snack: Snack) {
          snacks.add(snack)
      }

      fun total(): Int {
          return snacks.sumOf { it.price }
      }
  }

  fun main() {
      val a = Basket()
      val b = Basket()
      a.add(Snack("cake", 4))
      a.add(Snack("juice", 3))
      b.add(Snack("crisps", 2))
      println(a.total())
      println(b.total())
  }
options:
  - id: a
    text: "9, then 2"
    why: "You may have put the crisps into the first basket. They were added to b, so a holds only the cake and the juice."
  - id: b
    text: "7, then 2"
    correct: true
    why: "Each basket makes its own list of snacks. The first holds a cake and a juice, which cost 4 and 3. The second holds only the crisps."
  - id: c
    text: "9, then 9"
    why: "You may have thought all baskets share one list. The plan makes a new list for each basket, like a separate station for each."
output: |
  7
  2
```

```challenge
type: predict
run: false
id: ask-the-station-do-not-rummage
verified: not-run
code: |
  class Snack(val name: String, val price: Int)

  class Basket(val snacks: List<Snack>) {
      fun total(): Int {
          return snacks.sumOf { it.price }
      }
  }

  fun main() {
      val basket = Basket(listOf(Snack("cake", 4), Snack("juice", 3)))
      println(basket.total())
      println(basket.snacks.map { it.name })
  }
options:
  - id: a
    text: "7, then [4, 3]"
    why: "You may have thought map gives the prices. The code maps each snack to its name, so the names come out."
  - id: b
    text: "The program is refused, because snacks is not private"
    why: "You may have thought every property must be locked away. Nothing forces that. A property with no private in front is open, as the earlier lesson showed."
  - id: c
    text: "7, then [cake, juice]"
    correct: true
    why: "The basket asks each snack for its price, which adds up to 7. The names come out in the order the snacks were put in."
output: |
  7
  [cake, juice]
```

```challenge
type: predict
run: false
id: new-feature-lives-in-one-station
verified: not-run
code: |
  class Snack(val name: String, val price: Int)

  class Basket(val snacks: List<Snack>) {
      fun total(): Int {
          return snacks.sumOf { it.price }
      }

      fun totalWithDiscount(off: Int): Int {
          return total() - off
      }
  }

  fun main() {
      val basket = Basket(listOf(Snack("cake", 4), Snack("juice", 3)))
      println(basket.totalWithDiscount(2))
      println(basket.total())
  }
options:
  - id: a
    text: "5, then 7"
    correct: true
    why: "The new job sits in the basket and asks for the existing total. It takes 2 off and leaves the snacks alone, so total still gives 7."
  - id: b
    text: "5, then 5"
    why: "You may have thought the discount changes the snacks. The new job only works out a number from the total. Nothing is stored."
  - id: c
    text: "7, then 5"
    why: "You may have mixed up the two calls. The first call asks for the discounted total and the second asks for the plain one."
output: |
  5
  7
```

## Tricks to remember

- Nouns become stations. Verbs become jobs. A fact about another noun becomes a box.
- Put each job where its facts already are.
- A new feature should change one station. If it changes five, the plan is wrong.
- On your own machine, add a `Customer` class that holds a `Basket` and has a function that returns a text like "Ada owes 9". Then add a discount and see which class has to change.

## Common mistakes

- **Building one class that does everything.** It feels simple to keep everything in one place. Every new feature edits the same class, and a change can break unrelated parts. Give each class one job.
- **Making a class for every noun.** More classes feels like better design. A name or a price is a fact about something else. Make it a property unless it has facts or jobs of its own.
- **Putting a job far from its facts.** A Shop function that reaches into every basket works, so it feels fine. Then a change to the basket breaks the Shop. Ask the basket for its total.
- **Leaving the inner list open.** It saves writing a function. Any code can then change the basket's contents and skip its rules. Keep it private and offer `add`.

## Where this shows up in Android

The Android architecture guide says the most important principle is separation of concerns. It means separating an app into methods, classes, files, packages, modules and layers that have clearly defined responsibilities and boundaries. It says the role of the UI layer is to display the application data on screen. It says the data layer contains the business logic, the rules that decide how the app creates, stores and changes data.

The symptom is an app where changing one rule, such as how a price is worked out, means editing many screens. My own reasoning: the rule was not kept in one class, as the total is kept in the basket. A screen that only displays what the data layer gives it is the cook who asks.

## Say it in an interview

**How do you design a small program with classes, properties and functions?**
I write the problem as a short story and underline the nouns and verbs. Nouns with their own facts or jobs become classes, and facts about them become properties. Each verb goes to the class that holds the data it needs. My own reasoning: I then add a feature on paper and check that only one class changes.

**How do classes work together?**
One class holds objects of another and calls their public functions. A basket holds snacks and asks each for its price. This is called composition (general knowledge). The caller does not need to know how the total is worked out.

**What is the trade-off between inheritance and composition?**
My own reasoning, from general knowledge: inheritance means "is a" and shares code by making one class a child of another. Deep chains tie a child to its parent, so a change high up can break many classes. Composition means "has a" and is looser. The trade-off is that composition needs a little more code to pass calls along.

**What goes wrong when one class does everything?**
My own reasoning: every feature edits the same file, tests have to set up everything, and a bug anywhere can come from anywhere. The Android architecture guide gives the opposite rule, separation of concerns, with clear responsibilities and boundaries.

## Recap

1. Turn nouns into classes only when they have facts or jobs of their own.
2. Give each action to the class that already holds the facts it needs.
3. A new feature should change one class, not many.

## You can now

- Predict the totals of separate baskets that each hold their own snacks.
- Predict what a chain of list tools returns from a class's list property.
- Predict that a new function in the owning class leaves the old results unchanged.
