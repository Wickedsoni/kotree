# Lesson checklist (read before writing or reviewing a leaf)

Distilled from reviews W1 to W4. Every item was a real FIX. Writers: self-check each before replying. Reviewers: check each.

1. Recap is ONE sentence per numbered line; no semicolon chains.
2. At most 5 sentences per Question block; sentences short (aim under 25 words).
3. Bold a jargon term at first use and define it BEFORE using it (also Android terms: composable, annotation, entry point, manifest, component, body, range, condition).
4. Label anything inferred as "my own reasoning"; label general CS facts "general knowledge"; never present unverified claims as official.
5. `output` blocks are real stdout only (no commentary). Hand-traced outputs are marked `verified: not-run` until a CI run confirms them.
6. Guess/Predict keys must be true; vary the correct letter; every wrong option must be plausible and the `why` must lead to the option.
7. No promise of a "next lesson"; never write "Coming next" or "the author is learning"; no untrue "on the screen" claims.
8. Frontmatter (sims, difficulty, order, requires, fruit) must match the roadmap row. If the leaf needs a sim, one `<!-- SIM: id -->` marker; append the id to docs/sims/needed-ids.md.
9. Sources go in the standard HTML comment near line 18 (housekeeper strips it at go-live); no stray VERIFY marks in finished leaves.
10. Kotlin code must really compile (Kotlin 2.x) and be traceable by hand; small snippets.
11. Interview angle: every leaf has a `## Say it in an interview` section (placed after "Where this shows up in Android", before "Recap"): 3 to 4 questions an interviewer really asks, each with a short plain-words model answer covering the why and the trade-off.
12. DSA, DAA and OS leaves end with a "Practise on these" list: real LeetCode and HackerRank problems with their numbers and titles (see decision L33), grouped easy to hard, 4 to 8 problems each, with one line saying what each one trains. Only include a problem whose number and title you are sure of; a reviewer re-checks them.
