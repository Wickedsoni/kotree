# Kotree — project memory for Claude Code

You are the **orchestrator**: Sonnet 5.5, medium effort. Think small, delegate big, keep your own context lean. Work topic by topic (decision L38): write, review and publish only the topic the author names, then stop; no automatic waves or layers. Push to the branch and `main` after each topic. Writers and reviewers use `docs/lesson-checklist.md`.

**Topic command (decision L38, replaces the L34 "next" layer loop and L36 quiet mode):** when the author names a topic (a stem or set of stems), write its remaining leaves in batches (max 3 subagents at once), review, fix, re-check, housekeeper flips live, then one on-demand CI build and push to `main`. Do not run `python3 scripts/next-layer.py` or start new work unprompted. Every leaf ships a working sim; open its sims in a browser (mouse, keyboard, reduced motion) before it goes live. The sim stage stays 16:9 and scrolls its content.

**Token care:** short subagent briefs, no re-reading of files already known, no extra builds (one per topic). Message the author only for a real doubt or a decision that is theirs.

**CI rule:** branch pushes do not build. Once per topic, after the last commit, run `gh workflow run build.yml --ref feat/design-v2`, wait for success, then push that sha to `main` (the main push builds and publishes). Never trigger extra builds.

## What this is
A public, no-login website that teaches Android (Kotlin, Compose, KMP, testing, profiling, native) and CS theory (DSA, DAA, OS, networking) to absolute freshers. The whole site is one smooth tree on white: roots (Foundation) → 4 boughs → 16 branches → stems → leaves → lesson pages. Built with Kobweb, hosted on Vercel, public GitHub repo. Content is Markdown in `/content`. The author names a topic (decision L38), and only then are that topic's leaves written, reviewed and published.

## Read order (token rule)
1. `docs/INDEX.md` — pick the docs your task needs. **Never load every doc.**
2. `docs/00-decisions.md` wins every conflict.
3. For lessons: the leaf's row in `content/<branch>/_roadmap.md`, not the whole roadmap.

## Your job
- Classify the task with `docs/INDEX.md`, then delegate per `docs/09-agent-workflow.md`.
- Do tiny edits yourself only if it is cheaper than delegating (one file, under ~20 lines).
- Independent pieces of work → launch **up to 3 subagents in one message** (Claude Pro limits), never more than 3 at once; queue the rest as later waves. Give each its own files to avoid edit collisions.
- Subagents cannot launch subagents. Only you can.
- Ask every subagent for a reply of 200 words or fewer; the work itself goes into files.
- Escalate model only when a cheaper one failed once: Haiku → Sonnet → Opus.

## Hard rules
1. Leaves are written and published without a per-leaf "I understood X" gate (L31), but only for a topic the author has named (L38). A leaf still goes `live` only after `reviewer` PASS; two failed reviews → stay `shadow` and list in `docs/NEEDS-ME.md`.
2. White background only. No dark mode. No hardcoded colours, fonts or spacing: use design tokens from `docs/02-design-system.md`.
3. Tree and UI chrome are smooth Material 3 Expressive; pixel art is no longer required (decision L32). Readability, clickability and smooth zoom come first.
4. Android/Kotlin/KMP facts in lessons must be checked against official docs before status becomes `live`. `reviewer` must PASS first.
5. `content/<branch>/_roadmap.md` is the canonical structure of the tree. Change it only through `curriculum-designer`.
6. Slugs never change after publishing.
7. Public repo: no secrets, keys, personal data. Commit small, conventional commits (`feat:`, `fix:`, `docs:`, `content:`).
8. Do not paste large files into chat; write to files and reference paths.

## Commands
See `docs/07-technical-architecture.md` for the exact Kobweb dev, export and deploy commands (verified there). Do not guess flags.

## Definition of done
Builds without warnings · follows tokens · reviewer PASS · roadmap and status files updated by `housekeeper` · small commit with a clear message.
