---
id: app-networking/requests-from-an-app/what-an-app-sends-and-gets
title: "What does my app actually send to a server, and what comes back?"
status: shadow
difficulty: easy
order: 1
requires: []
summary: A request has an address, a kind, notes and sometimes a body. The reply carries a status code and a body.
video: ""
publishedOn:
updatedOn:
sims: [request-parcel-pipe]
playground: true
fruit: false
upnext: false
---

## The question

You open a book app and it shows your three saved books. You type "kotlin" into the search box and it shows results about Kotlin. A computer far away answered both times. What did your app send to make that happen? And what came back, so that the app knew it worked?

## Mental model

Think of posting a letter and getting a reply.

- The address on the envelope says where the letter goes.
- The kind of request is written on the front of the envelope, such as "I want to read something" or "I am sending something to keep".
- A note stuck to the outside gives extra facts about the letter, like "reply in plain text".
- The letter inside is the message itself. Some letters have nothing inside.
- Extra words can be written at the end of the address, after a question mark, like "room=5".
- The reply is another letter, with a result code on the outside and a message inside.

You write the envelope, post it and wait. You never visit the other house.

**Where the analogy stops:** A real letter takes days. A request takes a fraction of a second, and it can fail on the way. The reply has its own outside notes too, which this page names but does not open. And a letter that only asks to read something usually has nothing inside, which is normal.

## See it move

<!-- SIM: request-parcel-pipe -->

**Try this:**
1. Press "Send" with the kind set to "read" and the address set to the books list. Watch the parcel go to the server and a reply parcel come back.
2. Change the address to one that does not exist and send again. Look at the label on the reply.
3. Change the kind from "read" to "save" and send again. Look at what the reply says.

**What you should notice:** The reply parcel always carries a result label, and a wrong address or a different kind of request changes that label and the message.

## Build the logic

### Notice

The app wants to show the list of books. It also wants to search for the word "kotlin". Both times it must tell the computer far away something. For the list, the app says: "Please show me the books." For the search, it says: "Please show me results for the word kotlin." That computer does not know the app. It has only what the app wrote on the envelope.

### Guess

What does the app put in the message so the other computer can answer?

- A. Only the address, and the other side works out the rest.
- B. Nothing, because the other side knows what every app wants.
- C. The address, what kind of request it is, and any extra word it needs, such as "kotlin".

Idea A is a common guess. It misses that one address can mean different things: reading a list is not the same as saving a new book. Idea B is how it can feel when an app works smoothly. Idea C is the answer.

### Build

1. Write the address: which thing, and where.
2. Write the kind of request: read something, or send something to keep.
3. If an extra word is needed, such as the search word, write it at the end of the address.
4. Stick on the outside notes, if you have facts about the request.
5. If you are sending something to keep, put it inside the envelope.
6. Post it. The reply comes back with a result code and a message inside.

### Name it

The computer that answers is the **server**. Apps and servers usually talk in **HTTP**, the web's standard way of sending messages (general knowledge). A message your app sends is a **request**, and what comes back is a **response**. A request has these parts (general knowledge).

| Letter | Part | Meaning |
|---|---|---|
| Address on the envelope | **URL** | Which thing, and where. A search word can follow a question mark, as in `/search?q=kotlin` |
| Kind on the front | **Method** | `GET` reads something. `POST` sends something to keep |
| Note on the outside | **Headers** | Extra facts about the request |
| Letter inside | **Body** | The data you send. A read request such as `GET` usually has none |

The response has a **status code**, a number that says whether the request worked, and a body with the answer or the reason it failed. Common codes are `200` for success, `201` for "created" and `404` for "not found" (general knowledge). A response has headers too.

Here is a fake server in plain Kotlin, not real Android. The real server is somewhere else. This one lives in your program so you can see the parts.

```kotlin
// A model in plain Kotlin, not real Android.
data class Response(val status: Int, val body: String)

fun fakeServer(method: String, address: String): Response = when {
    method == "GET" && address == "/books" -> Response(200, "3 books")
    method == "POST" && address == "/books" -> Response(201, "saved")
    method == "GET" && address.startsWith("/search?q=") ->
        Response(200, "results for " + address.substringAfter("q="))
    else -> Response(404, "nothing here")
}

fun main() {
    val reply = fakeServer("GET", "/books")
    println(reply.status)
    println(reply.body)
}
```

This prints `200`, then `3 books`. A **data class** is a small bundle of named boxes, here the status and the body. The `when` block checks the request from the top and gives the first reply that fits. The `else` line is the reply for anything it does not know.

## Predict the output

```challenge
type: predict
id: address-that-does-not-exist
verified: kotlin-2.4.21
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
    text: "404, then nothing here"
    correct: true
    why: "The address on the envelope is not one the server knows, so the reply carries the not-found code."
  - id: b
    text: "200, then 3 books"
    why: "You may have thought the server answers any address. This one knows only the addresses in its list."
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
verified: kotlin-2.4.21
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
    why: "You may have thought the question mark makes the address unknown. The server checks the start of the address and reads the word after q=."
  - id: b
    text: "200, then 3 books"
    why: "You may have matched the books reply. This address starts with /search, so it takes the search line."
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
3. **Break.** Change `"GET"` in the `main` line to `"POST"` and change nothing else. Run it again.
4. **Fix.** You now see `201` and `saved`. The address was right, but the kind of request told the server to save. Put `"GET"` back and run it. The reply is `200` and `3 books` again.

## Fix the bug

```challenge
type: fix
id: fix-search-word-in-address
verified: kotlin-2.4.21
code: |
  // A model in plain Kotlin, not real Android.
  data class Response(val status: Int, val body: String)

  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") ->
          Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }

  fun search(word: String): Response {
      val address = "/search/" + word
      return fakeServer("GET", address)
  }

  fun main() {
      val reply = search("kotlin")
      println(reply.status)
      println(reply.body)
  }
hints:
  - "Read the line that builds address in search. Compare it with the search address in Predict the output."
  - "The search word must follow a question mark and q=, not a second slash."
expected: |
  200
  results for kotlin
fix: |
  // A model in plain Kotlin, not real Android.
  data class Response(val status: Int, val body: String)

  fun fakeServer(method: String, address: String): Response = when {
      method == "GET" && address == "/books" -> Response(200, "3 books")
      method == "POST" && address == "/books" -> Response(201, "saved")
      method == "GET" && address.startsWith("/search?q=") ->
          Response(200, "results for " + address.substringAfter("q="))
      else -> Response(404, "nothing here")
  }

  fun search(word: String): Response {
      val address = "/search?q=" + word
      return fakeServer("GET", address)
  }

  fun main() {
      val reply = search("kotlin")
      println(reply.status)
      println(reply.body)
  }
explanation: "The kind of request was right, but the search word was written into the address in the wrong place, so the server did not know that address and answered 404."
```

**Read it like a reviewer.** Answer in this order:
1. What are the types?
2. What runs, and in what order?
3. What can be missing (null)?
4. What can run twice?
5. Would you accept this in review? Why or why not?

This code has no null values in play. Question 4 is worth a thought: if a screen calls `search` on every redraw, the app sends the same request again each time.

## Tricks to remember

- Four on the way out: address, kind, notes, letter inside.
- Two to read first on the way back: result code, message inside.
- A search word rides on the address, after the question mark.
- A 404 is a normal reply, not a crash.

## Common mistakes

- **Thinking the app sends only the address, and the server works out the rest.** An address feels like the whole question. The same address can mean "show me" or "save this", so the kind of request matters.
- **Treating a status code other than 200 as a crash.** A different number feels like failure. It is the server's normal answer, and your code can read it and decide what to do.
- **Reading the body without looking at the status code.** The body looks like the real answer. On a 404 the body is the reason, not the data you asked for.

## Where this shows up in Android

A book screen shows an empty list, or says "not found". Nothing crashed. The app sent a request, and the reply carried a code such as 404 because the address was wrong, like `/search/kotlin` in the Fix the bug task.

The Android documentation says most network-connected apps use HTTP to send and receive data. Whatever tool an app uses, the message it sends and the reply it gets have the parts in this lesson. My own reasoning: when a screen shows the wrong thing, ask four questions in order. Was the address right? Was it the right kind of request? Did it need something inside or a note outside? What status code came back?

## Say it in an interview

**When your app calls an API, what is in the request and what is in the response?**
A request has a URL, a method, headers and sometimes a body. A response has a status code, headers and a body (general knowledge). The status code tells the app whether the call worked before it reads the body.

**Where would you put a search word, a login token and a new record you want to save?**
The search word goes on the URL after a question mark. The new record goes in the body of a `POST`. A token is a fact about who is asking, so it goes in a header (all three placements are general knowledge). My own reasoning: URLs are often written to server logs (general knowledge), which is one reason to keep secrets out of them.

**Does a 404 mean the app crashed?**
No. The server answered normally and said it cannot find what was asked for (general knowledge). The app should read the status code and decide what to show. A crash would be an error in the app's own code.

**Why can the same address do two different things?**
The method changes the meaning. `GET /books` asks to read the list, and `POST /books` sends a new book to keep (general knowledge). If a screen sends the wrong method, the address can be right and the result still wrong.

## Recap

1. A request has an address, a kind, headers and sometimes a body.
2. A response comes back with a status code and a body.
3. A search word travels on the address, after the question mark.

## You can now

- Predict the status code and message a fake server returns for an address it does not know.
- Predict which reply a changed request kind gets in the Try it loop.
- Fix a request whose search word is written in the wrong place, so the reply shows the results.
