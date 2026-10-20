# Review W4-b: four shadow leaves (what runs on a phone, what is in an app file, build, install)

Date 20 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31. Voice: L29. Nothing in the leaves was edited. No gradle run (as instructed). No Kotlin in these leaves (all four are `playground: false`, `run: false`, `verified: not-run`); every Predict key was re-traced by hand by me.
Policy applied: a SIM marker with no built sim does not block; the `<!-- SOURCES -->` comment at line 18 is `housekeeper`'s.

VERDICT: FIX (0 PASS, 4 FIX; every FIX is mechanical, `fixer` can do it; no owner decision blocks a leaf)

| Leaf | Verdict |
|---|---|
| `foundation/from-code-to-running/what-is-art` | FIX (3 items) |
| `foundation/how-an-app-is-born/what-is-an-app-file` | FIX (3 items) |
| `foundation/how-an-app-is-born/what-happens-when-i-build` | FIX (3 items) |
| `foundation/how-an-app-is-born/how-an-app-gets-installed` | FIX (1 item) |

## Rulings

1. Sim divergence (hard rule 5): not a violation. Rule 5 protects the tree structure: leaf ids, titles, difficulty, `requires`, `fruit` (V5). All four leaves equal their roadmap rows (`content/foundation/_roadmap.md` lines 165-188; the roadmap has no `order` key, so I counted the leaf entries: they are leaves 29, 30, 31, 32 of the branch, matching `order` in the files). The roadmap file was not touched. Only the Sim cells differ: `what-is-an-app-file` ("the phone refuses the box") and `what-happens-when-i-build` ("the phone rejects the box") say more than the official pages do. The leaf wording is the safe one: the docs say a component missing from the manifest cannot be started, and that Android requires every APK to be signed before install; they do not describe a refusal screen. `docs/04` §3.4 (the sim only shows what the text says) makes the leaf binding. `docs/sims/needed-ids.md` lines 17 and 18 already record both divergences. Follow-up for `curriculum-designer` (owner-level, not a `fixer` job, not blocking): reword both Sim cells and the roadmap `Notes` VERIFY cells (lines 271 and 279 to 281) to match the leaves before `sim-engineer` builds `flat-pack-app-box` and `bakery-build-line`; `sim-engineer` must build to the leaf text.
2. Four-station build order: acceptable. It is labelled as the writer's own summary in the body (`what-happens-when-i-build.md` line 85: "This four-station picture is my own summary, not a list from one Android page") and in the SOURCES comment. I found no official page that lists build steps (developer.android.com/build/android-build-structure lists files, not steps; /build/gradle-build-overview lists three Gradle phases: initialization, configuration, execution). Nothing contradicts it: /build says the build system "compiles app resources and source code and packages them into APKs or Android App Bundles"; /tools/d8 says d8 compiles Java bytecode into DEX; /build/building-cmdline says each build output "must be signed with a certificate before you can deploy". Optional (not FIX): add "Real builds have many more tasks than four." to line 85 so the simplification is stated.
3. Roadmap `VERIFY` notes for these four rows are now confirmed on developer.android.com (see table). The "Configure ART" page on source.android.com could not be reached; the leaf `what-is-art` rests on developer.android.com/topic/performance/baselineprofiles/overview instead, which covers the same mix. `housekeeper` clears the roadmap warning signs at go-live.

## Hand-traces (mine)

what-is-art
- `who-cooks-on-the-phone`: compact cards are DEX; ART executes DEX; the JVM is not used on devices. Key b correct.
- `prepare-ahead-or-while-cooking`: moment 1 reads as it cooks (interpreted); moment 2 prepares while running (JIT); moment 3 prepares in the idle time before need (AOT). Key c correct.
- `first-open-after-install`: no profile, so code is interpreted and JIT-compiled until optimised; docs say a suboptimal first run after install or update. Key a correct, and it is hedged ("can feel slower").

what-is-an-app-file
- `which-pile-names-the-icon`: manifest `application` sets `android:icon` and `android:label`, pointing to resources. Key c correct.
- `a-file-called-game-aab`: an AAB "can't be installed on Android devices". Key a correct.
- `part-missing-from-the-label`: not declared in the manifest, so the system can't start it. Key b correct (see FIX 2 for the scope of the general claim).

what-happens-when-i-build
- `stations-in-order`: Mix, Bake, Pack, Stamp. Key b correct; a and c are the believable swaps.
- `box-without-a-stamp`: Android requires all APKs be signed before install. Key a correct. The page does not state the refusal outcome itself; the leaf labels this at line 93 ("My own reasoning from that rule").
- `no-mixing-station`: no dough, so Bake, Pack and Stamp have nothing. Key c correct; the `why` labels it as own reasoning.

how-an-app-gets-installed
- `phone-checks-the-seal`: signed before install or update; certificates must match on update. Key b correct.
- `parcel-for-each-phone`: Play serves optimised APKs per device configuration. Key a correct.
- `missing-pieces`: sideloaded install missing required split APKs fails on Android 10 or higher. Key c correct.

Key letters vary in each leaf (b, c, a / c, a, b / b, a, c / b, a, c). Every `output` string equals the correct option text.

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| On Android 5.0 and higher each app runs in its own process with its own ART instance; ART executes DEX, "a bytecode format designed specifically for Android that's optimized for a minimal memory footprint"; AOT and JIT are listed ART features; d8 compiles to DEX | developer.android.com/guide/platform | Matches. The page has no word on interpretation or profile-guided compilation; those come from the next row |
| Without a profile, code is "either JIT-compiled in memory after being interpreted, or written to an odex file in the background when the device is idle"; suboptimal first run after install or update; Baseline Profile = ART does AOT of listed methods at install, avoids interpretation and JIT for them; version table (5-6 full AOT, 7-8.1 partial, 9+ baseline plus cloud) | /topic/performance/baselineprofiles/overview | Matches |
| "The JVM is not used on Android devices or emulators"; bytecode generated for compiled Java and Kotlin source | /build/jdks | Matches |
| d8 compiles Java bytecode into DEX bytecode; used by Android Studio and the Android Gradle plugin | /tools/d8 | Matches. The page does not define DEX beyond "bytecode that runs on Android devices"; the definition comes from guide/platform. R8 is not mentioned in any leaf (not a claim, nothing to check) |
| APK = archive with `.apk` suffix, the file devices use to install; AAB = "a publishing format and can't be installed on Android devices"; sandbox, unique Linux user ID, own process and VM | /guide/components/fundamentals | Matches |
| Sandbox: "The system sets permissions for all the files in an app so that only the user ID assigned to that app can access them" | same | Matches (re-fetched) |
| Manifest must declare components, permissions, hardware and software features; example `android:icon="@mipmap/ic_launcher"`, `android:label="@string/app_name"`; "If you subclass any of these components without declaring it in the manifest file, the system can't start it" | /guide/topics/manifest/manifest-intro | Matches, with one scope problem: broadcast receivers can be registered in code (fundamentals page), so "only a listed component can start" is too wide. See what-is-an-app-file FIX 2 |
| Manifest normally a binary file inside the APK; `resources.arsc` holds configuration-specific values such as translations; `classes.dex` | /studio/debug/apk-analyzer | Matches |
| AAB: publishing format, "defers APK generation and signing to Google Play"; Google Play generates and serves optimised APKs per device configuration; base APK plus configuration APKs; partial sideloaded installs fail on all Google-certified devices and on Android 10 or higher | /guide/app-bundle | Matches |
| "You can't deploy an app bundle directly to a device"; APK better for a quick debug test; two build types; each output must be signed; debug key is insecure and cannot be used to publish; release must use your own private key; bundletool converts to bundles | /build/building-cmdline | Matches |
| Build system compiles resources and source and packages them into APKs or AABs; debug signed with a default key; release unsigned unless configured; "All new apps submitted after August 2021 are required to use AABs" | /build | Matches |
| Android requires all APKs be signed before install or update; update allowed if certificates match; debug certificate "insecure by design", most stores reject it; with Play App Signing "Google manages and protects your app's signing key for you and uses it to sign your APKs for distribution" | /studio/publish/app-signing | Matches. The last clause conflicts with install FIX 1 |
| Manifest "Read by Android package manager" | /build/android-build-structure | Matches (not used in the leaf text) |
| AGP registers all tasks needed to build an APK | /build/gradle-build-overview | Matches |

Could not verify from an official page (accepted, none blocks): source.android.com "Configure ART" (not reachable); that "the quiet hour" is exactly AOT (the docs say profile code is compiled "when the device is idle", leaf wording is hedged: "can prepare ahead"); bytecode "not tied to one exact kind of processor" (CS definition); the build-order summary (labelled); the install order seal check, label card, locked room (labelled at line 93); the "separate head cooks" reading (labelled "My own reasoning"); the update-mismatch consequence (labelled).

Checked and OK on all four: frontmatter keys, order and values; `id` equals path; `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows; `sims` equals the SIM marker (`phone-kitchen-two-ways`, `flat-pack-app-box`, `bakery-build-line`, `parcel-to-phone`; all four in `docs/sims/needed-ids.md`); summaries under 25 words; `playground: false`, no Try it, no Fix the bug; headings in template order with exact names; all challenge blocks have 3 options, exactly one `correct`, a `why` on every option; Question 5, 5, 4, 4 sentences, no code, no bold; Recap three numbered lines (see FIX for the one two-sentence line); prose 1,481 / 1,392 / 1,481 / 1,403 words (budget 900 to 1,600); T4 banned words none ("just-in-time" is the term), no exclamation marks, no non-ASCII, no emoji, no VERIFY, no video, no capstone, no next-lesson promise; every bold term appears once; Android sections are reading-only and name a symptom; "Where the analogy stops" present in all four; every "You can now" line has a matching task except one (build FIX 3); correct-option letters vary.

## Leaf 1: what-is-art: FIX

1. `content/foundation/from-code-to-running/what-is-art.md`, line 38 (Where the analogy stops). "nobody can ask them to work differently" contradicts line 204, where app makers ship a Baseline Profile that makes ART prepare listed code ahead. Old: `The cooks are programs, not people, and nobody can ask them to work differently. Android decides how they work, and its rules have changed between versions.` New: `The cooks are programs, not people. Android decides how they work, and its rules have changed between versions.` (mechanical)
2. Same file, line 211 (Recap line 2). Two sentences; Recap lines must be one sentence. Old: `2. The phone's own runtime, ART, runs the DEX. The JVM is not used.` New: `2. The phone's own runtime, ART, runs the DEX, and the JVM is not used.` (mechanical)
3. Same file, table after line 101 (Name it). Three analogy objects are never mapped (`docs/04` §4.2): the box (used in Build step 3 and in the first Predict), the station and the head cook (Mental model, Build step 4). Insert after line 101 (`| Phone kitchen's cooks | ART | Run the DEX on the phone |`): `| Box | The app file | Carries the DEX to the phone |` and `| Head cook and station | One ART instance in one process | Every app gets its own |` (mechanical)
Optional (not FIX): sentence length (T7, as in W3-b). Line 91 is 35 words: split before "and with its own instance of ART". Lines 93 and 202 are 29 words each. Line 142 "the fifth time" reads like a real ART threshold that no page gives; "again and again" is safer.

## Leaf 2: what-is-an-app-file: FIX

1. `content/foundation/how-an-app-is-born/what-is-an-app-file.md`, line 37 (Where the analogy stops). It says "in the animation the phone refuses a box with no label card", but the Try this (line 48) and `needed-ids.md` say the phone is left with no list of parts, and no official page describes a refusal. Old: `Also, in the animation the phone refuses a box with no label card. The Android documentation says something narrower: a part that is not listed on the label card cannot be started.` New: `Also, the animation takes the label card away to make a point. The Android documentation says the system must read the label card to know a part exists, so for the main kinds of part, one that is not listed cannot be started.` (mechanical)
2. Same file, lines 93, 171, 206. The claim "only a component the manifest lists can start" is wider than the docs: activities, services and content providers must be declared; broadcast receivers can also be registered in code (developer.android.com/guide/components/fundamentals). Make each claim match the documented case, a screen.
   - Line 93 old: `The system can only start a component that the manifest lists.` New: `For the main kinds of component, such as a screen of the app, the system can't start one that the manifest does not list.`
   - Line 171 old: `A maker writes a new component, a part the system can start.` New: `A maker writes a new screen for the app, a part the system can start.`
   - Line 206 old: `The documentation says that a component not declared in the manifest cannot be started by the system.` New: `The documentation says that a screen left out of the manifest is not visible to the system and can never run.`
   (mechanical; no answer key changes)
3. Same file, lines 91, 92, 93 (jargon used before it is bold and defined, `docs/04` §7).
   - Line 91 old: `The phone's own runtime executes it.` New: `The phone's own program for running apps executes it.`
   - Line 92 old: `such as the translations of a string.` New: `such as the same text in each language.`
   - Line 93 old: `the permissions the app needs,` New: `the **permissions** the app needs, which are the things it asks the phone to let it use,`
   (mechanical)

## Leaf 3: what-happens-when-i-build: FIX

1. `content/foundation/how-an-app-is-born/what-happens-when-i-build.md`, line 38 (Where the analogy stops). Seven sentences; `docs/04` §4.2 says one or two. Old: the whole paragraph after `**Where the analogy stops:**`. New: `**Where the analogy stops:** A bakery stamp is ink, but a real stamp here is a digital mark made with a key, which is a secret code that only the maker holds. Test boxes get the house stamp without being asked, so in daily work you never see the stamp station missing. The animation takes the stamp away to make a point.` (The docs rule on signing is already in Name it, line 93, and the bread sentence adds nothing.) (mechanical)
2. Same file, line 95 (jargon: "tasks" is used without a plain meaning).
   - Line 95 old: `The documentation says you can run all the build tasks of an Android project with it, and that adding the Android Gradle plugin, an add-on, registers the tasks needed to build an APK.` New: `The documentation says you can run every build step of an Android project with it. It also says the Android Gradle plugin, an add-on, registers the steps needed to build an APK.`
   (mechanical)
3. Same file, line 207 (You can now). `Put the four build steps in order and say what each one makes.` Only the order is tested by a Predict item (`stations-in-order`); "what each one makes" has no matching task (`docs/04` §4.12). New: `- Put the four build steps in order.` (mechanical)

Optional (not FIX): line 195 is 31 words; split after "insecure by design." Add to line 85 "Real builds have many more tasks than four." Step 6 (line 81) says each station uses what the one before made; this is the writer's simplification and is covered by the line 85 label.

## Leaf 4: how-an-app-gets-installed: FIX

1. `content/foundation/how-an-app-is-born/how-an-app-gets-installed.md`, line 85 (Name it, signature). "a key that only the maker holds" conflicts with the signing page: with Play App Signing, Google manages the app signing key and signs the APKs for distribution. That is the path this leaf teaches (the shop seals the parcel, Build step 4). Old: `The seal is a **signature**, a digital mark made with a key that only the maker holds.` New: `The seal is a **signature**, a digital mark made with a private key, which is a secret code. With **Play App Signing**, the maker lets Google keep that key, and Google does the sealing for the maker.` (mechanical; Build step 4 and the analogy stay as they are)
Optional (not FIX): line 89 is 27 words with two ideas; split after "ID number." The delivery slip (Mental model) and the key (locked room) are never mapped in Name it or reused in Build; the locked room and sandbox have no Predict item although Recap 3 teaches them (Recap does not require one, You can now does).

## Cross-leaf notes

- Sims `phone-kitchen-two-ways`, `flat-pack-app-box`, `bakery-build-line`, `parcel-to-phone` are one-line entries in `docs/sims/needed-ids.md`. Not blocking (L31 ruling). The text under each marker works with the sim off.
- Recurring FIXes from W3-a and W3-b checked in all four: one-sentence Recap (one hit, art FIX 2); Question at most 5 sentences (none over); jargon bold and defined before use (app-file FIX 3); real stdout (n/a, no code); no next-lesson promise (none).
- Go-live step for `housekeeper`: strip the line-18 SOURCES comment from each leaf, keep the SIM markers, set `status`, `publishedOn`, `updatedOn`, and clear the roadmap warning signs in the Notes cells.

## After the FIX items

Re-check only that the edited lines match the New text. No answer key changes. All four leaves become PASS once their FIX items are applied.
