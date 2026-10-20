---
id: foundation/programming-thinking/how-a-program-repeats
title: How does a program keep doing something until a condition changes?
status: live
difficulty: easy
order: 15
requires: [foundation/programming-thinking/how-a-program-decides]
summary: A program checks a yes-or-no question, runs its steps, then checks again. It stops only when the answer changes to no.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [loop-track-gate]
playground: false
fruit: false
upnext: false
---

## The question

You stir a pot of soup and wait for it to boil. You do not know how many stirs it will take. You only know what you are waiting for.

How does a program do something over and over until a thing changes? And what stops it from going on for ever?

## Mental model

Think of a cook stirring soup until it boils.

- The pot holds the soup.
- The spoon goes round the pot once for each stir.
- A thermometer in the pot shows the heat as marks. It starts at 0. The soup boils at 3 marks.
- A stove under the pot adds one mark for each stir, because each stir takes a minute.
- Before every stir, the cook asks one question: "Is the pot still short of boiling?"

The cook checks, stirs, checks again, stirs again. When the answer to the question is no, the cook puts the spoon down.

**Where the analogy stops:** A real stove warms the pot by itself, whatever the cook does. A program has no stove. It only checks a number. Unless a step in the repeated part changes that number, the check sees the same number every time, and the cook stirs for ever. Also, a real cook can see the soup is nearly done. A program checks only at the start of each round.

## See it move

<!-- SIM: loop-track-gate -->

**Try this:**
1. Press Step and watch the spoon go round once. Before the stir, the thermometer is checked, and the stove adds one mark.
2. Change the boiling mark from 3 to 5 and press Run.
3. Switch the stove off and press Run.

**What you should notice:** The spoon stops only because the thermometer changes, and with the stove off it circles for ever.

## Build the logic

### Notice

The thermometer reads 0. The soup boils at 3. The cook goes through these rounds.

1. Check: 0 marks, still short of 3. Stir. The thermometer now reads 1.
2. Check: 1 mark, still short. Stir. The thermometer now reads 2.
3. Check: 2 marks, still short. Stir. The thermometer now reads 3.
4. Check: 3 marks. The pot is not short of boiling any more. The cook puts the spoon down.

There were three stirs and four checks. The last check is the one that says no.

### Guess

The thermometer reads 2. What decides whether the cook stirs again? Here are three ideas.

- A. The cook keeps stirring for a while and stops when tired.
- B. The cook checks the thermometer again, and stirs only if the pot is still short of boiling.
- C. The cook checks once at the start, and then keeps stirring.

Idea A gives the cook a feeling that a program does not have. Idea C is believable, because the first check said yes. But a check made once cannot notice when the thermometer changes. Idea B is the answer: the check is made again before every stir.

### Build

1. Check: is the pot still short of boiling?
2. If the answer is no, stop. The cook moves on to whatever comes after the soup.
3. If the answer is yes, stir once.
4. Go back to step 1.
5. Something in the stirring round must change the thermometer. If nothing does, the check says yes every time and the cook never stops.
6. If the pot is already boiling at the first check, the cook stirs zero times.

Note what the question asks. It asks "should I keep going?", not "should I stop?". The cook keeps stirring while the answer is yes.

### Name it

A set of steps that is repeated is a **loop**. The loop in the Build steps is a **`while`** loop. The yes-or-no question in the round brackets is the **condition**. The Kotlin documentation says that `while` checks the condition and, if the answer is yes, runs the code in its body and then returns to the condition check. The steps go in curly braces and are called the **body**. One run of the body is an **iteration**.

The number that the check reads must be able to change. A name that can hold a new value is made with **`var`**. The documentation says `var` declares variables that can be reassigned. The line `heat = heat + 1` is the stove: it gives `heat` a new value that is one more than the old one.

If the check never turns to no, the result is an **endless loop**: the body runs again and again, and the lines after the loop never run.

```kotlin
fun main() {
    var heat = 0
    while (heat < 3) {
        println("Stir")
        heat = heat + 1
    }
    println("Boiling")
}
```

It prints `Stir` three times and then `Boiling`. The check `heat < 3` is the question "still short of boiling?". With `heat` at 0, 1 and 2, the answer is yes. With `heat` at 3, the answer is no and the loop ends. The Kotlin documentation has a sample of the same shape. It counts up to a limit and then prints a message.

## Predict the output

```challenge
type: predict
run: false
id: how-many-stirs
verified: not-run
code: |
  fun main() {
      var heat = 0
      while (heat < 4) {
          println("Stir")
          heat = heat + 1
      }
  }
  // How many times does Stir print?
options:
  - id: a
    text: "3 times"
    why: "You may have counted the heat values 1, 2 and 3. The heat starts at 0, so the values 0, 1, 2 and 3 all say yes."
  - id: b
    text: "4 times"
    correct: true
    why: "The check says yes when the heat is 0, 1, 2 and 3. That is four stirs. At 4 the check says no, and the cook puts the spoon down."
  - id: c
    text: "5 times"
    why: "You may have counted the check that says no as a fifth stir. That check ends the loop. It does not stir."
output: "4 times"
```

```challenge
type: predict
run: false
id: stove-never-lit
verified: not-run
code: |
  fun main() {
      var heat = 0
      while (heat < 3) {
          println("Stir")
      }
      println("Boiling")
  }
options:
  - id: a
    text: "Stir three times, then Boiling"
    why: "You may have expected the loop to count by itself. A loop only checks a number. Nothing here changes heat."
  - id: b
    text: "Stir over and over, and Boiling never prints"
    correct: true
    why: "The stove is off. The heat stays at 0, so the check says yes every time and the cook stirs for ever."
  - id: c
    text: "Only Boiling"
    why: "You may have expected the loop to be skipped. The check at heat 0 says yes, so the body runs."
output: "Stir over and over, and Boiling never prints"
```

```challenge
type: predict
run: false
id: at-most-four
verified: not-run
code: |
  fun main() {
      var heat = 0
      while (heat <= 4) {
          println("Stir")
          heat = heat + 1
      }
  }
  // Same loop as the first item. The sign now says at most 4.
  // How many times does Stir print?
options:
  - id: a
    text: "4 times"
    why: "You may have kept the answer from the first item. With at most 4, the heat value 4 also says yes, so one more stir happens."
  - id: b
    text: "5 times"
    correct: true
    why: "The check says yes at heat 0, 1, 2, 3 and 4. That is five stirs. At 5 the answer is no."
  - id: c
    text: "6 times"
    why: "You may have counted the check at 5 as a stir. That check says no and ends the loop."
output: "5 times"
```

## Tricks to remember

- Check, stir, check again.
- The check asks "keep going?", not "stop?".
- No stove, no end. Something inside the loop has to change the number.
- The check comes first, so a `while` loop can run zero times.

## Common mistakes

- **Thinking a loop knows by itself when to stop.** A person sees the soup is ready and stops, so it feels as if a loop can do the same. A loop only reads a yes-or-no answer. Find the line inside the loop that changes the number the check reads.
- **Writing the stop rule instead of the keep-going rule.** You want to stop at 3, so you ask "is heat equal to 3?". At the start the heat is 0, so the first check says no and the body never runs. Ask the question that is true while you still want to go on.
- **Miscounting by one.** Swapping "less than" for "at most" feels like a tiny edit. It lets one more value say yes, so the body runs one more time. Write the heat values on paper and mark which ones say yes.

## Where this shows up in Android

You tap a button and the whole app freezes. A few seconds later, Android shows a message that the app is not responding. The Android documentation calls this an **ANR**, short for "application not responding". It says the system shows an ANR if an app cannot respond to user input on the **main thread**, which it also calls the UI thread. One of its conditions is no response to a key press or screen tap within 5 seconds. It also says not to run blocking or long-running work on the main thread.

The documentation does not mention endless loops. This part is our own reasoning. Picture a loop that never ends on the main thread. It is one cook who never puts the spoon down. The cook cannot answer a tap, because the cook is busy stirring. After 5 seconds with no answer, the app meets the limit the page describes. The stove-never-lit item shows how such a loop can be made by accident.

## Recap

1. A loop checks a yes-or-no question, runs its steps, then checks again.
2. The loop stops only when the answer changes to no.
3. Something inside the loop must change the number the check reads.

## You can now

- Predict how many times a while loop runs its body.
- Spot a loop that never ends because nothing changes the number it checks.
- Predict how many extra times a loop runs when less than becomes at most.
