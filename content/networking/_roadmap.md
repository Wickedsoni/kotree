```yaml
branch: networking
bough: cs-theory
stems:
  - id: how-the-internet-works
    title: How the Internet Works
    why: Every app that loads data is a client talking to a server over a network that cuts messages into pieces, and a fresher needs that picture before any detail.
    leaves:
      - id: clients-and-servers
        title: When my app loads data, which computer asks and which one answers?
        difficulty: easy
        requires: []
        fruit: false
      - id: how-data-travels-in-packets
        title: How does a message cross the internet as many small packets?
        difficulty: easy
        requires: [networking/how-the-internet-works/clients-and-servers]
        fruit: false
  - id: layers
    title: Layers
    why: Network errors are named after the layer that failed, so a fresher needs the layers to read an error message.
    leaves:
      - id: why-networks-use-layers
        title: Why does a message pass through several layers before it leaves my phone?
        difficulty: medium
        requires: [networking/how-the-internet-works/how-data-travels-in-packets]
        fruit: false
  - id: ip-and-dns
    title: Addresses and Names
    why: A request needs an address to go to, and a failed name lookup is one of the most common reasons an app shows an error.
    leaves:
      - id: ip-addresses
        title: What does an IP address tell the network about my phone?
        difficulty: easy
        requires: [networking/how-the-internet-works/how-data-travels-in-packets]
        fruit: false
      - id: how-packets-find-a-route
        title: How does a packet find its way across many routers?
        difficulty: medium
        requires: [networking/how-the-internet-works/how-data-travels-in-packets, networking/ip-and-dns/ip-addresses]
        fruit: false
      - id: dns
        title: How does a name like example.com become an address?
        difficulty: medium
        requires: [networking/ip-and-dns/ip-addresses]
        fruit: false
  - id: tcp-and-udp
    title: TCP and UDP
    why: Timeouts, refused connections and choppy calls all come from how data is delivered, and a fresher should know what each way promises.
    leaves:
      - id: ports-and-sockets
        title: How does data reach the right app on my phone?
        difficulty: medium
        requires: [networking/ip-and-dns/ip-addresses, networking/layers/why-networks-use-layers]
        fruit: false
      - id: tcp-reliable-delivery
        title: How does TCP make an unreliable network feel reliable?
        difficulty: hard
        requires: [networking/how-the-internet-works/how-data-travels-in-packets, networking/tcp-and-udp/ports-and-sockets]
        fruit: false
      - id: udp-speed-over-reliability
        title: Why do video calls skip the safety checks that downloads use?
        difficulty: easy
        requires: [networking/tcp-and-udp/tcp-reliable-delivery]
        fruit: false
  - id: http
    title: HTTP
    why: Almost every app call is an HTTP request, and ignoring the method or the status code is the commonest flaw in generated networking code.
    leaves:
      - id: request-and-response
        title: What do a request and a response look like on the wire?
        difficulty: easy
        requires: [networking/ip-and-dns/dns, networking/tcp-and-udp/tcp-reliable-delivery]
        fruit: false
      - id: http-methods
        title: Which requests are safe to send twice?
        difficulty: easy
        requires: [networking/http/request-and-response]
        fruit: false
      - id: status-codes
        title: What does the number at the top of a response tell my app?
        difficulty: easy
        requires: [networking/http/request-and-response]
        fruit: false
      - id: headers-and-bodies
        title: Where does information go in a request, in the headers or in the body?
        difficulty: easy
        requires: [networking/http/request-and-response]
        fruit: false
      - id: stateless-requests
        title: Why must every request prove who I am again?
        difficulty: medium
        requires: [networking/http/status-codes, networking/http/headers-and-bodies]
        fruit: false
  - id: tls
    title: HTTPS and TLS
    why: Apps are expected to use HTTPS, and a certificate error is a trust problem that generated code often hides with an unsafe shortcut.
    leaves:
      - id: why-https
        title: What can someone on the same Wi-Fi read when an app uses plain HTTP?
        difficulty: medium
        requires: [networking/http/request-and-response, networking/tcp-and-udp/tcp-reliable-delivery]
        fruit: false
      - id: certificates-and-trust
        title: How does my phone know it is talking to the real server?
        difficulty: hard
        requires: [networking/tls/why-https]
        fruit: false
  - id: mobile-networks-and-latency
    title: Mobile Networks and Latency
    why: Phones move, lose signal and switch networks, and a screen that is fast on office Wi-Fi can crawl on mobile data.
    leaves:
      - id: latency-and-round-trips
        title: Why are ten small requests slower than one big one?
        difficulty: medium
        requires: [networking/tcp-and-udp/tcp-reliable-delivery, networking/http/request-and-response]
        fruit: false
      - id: flaky-mobile-networks
        title: What happens to a request when the signal drops or the network changes?
        difficulty: medium
        requires: [networking/mobile-networks-and-latency/latency-and-round-trips, networking/ip-and-dns/ip-addresses]
        fruit: false
  - id: api-styles
    title: API Styles
    why: A fresher meets URLs like /users/42/posts on day one and needs to read what they ask for.
    leaves:
      - id: rest-style-apis
        title: What does a URL like /users/42/posts ask for?
        difficulty: easy
        requires: [networking/http/request-and-response, networking/http/http-methods]
        fruit: false
  - id: websockets
    title: WebSockets
    why: Chats, scores and live prices need the server to speak first, which ordinary requests cannot do.
    leaves:
      - id: websockets-and-live-updates
        title: How can a server send my phone a message without being asked?
        difficulty: medium
        requires: [networking/tcp-and-udp/tcp-reliable-delivery, networking/http/request-and-response]
        fruit: false
  - id: putting-it-together
    title: Putting It Together
    why: The branch ability is to name the network step behind a symptom, so the branch ends on matching symptoms to causes.
    leaves:
      - id: diagnose-a-failed-request
        title: Which step of the journey failed when a request does not work?
        difficulty: hard
        requires: [networking/ip-and-dns/dns, networking/tcp-and-udp/tcp-reliable-delivery, networking/http/status-codes, networking/tls/certificates-and-trust]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Every stem is phase 4 (`docs/08` §10.1). Android framework code does not run in the browser editor and there is no Android emulator or simulator (L15, D8). The Kotlin Playground may not be able to open network connections, so challenges use printed text, small plain-Kotlin models and diagrams. ⚠️ VERIFY on kotlinlang.org whether the Playground allows outgoing network calls before any leaf promises a live request.

Rule for the phase checks below: networking is phase 4 and comes after `foundation`, `kotlin`, `tools`, `os` and `dsa` in the tie-break order, so it may assume leaves from those branches (phase 4 or lower for the OS and DSA stems). Android core, Concurrency and Networking inside apps come later in the tie-break order, so they are "Helps to know" only.

- Assumes Foundation: `foundation/how-computers-work/what-does-a-computer-do` (phase 1) — used by clients-and-servers.
- Assumes Foundation: `foundation/how-computers-work/what-is-a-bit` (phase 1) — used by how-data-travels-in-packets.
- Assumes Foundation: `foundation/how-computers-work/how-letters-become-numbers` (phase 1) — used by request-and-response (a request is text turned into bytes).
- Assumes Kotlin language: `kotlin/collections/maps` (phase 2) — headers and name caches are maps.
- Assumes Kotlin language: `kotlin/strings/everyday-string-functions` and `kotlin/strings/text-to-numbers` (phase 2) — the printed-text tasks cut a request apart and read a status code as a number.
- Assumes Operating systems: `os/processes/what-is-a-process` (phase 3) — used by ports-and-sockets (which process listens on a port).
- Assumes Operating systems: `os/threads/a-thread-that-waits` (phase 3) — used by latency-and-round-trips and diagnose-a-failed-request (a call that waits for a reply).
- Assumes DSA: `dsa/queues/what-is-a-queue` (phase 3) — used by how-data-travels-in-packets (a full router queue drops packets).
- Helps to know DSA: `dsa/trees/what-is-a-tree` and `dsa/hashing/hashing-idea` — names and paths form a tree, and a name cache is a lookup table; each is recapped in one paragraph.
- Helps to know Android core and lifecycles: `android-core/permissions/why-permissions` and `android-core/manifest-and-resources/what-is-the-manifest` — the INTERNET permission in clients-and-servers.
- Helps to know Android core and lifecycles: `android-core/background-work/why-background-is-limited` and `android-core/process-death/what-is-process-death` — why a live connection does not survive a closed app.
- Helps to know Concurrency: `concurrency/coroutine-basics/async-and-await` and `concurrency/suspend/suspend-vs-blocking` — the fix for a chain of slow calls.
- Helps to know Networking inside apps: `app-networking/requests-from-an-app/what-an-app-sends-and-gets` and `app-networking/http-client/building-requests` — this branch is the theory; the app branch is the code.
- Helps to know Security: `security/network-security/network-security-config-and-pinning` — certificate pinning and cleartext rules are named once and taught there.

Foundation is recommended first ("Start here"), then `kotlin` and `os` (processes and threads).

Every theory leaf names the Android symptom or app situation it explains in its Notes cell (`Android:`), as the Theory-Scared Student requires (`docs/01` §2.3). Error names, default rules for plain HTTP, connectivity APIs and background limits are `volatile` or carry `⚠️ VERIFY` against developer.android.com before the leaf goes live. No timeout, size or API-level number is claimed in this roadmap. The branch teaches the four-layer picture (app, transport, internet, link); the seven-layer table is named once and kept in `## Later`.

Cited by other roadmaps, with the full leaf ID that should replace each stem reference when those roadmaps are next edited through `curriculum-designer`:

- `app-networking` cites stem `networking/http`: use `networking/http/request-and-response` (first-get-call), `networking/http/http-methods` (retry-with-backoff), `networking/http/status-codes` (kinds-of-failure), `networking/http/headers-and-bodies` (building-requests) and `networking/http/stateless-requests` (how-apps-prove-who-they-are).
- `app-networking` cites stem `networking/tls`: use `networking/tls/why-https`.
- `app-networking` cites stem `networking/websockets`: use `networking/websockets/websockets-and-live-updates` (live-updates-without-asking).
- `architecture-data` cites stem `networking/http`: use `networking/http/request-and-response`.

## Stems

### how-the-internet-works · How the Internet Works

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `clients-and-servers` | easy | "When my app loads data, which computer asks and which one answers?" | A restaurant: the customer asks, the waiter carries the order, the kitchen answers; the kitchen never walks to your table on its own. | "My phone is wired straight to the data, and the internet is a cloud that stores everything." | `none:` one diagram of two boxes and an arrow; the moving version, with packets, is in the next leaf. | `P:` which side starts the conversation in four situations (open a page, get a chat message, upload a photo, receive a news alert) · `F:` a model where the "server" function calls the "client" before being asked · `B:` remove the server and watch the request wait | Android: the spinner that never ends when the server is down. ⚠️ VERIFY: the INTERNET permission is granted at install time and is not a runtime prompt (developer.android.com). Assumes Foundation: `foundation/how-computers-work/what-does-a-computer-do`. Helps to know Android core and lifecycles: `android-core/permissions/why-permissions`. |
| `how-data-travels-in-packets` | easy | "How does a message cross the internet as many small packets?" | A book posted as numbered postcards: each takes its own route, may arrive late or not at all, and the reader puts them in order. | "A message travels as one piece along one fixed wire." | `sim:` balls (packets) roll through a network of pipes and junctions, each carrying a number; the learner closes a pipe or drops a ball, and the receiving tray re-orders the rest and asks for the missing one. | `P:` the order packets arrive in on a drawn map with two routes · `F:` a model that joins received pieces in arrival order, not by number · `B:` drop packet 3 | Android: a photo that loads in strips on a slow connection, and a video that stops to buffer. Assumes Foundation: `foundation/how-computers-work/what-is-a-bit`. Assumes DSA: `dsa/queues/what-is-a-queue` (a full router queue drops packets). |

### layers · Layers

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-networks-use-layers` | medium | "Why does a message pass through several layers before it leaves my phone?" | A letter inside an envelope inside a courier box inside a lorry: each handler reads only the label on the outside. | "Every layer reads and understands the whole message." | `sim:` animation of nested wrappers that close around a message ball on the way out and open one by one on the way in; the learner tries to open the wrong wrapper and fails. | `P:` which layer reads which label in a drawn example · `F:` a model where the transport step edits the app's message · `B:` remove the middle wrapper | Android: every network error name belongs to one layer (a name problem, a connection problem, a trust problem); `networking/putting-it-together/diagnose-a-failed-request` returns to this. ⚠️ VERIFY exact exception names at developer.android.com and the Java documentation. Uses the four-layer picture; the seven-layer table is named once. |

### ip-and-dns · Addresses and Names

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `ip-addresses` | easy | "What does an IP address tell the network about my phone?" | A postal address for the building you are in right now: move to another building and the address changes. | "My phone has one fixed address wherever I go." | `none:` an address format and a small table (the home Wi-Fi address versus the address a website sees); moving packets is in the next leaf. | `P:` which of five addresses are private (standard private ranges given on the page) · `F:` a program that stores the phone's address as a permanent user ID · `B:` change the Wi-Fi and ask what changed | Android: an app that used the address as a login key breaks when the user switches from Wi-Fi to mobile data. Assumes Kotlin language: `kotlin/strings/text-to-numbers`. IPv6 details are in `## Later`. |
| `how-packets-find-a-route` | medium | "How does a packet find its way across many routers?" | Parcels passing through sorting offices: each office knows only which door to send it to next, and a counter on the label stops it circling for ever. | "The whole route from my phone to the server is planned before the packet leaves." | `sim:` reuse `networking/how-the-internet-works/how-data-travels-in-packets` sim, with routers as junctions that carry a "next door" sign and a hop counter on every ball; a loop in the signs shows a ball running out of hops. | `P:` the hops a packet takes on a drawn map · `F:` a routing model with a loop that never ends · `B:` close one router | Android: a site that is quick on Wi-Fi and slow on mobile data because the route differs. ⚠️ VERIFY: which route-tracing tools exist on a phone before naming any. Routing between networks (BGP) is in `## Later`. |
| `dns` | medium | "How does a name like example.com become an address?" | A phone book that points to another phone book: ask the local library, then the city office, then the street office, and everyone writes the answer down for next time. | "My phone already knows every website's address and never has to ask." | `sim:` animation of a question ball that travels from the phone to a helper and on to higher offices and back; a cache shelf near the phone answers repeat questions at once; the learner breaks one office and sees the error. | `P:` how many lookups a second visit needs when the answer is saved · `F:` a name cache that never forgets, so a moved server stays unreachable · `B:` break one office | Android: `UnknownHostException` ("unable to resolve host") when a name cannot become an address, for example with no signal. ⚠️ VERIFY exception name and message at developer.android.com. Assumes Kotlin language: `kotlin/collections/maps`. Helps to know DSA: `dsa/trees/what-is-a-tree`, `dsa/hashing/hashing-idea`. |

### tcp-and-udp · TCP and UDP

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `ports-and-sockets` | medium | "How does data reach the right app on my phone?" | An office building: the street address gets the parcel to the building, the flat number gets it to the right tenant. | "The IP address is enough to reach an app." | `sim:` reuse `networking/how-the-internet-works/how-data-travels-in-packets` sim, with a block of numbered flats at the end of the pipe; a ball for an empty flat bounces back marked "refused". | `P:` which app receives a packet for a given flat number in a drawn table · `F:` a client that sends to the wrong port and ignores the "refused" answer · `B:` send a ball to an empty flat | Android: "connection refused" means the address was right but nobody was listening on that port. ⚠️ VERIFY message text. Assumes Operating systems: `os/processes/what-is-a-process`. A **socket** is named once as one end of a connection. |
| `tcp-reliable-delivery` | hard | "How does TCP make an unreliable network feel reliable?" | Recorded-delivery post: every parcel gets a receipt, and with no receipt the sender sends it again. | "TCP guarantees that a request will always get through." | `sim:` balls sent through a leaky pipe, each needing an "ok" ball back; the learner sets the leak; a missing "ok" makes the sender resend; a counter shows total time rising with loss; the opening handshake is shown as one round trip. | `P:` how many round trips pass before the first byte of data · `P:` how many sends are needed when 1 of 5 pieces is lost · `F:` a sender model that never resends · `B:` set the leak to a high value | Android: `SocketTimeoutException` when the connection exists but answers stop coming. ⚠️ VERIFY name and meaning at developer.android.com; the connect-versus-read timeout idea is taught in `app-networking/errors-and-retries/timeouts`. Congestion control is named in one line only. |
| `udp-speed-over-reliability` | easy | "Why do video calls skip the safety checks that downloads use?" | A live radio broadcast versus a posted DVD: a lost word on the radio is skipped, because a late word is useless. | "UDP is a broken, second-rate version of TCP." | `sim:` reuse `networking/tcp-and-udp/tcp-reliable-delivery` sim, with the receipt balls switched off; in TCP mode a late piece holds everything up, in UDP mode it is skipped and the picture glitches. | `P:` which of TCP or UDP fits a download, a voice call and a chat message · `F:` a live-stream model that waits for a resend and falls ever further behind · `B:` turn the receipts off for a file download and watch the file break | Android: video calls and online games skip lost pieces; file downloads and API calls must not. ⚠️ VERIFY: HTTP/3 runs over UDP; its internals are in `## Later`. No Android class is named. |

### http · HTTP

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `request-and-response` | easy | "What do a request and a response look like on the wire?" | Filling in a form at a counter and getting a filled-in form back: a line saying what you want, some labelled boxes, and sometimes an attached document. | "A URL is a file path, and HTTP is only for web pages." | `none:` a raw request and response printed side by side in the Playground; the learner changes a line and sees what would change in the answer. | `P:` which line of a printed request names the thing asked for · `F:` a program that builds a request without the blank line before the body · `B:` change the path and predict the answer | Android: the lines a logging interceptor prints in Logcat are this text. ⚠️ VERIFY the interceptor wording in the library docs. Version differences are one paragraph, no leaf. Assumes Foundation: `foundation/how-computers-work/how-letters-become-numbers`. Assumes Kotlin language: `kotlin/strings/everyday-string-functions`. Cited by `app-networking` and `architecture-data` as stem `networking/http`. |
| `http-methods` | easy | "Which requests are safe to send twice?" | Reading a notice board is safe to repeat; posting a cheque twice may pay twice. | "GET and POST differ only in how the data is packed." | `none:` a table of the main methods (GET, POST, PUT, DELETE) and a Playground counter that shows what each call changes when it runs twice. | `P:` the counter after the same POST runs twice and the same PUT runs twice · `F(ai):` a tidy "load list" function that uses POST for a read · `F(ai):` a retry loop that resends a payment POST · `B:` resend the same request | Android: a double-tap on a Pay button, or a retry after a timeout, can charge twice. This is the base for `app-networking/errors-and-retries/retry-with-backoff`. ⚠️ VERIFY: the meanings of "safe" and "repeatable" against the HTTP standard (rfc-editor.org). |
| `status-codes` | easy | "What does the number at the top of a response tell my app?" | A shop reply: "here you go" (2xx), "it moved, go next door" (3xx), "you asked wrongly" (4xx), "our kitchen is on fire" (5xx). | "Any reply means my request worked." | `none:` a table of the five families with one decision column (show it, fix the request, wait and retry); a Playground function that prints a decision for each code is the whole idea. | `P:` for 200, 301, 401, 404 and 500, which action the app takes · `F(ai):` code that treats every answer as success and parses the body · `F(ai):` code that retries a 404 five times · `B:` change the code to 503 and re-predict | Android: a blank screen caused by parsing an error page as data; some HTTP libraries do not throw on 4xx or 5xx unless asked. ⚠️ VERIFY per-library behaviour in `app-networking` before claiming it. |
| `headers-and-bodies` | easy | "Where does information go in a request, in the headers or in the body?" | A parcel: the label on the box tells the courier who and what; the contents are for the receiver. | "The body holds all the information and the headers are decoration." | `none:` a labelled-parcel diagram and a Playground map of headers printed next to the body. | `P:` which of six items (login token, file bytes, language, JSON of a new note, content type, size) go in headers and which in the body · `F:` JSON sent without a content-type header and rejected · `B:` remove a header | Android: an "unsupported media type" style failure, and login tokens that travel in a header. ⚠️ VERIFY header naming in the authentication docs. Assumes Kotlin language: `kotlin/collections/maps`. Helps to know Networking inside apps: `app-networking/http-client/building-requests`. |
| `stateless-requests` | medium | "Why must every request prove who I am again?" | A bank clerk with no memory: each visit you show your card again, and nothing from the last visit is remembered. | "The server remembers me between requests, like a conversation." | `none:` two Playground runs: a server model that forgets between calls, then one that is given a token each time. | `P:` what a server model returns for the second call without a token · `F(ai):` a client that logs in once and assumes later requests are logged in · `B:` drop the token on the third call | Android: "401 after a while" and why apps attach a token to each request. Cookies are named in one line. Cited by `app-networking` for how-apps-prove-who-they-are. |

### tls · HTTPS and TLS

Flags: phase=4 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `why-https` | medium | "What can someone on the same Wi-Fi read when an app uses plain HTTP?" | A postcard versus a sealed, tamper-proof envelope: everyone who handles a postcard can read it. | "HTTPS hides which server I talk to, and it proves the server is honest." | `sim:` packet balls pass a snooper ball that can open clear balls and sees only opaque ones; the learner flips plain and secure and watches readable text turn to scrambled text. | `P:` what a snooper sees for HTTP and for HTTPS (path, body, which server) · `F(ai):` an API address written as `http://` "for testing" that ships · `F(ai):` a setting that allows plain traffic to every domain · `B:` flip to plain and read the login | Android: recent Android versions block plain HTTP by default. ⚠️ VERIFY from which API level, and the options in the network security configuration (developer.android.com). Cited by `app-networking` as stem `networking/tls`. The scrambling rides on top of the connection taught in `networking/tcp-and-udp/tcp-reliable-delivery`. |
| `certificates-and-trust` | hard | "How does my phone know it is talking to the real server?" | An ID card issued by an office you already trust: the clerk checks the stamp, the name and the expiry date. | "The padlock means the site is honest and safe." | `sim:` a chain of stamps from the server's card up to a trusted office stored in the phone; the learner moves the phone's clock past expiry or swaps the name and the chain turns red. | `P:` which of four certificates is rejected (expired, wrong name, unknown issuer, valid) · `F(ai):` a generated "fix" for a certificate error that trusts every certificate · `F(ai):` a client with the host-name check turned off · `B:` change the clock | Android: `SSLHandshakeException` ("Trust anchor for certification path not found") and a wrong phone clock that breaks HTTPS. ⚠️ VERIFY exact messages. The right fix is never "trust everything". Certificate pinning is named once; ⚠️ VERIFY current guidance on developer.android.com; depth belongs to the security branch. |

### mobile-networks-and-latency · Mobile Networks and Latency

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `latency-and-round-trips` | medium | "Why are ten small requests slower than one big one?" | A pipe: width is how much water passes each second, length is how long the first drop takes to arrive; ten trips to a far well cost ten walks however wide the bucket. | "A faster connection fixes every slow screen." | `sim:` balls in a pipe with sliders for width and length; ten small parcels, each with its own round trip, race one big parcel; a parallel-lanes switch shows the total shrink. | `P:` the total time for 10 requests one after another versus all at once, with numbers given on the page · `F(ai):` a screen that makes ten calls in a row where one combined call or parallel calls would do · `B:` widen the pipe and see little change | Android: a slow first load when a screen waits for a chain of calls, and an app that is quick on office Wi-Fi but slow on mobile data. Assumes Operating systems: `os/threads/a-thread-that-waits`. Helps to know Concurrency: `concurrency/coroutine-basics/async-and-await`. ⚠️ VERIFY: any typical round-trip times quoted on the page, against a named source. |
| `flaky-mobile-networks` | medium | "What happens to a request when the signal drops or the network changes?" | A phone call while driving through a tunnel: the line drops, you must dial again, and the other side may or may not have heard your last sentence. | "A request either works or returns a clear error." | `sim:` reuse `networking/mobile-networks-and-latency/latency-and-round-trips` sim, with a valve; the learner closes it mid-flight and a timer shows how long the app waits before giving up; switching from Wi-Fi to mobile data moves the pipe to a new address and the old connection breaks. | `P:` what the app sees (a wait, a reset, no route) in three situations on a diagram · `F(ai):` a request with no timeout that waits for ever · `B:` close the valve after half the data | Android: moving between Wi-Fi and mobile data, lifts and tunnels; Android reports connectivity changes through system callbacks. ⚠️ VERIFY the current connectivity API at developer.android.com. Never tell the user "no internet" for every failure. Helps to know Android core and lifecycles: `android-core/background-work/why-background-is-limited`. |

### api-styles · API Styles

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `rest-style-apis` | easy | "What does a URL like /users/42/posts ask for?" | A library catalogue: section, shelf, book; the path reads from wide to narrow. | "REST is a library or protocol I install." | `none:` a URL-anatomy diagram and a table of paths with the methods that make sense for them. | `P:` what `GET /users/42/posts` and `DELETE /users/42` each mean · `F(ai):` a generated client that builds URLs by gluing strings and forgets to encode a space · `B:` change the id and re-read the path | Android: interface functions in common HTTP libraries mirror these paths. ⚠️ VERIFY wording against the library docs used in `app-networking/http-client/building-requests`. GraphQL and gRPC are in the `app-networking` Later list. Helps to know DSA: `dsa/trees/what-is-a-tree`. |

### websockets · WebSockets

Flags: phase=4 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `websockets-and-live-updates` | medium | "How can a server send my phone a message without being asked?" | A phone call versus letters: letters need a request for each answer, while a call lets either side speak at any time. | "A server can contact an app at any time over ordinary HTTP." | `sim:` two pipes side by side; in the request pipe a ball comes back only after one goes out, in the open pipe the server drops balls in at any time; closing the screen cuts the open pipe. | `P:` how many requests a chat that asks every 5 seconds sends in a minute versus one open connection · `F(ai):` a chat client that never reconnects after the connection drops · `F(ai):` asking every second "for real time" · `B:` let the connection sit idle | Android: live chat, scores and typing indicators; the connection ends when the app is closed or the phone sleeps, so push messages exist. ⚠️ VERIFY current background rules and push options on developer.android.com. Server-sent events and long polling are named in one line, depth in `## Later`. Cited by `app-networking/real-time/live-updates-without-asking`. |

### putting-it-together · Putting It Together

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `diagnose-a-failed-request` | hard | "Which step of the journey failed when a request does not work?" | A parcel-tracking page that shows the last place the parcel was scanned: name lookup, connection, trust check, delivery, reply. | "A failed request always means the phone has no internet." | `sim:` animation of a journey track with six stations (name lookup, connect, secure handshake, send, server work, reply); the learner breaks one station and sees the matching error name and what the user would see. | `P:` match five symptoms (blank screen, spinner for ever, certificate error, "not found" page, works on Wi-Fi but not on mobile data) to the failed step · `F(ai):` a generated `catch (e: Exception)` that shows "No internet" for every failure · `F(ai):` an app that ignores the status code and parses an error page as data · `B:` break the name step and read the error | Android: the `docs/01` §6.2 check "match 5 symptoms to causes". Assumes Operating systems: `os/threads/a-thread-that-waits`. Branch synthesis; fruit (last leaf, `docs/08` §12 F2). |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `networking/putting-it-together/diagnose-a-failed-request` | Failure map of one request | Pick one request from an app you used or built · write its journey in six steps · for each step name one failure, the error name or symptom the user would see, and how you could tell it apart in a log · two failures reproduced as small plain-Kotlin models you ran · one case where the phone is fine and the server is at fault · a one-page write-up that also names one generated-code habit that would hide the cause | "Which network event causes a given app symptom" (`docs/01` §6.2, check: match 5 symptoms to causes; L24). Last leaf of the branch; the only fruit, because no segment end sits 15 leaves from it (F3). |

## Later

- Subnetting maths: calculation practice; ip-addresses only needs the idea of a private and a public address.
- IPv6 details: the app-level rules are the same; the format is a reference topic.
- HTTP/3 and QUIC internals: a library-author topic.
- CDNs: server-side delivery, not app code.
- Routing between networks (BGP): operator topic.
- The seven-layer model in depth: the four-layer picture is enough to read errors.
- Wi-Fi and cellular radio details: physical-layer topic.
- Server-sent events and long polling in depth: the websockets leaf names them in one line.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 10 stems, 21 leaves, all `shadow`. | +21 | Estimate 20, band 16–24: inside the band (+5 %). Differences from the `docs/08` §9 sketch: `layers` has 1 leaf, not 2, because the second idea (which layer shows which error) is the synthesis leaf; `http` has 5 leaves, not 4, because methods and status codes are two mechanisms (split test) and `stateless-requests` is needed by the app branch for tokens; `putting-it-together` (1 leaf) added so the last leaf is the symptom-matching synthesis leaf and the fruit proves the `docs/01` §6.2 ability (F2). Stem slugs `http`, `tls` and `websockets` are kept because other roadmaps cite them. |
| 2026-10-18 | Difficulty mix: 9 easy, 9 medium, 3 hard (14 % hard). New sims: 9 of 21 leaves (43 %), plus 4 reuse of an earlier sim and 8 `none:`. | 0 | Inside the 35–50 % guide for new sims. The eight `none:` leaves are rules, tables or printed text (addresses, HTTP text, methods, status codes, headers, tokens, URL paths, the first diagram), where a moving picture adds nothing a table does not. |
| 2026-10-18 | A2e review fixes: fruit row cut from 7 constraints to 6 (F5); the `Notes` pointer to the old app-networking leaf `timeouts-and-cancellation` now names `app-networking/errors-and-retries/timeouts`, the leaf it was split into. | 0 | No leaf added or removed. |
