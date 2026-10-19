# My timeline (author's study plan)

> Personal planning aid, not a spec. Numbers come from `docs/08` §10 and §13 (482 leaves, 3 leaves a week). `docs/00-decisions.md` wins any conflict. Week numbers are counted from your start date; add one rest week per quarter (about 3 years 4 months in total).

Mix after Phase 1: about 2 app-track leaves plus 1 CS-theory leaf per week (`docs/08` §10.2). A leaf is only eligible when its `requires` are live.

## Phase overview

| Phase | Name | Leaves | Cumulative | Weeks (3 a week) | Approx. calendar from start |
|---|---|---|---|---|---|
| 1 | Roots | 34 | 34 | 1-11 | months 0-2.6 |
| 2 | Speak Kotlin | 60 | 94 | 12-31 | months 2.6-7 |
| 3 | First real app | 84 | 178 | 32-59 | months 7-14 |
| 4 | Apps that wait, remember and talk | 121 | 299 | 60-100 | months 14-23 |
| 5 | Prove and polish | 125 | 424 | 101-141 | months 23-33 |
| 6 | Everywhere and underneath | 58 | 482 | 142-161 | months 33-37 |

## Phase 1: Roots (weeks 1-11)

- [ ] foundation: how-computers-work (6)
- [ ] foundation: first-code (3)
- [ ] foundation: programming-thinking (12)
- [ ] foundation: debugging-mindset (4)
- [ ] foundation: from-code-to-running (4)
- [ ] foundation: how-an-app-is-born (5)

Finale: run your first app on a phone. No fruit in this branch.

## Phase 2: Speak Kotlin (weeks 12-31)

- [ ] kotlin: first-programs, types-and-variables, control-flow, functions, null-safety, strings, collections, lambdas, classes-and-objects (44)
- [ ] tools: git, android-studio (7)
- [ ] dsa (theory): complexity, arrays, strings (9)

## Phase 3: First real app (weeks 32-59)

- [ ] kotlin: interfaces-and-inheritance, special-classes, generics, extensions-scope-and-delegates, errors-and-results, reading-code (21)
- [ ] os (theory): what-an-os-does, processes, memory, threads (11)
- [ ] dsa (theory): linked-lists, stacks, queues, hashing, recursion (13)
- [ ] android-core: what-is-an-app, activities, lifecycle, configuration-changes, process-death (13)
- [ ] ui: first-composable, modifiers, layouts, state-and-recomposition, lists, theming, navigation (26)

Milestone: first real app (about 1 year 2 months).

## Phase 4: Apps that wait, remember and talk (weeks 60-100)

- [ ] os (theory): scheduling, synchronisation, deadlock (6)
- [ ] networking (theory): how-the-internet-works, layers, ip-and-dns, tcp-and-udp, http, tls, mobile-networks-and-latency, api-styles, websockets, putting-it-together (21)
- [ ] dsa (theory): sorting, searching, trees, bst (12)
- [ ] concurrency: why-the-main-thread, threads, coroutine-basics, suspend, dispatchers, scopes, cancellation-and-errors, flow-basics, flow-operators, state-and-shared-flow, channels, shared-mutable-state, putting-it-together (32)
- [ ] architecture-data: why-architecture, viewmodel, ui-state-and-udf, repositories, dependency-injection, key-value-storage, databases, offline-first, paging, modules, layers-and-mapping, putting-it-together (33)
- [ ] app-networking: requests-from-an-app, http-client, json, errors-and-retries, auth-tokens, caching, images, real-time, sync (17)

Milestone: an app that loads, stores and shows data properly (about 1 year 11 months).

## Phase 5: Prove and polish (weeks 101-141)

- [ ] tools: gradle-basics, adb-and-logcat, lint-and-static-analysis (6)
- [ ] dsa (theory): heaps, graphs, tries, patterns (12)
- [ ] android-core: manifest-and-resources, intents, permissions, background-work, services-and-notifications, files-and-sharing, app-startup (24)
- [ ] ui: side-effects, forms-and-input, animation, accessibility, adaptive-layouts, views-and-interop, custom-drawing (23)
- [ ] testing: why-test, unit-tests, assertions, test-doubles, testing-coroutines, testing-viewmodels, device-tests, ui-tests, screenshot-tests, flaky-tests, putting-it-together (22)
- [ ] daa (theory): analysis, recurrences, divide-and-conquer, greedy, dynamic-programming, backtracking, graph-algorithms, hard-problems, choosing-an-approach (22)
- [ ] profiling: measure-first, profiler-tour, cpu, memory-and-leaks, jank-and-frames, compose-performance, startup, battery-and-network, app-size, putting-it-together (16)

## Phase 6: Everywhere and underneath (weeks 142-161)

- [ ] os (theory): files, android-os, putting-it-together (6)
- [ ] tools: ci, publishing (4)
- [ ] kmp: what-is-kmp, source-sets, expect-actual, shared-logic, shared-networking, shared-storage, compose-multiplatform, swift-interop, libraries, shipping-to-ios (22)
- [ ] security: threat-thinking, secure-storage, network-security, auth, privacy-and-permissions, obfuscation, webviews, app-integrity, secrets-and-logs, putting-it-together (15)
- [ ] native-ndk: what-is-native, art-and-dex, jni-basics, ndk-build, native-memory, when-to-go-native (11)

## Notes

- Branches are open; this order is a recommendation. The stem names above are the real IDs from `content/<branch>/_roadmap.md`.
- Leaf counts per phase are rounded from the roadmaps; the exact leaf list is in each `_roadmap.md`.
- KMP iOS leaves need a Mac or free GitHub Actions `macos-latest` as evidence (`docs/08` §8.1).
- The site is not needed to study. Leaves go live only after you say "I understood X".
