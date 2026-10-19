```yaml
branch: app-networking
bough: architecture-kmp
stems:
  - id: requests-from-an-app
    title: "Requests from an App"
    why: "Before any library helps, a fresher must see what an app sends and gets, and why the first call often fails on a phone."
    leaves:
      - id: what-an-app-sends-and-gets
        title: "What does my app actually send to a server, and what comes back?"
        difficulty: easy
        requires: []
        fruit: false
      - id: why-my-first-call-fails
        title: "Why does my first network call fail before it even leaves the phone?"
        difficulty: easy
        requires: [app-networking/requests-from-an-app/what-an-app-sends-and-gets]
        fruit: false
  - id: http-client
    title: "The HTTP Client"
    why: "Apps do not open connections by hand; they describe calls to a client library, and a fresher must read those descriptions."
    leaves:
      - id: first-get-call
        title: "How do I fetch data from an address with a few lines of Kotlin?"
        difficulty: medium
        requires: [app-networking/requests-from-an-app/why-my-first-call-fails]
        fruit: false
      - id: building-requests
        title: "How do I send a search word, an id in the address, or a form to the server?"
        difficulty: easy
        requires: [app-networking/http-client/first-get-call]
        fruit: false
      - id: interceptors-and-logging
        title: "How can I add a header or a log line to every call?"
        difficulty: easy
        requires: [app-networking/http-client/first-get-call]
        fruit: false
  - id: json
    title: "JSON"
    why: "Servers send text, and every screen needs that text turned into safe Kotlin objects, even when the server changes."
    leaves:
      - id: json-to-kotlin-objects
        title: "How does text from the server become a Kotlin object?"
        difficulty: easy
        requires: [app-networking/http-client/first-get-call]
        fruit: false
      - id: when-json-does-not-match
        title: "What happens when the server adds, drops or renames a field?"
        difficulty: medium
        requires: [app-networking/json/json-to-kotlin-objects]
        fruit: false
  - id: errors-and-retries
    title: "Errors and Retries"
    why: "Phones lose signal all the time, so every call needs a plan for failing, waiting and trying again without hurting the user or the server."
    leaves:
      - id: kinds-of-failure
        title: "How do I tell the different ways a network call can fail?"
        difficulty: medium
        requires: [app-networking/http-client/first-get-call, app-networking/json/when-json-does-not-match]
        fruit: false
      - id: timeouts
        title: "How long will my call wait before it gives up?"
        difficulty: medium
        requires: [app-networking/errors-and-retries/kinds-of-failure]
        fruit: false
      - id: cancelling-a-call
        title: "Does my call stop when the user leaves the screen?"
        difficulty: hard
        requires: [app-networking/errors-and-retries/timeouts]
        fruit: false
      - id: retry-with-backoff
        title: "How do I retry a failed call without making things worse?"
        difficulty: medium
        requires: [app-networking/errors-and-retries/kinds-of-failure, app-networking/http-client/building-requests]
        fruit: false
  - id: auth-tokens
    title: "Auth Tokens"
    why: "Most useful APIs know who is asking, and a fresher must handle the token without leaking it or locking the user out."
    leaves:
      - id: how-apps-prove-who-they-are
        title: "What is a token, and why does the app send it instead of the password?"
        difficulty: medium
        requires: [app-networking/http-client/interceptors-and-logging]
        fruit: false
      - id: refreshing-an-expired-token
        title: "What should happen when the server says my token has expired?"
        difficulty: hard
        requires: [app-networking/auth-tokens/how-apps-prove-who-they-are, app-networking/errors-and-retries/kinds-of-failure]
        fruit: false
  - id: caching
    title: "Caching"
    why: "Asking again for something the app already has wastes battery and data, but reusing a stale answer shows wrong information."
    leaves:
      - id: reusing-saved-answers
        title: "When can the app reuse a saved answer instead of asking the server again?"
        difficulty: medium
        requires: [app-networking/http-client/building-requests]
        fruit: false
  - id: images
    title: "Images"
    why: "Photos are the biggest downloads in most apps, and loading them badly makes lists jump, stutter and run out of memory."
    leaves:
      - id: images-from-urls
        title: "How does a picture from the internet reach the screen without freezing the list?"
        difficulty: medium
        requires: [app-networking/http-client/first-get-call]
        fruit: false
  - id: real-time
    title: "Real-Time Updates"
    why: "Chat, scores and delivery tracking cannot wait for the user to pull to refresh, so the server must be able to speak first."
    leaves:
      - id: live-updates-without-asking
        title: "How can the server send my app news without my app asking every few seconds?"
        difficulty: medium
        requires: [app-networking/errors-and-retries/retry-with-backoff]
        fruit: false
  - id: sync
    title: "Sync"
    why: "Real apps are used offline, so changes made on the phone and on the server must meet again without losing anyone's work."
    leaves:
      - id: keeping-phone-and-server-in-step
        title: "What happens when the user edits offline and the server changed the same item?"
        difficulty: hard
        requires: [app-networking/errors-and-retries/retry-with-backoff, app-networking/caching/reusing-saved-answers]
        fruit: true
```

## Branch prerequisites

All leaves are `shadow` on 18 Oct 2026. Nothing is published. Every stem is phase 4, so every `Assumes` target must sit in phase 4 or lower, and in phase 4 only in a branch that comes earlier in the `docs/08` §10.1 tie-break order (computer networking, android-core, concurrency, architecture and data). Anything later (android-core phase 5 stems, ui phase 5 stems, security, testing, tools) is `Helps to know` only.

All targets below are full leaf IDs, checked against the existing roadmaps on 18 Oct 2026.

- Assumes Foundation: `foundation/programming-thinking/function-inputs-and-outputs` (phase 1) — every call description is a function with inputs and a result.
- Assumes Foundation: `foundation/debugging-mindset/reading-error-messages` (phase 1) — failures are read from exception messages and status codes.
- Assumes Kotlin: `kotlin/lambdas/lambda-syntax` (phase 2) — client setup blocks and interceptors are lambdas.
- Assumes Kotlin: `kotlin/null-safety/nullable-types` (phase 2) — missing JSON fields become nullable properties.
- Assumes Kotlin: `kotlin/special-classes/data-classes` (phase 3) — response models are data classes.
- Assumes Kotlin: `kotlin/special-classes/sealed-classes` (phase 3) — success, failure and loading are modelled as a sealed type.
- Assumes Kotlin: `kotlin/errors-and-results/try-catch-finally` (phase 3) — used by kinds-of-failure and retry-with-backoff.
- Assumes Computer networking: `networking/http/request-and-response`, `networking/http/http-methods`, `networking/http/status-codes` and `networking/http/headers-and-bodies` (phase 4) — methods, headers, status codes and bodies are used, not re-taught.
- Assumes Computer networking: `networking/tls/why-https` (phase 4) — HTTPS is assumed; the lesson recaps one paragraph on why plain HTTP is blocked.
- Assumes Computer networking: `networking/websockets/websockets-and-live-updates` (phase 4) — used by live-updates-without-asking.
- Assumes Android core and lifecycles: `android-core/lifecycle/lifecycle-callbacks-order` (phase 3) — calls started from a screen must stop with it.
- Assumes Android core and lifecycles: `android-core/process-death/what-is-process-death` (phase 3) — used by keeping-phone-and-server-in-step; unsent edits must survive being killed.
- Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen` (phase 4) — a call must never block the main thread.
- Assumes Concurrency: `concurrency/coroutine-basics/launch-a-coroutine`, `concurrency/suspend/what-suspend-means`, `concurrency/dispatchers/main-io-and-default` and `concurrency/scopes/what-is-a-coroutine-scope` (phase 4) — every client call in this branch is a suspending call.
- Assumes Concurrency: `concurrency/cancellation-and-errors/cancel-is-cooperative` (phase 4) — used by cancelling-a-call.
- Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow` (phase 4) — used by live-updates-without-asking.
- Assumes Architecture and data: `architecture-data/repositories/what-is-a-repository` (phase 4) — the client lives behind a repository; this branch never puts a call in a screen.
- Assumes Architecture and data: `architecture-data/layers-and-mapping/mapping-between-models` (phase 4) — used by when-json-does-not-match (response model versus app model).
- Assumes Architecture and data: `architecture-data/offline-first/single-source-of-truth` (phase 4) — used by reusing-saved-answers and keeping-phone-and-server-in-step.
- Helps to know Architecture and data: `architecture-data/dependency-injection/what-is-a-dependency` — the client is one shared object; the lesson recaps this in one line.
- Helps to know Android core and lifecycles: `android-core/manifest-and-resources/what-is-the-manifest` and `android-core/permissions/why-permissions` (phase 5) — why-my-first-call-fails recaps the one permission it needs.
- Helps to know Android core and lifecycles: `android-core/background-work/what-is-workmanager` and `android-core/background-work/when-work-fails` (phase 5) — keeping-phone-and-server-in-step names WorkManager for sending queued edits.
- Helps to know UI: `ui/side-effects/collecting-flows` and `ui/lists/lazy-column` (phase 3 and 5) — screens that show these results are named only.
- Helps to know Security: `security/secure-storage/where-data-lives-on-a-phone`, `security/network-security/network-security-config-and-pinning` and `security/secrets-and-logs/sensitive-data-in-logs` — token storage, certificate pinning and tokens in logs are named once and taught there.
- Helps to know Testing: `testing/test-doubles/what-is-a-test-double` — the plain-Kotlin fake client used in challenges is a test double.

Library choice (one place only): the worked client is Retrofit on top of OkHttp, with kotlinx.serialization for JSON and Coil for images, because these are the libraries a fresher meets most in existing and AI-written Android code. Leaf titles stay about the idea, not the library. The Ktor client, which shares code with iOS, is taught in `kmp` (`kmp/shared-networking/a-shared-api-client`), which assumes the ideas here. ⚠️ VERIFY on the publish date: current Retrofit and OkHttp versions (sources conflict on which OkHttp version Retrofit 3 uses), suspend-function support in Retrofit, and the current JSON converter setup for kotlinx.serialization.

Challenge note: the Kotlin Playground cannot reach real servers or run Retrofit and OkHttp. Challenges use a plain-Kotlin fake client that returns canned responses, or are read-only "which line is wrong" tasks on real client code. ⚠️ VERIFY whether kotlinx.serialization is available in the Playground (`docs/06` once written). Android-only behaviour (permissions, screens) is shown with `code-to-screen` sims (`docs/05` §4.3), never an emulator (L15).

Sim share note: this branch is about time, waiting and flow, so its sim share (8 of 17, about 47 %) is above the 15–25 % guide for "elsewhere" in `docs/08` §6. Two of them (`timeouts`, `cancelling-a-call`) reuse the `kinds-of-failure` sim.

## Stems

### requests-from-an-app · Requests from an App

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-an-app-sends-and-gets` | easy | "Which four parts does my app put in a request, and which two come back?" | Posting a letter and getting a reply: the address on the envelope, the kind of request written on the front, a note stuck to the outside, and the letter inside; the reply comes with a result code. | "The app sends only the address, and the server works out the rest." | `sim:` a parcel rolls along a pipe from a phone to a server; the learner edits the kind of request, the address, the outside note and the inside body, and a reply parcel returns with a status label. | `P:` which status label comes back for an address that does not exist (fake server) · `P:` which part carries a search word · `B:` change the kind of request and see the reply change | Assumes Computer networking: `networking/http/request-and-response`. ⚠️ VERIFY: which request parts the current client sets by default (for example headers added automatically). |
| `why-my-first-call-fails` | easy | "What must my app declare and allow before any call can leave the phone?" | A stadium gate with two checks: a ticket (permission) and a rule of "sealed bags only" (secure connections only). | "If the address works in my laptop browser, it works in my app." | `none:` two settings and one rule; a before and after checklist plus a reference video of the real error are clearer than motion. | `P:` which of three addresses an app allows by default · `F:` a manifest that is missing the internet permission (read-only) · `F:` an `http` address in an app that allows only secure ones (read-only) | volatile. Checked 18 Oct 2026 (developer.android.com network security config): from API 28 cleartext traffic is off by default. ⚠️ VERIFY internet permission is still granted at install, and the exact crash message for a call on the main thread. Helps to know Android core and lifecycles: `android-core/permissions/why-permissions`. |

### http-client · The HTTP Client

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `first-get-call` | medium | "What are the three pieces I write to fetch data: an interface, a client and a call?" | Ordering through a waiter: you write the order on a card, the waiter walks to the kitchen, and you do not stand at the kitchen door. | "A network call is a normal function: it returns at once and the answer is on the next line." | `none:` one pattern of three pieces; a numbered code listing with a fake client is clearer, and the timing story is told in `concurrency/suspend/what-suspend-means`. | `P:` which line prints first when the fake client waits two seconds · `F:` a call made outside a coroutine (read-only) · `B:` remove `suspend` from the interface | Assumes Concurrency: `concurrency/suspend/what-suspend-means` and `concurrency/dispatchers/main-io-and-default`. ⚠️ VERIFY suspend support in the current Retrofit, and the base-address rule (trailing slash). Library named once in Branch prerequisites. |
| `building-requests` | easy | "How do I send a search word, an id inside the address and a form body?" | A shop order form with three zones: what to look for, which shelf, and the part you sign and hand over. | "I must glue the search word into the address by hand." | `none:` a syntax rule; a table of marked parameters and the address each one builds is clearer. | `P:` the final address built from a path id and a search word · `F:` an id placed in the search part instead of the path · `B:` put a space in the search word and see it encoded | Words like GET, POST, PUT and DELETE are used, not taught (`networking/http/http-methods`). ⚠️ VERIFY: the current client's annotation names for path, query and body parts. |
| `interceptors-and-logging` | easy | "How can one piece of code touch every call, and why must it not print secrets?" | A mail room that stamps every outgoing envelope and keeps a copy of each. | "Logging the full request in the released app is harmless." | `none:` a plain pipeline idea; two numbered stations in a code listing are enough. | `P:` print order of two interceptors in a fake chain · `F:` a logging interceptor that prints the authorisation header · `B:` swap the order of the two stations | Helps to know Security: `security/secrets-and-logs/sensitive-data-in-logs`. ⚠️ VERIFY current logging-level names. |

### json · JSON

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `json-to-kotlin-objects` | easy | "How does the text `{"name":"Sam"}` become a `User` with a name?" | Copying an order from a form in another language onto your own form by matching the box names. | "JSON is already a Kotlin object once it arrives." | `none:` a mapping rule; JSON and the matching class side by side are clearer. | `P:` what `user.name` holds after decoding a given text · `F:` a property name that differs from the key in the text · `B:` change a number in the text into words and see the decode fail | Assumes Kotlin: `kotlin/special-classes/data-classes`. ⚠️ VERIFY whether the serialization library runs in the Kotlin Playground; otherwise the challenges use a fake decoder or are read-only. |
| `when-json-does-not-match` | medium | "Which changes by the server crash my decode, and which does my app survive?" | A shop that redesigns its order form: a new box appears, an old one vanishes, and your old copy must still be readable. | "If my class has every field the server sends today, it will keep working." | `none:` a rule table of change type against outcome (extra key, missing key, wrong type, null) is clearer than motion. | `P:` which of four server changes crash decoding by default · `F(ai):` a tidy model with non-null fields for data the server sometimes leaves out · `B:` add an unknown key to the text | Assumes Architecture and data: `architecture-data/layers-and-mapping/mapping-between-models` (response model versus app model). ⚠️ VERIFY the setting that ignores unknown keys. |

### errors-and-retries · Errors and Retries

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `kinds-of-failure` | medium | "How do I tell no signal from a server error from a bad reply?" | A parcel that can fail four ways: no road to the town, the shop says "not here", the shop is on fire, or the box holds the wrong goods. | "No signal and a server error are the same kind of failure, so one catch handles both." | `sim:` code-to-screen three switches (no signal, server error, bad reply) against two code variants (nothing handled, each case turned into a message); the drawn screen shows a spinner that never ends or a clear message. | `P:` which message shows for a 404 with the fake client · `F(ai):` tidy code that catches every exception and shows one vague message · `F(ai):` code that treats a 500 reply as success because nothing was thrown · `B:` switch on no signal in the sim | Assumes Kotlin: `kotlin/errors-and-results/result-and-runcatching`. Assumes Computer networking: `networking/http/status-codes`. ⚠️ VERIFY: which failures the current client throws and which it returns as a response object. Links to `kotlin/special-classes/sealed-classes` for the screen state. |
| `timeouts` | medium | "After how many seconds does my call give up and show an error?" | Waiting at a bus stop: you decide before you leave home how long you will wait, then you walk. | "A call waits as long as it needs, so I never have to set a limit." | `sim:` reuse `kinds-of-failure` code-to-screen sim, with a slow-server slider; the timeline shows connect, wait and read, and the call is cut when the slider passes the limit. | `P:` after how many seconds the screen shows an error for given limits · `F(ai):` one huge timeout copied to every call · `B:` slide the server to slower than the limit | ⚠️ VERIFY: default timeout values of the current client and the difference between connect, read and whole-call limits. |
| `cancelling-a-call` | hard | "If the user leaves the screen while a call is running, does the call stop?" | Cancelling a taxi booking when you walk away: if you never tell the taxi, the meter keeps running. | "Calls always end by themselves, so there is nothing to clean up." | `sim:` reuse `kinds-of-failure` code-to-screen sim, with a back button; the learner flips between a call launched in the screen's scope and one launched in `GlobalScope`, and leaving the screen cuts only the first. | `F(ai):` a call launched in `GlobalScope` that keeps running after the screen closes · `P:` which log lines print when the screen closes in the middle of a call (fake client) · `B:` press back while the call is running | Assumes Concurrency: `concurrency/cancellation-and-errors/cancel-is-cooperative`. ⚠️ VERIFY: how the current client cancels an in-flight call when its coroutine is cancelled. |
| `retry-with-backoff` | medium | "How should my app retry a failed call so it never makes things worse?" | Knocking on a friend's door: wait a little longer after each knock, and do not let the whole street knock at the same second. | "Trying again at once until it works is the safest thing." | `sim:` balls bounce back from a gate with growing gaps; fifty phones retry together at the same moments until the learner switches on random spread (jitter). | `P:` the times of four tries that start at 1 second and double · `F(ai):` a tidy retry loop with no limit and a fixed delay · `F(ai):` a retry on a payment POST that can charge twice · `B:` switch on jitter and watch the crowd thin out | Introduces idempotent in plain words ("same result if done twice"). Helps to know Android core and lifecycles: `android-core/background-work/when-work-fails` (WorkManager has its own backoff; do not mix). |

### auth-tokens · Auth Tokens

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `how-apps-prove-who-they-are` | medium | "Which header carries the token, and why is a key inside the app not a secret?" | A festival wristband: it shows you paid, it can be switched off, and it does not hold your bank card. | "An API key in my app's code is a secret." | `none:` a three-step message flow (log in, receive token, send token); a labelled diagram of what travels in each step is clearer than motion. | `F(ai):` a generated client with the API key as a string constant "for convenience" · `F(ai):` a password sent with every call · `P:` which header carries the token | Assumes Computer networking: `networking/http/headers-and-bodies`. Helps to know Security: `security/auth/why-tokens-not-passwords` and `security/secure-storage/where-data-lives-on-a-phone` (where to keep the token). |
| `refreshing-an-expired-token` | hard | "What should my app do on a 401, and how do I stop five calls from each refreshing?" | A wristband that expires at midnight: one desk swaps it, and the five people in the queue should not each demand their own. | "When a call fails with 401 the app should log the user out at once." | `sim:` five calls hit the gate together and all get a 401; one refresh desk lets a single swap happen and the rest retry with the new wristband; the learner removes the lock and sees five swaps. | `F(ai):` refresh code that loops forever when the refresh call itself returns 401 · `F(ai):` five parallel refreshes with no lock · `P:` how many refresh calls happen after five parallel 401s, with and without a lock | Assumes Concurrency: `concurrency/scopes/parent-and-child-coroutines` (one shared job). ⚠️ VERIFY how the current client's authenticator hook retries a call. |

### caching · Caching

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `reusing-saved-answers` | medium | "Given these server headers, will the second call go to the network?" | A milk carton with an expiry date and a "check with the shop" sticker: fresh milk is used, old milk is checked first. | "Caching means saving every answer in my own database." | `none:` a rule table of header values against what the client does, stepped with a fake clock in the editor, is clearer than motion. | `P:` whether the second call hits the network for a given max-age and gap · `F:` a price list shown a day late because of a long max-age · `B:` change max-age to 0 | Assumes Architecture and data: `architecture-data/offline-first/cache-then-refresh` (HTTP cache versus database). Helps to know Computer networking: `networking/http/headers-and-bodies` (caching headers). ⚠️ VERIFY the current client's cache setup. |

### images · Images

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `images-from-urls` | medium | "Why does an image library show a placeholder, resize the picture and keep two caches?" | A photo lab that makes a thumbnail print once and keeps it in a drawer, so the next customer gets it at once. | "Download the full photo and let the screen shrink it." | `none:` a library-use rule; the memory side belongs to `profiling/memory-and-leaks/read-memory-use`, and a reference video of a list with and without placeholders shows the effect. | `P:` which cache answers when the user scrolls back up · `F(ai):` a list item that downloads picture bytes by hand and decodes the full size · `B:` scroll fast with no placeholder (video) | Assumes Concurrency: `concurrency/dispatchers/main-io-and-default`. Helps to know UI: `ui/lists/lazy-column`. ⚠️ VERIFY the current image library and its Compose entry point. |

### real-time · Real-Time Updates

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `live-updates-without-asking` | medium | "When should I poll, hold a socket open, or let the server wake the phone?" | Checking the letterbox every minute versus hearing the doorbell. | "A socket stays connected forever once it is opened." | `sim:` a postman checks a letterbox every N seconds while an open pipe delivers news at once; the learner drops the signal and sees the open pipe die silently until a heartbeat notices. | `P:` how many requests polling every 5 seconds makes in a minute · `F(ai):` a socket opened in a screen and never closed · `F(ai):` a reconnect loop with no backoff · `B:` drop the signal in the sim | Assumes Computer networking: `networking/websockets/websockets-and-live-updates`. Assumes Concurrency: `concurrency/flow-basics/what-is-a-flow`. volatile. ⚠️ VERIFY: Doze and background limits close sockets; push messages through Google's messaging service are named only (current name and rules). Helps to know Android core and lifecycles: `android-core/background-work/why-background-is-limited`. |

### sync · Sync

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `keeping-phone-and-server-in-step` | hard | "If I edit a note on a plane and my friend edits it online, what does the app do when I land?" | Two people with paper copies of one recipe, one on a plane: when they meet they must decide whose change stays. | "The latest change always wins and nobody loses anything." | `sim:` two timelines, phone and server, with edit balls; going offline holds balls in an outbox, reconnecting meets them in the middle, and the learner picks a rule (last write wins, keep both, ask the user) and sees which edits are lost. | `P:` the final value under last-write-wins for two timed edits · `F(ai):` an outbox that deletes queued edits when the first send fails · `F(ai):` a sync that overwrites newer server data with older local data · `B:` edit the same item on both sides | Synthesis leaf; fruit. Assumes Architecture and data: `architecture-data/offline-first/offline-writes`. Assumes Android core and lifecycles: `android-core/process-death/what-is-process-death`. Helps to know Android core and lifecycles: `android-core/background-work/what-is-workmanager`. Advanced conflict handling is in the Later list. ⚠️ VERIFY: current WorkManager guidance for sending queued edits. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `app-networking/sync/keeping-phone-and-server-in-step` | "A notes app that survives bad signal" | Loads and saves notes through a repository; no network call inside a screen · shows a different message for no signal, a server error and a bad reply · every call has a timeout and stops when its screen closes · edits made offline are queued and sent later with limited retries and growing waits · two devices editing one note end in a stated rule, never silent loss · no key or token in source files or logs; the server may be a fake or self-hosted one | The app talks to a server and stays correct when the network does not (`docs/01` §6.2 ability, L24). ⚠️ VERIFY exact ability wording in `docs/01`. This is the only fruit: all 17 leaves sit within the F3 gap of 15. |

## Later

- GraphQL clients: most apps still meet plain HTTP APIs first.
- gRPC: a different way to describe calls, needed only when a team chooses it.
- Advanced sync conflict resolution (merge rules, vector clocks): the sync leaf covers the three simple rules.
- HTTP/2 multiplexing from the client side: the client library handles it, so the lesson would not change what the learner writes.
- Server-sent events as a separate topic: the real-time leaf names them next to sockets.
- OAuth login flows in depth: belongs with the Security branch.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 9 stems, 16 leaves, all `shadow`. | +16 | Estimate 16, band 13–19: inside the band. Followed the `docs/08` §9 sketch (2, 3, 2, 3, 2, 1, 1, 1, 1). Two sketch slots were given a sharper idea: the first stem splits "what is sent" from "why the first call fails", and the sketch's one-leaf sync is the synthesis leaf that carries the only fruit (F2). |
| 2026-10-18 | Split `timeouts-and-cancellation` into `timeouts` (medium) and `cancelling-a-call` (hard); both are shadow, so the slug change is allowed (R3). `kinds-of-failure` and `retry-with-backoff` reworded to one idea each; slugs unchanged. Fruit row: constraints cut from 7 to 6 (F5). One `⚠️ VERIFY` added to `keeping-phone-and-server-in-step`. | +1 | A2e review: the old leaf taught two mechanisms (a time limit, and cancelling with the screen's scope), each with its own misconception and challenge (`docs/08` §3 split test). 17 leaves, band 13–19: still inside. Difficulty mix 5 / 9 / 3. |

