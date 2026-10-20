# Needs the author

Items that block further work and cannot be done by agents in this sandbox.

1. **Sims needed.** Specs and IDs are listed in `docs/sims/needed-ids.md`. The live leaves that name a sim (`binary-switches`, `recipe-card-pointer`, `desk-and-cabinet`, `building-manager`, `input-process-output`, `hotel-bell-startup`, `mug-and-whiteboard-tag`, `overflow-cups`, `sorting-chutes`, `tape-measure-range`, `letter-stack-gates`) render nothing until the sims are built (policy L31).
2. **Build and export unverified.** `dl.google.com` and the Playwright hosts are blocked here, so `kobweb export`, the browser check and the Android-page claims were not run. `./gradlew :site:validateContent` passes (`validateContent: ok`).
