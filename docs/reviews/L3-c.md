# Review L3-c (layer 3 drafts C, commit 90d7b57)

Reviewer: independent. Date: 21 Oct 2026. Leaves: profiling/cpu/find-the-hot-function, security/secure-storage/keystore-and-encryption, testing/unit-tests/arrange-act-assert, tools/git/merge-conflicts, ui/first-composable/text-and-images.

## Method
- `./gradlew :site:validateContent` printed `validateContent: ok` (SM0 to SM11 green for all five sims).
- Every runnable Kotlin snippet was compiled and run with `bash scripts/kt-run.sh` (Kotlin 2.4.21). All outputs match the leaves: profiling 8 snippets (Name it, 2 predicts, Try-it break and fix, fix-the-bug before and after, plus the const-val block in security); testing 4 programs (Name it model with the 31 break, both predicts, fix before and after). Merge-conflicts and UI have no runnable Kotlin.
- Every sim's Try steps were traced by hand against `SimCore.fold`, including hoisted toggles, and against `TryProgressCalc`. Every `done` is reachable. I did not open `/preview/<id>` in a browser, so the mouse, keyboard-only and reduced-motion passes are NOT done here (template-level behaviour, not leaf data).
- Facts were checked against official pages fetched on 21 Oct 2026 (list under each leaf).
- Section order, frontmatter vs roadmap (id, title, difficulty, requires, fruit), sims list, needed-ids entries, 3 options per predict, varied correct letters, 2 hints per fix, run:false and `verified: not-run` on non-playground leaves: all correct.

## 1. profiling/cpu/find-the-hot-function: FIX

Facts verified: instrumentation timestamps at entry and exit, five seconds or less, timing might deviate (record-java-kotlin-methods); Find CPU Hotspots (Callstack Sample), Android 8.0 (API 26) (sample-callstack); flame chart merges identical stacks, axis is relative not a timeline, most CPU time appears first (flame-chart); Self, Total, Bottom Up useful for sorting by CPU time (top-bottom-charts); "flame chart or top down chart" (record page). Sim numbers add up (8+20+62+10 = 100; exact-on bars all taller than sampled bars; after "remember" buildReceipt is tallest in both modes).

1. Line 28, stray `<!-- ⚠️ VERIFY: compile not run ... -->`. The code now compiles and matches. Delete the line (checklist 9). Mechanical.
2. Challenge ids `five-calls-five-answers`, `ask-once-before-the-loop`, `fix-tax-lookup-in-loop`.
   Old: `verified: "UNVERIFIED: compile not run"`
   New: `verified: kotlin-2.4.21`
   Mechanical. Also SOURCES line 24:
   Old: `- Kotlin code: compile not run (Kotlin 2.4.21, docs/06 section 5). Outputs traced by hand.`
   New: `- Kotlin code: compiled and run with Kotlin 2.4.21 (scripts/kt-run.sh, reviewer, 21 Oct 2026); outputs match.`
3. Try step 3 (sim). A toggle replays the whole history (hoisting), so the bars grow at the moment of the switch and the second "Record the app" changes nothing visible. The step says to compare with the first recording, which the learner can no longer see.
   Old: `Switch on "Record every call" and press "Record the app" again. Compare the bars with the first recording.`
   New: `Switch on "Record every call" and watch the bars grow. Then press "Record the app" again and read what it says.`
   `done` stays `["exact=on", record]`. Mechanical.
4. Sim `say` of case `exact: "off", fixed: "no"` is clumsy.
   Old: `The recording takes a look at the running function many times a second.`
   New: `The recording looks at what is running many times a second.`
   Mechanical.
5. Name it, sampling paragraph: "Sampling costs little" is unlabelled (checklist 4).
   Old: `Sampling costs little. A call that is shorter than the gap between two looks can be missed (general knowledge).`
   New: `Sampling costs little, and a call that is shorter than the gap between two looks can be missed (general knowledge).`
   Mechanical.

## 2. security/secure-storage/keystore-and-encryption: FIX

Resolution of the VERIFY on names (checked on developer.android.com reference pages, 21 Oct 2026):
- `KeyGenParameterSpec.Builder(String keystoreAlias, int purposes)`, `setBlockModes(String...)`, `setEncryptionPaddings(String...)`, `build()` returning `KeyGenParameterSpec`: confirmed.
- `KeyProperties.KEY_ALGORITHM_AES`, `PURPOSE_ENCRYPT` (int), `PURPOSE_DECRYPT` (int), `BLOCK_MODE_GCM`, `ENCRYPTION_PADDING_NONE`: confirmed.
- `Cipher.getIV()` (Kotlin `cipher.iv`): confirmed (Android javax.crypto reference). The class intro of `KeyGenParameterSpec` has the same example: `KeyGenerator.getInstance(KEY_ALGORITHM_AES, "AndroidKeyStore")`, `init(Builder("key2", PURPOSE_ENCRYPT | PURPOSE_DECRYPT).setBlockModes(BLOCK_MODE_GCM).setEncryptionPaddings(ENCRYPTION_PADDING_NONE).build())`, `Cipher.getInstance("AES/GCM/NoPadding")`, `cipher.init(ENCRYPT_MODE, key)`. The leaf's block matches it.
- Keystore page: key material never enters the app process, attacker might use but not extract keys, more difficult to extract, non-exportable, TEE, StrongBox "stronger isolation and tamper resistance compared to the TEE", `FEATURE_STRONGBOX_KEYSTORE`, biometric-only key invalidated by default on new enrollment: all confirmed.
- Cryptography page: AES CBC or GCM with 256-bit keys such as AES/GCM/NoPadding; "you must specify a provider" with Android Keystore; all security-crypto APIs deprecated in stable 1.1.0, no further releases: confirmed.
- Roadmap claim (EncryptedSharedPreferences class deprecated since 1.1.0; MasterKey deprecation points to `javax.crypto.KeyGenerator` with the AndroidKeyStore instance): confirmed through a search result of the official reference pages. The reference page body does not render without JavaScript, so I could not quote it directly.
- Not verifiable: the exact default AES key size of the Keystore (reference says only "specific to each key algorithm").

Sim trace: all three Try steps reachable; every label, mark and `say` agrees with the prose and the predicts.

1. Lines 25 and 26, two `⚠️ VERIFY` comments. Both are resolved (names confirmed above; the compilable const-val block was run and prints `The key is a plain line in the code: not-a-real-key-1234`; the leaf states no output, so nothing else changes). Delete both lines. VERIFY must not remain. Mechanical.
2. SOURCES, add after line 21:
   `- https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder, .../KeyProperties, .../javax/crypto/Cipher (read 21 Oct 2026): constructor (alias, purposes), setBlockModes, setEncryptionPaddings, build, the constants KEY_ALGORITHM_AES, PURPOSE_ENCRYPT, PURPOSE_DECRYPT, BLOCK_MODE_GCM, ENCRYPTION_PADDING_NONE, getIV(); the class intro of KeyGenParameterSpec has the same AES/GCM example.`
   and replace line 22:
   Old: `Not re-read in this run because the reference page text was too long to fetch.`
   New: `Re-checked 21 Oct 2026 by search of the official reference pages: EncryptedSharedPreferences deprecated since 1.1.0; MasterKey deprecation points to javax.crypto.KeyGenerator with the AndroidKeyStore instance.`
   Mechanical.
3. Name it, library paragraph: "deprecated" is used before it is defined (checklist 3).
   Old: `has all its APIs deprecated, according to the Android cryptography page, and there will be no more releases.`
   New: `has all its APIs **deprecated**, which means they are marked as no longer recommended, according to the Android cryptography page, and there will be no more releases.`
   Mechanical.
4. "biometric" appears in Common mistakes, Where this shows up and the interview answer without a definition (checklist 3). Add at the end of the StrongBox paragraph in Name it:
   New sentence: `A key can also be tied to the user's **biometric** sign-in, such as a fingerprint or a face scan (general knowledge).`
   Mechanical.
5. Code does not match the "256-bit keys" the leaf quotes. The snippet leaves the key size to the default. `setKeySize(int)` exists on `KeyGenParameterSpec.Builder`.
   Old: `        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)\n        .build()`
   New: `        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)\n        .setKeySize(256)\n        .build()`
   Mechanical (the owner may prefer to leave it out and drop "with 256-bit keys" instead).
6. End of Name it.
   Old: `This page has no program to run. The Keystore needs a real phone.`
   New: `The Keystore needs a real phone, so the vault code above cannot run in the editor.`
   Reason: the page just showed a block that "compiles and runs", so "no program to run" contradicts it. Mechanical.

## 3. testing/unit-tests/arrange-act-assert: FIX

Facts verified: local tests live in `module-name/src/test/`, test method begins with `@Test` and exercises a single aspect (local-tests page); the page's example has `// Given`, `// ...when`, `// ...then` comments; `check(value)` and `check(value, lazyMessage)` throw `IllegalStateException` (Kotlin stdlib). `assertEquals(expected, actual)` argument order is correct. The 31 break prints `FAIL totalOfTwoItems: expected 31 but saw 30`. All predicts and the fix (before: `PASS oneItem`, `FAIL twoItems: expected 30 but saw 40`; after: two PASS) match.

Sim trace: Try 1 (four `next`) lights l1 to l4, pc 5, expected 30. Try 2 (two `next`) runs l5 (total 30) and l6 (pass). Try 3 (optional): `again`, `wrong=on`, six `next` ends on `fail` with the matching `say`. The toggle is hoisted but `again` is the last action, so the state is clean. Prose matches.

1. Line 26, stray `<!-- ⚠️ VERIFY: compile not run ... -->`. Delete. Mechanical.
2. Challenge ids `which-test-fails-and-says-what`, `a-second-act-hides-the-cause`, `fix-shared-cart-between-tests`.
   Old: `verified: "UNVERIFIED: compile not run"`
   New: `verified: kotlin-2.4.21`
   Also SOURCES line 23: replace `compile not run (Kotlin 2.4.21, docs/06 section 5). Outputs traced by hand.` with `compiled and run with Kotlin 2.4.21 (scripts/kt-run.sh, reviewer, 21 Oct 2026); outputs match.` Mechanical.

## 4. tools/git/merge-conflicts: PASS

Facts verified on the Git book (Basic Merge Conflicts): same part of the same file differently, "Unmerged paths", HEAD is the top part above `=======`, "Staging the file marks it as resolved in Git", `git mergetool`. JetBrains page: Conflicts dialog, Accept Yours (keep the checked-out branch), Accept Theirs (accept the merged-in branch), left and right read-only panes, editable central pane. Unverified (labelled in the leaf): Android Studio is built on IntelliJ (general knowledge); "Git does not check marker lines on staging" (labelled my own reasoning, true in practice).

Sim trace (zones): Try 1 `join` with `same` off gives "Joined on its own". Try 2 `same=on` is hoisted, so the history replays and `join` now runs the conflict case (three marker items spawned, `conflict` yes). Try 3 `mark-resolved` gives "Marked resolved, but marker lines are still on the page" and the conflict stays. Optional Try 4 `keep-both` removes the three markers, then `mark-resolved` gives "Resolved and saved". All `remove` targets exist, no refusal. Prose matches. No runnable Kotlin; the marker block is marked `// does not compile`. Playground false is correct.

## 5. ui/first-composable/text-and-images: PASS

Facts verified on the Compose pages: `Text("Hello World")`, string resources recommended for internationalization, `Text(stringResource(R.string.hello_world))`; `painterResource` loads PNG, JPEG, WEBP or vector, `Image(painter = painterResource(id = R.drawable.dog), contentDescription = stringResource(id = R.string.dog_content_description))` (no size in the example); TalkBack reads contentDescription, null for purely decorative; third-party libraries such as Coil handle caching and networking; `strings.xml` named on the images page. The roadmap says the leaf's contentDescription is taught in accessibility; the leaf teaches it briefly with the official wording, which is acceptable.

Sim trace (compare): five independent actions, no conditions. Try 1 (`show-text`, `words-resource`), Try 2 (`show-photo`), Try 3 (`add-description`), optional Try 4 (`decoration`) are reachable in order; labels and `say` lines match the prose and the three predicts. No `phone` add-on, so no caption is required. No runnable Kotlin; code block is marked `// does not compile`.

## Unverified claims (not blocking)
- Browser behaviour of the five sims (mouse, keyboard only, reduced motion, live region): not run in this review.
- Default Keystore AES key size (see leaf 2, item 5).

VERDICT: FIX

## Fixes applied
- profiling/cpu/find-the-hot-function: items 1-5 applied (VERIFY line removed, verified kotlin-2.4.21 on 3 challenges, SOURCES line, Try 3 wording, say text, sampling sentence).
- security/secure-storage/keystore-and-encryption: items 1-6 applied (VERIFY lines removed, SOURCES added and re-check line, deprecated defined, biometric defined, setKeySize(256) added, closing sentence). Item 5 applied as the owner-preferred option is not known; setKeySize(256) kept.
- testing/unit-tests/arrange-act-assert: items 1-2 applied (VERIFY line removed, verified kotlin-2.4.21 on 3 challenges, SOURCES line).
- tools/git/merge-conflicts and ui/first-composable/text-and-images: PASS, no edits.
- validateContent: ok (run after edits).
