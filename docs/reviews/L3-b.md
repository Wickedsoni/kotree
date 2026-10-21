# Review L3-b: layer 3 shadow leaves, commit 14786ab

Reviewer: independent. Reviewed files are not edited. Date 21 Oct 2026.

## What was run

- `bash scripts/kt-run.sh` on every Kotlin block and every runnable `code` / `fix` field (Kotlin 2.4.21). All compile and all printed outputs match the stated ones:
  - dsa: Name it program prints 45 / 4950 / 499500; Break variant prints 9 / 99 / 999; predict 1 prints `6`; predict 2 prints `[4, 8, 2, 6]`; fix-merge-scan broken prints `merged: 150 items`, `id checks: 8775`, fixed prints `merged: 150 items`, `id checks: 200` (matches `expected`).
  - kotlin: Name it program prints `7`, `4`, `[9]`; predicts print `7` / `7.5`, `4` / `NullPointerException` / `done`, `start` / `ArithmeticException` / `end`; Break variant throws `ArithmeticException: / by zero` on line 1; Fix variant prints `0`, `4`, `[9]`; fix-average-of-nobody broken stops after `Asha: 85`, fixed prints the three `expected` lines.
  - kmp: the one `kotlin` block starts with `// does not compile` and does not compile, as labelled.
  - ndk, networking, os: no Kotlin (theory leaves, `run: false`).
- `./gradlew :site:validateContent` (SM0 to SM11, roadmap match): `validateContent: ok`.
- Hand trace of every sim `try` list against `SimCore.fold`, `TryProgressCalc`, `ChartMath.published`, and the layout files (results per leaf below).
- Facts re-read at the official pages (Kotlin docs, developer.android.com, source.android.com, RFC 1122, MDN). Result per leaf below.

NOT run: `/preview/<id>` in a browser (mouse, keyboard-only, reduced motion). Sim behaviour is judged from the code only. The orchestrator should do one preview pass per sim at the layer build.

Every leaf still has a `<!-- ⚠️ VERIFY ... -->` comment (checklist item 9). All of them are resolved or already labelled in the prose, so the fixer deletes them (listed per leaf).

---

## 1. dsa/complexity/reading-complexity-in-code: FIX

Facts: ArrayList and HashSet Javadoc claims (Java SE 17 pages: size, isEmpty, get, set in constant time; add amortized constant; the rest linear; HashSet constant time for add, remove, contains, size if the hash function disperses well), `MutableSet.add` (true if added, false if already contained), and the Android 16 ms / jank paragraph match the cited pages (all re-read). LeetCode 283, 14, 209, 15, 84 titles and difficulties match `docs/practice/practice-dsa.md` ([both] plus [A] 14). Frontmatter matches the roadmap. Sim `hidden-second-loop` traced: n10 gives board 10, hands 45; n100 gives 100, 4950; n1000 gives 1000, 499500; `{board.at}` and `{x}` interpolate; both Try steps reachable with buttons; the `say` numbers are true.

1. Frontmatter of the three challenge blocks (lines 186, 216, 257), mechanical.
   - Old: `verified: "UNVERIFIED: compile not run"`
   - New: `verified: kotlin-2.4.21` (all three blocks ran and match their `output` / `expected`).
2. Line 25, mechanical. Delete the whole `<!-- ⚠️ VERIFY: compile not run ... -->` comment. Compile is now done; the ArrayList point is already labelled "general knowledge" in line 135.
3. Line 135, mechanical (undefined jargon, checklist 3).
   - Old: `run in constant time and that all other operations run in linear time, roughly speaking, except that `add` takes amortized constant time.`
   - New: `run in constant time, which means about one step however long the list is, and that all other operations run in linear time, which means about n steps for n items, roughly speaking, except that `add` takes amortized constant time, which means one step on average over many adds.`
4. Line 350, mechanical (inference presented as fact, checklist 4).
   - Old: `That is about two million checks, and the checks run before the screen can draw. My own reasoning: I have no official figure`
   - New: `That is about two million checks. My own reasoning: if the merge runs while the screen waits to draw, the checks come first, and I have no official figure`
5. Sim block, lines 50 and 55, mechanical but a design call (the writer may keep it). The plot spans n = 0 to 12, so after "Set n to 100" or "Set n to 1000" the marker leaves the chart and the picture does not change; only the legend numbers move. Drawing 0 to 1000 shows the blow-up.
   - Old: `intro: "A class of n students walks into a corridor. The plot shows n from 0 to 12, and the buttons read the costs at 10, 100 and 1000 students."` and `scale: { x: [0, 12] }`
   - New: `intro: "A class of n students walks into a corridor. The plot shows n from 0 to 1000, and the buttons read the costs at 10, 100 and 1000 students."` and `scale: { x: [0, 1000] }`
6. Note only, no change required. The fixed `mergeItems` also drops duplicate ids inside `server`, which the broken version did not. It does not affect the expected output. A one-clause mention in `explanation` is optional.

---

## 2. kmp/source-sets/project-layout-and-targets: FIX

Facts, all confirmed at the cited pages: `commonMain` present in all projects and compiles to all declared targets; platform source sets compile for only their target; `iosMain` shared among iOS targets; `iosArm64()` 64-bit iPhones; `iosSimulatorArm64` on Apple silicon; default hierarchy template creates shared sets from `android`, `iosArm64`, `iosSimulatorArm64`; Android plugin `com.android.kotlin.multiplatform.library` "officially supported", `android { namespace }` inside `kotlin { }`, `androidMain`, `androidTarget()` only in the migration (legacy) section; "common code needs to compile for every declared target". The leaf correctly avoids the roadmap's "androidTarget renamed android" claim. Frontmatter matches the roadmap. Predict keys true and varied (c, b, a).

Sim `floor-plan-targets` traced: Step 1 `build-android` marks and lights the two Android rooms. Step 2 `build-ios` with `ios` on lights corridor and iPhone wing. Step 3: settings are hoisted in `SimCore`, so `ios=off` replays the earlier `build-ios` with the switch off too; the final state (iPhone side all ghost, say "The plan lists no iPhone wing...") is exactly what the Try text and the notice sentence describe. Pass.

1. Line 25, mechanical. Delete the `<!-- ⚠️ VERIFY ... -->` comment. All three points are non-claims that the prose already respects.
2. Lines 134 and 138, 158, mechanical (Gradle, plugin, `compileSdk`, `minSdk` used without definition; Gradle is a phase-5 prerequisite and not required here).
   - Old (134): `The build file is the plan. Targets are declared with calls inside a `kotlin { }` block.`
   - New: `The build file is the plan. In a shared module it is the file `build.gradle.kts`, which **Gradle**, the tool that builds the project, reads (general knowledge). Targets are declared with calls inside a `kotlin { }` block.`
   - Old (138): `You do not make it by hand. The Kotlin Gradle plugin has a built-in **default hierarchy template**.`
   - New: `You do not make it by hand. A **plugin** is an add-on that teaches Gradle how to build one kind of project (general knowledge). The Kotlin Gradle plugin has a built-in **default hierarchy template**.`
   - Old (158): `such as `compileSdk` and `minSdk`, which the Android page's example sets.`
   - New: `such as `compileSdk` and `minSdk`, which the Android page's example sets. They name the Android version the code is built against and the oldest Android version it runs on (general knowledge).`

3. Lines 42, 241, 258, 286 and 293, mechanical (checklist 4). "Read once for each build", "nothing is copied into the wings" and "in place, never copied" are not stated on any of the four pages read; the docs say only that `commonMain` "compiles to all declared targets". Label them as general knowledge.
   - Old (42): `A file in the shared folder is read once for each build, and each build makes its own result from it. The Kotlin documentation says the shared folder compiles to all declared targets. Nothing is copied into the wings.`
   - New: `The Kotlin documentation says the shared folder compiles to all declared targets. My own reasoning, from that: each build compiles the shared files for its own target and makes its own result from them, and nothing is pasted into the wings.`
   - Old (258): `The file is read in place, once for each build.`
   - New: `The documentation says the shared folder compiles to all declared targets, so each build compiles it for itself (my own reasoning).`
   - Old (286): `2. The shared folder compiles to all declared targets, in place and never copied.`
   - New: `2. The shared folder compiles to all declared targets, so each build uses the same files.`
   - Old (241, Predict `who-compiles-common-main` option b why): `It is read in place, once for each build.`
   - New: `The documentation says the shared folder compiles to all declared targets, with no copying step.`
   - Old (You can now, last bullet, line 293): `Explain why the shared folder is read once for each build and is not copied.`
   - New: `Explain why the shared folder compiles to every declared target and does not need a copy in each platform folder.`

---

## 3. kotlin/reading-code/review-a-generated-file: FIX

Facts, confirmed: Kotlin null-safety page (`!!` results in an NPE; the "only possible causes" list includes `!!`); Kotlin numbers page (integer division discards the fraction); `String.toInt()` throws `NumberFormatException`; activity lifecycle (`savedInstanceState` is a `Bundle`, null if the activity never existed before); crash page (Android terminates the app's process and shows a dialog). The three exception classes the writer did not read on the Android reference are now read, and the leaf's use of them is correct: `java.lang.ArithmeticException` "an integer 'divide by zero' throws an instance of this class", `java.lang.NullPointerException` "Thrown when an application attempts to use null in a case where an object is required", `java.lang.NumberFormatException` "attempted to convert a string to one of the numeric types, but that the string does not have the appropriate format". The leaf's open VERIFY (Int division by zero throws) is therefore settled; only the citation is missing.

Sim `review-walk-through` traced (stepper): each walk's `when p_i: 0` holds once, `found` reaches 4, `Decide` needs `found: 4` and otherwise says the `refuse` text. Try steps 1 to 3 are reachable with buttons. Line numbers in the `say` texts (3, 6, 9) match the ten sim lines. Pass, apart from item 6.

1. Frontmatter of four challenge blocks (lines 202, 228, 259, 300), mechanical.
   - Old: `verified: "UNVERIFIED: compile not run"`
   - New: `verified: kotlin-2.4.21`
2. Line 26, mechanical. Delete the `<!-- ⚠️ VERIFY ... -->` comment (compile done; ArithmeticException claim confirmed on the Android reference).
3. SOURCES comment, insert before the "Interview seed" line (line 24), mechanical.
   - New lines: `Android reference, java.lang.ArithmeticException (an integer "divide by zero" throws an instance of this class): https://developer.android.com/reference/java/lang/ArithmeticException` and `Android reference, java.lang.NullPointerException (thrown when an application attempts to use null in a case where an object is required): https://developer.android.com/reference/java/lang/NullPointerException` and `Android reference, java.lang.NumberFormatException (string not in the right format for a numeric type): https://developer.android.com/reference/java/lang/NumberFormatException`
4. Line 399, mechanical.
   - Old: `Integer division by an empty list's size is the same kind of hidden stop (general knowledge).`
   - New: `Integer division by an empty list's size is the same kind of hidden stop: the Android reference for `ArithmeticException` says an integer "divide by zero" throws it.`
5. Line 168, mechanical (`javaClass.simpleName` is used in two Predict items and never explained).
   - Old: `` `exceptionOrNull()` gives the failure, or `null` if there was none. This lets``
   - New: `` `exceptionOrNull()` gives the failure, or `null` if there was none, and `javaClass.simpleName` is the short name of that failure's type, such as `ArithmeticException`. This lets``
6. Sim, `decide` case, line 119, mechanical. The first words "Not yet." read as if the file could be signed later; the rest says send it back.
   - Old: `say: "Not yet. Line 3 stops the program on an empty list,`
   - New: `say: "Do not sign yet. Line 3 stops the program on an empty list,`
7. Line 383, mechanical (`Bundle` undefined).
   - Old: `a `Bundle` that holds the activity's previously saved state`
   - New: `a `Bundle`, a small bag of saved values (general knowledge), that holds the activity's previously saved state`

---

## 4. native-ndk/art-and-dex/how-art-runs-code: FIX

Facts checked at source.android.com "Configure ART" and the Baseline Profiles overview. Confirmed: hybrid AOT/JIT/interpretation since Android 7; AOT can be profile-guided; methods not AOT-compiled are interpreted and frequent ones JIT-compiled; idle-and-charging daemon re-compiles from the combined profile; Baseline Profile text (JIT after interpretation or odex written in the background; suboptimal until optimized; ART AOT-compiles the profile's methods during installation; "about 30%" from the first launch); NDK sentence. Not supported as written (items 2 to 4): three claims. The made-up numbers (3 orders, 20 of 5,000) are disclosed in the prose. Sim `waiter-learns-the-card` traced: serve x3 gives orders 1, 2 (read), then case 3 at `orders == 2` moves the dish to `memo` with the right say; overnight moves `memo` to `ahead`; optional step (reinstall, `profile=on`, prep, serve) ends fast. Pass, apart from item 6.

1. Line 24, mechanical. Delete the `<!-- ⚠️ VERIFY ... -->` comment (points 2 and 4 are already labelled in the prose).
2. Line 178, mechanical. The "first few runs" flow and the idle daemon are given by the page as the example flow for Pixel devices, not as a rule for all phones.
   - Old: `It says the first few times an app runs, methods that are not AOT-compiled are interpreted`
   - New: `It then gives the flow that Pixel devices use as its example. In that flow, the first few times an app runs, methods that are not AOT-compiled are interpreted`
3. Line 178, mechanical. The `speed-profile` default sits under "System ROM configuration" and describes apps built into the system image, not apps installed by the learner. Remove the sentence.
   - Old: ` The page says the default for apps is the `speed-profile` filter, which compiles the methods listed in the profile.`
   - New: (delete the sentence)
4. Lines 180, 229, 276, mechanical. "No AOT compilation" is true at install only; the leaf itself says the idle daemon compiles later.
   - Old (180): `The same page says that an app installed without a dex metadata file, a side file that can carry the profile (general knowledge), gets no AOT compilation.`
   - New: `In the same Pixel example, the page says that an app installed without a dex metadata file, a side file that can carry the profile (general knowledge), gets no AOT compilation at install.`
   - Old (229): `Without a profile, the documentation says no AOT compilation is done.`
   - New: `Without a profile, the documentation says no AOT compilation is done at install.`
   - Old (276): `that without a profile no AOT compilation is done, and with one only the listed methods are.`
   - New: `that without a profile no AOT compilation is done at install, and with one only the listed methods are.`
5. Line 184, mechanical (NDK, APK, JNI undefined).
   - Old: `One more thing from the NDK documentation: native C and C++ code is compiled into a native library and packaged into the APK, and Java calls it through JNI.`
   - New: `One more thing from the NDK documentation. The NDK, the Native Development Kit, is the toolkit that compiles C and C++ code into a native library and packages it into the APK, the app file. Java code then calls that library through JNI, the Java Native Interface, which is the doorway between Java or Kotlin and native code.`
6. Sim, `serve` case for `ahead`, line 77, mechanical. The same case runs after "Leave the phone idle", where it is not the first order.
   - Old: `No card is read, so even the first order is quick."`
   - New: `No card is read, so this order is quick."`
7. Lines 182 and 285, mechanical. The Baseline Profiles page states the 30% twice with different strength: in its introduction as a general claim ("Baseline Profiles improve code execution speed by about 30% from the first launch by avoiding interpretation and just-in-time (JIT) compilation steps for included code paths") and in its benefits section as a measurement ("Many apps measure performance boosts of about 30% after optimizing"). The leaf's hedge matches only the second.
   - Old (182): `And it says a Baseline Profile improves code execution speed by about 30% from the first launch, which the page presents as a typical result, not a promise.`
   - New: `The page's introduction says Baseline Profiles improve code execution speed by about 30% from the first launch, and its benefits section says many apps measure boosts of about 30%. My own reasoning: the second wording is a measurement, so treat 30% as typical and not guaranteed.`
   - Old (285): `and that this improves execution speed by about 30% from the first launch.`
   - New: `and that many apps measure speed boosts of about 30%.`
8. Owner, no change forced. The roadmap row says "Baseline Profiles belong to Profiling", but this leaf defines Baseline Profile in Name it and uses it in Common mistakes and Android. It is accurate and short, so it can stay; the curriculum owner should say whether to trim it to a one-line pointer.

---

## 5. networking/layers/why-networks-use-layers: FIX

Facts, confirmed: RFC 1122 four layers; "The transport layer provides end-to-end communication services for applications"; "All Internet transport protocols use the Internet Protocol (IP) to carry data from source host to destination host"; hosts implement "the communication protocol used to interface to that network"; MDN HTTP is an application layer protocol sent over TCP or TLS-encrypted TCP; "these are hidden in the network and transport layers"; proxies "operating at the application layers"; MDN's picture says "network layer". The three exception classes were read on the Android reference (the writer used Java SE 17 only). Android reference text, word for word the same as the leaf's quotes: `java.net.UnknownHostException` "Thrown to indicate that the IP address of a host could not be determined."; `java.net.ConnectException` "Signals that an error occurred while attempting to connect a socket to a remote address and port. Typically, the connection was refused remotely (e.g., no process is listening on the remote address/port)."; `javax.net.ssl.SSLHandshakeException` "Indicates that the client and server could not negotiate the desired level of security. The connection is no longer usable." Packages match. So the "I did not confirm" gap closes.

Sim `envelope-layers` traced (flow): `next` x3 reaches `link-out` with the three labels; `peek` there falls to `refuse` ("under other wrappers"); `next` x3 reaches `transport-in`; `peek` there is allowed. All Try steps reachable with buttons and the events appear in the order the steps need. Say texts match the state. Pass.

1. Line 26, mechanical. Delete the `<!-- ⚠️ VERIFY ... -->` comment (point 1 settled above, points 2 and 3 are labelled in the prose).
2. SOURCES comment, mechanical.
   - Old (line 19): `RFC 1122, section 1.1.3 Internet Architecture`
   - New: `RFC 1122, section 1.1.3 Internet Protocol Suite` (the title the fetched RFC gives for 1.1.3).
   - Add after line 23: `Android reference, java.net.UnknownHostException: https://developer.android.com/reference/java/net/UnknownHostException`, `Android reference, java.net.ConnectException: https://developer.android.com/reference/java/net/ConnectException`, `Android reference, javax.net.ssl.SSLHandshakeException: https://developer.android.com/reference/javax/net/ssl/SSLHandshakeException`
3. Line 295, mechanical.
   - Old: `These are the descriptions in the Java SE 17 documentation. Android apps use the same Java classes, which I did not confirm on the Android reference pages.`
   - New: `These are the descriptions on the Android reference pages for these classes. A socket is one end of a connection between two programs, a port is the number that picks the program on a computer, and a handshake is the first exchange in which the two sides agree how to protect the connection (general knowledge).`
4. Line 173 to 175, mechanical (HTTP, TCP, IP appear in the table before they are explained).
   - Old: `It is general knowledge, and it is not used here.` (end of the OSI sentence, line 173)
   - New: `It is general knowledge, and it is not used here. In the table, **HTTP** is the language web programs speak, **TCP** is a protocol that delivers data between two programs, and **IP**, the Internet Protocol, gives each computer an address (general knowledge).`

---

## 6. os/processes/what-is-a-process: FIX

Facts, confirmed: Application Sandbox ("assigns a unique user ID (UID) to each Android app and runs it in its own process"; "kernel-level Application Sandbox"; kernel enforces security at the process level; "By default, apps can't interact with each other"); processes page ("By default, all of an application's components run in the same process, and most applications don't change this"; `android:process`; same Linux user ID and same certificates; process shut down for processes "more immediately serving the user"; "A process is started again ... when there's work"); crash page. `problems: none` in `practice-os.md`, so no "Practise on these" is correct. Interview questions seed all four of the author's questions. Predict keys true and varied (b, c, a).

Sim `sealed-flats` traced (zones): fire marks Music bad and `running` 3 to 2 with the right say; `spread` with walls on matches no case and shows the `refuse` ("The wall stops it"), which still counts for Try step 2; `copy` spawns `notes-two` and `running` goes to 3. Optional step: because settings are hoisted, switching walls off replays the earlier `spread` with walls off, so Flat 3 is already spoiled when the switch flips, and the final `spread` says "Flat 3 is already spoiled". The Try text claims the press spoils Flat 3, which is slightly off (item 3).

1. Line 24, mechanical. Delete the `<!-- ⚠️ VERIFY ... -->` comment (points checked; general-knowledge points are labelled in the prose).
2. Line 276, mechanical (checklist 1, recap is one sentence per line).
   - Old: `1. A program is a file. A process is that program running, with its own memory.`
   - New: `1. A program is a file, and a process is that program running with its own memory.`
3. Sim optional Try step, line 109, mechanical.
   - Old: `- text: "(Optional) Switch off \"Walls between the flats\", then press \"Let the mess spread to Flat 3\". Flat 3 is spoiled. This is what the walls prevent."`
   - New: `- text: "(Optional) Switch off \"Walls between the flats\". Your earlier presses play again with no walls, so Flat 3 is spoiled. Then press \"Let the mess spread to Flat 3\" once more and read what is said. This is what the walls prevent."` (the `done` list stays as it is).
4. Lines 156 and 158, mechanical ("components", "user ID", "certificate" used before or without a definition).
   - Old (156): `Android assigns a unique user ID to each app and runs it in its own process.`
   - New: `Android assigns a unique user ID, a number the system uses to tell who owns what (general knowledge), to each app and runs it in its own process.`
   - Old (156): `by default all the components of one app run in the same process, and that most apps do not change this.`
   - New: `by default all the **components** of one app run in the same process, and that most apps do not change this. A component is one part of an app, such as a screen or a background service (general knowledge).`
   - Old (158): ` A component is one part of an app, such as a screen or a background service (general knowledge).`
   - New: (delete, now said in line 156)
   - Old (158): `and are signed with the same certificate.`
   - New: `and are signed with the same certificate, the developer's digital signature on the app (general knowledge).`
5. Predict `two-starts-two-processes` (lines 168 to 169) and Task (line 245), mechanical and recommended. Many editors pass a second start to the first one, so the premise can be false on a real laptop.
   - Old: `On a laptop you start the same text editor program twice,` / `by double-clicking its icon two times.`
   - New: `On a laptop you open two terminal windows` / `and start the same small program in each one. Both are still running.`
   - Old (Task): `start a program you use twice on a computer, then open the system's list of running programs and count its entries.`
   - New: `start a small program twice, for example in two terminal windows, then open the system's list of running programs and count its entries.`

---

## Cross-leaf item (owner)

Checklist 14 says every `say` is one plain, true sentence. Most `say` texts in these six sims carry two or three sentences (`hidden-second-loop` n100, `floor-plan-targets`, `review-walk-through`, `waiter-learns-the-card`, `envelope-layers`, `sealed-flats`). The live `tape-measure-range` exemplar uses one sentence, while the ADR 009 worked example uses two. All `say` texts are true and match the sims; the owner should decide whether checklist 14 means one sentence strictly. If it does, the writers split or trim them in one pass; no per-line rewrite is listed here.

## Claims I could not verify

- Sims in a browser (preview, keyboard, reduced motion): not run.
- LeetCode numbers and titles were checked against `docs/practice/practice-dsa.md` ([both] tags and [A] 14) and my own knowledge, not by opening leetcode.com.
- Java SE 17 pages cited by the networking leaf were not opened by me; the Android reference pages (same wording) were.

VERDICT: FIX

## Fixes applied

Applied 21 Oct 2026 on top of commit 14786ab. `../gradlew :site:validateContent` printed `validateContent: ok`.

- dsa/complexity/reading-complexity-in-code: items 1-5 applied (verified kotlin-2.4.21 on 3 challenges, VERIFY comment removed, ArrayList wording, two-million sentence, sim intro and x scale 0 to 1000). Item 6 is a note, not changed. Checklist 14: n100 `say` cut to one sentence.
- kmp/source-sets/project-layout-and-targets: items 1-3 applied (VERIFY removed, Gradle, plugin and compileSdk defined, read-once wording relabelled as own reasoning in lines 42, 241, 258, 286, 293). Checklist 14: iPhone-wing `say` cut to one sentence.
- kotlin/reading-code/review-a-generated-file: items 1-7 applied (verified kotlin-2.4.21 on 4 challenges, VERIFY removed, Android references added to SOURCES, ArithmeticException citation, javaClass.simpleName defined, "Do not sign yet", Bundle defined). Checklist 14: four walk `say` texts and the decide `say` cut to one sentence.
- native-ndk/art-and-dex/how-art-runs-code: items 1-7 applied (VERIFY removed, Pixel flow framing, speed-profile sentence removed, "at install" scope, NDK/JNI defined, "this order" wording, 30% statements limited to the two sourced page statements). Item 8 is owner-only, not changed. Checklist 14: all multi-sentence `say` texts merged to one sentence.
- networking/layers/why-networks-use-layers: items 1-4 applied (VERIFY removed, RFC section title, Android references, socket/port/handshake, HTTP/TCP/IP glosses). Checklist 14: multi-sentence `say` texts merged.
- os/processes/what-is-a-process: items 1-5 applied (VERIFY removed, recap sentence, optional Try step rewritten, UID/component/certificate defined, two terminal windows in the Predict and Task). Checklist 14: fire, spread and copy `say` texts merged to one sentence.
