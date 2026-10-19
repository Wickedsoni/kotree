# 09 — Agent Workflow (Claude Code + subagents)

> **Purpose:** how the main agent and the 10 subagents split the work, which model and effort each one uses, how they run in parallel, and how to keep token use low.
> **Read this when:** starting any task, adding an agent, or tuning cost.
> **Depends on:** `00-decisions.md`.
> **Version:** v1.1 · 18 Oct 2026 · open verification marks checked against the Claude Code docs (code.claude.com), read 18 Oct 2026.

## Changes from verification (18 Oct 2026)

| # | Change | Source |
|---|---|---|
| A1 | Subagents **can** launch subagents in Claude Code: "By default, a subagent can spawn subagents of its own, up to three layers below the main conversation". Kotree keeps "only the main agent launches subagents" as a **project rule** and enforces it: every `.claude/agents/*.md` lists its `tools` explicitly **without `Agent`**. Belt and braces: `CLAUDE_CODE_MAX_SUBAGENT_SPAWN_DEPTH=1` ("Set `1` to turn nesting off") in the `env` block of `.claude/settings.json`. | https://code.claude.com/docs/en/sub-agents |
| A2 | Claude Code's own parallel cap is **20** running subagents per session (`CLAUDE_CODE_MAX_CONCURRENT_SUBAGENTS`, Claude Code v2.1.217+). Kotree's cap of 3 is a usage-budget choice (Pro plan), not a tool limit. To enforce it, set `CLAUDE_CODE_MAX_CONCURRENT_SUBAGENTS=3` in the same `env` block. | same |
| A3 | `effort:` in agent frontmatter **is supported**: "Overrides the session effort level, but not the `CLAUDE_CODE_EFFORT_LEVEL` environment variable". Levels for the 5.5 models: `low`, `medium`, `high`, `xhigh`, `max`. | same; https://code.claude.com/docs/en/model-config |
| A4 | Main-session settings use `"model": "claude-sonnet-5-5"` and `"effortLevel": "medium"`, replacing `"model": "sonnet"`. | https://code.claude.com/docs/en/model-config |
| A5 | Prompt caching is automatic. A subagent has its own cache and gets a **5-minute** TTL even on a subscription, while the main conversation gets 1 hour. CLAUDE.md is read once at session start: edits apply only after `/clear`, `/compact` or a restart. | https://code.claude.com/docs/en/prompt-caching |

---

## 1. Principles

1. **The main agent orchestrates; subagents work; files remember.** The main agent (Sonnet 5.5, medium) never carries long design thinking in its own context. It routes, delegates, merges results and commits.
2. **Right model for the job.** Opus 5.5 = decisions, curriculum, physics design. Sonnet 5.5 = building, writing, reviewing. Haiku 5.5 = mechanical fixes and bookkeeping.
3. **Right effort for the risk.** Raise effort only where a mistake is expensive or hard to see later.
4. **Parallel when independent.** Up to 3 subagents at once (§4); queue the rest.
5. **Everything goes to disk.** Subagents write files and return a summary of 200 words or fewer. Chat is not storage.
6. **Subagents do not launch subagents (project rule).** Claude Code allows nesting by default (A1), but Kotree turns it off. No agent file lists `Agent` in `tools`, and `.claude/settings.json` sets `CLAUDE_CODE_MAX_SUBAGENT_SPAWN_DEPTH=1`. So all coordination lives in the main agent's plan.

---

## 2. The roster (10 subagents)

Definitions live in `.claude/agents/<name>.md`.

| # | Agent | Model | Effort | Owns | Typical output |
|---|---|---|---|---|---|
| 1 | `architect` | Opus 5.5 | high | Technical decisions, ADRs, Kobweb structure, module boundaries, performance budgets | `docs/adr/NNN-title.md`, a short build plan |
| 2 | `curriculum-designer` | Opus 5.5 | high | Roadmaps, prerequisite graphs, leaf outlines, "what is next" | `content/<branch>/_roadmap.md` edits, leaf briefs |
| 3 | `physics-designer` | Opus 5.5 | high | Physics visualization design: bodies, constraints, parameters, concept-to-physics mapping | `docs/sims/<sim-id>.md` (sim spec) |
| 4 | `lesson-writer` | Sonnet 5.5 | high | Lesson Markdown from the author's notes and the leaf brief | `content/<branch>/<stem>/<leaf>.md` |
| 5 | `sim-engineer` | Sonnet 5.5 | high | Implements sims from a spec (Kotlin + JS interop) | `site/.../sims/*` |
| 6 | `ui-engineer` | Sonnet 5.5 | medium | Kobweb pages, tree canvas, camera, components, tokens | `site/.../*` |
| 7 | `challenge-builder` | Sonnet 5.5 | medium | Predict-the-output and Fix-the-bug items with verified answers | `challenge` blocks (`docs/06` §3) in `content/<branch>/<stem>/<leaf>.challenges.md`, pasted into the leaf by `lesson-writer` (one owner per file) |
| 8 | `reviewer` | Sonnet 5.5 | high | Read-only review: facts, template, tokens, accessibility, build | `docs/reviews/<leaf-or-feature>.md` with PASS or FIX list |
| 9 | `fixer` | Haiku 5.5 | low | Mechanical fixes from a FIX list: typos, tokens, links, lint | small edits |
| 10 | `housekeeper` | Haiku 5.5 | low | Status flips, counts, `PROGRESS.md`, removing the `upnext: true` override from a published leaf, link checks, tree index | metadata edits |

Effort levels used: **low** (mechanical), **medium** (routine build), **high** (default where errors cost), **xhigh** and **max** (reserved; none currently assigned). All five are valid `effort:` values in agent frontmatter for the 5.5 models (A3). In `settings.json`, `effortLevel` accepts `low`–`xhigh` but not `max`.

---

## 3. The main agent (Sonnet 5.5, medium)

Does: classify task, read `INDEX.md`, pick pipeline, launch subagents, merge, run build, commit.
Does not: write whole lessons, design simulations, make architecture decisions, review its own output.
Raise its own effort to high only for cross-file debugging that two cheaper attempts failed to solve. Switch the main agent to Opus only for a one-off architecture crisis, then switch back.

---

## 4. Parallelism rules (maximum 3, Claude Pro plan)

1. Launch independent subagents **in one message** so they run together.
2. **Maximum 3** at once (author is on the Claude Pro plan; usage limits are tight, updated 18 Oct 2026). This is Kotree's budget rule. Claude Code itself allows 20 (A2), so `.claude/settings.json` sets `CLAUDE_CODE_MAX_CONCURRENT_SUBAGENTS=3` to enforce it. If the work has 1 or 2 pieces, run them sequentially. Never invent work to fill three.
3. Queue extra work as later waves of 3. More agents means more rate-limit risk and more merge work.
4. **One owner per file.** Two agents never edit the same file in the same wave.
5. Real dependencies run in order: `physics-designer` → `sim-engineer`; `architect` → engineers; `lesson-writer` → `reviewer` → `fixer` → `housekeeper`. Put everything else beside them.

Wave patterns:

| Task | Wave 1 (parallel) | Wave 2 | Wave 3 |
|---|---|---|---|
| Publish a leaf | `lesson-writer`, `physics-designer`, `challenge-builder` | `sim-engineer` (needs spec), `reviewer` on lesson | `fixer`, then `housekeeper` |
| New UI feature | `architect` | `ui-engineer`, `sim-engineer` (separate files), `challenge-builder` if editor-related | `reviewer`, `fixer` |
| Roadmap refresh (one bough) | `curriculum-designer` × up to 3 (queue the fourth as wave 2) | `reviewer` consistency check | `housekeeper` |
| Whole-site audit | `reviewer` × 3 (design, content, accessibility) | `fixer` | `housekeeper` |

---

## 5. Handoff contract

Every subagent ends with at most 200 words in this shape:

```
STATUS: done | partial | blocked
FILES: path1, path2
DECISIONS: one line each
OPEN QUESTIONS: one line each, or none
NEXT: what the main agent should do
```

The main agent reads the files, not long chat output.

---

## 6. Pipelines

### P1 — Publish a leaf (the routine behind 3 videos a week)

0. **Author:** "I understood `<leaf-id>`", plus rough notes or a video outline (optional file `content/<branch>/<stem>/<leaf>.notes.md`).
1. **Main:** read only that leaf's row in the branch roadmap and `docs/04-lesson-system.md`.
2. **Wave 1 (3 agents):**
   - `lesson-writer` (Sonnet, high): lesson draft following `docs/04`.
   - `physics-designer` (Opus, high): sim spec for the leaf, only if the roadmap row names a sim idea.
   - `challenge-builder` (Sonnet, medium): 1 to 3 predict-the-output items (3 options each) and exactly 1 fix-the-bug item (2 hints; none on a `playground: false` leaf), written as `challenge` blocks in `<leaf>.challenges.md`, with expected outputs verified by running Kotlin 2.4.21 (`docs/06` §5).
3. **Wave 2:** `sim-engineer` implements the spec. `lesson-writer` pastes the blocks from `<leaf>.challenges.md` into the leaf's Predict and Fix sections and deletes the draft file. Then `reviewer` checks the lesson draft against official docs and `docs/04`.
4. **Wave 3:** `fixer` applies mechanical items from the FIX list; anything else goes back to its owner. Re-run `reviewer` once. Two failed reviews → main agent escalates the owner's model one step.
5. **`housekeeper`:** `status: live`, dates, branch and stem counts, remove any `upnext: true` override from the published leaf (the build computes up-next), `PROGRESS.md`, link check.
6. **Author:** records the video, pastes the URL; `housekeeper` fills `video:`.
7. **Main:** commit `content: publish <leaf-id>`; push; Vercel deploys.

### P2 — New feature (tree, camera, editor, component)
`architect` writes a one-page plan or ADR → `ui-engineer` and `sim-engineer` build in parallel on separate files → `reviewer` → `fixer` → commit.

### P3 — Bug
Main agent reproduces. One-line cause → `fixer`. Anything bigger → `ui-engineer` or `sim-engineer`. Escalate to `architect` only when the bug is a design flaw.

### P4 — Weekly cadence (3 videos a week)
Sunday: `curriculum-designer` (one call) confirms the next 3 leaves per the roadmap and flags prerequisites you skipped. Mon/Wed/Fri: run P1 for one leaf each. Friday: `housekeeper` weekly summary in `PROGRESS.md`.

### P5 — Monthly roadmap refresh
`curriculum-designer` per branch you worked on (parallel), `reviewer` for consistency, `housekeeper` for the tree index.

---

## 7. Token playbook

1. **Doc routing.** Open `INDEX.md`, then only the doc the task needs. Roadmaps are per branch for the same reason.
2. **Short CLAUDE.md.** It loads on every turn. Keep it under about 60 lines; put detail in docs.
3. **Summaries ≤ 200 words.** Results live in files.
4. **Haiku first for mechanical work.** Typos, renames, token swaps, status flips, link checks.
5. **Opus only where its judgment changes the result:** curriculum order, physics design, architecture. Not for typing code.
6. **Write once, review once.** A precise brief to the writer is cheaper than three revision rounds.
7. **Use `/clear` between unrelated tasks** and `/compact` when a session grows long. Continue from files, not memory.
8. **Don't re-read.** Tell subagents the exact file paths and line ranges.
9. **Limit tool output.** Ask for `grep -n` with a path, builds with warnings only, tests with failures only.
10. **Stable prefix.** Keep CLAUDE.md and agent definitions stable between runs so cached prompt prefixes can be reused. Verified (A5, https://code.claude.com/docs/en/prompt-caching, read 18 Oct 2026):
    - Caching is automatic and matches the exact request prefix.
    - On a subscription within plan usage, the main conversation gets a 1-hour TTL. Subagents get 5 minutes unless `subagentPromptCacheTtl` is set.
    - Switching model mid-session rebuilds the cache. Changing effort on the 5.5 models keeps it.
    - Editing CLAUDE.md mid-session neither breaks the cache nor applies until `/clear`, `/compact` or a restart.
    - `/usage` shows the "Prompt cache (main)" hit ratio.
    - So: pick model and effort at session start, and edit CLAUDE.md between sessions.
11. **Batch the weekly work.** One planning call on Sunday beats three small ones.
12. **Budget guide per leaf:** 1 lesson-writer, 1 challenge-builder, 0 or 1 physics-designer, 0 or 1 sim-engineer, 1 or 2 reviewer, 1 fixer, 1 housekeeper. If a leaf needs more than that, the brief was weak.

---

## 8. Quality gates (Definition of done)

**Lesson (leaf):** follows `docs/04` section order · every Kotlin/Android claim checked against official docs · all code compiles and runs · expected outputs verified by running · sim (if any) meets `docs/05` budgets · tokens only (no hardcoded colours/fonts) · alt text and keyboard path present · `reviewer` PASS · `housekeeper` done.

**Feature:** builds without warnings · matches `docs/02` and `docs/03` · 60 fps target on the author's laptop for the tree (see `docs/07`) · `reviewer` PASS · commit message follows convention.

---

## 9. Setup in IntelliJ IDEA

Source: https://code.claude.com/docs/en/jetbrains and https://code.claude.com/docs/en/model-config (read 18 Oct 2026).

1. Install the Claude Code **CLI** first, following the quickstart (https://code.claude.com/docs/en/quickstart). The plugin "does not bundle its own copy of the CLI" and shows "Cannot launch Claude Code" when `claude` is not on PATH. Check with `claude --version`.
2. Install the **Claude Code [Beta]** plugin from the JetBrains Marketplace (https://plugins.jetbrains.com/plugin/27310-claude-code-beta-) and restart IntelliJ IDEA (a supported IDE). If the IDE cannot find `claude`, set the full path under Settings → Tools → Claude Code [Beta] → Claude command.
3. Open the repo root (the folder that contains `CLAUDE.md`) and run `claude` in the IDE's integrated terminal (or press `Ctrl+Esc`). From an external terminal, `/ide` connects it. The Pro subscription works; no API key is needed.
4. Model and effort: `/model claude-sonnet-5-5`, then `/effort medium`. `/effort` with no argument opens a slider, and `/model` also has an effort slider (left/right arrows). One session only: `claude --model claude-sonnet-5-5 --effort medium`.
5. Run `/agents` and confirm the 10 agents from `.claude/agents/` appear with the right model.
6. Commit `.claude/settings.json` (project settings) with:
   ```json
   {
     "model": "claude-sonnet-5-5",
     "effortLevel": "medium",
     "env": {
       "CLAUDE_CODE_MAX_CONCURRENT_SUBAGENTS": "3",
       "CLAUDE_CODE_MAX_SUBAGENT_SPAWN_DEPTH": "1"
     }
   }
   ```
   `model` and `effortLevel` are documented settings keys, and both variables are documented on the subagents page. `CLAUDE_CODE_MAX_CONCURRENT_SUBAGENTS` needs Claude Code v2.1.217+. ⚠️ VERIFY (Phase 0): the docs read do not show that an `env` block in **project** `.claude/settings.json` reaches these two variables. Check: with the file in place, ask the main agent to launch 4 trivial subagents in one message. The fourth must fail with `Concurrent subagent limit reached`. If it does not, set both variables in the shell profile instead.
7. Commit `.claude/agents/` to the repo so the setup travels with the project.

---

## 10. Escalation and de-escalation

| Situation | Action |
|---|---|
| Haiku fix didn't work | Retry once with a sharper brief; then Sonnet |
| Sonnet output fails review twice | Raise that agent's effort one level, once; then ask `architect` or `curriculum-designer` to rewrite the brief |
| Opus agent finishes quickly with clean output | Drop its effort one level next time |
| Physics design feels flat | Keep `max` for that agent only; do not raise others |
| Cost spike | Check for re-reading large files, unbounded tool output, or too many parallel agents |

---

## 11. Caveats

- **Effort key.** `effort:` is a documented frontmatter field. Values are `low`, `medium`, `high`, `xhigh`, `max`, and "available levels depend on the model". It overrides the session effort but not the `CLAUDE_CODE_EFFORT_LEVEL` environment variable, so never set that variable, or it flattens every agent to one level. Claude Code "ignores a field it doesn't recognize without reporting an error", so a typo such as `efort:` fails silently; `reviewer` checks the spelling in agent files. Source: https://code.claude.com/docs/en/sub-agents (read 18 Oct 2026).
- **Model names.** Agents pin the exact IDs `claude-opus-5-5`, `claude-sonnet-5-5`, `claude-haiku-5-5`. All three appear in the Claude Code docs (model-config, prompt-caching; read 18 Oct 2026), and frontmatter `model` accepts "a full model ID". Do not use the short aliases `opus`, `sonnet`, `haiku`. On the Anthropic API they resolve to the 5.5 models today, but an alias follows new releases, and in one test the `opus` alias resolved to a model ID that did not exist and the run failed.
- **Parallel limit.** By default Claude Code allows 20 running subagents per session. Spawning another "fails with `Concurrent subagent limit reached`, and the error tells Claude not to retry" (it is not queued). Kotree sets the cap to 3 (§4, §9 step 6), so the main agent queues later waves itself. Source: https://code.claude.com/docs/en/sub-agents (read 18 Oct 2026).
- **Nesting.** Claude Code allows subagents to spawn subagents up to three layers deep by default. Kotree disables this (§1 rule 6).
- **Reviewer independence.** `reviewer` must not be the agent that wrote the thing it reviews. The main agent enforces this.
