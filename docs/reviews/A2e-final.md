# A2e final consistency read

Reviewer: independent. Date: 2026-10-18. Scope: contradictions and stale statements after the 18 Oct 2026 amendments. `docs/00` wins. No doc was edited. VERIFY items were not settled.

VERDICT: FIX (no blocking contradiction; 1 high, 5 medium, 8 low)

## Checked and consistent (no action)

- Subdomain root, no `/kotree` or `BasePath`: only deny-statements remain (00 L25/O1/O2, 03 §7, 07 §4). Agents and template are clean.
- "emulator/simulator": every hit is a denial (00 L15/D8, 02, 04, 05, 06, 08, 10, template). None in agent files or CLAUDE.md.
- Kotlin 2.4.21: 04, 06, 07, 09, 10 and template agree (`kotlin-2.4.21`, `LESSON_KOTLIN_VERSION`).
- `TreeData.kt` + `/tree.json` (app never fetches it): 03, 04 O7, 07, 10 agree.
- D15/D16 in 03, 04, 07 and template agree (computed up-next, `upnext: true` override, live list = frontmatter).
- Counts: roadmaps recount to 482 leaves and 20 fruits (grep of all 17 YAML blocks); 08 §9, §13 and F6 agree. 00, 01 and 10 state no counts.
- Zoom names Canopy/Bough/Branch/Stem/Leaf: 01, 02, 03, 07 agree.
- Local-storage keys `kotree.v1.done`, `kotree.v1.camera` (sessionStorage), `kotree.v1.challenge.<leafId>.<challengeId>`: no clash. "I finished this" wording is the same in 03, 04 and template.
- Token names used in 03, 05, 06 and 07 all exist in `docs/02`.
- Challenge block format: no `<!-- CHALLENGE -->` marker is left in any live instruction (mentions are denials). Template, 04, 06, 07, 09 agree on Predict 1 to 3 and Fix exactly 1 with 2 hints.
- Agent model IDs match `docs/09` §11 and every effort level matches the §2 roster except `physics-designer` (item 7).

## FIX list

1. HIGH. `docs/09-agent-workflow.md:14` (principle 4): "At least 3 subagents ... never more than 10 at once, typically 3 to 5". Contradicts `CLAUDE.md:16` and `docs/09` §4 (maximum 3). Fix: replace with "Up to 3 subagents at once (§4); queue the rest." Mechanical.
2. MEDIUM. `docs/09-agent-workflow.md:63` (wave table, "Roadmap refresh" row): "`curriculum-designer` x up to 4". Exceeds the max of 3. Fix: "x up to 3 (queue the fourth as wave 2)". Mechanical.
3. MEDIUM. `docs/09-agent-workflow.md:52` (§4 rule 2): "Overrides any older 'minimum 3 / maximum 10' text, including CLAUDE.md". CLAUDE.md is already corrected, so the statement is stale. Fix: delete the clause. Mechanical.
4. MEDIUM. `.claude/agents/housekeeper.md:3` and `:13`, `docs/09:35` and `:96`: housekeeper "sets the next leaf to `upnext`" and "marks the next leaf as `upnext` (exactly one per branch)". Contradicts D15/D16 and `docs/03` §3.1: the build computes up-next; `upnext: true` is only an optional override that must be removed from a leaf in the commit that publishes it (03 line 159). Fix: housekeeper removes `upnext: true` from the published leaf and never marks the next leaf. Change "up-next marker" in `docs/09:35` and "next leaf becomes `upnext`" in `docs/09:96` to match. Mechanical, but the owner (`architect`) should confirm the wording.
5. MEDIUM. `.claude/agents/challenge-builder.md:13-14`: "Predict-the-output: 2 to 3 items", "Fix-the-bug: 1 to 2 items", "hint ladder (3 hints ...)", and `:16` "`⚠️ UNVERIFIED`". `docs/06` §3.2/3.3, `docs/09:93` and the template say Predict 1 to 3, Fix exactly 1 with exactly 2 hints (none on a `playground: false` leaf), and `verified: UNVERIFIED: <reason>`. The agent also does not name Kotlin 2.4.21, the `challenge` block as the output, or the `<leaf>.challenges.md` draft file. Fix: rewrite those lines from `docs/06`. Mechanical.
6. MEDIUM. `docs/07-technical-architecture.md:24`: "offered `2.4.21` but not `2.4.21`". Self-contradiction. `docs/06:198` and `docs/07:406` say 2.4.20 was missing. Fix: "offered `2.4.21` but not `2.4.20`". Do not close the ⚠️ VERIFY. Mechanical.
7. LOW. `.claude/agents/physics-designer.md:5` has `effort: high`, while `docs/09:28` and §2 say `max`. Fix: set `effort: max`, or change the doc. Needs the owner (the author's choice, and the available effort values are unverified).
8. LOW. Review report location. `reviewer.md:21`, `housekeeper.md:12`, `docs/09:33` and `docs/10:112` say `reviews/<name>.md` at the repo root, which does not exist. The real reports are in `docs/reviews/`, and `docs/INDEX.md` does not list that folder. Fix: pick `docs/reviews/` and change all four, and add it, `docs/sims/` and `docs/adr/` to INDEX and to `docs/00` §8. Mechanical once the owner picks the folder.
9. LOW. `docs/INDEX.md`: no row for `docs/reviews/`, `docs/sims/<id>.md`, `docs/adr/` or `content/_templates/leaf.md` (see item 8). Fix: add one row each. Mechanical.
10. LOW. `docs/01-vision-and-principles.md:196` (P8 "Do"): "Publish 'What is null?' on its own, finished, with its video." Conflicts with L26 and Rule V1 at `docs/01:250`. Also `:383` ("I add a video for it too") reads as a promise. Fix: "...finished; the video follows when ready." Mechanical.
11. LOW. `docs/00-decisions.md:65` (D11): "Watch the video button on every leaf" is contradicted by L26 and the §3.1 row. Fix: "on every leaf that has a video". Also D17 (line 89, "Wait for the final check in `docs/07`") and O1 ("Remaining check") are stale, since `docs/07` §5.6 recorded the check on 18 Oct. Fix: close them and add "GitHub Actions builds, Vercel CLI `--prebuilt` deploys (`docs/07` §5.1)" to D17. Needs the owner (`docs/00` is the author's file).
12. LOW. `docs/04-lesson-system.md:713` (C1): says `docs/01` §5.2 and §5.3 are "older than L26" and should be corrected. They are already corrected (`docs/01:235-236`, `:250-260`). Fix: mark C1 resolved. Mechanical.
13. LOW. `docs/07-technical-architecture.md:6`: header reads "Version: v2.1" but the same line describes v2.2. Fix: "v2.2". `docs/09:98` ("Vercel deploys") and `docs/10:98` ("every push to `main` redeploys") omit that the deploy is by GitHub Actions + Vercel CLI; optional wording fix. Mechanical.
14. LOW. `.claude/agents/lesson-writer.md:17` says to leave `challenge` blocks "where a challenge goes". `docs/09:32` and `:94` say `challenge-builder` writes `<leaf>.challenges.md` and `lesson-writer` pastes the blocks in and deletes the draft. Fix: add that step. Mechanical.

## Not changed (as instructed)

All ⚠️ VERIFY items (Kobweb version, Playground `data-version` 2.4.21, Vercel empty `buildCommand`, CAA/CNAME target, effort-key support, parallel limit) are left open.
