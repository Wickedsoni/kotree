# Review L1-a: first-leaf shadow leaves (networking, os, profiling, security)

Reviewer: independent, 21 Oct 2026. Checklist: `docs/lesson-checklist.md`; structure: `docs/04` sections 4 to 6.

Leaves:
- `content/networking/how-the-internet-works/clients-and-servers.md`
- `content/os/what-an-os-does/what-the-os-does-for-apps.md`
- `content/profiling/measure-first/measure-before-you-fix.md`
- `content/security/threat-thinking/who-would-attack-my-app.md`

## What passed

- Frontmatter matches the roadmap YAML rows (id, title, difficulty easy, requires [], fruit false). `sims` match: `[]`, `[]`, `[measure-bars]`, `[app-house-doors]`. Both sim ids are in `docs/sims/needed-ids.md`. One `<!-- SIM: id -->` marker each, in the correct form.
- `gradlew :site:validateContent` reports one violation in total, and it is in an unrelated file (see FIX 9). None of the four leaves is flagged.
- Section order is correct. The interview section sits before Recap, with 4 questions each. Recaps are 3 one-sentence lines. SOURCES comments are on line 18. No stray VERIFY marks. No banned words. No "next lesson" text.
- Prose length (Question to before Interview): 1370, 1383, 1390 and 1570 words. All are inside the 900 to 1600 budget (security is close to the limit).
- No Kotlin block exists in any of the four leaves, so there is nothing to compile with `scripts/kt-run.sh`. The `verified: kotlin-2.4.21` stamp does not apply, and `verified: not-run` is correct for paper traces. Predict arithmetic traced by hand: 60+200+40 = 300; 40/2 = 20 saved, so 300-20 = 280; 60 ms is less than a third of 200 ms. The keys are true. The correct letter varies (a/c, b/a, c/a, a/b). OS has no "Practise on these" by instruction (practice-os has `problems: none`).

## Facts verified against official pages (read 21 Oct 2026)

- `developer.android.com/training/basics/network-ops/connecting`: INTERNET and ACCESS_NETWORK_STATE are normal permissions, granted at install time. `NetworkOnMainThreadException` is thrown for main-thread network work. The page says HttpsURLConnection supports "configurable timeouts". All three are verified.
- `developer.android.com/guide/platform`: the Linux kernel is the foundation, and from Android 5.0 each app runs in its own process with its own ART instance. Verified.
- `developer.android.com/media/optimize/audio-focus`: only one app holds focus at a time. From Android 12 the system forces the playing app to fade out. Before Android 12 the system cannot stop an app that plays loudly. Verified (but see FIX 6 on the fade-out conditions).
- UID claim (`source.android.com/docs/security/app-sandbox`): the page body is blocked from this environment (the proxy returns 403, DNS fails). Two web searches return the AOSP page's text: "Android assigns a unique user ID (UID) to each Android application and runs it in its own process." The claim is supported by a search excerpt only. I could not read the page body itself.
- `.../risks/log-info-disclosure`: "Since Android 4.1 (API level 16), only privileged system apps can be granted access to read logcat, by declaring the READ_LOGS permission", and pre-loaded apps on some devices declare it. The page also says "Don't log sensitive data". Verified, including the leaf's "tells you not to log sensitive data".
- `.../security-tips`: HTTPS and public Wi-Fi hotspots; internal files are accessible only to your app by default; external storage files are "globally readable and writable"; logs are a shared resource readable with READ_LOGS. All verified verbatim.
- `.../topic/performance/vitals/render`: the release or non-debuggable advice and ART disabling optimizations; under 16 ms for 60 fps; method traces give false-positive janks; Profile GPU Rendering, Systrace and Perfetto are named. All verified.
- Practice-file links: `topic/performance/profiling` and `training/volley/request-counter` both return 404, so the writer was right not to use them. `topic/security` returns 200 and is not needed.

Unverified (general knowledge, labelled as such in the leaves): what a client and server are, "the cloud", premature optimization, attack surface and threat modelling, what a process is, scripts attacking many apps, and decompiling an app file.

## FIX list

1. `security/who-would-attack-my-app.md`, Predict `stranger-on-wifi` and `lock-all-but-one` (checklist 3). "plain HTTP", "HTTPS", "internal storage" and "external storage" are used in the Predict code before they are defined. They are first explained in "Where this shows up in Android". Add one-line definitions in "Name it" or before the Predict section. Mechanical.
2. `profiling/measure-before-you-fix.md`, Name it paragraph "One more rule" and Tricks/Common mistakes (checklist 3). "release", "debug" and "non-debuggable" builds are never explained. Add one clause: a debug build is made for testing and carries extra debugging support; a release build is the one users get. Also give Systrace and Perfetto a short gloss such as "recording tools". Mechanical.
3. Unlabelled inference (checklist 4). `networking/clients-and-servers.md` "Where this shows up in Android", sentence "an app that never stops waiting leaves the spinner on the screen": add "(my own reasoning)". `os/what-the-os-does-for-apps.md` Mental model, "thousands of times a second": remove it or label it "(general knowledge)". `networking` Mental model, "thousands of customers at the same moment": label it "(general knowledge)". Mechanical.
4. `profiling/measure-before-you-fix.md`, Notice and Guess. "The author of the app" and "the author is surprised" clash with the site's own "author" (the Kotree author) and with checklist 7's ban on "the author is learning". Replace with "the developer". Mechanical.
5. Author-supplied data not used (checklist 13). No `build-exercise` appears in Try it or Tricks in the networking, profiling or security leaves:
   - Ktor/Retrofit fetch (networking).
   - Record a CPU trace after a blind optimisation (profiling).
   - Decompile a release APK with apktool or JADX (security).
   The security `common-wrong-beliefs` seed (secrets hidden in compiled code can be read) is also missing from Common mistakes, and the networking seed (the phone works alone) is only partly covered. Needs the owner or writer to decide: add one optional "Tricks to remember" line each, or record in `docs/NEEDS-ME.md` that the exercises are deferred because these leaves have no playground.
6. `os/what-the-os-does-for-apps.md`, The question and "Where this shows up". The leaf says the music fades out as a certainty. The audio-focus page lists conditions: the first app uses USAGE_MEDIA or USAGE_GAME, it holds AUDIOFOCUS_GAIN, it is not playing speech content, and the second app requests AUDIOFOCUS_GAIN. "the manager made it happen" overstates this. Soften with "when both are media apps" or "often". The Predict `remove-the-manager` option c ("The phone refuses to start") is not a believable guess (checklist 6). Replace it with a plausible one, for example "The louder app wins and the other goes silent". Mechanical for the wording; the owner may choose the replacement option.
7. `security/who-would-attack-my-app.md`, sim Try-this step 1 and table: "installed package" is used where Mental model says "installed app file". Use one phrase everywhere (docs/04 section 7: same word for the same thing). Mechanical.
8. Housekeeper on go-live: the networking roadmap row line 192 still carries "VERIFY: the INTERNET permission is granted at install time". This is now verified, so drop the marker. Also strip the SOURCES comments. Mechanical.
9. Out of scope but blocking `validateContent` for the whole site: `content/kotlin/reading-code/reading-order-for-code.md:8` has invalid YAML frontmatter ("mapping values are not allowed here"). Needs its owner.

Not run: the full `wave-check.sh` export. None of these leaves has code, and `validateContent` was the relevant gate.

VERDICT: FIX
