# 07 — Technical Architecture

> **Purpose:** how Kotree is built, served at `https://kotree.garvitmaheshwari.in`, deployed, and kept fast. Exact commands, settings, pipeline and budgets.
> **Read this when:** setting up or changing the Kobweb project, the markdown pipeline, CI, deploy, DNS, asset URLs, or anything performance-related.
> **Depends on:** `00-decisions.md` (wins conflicts; L25, L26, D15–D17, O1, O2 amended 18 Oct 2026), `02-design-system.md` (tokens, fonts), `03-information-architecture.md` (URLs, zoom levels, leaf states), `05` (sims), `06` (editor).
> **Version:** v2.5 · 20 Oct 2026 · owner: `architect`. v2 replaces the `/kotree` sub-path and Hostinger-folder plan of v1. v2.1 adds challenge-block sync and validation (§6.2.1), the lesson Kotlin version rule (§1), and the two-file Three.js budget (§8). v2.2 (18 Oct 2026, author decisions) pins `LESSON_KOTLIN_VERSION = 2.4.21` (§1) and makes `generateTreeData` emit both `TreeData.kt` (read by the app) and `tree.json` (tool artifact at `/tree.json`, never fetched by the app) (§6.4), matching `docs/03` §4.

v2.3 (18 Oct 2026, `architect`): every open verification mark from v2.2 checked against official sources (Kobweb docs and the `varabyte/kobweb` source on `main`, Vercel docs, Gradle, GitHub, Hostinger, Let's Encrypt). Items still open say exactly what is unknown and which Phase 0/6/7 check settles them.

v2.4 (20 Oct 2026, `architect`): adds §6.7, the plan for exporting every live lesson page (answers `docs/03` §7.2 "docs/07 chooses"), the `verifyLessonExport` check, and `"trailingSlash": false` in `vercel.json` (decided in `docs/03` §7.2, missing here until now). Every open check across docs 00-10 is listed with its test in `docs/11-phase0-checklist.md`.

v2.5 (20 Oct 2026, `architect`): Phase 3 S1 probe results (`docs/reviews/P3-probes.md`) and **ADR 005** (`docs/adr/005-lesson-content-delivery.md`). P0-7 failed (4,190 B gzip per Markdown lesson in the main bundle), so lessons are no longer Kobweb Markdown pages: the build renders each live leaf to JSON on the JVM, one dynamic lesson page renders it, `addExtraRoute` exports one HTML file per live leaf, and `emitLessonPages` bakes head tags, static prose and the JSON into it (§6.2, §6.7). Also: `kobweb export` can exit 0 on a failed build (§3, §5.4); P0-3a/P0-3b/P0-4/U4/U5 results recorded (§6.2); new rules V11, V12.

## Changes from verification (18 Oct 2026)

| # | Change | Why (source) |
|---|---|---|
| V1 | Kobweb pin candidate is **0.25.1** (not 0.23.x). Its version block sets Kotlin 2.4.10 and compose-runtime 1.12.0. Kobweb CLI latest is 0.9.23. | github.com/varabyte/kobweb/releases , github.com/varabyte/kobweb-cli/releases |
| V2 | R1 keeps `{{{ SimHost("<sim-id>") }}}` as the **primary** form. The block parser passes the trimmed text through unchanged and adds `()` only when the text ends in a letter or digit. The generated wrapper is now a fallback, used only if the Phase 0 smoke test fails. | `KobwebCallBlockParser.kt`, `KobwebCall.kt` |
| V3 | (Settled 20 Oct 2026: P0-3a failed, see V8; moot for lessons under ADR 005.) `syncLessons` no longer adds `routeOverride`. Slugs keep their hyphens (`what-is-null.md` → `what-is-null`), and folders map to path segments as they are. **New Phase 0 check:** the Kotlin package and function names generated for hyphenated or keyword folder names (§6.2). | `markdown/util/RouteUtils.kt`, `ConvertMarkdownTask.kt` |
| V4 | 404 page: Kobweb exports **no** `404.html` by itself. Kotree adds a page with route `/404`, which the export rule writes to `404.html`, and registers the same composable through `ctx.router.setErrorPage` in `@InitKobweb` (§4). | `KobwebExportTask.kt`, `AppBlock.kt`, Kobweb routing docs, Vercel custom-404 guide |
| V5 | The bundle-growth measurement moves to a **Phase 0 spike** with 50 synthetic leaves. Previously it started at the pilot leaf and had to be decided before about 20 leaves were live (§8). | Generated Markdown Kotlin is added to the `jsMain` source set (`KobwebxMarkdownPlugin.kt`) |
| V6 | New deploy risk: Vercel's "Uploads per day (Free) = 5000" (§5.6). | vercel.com/docs/limits |
| V7 | (20 Oct 2026) ~~Lesson pages are static routes, exported without `addExtraRoute`.~~ **Superseded the same day by ADR 005:** one dynamic lesson page, exported once per live leaf with `addExtraRoute` (§6.7). | `KotlinRenderer.kt` and `AppBlock.kt` at tag `v0.25.1`; Kobweb exporting docs |
| V8 | (20 Oct 2026) S1 probes: P0-3a FAIL (file names not sanitised: `fun What-suspend-meansPage`), P0-3b PASS (`<route>.html`), P0-4 PASS, U4 PASS, U5 PASS, P0-7 FAIL (4,190 B gzip per leaf). `kobweb export --notty` returned exit 0 on four failed builds (CLI 0.9.23, Windows). | `docs/reviews/P3-probes.md` |
| V9 | (20 Oct 2026) Export snapshot = whole document right after `navigate(...)` with no extra wait; generated `main` clears `#_kobweb-root` then renders and routes by `window.location`; `AppGlobals.isExporting` exists. These shape ADR 005. | `KobwebExportTask.kt`, `templates/MainTemplate.kt` at `v0.25.1`; Kobweb exporting docs |

Shared names used here: site origin `https://kotree.garvitmaheshwari.in`, site root `/` (no base path); zoom levels **Canopy, Bough, Branch, Stem, Leaf** (defined in `docs/03`); colours, fonts and spacing come only from `docs/02` tokens.

---

## 1. Stack summary

| Layer | Choice | Notes |
|---|---|---|
| Framework | Kobweb (Kotlin/JS + Compose HTML + Silk) | **Pin `kobweb = "0.25.1"`.** It was the latest stable release on 18 Oct 2026 (released 19 Aug; version block `kotlin = "2.4.10"`, `compose-runtime = "1.12.0"`; source: https://github.com/varabyte/kobweb/releases/tag/v0.25.1, read 18 Oct 2026). The site compiles with the Kotlin version that release names. Phase 0 re-opens the releases page and records the pin in ADR 001. |
| Markdown | `com.varabyte.kobwebx.markdown` Gradle plugin + `kobwebx-markdown` library | Lessons stay in `/content`; see §6. |
| Tree rendering | One HTML `<canvas>` (2D context) for the pixel backdrop + DOM elements on top for stems/leaves (D2) | `imageSmoothingEnabled = false`, CSS `image-rendering: pixelated`. |
| Export | `kobweb export --layout static` | Output in `site/.kobweb/site`. |
| CI | GitHub Actions | Builds on every push and PR; also performs the Vercel deploy (§5). |
| Host | Vercel (D17, L18), production and PR previews | §5. |
| DNS | Hostinger DNS zone of `garvitmaheshwari.in`: one CNAME for `kotree` | §5.3. Root site untouched (L25). |
| JDK | Temurin 17 | Same version locally and in CI. Verified 18 Oct 2026: the Kobweb workflow guide uses `actions/setup-java` with `temurin` / `17` and calls 17 "the minimum version the latest Gradle requires"; Gradle 9.8.1 needs "a JVM version between 17 and 27" (https://docs.gradle.org/current/userguide/compatibility.html). Locally, `scoop install java/openjdk` installs the newest OpenJDK. That is fine as long as it is 17–27; `java -version` is checked in Phase 0. |
| Lesson Kotlin version | One constant `LESSON_KOTLIN_VERSION` in `site/build.gradle.kts`: the version every challenge is run with (`docs/06` §5) and the Playground `data-version` every block uses. **Pinned: `LESSON_KOTLIN_VERSION = "2.4.21"`** (author decision 18 Oct 2026; kotlinlang.org releases lists `2.4.21` as a bug-fix release of 8 Oct 2026, and `https://api.kotlinlang.org/versions` offered `2.4.21` but not `2.4.20` on 18 Oct 2026). Phase 0 re-checks that `2.4.21` is still in that version list before ADR 001 is written; if it is gone, ask the author to re-pin. Rules in §6.2.1. | Separate from the Kotlin version that compiles the site, which is whatever the pinned Kobweb release needs (ADR 001). The two may differ. |

---

## 2. Project setup (Phase 0)

### 2.1 Install the Kobweb CLI (author's machine is Windows)

```powershell
scoop bucket add java
scoop install java/openjdk
scoop bucket add varabyte https://github.com/varabyte/scoop-varabyte.git
scoop install varabyte/kobweb
```

macOS/Linux: `brew install varabyte/tap/kobweb`. Other: `sdk install kobweb` (SDKMAN!).

### 2.2 Create the project

`kobweb create app` asks for a root folder name and creates `<root>/site/...` plus the Gradle wrapper and settings at `<root>`. Kotree's repo root **is** that root, so:

1. Run `kobweb create app` in a scratch folder, name it `kotree`.
2. Move its contents (`gradle/`, `gradlew`, `gradlew.bat`, `settings.gradle.kts`, `build.gradle.kts` if present, `gradle.properties`, `site/`) into the repo root. Do not overwrite `CLAUDE.md`, `docs/`, `content/`.
3. Delete the template's sample `site/src/jsMain/resources/markdown/About.md` (lessons come from `/content`, §6).
4. `git update-index --chmod=+x gradlew` (the executable bit is lost on Windows; the Kobweb workflow guide calls this out).

Resulting layout (extends `00` §8):

```
/settings.gradle.kts, /gradle/, /gradlew(.bat)   Gradle root (from template)
/site/.kobweb/conf.yaml                           site title, server files (no basePath)
/site/build.gradle.kts                            kobweb + markdown config, content tasks (§6)
/site/src/jsMain/kotlin/<pkg>/                    AppEntry.kt, pages/, components/, tree/, sims/
/site/src/jsMain/resources/public/                fonts/, tree/, img/ (copied to export root)
/vercel.json                                      Vercel project config (§5.5)
/.github/workflows/build.yml, deploy.yml, preview.yml
```

Package name: `in.garvitmaheshwari.kotree` is invalid Kotlin (`in` is a keyword) unless back-ticked. **Use `dev.kotree.site`.** Rejected: `in.garvitmaheshwari.kotree` (keyword escaping in every file).

### 2.3 `.kobweb/conf.yaml` (target shape)

```yaml
site:
  title: "Kotree"

server:
  files:
    dev:
      contentRoot: "build/processedResources/js/main/public"
      script: "build/kotlin-webpack/js/developmentExecutable/kotree.js"
    prod:
      script: "build/kotlin-webpack/js/productionExecutable/kotree.js"
      siteRoot: ".kobweb/site"
  port: 8080
```

Keep the `server.files` values the template generates (script file names follow the module name). **Do not add `basePath`.**

---

## 3. Commands (all run from `/site` unless stated)

| Purpose | Command | Verified |
|---|---|---|
| Dev server with live reload | `kobweb run` | Yes. Open `http://localhost:8080/`. |
| Dev via Gradle (watch) | `../gradlew kobwebStart -t` / stop: `../gradlew kobwebStop` | Yes |
| Static export | `kobweb export --layout static` | Yes. Without `--layout`, CLI 0.9.19+ opens an interactive layout menu, so always pass it. |
| Export in CI (no TTY) | `rm -rf .kobweb/site && kobweb export --notty --layout static 2>&1 \| tee export.log; test -f .kobweb/site/index.html && ! grep -q "Failed to export" export.log` | Command: yes (Kobweb GitHub-workflow guide; the CI guide downloads the CLI zip from `varabyte/kobweb-cli` releases and Kotree **pins** 0.9.23). **Exit code: not trustworthy.** The CLI notes say 0.9.16+ exits non-zero on failure in `--notty` mode, but the S1 probe (20 Oct 2026, CLI 0.9.23 via `kobweb.bat` on Windows) saw exit 0 on four runs that printed `BUILD FAILED` and `Failed to export a Kobweb site.` So every script and workflow deletes the old export first and checks the file and the log as shown. ⚠️ VERIFY (`docs/11` P4-4): repeat on the Ubuntu CI runner; even if Linux exits non-zero, keep the checks. Gate tests that need a reliable exit code (validators) run `../gradlew :site:validateContent` directly. |
| Test the export locally | `kobweb run --env prod --layout static` | Yes |
| Browser cache key for CI | `./gradlew -q :site:kobwebBrowserCacheId` (from repo root) | Yes |
| Content validation | `../gradlew :site:validateContent` | Kotree task, defined in §6.4 |
| Vercel CLI install (CI) | `npm install --global vercel@latest` | Yes (Vercel GitHub Actions guide) |
| Preview deploy (CI, repo root) | `vercel pull --yes --environment=preview --token=$VERCEL_TOKEN` → `vercel build --token=$VERCEL_TOKEN` → `vercel deploy --prebuilt --token=$VERCEL_TOKEN` | Yes (Vercel GitHub Actions guide) |
| Production deploy (CI, repo root) | `vercel pull --yes --environment=production --token=$VERCEL_TOKEN` → `vercel build --prod --token=$VERCEL_TOKEN` → `vercel deploy --prebuilt --prod --token=$VERCEL_TOKEN` | Yes (Vercel GitHub Actions guide) |

Rules:
- `kobweb export` downloads a headless browser on first run (it snapshots each page). Cache `~/.cache/ms-playwright` in CI keyed by `kobwebBrowserCacheId`.
- Export threads: `KOBWEB_EXPORT_NUM_THREADS` (`half` is the default; leave unset).
- Never invent flags. If a command is not in this table, check the Kobweb or Vercel docs and add it here first.

---

## 4. URLs and paths (no base path)

The site is served at the root of its own subdomain (L25). **No base path is used** (O2 dropped): no `site.basePath`, no `BasePath.prependTo`, no URL rewriting of lesson links. Plain absolute paths work everywhere: `Link("/kotlin/null-safety/what-is-null")`, `/fonts/roboto-flex-v1.woff2`, `/tree/bark-v1.png`, `fetch("/...")`, head links in `build.gradle.kts`, Markdown links `[What is null](/kotlin/null-safety/what-is-null)`.

| Subsystem | Rule |
|---|---|
| Routes | `/<branch>/<stem>/<leaf>` (scheme owned by `docs/03`). |
| Assets | `public/fonts`, `public/tree`, `public/img` export to the site root; reference as `/fonts/...`, `/tree/...`, `/img/...`. Inside static `.css` files relative `url(...)` is also fine. |
| External | Kotlin Playground (`docs/06`) and YouTube use absolute `https://` URLs. |
| Sitemap, canonical, Open Graph | Built from one constant `SITE_ORIGIN = "https://kotree.garvitmaheshwari.in"` + route. The origin string appears only there. |
| 404 | Kobweb's export writes **no** `404.html` by itself (`KobwebExportTask.kt` has no error-page export). The export path rule (`AppBlock.kt` `RouteConfig`) is: route `/a/b` → `a/b.html`, `/a/b/` → `a/b/index.html`, `/` → `index.html`. So Kotree adds one page, `pages/NotFound.kt` with `@Page("404")`, which gives route `/404` and the file `404.html`. The same composable is registered with `ctx.router.setErrorPage { ... }` inside an `@InitKobweb` function (Kobweb routing docs), so client-side navigation to an unknown route looks the same. Vercel: "Emit a `404.html` file to your Output Directory and it will be served as the 404 page when a route does not match any other static file" (https://vercel.com/kb/guide/custom-404-page, read 18 Oct 2026). (1) `@Page("404")` in the root `pages` package resolves to `/404`: **PASS 20 Oct 2026** (P0-4, `404.html` exported). ⚠️ VERIFY what the sources do not state: (2) Does Vercel answer with HTTP status 404 (not 200)? Phase 7 check: `curl -s -o /dev/null -w "%{http_code}" https://<preview-url>/does-not-exist` must print `404`. If it prints 200, apply the guide's `routes` pattern (`{ "src": "/(.*)", "status": 404, "dest": "/404.html" }`) and first read the `routes` section of the `vercel.json` reference to see how it combines with `cleanUrls` and `headers`. Not checked yet, because it is needed only in that case. |

Acceptance criteria:
- [ ] Cold load of `/` and of a deep lesson URL renders with fonts, sprites and styles; DevTools Network shows **zero 404s**.
- [ ] Source grep: the literals `/kotree`, `basePath` and `BasePath` appear nowhere in `site/src`, `site/build.gradle.kts` or `conf.yaml`.

---

## 5. Hosting and deploy (D17, answers O1)

### 5.1 Decision

**Vercel hosts production and pull-request previews. GitHub Actions builds the Kobweb export and deploys it to Vercel with the Vercel CLI (`--prebuilt`).** Push to `main` deploys production; every PR from a branch of this repo gets a preview URL. Vercel's own Git builds are switched off (`"git": { "deploymentEnabled": false }` in `vercel.json`, the setting the Vercel Actions guide gives so the two do not both fire). Record as ADR 002 (replaces v1's "Hostinger folder" ADR 002).

Why:
- The export needs JDK 17, Gradle and a Playwright headless browser. Vercel's build image (Amazon Linux 2023) lists only `java-11-amazon-corretto-headless` pre-installed; JDK 17 via `dnf` in `installCommand` plus Chromium system libraries is unproven. GitHub Actions is the path the Kobweb docs document.
- One build path for production and previews.
- Free (L27): "GitHub Actions usage is free for self-hosted runners and for public repositories that use standard GitHub-hosted runners"; larger runners are always billed, so use only `ubuntu-latest` (https://docs.github.com/en/billing/concepts/product-billing/github-actions, read 18 Oct 2026).

### 5.2 Rejected options

| Option | Why rejected |
|---|---|
| Vercel native Git integration building on Vercel | Neither Vercel nor Kobweb documents JDK 17 + Gradle + Playwright browser deps on Vercel's build image, and Kotree does not test it because the option is rejected. It would also split debugging across two CI systems. Keep as fallback only. |
| Hostinger static folder (v1 plan) | Only needed for the `/kotree` sub-path, which is gone (L25, D17). |
| Moving `garvitmaheshwari.in` nameservers to Vercel | Would move the whole domain's DNS and risk the root site; a single CNAME is enough. |
| GitHub Pages | L18 names Vercel; Vercel gives PR previews and Web Analytics. |

### 5.3 DNS and domain (one-time, author)

| Step | Where | What |
|---|---|---|
| 1 | Vercel dashboard | Create project `kotree` (Framework preset: Other). Settings → Domains → add `kotree.garvitmaheshwari.in`. Vercel shows "Invalid Configuration" until DNS is set; this is expected. |
| 2 | Vercel dashboard | Copy the exact CNAME target Vercel shows. Vercel docs: "Each project has a unique CNAME record e.g. `d1d4fc829fe7bc7c.vercel-dns-017.com`". So **use the project-specific dashboard value**, not a generic `cname.vercel-dns.com` (https://vercel.com/docs/domains/working-with-domains/add-a-domain, read 18 Oct 2026). If Vercel says the domain is used by another Vercel account, it asks for a `TXT` record at `_vercel.garvitmaheshwari.in`; add it in Hostinger the same way. |
| 3 | Hostinger hPanel → Domains → `garvitmaheshwari.in` → DNS / Nameservers (DNS Zone Editor) | First delete any existing `A`, `AAAA` or `CNAME` record named `kotree`. Vercel's troubleshooting table lists "conflicting A, AAAA, or CNAME records for the same hostname" as the cause of invalid configuration. Add **Type `CNAME`, Name `kotree`**: the name is "the hostname for the record, without the domain name", and Hostinger appends the domain. **Target** = value from step 2, TTL default (14400 s). Propagation can take "up to 24 hours". DNS editing in hPanel works only while the domain uses Hostinger nameservers. Do not touch the `@` / `www` records (root site, L25). Sources: https://www.hostinger.com/support/4738777-how-to-manage-cname-records-at-hostinger/ , https://www.hostinger.com/support/1583249-how-to-manage-dns-records-at-hostinger/ (read 18 Oct 2026). |
| 4 | Hostinger | Run `dig CAA garvitmaheshwari.in +short`. Empty output: nothing to do. If any `CAA` records exist, add `CAA 0 issue "letsencrypt.org"`. Vercel: "A CAA record that doesn't permit Let's Encrypt blocks issuance" (https://vercel.com/kb/guide/a-record-and-caa-with-vercel). Let's Encrypt's CAA identifier is `letsencrypt.org` (https://letsencrypt.org/docs/caa/). Both read 18 Oct 2026. |
| 5 | Vercel | Wait for propagation; Vercel verifies the record and issues HTTPS automatically. Set this domain as the project's production domain. |
| 6 | Local | `vercel login`, `vercel link` once to get `VERCEL_ORG_ID`, `VERCEL_PROJECT_ID` from `.vercel/project.json` (gitignore `.vercel/`). Create a token on the Vercel tokens page. Store all three as **GitHub repo secrets** only. |

### 5.4 Pipelines

| Workflow | Trigger | Steps |
|---|---|---|
| `build.yml` | push to any branch except `main`, and `pull_request` | Checkout, Temurin 17, `setup-gradle`, Playwright cache, `:site:validateContent`, the export command with its success check (§3; the exit code alone is not trusted), `./gradlew :site:copyTreeJson` (§6.4), `./gradlew :site:emitLessonPages` (§6.7, ADR 005), `./gradlew :site:verifyLessonExport` (§6.7), budget checks (§8). No secrets. |
| `preview.yml` | `pull_request` (same-repo branches) | Same build, then the three preview commands (§3); comment the preview URL on the PR. Uses `pull_request`, **never `pull_request_target`**, so fork PRs get no secrets (public repo); fork PRs get `build.yml` only. |
| `deploy.yml` | push to `main` | Same build, `validateContent` slug check against the live sitemap (§6.4), then the three production commands (§3). `concurrency: deploy` so two pushes never race. |

### 5.5 `/vercel.json`

```json
{
  "framework": null,
  "installCommand": "",
  "buildCommand": "",
  "outputDirectory": "site/.kobweb/site",
  "cleanUrls": true,
  "trailingSlash": false,
  "git": { "deploymentEnabled": false },
  "headers": [
    { "source": "/fonts/(.*)", "headers": [{ "key": "Cache-Control", "value": "public, max-age=31536000, immutable" }] },
    { "source": "/tree/(.*)",  "headers": [{ "key": "Cache-Control", "value": "public, max-age=31536000, immutable" }] },
    { "source": "/lessons/(.*)", "headers": [{ "key": "X-Robots-Tag", "value": "noindex" }] }
  ]
}
```

- `/lessons/<id>.json` (ADR 005) is data for the lesson page, so search engines index the page, not the JSON. It keeps Vercel's default caching (not versioned in its name, like HTML).

- All keys exist in Vercel's `vercel.json` reference (https://vercel.com/docs/project-configuration/vercel-json and .../git-configuration, read 18 Oct 2026). Verified meanings:
  - `"framework": null` selects "Other".
  - `"installCommand": ""`: "An empty string value will cause the Install Command to be skipped."
  - `"git": { "deploymentEnabled": false }` turns off automatic deployments for all branches.
  - `cleanUrls: true`: `about.html` is served at `/about`, and `/about.html` gets a 308 redirect to `/about`.
  - `trailingSlash: false`: `/about/` redirects to `/about`, so each lesson has one canonical URL (decision and source in `docs/03` §7.2).
- `"buildCommand": ""`: the Vercel docs describe "Skip Build Step" (preset "Other", Override on, Build Command left empty: "This prevents running the build, and your content is served directly"). They do not say that `vercel build` then copies `outputDirectory` into `.vercel/output/static`. ⚠️ VERIFY in Phase 7. Run `vercel pull --yes --environment=preview --token=$VERCEL_TOKEN && vercel build --token=$VERCEL_TOKEN` locally, then check that `.vercel/output/static/index.html` exists and that `.vercel/output/config.json` holds the clean-URL and header routes. Fallback if not: the workflow writes the Build Output API folder itself (`.vercel/output/static/` + `config.json`, https://vercel.com/docs/build-output-api), then `vercel deploy --prebuilt`.
- Export shape (verified from `AppBlock.kt` `RouteConfig.exportPath` on `main`, 18 Oct 2026): a route without a trailing slash is written as `<route>.html`. So `/kotlin/null-safety/what-is-null` → `kotlin/null-safety/what-is-null.html`, and `cleanUrls` serves it at the clean URL. Lesson links never end in `/`.
- Kobweb's JS file is **not** content-hashed. The export copies it as `systemRoot.resolve(scriptFile.name)` (`KobwebExportTask.kt`), for example `/kotree.js`. So HTML and JS must never get a long `Cache-Control`; they keep Vercel's default. Long caching is safe only because files under `fonts/` and `tree/` carry a version in their name (`roboto-flex-v1.woff2`), so renaming is the cache-bust.
- `kobweb { app { export { includeSourceMap } } }` defaults to `true`, so `kotree.js.map` is exported too. Kotree keeps the default (public repo; debugging). The bundle budget (§8) measures `*.js` only.

### 5.6 Vercel Hobby (free) fit, checked 18 Oct 2026

Sources: https://vercel.com/docs/plans/hobby (updated 14 Sep 2026), https://vercel.com/docs/limits (updated 16 Sep 2026), https://vercel.com/docs/limits/fair-use-guidelines (updated 14 Sep 2026); all read 18 Oct 2026.

| Limit (Vercel docs) | Hobby value | Kotree use |
|---|---|---|
| Fast Data Transfer | 100 GB / month | Home ≤ 1 MB first load → roughly 100k cold visits/month before the cap. Fine for v1. |
| CDN requests | 1,000,000 / month | Static site, few requests per page. Fine. |
| Deployments per day | 100 | ~3 content pushes/week + PRs. Fine. |
| Build time per deployment | 45 min | Build runs in Actions; `vercel build` only packages. |
| CLI upload size | 100 MB ("Static file uploads") | Export budget ≤ 100 MB (§8). |
| CLI files per deployment | 15,000 source files | Kotree's export is roughly 500 HTML files plus assets, far below this. ⚠️ VERIFY (sources conflict, non-blocking): the limits page still lists 100 MB and 15,000 files, but the changelog "CLI deployment limits removed" (17 Jun 2026, https://vercel.com/changelog/cli-deployment-limits-removed) does not say which limits went. CI keeps both budgets anyway: `find site/.kobweb/site -type f \| wc -l` ≤ 15,000 and `du -sm site/.kobweb/site` ≤ 100. |
| Uploads per day (API rate limit `api-upload-free`) | 5,000 / day | New risk. A layout change rewrites about 500 HTML files, and each deploy (preview + production) uploads the changed files. ⚠️ VERIFY in Phase 7 whether the limit counts files and whether unchanged files are skipped: run `vercel deploy --prebuilt --debug` twice on the same output and compare the upload lines. Until then: at most 5 full-site redeploys per day. |
| Web Analytics | 50,000 events / month (collection pauses, resumes after 7 days) | Available on production (`00` §9). |
| Over the limit | "In most cases ... you will have to wait until 30 days have passed before you can use the feature again." No billing on Hobby. | Monitor usage monthly. |
| Use | "Hobby teams are restricted to non-commercial personal use only." Commercial = a deployment "used for the purpose of financial gain of anyone involved in any part of the production". Examples: payments from visitors, advertising a product or service, being paid to build or host the site, affiliate links as the main purpose, and "the inclusion of advertisements, including ... Google AdSense". "Asking for Donations does not fall under commercial usage." | Fits L27 while Kotree has no ads, no payments, no sponsors and no affiliate links, and donations are allowed. ⚠️ VERIFY (author, before Phase 7): the guidelines do not mention YouTube. Unknown: whether a monetised YouTube channel counts as "financial gain". That covers its videos embedded on Kotree (the player can show ads) or just linked. The guideline's own route: "contact the Vercel Support team". The author asks Support before Phase 7 and records the answer in ADR 002. If the answer is "commercial", the options are Pro (L27: upgrade only when required) or linking out instead of embedding. The second needs an author decision because it changes `docs/04`. |

### 5.7 Acceptance criteria (deploy)

- [ ] `https://kotree.garvitmaheshwari.in/` serves the site over HTTPS; `https://garvitmaheshwari.in/` is unchanged.
- [ ] `dig CNAME kotree.garvitmaheshwari.in` returns the Vercel target from §5.3.
- [ ] A content commit on `main` is live in **≤ 15 minutes**.
- [ ] Every same-repo PR gets a Vercel preview URL that behaves identically to production at `/`.
- [ ] `/does-not-exist` returns the Kotree 404 page with HTTP 404.
- [ ] Vercel's own Git deployments do not fire (only Actions deployments appear in the dashboard).
- [ ] Repo contains no tokens, passwords or keys; `.vercel/` is gitignored; secrets exist only in GitHub repo settings.

---

## 6. Markdown content pipeline

### 6.1 Overview

```
/content/<branch>/_roadmap.md ─┐
/content/<branch>/_branch.md   ├─> generateTreeData ─┬> build/generated/kotree/kotlin/.../TreeData.kt  (compiled into the site; the app reads only this)
/content/<branch>/<stem>/_stem.md ┘                   └> build/tree.json  (tool artifact, copied to export root as /tree.json; never fetched by the app)
                                           │
/content/<branch>/<stem>/<leaf>.md (status: live only)
   ├─> LiveLessonRoutes (ValueSource, configuration time) ─> addExtraRoute per live leaf (§6.7)
   └─> renderLessons (JVM) ─┬> build/generated/kotree/resources/public/lessons/<id>.json  (served and exported as /lessons/<id>.json)
                            ├> build/kotree/lessons-static/<id>.html  (static prose for emitLessonPages)
                            └> build/generated/kotree/kotlin/.../lesson/LessonConfig.kt  (LESSON_KOTLIN_VERSION only)
kobweb export ─> .kobweb/site/<id>.html (snapshot of the one lesson page) ─> emitLessonPages (head tags, static prose, inline JSON, sitemap.xml) ─> verifyLessonExport
                                     validateContent runs before generateTreeData and renderLessons and fails the build on any error
```

**ADR 005 (20 Oct 2026):** lesson text and challenge data are not compiled into `kotree.js`. The Kobweb markdown plugin is not used for lessons.

`/content` stays at the repo root (L17, `00` §8). It is never copied by hand into `site/`.

### 6.2 `renderLessons` (replaces `syncLessons`, ADR 005)

**Task.** A task class in `buildSrc` (`B/LessonRenderer.kt`, `B/LessonJson.kt`; a class, not a `Sync` with lambdas: the S1 probe saw lambda edits reported `UP-TO-DATE`). Inputs: `@InputDirectory ../content`, `LESSON_KOTLIN_VERSION`. Outputs: the three paths in §6.1. It reuses `loadContent` from `docs/14` §3.4, takes only `status: live` leaves (D16), and deletes its outputs first.

| Step | Rule |
|---|---|
| 1. Split | Cut the body at: an exact SIM marker line (R1 regex below) → `{"t":"sim"}`; a ```` ```challenge ```` block (already valid by CH1-CH10, §6.2.1) → `{"t":"predict"}` or `{"t":"fix"}` with the `docs/06` §3 fields; a ```` ```kotlin ```` fence → `{"t":"code"}` with `runnable` by the `docs/06` §1 rule. |
| 2. Prose | Each run of Markdown between cuts → one `{"t":"html"}` block, rendered by commonmark-java (GFM tables extension, `escapeHtml(true)`). |
| 3. Write | Lesson JSON schema 1 (ADR 005 §1), deterministic; static body (prose plus escaped `<pre><code>` for code, no answers); `LessonConfig.kt`. |
| 4. Wire | `kotlin.sourceSets.jsMain { resources.srcDir(...); kotlin.srcDir(...) }` from the task outputs, so `kobweb run` serves the JSON and the export copies it as a public resource. ⚠️ VERIFY (`docs/11` P4-3): `test -f site/.kobweb/site/lessons/<id>.json` after export and the JSON loads in `kobweb run`. Fallback: `emitLessonPages` copies the JSON into the export, and the dev server path uses `src/jsMain/resources/public/lessons/` (gitignored). |

New `validateContent` rules (message format `docs/14` §3.2):

| Rule | Fails when |
|---|---|
| V11 | A stem or leaf slug is `index` or starts with a digit. `index` would sit beside folder index files under `cleanUrls`; a leading digit breaks any generated Kotlin name if a Markdown page ever goes through the plugin again (P0-3a). No current slug is affected (grep, 20 Oct 2026). Runs on roadmaps, so it lands with V1-V10 in Phase 3 S2-A. |
| V12 | A live leaf body contains raw HTML (an HTML block or inline tag) other than an exact SIM marker line. `renderLessons` escapes HTML, so it would show as text. Phase 4. |

**Kobweb markdown plugin facts (S1 probe, 20 Oct 2026; kept for any future Markdown page, not used by lessons):** folder names are sanitised (`null-safety` → package `null_safety`; the soft keyword `suspend` compiles); **file names are not** (`what-suspend-means.md` → `fun What-suspend-meansPage()`, compile error). A Markdown file that goes through the plugin must therefore be written with `-` → `_` in its file name and an absolute `routeOverride: /<route>` in its frontmatter (P0-3a fallback, proven: routes stayed hyphenated). `addSource(taskProvider)` reads the task output with no `dependsOn` (U4); `{{{ Widget("arg") }}}` compiles (U5); the route file shape is `<route>.html` (P0-3b); `@Page("404")` writes `404.html` (P0-4).

The rest of this section (below, to §6.3) is the **superseded** `syncLessons` design (v2.4), kept for its R1 regex and the reasoning about HTML comments. Do not build it.

#### 6.2-old `syncLessons` (superseded)

Shape: `tasks.register("syncLessons")` with `inputs.dir("../content")`, `outputs.dir("build/kotree/markdown")` and a second output, the generated file of §6.2.1. Its action deletes the markdown output folder, then for each live leaf reads the file as text, applies rewrite R1 (sim marker) and R2 (challenge block) in that order, and writes the result. A plain copy task is not enough because R2 replaces a multi-line block, and Gradle's line `filter` sees one line at a time.

- From `../content`, include `*/*/*.md`; exclude any file whose name starts with `_` and the `_templates/` folder.
- Copy only files whose frontmatter has `status: live` (L5, D16: nothing unpublished becomes a page).
- Destination `site/build/kotree/markdown/`, preserving `<branch>/<stem>/<leaf>.md`, so the route is `/<branch>/<stem>/<leaf>` at the site root. URL scheme is owned by `docs/03`; change both docs together.
- Register it with `kobweb.markdown.addSource(syncLessons)`. `syncLessons` is the `TaskProvider` returned by `tasks.register`. `MarkdownBlock.kt` (main, read 18 Oct 2026) declares `addSource(dir: Directory, ...)`, `addSource(dirProvider: Provider<Directory>, ...)` and `addSource(taskProvider: TaskProvider<*>, targetPackage: String? = null)` (plus `Provider<String>` package overloads). The task's declared `outputs.dir` is what the plugin reads. Fallback if Phase 0 shows otherwise: `addSource(layout.buildDirectory.dir("kotree/markdown"))` with `dependsOn(syncLessons)`, or sync into `site/src/jsMain/resources/markdown/` and gitignore that folder.
- Routes: hyphens are kept, so **no `routeOverride` is injected**. `RouteUtils.getSlug` (markdown plugin, main, read 18 Oct 2026) runs `camelCaseToKebabCase`, turns `_` into `-`, collapses repeated `-`, and trims leading and trailing `-`. The route is `/` + parent folder path + slug, so `kotlin/null-safety/what-is-null.md` → `/kotlin/null-safety/what-is-null`. Settled by P0-3a on 20 Oct 2026 (FAIL; result and fallback above). Original open question: `ConvertMarkdownTask` builds the generated Kotlin package from the folder path (`packageFromFile`) and the function name from the file name (`capitalizedNameWithoutExtension + "Page"`), but the sanitizing code was not visible. Phase 0 check: put fixtures `content/kotlin/null-safety/what-is-null.md` and a keyword-named stem `content/kotlin/when/object.md` through `syncLessons`, then run `kobweb export --notty --layout static` in `/site`. Pass = the build compiles and `site/.kobweb/site/kotlin/null-safety/what-is-null.html` and `site/.kobweb/site/kotlin/when/object.html` exist. Fail = `syncLessons` writes to safe file names and adds `routeOverride: /<branch>/<stem>/<leaf>` (an absolute override starting with `/` is used as-is, per `RouteUtils.getRoute`), and `curriculum-designer` is told which slugs to avoid.
- **R1, sim marker rewrite (resolves `docs/05` C3).** Authors write only `<!-- SIM: sim-id -->` (`docs/04` §3.4; that doc is not changed). `/content` is never edited by the build. While writing each file, `syncLessons` replaces every line that matches exactly `^<!-- SIM: ([a-z0-9-]+) -->$` with the Kobweb block-widget line `{{{ SimHost("<sim-id>") }}}`. Kobweb's markdown plugin then compiles that line into a call to the `SimHost(id: String)` composable (`docs/05` §3.2), found through `imports.add(...)` (§6.3). `SimHost` then uses `SimEngineLoader` to load Matter.js or Three.js on demand (`docs/05` §3.1).
  - String argument: **verified from source** (`ext/kobwebcall/KobwebCallBlockParser.kt` and `KobwebCall.kt`, main, read 18 Oct 2026). A line starting with `{{{` and ending with `}}}` is a single-line block. The text between is trimmed, so spaces next to the braces are fine. The "no spaces" rule in the docs applies to the inline `${...}` form. A leading `.` is replaced by the project group, and `()` is appended only when the text ends in a letter or digit. Any other text, such as `SimHost("null-lunch-box")`, is emitted unchanged as a Kotlin call. The documented examples are bare names only, and the source read is `main`, not the 0.25.1 tag. Phase 0 smoke test: one fixture with `{{{ SimHost("null-lunch-box") }}}` compiles and renders the frame. Fallback only if that fails: `generateTreeData` writes one argument-free `@Composable` wrapper per sim ID (`fun Sim_null_lunch_box() = SimHost("null-lunch-box")`), and the filter writes `{{{ Sim_null_lunch_box }}}`. Authors see no difference either way.
  - HTML comments: the markdown plugin's `html` handler parses HTML blocks with Jsoup and renders only text and element nodes (`handlers/MarkdownHandlers.kt`). So a comment that is not rewritten produces no output (inferred from source; the docs say nothing). That is why the rule below exists.
  - Rejected: asking authors to write `{{{ SimHost(...) }}}` themselves (breaks `docs/04`, unreadable on GitHub); overriding a Markdown handler. The source has `html`, `code`, `rawTag`, `inlineTag` and others, but the docs site documents only `handlers.blockquote`, and an `html` override would affect every HTML block.
  - A marker line that does not match the exact form, or an ID not in frontmatter `sims`, fails `validateContent` (§6.4), so no comment is ever silently left as invisible HTML.
- **R2, challenge block rewrite.** See §6.2.1.

### 6.2.1 Challenge blocks: sync and validation (author decision 18 Oct 2026)

Authors write Predict-the-output and Fix-the-bug challenges only as the fenced ```` ```challenge ```` YAML block of `docs/06` §3 (fields and counts are owned by `docs/06`). This replaces the `<!-- CHALLENGE -->` plain-Markdown form; `docs/04` must be aligned by its owner.

**ADR 005 (20 Oct 2026): steps 3, 4 and 5 below are superseded.** No `ChallengeWidgets.kt` is generated and no `{{{ }}}` line is written: `renderLessons` (§6.2) puts each parsed block into the lesson JSON as a `predict` or `fix` block with the `docs/06` §3 field names, and the lesson page renders it with `PredictOutput` / `FixBug` (the `ui/lesson/*` components; `docs/06` names them `PredictCard` / `FixCard`). `LESSON_KOTLIN_VERSION` moves to the generated `LessonConfig.kt`. Steps 1, 2 and 6, the version rules and CH1-CH10 stay.

**How `syncLessons` turned a block into a widget (R2, v2.4; steps 3-5 superseded):**

| Step | Rule |
|---|---|
| 1. Find | A block opens on a line that is exactly ```` ```challenge ```` and closes on the next line that is exactly ```` ``` ````. |
| 2. Parse | The lines between are parsed with SnakeYAML (already on the build classpath for `generateTreeData`, §6.4). |
| 3. Name | Wrapper name = `Challenge_` + leaf ID with `/` → `__` and `-` → `_`, then `__`, then the challenge `id` with `-` → `_`. Example: `kotlin/null-safety/what-is-null` + `nullable-length` → `Challenge_kotlin__null_safety__what_is_null__nullable_length`. Slugs are kebab-case (no `__`, no leading or trailing `-`), so names cannot collide. |
| 4. Generate | Append to `build/generated/kotree/kotlin/dev/kotree/site/lessons/ChallengeWidgets.kt` (added to the `jsMain` source set like `TreeData.kt`) one `@Composable` function per block, without parameters, whose body calls `PredictCard(...)` or `FixCard(...)` (names from `docs/06` §3) with a data object holding every YAML field as literals: `PredictChallenge(leafId, id, code, verified, generated, options = listOf(PredictOption(id, text, why, correct)), output)` or `FixChallenge(leafId, id, code, verified, generated, hints, expected, fix, explanation)`. Strings are written as escaped Kotlin string literals (`\\`, `\"`, `\$`, `\n`). The same file holds `const val LESSON_KOTLIN_VERSION = "<pin>"` from §1. |
| 5. Replace | The whole block, fences included, becomes one line `{{{ Challenge_<...> }}}`, the bare-name block-widget form Kobweb documents. It resolves through `imports.add("dev.kotree.site.lessons.*")` (§6.3). `imports` is a `ListProperty<String>`; each entry is written as `import <entry>`, and a leading `.` is replaced by the project group (`MarkdownBlock.kt`, `KotlinRenderer.kt`; docs example `imports.add(".components.widgets.*")`; read 18 Oct 2026). Kotree writes the full package so it does not depend on the Gradle `group`. |
| 6. Run | `PredictCard` / `FixCard` pass `LESSON_KOTLIN_VERSION` to the Playground `data-version` attribute (option name from the `kotlin-playground` README, review A2e), so learners run code on the version the outputs were verified with. |

Why this shape: the bare-name `{{{ Name }}}` call is the only form Kobweb's docs show, and a challenge's data (multi-line code, options) would make one very long argument line; the data is type-checked Kotlin, like `TreeData.kt`; the block and the widget it names come from one parse in one task, so they cannot drift.

Rejected: `{{{ PredictCard(...) }}}` with all fields inline. The parser passes arguments through, but multi-line code and options in one Markdown line are unreadable and hard to escape. Also rejected: overriding the `code` handler. It exists in `MarkdownHandlers.kt` but is not on the docs site, and it would run for every fenced block; shipping the YAML to the browser and parsing it there (a YAML parser in the bundle); a runtime JSON fetch per lesson (extra request plus `kotlinx.serialization`).

**Kotlin version rules:**

- `LESSON_KOTLIN_VERSION` must be a stable release that appears in `https://api.kotlinlang.org/versions`. The `kotlin-playground` README links the `data-version` option ("Target Kotlin compiler version") to that URL (https://github.com/JetBrains/kotlin-playground, read 18 Oct 2026). Re-read 18 Oct 2026: the list has 16 entries, includes `2.4.21` (marked latest stable) and `2.5.0-Beta1`, and has no `2.4.20`. So it keeps only the newest patch of each minor line, and a pin goes stale when JetBrains ships a patch. ⚠️ VERIFY (Phase 6): the README does not say the widget reads this list at runtime. Check in DevTools Network on a page with a Playground block: one request to `api.kotlinlang.org/versions` (or the compiler server's equivalent), and a run with `data-version="2.4.21"` reports Kotlin 2.4.21.
- Changing the pin: edit the constant, record it in ADR 001, then `challenge-builder` re-runs every challenge that `validateContent` lists as older (below) and updates `verified`.
- `deploy.yml` fetches the version list and prints a warning (not a failure) if the pin is missing from it, so the author knows to bump.

**`validateContent` rules for challenge blocks** (added to §6.4; every rule names the file and line):

| # | Fails the build when |
|---|---|
| CH1 | A line starts with ```` ```challenge ```` but is not exactly ```` ```challenge ````, or a block is never closed. |
| CH2 | The block is not valid YAML. |
| CH3 | `type` is not `predict` or `fix`; a required field for that type (`docs/06` §3.1–3.3) is missing; or an unknown key is present (catches typos such as `expect:`). |
| CH4 | `id` is not kebab-case, or two blocks in one leaf share an `id`. |
| CH5 | Predict: the option count is outside `docs/06` §3.2, or not exactly one option has `correct: true`, or an option lacks `id`, `text` or `why`. Fix: the hint count is not the number set in `docs/06` §3.3. The counts are two named constants in the task, copied from `docs/06`. |
| CH6 | `generated` is present and not `true` or `false`. |
| CH7 | `code` or `fix` contains a line starting with `import android.` or `import androidx.` (`docs/06` §1). |
| CH8 | `verified` is neither `kotlin-<major>.<minor>.<patch>` nor `UNVERIFIED: <reason>`. |
| CH9 | On a `status: live` leaf: `verified` is `UNVERIFIED: ...`, or its version is newer than `LESSON_KOTLIN_VERSION` (compared as three numbers). |
| CH10 | A leaf contains a challenge block but its frontmatter has `playground: false` ("Run it to check" needs the Playground). |

Warnings (printed, build continues): `UNVERIFIED` on a non-live leaf (`docs/06` §5); `verified` older than `LESSON_KOTLIN_VERSION` on a live leaf (listed as "re-run on <pin>").

### 6.3 Kobweb markdown configuration (`kobweb { markdown { ... } }`) — not used for lessons (ADR 005)

Lessons no longer pass through the markdown plugin, so none of the settings below is configured for them. The lesson layout is the dynamic lesson page (§6.7); callout styling for blockquotes lives in `LessonProseStyle`; `sitemap.xml` is written by `emitLessonPages`. If no other page uses Markdown, Phase 4 removes the `kobwebx.markdown` plugin and `kobwebx-markdown` library from `site/build.gradle.kts` and records the bundle change in §8.1. The table is kept as the v2.4 record.

| Setting (documented) | Kotree use (v2.4, superseded) |
|---|---|
| `defaultLayout.set(...)` | The lesson layout from `docs/04` (header, sections). The Watch-the-video button and video section render **only when `video` is non-empty** (§6.5). Leaf frontmatter has no `layout` key. |
| `imports.add(...)` | Packages of lesson widgets (sim host, Playground block, and the generated challenge wrappers in `dev.kotree.site.lessons`, §6.2.1) so the lines `syncLessons` writes, such as `{{{ SimHost("null-lunch-box") }}}` and `{{{ Challenge_kotlin__null_safety__what_is_null__nullable_length }}}`, resolve. Authors never write this form; they write `<!-- SIM: null-lunch-box -->` or a ```` ```challenge ```` block and the sync rewrites (§6.2) produce it. Widget names are owned by `docs/05` and `docs/06`. |
| `handlers.blockquote.set(...)` | Optional: callouts ("Trick to remember") via `SilkCalloutBlockquoteHandler` if `docs/04` asks for them. |
| `process.set { entries -> ... }` | Build-time list of all lesson entries (`filePath`, `route`, `frontMatter`): used to cross-check that every page matches a roadmap leaf, and to emit `sitemap.xml` data. |

~~Lesson pages read their metadata with `rememberPageContext().markdown!!.frontMatter`.~~ The lesson page reads metadata from the compiled `TreeData` and the body from the lesson JSON (ADR 005 §2).

### 6.4 `generateTreeData` and `validateContent`

- `generateTreeData` parses the first ```` ```yaml ```` block of every `_roadmap.md` (`00` §8.1) with SnakeYAML in the build script classpath, plus each leaf file's frontmatter (`00` §8.2), builds one in-memory model with the schema of `docs/03` §4.2, and writes **two outputs** from it:
  1. **`TreeData.kt`** (Kotlin source, `build/generated/kotree/kotlin/dev/kotree/site/tree/TreeData.kt`, added to the `jsMain` source set): data classes and literals mirroring `docs/03` §4.2 field for field (`video: String?`, `null` when empty, §6.5). This is the **only** tree data the app reads.
  2. **`tree.json`** (`site/build/tree.json`, plain JSON in the `docs/03` §4.2 schema, written with a JSON library on the build classpath, never in the browser bundle): a build artifact for tools and agents (`housekeeper`'s `PROGRESS.md` generator, progress counter scripts, link checks). Published at `/tree.json` by the workflow step "copy `site/build/tree.json` to `site/.kobweb/site/tree.json`" that runs after `kobweb export` (§5.4). The app never fetches it.
  - Both outputs are written deterministically: same input → byte-identical files (stable key order, fixed indentation, `\n` line endings, no timestamps).
  - **As built in Phase 3 (deviations, ADR 003; `docs/14` §3.4):** (a) the generated file is `site/build/generated/kotree/kotlin/dev/kotree/site/tree/model/GeneratedTreeData.kt`, not `.../tree/TreeData.kt`, and it holds **only** `val treeData: TreeData = TreeData(...)` plus one private function per branch (17); the data classes and enums stay hand-written in `site/src/jsMain/kotlin/dev/kotree/site/tree/model/TreeData.kt`, so a drifting generator is a compile error and the name does not clash with that file. (b) `tree.json` is written by a hand-written deterministic writer (`buildSrc` `EmitJson.kt`), not a JSON library. (c) Locally the copy step is `../gradlew :site:copyTreeJson`, run after the export. (d) The "previously live slug removed or renamed" rule is V8 on git history (`-PbaselineRef`, default `origin/main`; a missing ref or `git` is a warning); the sitemap check below stays a Phase 7 `deploy.yml` step. (e) The SIM-marker, `sims`, `SimRegistry` and CH1–CH10 rules below arrive with Phases 4 to 6; Phase 3 implements V1–V12 (`docs/14` §3.2, V12 from ADR 005).
  - Chosen: generated Kotlin for the app (no runtime fetch, no `kotlinx.serialization` in the bundle, compile errors if the shape drifts) plus JSON for tools (agents and scripts need not parse Kotlin).
  - Rejected: runtime `tree.json` fetch by the app (extra request plus `kotlinx.serialization` in the bundle); hand-written data (drifts from roadmaps); adding a generated folder as an extra Kobweb resource directory so `tree.json` lands in the export by itself (whether the export copies an extra resource source dir is unverified; the copy step needs no unverified behaviour); two generator tasks (the outputs could drift).
- **Live list (D16):** a leaf is `live` if and only if its file exists with `status: live`. There is no separate published-IDs file. Every other roadmap leaf is non-live (`shadow`).
- **Up-next (D15):** per branch, `upnext` = the first non-live leaf in roadmap order. If a non-live leaf file in that branch has `upnext: true` in its frontmatter, that leaf is up-next instead. A file with `status: upnext` is treated as non-live and does not by itself change the choice. A fully live branch has no up-next leaf.
- `validateContent` (both tasks depend on it) fails the build on: YAML parse error; leaf file without a roadmap row; unknown or cross-branch IDs in `requires`; a `requires` that points forward; duplicate stem or leaf slugs in a branch; YAML block vs tables mismatch (`00` §8.1); frontmatter `id` not equal to its file path; missing required frontmatter keys; more than one `upnext: true` in a branch; `upnext: true` on a live leaf; a line containing `<!-- SIM` that is not exactly `<!-- SIM: sim-id -->`; a `SIM` marker ID missing from frontmatter `sims`, or a `sims` ID with no marker in the body (`docs/04` §2); a live leaf whose `sims` ID has no `SimRegistry` entry or no `docs/sims/<id>.md` (`docs/05` build check); non-empty `video` that is not an `https://www.youtube.com/watch?v=` or `https://youtu.be/` URL; any challenge-block rule CH1–CH10 (§6.2.1); slug of a previously live leaf removed or renamed (hard rule 6: `deploy.yml` fetches `https://kotree.garvitmaheshwari.in/sitemap.xml` and fails if any listed lesson URL is missing from the new export; skipped when no sitemap exists yet).

### 6.5 Optional video (L26)

- `video: ""` (or the key missing, or only whitespace) → `TreeData.video = null` → **show nothing**: no Watch-the-video button in the leaf popup or lesson header, no video section, no placeholder, no disabled button.
- Non-empty valid URL → button (popup and lesson header) and the video section appear. Adding the URL later is a one-line frontmatter edit; no other file changes.
- The YouTube player is loaded only after the learner clicks (click-to-load); 0 bytes from YouTube on page load. Layout of the section is owned by `docs/04`.

### 6.6 Acceptance criteria (pipeline)

- [ ] Adding one live leaf file and pushing produces exactly one new page and lights exactly that leaf on the tree; no other file changes are needed except the housekeeper's status files.
- [ ] Two runs of `generateTreeData` on the same content produce byte-identical `TreeData.kt` and byte-identical `tree.json`; a test confirms both hold the same values.
- [ ] `https://kotree.garvitmaheshwari.in/tree.json` returns the artifact; the home page and lesson pages make no request for it (Network panel); the main bundle contains no `kotlinx.serialization` code.
- [ ] A leaf file with `status: upnext` or `status: shadow` produces no page.
- [ ] With no `upnext: true`, each branch's glowing leaf is its first non-live roadmap leaf; adding `upnext: true` to another non-live leaf moves the glow there.
- [ ] A live leaf with `video: ""` shows no video button or section anywhere; setting a URL shows both after the next deploy.
- [ ] Each `validateContent` error case above has a fixture under `buildSrc/src/test/resources/fixtures/` (`docs/14` §3.4) and fails with a message naming the file and line.
- [ ] A live leaf containing `<!-- SIM: null-lunch-box -->` renders the `SimFrame`/`SimHost` frame at exactly that place (its lesson JSON has `{"t":"sim","id":"null-lunch-box"}` between the neighbouring `html` blocks); the source file in `/content` is unchanged after the build; a misspelled marker (for example `<!-- SIM:null-lunch-box -->`) fails `validateContent`.
- [ ] A live leaf with the `nullable-length` Predict block of `docs/06` §3.2 renders a Predict card at exactly that place; its lesson JSON has a `predict` block with `"id": "nullable-length"` and no `html` block contains the text `challenge`; `grep -c nullable-length site/.kobweb/site/kotree.js` is 0 (ADR 005: no lesson data in the bundle); the file in `/content` is unchanged.
- [ ] Each rule CH1–CH10 has a failing fixture; a live leaf with `verified: "UNVERIFIED: ..."` fails, the same leaf as `status: shadow` only warns.
- [ ] The Playground block on that card sends `data-version` equal to `LESSON_KOTLIN_VERSION`.
- [ ] Editing `/content` while `kobweb run` (or `../gradlew kobwebStart -t`) is running updates the page after reload. Verified basis: Gradle continuous build watches the file inputs of the requested tasks, and "Gradle only watches for changes to files inside the project directory" (https://docs.gradle.org/current/userguide/continuous_builds.html, read 18 Oct 2026). The Gradle root is the repo root (§2.2), so `/content` is inside it, and `syncLessons` declares `inputs.dir("../content")`. Known gap from the same page: "newly created input directories or filtered files may not trigger a build", so after adding a **new** leaf file or stem folder, restart `kobweb run`. Under ADR 005 the watched task is `renderLessons` (its `@InputDirectory ../content`); a changed lesson shows after reload because the page fetches `/lessons/<id>.json` in dev.

### 6.7 Exporting every live lesson page (answers `docs/03` §7.2)

**Verified facts (20 Oct 2026, Kobweb docs and the source at tag `v0.25.1`, the pinned release):**

| # | Fact | Source |
|---|---|---|
| E1 | "Dynamic routes are skipped over by the export process." To export one instance, call `addExtraRoute("/users/default/posts/0", exportPath = "users/index.html")` inside `kobweb { app { export { ... } } }` in `site/build.gradle.kts`. | https://kobweb.varabyte.com/docs/concepts/foundation/exporting |
| E2 | Signature: `fun addExtraRoute(route: String, exportPath: String? = null)` on `ExportBlock`. It adds to `extraRoutes: SetProperty<RouteConfig>`. KDoc: "Add a route to export on top of what's normally discovered. This can be useful if you want to export a specific dynamic route (which are normally skipped) or re-export a copy of an existing route to a different location. Note that any added [filter] will NOT be applied to these routes." Default `exportPath` = the route plus `.html` (or `index.html` if the route ends in `/`), with the leading `/` dropped. | `tools/gradle-plugins/application/.../extensions/AppBlock.kt` (tag `v0.25.1` and `main`) |
| E3 | `ExportBlock` also has `filter: Property<ExportFilterContext.() -> Boolean>` ("invoked for every page to test if it should be exported"; `ExportFilterContext` has `route`). | same file |
| E4 | The markdown plugin writes each Markdown file as a page annotated `@Page("<route>")`, where the route is `routeOverride` or `routeWithSlug`. A path made of kebab-case slugs has no `{...}` segment, so every lesson page is a **static** route. | `tools/gradle-plugins/extensions/markdown/.../KotlinRenderer.kt` (tag `v0.25.1`) |

| E5 | The export loads each route with `navigate("<url>?_kobwebIsExporting=true...")` and no further wait, then saves the whole document (`content()`). Data fetched after the load event is not in the snapshot. | `application/.../tasks/KobwebExportTask.kt` (tag `v0.25.1`) |
| E6 | The generated `main` removes every child of `#_kobweb-root` before the first render ("poor man's hydration") and routes by `window.location` (with `.html` stripped). Nodes outside `#_kobweb-root` stay. | `application/.../templates/MainTemplate.kt` (tag `v0.25.1`) |
| E7 | `AppGlobals.isExporting` is true while the export renders a page. | Kobweb exporting docs |

**Decision (v2.5, ADR 005; replaces the v2.4 "one static Markdown page per leaf, no `addExtraRoute`").** One dynamic page `K/pages/Lesson.kt` with `@Page("/{branch}/{stem}/{leaf}")` renders every lesson. The export writes one file per live leaf through `addExtraRoute`, and `emitLessonPages` finishes each file. The live list still has one parser (`buildSrc` `LeafParser`, D16) and two consumers: `LiveLessonRoutes` at configuration time and `generateTreeData`/`renderLessons` at execution time.

```kotlin
// site/build.gradle.kts (Phase 4)
val liveRoutes: List<String> = providers.of(LiveLessonRoutes::class) {          // Kotree ValueSource in buildSrc, not a Kobweb API
    parameters.contentDir.set(rootProject.layout.projectDirectory.dir("content"))
}.get()                                                                            // sorted, e.g. "/kotlin/null-safety/what-is-null"
kobweb { app { export {
    liveRoutes.forEach { addExtraRoute(it, exportPath = it.removePrefix("/") + ".html") }
} } }
```

- `exportPath` is explicit so the shape stays `<route>.html` (P0-3b PASS) whatever the default does.
- A `ValueSource` is used because `org.gradle.configuration-cache=true` (`gradle.properties`) and Gradle documents tracking of file **contents** read at configuration time but not of directory listings (a new leaf file). ⚠️ VERIFY (`docs/11` OD-3): export, add a new live leaf file, export again; the second export has its HTML.
- During the export the page renders `<div id="kt-lesson-body">` empty and does not fetch (E5, E7), so snapshots are deterministic.

**`emitLessonPages`** (task class in `buildSrc` `B/LessonPages.kt`, Jsoup; runs after the export and `copyTreeJson`, §5.4):

| Step | Rule |
|---|---|
| 1. For each live leaf | Open `site/.kobweb/site/<id>.html`. Set `<title>` (`<leaf title> · Kotree`), `<meta name="description">` = `summary`, `<link rel="canonical">` and `og:title`, `og:description`, `og:url` from `SITE_ORIGIN` + route (§4). |
| 2. Body | Replace the children of `#kt-lesson-body` with `site/build/kotree/lessons-static/<id>.html` (fails if the element is missing or not empty). |
| 3. Data | Append `<script type="application/json" id="kt-lesson-data">` with the lesson JSON to `<head>` (outside `#_kobweb-root`, so E6 keeps it); `<` is written as `<`. |
| 4. Once | Write `sitemap.xml` (`/` plus each live route); add `<meta name="robots" content="noindex">` to `404.html`; copy `/lessons/*.json` only if P4-3 failed (§6.2 step 4). |

On the client (ADR 005 §2): body from `#kt-lesson-data` when its `id` matches (direct load, synchronous, no flash after the E6 clear), else `fetch("/lessons/<id>.json")` (in-app navigation and `kobweb run`).

Why not the v2.4 design: a Markdown page per leaf costs 4,190 B gzip of the shared bundle (P0-7). Rejected also: a `filter` that drops non-live routes (E3); non-live leaves are never routes.

**`verifyLessonExport`** (Gradle task in `site/build.gradle.kts`, run as a CI step after `kobweb export`, the `tree.json` copy and `emitLessonPages`, §5.4; it can also run locally):

| Step | Rule |
|---|---|
| 1. Expected set | Read `site/build/tree.json` and take every leaf with `status` = `live`. Each expected file is `site/.kobweb/site/<branch>/<stem>/<leaf>.html` (export path rule E2, no trailing slash, §5.5). |
| 2. Actual set | Every `*.html` file exactly three path segments deep whose first folder is a branch slug from `docs/00` §5. |
| 3. Fail when | An expected file is missing ("live leaf `<id>` was not exported"), or an actual file is not expected ("`<path>` exported but its leaf is not live"). Print both lists. |
| 4. Also fail when | `sitemap.xml` lists a lesson URL that is not in the expected set, or misses one that is. |
| 5. Per page (ADR 005) | Each expected file has exactly one `script#kt-lesson-data` whose JSON `id` equals the leaf and `schema` is 1, a non-empty `#kt-lesson-body`, and a canonical URL equal to `SITE_ORIGIN` + route; `site/.kobweb/site/lessons/<id>.json` exists for every live leaf and for no other; `404.html` has `robots` `noindex`; no other exported page contains `kt-lesson-data`. |

**Shadow and up-next leaves.**
- They have no file in `/content` (shadow), or a file that is not `status: live` (up-next draft). `LiveLessonRoutes` and `renderLessons` skip both, so there is no route string, no exported HTML, no JSON and no sitemap entry.
- A request for `/<branch>/<stem>/<shadow-leaf>` finds no file. Vercel serves `404.html` (§4) with HTTP 404. On start the client router routes by `window.location` (E6), so it matches the dynamic lesson page, which finds the id is not `LIVE` in `TreeData` and renders the same shadow stub content as `NotFound` (`docs/03` §7.4): "This leaf has not grown yet.", the stem's `why` line, and a link to `/#<branch>/<stem>`. Any other unknown path reaches `NotFound` (plain "Not found"). No soft 200, no redirect.
- In-app navigation to such a path reaches the lesson page directly (same stub), or `ctx.router.setErrorPage` for other unknown routes (§4).
- ⚠️ VERIFY (Phase 4, checked again on a Vercel preview in Phase 7). Unknown: that the router picks the dynamic lesson page (not the `/404` page) when `404.html` is served at a three-segment path. Both paths render the same stub composable, which reads the path, so the stub should be right either way. Test: open `/kotlin/null-safety/<a-shadow-leaf>` on the preview. The stub text must be visible, the console must show no error, and `curl -s -o /dev/null -w "%{http_code}"` on that URL must print `404`.
- Un-publishing a live leaf would remove its page. The sitemap slug check (§6.4, hard rule 6) fails the deploy, so it cannot happen silently.

(The v2.4 "contingent path: one dynamic lesson page" is now the main path above.)

**Acceptance checks:**
- [x] **Check 1 (Phase 0), DONE 20 Oct 2026** with the Markdown-page design (`docs/reviews/P3-probes.md`): `<route>.html` shape PASS, shadow file absent; generated names needed the P0-3a fallback. Re-run in Phase 4 on the ADR 005 page with the pilot leaf plus one synthetic live leaf on a scratch copy.
- [ ] Publishing one leaf (`shadow` → `live`) adds exactly one lesson `.html`, one `/lessons/<id>.json` and one sitemap URL to the export, and `kotree.js` grows only by that leaf's live `TreeData` fields (ADR 005 cost table).
- [ ] A fixture where a live leaf's file is deleted from the export after the export run makes `verifyLessonExport` fail with "live leaf `<id>` was not exported"; a page whose `#kt-lesson-body` is empty fails step 5.
- [ ] `grep -n addExtraRoute site/build.gradle.kts` shows only the `liveRoutes.forEach` line.
- [ ] Direct load of a lesson makes no request to `/lessons/`; "Open lesson" from the tree makes exactly one.
- [ ] Vercel preview (Phase 7): `curl -s -o /dev/null -w "%{http_code}" https://<preview-url>/kotlin/null-safety/what-is-null` prints `200`; the same URL with a trailing `/` answers `308` to the slashless form; a shadow leaf URL prints `404` and shows the stub.

---

## 7. Canvas tree: technical rules

Visual rules are in `docs/02`, zoom behaviour in `docs/03`. This section only fixes the technical shape.

| Rule | Value |
|---|---|
| Layers | 1 `<canvas>` for the pixel backdrop (bark, shadow branches) + absolutely positioned DOM nodes for stems, leaves, fruits, labels (D2). DOM nodes are real `<button>`s for keyboard and screen-reader access. |
| Pixel crispness | Canvas backing size = CSS size × `devicePixelRatio`, `ctx.imageSmoothingEnabled = false`, CSS `image-rendering: pixelated`. Camera zoom snaps the pixel scale to integer multiples while drawing. |
| Determinism | Tree generator takes a fixed seed + `TreeData`; same input gives the same pixels on every build (D1). A snapshot test hashes the generated geometry. |
| Render loop | `requestAnimationFrame` only while the camera moves or an animation runs; idle = no frames. |
| Offscreen cache | Pre-render the static tree per zoom level (Canopy, Bough, Branch, Stem, Leaf) to an offscreen canvas; the frame loop blits and transforms. |
| DOM culling | Only leaf/stem nodes inside the viewport plus a 20% margin are in the DOM. |
| Reduced motion | `prefers-reduced-motion: reduce` replaces camera flights with an instant cut plus a 150 ms fade (motion tokens from `docs/02`). |
| Colours | Read from `docs/02` tokens via the shared token file; the renderer never contains a colour literal. |
| Asset loading | Sprite sheets load from `/tree/<file>` (absolute path, §4). |

---

## 8. Performance budgets

Measured on the production export, desktop, Chrome, Lighthouse "desktop" preset, on a mid-range laptop (4-core, integrated GPU, 1920×1080, DPR 1 and 2).

| Metric | Budget | How checked |
|---|---|---|
| Lighthouse Performance (home `/`, one lesson page) | ≥ 90 | Phase 8 audit; CI Lighthouse run optional later |
| Lighthouse Accessibility | ≥ 95 | Phase 8 audit |
| LCP | ≤ 2.0 s | Lighthouse |
| CLS | ≤ 0.05 | Lighthouse |
| Total Blocking Time | ≤ 200 ms | Lighthouse |
| Main site JS (Kotlin/JS bundle) | ≤ 600 KB gzip (raised from 400 KB by the author on 21 Oct 2026; revisit if the first load feels slow on a phone) | CI size check: `gzip -9 -c site/.kobweb/site/<script>.js \| wc -c`, where the file name is the one in `conf.yaml` `server.files.prod.script` (`kotree.js` in §2.3). It is one unhashed file (§5.5), and the `.map` is excluded. Measured (§8.1): 277,917 B after Phase 1; lesson text is kept out of it by ADR 005. |
| `TreeData` share of the bundle | ≤ 60 KB gzip at 500 leaves | Size diff when content grows |
| Fonts (Roboto Flex + JetBrains Mono, woff2, Latin subset, only the axes `docs/02` uses) | ≤ 250 KB total | File size check |
| Tree sprites / images on home | ≤ 150 KB total | File size check |
| Home page, first load total transfer | ≤ 1 MB | DevTools |
| Whole export (`site/.kobweb/site`) | ≤ 100 MB (Vercel Hobby CLI upload limit) | CI size check |

### 8.1 Measured baselines (tree-first prototype, 20 Oct 2026; `docs/reviews/S3-acceptance.md`)

These close `docs/11` P0-10 for the prototype. They come from headless Chrome on the author's machine, **not** the Lighthouse run on the reference laptop above, so they are a first reading and do not become acceptance criteria until re-measured in Phase 8.

| Measure | Value | Against the budget |
|---|---|---|
| First paint, HTML shell (static export) | 56 ms | Well under LCP ≤ 2.0 s |
| First paint, dev server | 590 to 700 ms | Dev only |
| Tree visible | before the 3 s sample | Meets "first screen < 3 s" |
| Frame time while zooming (120 wheel events) | p50 16.7 ms, p95 16.7 ms, max 16.9 ms | At the display refresh limit (max 0.2 ms over one 16.7 ms frame) |
| `kotree.js` | 827,482 B raw, **240,690 B gzip -9** (`gzip -9 -c site/.kobweb/site/kotree.js \| wc -c`) | 60 % of the 400 KB budget before any lesson text |
| DOM nodes on the home page | 24 to 29 | Far below the 200-node test |
| Geometry hash | `cecb906380417dd8` on every load | Deterministic |

**Phase 1 update (20 Oct 2026, `docs/reviews/P1-acceptance.md`).** After tokens, fonts, the base components and the `/kitchen-sink` page, `kotree.js` is **277,917 B gzip** (+37,227 B over the 240,690 B prototype; 247,676 B without the kitchen sink, so the sink costs about 30 KB). That is over the +25 KB point where the architect must look and under the +40 KB fail line. Decision (orchestrator, 20 Oct 2026): accept for now because the kitchen sink is the only large item and Phase 3 will measure the real lesson cost; if the Phase 3 projection is over budget, the first cut is to drop `/kitchen-sink` from the production build, then ADR 005. Headroom is now about 122 KB of the 400 KB budget.

**Phase 3 S2 update (20 Oct 2026).** With the generated tree data (482 leaves, 180 stems, shadow state) `kotree.js` is **313,048 B gzip**, so T = +35,131 B over the 277,917 B Phase 1 figure (cap 60,000; ADR 005 needs T <= 40,000). Headroom to the 400,000 B budget is about 87 KB. S3 must add at most 15,000 B. (S3 result: see "Phase 3 result" below.)

**Phase 3 S1 bundle spike (P0-7, 20 Oct 2026, `docs/reviews/P3-probes.md`).** 50 synthetic Markdown leaves (about 8,100 B of real prose each, 6 headings, a 15-line Kotlin block, one `PredictOutput` and one `FixBug` with literal data), compiled by the Kobweb markdown plugin:

| N leaves | `kotree.js` raw B | gzip -9 B |
|---|---|---|
| 0 (scratch copy) | 975,783 | 278,899 |
| 10 | 1,145,130 | 320,962 |
| 50 | 1,827,990 | 488,421 |

Per leaf c50 = **4,190 B gzip** (c10 = 4,206 B). Projection 277,917 + 482 × 4,190 + 40,000 = **2,337,690 B**, 5.8 × the budget; 19 leaves fit. **FAIL → ADR 005** (lessons out of the bundle). Projection under ADR 005: 277,917 + T 40,000 (reserve) + lesson runtime ≤ 15,000 + 482 × ≤ 120 B live `TreeData` fields = **about 390,757 B**; levers if over 380,000 are listed in ADR 005. Record after Phase 4: `kotree.js` with the lesson runtime, and the per-live-leaf growth.

**Phase 3 result (S2 and S3, 20 Oct 2026; `docs/reviews/P3-acceptance.md`).** Each value is `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` after a clean export, against the 400,000 B budget.

| Point | `kotree.js` gzip -9 B | Delta | Fail line | Result |
|---|---|---|---|---|
| M0 (after Phase 1) | 277,917 | — | — | baseline |
| After S2 (generated `TreeData`, 482 leaves, 180 stems) | 313,048 | T = +35,131 over M0 | T > 60,000 (ADR 005 assumes T ≤ 40,000) | PASS |
| After S3 (stems, twigs, up-next leaves on the tree) | 323,366 | +10,318 over S2; +45,449 over M0 | S3 > 15,000 | PASS |
| Headroom after S3 | 76,634 | | | |

- **50-leaf projection (S1 spike, above):** compiled Markdown lessons cost 4,190 B each, 482 leaves project to 2,337,690 B (FAIL, ADR 005).
- **Projection under ADR 005, re-based on the measured S3 figure:** 323,366 + lesson runtime ≤ 15,000 + 482 × ≤ 120 B live `TreeData` fields (57,840) = **about 396,206 B**. ADR 005 assumed 390,757 B because its 40,000 B T reserve did not include S3. This is over ADR 005's 380,000 B lever point and only about 3.8 KB under the budget, so Phase 4 must measure the lesson runtime and the per-live-leaf cost at the pilot and apply ADR 005's first lever (drop `/kitchen-sink` from production, about −30 KB) if the measured projection stays above 380,000 B.
- **Determinism hashes (console on page load, 1280 × 800):** `limbHash` = `cecb906380417dd8` (unchanged since the prototype: limbs did not move); `fullHash` = `c9399b38f0f393cf` (limbs + stems + leaves). The `fullHash` is **provisional**: a fix pass (`P3-acceptance.md` FIX 5, `Tokens.Gen` constants in `Stems.kt`) is in progress and may change it; the final value is the one recorded in `docs/reviews/P3-acceptance.md`.

**§8 budget table, continued** (these rows belong to the table at the top of §8; they were split off by an earlier edit and get a header here so they render):

| Metric | Budget | How checked |
|---|---|---|
| Frame time while panning/zooming | ≤ 16.7 ms p95 (60 fps), canvas draw ≤ 8 ms | Performance panel, 10 s pan/zoom script |
| Click on a leaf → popup visible | ≤ 100 ms | Performance panel |
| Matter.js (physics engine) | 0 bytes on home and on pages with no `physics` sim; on other pages loaded on demand (script tag, when the first sim nears the viewport), ≤ 30 KB gzip, cached once per visit (`docs/05` §3.1, §6.3) | Network panel; file size check on `/vendor/` |
| Three.js (two files: `three.core.js` + `three.module.js`, minified when vendored to `/vendor/three-<version>/`) | 0 bytes except on pages with a `3d` sim; there loaded on demand through one inline `<script type="module">` that imports `three.module.js`, which imports `./three.core.js` (`docs/05` §3.1). **≤ 200 KB gzip for both files combined** ⚠️ VERIFY: no official source states the gzip size. Measure the pinned pair (candidate `0.186.1`, `docs/05` §6.3, Q3) when it is vendored: `cat three.core.min.js three.module.min.js \| gzip -9 \| wc -c` ≤ 204800 | Network panel shows exactly two `/vendor/three-*` requests; file size check sums both |
| Sim Kotlin code | **In the main bundle on every page, including home** (Kotlin/JS emits one bundle; code splitting is not used). Shared sim runtime (host, loader, `SimWorld`, renderer) ≤ 12 KB gzip; each sim's Kotlin code and config ≤ 6 KB gzip; counted inside the main-bundle budget above (`docs/05` §6.3). ⚠️ VERIFY: these are targets, not measured. Measure with the same gzip command before and after the first sim lands (Phase 5) | CI size diff of the main bundle per sim added |
| Sim images or audio | None by default; if a spec needs one, ≤ 30 KB, from `/sims/<sim-id>/`, loaded on demand | File size check |
| Kotlin Playground | 0 bytes on pages with `playground: false`; on others loaded when the first code block nears the viewport | Network panel |
| YouTube | 0 bytes until the learner clicks the video (§6.5) | Network panel |
| CI build + deploy time | ≤ 10 min | Workflow duration |

A PR that breaks a budget is not merged until it is fixed or this table is changed with a reason.

**Known risk (closed 20 Oct 2026: measured by P0-7, resolved by ADR 005; text kept as the record).** Kobweb's markdown plugin writes a Kotlin file per Markdown page, and `KobwebxMarkdownPlugin.kt` adds that generated folder to the `jsMain` Kotlin source set (`kotlin.srcDir(convertTask.map { it.getGenDir() })`). The generated challenge wrappers (§6.2.1) are Kotlin too. So every lesson's text and challenge data lands in the **one** main bundle, which every page, including home, downloads. No official Kobweb source documents per-page code splitting. At about 480 detailed leaves this very probably exceeds 400 KB gzip. That is an estimate; the per-leaf cost is unmeasured.

**Phase 0 spike (V5; replaces "measure from the pilot leaf, decide before ~20 leaves"):**

1. On a throwaway branch, generate 50 synthetic live leaves of about 1,500 words, each with one Predict and one Fix block.
2. Export, and record `gzip -9 -c site/.kobweb/site/kotree.js | wc -c` with 0 leaves and with 50.
3. Per-leaf cost = difference / 50. Projected total = baseline + 480 × per-leaf cost.
4. If the projection is ≤ 400 KB, keep this design. If it is over, ADR 005 must pick a design before the pilot leaf.

Result: over budget (§8.1). ADR 005 chose lesson JSON plus one dynamic lesson page exported per live leaf with `addExtraRoute` (§6.7).

---

## 9. ADRs this doc requires

| ADR | Title | Content |
|---|---|---|
| 001 | Kobweb as the framework | Phase 0; pin Kobweb version, JDK 17, package `dev.kotree.site`, and `LESSON_KOTLIN_VERSION = 2.4.21` (§1, author decision 18 Oct 2026; re-check the Playground version list first). |
| 002 | Vercel hosting, built and deployed from GitHub Actions; `kotree` CNAME at Hostinger | §5.1–5.3. |
| 003 | Content outside `site/`, read at build time; tree data as generated Kotlin for the app plus a `tree.json` tool artifact from the same generator; validators | §6.4. **Written 20 Oct 2026:** `docs/adr/003-content-pipeline.md`. Lesson delivery is ADR 005, not 003. |
| 004 | ~~Challenge blocks rewritten at sync time into generated widgets~~ | Not written: superseded by ADR 005 (challenge data travels in the lesson JSON). The number stays unused. |
| 005 | Lesson content delivery | **Written 20 Oct 2026:** `docs/adr/005-lesson-content-delivery.md` (P0-7 failed). |

---

## 10. Sources (checked 18 Oct 2026)

- Kobweb exporting: https://kobweb.varabyte.com/docs/concepts/foundation/exporting
- Kobweb GitHub workflow export: https://kobweb.varabyte.com/docs/guides/git-hub-workflow-export
- Kobweb markdown: https://kobweb.varabyte.com/docs/concepts/foundation/markdown
- Kobweb conf.yaml: https://kobweb.varabyte.com/docs/concepts/foundation/kobweb-conf
- Kobweb install and project: https://kobweb.varabyte.com/docs/getting-started/getting-kobweb , https://kobweb.varabyte.com/docs/getting-started/kobweb-project
- Vercel + GitHub Actions (commands, secrets, `git.deploymentEnabled`): https://vercel.com/kb/guide/how-can-i-use-github-actions-with-vercel
- Vercel project configuration (`vercel.json` keys): https://vercel.com/docs/project-configuration
- Vercel custom domains (subdomain = CNAME): https://vercel.com/docs/concepts/projects/custom-domains , https://vercel.com/kb/guide/how-do-i-add-a-custom-domain-to-my-vercel-project
- Vercel Hobby plan: https://vercel.com/docs/plans/hobby
- Vercel limits (45 min build, 100 MB upload, 15,000 files, 100 deployments/day): https://vercel.com/docs/limits
- Vercel build image (Amazon Linux 2023, pre-installed packages): https://vercel.com/docs/deployments/build-image
- Kotlin releases (2.4.21 bug fix, 8 Oct 2026; 2.4.20, 7 Sep 2026): https://kotlinlang.org/docs/releases.html , https://kotlinlang.org/docs/whatsnew2420.html
- Kotlin version list served by the API (lists 2.4.21; no 2.4.20 on 18 Oct 2026): https://api.kotlinlang.org/versions
- Kotlin Playground options (`data-version`): https://github.com/JetBrains/kotlin-playground
- Three.js 0.186.1 build folder: https://app.unpkg.com/three@0.186.1/files/build
- Kobweb releases (0.25.1 latest stable) and CLI releases (0.9.23): https://github.com/varabyte/kobweb/releases , https://github.com/varabyte/kobweb-cli/releases
- Kobweb routing (slugs, `@Page`, `setErrorPage`): https://kobweb.varabyte.com/docs/concepts/foundation/routing
- Kobweb source on `main` (read 18 Oct 2026), under `tools/gradle-plugins/`: `extensions/markdown/.../MarkdownBlock.kt` (`addSource`, `imports`), `.../KobwebxMarkdownPlugin.kt` (generated sources added to `jsMain`), `.../tasks/ConvertMarkdownTask.kt`, `.../util/RouteUtils.kt` (slugs, `routeOverride`), `.../ext/kobwebcall/KobwebCallBlockParser.kt` and `KobwebCall.kt` (`{{{ }}}`), `.../handlers/MarkdownHandlers.kt` (handler names, HTML), `application/.../tasks/KobwebExportTask.kt` (no 404 export, unhashed JS), `application/.../extensions/AppBlock.kt` (export paths, `includeSourceMap`). Base: https://github.com/varabyte/kobweb/tree/main/tools/gradle-plugins
- Kobweb source at tag `v0.25.1` (read 20 Oct 2026): `application/.../extensions/AppBlock.kt` (`addExtraRoute(route: String, exportPath: String? = null)`, `filter`, default export path) and `extensions/markdown/.../KotlinRenderer.kt` (`@Page("<route>")` per Markdown file). Base: https://github.com/varabyte/kobweb/tree/v0.25.1/tools/gradle-plugins
- Kobweb source at tag `v0.25.1` (read 20 Oct 2026 for ADR 005): `application/.../tasks/KobwebExportTask.kt` (snapshot after `navigate` with no extra wait, `content()`, dynamic routes filtered, extra routes added, public resources copied) and `application/.../templates/MainTemplate.kt` (`#_kobweb-root` children removed before render, routing by `window.location`). `AppGlobals.isExporting`: https://kobweb.varabyte.com/docs/concepts/foundation/exporting
- Gradle configuration cache, undeclared file reads tracked by content; `providers.fileContents`, `ValueSource`: https://docs.gradle.org/current/userguide/configuration_cache_requirements.html (read 20 Oct 2026)
- Compose HTML `ref` in `attrs` (DOM element once on enter, `onDispose`): Compose Multiplatform HTML tutorial "Using Effects" (read 20 Oct 2026)
- Gradle Java compatibility (9.8.1: JVM 17–27): https://docs.gradle.org/current/userguide/compatibility.html ; continuous build: https://docs.gradle.org/current/userguide/continuous_builds.html
- GitHub Actions billing (free for public repos on standard runners): https://docs.github.com/en/billing/concepts/product-billing/github-actions
- Vercel: fair use (commercial definition): https://vercel.com/docs/limits/fair-use-guidelines ; `vercel build`: https://vercel.com/docs/cli/build ; build settings / Skip Build Step: https://vercel.com/docs/builds/configure-a-build ; `vercel.json`: https://vercel.com/docs/project-configuration/vercel-json ; git config: https://vercel.com/docs/project-configuration/git-configuration ; custom 404: https://vercel.com/kb/guide/custom-404-page ; add a domain: https://vercel.com/docs/domains/working-with-domains/add-a-domain ; CAA: https://vercel.com/kb/guide/a-record-and-caa-with-vercel ; changelog: https://vercel.com/changelog/cli-deployment-limits-removed
- Hostinger CNAME / DNS: https://www.hostinger.com/support/4738777-how-to-manage-cname-records-at-hostinger/ , https://www.hostinger.com/support/1583249-how-to-manage-dns-records-at-hostinger/
- Let's Encrypt CAA: https://letsencrypt.org/docs/caa/
