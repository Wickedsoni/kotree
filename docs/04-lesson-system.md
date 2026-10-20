# 04 — Lesson System

> **Purpose:** defines what one lesson page contains, in what order, and how it is written and checked (D7). It is the contract between the author, `lesson-writer`, `reviewer`, the layout code and the sim and challenge builders.
> **Read this when:** you write, review or lay out a leaf; you build the lesson layout; you add a sim or a challenge marker.
> **Depends on:** `docs/00-decisions.md` (wins every conflict; L1, L13, L15, L19, L26, D7–D9, §8.2), `docs/01-vision-and-principles.md` (P5–P10, §3, §7), `docs/03-information-architecture.md` (links, checkmarks, build rules V4, V5, V9), `docs/07-technical-architecture.md` (render and video rules; `docs/adr/005-lesson-content-delivery.md`). Sims are owned by `docs/05`. The editor, the `challenge` block format and the challenge widgets are owned by `docs/06`.
> **Principles used:** P5 (why before how), P6 (learner acts, then reads), P7 (plain English), P8 (small finished leaves), P9 (facts checked), P10 (page works without the video).
> **Skeleton file:** `content/_templates/leaf.md`. The section names in §4 below and in that file must match exactly (see acceptance in §9).
> **Version:** v1 · written 18 Oct 2026 · §5.3 aligned with ADR 005 on 20 Oct 2026.

---

## 1. Page anatomy

A lesson page has two kinds of parts. **Authored** parts are written in the leaf's Markdown file. **Layout** parts are drawn by the page layout from data, so nobody writes or edits them in a leaf.

### 1.1 Top to bottom

| Order | Part | Kind | Data source | Shown when |
|---|---|---|---|---|
| 1 | Breadcrumb `Kotree › Bough › Branch › Stem` | Layout | Tree data (`docs/03` §6.3) | Always |
| 2 | Title, difficulty chip ("Easy topic" / "Medium topic" / "Hard topic"), progress line | Layout | Frontmatter `title`, `difficulty`; counts (`docs/03` §8) | Always |
| 3 | **Watch the video** button | Layout | Frontmatter `video` | Only when `video` is non-empty (L26) |
| 4 | Authored body: the 12 sections in §4 | Authored | The leaf file | Always (some sections may be left out, §4) |
| 5 | **Video section** (click-to-load player, `docs/07` §6.5) | Layout | Frontmatter `video` | Only when `video` is non-empty (L26) |
| 6 | "I finished this" toggle (the wording of `docs/03` §6.1 and §9) | Layout | Local storage (`docs/03` §9) | When local storage works |
| 7 | Previous / Next, "Back to the tree" | Layout | Tree data (`docs/03` §6.3) | Always |

Rules:

- The author **never writes** a Video heading, a video link, a Previous/Next link, an "I finished this" button or a Capstone link in the body. A body heading would show an empty box while `video` is empty, and a hand-written link goes stale when the tree grows.
- There is **no Capstone-link section** on any lesson page (L26). Capstones are fruits on the tree (D12).
- With `video: ""` (or missing, or only spaces) parts 3 and 5 do not exist: no placeholder, no disabled button, no "coming soon" (L26, `docs/07` §6.5). A leaf can be live without a video.
- The video section is placed **after** the body, not inside it. Reason: the page must carry the whole lesson alone (P10), so a learner who watches the video should still find the body complete, and a learner who skips the video should not meet a gap in the middle.
- Previous and Next are the nearest `live` leaves in branch `order`. The up-next leaf may appear as "Coming next" without a link (`docs/03` §6.3).

Rejected: a `## Video` heading inside the template, hidden by the layout when empty. It would put a layout decision into content files and make "add the URL, change nothing else" false.
Rejected: placing the video section right under the question. It makes the page look like a video with notes, which breaks P10.

### 1.2 What the learner does on the page

```
read the question → picture the analogy → push the sim → follow the logic being built
→ guess the output → run it → break it → fix someone else's code → recap → "You can now…"
```

The order moves from **asking** (question), to **showing how it works** (analogy, sim), to **building the logic** (notice, guess, build, name), to **doing** (predict, try, fix). This is the order the author asked for: questions first, then how it works, then logic step by step.

---

## 2. Frontmatter

Keys, order and value formats are fixed by `docs/00` §8.2. This table only explains how to fill them. Do not add keys.

| Key | Type | Rule for a leaf file |
|---|---|---|
| `id` | string | `<branch>/<stem>/<leaf>`. Must equal the file path (V4). |
| `title` | string | Equal to the roadmap title, letter for letter (V5). A question where possible (`docs/01` §3.1). |
| `status` | `shadow` \| `upnext` \| `live` | New files start as `shadow`. Only `housekeeper` sets `live`, and only after `reviewer` PASS (CLAUDE.md rule 4). Only `live` files become pages (`docs/07` §6.2). |
| `difficulty` | `easy` \| `medium` \| `hard` | Equal to the roadmap (V5). Describes the topic, never the learner (`docs/00` §7). |
| `order` | integer | 1-based position in the branch, counting across stems. Equal to the roadmap order. |
| `requires` | list of leaf IDs | Equal to the roadmap (V5). Same branch only. |
| `summary` | string | One plain sentence for the popup. Under 25 words. No term the popup reader has not met. Not a copy of the title. |
| `video` | string | `""` while there is no video. Later, one `https://www.youtube.com/watch?v=<id>` or `https://youtu.be/<id>` URL (`docs/07` §6.4). Setting it is a one-line edit and makes the button and video section appear (L26). |
| `publishedOn` | `YYYY-MM-DD` | Empty until `live`. Set by `housekeeper`. |
| `updatedOn` | `YYYY-MM-DD` | Empty until `live`. Updated on every fact fix (`docs/01` §9). |
| `sims` | list of sim IDs | Every ID used by a `SIM` marker in the body. `[]` means the page has no See it move section. IDs come from `docs/05`. |
| `playground` | boolean | `true` if and only if the page has at least one **runnable block**, defined in `docs/06` §1: the `code` of a `challenge` block with `run` true or absent, or the code in Try it. Name it blocks and `// does not compile` blocks are read-only and do not count. `true` means Try it and Fix the bug are present; `false` is a theory leaf whose Predict items are paper-and-pencil (`run: false`) (`docs/07` §8 loads the Playground only when `true`). |
| `fruit` | boolean | Equal to the roadmap (V5). |
| `upnext` | boolean, optional | `true` forces this leaf to be its branch's up-next leaf (D15). Leave `false` or delete the line. Must never be `true` on a `live` leaf (V9). |

Acceptance:

- [ ] Every key of `docs/00` §8.2 is present, in that order, and no other key is.
- [ ] `sims` lists exactly the IDs that appear in `SIM` markers.
- [ ] `playground` is `true` exactly when the body has a runnable block (`docs/06` §1). It is `true` if any `challenge` block has `run` true or absent, and then Try it and Fix the bug exist. It is `false` only when every `challenge` block is a `run: false` Predict item and neither Try it nor Fix the bug exists.

---

## 3. Writing rules that apply to every section

### 3.1 Question-first (`docs/01` §3.1)

- The first screenful has a question mark, a real situation, and **no term the learner has not met**. Foundation is assumed in words, never by name-dropping.
- The title is the question where possible.
- The first screenful contains no code block and no bold technical term.

### 3.2 One running analogy

Each lesson uses **one** real-life analogy, picked from objects a 14-year-old knows (a lunch box, a queue, a lift, a bank passbook).

1. Introduce it in **Mental model**, with every object named once.
2. Reuse the same objects in **Build the logic**, in the **Why** line of each Predict item, and in **Common mistakes**.
3. In Mental model, write one sentence starting **Where the analogy stops:** and say what is false about it. Every analogy breaks somewhere; the learner must hear where before the break misleads them.
4. Never switch to a second analogy to patch the first. If the first analogy cannot carry the lesson, the leaf is probably two leaves (P8).

Acceptance: the Mental model section contains the words "Where the analogy stops"; no second analogy object appears in later sections.

### 3.3 Logic building: notice, guess, build, name (`docs/01` §3.2)

The **Build the logic** section has four `###` sub-headings, always in this order:

| Step | The learner is asked to | Text rule |
|---|---|---|
| Notice | Look at one concrete case | Plain words or boxes. Same objects as the analogy. No code. |
| Guess | Say what the computer must do | Ask the question first. Give two or three honest options (one may be wrong in a believable way) before the answer. |
| Build | Put the answer together | Numbered steps, plain words. Still no keyword. |
| Name it | Meet the official term | Now give the bold term with its plain meaning and the Kotlin keyword or syntax, plus the smallest program that shows it. |

The rule behind it: a learner who forgets the keyword can rebuild it from the logic.

Acceptance: no Kotlin keyword or operator appears in Notice, Guess or Build except inside a quoted learner guess; every term first used in Name it is bold exactly once on the page.

### 3.4 See it move (`docs/01` §3.3)

- A leaf about memory, time, order, flow or state change has at least one sim, or its roadmap row says why not.
- The sim is placed with `<!-- SIM: sim-id -->`. Directly under the marker the author writes, in plain text: **Try this** (one to three things to drag, push or change, plus at most one optional reveal step) and **What you should notice** (one sentence).
- An optional reveal step shows the safe or fixed path after the learner has seen the problem (for example a "Look inside first" tick-box). It is written as the last Try-this item, so the numbered items still match the sim spec in `docs/05`. The sim spec decides whether it exists.
- The sim only shows what the text already says. If the sim fails to load, the page still makes sense.
- Which physics engine and what moves is decided in `docs/05`. This doc fixes only the slot.
- No Android app or emulator exists on the site (D8, `docs/00` §9). Where a leaf is about Android, the sim is an animation or a `code-to-screen` sim from `docs/05` (no simulator).

### 3.5 Write, run, break, fix (`docs/01` §3.4)

The loop is split across three sections so each has one job:

| Step | Section | Notes |
|---|---|---|
| (guess first) | Predict the output | The learner commits to a guess **before** anything runs (P6, D9). |
| Write, Run, Break, Fix | Try it | Four numbered steps, in this order. Break changes exactly one thing. |
| Fix | Fix the bug | Someone else's code, with an expected output. |

Rejected: a `Code` section before Predict (the old placeholder order). The learner would see the output first, which defeats P6.

### 3.6 Reading AI-generated code (`docs/01` §3.5)

Fix the bug tasks are written in the style of generated code: tidy names, confident structure, one wrong assumption. Every Fix the bug section carries the same five reviewer questions, in this order:

1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For a leaf whose idea has nothing to do with the third or fourth question, write "This code has no null values in play" or similar, so the learner practises the habit. The curriculum designer marks in `_roadmap.md` which stems carry AI-style tasks (`docs/01` §3.5). The marker is settled in `docs/08` §2.2 and §5 (read 18 Oct 2026): the stem's `Flags:` line contains `ai-review`, and each such task is written `F(ai):` in the leaf row's Challenges column (the `kotlin` roadmap already uses both). `challenge-builder` sets `generated: true` on a Fix the bug block only for an `F(ai):` task. A leaf that is not marked still uses the five questions, but its code may be plain.

Never say AI is bad. Say: the person who can check it is the person who stays in the job.

---

## 4. The twelve body sections

Heading names are exact (`## ` level). Order is fixed. The same twelve names appear in `content/_templates/leaf.md`.

| # | Heading | Required? | Job |
|---|---|---|---|
| 1 | `## The question` | Always | Hook with a real situation (P5). |
| 2 | `## Mental model` | Always | The one analogy and where it stops. |
| 3 | `## See it move` | Unless `sims: []` and the roadmap row says why | Something the learner can push (L14). |
| 4 | `## Build the logic` | Always | Notice, Guess, Build, Name it (`docs/01` §3.2). |
| 5 | `## Predict the output` | Always | Guess before running (D9). |
| 6 | `## Try it` | Only if `playground: true` | Write, run, break, fix (L15). |
| 7 | `## Fix the bug` | Only if `playground: true` | Repair code and compare the output (D9). |
| 8 | `## Tricks to remember` | Always | Memory aids (L13). |
| 9 | `## Common mistakes` | Always | Known wrong turns, as clues. |
| 10 | `## Where this shows up in Android` | Always | Link to a real app symptom. |
| 11 | `## Recap` | Always | Three lines. |
| 12 | `## You can now` | Always | Self-test abilities (`docs/01` §6.2). |

A section that may be left out is **removed with its heading**. A heading with no content never ships. A theory leaf with `playground: false` has no Try it and no Fix the bug; its Predict the output items are paper-and-pencil (count steps, trace a table): `challenge` blocks with `run: false`, with the answers written in the block.

### 4.1 `## The question`

- 2 to 5 sentences. A real situation, then the question. Example shape: "A user leaves a box blank. The app closes. Why?"
- No code block, no bold technical term, no unmet term.
- The question may repeat the title's question in other words.
- Acceptance: contains `?`; a Tutorial Survivor could read it without scrolling and say what the page is about.

### 4.2 `## Mental model`

- Introduces the analogy (§3.2). Maps each analogy object to the idea it stands for, in a short list or sentence. Does not yet use Kotlin words.
- Ends with **Where the analogy stops:** and one or two sentences.
- Acceptance: the sentence "Where the analogy stops" exists; every analogy object is named once.

### 4.3 `## See it move`

- Layout as in §3.4: the SIM marker, **Try this**, **What you should notice**.
- The marker form is exactly `<!-- SIM: sim-id -->` with the ID from `docs/05`, and the same ID is listed in frontmatter `sims`.
- Until `sim-engineer` builds the sim, the marker stays and the plain text still teaches. No placeholder text such as "sim coming soon" on a live page: a live leaf either has a working sim or no See it move section (P2).
- Acceptance: marker ID is in `sims`; the text works with the sim switched off.

### 4.4 `## Build the logic`

- Four `###` sub-headings: `### Notice`, `### Guess`, `### Build`, `### Name it` (§3.3).
- Name it ends with one complete `kotlin` program showing only the named idea. It must compile, but it is shown as read-only highlighted code; it is not a runnable block (`docs/06` §1). The learner runs code in Try it and in the challenges. A theory leaf may show a table or trace instead of a program.
- Acceptance: four sub-headings in order; no Kotlin syntax before Name it.

### 4.5 `## Predict the output`

- Format in §5.1. One to three items, each its own `challenge` block (`type: predict`), directly under the heading.
- Acceptance: each item has code, exactly three options, a `correct: true` option, and a `why` on every option; `output` is present. The answers are in the block, so the section works offline (`docs/01` §9).

### 4.6 `## Try it`

- Four numbered steps: **Write**, **Run**, **Break**, **Fix**, in that order. Break changes exactly one thing.
- Run describes the result in words, because the learner's own output appears in the editor.
- Acceptance: four steps in order; the code of the Run step compiles as written.

### 4.7 `## Fix the bug`

- Format in §5.2. Exactly one `challenge` block (`type: fix`) under the heading, followed by the five reviewer questions as plain Markdown.
- Acceptance: `expected` is present; exactly two hints exist; `fix` exists; the five reviewer questions (§3.6) are present.

### 4.8 `## Tricks to remember`

- 2 to 4 one-line memory aids: a picture, a rule of thumb, a rhyme. Each is tied to the analogy or to a keyword.
- Acceptance: no new fact appears here that is not taught above.

### 4.9 `## Common mistakes`

- 2 to 4 items. Each has three parts: the mistake, why it feels right, what to do instead. Wording treats a mistake as a clue (§7).
- Acceptance: each mistake is something a real learner does, not a straw man.

### 4.10 `## Where this shows up in Android`

- Reading and pictures only. No "run this on an emulator" step (no app or emulator exists, `docs/00` §9).
- Names one real symptom ("the form data vanished", "the screen froze") and says which part of this lesson explains it.
- For theory leaves, this section is where the theory meets a phone (`docs/01` §2.3).
- Facts here are checked against official Android or Kotlin docs (P9) and cite the page in a comment for the reviewer, or carry `⚠️ VERIFY`.

### 4.11 `## Recap`

- Exactly three numbered lines, each under 20 words. They match the three biggest ideas in the body.

### 4.12 `## You can now`

- 2 to 4 bullet lines. Each begins with a verb (Explain, Predict, Spot, Fix, Sort, Write) and names something the learner can do **without looking at the page**.
- Each line must be backed by something on the page: a Predict item, the Try it loop or a Fix the bug task. A line with no matching task is cut.
- The lines come from the checkpoint abilities in `docs/01` §6.2 where that branch has one.
- Acceptance: every line is testable alone; none says "understand" or "know" without an observable verb.

### 4.13 Evidence labels on `Mac needed` leaves (`docs/08` §8.1, L27)

Applies only to a leaf whose roadmap Notes say `Mac needed`. The author is on Windows, so each claim about the iOS side carries one of three plain-sentence labels, copied exactly. They are body text, not a new section and not a frontmatter field (`docs/00` §8.2 is locked).

| Label (exact words) | Use it when | What follows |
|---|---|---|
| Ran on my Windows machine | The author built and ran the code or Gradle step on Windows (concept, `commonMain`, `androidMain`, Gradle files, plain Kotlin). | Nothing extra. |
| Seen in this CI run | The iOS side was built by the public GitHub Actions run (or Codemagic). | A link to that run, then a log excerpt copied into the repo's lesson assets. |
| From the docs, not run by me | Anything on a real iPhone, signing, TestFlight, the App Store, or any step nobody ran. | The official page it comes from. |

Where they sit:

- On its own line, directly after the paragraph, list or code block it covers. Wherever an iOS-side claim appears (usually Name it and Where this shows up in Android).
- One label per claim. A section with both Windows and iOS claims has two labels.
- The lesson must stand alone without the iOS evidence (`docs/08` §8.1 teaching rule 1).
- Never write "I ran this on an iPhone" from CI evidence. A CI build is not an iPhone run.

### 4.13.1 Acceptance

- [ ] Every iOS-side claim in a `Mac needed` leaf has one of the three labels, in the exact words above.
- [ ] "Seen in this CI run" always has a working link.

---

## 5. Challenge formats (D9)

Challenges are written as fenced `challenge` YAML blocks. The field list, the card behaviour, hiding of answers and scoring-free feedback are owned by `docs/06` §3 to §6. This doc fixes the written content and the counts. The block stays readable as plain text when the widget is missing or the learner is offline (`docs/01` §9).

### 5.1 Predict the output (1 to 3 items, each its own `challenge` block, `type: predict`)

| Part | Block field | Rule |
|---|---|---|
| Code | `code` | A complete `kotlin` program with `fun main()`, 3 to 12 lines. On a `playground: false` leaf (`run: false`) it is a table or trace to follow on paper. |
| Options | `options` | Exactly three (`a`, `b`, `c`, shown as A, B, C). Two wrong options must be believable, based on a real mistake. |
| Answer | `correct: true` on one option | Exactly one. |
| Why | `why` of the correct option | One or two short sentences, in the language of the analogy. |
| If you picked another one | `why` of each wrong option | One sentence each. It treats the guess as a clue ("You may have expected…"). |
| Real output | `output` | The exact stdout, copied from a run (`docs/06` §5). |

After the guess the learner presses "Run it to check" (D9) and sees the real output. The page copy never uses red crosses, "Wrong" or shaming (`docs/01` §3.4).

### 5.2 Fix the bug (exactly 1 per leaf, `type: fix`, only when `playground: true`)

| Part | Block field | Rule |
|---|---|---|
| Code | `code` | A complete `kotlin` program, 10 to 30 lines, in the style of generated code where the roadmap marks it (§3.6). One wrong assumption. It compiles, or its only compile error is the one being taught. |
| Expected output | `expected` | The exact text a correct program prints. The editor compares the learner's output with it (D9). |
| Hint 1 | `hints[0]` | Points at a line, not at the fix. |
| Hint 2 | `hints[1]` | Names the idea. Exactly two hints. |
| Fixed version | `fix` | A complete program. It uses only what this leaf (or an earlier required leaf) taught. |
| Explanation | `explanation` | Optional, one sentence. |
| Reviewer questions | (plain Markdown under the block) | The five questions of §3.6, identical in every leaf. |

### 5.3 Block and marker rules

- The `challenge` block is the only challenge format. Authors write no `<!-- CHALLENGE -->` comment and no hand-written Answer, Why or Hint labels. The `challenge` info string, YAML fields, `id` rules and storage key are defined once, in `docs/06` §3.
- Each `challenge` block has a unique kebab-case `id` on the page. It never changes after publishing.
- `verified` holds the Kotlin version used for the run (`kotlin-2.4.21`, the version pinned in `docs/06` §5). A leaf with `UNVERIFIED` anywhere is not `live`.
- The only HTML-comment marker left is `<!-- SIM: sim-id -->`. No other marker is invented in this doc. Write it on its own line with a blank line before and after, in the exact form `<!-- SIM: sim-id -->` (regex in `docs/07` §6.2 R1).
- **No raw HTML in lesson bodies except the SIM marker** (validation V12, `docs/07` §6.2). No `<div>`, `<br>`, `<img>`, `<details>`, inline tags or other comments. The build escapes raw HTML, so it would show as visible text. Use Markdown for everything. V12 checks `live` leaves only: a `<!-- ⚠️ VERIFY ... -->` comment may sit in a shadow leaf (as in the worked example, §10) and must be removed before the leaf goes live, which the zero-`⚠️ VERIFY` item of the checklist already requires.
- **Supported Markdown** (ADR 005): CommonMark plus GFM tables. That means headings, paragraphs, emphasis, lists, links, inline code, block quotes, thematic breaks, plain `kotlin` fences, the `challenge` fence (`docs/06` §3) and pipe tables. Not supported: task lists, strikethrough, footnotes, autolink extensions, raw HTML. Prose goes through commonmark-java; a `kotlin` fence becomes a code block and a `challenge` fence becomes a card.
- `challenge` blocks and the SIM marker are split out of the body by `renderLessons` into the leaf's lesson JSON (`/lessons/<id>.json`) and checked by `validateContent` (`docs/07` §6, ADR 005).

### 5.4 Code block rules

- Every `kotlin` block is a complete program with `fun main()`, or its first line is `// does not compile`.
- A block that does not compile is shown, never run. It is used to show what the compiler stops.
- Do not copy compiler or Playground error text into a lesson unless it was produced by running the code and is pasted exactly. Say what happens in words ("the compiler refuses to run it") instead.
- `reviewer` compiles every `kotlin` block (runnable or not) with Kotlin 2.4.21 (`docs/06` §5), in the Kotlin Playground or an IDE, before PASS. Until a block has been run, the leaf carries `⚠️ VERIFY: compile not run` near it, and its `challenge` blocks carry `verified: "UNVERIFIED: <reason>"`.

---

## 6. Length budget (a 40-minute evening slot)

The Career Switcher has 40 minutes (`docs/01` §2.2). A leaf fits in that slot, one idea per page (P8).

| Part | Budget |
|---|---|
| Prose in the body | 900 to 1,600 words |
| One sentence | Under about 25 words (§7) |
| Sims | 0 or 1 per leaf, never more than 2 |
| Predict items | 1 to 3 (each exactly 3 options) |
| Fix the bug tasks | Exactly 1 (exactly 2 hints) |
| `kotlin` code blocks | At most 8, counting each `challenge` `code` and `fix` |
| Reading time | 8 to 12 minutes |
| Doing time (sim, predict, try, fix) | 15 to 25 minutes |

A leaf that cannot fit these numbers is two leaves. Ask `curriculum-designer` to split it, never to cram it.

---

## 7. Tone checks (from `docs/01` §7)

Voice: a friendly older student at the next desk. Short sentences, active verbs, real objects, one idea per sentence (L19).

| # | Check | How `reviewer` tests it |
|---|---|---|
| T1 | Words a 14-year-old knows, except the technical term being named | Read each sentence. Any word with a plainer one is flagged. |
| T2 | A technical term is bold **once**, with its plain meaning in the same sentence | Search for `**`. Each term appears bold once on the page. Labels such as **Answer** or **Try this** are not terms. |
| T3 | "You" for the learner, "I" for the author's own experience | No "the student", no "one should". |
| T4 | Banned words: "simply", "just", "obviously", "as we all know", "easy", "trivial", "of course" | Search the body. Zero hits. ("Easy topic" in the layout chip is allowed; it is not body text.) |
| T5 | A mistake is a clue, not a failure | No "Wrong", "Incorrect", "Failed", red-cross language. Use "Not quite. Look at line 3: …" |
| T6 | No hype, no slogans, no emoji | Search for emoji and for exclamation marks in prose (the `!!` and `!=` operators inside code are fine). Zero hits. |
| T7 | A sentence over about 25 words with two ideas is split | Count. Flag. |
| T8 | Never imply "this is easy" or "you should know this" | Learner confidence is low (`docs/01` §2.4). Allowed: "This part looks small. It trips a lot of people." |

The eight good and bad pairs in `docs/01` §7 are the calibration set. Any sentence that matches a "Bad" pattern there is rejected.

Extra writing rules for lessons:

- Define each term the first time it appears on the page, even if an earlier leaf defined it. The learner may land on this page from a link.
- Use the same word for the same thing all through the page. Do not rotate "null", "empty", "nothing" and "missing" unless the lesson is about their difference.
- Prefer a number over "many" and an object over "entity".
- Code comments are in plain English too.

---

## 8. Reviewer checklist

`reviewer` runs this list on every leaf file before PASS. One failed item is a FAIL with the item number. `docs/09` owns the pipeline; this list owns the content.

### 8.1 Gate and frontmatter

- [ ] The author said "I understood X" for this topic (L5, CLAUDE.md rule 1). Recorded in the task, not in the file.
- [ ] Frontmatter has exactly the keys of `docs/00` §8.2, in order, with valid values (§2).
- [ ] `id` equals the file path (V4). `title`, `difficulty`, `requires`, `fruit` equal the roadmap row (V5).
- [ ] `status` is still `shadow` while the review runs. `housekeeper` sets `live` after PASS.
- [ ] `upnext` is absent or `false` on a leaf about to go live (V9).
- [ ] `video` is `""` or a valid YouTube URL (`docs/07` §6.4).
- [ ] `sims` equals the SIM marker IDs. `playground` is true exactly when a runnable block exists (§2, `docs/06` §1).

### 8.2 Structure

- [ ] The body has the headings of §4 in order, with exact names. Left-out sections follow the rule in §4.
- [ ] No empty heading.
- [ ] **No Capstone-link section and no capstone link anywhere in the body (L26).**
- [ ] **No Video heading, no video link and no "watch the video" sentence in the body.** When `video` is empty, nothing on the rendered page mentions a video, a button or "coming soon" for one (L26, P10).
- [ ] Build the logic has Notice, Guess, Build, Name it in that order (§3.3).
- [ ] Predict the output comes before Try it (P6).
- [ ] Predict the output has 1 to 3 `challenge` blocks (`type: predict`) and Fix the bug has exactly one (`type: fix`); each block follows `docs/06` §3 and has a unique `id`. No `<!-- CHALLENGE -->` comment exists. See it move has exactly one SIM marker, if present.
- [ ] No previous/next link, "I finished this" button or breadcrumb is written by hand.

### 8.3 Teaching quality

- [ ] First screenful has a `?`, no code, no bold term, no unmet term (§3.1).
- [ ] One analogy only; "Where the analogy stops" is present (§3.2).
- [ ] Logic is built before keywords (§3.3).
- [ ] The leaf teaches one idea (P8). Title, Recap and You can now describe the same idea.
- [ ] Every "You can now" line has a matching task on the page (§4.12).
- [ ] The Fix the bug code has the AI-code style (`generated: true`) where the roadmap marks the stem, and always the five reviewer questions (§3.6).
- [ ] Passes all three learners (`docs/01` §2): a Tutorial Survivor sees what every part does; a Career Switcher can finish in 40 minutes; a Theory-Scared Student sees where it meets a phone app (Where this shows up in Android).

### 8.4 Facts and code (P9)

- [ ] Every Kotlin, Android and KMP claim was checked against an official page (kotlinlang.org, developer.android.com). The reviewer lists the page for each claim in the review note.
- [ ] Every `kotlin` block is a complete program or begins `// does not compile`. Every runnable block was compiled and run by someone with Kotlin 2.4.21, and its output matches the text and the `output` / `expected` fields (§5.4). Every `verified` field holds a version (or `not-run` for a hand-traced `run: false` item), never `UNVERIFIED`.
- [ ] No compiler or Playground message is quoted unless it was produced by running the code (§5.4).
- [ ] **Zero `⚠️ VERIFY` markers remain** in the file. A leaf with any marker stays `shadow`.
- [ ] No API name was invented. Each function or class named in code exists in the Kotlin standard library or the stated library.
- [ ] On a `Mac needed` leaf (§4.13): every iOS-side claim has one of the three evidence labels, and no text claims an iPhone run from CI evidence alone.

### 8.5 Tone and safety

- [ ] Checks T1 to T8 pass (§7).
- [ ] No emoji, no hype, no banned word, no personal data, no secret (CLAUDE.md rule 7).
- [ ] No mention of an Android app to run or an emulator (none exists).
- [ ] P2: no placeholder text such as "coming soon" or "to be added" anywhere in the body. P7: plain English. P8: one idea. P9: facts checked.

---

## 9. Acceptance criteria for this system

- [ ] `content/_templates/leaf.md` has the frontmatter of `docs/00` §8.2 and exactly the twelve `## ` headings of §4, in the same order and with the same names. (Check with: list lines starting `## ` in the template and compare to the table in §4.)
- [ ] The worked example in §10 has the same twelve headings (the optional three are present because the example has a sim and runnable code).
- [ ] The template explains in a comment that `video: ""` means no video button and no video section, and that the author never writes a Video heading.
- [ ] The template has no Capstone section and no previous/next section.
- [ ] The template and the worked example use `challenge` blocks (`docs/06` §3) and contain no `<!-- CHALLENGE -->` comment.
- [ ] A leaf that follows the template and sets `video` to a YouTube URL needs no other edit for the button and video section to appear.

### 9.1 Rejected alternatives

| Alternative | Why rejected |
|---|---|
| Free-form lessons, no fixed headings | Reviewer cannot check them; agents drift; the layout cannot link to sections. |
| Learner levels inside a lesson ("beginner / advanced boxes") | L1: no levels. Difficulty belongs to the topic. |
| A quiz at the end that stores scores | `docs/01` §8: self-checks only; checkmarks live in the browser (D10). |
| One long "Examples" section | It repeats the mental model and invites a second analogy (§3.2). |
| Capstone-link section on every lesson | Removed by L26 and D7. |
| Hand-written Previous / Next links | Go stale when the tree grows; the layout reads the tree data instead. |
| A plain-Markdown `<!-- CHALLENGE -->` slot with hand-written Answer, Why and Hint lines | Two formats existed (this one and the `challenge` block in `docs/06`). The block has machine-readable fields (`output`, `expected`, per-option `why`) the cards need, so it is the only format (author decision, 18 Oct 2026). |
| Predict the output after Try it | The learner would see the output first (P6). |

---

## 10. Worked example: `kotlin/null-safety/what-is-null`

This is a **filled example for teaching the format**. It is not a published leaf and must not be copied into `content/` without the author's "I understood X" (L5). Its facts were checked on 18 Oct 2026 against four official pages: `https://kotlinlang.org/docs/null-safety.html`, `https://kotlinlang.org/docs/java-interop.html`, `https://kotlinlang.org/docs/strings.html` and the Kotlin language specification, `https://kotlinlang.org/spec/expressions.html` (string interpolation). The code was **not compiled or run** by anyone (no shell in either review), so it carries one `⚠️ VERIFY`, and its `challenge` blocks say `verified: "UNVERIFIED: ..."`. The expected output of each snippet is listed in §10.2 with where it comes from (documented, or derived by reading, never run). The title and difficulty below match the `kotlin` roadmap row (checked 18 Oct 2026); `order` matches the roadmap. `requires` lists only `basic-types`: since 20 Oct 2026 (O5) the example code declares no function of its own and has no `if`, so it does not need `declaring-functions` or `if-as-an-expression`. The code still sits inside the usual `fun main()` wrapper, which the first-programs leaves already taught. The `val` lines sit above `main`, on purpose: Kotlin does not narrow a top-level property from its initialiser, so `!!` and `!= null` stay meaningful and draw no "always null" or "unnecessary" warning (derived, not run, so ⚠️ VERIFY). `requires` holds same-branch leaves only (V3), so the Foundation leaf `foundation/programming-thinking/what-is-a-function` is an `Assumes` line in the Kotlin roadmap, not a `requires` entry. The analogy is the lunch box (decided 20 Oct 2026, O5), as in `docs/05` §10 and the `docs/08` row example.

Frontmatter uses `status: shadow` and empty dates on purpose: nothing goes live without `reviewer` PASS and `housekeeper` (§8.1). The example has one sim marker. The sim ID `null-lunch-box` is specified in `docs/05` §10 (worked spec).

The example teaches one idea (P8): `null` means "nothing here", and a program can crash if it treats nothing as something. It leaves `?.`, `?:`, `let`, `if` checks and smart casts to later leaves.

````markdown
---
id: kotlin/null-safety/what-is-null
title: What is null and why does it crash apps?
status: shadow
difficulty: easy
order: 17
requires: [kotlin/types-and-variables/basic-types]
summary: Null means nothing is here. See how Kotlin catches it before your app runs, and how a crash still gets through.
video: ""
publishedOn:
updatedOn:
sims: [null-lunch-box]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: compile not run for any kotlin block in this leaf (Kotlin 2.4.21, docs/06 section 5). -->
## The question

A user fills in a sign-up form and leaves the middle name blank. The app closes on the spot. Nothing in the form looked wrong.

Your code asked for the number of letters in the middle name. There was no middle name to count. What should a program do when you ask about something that is not there?

## Mental model

Think of a lunch box with a label on the lid.

- The lunch box is a place in your program that holds a value.
- The sandwich is the value.
- An empty box means nothing is here.
- A note with no words on it is not an empty box. It stands for a name with zero letters.
- The label says what kind of box it is. One label reads "always has a sandwich". The other reads "might be empty".
- Asking "how many slices?" means using the value.

If the label says "always has a sandwich", you can ask about the slices straight away. If the label says "might be empty", you must open the box and look first.

**Where the analogy stops:** In real life you open an empty box and see air. In code, "nothing here" is a special value, and Kotlin reads the labels before the program starts. So many mistakes are caught before anyone takes a bite. Also, a box holding a note with no words is not an empty box. That difference matters later.

## See it move

<!-- SIM: null-lunch-box -->

**Try this:**
1. Drag a sandwich into the box labelled "might be empty". Drag it out again.
2. Press "Count the slices" while that box is empty.
3. Try to leave the box labelled "always has a sandwich" empty.
4. (Optional reveal) Press Reset. Tick "Look inside first", then press "Count the slices" on the empty "might be empty" box.

**What you should notice:** The "might be empty" box lets you leave without a sandwich, the "always has a sandwich" box refuses to stay empty, and counting slices in an empty box stops the sim unless you looked first.

## Build the logic

### Notice

The form has three boxes: first name, middle name and last name. Ada fills in all three. Sam leaves the middle one empty. The app wants to print how many letters each name has. For Ada it can count. For Sam's middle name there is nothing to count.

### Guess

What should the app do when it reaches Sam's empty box? Here are three ideas.

- A. Pretend the box holds a name with zero letters.
- B. Mark the box as "nothing here" and look for that mark before counting.
- C. Count anyway and see what happens.

Idea A hides the truth. Sam has no middle name, which is not the same as a middle name made of nothing. Idea C is the crash from the opening question. Idea B is what Kotlin does.

### Build

1. Give every box a label: "always has a value" or "might have nothing".
2. Never hand nothing to a box labelled "always". Refuse it before the program starts.
3. Before asking about what is inside a box labelled "might have nothing", find out whether something is there.
4. If you skip that and ask anyway, the program stops with an error.

### Name it

The mark for "nothing here" is **`null`**. A box that must always hold a value has a **non-null type**, written `String`. A box that might hold nothing has a **nullable type**, written with a question mark: `String?`. The question from step 3, "is anything here?", is written `middleName != null`. It answers `true` or `false`. A later leaf shows what to do with that answer.

```kotlin
val firstName: String = "Ada"
val middleName: String? = null

fun main() {
    println(firstName)
    println(middleName)
    println(middleName != null)
}
```

This prints `Ada`, then `null`, then `false`. Printing an empty box is fine. The question `middleName != null` answers `false` because nothing is here.

Kotlin refuses to run the next two programs. They break rules 2 and 3.

```kotlin
// does not compile
val firstName: String = null

fun main() {
    println(firstName)
}
```

```kotlin
// does not compile
val middleName: String? = null

fun main() {
    println(middleName.length)
}
```

Kotlin has one more tool. You can tell it "trust me, this box is not empty" by writing `!!` after the name. This is the **not-null assertion**. If the box is empty after all, the program stops with a **NullPointerException**, called an **NPE** for short. That is the crash from step 4.

## Predict the output

```challenge
type: predict
id: print-null-box
verified: "UNVERIFIED: compile not run"
code: |
  val middleName: String? = null

  fun main() {
      println("Middle name: $middleName")
      println("Done")
  }
options:
  - id: a
    text: "Middle name: followed by nothing, then Done"
    why: "You may have expected a blank. An empty box prints as the word null, not as nothing."
  - id: b
    text: "Middle name: null, then Done"
    correct: true
    why: "The box is allowed to be empty. Printing an empty box shows the word null. Nobody asked about the contents, so nothing crashes."
  - id: c
    text: "The program stops before it prints anything"
    why: "You may have expected a crash. Null only hurts when you ask about what is inside."
output: |
  Middle name: null
  Done
```

```challenge
type: predict
id: bang-on-full-box
verified: "UNVERIFIED: compile not run"
code: |
  val middleName: String? = "Rose"

  fun main() {
      println("Start")
      println(middleName!!.length)
      println("End")
  }
options:
  - id: a
    text: "Start, 4, End"
    correct: true
    why: "The !! told Kotlin to trust you that the box has a sandwich. This box held Rose, so the promise was true and the slices could be counted."
  - id: b
    text: "Start, then the program stops with a NullPointerException"
    why: "You may think !! always crashes. It stops the program only when the box is empty, and this box had a sandwich."
  - id: c
    text: "Start, null, End"
    why: "The word null shows up only when you print the box itself. Here you printed its length, which is a number."
output: |
  Start
  4
  End
```

## Try it

1. **Write.** Copy the program from the second Predict item into the editor.
2. **Run.** You should see `Start`, then `4`, then `End`. The word "Rose" has four letters.
3. **Break.** Change `"Rose"` to `null`. Run it again. The program prints `Start` and then stops with a NullPointerException.
4. **Fix.** Replace the line `println(middleName!!.length)` with `println(middleName)`. Run it. You should see `Start`, `null` and `End`, and no crash. You stopped asking about what is inside an empty box. Later leaves in this branch show safer ways to ask.

## Fix the bug

```challenge
type: fix
id: fix-bang-in-describe
generated: true
verified: "UNVERIFIED: compile not run"
code: |
  val firstName: String = "Ada"
  val middleName: String? = null

  fun main() {
      println(firstName)
      println("Middle name has ${middleName!!.length} letters")
  }
hints:
  - "Look at the line that uses !!. What is in the box when that line runs?"
  - "You do not need the letter count to show the box. Print the box itself."
expected: |
  Ada
  Middle name: null
fix: |
  val firstName: String = "Ada"
  val middleName: String? = null

  fun main() {
      println(firstName)
      println("Middle name: $middleName")
  }
explanation: "The !! promised that the box has a sandwich. The box was empty, so the promise broke. Printing the box itself never asks about what is inside, so it cannot crash."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

## Tricks to remember

- Read the label before the lunch. Look at the type before you look at the value.
- A question mark after the type means "might be empty".
- `!!` is a promise. If the box is empty, the app pays for it.
- A note with no words (`""`) is a sandwich. `null` is no sandwich.

## Common mistakes

- **Treating null and empty text as the same.** In everyday talk both mean "no name". In code, `""` is a text with zero letters and `null` is no text. Choose `String?` when "nothing here" is a real answer.
- **Adding `!!` to make the red line go away.** It feels like the compiler is being fussy. The red line was the compiler's warning. Check for `null` first.
- **Thinking Kotlin makes crashes from null impossible.** The compiler is strict, so this feels true. The Kotlin documentation lists the `!!` operator and calls into Java code among the causes of an NPE.

## Where this shows up in Android

Picture a profile screen that shows the user's middle name. A new user has none. If the code uses `!!` on that value, the screen crashes the moment it opens. Everything that looked fine in testing used users who had a middle name.

The same crash can come from Java code. Kotlin does not report nullability errors at compile time for Java values that do not say whether they can be null. The call can fail while the app runs. Kotlin code in an Android app can call Java code, so look at where each value comes from.

## Recap

1. `null` means nothing is here.
2. A `String?` box may hold it. A `String` box never does.
3. Using `!!` on an empty box crashes the app with an NPE.

## You can now

- Explain why a `String` box cannot hold `null` but a `String?` box can.
- Predict whether a short program prints `null` or stops with a NullPointerException.
- Fix a line that uses `!!` on an empty box so the program no longer crashes.
````

### 10.1 What the example shows

| Rule | Where it appears in the example |
|---|---|
| Question mark in the first screenful, no code, no bold term | `## The question` |
| One analogy, with its limit stated | Lunch box in `## Mental model`; reused in Build, Predict and Common mistakes |
| Notice, Guess, Build, Name it | `## Build the logic`; Kotlin syntax first appears under Name it |
| SIM slot with plain-text fallback | `## See it move` |
| Predict before run, answers in the page (`challenge` blocks, exactly 3 options) | `## Predict the output` |
| Write, run, break, fix | `## Try it` |
| One `fix` block with exactly two hints; five reviewer questions in plain Markdown | `## Fix the bug` |
| Verified facts | `String?` versus `String`, `!!` causing an NPE, the list of NPE causes, and Java values without nullability information (`https://kotlinlang.org/docs/null-safety.html`, `https://kotlinlang.org/docs/java-interop.html`, read 18 Oct 2026) |
| Not verified | That every block compiles and prints what the text and the `output` / `expected` fields say (⚠️ VERIFY in the example's comment; the `challenge` blocks say `UNVERIFIED`). §10.2 lists, per snippet, what the official pages support. The run on Kotlin 2.4.21 is still needed. |
| No video section | `video: ""`, so the layout draws no button and no video section |
| No capstone section, no previous/next | Not in the body at all |

### 10.2 Expected output of each snippet, and how far it is supported (18 Oct 2026)

**No snippet below was compiled or run.** "Documented" means an official page states the output, or the error, for the same kind of code. "Derived" means it follows from reading the code and the rules, with no official page showing that exact case. Neither counts as a run (`docs/06` §5): every `verified` field stays `UNVERIFIED` until someone runs it on Kotlin 2.4.21. No compiler message and no stack-trace text is quoted in the lesson, on purpose.

| Snippet | Expected result | Support |
|---|---|---|
| Name it program (top-level `val`s, three `println`) | Prints `Ada`, then `null`, then `false` | Documented for `null` printing: null-safety page `print(b) // null`. The `Ada` line and `middleName != null` giving `false`: derived. No "always false" warning is expected because the top-level property is not narrowed by its initialiser: derived, ⚠️ VERIFY on Kotlin 2.4.21. |
| `val firstName: String = null` | Does not compile | Derived. The null-safety page shows `a = null` on a non-null `String` with the comment "Null can not be a value of a non-null type String", but not an initialiser. |
| `println(middleName.length)` on a `String?` | Does not compile | Documented: null-safety page says the compiler reports an error for `b.length` on a nullable; java-interop page shows `inventory.nullable().length // Error: only safe (?.) or non-null asserted (!!) calls are allowed`. |
| Predict 1 `print-null-box`: `"Middle name: $middleName"` | `Middle name: null`, then `Done` | Documented: strings page, `val text: String? = null; println("Hello, $text") // Hello, null`; the spec (string interpolation) says a null value becomes the text `"null"`. |
| Predict 2 `bang-on-full-box`: top-level `val middleName: String? = "Rose"` with `!!` | `Start`, `4`, `End` | Documented for `!!` on a non-null value returning it (`val l = b!!.length; println(l) // 6`). The length 4 of "Rose": derived. |
| Try it step 3 (`"Rose"` changed to `null`) | `Start`, then the program stops with a `NullPointerException` | Documented: null-safety page, `println(l) // Exception in thread "main" java.lang.NullPointerException`. How the Playground shows it: unknown, so the lesson says it in words only. |
| Try it step 4 (`println(middleName)`) | `Start`, `null`, `End` | Documented for printing a null `String?` as `null` (strings page, same as Predict 1). |
| Fix the bug, broken version | Prints `Ada`, then stops with an NPE | Derived from the `!!` rule above. |
| Fix the bug, fixed version (`expected`) | `Ada`, then `Middle name: null` | Documented for the `null` text in a template (strings page). The `Ada` line: derived. |

---

## 11. Conflicts and open items

| # | Item | Default until answered |
|---|---|---|
| C1 | `docs/01` §5.3 table says a page cannot be a live leaf without a video, and §5.2 step 2 puts the video before going live. Both are older than L26 and Rule V1 in the same doc. | Follow L26 and Rule V1: a leaf can be live with `video: ""`. `docs/01` §5.2 and §5.3 should be corrected by their owner. | RESOLVED: already corrected in `docs/01` (A2e). |
| C2 | The extra banned words in T4 ("easy", "trivial", "of course") and the no-exclamation check in T6 go beyond the list in `docs/01` §7. | Kept as stricter checks for lessons. Remove them if the author disagrees. |
| O1 | ~~Sim IDs belong to `docs/05`.~~ **Resolved 18 Oct 2026:** `null-lunch-box` is specified in `docs/05` §10. | Marker form `<!-- SIM: id -->` stays. |
| O2 | ~~How Predict and Fix widgets work.~~ **Resolved 18 Oct 2026:** the `challenge` block of `docs/06` §3 is the single format. `renderLessons` (ADR 005; formerly `syncLessons`) and `validateContent` handling is owned by `docs/07` §6. | `docs/06` §3. |
| O3 | ~~Kobweb and HTML comments (§5.3).~~ **Obsolete 20 Oct 2026 (ADR 005):** lessons no longer go through the Kobweb Markdown plugin. `renderLessons` cuts the SIM marker out of the source, and V12 forbids any other raw HTML in a live leaf. The old export-and-grep test (P6-5) is dropped. | closed |
| O4 | ~~Marker in `_roadmap.md` for stems with AI-style tasks.~~ **Resolved 18 Oct 2026:** `ai-review` in the stem `Flags:` line and `F(ai):` in the Challenges column (`docs/08` §2.2, §5). | closed |
| O5 | **Resolved 20 Oct 2026 (author, via orchestrator):** (1) the analogy is the lunch box (`docs/05` §10, §3.2 list); the Mental model keeps "Where the analogy stops". (2) The Predict seeds, Try it and Fix block need no `?.`, `if` or declared function; `?.` waits for `safe-call-and-elvis`. (3) The code is simplified: top-level `val`s plus `fun main()`, no `fun` of its own, no `if`; `requires` is `[basic-types]` only. Code still not compiled (⚠️ VERIFY). **Still open, outside this doc:** `content/kotlin/_roadmap.md` (not edited here) still has, checked 20 Oct 2026, the bus analogy, a `name?.length` Predict seed and a ball-and-shelf sim in its table row, and `requires` with `if-as-an-expression` and `declaring-functions` in its YAML and Notes. V5 will fail until `curriculum-designer` aligns that row with this example. | `curriculum-designer` updates the roadmap row; `reviewer` before the leaf is written for real. |
| O7 | ~~Tree data source.~~ **Resolved 18 Oct 2026:** the app reads generated `TreeData.kt`; the same generator also writes `/tree.json` for tools (`docs/03` §4, `docs/07` §6.4). | closed |
| O6 | `docs/05` §11.3 Q1: author decided to KEEP "Look inside first". The worked example now has it as optional Try-this item 4; `showLookInsideFirst` stays `true`. | Done in §10 and §3.4. |
