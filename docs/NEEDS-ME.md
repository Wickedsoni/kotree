# Needs the author

Items that block further work and cannot be done by agents in this sandbox.

1. **`foundation/first-code/run-your-first-program`: content resolved, awaiting reviewer re-check.** The leaf is now `playground: false` (no Try it, no Fix the bug, three `run: false` Predict items hand-traced, `verified: not-run`), so the editor handoff (`docs/06` E7) and the Kotlin 2.4.21 run no longer block it. Still open: the authoring session had no shell, so `validateContent` was not run and the `println("Hi")` block was not compiled. Run `./gradlew :site:validateContent` and a quick kotlinc check, then `reviewer`.
2. **Sims needed.** Specs and IDs are listed in `docs/sims/needed-ids.md`. The live leaves that name a sim (`binary-switches`, `recipe-card-pointer`, `desk-and-cabinet`, `building-manager`, `input-process-output`) render nothing until the sims are built (policy L31).
3. **Build and export unverified.** `dl.google.com` and the Playwright hosts are blocked here, so `kobweb export`, the browser check and the Android-page claims were not run. `./gradlew :site:validateContent` passes (`validateContent: ok`).
