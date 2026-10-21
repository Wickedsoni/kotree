# interview: tools

## tools/git/what-is-a-commit
interview-questions:
- [intern] What is a Git commit, and what does it represent in your project's history?
- [junior] How do you stage changes (`git add`) and record a commit (`git commit -m`) with a descriptive message?
- [mid] Why are atomic, single-purpose commits with clear messages critical for code reviews and git history hygiene?
- [senior] What goes wrong in production when developers push massive, unfocused commits containing unrelated changes bundled together?
what-interviewers-listen-for:
- A commit is a cryptographic snapshot of your project state at a specific point in time.
- Atomic commits isolate features or bug fixes, making `git bisect` and code reviews straightforward.
- Messy, unfocused commits obscure change intent and make rollbacks hazardous.
build-exercise: Stage a single modified file in a Git repository, commit it with an atomic message, and inspect the commit hash via `git log`.
official-references:
- Git documentation | https://git-scm.com/doc
common-wrong-beliefs:
- A Git commit is just a diff patch file recording line insertions and deletions | A Git commit is a snapshot of the entire project file tree state, referencing parent commit snapshots.

## tools/git/branches
interview-questions:
- [intern] What is a Git branch, and how does it allow you to work on new features without breaking your working code?
- [junior] How do you create, switch, and merge branches (`git checkout`/`git switch`, `git merge`)?
- [mid] What is the difference between merging a branch (`git merge`) and rebasing (`git rebase`)?
- [senior] What goes wrong in production when rebasing shared public feature branches that other team members are collaborating on?
what-interviewers-listen-for:
- Branches are lightweight movable pointers to specific commits.
- Rebasing rewrites commit history for a linear trail, while merging preserves branch merge history.
- Rebasing shared remote branches destroys teammates' commit histories and causes severe merge conflicts.
build-exercise: Create a new feature branch, make a commit, switch back to main, and merge the feature branch using fast-forward or merge commit.
official-references:
- Git branching | https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging
common-wrong-beliefs:
- Rebasing shared branches that have already been pushed to a remote repository is a great way to keep team collaboration clean | Rebasing rewrites history hashes, destroying remote synchronization and breaking teammates working on the same branch.

## tools/git/merge-conflicts
interview-questions:
- [intern] What is a Git merge conflict, and when does it occur?
- [junior] How do you read and resolve conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`) in source code files?
- [mid] How do modern IDEs (Android Studio / IntelliJ) simplify three-way merge conflict resolution?
- [senior] What goes wrong in production when developers blindly accept "theirs" or "ours" during conflict resolution without verifying business logic integrity?
what-interviewers-listen-for:
- Conflicts occur when two branches modify the exact same lines of code in conflicting ways, requiring manual human resolution.
- Blindly accepting incoming or current changes risks wiping out critical bug fixes or business logic.
build-exercise: Create a conflicting edit on the same line in two branches, attempt a merge, examine the conflict markers in Android Studio, and resolve them.
official-references:
- Basic merge conflicts | https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging#_basic_merge_conflicts
common-wrong-beliefs:
- Git can automatically merge any conflicting code modifications as long as the file is valid syntax | Git cannot resolve semantic conflicts when identical lines are modified differently in two branches.

## tools/git/remotes-and-pull-requests
interview-questions:
- [intern] What is a remote repository (like GitHub or GitLab), and how do `git push` and `git pull` interact with it?
- [junior] What is a Pull Request (PR) / Merge Request (MR), and why is it the core artifact of code review?
- [mid] How do code reviews, status checks, and branch protection rules enforce code quality before merging into main?
- [senior] What goes wrong in production when PRs bypass code reviews or CI checks, pushing untested code directly into production branches?
what-interviewers-listen-for:
- Remotes store shared repository copies; PRs facilitate peer code review, automated testing, and discussion.
- Branch protection rules prevent direct pushes to main without passing CI and review approvals.
build-exercise: Push a feature branch to GitHub, open a Pull Request, and check automated CI status workflows.
official-references:
- GitHub Pull Requests | https://docs.github.com/en/pull-requests
common-wrong-beliefs:
- Pull requests are just a bureaucratic formality to slow down code delivery | PRs are essential quality gates enabling peer review, automated testing, and collective code ownership.

## tools/android-studio/find-your-way-around-studio
interview-questions:
- [intern] Where are your source code files, build outputs, and run buttons located in Android Studio?
- [junior] What is the Project tool window (`Android` view vs `Project` view)?
- [mid] How do you use tool windows like Logcat, Build Output, Device File Explorer, and Run efficiently?
- [senior] What goes wrong in production when developers misunderstand module file structures or build artifact paths?
what-interviewers-listen-for:
- Android Studio (built on IntelliJ IDEA) provides integrated tool windows for building, debugging, and inspecting Android apps.
- The `Android` project view simplifies module organization, while the `Project` view shows physical directory layouts.
build-exercise: Switch between Android view and Project view in Android Studio's left sidebar, and locate `build.gradle.kts` and `AndroidManifest.xml`.
official-references:
- Meet Android Studio | https://developer.android.com/studio/intro
common-wrong-beliefs:
- Android Studio's `Android` project view shows the exact physical directory layout of files on your hard drive | The `Android` view is a virtualized grouping of source files; the `Project` view shows the true physical directory structure.

## tools/android-studio/navigate-and-refactor
interview-questions:
- [intern] How do you quickly jump to a file, symbol, or line in Android Studio using keyboard shortcuts (`Shift+Shift`, `Cmd/Ctrl+N`)?
- [junior] How do you find usages of a function or class across the entire project (`Alt+F7` / `Cmd+B`)?
- [mid] Why is using Android Studio's safe refactoring tools (`Shift+F6` for rename) vastly superior to manual text search-and-replace?
- [senior] What goes wrong in production when manual string replacement renames public library symbols or XML resource IDs incorrectly?
what-interviewers-listen-for:
- Navigation shortcuts (`Shift+Shift` everywhere search) accelerate code discovery.
- Safe refactoring updates references across Kotlin, XML, and Java files automatically without breaking bindings.
build-exercise: Use `Shift+Shift` to search for an Activity class, and use safe rename (`Shift+F6`) to rename a function across multiple files.
official-references:
- Navigate your source code | https://developer.android.com/studio/write/code-editor#nav
- Refactor code | https://developer.android.com/studio/write/refactor
common-wrong-beliefs:
- Using global text search and replace (`Ctrl+Shift+R`) is just as safe as Android Studio's refactor-rename tool | Global text replace misses XML attribute references, serialized names, and reflection strings, breaking app compilation.

## tools/android-studio/debug-with-breakpoints
interview-questions:
- [intern] How do you set a breakpoint in Android Studio and pause app execution on a specific line of code?
- [junior] How do you inspect local variables, evaluate expressions, and step through code (`F8` step over, `F7` step into)?
- [mid] What are conditional breakpoints and exception breakpoints, and how do they stop execution only when specific criteria are met?
- [senior] What goes wrong in production and debugging sessions when multi-threaded coroutine debugging lacks proper coroutine debugger inspection support?
what-interviewers-listen-for:
- Breakpoints pause execution to inspect live variable states and memory objects.
- Conditional breakpoints prevent stopping on every iteration of a loop by firing only when a condition is true.
- Coroutine debugger tracks coroutine suspension states across thread jumps.
build-exercise: Set a conditional breakpoint inside a loop that pauses execution only when an iterator index equals 5, and inspect local variables.
official-references:
- Debug your app | https://developer.android.com/studio/debug
common-wrong-beliefs:
- Debugging complex multi-threaded asynchronous code requires scattering `println` statements everywhere | Android Studio's interactive debugger with conditional and coroutine breakpoints allows pausing and inspecting runtime state precisely.

## tools/gradle-basics/what-gradle-does
interview-questions:
- [intern] What is Gradle, and what happens when you press the "Sync Project with Gradle Files" button in Android Studio?
- [junior] What are the phases of a Gradle build lifecycle (Initialization, Configuration, Execution)?
- [mid] How does Gradle dependency caching and incremental compilation speed up build times?
- [senior] What goes wrong in production when custom Gradle build script logic executes expensive I/O operations during the Configuration phase, slowing down every build?
what-interviewers-listen-for:
- Gradle is an advanced build automation system managing dependency resolution, compilation tasks, and packaging.
- Three build phases: Initialization (projects/modules parsed), Configuration (task graphs built), Execution (tasks run).
- Heavy I/O during configuration slows down project syncs across the team.
build-exercise: Run a Gradle build task with performance profiling flags (`--profile` or `--scan`) from the terminal and inspect task execution times.
official-references:
- Gradle build lifecycle | https://docs.gradle.org/current/userguide/build_lifecycle.html
common-wrong-beliefs:
- Gradle is exclusively a Java and Android build tool | Gradle is a general-purpose build automation tool supporting Kotlin, Java, C++, Python, Swift, and web projects.

## tools/gradle-basics/dependencies-and-configurations
interview-questions:
- [intern] How do you declare library dependencies in Gradle build files (`implementation`, `api`, `kapt`/`ksp`, `testImplementation`)?
- [junior] What is the difference between `implementation` and `api` dependencies regarding transitive module exposure?
- [mid] Why does `api` cause wider module recompilation cascades across multi-module projects compared to `implementation`?
- [senior] What goes wrong in production when circular project dependencies or conflicting transitive library versions bloat APK size and cause duplicate class errors?
what-interviewers-listen-for:
- `implementation` restricts dependency visibility to the current module; `api` exposes transitive dependencies to consumers.
- Overusing `api` breaks modularization encapsulation and slows incremental build compilation speeds.
build-exercise: Declare a dependency using `implementation` vs `api` in a multi-module project and observe compile-scope visibility differences.
official-references:
- Add build dependencies | https://developer.android.com/build/dependencies
common-wrong-beliefs:
- Using `api` for every single library dependency in your Gradle files is safer and simpler than `implementation` | Using `api` unnecessarily exposes internal dependencies, forcing dependent modules to recompile whenever the library updates and slowing build speeds.

## tools/gradle-basics/version-catalogs
interview-questions:
- [intern] What is a Gradle Version Catalog (`libs.versions.toml`), and what problem does it solve?
- [junior] How do you declare library coordinates, versions, and plugins in a TOML version catalog file?
- [mid] How do version catalogs provide type-safe dependency accessors in Kotlin DSL build files (`libs.androidx.core.ktx`)?
- [senior] What goes wrong in production when large enterprise multi-module projects lack centralized version catalogs, causing version drift and dependency conflicts?
what-interviewers-listen-for:
- Version catalogs centralize dependency versions in a single `libs.versions.toml` file, ensuring consistency across multi-module projects.
- Type-safe accessors prevent typos and mismatched library versions.
build-exercise: Create a `libs.versions.toml` file, declare a library dependency version, and reference it in a module `build.gradle.kts` file.
official-references:
- Migrate to version catalogs | https://developer.android.com/build/migrate-to-version-catalogs
common-wrong-beliefs:
- Hardcoding string-based dependency coordinates directly inside every individual module's build.gradle file is recommended for project isolation | Centralizing dependencies via Version Catalogs (`libs.versions.toml`) prevents version mismatch bugs across modules.

## tools/adb-and-logcat/adb-basics
interview-questions:
- [intern] What is ADB (Android Debug Bridge), and how do you use it to communicate with a connected phone or emulator?
- [junior] What are essential everyday ADB commands (`adb devices`, `adb install`, `adb logcat`, `adb shell`, `adb forward`)?
- [mid] How do you forward network ports from an emulator to your local machine using ADB (`adb reverse` / `adb forward`)?
- [senior] What goes wrong in production when forgetting to strip ADB debugging permissions or debuggable flags in release builds?
what-interviewers-listen-for:
- ADB is a versatile command-line tool bridging your computer to device daemons for installation, debugging, and shell access.
- Port forwarding enables testing local backend APIs from an Android emulator.
build-exercise: Run `adb devices` in your terminal to list connected devices, and install an APK file using `adb install path/to/app.apk`.
official-references:
- Android Debug Bridge (ADB) | https://developer.android.com/studio/command-line/adb
common-wrong-beliefs:
- ADB commands can only be executed when you have a physical USB cable plugged into an Android phone | ADB can connect wirelessly over Wi-Fi network debugging.

## tools/adb-and-logcat/reading-logcat
interview-questions:
- [intern] What is Logcat, and how do you view system and app log messages in Android Studio?
- [junior] What are the log priority levels (`Verbose`, `Debug`, `Info`, `Warn`, `Error`, `Assert`), and when should each be used?
- [mid] How do you use log filters, package tags, and regex expressions to isolate specific app crashes from thousands of system log lines?
- [senior] What goes wrong in production when leaving verbose debug logging enabled in production release builds (performance overhead and data leaks)?
what-interviewers-listen-for:
- Logcat captures system and app logs streams.
- Filtering by tag and log level (`package:mine level:error`) isolates relevant application events.
- Production builds must strip verbose logs using ProGuard/R8 rules.
build-exercise: Write a custom `Log.d("MY_TAG", "Debug message")` statement, run the app, and filter Logcat in Android Studio by `tag:MY_TAG`.
official-references:
- Write and view logs with Logcat | https://developer.android.com/studio/debug/logcat
common-wrong-beliefs:
- Leaving high-volume debug log statements in production release builds has zero impact on app performance | Printing thousands of lines to Logcat consumes CPU cycles and increases garbage collection overhead.

## tools/lint-and-static-analysis/what-lint-warns-about
interview-questions:
- [intern] What is Android Lint, and how does it catch bugs and style violations before you run your app?
- [junior] What types of checks does Lint perform (accessibility, internationalization, performance, security, correctness)?
- [mid] How do you run Lint checks via Gradle (`./gradlew lint`) and interpret HTML/XML lint reports?
- [senior] What goes wrong in production when ignoring Lint warnings or suppressing critical security and lifecycle checks indiscriminately?
what-interviewers-listen-for:
- Lint statically analyzes code structures, XML layouts, and manifests to flag potential bugs, memory leaks, and accessibility flaws.
- Running lint checks in CI prevents shipping code with known performance or security defects.
build-exercise: Run `./gradlew lint` in your terminal, open the generated HTML lint report, and inspect flagged warnings.
official-references:
- Improve your code with lint checks | https://developer.android.com/studio/write/lint
common-wrong-beliefs:
- Android Lint only checks Kotlin and Java source code files | Lint analyzes all project assets, including XML layout files, AndroidManifest.xml, resource strings, and Gradle build files.

## tools/ci/what-is-ci
interview-questions:
- [intern] What is Continuous Integration (CI), and what happens automatically when you push code to a shared repository?
- [junior] How do CI platforms (like GitHub Actions, GitLab CI, Bitrise) automate building and testing on every pull request?
- [mid] Why is a fast, reliable CI pipeline critical for maintaining code quality across distributed engineering teams?
- [senior] What goes wrong in production when teams merge code without passing automated CI builds, leading to broken master branches?
what-interviewers-listen-for:
- CI automates building, linting, and running unit test suites on remote clean environments whenever code is pushed.
- Automated CI prevents broken builds and regressions from landing in production branches.
build-exercise: Create a simple GitHub Actions workflow YAML file (`.github/workflows/ci.yml`) that runs `./gradlew test` on every push.
official-references:
- GitHub Actions documentation | https://docs.github.com/en/actions
common-wrong-beliefs:
- Running automated test suites locally on your own laptop is sufficient to guarantee your code builds and passes tests on everyone else's machines | CI servers provide clean, standardized environments ensuring code builds correctly for all contributors.

## tools/ci/a-first-ci-workflow
interview-questions:
- [intern] What are the key sections of a CI workflow configuration file (triggers, jobs, steps, runners)?
- [junior] How do you configure a Gradle wrapper cache in GitHub Actions to speed up build times?
- [mid] How do you upload test reports, APK artifacts, and lint results from CI runners for inspection?
- [senior] What goes wrong in production when CI workflows fail to cache Gradle dependencies, causing slow builds and hitting rate limits?
what-interviewers-listen-for:
- Workflows define triggers (on pull_request), runners (ubuntu-latest), and sequential steps (checkout, setup JDK, run gradlew test).
- Caching Gradle dependencies (`actions/cache`) cuts CI build durations significantly.
build-exercise: Write a GitHub Actions workflow that checks out code, sets up Java 17, caches Gradle, and executes `./gradlew test`.
official-references:
- Automating builds with GitHub Actions | https://docs.github.com/en/actions/automating-builds-and-tests
common-wrong-beliefs:
- CI workflows download all required Gradle dependencies and Android SDK components from scratch on every single build step without caching, and it is fast | Without Gradle and SDK caching, CI builds take excessive time and risk network timeouts.

## tools/publishing/release-builds-and-signing
interview-questions:
- [intern] What is the difference between a Debug build and a Release build of an Android application?
- [junior] What is code signing, and why must every release APK/AAB be cryptographically signed with a release keystore before publishing?
- [mid] How do R8 code shrinking, obfuscation, and resource optimization secure and shrink release builds?
- [senior] What goes wrong in production when releasing an app signed with debug keys or failing to back up the release signing keystore?
what-interviewers-listen-for:
- Release builds enable R8 shrinking, remove debug flags, and require cryptographic signatures using private keystores.
- Losing release keystores permanently locks you out of publishing updates to existing app packages on Google Play.
build-exercise: Configure a release signing config in `build.gradle.kts` using keystore properties, and generate a signed release AAB.
official-references:
- Sign your app | https://developer.android.com/build/building-cmdline#sign_app
common-wrong-beliefs:
- You can publish updates to your app on Google Play using the default debug signing key generated automatically by Android Studio | Debug keys are insecure and rejected by Google Play; release builds require a secure private production keystore.

## tools/publishing/from-commit-to-store
interview-questions:
- [intern] Can you name every step in the pipeline from writing a git commit to an app update reaching a user's phone?
- [junior] What are the roles of Google Play Console, internal testing tracks, alpha/beta tracks, and staged rollouts?
- [mid] How do automated publishing tools (like Fastlane or Play Publisher Gradle Plugin) deploy builds directly to Google Play from CI?
- [senior] What goes wrong in production when pushing a 100% staged rollout of a buggy release without utilizing gradual staged rollout percentages?
what-interviewers-listen-for:
- Pipeline: Commit -> PR Review -> CI Build & Test -> Merge to Main -> Release Tag -> Artifact Signing -> Play Console Upload -> Staged Rollout.
- Staged rollouts limit blast radius if critical crashes slip into production releases.
build-exercise: Map out a complete CI/CD release pipeline diagram connecting GitHub Actions to Google Play Console publishing tracks.
official-references:
- Publish your app | https://developer.android.com/studio/publish
common-wrong-beliefs:
- Releasing a new app update should always be pushed to 100% of all users worldwide simultaneously on day one | Best practice dictates using staged rollouts (e.g., 1% -> 5% -> 20% -> 100%) to catch production crashes early with minimal user impact.

## Self-check
1. Git documentation | https://git-scm.com/doc (Verify URL path)
2. Git branching | https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging (Verify book URL)
3. GitHub Pull Requests | https://docs.github.com/en/pull-requests (Verify URL)
4. Meet Android Studio | https://developer.android.com/studio/intro (Verify URL path)
5. Gradle build lifecycle | https://docs.gradle.org/current/userguide/build_lifecycle.html (Verify Gradle user guide path)
6. Add build dependencies | https://developer.android.com/build/dependencies (Verify path)
7. Android Debug Bridge (ADB) | https://developer.android.com/studio/command-line/adb (Verify path)
8. Improve your code with lint checks | https://developer.android.com/studio/write/lint (Verify path)
9. GitHub Actions documentation | https://docs.github.com/en/actions (Verify URL)
10. Sign your app | https://developer.android.com/build/building-cmdline#sign_app (Verify anchors)