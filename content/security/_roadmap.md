```yaml
branch: security
bough: craft
stems:
  - id: threat-thinking
    title: Threat Thinking
    why: A fresher cannot protect an app until they can name who might attack it and through which door.
    leaves:
      - id: who-would-attack-my-app
        title: "How do I think like someone who wants to attack my app?"
        difficulty: easy
        requires: []
        fruit: false
  - id: secure-storage
    title: Secure Storage
    why: Apps save tokens and personal data on the phone, and generated code often saves them in the easiest, least safe place.
    leaves:
      - id: where-data-lives-on-a-phone
        title: "Which of my app's saved data can another app or person read?"
        difficulty: easy
        requires: [security/threat-thinking/who-would-attack-my-app]
        fruit: false
      - id: keystore-and-encryption
        title: "How can an app lock data with a key that the app itself cannot copy?"
        difficulty: hard
        requires: [security/secure-storage/where-data-lives-on-a-phone]
        fruit: false
  - id: network-security
    title: Network Security
    why: Phones join untrusted Wi-Fi all day, so what travels over the network must be unreadable and trusted.
    leaves:
      - id: https-and-cleartext
        title: "What stops someone on the same Wi-Fi from reading my app's traffic?"
        difficulty: medium
        requires: [security/threat-thinking/who-would-attack-my-app]
        fruit: false
      - id: network-security-config-and-pinning
        title: "When should my app trust only one certificate, and what breaks when it changes?"
        difficulty: hard
        requires: [security/network-security/https-and-cleartext]
        fruit: false
  - id: auth
    title: Sign-In and Tokens
    why: Almost every app has a login, and keeping the wrong thing after sign-in turns one leak into a stolen account.
    leaves:
      - id: why-tokens-not-passwords
        title: "Why should the app keep a short-lived token and never the password?"
        difficulty: medium
        requires: [security/secure-storage/where-data-lives-on-a-phone]
        fruit: false
      - id: sign-in-with-credential-manager
        title: "How can users sign in with a passkey or saved account instead of typing a password?"
        difficulty: medium
        requires: [security/auth/why-tokens-not-passwords]
        fruit: false
  - id: privacy-and-permissions
    title: Privacy and Permissions
    why: Collecting less data is the safest protection, and stores and laws expect apps to say what they collect.
    leaves:
      - id: ask-for-less-data
        title: "What is the least data my app needs?"
        difficulty: easy
        requires: [security/threat-thinking/who-would-attack-my-app]
        fruit: false
      - id: tell-users-what-you-collect
        title: "What must I tell people about the data my app collects?"
        difficulty: easy
        requires: [security/privacy-and-permissions/ask-for-less-data]
        fruit: false
  - id: obfuscation
    title: Obfuscation
    why: Many freshers think code shrinking hides secrets, and that false comfort leads to keys shipped inside apps.
    leaves:
      - id: what-obfuscation-really-hides
        title: "If I turn on code shrinking, is the API key in my app hidden?"
        difficulty: medium
        requires: [security/threat-thinking/who-would-attack-my-app]
        fruit: false
  - id: webviews
    title: WebViews
    why: A WebView puts a web page inside the app, and one open door between page and app can hand control to a stranger.
    leaves:
      - id: webview-risks
        title: "What can go wrong when my app shows a web page inside itself?"
        difficulty: medium
        requires: [security/network-security/https-and-cleartext]
        fruit: false
  - id: app-integrity
    title: App Integrity
    why: A server cannot see the phone, so it needs a way to tell a real copy of the app from a fake one.
    leaves:
      - id: can-the-server-trust-the-app
        title: "How can my server tell a genuine copy of my app from a fake one?"
        difficulty: hard
        requires: [security/obfuscation/what-obfuscation-really-hides, security/auth/why-tokens-not-passwords]
        fruit: false
  - id: secrets-and-logs
    title: Secrets and Logs
    why: Keys committed to git and tokens printed to Logcat are the most common leaks in real apps and in generated code.
    leaves:
      - id: secrets-in-source-code
        title: "Why is an API key in my code or my git history already public?"
        difficulty: medium
        requires: [security/obfuscation/what-obfuscation-really-hides]
        fruit: false
      - id: sensitive-data-in-logs
        title: "Why must a token or a password never reach Logcat or a crash report?"
        difficulty: easy
        requires: [security/threat-thinking/who-would-attack-my-app]
        fruit: false
  - id: putting-it-together
    title: Putting It Together
    why: The branch ends with one skill employers check first, which is reading a short piece of code and spotting what leaks.
    leaves:
      - id: spot-three-leaks
        title: "Can I read a short snippet and name three places where it leaks data?"
        difficulty: medium
        requires: [security/secure-storage/where-data-lives-on-a-phone, security/network-security/https-and-cleartext, security/secrets-and-logs/secrets-in-source-code, security/secrets-and-logs/sensitive-data-in-logs]
        fruit: true
```

## Branch prerequisites

All leaves are `shadow` on 18 Oct 2026. Nothing is published. Every stem is phase 6. Every `Assumes` target sits in phase 5 or lower, or in a phase-6 branch that comes earlier in the `docs/08` §10.1 tie-break (here only `tools`). Security comes after `kmp`, so `kmp` and `native-ndk` link to Security with `Helps to know` only. There is no Android emulator or simulator (L15): Android-behaviour sims are `code-to-screen`; steps with no Android screen are plain animations.

Android, Play and library facts below were checked on 18 Oct 2026 against developer.android.com; each one is marked again with `⚠️ VERIFY` in the leaf where it is used, because security guidance changes between releases.

- Assumes Foundation: `foundation/how-computers-work/memory-vs-storage` (phase 1) — used by where-data-lives-on-a-phone.
- Assumes Foundation: `foundation/debugging-mindset/check-code-you-did-not-write` (phase 1) — used by spot-three-leaks and every `F(ai):` task.
- Assumes Kotlin: `kotlin/reading-code/reading-order-for-code` and `kotlin/reading-code/spotting-risky-patterns` (phase 3) — used by spot-three-leaks.
- Assumes Kotlin: `kotlin/strings/everyday-string-functions` (phase 2) — snippets build strings that carry tokens into logs.
- Assumes Tools and ecosystem: `tools/git/what-is-a-commit` and `tools/git/remotes-and-pull-requests` (phase 2) — used by secrets-in-source-code (history keeps old snapshots).
- Assumes Tools and ecosystem: `tools/adb-and-logcat/reading-logcat` (phase 5) — used by sensitive-data-in-logs.
- Assumes Tools and ecosystem: `tools/publishing/release-builds-and-signing` (phase 6, earlier branch in the tie-break) — used by what-obfuscation-really-hides and can-the-server-trust-the-app.
- Assumes Android core and lifecycles: `android-core/files-and-sharing/app-private-files` (phase 5) — used by where-data-lives-on-a-phone.
- Assumes Android core and lifecycles: `android-core/permissions/why-permissions` and `android-core/permissions/when-the-user-says-no` (phase 5) — used by ask-for-less-data.
- Assumes Android core and lifecycles: `android-core/manifest-and-resources/what-is-the-manifest` (phase 5) — used by https-and-cleartext and webview-risks.
- Assumes Architecture and data: `architecture-data/key-value-storage/store-a-setting-with-datastore` (phase 4) — used by where-data-lives-on-a-phone and keystore-and-encryption.
- Assumes Networking inside apps: `app-networking/http-client/first-get-call` (phase 4) — used by https-and-cleartext.
- Assumes Networking inside apps: `app-networking/auth-tokens/how-apps-prove-who-they-are` and `app-networking/auth-tokens/refreshing-an-expired-token` (phase 4) — used by why-tokens-not-passwords and can-the-server-trust-the-app.
- Assumes Networking inside apps: `app-networking/http-client/interceptors-and-logging` (phase 4) — used by sensitive-data-in-logs (a logging interceptor can print headers).
- Assumes Computer networking: `networking/tls/why-https` (phase 4) — used by https-and-cleartext.
- Assumes Computer networking: `networking/tls/certificates-and-trust` (phase 4) — used by network-security-config-and-pinning.
- Helps to know OS: `os/processes/what-is-a-process` — used by where-data-lives-on-a-phone (each app runs in its own sandbox).
- Helps to know Testing: `testing/test-doubles/what-is-a-test-double` — a leak can be proved with a fake logger.
- Helps to know Native layer: `native-ndk/native-memory/native-memory-has-no-gc` — native buffer overflows are a security risk; named once.

Challenge note: Android framework code does not run in the Kotlin Playground. Every `P:` and `F:` on framework code is a plain-Kotlin model (for example a fake logger or a fake preferences map that prints what an outside reader would see) or a read-only "which line leaks" task. `F(ai):` tasks use tidy, confident snippets with one real defect, and the framing is "Would you accept this in review?" (`docs/01` §3.5). ⚠️ VERIFY with `docs/06` once written.

## Stems

### threat-thinking · Threat Thinking

Flags: phase=6

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `who-would-attack-my-app` | easy | "Given an app, how do I list who wants its data and which door they would use?" | A house with doors, windows and a spare key under the mat: a burglar does not pick the strongest lock, he tries the weakest door. | "Nobody would bother attacking a small app." | sim: a floor plan of an app drawn as a house (screen, files, network, logs, installed package); a thief ball tries each door; the learner places locks and sees the thief take the unguarded door | `P:` on the plan: which door does a stranger on the same Wi-Fi use · `B:` lock every door but one and predict the thief's path | Names four attackers: a snoop on the network, another app on the phone, a person holding the phone, a person with a copy of the app file. Fact basis: developer.android.com security tips (checked 18 Oct 2026). |

### secure-storage · Secure Storage

Flags: phase=6 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `where-data-lives-on-a-phone` | easy | "If I save a token in preferences or in a downloads folder, who can read it?" | A hotel: your room is locked to you, the corridor is open to everyone, and a note slid under the door can be read by anyone who finds it. | "Anything I save on the phone is private to my app." | sim: code-to-screen a phone drawn as private rooms and a public hall; flip code variants (internal file, external file, plain preferences) and watch where the file lands and whether a snooper ball can pick it up | `P:` which of four save calls leaves the file readable by other apps · `F(ai):` a tidy helper that writes the login token to external storage "so it survives uninstall" · `F(ai):` a screen that saves a password in plain preferences | volatile. Assumes Foundation: `foundation/how-computers-work/memory-vs-storage`. Assumes Android core: `android-core/files-and-sharing/app-private-files`. Checked 18 Oct 2026 (security tips): internal storage is private to your app by default; external storage is readable and writable by others; do not store user passwords on the device, use a short-lived token. ⚠️ VERIFY: Auto Backup and data-extraction rules for what leaves the phone, and `allowBackup` default for new targets. |
| `keystore-and-encryption` | hard | "How can an app lock data with a key that no code, even the app's own, can copy out?" | A bank's safe-deposit box: you hand the clerk the item to lock and the clerk locks it; the master key never leaves the vault. | "Encrypting data means keeping the key in a constant in my code." | sim: a vault box on the screen; the learner sends plain data in and gets locked data out; trying to drag the key out is refused; a second vault with the key in the code lets a thief copy it | `P:` which of three designs stores the key where an attacker can read it · `F(ai):` a generated helper that encrypts with a hard-coded key · `F(ai):` a call to the old EncryptedSharedPreferences class on a new project · `B:` move the key out of the vault in the sim | volatile. Checked 18 Oct 2026 (security tips): use the Android Keystore for keys; generate keys with `SecureRandom`; consider Tink. Checked 18 Oct 2026 (developer.android.com androidx.security.crypto reference): the classes `EncryptedSharedPreferences` and `MasterKey` are marked deprecated, pointing to plain `SharedPreferences` and a `KeyGenerator` with the `AndroidKeyStore` provider; Google's direction for plain settings is DataStore. ⚠️ VERIFY again before live: the recommended pairing (DataStore plus Tink is community advice, not confirmed as official), StrongBox support, and biometric-bound keys (named only). Assumes Architecture and data: `architecture-data/key-value-storage/store-a-setting-with-datastore`. |

### network-security · Network Security

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `https-and-cleartext` | medium | "Why does a plain http link stop working in my app, and why is that good?" | A postcard versus a sealed, tamper-evident envelope: anyone who handles a postcard can read it, and only the receiver can open the envelope. | "A padlock icon means the app is safe." | sim: a letter travels through three hands across a coffee-shop Wi-Fi; as a postcard the snoop in the middle reads it, as a sealed envelope the snoop sees only a scrambled lump | `P:` on the sim: what does the snoop read for an http call and for an https call · `F(ai):` generated config that turns cleartext on for every domain "to make the API work" · `B:` move the snoop to the server side and note what changes | Assumes Computer networking: `networking/tls/why-https`. Assumes Networking inside apps: `app-networking/http-client/first-get-call`. Checked 18 Oct 2026 (developer.android.com security-config): cleartext is allowed by default at API 27 and lower, and blocked by default at API 28 and higher. ⚠️ VERIFY again before live: current wording and any change for newer target levels. |
| `network-security-config-and-pinning` | hard | "When is it worth trusting only one certificate, and what breaks when the server changes it?" | A door guard who knows one visitor by a photo: very safe, but when the visitor changes face and nobody updated the photo, the guard turns the real visitor away. | "Pinning makes my app impossible to attack." | sim: a guard at a gate holds one photo; the server rotates its certificate; without a backup photo the app is locked out until an update ships; with a backup photo it keeps working | `P:` after a certificate change, which of two apps still connects (with and without a backup pin) · `F(ai):` generated config with one pin and no backup pin · `B:` set an expiry date in the past and see which apps are exposed | volatile. Checked 18 Oct 2026 (security-config): pins are SHA-256 hashes of the public key; always include a backup pin; pinning is skipped for certificates added by `debug-overrides`; the page also mentions opt-in certificate transparency on Android 16 and on by default from Android 17. ⚠️ VERIFY the Android 16 and 17 transparency wording before live. |

### auth · Sign-In and Tokens

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-tokens-not-passwords` | medium | "If the phone is stolen, why is a saved token less dangerous than a saved password?" | A festival wristband that stops working after the day, compared with handing over your house key. | "Saving the password is fine because it is on the user's own phone." | sim: a wristband with a timer; a thief copies the band and uses it until it expires, while a copied password works forever; the learner shortens the timer and moves the band into a vault | `P:` on the sim: how long can a thief use a copied token that lasts 15 minutes · `F(ai):` a generated login that stores the user's email and password for "auto login" · `B:` revoke the token on the server side | Assumes Networking inside apps: `app-networking/auth-tokens/how-apps-prove-who-they-are`, `app-networking/auth-tokens/refreshing-an-expired-token`. Checked 18 Oct 2026 (security tips): do not store user passwords on the device; use a short-lived, service-specific authorisation token. |
| `sign-in-with-credential-manager` | medium | "How does one sign-in sheet offer a passkey, a saved password or a Google account?" | A building reception that checks your face, your card or a visitor slip at one desk, instead of three separate doors. | "A passkey is a password with a longer name." | sim: code-to-screen the sign-in sheet changes as the learner flips code variants (passkey only, password only, both, none available) and shows which options appear | `P:` on the sim: which options show when the user has no passkey · `F(ai):` a generated sign-in that keeps its own password field and ignores the sheet · `B:` remove the Google option from the request and see the sheet | volatile. Checked 18 Oct 2026 (developer.android.com/identity/credential-manager): Credential Manager is the recommended Jetpack API for credential exchange and unifies passkeys, passwords and federated sign-in such as Sign in with Google. ⚠️ VERIFY: minimum Android and library versions on its prerequisites page, and the server-side passkey steps. |

### privacy-and-permissions · Privacy and Permissions

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `ask-for-less-data` | easy | "What is the least data this feature needs, such as a picker or approximate location instead of full access?" | A waiter who asks only for your order, not your home address: less written down means less that can be lost. | "More data is always better, so ask for everything in case it is useful." | none: a decision rule with a two-column table (want / need) is clearer than motion | `P:` match five features to the smallest data they need · `F(ai):` a generated app asking for contacts, precise location and full storage for a simple notes screen · `B:` remove one permission and say what still works | Assumes Android core: `android-core/permissions/why-permissions`, `android-core/permissions/when-the-user-says-no`. ⚠️ VERIFY: photo picker and approximate-location options as of the publish date. |
| `tell-users-what-you-collect` | easy | "What do I write in the store's data form so it matches what my app really collects?" | A food label: it lists what is inside, so nobody finds a surprise later. | "A privacy policy is only for big companies." | none: a form-reading skill; a worked sample form on the page is the practice | `P:` which of four SDKs in a sample app make the form list "location" · `F(ai):` a generated form that says "no data collected" for an app with analytics · `B:` add one SDK and update the form | volatile. ⚠️ VERIFY: Google Play's current Data safety form fields and policy rules on the publish date. This is store practice, not legal advice. |

### obfuscation · Obfuscation

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-obfuscation-really-hides` | medium | "If I turn on code shrinking, can a stranger still read the API key and the web addresses in my app?" | Taking the labels off a shop's shelves: the goods are still in the window, and only the names are gone. | "Obfuscation encrypts my code and my secrets." | sim: a shop window before and after; shelf labels (class and function names) change to a, b, c, but price tags (text strings) stay readable; the learner tries to hide a key and finds it | `P:` on the sim: which of five things in the app remain readable after shrinking · `F(ai):` a generated build file that turns on shrinking and leaves a key in a string constant · `B:` split a key into two strings and see whether a reader can still join them | volatile. Assumes Tools and ecosystem: `tools/publishing/release-builds-and-signing`. R8 is named at overview level; tuning is in Profiling's `## Later`. ⚠️ VERIFY: what R8 shrinking, optimisation and name changes do today, from developer.android.com "Shrink, obfuscate, and optimize your app". |

### webviews · WebViews

Flags: phase=6

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `webview-risks` | medium | "What can a web page do to my app if I turn on JavaScript and add a bridge?" | A window cut into your shop wall, with a door behind it: anyone outside can wave, but only if the door is unlocked can they walk in and press your till. | "A WebView is just a browser, so it is as safe as a browser." | sim: code-to-screen a page box inside an app box; flip code variants (JavaScript off, JavaScript on, JavaScript on with a bridge); a page ball from an untrusted address crosses into the app only when the bridge is open | `P:` on the sim: which variant lets the page call the app's method · `F(ai):` tidy code that enables JavaScript and adds a bridge for a URL taken from an intent · `B:` load only a page packaged inside the app and see the risk drop | Checked 18 Oct 2026 (security tips): do not enable JavaScript if the page does not need it; use `addJavascriptInterface()` only with fully trusted pages, ideally bundled in the app; do not trust data over insecure protocols; call `clearCache()` after sensitive data. ⚠️ VERIFY again before live. |

### app-integrity · App Integrity

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `can-the-server-trust-the-app` | hard | "Why must the check that my app is genuine happen on my server and not inside the app?" | A theatre ticket: the usher at the door checks it against the box-office list, and a forger cannot fool the list by waving a nicer-looking ticket. | "I can detect a fake copy of my app with an if-statement inside the app." | sim: a server bouncer asks the phone for a signed note from the store; the learner runs the check in the app, and a fake app simply skips it, then moves the check to the server | `P:` on the sim: which of three designs lets a fake app get an answer · `F(ai):` a generated check that decides locally and sends "trusted: true" to the server · `B:` replay an old note and see why a fresh request is needed | volatile. Checked 18 Oct 2026 (developer.android.com/google/play/integrity/overview): Play Integrity verdicts cover app, device and account details; the backend server decides to allow, limit or deny; use it alongside other anti-abuse measures; do not cache verdicts; use a request hash or nonce against replay. ⚠️ VERIFY again before live: quotas, device-tier labels and any change to the API name or flow. Assumes Tools and ecosystem: `tools/publishing/release-builds-and-signing`. |

### secrets-and-logs · Secrets and Logs

Flags: phase=6 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `secrets-in-source-code` | medium | "I deleted my key from the file; why is it still public?" | A key taped to a shop window and later removed: everyone who walked past already took a photo. | "Deleting the key in a new commit removes it from the world." | sim: reuse `tools/git/what-is-a-commit` sim, with a key in an old snapshot; the learner steps back through history and finds it, then ignores the file and sees the next snapshots stay clean | `P:` after a commit that deletes the key, which snapshots still show it · `F(ai):` a generated config with a live API key in a Kotlin constant · `F(ai):` a `.gitignore` that lists local.properties after the file was already committed | Assumes Tools and ecosystem: `tools/git/what-is-a-commit`, `tools/git/remotes-and-pull-requests`. Checked 18 Oct 2026 (security tips): never commit API keys to source control; use a secrets Gradle plugin. ⚠️ VERIFY: current plugin name and the safest place for a build-time key. A key in the app file can still be read; keys that must stay secret live on a server. |
| `sensitive-data-in-logs` | easy | "Why can a token printed to Logcat end up in a stranger's hands?" | Talking about a password out loud in a busy train carriage: everyone nearby hears it, and a phone log is a very busy carriage. | "Logs are private and only I see them." | sim: reuse `tools/adb-and-logcat/reading-logcat` sim, with a leaky tap that drops a token into the log river; the learner closes it with a release-only filter | `P:` which of five log lines would leak something · `F(ai):` a generated logging interceptor that prints full headers including Authorization · `F(ai):` debug prints of the user's email in a release build | Assumes Tools and ecosystem: `tools/adb-and-logcat/reading-logcat`. Assumes Networking inside apps: `app-networking/http-client/interceptors-and-logging`. Checked 18 Oct 2026 (security tips): logs can be read by apps holding the `READ_LOGS` permission; never log personal data or secrets; limit logging in production with a wrapper. ⚠️ VERIFY: `READ_LOGS` access rules on current Android. |

### putting-it-together · Putting It Together

Flags: phase=6 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `spot-three-leaks` | medium | "Given a short snippet, can I name three places where it leaks data, and say what to do instead?" | A home inspector with a checklist who walks every room and names the three worst leaks before the paint is blamed. | "Security checks are a separate job for a specialist after the app is done." | none: a reading skill; the practice is a tap-to-flag snippet in the editor, not motion | `F(ai):` a 25-line generated login screen with a key in a constant, a token logged on error and a password saved in plain preferences · `F(ai):` a generated upload helper that writes a document to external storage and sends it over http · `P:` rank the leaks by what an attacker gains | Synthesis leaf. Fruit (see `## Fruits`). Proves the `docs/01` §6.2 security row: "What I must never store or log; spot 3 leaks in a short snippet." Uses only leaves at or before it. |

## Fruits

| Leaf | Capstone | Constraints | Proves |
|---|---|---|---|
| `security/putting-it-together/spot-three-leaks` | "A leak audit of your own app" | Pick one app you built or a 100-line sample you did not write · list every place data is saved, sent or printed · for each place write who could read it and what they would gain · name your three worst leaks, each tied to a file and line · fix at least one and show the output before and after · no secret may appear in your write-up | The `docs/01` §6.2 security ability: "what I must never store or log" and "spot 3 leaks in a short snippet" (L24). |

## Later

- Reverse-engineering walkthrough, because it needs tools and an attacker's view beyond a first full app.
- Root and tamper detection, because it is fragile and often bypassed; Play Integrity covers the safe part.
- Cryptography internals, because a fresher should use a library, not write ciphers.
- Full OWASP MASVS mapping, a useful checklist after the basics ⚠️ VERIFY name.
- Biometric prompts in depth, named once in keystore-and-encryption.
- Certificate transparency details, named once in the pinning leaf.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First version: 10 stems, 15 leaves, all shadow. | +15 | Est. 14, band 11-17: inside the band. Sketch had 9 stems and 14 leaves. Added the stem `putting-it-together` with one synthesis leaf, `spot-three-leaks`, so the branch ends on the `docs/01` §6.2 ability and the fruit sits on the last leaf (F2). Stem slugs `secure-storage`, `network-security`, `auth`, `secrets-and-logs` and `app-integrity` are kept because `app-networking` and `kmp` already point at them. Split auth into tokens and Credential Manager (split test). |
| 2026-10-18 | A2e review fixes: reworded `who-would-attack-my-app`, `ask-for-less-data` and `tell-users-what-you-collect` to one idea each (slugs unchanged; the store-form and picker details stay inside those single ideas). | 0 | No leaf added or removed. |
| 2026-10-18 | Sim share note (A2e review): 12 of 15 leaves have a `sim:` cell (80 %), above the 15–25 % guide in `docs/08` §6. Kept because most security leaves are about where data travels or stays (flow and state), and two reuse the `tools/git` and `tools/adb-and-logcat` sims, so about 10 are new; `physics-designer` should cut any that only repeat a diagram. | 0 | Logged, no leaf cut. |
