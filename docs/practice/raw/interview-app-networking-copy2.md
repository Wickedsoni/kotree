# interview: app-networking

## app-networking/requests-from-an-app/what-an-app-sends-and-gets
interview-questions:
- [intern] What does your mobile app actually send across the network to a backend server, and what format comes back?
- [junior] What are the components of an HTTP request (method, headers, URI, body) and an HTTP response (status code, headers, payload)?
- [mid] Why is JSON (JavaScript Object Notation) the universal data interchange format for modern mobile applications over older XML formats?
- [senior] What goes wrong in production when mobile apps send oversized payloads or uncompressed JSON structures over metered cellular networks?
what-interviewers-listen-for:
- HTTP requests consist of a method, path, headers, and body; responses return status codes, headers, and data payloads.
- JSON is lightweight, human-readable, and natively supported by JavaScript and Kotlin serialization.
- Large payloads waste cellular bandwidth and increase latency on flaky mobile networks.
build-exercise: Inspect an HTTP request and response payload using Android Studio Network Profiler or Charles Proxy while triggering a sample API call.
official-references:
- Android connectivity overview | https://developer.android.com/training/volley/request-counter
common-wrong-beliefs:
- Network data sent from an app is transmitted as raw unformatted Java/Kotlin object memory structures directly to the server | Objects must be serialized into text-based formats (like JSON) before transmission across network sockets.

## app-networking/requests-from-an-app/why-my-first-call-fails
interview-questions:
- [intern] Why does your very first network request often fail before it even leaves the mobile device?
- [junior] What common setup mistakes cause network calls to fail instantly (missing `INTERNET` permission, cleartext HTTP blocks, missing network security config)?
- [mid] How do emulator network isolation and local host addresses (`10.0.2.2` vs `localhost`) affect local backend testing?
- [senior] What goes wrong in production when network failure handling lacks graceful degradation or offline fallback messaging?
what-interviewers-listen-for:
- Common root causes: missing `<uses-permission android:name="android.permission.INTERNET" />` in the manifest, cleartext traffic blocks, or incorrect emulator localhost routing (`10.0.2.2`).
- Failing to handle startup network failures crashes unseasoned apps.
build-exercise: Remove the internet permission from `AndroidManifest.xml`, make an API request, observe the immediate `SecurityException` in Logcat, and restore the permission.
official-references:
- Permissions overview | https://developer.android.com/guide/topics/permissions/overview
common-wrong-beliefs:
- An Android app has full permission to access the internet by default without declaring anything in the manifest | Declaring `<uses-permission android:name="android.permission.INTERNET" />` in the AndroidManifest.xml is mandatory for any network activity.

## app-networking/http-client/first-get-call
interview-questions:
- [intern] How do you fetch data from a server address with a few lines of Kotlin using a modern HTTP client like Ktor or Retrofit?
- [junior] What is the difference between defining API endpoints as Kotlin interfaces (Retrofit) versus DSL builder clients (Ktor)?
- [mid] Why must network calls in HTTP clients execute inside `suspend` functions off the main thread?
- [senior] What goes wrong in production when synchronous HTTP client calls are invoked directly on the main thread, triggering NetworkOnMainThreadException?
what-interviewers-listen-for:
- Retrofit uses annotated interfaces; Ktor uses type-safe builder DSLs.
- Executing network calls on the main thread throws `NetworkOnMainThreadException` and triggers ANRs.
build-exercise: Configure a simple Retrofit or Ktor client in Kotlin, invoke a GET request inside a `viewModelScope.launch`, and print the response body.
official-references:
- Retrofit documentation | https://square.github.io/retrofit/
- Ktor client getting started | https://ktor.io/docs/getting-started-ktor-client.html
common-wrong-beliefs:
- Calling a networking library's request method automatically shifts execution off the main thread to a background thread | Unless the library or caller explicitly dispatches to an I/O thread (`withContext(Dispatchers.IO)`), synchronous calls execute on the calling thread.

## app-networking/http-client/building-requests
interview-questions:
- [intern] How do you send dynamic parameters in an HTTP request (query parameters, path parameters, form-url-encoded bodies, JSON bodies)?
- [junior] What is the difference between `@Query`, `@Path`, and `@Body` annotations in Retrofit (or equivalent Ktor builder methods)?
- [mid] How do query parameter encoding and special character escaping work in URL builders?
- [senior] What goes wrong in production when dynamic path parameters contain unescaped slashes or special characters, breaking URL routing?
what-interviewers-listen-for:
- `@Path` replaces URL path segments; `@Query` appends query string parameters (`?search=kotlin`); `@Body` sends serialized payloads.
- Improperly encoded special characters corrupt URI structures.
build-exercise: Build a Retrofit interface or Ktor request adding a path ID and query parameters, and inspect the constructed outgoing URL string.
official-references:
- Retrofit API declarations | https://square.github.io/retrofit/#API-DECLARATION
common-wrong-beliefs:
- You can concatenate raw user input strings directly into URL paths without encoding (`"api/users/" + userInput`) | Unescaped user input containing special characters or slashes breaks URI routing and exposes injection vulnerabilities.

## app-networking/http-client/interceptors-and-logging
interview-questions:
- [intern] What is an HTTP interceptor, and how can you add a logging header or authorization token to every outgoing request automatically?
- [junior] What is the difference between OkHttp Application Interceptors and Network Interceptors?
- [mid] How do interceptors modify requests and responses immutably (`chain.proceed(request.newBuilder()...)` )?
- [senior] What goes wrong in production when OkHttp logging interceptors accidentally log sensitive user auth tokens or PII in release Logcat output?
what-interviewers-listen-for:
- Interceptors inspect, rewrite, or retry HTTP requests and responses globally.
- Application interceptors run once per call; network interceptors run for every retry/redirect.
- Logging sensitive tokens in production Logcat is a severe security vulnerability.
build-exercise: Create an OkHttp Interceptor that adds a custom header (`X-Client-Version: 1.0`), attach it to Retrofit, and verify it appears in outgoing requests.
official-references:
- OkHttp Interceptors | https://square.github.io/okhttp/features/interceptors/
common-wrong-beliefs:
- OkHttp interceptors mutate incoming request objects in place directly | Requests and responses in OkHttp interceptors are immutable; you must build and return a new request builder copy via `chain.proceed(newRequest)`.

## app-networking/json/json-to-kotlin-objects
interview-questions:
- [intern] How does raw text JSON from a server get transformed into typed Kotlin data classes?
- [junior] What is the role of serialization libraries like `kotlinx.serialization` or `Gson` / `Moshi` in JSON parsing?
- [mid] Why is `kotlinx.serialization` with the `@Serializable` annotation preferred in modern Kotlin and KMP projects over reflection-based Gson?
- [senior] What goes wrong in production when reflection-based JSON parsers fail to obfuscate property names under R8 release minification?
what-interviewers-listen-for:
- Serialization libraries map JSON fields to Kotlin class properties.
- `kotlinx.serialization` uses compiler-generated serializers at compile time, eliminating runtime reflection overhead and R8 obfuscation mapping bugs.
build-exercise: Define a data class annotated with `@Serializable`, use `Json.decodeFromString()` to parse a sample JSON string, and print the resulting object.
official-references:
- Kotlinx Serialization guide | https://kotlinlang.org/docs/serialization.html
common-wrong-beliefs:
- Gson and Moshi require zero configuration to work correctly when R8 code shrinking and obfuscation are enabled in release builds | Reflection-based libraries like Gson break under R8 obfuscation unless explicit keep rules are added; `kotlinx.serialization` avoids reflection entirely.

## app-networking/json/when-json-does-not-match
interview-questions:
- [intern] What happens to your app when the backend server adds, drops, or renames a JSON field unexpectedly?
- [junior] How do you handle missing JSON fields using default parameter values in Kotlin data classes?
- [mid] How do you handle unknown extra JSON properties or type mismatches (e.g., receiving a String instead of an Int) without crashing?
- [senior] What goes wrong in production when tight JSON schema coupling causes widespread app crashes whenever backend APIs deploy schema changes?
what-interviewers-listen-for:
- Assigning default parameter values in data classes prevents deserialization crashes when optional fields are missing.
- Using `@JsonNames` or lenient parsing configurations prevents schema mismatch crashes.
build-exercise: Parse a JSON string missing an optional field into a data class with a default argument value, and verify successful parsing.
official-references:
- Kotlinx Serialization JSON configuration | https://kotlinlang.org/docs/json-basics.html#uncaught-exceptions
common-wrong-beliefs:
- A deserialization crash caused by a backend server adding or renaming a JSON field is unavoidable on the mobile client side | Proper use of default parameter values, `@SerialName`, and lenient parsing configurations prevents schema mismatches from crashing the app.

## app-networking/errors-and-retries/kinds-of-failure
interview-questions:
- [intern] What are the different categories of network failure (DNS lookup failure, timeout, HTTP 4xx client errors, HTTP 5xx server errors, serialization exceptions)?
- [junior] How do you distinguish between client-side bugs (400 Bad Request, 401 Unauthorized) and server-side outages (500 Internal Server Error)?
- [mid] Why should 4xx client errors never be retried automatically by network retry interceptors?
- [senior] What goes wrong in production when generic error handlers treat HTTP 401 Unauthorized the same as HTTP 500 Server Error?
what-interviewers-listen-for:
- Differentiating error codes allows triggering specific UX flows (e.g., 401 triggers token refresh/logout; 500 triggers retry banner).
- 4xx errors indicate invalid client requests that will fail identically if retried.
build-exercise: Write a response code handler function that categorizes HTTP error codes into network drops, client auth errors, and server failures.
official-references:
- HTTP status codes | https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
common-wrong-beliefs:
- Retrying an HTTP 400 Bad Request or 401 Unauthorized error automatically in a retry loop will eventually succeed if you try enough times | Client error codes (4xx) indicate a request defect or auth failure that will never succeed on retry without changing request parameters or tokens.

## app-networking/errors-and-retries/timeouts
interview-questions:
- [intern] What is a network timeout, and why must every HTTP client configure explicit timeout limits?
- [junior] What is the difference between connect timeout, read timeout, and write timeout in OkHttp?
- [mid] Why do default infinite or excessively long timeout settings leave users hanging during poor cellular connectivity?
- [senior] What goes wrong in production when slow server responses tie up network thread pools due to missing timeout limits?
what-interviewers-listen-for:
- Timeouts prevent network requests from hanging indefinitely when servers or networks stall.
- Connect timeout limits connection establishment; read timeout limits packet arrival intervals.
build-exercise: Configure OkHttp client with explicit connect and read timeouts (`connectTimeout(10, TimeUnit.SECONDS)`), and test a simulated slow server response.
official-references:
- OkHttp timeouts | https://square.github.io/okhttp/features/calls/#timeouts
common-wrong-beliefs:
- Network HTTP clients like OkHttp have infinite timeout limits by default, waiting forever for a server response if not configured | OkHttp enforces default 10-second timeout limits on connect, read, and write operations.

## app-networking/errors-and-retries/cancelling-a-call
interview-questions:
- [intern] Does an in-flight network call stop immediately when the user leaves the screen or cancels an action?
- [junior] How do Kotlin coroutine job cancellations automatically cancel active Ktor or Retrofit network requests?
- [mid] What happens under the hood when a coroutine containing an active network call is cancelled (cancelling the underlying OkHttp `Call`)?
- [senior] What goes wrong in production when leaving orphaned network requests running in the background after UI destruction, wasting server resources and battery?
what-interviewers-listen-for:
- Coroutine cancellation propagates downward, invoking cancellation handlers that call `call.cancel()` on OkHttp.
- Cancelling obsolete network calls saves mobile battery and prevents wasted server processing.
build-exercise: Launch a network request inside a coroutine job, cancel the job immediately after initiation, and verify in OkHttp logs that the network call was aborted.
official-references:
- Cancellation and timeouts in coroutines | https://kotlinlang.org/docs/cancellation-and-timeouts.html
common-wrong-beliefs:
- Leaving a Composable screen while a network request is loading automatically cancels the network request in traditional Java/Callback architectures | Traditional callback-based networking continues running in the background even after screen closure unless explicitly cancelled.

## app-networking/errors-and-retries/retry-with-backoff
interview-questions:
- [intern] How do you retry a failed network call without hammering the server or draining battery?
- [junior] What is exponential backoff (delaying retries by $2^n$ seconds)?
- [mid] How do you implement a retry interceptor or coroutine retry loop with jitter and max attempt caps?
- [senior] What goes wrong in production when thousands of clients retry failed requests simultaneously without jitter, causing a thundering herd server outage?
what-interviewers-listen-for:
- Exponential backoff with random jitter spreads retry attempts over time, preventing server overload (thundering herd problem).
- Max attempt caps prevent infinite retry loops.
build-exercise: Write a coroutine retry function implementing exponential backoff with jitter, simulating network retries.
official-references:
- Exponential backoff pattern | https://cloud.google.com/architecture/retry-exponential-backoff-and-jitter
common-wrong-beliefs:
- Retrying failed network requests immediately in a tight `while` loop is an effective way to recover from network drops | Immediate retries without backoff hammer struggling servers and drain battery rapidly.

## app-networking/auth-tokens/how-apps-prove-who-they-are
interview-questions:
- [intern] What is an authentication token (like a Bearer token), and why do apps send it instead of raw passwords on every request?
- [junior] How do you attach a token to outgoing requests using HTTP headers (`Authorization: Bearer <token>`)?
- [mid] What is JSON Web Token (JWT) structure (Header, Payload, Signature), and what can be read from it on the client side?
- [senior] What goes wrong in production when storing sensitive auth tokens in plaintext SharedPreferences instead of encrypted storage?
what-interviewers-listen-for:
- Tokens provide scoped, time-limited proof of authentication without exposing raw passwords.
- JWT payloads can be decoded on the client to read expiration claims, but signatures must be verified on the server.
build-exercise: Add an OkHttp interceptor that reads a token from secure storage and appends the `Authorization: Bearer` header to every API request.
official-references:
- Token-based authentication | https://jwt.io/introduction
common-wrong-beliefs:
- Reading a JWT payload on the Android client using base64 decoding means the client can verify the cryptographic signature of the token | JWT signatures can only be cryptographically verified by the backend server holding the secret key.

## app-networking/auth-tokens/refreshing-an-expired-token
interview-questions:
- [intern] What should happen when the server responds with an HTTP 401 Unauthorized indicating your auth token has expired?
- [junior] How do you implement a token refresh flow using OkHttp `Authenticator` or an auth interceptor?
- [mid] How do you handle concurrent requests encountering a 401 error so you don't trigger multiple simultaneous refresh calls (token refresh race conditions)?
- [senior] What goes wrong in production when token refresh loops cause infinite recursion or log the user out incorrectly on transient network errors?
what-interviewers-listen-for:
- OkHttp `Authenticator` handles 401 responses by pausing failed requests, requesting a new token via refresh token, and retrying.
- Synchronization locks or mutexes prevent multiple threads from triggering duplicate token refresh requests simultaneously.
build-exercise: Implement an OkHttp `Authenticator` that intercepts 401 responses, calls a refresh token endpoint synchronously, and retries the original request.
official-references:
- OkHttp Authenticator | https://square.github.io/okhttp/features/authentication/
common-wrong-beliefs:
- Handling 401 expired tokens requires navigating the user back to the login screen immediately without attempting a background refresh | Token refresh flows should acquire a new access token via a refresh token in the background seamlessly before forcing a logout.

## app-networking/caching/reusing-saved-answers
interview-questions:
- [intern] When can an app reuse a cached network response instead of asking the backend server again?
- [junior] What are HTTP caching headers (`Cache-Control`, `ETag`, `Last-Modified`)?
- [mid] How do OkHttp cache interceptors configure disk-based HTTP caching (`Cache(directory, size)`)?
- [senior] What goes wrong in production when caching sensitive user account data or mutable financial data incorrectly without cache invalidation?
what-interviewers-listen-for:
- HTTP caching headers tell clients and proxies how long responses remain valid.
- ETag and Last-Modified enable conditional requests (`If-None-Match`), saving bandwidth when data hasn't changed.
build-exercise: Configure OkHttp with a disk cache directory and a cache interceptor, and verify that repeated GET requests return cached responses without hitting the network.
official-references:
- OkHttp caching | https://square.github.io/okhttp/features/caching/
common-wrong-beliefs:
- Network HTTP clients like Retrofit and Ktor cache all API responses automatically on disk by default | HTTP caching requires explicit cache directory configuration and server caching headers (`Cache-Control`).

## app-networking/images/images-from-urls
interview-questions:
- [intern] How does a picture from the internet reach a mobile screen without freezing the UI list during scrolling?
- [junior] How do image loading libraries (Coil, Glide) manage asynchronous decoding, memory caching, and disk caching?
- [mid] Why is loading raw uncompressed bitmap images directly into memory a primary cause of OutOfMemoryError crashes?
- [senior] What goes wrong in production when image caching layers lack memory eviction policies, causing unbounded memory growth?
what-interviewers-listen-for:
- Image loaders fetch, downsample, decode bitmaps asynchronously, and store them in multi-tier caches (memory + disk).
- Uncompressed bitmaps exceed mobile heap limits rapidly.
build-exercise: Load a remote image into a Compose `AsyncImage` (Coil), and inspect memory usage in Android Studio Memory Profiler during rapid list scrolling.
official-references:
- Coil image loading for Compose | https://coil-kt.github.io/coil/compose/
common-wrong-beliefs:
- Downloading and displaying high-resolution 4K images directly from a URL into an ImageView or Compose Image without downsampling is fine | Displaying full-size uncompressed images consumes megabytes of memory per image, quickly triggering OutOfMemoryError crashes.

## app-networking/real-time/live-updates-without-asking
interview-questions:
- [intern] How can a backend server send news or updates to your mobile app without your app asking every few seconds (polling)?
- [junior] What is the difference between polling, Server-Sent Events (SSE), WebSockets, and push notifications (FCM)?
- [mid] When should you choose WebSockets (bidirectional real-time) versus Firebase Cloud Messaging (FCM) for background alerts?
- [senior] What goes wrong in production when maintaining persistent WebSocket connections on mobile devices without handling network drops and reconnection backoff?
what-interviewers-listen-for:
- Polling is inefficient; WebSockets provide persistent bidirectional full-duplex socket communication; FCM delivers background push notifications via OS system services.
- Mobile networks drop persistent socket connections frequently; robust reconnection backoff logic is mandatory.
build-exercise: Connect to a public echo WebSocket server using Ktor WebSockets or OkHttp WebSockets, send a message, and receive the reply.
official-references:
- Ktor WebSockets | https://ktor.io/docs/websocket.html
- Firebase Cloud Messaging | https://firebase.google.com/docs/cloud-messaging
common-wrong-beliefs:
- Polling a server API endpoint every 3 seconds (`while(true) { delay(3000); fetch() }`) is the best way to implement real-time chat updates | Frequent polling wastes battery and bandwidth; WebSockets or push notifications should be used for real-time updates.

## app-networking/sync/keeping-phone-and-server-in-step
interview-questions:
- [intern] What happens when a user edits data offline on their phone, and the server changed the exact same item in the meantime?
- [junior] What are conflict resolution strategies (Last-Write-Wins, client-wins, server-wins, manual merge)?
- [mid] How do offline-first architectures use local write queues and work manager sync workers to reconcile server state?
- [senior] What goes wrong in production when offline-first sync engines lack idempotent API endpoints, causing duplicate financial transactions on retry?
what-interviewers-listen-for:
- Concurrent offline edits create synchronization conflicts requiring deterministic resolution rules.
- Idempotent API design ensures repeated retry requests produce the same result without duplicating actions.
build-exercise: Design a simple offline write queue that saves pending mutations to a local database and flushes them via WorkManager when connectivity returns.
official-references:
- Guide to app architecture (Offline-first) | https://developer.android.com/topic/architecture#offline-first
common-wrong-beliefs:
- Building an offline-first app simply means catching network exceptions and showing an "Offline" toast message to the user | True offline-first architecture requires local persistence as the single source of truth and robust background synchronization queues.

## Self-check
1. Android connectivity overview | https://developer.android.com/training/volley/request-counter (Verify URL path)
2. Permissions overview | https://developer.android.com/guide/topics/permissions/overview (Verify path)
3. Retrofit documentation | https://square.github.io/retrofit/ (Verify URL)
4. OkHttp Interceptors | https://square.github.io/okhttp/features/interceptors/ (Verify path)
5. Kotlinx Serialization guide | https://kotlinlang.org/docs/serialization.html (Verify path)
6. OkHttp timeouts | https://square.github.io/okhttp/features/calls/#timeouts (Verify anchor)
7. OkHttp Authenticator | https://square.github.io/okhttp/features/authentication/ (Verify path)
8. Coil image loading for Compose | https://coil-kt.github.io/coil/compose/ (Verify path)
9. Ktor WebSockets | https://ktor.io/docs/websocket.html (Verify path)
10. Guide to app architecture (Offline-first) | https://developer.android.com/topic/architecture#offline-first (Verify anchor)