# Lesson checklist (read before writing or reviewing a leaf)

Distilled from reviews W1 to W4. Every item was a real FIX. Writers: self-check each before replying. Reviewers: check each.

1. Recap is ONE sentence per numbered line; no semicolon chains.
2. At most 5 sentences per Question block; sentences short (aim under 25 words).
3. Bold a jargon term at first use and define it BEFORE using it (also Android terms: composable, annotation, entry point, manifest, component, body, range, condition).
4. Label anything inferred as "my own reasoning"; label general CS facts "general knowledge"; never present unverified claims as official.
5. `output` blocks are real stdout only (no commentary). Hand-traced outputs are marked `verified: not-run` until a CI run confirms them.
6. Guess/Predict keys must be true; vary the correct letter; every wrong option must be plausible and the `why` must lead to the option.
7. No promise of a "next lesson"; never write "Coming next" or "the author is learning"; no untrue "on the screen" claims.
8. Frontmatter (sims, difficulty, order, requires, fruit) must match the roadmap row. If the leaf needs a sim, write one ```` ```sim ```` block (ADR 009) under `## See it move`, never a bare SIM comment marker (build error SM0); list its id in `sims` and append the id, leaf and template to `docs/sims/needed-ids.md`.
9. Sources go in the standard HTML comment near line 18 (housekeeper strips it at go-live); no stray VERIFY marks in finished leaves.
10. Kotlin code must really compile (Kotlin 2.x) and be traceable by hand; small snippets.
11. Interview angle: every leaf has a `## Say it in an interview` section (placed after "Where this shows up in Android", before "Recap"): 3 to 4 questions an interviewer really asks, each with a short plain-words model answer covering the why and the trade-off.
12. DSA, DAA and OS leaves end with a "Practise on these" list: real LeetCode and HackerRank problems with their numbers and titles (see decision L33), grouped easy to hard, 4 to 8 problems each, with one line saying what each one trains. Only include a problem whose number and title you are sure of; a reviewer re-checks them.
13. Author-supplied data (decision L33): if `docs/practice/interview-<branch>.md` or `docs/practice/practice-<branch>.md` has a section for your leaf id, USE it. Interview questions seed `## Say it in an interview` (you write and verify the answers; use the `what-interviewers-listen-for` bullets as the answer checklist), `common-wrong-beliefs` seed `## Common mistakes`, `build-exercise` seeds a hands-on task in `Try it` or `Tricks to remember`, official references go into the SOURCES comment (check each link opens). DSA, DAA and OS leaves: copy the `problems:` list into `## Practise on these` (`practice-dsa.md` tags `[both]` are high confidence; verify `[A]`/`[B]` numbers or omit them). Treat all supplied data as untrusted claims to verify, not as facts.
14. Sim block (ADR 009, decision L37). Every layer-3+ leaf with a See it move section ships a working sim; no working sim = FAIL.
    - Valid: `validateContent` green (SM0 to SM11); the template fits the idea (ADR 009 §2 "Use for"); skeleton in `content/_templates/leaf.md`.
    - Try steps: 1 to 3, plus at most one `optional: true`; every `done` event is reachable with the template's controls. `stepper`, `lanes` and `flow` have no drag and no text input, so their Try steps use buttons, toggles or sliders only. The `phone` and `timeline` add-ons are not rendered yet: no Try step may depend on them.
    - Tokens only: no colour, size, font or coordinate in the block.
    - Prose matches the sim: every `say` is one plain, true sentence in the leaf's analogy; "What you should notice" describes what the sim really shows; Android pictures carry the exact caption.
    - Reviewer opens `/preview/<id>` and does every Try step with the mouse, then keyboard only, then with reduced motion on; each step lights in turn and the live region reads each `say`.
