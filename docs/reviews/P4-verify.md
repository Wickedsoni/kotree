# P4 verify, Linux cloud box (20 Oct 2026)

Status: PARTIAL. Toolchain installed; export and site compile could not run because the sandbox network policy blocks two hosts.

| Step | Command | Result |
|---|---|---|
| JDK | `java -version` | PASS: OpenJDK 21.0.12 present (docs/07 §1 allows 17 to 27); script installs 17 only if java < 17 |
| Kobweb CLI | `scripts/setup-cloud.sh` (zip from kobweb-cli v0.9.23 release) | PASS: `kobweb version` = 0.9.23; second run is a no-op |
| Content validation | `cd site && ../gradlew :site:validateContent` | PASS: `validateContent: ok` (first try hit Maven Central HTTP 429, retry passed) |
| buildSrc tests | `../gradlew -p ../buildSrc test` | PASS |
| Site compile, 0 warnings | `../gradlew :site:compileKotlinJs` | BLOCKED: `androidx.compose.runtime:runtime:1.12.0` from `dl.google.com` returns 403 (proxy policy). With config cache on, KSP also fails serialising its config as a side effect of the unresolved classpath (not proven independent; re-check once the host is open). |
| CI-style export | `kobweb export --notty --layout static` + file/log check | NOT RUN: needs the compile, plus a Playwright browser from `cdn.playwright.dev` / `playwright.azureedge.net` (403). |
| kotree.js gzip | `gzip -9 -c site/.kobweb/site/kotree.js \| wc -c` vs 600000 | NOT MEASURED (last recorded: 313,048 B at Phase 3 S2, docs/07 §8.1) |
| Shadow leaves not exported | static read | By construction only: `LiveLessonRoutes` skips non-live; preview route `/preview/{branch}/{stem}/{leaf}` is dynamic (never exported), sets `robots noindex` in `LessonView.kt`; `kotree.preview` is forced off in an export; `emitLessonPages` deletes any `lessons-preview` dir and noindexes `404.html`. buildSrc tests cover the renderer. NOT verified on a real export. |

## Fix needed from the author
Environment network settings: allow `dl.google.com`, `cdn.playwright.dev`, `playwright.azureedge.net`, `playwright.download.prss.microsoft.com` (keep "Allow package managers" ticked). Then run `bash scripts/setup-cloud.sh && bash scripts/wave-check.sh`.

## Notes
- No source bugs found; no code changed. Docs/07 §3 P4-4 (Linux exit code) is still open.
- If KSP config-cache error persists with all hosts open, run Gradle with `--no-configuration-cache` and record it (docs/11 OD-3).
- New scripts: `scripts/setup-cloud.sh`, `scripts/wave-check.sh` (runs validate, export with the file+log check, prints gzip bytes vs 600000).
