---
id: app-networking/http-client/first-get-call
title: "How do I fetch data from an address with a few lines of Kotlin?"
status: shadow
difficulty: medium
order: 3
requires: [app-networking/requests-from-an-app/why-my-first-call-fails]
summary: "Fetching data takes three pieces: an interface that lists the calls, a client that sends them, and the call itself."
video: ""
publishedOn:
updatedOn:
sims: [waiter-and-kitchen]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (read 21 Oct 2026):
- https://developer.android.com/develop/connectivity/network-ops/connecting (Retrofit is a type-safe HTTP client built on OkHttp that creates a client interface declaratively; UserService sample with @GET and suspend fun; no network work on the main thread, NetworkOnMainThreadException)
- https://lysine.dev/retrofit/ (Retrofit.Builder().baseUrl(...).build() and retrofit.create(...); @GET on an interface method)
- https://raw.githubusercontent.com/square/retrofit/trunk/CHANGELOG.md (2.6.0, 2019-06-05: "Support suspend modifier on functions for Kotlin")
- https://kotlinlang.org/docs/coroutines-basics.html (a suspending function can pause and resume later; a suspended coroutine does not block its thread; a suspending function is called from another suspending function)
- https://javadoc.io/static/com.squareup.okhttp3/okhttp/3.9.1/okhttp3/OkHttpClient.html (create one OkHttpClient and reuse it; each client has its own connection pool and thread pools)
⚠️ VERIFY: the OkHttp sentence is read from the 3.9.1 Javadoc via a search result. Re-read the current OkHttp page before go-live.
⚠️ VERIFY: "building the client sends nothing, only the call line does" and "treat the Retrofit object as the one shared waiter" are my own reasoning. Check both against the current Retrofit documentation before go-live.
⚠️ VERIFY: the roadmap asks about the base-address trailing slash rule. The sources read here do not state it, so this leaf makes no claim about it.
⚠️ VERIFY: compile not run for the plain-Kotlin model. The Retrofit blocks are marked "does not compile" because they need the library.
-->

## The question

Your weather app needs today's forecast, and you know the address where it lives. How many lines of Kotlin does it take to ask for it? And when you ask, why does the answer not appear the moment you ask?

## Mental model

Think of ordering through a waiter.

- You sit at your table. The table is your screen.
- The order card lists what you can ask for and where to ask. It does not send anything by itself.
- The waiter is hired once and knows the way to the kitchen. The waiter carries your card there.
- The kitchen is the server that holds the forecast.
- The plate is the answer that comes back.
- Handing the card to the waiter is the moment you really ask.

The waiter walks, and you do not stand at the kitchen door. While you wait, the other guests at your table still get served.

**Where the analogy stops:** A waiter walks a few steps. A phone sends data over a network that can be slow or can fail, so the wait can be long. Also, the kitchen can answer "no" instead of sending a plate, for example when the address does not exist. A real diner can stand at the kitchen door, but on a phone the screen must never do that.

## See it move

```sim
id: waiter-and-kitchen
template: flow
title: An order card, a waiter and a kitchen
intro: "You sit at your table with an order card for today's weather. The waiter stands beside you. The kitchen is across the street."
stations:
  - { id: you, label: "Your table (the screen)" }
  - { id: waiter, label: "The waiter (the client)" }
  - { id: kitchen, label: "The kitchen (the server)" }
items:
  - { id: card, label: "Order card: today's weather", at: you }
vars: { door: "off", pictures: 0 }
show:
  - { var: pictures, label: "Screen pictures drawn while waiting" }
toggles:
  - { id: door, label: "Stand at the kitchen door while waiting", var: door }
actions:
  - id: order
    label: "Hand the card to the waiter"
    cases:
      - when: { at: { card: you } }
        do:
          - move: { card: waiter }
          - light: [ waiter ]
          - say: "You hand the order card to the waiter. Up to now nothing was sent. The card only said what you want."
    refuse: "The card is already with the waiter."
  - id: walk
    label: "The waiter walks to the kitchen"
    cases:
      - when: { at: { card: waiter }, var: { door: "on" } }
        do:
          - move: { card: kitchen }
          - set: { pictures: 0 }
          - mark: { card: bad }
          - light: [ kitchen ]
          - say: "You follow the waiter and stand at the kitchen door. While you stand there, nobody at your table is served and the screen draws no new pictures."
      - when: { at: { card: waiter } }
        do:
          - move: { card: kitchen }
          - set: { pictures: 3 }
          - light: [ kitchen ]
          - say: "The waiter walks to the kitchen with the card. You stay at your table, so the screen keeps drawing. In this toy it draws 3 pictures while you wait."
    refuse: "Hand the card to the waiter first."
  - id: serve
    label: "The kitchen sends the plate back"
    cases:
      - when: { at: { card: kitchen } }
        do:
          - remove: card
          - spawn: { id: plate, label: "Plate: the answer", at: you }
          - mark: { plate: good }
          - light: [ you ]
          - say: "The kitchen cooks the order and the waiter brings the plate to your table. The answer arrives after the waiting, not at the moment you ordered."
    refuse: "The card has not reached the kitchen yet."
script: [ order, walk, serve, "door=on" ]
try:
  - text: "Press \"Hand the card to the waiter\", then \"The waiter walks to the kitchen\". Read the pictures counter."
    done: [ order, walk ]
  - text: "Press \"The kitchen sends the plate back\". The plate arrives after the wait."
    done: [ serve ]
  - text: "(Optional) Switch on \"Stand at the kitchen door while waiting\". The steps replay and the pictures counter drops to 0."
    done: [ "door=on" ]
    optional: true
```

**What you should notice:** While the waiter walks, the screen keeps drawing, and the plate arrives only after the wait. If you stand at the kitchen door, the drawing stops.

## Build the logic

### Notice

Your table wants today's weather from the kitchen across the street. The kitchen needs a moment to prepare it. On a weak signal the moment can be long. In the meantime the guests at your table still want to be served.

### Guess

What should your table do while the order is on its way? Here are three ideas.

- A. Stand at the kitchen door until the plate is ready.
- B. Write the order, give it to the waiter, carry on serving, and take the plate when it arrives.
- C. Skip the kitchen and cook the forecast at the table.

Idea A freezes your table for the whole wait. Idea C cannot work, because the forecast data lives in the kitchen. Idea B is the pattern.

### Build

1. Write down what you can order. Each entry on the card has a kind of request and the rest of the address.
2. Hire one waiter. Tell the waiter the first part of the address that every order starts with.
3. Give the waiter the card, so the waiter can turn "today's weather" into a real walk.
4. Hand over the card when you want the answer. This is the only step that sends anything.
5. The waiter walks. Your table carries on, and the plate comes back later.
6. The function that hands over the card is marked as one that can wait. Kotlin lets it pause without freezing the table.

### Name it

A **client** is the object that sends requests for you, like the waiter. **Retrofit** is a library for this. The Android documentation describes it as a type-safe HTTP client for the JVM, built on top of OkHttp, which lets you create a client interface declaratively. An **interface** is a list of functions that says what can be done and not how. Here it is the order card. **`@GET`** marks one entry as a request of the GET kind, which asks for data without changing anything. A **suspend function** is a function that can pause and resume later. The Kotlin documentation says a suspended coroutine does not block its thread, so the thread stays free to do other work. A **coroutine** is a piece of work that Kotlin runs and that is allowed to pause. You call a suspend function from another suspend function or from inside a coroutine, for example one started with `launch`.

The three pieces are these.

| Waiter | Piece | In the code |
|---|---|---|
| The order card | An interface | `interface WeatherApi { ... }` |
| Hiring the waiter | A client | `Retrofit.Builder().baseUrl(...).build()` and `create(...)` |
| Handing over the card | The call | `weatherApi.today()` |

The Retrofit changelog says Retrofit 2.6.0 added support for the `suspend` modifier on functions. The Android documentation shows the same shape with a `UserService` interface. Here it is for the weather app. It needs the Retrofit library, so it cannot run on this page.

```kotlin
// does not compile
// Needs the Retrofit library, so it cannot run on this page.
interface WeatherApi {
    @GET("weather/today")
    suspend fun today(): Forecast
}

val weatherApi: WeatherApi = Retrofit.Builder()
    .baseUrl("https://example.com/api/")
    .build()
    .create(WeatherApi::class.java)

suspend fun show() {
    val forecast = weatherApi.today()
}
```

To get a `Forecast` object and not raw text, a converter must also be added to the builder (general knowledge). This page leaves it out. Building `weatherApi` sends nothing. Only the line `weatherApi.today()` does. That is my own reasoning from how the interface and builder work, and the pages I read do not state it in those words.

The Android documentation says Retrofit is built on top of OkHttp. The OkHttp advice is about its own client, and I treat the Retrofit object as the one waiter in the same way. That step is my own reasoning.

Here is the same shape in plain Kotlin. It is a model, not real Retrofit. It has no waiting, because plain code cannot pause. It shows the three pieces and the order of the lines.

```kotlin
// A model in plain Kotlin, not real Retrofit. It has no waiting.
interface WeatherApi {
    fun today(): String
}

class FakeClient {
    fun create(): WeatherApi = object : WeatherApi {
        override fun today(): String {
            println("Waiter: walking to the kitchen")
            return "Sunny"
        }
    }
}

fun main() {
    val api = FakeClient().create()
    println("Table: card handed over")
    val plate = api.today()
    println("Table: got " + plate)
}
```

This prints `Table: card handed over`, then `Waiter: walking to the kitchen`, then `Table: got Sunny`. The waiter walks only when the card is handed over.

## Predict the output

```challenge
type: predict
run: false
id: which-line-asks-the-server
verified: not-run
code: |
  Read this code. The line numbers are not part of it.
   1  interface WeatherApi {
   2      @GET("weather/today")
   3      suspend fun today(): Forecast
   4  }
   5
   6  val weatherApi = Retrofit.Builder().baseUrl("https://example.com/api/").build().create(WeatherApi::class.java)
   7
   8  suspend fun show() {
   9      val forecast = weatherApi.today()
  10  }
  Which line hands the order card to the waiter, so that the app really asks the server?
options:
  - id: a
    text: "Line 3"
    why: "You may have thought the line that names the call sends it. Line 3 is an entry on the order card. It describes the order and sends nothing."
  - id: b
    text: "Line 6"
    why: "You may have thought building the client starts the work. Line 6 hires the waiter and teaches the waiter the first part of the address. Nobody has walked anywhere yet."
  - id: c
    text: "Line 9"
    correct: true
    why: "Handing over the card is the moment you ask. Line 9 calls the entry on the card, so the waiter walks to the kitchen."
output: |
  Line 9
```

```challenge
type: predict
run: false
id: how-many-waiters
verified: not-run
code: |
  An app asks for the weather five times in one session.
  The OkHttp documentation says to create one OkHttpClient and reuse it.
  How many waiters (clients) should the app hire?
options:
  - id: a
    text: "Five, one for each call"
    why: "You may have matched one waiter to one order. The OkHttp documentation says each client holds its own pools, so five clients leave five sets of idle pools behind."
  - id: b
    text: "One, hired once and used for all five calls"
    correct: true
    why: "One waiter can carry any number of cards. The documentation says reusing one client shares its connections and threads."
  - id: c
    text: "None, because the interface sends the calls by itself"
    why: "You may have thought the order card sends the order. A card only describes it. A client has to carry it."
output: |
  One, hired once and used for all five calls
```

## Tricks to remember

- Card, waiter, hand-over: interface, client, call.
- Nothing is sent until you hand over the card.
- Hire one waiter and reuse the same one.
- The call can wait, so it is marked `suspend`.

## Common mistakes

- **Thinking a network call returns at once.** Most functions you have used finish in a blink, so a call feels the same. A network call has to cross the network and wait for the kitchen. That is why the function can pause.
- **Thinking that writing the interface sends the request.** The interface looks like code that does something. It is only the order card. The request leaves when you call one of its functions.
- **Building a new client for every call.** It feels tidy to hire a waiter for each order. The OkHttp documentation says each client holds its own connection pool and thread pools, and building one for each request wastes resources on idle pools.

## Where this shows up in Android

You tap Refresh and the app crashes. The Android documentation says that if you try a network operation on the main thread, a `NetworkOnMainThreadException` is thrown. That is the table standing at the kitchen door. A call marked `suspend` lets the table carry on.

Another symptom: the data screen stays empty and no error shows. My own reasoning: the call may still be on its way, or the code may never have handed over the card. Look at the line that calls the function.

## Say it in an interview

**How do you define a GET endpoint with Retrofit in Kotlin?**
You write an interface with one function for each endpoint. You mark it with `@GET` and the path, and you add `suspend` to the function. The Android documentation shows this shape with a `UserService` interface. Retrofit has supported `suspend` functions since version 2.6.0, according to its changelog.

**Why share one client across the app?**
The OkHttp documentation says to create one `OkHttpClient` and reuse it. Each client has its own connection pool and thread pools, so reuse saves connections and memory. The trade-off is that one shared client carries one set of settings. My own reasoning: if two parts of an app need different timeouts, you build a second client on purpose and still share each one.

**How does a suspend call avoid freezing the thread that calls it?**
The Kotlin documentation says a coroutine can suspend instead of blocking a thread, and the thread stays free for other work. The call pauses at that line and carries on when the answer arrives. I do not know how Retrofit does this inside, so I would say that if asked.

**What goes wrong if you build a new client on every call?**
The OkHttp documentation says that creating a client for each request wastes resources on idle pools. My own reasoning: you also lose the chance to reuse an open connection, so calls can be slower.

## Recap

1. Fetching data takes an interface, a client and a call.
2. Only the call sends the request, and it waits for the answer without freezing the screen.
3. One client is built once and reused for every call.

## You can now

- Name the line that actually sends a request in a short Retrofit listing.
- Explain, using the waiter, why building a client sends nothing.
- Predict how many clients an app should build for five calls.
