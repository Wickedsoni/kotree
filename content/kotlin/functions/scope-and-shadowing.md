---
id: kotlin/functions/scope-and-shadowing
title: Why can't I use a variable outside the braces where I made it?
status: live
difficulty: medium
order: 16
requires: [kotlin/functions/declaring-functions, kotlin/control-flow/ranges-and-for-loops]
summary: A name works only inside the braces where it was made. A name made inside can hide one with the same spelling outside.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [house-and-street-names]
playground: false
fruit: false
upnext: false
---

## The question

A program makes a variable called message inside a loop and prints it without trouble. Two lines later, after the loop, it tries to print message again. Kotlin refuses to run the program. The name was right there a few lines above. Why can't the later line use it?

## Mental model

Think of a street with houses, and of people called by their first names.

- The street is the outer area: the whole function or file.
- A house is a pair of curly braces. A house can have a room in it, which is a pair of braces inside a pair of braces.
- A person is a variable, and the person's first name is the variable's name.
- Calling a name out loud is reading the variable.
- A person is born on the line that makes them. They stay until the closing brace of the house they were born in.

At home, calling "Sam" gets the Sam who lives in the house. On the street, calling "Sam" gets the Sam who lives on the street. Nobody on the street can reach a person who lives inside a house.

**Where the analogy stops:** A real person exists before you meet them and can walk out of the house. A Kotlin name exists only from the line that makes it to the closing brace, and it never leaves. Each time the house is opened again, such as on every round of a loop, a new person is born.

## See it move

```sim
id: house-and-street-names
template: zones
title: A street, a house and the names people answer to
intro: Sam the baker and Joe the postman live on the street. Sam the cousin and Mia live in the house. Lee stands on the street.
zones:
  - { id: street, label: Street (the outer area) }
  - { id: house, label: House (a pair of braces) }
items:
  - { id: baker, label: "Sam (baker)", at: street }
  - { id: postman, label: "Joe (postman)", at: street }
  - { id: cousin, label: "Sam (cousin)", at: house }
  - { id: mia, label: "Mia", at: house }
  - { id: lee, label: "Lee, who calls", at: street }
actions:
  - id: call-sam
    label: Call Sam
    cases:
      - when: { at: { lee: house, cousin: house } }
        do:
          - say: "Lee is in the house, so the call looks in the house first. Sam the cousin answers."
      - when: { at: { lee: house } }
        do:
          - say: "Lee is in the house, but nobody called Sam lives there. The call goes out to the street and Sam the baker answers."
      - do:
          - say: "Lee is on the street, so the call looks on the street. Sam the baker answers."
  - id: call-joe
    label: Call Joe
    cases:
      - when: { at: { lee: house } }
        do:
          - say: "Nobody called Joe lives in the house, so the call goes out to the street. Joe the postman answers."
      - do:
          - say: "Lee is on the street, and Joe the postman answers."
  - id: call-mia
    label: Call Mia
    cases:
      - when: { at: { lee: house } }
        do:
          - say: "Mia lives in the house, so Lee finds her."
      - do:
          - say: "Nobody on the street is called Mia. She lives inside the house, and a call from the street cannot reach her."
drops:
  - item: lee
    to: house
    do:
      - say: "Lee is in the house."
  - item: lee
    to: street
    do:
      - say: "Lee is on the street."
  - item: cousin
    to: street
    when: { at: { cousin: house } }
    do:
      - remove: cousin
      - say: "Sam the cousin has been removed from the house. No Sam lives there now."
    refuse: "Sam the cousin is already gone."
script: ["lee>house", call-sam, "lee>street", call-sam, call-mia, "cousin>street", "lee>house", call-sam]
try:
  - text: "Stand Lee in the house and press \"Call Sam\"."
    done: ["lee>house", call-sam]
  - text: "Move Lee out to the street. Press \"Call Sam\", then \"Call Mia\"."
    done: ["lee>street", call-sam, call-mia]
  - text: "Drag the house's Sam out of the house, move Lee back in, and press \"Call Sam\" again."
    done: ["cousin>street", "lee>house", call-sam]
```

**What you should notice:** A call looks in the house first and then outwards, a person who lives in the house cannot be reached from the street, and removing the house's Sam lets the street's Sam answer.

## Build the logic

### Notice

The street has Sam the baker and Joe the postman. Lee's house has another person called Sam, who is Lee's cousin, and a person called Mia.

- Lee, at home, calls "Sam". The cousin answers.
- Lee, at home, calls "Joe". No Joe lives in the house, so Lee's call goes out to the street and the postman answers.
- Lee steps out and calls "Sam". The baker answers.
- Lee, on the street, calls "Mia". Nobody answers. Mia lives inside the house.

### Guess

Lee is at home and calls "Sam". Two Sams can hear, the cousin in the house and the baker on the street. Who answers? Here are three ideas.

- A. Both of them.
- B. The cousin, because he is in the house.
- C. The baker, because he was on the street first.

Idea A would make the name unclear. Idea C feels fair because the baker came first, but the call does not care who came first. It cares who is closest. Idea B is the answer.

### Build

1. Every pair of curly braces is a house. A function body, one branch of a choice and a loop body each make one.
2. A person is born on the line that makes them and lives until the closing brace of their house. A call before that line does not find them, so it keeps looking outwards.
3. To answer a call, look first in the house where you are standing. If a person with that name lives there, stop.
4. If not, step out one level and look again. Keep going until you reach the street.
5. If you reach the end of the street with no match, Kotlin refuses to run the program.
6. Looking only goes outward. A call from the street never goes into a house.
7. Two people can share a name if they live in different houses. The one closer to the caller answers, and the other is hidden while the caller is in that house.
8. They are two separate people. Giving a new value to the inner Sam does not change the outer Sam.
9. When the caller leaves the house, the inner person is gone, and the outer one is exactly as before.

### Name it

The stretch of code where a name can be used is its **scope**. The Kotlin language specification says a scope is a delimited region of code in which names are introduced. It says scopes can be nested, with names from outer scopes possibly available in the inner ones. A **block** is a group of steps between curly braces, and the body of a function is one. The specification also says that inside a function body, a name can be used only after the line that makes it.

When an inner name has the same spelling as an outer name, the inner one **shadows** the outer one: it hides it while you are inside. The specification does not use the word shadowing. It does say that giving a name to a value is allowed even when the name is already available from an outer scope. The word shadowing is general knowledge. The same spelling for two people causes mix-ups, so many teams avoid it (my own reasoning). The compiler accepts the program below. Some editors show a warning for it (general knowledge).

```kotlin
fun main() {
    val price = 10
    if (price > 5) {
        val price = 99
        println("inside: $price")
    }
    println("outside: $price")
}
```

It prints `inside: 99` and then `outside: 10`. The `if` branch is a house with its own person called `price`. The call inside finds that one first. After the closing brace the house is gone, and the call finds the one on the street.

Kotlin refuses to run this next program. The name `message` was born inside the loop's braces, and the last line stands outside them. The loop-variable rule comes from the specification's scope rules (our own reasoning).

```kotlin
// does not compile
fun main() {
    for (round in 1..3) {
        val message = "Round $round"
        println(message)
    }
    println(message)
}
```

## Predict the output

```challenge
type: predict
run: false
id: inner-total-hides
verified: kotlin-2.4.21
code: |
  fun main() {
      val total = 10
      if (total > 5) {
          val total = 99
          println(total)
      }
      println(total)
  }
options:
  - id: a
    text: "99, then 99"
    why: "You may have thought the new Sam replaces the old one. They are two separate people, and the outer one is untouched."
  - id: b
    text: "99, then 10"
    correct: true
    why: "Inside the house, the call finds the Sam who lives there, and that is 99. After the closing brace the house is gone, and the call finds the street's Sam, who is still 10."
  - id: c
    text: "10, then 10"
    why: "You may have thought the first Sam always wins. The caller in the house reaches the closest Sam first."
output: |
  99
  10
```

```challenge
type: predict
run: false
id: street-person-changed
verified: kotlin-2.4.21
code: |
  fun main() {
      var sum = 0
      for (n in 1..3) {
          val twice = n * 2
          sum = sum + twice
      }
      println(sum)
  }
options:
  - id: a
    text: "0"
    why: "You may have thought changes made inside the house cannot reach the street. There is no second sum in the house, so the call reaches the street's sum and changes it."
  - id: b
    text: "6"
    why: "You may have kept only the last round's twice. The street's sum keeps every change."
  - id: c
    text: "12"
    correct: true
    why: "The loop house reaches out to the street's sum on every round. It adds 2, then 4, then 6, and the street's sum keeps all of it. Each twice is born and leaves inside the house."
output: |
  12
```

```challenge
type: predict
run: false
id: add-up-gives-zero
verified: kotlin-2.4.21
code: |
  fun addUp(limit: Int): Int {
      var total = 0
      for (n in 1..limit) {
          var total = 0
          total = total + n
      }
      return total
  }

  fun main() {
      println(addUp(4))
  }
options:
  - id: a
    text: "0"
    correct: true
    why: "Each round makes a new total who lives only in the loop house, and the sum goes to that person. The return line is outside the house, so it reads the street's total, which was never changed."
  - id: b
    text: "10"
    why: "You may have read what the function was meant to do. The inner total hides the outer one, so the street's total never grows."
  - id: c
    text: "4"
    why: "You may have kept only the last value of n. Nothing keeps any n outside the loop house."
output: |
  0
```

## Tricks to remember

- Look at home first, then step outwards. Never inwards.
- A person lives from their line to the closing brace.
- Same spelling inside means a new person, not a new value for the old one.
- Want the street's person to change? Do not make a second one with the same name.

## Common mistakes

- **Expecting a name made in a loop to exist after it.** The loop is over, and the name still looks close on the page, so it feels reachable. It was born in the loop's house and left with it. Make the name before the loop, in the house where you need it later.
- **Making a second variable to "update" the first.** Writing `var total = 0` again inside a loop looks like resetting it. It makes a new person, and the old one never changes. Give the old name a new value without `var` or `val`.
- **Leaving two people with one name.** The program still runs, so it feels harmless. But the next reader must count houses to know who answers. Rename the inner one.

## Where this shows up in Android

**Jetpack Compose** is the modern toolkit for building Android screens in Kotlin. Its state page keeps the text a person types in a line like `var name by remember { mutableStateOf("") }`. The part after `by` is not covered here. It then gives the text field `onValueChange = { name = it }`. The braces hold a small piece of code that the field runs each time the text changes, and `it` is the new text it hands in.

That is a house on the street. The code inside the braces calls `name` and finds the person on the street, so it changes the real one. The name `it` was born in the braces and is gone after the closing brace. The page says a `TextField` updates only when its `value` parameter changes.

This next part is our own reasoning. Suppose the code in the braces made a new `name` instead of giving a new value to the street's `name`. Then the real one would never change. The field would not update, and the typing would seem to vanish.

## Say it in an interview

**What is scope?**
Scope is the part of the code where a name can be used. The Kotlin specification says scopes can be nested, with outer names available in inner ones. A name inside a function body can be used only after the line that makes it. The trade-off is that a name with a small scope is safe to change, because few lines can see it.

**Is a variable made in a `for` loop still there after the loop?**
No. The name lives from its line to the closing brace of the loop body. A new one is made on every round. To use a value after the loop, make the variable before the loop. This follows from the specification's scope rules (my own reasoning), and Kotlin refuses to run code that uses it outside.

**What is shadowing, and does it change the outer variable?**
Shadowing is when an inner name has the same spelling as an outer one and hides it. It does not change the outer one. They are two separate variables. The specification allows this, and the compiler accepts it. Some editors show a warning (general knowledge).

**Is shadowing good practice?**
Usually not. The same spelling saves you one new name, but it makes the code hard to read correctly. A function that adds up numbers in a loop but declares a second `total` inside the loop returns 0, with no error. My own reasoning: renaming the inner variable costs a few seconds and removes the trap.

## Recap

1. A name works from the line that makes it to the closing brace of its block.
2. A call looks in the current block first, then outwards, and never inwards.
3. An inner name with the same spelling hides the outer one but never changes it.

## You can now

- Predict which of two same-named variables a line prints when one hides the other.
- Predict whether a change inside a loop reaches a variable made before the loop.
- Spot the line where an inner variable hides the outer one and makes a function return 0.
