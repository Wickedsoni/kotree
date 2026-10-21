---
id: app-networking/requests-from-an-app/why-my-first-call-fails
title: "Why does my first network call fail before it even leaves the phone?"
status: shadow
difficulty: easy
order: 2
requires: [app-networking/requests-from-an-app/what-an-app-sends-and-gets]
summary: "A new app needs two things before a call can leave the phone: a permission line in the manifest and a secure address."
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- SOURCES: https://developer.android.com/develop/connectivity/network-ops/connecting (manifest must include INTERNET and ACCESS_NETWORK_STATE; both are normal permissions granted at install; network on the main thread throws NetworkOnMainThreadException) ; https://developer.android.com/privacy-and-security/security-config (cleartext enabled by default up to API 27, disabled by default from API 28; base-config default cleartextTrafficPermitted false for apps targeting 28 and higher; opting in for any domain "should be avoided whenever possible"). ⚠️ VERIFY: compile not run for the kotlin block (no shell). ⚠️ VERIFY: the claim that the call "never leaves the phone" for a blocked cleartext address or a missing permission is from the roadmap title; no official page uses those words. ⚠️ VERIFY: the exact exception thrown for a missing INTERNET permission and for blocked cleartext is not stated on the pages above, so the leaf names neither. -->

## The question

Your app asks a server for a list of books, and the same address opens fine in your laptop's browser. In the app, the call fails at once, and the server's log shows nothing arriving. Is the server broken? Or did the phone stop the request before it left?

## Mental model

Think of a stadium gate with two checks.

- The stadium is the internet, and your app is a visitor trying to go in.
- The first check is the ticket. The ticket is a line in the app's **manifest**, the file that tells the system what the app needs. No ticket line means no entry.
- The second check is the bag rule: sealed bags only. A sealed bag is a secure address. An open bag is a plain one.
- The guard at the gate follows the rule without asking who the visitor is.

A visitor with a ticket and a sealed bag walks in. Missing either one, the visitor is stopped at the gate, long before reaching a seat.

**Where the analogy stops:** A stadium has the same gate for everyone. On Android the sealed-bag rule changed over time. Older versions let open bags in, and newer ones do not unless the app asks for an exception. Also, a third thing can stop a first call, which is not a gate: asking from the one worker that draws the screen. The documentation says that throws a `NetworkOnMainThreadException`.

## Build the logic

### Notice

Two new apps ask for the same book list. App A has no ticket line in its manifest, and its address starts with `https`. App B has the ticket line, but its address starts with `http`, with no `s`. Both calls fail. In a laptop browser, both addresses open.

### Guess

Why do both apps fail while the browser works? Here are three ideas.

- A. The server is down, so nothing can answer.
- B. The phone checks two things before it lets a call go: may this app use the network, and is the address a secure one.
- C. The phone has no signal.

Idea A does not fit, because the browser works. Idea C does not fit, because the browser would fail too. Idea B explains both apps.

### Build

1. Before any call, ask for the ticket. Put one line in the manifest that says the app uses the internet.
2. Know that the ticket needs no popup. The user is not asked, and the system gives it when the app is installed.
3. Write the address with the sealed-bag form, so it starts with `https://`.
4. If you must reach one plain address, tell the phone about that one address only.
5. Never open the gate for every address to make an error disappear.

### Name it

A **permission** is a line in the manifest that tells the system your app needs a capability. For the internet the line is `<uses-permission android:name="android.permission.INTERNET" />`. The Android documentation says the manifest must include `INTERNET` and `ACCESS_NETWORK_STATE`, and that both are **normal permissions**, granted at install time with no request at runtime.

**HTTPS** is HTTP sent in an encrypted form (general knowledge). Traffic sent without encryption is **cleartext**. The security documentation says cleartext support is enabled by default up to Android 8.1 (API 27) and disabled by default starting with Android 9 (API 28). For apps that target API 28 and higher, the default setting is `cleartextTrafficPermitted="false"`.

An app can opt in to cleartext for one domain in a **network security configuration**, a small settings file. The documentation says opting in for any domain should be avoided whenever possible.

Here is the gate in plain Kotlin. It is a model, not real Android.

```kotlin
// A model in plain Kotlin, not real Android.
fun gate(hasTicket: Boolean, address: String): String {
    if (!hasTicket) return "Stopped: no internet permission"
    if (!address.startsWith("https://")) return "Stopped: plain http not allowed"
    return "Let through"
}

fun main() {
    println(gate(true, "https://example.com/books"))
    println(gate(false, "https://example.com/books"))
    println(gate(true, "http://example.com/books"))
}
```

This prints `Let through`, then `Stopped: no internet permission`, then `Stopped: plain http not allowed`. The guard checks the ticket first and the bag second.

## Predict the output

```challenge
type: predict
run: false
id: which-address-is-allowed
verified: not-run
code: |
  An app targets Android 9 (API 28) or higher.
  Its manifest has the internet permission line.
  It has no network security configuration.
  Which address can it call?
    1. http://api.example.com/books
    2. https://api.example.com/books
    3. http://api.example.com/books?page=1
options:
  - id: a
    text: "Address 1, because the page does not change"
    why: "You may have thought only the part after the address matters. The check is on the start of the address, http or https."
  - id: b
    text: "Address 2 only"
    correct: true
    why: "From API 28 the default turns cleartext off, so only the sealed-bag address passes the second check."
  - id: c
    text: "Addresses 1 and 3, because they are the same address"
    why: "You may have thought the extra words after the question mark make it secure. They do not. Both start with http, so both are open bags."
output: |
  Address 2 only
```

```challenge
type: predict
run: false
id: no-ticket-line
verified: not-run
code: |
  A manifest names one screen and has no permission lines.
  The app calls https://api.example.com/books.
  What happens at the gate?
options:
  - id: a
    text: "The call goes through, because the address is secure"
    why: "You may have thought the sealed bag is enough. The ticket is a separate check, and this visitor has none."
  - id: b
    text: "The phone asks the user to tap Allow, then the call goes through"
    why: "You may have pictured a popup. The internet permission is a normal permission, granted at install, so there is no popup to fix a missing line."
  - id: c
    text: "The call is stopped, and the fix is to add the permission line to the manifest"
    correct: true
    why: "The documentation says the manifest must include the permission. With no ticket line, the guard stops the visitor."
output: |
  The call is stopped, and the fix is to add the permission line to the manifest
```

```challenge
type: predict
run: false
id: trace-the-gate
verified: not-run
code: |
  fun gate(hasTicket: Boolean, address: String): String {
      if (!hasTicket) return "Stopped: no internet permission"
      if (!address.startsWith("https://")) return "Stopped: plain http not allowed"
      return "Let through"
  }

  fun main() {
      println(gate(false, "http://example.com"))
  }
options:
  - id: a
    text: "Stopped: plain http not allowed"
    why: "You may have checked the bag first. The guard checks the ticket first, and this visitor has none."
  - id: b
    text: "Stopped: no internet permission"
    correct: true
    why: "The ticket check comes first. This visitor fails it, so the guard never looks at the bag."
  - id: c
    text: "Let through"
    why: "You may have thought one good check is enough. Both checks must pass, and this visitor passes neither."
output: |
  Stopped: no internet permission
```

## Tricks to remember

- Two checks at the gate: a ticket, then a sealed bag.
- The ticket is one manifest line, and no popup comes with it.
- A browser accepts open bags. A new app does not.
- An error is a clue. Read which check stopped you before changing anything.

## Common mistakes

- **Thinking an address that works in the browser works in the app.** The browser and the app are two different visitors with different rules. Check the app's ticket and its bag.
- **Looking for a popup to allow the internet.** Other permissions ask the user, so it feels the same. The documentation says the internet permission is granted at install, so a missing manifest line is the only fix.
- **Opening the gate for every address to make the error disappear.** It works at once, so it feels like the fix. The documentation says to avoid opting in for any domain whenever possible. Open the gate for one address, or change the address to https.
- **Blaming the server.** The call fails at once, so the server feels guilty. If the server's log shows nothing arriving, look at the gate first.

## Where this shows up in Android

Your book list opens in a browser but shows an error the moment your app runs. The server is healthy. Either the manifest has no ticket line, or the address starts with `http` in an app that targets Android 9 or higher. A third possibility is a first call made from the worker that draws the screen. The documentation says Android throws an exception for network calls there.

My own reasoning: when a first call fails and the server log is empty, check these three in order: the permission line, the start of the address, and the worker the call runs on.

## Say it in an interview

**Your first network call fails in a new app. What do you check?**
First, the manifest must include the internet permission. Second, the address must be https, because the documentation says cleartext is off by default from Android 9. Third, the call must not run on the main thread, the one worker that draws the screen, because the documentation says Android refuses network calls there with an exception.

**Why does an `http` address fail on modern Android?**
The security documentation says cleartext support is disabled by default starting with Android 9 (API 28). An app that needs it must opt in. Protecting users from open traffic is the trade-off: the default is safer, and old servers that only speak http need a setting or an upgrade.

**Is the internet permission a runtime permission with a popup?**
No. The documentation says `INTERNET` and `ACCESS_NETWORK_STATE` are normal permissions, granted at install time and not requested at runtime. A missing manifest line cannot be fixed by tapping Allow.

**A developer allows cleartext for every domain to fix an error. What is the cost?**
The security documentation says that setting is insecure and should be avoided whenever possible. It lets every connection in the app go out without encryption. My own reasoning: allow one named domain if you must, or change the address to https.

## Recap

1. A call can be stopped before it leaves the phone by a missing manifest permission line.
2. A plain http address is blocked by default from Android 9, for apps that target it.
3. The internet permission is granted at install, so there is no popup to fix a missing line.

## You can now

- Predict which of three addresses an app allows by default on Android 9 or higher.
- Say what to add to the manifest when a call fails with no permission line.
- Trace a model of the gate and say which check stops each visitor first.
