# Needs the author

Items that block further work and cannot be done by agents in this sandbox.

1. **`foundation/first-code/run-your-first-program` (stays shadow).** Needs a real run of Kotlin 2.4.21 that confirms the 4 verified fields in its Predict and Run items. Also needs the Try-it editor handoff (`docs/06` E7).
2. **Sims needed.** Specs and IDs are listed in `docs/sims/needed-ids.md`. The live leaves that name a sim (`binary-switches`, `recipe-card-pointer`, `desk-and-cabinet`, `building-manager`, `input-process-output`) render nothing until the sims are built (policy L31).
3. **Build and export unverified.** `dl.google.com` and the Playwright hosts are blocked here, so `kobweb export`, the browser check and the Android-page claims were not run. `./gradlew :site:validateContent` passes (`validateContent: ok`).
