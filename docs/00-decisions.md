# 00 — Decisions (Single Source of Truth)

> **Purpose:** every locked decision, every assumed default, the vocabulary, IDs, schemas and repo layout of the project.
> **Read this first.** If any other doc contradicts this one, this doc wins.
> **Project:** Kotree. Domain: `garvitmaheshwari.in` (owned at Hostinger). Public URL: `https://kotree.garvitmaheshwari.in` (a subdomain, see L25; changed 18 Oct 2026 from the `/kotree` sub-path).
> **Version:** v1 · written 17 Oct 2026.

---

## 1. What we are building

A public, no-login learning website that teaches Android development (Kotlin, Compose, KMP, testing, profiling, native) and the computer-science theory behind it (DSA, DAA, OS, networking) to **absolute freshers**. The whole site is **one pixel-art tree on a white page**. The visitor zooms through it like Google Maps: roots (Foundation) → boughs → branches → stems → leaves → lesson page. Each lesson explains a concept with real-world examples, physics-based animations, an in-browser editor, "predict the output" and "fix the bug" challenges.

The author documents his own learning on YouTube (3 videos/week). A leaf is **published only after the author has understood the topic** and says so. Until then the tree shows light-gray "shadow" branches with "coming soon". Anyone who finishes the whole tree should be able to work as a one-person Android + KMP team, and to read and understand AI-generated Kotlin code.

---

## 2. Locked decisions (stated by the author)

| # | Decision |
|---|---|
| L1 | Audience = absolute freshers. **No difficulty levels for learners.** Teach through questions, "how it works", and logic building. |
| L2 | No login, no accounts, no gating, no newsletter, no sign-up, no comments. Everything is open. |
| L3 | Progress tracking is for the author (what is done, how much). |
| L4 | Day one: all boughs, all branches and the Foundation are visible. Nothing is published. Unpublished parts are light-gray "shadow" branches labelled "coming soon", with a hint of which topic is coming. |
| L5 | A leaf page is created only after the author says "I understood X". Then it goes live and that part of the tree lights up. Cadence: 3 videos per week. |
| L6 | Navigation: one standing tree on a white background; Google-Maps-style **semantic zoom** (more detail and more spacing as you zoom). Foundation (roots) → branches → stems → leaves. Clicking a branch zooms the camera into it and shows its stems; clicking a leaf shows the topic name, then opens the lesson page. A "Start here" path begins at the Foundation. |
| L7 | Look: **pixel art**. Black bark. Not-yet-covered areas light gray. Published leaves light green; **hard topics dark green**. Visible space between leaves so they are easy to click. References: a bare black oak silhouette on a dark ground, and a white-blossom tree alone in a misty field. |
| L8 | Desktop-first. Mobile is not a priority now. |
| L9 | One strong theme. White background only. **No dark mode.** |
| L10 | One display/UI font and one code font, taken from Material 3's recommendations, easy to read. |
| L11 | Material 3 Expressive + colour psychology. It must **not** look like generic AI-generated design. |
| L12 | Section accent colours follow the mapping in §6. |
| L13 | Lessons are detailed: concept, logic building, tricks to learn and remember it, examples. |
| L14 | A real physics engine drives animations. Which physics goes where is decided per topic, guided by `docs/05`. |
| L15 | The editor runs real Kotlin (Kotlin Playground) and includes **Fix the bug** and **Predict the output**. **There is no Android emulator and no Android simulator in the browser** (amended 18 Oct 2026: too heavy). Android behaviour is shown through reference videos, animations and sims that show what changes on screen when the code changes. |
| L16 | Capstones: the author builds his own and shows it. Learners receive constraints and guidelines only. |
| L17 | Content is Markdown inside the repo, so the author and an AI agent can see the next topic of each branch. |
| L18 | Stack: Kobweb, public GitHub repo, Vercel, domain on Hostinger. |
| L19 | Plain English only. |
| L20 | Personalised user journeys: **not in v1** (future). |
| L21 | 3D components are allowed where they help (inside visualizations). |
| L22 | Name: Kotree (confirmed). |
| L23 | Development: Claude Code in IntelliJ IDEA; main agent = Sonnet 5.5, medium effort; subagents per `docs/09`. |
| L24 | Success = a learner who completes the tree can be "a team in one person" for Android including KMP. |
| L25 | The site is served at `https://kotree.garvitmaheshwari.in` (subdomain of the author's domain; amended 18 Oct 2026, replacing the `/kotree` sub-path). The site root is `/`: **no base path**. The root site `garvitmaheshwari.in` on Hostinger is not touched; only a DNS record for the `kotree` subdomain is added. |
| L26 | The lesson page has **no Capstone-link section**. Video is optional and added later (amended 18 Oct 2026): while a leaf's `video` field is empty, no video button or section is shown anywhere; once the author adds the YouTube URL, the "Watch the video" button (leaf popup and lesson header) and a video section become visible. A leaf can be live without a video. Capstones still exist as fruits on the tree (L16, D12). |
| L27 | Kotree is the author's **personal tree of growth**: a public record of his own learning journey, shared with the world; anyone else may use it to learn the same way. It is not a business. Use **free tiers** of every service (Vercel Hobby, GitHub free, Kotlin Playground); upgrade to paid plans only if usage requires it later (decided 18 Oct 2026). | Keeps cost at zero; fits the non-commercial limit of Vercel Hobby while the site earns nothing. |
| L28 | Tree-first prototype (author, 20 Oct 2026): Phase 0 (scaffold) and Phase 2 (pixel tree) are merged into one early milestone that shows the tree with sample data. Phase 1 tokens are applied after it, not before; the prototype keeps all values in one token file so they can be swapped (hard rule 2 still holds). Phases 1 and 3 follow. |
| L29 | Lesson voice (author, 20 Oct 2026): every lesson is industry-accurate (facts checked against official docs, hard rule 4) AND easy to say aloud, because the author explains it to himself and to a YouTube audience. Plain words, one everyday analogy with where it stops (`docs/04` §3.2), one idea per sentence, no unexplained jargon. |
| L30 | Bundle budget (author, 21 Oct 2026): main `kotree.js` budget is **600 KB gzip** (was 400 KB). Not a hard performance law: revisit if first load is slow on a phone. Supersedes the 400 KB figure in `docs/07` §8, ADR 003 and ADR 005. |
| L31 | Autonomous publishing (author, 21 Oct 2026): overrides hard rule 1 and the "I understood X" gate. The whole tree is built and published without per-topic approval. Facts are still checked against official docs (hard rule 4) and `reviewer` must PASS before a leaf goes `live`. A leaf that fails review twice stays `shadow` and is listed in `docs/NEEDS-ME.md`. Mac-needed leaves publish like any other; iOS-only claims are labelled "From the docs, not run by me" (docs/08 §8.1 item 5 dropped). Work runs in waves of about 8 leaves, one wave per session. |
| L32 | Design reset (author, 21 Oct 2026, from live-site screenshots): the tree is **no longer required to be pixel art**. It must be readable first: leaves, stems and branches clearly distinguishable, every stem and leaf clickable, smooth zoom and pan. The tree may be drawn with smooth vector shapes in Material 3 Expressive style (shapes, colour roles, motion). Supersedes the pixel-world part of hard rule 3, L28's pixel look, and the white-only rule for status colours (status needs distinct token colours; page background stays white, hard rule 2 otherwise stands). `/topics` becomes a bento grid of cards (topic cards holding their sub-topics). Breadcrumbs use real names (Kotree / Branch / Topic / Leaf, no duplicate words). Wide screens use the side space (lesson column plus a side rail). Design fixes come before new waves. |
| L33 | Continuous build (author, 21 Oct 2026): sessions keep running waves without waiting for input, until the whole tree is live. Waves are about 15 leaves. Push to the working branch and `main` after every wave and after every step. Overrides the earlier "stop after every wave" rule. Main goal: the author becomes expert in Android, Kotlin and KMP from intern to senior interview level, by understanding rather than memorising, so every leaf must teach the reason, the trade-off and how to say it aloud. DSA, DAA and OS leaves end with a "Practise on these" list of real LeetCode and HackerRank problems with numbers. At the end of a large block of work an Opus reviewer reviews it and its suggestions are applied. More leaves will be added later. |
| L34 | Radial build (author, 21 Oct 2026): from wave 11 on, build layer by layer across ALL branches, never one branch to the end. A layer is the next leaf of every branch (first leaf in roadmap order that is not live and whose `requires` are all live; a branch with nothing eligible sits out). `scripts/next-layer.py [N]` prints the layer. When the author says "next", the orchestrator builds the next layer: same pipeline (writers, 2 reviewers, fixer, re-check, housekeeper), one on-demand CI build at the end, then push the tested sha to `main`. Waves 1-10 were Foundation plus the Kotlin branch; Foundation is complete (34 of 34 live). The author lists topics per branch and may reorder; the roadmap rows stay canonical (curriculum-designer edits them). |
| L35 | Tree backdrop (author, 21 Oct 2026, ADR 008): the **tree canvas on `/` only** may draw a soft sky/ground backdrop (sky gradient, light clouds, distant hills, ground, subtle grain), all from tokens in `Tokens.kt`. Every tree ink must keep ≥ 3:1 against every backdrop colour (`scripts/check-tokens.py`). The page `body`, `/topics`, lesson pages and all UI chrome stay white. There is still no dark mode. Relaxes hard rule 2 and L9 for the tree canvas only. |

---

## 3. Defaults (all confirmed by the author on 17 Oct 2026; amendments are in §3.1 — edit a row to change it)

| # | Default | Why |
|---|---|---|
| D1 | The tree is **generated procedurally** (seeded, deterministic) and drawn as pixels on a canvas. Branch anchor points come from data, so the tree is stable between builds and new growth appears where content is published. | Static sprite can't grow. |
| D2 | Pixel-art tree is the **backdrop**. Stems and leaves are **sharp interactive elements placed on top** in world coordinates. | Crisp click targets at every zoom. |
| D3 | From day one every **stem name is visible in light gray**. Leaves are hidden until published. Each branch has exactly one glowing **up-next** leaf. | Shows the journey without spoilers. |
| D4 | **4 boughs, 16 branches** plus Foundation (see §5). | Avoid a 16-branch crowd at the top level. |
| D5 | The tree stays organic: black bark, gray shadow, green leaves. The **section accent colour** appears in panels, buttons, and lesson pages once the camera enters a branch. | Keeps one theme while colours vary by section (L12). |
| D6 | The **world is pixelated**; the **UI chrome** (panels, buttons, editor) is smooth Material 3 Expressive. | Pixel UI would clash with M3E shapes. |
| D7 | Lesson page structure = `docs/04`. | |
| D8 | Real Kotlin runs through the **Kotlin Playground** (needs internet). Android-specific ideas are shown with reference videos, animations and sims, **not simulators** (L15 amended 18 Oct 2026). | Running Android in a browser is not realistic. |
| D9 | Predict-the-output: multiple choice + "Run it to check". Fix-the-bug: learner edits, code runs, output compared with expected output. | |
| D10 | Visitor checkmarks (leaves completed) are saved in the browser's local storage. No accounts. | |
| D11 | A public progress counter ("N of M leaves grown") and a **Watch the video** button on every leaf that has a video (L26). | |
| D12 | Capstones appear as **fruits** at the tips of branches. | |
| D13 | Display + UI font: **Roboto Flex** (variable). Code font: **JetBrains Mono**. Verified in `docs/02` §3 on 18 Oct 2026 (both OFL 1.1; Roboto Flex is the M3E default type scale). | M3's own typeface family; easy to read. |
| D14 | Tagline (working): "Grow your Android knowledge, one leaf at a time." | |

### 3.1 Amendments from the author's answers (17 Oct 2026)

| Default | Answer | Effect |
|---|---|---|
| D1 | Confirmed: the tree is drawn by an algorithm (procedural). | As written. |
| D2 | Confirmed. | As written. |
| D3 | Confirmed: stem names visible in gray from day one, leaves hidden until published, one glowing up-next leaf per branch. | As written. |
| D4 | Amended: **boughs are shown grouped first and split into their branches progressively as the camera zooms in.** | `docs/03` must define the grouped-to-split zoom behaviour. |
| D5 | Delegated to Claude ("whatever feels good by colour psychology"). | Keep D5; `docs/02` justifies the choice. |
| D6 | Confirmed: only the tree is pixelated; all other UI is sharp (smooth Material 3 Expressive). | As written. |
| D7 | Amended: the lesson template has **no Capstone-link section**, and its **Video section is optional and appears only when `video` is set** (L26). | `docs/04` and `content/_templates/leaf.md` follow this. |
| D8, D9 | Confirmed. | As written. |
| D10 | Confirmed: visitor checkmarks in the browser's local storage. | As written. |
| D11 | Confirmed: public progress counter and a Watch-the-video button (button in popup and lesson header only, see L26). | As written. |
| D12 | Confirmed: capstones appear as fruits. | As written. |
| D13 | Domain: `garvitmaheshwari.in`. | See L25 and §11. |
| D14 | Name Kotree confirmed; URL `kotree.garvitmaheshwari.in`. | See L25 and §11. |
| D15 | Up-next leaf: computed by the build as the first non-live leaf in roadmap order; an optional `upnext: true` in a leaf's frontmatter overrides it (18 Oct 2026). | `docs/03` and §8.2 follow this. |
| D16 | The list of live leaves is the leaves' own `status: live` frontmatter. No separate published-IDs file (18 Oct 2026). | One source of truth. |
| D17 | Hosting: Vercel (L18) with the `kotree` subdomain pointed at it by a DNS CNAME in Hostinger. GitHub Actions builds the site and the Vercel CLI (`--prebuilt`) deploys it (`docs/07` §5.1; checked 18 Oct 2026, with the open ⚠️ VERIFY items listed there). | Replaces the Hostinger-static plan, which was only needed for the sub-path. |

---

## 4. Vocabulary

| Term | Meaning |
|---|---|
| **Tree** | The whole site. |
| **Foundation (roots)** | Pre-requisite ideas for an absolute fresher (how computers run code, programming thinking). The "Start here" path begins here. |
| **Bough** | One of 4 major limbs that group branches (D4). |
| **Branch** | One section/subject (e.g. `kotlin`, `dsa`). 16 branches. |
| **Stem** | A topic group inside a branch (e.g. `null-safety`). |
| **Leaf** | One lesson (one page, usually one video). |
| **Fruit** | A capstone project attached to a branch (D12). |
| **Shadow** | Planned but not published (light gray). |
| **Up next** | The next leaf the author will cover in a branch (gray, glowing). |
| **Live** | Published. |
| **Sim** | A physics-based interactive visualization on a lesson page. |

---

## 5. Structure: boughs and branches

Slugs are lowercase kebab-case and never change after publishing (they are URLs).

| Bough (slug) | Branch slug | Branch name |
|---|---|---|
| `roots` | `foundation` | Foundation |
| `kotlin-android` | `kotlin` | Kotlin language |
| `kotlin-android` | `android-core` | Android core and lifecycles |
| `kotlin-android` | `ui` | UI (Compose and Views) |
| `kotlin-android` | `concurrency` | Coroutines, Flow, threads |
| `architecture-kmp` | `architecture-data` | Architecture and data |
| `architecture-kmp` | `app-networking` | Networking inside apps |
| `architecture-kmp` | `kmp` | Kotlin Multiplatform |
| `cs-theory` | `dsa` | Data structures and algorithms |
| `cs-theory` | `daa` | Design and analysis of algorithms |
| `cs-theory` | `os` | Operating systems |
| `cs-theory` | `networking` | Computer networking |
| `craft` | `testing` | Testing |
| `craft` | `profiling` | Profiling and optimisation |
| `craft` | `native-ndk` | Native layer (NDK, JNI, ART) |
| `craft` | `security` | Security |
| `craft` | `tools` | Tools and ecosystem |

Foundation covers: how a computer runs code (bits, CPU, memory, compilers vs interpreters, JVM/ART at overview level), programming thinking (variables, conditions, loops, functions, data, debugging), how an Android app comes to exist (build → APK/AAB → install → run).

---

## 6. Section colour mapping (colour psychology)

Author-accepted: **blue** → Kotlin / Android core (trust, focus) · **green** → architecture / KMP (growth, structure) · **orange** → DSA (energy, problem solving) · **purple** → DAA (abstraction, depth) · **teal** → networking (connection, flow).
Remaining branches get a hue assigned in `docs/02` with this rule: every branch hue must be distinguishable at a glance, and the psychology rationale must be written down per branch. The tree itself stays organic (D5).

---

## 7. Leaf states and difficulty

- `status`: `shadow` | `upnext` | `live`.
- `difficulty` (author's rating of how hard the *topic* is, not a learner level): `easy` | `medium` | `hard`.
- Visual mapping: shadow/upnext = light gray (upnext glows); live easy = light green; live medium = mid green; live hard = dark green.

---

## 8. IDs and repo layout

IDs: `<branch>/<stem>/<leaf>` (e.g. `kotlin/null-safety/what-is-null`). Stem ID = `<branch>/<stem>`.

```
/CLAUDE.md                          orchestrator memory (Sonnet main agent)
/.claude/agents/*.md                10 subagents
/docs/                              specs 00-10 + INDEX.md
/docs/reviews/                      reviewer reports (PASS / FIX lists)
/docs/sims/<sim-id>.md              sim specs
/docs/adr/                          architecture decision records
/content/<branch>/_roadmap.md       planned tree for the branch (YAML block + tables) — canonical structure
/content/<branch>/_branch.md        branch metadata (written when the branch gets its first leaf)
/content/<branch>/<stem>/_stem.md   stem metadata
/content/<branch>/<stem>/<leaf>.md  published lesson (frontmatter + body)
/content/_templates/leaf.md         lesson skeleton
/site/                              Kobweb project (created in Phase 0)
```

### 8.1 Roadmap YAML block (first block of each `_roadmap.md`)

```yaml
branch: kotlin
bough: kotlin-android
stems:
  - id: null-safety
    title: Null Safety
    why: One sentence — why a fresher needs this stem.
    leaves:
      - id: what-is-null
        title: What is null and why does it crash apps?
        difficulty: easy          # easy | medium | hard
        requires: []              # full leaf IDs
        fruit: false              # true = capstone (fruit) attached here
```

Rules for the YAML block:
- Stems and leaves are listed in **recommended learning order**. Leaf `id` and stem `id` are kebab-case slugs, unique within the branch.
- `requires` lists leaf IDs from the **same branch only**, each appearing **earlier** in the file. In the YAML block, write them as full IDs (`<branch>/<stem>/<leaf>`).
- Cross-branch assumptions are written in words in the roadmap's "Branch prerequisites" section (for example "Assumes Foundation: variables-and-types"). Foundation is recommended before every other branch ("Start here").
- The tables below the YAML block must cover **exactly** the same leaves, in the same order.

### 8.2 Leaf frontmatter

```yaml
---
id: kotlin/null-safety/what-is-null
title: What is null and why does it crash apps?
status: live                # shadow | upnext | live
difficulty: easy            # easy | medium | hard
order: 1
requires: []
summary: One sentence for the leaf popup.
video: ""                   # YouTube URL once uploaded
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: []                    # simulation IDs used on the page (docs/05)
playground: true            # page contains runnable Kotlin
fruit: false
upnext: false               # optional; true forces this leaf to be the branch's up-next (D15)
---
```

---

## 9. Out of scope for v1

Accounts, personalised paths, comments, newsletter, mobile layout polish, dark mode, multiple languages, analytics beyond what Vercel provides, running real Android code in the browser.

---

## 10. Authoring rules for spec documents (for every agent that writes docs)

1. The readers are **the author and AI coding agents**. Be concrete: decisions, numbers, names, examples. No marketing language.
2. Start every doc with: *Purpose · Read this when · Depends on*.
3. Prefer tables, checklists, code blocks, and worked examples. Give **acceptance criteria** wherever a feature is described.
4. Choose one option and state it. List rejected alternatives in one line each, with the reason.
5. Verify technical facts (Kobweb, Kotlin Playground, Matter.js, Rapier, Material 3 Expressive, Vercel) with web search before asserting them. Mark anything unverified with `⚠️ VERIFY`. Never invent API names.
6. Respect §2 (locked) always, and §3 (defaults) unless you document a strong reason to differ — then flag it at the top as `⚠️ CONFLICT WITH DEFAULT Dn`.
7. Write long files in several stages (create, then extend) so output is never truncated.
8. Finish your run with a reply of **120 words or fewer**: file path, word count, count of `⚠️ VERIFY` items, any contradiction with this doc.

---

## 11. Open items

| # | Item | Why it matters |
|---|---|---|
| O1 | ~~How is `/kotree` served?~~ **Resolved 18 Oct 2026:** subdomain `kotree.garvitmaheshwari.in` (L25), hosted on Vercel (D17) via a DNS CNAME at Hostinger. DNS record and free-tier limits are recorded in `docs/07` §5; the Vercel free tier is non-commercial only, so revisit if the site earns money. | Deploy plan in `docs/07` and Phase 7. |
| O2 | ~~Base path support in Kobweb.~~ **Dropped 18 Oct 2026:** the site root is `/`, so no base path is needed. `docs/02`, `03`, `07` must remove `/kotree` and `BasePath` rules. | Every route and asset URL. |
