---
id: app-networking/requests-from-an-app/what-an-app-sends-and-gets
title: "What does my app actually send to a server, and what comes back?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: An app sends a request with an address, a kind, a few notes and sometimes a body. The server answers with a status code and a body.
video: ""
publishedOn:
updatedOn:
sims: [request-parcel-pipe]
playground: true
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: shadow draft written 21 Oct 2026, author has not yet said he understood this topic. HTTP facts were checked on 21 Oct 2026 against https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Messages (request = method, target, headers, optional body; only PATCH, POST and PUT have a body; query string in key=value form; response = status code, headers, body; 201 Created) and https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/404 (404 = server cannot find the requested resource). These are HTTP facts, not Android documentation. Not checked: which request parts an Android HTTP client sets by default (the roadmap row asks for this; the text makes no claim about it). The sim id request-parcel-pipe has no spec in docs/05 yet (sim-engineer / physics-designer must create docs/sims/request-parcel-pipe.md). Code was compiled and run on Kotlin 2.4.20 only (not 2.4.21), so every challenge block says UNVERIFIED. Model code is plain Kotlin with a fake server, labelled "not real Android". -->
## The question

You open a book app and it shows your three saved books. You type "kotlin" into the search box and it shows results about Kotlin. Somewhere a computer far away answered both times.

What did your app actually send to make that happen? And what came back, so that the app knew it worked?

## Mental model

Think of posting a letter and getting a reply.

- The address on the envelope says where the letter goes.
- The kind of request is written on the front of the envelope, such as "I want to read something" or "I am sending something to keep".
- A note stuck to the outside gives extra facts about the letter, like "reply in plain text".
- The letter inside is the message itself. Some letters have nothing inside.
- The reply comes back as another letter, with a result code on it and a message inside.

You write the envelope, post it, and wait for the answer. You never visit the other house.

**Where the analogy stops:** A real letter takes days. A request takes a fraction of a second, and it can fail on the way. Also, the reply is a letter with its own outside notes, which this lesson names but does not open. And a letter that only asks to read something usually has nothing inside, which is normal.

## See it move

<!-- SIM: request-parcel-pipe -->

**Try this:**
1. Press "Send" with the kind set to "read" and the address set to the books list. Watch the parcel go to the server and a reply parcel come back.
2. Change the address to one that does not exist and send again. Look at the label on the reply.
3. Change the kind of request from "read" to "save" and send again. Look at what the reply says.

**What you should notice:** The reply parcel always carries a result label, and a wrong address or a different kind of request changes that label and the message.

## Build the logic

### Notice

The app wants to show the list of books. It also wants to search for the word "kotlin". Both times it must tell a server something.

For the list, the app says: "Please show me the books." For the search, the app says: "Please show me results for the word kotlin."

The server does not know the app. It has only what the app wrote on the envelope.

### Guess

What does the app have to put in the message so the server can answer?

- A. Only the address, and the server works out the rest.
- B. The address, what kind of request it is, and any extra word the server needs, such as "kotlin".
- C. Nothing, because the server knows what every app wants.

Idea A is a common guess. It misses that the same address can mean different things: reading a list is not the same as saving a new book. Idea C is how it can feel when an app works smoothly. Idea B is the answer.

### Build

1. Write the address: which thing on which server.
2. Write the kind of request: read something, or send something to keep.
3. If the server needs an extra word, such as the search word, attach it to the address.
4. Stick on the outside notes if you have facts about the request.
5. If you are sending something to keep, put it inside the envelope.
6. Post it. The reply comes back with a result code and a message inside.

### Name it

A message your app sends is a **request**, and what comes back is a **response**. These are facts about the web's standard way of talking, called HTTP, which the MDN documentation describes. A request has these parts.

| Letter | Part | Meaning |
|---|---|---|
| Address on the envelope | **Address** (the URL) | Which resource, and where. A search word can be added after a question mark, like `/search?q=kotlin` |
| Kind of request on the front | **Method** | Read something, such as `GET`, or send something, such as `POST` |
| Note on the outside | **Headers** | Extra facts about the request |
| Letter inside | **Body** | The data you send. Only `PATCH`, `POST` and `PUT` requests have one |

The response has a **status code**, a number that says whether the request worked, and a body with the answer or the reason it failed. Common codes are `200` for success, `201` for "created" and `404` for "not found". MDN says `404` means the server cannot find the requested resource. A response also has headers, which this lesson does not open.

Here is a fake server in plain Kotlin, not real Android. The real server is somewhere else. This one lives in your program so you can see the parts.

```kotlin
// A model in plain Kotlin, not real Android.
data class Response(val status: Int, val body: String)
fun fakeServer(method: String, address: String): Response = when {
    method == "GET" && address == "/books" -> Response(200, "3 books")
    method == "POST" && address == "/books" -> Response(201, "saved")
    method == "GET" && address.startsWith("/search?q=") -> Response(200, "results for " + address.substringAfter("q="))
    else -> Response(404, "nothing here")
}
fun main() {
    val reply = fakeServer("GET", "/books")
    println(reply.status)
    println(reply.body)
}
```

This prints `200`, then `3 books`. A **data class** is a small bundle of named boxes, here the status and the body. The `when` block checks the request from the top and gives the first reply that fits, and `else` is the reply for anything it does not know.

## Predict the output

```challenge
type: predict
id: address-that-does-not-exist
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  data class Response(val status: Int, val body: String)
  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") -> Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }
  fun main() {
      val reply = fakeServer("GET", "/shelf")
      println(reply.status)
      println(reply.body)
  }
options:
  - id: a
    text: "200, then 3 books"
    why: "You may have thought the server returns something for any address. This server knows only the addresses in its list."
  - id: b
    text: "404, then nothing here"
    correct: true
    why: "The address on the envelope is not one the server knows, so the reply carries the not-found code."
  - id: c
    text: "The program stops with an error"
    why: "You may have expected a crash. A missing address is a normal reply with a status code, not an error in your code."
output: |
  404
  nothing here
```

```challenge
type: predict
id: where-the-search-word-goes
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  data class Response(val status: Int, val body: String)
  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") -> Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }
  fun main() {
      val reply = fakeServer("GET", "/search?q=kotlin")
      println(reply.status)
      println(reply.body)
  }
options:
  - id: a
    text: "404, then nothing here"
    why: "You may have thought the question mark makes the address unknown. The server looks at the start of the address and reads the word after q=."
  - id: b
    text: "200, then 3 books"
    why: "You may have matched the books reply. The address starts with /search, so it takes the search branch."
  - id: c
    text: "200, then results for kotlin"
    correct: true
    why: "The search word travels on the address, after the question mark. The server reads it and puts it in the reply."
output: |
  200
  results for kotlin
```

## Try it

1. **Write.** Type the fake server from Name it into the editor, with `fakeServer("GET", "/books")` in `main`.
2. **Run.** You should see two lines: `200` and `3 books`.
3. **Break.** Change `"/books"` in the `main` line to `"/book"` and change nothing else. Run it again.
4. **Fix.** You now see `404` and `nothing here`, because one missing letter makes an address the server does not know. Put the `s` back and run it. The reply is `200` and `3 books` again.

## Fix the bug

```challenge
type: fix
id: fix-wrong-kind-of-request
verified: "UNVERIFIED: ran on Kotlin 2.4.20, not 2.4.21"
code: |
  // A model in plain Kotlin, not real Android.
  data class Response(val status: Int, val body: String)

  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") -> Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }

  fun main() {
      // The screen only wants to show the saved books.
      val reply = fakeServer("POST", "/books")
      println(reply.status)
      println(reply.body)
  }
hints:
  - "Look at the first word passed to fakeServer in main. What does the screen want to do with the books?"
  - "Reading a list uses GET. POST is for sending something to keep."
expected: |
  200
  3 books
fix: |
  // A model in plain Kotlin, not real Android.
  data class Response(val status: Int, val body: String)

  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") -> Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }

  fun main() {
      val reply = fakeServer("GET", "/books")
      println(reply.status)
      println(reply.body)
  }
explanation: "The address was right, but the kind of request was wrong. POST means send something to keep, so the server saved instead of showing the list."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

For this code, question 3 has a short answer: this code has no null values in play. Question 4 is worth a thought. If the screen runs this on every redraw, a POST would save a book again each time.

## Tricks to remember

- Four on the way out: address, kind, notes, letter inside.
- Two to read first on the way back: result code, message inside.
- A search word rides on the address, after the question mark.
- A 404 is a normal reply, not a crash.

## Common mistakes

- **Thinking the app sends only the address, and the server works out the rest.** An address feels like the whole question. The same address can mean "show me" or "save this", so the kind of request matters.
- **Putting data inside a read request.** Putting everything in the body feels tidy. MDN says only `PATCH`, `POST` and `PUT` requests have a body. A read request carries its extras on the address.
- **Treating a status code as a crash.** A number other than 200 feels like failure. It is the server's normal answer, and your code can read it and decide what to do.
- **Ignoring the status code and reading the body.** The body looks like the real answer. On a 404 the body is the reason, not the data you asked for.

## Where this shows up in Android

A book screen shows an empty list, or says "not found". Nothing crashed. The app sent a request, and the reply carried a code such as 404 because one letter in the address was wrong, like `/book` in Try it.

Whatever tool an Android app uses to talk to a server, the message it sends and the reply it gets have the parts in this lesson. When a screen shows the wrong thing, ask four questions in order. Was the address right? Was it the right kind of request? Did it need something inside or a note outside? What status code came back?

## Recap

1. A request has an address, a kind, headers and sometimes a body.
2. A response comes back with a status code and a body.
3. A search word travels on the address, after the question mark.

## You can now

- Name the four parts of a request and the two parts of a response you read first.
- Predict the status code and message the fake server returns for an address it does not know.
- Fix a request that uses the wrong kind, so the reply shows the saved books.
