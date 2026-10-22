---
id: dsa/complexity/time-vs-space
title: When is it right to spend more memory to save time?
status: live
difficulty: easy
order: 4
requires: [dsa/complexity/big-o-notation]
summary: Remembering answers saves repeated work but costs memory. It pays off only when the same question comes back and the pile stays small.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [pocket-timetable]
playground: true
fruit: false
upnext: false
---

## The question

A bus app works out the next bus for a stop every time you look at it. You look at the same stop ten times in a morning. Should the app do all the work ten times, or keep the answer from the first time? And what does keeping it cost?

## Mental model

Think of a traveller who needs bus times.

- The station is where the slow work happens. Each walk there is one trip.
- A trip is the work you want to avoid repeating.
- A printed timetable page holds one answer, for one stop.
- The pocket is the memory you spend to keep the pages.

Traveller one walks to the station every time. Traveller two walks once per stop, writes the answer on a page and keeps it in a pocket. Next time the same stop comes up, traveller two reads the page and walks nowhere.

**Where the analogy stops:** A pocket gets heavy slowly, and you feel it. Memory on a phone is shared with other apps, and you do not feel it until the system acts. A printed page also goes out of date, and a remembered answer can too. This lesson does not cover that.

## See it move

```sim
id: pocket-timetable
template: compare
title: Asking the station every time or carrying a timetable
intro: "Two travellers ask about bus stops. One walks to the station every time. The other carries printed pages in a pocket. Nothing has been asked yet."
sides:
  - id: station
    label: Ask the station every time
    boxes:
      - { id: walks, label: "Walks to the station" }
      - { id: pocket, label: "Pocket: empty" }
  - id: timetable
    label: Carry a timetable
    boxes:
      - { id: walks, label: "Walks to the station" }
      - { id: pocket, label: "Pocket: no pages yet" }
vars: { stationWalks: 0, tableWalks: 0, pages: 0, seen7: 0, seen12: 0 }
show:
  - { var: stationWalks, label: "Walks by traveller one" }
  - { var: tableWalks, label: "Walks by traveller two" }
  - { var: pages, label: "Pages in the pocket" }
actions:
  - id: ask7
    label: "Ask about stop 7"
    cases:
      - when: { var: { seen7: 0 } }
        do:
          - add: { stationWalks: 1, tableWalks: 1, pages: 1 }
          - set: { seen7: 1 }
          - label: { timetable.pocket: "Pocket: {pages} page(s)" }
          - light: [ station.walks, timetable.walks, timetable.pocket ]
          - say: "Stop 7 is new to both. Both walk to the station. Traveller two writes a page and keeps it."
      - when: { var: { seen7: 1 } }
        do:
          - add: { stationWalks: 1 }
          - light: [ station.walks, timetable.pocket ]
          - say: "Stop 7 again. Traveller one walks to the station again. Traveller two reads the page and walks nowhere."
  - id: ask12
    label: "Ask about stop 12"
    cases:
      - when: { var: { seen12: 0 } }
        do:
          - add: { stationWalks: 1, tableWalks: 1, pages: 1 }
          - set: { seen12: 1 }
          - label: { timetable.pocket: "Pocket: {pages} page(s)" }
          - light: [ station.walks, timetable.walks, timetable.pocket ]
          - say: "Stop 12 is new to both. Both walk to the station. Traveller two writes another page."
      - when: { var: { seen12: 1 } }
        do:
          - add: { stationWalks: 1 }
          - light: [ station.walks, timetable.pocket ]
          - say: "Stop 12 again. Traveller one walks again. Traveller two reads the page and walks nowhere."
  - id: fill
    label: "Keep a page for all 500 stops"
    do:
      - set: { pages: 500 }
      - label: { timetable.pocket: "Pocket: 500 pages, too full to close" }
      - mark: { timetable.pocket: bad }
      - light: [ timetable.pocket ]
      - say: "Traveller two now carries 500 pages. Most will never be read, and the pocket is overfull."
script: [ ask7, ask7, ask12, ask12, fill ]
try:
  - text: "Press \"Ask about stop 7\" three times. Watch the two walk counters."
    done: [ ask7, ask7, ask7 ]
  - text: "Press \"Ask about stop 12\" twice. The first time costs both travellers a walk. The second time costs only one."
    done: [ ask12, ask12 ]
  - text: "(Optional) Press \"Keep a page for all 500 stops\" and read what happens to the pocket."
    done: [ fill ]
    optional: true
```

**What you should notice:** Traveller one's walks keep rising with every question, traveller two's walks stop rising once each stop has a page, and the pages cost pocket space.

## Build the logic

### Notice

A traveller asks about stop 7 ten times and about stop 12 three times. That is 13 questions. Each answer needs one walk to the station.

If the traveller keeps no pages, that is 13 walks. If the traveller keeps a page for each stop, there are 2 walks, one per stop, and 2 pages in the pocket.

### Guess

A traveller asks 500 questions in a day, but only about 5 different stops. How many walks does the page-keeper make?

- A. 500, because every question needs a check.
- B. 5, one for each different stop.
- C. 100, because 500 divided by 5.

Idea A is traveller one. Idea C divides when nothing needs dividing. Idea B is the answer: each stop costs one walk, and later questions read a page.

Now a harder one. A traveller asks about 1,000,000 different stops, once each. Is the pocket worth it?

- A. Yes, always.
- B. No, every question is new, so every question still needs a walk, and the pocket fills with pages nobody reads.
- C. Yes, but only if the pages are small.

Idea B is the answer. Remembering helps only when a question comes back.

### Build

1. A question arrives. Look in the pocket for its page.
2. If the page is there, read it. No walk.
3. If not, walk to the station, write the page, keep it in the pocket, and read it.
4. Ask whether the trade is worth it. Do questions repeat? Is the walk expensive? Are the pages small?
5. Set a limit on the pocket. When it is full, throw a page away.

### Name it

**Space** is the extra memory your code uses to do its job. A **time-space trade-off** means spending space to save time, or the other way round. Remembered answers are called a **cache**. In Kotlin you can build one with a `MutableMap` and `getOrPut`.

The Kotlin documentation says `getOrPut` returns the value for the key if it is present and not null. Otherwise it runs your function, puts the result in the map under the key, and returns it. So the work runs only the first time a key is seen.

```kotlin
var trips = 0

fun askStation(stop: Int): String {
    trips++
    return "Bus at 8:15"
}

val pocket = mutableMapOf<Int, String>()

fun lookUp(stop: Int): String = pocket.getOrPut(stop) { askStation(stop) }

fun main() {
    for (stop in listOf(7, 12, 7, 7, 12)) {
        lookUp(stop)
    }
    println("Trips to the station: $trips")
    println("Pages in the pocket: ${pocket.size}")
}
```

This prints `Trips to the station: 2` and then `Pages in the pocket: 2`. Five questions, two different stops, two trips. The cost is two pages. Looking up a key in a map takes about one step (general knowledge).

## Predict the output

```challenge
type: predict
id: repeats-use-the-pocket
verified: kotlin-2.4.21
code: |
  fun main() {
      var calls = 0
      val remembered = mutableMapOf<Int, Int>()
      for (q in listOf(3, 5, 3, 3, 5, 3)) {
          remembered.getOrPut(q) { calls++; q * 10 }
      }
      println("$calls ${remembered.size}")
  }
options:
  - id: a
    text: "6 0"
    why: "You may have counted every question as a walk and forgotten that getOrPut keeps the answer. The map holds one page for each different stop."
  - id: b
    text: "6 2"
    why: "You kept the two pages but still counted six walks. A page that is already in the pocket costs no walk."
  - id: c
    text: "2 2"
    correct: true
    why: "Six questions, but only two different stops. Each stop needs one walk the first time, and the other four questions read a page."
output: |
  2 2
```

```challenge
type: predict
id: all-new-questions
verified: kotlin-2.4.21
code: |
  fun main() {
      var calls = 0
      val remembered = mutableMapOf<Int, Int>()
      for (q in 1..4) {
          remembered.getOrPut(q) { calls++; q * 10 }
      }
      println("$calls ${remembered.size}")
  }
options:
  - id: a
    text: "4 4"
    correct: true
    why: "Every stop is new, so every question needs a walk. The pocket saves nothing here and still holds four pages."
  - id: b
    text: "1 4"
    why: "You may have expected the pocket to save walks. It saves a walk only when a stop comes back."
  - id: c
    text: "4 0"
    why: "You may have thought the pocket stays empty. getOrPut writes each new answer into the map."
output: |
  4 4
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `Trips to the station: 2` and `Pages in the pocket: 2`.
3. **Break.** Change `listOf(7, 12, 7, 7, 12)` to `(1..1000).toList()`. Run it again. Now every stop is new: 1000 trips and 1000 pages. The pocket saved nothing and grew with every question.
4. **Fix.** Give the pocket a limit. Change the body of `lookUp` to two lines: first `if (pocket.size >= 100) pocket.clear()`, then `return pocket.getOrPut(stop) { askStation(stop) }`. The function now needs braces and a `return`. Run it. You should see 1000 trips and 100 pages.

The limit does not bring the trips down, because every stop was new. It stops the pocket from growing without end. That is the second half of the trade.

## Fix the bug

```challenge
type: fix
id: fix-endless-cache
generated: true
verified: kotlin-2.4.21
code: |
  class AnswerCache {
      private val saved = mutableMapOf<Int, String>()
      var work = 0

      // Team rule: keep at most 100 answers.
      fun answerFor(id: Int): String {
          return saved.getOrPut(id) {
              work++
              "answer-$id"
          }
      }

      fun kept(): Int = saved.size
  }

  fun main() {
      val cache = AnswerCache()
      for (id in 1..1000) {
          cache.answerFor(id)
      }
      println("Work done: ${cache.work}")
      println("Answers kept: ${cache.kept()}")
  }
hints:
  - "Read the comment above answerFor. Which line enforces that rule?"
  - "Nothing ever removes a page. Check the size of the map before you add a new one."
expected: |
  Work done: 1000
  Answers kept: 100
fix: |
  class AnswerCache {
      private val saved = mutableMapOf<Int, String>()
      var work = 0

      // Team rule: keep at most 100 answers.
      fun answerFor(id: Int): String {
          if (saved.size >= 100) {
              saved.clear()
          }
          return saved.getOrPut(id) {
              work++
              "answer-$id"
          }
      }

      fun kept(): Int = saved.size
  }

  fun main() {
      val cache = AnswerCache()
      for (id in 1..1000) {
          cache.answerFor(id)
      }
      println("Work done: ${cache.work}")
      println("Answers kept: ${cache.kept()}")
  }
explanation: "The pocket had no limit, so it grew by a page for every new question. A real cache throws away a page when it is full, for example the one used least recently."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a small answer: `getOrPut` never hands back a missing value here. Ask it anyway, then spend your time on question 4. Which calls repeat, and what happens to the pocket each time?

## Tricks to remember

- Same question twice? Keep the page. Every question new? Do not bother.
- A pocket with no limit is a bag you never empty.
- Spend space to save time, only when the saved time is worth the space.
- Count two things: the walks you avoid, and the pages you keep.

## Common mistakes

- **Thinking less memory is always better.** Small feels safe. But a few kilobytes of remembered answers can save thousands of repeated steps. Ask what the memory buys.
- **Remembering when no question repeats.** A cache sounds smart. If every question is new, you pay for pages and save nothing. Count how often a question comes back.
- **A cache with no limit.** The pile grows slowly, so it feels free. Over a long session it can grow until memory runs short. Set a cap.
- **Treating phone memory as free because the phone has gigabytes.** Your app shares that memory with every other app. See the Android section.

## Where this shows up in Android

Think of a photo grid that keeps every decoded picture in memory so that scrolling back is smooth. That is traveller two. It is fast, and it spends memory.

The Android memory page says the system uses `onTrimMemory()` to tell an app that memory is running low and that it should reduce its allocations. If that is not enough, the kernel starts killing processes, and the low-memory killer kills background apps first. This part is my own reasoning: an app with a huge cache is a bigger target. A user who switches away may come back to find the app has restarted from scratch.

Android's `LruCache` is a cache that holds a limited number of values. When it is full and a new value arrives, it drops the value used least recently (LRU means least recently used).

## Say it in an interview

**When is it right to spend more memory to save time?**
When the same question comes back many times, the work is costly, and the stored answers are small. Then a cache turns many slow steps into one. The trade-off is memory, so I would also set a limit, and I would not cache when every question is new (my own reasoning).

**What is the difference between auxiliary space and total space?**
Auxiliary space is the extra memory the algorithm needs on top of its input. Total space is the input plus that extra. People usually quote auxiliary space, because the input is there anyway. General knowledge.

**Does changing the input array in place count as O(1) space? What is the side effect?**
It counts as O(1) extra space, because no new collection grows with n. The side effect is that the caller's data changes. If the caller still needs the original, you must copy it first, and then you are back to O(n). My own reasoning: say this out loud in an interview, because it shows you thought about the caller.

**On a phone with little memory, when might you choose a slower in-place method over a faster method that uses a map?**
When the list is large and memory is tight. The map keeps up to n entries, and in-place work keeps almost none. I would first measure how big n really is, because for a small n the map costs little (my own reasoning).

## Recap

1. Remembering answers saves repeated work but costs memory.
2. It pays off only when the same question comes back.
3. Every cache needs a limit, or it grows without end.

## You can now

- Predict how many times the slow work runs when answers are remembered.
- Explain why a cache saves nothing when every question is new.
- Fix a cache that grows without a limit by adding a cap.

## Practise on these

Each problem has a slow way and a way that spends memory to be faster. Say the time and the extra space of both before you code. The list runs from the gentlest to the hardest.

1. LeetCode 217, Contains Duplicate (Easy). Spend a set of extra memory to cut the time from sorting to one pass. https://leetcode.com/problems/contains-duplicate/
2. LeetCode 1, Two Sum (Easy). Keep a map of numbers seen so far, instead of checking every pair. https://leetcode.com/problems/two-sum/
3. LeetCode 136, Single Number (Easy). A map works, then a bit trick gets the memory back to a constant. https://leetcode.com/problems/single-number/
4. LeetCode 238, Product of Array Except Self (Medium). Build the answer with little extra memory instead of two helper arrays. https://leetcode.com/problems/product-of-array-except-self/
5. LeetCode 41, First Missing Positive (Hard). Reuse the input array as the storage, so extra space stays constant. https://leetcode.com/problems/first-missing-positive/
