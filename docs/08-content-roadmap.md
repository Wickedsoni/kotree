# 08 — Content Roadmap Rules

> **Purpose:** how every `content/<branch>/_roadmap.md` is written (17 files: Foundation + 16 branches), how big a leaf is, how difficulty, sims, AI-style Fix-the-bug stems, fruits and cross-branch prerequisites are marked, the recommended learning order across branches, the "Start here" path, honest leaf-count targets, the publishing cadence, and the review checklist.
> **Read this when:** you write, extend or review a `_roadmap.md`; you answer "what is next"; you need the size of the whole tree.
> **Depends on:** `docs/00-decisions.md` (wins every conflict; §5, §7, §8.1, §8.2), `docs/01-vision-and-principles.md` (§3.3 sims, §3.5 AI code, P8 leaf size, §6.2 abilities), `docs/03-information-architecture.md` (validation V2–V6, up-next, Start here).
> **Owner:** `curriculum-designer`. Only that agent edits `_roadmap.md` files (CLAUDE.md rule 5).
> **Version:** v1.1 · written 18 Oct 2026 · amended 18 Oct 2026 (author decision: trim the tree from about 820 to about 450 leaves, about 3 years at 3 leaves/week; all 16 branches and Foundation kept (17 roadmaps); nice-to-know topics moved to per-branch "Later" lists, §9.1) · amended 18 Oct 2026 (L15: no simulator, Android sims use `code-to-screen` per `docs/05` §4.3; §2.1 example uses the `null-lunch-box` analogy, resolving `docs/05` C1 and C2) · amended 18 Oct 2026 (A2e review and author decisions: real totals of 482 leaves and 20 fruits in §9, §10.1, §12 and §13; short branch names allowed in broad `Assumes` lines, §7.1; profiling sims use `animation`, §6; reading and doing time aligned with `docs/04` §6; Q3 partly closed, §15) · amended 18 Oct 2026 (new §8.1 "KMP without a Mac" and §8.2 "Other facts checked"; every open fact check that named a fact is now settled with a source and date, or kept with a concrete test).

No conflict with any default in `docs/00` §3. This doc adds **no new YAML fields**: everything new lives in the Markdown below the YAML block, so the generator (`docs/07`) and validator (`docs/03` §4.3) are unaffected.

---

## 1. The honest-tree rule (author decision)

Every `_roadmap.md` lists the **full** set of leaves for its branch from day one. The public counter "N of M leaves grown" (D11) uses M = every leaf in every roadmap YAML block, so M must be a real plan, not a placeholder.

| Rule | Detail |
|---|---|
| R1. Full list | A branch roadmap is not "written" until it lists every **must-know** leaf the branch needs to reach its `docs/01` §6.2 ability and L24. No "more coming later" stubs. Nice-to-know topics go to the branch's `## Later` list (§9.1): they are not leaves, not in the YAML, not in the tree and not counted in M. |
| R2. Within the estimate | Leaf count must land within ±20 % of the **18 Oct 2026 trimmed** estimate in §9 (the "Band" column). Outside that band, add a one-line reason in the roadmap's "Roadmap changes" section. |
| R3. Shadow leaves may change | While a leaf is `shadow` or `upnext`, it may be renamed, split, merged, moved or deleted. |
| R4. Live leaves are frozen | A leaf whose file has `status: live` keeps its `id`, stem and branch forever (CLAUDE.md rule 6, V8). Its `title` may be corrected; its position may not move before an earlier live leaf in a way that breaks `requires`. |
| R5. Log changes to M | Any edit that changes the number of leaves in a branch adds a row to "Roadmap changes": date, +n / −n, reason. This keeps the counter's denominator honest. |
| R6. No padding | Never add leaves to make a branch look bigger, never merge leaves to make progress look faster (P2, P8). The 18 Oct 2026 trim cuts scope (topics moved to `## Later`) and merges only leaves that fail the §3 merge test; every merged leaf must still pass the §3 split test. |
| R7. Later list | A `## Later` topic becomes a leaf only by a roadmap edit through `curriculum-designer`, logged under R5 (+1). Moving a shadow leaf to `## Later` is logged as −1. A `live` leaf can never move to `## Later` (R4). |

Rejected: *publish roadmaps with only the first stems and grow M later* — the counter would look faster than reality and break P2.

---

## 2. `_roadmap.md` file layout (all 17 files)

Sections appear in this order. Headings are exact so `housekeeper` can find them.

| # | Section | Content |
|---|---|---|
| 1 | YAML block | The first fenced ` ```yaml ` block in the file, exactly the schema of `docs/00` §8.1 (`branch`, `bough`, `stems[].id/title/why/leaves[].id/title/difficulty/requires/fruit`). Nothing before it except blank lines. |
| 2 | `## Branch prerequisites` | Cross-branch assumptions in words (§7). Foundation's file says "None. This is the start." |
| 3 | `## Stems` | One `### <stem-id> · <Stem title>` heading per stem, in YAML order, each followed by a **stem flag line** (§2.2) and the **leaf table** (§2.1). |
| 4 | `## Fruits` | One row per `fruit: true` leaf: leaf ID, capstone name, 3–6 constraints (L16: constraints and guidelines only, never a solution). Placement rules in §12. |
| 5 | `## Later` | Nice-to-know topics kept out of the tree (R1, R7, §9.1). A plain bullet list, one topic per bullet, with a one-line reason. **No tables and no backticked slugs**, so the V6 check never reads them as leaves. Not counted in M. |
| 6 | `## Roadmap changes` | Date · change · leaf-count delta · reason (R5). |

### 2.1 Leaf table (one per stem)

The first column is the leaf `id` in backticks, so V6 (YAML and tables list the same leaves in the same order) can be checked by a script. Columns are fixed:

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-null` | easy | "Why does my app crash when a field is empty?" | A lunch box with a label: the box exists, but it may hold no sandwich. | "null is the same as 0 or an empty string." | `sim: null-lunch-box` two labelled boxes ("always has a sandwich" / "might be empty"); counting the slices in an empty box without looking first fails. | `P:` what does `name?.length` print for null · `F(ai):` tidy code with `!!` on user input · `B:` remove the `?` | — |

Column rules:

- **Leaf**: slug only; must match the YAML `id`.
- **Diff**: copy of YAML `difficulty` (§4).
- **After this, a fresher can answer**: one question, in quotes, answerable in one or two sentences. If it needs "and", the leaf is two leaves (§3).
- **Analogy**: one real-life object or situation (lift, queue, passbook, kitchen). No software inside the analogy.
- **Misconception**: the most common wrong belief, in quotes, as a fresher would say it.
- **Sim**: `sim: <idea>` or `none: <reason>` (§6). Never empty.
- **Challenges**: codes `P:` Predict-the-output, `F:` Fix-the-bug, `F(ai):` Fix-the-bug written in AI-code style (§5), `B:` "Break it" prompt (`docs/01` §3.4). At least one code per leaf; leaves with no code use `P:` on a sim or a diagram.
- **Notes**: cross-branch `Assumes` (§7), `volatile` (§8), `⚠️ VERIFY` items, or `—`.

### 2.2 Stem flag line

Directly under each stem heading, one line:

```
Flags: phase=2 · ai-review · volatile
```

| Flag | Meaning | Required? |
|---|---|---|
| `phase=<1-6>` | Where this stem sits in the cross-branch order (§10). | Always. |
| `ai-review` | Most Fix-the-bug tasks in this stem use AI-code style (§5). | At least one stem per branch. |
| `volatile` | The stem depends on current Android/Kotlin/KMP behaviour that changes between releases (§8). Every leaf's facts must be re-checked against official docs right before it goes live. | When true. |

If no optional flag applies, write `Flags: phase=<n>`.

### 2.3 Acceptance criteria (file layout)

- [ ] The first fenced block is ` ```yaml ` and parses with only the §8.1 keys.
- [ ] Every YAML leaf appears exactly once in a table, same order, same stem (V6).
- [ ] Every stem has a `Flags:` line with a `phase`.
- [ ] Every `Sim` cell starts with `sim:` or `none:`.
- [ ] `## Fruits` lists every `fruit: true` leaf and nothing else.
- [ ] `## Later` exists (may say "None."), is a bullet list only, and no topic in it also appears as a YAML leaf.

---

## 3. Leaf size (P8: one idea, one page)

A leaf teaches **one idea** a fresher can carry away in one sentence.

| Measure | Target | Hard limit |
|---|---|---|
| Core idea | 1 | 1 |
| New technical terms named in bold | 1–3 | 5 |
| Reading time of the page | 8–12 min | 15 min (aligned with `docs/04` §6) |
| Doing time (sim, predict, try, fix) | 15–25 min | 25 min (reading 15 + doing 25 = the Career Switcher's 40-minute slot) |
| Video length | 8–12 min | 15 min |
| Runnable code examples | 1–3 | 4 |
| Sims | 0–1 | 2 |
| Same-branch `requires` | 0–2 | 4 (more means the leaf sits too deep or too wide) |

**Split test.** Split a leaf when any of these is true:

1. The title or the "can answer" question needs "and" or "vs" to cover two separate mechanisms. ("`val` vs `var`" is one idea, a choice; "Lists and maps" is two.)
2. It breaks a hard limit above.
3. Two different misconceptions need correcting.

**Merge test.** Merge two shadow leaves when one cannot be understood or tested alone (for example "what is a parameter" without "what is a function"), or when each is too small to fill a 10-minute page and both serve the **same** idea (for example "what is `if`" and "what is `else`" become one leaf about how a program decides). The merged leaf must pass the split test; if it fails, keep the two leaves or move one to `## Later`.

**Trim test (18 Oct 2026).** A topic stays a leaf only if a fresher needs it to reach the branch's `docs/01` §6.2 ability or to read typical AI-written Android/KMP code. Otherwise it goes to `## Later` (§9.1).

Worked example (Kotlin null safety, illustrative):

| Too big (one leaf) | Right size (four leaves) |
|---|---|
| "Null safety in Kotlin" | `what-is-null` · `nullable-types` · `safe-call-and-elvis` · `when-to-use-double-bang` |

Rejected: *one leaf per stem* — pages would run 60+ minutes and break P8 and the Career Switcher test.

---

## 4. Difficulty rule (easy / medium / hard)

Difficulty describes the **topic**, not the learner (`docs/00` §7, L1). It drives the leaf's green shade only. `curriculum-designer` proposes it; the author may change it before the leaf is live, after he has learned it.

Score each leaf on four questions. One point per "yes".

| # | Question |
|---|---|
| Q1 | Does it combine 3 or more earlier ideas at once? |
| Q2 | Does it depend on behaviour you cannot see in the code (timing, memory, threads, the OS killing the app, the network)? |
| Q3 | Does the everyday analogy break down in an important way that must be taught? |
| Q4 | Do working developers commonly get it wrong (it shows up in bug reports, code review or AI-written code)? |

| Score | Difficulty |
|---|---|
| 0–1 | `easy` |
| 2 | `medium` |
| 3–4 | `hard` |

Target mix per branch (a sanity check, not a quota): about 40 % easy, 45 % medium, 15 % hard. Foundation: at least 70 % easy and no `hard`. If a branch is above 30 % hard, check whether hard leaves should be split (§3).

Examples: `what-is-null` → Q4 only → easy. "Why does rotation wipe my form?" → Q2, Q4 → medium. "Structured concurrency and cancellation" → Q1, Q2, Q3, Q4 → hard.

Rejected: *author's gut feeling only* — inconsistent across 17 files written months apart.

---

## 5. AI-code-style Fix-the-bug stems (`ai-review`)

`docs/01` §3.5 requires at least one stem per branch whose Fix-the-bug tasks look like code an AI assistant would produce: tidy, confident, plausible names, comments that sound right, and one real defect.

Rules:

1. Mark the stem with `ai-review` in its flag line (§2.2). Mark each such task `F(ai):` in the Challenges column.
2. In an `ai-review` stem, at least half of the leaves carry an `F(ai):` task.
3. Each branch has **at least 1** `ai-review` stem; aim for about **1 in 4 stems**.
4. Pick stems where generated code really fails. Defaults:

| Branch | Typical `ai-review` stem topic | Typical defect |
|---|---|---|
| `foundation` | debugging mindset | off-by-one loop, wrong condition order |
| `kotlin` | null safety; a dedicated "reading code" stem | unneeded `!!`, shadowed variable, wrong equality |
| `android-core` | lifecycle, configuration change | state lost on rotation, leaked `Context` |
| `ui` | state and recomposition | state held in the wrong place, missing `remember` key |
| `concurrency` | scopes and cancellation | coroutine launched in the wrong scope, blocking call on main |
| `architecture-data` | ViewModel and state holders | screen talks to the database directly |
| `app-networking` | errors and retries | unhandled failure, no timeout |
| `kmp` | `expect`/`actual` and source sets | platform API in common code |
| `dsa` / `daa` | complexity | hidden O(n²), wrong base case |
| `os` / `networking` | processes/threads; HTTP | race condition; ignored status code |
| `testing` | test doubles, flaky tests | test that always passes |
| `profiling` | recomposition, startup | work on the main thread at startup |
| `native-ndk` | JNI references | leaked local reference (checked, §8.2 V1) |
| `security` | storage and logging | token written to the log |
| `tools` | Gradle config | version mismatch, wrong dependency scope |

5. The "reading code" stem in `kotlin` teaches the reading order from `docs/01` §3.5: types first, then what runs when, then what can be null, then what can run twice.
6. Never say "AI is bad". The task framing is "Would you accept this in review?" (`docs/01` §3.5).

Acceptance: a script counting `ai-review` stems per branch finds at least 1 in every one of the 17 roadmaps.

---

## 6. Sims (`sim:` / `none:`)

`docs/01` §3.3: any leaf about **memory, time, order, flow or state change** has a sim, or its row says why not.

| Leaf is about… | Sim cell |
|---|---|
| memory, time, order, flow, state change | `sim: <one-line idea>` required, unless `none:` gives a concrete reason |
| syntax, naming, a single rule, tooling steps | usually `none: <reason>` (e.g. `none: syntax rule, editor is enough`) |

Rules:

- The idea names the **physical metaphor** (balls in a pipe, a lift, a queue at a counter, springs, a conveyor belt) and **what the learner can push or break**. The physics choice itself belongs to `physics-designer` (`docs/05`); the roadmap only proposes.
- Reuse beats novelty: if two leaves can share one sim with different settings, say `sim: reuse <leaf-id> sim, <change>`.
- Android-only behaviour is never run in the browser: there is no Android emulator or simulator (L15 amended, D8). Show it with a `code-to-screen` sim (`docs/05` §4.3): the learner flips between pre-written code variants and watches a drawn screen and timeline change. Write `sim: code-to-screen <idea>`. Exact, step-by-step ideas with no Android screen use the `animation` kind (`docs/05` §4.2). The word "sim" always means a visualization on a lesson page, never a simulator.
- Kind by branch (author decision 18 Oct 2026): `android-core` lifecycle and process sims that show behaviour changing with code (`who-starts-your-app`, `back-stack`, the lifecycle leaves, `what-is-process-death`, `why-background-is-limited`, the startup leaves) are `code-to-screen`. `profiling` sims are `animation`: recorded runs the learner switches between, never a live or code-driven Android screen. Sims with no Android screen (git, CI, Gradle, algorithms) are usually `animation`.
- Roadmap prose never says "emulator" or "simulator" except to deny them. For the Xcode tool write "an iPhone or the Xcode test device".
- `none:` reasons that are **not** accepted: "too hard to build", "later", empty text.

Expected share: about 35–50 % of leaves in `concurrency`, `os`, `networking`, `dsa`, `daa`, `android-core`; about 15–25 % elsewhere. This guides `docs/05` workload. A branch above its guide adds one "Sim share note" row (delta 0) to its "Roadmap changes" with the reason and the number of sims that reuse another sim; on 18 Oct 2026 `dsa`, `daa`, `foundation`, `tools`, `security` and `native-ndk` did so.

---

## 7. Cross-branch prerequisites

`requires` in YAML holds **same-branch, earlier** leaves only (`docs/00` §8.1, V3). Anything outside the branch is written in words.

### 7.1 Wording

Branch level, in `## Branch prerequisites`, one bullet each:

```
- Assumes Foundation: `foundation/programming-thinking/what-is-a-loop` — the stem `collections` iterates lists.
- Helps to know OS: `os/processes/what-is-a-thread` — not required; the lesson re-explains it in one paragraph.
```

Leaf level, in the Notes column:

```
Assumes Networking: `networking/http/what-is-a-status-code`
```

| Keyword | Meaning | Rule |
|---|---|---|
| `Assumes <Branch name>:` | Hard: the lesson does not re-explain it. | The checked target is a full leaf ID that exists, in a stem whose `phase` is **lower than or equal to** the assuming stem's phase (§10). If equal phase, the target branch must be earlier in the §10.1 table. |
| `Helps to know <Branch name>:` | Soft: the lesson gives a short recap. | No phase restriction. Never creates a dependency. |

Branch names are the display names from `docs/00` §5, or a short branch name ("Foundation", "Kotlin", "OS", "DSA", "UI", "DAA", "KMP", "Concurrency", "Android core", "Tools", "Networking", "App networking", "Native layer") or the branch slug (`foundation`, `android-core`, `app-networking`, `native-ndk` and so on) when the line names a **broad dependency** on a whole branch or stem (author decision 18 Oct 2026, for example `Assumes: foundation`). A **specific dependency** on one lesson always names the leaf by its full ID so a script can resolve it. A line that gives only a branch name is a hint for the reader, not an edge: the phase rule below and the cycle check apply only to full leaf IDs, and a hard dependency on one leaf needs that leaf's full ID in the same line or in the Notes cell.

`Assumes` is for other branches only. A dependency inside the same branch belongs in YAML `requires`, not in an `Assumes` note.

### 7.2 No cycles

- Same-branch `requires`: point only to earlier leaves (V3). A forward pointer or a cycle is impossible if V3 passes.
- Cross-branch `Assumes`: the phase rule above makes cycles impossible: an edge always goes to a lower phase, or to an earlier branch inside the same phase.
- If a leaf seems to need a later-phase leaf, do one of: move the target stem to an earlier phase, downgrade to `Helps to know` with a recap, or move the leaf to a later phase.

---

## 8. Volatile facts

A stem is `volatile` when its leaves state behaviour that changes with Android, Kotlin, Compose, AGP/Gradle or KMP releases. Examples (each needs checking when the roadmap is written; the ones marked "checked" were read in official docs on 18 Oct 2026, see §8.1 and §8.2): edge-to-edge and system-bar rules on recent Android versions (checked, §8.2 V2); predictive back (checked, §8.2 V3); background work and foreground-service limits (checked in part, §8.2 V4); runtime permissions; Compose Multiplatform iOS status (checked, §8.1 M6); Gradle/AGP DSL; Play publishing requirements (checked, §8.2 V5).

Rules:

1. In the roadmap, the leaf's Notes cell says what to check (`⚠️ VERIFY: minimum API that enforces edge-to-edge`), with no claimed answer unless it was checked against official docs (developer.android.com, kotlinlang.org, jetbrains.com) on a stated date.
2. A `volatile` leaf must be re-checked by `reviewer` right before it goes live (P9), even if the roadmap note says "checked".
3. Titles of volatile leaves avoid version numbers where possible ("Why does my app draw under the status bar?" not "Android 15 edge-to-edge"), so slugs survive new releases.

### 8.1 KMP without a Mac (author on Windows, free tiers only, L27)

Checked 18 Oct 2026. Every row gives the page URL and the date **the page itself shows** ("footer" = date at the foot of the page, not labelled as last-updated; "no date" = the page shows none). Re-read the page before the lesson goes live (§8 rule 2). Roadmap prose never says "emulator" or "simulator" (§6): the Xcode tool is "the Xcode test device".

**What compiles where**

| # | Fact | Source (page date) |
|---|---|---|
| M1 | Shared code and platform code other than iOS can be written on any OS the IDE supports. Writing iOS-specific code and running an iOS app needs a Mac, because the Xcode test device runs only on macOS, not on Windows or Linux. | kotlinlang.org/docs/multiplatform/faq.html (footer 01 Oct 2026) |
| M2 | "To create iOS applications, you need a macOS machine with Xcode installed. Your IDE runs Xcode under the hood to build iOS frameworks." Xcode must be launched once before KMP work. | kotlinlang.org/docs/multiplatform/quickstart.html (footer 02 Oct 2026) |
| M3 | Final binaries for the iOS targets (`iosArm64` for iPhones, the Apple-silicon test-device target, `iosX64`) can be built on a macOS host only: "Building final binaries for Apple targets on Linux and Windows is also not possible." A Windows host may produce `.klib` files for Apple targets only when the project has no cinterop dependency (CocoaPods counts). **Answer: Kotlin/Native iOS cannot be cross-compiled from Windows into something that runs.** | kotlinlang.org/docs/native-target-support.html (footer 19 Aug 2026) |
| M4 | "A target that is not supported by the current host is ignored during building and, therefore, not published." So on Windows the iOS targets are skipped. | kotlinlang.org/docs/multiplatform/multiplatform-dsl-reference.html (footer 07 Sep 2026). Test: run `gradlew build` once on Windows and read the log for the skipped iOS targets; keep that log as lesson evidence. |
| M5 | Stability table: Android, iOS, desktop (JVM) and server (JVM) are Stable; web on Kotlin/JS Stable, on Kotlin/Wasm Beta; watchOS and tvOS Beta. On Windows the author can build Android, desktop, server and web (M1, M3). | kotlinlang.org/docs/multiplatform/supported-platforms.html (footer 10 Sep 2025) |
| M6 | Compose Multiplatform: Android, iOS and desktop (JVM) Stable, web on Kotlin/Wasm Beta, from the same table as M5. It is the only official stability table found and is **over a year old**; a newer official page repeats it ("stable for iOS, Android, and desktop, with web in Beta", kotlinlang.org/docs/multiplatform/kotlin-multiplatform-react-native.html, footer 21 Jul 2026); the compatibility page (footer 15 Oct 2026) names Compose Multiplatform 1.12.2 with minimum iOS 14 and a WasmGC browser for web but gives no stability labels. Lessons say "stable on iOS according to JetBrains' table, read on [date]", with no version number. Test at publish: reopen the M5 page and copy the current status. | supported-platforms.html (footer 10 Sep 2025); kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html (footer 15 Oct 2026) |
| M7 | The Android Studio KMP plugin offers run configurations for iOS and Android. On Windows only the Android half can run (M1, M2). | developer.android.com/kotlin/multiplatform ("Last updated 2026-10-12 UTC") |
| M8 | The current Xcode line needs macOS Tahoe 26.6 or later. A borrowed Mac must meet that; check its newest allowed macOS first. | developer.apple.com/support/xcode (no date; copyright 2026) |

**Free options**

| # | Option | Fact | Source (page date) | Verdict |
|---|---|---|---|---|
| O1 | GitHub Actions macOS runner | "Use of the standard GitHub-hosted runners is free and unlimited on public repositories." macOS labels: `macos-latest`, `macos-14`, `macos-15`, `macos-26` (arm64, 3 CPU, 7 GB) and `macos-15-intel`, `macos-26-intel` (4 CPU, 14 GB). `-latest` is the latest stable image, not always Apple's newest macOS. Larger runners are not free for public repositories. The same macOS runner on a private repository costs $0.062 per minute, so the practice repository **must be public**. | docs.github.com/en/actions/reference/runners/github-hosted-runners (no date); docs.github.com/en/billing/concepts/product-billing/github-actions (no date); docs.github.com/en/billing/reference/actions-minute-multipliers (no date) | **Use** |
| O2 | Official KMP CI sample | Job on `macos-latest`, "which includes `xcodebuild`"; it builds the iOS app for the Xcode test device (so no signing) and uploads the built app folder. The page does not claim the app is run or photographed. | kotlinlang.org/docs/multiplatform/github-actions-for-kmp.html (footer 18 Mar 2026) | **Copy this workflow** |
| O3 | Borrowed Mac | Free; needs M8 and Xcode launched once (M2). Use for one "iOS day" to capture real screen recordings for all Mac-needed leaves in a batch. | M2, M8 | **Optional extra** |
| O4 | Codemagic | Individual plan lists "500 free macOS M2 minutes / month", 1 parallel build. Vendor page, terms may change. | codemagic.io/pricing (no date) | Backup only |
| O5 | Xcode Cloud | "25 compute hours per month" but "requires Apple Developer Program membership" (99 USD per year) and is set up from Xcode on a Mac. | developer.apple.com/xcode-cloud and developer.apple.com/programs/whats-included (no date) | Rejected: not free, needs a Mac (L27) |
| O6 | JetBrains Fleet | JetBrains stopped distributing Fleet on 22 Dec 2025 and never offered a Mac. ⚠️ VERIFY: the blog page (blog.jetbrains.com/fleet/2025/12/the-future-of-fleet/) returned HTTP 403 to the fetch tool; the date comes from search summaries of it. Test: open the page in a browser. | search summaries, Dec 2025 | Rejected |

Not tested, not used: paid cloud Macs, and macOS inside a virtual machine on Windows (cost; Apple licence terms not checked).

**Recommended free plan.** Write and run all shared Kotlin, Android and desktop work on Windows in a public GitHub repository, and let the free `macos-latest` GitHub Actions runner (copy of the O2 workflow) build the iOS side and keep its log and artifacts as the author's iOS evidence, with a borrowed Mac as an optional one-day extra.

**What the author may honestly claim as "understood" (L5)**

| Part of a lesson | Evidence the author needs | Label printed on the lesson page |
|---|---|---|
| Concept, `commonMain` and `androidMain` code, Gradle files, plain-Kotlin models | He built and ran it on Windows | "Ran on my Windows machine" |
| iOS side of a `Mac needed` leaf | A green CI run on the public repository whose link and log excerpt he can show | "Seen in this CI run" (link) |
| Anything on a real iPhone, signing, TestFlight, App Store | Only if he borrows a Mac or pays 99 USD per year (O5, outside L27) | "From the docs, not run by me" |

He must never write "I ran this on an iPhone" from a CI build alone. **Proposal, author to confirm:** the three labels are plain sentences in the lesson body; they add no frontmatter field (`docs/00` §8.2 is locked). Lesson structure belongs to `docs/04`, which was not read for this pass; `docs/04` decides where the labels sit.

**How a `Mac needed` leaf is taught (applies to every leaf whose Notes say `Mac needed`)**

1. Teach the concept and the shared code first. It must stand alone: the learner can finish the leaf with the Kotlin Playground model, a read-only listing or a diagram, and never owns a Mac.
2. Show the iOS result as a recorded artifact from the CI run, never as a live claim: the log lines, the generated framework header, the built app folder, or a screenshot or recording only if experiment E3 works. Copy the excerpt into the repo's lesson assets so the lesson does not depend on how long GitHub keeps run files.
3. Print the §8.1 label next to each iOS-side claim.
4. Name the Xcode steps that stay outside the lesson (signing, account) as "From the docs, not run by me".
5. **Proposal, author to confirm:** a `Mac needed` leaf goes live only when the author links a green CI run for it, or notes a borrowed-Mac session. This is a suggested check for the author and `reviewer`, not a new field and not yet a rule; L5 and CLAUDE.md name the real gates.

**Experiments for the author's first CI week** (each has a pass test; until it passes, the lesson claims nothing it needs)

| # | Experiment | Pass test | If it fails |
|---|---|---|---|
| E1 | Public repository plus the O2 workflow on `macos-latest` | A green run whose log shows `xcodebuild` finishing | Try Codemagic (O4) |
| E2 | A Gradle step that links the iOS framework and uploads the generated header file | The header downloads and shows the Kotlin names as Swift sees them (used by `calling-kotlin-from-swift`) | Fall back to the mapping table from the docs |
| E3 ⚠️ VERIFY | Start the built app on the Xcode test device in CI and save a screenshot | A screenshot file in the run. No fetched official page shows this works, so it is unproven | Use log, header and source only; never claim a running iPhone screen |
| E4 | `gradlew build` on Windows | The log shows the iOS targets skipped (M4) and the rest built | Read the Gradle error; do not guess |

### 8.2 Other facts checked (non-KMP, 18 Oct 2026)

Dates are the "Last updated" line the page shows unless marked otherwise. These settle the earlier open fact checks in §5, §8, §9 and §9.1. A `volatile` leaf is still re-checked right before it goes live (§8 rule 2).

| # | Fact | Source (page date) |
|---|---|---|
| V1 | JNI: local references are valid only for the current native call on the current thread. Code that makes many in a loop should free them with `DeleteLocalRef`. On a thread attached with `AttachCurrentThread`, local references are not freed until the thread detaches. Before Android 8.0 the count is capped; from Android 8.0 it is unlimited. Global references live until `DeleteGlobalRef`. Never compare references with `==`; use `IsSameObject`. The typical AI-code defect is therefore a local reference created in a loop or on an attached thread and never deleted, or a global reference never deleted. | developer.android.com/guide/practices/jni (2026-03-06) |
| V2 | Edge-to-edge: an app that targets SDK 35 or higher is shown edge-to-edge on Android 15 or higher. For apps targeting API 36 on Android 16, the old opt-out attribute is deprecated and disabled. Before that, call `enableEdgeToEdge()` in the activity. | developer.android.com/develop/ui/views/layout/edge-to-edge (2026-10-16); developer.android.com/about/versions/16/behavior-changes-16 (2026-10-16) |
| V3 | Predictive back: system animations are on by default from Android 15 for apps that support predictive back. Opt out with `android:enableOnBackInvokedCallback="false"`. For apps targeting API 36 on Android 16, the animations are on by default and `onBackPressed` is not called. | developer.android.com/guide/navigation/custom-back/predictive-back-gesture (2026-10-16); behavior-changes-16 page above |
| V4 | Foreground services: apps targeting Android 12 or higher cannot start one from the background except in listed cases (otherwise `ForegroundServiceStartNotAllowedException`). From Android 14 each foreground service must declare a type and its permission. ⚠️ VERIFY (not read): the Android 15 `dataSync` timeout. Test: open the "foreground service timeouts" page under developer.android.com/develop/background-work/services/fgs and copy its limit with the date. | developer.android.com/develop/background-work/services/fgs/restrictions-bg-start and .../service-types (both 2026-10-16) |
| V5 | Google Play target API: new apps and updates must target Android 15 (API 35) since 31 Aug 2025 and Android 16 (API 36) from 31 Aug 2026; an extension to 1 Nov 2026 can be requested. On 18 Oct 2026 the API 36 rule is in force. | support.google.com/googleplay/android-developer/answer/11926878 (no date) |
| V6 | Baseline Profiles let ART compile listed code paths ahead of time; Google reports "about 30%" faster code execution from the first launch (typical, not guaranteed). Needs Android Gradle plugin 8.0.0 or newer and the Macrobenchmark library to generate them. Cloud Profiles need Android 9 (API 28) or higher. | developer.android.com/topic/performance/baselineprofiles/overview (2026-10-16) |
| V7 | `lmkd` (low memory killer daemon) watches memory pressure and ends lower-priority processes; on Android 10 or higher it uses kernel pressure stall information (PSI) by default; the old in-kernel driver was removed from the upstream kernel. | source.android.com/docs/core/perf/lmkd (2026-10-09) |
| V8 | Zygote forks app processes and is the root of system and app processes of one ABI (it may keep a pool of pre-forked processes). The page does not say it "preloads classes": do not write that. Binder is "a system for interprocess communication that lets two processes on an Android-powered device communicate" (client proxy, server node, kernel driver; AIDL is recommended). Since Android 5.0 each app runs in its own process with its own ART instance (this last page is old, 2024-05-20). | source.android.com/docs/core/runtime/zygote (2026-10-09); source.android.com/docs/core/architecture/ipc/binder-overview (2026-10-09); developer.android.com/guide/platform (2024-05-20) |
| V9 | Play Integrity API: checks that requests come "from your unmodified app binary, installed by Google Play, running on a genuine Android device". The verdict has `accountDetails` (licensing), `appIntegrity`, `deviceIntegrity` and optional `environmentDetails`; the server must check `requestDetails` against its own request first. | developer.android.com/google/play/integrity (2026-06-08); .../integrity/verdicts (2026-10-06) |
| V10 | The standard is named "OWASP MASVS" (Mobile Application Security Verification Standard), part of the OWASP Mobile Application Security (MAS) project, with eight control groups: STORAGE, CRYPTO, AUTH, NETWORK, PLATFORM, CODE, RESILIENCE, PRIVACY. The page shows no version or date. | mas.owasp.org/MASVS/ (no date) |

---

## 9. Leaf-count estimates (the honest M)

Stem lists below are **draft sketches** to size each branch; the number after each stem is its approximate leaf count. The branch's `_roadmap.md` wins once written; R2 allows ±20 % of the **Est.** value (the **Band** column). These estimates replace the v1 figures (820 total) from 18 Oct 2026.

| Branch | Est. (v1) | Est. | Band (±20 %) | Draft stem sketch (in order, ~leaves) |
|---|---|---|---|---|
| `foundation` | 40 | **32** | 26–38 | how-computers-work 6 · first-code 3 · programming-thinking 11 · debugging-mindset 4 · from-code-to-running 4 · how-an-app-is-born 4 |
| `kotlin` | 100 | **60** | 48–72 | first-programs 3 · types-and-variables 4 · control-flow 4 · functions 5 · null-safety 4 · strings 3 · collections 6 · lambdas 5 · classes-and-objects 5 · interfaces-and-inheritance 4 · special-classes (data, enum, sealed, object) 5 · generics 2 · extensions-scope-and-delegates (incl. `by lazy`) 4 · errors-and-results 3 · reading-code 3 |
| `android-core` | 60 | **34** | 27–41 | what-is-an-app 2 · activities 3 · lifecycle 4 · configuration-changes 2 · process-death 2 · intents 3 · manifest-and-resources 3 · permissions 3 · background-work 4 · services-and-notifications 3 · files-and-sharing 3 · app-startup 2 |
| `ui` | 80 | **44** | 35–53 | first-composable 3 · layouts 4 · modifiers 3 · state-and-recomposition 5 · lists 3 · theming 3 · navigation 3 · side-effects 3 · forms-and-input 3 · animation 3 · accessibility 3 · adaptive-layouts (incl. edge-to-edge, checked §8.2 V2) 2 · views-and-interop (reading legacy XML, Compose inside Views) 4 · custom-drawing 2 |
| `concurrency` | 45 | **28** | 22–34 | why-the-main-thread 2 · threads 2 · coroutine-basics 3 · suspend 2 · dispatchers 2 · scopes 3 · cancellation-and-errors 4 · flow-basics 3 · flow-operators 3 · state-and-shared-flow 2 · channels 1 · shared-mutable-state 1 |
| `architecture-data` | 55 | **30** | 24–36 | why-architecture 2 · viewmodel 3 · ui-state-and-udf 4 · repositories 2 · dependency-injection 4 · key-value-storage 2 · databases 5 · offline-first 3 · paging 1 · modules 2 · layers-and-mapping 2 |
| `app-networking` | 30 | **16** | 13–19 | requests-from-an-app 2 · http-client 3 · json 2 · errors-and-retries 3 · auth-tokens 2 · caching 1 · images 1 · real-time 1 · sync 1 |
| `kmp` | 40 | **22** | 18–26 | what-is-kmp 2 · source-sets 2 · expect-actual 2 · shared-logic 2 · shared-networking 3 · shared-storage 3 · compose-multiplatform 4 (iOS status checked, §8.1 M6) · swift-interop 2 · shipping-to-ios 1 · libraries 1 |
| `dsa` | 85 | **46** | 37–55 | complexity 4 · arrays 3 · strings 2 · linked-lists 3 · stacks 2 · queues 2 · hashing 3 · recursion 3 · sorting 5 · searching 2 · trees 3 · bst 2 · heaps 2 · graphs 5 · tries 1 · patterns (two pointers, sliding window) 4 |
| `daa` | 45 | **22** | 18–26 | analysis 3 · recurrences 2 · divide-and-conquer 3 · greedy 3 · dynamic-programming 5 · backtracking 2 · graph-algorithms (shortest path, spanning tree) 3 · hard-problems (P, NP) 1 |
| `os` | 45 | **22** | 18–26 | what-an-os-does 2 · processes 3 · threads 3 · memory (incl. virtual memory) 3 · scheduling 2 · synchronisation 3 · deadlock 1 · files 2 · android-os (kernel, zygote, binder, low-memory killer; checked §8.2 V7 and V8) 3 |
| `networking` | 40 | **20** | 16–24 | how-the-internet-works 2 · layers 2 · ip-and-dns 3 · tcp-and-udp 3 · http 4 · tls 2 · mobile-networks-and-latency 2 · api-styles 1 · websockets 1 |
| `testing` | 40 | **20** | 16–24 | why-test 1 · unit-tests 2 · assertions 1 · test-doubles 3 · testing-viewmodels 2 · testing-coroutines 3 · ui-tests 3 · device-tests 1 · test-first 1 · flaky-tests 2 · screenshot-tests 1 |
| `profiling` | 30 | **14** | 11–17 | measure-first 1 · profiler-tour 1 · cpu 2 · memory-and-leaks 2 · jank-and-frames 2 · compose-performance 2 · startup 2 (baseline profiles checked, §8.2 V6) · battery-and-network 1 · app-size 1 |
| `native-ndk` | 25 | **10** | 8–12 | what-is-native 1 · art-and-dex 2 · jni-basics 3 · ndk-build 2 · native-memory 1 · when-to-go-native 1 |
| `security` | 30 | **14** | 11–17 | threat-thinking 1 · secure-storage 2 · network-security 2 · auth 2 · privacy-and-permissions 2 · obfuscation 1 · webviews 1 · secrets-and-logs 2 · app-integrity 1 (checked, §8.2 V9) |
| `tools` | 30 | **16** | 13–19 | git 4 · android-studio 2 · gradle-basics (incl. version catalogs) 3 · adb-and-logcat 2 · lint-and-static-analysis 1 · ci 2 · publishing 2 (Play target-API rule checked, §8.2 V5) |
| **Total** | **820** | **450** | 360–540 | 17 roadmaps, all branches kept. **Real total on 18 Oct 2026: 482 (table below).** |

### 9.0 Real counts (18 Oct 2026, from the 17 YAML blocks)

The roadmaps now exist, so the counter's M is the real number: **482 leaves, 180 stems, 20 fruits**. It is 7.1 % above the 450 estimate (more than the 5 % trigger below), but every branch is inside its Band, so R2 holds. The Est. and Band columns above stay as the planning reference; this table is the one to quote.

| Branch | Real leaves | Est. | Band | Stems | Fruits |
|---|---|---|---|---|---|
| `foundation` | 34 | 32 | 26–38 | 6 | 0 |
| `kotlin` | 65 | 60 | 48–72 | 15 | 2 |
| `android-core` | 37 | 34 | 27–41 | 12 | 2 |
| `ui` | 49 | 44 | 35–53 | 14 | 2 |
| `concurrency` | 32 | 28 | 22–34 | 13 | 1 |
| `architecture-data` | 33 | 30 | 24–36 | 12 | 1 |
| `app-networking` | 17 | 16 | 13–19 | 9 | 1 |
| `kmp` | 22 | 22 | 18–26 | 10 | 1 |
| `dsa` | 46 | 46 | 37–55 | 16 | 2 |
| `daa` | 22 | 22 | 18–26 | 9 | 1 |
| `os` | 23 | 22 | 18–26 | 10 | 1 |
| `networking` | 21 | 20 | 16–24 | 10 | 1 |
| `testing` | 22 | 20 | 16–24 | 11 | 1 |
| `profiling` | 16 | 14 | 11–17 | 10 | 1 |
| `native-ndk` | 11 | 10 | 8–12 | 6 | 1 |
| `security` | 15 | 14 | 11–17 | 10 | 1 |
| `tools` | 17 | 16 | 13–19 | 7 | 1 |
| **Total** | **482** | **450** | 360–540 | **180** | **20** |

The review that counted 481 found `app-networking` at 16; the same day `timeouts-and-cancellation` was split into `timeouts` and `cancelling-a-call` (A2e fix, +1, logged in that roadmap), which makes 482. Of the 24 titles that failed the one-idea test, 1 was that split and 23 were reworded to one idea each with no change in count.

What changed in the trim: `kotlin` advanced stem and `tools` gradle-in-depth moved to `## Later`; `os` virtual-memory merged into memory; `ui` views-and-xml and interop merged into views-and-interop; every other stem kept with fewer, fuller leaves. Foundation keeps all six stems so an absolute fresher still reaches "run your first app on a phone" with no gaps.

`housekeeper` reports the real M; the §9.0 table is updated whenever the real total changes (R5 keeps each roadmap's log in step). The Est. and Band columns were last re-checked on 18 Oct 2026, when the real total (482) differed from 450 by more than 5 %.

### 9.1 Later lists (not counted, not in the tree)

Each roadmap's `## Later` section starts from these drafts. Keep it short: at most about 8 bullets per branch. These topics never appear on the tree, in YAML, or in M (R1, R7).

| Branch | Later (draft) |
|---|---|
| `foundation` | binary arithmetic in depth (two's complement, floating-point layout) · how a CPU pipeline and cache work · history of computing |
| `kotlin` | inline and reified functions · building DSLs · operator overloading · value classes · contracts · generic variance in depth (`in`/`out`) · Kotlin reflection |
| `android-core` | fragments (legacy) · content providers in depth · broadcast receivers in depth · home-screen widgets · app shortcuts · multi-window details |
| `ui` | custom layouts (measure and place) · shared-element transitions · Wear OS and TV UI · MotionLayout (legacy Views) · advanced Canvas shaders |
| `concurrency` | `select` expressions · actors · custom dispatchers and limited parallelism · RxJava (reading old code) · Flow internals |
| `architecture-data` | MVI frameworks · clean-architecture use-case debates · Paging in depth · DI library internals · database migrations at scale |
| `app-networking` | GraphQL clients · gRPC · advanced sync conflict resolution · HTTP/2 multiplexing from the client side |
| `kmp` | Kotlin/Wasm and web targets · desktop targets · publishing a KMP library · Kotlin/Native memory internals |
| `dsa` | balanced trees (AVL, red-black rotations) · segment and Fenwick trees · skip lists · suffix arrays · union-find in depth |
| `daa` | amortised analysis (potential method) · network flow · string matching (KMP, Rabin-Karp) · approximation and randomised algorithms |
| `os` | page-replacement algorithms · file-system internals (inodes, journaling) · advanced scheduling (multi-level feedback queues) · semaphores vs monitors theory |
| `networking` | subnetting maths · IPv6 details · HTTP/3 and QUIC internals · CDNs · routing between networks (BGP) |
| `testing` | property-based testing · mutation testing · contract tests · test architecture for large modular apps |
| `profiling` | Perfetto trace deep dive · custom Macrobenchmark metrics · R8 tuning in depth · custom trace sections |
| `native-ndk` | CMake in depth · native crash symbolication · Vulkan graphics · SIMD |
| `security` | reverse-engineering walkthrough · root and tamper detection · cryptography internals · full OWASP MASVS mapping (name checked, §8.2 V10) |
| `tools` | Gradle in depth (custom and convention plugins, build cache) · advanced Git (interactive rebase, bisect) · writing custom lint checks |

---

## 10. Recommended cross-branch learning order

Branches stay independent and open (P1); this is a **recommended** order, and the basis of the `phase` flag (§2.2) and the `Assumes` rule (§7).

### 10.1 Phases

The leaf counts below are the real ones from the 17 roadmaps' `Flags: phase=` lines and stem tables (18 Oct 2026). They replace the 450-leaf sketch.

| Phase | Name | Stems opened (real leaves) | ~Leaves |
|---|---|---|---|
| 1 | Roots | `foundation` all (34) | 34 |
| 2 | Speak Kotlin | `kotlin` first-programs → classes-and-objects (44) · `tools` git, android-studio (7) · `dsa` complexity, arrays, strings (9) | 60 |
| 3 | First real app | `kotlin` interfaces → reading-code (21) · `os` what-an-os-does, processes, memory, threads (11) · `dsa` linked-lists → recursion (13) · `android-core` what-is-an-app → process-death (13) · `ui` first-composable → navigation (26) | 84 |
| 4 | Apps that wait, remember and talk | `os` scheduling, synchronisation, deadlock (6) · `networking` all (21) · `dsa` sorting → bst (12) · `concurrency` all (32) · `architecture-data` all (33) · `app-networking` all (17) | 121 |
| 5 | Prove and polish | `tools` gradle-basics, adb, lint (6) · `dsa` heaps → patterns (12) · `android-core` manifest-and-resources → app-startup (24) · `ui` side-effects → custom-drawing (23) · `testing` all (22) · `daa` all (22) · `profiling` all (16) | 125 |
| 6 | Everywhere and underneath | `os` files, android-os, putting-it-together (6) · `tools` ci, publishing (4) · `kmp` all (22) · `security` all (15) · `native-ndk` all (11) | 58 |
| | | **Total** | **482** |

The `kotlin` advanced stem left phase 6 in the 18 Oct 2026 trim (now in `kotlin` `## Later`); `by lazy` and other property delegates are taught in phase 3 inside extensions-scope-and-delegates, because Android code uses them everywhere.

**Tie-break order inside a phase** (used by the §7.1 `Assumes` rule): foundation · kotlin · tools · os · dsa · networking · android-core · ui · concurrency · architecture-data · app-networking · testing · daa · profiling · kmp · security · native-ndk. Within one phase, a hard `Assumes` may only point to a branch earlier in this list.

Why this order: Kotlin before any Android (you cannot read Compose without lambdas and classes); OS threads before coroutines (a coroutine only makes sense once a blocked thread does); computer networking before in-app networking; testing after architecture (you test ViewModels and repositories); KMP last because it re-uses architecture, networking, storage and Gradle.

### 10.2 Weekly mix

- Phase 1: all 3 weekly leaves from `foundation`.
- Phases 2–6: default **2 app-track leaves + 1 `cs-theory` leaf** per week. This keeps the Theory-Scared Student's link between symptom and theory (`docs/01` §2.3) and grows more than one bough.
- A leaf is eligible only when all its `requires` and `Assumes` targets are `live`. A stem of phase n+1 may start before phase n is complete if that holds.
- The 2 + 1 mix is best-effort. Phase 2 has only 9 `cs-theory` leaves (`dsa`) among 60, and phase 6 has about 6 `os` leaves among 58, so in those phases the week may be 3 app-track leaves, or a theory leaf from a neighbouring phase when its prerequisites are live.

### 10.3 Answering "what is next"

1. List the up-next leaf of each branch whose current stem is in the earliest open phase.
2. Drop any whose `requires` or `Assumes` targets are not live; name the missing prerequisite.
3. Pick 3, preferring the §10.2 mix and the tie-break order.

---

## 11. "Start here" path through Foundation

"Start here" opens the first live Foundation leaf, else the Foundation up-next leaf (`docs/03` §5). The path is the Foundation YAML order. Draft:

| # | Stem | ~Leaves | Example leaf questions (draft) |
|---|---|---|---|
| 1 | how-computers-work | 6 | What does a computer actually do all day? · What is a bit? · Why does a phone have both memory and storage? |
| 2 | first-code | 3 | What is code? · Where can I run code without installing anything? (Kotlin Playground, D8) |
| 3 | programming-thinking | 11 | What is a variable? · How does a program decide? · How does a program repeat? · What is a function? · How do I trace code by hand? |
| 4 | debugging-mindset (`ai-review`) | 4 | What is an error message telling me? · How do I find a bug without guessing? |
| 5 | from-code-to-running | 4 | What is a compiler? · What do the JVM and ART do (overview)? |
| 6 | how-an-app-is-born | 4 | How does code become an app on a phone (build → APK/AAB → install → run)? · What happens when I tap an app icon? |

The counts above are the sketch; the real Foundation roadmap has 34 leaves (programming-thinking 12, how-an-app-is-born 5; the other stems as sketched). The 18 Oct 2026 trim keeps all six stems and every idea listed in `docs/00` §5 for Foundation; it removes only overlap (for example one leaf on memory vs storage instead of two) and moves deep number-system and CPU-internals topics to Foundation's `## Later`.

Foundation rules: no `Assumes` of any kind; every term is defined on the page where it first appears; no `hard` leaves (§4); stems 2–4 use the browser editor only, no Android Studio until stem 6. After Foundation, `kotlin` is the recommended next branch (text in its Branch prerequisites; no forced hand-off, `docs/03` §5).

---

## 12. Fruit (capstone) placement

| Rule | Detail |
|---|---|
| F1 | `foundation` has **no fruit** (roots have no tips; D12 puts fruits at branch tips). Its finale is "run your first app on a phone". |
| F2 | Every other branch has **1–3** fruits. The **last leaf of the branch** (YAML order) always has `fruit: true`; it should be a synthesis leaf, not a minor topic. |
| F3 | Extra fruits sit on the last leaf of a stem that ends a phase segment (§10.1) and are at least 15 leaves apart. |
| F4 | A fruit's constraints use only leaves at or before it in the branch plus its `Assumes` targets. |
| F5 | Each `## Fruits` row names the `docs/01` §6.2 ability it proves and gives 3–6 constraints, never steps or a solution (L16). The author builds and shows his own. |
| F6 | Expected total: **20 fruits** across 16 branches (real count, 18 Oct 2026): one in each of 12 branches, two in each of `kotlin`, `android-core`, `ui` and `dsa`, none in `foundation`. The 15-leaf gap of F3 allowed second fruits only in those four. |

Example row: `` `kotlin/reading-code/review-a-generated-file` `` · "Review a 150-line generated file" · constraints: list its 3 biggest risks; every risk names a line; fix one with a test or a run · proves the "read a 200-line AI-written file and list its three biggest risks" check (`docs/01` §6.2, L24). `reading-code` is the last stem of `kotlin`, so this is the branch's F2 fruit on its last leaf. The extra fruit (F3) of `kotlin` sits earlier, on the last leaf of a stem 15 or more leaves before the end (`kotlin` leaf 44 of 65).

---

## 13. Cadence in plain English (for the author)

- The full tree is **482 leaves** (the 18 Oct 2026 plan was about 450 after trimming from about 820; the real roadmaps came out 7 % larger, all 16 branches and Foundation kept (17 roadmaps)). At **3 leaves a week**, that is about **161 weeks: roughly 3 years and 1 month**.
- If you take one rest week per quarter (`docs/01` §9), you publish about 144 leaves a year, so about **3 years and 4 months**.
- If real life gives you closer to 2.5 a week, plan for **about 3 years and 8 months**.
- Milestones at 3 a week: **Foundation done in about 2.6 months** (34 leaves). End of phase 3, "first real app", after about 178 leaves: **about 1 year 2 months**. End of phase 4, when you can build an app that loads, stores and shows data properly: about 299 leaves, **about 1 year 11 months**.
- The counter still shows the real size: M = the must-know plan in the roadmaps (482 today). `## Later` topics are not promises and are never counted. If the horizon still feels too long, the honest options are to raise the cadence or to move more topics to `## Later` (logged under R5 and R7), never to merge leaves into fat pages.

---

## 14. Roadmap review checklist (`curriculum-designer` and `reviewer`)

Structure

- [ ] First block is ` ```yaml ` with only `docs/00` §8.1 keys; `branch` and `bough` match `docs/00` §5.
- [ ] All slugs kebab-case and unique in the branch (V2); no `live` slug changed (R4, V8).
- [ ] Tables cover exactly the YAML leaves, same order (V6); `## Branch prerequisites`, `## Fruits`, `## Roadmap changes` present.

Order and prerequisites

- [ ] Every `requires` is a full ID, same branch, earlier in the file (V3). No cycle.
- [ ] Every specific `Assumes` names a full leaf ID that exists and obeys the phase and tie-break rule (§7). A broad dependency may use a short branch name (§7.1).
- [ ] Every stem has `Flags: phase=<n>`.

Leaves

- [ ] Each leaf passes the split test (§3); title is a question where possible (`docs/01` §3.1).
- [ ] Every row has all 8 cells filled; Sim cell starts with `sim:` or `none:` with an accepted reason (§6).
- [ ] Difficulty follows the §4 score; branch mix within the sanity band.
- [ ] Plain English in every cell (`docs/01` §7); no banned words.

Coverage

- [ ] At least one `ai-review` stem; at least half its leaves have `F(ai):` (§5).
- [ ] Leaf count inside the §9 Band (±20 % of the trimmed estimate), or the reason is logged (R2).
- [ ] Nice-to-know topics sit in `## Later`, not in the YAML (R1, §3 trim test).
- [ ] The branch reaches its `docs/01` §6.2 ability.
- [ ] Fruits follow F1–F6.
- [ ] Every `volatile` stem's leaves have `⚠️ VERIFY` notes or a dated official-docs check (§8).

---

## 15. Open questions

| # | Question | Owner |
|---|---|---|
| Q1 | Exact Markdown table format the V6 check parses (this doc proposes: stem heading `### <stem-id> · <title>`, first column = backticked leaf slug). Confirm in `docs/07`. | `architect`, `housekeeper` |
| Q2 | ~~Does the author accept the ~5.5-year horizon (§13), or trim stems now?~~ **Resolved 18 Oct 2026:** trim to about 450 leaves (about 3 years), all 16 branches and Foundation kept (17 roadmaps) (§9, §13). | Author |
| Q4 | The new `## Later` section (§2, row 5): confirm the V6 script ignores it (bullets only, no tables, no backticked slugs) and whether `docs/03` or `docs/07` should mention it. | `architect`, `housekeeper` |
| Q3 | ~~`docs/04`, `05`, `06` are not written yet.~~ **Partly resolved 18 Oct 2026:** all three exist. Reading and doing time now follow `docs/04` §6 (§3), and the sim kinds follow `docs/05` §4 (§6). Still to confirm: the challenge codes (`P:`, `F:`, `F(ai):`, `B:`) against `docs/06`. | `curriculum-designer` |
