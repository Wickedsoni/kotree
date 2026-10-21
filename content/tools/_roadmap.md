```yaml
branch: tools
bough: craft
stems:
  - id: git
    title: Git
    why: Every team, every tutorial and every AI-assisted project uses git, and an un-saved mistake costs hours.
    leaves:
      - id: what-is-a-commit
        title: "How do I save a snapshot of my project that I can return to later?"
        difficulty: medium
        requires: []
        fruit: false
      - id: branches
        title: "How can I try an idea without breaking my working code?"
        difficulty: medium
        requires: [tools/git/what-is-a-commit]
        fruit: false
      - id: merge-conflicts
        title: "What do I do when git says two changes collide?"
        difficulty: hard
        requires: [tools/git/branches]
        fruit: false
      - id: remotes-and-pull-requests
        title: "How does my code get to GitHub and into a teammate's project?"
        difficulty: hard
        requires: [tools/git/what-is-a-commit, tools/git/merge-conflicts]
        fruit: false
  - id: android-studio
    title: Android Studio and IntelliJ
    why: The editor is where a fresher spends every day, and knowing its map saves hours of lost clicking.
    leaves:
      - id: find-your-way-around-studio
        title: "Where are my code, my build output and my run button in Android Studio?"
        difficulty: easy
        requires: []
        fruit: false
      - id: navigate-and-refactor
        title: "How do I jump to code, find usages and rename safely?"
        difficulty: easy
        requires: [tools/android-studio/find-your-way-around-studio]
        fruit: false
      - id: debug-with-breakpoints
        title: "How do I pause my app on a line and look at its variables?"
        difficulty: medium
        requires: [tools/android-studio/find-your-way-around-studio]
        fruit: false
  - id: gradle-basics
    title: Gradle Basics
    why: Every build error and every added library goes through Gradle, and generated build files are a common place for quiet mistakes.
    leaves:
      - id: what-gradle-does
        title: "What does Gradle do when I press Sync?"
        difficulty: medium
        requires: [tools/android-studio/find-your-way-around-studio]
        fruit: false
      - id: dependencies-and-configurations
        title: "How does a dependency line decide who can use a library?"
        difficulty: medium
        requires: [tools/gradle-basics/what-gradle-does]
        fruit: false
      - id: version-catalogs
        title: "Where do library versions live so that every module uses the same one?"
        difficulty: easy
        requires: [tools/gradle-basics/dependencies-and-configurations]
        fruit: false
  - id: adb-and-logcat
    title: ADB and Logcat
    why: When an app misbehaves on a real phone, the phone's own log and a command line are the first tools a developer reaches for.
    leaves:
      - id: adb-basics
        title: "How do I talk to my phone from my computer?"
        difficulty: easy
        requires: []
        fruit: false
      - id: reading-logcat
        title: "How do I find my crash among thousands of log lines?"
        difficulty: medium
        requires: [tools/adb-and-logcat/adb-basics]
        fruit: false
  - id: lint-and-static-analysis
    title: Lint and Static Analysis
    why: Warnings are a free reviewer, and freshers either ignore them all or silence them all.
    leaves:
      - id: what-lint-warns-about
        title: "How do I tell a real lint warning from noise?"
        difficulty: easy
        requires: [tools/gradle-basics/what-gradle-does]
        fruit: false
  - id: ci
    title: Continuous Integration
    why: A team needs the build and tests to run on every change by a machine that never forgets.
    leaves:
      - id: what-is-ci
        title: "What runs automatically when I push my code?"
        difficulty: easy
        requires: [tools/git/remotes-and-pull-requests]
        fruit: false
      - id: a-first-ci-workflow
        title: "How do I make GitHub build my app and run tests on every pull request?"
        difficulty: medium
        requires: [tools/ci/what-is-ci, tools/gradle-basics/what-gradle-does]
        fruit: false
  - id: publishing
    title: Publishing
    why: An app that never reaches a user's phone teaches nothing; a fresher must know the road from a commit to a store listing.
    leaves:
      - id: release-builds-and-signing
        title: "What makes a build one that I can ship?"
        difficulty: hard
        requires: [tools/gradle-basics/what-gradle-does]
        fruit: false
      - id: from-commit-to-store
        title: "Can I name every step from a commit to an update on a user's phone?"
        difficulty: hard
        requires: [tools/ci/a-first-ci-workflow, tools/publishing/release-builds-and-signing]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Stems `git` and `android-studio` are phase 2, `gradle-basics`, `adb-and-logcat` and `lint-and-static-analysis` are phase 5, and `ci` and `publishing` are phase 6. Tools comes right after Kotlin in the `docs/08` §10.1 tie-break, so the phase-2 and phase-5 stems may `Assume` only Foundation, Kotlin and earlier-phase leaves; later branches (`android-core`, `ui`, `testing`, `profiling`) appear here as `Helps to know` only. Gradle in depth is in `## Later`. There is no Android emulator or simulator (L15): git and CI sims are plain animations; Android-behaviour sims, where used, are `code-to-screen`.

- Assumes Foundation: `foundation/first-code/run-your-first-program` (phase 1) — used by what-is-a-commit.
- Assumes Foundation: `foundation/how-an-app-is-born/run-your-first-app` (phase 1) — used by find-your-way-around-studio, adb-basics and debug-with-breakpoints (first use of Android Studio is there; this stem is the full tour).
- Assumes Foundation: `foundation/how-an-app-is-born/what-happens-when-i-build` and `foundation/how-an-app-is-born/what-is-an-app-file` (phase 1) — used by what-gradle-does and release-builds-and-signing.
- Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing` and `foundation/debugging-mindset/reading-error-messages` (phase 1) — used by debug-with-breakpoints and reading-logcat.
- Assumes Kotlin: `kotlin/control-flow/ranges-and-for-loops` (phase 2) — the debugger lesson steps through a loop.
- Assumes Kotlin: `kotlin/lambdas/lambda-syntax` (phase 2) — Gradle build files in Kotlin are blocks of lambdas.
- Assumes Kotlin: `kotlin/reading-code/spotting-risky-patterns` (phase 3) — used by what-lint-warns-about.
- Helps to know Android core: `android-core/manifest-and-resources/sdk-versions` (phase 5) — what-gradle-does and release-builds-and-signing name the SDK numbers in the build file.
- Helps to know Android core: `android-core/manifest-and-resources/what-is-the-manifest` — what-gradle-does names the merged manifest.
- Helps to know Testing: `testing/unit-tests/run-your-first-unit-test` — a-first-ci-workflow runs tests the learner already wrote.
- Helps to know Profiling: `profiling/app-size/shrink-the-app` — release-builds-and-signing names shrinking only.
- Helps to know Security: `security/obfuscation/what-obfuscation-really-hides` and `security/secrets-and-logs/secrets-in-source-code` — what-is-a-commit mentions `.gitignore` and links forward to the secrets lesson; reading-logcat links to `security/secrets-and-logs/sensitive-data-in-logs`.
- Helps to know Native layer: `native-ndk/ndk-build/add-cpp-to-a-project` — what-gradle-does names the CMake hook in one sentence.
- Helps to know Kotlin Multiplatform: `kmp/source-sets/project-layout-and-targets` — Gradle for KMP is taught there; `version-catalogs` and `what-gradle-does` are its prerequisites.

Challenge note: git, Gradle files, YAML and shell commands do not run in the Kotlin Playground. Every `P:` and `F:` here is a read-only "which line or command is wrong" task, a plain-Kotlin model of the rule (for example a list of commits and a staging list that print what a snapshot would hold), or a question on a diagram. ⚠️ VERIFY with `docs/06` once written.

## Stems

### git · Git

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-commit` | medium | "What is in a snapshot, and why is a change I did not stage missing from it?" | A photo album of your room: you pick what goes in the frame (staging), take the photo (commit) and write a caption, and the album keeps every old photo. | "Git saves my file every time I press save, and a commit uploads it to the internet." | sim: a working folder, a small tray and a shelf of snapshots; the learner moves a changed file into the tray, takes a snapshot, and sees that an edit left outside the tray is not in it | `P:` after edit A, stage A, edit B, commit: which edits are in the snapshot (plain-Kotlin model) · `F:` a commit made with a new file never added · `B:` delete a file and bring it back from the last snapshot | Assumes Foundation: `foundation/first-code/run-your-first-program`. Names `.gitignore` and links to the secrets lesson. ⚠️ VERIFY: git-scm.com command names (`git add`, `git commit`, `git restore`) and the default branch name on the publish date. |
| `branches` | medium | "What is a branch, and what happens to my main code while I try something on another?" | A sticky note on one page of the photo album: the note is a movable label, not a second album. | "A branch is a full copy of the project folder." | sim: reuse `what-is-a-commit` sim, where snapshots are beads on a string and a branch is a label tied to a bead; making a snapshot moves only the label you hold | `P:` on the sim: which snapshot does main point to after two commits on a branch · `F:` work committed on main when a branch was meant · `B:` move the label back and say which snapshots are no longer on the line | ⚠️ VERIFY: `git switch` versus `git checkout` wording on git-scm.com. |
| `merge-conflicts` | hard | "Why does git stop and ask me to choose between two versions of the same line?" | Two people each correcting the same sentence in a shared printed page: the page cannot know whose correction wins until someone decides. | "A conflict means the code is broken or I did something wrong." | sim: reuse `what-is-a-commit` sim with two label lines meeting; both edit one line, the meeting point shows markers, and the learner picks, merges or discards | `P:` given the marker lines, which text remains after keeping both sides · `F:` a file saved with the conflict markers left in · `B:` make two edits on different lines and note that git merges them alone | Assumes the branches leaf of this stem. Plain-Kotlin model of three-way text merge is read-only. |
| `remotes-and-pull-requests` | hard | "What is the difference between my copy, GitHub's copy and a pull request?" | A shared noticeboard in the street: you draft at home, pin your draft up for review, and neighbours approve before it replaces the notice. | "Pushing sends my changes straight into everyone's project." | sim: two houses and a noticeboard; the learner pushes a branch, opens a request, and a reviewer's tick moves it onto the main board | `P:` after a push, which copy has the new commit and which still does not · `F:` a push rejected because the remote moved first · `B:` open a pull request from the wrong branch | Public repo rule: no secrets in commits (see `CLAUDE.md` rule 7). ⚠️ VERIFY: GitHub "pull request" wording and the button names on the publish date. |

### android-studio · Android Studio and IntelliJ

Flags: phase=2 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `find-your-way-around-studio` | easy | "Where do I find my source files, my build errors and my run button?" | The dashboard of a car: the same controls live in the same places, so you stop looking at them. | "Android Studio is a different program from IntelliJ, so what I learn does not carry over." | none: tooling steps on real software; annotated screenshots and a short reference video show it | `P:` on a screenshot: which panel shows build errors, which shows files · `F:` a "cannot find main" checklist with one step missed | volatile. Assumes Foundation: `foundation/how-an-app-is-born/run-your-first-app`. Android Studio is built on the IntelliJ platform; most steps carry over to IntelliJ IDEA with the Android plugin, and the lesson says which ones. ⚠️ VERIFY: current panel names, the "Empty Activity" template and the IntelliJ-with-Android-plugin story on developer.android.com and jetbrains.com on the publish date. |
| `navigate-and-refactor` | easy | "How do I jump to where a function is defined and rename it everywhere without breaking things?" | Using a book's index and a "replace all names" stamp instead of reading every page. | "Rename means find-and-replace in all files." | none: editor shortcuts; a keymap table and a short reference video are the right tool | `P:` on a screenshot: what the find-usages list shows for a function used twice · `F:` a manual rename that missed one call · `B:` rename with and without the safe-rename tool and compare | volatile. ⚠️ VERIFY: shortcut names in the Android Studio user guide (shortcuts differ by operating system). |
| `debug-with-breakpoints` | medium | "How can I stop my app on a line and watch a variable change?" | Pausing a film on one frame to look at everyone in the picture. | "I should print everything to find a bug; the debugger is for experts." | sim: code-to-screen a short loop on one side and the Variables panel on the other; the learner sets a breakpoint, steps over, into and out, and watches the values change | `P:` on the sim: what is the value of the counter after three Step Overs · `F:` a breakpoint set on a line that never runs · `B:` set a condition on the breakpoint | volatile. Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. Assumes Kotlin: `kotlin/control-flow/ranges-and-for-loops`. Checked 18 Oct 2026 (developer.android.com/studio/debug): click the gutter to set a breakpoint, use the Debug button on a debuggable build, Step Over, Step Into, Step Out and Resume. ⚠️ VERIFY again before live. |

### gradle-basics · Gradle Basics

Flags: phase=5 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-gradle-does` | medium | "What does Sync actually do with the build files?" | A building site foreman who reads the plan (settings and build files), orders the materials (libraries) and tells each crew (compile, pack, sign) when to start. | "Sync compiles and runs my app." | sim: a conveyor belt with stations; the learner edits a plan line (add a library) and sees the foreman re-order the materials; Sync only reads the plan, Run builds the box | `P:` which file lists the project's modules · `F(ai):` a generated build file with the module name misspelt in settings · `B:` remove the plugin line and say which station stops | volatile. Assumes Foundation: `foundation/how-an-app-is-born/what-happens-when-i-build`. Assumes Kotlin: `kotlin/lambdas/lambda-syntax`. Helps to know Android core: `android-core/manifest-and-resources/sdk-versions`. ⚠️ VERIFY: current Android Gradle Plugin and Gradle version basics, and the default file names (`build.gradle.kts`, `settings.gradle.kts`) on developer.android.com "Configure your build". |
| `dependencies-and-configurations` | medium | "Why does the same library work in my app but not in my tests?" | Luggage labels on a trip: "carry-on" goes into the cabin (app), "hold" goes to the plane only (tests), and a label decides which bag it can be opened in. | "Every dependency line is the same; I can use any keyword." | none: a table of keywords and a short reading of a real build file; no process to animate | `P:` which of three lines makes a library available in unit tests only · `F(ai):` a generated file that puts a test library in `implementation` · `F(ai):` a version that differs from the other module's | volatile. Checked 18 Oct 2026 (developer.android.com/build/dependencies): `implementation` is the default and hides the library from other modules at compile time; `api` exports it; `compileOnly`, `runtimeOnly`, `testImplementation` and `androidTestImplementation` limit where it is available; the `dependencies` block sits in the module's build file. ⚠️ VERIFY again before live. |
| `version-catalogs` | easy | "Where do I change a library version once so every module uses it?" | A shared price list pinned in the kitchen: every cook reads the same sheet, so no two orders use different prices. | "A version catalog is a new way to download libraries." | none: a file-reading rule; a side-by-side of the catalog file and the build file is enough | `P:` what name in the build file points to a catalog entry · `F(ai):` a generated build file that keeps a hard-coded version beside a catalog · `F(ai):` a catalog entry whose version reference does not exist | volatile. Checked 18 Oct 2026 (developer.android.com/build/migrate-to-catalogs): the file is `libs.versions.toml` in the root project's `gradle` folder; sections are `[versions]`, `[libraries]`, `[plugins]`; kebab-case names become dot accessors such as `libs.androidx.ktx`; plugins use `alias(libs.plugins....)`. ⚠️ VERIFY again before live. |

### adb-and-logcat · ADB and Logcat

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `adb-basics` | easy | "How do I check that my computer sees my phone, and how do I install or copy a file?" | A walkie-talkie between computer and phone: both must be on the same channel before either can speak. | "ADB is only for rooted phones or experts." | none: tooling steps with real devices; a short reference video and a command table are enough | `P:` which command lists connected devices · `F:` a "device offline" checklist with one step missed · `B:` run a command with two phones attached and see which one answers | Assumes Foundation: `foundation/how-an-app-is-born/run-your-first-app`. ⚠️ VERIFY: wireless debugging steps, the `adb` command names and developer options on developer.android.com on the publish date. |
| `reading-logcat` | medium | "How do I find the line that crashed my app among thousands of others?" | Finding your own name in a very long attendance sheet by filtering to your class and to the red-marked absences first. | "Logcat is just noise; the crash is already on my screen." | sim: a river of log lines flows past the learner; filter gates for tag, level and package let the learner cut the river down to one red error and its stack trace | `P:` on the sim: which filter shows only errors from my app · `F:` a log call using the wrong priority so it never shows · `B:` add a tag filter and note which lines disappear | Assumes Foundation: `foundation/debugging-mindset/reading-error-messages`. Checked 18 Oct 2026 (developer.android.com/studio/command-line/logcat): priorities run Verbose, Debug, Info, Warning, Error, Fatal, Silent; a filter is `tag:priority`, with `*:S` to silence the rest. ⚠️ VERIFY: Android Studio Logcat window filter syntax and the `--pid` option. Links forward to `security/secrets-and-logs/sensitive-data-in-logs`. |

### lint-and-static-analysis · Lint and Static Analysis

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-lint-warns-about` | easy | "How do I tell a lint warning that points at a real bug from one that is noise?" | A proofreader's margin notes: some mark a real mistake, some a style choice, and you decide which to accept, but you never rub them all out. | "Warnings are harmless, so I can hide every one." | none: a reading skill; real warnings with the fix beside them are clearer than motion | `P:` which of four warnings is likely a real bug · `F(ai):` a generated file that silences a warning with a suppress annotation instead of fixing it · `F(ai):` a hard-coded text warning dismissed as "not important" | Assumes Kotlin: `kotlin/reading-code/spotting-risky-patterns`. Names Android Lint, ktlint and detekt only; writing custom checks is in `## Later`. ⚠️ VERIFY: Android Lint basics and the Gradle task name on developer.android.com. |

### ci · Continuous Integration

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-ci` | easy | "What does a machine do the moment I push my code, and why does a team want that?" | A factory quality gate: every box goes through the same scanner, whoever packed it. | "CI is for big companies; it works on my laptop, so it is fine." | sim: a conveyor belt of pull requests passes through gates (build, tests, lint); a broken change turns the gate red and stops at the door | `P:` on the sim: which gate stops a change that does not compile · `B:` remove the test gate and say what slips through | Assumes git leaf `tools/git/remotes-and-pull-requests`. Names GitHub Actions only; other services are out of scope. ⚠️ VERIFY: current GitHub Actions free-tier limits for public repositories (docs.github.com). |
| `a-first-ci-workflow` | medium | "How do I write the small file that makes GitHub build my app on every pull request?" | A standing order at a bakery: "every morning, bake, taste, and report", written once and followed without you. | "I have to start the build myself each time, on my computer." | sim: reuse `what-is-ci` sim, with a workflow file on the left whose lines (trigger, machine, steps) light up on the conveyor | `P:` which line makes the workflow run on pull requests · `F(ai):` a generated workflow that never installs Java · `F(ai):` a generated workflow that runs on every branch push with a leaked secret in a plain line | volatile. Checked 18 Oct 2026 (docs.github.com, building and testing Java with Gradle): a workflow has `on`, `jobs`, `runs-on` and steps; the Gradle example uses `actions/checkout`, `actions/setup-java` and the Gradle setup action then `./gradlew build`; action versions change often. ⚠️ VERIFY action names and versions before live; keep secrets in the repository's secret store, never in the file. |

### publishing · Publishing

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `release-builds-and-signing` | hard | "What must a build have before Google Play accepts it?" | A shop's official stamp: every parcel carries it, users trust it, and a lost stamp means you cannot prove later parcels come from you. | "A debug build is just as good to ship as a release build." | sim: reuse `foundation/how-an-app-is-born/what-happens-when-i-build` sim, with two lanes, debug and release; release adds shrinking and signing; the learner loses the stamp and sees which updates are refused | `P:` which of three builds Google Play would accept · `F(ai):` a generated build file with the keystore password written in plain text · `F(ai):` a release type with signing missing · `B:` change the debug stamp for a release stamp | volatile. Assumes Foundation: `foundation/how-an-app-is-born/what-is-an-app-file`. Checked 18 Oct 2026 (developer.android.com/studio/publish/app-signing): Play App Signing keeps the app signing key and you sign uploads with a separate upload key, so a lost upload key can be reset; a self-managed signing key lost means no more updates; debug builds are signed with a debug certificate that Play does not accept. ⚠️ VERIFY again before live. Helps to know Security: `security/obfuscation/what-obfuscation-really-hides`. |
| `from-commit-to-store` | hard | "What are all the steps from a commit on my laptop to an update on a user's phone?" | A parcel route from the sender's desk to a doorstep: every hand-off in order, and where it can be held up. | "Publishing is only the last upload button." | sim: reuse `tools/ci/what-is-ci` sim extended to the end of the line: build, tests, sign, upload, review, rollout; a stopwatch shows where each wait is | `P:` order seven shuffled steps · `F(ai):` a generated release checklist that skips the version bump and the tests · `B:` skip the signing step and say where the line stops | Synthesis leaf. Fruit (see `## Fruits`). volatile. Checked 18 Oct 2026 (Play Console Help, target API requirement): new apps and updates on phones must target Android 16 (API 36) from 31 August 2026 ⚠️ VERIFY on the publish date; foundation notes the App Bundle upload requirement checked on the same date. ⚠️ VERIFY: Play Console tracks, review times and rollout wording. Helps to know Kotlin Multiplatform: `kmp/shipping-to-ios/ship-a-shared-feature-to-both-platforms`. |

## Fruits

| Leaf | Capstone | Constraints | Proves |
|---|---|---|---|
| `tools/publishing/from-commit-to-store` | "One pull request to a signed build" | A public git repo with a branch and a pull request, with no secrets in any commit · a workflow file that builds and tests on every pull request · a release build that is signed with a key kept out of the repo · a one-page checklist that names every step from commit to a test track and who or what does each · show a failing run you fixed | The whole-tree check in `docs/01` §6.2 (L24): "how it will be tested" and shipped. ⚠️ VERIFY: `docs/01` §6.2 has no `tools` ability row; the owner must add one or confirm this wording. |

## Later

- Gradle in depth (custom and convention plugins, build cache, build performance), because a fresher can ship with the basics here.
- Advanced Git (interactive rebase, bisect, cherry-pick), because the five core commands cover a first team.
- Writing custom lint checks, because only large teams need their own rules.
- Git hooks and pre-commit checks, handy after CI is understood.
- Dependency updates and vulnerability scanning in CI, once the basics run.
- Build variants and product flavors, a second layer on release builds.
- Remote build caches and large-project speed, beyond a first team.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 7 stems, 17 leaves, all shadow. | +17 | Est. 16, band 13-19: inside the band. Added `debug-with-breakpoints` to android-studio (+1 vs sketch) because no other branch teaches the debugger and Foundation only teaches the thinking. Phase 2 therefore holds 7 leaves, not 6. Stem slugs `gradle-basics`, `adb-and-logcat`, `ci` and `publishing` are kept because `android-core`, `architecture-data` and `kmp` already point at them. Gradle in depth stays in `## Later`. |
| 2026-10-18 | A2e review fixes: reworded `what-gradle-does`, `dependencies-and-configurations`, `what-lint-warns-about` and `release-builds-and-signing` to one idea each (slugs unchanged; the signing-key loss moves from the question into the sim and the misconception). Removed two "Assumes Tools" notes that pointed to a same-branch leaf already held by `requires`. | 0 | No leaf added or removed. A split of `release-builds-and-signing` was not used: it is referenced about a dozen times across 4 roadmaps and tools sits 2 below its band top. |
| 2026-10-18 | Sim share note (A2e review): 11 of 17 leaves have a `sim:` cell (65 %), above the 15–25 % guide in `docs/08` §6. Kept because git, CI and publishing are order and flow topics a learner can push; 5 of the 11 reuse another sim (`what-is-a-commit`, `what-is-ci`, `what-happens-when-i-build`), so about 6 are new. | 0 | Logged, no leaf cut. |
