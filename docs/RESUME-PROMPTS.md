# Resume prompts (for the two agents stopped on 18 Oct 2026)

> Used by the orchestrator at the start of the next session. Launch BOTH agents in one message (max 3 at once). Each prompt is complete: paste it as the agent prompt as written. After both report, follow `docs/NEXT-STEPS.md` from step 2.
> The agents were stopped mid-run, so their files may be half-edited. Each prompt starts by telling the agent to inspect first and finish, not restart.

## Agent 1: `architect` (Opus, default effort)

Owns ONLY: `docs/07-technical-architecture.md`, `docs/10-build-phases.md`, new `docs/11-phase0-checklist.md`.

```
You are resuming work that was stopped part-way on 18 Oct 2026. Some of your files may be half-edited. FIRST inspect docs/07, docs/10 and docs/11-phase0-checklist.md (it may not exist): see what is already done, then finish the rest. Do not restart and do not undo good edits.

Read docs/00-decisions.md first (it wins; L25 subdomain root no base path, L26 optional video, L27 free tiers only, D15-D17), docs/03 section 7.2 (Kobweb static export skips dynamic routes, so each live lesson needs addExtraRoute), docs/07 and docs/10.

You own ONLY docs/07-technical-architecture.md, docs/10-build-phases.md and docs/11-phase0-checklist.md. Do not edit any other file.

(1) docs/07: add a concrete plan for exporting every live lesson page. The same generator that writes TreeData.kt registers one extra route per status: live leaf. Verify the exact Kobweb API name and where it is configured in the Kobweb docs or source (varabyte/kobweb); if you cannot verify, keep a VERIFY mark with a test. Say how shadow leaves are handled (not exported; the route shows the shadow stub or 404). Add acceptance checks. Update docs/10 Phase 3 and Phase 4 text to match.

(2) Create docs/11-phase0-checklist.md: one table of EVERY still-open VERIFY mark across docs 00-10 (grep for 'VERIFY'). Columns: id, doc+section, what is unknown, exact command or test, phase it runs in, pass criterion, fallback if it fails. Group by phase, Phase 0 first; run cheap blockers first. Leave roadmap-level marks out (those are checked per leaf before it goes live). Skip VERIFY text in docs/00 section 10 and docs/01 that is only an authoring rule, not an open fact.

Reply in under 100 words: what you finished, number of rows in docs/11, any decision that changed.
```

## Agent 2: `architect` (model: sonnet)

Owns ONLY: `docs/02-design-system.md`, `docs/05-physics-and-visualization.md`.

```
You are resuming work that was stopped part-way on 18 Oct 2026. Some of your files may be half-edited. FIRST inspect docs/02 and docs/05 (look for edits about CSS variable names and SimFrame width and for the open questions Q4 and Q5 in docs/05): see what is already done, then finish the rest. Do not restart and do not undo good edits.

Read docs/00-decisions.md first (it wins; L8 desktop-first, L9 white only, L27 free tiers), then docs/05 (open questions Q4 token CSS variable names, Q5 narrowest SimFrame width) and docs/02.

You own ONLY docs/02-design-system.md and docs/05-physics-and-visualization.md. Do not edit any other file.

Q4: docs/02 says token names are dotted and become CSS variables with dashes. Settle the exact mapping rule in docs/02 using the Kobweb/Silk source or docs (StyleVariable or CSS custom properties), and make docs/05 use the same names. Keep a VERIFY mark only if the exact emitted name cannot be known without running it, and give a one-command Phase 0 test.

Q5: define in docs/02 the narrowest supported SimFrame width for a desktop-first site (L8). Choose a number, justify it from the lesson column width in docs/02 and docs/04, and make docs/05 reference it. Close Q4 and Q5 in docs/05 section 11 when done.

Reply in under 80 words.
```

## After both agents report (orchestrator)

1. Run the check script on the roadmaps (YAML leaves == table rows, `requires` exist and are earlier, no dangling IDs). Expect 482 leaves, 20 fruits, 0 bad.
2. Add `docs/11-phase0-checklist.md` to `docs/INDEX.md`.
3. One `reviewer` (Sonnet) pass for new contradictions since `docs/reviews/A2e-final.md`. Report to `docs/reviews/A2e-final-2.md`.
4. Update `docs/NEXT-STEPS.md` (remove the "stopped agents" section) and tell the author it is ready for review.
