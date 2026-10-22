---
id: dsa/strings/compare-text-by-characters
title: How do I check two words use the same letters without sorting them?
status: live
difficulty: easy
order: 9
requires: [dsa/arrays/arrays-in-memory]
summary: Count the tiles of each letter once. Two words match when all the counts end at zero. No sorting needed.
video: ""
publishedOn: 2026-10-22
updatedOn: 2026-10-22
sims: [tile-bins]
playground: true
fruit: false
upnext: false
---

## The question

A word game shows you "listen" and asks whether its letters can be rearranged to spell "silent". They can. One way is to put the letters of each word in alphabetical order and compare the results. But is there a way that never puts anything in order? How can you check that two words use the same letters without sorting them?

## Mental model

Think of Scrabble tiles and a shelf of bins.

- A tile is one letter.
- A bag of tiles is one word.
- The shelf has one bin for each letter of the alphabet. That is 26 bins, labelled a to z.
- Dropping a tile in its bin adds one to the count in that bin.
- Lifting a tile out takes one from the count.

To compare two bags, drop every tile of the first bag into its bin. Then lift every tile of the second bag out of its bin. If every lift finds a tile and every bin ends empty, the two bags held the same tiles.

**Where the analogy stops:** A Scrabble set has no digits or emoji, and a computer treats a capital letter as a different tile from a small one. This shelf has bins only for small a to z. Also, a real bin cannot hold less than nothing. In code a count can drop below zero, and that is the signal that the second word has a tile the first word does not.

## See it move

```sim
id: tile-bins
template: lanes
title: Six bins, one for each letter of listen
intro: "Six bins stand in a row, one for each letter l, i, s, t, e and n. Every bin shows 0. Three bags of tiles wait: listen, silent and silver."
lanes:
  - { id: bins, label: "Bins, one for each letter", kind: row, slots: 6 }
items:
  - { id: bin-l, label: "l: 0", at: "bins.0" }
  - { id: bin-i, label: "i: 0", at: "bins.1" }
  - { id: bin-s, label: "s: 0", at: "bins.2" }
  - { id: bin-t, label: "t: 0", at: "bins.3" }
  - { id: bin-e, label: "e: 0", at: "bins.4" }
  - { id: bin-n, label: "n: 0", at: "bins.5" }
vars: { phase: 0 }
actions:
  - id: put-listen
    label: "Drop the tiles of listen in"
    do:
      - set: { phase: 1 }
      - label: { bin-l: "l: 1", bin-i: "i: 1", bin-s: "s: 1", bin-t: "t: 1", bin-e: "e: 1", bin-n: "n: 1" }
      - mark: { bin-l: picked, bin-i: picked, bin-s: picked, bin-t: picked, bin-e: picked, bin-n: picked }
      - say: "Each letter of listen adds one tile to its bin. Every bin now shows 1."
  - id: lift-silent
    label: "Lift the tiles of silent out"
    cases:
      - when: { var: { phase: 1 } }
        do:
          - set: { phase: 2 }
          - label: { bin-l: "l: 0", bin-i: "i: 0", bin-s: "s: 0", bin-t: "t: 0", bin-e: "e: 0", bin-n: "n: 0" }
          - mark: { bin-l: good, bin-i: good, bin-s: good, bin-t: good, bin-e: good, bin-n: good }
          - say: "Each letter of silent takes one tile from its bin. Every bin is back at 0, so the two words use the same letters."
    refuse: "There is nothing to lift out. Drop the tiles of listen in first."
  - id: lift-silver
    label: "Lift the tiles of silver out"
    cases:
      - when: { var: { phase: 1 } }
        do:
          - set: { phase: 3 }
          - label: { bin-s: "s: 0", bin-i: "i: 0", bin-l: "l: 0", bin-e: "e: 0" }
          - mark: { bin-s: good, bin-i: good, bin-l: good, bin-e: good, bin-t: bad, bin-n: bad }
          - say: "Silver takes one tile from bins s, i, l and e. Bins t and n still hold a tile, and the letters v and r have no bin at all. The words do not match."
    refuse: "There is nothing to lift out. Drop the tiles of listen in first."
script: [put-listen, lift-silver, put-listen, lift-silent]
try:
  - text: "Press \"Drop the tiles of listen in\". Then press \"Lift the tiles of silver out\" and look at which bins are left."
    done: [put-listen, lift-silver]
  - text: "Press \"Drop the tiles of listen in\" again to start over. Then press \"Lift the tiles of silent out\" and look at the bins."
    done: [put-listen, lift-silent]
```

**What you should notice:** Every bin ends at 0 only when the second word has the same letters as the first, while silver leaves two bins full and has two tiles with no bin.

## Build the logic

### Notice

The word "listen" has the tiles l, i, s, t, e and n. The word "silent" has the tiles s, i, l, e, n and t. Drop the tiles of "listen" into the bins. Six bins hold one tile each. Now lift the tiles of "silent" out. Each lift finds a tile, and all six bins end at zero.

### Guess

Two words have 1,000 letters each. Which plan takes the fewest steps to say whether they use the same letters?

- A. Put the letters of each word in order, then compare them.
- B. Check every letter of the first word against every letter of the second.
- C. Drop the tiles of the first word into bins and lift the tiles of the second word out.

Idea A works, but it spends effort putting letters in order that counting does not need. Idea B takes about 1,000 times 1,000 steps, which is a million. Idea C goes through each word once, about 2,000 steps, and it is the answer.

### Build

1. Compare the lengths of the two words. If they differ, stop. The answer is no.
2. Set up 26 bins, all at 0.
3. For each tile of the first word, find its bin from the letter and add 1.
4. For each tile of the second word, find its bin and take 1. If a bin drops below 0, stop. The second word has more tiles of that letter than the first. The answer is no.
5. If you get through the whole second word, the answer is yes. The first word added as many tiles as the second word lifted, and no bin went below 0, so every bin ends at 0.
6. The work is one pass over each word. The shelf stays at 26 bins however long the words are. This is general knowledge about counting, not a Kotlin fact.

### Name it

A count of how often each item appears is a **frequency count**. Here the items are letters, and the bins are the counts. In Kotlin the bins are an **IntArray**, an array of whole numbers. The Kotlin documentation shows that `IntArray(5)` makes five numbers that all start at zero, so `IntArray(26)` makes 26 bins that all start at 0. A bin is read and written with square brackets, as in `bins[0]`, and `bins[0]++` adds 1.

The bin for a letter comes from the letter itself. In Kotlin, a letter minus another letter gives a whole number. So `'a' - 'a'` is 0, `'b' - 'a'` is 1, and `'z' - 'a'` is 25. The expression `letter - 'a'` is the bin number for a small letter. The Kotlin documentation says an index outside the array stops the program with an `ArrayIndexOutOfBoundsException`.

```kotlin
fun sameLetters(first: String, second: String): Boolean {
    if (first.length != second.length) return false
    val bins = IntArray(26)
    for (letter in first) {
        bins[letter - 'a']++
    }
    for (letter in second) {
        bins[letter - 'a']--
        if (bins[letter - 'a'] < 0) return false
    }
    return true
}

fun main() {
    println(sameLetters("listen", "silent"))
    println(sameLetters("listen", "silver"))
    println(sameLetters("tea", "team"))
}
```

This prints `true`, then `false`, then `false`. The word "silver" has a v that the bins cannot cover, and "tea" and "team" have different lengths.

## Predict the output

```challenge
type: predict
id: bins-tea-tee
verified: kotlin-2.4.21
code: |
  fun main() {
      val bins = IntArray(26)
      for (letter in "tea") {
          bins[letter - 'a']++
      }
      for (letter in "tee") {
          bins[letter - 'a']--
      }
      println("${bins[0]} ${bins['e' - 'a']} ${bins['t' - 'a']}")
  }
options:
  - id: a
    text: "1 -1 0"
    correct: true
    why: "Tea puts one a, one e and one t in the bins. Tee lifts one t and two e tiles out. So bin a stays at 1, bin e drops to -1 and bin t ends at 0."
  - id: b
    text: "0 0 0"
    why: "You may have thought the two words match. Tea puts an a in the bins and tee never lifts one out, so bin a stays at 1."
  - id: c
    text: "1 0 0"
    why: "You may have lifted only one e. Tee has two e tiles, so bin e goes one below zero."
output: |
  1 -1 0
```

```challenge
type: predict
id: bins-noon-none
verified: kotlin-2.4.21
code: |
  fun main() {
      val bins = IntArray(26)
      for (letter in "noon") {
          bins[letter - 'a']++
      }
      for (letter in "none") {
          bins[letter - 'a']--
      }
      println("${bins['n' - 'a']} ${bins['o' - 'a']} ${bins['e' - 'a']}")
  }
options:
  - id: a
    text: "0 0 0"
    why: "You may have thought the words match because both have four letters. Noon has two o tiles and none has one o and an e."
  - id: b
    text: "0 1 -1"
    correct: true
    why: "Noon drops two n and two o tiles in. Lifting none takes out two n, one o and one e. So bin n ends at 0, bin o at 1 and bin e at -1."
  - id: c
    text: "0 1 1"
    why: "You may have thought lifting adds a tile. Lifting takes one away, so bin e goes below zero to -1."
output: |
  0 1 -1
```

## Try it

1. **Write.** Copy the program from Name it into the editor.
2. **Run.** You should see `true`, `false` and `false`, one on each line.
3. **Break.** In the first call, change `"silent"` to `"silent "` with a space at the end. Run it again. The first line becomes `false`. The space is a seventh tile, so the lengths differ and the check stops at once.
4. **Fix.** Change that call to `sameLetters("listen", "silent ".trim())`. Run it. You should see `true`, `false` and `false` again. The Kotlin function `trim()` removes spaces from both ends of a String.

## Fix the bug

```challenge
type: fix
id: fix-capital-letters
verified: kotlin-2.4.21
code: |
  fun sameLetters(first: String, second: String): Boolean {
      if (first.length != second.length) return false
      val bins = IntArray(26)
      for (letter in first) {
          bins[letter - 'a']++
      }
      for (letter in second) {
          bins[letter - 'a']--
          if (bins[letter - 'a'] < 0) return false
      }
      return true
  }

  fun main() {
      println(sameLetters("silent", "listen"))
      println(sameLetters("Listen", "Silent"))
  }
hints:
  - "Run it. The first line prints true, then the program stops with an error. Look at the first loop: what number is a capital L minus the letter a?"
  - "A capital L is a different tile from a small l, and the shelf has no bin for it. Change both words to small letters before you count."
expected: |
  true
  true
fix: |
  fun sameLetters(first: String, second: String): Boolean {
      if (first.length != second.length) return false
      val bins = IntArray(26)
      for (letter in first.lowercase()) {
          bins[letter - 'a']++
      }
      for (letter in second.lowercase()) {
          bins[letter - 'a']--
          if (bins[letter - 'a'] < 0) return false
      }
      return true
  }

  fun main() {
      println(sameLetters("silent", "listen"))
      println(sameLetters("Listen", "Silent"))
  }
explanation: "A capital L has no bin on a shelf of 26 small letters, so the index falls outside the array. Changing both words to small letters first gives every tile a bin."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For question 3, this code has no null values in play. For question 4, `sameLetters` runs once for each pair of words, and each run has two loops that go once through each word.

## Tricks to remember

- Drop in, lift out, then check that every bin is zero.
- The bin number is the letter minus `'a'`. The letter a is bin 0 and z is bin 25.
- Different lengths mean no, so stop at once.
- Capitals, spaces and emoji are other tiles. Tidy the text before you count.

## Common mistakes

- **Sorting both words when counting is enough.** Sorting is familiar, so it feels safe. It works, but it does more work than counting, which goes through each word once (general knowledge).
- **Checking only that each letter of the second word appears in the first.** It feels like the same test. But "aab" and "abb" pass it, because every letter of "abb" appears in "aab", and their counts differ. Count the tiles, not only the letters.
- **Forgetting capitals and spaces.** The shelf has bins for small a to z only. A capital L has no bin and stops the program in the Fix the bug task. Change to small letters first and decide what to do with spaces.
- **Skipping the length check.** Lifting "tea" out of the bins for "team" never takes a bin below zero, so the check would say yes. A tile m is left over. Comparing lengths first closes that gap.

## Where this shows up in Android

Picture a sign-up screen that rejects a username with a repeated letter, or a search box that must treat "Cafe" and "cafe" as the same word. Counting tiles finds the repeated letter, because a bin above 1 means a repeat. Changing to small letters first is the tidy-up step. The Kotlin documentation says `lowercase()` uses the invariant locale (a fixed set of letter rules, not the phone's language setting), and its example shows that passing a locale such as Turkish can change the result. My own reasoning: the default behaves the same on every phone. The part of this lesson that explains the symptom is the shelf: a letter with no bin breaks the count.

## Say it in an interview

**Why is counting letters in a 26-slot array faster than sorting both words?**
Counting goes through each word once, so it takes about n steps, and the array stays at 26 bins however long the words are. Sorting both words takes more than one pass, about n log n steps (general knowledge). The trade-off is that bins only work when you know the alphabet in advance, here small a to z.

**When must you switch from a 26-slot array to a map?**
Switch when the text can hold more than small a to z: capitals, digits, spaces, accents or emoji. An array for all of them would be huge and mostly empty. A map such as `HashMap<Char, Int>` (a map stores a count for each key) stores a count only for the letters that appear (general knowledge). My own reasoning: the trade-off is that a map lookup costs a little more than an array index.

**How do emoji affect comparing text character by character in Kotlin?**
A `Char` is one UTF-16 code unit, not always a whole character. The Kotlin documentation says one of the emoji that does not fit in a single Char is stored as two `Char` values, so the broccoli emoji has a length of 2. Counting `Char` values would treat one emoji as two tiles. The documentation says to use Unicode code points, stored as `Int` values, to handle such symbols one by one.

**Why does the length check matter in the bins method?**
Without it, "team" against "tea" passes. Lifting the tiles of "tea" never takes a bin below zero, and the m tile is left over, so the code would say yes. Equal lengths plus "no bin below zero" proves every bin ends at zero. This is my own reasoning, traced by hand.

## Recap

1. Two words use the same letters when their letter counts match.
2. Drop word one's tiles into bins, lift word two's out, and every bin must end at zero.
3. This takes one pass over each word, with a shelf of only 26 bins.

## You can now

- Explain how counting tiles in bins checks two words without sorting them.
- Predict the bin counts after one word goes in and another comes out.
- Fix a letter-count check that stops the program on capital letters.

## Practise on these

Say the cost of the counting way before you code. The list runs from the gentlest to the hardest.

1. LeetCode 242, Valid Anagram (Easy). Use a 26-slot counter to compare two words in one pass each. https://leetcode.com/problems/valid-anagram/
2. LeetCode 383, Ransom Note (Easy). Check that one text has enough of every tile to build another. https://leetcode.com/problems/ransom-note/
3. LeetCode 387, First Unique Character in a String (Easy). Count every letter first, then find the first letter whose count is 1. https://leetcode.com/problems/first-unique-character-in-a-string/
4. LeetCode 49, Group Anagrams (Medium). Group words by their letter counts without sorting each word. https://leetcode.com/problems/group-anagrams/
5. LeetCode 438, Find All Anagrams in a String (Medium). Keep a moving window of 26 counts and match it against a word. https://leetcode.com/problems/find-all-anagrams-in-a-string/
