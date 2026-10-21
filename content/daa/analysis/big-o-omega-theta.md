---
id: daa/analysis/big-o-omega-theta
title: What do O, Omega and Theta each promise?
status: live
difficulty: medium
order: 2
requires: [daa/analysis/best-worst-average-case]
summary: O is a ceiling on the steps, Omega is a floor, and Theta says both hold. None of them means worst case.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [ceiling-and-floor-band]
playground: false
fruit: false
upnext: false
---


## The question

A library page says an operation takes O(n) time. A code comment above a nested loop also says O(n). A friend says: "Big O is just the worst case." Are these three people saying the same kind of thing? And how would you say "at least this much" or "exactly about this much"?

## Mental model

Think of promises about a car trip. A trip of d km takes 3 minutes per km, plus 2 minutes to leave the car park. So the time is 3d + 2 minutes.

- A **ceiling** promise: "never more than 4 minutes per km, once you are 2 km in." The trip stays under that line.
- A **floor** promise: "never less than 3 minutes per km." The trip stays above that line.
- A **band** promise: both at once. The trip stays between the floor and the ceiling.

**Where the analogy stops:** One trip has one distance. An algorithm's promise has to hold for every input size above a starting point, all at once. Also, a trip time is measured in minutes. An algorithm's cost here is a count of steps.

## See it move

<!-- SIM: ceiling-and-floor-band -->

**Try this:**
1. Pick the shape n. Drag the floor and the ceiling constants until the curve 3n + 2 stays between the two lines from n = 2 onwards.
2. Pick the shape n². Try to place a floor line under the curve for every large n.
3. Drag the ceiling constant low, for example to 2, and watch the curve sit above the line at every n.

**What you should notice:** With the shape n, a floor and a ceiling can both hold. With the shape n², the ceiling holds and the floor fails, and a ceiling below the curve fails once n is large.

## Build the logic

### Notice

A piece of code takes 3n + 2 steps for an input of size n. Someone writes three claims about it.

1. The code is O(n).
2. The code is O(n²).
3. The code is Θ(n²).

Exactly one claim is false.

### Guess

Which one?

- A. Claim 1, because O(n) is a promise about the worst case only.
- B. Claim 3, because the curve never gets close to n².
- C. Claim 2, because n² is much bigger than 3n + 2.

Idea A mixes up the bound with the case. Idea C treats a loose ceiling as a lie, but a ceiling that is too high is still true. Idea B is the answer, and the steps below show why.

### Build

1. Say what you want to promise. A ceiling: the steps stay below a line. A floor: the steps stay above a line. A band: both.
2. Pick the shape of the line, for example n.
3. Pick a constant for the line, like "4 times n".
4. Pick a starting point, called n₀. The promise only has to hold from there on, like "once you are 2 km in".
5. Check the promise for every n from n₀ upwards.

Try it on 3n + 2.

- Ceiling: 3n + 2 ≤ 4n holds for every n ≥ 2. At n = 2 both sides are 8. So the constant is 4 and n₀ is 2.
- Floor: 3n ≤ 3n + 2 holds for every n ≥ 1. So the constant is 3.
- Both hold with the shape n, so the code is in the band between 3n and 4n.

Now try the shape n² as a floor. You need a constant c with c × n² ≤ 3n + 2 for every large n. At n = 1,000 that forces c to be about 0.003 or less. At n = 1,000,000 it forces c below 0.000004. The allowed c keeps shrinking towards zero, so no positive constant works. A floor of shape n² fails.

### Name it

These three are the standard definitions (general knowledge). Here f(n) is the step count and g(n) is the shape you compare it with.

- **O(g(n))**, read "big O", is a **ceiling**. It says there are constants c > 0 and n₀ so that f(n) ≤ c × g(n) for every n ≥ n₀.
- **Ω(g(n))**, read "big Omega", is a **floor**. It says f(n) ≥ c × g(n) for every n ≥ n₀.
- **Θ(g(n))**, read "big Theta", is a **tight bound**. It says both a floor and a ceiling of the same shape g hold, possibly with two different constants.

So 3n + 2 is O(n), Ω(n) and Θ(n). It is also O(n²), because a ceiling may be loose. It is not Θ(n²), because the floor fails.

Now the part people mix up. There are two separate questions.

| Question | The choices | Example for linear search of n items |
|---|---|---|
| Which input do I measure? | best case, worst case, average case | best case: 1 step. Worst case: n steps. |
| What kind of promise do I make about those steps? | O ceiling, Ω floor, Θ tight | The worst case is Θ(n). The best case is Θ(1). |

"Big O means the worst case" blends the two questions. O is a ceiling and you can put it on any case. For linear search, all inputs together are O(n) and Ω(1).

```kotlin
fun steps(n: Int): Int = 3 * n + 2

fun main() {
    var linearBand = true
    var squareFloor = true
    for (n in 2..1000) {
        if (steps(n) < 3 * n || steps(n) > 4 * n) linearBand = false
        if (steps(n) < n * n / 10) squareFloor = false
    }
    println(linearBand)
    println(squareFloor)
}
```

This prints `true`, then `false`. The first check stays true because 3n ≤ 3n + 2 ≤ 4n for every n from 2 to 1,000. The second check fails once n reaches 31, where 3n + 2 is 95 and n × n / 10 is 96. The algebra above is the proof.

## Predict the output

```challenge
type: predict
run: false
id: which-claim-is-false
verified: not-run
code: |
  A piece of code takes 5n + 1 steps.
  Three claims:
    1. It is Θ(n²).
    2. It is O(n³).
    3. It is Ω(n).
  Which claim is false?
options:
  - id: a
    text: "Claim 1"
    correct: true
    why: "Θ needs a floor and a ceiling of the same shape. No floor of shape n² can stay under 5n + 1 for large n, so the band fails."
  - id: b
    text: "Claim 2"
    why: "You may have thought a ceiling that is far too high is wrong. It is loose, like 'under a week', but it is still true."
  - id: c
    text: "Claim 3"
    why: "You may have thought a floor has to be as big as the curve. A floor of 5n stays under 5n + 1 from n = 1 on, so claim 3 is true."
output: |
  Claim 1
```

```challenge
type: predict
run: false
id: ceiling-on-a-best-case
verified: not-run
code: |
  Linear search of n items.
  Best case: the target is first, so 1 step.
  A developer writes: "The best case is O(1)."
  Is that allowed?
options:
  - id: a
    text: "No. Big O describes the worst case only"
    why: "You may have blended the two questions. Which input and which promise are separate choices."
  - id: b
    text: "No. A best case needs Ω, never O"
    why: "You may have matched best with floor. A floor can also describe the worst case. A best case of exactly 1 step is both O(1) and Ω(1)."
  - id: c
    text: "Yes. O is a ceiling, and a ceiling can describe any case"
    correct: true
    why: "The case says which input you measure. O says what kind of promise you make about it. The best case of 1 step stays under the line 'a constant'."
output: |
  Yes. O is a ceiling, and a ceiling can describe any case
```

```challenge
type: predict
run: false
id: ceiling-set-too-low
verified: not-run
code: |
  A piece of code takes n * n steps.
  Someone claims: "It is O(n)" and offers the ceiling 100 * n.
  The ceiling holds at n = 50: 2500 steps against 5000.
  At what size does it first break?
options:
  - id: a
    text: "It never breaks, because 100 is a big constant"
    why: "You may have thought a big constant is enough. n * n overtakes 100 * n once n passes 100, and a bigger constant only moves that point."
  - id: b
    text: "At n = 101"
    correct: true
    why: "At n = 100 both sides are 10000. At n = 101 the code takes 10201 steps against a ceiling of 10100. A ceiling of shape n is too low."
  - id: c
    text: "At n = 50"
    why: "You may have used the example size. At n = 50 the code takes 2500 steps and the ceiling is 5000, so it still holds."
output: |
  At n = 101
```

## Tricks to remember

- O is a ceiling, Ω is a floor, Θ is a floor and a ceiling together.
- Two questions: which input, and which promise. Do not mix them.
- A loose ceiling is true but tells you little. Ask for Θ when you want it tight.
- The promise only has to hold from n₀ onwards.

## Common mistakes

- **Thinking Big O means worst case.** People say "O(n)" when they quote the worst case, so the two words stick together. O is a ceiling and you can put it on any case.
- **Thinking Ω means best case.** Ω and best case both sound like "at least". A floor can describe the worst case too. For linear search the worst case is Ω(n) and the best case is Ω(1).
- **Thinking O(n) rules out O(n²).** A ceiling that is too high feels like a mistake. It is still true, so say Θ when you mean "exactly about".
- **Trusting a complexity comment because it is written confidently.** A neat comment reads like a proof. Check the loops under it before you believe it.

## Where this shows up in Android

Android code is full of claims about cost, and they come from three places: library pages, code reviews and comments, including comments an AI tool wrote. For example, the Java SE page for `ArrayList` says that `size`, `get` and `set` run in constant time, that `add` takes amortized constant time (adding n elements takes O(n) time), and that all of the other operations run in linear time, "roughly speaking". That text is from Java SE, not from an Android page. Android apps use a class with the same name (general knowledge).

Now picture a comment `// O(n)` above a loop with another loop inside it. This part is my own reasoning. If both loops run over the same list, the code takes about n × n steps, so the comment is a ceiling that is too low. The symptom is a screen that is fine at 20 items and stutters at 5,000. The lesson is to ask three things of any claim: is it a ceiling, a floor or both, which case does it describe, and does the code under it match?

## Say it in an interview

**What is the difference between Big O, Big Omega and Big Theta?**
Big O is a ceiling: the cost grows no faster than the shape you name, from some size onwards. Big Omega is a floor: it grows at least that fast. Big Theta is both at once, so the shape is tight. Each one is a statement about a function, and you can aim it at the best, average or worst case.

**Is an O(n) algorithm also O(n²)? Why do people say O(n) when they mean Θ(n)?**
Yes, because a ceiling can be loose, so anything that is O(n) is also O(n²) and O(2ⁿ). People say O(n) because it is short, and they mean the tight answer. When it matters, say Θ(n) or say "linear in the worst case".

**Why is merge sort Θ(n log n) in every case, while insertion sort has no single Θ for all inputs?**
Merge sort and insertion sort are two ways to sort a list. This answer is general knowledge. Merge sort does about n log n work whatever the order of the input. Insertion sort takes about n steps on a sorted list and about n² steps on a reversed list. So over all inputs it is Ω(n) and O(n²), and no one shape is tight for all of them. Each case on its own does have a Θ: Θ(n) at best and Θ(n²) at worst.

**A code review says "this comment claims O(n) but I see a nested loop." What do you check?**
I check what each loop runs over. If both run over the same input, the cost is about n × n, so an O(n) ceiling is too low. If the inner loop runs over a fixed-size list, the claim can be right. This is my own reasoning: a complexity comment is a claim, and I read the code to test it.

## Recap

1. O is a ceiling on the steps, Ω is a floor, and Θ is both together.
2. Which input you measure and which promise you make are two separate choices.
3. A promise only has to hold from some starting size onwards.

## You can now

- Pick the false claim among O, Ω and Θ statements about 3n + 2.
- Say why "Big O means worst case" mixes up two separate questions.
- Find the size where a ceiling that is too low first breaks.

## Practise on these

Say which case each bound covers. The list runs from the gentlest to the hardest.

1. LeetCode 704, Binary Search (Easy). The best case is 1 check and the worst case is about log n checks. Say which case your bound covers. https://leetcode.com/problems/binary-search/
2. LeetCode 268, Missing Number (Easy). Every number must be looked at, so no method can use fewer than about n steps. Show a method that uses about n. https://leetcode.com/problems/missing-number/
3. LeetCode 75, Sort Colors (Medium). A one-pass method takes a number of steps tight to n and uses a fixed amount of extra memory. https://leetcode.com/problems/sort-colors/
4. LeetCode 148, Sort List (Medium). Merge sort takes about n log n steps whatever the input, so the bound is tight. https://leetcode.com/problems/sort-list/
5. LeetCode 41, First Missing Positive (Hard). The statement asks for time tight to n with fixed extra memory, which a swap-into-place method achieves. https://leetcode.com/problems/first-missing-positive/
