# interview: security

## security/threat-thinking/who-would-attack-my-app
interview-questions:
- [intern] What are the primary threat actors who might attack a mobile application (malicious users, botnets, rogue apps, Man-in-the-Middle attackers)?
- [junior] Why do mobile apps present a unique security challenge compared to server-side web applications (client environment is fully controlled by the user)?
- [mid] What is threat modeling (e.g., STRIDE framework), and how does it help identify security vulnerabilities during design phases?
- [senior] What goes wrong in production when engineering teams assume client-side validation and security checks are sufficient without server-side enforcement?
what-interviewers-listen-for:
- The client device is untrusted; anyone can decompile APKs, intercept network traffic, or run rooted emulators.
- Client-side security checks (UI validations, token checks) can always be bypassed by a determined attacker.
- Server-side validation is mandatory for all business logic and permissions.
build-exercise: Decompile a simple release APK using `apktool` or JADX, and inspect the generated Smali/Java source code to see how easily client logic is exposed.
official-references:
- Android security overview | https://developer.android.com/topic/security
common-wrong-beliefs:
- If you hide API keys or validation logic inside compiled Kotlin code or native C++ libraries, users cannot inspect or reverse-engineer them | Any code shipped to a client device can be decompiled, reverse-engineered, or debugged by a determined attacker.

## security/secure-storage/where-data-lives-on-a-phone
interview-questions:
- [intern] Where is app data stored locally on an Android device (`filesDir`, `cacheDir`, `SharedPreferences`, SQLite databases)?
- [junior] Can another app on the same phone read your app's private files on a standard non-rooted device?
- [mid] What security risks arise when files are saved to external storage (`getExternalFilesDir` or shared public storage)?
- [senior] What goes wrong in production when storing unencrypted sensitive data (tokens, passwords, PII) in plain SharedPreferences files?
what-interviewers-listen-for:
- App-private storage is sandboxed by Linux UIDs, protecting files from other apps on non-rooted devices.
- External or shared storage is accessible to other apps with storage permissions.
- Plaintext storage of credentials on rooted devices allows immediate credential theft.
build-exercise: Save a plain text password in SharedPreferences, inspect the app's internal XML storage directory using Android Studio Device File Explorer on an emulator, and view the plaintext file.
official-references:
- Data storage security | https://developer.android.com/privacy/security/storage
common-wrong-beliefs:
- Data saved in app-private `SharedPreferences` or internal files is automatically encrypted by the operating system | App-private storage is sandboxed from other apps, but the files are stored in plaintext unless explicitly encrypted via Jetpack Security EncryptedSharedPreferences.

## security/secure-storage/keystore-and-encryption
interview-questions:
- [intern] What is the Android Keystore system, and how does it protect cryptographic keys securely in hardware?
- [junior] How does Jetpack Security (`EncryptedSharedPreferences` / `EncryptedFile`) leverage the Android Keystore under the hood?
- [mid] Why must cryptographic keys be generated and stored inside hardware-backed secure enclaves (TEE/StrongBox) rather than raw files?
- [senior] What goes wrong in production when Keystore master keys become invalidated due to device lock screen credential changes without fallback handling?
what-interviewers-listen-for:
- Android Keystore stores cryptographic keys in hardware-backed secure storage (Trusted Execution Environment or StrongBox), preventing extraction even if the OS kernel is compromised.
- `EncryptedSharedPreferences` encrypts keys and values transparently using Keystore-backed keys.
- Changing device lock screen passwords can invalidate Keystore keys, throwing crypto exceptions that must be handled gracefully.
build-exercise: Initialize `EncryptedSharedPreferences` using Jetpack Security, save an encrypted string, and verify in Device File Explorer that the stored file contents are ciphertext.
official-references:
- Android Keystore system | https://developer.android.com/privacy/security/keystore
common-wrong-beliefs:
- You can safely encrypt local database files or tokens by writing your own custom AES encryption key hardcoded in source code | Hardcoded encryption keys can be extracted via decompilation; keys must be generated and stored securely in the Android Keystore.

## security/network-security/https-and-cleartext
interview-questions:
- [intern] What does HTTPS provide that plain HTTP traffic does not (encryption, integrity, authentication)?
- [junior] Why does modern Android block cleartext HTTP traffic (`android:usesCleartextTraffic="false"`) by default?
- [mid] How do Network Security Config XML files configure domain-specific cleartext policies and trust anchors?
- [senior] What goes wrong in production when an app falls back to unencrypted HTTP endpoints during server degradation or misconfiguration?
what-interviewers-listen-for:
- HTTPS encrypts transport data, preventing eavesdropping and tampering.
- Android 9+ blocks cleartext HTTP by default to protect user data from local Wi-Fi eavesdroppers.
- Network Security Config files define explicit domain trust rules.
build-exercise: Attempt to make an HTTP network request to an unencrypted endpoint, observe the security rejection exception, and configure a Network Security Config file.
official-references:
- Network security configuration | https://developer.android.com/privacy/security/network-security-config
common-wrong-beliefs:
- Running your app on a private home Wi-Fi network makes unencrypted HTTP traffic completely safe from interception | Anyone on the same local Wi-Fi network can sniff unencrypted HTTP traffic using tools like Wireshark or Charles Proxy.

## security/network-security/network-security-config-and-pinning
interview-questions:
- [intern] What is SSL certificate pinning, and how does it prevent Man-in-the-Middle (MitM) proxy attacks?
- [junior] How do you configure certificate pinning using Android Network Security Config or OkHttp `CertificatePinner`?
- [mid] What are the risks of certificate pinning (e.g., app bricking when server certificates expire or rotate)?
- [senior] What goes wrong in production when pinned certificates expire or rotate without embedding backup pins or remote fallback configurations?
what-interviewers-listen-for:
- Certificate pinning hardcodes trusted server certificate fingerprints or public keys in the app, rejecting rogue intermediate CAs used by interception proxies (e.g., Burp Suite).
- Expiration or rotation without backup pins causes permanent app breakage until an update is installed.
build-exercise: Configure OkHttp `CertificatePinner` with a pin for your test server domain, and verify successful connection against valid certificates.
official-references:
- Certificate pinning | https://developer.android.com/privacy/security/network-security-config#CertificatePinning
common-wrong-beliefs:
- Once you implement SSL certificate pinning, your app's network traffic is 100% immune to interception forever | If server certificates rotate without backup pins included in the app, all users lose connectivity when certificates expire.

## security/auth/why-tokens-not-passwords
interview-questions:
- [intern] Why should a mobile app store a short-lived auth token (like a JWT) instead of saving the user's raw password locally?
- [junior] What is the difference between an Access Token and a Refresh Token?
- [mid] Why must refresh tokens be stored securely (in EncryptedSharedPreferences) while short-lived access tokens can reside in memory?
- [senior] What goes wrong in production when access tokens lack proper expiration lifetimes or scopes, exposing sensitive user data if intercepted?
what-interviewers-listen-for:
- Tokens limit exposure window; if an access token leaks, it expires quickly. Refresh tokens acquire new access tokens securely.
- Saving raw passwords locally is a severe security vulnerability.
build-exercise: Implement a secure token manager that saves access tokens in memory and refresh tokens in EncryptedSharedPreferences.
official-references:
- Authentication best practices | https://developer.android.com/privacy/security/authentication
common-wrong-beliefs:
- Saving the user's login username and password plaintext inside a local SQLite database for "convenience auto-login" is standard practice | Saving raw user passwords locally violates security standards; secure OAuth tokens should be used instead.

## security/auth/sign-in-with-credential-manager
interview-questions:
- [intern] What is Credential Manager in Android, and what authentication methods does it support (passkeys, passwords, federated sign-in)?
- [junior] Why are passkeys replacing traditional passwords and SMS One-Time Passwords (OTPs)?
- [mid] How do you integrate Google Sign-In and passkey authentication using the Credential Manager API?
- [senior] What goes wrong in production when legacy authentication flows lack multi-factor or passkey support, exposing users to credential stuffing and phishing?
what-interviewers-listen-for:
- Credential Manager unifies sign-in methods into a single modern Jetpack API supporting biometric-backed passkeys.
- Passkeys eliminate phishing and password reuse risks using public-key cryptography.
build-exercise: Integrate Credential Manager request builders to support passkey sign-in flows in an Android app.
official-references:
- Credential Manager overview | https://developer.android.com/training/sign-in/credential-manager
common-wrong-beliefs:
- Implementing custom password hashing and storage inside your app's local database is safer than using platform-supported Credential Manager and passkeys | Custom auth flows are prone to security vulnerabilities; Credential Manager provides robust platform-secured authentication.

## security/privacy-and-permissions/ask-for-less-data
interview-questions:
- [intern] What is the principle of least privilege regarding data collection in mobile applications?
- [junior] Why does Google Play enforce strict privacy disclosures and data safety form submissions?
- [mid] How do you audit your app to ensure it only requests permissions and collects data strictly necessary for core functionality?
- [senior] What goes wrong in production when requesting excessive background location or contacts data without justification, resulting in Play Store suspension?
what-interviewers-listen-for:
- Principle of least privilege: collect only the minimum data required to deliver core app features.
- Google Play Data Safety policies require transparent disclosures of all data collected and shared.
build-exercise: Audit your app manifest permissions, remove any unused dangerous permissions, and review data safety disclosure requirements.
official-references:
- Play Console Data safety section | https://support.google.com/googleplay/android-developer/answer/10787469
common-wrong-beliefs:
- Collecting as much user data as possible "just in case we need it later for analytics or marketing" complies with modern mobile app store privacy guidelines | App stores penalize and suspend apps that collect excessive user data without clear user disclosure and operational necessity.

## security/privacy-and-permissions/tell-users-what-you-collect
interview-questions:
- [intern] What are your legal and platform obligations regarding privacy policies and data collection transparency?
- [junior] When must your app display an in-app privacy consent notice (e.g., GDPR, COPPA, ATT)?
- [mid] How do ad SDKs and analytics trackers impact user privacy compliance?
- [senior] What goes wrong in production when third-party SDKs harvest user data without consent, resulting in massive regulatory fines and store bans?
what-interviewers-listen-for:
- Apps must provide accessible privacy policies and obtain explicit user consent before harvesting personal or device data.
- Third-party SDK data collection must be disclosed and audited.
build-exercise: Review third-party SDK dependencies in your project and verify that data collection matches Play Store Data Safety declarations.
official-references:
- Google Play Families policy and privacy | https://support.google.com/googleplay/android-developer/answer/9893335
common-wrong-beliefs:
- If a third-party analytics or ad SDK collects user data automatically, app developers are not responsible for privacy compliance | Developers are fully responsible for all data collected by any SDK integrated into their app.

## security/obfuscation/what-obfuscation-really-hides
interview-questions:
- [intern] What does R8 code shrinking and obfuscation actually do to your compiled DEX classes and method names?
- [junior] Does turning on R8 obfuscation make your app completely secure against reverse engineering or hide embedded API keys?
- [mid] How do ProGuard/R8 keep rules (`-keep`) prevent obfuscation from breaking reflection and serialization?
- [senior] What goes wrong in production when missing R8 keep rules strip required classes, causing `ClassNotFoundException` only in release builds?
what-interviewers-listen-for:
- Obfuscation replaces human-readable class and method names with meaningless characters (`a`, `b`, `c`), making reverse engineering harder.
- Obfuscation does NOT encrypt code or hide hardcoded strings/API keys; reverse-engineering tools can still inspect strings.
build-exercise: Enable R8 minification, decompile the release APK using JADX, and observe how class names are obfuscated while string literals remain visible.
official-references:
- Shrink, obfuscate, and optimize your app | https://developer.android.com/build/shrink-code
common-wrong-beliefs:
- Enabling R8 code obfuscation completely encrypts your source code and hides all hardcoded API keys and string constants from hackers | Obfuscation renames symbols; plain text string constants and API keys remain fully visible in decompiled binaries.

## security/webviews/webview-risks
interview-questions:
- [intern] What are the major security risks when displaying web pages inside a `WebView` (`setJavaScriptEnabled`, `addJavascriptInterface`)?
- [junior] What is Cross-Site Scripting (XSS) and how can malicious injected scripts compromise a WebView?
- [mid] Why is `addJavascriptInterface` considered a dangerous security hazard in older Android versions (arbitrary code execution)?
- [senior] What goes wrong in production when WebView clients load untrusted remote URLs without validating SSL errors or restricting URL navigation?
what-interviewers-listen-for:
- WebViews can execute malicious JavaScript, access local storage, or bridge unsafe Java interfaces if misconfigured.
- Disable JavaScript unless strictly required, and validate all loaded URLs (`shouldOverrideUrlLoading`).
build-exercise: Configure a secure WebView by disabling file access, enabling safe navigation checks, and restricting unnecessary JavaScript interfaces.
official-references:
- Security tips for WebViews | https://developer.android.com/privacy/security/webview
common-wrong-beliefs:
- Displaying a trusted web page inside a `WebView` is completely secure and inherits all native app security sandbox protections | WebViews process untrusted web content and require strict security hardening to prevent XSS and code injection attacks.

## security/app-integrity/can-the-server-trust-the-app
interview-questions:
- [intern] How can your backend server verify whether an incoming API request is coming from a genuine, unmodified copy of your app versus a malicious bot or modified APK?
- [junior] What is Google Play Integrity API, and how does it check device and app licensing status?
- [mid] How do root detection, emulator detection, and hardware attestation work?
- [senior] What goes wrong in production when servers blindly trust client requests without cryptographic app integrity attestation tokens?
what-interviewers-listen-for:
- Play Integrity API provides cryptographic verdicts verifying that requests originate from genuine app binaries running on untampered devices.
- Client-side root detection can be bypassed by advanced attackers; backend integrity verification is essential.
build-exercise: Integrate Play Integrity API dependency, request an integrity token, and verify how servers decode and validate the token payload.
official-references:
- Play Integrity API | https://developer.android.com/google/play/integrity
common-wrong-beliefs:
- Client-side root detection checks written in Kotlin code are 100% foolproof and prevent rooted phones from running your app | Determined attackers can patch bytecode or hook functions with Frida to bypass client-side root checks; server-side integrity verification is required.

## security/secrets-and-logs/secrets-in-source-code
interview-questions:
- [intern] Why is hardcoding an API key, database password, or client secret directly inside your Kotlin source code or Git history dangerous?
- [junior] How do tools scan public GitHub repositories for leaked credentials within seconds of pushing?
- [mid] How do you manage secrets securely using local `local.properties` files injected into `BuildConfig` or Gradle fields?
- [senior] What goes wrong in production when secret API keys leaked in git commit history are exploited by bots to incur massive cloud billing charges?
what-interviewers-listen-for:
- Code committed to Git history is public or easily extractable via APK decompilation.
- Secrets must never be stored in source code; use environment variables, secure build configs, or backend proxy tokens.
build-exercise: Scan your repository git history for accidentally committed secrets using `git log -S` or truffleHog, and remove them using `git filter-repo`.
official-references:
- Secure coding guidelines | https://developer.android.com/privacy/security/secure-coding
common-wrong-beliefs:
- If you add your API key to a private GitHub repository or delete the commit later, the secret is completely safe and cannot be recovered | Git history retains all past commits; once pushed publicly, secrets must be immediately revoked and rotated.

## security/secrets-and-logs/sensitive-data-in-logs
interview-questions:
- [intern] Why must sensitive user data (passwords, auth tokens, PII, credit card numbers) never reach Logcat or crash reporting tools (Firebase Crashlytics)?
- [junior] How do automated crash reporters capture uncaught exceptions, and why do they sometimes capture local variable states containing sensitive data?
- [mid] How do you configure ProGuard/R8 or custom logging wrappers to strip debug log statements in release builds?
- [senior] What goes wrong in production when auth tokens printed to Logcat during debugging are harvested by malicious malware apps with `READ_LOGS` permission?
what-interviewers-listen-for:
- Logcat output can be read by other apps on older Android versions or debug tooling, leaking sensitive tokens.
- Production builds must disable or strip verbose logging statements.
build-exercise: Search your codebase for raw `Log.d` or `println` statements containing variables, and replace them with a safe logging utility that scrubs sensitive parameters.
official-references:
- Logging security | https://developer.android.com/privacy/security/log-security
common-wrong-beliefs:
- Printing full network JSON responses containing user auth tokens to Logcat during development is fine as long as you remember to delete the lines later | Developers frequently forget to remove log statements, leaking sensitive production data into device logs.

## security/putting-it-together/spot-three-leaks
interview-questions:
- [intern] Can you review a short code snippet and identify security vulnerabilities and data leaks?
- [junior] What are the three most common security flaws found in mobile code reviews (plaintext storage, hardcoded secrets, unvalidated intents)?
- [mid] How do automated static analysis tools (like MobSF or Android Lint security checks) catch common vulnerabilities during CI builds?
- [senior] What goes wrong in production when security audits are deferred until right before app launch instead of integrating continuous threat modeling?
what-interviewers-listen-for:
- Spotting hardcoded keys, plaintext storage, unvalidated intents, and missing HTTPS enforcement.
- Automated security scanners in CI catch common vulnerabilities early.
build-exercise: Review a provided insecure code snippet containing an unencrypted SharedPreferences save, a hardcoded API key, and an unvalidated intent, and refactor it securely.
official-references:
- OWASP Top 10 Mobile Risks | https://owasp.org/www-project-mobile-top-10/
common-wrong-beliefs:
- Conducting a single security pen-test right before releasing your app guarantees it is secure against all future attacks | Security is an ongoing process requiring continuous dependency auditing, static analysis, and threat modeling.

## Self-check
1. Android security overview | https://developer.android.com/topic/security (Verify URL path)
2. Data storage security | https://developer.android.com/privacy/security/storage (Verify URL path)
3. Android Keystore system | https://developer.android.com/privacy/security/keystore (Verify URL path)
4. Network security configuration | https://developer.android.com/privacy/security/network-security-config (Verify URL path)
5. Credential Manager overview | https://developer.android.com/training/sign-in/credential-manager (Verify URL path)
6. Play Console Data safety section | https://support.google.com/googleplay/android-developer/answer/10787469 (Verify URL)
7. Shrink, obfuscate, and optimize your app | https://developer.android.com/build/shrink-code (Verify path)
8. Play Integrity API | https://developer.android.com/google/play/integrity (Verify path)
9. Secure coding guidelines | https://developer.android.com/privacy/security/secure-coding (Verify path)
10. OWASP Top 10 Mobile Risks | https://owasp.org/www-project-mobile-top-10/ (Verify external OWASP URL)