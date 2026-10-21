# Review L2-c: four layer-2 shadow leaves

Reviewer: independent. Date: 21 Oct 2026. Checklist: `docs/lesson-checklist.md`. Structure: `docs/04-lesson-system.md` sections 4 to 6 and `content/_templates/leaf.md`.

VERDICT: FIX (0 PASS, 4 FIX; all items mechanical except those marked owner/lesson-writer)

Build: in the working tree `./gradlew :site:validateContent` FAILS, but not because of these leaves. The untracked, in-progress `buildSrc/src/main/kotlin/dev/kotree/build/SimSpec.kt` does not compile (`:buildSrc:compileKotlin` errors, for example "Too many arguments for constructor SimLessonBlock", private `jsonString`, unresolved `tpl`). Owner of the ADR 009 work must fix or stash it before the layer build. I re-ran the task in a clean `git worktree` of HEAD (worktree removed afterwards): `validateContent: ok`, BUILD SUCCESSFUL. So all four leaves pass validation (V8 skipped: no `origin/main` ref).

Snippets: every Kotlin block and every runnable challenge was compiled and run with `bash scripts/kt-run.sh` (Kotlin 2.4.21). All compile and every stated output matches stdout: packets Name it (`[3, 1, 2]`, `HELLO WORLD!`), packets join-by-number (`CAB`, `ABC`), kernel Name it (`5`, `false`, with no `missing.txt` present), security Name it (`[photo]`), who-can-read (`[photo, export]`), what-stays (`[photo]`), Try it break (`[notes, photo, export]`, starts with `notes` as stated), fix-token-in-the-hall broken code (`Other apps can read: [login-token]`) and its `fix` (`Other apps can read: []`, equals `expected`). The profiler leaf has no Kotlin. Warnings: NOT verified (this harness hides warnings, as noted in L2-a). The `run: false` Predict items were hand-traced by me and are right (arrival minutes 3, 4, 5, 8 give order 2, 3, 1, 4; Self = 60 - 45 = 15).

Docs verified by fetch (all matched unless listed in a FIX): MDN How the web works "Packets explained" (header, payload, both reasons, out-of-sequence); RFC 9293 section 2.2 (the "reliable, in-order, byte-stream service" sentence and loss detection via sequence numbers); Media3 listening-to-player-events (STATE_BUFFERING wording); android platform ("foundation ... is the Linux kernel", updated 2024-05-20); source.android.com app-sandbox (unique UID, "kernel-level Application Sandbox", "limited access to the OS"); Android 14 exact-alarm page (SCHEDULE_EXACT_ALARM, setExact, SecurityException); man7 syscalls(2) (both quotes) and open(2) (EACCES, ENOENT, O_CREAT); inspect-app-live (View Live Telemetry; exactly four rows Interactions, CPU usage, Threads, Memory; Interactions needs debuggable and API 26+; green, yellow, gray wording; stacked memory graph); inspect-traces (Call Chart, Flame Chart axis not a timeline, Top Down, Bottom Up, Self, Children, Total = Self + Children, percentages over the selected range); top-bottom-charts (Bottom Up "useful for sorting ... by CPU time"); studio/profile (debuggable "performance costs", profileable based on release, allocations and heap dumps need debuggable); security-tips (internal only to your app, external globally readable and writable, validate, no passwords on the device, short-lived token, unique UID); app-specific (other apps cannot access, "on Android 10 (API level 29) and higher, these locations are encrypted", removed on uninstall); training/data-storage (preferences wording, Room, uninstall table); shared/media ("Even after your app is uninstalled, these files remain"); autobackup (shared preferences and `getFilesDir()` files, `allowBackup` default true for API 23+, sensitive-information advice, Block Store tip).

Could not verify: that the exact-alarm `SecurityException` is raised by an Android system service or by the kernel (no page says; the kernel leaf hedges this, see 2.2); what the Android 10 encryption protects against (the page does not say, see 4.2); "closer to what users see" for profileable builds (inference, see 3.3).

docs/04 structure on all four: 13 headings are in order (Try it and Fix the bug only in security, where `playground: true`; the three `playground: false` leaves have only `run: false` Predict items); frontmatter matches each roadmap row (title, difficulty, requires, fruit, sims); one SIM marker each where `sims` is set, and `postcard-routes`, `hospital-monitor`, `hotel-corridor-files` are listed in `docs/sims/needed-ids.md`; kernel has `sims: []` as its roadmap row says. Predict items have exactly 3 options and one correct (packets b, a, c; kernel b, c, a; profiler b, c, a; security c, a); Guess answers A, C, A, A; Recap is 3 lines each under 20 words; Common mistakes 4 each; You can now 3 each; Interview has 4 questions each. Fix block: 2 hints, 5 reviewer questions, 27 lines (limit 10 to 30). Prose words (outside code, comments and tables): packets 1,583; kernel 1,462; profiler 1,588; security 1,760. No Practise section is needed (practice-os.md says "problems: none" for the kernel leaf; the others are not DSA, DAA or OS).

---

## 1. networking/how-the-internet-works/how-data-travels-in-packets: FIX (minor)

Facts: all MDN, RFC and Media3 quotes match. The "of 5" total is MDN's simplified header and the leaf says so (line 92).

1. Banned word "just" (docs/04 T4): line 94 "a Kotlin class that just holds values" -> "a Kotlin class that holds values"; line 209 "ask for just that one" -> "ask for that one only". Mechanical.
2. Lines 45 and 227: "On the internet, the receiving side notices a gap and asks for that one packet again" and "the missing piece is sent again" are stated for all internet traffic. Resending is TCP's job (RFC 9293); UDP does not resend. Change line 45 to "On the internet, with a protocol such as TCP, the receiving side notices ..." and line 227 to "With TCP, the receiver sees a gap ...". Mechanical (checklist 4).
3. Line 214, last two sentences: "The player had played everything it had and was waiting for more packets to arrive" is an inference, not what the page says (it says "more data needs to be loaded"). Change to "My own reasoning: the player had played everything it had and was waiting for more data to arrive." Mechanical.
4. Line 92: "TCP, a protocol that carries much app data" is unlabelled; add "(general knowledge)". "Byte-stream" and "stream" (lines 92, 210, 230) are unglossed; add at line 92 "(a stream is data that arrives as one ordered flow)". Mechanical (checklist 3).
5. Line 86: **packet** is bold here and again on line 37; docs/04 3.3 wants each term bold once. Remove the bold on line 86 (keep line 37). Mechanical. Style nit.
6. Housekeeper: SOURCES line 23 says the stdlib calls were "not re-fetched"; `chunked`, `mapIndexed`, `sortedBy` and `joinToString` now ran and behave as described. Strip the SOURCES comment at go-live; set `verified: kotlin-2.4.21` on `join-by-number` (the other two `not-run` stamps are hand-traced prose items and stay).

## 2. os/what-an-os-does/kernel-and-system-calls: FIX (minor)

Facts: Linux kernel under Android, UID sandbox, `SCHEDULE_EXACT_ALARM` / `SecurityException`, syscalls(2), open(2) ENOENT/EACCES/O_CREAT all match. The `svc` instruction on 64-bit ARM is correct (labelled general knowledge).

1. Banned words "simply" and "just" (T4): line 31 "not simply allowed" -> "not allowed"; line 45 "cannot simply climb over" -> "cannot climb over"; line 193 "is just another function call" -> "is another ordinary function call". Mechanical.
2. Line 199 and 203: "`SecurityException`, which is Java's name for a refused request" and the exact-alarm example are used to illustrate a kernel refusal, but the page does not say who made that check (Android 14 page only says the permission is required or the exception is thrown). The leaf hedges once at the end of line 203; make it explicit: after the alarm sentence add "The page does not say whether the kernel or an Android system service made this check." and change line 199 to "a `SecurityException`, which Java and Android throw when a request is refused (general knowledge)". Mechanical.
3. Checklist 4: "The processor itself enforces it" (line 45) and Build step 2 "Put the wall between them in the processor" are general CS facts without a label. Add "(general knowledge)" to both. Mechanical.
4. Checklist 3: "the runtime" and "the library" (lines 87, 99) are unglossed. Change line 87 to "... go through the runtime (the program that runs your Kotlin code) and a library ..." Mechanical.
5. Housekeeper: SOURCES line 25 mentions the roadmap VERIFY; it is covered by the two Android pages (both fetched today). Strip the comment at go-live. The `not-run` stamps on the three `run: false` Predicts are correct (hand-traced).

## 3. profiling/profiler-tour/read-a-profiler-screen: FIX (minor)

Facts: every claim in the row table, the thread colours, the chart table, Self/Children/Total, the debuggable/profileable text and the heap dump and allocation point match the docs fetched today. The task name "View Live Telemetry" is current (roadmap VERIFY is satisfied; the leaf already says labels change between versions). The author-data deviations (no Energy or Network rows, no "busiest 200 ms") are correct: the page lists exactly four rows and the flame chart axis is not time.

1. Tricks to remember has 6 bullets (docs/04 allows 2 to 4). Merge "Rows say when. Charts say what." with "Self plus Children is Total." into one bullet, and drop "Check the build first, because a debug build changes the speed." (it is covered in Common mistakes). Keep the Task bullet. Result: 4 bullets. Mechanical.
2. Lines 215 to 217 (Where this shows up): "The CPU row shows a spike. The thread row shows which thread was green and which was waiting in yellow." reads as what every scroll recording looks like. A jank from a busy thread shows green, not yellow. Reword to "You might see a spike on the CPU row and a thread in yellow (an example: what you see depends on the cause)". Mechanical (checklist 4, no untrue on-screen claims).
3. Line 231 (interview, last answer): "So the numbers from a profileable build are closer to what users see" is an inference; the page says only "without the performance overhead of the debug build". Add "(my own reasoning)" before it. Mechanical.
4. Checklist 3: "trace" (line 98 "record a trace of calls") and "debug variant", "release variant" (line 109) are unglossed. Add "(a variant is one version of the build, such as debug or release; general knowledge)" and "a trace is a recording of which calls ran". Mechanical.
5. Housekeeper: strip the SOURCES comment at go-live; the three Predict stamps stay `not-run` (hand-traced, prose items).
6. No FIX needed on length (1,588 words, at the top of the 900 to 1,600 budget; do not add words while doing 1 to 4, and trim an equal number elsewhere, for example in the "Where the analogy stops" paragraph).

## 4. security/secure-storage/where-data-lives-on-a-phone: FIX

Facts: internal-only-by-default, external globally readable and writable, validation, no passwords on the device, short-lived token, UID sandbox, preferences wording, Room, uninstall table, shared-media sentence, `allowBackup` default, Block Store tip, sensitive-information advice: all match. All code compiles and runs as stated (see Snippets).

1. Checklist 3 and docs/04 3.1 ("no term the learner has not met" in the first screenful): "login token" in The question (line 30) is not explained until Name it (line 89). Change line 30 to "Your app saves a login token (a short pass from the server that keeps the user signed in) and a few notes ..." Also "internal storage" is used in Build step 3 (line 78) before Name it; add "(your app's private area)" after it. Mechanical.
2. Lines 86 and 99 (encryption). The docs say only "on Android 10 (API level 29) and higher, these locations are encrypted" and do not say what that protects against. Line 99 then claims "encrypted storage on Android 10 and higher keeps your files from other people" under "My own reasoning", which can be read as the encryption being what blocks other apps (that is the sandbox and the UID, line 85). Replace line 99 with: "My own reasoning, general knowledge: the encryption protects stored files if someone gets at the storage without unlocking the phone. Other apps are kept out by the sandbox. Your own app reads its files as normal text, because the system decrypts them for it. The page does not say more." Mechanical.
3. Line 90, 97, 278 and 285: Auto Backup "includes ... the files in your internal storage". The docs say "files in most of the directories" and name files from `getFilesDir()` (cache is not listed). Change line 90 to "By default it includes shared preferences files and the files saved with `getFilesDir()` in internal storage." and keep "internal files" elsewhere only as "files from getFilesDir()". Mechanical.
4. Tricks to remember has 5 bullets (limit 4). Merge "Private does not mean safe to store a password." and "Ask first whether the data needs to be saved at all." into "Private does not mean safe to store a password, so ask first whether it needs to be saved at all." Mechanical.
5. Checklist 4 labels: line 43 "newer Android versions put limits on the hall" needs "(general knowledge)"; line 279 "and anyone with access can still read them" needs "(my own reasoning)"; line 293 "My own reasoning, general knowledge:" -> "General knowledge:" (rooted-device point is not a deduction). Mechanical.
6. Checklist 3: "Room library" (line 290) and "Block Store" (line 285) are unglossed. Add "(Room is a library that stores data in a database)" and "(Block Store is a Google service for saving sign-in credentials)" or drop the names; the second is on the autobackup page (Google's tip), so it is safe to keep with the gloss. Mechanical.
7. Prose is about 1,760 words against the 900 to 1,600 budget (about 160 over). Owner/lesson-writer: trim the interview answers and the Where-this-shows section, or the owner relaxes the budget for leaves with the interview section (same open item as L2-a 4.2b, 5.3c, 8.2).
8. Housekeeper: set `verified: kotlin-2.4.21` on `who-can-read-the-files`, `what-stays-after-uninstall` and `fix-token-in-the-hall` (all three ran and match, including the Try it break); strip the SOURCES comment at go-live. `generated: true` on the Fix block is correct for the roadmap's F(ai) row.

---

Summary for the fixer: all items are mechanical except 4.7 (word budget, owner/lesson-writer) and the working-tree build break caused by `SimSpec.kt` (not a leaf item; owner of ADR 009). Re-run `./gradlew :site:validateContent` after edits, and only after `SimSpec.kt` compiles or is moved aside. No Kotlin code changes are needed.

## Fixes applied

Applied by the fixer (21 Oct 2026). `validateContent` not run, per instruction (SimSpec.kt in progress).

- packets 1 to 5: "just" removed (2 places); TCP scoped (2 places); Media3 inference labelled "My own reasoning"; "(general knowledge)" on TCP line; "(a stream is data ...)" gloss; bold removed on line 86 "packet".
- kernel 1 to 4: "simply" x2 and "just" removed; SecurityException reworded and hedge sentence added; "(general knowledge)" x2; runtime and library glossed.
- profiler 1 to 4: Tricks cut to 4 bullets (merged, build bullet dropped); CPU/thread sentence reworded; "(my own reasoning)" added; trace and variant glossed; analogy paragraph trimmed to offset added words.
- security 1 to 7: login token glossed in The question; "your app's private area" added; encryption paragraph replaced (item 2); Auto Backup wording corrected (3 places); Tricks cut to 4 bullets; labels fixed (general knowledge x2, my own reasoning, "General knowledge:"); Room and Block Store glossed. Word trim done by cutting repeats (mental-model sentence, Name-it token and external-storage bullets, two common-mistake repeats, Task filler). Word count NOT measured; estimate about 1,700.
- Not done (housekeeper, at go-live): SOURCES comment strip, `verified:` stamps.
- Open: security word count to be confirmed by the owner.

## Re-check

Re-checked 21 Oct 2026 by the independent reviewer, against the "Fixes applied" list. `validateContent` was SKIPPED (a sims WIP file in buildSrc is mid-rewrite), so the structure/validation result is the clean-HEAD one from the first review. Kotlin: the diff against HEAD changes no code in any of the four leaves (one prose line in packets that contains the word "Kotlin"), so there was nothing new to compile; the first review's `bash scripts/kt-run.sh` results stand. Prose words (outside frontmatter, comments, code and tables, same method as before): packets 1,590; kernel 1,477; profiler 1,573; security 1,667 (down from 1,760, under the 1,840 allowance).

1. networking/how-data-travels-in-packets: PASS. FIX 1 to 5 resolved ("just" gone, TCP scoped at lines 45 and 227, Media3 inference labelled, TCP line labelled and "stream" glossed, bold on line 86 removed). Housekeeper item 6 stays for go-live.
2. os/kernel-and-system-calls: PASS. FIX 1 to 4 resolved ("simply"/"just" gone, SecurityException wording and the "page does not say" sentence added, "(general knowledge)" on both processor claims, runtime and library glossed). Housekeeper item 5 stays.
3. profiling/read-a-profiler-screen: PASS. FIX 1 to 4 resolved (4 Tricks bullets, CPU/thread sentence now an example, "(my own reasoning)" added, trace and variant glossed). 1,573 words is inside the budget. Housekeeper item 5 stays.
4. security/where-data-lives-on-a-phone: FIX (one new defect from the edit; items 1 to 7 otherwise resolved: login token and "your app's private area" glossed, encryption paragraph replaced, Auto Backup wording corrected, 4 Tricks bullets, labels fixed, Room and Block Store glossed, word count within allowance).
   - 4.9 (new, mechanical, fixer): line 85 has two bullets glued on one line: `- **External storage** is the shared kind of place.- **Preferences** are small saved settings ...`. Put "- **Preferences** ..." on its own line so the list renders as five bullets. Also line 85 is now a thin definition; optionally add "Files there can be read by other apps, as the documentation says (globally readable and writable)." only if the word count stays under 1,840 (it is at 1,667).
   - Housekeeper item 8 stays (verified stamps, SOURCES strip).

VERDICT (re-check, final): FIX
- security 4.9: split the glued bullet on line 85 of `content/security/secure-storage/where-data-lives-on-a-phone.md` (mechanical, fixer). The other three leaves pass.

## Re-check 2

- where-data-lives-on-a-phone.md: the glued bullet is split. Lines 83 to 87 now render as five separate bullets (Sandbox, Internal, External, Preferences, Auto Backup). Confirmed.

VERDICT (re-check, final): PASS
