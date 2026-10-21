# interview: networking

## networking/how-the-internet-works/clients-and-servers
interview-questions:
- [intern] When your mobile app requests data, which computer acts as the client and which computer acts as the server?
- [junior] What is the request-response cycle in client-server architecture?
- [mid] Why do modern mobile architectures decouple the client interface from server state storage?
- [senior] What goes wrong in production when mobile clients assume persistent server connection availability without fallback caching?
what-interviewers-listen-for:
- The client initiates requests (asking); the server processes requests and returns data (answering).
- Separation of concerns allows independent scaling of backend infrastructure and client UI.
- Offline-first resilience requires handling server unreachability gracefully.
build-exercise: Write a simple HTTP client request using Ktor or Retrofit to fetch a JSON payload from a public API endpoint, and observe the client-server request-response exchange.
official-references:
- Android connectivity overview | https://developer.android.com/training/volley/request-counter
common-wrong-beliefs:
- The mobile phone app operates as an independent self-contained node that only talks to the internet when syncing explicitly | Mobile apps act strictly as clients dependent on remote servers for dynamic data storage and business logic processing.

## networking/how-the-internet-works/how-data-travels-in-packets
interview-questions:
- [intern] Why does the internet break large messages into smaller chunks called packets rather than sending entire files in one massive stream?
- [junior] What metadata is included inside an internet packet header (source IP, destination IP, sequence number)?
- [mid] How do packet fragmentation and reassembly work across varying network routers?
- [senior] What goes wrong in production when packet loss or MTU size mismatches cause packet fragmentation overhead and connection drops?
what-interviewers-listen-for:
- Packet switching enables efficient multiplexing and shared utilization of network bandwidth across multiple users.
- Packets can travel along different physical router paths and reassemble at the destination.
- Maximum Transmission Unit (MTU) mismatches cause packet fragmentation, degrading throughput.
build-exercise: Use a network inspection tool or Wireshark (or ADB network dump) to observe individual TCP/IP packet headers flowing during an API request.
official-references:
- MDN How the web works | https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/How_the_web_works
common-wrong-beliefs:
- Data sent across the internet flows as a single continuous unbroken stream of bytes from your phone to the server | Data is broken down into discrete packets routed independently and reassembled at the receiving end.

## networking/layers/why-networks-use-layers
interview-questions:
- [intern] Why does a network message pass through several abstraction layers (OSI or TCP/IP model) before leaving your phone?
- [junior] What is the role of each layer in the network stack (Application, Transport, Network, Link)?
- [mid] How does encapsulation and decapsulation work as data moves down and up the network layers?
- [senior] What goes wrong in production when debugging network faults without understanding which OSI layer is failing (DNS vs TLS vs TCP)?
what-interviewers-listen-for:
- Layering provides modularity and abstraction; changes in physical hardware do not break application protocols.
- Encapsulation wraps data with headers specific to each layer (HTTP -> TLS -> TCP -> IP -> Ethernet).
build-exercise: Trace an API request in your head through Application (HTTP), Transport (TCP), Network (IP), and Link layers to understand header wrapping.
official-references:
- MDN HTTP overview | https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview
common-wrong-beliefs:
- Network requests are sent directly from application code into physical radio waves without passing through intermediary abstractions | Application requests pass through strict protocol layers, each adding its own encapsulation headers.

## networking/ip-and-dns/ip-addresses
interview-questions:
- [intern] What does an IP address tell the network about your mobile phone or server?
- [junior] What is the difference between IPv4 and IPv6 addresses, and why was IPv6 introduced?
- [mid] How do private IP addresses behind Network Address Translation (NAT) communicate with public servers?
- [senior] What goes wrong in production when mobile apps encounter IPv6-only cellular networks while communicating with legacy IPv4-only backend servers?
what-interviewers-listen-for:
- IP addresses uniquely identify devices on a network for routing packets.
- IPv4 exhaustion necessitated IPv6 128-bit address spaces.
- NAT maps private local IPs to public IP addresses at the router boundary.
build-exercise: Check your device's current local IP address and gateway configuration in device settings or via ADB shell (`ifconfig` / `ip addr`).
official-references:
- MDN IP address definition | https://developer.mozilla.org/en-US/docs/Glossary/IP_address
common-wrong-beliefs:
- Your mobile phone retains the exact same public IP address permanently wherever you go | Mobile devices change IP addresses constantly as they switch between Wi-Fi networks and cellular carrier towers.

## networking/ip-and-dns/how-packets-find-a-route
interview-questions:
- [intern] How does a packet find its way across dozens of routers scattered across the globe to reach its destination server?
- [junior] What is a routing table, and how do routers determine the next hop for a packet?
- [mid] What is the difference between interior routing protocols and Border Gateway Protocol (BGP) on the global internet?
- [senior] What goes wrong in production when BGP route leaks or DNS routing misconfigurations cause regional connection blackouts?
what-interviewers-listen-for:
- Routers inspect packet destination IP headers and consult routing tables to forward packets toward the next hop.
- Dynamic routing protocols adapt around congested or failed links.
build-exercise: Run a traceroute command via terminal (`traceroute` or `tracereoute` equivalent) to observe individual router hops between your machine and a remote server.
official-references:
- MDN Routing basics | https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/How_does_the_internet_work
common-wrong-beliefs:
- Every packet sent from your phone follows the exact same static pre-determined path to the server | Packets take dynamic individual hops across routers based on real-time network congestion and availability.

## networking/ip-and-dns/dns
interview-questions:
- [intern] How does a human-readable domain name like `example.com` get converted into a machine IP address?
- [junior] What is the DNS resolution hierarchy (Browser cache, OS cache, Recursive Resolver, Root servers, TLD servers, Authoritative nameservers)?
- [mid] What is DNS caching, and why is setting appropriate TTL (Time To Live) values crucial for backend server migration?
- [senior] What goes wrong in production when stale DNS caches prevent mobile apps from resolving migrated backend server IPs during outages?
what-interviewers-listen-for:
- DNS translates domain names to IP addresses.
- Caching at multiple levels speeds up lookups, but low TTLs are required during server failovers to prevent connection failures.
build-exercise: Use a network utility (`nslookup` or `dig`) in terminal to query DNS records for a domain and check its TTL value.
official-references:
- MDN What is DNS | https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_DNS
common-wrong-beliefs:
- Your app queries the root DNS servers directly every single time you make an API request to a domain | DNS queries are heavily cached by the OS, local routers, and recursive ISPs to avoid root lookups on every request.

## networking/tcp-and-udp/ports-and-sockets
interview-questions:
- [intern] How does data reach the correct application on your phone when multiple apps are using the internet simultaneously?
- [junior] What is a network port, and what distinguishes well-known ports (like 80 for HTTP and 443 for HTTPS) from ephemeral ports?
- [mid] What is a network socket (IP address + port + protocol)?
- [senior] What goes wrong in production when exhausting available ephemeral client ports under high-frequency socket connection churn?
what-interviewers-listen-for:
- Ports multiplex traffic on a single IP address, routing data streams to specific application sockets.
- Ephemeral ports are dynamically allocated by the OS for outgoing client connections.
build-exercise: Inspect active network sockets and listening ports on your development machine using terminal commands (`netstat` or `ss`).
official-references:
- MDN Port definition | https://developer.mozilla.org/en-US/docs/Glossary/Port
common-wrong-beliefs:
- Your phone needs a separate unique public IP address for every single app running in the background | Ports allow multiple apps to share a single IP address by multiplexing traffic over distinct port numbers.

## networking/tcp-and-udp/tcp-reliable-delivery
interview-questions:
- [intern] How does TCP make an unreliable packet network feel reliable and ordered?
- [junior] What is the TCP 3-way handshake (SYN, SYN-ACK, ACK), and how does it establish a connection?
- [mid] How do acknowledgment numbers, sequence numbers, and retransmission mechanisms handle packet loss?
- [senior] What goes wrong in production when TCP head-of-line blocking stalls all data streams over a single dropped packet on flaky mobile networks?
what-interviewers-listen-for:
- TCP guarantees reliable, ordered, error-checked delivery via handshakes, acknowledgments, and retransmissions.
- Head-of-line blocking: if one TCP packet is dropped, subsequent packets wait in the buffer until it is retransmitted.
build-exercise: Review a TCP handshake sequence diagram and simulate how a dropped packet triggers a TCP retransmission timeout.
official-references:
- MDN TCP protocol guide | https://developer.mozilla.org/en-US/docs/Glossary/TCP
common-wrong-beliefs:
- TCP guarantees instant zero-latency delivery of packets across mobile networks | TCP guarantees reliability and order, but packet drops require retransmissions, adding latency on poor signals.

## networking/tcp-and-udp/udp-speed-over-reliability
interview-questions:
- [intern] Why do live video calls and audio streaming apps use UDP instead of TCP?
- [junior] What is the fundamental difference in trade-offs between TCP (reliable, ordered) and UDP (fast, connectionless, unverified)?
- [mid] How do modern transport protocols like QUIC (built on UDP) solve TCP's head-of-line blocking problem?
- [senior] What goes wrong in production when using UDP for data payloads that require strict integrity without implementing custom app-layer acknowledgment checks?
what-interviewers-listen-for:
- UDP drops lost packets rather than retransmitting them, prioritizing real-time speed over absolute reliability.
- QUIC/HTTP3 multiplexes streams over UDP to eliminate TCP head-of-line blocking.
build-exercise: Compare connection setup latency between a TCP socket connection and a UDP datagram send in a test script.
official-references:
- MDN UDP definition | https://developer.mozilla.org/en-US/docs/Glossary/UDP
common-wrong-beliefs:
- UDP is completely obsolete and never used in modern mobile applications | UDP powers DNS lookups, real-time gaming, WebRTC video calls, and HTTP/3 (QUIC).

## networking/http/request-and-response
interview-questions:
- [intern] What do an HTTP request and an HTTP response look like raw on the wire?
- [junior] What are the structural sections of an HTTP message (start line, headers, blank line, body)?
- [mid] How do content-type headers (`application/json`, `multipart/form-data`) dictate how the receiver parses the body?
- [senior] What goes wrong in production when missing or incorrect Content-Type headers cause servers to reject payloads as malformed?
what-interviewers-listen-for:
- HTTP messages consist of a start line, key-value headers, a separator newline, and an optional body payload.
- Content-Type headers inform parsers how to interpret the byte stream.
build-exercise: Capture raw HTTP request headers and body payload using Android Studio Network Profiler during an API call.
official-references:
- MDN HTTP messages | https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages
common-wrong-beliefs:
- HTTP requests and responses are binary data structures passed directly between application memory spaces | HTTP messages are structured text protocols transmitted over TCP byte streams.

## networking/http/http-methods
interview-questions:
- [intern] Which HTTP methods are safe and idempotent, and why does that distinction matter?
- [junior] What is the difference between GET, POST, PUT, PATCH, and DELETE?
- [mid] What does idempotency mean (e.g., executing PUT or DELETE multiple times yields the exact same state), and why is POST not idempotent?
- [senior] What goes wrong in production when mobile apps retry non-idempotent POST requests blindly on network drops, duplicating financial transactions?
what-interviewers-listen-for:
- Safe methods (GET) do not modify server state; idempotent methods (PUT, DELETE) can be repeated multiple times with identical results.
- POST is neither safe nor idempotent; retrying POST requests risks duplicate resource creation unless using idempotency keys.
build-exercise: Design an API client call for a financial transaction using an idempotency key header to ensure safe POST retries.
official-references:
- MDN HTTP request methods | https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
common-wrong-beliefs:
- You can safely retry any failed HTTP request method (including POST) automatically whenever a network timeout occurs | Retrying non-idempotent POST requests without idempotency keys creates duplicate charges or records on the server.

## networking/http/status-codes
interview-questions:
- [intern] What do the number ranges in HTTP status codes signify (1xx, 2xx, 3xx, 4xx, 5xx)?
- [junior] What is the difference between 401 Unauthorized and 403 Forbidden?
- [mid] When should an app handle a 404 Not Found versus a 503 Service Unavailable?
- [senior] What goes wrong in production when mobile apps misinterpret 5xx server errors as permanent client-side validation failures?
what-interviewers-listen-for:
- 2xx Success, 3xx Redirection, 4xx Client Errors, 5xx Server Errors.
- 401 means unauthenticated (missing/expired token); 403 means authenticated but lacking permission.
build-exercise: Implement an HTTP response status handler in Kotlin that branches UI error states based on 401, 404, and 500 status codes.
official-references:
- MDN HTTP response status codes | https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
common-wrong-beliefs:
- All HTTP status codes in the 4xx range mean the server is broken and experiencing an outage | 4xx status codes indicate client-side request errors (bad syntax, unauthorized, not found).

## networking/http/headers-and-bodies
interview-questions:
- [intern] Where does metadata go in an HTTP request versus actual data payload, headers or body?
- [junior] What are essential standard headers (`Authorization`, `Content-Type`, `Accept`, `Cache-Control`, `User-Agent`)?
- [mid] How do custom headers enable client tracking, telemetry, and API versioning?
- [senior] What goes wrong in production when bloated header sizes exceed server header buffer limits (431 Request Header Fields Too Large)?
what-interviewers-listen-for:
- Headers contain request metadata (auth, formats, caching); bodies contain the actual data payload.
- Excessive header sizes or cookie bloat trigger server rejection errors.
build-exercise: Add custom headers (`X-App-Version`, `X-Device-Id`) to an OkHttp/Retrofit request and verify delivery in network inspector.
official-references:
- MDN HTTP headers | https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers
common-wrong-beliefs:
- You can store large data payloads like images or JSON objects inside custom HTTP headers for convenience | HTTP headers are meant for metadata key-value pairs; large data payloads must reside in the HTTP request/response body.

## networking/http/stateless-requests
interview-questions:
- [intern] Why is HTTP described as a "stateless" protocol, and what does that mean for how apps prove who they are?
- [junior] How do stateless protocols require every request to carry authentication tokens or session identifiers?
- [mid] How do cookies and token headers bridge the gap between stateless HTTP transport and stateful user sessions?
- [senior] What goes wrong in production when mobile clients assume servers remember previous requests without passing full auth context?
what-interviewers-listen-for:
- HTTP treats every request as an independent transaction with no built-in memory of prior requests.
- Authentication tokens must accompany every single API request.
build-exercise: Inspect two consecutive API requests in Network Profiler to verify that authorization headers are sent identically on both.
official-references:
- MDN Overview of HTTP | https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview#http_is_stateless_but_not_sessionless
common-wrong-beliefs:
- Once a mobile app successfully logs in with a POST request, the server automatically remembers your session state for all subsequent GET requests without sending tokens | HTTP is stateless; subsequent requests must explicitly provide auth credentials (tokens/cookies).

## networking/tls/why-https
interview-questions:
- [intern] What can someone sitting on the same coffee shop Wi-Fi read if your mobile app uses plain HTTP instead of HTTPS?
- [junior] What three security guarantees does TLS (Transport Layer Security) provide over plain TCP (Encryption, Authentication, Data Integrity)?
- [mid] How does TLS encryption prevent Man-in-the-Middle (MitM) eavesdropping?
- [senior] What goes wrong in production when transmitting sensitive user data over unencrypted HTTP channels on public networks?
what-interviewers-listen-for:
- HTTPS encrypts traffic, hiding payload data and URL paths from local network sniffers.
- Encryption prevents eavesdropping; integrity prevents packet tampering; authentication proves server identity.
build-exercise: Attempt to inspect plain HTTP traffic versus encrypted HTTPS traffic using a local proxy tool (like Charles Proxy or Wireshark) to observe plaintext exposure.
official-references:
- MDN Transport Layer Security (TLS) | https://developer.mozilla.org/en-US/docs/Glossary/TLS
common-wrong-beliefs:
- Using plain HTTP for internal or development APIs is harmless as long as nobody is actively watching your network connection | Local network sniffers and proxy tools can capture plain HTTP traffic effortlessly at any time.

## networking/tls/certificates-and-trust
interview-questions:
- [intern] How does your phone know it is talking to the real server and not an imposter intercepting your connection?
- [junior] What is a Certificate Authority (CA), and how does the device trust store validate server certificates?
- [mid] What is the TLS Handshake process, and how are symmetric encryption keys negotiated after asymmetric certificate verification?
- [senior] What goes wrong in production when trusting user-installed CA certificates or disabling SSL validation checks, exposing apps to trivial MitM attacks?
what-interviewers-listen-for:
- Devices validate server certificates against a built-in store of trusted root Certificate Authorities.
- Asymmetric cryptography verifies identity during the handshake; fast symmetric encryption is used for data transmission.
build-exercise: Inspect an HTTPS server certificate chain in a web browser or network profiler to see the issuer CA and validity dates.
official-references:
- MDN Public Key Infrastructure | https://developer.mozilla.org/en-US/docs/Glossary/PKI
common-wrong-beliefs:
- Writing a custom OkHttp `TrustAllCertificates` trust manager to bypass SSL errors makes local development easier with zero security risk in testing builds | Bypassing SSL validation leaves apps vulnerable to active Man-in-the-Middle interception attacks.

## networking/mobile-networks-and-latency/latency-and-round-trips
interview-questions:
- [intern] Why are ten small sequential network requests significantly slower than one big aggregated request on mobile networks?
- [junior] What is network latency (Round Trip Time - RTT), and why does cellular network latency exceed home broadband latency?
- [mid] How do connection keep-alive headers and HTTP/2 multiplexing reduce round-trip overhead?
- [senior] What goes wrong in production when chatty API design (making dozens of sequential requests per screen) causes sluggish mobile UI performance?
what-interviewers-listen-for:
- Latency (RTT) dictates the time required for a packet to travel to the server and back; chatty APIs multiply this delay.
- HTTP/2 multiplexing allows concurrent streams over a single TCP connection, reducing handshake overhead.
build-exercise: Compare the total load time of making 5 sequential API calls vs 1 batched API call in an app trace.
official-references:
- Google Web Fundamentals RAIL performance model | https://developer.android.com/topic/performance/vitals/render
common-wrong-beliefs:
- Because high-speed 5G mobile networks exist, chatty API design with dozens of small sequential requests has zero performance impact | Cellular latency (RTT) is inherently higher than broadband; chatty requests multiply round-trip delays, degrading user experience.

## networking/mobile-networks-and-latency/flaky-mobile-networks
interview-questions:
- [intern] What happens to an active network request when a user walks into a tunnel, drops from 5G to 3G, or loses signal entirely?
- [junior] How do connection drops trigger socket exceptions (`IOException`, `SocketTimeoutException`)?
- [mid] How do robust mobile apps handle flaky networks using automatic retries, caching, and offline queues?
- [senior] what goes wrong in production when network failure handling crashes the app or leaves UI loading spinners spinning indefinitely on connection drops?
what-interviewers-listen-for:
- Mobile environments are inherently flaky; signal transitions throw socket and timeout exceptions.
- Apps must handle network disconnection gracefully with offline states and retry policies.
build-exercise: Simulate network disconnection in Android Studio Emulator (setting Network Speed to "No connection" or "GPRS"), and verify app error handling resilience.
official-references:
- Android connectivity monitoring | https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
common-wrong-beliefs:
- Mobile network connectivity is binary (either fully connected at high speed or completely offline) | Mobile connections fluctuate constantly between Wi-Fi, 5G, 4G, 3G, and dead zones, requiring robust handling of transient drops.

## networking/api-styles/rest-style-apis
interview-questions:
- [intern] What does a REST-style URL like `/users/42/posts` ask for?
- [junior] What are the core architectural principles of REST (Statelessness, Uniform Interface, Resource-based URLs)?
- [mid] How do REST resource naming conventions map HTTP methods (GET, POST, PUT, DELETE) to CRUD operations?
- [senior] What goes wrong in production when API designs violate REST principles by encoding complex state mutations into messy query parameters?
what-interviewers-listen-for:
- REST maps resources to clean URIs and uses HTTP methods to perform operations on those resources.
- Stateless design and uniform interfaces make APIs predictable and easy to cache.
build-exercise: Design a clean REST API endpoint structure for a shopping cart app (resources: `/carts`, `/items`).
official-references:
- MDN REST introduction | https://developer.mozilla.org/en-US/docs/Glossary/REST
common-wrong-beliefs:
- Any API that returns JSON over HTTP is automatically a fully compliant REST API | True REST APIs require adhering to resource-based URIs, standard HTTP methods, and stateless architectural constraints.

## networking/websockets/websockets-and-live-updates
interview-questions:
- [intern] How can a backend server send your phone a message without your app asking every few seconds?
- [junior] How does the WebSocket protocol upgrade from a standard HTTP request into a persistent full-duplex TCP socket connection?
- [mid] Why are WebSockets superior to HTTP polling for real-time bidirectional messaging (chat apps, live sports scores)?
- [senior] What goes wrong in production when WebSocket connections fail to implement heartbeat pings, leaving zombie connections open behind dropped cellular radios?
what-interviewers-listen-for:
- WebSockets establish a persistent, bidirectional full-duplex connection over a single TCP socket after an HTTP handshake upgrade.
- Heartbeat ping/pong frames detect dead connections when mobile radios drop silently.
build-exercise: Establish a WebSocket connection using Ktor or OkHttp, send a ping frame, and listen for incoming messages.
official-references:
- MDN WebSockets API | https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
common-wrong-beliefs:
- A WebSocket connection stays open forever automatically on a mobile device without any heartbeat ping management | Mobile OS power management and carrier NAT timeouts drop idle TCP sockets; heartbeat pings are mandatory to keep connections alive.

## networking/putting-it-together/diagnose-a-failed-request
interview-questions:
- [intern] Which step of the journey failed when a network request does not work (DNS lookup, TCP handshake, TLS certificate validation, HTTP response error)?
- [junior] How do you use Android Studio Network Profiler or Charles Proxy to isolate where a request failure occurred?
- [mid] What is the systematic troubleshooting flow for diagnosing an intermittent production network bug?
- [senior] What goes wrong in production when teams blame backend servers without inspecting client-side logs, proxy traces, or TLS handshakes?
what-interviewers-listen-for:
- Systematic diagnosis isolates layers: check if DNS resolves -> check if TCP connects -> check if TLS succeeds -> check HTTP status code.
- Network Profiler and packet inspection tools pinpoint exact failure points.
build-exercise: Intentionally break a network call (e.g., mistype domain name, invalid cert, wrong auth token) one by one, and observe the distinct exception thrown at each layer.
official-references:
- Inspect network traffic with Network Profiler | https://developer.android.com/studio/profile/network-profiler
common-wrong-beliefs:
- Whenever a network request fails in an app, the error message from the library always tells you the exact root cause immediately | Network libraries often throw generic `IOException` wrappers; inspecting profiler traces and exception causes is required to find the true layer failure.

## Self-check
1. Android connectivity overview | https://developer.android.com/training/volley/request-counter (Verify URL path)
2. MDN How the web works | https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/How_the_web_works (Verify MDN URL)
3. MDN HTTP overview | https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview (Verify MDN URL)
4. MDN IP address definition | https://developer.mozilla.org/en-US/docs/Glossary/IP_address (Verify MDN URL)
5. MDN What is DNS | https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_DNS (Verify MDN URL)
6. MDN TCP protocol guide | https://developer.mozilla.org/en-US/docs/Glossary/TCP (Verify MDN URL)
7. MDN HTTP request methods | https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods (Verify MDN URL)
8. MDN Transport Layer Security (TLS) | https://developer.mozilla.org/en-US/docs/Glossary/TLS (Verify MDN URL)
9. MDN WebSockets API | https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API (Verify MDN URL)
10. Inspect network traffic with Network Profiler | https://developer.android.com/studio/profile/network-profiler (Verify URL path)