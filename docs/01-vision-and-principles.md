# 01 — Vision and Principles

> **Purpose:** says why Kotree exists, who it is for, how it teaches, which rules every feature must obey, and how we know it works. Every other doc is judged against this one (after `00`).
> **Read this when:** you start any new feature, write any lesson text, argue about scope, or need the author's on-camera pitch.
> **Depends on:** `docs/00-decisions.md` (wins every conflict). Locked decisions are cited as L1–L24, defaults as D1–D14. They are not repeated here.
> **Version:** v1 · written 17 Oct 2026 · verified 18 Oct 2026.
> **Changes from verification:** none. Vercel Hobby limits fit L27 (free tiers, non-commercial personal site). Two items stay `⚠️ VERIFY` with Phase 0 tests (§9): Playground offline behaviour and performance targets.

---

## 1. Purpose and the promise

### 1.1 Purpose

Kotree is one public website, shaped like one pixel-art tree, that teaches a person who has never built an app how to build, test, speed up and reason about Android and Kotlin Multiplatform apps. It also teaches the computer-science theory behind that work. The author learns each topic in public, on YouTube, and the tree grows only when he has really understood a topic (L5).

Two things make it different from a course site:

1. **The map is the product.** A learner sees the whole subject on day one (L4, L6). Nothing is hidden behind a menu, a level, or a login (L1, L2).
2. **The teacher is a learner one step ahead.** The author is not an expert talking down. He is a final-year student who has just understood the thing and remembers exactly what confused him.

### 1.2 The promise to the learner

Five sentences. Every doc, page and video must keep all five true.

| # | Promise | How we keep it |
|---|---|---|
| 1 | **You can start with zero.** | The Foundation branch assumes nothing (see `00` §5). "Start here" is always visible (L6). |
| 2 | **You will never be asked to sign up, pay, or pick a level.** | L1, L2. No gating, no newsletter, no comments. |
| 3 | **Every page explains why before how.** | Question-first lessons (see §3.1). |
| 4 | **Everything we show you was understood by a human first.** | The leaf gate (L5). A shadow branch is an honest "not yet", never a fake page. |
| 5 | **If you finish the whole tree, you can work as a one-person Android + KMP team and read AI-written Kotlin without fear.** | L24. Success measures in §6. |

### 1.3 What the learner gets that they cannot get elsewhere

- A single picture of the entire field, so they know how much is left.
- A learning order they did not have to invent (recommended order in each `_roadmap.md`, `00` §8.1).
- Honest uncertainty: when the author is still learning something, the leaf is not there yet.

Acceptance criteria for the promise:

- [ ] A first-time visitor can say what the site is within 10 seconds of the first screen, without scrolling or clicking.
- [ ] No page anywhere asks for an email, a name, a password or a payment.
- [ ] No leaf is live without the author's "I understood X" (L5).

---

## 2. Three learners (all absolute freshers)

These are profiles, not people. When a lesson is in doubt, test it against all three. If it fails any one, it is not done.

### 2.1 The Tutorial Survivor

- **Situation:** Has copied code from videos for a year. Apps "work" until one line changes. Cannot say what any line does.
- **Knows:** Can type, can follow steps. Has seen the words "variable" and "function" but could not explain them to a friend.
- **Goal:** Stop copying. Be able to change an app on purpose.
- **Fears:** Being found out in an interview. "Everyone else gets this and I don't." Wasting another six months.
- **What they need from Kotree:** Why-first lessons, a place where breaking code is allowed, small wins they can check themselves.
- **Fails the test if:** A lesson says "just paste this" without saying what each part does.

### 2.2 The Career Switcher

- **Situation:** Works in a non-software job. Studies at night, in 40-minute slots, on a laptop. Has never opened a code editor on purpose.
- **Knows:** How to use apps, not how they are made. Strong at real-world reasoning (money, logistics, people).
- **Goal:** A first paid Android job or freelance gig within a year.
- **Fears:** That the field changes faster than they can learn. That AI will do the job before they qualify. That they are too old or too late.
- **What they need from Kotree:** Real-world examples (a lift, a queue, a bank passbook) before jargon; a visible finish line; proof that reading and checking AI code is a real skill.
- **Fails the test if:** A page needs 90 minutes of unbroken focus, or assumes a computer-science degree.

### 2.3 The Theory-Scared Student

- **Situation:** In a CS or similar degree. Passed exams by memorising. Has seen DSA, OS and networking as lists of definitions. Cannot connect them to a phone app.
- **Knows:** Vocabulary. Not intuition.
- **Goal:** Understand why an app freezes, why a list is slow, why a request fails, and link each to the theory that explains it.
- **Fears:** Campus placements that do not hire for Android. Interview questions on theory they cannot visualise. Being "only a UI person".
- **What they need from Kotree:** Theory shown next to the Android symptom it explains; animations they can drag and break; a path that mixes both.
- **Fails the test if:** A theory leaf never says where it shows up in a real app.

### 2.4 What all three share

| Shared trait | Design consequence |
|---|---|
| Little patience for jargon walls | Plain English only (L19). Define a term the first time it appears on every page. |
| Low confidence | Never imply "this is easy" or "you should know this". |
| Alone, no teacher nearby | Every question a teacher would ask in a room must be on the page. |
| Desktop at a desk, headphones on or off | Desktop-first (L8). Video and page must each work alone. |
| Afraid of being replaced by AI | The tree teaches judgement: reading, testing and fixing code (see §3.5). |

---

## 3. Learning philosophy

Five rules for how a lesson teaches. Lesson structure itself is owned by `docs/04` (D7). This section says why.

### 3.1 Question-first

Every leaf starts with a question the learner already has or can feel, not a definition.

| Weak opening | Question-first opening |
|---|---|
| "A coroutine is a lightweight thread." | "You tap Refresh, the screen freezes for two seconds, and your thumb hovers. What was the phone doing?" |
| "Null means no value." | "Why did this app crash the moment a user left their middle name blank?" |

The question is also the leaf's title where possible (see the roadmap example in `00` §8.1: "What is null and why does it crash apps?"). Acceptance: the first screenful of every lesson contains a question mark and no term the learner has not met.

### 3.2 Logic building

We teach how to think, then the syntax. For each idea, the lesson walks the same four steps:

1. **Notice** a real situation.
2. **Guess** what the computer would need to do.
3. **Build** that logic in words or boxes.
4. **Name it**: only now give the official term and the Kotlin keyword.

Logic is built before keywords so that a learner who forgets a keyword can rebuild it.

### 3.3 See it move

An idea that can be shown moving is shown moving, driven by a real physics engine (L14, L21), with the choice of physics per topic guided by `docs/05`. The learner can drag, pause and break the simulation.

Rule of thumb: if the lesson says "imagine", try to replace it with something the learner can push.

Acceptance: any leaf about memory, time, order, flow or state change has at least one sim, or its roadmap row says why not.

### 3.4 Write, run, break, fix

The editor (L15) is not a decoration. Every leaf that has code follows this loop:

| Step | What the learner does | Where |
|---|---|---|
| Write | Types or edits a small piece of code | Editor |
| Run | Sees the output, or (for Android ideas) the code-to-screen sim result | Editor (D8) |
| Break | Is told to change one thing to make it fail | "Break it" prompt |
| Fix | Repairs it and compares with the expected result | Fix-the-bug (D9) |

Predict-the-output comes before Run, so the learner commits to a guess first (D9). A wrong guess is the most useful moment on the page and the page must treat it that way: no red crosses, no shaming copy.

### 3.5 Reading AI-generated code

L24 and the author's own aim say: a finished learner can read and understand AI-written Kotlin. So reading is taught as a skill, not a by-product.

- Many Fix-the-bug and Predict-the-output tasks use code that "looks like" what an AI assistant would output: tidy, confident, and slightly wrong.
- Lessons teach a reading order: types first, then what runs when, then what can be null, then what can run twice.
- Lessons show typical AI-code smells (unneeded `!!`, a coroutine launched in the wrong scope, state held in the wrong place) and ask "would you accept this in review?".
- We never claim AI is bad. We say: the person who can check it is the person who stays in the job.

Acceptance: each branch has at least one stem whose Fix-the-bug tasks are written in the style of generated code (the curriculum designer marks them in `_roadmap.md`).

---

## 4. Ten product principles

Each principle has an ID (P1–P10), one rule, and a do and a don't. When two principles clash, the lower number wins.

| ID | Principle |
|---|---|
| P1 | Open by default |
| P2 | Honest empty space |
| P3 | One tree, one theme |
| P4 | The map explains itself |
| P5 | Why before how |
| P6 | Learner acts, then reads |
| P7 | Plain English |
| P8 | Small, finished leaves |
| P9 | Facts checked before live |
| P10 | The page works without the video, and the video without the page |

**P1 — Open by default.** Nothing sits behind a wall (L1, L2).
- *Do:* Let a visitor open the hardest leaf from the first minute, directly by URL.
- *Don't:* Add "unlock after finishing Foundation", a locked padlock icon, or a pop-up asking for an email.

**P2 — Honest empty space.** Shadow means "not yet understood by the author" (L4, L5).
- *Do:* Show a shadow stem in light gray with its name and one hint line: "Coming soon: how a coroutine pauses without blocking a thread."
- *Don't:* Publish a stub page with "Content coming" text, or fill a leaf with copied text to make the tree look fuller.

**P3 — One tree, one theme.** White page, pixel world, smooth UI (L7, L9, D6).
- *Do:* Use the section accent only inside panels, buttons and lesson pages after the camera enters a branch (D5).
- *Don't:* Add a dark-mode switch, a second illustration style, or a gradient hero banner.

**P4 — The map explains itself.** Zoom shows more detail and more space (L6); at Canopy zoom the labels shown are bough names.
- *Do:* At low zoom (Canopy) show only bough names; at higher zoom reveal branch names, then stem names, then leaf popups with a one-line summary.
- *Don't:* Put a tutorial overlay in front of the tree, or require a "how to use this site" page.

**P5 — Why before how.** A concept appears after the problem it solves (§3.1, L13).
- *Do:* Start the `lifecycle` stem with "why did my game restart when I turned the phone?" and only then name the callback.
- *Don't:* Open with a table of lifecycle method names.

**P6 — Learner acts, then reads.** Prediction before explanation where possible (D9).
- *Do:* Ask "what will this print?" with three options, then show the answer and the reason.
- *Don't:* Paste the output next to the code in the first view.

**P7 — Plain English.** Short words, short sentences (L19, see §7).
- *Do:* "The phone kills your app when memory runs low."
- *Don't:* "The OS may reclaim process resources under memory pressure."

**P8 — Small, finished leaves.** One idea, one page, usually one video (§1 of `00`).
- *Do:* Publish "What is null?" on its own, finished; the video follows when ready.
- *Don't:* Publish "Null safety, generics and extension functions" as one leaf to hit a weekly count.

**P9 — Facts checked before live.** A wrong fact on a public tree damages every learner who trusts it (CLAUDE.md hard rule 4).
- *Do:* Check API names and behaviour against official Android and Kotlin docs, then set `status: live`.
- *Don't:* Publish from memory because the topic "feels basic".

**P10 — Page and video stand alone.** Each carries the whole lesson; together they reinforce (D11).
- *Do:* Put the key diagram and the key code in the page; narrate the same logic in the video.
- *Don't:* Write "see the video for the explanation" on the page, or "see the page for the code" in the video.

### 4.1 Using the principles

- Every feature spec in `docs/03` onward lists the principle IDs it relies on.
- A pull request that breaks a principle must say so in its description and name the principle.
- `reviewer` checks P2, P7, P8, P9 on every leaf.

---

## 5. "A journey we unlock together"

The phrase means something specific: the learner and the author are on the same slope, and every new leaf is visible proof that the two of them moved.

### 5.1 The emotional arc

| Stage | What the visitor sees | What they should feel | Design rule |
|---|---|---|---|
| Day 1 (L4) | A full black tree, all gray, no green | "This is big, but it has a shape." | Show everything; hide nothing. No fake progress. |
| Weeks 1–4 | The Foundation and a few leaves light up | "It is real. It moves every week." | Each new leaf is the first thing a returning visitor sees, via the up-next glow (D3). |
| Months 2–3 | One branch is mostly green; others still gray | "I can pick my own route." | Branches stay independent. "Start here" remains available (L6). |
| Months 4–8 | Dark-green hard leaves appear (L7) | "I did the hard part, and it is marked as hard, so it is not just me." | Hard is a fact about the topic, not a judgement of the learner (§7 of `00`). |
| First fruit (D12) | A capstone appears at a branch tip | "Someone built a real thing with exactly this." | Learners get constraints only (L16); the author shows his own. |
| Full canopy | Dense green, small gray gaps | Calm and ready, not "done forever". | The tree may keep growing; "complete" never means "finished learning". |

### 5.2 How publishing a leaf feels

For the author, publishing is a ceremony, not a chore.

1. He says, out loud and in plain words: **"I understood X."** That sentence is the gate (L5).
2. The video is recorded and uploaded to YouTube whenever he chooses; it does not block publishing (see §5.3).
3. The leaf file gets `status: live` and today's `publishedOn`. The video URL is added to `video` later (`00` §8.2).
4. The tree rebuilds. The leaf turns light green (or dark for hard) and the next leaf in that branch begins to glow (D3).
5. The public counter goes up by one: "N of M leaves grown" (D11).

For the visitor, the moment is quiet. There is no confetti, no banner, no pop-up. A gray leaf is now green, and the counter is one higher. Anyone who looked last week can see the change without being told.

Acceptance criteria:

- [ ] Between "I understood X" and the leaf being live, the only human step is the reviewer pass (see `docs/09`).
- [ ] The tree shows the newly live leaf as green without any cache clear.
- [ ] The up-next glow moves to the next leaf in `_roadmap.md` order, in the same build.

### 5.3 Video and page together; video is optional (L5, L26)

**Rule V1 (video optional, added later; author decision 18 Oct 2026).** A leaf goes live when the author says "I understood X" and the reviewer passes. It does not wait for its video. The author uploads the video to YouTube and adds the URL to the leaf's `video` field whenever he chooses. While `video` is empty, the page and popup show no video button or section at all (no placeholder, no "coming soon"). Once the URL is added, the **Watch the video** button (D11) and the video section become visible. This replaces the earlier "video first" reading.

| Question | Answer |
|---|---|
| What comes first? | Understanding, then the page. The video follows when the author is ready. |
| Can a live page exist without a video? | Yes. Until `video` is set, the page and popup show no video button or section. A draft with `status: shadow` may also exist in the repo, invisible to visitors. |
| Which one is the full lesson? | Both (P10). The page adds what a video cannot: the editor, the sims, copyable code, and quick re-reading. |
| What does the video add? | The author's voice, his real mistakes, and the "I was stuck here" moments. |
| Do they share the same story? | Yes: same opening question, same worked example, same checkpoint. |

Cadence is 3 leaves per week (L5), usually one video per leaf (§1 of `00`), added when ready.

---

## 6. Success measures

Kotree has no accounts and no analytics beyond what Vercel provides (L2, §9 of `00`). So measures for the author come from the repo, and measures for the learner are **self-check abilities** the learner can test alone.

### 6.1 For the author

| Measure | Target | How it is counted | Why it matters |
|---|---|---|---|
| Leaves published per week | 3 (L5) | Count of leaf files whose `publishedOn` falls in the week | The promised cadence |
| Weeks on target | At least 9 of any 12 weeks have 3 leaves | Rolling 12-week count | Consistency beats bursts |
| Zero-leaf weeks in a row | Never more than 1 | Same count | Two empty weeks makes the tree look abandoned |
| Live leaves that have a video | Rising over time; no fixed target | Live leaves with a non-empty `video` | Rule V1 (video is added later) |
| Facts corrected after going live | Under 1 per 10 leaves | Commits with `fix:` that change facts in a live leaf | Measures P9 |
| Tree progress | "N of M leaves grown" (D11) | Count of `status: live` over all leaves in `_roadmap.md` files | The public score |

Acceptance: `housekeeper` can print the first three rows from the repo in one command, with no manual counting. Which tool does that is decided in `docs/09`.

A bad week is not a failure; a pattern is. If the weeks-on-target measure drops under 6 of 12, stop adding scope and apply the burnout mitigations in §9.

### 6.2 For the learner: checkpoint abilities

A learner who has finished a stem can do the things below **without looking at the page**. Each ability is a sentence the learner can say out loud. Leaves end with "You can now…" lines drawn from this list (structure owned by `docs/04`). Each ability needs a concrete test, listed beside it.

| Branch | Ability: "I can explain…" | Self-test |
|---|---|---|
| `foundation` | What happens between pressing Run and seeing the app on a phone | Draw the build → install → run chain from memory |
| `kotlin` | Why `String?` and `String` are different, and what the compiler stops | Predict whether three given snippets compile |
| `android-core` | What happens to my screen on **rotation**, on **process death**, when the app goes to **background**, and when the phone is **offline** | Given a bug report ("form data vanishes"), name the cause and the fix |
| `ui` | Why a Compose screen redraws, and what makes it redraw too often | Find the extra redraw in a short composable |
| `concurrency` | Why a long task must not run on the main thread, and what a coroutine does instead | Fix a Fix-the-bug leaf that freezes the screen |
| `architecture-data` | Where state lives, and who is allowed to change it | Move state from a screen to a ViewModel in a small example |
| `kmp` | Which code can be shared and which must stay per platform | Sort 10 pieces of code into "shared" and "platform" |
| `dsa` / `daa` | Why one approach is slower than another, in numbers | Count steps for 10, 100 and 1000 items |
| `os` / `networking` | Which OS or network event causes a given app symptom | Match 5 symptoms to causes |
| `testing` | How I would prove a bug is fixed | Write one test that fails first and then passes |
| `profiling` | How I find the slowest part instead of guessing | Name the tool and the first thing to look at for jank |
| `security` | What I must never store or log | Spot 3 leaks in a short snippet |

**Whole-tree check (L24).** The learner can take a one-line app idea and say: what screens it needs, where state lives, what happens on rotation, process death, background and offline, what is shared across Android and iOS, how it will be tested, and what could make it slow. If they can, they are a team in one person. If they can also read a 200-line AI-written file and list its three biggest risks, they have the second half of the promise (§1.2, promise 5).

These measures are **self-assessed by design**. We do not score learners, store results beyond browser checkmarks (D10), or compare people.

---

## 7. Tone-of-voice guide

Voice: a friendly older student at the next desk. Short sentences. Active verbs. Real objects. One idea per sentence. Plain English (L19).

Rules:

1. Words a 14-year-old knows, unless the lesson is naming a technical term.
2. Name a technical term once, in bold, with its plain meaning in the same sentence.
3. Say "you" to the learner and "I" for the author's own experience.
4. Never say "simply", "just", "obviously", "as we all know".
5. A mistake is a **clue**, not a failure.
6. No hype, no slogans, no emoji in lessons.

Eight good vs bad pairs:

| # | Bad | Good |
|---|---|---|
| 1 | "Leverage coroutines to facilitate asynchronous execution." | "Use a coroutine to do slow work without freezing the screen." |
| 2 | "This is trivial, so we'll skip it." | "This part looks small. It trips a lot of people, so let's go slowly." |
| 3 | "Obviously, null references are a billion-dollar mistake." | "Null means 'nothing here'. If your code forgets that, the app crashes." |
| 4 | "Wrong. Try again." | "Not quite. Look at line 3: what is `name` when the user leaves it empty?" |
| 5 | "Simply instantiate the ViewModel via the factory." | "Ask Android to create the ViewModel for you. Here is how, line by line." |
| 6 | "Under memory pressure, the OS reclaims background processes." | "When the phone runs low on memory, it closes apps you are not using." |
| 7 | "Unlock your potential with our revolutionary curriculum!" | "Start at the roots. Each green leaf is something I understood and can show you." |
| 8 | "As an AI-resistant developer you must stay ahead of the machines." | "AI can write code fast. Someone still has to check it. This page teaches you how." |

Acceptance: `reviewer` can reject any sentence that matches a "Bad" pattern above: jargon where a plain word exists, a banned word from rule 4, or a sentence over about 25 words that holds two ideas.

---

## 8. Anti-goals: what we will not build

| Anti-goal | Why not | Source |
|---|---|---|
| Accounts, sign-in, profiles | Contradicts open access | L2, §9 of `00` |
| Learner levels (beginner/advanced tracks) | Every learner is a fresher; difficulty is a property of a topic | L1, §7 of `00` |
| Comments, forums, chat, newsletters | Adds moderation load and a gate | L2 |
| Personalised paths and recommendations | Future work, not v1 | L20 |
| Dark mode or theme picker | One strong theme | L9 |
| Mobile-polished layout | Desktop-first for now | L8, §9 of `00` |
| Real Android emulator in the browser | Not realistic; sims stand in | D8, §9 of `00` |
| Leaderboards, streaks, XP, badges, confetti | Pressure and noise; we teach, not game | P1, P3 (our own rule) |
| Quizzes that score and store results | Self-checks only; progress is checkmarks in the browser | D10 |
| Ads, trackers, third-party analytics beyond Vercel's | Trust and speed | §9 of `00` |
| Certificates | Learners would be tempted to rush; capability is the proof | L24 |
| Multiple languages | Plain English first | §9 of `00` |
| AI-written lessons published without the author's understanding | Breaks the gate | L5 |

Rejected alternative, one line: **streaks and XP** were considered to keep people returning; rejected because they reward showing up, not understanding, and clash with P3.

---

## 9. Risks and mitigations

| Risk | Likely form | Mitigation | Early warning |
|---|---|---|---|
| **Burnout** | 3 videos a week plus a tree to maintain, alongside final-year study and a job search | Batch: learn on 3 days, record on 2, rest on 2. Keep 2 finished leaves in reserve as a buffer. Allow a "learning week" with zero leaves once per quarter, announced honestly on the tree page. Never publish a leaf to hit a number (P8). | Weeks-on-target under 6 of 12 (§6.1) |
| **Scope creep** | New sims, new branches, new features before the first branch is green | Phases in `docs/10` are the only plan. Any new feature must cite a principle and a locked decision, and replace something of equal size. Anti-goals (§8) are the first answer. | More than one feature in progress at once |
| **Wrong facts** | A lesson states a lifecycle rule or API name that is outdated or wrong | P9: official docs check; `reviewer` PASS before `live`; every live leaf shows `updatedOn`; corrections are made in place and the video description notes the fix. Slugs never change (CLAUDE.md rule 6). | Facts-corrected measure above 1 per 10 leaves (§6.1) |
| **Performance** | A canvas tree, sims and a code editor on one desktop page make it slow or hang | Draw the tree once and cache it (D1, D2); only interactive elements are live DOM; sims load only on their own lesson page; targets (to be tested in `docs/07`): first screen usable in under 3 seconds on a mid-range laptop, camera zoom stays smooth at the display's refresh rate. `⚠️ VERIFY` these are reachable with Kobweb and a pixel canvas before they become acceptance criteria. No official source can settle this: it is a measurement. Phase 0 test: build a throwaway Kobweb page with a cached 2000x2000 canvas, 200 absolutely placed DOM overlay nodes and wheel-zoom; record Lighthouse load time and a 10 s DevTools Performance trace on a mid-range laptop. Pass = first screen usable under 3 s and no frame over 16.7 ms; record the result in `docs/07`. | Frame drops when zooming |
| **Online-only code running** | The Kotlin Playground needs internet (D8), so offline learners see broken editors | Show a plain message ("Running code needs internet. You can still read and predict.") and keep Predict-the-output usable with answers in the page. `⚠️ VERIFY` how the Playground fails when offline: the official docs we could reach do not say. Phase 0 test (owned by `docs/06`): load a page with a Playground block, switch DevTools to Offline, press Run, and record what the user sees (error text, spinner, nothing); then pick the message trigger (a failed fetch vs `navigator.onLine`). | Support messages about "Run does nothing" |
| **Hosting limits** | Traffic spikes after a good video | Static export; no server code (see `docs/07`). Verified 18 Oct 2026 on https://vercel.com/docs/limits (updated 16 Sep 2026) and https://vercel.com/docs/limits/fair-use-guidelines (updated 14 Sep 2026): Hobby includes the first 100 GB Fast Data Transfer per month; 100 deployments per day and 100 builds per hour; 45 minutes build time per deployment; 100 MB static file upload limit; 200 projects. Hobby is restricted to non-commercial personal use, matching L27; "asking for donations does not fall under commercial usage", but ads, sponsor payments for hosting, affiliate-first pages or selling anything do. Budgets: exported site under 30 MB; a normal day under 10 deployments (one per publish commit). If Hobby is ever exceeded the site is not shut down by this doc: L27 says upgrade only if usage requires it. Vercel's guidelines say it will notify users whose usage is an outlier. | Vercel usage emails; any monetisation idea (check L27 first) |
| **Author drift** | Teaching things he has not understood | Rule L5 is not negotiable; shadow stays shadow. | A leaf published with no "I understood" note |
| **Fear of AI replacement turns into hype** | Marketing language creeps in | Tone guide (§7); the pitch (§10) says only what the tree can show. | Words like "revolutionary", "unlock" in copy |

---

## 10. Elevator pitch (say this on camera)

About 90 seconds spoken. Read it slowly. Change the nouns, not the structure.

> I am a final-year student, and I wanted to become a real Android developer. Every course I found had the same two problems. It either assumed I already knew things, or it made me sign up, pick a level, and watch forty videos before I wrote a line.
>
> So I am building the thing I wanted. It is one website, shaped like one tree. Right now, look: it is almost all gray. Those gray branches are everything I still have to learn, and everything you can learn with me. The roots are the basics: how a computer even runs your code. The branches are Kotlin, Compose, Kotlin Multiplatform, testing, speed, security, and the computer-science theory underneath: data structures, algorithms, operating systems, networking.
>
> Here is the rule. I only turn a leaf green when I have actually understood that topic, and I add a video for it too. Three leaves a week. No login. No levels. No ads. Nothing to pay.
>
> Each page starts with a question, not a definition. Then you can push things around: drag a block, watch memory fill, break the code on purpose, and fix it. You will also learn to read code that an AI wrote, and check whether it is any good. AI can write the code. Someone still has to understand it. That someone can be you.
>
> My goal is simple. When the whole tree is green, anyone who followed it can build, test, and ship an Android and iOS app on their own, like a team in one person.
>
> I do not know everything yet. That is why the tree is gray. Come and grow it with me. The first leaf goes live this week. Start at the roots.

Acceptance: the pitch contains the promise (§1.2), the leaf gate (L5), no sign-up (L2), the question-first idea (§3.1), and the one-person-team goal (L24), and is under 300 words.

---

## 11. Checklist for anyone using this doc

- [ ] The feature or text I am writing keeps all five promises (§1.2).
- [ ] I tested it against all three learners (§2).
- [ ] It names the principle IDs it relies on (§4).
- [ ] It does not appear in the anti-goals (§8).
- [ ] If it is lesson text, it passes the tone pairs (§7).
