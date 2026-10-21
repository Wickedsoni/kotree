---
id: security/secure-storage/keystore-and-encryption
title: "How can an app lock data with a key that the app itself cannot copy?"
status: shadow
difficulty: hard
order: 3
requires: [security/secure-storage/where-data-lives-on-a-phone]
summary: The Android Keystore does the locking for your app, so a key can be used but never copied out.
video: ""
publishedOn:
updatedOn:
sims: [safe-deposit-vault]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://developer.android.com/privacy-and-security/keystore (read 21 Oct 2026): the Keystore lets you store cryptographic keys in a container to make them more difficult to extract; key material never enters the application process; if the app process is compromised the attacker might be able to use the app's keys but cannot extract the key material; key material can be bound to secure hardware (TEE or Secure Element); StrongBox gives stronger isolation than the TEE; a key that supports only biometric credentials is invalidated by default when new biometric enrollments are added; KeyStore.getInstance("AndroidKeyStore") with load(null).
- https://developer.android.com/privacy-and-security/cryptography (read 21 Oct 2026): AES in CBC or GCM mode with 256-bit keys, such as AES/GCM/NoPadding; with the Android Keystore you must specify a provider; all APIs in the security-crypto Jetpack library were deprecated in version 1.1.0 stable and there will be no more releases.
- Earlier check recorded in content/security/_roadmap.md (18 Oct 2026): EncryptedSharedPreferences and MasterKey marked deprecated on the reference pages. Not re-read in this run because the reference page text was too long to fetch.
-->

<!-- ⚠️ VERIFY: compile not run for any kotlin block (Kotlin 2.4.21, docs/06 section 5). The first block needs Android and is not meant to run. -->
<!-- ⚠️ VERIFY: the method and constant names KeyGenParameterSpec.Builder, setBlockModes, setEncryptionPaddings, KeyProperties.BLOCK_MODE_GCM, KeyProperties.ENCRYPTION_PADDING_NONE and Cipher.iv come from general knowledge. The reference pages could not be read in this run. Check them on developer.android.com before go-live. -->
## The question

An app saves a sign-in code on the phone. Anyone who gets the saved file could read it, so you scramble the code with a secret number. Now where do you keep that secret number? If it sits inside the app, can't a thief copy it too?

## Mental model

Think of a safe-deposit box at a bank.

- The vault is a locked room that only the bank's clerk can open.
- The clerk is the one who does the locking. You never lock anything yourself.
- The note is the data you want to protect.
- The master key is the secret that locks and unlocks. It stays in the vault.
- The thief wants a copy of that key.

You hand the clerk a note and ask for it to be locked. The clerk locks it and gives you the locked note. Later you hand the locked note back, and the clerk gives you the words. At no point do you hold the master key.

**Where the analogy stops:** A bank clerk checks who you are. The vault on a phone serves the app that made the key. So a thief who gets code running inside your app can still ask the clerk to lock and unlock, even though the thief can never take the key away. Also, on some phones the vault is a separate chip. On others it is protected by software. The rules about that are in Name it.

## See it move

```sim
id: safe-deposit-vault
template: compare
title: "A vault and a key written in code"
intro: "Two apps keep a secret note. The left app has a vault and a clerk, so its key stays inside the vault. The right app wrote its key as a line in its own code. The thief's pockets are empty."
sides:
  - id: vault
    label: "Key in the vault (Android Keystore)"
    boxes:
      - { id: note, label: "Note: plain words" }
      - { id: key, label: "Key: inside the vault, never handed out" }
      - { id: pocket, label: "Thief's pocket: empty" }
  - id: code
    label: "Key written in the app's code"
    boxes:
      - { id: note, label: "Note: plain words" }
      - { id: key, label: "Key: a plain line in the code" }
      - { id: pocket, label: "Thief's pocket: empty" }
actions:
  - id: lock
    label: "Lock the note"
    do:
      - label:
          vault.note: "Note: locked by the clerk (scrambled)"
          code.note: "Note: locked with the key from the code (scrambled)"
      - light: [vault.note, code.note]
      - say: "Both notes are scrambled. On the left the clerk did the locking and the key stayed in the vault. On the right the app used the key from its code."
  - id: steal-vault
    label: "Thief tries to copy the key from the vault"
    do:
      - mark: { vault.key: good }
      - light: [vault.key]
      - say: "The clerk refuses to hand over the key. The thief's pocket stays empty."
  - id: steal-code
    label: "Thief tries to copy the key from the code"
    do:
      - label:
          code.pocket: "Thief's pocket: a copy of the key"
      - mark: { code.key: bad }
      - light: [code.key, code.pocket]
      - say: "The key is a plain line in the code, so the thief reads it and copies it. Now the thief can unlock the note anywhere."
  - id: inside-thief
    label: "A thief inside the app asks the vault to unlock"
    do:
      - label:
          vault.pocket: "Thief's pocket: the note's words, still no key"
      - light: [vault.pocket]
      - say: "The clerk serves the app, so a thief running inside the app can still get the note opened. The key itself still never leaves the vault."
script: [lock, steal-vault, steal-code, inside-thief]
try:
  - text: 'Press "Lock the note". Both notes become scrambled.'
    done: [lock]
  - text: 'Press "Thief tries to copy the key from the vault", then "Thief tries to copy the key from the code". Compare the two pockets.'
    done: [steal-vault, steal-code]
  - text: 'Press "A thief inside the app asks the vault to unlock" and read what the pocket holds.'
    done: [inside-thief]
    optional: true
```

**What you should notice:** The vault never hands out the key, so the thief's pocket stays empty on that side, while a key written in the code is copied at once.

## Build the logic

### Notice

The app saves a sign-in code in a file. The code lets the user stay signed in. Someone who reads the file, for example from a copied backup, can use the code too.

So you scramble the code before you save it. Scrambling needs a secret number. The scrambled code is safe only if the secret number is.

### Guess

Where should the secret number live?

- A. In the app's code, as a plain line, so the app can always find it.
- B. In a file next to the scrambled code.
- C. In a place that does the locking for the app and never hands the number out.

Idea A means anyone who opens the app file reads the number. Idea B means a thief who gets the scrambled code gets the number with it. Idea C is the vault and the clerk.

### Build

1. Ask the vault to make a secret number, and give it a name so you can ask for it again.
2. Never ask for the number itself. Ask the clerk to lock or unlock things with it.
3. To lock a note, hand it to the clerk. You get back the scrambled note and a small extra number that the clerk needs later.
4. Save the scrambled note and the small extra number anywhere. They are no use without the vault.
5. To unlock, hand both back to the clerk. The clerk returns the words.
6. Plan for a lost key. If the vault can no longer use the key, the scrambled notes cannot be opened, so you must ask the user to sign in again.

### Name it

**Encryption** is scrambling data with a key, so that only someone with the key can read it again (general knowledge). A **key** is the secret that locks and unlocks. The scrambled result is called ciphertext, and the readable words are called plaintext (general knowledge).

The **Android Keystore** is the vault. The Android documentation says it lets you store cryptographic keys in a container to make them more difficult to extract from the device. It says the **key material**, which is the secret itself, never enters the application process. Once keys are in the keystore, you can use them for cryptographic operations while the key material stays non-exportable, which means it cannot be taken out. The documentation also says that if the app's process is compromised, the attacker might be able to use the app's keys but cannot extract their key material.

Key material can be bound to secure hardware. The documentation names the Trusted Execution Environment, or **TEE**, and says that when this is enabled for a key, its key material is never exposed outside of secure hardware. **StrongBox** is a stronger kind of secure hardware, in embedded secure elements, with stronger isolation and tamper resistance than the TEE, according to the documentation. Not every phone has it, and the documentation says to check with `FEATURE_STRONGBOX_KEYSTORE`.

The lock method is **AES**. The Android cryptography page recommends AES in CBC or GCM mode with 256-bit keys, for example `AES/GCM/NoPadding`. The small extra number from step 3 is called an **IV** (general knowledge). You need it to unlock, and it is not secret. The same page says that if you use the Android Keystore, you must name the provider. A provider is the part of Java that supplies the locking code (general knowledge), and here it is `"AndroidKeyStore"`.

A **hard-coded key** is a key written as plain text in the code. This is the design to avoid, because the key travels inside the app file (general knowledge).

Here is the vault design. It needs Android, so it builds only inside an Android project. A key is made once under a name, called an **alias**. Then the clerk does the locking.

```kotlin
// does not compile
// It needs Android, so it builds only inside an Android project.
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

fun makeVaultKey(alias: String): SecretKey {
    val generator = KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES,
        "AndroidKeyStore"
    )
    val spec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
    generator.init(spec)
    return generator.generateKey()
}

fun lockNote(key: SecretKey, note: ByteArray): Pair<ByteArray, ByteArray> {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    return Pair(cipher.iv, cipher.doFinal(note))
}
```

`lockNote` returns the small extra number first and the scrambled note second. The `SecretKey` object stands for the key. It is a handle for asking the vault to work, not a copy of the secret.

Here is the design to avoid. This one compiles and runs, and it is wrong.

```kotlin
// This compiles, but it is the wrong design.
// The key is a plain line of the app's own code, so anyone who opens the app file can read it.
const val SECRET_KEY = "not-a-real-key-1234"

fun main() {
    println("The key is a plain line in the code: $SECRET_KEY")
}
```

One more thing about libraries. Jetpack is Google's set of Android libraries (general knowledge). The Jetpack library `security-crypto`, which holds `EncryptedSharedPreferences` and `MasterKey`, has all its APIs deprecated, according to the Android cryptography page, and there will be no more releases. A tutorial that uses it may be old, so check how recent a tutorial is. My own reasoning: when you must lock data, use a well-reviewed library and do not write your own steps.

This page has no program to run. The Keystore needs a real phone.

## Predict the output

```challenge
type: predict
run: false
id: which-design-hides-the-key
verified: not-run
code: |
  Three apps lock a saved sign-in code. Each keeps its key in a different place.

    Design 1: the key is a line of code in the app (const val SECRET_KEY = "...").
    Design 2: the key is saved in a file in the app's folder, next to the locked code.
    Design 3: the key is made inside the Android Keystore, and the app only asks it to lock and unlock.

  A thief copies the app file and the saved data.
  In which design can the thief still not read the key?
options:
  - id: a
    text: "Design 1"
    why: "You may have thought the app file is hard to read. The key is a plain line in the code, so anyone who opens the app file can read it. It is the right-hand side of the sim."
  - id: b
    text: "Design 2"
    why: "You may have thought a private folder is a vault. The thief took the saved data, and the key sits in the same place, so the lock is no use."
  - id: c
    text: "Design 3"
    correct: true
    why: "The clerk does the locking and the documentation says the key material never enters the app process. A copy of the app file and the saved data holds no key."
output: |
  Design 3
```

```challenge
type: predict
run: false
id: thief-inside-the-app
verified: not-run
code: |
  An app keeps its key in the Android Keystore.
  A thief gets code running inside the app, on an unlocked phone.
  What can the thief do with the key?
options:
  - id: a
    text: "Copy the key material and use it on another computer"
    why: "You may have thought the vault is open to anything inside the app. The documentation says the key material cannot be extracted, even if the app's process is compromised."
  - id: b
    text: "Ask the vault to use the key, but not take the key away"
    correct: true
    why: "The clerk serves the app. The documentation says an attacker might be able to use the app's keys but cannot extract their key material. That is the last button in the sim."
  - id: c
    text: "Nothing at all, because the vault knows the thief is not the real app"
    why: "You may have thought the clerk checks who is asking, like a bank clerk. The vault serves the app, so a thief inside the app can still ask it to work."
output: |
  Ask the vault to use the key, but not take the key away
```

```challenge
type: predict
run: false
id: old-tutorial-library
verified: not-run
code: |
  You start a new app. A tutorial says to lock saved data with
  EncryptedSharedPreferences from the security-crypto library.
  What does the Android cryptography page say about that library?
options:
  - id: a
    text: "All its APIs were deprecated in version 1.1.0, and there will be no more releases"
    correct: true
    why: "The tool in the tutorial is old, even though the vault behind it is not. The documentation says all APIs in security-crypto were deprecated in the stable 1.1.0 release."
  - id: b
    text: "It is the only way to use the Android Keystore"
    why: "You may have thought the library and the vault are one thing. The Keystore is part of Android and you can call it directly, as in Name it."
  - id: c
    text: "It was removed from Android, so apps that use it stop building"
    why: "You may have thought deprecated means gone. Deprecated means that it is not recommended, and no new releases will come."
output: |
  All its APIs were deprecated in version 1.1.0, and there will be no more releases
```

## Tricks to remember

- Ask the clerk. Never ask for the key.
- A key in the code is a key in the thief's pocket.
- Scrambled note plus extra number go in the file. The key stays in the vault.
- The vault stops copying, not use. A thief inside the app can still ask.

## Common mistakes

- **Locking the data with a key written in the code.** It feels safe because the data is scrambled. The key travels in the app file, so a thief who opens the file has the key. Make the key in the Keystore and ask it to do the locking.
- **Thinking the Keystore makes an app safe from every thief.** It feels like a perfect vault. The documentation says that if the app's process is compromised, the attacker might still be able to use the keys. It stops the key from being copied out. It does not stop misuse while the app is under attack.
- **Copying EncryptedSharedPreferences from a tutorial into a new project.** The tutorial code looks fine and builds. The documentation says every API in that library is deprecated. Check the date of the tutorial and the page it came from.
- **Forgetting that a key can be lost.** The saved data looks like it will always be there. The documentation says a key that supports only biometric credentials is invalidated by default when new biometric enrollments are added. Keep a plan: ask the user to sign in again and save a new locked note.

## Where this shows up in Android

A user adds a new fingerprint to the phone. Your app then fails to open its saved sign-in code, and the user has to sign in again. This happens only if your key was set up to need biometric sign-in, and the documentation says such a key is invalidated by default when new biometric enrollments are added. The idea of a lost key from Build, step 6, explains it.

Here is a second symptom. A generated helper locks a token with a key stored in a constant, and a reviewer reads the app file and finds the key. The vault design in Name it is the repair.

## Say it in an interview

**What is the Android Keystore, and how does it protect keys?**
The documentation says it lets you store cryptographic keys in a container to make them more difficult to extract from the device. The key material never enters the application process, so my code asks the Keystore to lock and unlock and never holds the secret. If the app's process is compromised, an attacker might use the keys but cannot extract them.

**What happened to Jetpack Security and EncryptedSharedPreferences?**
The Android cryptography page says all APIs in the security-crypto library were deprecated in version 1.1.0 stable, with no more releases. An earlier check of the class pages, recorded in the roadmap, says the deprecation notes point to a plain `KeyGenerator` with the `AndroidKeyStore` provider. My own reasoning: I would not start a new project on it.

**Why should keys live in secure hardware and not in a file?**
The documentation says key material bound to secure hardware, such as the TEE, is never exposed outside it. StrongBox, in an embedded secure element, gives stronger isolation and tamper resistance, and you check `FEATURE_STRONGBOX_KEYSTORE`. A key in a file can be copied with the file, and secure hardware stops that copy.

**What goes wrong in production when a Keystore key becomes invalid?**
The documentation says a key that supports only biometric credentials is invalidated by default when new biometric enrollments are added. My own reasoning, from general knowledge: reading data locked with that key then fails with an error. The app must catch it, make a new key, and ask the user to sign in again. Data locked with the old key is lost.

## Recap

1. A key written in the code or saved next to the data can be copied with them.
2. The Android Keystore does the locking, and the key material never enters your app.
3. A thief inside your app can still use the key, and a key can be lost.

## You can now

- Explain why a key written in the app's code does not protect the data it locks.
- Pick, from three designs, the one where a copy of the app file holds no key.
- Predict what a thief inside the app can and cannot do with a Keystore key.
