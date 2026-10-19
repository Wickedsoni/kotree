# A2e review: content/*/_roadmap.md (17 files)

Reviewer: independent (did not write the roadmaps). Date: 2026-10-18. Rules used: `docs/00-decisions.md` §8.1 (wins), `docs/08-content-roadmap.md` §2-§14. No roadmap was edited. Script and data: scratchpad `chk.py` (YAML + table parser, run with Python 3.14 and PyYAML).

VERDICT: FIX (no blocking structural defect; 14 small or owner-level items, none higher than medium)

## Mechanical results (all 17 files)

| Check | Result |
|---|---|
| (a) YAML leaves = table rows, same stem, same order | PASS in all 17 (481 rows, 0 mismatches). Every stem has `Flags: phase=n`; all 8 cells filled in every row; table `Diff` = YAML `difficulty` in all rows. |
| (b) `requires` same branch, exists, earlier | PASS (0 violations; max 4 requires per leaf). |
| (c) kebab-case, unique slugs per branch | PASS (stems and leaves). First block is ```yaml with only the §8.1 keys; `branch`/`bough` match `docs/00` §5; section order and headings exact. |
| (d) Assumes / Helps-to-know IDs exist; phase rule; no cycle | PASS. 303 `Assumes` edges checked: every full ID exists, 0 phase-rule violations (§7.1 incl. tie-break order), 0 cycles in the leaf graph (same-branch `requires` + `Assumes`). Branch-level Assumes bullets that point to a later phase are scoped to later stems ("used by ...") and are legal. Foundation has no Assumes. |
| (e) difficulty vs 4-question score | NOT MECHANICALLY CHECKABLE: no roadmap shows a per-leaf score. Mix check only: Foundation 24 easy / 10 medium / 0 hard (71 % easy, no hard: PASS). No branch above 30 % hard (max concurrency 25 %). Branch mixes are below the 40/45/15 guide on easy but are explained in the log for concurrency, architecture-data, daa, profiling, os, testing. Logged mixes match the files for concurrency, os, networking, architecture-data, testing, profiling; daa does not (item 1). |
| (h) every status is shadow | PASS. No `status:`, `live` or `upnext` value in any file; all "All N leaves are shadow" counts equal the real counts. |
| (j) `## Later` | PASS: bullets only, 5-8 per branch, none overlaps a YAML leaf, not counted below. Two cosmetic breaches (item 9). |
| (g) emulator/simulator wording | PASS for site claims: every hit that is a site statement is a negation ("no Android emulator", L15, D8). Real-device uses (misconceptions "needs an emulator", iPhone simulator in kmp) see items 7 and 8. Sim kind: see items 5 and 6. |

## True counts (leaves in YAML, `## Later` excluded)

| Branch | Leaves | Stems | Est. (band) | Fruits | easy / med / hard |
|---|---|---|---|---|---|
| foundation | 34 | 6 | 32 (26-38) | 0 | 24 / 10 / 0 |
| kotlin | 65 | 15 | 60 (48-72) | 2 | 29 / 29 / 7 |
| android-core | 37 | 12 | 34 (27-41) | 2 | 11 / 21 / 5 |
| ui | 49 | 14 | 44 (35-53) | 2 | 16 / 26 / 7 |
| concurrency | 32 | 13 | 28 (22-34) | 1 | 4 / 20 / 8 |
| architecture-data | 33 | 12 | 30 (24-36) | 1 | 8 / 17 / 8 |
| app-networking | 16 | 9 | 16 (13-19) | 1 | 5 / 8 / 3 |
| kmp | 22 | 10 | 22 (18-26) | 1 | 7 / 12 / 3 |
| dsa | 46 | 16 | 46 (37-55) | 2 | 16 / 24 / 6 |
| daa | 22 | 9 | 22 (18-26) | 1 | 5 / 12 / 5 |
| os | 23 | 10 | 22 (18-26) | 1 | 8 / 11 / 4 |
| networking | 21 | 10 | 20 (16-24) | 1 | 9 / 9 / 3 |
| testing | 22 | 11 | 20 (16-24) | 1 | 7 / 12 / 3 |
| profiling | 16 | 10 | 14 (11-17) | 1 | 3 / 10 / 3 |
| native-ndk | 11 | 6 | 10 (8-12) | 1 | 3 / 6 / 2 |
| security | 15 | 10 | 14 (11-17) | 1 | 5 / 7 / 3 |
| tools | 17 | 7 | 16 (13-19) | 1 | 6 / 7 / 4 |
| **Total** | **481** | 180 | 450 (360-540) | **20** | 166 / 241 / 74 |

Every branch is inside its R2 band. The true fruit count is **20** (F6 and `docs/08` §12 say about 22; the earlier reviewer's 20 is correct). Every non-Foundation branch has its fruit on the last leaf (F2); 4 branches have a second fruit (kotlin 44 of 65, dsa 22 of 46, android-core 13 of 37, ui 26 of 49), each at least 15 leaves from the other (F3) and on the last leaf of a stem. Sims: 276 of 481 leaves have a `sim:` cell (about 57 %), 55 of them `reuse`, so about 221 new sims for `docs/05` workload; `none:` 205.

## FIX list

1. `content/daa/_roadmap.md` line 279 (Roadmap changes, difficulty mix): says 5 easy / 13 medium / 4 hard; the YAML has 5 / 12 / 5. Fix the numbers (or the difficulty of one leaf). Mechanical (`fixer`); severity low. If a leaf difficulty is wrong, owner.
2. `docs/08-content-roadmap.md` §9 table total, §10.1 phase table, §13 cadence: real total is 481, which is 6.9 % above 450 (more than the 5 % trigger in §9). Update the total and cadence (about 160 weeks at 3 a week). Per-branch reals are in the table above. Mechanical (`housekeeper`); severity medium. Same for F6 and §12: change "about 22 fruits" to 20.
3. Fruit rows with 7 constraints; F5 and `docs/08` §2 allow 3-6: `content/dsa/_roadmap.md` line 483, `content/networking/_roadmap.md` line 281, `content/app-networking/_roadmap.md` line 246. Merge or drop one constraint each. Needs `curriculum-designer` (content decision); severity low.
4. Titles that fail the split test (one idea) or the one-sentence question rule (`docs/08` §3 test 1, `docs/01` P7). Sample of 40 leaves (every 12th, offset 5): 5 failed, plus other offenders from the full scan below. All 481 titles are questions (0 missing "?") and no banned or jargon word is used as a command ("simply", "obviously" etc. none; the two "just" hits, dsa `what-is-a-graph` and daa `loop-invariants`, are not the condescending use). Failures in the sample: `dsa/sorting/what-sorting-means` (sorted order and equal items), `android-core/activities/what-is-context` (what it is and which to pass), `app-networking/errors-and-retries/timeouts-and-cancellation` (two mechanisms; the id even says "and"), `architecture-data/layers-and-mapping/layers-and-their-jobs` (jobs and may I skip), `architecture-data/key-value-storage/reading-old-sharedpreferences-code` (what it is and why tutorials use it). Other likely offenders in the full list of 112 titles with "and"/"vs": `tools/gradle-basics/what-gradle-does`, `tools/gradle-basics/dependencies-and-configurations`, `tools/lint-and-static-analysis/what-lint-warns-about`, `tools/publishing/release-builds-and-signing`, `dsa/queues/what-is-a-queue`, `dsa/graphs/what-is-a-graph`, `architecture-data/databases/rows-tables-and-keys`, `architecture-data/databases/room-entities-and-daos`, `app-networking/errors-and-retries/retry-with-backoff`, `app-networking/errors-and-retries/kinds-of-failure`, `kmp/compose-multiplatform/shared-ui-screen`, `kmp/swift-interop/calling-kotlin-from-swift`, `kmp/shared-networking/a-shared-api-client`, `kmp/shared-networking/ktor-plugins`, `security/threat-thinking/who-would-attack-my-app`, `security/privacy-and-permissions/ask-for-less-data`, `security/privacy-and-permissions/tell-users-what-you-collect`, `native-ndk/what-is-native/what-is-native-code`, `native-ndk/art-and-dex/what-is-dex`. Reword to one question or split. Needs `curriculum-designer` (a split changes the leaf count, R5); severity medium. Reword-only cases can be done by `fixer` once the owner picks the wording. Not flagged: `kotlin/null-safety/what-is-null` (it is the title used in `docs/00` §8.1) and synthesis leaves.
5. Android-only behaviour sims without `code-to-screen` (`docs/08` §6 and `docs/05` §4, which lists lifecycle and process behaviour as `code-to-screen`): `content/android-core/_roadmap.md` rows `who-starts-your-app` (271), `back-stack` (281), `what-is-the-lifecycle` (289), `lifecycle-callbacks-order` (290, reuse), `what-is-process-death` (309), `why-background-is-limited` (350), `cold-warm-hot-start` (382), `from-tap-to-first-frame` (383) use a plain `sim:` with no kind. Write `sim: code-to-screen <idea>`. Mechanical once the owner confirms the kind for each (some may be `animation`); severity medium.
6. `content/profiling/_roadmap.md` rows `read-a-profiler-screen` (190), `find-a-leak` (208), `frame-budget` (216), `baseline-profiles` (236) use `sim: animation` for behaviour on an Android phone (rotating the phone, two phones at first launch). Confirm with `physics-designer` whether these are `animation` (step-by-step, no Android screen) or `code-to-screen`. Owner; severity low.
7. `content/kmp/_roadmap.md` lines 211, 230, 305, 311 use "iPhone simulator". It is the real Xcode tool, not a site claim, but `docs/08` §6 says the word "sim" never means simulator and the review checklist forbids emulator/simulator wording. Reword to "run on an iPhone or the Xcode test device". Owner (wording choice); severity low. Likewise `content/testing/_roadmap.md` lines 259 and 268 quote the misconceptions "needs an emulator or a phone": acceptable (real devices), no change needed.
8. Sim share far above the `docs/08` §6 guide with no R5 log entry: dsa 100 %, daa 100 %, foundation 68 %, tools 65 %, security 80 %, native-ndk 64 % (guide 35-50 % for dsa/daa, 15-25 % elsewhere). testing, profiling, os, concurrency and others already explain theirs. Add one log line each, or cut sims. Owner (workload for `docs/05`); severity low.
9. `## Later` hygiene (§2 row 5: no backticked slugs): `content/os/_roadmap.md` line 309 (`malloc`), `content/testing/_roadmap.md` line 315 (`tests-that-always-pass`, a real leaf slug; this can confuse the V6 script). Remove the backticks. Mechanical; severity low.
10. `content/tools/_roadmap.md` lines 198 and 206: "Assumes Tools:" points to a same-branch leaf (`tools/gradle-basics/what-gradle-does`); the keyword is for cross-branch targets and `requires` already holds it. Delete the note or write "Needs". Mechanical; severity low.
11. Branch names in `Assumes` / `Helps to know` bullets and notes are short forms ("Kotlin", "OS", "DSA", "UI", "DAA", "KMP", "Concurrency", "Android core", "Tools", "Networking", "App networking", "Native layer") instead of the `docs/00` §5 display names (§7.1 says display names). IDs are full and resolve, so no script breaks. Either allow short names in `docs/08` §7.1 or replace them. Mechanical but large; severity low (suggest allowing short names in the doc).
12. Volatile stems with leaves that carry neither `⚠️ VERIFY` nor a dated check (§8 rule 1, §14 checklist): 20 leaves, e.g. `android-core/manifest-and-resources` (what-is-the-manifest, what-are-resources, resource-qualifiers), `android-core/app-startup` (what-is-the-application-class, from-tap-to-first-frame), `architecture-data/dependency-injection` (what-is-a-dependency, constructor-injection), `ui/forms-and-input` (validating-input, choice-controls), `kmp/source-sets/what-common-code-cannot-use`, `native-ndk/native-memory/native-memory-has-no-gc`, `testing/ui-tests/test-a-screen-with-fake-state`, `app-networking/sync/keeping-phone-and-server-in-step` and 11 others. Add a one-line `⚠️ VERIFY` note. `curriculum-designer`; severity low (every leaf is rechecked before live anyway, P9).
13. Fact not verified: `content/security/_roadmap.md` line 176, "certificate transparency opt-in on Android 16 and on by default from Android 17". The Android 17 behaviour-changes page I read does not mention it; it may sit on the network-security-config page. The roadmap already marks it VERIFY; leave the flag. Owner; severity low.
14. `docs/08` §10.1 "Stems opened" leaf counts no longer match the real roadmaps (for example Foundation 34, not 32; `kotlin` 65, not 60). Folded into item 2. Mechanical (`housekeeper`).

## Fact spot-check (11 official-doc checks by web search, all on 18 Oct 2026)

Verified correct against the cited official page:
1. ANR input timeout 5 s with a foreground activity (developer.android.com/topic/performance/vitals/anr).
2. 16 KB page size: apps targeting API 35+, 64-bit devices, updates blocked from 1 Feb 2027 (developer.android.com/guide/practices/page-sizes).
3. Apps targeting API 36 cannot opt out of edge-to-edge on Android 16 (behavior-changes-16).
4. Predictive back animations on by default and `onBackPressed` not called for apps targeting API 36 (behavior-changes-16).
5. Orientation/resizability restrictions ignored on displays of sw600dp or wider when targeting API 36, opt-out ends at API 37 (behavior-changes-16).
6. Jetpack Navigation 3 1.0 stable, Android Developers blog, 19 Nov 2025.
7. `dataSync` foreground services: 6 hours in 24, `onTimeout` (Android 15 fg-service-timeout).
8. Frame budget: 16 ms at 60 fps, 11 ms at 90, 8 ms at 120; slow 16-700 ms, frozen over 700 ms (topic/performance/vitals/render). Note: that page's own table says frozen frames are "between 700 ms and 5 s"; the roadmap's wording is consistent with the definition.
9. Cleartext traffic allowed by default up to API 27, blocked from API 28 (privacy-and-security/security-config).
10. Expected and actual classes are Beta; compiler flag `-Xexpect-actual-classes` silences the warning (kotlinlang.org multiplatform-expect-actual).
11. Compose Multiplatform for iOS announced Stable with 1.8.0 (JetBrains blog, May 2025).
12. Android 17: keyboard visibility not restored after a configuration change; automatic URI grants for `ACTION_SEND`/`ACTION_IMAGE_CAPTURE` end in Android 18 (behavior-changes-all, version 17).

Could not verify: item 13 above (certificate transparency default on Android 17). Facts the roadmaps only mark `⚠️ VERIFY` (Play signing, Credential Manager, Hilt/Room versions, baseline profiles, notification permission on Android 13) were not tested here and stay as VERIFY.
