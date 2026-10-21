---
id: security/secure-storage/where-data-lives-on-a-phone
title: "Which of my app's saved data can another app or person read?"
status: shadow
difficulty: easy
order: 2
requires: [security/threat-thinking/who-would-attack-my-app]
summary: Files in your app's own private area are closed to other apps, files in shared places are open, and a saved password is a risk in either.
video: ""
publishedOn:
updatedOn:
sims: [hotel-corridor-files]
playground: true
fruit: false
upnext: false
---

<!-- SOURCES (housekeeper strips at go-live):
- https://developer.android.com/privacy-and-security/security-tips : "By default, files that you create on internal storage are accessible only to your app"; external storage "globally readable and writable ... only store non-sensitive information"; "don't store user IDs and passwords on the device ... use a short-lived, service-specific authorization token"; the application sandbox and the unique UID per app.
- https://developer.android.com/training/data-storage : app-specific storage, shared storage, preferences ("Store private, primitive data in key-value pairs"), databases.
- https://developer.android.com/training/data-storage/app-specific : other apps cannot access internal storage; on Android 10 and higher "these locations are encrypted"; files in app-specific storage are removed on uninstall.
- https://developer.android.com/training/data-storage/shared/media : "Even after your app is uninstalled, these files remain on the user's device."
- https://developer.android.com/identity/data/autobackup : Auto Backup includes shared preferences files and files from getFilesDir() by default; android:allowBackup defaults to true for apps targeting Android 6.0 and higher; set it to false for sensitive data; tip to use Block Store for credentials and authentication tokens.
- Left out on purpose: EncryptedSharedPreferences (the roadmap records it as deprecated; keys are a separate topic); getExternalFilesDir access rules (the pages I read differ by Android version, so the Predict items use only "private" and "shared" places).
- Author data (docs/practice/interview-security.md) used as questions only. Its "Linux UID sandbox" point is verified above; its "plaintext on rooted devices" point is labelled general knowledge.
-->

## The question

Your app saves a login token and a few notes on the phone. The user closes the app, and the data is still there. Who else can open it, another app or a person holding the phone? And what changes if you save the file in a different place?

## Mental model

Think of a hotel.

- Your room is the private area your app gets. It is locked, and only your app has the key.
- The hall is a shared place. Anyone walking by can look at what is lying there.
- A note with a password written on it is sensitive data. It is risky wherever you leave it.
- The front desk is the phone's backup service. It takes a photo of your room each night and keeps it for you.

A guest does not leave a password note in the hall. But a guest also should not leave it in the room, if the note never needed to exist.

**Where the analogy stops:** A hotel has staff with a master key. On a phone, the owner of the phone and anything that runs with higher rights can get into more than your app can (general knowledge). Also, newer Android versions put limits on the hall, so "anyone" means any app that has the right permission. And a hotel has one front desk, but a phone's backup copy can leave the phone, which is why it is in the picture.

## See it move

<!-- SIM: hotel-corridor-files -->

**Try this:**
1. Pick a way to save a file: in the private room, or in the shared hall. Watch where the file lands.
2. Send the snooper ball along the hall and then to your room's door. See which files it can pick up.
3. (Optional reveal) Turn on "front desk photo" and see which files are in the photo.

**What you should notice:** The snooper picks up files in the hall, and it cannot open the private room.

## Build the logic

### Notice

Your app saves four things: a list of notes, a photo the user wants to share, some settings and a login token. You have to decide where each one goes.

The phone has more than one place to keep a file. Some places are only for your app. Others are meant to be seen by other apps.

### Guess

Where should the login token go?

- A. In your app's private place, and keep it short-lived.
- B. In the shared place, so it survives an uninstall.
- C. In the shared place, because the file is easier to find there.

Idea B sounds helpful, but anything that survives an uninstall is lying where others can see it. Idea C trades safety for ease. Idea A is the locked room.

### Build

1. List what your app saves.
2. For each item, ask: should another app ever see this?
3. If no, keep it in your private area. The Android documentation says files in internal storage are by default accessible only to your app.
4. If the user wants to share it, put it in a shared place on purpose.
5. Ask whether a password needs to be saved at all. The documentation says not to store user IDs and passwords on the device, and to use a short-lived authorization token instead.
6. Ask whether the backup copy should contain it.

### Name it

- The **sandbox** is the Android rule that isolates your app's data and code from other apps. The documentation says each app is given a unique user identifier, a **UID**, with very limited permissions.
- **Internal storage** is your app's private area. The documentation says that by default, files in it are accessible only to your app. The app-specific storage page adds that on Android 10 and higher these locations are encrypted.
- **External storage** is the shared kind of place. The documentation says files on external storage are globally readable and writable, so you should only store non-sensitive information there.
- **Preferences** are small saved settings kept as key-value pairs. The storage page calls them private, primitive data.
- A **token** is a short-lived pass that the server gives your app after sign-in. It stands in for the password.
- **Auto Backup** is the front desk. By default it includes shared preferences files and the files in your internal storage. The default for `android:allowBackup` is `true`, and the documentation suggests setting it to `false` if your app deals with sensitive information.

| Hotel | Official name | What it means |
|---|---|---|
| Your room | Internal storage | Only your app by default |
| The hall | External or shared storage | Other apps can read it |
| The sticky note with the password | A saved password | Risky anywhere; do not store it |
| The front desk photo | Auto Backup | Can copy private files off the phone |

My own reasoning: encrypted storage on Android 10 and higher keeps your files from other people. It does not hide them from your own app, which reads them as normal text. Locking data with a key that your app cannot copy is a different topic, and this page does not cover it.

The code below is a model in plain Kotlin, not real Android. It shows the rule with a map of file names to places.

```kotlin
// A model in plain Kotlin, not real Android.
enum class Place { PRIVATE_ROOM, PUBLIC_HALL }

fun main() {
    val saved = mapOf(
        "notes" to Place.PRIVATE_ROOM,
        "photo" to Place.PUBLIC_HALL
    )
    println(saved.filter { it.value == Place.PUBLIC_HALL }.keys)
}
```

It prints `[photo]`. The `filter` keeps the files in the hall, and `keys` gives their names.

## Predict the output

```challenge
type: predict
id: who-can-read-the-files
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  // A model in plain Kotlin, not real Android.
  enum class Place { PRIVATE_ROOM, PUBLIC_HALL }

  fun main() {
      val saved = mapOf(
          "notes" to Place.PRIVATE_ROOM,
          "photo" to Place.PUBLIC_HALL,
          "settings" to Place.PRIVATE_ROOM,
          "export" to Place.PUBLIC_HALL
      )
      println(saved.filter { it.value == Place.PUBLIC_HALL }.keys)
  }
options:
  - id: a
    text: "[notes, settings]"
    why: "You may have listed the private files. Those are in the locked room, so a snooper cannot open them."
  - id: b
    text: "[notes, photo, settings, export]"
    why: "You may have thought every saved file is open. The filter keeps only the files lying in the hall."
  - id: c
    text: "[photo, export]"
    correct: true
    why: "Only the hall is open to other apps. The photo and the export are in the hall, and the other two are in the locked room."
output: |
  [photo, export]
```

```challenge
type: predict
id: what-stays-after-uninstall
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  // A model in plain Kotlin, not real Android.
  enum class Place { PRIVATE_ROOM, PUBLIC_HALL }

  fun main() {
      val saved = mapOf("notes" to Place.PRIVATE_ROOM, "photo" to Place.PUBLIC_HALL)
      // The app is uninstalled: its private room is emptied.
      val left = saved.filter { it.value == Place.PUBLIC_HALL }
      println(left.keys)
  }
options:
  - id: a
    text: "[photo]"
    correct: true
    why: "The Android documentation says app-specific files are removed on uninstall, and files in shared storage stay. So only the photo is left, and anyone with access can still read it."
  - id: b
    text: "[notes, photo]"
    why: "You may have thought an uninstall keeps everything. The documentation says files in app-specific storage are removed."
  - id: c
    text: "[]"
    why: "You may have thought an uninstall wipes all the app's files. The documentation says files saved to shared storage remain on the device."
output: |
  [photo]
```

## Try it

1. **Write.** Type the first program from Predict the output into the editor.
2. **Run.** You should see one line, `[photo, export]`. Those are the files the snooper can pick up.
3. **Break.** Change `Place.PRIVATE_ROOM` to `Place.PUBLIC_HALL` on the `"notes"` line, and change nothing else. Run it again.
4. **Fix.** The output now starts with `notes`, because the notes moved into the hall. Change that line back and run it. The output is `[photo, export]` again.

## Fix the bug

```challenge
type: fix
id: fix-token-in-the-hall
generated: true
verified: "UNVERIFIED: not run, no shell; hand-traced"
code: |
  // A model in plain Kotlin, not real Android.
  enum class Place { PRIVATE_ROOM, PUBLIC_HALL }

  class Phone {
      private val saved = mutableMapOf<String, Place>()

      fun save(name: String, place: Place) {
          saved[name] = place
      }

      fun readableByOthers(): List<String> =
          saved.filter { it.value == Place.PUBLIC_HALL }.keys.toList()
  }

  // Saves the login token so the user stays signed in.
  // It goes to the public hall so it survives an uninstall.
  fun saveLoginToken(phone: Phone) {
      phone.save("login-token", Place.PUBLIC_HALL)
  }

  fun main() {
      val phone = Phone()
      phone.save("notes", Place.PRIVATE_ROOM)
      saveLoginToken(phone)
      println("Other apps can read: ${phone.readableByOthers()}")
  }
hints:
  - "Run it and read the last line. Which file name should not be in that list?"
  - "A token is a pass to the user's account. Which place is only for your own app?"
expected: |
  Other apps can read: []
fix: |
  // A model in plain Kotlin, not real Android.
  enum class Place { PRIVATE_ROOM, PUBLIC_HALL }

  class Phone {
      private val saved = mutableMapOf<String, Place>()

      fun save(name: String, place: Place) {
          saved[name] = place
      }

      fun readableByOthers(): List<String> =
          saved.filter { it.value == Place.PUBLIC_HALL }.keys.toList()
  }

  // Saves the login token so the user stays signed in.
  // It stays in the private room. The user signs in again after an uninstall.
  fun saveLoginToken(phone: Phone) {
      phone.save("login-token", Place.PRIVATE_ROOM)
  }

  fun main() {
      val phone = Phone()
      phone.save("notes", Place.PRIVATE_ROOM)
      saveLoginToken(phone)
      println("Other apps can read: ${phone.readableByOthers()}")
  }
explanation: "The helper looked tidy, but it left a pass to the account in the hall to survive an uninstall. A token belongs in the private room, and it is fine for it to be gone after an uninstall."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 4 has a short answer: if `saveLoginToken` runs twice, the map keeps one entry under the same name. For question 5, read the comment. "So it survives an uninstall" is the wrong reason to put a credential in a shared place.

## Tricks to remember

- Room is locked, hall is open, front desk keeps a photo.
- Private does not mean safe to store a password.
- Survives an uninstall means it is lying in the hall.
- Ask first whether the data needs to be saved at all.
- Task, on an emulator and with a fake value only: save a fake password in preferences, then use Android Studio's device file viewer to open the file. You will see the text as plain text. The viewer's name differs between Studio versions. Delete the project afterwards.

## Common mistakes

- **Thinking anything saved on the phone is private to my app.** The word "saved" makes it feel like one place. The Android documentation says external storage is globally readable and writable. Ask which place the file is in.
- **Thinking private means safe for a password.** A locked room feels like a safe. The documentation says not to store user IDs and passwords on the device, and to use a short-lived token. Keep the token private, and do not save the password.
- **Forgetting the front desk photo.** Backup runs by itself, so it is hard to notice. The Auto Backup documentation says shared preferences and internal files are included by default, and `allowBackup` defaults to `true`. Decide on purpose what the backup should hold.
- **Writing to shared storage so a file survives an uninstall.** It feels helpful for the user. The documentation says shared files remain after an uninstall, and anyone with access can still read them. A token is better off gone and issued again by the server.

## Where this shows up in Android

You generate a helper with an AI tool and it saves the login token into a file in a downloads folder, with the comment "so it survives uninstall". The code compiles, the app works, and the token is sitting in a shared place. That is the helper in the Fix the bug section.

A second symptom is a user who restores a phone from a backup and finds the app still holding an old token. My own reasoning: the backup copy brought it back. The Auto Backup documentation says that by default, shared preferences and internal files are in the backup. For credentials and authentication tokens it suggests Block Store instead of shared preferences or a file.

## Say it in an interview

**Where does an Android app store its data locally?**
The storage documentation describes app-specific storage in internal storage, reached with `getFilesDir()` or `getCacheDir()`, plus preferences for small key-value data and databases through the Room library. Shared storage is for files meant to be shared with other apps. I pick the place by asking who should be able to read the data.

**Can another app read my app's private files?**
By default, no. The documentation says files on internal storage are accessible only to your app, and each app has its own UID inside the sandbox. My own reasoning, general knowledge: that protection can be bypassed by someone with higher rights on the phone, such as on a rooted device.

**What are the risks of external storage?**
The documentation says files on external storage are globally readable and writable, and that the storage can be removed by the user and modified by any application. So store only non-sensitive information there, and validate any data you read from it as you would from an untrusted source.

**What goes wrong if you store a password or token in plain preferences?**
The documentation says not to store user IDs and passwords on the device, and to use a short-lived token. Preferences are private, but they are in the Auto Backup set by default, so the value can leave the phone. The trade-off is that a short-lived token means the user may sign in again sometimes.

## Recap

1. Files in your app's private area are closed to other apps, and files in shared places are open.
2. A password should not be saved on the device, and a token should be short-lived and private.
3. Auto Backup can copy private files off the phone, so decide what it holds.

## You can now

- Predict which of a list of saved files another app can read.
- Fix a helper that leaves a login token in a shared place.
- Explain, using the hotel, why private does not mean safe for a password.
