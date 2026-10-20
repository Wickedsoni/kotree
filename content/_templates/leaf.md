---
id: <branch>/<stem>/<leaf>
title: <Title as a question or a plain promise; must equal the roadmap title>
status: shadow
difficulty: easy
order: 1
requires: []
summary: <One sentence for the leaf popup>
video: ""
publishedOn:
updatedOn:
sims: []
playground: true
fruit: false
upnext: false
---

<!--
TEMPLATE FOR ONE LEAF. Rules live in docs/04-lesson-system.md. Delete this comment block in a real leaf.

FRONTMATTER
- Keys and order follow docs/00 section 8.2. Do not add keys. Do not remove keys.
- id, title, difficulty, requires, fruit must equal the leaf's row in content/<branch>/_roadmap.md (build rule V5).
- status stays `shadow` until reviewer says PASS. Only housekeeper sets `live`.
- publishedOn and updatedOn stay empty until live. Format YYYY-MM-DD.
- video: "" means NO video. While it is empty the page shows no Watch-the-video button and no video section (L26).
  When the author uploads the video, paste the URL here and nothing else. Accepted forms:
  https://www.youtube.com/watch?v=<id>  or  https://youtu.be/<id>
- sims: list every sim id used by a SIM marker below. Empty list = no See it move section.
- playground: true if and only if the page has at least one RUNNABLE block (docs/06 section 1): the code of a
  challenge block whose `run` is true or absent, or the code in Try it. Name it blocks are read-only and do not count.
  true = Try it and Fix the bug are kept. false = a theory leaf: delete Try it and Fix the bug, and give every
  Predict item `run: false` (paper-and-pencil) and `verified: not-run`.
- MAC-NEEDED LEAVES ONLY (roadmap Notes say `Mac needed`; docs/04 section 4.13): put one plain-sentence label on its own line
  after each iOS-side claim, in these exact words: "Ran on my Windows machine" | "Seen in this CI run" (+ link) |
  "From the docs, not run by me". No new frontmatter field. Never claim an iPhone run from CI evidence alone.
- upnext: optional. Leave false or delete the line. Must never be true on a live leaf (build rule V9).

WHAT YOU DO NOT WRITE IN THIS FILE (the page layout adds it from data)
- Breadcrumb, title, difficulty chip, progress line.
- The Watch-the-video button and the Video section. Never write a "Video" heading here.
- The "I finished this" toggle, Previous / Next, "Back to the tree".
- There is no Capstone-link section (L26).

BODY RULES (short version)
- Keep the 12 headings below, in this order, with these exact names.
- Delete a whole section only where docs/04 section 4 says it may be left out. Never leave an empty heading.
- First screenful: the question, a question mark, no term the learner has not met.
- Every `kotlin` block is a complete program with fun main(), or its first line is `// does not compile`.
- Mark anything not yet checked against official docs with the word VERIFY and a warning sign. No VERIFY may remain when status becomes live.
- The SIM marker comment below is filled in later by sim-engineer. Keep the plain-text content next to it.
- Challenges are fenced `challenge` blocks (YAML), defined in docs/06 section 3. There is no CHALLENGE comment marker.
  Predict: 1 to 3 blocks, exactly 3 options each. Fix the bug: exactly 1 block, exactly 2 hints.
  `verified` is `kotlin-2.4.21` only after the code was run with that version; until then write `"UNVERIFIED: <reason>"`.
  A leaf with any UNVERIFIED stays shadow.
-->

## The question

<!-- 2 to 5 sentences. Start with a real situation and a question the learner can feel. No jargon. No code. -->

## Mental model

<!-- ONE analogy for the whole lesson. Name the objects once. Then add the sentence:
**Where the analogy stops:** ... -->

## See it move

<!-- Optional. Omit this section only if sims is [] and the roadmap row says why. -->
<!-- SIM: sim-id -->

**Try this:**
1. <One thing to drag, push or change.>
2. <A second thing.>
<!-- One to three items. A sim may add one optional reveal step as the last item (for example, a tick-box that shows the safe path). Delete this line if the sim has none. -->

**What you should notice:** <One sentence. The page must still make sense if the sim does not load.>

## Build the logic

### Notice

<!-- A concrete case, in words or boxes. Use the same objects as the analogy. -->

### Guess

<!-- Ask: what would the computer need to do here? Give 2 or 3 honest options before the answer. -->

### Build

<!-- Put the answer together step by step, in plain words or a small diagram. No keywords yet. -->

### Name it

<!-- Only now: the official term in bold with its plain meaning, and the Kotlin keyword or syntax.
First use of each technical term on the page is bold. -->

```kotlin
fun main() {
    // Smallest program that shows the named idea.
}
```

## Predict the output

<!-- Learner guesses first (P6, D9). 1 to 3 items, each its own challenge block (copy the block below).
Code is 3 to 12 lines. Exactly 3 options (a, b, c), exactly one `correct: true`. Every option has a one-line `why`:
the correct one explains the rule using the analogy; each wrong one treats the guess as a clue.
`output` is the exact stdout copied from a real run (docs/06 section 5). The answers live in the block, so the section works offline.
Theory leaf (playground: false): add `run: false`, make `code` a table or trace to follow on paper, and set `verified: not-run`. -->

```challenge
type: predict
id: replace-me-predict
verified: "UNVERIFIED: not run yet"
code: |
  fun main() {
      println("replace me")
  }
options:
  - id: a
    text: "<option>"
    why: "<One sentence that treats this guess as a clue.>"
  - id: b
    text: "<option>"
    correct: true
    why: "<One or two short sentences, using the analogy.>"
  - id: c
    text: "<option>"
    why: "<One sentence that treats this guess as a clue.>"
output: |
  replace me
```

## Try it

<!-- Needs playground: true. Omit if playground is false.
The write, run, break, fix loop. Four numbered steps, in this order. -->

1. **Write.** <What to type or edit.>
2. **Run.** <What the learner should see. Describe the result in plain words.>
3. **Break.** <Change exactly one thing so it fails.>
4. **Fix.** <How to repair it and what the output should be again.>

## Fix the bug

<!-- Needs playground: true. Omit if playground is false.
Exactly one challenge block. Code is 10 to 30 lines with one realistic bug. Where the leaf's roadmap row has an
`F(ai):` task in its Challenges column (its stem has `ai-review` in the `Flags:` line, docs/08 sections 2.2 and 5),
write tidy, confident code with one wrong assumption and add `generated: true`.
Exactly 2 hints. `expected` is the exact stdout of the fixed program. `fix` is a complete program.
Keep the five reviewer questions below the block, unchanged. -->

```challenge
type: fix
id: replace-me-fix
verified: "UNVERIFIED: not run yet"
code: |
  fun main() {
      println("replace me")
  }
hints:
  - "<Hint 1: points at a line, not at the fix.>"
  - "<Hint 2: names the idea.>"
expected: |
  replace me
fix: |
  fun main() {
      println("replace me")
  }
explanation: "<Optional, one sentence.>"
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

## Tricks to remember

<!-- 2 to 4 short memory aids: a rhyme, a picture, a rule of thumb. One line each. -->

## Common mistakes

<!-- 2 to 4 items. Each: the mistake, why it feels right, what to do instead. Mistakes are clues, not failures. -->

## Where this shows up in Android

<!-- Reading and pictures only. No emulator, no app to run. Name one real symptom and say which part of the lesson explains it.
Theory leaves (DSA, DAA, OS, networking) must name the app symptom here. -->

## Say it in an interview

<!-- Decision L33. 3 to 4 questions an interviewer really asks about this topic, each followed by a model answer in plain words (2 to 4 short sentences, easy to say aloud). Cover the WHY and the trade-off, not only the definition. Label inferred points "my own reasoning". For DSA, DAA and OS leaves add a following "## Practise on these" section with real LeetCode and HackerRank problems (number, title, difficulty, one line on what each trains). -->

## Recap

<!-- Exactly three lines. Each line is one sentence under 20 words. -->

1. <Line one.>
2. <Line two.>
3. <Line three.>

## You can now

<!-- 2 to 4 lines. Each starts with a verb and is something the learner can test alone, without looking at the page. -->

- Explain <...> in your own words.
- Predict <...>.
- Fix <...>.
