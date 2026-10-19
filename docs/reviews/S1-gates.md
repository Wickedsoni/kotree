# S1 gate results (20 Oct 2026)

| Gate | Result |
|---|---|
| G1 JDK | PASS. `java -version` = OpenJDK 25.0.4 (range 17-27). |
| G2 Kobweb pin | PASS. GitHub API latest release is `v0.25.1` (published 2026-08-19); CLI latest `v0.9.23`. Pin unchanged. |
| G3 Lesson Kotlin pin | PASS. `curl ... api.kotlinlang.org/versions \| grep -c '"2.4.21"'` printed 1. |
| G4 Build runs, white page | PASS. `kobweb run` served `http://localhost:8080/` (title Kotree, `kotree.js` 200); the exported page contains the text "Kotree". Visual white check by eye still owed to the author. |
| G5 Export | PASS. `kobweb export --notty --layout static` exit 0, `site/.kobweb/site` holds index.html, kotree.js, favicon.ico. |

Notes
- Kobweb CLI was not installed; used the 0.9.23 release zip from `varabyte/kobweb-cli`. `kobweb create app` is interactive, so the `app/default` template from `varabyte/kobweb-templates` was instantiated by hand (groupId `dev.kotree.site`, projectName `kotree`, About.md and sample pages dropped).
- First export failed with "Not enough memory" yet the CLI still exited 0 (check the output, not only the exit code). Fixed with `org.gradle.jvmargs` and `kotlin.daemon.jvmargs` = -Xmx3g in `gradle.properties`.
- Webpack prints source-map warnings for library modules; no Kotlin compiler `w:` warnings from Kotree code.
- `git update-index --chmod=+x gradlew` not run (needs `git add`, not authorised).
