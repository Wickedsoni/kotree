# interview: app-networking

## app-networking/requests-from-an-app/what-an-app-sends-and-gets
interview-questions:
- [intern] When your app makes an HTTP call to an API, what pieces make up the request and what pieces come back in the response?
- [junior] Why do mobile APIs typically exchange JSON payloads over HTTPS rather than HTML?
- [mid] What is the trade-off between sending data in URL query parameters, request headers, and the request body?
- [senior] What goes wrong in production when an app sends sensitive tokens or PII in URL query parameters instead of headers or the body?
what-interviewers-listen-for:
- Request (method, URL, headers, optional body) and Response (status code, headers, body).
- URL query parameters are logged in proxy logs, CDN access logs, and crash breadcrumbs, making them unsafe for secrets or PII.
build-exercise: Inspect a public JSON API call in Android Studio's App Inspection Network Inspector and examine the exact request headers, response headers, status code, and raw payload bytes.
official-references:
- Connect to the network | https://developer.android.com/develop/connectivity/network-ops/connecting
- Inspect network traffic with the Network Inspector | https://developer.android.com/studio/debug/network-profiler
common-wrong-beliefs:
- HTTPS hides URL query parameters from the server's access logs and CDNs | While TLS encrypts the URL in transit, the full path and query string are routinely written in plain text to server and proxy logs.

## app-networking/requests-from-an-app/why-my-first-call-fails
interview-questions:
- [intern] What two common exceptions happen on a beginner's very first network call in Android (`SecurityException` and `NetworkOnMainThreadException`)?
- [junior] Why does calling `http://` (cleartext) fail by default on modern Android versions?
- [mid] Why does `localhost` (`127.0.0.1`) inside an Android emulator fail to reach a server running on your development laptop?
- [senior] What goes wrong in production when a developer fixes a cleartext error by setting `android:usesCleartextTraffic="true"` globally in the manifest?
what-interviewers-listen-for:
- Missing `INTERNET` permission (`SecurityException`), calling on the main thread (`NetworkOnMainThreadException`), or cleartext HTTP blocked (`CleartextHttpTrafficNotPermittedException`, Android 9+).
- The emulator runs in its own virtual router space (`127.0.0.1` is the emulator itself; `10.0.2.2` routes to the host machine loopback).
build-exercise: Make a network call without `<uses-permission android:name="android.permission.INTERNET" />`, then on the main thread, then to an `http://` URL, and observe each distinct exception in Logcat.
official-references:
- Connect to the network | https://developer.android.com/develop/connectivity/network-ops/connecting
- Set up Android Emulator networking | https://developer.android.com/studio/run/emulator-networking
common-wrong-beliefs:
- `INTERNET` is a runtime permission that shows a user popup dialog | `android.permission.INTERNET` is a normal install-time permission granted automatically when declared in `AndroidManifest.xml`.

## app-networking/http-client/first-get-call
interview-questions:
- [intern] How do you define a `suspend` GET endpoint using Retrofit or Ktor Client in Kotlin?
- [junior] Why should a single `OkHttpClient` / `Retrofit` / `HttpClient` instance be shared across the app instead of creating a new one per request?
- [mid] How do Retrofit/OkHttp and Ktor integrate with Kotlin coroutines under the hood so a `suspend` network call doesn't block the calling thread?
- [senior] What goes wrong in production if a new `OkHttpClient` is instantiated on every API call?
what-interviewers-listen-for:
- Sharing a single client instance preserves the TCP/TLS connection pool, thread pool dispatcher, and response cache.
- Creating a client per request leaks thread pools, breaks HTTP/2 multiplexing, forces a full TLS handshake on every call, and risks `OutOfMemoryError`.
build-exercise: Write a single `@GET` endpoint with Retrofit (or Ktor `client.get`), call it 5 times using a shared client vs a newly created client each time, and compare connection setup time in Network Inspector.
official-references:
- Connect to the network | https://developer.android.com/develop/connectivity/network-ops/connecting
- Creating and configuring a client | https://ktor.io/docs/client-create-and-configure.html
common-wrong-beliefs:
- Calling a `suspend` function on a Retrofit interface requires wrapping it in `withContext(Dispatchers.IO)` to avoid `NetworkOnMainThreadException` | Retrofit's suspend support uses OkHttp's asynchronous `enqueue` callback under the hood and is main-safe out of the box.

## app-networking/http-client/building-requests
interview-questions:
- [intern] What is the difference between `@Path`, `@Query`, and `@Body` in a Retrofit request (or Ktor URL builder)?
- [junior] Why must path and query parameters be URL-encoded before being sent over HTTP?
- [mid] When should an API request use `application/json` versus `multipart/form-data` or `application/x-www-form-urlencoded`?
- [senior] What goes wrong in production when an app builds a request URL using string concatenation (`"users/$input"`) instead of proper path/query builders?
what-interviewers-listen-for:
- `@Path` replaces URL segments, `@Query` appends `?key=value`, and `@Body` serializes the payload stream.
- Manual string concatenation with user input allows spaces, `&`, `#`, or `../` path-traversal characters to corrupt the URL or hit unintended endpoints.
build-exercise: Pass a search query containing spaces and `&` (e.g., `"rock & roll"`) through `@Query` (or Ktor `parameter()`) and inspect the percent-encoded URL (`rock%20%26%20roll`) in Network Inspector.
official-references:
- Making requests | https://ktor.io/docs/client-requests.html
common-wrong-beliefs:
- A GET request should carry a JSON body for complex search filters | HTTP GET bodies have undefined semantics and are dropped or rejected by many proxies and CDNs; use query parameters or a POST search endpoint instead.

## app-networking/http-client/interceptors-and-logging
interview-questions:
- [intern] What is an OkHttp Interceptor (or Ktor plugin) and why use it instead of passing headers to every function?
- [junior] How does `chain.proceed(request)` work inside an OkHttp Interceptor?
- [mid] What is the difference between an Application Interceptor (`addInterceptor`) and a Network Interceptor (`addNetworkInterceptor`) in OkHttp?
- [senior] What goes wrong in production if `HttpLoggingInterceptor.Level.BODY` is left enabled in release builds or when streaming large files?
what-interviewers-listen-for:
- Interceptors form a middleware chain around `chain.proceed(request)` that can inspect, rewrite, or retry requests and responses centrally.
- `Level.BODY` reads the entire response stream into memory (causing OOMs on large downloads) and leaks auth tokens and PII into system Logcat on release devices.
build-exercise: Write an OkHttp Interceptor that adds an `X-Request-Id` header and measures elapsed time around `chain.proceed(request)`. Verify the header appears on every request in Network Inspector.
official-references:
- Inspect network traffic with the Network Inspector | https://developer.android.com/studio/debug/network-profiler
common-wrong-beliefs:
- `addInterceptor` and `addNetworkInterceptor` run at the exact same times | Application interceptors run once per logical call (even if served from cache), whereas network interceptors run for every physical network redirect and retry.

## app-networking/json/json-to-kotlin-objects
interview-questions:
- [intern] How does `kotlinx.serialization` (or Moshi) convert a JSON string into a Kotlin data class?
- [junior] Why should you use `@SerialName("snake_case")` instead of naming your Kotlin property `val user_id: String`?
- [mid] Why is compile-time code generation (`kotlinx.serialization` / Moshi KSP) preferred over runtime reflection (`Gson`) in Kotlin Android apps?
- [senior] What goes wrong in production when reflection-based `Gson` deserializes a missing JSON field into a non-null Kotlin property?
what-interviewers-listen-for:
- Compile-time serializers generate type-safe adapter code without reflection overhead and enforce Kotlin null-safety contracts and default values.
- `Gson` uses `Unsafe` reflection on JVM, bypassing Kotlin constructors and silently injecting `null` into non-null `val name: String` properties, causing delayed `NullPointerException`s when read later.
build-exercise: Parse `{}` into `data class User(val name: String = "Guest")` using `kotlinx.serialization` vs reflection, and observe how `kotlinx.serialization` respects Kotlin default values and non-null guarantees.
official-references:
- Kotlin serialization | https://kotlinlang.org/docs/serialization.html
common-wrong-beliefs:
- Kotlin's non-null types (`String`) cannot hold `null` at runtime after JSON parsing | Reflection-based Java parsers like Gson bypass Kotlin constructors and can assign `null` into a non-null Kotlin field unless a Kotlin-aware serializer is used.

## app-networking/json/when-json-does-not-match
interview-questions:
- [intern] What happens by default if the server adds a new JSON field that your Kotlin `@Serializable` class does not know about?
- [junior] How do `ignoreUnknownKeys = true` and `coerceInputValues = true` in `kotlinx.serialization` protect your app from backend changes?
- [mid] How should you deserialize an enum field when the server might add a new enum value in the future before old app versions update?
- [senior] What goes wrong in production when a single malformed item in a 50-item JSON array causes the entire screen feed to fail parsing?
what-interviewers-listen-for:
- Forward compatibility: mobile app versions stay in the wild for months, so JSON parsers must ignore unknown keys and provide fallback unknown enum values.
- Without `ignoreUnknownKeys = true` and fallback enum defaults, a harmless backend field addition immediately breaks existing production app releases.
build-exercise: Configure `Json { ignoreUnknownKeys = true; coerceInputValues = true }` and test parsing JSON with extra unknown fields, explicit `null` for a defaulted property, and an unrecognized enum string.
official-references:
- Kotlin serialization | https://kotlinlang.org/docs/serialization.html
common-wrong-beliefs:
- You only need to update the app when the backend adds a field to a JSON response | Old app versions stay installed on users' phones, so parsers must be configured to ignore unknown fields from day one.

## app-networking/errors-and-retries/kinds-of-failure
interview-questions:
- [intern] What are the three main ways a network call can fail (transport `IOException`, HTTP 4xx/5xx status, and serialization/parsing error)?
- [junior] Why should your UI treat an `IOException` (no internet) differently from a `401 Unauthorized` or a `SerializationException`?
- [mid] What is the trade-off between throwing exceptions from a repository versus returning a sealed `NetworkResult<T>` / `Either` type?
- [senior] What goes wrong in production when a catch-all `catch (e: Exception)` maps every error to "Check your internet connection" and accidentally catches `CancellationException`?
what-interviewers-listen-for:
- Distinguishing transport errors (`IOException`), HTTP protocol errors (`4xx` client vs `5xx` server), and schema contract mismatches (`SerializationException`).
- Catching `Exception` broadly hides backend schema bugs behind "Offline" messages and breaks coroutine structured concurrency if `CancellationException` is swallowed.
build-exercise: Model a sealed interface `ApiError { object Offline; data class Http(val code: Int); object SchemaMismatch }` and write a wrapper that maps exceptions (rethrowing `CancellationException`) to each branch.
official-references:
- Connect to the network | https://developer.android.com/develop/connectivity/network-ops/connecting
- Response validation | https://ktor.io/docs/client-response-validation.html
common-wrong-beliefs:
- Every network failure means the user has no internet connection | Server 500s, expired tokens (401), and JSON parsing crashes happen on fast Wi-Fi and require completely different handling.

## app-networking/errors-and-retries/timeouts
interview-questions:
- [intern] Why does an HTTP client need a timeout instead of waiting forever?
- [junior] What is the difference between a connect timeout, a read timeout, a write timeout, and a call timeout?
- [mid] How can a request with a 10-second connect timeout and a 10-second read timeout still take 5 minutes before failing if no overall call timeout is set?
- [senior] What goes wrong in production when file-upload endpoints share the same short timeout as lightweight JSON endpoints?
what-interviewers-listen-for:
- Connect timeout bounds TCP handshake; read/write timeouts bound inactivity between individual socket reads/writes; call timeout bounds the entire end-to-end request lifecycle.
- A "tarpit" or ultra-slow connection dripping 1 byte every 9 seconds never triggers a 10-second read timeout unless an overall call timeout is configured.
build-exercise: Configure an HTTP client with a 2-second read/request timeout, call a test endpoint that delays for 5 seconds (or simulate high latency in Emulator Network settings), and catch the timeout exception.
official-references:
- Timeout | https://ktor.io/docs/client-timeout.html
common-wrong-beliefs:
- Setting `readTimeout(10, SECONDS)` guarantees the HTTP call will finish or fail within 10 seconds | `readTimeout` resets every time a single byte arrives from the socket; only `callTimeout` (or Ktor `requestTimeoutMillis`) bounds total elapsed time.

## app-networking/errors-and-retries/cancelling-a-call
interview-questions:
- [intern] What happens to an in-flight `suspend` network call launched in `viewModelScope` when the user closes the screen?
- [junior] How does coroutine cancellation propagate down into OkHttp (`Call.cancel()`) or Ktor to close the underlying network request?
- [mid] Why should a GET request for screen data be cancelled when leaving the screen, whereas a POST/checkout mutation might need an application or WorkManager scope?
- [senior] What goes wrong in production if a POST request that is not idempotent is cancelled mid-flight on screen close and the user taps "Submit" again?
what-interviewers-listen-for:
- `suspendCancellableCoroutine` registers an `invokeOnCancellation` handler that calls `Call.cancel()` on the HTTP client when the coroutine scope cancels.
- Client-side cancellation closes the socket locally, but the server may have already received and processed a non-idempotent POST request.
build-exercise: Start a slow 5-second GET request in `viewModelScope`, navigate back after 1 second, and inspect Logcat and Network Inspector to verify the coroutine cancels with `CancellationException` and aborts the call.
official-references:
- Coroutines guide | https://kotlinlang.org/docs/coroutines-guide.html
- Cancellation and timeouts | https://kotlinlang.org/docs/cancellation-and-timeouts.html
common-wrong-beliefs:
- Cancelling an HTTP call on the phone cancels the work on the server | Cancelling only aborts waiting for the response on the device; the server may still finish executing the request.

## app-networking/errors-and-retries/retry-with-backoff
interview-questions:
- [intern] Why is immediately retrying a failed request 5 times in a `while` loop dangerous?
- [junior] How does exponential backoff with random jitter calculate the delay between retries?
- [mid] Which HTTP methods and status codes are safe to retry automatically (`GET`, `503`, `429`), and why is retrying `POST` without an idempotency key risky?
- [senior] What goes wrong in production ("thundering herd") when 100,000 clients come back online after a 30-second server outage and retry on identical fixed intervals?
what-interviewers-listen-for:
- Exponential backoff ($base \times 2^{attempt}$) plus random jitter spreads retry spikes over time so recovering servers are not DDoSed by their own clients.
- Only idempotent operations (`GET`, `PUT`, `DELETE`, or `POST` with an `Idempotency-Key` header) and transient failures (`IOException`, `502`/`503`/`504`, `429` respecting `Retry-After`) should be auto-retried.
build-exercise: Write a suspend `retryWithBackoff(maxRetries = 3)` helper that doubles delay and adds random jitter (`Random.nextLong(0, 500)`), and test it against a fake API that fails twice with 503 before succeeding.
official-references:
- Retrying requests | https://ktor.io/docs/client-retry.html
common-wrong-beliefs:
- Exponential backoff alone prevents server overload after an outage | Without random jitter, clients that failed at the same second will still retry in synchronized waves at 2s, 4s, and 8s.

## app-networking/auth-tokens/how-apps-prove-who-they-are
interview-questions:
- [intern] What is an access token (like a Bearer token), and how does an app attach it to API requests?
- [junior] Why do apps store and send a short-lived token instead of storing and sending the user's raw password on every call?
- [mid] What is the difference between a short-lived access token and a long-lived refresh token?
- [senior] What goes wrong in production if an app attaches its `Authorization: Bearer <token>` header to every outgoing URL, including third-party analytics or CDN domains?
what-interviewers-listen-for:
- Access tokens are scoped, revocable, and short-lived (`Authorization: Bearer <token>`), limiting blast radius compared to raw credentials.
- Auth interceptors must verify the request host before attaching the `Authorization` header so tokens are never leaked to third-party domains.
build-exercise: Build an auth interceptor that reads a token from an in-memory provider and attaches `Authorization: Bearer <token>` only when `request.url.host == "api.example.com"`.
official-references:
- Authentication and authorization in Ktor Client | https://ktor.io/docs/client-auth.html
common-wrong-beliefs:
- Decoding a JWT on the client proves the token is valid and authentic | Only the backend holding the verification key can validate a JWT signature; client decoding is only for reading non-trusted metadata claims.

## app-networking/auth-tokens/refreshing-an-expired-token
interview-questions:
- [intern] What HTTP status code tells the app that its access token has expired?
- [junior] How do an access token and a refresh token work together so the user doesn't have to log in every hour?
- [mid] Why do you use an OkHttp `Authenticator` (or Ktor `Auth` plugin `refreshTokens` block) instead of checking token expiry manually in every ViewModel?
- [senior] What goes wrong in production when 4 concurrent requests on a screen all receive `401 Unauthorized` at the same time and your refresh logic is not synchronized with a `Mutex` or lock?
what-interviewers-listen-for:
- Central `401` handling transparently calls the refresh endpoint and replays the original failed request.
- If 4 parallel requests hit `401` simultaneously without a lock (`synchronized` or `Mutex`) and double-checked token comparison, 4 refresh calls fire in parallel—and with rotating single-use refresh tokens, the second call invalidates the user's entire session.
build-exercise: Simulate 3 parallel requests failing with `401`. Guard the refresh call with a `Mutex` and compare the failed request's Bearer token with the currently stored token so only 1 refresh network call runs.
official-references:
- Authentication and authorization in Ktor Client | https://ktor.io/docs/client-auth.html
common-wrong-beliefs:
- Checking the local device clock against a JWT's `exp` timestamp eliminates `401` responses | Users' device clocks can be skewed and servers can revoke tokens early, so reactive `401` refresh handling is always required.

## app-networking/caching/reusing-saved-answers
interview-questions:
- [intern] Why should an app cache server responses instead of fetching them over the network every time?
- [junior] How do HTTP headers like `Cache-Control: max-age`, `ETag`, and `304 Not Modified` let an HTTP client reuse responses automatically?
- [mid] What is the trade-off between relying on an HTTP disk cache (OkHttp `Cache`) versus persisting structured data in a Room database?
- [senior] What goes wrong in production when user-specific HTTP cached responses are not cleared on logout?
what-interviewers-listen-for:
- `Cache-Control: max-age` skips the network entirely while fresh; `ETag` / `If-None-Match` performs a cheap conditional validation returning an empty `304 Not Modified`.
- HTTP cache stores raw responses keyed by URL (great for static/GET endpoints), while Room supports relational queries, offline mutations, and reactive UI Flows.
build-exercise: Configure an OkHttp `Cache(File(context.cacheDir, "http_cache"), 10L * 1024 * 1024)` and inspect two consecutive calls to an endpoint with `Cache-Control` or `ETag` headers using `response.cacheResponse` and `response.networkResponse`.
official-references:
- Caching | https://ktor.io/docs/client-caching.html
- Reduce network battery drain | https://developer.android.com/develop/connectivity/network-ops/reading-network-state
common-wrong-beliefs:
- Setting up an HTTP cache object automatically caches every GET response | An HTTP client cache obeys standard HTTP headers (`Cache-Control`, `ETag`) sent by the server unless overridden.

## app-networking/images/images-from-urls
interview-questions:
- [intern] Why shouldn't you download a bitmap from a URL inside a Composable and pass the full-resolution image straight to `Image()`?
- [junior] What three caches and optimizations do libraries like Coil perform behind `AsyncImage`?
- [mid] Why is downsampling (subsampling) an image to the exact target view pixel size critical on mobile devices?
- [senior] What goes wrong in production when a `LazyColumn` decodes 4000x3000 pixel camera photos into `ARGB_8888` bitmaps without downsampling?
what-interviewers-listen-for:
- Image loaders manage in-memory `MemoryCache`, disk cache, coroutine request cancellation on scroll, and bitmap downsampling to target layout dimensions.
- A single 4000x3000 `ARGB_8888` bitmap consumes $4000 \times 3000 \times 4 \approx 48\text{ MB}$ of uncompressed RAM regardless of how small the compressed JPEG file was on the network.
build-exercise: Render a `LazyColumn` of 50 high-resolution image URLs using Coil's `AsyncImage`, and watch the Memory Profiler and Network Inspector while scrolling fast up and down.
official-references:
- Loading images | https://developer.android.com/develop/ui/compose/graphics/images/loading
common-wrong-beliefs:
- A 200 KB JPEG file on a server takes 200 KB of RAM when displayed on screen | Once decoded into an `ARGB_8888` Bitmap in memory, size is `width * height * 4 bytes` (often tens of megabytes) unless downsampled.

## app-networking/real-time/live-updates-without-asking
interview-questions:
- [intern] Why is polling a server every 2 seconds (`while(true) { delay(2000); fetch() }`) bad for mobile apps?
- [junior] What are the differences between HTTP polling, Server-Sent Events (SSE), WebSockets, and Firebase Cloud Messaging (FCM)?
- [mid] When should an Android app use a WebSocket/SSE stream versus FCM push notifications?
- [senior] What goes wrong in production when an app tries to keep a WebSocket open 24/7 in the background instead of closing it on `STOPPED` and using FCM when backgrounded?
what-interviewers-listen-for:
- Frequent polling prevents the cellular radio from entering low-power sleep states, draining battery rapidly.
- Use WebSockets/SSE encapsulated in a `callbackFlow` only while the screen/app is in the foreground (`STARTED`); use FCM for waking or notifying the app when backgrounded.
build-exercise: Wrap a WebSocket or SSE listener inside a `callbackFlow` with `awaitClose { socket.close() }`, collect it with `collectAsStateWithLifecycle`, and verify the connection closes automatically when the app goes to the background.
official-references:
- WebSockets | https://ktor.io/docs/client-websockets.html
- Reduce network battery drain | https://developer.android.com/develop/connectivity/network-ops/reading-network-state
common-wrong-beliefs:
- Apps should keep a background WebSocket open at all times to receive chat messages | Android kills background connections and processes; production apps use WebSockets in the foreground and FCM push notifications in the background.

## app-networking/sync/keeping-phone-and-server-in-step
interview-questions:
- [intern] What is a sync conflict when a user edits an item offline on their phone while someone else edits the same item on the server?
- [junior] How does an outbox table (pending mutations queue) in Room plus WorkManager ensure offline edits eventually reach the server?
- [mid] What is the trade-off between "Last-Write-Wins" (LWW) conflict resolution versus version vectors / optimistic concurrency (`ETag` / `If-Match` / `version` field)?
- [senior] What goes wrong in production when an app uses the phone's local `System.currentTimeMillis()` to decide "Last-Write-Wins" across devices?
what-interviewers-listen-for:
- Offline-first sync separates reading (delta pull via cursor/server timestamp) from writing (local Room update + outbox table flushed by `WorkManager`).
- Device wall clocks drift or can be manually changed by users, so client timestamps corrupt Last-Write-Wins ordering; servers must assign authoritative timestamps or monotonic version numbers (`409 Conflict`).
build-exercise: Create a Room entity with `val isSynced: Boolean` and `val version: Int`. Save an edit while in Airplane mode (`isSynced = false`), schedule a `WorkManager` sync job with a network constraint, and watch it sync when Airplane mode is turned off.
official-references:
- Build an offline-first app | https://developer.android.com/topic/architecture/data-layer/offline-first
common-wrong-beliefs:
- Client `System.currentTimeMillis()` can be trusted to order edits across different devices | User device clocks are frequently out of sync by seconds, minutes, or time zones; only server-assigned versions or monotonic tokens are reliable.

## Self-check
- CleartextHttpTrafficNotPermittedException (OkHttp/platform exception naming)
- Inspect network traffic with the Network Inspector | https://developer.android.com/studio/debug/network-profiler
- Set up Android Emulator networking | https://developer.android.com/studio/run/emulator-networking
- Creating and configuring a client | https://ktor.io/docs/client-create-and-configure.html
- Making requests | https://ktor.io/docs/client-requests.html
- Response validation | https://ktor.io/docs/client-response-validation.html
- Timeout | https://ktor.io/docs/client-timeout.html
- Retrying requests | https://ktor.io/docs/client-retry.html
- Authentication and authorization in Ktor Client | https://ktor.io/docs/client-auth.html
- Caching | https://ktor.io/docs/client-caching.html