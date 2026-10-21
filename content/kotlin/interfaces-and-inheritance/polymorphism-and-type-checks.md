---
id: kotlin/interfaces-and-inheritance/polymorphism-and-type-checks
title: How can one variable safely hold many kinds of objects?
status: live
difficulty: hard
order: 48
requires: [kotlin/interfaces-and-inheritance/what-is-an-interface, kotlin/null-safety/smart-casts]
summary: One variable can hold many kinds of objects. Learn how is checks the kind safely and why as can stop the program.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [mixed-tools-chute]
playground: false
fruit: false
upnext: false
---

## The question

A worker pulls tools out of a mixed toolbox one at a time. Any tool can say its name, but only a hammer can swing and only a saw can cut. What should the worker do before swinging whatever is in hand? And what happens if the worker swings without looking?

## Mental model

Think of a mixed toolbox on a worker's bench.

- The toolbox has a label on the lid: "Tool". It holds hammers, saws and other tools.
- Each tool is an object. It has its own kind and its own skills.
- Every tool keeps the one promise on the label, which is to say its name.
- Only some tools have extra skills. A hammer swings and a saw cuts.
- The worker takes one tool out of the box at a time.
- Looking at a tool before using its extra skill is a check.
- Swinging without looking is a guess.

**Where the analogy stops:** A real worker sees a tool's kind at a glance. In code, Kotlin knows only the label on the lid before the program runs, so the kind is found out when the check runs. Also, a real worker who guesses wrong gets a bruise. In code, the whole program stops.

## See it move

<!-- SIM: mixed-tools-chute -->

**Try this:**
1. Press "Send the tools" with the sorter set to "Look first". Watch each tool land in its own tray.
2. Switch the gate to "Swing without looking" and send the tools again. Watch what happens when a saw arrives.
3. (Optional reveal) Tick "Ask politely". Send the tools again and watch the saw be handed back with a note saying "not a hammer".

**What you should notice:** Looking first sends every tool to the right tray, and swinging without looking stops the line at the first saw.

## Build the logic

### Notice

The toolbox label says "Tool". It holds a hammer, a saw and another hammer. The worker takes out the first tool and asks, "What is your name?" It answers "hammer". The worker does the same for the saw, which answers "saw". The worker never looked at the kind. Every tool on the label can answer. Later the worker wants a swing. A saw cannot swing.

### Guess

How should the worker ask for the name? Here are three ideas.

- A. Look at the kind first, then ask the name.
- B. Ask directly, because every tool keeps the promise on the label.
- C. Keep a separate box for each kind of tool.

Idea B is the answer. Idea A is extra work, and idea C throws away the shared label.

How should the worker get a swing? Here are three ideas.

- A. Swing whatever is in hand and hope it is a hammer.
- B. Look first. If it is a hammer, swing it. If not, leave it.
- C. Say "if this is a hammer, hand it over, otherwise give me nothing", then handle the nothing.

Idea A stops the line when a saw arrives. Ideas B and C are both safe. Idea B is the answer to the title's question, and idea C is the soft version of it.

### Build

1. Put a label on the box that names the shared promise.
2. Anything the label promises, you ask for directly. Every tool answers in its own way.
3. For a skill only one kind has, look at the kind first.
4. After a good look, treat that tool as that kind until the next tool.
5. If you skip the look and say "this is a hammer" and it is a saw, the line stops.
6. The soft way: say "give it to me if it is a hammer, otherwise give me nothing", and handle the nothing.

### Name it

**Polymorphism** means one variable, such as one with the label `Tool`, can hold objects of many kinds, and a call such as `t.name()` runs the version that belongs to the object inside. This is general knowledge, not a quote from the Kotlin documentation.

The `is` operator is the **type check**. The Kotlin documentation says to use it to check whether an object matches a type at runtime (runtime means while the program is running). This is the same smart cast idea as the `!= null` check from before. After `t is Hammer`, Kotlin treats `t` as a `Hammer` inside the branch.

The `as` operator is the **unsafe cast**. The documentation says that if a cast fails with `as`, a `ClassCastException` (an exception is a crash signal that stops the program) is thrown at runtime. The `as?` operator is the **safe cast**. The documentation says that if the cast fails, it returns `null`.

```kotlin
interface Tool {
    fun name(): String
}

class Hammer : Tool {
    override fun name(): String {
        return "hammer"
    }

    fun swing(): String {
        return "bang"
    }
}

class Saw : Tool {
    override fun name(): String {
        return "saw"
    }

    fun cut(): String {
        return "zzz"
    }
}

fun main() {
    val box: List<Tool> = listOf(Hammer(), Saw(), Hammer())
    for (t in box) {
        println(t.name())
        if (t is Hammer) {
            println(t.swing())
        }
    }
}
```

It prints `hammer`, `bang`, `saw`, `hammer` and `bang`. Asking for the name works on every tool. The `swing` call is only allowed after the check, and it never meets a saw.

This program uses the two casts, with the same classes.

```kotlin
interface Tool {
    fun name(): String
}

class Hammer : Tool {
    override fun name(): String {
        return "hammer"
    }

    fun swing(): String {
        return "bang"
    }
}

class Saw : Tool {
    override fun name(): String {
        return "saw"
    }
}

fun main() {
    val box: List<Tool> = listOf(Hammer(), Saw())
    val first = box[0] as Hammer
    println(first.swing())
    val second = box[1] as? Hammer
    println(second)
}
```

It prints `bang` and then `null`. Writing `box[1] as Hammer` instead would stop the program, because the second tool is a `Saw`.

Kotlin refuses to run this program. The label says `Tool`, and Kotlin only knows the label before the program runs.

```kotlin
// does not compile
interface Tool {
    fun name(): String
}

class Hammer : Tool {
    override fun name(): String {
        return "hammer"
    }

    fun swing(): String {
        return "bang"
    }
}

fun tryIt(t: Tool) {
    println(t.swing())
}

fun main() {
    tryIt(Hammer())
}
```

## Predict the output

```challenge
type: predict
run: false
id: when-over-mixed-box
verified: kotlin-2.4.21
code: |
  interface Tool {
      fun name(): String
  }

  class Hammer : Tool {
      override fun name(): String {
          return "hammer"
      }

      fun swing(): String {
          return "bang"
      }
  }

  class Saw : Tool {
      override fun name(): String {
          return "saw"
      }

      fun cut(): String {
          return "zzz"
      }
  }

  fun useTool(t: Tool): String {
      return when (t) {
          is Hammer -> "hit " + t.swing()
          is Saw -> "cut " + t.cut()
          else -> "unknown"
      }
  }

  fun main() {
      val box: List<Tool> = listOf(Saw(), Hammer())
      for (t in box) {
          println(useTool(t))
      }
  }
options:
  - id: a
    text: "hit bang, then cut zzz"
    why: "You may have read the branches in the order they are written. The loop visits the tools in the order they are in the box, and the saw is first."
  - id: b
    text: "cut zzz, then hit bang"
    correct: true
    why: "The saw comes out first and passes the is Saw look. The hammer comes out second and passes the is Hammer look."
  - id: c
    text: "unknown, then unknown"
    why: "You may have thought the label Tool does not match any branch. The check looks at the real tool inside, not at the label."
output: |
  cut zzz
  hit bang
```

```challenge
type: predict
run: false
id: polite-cast-gives-nothing
verified: kotlin-2.4.21
code: |
  interface Tool {
      fun name(): String
  }

  class Hammer : Tool {
      override fun name(): String {
          return "hammer"
      }

      fun swing(): String {
          return "bang"
      }
  }

  class Saw : Tool {
      override fun name(): String {
          return "saw"
      }
  }

  fun main() {
      val box: List<Tool> = listOf(Hammer(), Saw())
      for (t in box) {
          val h = t as? Hammer
          println(h?.swing())
      }
  }
options:
  - id: a
    text: "bang, then the program stops"
    why: "You may have thought as? behaves like as. The polite version hands back nothing for the saw, and the program carries on."
  - id: b
    text: "bang, then bang"
    why: "You may have thought the saw can swing once it is cast. The cast cannot turn a saw into a hammer."
  - id: c
    text: "bang, then null"
    correct: true
    why: "For the hammer the polite ask succeeds. For the saw it gives back nothing, and the safe call on nothing is also nothing, which prints as null."
output: |
  bang
  null
```

```challenge
type: predict
run: false
id: swing-without-looking
verified: kotlin-2.4.21
code: |
  interface Tool {
      fun name(): String
  }

  class Hammer : Tool {
      override fun name(): String {
          return "hammer"
      }

      fun swing(): String {
          return "bang"
      }
  }

  class Saw : Tool {
      override fun name(): String {
          return "saw"
      }
  }

  fun main() {
      val box: List<Tool> = listOf(Hammer(), Saw())
      println("start")
      val tool = box[1] as Hammer
      println(tool.swing())
  }
options:
  - id: a
    text: "start, then the program stops with a ClassCastException"
    correct: true
    why: "The tool at place 1 is a saw. Saying as Hammer without looking fails, and the second println never runs."
  - id: b
    text: "start, then bang"
    why: "You may have thought a cast that compiles must be safe. Kotlin only checks the label before the program runs. The saw is found out at runtime."
  - id: c
    text: "start, then null"
    why: "You may have mixed up as and as?. Only the polite as? hands back nothing."
output: |
  start
```

## Tricks to remember

- Ask the label for what it promises. Look before you use a skill only some tools have.
- `is` looks, `as` bets, `as?` bets politely.
- The label on the lid is all Kotlin knows ahead of time.
- If a cast compiles, it is still a bet. Compiling does not make it safe.

## Common mistakes

- **Believing that if it compiles, the cast must be safe.** The compiler accepts the cast, so it feels checked. It only knows the label, not the tool inside. Use `is` or `as?` unless you can be sure of the kind.
- **Using `as` to get a hammer skill and ignoring what happens when the tool is a saw.** It works in testing when the box has only hammers. The documentation says a failed `as` throws a `ClassCastException`. Use `as?` and handle the nothing.
- **Checking the kind before every use, even for the promised name.** Looking first feels careful. The label already promises the name, so you can ask for it directly. If you check the kind for every use, the label probably needs another promise.
- **Using the checked tool after changing what holds it.** The check was on the page a moment ago, so it feels still valid. Kotlin trusts a check only when nothing can swap the value, as in the smart casts lesson. Use the tool right after the look.

## Where this shows up in Android

My own reasoning, with no Android class named: picture a list on a phone screen where some rows are headers and some are photos. The list's items share one label, so the code that draws a row can ask for what is promised. For a photo's special skill, such as its size, the code checks the kind first. If the code uses `as` and a header turns up, the screen can crash while the user scrolls. That crash would be a ClassCastException, which is the stop the lesson showed.

## Say it in an interview

**What is polymorphism?**
One variable of a shared type can hold objects of many kinds, and a call runs the version that belongs to the object inside. This is general knowledge. In our toolbox, `t.name()` gave "hammer" for one tool and "saw" for the other.

**How do `is` and smart casts work with class hierarchies?**
The Kotlin documentation says you use `is` to check whether an object matches a type at runtime. The compiler tracks the check for values that cannot change and inserts the cast for you. So inside the branch you can call the child class's own functions.

**What is the difference between `as` and `as?`?**
The documentation says a failed `as` throws a `ClassCastException`, which is why it is called the unsafe operator. A failed `as?` returns `null`, which is why it is the safe operator. Prefer `as?` when the kind is not certain.

**When is a lot of `is` checking a sign of a design problem?**
My own reasoning: a long `when` with `is` for each kind means the label does not promise enough. Often the better fix is to add a function to the interface, so each kind does its own work and the caller asks directly. The trade-off is that the interface grows.

## Recap

1. One variable of a shared type can hold many kinds, and a call runs the held object's version.
2. `is` checks the kind first, and `as?` hands back null when the kind is wrong.
3. `as` stops the program with a ClassCastException when the kind is wrong.

## You can now

- Predict the output of a `when` with `is` branches over a mixed list.
- Predict what `as?` gives for an object of the wrong kind.
- Predict when a program with `as` stops, and what it printed first.
- Explain why a cast that compiles can still stop the program.
