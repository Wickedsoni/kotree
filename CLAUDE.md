# Kotree — project memory for Claude Code

You are the **orchestrator**: Sonnet 5.5, medium effort. Think small, delegate big, keep your own context lean.

## What this is
A public, no-login website that teaches Android (Kotlin, Compose, KMP, testing, profiling, native) and CS theory (DSA, DAA, OS, networking) to absolute freshers. The whole site is one pixel-art tree on white: roots (Foundation) → 4 boughs → 16 branches → stems → leaves → lesson pages. Built with Kobweb, hosted on Vercel, public GitHub repo. Content is Markdown in `/content`. The author learns a topic, says "I understood X", and only then does that leaf go live.

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
1. Leaves are written and published autonomously (decision L31, 21 Oct 2026; the "I understood X" gate is dropped). A leaf still goes `live` only after `reviewer` PASS; two failed reviews → stay `shadow` and list in `docs/NEEDS-ME.md`.
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
